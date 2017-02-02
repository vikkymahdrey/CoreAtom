package com.agiledge.atom.commons;

import com.agiledge.atom.service.SocketDeviceService;

public class StoppingThread extends Thread {

	private String deviceNo;
	private String lat;
	private String lng;

	public StoppingThread(String deviceNo, String lat, String lng) {
		this.deviceNo = deviceNo;
		this.lat = lat;
		this.lng = lng;
		this.start();
	}

	@Override
	public void run() {
		try{
		System.out.println("Started stoppng");
		int sent= new SocketDeviceService().vehicleAlertSms(deviceNo, lat, lng);
		System.out.println(sent+"message sent for vehicle alert");
		new SocketDeviceService().stopTrip(deviceNo, lat, lng);
		Thread.currentThread().stop();
		}catch(Exception e){System.out.println("Error in StoppingThread"+e);}
	}
}
