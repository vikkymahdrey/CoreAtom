package com.agiledge.atom.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import com.agiledge.atom.dbConnect.DbConnect;
import com.agiledge.atom.dto.TripDetailsDto;

public class TripCostDao {
	public ArrayList<TripDetailsDto> gettripCostInRange(String fromDate,
			String toDate, String project) {
		DbConnect db = DbConnect.getInstance();
		Connection con = null;
		con = db.connectDB();
		PreparedStatement pst = null;
		ResultSet rs = null;
		ArrayList<TripDetailsDto> tripCostList=new ArrayList<TripDetailsDto>();
		TripDetailsDto costDto=null;
		/*
		 * 
		 * PUT schedule Id to vendor_trip_sheet table and replace
		 * trip_details_child table from the quey
		 */
		String query = "select bb.trip_date, bb.empCount,bb.projectEmpCount,bb.otherProjectEmpCount,bb.projectFemaleCountInSecurityTrip,bb.otherProjectFemaleCountInSecurityTrip,"
				+ "bb.baseCost,bb.securityCost,projectEmpCount*(bb.baseCost/(bb.empCount+bb.projectEmpCount)) as projectEmpCost"
				+ "projectFemaleCountInSecurityTrip*(bb.securityCost/(projectFemaleCountInSecurityTrip+otherProjectFemaleCountInSecurityTrip)) projectEscortCost from ( "
				+ " select trip_date,"

				+ " (select count(*) from vendor_trip_sheet where tripid in (select id from trip_details td1 where td1.trip_date=td.trip_date)) "
				+ "as empCount, "

				+ " (select count(*) from trip_details_child tdc,employee_schedule es,vendor_trip_sheet vts where tdc.tripid in (select id from trip_details td1 where td1.trip_date=td.trip_date ) "
				+ " and tdc.tripid=vts.tripid and  tdc.scheduleId=es.id and tdc.employeeId=vts.employeeid and es.project='"
				+ project
				+ "') "
				+ " as projectEmpCount,"

				+ " (select count(*) from trip_details_child tdc,employee_schedule es,vendor_trip_sheet vts where tdc.tripid=vts.tripid and vts.employeeId=tdc.employeeid and tdc.tripid in (select id from trip_details td1 where td1.trip_date=td.trip_date ) "
				+ " and tdc.scheduleId=es.id  and es.project!='"
				+ project
				+ "')"
				+ " as otherProjectEmpCount,"

				+ " (select count(*) from trip_details_child tdc,employee_schedule es,vendor_trip_sheet vts where tdc.tripid in (select id from trip_details td1 where td1.trip_date=td.trip_date )"
				+ " and tdc.tripid=vts.tripid and vts.employeeid=tdc.employeeid and tdc.scheduleId=es.id and es.project='"
				+ project
				+ "' and tdc.tripid in (select tripid from vendor_trip_sheet_parent vtsp where vtsp.escort='YES' ) and tdc.employeeId in "
				+ "  (select e.id from employee e where e.gender='F')) "
				+ " as projectFemaleCountInSecurityTrip, "

				+ " (select count(*) from trip_details_child tdc,employee_schedule es,vendor_trip_sheet vts where vts.tripid in (select id from trip_details td1 where td1.trip_date=td.trip_date ) "
				+ " and vts.tripid=tdc.tripid and vts.employeeid=tdc.employeeid and tdc.scheduleId=es.id and es.project !='"
				+ project
				+ "' and tdc.tripid in (select tripid from vendor_trip_sheet_parent vtsp where vtsp.escort='YES' ) and tdc.employeeId in "
				+ " (select e.id from employee e where e.gender ='F')) "
				+ " as otherProjectFemaleCountInSecurityTrip,"

				+ " (select sum(svrc.ratePerTrip) "
				+ " from site_vehicle_rate_calander svrc, trip_details td1 where "
				+ " td1.trip_date>=svrc.fromDate and td1.trip_date< ifnull(svrc.toDate,date_add(curdate(),  interval 2 day)) and "
				+ " td1.trip_date=td.trip_date and td1.vehicle_type=svrc.vehicle_type) "
				+ "as baseCost, "

				+ " (select ifnull(sum(svrc.ratePerTrip),0) from "
				+ " trip_details td1,vendor_trip_sheet_parent vtsp, site_vehicle_rate_calander svrc where "
				+ " td1.id=vtsp.tripid and "
				+ " td1.trip_date=td.trip_date and td1.vehicle_type=svrc.vehicle_type   and "
				+ " td1.trip_date>=svrc.fromDate and td1.trip_date< ifnull(svrc.toDate,date_add(curdate(),  interval 2 day)) "
				+ " and vtsp.escort='YES') "
				+ "as securityCost "

				+ " from trip_details td where td.trip_date between '"
				+ fromDate + "' and '" + toDate + "' group by trip_date) as bb";
		try {
			
			pst=con.prepareStatement(query);
			rs=pst.executeQuery();
			while(rs.next())				
			{
				costDto=new TripDetailsDto();
				costDto.setTrip_date(rs.getString("trip_date"));
				costDto.setEmpCount(rs.getInt("empCount"));
				costDto.setProjectEmpCount(rs.getInt("projectEmpCount"));
				costDto.setNotProjectEmpCount(rs.getInt("notProjectEmpCount"));
				costDto.setProjectFemaleCountInSecurityTrip(rs.getInt("projectFemaleCountInSecurityTrip"));
				costDto.setProjectFemaleCountInSecurityTrip(rs.getInt("projectFemaleCountInSecurityTrip"));	
				costDto.setNotProjectFemaleCountInSecurityTrip(rs.getInt("notProjectFemaleCountInSecurityTrip"));
				costDto.setTripBaseCost(rs.getString("baseCost"));
				costDto.setEscortCost(rs.getString("securityCost"));
				costDto.setProjectEmpCost(rs.getString("projectEmpCost"));
				costDto.setProjectEscortCost(rs.getString("projectEscortCost"));
				tripCostList.add(costDto);
				
			}

		} catch (Exception e) {

		}
		return tripCostList;
	}

