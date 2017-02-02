package com.agiledge.atom.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.print.attribute.ResolutionSyntax;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.agiledge.atom.dao.ShuttleDao;
import com.agiledge.atom.dto.RouteDto;
import com.agiledge.atom.service.AdhocService;
import com.agiledge.atom.service.ShuttleServcie;

/**
 * Servlet implementation class GetShuttleVehicles
 */
public class GetShuttleVehicles extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public GetShuttleVehicles() {
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
		String routeId = request.getParameter("routeId");
		String shiftTime = request.getParameter("shiftTime");
		
		ArrayList<RouteDto> shuttleVehicles = new ShuttleDao()
				.getShuttleVehicles(routeId, shiftTime);
		PrintWriter out = response.getWriter();
		String retString = "";
		int totalSeats=0;
		for (RouteDto dto : shuttleVehicles) {
			retString += dto.getVehicleTypeId() + "~";
			retString += dto.getVehicleCount() + "|";
			totalSeats+=(Integer.parseInt(dto.getSeats())*Integer.parseInt(dto.getVehicleCount()));
		}
		retString+=totalSeats+"~"+new ShuttleServcie().getTotalbookings(routeId, shiftTime);
		out.write(retString);

	}

}
