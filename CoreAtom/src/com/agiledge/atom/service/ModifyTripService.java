package com.agiledge.atom.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import com.agiledge.atom.commons.OtherFunctions;
import com.agiledge.atom.dao.APLDao;
import com.agiledge.atom.dao.DistanceListDao;
import com.agiledge.atom.dao.ModifyTripDao;
import com.agiledge.atom.dto.APLDto;
import com.agiledge.atom.dto.ModifyTripDto;
import com.agiledge.atom.dto.RoutingDto;
import com.agiledge.atom.trip.dto.AplDistanceDto;

public class ModifyTripService {

	ArrayList<AplDistanceDto> aplDistanceList = new ArrayList<AplDistanceDto>();
	ArrayList<AplDistanceDto> landmarkDistanceList = new ArrayList<AplDistanceDto>();

	public int modifyTrip(
			LinkedHashMap<String, ArrayList<ModifyTripDto>> modifiedList,
			String[] tripIds, String doneBy, String tripMode, String siteId,
			String tripTime, String tripDate) {
		float[] distance = null;
		float time[] = null;
		ModifyTripDao modifyDaoObj = new ModifyTripDao();
		modifyDaoObj.getSitelandmark(siteId);
		modifyDaoObj.keepOrginal(tripIds);
		int retVal = 0;
		try {
			for(Iterator it=modifiedList.entrySet().iterator();it.hasNext();)
			{
			Entry entry=(Entry) it.next();
			String tripId=(String) entry.getKey();
			ArrayList<ModifyTripDto> emplist=(ArrayList<ModifyTripDto>) entry.getValue();					
						distance = getDistance(modifyDaoObj, siteId, tripMode,emplist);
						time = getTime(modifyDaoObj, tripMode, distance,tripTime);
						System.out.println("BEFORE MODIFYDAO CALLL");
						
						retVal += modifyDaoObj.modifyTrip(emplist,tripId, doneBy, tripMode, siteId, distance, time,
								tripDate, tripTime);
								
						System.out.println("AFTER MODIFYDAO CALLL");																	
				
			}
		} catch (Exception e) {
			System.out.println("Errror" + e);
		}
		return retVal;

	}

	public int addNewtrip(ArrayList<ModifyTripDto> addList, String doneBy,
			String tripMode, String siteId, String tripTime, String tripDate) {
		ModifyTripDao modifyDaoObj = new ModifyTripDao();

		 int retVal=new ModifyTripDao().addToRemovedtrip(addList, doneBy, tripMode,siteId,tripDate,tripTime);
		return retVal;
	}

	public float[] getDistance(ModifyTripDao modifyDaoObj, String siteId,
			String inOut, ArrayList<ModifyTripDto> modifiedList) {
	
		float distance[] = null;
//System.out.println("In Get Distance");

		try {
			int empCountInvehicle = modifiedList.size();
			int landmarks[] = new int[empCountInvehicle];
			for (int i = 0; i < empCountInvehicle; i++) {
				landmarks[i] = Integer.parseInt(modifiedList.get(i)
						.getLandmarkId());
			}

			distance = modifyDaoObj.getDistances(landmarks, inOut);

			if (modifyDaoObj.isDistanceInDb() == false) {
				float[] distance1 = getMapDistances(modifyDaoObj, landmarks,
						inOut, modifyDaoObj.getAplDistanceList(),
						modifyDaoObj.getLandmarkDistanceList());
				if (distance1 == null) {
					System.out.println("Still Taking db distance only");
				} else {
					System.out.println(" Taking map distanceonly");
					distance = distance1;
					DistanceListDao distanceListDao = new DistanceListDao();
					distanceListDao.insertDistanceFromDistanceList(modifyDaoObj
							.getAplDistanceList());
				}
			}
		} catch (Exception e) {
			System.out.println("Get distance error" + e);
		}
		System.out.println();
		return distance;
	}

