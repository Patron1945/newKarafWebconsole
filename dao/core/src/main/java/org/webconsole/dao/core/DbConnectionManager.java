package org.webconsole.dao.core;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.apache.cassandra.cli.CliParser.columnFamily_return;
import org.apache.cassandra.cli.CliParser.keyRangeExpr_return;

import com.google.common.collect.ImmutableMap;
import com.netflix.astyanax.AstyanaxContext;
import com.netflix.astyanax.ExceptionCallback;
import com.netflix.astyanax.Keyspace;
import com.netflix.astyanax.MutationBatch;
import com.netflix.astyanax.connectionpool.NodeDiscoveryType;
import com.netflix.astyanax.connectionpool.OperationResult;
import com.netflix.astyanax.connectionpool.exceptions.ConnectionException;
import com.netflix.astyanax.connectionpool.impl.ConnectionPoolConfigurationImpl;
import com.netflix.astyanax.connectionpool.impl.CountingConnectionPoolMonitor;
import com.netflix.astyanax.ddl.ColumnFamilyDefinition;
import com.netflix.astyanax.ddl.KeyspaceDefinition;
import com.netflix.astyanax.ddl.SchemaChangeResult;
import com.netflix.astyanax.impl.AstyanaxConfigurationImpl;
import com.netflix.astyanax.model.Column;
import com.netflix.astyanax.model.ColumnFamily;
import com.netflix.astyanax.model.ColumnList;
import com.netflix.astyanax.model.Row;
import com.netflix.astyanax.model.Rows;
import com.netflix.astyanax.serializers.IntegerSerializer;
import com.netflix.astyanax.serializers.LongSerializer;
import com.netflix.astyanax.serializers.StringSerializer;
import com.netflix.astyanax.thrift.ThriftFamilyFactory;
import com.netflix.astyanax.util.RangeBuilder;

public class DbConnectionManager
{
	private static DbConnectionManager instance = null;

	AstyanaxContext<Keyspace> context;
	Keyspace keyspace;

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

	public Keyspace connect(String keyspaceName, String nodeName, String host, String port)
	{
		// Cassandra
		context = new AstyanaxContext.Builder()
				.forCluster(nodeName)
				.forKeyspace(keyspaceName)
				.withAstyanaxConfiguration(
						new AstyanaxConfigurationImpl()
								.setDiscoveryType(NodeDiscoveryType.RING_DESCRIBE)
								.setCqlVersion("3.0.0").setTargetCassandraVersion("2.0.6"))
				.withConnectionPoolConfiguration(
						new ConnectionPoolConfigurationImpl("MyConnectionPool").setPort(9160)
								.setMaxConnsPerHost(1).setSeeds(host + ":" + port))
				.withConnectionPoolMonitor(new CountingConnectionPoolMonitor())
				.buildKeyspace(ThriftFamilyFactory.getInstance());

		context.start();
		keyspace = context.getClient();

		return keyspace;

	}

	public static void main(String... args)
	{
//		// Connection
//		DbConnectionManager connectionManager = DbConnectionManager.getInstance();
//		Keyspace keyspace = connectionManager.connect("cpu_keyspace", "Test Claster", "localhost",
//				"9160");
//
//		// Rows retrieving
//		Rows<Integer, Long> rows;
//		try
//		{
//			rows = keyspace.prepareQuery(connectionManager.CF_CPU_INFO).getAllRows()
//					.setRowLimit(10).withColumnRange(new RangeBuilder().setLimit(10).build())
//					.setExceptionCallback(new ExceptionCallback()
//					{
//						@Override
//						public boolean onException(ConnectionException e)
//						{
//							try
//							{
//								Thread.sleep(1000);
//							}
//							catch (InterruptedException e1)
//							{
//							}
//							return true;
//						}
//					}).execute().getResult();
//
//			Iterator<Row<Integer, Long>> iterator = rows.iterator();
//
//			while (iterator.hasNext())
//			{
//
//				Row<Integer, Long> next = iterator.next();
//
//				System.out.println("RowKey: " + next.getKey());
//				System.out.println("RawRowKey: " + next.getRawKey());
//
//				ColumnList<Long> columns = next.getColumns();
//
//				System.out.println(columns.getColumnByIndex(0).getStringValue());
//				// System.out.println(columns.getColumnByIndex(0).getIntegerValue());
//
//				Iterator<Long> iterator2 = columns.getColumnNames().iterator();
//				while (iterator2.hasNext())
//				{
//					Long col = iterator2.next();
//					System.out.println("ColumnNames: " + col + " "
//							+ columns.getIntegerValue(col, null));
//				}
//
//			}
//
//		}
//		catch (ConnectionException e)
//		{
//
//		}

		// This will never throw an exception
		// for (Row<String, String> row : rows.g) {
		// System.out.println("ROW: " + row.getKey() + " " +
		// row.getColumns().size());
		// }

	}

}
