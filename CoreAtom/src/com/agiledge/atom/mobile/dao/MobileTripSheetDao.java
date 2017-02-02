package com.agiledge.atom.mobile.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;

import com.agiledge.atom.commons.OtherFunctions;
import com.agiledge.atom.constants.SettingsConstant;
import com.agiledge.atom.dbConnect.DbConnect;
import com.agiledge.atom.dto.TripDetailsChildDto;
import com.agiledge.atom.dto.TripDetailsDto;


public class MobileTripSheetDao {
	public TripDetailsDto getVehicleTripSheetFromImei(String imei ) {
 
		DbConnect ob = null;
		Connection con = null;
		ob = DbConnect.getInstance();
		con = ob.connectDB();
		TripDetailsDto tripDetailsDtoObj = new TripDetailsDto();
		TripDetailsChildDto tripDetailsChildDtoObj = null;
		ArrayList<TripDetailsChildDto> tripSheetChild = null;
		PreparedStatement pst = null;
		Statement st = null;
		ResultSet rs = null;
		ResultSet rs1 = null;
		try {
			st = con.createStatement();
			pst = con
					.prepareStatement("SELECT d.name,t.escortPswd, t.siteId, t.id,t.trip_code,t.trip_date,t.trip_time,t.trip_log,t.routeid,t.trip_code,v.regNo,t.security_status,t.status,vt.type,t.distance,t.travelTime,t.preTripId,t.distanceFromPreTrip, ifnull(vtp.status,'initial') status, ifnull(vtp.escort,'NO') escort, vtp.distanceCovered, vtp.timeElapsed, vtp.downloadStatus FROM trip_details t left join vendor_trip_sheet_parent vtp on t.id=vtp.tripId ,vehicles v,driver_logins dl,vehicle_type vt,driver d where dl.vehicleId=t.vehicle and dl.imei_number=? and dl.status='logged' and dl.tripId=t.id and vt.id=t.vehicle_type  and v.id=t.vehicle and d.id=t.driverid ");
			 String testQuery = "SELECT d.name,t.escortPswd, t.siteId, t.id,t.trip_code,t.trip_date,t.trip_time,t.trip_log,t.routeid,t.trip_code,v.regNo,t.security_status,t.status,vt.type,t.distance,t.travelTime,t.preTripId,t.distanceFromPreTrip, ifnull(vtp.status,'initial') status, ifnull(vtp.escort,'NO') escort, vtp.distanceCovered, vtp.timeElapsed, vtp.downloadStatus FROM trip_details t left join vendor_trip_sheet_parent vtp on t.id=vtp.tripId ,vehicles v,driver_logins dl,vehicle_type vt,driver d where dl.vehicleId=t.vehicle and dl.imei_number=? and dl.status='logged' and dl.tripId=t.id and vt.id=t.vehicle_type  and v.id=t.vehicle and d.id=t.driverid ";
			 testQuery =  testQuery.replace("?", "'%s'");
			 testQuery = String.format(testQuery, imei );
			 System.out.println("-- " + testQuery);
			pst.setString(1, imei);
			 
			rs = pst.executeQuery();
			if (rs.next()) {
				System.out.println("..............");
				tripDetailsDtoObj.setSiteId(rs.getString("siteId"));
				tripDetailsDtoObj.setId(rs.getString("id"));
				tripDetailsDtoObj.setTrip_code(rs.getString("trip_code"));
				tripDetailsDtoObj.setTrip_date(rs.getDate("trip_date")
						.toString());
				tripDetailsDtoObj.setTrip_time(rs.getString("trip_time"));
				tripDetailsDtoObj.setTrip_log(rs.getString("trip_log"));
				tripDetailsDtoObj.setRouteId(rs.getString("routeid"));
				tripDetailsDtoObj.setVehicle_type(rs.getString("type"));
				tripDetailsDtoObj
						.setIsSecurity(rs.getString("security_status"));
				tripDetailsDtoObj.setStatus(rs.getString("status"));
				tripDetailsDtoObj.setEscort(rs.getString("escort"));
				tripDetailsDtoObj.setTimeElapsed(rs.getLong("timeElapsed"));
				tripDetailsDtoObj.setDistanceCovered((rs.getDouble("distanceCovered")));
				tripDetailsDtoObj.setDownloadStatus(rs.getString("downloadStatus"));
				tripDetailsDtoObj.setVehicleNo(rs.getString("regNo"));
				
				tripDetailsDtoObj.setDistance(rs.getString("distance"));
				tripDetailsDtoObj.setTravelTime(rs.getString("travelTime"));
				tripDetailsDtoObj.setEscortPassword(rs.getString( "escortPswd"));
				tripDetailsDtoObj.setDriverName(rs.getString("name"));
				
				//	tripDetailsDtoObj.setPreviousTripId(rs.getString("preTripId"));
		//		tripDetailsDtoObj.setDistanceFromPrevioustrip(rs.getFloat("distanceFromPreTrip"));
				// tripSheetChild = null;
				tripSheetChild = new ArrayList<TripDetailsChildDto>();
		//		if (status.equals("modified") || status.equals("addmod")) {
		//			rs1 = st.executeQuery("select tdc.empid as employeeId,e.displayname as EmployeeName,a.area,p.place,l.id as landmarkId,l.landmark, tdc.status from trip_details_modified tdc,employee e,area a,place p,landmark l where e.id=tdc.empid and tdc.tripId="
			//				+ rs.getString(1)
			//				+ " and tdc.status='modified' and tdc.landmarkId=l.id and l.place=p.id and p.area=a.id order by tdc.routedOrder");
			//	} else {
				//System.out.println("Trip Id :" + tripDetailsDtoObj.getId());
				//System.out.println("Query : select emp.Gender, tdc.employeeId,e.displayname as EmployeeName,a.area,p.place,l.id as landmarkId,l.landmark,tdc.time,tdc.dist,vts.showStatus, ifnull(vts.curStatus,'INIT') curStatus,tdc.dist,tdc.time, ifnull(vts.keypin,'') keypin from employee e,area a,place p,landmark l,trip_details_child tdc join employee emp on tdc.employeeId=emp.id left outer join vendor_trip_sheet vts on tdc.tripId=vts.tripId and tdc.employeeId=vts.employeeId where e.id=tdc.employeeId and tdc.tripId="+tripDetailsDtoObj.getId()+"  and tdc.landmarkId=l.id and l.place=p.id and p.area=a.id order by tdc.routedOrder" );
				String empquery="select e.id,e.gender,e.personnelNo,e.loginid, tdc.employeeId,e.displayname as EmployeeName,a.area,p.place,l.id as landmarkId,l.landmark,tdc.time,tdc.dist,vts.showStatus, ifnull(vts.curStatus,'INIT') curStatus, ifnull(vts.keypin,'') keypin,l.latitude,l.longitude from employee e,area a,place p,landmark l,trip_details_child tdc  left outer join vendor_trip_sheet vts on tdc.tripId=vts.tripId and tdc.employeeId=vts.employeeId where e.id=tdc.employeeId  and tdc.tripId="+tripDetailsDtoObj.getId()+"  and tdc.landmarkId=l.id and l.place=p.id and p.area=a.id order by tdc.routedOrder";	
				if(SettingsConstant.comp.equalsIgnoreCase("gss")||SettingsConstant.comp.equalsIgnoreCase("gss1"))
				{
					empquery="select e.id,e.gender,e.personnelNo,e.loginid, tdc.employeeId,e.displayname as EmployeeName,a.area,p.place,l.id as landmarkId,l.landmark,tdc.time,tdc.dist,vts.showStatus, ifnull(vts.curStatus,'INIT') curStatus, ifnull(vts.keypin,'') keypin,case when td.trip_time between '19:00' and '08:00' then ifnull(e.emp_lat,l.latitude) else l.latitude  end as latitude,case when td.trip_time between '19:00' and '08:00' then ifnull(e.emp_long,l.longitude) else l.longitude  end as longitude from employee e,area a,place p,landmark l,trip_details_child tdc join trip_details td on td.id=tdc.tripid left outer join vendor_trip_sheet vts on tdc.tripId=vts.tripId and tdc.employeeId=vts.employeeId where e.id=tdc.employeeId  and  tdc.tripId="+tripDetailsDtoObj.getId()+" and tdc.landmarkId=l.id and l.place=p.id and p.area=a.id order by tdc.routedOrder";
				}
				rs1 = st.executeQuery(empquery);
					//                       select e.personnelNo, tdc.employeeId,e.displayname as EmployeeName,a.area,p.place,l.id as landmarkId,l.landmark,tdc.time,tdc.dist,vts.showStatus, ifnull(vts.curStatus,'INIT') curStatus,tdc.dist,tdc.time, ifnull(vts.keypin,'') keypin from employee e,area a,place p,landmark l,trip_details_child tdc  left outer join vendor_trip_sheet vts on tdc.tripId=vts.tripId and tdc.employeeId=vts.employeeId where e.id=tdc.employeeId  
					 
				//}
				while (rs1.next()) {
					//System.out.println("select emp.Gender, tdc.employeeId,e.displayname as EmployeeName,a.area,p.place,l.id as landmarkId,l.landmark,tdc.time,tdc.dist,vts.showStatus, ifnull(vts.curStatus,'INIT') curStatus,tdc.dist,tdc.time, ifnull(vts.keypin,'') keypin from employee e,area a,place p,landmark l,trip_details_child tdc join employee emp on tdc.employeeId=emp.id left outer join vendor_trip_sheet vts on tdc.tripId=vts.tripId and tdc.employeeId=vts.employeeId where e.id=tdc.employeeId and tdc.tripId="+tripDetailsDtoObj.getId()+"  and tdc.landmarkId=l.id and l.place=p.id and p.area=a.id order by tdc.routedOrder");
					tripDetailsChildDtoObj = new TripDetailsChildDto();
					tripDetailsChildDtoObj.setGender(rs1.getString("Gender"));
					tripDetailsChildDtoObj.setTripId(  rs.getString("id"));
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
					tripDetailsChildDtoObj.setGetInStatus(rs1.getString("curStatus"));
					tripDetailsChildDtoObj.setShowStatus(rs1.getString("showStatus"));
					tripDetailsChildDtoObj.setTime(rs1.getString("time"));

					tripDetailsChildDtoObj.setLatitude(rs1.getString("latitude"));
					tripDetailsChildDtoObj.setLongitude(rs1.getString("longitude"));
					
					try {
						tripDetailsChildDtoObj
								.setDistance(rs1.getFloat("dist"));
						
					} catch (Exception e) {

					
					}
					if(SettingsConstant.empAuthinticationForComet.equalsIgnoreCase("pin"))
					{
					tripDetailsChildDtoObj.setKeyPin(rs1.getString("keypin"));
					}
					else if(SettingsConstant.empAuthinticationForComet.equalsIgnoreCase("loginid")||SettingsConstant.empAuthinticationForComet.equalsIgnoreCase("personnelNo"))
					{
						int loginidlength=rs1.getString("personnelNo").length();
						tripDetailsChildDtoObj.setKeyPin(rs1.getString("personnelNo").substring(loginidlength-4));
					}
					else
					{
					tripDetailsChildDtoObj.setKeyPin(rs1.getString("personnelNo"));
					}
					tripSheetChild.add(tripDetailsChildDtoObj);
					
				}
				tripDetailsDtoObj.setTripDetailsChildDtoList(tripSheetChild);
			}

			pst=con.prepareStatement("select * from vendor_trip_sheet_parent where tripId="+tripDetailsDtoObj.getId());
			rs=pst.executeQuery();
			if(rs.next())
			{
			tripDetailsDtoObj.setStatus(rs.getString("status"));	
			}
			else
			{
				tripDetailsDtoObj.setStatus("new");	
			}
			return tripDetailsDtoObj;
		} catch (Exception e) {
			System.out.println("Error in getVehicleTripSheet : " + e);
			return null;
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);
		}

	}
	
	
	public TripDetailsDto getVehicleTripSheetFromTripId(String  tripId) {

		DbConnect ob = null;
		Connection con = null;
		ob = DbConnect.getInstance();
		con = ob.connectDB();
		TripDetailsDto tripDetailsDtoObj = new TripDetailsDto();
		TripDetailsChildDto tripDetailsChildDtoObj = null;
		ArrayList<TripDetailsChildDto> tripSheetChild = null;
		PreparedStatement pst = null;
		Statement st = null;
		ResultSet rs = null;
		ResultSet rs1 = null;
		try {
			st = con.createStatement();
			pst = con
					.prepareStatement("SELECT t.id,t.trip_code,t.trip_date,t.trip_time,t.trip_log,t.routeid,t.trip_code,v.regNo,t.security_status,t.status,vt.type,t.distance,t.travelTime,t.preTripId,t.distanceFromPreTrip, ifnull(vtp.status,'initial') status, ifnull(vtp.escort,'NO') escort, vtp.distanceCovered, vtp.timeElapsed, vtp.downloadStatus FROM trip_details t left join vendor_trip_sheet_parent vtp on t.id=vtp.tripId ,vehicles v,driver_logins dl,vehicle_type vt where dl.vehicleId=t.vehicle and dl.imei_number=? and dl.status='logged' and vt.id=t.vehicle_type  and v.id=t.vehicle and t.id=?");
			pst.setString(1, tripId);
			 
			rs = pst.executeQuery();
			if (rs.next()) {
				tripDetailsDtoObj.setId(rs.getString("id"));
				tripDetailsDtoObj.setTrip_code(rs.getString("trip_code"));
				tripDetailsDtoObj.setTrip_date(rs.getDate("trip_date")
						.toString());
				tripDetailsDtoObj.setTrip_time(rs.getString("trip_time"));
				tripDetailsDtoObj.setTrip_log(rs.getString("trip_log"));
				tripDetailsDtoObj.setRouteId(rs.getString("routeid"));
				tripDetailsDtoObj.setVehicle_type(rs.getString("type"));
				tripDetailsDtoObj
						.setIsSecurity(rs.getString("security_status"));
				tripDetailsDtoObj.setStatus(rs.getString("status"));
				tripDetailsDtoObj.setEscort(rs.getString("escort"));
				tripDetailsDtoObj.setTimeElapsed(rs.getLong("timeElapsed"));
				tripDetailsDtoObj.setDistanceCovered((rs.getDouble("distanceCovered")));
				tripDetailsDtoObj.setDownloadStatus(rs.getString("downloadStatus"));
				tripDetailsDtoObj.setVehicleNo(rs.getString("regNo"));
				
				tripDetailsDtoObj.setDistance(rs.getString("distance"));
				tripDetailsDtoObj.setTravelTime(rs.getString("travelTime"));
			//	tripDetailsDtoObj.setPreviousTripId(rs.getString("preTripId"));
		//		tripDetailsDtoObj.setDistanceFromPrevioustrip(rs.getFloat("distanceFromPreTrip"));
				// tripSheetChild = null;
				tripSheetChild = new ArrayList<TripDetailsChildDto>();
		//		if (status.equals("modified") || status.equals("addmod")) {
		//			rs1 = st.executeQuery("select tdc.empid as employeeId,e.displayname as EmployeeName,a.area,p.place,l.id as landmarkId,l.landmark, tdc.status from trip_details_modified tdc,employee e,area a,place p,landmark l where e.id=tdc.empid and tdc.tripId="
			//				+ rs.getString(1)
			//				+ " and tdc.status='modified' and tdc.landmarkId=l.id and l.place=p.id and p.area=a.id order by tdc.routedOrder");
			//	} else {
					rs1 = st.executeQuery("select emp.Gender, tdc.employeeId,e.displayname as EmployeeName,a.area,p.place,l.id as landmarkId,l.landmark,tdc.time,tdc.dist,vts.showStatus, ifnull(vts.curStatus,'INIT') curStatus,tdc.dist,tdc.time from employee e,area a,place p,landmark l,trip_details_child tdc join employee emp on tdc.employeeId=emp.id left outer join vendor_trip_sheet vts on tdc.tripId=vts.tripId and tdc.employeeId=vts.employeeId where e.id=tdc.employeeId and tdc.tripId="+tripDetailsDtoObj.getId()+"  and tdc.landmarkId=l.id and l.place=p.id and p.area=a.id order by tdc.routedOrder");
				//}
				while (rs1.next()) {
					tripDetailsChildDtoObj = new TripDetailsChildDto();
					tripDetailsChildDtoObj.setGender(rs1.getString("Gender"));
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
					tripDetailsChildDtoObj.setGetInStatus(rs1.getString("curStatus"));
					tripDetailsChildDtoObj.setShowStatus(rs1.getString("showStatus"));
					try {
						tripDetailsChildDtoObj
								.setDistance(rs1.getFloat("dist"));
						tripDetailsChildDtoObj.setTime(rs1.getString("time"));
					} catch (Exception e) {

					}
					tripSheetChild.add(tripDetailsChildDtoObj);
					
				}
				tripDetailsDtoObj.setTripDetailsChildDtoList(tripSheetChild);
			}

			pst=con.prepareStatement("select * from vendor_trip_sheet_parent where tripId="+tripDetailsDtoObj.getId());
			rs=pst.executeQuery();
			if(rs.next())
			{
			tripDetailsDtoObj.setStatus(rs.getString("status"));	
			}
			else
			{
				tripDetailsDtoObj.setStatus("new");	
			}
			return tripDetailsDtoObj;
		} catch (Exception e) {
			System.out.println("Error" + e);
			return null;
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);
		}

	}



	public ArrayList<TripDetailsDto> getTrips(String imei, String date) {

		DbConnect ob = null;
		Connection con = null;
		ob = DbConnect.getInstance();
		con = ob.connectDB();		
		PreparedStatement pst = null;
		ResultSet rs = null;
	  ArrayList<TripDetailsDto>	dtos=new ArrayList<TripDetailsDto>(); 
		try {
			String tripId= getLoggedTripIdFromIMEI(imei);
			 
			pst = con
					.prepareStatement("select distinct t.trip_time,t.trip_log from trip_details t left join vendor_trip_sheet_parent vtp on t.id=vtp.tripId,driver_logins d where t.trip_date=? and  d.imei_number=? and d.status='logged' and d.vehicleId=t.vehicle and (vtp.status is null or vtp.status!='stopped') and t.id="+tripId+"  ");
		
			pst.setString(1, date);
			pst.setString(2, imei);
			rs=pst.executeQuery();
			TripDetailsDto dto=null;
			while(rs.next())
			{
				dto=new TripDetailsDto();
				dto.setTrip_time( OtherFunctions. changeTimeFormat(rs.getString("trip_time"),  "HH:mm", "hh:mm-a"));
				
				dto.setTrip_log(rs.getString("trip_log"));
				//dto.setTrip_time(dto.getTrip_time()+" " + dto.getTrip_log());
				dtos.add(dto);
				
		 
			}
			
			
			}catch(Exception e)
			{
				System.out.println("Exception in mobile gettrips");
			}
		finally
		{
		DbConnect.closeResultSet(rs);
		DbConnect.closeStatement(pst);
		DbConnect.closeConnection(con);
			
		}
		return dtos;
	}
	
	public TripDetailsDto getVehicleTripSheet(String tripId ) {

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
 
		 
			String querytemp="SELECT t.id,t.trip_code,t.trip_date,t.trip_time,t.trip_log,t.routeid,t.trip_code,v.regNo,t.security_status,t.status,vt.type,t.distance,t.travelTime,t.preTripId,t.distanceFromPreTrip, ifnull(vtp.status,'initial') status, ifnull(vtp.escort,'NO') escort, vtp.distanceCovered, vtp.timeElapsed, vtp.downloadStatus, t.siteId FROM trip_details t left join vendor_trip_sheet_parent vtp on t.id=vtp.tripId ,vehicles v, vehicle_type vt where  vt.id=t.vehicle_type  and v.id=t.vehicle and t.id=?";
			//String originalQuery="SELECT t.id,t.trip_code,t.trip_date,t.trip_time,t.trip_log,t.routeid,t.trip_code,v.regNo,t.security_status,t.status,vt.type,t.distance,t.travelTime,t.preTripId,t.distanceFromPreTrip, ifnull(vtp.status,'initial') status, ifnull(vtp.escort,'NO') escort, vtp.distanceCovered, vtp.timeElapsed, vtp.downloadStatus, t.siteId FROM trip_details t left join vendor_trip_sheet_parent vtp on t.id=vtp.tripId ,vehicles v,driver_logins dl,vehicle_type vt where dl.vehicleId=t.vehicle   and dl.status='logged' and vt.id=t.vehicle_type  and v.id=t.vehicle and t.id=?";
			System.out.println(querytemp+" ");
			pst = con
					.prepareStatement(querytemp);
			pst.setString(1, tripId);
			 
			rs = pst.executeQuery();
			if (rs.next()) {
				tripDetailsDtoObj = new TripDetailsDto();
				tripDetailsDtoObj.setId(rs.getString("id"));
				tripDetailsDtoObj.setSiteId(rs.getString("siteId")); 
				tripDetailsDtoObj.setTrip_code(rs.getString("trip_code"));
				tripDetailsDtoObj.setTrip_date(rs.getDate("trip_date")
						.toString());
				tripDetailsDtoObj.setTrip_time(rs.getString("trip_time"));
				tripDetailsDtoObj.setTrip_log(rs.getString("trip_log"));
				tripDetailsDtoObj.setRouteId(rs.getString("routeid"));
				tripDetailsDtoObj.setVehicle_type(rs.getString("type"));
				tripDetailsDtoObj
						.setIsSecurity(rs.getString("security_status"));
				tripDetailsDtoObj.setStatus(rs.getString("status"));
				tripDetailsDtoObj.setEscort(rs.getString("escort"));
				tripDetailsDtoObj.setTimeElapsed(rs.getLong("timeElapsed"));
				tripDetailsDtoObj.setDistanceCovered((rs.getDouble("distanceCovered")));
				tripDetailsDtoObj.setDownloadStatus(rs.getString("downloadStatus"));
				tripDetailsDtoObj.setVehicleNo(rs.getString("regNo"));
				tripDetailsDtoObj.setDistance(rs.getString("distance"));
				tripDetailsDtoObj.setTravelTime(rs.getString("travelTime"));
			//	tripDetailsDtoObj.setPreviousTripId(rs.getString("preTripId"));
		//		tripDetailsDtoObj.setDistanceFromPrevioustrip(rs.getFloat("distanceFromPreTrip"));
				// tripSheetChild = null;
				tripSheetChild = new ArrayList<TripDetailsChildDto>();
		//		if (status.equals("modified") || status.equals("addmod")) {
		//			rs1 = st.executeQuery("select tdc.empid as employeeId,e.displayname as EmployeeName,a.area,p.place,l.id as landmarkId,l.landmark, tdc.status from trip_details_modified tdc,employee e,area a,place p,landmark l where e.id=tdc.empid and tdc.tripId="
			//				+ rs.getString(1)
			//				+ " and tdc.status='modified' and tdc.landmarkId=l.id and l.place=p.id and p.area=a.id order by tdc.routedOrder");
			//	} else {
					rs1 = st.executeQuery("select e.Gender, tdc.employeeId,e.displayname as EmployeeName,a.area,p.place,l.id as landmarkId,l.landmark,tdc.time,tdc.dist,vts.showStatus, ifnull(vts.curStatus,'INIT') curStatus,vts.keypin,e.contactnumber1,e.personnelno,e.loginid from employee e,area a,place p,landmark l,trip_details_child tdc  left outer join vendor_trip_sheet vts on tdc.tripId=vts.tripId and tdc.employeeId=vts.employeeId where e.id=tdc.employeeId and tdc.tripId="+tripDetailsDtoObj.getId()+"  and tdc.landmarkId=l.id and l.place=p.id and p.area=a.id order by tdc.routedOrder");
				//}
				while (rs1.next()) {
					tripDetailsChildDtoObj = new TripDetailsChildDto();
					tripDetailsChildDtoObj.setGender(rs1.getString("Gender"));
					tripDetailsChildDtoObj.setTripId("" + rs.getInt(1));
					tripDetailsChildDtoObj.setEmployeeId(rs1
							.getString("employeeId"));
					tripDetailsChildDtoObj.setPersonnelNo(rs1.getString("personnelno"));
					tripDetailsChildDtoObj.setEmployeeName(rs1
							.getString("EmployeeName"));
					tripDetailsChildDtoObj.setArea(rs1.getString("area"));
					tripDetailsChildDtoObj.setPlace(rs1.getString("place"));
					tripDetailsChildDtoObj.setLandmarkId(rs1
							.getString("landmarkId"));
					tripDetailsChildDtoObj.setLandmark(rs1
							.getString("landmark"));
					tripDetailsChildDtoObj.setGetInStatus(rs1.getString("curStatus"));
					tripDetailsChildDtoObj.setShowStatus(rs1.getString("showStatus"));
					tripDetailsChildDtoObj.setTime(rs1.getString("time"));
					tripDetailsChildDtoObj.setContactNumber(rs1.getString("contactnumber1"));					
					if(SettingsConstant.empAuthinticationForComet.equalsIgnoreCase("pin"))
					{
					tripDetailsChildDtoObj.setKeyPin(rs1.getString("keypin"));
					}
					else if(SettingsConstant.empAuthinticationForComet.equalsIgnoreCase("loginid"))
					{
						int loginidlength=rs1.getString("loginid").length();
						tripDetailsChildDtoObj.setKeyPin(rs1.getString("loginid").substring(loginidlength-4));
					}
					else
					{
					tripDetailsChildDtoObj.setKeyPin(rs1.getString("personnelno"));
					}	
					tripSheetChild.add(tripDetailsChildDtoObj);
					
				}
				tripDetailsDtoObj.setTripDetailsChildDtoList(tripSheetChild);
			}

			pst=con.prepareStatement("select * from vendor_trip_sheet_parent where tripId="+tripDetailsDtoObj.getId());
			rs=pst.executeQuery();
			if(rs.next())
			{
			tripDetailsDtoObj.setStatus(rs.getString("status"));	
			}
			else
			{
				tripDetailsDtoObj.setStatus("new");	
			}
			return tripDetailsDtoObj;
		} catch (Exception e) {
			System.out.println("Error in MobileTripSheetDao getVehicleTripSheet("+tripId+")" + e);
			return null;
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);
		}

	}

	public ArrayList<String> getDriverPasswords(String tripId, int priorDays, int postDays) {
		// TODO Auto-generated method stub
		DbConnect ob = null;
		Connection con = null;
		ob = DbConnect.getInstance();
		con = ob.connectDB();
	 	ArrayList<String> passwords = new ArrayList<String>();
		PreparedStatement pst = null;
		 
		ResultSet rs = null;
		 
		try {
			pst = con.prepareStatement("select driverPswd from trip_details where trip_date   between date_add(trip_date,  interval ? day ) and date_add(trip_date,  interval ? day )  and driverPswd is not null ");
			System.out.println(" select driverPswd from trip_details where trip_date   between date_add(trip_date,  interval -" + priorDays +" day ) and date_add(trip_date,  interval " + postDays + " day )  and driverPswd is not null");
			pst.setInt(1, priorDays); 
			  
			pst.setInt(2, postDays); 
			  
			 
			rs=pst.executeQuery();
			 
			while(rs.next()) {
				String driverPswd=rs.getString("driverPswd");
//				System.out.println("~password :"+driverPswd);
				if(driverPswd!=null&&driverPswd.trim().equals("")==false) {
					passwords.add(driverPswd);
				}
			}
				
			}catch(Exception e ) {
				System.out.println("Error in getPasswords :"+e);
			} finally {
				DbConnect.closeResultSet(rs);
				DbConnect.closeStatement(pst);
				DbConnect.closeConnection(con);
			}
		
		return passwords;
	}
	
	public String getLoggedTripIdFromIMEI(String imei) {
		
		DbConnect ob = null;
		Connection con = null;
		ob = DbConnect.getInstance();
		con = ob.connectDB();
	 	 String tripId="";
		PreparedStatement pst = null;
		 
		ResultSet rs = null;
		 
		try {
			pst = con.prepareStatement(" select tripid from driver_logins where imei_number=? and status='logged' ");
			pst.setString(1, imei); 
			  
			rs=pst.executeQuery();
			 
			if(rs.next()) {
				 tripId=rs.getString("tripid");
			}
				
			}catch(Exception e ) {
				System.out.println("Error in getPasswords :"+e);
			} finally {
				DbConnect.closeResultSet(rs);
				DbConnect.closeStatement(pst);
				DbConnect.closeConnection(con);
			}
		return tripId;
		
	}

	public ArrayList<String> getEscortPasswords(String tripId, int priorDays, int postDays) {
		// TODO Auto-generated method stub
		DbConnect ob = null;
		Connection con = null;
		ob = DbConnect.getInstance();
		con = ob.connectDB();
	 	ArrayList<String> passwords = new ArrayList<String>();
		PreparedStatement pst = null;
		 
		ResultSet rs = null;
		 
		try {
			pst = con.prepareStatement("select escortPswd from trip_details where trip_date   between  (select date_add(trip_date,  interval -? day ) from trip_details where id=?) and (select date_add(trip_date,  interval ? day ) from trip_details where id=?)");
			pst.setInt(1, priorDays); 
			pst.setString(2, tripId); 
			pst.setInt(3, postDays); 
			pst.setString(4, tripId); 
			 
			rs=pst.executeQuery();
			 
			while(rs.next()) {
				String escortPswd=rs.getString("escortPswd");
				System.out.println("~password :"+escortPswd);
				if(escortPswd!=null&&escortPswd.trim().equals("")==false) {
					passwords.add(escortPswd);
				}
			}
				
			}catch(Exception e ) {
				System.out.println("Error in getPasswords :"+e);			
	} finally {
		DbConnect.closeResultSet(rs);
		DbConnect.closeStatement(pst);
		DbConnect.closeConnection(con);
	}
		return passwords;
	
	}

}
