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
import java.util.HashMap;

import com.agiledge.atom.commons.OtherFunctions;
import com.agiledge.atom.constants.SettingsConstant;
import com.agiledge.atom.dbConnect.DbConnect;
import com.agiledge.atom.dto.BackToBackDto;
import com.agiledge.atom.dto.RoutingDto;
import com.agiledge.atom.dto.TripDetailsChildDto;
import com.agiledge.atom.dto.TripDetailsDto;
import com.agiledge.atom.reports.dto.TripUtilisationReportDto;
import com.agiledge.atom.service.BackToBackRoutingService;



/**
 * 
 * @author muhammad
 */
public class BackToBackRoutingDao {

	static final int LADY_SECURTY = 1;
	StringBuilder selectedEmpSubscribedIDString = null;
	StringBuilder selectedEmployeeIDString = null;
	DbConnect ob = null;
	Connection con = null;
	ArrayList<Integer> selectedRoutesLandmarks = new ArrayList<Integer>();
	String uniqueId = "";
	String uniqueCode = "";
	int uniqueNo = 1;

	public HashMap<String, RoutingDto> setEmployeesForTravel(
			RoutingDto routingDto) throws SQLException {
		uniqueNo = 1;
		uniqueId = "";
		uniqueCode = "";
		// System.out.println("SET to Travel Starts"+routingDto.getDate()+routingDto.getTravelMode()+routingDto.getTime());
		uniqueCode = OtherFunctions.getOrdinaryDateFormat(routingDto.getDate());
		uniqueCode = uniqueCode.replaceAll("/", "");
		if (routingDto.getTravelMode().equals("IN"))
			uniqueCode += "P";
		if (routingDto.getTravelMode().equals("OUT"))
			uniqueCode += "D";
		Statement st = con.createStatement();
		ResultSet rs = st
				.executeQuery("select count(*) from trip_details where trip_date='"
						+ routingDto.getDate()
						+ "' and trip_log='"
						+ routingDto.getTravelMode() + "'");
		if (rs.next()) {
			uniqueNo = rs.getInt(1) + 1;
		}
		CallableStatement cst = null;
		rs = null;
		// st = null;
		HashMap<String, RoutingDto> routingDtoList = null;
		RoutingDto routingDtoToRoute = null;
		// System.out.println("Fields: siteId:"+siteid+"  Date:"+routingDto.getDate()+" time:"+routingDto.getTime()+" Log:"+routingDto.getTravelMode());
		cst = con.prepareCall("{CALL getEmployeeToTravel(?,?,?,?,?,?)}");
		cst.setInt(1, routingDto.getSiteId());
		cst.setString(2, routingDto.getDate());
		cst.setString(3, routingDto.getTime());
		cst.setString(4, routingDto.getTravelMode());
		cst.setString(5, "o");
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
					routingDtoToRoute.setEmployeegender(rs.getString("gender"));
					routingDto.setSiteLandmark(rs.getInt("sitelandmark"));
					routingDtoList.put(routingDtoToRoute.getEmployeeId(),
							routingDtoToRoute);
				} while (rs.next());
			}
		}
		// System.out.println("SET to Travel ENDS"+routingDtoList.size());
		return routingDtoList;
	}

	public int[] getBestRouteId(StringBuilder selectedEmpSubscribedIDString,
			StringBuilder selectedEmployeeIDString, RoutingDto routingDto)
			throws SQLException {
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
					query = "select rc.routeid,count(es.subscriptionID) as c "
							+ ",rt.type,rt.typeDesc,rt.priority from route r,routeTypes rt,"
							+ "employee_subscription es,routeChild rc where r.siteId=? "
							+ " and r.id=rc.routeId and r.type=rt.type and rt.priority<="
							+ i
							+ " and (es.subscriptionStatus='subscribed' or es.subscriptionStatus='pending') and rc.landmarkId=es.landmark and es.subscriptionID in("
							+ selectedEmpSubscribedIDString
							+ ") and "
							+ "es.landmark in(SELECT landmarkId from routeChild) group by rc.routeid,rt.type,rt.typeDesc,rt.priority order by c desc limit 1";
					// System.out.println("i="+i+"            Query"+query);

					pst = con.prepareStatement(query);
					pst.setInt(1, routingDto.getSiteId());
					rs = pst.executeQuery();
					if (rs.next()) {
						routeId = rs.getInt("routeid");
						employeeCount = rs.getInt("c");
					}
					if (employeeCount <= 2) {
						if (!checkCombainedRouteStatus(routingDto)) {
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
				System.out.println("ERRORRoute ID" + e);
			}

		}
		// System.out.println("GEt BEst Route ENDS");
		return returnArray;
	}

	private boolean checkCombainedRouteStatus(RoutingDto routingDto)
			throws SQLException {
		PreparedStatement pst = null;
		ResultSet rs = null;
		boolean retFlag = false;
		String query = "select combroute,combainedRouteOnspecifiedTime,combainedRouteOnweekoff from logtime,site where logtime=? and logtype=? and site.id=?";
		// System.out.println("In combained query"+query);
		pst = con.prepareStatement(query);
		pst.setString(1, routingDto.getTime());
		pst.setString(2, routingDto.getTravelMode());
		pst.setInt(3, routingDto.getSiteId());
		rs = pst.executeQuery();
		if (rs.next()) {
			if (rs.getString(1).equals("yes") && rs.getInt(2) == 1) {
				retFlag = true;
			} else if (rs.getInt(3) == 1
					&& (OtherFunctions.getDay(routingDto.getDate()) == 1 || OtherFunctions
							.getDay(routingDto.getDate()) == 7)) {
				retFlag = true;
			}
		}
		DbConnect.closeResultSet(rs);
		DbConnect.closeStatement(pst);
		return retFlag;
	}

	public ArrayList<Integer> getLandmarksOfTheSelectedRoute(int routeId)
			throws SQLException {
		// System.out.println("get Landmarks starts");
		PreparedStatement pst = null;
		ResultSet rs = null;
		selectedRoutesLandmarks.clear();
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
		// System.out.println("get Landmarks ends");
		return selectedRoutesLandmarks;
	}

	public int checkSecurity(RoutingDto routingDto) throws SQLException {
		// System.out.println("security check starts");
		PreparedStatement pst = null;
		ResultSet rs = null;
		String query = "SELECT * FROM site WHERE (night_shift_start<'"
				+ routingDto.getTime() + "' OR night_shift_end>'"
				+ routingDto.getTime() + "')AND lady_security=? and id=?";
		// System.out.println("query"+query);
		pst = con.prepareStatement(query);
		pst.setInt(1, LADY_SECURTY);
		pst.setInt(2, routingDto.getSiteId());
		rs = pst.executeQuery();
		if (rs.next()) {
			return 1;
		}
		// System.out.println("check securuty ends");
		return 0;

	}

	public int checkFemaleInLast(int lastLandmark) throws SQLException {
		// System.out.println("check female in last starts");
		PreparedStatement pst = null;
		ResultSet rs = null;
		pst = con
				.prepareStatement("select count(e.id) as femaleCount from employee e,employee_subscription es  where es.empID=e.id and es.subscriptionID in("
						+ selectedEmpSubscribedIDString
						+ ") and e.gender='F' and es.landmark=?");
		pst.setInt(1, lastLandmark);
		rs = pst.executeQuery();
		if (rs.next()) {
			return rs.getInt("femaleCount");
		}
		// System.out.println("check female in last ends");
		return 0;

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
		if (uniqueNo < 10) {
			uniqueId = uniqueCode + "00" + uniqueNo;
		} else if (uniqueNo < 100) {
			uniqueId = uniqueCode + "0" + uniqueNo;
		} else {
			uniqueId = uniqueCode + uniqueNo;
		}

		uniqueNo++;

		PreparedStatement pst = null;
		ResultSet rs = null;
		int retVal = 0;
		pst = con
				.prepareStatement(
						"insert into trip_details(trip_code,siteId,trip_date,trip_time,trip_log,routeId,vehicle,vehicle_type,status,security_status,distance,travelTime,destinationLandmark,preTripId,distanceFromPreTrip) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
						Statement.RETURN_GENERATED_KEYS);
		pst.setString(1, uniqueId);
		pst.setString(2, tripDetailsDtoObj.getSiteId());
		pst.setString(3, tripDetailsDtoObj.getTrip_date());
		pst.setString(4, tripDetailsDtoObj.getTrip_time());
		pst.setString(5, tripDetailsDtoObj.getTrip_log());
		pst.setString(6, tripDetailsDtoObj.getRouteId());
		pst.setString(7, tripDetailsDtoObj.getVehicle());
		pst.setString(8, tripDetailsDtoObj.getVehicle_type());
		pst.setString(9, "routed");
		pst.setString(10, tripDetailsDtoObj.getIsSecurity());
		pst.setString(11, tripDetailsDtoObj.getDistance());
		pst.setString(12, tripDetailsDtoObj.getTravelTime());
		pst.setString(13, tripDetailsDtoObj.getSrcDestLandmark());
		pst.setString(14, tripDetailsDtoObj.getPreviousTripId());
		pst.setFloat(15, tripDetailsDtoObj.getDistanceFromPrevioustrip());
		pst.executeUpdate();
		rs = pst.getGeneratedKeys();
		if (rs.next()) {
			retVal = rs.getInt(1);
		}

		return retVal;
	}

	public void storeInTripChild(TripDetailsChildDto tripDetailsChildDtoObj)
			throws SQLException {
		// System.out.println("store in trip child staarts");
		PreparedStatement pst = null;
		pst = con
				.prepareStatement("insert into trip_details_child(tripId,employeeId,routedOrder,landmarkId,time,dist) values(?,?,?,?,?,?)");
		pst.setString(1, tripDetailsChildDtoObj.getTripId());
		pst.setString(2, tripDetailsChildDtoObj.getEmployeeId());
		pst.setString(3, tripDetailsChildDtoObj.getRoutedOrder());
		pst.setString(4, tripDetailsChildDtoObj.getLandmarkId());
		pst.setString(5, tripDetailsChildDtoObj.getTime());
		pst.setFloat(6, tripDetailsChildDtoObj.getDistance());
		pst.executeUpdate();
		// System.out.println("store in trip child end");
	}

	public void closeConnection() {
		// System.out.println("close staarts");
		DbConnect.closeConnection(con);
	}

	public int checkTripExist(RoutingDto routingDto) {
		DbConnect ob1 = null;
		Connection con1 = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		PreparedStatement pst1 = null;
		int retunVal = 0;
		//int sec = 0;
		try {
			ob1 = DbConnect.getInstance();
			con1 = ob1.connectDB();
			if (routingDto.getTravelMode().equalsIgnoreCase("all")) {
			//	sec = 1;
				String query = "select id from trip_details where siteId=? and trip_date=? and status!=? and status!=? and status!=?";
				pst1 = con1.prepareStatement(query);
				pst1.setInt(1, routingDto.getSiteId());
				pst1.setString(2, routingDto.getDate());
				pst1.setString(3, "saved");
				pst1.setString(4, "addsave");
				pst1.setString(5, "saveedit");
			} else if (routingDto.getTime().equalsIgnoreCase("all")) {
			//	sec = 2;
				String query = "select id from trip_details where siteId=? and trip_date=? and trip_log=? and status!=? and status!=? and status!=?";
				pst1 = con1.prepareStatement(query);
				pst1.setInt(1, routingDto.getSiteId());
				pst1.setString(2, routingDto.getDate());
				pst1.setString(3, routingDto.getTravelMode());
				pst1.setString(4, "saved");
				pst1.setString(5, "addsave");
				pst1.setString(6, "saveedit");
			} else {
			//	sec = 3;
				String query = "select id from trip_details where siteId=? and trip_date=? and trip_log=? and trip_time=? and status!=? and status!=? and status!=?";
				pst1 = con1.prepareStatement(query);
				pst1.setInt(1, routingDto.getSiteId());
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

	public int overWriteTrips(RoutingDto routingDto) {
		DbConnect ob1 = null;
		Connection con1 = null;
		PreparedStatement pst = null;
		//ResultSet rs = null;
		PreparedStatement pst1 = null;
		int retunVal = 0;
		try {
			ob1 = DbConnect.getInstance();
			con1 = ob1.connectDB();
			if (routingDto.getTravelMode().equalsIgnoreCase("all")) {
				String query = "delete from trip_details_child where tripId in (select id from trip_details where siteId=? and trip_date=? and status!=? and status!=? and status!=?)";
				pst = con1.prepareStatement(query);
				pst.setInt(1, routingDto.getSiteId());
				pst.setString(2, routingDto.getDate());
				pst.setString(3, "saved");
				pst.setString(4, "addsave");
				pst.setString(5, "saveedit");

				String query1 = "delete from trip_details where siteId=? and trip_date=? and status!=? and status!=? and status!=?";
				pst1 = con1.prepareStatement(query1);
				pst1.setInt(1, routingDto.getSiteId());
				pst1.setString(2, routingDto.getDate());
				pst1.setString(3, "saved");
				pst1.setString(4, "addsave");
				pst1.setString(5, "saveedit");
			} else if (routingDto.getTime().equalsIgnoreCase("all")) {
				String query = "delete from trip_details_child where tripId in (select id from trip_details where siteId=? and trip_date=? and trip_log=? and status!=? and status!=? and status!=?)";
				pst = con1.prepareStatement(query);
				pst.setInt(1, routingDto.getSiteId());
				pst.setString(2, routingDto.getDate());
				pst.setString(3, routingDto.getTravelMode());
				pst.setString(4, "saved");
				pst.setString(5, "addsave");
				pst.setString(6, "saveedit");

				String query1 = "delete from trip_details where siteId=? and trip_date=? and trip_log=? and status!=? and status!=? and status!=?";
				pst1 = con1.prepareStatement(query1);
				pst1.setInt(1, routingDto.getSiteId());
				pst1.setString(2, routingDto.getDate());
				pst1.setString(3, routingDto.getTravelMode());
				pst1.setString(4, "saved");
				pst1.setString(5, "addsave");
				pst1.setString(6, "saveedit");
			} else {
				String query = "delete from trip_details_child where tripId in (select id from trip_details where siteId=? and trip_date=? and trip_log=? and trip_time=? and status!=? and status!=? and status!=?)";
				pst = con1.prepareStatement(query);
				pst.setInt(1, routingDto.getSiteId());
				pst.setString(2, routingDto.getDate());
				pst.setString(3, routingDto.getTravelMode());
				pst.setString(4, routingDto.getTime());
				pst.setString(5, "saved");
				pst.setString(6, "addsave");
				pst.setString(7, "saveedit");
				String query1 = "delete from trip_details where siteId=? and trip_date=? and trip_log=? and trip_time=? and status!=? and status!=? and status!=?";
				pst1 = con1.prepareStatement(query1);
				pst1.setInt(1, routingDto.getSiteId());
				pst1.setString(2, routingDto.getDate());
				pst1.setString(3, routingDto.getTravelMode());
				pst1.setString(4, routingDto.getTime());
				pst1.setString(5, "saved");
				pst1.setString(6, "addsave");
				pst1.setString(7, "saveedit");
			}
			retunVal = pst.executeUpdate();
			retunVal += pst1.executeUpdate();
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

	public ArrayList<RoutingDto> getAllroutingDtos(RoutingDto routingDto) {
		ob = DbConnect.getInstance();
		con = ob.connectDB();
		PreparedStatement pst = null;
		ResultSet rs = null;
		ArrayList<RoutingDto> routingDtos = new ArrayList<RoutingDto>();
		RoutingDto dto = null;
		routingDto.setSiteId(1);
		System.out.println("siteee"+routingDto.getSiteId());
		// TODO Auto-generated method stub
		// String
		// query="(select distinct table1.login_time as time,'IN' as logtype from (select login_time from employee_schedule where from_date<=? and to_date>=?  union select login_time from employee_schedule_alter where date=?) as table1 where table1.login_time not in (select trip_details.trip_time from trip_details where trip_date=? and trip_log='IN'))";
		// String
		// query1="(select distinct table2.logout_time as time,'OUT' as logtype from (select logout_time from employee_schedule where from_date<=? and to_date>=?  union select logout_time from employee_schedule_alter where date=?) as table2 where table2.logout_time not in (select trip_details.trip_time from trip_details where trip_date=? and trip_log='OUT'))";
		String query = "(select distinct table1.login_time as time,'IN' as logtype,table1.bookType from (select login_time,'shift' as  bookType from employee_schedule where from_date<=? and to_date>=? and status='a'  union select login_time,'shift' as  bookType from employee_schedule_alter where date=?  and status='a' union select startTime login_time,'adhoc' as  bookType from adhocBooking where adhocType='"+SettingsConstant.SHIFT_EXTENSTION+"' and siteId="+routingDto.getSiteId()+" and travelDate=? and pickDrop='IN') as table1 where table1.login_time not in (select trip_details.trip_time from trip_details where trip_date=?  and trip_log='IN' and siteId="
				+ routingDto.getSiteId() + ") order by table1.login_time )";

		String query1 = "(select distinct table2.logout_time as time,'OUT' as logtype,table2.bookType from (select logout_time,'shift' as  bookType from employee_schedule where from_date<=? and to_date>=? and status='a' union select logout_time,'shift' as  bookType from employee_schedule_alter where date=? and status='a' union select startTime as login_time,'adhoc' as  bookType from adhocBooking where adhocType='"+SettingsConstant.SHIFT_EXTENSTION+"' and siteId="+routingDto.getSiteId()+" and travelDate=? and pickDrop='OUT') as table2 where table2.logout_time not in (select trip_details.trip_time from trip_details where trip_date=? and trip_log='OUT' and siteId="
				+routingDto.getSiteId()+ ") order by table2.logout_time )";
		String queryLastpart=" as td where time!='' and time!='null' and time!='none' and time!='' and time!='weekly off' and time is not null " +
				" and time+logtype not in (select trip_time+trip_log from trip_details where siteid="+routingDto.getSiteId()+" and trip_date='"+routingDto.getDate()+"')" +
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
					dto.setSiteId(routingDto.getSiteId());
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
		return routingDtos;
	}

	/*
	 * if (preTripId != 0) { Statement st = con.createStatement(); ResultSet rs1
	 * = st .executeQuery(
	 * "select top 1 t.distance, tdc.landmarkId from trip_details t, trip_details_child tdc where t.id="
	 * + preTripId + " and t.id=tdc.tripId order by tdc.routedOrder desc"); if
	 * (rs1.next()) { preTripDistance = rs1.getFloat("distance");
	 * preTriplastLandmark = rs1.getInt("landmarkId"); } }
	 * if(routingDto.getTravelMode().equals("IN")) { pst.setInt(1,
	 * siteLandmarkId); pst.setInt(2, landmarks[landmarks.length-1]);
	 * pst.setInt(3, siteLandmarkId); pst.setInt(4,
	 * landmarks[landmarks.length-1]); rs = pst.executeQuery(); if (rs.next()) {
	 * totalDistance += rs.getFloat(1); }
	 */

	public float[] getDistances(int[] landmarks, RoutingDto routingDto)
			throws SQLException {
		int siteLandmarkId = 3314;
		PreparedStatement pst = null;
		ResultSet rs = null;
		float totalDistance = (float) 0.0;
		float distance[] = new float[landmarks.length];
		String query = "select distance from distchart where (srcId=? and destId=?) or (destId=? and srcId=?)";
		try {
			pst = con.prepareStatement(query);
			for (int i = 0; i < landmarks.length; i++) {
				if (routingDto.getTravelMode().equals("IN")) {
					if (i == 0) {
						pst.setInt(1, siteLandmarkId);
						pst.setInt(2, landmarks[landmarks.length - 1]);
						pst.setInt(3, siteLandmarkId);
						pst.setInt(4, landmarks[landmarks.length - 1]);
					} else {
						distance[0] = 0;
						pst.setInt(1, landmarks[i - 1]);
						pst.setInt(2, landmarks[i]);
						pst.setInt(3, landmarks[i - 1]);
						pst.setInt(4, landmarks[i]);
					}
				} else {
					if (i == landmarks.length - 1) {
						pst.setInt(1, siteLandmarkId);
						pst.setInt(2, landmarks[i]);
						pst.setInt(3, siteLandmarkId);
						pst.setInt(4, landmarks[i]);
					} else {
						pst.setInt(1, landmarks[i]);
						pst.setInt(2, landmarks[i + 1]);
						pst.setInt(3, landmarks[i]);
						pst.setInt(4, landmarks[i + 1]);
					}
				}
				rs = pst.executeQuery();
				if (rs.next()) {

					distance[i] = rs.getFloat(1);
					totalDistance += distance[i];
				} else {
					distance[i] = 0;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		routingDto.setDistance(totalDistance);
		return distance;
	}

	public float[] getTime(RoutingDto routingDto, float[] distance)
			throws SQLException {
		PreparedStatement pst = null;
		ResultSet rs = null;
		float time[] = new float[distance.length];
		float totalMinutes = (float) 0.0;
		float sumOfistances = (float) 0.0;
		String query = "select ROUND( ?/speedpkm*60,0)as time from timeSloat where startTime<=? and endTime>?";

		try {
			pst = con.prepareStatement(query);
			if (routingDto.getTravelMode().equals("IN")) {
				for (int i = distance.length - 1; i >= 0; i--) {
					if (i == 0) {
						pst.setFloat(1, routingDto.getDistance()
								- sumOfistances);
					} else {
						sumOfistances += distance[i];
						pst.setFloat(1, distance[i]);
					}
					pst.setString(2, routingDto.getTime());
					pst.setString(3, routingDto.getTime());

					rs = pst.executeQuery();
					if (rs.next()) {
						time[i] = rs.getFloat("time");
						totalMinutes += time[i];
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
		return time;

	}

	public ArrayList<TripDetailsDto> getPreviousTrips(RoutingDto routingDto,
			String currentSrcdestionationlandmark) throws Exception {
		String distanceAndTime[] = null;
		PreparedStatement pst = null;
		ArrayList<TripDetailsDto> PreviousTripdtos = new ArrayList<TripDetailsDto>();
		TripDetailsDto tripDetailsDtoObj = null;
		ResultSet rs = null;
		String query = "select t.*,vt.sit_cap, CAST(TIME_TO_SEC(TIMEDIFF(t.trip_time,?))/60 AS DECIMAL(0)) as timediff from trip_details t,vehicle_type vt where vt.id=t.vehicle_type and "
				+ "t.siteId=? and  t.trip_date=? and t.trip_log='OUT' and  "
				+ "CAST(TIME_TO_SEC(TIMEDIFF(t.trip_time,?))/60 AS DECIMAL(0)) between 30 and 300 "
				+ "union "
				+ "select t.*,vt.sit_cap, CAST(TIME_TO_SEC(TIMEDIFF(t.trip_time,?))/60 AS DECIMAL(0)) as timediff from trip_details t,vehicle_type vt "
				+ "where vt.id=t.vehicle_type and  t.siteId=? and  DATE_ADD(t.trip_date,INTERVAL 1 DAY)=? and t.trip_log='OUT' and  "
				+ "CAST(TIME_TO_SEC(TIMEDIFF(t.trip_time,?))/60 AS DECIMAL(0)) between -1410 and -1140 ";
		pst = con.prepareStatement(query);
		pst.setString(1, routingDto.getTime());
		pst.setInt(2, routingDto.getSiteId());
		pst.setString(3, routingDto.getDate());
		pst.setString(4, routingDto.getTime());
		pst.setString(5, routingDto.getTime());
		pst.setInt(6, routingDto.getSiteId());
		pst.setString(7, routingDto.getDate());
		pst.setString(8, routingDto.getTime());
		System.out.println("here"+query+"\nparams "+routingDto.getTime()+" "+routingDto.getSiteId()+" "+routingDto.getDate()+" "+routingDto.getTime()+" ");
		rs = pst.executeQuery();

		while (rs.next()) {

			while (rs.next()) {
				tripDetailsDtoObj = new TripDetailsDto();
				System.out.println("here"+rs.getString("id"));
				tripDetailsDtoObj.setId(rs.getString("id"));
				tripDetailsDtoObj.setActualLogTime(rs.getString("trip_time"));
				tripDetailsDtoObj.setTrip_code(rs.getString("trip_code"));
				tripDetailsDtoObj.setTrip_date(rs.getDate("trip_date")
						.toString());
				tripDetailsDtoObj.setTrip_time(rs.getString("trip_time"));
				tripDetailsDtoObj.setTrip_log(rs.getString("trip_log"));
				tripDetailsDtoObj.setRouteId(rs.getString("routeid"));
				tripDetailsDtoObj.setTrip_code(rs.getString("trip_code"));
				tripDetailsDtoObj.setVehicle_type(rs.getString("vehicle_type"));
				tripDetailsDtoObj
						.setIsSecurity(rs.getString("security_status"));
				tripDetailsDtoObj.setDistance(rs.getString("distance"));
				tripDetailsDtoObj.setTravelTime(rs.getString("travelTime"));
				tripDetailsDtoObj.setSrcDestLandmark(rs
						.getString("destinationLandmark"));
				tripDetailsDtoObj.setSeat(rs.getInt("sit_cap"));
				distanceAndTime = getDistanceAndTime(
						tripDetailsDtoObj.getSrcDestLandmark(),
						currentSrcdestionationlandmark,
						tripDetailsDtoObj.getTrip_time(),
						tripDetailsDtoObj.getTravelTime());

				tripDetailsDtoObj.setDistanceFromPrevioustrip(Float
						.parseFloat(distanceAndTime[0]));
				tripDetailsDtoObj.setTimeFromPrevioustrip(distanceAndTime[1]);
				PreviousTripdtos.add(tripDetailsDtoObj);
			}
		}
		return PreviousTripdtos;
	}

	private String[] getDistanceAndTime(String srcDestLandmark,
			String currentSrcdestionationlandmark, String tripTime,
			String TravelTime) throws Exception {
		String distanceAndTime[] = new String[2];
		try {
			CallableStatement cst = con
					.prepareCall("{call getDistanceAndTime(?,?,?,?)}");
			cst.setString(1, srcDestLandmark);
			cst.setString(2, currentSrcdestionationlandmark);
			cst.setString(3, tripTime);
			cst.setString(4, TravelTime);
			ResultSet rs = cst.executeQuery();
			if (rs.next()) {
				distanceAndTime[0] = rs.getString(1);
				distanceAndTime[1] = rs.getString(2);
			}
		} catch (Exception e) {
			System.out.println("Error in get distance Time" + e);
			throw (e);
		}
		return distanceAndTime;
	}

	public float getDistancebetweentwoLandmarks(int source, int destination)
			throws SQLException {
		float distance = (float) 0.0;
		String query = "select distance from distchart where (srcId=" + source
				+ " and destId=" + destination + ") or (srcId=" + destination
				+ " and destId=" + source + ")";
		System.out.println("Query" + query);
		Statement st = con.createStatement();
		ResultSet rs = st.executeQuery(query);
		if (rs.next()) {
			distance = rs.getFloat(1);
		}
		return distance;
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
	
	public ArrayList<TripDetailsDto> getINTrips(String date,String site)
	{
		ArrayList<TripDetailsDto> list=new ArrayList<TripDetailsDto>();
		DbConnect ob = DbConnect.getInstance();
		String query="SELECT TD.ID,TD.trip_time,TD.security_status,TD.distance,TDC.LANDMARKID AS DESTINATIONID,DATE_FORMAT(TIMESTAMPADD(MINUTE,-TD.TRAVELTIME,CONCAT(?,' ',TD.TRIP_TIME)),'%d-%m-%Y %H:%i:%s') AS startdate,TD.VEHICLE_TYPE FROM TRIP_DETAILS TD JOIN TRIP_DETAILS_CHILD TDC ON TDC.TRIPID=TD.ID AND TDC.ROUTEDORDER=1 WHERE TD.TRIP_DATE BETWEEN ? AND DATE_ADD(?,INTERVAL 1 DAY) and TD.TRIP_LOG='IN' AND TD.SITEID=? AND TRAVELTIME!=0 AND PRETRIPID=0";
		Connection con = ob.connectDB();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try{
				stmt = con.prepareStatement(query);
				stmt.setString(1, date);
				stmt.setString(2, date);
				stmt.setString(3, date);
				stmt.setString(4, site);
				rs = stmt.executeQuery();
				while(rs.next())
				{
					TripDetailsDto dto=new TripDetailsDto();
					dto.setId(rs.getString("id"));
					dto.setTrip_time(rs.getString("trip_time"));
					dto.setIsSecurity(rs.getString("security_status"));
					dto.setDistance(rs.getString("distance"));
					dto.setSrcDestLandmark(rs.getString("DESTINATIONID"));
					dto.setTrip_date(rs.getString("startdate"));
					dto.setVehicle_type(rs.getString("VEHICLE_TYPE"));
					list.add(dto);
				}
		}catch(Exception e){System.out.println("error in getiN Trips"+e);}
		finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(stmt);
			DbConnect.closeConnection(con);
		}
		return list;
	}
	
	public ArrayList<TripDetailsDto> getOUTTrips(String date,String site)
	{
		ArrayList<TripDetailsDto> list=new ArrayList<TripDetailsDto>();
		DbConnect ob = DbConnect.getInstance();
		String query="SELECT TD.ID,TD.trip_time,TD.security_status,TD.distance,TDC.LANDMARKID AS DESTINATIONID,DATE_FORMAT(TIMESTAMPADD(MINUTE,TD.TRAVELTIME,CONCAT(?,' ',TD.TRIP_TIME)),'%d-%m-%Y %H:%i:%s') AS dropdate,TD.VEHICLE_TYPE FROM TRIP_DETAILS TD JOIN TRIP_DETAILS_CHILD TDC ON TDC.TRIPID=TD.ID AND ROUTEDORDER=(SELECT COUNT(TRIPID) FROM TRIP_DETAILS_CHILD WHERE TRIPID=TD.ID) WHERE TD.TRIP_DATE =? and TD.TRIP_LOG='OUT' AND TD.SITEID=? AND  TD.TRAVELTIME!=0";
		Connection con = ob.connectDB();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try{
				stmt = con.prepareStatement(query);
				stmt.setString(1, date);
				stmt.setString(2, date);
				stmt.setString(3, site);
				rs = stmt.executeQuery();
				while(rs.next())
				{
					System.out.println("time"+rs.getString("trip_time")+"vv"+rs.getString("dropdate"));
					TripDetailsDto dto=new TripDetailsDto();
					dto.setId(rs.getString("id"));
					dto.setTrip_time(rs.getString("trip_time"));
					dto.setIsSecurity(rs.getString("security_status"));
					dto.setDistance(rs.getString("distance"));
					dto.setSrcDestLandmark(rs.getString("DESTINATIONID"));
					dto.setTrip_date(rs.getString("dropdate"));
					dto.setVehicle_type(rs.getString("VEHICLE_TYPE"));
					list.add(dto);
				}
		}catch(Exception e){System.out.println("error in getOut Trips"+e);}
		finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(stmt);
			DbConnect.closeConnection(con);
		}
		return list;
	}
	
	public boolean checkDistance(String landark1,String landmark2)
	{
		boolean result=false;
		DbConnect ob = DbConnect.getInstance();
		String query="select l1.latitude,l1.longitude,l2.latitude as lat2,l2.longitude as long2  from landmark l1 join landmark l2 on l2.id=? where l1.id=?";
		Connection con = ob.connectDB();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try{
			
			stmt = con.prepareStatement(query);
			stmt.setString(1, landark1);
			stmt.setString(2, landmark2);
			rs = stmt.executeQuery();
			if(rs.next())
			{
				System.out.println("lat"+rs.getLong("latitude")+"long"+rs.getLong("longitude")+"lat2"+rs.getLong("lat2")+"long 2"+ rs.getLong("long2"));
				double no= new BackToBackRoutingService().getdistance(rs.getLong("latitude"), rs.getLong("longitude"),rs.getLong("lat2") , rs.getLong("long2"));
				if(no<=10)
				{
					System.out.println("distance"+no);
					result=true;
				}
			}
		}catch(Exception e){e.printStackTrace();}
		finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(stmt);
			DbConnect.closeConnection(con);
		}
		return result;
	}
	
	public int UpdatePreviousTrip(String previd,String prevdis,String tripid)
	{
		DbConnect ob = DbConnect.getInstance();
		Connection con = null;
		PreparedStatement pst = null;
		int returnint = 0;
		try {
			con = ob.connectDB();
			String query = "UPDATE TRIP_DETAILS SET PRETRIPID=?,DISTANCEFROMPRETRIP=? WHERE ID=?";
			pst = con.prepareStatement(query);
			pst.setString(1, previd);
			pst.setString(2, prevdis);
			pst.setString(3, tripid);
			returnint = pst.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);
		}
		return returnint;
	}
	public boolean checkBackTrip(String tripid)
	{
		boolean result=false;
		DbConnect ob = DbConnect.getInstance();
		String query="SELECT PRETRIPID FROM TRIP_DETAILS WHERE ID=?";
		Connection con = ob.connectDB();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try{
			
			stmt = con.prepareStatement(query);
			stmt.setString(1, tripid);
			rs = stmt.executeQuery();
			if(rs.next())
			{
				int no= rs.getInt("PRETRIPID");
				if(no==0)
				{
					result=true;
				}
			}
		}catch(Exception e){e.printStackTrace();}
		finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(stmt);
			DbConnect.closeConnection(con);
		}
		return result;	
	}
	
	public boolean checkVehicleType(String typeid1,String typeid2)
	{

		boolean result=false;
		DbConnect ob = DbConnect.getInstance();
		String query="SELECT VT1.SIT_CAP AS SEAT1,VT2.SIT_CAP AS SEAT2 FROM VEHICLE_TYPE VT1 JOIN VEHICLE_TYPE VT2 ON VT2.ID=? WHERE VT1.ID=?" ;
		Connection con = ob.connectDB();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try{
			
			stmt = con.prepareStatement(query);
			stmt.setString(1, typeid2);
			stmt.setString(2, typeid1);
			rs = stmt.executeQuery();
			if(rs.next())
			{
				int outseat=rs.getInt("SEAT1");
				int inseat=rs.getInt("SEAT2");
				if(outseat==inseat||outseat>inseat)
				{
					result=true;
				}
			}
		}catch(Exception e){e.printStackTrace();}
		finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(stmt);
			DbConnect.closeConnection(con);
		}
		return result;
	
	}
	
	
	public ArrayList<BackToBackDto> getAllBackToBackTrips(String site,String date,String todate)
	{
		ArrayList<BackToBackDto> list=new ArrayList<BackToBackDto>();
		DbConnect ob = DbConnect.getInstance();
		date = OtherFunctions.changeDateFromatToIso(date);
		String query="SELECT TD1.ID,TD1.TRIP_CODE,DATE_FORMAT(TD1.TRIP_DATE,'%d-%m-%Y') as trip_date,TD1.TRIP_TIME,TD1.TRIP_LOG,TD1.SECURITY_STATUS,TD1.DISTANCE,TD1.TRAVELTIME,VT1.TYPE as type1,TD2.ID as id2,TD2.TRIP_CODE as code2,DATE_FORMAT(TD2.TRIP_DATE,'%d-%m-%Y') as date2,TD2.TRIP_TIME as time2,TD2.TRIP_LOG as log2,TD2.SECURITY_STATUS as security2,TD2.DISTANCE as distance2,TD2.TRAVELTIME as traveltime2,VT2.TYPE as vehicle2 FROM TRIP_DETAILS TD1 JOIN VEHICLE_TYPE VT1 ON VT1.ID=TD1.VEHICLE_TYPE JOIN TRIP_DETAILS TD2 ON TD2.ID=TD1.PRETRIPID JOIN VEHICLE_TYPE VT2 ON VT2.ID=TD2.VEHICLE_TYPE WHERE TD1.TRIP_DATE BETWEEN ? AND ? AND TD1.SITEID=?" ;
		Connection con = ob.connectDB();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try{
			
			stmt = con.prepareStatement(query);
			stmt.setString(1, date);
			stmt.setString(2, todate);
			stmt.setString(3, site);
			rs = stmt.executeQuery();
			while(rs.next())
			{
				BackToBackDto dto = new BackToBackDto();
				dto.setTripid1(rs.getString("id"));
				dto.setTripcode1(rs.getString("trip_code"));
				dto.setTripdate1(rs.getString("trip_date"));
				dto.setTriptime1(rs.getString("trip_time"));
				dto.setTriplog1(rs.getString("trip_log"));
				dto.setSecurity1(rs.getString("security_status"));
				dto.setDistance1(rs.getString("distance"));
				dto.setTraveltime1(rs.getString("traveltime"));
				dto.setVehicletype1(rs.getString("type1"));
				
				dto.setTripid2(rs.getString("id2"));
				dto.setTripcode2(rs.getString("code2"));
				dto.setTripdate2(rs.getString("date2"));
				dto.setTriptime2(rs.getString("time2"));
				dto.setTriplog2(rs.getString("log2"));
				dto.setSecurity2(rs.getString("security2"));
				dto.setDistance2(rs.getString("distance2"));
				dto.setTraveltime2(rs.getString("traveltime2"));
				dto.setVehicletype2(rs.getString("vehicle2"));
				list.add(dto);
			}
			}catch(Exception e){e.printStackTrace();}
	finally {
		DbConnect.closeResultSet(rs);
		DbConnect.closeStatement(stmt);
		DbConnect.closeConnection(con);
	}
	return list;
	}
	
	public ArrayList<BackToBackDto> getTripsMonthly(String site, String fromDate, String toDate) {
		DbConnect dbConn = DbConnect.getInstance();
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;	
		fromDate=OtherFunctions.changeDateFromatToIso(fromDate);
		toDate = OtherFunctions.changeDateFromatToIso(toDate);				
		fromDate=fromDate==null?"":fromDate;
		toDate=toDate==null?"":toDate;
		site=site==null?"":site;
		ArrayList<BackToBackDto> dtoList = new ArrayList<BackToBackDto>();		
		try {
			String query="select DATE_FORMAT(td.trip_date, '%d/%m/%Y') as trip_date,count(td.id) as totaltrips, SUM(CASE WHEN td.trip_log='IN' THEN 1 ELSE 0 END) as incount,SUM(CASE WHEN td.security_status='Yes' THEN 1 ELSE 0 END) as escort,SUM(CASE WHEN td.pretripid=0 then 1 else 0 end) as normal,SUM(CASE WHEN td.pretripid!=0 then 1 else 0 end) as backtoback from trip_details td where td.trip_date between '"+fromDate+"'  and '"+toDate+"' and td.siteid="+site+" group by td.trip_date";
			System.out.println("QUERY: " +query);
			con = dbConn.connectDB();
			stmt = con.createStatement();
			rs = stmt.executeQuery(query);
			while (rs.next()) {
				BackToBackDto dto = new BackToBackDto();
				dto.setTripdate1(rs.getString("trip_date"));
			/*	dto.setTotalcount(rs.getInt("totaltrips"));
				dto.setIncount(rs.getInt("incount"));
				dto.setEscortcount(rs.getInt("escort"));
				dto.setOutcount(dto.getTotalcount()-dto.getIncount());
				dto.setNonescortcount(dto.getTotalcount()-dto.getEscortcount());*/
			 	 	 dtoList.add(dto);
			}
			return dtoList;
			
		} catch (Exception e) {
			System.out.println("Error in trip summary report"+ e);
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(stmt);
			DbConnect.closeConnection(con);
		}

		return null;		

		
	}
}