package org.webconsole.api;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "cpudata")
public class CpuData implements Serializable
{
	private String data;

	public String getData()
	{
		return data;
	}
	
	public void setData(String data)
	{
		this.data = data;	
	}
}
