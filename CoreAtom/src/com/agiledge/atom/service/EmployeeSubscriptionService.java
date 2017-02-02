/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.agiledge.atom.service;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.agiledge.atom.commons.Common;
import com.agiledge.atom.commons.OtherFunctions;
import com.agiledge.atom.constants.SettingsConstant;
import com.agiledge.atom.dao.EmployeeDao;
import com.agiledge.atom.dao.EmployeeSubscriptionDao;
import com.agiledge.atom.dao.MailDao;
import com.agiledge.atom.dao.SettingsDoa;
import com.agiledge.atom.dto.EmployeeDto;
import com.agiledge.atom.dto.EmployeeSubscriptionDto;
import com.agiledge.atom.mail.SendMail;
import com.agiledge.atom.mail.SendMailFactory;



/**
 * 
 * @author 123
 */
public class EmployeeSubscriptionService {
	
	private String errorMessage;

	EmployeeSubscriptionDao employeeSubscriptionDao = new EmployeeSubscriptionDao();

	private boolean error;

	private String message;

	public int subscribeRequest(EmployeeSubscriptionDto employeeSubscriptionDto) {
		int val = employeeSubscriptionDao
				.subscribeRequest(employeeSubscriptionDto);
		if (val > 0) {
			sendEmailForSubscriptionRequested(employeeSubscriptionDto);
			
		}
		return val;
	}

	public boolean isSubscribed(String empid) {

		return employeeSubscriptionDao.isSubscribed(empid);
		}
	public boolean isUnsubscribed(String empid) {

		return employeeSubscriptionDao.isUnsubscribed(empid);
		}
	public boolean isPendingOrSubscribed(String empid) {

		return employeeSubscriptionDao.isPendingOrSubscribed(empid);
	}
	public int unsubscribeRequest(EmployeeSubscriptionDto dto) {
		int returnInt = employeeSubscriptionDao.unsubscribeRequest(dto);
		if (returnInt > 0) {
			sendEmailToUnsubscribed(dto);
		}
		return returnInt;
	}

