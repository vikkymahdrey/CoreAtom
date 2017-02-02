package com.agiledge.atom.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.agiledge.atom.dao.VehicleDao;
import com.agiledge.atom.dto.VehicleDto;


/**
 * Servlet implementation class AddVehicle
 */
public class AddVehicle extends HttpServlet {


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
		HttpSession session = request.getSession(true);
		String doneBy = session.getAttribute("user").toString();
		VehicleDto vehicleDtoObj = new VehicleDto();
		VehicleDao vehicleDaoObj = new VehicleDao();
		String vehicleType = request.getParameter("vehicleType");
		String vehicleNo = request.getParameter("vehicleNo");
		String vendor=request.getParameter("vendor");
		vehicleDtoObj.setVehicleNo(vehicleNo);
		vehicleDtoObj.setVehicleType(vehicleType);
		vehicleDtoObj.setVendor(vendor);
		vehicleDtoObj.setDoneBy(doneBy);
		try {			
			vehicleDaoObj.addVehicle(vehicleDtoObj);
			response.sendRedirect("view_vehicle.jsp");
		} catch (Exception ex) {
			System.out.println("Error in Dao" + ex);
			response.sendRedirect("view_vehicle.jsp");
		}

	}

}
