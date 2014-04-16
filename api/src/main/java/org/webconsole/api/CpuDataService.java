package org.webconsole.api;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.cxf.jaxrs.impl.NewCookieHeaderProvider;
import org.springframework.stereotype.Service;

//Path: /rest/userservices/cpudata/

@Path("/cpudata")
public class CpuDataService
{
	
	CpuData cpuData = new CpuData() ;
	
	@GET
	@Produces("application/xml")
	@Path("get/xml")
	public CpuData getCpuDataXML()
	{
		return cpuData;
	}
	
	@GET
	@Produces("application/json")
	@Path("get/json")
	public CpuData getCpuDataJSON()
	{
		return cpuData;
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_XML)
	@Produces(MediaType.APPLICATION_XML)
	@Path("post/xml")
	public CpuData putCpuDataXML(CpuData data)
	{
		cpuData = data;
		return data;
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("post/json")
	public CpuData putCpuDataJSON(CpuData data)
	{
		cpuData = data;
		return data;
	}
}
