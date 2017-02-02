package com.agiledge.atom.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.agiledge.atom.dto.LogTimeDto;
import com.agiledge.atom.dto.RouteDto;
import com.agiledge.atom.service.LogTimeService;
import com.agiledge.atom.service.RouteService;

public class GetLogTimeAndRouteBasedOnType extends HttpServlet{

	/**
	 * 
	 */
	
	private static final long serialVersionUID = 1L;
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String logtype= req.getParameter("logtype");
		String time=req.getParameter("previoustime");
		Integer routeid=Integer.parseInt(req.getParameter("previousroute"));
		PrintWriter out = resp.getWriter();
		String outputString="";
		//only for keonics 
		ArrayList<RouteDto> routes = new RouteService().getAllRoutesWithlog("1", logtype);
		ArrayList<LogTimeDto> Log = new LogTimeService().getAllLogtime(logtype);
		for(LogTimeDto logdto : Log){
		 String selected="";
		 if(logdto.getLogTime().equalsIgnoreCase(time)){
			 selected="selected";
		 }
		 outputString+="<option value='"+logdto.getLogTime()+"' "+selected+">"+logdto.getLogTime()+"</option>";
	 }
	 outputString+="$";
	 for (RouteDto RouteDto : routes) {
		 String selected="";
		 if(RouteDto.getRouteId()==routeid){
			 selected="selected";
		 }
		outputString+="<option value='"+RouteDto.getRouteId()+"' "+selected+">"+RouteDto.getRouteName()+"</option>";
	}
	 out.print(outputString);
	}

}
