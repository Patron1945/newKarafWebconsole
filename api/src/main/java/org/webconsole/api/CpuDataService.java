package org.webconsole.api;

import javax.ws.rs.PathParam;

public interface CpuDataService
{
	CpuData getCpuData(String id);

	String getRowCpuData(String id);
	
	String getAllCpuData();
	
	void postCpuData(CpuData cpudata);
}