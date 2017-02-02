package com.agiledge.atom.sms;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class SendSMSGSS implements Runnable, SendSMS {

	private String message;
	private String[] messages;
	int sleepTime;
	// ------- send to single
	String postData = "";
	String retval = "";
	 String User = "siemenstsplcri";
	 String passwd = "simen567";
	String mobilenumber;
	String[] mobilenumbers;
	String sid = "GSSTPS";
	String templateid = "174";
	String working_key = "A2758ec95e802a6e12067485df1f57441";
	String api="http";
		
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
			//this.mobilenumbers = mobileNo;
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
				if(messages.length==1)
				this.message = messages[0];
				else
					this.message = messages[i];
				
			
						postData +="?api="+ api + "&workingkey=" + working_key + "&to="	+ mobilenumber + "&sender=" + sid + "&message="	+ URLEncoder.encode(message, "UTF-8");	
					
						//postData="?username="+User+"&password="+passwd+"&to="+mobilenumber+"&from="+sid+"&text="+URLEncoder.encode(message, "UTF-8");;
			
				
					
				

					//URL url = new URL("http://www.myvaluefirst.com/smpp/sendsms"+ postData);
				
				URL url = new URL("http://sms.variformsolutions.co.in/apiv2/"+postData);
				System.out.println(url);
				HttpURLConnection urlconnection = (HttpURLConnection) url
							.openConnection();
				urlconnection.setRequestMethod("GET");
				urlconnection.setDoOutput(true);
				OutputStreamWriter out = new OutputStreamWriter(
						urlconnection.getOutputStream());
				out.write(postData);
				out.close();
				BufferedReader in = new BufferedReader(new InputStreamReader(
						urlconnection.getInputStream()));
				String decodedString;
				while ((decodedString = in.readLine()) != null) {
					retval = decodedString;
				}
				in.close();
				System.out.println(retval);
			}
		} catch (Exception e) {
			System.out.println("Error in runnnn" + e);
		}
	}

	
}
