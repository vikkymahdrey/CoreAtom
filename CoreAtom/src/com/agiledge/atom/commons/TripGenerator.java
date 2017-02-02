package com.agiledge.atom.commons;

import com.agiledge.atom.dao.SocketDeviceDao;

public class TripGenerator extends Thread {

	private String lattitude;
	private String longitude;
	private String deviceNo;

	public TripGenerator(String lattitude, String longitude, String deviceNo) {
		this.lattitude = lattitude;
		this.longitude = longitude;
		this.deviceNo = deviceNo;
	}

	@Override
	public void run() {
	try{
		System.out.println("Sand");
		
		new SocketDeviceDao().StartTrip(lattitude,longitude,deviceNo);
		Thread.currentThread().stop();
	}catch(Exception e){
		System.out.println("Error in TripGenerator"+e);
	}
	}
}
