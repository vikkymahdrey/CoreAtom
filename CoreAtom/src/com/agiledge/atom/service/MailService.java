/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.agiledge.atom.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import javax.mail.internet.AddressException;

import org.apache.commons.collections.MultiMap;
import org.apache.commons.collections.map.MultiValueMap;

import com.agiledge.atom.commons.OtherFunctions;
import com.agiledge.atom.constants.SettingsConstant;
import com.agiledge.atom.dao.AdhocDao;
import com.agiledge.atom.dao.EmployeeDao;
import com.agiledge.atom.dao.MailDao;
import com.agiledge.atom.dao.SettingsDoa;
import com.agiledge.atom.dto.AdhocDto;
import com.agiledge.atom.dto.DelegateRoleDto;
import com.agiledge.atom.dto.EmployeeDto;
import com.agiledge.atom.dto.EmployeeSubscriptionDto;
import com.agiledge.atom.dto.ScheduleAlterDto;
import com.agiledge.atom.dto.ScheduleMailDto;
import com.agiledge.atom.mail.SendGmail;
import com.agiledge.atom.mail.SendMail;
import com.agiledge.atom.mail.SendMailFactory;



/**
 * 
 * @author 123
 */
public class MailService {

	MailDao dao = new MailDao();

	// to retrive schedulte list based on subscription id
	public ScheduleMailDto getScheduleListMessage(String subscriptionID) {
		ScheduleMailDto message;
		message = dao.getScheduleListMessage(subscriptionID);
		return message;
	}

	public String getAlteredScheduleListMessage(
			ArrayList<ScheduleAlterDto> scheduleAlterDtoList, String user) {
		return new SettingsService().getAlteredScheduleListMessage(
				scheduleAlterDtoList, user);

	}

	public String getEmployeeEmailByScheduleId(String scheduleID) {

		String email = "";
		dao.getEmployeeEmailByScheduleId(scheduleID);
		return scheduleID;
	}

	public void sendAlteredScheduleMail(
			ArrayList<ScheduleAlterDto> scheduleAlterDtoList, String user) {
		try {
			String message = getAlteredScheduleListMessage(
					scheduleAlterDtoList, user);
			String scheduleID = "";
			if (scheduleAlterDtoList.size() > 0) {
				ScheduleAlterDto dto = new ScheduleAlterDto();
				dto = scheduleAlterDtoList.get(0);
				scheduleID = dto.getScheduleId();
			}
			String email = getEmployeeEmailByScheduleId(scheduleID);
			SendMail mail = SendMailFactory.getMailInstance();
			mail.send(email, "Transport Schedule Modification", message);
		} catch (Exception ex) {
			System.out.println("Sending Error : " + ex);
		}

	}

	// to get email address of employees whose based on booking expiry date in
	// service

