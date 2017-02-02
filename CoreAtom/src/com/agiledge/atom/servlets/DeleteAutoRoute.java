package com.agiledge.atom.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.agiledge.atom.service.RouteService;

public class DeleteAutoRoute extends HttpServlet{

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String routeid=req.getParameter("routeid");
		PrintWriter out= resp.getWriter();
		String message="";
		int result=new RouteService().disableAutoRoute(routeid);
		if(result>0){
			message="Route deleted successfully";
		}else{
			message="Failed";
		}
		out.print(message);
	}
}
