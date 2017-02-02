/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.agiledge.atom.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;


import com.agiledge.atom.commons.OtherFunctions;
import com.agiledge.atom.constants.SettingsConstant;
import com.agiledge.atom.dbConnect.DbConnect;
import com.agiledge.atom.dto.RoutingDto;
import com.agiledge.atom.dto.TripDetailsChildDto;
import com.agiledge.atom.dto.TripDetailsDto;
import com.agiledge.atom.trip.dto.AplDistanceDto;

/**
 * 
 * @author muhammad
 */
public class RoutingDao {

	static final int LADY_SECURTY = 1;
	StringBuilder selectedEmpSubscribedIDString = null;
	StringBuilder selectedEmployeeIDString = null;
	DbConnect ob = null;
	public Connection con = null;
	ArrayList<Integer> selectedRoutesLandmarks = new ArrayList<Integer>();
	String uniqueId = "";
	String uniqueCode = "";
	int uniqueNo = 1;
	public int siteLandmarkId;
	private boolean distanceInDb = true;
	private ArrayList<AplDistanceDto> aplDistanceList;
	private ArrayList<AplDistanceDto> landmarkDistanceList;
	
	//public boolean testCase;
//	private ArrayList<String> errorMessageList = new ArrayList<String>();

	public ArrayList<AplDistanceDto> getAplDistanceList() {
		return aplDistanceList;
	}

	public void setAplDistanceList(ArrayList<AplDistanceDto> aplDistanceList) {
		this.aplDistanceList = aplDistanceList;
	}

	public HashMap<String, RoutingDto> setEmployeesForTravel(
			RoutingDto routingDto, int siteid, String routingType)
			throws SQLException {
		// uniqueNo = 1;
		getSitelandmark(siteid);
		ResultSet rs = null;
		/*
		 * uniqueId = ""; uniqueCode = ""; //
		 * System.out.println("SET to Travel Starts"
		 * +routingDto.getDate()+routingDto
		 * .getTravelMode()+routingDto.getTime()); uniqueCode =
		 * OtherFunctions.getOrdinaryDateFormat(routingDto.getDate());
		 * uniqueCode = uniqueCode.replaceAll("/", ""); if
		 * (routingDto.getTravelMode().equals("IN")) uniqueCode += "P"; if
		 * (routingDto.getTravelMode().equals("OUT")) uniqueCode += "D";
		 * Statement st = con.createStatement(); ResultSet rs = st
		 * .executeQuery("select count(*) from trip_details where trip_date='" +
		 * routingDto.getDate() + "' and trip_log='" +
		 * routingDto.getTravelMode() + "'"); if (rs.next()) { uniqueNo =
		 * rs.getInt(1) + 1; }
		 */
		CallableStatement cst = null;
		rs = null;
		// st = null;
		HashMap<String, RoutingDto> routingDtoList = null;
		RoutingDto routingDtoToRoute = null;
		System.out.println(routingDto.getTravelMode()+"-"+routingDto.getTime()+"Call get emp to travel"+ new Date());
		try{
		cst = con.prepareCall("{CALL getEmployeeToTravel(?,?,?,?,?,?)}");
		// System.out.println("{CALL getEmployeeToTravel("+siteid+","+routingDto.getDate()+","+routingDto.getTime()+","+routingDto.getTravelMode()+","+routingType+",?)}");
		cst.setInt(1, siteid);
		cst.setString(2, routingDto.getDate());
		cst.setString(3, routingDto.getTime());
		cst.setString(4, routingDto.getTravelMode());
		cst.setString(5, routingType);
		cst.registerOutParameter(6, Types.INTEGER);
		cst.execute();
		int returnInt = cst.getInt(6);
		// System.out.println("returnInttn" + returnInt);
		if (returnInt == 0) {
			rs = cst.executeQuery();
			if (rs.next()) {
				routingDtoList = new HashMap<String, RoutingDto>();
				do {
					routingDtoToRoute = new RoutingDto();
					routingDtoToRoute.setDate(routingDto.getDate());
					routingDtoToRoute.setTime(routingDto.getTime());
					routingDtoToRoute.setTravelMode(routingDto.getTravelMode());
					routingDtoToRoute.setEmployeeId(rs.getString("employeeId"));
					routingDtoToRoute.setSubscriptionId(rs
							.getString("subscriptionId"));
					routingDtoToRoute.setScheduleId(rs.getString("scheduleId"));
					routingDtoToRoute.setEmployeeLandmark(rs
							.getString("landmarkId"));
					routingDtoToRoute.setProject(rs.getString("project"));
					routingDtoToRoute.setEmployeegender(rs.getString("gender"));
					routingDtoToRoute.setTransportType(rs.getString("bookType"));
					routingDtoList.put(routingDtoToRoute.getEmployeeId(),
							routingDtoToRoute);
				} while (rs.next());
			}
		}
		}catch(Exception e)
		{
			
		}
		finally
		{
		DbConnect.closeStatement(cst);
		DbConnect.closeResultSet(rs);
		}
		System.out.println("Call get emp to travel Finieshed"+new Date());
		// System.out.println("SET to Travel ENDS");
		//System.out.println(" employee s size : " + routingDtoList.size());
		return routingDtoList;
	}

