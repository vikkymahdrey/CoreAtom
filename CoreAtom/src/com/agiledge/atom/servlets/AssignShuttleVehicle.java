package com.agiledge.atom.servlets;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.agiledge.atom.dto.RouteDto;
import com.agiledge.atom.service.RouteService;

/**
 * Servlet implementation class AssignShuttleVehicle
 */
public class AssignShuttleVehicle extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AssignShuttleVehicle() {
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
		HttpSession session = request.getSession();
		String[] vehicleTypes = request.getParameterValues("vehicletype");
		int routeId = Integer.parseInt(request.getParameter("routeId"));
		String shitfTime = request.getParameter("shiftTime");
		String totalBookings = request.getParameter("totBooking");
		int totalSeats = 0;
		ArrayList<RouteDto> list = new ArrayList<RouteDto>();
		RouteDto dto = null;
		int retVal = 0;
		try {
			for (int i = 0; i < vehicleTypes.length; i++) {
				dto = new RouteDto();
				dto.setRouteId(routeId);
				dto.setInOut(shitfTime);
				dto.setVehicleType(vehicleTypes[i]);
				dto.setVehicleCount(request.getParameter("count"
						+ vehicleTypes[i]));
				dto.setSeat(Integer.parseInt(request.getParameter("seat" + vehicleTypes[i])));
				if (request.getParameter("count" + vehicleTypes[i]) != null
						&& !request.getParameter("count" + vehicleTypes[i])
								.equals("")
						&& !request.getParameter("count" + vehicleTypes[i])
								.equals("0")) {
					int seats = dto.getSeat()* Integer.parseInt(dto.getVehicleCount());
					dto.setSeats(Integer.toString(seats));
					totalSeats += seats;
					list.add(dto);
				}
			}
			totalSeats = totalSeats - Integer.parseInt(totalBookings);
			retVal = new RouteService().assignShuttleVehicle(list, totalSeats);

		} catch (Exception e) {
			System.out.println("Error" + e);
		}
		if (retVal > 0) {
			session.setAttribute("status",
					"<div class=\"success\" > Shuttle Vehicle Assign Successful</div>");
		} else {
			session.setAttribute("status",
					"<div class=\"failure\" > Shuttle Vehicle Assign failed !</div>");

		}
		response.sendRedirect("routeListShuttle.jsp");

	}
}