	public void sentEmailNearToBookingExpiry(String today)
			throws AddressException {

		String date1 = today;

		String d[] = today.split("/");
		Calendar cal = Calendar.getInstance();
		cal.set(cal.DATE, Integer.parseInt(d[1]));
		cal.set(cal.MONTH, Integer.parseInt(d[0]) - 1);
		cal.set(cal.YEAR, Integer.parseInt(d[2]));

		cal.add(cal.DATE, 2);
		int month = cal.get(cal.MONTH);
		month++;
		String date2 = month + "/" + cal.get(cal.DATE) + "/"
				+ cal.get(cal.YEAR);

		List<EmployeeSubscriptionDto> dtolist = dao
				.getEmployeesNearToBookingExpiry(date1, date2);

		// ------------

		// /-------

		MultiMap supervisorMap = new MultiValueMap();
		MultiMap spocMap = new MultiValueMap();
		for (EmployeeSubscriptionDto dto : dtolist) {
			EmployeeDto employee = dto.getEmployee();
			EmployeeDto supervisor = dto.getSupervisor();
			EmployeeDto spoc = dto.getSpoc();

			// System.out.println( spoc.getEmployeeID() + "-->Supervisor " +
			// dto.getSupervisor().getEmployeeFirstName() + " " +
			// dto.getSupervisor().getEmployeeLastName());
			// System.out.println(supervisor.getEmployeeID() + "-->Spoc " +
			// dto.getSpoc().getEmployeeFirstName() + " " +
			// dto.getSpoc().getEmployeeLastName());

			spocMap.put(spoc.getEmployeeID(), dto);
			supervisorMap.put(supervisor.getEmployeeID(), dto);

		}
		// System.out.println("-----------------------------------supervisor---------------------------------");
		// to send mail to supervisor
		Iterator i = supervisorMap.entrySet().iterator();

		while (i.hasNext()) {
			Entry entry = (Entry) i.next();
			String key = (String) entry.getKey();
			// System.out.println("Supervisor : key : " + key);
			List<EmployeeSubscriptionDto> dtoList = (List<EmployeeSubscriptionDto>) entry
					.getValue();
			String message = " The following employees schedules are near to expiry. Please have a look \n"
					+ "Employee                              Expiry Date \n"
					+ "----------------------------------------------------";
			String details = "";
			String supervisorEmail = "";
			String supervisorName = "";
			for (EmployeeSubscriptionDto dto : dtoList) {
				EmployeeDto employee = dto.getEmployee();
				details = details + "\n" + employee.getEmployeeFirstName()+ ",  "
						+ dto.getUnsubscriptionDate();
				if (supervisorName.equals("")) {
					EmployeeDto supervisor = dto.getSupervisor();

					supervisorName = supervisor.getEmployeeFirstName();
					supervisorEmail = supervisor.getEmailAddress();

				}

			}

			if (!details.equals("")) {
				message = "Hi " + supervisorName + message + details;
				/*
				 * SendMail sendmail = SendMailFactory.getMailInstance(); try {
				 * sendmail.send(supervisorEmail, "Booking Expiry Notification",
				 * message); } catch ( Exception ex) {
				 * Logger.getLogger(MailService
				 * .class.getName()).log(Level.SEVERE, null, ex); }
				 */
				// System.out.println("Supervisor : " + supervisorEmail +
				// message);
			}

		}

		// System.out.println("-----------------------------------spoc---------------------------------");
		// to send mail to spocs
		i = spocMap.entrySet().iterator();
		while (i.hasNext()) {
			Entry entry = (Entry) i.next();
			String key = (String) entry.getKey();
			List<EmployeeSubscriptionDto> dtoList = (List<EmployeeSubscriptionDto>) entry
					.getValue();
			String message = " The following employees are near to boooking expiry. Please have a look \n"
					+ "Employee                              Expiry Date \n"
					+ "----------------------------------------------------";
			String details = "";
			String spocEmail = "";
			String spocName = "";
			for (EmployeeSubscriptionDto dto : dtoList) {
				EmployeeDto employee = dto.getEmployee();
				details = details + "\n" + employee.getEmployeeFirstName()+ ",  "
						+ dto.getUnsubscriptionDate();
				if (spocName.equals("")) {
					EmployeeDto spoc = dto.getSpoc();

					spocName = spoc.getEmployeeFirstName();
					spocEmail = spoc.getEmailAddress();

				}

			}

			if (!details.equals("")) {
				message = "Hi " + spocName + message + details;
				/*
				 * SendMail sendmail = SendMailFactory.getMailInstance(); try {
				 * sendmail.send(spocEmail, "Booking Expiry Notification",
				 * message); } catch ( Exception ex) {
				 * Logger.getLogger(MailService
				 * .class.getName()).log(Level.SEVERE, null, ex); }
				 */
				// System.out.println("spoc : " + spocEmail + "\n " + message);
			}

		}

	}