	public int[] getBestRouteId(StringBuilder selectedEmpSubscribedIDString,
			StringBuilder selectedEmployeeIDString, int siteid,
			RoutingDto routingDto) throws SQLException {
		// System.out.println("get Best Route Starts");
		PreparedStatement pst = null;
		ResultSet rs = null;
		int routeId = 0;
		int employeeCount = 0;
		int[] returnArray = new int[2];
		if (!(selectedEmpSubscribedIDString.length() < 1)) {
			this.selectedEmpSubscribedIDString = selectedEmpSubscribedIDString;
			this.selectedEmployeeIDString = selectedEmployeeIDString;
			// System.out.println("EMP STRING" + selectedEmpSubscribedIDString);
			String query = "";
			try {
				for (int i = 1; i <= 3; i++) {
			/*	 	query = "select rc.routeid,count(es.subscriptionID) as c "
							+ ",rt.type,rt.typeDesc,rt.priority from route r,routeTypes rt,"
							+ "employee_subscription es,routeChild rc where r.siteId=? "
							+ " and r.id=rc.routeId and r.type=rt.type and rt.priority<="
							+ i
							+ " and (es.subscriptionStatus='subscribed' or es.subscriptionStatus='pending') and rc.landmarkId=es.landmark and es.subscriptionID in("
							+ selectedEmpSubscribedIDString
							+ ") and "
							+ "es.landmark in(SELECT landmarkId from routeChild) group by rc.routeid,rt.type,rt.typeDesc,rt.priority order by c desc limit 1";
				 	System.out.println("ii=" + i + "            Query " +query);
			*/	 query = "select routeId, count(subscriptionID) c, rt.type,rt.typeDesc,rt.priority    from (  select distinct rc.routeId,  es.subscriptionID, r.type  from  route r, employee_subscription es, routeChild rc where r.id=rc.routeId and es.landmark=rc.landmarkid   and es.subscriptionId  in("+ selectedEmpSubscribedIDString +" )  and (es.subscriptionStatus='subscribed' or es.subscriptionStatus='pending') and r.siteId=?   " +
							 ") s, routeTypes rt  where s.type=rt.type and rt.priority<="+i + " group by routeId, type order by c desc limit 1;";
				 
		//			 System.out.println("ii=" + i + "            Query " +query);
							
					pst = con.prepareStatement(query);
					pst.setInt(1, siteid);
					rs = pst.executeQuery();
					if (rs.next()) {
						routeId = rs.getInt("routeid");
						employeeCount = rs.getInt("c");
					}
					if (employeeCount <= 3) {
						if (!checkCombainedRouteStatus(routingDto, siteid)) {
							break;
							/*
							 * query =
							 * "select top 1 rc.routeid,count(es.subscriptionID) as c from route r,"
							 * +
							 * "employee_subscription es,routeChild rc where r.siteId=? "
							 * +
							 * "and r.id=rc.routeId and (es.subscriptionStatus='subscribed' or es.subscriptionStatus='pending') and rc.landmarkId=es.landmark and es.subscriptionID in("
							 * + selectedEmpSubscribedIDString + ") and " +
							 * "es.landmark in(SELECT landmarkId from routeChild) group by rc.routeid order by c desc"
							 * ; // System.out.println("Query"+query); pst =
							 * con.prepareStatement(query); pst.setInt(1,
							 * siteid); rs = pst.executeQuery(); if (rs.next())
							 * { routeId = rs.getInt("routeid"); employeeCount =
							 * rs.getInt("c"); }
							 */
						}
					} else {
						break;
					}
				}
				returnArray[0] = routeId;
				returnArray[1] = employeeCount;
			} catch (Exception e) {
				System.out.println("ERROR" + e);
			}
			finally
			{
			DbConnect.closeStatement(pst);
			DbConnect.closeResultSet(rs);
			}
		}

		// System.out.println("GEt BEst Route ENDS");
		return returnArray;
	}

	public int getRouteId(String landmark) throws SQLException {
		PreparedStatement pst = null;
		ResultSet rs = null;
		int returnVal = 0;
		try{
		pst = con
				.prepareStatement("select  id  from route where id in (select routeId from routeChild where landmarkId="
						+ landmark + ") limit 1");
		rs = pst.executeQuery();
		if (rs.next()) {
			returnVal = rs.getInt(1);
		}
		}finally
		{
			DbConnect.closeResultSet(rs);	
		DbConnect.closeStatement(pst);
		
		}
		
		return returnVal;
	}

	private boolean checkCombainedRouteStatus(RoutingDto routingDto, int siteId)
			throws SQLException {
		PreparedStatement pst = null;
		ResultSet rs = null;
		boolean retFlag = false;
		
		String query = "select ss.combainroute,combainedRouteOnweekoff from logtime l,site s,site_shift ss where l.logtime=? and l.logtype=? AND s.id=? and ss.siteId=s.id and ss.shiftId=l.id ";
		// System.out.println("In combained query"+query);
		try{
		pst = con.prepareStatement(query);
		pst.setString(1, routingDto.getTime());
		pst.setString(2, routingDto.getTravelMode());
		pst.setInt(3, siteId);
		rs = pst.executeQuery();
		if (rs.next()) {
			if (rs.getString(1).equals("yes")) {
				retFlag = true;
			} else if (rs.getInt(2) == 1
					&& (OtherFunctions.getDay(routingDto.getDate()) == 1 || OtherFunctions
							.getDay(routingDto.getDate()) == 7)) {
				retFlag = true;
			}
		}
		}finally{
		DbConnect.closeResultSet(rs);
		DbConnect.closeStatement(pst);
		}
		// /System.out.println(retFlag+"In combained query"+query);
		return retFlag;
	}