	public void sendEmailForSubscriptionRequested(EmployeeSubscriptionDto dto) {
		if (dto != null) {
			EmployeeDto emp = new EmployeeDao().getEmployeeAccurate(dto
					.getEmployeeID());
			String title = "Subscription Notification";

			String address = emp.getEmailAddress();
			String msg = "Hi "+ emp.getDisplayName()+ " Congratulations! Your request for the shift transport facility has been placed successfully from "					
					+ dto.getSubscriptionFromDate()
					+ ".<br/><br/>For any change or for queries, call transport help desk at "+SettingsConstant.transportTeamNumber+" or mail us at "+SettingsConstant.transportTeamEmail
					+ "<br/><br/> The facility will be made available to you when you are scheduled to a specified shift by your reporting officer/SPOC." 
					+	"<br/> <br/>Thanks & Regards,<br/>" +
					"Transport Facility Team"+
					"<a href='"
					+ new SettingsDoa().getDomainName()
					+"'>"+
					
					"</a><br/>---------------<br/> <i><u>Note:</u> This is a system generated email. Please do not reply.</i>";

			// EmployeeDto spoc =
			EmployeeDto spoc= new EmployeeDao().getEmployeeAccurate(
					dto.getSupervisor1());
			EmployeeDto sup = new EmployeeDao().getEmployeeAccurate(dto.getSupervisor2());
			String spocEmail =spoc.getEmailAddress();
			String supEmail = sup.getEmailAddress();
			ArrayList<EmployeeDto> spocDelegates= new EmployeeDao().getDelegatedEmployees(spoc.getEmployeeID());
			ArrayList<EmployeeDto> supDelegates= new EmployeeDao().getDelegatedEmployees(sup.getEmployeeID());
			 
			
		  
			String cc[]=new String[2];
			cc[0]=spocEmail;
			cc[1]=supEmail;
			cc=OtherFunctions.appendArray(cc, new EmployeeService().getEmails(spocDelegates));
			cc=OtherFunctions.appendArray(cc, new EmployeeService().getEmails(supDelegates));
			 ArrayList<EmployeeDto> tms = new EmployeeDao().getEmployeesByRole("ta");
				String bccs[] = new String[0];
				bccs = OtherFunctions.appendArray(bccs, new EmployeeService().getEmails(tms));
				for(EmployeeDto tm: tms)
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

	
	public void sendEmailToUnsubscribed(EmployeeSubscriptionDto dto) {
		if (dto != null) {
			EmployeeDto emp = new EmployeeDao().getEmployeeAccurate(dto
					.getEmployeeID());			
			
			String title = "Unsubscription Submitted";
			String address = emp.getEmailAddress();
			String msg = "Hi "
					+ emp.getDisplayName()					
					+ "! As per your request, the transport facility requisition is unsubscribed with effect from "
					+ dto.getUnsubscriptionFromDate()
					+ ".<br/><br/> The transport schedule for you remains unaffected until the un-subscription date "+ dto.getUnsubscriptionFromDate()+".<br/><br/> Please note, the transport facility will not be provided to you after this date. "
					+ "<br/><br/>For any change or for any queries, please contact transport help desk at "+SettingsConstant.transportTeamNumber+"/"+SettingsConstant.transportTeamEmail 
					+ "<br/><br/>Thanks & Regards,<br/>"
					+ "Transport Facility Team."
					+" <a href='"
					+ new SettingsDoa().getDomainName()
					+"'>" 
					
					+"</a>"
				
					+ "<br/>---------------<br/> <i><u>Note:</u> This is a system generated email. Please do not reply.</i>";

			dto = new EmployeeSubscriptionDao()
					.getEmployeeSubscriptionDetails(dto.getEmployeeID());
			String spocEmail = new EmployeeDao().getEmployeeAccurate(
					dto.getSupervisor2()).getEmailAddress();
			String supEmail = new EmployeeDao().getEmployeeAccurate(
					dto.getSupervisor1()).getEmailAddress();
			 
			
			SendMail mail=SendMailFactory.getMailInstance();
			String cc[]=new String[2];
			 
			cc[0]=spocEmail;
			cc[1]=supEmail;
			cc=OtherFunctions.appendArray(cc,new EmployeeService().getEmails( new EmployeeDao().getDelegatedEmployees( new EmployeeDao().getEmployeeAccurate(	dto.getSupervisor2()).getEmployeeID())));
			cc=OtherFunctions.appendArray(cc,new EmployeeService().getEmails( new EmployeeDao().getDelegatedEmployees( new EmployeeDao().getEmployeeAccurate(	dto.getSupervisor1()).getEmployeeID())));
			 
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
			mail.setCcs(cc);
			 mail.send(address, title, msg);
		
		}
	}

	public int subscribe(String currentDate) {
		int returnValue = 0;
		returnValue = employeeSubscriptionDao.subscribe(currentDate);
		if (returnValue > 0) {
		//	sendMailToSubscribed(currentDate);

		}
		return returnValue;
	}

	public void unsubscribe(String from_date) {
		new EmployeeSubscriptionDao().unsubscribe(from_date);

	//	sendMailToUnsubscribed(from_date);

	}

	
	public int sendMailToSubscribed(String currentDate) {
		List<EmployeeDto> dtoList = new EmployeeDao()
				.getAllSubscribedEmployees(currentDate);
		String employees;
		String message;
		int returnInt = 0;
		if (dtoList != null && dtoList.size() > 0) {
			for (EmployeeDto dto : dtoList) {
				employees = dto.getEmailAddress();
				message = "Hi "
						+ dto.getEmployeeFirstName()
						+ " "
						+ dto.getEmployeeLastName()
						+ ",<br/><br/>Your request for shift transport has been placed successfully with effective date "
						+ OtherFunctions.changeDateFromatToddmmyyyy( currentDate )
						+ "<br/><br/>You will be able to avail transport one your Manager/SPOC schedules you."
						+ "<br/><br/>Regards,<br/>" +
						" <a href='"
						+ new SettingsDoa().getDomainName()
						+"'>" 
						+ "Transport Team."
						+"</a>"+
						"<br/>--------------------<br/><i><b>Note:</b> This is a system generated email. Please do not reply.</i>";
				String title = "Subscription Notification";
				 
				 
				 ArrayList<EmployeeDto> tms = new EmployeeDao().getEmployeesByRole("ta");
					String bccs[] = new String[0];
					bccs = OtherFunctions.appendArray(bccs, new EmployeeService().getEmails(tms));
					for(EmployeeDto tm: tms)
					{
					bccs = OtherFunctions.appendArray(bccs, new EmployeeService()
							.getEmails(new EmployeeDao().getDelegatedEmployees(tm
									.getEmployeeID())));
					}
				
				SendMail mail= SendMailFactory.getMailInstance();
				mail.setBccs(bccs);
				
				EmployeeDto spoc = new EmployeeDao().getSpoc(dto.getEmployeeID());
				EmployeeDto manager = new EmployeeDao().getManager(dto.getEmployeeID());
				String ccs[]=new String[2];
				ccs[0]=spoc.getEmailAddress();
				ccs[1]=manager.getEmailAddress();
				mail.setCcs(ccs);
				
				mail.send( employees, title, message );
			 

			}
		}
		return returnInt;

	}

	public boolean validateUnsubscriptionForm(EmployeeSubscriptionDto dto,String source) {
		
		errorMessage="";
		
		boolean flag = true;
		try {
			if (dto.getEmployeeID().equals("")) {
				errorMessage="Invalid Employee";
				flag = false;

			} else if (dto.getUnsubscriptionFromDate().equals("")) {
				errorMessage="Un-subscription date is blank";
				flag = false;
			}else if(source.equals("user")&&(isBeyoundTwoMonths(dto.getUnsubscriptionFromDate())))
			{
				flag=false;
			}else if( validatePastDate(dto.getUnsubscriptionFromDate()))
			{
				flag=false;
			}
		} catch (NullPointerException e) {
			flag = false;
			
			System.out.println(" validation error " + e);
		}
		return flag;
	}

	public boolean isSubscriptionRequestMade(String empid) {
		return employeeSubscriptionDao.isSubscriptionRequestMade(empid);

	}

	public boolean isUnsubscriptionRequestMade(String empid) {
		return employeeSubscriptionDao.isUnsubscriptionRequestMade(empid);

	}
	public EmployeeSubscriptionDto getUnsubscriptionRequestDetails(String empid) {
		return employeeSubscriptionDao.getUnsubscriptionRequestDetails(empid);

	}	

	public EmployeeSubscriptionDto getEmployeeSubscriptionDetails(String empid) {
		return employeeSubscriptionDao.getEmployeeSubscriptionDetails(empid);
	}

	public EmployeeSubscriptionDto getUnsubscriptionDetails(String empid) {
		return employeeSubscriptionDao.getUnsubscriptionDetails(empid);
	}

	public int modifySubscribeRequest(EmployeeSubscriptionDto dto) {
		String landmarkId= new EmployeeSubscriptionService().getLandmark(dto.getEmployeeID());
		int value=employeeSubscriptionDao.modifySubscribeRequest(dto);
		if(value>0)
		{
			if(landmarkId!=null&&!landmarkId.equals("")&&!landmarkId.equals(dto.getLandMark()))
			{
				new MailDao().sendMailForAplChange(dto.getEmployeeID());
			}
		}
		
		
		return value;

	}
	public int modifySubscription(EmployeeSubscriptionDto dto) {
		//String landmarkId= new EmployeeSubscriptionService().getLandmark(dto.getEmployeeID());
		int value=employeeSubscriptionDao.modifySubscription(dto);
		/*if(value>0)
		{
			if(landmarkId!=null&&!landmarkId.equals("")&&!landmarkId.equals(dto.getLandMark()))
			{
				new MailDao().sendMailForAplChange(dto.getEmployeeID());
			}
		}*/
		
		
		return value;

	}

	
	public int modifySubscriptionEffective(String curdate)
	{
		int value=employeeSubscriptionDao.modifySubscriptionEffective(curdate);
		/*if(value>0)
		{
			if(landmarkId!=null&&!landmarkId.equals("")&&!landmarkId.equals(dto.getLandMark()))
			{
				new MailDao().sendMailForAplChange(dto.getEmployeeID());
			}
		}*/
		return value;
	}
	private String getLandmark(String employeeId) {
		 
		return  new EmployeeSubscriptionDao().getLandmark(employeeId);
	}

	// to get list of subscription done by employee
	public List<EmployeeSubscriptionDto> getEmployeeSubscriptionsList(
			String empID) {
		return employeeSubscriptionDao.getEmployeeSubscriptionsList(empID);
	}

	/*
	 * public int setAddressChangePrivilegeCounter( ) { return
	 * employeeSubscriptionDao.setAddressChangePrivilegeCounter( ); }
	 * 
	 * public void countDownAplChangePrivilegeCounter() { //
	 * employeeSubscriptionDao.countDownAplChangePrivilegeCounter();
	 * 
	 * }
	 */

	/*
	 * public boolean hasPrivilegeToChangeAddress(String empid) { // TODO
	 * Auto-generated method stub return
	 * employeeSubscriptionDao.hasPrivilegeToChangeAddress(empid); }
	 * 
	 * public int setAddressChangePrivilegeCounterForContractEmployee(String
	 * curDate ) { return
	 * employeeSubscriptionDao.setAddressChangePrivilegeCounterForContractEmployee
	 * (curDate ); }
	 */
	public void sendEmailToEmployeesWhoAreAboutTheUnsubscription(String onDate,
			int dayBefore) {
		Calendar cal=Calendar.getInstance();
	    String temp[]=onDate.split("-");
		int cur_year=Integer.parseInt(temp[0]);
		int cur_month=Integer.parseInt(temp[1]);
		cur_month=cur_month-1;
		int cur_day=Integer.parseInt(temp[2]);
		cal.set(cur_year,cur_month,cur_day);
		int day_val=cal.get(Calendar.DAY_OF_WEEK);
		if(day_val !=7 && day_val !=1)
		{
		List<EmployeeSubscriptionDto> list = new EmployeeSubscriptionDao()
				.getSubscriptionDetailsOfEmployeesForUnsubscription(onDate,
						dayBefore);
		if (list != null && list.size() > 0) {
			for (EmployeeSubscriptionDto dto : list) {
				String email = new EmployeeDao().getEmployeeAccurate(
						dto.getEmployeeID()).getEmailAddress();
				String message = new SettingsService()
						.getEmailMessageToEmployeesWhoAreAboutTheUnsubscription(dto);
				String title = "Unsubscription Alert";
				 
				 
				 EmployeeDto spoc=new EmployeeDao().getEmployeeAccurate(
							dto.getSupervisor2());
				 EmployeeDto sup=new EmployeeDao().getEmployeeAccurate(
							dto.getSupervisor1());
				String spocEmail = spoc.getEmailAddress();
				String supEmail = sup.getEmailAddress();
				
				String  ccs[]=new String[2];
				 ccs[0]=spocEmail;
				 ccs[1]=supEmail;
				 ccs=OtherFunctions.appendArray(ccs,new EmployeeService().getEmails( new EmployeeDao().getDelegatedEmployees(spoc.getEmployeeID())));
				 ccs=OtherFunctions.appendArray(ccs,new EmployeeService().getEmails( new EmployeeDao().getDelegatedEmployees(sup.getEmployeeID())));
				 
					 
				 ArrayList<EmployeeDto> tms = new EmployeeDao().getEmployeesByRole("ta");
					String bccs[] = new String[0];
					bccs = OtherFunctions.appendArray(bccs, new EmployeeService().getEmails(tms));
					for(EmployeeDto tm: tms)
					{
					bccs = OtherFunctions.appendArray(bccs, new EmployeeService()
							.getEmails(new EmployeeDao().getDelegatedEmployees(tm
									.getEmployeeID())));
					}
				
				 
					SendMail mail= SendMailFactory.getMailInstance();
				 mail.setBccs(bccs);
				 mail.setCcs(ccs);

				 
						
						
						mail.send(email, title, message);
			}
		}
		if(day_val==6)
		{
			dayBefore=dayBefore+1;
			List<EmployeeSubscriptionDto> list1 = new EmployeeSubscriptionDao()
			.getSubscriptionDetailsOfEmployeesForUnsubscription(onDate,
					dayBefore);
	if (list1 != null && list1.size() > 0) {
		for (EmployeeSubscriptionDto dto1 : list1) {
			String email1 = new EmployeeDao().getEmployeeAccurate(
					dto1.getEmployeeID()).getEmailAddress();
			String message1 = new SettingsService()
					.getEmailMessageToEmployeesWhoAreAboutTheUnsubscription(dto1);
			String title1 = "Unsubscription Alert";
			System.out.println(" start send....");
			 
			 
			 //------
			 EmployeeDto spoc=new EmployeeDao().getEmployeeAccurate(
						dto1.getSupervisor2());
			 EmployeeDto sup=new EmployeeDao().getEmployeeAccurate(
						dto1.getSupervisor1());
			String spocEmail = spoc.getEmailAddress();
			String supEmail = sup.getEmailAddress();
			
			String  ccs[]=new String[3];
			 ccs[0]=spocEmail;
			 ccs[1]=supEmail;
			 ccs=OtherFunctions.appendArray(ccs,new EmployeeService().getEmails( new EmployeeDao().getDelegatedEmployees(spoc.getEmployeeID())));
			 ccs=OtherFunctions.appendArray(ccs,new EmployeeService().getEmails( new EmployeeDao().getDelegatedEmployees(sup.getEmployeeID())));
			  
			 ArrayList<EmployeeDto> tms = new EmployeeDao().getEmployeesByRole("ta");
				String bccs[] = new String[0];
				bccs = OtherFunctions.appendArray(bccs, new EmployeeService().getEmails(tms));
				for(EmployeeDto tm: tms)
				{
				bccs = OtherFunctions.appendArray(bccs, new EmployeeService()
						.getEmails(new EmployeeDao().getDelegatedEmployees(tm
								.getEmployeeID())));
				}
			
			 SendMail mail= SendMailFactory.getMailInstance();
			 mail.setBccs(bccs);
			 mail.setCcs(ccs);

					
					
					mail.send(email1, title1, message1);
		}
	}
			dayBefore=dayBefore+1;
			List<EmployeeSubscriptionDto> list2 = new EmployeeSubscriptionDao()
			.getSubscriptionDetailsOfEmployeesForUnsubscription(onDate,
					dayBefore);
	if (list2 != null && list2.size() > 0) {
		for (EmployeeSubscriptionDto dto2 : list2) {
			String email2 = new EmployeeDao().getEmployeeAccurate(
					dto2.getEmployeeID()).getEmailAddress();
			String message2 = new SettingsService()
					.getEmailMessageToEmployeesWhoAreAboutTheUnsubscription(dto2);
			String title2 = "Unsubscription Alert";
			System.out.println(" start send....");
			 
			 
			 EmployeeDto spoc=new EmployeeDao().getEmployeeAccurate(
						dto2.getSupervisor2());
			 EmployeeDto sup=new EmployeeDao().getEmployeeAccurate(
						dto2.getSupervisor1());
			String spocEmail = spoc.getEmailAddress();
			String supEmail = sup.getEmailAddress();
			
			String  ccs[]=new String[3];
			 ccs[0]=spocEmail;
			 ccs[1]=supEmail;
			 ccs=OtherFunctions.appendArray(ccs,new EmployeeService().getEmails( new EmployeeDao().getDelegatedEmployees(spoc.getEmployeeID())));
			 ccs=OtherFunctions.appendArray(ccs,new EmployeeService().getEmails( new EmployeeDao().getDelegatedEmployees(sup.getEmployeeID())));
			 
			 ArrayList<EmployeeDto> tms = new EmployeeDao().getEmployeesByRole("ta");
				String bccs[] = new String[0];
				bccs = OtherFunctions.appendArray(bccs, new EmployeeService().getEmails(tms));
				for(EmployeeDto tm: tms)
				{
				bccs = OtherFunctions.appendArray(bccs, new EmployeeService()
						.getEmails(new EmployeeDao().getDelegatedEmployees(tm
								.getEmployeeID())));
				}
			
			 SendMail mail= SendMailFactory.getMailInstance();
			 mail.setBccs(bccs);
			 mail.setCcs(ccs);

					
					
					mail.send(email2, title2, message2);
			}
		}
		}
			}
		}
	

	public void sendEmailToEmployeesWhoAreAboutTheSubscription(String onDate,
			int dayBefore) {
		
		Calendar cal=Calendar.getInstance();		
		cal.setTime(OtherFunctions.sqlFormatToDate(onDate));
		
		int day_val=cal.get(Calendar.DAY_OF_WEEK);
		System.out.println(day_val+"\tDate"+cal.getTime());
		if(day_val !=7 && day_val !=1)
		{
		List<EmployeeSubscriptionDto> list = new EmployeeSubscriptionDao()
				.getSubscriptionDetailsOfEmployeesForSubscription(onDate,
						dayBefore);		
		if (list != null && list.size() > 0) {
			for (EmployeeSubscriptionDto dto : list) {
				String email = new EmployeeDao().getEmployeeAccurate(
						dto.getEmployeeID()).getEmailAddress();
				String message = new SettingsService()
						.getEmailMessageToEmployeesWhoAreAboutTheSubscription(dto);
				String title = "Subscription Reminder ";
				//System.out.println(" start send....");
				
				
				
				
		 EmployeeDto spoc=new EmployeeDao().getEmployeeAccurate(
					dto.getSupervisor2());
		 EmployeeDto sup=new EmployeeDao().getEmployeeAccurate(
					dto.getSupervisor1());
		String spocEmail = spoc.getEmailAddress();
		String supEmail = sup.getEmailAddress();
		
		String  ccs[]=new String[3];
		 ccs[0]=spocEmail;
		 ccs[1]=supEmail;
		 ccs=OtherFunctions.appendArray(ccs,new EmployeeService().getEmails( new EmployeeDao().getDelegatedEmployees(spoc.getEmployeeID())));
		 ccs=OtherFunctions.appendArray(ccs,new EmployeeService().getEmails( new EmployeeDao().getDelegatedEmployees(sup.getEmployeeID())));
		 ArrayList<EmployeeDto> tms = new EmployeeDao().getEmployeesByRole("ta");
			String bccs[] = new String[0];
			bccs = OtherFunctions.appendArray(bccs, new EmployeeService().getEmails(tms));
			for(EmployeeDto tm: tms)
			{
			bccs = OtherFunctions.appendArray(bccs, new EmployeeService()
					.getEmails(new EmployeeDao().getDelegatedEmployees(tm
							.getEmployeeID())));
			}
		
			SendMail mail= SendMailFactory.getMailInstance();
		 mail.setBccs(bccs);
		 mail.setCcs(ccs);
				
				
				mail.send(email, title, message);
			}
		}
			if(day_val==6)
				{
					dayBefore=dayBefore+1;
					List<EmployeeSubscriptionDto> list1 = new EmployeeSubscriptionDao()
					.getSubscriptionDetailsOfEmployeesForSubscription(onDate,
							dayBefore);
			if (list1 != null && list1.size() > 0) {
				for (EmployeeSubscriptionDto dto1 : list1) {
					String email1 = new EmployeeDao().getEmployeeAccurate(
							dto1.getEmployeeID()).getEmailAddress();
					String message1 = new SettingsService()
							.getEmailMessageToEmployeesWhoAreAboutTheSubscription(dto1);
					String title1 = "Subscription Reminder";
					System.out.println(" start send....");
					 
					
					 
					//-------
					 EmployeeDto spoc=new EmployeeDao().getEmployeeAccurate(
								dto1.getSupervisor2());
					 EmployeeDto sup=new EmployeeDao().getEmployeeAccurate(
								dto1.getSupervisor1());
					String spocEmail = spoc.getEmailAddress();
					String supEmail = sup.getEmailAddress();
					
					String  ccs[]=new String[3];
					 ccs[0]=spocEmail;
					 ccs[1]=supEmail;
					 ccs=OtherFunctions.appendArray(ccs,new EmployeeService().getEmails( new EmployeeDao().getDelegatedEmployees(spoc.getEmployeeID())));
					 ccs=OtherFunctions.appendArray(ccs,new EmployeeService().getEmails( new EmployeeDao().getDelegatedEmployees(sup.getEmployeeID())));
					 ArrayList<EmployeeDto> tms = new EmployeeDao().getEmployeesByRole("ta");
						String bccs[] = new String[0];
						bccs = OtherFunctions.appendArray(bccs, new EmployeeService().getEmails(tms));
						for(EmployeeDto tm: tms)
						{
						bccs = OtherFunctions.appendArray(bccs, new EmployeeService()
								.getEmails(new EmployeeDao().getDelegatedEmployees(tm
										.getEmployeeID())));
						}
					
					 SendMail mail= SendMailFactory.getMailInstance();
					 mail.setBccs(bccs);
					 mail.setCcs(ccs);

							
							
							mail.send(email1, title1, message1);
					
					
					 
				}
			}
					dayBefore=dayBefore+1;
					List<EmployeeSubscriptionDto> list2 = new EmployeeSubscriptionDao()
					.getSubscriptionDetailsOfEmployeesForSubscription(onDate,
							dayBefore);
			if (list2 != null && list2.size() > 0) {
				for (EmployeeSubscriptionDto dto2 : list2) {
					String email2 = new EmployeeDao().getEmployeeAccurate(
							dto2.getEmployeeID()).getEmailAddress();
					String message2 = new SettingsService()
							.getEmailMessageToEmployeesWhoAreAboutTheSubscription(dto2);
					String title2 = "Subscription Reminder";
					System.out.println(" start send....");
					 
					 
					 //-------
					 EmployeeDto spoc=new EmployeeDao().getEmployeeAccurate(
								dto2.getSupervisor2());
					 EmployeeDto sup=new EmployeeDao().getEmployeeAccurate(
								dto2.getSupervisor1());
					String spocEmail = spoc.getEmailAddress();
					String supEmail = sup.getEmailAddress();
					
					String  ccs[]=new String[3];
					 ccs[0]=spocEmail;
					 ccs[1]=supEmail;
					 ccs=OtherFunctions.appendArray(ccs,new EmployeeService().getEmails( new EmployeeDao().getDelegatedEmployees(spoc.getEmployeeID())));
					 ccs=OtherFunctions.appendArray(ccs,new EmployeeService().getEmails( new EmployeeDao().getDelegatedEmployees(sup.getEmployeeID())));
					 
					 ArrayList<EmployeeDto> tms = new EmployeeDao().getEmployeesByRole("ta");
						String bccs[] = new String[0];
						bccs = OtherFunctions.appendArray(bccs, new EmployeeService().getEmails(tms));
						for(EmployeeDto tm: tms)
						{
						bccs = OtherFunctions.appendArray(bccs, new EmployeeService()
								.getEmails(new EmployeeDao().getDelegatedEmployees(tm
										.getEmployeeID())));
						}
					
					SendMail mail= SendMailFactory.getMailInstance();
					 mail.setBccs(bccs);
					 mail.setCcs(ccs);

							
						
					
					
					mail.send(email2, title2, message2);
					}
				}
				}
		}
	}

	
		
	
	
	
	public boolean validateWithAccountingDate( String fromDate, String curDate)
	{
		boolean flag=true;
		//String fromDate="32/12/2012";
		//String accountingDate="23/12/2012";
		String froms[]= fromDate.split("/");
	//	String acc[]= accountingDate.split("/");
	//	Calendar unsubscriptionDate = Calendar.getInstance();
		
		Calendar accounting= Calendar.getInstance();
		accounting.set(Calendar.DATE, Integer.parseInt(new SettingsDoa().getAccountingDate()));
		Date date=new Date();
		
		String toDays[]= curDate.split("/");
		int curMonth=Integer.parseInt(toDays[1]);
		curMonth--;
		if(curMonth==-1)
		{
			curMonth=11;
		}
		Calendar today= Calendar.getInstance();
		today.set(Calendar.DATE,Integer.parseInt(toDays[0]));
		today.set(Calendar.MONTH,curMonth);
		today.set(Calendar.YEAR,Integer.parseInt(toDays[2]));
		System.out.println(" Today  " + today.get(Calendar.DATE) + "/"+ today.get(Calendar.MONTH) + "/"+ today.get(Calendar.YEAR));
		
		Calendar from=Calendar.getInstance();
		Calendar nextNextMonth=Calendar.getInstance();
		Calendar lastDateOfCurrentMonth=Calendar.getInstance();
		lastDateOfCurrentMonth.set(Calendar.MONTH, lastDateOfCurrentMonth.get(Calendar.MONTH) );
		 lastDateOfCurrentMonth.set(Calendar.DATE, Common.getLastDay(lastDateOfCurrentMonth.get(Calendar.MONTH), lastDateOfCurrentMonth.get(Calendar.YEAR)));
		
		 Calendar lastDateOfNextMonth=Calendar.getInstance();
		 lastDateOfNextMonth.set(Calendar.MONTH, lastDateOfNextMonth.get(Calendar.MONTH) + 1 );
		 lastDateOfNextMonth.set(Calendar.DATE, Common.getLastDay(lastDateOfNextMonth.get(Calendar.MONTH), lastDateOfNextMonth.get(Calendar.YEAR)));
		
		 System.out.println("lastdate of next month : " + lastDateOfNextMonth.get(Calendar.DATE) + "/"+ lastDateOfNextMonth.get(Calendar.MONTH) + "/"+ lastDateOfNextMonth.get(Calendar.YEAR));
		 
		nextNextMonth.set(Calendar.MONTH, nextNextMonth.get(Calendar.MONTH) + 2);
		nextNextMonth.set(Calendar.DATE, 1);
		
		
		int month=Integer.parseInt(froms[1]);
		month--;
		if(month==-1)
		{
			month=11;
		}
		
		from.set(Calendar.DATE,Integer.parseInt(froms[0]));
		from.set(Calendar.MONTH,month);
		from.set(Calendar.YEAR,Integer.parseInt(froms[2]));
		
		
		
		if(accounting.get(Calendar.DAY_OF_WEEK)==7)
		{
			accounting.set(Calendar.DATE,accounting.get(Calendar.DATE)-1);
		}
		else if (accounting.get(Calendar.DAY_OF_WEEK)==1)
		{
			accounting.set(Calendar.DATE,accounting.get(Calendar.DATE)-2);
		}
		from.set(Calendar.HOUR, 0);
		from.set(Calendar.MINUTE,0);
		from.set(Calendar.SECOND, 0);
		from.set(Calendar.MILLISECOND, 0);
		
		accounting.set(Calendar.HOUR, 0);
		accounting.set(Calendar.MINUTE,0);
		accounting.set(Calendar.SECOND, 0);
		accounting.set(Calendar.MILLISECOND, 0);
		
		lastDateOfCurrentMonth.set(Calendar.HOUR, 0);
		lastDateOfCurrentMonth.set(Calendar.MINUTE,0);
		lastDateOfCurrentMonth.set(Calendar.SECOND, 0);
		lastDateOfCurrentMonth.set(Calendar.MILLISECOND, 0);
		
		lastDateOfNextMonth.set(Calendar.HOUR, 0);
		lastDateOfNextMonth.set(Calendar.MINUTE,0);
		lastDateOfNextMonth.set(Calendar.SECOND, 0);
		lastDateOfNextMonth.set(Calendar.MILLISECOND, 0);
		
		nextNextMonth.set(Calendar.HOUR, 0);
		nextNextMonth.set(Calendar.MINUTE,0);
		nextNextMonth.set(Calendar.SECOND, 0);
		nextNextMonth.set(Calendar.MILLISECOND, 0);
		
		
		System.out.println("Accouning date is "+ accounting.get(Calendar.DATE) + "/"+ accounting.get(Calendar.MONTH) + "/"+ accounting.get(Calendar.YEAR));
		
		System.out.println("FromDate : "+ from.get(Calendar.DATE)+ "/"+ from.get(Calendar.MONTH)+ "/"+ from.get(Calendar.YEAR));
		System.out.println(" Last date of current month : " +  lastDateOfCurrentMonth.get(Calendar.DATE)+ "/"+ lastDateOfCurrentMonth.get(Calendar.MONTH)+ "/"+ lastDateOfCurrentMonth.get(Calendar.YEAR));
		System.out.println(" Next Next Month : " + nextNextMonth.get(Calendar.DATE)+ "/"+ nextNextMonth.get(Calendar.MONTH)+ "/"+ nextNextMonth.get(Calendar.YEAR));
		
		System.out.println("Compare Accounting date with from date : " + accounting.compareTo(from));
		System.out.println("Compare from with last : " + from.compareTo(lastDateOfCurrentMonth));
		
		if(today.compareTo(from)>=0)
		{
			flag= false;
			errorMessage="Unsubscription date could not be past Date!";
			
		}else if(today.compareTo(accounting)>=0 && today.compareTo(lastDateOfCurrentMonth)<=0)
		{
			if(accounting.compareTo(from)<=0&& from.compareTo(lastDateOfNextMonth)<=0)
			{
				flag= false;
				errorMessage="The cut off date to unsubscribe is "+ accounting.get(Calendar.DAY_OF_MONTH) + "th.";
			}
		}
		
		
		
		
		else
		{
			flag= true;
		}
		if(!flag)
		{
			System.out.println("validation eror " +errorMessage);
		}

 
		return flag;
	}
	
	public boolean isBeyoundTwoMonths(String fromDateParam)
	{
		
		boolean flag=false;
		 
		String froms[]= fromDateParam.split("/");
		 Calendar fromDate= Calendar.getInstance();
		 int month=Integer.parseInt(froms[1]);
		 
		 month--;
			if(month==-1)
			{
				month=11;
			}
			
		fromDate.set(Calendar.MONTH, month);
		fromDate.set(Calendar.DAY_OF_MONTH, Integer.parseInt(froms[0]));
		fromDate.set(Calendar.YEAR, Integer.parseInt(froms[2]));
		fromDate.set(Calendar.MILLISECOND, 0);
		fromDate.set(Calendar.SECOND, 0);
		fromDate.set(Calendar.MINUTE, 0);
		fromDate.set(Calendar.HOUR, 0);
		
		
		Calendar nextnextNextMonth = Calendar.getInstance();
		nextnextNextMonth.setTime(new Date());
		//System.out.println(" today " );printD(nextnextNextMonth);
		nextnextNextMonth.set(Calendar.MONTH, nextnextNextMonth.get(Calendar.MONTH) + 2);
		nextnextNextMonth.set(Calendar.MILLISECOND, 0);
		nextnextNextMonth.set(Calendar.SECOND, 0);
		nextnextNextMonth.set(Calendar.MINUTE, 0);
		nextnextNextMonth.set(Calendar.HOUR, 0);
	 
		//System.out.println(" fromDate " );printD(fromDate);
	//	System.out.println(" next2nd :"); printD(nextnextNextMonth);
	//	System.out.println(" value: "+fromDate.compareTo( nextnextNextMonth));
		if(fromDate.compareTo( nextnextNextMonth)>0)
		{
			
			
			flag=true;
			errorMessage="Unsubscription date should not exceed 2 months";
		}else
		{
			
			flag=false;
		}
		System.out.println("Value : "+ flag);
		return flag;
		
	}
	public void printD(Calendar d)
	{
		System.out.println(d.get(Calendar.YEAR)+"-"+(d.get(Calendar.MONTH)+1)+"-"+d.get(Calendar.DAY_OF_MONTH)+":"+d.get(Calendar.MILLISECOND));
	}
	
	public boolean validatePastDate (String fromDateParam)
	{
		boolean flag=false;
		String froms[]= fromDateParam.split("/");
		 Calendar fromDate= Calendar.getInstance();
		 int month=Integer.parseInt(froms[1]);
		 
		 month--;
			if(month==-1)
			{
				month=11;
			}
			
		fromDate.set(Calendar.MONTH, month);
		fromDate.set(Calendar.DAY_OF_MONTH, Integer.parseInt(froms[0]));
		fromDate.set(Calendar.YEAR, Integer.parseInt(froms[2]));
		
		Calendar today = Calendar.getInstance();
		today.setTime(new Date());
		 
		
		
		
		if(fromDate.compareTo( today)<=0)
		{
			flag=true;
			errorMessage="Unsubscription should past date";
		}else
		{
			
			flag=false;
		}
		System.out.println("Value : "+ flag);
		return flag;
		
	}
	
	public boolean validateWithAccountingDate( String fromDate)
	{
		 
		boolean flag=true;
		//String fromDate="32/12/2012";
		//String accountingDate="23/12/2012";
		String froms[]= fromDate.split("/");
	//	String acc[]= accountingDate.split("/");
	//	Calendar unsubscriptionDate = Calendar.getInstance();
		
		Calendar accounting= Calendar.getInstance();
		accounting.set(Calendar.DATE, Integer.parseInt(new SettingsDoa().getAccountingDate()));
		
		 
		 
		Calendar today= Calendar.getInstance();
		 
		System.out.println(" Today  " + today.get(Calendar.DATE) + "/"+ today.get(Calendar.MONTH) + "/"+ today.get(Calendar.YEAR));
		
		Calendar from=Calendar.getInstance();
		Calendar nextNextMonth=Calendar.getInstance();		;
		Calendar lastDateOfCurrentMonth=Calendar.getInstance();
		lastDateOfCurrentMonth.set(Calendar.MONTH, lastDateOfCurrentMonth.get(Calendar.MONTH) );
		 lastDateOfCurrentMonth.set(Calendar.DATE, Common.getLastDay(lastDateOfCurrentMonth.get(Calendar.MONTH), lastDateOfCurrentMonth.get(Calendar.YEAR)));
		
		 Calendar lastDateOfNextMonth=Calendar.getInstance();
		 lastDateOfNextMonth.set(Calendar.MONTH, lastDateOfNextMonth.get(Calendar.MONTH) + 1 );
		 lastDateOfNextMonth.set(Calendar.DATE, Common.getLastDay(lastDateOfNextMonth.get(Calendar.MONTH), lastDateOfNextMonth.get(Calendar.YEAR)));
		
		 System.out.println("lastdate of next month : " + lastDateOfNextMonth.get(Calendar.DATE) + "/"+ lastDateOfNextMonth.get(Calendar.MONTH) + "/"+ lastDateOfNextMonth.get(Calendar.YEAR));
		 
		nextNextMonth.set(Calendar.MONTH, nextNextMonth.get(Calendar.MONTH) + 2);
		nextNextMonth.set(Calendar.DATE, 1);
		
		
		int month=Integer.parseInt(froms[1]);
		month--;
		if(month==-1)
		{
			month=11;
		}
		
		from.set(Calendar.DATE,Integer.parseInt(froms[0]));
		from.set(Calendar.MONTH,month);
		from.set(Calendar.YEAR,Integer.parseInt(froms[2]));
		
		
		
		if(accounting.get(Calendar.DAY_OF_WEEK)==7)
		{
			accounting.set(Calendar.DATE,accounting.get(Calendar.DATE));
		}
		else if (accounting.get(Calendar.DAY_OF_WEEK)==1)
		{
			accounting.set(Calendar.DATE,accounting.get(Calendar.DATE)-1);
		}
		from.set(Calendar.HOUR, 0);
		from.set(Calendar.MINUTE,0);
		from.set(Calendar.SECOND, 0);
		from.set(Calendar.MILLISECOND, 0);
		
		accounting.set(Calendar.HOUR, 0);
		accounting.set(Calendar.MINUTE,0);
		accounting.set(Calendar.SECOND, 0);
		accounting.set(Calendar.MILLISECOND, 0);
		
		lastDateOfCurrentMonth.set(Calendar.HOUR, 0);
		lastDateOfCurrentMonth.set(Calendar.MINUTE,0);
		lastDateOfCurrentMonth.set(Calendar.SECOND, 0);
		lastDateOfCurrentMonth.set(Calendar.MILLISECOND, 0);
		
		lastDateOfNextMonth.set(Calendar.HOUR, 0);
		lastDateOfNextMonth.set(Calendar.MINUTE,0);
		lastDateOfNextMonth.set(Calendar.SECOND, 0);
		lastDateOfNextMonth.set(Calendar.MILLISECOND, 0);
		
		nextNextMonth.set(Calendar.HOUR, 0);
		nextNextMonth.set(Calendar.MINUTE,0);
		nextNextMonth.set(Calendar.SECOND, 0);
		nextNextMonth.set(Calendar.MILLISECOND, 0);
		
		
		System.out.println("Accouning date is "+ accounting.get(Calendar.DATE) + "/"+ accounting.get(Calendar.MONTH) + "/"+ accounting.get(Calendar.YEAR));
		
		System.out.println("FromDate : "+ from.get(Calendar.DATE)+ "/"+ from.get(Calendar.MONTH)+ "/"+ from.get(Calendar.YEAR));
		System.out.println(" Last date of current month : " +  lastDateOfCurrentMonth.get(Calendar.DATE)+ "/"+ lastDateOfCurrentMonth.get(Calendar.MONTH)+ "/"+ lastDateOfCurrentMonth.get(Calendar.YEAR));
		System.out.println(" Next Next Month : " + nextNextMonth.get(Calendar.DATE)+ "/"+ nextNextMonth.get(Calendar.MONTH)+ "/"+ nextNextMonth.get(Calendar.YEAR));
		
		System.out.println("Compare Accounting date with from date : " + accounting.compareTo(from));
		System.out.println("Compare from with last : " + from.compareTo(lastDateOfCurrentMonth));
		
		if(today.compareTo(from)>=0)
		{
			flag= false;
			errorMessage="Unsubscription date could not be past Date";
			
		}else if(today.compareTo(accounting)>=0 && today.compareTo(lastDateOfCurrentMonth)<=0)
		{
			if(accounting.compareTo(from)<=0&& from.compareTo(lastDateOfNextMonth)<=0)
			{
				flag= false;
				errorMessage="The cut off date to unsubscribe is "+ accounting.get(Calendar.DAY_OF_MONTH) + "th. You are allowed to unsubscribe from "+ nextNextMonth.get(Calendar.DATE)+ "/"+ (nextNextMonth.get(Calendar.MONTH)+1 ) + "/"+ nextNextMonth.get(Calendar.YEAR) + " only";
			}
		}
		
		
		
		
		else
		{
			flag= true;
		}
		if(!flag)
		{
			System.out.println("validation eror " +errorMessage);
		}

 
		return flag;
		
	}


	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	public boolean validateSubscriptionForm(EmployeeSubscriptionDto dto,String source) {
		boolean flag = true;
		if (dto.getEmployeeID().equals("")) {
			flag = false;

		} else if (dto.getSupervisor1().equals("")) {
			errorMessage = "Spoc is not selected";
			flag = false;
		} else if (dto.getSupervisor2().equals("")) {
			errorMessage = "Spoc is not selected";
			flag = false;
		} else if (dto.getLandMark().equals("")) {
			errorMessage = "Landmark is not selected";
			flag = false;
		} else if (dto.getSite().equals("")) {
			errorMessage = "Site is not selected";
			flag = false;
		}else
		
		if (source.equals("Employee")&&
				 isBeyoundTwoMonths(dto.getSubscriptionFromDate()
				)){
			System.out.println("trs--004");
			flag=false;
			errorMessage = "Effective date should not exceed 2 months";
		} 
		System.out.println(source);
		return flag;
	}
	
	
	public int uploadSubscription(InputStream in, String site, String changedBy) {
		EmployeeSubscriptionDao esubDao = new EmployeeSubscriptionDao();
		ArrayList<EmployeeSubscriptionDto> subList =uploadSubscriptionFile(in );
		int val=0;
		if(error ==  false) {
			 
			 System.out.println("__________");
			 print(subList); 
			 System.out.println("__________");
			val  = esubDao.uploadSubscription(subList, site, changedBy);
			if(val==0) {
				
				getMessageList().addAll(esubDao.getMessageList());
				
			}
			
		} 
		 
		 
		return val;
		
	}
	
	private void print(ArrayList<EmployeeSubscriptionDto> subList) {
		if(subList!=null && subList.size()>0)
		for(EmployeeSubscriptionDto dto: subList) {
			System.out.print ("\tempId :" + dto.getEmployeeID());
			System.out.print ("\tmanagerId :" + dto.getSupervisor1());
			System.out.print ("\tSpocId :" + dto.getSupervisor2());
			System.out.print ("\tSubscription Date :" + dto.getSubscriptionDate());
			System.out.print ("\tSubscription From Date :" + dto.getSubscriptionFromDate());
			System.out.print("\tArea " + dto.getApl().getArea());
			System.out.print("\tPlace " + dto.getApl().getPlace());
			System.out.println("\tLandmark " + dto.getApl().getLandMark());
		}
	}

	public ArrayList<String> getMessageList() {
		return messageList;
	}

	public void setMessageList(ArrayList<String> messageList) {
		this.messageList = messageList;
	}
	
	public ArrayList<EmployeeSubscriptionDto>  uploadSubscriptionFile( InputStream in) {
		int r=0;
		error=false;
		message="";
		int rowIndex=0;
		ArrayList<EmployeeSubscriptionDto> dtoList = new ArrayList<EmployeeSubscriptionDto>();
		try {
			
			boolean flag = false;
			XSSFWorkbook workbook = new XSSFWorkbook(in);
			XSSFSheet sheet = workbook.getSheetAt(0);
			  
			Iterator<Row> rowIterator = sheet.iterator();
			  
			rowIterator.next();
			System.out.println("row 0");
			while (rowIterator.hasNext()  ) {
			
				
				rowIndex ++;
				if (flag)
					break;

				Row row = rowIterator.next();
				System.out.println("row " + row.getRowNum());
				// For each row, iterate through each columns

				Iterator<Cell> cellIterator = row.cellIterator();
				EmployeeSubscriptionDto dto = new EmployeeSubscriptionDto();
				 
				while (cellIterator.hasNext()) {
					
					Cell cell = cellIterator.next();
					
					
					System.out.println("col " + cell.getColumnIndex());
					r=cell.getColumnIndex();
					if (cell.getColumnIndex() < 8) {
						System.out.println("col type :" + cell.getCellType());
					//	if(cell.getColumnIndex()!=3&&cell.getColumnIndex()!=4) { 
						cell.setCellType(Cell.CELL_TYPE_STRING);
					//	}
						
					} else {
						break;
					}
 
					if (cell.getColumnIndex() == 0) {
						 // personnel No
								String value = cell.getStringCellValue();
						
						if (value == null || value.equals("")) {
							System.out.println("End of file");
							flag = true;
							break;

						}

						dto.setEmployeeID( cell.getStringCellValue().trim());
						 
						 
					 

					} 
					else if (cell.getColumnIndex() == 1) {
						 // personnel No
								String value = cell.getStringCellValue();
						
						if (value == null || value.equals("")) {
							System.out.println("End of file");
							flag = true;
							break;

						}

						dto.setSupervisor1( cell.getStringCellValue().trim());
						 
						 
					 

					}  
					else if (cell.getColumnIndex() == 2) {
						 // personnel No
								String value = cell.getStringCellValue();
						
						if (value == null || value.equals("")) {
							System.out.println("End of file");
							flag = true;
							break;

						}

						dto.setSupervisor2( cell.getStringCellValue().trim());
						 
						 
					 

					}else if (cell.getColumnIndex() == 3) {
						  
						 
					 try{
						Calendar cal = Calendar.getInstance();
						cal.set(1900, 0, 0, 0, 0);
					    cal.add(Calendar.DATE,  Integer.parseInt( cell.getStringCellValue())-1);
					    dto.setSubscriptionDate(OtherFunctions
								.changeDateFromatToSqlFormat( new Date(cal.getTimeInMillis()) ));
					    System.out.println("Actual su date "+dto.getSubscriptionDate()); 
						
					 }catch(NumberFormatException ne) {
						 dto.setSubscriptionDate(cell.getStringCellValue());
						}
					 System.out.println(" from date :" + dto.getSubscriptionDate());
					/*	  if(OtherFunctions.checkDateFormat(dto.getSubscriptionDate(), "dd/MM/yyyy")==false){
							 if(OtherFunctions.checkDateFormat(dto.getSubscriptionDate(), "yyyy-MM-dd")) {
								 
								 dto.setSubscriptionDate(OtherFunctions.changeDateFromat(dto.getSubscriptionDate()));
							 } else {
							  
							 getMessageList().add(" Line :" + rowIndex + " Validation Error : Incorrect subscription effective date format ");
							 error=true;
							 }
						 }
					*/	 
 
					} else if (cell.getColumnIndex() == 4) {
						  
						 
					 try{
						Calendar cal = Calendar.getInstance();
						cal.set(1900, 0, 0, 0, 0);
					    cal.add(Calendar.DATE,  Integer.parseInt( cell.getStringCellValue())-1);
					    dto.setSubscriptionFromDate(OtherFunctions
								.changeDateFromatToSqlFormat( new Date(cal.getTimeInMillis()) ));
						 System.out.println("Actual "+dto.getSubscriptionFromDate()); 
						
					 }catch(NumberFormatException ne) {
						 dto.setSubscriptionFromDate(cell.getStringCellValue());
						}
					 System.out.println(" from date :" + dto.getSubscriptionFromDate());
					/*	 if(OtherFunctions.checkDateFormat(dto.getSubscriptionFromDate(), "dd/MM/yyyy")==false){
							 if(OtherFunctions.checkDateFormat(dto.getSubscriptionFromDate(), "yyyy-MM-dd")) {
								 
								 dto.setSubscriptionFromDate(OtherFunctions.changeDateFromat(dto.getSubscriptionFromDate()));
							 } else {
							  
							 getMessageList().add(" Line :" + rowIndex + " Validation Error : Incorrect subscription effective date format ");
							 error=true;
							 }
						 }
					*/	 

					}   else if (cell.getColumnIndex() == 5) {
						 
						 dto.getApl().setArea(cell.getStringCellValue().trim());  
						   
						  System.out.println("area : " + dto.getApl().getArea());
					} else if (cell.getColumnIndex() == 6) {
						   
						dto.getApl().setPlace(cell.getStringCellValue().trim());
					} else if (cell.getColumnIndex() == 7) {
						dto.getApl().setLandMark(cell.getStringCellValue().trim());
					}
					
					
					
				
				}
				
				if (!flag) {
				 
						dtoList.add(dto);
				 
				}
				

			}
 			//System.out.println("returnint" + returnint);
			if(error) {
				message="Validation error at line : "+ rowIndex + " "+ message;
			}
		//	file.close();
			in.close();
			
		}

		catch (FileNotFoundException fnfe) {
			dtoList = null;
			fnfe.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			dtoList = null;
			System.out.println("ERRRO on cell "+r+"  cause"+e);
			e.printStackTrace();
			
		}
		return dtoList;
	}
	private ArrayList<String> messageList= new ArrayList<String> ();
	//for keonics
		public EmployeeDto getShuttleEmpSubscriptionDetails(String empid){
			return new EmployeeSubscriptionDao().getShuttleEmpSubscriptionDetails(empid);
		}

	
		//for keonics
		public int setShuttleEmpSubscriptionDetails(EmployeeDto dto) {
			return new EmployeeSubscriptionDao().setShuttleEmpSubscriptionDetails(dto);
		}
		//for visa
		public EmployeeDto getEmpRegisterDetails(String empid){
			return new EmployeeSubscriptionDao().getEmpRegisterDetails(empid);
		}

	
		//for visa
		public int setEmpRegisterDetails(EmployeeDto dto) {
			return new EmployeeSubscriptionDao().setEmpRegisterDetails(dto);
		}

	
}
