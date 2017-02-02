package com.agiledge.atom.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.agiledge.atom.dao.VehicleTypeDao;
import com.agiledge.atom.dto.VehicleTypeDto;

public class GetVehicleNotInSiteAndType extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public GetVehicleNotInSiteAndType() {
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
		String siteId = "" + request.getParameter("siteId");
		String type = "" + request.getParameter("typeId");
		PrintWriter out = response.getWriter();
		String options = "";
		ArrayList<VehicleTypeDto> vehicleTypeDtos = null;
		ArrayList<VehicleTypeDto> vehicleTypeInSiteDtos = null;
		ArrayList<VehicleTypeDto> vehicleTypeInSiteDtosObj = null;
		try {
			vehicleTypeDtos = new VehicleTypeDao()
					.getAllOtherVehicleType(siteId);
			vehicleTypeInSiteDtos = new VehicleTypeDao()
					.getSiteVehicleType(siteId);
			vehicleTypeInSiteDtosObj = new VehicleTypeDao().getBySiteAndTypeVehicle(siteId, type);
			
		} catch (Exception e) {

			e.printStackTrace();
		}
		for (VehicleTypeDto dto : vehicleTypeDtos) {
			options += "<option value='" + dto.getId() + "'>" + dto.getType()
					+ "</option>";
		}
		options += "";
		options += "$ ";
		for (VehicleTypeDto dto : vehicleTypeInSiteDtos) {
			options += "<option value='" + dto.getId() + "'>" + dto.getType()
					+ "</option>";
		}
		options += "";
		options += "#";
		for (VehicleTypeDto dto : vehicleTypeInSiteDtosObj) {
			options += "<option value='" + dto.getId() + "'>" + dto.getRegNo()
					+ "</option>";
		}
		options += "";
		 System.out.println("DATA"+options);
		out.print(options);
	}

}
