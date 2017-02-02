package com.agiledge.atom.reports;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.agiledge.atom.commons.OtherFunctions;
import com.agiledge.atom.dbConnect.DbConnect;
import com.agiledge.atom.dto.EmergencyDto;

public class EmergencyTransportReportForCD {

	public List<EmergencyDto> getAllEmergencyDetails(String fromDate,
			String toDate,String siteId) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		SimpleDateFormat sdf1 = new SimpleDateFormat("MM/dd/yyyy");

		System.out.println("from Date " + fromDate);
		System.out.println("to Date " + toDate);
		DbConnect dbConn = DbConnect.getInstance();
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		List<EmergencyDto> reportList = new ArrayList<EmergencyDto>();
		try {
			fromDate = OtherFunctions.changeDateFromatToIso(fromDate);
			toDate = OtherFunctions.changeDateFromatToIso(toDate);
			String query = "select t.siteId,e.displayname as bookingFor,e.personnelNo, date_format(t.travelDate,'%d/%m/%y') travelDate,t.startTime,e1.displayname as bookingBy, v.regNo as vehicle, t.area,t.place,t.landmark,t.reason,t.booked_date_status,t.booked_time_status from transportation_in_emergency t, employee e ,employee e1, vehicles v where t.bookingFor =e.id and t.bookingBy=e1.id and t.vehicle=v.id and t.siteId="+siteId+" and t.travelDate between '"+fromDate+"' and '"+toDate+"'";
			System.out.println("EmergencyReportQuery: " + query);
			
			con = dbConn.connectDB();
			stmt = con.createStatement();
			
			rs = stmt.executeQuery(query);
			EmergencyDto emerDto=null;
			while (rs.next()) {
				emerDto=new EmergencyDto();
				emerDto.setSiteId(rs.getString("siteId"));
				emerDto.setBookingFor(rs.getString("bookingFor"));
				emerDto.setPersonnelNo(rs.getString("personnelNo"));
				emerDto.setTravelDate(rs.getString("travelDate"));
				emerDto.setStartTime(rs.getString("startTime"));
				emerDto.setBookingBy(rs.getString("bookingBy"));
				emerDto.setVehicle(rs.getString("vehicle"));
				emerDto.setArea(rs.getString("area"));
				emerDto.setPlace(rs.getString("place"));
				emerDto.setLandmark(rs.getString("landmark"));
				emerDto.setReason(rs.getString("reason"));
				emerDto.setBooking_date_status(rs.getString("booked_date_status"));
				emerDto.setBooking_time_status(rs.getString("booked_time_status"));
				reportList.add(emerDto);
			}
			return reportList;

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Exception in EmergencyTransportReport.java");
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(stmt);
			DbConnect.closeConnection(con);
		}

		return null;

	}
}
