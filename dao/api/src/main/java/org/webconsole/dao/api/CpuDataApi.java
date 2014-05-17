package org.webconsole.dao.api;

import java.util.Map;

public interface CpuDataApi
{
	Long getCpuUsage(int cpu_id);
	
	Map<Integer, Map<Long, Long>> getAllCpuUsage();
	
	Map<Long, Long> getRowCpuUsage(int rowKey);
	
	void postCpuUsage(int cpu_usage);
}
