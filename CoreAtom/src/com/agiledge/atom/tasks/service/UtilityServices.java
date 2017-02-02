package com.agiledge.atom.tasks.service;

import java.util.TimerTask;

public class UtilityServices extends TimerTask {
 
	Thread ut = null;
	public void run() {
		
		try{
			 ut = new Thread(new UtilitySupport());
			ut.setPriority(Thread.MAX_PRIORITY);
			ut.start();
			//System.out.println("no utility run");
			ut.join();
		}catch(Exception e) {
			System.out.println("Exception in Utility(Main) : "+ e);

		}		
	}
	
	
	

}