	public String getEmailAddressOfEmployeesWhoUnsubscribed(String from_date) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void sendMailToTmDueToInappropriateSelectionOfManager(EmployeeSubscriptionDto dto, String mngrType, String spocType)
	{
		try{
			EmployeeDto emp= new EmployeeDao().getEmployeeAccurate(dto.getEmployeeID());
			EmployeeDto ta= new EmployeeDao().getOne_Employee("ta");
			EmployeeDto manager=new EmployeeDao().getEmployeeAccurate(dto.getSupervisor1());
			EmployeeDto spoc=new EmployeeDao().getEmployeeAccurate(dto.getSupervisor2());
			mngrType=mngrType==null?"":mngrType;
			spocType=spocType==null?"":spocType;
			if((mngrType+spocType).equals("")==false)
			{
				String mngText="";
				String spocText="";
				String commonText="<tr><th></th><th>Staff#</th><th>Full Name</th></tr>";
				if(mngrType.trim().equals("")==false)
				{
					
					mngText="<tr><td><b>Manager</b></td><td>" +manager.getPersonnelNo() +
							"</td>" +
							"<td>" +manager.getDisplayName() +
							"</td></tr>";
					 
					
				}
				if(spocType.trim().equals("")==false)
				{

					spocText="<tr><td><b>SPOC</b></td><td>" +spoc.getPersonnelNo() +
							"</td>" +
							"<td>" +spoc.getDisplayName() +
							"</td></tr>";
				}
				String message="Hi " + ta.getDisplayName() +
						".<br/><br/>"+
						"The employee \""+ emp.getDisplayName() +"\" has selected below Manager/SPOC who does not have access to HRM. " 
						+ "<table border='1' >"+ commonText +mngText +spocText+
						"</table><br/><br/>"
						+ "Regards,<br/>" +
						" <a href='"
						+ new SettingsDoa().getDomainName()
						+"'>" 
						+ "Transport Team."
						+"</a>"
						+"<br/>---------------<br/> <i><u>Note:</u> This is a system generated email. Please do not reply.</i>";
	
				String title="New Manager/SPOC Notification";
				SendMail mail = SendMailFactory.getMailInstance();
				String cc[]={""};
				 ArrayList<EmployeeDto> tms = new EmployeeDao().getEmployeesByRole("ta");
					
					cc = OtherFunctions.appendArray(cc, new EmployeeService().getEmails(tms));
					for(EmployeeDto ta1: tms)
					{
					cc = OtherFunctions.appendArray(cc, new EmployeeService()
							.getEmails(new EmployeeDao().getDelegatedEmployees(ta1
									.getEmployeeID())));
					}
				
					 ArrayList<EmployeeDto> tmms = new EmployeeDao().getEmployeesByRole("tm");
						 
						cc = OtherFunctions.appendArray(cc, new EmployeeService().getEmails(tmms));
						for(EmployeeDto tmm: tms)
						{
						cc = OtherFunctions.appendArray(cc, new EmployeeService()
								.getEmails(new EmployeeDao().getDelegatedEmployees(tmm
										.getEmployeeID())));
						}
					
				String to=ta.getEmailAddress();
				mail.setCcs(cc);
				if((mngText+spocText).equals("")==false)
				mail.send(to, title, message);
			}
			
		}catch(Throwable e)
		{
			System.out.println("Exception in MailService.sendMailToTmDueToInappropriateSelectionOfManager : "+ e);
		}
	}
	
	
	public void sendRoleDelegationMail(
			 DelegateRoleDto dto, String user) {
		try {
			
			EmployeeDto giver= new EmployeeDao().getEmployeeAccurate(dto.getActualEmpId());
			EmployeeDto reciever= new EmployeeDao().getEmployeeAccurate(dto.getDelegatedEmpId());
			String message = "Hi "+ reciever.getDisplayName() + ". <br/>You have been delegated as " + OtherFunctions.getRoleNameById(dto.getDelegatedEmpId())+ " by " + giver.getDisplayName()+". The delegation will be valid from " + dto.getFromDate() + " up to " + dto.getToDate() + " only.<br/><br/>"+

					"Regards,<br/>" +
					" <a href='"
					+ new SettingsDoa().getDomainName()
					+"'>" 
					+ "Transport Team."
					+"</a>"
					+"<br/>---------------<br/> <i><u>Note:</u> This is a system generated email. Please do not reply.</i>";
 			 
			SendMail mail = SendMailFactory.getMailInstance();
			 
			 ArrayList<EmployeeDto> tms = new EmployeeDao().getEmployeesByRole("ta");
				String bccs[] = new String[0];
				bccs = OtherFunctions.appendArray(bccs, new EmployeeService().getEmails(tms));
				for(EmployeeDto tm: tms)
				{
				bccs = OtherFunctions.appendArray(bccs, new EmployeeService()
						.getEmails(new EmployeeDao().getDelegatedEmployees(tm
								.getEmployeeID())));
				}
			
			 
		 mail.setBccs(bccs);
			mail.send(reciever.getEmailAddress(), "Role Delegation Notification", message);
		} catch (Exception ex) {
			System.out.println("Sending Error : " + ex);
		}

	}

