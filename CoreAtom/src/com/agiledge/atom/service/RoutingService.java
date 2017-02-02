/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.agiledge.atom.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import com.agiledge.atom.commons.OtherFunctions;
import com.agiledge.atom.constants.AuditLogConstants;
import com.agiledge.atom.dao.APLDao;
import com.agiledge.atom.dao.AuditLogDAO;
import com.agiledge.atom.dao.DistanceListDao;
import com.agiledge.atom.dao.RoutingDao;
import com.agiledge.atom.dao.VehicleTypeDao;
import com.agiledge.atom.dto.APLDto;
import com.agiledge.atom.dto.AuditLogDTO;
import com.agiledge.atom.dto.RoutingDto;
import com.agiledge.atom.dto.TripDetailsChildDto;
import com.agiledge.atom.dto.TripDetailsDto;
import com.agiledge.atom.dto.VehicleTypeDto;
import com.agiledge.atom.trip.dto.AplDistanceDto;

/**
 * 
 * @author muhammad
 */
public class RoutingService {

	HashMap<String, RoutingDto> employeeDtoList = null;
	LinkedHashMap<String, RoutingDto> employeesInRoute = new LinkedHashMap<String, RoutingDto>();
	HashMap<String, RoutingDto> allocatedEmployeeDtoList = new HashMap<String, RoutingDto>();
	RoutingDao routingDaoObj = null;
	int selectedRoute = 0;
	int routeid = 0;
	// int siteid = 0;
	VehicleTypeDto chosenVehicleType = null;
	int employeeCountInRoute = 0;
	ArrayList<Integer> selectedRoutesLandmarks = null;
	StringBuilder selectedEmpSubscribedIDString = null;
	StringBuilder selectedEmployeeIDString = null;
	RoutingDto routingDtoForEmps = null;
	ArrayList<String> employeeInVehicle = new ArrayList<String>();
	int remainingAvaialableSeat = 0;
	TripDetailsDto tripDetailsDtoObj = new TripDetailsDto();
	TripDetailsChildDto tripDetailsChildDtoObj = new TripDetailsChildDto();
	int needSecurity = 0;
	String isSecurityIn = "NO";
	public HashSet<String> projects = new HashSet<String>();
	public String routedType = "";
	public String doneBy = "";
	ArrayList<TripDetailsChildDto> tripChildDto=null;
	String siteLandmark=null;
//	private ArrayList<String> errorMessageList = new ArrayList<String>();
	 

