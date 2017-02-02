package com.agiledge.atom.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.agiledge.atom.commons.OtherFunctions;
import com.agiledge.atom.dto.TripDetailsChildDto;
import com.agiledge.atom.dto.TripDetailsDto;
import com.agiledge.atom.service.TripDetailsService;



/**
 * Servlet implementation class ApproveTrips
 */
 
public class ApproveTrips extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ApproveTrips() {
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
		
		String siteId=null;
		String  tripMode=null;
		String tripTime=null;
		String tripDate=null;
		 String approvalStatusParam=null;
		
		try{
			HttpSession session = request.getSession(true);
			String doneBy = session.getAttribute("user").toString();
			 siteId=request.getParameter("siteId");
			  tripMode=request.getParameter("tripMode");
			  tripTime=request.getParameter("tripTime");
			  tripDate=request.getParameter("tripDate");
			  approvalStatusParam=request.getParameter("approvalStatusParam");
			  String tripIds[]=request.getParameterValues("tripId");
			  String approvedIds[]=request.getParameterValues("selectedTrip");
			  String status=request.getParameter("approvalStatus");
			 
			  System.out.println("approval Status ");
			  for(String apid: approvedIds)
			  {
				  System.out.println(".. " + apid);
			  }
			  String comments[]= new String[approvedIds.length];
			 // TC can track 
		 		String ss[] = request.getParameterValues("approve");
			 	String disapproved[] = request.getParameterValues("disapprove");
			 	ArrayList<TripDetailsChildDto> checkedDtoList = new ArrayList<TripDetailsChildDto>();
				ArrayList<TripDetailsChildDto> unCheckedDtoList = new ArrayList<TripDetailsChildDto>();
				HashMap<String, TripDetailsDto> trips = new LinkedHashMap<String, TripDetailsDto>();
				HashMap<String, String> noTrips = new HashMap<String, String>();
			   if (ss != null) {
					System.out.println("approved ...");
					for (String s : ss) {

						if (OtherFunctions.condains(
								request.getParameter("tripId-" + s), approvedIds)) {
							System.out.println("Show : " + s);
							TripDetailsChildDto dto = new TripDetailsChildDto();
							dto.setDoneBy(doneBy);
							dto.setTripId(request.getParameter("tripId-" + s));

							String actualTime = request.getParameter("hour-"
									+ dto.getTripId())
									+ ":"
									+ request.getParameter("minute-"
											+ dto.getTripId());

							String oldTime = request.getParameter("actualLogTime-"
									+ dto.getTripId());
							String onTimeStatus;
							if (compareTime(oldTime.trim(), actualTime.trim())) {
								onTimeStatus = "Not On Time";
							} else {

								onTimeStatus = "On Time";
							}

							System.out.println("actual Time" + actualTime);
							// request.getParameter("actualLogTime-" +
							// dto.getTripId());
							String vehicleNo = request.getParameter("vehiclNo-"
									+ dto.getTripId());
							
							TripDetailsDto tripDto = new TripDetailsDto();
							tripDto.setOnTimeStatus(onTimeStatus);
							tripDto.setActualLogTime(actualTime);
							tripDto.setVehicleNo(vehicleNo);
							tripDto.setEscort( request.getParameter("escort-"
									+ dto.getTripId()));
							tripDto.setEscortNo( request.getParameter("escortClock-"
									+ dto.getTripId()));
							tripDto.setDoneBy(doneBy);
							trips.put(dto.getTripId(), tripDto);
							

							
							dto.setReason("");

							dto.setEmployeeId(request.getParameter("employeeId-"
									+ s));
							System.out.println(" empid : " + dto.getEmployeeId());
							checkedDtoList.add(dto);
						} else {

							noTrips.put(request.getParameter("tripId-" + s),
									request.getParameter("tripId-" + s));
						}
					}
				}
				System.out.println("Disapproved ");
				if (disapproved != null) {
					for (String d : disapproved) {

						if (OtherFunctions.condains(
								request.getParameter("tripId-" + d), approvedIds)) {

							System.out.println("No Show : " + d);
							TripDetailsChildDto dto = new TripDetailsChildDto();
							dto.setTripId(request.getParameter("tripId-" + d));
							dto.setDoneBy(doneBy);
							String actualTime = request.getParameter("hour-"
									+ dto.getTripId())
									+ ":"
									+ request.getParameter("minute-"
											+ dto.getTripId());

							System.out.println("actual Time" + actualTime);

							String oldTime = request.getParameter("actualLogTime-"
									+ dto.getTripId());
							String onTimeStatus;
							
							// if (oldTime.trim().equals(actualTime.trim())) {

							if (compareTime(oldTime.trim(), actualTime.trim())) {
								onTimeStatus = "Not On Time";
							} else {

								onTimeStatus = "On Time";
							}
	System.out.println("Escort :"+request.getParameter("escort-"
									+ dto.getTripId())+","+request.getParameter("escortClock-"
									+ dto.getTripId()));
							String vehicleNo = request.getParameter("vehiclNo-"
									+ dto.getTripId());
							TripDetailsDto tripDto = new TripDetailsDto();
							tripDto.setOnTimeStatus(onTimeStatus);
							tripDto.setActualLogTime(actualTime);
							tripDto.setVehicleNo(vehicleNo);
							tripDto.setEscort( request.getParameter("escort-"
									+ dto.getTripId()));
							tripDto.setEscortNo( request.getParameter("escortClock-"
									+ dto.getTripId()));
							tripDto.setDoneBy(doneBy);
							trips.put(dto.getTripId(), tripDto);
						
							dto.setEmployeeId(request.getParameter("employeeId-"
									+ d));
							dto.setReason(request.getParameter("reason-"
									+ d));
							dto.setShowStatus("No Show");
							System.out.println(" empid : " + dto.getEmployeeId());
							unCheckedDtoList.add(dto);
						} else {
							noTrips.put(request.getParameter("tripId-" + d),
									request.getParameter("tripId-" + d));
						}
					}
				}
			 	TripDetailsService service = new TripDetailsService();
				boolean flag = false;
				String user = request.getSession().getAttribute("user").toString();
				System.out.println(" track vendor ..");
				System.out.println("Total Shows " + checkedDtoList.size());
				System.out.println("Total No Shows " + unCheckedDtoList.size());

				flag = service.trackByVendor(trips, checkedDtoList,
						unCheckedDtoList, noTrips, "", user);
				
		System.out.println("Trips in servlet");
		if(tripIds!=null&&tripIds.length>0)
		{
			for(String s: tripIds)
			{
				System.out.println(" " +s);
			}
		}
		
		if(approvedIds!=null&&approvedIds.length>0)
		{
			int i=0;
			System.out.println("approved Trips");
    
			for(String s: approvedIds)
			{
				comments[i]=request.getParameter("comment-"+s);
				System.out.println("trip: " +s+" Comment :" + comments[i]);
			}
		}

 		 int value=0;
 				 value=new TripDetailsService().transAdmin_ApproveTrackedTrips(approvedIds, status,comments,doneBy);
		 //	int value=0;
		 	System.out.println(" just before approval status");
			System.out.println(" Status__ : " + status);
			if(value>0)
			{
				request.getSession().setAttribute("status",
						"<div class=\"success\" width=\"100%\" >Operation Successful</div>");
			}else
			{
				request.getSession().setAttribute("status",
						"<div class=\"failure\" width=\"100%\" >Operation Failed</div>");
			}
			
			
			
			
		}catch(Exception e)
		{
			System.out.println("Error in ApproveTrips " + e);
			request.getSession().setAttribute("status",
					"<div class=\"failure\" width=\"100%\" >Operation Failed</div>");
			
		}
		
	//	String url="vendor_tripApproval.jsp?siteId=" + siteId + "&tripMode=" + tripMode + "&tripTime=" + tripTime + "&tripDate=" + tripDate;
		String url="transadmin_trackedTrips.jsp?siteId=" + siteId + "&tripMode=" + tripMode + "&tripTime=" + tripTime + "&approvalStatusParam="+approvalStatusParam+"&tripDate=" + tripDate;
		response.sendRedirect( url);
	}


	public boolean compareTime(String plannedTime, String actualTime) {

		String old[] = plannedTime.split(":");
		String actual[] = actualTime.split(":");

		String newoldTime = old[0] + old[1];
		String newactual = actual[0] + actual[1];
		System.out.println("status : "
				+ (Integer.parseInt(newactual) > Integer.parseInt(newoldTime)));
		return (Integer.parseInt(newactual) > Integer.parseInt(newoldTime));
	}

}