	public void sendmailForAdhocBooking(AdhocDto dto) {
		System.out.println("Mailings");
	
		if (dto != null) {
			EmployeeDto emp = new EmployeeDao().getEmployeeAccurate(dto.getBookedFor());
			System.out.println(emp + "emp in mail");
			String title = "Adhoc Notification";

			String address = emp.getEmailAddress();
			System.out.println(address + "address of emp in mail");
			/*
			Hi! <Emp Name>
			The Ad-Hoc request for Travel on < Date> and  <Time> is created and awaiting approval as given below
			Create For: 		<Emp Name>
			Created by:	 	< Name>
			Kindly ensure approval for request fulfillment

			For any change, please do contact your reporting officer/SPOC

			Do use the following link to access the ATOm

			For any queries, call transport help desk at <974025575> or mail us at transport@cross-domain.com

			Thanks & Regards,
			Transport Team
			transport@cross-domain.com
			Note: This is a system generated email. Please do not reply.

			*/
			
			// EmployeeDto spoc =
			EmployeeDto sup = new EmployeeDao().getEmployeeAccurate(dto.getBookedBy());
			//EmployeeDto pm = new EmployeeDao().getEmployeeAccurate(dto.getBookedFor());
			
			EmployeeDto pm = new EmployeeDao().getEmployeeAccurate(sup.getLineManager());
			
			String supEmail = sup.getEmailAddress();
			String pmEmail = pm.getEmailAddress();
			
			
			//ArrayList<EmployeeDto> spocDelegates= new EmployeeDao().getDelegatedEmployees(spoc.getEmployeeID());
			ArrayList<EmployeeDto> supDelegates= new EmployeeDao().getDelegatedEmployees(sup.getEmployeeID());
			
			String msg = "Hi "+ emp.getDisplayName()+ "!<br/>"
					+"The Ad-Hoc request for Travel on " + dto.getTravelDate()+" and "+ dto.getStartTime()+" is created and awaiting approval as given below.\n"
					+"<br/>Created For: " + emp.getDisplayName()+"<br/>Created by: "+ sup.getDisplayName()+"\n"
					+"<br/>Kindly ensure approval for request fulfillment \n\n"
					
					+ "<br/><br/>For any change, please do contact your reporting officer/SPOC \n"
					+ "<br/>Do use the following link to access the ATOm" + " <a href='"
					+ new SettingsDoa().getDomainName()
					+"'>"+new SettingsDoa().getDomainName()+"</a><br/>"
					+ "For any queries, call transport help desk at "+SettingsConstant.transportTeamNumber+" or mail us at "+SettingsConstant.transportTeamEmail+" \n\n\n"
					+ "<br/><br/>Thanks & Regards, \n" 
					+ "<br/>Transport Team \n"
					+ "<br/>"+SettingsConstant.transportTeamEmail+" \n"
					+ "<br/>---------------<br/> <i><u>Note:</u> This is a system generated email. Please do not reply.</i>";
					
			String cc[]=new String[2];
			//cc[0]=spocEmail;
			cc[0]=supEmail;
			cc[1]=pmEmail;
			//cc=OtherFunctions.appendArray(cc, new EmployeeService().getEmails(spocDelegates));
		
			cc=OtherFunctions.appendArray(cc, new EmployeeService().getEmails(supDelegates));
			 ArrayList<EmployeeDto> tms = new EmployeeDao().getEmployeesByRole("tm");
			 ArrayList<EmployeeDto> tas = new EmployeeDao().getEmployeesByRole("ta");
			 ArrayList<EmployeeDto> all = new ArrayList<EmployeeDto>();
			 all.addAll(tas);
			 all.addAll(tms);
				String bccs[] = new String[0];
				bccs = OtherFunctions.appendArray(bccs, new EmployeeService().getEmails(all));
				for(EmployeeDto tm: all)
				{
				bccs = OtherFunctions.appendArray(bccs, new EmployeeService()
						.getEmails(new EmployeeDao().getDelegatedEmployees(tm
								.getEmployeeID())));
				
				}
			
					
			SendMail mail=SendMailFactory.getMailInstance();
			mail.setCcs(cc);
			mail.setBccs(bccs);
			mail.send (address, title, msg);
			 

		}
	}

	
	public void sendmailForAdhocBookingApproval(String adhocId, String approverId,
			String update)
	
