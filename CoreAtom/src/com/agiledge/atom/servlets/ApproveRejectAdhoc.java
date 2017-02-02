package com.agiledge.atom.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.agiledge.atom.constants.SettingsConstant;
import com.agiledge.atom.service.AdhocService;

/**
 * Servlet implementation class ApproveRejectAdhoc
 */

public class ApproveRejectAdhoc extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ApproveRejectAdhoc() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
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

	private void processRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String adhocId = request.getParameter("bookingId");
		String appReject = request.getParameter("appReject");
		String cancel = request.getParameter("cancel");
		String src = request.getParameter("src");
		String bookingId = "";
		String isShiftExt = "";
		String scheduleId = "";
		HttpSession session = request.getSession();
		String update = "";
		String approverId = session.getAttribute("user").toString();
		if (appReject != null){
			if(appReject.equals("approve")) 
			update = "Approved";
		if (appReject.equals("reject")) 
					
			update = "Rejected";
		int status = new AdhocService().approveAdhocBooking(adhocId,approverId,update);
		if (status > 0) {
			session.setAttribute("status",
					"<div class=\"success\" > Adhoc Booking " + update
							+ "</div>");
		} else {
			session.setAttribute("status",
					"<div class=\"failure\" > Adhoc Approval/Rejection failed !</div>");
		}
		
		if(src!=null)
		{
			response.sendRedirect("viewAllBookingForTransport.jsp");	
		}
		else
		{
		response.sendRedirect("viewAllAdhocBooking.jsp");
		}
			
		} else {
			update = "cancel";
			bookingId = request.getParameter("bookingId");
			scheduleId = request.getParameter("scheduleId");
			if (request.getParameter("adhoctype") != null
					&& request.getParameter("adhoctype").equalsIgnoreCase(
							SettingsConstant.SHIFT_EXTENSTION))
				isShiftExt = "yes";
			
			if (update.equals("cancel")) {
				int status = new AdhocService().cancelAdhocBooking(bookingId,
						isShiftExt, scheduleId, session.getAttribute("user").toString());
				if (status > 0) {
					session.setAttribute("status",
							"<div class=\"success\" > Adhoc Booking Cancelled</div>");
				} else {
					session.setAttribute("status",
							"<div class=\"failure\" > Adhoc Booking Cancellation  failed !</div>");
				}
				
			
				response.sendRedirect("viewAdhocBooking.jsp");
				
			}

		}
	}
}
