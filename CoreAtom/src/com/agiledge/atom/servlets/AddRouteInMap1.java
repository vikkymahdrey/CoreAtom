package com.agiledge.atom.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.agiledge.atom.service.RouteService;

public class AddRouteInMap1 extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		HttpSession session = req.getSession();
		String routename =req.getParameter("routename");
		String routeType=req.getParameter("type");
		String[] points= req.getParameterValues("points");
		String[] landmarkName= req.getParameterValues("points"); //req.getParameterValues("landmarkname");
		String[] empids=req.getParameterValues("empid");
		String vehiceType=req.getParameter("vt");
		int result=0;
		
		//AutoShuttleRoute
		if(req.getParameter("filter1")!=null){
			
			String[] compDist=req.getParameterValues("cmpnydist");
			String[] noodleDist=req.getParameterValues("noodledist");
			String time=req.getParameter("time1");
			String filter=req.getParameter("filter1");
			String triptime=req.getParameter("traveltime");
			String site=req.getParameter("site");
			result= new RouteService().createShuttleRoute1(routename, routeType, points, landmarkName,vehiceType,empids,time,compDist,noodleDist,filter,triptime,site);
			
			
		}else if(req.getParameterValues("cmpnydist")!=null){
			String[] compDist=req.getParameterValues("cmpnydist");
			String[] noodleDist=req.getParameterValues("noodledist");
			String time=req.getParameter("time1");
			result= new RouteService().createShuttleRoute(routename, routeType, points, landmarkName,vehiceType,empids,time,compDist,noodleDist);
			
		}else if(req.getParameter("time1")!=null){
			String time=req.getParameter("time1");
			result= new RouteService().createAutoRoutewithLog(routename, routeType, points, landmarkName,vehiceType,empids,time);
		}else{
			result= new RouteService().createAutoRoute(routename, routeType, points, landmarkName,vehiceType,empids);
		}	
		/*int status=0;
		if(points.length==landmarkName.length){
		  status=new RouteService().createRouteWithoutAPL(routename, routeType, points, landmarkName);
			for(int i=0;i<points.length;i++){
			System.out.println(points[i]+" points "+landmarkName[i]);
			}
		}*/
		if (result > 0) {
			session.setAttribute("status",
					"<div class='success'>Route Created Successfully</div");
			resp.sendRedirect("createShuttleRouteKeo.jsp");
		} else {
			session.setAttribute("status",
					"<div class='failure'>Route Creation Failed</div");
			resp.sendRedirect("createShuttleRouteKeo.jsp");
		}
		
	}

}
