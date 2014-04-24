package org.webconsole.dao.api;

public interface CpuDataApi
{
	public Long getCpuUsage(int cpu_id);
	
	public String[] getAllCpuUsage();
}
