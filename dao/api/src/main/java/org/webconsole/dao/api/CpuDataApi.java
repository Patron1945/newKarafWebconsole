package org.webconsole.dao.api;

import java.util.Map;

public interface CpuDataApi
{
	public Long getCpuUsage(int cpu_id);
	
	public Map<Long, Long> getAllCpuUsage(int rowKey);
	
	public void postCpuUsage(int cpu_usage);
}
