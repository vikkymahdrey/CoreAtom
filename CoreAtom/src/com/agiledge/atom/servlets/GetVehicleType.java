package com.agiledge.atom.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.agiledge.atom.dao.VehicleDao;
import com.agiledge.atom.dao.VehicleTypeDao;
import com.agiledge.atom.dto.VehicleTypeDto;

/**
 * Servlet implementation class GetVehicleType
 */
public class GetVehicleType extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetVehicleType() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String siteId = "" + request.getParameter("siteId");
		PrintWriter out = response.getWriter();
		String options = "";
		ArrayList<VehicleTypeDto> vehicleTypeDtos = null;
		try {
			vehicleTypeDtos=new VehicleTypeDao().getAllVehicleType(siteId);
		}catch(Exception e){e.printStackTrace();}
		for (VehicleTypeDto dto : vehicleTypeDtos) {
			options += "<option value='" + dto.getId() + "'>" + dto.getType()
					+ "</option>";
		}
		System.out.println("options"+options);
		out.print(options);
	}

}
