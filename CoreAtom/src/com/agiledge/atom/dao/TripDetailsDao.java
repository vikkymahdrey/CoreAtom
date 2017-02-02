/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.agiledge.atom.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import com.agiledge.atom.billingtype.dto.BillingTypeConstant;
import com.agiledge.atom.commons.OtherFunctions;
import com.agiledge.atom.commons.RandomString;
import com.agiledge.atom.constants.AuditLogConstants;
import com.agiledge.atom.constants.SettingsConstant;
import com.agiledge.atom.dbConnect.DbConnect;
import com.agiledge.atom.dto.AuditLogDTO;
import com.agiledge.atom.dto.EmployeeDto;
import com.agiledge.atom.dto.PanicDto;
import com.agiledge.atom.dto.RouteDto;
import com.agiledge.atom.dto.TripCommentDto;
import com.agiledge.atom.dto.TripDetailsChildDto;
import com.agiledge.atom.dto.TripDetailsDto;
import com.agiledge.atom.dto.VehicleDto;
import com.agiledge.atom.dto.VehicleTypeDto;
import com.agiledge.atom.dto.VendorDto;
import com.agiledge.atom.service.TripDetailsService;
import com.agiledge.atom.sms.SMSService;
import com.agiledge.atom.trip.dto.TripDetailsConstant;

/**
 * 
 * @author muhammad
 */
//sandesh
public class TripDetailsDao {
	String uniqueId = "";
	String uniqueCode = "";
	int uniqueNo = 1;

