package com.agiledge.atom.sms;

import com.agiledge.atom.mail.SendMail;
import com.agiledge.atom.mail.SendMailFactory;

public class PanicForEmployee implements Runnable{
	private String empid;
	private String imei;
	private String mobileNo;
	private String mobileNos[];
	private String timeGaps[];
	private String email;
	private String emails[];
	private String message;
	private String tripId;
	int sleepTime;
	public void sendRepeated(String mobileNo[],String [] emails, String message,
			String timeGap[], String tripId,String imei,String empid) {
		try {
			Thread t = new Thread(this);
			this.message = message;
			this.mobileNos = mobileNo;
			this.timeGaps = timeGap;
			this.emails=emails;
			this.tripId = tripId;
			this.imei=imei;
			this.empid=empid;
			t.start();
		} catch (Exception e) {
			System.out.println("error" + e);
		}
	}
		public void run() {

			try {

				for (int i = 0; i < timeGaps.length; i++) {
					if (SMSDao.checkClosed( tripId))
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
				sendmail.send(email, "Panic Alarm Pressed In Employee App", message);
				}
			} catch (Exception e) {
				System.out.println("exception : " + e);
			}
		}
	}
