/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.agiledge.atom.service;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;
import static java.lang.Math.abs;

import com.agiledge.atom.commons.OtherFunctions;
import com.agiledge.atom.dao.BackToBackRoutingDao;
import com.agiledge.atom.dao.VehicleDao;
import com.agiledge.atom.dto.BackToBackDto;
import com.agiledge.atom.dto.RoutingDto;
import com.agiledge.atom.dto.TripDetailsChildDto;
import com.agiledge.atom.dto.TripDetailsDto;
import com.agiledge.atom.dto.VehicleDto;
import com.agiledge.atom.dto.VehicleTypeDto;


/**
 * 
 * @author muhammad
 */
public class BackToBackRoutingService {

	HashMap<String, RoutingDto> employeeDtoList = null;
	LinkedHashMap<String, RoutingDto> employeesInRoute = new LinkedHashMap<String, RoutingDto>();
	HashMap<String, RoutingDto> allocatedEmployeeDtoList = new HashMap<String, RoutingDto>();
	BackToBackRoutingDao BackToBackRoutingDaoObj = null;
	int selectedRoute = 0;
	int routeid = 0;
	// int siteid = 0;
	VehicleTypeDto chosenVehicleType = null;
	VehicleDto chosenVehicle = null;
	int employeeCountInRoute = 0;
	ArrayList<Integer> selectedRoutesLandmarks = null;
	StringBuilder selectedEmpSubscribedIDString = null;
	StringBuilder selectedEmployeeIDString = null;
	RoutingDto routingDtoForEmps = null;
	ArrayList<String> employeeInVehicle = new ArrayList<String>();
	int remainingAvaialableSeat = 0;
	ArrayList<VehicleTypeDto> vehicleTypeDtoListObj = null;
	TripDetailsDto tripDetailsDtoObj = new TripDetailsDto();
	TripDetailsChildDto tripDetailsChildDtoObj = new TripDetailsChildDto();
	int needSecurity = 0;
	String isSecurityIn = "NO";
	long preTrip = 0;
	ArrayList<Integer> previousTripsTaken = new ArrayList<Integer>();

	public String routeProcess(RoutingDto routingDto, String overwrite)
			throws SQLException {
		String status = "success";
		boolean empFlag = false;
		try {
			if (overwrite != null && overwrite.equals("yes")) {
				overWriteTrips(routingDto);
			}

			ArrayList<RoutingDto> routingDtoList = getAllroutingDtos(routingDto);

			for (RoutingDto routingDto2 : routingDtoList) {

				if (setEmployeesForTravel(routingDto2)) {
					System.out
							.println(routingDto2.getTravelMode()
									+ "set ALll emps to travel"
									+ routingDto2.getTime());
					empFlag = true;
					while (getBestRouteId(routingDto2)) {
						// System.out.println("got best Route");
						getNodesOfTheSelectedRoute();
						// System.out.println("got noddes of  Route");
						checkSecurity(routingDto2);
						// System.out.println("checked the secuirty");
						selectVehicleType(routingDto.getSiteId());
						// System.out.println("set vehicle type");
						if (allocateEmployee(routingDto2)) {
							// System.out.println("allocted emps");
							storeInDatabase(routingDto2);
							// System.out.println("stored in db");
						}
					}
				}
			}
		} finally {
			closeConnection();
		}
		if (!empFlag) {
			status = "noEmps";
		}
		return status;
	}

	private ArrayList<RoutingDto> getAllroutingDtos(RoutingDto routingDto)
			throws SQLException {
		BackToBackRoutingDaoObj = new BackToBackRoutingDao();
		return BackToBackRoutingDaoObj.getAllroutingDtos(routingDto);
	}

	public int overWriteTrips(RoutingDto routingDto) {
		BackToBackRoutingDao BackToBackRoutingDaoObj1 = new BackToBackRoutingDao();
		int retval = BackToBackRoutingDaoObj1.overWriteTrips(routingDto);
		// BackToBackRoutingDaoObj=null;
		// System.out.println("RetVal"+retval);
		return retval;
	}