	public ArrayList<Integer> getLandmarksOfTheSelectedRoute(int routeId)
			throws SQLException {
		// System.out.println("get Landmarks starts");
		PreparedStatement pst = null;
		ResultSet rs = null;
		selectedRoutesLandmarks.clear();
		try{
		pst = con
				.prepareStatement("SELECT landmarkId FROM routeChild rc WHERE rc.routeId=? AND rc.landmarkId "
						+ "IN(SELECT es.landmark FROM employee_subscription es "
						+ "WHERE (es.subscriptionStatus='subscribed' or es.subscriptionStatus='pending') and es.subscriptionID IN("
						+ selectedEmpSubscribedIDString
						+ ")) ORDER BY rc.position");
		pst.setInt(1, routeId);
		rs = pst.executeQuery();
		while (rs.next()) {
			selectedRoutesLandmarks.add(rs.getInt("landmarkId"));
		}
		}
		finally
		{
			DbConnect.closeResultSet(rs);			
		DbConnect.closeStatement(pst);		
		}
		// System.out.println("get Landmarks ends");
		return selectedRoutesLandmarks;
	}

	public int checkSecurity(RoutingDto routingDto, int siteid)
			throws SQLException {
		// System.out.println("security check starts");
		PreparedStatement pst = null;
		ResultSet rs = null;
		int retVal=0;
		try{
		String query = "SELECT * FROM site WHERE (night_shift_start<'"
				+ routingDto.getTime() + "' OR night_shift_end>'"
				+ routingDto.getTime() + "')AND lady_security=? and id=?";
		// System.out.println("query"+query);
		pst = con.prepareStatement(query);
		pst.setInt(1, LADY_SECURTY);
		pst.setInt(2, siteid);
		rs = pst.executeQuery();
		if (rs.next()) {
			retVal= 1;
		}
		
			}
			finally
			{
				DbConnect.closeResultSet(rs);			
			DbConnect.closeStatement(pst);		
			}
		// System.out.println("check securuty ends");
		return retVal;

	}

	public int checkFemaleInLast(int lastLandmark) throws SQLException {
		// System.out.println("check female in last starts");
		PreparedStatement pst = null;
		ResultSet rs = null;
		int retVal=0;
		try{
		pst = con
				.prepareStatement("select count(e.id) as femaleCount from employee e,employee_subscription es  where es.empID=e.id and es.subscriptionID in("
						+ selectedEmpSubscribedIDString
						+ ") and e.gender='F' and es.landmark=?");
		pst.setInt(1, lastLandmark);
		rs = pst.executeQuery();
		if (rs.next()) {
			retVal= rs.getInt("femaleCount");
		}
		}finally
		{
			DbConnect.closeResultSet(rs);			
			DbConnect.closeStatement(pst);		
		}
		// System.out.println("check female in last ends");
		return retVal;

	}

	/*
	 * public VehicleTypeDto getVehicleTypeList() throws SQLException {
	 * VehicleTypeDto vehicleTypeDto=new VehicleTypeDto(); PreparedStatement pst
	 * = null; ResultSet rs = null; pst = con.prepareStatement(""); rs =
	 * pst.executeQuery(); if (rs.next()) { return rs.getInt("femaleCount"); }
	 * return 0; }
	 */

	public int storeInTripMaster(TripDetailsDto tripDetailsDtoObj)
			throws SQLException {
		// System.out.println("store in trip master staarts");
		/*
		 * if (uniqueNo < 10) { uniqueId = uniqueCode + "00" + uniqueNo; } else
		 * if (uniqueNo < 100) { uniqueId = uniqueCode + "0" + uniqueNo; } else
		 * { uniqueId = uniqueCode + uniqueNo; }
		 */
		uniqueId = "" + uniqueNo;
		uniqueNo++;
		
		PreparedStatement pst = null;
		ResultSet rs = null;
		int retVal = 0;
		try{
		pst = con
				.prepareStatement(
						"insert into trip_details(trip_code,siteId,trip_date,trip_time,trip_log,routeId,vehicle_type,status,security_status,routingType,distance,travelTime) values(?,?,?,?,?,?,?,?,?,?,?,?)",
						Statement.RETURN_GENERATED_KEYS);

		pst.setString(1, uniqueId);
		pst.setString(2, tripDetailsDtoObj.getSiteId());
		pst.setString(3, tripDetailsDtoObj.getTrip_date());
		pst.setString(4, tripDetailsDtoObj.getTrip_time());
		pst.setString(5, tripDetailsDtoObj.getTrip_log());
		pst.setString(6, tripDetailsDtoObj.getRouteId());
		pst.setString(7, tripDetailsDtoObj.getVehicle_type());
		pst.setString(8, "routed");
		pst.setString(9, tripDetailsDtoObj.getIsSecurity());
		pst.setString(10, tripDetailsDtoObj.getRoutingType());
		pst.setString(11, tripDetailsDtoObj.getDistance());
		pst.setString(12, tripDetailsDtoObj.getTravelTime());
		pst.executeUpdate();
		rs = pst.getGeneratedKeys();
		if (rs.next()) {			
			retVal = rs.getInt(1);
		}
		}finally
		{
			DbConnect.closeResultSet(rs);			
			DbConnect.closeStatement(pst);
		}

		return retVal;
	}