	public String routeProcess(RoutingDto routingDto, int siteid,
			String overwrite, String doneBy,
			ArrayList<VehicleTypeDto> vehicleTypeCount) throws Exception {
		String status = "success";
		boolean empFlag = false;
		try {
			
			if (overwrite != null && overwrite.equals("yes")) {
				overWriteTrips(routingDto, siteid);
			}
			
			ArrayList<RoutingDto> routingDtoList = getAllroutingDtos(
					routingDto, siteid);
			 
int shiftIndex=0;

			for (RoutingDto routingDto2 : routingDtoList) {
				int routeIndex=0; 
				if (setEmployeesForTravel(routingDto2, siteid, "o")) {

					empFlag = true;
					routedType = "o";
			
					while (getBestRouteId(routingDto2, siteid)) {
						System.out.println(" got route Id"+new Date());
						getNodesOfTheSelectedRoute();
						System.out.println(" got nodes of route"+new Date());
						checkSecurity(routingDto2, siteid);
						System.out.println(" security checked "+new Date());
						selectVehicleType(siteid, vehicleTypeCount);
						System.out.println(" vehicle type selected "+new Date());
						if (allocateEmployee(routingDto2)) {
							System.out.println(" allocation done"+new Date());
							storeInDatabase(routingDto2, siteid, doneBy);
							System.out.println(" Sotred in DB "+new Date());
							routeIndex ++;
						}
					}
					
				}
				/*
				if (setEmployeesForTravel(routingDto2, siteid, "i")) {
					routedType = "i";
					empFlag = true;
					allcateEmployeeSeperately(routingDto2, siteid, doneBy,
							vehicleTypeCount);
				}
				if (setEmployeesForTravel(routingDto2, siteid, "p")) {
					routedType = "p";
					projects.clear();
					HashMap<String, RoutingDto> employeeDtoInProject = new HashMap<String, RoutingDto>();
					HashMap<String, RoutingDto> tempEmployeeDtoList = new HashMap<String, RoutingDto>(employeeDtoList);
					String curEmployee = "";
					RoutingDto curDto = null;
					Entry entry = null;
					for (Iterator it = employeeDtoList.entrySet().iterator(); it
							.hasNext();) {
						entry = (Entry) it.next();
						curDto = (RoutingDto) entry.getValue();
						projects.add(curDto.getProject());
					}
					for (String curProject : projects) {
						for (Iterator it = tempEmployeeDtoList.entrySet()
								.iterator(); it.hasNext();) {
							entry = (Entry) it.next();
							curEmployee = (String) entry.getKey();
							curDto = (RoutingDto) entry.getValue();
							if (curDto.getProject().equals(curProject)) {
								employeeDtoInProject.put(curEmployee, curDto);
							}
						}
						employeeDtoList = employeeDtoInProject;
						empFlag = true;
						while (getBestRouteId(routingDto2, siteid)) {
							getNodesOfTheSelectedRoute();
							checkSecurity(routingDto2, siteid);
							selectVehicleType(siteid, vehicleTypeCount);
							if (allocateEmployee(routingDto2)) {
								storeInDatabase(routingDto2, siteid, doneBy);
							}

						}
						
						
					}
				}
				
				*/
				shiftIndex++;
			}
			
		} finally {
			closeConnection();
		}
		if (!empFlag) {
			status = "noEmps";
		}
		return status;
	}

	private ArrayList<RoutingDto> getAllroutingDtos(RoutingDto routingDto,
			int siteid) throws SQLException {
		routingDaoObj = new RoutingDao();		
		return routingDaoObj.getAllroutingDtos(routingDto, siteid);
	}

	/*
	 * 
	 * Commented beacuse no need to reduce date public String
	 * changeDateForNightShift(RoutingDto dto) throws SQLException {
	 * routingDaoObj = new RoutingDao();
	 * routingDaoObj.changeDateForNightShift(dto); // TODO Auto-generated method
	 * stub return null; }
	 */
	public int overWriteTrips(RoutingDto routingDto, int siteid) {
		RoutingDao routingDaoObj1 = new RoutingDao();
		int retval = routingDaoObj1.overWriteTrips(routingDto, siteid);
		// routingDaoObj=null;
		// System.out.println("RetVal"+retval);
		return retval;
	}

	public int checkTripExist(RoutingDto routingDto, int siteid) {

		// System.out.println("RetVal");
		RoutingDao routingDaoObj1 = new RoutingDao();
		int retval = routingDaoObj1.checkTripExist(routingDto, siteid);
		// routingDaoObj=null;

		return retval;
	}

	public boolean setEmployeesForTravel(RoutingDto routingDto, int siteid,
			String routingType) throws SQLException {
	//	System.out.println("inside setEmployeesForTravel");
		employeeDtoList = routingDaoObj.setEmployeesForTravel(routingDto,
				siteid, routingType);
		siteLandmark=Integer.toString(routingDaoObj.siteLandmarkId);
		if (employeeDtoList == null) {
			employeeInVehicle.clear();

			return false;
		} else {
			return true;
		}

	}

