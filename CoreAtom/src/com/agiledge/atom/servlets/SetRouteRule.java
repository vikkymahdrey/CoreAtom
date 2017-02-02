package com.agiledge.atom.servlets;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.agiledge.atom.dao.LogTimeDao;


/**
 * Servlet implementation class SetRouteRule
 */
public class SetRouteRule extends HttpServlet {

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	String[] shifts=request.getParameterValues("selectedshift");
	String weekoffstatus=request.getParameter("weekoffstatus");
	String siteId=request.getParameter("siteId");
	try {
		int value=new LogTimeDao().updateLogtimeCombainRoute(shifts,weekoffstatus,siteId);
		if (value > 0) {		
			request.getSession()
					.setAttribute("status",
							"<div class=\"success\" >  Combained Shift Updation Successful </div>");
		} else {
			request.getSession()
					.setAttribute("status",
							"<div class=\"failure\" > Combained Shift Updation Failed </div>");
		}

	} catch (SQLException e) {
		request.getSession()
		.setAttribute("status",	"<div class=\"failure\" > Combained Shift Updation Failed </div>");
		e.printStackTrace();
	}
	response.sendRedirect("routeSetup.jsp");
	}

}
