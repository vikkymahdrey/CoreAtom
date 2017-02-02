package com.agiledge.atom.sms;

import com.agiledge.atom.constants.SettingsConstant;
import com.agiledge.atom.mail.SendGmail;
import com.agiledge.atom.mail.SendMail;
import com.agiledge.atom.mail.SendMailCrossDomain;
import com.agiledge.atom.mail.SendMailLogica;
import com.agiledge.atom.mail.SendMailUAT;

public class SendSMSFactory {

	public static String smsFlag =SettingsConstant.smsFlag;



	private SendSMSFactory() {

	}

	public static SendSMS getSMSInstance() {
		if (smsFlag.equalsIgnoreCase("cd")||smsFlag.equalsIgnoreCase("cdt")) {

			return (SendSMS) new SendSMSCD();
		}else if(smsFlag.equalsIgnoreCase("Demo"))
		{
			return (SendSMS) new SendSMSDemo();
		}else if(smsFlag.equalsIgnoreCase("gss"))
		{
			return (SendSMS) new SendSMSGSS();	
		}else
		{
			return (SendSMS) new SendSMSUAT();
		}


	}
}
