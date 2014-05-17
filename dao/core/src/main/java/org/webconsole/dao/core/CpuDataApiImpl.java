package org.webconsole.dao.core;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.webconsole.dao.api.CpuDataApi;

import com.google.common.collect.ImmutableMap;
import com.netflix.astyanax.Keyspace;
import com.netflix.astyanax.MutationBatch;
import com.netflix.astyanax.connectionpool.OperationResult;
import com.netflix.astyanax.connectionpool.exceptions.ConnectionException;
import com.netflix.astyanax.ddl.ColumnFamilyDefinition;
import com.netflix.astyanax.ddl.SchemaChangeResult;
import com.netflix.astyanax.model.ColumnFamily;
import com.netflix.astyanax.model.ColumnList;
import com.netflix.astyanax.serializers.IntegerSerializer;
import com.netflix.astyanax.serializers.LongSerializer;

public class CpuDataApiImpl implements CpuDataApi
{
	private Keyspace keyspace;

	public ColumnFamily<Integer, Long> CF_CPU_INFO = new ColumnFamily<Integer, Long>("cpus",
			IntegerSerializer.get(), LongSerializer.get(), IntegerSerializer.get());

	public CpuDataApiImpl(Keyspace keyspace)
	{
		this.keyspace = keyspace;
//		initialize(); wywolac te metode w springu
	}

	public Long getCpuUsage(int cpu_id)
	{
		Long result = null;

		// Bierzemy tylko ostani wynik - tak dla ulatwienia
		try
		{
			OperationResult<ColumnList<Long>> operationResult = keyspace.prepareQuery(CF_CPU_INFO)
					.getKey(cpu_id).execute();

			ColumnList<Long> columnList = operationResult.getResult();

			result = columnList.getColumnByIndex(columnList.size() - 1).getLongValue();

		}
		catch (ConnectionException e)
		{
			e.printStackTrace();
		}

		return result;
	}
	
	public Map<Integer, Map<Long, Long>> getAllCpuUsage()
	{
		//Tymczasowo pobieramy tylko cpu 10 i 11
		HashMap<Integer, Map<Long, Long>> map = new HashMap<Integer, Map<Long,Long>>();
		
		for(int i = 10; i < 12; i++)
		{
			map.put(i, getRowCpuUsage(i));
		}
		
		return map;
	}
	
	public Map<Long, Long> getRowCpuUsage(int rowKey)
	{
		LinkedHashMap<Long, Long> result = new LinkedHashMap<Long, Long>();
		
		OperationResult<ColumnList<Long>> operationResult;
		try
		{
			operationResult = keyspace.prepareQuery(CF_CPU_INFO).getKey(rowKey).execute();
			ColumnList<Long> columnList = operationResult.getResult();	
			
			for(int i = 0; i < columnList.size(); i++)
			{
				result.put(columnList.getColumnByIndex(i).getName(), columnList.getColumnByIndex(i).getLongValue());
			}
			
		}
		catch (ConnectionException e)
		{
			e.printStackTrace();
		}
		
		return result;
	}
	
	// Oczywiscie ksztalt funkcji do ustalenia :)
	public void postCpuUsage(int cpu_usage)
	{
		MutationBatch mutationBatch = keyspace.prepareMutationBatch();
		
		mutationBatch.withRow(CF_CPU_INFO, 10).putColumn(System.currentTimeMillis(), cpu_usage);
		
		try
		{
			mutationBatch.execute();
		}
		catch (ConnectionException e)
		{
			e.printStackTrace();
		}
	}

	public void initialize() throws Exception
	{
		
		try
		{
			ColumnFamilyDefinition columnFamily = keyspace.describeKeyspace().getColumnFamily(
					CF_CPU_INFO.getName());
			
			if(columnFamily == null)
			{
				ImmutableMap<String, Object> properties = new ImmutableMap.Builder<String, Object>()
						.put("name", CF_CPU_INFO.getName())
						.put("comparator_type", CF_CPU_INFO.getColumnSerializer())
						.put("key_validation_class", CF_CPU_INFO.getKeySerializer())
						.put("default_validation_class", CF_CPU_INFO.getDefaultValueSerializer())
						.put("comment", "Created by IPF persistence layer at " + new Date().toString())
						.build();
			
				OperationResult<SchemaChangeResult> result = keyspace.createColumnFamily(properties);
			}
		}
		catch (ConnectionException e)
		{
			throw new Exception("Can not start DAO", e);
		}
	}

	public static void main(String... args)
	{
		DbConnectionManager connectionManager = DbConnectionManager.getInstance();

		CpuDataApiImpl cpuDataApiImpl = new CpuDataApiImpl(connectionManager.connect(
				"cpu_keyspace", "Test Node", "localhost", "9160"));

		System.out.print(cpuDataApiImpl.getCpuUsage(10));
		
		cpuDataApiImpl.postCpuUsage(18);
		
		Map<Long, Long> allCpuUsage = cpuDataApiImpl.getRowCpuUsage(10);
		
		Map<Integer, Map<Long, Long>> allCpuUsage2 = cpuDataApiImpl.getAllCpuUsage();
		System.out.println();
		for(Integer i : allCpuUsage2.keySet())
		{
			System.out.println("i: " + i);
			for(Long l : allCpuUsage2.get(i).keySet())
			{
				System.out.println(allCpuUsage2.get(i).get(l));
			}
		}
		
		

	}
}
