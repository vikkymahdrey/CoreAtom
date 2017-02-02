package com.agiledge.atom.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.agiledge.atom.service.RouteService;

public class UpdateAutoRoute extends HttpServlet{

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		HttpSession session = req.getSession();
		String routename =req.getParameter("routename");
		String routeType=req.getParameter("type1");
		String[] points= req.getParameterValues("points");
		String[] landmarkName= req.getParameterValues("points"); //req.getParameterValues("landmarkname");
		String[] empids=req.getParameterValues("empid");
		String vehiceType=req.getParameter("vt");
		String routeid=req.getParameter("routeid");
		int result=0;
		if(req.getParameter("filter1")!=null){
			String[] compDist=req.getParameterValues("cmpnydist");
			String[] noodleDist=req.getParameterValues("noodledist");
			String time=req.getParameter("time1");
			String filter=req.getParameter("filter1");
			String siteid=req.getParameter("site");
			String traveltime=req.getParameter("traveltime");
			result= new RouteService().UpdateAutoRouteWithFilter(siteid,routeid,routename, routeType, points, landmarkName,vehiceType,empids,time,compDist,noodleDist,filter,traveltime);
		}else if(req.getParameter("time1")!=null){
			result= new RouteService().UpdateAutoRouteWithLog(routeid,routename, routeType, points, landmarkName,vehiceType,empids,req.getParameter("time1"));
		}else{
			result= new RouteService().UpdateAutoRoute(routeid,routename, routeType, points, landmarkName,vehiceType,empids);
		
		}
		if (result > 0) {
			session.setAttribute("status",
					"<div class='success'>Route Updated Successfully</div");
			resp.sendRedirect("createShuttleRouteKeo.jsp");
		} else {
			session.setAttribute("status",
					"<div class='failure'>Route Updation Failed</div");
			resp.sendRedirect("createShuttleRouteKeo.jsp");
		}
		
	}
}