	{
		System.out.println("Mailings");
		
		if (adhocId != null) {
			AdhocDto dto = new AdhocDao().getBookingDetails(adhocId);;
		
			EmployeeDto emp = new EmployeeDao().getEmployeeAccurate(dto.getEmployeeId());
			System.out.println(emp + "emp in mail");
			String title = "Adhoc Notification";

			String address = emp.getEmailAddress();
			System.out.println(address + "address of emp in mail");
			/*
			Hi! <Emp Name>
			The Ad-Hoc request for Travel on < Date> and  <Time> is created and awaiting approval as given below
			Create For: 		<Emp Name>
			Created by:	 	< Name>
			Kindly ensure approval for request fulfillment

			For any change, please do contact your reporting officer/SPOC

			Do use the following link to access the ATOm

			For any queries, call transport help desk at <974025575> or mail us at transport@cross-domain.com

			Thanks & Regards,
			Transport Team
			transport@cross-domain.com
			Note: This is a system generated email. Please do not reply.

			*/
			
			// EmployeeDto spoc =
			EmployeeDto sup = new EmployeeDao().getEmployeeAccurate(dto.getBookedBy());
			//EmployeeDto pm = new EmployeeDao().getEmployeeAccurate(dto.getBookedFor());
			
			EmployeeDto pm = new EmployeeDao().getEmployeeAccurate(dto.getApprovedBy());
			
			String supEmail = sup.getEmailAddress();
			String pmEmail = pm.getEmailAddress();
			
			
			//ArrayList<EmployeeDto> spocDelegates= new EmployeeDao().getDelegatedEmployees(spoc.getEmployeeID());
			ArrayList<EmployeeDto> supDelegates= new EmployeeDao().getDelegatedEmployees(sup.getEmployeeID());
			
			String msg = "Hi "+ emp.getDisplayName()+ "!"
					+"<br/> Your ad-hoc request for the transport facility has been " +update+ " by "  + pm.getDisplayName() +", with travel time "+ dto.getStartTime()+" on " +dto.getTravelDate()+
					 "<br/><br/>For any change, please do contact your reporting officer/SPOC"
					+ "<br/>Do use the following link to access the ATOm" + " <a href='"
					+ new SettingsDoa().getDomainName()
					+"'>"+new SettingsDoa().getDomainName()+" </a>\n"
					+ "<br/>For any queries, call transport help desk at "+SettingsConstant.transportTeamNumber+" or mail us at "+SettingsConstant.transportTeamEmail+"\n\n\n"
					+ "<br/><br/>Thanks & Regards, \n" 
					+ "<br/>Transport Team \n"
					+ "<br/>"+SettingsConstant.transportTeamEmail+" \n"
					+ "<br/>---------------<br/> <i><u>Note:</u> This is a system generated email. Please do not reply.</i>";
			String cc[]=new String[2];
			//cc[0]=spocEmail;
			cc[0]=supEmail;
			cc[1]=pmEmail;
			//cc=OtherFunctions.appendArray(cc, new EmployeeService().getEmails(spocDelegates));
			cc=OtherFunctions.appendArray(cc, new EmployeeService().getEmails(supDelegates));
			 ArrayList<EmployeeDto> tms = new EmployeeDao().getEmployeesByRole("tm");
			 ArrayList<EmployeeDto> tas = new EmployeeDao().getEmployeesByRole("ta");
			 ArrayList<EmployeeDto> all = new ArrayList<EmployeeDto>();
			 all.addAll(tas);
			 all.addAll(tms);
				String bccs[] = new String[0];
				bccs = OtherFunctions.appendArray(bccs, new EmployeeService().getEmails(all));
				for(EmployeeDto tm: all)
				{
				bccs = OtherFunctions.appendArray(bccs, new EmployeeService()
						.getEmails(new EmployeeDao().getDelegatedEmployees(tm
								.getEmployeeID())));
				
				}
			
					
			SendMail mail=SendMailFactory.getMailInstance();
			mail.setCcs(cc);
			mail.setBccs(bccs);
			mail.send (address, title, msg);
			 

	
	}
	
	
	
	
}
	
