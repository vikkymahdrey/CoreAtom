package com.agiledge.atom.sms;

import java.net.URLEncoder;

public class SendSMSUAT implements SendSMS, Runnable {
	private String message;
	
	private String[] messages;
	int sleepTime;
	// ------- send to single
	String postData = "";
	String retval = "";
	// String User = "ATOm1";
	// String passwd = "atom9886021161!";
	String mobilenumber;
	String[] mobilenumbers;
	String sid = "CDdesk";
	String templateid = "174";
	String working_key = "16688se88mn67286f76b";
	
	@Override
	public void send(String mobileNo, String message) {
		try {
			Thread t = new Thread(this);
			this.message = message;
			this.mobilenumber = mobileNo;
			t.start();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("error" + e);
		}
	}
	@Override
	public void send(String[] mobileNo, String message) {
		try {
			Thread t = new Thread(this);
			this.message = message;
			this.mobilenumbers = mobileNo;
			t.start();
		} catch (Exception e) {
			System.out.println("error" + e);

		}

	}
	
	@Override
	public void send(String mobileNo[], String[] messages) {
		try {
			Thread t = new Thread(this);
			this.messages = messages;
			this.mobilenumbers = mobileNo;
			t.start();
		} catch (Exception e) {
			System.out.println("error" + e);

		}
	}
	
	public void run() {
		try {
			if (mobilenumbers == null || mobilenumbers.length < 1) {
				mobilenumbers=new String[1];
				mobilenumbers[0] = mobilenumber;
			}
			if (messages == null || messages.length < 1) {
				messages=new String[1];
				messages[0] = message;
			}

			for (int i = 0; i < mobilenumbers.length; i++) {
				this.mobilenumber = mobilenumbers[i];
				if(messages.length==0)
				this.message = messages[0];
				else
					this.message = messages[i];
				System.out.println(message);
				postData += "?workingkey=" + working_key + "&to="
						+ mobilenumber + "&sender=" + sid + "&message="+message;

				//System.out.println("http://sms.variformsolutions.co.in/api/web2sms.php"+ postData);
			}
		} catch (Exception e) {
			System.out.println("Error" + e);
		}
	}


}
