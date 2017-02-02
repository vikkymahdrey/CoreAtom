package com.agiledge.atom.service;

import java.util.List;

import com.agiledge.atom.dao.NotificationDao;
import com.agiledge.atom.dao.SettingsDoa;
import com.agiledge.atom.dto.DriverDto;
import com.agiledge.atom.dto.VehicleDto;
import com.agiledge.atom.mail.SendMail;
import com.agiledge.atom.mail.SendMailFactory;


public class NotificationService {
	
	public List<VehicleDto> notificationForvehicles(String notificationFor){
		return new NotificationDao().notificationForvehicles(notificationFor);
	}
	public int mailNotificationForVehicleAlert(){
		List<VehicleDto> taxEndvehicleList=notificationForvehicles("tax");
		List<VehicleDto> insuranceEndvehicleList=notificationForvehicles("insurance");
		List<VehicleDto> pollutionEndvehicleList=notificationForvehicles("pollution");
		
		if(taxEndvehicleList!=null){
			String body="<table><th><td>Tax ExpiryDate</td><td>Vendor Company</td></th>";
			for(VehicleDto dto : taxEndvehicleList){
				body+="<tr><td>"+dto.getVehicleNo()+"</td><td>"
						+dto.getTaxtodate()+"</td><td>"
						+dto.getVendor()+"</td></tr>";
			}
			body+="</table>";
			String msg="Hi,<br/><br/>Tax Certificate of following Vehicles are expiring. Please renew the cetificate. <br/><br/>"
					+ body
					+ "<br/>Regards,<br/><a href='"
					+  new SettingsDoa().getDomainName()
					+ "'  >" + "Transport Team" + " </a>"
					+ "<br/>---------------<br/> <i><u>Note:</u> This is a system generated email. Please do not reply.</i>";
			SendMail mail= SendMailFactory.getMailInstance();
			String[]  mailaddress={"HARIHARAN.RAMAKRISHNAN@SIEMENS.COM"};
			String cc[]={"anand.s.ext@siemens.com"};
			String[] message={msg};
			mail.send( mailaddress,"Vehicle Tax Certificate End Notification", message,cc );
		}
		if(insuranceEndvehicleList!=null){
			String body="<table><th><td>Insurance ExpiryDate</td><td>Vendor Company</td></th>";
			for(VehicleDto dto : insuranceEndvehicleList){
				body+="<tr><td>"+dto.getVehicleNo()+"</td><td>"
						+dto.getInsuranceupto()+"</td><td>"
						+dto.getVendor()+"</td></tr>";
			}
			body+="</table>";
			String msg="Hi,<br/><br/>Insurance Certificate of following Vehicles are expiring. Please renew the cetificate. <br/><br/>"
					+ body
					+ "<br/>Regards,<br/><a href='"
					+  new SettingsDoa().getDomainName()
					+ "'  >" + "Transport Team" + " </a>"
					+ "<br/>---------------<br/> <i><u>Note:</u> This is a system generated email. Please do not reply.</i>";
			SendMail mail= SendMailFactory.getMailInstance();
			String[]  mailaddress={"HARIHARAN.RAMAKRISHNAN@SIEMENS.COM"};
			String cc[]={"anand.s.ext@siemens.com"};
			String[] message={msg};
			mail.send( mailaddress,"Vehicle Insurance Certificate End Notification", message,cc );
		}
		if(pollutionEndvehicleList!=null){
			String body="<table><th><td>PUC ExpiryDate</td><td>Vendor Company</td></th>";
			for(VehicleDto dto : pollutionEndvehicleList){
				body+="<tr><td>"+dto.getVehicleNo()+"</td><td>"
						+dto.getPollutionUpto()+"</td><td>"
						+dto.getVendor()+"</td></tr>";
			}
			body+="</table>";
			String msg="Hi,<br/><br/>PUC of following Vehicles are expiring. Please renew the cetificate. <br/><br/>"
					+ body
					+ "<br/>Regards,<br/><a href='"
					+  new SettingsDoa().getDomainName()
					+ "'  >" + "Transport Team" + " </a>"
					+ "<br/>---------------<br/> <i><u>Note:</u> This is a system generated email. Please do not reply.</i>";
			SendMail mail= SendMailFactory.getMailInstance();
			String[]  mailaddress={"HARIHARAN.RAMAKRISHNAN@SIEMENS.COM"};
			String cc[]={"anand.s.ext@siemens.com"};
			String[] message={msg};
			mail.send( mailaddress,"Vehicle PUC End Notification", message,cc );
		}
		return 1;
	}
	public int mailNotificationForDriverLisence(){
		
		List<DriverDto> list=new NotificationDao().notificationForDriverLisence();
		if(list.size()>0){
			String body="<table><th><td>Vendor</td><td>Lisence Number</td><td>Lisence ExpiryDate</td><td>Contact Number</td></th>";
			for(DriverDto dto : list){
				body+="<tr> <td>"+dto.getDriverName()+"</td><td>"
						+dto.getVendor()+"</td><td>"
						+dto.getLisence()+"</td><td>"
						+dto.getLisenceExpiryDt()+"</td><td>"
						+dto.getContactNo()
						+"</td></tr>";
			}
			body+="</table>";
			String msg="Hi,<br/><br/>Driving Lisence of following Drivers are expiring. Please renew the cetificate. <br/><br/>"
					+ body
					+ "<br/><br/>Regards,<br/><a href='"
					+  new SettingsDoa().getDomainName()
					+ "'  >" + "Transport Team" + " </a>"
					+ "<br/>---------------<br/> <i><u>Note:</u> This is a system generated email. Please do not reply.</i>";
			SendMail mail= SendMailFactory.getMailInstance();
			String[]  mailaddress={"HARIHARAN.RAMAKRISHNAN@SIEMENS.COM"};
			String cc[]={"anand.s.ext@siemens.com"};
			String[] message={msg};
			mail.send( mailaddress,"Driving Lisence Expiring Notification", message,cc );
		}
		return 1;
		
	}
}
