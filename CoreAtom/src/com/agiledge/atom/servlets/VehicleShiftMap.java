package com.agiledge.atom.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.agiledge.atom.service.VehicleService;

public class VehicleShiftMap extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		HttpSession session = req.getSession();
		String siteId= req.getParameter("site");
		String vehicle= req.getParameter("vehicle");
		String[] inT=req.getParameterValues("in");
		String[] outT=req.getParameterValues("out");
		String routeIn=req.getParameter("routeIn");
		String routeOut=req.getParameter("routeIn");
		int result=0;
		if(inT!=null || outT!=null){
			result= new VehicleService().mapVehicleWithShift(siteId,vehicle,inT,outT,routeIn,routeOut);
			if(result>0){
				session.setAttribute("status",
						"<div class=\"success\" > Successfull</div>");
			}else{
				session.setAttribute("status",
						"<div class=\"failure\" > Failed!</div>");
			}
		}else{
			session.setAttribute("status",
					"<div class=\"failure\" > select  atleast one Time</div>");
		}
		resp.sendRedirect("vehicleShiftMap.jsp");
	}

}
