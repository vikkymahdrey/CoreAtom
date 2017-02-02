package com.agiledge.atom.mobile.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;

import com.agiledge.atom.commons.OtherFunctions;
import com.agiledge.atom.constants.SettingsConstant;
import com.agiledge.atom.dao.PanicDao;
import com.agiledge.atom.dao.ShuttleDao;
import com.agiledge.atom.dbConnect.DbConnect;
import com.agiledge.atom.dto.PanicDto;
import com.agiledge.atom.dto.TripDetailsDto;

public class TripServiceDao {
	private String message = "";
	public int setTripReadyForTracking(String tripId, String vehicleNo) {
		DbConnect ob = null;
		Connection con = null;
		PreparedStatement pst = null;
		PreparedStatement pst1 = null;
		ResultSet rs = null;
		int retVal = 0;
		try {

			ob = DbConnect.getInstance();
			con = ob.connectDB();
			pst = con
					.prepareStatement("select * from vendor_trip_sheet_parent where tripId="
							+ tripId + "");
			rs = pst.executeQuery();

			if (rs.next()) {
				pst1 = con
						.prepareStatement("update vendor_trip_sheet_parent set downloadStatus='ready', vehicleNo=(select regno from vehicles where id="+vehicleNo+") where tripId="
								+ tripId
								+ " and ( ( status!='started' or status!='stopped' ) or status is null ) ");
				pst1.executeUpdate();
				retVal = 1;

				System.out
						.println(" update vendor_trip_sheet_parent set downloadStatus='ready', vehicleNo=(select regno from vehicles where id="+vehicleNo+") where tripId="
								+ tripId
								+ " and ( ( status!='started' or status!='stopped' ) or status is null ) ");
				DbConnect.closeStatement(pst);

			} else {
				System.out
						.println("Trackkkkkk......... inserting into vendor_trip_sheet_parent");
				pst1 = con
						.prepareStatement("insert into vendor_trip_sheet_parent(tripId,downloadStatus,escort, vehicleNo, status ) values ("
								+ tripId
								+ ", 'ready','NO',(select regno from vehicles where id="+vehicleNo+"), 'initial')");
				pst1.executeUpdate();

				System.out.println("..............sdfsdfsdf..................");
				pst1 = con
						.prepareStatement("insert into vendor_trip_sheet(tripId,employeeId,showStatus,insertedOrder,keypin) select tripId,employeeId,'No Show',routedOrder,round(rand()*10000) from trip_details_child  where tripId="
								+ tripId + " ");
				System.out
						.println("insert into vendor_trip_sheet(tripId,employeeId,showStatus,insertedOrder,keypin) select tripId,employeeId,'No Show',routedOrder,round(rand()*10000) from trip_details_child  where tripId="
								+ tripId + " ");
				pst1.executeUpdate();
				retVal = 1;
			}

		} catch (Exception e) {
			System.out.println("Error in setTripReadyForTracing " + e);
		} finally {
			DbConnect.closeStatement(pst);
			DbConnect.closeStatement(pst1);
			DbConnect.closeConnection(con);
		}

		return retVal;
	}


	public int updateGeoCode(String imei, String tripId, double lattitude,
			double longitude, long nuance)