	public int checkTripExist(RoutingDto routingDto) {

		// System.out.println("RetVal");
		BackToBackRoutingDao BackToBackRoutingDaoObj1 = new BackToBackRoutingDao();
		int retval = BackToBackRoutingDaoObj1.checkTripExist(routingDto);
		// BackToBackRoutingDaoObj=null;

		return retval;
	}

	public boolean setEmployeesForTravel(RoutingDto routingDto)
			throws SQLException {
		employeeDtoList = BackToBackRoutingDaoObj
				.setEmployeesForTravel(routingDto);
		if (employeeDtoList == null) {
			employeeInVehicle.clear();

			return false;
		} else {
			return true;
		}

	}

	public boolean getBestRouteId(RoutingDto routingDto) {
		// System.out.println("Getting in service Route Id");
		routeid = 0;
		try {
			if (employeeDtoList.size() <= 0) {
				return false;
			}
			selectedEmpSubscribedIDString = null;
			selectedEmployeeIDString = null;
			selectedEmpSubscribedIDString = new StringBuilder("");
			selectedEmployeeIDString = new StringBuilder("");
			RoutingDto routingDtoObj = null;
			for (Iterator it = employeeDtoList.entrySet().iterator(); it
					.hasNext();) {
				java.util.Map.Entry entry = (java.util.Map.Entry) it.next();
				routingDtoObj = (RoutingDto) entry.getValue();
				selectedEmpSubscribedIDString.append(
						routingDtoObj.getSubscriptionId()).append(",");
				selectedEmployeeIDString.append(routingDtoObj.getEmployeeId())
						.append(",");
			}
			selectedEmpSubscribedIDString.delete(
					selectedEmpSubscribedIDString.length() - 1,
					selectedEmpSubscribedIDString.length());
			selectedEmployeeIDString.delete(
					selectedEmployeeIDString.length() - 1,
					selectedEmployeeIDString.length());

			int[] returnArray = BackToBackRoutingDaoObj.getBestRouteId(
					selectedEmpSubscribedIDString, selectedEmployeeIDString,
					routingDto);
			routeid = returnArray[0];
			employeeCountInRoute = returnArray[1];
		} catch (Exception ex) {
			System.out.println("Error" + ex);
		}
		if (routeid == 0) {
			return false;
		} else {
			return true;
		}
	}

	public void getNodesOfTheSelectedRoute() {
		try {
			selectedRoutesLandmarks = BackToBackRoutingDaoObj
					.getLandmarksOfTheSelectedRoute(routeid);
			// System.out.println("landmarks" + selectedRoutesLandmarks);

		} catch (SQLException ex) {
			System.out.println("Error" + ex);
		}
	}

	public void checkSecurity(RoutingDto routingDto) {
		try {
			needSecurity = 0;
			if ((BackToBackRoutingDaoObj.checkSecurity(routingDto)) == 1) {
				if (BackToBackRoutingDaoObj
						.checkFemaleInLast(selectedRoutesLandmarks
								.get(selectedRoutesLandmarks.size() - 1)) >= 1) {
					employeeCountInRoute++;
					needSecurity = 1;
				}
			}
			// System.out.println("empcnt" + employeeCountInRoute);
		} catch (SQLException ex) {
			System.out.println("Error" + ex);
		}
	}

	public static <K, V extends Comparable<? super V>> SortedSet<Map.Entry<K, V>> entriesSortedByValues(
			Map<K, V> map) {
		SortedSet<Map.Entry<K, V>> sortedEntries = new TreeSet<Map.Entry<K, V>>(
				new Comparator<Map.Entry<K, V>>() {

					@Override
					public int compare(Map.Entry<K, V> e1, Map.Entry<K, V> e2) {
						int res = e1.getValue().compareTo(e2.getValue());
						return res != 0 ? res : 1; // Special fix to preserve
													// items with equal values
					}
				});
		sortedEntries.addAll(map.entrySet());
		return sortedEntries;
	}