	public void storeInTripChild(ArrayList<TripDetailsChildDto> tripDetailsChildDtoList) throws SQLException
			 {
		// System.out.println("store in trip child staarts");
		PreparedStatement pst = null;
		pst = con.prepareStatement("insert into trip_details_child(tripId,employeeId,routedOrder,landmarkId,scheduleId,time,dist,transportType) values(?,?,?,?,?,?,?,?)");
		try{
		for(TripDetailsChildDto tripDetailsChildDtoObj: tripDetailsChildDtoList)
		{
			try{		
		pst.setString(1, tripDetailsChildDtoObj.getTripId());
		pst.setString(2, tripDetailsChildDtoObj.getEmployeeId());
		pst.setString(3, tripDetailsChildDtoObj.getRoutedOrder());
		pst.setString(4, tripDetailsChildDtoObj.getLandmarkId());
		pst.setString(5, tripDetailsChildDtoObj.getScheduleId());
		pst.setString(6, tripDetailsChildDtoObj.getTime());
		pst.setFloat(7, tripDetailsChildDtoObj.getDistance());
		pst.setString(8, tripDetailsChildDtoObj.getTransportType());
		System.out.println(tripDetailsChildDtoObj.getTripId()+" "+tripDetailsChildDtoObj.getEmployeeId()+" "+tripDetailsChildDtoObj.getRoutedOrder()+" "+tripDetailsChildDtoObj.getLandmarkId()+" "+tripDetailsChildDtoObj.getScheduleId()+" "+tripDetailsChildDtoObj.getTime()+" "+tripDetailsChildDtoObj.getDistance()+" "+tripDetailsChildDtoObj.getTransportType());
		pst.executeUpdate();
			}
		catch(SQLException e)
		{
		System.out.println("Error in strong child"+e);	
		}
		}	
		}
		finally{			
			DbConnect.closeStatement(pst);
			}
		// System.out.println("store in trip child end");
	}

	public void closeConnection() {
		// System.out.println("close staarts");
		DbConnect.closeConnection(con);
	}

