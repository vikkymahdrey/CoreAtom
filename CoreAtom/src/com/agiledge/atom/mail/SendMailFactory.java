package com.agiledge.atom.mail;

import com.agiledge.atom.constants.SettingsConstant;

public class SendMailFactory {
 
	public static String mailFlag = SettingsConstant.emailFlag;
 

	private SendMailFactory() {

	}

	public static SendMail getMailInstance() {
		if (mailFlag.equalsIgnoreCase("gmail")) {

			return (SendMail) new SendGmail();
		}
		else if (mailFlag.equalsIgnoreCase("gss")) {

			return (SendMail) new SendGmail();
		}
		else if (mailFlag.equalsIgnoreCase("CGI")) {

			return (SendMail) new SendMailLogica();
		} else if (mailFlag.equalsIgnoreCase("cd")) {

			return (SendMail) new SendMailCrossDomain();

		}else
		{
			return (SendMail) new SendMailUAT();
		}


	}

}
