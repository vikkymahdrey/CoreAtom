package com.agiledge.atom.mobile.service;

import java.util.ArrayList;
import java.util.Calendar;

import org.json.JSONArray;
import org.json.JSONObject;


import com.agiledge.atom.commons.OtherFunctions;
import com.agiledge.atom.constants.SettingsConstant;
import com.agiledge.atom.dao.EscortDao;
import com.agiledge.atom.dto.EscortDto;
import com.agiledge.atom.dto.TripDetailsChildDto;
import com.agiledge.atom.dto.TripDetailsDto;
import com.agiledge.atom.mobile.dao.MobileTripSheetDao;
import com.agiledge.atom.mobile.dao.TripServiceDao;
import com.agiledge.atom.service.SettingsService;
import com.agiledge.atom.settings.dto.DeviceConfigurationSettingsConstants;

public class TripSheetService {
	public JSONObject getTripSheet(String imei ) {
		JSONObject rObj = null;
		try {
			System.out.println("Before checkUserOTP ");
			TripDetailsDto dto = new MobileTripSheetDao().getVehicleTripSheetFromImei(imei );
			updateDistanceAndTime(dto);
			System.out.println("After checkUserOTP");
			if (dto != null) {
				System.out.println("::: " + dto.getId());
				new TripServiceDao().setTripAsDownloaded(dto.getId(), imei,
						dto.getVehicleNo());
			//	updateDistanceAndTime(dto);
			//	new TripServiceDao().getDriverNameAndMob(dto);
			//	new SMSService().sendPinSMS(dto);
			}
			rObj = new JSONObject();
			// rObj = new JSONObject(new
			// MobileTripSheetDao().getVehicleTripSheet(
			// imei, date, time, log));
			// System.out.println(rObj);
			rObj.put("ACTION", "gettrip");
			
			rObj.put("tripId", dto.getId());
			
			rObj.put("tripDate", dto.getTrip_date());
			rObj.put("tripCode", dto.getTrip_code());
			rObj.put("tripTime",  OtherFunctions. changeTimeFormat( dto.getTrip_time(),  "HH:mm", "hh:mm-a"));
			rObj.put("tripLog", dto.getTrip_log());
			rObj.put("vehicle", dto.getVehicle_type());
			rObj.put("status", OtherFunctions.isEmpty (dto.getStatus() )?"Initial":dto.getStatus() );
			
			rObj.put("distanceCovered", dto.getDistanceCovered());
			rObj.put("timeElapsed", dto.getTimeElapsed());
			System.out.println(dto.getStatus() + "puuting   getting"
					+ rObj.getString("status"));
			rObj.put("escort", dto.getEscort());
			rObj.put("isSecurity", dto.getIsSecurity());
			// configuration
			rObj.put("cometAuthType", "password type");
			rObj.put("driverName", dto.getDriverName());
			
			
			// configuration
	System.out.println(")))))))))))))))))))))))))00");
	
			rObj.put("result", "true");
			rObj.put("forceStopPin", "1234");
			SettingsService stService = new SettingsService();
			System.out.println("Ahi  " + stService.getSiteSetting(Integer.parseInt(dto.getSiteId()), DeviceConfigurationSettingsConstants.DOUBLE_AUTHENTICATION_FOR_EMPLOYEE_PICKUP));
			
			OtherFunctions.getBoolean( stService.getSiteSetting(Integer.parseInt(dto.getSiteId()),"PRE STOP DELAY FOR IN"));
			System.out.println("Ahi  " + OtherFunctions.getBoolean(""+ stService.getSiteSetting(Integer.parseInt(dto.getSiteId()), DeviceConfigurationSettingsConstants.DOUBLE_AUTHENTICATION_FOR_EMPLOYEE_PICKUP)));
			rObj.put("doubleAuthenticationForEmpPickup",OtherFunctions.getBoolean( stService.getSiteSetting(Integer.parseInt(dto.getSiteId()), DeviceConfigurationSettingsConstants.DOUBLE_AUTHENTICATION_FOR_EMPLOYEE_PICKUP)));
			rObj.put("doubleAuthenticationForEmpDrop",OtherFunctions.getBoolean( stService.getSiteSetting(Integer.parseInt(dto.getSiteId()), DeviceConfigurationSettingsConstants.DOUBLE_AUTHENTICATION_FOR_EMPLOYEE_DROP)));
			rObj.put("doubleAuthenticationForEscortPickup",OtherFunctions.getBoolean( stService.getSiteSetting(Integer.parseInt(dto.getSiteId()), DeviceConfigurationSettingsConstants.DOUBLE_AUTHENTICATION_FOR_ESCORT_PICKUP)));
			rObj.put("doubleAuthenticationForEscortDrop",OtherFunctions.getBoolean( stService.getSiteSetting(Integer.parseInt(dto.getSiteId()), DeviceConfigurationSettingsConstants.DOUBLE_AUTHENTICATION_FOR_ESCORT_DROP)));
			rObj.put("firstEscortAuthBeforeEmpAuthPickup",OtherFunctions.getBoolean( stService.getSiteSetting(Integer.parseInt(dto.getSiteId()), DeviceConfigurationSettingsConstants.FIRST_ESCORT_AUTH_BEFORE_EMP_AUTH_PICKUP)));
			rObj.put("secondEscortAuthAfterEmpAuthPickup",OtherFunctions.getBoolean( stService.getSiteSetting(Integer.parseInt(dto.getSiteId()), DeviceConfigurationSettingsConstants.SECOND_ESCORT_AUTH_AFTER_EMP_AUTH_PICKUP)));
			rObj.put("firstEscortAuthBeforeEmpAuthDrop",OtherFunctions.getBoolean( stService.getSiteSetting(Integer.parseInt(dto.getSiteId()), DeviceConfigurationSettingsConstants.FIRST_ESCORT_AUTH_BEFORE_EMP_AUTH_DROP)));
			rObj.put("secondEscortAuthAfterEmpAuthDrop",OtherFunctions.getBoolean( stService.getSiteSetting(Integer.parseInt(dto.getSiteId()), DeviceConfigurationSettingsConstants.SECOND_ESCORT_AUTH_AFTER_EMP_AUTH_DROP))); 

			JSONArray empNameArray = new JSONArray();
			JSONArray empCodeArray = new JSONArray();
			JSONArray aplArray = new JSONArray();
			JSONArray showStatusArray = new JSONArray();
			JSONArray securityUserNameArray = new JSONArray();
			JSONArray getInStatusArray = new JSONArray();
			JSONArray genderArray = new JSONArray();
			JSONArray emplatitude=new JSONArray();
			JSONArray emplongitudes=new JSONArray();
			JSONArray employeePinArray = new JSONArray();
			JSONArray empPickuptimings=new JSONArray();
			if( OtherFunctions.getBoolean(dto.getIsSecurity()) ) {
				employeePinArray.put(dto.getEscortPassword());
			}

			for (TripDetailsChildDto childDto : dto
					.getTripDetailsChildDtoList()) {
				empCodeArray.put(childDto.getEmployeeId());
				empNameArray.put(childDto.getEmployeeName());
				aplArray.put(childDto.getArea() + " " + childDto.getPlace()
						+ " " + childDto.getLandmark());
				showStatusArray.put(childDto.getShowStatus());
				getInStatusArray.put(childDto.getGetInStatus());
				genderArray.put(childDto.getGender());
				employeePinArray.put(childDto.getKeyPin());
				emplatitude.put(childDto.getLatitude());
				emplongitudes.put(childDto.getLongitude());
				empPickuptimings.put(childDto.getTime());

			}
			
			ArrayList<EscortDto> empList = new ArrayList<EscortDto>();
			EscortDto escortDto = new EscortDao()
					.getEscortByTripId(dto.getId());
			if (escortDto != null) {
				empList.add(escortDto);
   
				for (EscortDto emp : empList) {
					securityUserNameArray.put(emp.getEscortClock());
				}
			} else {
				securityUserNameArray.put("");
			}
			rObj.put("secUserName", securityUserNameArray);
			rObj.put("empName", empNameArray);
			rObj.put("empCode", empCodeArray);
			rObj.put("apls", aplArray);
			rObj.put("showStatus", showStatusArray);
			rObj.put("getInStatus", getInStatusArray);
			rObj.put("gender", genderArray);
			rObj.put("emplats", emplatitude);
			rObj.put("emplongs", emplongitudes);
			rObj.put("employeeKeyPins", employeePinArray);
			rObj.put("emptimes", empPickuptimings);

		} catch (Exception e) {
			System.out.println("Exception in TripSheetService.getTripSheet :"
					+ e);
		}
		System.out.println("trip returns" + rObj);
		return rObj;
	}
	
	 
	public JSONObject getTrips(String imei, String date) {
		JSONObject rObj = null;
		try {

			ArrayList<TripDetailsDto> dtoList = new MobileTripSheetDao()
					.getTrips(imei, date);
			TripDetailsDto[] dto = dtoList.toArray(new TripDetailsDto[dtoList
					.size()]);
		 
			JSONArray times = new JSONArray(dto);

			rObj = new JSONObject();
			rObj.put("ACTION", "gettrips");
			rObj.put("tripTime", times);
			System.out.println("Return Object :" + rObj.toString());
		} catch (Exception e) {
			System.out.println("Error : " + e);
		}
		return rObj;
	}