	public void sendmailForAdhocBookingModification(AdhocDto dto)
	{
		System.out.println("Mailings");
		
		if (dto != null) {
			AdhocDto dto2 = new AdhocDao().getBookingDetails(dto.getBookingId());
			
			//EmployeeDto emp = new EmployeeDao().getEmployeeAccurate(dto.getBookingId()());
			
			EmployeeDto emp = new EmployeeDao().getEmployeeAccurate(dto2.getEmployeeId());
			System.out.println(dto.toString());
			System.out.println(emp + "emp in mail");
			String title = "Adhoc Notification";

			String address = emp.getEmailAddress();
			System.out.println(address + "address of emp in mail");
			/*
			Hi! <Emp Name>
			The Ad-Hoc request for Travel on < Date> and  <Time> is created and awaiting approval as given below
			Create For: 		<Emp Name>
			Created by:	 	< Name>
			Kindly ensure approval for request fulfillment

			For any change, please do contact your reporting officer/SPOC

			Do use the following link to access the ATOm

			For any queries, call transport help desk at <974025575> or mail us at transport@cross-domain.com

			Thanks & Regards,
			Transport Team
			transport@cross-domain.com
			Note: This is a system generated email. Please do not reply.

			*/
			
			// EmployeeDto spoc =
			EmployeeDto sup = new EmployeeDao().getEmployeeAccurate(dto.getBookedBy());
			//EmployeeDto pm = new EmployeeDao().getEmployeeAccurate(dto.getBookedFor());
			
			EmployeeDto pm = new EmployeeDao().getEmployeeAccurate(sup.getLineManager());
			
			String supEmail = sup.getEmailAddress();
			String pmEmail = pm.getEmailAddress();
			
			
			//ArrayList<EmployeeDto> spocDelegates= new EmployeeDao().getDelegatedEmployees(spoc.getEmployeeID());
			ArrayList<EmployeeDto> supDelegates= new EmployeeDao().getDelegatedEmployees(sup.getEmployeeID());
			
			String msg = "Hi "+ emp.getDisplayName()+ "!\n"
					+"<br/>The Ad-Hoc request has been modified to " + dto.getTravelDate()+" and "+ dto.getStartTime()+" and awaiting approval as given below.\n"
					+"<br/>Created For: " + emp.getDisplayName()+"\n"
					+"<br/>Modified by: "+ sup.getDisplayName()+"\n"
					+"<br/>Kindly ensure approval for request fulfillment \n\n"
					
					+ "<br/><br/>For any change, please do contact your reporting officer/SPOC \n"
					+ "<br/>Do use the following link to access the ATOm" + " <a href='"
					+ new SettingsDoa().getDomainName()
					+"'>"+new SettingsDoa().getDomainName()+"</a> \n"
					+ "<br/>For any queries, call transport help desk at "+SettingsConstant.transportTeamNumber+" or mail us at "+SettingsConstant.transportTeamEmail+" \n\n\n"
					+ "<br/><br/>Thanks & Regards, \n" 
					+ "<br/>Transport Team \n"
					+ "<br/>"+SettingsConstant.transportTeamEmail+"\n"
					+ "<br/>---------------<br/> <i><u>Note:</u> This is a system generated email. Please do not reply.</i>";
					
			String cc[]=new String[2];
			//cc[0]=spocEmail;
			cc[0]=supEmail;
			cc[1]=pmEmail;
			//cc=OtherFunctions.appendArray(cc, new EmployeeService().getEmails(spocDelegates));
			cc=OtherFunctions.appendArray(cc, new EmployeeService().getEmails(supDelegates));
			 ArrayList<EmployeeDto> tms = new EmployeeDao().getEmployeesByRole("tm");
			 ArrayList<EmployeeDto> tas = new EmployeeDao().getEmployeesByRole("ta");
			 ArrayList<EmployeeDto> all = new ArrayList<EmployeeDto>();
			 all.addAll(tas);
			 all.addAll(tms);
				String bccs[] = new String[0];
				bccs = OtherFunctions.appendArray(bccs, new EmployeeService().getEmails(all));
				for(EmployeeDto tm: all)
				{
				bccs = OtherFunctions.appendArray(bccs, new EmployeeService()
						.getEmails(new EmployeeDao().getDelegatedEmployees(tm
								.getEmployeeID())));
				
				}
			
					
			SendMail mail=SendMailFactory.getMailInstance();
			mail.setCcs(cc);
			mail.setBccs(bccs);
			mail.send (address, title, msg);
			 

		}
		
	}
	public void sendmailForAdhocBookingCancellation(String adhocId, String isShiftExt,
			String alterId,String user)
	{
	System.out.println("Mailings");
		
		if (adhocId != null) {
			AdhocDto dto = new AdhocDao().getBookingDetails(adhocId);;
		
			EmployeeDto emp = new EmployeeDao().getEmployeeAccurate(dto.getEmployeeId());
			EmployeeDto emp2 = new EmployeeDao().getEmployeeAccurate(user);
			
			System.out.println(emp + "emp in mail");
			String title = "Adhoc Notification";

			String address = emp.getEmailAddress();
			System.out.println(address + "address of emp in mail");
			/*
			Hi! <Emp Name>
			The Ad-Hoc request for Travel on < Date> and  <Time> is created and awaiting approval as given below
			Create For: 		<Emp Name>
			Created by:	 	< Name>
			Kindly ensure approval for request fulfillment

			For any change, please do contact your reporting officer/SPOC

			Do use the following link to access the ATOm

			For any queries, call transport help desk at <974025575> or mail us at transport@cross-domain.com

			Thanks & Regards,
			Transport Team
			transport@cross-domain.com
			Note: This is a system generated email. Please do not reply.

			*/
			
			// EmployeeDto spoc =
			EmployeeDto sup = new EmployeeDao().getEmployeeAccurate(dto.getBookedBy());
			//EmployeeDto pm = new EmployeeDao().getEmployeeAccurate(dto.getBookedFor());
			
			EmployeeDto pm = new EmployeeDao().getEmployeeAccurate(sup.getLineManager());
			
			String supEmail = sup.getEmailAddress();
			String pmEmail = pm.getEmailAddress();
			
			
			//ArrayList<EmployeeDto> spocDelegates= new EmployeeDao().getDelegatedEmployees(spoc.getEmployeeID());
			ArrayList<EmployeeDto> supDelegates= new EmployeeDao().getDelegatedEmployees(sup.getEmployeeID());
			
			String msg = "Hi "+ emp.getDisplayName()+ "!\n"
					+"<br/>Your ad-hoc request for the transport facility has been cancelled " + " by "  + emp2.getDisplayName() +", with travel time "+ dto.getStartTime()+" on " +dto.getTravelDate()+" \n\n"
					+ "<br/><br/>For any change, please do contact your reporting officer/SPOC \n"
					+ "<br/>Do use the following link to access the ATOm" + " <a href='"
					+ new SettingsDoa().getDomainName()
					+"'>"+new SettingsDoa().getDomainName()+"</a>\n"
					+ "<br/>For any queries, call transport help desk at "+SettingsConstant.transportTeamNumber+" or mail us at "+SettingsConstant.transportTeamEmail+"\n\n\n"
					+ "<br/><br/>Thanks & Regards, \n" 
					+ "<br/>Transport Team \n"
					+ "<br/>"+SettingsConstant.transportTeamEmail+"\n"
					+ "<br/>---------------<br/> <i><u>Note:</u> This is a system generated email. Please do not reply.</i>";
					
			String cc[]=new String[2];
			//cc[0]=spocEmail;
			cc[0]=supEmail;
			cc[1]=pmEmail;
			//cc=OtherFunctions.appendArray(cc, new EmployeeService().getEmails(spocDelegates));
			cc=OtherFunctions.appendArray(cc, new EmployeeService().getEmails(supDelegates));
			 ArrayList<EmployeeDto> tms = new EmployeeDao().getEmployeesByRole("tm");
			 ArrayList<EmployeeDto> tas = new EmployeeDao().getEmployeesByRole("ta");
			 ArrayList<EmployeeDto> all = new ArrayList<EmployeeDto>();
			 all.addAll(tas);
			 all.addAll(tms);
				String bccs[] = new String[0];
				bccs = OtherFunctions.appendArray(bccs, new EmployeeService().getEmails(all));
				for(EmployeeDto tm: all)
				{
				bccs = OtherFunctions.appendArray(bccs, new EmployeeService()
						.getEmails(new EmployeeDao().getDelegatedEmployees(tm
								.getEmployeeID())));
				
				}
			
					
			SendMail mail=SendMailFactory.getMailInstance();
			mail.setCcs(cc);
			mail.setBccs(bccs);
			mail.send (address, title, msg);
			 

	
	}
	}
	
