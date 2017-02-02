/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.agiledge.atom.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.agiledge.atom.commons.OtherFunctions;
import com.agiledge.atom.dbConnect.DbConnect;
import com.agiledge.atom.dto.EmployeeDto;
import com.agiledge.atom.dto.EmployeeSubscriptionDto;
import com.agiledge.atom.dto.ScheduleAlterDto;
import com.agiledge.atom.dto.ScheduleMailDto;
import com.agiledge.atom.mail.SendMail;
import com.agiledge.atom.mail.SendMailFactory;
import com.agiledge.atom.service.EmployeeService;



/**
 * 
 * @author 123
 */
public class MailDao {

	// to retrive schedulte list based on subscription id

	public ScheduleMailDto getScheduleListMessage(String subscriptionID) {
		String message = "";
		ScheduleMailDto returnDto = new ScheduleMailDto();
		DbConnect ob = DbConnect.getInstance();

		Connection con = ob.connectDB();
		Statement stmt =null;
		ResultSet rs = null;
		try {
			// String query =
			// "select a.scheduleId, a.date, a.login_time, a.logout_time, a.status  from employee_schedule s join employee_schedule_alter a on s.id=a.scheduleId and s.subscription_id="
			// + subscriptionID + "";

			String query = "select e.EmailAddress, e.displayname as EmployeeName, e1.id supervisorID, e1.displayname supervisorName, e1.EmailAddress supervisorEmailAddress, project, from_date, to_date, login_time, logout_time from employee_schedule s join employee e on s.employee_id=e.id join employee e1 on s.scheduledBy=e1.id where subscription_id="
					+ subscriptionID;
			stmt = con.createStatement();
			rs = stmt.executeQuery(query);
			if (rs.next()) {

				message = "Hi " + rs.getString("EmployeeName")
						+ ". Your transportation has been scheduled from "
						+ rs.getString("from_date") + " with "
						+ rs.getString("login_time") + " as login time and "
						+ rs.getString("logout_time") + " as logout time"
						+ " up to " + rs.getString("to_date")
						+ ". Your transportation is scheduled by "
						+ rs.getString("supervisorName");

				returnDto.getMail().setDescription(message);

				returnDto.getMail().setSubject("Transportation Scheduled.");
				returnDto.getMail().setToAddress(rs.getString("EmailAddress"));
				returnDto.getSupervisor().setEmployeeID(
						rs.getString("supervisorID"));
				returnDto.getSupervisor().setEmailAddress(
						rs.getString("supervisorEmailAddress"));

			}


		}

		catch (Exception e) {
			System.out.println("Error1111" + e);
		}

		finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(stmt);
			DbConnect.closeConnection(con);		}

