package com.agiledge.atom.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.agiledge.atom.dto.EmergencyDto;
import com.agiledge.atom.service.EmergencyTransportService;
import com.agiledge.atom.sms.SMSService;

/**
 * Servlet implementation class EmergencyTransport
 */

public class EmergencyTransport extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EmergencyTransport() {
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
		// TODO Auto-generated method stub
		EmergencyDto dto=new EmergencyDto();
		dto.setSiteId(request.getParameter("siteId"));
		dto.setVehicleType(request.getParameter("chosenVehicleType"));
		dto.setVehicle( request.getParameter("selectVehicle"));
		dto.setBookingFor(request.getParameter("employeeID"));
		dto.setBookingBy( request.getParameter("empID"));
		dto.setReason( request.getParameter("reason"));
		dto.setTravelDate(request.getParameter("travelDate"));
		dto.setStartTime( request.getParameter("startTime"));
		dto.setArea( request.getParameter("area"));
		dto.setPlace( request.getParameter("place"));
		dto.setLandmark(request.getParameter("landMark")) ;
		dto.setLandmarkid( request.getParameter("landMarkID"));
		dto.setEscortId(request.getParameter("escort"));
		dto.setDriverId(request.getParameter("selectDriver"));
		dto.setBookingforName(request.getParameter("employeeName"));
		dto.setVehicleNo(request.getParameter("vehicleno"));
		
		int i=new EmergencyTransportService().insertEmergencyDetails(dto);
		if(i>0)
		{
			request.getSession().setAttribute("status",
					"<div class=\"success\" > Booking Successful</div>");
		}
		else
		{
			request.getSession().setAttribute("status",
					"<div class=\"failure\" > Booking failed</div>");
		}
		response.sendRedirect("EmergencyTransportForCD.jsp");
	}

}
