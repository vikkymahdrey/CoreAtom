package com.agiledge.atom.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.agiledge.atom.constants.SettingsConstant;
import com.agiledge.atom.dao.GeneralShiftDAO;
import com.agiledge.atom.dao.RouteDao;
import com.agiledge.atom.dao.ShuttleDao;
import com.agiledge.atom.dto.EmployeeSubscriptionDto;
import com.agiledge.atom.dto.GeneralShiftDTO;
import com.agiledge.atom.dto.RouteDto;

public class ShuttleServcie {
	public String subscribeForShuttle(EmployeeSubscriptionDto dto) {
		String retString = validateShuttleBooking(dto);
		if (retString.equalsIgnoreCase("success")) {
			if (dto.getInRoute() != null && !dto.getInRoute().equals(""))
				retString = "Booking For IN :"
						+ new ShuttleDao().subscribe(dto, "IN");
			if (dto.getOutRoute() != null && !dto.getOutRoute().equals(""))
				retString += "  Booking For OUT :"
						+ new ShuttleDao().subscribe(dto, "OUT");
		}
		return retString;
	}

	public String validateShuttleBooking(EmployeeSubscriptionDto dto) {
		GeneralShiftDTO configDtoIn = new GeneralShiftDAO().getConfigurations(
				"IN", dto.getShiftIn());
		GeneralShiftDTO configDtoOut = new GeneralShiftDAO().getConfigurations(
				"OUT", dto.getShiftOut());
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Calendar cal = Calendar.getInstance();
		
		try {

			cal.setTime(new Date());
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			cal.add(Calendar.DATE,
					Integer.parseInt(configDtoIn.getCutoffdays()));
			if (sdf.parse(dto.getSubscriptionFromDate()).compareTo(
					cal.getTime()) < 0) {
				return "In Valid Date";
			}

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (!configDtoIn.getApproval_req().equalsIgnoreCase("y")) {
			dto.setApprovalStatus(SettingsConstant.APPROVED);
		} else {
			dto.setApprovalStatus(SettingsConstant.NOT_APPROVED);
		}

		return "success";

	}

	public boolean isSubscriptionRequestMade(String employeeID) {
		return new ShuttleDao().isSubscriptionRequestMade(employeeID);
	}

	public ArrayList<RouteDto> getShuttleRouteDetails(int routeId) {
		return new RouteDao().getShuttleRouteDetails(routeId);

	}
	public ArrayList<EmployeeSubscriptionDto> getShuttleBookingDatiles(String empId) {
		return new ShuttleDao().getShuttleBookingDatiles(empId);
	}

	public int cancelBookingRequest(String bookingId,String date) {
		return new ShuttleDao().cancelRequest(bookingId,date);
	}
	public int reConfirmBooking(String bookingId) {
		return new ShuttleDao().reConfirmBooking(bookingId);
	}

	public int updateBookingWaitingList(ArrayList<RouteDto> list,int totalSeats) {
		return new ShuttleDao().updateBookingWaitingList(list.get(0),totalSeats);
		
	}
	public int getTotalbookings(String routeId,String inOutTime) {
		return new ShuttleDao().getTotalbookings(routeId, inOutTime); 
	}
	public ArrayList<GeneralShiftDTO> getBookingDetailsForManager(String managerId) {
		return new ShuttleDao().getBookingDetailsForManager(managerId);	
	}
	public int approveBooking(String bookingId) {
		return new ShuttleDao().approveBooking(bookingId);	
	}

	public String createShuttle(String deviceNo, String logType, String logTime) {
		return new ShuttleDao().createShuttle(deviceNo,logType,logTime);
	}

	public int stopShuttle(String deviceNo) {
		return new ShuttleDao().stopShuttle(deviceNo);
	}
}
