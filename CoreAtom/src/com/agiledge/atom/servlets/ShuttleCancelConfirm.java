package com.agiledge.atom.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.agiledge.atom.commons.OtherFunctions;
import com.agiledge.atom.service.ShuttleServcie;

/**
 * Servlet implementation class ShuttleCancel
 */

public class ShuttleCancelConfirm extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ShuttleCancelConfirm() {
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
		String bookingId = request.getParameter("bookingId");
		String requestDate = OtherFunctions.changeDateFromatToIso(request.getParameter("fromDate"));
		if (requestDate == null) {
			int status = new ShuttleServcie().reConfirmBooking(bookingId);
		} else {
			int status = new ShuttleServcie().cancelBookingRequest(bookingId,
					requestDate);
		}
		response.sendRedirect("shuttleBookingDetails.jsp");
	}

}
