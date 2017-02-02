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
public class UpdatePlace extends HttpServlet {

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
		System.out.print("..................sdf.sdfsdfsdf ................");
		HttpSession session = request.getSession(true);
		String doneBy = session.getAttribute("user").toString();
		String placeId = request.getParameter("placeId");
		String place = request.getParameter("place");
		String areaId = request.getParameter("areaId");
		String isShuttle=request.getParameter("isShuttle");
		APLDto APLDtoObj = new APLDto();
		APLDtoObj.setAreaID(areaId);
		APLDtoObj.setPlaceID(placeId);
		APLDtoObj.setDoneby(doneBy);
		APLDtoObj.setPlace(place);
		if(isShuttle==null)
		{
			new APLService().updatePlace(APLDtoObj);
			response.sendRedirect("place.jsp?areaId=" + areaId + "");
		}		
			else
			{
				new APLService().updateShuttlePlace(APLDtoObj);
				response.sendRedirect("placeShuttle.jsp?areaId=" + areaId + "");
			}
		
		// System.out.println(" Place.....  "+ place);
		
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
