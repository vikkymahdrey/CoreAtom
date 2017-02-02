/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.agiledge.atom.service;

import java.util.ArrayList;
import java.util.Calendar;

import org.json.JSONObject;

import com.agiledge.atom.constants.SettingsConstant;
import com.agiledge.atom.dao.EmployeeDao;
import com.agiledge.atom.dao.SchedulingDao;
import com.agiledge.atom.dao.SettingsDoa;
import com.agiledge.atom.dto.EmployeeDto;
import com.agiledge.atom.dto.EmployeeSubscriptionDto;
import com.agiledge.atom.dto.ScheduleAlterDto;
import com.agiledge.atom.dto.SchedulingDto;
import com.agiledge.atom.dto.SettingsDTO;


/**
 * 
 * @author 123
 */
public class SettingsService {

	public String getEmailMessageToEmployeesWhoAreAboutTheUnsubscription(
			EmployeeSubscriptionDto dto) {
		// TODO Auto-generated method stub
		EmployeeDto employee = new EmployeeDao().getEmployeeAccurate(dto
				.getEmployeeID());

		return "Hi "
				+ employee.getDisplayName()
				+ ",<br/><br/>You have unsubscribed from shift transport effective " +
				  dto.getUnsubscriptionFromDate()+
				". You will not be able to avail transport going forward.<br/><br/>"
				+ " Your entire schedule will be deleted from this date onwards. <br/><br/>"
				+ "Regards,<br/><a href='"
				+ new SettingsDoa().getDomainName()
				+ "'  >" + "Transport Team" + " </a>"
				+ "<br/>---------------<br/> <i><u>Note:</u> This is a system generated email. Please do not reply.</i>";
	}

	public String getEmailMessageToEmployeesWhoAreAboutTheSubscription(
			EmployeeSubscriptionDto dto) {
		// TODO Auto-generated method stub
		EmployeeDto employee = new EmployeeDao().getEmployeeAccurate(dto
				.getEmployeeID());

		return "Hi "
				+ employee.getEmployeeFirstName()
				+ " "
				+ employee.getEmployeeLastName()
				+ " ,<br/><br/>Your subscription for shift transport is effective "+dto.getSubscriptionFromDate()+". You will be able to avail transport once your Manager/SPOC schedules. "
				+ "<br/><br/>Please click on link below to check your schedule."
				+ "<br/><br/>In case you are not scheduled, please check with your Manager/SPOC."
				+ "<br/><br/>Regards,<br/>" +
				" <a href='"
				+ new SettingsDoa().getDomainName()
				+"'>" 
				+ "Transport Team."
				+"</a>"
				+ " <br/>---------------<br/> <i><u>Note:</u> This is a system generated email. Please do not reply.</i>";
	}

	public static String getAddExternalEmployeeMessage(EmployeeDto dto)
	{
		return "Hi "
				+dto.getEmployeeFirstName()
				+" "
				+dto.getEmployeeLastName()
				+",<br/><br/>You have been added in ATOm application.To access ATOm application,please use details given below</br></br> "
				+ ".<br/><br/>"
				+"Link - <a href='"+new SettingsDoa().getDomainName()+"'>ATOm</a>"
				+"<br/><br/>Login Id - "+dto.getLoginId()
				+"<br/><br/>Password - "+dto.getPassword()
				+"<br/><br/>Regards,<br/>" +
						" <a href='"
						+ new SettingsDoa().getDomainName()
						+"'>" 
						+"Transport Team."
						+"</a>"+"<br/>---------------<br/> <i><u>Note:</u> This is a system generated email. Please do not reply.</i>";
	}
	
	public static String getAddressChangedEmployeesMessage(EmployeeDto dto) {
		return "Hi "
				+ dto.getDisplayName()
				+ "!  "
				+ " Your address has been changed/updated in our Transport subscription database. If your transport destination has changed, please do change your APL by suitably modifying your APL in your subscription to ensure appropriate transit.<br/><br/>"
				+ ".<br/><br/>" 
				+ "If no changes required please ignore this email."
				
				+"For any queries, call transport help desk at "+SettingsConstant.transportTeamNumber+" or mail us at "+SettingsConstant.transportTeamEmail
				+"<br/><br/>Thanks & Regards,<br/>" 
				+"Transport Team."
				+" <a href='"
				+ new SettingsDoa().getDomainName()
				+"'>" 				
				+"</a>"+"<br/>---------------<br/> <i><u>Note:</u> This is a system generated email. Please do not reply.</i>";

	}