	public void createUniqueID(String date, String mode, String siteid) {
		DbConnect ob = null;
		Connection con = null;
		ResultSet rs = null;
		Statement st = null;
		try {
			ob = DbConnect.getInstance();
			con = ob.connectDB();
			uniqueCode = OtherFunctions.getOrdinaryDateFormat(date);
			uniqueCode = uniqueCode.replaceAll("/", "");
			if (mode.equals("IN"))
				uniqueCode += "P";
			if (mode.equals("OUT"))
				uniqueCode += "D";

			st = con.createStatement();
			String query = "select CAST(SUBSTRING(trip_code,8,3) as UNSIGNED) as serial from trip_details where siteId="
					+ siteid
					+ " and trip_date='"
					+ date
					+ "' and trip_log='"
					+ mode
					+ "' and status in ('saved','addsave','saveedit') order by  serial desc limit 1";
			// System.out.println("query" + query);
			rs = st.executeQuery(query);
			if (rs.next()) {
				uniqueNo = rs.getInt(1) + 1;
			}
		} catch (SQLException ex) {

			System.out.println("Error in save" + ex);
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);
		}
	}

	public void getIncremenetedUnique() {
		if (uniqueNo < 10) {
			uniqueId = uniqueCode + "00" + uniqueNo;
		} else if (uniqueNo < 100) {
			uniqueId = uniqueCode + "0" + uniqueNo;
		} else {
			uniqueId = uniqueCode + uniqueNo;
		}

		uniqueNo++;

	}

	public void saveTrip(String tripDate, String tripLog, String siteId,
			String tripTime, String doneBy, String tripIds) {
		DbConnect ob = null;
		Connection con = null;
		ResultSet rs = null;
		PreparedStatement pst = null;
		PreparedStatement pst1 = null;
		int autoincNumber = 0, changedBy = 0;
		createUniqueID(tripDate, tripLog, siteId);
		try {
			changedBy = Integer.parseInt(doneBy);
			ob = DbConnect.getInstance();
			con = ob.connectDB();
			String subQuery = "";
			if (tripIds != null) {
				subQuery = " id in (" + tripIds + ") and ";
			}
			pst = con
					.prepareStatement("update trip_details set status='saved' where "
							+ subQuery
							+ " siteId=? and status='routed' and trip_date=? and trip_log=? and trip_time=?");
			pst.setString(1, siteId);
			pst.setString(2, tripDate);
			pst.setString(3, tripLog);
			pst.setString(4, tripTime);
			pst.executeUpdate();
			pst = con
					.prepareStatement("update trip_details set status='saveedit' where "
							+ subQuery
							+ " siteId=? and status='modified'  and trip_date=? and trip_log=? and trip_time=?");
			pst.setString(1, siteId);
			pst.setString(2, tripDate);
			pst.setString(3, tripLog);
			pst.setString(4, tripTime);
			pst.executeUpdate();
			pst = con
					.prepareStatement("update trip_details set status='addsave' where "
							+ subQuery
							+ " siteId=? and status='addmod'  and trip_date=? and trip_log=? and trip_time=?");
			pst.setString(1, siteId);
			pst.setString(2, tripDate);
			pst.setString(3, tripLog);
			pst.setString(4, tripTime);
			pst.executeUpdate();
			pst = con
					.prepareStatement("update trip_details set status='deleted' where "
							+ subQuery
							+ " siteId=? and status='removed'  and trip_date=? and trip_log=? and trip_time=?");
			pst.setString(1, siteId);
			pst.setString(2, tripDate);
			pst.setString(3, tripLog);
			pst.setString(4, tripTime);
			pst.executeUpdate();

			String tripId = "";
			String query1 = "SELECT ID from trip_details where "
					+ subQuery
					+ " siteId=?  and trip_date=? and trip_log=? and trip_time=? and status in ('saved','saveedit','addsave')";
			pst = con.prepareStatement(query1);
			pst.setString(1, siteId);
			pst.setString(2, tripDate);
			pst.setString(3, tripLog);
			pst.setString(4, tripTime);
			rs = pst.executeQuery();
			while (rs.next()) {
				tripId = rs.getString(1);
				getIncremenetedUnique();
				pst1 = con
						.prepareStatement("update trip_details set trip_code=? where id=?");
				pst1.setString(1, uniqueId);
				pst1.setString(2, tripId);
				pst1.executeUpdate();
				autoincNumber = Integer.parseInt(tripId);
				auditLogEntryforsavetripsheet(autoincNumber,
						AuditLogConstants.TRIPSHEET_MODULE, changedBy,
						AuditLogConstants.WORKFLOW_STATE_TRIPSHEET_GENERATED,
						AuditLogConstants.WORKFLOW_STATE_TRIPSHEET_SAVED,
						AuditLogConstants.AUDIT_LOG_EDIT_ACTION);

			}
		} catch (SQLException ex) {

			System.out.println("Error in save" + ex);
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(pst);
			DbConnect.closeStatement(pst1);
			DbConnect.closeConnection(con);
		}

	}

	private void auditLogEntryforsavetripsheet(int autoincNumber,
			String Module, int changedBy, String preworkflowState,
			String curworkflowState, String auditLogAction) {
		AuditLogDTO auditLog = new AuditLogDTO();

		auditLog.setRelatedNodeId(autoincNumber);
		auditLog.setModuleName(Module);
		auditLog.setChangedBy(changedBy);
		auditLog.setDateChanged(new Date());
		auditLog.setPreviousState(preworkflowState);
		auditLog.setCurrentState(curworkflowState);
		auditLog.setAction(auditLogAction);

		AuditLogDAO auditDAO = new AuditLogDAO();
		auditDAO.addAuditLogEntry(auditLog);

	}

	public ArrayList<TripDetailsDto> getTripSheetActual(String tripDate,
			String tripLog, String siteId, String tripTime) {
		ArrayList<TripDetailsDto> tripSheet = null;
		DbConnect ob = null;
		Connection con = null;
		ob = DbConnect.getInstance();
		PreparedStatement pst = null;
		Statement st = null;
		ResultSet rs = null;
		ResultSet rs1 = null;
		try {
			con = ob.connectDB();
			TripDetailsDto tripDetailsDtoObj = null;
			TripDetailsChildDto tripDetailsChildDtoObj = null;
			tripSheet = new ArrayList<TripDetailsDto>();
			ArrayList<TripDetailsChildDto> tripSheetChild = null;
			st = con.createStatement();
			String subQuery = "";

			String query = "SELECT t.id,t.trip_code,t.trip_date,t.trip_time,t.trip_log,t.routeid,vt.type,t.security_status,t.distance,t.travelTime,t.preTripId,t.distanceFromPreTrip,t.status, vt.id vehicleTypeId ,'actual' as modifyStatus FROM trip_details t ,vehicle_type vt where t.siteId=? and (t.status=? or t.status=?) and vt.id=t.vehicle_type ";
			String query1 = "SELECT t.tripid as id,t.trip_code,t.trip_date,t.trip_time,t.trip_log,t.routeid,vt.type,t.security_status,t.distance,t.travelTime,t.preTripId,t.distanceFromPreTrip,t.status, vt.id vehicleTypeId ,'modified' as modifyStatus  FROM trip_details_parent_modified t ,vehicle_type vt where t.siteId=?  and vt.id=t.vehicle_type ";
			if (tripLog.equals("ALL")) {
				subQuery = " and t.trip_date='" + tripDate + "'";

			} else if (tripTime.equals("ALL")) {
				subQuery = " and t.trip_date='" + tripDate
						+ "' and t.trip_log='" + tripLog + "' ";
			} else {
				subQuery = " and t.trip_date='" + tripDate
						+ "' and t.trip_log='" + tripLog
						+ "' and t.trip_time='" + tripTime + "'";
			}
			String lastQuery = " as  tble order by tble.id,tble.modifyStatus";
			// System.out.println("select * from ("+query + subQuery
			// +" union "+query1 + subQuery +")"+ lastQuery);
			pst = con.prepareStatement("select * from (" + query + subQuery
					+ " union " + query1 + subQuery + ")" + lastQuery);
			// System.out.println(query + subQuery + lastQuery);
			pst.setString(1, siteId);
			pst.setString(2, "routed");
			pst.setString(3, "saved");
			pst.setString(4, siteId);
			rs = pst.executeQuery();
			while (rs.next()) {
				tripDetailsDtoObj = new TripDetailsDto();
				tripDetailsDtoObj.setId(rs.getString("id"));
				tripDetailsDtoObj.setActualLogTime(rs.getString("trip_time"));
				tripDetailsDtoObj.setTrip_code(rs.getString("trip_code"));
				tripDetailsDtoObj.setTrip_date(rs.getDate("trip_date")
						.toString());
				tripDetailsDtoObj.setTrip_time(rs.getString("trip_time"));
				tripDetailsDtoObj.setTrip_log(rs.getString("trip_log"));
				tripDetailsDtoObj.setRouteId(rs.getString("routeid"));
				tripDetailsDtoObj.setTrip_code(rs.getString("trip_code"));
				tripDetailsDtoObj.setVehicle_type(rs.getString("type"));
				tripDetailsDtoObj
						.setIsSecurity(rs.getString("security_status"));
				tripDetailsDtoObj.setVehicleId(rs.getString("vehicleTypeId"));
				// tripSheetChild = null;

				tripDetailsDtoObj.setDistance(rs.getString("distance"));
				tripDetailsDtoObj.setTravelTime(rs.getString("travelTime"));
				tripDetailsDtoObj.setPreviousTripId(rs.getString("preTripId"));
				tripDetailsDtoObj.setDistanceFromPrevioustrip(rs
						.getFloat("distanceFromPreTrip"));
				String status = rs.getString("status");
				tripSheetChild = new ArrayList<TripDetailsChildDto>();
				if (rs.getString("modifyStatus").equalsIgnoreCase("actual")) {
					query = "select tdc.employeeId,e.displayname as EmployeeName,e.personnelNo,e.gender,a.area,p.place,l.id as landmarkId,l.landmark,ifnull(e.contactNumber1,'') as contactno, vt.employeeId approvedEmployee,  vt.showStatus, vt.noShowReason,tdc.time,tdc.dist,e.gender from trip_details_child tdc  left  join vendor_trip_sheet vt on tdc.employeeId=vt.employeeId and tdc.tripId=vt.tripId ,employee e ,area a,place p,landmark l where e.id=tdc.employeeId and tdc.tripId="
							+ rs.getString(1)
							+ " and tdc.landmarkId=l.id and l.place=p.id and p.area=a.id order by tdc.routedOrder";
				} else {
					query = "select tdc.empid as employeeId,e.displayname as EmployeeName,e.personnelNo,e.gender,a.area,p.place,l.id as landmarkId,l.landmark, ifnull(e.contactNumber1,'') as contactno, vt.employeeId approvedEmployee,  vt.showStatus, vt.noShowReason,tdc.time,tdc.dist,e.gender from trip_details_modified tdc left  join vendor_trip_sheet vt on tdc.empId=vt.employeeId and tdc.tripId=vt.tripId ,employee e ,area a,place p,landmark l where e.id=tdc.empid and tdc.tripId="
							+ rs.getString(1)
							+ " and tdc.landmarkId=l.id and l.place=p.id and p.area=a.id order by tdc.routedOrder";
				}

				rs1 = st.executeQuery(query);
				while (rs1.next()) {
					tripDetailsChildDtoObj = new TripDetailsChildDto();
					tripDetailsChildDtoObj.setTripId("" + rs.getInt(1));
					tripDetailsChildDtoObj.setEmployeeId(rs1
							.getString("employeeId"));
					tripDetailsChildDtoObj.setEmployeeName(rs1
							.getString("EmployeeName"));
					tripDetailsChildDtoObj.setArea(rs1.getString("area"));
					tripDetailsChildDtoObj.setPlace(rs1.getString("place"));
					tripDetailsChildDtoObj.setLandmarkId(rs1
							.getString("landmarkId"));
					tripDetailsChildDtoObj.setLandmark(rs1
							.getString("landmark"));
					try {
						tripDetailsChildDtoObj
								.setDistance(rs1.getFloat("dist"));
						tripDetailsChildDtoObj.setTime(rs1.getString("time"));
					} catch (Exception e) {

					}
					tripSheetChild.add(tripDetailsChildDtoObj);
				}
				tripDetailsDtoObj.setTripDetailsChildDtoList(tripSheetChild);
				tripSheet.add(tripDetailsDtoObj);
			}

			// System.out.println("Completed");
		} catch (Exception e) {
			System.out.println("Error in dao" + e);
		} finally {
			DbConnect.closeResultSet(rs, rs1);
			DbConnect.closeStatement(st, pst);
			DbConnect.closeConnection(con);
		}
		return tripSheet;
	}

	public ArrayList<TripDetailsDto> getTripSheetSaved(String tripDate,
			String tripLog, String siteId, String tripTime) {
		ArrayList<TripDetailsDto> tripSheet = null;
		DbConnect ob = null;
		Connection con = null;
		Statement st = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		ResultSet rs1 = null;
		try {
			ob = DbConnect.getInstance();
			con = ob.connectDB();
			TripDetailsDto tripDetailsDtoObj = null;
			TripDetailsChildDto tripDetailsChildDtoObj = null;
			tripSheet = new ArrayList<TripDetailsDto>();
			ArrayList<TripDetailsChildDto> tripSheetChild = null;
			st = con.createStatement();
			// order by t.id
			String subQuery = "";
			String query = "SELECT t.id,t.trip_code,t.trip_date,t.trip_time,t.trip_log,t.routeid,t.trip_code,vt.id as vehicletypeId,vt.sit_cap,vt.type,t.status,t.security_status,t.distance,t.travelTime,t.preTripId,t.distanceFromPreTrip, case when t.vehicle is not null then (select regno from vehicles where id=t.vehicle) else 'Not set' end as vehicleNo,case when t.driverid is not null then (select name from driver where id=t.driverid) else 'Not set' end as driver,case when t.driverid is not null then (select contact from driver where id=t.driverid) else ' ' end as driverContact,case when t.escortId is not null then (select concat(escortClock,'-',name) from escort where id=t.escortid) else ' ' end as escort    FROM trip_details t,vehicle_type vt where t.siteId=? and vt.id=t.vehicle_type and  (t.status=? or t.status=? or t.status=?) ";
			if (tripLog.equals("ALL")) {
				subQuery = " and t.trip_date='" + tripDate + "'";

			} else if (tripTime.equals("ALL")) {
				subQuery = " and t.trip_date='" + tripDate
						+ "' and t.trip_log='" + tripLog + "' ";
			} else {
				subQuery = " and t.trip_date='" + tripDate
						+ "' and t.trip_log='" + tripLog
						+ "' and t.trip_time='" + tripTime + "'";
			}
			String lastQuery = " order by t.id ";
			// System.out.println("HHHHHHH" + query + subQuery + lastQuery);
			pst = con.prepareStatement(query + subQuery + lastQuery);
			pst.setString(1, siteId);
			pst.setString(2, "saved");
			pst.setString(3, "addsave");
			pst.setString(4, "saveedit");
			rs = pst.executeQuery();
			while (rs.next()) {
				tripDetailsDtoObj = new TripDetailsDto();
				tripDetailsDtoObj.setId(rs.getString("id"));
				tripDetailsDtoObj.setSiteId(siteId);
				tripDetailsDtoObj.setVehicleNo(rs.getString("vehicleNo"));
				tripDetailsDtoObj.setDriverName(rs.getString("driver"));
				// System.out.println(tripDetailsDtoObj.getDriverName()+"vehicles:::::"+tripDetailsDtoObj.getVehicleNo());
				tripDetailsDtoObj.setTrip_code(rs.getString("trip_code"));
				tripDetailsDtoObj.setTrip_date(rs.getDate("trip_date")
						.toString());
				tripDetailsDtoObj.setTrip_time(rs.getString("trip_time"));
				tripDetailsDtoObj.setTrip_log(rs.getString("trip_log"));
				tripDetailsDtoObj.setRouteId(rs.getString("routeid"));
				tripDetailsDtoObj.setTrip_code(rs.getString("trip_code"));
				tripDetailsDtoObj.setVehicle_type(rs.getString("type"));
				tripDetailsDtoObj.setSeat(rs.getInt("sit_cap"));
				tripDetailsDtoObj.setVehicleTypeId(rs
						.getString("vehicletypeId"));
				tripDetailsDtoObj
						.setIsSecurity(rs.getString("security_status"));
				// tripSheetChild = null;
				tripSheetChild = new ArrayList<TripDetailsChildDto>();
				tripDetailsDtoObj.setDistance(rs.getString("distance"));
				tripDetailsDtoObj.setTravelTime(rs.getString("travelTime"));
				tripDetailsDtoObj.setPreviousTripId(rs.getString("preTripId"));
				tripDetailsDtoObj.setDistanceFromPrevioustrip(rs
						.getFloat("distanceFromPreTrip"));
				tripDetailsDtoObj.setDriverContact(rs
						.getString("driverContact"));
				tripDetailsDtoObj.setEscort(rs.getString("escort"));
				query = "select tdc.scheduleId, tdc.employeeId as employeeId,e.displayname as EmployeeName,e.personnelNo,e.gender,a.area,p.place,l.id as landmarkId,l.landmark, ifnull(e.contactNumber1,'') as contactno, vt.employeeId approvedEmployee,  vt.showStatus, vt.noShowReason,tdc.time,tdc.dist,e.gender,tdc.transportType from trip_details_child tdc left  join vendor_trip_sheet vt on tdc.employeeid=vt.employeeId and tdc.tripId=vt.tripId ,employee e ,area a,place p,landmark l where e.id=tdc.employeeId and tdc.tripId="
						+ rs.getString(1)
						+ " and tdc.landmarkId=l.id and l.place=p.id and p.area=a.id order by tdc.routedOrder";
				rs1 = st.executeQuery(query);
				while (rs1.next()) {

					tripDetailsChildDtoObj = new TripDetailsChildDto();
					tripDetailsChildDtoObj.setApprovedEmployee(rs1
							.getString("approvedEmployee"));
					tripDetailsChildDtoObj.setShowStatus(rs1
							.getString("showStatus"));
					tripDetailsChildDtoObj.setReason(rs1
							.getString("noShowReason"));

					if (tripDetailsChildDtoObj.getApprovedEmployee() != null
							&& !tripDetailsChildDtoObj.getApprovedEmployee()
									.equals("")) {
						tripDetailsDtoObj.setCanTravel(true);
					}

					tripDetailsChildDtoObj.setTripId("" + rs.getInt(1));
					tripDetailsChildDtoObj.setEmployeeId(rs1
							.getString("employeeId"));
					tripDetailsChildDtoObj.setPersonnelNo(rs1
							.getString("personnelNo"));
					tripDetailsChildDtoObj.setEmployeeName(rs1
							.getString("EmployeeName"));
					tripDetailsChildDtoObj.setGender(rs1.getString("gender"));
					tripDetailsChildDtoObj.setArea(rs1.getString("area"));
					tripDetailsChildDtoObj.setPlace(rs1.getString("place"));
					tripDetailsChildDtoObj.setLandmarkId(rs1
							.getString("landmarkId"));
					tripDetailsChildDtoObj.setLandmark(rs1
							.getString("landmark"));
					tripDetailsChildDtoObj.setContactNumber(rs1
							.getString("contactno"));
					tripDetailsChildDtoObj.setScheduleId(rs1
							.getString("scheduleId"));

					try {
						tripDetailsChildDtoObj
								.setDistance(rs1.getFloat("dist"));
						// System.out.println("Dist"+tripDetailsChildDtoObj.getDistance());
						tripDetailsChildDtoObj.setTime(rs1.getString("time"));
						tripDetailsChildDtoObj.setTransportType(rs1
								.getString("transportType"));

					} catch (Exception e) {
						// System.out.println("error in child dist time ttype"+e);
					}

					tripSheetChild.add(tripDetailsChildDtoObj);
				}

				tripDetailsDtoObj.setTripDetailsChildDtoList(tripSheetChild);
				tripSheet.add(tripDetailsDtoObj);
				rs1.close();
			}

		} catch (Exception e) {
			DbConnect.closeConnection(con);
			System.out.println("Error in getting saved" + e);
		} finally {
			DbConnect.closeResultSet(rs, rs1);
			DbConnect.closeStatement(st, pst);
			DbConnect.closeConnection(con);
		}
		return tripSheet;
	}

	/*- this function is only for vendor as of now.
	 * 
	 */
	
	public ArrayList<TripDetailsDto> getVendorAssignedTripSheet(String tripDate,
			String tripLog, String siteId, String tripTime) {
		ArrayList<TripDetailsDto> tripSheet = null;
		DbConnect ob = null;
		Connection con = null;
		Statement st = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		ResultSet rs1 = null;
		try {
			ob = DbConnect.getInstance();
			con = ob.connectDB();
			TripDetailsDto tripDetailsDtoObj = null;
			TripDetailsChildDto tripDetailsChildDtoObj = null;
			tripSheet = new ArrayList<TripDetailsDto>();
			ArrayList<TripDetailsChildDto> tripSheetChild = null;
			st = con.createStatement();
			// order by t.id
			String subQuery = "";
			String query = "SELECT t.id,t.trip_code,t.trip_date,t.trip_time,t.trip_log,t.routeid,t.trip_code,vt.id as vehicletypeId,vt.sit_cap,vt.type,t.status,t.security_status,t.distance,t.travelTime,t.preTripId,t.distanceFromPreTrip, case when t.vehicle is not null then (select regno from vehicles where id=t.vehicle) else 'Not set' end as vehicleNo,case when t.driverid is not null then (select name from driver where id=t.driverid) else 'Not set' end as driver,case when t.driverid is not null then (select contact from driver where id=t.driverid) else ' ' end as driverContact,case when t.escortId is not null then (select concat(escortClock,'-',name) from escort where id=t.escortid) else ' ' end as escort    FROM trip_details t,vehicle_type vt,tripvendorassign tva where t.id=tva.tripId and t.siteId=? and vt.id=t.vehicle_type and  (t.status=? or t.status=? or t.status=?) ";
			if (tripLog.equals("ALL")) {
				subQuery = " and t.trip_date='" + tripDate + "'";

			} else if (tripTime.equals("ALL")) {
				subQuery = " and t.trip_date='" + tripDate
						+ "' and t.trip_log='" + tripLog + "' ";
			} else {
				subQuery = " and t.trip_date='" + tripDate
						+ "' and t.trip_log='" + tripLog
						+ "' and t.trip_time='" + tripTime + "'";
			}
			String lastQuery = " order by t.id ";
			// System.out.println("HHHHHHH" + query + subQuery + lastQuery);
			pst = con.prepareStatement(query + subQuery + lastQuery);
			pst.setString(1, siteId);
			pst.setString(2, "saved");
			pst.setString(3, "addsave");
			pst.setString(4, "saveedit");
			rs = pst.executeQuery();
			while (rs.next()) {
				tripDetailsDtoObj = new TripDetailsDto();
				tripDetailsDtoObj.setId(rs.getString("id"));
				tripDetailsDtoObj.setSiteId(siteId);
				tripDetailsDtoObj.setVehicleNo(rs.getString("vehicleNo"));
				tripDetailsDtoObj.setDriverName(rs.getString("driver"));
				// System.out.println(tripDetailsDtoObj.getDriverName()+"vehicles:::::"+tripDetailsDtoObj.getVehicleNo());
				tripDetailsDtoObj.setTrip_code(rs.getString("trip_code"));
				tripDetailsDtoObj.setTrip_date(rs.getDate("trip_date")
						.toString());
				tripDetailsDtoObj.setTrip_time(rs.getString("trip_time"));
				tripDetailsDtoObj.setTrip_log(rs.getString("trip_log"));
				tripDetailsDtoObj.setRouteId(rs.getString("routeid"));
				tripDetailsDtoObj.setTrip_code(rs.getString("trip_code"));
				tripDetailsDtoObj.setVehicle_type(rs.getString("type"));
				tripDetailsDtoObj.setSeat(rs.getInt("sit_cap"));
				tripDetailsDtoObj.setVehicleTypeId(rs
						.getString("vehicletypeId"));
				tripDetailsDtoObj
						.setIsSecurity(rs.getString("security_status"));
				// tripSheetChild = null;
				tripSheetChild = new ArrayList<TripDetailsChildDto>();
				tripDetailsDtoObj.setDistance(rs.getString("distance"));
				tripDetailsDtoObj.setTravelTime(rs.getString("travelTime"));
				tripDetailsDtoObj.setPreviousTripId(rs.getString("preTripId"));
				tripDetailsDtoObj.setDistanceFromPrevioustrip(rs
						.getFloat("distanceFromPreTrip"));
				tripDetailsDtoObj.setDriverContact(rs
						.getString("driverContact"));
				tripDetailsDtoObj.setEscort(rs.getString("escort"));
				query = "select tdc.scheduleId, tdc.employeeId as employeeId,e.displayname as EmployeeName,e.personnelNo,e.gender,a.area,p.place,l.id as landmarkId,l.landmark, ifnull(e.contactNumber1,'') as contactno, vt.employeeId approvedEmployee,  vt.showStatus, vt.noShowReason,tdc.time,tdc.dist,e.gender,tdc.transportType from trip_details_child tdc left  join vendor_trip_sheet vt on tdc.employeeid=vt.employeeId and tdc.tripId=vt.tripId ,employee e ,area a,place p,landmark l where e.id=tdc.employeeId and tdc.tripId="
						+ rs.getString(1)
						+ " and tdc.landmarkId=l.id and l.place=p.id and p.area=a.id order by tdc.routedOrder";
				rs1 = st.executeQuery(query);
				while (rs1.next()) {

					tripDetailsChildDtoObj = new TripDetailsChildDto();
					tripDetailsChildDtoObj.setApprovedEmployee(rs1
							.getString("approvedEmployee"));
					tripDetailsChildDtoObj.setShowStatus(rs1
							.getString("showStatus"));
					tripDetailsChildDtoObj.setReason(rs1
							.getString("noShowReason"));

					if (tripDetailsChildDtoObj.getApprovedEmployee() != null
							&& !tripDetailsChildDtoObj.getApprovedEmployee()
									.equals("")) {
						tripDetailsDtoObj.setCanTravel(true);
					}

					tripDetailsChildDtoObj.setTripId("" + rs.getInt(1));
					tripDetailsChildDtoObj.setEmployeeId(rs1
							.getString("employeeId"));
					tripDetailsChildDtoObj.setPersonnelNo(rs1
							.getString("personnelNo"));
					tripDetailsChildDtoObj.setEmployeeName(rs1
							.getString("EmployeeName"));
					tripDetailsChildDtoObj.setGender(rs1.getString("gender"));
					tripDetailsChildDtoObj.setArea(rs1.getString("area"));
					tripDetailsChildDtoObj.setPlace(rs1.getString("place"));
					tripDetailsChildDtoObj.setLandmarkId(rs1
							.getString("landmarkId"));
					tripDetailsChildDtoObj.setLandmark(rs1
							.getString("landmark"));
					tripDetailsChildDtoObj.setContactNumber(rs1
							.getString("contactno"));
					tripDetailsChildDtoObj.setScheduleId(rs1
							.getString("scheduleId"));

					try {
						tripDetailsChildDtoObj
								.setDistance(rs1.getFloat("dist"));
						// System.out.println("Dist"+tripDetailsChildDtoObj.getDistance());
						tripDetailsChildDtoObj.setTime(rs1.getString("time"));
						tripDetailsChildDtoObj.setTransportType(rs1
								.getString("transportType"));

					} catch (Exception e) {
						// System.out.println("error in child dist time ttype"+e);
					}

					tripSheetChild.add(tripDetailsChildDtoObj);
				}

				tripDetailsDtoObj.setTripDetailsChildDtoList(tripSheetChild);
				tripSheet.add(tripDetailsDtoObj);
				rs1.close();
			}

		} catch (Exception e) {
			DbConnect.closeConnection(con);
			System.out.println("Error in getting saved" + e);
		} finally {
			DbConnect.closeResultSet(rs, rs1);
			DbConnect.closeStatement(st, pst);
			DbConnect.closeConnection(con);
		}
		return tripSheet;
	}


	public ArrayList<TripDetailsDto> getTripSheetSaved(String tripDate,
			String tripLog, String siteId, String tripTime[]) {
		ArrayList<TripDetailsDto> tripSheet = null;
		DbConnect ob = null;
		Connection con = null;
		Statement st = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		ResultSet rs1 = null;
		try {
			ob = DbConnect.getInstance();
			con = ob.connectDB();
			TripDetailsDto tripDetailsDtoObj = null;
			TripDetailsChildDto tripDetailsChildDtoObj = null;
			tripSheet = new ArrayList<TripDetailsDto>();
			ArrayList<TripDetailsChildDto> tripSheetChild = null;
			st = con.createStatement();
			String subQuery = "";
			String query = "SELECT t.id,t.trip_code,t.trip_date,t.trip_time,t.trip_log,t.routeid,t.trip_code,vt.type,t.status,t.security_status,t.distance,t.travelTime,t.preTripId,t.distanceFromPreTrip, case when t.vehicle is not null then (select regno from vehicles where id=t.vehicle) else 'Not set' end as vehicleNo,case when t.driverid is not null then (select name from driver where id=t.driverid) else 'Not set' end as driver    FROM trip_details t,vehicle_type vt where t.siteId=? and vt.id=t.vehicle_type and  (t.status=? or t.status=? or t.status=?) ";
			if (tripLog.equals("ALL")) {
				subQuery = " and t.trip_date='" + tripDate + "'";

			} else if (tripTime.equals("ALL")) {
				subQuery = " and t.trip_date='" + tripDate
						+ "' and t.trip_log='" + tripLog + "' ";
			} else {
				subQuery = " and t.trip_date='" + tripDate
						+ "' and t.trip_log='" + tripLog + "' ";
				String tripTimeQuuery = "";
				for (String ttime : tripTime) {
					tripTimeQuuery += ",'" + ttime + "'";
				}
				tripTimeQuuery = tripTimeQuuery.replaceFirst(",", "");
				subQuery = " and t.trip_time in (" + tripTimeQuuery + ") ";
			}
			String lastQuery = " order by t.id ";
			// System.out.println("HHHHHHH" + query + subQuery + lastQuery);
			pst = con.prepareStatement(query + subQuery + lastQuery);

			pst.setString(1, siteId);
			pst.setString(2, "saved");
			pst.setString(3, "addsave");
			pst.setString(4, "saveedit");
			pst.setString(5, tripDate);
			rs = pst.executeQuery();
			while (rs.next()) {

				tripDetailsDtoObj = new TripDetailsDto();
				tripDetailsDtoObj.setId(rs.getString("id"));
				tripDetailsDtoObj.setActualLogTime(rs.getString("logTime"));
				tripDetailsDtoObj.setVehicleNo(rs.getString("vehicleNo"));
				tripDetailsDtoObj.setOnTimeStatus(rs.getString("onTimeStatus"));
				tripDetailsDtoObj.setComment(rs.getString("comment"));
				tripDetailsDtoObj.setEscort(rs.getString("escort"));
				tripDetailsDtoObj.setEscortNo(rs.getString("escortClock"));

				tripDetailsDtoObj.setApprovalStatus(rs
						.getString("approvalStatus"));

				tripDetailsDtoObj.setTrip_code(rs.getString("trip_code"));
				tripDetailsDtoObj.setTrip_date(rs.getDate("trip_date")
						.toString());
				tripDetailsDtoObj.setTrip_time(rs.getString("trip_time"));
				tripDetailsDtoObj.setTrip_log(rs.getString("trip_log"));
				tripDetailsDtoObj.setRouteId(rs.getString("routeid"));
				tripDetailsDtoObj.setTrip_code(rs.getString("trip_code"));
				tripDetailsDtoObj.setVehicle_type(rs.getString("type"));
				tripDetailsDtoObj
						.setIsSecurity(rs.getString("security_status"));
				tripDetailsDtoObj.setComments(getTripComments(tripDetailsDtoObj
						.getId()));
				// tripSheetChild = null;
				tripSheetChild = new ArrayList<TripDetailsChildDto>();
				query = "select tdc.employeeId,e.displayname as EmployeeName,a.area,p.place,l.id as landmarkId,l.landmark,ifnull(e.contactNumber1,'') as contactno, vt.employeeId approvedEmployee,  vt.showStatus, vt.noShowReason,e.gender from trip_details_child tdc  left  join vendor_trip_sheet vt on tdc.employeeId=vt.employeeId and tdc.tripId=vt.tripId left outer join landmark l on tdc.landmarkId=l.id left outer join place p on l.place=p.id left outer join area a on p.area=a.id,employee e where e.id=tdc.employeeId and tdc.tripId="
						+ rs.getString(1) + " order by tdc.routedOrder";
			}
			// System.out.println("Query"+query);
			rs1 = st.executeQuery(query);
			while (rs1.next()) {

				tripDetailsChildDtoObj = new TripDetailsChildDto();
				tripDetailsChildDtoObj.setApprovedEmployee(rs1
						.getString("approvedEmployee"));
				tripDetailsChildDtoObj.setShowStatus(rs1
						.getString("showStatus"));
				tripDetailsChildDtoObj.setReason(rs1.getString("noShowReason"));

				if (tripDetailsChildDtoObj.getApprovedEmployee() != null
						&& !tripDetailsChildDtoObj.getApprovedEmployee()
								.equals("")) {
					tripDetailsDtoObj.setCanTravel(true);
				}

				tripDetailsChildDtoObj.setTripId("" + rs.getInt(1));
				tripDetailsChildDtoObj.setEmployeeId(rs1
						.getString("employeeId"));
				tripDetailsChildDtoObj.setEmployeeName(rs1
						.getString("EmployeeName"));
				tripDetailsChildDtoObj.setArea(rs1.getString("area"));
				tripDetailsChildDtoObj.setPlace(rs1.getString("place"));
				tripDetailsChildDtoObj.setLandmarkId(rs1
						.getString("landmarkId"));
				tripDetailsChildDtoObj.setLandmark(rs1.getString("landmark"));
				tripDetailsChildDtoObj.setContactNumber(rs1
						.getString("contactno"));
				tripSheetChild.add(tripDetailsChildDtoObj);
			}

			tripDetailsDtoObj.setTripDetailsChildDtoList(tripSheetChild);
			tripSheet.add(tripDetailsDtoObj);
			rs1.close();

		} catch (Exception e) {
			DbConnect.closeConnection(con);
			System.out.println("Error in getting saved" + e);
		} finally {
			DbConnect.closeResultSet(rs, rs1);
			DbConnect.closeStatement(st, pst);
			DbConnect.closeConnection(con);
		}
		return tripSheet;
	}

	public ArrayList<TripDetailsDto> getTripSheetModified(String tripDate,
			String tripLog, String siteId, String tripTime) {
		ArrayList<TripDetailsDto> tripSheet = null;
		//System.out.println("IN MODIFY>>>>>>>>>>>>>>STARTS");

		DbConnect ob = null;
		Connection con = null;
		PreparedStatement pst = null;
		Statement st = null;
		ResultSet rs = null;
		ResultSet rs1 = null;
		try {
			ob = DbConnect.getInstance();
			con = ob.connectDB();
			TripDetailsDto tripDetailsDtoObj = null;
			TripDetailsChildDto tripDetailsChildDtoObj = null;
			tripSheet = new ArrayList<TripDetailsDto>();
			ArrayList<TripDetailsChildDto> tripSheetChild = null;
			st = con.createStatement();

			String subQuery = "";

			String query = "SELECT t.id,t.trip_code,t.trip_date,t.trip_time,t.trip_log,t.routeid,t.trip_code,vt.type,t.status,t.security_status, p.logTime,t.distance,t.travelTime,t.preTripId,t.distanceFromPreTrip, ifnull(p.approvalStatus,'Open')  approvalStatus, p.vehicleNo   FROM trip_details t left join vendor_trip_sheet_parent p on t.id=p.tripId,vehicle_type vt where t.siteId=? and (t.status=? or t.status=? or t.status=?)and vt.id=t.vehicle_type ";
			if (tripLog.equals("ALL")) {
				subQuery = " and t.trip_date='" + tripDate + "'";

			} else if (tripTime.equals("ALL")) {
				subQuery = " and t.trip_date='" + tripDate
						+ "' and t.trip_log='" + tripLog + "' ";
			} else {
				subQuery = " and t.trip_date='" + tripDate
						+ "' and t.trip_log='" + tripLog
						+ "' and t.trip_time='" + tripTime + "'";
			}
			String lastQuery = " order by t.id ";

			pst = con.prepareStatement(query + subQuery + lastQuery);
			//System.out.println(query + subQuery + lastQuery);
			pst.setString(1, siteId);
			pst.setString(2, "routed");
			pst.setString(3, "addmod");
			pst.setString(4, "modified");
			rs = pst.executeQuery();
			while (rs.next()) {

				tripDetailsDtoObj = new TripDetailsDto();
				tripDetailsDtoObj.setId(rs.getString("id"));

				tripDetailsDtoObj.setActualLogTime(rs.getString("logTime"));
				tripDetailsDtoObj.setVehicleNo(rs.getString("vehicleNo"));

				tripDetailsDtoObj.setApprovalStatus(rs
						.getString("approvalStatus"));
				tripDetailsDtoObj.setTrip_code(rs.getString("trip_code"));
				tripDetailsDtoObj.setTrip_date(rs.getDate("trip_date")
						.toString());
				tripDetailsDtoObj.setTrip_time(rs.getString("trip_time"));
				tripDetailsDtoObj.setTrip_log(rs.getString("trip_log"));
				tripDetailsDtoObj.setRouteId(rs.getString("routeid"));
				tripDetailsDtoObj.setTrip_code(rs.getString("trip_code"));
				tripDetailsDtoObj.setVehicle_type(rs.getString("type"));
				tripDetailsDtoObj.setDistance(rs.getString("distance"));
				tripDetailsDtoObj.setTravelTime(rs.getString("travelTime"));
				tripDetailsDtoObj
						.setIsSecurity(rs.getString("security_status"));
				// tripSheetChild = null;
				tripSheetChild = new ArrayList<TripDetailsChildDto>();
				query = "select tdc.employeeId,e.displayname as EmployeeName, ifnull(e.contactNumber1,'') contactNumber1,a.area,p.place,l.id as landmarkId,l.landmark, vt.employeeId approvedEmployee,tdc.time,tdc.dist,e.gender,tdc.transportType  from trip_details_child tdc  left  join vendor_trip_sheet vt on tdc.employeeId=vt.employeeId and tdc.tripId=vt.tripId ,employee e ,area a,place p,landmark l where e.id=tdc.employeeId and tdc.tripId="
						+ rs.getString(1)
						+ " and tdc.landmarkId=l.id and l.place=p.id and p.area=a.id order by tdc.routedOrder";
				// System.out.println(query);
				rs1 = st.executeQuery(query);
				while (rs1.next()) {

					tripDetailsChildDtoObj = new TripDetailsChildDto();
					tripDetailsChildDtoObj.setApprovedEmployee(rs1
							.getString("approvedEmployee"));
					if (tripDetailsChildDtoObj.getApprovedEmployee() != null
							&& !tripDetailsChildDtoObj.getApprovedEmployee()
									.equals("")) {
						tripDetailsDtoObj.setCanTravel(true);
					}

					tripDetailsChildDtoObj.setTripId("" + rs.getInt(1));
					tripDetailsChildDtoObj.setEmployeeId(rs1
							.getString("employeeId"));
					tripDetailsChildDtoObj.setEmployeeName(rs1
							.getString("EmployeeName"));
					tripDetailsChildDtoObj.setGender(rs1.getString("gender"));
					tripDetailsChildDtoObj.setArea(rs1.getString("area"));
					tripDetailsChildDtoObj.setPlace(rs1.getString("place"));
					tripDetailsChildDtoObj.setLandmarkId(rs1
							.getString("landmarkId"));
					tripDetailsChildDtoObj.setLandmark(rs1
							.getString("landmark"));
					tripDetailsChildDtoObj.setContactNumber(rs1
							.getString("contactNumber1"));
					tripDetailsChildDtoObj.setDistance(rs1.getFloat("dist"));
					tripDetailsChildDtoObj.setTime(rs1.getString("time"));
					tripDetailsChildDtoObj.setTransportType(rs1
							.getString("transportType"));
					tripSheetChild.add(tripDetailsChildDtoObj);

				}
				// System.out.println("M");
				tripDetailsDtoObj.setTripDetailsChildDtoList(tripSheetChild);
				tripSheet.add(tripDetailsDtoObj);
				rs1.close();
			}
			// System.out.println("IN MODIFY>>>>>>>>>>>>>>ENDSSS");
		} catch (Exception e) {
			System.out.println("Error in getting Modify" + e);
		} finally {
			DbConnect.closeResultSet(rs, rs1);
			DbConnect.closeStatement(st, pst);
			DbConnect.closeConnection(con);
		}

		return tripSheet;
	}
	public ArrayList<TripDetailsDto> getTripSheetModifiedSaved(String tripDate,
			String tripLog, String siteId, String tripTime) {
		ArrayList<TripDetailsDto> tripSheet = null;
		//System.out.println("IN MODIFY>>>>>>>>>>>>>>STARTS");

		DbConnect ob = null;
		Connection con = null;
		PreparedStatement pst = null;
		Statement st = null;
		ResultSet rs = null;
		ResultSet rs1 = null;
		try {
			ob = DbConnect.getInstance();
			con = ob.connectDB();
			TripDetailsDto tripDetailsDtoObj = null;
			TripDetailsChildDto tripDetailsChildDtoObj = null;
			tripSheet = new ArrayList<TripDetailsDto>();
			ArrayList<TripDetailsChildDto> tripSheetChild = null;
			st = con.createStatement();

			String subQuery = "";

			String query = "SELECT t.id,t.trip_code,t.trip_date,t.trip_time,t.trip_log,t.routeid,t.trip_code,vt.type,t.status,t.security_status, p.logTime,t.distance,t.travelTime,t.preTripId,t.distanceFromPreTrip, ifnull(p.approvalStatus,'Open')  approvalStatus, p.vehicleNo   FROM trip_details t left join vendor_trip_sheet_parent p on t.id=p.tripId,vehicle_type vt where t.siteId=? and (t.status=? or t.status=?)and vt.id=t.vehicle_type ";
			if (tripLog.equals("ALL")) {
				subQuery = " and t.trip_date='" + tripDate + "'";

			} else if (tripTime.equals("ALL")) {
				subQuery = " and t.trip_date='" + tripDate
						+ "' and t.trip_log='" + tripLog + "' ";
			} else {
				subQuery = " and t.trip_date='" + tripDate
						+ "' and t.trip_log='" + tripLog
						+ "' and t.trip_time='" + tripTime + "'";
			}
			String lastQuery = " order by t.id ";

			pst = con.prepareStatement(query + subQuery + lastQuery);
			//System.out.println(query + subQuery + lastQuery);
			pst.setString(1, siteId);
			pst.setString(2, "addmod");
			pst.setString(3, "modified");
			rs = pst.executeQuery();
			while (rs.next()) {

				tripDetailsDtoObj = new TripDetailsDto();
				tripDetailsDtoObj.setId(rs.getString("id"));

				tripDetailsDtoObj.setActualLogTime(rs.getString("logTime"));
				tripDetailsDtoObj.setVehicleNo(rs.getString("vehicleNo"));

				tripDetailsDtoObj.setApprovalStatus(rs
						.getString("approvalStatus"));
				tripDetailsDtoObj.setTrip_code(rs.getString("trip_code"));
				tripDetailsDtoObj.setTrip_date(rs.getDate("trip_date")
						.toString());
				tripDetailsDtoObj.setTrip_time(rs.getString("trip_time"));
				tripDetailsDtoObj.setTrip_log(rs.getString("trip_log"));
				tripDetailsDtoObj.setRouteId(rs.getString("routeid"));
				tripDetailsDtoObj.setTrip_code(rs.getString("trip_code"));
				tripDetailsDtoObj.setVehicle_type(rs.getString("type"));
				tripDetailsDtoObj.setDistance(rs.getString("distance"));
				tripDetailsDtoObj.setTravelTime(rs.getString("travelTime"));
				tripDetailsDtoObj
						.setIsSecurity(rs.getString("security_status"));
				// tripSheetChild = null;
				tripSheetChild = new ArrayList<TripDetailsChildDto>();
				query = "select tdc.employeeId,e.displayname as EmployeeName, ifnull(e.contactNumber1,'') contactNumber1,a.area,p.place,l.id as landmarkId,l.landmark, vt.employeeId approvedEmployee,tdc.time,tdc.dist,e.gender,tdc.transportType  from trip_details_child tdc  left  join vendor_trip_sheet vt on tdc.employeeId=vt.employeeId and tdc.tripId=vt.tripId ,employee e ,area a,place p,landmark l where e.id=tdc.employeeId and tdc.tripId="
						+ rs.getString(1)
						+ " and tdc.landmarkId=l.id and l.place=p.id and p.area=a.id order by tdc.routedOrder";
				// System.out.println(query);
				rs1 = st.executeQuery(query);
				while (rs1.next()) {

					tripDetailsChildDtoObj = new TripDetailsChildDto();
					tripDetailsChildDtoObj.setApprovedEmployee(rs1
							.getString("approvedEmployee"));
					if (tripDetailsChildDtoObj.getApprovedEmployee() != null
							&& !tripDetailsChildDtoObj.getApprovedEmployee()
									.equals("")) {
						tripDetailsDtoObj.setCanTravel(true);
					}

					tripDetailsChildDtoObj.setTripId("" + rs.getInt(1));
					tripDetailsChildDtoObj.setEmployeeId(rs1
							.getString("employeeId"));
					tripDetailsChildDtoObj.setEmployeeName(rs1
							.getString("EmployeeName"));
					tripDetailsChildDtoObj.setGender(rs1.getString("gender"));
					tripDetailsChildDtoObj.setArea(rs1.getString("area"));
					tripDetailsChildDtoObj.setPlace(rs1.getString("place"));
					tripDetailsChildDtoObj.setLandmarkId(rs1
							.getString("landmarkId"));
					tripDetailsChildDtoObj.setLandmark(rs1
							.getString("landmark"));
					tripDetailsChildDtoObj.setContactNumber(rs1
							.getString("contactNumber1"));
					tripDetailsChildDtoObj.setDistance(rs1.getFloat("dist"));
					tripDetailsChildDtoObj.setTime(rs1.getString("time"));
					tripDetailsChildDtoObj.setTransportType(rs1
							.getString("transportType"));
					tripSheetChild.add(tripDetailsChildDtoObj);

				}
				// System.out.println("M");
				tripDetailsDtoObj.setTripDetailsChildDtoList(tripSheetChild);
				tripSheet.add(tripDetailsDtoObj);
				rs1.close();
			}
			// System.out.println("IN MODIFY>>>>>>>>>>>>>>ENDSSS");
		} catch (Exception e) {
			System.out.println("Error in getting ModifySaved" + e);
		} finally {
			DbConnect.closeResultSet(rs, rs1);
			DbConnect.closeStatement(st, pst);
			DbConnect.closeConnection(con);
		}

		return tripSheet;
	}


	public TripDetailsDto getTripSheetByTrip(String tripId) throws SQLException {
		DbConnect ob = null;
		Connection con = null;
		ob = DbConnect.getInstance();
		con = ob.connectDB();
		TripDetailsDto tripDetailsDtoObj = new TripDetailsDto();
		TripDetailsChildDto tripDetailsChildDtoObj = null;
		ArrayList<TripDetailsChildDto> tripSheetChild = null;
		PreparedStatement pst = null;
		Statement st = con.createStatement();
		ResultSet rs = null;
		ResultSet rs1 = null;
		try {
			pst = con
					.prepareStatement("SELECT t.id,t.trip_code, t.siteId, t.driverId, t.driverPswd, t.trip_date,t.trip_time,t.trip_log,t.routeid,t.trip_code,vt.type,t.security_status,t.status,t.distance,t.travelTime,t.preTripId,t.distanceFromPreTrip, v.regNo, t.escortId, t.escortPswd,d.name as driverName,d.contact as driverConatct FROM trip_details t left join vehicles v on t.vehicle=v.id left join driver d  on t.driverid=d.id,vehicle_type vt where t.id=? and vt.id=t.vehicle_type");
			System.out
					.println("SELECT t.id,t.trip_code, t.siteId, t.driverId, t.driverPswd, t.trip_date,t.trip_time,t.trip_log,t.routeid,t.trip_code,vt.type,t.security_status,t.status,t.distance,t.travelTime,t.preTripId,t.distanceFromPreTrip, v.regNo, t.escortId, t.escortPswd,d.name as driverName,d.contact as driverConatct FROM trip_details t left join vehicles v on t.vehicle=v.id left join driver d  on t.driverid=d.id,vehicle_type vt where t.id=? and vt.id=t.vehicle_type");
			System.out.println(tripId);
			pst.setString(1, tripId);
			rs = pst.executeQuery();
			if (rs.next()) {

				tripDetailsDtoObj.setId(rs.getString("id"));
				tripDetailsDtoObj.setSiteId(rs.getString("siteId"));
				tripDetailsDtoObj.setTrip_code(rs.getString("trip_code"));
				tripDetailsDtoObj.setTrip_date(rs.getDate("trip_date")
						.toString());
				tripDetailsDtoObj.setTrip_time(rs.getString("trip_time"));
				tripDetailsDtoObj.setTrip_log(rs.getString("trip_log"));
				tripDetailsDtoObj.setRouteId(rs.getString("routeid"));
				tripDetailsDtoObj.setTrip_code(rs.getString("trip_code"));
				tripDetailsDtoObj.setDriverId(rs.getString("driverId"));
				tripDetailsDtoObj.setVehicle_type(rs.getString("type"));
				tripDetailsDtoObj
						.setIsSecurity(rs.getString("security_status"));
				tripDetailsDtoObj.setDistance(rs.getString("distance"));
				tripDetailsDtoObj.setTravelTime(rs.getString("travelTime"));
				tripDetailsDtoObj.setPreviousTripId(rs.getString("preTripId"));
				tripDetailsDtoObj.setDistanceFromPrevioustrip(rs
						.getFloat("distanceFromPreTrip"));
				tripDetailsDtoObj.setDriverPassword(rs.getString("driverPswd"));
				tripDetailsDtoObj.setVehicleNo(rs.getString("regNo"));
				tripDetailsDtoObj.setEscortId(rs.getString("escortId"));
				tripDetailsDtoObj.setEscortPassword(rs.getString("escortPswd"));
				tripDetailsDtoObj.setDriverName(rs.getNString("driverName"));
				tripDetailsDtoObj.setDriverContact(rs
						.getString("driverConatct"));
				// tripSheetChild = null;
				tripSheetChild = new ArrayList<TripDetailsChildDto>();
				rs1 = st.executeQuery("select tdc.tripid,e.gender, tdc.employeeId,tdc.time,e.displayname as EmployeeName,a.area,p.place,l.id as landmarkId,l.landmark,tdc.scheduleId,vts.keyPin ,e.contactnumber1,e.personnelno,e.loginid from  trip_details_child tdc left outer join landmark l on tdc.landmarkId=l.id left outer join place p on l.place=p.id left outer join area a on p.area=a.id left outer join vendor_trip_sheet vts on tdc.tripid=vts.tripid and tdc.employeeid=vts.employeeid, employee e where tdc.tripid="
						+ tripId
						+ " and e.id=tdc.employeeId   order by tdc.routedOrder");

				while (rs1.next()) {

					tripDetailsChildDtoObj = new TripDetailsChildDto();
					tripDetailsChildDtoObj.setTripId("" + rs.getInt(1));
					tripDetailsChildDtoObj.setEmployeeId(rs1
							.getString("employeeId"));
					tripDetailsChildDtoObj.setEmployeeName(rs1
							.getString("EmployeeName"));
					tripDetailsChildDtoObj.setGender(rs1.getString("gender"));
					tripDetailsChildDtoObj.setArea(rs1.getString("area"));
					tripDetailsChildDtoObj.setPlace(rs1.getString("place"));
					tripDetailsChildDtoObj.setLandmarkId(rs1
							.getString("landmarkId"));
					tripDetailsChildDtoObj.setLandmark(rs1
							.getString("landmark"));
					tripDetailsChildDtoObj.setScheduleId(rs1
							.getString("scheduleId"));
					tripDetailsChildDtoObj.setTime(rs1.getString("time"));
					if (SettingsConstant.empAuthinticationForComet
							.equalsIgnoreCase("pin")) {
						tripDetailsChildDtoObj.setKeyPin(rs1
								.getString("keyPin"));
					} else if (SettingsConstant.empAuthinticationForComet
							.equalsIgnoreCase("loginid")) {
						int loginidlength = rs1.getString("loginid").length();
						tripDetailsChildDtoObj.setKeyPin(rs1.getString(
								"loginid").substring(loginidlength - 4));
					} else {
						tripDetailsChildDtoObj.setKeyPin(rs1
								.getString("personnelno"));
					}
					tripDetailsChildDtoObj.setContactNumber(rs1
							.getString("contactnumber1"));
					tripSheetChild.add(tripDetailsChildDtoObj);
				}
				tripDetailsDtoObj.setTripDetailsChildDtoList(tripSheetChild);
			}
		} catch (Exception e12) {
			System.out.println("error" + e12);
		} finally {
			DbConnect.closeResultSet(rs, rs1);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);
		}
		// System.out.println("Completed");
		return tripDetailsDtoObj;
	}

	public ArrayList<TripDetailsDto> getEmployeeTripSheet(String employeeId) {
		DbConnect ob = null;
		Connection con = null;
		PreparedStatement pst = null;
		Statement st = null;
		ResultSet rs = null;
		ResultSet rs1 = null;
		ArrayList<TripDetailsDto> tripDetailsListObj = new ArrayList<TripDetailsDto>();
		try {
			TripDetailsDto tripDetailsDtoObj = null;
			ob = DbConnect.getInstance();
			con = ob.connectDB();
			TripDetailsChildDto tripDetailsChildDtoObj = null;
			ArrayList<TripDetailsChildDto> tripSheetChild = null;
			st = con.createStatement();
			String query = "";
			pst = con
					.prepareStatement("select trip_details.*,vehicle_type.type as type from trip_details,vehicle_type where  ((trip_date=date_add(curdate(),interval 1 day) and trip_log='IN') or(trip_date=curdate() and trip_log='OUT'))  and (trip_details.status=? or trip_details.status=? or trip_details.status=?) and vehicle_type.id=trip_details.vehicle_type and trip_details.id in(select tripId from  trip_details_child where employeeId=?) order by trip_log ");
			pst.setString(1, "saved");
			pst.setString(2, "addsave");
			pst.setString(3, "saveedit");
			pst.setString(4, employeeId);
			rs = pst.executeQuery();
			while (rs.next()) {
				tripDetailsDtoObj = new TripDetailsDto();
				tripDetailsDtoObj.setId(rs.getString("id"));
				tripDetailsDtoObj.setTrip_code(rs.getString("trip_code"));
				tripDetailsDtoObj.setTrip_date(rs.getDate("trip_date")
						.toString());
				tripDetailsDtoObj.setTrip_time(rs.getString("trip_time"));
				tripDetailsDtoObj.setTrip_log(rs.getString("trip_log"));
				tripDetailsDtoObj.setRouteId(rs.getString("routeid"));
				tripDetailsDtoObj.setTrip_code(rs.getString("trip_code"));
				tripDetailsDtoObj.setVehicle_type(rs.getString("type"));
				tripDetailsDtoObj
						.setIsSecurity(rs.getString("security_status"));
				tripSheetChild = new ArrayList<TripDetailsChildDto>();
				query = "select tdc.employeeId,e.displayname as EmployeeName,a.area,p.place,l.id as landmarkId,l.landmark,ifnull(e.contactNumber1,'') as contactno,e.gender from trip_details_child tdc,employee e,area a,place p,landmark l where e.id=tdc.employeeId and tdc.tripId="
						+ rs.getString(1)
						+ " and tdc.landmarkId=l.id and l.place=p.id and p.area=a.id order by tdc.routedOrder";
				rs1 = st.executeQuery(query);
				while (rs1.next()) {
					tripDetailsChildDtoObj = new TripDetailsChildDto();
					tripDetailsChildDtoObj.setTripId("" + rs.getInt(1));
					tripDetailsChildDtoObj.setEmployeeId(rs1
							.getString("employeeId"));
					tripDetailsChildDtoObj.setEmployeeName(rs1
							.getString("EmployeeName"));
					tripDetailsChildDtoObj.setArea(rs1.getString("area"));
					tripDetailsChildDtoObj.setPlace(rs1.getString("place"));
					tripDetailsChildDtoObj.setLandmarkId(rs1
							.getString("landmarkId"));
					tripDetailsChildDtoObj.setLandmark(rs1
							.getString("landmark"));
					tripDetailsChildDtoObj.setContactNumber(rs1
							.getString("contactno"));
					tripSheetChild.add(tripDetailsChildDtoObj);
				}

				tripDetailsDtoObj.setTripDetailsChildDtoList(tripSheetChild);
				tripDetailsListObj.add(tripDetailsDtoObj);
				rs1.close();
			}
		} catch (Exception e) {
			System.out.println("Error in getting saved" + e);
		} finally {
			DbConnect.closeResultSet(rs, rs1);
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);
		}
		return tripDetailsListObj;
	}

	public ArrayList<TripDetailsChildDto> searchEmployee(EmployeeDto employeeDto) {
		ArrayList<TripDetailsChildDto> tripSheetChild = new ArrayList<TripDetailsChildDto>();

		DbConnect ob = DbConnect.getInstance();
		boolean nameFlag = false;
		boolean emailFlag = false;
		boolean idflag = false;
		String query = "SELECT e.id,e.displayname as EmployeeName,e.Gender,a.area,p.place,l.id as landmarkId ,l.landmark  FROM employee e,employee_subscription es,area a,place p,landmark l where e.id=es.empID and (es.subscriptionStatus='subscribed'  or es.subscriptionStatus='pending') and es.landMark=l.id and l.place=p.id and p.area=a.id";
		String subQuery = "";

		if (employeeDto.getEmployeeFirstName() != null
				&& !employeeDto.getEmployeeFirstName().equals("")) {
			subQuery = subQuery + "EmployeeFirstName like ? ";
			nameFlag = true;
		}

		if (employeeDto.getEmployeeID() != null
				&& !employeeDto.getEmployeeID().equals("")) {
			if (!subQuery.equals("")) {
				subQuery = subQuery + " and ";
			}
			subQuery = subQuery + " PersonnelNo =? ";

			idflag = true;

		}

		if (employeeDto.getEmployeeLastName() != null
				&& !employeeDto.getEmployeeLastName().equals("")) {
			if (!subQuery.equals("")) {
				subQuery = subQuery + " and ";
			}
			subQuery = subQuery + " employeeLastName like ? ";

			emailFlag = true;
		}

		if (!subQuery.equals("")) {
			query = query + " and " + subQuery;
		}

		// System.out.println("In getEmployeeDao");
		// System.out.println("Query :" + query);
		Connection con = ob.connectDB();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = con.prepareStatement(query);
			// Statement stmt = con.createStatement();
			int i = 1;
			if (nameFlag) {
				stmt.setString(i, "%" + employeeDto.getEmployeeFirstName()
						+ "%");
				i++;
			}
			if (idflag) {
				stmt.setString(i, employeeDto.getEmployeeID());
				i++;
			}
			if (emailFlag) {
				stmt.setString(i, "%" + employeeDto.getEmployeeLastName() + "%");
				i++;
			}

			// stmt.setString(1, employeeName );
			// stmt.setString(2, employeeName );
			// ResultSet rs = stmt.executeQuery(query);
			rs = stmt.executeQuery();

			while (rs.next()) {
				TripDetailsChildDto tripDetailsChildDtoObj = new TripDetailsChildDto();
				tripDetailsChildDtoObj.setEmployeeId(rs.getString("id"));
				tripDetailsChildDtoObj.setEmployeeName(rs
						.getString("EmployeeName"));
				tripDetailsChildDtoObj.setArea(rs.getString("area"));
				tripDetailsChildDtoObj.setPlace(rs.getString("place"));
				tripDetailsChildDtoObj
						.setLandmarkId(rs.getString("landmarkId"));
				tripDetailsChildDtoObj.setLandmark(rs.getString("landmark"));
				tripSheetChild.add(tripDetailsChildDtoObj);
			}

		} catch (Exception e) {

			System.out.println("Exception in EmployeeDao SearchEmployee " + e);

		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(stmt);
			DbConnect.closeConnection(con);
		}
		return tripSheetChild;
	}

	public ArrayList<TripDetailsChildDto> getTripSheetRemoved(String tripDate,
			String siteId, String tripLog, String tripTime) {
		DbConnect ob = null;
		Connection con1 = null;
		ob = DbConnect.getInstance();
		con1 = ob.connectDB();
		TripDetailsChildDto tripDetailsChildDtoObj = null;
		ArrayList<TripDetailsChildDto> tripSheetChild = new ArrayList<TripDetailsChildDto>();
		PreparedStatement pst = null;
		ResultSet rs = null;
		String query = "select tdr.employeeid as employeeId,e.displayname as EmployeeName,a.area,p.place,l.id as landmarkId,l.landmark,tdr.scheduleId from trip_details_removed tdr, employee e,area a,place p,landmark l   where tdr.trip_date=? and tdr.siteid=? and tdr.trip_time=? and tdr.in_out=? and e.id=tdr.employeeid  and tdr.landmarkId=l.id and l.place=p.id and p.area=a.id";
		try {
			pst = con1.prepareStatement(query);
			pst.setString(1, tripDate);
			pst.setString(2, siteId);
			pst.setString(3, tripTime);
			pst.setString(4, tripLog);
			rs = pst.executeQuery();
			while (rs.next()) {
				tripDetailsChildDtoObj = new TripDetailsChildDto();
				tripDetailsChildDtoObj
						.setEmployeeId(rs.getString("employeeId"));
				tripDetailsChildDtoObj.setEmployeeName(rs
						.getString("EmployeeName"));
				tripDetailsChildDtoObj.setArea(rs.getString("area"));
				tripDetailsChildDtoObj.setPlace(rs.getString("place"));
				tripDetailsChildDtoObj
						.setLandmarkId(rs.getString("landmarkId"));
				tripDetailsChildDtoObj.setLandmark(rs.getString("landmark"));
				tripDetailsChildDtoObj
						.setScheduleId(rs.getString("scheduleId"));
				tripSheetChild.add(tripDetailsChildDtoObj);
			}
		} catch (SQLException ex) {
			System.out.println("ERR" + ex);
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con1);
		}
		return tripSheetChild;
	}

	public int trackTripByVendor(ArrayList<TripDetailsChildDto> dtoList) {

		Connection con1 = null;
		DbConnect ob = DbConnect.getInstance();
		Statement st = null;
		Statement st1 = null;
		ResultSet rs = null;
		int flag = 0;
		con1 = ob.connectDB();
		try {
			int index = 0;
			for (TripDetailsChildDto dto : dtoList) {
				index++;
				st = con1.createStatement();
				st1 = con1.createStatement();
				String updateQuery = "update vendor_trip_sheet set showStatus='"
						+ TripDetailsConstant.SHOW
						+ "' where tripId="
						+ dto.getTripId()
						+ " and employeeId="
						+ dto.getEmployeeId();

				String insertQuery = "insert into vendor_trip_sheet (tripId,employeeId, insertedOrder ) values ("
						+ dto.getTripId()
						+ ", "
						+ dto.getEmployeeId()
						+ ", "
						+ index + " )";
				String checkQuery = " select * from vendor_trip_sheet where tripId="
						+ dto.getTripId()
						+ " and employeeId="
						+ dto.getEmployeeId();
				rs = st.executeQuery(checkQuery);
				if (rs.next()) {
					st1.executeUpdate(updateQuery);
					flag++;
				} else {
					flag += st1.executeUpdate(insertQuery);
				}
			}
		} catch (Exception e) {
			System.out.println("Exception in approveTripByVendor : " + e);
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st, st1);
			DbConnect.closeConnection(con1);

		}
		return flag;
	}

	public int unCheckByVendor(ArrayList<TripDetailsChildDto> dtoList) {

		Connection con1 = null;
		DbConnect ob = DbConnect.getInstance();
		Statement st = null;
		ResultSet rs = null;
		int flag = 0;
		con1 = ob.connectDB();
		try {

			con1.setAutoCommit(false);
			con1.rollback();
			st = con1.createStatement();

			for (TripDetailsChildDto dto : dtoList) {
				// String insertQuery =
				// "insert into vendor_trip_sheet (tripId,employeeId) values ("
				// + dto.getTripId() + ", " + dto.getEmployeeId() + ")";
				String checkQuery = " select * from vendor_trip_sheet where tripId="
						+ dto.getTripId()
						+ " and employeeId="
						+ dto.getEmployeeId();
				String noShowQuery = " update vendor_trip_sheet set showStatus='"
						+ TripDetailsConstant.NO_SHOW
						+ "', noShowReason='"
						+ dto.getReason()
						+ "' where tripId="
						+ dto.getTripId()
						+ " and employeeId=" + dto.getEmployeeId();
				String insertQuery = "insert into vendor_trip_sheet (tripId,employeeId,showStatus,noShowReason ) values ("
						+ dto.getTripId()
						+ ", "
						+ dto.getEmployeeId()
						+ ", '"
						+ TripDetailsConstant.NO_SHOW
						+ "','"
						+ dto.getReason()
						+ "' )";
				rs = st.executeQuery(checkQuery);
				if (rs.next()) {
					// System.out.println(" uncheck: " + noShowQuery);
					flag += st.executeUpdate(noShowQuery);
				} else {

					flag += st.executeUpdate(insertQuery);

				}
				DbConnect.closeResultSet(rs);
			}
			// System.out.println("Count : " + flag);
			if (flag >= dtoList.size()) {
				con1.commit();
				// System.out.println("Commited");
			} else {
				con1.rollback();
				flag = 0;
			}
		} catch (Exception e) {
			try {
				con1.rollback();

			} catch (Exception e1) {
				;
			}
			flag = 0;
			System.out.println("Exception in approveTripByVendor : " + e);
		} finally {
			try {
				con1.setAutoCommit(true);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con1);

		}

		return flag;
	}

	public int updateVendorTrip(HashMap<String, TripDetailsDto> trips,
			String user) {

		Connection con1 = null;
		DbConnect ob = DbConnect.getInstance();
		PreparedStatement insertSt = null;
		PreparedStatement checkSt = null;
		PreparedStatement updateSt = null;
		String checkQuery = "select * from vendor_trip_sheet_parent where tripId=?";
		String updateQuery = "update vendor_trip_sheet_parent set logTime=?, vehicleNo=?, updatedBy=?, onTimeStatus=?, escort=?, escortClock=?,driverContact=?,manualDistance=?  where tripId=? ";
		String insertQuery = " insert into vendor_trip_sheet_parent (tripId, logTime,insertedBy,updatedBy, vehicleNo, onTimeStatus, escort, escortClock, driverContact, manualDistance) values (?, ?, ?, ?, ?, ?,?,?,?, ?)";
		int changedBy = 0, autoincNumber = 0;

		ResultSet rs = null;
		int flag = 0;
		con1 = ob.connectDB();
		try {
			insertSt = con1.prepareStatement(insertQuery);
			updateSt = con1.prepareStatement(updateQuery);
			checkSt = con1.prepareStatement(checkQuery);
			changedBy = Integer.parseInt(user);

			for (Iterator i = trips.entrySet().iterator(); i.hasNext();) {
				java.util.Map.Entry entry = (java.util.Map.Entry) i.next();
				TripDetailsDto tripD = (TripDetailsDto) entry.getValue();
				insertSt.setString(1, (String) entry.getKey());
				insertSt.setString(2, tripD.getActualLogTime());
				insertSt.setString(3, user);
				insertSt.setString(4, user);
				insertSt.setString(5, tripD.getVehicleNo());
				insertSt.setString(6, tripD.getOnTimeStatus());
				insertSt.setString(7, tripD.getEscort());
				System.out.println("ESCORTTTTTTTTTTTTTTTTInsert"
						+ tripD.getEscortNo());
				System.out.println("ESCORTTTnameTTTTTTTTTInsert"
						+ tripD.getEscort());

				insertSt.setString(8, tripD.getEscortNo());
				insertSt.setString(9, tripD.getDriverContact());
				tripD.setTripBasedDistance(OtherFunctions.isEmpty(tripD
						.getTripBasedDistance()) ? "0" : tripD
						.getTripBasedDistance());
				insertSt.setString(10, tripD.getTripBasedDistance());

				updateSt.setString(1, tripD.getActualLogTime());
				updateSt.setString(2, tripD.getVehicleNo());
				updateSt.setString(3, user);
				updateSt.setString(4, tripD.getOnTimeStatus());
				updateSt.setString(5, tripD.getEscort());
				updateSt.setString(6, tripD.getEscortNo());
				System.out.println("ESCORTTTTTTTTTTTTTTTTUpDate"
						+ tripD.getEscortNo());
				System.out.println("ESCORTTTnameTTTTTTTTTUpDate"
						+ tripD.getEscort());
				updateSt.setString(7, tripD.getDriverContact());
				updateSt.setString(8, tripD.getTripBasedDistance());
				updateSt.setString(9, (String) entry.getKey());

				checkSt.setString(1, (String) entry.getKey());
				String trid = (String) entry.getKey();
				rs = checkSt.executeQuery();
				if (rs.next()) {
					// System.out.println("update : "+ updateQuery);
					int updret = updateSt.executeUpdate();
					if (updret == 1) {
						autoincNumber = Integer.parseInt(trid);
						auditLogEntryforinsertvendortrip(
								autoincNumber,
								AuditLogConstants.VENDOR_TRACKING_MODULE,
								changedBy,
								AuditLogConstants.WORKFLOW_STATE_VENDOR_APPROVED,
								AuditLogConstants.WORKFLOW_STATE_VENDOR_APPROVED,
								AuditLogConstants.AUDIT_LOG_EDIT_ACTION);

					}
					flag++;
				} else {
					// System.out.println("insert : "+ insertQuery);
					flag += insertSt.executeUpdate();
					if (flag >= 1) {
						autoincNumber = Integer.parseInt(trid);
						auditLogEntryforinsertvendortrip(
								autoincNumber,
								AuditLogConstants.VENDOR_TRACKING_MODULE,
								changedBy,
								AuditLogConstants.WORKFLOW_STATE_EMPTY,
								AuditLogConstants.WORKFLOW_STATE_VENDOR_APPROVED,
								AuditLogConstants.AUDIT_LOG_CREATE_ACTION);
					}

				}
				DbConnect.closeResultSet(rs);

			}

		} catch (Exception e) {
			System.out.println("Exception in updateVendorTrip : " + e);
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(checkSt, updateSt, insertSt);
			DbConnect.closeConnection(con1);

		}

		return flag;
	}

	private void auditLogEntryforinsertvendortrip(int autoincNumber,
			String Module, int changedBy, String preworkflowState,
			String curworkflowState, String auditLogAction) {
		AuditLogDTO auditLog = new AuditLogDTO();

		auditLog.setRelatedNodeId(autoincNumber);
		auditLog.setModuleName(Module);
		auditLog.setChangedBy(changedBy);
		auditLog.setDateChanged(new Date());
		auditLog.setPreviousState(preworkflowState);
		auditLog.setCurrentState(curworkflowState);
		auditLog.setAction(auditLogAction);

		AuditLogDAO auditDAO = new AuditLogDAO();
		auditDAO.addAuditLogEntry(auditLog);

	}

	/*---------*/

	public ArrayList<TripDetailsDto> getTripSheetSaved_Vendor(String tripDate,
			String tripLog, String siteId, String tripTime[],
			String personnelNo, String vendor) {

		ArrayList<TripDetailsDto> tripSheet = null;
		DbConnect ob = null;
		Connection con1 = null;
		PreparedStatement pst = null;
		Statement st = null;
		ResultSet rs = null;
		ResultSet rs1 = null;
		try {
			ob = DbConnect.getInstance();
			con1 = ob.connectDB();
			TripDetailsDto tripDetailsDtoObj = null;
			TripDetailsChildDto tripDetailsChildDtoObj = null;
			tripSheet = new ArrayList<TripDetailsDto>();
			ArrayList<TripDetailsChildDto> tripSheetChild = null;
			st = con1.createStatement();
			String query = "";
			String employeeSearchQuery = "";
			String vendorSearchQuery = "";
			if (personnelNo != null && !personnelNo.equals("")) {
				// employeeSearchQuery=" and t.id in (select c.tripId from trip_details_child c join employee e on c.employeeId=e.id where e.PersonnelNo="
				// + personnelNo + ")";
				// and (t.id in (select c.tripId from trip_details_child c join
				// employee e on c.employeeId=e.id where e.PersonnelNo='" +
				// personnelNo +
				// "'  and c.status ='saved' ) or t.id in (select m.tripId from trip_details_modified m join employee e on m.empid=e.id where e.PersonnelNo='"
				// + personnelNo + "' and m.status in ('saveedit','addsave')) )
				employeeSearchQuery = " and (t.id in (select c.tripId from trip_details_child c join employee e on c.employeeId=e.id where e.PersonnelNo=?  and t.status ='saved' ) or t.id in (select m.tripId from trip_details_modified m join employee e on m.empid=e.id where e.PersonnelNo=? and t.status in ('saveedit','addsave')) ) ";
			}
			if (vendor != null && !vendor.equals("")) {
				vendorSearchQuery = "  and t.id in (select tripId from vendor_trip_sheet_parent vp where vp.insertedBy in (select vdr.id from vendor vdr where vdr.name like ?) or vp.updatedBy in (select vdr.id from vendor vdr where vdr.name like ?))";
			}

			int step = 0;
			if (tripLog.equals("ALL")) {
				pst = con1
						.prepareStatement("SELECT t.id,t.trip_code,t.trip_date,t.trip_time,t.trip_log,t.routeid,t.trip_code,vt.type,t.status,t.security_status, p.logTime, p.approvalStatus, p.vehicleNo, p.onTimeStatus, p.comment, p.escort, p.escortClock     FROM trip_details t left join vendor_trip_sheet_parent p on t.id=p.tripId ,vehicle_type vt where t.siteId=? and (t.status=? or t.status=? or t.status=?)and vt.id=t.vehicle_type and t.trip_date=? "
								+ employeeSearchQuery
								+ vendorSearchQuery
								+ "  order by t.id");
				step = 0;

				pst.setString(step + 1, siteId);
				pst.setString(step + 2, "saved");
				pst.setString(step + 3, "addsave");
				pst.setString(step + 4, "saveedit");
				pst.setString(step + 5, tripDate);
				step = 5;
				if (personnelNo != null && !personnelNo.equals("")) {
					pst.setString(++step, personnelNo);
					pst.setString(++step, personnelNo);
				}
				if (vendor != null && !vendor.equals("")) {
					pst.setString(++step, "%" + vendor + "%");
					pst.setString(++step, "%" + vendor + "%");
				}

			} else if (tripTime.equals("ALL")) {
				pst = con1
						.prepareStatement("SELECT t.id,t.trip_code,t.trip_date,t.trip_time,t.trip_log,t.routeid,t.trip_code,vt.type,t.status,t.security_status,  p.logTime, p.approvalStatus, p.vehicleNo, p.onTimeStatus, p.comment, p.escort, p.escortClock     FROM trip_details t left join vendor_trip_sheet_parent p on t.id=p.tripId  ,vehicle_type vt where t.siteId=? and (t.status=? or t.status=? or t.status=?)and vt.id=t.vehicle_type and t.trip_date=? and t.trip_log=? "
								+ employeeSearchQuery
								+ vendorSearchQuery
								+ "  order by t.id");
				step = 0;

				pst.setString(step + 1, siteId);
				pst.setString(step + 2, "saved");
				pst.setString(step + 3, "addsave");
				pst.setString(step + 4, "saveedit");
				pst.setString(step + 5, tripDate);
				pst.setString(step + 6, tripLog);
				step = 6;
				if (personnelNo != null && !personnelNo.equals("")) {
					pst.setString(++step, personnelNo);
					pst.setString(++step, personnelNo);
				}
				if (vendor != null && !vendor.equals("")) {
					pst.setString(++step, "%" + vendor + "%");
					pst.setString(++step, "%" + vendor + "%");
				}
			} else {

				String tripTimeQuery = "";
				for (String ttime : tripTime) {
					tripTimeQuery += "," + ttime;
				}
				tripTimeQuery.replaceFirst(",", "");
				tripTimeQuery = "(" + tripTimeQuery + ") ";
				pst = con1
						.prepareStatement("SELECT t.id,t.trip_code,t.trip_date,t.trip_time,t.trip_log,t.routeid,t.trip_code,vt.type,t.status,t.security_status,  p.logTime, p.approvalStatus, p.vehicleNo, p.onTimeStatus, p.comment, p.escort, p.escortClock     FROM trip_details t left join vendor_trip_sheet_parent p on t.id=p.tripId  ,vehicle_type vt where t.siteId=? and (t.status=? or t.status=? or t.status=?)and vt.id=t.vehicle_type and t.trip_date=? and t.trip_log=? and t.trip_time in "
								+ tripTimeQuery
								+ employeeSearchQuery
								+ vendorSearchQuery + "  order by t.id");
				step = 0;

				pst.setString(step + 1, siteId);
				pst.setString(step + 2, "saved");
				pst.setString(step + 3, "addsave");
				pst.setString(step + 4, "saveedit");
				pst.setString(step + 5, tripDate);
				pst.setString(step + 6, tripLog);

				step = 6;
				if (personnelNo != null && !personnelNo.equals("")) {
					pst.setString(++step, personnelNo);
					pst.setString(++step, personnelNo);
				}
				if (vendor != null && !vendor.equals("")) {
					pst.setString(++step, "%" + vendor + "%");
					pst.setString(++step, "%" + vendor + "%");
				}
			}
			rs = pst.executeQuery();
			while (rs.next()) {
				tripDetailsDtoObj = new TripDetailsDto();
				tripDetailsDtoObj.setId(rs.getString("id"));
				tripDetailsDtoObj.setActualLogTime(rs.getString("logTime"));
				tripDetailsDtoObj.setVehicleNo(rs.getString("vehicleNo"));
				tripDetailsDtoObj.setApprovalStatus(rs
						.getString("approvalStatus"));
				tripDetailsDtoObj.setOnTimeStatus(rs.getString("onTimeStatus"));
				tripDetailsDtoObj.setComment(rs.getString("comment"));

				tripDetailsDtoObj.setTrip_code(rs.getString("trip_code"));
				tripDetailsDtoObj.setTrip_date(rs.getDate("trip_date")
						.toString());
				tripDetailsDtoObj.setTrip_time(rs.getString("trip_time"));
				tripDetailsDtoObj.setTrip_log(rs.getString("trip_log"));
				tripDetailsDtoObj.setRouteId(rs.getString("routeid"));
				tripDetailsDtoObj.setTrip_code(rs.getString("trip_code"));
				tripDetailsDtoObj.setVehicle_type(rs.getString("type"));
				tripDetailsDtoObj
						.setIsSecurity(rs.getString("security_status"));
				// tripSheetChild = null;
				tripSheetChild = new ArrayList<TripDetailsChildDto>();
				String status = rs.getString("status");
				query = "select tdc.employeeId,e.displayname as EmployeeName,a.area,p.place,l.id as landmarkId,l.landmark,e.contactNumber1 as contactno, vt.employeeId approvedEmployee, vt.showStatus,vt.noShowReason,e.gender  from trip_details_child tdc  left  join vendor_trip_sheet vt on tdc.employeeId=vt.employeeId and tdc.tripId=vt.tripId left outer join landmark l on  tdc.landmarkid=l.id left outer join place p on  l.place=p.id left outer join area a on  p.area=a.id,employee e  where e.id=tdc.employeeId and tdc.tripId="
						+ rs.getString(1) + " order by tdc.routedOrder";
				rs1 = st.executeQuery(query);
				while (rs1.next()) {

					tripDetailsChildDtoObj = new TripDetailsChildDto();
					tripDetailsChildDtoObj.setApprovedEmployee(rs1
							.getString("approvedEmployee"));
					tripDetailsChildDtoObj.setShowStatus(rs1
							.getString("showStatus"));
					tripDetailsChildDtoObj.setReason(rs1
							.getString("noShowReason"));
					if (tripDetailsChildDtoObj.getApprovedEmployee() != null
							&& !tripDetailsChildDtoObj.getApprovedEmployee()
									.equals("")) {
						tripDetailsDtoObj.setCanTravel(true);
					}

					tripDetailsChildDtoObj.setTripId("" + rs.getInt(1));
					tripDetailsChildDtoObj.setEmployeeId(rs1
							.getString("employeeId"));
					tripDetailsChildDtoObj.setEmployeeName(rs1
							.getString("EmployeeName"));
					tripDetailsChildDtoObj.setArea(rs1.getString("area"));
					tripDetailsChildDtoObj.setPlace(rs1.getString("place"));
					tripDetailsChildDtoObj.setLandmarkId(rs1
							.getString("landmarkId"));
					tripDetailsChildDtoObj.setLandmark(rs1
							.getString("landmark"));
					tripDetailsChildDtoObj.setContactNumber(rs1
							.getString("contactno"));
					tripSheetChild.add(tripDetailsChildDtoObj);
				}

				tripDetailsDtoObj.setTripDetailsChildDtoList(tripSheetChild);
				tripSheet.add(tripDetailsDtoObj);
				rs1.close();
			}
		} catch (Exception e) {
			System.out.println("Error in getting saved" + e);
		} finally {
			DbConnect.closeResultSet(rs, rs1);
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con1);
		}
		return tripSheet;
	}

	public ArrayList<TripDetailsDto> getTripSheetModified_Vendor(
			String tripDate, String tripLog, String siteId, String tripTime,
			String personnelNo, String vendor) {
		ArrayList<TripDetailsDto> tripSheet = null;
		// System.out.println("IN MODIFY>>>>>>>>>>>>>>STARTS");

		DbConnect ob = null;
		Connection con1 = null;
		ob = DbConnect.getInstance();
		con1 = ob.connectDB();
		TripDetailsDto tripDetailsDtoObj = null;
		TripDetailsChildDto tripDetailsChildDtoObj = null;
		tripSheet = new ArrayList<TripDetailsDto>();
		ArrayList<TripDetailsChildDto> tripSheetChild = null;
		PreparedStatement pst = null;
		Statement st = null;
		ResultSet rs = null;
		ResultSet rs1 = null;
		String query = "";
		String employeeSearchQuery = "";
		String vendorSearchQuery = "";
		try {
			st = con1.createStatement();
			if (personnelNo != null && !personnelNo.equals("")) {
				// employeeSearchQuery=" and t.id in (select c.tripId from trip_details_child c join employee e on c.employeeId=e.id where e.PersonnelNo="
				// + personnelNo + ")";
				employeeSearchQuery = " and (t.id in (select c.tripId from trip_details_child c join employee e on c.employeeId=e.id where e.PersonnelNo=?  and t.status ='routed' ) or t.id in (select m.tripId from trip_details_modified m join employee e on m.empid=e.id where e.PersonnelNo=? and t.status in ('modified','addmod')) ) ";
			}
			if (vendor != null && !vendor.equals("")) {
				vendorSearchQuery = "  and t.id in (select tripId from vendor_trip_sheet_parent vp where vp.insertedBy in (select vdr.id from vendor vdr where vdr.name like ?) or vp.updatedBy in (select vdr.id from vendor vdr where vdr.name like ?)) ";
			}

			int step = 0;
			if (tripLog.equals("ALL")) {
				pst = con1
						.prepareStatement("SELECT t.id,t.trip_code,t.trip_date,t.trip_time,t.trip_log,t.routeid,t.trip_code,vt.type,t.status,t.security_status, p.logTime, p.approvalStatus, p.vehicleNo   FROM trip_details t left join vendor_trip_sheet_parent p on t.id=p.tripId,vehicle_type vt where t.siteId=? and (t.status=? or t.status=? or t.status=?)and vt.id=t.vehicle_type and t.trip_date=? "
								+ employeeSearchQuery
								+ vendorSearchQuery
								+ " order by t.id");
				step = 0;

				pst.setString(step + 1, siteId);
				pst.setString(step + 2, "routed");
				pst.setString(step + 3, "addmod");
				pst.setString(step + 4, "modified");
				pst.setString(step + 5, tripDate);
				step = step + 5;
				if (personnelNo != null && !personnelNo.equals("")) {
					pst.setString(++step, personnelNo);
					pst.setString(++step, personnelNo);
				}
				if (vendor != null && !vendor.equals("")) {
					pst.setString(++step, "%" + vendor + "%");
					pst.setString(++step, "%" + vendor + "%");
				}

			} else if (tripTime.equals("ALL")) {
				pst = con1
						.prepareStatement("SELECT t.id,t.trip_code,t.trip_date,t.trip_time,t.trip_log,t.routeid,t.trip_code,vt.type,t.status,t.security_status, time_format(p.logTime, '%H:%i') logTime , p.approvalStatus, p.vehicleNo   FROM trip_details t left join vendor_trip_sheet_parent p on t.id=p.tripId,vehicle_type vt where t.siteId=? and (t.status=? or t.status=? or t.status=?)and vt.id=t.vehicle_type and t.trip_date=? and t.trip_log=? "
								+ employeeSearchQuery
								+ vendorSearchQuery
								+ "  order by t.id");
				step = 0;
				pst.setString(step + 1, siteId);
				pst.setString(step + 2, "routed");
				pst.setString(step + 3, "addmod");
				pst.setString(step + 4, "modified");
				pst.setString(step + 5, tripDate);
				pst.setString(step + 6, tripLog);
				step = 6;
				if (personnelNo != null && !personnelNo.equals("")) {
					pst.setString(++step, personnelNo);
					pst.setString(++step, personnelNo);
				}
				if (vendor != null && !vendor.equals("")) {
					pst.setString(++step, "%" + vendor + "%");
					pst.setString(++step, "%" + vendor + "%");
				}

			} else {

				pst = con1
						.prepareStatement("SELECT t.id,t.trip_code,t.trip_date,t.trip_time,t.trip_log,t.routeid,t.trip_code,vt.type,t.status,t.security_status,  time_format(p.logTime, '%H:%i') logTime , p.approvalStatus, p.vehicleNo   FROM trip_details t left join vendor_trip_sheet_parent p on t.id=p.tripId,vehicle_type vt where t.siteId=? and (t.status=? or t.status=? or t.status=?) and vt.id=t.vehicle_type and t.trip_date=? and t.trip_log=? and t.trip_time=? "
								+ employeeSearchQuery
								+ vendorSearchQuery
								+ "  order by t.id");
				step = 0;

				pst.setString(step + 1, siteId);
				pst.setString(step + 2, "routed");
				pst.setString(step + 3, "addmod");
				pst.setString(step + 4, "modified");
				pst.setString(step + 5, tripDate);
				pst.setString(step + 6, tripLog);
				pst.setString(step + 7, tripTime);
				step = 7;
				if (personnelNo != null && !personnelNo.equals("")) {
					pst.setString(++step, personnelNo);
					pst.setString(++step, personnelNo);
				}
				if (vendor != null && !vendor.equals("")) {
					pst.setString(++step, "%" + vendor + "%");
					pst.setString(++step, "%" + vendor + "%");
				}

			}
			rs = pst.executeQuery();
			while (rs.next()) {
				tripDetailsDtoObj = new TripDetailsDto();
				tripDetailsDtoObj.setId(rs.getString("id"));
				tripDetailsDtoObj.setActualLogTime(rs.getString("logTime"));
				tripDetailsDtoObj.setVehicleNo(rs.getString("vehicleNo"));
				tripDetailsDtoObj.setApprovalStatus(rs
						.getString("approvalStatus"));
				// System.out.println(" log tim e: " + rs.getString("logTime"));
				tripDetailsDtoObj.setTrip_code(rs.getString("trip_code"));
				tripDetailsDtoObj.setTrip_date(rs.getDate("trip_date")
						.toString());
				tripDetailsDtoObj.setTrip_time(rs.getString("trip_time"));
				tripDetailsDtoObj.setTrip_log(rs.getString("trip_log"));
				tripDetailsDtoObj.setRouteId(rs.getString("routeid"));
				tripDetailsDtoObj.setTrip_code(rs.getString("trip_code"));
				tripDetailsDtoObj.setVehicle_type(rs.getString("type"));
				tripDetailsDtoObj
						.setIsSecurity(rs.getString("security_status"));
				// tripSheetChild = null;
				tripSheetChild = new ArrayList<TripDetailsChildDto>();
				String status = rs.getString("status");
				query = "select tdc.employeeId,e.displayname as EmployeeName,a.area,p.place,l.id as landmarkId,l.landmark,e.contactNumber1 as contactno, vt.employeeId approvedEmployee, vt.showStatus,e.gender  from trip_details_child tdc  left  join vendor_trip_sheet vt on tdc.employeeId=vt.employeeId and tdc.tripId=vt.tripId left outer join landmark l on  tdc.landmarkid=l.id left outer join place p on  l.place=p.id left outer join area a on  p.area=a.id,employee e  where e.id=tdc.employeeId and tdc.tripId="
						+ rs.getString(1) + " order by tdc.routedOrder";
				rs1 = st.executeQuery(query);
				while (rs1.next()) {

					tripDetailsChildDtoObj = new TripDetailsChildDto();
					tripDetailsChildDtoObj.setApprovedEmployee(rs1
							.getString("approvedEmployee"));
					if (tripDetailsChildDtoObj.getApprovedEmployee() != null
							&& !tripDetailsChildDtoObj.getApprovedEmployee()
									.equals("")) {
						tripDetailsDtoObj.setCanTravel(true);
					}

					tripDetailsChildDtoObj.setTripId("" + rs.getInt(1));
					tripDetailsChildDtoObj.setEmployeeId(rs1
							.getString("employeeId"));
					tripDetailsChildDtoObj.setEmployeeName(rs1
							.getString("employeeName"));
					tripDetailsChildDtoObj.setArea(rs1.getString("area"));
					tripDetailsChildDtoObj.setPlace(rs1.getString("place"));
					tripDetailsChildDtoObj.setLandmarkId(rs1
							.getString("landmarkId"));
					tripDetailsChildDtoObj.setLandmark(rs1
							.getString("landmark"));
					tripDetailsChildDtoObj.setContactNumber(rs1
							.getString("contactNumber1"));
					tripSheetChild.add(tripDetailsChildDtoObj);
				}
				// System.out.println("M");
				tripDetailsDtoObj.setTripDetailsChildDtoList(tripSheetChild);
				tripSheet.add(tripDetailsDtoObj);
				rs1.close();
			}
		} catch (Exception e) {
			System.out.println("Error in modfied" + e);
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con1);
		}

		return tripSheet;
	}

	public int setApproved(HashMap<String, TripDetailsDto> trips, String user) {

		Connection con1 = null;
		DbConnect ob = DbConnect.getInstance();
		Statement st = null;
		ResultSet rs = null;
		int changedBy = 0, autoincNumber = 0;
		int flag = 0;
		con1 = ob.connectDB();
		try {
			changedBy = Integer.parseInt(user);
			st = con1.createStatement();
			for (Iterator i = trips.entrySet().iterator(); i.hasNext();) {
				java.util.Map.Entry entry = (java.util.Map.Entry) i.next();

				String tripID = (String) entry.getKey();

				String updateQuery = "update vendor_trip_sheet_parent set approvalStatus='Sent for TC approval', updatedBy="
						+ user + " where tripId=" + tripID + " ";
				autoincNumber = Integer.parseInt(tripID);
				flag += st.executeUpdate(updateQuery);
				if (flag >= 1) {
					auditLogEntryforsetapproved(
							autoincNumber,
							AuditLogConstants.VENDOR_TRACKING_MODULE,
							changedBy,
							AuditLogConstants.WORKFLOW_STATE_VENDOR_APPROVED,
							AuditLogConstants.WORKFLOW_STATE_VENDOR_SEND_FOR_APPROVAL,
							AuditLogConstants.AUDIT_LOG_EDIT_ACTION);
					auditLogEntryforsetapproved(
							autoincNumber,
							AuditLogConstants.ADMIN_TRACKING_MODULE,
							changedBy,
							AuditLogConstants.WORKFLOW_STATE_VENDOR_SEND_FOR_APPROVAL,
							AuditLogConstants.WORKFLOW_STATE_VENDOR_SEND_FOR_APPROVAL,
							AuditLogConstants.AUDIT_LOG_CREATE_ACTION);
				}

			}

		} catch (Exception e) {
			System.out.println("Exception in setApprove : " + e);
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con1);

		}

		return flag;

	}

	private void auditLogEntryforsetapproved(int autoincNumber, String Module,
			int changedBy, String preworkflowState, String curworkflowState,
			String auditLogAction) {
		AuditLogDTO auditLog = new AuditLogDTO();

		auditLog.setRelatedNodeId(autoincNumber);
		auditLog.setModuleName(Module);
		auditLog.setChangedBy(changedBy);
		auditLog.setDateChanged(new Date());
		auditLog.setPreviousState(preworkflowState);
		auditLog.setCurrentState(curworkflowState);
		auditLog.setAction(auditLogAction);

		AuditLogDAO auditDAO = new AuditLogDAO();
		auditDAO.addAuditLogEntry(auditLog);

	}

	public int disapproveTripsHavingNoEmployee(
			HashMap<String, TripDetailsDto> trips, String user) {

		Connection con1 = null;
		DbConnect ob = DbConnect.getInstance();
		Statement st = null;
		ResultSet rs = null;
		int flag = 0;
		con1 = ob.connectDB();
		try {
			st = con1.createStatement();

			String updateQuery = "update  vendor_trip_sheet_parent  set approvalStatus=null where tripId not in (select tripId from vendor_trip_sheet)";

			flag = st.executeUpdate(updateQuery);

		} catch (Exception e) {
			System.out.println("Exception in setApprove : " + e);
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con1);

		}

		return flag;

	}

	public int transAdmin_ApproveTrackedTrips(String[] tripIds, String status,
			String[] comments, String doneBy) {

		Connection con1 = null;
		DbConnect ob = DbConnect.getInstance();
		PreparedStatement st = null;
		ResultSet rs = null;
		int flag = 0;
		con1 = ob.connectDB();
		int changedBy = 0, autoincNumber = 0;
		try {
			String query = "update  vendor_trip_sheet_parent set approvalStatus=?, comment=? where tripId=?";
			st = con1.prepareStatement(query);
			changedBy = Integer.parseInt(doneBy);
			for (int i = 0; i < tripIds.length; i++) {
				TripCommentDto commentDto = null;
				if (comments[i] == null || comments[i].trim().equals("")) {
					comments[i] = "";
				} else {
					commentDto = new TripCommentDto();
					commentDto.setTripId(tripIds[i]);
					commentDto.setCommentedById(doneBy);
					commentDto.setComment(comments[i].trim());

				}
				st.setString(1, status);
				st.setString(2, comments[i]);
				st.setString(3, tripIds[i]);
				autoincNumber = Integer.parseInt(tripIds[i]);
				int subflag = st.executeUpdate();

				int subflag1 = addComment(commentDto);
				flag = flag + (subflag > 0 || subflag1 > 0 ? 1 : 0);
				if (subflag > 0 || subflag1 > 0) {

					if (status.equals("Approved by Transport Manager")) {
						auditLogEntryforadminapproval(
								autoincNumber,
								AuditLogConstants.ADMIN_TRACKING_MODULE,
								changedBy,
								AuditLogConstants.WORKFLOW_STATE_VENDOR_SEND_FOR_APPROVAL,
								AuditLogConstants.WORKFLOW_STATE_TRANSADMIN_APPROVED,
								AuditLogConstants.AUDIT_LOG_EDIT_ACTION);
					} else if (status.equals("Rejected by Transport Manager")) {
						auditLogEntryforadminapproval(
								autoincNumber,
								AuditLogConstants.ADMIN_TRACKING_MODULE,
								changedBy,
								AuditLogConstants.WORKFLOW_STATE_VENDOR_SEND_FOR_APPROVAL,
								AuditLogConstants.WORKFLOW_STATE_TRANS_ADMIN_REJECT,
								AuditLogConstants.AUDIT_LOG_EDIT_ACTION);
					} else if(status.equalsIgnoreCase("Approved by Transport Co-ordinator")){
						auditLogEntryforadminapproval(
								autoincNumber,
								AuditLogConstants.ADMIN_TRACKING_MODULE,
								changedBy,
								AuditLogConstants.WORKFLOW_STATE_VENDOR_SEND_FOR_APPROVAL,
								AuditLogConstants.WORKFLOW_STATE_TC_APPROVED,
								AuditLogConstants.AUDIT_LOG_EDIT_ACTION);
					}else if(status.equalsIgnoreCase("Rejected by Transport Co-ordinator")){
						auditLogEntryforadminapproval(
								autoincNumber,
								AuditLogConstants.ADMIN_TRACKING_MODULE,
								changedBy,
								AuditLogConstants.WORKFLOW_STATE_VENDOR_SEND_FOR_APPROVAL,
								AuditLogConstants.WORKFLOW_STATE_TC_REJECT,
								AuditLogConstants.AUDIT_LOG_EDIT_ACTION);
					}
				}
			}

		} catch (Exception e) {
			System.out.println("Exceptio : " + e);
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con1);

		}

		return flag;
	}

	private void auditLogEntryforadminapproval(int autoincNumber,
			String Module, int changedBy, String preworkflowState,
			String curworkflowState, String auditLogAction) {
		AuditLogDTO auditLog = new AuditLogDTO();

		auditLog.setRelatedNodeId(autoincNumber);
		auditLog.setModuleName(Module);
		auditLog.setChangedBy(changedBy);
		auditLog.setDateChanged(new Date());
		auditLog.setPreviousState(preworkflowState);
		auditLog.setCurrentState(curworkflowState);
		auditLog.setAction(auditLogAction);

		AuditLogDAO auditDAO = new AuditLogDAO();
		auditDAO.addAuditLogEntry(auditLog);

	}

	/*
	 * public ArrayList<TripDetailsDto> getTrackedTripSheetBasedOnStatus( String
	 * tripDate, String tripLog, String siteId, String tripTime[], String
	 * approvalStatus[]) { ArrayList<TripDetailsDto> tripSheet = null; DbConnect
	 * ob = null; Connection con = null;
	 * 
	 * ob = DbConnect.getInstance(); con = ob.connectDB(); TripDetailsDto
	 * tripDetailsDtoObj = null; TripDetailsChildDto tripDetailsChildDtoObj =
	 * null; tripSheet = new ArrayList<TripDetailsDto>();
	 * ArrayList<TripDetailsChildDto> tripSheetChild = null; PreparedStatement
	 * pst = null; Statement st = null; ResultSet rs = null; ResultSet rs1 =
	 * null; try { st = con.createStatement(); String query = ""; String
	 * approvalStatusQuery = ""; if (approvalStatus != null &&
	 * approvalStatus.length > 0) { approvalStatusQuery = " and ("; for (String
	 * status : approvalStatus) { if (!approvalStatusQuery.equals(" and (")) {
	 * approvalStatusQuery += "  or ";
	 * 
	 * } if (status == null || status.equals("") || status.equals("Open")) {
	 * approvalStatusQuery +=
	 * " ( p.approvalStatus is NULL or p.approvalStatus='' or p.approvalStatus='Open' )"
	 * ; } else { approvalStatusQuery += "     p.approvalStatus='" + status +
	 * "' "; } }
	 * 
	 * if (approvalStatusQuery == null || approvalStatusQuery.equals(" and (")
	 * || approvalStatusQuery.equals("")) { approvalStatusQuery = "";
	 * 
	 * } else { approvalStatusQuery += " ) "; } }
	 * 
	 * if (tripLog.equals("ALL")) { pst = con .prepareStatement(
	 * "SELECT t.id,t.trip_code,t.trip_date,t.trip_time,t.trip_log,t.routeid,t.trip_code,vt.type,t.status,t.security_status, p.logTime, ifnull(p.approvalStatus,'Open')  approvalStatus, ifnull(p.vehicleNo, v.regNo) vehicleNo, ifnull(p.status,'initial') trackingStatus, ifnull(p.comment,'') comment, ifnull(p.escort,'') escort, ifnull(p.escortClock,'') escortClock, ifnull(t.distance,0) distance, ifnull(p.manualDistance,0) manualDistance, ifnull(p.driverContact,drv.contact) driverContact    FROM trip_details t left join vehicles v on t.vehicle=v.id left join driver drv on t.driverId=drv.id left join vendor_trip_sheet_parent p on t.id=p.tripId ,vehicle_type vt where t.siteId=? and (t.status=? or t.status=? or t.status=?)and vt.id=t.vehicle_type and t.trip_date=?   "
	 * + approvalStatusQuery + "  order by t.id"); pst.setString(1, siteId);
	 * pst.setString(2, "saved"); pst.setString(3, "addsave"); pst.setString(4,
	 * "saveedit"); pst.setString(5, tripDate); String printQuery=
	 * "SELECT t.id,t.trip_code,t.trip_date,t.trip_time,t.trip_log,t.routeid,t.trip_code,vt.type,t.status,t.security_status, p.logTime, ifnull(p.approvalStatus,'Open')  approvalStatus, ifnull(p.vehicleNo, v.regNo) vehicleNo, ifnull(p.status,'initial') trackingStatus, ifnull(p.comment,'') comment, ifnull(p.escort,'') escort, ifnull(p.escortClock,'') escortClock, ifnull(t.distance,0) distance, ifnull(p.manualDistance,0) manualDistance, ifnull(p.driverContact,drv.contact) driverContact    FROM trip_details t join vehicles v on t.vehilce=v.id left join driver drv on t.driverId=drv.id left join vendor_trip_sheet_parent p on t.id=p.tripId ,vehicle_type vt where t.siteId=? and (t.status=? or t.status=? or t.status=?)and vt.id=t.vehicle_type and t.trip_date=?   "
	 * + approvalStatusQuery + "  order by t.id"; printQuery =
	 * printQuery.replace("?", "'%s'"); printQuery = String.format(printQuery,
	 * siteId , "saved" , "addsave" , "saveedit" , tripDate );
	 * System.out.println(printQuery);
	 * 
	 * 
	 * } else if (tripTime[0].equals("ALL")) { pst = con .prepareStatement(
	 * "SELECT t.id,t.trip_code,t.trip_date,t.trip_time,t.trip_log,t.routeid,t.trip_code,vt.type,t.status,t.security_status,  p.logTime, ifnull(p.approvalStatus,'Open')  approvalStatus, ifnull(p.vehicleNo, v.regNo) vehicleNo, ifnull(p.status,'initial') trackingStatus, ifnull(p.comment,'') comment, ifnull(p.escort,'') escort, ifnull(p.escortClock,'') escortClock, ifnull(t.distance,0) distance, ifnull(p.manualDistance,0) manualDistance, ifnull(p.driverContact,drv.contact) driverContact FROM trip_details t left join vehicles v on t.vehilce=v.id left join driver drv on t.driverId=drv.id left join vendor_trip_sheet_parent p on t.id=p.tripId  ,vehicle_type vt where t.siteId=? and (t.status=? or t.status=? or t.status=?)and vt.id=t.vehicle_type and t.trip_date=? and t.trip_log=?   "
	 * + approvalStatusQuery + "  order by t.id"); pst.setString(1, siteId);
	 * pst.setString(2, "saved"); pst.setString(3, "addsave"); pst.setString(4,
	 * "saveedit"); pst.setString(5, tripDate); pst.setString(6, tripLog);
	 * String printQuery=
	 * "SELECT t.id,t.trip_code,t.trip_date,t.trip_time,t.trip_log,t.routeid,t.trip_code,vt.type,t.status,t.security_status,  p.logTime, ifnull(p.approvalStatus,'Open')  approvalStatus, ifnull(p.vehicleNo, v.regNo) vehicleNo, ifnull(p.status,'initial') trackingStatus, ifnull(p.comment,'') comment, ifnull(p.escort,'') escort, ifnull(p.escortClock,'') escortClock, ifnull(t.distance,0) distance, ifnull(p.manualDistance,0) manualDistance, ifnull(p.driverContact,drv.contact) driverContact FROM trip_details t left join vehicles v on t.vehicle=v.id left join driver drv on t.driverId=drv.id left join vendor_trip_sheet_parent p on t.id=p.tripId  ,vehicle_type vt where t.siteId=? and (t.status=? or t.status=? or t.status=?)and vt.id=t.vehicle_type and t.trip_date=? and t.trip_log=?   "
	 * + approvalStatusQuery + "  order by t.id"; printQuery =
	 * printQuery.replace("?", "'%s'"); printQuery = String.format(printQuery,
	 * siteId , "saved" , "addsave" , "saveedit" , tripDate , tripLog);
	 * System.out.println(printQuery);
	 * 
	 * } else { String tripTimeQuery = ""; for (String ttime : tripTime) {
	 * tripTimeQuery += ",'" + ttime + "'"; } tripTimeQuery =
	 * tripTimeQuery.replaceFirst(",", ""); tripTimeQuery = "(" + tripTimeQuery
	 * + ") ";
	 * 
	 * pst = con .prepareStatement(
	 * "SELECT t.id,t.trip_code,t.trip_date,t.trip_time,t.trip_log,t.routeid,t.trip_code,vt.type,t.status,t.security_status,  p.logTime, ifnull(p.approvalStatus,'Open')  approvalStatus, ifnull(p.vehicleNo, v.regNo) vehicleNo, ifnull(p.status,'initial') trackingStatus, ifnull(p.comment,'') comment, ifnull(p.escort,'') escort, ifnull(p.escortClock,'') escortClock, ifnull(t.distance,0) distance, ifnull(p.manualDistance,0) manualDistance, ifnull(p.driverContact, drv.contact) driverContact FROM trip_details t left join vehicles v on t.vehicle=v.id left join driver drv on t.driverId=drv.id left join vendor_trip_sheet_parent p on t.id=p.tripId  ,vehicle_type vt where t.siteId=? and (t.status=? or t.status=? or t.status=?)and vt.id=t.vehicle_type and t.trip_date=? and t.trip_log=? and t.trip_time in "
	 * + tripTimeQuery + "    " + approvalStatusQuery + "  order by t.id");
	 * pst.setString(1, siteId); pst.setString(2, "saved"); pst.setString(3,
	 * "addsave"); pst.setString(4, "saveedit"); pst.setString(5, tripDate);
	 * pst.setString(6, tripLog); String printQuery=
	 * "SELECT t.id,t.trip_code,t.trip_date,t.trip_time,t.trip_log,t.routeid,t.trip_code,vt.type,t.status,t.security_status,  p.logTime, ifnull(p.approvalStatus,'Open')  approvalStatus, ifnull(p.vehicleNo, v.regNo) vehicleNo, ifnull(p.status,'initial') trackingStatus, ifnull(p.comment,'') comment, ifnull(p.escort,'') escort, ifnull(p.escortClock,'') escortClock, ifnull(t.distance,0) distance, ifnull(p.manualDistance,0) manualDistance, ifnull(p.driverContact,drv.contact) driverContact  FROM trip_details t join vehicles v on t.vehicle=v.id left join driver drv on t.driverId=drv.id left join vendor_trip_sheet_parent p on t.id=p.tripId  ,vehicle_type vt where t.siteId=? and (t.status=? or t.status=? or t.status=?)and vt.id=t.vehicle_type and t.trip_date=? and t.trip_log=? and t.trip_time in "
	 * + tripTimeQuery + "    " + approvalStatusQuery + "  order by t.id";
	 * printQuery = printQuery.replace("?", "'%s'"); printQuery =
	 * String.format(printQuery, siteId , "saved" , "addsave" , "saveedit" ,
	 * tripDate , tripLog); System.out.println(printQuery);
	 * 
	 * } rs = pst.executeQuery(); while (rs.next()) { tripDetailsDtoObj = new
	 * TripDetailsDto(); tripDetailsDtoObj.setId(rs.getString("id"));
	 * 
	 * tripDetailsDtoObj.setActualLogTime(rs.getString("logTime"));
	 * tripDetailsDtoObj.setVehicleNo(rs.getString("vehicleNo"));
	 * 
	 * tripDetailsDtoObj.setApprovalStatus(rs .getString("approvalStatus"));
	 * tripDetailsDtoObj.setComment(rs.getString("comment"));
	 * tripDetailsDtoObj.setEscort(rs.getString("escort"));
	 * tripDetailsDtoObj.setEscortNo(rs.getString("escortClock"));
	 * tripDetailsDtoObj.setTrip_code(rs.getString("trip_code"));
	 * tripDetailsDtoObj.setTrip_date(rs.getDate("trip_date") .toString());
	 * tripDetailsDtoObj.setDistance(rs.getString("distance"));
	 * tripDetailsDtoObj.setTrip_time(rs.getString("trip_time"));
	 * 
	 * tripDetailsDtoObj.setTrip_log(rs.getString("trip_log"));
	 * tripDetailsDtoObj.setRouteId(rs.getString("routeid"));
	 * tripDetailsDtoObj.setTrip_code(rs.getString("trip_code"));
	 * tripDetailsDtoObj.setVehicle_type(rs.getString("type"));
	 * tripDetailsDtoObj .setIsSecurity(rs.getString("security_status"));
	 * tripDetailsDtoObj.setComments(getTripComments(tripDetailsDtoObj
	 * .getId()));
	 * 
	 * tripDetailsDtoObj.setTrackingStatus(rs.getString("trackingStatus"));
	 * tripDetailsDtoObj.setTripBasedDistance(rs.getString("manualDistance" ));
	 * tripDetailsDtoObj.setDriverContact(rs.getString("driverContact"));
	 * 
	 * 
	 * 
	 * // tripSheetChild = null; tripSheetChild = new
	 * ArrayList<TripDetailsChildDto>(); String status = rs.getString("status");
	 * query =
	 * "select tdc.employeeId,e.displayname as EmployeeName, e.personnelNo,a.area,p.place,l.id as landmarkId,l.landmark,e.contactNumber1 as contactno, vt.employeeId approvedEmployee, vt.showStatus,vt.noShowReason,e.gender   from trip_details_child tdc left  join vendor_trip_sheet vt on tdc.employeeId=vt.employeeId and tdc.tripId=vt.tripId left outer join landmark l on  tdc.landmarkid=l.id left outer join place p on  l.place=p.id left outer join area a on  p.area=a.id,employee e  where e.id=tdc.employeeId and tdc.tripId="
	 * + rs.getString(1) + " order by tdc.routedOrder"; rs1 =
	 * st.executeQuery(query); while (rs1.next()) {
	 * 
	 * tripDetailsChildDtoObj = new TripDetailsChildDto();
	 * tripDetailsChildDtoObj.setApprovedEmployee(rs1
	 * .getString("approvedEmployee")); tripDetailsChildDtoObj.setShowStatus(rs1
	 * .getString("showStatus")); tripDetailsChildDtoObj.setReason(rs1
	 * .getString("noShowReason")); if
	 * (tripDetailsChildDtoObj.getApprovedEmployee() != null &&
	 * !tripDetailsChildDtoObj.getApprovedEmployee() .equals("")) {
	 * tripDetailsDtoObj.setCanTravel(true); }
	 * 
	 * tripDetailsChildDtoObj.setTripId("" + rs.getInt(1));
	 * tripDetailsChildDtoObj.setEmployeeId(rs1 .getString("employeeId"));
	 * tripDetailsChildDtoObj.setEmployeeName(rs1 .getString("EmployeeName"));
	 * tripDetailsChildDtoObj.setPersonnelNo(rs1 .getString("personnelNo"));
	 * tripDetailsChildDtoObj.setArea(rs1.getString("area"));
	 * tripDetailsChildDtoObj.setPlace(rs1.getString("place"));
	 * tripDetailsChildDtoObj.setLandmarkId(rs1 .getString("landmarkId"));
	 * tripDetailsChildDtoObj.setLandmark(rs1 .getString("landmark"));
	 * tripDetailsChildDtoObj.setContactNumber(rs1 .getString("contactno"));
	 * 
	 * tripSheetChild.add(tripDetailsChildDtoObj); }
	 * 
	 * tripDetailsDtoObj.setTripDetailsChildDtoList(tripSheetChild);
	 * tripSheet.add(tripDetailsDtoObj); //
	 * System.out.println(" early log time :" + //
	 * tripDetailsDtoObj.getActualLogTime()+" ->"+rs.getString("logTime") // );
	 * // System.out.println(" late log time :" + //
	 * tripDetailsDtoObj.getTrip_time()+" ->"+rs.getString("trip_time") // );
	 * rs1.close();
	 * 
	 * }
	 * 
	 * } catch (Exception e) { System.out.println("Error in getting saved" + e);
	 * } finally { DbConnect.closeResultSet(rs, rs1);
	 * DbConnect.closeStatement(pst); DbConnect.closeConnection(con); } return
	 * tripSheet; }
	 */
	public ArrayList<TripDetailsDto> getTrackedTripSheetBasedOnStatus_Search(
			String tripDate, String tripLog, String siteId, String tripTime[],
			String personnelNo, String vendor, String approvalStatus[]) {
		ArrayList<TripDetailsDto> tripSheet = null;
		DbConnect ob = null;
		Connection con1 = null;
		TripDetailsDto tripDetailsDtoObj = null;
		TripDetailsChildDto tripDetailsChildDtoObj = null;
		tripSheet = new ArrayList<TripDetailsDto>();
		ArrayList<TripDetailsChildDto> tripSheetChild = null;
		PreparedStatement pst = null;
		Statement st = null;
		ResultSet rs = null;
		ResultSet rs1 = null;
		try {
			ob = DbConnect.getInstance();
			con1 = ob.connectDB();
			st = con1.createStatement();
			String query = "";

			String employeeSearchQuery = "";
			String vendorSearchQuery = "";
			if (personnelNo != null && !personnelNo.equals("")) {
				// employeeSearchQuery=" and t.id in (select c.tripId from trip_details_child c join employee e on c.employeeId=e.id where e.PersonnelNo="
				// + personnelNo + ")";
				// and (t.id in (select c.tripId from trip_details_child c join
				// employee e on c.employeeId=e.id where e.PersonnelNo='" +
				// personnelNo +
				// "'  and c.status ='saved' ) or t.id in (select m.tripId from trip_details_modified m join employee e on m.empid=e.id where e.PersonnelNo='"
				// + personnelNo + "' and m.status in ('saveedit','addsave')) )
				// employeeSearchQuery =
				// " and (t.id in (select c.tripId from trip_details_child c join employee e on c.employeeId=e.id where e.PersonnelNo=?  and t.status ='saved' ) or t.id in (select m.tripId from trip_details_modified m join employee e on m.empid=e.id where e.PersonnelNo=? and t.status in ('saveedit','addsave')) ) ";
				employeeSearchQuery = " and t.id in (select c.tripId from trip_details_child c join employee e on c.employeeId=e.id where e.PersonnelNo=? )";
			}
			if (vendor != null && !vendor.equals("")) {
				vendorSearchQuery = "  and t.id in (select tripId from vendor_trip_sheet_parent vp where vp.insertedBy in (select vdr.id from vendor vdr where vdr.name like ?) or vp.updatedBy in (select vdr.id from vendor vdr where vdr.name like ?))";
			}

			String approvalStatusQuery = "";
			if (approvalStatus != null && approvalStatus.length > 0) {
				approvalStatusQuery = " and (";
				for (String status : approvalStatus) {
					if (!approvalStatusQuery.equals(" and (")) {
						approvalStatusQuery += "  or ";

					}
					if (status == null || status.equals("")
							|| status.equals("Open")) {
						approvalStatusQuery += " ( p.approvalStatus is NULL or p.approvalStatus='' or p.approvalStatus='Open' )";
					} else {
						approvalStatusQuery += "     p.approvalStatus='"
								+ status + "' ";
					}
				}

				if (approvalStatusQuery == null
						|| approvalStatusQuery.equals(" and (")
						|| approvalStatusQuery.equals("")) {
					approvalStatusQuery = "";

				} else {
					approvalStatusQuery += " ) ";
				}
			}

			int step = 0;
			if (tripLog.equals("ALL")) {
				pst = con1
						.prepareStatement("SELECT t.id,t.trip_code,t.trip_date,t.trip_time,t.trip_log,t.routeid,t.trip_code,vt.type,t.status,t.security_status, p.logTime, p.approvalStatus, p.vehicleNo , ifnull(p.comment,'') comment, ifnull(p.escort,'') escort, ifnull(p.escortClock,'') escortClock,ifnull(t.distance,0) distance, ifnull(p.manualDistance,0) manualDistance  FROM trip_details t left join vendor_trip_sheet_parent p on t.id=p.tripId ,vehicle_type vt where t.siteId=? and (t.status=? or t.status=? or t.status=?)and vt.id=t.vehicle_type and t.trip_date=? "
								+ employeeSearchQuery
								+ vendorSearchQuery
								+ " "
								+ approvalStatusQuery + "  order by t.id");
				step = 0;

				pst.setString(step + 1, siteId);
				pst.setString(step + 2, "saved");
				pst.setString(step + 3, "addsave");
				pst.setString(step + 4, "saveedit");
				pst.setString(step + 5, tripDate);
				step = 5;
				if (personnelNo != null && !personnelNo.equals("")) {
					pst.setString(++step, personnelNo);
					pst.setString(++step, personnelNo);
				}
				if (vendor != null && !vendor.equals("")) {
					pst.setString(++step, "%" + vendor + "%");
					pst.setString(++step, "%" + vendor + "%");
				}

			} else if (tripTime[0].equals("ALL")) {
				pst = con1
						.prepareStatement("SELECT t.id,t.trip_code,t.trip_date,t.trip_time,t.trip_log,t.routeid,t.trip_code,vt.type,t.status,t.security_status,   time_format(p.logTime, '%H:%i') logTime , p.approvalStatus, p.vehicleNo , ifnull(p.comment,'') comment, ifnull(p.escort,'') escort, ifnull(p.escortClock,'') escortClock,ifnull(t.distance,0) distance, ifnull(p.manualDistance,0) manualDistance  FROM trip_details t left join vendor_trip_sheet_parent p on t.id=p.tripId  ,vehicle_type vt where t.siteId=? and (t.status=? or t.status=? or t.status=?)and vt.id=t.vehicle_type and t.trip_date=? and t.trip_log=? "
								+ employeeSearchQuery
								+ vendorSearchQuery
								+ "  "
								+ approvalStatusQuery
								+ "   order by t.id");
				step = 0;

				pst.setString(step + 1, siteId);
				pst.setString(step + 2, "saved");
				pst.setString(step + 3, "addsave");
				pst.setString(step + 4, "saveedit");
				pst.setString(step + 5, tripDate);
				pst.setString(step + 6, tripLog);
				step = 6;
				if (personnelNo != null && !personnelNo.equals("")) {
					pst.setString(++step, personnelNo);
					pst.setString(++step, personnelNo);
				}
				if (vendor != null && !vendor.equals("")) {
					pst.setString(++step, "%" + vendor + "%");
					pst.setString(++step, "%" + vendor + "%");
				}
			} else {
				String tripTimeQuery = "";
				for (String ttime : tripTime) {
					tripTimeQuery += ",'" + ttime + "'";
				}
				tripTimeQuery = tripTimeQuery.replaceFirst(",", "");
				tripTimeQuery = "(" + tripTimeQuery + ") ";

				// System.out.println("Trim time in-query:" + tripTimeQuery);

				pst = con1
						.prepareStatement("SELECT t.id,t.trip_code,t.trip_date,t.trip_time,t.trip_log,t.routeid,t.trip_code,vt.type,t.status,t.security_status,   time_format(p.logTime, '%H:%i') logTime , p.approvalStatus, p.vehicleNo, ifnull(p.status,'initial') trackingStatus, ifnull(p.comment,'') comment, ifnull(p.escort,'') escort, ifnull(p.escortClock,'') escortClock,ifnull(t.distance,0) distance, ifnull(p.manualDistance,0) manualDistance   FROM trip_details t left join vendor_trip_sheet_parent p on t.id=p.tripId  ,vehicle_type vt where t.siteId=? and (t.status=? or t.status=? or t.status=?)and vt.id=t.vehicle_type and t.trip_date=? and t.trip_log=? and t.trip_time in "
								+ tripTimeQuery
								+ "  "
								+ employeeSearchQuery
								+ vendorSearchQuery
								+ "  "
								+ approvalStatusQuery + "   order by t.id");
				step = 0;

				pst.setString(step + 1, siteId);
				pst.setString(step + 2, "saved");
				pst.setString(step + 3, "addsave");
				pst.setString(step + 4, "saveedit");
				pst.setString(step + 5, tripDate);
				pst.setString(step + 6, tripLog);

				step = 6;
				if (personnelNo != null && !personnelNo.equals("")) {
					pst.setString(++step, personnelNo);
					pst.setString(++step, personnelNo);
				}
				if (vendor != null && !vendor.equals("")) {
					pst.setString(++step, "%" + vendor + "%");
					pst.setString(++step, "%" + vendor + "%");
				}
			}
			rs = pst.executeQuery();
			while (rs.next()) {
				tripDetailsDtoObj = new TripDetailsDto();
				tripDetailsDtoObj.setId(rs.getString("id"));
				tripDetailsDtoObj.setActualLogTime(rs.getString("logTime"));
				tripDetailsDtoObj.setVehicleNo(rs.getString("vehicleNo"));
				tripDetailsDtoObj.setApprovalStatus(rs
						.getString("approvalStatus"));
				tripDetailsDtoObj.setComment(rs.getString("comment"));
				tripDetailsDtoObj.setEscort(rs.getString("escort"));
				tripDetailsDtoObj.setEscortNo(rs.getString("escortClock"));
				tripDetailsDtoObj.setTrip_code(rs.getString("trip_code"));
				tripDetailsDtoObj.setTrip_date(rs.getDate("trip_date")
						.toString());
				tripDetailsDtoObj.setTrip_time(rs.getString("trip_time"));
				tripDetailsDtoObj.setTrip_log(rs.getString("trip_log"));
				tripDetailsDtoObj.setRouteId(rs.getString("routeid"));
				tripDetailsDtoObj.setTrip_code(rs.getString("trip_code"));
				tripDetailsDtoObj.setVehicle_type(rs.getString("type"));
				tripDetailsDtoObj
						.setIsSecurity(rs.getString("security_status"));
				tripDetailsDtoObj.setComments(getTripComments(tripDetailsDtoObj
						.getId()));
				tripDetailsDtoObj.setTrackingStatus(rs
						.getString("trackingStatus"));
				// tripSheetChild = null;
				tripSheetChild = new ArrayList<TripDetailsChildDto>();
				String status = rs.getString("status");
				query = "select tdc.employeeId,e.displayname as EmployeeName,e.personnelNo,a.area,p.place,l.id as landmarkId,l.landmark,e.contactNumber1 as contactno, vt.employeeId approvedEmployee, vt.showStatus,vt.noShowReason,e.gender    from trip_details_child tdc  left  join vendor_trip_sheet vt on tdc.employeeId=vt.employeeId and tdc.tripId=vt.tripId left outer join landmark l on  tdc.landmarkid=l.id left outer join place p on  l.place=p.id left outer join area a on  p.area=a.id,employee e  where e.id=tdc.employeeId and tdc.tripId="
						+ rs.getString(1) + " order by tdc.routedOrder";
				// System.out.println("Child Query " + query);
				rs1 = st.executeQuery(query);
				while (rs1.next()) {

					tripDetailsChildDtoObj = new TripDetailsChildDto();
					tripDetailsChildDtoObj.setApprovedEmployee(rs1
							.getString("approvedEmployee"));
					tripDetailsChildDtoObj.setShowStatus(rs1
							.getString("showStatus"));
					tripDetailsChildDtoObj.setReason(rs1
							.getString("noShowReason"));
					if (tripDetailsChildDtoObj.getApprovedEmployee() != null
							&& !tripDetailsChildDtoObj.getApprovedEmployee()
									.equals("")) {
						tripDetailsDtoObj.setCanTravel(true);
					}

					tripDetailsChildDtoObj.setTripId("" + rs.getInt(1));
					tripDetailsChildDtoObj.setEmployeeId(rs1
							.getString("employeeId"));
					tripDetailsChildDtoObj.setEmployeeName(rs1
							.getString("EmployeeName"));
					tripDetailsChildDtoObj.setPersonnelNo(rs1
							.getString("personnelNo"));
					tripDetailsChildDtoObj.setArea(rs1.getString("area"));
					tripDetailsChildDtoObj.setPlace(rs1.getString("place"));
					tripDetailsChildDtoObj.setLandmarkId(rs1
							.getString("landmarkId"));
					tripDetailsChildDtoObj.setLandmark(rs1
							.getString("landmark"));
					tripDetailsChildDtoObj.setContactNumber(rs1
							.getString("contactno"));
					tripSheetChild.add(tripDetailsChildDtoObj);
				}

				tripDetailsDtoObj.setTripDetailsChildDtoList(tripSheetChild);
				tripSheet.add(tripDetailsDtoObj);
				rs1.close();
			}
		} catch (Exception e) {
			System.out.println("Error in getting saved" + e);
		} finally {
			DbConnect.closeResultSet(rs, rs1);
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con1);
		}
		return tripSheet;
	}

	public int deleteNoTrips(HashMap<String, String> noTrips) {

		DbConnect ob = null;
		Connection con1 = null;
		ResultSet rs = null;
		ResultSet rs1 = null;

		String trips = "";
		Statement st1 = null;
		boolean flag = false;
		int val = 0;
		Statement st = null;
		try {
			for (Iterator i = noTrips.entrySet().iterator(); i.hasNext();) {
				java.util.Map.Entry entry = (java.util.Map.Entry) i.next();

				trips += "," + entry.getValue();

			}

			trips = trips.replaceFirst(",", "");

			ob = DbConnect.getInstance();
			con1 = ob.connectDB();
			String checkParentQuery = "select * from vendor_trip_sheet_parent where tripId in ("
					+ trips + ") ";
			String checkChildQuery = "select * from vendor_trip_sheet where tripId in ("
					+ trips + ")";
			String deleteChildQuery = "delete vendor_trip_sheet where tripId in ("
					+ trips + ")";
			String deleteParentQuery = "delete vendor_trip_sheet_parent where tripId in ("
					+ trips + ")";

			st = con1.createStatement();
			rs = st.executeQuery(checkChildQuery);
			if (rs.next()) {
				st1 = con1.createStatement();
				val = st1.executeUpdate(deleteChildQuery);

				DbConnect.closeStatement(st1);

			}
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);

			st = con1.createStatement();
			rs = st.executeQuery(checkParentQuery);
			if (rs.next()) {
				st1 = con1.createStatement();
				val += st1.executeUpdate(deleteParentQuery);

				DbConnect.closeStatement(st1);
			}

		} catch (Exception e) {

		}

		finally {
			DbConnect.closeResultSet(rs, rs1);
			DbConnect.closeStatement(st1);
			DbConnect.closeConnection(con1);

		}

		return val;
	}

	public ArrayList<TripDetailsDto> viewTrackedTrip(String tripId) {

		ArrayList<TripDetailsDto> tripSheet = null;
		DbConnect ob = null;
		Connection con = null;
		PreparedStatement pst = null;
		Statement st = null;
		ResultSet rs = null;
		ResultSet rs1 = null;
		try {
			ob = DbConnect.getInstance();
			con = ob.connectDB();
			st = con.createStatement();
			TripDetailsDto tripDetailsDtoObj = null;
			TripDetailsChildDto tripDetailsChildDtoObj = null;
			tripSheet = new ArrayList<TripDetailsDto>();
			ArrayList<TripDetailsChildDto> tripSheetChild = null;

			String query = "";
			String approvalStatusQuery = "";

			pst = con
					.prepareStatement("SELECT t.id,t.trip_code,t.trip_date,t.trip_time,t.trip_log,t.routeid,t.trip_code,vt.type,t.status,t.security_status, p.logTime, p.approvalStatus, p.vehicleNo, ifnull(p.status,'initial') trackingStatus, ifnull(p.comment,'') comment, p.escort, p.escortClock     FROM trip_details t   join vendor_trip_sheet_parent p on t.id=p.tripId ,vehicle_type vt where t.id=?  and vt.id=t.vehicle_type "
							+ approvalStatusQuery + "  order by t.id");
			pst.setString(1, tripId);

			rs = pst.executeQuery();
			while (rs.next()) {
				tripDetailsDtoObj = new TripDetailsDto();
				tripDetailsDtoObj.setId(rs.getString("id"));
				tripDetailsDtoObj.setActualLogTime(rs.getString("logTime"));
				tripDetailsDtoObj.setVehicleNo(rs.getString("vehicleNo"));

				tripDetailsDtoObj.setApprovalStatus(rs
						.getString("approvalStatus"));
				tripDetailsDtoObj.setComment(rs.getString("comment"));
				tripDetailsDtoObj.setEscort(rs.getString("escort"));
				tripDetailsDtoObj.setEscortNo(rs.getString("escortClock"));
				tripDetailsDtoObj.setTrip_code(rs.getString("trip_code"));
				tripDetailsDtoObj.setTrip_date(rs.getDate("trip_date")
						.toString());
				tripDetailsDtoObj.setTrip_time(rs.getString("trip_time"));
				tripDetailsDtoObj.setTrip_log(rs.getString("trip_log"));
				tripDetailsDtoObj.setRouteId(rs.getString("routeid"));
				tripDetailsDtoObj.setTrip_code(rs.getString("trip_code"));
				tripDetailsDtoObj.setVehicle_type(rs.getString("type"));
				tripDetailsDtoObj
						.setIsSecurity(rs.getString("security_status"));
				tripDetailsDtoObj.setTrackingStatus(rs
						.getString("trackingStatus"));
				// tripSheetChild = null;
				tripSheetChild = new ArrayList<TripDetailsChildDto>();
				String status = rs.getString("status");

				query = "select tdc.employeeId,e.displayname as EmployeeName,a.area,p.place,l.id as landmarkId,l.landmark,ifnull(e.contactNumber1,'') as contactno, vt.employeeId approvedEmployee, vt.showStatus, vt.noShowReason,e.gender  from trip_details_child tdc  left  join vendor_trip_sheet vt on tdc.employeeId=vt.employeeId and tdc.tripId=vt.tripId left outer join landmark l on  tdc.landmarkid=l.id left outer join place p on  l.place=p.id left outer join area a on  p.area=a.id,employee e  where e.id=tdc.employeeId and tdc.tripId="
						+ rs.getString(1) + " order by tdc.routedOrder";
			}
			// System.out.println("Query"+query);
			rs1 = st.executeQuery(query);
			while (rs1.next()) {

				tripDetailsChildDtoObj = new TripDetailsChildDto();
				tripDetailsChildDtoObj.setApprovedEmployee(rs1
						.getString("approvedEmployee"));
				tripDetailsChildDtoObj.setShowStatus(rs1
						.getString("showStatus"));
				tripDetailsChildDtoObj.setReason(rs1.getString("noShowReason"));
				if (tripDetailsChildDtoObj.getApprovedEmployee() != null
						&& !tripDetailsChildDtoObj.getApprovedEmployee()
								.equals("")) {
					tripDetailsDtoObj.setCanTravel(true);
				}

				tripDetailsChildDtoObj.setTripId("" + rs.getInt(1));
				tripDetailsChildDtoObj.setEmployeeId(rs1
						.getString("employeeId"));
				tripDetailsChildDtoObj.setEmployeeName(rs1
						.getString("EmployeeName"));
				tripDetailsChildDtoObj.setArea(rs1.getString("area"));
				tripDetailsChildDtoObj.setPlace(rs1.getString("place"));
				tripDetailsChildDtoObj.setLandmarkId(rs1
						.getString("landmarkId"));
				tripDetailsChildDtoObj.setLandmark(rs1.getString("landmark"));
				tripDetailsChildDtoObj.setContactNumber(rs1
						.getString("contactno"));
				tripSheetChild.add(tripDetailsChildDtoObj);
			}

			tripDetailsDtoObj.setTripDetailsChildDtoList(tripSheetChild);
			tripSheet.add(tripDetailsDtoObj);
			rs1.close();
		} catch (Exception e) {
			DbConnect.closeConnection(con);
			System.out.println("Error in getting viewTrackedTrip" + e);
		} finally {
			DbConnect.closeResultSet(rs, rs1);
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);
		}
		return tripSheet;
	}

	/* to get TMs or TCs comments on trips while tracking and approval */
	public ArrayList<TripCommentDto> getTripComments(String tripId) {
		ArrayList<TripCommentDto> dtoList = null;
		DbConnect ob = null;
		Connection con = null;
		PreparedStatement pst = null;
		Statement st = null;
		ResultSet rs = null;

		try {
			ob = DbConnect.getInstance();
			con = ob.connectDB();
			st = con.createStatement();
			TripCommentDto dto = null;

			pst = con
					.prepareStatement("select tc.id, tc.comment, tc.commentedBy, e.displayname commentedByName, tc.tripId,  date as date from tripComments tc, employee e where tc.commentedBy=e.id and tripId=? order by date asc");
			pst.setString(1, tripId);
			rs = pst.executeQuery();

			while (rs.next()) {
				dto = new TripCommentDto();
				dto.setComment(rs.getString("comment"));
				dto.setCommentedById(rs.getString("commentedBy"));
				dto.setCommentedByName(rs.getString("commentedByName"));
				dto.setCommentedDate(rs.getString("date"));
				dto.setTripId(tripId);
				if (dtoList == null) {
					dtoList = new ArrayList<TripCommentDto>();
				}
				dtoList.add(dto);

			}
		} catch (Exception e) {
			dtoList = null;
			System.out.println("Error in get trip Comment" + e);
		}

		finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);
		}
		return dtoList;

	}

	// to insert comments
	public int addComment(TripCommentDto dto) {
		int val = 0;
		DbConnect ob = null;
		Connection con = null;
		PreparedStatement pst = null;
		try {
			if (dto != null) {
				ob = DbConnect.getInstance();
				con = ob.connectDB();

				pst = con
						.prepareStatement("insert into TripComments (comment, commentedBy, tripId, date ) values (?,?,?,curdate())");
				pst.setString(1, dto.getComment());
				pst.setString(2, dto.getCommentedById());
				pst.setString(3, dto.getTripId());

				val = pst.executeUpdate();
			} else {
				val = 0;
			}
		} catch (Exception e) {
			System.out.println("Error : " + e);

		}

		finally {

			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);
		}

		return val;
	}

	/*
	 * public ArrayList<TripDetailsDto> viewActualTrips(String tripId, String
	 * month, String year, String type) {
	 * 
	 * 
	 * ArrayList<TripDetailsDto> tripSheet = null; DbConnect ob = null;
	 * Connection con = null; try { ob = DbConnect.getInstance(); con =
	 * ob.connectDB(); TripDetailsDto tripDetailsDtoObj = null;
	 * TripDetailsChildDto tripDetailsChildDtoObj = null; tripSheet = new
	 * ArrayList<TripDetailsDto>(); ArrayList<TripDetailsChildDto>
	 * tripSheetChild = null; PreparedStatement pst = null; Statement st =
	 * con.createStatement(); ResultSet rs = null; ResultSet rs1 = null; String
	 * query = ""; String approvalStatusQuery = "";
	 * 
	 * 
	 * pst = con .prepareStatement(
	 * "SELECT t.id,t.trip_code,t.trip_date,t.trip_time,t.trip_log,t.routeid,t.trip_code,vt.type,t.status,t.security_status, p.logTime, p.approvalStatus, p.vehicleNo, ifnull(p.status,'initial') trackingStatus, ifnull(p.comment,'') comment, p.escort, p.escortClock     FROM trip_details t left   join vendor_trip_sheet_parent p on t.id=p.tripId ,vehicle_type vt where  vt.id=t.vehicle_type "
	 * + " and datepart(month, t.trip_date)=? and datepart(month,t.tip_ " +
	 * approvalStatusQuery + "  order by t.id"); pst.setString(1, tripId);
	 * 
	 * 
	 * rs = pst.executeQuery(); while (rs.next()) { tripDetailsDtoObj = new
	 * TripDetailsDto(); tripDetailsDtoObj.setId(rs.getString("id"));
	 * tripDetailsDtoObj.setActualLogTime(rs.getString("logTime"));
	 * tripDetailsDtoObj.setVehicleNo(rs.getString("vehicleNo"));
	 * 
	 * tripDetailsDtoObj.setApprovalStatus(rs .getString("approvalStatus"));
	 * tripDetailsDtoObj.setComment(rs.getString("comment"));
	 * tripDetailsDtoObj.setEscort(rs.getString("escort"));
	 * tripDetailsDtoObj.setEscortNo(rs.getString("escortClock"));
	 * System.out.println(" log tim e: " + rs.getString("logTime"));
	 * tripDetailsDtoObj.setTrip_code(rs.getString("trip_code"));
	 * tripDetailsDtoObj.setTrip_date(rs.getDate("trip_date") .toString());
	 * tripDetailsDtoObj.setTrip_time(rs.getString("trip_time"));
	 * tripDetailsDtoObj.setTrip_log(rs.getString("trip_log"));
	 * tripDetailsDtoObj.setRouteId(rs.getString("routeid"));
	 * tripDetailsDtoObj.setTrip_code(rs.getString("trip_code"));
	 * tripDetailsDtoObj.setVehicle_type(rs.getString("type"));
	 * tripDetailsDtoObj .setIsSecurity(rs.getString("security_status")); //
	 * tripSheetChild = null; tripSheetChild = new
	 * ArrayList<TripDetailsChildDto>(); String status = rs.getString("status");
	 * if (status.equals("saveedit") || status.equals("addsave")) { query =
	 * "select tdc.empid as employeeId,e.displayname as EmployeeName,a.area,p.place,l.id as landmarkId,l.landmark,ifnull(e.contactNumber1,'') as contactno, vt.employeeId approvedEmployee, vt.showStatus, vt.noShowReason  from trip_details_modified tdc left  join vendor_trip_sheet vt on tdc.empId=vt.employeeId and tdc.tripId=vt.tripId ,employee e ,area a,place p,landmark l where e.id=tdc.empid and tdc.tripId="
	 * + rs.getString(1) +
	 * " and tdc.landmarkId=l.id and l.place=p.id and p.area=a.id order by tdc.routedOrder"
	 * ; } else if (status.equals("saved")) { query =
	 * "select tdc.employeeId,e.displayname as EmployeeName,a.area,p.place,l.id as landmarkId,l.landmark,ifnull(e.contactNumber1,'') as contactno, vt.employeeId approvedEmployee, vt.showStatus, vt.noShowReason  from trip_details_child tdc  left  join vendor_trip_sheet vt on tdc.employeeId=vt.employeeId and tdc.tripId=vt.tripId ,employee e ,area a,place p,landmark l where e.id=tdc.employeeId and tdc.tripId="
	 * + rs.getString(1) +
	 * " and tdc.landmarkId=l.id and l.place=p.id and p.area=a.id order by tdc.routedOrder"
	 * ; } // System.out.println("Query"+query); rs1 = st.executeQuery(query);
	 * while (rs1.next()) {
	 * 
	 * tripDetailsChildDtoObj = new TripDetailsChildDto();
	 * tripDetailsChildDtoObj.setApprovedEmployee(rs1
	 * .getString("approvedEmployee")); tripDetailsChildDtoObj.setShowStatus(rs1
	 * .getString("showStatus")); tripDetailsChildDtoObj.setReason(rs1
	 * .getString("noShowReason")); if
	 * (tripDetailsChildDtoObj.getApprovedEmployee() != null &&
	 * !tripDetailsChildDtoObj.getApprovedEmployee() .equals("")) {
	 * tripDetailsDtoObj.setCanTravel(true); }
	 * 
	 * tripDetailsChildDtoObj.setTripId("" + rs.getInt(1));
	 * tripDetailsChildDtoObj.setEmployeeId(rs1 .getString("employeeId"));
	 * tripDetailsChildDtoObj.setEmployeeName(rs1 .getString("EmployeeName"));
	 * tripDetailsChildDtoObj.setArea(rs1.getString("area"));
	 * tripDetailsChildDtoObj.setPlace(rs1.getString("place"));
	 * tripDetailsChildDtoObj.setLandmarkId(rs1 .getString("landmarkId"));
	 * tripDetailsChildDtoObj.setLandmark(rs1 .getString("landmark"));
	 * tripDetailsChildDtoObj.setContactNumber(rs1 .getString("contactno"));
	 * tripSheetChild.add(tripDetailsChildDtoObj); }
	 * 
	 * tripDetailsDtoObj.setTripDetailsChildDtoList(tripSheetChild);
	 * tripSheet.add(tripDetailsDtoObj); rs1.close(); }
	 * DbConnect.closeResultSet(rs, rs1); DbConnect.closeStatement(pst);
	 * DbConnect.closeConnection(con); } catch (Exception e) {
	 * DbConnect.closeConnection(con);
	 * System.out.println("Error in getting viewTrackedTrip" + e); } return
	 * tripSheet; }
	 */
	public ArrayList<EmployeeDto> getemployeeContactNo(int tripid) {
		DbConnect ob = DbConnect.getInstance();
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		ArrayList<EmployeeDto> list = new ArrayList<EmployeeDto>();
		String query = "Select e.displayname,e.contactNumber1,e.contactNumber2 from employee e,vendor_trip_sheet vt where vt.tripId in  (select id from trip_details where trip_log='IN' and id="
				+ tripid + ") and   e.id=vt.employeeId";
		try {
			con = ob.connectDB();
			st = con.createStatement();
			rs = st.executeQuery(query);
			while (rs.next()) {
				EmployeeDto dto = new EmployeeDto();
				dto.setDisplayName(rs.getString("displayname"));
				if (rs.getString("contactNumber1").equals("")
						|| rs.getString("contactNumber1") == null) {
					dto.setContactNo(rs.getString("contactNumber2"));
				} else {
					dto.setContactNo(rs.getString("contactNumber1"));
				}

				list.add(dto);
			}
		} catch (Exception e) {
			System.out.println("Error" + e);
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);
		}
		return list;
	}

	public VendorDto getVendorFromTripId(String tripId) {
		Connection con = null;
		ResultSet rs = null;
		Statement st = null;
		VendorDto vdto = null;

		try {
			String query = "select tripid,vm.id, vm.Company, vm.address, vm.contact, vm.email from tripvendorassign va join vendormaster vm on va.vendorId=vm.id where va.status='a' and vm.status='Active' and tripid="
					+ tripId;
			// System.out.println("Query : " + query);
			con = DbConnect.getInstance().connectDB();
			st = con.createStatement();
			rs = st.executeQuery(query);
			if (rs.next()) {
				// vm.id, vm.Company, vm.address, vm.contact, vm.email
				vdto = new VendorDto();
				vdto.setId(rs.getString("id"));
				vdto.setCompany(rs.getString("Company"));
				vdto.setAddress(rs.getString("address"));
				vdto.setContactNumber(rs.getString("contact"));
				vdto.setEmail(rs.getString("email"));

			}

		} catch (Exception e) {
			System.out.println(" Error : " + e);
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);

		}
		return vdto;
	}

	public String getIsLadyIn(String tripId) {
		DbConnect ob = null;
		Connection con = null;
		ResultSet rs = null;
		Statement st = null;
		String isLady = "NO";
		try {
			ob = DbConnect.getInstance();
			con = ob.connectDB();
			String query = "select count(*) from trip_details_child tdc,employee e where e.id=tdc.employeeId and tdc.tripid="
					+ tripId + " and e.gender='F'";
			st = con.createStatement();
			rs = st.executeQuery(query);
			if (rs.next() && rs.getInt(1) > 0) {
				isLady = "YES";
			}
		} catch (Exception e) {
			System.out.println("Error in get tirp code" + e);
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);
		}
		return isLady;

	}

	public ArrayList<TripDetailsDto> getTripsWithVehicle(String branch,
			String shift, String regNo) {
		DbConnect ob = DbConnect.getInstance();
		PreparedStatement pst = null;
		PreparedStatement pst1 = null;
		ResultSet rs = null;
		ResultSet rs1 = null;
		TripDetailsDto tripDto = null;
		TripDetailsChildDto childDto = null;
		Connection con = ob.connectDB();
		ArrayList<TripDetailsDto> tripDtailsDtos = new ArrayList<TripDetailsDto>();
		ArrayList<TripDetailsChildDto> childDtos = null;
		try {
			String filterquery = "";
			if (!shift.equalsIgnoreCase("ALL") && regNo.equalsIgnoreCase("ALL")) {
				System.out.println("here shift");
				filterquery = " and td.trip_log='" + shift + "'";
			} else if (shift.equalsIgnoreCase("ALL")
					&& !regNo.equalsIgnoreCase("ALL")) {
				System.out.println("here regNo");
				filterquery = " and v.regNo='" + regNo + "'";
			} else if (!shift.equalsIgnoreCase("ALL")
					&& !regNo.equalsIgnoreCase("ALL")) {
				System.out.println("here regNo");
				filterquery = " and v.regNo='" + regNo + "' and td.trip_log='"
						+ shift + "'";
			}

			String query = "select vp.vehicleId,vp.date_time,vp.lattitude,vp.longitude,case when timediff(now(),vp.date_time)>'00:10:00' then 'outofreach' else vp.logstatus  end as logstatus,vp.tripId,v.regNo,td.trip_code,td.trip_log,vtsp.escort,td.siteid,td.trip_code,(select count(*)  from vendor_trip_sheet vts,employee e where vts.tripId=td.id and  e.id=vts.employeeId and e.gender='F' and vts.curStatus='IN') as  ladyInCount,(select count(*)  from vendor_trip_sheet  vts where  vts.tripId=td.id) as empCount,(select count(*)  from vendor_trip_sheet  vts where  vts.tripId=td.id and vts.curStatus='IN') as empInCount, ifnull(pa.acknowledgeBy,'notstop') as acknowledgeBy from vehicles v,vehicle_position vp, (select vp.vehicleId,max(vp.date_time) as date_time from vehicle_position vp group by tripid,vehicleId) as  tg,site s,trip_details td left  outer join  panicaction pa on td.id=pa.tripId join vendor_trip_sheet_parent vtsp on td.id=vtsp.tripid where tg.vehicleId=vp.vehicleId and td.id=vp.tripId and td.siteid=s.id and s.branch="
					+ branch
					+ " and tg.date_time=vp.date_time and  vp.vehicleId=v.id and vp.status='checkedin' and vp.tripid in (select tripid from vendor_trip_sheet_parent where status='started')"
					+ filterquery
					+ "  group by v.id order by vp.date_time desc";
			/*
			 * String query =
			 * "select   vp.vehicleId,vp.date_time,vp.lattitude,vp.longitude,case when (timediff(now(),vp.date_time)>'00:10:00' and vp.logstatus!='danger') then 'outofreach' else vp.logstatus  end as logstatus,vp.tripId,v.regNo,td.trip_code,td.trip_log,vtsp.escort,td.siteid,td.trip_code,(select count(*)  from vendor_trip_sheet vts,employee e where vts.tripId=td.id and  e.id=vts.employeeId and e.gender='F' and vts.curStatus='IN') as  ladyInCount,(select count(*)  from vendor_trip_sheet  vts where  vts.tripId=td.id and vts.curStatus='IN') as empInCount,(select count(*)  from vendor_trip_sheet  vts where  vts.tripId=td.id) as empCount,  ifnull(pa.acknowledgeBy,'notstop') as acknowledgeBy from vehicles v,vehicle_position vp, (select dv.vehicleId, max( fvp.date_time ) date_time from full_vehicle_position  fvp join device_vehicle dv on fvp.dvId=dv.id group by dv.vehicleId) rp,   (select vp.vehicleId,max(vp.date_time) as date_time from vehicle_position vp group by tripid,vehicleId) as  tg,site s,trip_details td left  outer join  panicaction pa on td.id=pa.tripId join vendor_trip_sheet_parent vtsp on td.id=vtsp.tripid where  rp.vehicleId=tg.vehicleId and vp.date_time >=rp.date_time and tg.vehicleId=vp.vehicleId and td.id=vp.tripId and td.siteid=s.id"
			 * + " and s.branch=" + branch +
			 * " and tg.date_time=vp.date_time and  vp.vehicleId=v.id and vp.status='checkedin' and vp.tripid in (select tripid from vendor_trip_sheet_parent where status='started')  group by v.id order by vp.date_time desc"
			 * ;
			 */
			pst = con.prepareStatement(query);
			System.out.println(query);

			rs = pst.executeQuery();
			while (rs.next()) {

				tripDto = new TripDetailsDto();

				tripDto.setVehicle(rs.getString("vehicleid"));
				tripDto.setTrip_date(rs.getString("date_time"));
				tripDto.setId(rs.getString("tripId"));
				tripDto.setTrip_code(rs.getString("trip_code"));
				tripDto.setVehicleNo(rs.getString("regNo"));
				tripDto.setLatitude(rs.getString("lattitude"));
				tripDto.setLongitude(rs.getString("longitude"));
				tripDto.setStatus(rs.getString("logstatus"));
				tripDto.setVehicleNo(rs.getString("regNo"));
				tripDto.setTrip_code(rs.getString("trip_code"));
				tripDto.setTrip_log(rs.getString("trip_log"));
				tripDto.setIsSecurity(rs.getString("escort"));
				tripDto.setEmpCount(rs.getInt("empCount"));
				tripDto.setLadyInCount(rs.getInt("ladyInCount"));
				tripDto.setEmpInCount(rs.getInt("empInCount"));

				tripDto.setPanicAck(rs.getString("acknowledgeBy"));

				String actq = "";
				String nuquery = " select l.latitude,l.longitude,tdc.routedOrder as pos from landmark l,trip_details_child tdc where tdc.tripId="
						+ tripDto.getId() + " and  tdc.landmarkid=l.id";
				if (tripDto.getTrip_log().equals("IN"))

				{
					actq = "  select l.latitude,l.longitude,100 as pos from landmark l,site s where  s.id="
							+ rs.getString("siteid") + " and s.landmark=l.id";
				} else {
					actq = "  select l.latitude,l.longitude,0 as pos from landmark l,site s where  s.id="
							+ rs.getString("siteid") + " and s.landmark=l.id";
				}
				// System.out.println("select * from ("+nuquery+" union "+actq+") as tble order by pos");
				pst1 = con.prepareStatement("select * from (" + nuquery
						+ " union " + actq + ") as tble order by pos");
				rs1 = pst1.executeQuery();
				childDtos = new ArrayList<TripDetailsChildDto>();
				while (rs1.next()) {

					childDto = new TripDetailsChildDto();
					childDto.setLatitude(rs1.getString("latitude"));
					childDto.setLongitude(rs1.getString("longitude"));
					childDtos.add(childDto);

				}
				tripDto.setTripDetailsChildDtoList(childDtos);

				tripDtailsDtos.add(tripDto);
			}

		} catch (Exception e) {
			System.out.println("Error" + e);
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeResultSet(rs1);
			DbConnect.closeStatement(pst);
			DbConnect.closeStatement(pst1);
			DbConnect.closeConnection(con);
		}
		return tripDtailsDtos;
	}

	public TripDetailsDto getPanicTripDetails(String tripId) {
		DbConnect ob = DbConnect.getInstance();
		PreparedStatement pst = null;
		PreparedStatement pst1 = null;
		ResultSet rs = null;
		ResultSet rs1 = null;
		TripDetailsDto tripDto = new TripDetailsDto();
		TripDetailsChildDto childDto = null;
		Connection con = ob.connectDB();
		ArrayList<TripDetailsChildDto> childDtos = null;
		try {
			pst = con
					.prepareStatement("select td.id tripId,d.name driverName,d.contact driverContact,v.name vendorName,v.contactNumber as vendorContact,vs.name vendorsuperVisor, vs.contactNumber vendorsuperVisorContact,"
							+ " veh.regNo,e.name escortName,e.escortClock,e.phone escortContact,"
							+ " (select date_time from vehicle_position vp where "
							+ " vp.tripId=td.id and vp.logstatus='danger' order by date_time desc limit 1) as date_time "
							+ " from trip_details td,driver d,driver_vehicle dv,vendor v,vendor vs,vehicles veh, "
							+ " vendor_trip_sheet_parent vtsp "
							+ " left outer join escort e on vtsp.escortClock  where td.id="
							+ tripId
							+ " and "
							+ " td.id=vtsp.tripid and td.vehicle=dv.vehicleId and dv.driverId=d.id and "
							+ "  v.supervisor=vs.id and td.vehicle=veh.id");
			rs = pst.executeQuery();
			if (rs.next()) {
				tripDto = new TripDetailsDto();
				tripDto.setId(rs.getString("tripId"));
				tripDto.setDriverName(rs.getString("driverName"));
				tripDto.setDriverContact(rs.getString("driverContact"));
				tripDto.setVendorName(rs.getString("vendorName"));
				tripDto.setVendorContact(rs.getString("vendorContact"));
				tripDto.setVendorSupervisor(rs.getString("vendorSupervisor"));
				tripDto.setVendorSupervisorContact(rs
						.getString("vendorSupervisorContact"));
				tripDto.setVehicleNo(rs.getString("regNo"));
				tripDto.setEscortName(rs.getString("escortName"));
				tripDto.setEscortclock(rs.getString("escortClock"));
				tripDto.setEscortContact(rs.getString("escortContact"));
				tripDto.setTrip_time(rs.getString("date_time"));

				pst1 = con
						.prepareStatement("select e.id,e.PersonnelNo,e.gender,e.employeeFirstName,e.employeeLastName,e.displayName,e.contactNumber1,e.contactNumber2 from vendor_trip_sheet vts,employee e where vts.tripId ="
								+ tripId
								+ " and curStatus='IN' and vts.employeeId=e.id");
				rs1 = pst1.executeQuery();
				childDtos = new ArrayList<TripDetailsChildDto>();
				while (rs1.next()) {
					childDto = new TripDetailsChildDto();
					childDto.setEmployeeName(rs1.getString("displayName"));
					childDto.setEmployeeId(rs1.getString("PersonnelNo"));
					childDto.setContactNumber(rs1.getString("contactNumber1"));
					childDto.setGender(rs1.getString("gender"));
					childDtos.add(childDto);
				}
				pst1 = con
						.prepareStatement("select pa.id,e.displayName,pa.actionTime,pa.alarmCause,pa.primaryAction from panicAction pa,employee e where pa.primaryActiontakenBy=e.id AND pa.tripId="
								+ tripDto.getId() + "");
				rs1 = pst1.executeQuery();
				PanicDto dto = null;
				if (rs1.next()) {
					dto = new PanicDto();
					dto.setId(rs1.getString("id"));
					dto.setPrimaryActiontakenByName(rs1
							.getString("displayName"));
					dto.setPrimaryActiontakenTime(rs1.getString("actionTime"));
					dto.setAlarmCause(rs1.getString("alarmCause"));
					dto.setPrimaryAction(rs1.getString("primaryAction"));
				}
				tripDto.setPanicdto(dto);
				tripDto.setTripDetailsChildDtoList(childDtos);
			}

		} catch (Exception e) {
			System.out.println("Error" + e);
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeResultSet(rs1);
			DbConnect.closeStatement(pst);
			DbConnect.closeStatement(pst1);
			DbConnect.closeConnection(con);
		}
		return tripDto;

	}

	public ArrayList<TripDetailsDto> getPanicTripDetails() {
		DbConnect ob = DbConnect.getInstance();
		PreparedStatement pst = null;
		PreparedStatement pst1 = null;
		ResultSet rs = null;
		ResultSet rs1 = null;
		ArrayList<TripDetailsDto> tripDetailsDtos = new ArrayList<TripDetailsDto>();
		TripDetailsDto tripDto = new TripDetailsDto();
		TripDetailsChildDto childDto = null;
		Connection con = ob.connectDB();
		ArrayList<TripDetailsChildDto> childDtos = null;
		try {
			pst = con
					.prepareStatement("select td.id tripId,d.name driverName,d.contact driverContact,vm.Company vendorName,vm.contact as "
							+ "  vendorContact,veh.regNo,e.name "
							+ " escortName,e.escortClock,e.phone escortContact, (select date_time from vehicle_position vp where "
							+ " vp.tripId=td.id and vp.logstatus='danger' order by date_time desc limit 1) as date_time  "
							+ " from trip_details td,driver d,vendormaster vm,vehicles veh, "
							+ " vendor_trip_sheet_parent vtsp "
							+ " left outer join escort e on vtsp.escortClock  where td.id in (select tripId from panicaction where curStatus='open')"
							+ " and "
							+ " td.id=vtsp.tripid and td.vehicle=veh.id and td.driverId=d.id and veh.vendor=vm.id and vm.id=d.vendorId ");
			rs = pst.executeQuery();
			while (rs.next()) {
				tripDto = new TripDetailsDto();
				tripDto.setId(rs.getString("tripId"));
				tripDto.setDriverName(rs.getString("driverName"));
				tripDto.setDriverContact(rs.getString("driverContact"));
				tripDto.setVendorName(rs.getString("vendorName"));
				tripDto.setVendorContact(rs.getString("vendorContact"));
				tripDto.setVehicleNo(rs.getString("regNo"));
				tripDto.setEscortName(rs.getString("escortName"));
				tripDto.setEscortclock(rs.getString("escortClock"));
				tripDto.setEscortContact(rs.getString("escortContact"));
				tripDto.setTrip_time(rs.getString("date_time"));

				pst1 = con
						.prepareStatement("select e.id,e.PersonnelNo,e.gender,e.employeeFirstName,e.employeeLastName,e.displayName,e.contactNumber1,e.contactNumber2 from vendor_trip_sheet vts,employee e where vts.tripId ="
								+ tripDto.getId()
								+ " and curStatus='IN' and vts.employeeId=e.id");
				rs1 = pst1.executeQuery();
				childDtos = new ArrayList<TripDetailsChildDto>();
				while (rs1.next()) {
					childDto = new TripDetailsChildDto();
					childDto.setEmployeeName(rs1.getString("displayName"));
					childDto.setEmployeeId(rs1.getString("PersonnelNo"));
					childDto.setContactNumber(rs1.getString("contactNumber1"));
					childDto.setGender(rs1.getString("gender"));
					childDtos.add(childDto);
				}
				tripDto.setTripDetailsChildDtoList(childDtos);
				pst1 = con
						.prepareStatement("select pa.id,e.displayName,pa.actionTime,pa.alarmCause,pa.primaryAction from panicAction pa,employee e where pa.primaryActiontakenBy=e.id AND pa.tripId="
								+ tripDto.getId() + "");
				// System.out.println("select pa.id,e.displayName,pa.actionTime,pa.alarmCause,pa.primaryAction from panicAction pa,employee e where pa.primaryActiontakenBy=e.id AND pa.tripId="
				// + tripDto.getId() + "");
				rs1 = pst1.executeQuery();
				PanicDto dto = null;
				if (rs1.next()) {
					dto = new PanicDto();
					dto.setId(rs1.getString("id"));
					dto.setPrimaryActiontakenByName(rs1
							.getString("displayName"));
					dto.setPrimaryActiontakenTime(rs1.getString("actionTime"));
					dto.setAlarmCause(rs1.getString("alarmCause"));
					dto.setPrimaryAction(rs1.getString("primaryAction"));
				}
				tripDto.setPanicdto(dto);
				tripDetailsDtos.add(tripDto);
			}

		} catch (Exception e) {
			System.out.println("Error here" + e);
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeResultSet(rs1);
			DbConnect.closeStatement(pst);
			DbConnect.closeStatement(pst1);
			DbConnect.closeConnection(con);
		}
		return tripDetailsDtos;

	}

	public ArrayList<TripDetailsDto> getPanicTripActionDetails() {
		DbConnect ob = DbConnect.getInstance();
		PreparedStatement pst = null;
		PreparedStatement pst1 = null;
		ResultSet rs = null;
		ResultSet rs1 = null;
		ArrayList<TripDetailsDto> tripDetailsDtos = new ArrayList<TripDetailsDto>();
		TripDetailsDto tripDto = new TripDetailsDto();
		TripDetailsChildDto childDto = null;
		Connection con = ob.connectDB();
		ArrayList<TripDetailsChildDto> childDtos = null;
		try {
			pst = con
					.prepareStatement("select td.id tripId,d.name driverName,d.contact driverContact,v.name vendorName,v.contactNumber as vendorContact,vs.name vendorsuperVisor, vs.contactNumber vendorsuperVisorContact,"
							+ " veh.regNo,e.name escortName,e.escortClock,e.phone escortContact,"
							+ " (select date_time from vehicle_position vp where "
							+ " vp.tripId=td.id and vp.logstatus='danger' order by date_time desc limit 1) as date_time "
							+ " from trip_details td,driver d,driver_vehicle dv,vendor v,vendor vs,vehicles veh, "
							+ " vendor_trip_sheet_parent vtsp "
							+ " left outer join escort e on vtsp.escortClock  where td.id in (select tripId from panicaction where curStatus='open')"
							+ " and "
							+ " td.id=vtsp.tripid and td.vehicle=dv.vehicleId and dv.driverId=d.id "
							+ " and v.supervisor=vs.id and td.vehicle=veh.id");
			rs = pst.executeQuery();
			while (rs.next()) {
				tripDto = new TripDetailsDto();
				tripDto.setId(rs.getString("tripId"));
				tripDto.setDriverName(rs.getString("driverName"));
				tripDto.setDriverContact(rs.getString("driverContact"));
				tripDto.setVendorName(rs.getString("vendorName"));
				tripDto.setVendorContact(rs.getString("vendorContact"));
				tripDto.setVendorSupervisor(rs.getString("vendorSupervisor"));
				tripDto.setVendorSupervisorContact(rs
						.getString("vendorSupervisorContact"));
				tripDto.setVehicleNo(rs.getString("regNo"));
				tripDto.setEscortName(rs.getString("escortName"));
				tripDto.setEscortclock(rs.getString("escortClock"));
				tripDto.setEscortContact(rs.getString("escortContact"));
				tripDto.setTrip_time(rs.getString("date_time"));

				pst1 = con
						.prepareStatement("select e.id,e.PersonnelNo,e.gender,e.employeeFirstName,e.employeeLastName,e.displayName,e.contactNumber1,e.contactNumber2 from vendor_trip_sheet vts,employee e where vts.tripId ="
								+ tripDto.getId()
								+ " and curStatus='IN' and vts.employeeId=e.id");
				rs1 = pst1.executeQuery();
				childDtos = new ArrayList<TripDetailsChildDto>();
				while (rs1.next()) {
					childDto = new TripDetailsChildDto();
					childDto.setEmployeeName(rs1.getString("displayName"));
					childDto.setEmployeeId(rs1.getString("PersonnelNo"));
					childDto.setContactNumber(rs1.getString("contactNumber1"));
					childDto.setGender(rs1.getString("gender"));
					childDtos.add(childDto);
				}
				tripDto.setTripDetailsChildDtoList(childDtos);
				pst1 = con
						.prepareStatement("select pa.id,e.displayName,pa.actionTime,pa.alarmCause,pa.primaryAction,e1.displayName as approvedBy,pa.stopTime from employee e,panicAction pa left outer join employee e1 on  pa.approvedBy=e1.id  where pa.primaryActiontakenBy=e.id AND pa.tripId=1"
								+ tripDto.getId() + "");
				rs1 = pst1.executeQuery();
				PanicDto dto = null;
				if (rs1.next()) {
					dto = new PanicDto();
					dto.setId(rs1.getString("id"));
					dto.setPrimaryActiontakenByName(rs1
							.getString("displayName"));
					dto.setPrimaryActiontakenTime(rs1.getString("actionTime"));
					dto.setAlarmCause(rs1.getString("alarmCause"));
					dto.setPrimaryAction(rs1.getString("primaryAction"));
					dto.setApprovedByName(rs1.getString("approvedBy"));
					dto.setApprovedByName(rs1.getString("stopTime"));
				}
				tripDto.setPanicdto(dto);
				tripDetailsDtos.add(tripDto);
			}

		} catch (Exception e) {
			System.out.println("Error" + e);
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeResultSet(rs1);
			DbConnect.closeStatement(pst);
			DbConnect.closeStatement(pst1);
			DbConnect.closeConnection(con);
		}
		return tripDetailsDtos;

	}

	public TripDetailsDto getPanicTripActionDetails(String tripId) {
		DbConnect ob = DbConnect.getInstance();
		PreparedStatement pst = null;
		PreparedStatement pst1 = null;
		ResultSet rs = null;
		ResultSet rs1 = null;
		TripDetailsDto tripDto = new TripDetailsDto();
		TripDetailsChildDto childDto = null;
		Connection con = ob.connectDB();
		ArrayList<TripDetailsChildDto> childDtos = null;
		try {
			pst = con
					.prepareStatement("select td.id tripId,d.name driverName,d.contact driverContact,vm.Company vendorName,vm.contact as "
							+ " vendorContact,veh.regNo,e.name "
							+ " escortName,e.escortClock,e.phone escortContact, (select date_time from vehicle_position vp where"
							+ " vp.tripId=td.id and vp.logstatus='danger' order by date_time desc limit 1) as date_time  from"
							+ " trip_details td,driver d,vendormaster vm,vehicles veh, "
							+ " vendor_trip_sheet_parent vtsp  left outer join escort e on vtsp.escortClock  where td.id="
							+ tripId
							+ " and"
							+ " td.id=vtsp.tripid and td.vehicle=veh.id and td.driverId=d.id and veh.vendor=vm.id and vm.id=d.vendorId");

			rs = pst.executeQuery();
			if (rs.next()) {
				tripDto = new TripDetailsDto();
				tripDto.setId(rs.getString("tripId"));
				tripDto.setDriverName(rs.getString("driverName"));
				tripDto.setDriverContact(rs.getString("driverContact"));
				tripDto.setVendorName(rs.getString("vendorName"));
				tripDto.setVendorContact(rs.getString("vendorContact"));
				tripDto.setVehicleNo(rs.getString("regNo"));
				tripDto.setEscortName(rs.getString("escortName"));
				tripDto.setEscortclock(rs.getString("escortClock"));
				tripDto.setEscortContact(rs.getString("escortContact"));
				tripDto.setTrip_time(rs.getString("date_time"));

				pst1 = con
						.prepareStatement("select e.id,e.PersonnelNo,e.gender,e.employeeFirstName,e.employeeLastName,e.displayName,e.contactNumber1,e.contactNumber2 from vendor_trip_sheet vts,employee e where vts.tripId ="
								+ tripId
								+ " and curStatus='IN' and vts.employeeId=e.id");
				rs1 = pst1.executeQuery();
				childDtos = new ArrayList<TripDetailsChildDto>();
				while (rs1.next()) {
					childDto = new TripDetailsChildDto();
					childDto.setEmployeeName(rs1.getString("displayName"));
					childDto.setEmployeeId(rs1.getString("PersonnelNo"));
					childDto.setContactNumber(rs1.getString("contactNumber1"));
					childDto.setGender(rs1.getString("gender"));
					childDtos.add(childDto);
				}
				pst1 = con
						.prepareStatement("select pa.id,e.displayName,pa.actionTime,pa.alarmCause,pa.primaryAction,e1.displayName as approvedBy,pa.stopTime from employee e,panicAction pa left outer join employee e1 on  pa.approvedBy=e1.id  where pa.primaryActiontakenBy=e.id AND pa.tripId="
								+ tripDto.getId() + "");
				rs1 = pst1.executeQuery();
				PanicDto dto = null;
				if (rs1.next()) {
					dto = new PanicDto();
					dto.setId(rs1.getString("id"));
					dto.setPrimaryActiontakenByName(rs1
							.getString("displayName"));
					dto.setPrimaryActiontakenTime(rs1.getString("actionTime"));
					dto.setAlarmCause(rs1.getString("alarmCause"));
					dto.setPrimaryAction(rs1.getString("primaryAction"));
					dto.setApprovedByName(rs1.getString("approvedBy"));
					dto.setApprovedByName(rs1.getString("stopTime"));
				}
				tripDto.setPanicdto(dto);
				tripDto.setTripDetailsChildDtoList(childDtos);
			}

		} catch (Exception e) {
			System.out.println("Error eher" + e);
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeResultSet(rs1);
			DbConnect.closeStatement(pst);
			DbConnect.closeStatement(pst1);
			DbConnect.closeConnection(con);
		}
		return tripDto;

	}

	public ArrayList<TripDetailsDto> getTripsByDateVehicle(String tripDate,
			String vehicleId) {

		DbConnect ob = null;
		Connection con = null;
		ResultSet rs = null;
		Statement st = null;
		String retVal = "";
		ArrayList<TripDetailsDto> detailsDtos = new ArrayList<TripDetailsDto>();
		TripDetailsDto dto = null;
		try {
			ob = DbConnect.getInstance();
			con = ob.connectDB();
			String query = "select id,trip_code,security_status from trip_details where trip_date='"
					+ OtherFunctions.changeDateFromatToIso(tripDate)
					+ "' and vehicle=" + vehicleId;
			st = con.createStatement();
			rs = st.executeQuery(query);
			// System.out.println("query" + query);
			while (rs.next()) {
				dto = new TripDetailsDto();
				dto.setId(rs.getString("id"));
				dto.setTrip_code(rs.getString("trip_code"));
				dto.setEscort(rs.getString("security_status"));
				detailsDtos.add(dto);
			}
		} catch (Exception e) {
			System.out.println("Error in get tirp code" + e);
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);
		}
		return detailsDtos;
	}

	public ArrayList<TripDetailsDto> tripStatus(String tripDate,
			String vendorType, int all) {
		DbConnect ob = DbConnect.getInstance();
		PreparedStatement pst = null;
		PreparedStatement pst1 = null;
		ResultSet rs = null;
		ResultSet rs1 = null;
		TripDetailsDto tripDto = null;
		TripDetailsChildDto childDto = null;
		Connection con = ob.connectDB();
		String tripDate1 = OtherFunctions.changeDateFromatToIso(tripDate);
		ArrayList<TripDetailsDto> tripDtailsDtos = new ArrayList<TripDetailsDto>();
		ArrayList<TripDetailsChildDto> childDtos = null;
		try {
			String contiString = "";
			String contiMastString = "";
			if (vendorType.equalsIgnoreCase("0")) {

			} else {
				contiString = "and tva.vendorId=" + vendorType;
				contiMastString = "and tva1.vendorId=" + vendorType;
			}
			/*
			 * String query =
			 * "select td.id,td.trip_code,trip_date,trip_log,trip_time,vtsp.escort,vtsp.status,vtsp.startTime,vtsp.stopTime,v.regNo,count(e.id)as planedmpCount,count(e0.id) as empCount,"
			 * +
			 * " count(e1.id) as femaleCount,count(e2.id) as reachedEmps,count(e3.id) as reachedFemaleEmps"
			 * +
			 * " from trip_details td,employee e,vendor_trip_sheet_parent vtsp,vehicles v,vendor_trip_sheet vts "
			 * + " left outer join " + "(select * from employee)  e0 " +
			 * " on vts.employeeId=e0.id and vts.showstatus='Show' " +
			 * " left outer join " +
			 * "(select * from employee where gender='F')  e1 " +
			 * " on vts.employeeId=e1.id and vts.showstatus='Show' " +
			 * " left outer join " +
			 * " (select distinct vts1.tripid,e.id from employee e,vendor_trip_sheet vts1 where e.id=vts1.employeeId and vts1.curStatus='OUT' and vts1.showStatus='Show' "
			 * +") e2 " + " on vts.employeeId=e2.id and vts.tripid=e2.tripid " +
			 * " left outer join " +
			 * " (select distinct vts1.tripid,e.id    from employee e,vendor_trip_sheet vts1,tripvendorassign tva where e.gender='F' and e.id=vts1.employeeId and vts1.curStatus='OUT' and vts1.showStatus='Show' "
			 * +") e3 " + " on vts.employeeId=e3.id and vts.tripid=e3.tripid " +
			 * " where td.trip_date='" + tripDate1 +
			 * "' and td.id=vtsp.tripId and vtsp.tripid= vts.tripid and vts.employeeId=e.id  "
			 * + "  and td.vehicle=v.id group by td.id";
			 */String query = "select td.id,td.trip_code,trip_date,trip_log,trip_time,vtsp.escort,vtsp.status,vtsp.startTime,vtsp.stopTime,v.regNo,"
					+ "count(e.id)as planedmpCount,count(e0.id) as empCount, count(e1.id) as femaleCount,count(e2.id) as reachedEmps,count(e3.id) as reachedFemaleEmps "
					+ "from trip_details td,employee e,vendor_trip_sheet_parent vtsp,vehicles v,tripvendorassign tva1,vendor_trip_sheet "
					+ "vts  left outer join (select * from employee)  e0 on vts.employeeId=e0.id and vts.showstatus='Show'  left outer join "
					+ "(select * from employee where gender='F')  e1 "
					+ "on vts.employeeId=e1.id and vts.showstatus='Show' "
					+ "left outer join "
					+ "(select distinct vts1.tripid,e.id from employee e,vendor_trip_sheet vts1,tripvendorassign tva where e.id=vts1.employeeId and vts1.curStatus='OUT' and vts1.showStatus='Show' and vts1.tripid=tva.tripId "
					+ contiString
					+ ") e2 "
					+ "on vts.employeeId=e2.id and vts.tripid=e2.tripid "
					+ "left outer join  "
					+ "(select distinct vts1.tripid,e.id from employee e,vendor_trip_sheet vts1,tripvendorassign tva where e.gender='F' and e.id=vts1.employeeId and vts1.curStatus='OUT' and vts1.showStatus='Show'and vts1.tripid=tva.tripId "
					+ contiString
					+ ") e3 "
					+ "on vts.employeeId=e3.id and vts.tripid=e3.tripid "
					+ "where td.trip_date='"
					+ tripDate1
					+ "' and td.id=vtsp.tripId and vtsp.tripid= vts.tripid and vts.tripid = tva1.tripId "
					+ contiMastString
					+ " and vts.employeeId=e.id and td.vehicle=v.id group by td.id";
			System.out.println(query);
			if (all == 0) {
				query += " having femaleCount>0 ";
			}

			pst = con.prepareStatement(query);
			rs = pst.executeQuery();
			while (rs.next()) {
				tripDto = new TripDetailsDto();
				tripDto.setId(rs.getString("id"));
				tripDto.setTrip_code(rs.getString("trip_code"));
				tripDto.setTrip_date(rs.getString("trip_date"));
				tripDto.setTrip_log(rs.getString("trip_log"));
				tripDto.setTrip_time(rs.getString("trip_time"));
				tripDto.setIsSecurity(rs.getString("escort"));
				if (tripDto.getIsSecurity() == null
						|| tripDto.getIsSecurity().equals("null"))
					tripDto.setIsSecurity("");

				tripDto.setStatus(rs.getString("status"));
				tripDto.setStartTime(rs.getString("startTime"));
				tripDto.setStopTime(rs.getString("stopTime"));
				tripDto.setVehicleNo(rs.getString("regNo"));
				tripDto.setEmpCount(rs.getInt("planedmpCount"));
				tripDto.setEmpInCount(rs.getInt("empCount"));
				tripDto.setLadyInCount(rs.getInt("femaleCount"));
				tripDto.setReachedempCount(rs.getInt("reachedEmps"));
				tripDto.setReachedLadyCount(rs.getInt("reachedFemaleEmps"));
				String inoutSort = "";
				if (tripDto.getTrip_log().equals("IN")) {
					inoutSort = " order by vts.inTime ";
				} else {
					inoutSort = " order by vts.outTime,vts.inTime ";
				}
				pst1 = con
						.prepareStatement("select e.personnelNo,e.displayName,e.gender,vts.inTime,vts.outTime,vts.isCorrectPos from vendor_trip_sheet vts,employee e where vts.tripId="
								+ tripDto.getId()
								+ " and e.id=vts.employeeId "
								+ inoutSort + " ");
				rs1 = pst1.executeQuery();
				childDtos = new ArrayList<TripDetailsChildDto>();
				while (rs1.next()) {
					childDto = new TripDetailsChildDto();
					childDto.setEmployeeId(rs1.getString("personnelNo"));
					childDto.setEmployeeName(rs1.getString("displayName"));
					childDto.setGender(rs1.getString("gender"));
					childDto.setInTime(rs1.getString("inTime"));
					childDto.setOutTime(rs1.getString("outTime"));
					childDto.setIsCorrectPos(rs1.getString("isCorrectPos"));
					childDtos.add(childDto);
				}
				tripDto.setTripDetailsChildDtoList(childDtos);

				tripDtailsDtos.add(tripDto);
			}

		} catch (Exception e) {
			System.out.println("Error" + e);
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeResultSet(rs1);
			DbConnect.closeStatement(pst);
			DbConnect.closeStatement(pst1);
			DbConnect.closeConnection(con);
		}
		return tripDtailsDtos;

	}

	public ArrayList<TripDetailsDto> tripStatusvendor(String tripDate,
			String vendorType, int all) {
		DbConnect ob = DbConnect.getInstance();
		PreparedStatement pst = null;
		PreparedStatement pst1 = null;
		ResultSet rs = null;
		ResultSet rs1 = null;
		TripDetailsDto tripDto = null;
		TripDetailsChildDto childDto = null;
		Connection con = ob.connectDB();
		String tripDate1 = OtherFunctions.changeDateFromatToIso(tripDate);
		ArrayList<TripDetailsDto> tripDtailsDtos = new ArrayList<TripDetailsDto>();
		ArrayList<TripDetailsChildDto> childDtos = null;
		String addvendor = "";
		String contiMastString = "";
		
		try {
						
			if (vendorType.equalsIgnoreCase("0")) {

			} else {
				addvendor = " and tva.vendorId=" + vendorType;
					}
			/*
			 * String query =
			 * "select td.id,td.trip_code,trip_date,trip_log,trip_time,vtsp.escort,vtsp.status,vtsp.startTime,vtsp.stopTime,v.regNo,count(e.id)as planedmpCount,count(e0.id) as empCount,"
			 * +
			 * " count(e1.id) as femaleCount,count(e2.id) as reachedEmps,count(e3.id) as reachedFemaleEmps"
			 * +
			 * " from trip_details td,employee e,vendor_trip_sheet_parent vtsp,vehicles v,vendor_trip_sheet vts "
			 * + " left outer join " + "(select * from employee)  e0 " +
			 * " on vts.employeeId=e0.id and vts.showstatus='Show' " +
			 * " left outer join " +
			 * "(select * from employee where gender='F')  e1 " +
			 * " on vts.employeeId=e1.id and vts.showstatus='Show' " +
			 * " left outer join " +
			 * " (select distinct vts1.tripid,e.id from employee e,vendor_trip_sheet vts1 where e.id=vts1.employeeId and vts1.curStatus='OUT' and vts1.showStatus='Show' "
			 * +") e2 " + " on vts.employeeId=e2.id and vts.tripid=e2.tripid " +
			 * " left outer join " +
			 * " (select distinct vts1.tripid,e.id    from employee e,vendor_trip_sheet vts1,tripvendorassign tva where e.gender='F' and e.id=vts1.employeeId and vts1.curStatus='OUT' and vts1.showStatus='Show' "
			 * +") e3 " + " on vts.employeeId=e3.id and vts.tripid=e3.tripid " +
			 * " where td.trip_date='" + tripDate1 +
			 * "' and td.id=vtsp.tripId and vtsp.tripid= vts.tripid and vts.employeeId=e.id  "
			 * + "  and td.vehicle=v.id group by td.id";
			 */
			String query = "select td.id,td.trip_code,trip_date,trip_log,trip_time,vtsp.escort,vtsp.status,vtsp.startTime,vtsp.stopTime,v.regNo,"
					+ "count(e.id) as planedmpCount,count(e0.id) as empCount, count(e1.id) as femaleCount,count(e2.id) as reachedEmps,count(e3.id) as reachedFemaleEmps "
					+ "from trip_details td,employee e,vendor_trip_sheet_parent vtsp,vehicles v,vendor_trip_sheet "
					+ "vts  left outer join (select * from employee)  e0 on vts.employeeId=e0.id and vts.showstatus='Show'  left outer join "
					+ "(select * from employee where gender='F')  e1 "
					+ "on vts.employeeId=e1.id and vts.showstatus='Show' "
					+ "left outer join "
					+ "(select distinct vts1.tripid,e.id from employee e,vendor_trip_sheet vts1 where e.id=vts1.employeeId and vts1.curStatus='OUT' and vts1.showStatus='Show' "
					+ ") e2 "
					+ "on vts.employeeId=e2.id and vts.tripid=e2.tripid "
					+ "left outer join  "
					+ "(select distinct vts1.tripid,e.id from employee e,vendor_trip_sheet vts1 where e.gender='F' and e.id=vts1.employeeId and vts1.curStatus='OUT' and vts1.showStatus='Show'"
					+ ") e3 "
					+ "on vts.employeeId=e3.id and vts.tripid=e3.tripid "
					+ " join tripvendorassign tva on vts.tripid=tva.tripid "
					+ "where td.trip_date='"
					+ tripDate1
					+ "' and td.id=vtsp.tripId and vtsp.tripid= vts.tripid "
					+ addvendor
					+ " and vts.employeeId=e.id and td.vehicle=v.id group by td.id";
			
			
			System.out.println("Vehicle status report"+query);
			if (all == 0) {
				query += " having femaleCount>0 ";
			}

			pst = con.prepareStatement(query);
			rs = pst.executeQuery();
			while (rs.next()) {
				tripDto = new TripDetailsDto();
				tripDto.setId(rs.getString("id"));
				tripDto.setTrip_code(rs.getString("trip_code"));
				tripDto.setTrip_date(rs.getString("trip_date"));
				tripDto.setTrip_log(rs.getString("trip_log"));
				tripDto.setTrip_time(rs.getString("trip_time"));
				tripDto.setIsSecurity(rs.getString("escort"));
				if (tripDto.getIsSecurity() == null
						|| tripDto.getIsSecurity().equals("null"))
					tripDto.setIsSecurity("");

				tripDto.setStatus(rs.getString("status"));
				tripDto.setStartTime(rs.getString("startTime"));
				tripDto.setStopTime(rs.getString("stopTime"));
				tripDto.setVehicleNo(rs.getString("regNo"));
				tripDto.setEmpCount(rs.getInt("planedmpCount"));
				tripDto.setEmpInCount(rs.getInt("empCount"));
				tripDto.setLadyInCount(rs.getInt("femaleCount"));
				tripDto.setReachedempCount(rs.getInt("reachedEmps"));
				tripDto.setReachedLadyCount(rs.getInt("reachedFemaleEmps"));
				String inoutSort = "";
				if (tripDto.getTrip_log().equals("IN")) {
					inoutSort = " order by vts.inTime ";
				} else {
					inoutSort = " order by vts.outTime,vts.inTime ";
				}
				pst1 = con
						.prepareStatement("select e.personnelNo,e.displayName,e.gender,vts.inTime,vts.outTime,vts.isCorrectPos from vendor_trip_sheet vts,employee e where vts.tripId="
								+ tripDto.getId()
								+ " and e.id=vts.employeeId "
								+ inoutSort + " ");
				rs1 = pst1.executeQuery();
				childDtos = new ArrayList<TripDetailsChildDto>();
				while (rs1.next()) {
					childDto = new TripDetailsChildDto();
					childDto.setEmployeeId(rs1.getString("personnelNo"));
					childDto.setEmployeeName(rs1.getString("displayName"));
					childDto.setGender(rs1.getString("gender"));
					childDto.setInTime(rs1.getString("inTime"));
					childDto.setOutTime(rs1.getString("outTime"));
					childDto.setIsCorrectPos(rs1.getString("isCorrectPos"));
					childDtos.add(childDto);
				}
				tripDto.setTripDetailsChildDtoList(childDtos);

				tripDtailsDtos.add(tripDto);
			}

		} catch (Exception e) {
			System.out.println("Error" + e);
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeResultSet(rs1);
			DbConnect.closeStatement(pst);
			DbConnect.closeStatement(pst1);
			DbConnect.closeConnection(con);
		}
		return tripDtailsDtos;

	}


	public ArrayList<TripDetailsChildDto> getEmployeePosition(int tripId) {
		DbConnect ob = null;
		Connection con = null;
		ResultSet rs = null;
		Statement st = null;
		String retVal = "";
		ArrayList<TripDetailsChildDto> routeDtos = new ArrayList<TripDetailsChildDto>();
		TripDetailsChildDto dto = null;
		try {
			ob = DbConnect.getInstance();
			con = ob.connectDB();
			String query = "select * from ((select l.latitude as latitude,l.longitude as longitude,0 as position,'' as employeeCode,'' as employeeName,'' as gender from site s,landmark l,trip_details td where td.id= "
					+ tripId
					+ "  and s.id=td.siteId and td.trip_log='OUT' and s.landmark=l.id ) union  (select l.latitude,l.longitude,tdc.routedOrder as position ,e.personnelNo as employeeCode,e.displayName as employeeName, e.Gender as gender  from trip_details_child tdc,employee e,landmark l where tdc.employeeId=e.id and tdc.tripId="
					+ tripId
					+ "  and tdc.landmarkId=l.id order by position) union (select l.latitude as latitude,l.longitude as longitude,100 as position,'' as employeeCode,'' as employeeName,'' as gender from site s,landmark l,trip_details td where td.id= "
					+ tripId
					+ "  and s.id=td.siteId and td.trip_log='IN' and s.landmark=l.id )) as tab order by tab.position";
			// String
			// query="select vts.*,e.displayname employeeName,e.personnelno employeeCode,e.gender from vendor_trip_sheet vts,employee e where vts.tripid="+tripId+" and vts.employeeid=e.id and showStatus='Show' order by vts.insertedOrder";
			// System.out.println("query for actual route  " + query);
			st = con.createStatement();
			rs = st.executeQuery(query);
			while (rs.next()) {
				dto = new TripDetailsChildDto();

				dto.setLatitude(rs.getString("latitude"));
				dto.setLongitude(rs.getString("longitude"));
				dto.setEmployeeId(rs.getString("employeeCode"));
				dto.setEmployeeName(rs.getString("employeeName"));
				dto.setGender(rs.getString("gender"));
				routeDtos.add(dto);
			}
		} catch (Exception e) {
			System.out.println("Error in get tirp code" + e);
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);
		}
		return routeDtos;
	}

	public ArrayList<TripDetailsChildDto> getEmployeeGetInPosition(int tripId) {
		DbConnect ob = null;
		Connection con = null;
		ResultSet rs = null;
		Statement st = null;
		String retVal = "";
		ArrayList<TripDetailsChildDto> routeDtos = new ArrayList<TripDetailsChildDto>();
		TripDetailsChildDto dto = null;
		try {
			ob = DbConnect.getInstance();
			con = ob.connectDB();
			String query = "select * from ((select l.latitude as latitude,l.longitude as longitude,0 as position,'' as employeeCode,'' as employeeName,'' as gender from site s,landmark l,trip_details td where td.id= "
					+ tripId
					+ "  and s.id=td.siteId and td.trip_log='OUT' and s.landmark=l.id ) union  (select vts.latitude,vts.longitude,tdc.routedOrder as position ,e.personnelNo as employeeCode,e.displayName as employeeName, e.Gender as gender  from trip_details_child tdc,employee e,vendor_trip_sheet vts where tdc.employeeId=e.id and tdc.tripId="
					+ tripId
					+ " and tdc.tripId=vts.tripid  and tdc.employeeid=vts.employeeid order by position)"
					+ " union (select l.latitude as latitude,l.longitude as longitude,100 as position,'' as employeeCode,'' as employeeName,'' as gender from site s,landmark l,trip_details td where td.id= "
					+ tripId
					+ "  and s.id=td.siteId and td.trip_log='IN' and s.landmark=l.id )) as tab where tab.latitude is not null order by tab.position";
			System.out.println(query);
			st = con.createStatement();
			rs = st.executeQuery(query);
			while (rs.next()) {
				dto = new TripDetailsChildDto();

				dto.setLatitude(rs.getString("latitude"));
				dto.setLongitude(rs.getString("longitude"));
				dto.setEmployeeId(rs.getString("employeeCode"));
				dto.setEmployeeName(rs.getString("employeeName"));
				dto.setGender(rs.getString("gender"));
				routeDtos.add(dto);
			}
		} catch (Exception e) {
			System.out
					.println("Error in get tirp code hhhhhhhhhhhhhhhhhhhhhhhh"
							+ e);
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);
		}
		return routeDtos;
	}

	public static String gettripCodeBytripId(String tripId) {
		DbConnect ob = null;
		Connection con = null;
		ResultSet rs = null;
		Statement st = null;
		String retVal = "";
		try {
			ob = DbConnect.getInstance();
			con = ob.connectDB();
			String query = "select trip_details.trip_code from trip_details where id="
					+ tripId;
			st = con.createStatement();
			rs = st.executeQuery(query);
			if (rs.next()) {
				retVal = rs.getString(1);
			}
		} catch (Exception e) {
			System.out.println("Error in get tirp code" + e);
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);
		}
		return retVal;
	}

	public ArrayList<TripDetailsDto> getVendorAssignedTrip(String vendorId,
			String siteId, String tripDate, String tripTime[], String tripLog,
			String src) {

		ArrayList<TripDetailsDto> tripSheet = null;
		DbConnect ob = null;
		Connection con = null;
		Statement st = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		ResultSet rs1 = null;
		try {
			String tripTimeQry = "";
			if (tripTime != null && tripTime.length > 0) {
				for (String tt : tripTime) {
					tripTimeQry = tripTimeQry + ", '" + tt + "'";
				}
				tripTimeQry = tripTimeQry.substring(1);
			}

			ob = DbConnect.getInstance();
			con = ob.connectDB();
			TripDetailsDto tripDetailsDtoObj = null;
			TripDetailsChildDto tripDetailsChildDtoObj = null;
			tripSheet = new ArrayList<TripDetailsDto>();
			ArrayList<TripDetailsChildDto> tripSheetChild = null;
			st = con.createStatement();
			// order by t.id
			String subQuery = "";
			String query = "SELECT t.id, t.vehicle, t.trip_code,t.trip_date,t.trip_time,t.trip_log,t.routeid,t.trip_code,t.vehicle_type,vt.type,t.status,t.security_status, p.logTime,t.distance,t.travelTime,t.preTripId,t.distanceFromPreTrip, t.driverId, ifnull(p.approvalStatus,'Open')  approvalStatus, p.vehicleNo, p.onTimeStatus, p.comment, p.escort, p.escortClock, t.escortId, t.escortPswd   FROM trip_details t left join vendor_trip_sheet_parent p on t.id=p.tripId ,vehicle_type vt where t.siteId=? and vt.id=t.vehicle_type and  (t.status=? or t.status=? or t.status=?) ";
			if (tripLog.equals("ALL")) {
				subQuery = " and t.trip_date='" + tripDate + "'";

			} else if (tripTime.equals("ALL")) {
				subQuery = " and t.trip_date='" + tripDate
						+ "' and t.trip_log='" + tripLog + "' ";
			} else {
				subQuery = " and t.trip_date='" + tripDate
						+ "' and t.trip_log='" + tripLog
						+ "' and t.trip_time in (" + tripTimeQry + " )";
			}
			if (src.equals("assaign")) {
				subQuery += " and t.vehicle_type is null ";
			}
			String lastQuery = " and t.id in ( select tripid from tripvendorassign where vendorId="
					+ vendorId + " ) order by t.id ";
			// System.out.println("VVVV" + query + subQuery + lastQuery);
			pst = con.prepareStatement(query + subQuery + lastQuery);
			pst.setString(1, siteId);
			pst.setString(2, "saved");
			pst.setString(3, "addsave");
			pst.setString(4, "saveedit");
			rs = pst.executeQuery();
			while (rs.next()) {
				tripDetailsDtoObj = new TripDetailsDto();
				tripDetailsDtoObj.setId(rs.getString("id"));
				tripDetailsDtoObj.setActualLogTime(rs.getString("logTime"));
				tripDetailsDtoObj.setVehicleNo(rs.getString("vehicleNo"));
				tripDetailsDtoObj.setOnTimeStatus(rs.getString("onTimeStatus"));
				tripDetailsDtoObj.setComment(rs.getString("comment"));
				tripDetailsDtoObj.setEscort(rs.getString("escort"));
				tripDetailsDtoObj.setEscortNo(rs.getString("escortClock"));
				tripDetailsDtoObj.setDriverId(rs.getString("driverId"));
				tripDetailsDtoObj.setVehicleId(rs.getString("vehicle"));

				tripDetailsDtoObj.setApprovalStatus(rs
						.getString("approvalStatus"));

				tripDetailsDtoObj.setTrip_code(rs.getString("trip_code"));
				tripDetailsDtoObj.setTrip_date(rs.getDate("trip_date")
						.toString());
				tripDetailsDtoObj.setTrip_time(rs.getString("trip_time"));
				tripDetailsDtoObj.setTrip_log(rs.getString("trip_log"));
				tripDetailsDtoObj.setRouteId(rs.getString("routeid"));
				tripDetailsDtoObj.setTrip_code(rs.getString("trip_code"));
				tripDetailsDtoObj.setVehicle(rs.getString("vehicle_type"));
				tripDetailsDtoObj.setVehicle_type(rs.getString("type"));
				tripDetailsDtoObj
						.setIsSecurity(rs.getString("security_status"));
				// tripSheetChild = null;
				tripSheetChild = new ArrayList<TripDetailsChildDto>();
				String status = rs.getString("status");
				tripDetailsDtoObj.setDistance(rs.getString("distance"));
				tripDetailsDtoObj.setTravelTime(rs.getString("travelTime"));
				tripDetailsDtoObj.setPreviousTripId(rs.getString("preTripId"));
				tripDetailsDtoObj.setDistanceFromPrevioustrip(rs
						.getFloat("distanceFromPreTrip"));
				tripDetailsDtoObj.setEscortId(rs.getString("escortId"));
				tripDetailsDtoObj.setEscortPassword(rs.getString("escortPswd"));

				query = "select tdc.employeeId,e.displayname as EmployeeName,a.area,p.place,l.id as landmarkId,l.landmark,ifnull(e.contactNumber1,'') as contactno, vt.employeeId approvedEmployee,  vt.showStatus, vt.noShowReason,tdc.time,tdc.dist from trip_details_child tdc  left  join vendor_trip_sheet vt on tdc.employeeId=vt.employeeId and tdc.tripId=vt.tripId ,employee e ,area a,place p,landmark l where e.id=tdc.employeeId and tdc.tripId="
						+ rs.getString(1)
						+ " and tdc.landmarkId=l.id and l.place=p.id and p.area=a.id order by tdc.routedOrder";

				rs1 = st.executeQuery(query);
				while (rs1.next()) {

					tripDetailsChildDtoObj = new TripDetailsChildDto();
					tripDetailsChildDtoObj.setApprovedEmployee(rs1
							.getString("approvedEmployee"));
					tripDetailsChildDtoObj.setShowStatus(rs1
							.getString("showStatus"));
					tripDetailsChildDtoObj.setReason(rs1
							.getString("noShowReason"));

					if (tripDetailsChildDtoObj.getApprovedEmployee() != null
							&& !tripDetailsChildDtoObj.getApprovedEmployee()
									.equals("")) {
						tripDetailsDtoObj.setCanTravel(true);
					}

					tripDetailsChildDtoObj.setTripId("" + rs.getInt(1));
					tripDetailsChildDtoObj.setEmployeeId(rs1
							.getString("employeeId"));
					tripDetailsChildDtoObj.setEmployeeName(rs1
							.getString("EmployeeName"));
					tripDetailsChildDtoObj.setArea(rs1.getString("area"));
					tripDetailsChildDtoObj.setPlace(rs1.getString("place"));
					tripDetailsChildDtoObj.setLandmarkId(rs1
							.getString("landmarkId"));
					tripDetailsChildDtoObj.setLandmark(rs1
							.getString("landmark"));
					tripDetailsChildDtoObj.setContactNumber(rs1
							.getString("contactno"));
					try {
						tripDetailsChildDtoObj
								.setDistance(rs1.getFloat("dist"));
						tripDetailsChildDtoObj.setTime(rs1.getString("time"));
					} catch (Exception e) {

					}
					tripSheetChild.add(tripDetailsChildDtoObj);
				}

				tripDetailsDtoObj.setTripDetailsChildDtoList(tripSheetChild);
				tripSheet.add(tripDetailsDtoObj);
				rs1.close();
			}

		} catch (Exception e) {
			DbConnect.closeConnection(con);
			System.out.println("Error in getting saved" + e);
		} finally {
			DbConnect.closeResultSet(rs, rs1);
			DbConnect.closeStatement(st, pst);
			DbConnect.closeConnection(con);
		}
		return tripSheet;
	}

	public ArrayList<TripDetailsDto> getVendorAssignedTrip1(String siteId,
			String tripDate, String tripTime[], String tripLog, String src) {

		ArrayList<TripDetailsDto> tripSheet = null;
		DbConnect ob = null;
		Connection con = null;
		Statement st = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		ResultSet rs1 = null;
		try {
			String tripTimeQry = "";
			if (tripTime != null && tripTime.length > 0) {
				for (String tt : tripTime) {
					tripTimeQry = tripTimeQry + ", '" + tt + "'";
				}
				tripTimeQry = tripTimeQry.substring(1);
			}

			ob = DbConnect.getInstance();
			con = ob.connectDB();
			TripDetailsDto tripDetailsDtoObj = null;
			TripDetailsChildDto tripDetailsChildDtoObj = null;
			tripSheet = new ArrayList<TripDetailsDto>();
			ArrayList<TripDetailsChildDto> tripSheetChild = null;
			st = con.createStatement();
			// order by t.id
			String subQuery = "";
			String query = "SELECT t.id, t.vehicle, t.trip_code,t.trip_date,t.trip_time,t.trip_log,t.routeid,t.trip_code,t.vehicle_type,vt.type,t.status,t.security_status, p.logTime,t.distance,t.travelTime,t.preTripId,t.distanceFromPreTrip, t.driverId, ifnull(p.approvalStatus,'Open')  approvalStatus, p.vehicleNo, p.onTimeStatus, p.comment, p.escort, p.escortClock, t.escortId, t.escortPswd   FROM trip_details t left join vendor_trip_sheet_parent p on t.id=p.tripId ,vehicle_type vt where t.siteId=? and vt.id=t.vehicle_type and  (t.status=? or t.status=? or t.status=?) ";
			if (tripLog.equals("ALL")) {
				subQuery = " and t.trip_date='" + tripDate + "'";

			} else if (tripTime.equals("ALL")) {
				subQuery = " and t.trip_date='" + tripDate
						+ "' and t.trip_log='" + tripLog + "' ";
			} else {
				subQuery = " and t.trip_date='" + tripDate
						+ "' and t.trip_log='" + tripLog
						+ "' and t.trip_time in (" + tripTimeQry + " )";
			}
			if (src.equals("assaign")) {
				subQuery += " and t.vehicle_type is null ";
			}
			// String lastQuery =
			// " and t.id in ( select tripid from tripvendorassign where vendorId="+
			// vendorId + " ) order by t.id ";
			// System.out.println("VVVV" + query + subQuery + lastQuery);
			pst = con.prepareStatement(query + subQuery);
			pst.setString(1, siteId);
			pst.setString(2, "saved");
			pst.setString(3, "addsave");
			pst.setString(4, "saveedit");
			rs = pst.executeQuery();
			while (rs.next()) {
				tripDetailsDtoObj = new TripDetailsDto();
				tripDetailsDtoObj.setId(rs.getString("id"));
				tripDetailsDtoObj.setActualLogTime(rs.getString("logTime"));
				tripDetailsDtoObj.setVehicleNo(rs.getString("vehicleNo"));
				tripDetailsDtoObj.setOnTimeStatus(rs.getString("onTimeStatus"));
				tripDetailsDtoObj.setComment(rs.getString("comment"));
				tripDetailsDtoObj.setEscort(rs.getString("escort"));
				tripDetailsDtoObj.setEscortNo(rs.getString("escortClock"));
				tripDetailsDtoObj.setDriverId(rs.getString("driverId"));
				tripDetailsDtoObj.setVehicleId(rs.getString("vehicle"));

				tripDetailsDtoObj.setApprovalStatus(rs
						.getString("approvalStatus"));

				tripDetailsDtoObj.setTrip_code(rs.getString("trip_code"));
				tripDetailsDtoObj.setTrip_date(rs.getDate("trip_date")
						.toString());
				tripDetailsDtoObj.setTrip_time(rs.getString("trip_time"));
				tripDetailsDtoObj.setTrip_log(rs.getString("trip_log"));
				tripDetailsDtoObj.setRouteId(rs.getString("routeid"));
				tripDetailsDtoObj.setTrip_code(rs.getString("trip_code"));
				tripDetailsDtoObj.setVehicle(rs.getString("vehicle_type"));
				tripDetailsDtoObj.setVehicle_type(rs.getString("type"));
				tripDetailsDtoObj
						.setIsSecurity(rs.getString("security_status"));
				// tripSheetChild = null;
				tripSheetChild = new ArrayList<TripDetailsChildDto>();
				String status = rs.getString("status");
				tripDetailsDtoObj.setDistance(rs.getString("distance"));
				tripDetailsDtoObj.setTravelTime(rs.getString("travelTime"));
				tripDetailsDtoObj.setPreviousTripId(rs.getString("preTripId"));
				tripDetailsDtoObj.setDistanceFromPrevioustrip(rs
						.getFloat("distanceFromPreTrip"));
				tripDetailsDtoObj.setEscortId(rs.getString("escortId"));
				tripDetailsDtoObj.setEscortPassword(rs.getString("escortPswd"));

				query = "select tdc.employeeId,e.displayname as EmployeeName,a.area,p.place,l.id as landmarkId,l.landmark,ifnull(e.contactNumber1,'') as contactno, vt.employeeId approvedEmployee,  vt.showStatus, vt.noShowReason,tdc.time,tdc.dist from trip_details_child tdc  left  join vendor_trip_sheet vt on tdc.employeeId=vt.employeeId and tdc.tripId=vt.tripId ,employee e ,area a,place p,landmark l where e.id=tdc.employeeId and tdc.tripId="
						+ rs.getString(1)
						+ " and tdc.landmarkId=l.id and l.place=p.id and p.area=a.id order by tdc.routedOrder";

				rs1 = st.executeQuery(query);
				while (rs1.next()) {

					tripDetailsChildDtoObj = new TripDetailsChildDto();
					tripDetailsChildDtoObj.setApprovedEmployee(rs1
							.getString("approvedEmployee"));
					tripDetailsChildDtoObj.setShowStatus(rs1
							.getString("showStatus"));
					tripDetailsChildDtoObj.setReason(rs1
							.getString("noShowReason"));

					if (tripDetailsChildDtoObj.getApprovedEmployee() != null
							&& !tripDetailsChildDtoObj.getApprovedEmployee()
									.equals("")) {
						tripDetailsDtoObj.setCanTravel(true);
					}

					tripDetailsChildDtoObj.setTripId("" + rs.getInt(1));
					tripDetailsChildDtoObj.setEmployeeId(rs1
							.getString("employeeId"));
					tripDetailsChildDtoObj.setEmployeeName(rs1
							.getString("EmployeeName"));
					tripDetailsChildDtoObj.setArea(rs1.getString("area"));
					tripDetailsChildDtoObj.setPlace(rs1.getString("place"));
					tripDetailsChildDtoObj.setLandmarkId(rs1
							.getString("landmarkId"));
					tripDetailsChildDtoObj.setLandmark(rs1
							.getString("landmark"));
					tripDetailsChildDtoObj.setContactNumber(rs1
							.getString("contactno"));
					try {
						tripDetailsChildDtoObj
								.setDistance(rs1.getFloat("dist"));
						tripDetailsChildDtoObj.setTime(rs1.getString("time"));
					} catch (Exception e) {

					}
					tripSheetChild.add(tripDetailsChildDtoObj);
				}

				tripDetailsDtoObj.setTripDetailsChildDtoList(tripSheetChild);
				tripSheet.add(tripDetailsDtoObj);
				rs1.close();
			}

		} catch (Exception e) {
			DbConnect.closeConnection(con);
			System.out.println("Error in getting saved" + e);
		} finally {
			DbConnect.closeResultSet(rs, rs1);
			DbConnect.closeStatement(st, pst);
			DbConnect.closeConnection(con);
		}
		return tripSheet;
	}

	public int assaginTripVehicle(ArrayList<VehicleDto> tripVehicles) {
		DbConnect ob = null;
		Connection con = null;
		PreparedStatement pst1 = null;
		PreparedStatement pst2 = null;
		int retVal = -1;
		try {
			RandomString rd = new RandomString(4);
			ob = DbConnect.getInstance();
			con = ob.connectDB();
			String updateQuery = "update trip_details set vehicle=?,driverId=?,driverPswd=ifnull(driverPswd,?) where id=? ";
			// System.out.println(updateQuery);
			pst1 = con.prepareStatement(updateQuery);
			for (VehicleDto dto : tripVehicles) {

				if (dto.getDriver() != null) {
					String password = rd.nextDriverString(dto.getTripId());// passwordGenerator.nextString();
					// System.out.println("@@ Driver password : "+password);
					pst1.setString(1, dto.getVehicleNo());
					pst1.setString(2, dto.getDriver());
					pst1.setString(3, password);
					pst1.setString(4, dto.getTripId());

					// System.out.println("nnnnn"+dto.getVehicleNo()+dto.getDriver()+dto.getTripId());
					int result = pst1.executeUpdate();
					retVal += result;
					if (result > 0) {
						SMSService sms = new SMSService();
						sms.sendPasswordToDriver(dto.getTripId());
					}
				}

			}

		} catch (Exception e) {
			System.out.println("Error in get tirp code" + e);
		} finally {
			DbConnect.closeStatement(pst1);
			DbConnect.closeStatement(pst2);
			DbConnect.closeConnection(con);
		}
		return retVal;

	}

	public int updateTravelTime(ArrayList<VehicleDto> tripVehicles) {
		DbConnect ob = null;
		Connection con = null;
		PreparedStatement pst = null;

		int retValResult = -1;

		try {
			ob = DbConnect.getInstance();
			con = ob.connectDB();
			String updateQuery = "update trip_details_child set time=? where tripId=? and routedOrder=? ";

			pst = con.prepareStatement(updateQuery);
			for (VehicleDto dto : tripVehicles) {
				int routedOrder = 0;
				if (dto.getDriver() != null) {
					//String traveltime = dto.getTraveltime();
				
						//pst.setString(1, traveltime);
						pst.setString(2, dto.getTripId());
						routedOrder = routedOrder + 1;
						pst.setInt(3, routedOrder);
						int result1 = pst.executeUpdate();
						retValResult += result1;
					

				}

			}
		}

		catch (Exception e) {
			System.out.println("Error in Update travel time" + e);
		} finally {
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);
		}
		return retValResult;

	}

	public ArrayList<TripDetailsDto> liveTripStatus(String siteId,
			String tripDate, String projectId) {
		DbConnect ob = DbConnect.getInstance();
		PreparedStatement pst = null;
		PreparedStatement pst1 = null;
		ResultSet rs = null;
		ResultSet rs1 = null;
		TripDetailsDto tripDto = null;

		Connection con = ob.connectDB();
		String tripDate1 = OtherFunctions.changeDateFromatToIso(tripDate);
		ArrayList<TripDetailsDto> tripDtailsDtos = new ArrayList<TripDetailsDto>();
		try {
			String projectFilterQuery = "";
			String query = "select DATE_FORMAT(trip_date,'%Y-%m-%d') trip_date,trip_log,trip_time,(select count(*) from trip_details td,vendor_trip_sheet_parent vtsp where vtsp.tripid=td.id and trip_date=tble.trip_date and trip_time=tble.trip_time and vtsp.status='started' and trip_log=tble.trip_log) as tripCount,count(employeeId) as empCount,count(showemp) as showempCount from(select vtp.tripid,td.trip_date,td.trip_log,td.trip_time,td.travelTime,td.distance,vt.employeeId,vt.showStatus,vt.curStatus,vts1.employeeid as showemp from  vendor_trip_sheet_parent vtp,trip_details td, vendor_trip_sheet vt left outer join  vendor_trip_sheet vts1 on  vt.tripId=vts1.tripid and vt.employeeid=vts1.employeeId  and vts1.showStatus='Show'  where td.id=vtp.tripid and vt.tripId=vtp.tripId and  vtp.status='started' ) as tble"
					+ " group by trip_date,trip_log,trip_time";
			pst = con.prepareStatement(query);
			rs = pst.executeQuery();
			while (rs.next()) {
				tripDto = new TripDetailsDto();
				tripDto.setTrip_date(rs.getString("trip_date"));
				tripDto.setTrip_log(rs.getString("trip_log"));
				tripDto.setTrip_time(rs.getString("trip_time"));
				tripDto.setTripCount(rs.getString("tripCount"));
				tripDto.setEmpInCount(rs.getInt("empCount"));
				tripDto.setReachedempCount(rs.getInt("showempCount"));
				tripDtailsDtos.add(tripDto);
			}

		} catch (Exception e) {
			System.out.println("Error" + e);
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeResultSet(rs1);
			DbConnect.closeStatement(pst);
			DbConnect.closeStatement(pst1);
			DbConnect.closeConnection(con);
		}
		return tripDtailsDtos;

	}

	public ArrayList<TripDetailsDto> liveTripStatusByTrips(String siteId,
			String tripDate, String tripTime, String tripLog, String projectId) {
		DbConnect ob = DbConnect.getInstance();
		PreparedStatement pst = null;
		PreparedStatement pst1 = null;
		ResultSet rs = null;
		ResultSet rs1 = null;
		TripDetailsDto tripDto = null;

		Connection con = ob.connectDB();
		ArrayList<TripDetailsDto> tripDtailsDtos = new ArrayList<TripDetailsDto>();
		try {
			String projectfilterQuery = "";
			if (projectId != null && !projectId.equals("")) {
				projectfilterQuery = " and  td.id in (select tripid from trip_details_child where scheduleId in (select id from employee_schedule where project="
						+ projectId + ")) ";
			}
			String query = "select td.id,td.siteid,DATE_FORMAT(td.trip_date,'%Y-%m-%d') trip_date,td.trip_code,td.trip_time,td.trip_log,td.distance,td.travelTime,count(vts.employeeId) as empCount,count(vts1.employeeId) as showempCount from vendor_trip_sheet_parent vtp,trip_details td,vendor_trip_sheet vts left outer join vendor_trip_sheet vts1 on vts.tripid=vts1.tripid and vts.employeeid=vts1.employeeid and vts1.showStatus='Show' where "
					+ " td.siteid="
					+ siteId
					+ " and td.trip_date='"
					+ tripDate
					+ "' and td.trip_time='"
					+ tripTime
					+ "' and td.trip_log='"
					+ tripLog
					+ "'  and td.id=vtp.tripid and vtp.status='started' and  vts.tripid=td.id "
					+ projectfilterQuery + " group by id";
			// System.out.println(query);
			pst = con.prepareStatement(query);
			rs = pst.executeQuery();
			while (rs.next()) {
				tripDto = new TripDetailsDto();
				tripDto.setId(rs.getString("id"));
				tripDto.setTrip_code(rs.getString("trip_code"));
				tripDto.setTrip_date(rs.getString("trip_date"));
				tripDto.setTrip_log(rs.getString("trip_log"));
				tripDto.setTrip_time(rs.getString("trip_time"));
				tripDto.setDistance(rs.getString("distance"));
				tripDto.setTravelTime(rs.getString("travelTime"));
				tripDto.setExpectedArrivalTime(getexpectedArrivalTime(rs
						.getString("id")));
				tripDto.setEmpInCount(rs.getInt("empCount"));
				tripDto.setReachedempCount(rs.getInt("showempCount"));
				tripDtailsDtos.add(tripDto);
			}

		} catch (Exception e) {
			System.out.println("Error" + e);
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeResultSet(rs1);
			DbConnect.closeStatement(pst);
			DbConnect.closeStatement(pst1);
			DbConnect.closeConnection(con);
		}
		return tripDtailsDtos;
	}

	public ArrayList<TripDetailsDto> liveTripStatusByTrips_triptimesAndStatus(
			String tripDate, String tripTimes[], String tripLog, String status) {
		DbConnect ob = DbConnect.getInstance();
		PreparedStatement pst = null;
		PreparedStatement pst1 = null;
		ResultSet rs = null;
		ResultSet rs1 = null;
		TripDetailsDto tripDto = null;

		Connection con = ob.connectDB();
		ArrayList<TripDetailsDto> tripDtailsDtos = new ArrayList<TripDetailsDto>();
		try {
			StringBuilder statusQuery = new StringBuilder();
			if (status.equalsIgnoreCase("downloaded")) {
				statusQuery.append(" and downloadStatus='downloaded' ");
			} else if (status.equalsIgnoreCase("started")) {
				statusQuery.append("  and vtp.status='started' ");
			} else if (status.equalsIgnoreCase("stopped")) {
				statusQuery.append("  and vtp.status='stopped' ");
			} else if (status.equalsIgnoreCase("initial")) {
				statusQuery.append("  and vtp.status='initial' ");
			}

			StringBuilder tripTimeQuery = new StringBuilder();
			if (tripTimes[0].equalsIgnoreCase("All")
					&& tripLog.equalsIgnoreCase("All")) {
				tripTimeQuery.append("");
			} else if (tripTimes[0].equals("All")) {
				tripTimeQuery.append(" and trip_log='").append(tripLog)
						.append("' ");

			} else {
				tripTimeQuery.append(" and trip_log='").append(tripLog)
						.append("' and trip_time in ( ");
				int i = 0;
				for (String time : tripTimes) {
					if (i++ > 0) {
						tripTimeQuery.append(",");
					}
					tripTimeQuery.append("'").append(time).append("'");
				}
				tripTimeQuery.append(" ) ");

			}

			String query = "select td.id,td.trip_date,td.trip_code,td.trip_time,td.trip_log, ifnull(vtp.distanceCovered,'0.00') distance,ifnull(vtp.timeElapsed,0) travelTime,count(vts.employeeId) as empCount,count(vts1.employeeId) as showempCount, ifnull(d.name,'') driverName, ifnull(vcl.regNo,'') regNo from vendor_trip_sheet_parent vtp,trip_details td left join driver d on td.driverId=d.id left join vehicles vcl on td.vehicle=vcl.id,vendor_trip_sheet vts left outer join vendor_trip_sheet vts1 on vts.tripid=vts1.tripid and vts.employeeid=vts1.employeeid and vts1.showStatus='Show' where td.trip_date='"
					+ tripDate
					+ "' "
					+ tripTimeQuery.toString()
					+ " and td.id=vtp.tripid "
					+ statusQuery.toString()
					+ " and  vts.tripid=td.id  and vtp.downloadStatus='downloaded' group by id";

			System.out.println(query);

			pst = con.prepareStatement(query);

			rs = pst.executeQuery();
			while (rs.next()) {
				tripDto = new TripDetailsDto();
				tripDto.setId(rs.getString("id"));
				tripDto.setTrip_code(rs.getString("trip_code"));
				tripDto.setTrip_date(rs.getString("trip_date"));
				tripDto.setTrip_log(rs.getString("trip_log"));
				tripDto.setTrip_time(rs.getString("trip_time"));
				tripDto.setDistance(rs.getString("distance"));
				tripDto.setDistanceDouble(rs.getDouble("distance"));
				tripDto.setTravelTime(rs.getString("travelTime"));
				tripDto.setEmpInCount(rs.getInt("empCount"));
				tripDto.setReachedempCount(rs.getInt("showempCount"));
				tripDto.setTravelTimeInDate(rs.getLong("travelTime"));
				tripDto.setDriverName(rs.getString("driverName"));
				tripDto.setVehicleNo(rs.getString("regNo"));
				tripDtailsDtos.add(tripDto);
			}

		} catch (Exception e) {
			System.out.println("Error" + e);
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeResultSet(rs1);
			DbConnect.closeStatement(pst);
			DbConnect.closeStatement(pst1);
			DbConnect.closeConnection(con);
		}
		return tripDtailsDtos;
	}

	/* total trips */
	public ArrayList<TripDetailsDto> totalTripStatus(String tripDate,
			String tripTimes[], String tripLog, String status) {
		DbConnect ob = DbConnect.getInstance();
		PreparedStatement pst = null;
		PreparedStatement pst1 = null;
		ResultSet rs = null;
		ResultSet rs1 = null;
		TripDetailsDto tripDto = null;

		Connection con = ob.connectDB();
		ArrayList<TripDetailsDto> tripDtailsDtos = new ArrayList<TripDetailsDto>();
		try {
			StringBuilder statusQuery = new StringBuilder();

			StringBuilder tripTimeQuery = new StringBuilder();
			if (tripTimes[0].equalsIgnoreCase("All")
					&& tripLog.equalsIgnoreCase("All")) {
				tripTimeQuery.append("");
			} else if (tripTimes[0].equals("All")) {
				tripTimeQuery.append(" and trip_log='").append(tripLog)
						.append("' ");

			} else {
				tripTimeQuery.append(" and trip_log='").append(tripLog)
						.append("' and trip_time in ( ");
				int i = 0;
				for (String time : tripTimes) {
					if (i++ > 0) {
						tripTimeQuery.append(",");
					}
					tripTimeQuery.append("'").append(time).append("'");
				}
				tripTimeQuery.append(" ) ");

			}
			String query = "";
			if (status != null && status.equalsIgnoreCase("not downloaded")) {
				query = "/*not downloaded */ select td.id,td.trip_date,td.trip_code,td.trip_time,td.trip_log, 0  distance, 0 travelTime, ifnull(vcl.regNo,'') regNo, ifnull(drv.name,'') driverName  from trip_details td left join vehicles vcl on td.vehicle=vcl.id left join driver drv on drv.id=td.driverId  where td.status in ('saved', 'addsave', 'saveedit') and td.id not in (select tripId from vendor_trip_sheet_parent vtp where vtp.downloadStatus='downloaded' ) and td.trip_date='"
						+ tripDate + "' " + tripTimeQuery.toString();
			} else {
				query = "select td.id,td.trip_date,td.trip_code,td.trip_time,td.trip_log, 0  distance, 0 travelTime, ifnull(vcl.regNo,'') regNo, ifnull(drv.name,'') driverName  from trip_details td left join vehicles vcl on td.vehicle=vcl.id left join driver drv on drv.id=td.driverId where td.status in ('saved', 'addsave', 'saveedit') and td.trip_date='"
						+ tripDate + "' " + tripTimeQuery.toString();
			}
			System.out.println("Total query :" + query);
			pst = con.prepareStatement(query);
			rs = pst.executeQuery();
			while (rs.next()) {
				tripDto = new TripDetailsDto();
				tripDto.setId(rs.getString("id"));
				tripDto.setTrip_code(rs.getString("trip_code"));
				tripDto.setTrip_date(rs.getString("trip_date"));
				tripDto.setTrip_log(rs.getString("trip_log"));
				tripDto.setTrip_time(rs.getString("trip_time"));
				tripDto.setDriverName(rs.getString("driverName"));
				tripDto.setVehicleNo(rs.getString("regNo"));

				tripDtailsDtos.add(tripDto);
			}

		} catch (Exception e) {
			System.out.println("Error" + e);
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeResultSet(rs1);
			DbConnect.closeStatement(pst);
			DbConnect.closeStatement(pst1);
			DbConnect.closeConnection(con);
		}
		return tripDtailsDtos;
	}

	public ArrayList<TripDetailsChildDto> liveTripStatusByEmps(String tripId,
			String projectId) {
		DbConnect ob = DbConnect.getInstance();
		PreparedStatement pst = null;
		PreparedStatement pst1 = null;
		ResultSet rs = null;
		ResultSet rs1 = null;
		TripDetailsDto tripDto = null;

		Connection con = ob.connectDB();
		ArrayList<TripDetailsChildDto> tripChildDtos = new ArrayList<TripDetailsChildDto>();
		TripDetailsChildDto tripChildDto = null;
		try {
			String query = "select e.id,e.displayName,a.area,p.place,l.landmark,vts.showStatus,vts.curStatus  from vendor_trip_sheet vts,employee e,area a,place p,landmark l,trip_details_child ts where vts.tripid="
					+ tripId
					+ " and ts.tripid=vts.tripid and ts.employeeid=vts.employeeid and vts.employeeid=e.id and ts.landmarkid=l.id and l.place=p.id and p.area=a.id";
			if (projectId != null && !projectId.equals("")) {
				query += " and ts.scheduleid in (select id from employee_schedule where project ="
						+ projectId + ")";
			}
			pst = con.prepareStatement(query);
			rs = pst.executeQuery();
			while (rs.next()) {
				tripChildDto = new TripDetailsChildDto();
				tripChildDto.setEmployeeId(rs.getString("id"));
				tripChildDto.setEmployeeName(rs.getString("displayName"));
				tripChildDto.setArea(rs.getString("area"));
				tripChildDto.setPlace(rs.getString("place"));
				tripChildDto.setLandmark(rs.getString("landmark"));
				tripChildDto.setShowStatus(rs.getString("showStatus"));
				tripChildDto.setStatus(rs.getString("curStatus"));
				tripChildDtos.add(tripChildDto);
			}

		} catch (Exception e) {
			System.out.println("Error" + e);
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeResultSet(rs1);
			DbConnect.closeStatement(pst);
			DbConnect.closeStatement(pst1);
			DbConnect.closeConnection(con);
		}
		return tripChildDtos;

	}

	public int authenticateEmployee(String tripId, String empCode,
			String passwpord) {
		DbConnect ob = null;
		Connection con = null;
		PreparedStatement pst = null;
		ResultSet rs = null;

		int retVal = 0;
		try {
			ob = DbConnect.getInstance();

			con = ob.connectDB();
			String query = "select * from vendor_trip_sheet vts where vts.tripId="
					+ tripId
			/*
			 * +
			 * " and vts.employeeId= (select id from employee where personnelno='"
			 * + passwpord +"' and id=" + empCode+ ")"; + " and vts.keypin='" +
			 * passwpord + "'"
			 */;
			// System.out.println(""+query);
			pst = con.prepareStatement(query);
			rs = pst.executeQuery();

			if (rs.next()) {
				retVal = 1;
			}

		} catch (Exception e) {
			System.out.println("Error in get tirp code" + e);
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);
		}
		return retVal;

	}

	public ArrayList<TripDetailsDto> getRoutingSummary(String siteId,
			String tripDate, String tripLog, String tripTime) {
		DbConnect ob = DbConnect.getInstance();
		Connection con = ob.connectDB();
		PreparedStatement pst = null;
		ResultSet rs = null;
		PreparedStatement pst1 = null;
		ResultSet rs1 = null;
		ArrayList<TripDetailsDto> tripSummary = new ArrayList<TripDetailsDto>();
		TripDetailsDto dto = null;
		HashSet<String> vehicleTypes = new HashSet<String>();
		try {
			String subQuery = "";

			if (!tripLog.equalsIgnoreCase("all")) {
				subQuery = " and td1.trip_log='" + tripLog + "' ";
				if (!tripTime.equalsIgnoreCase("all")) {
					subQuery += " and  td1.trip_time='" + tripTime + "' ";
				}
			}
			String query = "select count(id) tripCount,siteid,trip_date,trip_time,trip_log,sum(empcount) empcount,sum(secCount) secCount from (select td.siteid,td.id,td.trip_date,td.trip_time,td.trip_log,(select count(*) from trip_details td1 where td1.id=td.id and td1.security_status='yes') as secCount,(select count(*) from trip_details_child tdc where tdc.tripid=td.id) as empcount from trip_details td,(select distinct siteid,trip_date,trip_time,trip_log from trip_details td1 where td1.siteId="
					+ siteId
					+ " and  td1.trip_date='"
					+ tripDate
					+ "' "
					+ subQuery
					+ " ) as  subtable where subtable.siteid=td.siteid and subtable.trip_date=td.trip_date and subtable.trip_time=td.trip_time and subtable.trip_log=td.trip_log)as alltable group by  trip_date,trip_time,trip_log";
			String query1 = "";
			pst = con.prepareStatement(query);
			System.out.println(query);
			rs = pst.executeQuery();
			ArrayList<VehicleTypeDto> vehicles = null;
			VehicleTypeDto vehicle = null;
			while (rs.next()) {
				System.out.println("IN WHILEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE");
				dto = new TripDetailsDto();
				dto.setTrip_date(rs.getString("trip_date"));
				dto.setTrip_time(rs.getString("trip_time"));
				dto.setTrip_log(rs.getString("trip_log"));
				dto.setEmpInCount(rs.getInt("empcount"));
				dto.setTripCount(rs.getString("tripCount"));
				dto.setSecCount(rs.getInt("secCount"));
				query1 = "select vt.id,vt.type,count(vehicle_type) as vehiclecount from vehicle_type vt ,trip_details td where td.vehicle_type=vt.id and td.siteid="
						+ rs.getString("siteid")
						+ " and td.trip_date='"
						+ rs.getString("trip_date")
						+ "' and td.trip_time='"
						+ rs.getString("trip_time")
						+ "' and trip_log='"
						+ rs.getString("trip_log") + "' group by vt.id,vt.type";
				System.out.println(query1);
				pst1 = con.prepareStatement(query1);
				rs1 = pst1.executeQuery();
				vehicles = new ArrayList<VehicleTypeDto>();
				while (rs1.next()) {
					vehicle = new VehicleTypeDto();
					vehicle.setId(rs1.getInt("id"));
					vehicle.setType(rs1.getString("type"));
					vehicle.setCount(rs1.getInt("vehiclecount"));
					vehicles.add(vehicle);
					vehicleTypes.add(rs1.getString("type"));
				}
				dto.setVehicles(vehicles);

				tripSummary.add(dto);
			}
			tripSummary.get(0).setVehicleTypes(vehicleTypes);

		} catch (Exception e) {
			System.out.println("Error in trip summary dao" + e);
		} finally {
			DbConnect.closeResultSet(rs, rs1);
			DbConnect.closeStatement(pst, pst1);
			DbConnect.closeConnection(con);
		}
		return tripSummary;
	}

	public String getexpectedArrivalTime(String tripId) {
		DbConnect ob = DbConnect.getInstance();
		Connection con = ob.connectDB();
		PreparedStatement pst = null;
		ResultSet rs = null;
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());

		try {
			String query = "select lastTime,distance,speed from (select lastTime,sourceLandmark,destLandmark,(select speedpkm from timeSloat where  time_format(curtime(),'%h:%m')  >=startTime and endTime>time_format(curtime(),'%h:%m')) as speed from ((SELECT vts.employeeId,vts.outTime as lastTime,tdc.landmarkid as sourceLandmark,tdc1.landmarkid as destLandmark,vts1.insertedOrder as insertedOrder  FROM trip_details_child tdc,trip_details td,vendor_trip_sheet vts,vendor_trip_sheet vts1,trip_details_child tdc1 where td.id="
					+ tripId
					+ " and td.id=vts.tripid and td.id=vts1.tripid and vts.tripid=tdc.tripid and vts1.tripid=tdc1.tripid and vts.employeeId =tdc.employeeid   and vts1.employeeId =tdc1.employeeid  and  vts.showStatus='Show' and vts1.showStatus='Show'  and td.trip_log='OUT' )  union (SELECT vts.employeeId,vts.inTime as lastTime,tdc.landmarkid as sourceLandmark,s.landmark as destLandmark,0 insertedOrder FROM trip_details_child tdc,trip_details td,vendor_trip_sheet vts,vendor_trip_sheet vts1,site s where td.id="
					+ tripId
					+ " and td.id=vts.tripid  and vts.tripid=tdc.tripid and td.siteid=s.id and vts.employeeId =tdc.employeeid  and  vts.showStatus='Show' and vts1.showStatus='Show'  and td.trip_log='IN') ) as table1 order by lastTime,insertedOrder desc limit 1 ) as outerTable, distchart dc where (dc.srcid=outerTable.sourceLandmark and dc.destid=outerTable.destLandmark ) or (dc.destid=outerTable.sourceLandmark and dc.srcid=outerTable.destLandmark )";
			System.out.println(query);
			pst = con.prepareStatement(query);
			rs = pst.executeQuery();
			if (rs.next()) {
				System.out.println("heeeeeeeeer");
				String time = rs.getString("lastTime");
				System.out.println(time);
				cal.set(Calendar.HOUR_OF_DAY,
						Integer.parseInt(time.split(":")[0]));
				cal.set(Calendar.MINUTE, Integer.parseInt(time.split(":")[1]));
				int runningTime = (int) ((rs.getFloat("distance") / rs
						.getInt("speed")) * 60);
				cal.add(Calendar.MINUTE, runningTime);
			}

		} catch (Exception e) {
			System.out.println("Error in trip summary dao" + e);
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);
		}
		return (cal.get(Calendar.HOUR_OF_DAY) + ":" + cal.get(Calendar.MINUTE));
	}

	public ArrayList<TripDetailsDto> getTripSheetNotSetVendorSaved(
			String tripDate, String tripLog, String siteId, String tripTime) {
		ArrayList<TripDetailsDto> tripSheet = null;
		DbConnect ob = null;
		Connection con = null;
		Statement st = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		ResultSet rs1 = null;
		try {
			ob = DbConnect.getInstance();
			con = ob.connectDB();
			TripDetailsDto tripDetailsDtoObj = null;
			TripDetailsChildDto tripDetailsChildDtoObj = null;
			tripSheet = new ArrayList<TripDetailsDto>();
			ArrayList<TripDetailsChildDto> tripSheetChild = null;
			st = con.createStatement();
			// order by t.id
			String subQuery = "";
			String query = "SELECT t.id,t.trip_code,t.trip_date,t.trip_time,t.trip_log,t.routeid,t.trip_code,vt.type,t.status,t.security_status, p.logTime,t.distance,t.travelTime,t.preTripId,t.distanceFromPreTrip, ifnull(p.approvalStatus,'Open')  approvalStatus, p.vehicleNo, p.onTimeStatus, p.comment, p.escort, p.escortClock   FROM trip_details t left join vendor_trip_sheet_parent p on t.id=p.tripId ,vehicle_type vt where t.siteId=? and vt.id=t.vehicle_type and  (t.status=? or t.status=? or t.status=?) ";
			if (tripLog.equals("ALL")) {
				subQuery = " and t.trip_date='" + tripDate + "'";

			} else if (tripTime.equals("ALL")) {
				subQuery = " and t.trip_date='" + tripDate
						+ "' and t.trip_log='" + tripLog + "' ";
			} else {
				subQuery = " and t.trip_date='" + tripDate
						+ "' and t.trip_log='" + tripLog
						+ "' and t.trip_time='" + tripTime + "'";
			}
			String lastQuery = " and t.id not in (select tripid from tripvendorassign) ";
			System.out.println("HHHHHHH" + query + subQuery + lastQuery);
			pst = con.prepareStatement(query + subQuery + lastQuery);
			pst.setString(1, siteId);
			pst.setString(2, "saved");
			pst.setString(3, "addsave");
			pst.setString(4, "saveedit");
			rs = pst.executeQuery();
			while (rs.next()) {
				tripDetailsDtoObj = new TripDetailsDto();
				tripDetailsDtoObj.setId(rs.getString("id"));
				tripDetailsDtoObj.setActualLogTime(rs.getString("logTime"));
				tripDetailsDtoObj.setVehicleNo(rs.getString("vehicleNo"));
				tripDetailsDtoObj.setOnTimeStatus(rs.getString("onTimeStatus"));
				tripDetailsDtoObj.setComment(rs.getString("comment"));
				tripDetailsDtoObj.setEscort(rs.getString("escort"));
				tripDetailsDtoObj.setEscortNo(rs.getString("escortClock"));

				tripDetailsDtoObj.setApprovalStatus(rs
						.getString("approvalStatus"));

				tripDetailsDtoObj.setTrip_code(rs.getString("trip_code"));
				tripDetailsDtoObj.setTrip_date(rs.getDate("trip_date")
						.toString());
				tripDetailsDtoObj.setTrip_time(rs.getString("trip_time"));
				tripDetailsDtoObj.setTrip_log(rs.getString("trip_log"));
				tripDetailsDtoObj.setRouteId(rs.getString("routeid"));
				tripDetailsDtoObj.setTrip_code(rs.getString("trip_code"));
				tripDetailsDtoObj.setVehicle_type(rs.getString("type"));
				tripDetailsDtoObj
						.setIsSecurity(rs.getString("security_status"));
				// tripSheetChild = null;
				tripSheetChild = new ArrayList<TripDetailsChildDto>();
				String status = rs.getString("status");
				tripDetailsDtoObj.setDistance(rs.getString("distance"));
				tripDetailsDtoObj.setTravelTime(rs.getString("travelTime"));
				tripDetailsDtoObj.setPreviousTripId(rs.getString("preTripId"));
				tripDetailsDtoObj.setDistanceFromPrevioustrip(rs
						.getFloat("distanceFromPreTrip"));

				query = "select tdc.employeeId,e.displayname as EmployeeName,a.area,p.place,l.id as landmarkId,l.landmark,ifnull(e.contactNumber1,'') as contactno, vt.employeeId approvedEmployee,  vt.showStatus, vt.noShowReason,tdc.time,tdc.dist from trip_details_child tdc  left  join vendor_trip_sheet vt on tdc.employeeId=vt.employeeId and tdc.tripId=vt.tripId ,employee e ,area a,place p,landmark l where e.id=tdc.employeeId and tdc.tripId="
						+ rs.getString(1)
						+ " and tdc.landmarkId=l.id and l.place=p.id and p.area=a.id order by tdc.routedOrder";

				rs1 = st.executeQuery(query);
				while (rs1.next()) {

					tripDetailsChildDtoObj = new TripDetailsChildDto();
					tripDetailsChildDtoObj.setApprovedEmployee(rs1
							.getString("approvedEmployee"));
					tripDetailsChildDtoObj.setShowStatus(rs1
							.getString("showStatus"));
					tripDetailsChildDtoObj.setReason(rs1
							.getString("noShowReason"));

					if (tripDetailsChildDtoObj.getApprovedEmployee() != null
							&& !tripDetailsChildDtoObj.getApprovedEmployee()
									.equals("")) {
						tripDetailsDtoObj.setCanTravel(true);
					}

					tripDetailsChildDtoObj.setTripId("" + rs.getInt(1));
					tripDetailsChildDtoObj.setEmployeeId(rs1
							.getString("employeeId"));
					tripDetailsChildDtoObj.setEmployeeName(rs1
							.getString("EmployeeName"));
					tripDetailsChildDtoObj.setArea(rs1.getString("area"));
					tripDetailsChildDtoObj.setPlace(rs1.getString("place"));
					tripDetailsChildDtoObj.setLandmarkId(rs1
							.getString("landmarkId"));
					tripDetailsChildDtoObj.setLandmark(rs1
							.getString("landmark"));
					tripDetailsChildDtoObj.setContactNumber(rs1
							.getString("contactno"));
					try {
						tripDetailsChildDtoObj
								.setDistance(rs1.getFloat("dist"));
						tripDetailsChildDtoObj.setTime(rs1.getString("time"));
					} catch (Exception e) {

					}
					tripSheetChild.add(tripDetailsChildDtoObj);
				}

				tripDetailsDtoObj.setTripDetailsChildDtoList(tripSheetChild);
				tripSheet.add(tripDetailsDtoObj);
				rs1.close();
			}

		} catch (Exception e) {
			DbConnect.closeConnection(con);
			System.out.println("Error in getting saved" + e);
		} finally {
			DbConnect.closeResultSet(rs, rs1);
			DbConnect.closeStatement(st, pst);
			DbConnect.closeConnection(con);
		}
		return tripSheet;
	}

	// get tracked tripSheet by tripId
	public TripDetailsDto getTrackedTripSheetByTripId(String tripId) {

		DbConnect ob = null;
		Connection con = null;

		ob = DbConnect.getInstance();
		con = ob.connectDB();
		TripDetailsDto tripDetailsDtoObj = null;
		TripDetailsChildDto tripDetailsChildDtoObj = null;

		ArrayList<TripDetailsChildDto> tripSheetChild = null;
		PreparedStatement pst = null;
		Statement st = null;
		ResultSet rs = null;
		ResultSet rs1 = null;
		try {
			st = con.createStatement();
			String query = "";
			String mobileFields = " ifnull(p.vehicle_imei,'') vehicle_imei, ifnull(p.stopTime,'')stopTime  , ifnull( p.status,'') vStatus , ifnull(p.startTime,'') startTime  , ifnull(p.authenticatedBy, '') authenticatedBy, ifnull(p.distanceCovered,0) distanceCovered, ifnull(p.timeElapsed,'') timeElapsed, ifnull(p.downloadStatus,'') downloadStatus  ";
			pst = con
					.prepareStatement("SELECT t.siteId, t.id, t.vehicle_type, t.trip_code,t.trip_date,t.trip_time,t.trip_log,t.routeid,t.trip_code,vt.type,t.status,t.security_status, p.logTime, ifnull(p.approvalStatus,'Open')  approvalStatus, p.vehicleNo, ifnull(p.comment,'') comment, ifnull(p.escort,'') escort, ifnull(p.escortClock,'') escortClock, ifnull(t.distance,0) distance, "
							+ mobileFields
							+ "    FROM trip_details t left join vendor_trip_sheet_parent p on t.id=p.tripId ,vehicle_type vt where t.id=?   ");
			pst.setString(1, tripId);
			System.out
					.println(" Trip sheet : SELECT t.siteId, t.id,t.vehicle_type, t.trip_code,t.trip_date,t.trip_time,t.trip_log,t.routeid,t.trip_code,vt.type,t.status,t.security_status, p.logTime, ifnull(p.approvalStatus,'Open')  approvalStatus, p.vehicleNo, ifnull(p.comment,'') comment, ifnull(p.escort,'') escort, ifnull(p.escortClock,'') escortClock, ifnull(t.distance,0) distance, "
							+ mobileFields
							+ "    FROM trip_details t left join vendor_trip_sheet_parent p on t.id=p.tripId ,vehicle_type vt where t.id="
							+ tripId);
			rs = pst.executeQuery();
			if (rs.next()) {
				tripDetailsDtoObj = new TripDetailsDto();
				tripDetailsDtoObj.setId(rs.getString("id"));
				tripDetailsDtoObj.setSiteId(rs.getString("siteId"));
				tripDetailsDtoObj.setActualLogTime(rs.getString("logTime"));
				tripDetailsDtoObj.setVehicleNo(rs.getString("vehicleNo"));

				tripDetailsDtoObj.setApprovalStatus(rs
						.getString("approvalStatus"));
				tripDetailsDtoObj.setComment(rs.getString("comment"));
				tripDetailsDtoObj.setEscort(rs.getString("escort"));
				tripDetailsDtoObj.setEscortNo(rs.getString("escortClock"));
				tripDetailsDtoObj.setTrip_code(rs.getString("trip_code"));
				tripDetailsDtoObj.setTrip_date(rs.getDate("trip_date")
						.toString());
				tripDetailsDtoObj.setDistance(rs.getString("distance"));
				tripDetailsDtoObj.setTrip_time(rs.getString("trip_time"));

				tripDetailsDtoObj.setTrip_log(rs.getString("trip_log"));
				tripDetailsDtoObj.setRouteId(rs.getString("routeid"));
				tripDetailsDtoObj.setTrip_code(rs.getString("trip_code"));
				tripDetailsDtoObj.setVehicle_type(rs.getString("type"));
				tripDetailsDtoObj.setVehicle(rs.getString("vehicle_type"));
				tripDetailsDtoObj
						.setIsSecurity(rs.getString("security_status"));
				tripDetailsDtoObj.setComments(getTripComments(tripDetailsDtoObj
						.getId()));
				tripDetailsDtoObj.setDistanceCovered(rs
						.getDouble("distanceCovered"));

				// tripSheetChild = null;
				tripSheetChild = new ArrayList<TripDetailsChildDto>();
				String status = rs.getString("status");
				query = "select tdc.employeeId,e.displayname as EmployeeName, e.personnelNo,a.area,p.place,l.id as landmarkId,l.landmark,e.contactNumber1 as contactno, vt.employeeId approvedEmployee, vt.showStatus,vt.noShowReason from trip_details_child tdc  left  join vendor_trip_sheet vt on tdc.employeeId=vt.employeeId and tdc.tripId=vt.tripId left outer join landmark l on  tdc.landmarkid=l.id left outer join place p on  l.place=p.id left outer join area a on  p.area=a.id,employee e  where e.id=tdc.employeeId and tdc.tripId="
						+ rs.getString(1) + " order by tdc.routedOrder";
				rs1 = st.executeQuery(query);
				while (rs1.next()) {

					tripDetailsChildDtoObj = new TripDetailsChildDto();
					tripDetailsChildDtoObj.setApprovedEmployee(rs1
							.getString("approvedEmployee"));
					tripDetailsChildDtoObj.setShowStatus(rs1
							.getString("showStatus"));
					tripDetailsChildDtoObj.setReason(rs1
							.getString("noShowReason"));
					if (tripDetailsChildDtoObj.getApprovedEmployee() != null
							&& !tripDetailsChildDtoObj.getApprovedEmployee()
									.equals("")) {
						tripDetailsDtoObj.setCanTravel(true);
					}

					tripDetailsChildDtoObj.setTripId("" + rs.getInt(1));
					tripDetailsChildDtoObj.setEmployeeId(rs1
							.getString("employeeId"));
					tripDetailsChildDtoObj.setEmployeeName(rs1
							.getString("EmployeeName"));
					tripDetailsChildDtoObj.setPersonnelNo(rs1
							.getString("personnelNo"));
					tripDetailsChildDtoObj.setArea(rs1.getString("area"));
					tripDetailsChildDtoObj.setPlace(rs1.getString("place"));
					tripDetailsChildDtoObj.setLandmarkId(rs1
							.getString("landmarkId"));
					tripDetailsChildDtoObj.setLandmark(rs1
							.getString("landmark"));
					tripDetailsChildDtoObj.setContactNumber(rs1
							.getString("contactno"));

					tripSheetChild.add(tripDetailsChildDtoObj);
				}

				tripDetailsDtoObj.setTripDetailsChildDtoList(tripSheetChild);

				System.out.println(" early log time :"
						+ tripDetailsDtoObj.getActualLogTime() + " ->"
						+ rs.getString("logTime"));
				System.out.println(" late log time :"
						+ tripDetailsDtoObj.getTrip_time() + " ->"
						+ rs.getString("trip_time"));
				rs1.close();

			}

		} catch (Exception e) {
			System.out.println("Error in gettrackedtrip by tripId " + e);
		} finally {
			DbConnect.closeResultSet(rs, rs1);
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);
		}
		return tripDetailsDtoObj;
	}

	public ArrayList<TripDetailsDto> getTrackedTripSheetBasedOnStatus(
			String tripDate, String tripLog, String siteId, String tripTime[],
			String approvalStatus[], String personnelNo, String vendor) {

		ArrayList<TripDetailsDto> tripSheet = null;
		DbConnect ob = null;
		Connection con = null;

		ob = DbConnect.getInstance();
		con = ob.connectDB();
		TripDetailsDto tripDetailsDtoObj = null;
		TripDetailsChildDto tripDetailsChildDtoObj = null;
		tripSheet = new ArrayList<TripDetailsDto>();
		ArrayList<TripDetailsChildDto> tripSheetChild = null;
		PreparedStatement pst = null;
		Statement st = null;
		ResultSet rs = null;
		ResultSet rs1 = null;
		try {
			st = con.createStatement();
			String query = "";
			String employeeSearchQuery = "";
			String vendorSearchQuery = "";
			if (OtherFunctions.isEmpty(personnelNo) == false) {
				// employeeSearchQuery=" and t.id in (select c.tripId from trip_details_child c join employee e on c.employeeId=e.id where e.PersonnelNo="
				// + personnelNo + ")";
				// and (t.id in (select c.tripId from trip_details_child c join
				// employee e on c.employeeId=e.id where e.PersonnelNo='" +
				// personnelNo +
				// "'  and c.status ='saved' ) or t.id in (select m.tripId from trip_details_modified m join employee e on m.empid=e.id where e.PersonnelNo='"
				// + personnelNo + "' and m.status in ('saveedit','addsave')) )
				// employeeSearchQuery =
				// " and (t.id in (select c.tripId from trip_details_child c join employee e on c.employeeId=e.id where e.PersonnelNo=?  and t.status ='saved' ) or t.id in (select m.tripId from trip_details_modified m join employee e on m.empid=e.id where e.PersonnelNo=? and t.status in ('saveedit','addsave')) ) ";
				employeeSearchQuery = " and t.id in (select c.tripId from trip_details_child c join employee e on c.employeeId=e.id where e.PersonnelNo=? )";
			}
			if (OtherFunctions.isEmpty(vendor) == false) {
				vendorSearchQuery = " (select vm.id, tva.tripId from vendor vdr join vendorMaster vm on vdr.vendorMaster=vm.id join tripvendorassign tva on tva.vendorId=vm.id where empLinkId=1) vdr on td.id=vdr.tripid  ";
			}
			String approvalStatusQuery = "";
			if (approvalStatus != null && approvalStatus.length > 0) {
				approvalStatusQuery = " and (";
				for (String status : approvalStatus) {
					if (!approvalStatusQuery.equals(" and (")) {
						approvalStatusQuery += "  or ";

					}
					if (status == null || status.equals("")
							|| status.equals("Open")) {
						approvalStatusQuery += " ( p.approvalStatus is NULL or p.approvalStatus='' or p.approvalStatus='Open' )";
					} else {
						approvalStatusQuery += "     p.approvalStatus='"
								+ status + "' ";
					}
				}

				if (approvalStatusQuery == null
						|| approvalStatusQuery.equals(" and (")
						|| approvalStatusQuery.equals("")) {
					approvalStatusQuery = "";

				} else {
					approvalStatusQuery += " ) ";
				}
			}

			if (tripLog.equals("ALL")) {
				pst = con
						.prepareStatement("SELECT t.id,t.trip_code,t.trip_date,t.trip_time,t.trip_log,t.routeid,t.trip_code,vt.type,t.status,t.security_status, p.logTime, ifnull(p.approvalStatus,'Open')  approvalStatus,  v.regNo as vehicleNo, ifnull(p.status,'initial') trackingStatus, ifnull(p.comment,'') comment, ifnull(p.escort,'') escort, ifnull(p.escortClock,'') escortClock, ifnull(t.distance,0) distance, ifnull(p.manualDistance,0) manualDistance, ifnull(p.driverContact,drv.contact) driverContact    FROM trip_details t  join vehicles v on t.vehicle=v.id left join driver drv on t.driverId=drv.id left join vendor_trip_sheet_parent p on t.id=p.tripId "
								+ vendorSearchQuery
								+ " ,vehicle_type vt where t.siteId=? and (t.status=? or t.status=? or t.status=?)and vt.id=t.vehicle_type and t.trip_date=?   "
								+ approvalStatusQuery
								+ employeeSearchQuery
								+ "  order by t.id");
				pst.setString(1, siteId);
				pst.setString(2, "saved");
				pst.setString(3, "addsave");
				pst.setString(4, "saveedit");
				pst.setString(5, tripDate);
				String printQuery = "SELECT t.id,t.trip_code,t.trip_date,t.trip_time,t.trip_log,t.routeid,t.trip_code,vt.type,t.status,t.security_status, p.logTime, ifnull(p.approvalStatus,'Open')  approvalStatus, ifnull(p.vehicleNo, v.regNo) vehicleNo, ifnull(p.status,'initial') trackingStatus, ifnull(p.comment,'') comment, ifnull(p.escort,'') escort, ifnull(p.escortClock,'') escortClock, ifnull(t.distance,0) distance, ifnull(p.manualDistance,0) manualDistance, ifnull(p.driverContact,drv.contact) driverContact    FROM trip_details t join vehicles v on t.vehilce=v.id left join driver drv on t.driverId=drv.id left join vendor_trip_sheet_parent p on t.id=p.tripId  "
						+ vendorSearchQuery
						+ "  ,vehicle_type vt where t.siteId=? and (t.status=? or t.status=? or t.status=?)and vt.id=t.vehicle_type and t.trip_date=?   "
						+ approvalStatusQuery
						+ employeeSearchQuery
						+ "  order by t.id";
				printQuery = printQuery.replace("?", "'%s'");
				printQuery = String.format(printQuery, siteId, "saved",
						"addsave", "saveedit", tripDate);

				System.out.println(printQuery);

			} else if (tripTime[0].equals("ALL")) {
				pst = con
						.prepareStatement("SELECT t.id,t.trip_code,t.trip_date,t.trip_time,t.trip_log,t.routeid,t.trip_code,vt.type,t.status,t.security_status,  p.logTime, ifnull(p.approvalStatus,'Open')  approvalStatus,  v.regNo as vehicleNo, ifnull(p.status,'initial') trackingStatus, ifnull(p.comment,'') comment, ifnull(p.escort,'') escort, ifnull(p.escortClock,'') escortClock, ifnull(t.distance,0) distance, ifnull(p.manualDistance,0) manualDistance, ifnull(p.driverContact,drv.contact) driverContact FROM trip_details t join vehicles v on t.vehicle=v.id left join driver drv on t.driverId=drv.id left join vendor_trip_sheet_parent p on t.id=p.tripId  "
								+ vendorSearchQuery
								+ "  ,vehicle_type vt where t.siteId=? and (t.status=? or t.status=? or t.status=?)and vt.id=t.vehicle_type and t.trip_date=? and t.trip_log=?   "
								+ approvalStatusQuery
								+ employeeSearchQuery
								+ "  order by t.id");
				pst.setString(1, siteId);
				pst.setString(2, "saved");
				pst.setString(3, "addsave");
				pst.setString(4, "saveedit");
				pst.setString(5, tripDate);
				pst.setString(6, tripLog);
				String printQuery = "SELECT t.id,t.trip_code,t.trip_date,t.trip_time,t.trip_log,t.routeid,t.trip_code,vt.type,t.status,t.security_status,  p.logTime, ifnull(p.approvalStatus,'Open')  approvalStatus, ifnull(p.vehicleNo, v.regNo) vehicleNo, ifnull(p.status,'initial') trackingStatus, ifnull(p.comment,'') comment, ifnull(p.escort,'') escort, ifnull(p.escortClock,'') escortClock, ifnull(t.distance,0) distance, ifnull(p.manualDistance,0) manualDistance, ifnull(p.driverContact,drv.contact) driverContact FROM trip_details t  join vehicles v on t.vehicle=v.id left join driver drv on t.driverId=drv.id left join vendor_trip_sheet_parent p on t.id=p.tripId  "
						+ vendorSearchQuery
						+ "  ,vehicle_type vt where t.siteId=? and (t.status=? or t.status=? or t.status=?)and vt.id=t.vehicle_type and t.trip_date=? and t.trip_log=?   "
						+ approvalStatusQuery
						+ employeeSearchQuery
						+ "  order by t.id";
				printQuery = printQuery.replace("?", "'%s'");
				printQuery = String.format(printQuery, siteId, "saved",
						"addsave", "saveedit", tripDate, tripLog);
				System.out.println(printQuery);

			} else {
				String tripTimeQuery = "";
				for (String ttime : tripTime) {
					tripTimeQuery += ",'" + ttime + "'";
				}
				tripTimeQuery = tripTimeQuery.replaceFirst(",", "");
				tripTimeQuery = "(" + tripTimeQuery + ") ";

				pst = con
						.prepareStatement("SELECT t.id,t.trip_code,t.trip_date,t.trip_time,t.trip_log,t.routeid,t.trip_code,vt.type,t.status,t.security_status,  p.logTime, ifnull(p.approvalStatus,'Open')  approvalStatus,v.regNo as vehicleNo, ifnull(p.status,'initial') trackingStatus, ifnull(p.comment,'') comment, ifnull(p.escort,'') escort, ifnull(p.escortClock,'') escortClock, ifnull(t.distance,0) distance, ifnull(p.manualDistance,0) manualDistance, ifnull(p.driverContact, drv.contact) driverContact FROM trip_details t  join vehicles v on t.vehicle=v.id left join driver drv on t.driverId=drv.id left join vendor_trip_sheet_parent p on t.id=p.tripId  "
								+ vendorSearchQuery
								+ "  ,vehicle_type vt where t.siteId=? and (t.status=? or t.status=? or t.status=?)and vt.id=t.vehicle_type and t.trip_date=? and t.trip_log=? and t.trip_time in "
								+ tripTimeQuery
								+ "    "
								+ approvalStatusQuery
								+ employeeSearchQuery + "  order by t.id");
				pst.setString(1, siteId);
				pst.setString(2, "saved");
				pst.setString(3, "addsave");
				pst.setString(4, "saveedit");
				pst.setString(5, tripDate);
				pst.setString(6, tripLog);
				String printQuery = "SELECT t.id,t.trip_code,t.trip_date,t.trip_time,t.trip_log,t.routeid,t.trip_code,vt.type,t.status,t.security_status,  p.logTime, ifnull(p.approvalStatus,'Open')  approvalStatus, ifnull(p.vehicleNo, v.regNo) vehicleNo, ifnull(p.status,'initial') trackingStatus, ifnull(p.comment,'') comment, ifnull(p.escort,'') escort, ifnull(p.escortClock,'') escortClock, ifnull(t.distance,0) distance, ifnull(p.manualDistance,0) manualDistance, ifnull(p.driverContact,drv.contact) driverContact  FROM trip_details t join vehicles v on t.vehicle=v.id left join driver drv on t.driverId=drv.id left join vendor_trip_sheet_parent p on t.id=p.tripId  "
						+ vendorSearchQuery
						+ "  ,vehicle_type vt where t.siteId=? and (t.status=? or t.status=? or t.status=?)and vt.id=t.vehicle_type and t.trip_date=? and t.trip_log=? and t.trip_time in "
						+ tripTimeQuery
						+ "    "
						+ approvalStatusQuery
						+ employeeSearchQuery + "  order by t.id";
				printQuery = printQuery.replace("?", "'%s'");
				printQuery = String.format(printQuery, siteId, "saved",
						"addsave", "saveedit", tripDate, tripLog);
				System.out.println(printQuery);
				System.out.println("Query 1 " + printQuery);
			}

			rs = pst.executeQuery();
			System.out.println("Query 2 ");
			while (rs.next()) {
				tripDetailsDtoObj = new TripDetailsDto();
				tripDetailsDtoObj.setId(rs.getString("id"));

				tripDetailsDtoObj.setActualLogTime(rs.getString("logTime"));
				tripDetailsDtoObj.setVehicleNo(rs.getString("vehicleNo"));

				tripDetailsDtoObj.setApprovalStatus(rs
						.getString("approvalStatus"));
				tripDetailsDtoObj.setComment(rs.getString("comment"));
				tripDetailsDtoObj.setEscort(rs.getString("escort"));
				tripDetailsDtoObj.setEscortNo(rs.getString("escortClock"));
				tripDetailsDtoObj.setTrip_code(rs.getString("trip_code"));
				tripDetailsDtoObj.setTrip_date(rs.getDate("trip_date")
						.toString());
				tripDetailsDtoObj.setDistance(rs.getString("distance"));
				tripDetailsDtoObj.setTrip_time(rs.getString("trip_time"));

				tripDetailsDtoObj.setTrip_log(rs.getString("trip_log"));
				tripDetailsDtoObj.setRouteId(rs.getString("routeid"));
				tripDetailsDtoObj.setTrip_code(rs.getString("trip_code"));
				tripDetailsDtoObj.setVehicle_type(rs.getString("type"));
				tripDetailsDtoObj
						.setIsSecurity(rs.getString("security_status"));
				tripDetailsDtoObj.setComments(getTripComments(tripDetailsDtoObj
						.getId()));

				tripDetailsDtoObj.setTrackingStatus(rs
						.getString("trackingStatus"));
				tripDetailsDtoObj.setTripBasedDistance(rs
						.getString("manualDistance"));
				tripDetailsDtoObj.setDriverContact(rs
						.getString("driverContact"));

				// tripSheetChild = null;
				tripSheetChild = new ArrayList<TripDetailsChildDto>();
				String status = rs.getString("status");
				query = "select tdc.employeeId,e.displayname as EmployeeName, e.personnelNo,a.area,p.place,l.id as landmarkId,l.landmark,e.contactNumber1 as contactno, vt.employeeId approvedEmployee, vt.showStatus,vt.noShowReason,e.gender   from trip_details_child tdc left  join vendor_trip_sheet vt on tdc.employeeId=vt.employeeId and tdc.tripId=vt.tripId left outer join landmark l on  tdc.landmarkid=l.id left outer join place p on  l.place=p.id left outer join area a on  p.area=a.id,employee e  where e.id=tdc.employeeId and tdc.tripId="
						+ rs.getString(1) + " order by tdc.routedOrder";

				rs1 = st.executeQuery(query);

				while (rs1.next()) {

					tripDetailsChildDtoObj = new TripDetailsChildDto();
					tripDetailsChildDtoObj.setApprovedEmployee(rs1
							.getString("approvedEmployee"));
					tripDetailsChildDtoObj.setShowStatus(rs1
							.getString("showStatus"));
					tripDetailsChildDtoObj.setReason(rs1
							.getString("noShowReason"));
					if (tripDetailsChildDtoObj.getApprovedEmployee() != null
							&& !tripDetailsChildDtoObj.getApprovedEmployee()
									.equals("")) {
						tripDetailsDtoObj.setCanTravel(true);
					}

					tripDetailsChildDtoObj.setTripId("" + rs.getInt(1));
					tripDetailsChildDtoObj.setEmployeeId(rs1
							.getString("employeeId"));
					tripDetailsChildDtoObj.setEmployeeName(rs1
							.getString("EmployeeName"));
					tripDetailsChildDtoObj.setPersonnelNo(rs1
							.getString("personnelNo"));
					tripDetailsChildDtoObj.setArea(rs1.getString("area"));
					tripDetailsChildDtoObj.setPlace(rs1.getString("place"));
					tripDetailsChildDtoObj.setLandmarkId(rs1
							.getString("landmarkId"));
					tripDetailsChildDtoObj.setLandmark(rs1
							.getString("landmark"));
					tripDetailsChildDtoObj.setContactNumber(rs1
							.getString("contactno"));

					tripSheetChild.add(tripDetailsChildDtoObj);
				}

				tripDetailsDtoObj.setTripDetailsChildDtoList(tripSheetChild);
				tripSheet.add(tripDetailsDtoObj);
				// System.out.println(" early log time :" +
				// tripDetailsDtoObj.getActualLogTime()+" ->"+rs.getString("logTime")
				// );
				// System.out.println(" late log time :" +
				// tripDetailsDtoObj.getTrip_time()+" ->"+rs.getString("trip_time")
				// );
				rs1.close();

			}
			System.out.println("close");
		} catch (Exception e) {
			System.out.println("Error in getting trip  for tracking  " + e);
		} finally {
			DbConnect.closeResultSet(rs, rs1);
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);
		}
		return tripSheet;

	}

	public ArrayList<TripDetailsDto> getTripSheetSavedNotVendorAssigned(
			String tripDate, String tripLog, String siteId, String tripTime) {
		ArrayList<TripDetailsDto> tripSheet = null;
		DbConnect ob = null;
		Connection con = null;
		Statement st = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		ResultSet rs1 = null;
		try {
			ob = DbConnect.getInstance();
			con = ob.connectDB();
			TripDetailsDto tripDetailsDtoObj = null;
			TripDetailsChildDto tripDetailsChildDtoObj = null;
			tripSheet = new ArrayList<TripDetailsDto>();
			ArrayList<TripDetailsChildDto> tripSheetChild = null;
			st = con.createStatement();
			// order by t.id
			String subQuery = "";
			String query = "SELECT t.id,t.trip_code,t.trip_date,t.trip_time,t.trip_log,t.routeid,t.trip_code,vt.id as vehicletypeId,vt.sit_cap,vt.type,t.status,t.security_status,t.distance,t.travelTime,t.preTripId,t.distanceFromPreTrip, case when t.vehicle is not null then (select regno from vehicles where id=t.vehicle) else 'Not set' end as vehicleNo,case when t.driverid is not null then (select name from driver where id=t.driverid) else 'Not set' end as driver,case when t.driverid is not null then (select contact from driver where id=t.driverid) else ' ' end as driverContact,case when t.escortId is not null then (select concat(escortClock,'-',name) from escort where id=t.escortid) else ' ' end as escort    FROM trip_details t,vehicle_type vt where t.siteId=? and vt.id=t.vehicle_type and  (t.status=? or t.status=? or t.status=?) ";
			if (tripLog.equals("ALL")) {
				subQuery = " and t.trip_date='" + tripDate + "'";

			} else if (tripTime.equals("ALL")) {
				subQuery = " and t.trip_date='" + tripDate
						+ "' and t.trip_log='" + tripLog + "' ";
			} else {
				subQuery = " and t.trip_date='" + tripDate
						+ "' and t.trip_log='" + tripLog
						+ "' and t.trip_time='" + tripTime + "'";
			}
			String lastQuery = " and t.id not in (select tripid from tripvendorassign) order by t.id ";
			// System.out.println("HHHHHHH" + query + subQuery + lastQuery);
			pst = con.prepareStatement(query + subQuery + lastQuery);
			pst.setString(1, siteId);
			pst.setString(2, "saved");
			pst.setString(3, "addsave");
			pst.setString(4, "saveedit");
			rs = pst.executeQuery();
			while (rs.next()) {
				tripDetailsDtoObj = new TripDetailsDto();
				tripDetailsDtoObj.setId(rs.getString("id"));
				tripDetailsDtoObj.setSiteId(siteId);
				tripDetailsDtoObj.setVehicleNo(rs.getString("vehicleNo"));
				tripDetailsDtoObj.setDriverName(rs.getString("driver"));
				// System.out.println(tripDetailsDtoObj.getDriverName()+"vehicles:::::"+tripDetailsDtoObj.getVehicleNo());
				tripDetailsDtoObj.setTrip_code(rs.getString("trip_code"));
				tripDetailsDtoObj.setTrip_date(rs.getDate("trip_date")
						.toString());
				tripDetailsDtoObj.setTrip_time(rs.getString("trip_time"));
				tripDetailsDtoObj.setTrip_log(rs.getString("trip_log"));
				tripDetailsDtoObj.setRouteId(rs.getString("routeid"));
				tripDetailsDtoObj.setTrip_code(rs.getString("trip_code"));
				tripDetailsDtoObj.setVehicle_type(rs.getString("type"));
				tripDetailsDtoObj.setSeat(rs.getInt("sit_cap"));
				tripDetailsDtoObj.setVehicleTypeId(rs
						.getString("vehicletypeId"));
				tripDetailsDtoObj
						.setIsSecurity(rs.getString("security_status"));
				// tripSheetChild = null;
				tripSheetChild = new ArrayList<TripDetailsChildDto>();
				tripDetailsDtoObj.setDistance(rs.getString("distance"));
				tripDetailsDtoObj.setTravelTime(rs.getString("travelTime"));
				tripDetailsDtoObj.setPreviousTripId(rs.getString("preTripId"));
				tripDetailsDtoObj.setDistanceFromPrevioustrip(rs
						.getFloat("distanceFromPreTrip"));
				tripDetailsDtoObj.setDriverContact(rs
						.getString("driverContact"));
				tripDetailsDtoObj.setEscort(rs.getString("escort"));
				query = "select tdc.scheduleId, tdc.employeeId as employeeId,e.displayname as EmployeeName,e.personnelNo,e.gender,a.area,p.place,l.id as landmarkId,l.landmark, ifnull(e.contactNumber1,'') as contactno, vt.employeeId approvedEmployee,  vt.showStatus, vt.noShowReason,tdc.time,tdc.dist,e.gender,tdc.transportType from trip_details_child tdc left  join vendor_trip_sheet vt on tdc.employeeid=vt.employeeId and tdc.tripId=vt.tripId ,employee e ,area a,place p,landmark l where e.id=tdc.employeeId and tdc.tripId="
						+ rs.getString(1)
						+ " and tdc.landmarkId=l.id and l.place=p.id and p.area=a.id order by tdc.routedOrder";
				rs1 = st.executeQuery(query);
				while (rs1.next()) {

					tripDetailsChildDtoObj = new TripDetailsChildDto();
					tripDetailsChildDtoObj.setApprovedEmployee(rs1
							.getString("approvedEmployee"));
					tripDetailsChildDtoObj.setShowStatus(rs1
							.getString("showStatus"));
					tripDetailsChildDtoObj.setReason(rs1
							.getString("noShowReason"));

					if (tripDetailsChildDtoObj.getApprovedEmployee() != null
							&& !tripDetailsChildDtoObj.getApprovedEmployee()
									.equals("")) {
						tripDetailsDtoObj.setCanTravel(true);
					}

					tripDetailsChildDtoObj.setTripId("" + rs.getInt(1));
					tripDetailsChildDtoObj.setEmployeeId(rs1
							.getString("employeeId"));
					tripDetailsChildDtoObj.setPersonnelNo(rs1
							.getString("personnelNo"));
					tripDetailsChildDtoObj.setEmployeeName(rs1
							.getString("EmployeeName"));
					tripDetailsChildDtoObj.setGender(rs1.getString("gender"));
					tripDetailsChildDtoObj.setArea(rs1.getString("area"));
					tripDetailsChildDtoObj.setPlace(rs1.getString("place"));
					tripDetailsChildDtoObj.setLandmarkId(rs1
							.getString("landmarkId"));
					tripDetailsChildDtoObj.setLandmark(rs1
							.getString("landmark"));
					tripDetailsChildDtoObj.setContactNumber(rs1
							.getString("contactno"));
					tripDetailsChildDtoObj.setScheduleId(rs1
							.getString("scheduleId"));

					try {
						tripDetailsChildDtoObj
								.setDistance(rs1.getFloat("dist"));
						tripDetailsChildDtoObj.setTime(rs1.getString("time"));
						tripDetailsChildDtoObj.setTransportType(rs1
								.getString("transportType"));

					} catch (Exception e) {

					}

					tripSheetChild.add(tripDetailsChildDtoObj);
				}

				tripDetailsDtoObj.setTripDetailsChildDtoList(tripSheetChild);
				tripSheet.add(tripDetailsDtoObj);
				rs1.close();
			}

		} catch (Exception e) {
			DbConnect.closeConnection(con);
			System.out.println("Error in getting saved" + e);
		} finally {
			DbConnect.closeResultSet(rs, rs1);
			DbConnect.closeStatement(st, pst);
			DbConnect.closeConnection(con);
		}
		return tripSheet;
	}

	public int checkSecurity(String siteid, String tripTime) {
		int LADY_SECURTY = 1;
		DbConnect ob = null;
		Connection con = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		int retVal = 0;
		try {
			ob = DbConnect.getInstance();
			con = ob.connectDB();
			String query = "SELECT * FROM site WHERE (night_shift_start<'"
					+ tripTime + "' OR night_shift_end>'" + tripTime
					+ "')AND lady_security=? and id=?";
			// System.out.println("query"+query);
			pst = con.prepareStatement(query);
			pst.setInt(1, LADY_SECURTY);
			pst.setString(2, siteid);
			rs = pst.executeQuery();
			if (rs.next()) {
				retVal = 1;
			}

		} catch (Exception ee) {
			System.out.println("error in get is sec needed in trip dao" + ee);
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);
		}
		// System.out.println("check securuty ends");
		return retVal;

	}

	public void saveTrip(String tripids) {
		// TODO Auto-generated method stub

	}

	public int updateApprovalStatus(ArrayList<VehicleDto> tripVehicles) {
		DbConnect ob = null;
		Connection con = null;
		PreparedStatement pst = null;
		int retVal = -1;
		try {
			ob = DbConnect.getInstance();
			con = ob.connectDB();
			String updateQuery = "UPDATE vendor_trip_sheet_parent SET approvalStatus=? WHERE tripId=?";
			pst = con.prepareStatement(updateQuery);
			for (VehicleDto dto : tripVehicles) {
				pst.setString(1, "Sent for TC approval");
				pst.setString(2, dto.getTripId());
				int result = pst.executeUpdate();
				System.out.println("updated trip id" + dto.getTripId());
				retVal += result;
				System.out.println("retvalue" + retVal);
			}
		} catch (Exception e) {
		} finally {
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);
		}
		return retVal;
	}

	public int getbillingtypeById(String tripId) {
		DbConnect ob = null;
		Connection con = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		int returnint = 0;
		try {
			ob = DbConnect.getInstance();
			con = ob.connectDB();
			String Query = "SELECT billing_type_mapping.billing_type FROM billing_type_mapping,trip_details,tripvendorassign WHERE site=trip_details.siteId AND fromdate<=trip_details.trip_date AND vendor=tripvendorassign.vendorId AND tripid=? ORDER BY fromdate ASC LIMIT 1";
			pst = con.prepareStatement(Query);
			pst.setString(1, tripId);
			rs = pst.executeQuery();
			if (rs.next()) {
				returnint = rs.getInt("billing_type");
			}
		} catch (Exception e) {
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);
		}
		return returnint;
	}

	public TripDetailsDto getTripAmount(String tripId) {
		DbConnect ob = null;
		Connection con = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		TripDetailsDto dto = new TripDetailsDto();
		try {
			ob = DbConnect.getInstance();
			con = ob.connectDB();
			int bill_type = new TripDetailsDao().getbillingtypeById(tripId);
			String queryval = "";
			if (bill_type == BillingTypeConstant.KM_BASED_BILLING_TYPE) {
				queryval = "templateTripRate";
			} else if (bill_type == BillingTypeConstant.SLAB_BASED_BILLING_TYPE) {
				queryval = "slabTripRate";
			} else if (bill_type == BillingTypeConstant.TRIP_BASED_BILLING_TYPE) {
				queryval = "triptriprate";
			}
			String searchQuery = "SELECT " + queryval
					+ ",tripEscortRate FROM TD_BILLING_ARGS WHERE TRIPID=?";
			pst = con.prepareStatement(searchQuery);
			pst.setString(1, tripId);
			rs = pst.executeQuery();
			if (rs.next()) {
				dto.setTripRate(rs.getDouble(queryval));
				dto.setEscortCost(rs.getString("tripEscortRate"));

			} else {
				TripDetailsDto proxyTrip = new TripDetailsDao()
						.getTrackedTripSheetByTripId(tripId);
				HashMap<String, TripDetailsDto> map = new HashMap<>();
				map.put(tripId, proxyTrip);
				new TripDetailsService().saveTripRateArgs(map, " ");
				rs = pst.executeQuery();
				if (rs.next()) {
					dto.setTripRate(rs.getDouble(queryval));
					dto.setEscortCost(rs.getString("tripEscortRate"));

				}
			}
		} catch (Exception e) {
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);
		}
		return dto;
	}

	public int updateTripAmount(String tripId, Double amount) {
		DbConnect ob = null;
		Connection con = null;
		PreparedStatement pst = null;
		int result = 0;
		try {
			ob = DbConnect.getInstance();
			con = ob.connectDB();
			int bill_type = new TripDetailsDao().getbillingtypeById(tripId);
			String queryval = "";
			if (bill_type == BillingTypeConstant.KM_BASED_BILLING_TYPE) {
				queryval = "templateTripRate";
			} else if (bill_type == BillingTypeConstant.SLAB_BASED_BILLING_TYPE) {
				queryval = "slabTripRate";
			} else if (bill_type == BillingTypeConstant.TRIP_BASED_BILLING_TYPE) {
				queryval = "triptriprate";
			}
			String updatequery = "UPDATE td_billing_args SET " + queryval
					+ "=? WHERE tripid=?";
			pst = con.prepareStatement(updatequery);
			pst.setDouble(1, amount);
			pst.setString(2, tripId);
			result = pst.executeUpdate();
		} catch (Exception e) {
		} finally {
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);
		}

		return result;

	}

	public ArrayList<TripDetailsDto> getTripSummary(String employeeId) {
		// TODO Auto-generated method stub
		return null;
	}

	public ArrayList<TripDetailsDto> getAdhocTripSheetSaved(String tripDate,
			String siteId) {
		ArrayList<TripDetailsDto> tripSheet = null;
		DbConnect ob = null;
		Connection con = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			ob = DbConnect.getInstance();
			con = ob.connectDB();
			TripDetailsDto tripDetailsDtoObj = null;
			tripSheet = new ArrayList<TripDetailsDto>();
			String query = "SELECT td.id,td.trip_code,td.trip_date,td.trip_time,td.trip_log,td.routeid,td.trip_code,td.status,td.security_status,td.distance,td.travelTime,td.destinationLandmark,td.preTripId,td.distancefrompretrip,td.vehicle,td.routingType,td.driverPswd,td.driverId,td.escortId,td.escortPswd,td.vehicle_type,tc.employeeId,p.empId,p.displayName,p.email,p.gender,p.phone,p.type,vt.*,dt.name as 'driver',l.*,l.id as 'landmarkid' FROM adhoc_trip_details td LEFT JOIN adhoc_trip_details_child tc ON tc.tripId=td.id LEFT JOIN adhoc_passenger p ON p.empId=tc.employeeId LEFT JOIN vehicles vt ON vt.id=td.vehicle LEFT JOIN driver dt ON dt.id=td.driverId LEFT JOIN landmark l ON tc.landmarkId=l.id WHERE td.siteid=? AND td.trip_date=?";
			pst = con.prepareStatement(query);
			pst.setString(1, siteId);
			pst.setString(2, tripDate);
			// pst.setString(3, "saved");
			// pst.setString(4, "addsave");
			// pst.setString(5, "saveedit");
			rs = pst.executeQuery();
			while (rs.next()) {
				System.out.println("rs is moving");
				tripDetailsDtoObj = new TripDetailsDto();
				TripDetailsChildDto tripDetailsChildDtoObj = new TripDetailsChildDto();
				tripDetailsDtoObj.setId(rs.getString("id"));
				tripDetailsDtoObj.setTrip_code(rs.getString("trip_code"));
				tripDetailsDtoObj.setTrip_date(rs.getDate("trip_date")
						.toString());
				tripDetailsDtoObj.setTrip_time(rs.getString("trip_time"));
				tripDetailsDtoObj.setTrip_log(rs.getString("trip_log"));
				tripDetailsDtoObj.setRouteId(rs.getString("routeid"));
				tripDetailsDtoObj.setVehicle_type(rs.getString("vehicletype"));
				tripDetailsDtoObj.setStatus(rs.getString("status"));
				tripDetailsDtoObj
						.setIsSecurity(rs.getString("security_status"));
				tripDetailsDtoObj.setSiteId(siteId);
				tripDetailsDtoObj.setVehicleNo(rs.getString("regno"));
				tripDetailsDtoObj.setDriverName(rs.getString("driver"));
				tripDetailsDtoObj
						.setVehicleTypeId(rs.getString("vehicle_type"));

				tripDetailsDtoObj.setDistance(rs.getString("distance"));
				tripDetailsDtoObj.setTravelTime(rs.getString("travelTime"));
				tripDetailsDtoObj.setPreviousTripId(rs.getString("preTripId"));
				tripDetailsDtoObj.setDistanceFromPrevioustrip(rs
						.getFloat("distanceFromPreTrip"));
				// tripDetailsDtoObj.setDriverContact(rs.getString("driverContact"));
				// tripDetailsDtoObj.setEscort(rs.getString("escort"));

				ArrayList<TripDetailsChildDto> tripSheetChild = new ArrayList<TripDetailsChildDto>();
				tripDetailsChildDtoObj = new TripDetailsChildDto();
				/*
				 * tripDetailsChildDtoObj.setApprovedEmployee(rs
				 * .getString("approvedEmployee"));
				 * tripDetailsChildDtoObj.setShowStatus(rs
				 * .getString("showStatus"));
				 * tripDetailsChildDtoObj.setReason(rs
				 * .getString("noShowReason"));
				 * 
				 * if (tripDetailsChildDtoObj.getApprovedEmployee() != null &&
				 * !tripDetailsChildDtoObj.getApprovedEmployee() .equals("id"))
				 * { tripDetailsDtoObj.setCanTravel(true); }
				 */

				tripDetailsChildDtoObj.setTripId("" + rs.getInt("id"));
				tripDetailsChildDtoObj.setEmployeeId(rs.getString("empId"));
				tripDetailsChildDtoObj.setEmployeeName(rs
						.getString("displayname"));
				tripDetailsChildDtoObj.setGender(rs.getString("gender"));
				// tripDetailsChildDtoObj.setArea(rs.getString("area"));
				// tripDetailsChildDtoObj.setPlace(rs.getString("place"));
				tripDetailsChildDtoObj
						.setLandmarkId(rs.getString("landmarkId"));
				tripDetailsChildDtoObj.setLandmark(rs.getString("landmark"));
				tripDetailsChildDtoObj.setContactNumber(rs.getString("phone"));
				tripSheetChild.add(tripDetailsChildDtoObj);
				tripDetailsDtoObj.setTripDetailsChildDtoList(tripSheetChild);

				tripSheet.add(tripDetailsDtoObj);
			}

		} catch (Exception e) {
			DbConnect.closeConnection(con);
			System.out.println("Error in getting saved" + e);
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);
		}
		return tripSheet;
	}
	public int deleteTrips(String tripId[])
	{
		DbConnect ob = null;
		Connection con = null;
		Statement st = null;
		Statement st1 = null;
		Statement st2 = null;
		Statement st3 = null;
		ob = DbConnect.getInstance();
		con = ob.connectDB();
		String id="";
		int result=-1;
		
		try {
			for (int i = 0; i < tripId.length; i++) {
				if(i==0)
				{
					id=tripId[i];
				}
				else{
				id= id+","+tripId[i];
				}
			}
			st = con.createStatement();
			st1 = con.createStatement();
			st2 = con.createStatement();
			st3 = con.createStatement();
			String qry= "delete from tripvendorassign where tripId in ("+id+")";
			String qry1="delete from vendor_trip_sheet where tripId in("+id+")";
			String qry2="delete from vendor_trip_sheet_parent where tripId in("+id+")";

			String qry3="delete from vendor_trip_sheet_escort where tripId in("+id+")";
			//System.out.println(qry+qry1+qry2);
			result = st.executeUpdate(qry);
			
			st1.executeUpdate(qry1);
			st2.executeUpdate(qry2);
			st3.executeUpdate(qry2);
			
		} catch (SQLException e) {
			System.out.println("Error in TripDetaisDeleting");;
		}
		return result;

	}

	public Boolean checkTripWithDate(String dateinsql, String tripLog,String tripTime) {
		DbConnect ob = null;
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		boolean flag=false;
		try{
			ob=DbConnect.getInstance();
			con=ob.connectDB();
			st=con.createStatement();
			String selectqry="";
			if(tripLog.equalsIgnoreCase("ALL") && tripTime.equalsIgnoreCase("ALL")){
				selectqry="select id from trip_details where trip_date='"+dateinsql+"'";
			}else{
				selectqry="select id from trip_details where trip_date='"+dateinsql+"' and trip_log='"+tripLog+"' and trip_time='"+tripTime+"'";
			}
			rs=st.executeQuery(selectqry);
			if(rs.next()){
				flag=true;
			}
		} catch (Exception e) {
			System.out.println("Error in TripDetais@checkTripWithDate");
		}finally{
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);
		}
		return flag;
	}

	public int generatefixedRouteTrips(String travelDate, String tripLog,
			String tripTime,String siteid) {
		System.out.println("logggggggggg"+tripLog);
		DbConnect ob = null;
		Connection con = null;
		Statement st = null,st1=null,st2=null,st3=null;
		ResultSet rs = null,rs1=null,rs2=null,rs3=null;
		PreparedStatement st4=null;
		try{
			ob=DbConnect.getInstance();
			con=ob.connectDB();
			ArrayList<String> empids= new ArrayList<String>();
			String subqry="",subqy2="";
			if(tripLog.equalsIgnoreCase("IN")){
				subqry="log_in='"+tripTime+"'";
			}else{
				subqry="log_out='"+tripTime+"'";
			}
			String tdqry="select distinct(routein) as empid from employee_tripping where  routein!=0  and "+subqry;
			System.out.println("empsss"+tdqry);
			st=con.createStatement();
			rs=st.executeQuery(tdqry);
			while(rs.next()){
			
			String getRouteDetails="select * from  route where id ="+rs.getString("empid")+"";
			st1=con.createStatement();
			System.out.println("details "+getRouteDetails);
			rs1=st1.executeQuery(getRouteDetails);
			RouteDto routetdto = new RouteDto();
			while(rs1.next()){
				routetdto.setRouteId(rs1.getInt("id"));
				routetdto.setVehicleTypeId(rs1.getString("vehicle"));
			}
			
				createUniqueID(travelDate, tripLog, siteid);
				getIncremenetedUnique();
				String tripCode = uniqueId;
				String insertqry="INSERT INTO trip_details (trip_code, siteId, trip_date, trip_time, trip_log, vehicle_type, status, security_status,routingType) VALUES ('"+tripCode+"', '"+siteid+"', '"+travelDate+"', '"+tripTime+"', '"+tripLog+"', '"+routetdto.getVehicleTypeId()+"', 'routed', 'NO','o')";
				System.out.println("create trip "+insertqry);
				st2=con.createStatement();
				st2.executeUpdate(insertqry, Statement.RETURN_GENERATED_KEYS);
				rs2 = st2.getGeneratedKeys();
				int tripId=0;
				if (rs2.next()) {
					tripId = rs2.getInt(1);
				}
				//getting Childs
				System.out.println("tdid"+tripId);
				st3=con.createStatement();
				String getchild="select * from emp_route_map where routeid="+routetdto.getRouteId()+" order by position ";
				System.out.println("getEmps"+getchild);
				rs3=st3.executeQuery(getchild);
				int routedorder=1;
				while(rs3.next()){
					float timereq=getTime(rs3.getString("compdist"));
					String insertchild="INSERT INTO trip_details_child (tripId, employeeId, routedOrder, landmarkId, time, dist, scheduleId) VALUES (?, ?, ?, '3367', ?, ?, '0')";
					st4=con.prepareStatement(insertchild);
					st4.setInt(1, tripId);
					st4.setString(2, rs3.getString("empid"));
					st4.setInt(3, routedorder);
					st4.setFloat(4, timereq);
					st4.setString(5, rs3.getString("compdist"));
					st4.executeUpdate();
					
				}
			
			}
		} catch (Exception e) {
			System.out.println("Error in TripDetais@generatefixedRouteTrips");
		}finally{
			DbConnect.closeResultSet(rs,rs1,rs2,rs3);
			DbConnect.closeStatement(st,st1,st2,st3,st4);
			DbConnect.closeConnection(con);
		}
		
		return 0;
	}
	public float getTime(String distance) {

		DbConnect ob = DbConnect.getInstance();
		Connection con = null;
		ResultSet rs = null;
		Statement st = null;
		float time = 0;
		con = ob.connectDB();
		try {
			st = con.createStatement();
			String gettingTime = "select ROUND( "
					+ distance
					+ "/speedpkm*60,0)as time from timeSloat where CONCAT(startTime,':00') <= CONCAT(curtime(),'') and CONCAT(endTime,':00') > CONCAT(curtime(),'')";
			rs = st.executeQuery(gettingTime);
			if (rs.next()) {
				time = rs.getFloat("time");
			}

		} catch (Exception e) {
			System.out.println("Error in ShuttleDao@getTime" + e);
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);
		}
		return time;
	}

	public Boolean checkTripWithDateRange(String fromdateinsql, String todateinsql) {
		DbConnect ob = null;
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		boolean flag=false;
		try{
			ob=DbConnect.getInstance();
			con=ob.connectDB();
			st=con.createStatement();
			String selectqry="select id from trip_details where trip_date  between '"+fromdateinsql+"' and '"+todateinsql+"'";
			
			rs=st.executeQuery(selectqry);
			if(rs.next()){
				flag=true;
			}
		} catch (Exception e) {
			System.out.println("Error in TripDetais@checkTripWithDate");
		}finally{
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);
		}
		return flag;
	}

	public int mirrorFixedTrips(String siteId, String sourcedate,
			String fromdate, String todate) {
		DbConnect ob = null;
		Connection con = null;
		Statement st = null,st1=null,st2=null;
		ResultSet rs = null,rs1=null,rs2=null;
		PreparedStatement pst=null,pst1=null,pst2=null,pst3=null,pst4=null;
		int result=0;
		try{
			ob=DbConnect.getInstance();
			con=ob.connectDB();
			st=con.createStatement();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Calendar c = Calendar.getInstance();
			c.setTime(sdf.parse(fromdate));
			Calendar c1 = Calendar.getInstance();
			c1.setTime(sdf.parse(todate));
			c1.add(Calendar.DATE, 1);
			todate = sdf.format(c1.getTime());
			while(!fromdate.equalsIgnoreCase(todate)){
				String getTripDetails="select * from trip_details where siteid="+siteId+" and trip_date='"+sourcedate+"'";
				rs=st.executeQuery(getTripDetails);
				while(rs.next()){
					createUniqueID(fromdate, rs.getString("trip_log"), siteId);
					getIncremenetedUnique();
					String tripCode = uniqueId;
					
					String insertqry="INSERT INTO trip_details (trip_code, siteId, trip_date, trip_time, trip_log, vehicle_type, status, security_status,routingtype,vehicle,driverid,driverPswd) VALUES ('"+tripCode+"', '"+siteId+"', '"+fromdate+"', '"+rs.getString("trip_time")+"', '"+rs.getString("trip_log")+"', '"+rs.getString("vehicle_type")+"', 'saved', '"+rs.getString("security_status")+"' ,'o','"+rs.getString("vehicle")+"','"+rs.getString("driverid")+"','1234')";
					
					st1=con.createStatement();
					st1.executeUpdate(insertqry, Statement.RETURN_GENERATED_KEYS);
					rs1 = st1.getGeneratedKeys();
					int tripId=0;
					if (rs1.next()) {
						tripId = rs1.getInt(1);
					
						RandomString rd = new RandomString(4);
						 String password= rd.nextDriverString( tripId+"");
						 String driverpswd = "update trip_details set driverPswd="+password+" where id= "+tripId;
						 System.out.println(driverpswd);
						 pst4=con.prepareStatement(driverpswd);
						 pst4.executeUpdate(); 
					      
						
						
						
						
						
						
						
						String vendorassign="INSERT INTO tripvendorassign (tripId, vendorid, status) VALUES (?, '1', 'a')";
						pst1=con.prepareStatement(vendorassign);
						pst1.setInt(1, tripId);
						pst1.executeUpdate();
							
						
					
						
					
						
						String vendortripparent="INSERT INTO vendor_trip_sheet_parent (tripId, vehicleNO, escort,status,downloadStatus) VALUES (?,?,?,?,?)";
						pst2=con.prepareStatement(vendortripparent);
						pst2.setInt(1, tripId);
						pst2.setString(2, rs.getString("vehicle"));
						pst2.setString(3, "NO");
						pst2.setString(4, "initial");
						pst2.setString(5, "ready");
						
						pst2.executeUpdate();
						
					
					
					String getchild="select * from trip_details_child where tripid="+rs.getString("id");
					st2=con.createStatement();
					rs2=st2.executeQuery(getchild);
					while(rs2.next()){
						String insertchild="INSERT INTO trip_details_child (tripId, employeeId, routedOrder, landmarkId, time, dist, scheduleId) VALUES (?, ?, ?, '3367', ?, ?, '0')";
						pst=con.prepareStatement(insertchild);
						pst.setInt(1, tripId);
						pst.setString(2, rs2.getString("employeeId"));
						pst.setInt(3, rs2.getInt("routedOrder"));
						pst.setFloat(4, rs2.getFloat("time"));
						pst.setString(5, rs2.getString("dist"));
						pst.executeUpdate();
						
						
						
						String vendortripchild="INSERT INTO vendor_trip_sheet (tripId, employeeId, insertedorder, showstatus) VALUES (?, ?, ?, 'No Show')";
						pst3=con.prepareStatement(vendortripchild);
						pst3.setInt(1, tripId);
						pst3.setString(2, rs2.getString("employeeId"));
						pst3.setInt(3, rs2.getInt("routedOrder"));
						pst3.executeUpdate();
						
				
					}
						
						
					}
				}
				
				c.add(Calendar.DATE, 1);  // number of days to add
				fromdate = sdf.format(c.getTime());
				System.out.println(fromdate);
			}
			
		} catch (Exception e) {
			System.out.println("Error in TripDetais@mirrorFixedTrips");
			e.printStackTrace();
		}finally{
			DbConnect.closeResultSet(rs,rs1,rs2);
			DbConnect.closeStatement(st,st1,st2,pst3,pst2,pst1,pst);
			DbConnect.closeConnection(con);
		}
		return result;
	}
}