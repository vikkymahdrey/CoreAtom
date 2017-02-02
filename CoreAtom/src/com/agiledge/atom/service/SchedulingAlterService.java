/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.agiledge.atom.service;

import java.util.ArrayList;
import java.util.Calendar;

import com.agiledge.atom.commons.OtherFunctions;
import com.agiledge.atom.dao.EmployeeDao;
import com.agiledge.atom.dao.EmployeeSubscriptionDao;
import com.agiledge.atom.dao.MailDao;
import com.agiledge.atom.dao.ScheduleAlterDao;
import com.agiledge.atom.dao.ScheduleModifyCancelDao;
import com.agiledge.atom.dao.SettingsDoa;
import com.agiledge.atom.dto.EmployeeDto;
import com.agiledge.atom.dto.ScheduleAlterDto;
import com.agiledge.atom.dto.ScheduleModifyCancelDto;
import com.agiledge.atom.mail.SendMail;
import com.agiledge.atom.mail.SendMailFactory;



/**
 * 
 * @author Administrator
 */
public class SchedulingAlterService {
	public String getScheduleDetailsByDate(String scheduleId, String date) {
		return new ScheduleAlterDao()
				.getScheduleDetailsByDate(scheduleId, date);
	}
	
	
	
	public ArrayList<ScheduleAlterDto> getScheduleDetalsByDate(long scheduleId) {

		ArrayList<ScheduleAlterDto> scheduleAlterList = null;
		try {
			ScheduleAlterDao scheduleAlterDaoObj = null;
			Calendar fromDate = Calendar.getInstance();
			Calendar toDate = Calendar.getInstance();			
			scheduleAlterDaoObj = new ScheduleAlterDao();
			ArrayList<ScheduleAlterDto> scheduleExistingalter = scheduleAlterDaoObj
					.getExistingAlter(scheduleId);
			scheduleAlterList = new ArrayList<ScheduleAlterDto>();
			ScheduleAlterDto scheduleAlterDtoObj = null;
			ScheduleAlterDto scheduleAlterDtoObj1= scheduleAlterDaoObj
					.getScheduleDetails(scheduleId);			
			fromDate.setTime(scheduleAlterDtoObj1.getFromDate());
			toDate.setTime(scheduleAlterDtoObj1.getToDate());			
			String passdate = "";
			// System.out.println("From Date"+fromDate.getTime()+"To date"+toDate.getTime());
			boolean flag;
			while (fromDate.getTimeInMillis() <= toDate.getTimeInMillis()) {
				flag = false;
				scheduleAlterDtoObj = new ScheduleAlterDto();
				long fromdatelong = fromDate.getTime().getTime();
				passdate = new java.sql.Date(fromdatelong).toString();
				scheduleAlterDtoObj.setScheduleId("" + scheduleId);
				scheduleAlterDtoObj.setDate("" + passdate);
				if (scheduleExistingalter != null) {

					for (ScheduleAlterDto scheduleAlterExistingDto : scheduleExistingalter) {
						if (scheduleAlterExistingDto.getDate().equals(
								scheduleAlterDtoObj.getDate())) {
							scheduleAlterDtoObj.setUpdatedById(scheduleAlterExistingDto.getUpdatedById());
							scheduleAlterDtoObj.setUpdatedByDisplayName(scheduleAlterExistingDto.getUpdatedByDisplayName());
							scheduleAlterDtoObj
									.setLoginTime(scheduleAlterExistingDto
											.getLoginTime());
							scheduleAlterDtoObj
									.setLogoutTime(scheduleAlterExistingDto
											.getLogoutTime());
							scheduleAlterDtoObj
							.setStatusDate(scheduleAlterExistingDto
									.getStatusDate());
							scheduleAlterDtoObj.setScheduleStates("exist");
							flag = true;
							break;
						}
					}
				}
				if (!flag) {
					scheduleAlterDtoObj.setUpdatedById(scheduleAlterDtoObj1.getUpdatedById());
					scheduleAlterDtoObj.setUpdatedByDisplayName(scheduleAlterDtoObj1.getUpdatedByDisplayName());
					scheduleAlterDtoObj.setLoginTime(scheduleAlterDtoObj1.getLoginTime());
					scheduleAlterDtoObj.setLogoutTime(scheduleAlterDtoObj1.getLogoutTime());
					scheduleAlterDtoObj.setStatusDate(scheduleAlterDtoObj1.getStatusDate());
					scheduleAlterDtoObj.setScheduleStates("new");
				}
				fromDate.add(Calendar.DATE, 1);
				// System.out.println("List sts"+scheduleAlterDtoObj.getDate());
				scheduleAlterList.add(scheduleAlterDtoObj);
			}
		} catch (Exception e) {
			System.out.println("ERROIn service" + e);
		}
		return scheduleAlterList;
	}

	
	
	
	
	
	
	
	
	
	
