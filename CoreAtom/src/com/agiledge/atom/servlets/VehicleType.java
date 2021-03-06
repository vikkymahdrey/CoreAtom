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

import com.agiledge.atom.dao.VehicleTypeDao;
import com.agiledge.atom.dto.VehicleTypeDto;


/**
 * 
 * @author muhammad
 */
public class VehicleType extends HttpServlet {

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
		VehicleTypeDto vehicleTypeDtoObj = new VehicleTypeDto();
		VehicleTypeDao vehicleTypeDaoObj = new VehicleTypeDao();
		String vehicleType = request.getParameter("vehicleType");
		int seat = Integer.parseInt(request.getParameter("seat"));
		int sittingCapacity = Integer.parseInt(request
				.getParameter("sittingCapacity"));
		vehicleTypeDtoObj.setType(vehicleType);
		vehicleTypeDtoObj.setSeat(seat);
		vehicleTypeDtoObj.setSittingCopacity(sittingCapacity);
		vehicleTypeDtoObj.setDoneBy(doneBy);
		try {
			int val= vehicleTypeDaoObj.insertVehicleType(vehicleTypeDtoObj);
			if(val>0) {
				session.setAttribute("status",
						"<div class=\"success\" >"+vehicleTypeDaoObj.getMessage()+ "</div>");
			}else {
				session.setAttribute("status",
						"<div class=\"failure\" >"+vehicleTypeDaoObj.getMessage()+ "</div>");
			}
			
			response.sendRedirect("vehicle_type_list.jsp");
		} catch (Exception ex) {
			System.out.println("Error in Dao" + ex);
			response.sendRedirect("vehicle_type.jsp");
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