	private int checkAvaialablePreTrip(RoutingDto routingDto,
			TripDetailsDto tripDetailsDto) throws Exception {
		HashMap<Integer, Float> pretripids = new HashMap<Integer, Float>();
		ArrayList<TripDetailsDto> previousTripDetailsDtos = BackToBackRoutingDaoObj
				.getPreviousTrips(routingDto,
						tripDetailsDto.getSrcDestLandmark());
		int i = 0;
		for (TripDetailsDto previous : previousTripDetailsDtos) {
			if (!previousTripsTaken
					.contains(Integer.parseInt(previous.getId()))) {
				if (previous.getIsSecurity().equals("NO")
						&& tripDetailsDto.getIsSecurity().equals("YES")
						|| employeeInVehicle.size() > previous.getSeat()
						|| (previous.getIsSecurity().equals("YES")
								&& tripDetailsDto.getIsSecurity().equals("NO") && remainingAvaialableSeat < 1)) {

				} else {
					System.out.println("prev"+previous.getDistanceFromPrevioustrip());
					pretripids.put(i, previous.getDistanceFromPrevioustrip());
				}
			}
			i++;
		}
		for (Entry<Integer, Float> entry : entriesSortedByValues(pretripids)) {
			int index = entry.getKey();
			String timeDiff = getTimeDifference(
					previousTripDetailsDtos.get(index)
							.getTimeFromPrevioustrip(), routingDto.getTime(),
					routingDto.getTravellingTime());
			// System.out.println(timeDiff + " 00:30>>> "
			// + timeDiff.compareTo("00:30"));
			// System.out.println(timeDiff + " 03:00>>> "
			// + timeDiff.compareTo("03:00"));
			if (timeDiff.compareTo("00:30") > 0
					&& timeDiff.compareTo("03:00") < 0) {

				float distanceTosrcFromSite = BackToBackRoutingDaoObj
						.getDistancebetweentwoLandmarks(routingDto
								.getSiteLandmark(), Integer
								.parseInt(tripDetailsDto.getSrcDestLandmark()));
				System.out.println(distanceTosrcFromSite
						+ " new "
						+ previousTripDetailsDtos.get(index)
								.getDistanceFromPrevioustrip());
				if (previousTripDetailsDtos.get(index)
						.getDistanceFromPrevioustrip() < distanceTosrcFromSite) {

					tripDetailsDto.setPreviousTripId(previousTripDetailsDtos
							.get(index).getId());
					tripDetailsDto
							.setDistanceFromPrevioustrip(previousTripDetailsDtos
									.get(index).getDistanceFromPrevioustrip());
					previousTripsTaken.add(Integer
							.parseInt(previousTripDetailsDtos.get(index)
									.getId()));
					return 1;
				}
			}
		}
		return 0;

	}