	public static String getAddressChangedEmployeesMessageTitle() {
		return "Address Update/Change Notification";
	}

	static String getUnSubscriptionMessageTitle() {
		return "Unsubscription notification ";
	}

	public double getTransportationCost() {
		SettingsDoa dao = new SettingsDoa();
		return dao.getTransportationCost();

	}
	
	public boolean checkDate(String fromDate,String toDate,int projectid,int siteid)
	{
	return new SettingsDoa().checkDate(fromDate, toDate,projectid,siteid);
	}
	
	public int deletesetting(int deleteId)
	{
		return new SettingsDoa().deleteSetting(deleteId);
	}

	public String getAccountingDate() {
		SettingsDoa dao = new SettingsDoa();
		String accountingDate = dao.getAccountingDate();
		Calendar cal = Calendar.getInstance();
		// System.out.println("Date " + cal.toString());
		return accountingDate;

	}

	public String getCurrentAccountingDate() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DATE,
				Integer.parseInt(new SettingsDoa().getAccountingDate()));

		int minus = 0;
		if (cal.get(Calendar.DAY_OF_WEEK) == 1) {
			minus = 1;
		} else if (cal.get(Calendar.DAY_OF_WEEK) == 7) {
			minus = 0;
		}
		cal.set(Calendar.DATE, (cal.get(Calendar.DATE) - minus));

		String date = cal.get(Calendar.YEAR) + "-"
				+ (cal.get(Calendar.MONTH) + 1) + "-" + cal.get(Calendar.DATE);
		// System.out.println("Date : " + date);
		return date;

	}

	public String getSourceEmailAddress() throws Exception {
		return new SettingsDoa().getSourceEmailAddress();

	}

	public String[] getUtilityRunningTime() {
		return new SettingsDoa().getUtilityRunningTime();

	}

	public int setUtilityLastUpdatedDate(String curDate) {
		// TODO Auto-generated method stub
		return new SettingsDoa().setUtilityLastUpdatedDate(curDate);

	}

	public static String getScheduledMessageDescriptionForEmployee(
			SchedulingDto dto) {
		// TODO Auto-generated method stub
		String message = "";
		if (dto != null && dto.getSchedulingFromDate() != null
				&& !dto.getSchedulingFromDate().equals("")
				&& dto.getSchedulingToDate() != null
				&& !dto.getSchedulingToDate().equals("")) {
			EmployeeDto empdto = new EmployeeDao()
					.getEmployeeDetailsBasedOnSubscriptionID(dto
							.getSubscriptionId());
			EmployeeDto spoc=new EmployeeDao().getEmployeeAccurate( dto.getScheduledBy());
			System.out.println("subscription Id :"+dto.getSubscriptionId());		    
		    
			message = "Hi "
					+ empdto.getDisplayName()
					+ "! Your cab has been Scheduled from "
					+ dto.getSchedulingFromDate()
					+ " by " +
					spoc.getDisplayName()+
					".<br/> <br/>"
					+" For any change or for queries, call transport help desk at "+SettingsConstant.transportTeamNumber+" or mail us at "+SettingsConstant.transportTeamEmail
					+ "<br/><br/>Please click on the provided link below to view your updated schedule.<br/>"
					+ "<a href='"
					+ new SettingsDoa().getDomainName()
					+"'>"+new SettingsDoa().getDomainName()+"</a>\n"
					+ "<br/><br/>Thanks & Regards,<br/>" 
					+ "Transport Facility Team."
					
					
					+"<br/>---------------<br/> <i><u>Note:</u> This is a system generated email. Please do not reply.</i>";
		}

		return message;
	}

	public static String getScheduledMessageTitleDescriptionForEmployee() {
		// TODO Auto-generated method stub
		return "Schedule Created ";
	}

	// to get message for altered list of messages
	public String getAlteredScheduleListMessage(
			ArrayList<ScheduleAlterDto> scheduleAlterDtoList, String user) {
		String spocMan = "";
		EmployeeDto emp = new EmployeeDao()
				.getEmployeeDetailsBasedOnScheduleID(scheduleAlterDtoList
						.get(0).getScheduleId());
		EmployeeDto spoc = new EmployeeDao().getEmployeeAccurate(user);
		if (spoc != null) {
			spocMan = spoc.getEmployeeFirstName() + " "
					+ spoc.getEmployeeLastName();
		} else {
			spocMan = "Spoc/Manager/Transport Manager";
		}
		/*
		 * EmployeeDto spoc2 =new EmployeeDao().getEmployeeAccurate( new
		 * EmployeeSubscriptionService
		 * ().getEmployeeSubscriptionDetails(emp.getEmployeeID
		 * ()).getSupervisor1());
		 * 
		 * if(scheduleAlterDtoList!=null&&scheduleAlterDtoList.size()>0) {
		 * if(spoc1.getEmployeeID().equals(spoc2.getEmployeeID())) {
		 * spocMan=spoc1.getEmployeeFirstName() + " " +
		 * spoc1.getEmployeeLastName(); }else {
		 * spocMan=spoc1.getEmployeeFirstName() + " " +
		 * spoc1.getEmployeeLastName() + " or  " + spoc1.getEmployeeFirstName()
		 * + " " + spoc1.getEmployeeLastName(); } }
		 */
		String message = "Hi "
				+ emp.getEmployeeFirstName()
				+ ",<br/><br/>Your schedule has been modified by "
				+ spocMan
				+ "<br/><br/>. Please click on below link to view the schedule."
				+ "<br/><br/>Regards,<br/>" 
				+ "<a href='"
				+ new SettingsDoa().getDomainName()
				+"'>"+new SettingsDoa().getDomainName()+"</a>\n" 
					+" Transport Team."
					+"</a>"
				+ " <br/>---------------<br/> <i><u>Note:</u> This is a system generated email. Please do not reply.</i>";

		return message;
	}
	
	
	public static String getPasswordResetMessage(EmployeeDto dto)
	{
		return "Hi "
				+dto.getDisplayName()
				
				+",<br/><br/>Your Password has been reset. To access ATOm use the below information.</br></br> "
				+ ".<br/><br/>"
				+"Link - <a href='"+new SettingsDoa().getDomainName()+"'>ATOm</a>"
				+"<br/><br/>Login Id - "+dto.getLoginId()
				+"<br/><br/>Password - "+dto.getPassword()
				+"<br/><br/>Regards,<br/>" 
				+ "<a href='"
				+ new SettingsDoa().getDomainName()
				+"'>"+new SettingsDoa().getDomainName()+"</a>\n"
						+" Transport Team."
						+"</a>"+"<br/>---------------<br/> <i><u>Note:</u> This is a system generated email. Please do not reply.</i>";
	}
	
	public ArrayList<SettingsDTO> getSettings(String property){
		return new SettingsDoa().getSettings(property);
	}
	
	public ArrayList<SettingsDTO> getSettingsValues(SettingsDTO pDto) {
		return new SettingsDoa().getSettingsValues(pDto);
	}
	
	public  SettingsDTO getSettingValue(String property){
		SettingsDTO dto = new SettingsDTO();
		dto.setProperty(property);
		 
		return getSettingValue(dto);
	}

	public  SettingsDTO getSettingValue(SettingsDTO property){
		 return new SettingsDoa().getSettingsValue(property);
		 
	}
	
	public String getSiteSetting(int siteId, String property) {
		String value=null;
		try {
			SettingsDTO dto = new SettingsDTO();
		dto.setSiteid( siteId);
		dto.setProperty(property);
		value = getSettingValue(dto).getValue();
		System.out.println(value);
		
		}catch(Exception e) {
			System.out.println("Exception : " +e);
		}
		return value;
	}
	

	public int forceUpdateSettings(SettingsDTO settingsDto) {
		 return new SettingsDoa().forceUpdateSettings(settingsDto);
		
	}




	public ArrayList<SettingsDTO> getSettingsStrings(String module,
			String subModule) {
		return new SettingsDoa().getSettingsStrings(module, subModule);	
	}
	public ArrayList<SettingsDTO> getSettingsStrings(String module,
			String subModule,String siteId,String projectUnit) {
		return new SettingsDoa().getSettingsStrings(module, subModule,siteId,projectUnit);	
	}

	public static int updateSettings(SettingsDTO dto) {
		//  
		return new SettingsDoa().addSettings(dto); 
	}

	
	
}
