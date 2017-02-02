package com.agiledge.atom.mobile.service;

import java.util.Calendar;
import java.util.Date;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import net.sf.json.JSONArray;

import org.json.JSONObject;

import com.agiledge.atom.mobile.dao.CometSettingsDao;
import com.agiledge.atom.mobile.dto.CometSettingsDto;

public class CometSettingService {

	/**
	 * @param args
	 */
	public JSONObject getCometSettingsJSON(JSONObject jObj) {
		// TODO Auto-generated method stubJSONObject jObj = 
		 
		try {
			
		
				CometSettingsDto sDto = new CometSettingsDao().getCometSettings();
				System.out.println(sDto.toString());
				  jObj.put("result", "true");
				  jObj.put("versionNo",  sDto.getCometVersionNo());
				  jObj.put("configurationType", sDto.getCometAuthType());
		
		} catch(Exception e) {
			System.out.println("Error in getCometSettingsJSON : "+ e);
		}
		
		return jObj;
	}
	
	public JSONObject bulkInsert(JSONObject jObj) {
		try {
			TripService tripService =new TripService();
			 

			 org.json.JSONArray jsonArray = jObj.getJSONArray("array");
			  
			int length = jsonArray.length();
			System.out.println(jsonArray.length());
			for(int i=0; i< length; i++ ) {
				JSONObject obj = jsonArray.getJSONObject(i );
				System.out.println("ddd");
				System.out.println("Each Obj action:" + obj.getString("ACTION") +" : " + obj.toString());
				if (obj.getString("ACTION").equals("downloadSettings")) {
					 new CometSettingService().getCometSettingsJSON(obj); 
				} else if (obj.getString("ACTION").equals("loginOTP")) {
				 
					  new LoginService().ValidateUserOTP( obj.getString("password"),
							obj.getString("IMEI"));
				} else if (obj.getString("ACTION").equals("login")) {
					  new LoginService().ValidateUser(
							obj.getString("userName"), obj.getString("password"),
							obj.getString("IMEI"));
				} else if (obj.getString("ACTION").equals("gettrips")
						&& new LoginService().CheckLogged(obj.getString("IMEI"))) {
					  new TripSheetService().getTrips(obj.getString("IMEI"),
							obj.getString("date"));
				} else if (obj.getString("ACTION").equals("gettrip")
						&& new LoginService().CheckLogged(obj.getString("IMEI"))) {
					 new TripSheetService().getTripSheet(
							obj.getString("IMEI") );
				} else if (obj.getString("ACTION").equals("vehiclePosition")
						&& new LoginService().CheckLogged(obj.getString("IMEI"))) {
					  tripService.updateGeoCode(obj.getString("IMEI"),
							obj.getString("tripId"), obj.getDouble("latitude"),
							obj.getDouble("longitude"),
							obj.getDouble("distanceCovered"),
							obj.getLong("timeElapsed"), obj.getLong("NUANCE"));
				} else if (obj.getString("ACTION").equals("fullVehiclePosition")) {
					 tripService.updateGeoCodePassive(obj.getString("IMEI"),
							obj.getDouble("latitude"), obj.getDouble("longitude"),
							obj.getLong("NUANCE"));
				} else if (obj.getString("ACTION").equals("employeeGetIn")
						&& new LoginService().CheckLogged(obj.getString("IMEI"))
						&& new LoginService().authenticateEmployee(
								obj.getString("tripId"), obj.getString("empCode"),
								obj.getString("password"))) {
					 

					 tripService.employeeCheckIn(obj.getString("IMEI"),
							obj.getString("tripId"), obj.getString("empCode"),
							obj.getDouble("latitude"), obj.getDouble("longitude"),obj.getLong("NUANCE"));
				} else if (obj.getString("ACTION").equals("employeeGetOut")
						&& new LoginService().CheckLogged(obj.getString("IMEI"))
						&& new LoginService().authenticateEmployee(
								obj.getString("tripId"), obj.getString("empCode"),
								obj.getString("password")) ) {
				 tripService.employeeCheckOut(obj.getString("IMEI"),
							obj.getString("tripId"), obj.getString("empCode"),
							obj.getDouble("latitude"), obj.getDouble("longitude"),obj.getLong("NUANCE"));
				} else if (obj.getString("ACTION").equals("startTrip")
						&& new LoginService().CheckLogged(obj.getString("IMEI"))) {
					 tripService.startTrip(obj.getString("tripId"),
							obj.getString("IMEI"));
				} else if (obj.getString("ACTION").equals("stopTrip")
						&& new LoginService().CheckLogged(obj.getString("IMEI"))) {
					  tripService.stopTrip(obj.getString("tripId"));
				} else if (obj.getString("ACTION").equals("forceStopTrip")
						&& new LoginService().CheckLogged(obj.getString("IMEI"))) {
					 tripService.forceStopTrip(obj.getString("tripId"));
				} else if (obj.getString("ACTION").equals("logout")) {
					  new LoginService().logout(obj.getString("IMEI"));
					 
				} else if (obj.getString("ACTION").equals("alarm")) {
					String time = "";
					Calendar cal = Calendar.getInstance();
					cal.setTime(new Date());
					int hour = cal.get(Calendar.HOUR);
					int minute = cal.get(Calendar.MINUTE);
					time = "" + (hour < 10 ? "0" + hour : hour);
					time = time + ":" + (minute < 10 ? "0" + minute : minute);
					 tripService.panicAlarm(obj.getString("IMEI"),time);
					 
				} else if (obj.getString("ACTION").equals("updateTime")) {
					String time = "";
					Calendar cal = Calendar.getInstance();
					cal.setTime(new Date());
					int hour = cal.get(Calendar.HOUR);
					int minute = cal.get(Calendar.MINUTE);
					time = "" + (hour < 10 ? "0" + hour : hour);
					time = time + ":" + (minute < 10 ? "0" + minute : minute);
				//	System.out.println("time :" + time);

					 tripService.updateTime(obj.getString("tripId"), time,
							obj.getString("userName"), obj.getString("password"));
					 
				} else if (obj.getString("ACTION").equals("escortGetIn")) {

					 tripService.escortGetIn(obj.getString("tripId"),
							obj.getString("userName"), obj.getString("password"),
							obj.getDouble("latitude"), obj.getDouble("longitude"));
					 
				} else if (obj.getString("ACTION").equals("escortGetOut")) {

					 tripService.escortGetOut(obj.getString("tripId"),
							obj.getString("userName"), obj.getString("password"),
							obj.getDouble("latitude"), obj.getDouble("longitude"));
					 
				}
			}
			
			  jObj.put("result", "true");
		 
	
		}catch(Exception e) {
			
		}
		
		return jObj;
	}

}
