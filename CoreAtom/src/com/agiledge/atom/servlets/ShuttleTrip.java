package com.agiledge.atom.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.agiledge.atom.commons.OtherFunctions;
import com.agiledge.atom.service.ShuttleRoutingService;

/**
 * Servlet implementation class shuttleTrip
 */

public class ShuttleTrip extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String siteId = request.getParameter("siteId");
		String date = OtherFunctions.changeDateFromatToIso(request
				.getParameter("tripDate"));		
		String inOutTime = request.getParameter("tripTime");
		String inOut = request.getParameter("tripMode");
		int retVal=new ShuttleRoutingService().shttleRouting(siteId, date, inOut, inOutTime);
		response.sendRedirect("shuttleTrip.jsp?siteId="+siteId+"&tripDate="+date+"&tripTime="+inOutTime+"&tripMode="+inOut);				
	}

}