	public int checkTripExist(RoutingDto routingDto, int siteid) {
		DbConnect ob1 = null;
		Connection con1 = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		PreparedStatement pst1 = null;
		int retunVal = 0;
		// int sec = 0;
		try {
			ob1 = DbConnect.getInstance();
			con1 = ob1.connectDB();
 
			if (routingDto.getTravelMode().equalsIgnoreCase("All")) {
 				// sec = 1;
				String query = "select id from trip_details where siteId=? and trip_date=? and status!=? and status!=? and status!=?";
				pst1 = con1.prepareStatement(query);
				pst1.setInt(1, siteid);
				pst1.setString(2, routingDto.getDate());
				pst1.setString(3, "saved");
				pst1.setString(4, "addsave");
				pst1.setString(5, "saveedit");
 
			} else if (routingDto.getTime().equalsIgnoreCase("All")) {
  				// sec = 2;
				String query = "select id from trip_details where siteId=? and trip_date=? and trip_log=? and status!=? and status!=? and status!=?";
				pst1 = con1.prepareStatement(query);
				pst1.setInt(1, siteid);
				pst1.setString(2, routingDto.getDate());
				pst1.setString(3, routingDto.getTravelMode());
				pst1.setString(4, "saved");
				pst1.setString(5, "addsave");
				pst1.setString(6, "saveedit");
			} else {
				// sec = 3;
				String query = "select id from trip_details where siteId=? and trip_date=? and trip_log=? and trip_time=? and status!=? and status!=? and status!=?";
				pst1 = con1.prepareStatement(query);
				pst1.setInt(1, siteid);
				pst1.setString(2, routingDto.getDate());
				pst1.setString(3, routingDto.getTravelMode());
				pst1.setString(4, routingDto.getTime());
				pst1.setString(5, "saved");
				pst1.setString(6, "addsave");
				pst1.setString(7, "saveedit");
			}
			rs = pst1.executeQuery();
			if (rs.next()) {
				retunVal = 1;
			} else {
				retunVal = 0;
			}

		} catch (Exception e) {
			System.out.println("Errro" + e);

		} finally {
			DbConnect.closeStatement(pst1);
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con1);

		}
		return retunVal;
	}

	public int overWriteTrips(RoutingDto routingDto, int siteid) {
		DbConnect ob1 = null;
		Connection con1 = null;
		PreparedStatement pst = null;
		PreparedStatement pst1 = null;
		int retunVal = 0;
		try {
			ob1 = DbConnect.getInstance();
			con1 = ob1.connectDB();

 
			
			
			
			if (routingDto.getTravelMode().equalsIgnoreCase("All")) {
				
 				String query = "delete from trip_details_child where tripId in (select id from trip_details where siteId=? and trip_date=? and status!=? and status!=? and status!=?)";
 				
				pst = con1.prepareStatement(query);
				pst.setInt(1, siteid);
				pst.setString(2, routingDto.getDate());
				pst.setString(3, "saved");
				pst.setString(4, "addsave");
				pst.setString(5, "saveedit");

				String query1 = "delete from trip_details where siteId=? and trip_date=? and status!=? and status!=? and status!=?";
				pst1 = con1.prepareStatement(query1);
				pst1.setInt(1, siteid);
				pst1.setString(2, routingDto.getDate());
				pst1.setString(3, "saved");
				pst1.setString(4, "addsave");
				pst1.setString(5, "saveedit");
 
			} else if (routingDto.getTime().equalsIgnoreCase("All")) {
 
				String query = "delete from trip_details_child where tripId in (select id from trip_details where siteId=? and trip_date=? and trip_log=? and status!=? and status!=? and status!=?)";
				pst = con1.prepareStatement(query);
				pst.setInt(1, siteid);
				pst.setString(2, routingDto.getDate());
				pst.setString(3, routingDto.getTravelMode());
				pst.setString(4, "saved");
				pst.setString(5, "addsave");
				pst.setString(6, "saveedit");

				String query1 = "delete from trip_details where siteId=? and trip_date=? and trip_log=? and status!=? and status!=? and status!=?";
				pst1 = con1.prepareStatement(query1);
				pst1.setInt(1, siteid);
				pst1.setString(2, routingDto.getDate());
				pst1.setString(3, routingDto.getTravelMode());
				pst1.setString(4, "saved");
				pst1.setString(5, "addsave");
				pst1.setString(6, "saveedit");
			} else {
				String query = "delete from trip_details_child where tripId in (select id from trip_details where siteId=? and trip_date=? and trip_log=? and trip_time=? and status!=? and status!=? and status!=?)";
				pst = con1.prepareStatement(query);
				pst.setInt(1, siteid);
				pst.setString(2, routingDto.getDate());
				pst.setString(3, routingDto.getTravelMode());
				pst.setString(4, routingDto.getTime());
				pst.setString(5, "saved");
				pst.setString(6, "addsave");
				pst.setString(7, "saveedit");
				String query1 = "delete from trip_details where siteId=? and trip_date=? and trip_log=? and trip_time=? and status!=? and status!=? and status!=?";
				pst1 = con1.prepareStatement(query1);
				pst1.setInt(1, siteid);
				pst1.setString(2, routingDto.getDate());
				pst1.setString(3, routingDto.getTravelMode());
				pst1.setString(4, routingDto.getTime());
				pst1.setString(5, "saved");
				pst1.setString(6, "addsave");
				pst1.setString(7, "saveedit");

			}
			retunVal = pst.executeUpdate();
			retunVal += pst1.executeUpdate();
			String query = "delete from trip_details_modified where tripId not in (select id from trip_details)";
			pst1 = con1.prepareStatement(query);
			pst1.executeUpdate();
			query = "delete from trip_details_parent_modified where tripId not in (select id from trip_details)";
			pst1 = con1.prepareStatement(query);
			pst1.executeUpdate();
			
			
		} catch (Exception e) {
			System.out.println("Errro" + e);

		} finally {
			DbConnect.closeStatement(pst1);
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con1);

		}
		// System.out.println("Returning value"+retunVal);
		return retunVal;

	}

	public ArrayList<RoutingDto> getAllroutingDtos(RoutingDto routingDto,
			int siteid) {
		ob = DbConnect.getInstance();
		con = ob.connectDB();
		PreparedStatement pst = null;
		ResultSet rs = null;
		ArrayList<RoutingDto> routingDtos = new ArrayList<RoutingDto>();
		RoutingDto dto = null;
		// TODO Auto-generated method stub
		// String
		// query="(select distinct table1.login_time as time,'IN' as logtype from (select login_time from employee_schedule where from_date<=? and to_date>=?  union select login_time from employee_schedule_alter where date=?) as table1 where table1.login_time not in (select trip_details.trip_time from trip_details where trip_date=? and trip_log='IN'))";
		// String
		// query1="(select distinct table2.logout_time as time,'OUT' as logtype from (select logout_time from employee_schedule where from_date<=? and to_date>=?  union select logout_time from employee_schedule_alter where date=?) as table2 where table2.logout_time not in (select trip_details.trip_time from trip_details where trip_date=? and trip_log='OUT'))";
		String query = "(select distinct table1.login_time as time,'IN' as logtype,table1.bookType from (select login_time,'shift' as  bookType from employee_schedule where from_date<=? and to_date>=? and status='a'  union select login_time,'shift' as  bookType from employee_schedule_alter where date=?  and status='a' union select startTime login_time,'adhoc' as  bookType from adhocBooking where adhocType='"+SettingsConstant.SHIFT_EXTENSTION+"' and siteId="+siteid+" and travelDate=? and pickDrop='IN') as table1 where table1.login_time not in (select trip_details.trip_time from trip_details where trip_date=?  and trip_log='IN' and siteId="
				+ siteid + ") order by table1.login_time )";

		String query1 = "(select distinct table2.logout_time as time,'OUT' as logtype,table2.bookType from (select logout_time,'shift' as  bookType from employee_schedule where from_date<=? and to_date>=? and status='a' union select logout_time,'shift' as  bookType from employee_schedule_alter where date=? and status='a' union select startTime as login_time,'adhoc' as  bookType from adhocBooking where adhocType='"+SettingsConstant.SHIFT_EXTENSTION+"' and siteId="+siteid+" and travelDate=? and pickDrop='OUT') as table2 where table2.logout_time not in (select trip_details.trip_time from trip_details where trip_date=? and trip_log='OUT' and siteId="
				+ siteid + ") order by table2.logout_time )";
		String queryLastpart=" as td where time!='' and time!='null' and time!='none' and time!='' and time!='weekly off' and time is not null " +
				" and time+logtype not in (select trip_time+trip_log from trip_details where siteid="+siteid+" and trip_date='"+routingDto.getDate()+"')" +
				"group by logtype,time order by time,logtype ";
		String queryFrist="select * from  ";

		try {	
			if ( !(routingDto.getTime().equalsIgnoreCase("all"))) {
				routingDtos.add(routingDto);				
			} else {
				if (routingDto.getTravelMode().equalsIgnoreCase("All")) {
					pst = con.prepareStatement(queryFrist+"("+query + " union " + query1+" ) "+queryLastpart);
					System.out.println(queryFrist+"("+query + " union " + query1+" ) "+queryLastpart);
					pst.setString(1, routingDto.getDate());
					pst.setString(2, routingDto.getDate());
					pst.setString(3, routingDto.getDate());
					pst.setString(4, routingDto.getDate());
					pst.setString(5, routingDto.getDate());
					pst.setString(6, routingDto.getDate());
					pst.setString(7, routingDto.getDate());
					pst.setString(8, routingDto.getDate());
					pst.setString(9, routingDto.getDate());
					pst.setString(10, routingDto.getDate());
				} else if (routingDto.getTravelMode().equals("IN")
						&& routingDto.getTime().equalsIgnoreCase("all")) {
					System.out.println(queryFrist+query+queryLastpart);
					pst = con.prepareStatement(queryFrist+query+queryLastpart);
					pst.setString(1, routingDto.getDate());
					pst.setString(2, routingDto.getDate());
					pst.setString(3, routingDto.getDate());
					pst.setString(4, routingDto.getDate());
					pst.setString(5, routingDto.getDate());
				} else if (routingDto.getTravelMode().equals("OUT")
						&& routingDto.getTime().equalsIgnoreCase("all")) {
					System.out.println(queryFrist+query1+queryLastpart);
					pst = con.prepareStatement(queryFrist+query1+queryLastpart);
					pst.setString(1, routingDto.getDate());
					pst.setString(2, routingDto.getDate());
					pst.setString(3, routingDto.getDate());
					pst.setString(4, routingDto.getDate());
					pst.setString(5, routingDto.getDate());
				}
				rs = pst.executeQuery();
				while (rs.next()) {
					dto = new RoutingDto();
					dto.setDate(routingDto.getDate());
					dto.setTravelMode(rs.getString("logtype"));
					dto.setTime(rs.getString("time"));		
					dto.setTransportType(rs.getString("bookType"));
					System.out.println("Time :"+dto.getTravelMode()+"-"+dto.getTime());					
					routingDtos.add(dto);
				}
			}
		} catch (Exception e)

		{
			System.out.println(routingDtos.size()
					+ "ERROR in route dtolist dao" + e);
		}
		finally
		{
			DbConnect.closeResultSet(rs);			
			DbConnect.closeStatement(pst);	
		}
		return routingDtos;
	}

	/*
	 * public void changeDateForNightShift(RoutingDto dto) throws SQLException {
	 * ob = DbConnect.getInstance(); con = ob.connectDB();
	 * 
	 * int dateadd=0; PreparedStatement pst = null; ResultSet rs = null; pst =
	 * con
	 * .prepareStatement("select datedif from logtime where logtime=? and logtype=?"
	 * ); pst.setString(1, dto.getTime()); pst.setString(2,
	 * dto.getTravelMode()); rs = pst.executeQuery(); if (rs.next()) {
	 * dateadd=rs.getInt(1); } if(dateadd==1) {
	 * dto.setDate(OtherFunctions.addDate(dto.getDate())); }
	 * 
	 * }
	 */

	public void updateVehicleStatus(String id) {
		// System.out.println("vehicel Id"+id);
		PreparedStatement pst = null;

		try {
			pst = con
					.prepareStatement("update vehicle_status set status='out' where vehicleId=?");
			pst.setString(1, id);
			pst.executeUpdate();
		} catch (Exception e) {
			System.out.println("error in update" + e);
		} finally {
			DbConnect.closeStatement(pst);

		}

	}

