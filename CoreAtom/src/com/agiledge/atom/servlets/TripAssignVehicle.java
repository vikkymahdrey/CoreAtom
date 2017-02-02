package com.agiledge.atom.servlets;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.agiledge.atom.dto.EscortDto;
import com.agiledge.atom.dto.VehicleDto;
import com.agiledge.atom.escort.service.EscortService;
import com.agiledge.atom.mobile.service.TripService;
import com.agiledge.atom.service.VendorService;

/**
 * Servlet implementation class TripAssignVehicle
 */
public class TripAssignVehicle extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public TripAssignVehicle() {
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
		boolean isAnyEscort=false;
		try {
			String[] tripIds = request.getParameterValues("tripId");
			String vendorId = request.getParameter("vendorId");
			ArrayList<VehicleDto> vehicleList = new ArrayList<VehicleDto>();
			VehicleDto dto = null;
			
			
			ArrayList<EscortDto> escortList = new ArrayList<EscortDto>();
			EscortDto edto = null;
			boolean validEscortFlag=true;
			boolean validDriverFlag=true;
			
			for (String tripId : tripIds) {
				try{
				dto = new VehicleDto();
				
				dto.setVendor(vendorId);
				dto.setTripId(tripId);
				dto.setVehicleNo(request.getParameter("vehicle" + tripId));
				String[] triptimevalues=request.getParameterValues("traveltime" + tripId);
				dto.setTraveltime(triptimevalues);
				dto.setDriver(request.getParameter("driver" + tripId));
				String isSecurity= request.getParameter("isSecurity" + tripId);
				isSecurity = isSecurity==null?"":isSecurity;
				if(isSecurity.equals("YES")) {
					isAnyEscort=true;
					edto = new EscortDto();
					edto.setTripId(tripId);
					edto.setId(request.getParameter("escort" + tripId));
					System.out.println("ESCORT ID"+request.getParameter("escort" + tripId));
					escortList.add(edto);
					if(edto.getId().trim().equals("")) {
			//			validEscortFlag=false;
			//			break;
					}
					
				}
				vehicleList.add(dto);
				}catch(Exception e)
				{
			System.out.println("some trip not assigned error "+e);	
				} 
			}
			if(validEscortFlag&&validDriverFlag) {
			
		int val=	new VendorService().assaginTripVehicle(vehicleList);
		
		 if(val>=0) {
			 if(new TripService().setTripsReadyForTracking(vehicleList)>0) {
					request.getSession().setAttribute("status",
							"<div class=\"success\" width=\"100%\" > Allocation successful</div>");
					
				 } else {
					 request.getSession().setAttribute("status",
								"<div class=\"failure\" width=\"100%\" > Escort allocation failed</div>");
				 }
			 if(isAnyEscort)
			 {
		val=	new EscortService().assignTripEscort(escortList);
		if(val>=0) {
			request.getSession().setAttribute("status",
					"<div class=\"success\" width=\"100%\" > Allocation successful</div>");
		} else {
			request.getSession().setAttribute("status",
					"<div class=\"failure\" width=\"100%\" > Escort allocation failed</div>");
		}
			 }
			  
		} else {
			request.getSession().setAttribute("status",
					"<div class=\"failure\" width=\"100%\" > Driver & Escort allocations failed</div>");
		}
		} else if(!validEscortFlag){
			request.getSession().setAttribute("status",
					"<div class=\"failure\" width=\"100%\" > Escort validation failed</div>");
		} else if(!validDriverFlag ) {
			request.getSession().setAttribute("status",
					"<div class=\"failure\" width=\"100%\" > Driver validation failed</div>");
		}
		} catch (Exception e) {
			System.out.println("Eror" + e);
			request.getSession().setAttribute("status",
					"<div class=\"failure\" width=\"100%\" >Error :" + e + " </div>");
		}
		response.sendRedirect("vendors_home.jsp");
	}
}
