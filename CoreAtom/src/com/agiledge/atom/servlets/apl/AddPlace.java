/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.agiledge.atom.servlets.apl;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.agiledge.atom.dto.APLDto;
import com.agiledge.atom.service.APLService;


/**
 * 
 * @author muhammad
 */
public class AddPlace extends HttpServlet {

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
		String place = request.getParameter("place");
		String areaId = request.getParameter("areaId");
		String doneBy = session.getAttribute("user").toString();
		String isShuttle = request.getParameter("isShuttle");
		APLDto APLDtoObj = new APLDto();
		APLDtoObj.setAreaID(areaId);
		APLDtoObj.setPlace(place);
		APLDtoObj.setDoneby(doneBy);
		int status = 0;
		if (isShuttle == null) {
			status = new APLService().insertPlace(APLDtoObj);
		} else {
			status = new APLService().insertShuttlePlace(APLDtoObj);

		}
		if (status == 100) {
			request.getSession()
					.setAttribute("status",
							"<div class=\"failure\" > Place already exist in the area</div>");
		} else if (status > 0) {
			request.getSession().setAttribute("status",
					"<div class=\"success\" > New Place added</div>");
		} else {
			request.getSession().setAttribute("status",
					"<div class=\"failure\" > Place adding failed</div>");
		}
		if (isShuttle == null)
			response.sendRedirect("place.jsp?areaId=" + areaId + "");
		else
			response.sendRedirect("placeShuttle.jsp?areaId=" + areaId + "");
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