	@SuppressWarnings("deprecation")
	public float[] getMapDistances(ModifyTripDao modifyDaoObj,
			int landmarks222[], String inOut,
			ArrayList<AplDistanceDto> aplDistanceList,
			ArrayList<AplDistanceDto> landmarkDistanceList) {

		float distance[] = new float[landmarks222.length];
		float totalDistance = 0.0f;
		HashMap<String, APLDto> aplMap = new HashMap<String, APLDto>();
		int landmarkDupe[] = new int[landmarkDistanceList.size()];
		try {
			// aplList = getaplist from db
			int ctx = 0;
			for (AplDistanceDto addto : landmarkDistanceList) {
				landmarkDupe[ctx] = Integer.parseInt(addto.getSourceId());
				// System.out.println(".. "+ landmarkDupe[ctx]);
				ctx++;
			}

			aplMap = new APLDao().getAllAPL(landmarkDupe);
			System.out.println("Okey...");
			int totalPoints = landmarkDistanceList.size();
			System.out.println("Apl Distance Size :" + aplDistanceList.size());
			System.out.println("Map size : " + aplMap.size());

			String sourceLatLng = aplMap.get(
					landmarkDistanceList.get(0).getSourceId()).getLattitude()
					+ ","
					+ aplMap.get(landmarkDistanceList.get(0).getSourceId())
							.getLongitude();
			System.out.println(sourceLatLng);
			String destinationLatLng = aplMap.get(
					landmarkDistanceList.get(totalPoints - 1).getSourceId())
					.getLattitude()
					+ ","
					+ aplMap.get(
							landmarkDistanceList.get(totalPoints - 1)
									.getSourceId()).getLongitude();
			System.out.println(destinationLatLng);
			// System.out.println("source : " +
			// aplMap.get(landmarkDistanceList.get(0).getSourceId()).getLandMark());

			String wayPointLatLngs = "";

			for (int i = 1; i < landmarkDistanceList.size() - 1; i++) {
				/*
				 * System.out.println("i : "+ i); System.out.println("source : "
				 * +
				 * aplMap.get(landmarkDistanceList.get(i).getSourceId()).getLandMark
				 * ());
				 */
				if (i != 1 && i != landmarkDistanceList.size() - 1) {
					wayPointLatLngs += URLEncoder.encode("|");
				}
				wayPointLatLngs += aplMap.get(
						landmarkDistanceList.get(i).getSourceId())
						.getLattitude()
						+ ","
						+ aplMap.get(landmarkDistanceList.get(i).getSourceId())
								.getLongitude();
			}

			if (wayPointLatLngs.trim().equals("") == false) {
				wayPointLatLngs = "&waypoints=" + wayPointLatLngs;
			}
			/*
			 * System.out.println("source : " +
			 * aplMap.get(landmarkDistanceList.get
			 * (totalPoints-1).getSourceId()).getLandMark());
			 * System.out.println(wayPointLatLngs);
			 */
			/* get distances from map api */
			DefaultHttpClient client1 = new DefaultHttpClient();

			String url = "http://maps.googleapis.com/maps/api/directions/json?origin="
					+ sourceLatLng
					+ "&destination="
					+ destinationLatLng
					+ wayPointLatLngs;
			String keyedURL=OtherFunctions.encryptTheMapKey(url);
			System.out.println(keyedURL);
			HttpGet request = new HttpGet(keyedURL);
			HttpResponse response1 = client1.execute(request);

			BufferedReader rd = new BufferedReader(new InputStreamReader(
					response1.getEntity().getContent()));
			String line = "";
			String nLine = "";
			/* System.out.println(response1.getStatusLine()); */
			while ((line = rd.readLine()) != null) {
				nLine += line;
			}
		//	System.out.println(nLine);
			JSONObject obj = new JSONObject(nLine);

			int legs = ((JSONObject) obj.getJSONArray("routes").get(0))
					.getJSONArray("legs").length();
			System.out.println("Total Legs :" + legs);
			for (int i = 0; i < legs; i++) {
				JSONObject leg = ((JSONObject) obj.getJSONArray("routes")
						.get(0)).getJSONArray("legs").getJSONObject(i);
				double distanceM = leg.getJSONObject("distance").getDouble(
						"value");
				System.out.println(distanceM);
				distanceM = distanceM / 1000;

				modifyDaoObj.getLandmarkDistanceList().get(i)
						.setDistance(distanceM);
				/*
				 * System.out.println(modifyDaoObj.getLandmarkDistanceList().get(
				 * i).getSourceId() + " > "
				 * +modifyDaoObj.getLandmarkDistanceList
				 * ().get(i).getTargetId());
				 */
				totalDistance += distanceM;

				modifyDaoObj.getAplDistanceList().get(i).setDistance(distanceM);

				if (inOut.equalsIgnoreCase("IN")) {
					if (i == 0) {
						distance[i] = 0;
					} else if (i < legs - 1) {
						distance[i] = (float) distanceM;
					}
				} else if (inOut.equalsIgnoreCase("OUT")) {

					if (i < legs) {
						/* System.out.println(" [] :" + ((legs-1)-i) ); */
						distance[(legs - 1) - i] = (float) distanceM;
					}
				}

			}
			/* System.out.println("Total distance : "+ totalDistance); */
			modifyDaoObj.setTotalDistance(totalDistance);

		} catch (Exception e) {
			System.out.println("Error in getMapDistance : " + e);
			distance = null;
		}

		return distance;
	}

	public float[] getTime(ModifyTripDao modifyDaoObj, String inOut,
			float[] distance, String tripTime) {
		int firstlandmark = 0;
		float time[] = null;
		try {

			time = modifyDaoObj.getTime(inOut, distance, tripTime);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return time;
	}

}