	public ArrayList<TripDetailsDto> gettripCostForDate(String date, String project) {
		DbConnect db = DbConnect.getInstance();
		Connection con = null;
		con = db.connectDB();
		PreparedStatement pst = null;
		ResultSet rs = null;

		String query = "";
		if (project != null && project.equals("")) {
			query = "select tabl.id,tabl.trip_code,tabl.vehicleType,tabl.ratePertrip,tabl.escort,tabl.empCount,tabl.femaleCount,tabl.ProjectEmployeeCount,tabl.ProjectfemaleCount "
					+ "(select td.trip_code,vt.type,svrc.ratePertrip,vtsp.escort,"
					+ " (select count(*) from vendor_trip_sheet vts where vts.tripid=td.id) as empCount, "
					+ " (select count(*) from vendor_trip_sheet vts,employee e where vts.tripid=td.id and vts.employeeId=e.id and e.gender='F' ) as femaleCount "
					+ " ,(select count(*) from trip_details_child tdc,employee_schedule es where tdc.tripid=td.id and tdc.scheduleId=es.id and es.project="
					+ project
					+ ") as ProjectEmployeeCount,"
					+ " (select count(*) from trip_details_child tdc,employee e,employee_schedule es where tdc.tripid=td.id and tdc.scheduleId=es.id and tdc.employeeId=e.id and e.gender='F'  and  es.project="
					+ project
					+ ") as ProjectfemaleCount "
					+ " from trip_details td,vendor_trip_sheet_parent vtsp,vehicle_type vt, "
					+ " site_vehicle_rate_calander svrc,trip_details_child tdc "
					+ " where td.trip_date='"
					+ date
					+ "' and td.id=vtsp.tripid and td.vehicle_type=vt.id and vt.id=svrc.vehicle_type and svrc.fromDate<=td.trip_date and "
					+ " ifnull(svrc.toDate,date_add(curdate(),  interval 2 day)) and svrc.site=td.siteId "
					+ " and tdc.scheduleId in(select id from employee_schedule where project="
					+ project
					+ ") "
					+ " and tdc.tripid=td.id group by td.id) as tabl ";
		} else {
			query = "select tabl.id,tabl.trip_code,tabl.vehicleType,tabl.ratePertrip,tabl.escort,tabl.empCount,tabl.femaleCount from "
					+ "(select td.trip_code,vt.type,svrc.ratePertrip,vtsp.escort,"
					+ " (select count(*) from vendor_trip_sheet vts where vts.tripid=td.id) as empCount, "
					+ " (select count(*) from vendor_trip_sheet vts,employee e where vts.tripid=td.id and vts.employeeId=e.id and e.gender='F' ) as femaleCount "
					+ " from trip_details td,vendor_trip_sheet_parent vtsp,vehicle_type vt, "
					+ " site_vehicle_rate_calander svrc,trip_details_child tdc "
					+ " where td.trip_date='"
					+ date
					+ "' and td.id=vtsp.tripid and td.vehicle_type=vt.id and vt.id=svrc.vehicle_type and svrc.fromDate<=td.trip_date and "
					+ " ifnull(svrc.toDate,date_add(curdate(),  interval 2 day)) and svrc.site=td.siteId "
					+ " and tdc.tripid=td.id group by td.id ) as tabl";
		}

		try {
			pst=con.prepareStatement(query);
			

		} catch (Exception e) {

		}
		return null;
	}

	public ArrayList<TripDetailsDto> gettripCostForTrip(String tripId,
			String project) {
		DbConnect db = DbConnect.getInstance();
		Connection con = null;
		con = db.connectDB();
		PreparedStatement pst = null;
		ResultSet rs = null;

		String query = "";
		String projectQuery = "";
		if (project != null && project.equals("")) {
			projectQuery = "and es.project=" + project;
		}

		query = "select td.trip_code,e.displayName,e.gender,vtsp.escort,svrc.ratepertrip "
				+ " from vendor_trip_sheet vts,employee e,vendor_trip_sheet_parent vtsp,site_vehicle_rate_calander svrc,trip_details td,trip_details_child tdc,employee_schedule es where "
				+ " td.id="
				+ tripId
				+ " and td.id=vtsp.tripid and td.id=tdc.tripid "
				+ " and td.id=vts.tripid and vts.employeeid=tdc.employeeid and tdc.scheduleid=es.id "
				+ projectQuery
				+ " and td.vehicle_type=svrc.vehicle_type and svrc.fromDate<=td.trip_date and "
				+ " td.trip_date < ifnull(svrc.toDate,date_add(curdate(), interval 2 day) ) "
				+ " and vts.employeeId=e.id ";		
		return null;
	}
}