	public boolean getBestRouteId(RoutingDto routingDto, int siteid) {
	//	 System.out.println("Getting in service Route Id");
	//	 System.out.println("Employees remaining : " + employeeDtoList.size());
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

			int[] returnArray = routingDaoObj.getBestRouteId(
					selectedEmpSubscribedIDString, selectedEmployeeIDString,
					siteid, routingDto);
			routeid = returnArray[0];
			// System.out.println(" Route id : " + routeid);
			employeeCountInRoute = returnArray[1];
			// System.out.println("selectedEmpSubscribedIDString" +
			// selectedEmpSubscribedIDString);
			// System.out.println("selectedEmployeeIDString" +
			// selectedEmployeeIDString);
			// System.out.println("route Id:" + routeid);
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
			selectedRoutesLandmarks = routingDaoObj
					.getLandmarksOfTheSelectedRoute(routeid);
			ordertheLandmarks(selectedRoutesLandmarks);
			// System.out.println("landmarks" + selectedRoutesLandmarks);

		} catch (SQLException ex) {
			System.out.println("Error" + ex);
		}
	}

	public void checkSecurity(RoutingDto routingDto, int siteid) {
		try {
			needSecurity = 0;
			if ((routingDaoObj.checkSecurity(routingDto, siteid)) == 1) {
				if (routingDaoObj.checkFemaleInLast(selectedRoutesLandmarks
						.get(selectedRoutesLandmarks.size() - 1)) >= 1) {
					employeeCountInRoute++;
					needSecurity = 1;
				}
			}
			// System.out.println("empcnt" + employeeCountInRoute);
		} catch (Exception ex) {
			System.out.println("Error" + ex);
		}
	}

	public void selectVehicleType(int siteid,
			ArrayList<VehicleTypeDto> vehicleTypeCount) throws Exception {
		// System.out.println("vehicle typ chose starts");
		try {
			new VehicleTypeDao().getAllVehicleTypeByToAppend(vehicleTypeCount,routingDaoObj.con);
			int availVehicle = -1;
			/*System.out.println(" Employees in route : " + employeeCountInRoute);*/
			for (int i = 0; i < vehicleTypeCount.size(); i++) {
/*System.out.println("vhicle " + vehicleTypeCount.get(i).getId() + " sitting : " + vehicleTypeCount.get(i).getSittingCopacity());*/


				if (vehicleTypeCount.get(i).getCount() > 0) {
					availVehicle = i;
				}
				if (vehicleTypeCount.get(i).getSittingCopacity() >= employeeCountInRoute
						&& vehicleTypeCount.get(i).getCount() > 0) {
					// System.out.println("inside full alloc");
					vehicleTypeCount.get(i).setCount(
							vehicleTypeCount.get(i).getCount() - 1);
					chosenVehicleType = vehicleTypeCount.get(i);
					remainingAvaialableSeat = chosenVehicleType
							.getSittingCopacity();
					// System.out.println("select vehicle Type " + vehicleTypeCount.get(i).getId());
					
					return;
				}

				if (i == vehicleTypeCount.size() - 1) {
					// System.out.println("last ");
					if (availVehicle != -1) {
						// System.out.println("last but seat available : " + (vehicleTypeCount.get(availVehicle).getSittingCopacity() - 1) );
						vehicleTypeCount.get(availVehicle).setCount(vehicleTypeCount.get(availVehicle).getCount() - 1);
						chosenVehicleType = vehicleTypeCount.get(availVehicle);
						remainingAvaialableSeat = chosenVehicleType.getSittingCopacity();
						
						// System.out.println("select vehicle Type " + vehicleTypeCount.get(i).getId());
						return;
					} else {
						for (int j = 0; j < vehicleTypeCount.size(); j++) {
							/*System.out.println("last vhicle " + j + " . " + vehicleTypeCount.get(i).getId() + " sitting : " + vehicleTypeCount.get(i).getSittingCopacity());*/
							if (vehicleTypeCount.get(j).getSittingCopacity() >= employeeCountInRoute) {
								// System.out.println("last but full alloca again");
								vehicleTypeCount.get(j).setCount(
										vehicleTypeCount.get(j).getCount() - 1);
								chosenVehicleType = vehicleTypeCount.get(j);
								remainingAvaialableSeat = chosenVehicleType
										.getSittingCopacity();
								// System.out.println("select vehicle Type " + vehicleTypeCount.get(i).getId());
								return;
							}
							if (j == vehicleTypeCount.size() - 1) {
								 
								// System.out.println("again last but seat available : " + (vehicleTypeCount.get(j).getSittingCopacity() - 1) );
								vehicleTypeCount.get(j).setCount(vehicleTypeCount.get(j).getCount() - 1);
								chosenVehicleType = vehicleTypeCount.get(j);
								remainingAvaialableSeat = chosenVehicleType
										.getSittingCopacity();
								// System.out.println("select vehicle Type " + vehicleTypeCount.get(i).getId());
								return;

							}
						}
					}

				}
			}

		} catch (Exception e) {
			System.out.println("ERRRoooo" + e);
			throw (e);
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
				// System.out.println("selected landmark :" + selectedRoutesLandmarks.get(i).toString());
				if (i == selectedRoutesLandmarks.size() - 1) {
					lastLandmark = selectedRoutesLandmarks.get(i);
					
				}
				int iteratorCount =0;
				for (Iterator it = employeeDtoList.entrySet().iterator(); it
						.hasNext();) {
					java.util.Map.Entry entry = (java.util.Map.Entry) it.next();
					employeeId = (String) entry.getKey();
					routingDtoForEmps = (RoutingDto) entry.getValue();
					if (routingDtoForEmps.getEmployeeLandmark().equals(
							selectedRoutesLandmarks.get(i).toString())
							&& remainingAvaialableSeat > 0) {

						employeesInRoute.put(employeeId, routingDtoForEmps);
						// System.out.println("emp :"+ employeeId);

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

			distance = routingDaoObj.getDistances(landmarks, routingDto);
			
			if(routingDaoObj.isDistanceInDb()==false) {
				/*if(routingDaoObj.testCase) 
					routingDaoObj.getErrorMessageList().add("______source dest checking in apl (src,dest) list ________");
				
				System.out.println("source dest checking in apl (src,dest) list ");
				for(AplDistanceDto dto: routingDaoObj.getAplDistanceList()) {
					System.out.println(dto.getSourceId() + " | "+ dto.getTargetId());
					if(routingDaoObj.testCase) 
						System.out.println(dto.getSourceId() + " | "+ dto.getTargetId());
				}
				System.out.println("________source dest checking in landmark dist list________ ");
				if(routingDaoObj.testCase) 
					routingDaoObj.getErrorMessageList().add("______source dest checking in landmark dist list________");
				
				for(AplDistanceDto dto: routingDaoObj.getLandmarkDistanceList() ) {
					System.out.println(dto.getSourceId() + " , distance :"+ dto.getDistance());
					if(routingDaoObj.testCase) 
						routingDaoObj.getErrorMessageList().add(dto.getSourceId() + " , distance :"+ dto.getDistance());
					
				}*/
				/*System.out.println("landmark s from landmarks");
				for(int landmark: landmarks) {
					System.out.println("landmarks :"+ landmark+"");
				}
				
				System.out.println("distances of middle landmarks");
				if(routingDaoObj.testCase)
					routingDaoObj.getErrorMessageList().add("___distances of middle landmarks___");
				for(float dist: distance) {
					System.out.println("distance :"+ dist+"");
					if(routingDaoObj.testCase) 
						routingDaoObj.getErrorMessageList().add("distance :"+ dist+"");
				}*/
				/*getErrorMessageList().addAll(routingDaoObj.getErrorMessageList());*/
				float[] distance1 = getMapDistances(landmarks, routingDto, routingDaoObj.getAplDistanceList(), routingDaoObj.getLandmarkDistanceList());
				if(distance1 == null ) {
					// System.out.println("Still Taking db distance only");
				} else {
					// System.out.println(" Taking map distanceonly");
					/*for(AplDistanceDto dto: routingDaoObj.getAplDistanceList()) {
						System.out.println(dto.getSourceId() + " | "+ dto.getTargetId() + " distance : "+ dto.getDistance());
					}*/
					distance = distance1;
					DistanceListDao distanceListDao= new DistanceListDao();
					distanceListDao. insertDistanceFromDistanceList(routingDaoObj.getAplDistanceList());
					//trackList.addAll()
//					getErrorMessageList().addAll( distanceListDao.getErrorMessageList());
				}
			
				/*if(routingDto.getTravelMode().equalsIgnoreCase("IN")  && distance[0]!=0) {
					getErrorMessageList() .add("IN : before loop start in storeInDatabase Error routeid :"+routeid);
				}*/
			}
		} catch (Exception e) {
			System.out.println("Get distance error" + e);
 
			throw (e);
		}
		return distance;
	}
	
	@SuppressWarnings("deprecation")
	public float[] getMapDistances(int landmarks222[], RoutingDto routingDto, ArrayList<AplDistanceDto> aplDistanceList, ArrayList<AplDistanceDto> landmarkDistanceList) {
		
		float distance[] = new float[landmarks222.length];
		float totalDistance = 0.0f;
			
		HashMap<String, APLDto> aplMap = new HashMap<String, APLDto>();
		int landmarkDupe[] = new int[landmarkDistanceList.size()];
		try {
			if(landmarkDistanceList.size()<11)
			{
		//aplList = getaplist from db
			int ctx=0;
		 	for(AplDistanceDto addto : landmarkDistanceList ) {
		 		landmarkDupe[ctx] =Integer.parseInt( addto.getSourceId());
		 		//System.out.println(".. "+ landmarkDupe[ctx]);
		 		ctx++;
		 	}
		 	 
		aplMap = new APLDao().getAllAPL(landmarkDupe);
		//System.out.println("Okey...");
		int totalPoints = landmarkDistanceList.size();
		//System.out.println("Apl Distance Size :" + aplDistanceList.size());
		//System.out.println("Map size : "+ aplMap.size());
		  
		String sourceLatLng= aplMap.get(landmarkDistanceList.get(0).getSourceId()).getLattitude() + ","+ aplMap.get(landmarkDistanceList.get(0).getSourceId()).getLongitude();
		String destinationLatLng=aplMap.get(landmarkDistanceList.get(totalPoints-1).getSourceId()).getLattitude() + ","+ aplMap.get(landmarkDistanceList.get(totalPoints-1).getSourceId()).getLongitude();
		 //System.out.println("source : " + aplMap.get(landmarkDistanceList.get(0).getSourceId()).getLandMark());
		 
		String wayPointLatLngs = "";
		
		for(int i = 1; i< landmarkDistanceList.size()-1; i++) {
			/*System.out.println("i : "+ i);
			System.out.println("source : " + aplMap.get(landmarkDistanceList.get(i).getSourceId()).getLandMark());*/
			if(i!=1&& i!=landmarkDistanceList.size()-1) {
				wayPointLatLngs+=URLEncoder.encode("|");
			}
			wayPointLatLngs+=  aplMap.get(landmarkDistanceList.get(i).getSourceId()).getLattitude() + ","+ aplMap.get(landmarkDistanceList.get(i).getSourceId()).getLongitude();
		}
		
		if(wayPointLatLngs.trim().equals("")==false) {
			wayPointLatLngs="&waypoints="+wayPointLatLngs;
		}
		/*System.out.println("source : " + aplMap.get(landmarkDistanceList.get(totalPoints-1).getSourceId()).getLandMark());
		System.out.println(wayPointLatLngs);*/
		/* get distances from map api */
		DefaultHttpClient client1 = new DefaultHttpClient();					
	     
		String url="https://maps.googleapis.com/maps/api/directions/json?origin="+sourceLatLng+"&destination="+destinationLatLng+wayPointLatLngs;
		String keyedURL=OtherFunctions.encryptTheMapKey(url);
		HttpGet request = new HttpGet(keyedURL);
	    HttpResponse response1 = 
	    		 client1.execute(request);
	      
	     BufferedReader rd = new BufferedReader (new InputStreamReader(response1.getEntity().getContent()));
	     String line = "";
	     String nLine="";
	     /*System.out.println(response1.getStatusLine());*/
	     while ((line = rd.readLine()) != null) {
	    	 nLine+=line;
	        }
	     //System.out.println(nLine);
	     JSONObject obj = new JSONObject(nLine);
	 /*    		 routes : [0].legs[0].distance.value = meter
	    		 routes : [0].legs[0].duration.value= 1611 second
	    		 routes : [0].legs[0].start_location.lat= 12.9963345
	    		 routes : [0].legs[0].start_location.lng= 77.5646637
	    		 routes : [0].legs[0].end_location.lat= 12.9201671
	    		 routes : [0].legs[0].end_location.lng= 77.5646637
	 */      
	   
	   
	  int legs = ((JSONObject) obj.getJSONArray("routes").get(0)).getJSONArray("legs").length();
	  // System.out.println("Total Legs :"+ legs);
	  for(int i = 0 ; i < legs ; i ++) {
		  JSONObject leg = ((JSONObject) obj.getJSONArray("routes").get(0)).getJSONArray("legs").getJSONObject(i);
		  /*System.out.println( "Distance : " + leg.getJSONObject("distance").getDouble("value"));
		  System.out.println( "Duration : " + leg.getJSONObject("duration").getDouble("value"));
		  System.out.print( "Start Location lat/lng : " + leg.getJSONObject("start_location").getDouble("lat"));
		  System.out.println( "," + leg.getJSONObject("start_location").getDouble("lng"));
		  System.out.print( "End Location lat/lng : " + leg.getJSONObject("end_location").getDouble("lat"));
		  System.out.println( "," + leg.getJSONObject("end_location").getDouble("lng"));*/
		  double distanceM =leg.getJSONObject("distance").getDouble("value");
		  distanceM = distanceM / 1000;
		   /*System.out.println(landmarkDistanceList.get(i).getSourceId());*/
		  /*System.out.println("Disttance (" + i + ") : " + distanceM);*/
		  /*System.out.println();*/
		/* get distance from map api ends */
		  
		  routingDaoObj.getLandmarkDistanceList().get(i).setDistance(distanceM);
		   /*System.out.println(routingDaoObj.getLandmarkDistanceList().get(i).getSourceId() +  " > " +routingDaoObj.getLandmarkDistanceList().get(i).getTargetId());*/
		  totalDistance +=distanceM;
		   
			  routingDaoObj.getAplDistanceList().get(i).setDistance(distanceM);
			 
		   
		  if(routingDto.getTravelMode().equalsIgnoreCase("IN")) {
			  if(i==0) {
				  distance[i] = 0;
			  } else if(i<legs-1 ) {
				  distance[i] = (float) distanceM;
			  }
		  } else if(routingDto.getTravelMode().equalsIgnoreCase("OUT")) {
			  
			  if(i<legs) {
			/*	  System.out.println(" [] :" + ((legs-1)-i) );*/
				  distance[(legs-1)-i] = (float) distanceM;
			  }
		  }
		   
		  
		  }
		}
	  /*System.out.println("Total distance : "+ totalDistance);*/
	  routingDto.setDistance(totalDistance);
	   
		}catch(Exception e) {
			System.out.println("Error in getMapDistance : "+ e);
			distance = null;
		}
		
		return distance;
	}

	public float[] getTime(RoutingDto routingDto, float[] distance) {
		int empCountInvehicle = employeeInVehicle.size();
		int firstlandmark = 0;
		float time[] = null;
		try {

			time = routingDaoObj.getTime(routingDto, distance);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return time;
	}

	public void storeInDatabase(RoutingDto routingDto, int siteid, String doneBy) {
		try {
			// System.out.println(" >>>>   Distance taking         ");
			float distance[] = getDistance(routingDto);
			float time[] = getTime(routingDto, distance);
			int autoincNumber = 0, changedBy = 0;
			tripDetailsDtoObj.setSiteId("" + siteid);
			tripDetailsDtoObj.setTrip_date(routingDto.getDate());
			tripDetailsDtoObj.setTrip_time(routingDto.getTime());
			tripDetailsDtoObj.setTrip_log(routingDto.getTravelMode());
			tripDetailsDtoObj.setRouteId("" + routeid);
			tripDetailsDtoObj.setVehicle_type("" + chosenVehicleType.getId());
			tripDetailsDtoObj.setIsSecurity(isSecurityIn);
			tripDetailsDtoObj.setRoutingType(routedType);
			tripDetailsDtoObj.setDistance("" + routingDto.getDistance());
			tripDetailsDtoObj
					.setTravelTime("" + routingDto.getTravellingTime());
			
			int insertedID = routingDaoObj.storeInTripMaster(tripDetailsDtoObj);
			tripChildDto=null;
			tripChildDto=new ArrayList<TripDetailsChildDto>();
			if (insertedID != 0) {
				changedBy = Integer.parseInt(doneBy);
				autoincNumber = insertedID;
				auditLogEntryforinserttripsheet(autoincNumber,
						AuditLogConstants.TRIPSHEET_MODULE, changedBy,
						AuditLogConstants.WORKFLOW_STATE_EMPTY,
						AuditLogConstants.WORKFLOW_STATE_TRIPSHEET_GENERATED,
						AuditLogConstants.AUDIT_LOG_CREATE_ACTION);
			}
			int empCountInvehicle = employeeInVehicle.size();
			if (routingDto.getTravelMode().equals("IN")) {
				for (int i = 0; i < empCountInvehicle; i++) {
					tripDetailsChildDtoObj=new TripDetailsChildDto();
					// System.out.println(" inserted id " + insertedID);
					tripDetailsChildDtoObj.setTripId("" + insertedID);
					tripDetailsChildDtoObj.setEmployeeId(employeeInVehicle
							.get(i));
				//	System.out.println(" employees landmark  " + allocatedEmployeeDtoList.get(employeeInVehicle.get(i))
				//			.getEmployeeLandmark());
					tripDetailsChildDtoObj
							.setLandmarkId(allocatedEmployeeDtoList.get(
									employeeInVehicle.get(i))
									.getEmployeeLandmark());
				//	System.out.println(" employees in vheicle  " +  
				//			employeeInVehicle.size() );
					
				//	System.out.println("employee id :"+ employeeInVehicle
				//			.get(i));
					tripDetailsChildDtoObj
							.setScheduleId(allocatedEmployeeDtoList.get(
									employeeInVehicle.get(i)).getScheduleId());
				//	System.out.println(" employees scheudleid  " +allocatedEmployeeDtoList.get(
				//			employeeInVehicle.get(i)).getScheduleId());
					
					tripDetailsChildDtoObj.setRoutedOrder("" + (i + 1));
				//	System.out.println(" Route order : "+ (i+1)+"");
					
					tripDetailsChildDtoObj.setDistance(distance[i]);
				//	System.out.println(" Distance"+ (i+1)+" : " + distance[i]);
					 
					tripDetailsChildDtoObj.setTime("" + time[i]);
				//	System.out.println(time[i]);
					tripChildDto.add(tripDetailsChildDtoObj);
				//	System.out.println("*****");
				}
			} else {
				for (int i = empCountInvehicle - 1; i >= 0; i--) {
					
					tripDetailsChildDtoObj=new TripDetailsChildDto();
					tripDetailsChildDtoObj.setTripId("" + insertedID);
					tripDetailsChildDtoObj.setEmployeeId(employeeInVehicle
							.get(i));
					tripDetailsChildDtoObj
							.setLandmarkId(allocatedEmployeeDtoList.get(
									employeeInVehicle.get(i))
									.getEmployeeLandmark());
					tripDetailsChildDtoObj
							.setScheduleId(allocatedEmployeeDtoList.get(
									employeeInVehicle.get(i)).getScheduleId());
					tripDetailsChildDtoObj
					.setTransportType(allocatedEmployeeDtoList.get(
							employeeInVehicle.get(i)).getTransportType());

					tripDetailsChildDtoObj.setRoutedOrder(""
							+ (empCountInvehicle - i));
					tripDetailsChildDtoObj.setDistance(distance[i]);
					tripDetailsChildDtoObj.setTime("" + time[i]);
					tripChildDto.add(tripDetailsChildDtoObj);
					
					
				}
			}
			routingDaoObj.storeInTripChild(tripChildDto);			
			
			// System.out.println("emp in veh" + employeeInVehicle);
		} catch (Exception ex) {
			System.out.println("ERROR In Storing" + ex);
			try {
				throw (ex);
			} catch (Exception e) {
				// TODO Auto-generated catch block
			//	e.printStackTrace();
				System.out.println(" Error : " + ex);
			}
		}

	}
	

	

	private void auditLogEntryforinserttripsheet(int autoincNumber,
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

	private void allcateEmployeeSeperately(RoutingDto routingDto, int siteid,
			String doneBy, ArrayList<VehicleTypeDto> vehicleTypeCount) {
		try {
			// System.out.println("In seperate employees     :"+employeeDtoList.size());
			routingDtoForEmps = null;
			String employeeId = "";
			routingDtoForEmps = new RoutingDto();
			needSecurity = routingDaoObj.checkSecurity(routingDto, siteid);
			employeeCountInRoute = 1;
			selectVehicleType(siteid, vehicleTypeCount);
			for (Iterator it = employeeDtoList.entrySet().iterator(); it
					.hasNext();) {
				java.util.Map.Entry entry = (java.util.Map.Entry) it.next();
				employeeId = (String) entry.getKey();
				routingDtoForEmps = (RoutingDto) entry.getValue();
				routeid = routingDaoObj.getRouteId(routingDtoForEmps
						.getEmployeeLandmark());
				isSecurityIn = "NO";
				if (needSecurity == 1
						&& routingDtoForEmps.getEmployeegender()
								.equalsIgnoreCase("F")) {
					isSecurityIn = "YES";
				}
				employeeInVehicle.clear();
				allocatedEmployeeDtoList.put(employeeId, routingDtoForEmps);
				employeeInVehicle.add(routingDtoForEmps.getEmployeeId());
				employeeDtoList.remove(employeeId);
				storeInDatabase(routingDto, siteid, doneBy);
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

	}

	public void closeConnection() {
		routingDaoObj.closeConnection();
		
	}
		

	private void ordertheLandmarks(ArrayList<Integer> landmarks) {
		Map<Integer, Float> LandmarkDist = new HashMap<Integer, Float>();
		DistanceListDao distanceListDao = null;
		distanceListDao = new DistanceListDao();
		// System.out.println("Landmarksssssssssssssssssssssssssssss"+landmarks.size());
		for (int i = 0; i < landmarks.size(); i++) {
			if (i == 0) 
				LandmarkDist.put(routingDaoObj.siteLandmarkId, (float) 0.0);
							
				float distt = distanceListDao.getDistance(siteLandmark,landmarks.get(i).toString(),					
						routingDaoObj.con);
				LandmarkDist.put(landmarks.get(i), distt);
				// System.out.println(distt);			
		}
		// System.out.println("LandmarAaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"+LandmarkDist.size());
		LandmarkDist=sortTheMap(LandmarkDist);		
	}

	public <K, V extends Comparable<? super V>> Map<K, V> sortTheMap( Map<K, V> map )
				{
		// System.out.println("in sort");
			List<Map.Entry<K, V>> list = new LinkedList<>( map.entrySet() );
			Collections.sort( list, new Comparator<Map.Entry<K, V>>()
    {
        @Override
        public int compare( Map.Entry<K, V> o1, Map.Entry<K, V> o2 )
        {
            return (o1.getValue()).compareTo( o2.getValue() );
        }
    } );

    Map<K, V> result = new LinkedHashMap<>();
    selectedRoutesLandmarks=null;
    selectedRoutesLandmarks=new ArrayList<Integer>();
    // System.out.println(list.size());
    for (Map.Entry<K, V> entry : list)
    {
        result.put( entry.getKey(), entry.getValue() );
        
        if(!entry.getKey().equals(siteLandmark))        	
        selectedRoutesLandmarks.add((Integer) entry.getKey());
    }
    // System.out.println(" 1"+selectedRoutesLandmarks.get(0));
    
    return result;
}



/*	public ArrayList<String> getErrorMessageList() {
		return errorMessageList;
	}

	public void setErrorMessageList(ArrayList<String> errorMessageList) {
		this.errorMessageList = errorMessageList;
	}
*/
}