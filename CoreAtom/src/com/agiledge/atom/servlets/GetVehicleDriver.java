package com.agiledge.atom.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.agiledge.atom.dto.DriverDto;
import com.agiledge.atom.dto.DriverVehicleDto;
import com.agiledge.atom.service.VehicleService;

/**
 * Servlet implementation class GetVehicleDriver
 */
public class GetVehicleDriver extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public GetVehicleDriver() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		System.out.println("reached here");
		String vehicleId = request.getParameter("vehicle");
		ArrayList<DriverVehicleDto> dtos = new VehicleService()
				.getVehicleDetails(vehicleId);
		PrintWriter out = response.getWriter();
		String returnString = "";
		for (DriverVehicleDto dto : dtos) {
			DriverDto ddto = dto.getDriverDto();
			returnString += "<option value='" + ddto.getDriverId() + "'>"
					+ ddto.getDriverName() + "</option>";
		}
		//returnString=returnString;
		System.out.println(returnString);
		out.write(returnString);
	}

}
