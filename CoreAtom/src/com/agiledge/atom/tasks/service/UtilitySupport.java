
package com.agiledge.atom.tasks.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import com.agiledge.atom.constants.SettingsConstant;
import com.agiledge.atom.dao.EmployeeDao;
import com.agiledge.atom.dao.ProjectDao;
import com.agiledge.atom.dao.ScheduleModifyCancelDao;
import com.agiledge.atom.dao.SettingsDoa;
import com.agiledge.atom.dao.VehicleDao;
import com.agiledge.atom.dto.EmployeeDto;
import com.agiledge.atom.dto.ProjectDto;
import com.agiledge.atom.service.EmployeeSubscriptionService;
import com.agiledge.atom.service.MailService;
import com.agiledge.atom.service.PayrollService;
import com.agiledge.atom.service.SchedulingService;
import com.agiledge.atom.service.SettingsService;
import com.agiledge.atom.task.dao.GetEmps;
import com.agiledge.atom.task.dao.GetProject;

public class UtilitySupport implements Runnable {

	public static Date currentDate = new Date();

	public void run() {
		System.out
				.println("WORKING THE UTLITY FIRSTv \n currrenttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttt");
		ArrayList<EmployeeDto> ActualEmplist = null;

		ArrayList<EmployeeDto> ExistingEmplist = null;

		ArrayList<ProjectDto> ActualProjectList = null;
		ArrayList<ProjectDto> ExistingProjectList = null;
		HashMap<String, ProjectDto> ActualProjectJson = null;
		GetEmps getEmpsObj = null;
		GetProject getProjectObj = null;
		EmployeeDao employeeDaoObj = null;
		ProjectDao projectDaoObj = null;
		ScheduleModifyCancelDao scheduleModifyCancelDao = null;
		DateFormat formatter;
		EmployeeSubscriptionService employeeSubscriptionService = null;
		try {
			formatter = new SimpleDateFormat("yyyy-MM-dd");
			String curDate = formatter.format(new Date());
			Calendar cal = Calendar.getInstance();
			cal.setTime(new Date());
			cal.add(Calendar.DATE, -1);
			String yesterDay = formatter.format(cal.getTime());
	if (isUtilityRanOn(curDate) == false) {
				try {
					if (SettingsConstant.pullEmployee.equalsIgnoreCase("yes")) {
						getEmpsObj = GetEmps.getSource();
						ActualEmplist = getEmpsObj.getEmps();
						System.out.println("Employee got from central DB size "
								+ ActualEmplist.size());
						if (ActualEmplist != null && ActualEmplist.size() > 0) {
							employeeDaoObj = new EmployeeDao();
							employeeDaoObj.UpdateEmpsDBConnet();
							employeeDaoObj.setAutoCommit(false);
							ExistingEmplist = employeeDaoObj.getAllEmps();
							employeeDaoObj.setEmpLists(ActualEmplist,
									ExistingEmplist);
						}
					}
				} catch (Exception e) {
					System.out.println("Error in employee update" + e);
				}
				
				
				try {
					if(SettingsConstant.pullEmployee.equalsIgnoreCase("YES"))
					{
						employeeDaoObj.closeConnection();
			     	} 
					}catch (Exception ex) {
					ex.printStackTrace();
				}

				// Emploee Update Ends here
				
				// SUbscription Starts here
				try {
					employeeSubscriptionService = new EmployeeSubscriptionService();
					scheduleModifyCancelDao = new ScheduleModifyCancelDao();
					// System.out.println("Utility->Near unsubscribe");
					employeeSubscriptionService.unsubscribe(curDate);
					// ------------on 29-11-2012

					// Next line mail no requirement so commenting
					// new
					// EmployeeSubscriptionService().sendEmailToEmployeesWhoAreAboutTheSubscription(curDate,
					// 3);

					new EmployeeSubscriptionService()
							.sendEmailToEmployeesWhoAreAboutTheUnsubscription(
									curDate, 1);
					int prior = 3;
					SchedulingService schedulingService = new SchedulingService();
					for (; prior >= 1; prior--) {
						schedulingService
								.sendEmailAboutScheduleExpiryToSupervisor(
										curDate, prior);
					}
					// new SchedulingService()
					// .sendEmailAboutScheduleExpiryToEmployee(curDate, 2);

					// -------------
					scheduleModifyCancelDao.scheduleCancel();
					try {
						System.out.println("subscription update");
						employeeSubscriptionService
								.modifySubscriptionEffective(curDate);
					} catch (Exception e) {
						System.out
								.println("Error in modify subscription update "
										+ e);
					}
					System.out.println("before subscribe");
					new EmployeeSubscriptionService().subscribe(curDate);
					// SUbscription ends here here
				} catch (Exception e) {
					System.out.println("errro in unsubscription update" + e);
				}
				try {
					if(SettingsConstant.comp.equalsIgnoreCase("cd")||SettingsConstant.comp.equalsIgnoreCase("cd1"))
					{
					projectDaoObj = new ProjectDao();
					projectDaoObj.UpdateProjectDBConnet();
					getProjectObj = new GetProject();
					if (GetEmps.sourceFlag.equalsIgnoreCase("cd")) {
						ActualProjectJson = getProjectObj.getProjectJSON();
					} 
					ExistingProjectList = projectDaoObj.getAllProjects();
					projectDaoObj.setprojectLists(ActualProjectList,
							ExistingProjectList, ActualProjectJson);
					if (GetEmps.sourceFlag.equalsIgnoreCase("cd")) {
						projectDaoObj.updateProjectBasedEmployee();
					} 
					projectDaoObj.closeConnection();
					}
				} catch (Exception e) {
					System.out.println("ERROR in project" + e);
					try {
						projectDaoObj.closeConnection();
					} catch (Exception ex) {
					}
				}
				// SUbscription Ends here
				// Payroll Starts here
				
				try {
					System.out.println("payroll started:::::::::::::::::::::::");
					if(SettingsConstant.comp.equalsIgnoreCase("cd")||SettingsConstant.comp.equalsIgnoreCase("cd1"))
					{
						System.out.println("payroll stmt");
					new PayrollService().makePayroll();
					}
					// Payroll Ends here
					System.out.println("before date updating");
					// updated to day as last updated date
					new SettingsService().setUtilityLastUpdatedDate(curDate);
					System.out.println("after date updation");
				} catch (Exception ex) {
					System.out.println("Error in payroll " + ex);
				}
				
				try {
					System.out.println("in clean");
					new VehicleDao().cleanVehiclePosition();
				} catch (Exception ex) {
					System.out.println("Error Cleaning Vehicle position " + ex);
				}
				try{
					if(SettingsConstant.comp.equalsIgnoreCase("gss")||SettingsConstant.comp.equalsIgnoreCase("gss1"))
					{ 
						Calendar calObj = Calendar.getInstance();
				     	if(calObj.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY)
				    	{
						System.out.println("sending no show mail");
						new MailService().NoShowMail();
						System.out.println("sent no show mail");
					  }
				     }
					}
					catch(Exception e){System.out.println("Error in mail " + e);}
					
			

			}
			
		} catch (Exception e) {
			System.out.println("Error in utility run" + e);
			e.printStackTrace();
		} finally {
		}
	}
	Date getUtiltiyLastUpdatedDate() {
		System.out.println("1111111111111");
		Date date = SettingsDoa.getUtilityLastUpdatedDate();
		System.out.println("222222222222222222222");
		return date;
	}
	public void vehiclePositionTableChange() {
	}

	boolean isUtilityRanOn(String currentDate) {
		System.out.println("IN RAN ON" + currentDate);
		Date lastUpdated = getUtiltiyLastUpdatedDate();

		// System.out.println(currentDate+"  Status  "+currentDate.toString().compareTo(lastUpdatedDate.toString())+"Utilty Ran Last on "
		// + lastUpdatedDate);
		System.out.println(lastUpdated + "return");
		if (lastUpdated == null
				|| lastUpdated.toString().compareTo(currentDate.toString()) < 0) {

			System.out.println("return false");
			return false;
		} else {
			return true;
		}

	}

	public void cleanUp() {
		// System.out.println("ENDING___");
	}

}
