package com.agiledge.atom.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.agiledge.atom.service.ShuttleSocketService;

public class employeeTripping extends HttpServlet {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		
		HttpSession session = req.getSession();
		String site=req.getParameter("siteId");
		String empid[]=req.getParameterValues("empids");
		String inTime=req.getParameter("in");
		String outTime= req.getParameter("out");
		String route=req.getParameter("route1");
		String landmark=req.getParameter("apl1");
		String route1=req.getParameter("route2");
		String landmark1=req.getParameter("apl2");
		if(empid!=null){

			String empids[]=new String[empid.length];
		for (int i = 0; i < empids.length; i++) {
			empids[i]=(empid[i].split(":"))[0];
		}
		int result= new ShuttleSocketService().setEmployeeInOut(site,empids,inTime,outTime,route,landmark,route1,landmark1);
		if(result>0){
				session.setAttribute("status",
						"<div class=\"success\" > Successfull</div>");
			} else {
				session.setAttribute("status",
						"<div class=\"failure\" > failed !</div>");
			}
		}else{
			session.setAttribute("status",
					"<div class=\"failure\" > select atleast 1 employee</div>");
		}
		resp.sendRedirect("addShuttleSchedule.jsp");
	}

}
