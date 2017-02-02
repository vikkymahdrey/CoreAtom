package com.agiledge.atom.tasks.service;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;

import com.agiledge.atom.service.SettingsService;


public class Scheduler {

	private static Scheduler i_scheduler;
	private Timer timer;
	private UtilityServices utilityServices = null;

	private Scheduler() {
		// new ConnectDB();
	}

	public static Scheduler getInstance() {
		if (i_scheduler == null) {
			i_scheduler = new Scheduler();
		}
		return i_scheduler;
	}

	public boolean startTasks() {
		try {

			// updatedatetime.set(Calendar.HOUR, 16);
			// updatedatetime.set(Calendar.MINUTE, 38);

			timer = new Timer();
			Calendar updatedatetime = Calendar.getInstance();
			String time[] = new SettingsService().getUtilityRunningTime();
			if (time != null) {
				try {
					updatedatetime.setTime(new Date());
					updatedatetime
							.set(Calendar.HOUR, Integer.parseInt(time[0]));
					updatedatetime.set(Calendar.MINUTE,
							Integer.parseInt(time[1]));
					updatedatetime.set(Calendar.SECOND,
							Integer.parseInt(time[2]));
					if (time[3].equals("AM")) {
						updatedatetime.set(Calendar.AM_PM, Calendar.AM);
					} else {
						updatedatetime.set(Calendar.AM_PM, Calendar.PM);
					}
				} catch (Exception e) {
					time = null;
				}

			}
			if (time == null) {

				updatedatetime.setTime(new Date());
				updatedatetime.set(Calendar.HOUR, 12);
				updatedatetime.set(Calendar.MINUTE, 02);
				updatedatetime.set(Calendar.SECOND, 00);
				updatedatetime.set(Calendar.AM_PM, Calendar.AM);
			}
			Date dt = updatedatetime.getTime();

			// System.out.println("date" + dt.toString());
			utilityServices = new UtilityServices();
			timer.scheduleAtFixedRate(utilityServices , dt,
					1000 * 60 * 60 * 24);
		} catch (Exception e) {
			System.out.println("ERROR" + e);
			return false;
		}
		return true;
	}

	public boolean endTasks() {
		try {
			utilityServices.cancel();
		}catch(Exception e) {}
		try {
			timer.cancel();
		}catch(Exception e) {}
		return true;
	}
}
