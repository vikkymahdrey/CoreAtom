package com.agiledge.atom.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.agiledge.atom.dto.GeoTagDto;
import com.agiledge.atom.service.ShuttleSocketService;

public class GetEmployeeNearNoodle extends HttpServlet{
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		String[] points=req.getParameterValues("points");
		System.out.println(points.length);
		String distanceConst=req.getParameter("dist");
		PrintWriter out=resp.getWriter();
		String[] routes=req.getParameterValues("route");
		ArrayList<GeoTagDto> list=null;
		if(req.getParameter("filter")!=null){
			String logtype=req.getParameter("logtype");
			String time=req.getParameter("logtime");
			String filter=req.getParameter("filter");
			String siteid=req.getParameter("site");
			list= new ShuttleSocketService().getEmployeeNearNoodlePointsWithLog2(points,distanceConst,routes,logtype,time,filter,siteid);
		}else if(req.getParameter("type")!=null){
			String logtype=req.getParameter("logtype");
			String time=req.getParameter("logtime");
			list= new ShuttleSocketService().getEmployeeNearNoodlePointsForShuttle(points,distanceConst,routes,logtype,time);	
				
		}else if(req.getParameter("logtype")!=null){
			String logtype=req.getParameter("logtype");
			String time=req.getParameter("logtime");
			list= new ShuttleSocketService().getEmployeeNearNoodlePointsWithLog(points,distanceConst,routes,logtype,time);
		}else{
			list= new ShuttleSocketService().getEmployeeNearNoodlePoints(points,distanceConst,routes);
		}
		 
		String details="";
		for (GeoTagDto geoTagDto : list) {
			String data="";
			data=geoTagDto.getEmpid()+"#"+geoTagDto.getEmpName()+"#"+geoTagDto.getPersonnelNo()+"#"+geoTagDto.getDistanceperadmin()+"#"+geoTagDto.getDistanceperadminout()+"#"+geoTagDto.getHomelat()+"#"+geoTagDto.getHomelong();
			details+="/"+data;
		}
		out.print(details);
		System.out.println(list.size());
		out.flush();
		out.close();
		
	}
}
