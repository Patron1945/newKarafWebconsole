package org.webconsole.ui;

import java.net.URL;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;


@Produces({MediaType.TEXT_HTML})
@Path("/interface")
public class ChartUI
{
	
	@GET
	@Path("/")
	public String getChart()
	{
		ClassLoader loader = this.getClass().getClassLoader();
		URL resource = loader.getResource("jquery-2.1.1.min.js");
		StringBuilder chartWebsite = new StringBuilder();
		
		chartWebsite.append("<!DOCTYPE html>");
		chartWebsite.append("<html>");
		chartWebsite.append("<head>");
		chartWebsite.append("<script src=\"jquery.js\"></script>");
		chartWebsite.append("<body>");
		chartWebsite.append("Hello Moto!!!");
		chartWebsite.append("</body>");
		chartWebsite.append("</head>");
		chartWebsite.append("</html>");
		
		
		return chartWebsite.toString();
	}
}
