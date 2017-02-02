/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.agiledge.atom.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.agiledge.atom.commons.OtherFunctions;
import com.agiledge.atom.dao.LogTimeDao;
import com.agiledge.atom.dto.LogTimeDto;


/**
 * 
 * @author muhammad
 */
public class LogTime extends HttpServlet {

	/**
	 * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
	 * methods.
	 * 
	 * @param request
	 *            servlet request
	 * @param response
	 *            servlet response
	 * @throws ServletException
	 *             if a servlet-specific error occurs
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	protected void processRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(true);
		String doneBy = session.getAttribute("user").toString();
		String logTime = request.getParameter("logTime");
		String logtype = request.getParameter("logtype");
		LogTimeDto logTimeDtoObj = new LogTimeDto();
		try {
			logTimeDtoObj.setLogTime(logTime);
			logTimeDtoObj.setDoneBy(doneBy);
			logTimeDtoObj.setLogtype(logtype);
			String errorMessage="";
			if(OtherFunctions.isEmpty(logTime)) {
				errorMessage = "Log Time is empty";
			} else if(OtherFunctions.isEmpty(logtype)) {
				errorMessage = "Log Type is empty";
			} else 	if(new LogTimeDao().insertLogtime(logTimeDtoObj)>0) {
				session.setAttribute("status",
						"<div class=\"success\" width=\"100%\" > Time insertion successful</div>");
			}else {
				session.setAttribute("failure",
						"<div class=\"success\" width=\"100%\" > Time insertion failed !</div>");
			}
			if(!errorMessage.equals("")) {
				session.setAttribute("failure",
						"<div class=\"success\" width=\"100%\" > Validation Error : " + errorMessage + "</div>");
			}
			response.sendRedirect("logtime_list.jsp");
		} catch (Exception e) {
			// System.out.println("Error"+e);
			response.sendRedirect("logtime_list.jsp");
		}
	}

	// <editor-fold defaultstate="collapsed"
	// desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
	/**
	 * Handles the HTTP <code>GET</code> method.
	 * 
	 * @param request
	 *            servlet request
	 * @param response
	 *            servlet response
	 * @throws ServletException
	 *             if a servlet-specific error occurs
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}

	/**
	 * Handles the HTTP <code>POST</code> method.
	 * 
	 * @param request
	 *            servlet request
	 * @param response
	 *            servlet response
	 * @throws ServletException
	 *             if a servlet-specific error occurs
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}

	/**
	 * Returns a short description of the servlet.
	 * 
	 * @return a String containing servlet description
	 */
	@Override
	public String getServletInfo() {
		return "Short description";
	}// </editor-fold>
}
