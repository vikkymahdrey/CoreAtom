package com.agiledge.atom.servlets;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.agiledge.atom.service.VendorService;


public class AssignVendor extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) {
		// TODO Auto-generated method stub
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		HttpSession session=request.getSession();
		String tripDate = request.getParameter("tripDate");
		String tripTime = request.getParameter("tripTime");
		String tripMode = request.getParameter("tripMode");
		String siteId = request.getParameter("siteId");
		int retrVal = 0;
		try {
			String vendorId = request.getParameter("vendor");
			String[] trips = request.getParameterValues("selectedtrip");
			retrVal = new VendorService().assignVendorTrip(vendorId, trips);
			if(retrVal>0)
			{
				session.setAttribute("status",
						"<div class=\"success\" > Trip successfully Assigned to vendor </div>");
			} else {
				session.setAttribute("status",
						"<div class=\"failure\" > Trip Assigned to vendor Failed!</div>");	
			}

		} catch (Exception e) {
			System.out.println("error"+e);
			session.setAttribute("status",
					"<div class=\"failure\" > Trip Assigned to vendor Failed!</div>");
		}
		response.sendRedirect("assignVendor.jsp?siteId=" + siteId+ "&tripDate=" + tripDate + "&tripTime=" + tripTime+ "&tripMode=" + tripMode + "");
	}

}