/*	public float[] getDistances(int[] landmarks, RoutingDto routingDto) {
		PreparedStatement pst = null;
		ResultSet rs = null;
		float totalDistance = (float) 0.0;
		float distance[] = new float[landmarks.length];
		aplDistanceList = new ArrayList< AplDistanceDto> (); 
		String query = "select distance from distchart where (srcId=? and destId=?) or (destId=? and srcId=?)";
		// System.out.println("query"+query);
		try {
			pst = con.prepareStatement(query);
			int srcId=0;
			int destId=0;
			for (int i = 0; i < landmarks.length; i++) {
				AplDistanceDto aplDistanceDto = new AplDistanceDto();
				// System.out.println(landmarks[i]+"   "+routingDto.getTravelMode().equals("IN"));
				if (routingDto.getTravelMode().equals("IN")) {
					if (i == 0) {
						
						srcId=siteLandmarkId;
						destId=landmarks[landmarks.length - 1];
						
						
						pst.setInt(1, srcId);
						pst.setInt(2, destId);
						pst.setInt(3, srcId);
						pst.setInt(4, destId);
					} else {
						srcId=landmarks[i - 1];
						destId=landmarks[i];
						pst.setInt(1, srcId);
						pst.setInt(2, destId);
						pst.setInt(3, srcId);
						pst.setInt(4, destId);

					}
				} else {
					if (i == landmarks.length - 1) {
						srcId=siteLandmarkId;
						destId=landmarks[i];
						pst.setInt(1, srcId);
						pst.setInt(2, destId);
						pst.setInt(3, srcId);
						pst.setInt(4, destId);
					} else {
						srcId=landmarks[i];
						destId=landmarks[i + 1];
						pst.setInt(1, srcId);
						pst.setInt(2, destId);
						pst.setInt(3, srcId);
						pst.setInt(4, destId);
					}
				}
				
				

				rs = pst.executeQuery();
				if (rs.next()) {

					distance[i] = rs.getFloat(1);

					totalDistance += distance[i];
					
					

				} else {
					distance[i] = 0;
					if(srcId==destId) {
						setDistanceInDb(false);
					}
				}
				if (i == 0 && routingDto.getTravelMode().equals("IN")) {
					distance[0] = 0;
				}
				
				aplDistanceDto.setSourceId(String.valueOf(srcId));
				aplDistanceDto.setTargetId(String.valueOf(destId));
				aplDistanceList.add(aplDistanceDto);

			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		routingDto.setDistance(totalDistance);
		return distance;
	}*/
	
	public float[] getDistances(int[] landmarks, RoutingDto routingDto) {
		setDistanceInDb(true);
		PreparedStatement pst = null;
		ResultSet rs = null;
		float totalDistance = (float) 0.0;
		float distance[] = new float[landmarks.length];
		aplDistanceList = new ArrayList< AplDistanceDto> (); 
		landmarkDistanceList = new ArrayList< AplDistanceDto> ();
		String query = "select distance from distchart where (srcId=? and destId=?) or (destId=? and srcId=?)";
		try {
			pst = con.prepareStatement(query);
			int srcId=0;
			int destId=0;
			if(routingDto.getTravelMode().equalsIgnoreCase("OUT" )) {
				for (int i = landmarks.length; i > 0; i--) {
					
					if(i==landmarks.length) {
						srcId=siteLandmarkId;
						AplDistanceDto aplDto1 = new AplDistanceDto();
						aplDto1.setSourceId(String.valueOf(srcId));
						aplDto1.setDistance(0); 
						landmarkDistanceList.add(aplDto1);
						
					} else {
						srcId=landmarks[i];
						//destId = landmarks[i-1];
					}
						destId = landmarks[i-1];
 
						
						pst.setInt(1, srcId);
						pst.setInt(2, destId);
						pst.setInt(3, srcId);
						pst.setInt(4, destId);
						
						rs = pst.executeQuery();
						if (rs.next()) {
							distance[i-1] = rs.getFloat(1);
							totalDistance += rs.getFloat(1);
						} else {
							 
							if(srcId!=destId) {  
								setDistanceInDb(false);
							}
						}
						
						/* adding the site landmarks in queue */
						AplDistanceDto aplDto1 = new AplDistanceDto();
						aplDto1.setSourceId(String.valueOf(destId));
						aplDto1.setDistance(distance[i-1]); 
						landmarkDistanceList.add(aplDto1);
						 
							/* src, dest setup for db insert */
							AplDistanceDto aplDDto = new AplDistanceDto();
							aplDDto.setSourceId( String.valueOf(srcId) );
							aplDDto.setTargetId(String.valueOf(destId));
							aplDDto.setDistance(distance[i-1] );
							aplDistanceList.add(aplDDto);
							
						 
						 
				}
			
			} else if(routingDto.getTravelMode().equalsIgnoreCase("IN" )) {
				for (int i = 0; i<=landmarks.length; i++) {
					
					if(i==0) {
						
						/*srcId = landmarks[landmarks.length-1];
						destId=siteLandmarkId;
						*/
						distance[i]=0;
	/*					AplDistanceDto aplDto1 = new AplDistanceDto();
						aplDto1.setSourceId(String.valueOf(landmarks[i]));
						aplDto1.setDistance(0); 
						landmarkDistanceList.add(aplDto1);
	*/					
						
					} else if(i==landmarks.length) {
					//	System.out.println(i + " -> " + siteLandmarkId);
						srcId=landmarks[i-1];
						destId = siteLandmarkId;
						pst.setInt(1, srcId);
						pst.setInt(2, destId);
						pst.setInt(3, srcId);
						pst.setInt(4, destId);
						rs = pst.executeQuery();
						float distanceTemp =0f;
						if (rs.next()) {
							//distance[i] = rs.getFloat(1);
							distanceTemp = rs.getFloat(1);
							totalDistance += rs.getFloat(1);
						} else {
							 
							if(srcId!=destId) {  
								setDistanceInDb(false);
							}
						}
					 
						/* adding the site landmarks in queue */

						AplDistanceDto aplDto1 = new AplDistanceDto();
						aplDto1.setSourceId(String.valueOf(srcId));
						aplDto1.setDistance(0); 
						landmarkDistanceList.add(aplDto1);
						AplDistanceDto aplDto2 = new AplDistanceDto();
						aplDto2.setSourceId(String.valueOf(destId));
						aplDto2.setDistance(0); 
						landmarkDistanceList.add(aplDto2);
						
						/* src, dest setup for db insert */
						AplDistanceDto aplDDto = new AplDistanceDto();
						aplDDto.setSourceId( String.valueOf(srcId) );
						aplDDto.setTargetId(String.valueOf(destId));
						aplDDto.setDistance(distanceTemp );
						aplDistanceList.add(aplDDto);
						
						
						
						
					}else {
				//		System.out.println(i + " -> " + landmarks[i]);
						srcId=landmarks[i-1];
						destId = landmarks[i];
						pst.setInt(1, srcId);
						pst.setInt(2, destId);
						pst.setInt(3, srcId);
						pst.setInt(4, destId);
						rs = pst.executeQuery();
						if (rs.next()) {
							distance[i] = rs.getFloat(1);
							totalDistance += rs.getFloat(1);
						} else {
							distance[i] = 0;
							if(srcId!=destId) {  
								setDistanceInDb(false);
							}
						}
						
						
						/* test */
/*						if(srcId==1301 && destId == 719) {
							testCase = true;
							getErrorMessageList() .add(srcId + " - > " + destId + "=" + distance[i]);
						}
						testCase = false;
*/						/* adding the landmarks in queue */
						AplDistanceDto aplDto1 = new AplDistanceDto();
						aplDto1.setSourceId(String.valueOf(srcId));
						aplDto1.setDistance(distance[i]); 
						landmarkDistanceList.add(aplDto1);
						
						/* src, dest setup for db insert */
						AplDistanceDto aplDDto = new AplDistanceDto();
						aplDDto.setSourceId( String.valueOf(srcId) );
						aplDDto.setTargetId(String.valueOf(destId));
						aplDDto.setDistance(distance[i]);
						aplDistanceList.add(aplDDto);
						
						 
					}
				}
				
			
				
			
			}
			
			/*for(int i=1;i<landmarkDistanceList.size();i++) {
			//	 src, dest setup for db insert 
				AplDistanceDto aplDDto = new AplDistanceDto();
				aplDDto.setSourceId(String.valueOf(landmarkDistanceList.get(i-1).getSourceId()));
				aplDDto.setTargetId(String.valueOf(landmarkDistanceList.get(i).getSourceId()));
				aplDDto.setDistance(landmarkDistanceList.get(i).getDistance() );
				aplDistanceList.add(aplDDto);
			}
		 	*/	  
			
			 			
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally
		{
			DbConnect.closeResultSet(rs);			
			DbConnect.closeStatement(pst);	
		}
		routingDto.setDistance(totalDistance);
		return distance;
	}


	public float[] getTime(RoutingDto routingDto, float[] distance) {
		PreparedStatement pst = null;
		ResultSet rs = null;
		float time[] = new float[distance.length];
		float totalMinutes = (float) 0.0;
		float currentDistance = (float) 0.0;
		String query = "select ROUND( ?/speedpkm*60,0)as time from timeSloat where startTime<=? and endTime>?";

		try {
			pst = con.prepareStatement(query);
			if (routingDto.getTravelMode().equals("IN")) {
				
				for (int i = 0; i < distance.length; i++) {
					if(i==0)
					{
					currentDistance=routingDto.getDistance();
					}
					currentDistance -= distance[i];
					pst.setFloat(1, currentDistance);
					pst.setString(2, routingDto.getTime());
					pst.setString(3, routingDto.getTime());
					rs = pst.executeQuery();
					if (rs.next()) {
						time[i] = rs.getFloat("time");
						if(i == 0)
						totalMinutes = time[i];
					}
				}
			} else {
				for (int i = 0; i < distance.length; i++) {
					pst.setFloat(1, distance[i]);
					pst.setString(2, routingDto.getTime());
					pst.setString(3, routingDto.getTime());
					rs = pst.executeQuery();
					if (rs.next()) {
						time[i] = rs.getFloat("time");
						totalMinutes += time[i];
					}
				}
			}
			routingDto.setTravellingTime(totalMinutes);
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally
		{
			DbConnect.closeResultSet(rs);			
			DbConnect.closeStatement(pst);	
		}
		return time;

	}

	public void getSitelandmark(int siteId)

	{

		PreparedStatement pst = null;
		ResultSet rs =null;
		try {
			pst = con.prepareStatement("select landmark from site where id="
					+ siteId);
			rs = pst.executeQuery();
			if (rs.next()) {
				siteLandmarkId = rs.getInt(1);
			}

		} catch (Exception e) {
			System.out.println("error in update" + e);
		} finally {
			DbConnect.closeResultSet(rs);			
			DbConnect.closeStatement(pst);

		}

	}

	public boolean isDistanceInDb() {
		return distanceInDb;
	}

	public void setDistanceInDb(boolean distanceInDb) {
		this.distanceInDb = distanceInDb;
	}

	public ArrayList<AplDistanceDto> getLandmarkDistanceList() {
		return landmarkDistanceList;
	}

	public void setLandmarkDistanceList(ArrayList<AplDistanceDto> landmarkDistanceList) {
		this.landmarkDistanceList = landmarkDistanceList;
	}

/*	public ArrayList<String> getErrorMessageList() {
		return errorMessageList;
	}

	public void setErrorMessageList(ArrayList<String> errorMessageList) {
		this.errorMessageList = errorMessageList;
	}*/

}