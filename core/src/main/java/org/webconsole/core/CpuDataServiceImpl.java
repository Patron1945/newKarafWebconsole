package org.webconsole.core;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.cassandra.cli.CliParser.newColumnFamily_return;
import org.springframework.stereotype.Service;
import org.webconsole.api.CpuData;
import org.webconsole.api.CpuDataService;
import org.webconsole.dao.api.CpuDataApi;
import org.webconsole.dao.core.CpuDataApiImpl;
import org.webconsole.dao.core.DbConnectionManager;

//Path: /rest/userservices/cpudata/

// Przyklad zastosowania
// JaxRsClientFactoryBean.create()
// CpuDataService client = ...;
// clinet.getCpuDataXML();

@Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
@Path("/cpudata")
public class CpuDataServiceImpl implements CpuDataService
{
	
	CpuData cpuData = new CpuData() ;
	CpuDataApi cpuDataApi = new CpuDataApiImpl(DbConnectionManager
			.getInstance().connect("cpu_keyspace", "Test Node", "localhost", "9160"));
	
	@GET
	@Path("/")
	public CpuData getCpuData()
	{
		//Tymczasowo na stale zaimpelemntowane 10
		cpuData.setData(Long.toString(cpuDataApi.getCpuUsage(10)));
		return cpuData;
	}
	
	@POST
	@Path("/")
	public CpuData postCpuData(CpuData data)
	{
		cpuDataApi.postCpuUsage(Integer.valueOf(data.getData()));
		return getCpuData();
	}

}
