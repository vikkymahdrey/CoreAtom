package com.agiledge.atom.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.agiledge.atom.dto.AdhocDto;
import com.agiledge.atom.service.AdhocService;

/**
 * Servlet implementation class AdhocBooking
 */
public class AdhocBooking extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AdhocBooking() {
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
		String bookingId = request.getParameter("bookingId");
		String roleId=session.getAttribute("roleId").toString();
		String bookedFor = "";
		String bookedBy = "";
		System.out.println("bookingId=="+bookingId);
		try {
			String adhocType = request.getParameter("adhocType");
			String travelDate = request.getParameter("travelDate");
			String pickdrop = request.getParameter("pickdrop");
			String shiftTime = request.getParameter("shiftTime");
			String startTime = request.getParameter("startTime");
			if(startTime==null)
			{
				startTime=shiftTime;
						
			}
			String endTime = request.getParameter("endTime");
			String orgination = request.getParameter("orgination");
			String destination = request.getParameter("destination");
			String reason = request.getParameter("reason");
			String comment = request.getParameter("comment");
			bookedFor = request.getParameter("employeeID");
			bookedBy = session.getAttribute("user").toString();
			String siteId = request.getParameter("site");
			String projectUnit = request.getParameter("projectUnit");
		

			if (bookedFor == null || bookedFor.equals("")
					|| bookedFor.equals("null")) {
				bookedFor = bookedBy;
			}

			AdhocDto dto = new AdhocDto();
			dto.setAdhocType(adhocType);
			dto.setTravelDate(travelDate);
			dto.setStartTime(startTime);
			dto.setEndTime(endTime);
			dto.setOrgination(orgination);
			dto.setDestination(destination);
			dto.setReason(reason);
			dto.setComment(comment);
			dto.setBookedFor(bookedFor);
			dto.setBookedBy(bookedBy);
			dto.setPickupDrop(pickdrop);
			dto.setShiftTime(shiftTime);
			dto.setSiteId(siteId);
			dto.setProjectUnit(projectUnit);
			dto.setRequesterRoleId(roleId);
			dto.setEmployeeId(bookedFor);
			if (bookingId != null && !bookingId.equals("")) {
				dto.setBookingId(bookingId);
				int retVal = new AdhocService().modifyAdhocTravel(dto);
				if (retVal > 0) {
					session.setAttribute("status",
							"<div class=\"success\" > Adhoc Modification Successfull</div>");
				} else {
					session.setAttribute("status",
							"<div class=\"failure\" > Adhoc Modification failed !</div>");
				}
			}
			else
			{
			String status = new AdhocService().bookAdhocTravel(dto);			
			session.setAttribute("status", status);
			}
		} catch (Exception e) {
			System.out.println("Error in Servlet" + e);
			session.setAttribute("status",
					"<div class=\"failure\" > Adhoc booking/Updation failed !</div>");
		}
		if (bookedFor.equalsIgnoreCase(bookedBy)) {
			response.sendRedirect("viewAdhocBooking.jsp");
		} else {
			response.sendRedirect("viewAllAdhocBooking.jsp");
		}
	}

}
