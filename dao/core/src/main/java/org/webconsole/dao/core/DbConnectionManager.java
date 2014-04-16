package org.webconsole.dao.core;

import java.util.List;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Host;
import com.datastax.driver.core.Metadata;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;

public class DbConnectionManager
{
	private static DbConnectionManager instance = null;

	private Cluster cluster;
	private Session session;

	protected DbConnectionManager()
	{

	}

	public static DbConnectionManager getInstance()
	{
		if (instance == null)
		{
			instance = new DbConnectionManager();
		}

		return instance;
	}
	
	public ResultSet executeQuery(String query)
	{
		connect("127.0.0.1");
		ResultSet result = session.execute(query);
		disconnect();
		return result;
	}

	public Session connect(String node)
	{
		cluster = Cluster.builder().addContactPoint(node).build();
		Metadata metadata = cluster.getMetadata();

		for (Host host : metadata.getAllHosts())
		{
			System.out.printf("Datacenter: %s; Host: %s; Rack: %s\n", host.getDatacenter(), host.getAddress(), host.getRack());
		}
		
		session = cluster.connect();
		
		return session;

	}

	public void disconnect()
	{
		cluster.close();
	}
	
//	public static void main(String... args)
//	{
//		DbConnectionManager connectionManager = DbConnectionManager.getInstance();
//		Session session = connectionManager.connect("127.0.0.1");
//		String query = "SELECT * FROM cpu_keyspace.cpus;";
//		ResultSet resultSet = session.execute(query);
//		
//		List<Row> rows = resultSet.all();
//		
//		for(Row row: rows)
//		{
//			System.out.print(row.getInt("cpu_id") + " ");
//			System.out.println(row.getString("cpu_usage"));
//		}
//		
//		connectionManager.disconnect();
//	}

}
