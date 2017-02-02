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
public class UpdateLandmark extends HttpServlet {

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
		String placeId = request.getParameter("placeId");
		String landmarkId = request.getParameter("landmarkId");
		String landmark = request.getParameter("landmark");
		String isShuttle = request.getParameter("isShuttle");
		APLDto APLDtoObj = new APLDto();
		APLDtoObj.setPlaceID(placeId);
		APLDtoObj.setLandMarkID(landmarkId);
		APLDtoObj.setLandMark(landmark);
		APLDtoObj.setDoneby(doneBy);
		String latLng= request.getParameter("latlng");
		if(latLng!=null&&latLng.trim().equals("")==false)  {			
		String latitude=latLng.split(",")[0].substring(1,  latLng.split(",")[0].length()-1);
		String longitude = latLng.split(",")[1].substring(1, latLng.split(",")[1].length()-1);
			 APLDtoObj.setLattitude(latitude);
			 APLDtoObj.setLongitude(longitude);
			 APLDtoObj.setGeocodeUpdateMode(true);			
		}				
		if(APLDtoObj.isGeocodeUpdateMode()) {
			new APLService().updateLandmarkPostion(APLDtoObj);
			response.sendRedirect("marklandmark.jsp");
		}
		else if (isShuttle == null) {
			new APLService().updateLandmark(APLDtoObj);
			response.sendRedirect("landmark.jsp?placeId=" + placeId + "");
		}

		else {
			new APLService().updateShuttleLandmark(APLDtoObj);
			response.sendRedirect("landmarkShuttle.jsp?placeId=" + placeId + "");
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