	public ArrayList<ScheduleAlterDto> getScheduleEmployeeDetalsByDate(long[] scheduleIds) {

		ArrayList<ScheduleAlterDto> scheduleAlterList = null;
		try {
			ScheduleAlterDao scheduleAlterDaoObj = null;
			Calendar fromDate = Calendar.getInstance();
			Calendar toDate = Calendar.getInstance();
			
			scheduleAlterDaoObj = new ScheduleAlterDao();
			scheduleAlterList = new ArrayList<ScheduleAlterDto>();
			for(Long scheduleId:  scheduleIds)
			{
			ArrayList<ScheduleAlterDto> scheduleExistingalter = scheduleAlterDaoObj
					.getExistingAlter(scheduleId);			
			ScheduleAlterDto scheduleAlterDtoObj = null;
			ScheduleAlterDto scheduleAlterDtoObj1= scheduleAlterDaoObj
					.getScheduleDetails(scheduleId);			
			fromDate.setTime(scheduleAlterDtoObj1.getFromDate());
			toDate.setTime(scheduleAlterDtoObj1.getToDate());			
			String passdate = "";
			// System.out.println("From Date"+fromDate.getTime()+"To date"+toDate.getTime());
			boolean flag;
			while (fromDate.getTimeInMillis() <= toDate.getTimeInMillis()) {
				flag = false;
				scheduleAlterDtoObj = new ScheduleAlterDto();
				long fromdatelong = fromDate.getTime().getTime();
				passdate = new java.sql.Date(fromdatelong).toString();
				scheduleAlterDtoObj.setScheduleId("" + scheduleId);
				scheduleAlterDtoObj.setDate("" + passdate);
				if (scheduleExistingalter != null) {

					for (ScheduleAlterDto scheduleAlterExistingDto : scheduleExistingalter) {
						if (scheduleAlterExistingDto.getDate().equals(
								scheduleAlterDtoObj.getDate())) {
							scheduleAlterDtoObj.setScheduleId(scheduleId.toString());
							scheduleAlterDtoObj
									.setLoginTime(scheduleAlterExistingDto
											.getLoginTime());
							scheduleAlterDtoObj
									.setLogoutTime(scheduleAlterExistingDto
											.getLogoutTime());
							scheduleAlterDtoObj
							.setStatusDate(scheduleAlterExistingDto
									.getStatusDate());
							scheduleAlterDtoObj.setScheduleStates("exist");
							flag = true;
							break;
						}
					}
				}
				if (!flag) {
					scheduleAlterDtoObj.setScheduleId(scheduleId.toString());
					scheduleAlterDtoObj.setLoginTime(scheduleAlterDtoObj1.getLoginTime());
					scheduleAlterDtoObj.setLogoutTime(scheduleAlterDtoObj1.getLogoutTime());
					scheduleAlterDtoObj.setStatusDate(scheduleAlterDtoObj1.getStatusDate());
					scheduleAlterDtoObj.setScheduleStates("new");
				}
				fromDate.add(Calendar.DATE, 1);
				// System.out.println("List sts"+scheduleAlterDtoObj.getDate());
				scheduleAlterList.add(scheduleAlterDtoObj);
			}
		}
		} catch (Exception e) {
			System.out.println("ERROIn service" + e);
		}
		return scheduleAlterList;
	}

	public void scheduleAlterInsert(
			ArrayList<ScheduleAlterDto> scheduleAlterDtoList) {
		new ScheduleAlterDao().scheduleAlterInsert(scheduleAlterDtoList);

	}

	public void sendMessageForAlter(
			ArrayList<ScheduleAlterDto> scheduleAlterDtoList, String user) {
		// TODO Auto-generated method stub

		String scheduleID = "";
		try {
			if (scheduleAlterDtoList != null && scheduleAlterDtoList.size() > 0) {

				scheduleID = scheduleAlterDtoList.get(0).getScheduleId();
				// System.out.println(".........." + scheduleID);
				EmployeeDto dto = new EmployeeDao()
						.getEmployeeDetailsBasedOnScheduleID(scheduleID);
				if (dto != null) {
					String emailAddress = dto.getEmailAddress();

					String message = new SettingsService()
							.getAlteredScheduleListMessage(
									scheduleAlterDtoList, user);
					String title = "Transport Schedule Modification";

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
					
					mail.send(emailAddress , title, message );
					 

				}
			}
		} catch (Exception e) {
			System.out.println("Exception : " + e);
		}
		// TODO: handle exception

	}