		return returnDto;
	}

	/*
	 * public String getAlteredScheduleListMessage(ArrayList<ScheduleAlterDto>
	 * scheduleAlterDtoList ) { String message="";
	 * 
	 * message=
	 * "Hi . Your transportation schedule has been modified by SPOC . Now the modified details are following."
	 * + "\n  -DATE-    -LOGIN-  -LOGOUT-" + "\n-----------------------------";
	 * 
	 * String details=""; for (ScheduleAlterDto scheduleAlterDto :
	 * scheduleAlterDtoList) {
	 * 
	 * details = details + "\n" + scheduleAlterDto.getDate() + ", " +
	 * scheduleAlterDto.getLoginTime() + ", " +
	 * scheduleAlterDto.getLogoutTime();
	 * 
	 * 
	 * 
	 * 
	 * } message = message + details + "\n" + "  ------------------------      "
	 * + " ";
	 * 
	 * return message; }
	 */
	// message for schedule alter cancel
	public String getAlterCancelledScheduleListMessage(
			ArrayList<ScheduleAlterDto> scheduleAlterDtoList, EmployeeDto dto,
			String user) {
		String message = "";
		System.out.println("scheduled by : " + user);
		EmployeeDto doneBy = new EmployeeDao().getEmployeeAccurate(user);
		String subid = new EmployeeSubscriptionDao()
				.getSubscriptionIdFromScheduleId(scheduleAlterDtoList.get(0)
						.getScheduleId());
		message = "Hi " + dto.getEmployeeFirstName() + " "
				+ dto.getEmployeeLastName()
				+ ".<br/><br/>Your transportation schedule has been modified by "
				+ doneBy.getEmployeeFirstName() + " "
				+ doneBy.getEmployeeLastName()
				+ ".<br/><br/>The cancelled schedules are following."
				+ "<br/><br/>  -DATE-    <br/>" + "---------- ";

		String details = "";
		for (ScheduleAlterDto scheduleAlterDto : scheduleAlterDtoList) {

			details = details + "<br/>" + scheduleAlterDto.getDate();

		}
		message = message
				+ details
				+ "<br/>"
				+ "-------------------<br/><br/> "
				+ "<a href='"
				+ new SettingsDoa().getDomainName()
				+ "emp_subscriptionHistory.jsp?subid="
				+ subid
				+ "'  >Click here for details</a>  <br/><br/>Regards,<br/><br/>Transport Team<br/>---------------<br/><br/> <i><u>Note:</u> This is a system generated email. Please do not reply.</i>";

		return message;
	}

	public String getEmployeeEmailByScheduleId(String scheduleID) {

		DbConnect ob = DbConnect.getInstance();
		String email = "";
		Connection con = ob.connectDB();
		Statement stmt =null;
		ResultSet rs = null;
		try {
			// String query =
			// "select a.scheduleId, a.date, a.login_time, a.logout_time, a.status  from employee_schedule s join employee_schedule_alter a on s.id=a.scheduleId and s.subscription_id="
			// + subscriptionID + "";

			String query = " select emp.emailaddress from employee_schedule sch join employee_subscription sub  on sch.subscription_id=sub.subscriptionID join employee emp on emp.id=sub.empID where sch.id="
					+ scheduleID;
			stmt = con.createStatement();
			rs = stmt.executeQuery(query);
			if (rs.next()) {

				email = rs.getString(1);

			}
			
		}

		catch (Exception e) {
			System.out.println("Error1111" + e);
		}

		finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(stmt);
			DbConnect.closeConnection(con);
		}

		return email;

	}

	// to get email asch.to_dateddress of employees whose based on booking
	// expiry date

	public List<EmployeeSubscriptionDto> getEmployeesNearToBookingExpiry(
			String date1, String date2) {
		List<EmployeeSubscriptionDto> dtoList = new ArrayList<EmployeeSubscriptionDto>();

		// System.out.println("date 1 : " + date1 + " date 2: " + date2);

		DbConnect db = DbConnect.getInstance();
		Connection con = db.connectDB();

		Statement stmt = null;
		// System.out.println("Query : " + query);
		ResultSet rs = null;
		String query = " select sch.to_date as expiryDate, emp.id as employeeID ,emp.displayname as employeename, sup.id supervisorID, sup.displayname  as supervisorname, sup.emailaddress supervisoremailaddress, spoc.id spocID, spoc.employeefirstname as spocFirstName, spoc.employeelastname as spocLastName, spoc.emailaddress as spocemailaddress from employee_schedule sch  join employee_subscription sub   on sch.subscription_id=sub.subscriptionID  join employee emp  on emp.id=sub.empID  join employee sup on sub.supervisor=sup.id join employee spoc on sub.spoc=spoc.id where  sch.to_date >='"
				+ date1
				+ "' and sch.to_date<='"
				+ date2
				+ "' order by supervisorID ";
		try {

			stmt = con.createStatement();
			// System.out.println("Query : " + query);
			rs = stmt.executeQuery(query);

			while (rs.next()) {

				EmployeeSubscriptionDto dto = new EmployeeSubscriptionDto();

				EmployeeDto employee = new EmployeeDto();
				EmployeeDto supervisor = new EmployeeDto();
				EmployeeDto spoc = new EmployeeDto();

				employee.setEmployeeID(rs.getString("employeeID"));
				employee.setEmployeeFirstName(rs.getString("employeename"));
				supervisor.setEmployeeID(rs.getString("supervisorID"));
				supervisor.setEmployeeFirstName(rs
						.getString("supervisorname"));				
				supervisor.setEmailAddress(rs
						.getString("supervisoremailaddress"));

				spoc.setEmployeeID(rs.getString("spocID"));
				spoc.setEmployeeFirstName(rs.getString("supervisorname"));
				spoc.setEmailAddress(rs.getString("spocemailaddress"));
				dto.setSupervisor1(rs.getString("supervisorID"));
				dto.setSupervisor2(rs.getString("spocID"));
				dto.setEmployee(employee);
				dto.setSupervisor(supervisor);
				dto.setSpoc(spoc);
				dto.setUnsubscriptionDate(rs.getString("expiryDate"));

				dtoList.add(dto);
				// System.out.println("-");

				// System.out.println("Dao : Supervisor : " +
				// dto.getSupervisor().getEmployeeFirstName() +
				// " " + dto.getSupervisor().getEmployeeLastName() );
				//
				// System.out.println("Dao : Spoc : " +
				// dto.getSpoc().getEmployeeFirstName() +
				// " " + dto.getSpoc().getEmployeeLastName() );

			}
			
		}

		catch (Exception e) {
			System.out.println("Error " + e);
		}

		finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(stmt);
			DbConnect.closeConnection(con);
		}

		return dtoList;

	}

	public void sendMailForAplChange(String employeeID) {
		 
		try{
			EmployeeDto dto = new EmployeeDao().getEmployeeAccurate(employeeID);
			String title = "APL Update/Change Notification";
			String message="Hi " + dto.getDisplayName() +
					"<br/><br/>"+
					
					"Your APL has been updated/changed on ATOm. "+
					"<br/><br/>"+
					" " 
					+ "<a href='"
					+ new SettingsDoa().getDomainName()
					+ "/SubscriptionSelector' >" 
					+ "Click here " 
					+ " </a> "
					+" for details."
					+"<br/><br/>"
					+"Regards,<br/>"
					 
					+ "<a href='"
							+ new SettingsDoa().getDomainName()
							+ "' >" 
							+ "Transport Team"
							+ " </a> "
					 + "<br/>---------------<br/> <i><u>Note:</u> This is a system generated email. Please do not reply.</i>";
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
			
			
			mail.send(dto.getEmailAddress(), title, message);
					 
			
		}catch(Exception e)
		{
			System.out.println("Exception in sendMailForAplChange "+e);
		}
		
	}
	
	// mail tracking methods
