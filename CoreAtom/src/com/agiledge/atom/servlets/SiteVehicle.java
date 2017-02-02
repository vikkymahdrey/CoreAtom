/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.agiledge.atom.servlets;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.agiledge.atom.dto.SiteDto;
import com.agiledge.atom.service.SiteVehicleService;


/**
 * 
 * @author muhammad
 */
public class SiteVehicle extends HttpServlet {

	protected void processRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(true);
		String doneBy = session.getAttribute("user").toString();
		String vehicleTypeIds[] = request
				.getParameterValues("chosenVehicleType");
		String siteId = request.getParameter("siteId");
		ArrayList<SiteDto> siteDtoList = new ArrayList<SiteDto>();
		SiteDto SiteDtoObj = null;
		for (String vehicleType : vehicleTypeIds) {
			SiteDtoObj = new SiteDto();
			SiteDtoObj.setId(siteId);
			SiteDtoObj.setVehicleType(vehicleType);
			SiteDtoObj.setDoneBy(doneBy);
			siteDtoList.add(SiteDtoObj);
		}
		int status = new SiteVehicleService().setSiteVehicle(siteDtoList);

		if (status > 0) {
			request.getSession()
					.setAttribute("status",
							"<div class='success'>Site Vehicle Assign Successfully</div");
		} else {
			request.getSession().setAttribute("status",
					"<div class='failure'>Site Vehicle Assign Failed</div");
		}

		response.sendRedirect("site_vehicle_list.jsp");
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
