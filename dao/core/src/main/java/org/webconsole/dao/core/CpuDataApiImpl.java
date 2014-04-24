package org.webconsole.dao.core;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.webconsole.dao.api.CpuDataApi;
import org.webconsole.dao.core.DbConnectionManager;

import com.netflix.astyanax.Keyspace;
import com.netflix.astyanax.connectionpool.OperationResult;
import com.netflix.astyanax.connectionpool.exceptions.ConnectionException;
import com.netflix.astyanax.model.Column;
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
	}

	public Long getCpuUsage(int cpu_id)
	{
		Long result = null;
		
		//Bierzemy tylko ostani wynik - tak dla ulatwienia
		try
		{
			OperationResult<ColumnList<Long>> operationResult = keyspace.prepareQuery(CF_CPU_INFO)
					.getKey(cpu_id).execute();

			ColumnList<Long> columnList = operationResult.getResult();

			result = columnList.getColumnByIndex(columnList.size()-1).getLongValue();

		}
		catch (ConnectionException e)
		{
			e.printStackTrace();
		}
		
		return result;
	}

	public String[] getAllCpuUsage()
	{
		String[] result = new String[5];
		//
		//
		return result;
	}

	public void initialize()
	{

		// keyspace.createColumnFamily(null);
	}

	public static void main(String... args)
	{
		DbConnectionManager connectionManager = DbConnectionManager.getInstance();

		CpuDataApiImpl cpuDataApiImpl = new CpuDataApiImpl(connectionManager.connect(
				"cpu_keyspace", "Test Node", "localhost", "9160"));
		
		System.out.print(cpuDataApiImpl.getCpuUsage(10));

	}
}
