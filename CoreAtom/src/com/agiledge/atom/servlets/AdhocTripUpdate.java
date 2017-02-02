package com.agiledge.atom.servlets;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.agiledge.atom.dto.TripDetailsDto;
import com.agiledge.atom.service.AdhocRoutingService;

/**
 * Servlet implementation class AdhocTripUpdate
 */

public class AdhocTripUpdate extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AdhocTripUpdate() {
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
		String[] tripids = request.getParameterValues("tripids");
		String Approve = request.getParameter("Approve");
		String Reject = request.getParameter("Reject");
		String siteId = request.getParameter("siteId");
		String date = request.getParameter("date");
		if (Approve == null && Reject == null) {
			int retVal = new AdhocRoutingService().SaveTrip(tripids);
			if (retVal > 0) {
				session.setAttribute("status",
						"<div class=\"success\" > Trips Saved Successfully </div>");
			} else {
				session.setAttribute("status",
						"<div class=\"failure\" > Adhoc Trip Saving Failed !</div>");
			}
			response.sendRedirect("showAdhocTrips.jsp");
		} else {
			int retVal = 0;
			if (Approve != null) {
				ArrayList<TripDetailsDto> tripList = new ArrayList<TripDetailsDto>();
				TripDetailsDto dto = null;
				for (String tripid : tripids) {
					dto = new TripDetailsDto();
					dto.setId(tripid);
					dto.setVehicleNo(request.getParameter("vehicleNo" + tripid));
					dto.setVehicle_type(request.getParameter("vehicleType" + tripid));
					dto.setEscort(request
							.getParameter("escortApprove" + tripid));
					dto.setEscortId(request.getParameter("escortId" + tripid));
					dto.setTripBasedDistance(request.getParameter("distance" + tripid));
					tripList.add(dto);
				}
				retVal = new AdhocRoutingService().approveTrip(tripList);
				if (retVal > 0) {
					session.setAttribute("status",
							"<div class=\"success\" > Trip Approved Successfully </div>");
				} else {
					session.setAttribute("status",
							"<div class=\"failure\" > Trip Approve Failed !</div>");
				}
			} else {
				retVal = new AdhocRoutingService().rejectTrip(tripids);
				if (retVal > 0) {
					session.setAttribute("status",
							"<div class=\"success\" > Trip Rejected Successfully </div>");
				} else {
					session.setAttribute("status",
							"<div class=\"failure\" > Trip Rejection Failed !</div>");
				}
			}

			response.sendRedirect("trackAdhocTrip.jsp?siteId=" + siteId + "&date=" + date);
		}
	}

}
