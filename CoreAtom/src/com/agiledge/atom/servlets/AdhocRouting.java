package com.agiledge.atom.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.agiledge.atom.service.AdhocRoutingService;

/**
 * Servlet implementation class AdhocRouting
 */
public class AdhocRouting extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AdhocRouting() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String siteId = request.getParameter("siteId");	
		String date = request.getParameter("date");
		int retVal = new AdhocRoutingService().doAdhocRouting(siteId, date);
		if (retVal > 0) {
			response.sendRedirect("showAdhocTrips.jsp?siteId=" + siteId
					+ "&date=" + date + "");
		} else
			{response.sendRedirect("adhocRouting.jsp");}	
	

}
}
