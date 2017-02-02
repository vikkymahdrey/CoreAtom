package com.agiledge.atom.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.agiledge.atom.dto.RouteDto;
import com.agiledge.atom.dto.TripDetailsChildDto;
import com.agiledge.atom.service.RouteService;


/**
 * Servlet implementation class GetRoute
 */

public class GetRoute extends HttpServlet {

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	private void processRequest(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		if (request.getParameter("tripId") != null) {
			int tripId = Integer.parseInt(request.getParameter("tripId"));

			PrintWriter out = response.getWriter();

			ArrayList<RouteDto> routeDtos = new RouteService()
					.getAllAPLInTrip(tripId);
			String retVal = "";
			for (RouteDto dto : routeDtos) {
				retVal += ":" + dto.getArea();
				retVal += ":" + dto.getPlace();
				retVal += ":" + dto.getLandmarkId();
				retVal += ":" + dto.getLandmark();
				retVal += ":" + dto.getLattitude();
				retVal += ":" + dto.getLongitude() + "$";

			}
		//	System.out.println("Size" + routeDtos.size());
			out.println(retVal);

		}
		else if(request.getParameter("tripIdInTrace") != null)
		{			
				int tripId = Integer.parseInt(request.getParameter("tripIdInTrace"));

				PrintWriter out = response.getWriter();

				//ArrayList<TripDetailsChildDto> routeDtos = new RouteService()
					//	.getEmployeeGetInPosition(tripId);
				ArrayList<TripDetailsChildDto> routeDtos = new RouteService().getEmployeePosition(tripId);
				String retVal = "";
				for (TripDetailsChildDto dto : routeDtos) {
					retVal += dto.getEmployeeId();
					retVal += ":"+dto.getEmployeeName();
					retVal += ":"+dto.getLatitude();
					retVal += ":" + dto.getLongitude(); 
					retVal += ":" + dto.getGender()+"$";
				}
				out.println(retVal);
				
			
		}		
		else if(request.getParameter("tripIdForGetIn") != null)
		{			
				int tripId = Integer.parseInt(request.getParameter("tripIdForGetIn"));

				PrintWriter out = response.getWriter();

				ArrayList<TripDetailsChildDto> routeDtos = new RouteService().getEmployeeGetInPosition(tripId);
				String retVal = "";
				for (TripDetailsChildDto dto : routeDtos) {
					retVal += dto.getEmployeeId();
					retVal += ":"+dto.getEmployeeName();
					retVal += ":"+dto.getLatitude();
					retVal += ":" + dto.getLongitude(); 
					retVal += ":" + dto.getGender()+"$";
				}
				//System.out.println("VAL" + retVal);
				// System.out.println("Size"+routeDtos.size());
				out.println(retVal);
				
			
		}		
		else {
			int routeId = Integer.parseInt(request.getParameter("routeId"));

			PrintWriter out = response.getWriter();

			ArrayList<RouteDto> routeDtos = new RouteService()
					.getAllAPLInRoute(routeId);
			String retVal = "";
			for (RouteDto dto : routeDtos) {
				retVal += ":" + dto.getArea();
				retVal += ":" + dto.getPlace();
				retVal += ":" + dto.getLandmarkId();
				retVal += ":" + dto.getLandmark();
				retVal += ":" + dto.getLattitude();
				retVal += ":" + dto.getLongitude() + "$";

			}
		//	System.out.println("VAL" + retVal);
			// System.out.println("Size"+routeDtos.size());
			out.println(retVal);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
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
