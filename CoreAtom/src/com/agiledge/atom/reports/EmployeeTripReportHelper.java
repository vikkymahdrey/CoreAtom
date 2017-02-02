package com.agiledge.atom.reports;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.agiledge.atom.commons.OtherFunctions;
import com.agiledge.atom.dbConnect.DbConnect;
import com.agiledge.atom.reports.dto.EmployeesTripTimeDto;
 

public class EmployeeTripReportHelper {
	
	public List<EmployeesTripTimeDto> getAverageTripTimeOfEmployees(String site, String fromDate, String toDate, String employeeId, String tripLog) {
		String query="select employeeId, trip_log, avgTime, displayName, personnelNo from ( select employeeId, trip_log, ifnull(time(avg(totalTime)),time('0:0')) avgTime from ( select vtp.tripId, vtp.startTime, vts.employeeId, vts.inTime reachTime, td.trip_date, td.trip_log,  if(timediff(vts.inTime,vtp.startTime) >0,timediff(vts.inTime,vtp.startTime)  , timediff('24:00', -timediff(vts.inTime,vtp.startTime)) ) totalTime  from trip_details td join vendor_trip_sheet_parent vtp on td.id=vtp.tripId join vendor_trip_sheet vts on vtp.tripId=vts.tripId where td.trip_log='IN' union select vtp.tripId, vtp.startTime, vts.employeeId, vts.outTime reachTime, td.trip_date, td.trip_log,  if(timediff(vts.outTime,vtp.startTime) >0,timediff(vts.outTime,vtp.startTime)  , timediff('24:00', -timediff(vts.outTime,vtp.startTime)) ) totalTime  from trip_details td join vendor_trip_sheet_parent vtp on td.id=vtp.tripId join vendor_trip_sheet vts on vtp.tripId=vts.tripId where td.trip_log='OUT' ) empInTrip where trip_date between ? and ? group by employeeId, trip_log   ) e join employee on e.employeeId=id   where site=? ";
		
		System.out.println(query);
		DbConnect dbConn = DbConnect.getInstance();
		Connection con = null;
		java.sql.PreparedStatement stmt = null;
		ResultSet rs = null;
		
		ArrayList<EmployeesTripTimeDto> dtoList = new ArrayList<EmployeesTripTimeDto>();
		try {
		tripLog=tripLog==null?"":tripLog;
		employeeId=employeeId==null?"":employeeId;
		fromDate=fromDate==null?"":fromDate;
		toDate=toDate==null?"":toDate;
		site=site==null?"":site;
		
		if(site.equals("")==false) {
			
			if(tripLog.equals("")==false&&tripLog.equalsIgnoreCase("all")==false) {
				query=query + " and trip_log=?";
			}
			
			
			if(fromDate.equals("")==false && toDate.equals("")==false) {
				fromDate=OtherFunctions.changeDateFromatToIso(fromDate);
				toDate = OtherFunctions.changeDateFromatToIso(toDate);
			}
			con=dbConn.connectDB();
			stmt=con.prepareStatement(query);
			System.out.println("select employeeId, trip_log, avgTime, displayName, personnelNo from ( select employeeId, trip_log, ifnull(time(avg(totalTime)),time('0:0')) avgTime from ( select vtp.tripId, vtp.startTime, vts.employeeId, vts.inTime reachTime, td.trip_date, td.trip_log,  if(timediff(vts.inTime,vtp.startTime) >0,timediff(vts.inTime,vtp.startTime)  , timediff('24:00', -timediff(vts.inTime,vtp.startTime)) ) totalTime  from trip_details td join vendor_trip_sheet_parent vtp on td.id=vtp.tripId join vendor_trip_sheet vts on vtp.tripId=vts.tripId where td.trip_log='IN' union select vtp.tripId, vtp.startTime, vts.employeeId, vts.outTime reachTime, td.trip_date, td.trip_log,  if(timediff(vts.outTime,vtp.startTime) >0,timediff(vts.outTime,vtp.startTime)  , timediff('24:00', -timediff(vts.outTime,vtp.startTime)) ) totalTime  from trip_details td join vendor_trip_sheet_parent vtp on td.id=vtp.tripId join vendor_trip_sheet vts on vtp.tripId=vts.tripId where td.trip_log='OUT' ) empInTrip where trip_date between '"+fromDate+ "' and  '"+toDate+ "' group by employeeId, trip_log   ) e join employee on e.employeeId=id   where site="+site+" ");
			stmt.setString(1, fromDate);
			stmt.setString(2, toDate);
			stmt.setString(3, site);
			if(tripLog.equals("")==false&&tripLog.equalsIgnoreCase("all")==false) {
				stmt.setString(4, tripLog);
			}
		 rs=stmt.executeQuery();
		 while(rs.next()) {
			 EmployeesTripTimeDto dto = new EmployeesTripTimeDto();
			 dto.setAverageTime(rs.getString("avgTime"));
			 dto.setTripLog(rs.getString("trip_log"));
			 dto.setDisplayName(rs.getString("displayName"));
			 dto.setPersonnelNo(rs.getString("personnelNo"));
			 System.out.println(" personnelNo : " +dto.getPersonnelNo());
			  
			 dtoList.add(dto);
			 
		 }
	 
		} 
		
		

		
		
		
	}catch(Exception e) {
		System.out.println("Exception in employeeTripReportHelper :"+e);
	}
		finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(stmt);
			DbConnect.closeConnection(con);
		}
	return dtoList;
	}
	


}