/*	
	public int insertMail(MailDto dto)
	{

	 
			// System.out.println("date 1 : " + date1 + " date 2: " + date2);

			DbConnect db = DbConnect.getInstance();
			Connection con = db.connectDB();
			String query1 = " insert into Mail  (message,status) values(?,?) ";
			String query2=" insert into   mail_address(mail, address, type) values(?,?,?)";
			try {

				PreparedStatement stmt = con.prepareStatement(query1);
				
				// System.out.println("Query : " + query);
				ResultSet rs = stmt.executeQuery(query1);

				while (rs.next()) {

					EmployeeSubscriptionDto dto = new EmployeeSubscriptionDto();

					EmployeeDto employee = new EmployeeDto();
					EmployeeDto supervisor = new EmployeeDto();
					EmployeeDto spoc = new EmployeeDto();

					employee.setEmployeeID(rs.getString("employeeID"));
					employee.setEmployeeFirstName(rs.getString("employeename"));
					supervisor.setEmployeeID(rs.getString("supervisorID"));
					supervisor.setEmployeeFirstName(rs
							.getString("supervisorname"));				
					supervisor.setEmailAddress(rs
							.getString("supervisoremailaddress"));

					spoc.setEmployeeID(rs.getString("spocID"));
					spoc.setEmployeeFirstName(rs.getString("supervisorname"));
					spoc.setEmailAddress(rs.getString("spocemailaddress"));
					dto.setSupervisor1(rs.getString("supervisorID"));
					dto.setSupervisor2(rs.getString("spocID"));
					dto.setEmployee(employee);
					dto.setSupervisor(supervisor);
					dto.setSpoc(spoc);
					dto.setUnsubscriptionDate(rs.getString("expiryDate"));

					dtoList.add(dto);
					// System.out.println("-");

					// System.out.println("Dao : Supervisor : " +
					// dto.getSupervisor().getEmployeeFirstName() +
					// " " + dto.getSupervisor().getEmployeeLastName() );
					//
					// System.out.println("Dao : Spoc : " +
					// dto.getSpoc().getEmployeeFirstName() +
					// " " + dto.getSpoc().getEmployeeLastName() );

				}
				DbConnect.closeResultSet(rs);
				DbConnect.closeStatement(stmt);

				DbConnect.closeConnection(con);

			}

			catch (Exception e) {
				System.out.println("Error " + e);
			}

			finally {
				try {
					con.close();
				} catch (Exception ex) {

				}
			}

			return dtoList;

		
		
	}
*/
}
