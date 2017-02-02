package com.agiledge.atom.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.agiledge.atom.commons.OtherFunctions;
import com.agiledge.atom.dto.EmployeeDto;
import com.agiledge.atom.dto.TripDetailsChildDto;
import com.agiledge.atom.dto.TripDetailsDto;
import com.agiledge.atom.service.TripDetailsService;
import com.agiledge.atom.sms.SMSService;
import com.agiledge.atom.sms.SendSMS;



/**
 * Servlet implementation class TrackTripSheet
 */

public class TrackTripSheet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public TrackTripSheet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(true);
		String msgstatus=request.getParameter("msgfield");
		String doneBy=session.getAttribute("user").toString();
		
		String approvalStatusParam=request.getParameter("approvalStatusParam");
		String siteId = null;
		String tripMode = null;
		String tripTime[] = null;
		String tripDate = null;
		try {

			String ss[] = request.getParameterValues("approve");
			String disapproved[] = request.getParameterValues("disapprove");
			String isApproval = request.getParameter("isApproval");
			String selectedTrips[] = request.getParameterValues("selectedTrip");
			String status="";
			HashMap<String, String> noTrips = new HashMap<String, String>();

			System.out.println("is approval : " + isApproval);
			ArrayList<TripDetailsChildDto> checkedDtoList = new ArrayList<TripDetailsChildDto>();
			ArrayList<TripDetailsChildDto> unCheckedDtoList = new ArrayList<TripDetailsChildDto>();

			siteId = request.getParameter("siteId");
			tripMode = request.getParameter("tripMode");
			tripTime = request.getParameterValues("tripTime");
			tripDate = request.getParameter("tripDate");

			HashMap<String, TripDetailsDto> trips = new LinkedHashMap<String, TripDetailsDto>();
			if (ss != null) {
				System.out.println("approved ...");
				for (String s : ss) {

					if (OtherFunctions.condains(
							request.getParameter("tripId-" + s), selectedTrips)) {
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
						String forceStop=""+request.getParameter("forceStop-"
								+ dto.getTripId());
						tripDto.setForceStop(forceStop.equalsIgnoreCase("on")?true:false);
						tripDto.setTrackingStatus(forceStop);
						tripDto.setOnTimeStatus(onTimeStatus);
						tripDto.setActualLogTime(actualTime);
						tripDto.setVehicleNo(vehicleNo);
						tripDto.setDriverContact(request.getParameter("driverContact-"+ dto.getTripId()));
						tripDto.setEscort( request.getParameter("escort-"
								+ dto.getTripId()));
						tripDto.setEscortNo( request.getParameter("escortClock-"
								+ dto.getTripId()));
						tripDto.setDoneBy(doneBy);
						
						String manualDistance = request.getParameter("tripBasedDistance-"
								+ dto.getTripId());
						System.out.println("Manual : "+ manualDistance);
						tripDto.setTripBasedDistance( manualDistance);
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
							request.getParameter("tripId-" + d), selectedTrips)) {

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
						String forceStop=""+request.getParameter("forceStop-"
								+ dto.getTripId());
						tripDto.setForceStop(forceStop.equalsIgnoreCase("on")?true:false);
						tripDto.setOnTimeStatus(onTimeStatus);
						tripDto.setActualLogTime(actualTime);
						tripDto.setVehicleNo(vehicleNo);
						tripDto.setDriverContact(request.getParameter("driverContact-"+ dto.getTripId()));
						tripDto.setEscort( request.getParameter("escort-"
								+ dto.getTripId()));
						tripDto.setEscortNo( request.getParameter("escortClock-"
								+ dto.getTripId()));
						tripDto.setDoneBy(doneBy);
						String tripBasedDistance = request.getParameter("tripBasedDistance-"
								+ dto.getTripId());
						tripDto.setTripBasedDistance(tripBasedDistance);
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
			String errorMsg="";
			System.out.println("Selected trips");
			for (String trip : selectedTrips) {
				System.out.println(" Trip " + trip);
			}

			TripDetailsService service = new TripDetailsService();
			boolean flag = false;
			String user = request.getSession().getAttribute("user").toString();
			System.out.println(" track vendor ..");
			System.out.println("Total Shows " + checkedDtoList.size());
			System.out.println("Total No Shows " + unCheckedDtoList.size());

			flag = service.trackByVendor(trips, checkedDtoList,
					unCheckedDtoList, noTrips, isApproval, user);
			if (flag) {
				errorMsg="Tracking Successful";
				String message="";
				if(msgstatus.equals("true"))
				{
					int msgtrid;
					ArrayList<EmployeeDto> emlist= new ArrayList<EmployeeDto>();
					Set<String> keys=  trips.keySet();
					for(String key: keys)
						{
						TripDetailsDto tdto= trips.get(key);
						msgtrid=Integer.parseInt(key);
						emlist=service.getemployeeContactNo(msgtrid);
						for(EmployeeDto emdto:emlist)
						{													
							
							new SMSService().sendSMSMessage(key,emdto.getPickUpTime(),tdto.getVehicleNo(),emdto.getContactNo(),tdto.getDriverContact(),siteId);							
						}
						System.out.println("msgrid"+msgtrid);
						}
				}
				int value = 0;
				if (isApproval != null && isApproval.equals("true")) {
					System.out.println("....................true .........");

					value = service.setApproved(trips, user);
					if(value>0)
					{
						if(request.getSession().getAttribute("role").equals("v"))
						{
						errorMsg="Sent For Transport Co-ordinator's Approval";	
						}
						 
						
					}else
					{
						status="Sending For Transport Co-Ordinator's Approval Has Been Failed";
					}
					// service.disapproveTripsHavingNoEmployee(trips, user);

				} else {
					value = 1;
				}
				if (value > 0) {
					request.getSession()
							.setAttribute("status",
									"<div class=\"success\" width=\"100%\" >"+errorMsg+"</div>");
				} else {
					if(status.equals("Sending For Transport Co-Ordinator's Approval Has Been Failed"))
					{
						request.getSession()
						.setAttribute("status",
								"<div class=\"failure\" width=\"100%\" > "+status+" </div>");
					}else
					{
						request.getSession()
						.setAttribute("status",
								"<div class=\"failure\" width=\"100%\" > Updating Failed</div>");
					}
		
					
				}
			} else {
				request.getSession()
						.setAttribute("status",
								"<div class=\"failure\" width=\"100%\" > Updating Failed</div>");
			}

		} catch(NullPointerException e)
		{
			System.out.println("Ex : " + e);
			request.getSession()
					.setAttribute("status",
							"<div class=\"failure\" width=\"100%\" >Error: No Trips Are Selected</div>");
		}catch (Exception e) {
			System.out.println("Ex : " + e);
			request.getSession()
					.setAttribute("status",
							"<div class=\"failure\" width=\"100%\" > Validation"+e+" Error</div>");
		}
		request.setAttribute("tripTimes", tripTime);				
		
		String url = "vendor_tripApproval.jsp?siteId=" + siteId + "&tripMode="
				+ tripMode + "&tripDate=" + tripDate+"&approvalStatusParam="+approvalStatusParam;
		request.getRequestDispatcher("/"+url).include(request, response);	


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
