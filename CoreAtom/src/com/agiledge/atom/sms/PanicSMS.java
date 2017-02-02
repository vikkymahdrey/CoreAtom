/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.agiledge.atom.sms;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import com.agiledge.atom.mail.SendMail;
import com.agiledge.atom.mail.SendMailFactory;



public class PanicSMS implements Runnable {
	private String userName = "agiledge1";
	private String password = "543427914";
	private String senderName = "AGEDGE";
	private String mobileNo;
	private String mobileNos[];
	private String timeGaps[];
	private String email;
	private String emails[];
	private String message;
	private String tripId;
	int sleepTime;
	// ------- send to single

	public void sendRepeated(String mobileNo[],String [] emails, String message,
			String timeGap[], String tripId) {
		try {
			Thread t = new Thread(this);
			this.message = message;
			this.mobileNos = mobileNo;
			this.timeGaps = timeGap;
			this.emails=emails;
			this.tripId = tripId;
			t.start();
		} catch (Exception e) {
			System.out.println("error" + e);
		}
	}

	public void run() {

		try {

			for (int i = 0; i < timeGaps.length; i++) {
				if (SMSDao.checkClosed(tripId))
					return;
				else {
					this.mobileNo = mobileNos[i];
					this.email=emails[i];
					int time = Integer.parseInt(timeGaps[i]);
					int time1 = 0;
					if (i != 0) {
						time1 = Integer.parseInt(timeGaps[i - 1]);
					}
					sleepTime = time - time1;
				}
				Thread.sleep(sleepTime * 60 * 1000);				
				SendSMS sendSMS=SendSMSFactory.getSMSInstance();
				sendSMS.send(mobileNo, message);				
				SendMail sendmail=SendMailFactory.getMailInstance();
			sendmail.send(email, "Panic Alarm Pressed", message);
			}
		} catch (Exception e) {
			System.out.println("exception : " + e);
		}
	}

}