	public void sendMessageForCancelAlter(
			ArrayList<ScheduleAlterDto> scheduleAlterDtoList, String user) {
		// TODO Auto-generated method stub
		String scheduleID = "";
		try {
			if (scheduleAlterDtoList != null && scheduleAlterDtoList.size() > 0) {
				scheduleID = scheduleAlterDtoList.get(0).getScheduleId();
				EmployeeDto dto = new EmployeeDao()
						.getEmployeeDetailsBasedOnScheduleID(scheduleID);
				if (dto != null) {
					String emailAddress = dto.getEmailAddress();

					String message = new MailDao()
							.getAlterCancelledScheduleListMessage(
									scheduleAlterDtoList, dto, user);
					String title = "Schedule Cancellations ";

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
					
					mail.send(emailAddress , title, message );

				}
			}
		} catch (Exception e) {
			System.out.println("Exception : " + e);
		}
		// TODO: handle exception

	}

	public int scheduleModify(
			ArrayList<ScheduleModifyCancelDto> scheduledEmpList) {

		int returnInt = new ScheduleModifyCancelDao()
				.scheduleModify(scheduledEmpList);

		return returnInt;

	}

	public void sendMailForScheduleModify(
			ArrayList<ScheduleModifyCancelDto> scheduledEmpList, String user) {
		for (ScheduleModifyCancelDto dto : scheduledEmpList) {
			String subID = new EmployeeSubscriptionDao()
					.getSubscriptionIdFromScheduleId(dto.getScheduleId());
			EmployeeDto emp = new EmployeeDao()
					.getEmployeeDetailsBasedOnScheduleID(dto.getScheduleId());
			EmployeeDto spoc = new EmployeeDao().getEmployeeAccurate(user);
			String msg = "Hi "
					+ emp.getEmployeeFirstName()
					+ ",<br/><br/>Your schedule has been modified by "
					+ spoc.getEmployeeFirstName()
					+ " "
					+ spoc.getEmployeeLastName()
					+ "<br/><br/> Please click on below link to view the schedule."
					+ "<br/><br/>" 
					 
					+"Regards,<br/>"
					
					+ "<a href='"
							+ new SettingsDoa().getDomainName()
							+ "' >" 
							+"Transport Team "
							+ " </a> "
					 + "<br/>---------------<br/> <i><u>Note:</u> This is a system generated email. Please do not reply.</i>";
			String address = emp.getEmailAddress();
			String title = "Transport Schedule Modification";
			 
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
			
			mail.send(address, title, msg);
		}
	}

	public void sendMailForScheduleCancel(
			ArrayList<ScheduleModifyCancelDto> scheduledEmpList, String user) {
		for (ScheduleModifyCancelDto dto : scheduledEmpList) {
			String subID = new EmployeeSubscriptionDao()
					.getSubscriptionIdFromScheduleId(dto.getScheduleId());
			EmployeeDto emp = new EmployeeDao()
					.getEmployeeDetailsBasedOnScheduleID(dto.getScheduleId());
			EmployeeDto spoc = new EmployeeDao().getEmployeeAccurate(user);
			String message="Hi "
					+ emp.getEmployeeFirstName()
					+ ".<br/><br/>Your schedule has been modified by "  + spoc.getDisplayName()
					+ "<br/><br/> Please click on below link to view the schedule."
					+"<br/><br/>" 
					+"Regards,<br/>"
					+""
					+ "<a href='"
							+ new SettingsDoa().getDomainName()
							+ "' >" 
							+ "Transport Team." 
							+ " </a> "
					+ "<br/>---------------<br/> <i><u>Note:</u> This is a system generated email. Please do not reply.</i>";
					
		/*	String msg = "Hi "
					+ emp.getEmployeeFirstName()
					+ ",<br/><br/>&nbsp;&nbsp;&nbsp;&nbsp; Your schedule within "
					+ OtherFunctions.changeDateFromatToddmmyyyy(dto
							.getFromDate())
					+ " and  "
					+ OtherFunctions
							.changeDateFromatToddmmyyyy(dto.getToDate())
					+ "  has been cancelled by "
					+ spoc.getEmployeeFirstName()
					+ " "
					+ spoc.getEmployeeLastName()
					+ "<br/><br/><a href='"
					+ new SettingsDoa().getDomainName()
					+ "emp_schedulingHistory.jsp?subscriptionID="
					+ subID
					+ "&type=mail'   >Click here for details</a>  "
					+ "<br/><br/>Regards,<br/>Transport Team<br/>---------------<br/> <i><u>Note:</u> This is a system generated email. Please do not reply.</i>";
		*/	String address = emp.getEmailAddress();
			String title = "Schedule Cancellation";

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
			
			mail.send(address, title, message);
		}
	}

	
	
	
}
