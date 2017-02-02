package com.agiledge.atom.service;

import com.agiledge.atom.commons.StoppingThread;
import com.agiledge.atom.commons.TripGenerator;
import com.agiledge.atom.dao.SocketDeviceDao;

public class SocketDeviceService {

	public void ProcessData(String data) {
		System.out.println("data" + data);
		try {
			new SocketDeviceDao().insertdata(data);
		} catch (Exception e) {
			System.out.println("Error in data insert" + e);
		}
		try {
			if (data.indexOf("{") > -1 && data.lastIndexOf("}") > -1) {
				data = data.substring(data.indexOf("{") + 1,
						data.lastIndexOf("}"));
				if (data.length() > 10) {
					String deviceNum = data.substring(data.indexOf("{") + 1,
							data.lastIndexOf("@"));
					String actualgps[] = processGPS(data);
					if (actualgps[1] != null && actualgps[1] != ""
							&& actualgps[0] != null && actualgps[0] != "") {
						//Starting Thread
						try{
						 TripGenerator tg= new TripGenerator(
									actualgps[0], actualgps[1],deviceNum);
						 tg.start();
						}catch(Exception e){System.out.println("Error in Thrread"+e);}
						// inserting LatLong
						new SocketDeviceDao().insertGPSData(deviceNum,
								actualgps[0], actualgps[1]);
						if (data.indexOf("<") > -1) {
							String rfidcode = data.substring(
									data.indexOf("<") + 1,
									data.lastIndexOf(">"));
							// Employee Swipe
							new SocketDeviceDao().updateemployeeLogin(rfidcode,
									deviceNum, actualgps);
						}
						if (data.indexOf("[") > -1) {
							String message = data.substring(
									data.indexOf("[") + 1,
									data.lastIndexOf("]"));
							// Checking for Panic
							if (message.equalsIgnoreCase("EH")) {
								new SocketDeviceDao().panicactivated(deviceNum,
										actualgps[0], actualgps[1]);
							}
						}
						new StoppingThread(deviceNum, actualgps[0],
								actualgps[1]);
					}
				}
			}
		} catch (Exception e) {
			System.out.println("Erron in SocketDeviceService" + e);
		}
	}

	private String[] processGPS(String data) {
		String latitude = data.substring(data.indexOf("@") + 1,
				data.lastIndexOf(",N,"));
		String longitude = data.substring(data.indexOf(",N,") + 4,
				data.lastIndexOf(",E#"));
		String result[] = new String[2];
		// checking for invalid data
		String invalidlat[] = latitude.split(",");
		String invalidlong[] = longitude.split(",");
		// replacing to check data
		String invalidlatdotrep = latitude.replace(".", ",");
		String invalidlongdotrep = longitude.replace(".", ",");
		String invalidlatdot[] = invalidlatdotrep.split(",");
		String invalidlongdot[] = invalidlongdotrep.split(",");
		float latpart2 = 0;
		float longpart2 = 0;
		if (invalidlat.length == 1 && invalidlong.length == 1
				&& invalidlatdot.length == 2 && invalidlongdot.length == 2) {

			latpart2 = Float
					.parseFloat(latitude.substring(2, latitude.length())) / 60;

			longpart2 = Float.parseFloat(longitude.substring(2,
					longitude.length())) / 60;

			String strlatpart = "" + latpart2;
			String strlongpart2 = "" + longpart2;
			result[0] = latitude.substring(0, 2)
					+ strlatpart.substring(strlatpart.indexOf("."));
			result[1] = longitude.substring(0, 2)
					+ strlongpart2.substring(strlongpart2.indexOf("."));
			System.out.println("SAFE LATLONG");
		}
		return result;
	}

	public void stopTrip(String deviceNo, String lat, String lng) {
		new SocketDeviceDao().stopTrip(deviceNo, lat, lng);
	}

	public int vehicleAlertSms(String deviceNo, String lat, String lng) {
		// TODO Auto-generated method stub
		return new SocketDeviceDao().vehicleAlertSms(deviceNo, lat, lng);
	}
	
}
