package com.agiledge.atom.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import com.agiledge.atom.commons.OtherFunctions;
import com.agiledge.atom.dbConnect.DbConnect;
import com.agiledge.atom.dto.AdhocBookedEmployeeReportDto;

public class AdhocBookedEmployeeReportDao {
	public ArrayList<AdhocBookedEmployeeReportDto> getAdhocBookedEmp(String siteId, String fromDate, String toDate, String pickupDrop, String startTime){
	
		DbConnect dbConn = DbConnect.getInstance();
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		String travelFromDate=OtherFunctions.changeDateFromatToIso(fromDate);
		String traveltoDate=OtherFunctions.changeDateFromatToIso(toDate);
		
		String StartTimer = "";
		if(startTime.equalsIgnoreCase("ALL"))
		{
		
		}else{
			StartTimer = "and a.startTime= '"+startTime+"' ";
		}
		ArrayList<AdhocBookedEmployeeReportDto> dtoList = new ArrayList<AdhocBookedEmployeeReportDto>();
		con = dbConn.connectDB();
		String query = "SELECT e.personnelNo,e.displayname as BookedFor,a.adhocType,date_format(a.travelDate,'%d/%m/%y') travelDate,date_format(a.bookedDate,'%d/%m/%y') bookedDate,e1.displayname as BookedBy,a.pickDrop,a.startTime,e2.displayname,a.status FROM adhocbooking a join employee e on  e.id =a.empId join employee e1 on a.bookedBy=e1.id  left outer join employee e2 on e2.id=a.approvedBy where  siteId="+siteId+" and a.pickDrop ='"+pickupDrop+"' "+StartTimer+"and a.travelDate BETWEEN '"+travelFromDate+"' and '"+traveltoDate+"'";
			try {			
			stmt = con.createStatement();
			rs = stmt.executeQuery(query);
			while(rs.next())
			{
				AdhocBookedEmployeeReportDto dto = new AdhocBookedEmployeeReportDto();
				dto.setSiteId(siteId);
				dto.setPersonnelNo(rs.getString(1));
				dto.setBookedFor(rs.getString(2));
				dto.setAdhocType(rs.getString(3));
			    //dto.setTravelDate(dateFormat.format(rs.getDate(4)));
				//dto.setBookedDate(dateFormat.format(rs.getDate(5)));
				dto.setTravelDate(rs.getString(4));
				dto.setBookedDate(rs.getString(5));
				dto.setBookedBy(rs.getString(6));
				dto.setPickupDrop(rs.getString(7));
				dto.setStartTime(rs.getString(8));
				String approvedby=rs.getString(9)==null ? "" :rs.getString(9);
				dto.setApprovedBy(approvedby);
				dto.setStatus(rs.getString(10));
				dtoList.add(dto);
				
			}
			
			return dtoList;
		} catch (SQLException e) {
			System.err.println("Report:error in AdhocBookedEmployeeReportDao");
		}
		
		return dtoList;
	}
}
