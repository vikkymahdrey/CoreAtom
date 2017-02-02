package com.agiledge.atom.mobile.service;

import org.json.JSONException;
import org.json.JSONObject;



import com.agiledge.atom.constants.SettingsConstant;
import com.agiledge.atom.dao.DriverDAO;
import com.agiledge.atom.dao.SettingsDoa;
import com.agiledge.atom.dao.TripDetailsDao;
import com.agiledge.atom.device.dao.DeviceDao;
import com.agiledge.atom.device.dto.DeviceDto;
import com.agiledge.atom.dto.DriverDto;
import com.agiledge.atom.dto.SettingsDTO;
import com.agiledge.atom.mobile.dao.ValidateDriverDao;
import com.agiledge.atom.settings.dto.DeviceConfigurationSettingsConstants;
import com.itextpdf.text.log.SysoLogger;

public class LoginService {
	public JSONObject ValidateUser(String userName, String password, String imei) {
		JSONObject rObj = new JSONObject();
		try {
			boolean flag;
			rObj.put("ACTION", "login");
			logout(imei, userName);

			flag = new ValidateDriverDao().checkUser(userName, password, imei);
			if (flag) {
				autoMapDeviceAndVehicle(userName , imei);
				rObj.put("result", "true");
			} else {
				rObj.put("result", "false");
			}
		} catch (Exception e) {
			System.out.println("Exception in lOgin service" + e);
		}
		return rObj;
	}
	
	public void autoMapDeviceAndVehicle(String userName , String imei) {
		 try {
			 System.out.println(" Inside autoMapDeviceAndVehicle . . . ");
		SettingsDTO settingDto = new SettingsDTO();
		settingDto.setProperty(DeviceConfigurationSettingsConstants.AUTOMATIC_DEVICE_VEHICLE_MAPPING);
		settingDto.setModule(SettingsConstant.TRACKING_MODULE);
		settingDto = new SettingsDoa().getSettingsValue(settingDto);
		System.out.println(DeviceConfigurationSettingsConstants.AUTOMATIC_DEVICE_VEHICLE_MAPPING + " : " + settingDto.getValue());
		if(settingDto.getValue().trim().equalsIgnoreCase("YES")) {
			DeviceDto deviceDto = new DeviceDto();
			deviceDto.setImei(imei);
			deviceDto.setModel("UNKNOWN");
			deviceDto.setCompany("UNKNOWN");
			 DeviceDao  deviceDao = new DeviceDao();
				System.out.println("addUpdate Device invokation");
			deviceDao.addUpdateDevice(deviceDto);
			System.out.println("Map Device invokation");
			deviceDao.mapDeviceVehicleAuto(imei, userName);
			
		}
		 }catch(NullPointerException ignore) {
			 
		 }
	}

	public boolean CheckLogged(String imei) {
		/*try {
			System.out.println("..............");
			if (new ValidateDriverDao().checkLogged(imei)) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			System.out.println("Exception in lOgin service " + e);
			return false;
		}*/
		return true;
	}

	public boolean authenticateEmployee(String tripId, String empCode,
			String passwpord) throws JSONException {
		
		JSONObject rObj = new JSONObject();
		try {
			if (new TripDetailsDao().authenticateEmployee(tripId, empCode,
					passwpord) > 0) {
				return true;
			} else

			{
				return false;
				
			}

		} catch (Exception e) {
			System.out.println("Error" + e);

		}
		return false;
	}

	public JSONObject logout(String imei) {
		JSONObject rObj = new JSONObject();
		try {
			int flag;
			rObj.put("ACTION", "logout");
			flag = new ValidateDriverDao().logout(imei);
			if (flag != 0) {
				rObj.put("result", "true");
			} else {
				rObj.put("result", "false");
			}
		} catch (Exception e) {
			System.out.println("Exception in lOgin service" + e);
		}
		return rObj;
	}

 
	public JSONObject logout(String imei, String userName) {
		JSONObject rObj = new JSONObject();
		try {
			int flag;
			rObj.put("ACTION", "logout");
			flag = new ValidateDriverDao().logout(imei, userName);
			if (flag != 0) {
				rObj.put("result", "true");
			} else {
				rObj.put("result", "false");
			}
		} catch (Exception e) {
			System.out.println("Exception in lOgin service" + e);
		}
		return rObj;
	}
	
	public JSONObject logoutOTP(String imei, String userName) {
		JSONObject rObj = new JSONObject();
		try {
			int flag;
			rObj.put("ACTION", "logout");
			flag = new ValidateDriverDao().logoutImei(imei );
			if (flag != 0) {
				rObj.put("result", "true");
			} else {
				rObj.put("result", "false");
			}
		} catch (Exception e) {
			System.out.println("Exception in lOgin service" + e);
		}
		return rObj;
	}


	public JSONObject ValidateUserOTP(String password, String imei) {
		// TODO Auto-generated method stub
		JSONObject rObj = new JSONObject();
		try {
			boolean flag;
			 System.out.println(" inside ValidateUserOTP..");
			logout(imei);
			DriverDto dto =  new ValidateDriverDao().checkUserOTP(password, imei);
			if (dto!=null) {
				System.out.println("User Name : " + dto.getUsername());
				autoMapDeviceAndVehicle(dto.getUsername() , imei);
				rObj =new TripSheetService(). getTripSheet(imei);
				 
				 
//			 System.out.println("Action : " + rObj.get("action"));
			} else {
				rObj.put("ACTION", "loginOTP");
				rObj.put("result", "false");
			}
		} catch (Exception e) {
			System.out.println("Exception in lOgin service" + e);
		}
		return rObj;

 
	}
}