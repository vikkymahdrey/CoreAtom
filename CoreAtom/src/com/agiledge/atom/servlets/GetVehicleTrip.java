package com.agiledge.atom.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.agiledge.atom.dao.TripDetailsDao;
import com.agiledge.atom.dao.VehicleDao;
import com.agiledge.atom.dto.TripDetailsDto;
import com.agiledge.atom.dto.VehicleDto;


public class GetVehicleTrip extends HttpServlet {
	private void processRequest(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String tripDate = request.getParameter("tripDate");
		String vehicleId = request.getParameter("vehicleId");		
		String retVal = "";
		PrintWriter out = null;
		try {
			out = response.getWriter();
			if (vehicleId == null) {
				ArrayList<VehicleDto> dtos = new VehicleDao()
						.getTripVehicle(tripDate);
				for (VehicleDto dto : dtos) {
					retVal += "<option value='" + dto.getId() + "'>"
							+ dto.getVehicleNo() + "</option>";

				}

			} else {
				ArrayList<TripDetailsDto> dtos = new TripDetailsDao()
						.getTripsByDateVehicle(tripDate,vehicleId);
				for (TripDetailsDto dto : dtos) {
					retVal += "<option value='" + dto.getId() +":"+dto.getEscort()+"'>"
							+ dto.getTrip_code() + "</option>";
				}
			}
		} catch (Exception e) {
			System.out.println("erpro" + e);
		}
		out.write(retVal);
	}
	
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		processRequest(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}
	
}