	public static long checkTrip(long tripid, String desttime, int land,
			float tripdist) {

		/*
		 * try { pretripids.clear(); String fq = "SELECT tripdet.*,TIMEDIFF('" +
		 * desttime +
		 * "',tripdet.timeindest) AS tmdiff  FROM tripdet WHERE tripdet.date ='"
		 * + date +
		 * "' and tripdet.log='OUT' HAVING tmdiff between '00:00:00' and '05:00:00' "
		 * ; rs1 = st1.executeQuery(fq); while (rs1.next()) { pretripdist =
		 * rs1.getFloat("dist"); if (rs1.getLong("dest") != land) { trdist =
		 * DatabaseConnection.getDistFromSrcDest(rs1.getInt("dest"), land); if
		 * (trdist < 0) { trdist = 1000; } else { pretripids.put(rs1.getLong(1),
		 * trdist); }
		 * 
		 * } else { pretripids.put(rs1.getLong(1), new Float(0)); } } for
		 * (Entry<Long, Float> entry : entriesSortedByValues(pretripids)) {
		 * 
		 * ctripid = entry.getKey(); cdist = entry.getValue();
		 * System.out.println("Dist"+cdist); rs1 =
		 * st1.executeQuery("select * from tripdet where id=" + ctripid + "");
		 * if (rs1.next()) { rss = sts.executeQuery(
		 * "SELECT traveltime.timeforkm from traveltime where traveltime.starttime<='"
		 * + rs1.getString("timeindest") + "' and traveltime.endtime>'" +
		 * rs1.getString("timeindest") + "'"); if (rss.next()) { timetaken =
		 * (int) ((cdist / rss.getInt(1)) * 60); } String preTripInCurentSrc =
		 * DatabaseConnection.getAddedTime(rs1.getString("timeindest"),
		 * timetaken, date, "OUT"); rs2 = st2.executeQuery("SELECT TIMEDIFF('" +
		 * desttime + "','" + preTripInCurentSrc +
		 * "')BETWEEN '00:10:00' AND '04:30:00'  AS tm,(SELECT count(trip_sheet.empid) FROM trip_sheet WHERE trip_sheet.tripid="
		 * + rs1.getLong(1) +
		 * " and trip_sheet.empid='security') sec,(SELECT count(trip_sheet.empid) FROM trip_sheet WHERE trip_sheet.tripid="
		 * + rs1.getLong(1) + ") totemp"); if (rs2.next()) { if ((rs2.getInt(1)
		 * == 1) && (!takenTripIds.contains(rs1.getLong(1)))) { if ((securityin
		 * && rs2.getInt(2) == 1) || (securityin && rs2.getInt(3) < 6) ||
		 * (!securityin)) { rs3 =
		 * st3.executeQuery("select trip_sheet.dist from trip_sheet where tripid="
		 * + rs1.getLong(1) + " order by dist desc limit 1"); if (rs3.next()) {
		 * float curtripdist = DatabaseConnection.getDistFromSrcDest(land,
		 * sitelandmark); float nd = rs3.getFloat(1); float nd1 = nd + cdist +
		 * tripdist - curtripdist; takenTripIds.add(rs1.getLong(1)); float
		 * empdist = nd + cdist - curtripdist;
		 * System.out.println("tripid"+rs1.getLong(1));
		 * st3.executeUpdate("update tripdet set dist=" + nd1 + ",pretripid=" +
		 * rs1.getLong(1) + ",timeinsrc='" + preTripInCurentSrc + "',src='" +
		 * land + "' where id=" + tripid + " ");
		 * st3.executeUpdate("update tripdet set dist=" + nd + " where id=" +
		 * rs1.getLong(1) + " ");
		 * st3.executeUpdate("update trip_sheet set dist=dist+" + empdist +
		 * " where tripid =" + tripid + ""); return (rs1.getLong(1)); } } } } }
		 * 
		 * }
		 * 
		 * return 0; } catch (Exception e) { System.out.println("ERRRRRRRRR" +
		 * e); return 0; }
		 */

		return 0;
	}

	public void selectVehicleType(int siteid) {
		ArrayList<VehicleDto> vehicleDtoListObj = null;
		// System.out.println("vehicle typ chose starts");
		try {
			vehicleDtoListObj = new VehicleDao().getAllAvailableVehicle();					
			for (VehicleDto vehicleDto : vehicleDtoListObj) {
				// System.out.println("veh TYpes Iterating" +
				// vehicleTypeDto.getId());
				if (vehicleDto.getSit_cap() >= employeeCountInRoute) {
					chosenVehicle = vehicleDto;
					break;
				} else {
					chosenVehicle = vehicleDto;
				}

			}

			remainingAvaialableSeat = chosenVehicle.getSit_cap();
			// System.out.println("vehicle typ chose ends with" +
			// remainingAvaialableSeat);
		} catch (Exception e) {
			System.out.println("ERRRoooo" + e);
		}
	}

