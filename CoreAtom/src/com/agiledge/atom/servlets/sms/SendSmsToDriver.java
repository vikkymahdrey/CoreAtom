package com.agiledge.atom.servlets.sms;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.agiledge.atom.dto.EscortDto;
import com.agiledge.atom.dto.VehicleDto;
import com.agiledge.atom.escort.service.EscortService;
import com.agiledge.atom.mobile.service.TripService;
import com.agiledge.atom.service.VendorService;
import com.agiledge.atom.sms.SMSService;

/**
 * Servlet implementation class SendSmsToDriver
 */
 
public class SendSmsToDriver extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SendSmsToDriver() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
			System.out.println("......................");
			System.out.println("data " + request.getParameter("trip"));
			HttpSession session=request.getSession();
			String vendorId=(String) session.getAttribute("vendorCompany");
			String tripid=request.getParameter("trip");
			String vehicleId=request.getParameter("vehicleid");
			String driverid=request.getParameter("driverid");
			String escortid=request.getParameter("escortid");
			
			
			ArrayList<VehicleDto> vehicleList = new ArrayList<VehicleDto>();
			ArrayList<EscortDto> escortList = new ArrayList<EscortDto>();
			VehicleDto dto = null;
			
			
			EscortDto edto = null;
			try{
				dto = new VehicleDto();
			dto.setVendor(vendorId);
			dto.setTripId(tripid);
			dto.setVehicleNo(vehicleId);	
							
			dto.setDriver(driverid);
			vehicleList.add(dto);
			boolean isEscort=false;
			if(escortid!=null&&!escortid.equals("")) {
				edto = new EscortDto();
				edto.setTripId(tripid);
				edto.setId(escortid);
				escortList.add(edto);
				 isEscort=true;
				
			}
		
	int val=	new VendorService().assaginTripVehicle(vehicleList);
	
	
	 if(val>=0) {
		 if(new TripService().setTripsReadyForTracking(vehicleList)>0) {
			 if(isEscort)
			 {
			 new EscortService().assignTripEscort(escortList);
			 }
			response.getWriter().write("SMS Send");
		 }
	 }
			
		}catch(Exception e) {
			response.getWriter().write("Error : " + e);
		}
	}

}
