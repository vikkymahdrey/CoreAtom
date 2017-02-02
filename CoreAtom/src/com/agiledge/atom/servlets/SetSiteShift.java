package com.agiledge.atom.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.agiledge.atom.dao.LogTimeDao;


/**
 * Servlet implementation class SetSiteShift
 */
public class SetSiteShift extends HttpServlet {

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String[] shifts=request.getParameterValues("selectedshift");
		String siteId=request.getParameter("siteId");
		try {
			int value=new LogTimeDao().updateSiteShift(shifts,siteId);
			if (value > 0) {		
				request.getSession()
						.setAttribute("status",
								"<div class=\"success\" >  Site Shift Updation Successful </div>");
			} else {
				request.getSession()
						.setAttribute("status",
								"<div class=\"failure\" > Site Shift Updation Failed </div>");
			}

		} catch (Exception e) {
			request.getSession()
			.setAttribute("status",	"<div class=\"failure\" > Site Shift Updation Failed </div>");
			e.printStackTrace();
		}
		response.sendRedirect("site_shift.jsp");

	}

}