	{

		DbConnect ob = DbConnect.getInstance();
		Connection con = ob.connectDB();
		PreparedStatement pst = null;
		PreparedStatement pst1 = null;
		ResultSet rs = null;
		int vehicleId = 0;
		String vehicleStatus = "";
		int retVal = 0;
		try {
			pst = con
					.prepareStatement("select  t.vehicle,vp.logstatus from trip_details t  left outer join vehicle_position vp on t.id=vp.tripId  where t.Id="
							+ tripId + "  order by vp.id desc limit 1");
			
			rs = pst.executeQuery();
			if (rs.next())

			{
				vehicleId = rs.getInt(1);
				vehicleStatus = rs.getString(2);
			}

			/*
			 * pst = con .prepareStatement(
			 * "select t.id,t.vehicle,vp.vehicleId as logged from driver_logins ds,trip_details t left outer join vehicle_position vp on t.id=vp.tripId where ds.vehicleId=t.vehicle and  t.trip_code=? and  ds.imei_number=? "
			 * ); pst.setString(1, TripCode); pst.setString(2, imei); rs =
			 * pst.executeQuery(); if (rs.next()) { tripId = rs.getLong(1);
			 * vehicleId = rs.getInt(2); vehicleStatus = rs.getString(3); }
			 * pst=null; if (vehicleStatus==null || vehicleStatus.equals("") ||
			 * vehicleStatus.equalsIgnoreCase("null")) {
			 */
			pst = con
					.prepareStatement("insert into vehicle_position(vehicleId,lattitude,longitude,logstatus,tripId,status, date_time) values(?,?,?,?,?,?, ?)");
			//System.out.println("Time (stationary) :" +new java.sql.Timestamp(nuance).toGMTString());
			pst.setInt(1, vehicleId);
			pst.setDouble(2, lattitude);
			pst.setDouble(3, longitude);
			if (vehicleStatus != null && vehicleStatus.equals("danger")) {
				pst.setString(4, "danger");
			} else {
				pst.setString(4, "run");
			}
			pst.setString(5, tripId);
			pst.setString(6, "checkedin");
			pst.setTimestamp(7, new java.sql.Timestamp(nuance));
			retVal = pst.executeUpdate();
			DbConnect.closeStatement(pst);
			pst1 = con.prepareStatement("update vendor_trip_sheet set latitude=?, longitude=? where latitude='0' and longitude='0' and tripId=?");
			pst1.setDouble(1, lattitude);
			pst1.setDouble(2, longitude);
			pst1.setString(3, tripId);
			pst1.executeUpdate();
			pst1 = con.prepareStatement("update vendor_trip_sheet_escort set inLatitude=?, inLongitude=? where inLatitude='0' and inLongitude='0' and tripId=?");
			pst1.setDouble(1, lattitude);
			pst1.setDouble(2, longitude);
			pst1.setString(3, tripId);
			pst1.executeUpdate();
			
			/*
			 * } else { pst = con.prepareStatement(
			 * "update vehicle_position set lattitude=?,longitude=? where vehicleId=? and tripId=?"
			 * ); pst.setDouble(1, lattitude); pst.setDouble(2, longitude);
			 * pst.setInt(3, vehicleId); pst.setLong(4, tripId);
			 * retVal=pst.executeUpdate(); }
			 */
		} catch (Exception e) {
			System.out.println("error in update geocode dao" + e);

		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);
		}
		return retVal;
	}

	public int employeeCheckIn(String tripId, String empCode, double latitude,
			double longitude,long nuance) {
		DbConnect ob = DbConnect.getInstance();
		Connection con = ob.connectDB();
		PreparedStatement pst = null;
		PreparedStatement pst1 = null;
		ResultSet rs = null;
		int retVal = 0;

		try {
			String query ="";
			String checkQuery = "select * from trip_details where id=" + tripId;
			pst1 = con.prepareStatement(checkQuery);
			rs = pst1.executeQuery();
			String time=OtherFunctions.changeDateFormat(new Date(nuance), "HH:mm:ss");
			if(rs.next()) {
			
			if(rs.getString("trip_log").equalsIgnoreCase("IN")) {
				query = "update vendor_trip_sheet set showStatus='Show', curStatus='IN',latitude="
						+ latitude
						+ ",longitude="
						+ longitude
						+ ", inTime='"+time+"'  where employeeId="
						+ empCode
						+ " and  tripid=" + tripId;
				
			} else {
				query = "update vendor_trip_sheet set showStatus='Show', curStatus='IN'," +
						" inTime='"+time+"'  where employeeId="
						+ empCode
						+ " and  tripid=" + tripId;
			}
			
			
			pst = con
					.prepareStatement(query);
			retVal = pst.executeUpdate();
			}
		} catch (Exception e) {
			System.out.println("Error in dao" + e);
		} finally {
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);
		}
		return retVal;
	}

	public int startTrip(String tripId, String imei) {
		DbConnect ob = null;
		Connection con = null;
		//PreparedStatement pstCheck = null;
		PreparedStatement pst = null;
		PreparedStatement pst1 = null;
		PreparedStatement pst2 = null;
		ResultSet rs = null;
		int retVal = 0;
		boolean validFlag = true;

		try {

			ob = DbConnect.getInstance();
			con = ob.connectDB();
			pst2 = con
					.prepareStatement("update driver_logins set status='logout' where imei_number=? and tripid!=?");// logging
																													// out
																													// old
																													// trips
																													// if
																													// present
			pst2.setString(1, imei);
			pst2.setString(2, tripId);
			pst2.executeUpdate();

			pst = con
					.prepareStatement("select * from vendor_trip_sheet_parent where tripId="
							+ tripId + "");
			rs = pst.executeQuery();
			String onTimeStatusQuery = "";
			TripDetailsDto tripDto = new MobileTripSheetDao()
					.getVehicleTripSheet(tripId);
			if (tripDto != null
					&& tripDto.getTrip_log().equalsIgnoreCase("OUT")) {
				String onTimeStatus = getLiveOnTimeStatus(tripId);
				onTimeStatusQuery = ", onTimeStatus='" + onTimeStatus
						+ "',  logTime=curtime() ";

			}

			if (rs.next()) {
				String status = rs.getString("status");
				
				if(OtherFunctions.isEmpty(status)) {
					validFlag=true;
				} else if (status!=null && status.equalsIgnoreCase("stopped") ) {
					validFlag = false;
				} 
				if(validFlag) { 
					pst1 = con
							.prepareStatement("update vendor_trip_sheet_parent set status='started',downloadStatus='downloaded', startTime=curtime()  "
									+ onTimeStatusQuery + " where tripId=" + tripId);
					pst1.executeUpdate();
					retVal = 1;
	
					System.out
							.println(" Query :  update vendor_trip_sheet_parent set status='started'  where tripId="
									+ tripId);
					DbConnect.closeStatement(pst);
				} else {
					retVal = -2;
				}

			} else {
				System.out
						.println("Trackkkkkk......... inserting into vendor_trip_sheet_parent");
				pst1 = con
						.prepareStatement("insert into vendor_trip_sheet_parent(tripId,startTime,vehicle_imei,status,escort) values ("
								+ tripId + ",curtime() ,'" + imei

								+ "', 'started','NO')");
				pst1.executeUpdate();

				System.out.println("..............sdfsdfsdf..................");
				pst1 = con
						.prepareStatement("insert into vendor_trip_sheet(tripId,employeeId,showStatus,keypin) select tripId,employeeId,'No Show',round(rand()*10000) from trip_details_child  where tripId="
								+ tripId + " ");
				System.out
						.println("insert into vendor_trip_sheet(tripId,employeeId,showStatus) select tripId,employeeId,'No Show',round(rand()*10000) from trip_details_child  where tripId="
								+ tripId + " ");
				pst1.executeUpdate();
				retVal = 1;
			}

		} catch (Exception e) {
			System.out.println("ërror" + e);
		} finally {
			DbConnect.closeStatement(pst);
			DbConnect.closeStatement(pst1);
			DbConnect.closeConnection(con);
		}

		return retVal;
	}

	public int setTripAsDownloaded(String tripId, String imei, String vehicleNo) {
		System.out.println("trip id : " + tripId);
		DbConnect ob = null;
		Connection con = null;
		PreparedStatement pst = null;
		PreparedStatement pst1 = null;
		ResultSet rs = null;
		int retVal = 0;
		try {

			ob = DbConnect.getInstance();
			con = ob.connectDB();
			pst = con
					.prepareStatement("select * from vendor_trip_sheet_parent where tripId="
							+ tripId + "");
			rs = pst.executeQuery();

			if (rs.next()) {
				pst1 = con
						.prepareStatement("update vendor_trip_sheet_parent set downloadStatus='downloaded', vehicle_imei='"+imei+"', vehicleNo='"+vehicleNo+"' where tripId="
								+ tripId
								+ " and ( ( status!='started' or status!='stopped' ) or status is null ) ");
				pst1.executeUpdate();
				retVal = 1;

				System.out
						.println(" update vendor_trip_sheet_parent set downloadStatus='downloaded', vehicle_imei='"+imei+"', vehicleNo='"+vehicleNo+"' where tripId="
								+ tripId
								+ " and ( ( status!='started' or status!='stopped' ) or status is null ) ");
				DbConnect.closeStatement(pst);

			} else {
				System.out
						.println("Trackkkkkk......... inserting into vendor_trip_sheet_parent");
				pst1 = con
						.prepareStatement("insert into vendor_trip_sheet_parent(tripId, vehicle_imei,downloadStatus,escort, vehicleNo, status ) values ("
								+ tripId
								+ ",'"
								+ imei

								+ "', 'downloaded','NO','"
								+ vehicleNo
								+ "', 'initial')");
				pst1.executeUpdate();

				System.out.println("..............sdfsdfsdf..................");
				pst1 = con
						.prepareStatement("insert into vendor_trip_sheet(tripId,employeeId,showStatus,insertedOrder,keypin) select tripId,employeeId,'No Show',routedOrder,round(rand()*10000) from trip_details_child  where tripId="
								+ tripId + " ");
				System.out
						.println("insert into vendor_trip_sheet(tripId,employeeId,showStatus,insertedOrder,keypin) select tripId,employeeId,'No Show',routedOrder,round(rand()*10000) from trip_details_child  where tripId="
								+ tripId + " ");
				pst1.executeUpdate();
				retVal = 1;
			}

		} catch (Exception e) {
			System.out.println("ërror" + e);
		} finally {
			DbConnect.closeStatement(pst);
			DbConnect.closeStatement(pst1);
			DbConnect.closeConnection(con);
		}

		return retVal;
	}

	public int stopTrip(String tripId) {
		DbConnect ob = null;
		Connection con = null;
		PreparedStatement pst = null;

		int retVal = 0;
		try {

			ob = DbConnect.getInstance();

			con = ob.connectDB();
			String onTimeStatusQuery = "";
			TripDetailsDto tripDto = new MobileTripSheetDao()
					.getVehicleTripSheet(tripId);
			if (tripDto != null && tripDto.getTrip_log().equalsIgnoreCase("IN")) {
				pst = con
						.prepareStatement("update vendor_trip_sheet set curStatus='OUT',outTime=curtime() "
								+ " where tripId="
								+ tripId
								+ " and showStatus='Show'");
				pst.executeUpdate();
				String onTimeStatus = getLiveOnTimeStatus(tripId);
				onTimeStatusQuery = ", onTimeStatus='" + onTimeStatus
						+ "',  logTime=curtime()  ";

			}
System.out.println("update vendor_trip_sheet_parent set status='stopped',downloadStatus='downloaded',stopTime=curtime()  "
		+ onTimeStatusQuery + " where tripId=" + tripId);
			pst = con
					.prepareStatement("update vendor_trip_sheet_parent set status='stopped',  stopTime=curtime()  "
							+ onTimeStatusQuery + " where tripId=" + tripId);

			retVal = pst.executeUpdate();
			System.out.println("stopped the trip"+retVal);

		} catch (Exception e) {
			System.out.println("ërror" + e);
		} finally {
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);
		}

		return retVal;

	}

	public int panicAlarm(String imei) {
		DbConnect ob = null;
		Connection con = null;
		PreparedStatement pst=null,pst1 = null;
		ResultSet rs = null,rs1 = null;
		int retVal = 0;
		try {

			ob = DbConnect.getInstance();
			con = ob.connectDB();
			/*
			 * pst=con.prepareStatement(
			 * "select vehicleId,tripId from vehicle_position where vehicleId="
			 * +
			 * "(select vehicleId from driver_logins dl where dl.status='logged' "
			 * + " and dl.imei_number='"+imei+"') and logstatus='danger' ");
			 * rs=pst.executeQuery(); if(rs.next()) { retVal=0; } else{
			 */pst = con
					.prepareStatement("insert into vehicle_position(vehicleId,lattitude,longitude,"
							+ "logstatus,tripId,status) select vehicleId,lattitude,longitude,"
							+ "'danger',tripId,status from vehicle_position where vehicleId="
							+ "(select vehicleId from driver_logins dl where dl.status='logged' "
							+ " and dl.imei_number='"
							+ imei
							+ "') order by date_time desc limit 1 ");

			 retVal = pst.executeUpdate();
				
			 pst1=con.prepareStatement("select tripId from vehicle_position where vehicleId=(select vehicleId from driver_logins dl where dl.status='logged'  and dl.imei_number='"+imei+"')");
			 rs1=pst1.executeQuery();
			 String tripid="";
			 if(rs1.next())
			 {
				  tripid=rs1.getString("tripId");
				 
			 }
			 
			
			PanicDto panicDto = new PanicDto();
			panicDto.setAlarmCause("");
			panicDto.setTripId(tripid);
			panicDto.setPrimaryActiontakenBy("");
			panicDto.setPrimaryAction("No Action Taken");
		    panicDto.setActivatedby("IN DRIVER APP");
			
			new PanicDao().TakePanicAction(panicDto);
			
			
	
		} catch (Exception e) {
			System.out.println("ërror" + e);
		} finally {
			DbConnect.closeStatement(pst,pst1);
			DbConnect.closeConnection(con);
			DbConnect.closeResultSet(rs,rs1);
		}
		return retVal;

	}
	public int panicAlarm(String imei,String activatedBy ) {
		DbConnect ob = null;
		Connection con = null;
		PreparedStatement pst=null,pst1 = null;
		ResultSet rs = null,rs1 = null;
		int retVal = 0;
		try {

			ob = DbConnect.getInstance();
			con = ob.connectDB();
			/*
			 * pst=con.prepareStatement(
			 * "select vehicleId,tripId from vehicle_position where vehicleId="
			 * +
			 * "(select vehicleId from driver_logins dl where dl.status='logged' "
			 * + " and dl.imei_number='"+imei+"') and logstatus='danger' ");
			 * rs=pst.executeQuery(); if(rs.next()) { retVal=0; } else{
			 */pst = con
					.prepareStatement("insert into vehicle_position(vehicleId,lattitude,longitude,"
							+ "logstatus,tripId,status) select vehicleId,lattitude,longitude,"
							+ "'danger',tripId,status from vehicle_position where vehicleId="
							+ "(select vehicleId from driver_logins dl where dl.status='logged' "
							+ " and dl.imei_number='"
							+ imei
							+ "') order by date_time desc limit 1 ");

			 retVal = pst.executeUpdate();
				
			 pst1=con.prepareStatement("select tripId from vehicle_position where vehicleId=(select vehicleId from driver_logins dl where dl.status='logged'  and dl.imei_number='"+imei+"') order by date_time desc limit 1");
			 rs1=pst1.executeQuery();
			 String tripid="";
			 if(rs1.next())
			 {
				  tripid=rs1.getString("tripId");
				 
			 }
			 
			
			PanicDto panicDto = new PanicDto();
			panicDto.setAlarmCause("");
			panicDto.setTripId(tripid);
			panicDto.setPrimaryActiontakenBy("");
			panicDto.setPrimaryAction("No Action Taken");
			panicDto.setActivatedby(activatedBy);
			
			new PanicDao().TakePanicAction(panicDto);
			
			
	
		} catch (Exception e) {
			System.out.println("ërror" + e);
		} finally {
			DbConnect.closeStatement(pst,pst1);
			DbConnect.closeConnection(con);
			DbConnect.closeResultSet(rs,rs1);
		}
		return retVal;

	}

	public int updateTime(String tripId, String tripTime, String userName) {

		DbConnect ob = null;
		Connection con = null;
		PreparedStatement pst = null;
		int retVal = 0;
		try {

			ob = DbConnect.getInstance();
			con = ob.connectDB();
			System.out
					.println(" update vendor_trip_sheet_parent set logTime='"
							+ tripTime
							+ "', authenticatedBy=(select id from escort where escortClock='"
							+ userName + "' )  where tripId=" + tripId);
			pst = con
					.prepareStatement(" update vendor_trip_sheet_parent set logTime='"
							+ tripTime
							+ "', authenticatedBy=(select id from escort where escortClock='"
							+ userName + "' )  where tripId=" + tripId);
			retVal = pst.executeUpdate();
			pst = con
					.prepareStatement("update vendor_trip_sheet set curStatus='OUT',outTime='"
							+ tripTime
							+ "' where tripId="
							+ tripId
							+ " and showStatus='YES' and tripId in (select id from trip_details where trip_log='IN' and id="
							+ tripId + ") ");
			retVal += pst.executeUpdate();
		} catch (Exception e) {
			System.out.println("ërror" + e);
		} finally {
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);
		}
		return retVal;
	}

	public int escortGetIn(String tripId, String userName, double latitude,
			double longitude) {

		DbConnect ob = null;
		Connection con = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		PreparedStatement pst1 = null;

		int retVal = 0;
		try {

			ob = DbConnect.getInstance();
			con = ob.connectDB();
			pst1 = con
					.prepareStatement("select * from  vendor_trip_sheet_escort where  showstatus='Show' and tripId='"
							+ tripId + "' ");
			rs = pst1.executeQuery();
			if (rs.next()) {
				setMessage("Security is already authenticated");
			} else {
				String updateQuery1 = " update vendor_trip_sheet_parent set escort='YES', escortClock= '"
						+ userName
						+ "' where tripId="
						+ tripId;
				System.out.println(updateQuery1);
				pst = con
						.prepareStatement(updateQuery1);
				retVal = pst.executeUpdate();
				String escortGetInQuery =" update vendor_trip_sheet_escort set showStatus='Show', curStatus='IN', inLatitude= '"
						+ latitude
						+ "', inLongitude='" + longitude + "', inTime=curtime() where tripId="
						+ tripId;
				System.out.println("escortGetInQuery "+escortGetInQuery);
				pst = con
						.prepareStatement(escortGetInQuery);
				retVal = pst.executeUpdate();
			}
		} catch (Exception e) {

			System.out.println("ërror" + e);
			setMessage("Error : " + e);
		} finally {
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);
		}
		return retVal;
	}

	public int escortGetOut(String tripId, String userName, double latitude,
			double longitude) {

		DbConnect ob = null;
		Connection con = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		PreparedStatement pst1 = null;

		int retVal = 0;
		try {

			ob = DbConnect.getInstance();
			con = ob.connectDB();
			pst1 = con
					.prepareStatement("select * from  vendor_trip_sheet_parent where  escort='YES' and tripId='"
							+ tripId + "' ");
			rs = pst1.executeQuery();
			if (rs.next()) {
 
				pst = con
						.prepareStatement(" update vendor_trip_sheet_escort set showStatus='Show', curStatus='OUT', outLatitude= '"
								+ latitude
								+ "', outLongitude='" + longitude + "', outTime=curtime() where tripId="
								+ tripId);
				retVal = pst.executeUpdate();
			}
		} catch (Exception e) {

			System.out.println("ërror" + e);
			setMessage("Error : " + e);
		} finally {
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);
		}
		return retVal;
	}


	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int employeeCheckOut(String tripId, String empCode, double latitude,
			double longitude,long nuance) {
		DbConnect ob = DbConnect.getInstance();
		Connection con = ob.connectDB();
		PreparedStatement pst = null;
		PreparedStatement pst1 = null;
		ResultSet rs = null;
		int retVal = 0;
		String time=OtherFunctions.changeDateFormat(new Date(nuance), "HH:mm:ss");

		try {
			String query ="";
			
			String checkQuery = "select * from trip_details where id=" + tripId;
			pst1 = con.prepareStatement(checkQuery);
			rs = pst1.executeQuery();
			if(rs.next()) {
			if(rs.getString("trip_log").equalsIgnoreCase("OUT")) {
				query = "update vendor_trip_sheet set showStatus='Show',curStatus='OUT',latitude="
						+ latitude
						+ ",longitude="
						+ longitude
						+ ",outTime='"+time+"'  where employeeId="
						+ empCode + " and  tripid=" + tripId;
				
			} else {
				query = "update vendor_trip_sheet set showStatus='Show',curStatus='OUT', outTime='"+time+"'  where employeeId="
						+ empCode + " and  tripid=" + tripId;
			}
			
			
				
			 
		 		pst = con
						.prepareStatement(query);
				retVal = pst.executeUpdate();
			}
		 
		} catch (Exception e) {
			System.out.println("Error in dao emp get out" + e);
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(pst, pst1);

			DbConnect.closeConnection(con);
		}
		return retVal;
	}

	public double empLat = 0.0;
	public double empLon = 0.0;

	public int CheckOrUpdateEmpPos(String tripId, String empCode,
			double latitude, double longitude) {
		DbConnect ob = DbConnect.getInstance();
		Connection con = ob.connectDB();
		PreparedStatement pst = null;
		PreparedStatement pst1 = null;
		ResultSet rs = null;
		int subscriptionId = 0;
		int retVal = 0;
		int geoStat = 0;
		try {
			pst1 = con
					.prepareStatement("select subscriptionID,ifnull(geocodestatus,0),latitude,longitude from employee_subscription where empId="
							+ empCode
							+ " and subscriptionStatus in ('pending','subscribed')");
			rs = pst1.executeQuery();
			if (rs.next()) {
				subscriptionId = rs.getInt("subscriptionID");
				geoStat = rs.getInt(2);
				if (geoStat >= 5) {
					empLat = rs.getDouble("latitude");
					empLon = rs.getDouble("longitude");
					retVal = 1;
				} else {
					if (geoStat == 0) {
						empLat = latitude;
						empLon = longitude;
					}
					pst = con
							.prepareStatement("update employee_subscription set latitude=? ,  longitude=? , geocodestatus="
									+ (geoStat + 1)
									+ " where subscriptionId="
									+ subscriptionId
									+ " ");
					pst.setDouble(1, (latitude + empLat) / 2);
					pst.setDouble(2, (longitude + empLon) / 2);
					pst.executeUpdate();
				}
			}

		} catch (Exception e) {
			System.out.println("Error in dao emp get out" + e);
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(pst, pst1);
			DbConnect.closeConnection(con);
		}
		return retVal;
	}

	public int updateCheckOutIsInCorrect(String tripId, String empCode) {
		DbConnect ob = DbConnect.getInstance();
		Connection con = ob.connectDB();
		PreparedStatement pst = null;
		int retVal = 0;

		try {

			pst = con
					.prepareStatement("update vendor_trip_sheet set isCorrectPos='no'  where tripId="
							+ tripId + " and employeeId=" + empCode + " ");
			pst.executeUpdate();
		} catch (Exception e) {
			System.out.println("Error in dao emp get out" + e);
		} finally {
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);
		}
		return retVal;

	}

	public int updateDistanceCovered(String tripId, double distanceCovered,
			long timeElapsed) {

		DbConnect ob = DbConnect.getInstance();
		Connection con = ob.connectDB();
		PreparedStatement pst = null;

		int retVal = 0;

		try {
			pst = con
					.prepareStatement("update vendor_trip_sheet_parent set distanceCovered="
							+ distanceCovered
							+ ", timeElapsed="
							+ timeElapsed
							+ " where   tripid=" + tripId);
			retVal = pst.executeUpdate();
		} catch (Exception e) {
			System.out.println("Error in dao emp get out" + e);
		} finally {

			DbConnect.closeStatement(pst);

			DbConnect.closeConnection(con);
		}
		return retVal;

	}

	/*
	 * returns max time that driver can be considered as on time in minute
	 */
	public int getAffordableOnTimeDelay(String siteId) {
		DbConnect ob = DbConnect.getInstance();
		Connection con = ob.connectDB();

		PreparedStatement pst1 = null;
		ResultSet rs = null;
		int retVal = 0;

		try {

			String query = " select * from site_settings ss right join settings_table st on ss.settingsId=st.id where property='AFFORDABLE ON TIME DELAY'  and (site="
					+ siteId
					+ " or (site is null and type='SINGLE'))  curdate()<=fromDate and (toDate is null or todate>=curdate()) order by site desc";
			pst1 = con.prepareStatement(query);
			rs = pst1.executeQuery();
			if (rs.next()) {
				retVal = Integer.parseInt(rs.getString("value"));
			}
		} catch (Exception e) {
			System.out
					.println("Error in dao get affordable time delay in minute"
							+ e);
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(pst1);

			DbConnect.closeConnection(con);
		}
		return retVal;
	}

	/*
	 * returns max time that driver can be considered as on time in minute
	 */
	public int getSiteFromTripId(String tripId) {
		DbConnect ob = DbConnect.getInstance();
		Connection con = ob.connectDB();

		PreparedStatement pst1 = null;
		ResultSet rs = null;
		int retVal = 0;

		try {
			String query = " select siteId from trip_details where id="
					+ tripId;
			pst1 = con.prepareStatement(query);
			rs = pst1.executeQuery();
			if (rs.next()) {
				retVal = rs.getInt("siteId");
			}
		} catch (Exception e) {
			System.out
					.println("Error in dao get affordable time delay in minute"
							+ e);
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(pst1);

			DbConnect.closeConnection(con);
		}
		return retVal;
	}

	/*
	 * returns trip_date from tripId
	 */
	public Date getTripDate(String tripId) {
		DbConnect ob = DbConnect.getInstance();
		Connection con = ob.connectDB();

		PreparedStatement pst1 = null;
		ResultSet rs = null;
		Date retVal = null;

		try {
			String query = " select trip_date from trip_details where id="
					+ tripId;
			pst1 = con.prepareStatement(query);
			rs = pst1.executeQuery();
			if (rs.next()) {
				retVal = rs.getDate("trip_date");
			}
		} catch (Exception e) {
			System.out.println("Error in dao get trip date in minute" + e);
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(pst1);

			DbConnect.closeConnection(con);
		}
		return retVal;
	}

	/*
	 * returns max time that driver can be considered as on time in minute
	 */
	public String getActualLogTime(String tripId) {
		DbConnect ob = DbConnect.getInstance();
		Connection con = ob.connectDB();

		PreparedStatement pst1 = null;
		ResultSet rs = null;
		String retVal = null;

		try {
			String query = " select trip_time from trip_details where id="
					+ tripId;
			pst1 = con.prepareStatement(query);
			rs = pst1.executeQuery();
			if (rs.next()) {
				retVal = rs.getString("trip_time");
			}
		} catch (Exception e) {
			System.out.println("Error in dao get acutal time in minute" + e);
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(pst1);

			DbConnect.closeConnection(con);
		}
		return retVal;
	}

	public String getLiveOnTimeStatus(String tripId) {
		TripServiceDao dao = new TripServiceDao();
		int affordableDelay = dao.getAffordableOnTimeDelay(String.valueOf(dao
				.getSiteFromTripId(tripId)));
		String actualTime = dao.getActualLogTime(tripId);
		Date tripDate = getTripDate(tripId);

		// actualTime = "14:10";
		System.out.println("Actual Time :" + actualTime);
		long delay = OtherFunctions.getTimeDifference(tripDate, actualTime,
				"minute");
		// delay = delay * -1;
		System.out.println("Delay :" + delay);
		System.out.println("Affordable delay " + affordableDelay);
		return ((affordableDelay < delay) ? "Not On Time" : "On Time");

	}

	public void getDriverNameAndMob(TripDetailsDto dto) {
		DbConnect ob = DbConnect.getInstance();
		Connection con = ob.connectDB();

		PreparedStatement pst1 = null;
		ResultSet rs = null;
		String retVal = null;

		try {
			String query = "select d.name,d.contact from driver d,trip_details td where td.id="
					+ dto.getId() + " and  td.driverid=d.id";
			System.out.println(query);
			pst1 = con.prepareStatement(query);
			rs = pst1.executeQuery();
			if (rs.next()) {
				dto.setDriverName(rs.getString("name"));
				dto.setDriverContact(rs.getString("contact"));
			}
		} catch (Exception e) {
			System.out.println("Error in dao get acutal time in minute" + e);
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(pst1);

			DbConnect.closeConnection(con);

		}
	}

	public int updateGeoCodePassive(String imei, double latitude,
			double longitude, long nuance) {
		 

		DbConnect ob = DbConnect.getInstance();
		Connection con = ob.connectDB();
		PreparedStatement pst = null;
		 
		 
		 
		int retVal = 0;
		try {
			 
		   pst = con
					.prepareStatement("update full_vehicle_position,device_vehicle dv, device d  set full_vehicle_position.date_time=?, full_vehicle_position.lattitude=?, full_vehicle_position.longitude=?    where full_vehicle_position.dvId=dv.id and  dv.status='active' and dv.deviceId=d.id and  d.imei='"+imei+"' ");
			  
			 
			  
		   //System.out.println("Time (stationary) :" +new java.sql.Timestamp(nuance).toGMTString());
			pst.setTimestamp(1,new  java.sql.Timestamp( nuance));
			 
			pst.setDouble(2, latitude);
			pst.setDouble(3, longitude);
			  
			retVal = pst.executeUpdate();
 
			
		} catch (Exception e) {
			System.out.println("error in update geocode passive dao" + e);

		} finally {
 
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);
		}
		return retVal;

	}
	
	
	public ArrayList<TripDetailsDto> getStationaryVehiclePosition(String branch) {
		DbConnect ob = DbConnect.getInstance();
		PreparedStatement pst = null;
		PreparedStatement pst1 = null;
		ResultSet rs = null;
		ResultSet rs1 = null;
		TripDetailsDto tripDto = null;
 
		Connection con = ob.connectDB();
		ArrayList<TripDetailsDto> tripDtailsDtos = new ArrayList<TripDetailsDto>();
 
		try {
			
			String query = "select v.id vehicleid, v.regNo, vp.lattitude,  vp.longitude,  vp.date_time date_time, 'stationary' logstatus from vehicles v, full_vehicle_position vp, device d, device_vehicle dv, site_vehicle sv, site s, (select v.regNo,max(vp.date_time) date_time, 'stationary' from vehicles v, full_vehicle_position vp, device d, device_vehicle dv where v.id=dv.vehicleId and d.id=dv.deviceId and dv.id=vp.dvId group by dv.vehicleId ) ddv,  (select vehicleId, max( date_time) date_time from vehicle_position group by vehicleId) rp  where v.id=dv.vehicleId and d.id=dv.deviceId and dv.id=vp.dvId  and ddv.date_time=vp.date_time and ddv.regNo=v.regNo and v.vehicletype= sv.vehicleTypeId and s.id=sv.siteId and s.branch=" + branch + " and rp.vehicleId=v.id and rp.date_time<vp.date_time group by regNo having vp.date_time=max(vp.date_time) order by vp.date_time desc";

			if(SettingsConstant.comp.equalsIgnoreCase("keo"))
			{
				query="SELECT v.id vehicleid, v.regNo,vp.lattitude,vp.longitude,v.regNo, vp.date_time date_time FROM VEHICLE_POSITION vp JOIN vehicles v on vp.vehicleId=v.id group by regno order by vp.date_time desc ";
			}
			
			pst = con
					.prepareStatement(query);
			//System.out.println(query);
			
			rs=pst.executeQuery();
			while (rs.next()) {
				
				tripDto = new TripDetailsDto();
			 
				tripDto.setVehicle(rs.getString("vehicleid"));
 				tripDto.setId("-1");
				tripDto.setTrip_code(" ");				
				tripDto.setTrip_date("");
				tripDto.setVehicleNo(rs.getString("regNo"));			 
				tripDto.setLatitude(rs.getString("lattitude"));			 
				tripDto.setLongitude(rs.getString("longitude"));			 
				if(SettingsConstant.comp.equalsIgnoreCase("keo"))
				{
					tripDto.setStatus("");	
				}
				else
				{
					tripDto.setStatus(rs.getString("logstatus"));			 
							
				}
				tripDto.setTrip_log(" ");				 
				tripDto.setIsSecurity(" ");
				
 				tripDto.setLadyInCount(0);
				tripDto.setEmpInCount(0);
				tripDto.setEmpCount(0);
 				tripDto.setPanicAck(" ");
 				 
 				tripDtailsDtos.add(tripDto);
			}

		} catch (Exception e) {
			System.out.println("Error getStationaryVehiclePosition " + e);
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeResultSet(rs1);
			DbConnect.closeStatement(pst);
			DbConnect.closeStatement(pst1);
			DbConnect.closeConnection(con);
		}
		return tripDtailsDtos;
	}

	public int forceStopTrip(String tripId) {
		//  
		DbConnect ob = null;
		Connection con = null;
		PreparedStatement pst = null;

		int retVal = 0;
		try {

			ob = DbConnect.getInstance();

			con = ob.connectDB();
			String onTimeStatusQuery = "";
			TripDetailsDto tripDto = new MobileTripSheetDao()
					.getVehicleTripSheet(tripId);
			if (tripDto != null && tripDto.getTrip_log().equalsIgnoreCase("IN")) {
				/*pst = con
						.prepareStatement("update vendor_trip_sheet set curStatus='OUT',outTime=curtime() "
								+ " where tripId="
								+ tripId
								+ " and showStatus='Show'");
				pst.executeUpdate();*/
				String onTimeStatus = getLiveOnTimeStatus(tripId);
				onTimeStatusQuery = ", onTimeStatus='" + onTimeStatus
						+ "',  logTime=curtime()  ";

			}

			pst = con
					.prepareStatement("update vendor_trip_sheet_parent set status='stopped',  stopTime=curtime(), forceStop=true  "
							+ onTimeStatusQuery + " where tripId=" + tripId);

			retVal = pst.executeUpdate();

		} catch (Exception e) {
			System.out.println("ërror" + e);
		} finally {
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);
		}

		return retVal;

	}

	
	public  ArrayList<TripDetailsDto> getTripsByPassword(String Password)
	{
		DbConnect ob = DbConnect.getInstance();
		PreparedStatement pst = null;
		ResultSet rs = null;
		Connection con = ob.connectDB();
		ArrayList<TripDetailsDto> tripDtailsDtos = new ArrayList<TripDetailsDto>();
 
		try {
			ob = DbConnect.getInstance();
			con = ob.connectDB();
			String query="SELECT TD.ID,TD.TRIP_TIME,DATE_FORMAT(TD.TRIP_DATE,'%d-%m-%Y') as TRIP_DATE,TD.TRIP_LOG,TD.DRIVERPSWD FROM TRIP_DETAILS TD JOIN DRIVER D ON D.ID=TD.DRIVERID JOIN VENDOR_TRIP_SHEET_PARENT VTP ON VTP.TRIPID=TD.ID AND VTP.STATUS!='stopped' WHERE RIGHT(D.CONTACT,6)=? AND TD.TRIP_DATE BETWEEN CURDATE() AND DATE_ADD(CURDATE(),INTERVAL 1 DAY)";
			
			if(SettingsConstant.comp.equalsIgnoreCase("tyko")){
				
				query="SELECT TD.ID,TD.TRIP_TIME,DATE_FORMAT(TD.TRIP_DATE,'%d-%m-%Y') as TRIP_DATE,TD.TRIP_LOG,TD.DRIVERPSWD FROM TRIP_DETAILS TD JOIN DRIVER D ON D.ID=TD.DRIVERID JOIN VENDOR_TRIP_SHEET_PARENT VTP ON VTP.TRIPID=TD.ID AND VTP.STATUS!='stopped' WHERE RIGHT(D.CONTACT,6)=? AND TD.TRIP_DATE = CURDATE() AND   if(VTP.STATUS='initial' ,  TIME_TO_SEC( timediff(concat(TD.trip_time,':00') ,curtime()))   between -1800 and 7200 ,   TIME_TO_SEC( timediff(concat(TD.trip_time,':00') ,curtime()))   <= 7200) ";
				

			}
			
			
			
			pst = con.prepareStatement(query);
			pst.setString(1, Password);
		    rs=pst.executeQuery();
		    while(rs.next())
		    {
		    	TripDetailsDto dto=new TripDetailsDto();
		    	dto.setId(rs.getString("id"));
		    	dto.setTrip_time(rs.getString("trip_time"));
		    	dto.setTrip_date(rs.getString("trip_date"));
		    	dto.setDriverPassword(rs.getString("driverpswd"));
		    	dto.setTrip_log(rs.getString("trip_log"));
		    	tripDtailsDtos.add(dto);
		    }
			
			
		}catch(Exception e){e.printStackTrace();}
		finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);
		}
		return tripDtailsDtos;
	}


	public int employeeNFCSwap(String tripId, long NAUNCE,
			String latitude, String longitude, String NFC) {
		// TODO Auto-generated method stub
		DbConnect ob = DbConnect.getInstance();
		Statement st = null,st2=null;
		PreparedStatement pst=null,pst2=null;
		ResultSet rs = null,rs1=null;
		int res=0;
		String empid="";
		Connection con = ob.connectDB();
		try {
			String selectqry="select id from employee where PersonnelNo='"+NFC+"'";
			st=con.createStatement();
			rs=st.executeQuery(selectqry);
			if(rs.next()){
				empid=rs.getString("id");
				res=employeeCheckIn(tripId, empid,
						Double.parseDouble(latitude), Double.parseDouble(longitude),NAUNCE);
			}
			if(res==0){
				int count = 1;
				String tripLog="";
				st2 = con.createStatement();
				String countquery = "SELECT TDC.ROUTEDORDER,TD.TRIP_LOG FROM TRIP_DETAILS_CHILD TDC,TRIP_DETAILS TD WHERE TD.ID=TDC.TRIPID AND TDC.TRIPID="+ tripId + " ORDER BY ROUTEDORDER DESC";
				rs1 = st2.executeQuery(countquery);
				if (rs1.next()) {
					count = rs1.getInt("INSERTEDORDER") + 1;
					tripLog=rs1.getString("TRIP_LOG");
					String distance=Float.toString(getDistance(count, tripId, tripLog,empid));
					String time = Double.toString(getTime(distance));
					pst = con.prepareStatement("INSERT INTO trip_details_child (tripId, employeeId, routedOrder, time, dist, transportType) VALUES (?, ?, ?, ?, ?, 'shift')");
					pst.setString(1, tripId);
					pst.setString(2, empid);
					pst.setInt(3, count);
					pst.setString(4, time);
					pst.setString(5, distance);
					pst.executeUpdate();
					
					String insertVTSquery = "INSERT INTO VENDOR_TRIP_SHEET(TRIPID,EMPLOYEEID,SHOWSTATUS,LATITUDE,LONGITUDE,INSERTEDORDER,INTIME) VALUES(?,?,'Show',?,?,?,?)";
					pst2=con.prepareStatement(insertVTSquery);
					pst2.setString(1, tripId);
					pst2.setString(2, empid);
					pst2.setString(3, latitude);
					pst2.setString(4, longitude);
					pst2.setInt(5, count);
					pst2.setString(6,Long.toString(NAUNCE));
					res=pst2.executeUpdate();
				}
			}
		}catch(Exception e){e.printStackTrace();}
		finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);
		}
		return res;
	}
	public float getDistance(int routedOrder, String tripId, String empid,
			String logType) {
		float distance = 0;
		DbConnect ob = DbConnect.getInstance();
		Connection con = null;
		ResultSet rs = null;
		Statement st = null;
		con = ob.connectDB();
		String src = null, dest = null;
		try {
			if (logType.equalsIgnoreCase("IN")) {
				if (routedOrder > 1) {
					src = "(select landmarkId from trip_details_child where tripId='"
							+ tripId
							+ "' and routedOrder='"
							+ (routedOrder - 1) + "')";
				}
			} else {
				src = "(select landmark from site where id=1)";
			}
			dest = "select landMark from employee_subscription where empID='"+empid+"'";
			String getInDist = "select distance from distchart where (srcId="
					+ src + " and destId=" + dest + ") or (destId=" + src
					+ " and srcId=" + dest + ")";

			st = con.createStatement();
			rs = st.executeQuery(getInDist);
			if (rs.next()) {
				distance = rs.getFloat("distance");
			}

		} catch (SQLException e) {
			System.out.println("Error in TripServiceDao@getDistance" + e);
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);
		}

		return distance;
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
			System.out.println("Error in TripServiceDao@getTime" + e);
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);
		}
		return time;
	}


}
