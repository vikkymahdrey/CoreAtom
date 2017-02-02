package com.agiledge.atom.mobile.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import com.agiledge.atom.constants.SettingsConstant;
import com.agiledge.atom.dao.EscortDao;
import com.agiledge.atom.dto.EscortDto;
import com.agiledge.atom.dto.TripDetailsDto;
import com.agiledge.atom.dto.VehicleDto;
import com.agiledge.atom.mobile.dao.MobileTripSheetDao;
import com.agiledge.atom.mobile.dao.TripServiceDao;
import com.agiledge.atom.service.SettingsService;
import com.agiledge.atom.sms.SMSService;


public class TripService {

	private String message;
	public String getMessage() {
		return message;
	}
	public JSONObject startTrip(String tripId, String imei) {
		int retVal=0;
		JSONObject rObj = null;
	//	boolean validFalg=  validateTripStart(tripId);
		boolean isValid =true;
		if(isValid) {
		 retVal = new TripServiceDao().startTrip(tripId, imei);
		}
		rObj = new JSONObject();
		try {
			rObj.put("ACTION", "startTrip");
			if(isValid==true) {
			 
			if (retVal == 1) {
				rObj.put("result", "true");
				rObj.put("message", "Trip started successfully");
			
				if(SettingsConstant.comp.equalsIgnoreCase("tyko"))
				{
					new SMSService().sendTripStartSMS(tripId);
				}
				
			}else if(retVal == -2) {
				rObj.put("result", "false");
				rObj.put("message", "This is stopped trip");
			} else
			{
				rObj.put("result", "false");
				rObj.put("message", "Unable to start the trip");
			}
			} else {
				rObj.put("result", "false");
				rObj.put("message", "Unable to start trip. "+message);
			}
			
		} catch (Exception e) {
			System.out.println("erro" + e);
		}

		return rObj;
	}

	public JSONObject stopTrip(String tripId) {
		System.out.println("In stop service");
		int retVal=0;
		JSONObject rObj = null;
	//	boolean validFalg =validateTripStop(tripId);
		boolean isValid = true;
		
		if(isValid) {
		 retVal = new TripServiceDao().stopTrip(tripId );
		}
		rObj = new JSONObject();
		try {
			rObj.put("ACTION", "stopTrip");
			if(isValid==true) {
			
			if (retVal == 1) {
				rObj.put("result", "true");
				rObj.put("message", "Trip stopped successfully");
			}else
			{
				rObj.put("result", "false");
				rObj.put("message", "Unable to stop the trip");
			}
			} else {
				rObj.put("result", "false");
				rObj.put("message", "Unable to stop trip. "+message);
			}
			
		} catch (Exception e) {
			System.out.println("erro" + e);
		}

		return rObj;
 
	}
	
	public ArrayList<TripDetailsDto> getStationaryVehiclePosition(String branch) {
		return new TripServiceDao().getStationaryVehiclePosition(branch);
	}