	public boolean allocateEmployee(RoutingDto routingDto) {

		if (employeeInVehicle != null) {
			employeeInVehicle.clear();
		}
		boolean flag = false;
		try {
			routingDtoForEmps = null;
			String employeeId = "";
			routingDtoForEmps = new RoutingDto();
			// System.out.println("Satge 1");
			employeesInRoute.clear();
			int lastLandmark = 0;
			for (int i = selectedRoutesLandmarks.size() - 1; i >= 0; i--) {

				if (i == selectedRoutesLandmarks.size() - 1) {
					lastLandmark = selectedRoutesLandmarks.get(i);
				}
				for (Iterator it = employeeDtoList.entrySet().iterator(); it
						.hasNext();) {
					java.util.Map.Entry entry = (java.util.Map.Entry) it.next();
					employeeId = (String) entry.getKey();
					routingDtoForEmps = (RoutingDto) entry.getValue();
					if (routingDtoForEmps.getEmployeeLandmark().equals(
							selectedRoutesLandmarks.get(i).toString())
							&& remainingAvaialableSeat > 0) {

						employeesInRoute.put(employeeId, routingDtoForEmps);

					}
				}
			}
			flag = false;
			isSecurityIn = "NO";
			for (Iterator it = employeesInRoute.entrySet().iterator(); it
					.hasNext();) {
				java.util.Map.Entry entry = (java.util.Map.Entry) it.next();
				employeeId = (String) entry.getKey();
				routingDtoForEmps = (RoutingDto) entry.getValue();
				if (needSecurity == 1
						&& routingDtoForEmps.getEmployeeLandmark().equals(
								"" + lastLandmark)
						&& routingDtoForEmps.getEmployeegender().equals("F")) {
					if (remainingAvaialableSeat > 1) {
						allocatedEmployeeDtoList.put(employeeId,
								routingDtoForEmps);
						employeeInVehicle
								.add(routingDtoForEmps.getEmployeeId());
						flag = true;
						employeeDtoList.remove(employeeId);
						isSecurityIn = "YES";
						remainingAvaialableSeat = remainingAvaialableSeat - 2;

					}
				} else if (remainingAvaialableSeat > 0) {
					allocatedEmployeeDtoList.put(employeeId, routingDtoForEmps);
					employeeInVehicle.add(routingDtoForEmps.getEmployeeId());
					flag = true;
					employeeDtoList.remove(employeeId);
					remainingAvaialableSeat--;
				} else if (remainingAvaialableSeat <= 0) {
					break;
				}
			}
		} catch (Exception e) {
			System.out.println("Erro in allocation" + e);
			try {
				throw (e);
			} catch (Exception ex) {
				// TODO Auto-generated catch block
				ex.printStackTrace();
			}

		}
		return flag;
	}

	public float[] getDistance(RoutingDto routingDto) throws Exception {

		int empCountInvehicle = employeeInVehicle.size();
		int landmarks[] = new int[empCountInvehicle];
		float distance[] = null;
		try {
			for (int i = 0; i < empCountInvehicle; i++) {
				landmarks[i] = Integer.parseInt(allocatedEmployeeDtoList.get(
						employeeInVehicle.get(i)).getEmployeeLandmark());
			}

			distance = BackToBackRoutingDaoObj.getDistances(landmarks,
					routingDto);
		} catch (Exception e) {
			System.out.println("Get distance error" + e);
			throw (e);
		}
		return distance;
	}

