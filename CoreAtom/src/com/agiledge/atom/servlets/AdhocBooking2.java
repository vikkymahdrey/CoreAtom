
package com.agiledge.atom.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.agiledge.atom.dto.AdhocDto;
import com.agiledge.atom.dto.EmployeeDto;
import com.agiledge.atom.service.AdhocService;

/**
 * Servlet implementation class AdhocBooking
 */
public class AdhocBooking2 extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AdhocBooking2() {
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
			String[] passengerName = request.getParameterValues ("employeeName" );
			String[] bookingForId = request.getParameterValues("employeeID");
			String[] passengerType = request.getParameterValues("passengerType" );
			String[] passengerContactNo = request.getParameterValues("passengerContactNo");
			String[] passengerEmailId = request.getParameterValues("passengerEmailId");
			String[] passengerGender = request.getParameterValues("passengerGender");
			
			System.out.println("Before arrayList" + (passengerGender==null));
			
			List<EmployeeDto> empDtos = new ArrayList<>();
			for(int i=0; i<passengerName.length; i++ ) {
				EmployeeDto empDto = new EmployeeDto();
				empDto.setDisplayName(passengerName[i]);
				empDto.setEmployeeID( bookingForId[i]);
				empDto.setState(passengerType[i]);
				empDto.setContactNo(passengerContactNo[i]);
				empDto.setEmailAddress(passengerEmailId[i]);
				empDto.setGender(passengerGender[i]);
				System.out.println(empDto.getGender());
				empDtos.add(empDto);
			}
			
			System.out.println("after employee added to arrayList");
			
			
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
			dto.setPassengerList(empDtos);
			if (bookingId != null && !bookingId.equals("")) {
				System.out.println("booking Id is not null..");
				dto.setBookingId(bookingId);
				int retVal = 0;
//				int retVal = new AdhocService().modifyAdhocTravel(dto);
				
				if (retVal > 0) {
					session.setAttribute("status",
							"<div class=\"success\" > Adhoc Modification Successful</div>");
				} else {
					session.setAttribute("status",
							"<div class=\"failure\" > Adhoc Modification failed !</div>");
				}
			}
			else
			{
			String status = new AdhocService().bookAdhocTravel2(dto);		
			session.setAttribute("status", status);
			}
		} catch (Exception e) {
			System.out.println("Error in AdhocBooking2 : " + e);
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