	public void updateDistanceAndTime(TripDetailsDto detailsDto) {

		Calendar cal = Calendar.getInstance();
		Calendar cal1 = Calendar.getInstance();

		System.out.println("time 1" + cal.getTime());
		String logTime;
		try {
			cal.setTime(OtherFunctions.sqlFormatToDate(detailsDto
					.getTrip_date()));
			logTime = detailsDto.getTrip_time();
			int hr = Integer.parseInt(logTime.split(":")[0]);
			int mnt = Integer.parseInt(logTime.split(":")[1]);
			float distance = (float) 0.0;
			cal.add(Calendar.HOUR, hr);
			cal.add(Calendar.MINUTE, mnt);
			cal1.setTime(cal.getTime());
			System.out.println(cal.getTime());
			for (int i = 0; i < detailsDto.getTripDetailsChildDtoList()
					.size(); i++) {

				distance += detailsDto.getTripDetailsChildDtoList().get(i)
						.getDistance();
				if (detailsDto.getTrip_log().equals("IN")) {
					if (detailsDto.getTripDetailsChildDtoList().get(i)
							.getTime().equals("0.0")) {
					} else {
						cal.setTime(cal1.getTime());
						cal.add(Calendar.MINUTE, -(int) Float.parseFloat(detailsDto.getTripDetailsChildDtoList().get(i).getTime()));
					}
				} else
					cal.add(Calendar.MINUTE,(int) Float.parseFloat(detailsDto.getTripDetailsChildDtoList().get(i).getTime()));						
				detailsDto.getTripDetailsChildDtoList().get(i).setDistance(distance);				
				detailsDto.getTripDetailsChildDtoList().get(i).setTime(OtherFunctions.getTimePartFromDate(cal.getTime()));
				System.out.println(detailsDto.getTripDetailsChildDtoList().get(i).getEmployeeName()+"   "+detailsDto.getTripDetailsChildDtoList().get(i).getTime());
			}

		} catch (Exception e) {
			System.out.println("error in changing time and dist" + e);
		}

	}

}