	public float[] getTime(RoutingDto routingDto, float[] distance) {
		int empCountInvehicle = employeeInVehicle.size();
		int firstlandmark = 0;
		float time[] = null;
		try {
			// if (routingDto.getTravelMode().equals("IN")) {
			// firstlandmark =
			// Integer.parseInt(allocatedEmployeeDtoList.get(employeeInVehicle.get(empCountInvehicle-1)).getEmployeeLandmark());
			// }

			time = BackToBackRoutingDaoObj.getTime(routingDto, distance);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return time;
	}

	public void storeInDatabase(RoutingDto routingDto) {
		try {

			float distance[] = getDistance(routingDto);
			float time[] = getTime(routingDto, distance);

			tripDetailsDtoObj.setPreviousTripId("0");
			tripDetailsDtoObj.setDistanceFromPrevioustrip((float) 0.0);

			tripDetailsDtoObj.setSiteId("" + routingDto.getSiteId());
			tripDetailsDtoObj.setTrip_date(routingDto.getDate());
			tripDetailsDtoObj.setTrip_time(routingDto.getTime());
			tripDetailsDtoObj.setTrip_log(routingDto.getTravelMode());
			tripDetailsDtoObj.setRouteId("" + routeid);
			tripDetailsDtoObj.setVehicle_type(chosenVehicle.getVehicleTypeId());
			tripDetailsDtoObj.setVehicle(""+chosenVehicle.getId());
			tripDetailsDtoObj.setIsSecurity(isSecurityIn);
			tripDetailsDtoObj.setDistance("" + routingDto.getDistance());
			tripDetailsDtoObj
					.setTravelTime("" + routingDto.getTravellingTime());
			tripDetailsDtoObj.setSrcDestLandmark(allocatedEmployeeDtoList.get(
					employeeInVehicle.get(0)).getEmployeeLandmark());

			if (routingDto.getTravelMode().equals("IN")) {
				int retData = checkAvaialablePreTrip(routingDto,
						tripDetailsDtoObj);

			}

			int insertedID = BackToBackRoutingDaoObj
					.storeInTripMaster(tripDetailsDtoObj);
			int empCountInvehicle = employeeInVehicle.size();
			if (routingDto.getTravelMode().equals("IN")) {
				for (int i = 0; i < empCountInvehicle; i++) {
					tripDetailsChildDtoObj.setTripId("" + insertedID);
					tripDetailsChildDtoObj.setEmployeeId(employeeInVehicle
							.get(i));
					tripDetailsChildDtoObj
							.setLandmarkId(allocatedEmployeeDtoList.get(
									employeeInVehicle.get(i))
									.getEmployeeLandmark());
					tripDetailsChildDtoObj.setRoutedOrder("" + (i + 1));
					tripDetailsChildDtoObj.setDistance(distance[i]);
					tripDetailsChildDtoObj.setTime("" + time[i]);
					BackToBackRoutingDaoObj
							.storeInTripChild(tripDetailsChildDtoObj);
				}
			} else {
				for (int i = empCountInvehicle - 1; i >= 0; i--) {
					tripDetailsChildDtoObj.setTripId("" + insertedID);
					tripDetailsChildDtoObj.setEmployeeId(employeeInVehicle
							.get(i));
					tripDetailsChildDtoObj
							.setLandmarkId(allocatedEmployeeDtoList.get(
									employeeInVehicle.get(i))
									.getEmployeeLandmark());
					tripDetailsChildDtoObj.setRoutedOrder(""
							+ (empCountInvehicle - i));
					tripDetailsChildDtoObj.setDistance(distance[i]);
					tripDetailsChildDtoObj.setTime("" + time[i]);
					BackToBackRoutingDaoObj
							.storeInTripChild(tripDetailsChildDtoObj);
				}
			}
			// System.out.println("emp in veh" + employeeInVehicle);
		} catch (Exception ex) {
			System.out.println("ERROR In Storing service" + ex);
			try {
				throw (ex);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void closeConnection() {
		BackToBackRoutingDaoObj.closeConnection();
	}

	public String getTimeDifference(String time1, String time2, float TravleTime)
			throws ParseException {
		// System.out.println("get time diff" + time1 + " " + time2 + " "
		// + TravleTime);
		SimpleDateFormat format = new SimpleDateFormat("HH:mm");
		Date DateTime1 = format.parse(time1);
		Date DateTime2 = format.parse(time2);
		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(DateTime1);
		Calendar cal2 = Calendar.getInstance();
		cal2.setTime(DateTime2);
		cal2.add(Calendar.MINUTE, -(int) TravleTime);
		cal2.add(Calendar.HOUR_OF_DAY, -cal1.get(Calendar.HOUR_OF_DAY));
		cal2.add(Calendar.MINUTE, -cal1.get(Calendar.MINUTE));
		System.out.println("REDU" + format.format(cal2.getTime()));
		return (format.format(cal2.getTime()));
	}

	public int BackToBackRouting(String date,String site)
	{
		int returnint=0;
		date = OtherFunctions.changeDateFromatToIso(date);
		BackToBackRoutingDaoObj= new BackToBackRoutingDao();
		ArrayList<TripDetailsDto> inList=BackToBackRoutingDaoObj.getINTrips(date, site);
		ArrayList<TripDetailsDto> outList=BackToBackRoutingDaoObj.getOUTTrips(date, site);
		for(TripDetailsDto outdto:outList)
		{
				System.out.println("in first");
			for(TripDetailsDto indto:inList)
			{
				if(checkEscort(indto.getIsSecurity(),outdto.getIsSecurity())&&checkTime(indto.getTrip_date(),outdto.getTrip_date())&&checkDistance(indto.getSrcDestLandmark(),outdto.getSrcDestLandmark()))
				{
					System.out.println("in 2");
					if(checkBackTrip(indto.getId())&&checkVehicleType(outdto.getVehicle_type(), indto.getVehicle_type()))
					{
					int result=BackToBackRoutingDaoObj.UpdatePreviousTrip(outdto.getId(),outdto.getDistance(), indto.getId());
					returnint=result;
					if(result==1)
					{
						System.out.println("in"+outdto.getId()+"out"+indto.getId());
					break;
					}
					}
				}
			}
		}
		return returnint;
	}
	public boolean checkEscort(String insecurity,String outsecurity)
	{
		boolean result=true;
		if(insecurity.equalsIgnoreCase("YES")&&outsecurity.equalsIgnoreCase("NO"))
		{
			result=false;
		}
		else if(outsecurity.equalsIgnoreCase("YES"))
		{
			result=true;
		}
		return result;
	}
	
	public boolean checkTime(String inDate,String outDate)
	{
		boolean result=false;
		    SimpleDateFormat format = new SimpleDateFormat("dd-MM-yy HH:mm:ss");

		    Date d1 = null;
		    Date d2 = null;
		    try {
		        d1 = format.parse(inDate);
		        d2 = format.parse(outDate);
		    } catch (ParseException e) {
		        e.printStackTrace();
		    }
		    System.out.println("date1"+d1+"date2"+d2);
		    long duration  = d1.getTime() - d2.getTime();
		    long diffMinutes=TimeUnit.MILLISECONDS.toMinutes(duration);
		    System.out.println("diff"+diffMinutes);
		    if(diffMinutes>30&&diffMinutes<180)
		    {
		    	result=true;
		    }
		    
		return result;
	}
	public boolean checkDistance(String landark1,String landmark2)
	{
		System.out.println("lan"+landark1+"lan2"+landmark2);
		BackToBackRoutingDaoObj= new BackToBackRoutingDao();
		return BackToBackRoutingDaoObj.checkDistance(landark1, landmark2);
	}
	
	public double getdistance(double lat1, double lng1, double lat2, double lng2) {

	    double earthRadius = 6371; // in miles, change to 6371 for kilometers

	    double dLat = Math.toRadians(lat2-lat1);
	    double dLng = Math.toRadians(lng2-lng1);

	    double sindLat = Math.sin(dLat / 2);
	    double sindLng = Math.sin(dLng / 2);

	    double a = Math.pow(sindLat, 2) + Math.pow(sindLng, 2)
	        * Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2));

	    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));

	    double dist = earthRadius * c;

	    return dist;
	}
	
	public boolean checkBackTrip(String tripid)
	{
		return new BackToBackRoutingDao().checkBackTrip(tripid);
		
	}
	
	public boolean checkVehicleType(String typeid1,String typeid2)
	{
		return new BackToBackRoutingDao().checkVehicleType(typeid1, typeid2);
		
	}
	public ArrayList<BackToBackDto> getAllBackToBackTrips(String site,String date,String todate)
	{
		return new BackToBackRoutingDao().getAllBackToBackTrips(site, date,todate);
	}

}