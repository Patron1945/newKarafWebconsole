package org.webconsole.core;

import java.beans.XMLEncoder;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
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
	
	@GET
	@Path("/all")
	public String getAllCpuData()
	{
		Map<Long, Long> allCpuUsage = cpuDataApi.getAllCpuUsage(10);
//		ByteArrayOutputStream map = new ByteArrayOutputStream();  
//		XMLEncoder xmlEncoder = new XMLEncoder(map);  
//		xmlEncoder.writeObject(allCpuUsage);  
//		xmlEncoder.flush();  
		ObjectMapper mapper = new ObjectMapper();
		String writeValueAsString = null;
		try
		{
			writeValueAsString = mapper.writeValueAsString(allCpuUsage);
		}
		catch (JsonGenerationException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (JsonMappingException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 	
		return writeValueAsString;
	}
	
	@POST
	@Path("/")
	public CpuData postCpuData(CpuData data)
	{
		cpuDataApi.postCpuUsage(Integer.valueOf(data.getData()));
		return getCpuData();
	}

}
