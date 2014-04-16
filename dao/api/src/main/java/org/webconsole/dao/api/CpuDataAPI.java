package org.webconsole.dao.api;

import java.util.LinkedList;
import java.util.List;

import org.webconsole.dao.core.DbConnectionManager;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;

public class CpuDataAPI
{
	public String getCpuUsage(int cpu_id)
	{
		String result = "";
		String query = "SELECT cpu_usage FROM cpu_keyspace.cpus WHERE cpu_id = " +cpu_id +" ;";
		ResultSet resultSet = DbConnectionManager.getInstance().executeQuery(query);
		
		List<Row> rows = resultSet.all();
		
		for(Row row : rows)
			result = row.getString("cpu_usage");
		
		return result;
	}
	
	public Object[] getAllCpuUsage()
	{
		String[] result;
		String query = "SELECT cpu_usage FROM cpu_keyspace.cpus;";
		ResultSet resultSet = DbConnectionManager.getInstance().executeQuery(query);
		
		List<Row> rows = resultSet.all();
		LinkedList<String> linkedList = new LinkedList<String>();
		
		for(Row row : rows)
			linkedList.add(row.getString("cpu_usage"));
		
		result = new String[linkedList.size()];
		result = linkedList.toArray(result);
		
		return result;
	}
	
	public static void main(String... args)
	{
		CpuDataAPI cpuDataAPI = new CpuDataAPI();
		System.out.println(cpuDataAPI.getCpuUsage(1));
		System.out.println(cpuDataAPI.getCpuUsage(2));
		System.out.println(cpuDataAPI.getCpuUsage(3));
		
		String[] tmpStrTab = (String[]) cpuDataAPI.getAllCpuUsage();
		for(String str: tmpStrTab)
			System.out.println(str);
	}
}
