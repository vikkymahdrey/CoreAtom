package com.agiledge.atom.mobile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.agiledge.atom.mobile.dao.DeviceVersion;
import com.agiledge.atom.mobile.service.AndroidAppForEmployeeService;
import com.agiledge.atom.mobile.service.CometSettingService;
import com.agiledge.atom.mobile.service.LoginService;
import com.agiledge.atom.mobile.service.TripService;
import com.agiledge.atom.mobile.service.TripSheetService;

/**
 * Servlet implementation class MobileLayer
 */

public class AndroidServlet extends HttpServlet {
	TripService tripService = null;

	public void init(ServletConfig config) throws ServletException {
		tripService = new TripService();
	}

	/**
	 * @see Servlet#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#HttpServlet()
	 */

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(request, response);

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		process(request, response);

	}

	private void process(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		boolean retval = false;
		int no = 0;
		try {
			BufferedReader reader = request.getReader();
			String line;
			String lines = "";

			while ((line = reader.readLine()) != null) {
			//	System.out.println("Line :" + line);
				lines = lines + line + "\n";
			}
			reader.close();
			JSONObject obj = new JSONObject(lines);
		//	System.out.println("Lines :" + lines);
			PrintWriter out = response.getWriter();
			//System.out.println("JSON data :");
			//System.out.println(obj.getString("ACTION"));
			String nuance="";
			Calendar cal1=Calendar.getInstance();
			try{
				 nuance=obj.getString("NUANCE");
				 long longnaunce=Long.parseLong(nuance);
				cal1.setTimeInMillis(longnaunce);
			} catch (Exception e) {
				
			}

			JSONObject rObj = null;
			
			if (obj.getString("ACTION").equals("bulkinsert")) {
				 rObj = new CometSettingService().bulkInsert(obj); 
			} else
			if (obj.getString("ACTION").equals("INTERNET_CHECK")) {
				 rObj = obj;
				 rObj.put("result", "true"); 
			}
			if (obj.getString("ACTION").equals("downloadSettings")) {
				 rObj = new CometSettingService().getCometSettingsJSON(obj); 
			} else if (obj.getString("ACTION").equals("loginOTP")) {
				/*OLDER VERSION IMEI,PSWD ONLY*/
				no = 1;
				rObj = new LoginService().ValidateUserOTP( obj.getString("password"),
						obj.getString("IMEI"));
			}else if (obj.getString("ACTION").equals("LOGINOTP")) {
				/*NEWER  VERSION  IMEI,PSWD,VERSION*/
				no = 1;
				rObj = new LoginService().ValidateUserOTP( obj.getString("password"),
						obj.getString("IMEI"));
				
				if((rObj.get("result").equals("true")))
				{
					new DeviceVersion().Version(obj.getString("IMEI"),obj.getString("VERSION"));
					
				}
				
				
				
			} else if (obj.getString("ACTION").equals("login")) {
				no = 1;
				rObj = new LoginService().ValidateUser(
						obj.getString("userName"), obj.getString("password"),
						obj.getString("IMEI"));
			} else if (obj.getString("ACTION").equals("gettrips")
					&& new LoginService().CheckLogged(obj.getString("IMEI"))) {
				no = 2;
				rObj = new TripSheetService().getTrips(obj.getString("IMEI"),
						obj.getString("date"));
			} else if (obj.getString("ACTION").equals("gettrip")
					&& new LoginService().CheckLogged(obj.getString("IMEI"))) {
				no = 3;
				rObj = new TripSheetService().getTripSheet(
						obj.getString("IMEI") );
			} else if (obj.getString("ACTION").equals("vehiclePosition")
					&& new LoginService().CheckLogged(obj.getString("IMEI"))) {
				no = 4;
				rObj = tripService.updateGeoCode(obj.getString("IMEI"),
						obj.getString("tripId"), obj.getDouble("latitude"),
						obj.getDouble("longitude"),
						obj.getDouble("distanceCovered"),
						obj.getLong("timeElapsed"), obj.getLong("NUANCE"));
			} else if (obj.getString("ACTION").equals("fullVehiclePosition")) {
				no = 4;
				rObj = tripService.updateGeoCodePassive(obj.getString("IMEI"),
						obj.getDouble("latitude"), obj.getDouble("longitude"),
						obj.getLong("NUANCE"));
			} else if (obj.getString("ACTION").equals("employeeGetIn")
					&& new LoginService().CheckLogged(obj.getString("IMEI"))
					&& new LoginService().authenticateEmployee(
							obj.getString("tripId"), obj.getString("empCode"),
							obj.getString("password"))) {
				no = 5;

				rObj = tripService.employeeCheckIn(obj.getString("IMEI"),
						obj.getString("tripId"), obj.getString("empCode"),
						obj.getDouble("latitude"), obj.getDouble("longitude"),obj.getLong("NUANCE"));
			} else if (obj.getString("ACTION").equals("employeeGetOut")
					&& new LoginService().CheckLogged(obj.getString("IMEI"))
					&& new LoginService().authenticateEmployee(
							obj.getString("tripId"), obj.getString("empCode"),
							obj.getString("password")) ) {
				no = 5;

				rObj = tripService.employeeCheckOut(obj.getString("IMEI"),
						obj.getString("tripId"), obj.getString("empCode"),
						obj.getDouble("latitude"), obj.getDouble("longitude"),obj.getLong("NUANCE"));
			} else if (obj.getString("ACTION").equals("startTrip")
					&& new LoginService().CheckLogged(obj.getString("IMEI"))) {
				rObj = tripService.startTrip(obj.getString("tripId"),
						obj.getString("IMEI"));
			} else if (obj.getString("ACTION").equals("stopTrip")
					&& new LoginService().CheckLogged(obj.getString("IMEI"))) {
				rObj = tripService.stopTrip(obj.getString("tripId"));
			} else if (obj.getString("ACTION").equals("forceStopTrip")
					&& new LoginService().CheckLogged(obj.getString("IMEI"))) {
				rObj = tripService.forceStopTrip(obj.getString("tripId"));
			} else if (obj.getString("ACTION").equals("logout")) {
				rObj = new LoginService().logout(obj.getString("IMEI"));
				no = 8;
			} else if (obj.getString("ACTION").equals("alarm")) {
				String time = "";
				Calendar cal = Calendar.getInstance();
				cal.setTime(new Date());
				int hour = cal.get(Calendar.HOUR);
				int minute = cal.get(Calendar.MINUTE);
				time = "" + (hour < 10 ? "0" + hour : hour);
				time = time + ":" + (minute < 10 ? "0" + minute : minute);
				rObj = tripService.panicAlarm(obj.getString("IMEI"),time);
				no = 9;
			} else if (obj.getString("ACTION").equals("updateTime")) {
				String time = "";
				Calendar cal = Calendar.getInstance();
				cal.setTime(new Date());
				int hour = cal.get(Calendar.HOUR);
				int minute = cal.get(Calendar.MINUTE);
				time = "" + (hour < 10 ? "0" + hour : hour);
				time = time + ":" + (minute < 10 ? "0" + minute : minute);
			//	System.out.println("time :" + time);

				rObj = tripService.updateTime(obj.getString("tripId"), time,
						obj.getString("userName"), obj.getString("password"));
				no = 10;
			} else if (obj.getString("ACTION").equals("escortGetIn")) {

				rObj = tripService.escortGetIn(obj.getString("tripId"),
						obj.getString("userName"), obj.getString("password"),
						obj.getDouble("latitude"), obj.getDouble("longitude"));
				no = 10;
			} else if (obj.getString("ACTION").equals("escortGetOut")) {

				rObj = tripService.escortGetOut(obj.getString("tripId"),
						obj.getString("userName"), obj.getString("password"),
						obj.getDouble("latitude"), obj.getDouble("longitude"));
				no = 10;
			}
			else if(obj.getString("ACTION").equals("GET_TRIPS"))
			{
				rObj=tripService.getTrips(obj.getString("password"));
			}else if(obj.getString("ACTION").equalsIgnoreCase("employeeGetNew")){
				System.out.println(obj.getString("tripId"));
				rObj=tripService.employeeNFCSwap(obj.getString("tripId"),obj.getLong("NUANCE"),obj.getString("latitude"),obj.getString("longitude"),obj.getString("NFC"));
			}

			rObj.put("NUANCE", nuance);
			request.getSession().invalidate();
			out.print(rObj.toString());
			out.flush();
			out.close();
			
			
			
		} catch (Exception e) {
			System.out.println(no + "Error in Mobile Layer " + e);
		}
		 
	}
}

// forceStopTrip
// escortGetIn
// escortGetOut