package com.agiledge.atom.reports;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import com.agiledge.atom.commons.OtherFunctions;
import com.agiledge.atom.dbConnect.DbConnect;
import com.agiledge.atom.dto.TripCommentDto;
import com.agiledge.atom.dto.TripDetailsDto;

public class TripBillingBreakUpsReportHelper {

	public ArrayList<TripDetailsDto> getBillBreakUpsReport(int month,int year,int site)
	{
		 
 
		DbConnect dbConn = DbConnect.getInstance();
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		ArrayList<TripDetailsDto> dtoList = new ArrayList<TripDetailsDto>();
 		 
		 
		  		 
		try {
		 
			String query=" select td.id, td.trip_date,td.trip_code,vt.type,vtsp.vehicleNo,vtsp.escort," +
					"(case when vtsp.escort='NO' then '--' else vtsp.escortClock end)escortClock,((td.trip_time+' ')+case when td.trip_log='IN' then 'Pick Up' else 'Drop' end) trip_time, vtsp.logTime reportingTime,vtsp.onTimeStatus,isnull(vtsp.approvalStatus , 'Open')approvalStatus," +
					" vt.sit_cap, (select COUNT(employeeId) from vendor_trip_sheet where tripId=td.id  ) paxs, (select COUNT(employeeId) from vendor_trip_sheet where tripId=td.id and showStatus='Show') showCount, (select COUNT(employeeId) from vendor_trip_sheet where tripId=td.id and showStatus='No Show') noShowCount" +
					", (select top 1 count(c.id)  from TripComments c where tripId=td.id  ) commentCount    from trip_details td  ,vendor_trip_sheet_parent vtsp,vehicle_type vt       where  td.siteId="+site+" and month(td.trip_date)="+month+" and year(td.trip_date)="+year+" and  td.id=vtsp.tripId and td.vehicle_type=vt.id order by td.trip_date, td.id     ";
System.out.println("Query : "+ query);
			con = dbConn.connectDB();
			stmt = con.createStatement();
			rs = stmt.executeQuery(query);
			
			while(rs.next()) {
				TripDetailsDto dto = new TripDetailsDto();
				dto.setId(rs.getString("id"));
				dto.setTrip_date(OtherFunctions.changeDateFromatToddmmyyyy(rs.getString("trip_date")));
				dto.setTrip_code(rs.getString("trip_code"));
				dto.setVehicle_type(rs.getString("type"));
				dto.setVehicleNo(rs.getString("vehicleNo"));
				dto.setEscort(rs.getString("escort"));
				dto.setTrip_time(rs.getString("trip_time"));
				dto.setActualLogTime(rs.getString("reportingTime"));
				dto.setOnTimeStatus(rs.getString("onTimeStatus"));
				dto.setApprovalStatus(rs.getString("approvalStatus"));
				dto.setSittingCapasity(rs.getInt("sit_cap"));
				dto.setTotalEmployees(rs.getInt("paxs"));
				dto.setShowCount(rs.getInt("showCount"));
				dto.setNoShowCount(rs.getInt("noShowCount"));
				dto.setComment(rs.getString("commentCount"));		 
				dto.setEscortNo(rs.getString("escortClock"));
				 dto.setComments(getTripCommentReport(dto.getId()));
				dtoList.add(dto);
			}
			 
			
			return dtoList;
			 

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(stmt);
			DbConnect.closeConnection(con);
		}

		return null;
	}
	
	
	public ArrayList<TripCommentDto> getTripCommentReport(String tripId)
	{
		 
 
		DbConnect dbConn = DbConnect.getInstance();
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		ArrayList<TripCommentDto> dtoList = new ArrayList<TripCommentDto>();
 		 
		 
		  		 
		try {
		 
			String query=" select e.displayname, c.* from TripComments c join employee e on c.commentedBy=e.id where tripId="+ tripId + " order by date desc";
System.out.println("Query : "+ query);
			con = dbConn.connectDB();
			stmt = con.createStatement();
			rs = stmt.executeQuery(query);
			
			while(rs.next()) {
				TripCommentDto dto = new TripCommentDto();
				 dto.setComment(rs.getString("comment"));
				 dto.setCommentedByName(rs.getString("displayname"));
				 dto.setCommentedDate(rs.getString("date"));
				dtoList.add(dto);
			}
			 
			
			return dtoList;
			 

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(stmt);
			DbConnect.closeConnection(con);
		}

		return null;
	}
	

}