	public void NoShowMail()
	{
		ArrayList<EmployeeDto> list=new EmployeeService().NoshowEmps();
		String empsubject="No Show Alert";
		String fortransport="<table><tr><th>Employee Id</th><th>Name</th><th>Email</th><th>No Show Count</th></tr>";
		int size=list.size();
		String msg[],address[],ccs[];
		msg=new String[size];
		address=new String[size];
		ccs=new String[size];
		int i=0;
		String domainName=new SettingsDoa().getDomainName();

		for(EmployeeDto dto :list)
		{
	    	msg[i]= "Dear "+ dto.getDisplayName()+ "!\n"
					+"<br/>                           It has been noted that you were scheduled in last "+dto.getNoshowdays()+" day(s) but you have not boarded the cab for "+dto.getNoshowcount()+" times. Any further instances will cause cancellation of your schedule. Please notify the Transport Desk in case you are not availing transport."
					+ "<br/>Do use the following link to access the ATOm" + " <a href='"
					+ domainName
					+"'>"+domainName+"</a>\n"
					+ "<br/>For any queries, call transport help desk at "+SettingsConstant.transportTeamNumber+" or mail us at "+SettingsConstant.transportTeamEmail+"\n\n\n"
					+ "<br/><br/>Thanks & Regards, \n" 
					+ "<br/>Transport Team \n"
					+ "<br/>"+SettingsConstant.transportTeamEmail+"\n"
					+ "<br/>---------------<br/> <i><u>Note:</u> This is a system generated email. Please do not reply.</i>";
			System.out.println("here"+dto.getDisplayName());
			ccs[i]=dto.getSpocName();
			address[i]= dto.getEmailAddress();
			fortransport+="<tr><td>"+dto.getPersonnelNo()+"</td><td>"+dto.getDisplayName()+"</td><td>"+dto.getEmailAddress()+"</td><td>"+dto.getNoshowcount()+" (Out of"+dto.getNoshowdays()+"days)</td></tr>";
			i++;
		}
	
		
		fortransport+="</table>";
		String transmsg = "Hi \n"
				+"<br/> No Show Employee Details are listed below<br/>"	+fortransport		+ "<br/><br/>Thanks & Regards, \n" 
				+ "<br/>Transport Team \n"
				+ "<br/>"+SettingsConstant.transportTeamEmail+"\n"
				+ "<br/>---------------<br/> <i><u>Note:</u> This is a system generated email. Please do not reply.</i>";
		 SendMail mail1=SendMailFactory.getMailInstance();
		System.out.println("No show emp list size: "+list.size() );
		 if(list.size()>0)
		{ 
		// mail1.send ("lalith@agiledgesolutions.COM", empsubject, transmsg);
			 
		 mail1.send ("HARIHARAN.RAMAKRISHNAN@SIEMENS.COM", empsubject, transmsg);
		 System.out.println("No show MAil send to Admin"); 
	    }

		 /*SendMail mail =SendMailFactory.getMailInstance();
		 mail.send(address, empsubject, msg, ccs); */
	}
	public void ScheduleAlertMail()
	{
		
		ArrayList<EmployeeDto> list=new EmployeeService().getEmployeesForReminderMail();
		int size=list.size();
		String msg[],address[],ccs[];
		msg=new String[size];
		address=new String[size];
		ccs=new String[size];
		int i=0;
		for(EmployeeDto dto :list)
		{
			msg[i] ="Dear "+ dto.getDisplayName()+ "!\n"
					+"<br/> Now Scheduling has Open for Wednesday 10:00 AM to Friday 06:00 PM."
					+ "<br/>Do use the following link to access the ATOm" + " <a href='"
					+ new SettingsDoa().getDomainName()
					+"'>"+new SettingsDoa().getDomainName()+"</a>\n"
					+ "<br/>For any queries, call transport help desk at "+SettingsConstant.transportTeamNumber+" or mail us at "+SettingsConstant.transportTeamEmail+"\n\n\n"
					+ "<br/><br/>Thanks & Regards, \n" 
					+ "<br/>Transport Team \n"
					+ "<br/>"+SettingsConstant.transportTeamEmail+"\n"
					+ "<br/>---------------<br/> <i><u>Note:</u> This is a system generated email. Please do not reply.</i>";
			
			address[i]= dto.getEmailAddress();
			ccs[i]=address[i];		
			i++;
		}
		
		//System.out.println("before sending");
		SendMail mail =SendMailFactory.getMailInstance();
		mail.send(address,"Scheduling Alert", msg, ccs);
		//System.out.println("after sending");
		
		
		
		
	}
		
		

}