	/* the following function is used to update geo code while trip running */
	public JSONObject updateGeoCode(String imei, String tripId,
			double latitude, double longitude, double distanceCovered, long timeElapsed, long nuance) {
		JSONObject rObj = new JSONObject();
		int retVal = new TripServiceDao().updateGeoCode(imei, tripId, latitude,
				longitude, nuance);
		if(retVal>0)
		{
			 retVal = new TripServiceDao().updateDistanceCovered( tripId,  distanceCovered, timeElapsed);
			
		}
		try {
			rObj.put("ACTION", "vehiclePosition");
			rObj.put("retVal", retVal);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//System.out.println(rObj);
		return rObj;
	}
	
	/* the following function is used to update geo code while trip not running */
	public JSONObject updateGeoCodePassive(String imei,
			double latitude, double longitude, long nuance  ) {
		JSONObject rObj = new JSONObject();
		TripServiceDao tripService=new TripServiceDao();
		int retVal = tripService.updateGeoCodePassive(imei,   latitude,
				longitude, nuance);
		tripService=null;
		 
		try {
			String result ="false";
			if(retVal>0)  {
				result = "true";
			}
			rObj.put("ACTION", "fullVehiclePosition");
			rObj.put("retVal", retVal);
			rObj.put("result", result);
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//System.out.println(rObj);
		return rObj;
	}
	
	
	

	public JSONObject employeeCheckIn(String imei, String tripId,
			String empCode, double latitude, double longitude,long nuance) {
	//	System.out.println("Imei :" + imei + ", Trip Code: " + tripId
	//			+ "EmpCode :" + empCode + "lat" + latitude + "lon" + longitude);
		JSONObject rObj = new JSONObject();
		int retVal=0;
	retVal = new TripServiceDao().employeeCheckIn(tripId, empCode,
				latitude, longitude,nuance);

		try {
			if (retVal == 0) {
				rObj.put("result", "false");
			} else {
				rObj.put("result", "true");
			}
			rObj.put("ACTION", "employeeGetIn");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	//	System.out.println(rObj);
		return rObj;
	}

	
	public JSONObject panicAlarm(String imei,String time) {
		JSONObject rObj = new JSONObject();
	    String activatedBy="DRIVER APP";
	    int retVal=0;	
	   
	    	   retVal= new TripServiceDao().panicAlarm(imei,activatedBy);
	   
	    try {
			if (retVal == 0) {				
				rObj.put("result", "false");
				
			} else {
				new SMSService().sendSMSOnPanic(imei,time);
				rObj.put("result", "true");
			}
			rObj.put("ACTION", "alarm");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	//	System.out.println(rObj);
		return rObj;
	}

	public JSONObject updateTime(String tripId,String tripTime, String userName, String password) {
		JSONObject rObj = new JSONObject();
		EscortDto escortDto= new EscortDto();
		escortDto.setEscortClock(userName);
		escortDto.setPassword(password);
		escortDto.setTripId(tripId);
	//	System.out.println("tripTime:  "+ tripTime);
		try {
		boolean flag=new EscortDao().validateEscort(escortDto);
		if(flag==false)
		{
			rObj.put("result", "false");
			rObj.put("message", "Invalid User");
		}else
		{
			
			
		int retVal = new TripServiceDao().updateTime(tripId,tripTime,userName);
		
			if (retVal == 0) {
				rObj.put("result", "false");
				rObj.put("message", "Time is not updated");
			} else {
				rObj.put("result", "true");
				
			}
		}
			rObj.put("ACTION", "updateTime");
		
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	//	System.out.println(rObj);
		return rObj;
	}

	public JSONObject escortGetIn(String tripId, String userName,
			String password, double latitude, double longitude) {
		JSONObject rObj = new JSONObject();
		 EscortDto dto= new EscortDto();
		 dto.setEscortClock(userName);
		 dto.setPassword(password);
		 dto.setTripId(tripId);
		 
		try {
		boolean flag=new EscortDao().validateEscort(dto);
		if(flag==false)
		{
			if(SettingsConstant.escortpwd.equalsIgnoreCase("mobileNo"))
			{
			int result=new EscortDao().escortGetInbyPassword(tripId, password,latitude,longitude);
		if(result==1)
		{
			rObj.put("result", "true");
		}else if(result==-1)
		{
			rObj.put("result", "true");
			rObj.put("message", "Security is already authenticated");
		}
		else{
			rObj.put("result", "false");
			rObj.put("message", "Invalid User");
		}
			}
		}else
		{
			
			TripServiceDao dao= new TripServiceDao();
			 
		int retVal =   dao.escortGetIn(tripId,userName,latitude,longitude);
		
			if (retVal == 0) {
				rObj.put("result", "false");
				rObj.put("message", dao.getMessage());
			} else {
				rObj.put("result", "true");
				
			}
		}
			rObj.put("ACTION", "escortGetIn");
		
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		System.out.println(rObj);
		return rObj;
	}

	public JSONObject escortGetOut(String tripId, String userName,
			String password, double latitude, double longitude) {
		JSONObject rObj = new JSONObject();
		 EscortDto dto= new EscortDto();
		 dto.setEscortClock(userName);
		 dto.setPassword(password);
		 dto.setTripId(tripId);
		 
		try {
		boolean flag=new EscortDao().validateEscort(dto);
		if(flag==false)
		{
			rObj.put("result", "false");
			rObj.put("message", "Invalid User");
		}else
		{
			
			TripServiceDao dao= new TripServiceDao();
			 
		int retVal =   dao.escortGetOut(tripId,userName,latitude,longitude);
		
			if (retVal == 0) {
				rObj.put("result", "false");
				rObj.put("message", dao.getMessage());
			} else {
				rObj.put("result", "true");
				
			}
		}
			rObj.put("ACTION", "escortGetOut");
		
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		System.out.println(rObj);
		return rObj;
	}


	
	public JSONObject employeeCheckOut(String imei, String tripId,
			String empCode, double latitude, double longitude,long nuance) {
		
		JSONObject rObj = new JSONObject();
		TripServiceDao serviceDao=new TripServiceDao();
		int retVal = serviceDao.employeeCheckOut(tripId, empCode,
				latitude, longitude,nuance);
		int stVal=serviceDao.CheckOrUpdateEmpPos(tripId, empCode,
		latitude, longitude);
		if(stVal==1)
		{			
			  double theta = longitude - serviceDao.empLon;
			  double dist = Math.sin(deg2rad(latitude)) * Math.sin(deg2rad(serviceDao.empLat)) + Math.cos(deg2rad(latitude)) * Math.cos(deg2rad(serviceDao.empLat)) * Math.cos(deg2rad(theta));
			  dist = Math.acos(dist);
			  dist = rad2deg(dist);
			  dist = dist * 60 * 1.1515;			  
			  dist = dist * 1.609344;	
			  if(dist>0.3)
			  {
			  serviceDao.updateCheckOutIsInCorrect(tripId,empCode);
			  }
		}
		try {
			if (retVal == 0) {
				rObj.put("result", "false");
			} else {
				rObj.put("result", "true");
			}
			rObj.put("ACTION", "employeeGetOut");
		} catch (JSONException e) {
			e.printStackTrace();
		}
//		System.out.println(rObj);
		return rObj;
	}
	
	
	
	
	
	
	private double deg2rad(double deg) {
		  return (deg * Math.PI / 180.0);
		}


		private double rad2deg(double rad) {
		  return (rad * 180 / Math.PI);
		}
	
	
	
	
	
	public boolean validateTripStart(String tripId) {
		boolean flag=false;
		 
		SettingsService stService = new SettingsService();
		
		try {
		TripDetailsDto tripDto= new MobileTripSheetDao().getVehicleTripSheet(tripId);
	//	System.out.println("site :"+tripDto.getSiteId());
		int preDelay=0;
		int postDelay=0;
		String validString=stService.getSiteSetting(Integer.parseInt(tripDto.getSiteId()),"TRIP START VALIDATION");
	//	System.out.println("Valid : "+validString);
		 boolean valid=(validString!=null&&validString.equalsIgnoreCase("true"));
		if(tripDto.getTrip_log().equalsIgnoreCase("IN")&&valid) {
//			System.out.println("IN Trip and valid ");
			  preDelay=
			 Integer.parseInt(stService.getSiteSetting(Integer.parseInt(tripDto.getSiteId()),"PRE START DELAY FOR IN").trim());
			   postDelay=
			 Integer.parseInt(stService.getSiteSetting(Integer.parseInt(tripDto.getSiteId()),"POST START DELAY FOR IN"));
	//		System.out.println("IN pre delay : " + preDelay+ "post delay : " + postDelay);   
				 Calendar curTime= Calendar.getInstance();
				 curTime.setTime(new Date());
				 Calendar tripTime = Calendar.getInstance();
				 String date[] = tripDto.getTrip_date().split("-");
				 String time[] = tripDto.getTrip_time().split(":");
				 tripTime.set(Calendar.YEAR, Integer.parseInt(date[0]));
				 tripTime.set(Calendar.MONTH, Integer.parseInt(date[1])-1);
				 tripTime.set(Calendar.DATE, Integer.parseInt(date[2]));
				 tripTime.set(Calendar.HOUR_OF_DAY, Integer.parseInt( time[0]));
				 tripTime.set(Calendar.MINUTE, Integer.parseInt(time[1]));
				 Calendar preTime = (Calendar) tripTime.clone();
				 preTime.add(Calendar.MINUTE, -preDelay);
				 Calendar postTime = (Calendar) tripTime.clone();
				 postTime.add(Calendar.MINUTE, postDelay);
			//	 System.out.println("Cur Time : " + curTime.getTime());
			//	 System.out.println("Pre Time : " + preTime.getTime());
			//	 System.out.println("Post Time : " + postTime.getTime());
			//	 System.out.println(" Curtime compareTo preTime : " + curTime.compareTo(preTime));
			//	 System.out.println(" Curtime compareTo postTime : " + curTime.compareTo(postTime));
				 if(curTime.compareTo(preTime)>=0&&curTime.compareTo(postTime)<=0) {
					 flag=true;
					 message="";
				 } else if (curTime.compareTo(preTime)<0) {
					 message="Time is too early";
				 } else if (curTime.compareTo(postTime)>0) {
					 message="Time is too late";
				 }
				 
		
			   
			   
		} else if(tripDto.getTrip_log().equalsIgnoreCase("OUT")&&valid) {
			
	//		System.out.println("valid OUt");
			  preDelay=
			 Integer.parseInt(stService.getSiteSetting(Integer.parseInt(tripDto.getSiteId()),"PRE START DELAY FOR OUT"));
			   postDelay=
			 Integer.parseInt(stService.getSiteSetting(Integer.parseInt(tripDto.getSiteId()),"POST START DELAY FOR OUT"));
			   
				 Calendar curTime= Calendar.getInstance();
				 curTime.setTime(new Date());
				 Calendar tripTime = Calendar.getInstance();
				 String date[] = tripDto.getTrip_date().split("-");
				 String time[] = tripDto.getTrip_time().split(":");
				 tripTime.set(Calendar.YEAR, Integer.parseInt(date[0]));
				 tripTime.set(Calendar.MONTH, Integer.parseInt(date[1])-1);
				 tripTime.set(Calendar.DATE, Integer.parseInt(date[2]));
				 tripTime.set(Calendar.HOUR_OF_DAY, Integer.parseInt( time[0]));
				 tripTime.set(Calendar.MINUTE, Integer.parseInt(time[1]));
				 Calendar preTime = (Calendar) tripTime.clone();
				 preTime.add(Calendar.MINUTE, -preDelay);
				 Calendar postTime = (Calendar) tripTime.clone();
				 postTime.add(Calendar.MINUTE, postDelay);
		//		 System.out.println("Cur Time : " + curTime.getTime());
		//		 System.out.println("Pre Time : " + preTime.getTime());
		//		 System.out.println("Post Time : " + postTime.getTime());
	//		 System.out.println(" Curtime compareTo preTime : " + curTime.compareTo(preTime));
	//			 System.out.println(" Curtime compareTo postTime : " + curTime.compareTo(postTime));
				 if(curTime.compareTo(preTime)>=0&&curTime.compareTo(postTime)<=0) {
					 flag=true;
					 message="";
				 } else if (curTime.compareTo(preTime)<0) {
					 message="Time is too early";
				 } else if (curTime.compareTo(postTime)>0) {
					 message="Time is too late";
				 }
				 
		
			   
			   
		} 
			
			else {
	//			System.out.println("reached here in else ");
				flag=true;
				message="";
						 
		}
//		System.out.println("reached here end ");
			 
	
		} catch(Exception e) {
			System.out.println(" Error in validate Trip start/stop " +e );
		}
		return flag;
	}
	
	
		   public boolean validateTripStop(String tripId) {
				boolean flag=false;
				SettingsService stService = new SettingsService();
				try {
				TripDetailsDto tripDto= new MobileTripSheetDao().getVehicleTripSheet(tripId);
	//			System.out.println(""+tripDto.getSiteId());
				int preDelay=0;
				int postDelay=0;
				String validString=stService.getSiteSetting(Integer.parseInt(tripDto.getSiteId()),"TRIP STOP VALIDATION");
				boolean valid=(validString!=null&&validString.equalsIgnoreCase("true"));
				if(tripDto.getTrip_log().equalsIgnoreCase("IN")&&valid) {
					   preDelay=
								 Integer.parseInt(stService.getSiteSetting(Integer.parseInt(tripDto.getSiteId()),"PRE STOP DELAY FOR IN"));
					   postDelay=
								 Integer.parseInt(stService.getSiteSetting(Integer.parseInt(tripDto.getSiteId()),"POST STOP DELAY FOR IN"));
						    
						 Calendar curTime= Calendar.getInstance();
						 curTime.setTime(new Date());
						 Calendar tripTime = Calendar.getInstance();
						 String date[] = tripDto.getTrip_date().split("-");
						 String time[] = tripDto.getTrip_time().split(":");
						 tripTime.set(Calendar.YEAR, Integer.parseInt(date[0]));
						 tripTime.set(Calendar.MONTH, Integer.parseInt(date[1])-1);
						 tripTime.set(Calendar.DATE, Integer.parseInt(date[2]));
						 tripTime.set(Calendar.HOUR_OF_DAY, Integer.parseInt( time[0]));
						 tripTime.set(Calendar.MINUTE, Integer.parseInt(time[1]));
						 Calendar preTime = (Calendar) tripTime.clone();
						 preTime.add(Calendar.MINUTE, -preDelay);
						 Calendar postTime = (Calendar) tripTime.clone();
						 postTime.add(Calendar.MINUTE, postDelay);
	//					 System.out.println("Cur Time : " + curTime.getTime());
	//					 System.out.println("Pre Time : " + preTime.getTime());
	//					 System.out.println("Post Time : " + postTime.getTime());
		//				 System.out.println(" Curtime compareTo preTime : " + curTime.compareTo(preTime));
			//			 System.out.println(" Curtime compareTo postTime : " + curTime.compareTo(postTime));
						 if(curTime.compareTo(preTime)>=0&&curTime.compareTo(postTime)<=0) {
							 flag=true;
							 message="";
						 } else if (curTime.compareTo(preTime)<0) {
							 message="Time is too early";
						 } else if (curTime.compareTo(postTime)>0) {
							 message="Time is too late";
						 }
						 
				
					   
					   
				} else if(tripDto.getTrip_log().equalsIgnoreCase("OUT")&&valid)  {
					   preDelay=
								 Integer.parseInt(stService.getSiteSetting(Integer.parseInt(tripDto.getSiteId()),"PRE STOP DELAY FOR OUT"));
					   postDelay=
								 Integer.parseInt(stService.getSiteSetting(Integer.parseInt(tripDto.getSiteId()),"POST STOP DELAY FOR OUT"));
						    
						 Calendar curTime= Calendar.getInstance();
						 curTime.setTime(new Date());
						 Calendar tripTime = Calendar.getInstance();
						 String date[] = tripDto.getTrip_date().split("-");
						 String time[] = tripDto.getTrip_time().split(":");
						 tripTime.set(Calendar.YEAR, Integer.parseInt(date[0]));
						 tripTime.set(Calendar.MONTH, Integer.parseInt(date[1])-1);
						 tripTime.set(Calendar.DATE, Integer.parseInt(date[2]));
						 tripTime.set(Calendar.HOUR_OF_DAY, Integer.parseInt( time[0]));
						 tripTime.set(Calendar.MINUTE, Integer.parseInt(time[1]));
						 Calendar preTime = (Calendar) tripTime.clone();
						 preTime.add(Calendar.MINUTE, -preDelay);
						 Calendar postTime = (Calendar) tripTime.clone();
						 postTime.add(Calendar.MINUTE, postDelay);
	//					 System.out.println("Cur Time : " + curTime.getTime());
	//					 System.out.println("Pre Time : " + preTime.getTime());
	//					 System.out.println("Post Time : " + postTime.getTime());
	//					 System.out.println(" Curtime compareTo preTime : " + curTime.compareTo(preTime));
	//					 System.out.println(" Curtime compareTo postTime : " + curTime.compareTo(postTime));
						 if(curTime.compareTo(preTime)>=0&&curTime.compareTo(postTime)<=0) {
						 flag=true;
							 message="";
						 } else if (curTime.compareTo(preTime)<0) {
							 message="Time is too early";
						 } else if (curTime.compareTo(postTime)>0) {
							 message="Time is too late";
						 }
						 
				
					   
					   
				} else {
						flag=true;
						message="";		 
				}
					 
			
				} catch(Exception e) {
					System.out.println(" Error in validate Trip start/stop " +e );
				}
				return flag;
			}
		   
		   
		   public int setTripReadyForTracking(String tripId, String vehicleNo) {
			    int i = new TripServiceDao().setTripReadyForTracking(tripId, vehicleNo);
			    if(i>0) {
				TripDetailsDto dto = new MobileTripSheetDao().getVehicleTripSheet(tripId); 
				if (dto != null) {
					new TripSheetService().updateDistanceAndTime(dto);
					new TripServiceDao().getDriverNameAndMob(dto);
					new SMSService().sendPinSMS(dto);
				}
			    }
			    return i;
			
		   }
		   
		   

		   public int setTripsReadyForTracking(ArrayList<VehicleDto> vehicleList) {
			     int i=0;
			   for(VehicleDto dto : vehicleList) {
				   i = i + setTripReadyForTracking(dto.getTripId(), dto.getVehicleNo());
			   }
			   return i;
		   }
		public JSONObject forceStopTrip(String tripId) {
			// TODO Auto-generated method stub
			int retVal=0;
			JSONObject rObj = null;
	//		 System.out.println("Force stoping...");
			 
			 retVal = new TripServiceDao().forceStopTrip(tripId );
			 
			rObj = new JSONObject();
			try {
				rObj.put("ACTION", "forceStopTrip");
				 
				
				if (retVal == 1) {
					rObj.put("result", "true");
					rObj.put("message", "Trip stopped successfully");
				}else
				{
					rObj.put("result", "false");
					rObj.put("message", "Unable to stop the trip");
				}
				  
				
			} catch (Exception e) {
				System.out.println("erro" + e);
			}

			return rObj;

 
		}
	

		public JSONObject getTrips(String password)
		{

			JSONObject rObj = null;
			ArrayList<TripDetailsDto> list =new TripServiceDao().getTripsByPassword(password);
			try{
				int trips_size=list.size();
				rObj=new JSONObject();
				rObj.put("result", "true");
				rObj.put("message", "Trips updated successfully");
				rObj.put("total trips", trips_size);
		          int i=1;
				for(TripDetailsDto dto:list)
				{
				 rObj.put("trip_id"+i, dto.getId());
				 rObj.put("trip_date"+i, dto.getTrip_date());
				 rObj.put("trip_time"+i, dto.getTrip_time());
				 rObj.put("trip_log"+i,dto.getTrip_log());
				 rObj.put("otp"+i,dto.getDriverPassword());
				 i++;
				}	 
						
						
						
			}catch(Exception e){e.printStackTrace();}
				 return rObj;
		}
		public JSONObject employeeNFCSwap(String tripId, long NAUNCE,
				String latitude, String longitude, String NFC) {
			JSONObject rObj = new JSONObject();
			int retVal=0;
		retVal = new TripServiceDao().employeeNFCSwap(tripId,NAUNCE,latitude,longitude,NFC);

			try {
				if (retVal == 0) {
					rObj.put("result", "false");
				} else {
					rObj.put("result", "true");
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		//	System.out.println(rObj);
			return rObj;
		}
}
