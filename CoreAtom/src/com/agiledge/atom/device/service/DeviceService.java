package com.agiledge.atom.device.service;

import com.agiledge.atom.device.dao.DeviceDao;
import com.agiledge.atom.device.dto.DeviceDto;

public class DeviceService {

	DeviceDao dao = new DeviceDao();
	private String message;
	public int addUpdateDevice(DeviceDto dto) {
		System.out.println("TTTinsdie add updateDevinceTTTTT");
		int val =0;
		val = dao.addUpdateDevice(dto);
		setMessage(dao.getMessage());
		return val;
		
	}
	
	
	public int mapDeviceVehicleAuto(String imei, String driverUserName) {
		
		int val =0;
		val = dao.mapDeviceVehicleAuto(imei, driverUserName);
		setMessage(dao.getMessage());
		return val;
		
	}
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}


}
