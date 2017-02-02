/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.agiledge.atom.dao;

import java.awt.font.LineMetrics;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.agiledge.atom.commons.OtherFunctions;
import com.agiledge.atom.dbConnect.DbConnect;
import com.agiledge.atom.dto.ScheduledEmpDto;



/**
 * 
 * @author Administrator
 */
public class ScheduledEmpDao {
	public ArrayList<ScheduledEmpDto> getScheduledEmp(String site) {
		DbConnect ob = DbConnect.getInstance();
		Connection con = ob.connectDB();
		ScheduledEmpDto scheduledEmpDtoObj = null;
		ArrayList<ScheduledEmpDto> scheduledEmpList = new ArrayList<ScheduledEmpDto>();
		ResultSet rs = null;
		Statement st = null;
		try {
			String subQuery = "";
			// if(site!=null&&!site.equals(""))
			// {
			subQuery = " and e.site=" + site + " ";
			// }
			st = con.createStatement();
			String query = "select e.id as empid,es.id as scheduleId,e.personnelNo,(e.displayname) as EmployeeFirstName,(e1.displayname) as scheduledBy,es.project,pr.description,es.from_date,es.to_date,es.login_time,es.logout_time,es.statusDate as statusDate,(select count(*) from employee_schedule_alter esa where esa.scheduleId=es.id and esa.login_time!='weekly off' and esa.logout_time!='weekly off') as multistatus from employee_schedule es,employee e,employee e1,employee_subscription esub,atsconnect pr where es.to_date>=curdate() and es.employee_id=e.id "
					+ subQuery
					+ " and es.status='a' and es.scheduledBy=e1.id and pr.id=es.project and es.subscription_id=esub.subscriptionID order by e.displayName asc,es.from_date desc";
			 System.out.println("Query : "+ query);
			rs = st.executeQuery(query);
			while (rs.next()) {
				scheduledEmpDtoObj = new ScheduledEmpDto();
				scheduledEmpDtoObj.setEmployeeId(rs.getString("personnelNo"));
				scheduledEmpDtoObj.setScheduleId(rs.getString("scheduleId"));
				scheduledEmpDtoObj.setEmployeePersonnelNo(rs
						.getString("personnelNo"));
				scheduledEmpDtoObj.setEmployeeName(rs
						.getString("EmployeeFirstName"));
				scheduledEmpDtoObj.setScheduledBy(rs.getString("scheduledBy"));
				scheduledEmpDtoObj.setProject(rs.getString("project"));
				scheduledEmpDtoObj.setProjectDescription(rs
						.getString("description"));
				scheduledEmpDtoObj.setFrom_date("" + rs.getDate("from_date"));
				scheduledEmpDtoObj.setTo_date("" + rs.getDate("to_date"));
				scheduledEmpDtoObj.setLoginTime(rs.getString("login_time"));
				scheduledEmpDtoObj.setLogoutTime(rs.getString("logout_time"));
				scheduledEmpDtoObj.setStatusDate(rs.getString("statusDate"));
				scheduledEmpDtoObj.setMultistatus(rs.getInt("multistatus"));

				scheduledEmpList.add(scheduledEmpDtoObj);
			}
		} catch (Exception e) {
			System.out.println("error in scheduled Dao" + e);
			Logger.getLogger(ScheduledEmpDao.class.getName()).log(Level.SEVERE,
					null, e);
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);

		}
		return scheduledEmpList;
	}
	
	
		public ArrayList<ScheduledEmpDto> getSearchScheduledEmp(String site, String emppersonnelno) {
			DbConnect ob = DbConnect.getInstance();
			Connection con = ob.connectDB();
			ScheduledEmpDto scheduledEmpDtoObj = null;
			ArrayList<ScheduledEmpDto> scheduledEmpList = new ArrayList<ScheduledEmpDto>();
			ResultSet rs = null;
			Statement st = null;
			try {
				String subQuery = "";
				// if(site!=null&&!site.equals(""))
				// {
				subQuery = " and e.site=" + site + " ";
				// }
				st = con.createStatement();
				String query = "select e.id as empid,es.id as scheduleId,e.personnelNo,(e.displayname) as EmployeeFirstName,(e1.displayname) as scheduledBy,es.project,pr.description,es.from_date,es.to_date,es.login_time,es.logout_time,es.statusDate as statusDate,(select count(*) from employee_schedule_alter esa where esa.scheduleId=es.id and esa.login_time!='weekly off' and esa.logout_time!='weekly off') as multistatus from employee_schedule es,employee e,employee e1,employee_subscription esub,atsconnect pr where es.to_date>=curdate() and es.employee_id=e.id "
						+ subQuery
						+ " and es.status='a' and es.scheduledBy=e1.id and pr.id=es.project and es.subscription_id=esub.subscriptionID and e.personnelNo ='" + emppersonnelno + "' order by e.displayName asc,es.from_date desc";
				 System.out.println("Query : "+ query);
				rs = st.executeQuery(query);
				while (rs.next()) {
					scheduledEmpDtoObj = new ScheduledEmpDto();
					scheduledEmpDtoObj.setEmployeeId(rs.getString("personnelNo"));
					scheduledEmpDtoObj.setScheduleId(rs.getString("scheduleId"));
					scheduledEmpDtoObj.setEmployeePersonnelNo(rs
							.getString("personnelNo"));
					scheduledEmpDtoObj.setEmployeeName(rs
							.getString("EmployeeFirstName"));
					scheduledEmpDtoObj.setScheduledBy(rs.getString("scheduledBy"));
					scheduledEmpDtoObj.setProject(rs.getString("project"));
					scheduledEmpDtoObj.setProjectDescription(rs
							.getString("description"));
					scheduledEmpDtoObj.setFrom_date("" + rs.getDate("from_date"));
					scheduledEmpDtoObj.setTo_date("" + rs.getDate("to_date"));
					scheduledEmpDtoObj.setLoginTime(rs.getString("login_time"));
					scheduledEmpDtoObj.setLogoutTime(rs.getString("logout_time"));
					scheduledEmpDtoObj.setStatusDate(rs.getString("statusDate"));
					scheduledEmpDtoObj.setMultistatus(rs.getInt("multistatus"));

					scheduledEmpList.add(scheduledEmpDtoObj);
				}
			} catch (Exception e) {
				System.out.println("error in scheduled Dao" + e);
				Logger.getLogger(ScheduledEmpDao.class.getName()).log(Level.SEVERE,
						null, e);
			} finally {
				DbConnect.closeResultSet(rs);
				DbConnect.closeStatement(st);
				DbConnect.closeConnection(con);

			}
			return scheduledEmpList;
		}

		public ArrayList<ScheduledEmpDto> getScheduledEmp(long delegaterId,
				long managerId, String site ) {
			DbConnect ob = DbConnect.getInstance();
			Connection con = ob.connectDB();
			ScheduledEmpDto scheduledEmpDtoObj = null;
			ArrayList<ScheduledEmpDto> scheduledEmpList = new ArrayList<ScheduledEmpDto>();
			ResultSet rs = null;
			Statement st = null;
			try {
				st = con.createStatement();
				String subQuery = "";
				if (site != null && !site.equals("")) {
					subQuery = " and e.site=" + site + " ";
				}
				
				String query = "select e.id as empid,es.id as scheduleId,e.personnelNo,(e.displayname) as EmployeeFirstName,(e1.displayname) as scheduledBy,es.project,pr.description,es.from_date,es.to_date,es.login_time,es.logout_time,es.statusDate as statusDate,(select count(*) from employee_schedule_alter esa where esa.scheduleId=es.id and esa.login_time!='weekly off' and esa.logout_time!='weekly off') as multistatus from employee_schedule es,employee e,employee e1,employee_subscription esub,atsconnect pr where es.to_date>= curdate() and es.employee_id=e.id "
						+ subQuery
						+ " and es.status='a' and es.scheduledBy=e1.id and pr.id=es.project and es.subscription_id=esub.subscriptionID and (esub.supervisor="
						+ managerId
						+ " or esub.spoc="
						+ managerId
						+ " or esub.supervisor="
						+ delegaterId
						+ " or esub.spoc="
						+ delegaterId
						+ " or e.linemanager="+managerId+" or e.linemanager="+delegaterId+" or e.linemanager in (select id from employee where linemanager="+managerId+") or e.id="+managerId+" ) order by e.displayName asc,es.from_date desc";
			//	System.out.println("Query : " + query);
				rs = st.executeQuery(query);
				while (rs.next()) {
					scheduledEmpDtoObj = new ScheduledEmpDto();
					scheduledEmpDtoObj.setEmployeeId(rs.getString("empid"));
					scheduledEmpDtoObj.setScheduleId(rs.getString("scheduleId"));
					scheduledEmpDtoObj.setEmployeePersonnelNo(rs
							.getString("personnelNo"));
					scheduledEmpDtoObj.setEmployeeName(rs
							.getString("EmployeeFirstName"));
					scheduledEmpDtoObj.setScheduledBy(rs.getString("scheduledBy"));
					// System.out.println("scheduled By : ..........  "+
					// scheduledEmpDtoObj.getScheduledBy());
					scheduledEmpDtoObj.setProject(rs.getString("project"));
					scheduledEmpDtoObj.setProjectDescription(rs
							.getString("description"));
					scheduledEmpDtoObj.setFrom_date("" + rs.getDate("from_date"));
					scheduledEmpDtoObj.setTo_date("" + rs.getDate("to_date"));
					scheduledEmpDtoObj.setLoginTime(rs.getString("login_time"));
					scheduledEmpDtoObj.setLogoutTime(rs.getString("logout_time"));
					scheduledEmpDtoObj.setStatusDate(rs.getString("statusDate"));
					scheduledEmpDtoObj.setMultistatus(rs.getInt("multistatus"));
					scheduledEmpList.add(scheduledEmpDtoObj);
				}
			} catch (Exception e) {
				System.out.println("error in scheduled Dao" + e);
				Logger.getLogger(ScheduledEmpDao.class.getName()).log(Level.SEVERE,
						null, e);
			} finally {
				DbConnect.closeResultSet(rs);
				DbConnect.closeStatement(st);
				DbConnect.closeConnection(con);

			}
			return scheduledEmpList;
		}




	/*public ArrayList<ScheduledEmpDto> getScheduledEmp(long delegaterId,
			long managerId, String site, String employeeFilter) {
		DbConnect ob = DbConnect.getInstance();
		Connection con = ob.connectDB();
		ScheduledEmpDto scheduledEmpDtoObj = null;
		ArrayList<ScheduledEmpDto> scheduledEmpList = new ArrayList<ScheduledEmpDto>();
		ResultSet rs = null;
		Statement st = null;
		try {
			st = con.createStatement();
			String subQuery = "";
			if (site != null && !site.equals("")) {
				subQuery = " and e.site=" + site + " ";
			}
			String query = new String();
			if(employeeFilter.trim().equalsIgnoreCase("--Self--")) {
				
				query = "select e.id as empid,es.id as scheduleId,e.personnelNo,(e.displayname) as EmployeeFirstName,(e1.displayname) as scheduledBy,es.project,pr.description,es.from_date,es.to_date,es.login_time,es.logout_time,es.statusDate as statusDate,(select count(*) from employee_schedule_alter esa where esa.scheduleId=es.id and esa.login_time!='weekly off' and esa.logout_time!='weekly off') as multistatus from employee_schedule es,employee e,employee e1,employee_subscription esub,atsconnect pr where es.to_date>= curdate() and es.employee_id=e.id "
						+ subQuery
						+ " and es.status='a' and es.scheduledBy=e1.id and pr.id=es.project and es.subscription_id=esub.subscriptionID and (esub.supervisor="
						+ managerId
						+ " or esub.spoc="
						+ managerId
						+ " or esub.supervisor="
						+ delegaterId
						+ " or esub.spoc="
						+ delegaterId
						+ " or e.linemanager="+managerId+" or e.linemanager="+delegaterId+" or e.linemanager in (select id from employee where linemanager="+managerId+") or e.id="+managerId+" ) order by e.displayName asc,es.from_date desc";
			}
			else if (employeeFilter.trim().equalsIgnoreCase("--ALL--")) {
				query = "select e.id as empid,es.id as scheduleId,e.personnelNo,(e.displayname) as EmployeeFirstName,(e1.displayname) as scheduledBy,es.project,pr.description,es.from_date,es.to_date,es.login_time,es.logout_time,es.statusDate as statusDate,(select count(*) from employee_schedule_alter esa where esa.scheduleId=es.id and esa.login_time!='weekly off' and esa.logout_time!='weekly off') as multistatus from employee_schedule es,employee e,employee e1,employee_subscription esub,atsconnect pr where es.to_date>= curdate() and es.employee_id=e.id "
						+ subQuery
						+ " and es.status='a' and es.scheduledBy=e1.id and pr.id=es.project and es.subscription_id=esub.subscriptionID and (esub.supervisor="
						+ employeeFilter
						+ " or esub.spoc="
						+ employeeFilter
						+ " or esub.supervisor="
						+ employeeFilter
						+ " or esub.spoc="
						+ employeeFilter
						+ " or e.linemanager="+employeeFilter+" or e.linemanager="+employeeFilter+" or e.linemanager in (select id from employee where linemanager="+employeeFilter+") or e.id="+employeeFilter+" ) order by e.displayName asc,es.from_date desc";
			
				
			} else {
			 query = "select e.id as empid,es.id as scheduleId,e.personnelNo,(e.displayname) as EmployeeFirstName,(e1.displayname) as scheduledBy,es.project,pr.description,es.from_date,es.to_date,es.login_time,es.logout_time,es.statusDate as statusDate,(select count(*) from employee_schedule_alter esa where esa.scheduleId=es.id and esa.login_time!='weekly off' and esa.logout_time!='weekly off') as multistatus from employee_schedule es,employee e,employee e1,employee_subscription esub,atsconnect pr where es.to_date>= curdate() and es.employee_id=e.id "
						+ subQuery
						+ " and es.status='a' and es.scheduledBy=e1.id and pr.id=es.project and es.subscription_id=esub.subscriptionID and (esub.supervisor="
						+ employeeFilter
						+ " or esub.spoc="
						+ employeeFilter
						+ " or esub.supervisor="
						+ employeeFilter
						+ " or esub.spoc="
						+ employeeFilter
						+ " or e.linemanager="+employeeFilter+" or e.linemanager="+employeeFilter+" or e.linemanager in (select id from employee where linemanager="+employeeFilter+") or e.id="+employeeFilter+" ) order by e.displayName asc,es.from_date desc";}
				//delegated employee id is not given  here. May require future changes.
			String query = "select e.id as empid,es.id as scheduleId,e.personnelNo,(e.displayname) as EmployeeFirstName,(e1.displayname) as scheduledBy,es.project,pr.description,es.from_date,es.to_date,es.login_time,es.logout_time,es.statusDate as statusDate,(select count(*) from employee_schedule_alter esa where esa.scheduleId=es.id and esa.login_time!='weekly off' and esa.logout_time!='weekly off') as multistatus from employee_schedule es,employee e,employee e1,employee_subscription esub,atsconnect pr where es.to_date>= curdate() and es.employee_id=e.id "
					+ subQuery
					+ " and es.status='a' and es.scheduledBy=e1.id and pr.id=es.project and es.subscription_id=esub.subscriptionID and (esub.supervisor="
					+ managerId
					+ " or esub.spoc="
					+ managerId
					+ " or esub.supervisor="
					+ delegaterId
					+ " or esub.spoc="
					+ delegaterId
					+ " or e.linemanager="+managerId+" or e.linemanager="+delegaterId+" or e.linemanager in (select id from employee where linemanager="+managerId+") or e.id="+managerId+" ) order by e.displayName asc,es.from_date desc";
		System.out.println("Query : " + query);
			rs = st.executeQuery(query);
			while (rs.next()) {
				scheduledEmpDtoObj = new ScheduledEmpDto();
				scheduledEmpDtoObj.setEmployeeId(rs.getString("empid"));
				scheduledEmpDtoObj.setScheduleId(rs.getString("scheduleId"));
				scheduledEmpDtoObj.setEmployeePersonnelNo(rs
						.getString("personnelNo"));
				scheduledEmpDtoObj.setEmployeeName(rs
						.getString("EmployeeFirstName"));
				scheduledEmpDtoObj.setScheduledBy(rs.getString("scheduledBy"));
				// System.out.println("scheduled By : ..........  "+
				// scheduledEmpDtoObj.getScheduledBy());
				scheduledEmpDtoObj.setProject(rs.getString("project"));
				scheduledEmpDtoObj.setProjectDescription(rs
						.getString("description"));
				scheduledEmpDtoObj.setFrom_date("" + rs.getDate("from_date"));
				scheduledEmpDtoObj.setTo_date("" + rs.getDate("to_date"));
				scheduledEmpDtoObj.setLoginTime(rs.getString("login_time"));
				scheduledEmpDtoObj.setLogoutTime(rs.getString("logout_time"));
				scheduledEmpDtoObj.setStatusDate(rs.getString("statusDate"));
				scheduledEmpDtoObj.setMultistatus(rs.getInt("multistatus"));
				scheduledEmpList.add(scheduledEmpDtoObj);
			}
		} catch (Exception e) {
			System.out.println("error in scheduled Dao" + e);
			Logger.getLogger(ScheduledEmpDao.class.getName()).log(Level.SEVERE,
					null, e);
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);

		}
		return scheduledEmpList;
	}*/

	public ScheduledEmpDto getScheduledEmpBySchedule(long scheduleId) {
		DbConnect ob = DbConnect.getInstance();
		Connection con = ob.connectDB();
		ScheduledEmpDto scheduledEmpDtoObj = null;
		ResultSet rs = null;
		Statement st = null;
		try {
			st = con.createStatement();
			rs = st.executeQuery("select e.id as empid,es.id as scheduleId,e.personnelNo,(e.displayname) as EmployeeFirstName,(e1.displayname) as scheduledBy,es.project,pr.description,es.from_date,es.to_date,es.login_time,es.logout_time,es.statusDate as statusDate,(select count(*) from employee_schedule_alter esa where esa.scheduleId=es.id and esa.login_time!='weekly off' and esa.logout_time!='weekly off') as multistatus from employee_schedule es,employee e,employee e1,employee_subscription esub,atsconnect pr where es.id="
					+ scheduleId
					+ " and es.employee_id=e.id and es.scheduledBy=e1.id and es.subscription_id=esub.subscriptionID and pr.id=es.project");
			while (rs.next()) {
				scheduledEmpDtoObj = new ScheduledEmpDto();
				scheduledEmpDtoObj.setEmployeeId(rs.getString("empid"));
				scheduledEmpDtoObj.setScheduleId(rs.getString("scheduleId"));
				scheduledEmpDtoObj.setEmployeePersonnelNo(rs
						.getString("personnelNo"));
				scheduledEmpDtoObj.setEmployeeName(rs
						.getString("EmployeeFirstName"));
				scheduledEmpDtoObj.setScheduledBy(rs.getString("scheduledBy"));
				scheduledEmpDtoObj.setProject(rs.getString("project"));
				scheduledEmpDtoObj.setProjectDescription(rs
						.getString("description"));
				scheduledEmpDtoObj.setFrom_date("" + rs.getDate("from_date"));
				scheduledEmpDtoObj.setTo_date("" + rs.getDate("to_date"));
				scheduledEmpDtoObj.setLoginTime(rs.getString("login_time"));
				scheduledEmpDtoObj.setLogoutTime(rs.getString("logout_time"));
				scheduledEmpDtoObj.setStatusDate(rs.getString("statusDate"));
				scheduledEmpDtoObj.setMultistatus(rs.getInt("multistatus"));
			}
		} catch (Exception e) {
			System.out.println("error in scheduled Dao" + e);
		} finally {
			try {
				rs.close();
			} catch (SQLException ex) {
				Logger.getLogger(ScheduledEmpDao.class.getName()).log(
						Level.SEVERE, null, ex);
			}
			try {
				st.close();
			} catch (SQLException ex) {
				Logger.getLogger(ScheduledEmpDao.class.getName()).log(
						Level.SEVERE, null, ex);
			}
			try {
				con.close();
			} catch (SQLException ex) {
				Logger.getLogger(ScheduledEmpDao.class.getName()).log(
						Level.SEVERE, null, ex);
			}
		}
		return scheduledEmpDtoObj;
	}

	/*
	 * created by Noufal CC on oct 22 2012
	 */

	 
	// overloaded one of getScheduledHistory
	/*
	 * public List<ScheduledEmpDto> getScheduledHistory(String subscriptionID,
	 * String fromDate1, String toDate1)
	 * 
	 * { List<ScheduledEmpDto> scheduledList = new ArrayList<ScheduledEmpDto>();
	 * List<ScheduledEmpDto> alteredList = new ArrayList<ScheduledEmpDto>();
	 * List<ScheduledEmpDto> finalList = new ArrayList<ScheduledEmpDto>();
	 * fromDate1 = OtherFunctions.changeDateFromat(fromDate1); toDate1 =
	 * OtherFunctions.changeDateFromat(toDate1); DbConnect ob =
	 * DbConnect.getInstance(); Connection con = ob.connectDB(); String query =
	 * " SELECT id,  employee_id, case when from_date<'" + fromDate1 +
	 * "' then '" + fromDate1 +
	 * "' else from_date end from_date,  case when to_date>'" + toDate1 +
	 * "' then '" + toDate1 +
	 * "' else to_date end to_date,  login_time,logout_time,status, cancel_date from employee_schedule where subscription_id="
	 * + subscriptionID + " and from_date<='" + toDate1 + "' and to_date>='" +
	 * fromDate1 + "'"; String query1 =
	 * "  SELECT a.scheduleId, a.date, ifnull(a.login_time,'') login_time, ifnull(a.logout_time,'') logout_time, a.status from employee_schedule_alter a join employee_schedule s on a.scheduleId=s.id where s.subscription_id="
	 * + subscriptionID + " and a.date>='" + fromDate1 + "' and a.date<='" +
	 * toDate1 + "' order by a.scheduleId "; int returnInt = 0;
	 * 
	 * String m_status; int counter = 0; System.out.println("Query 1 : " +
	 * query); System.out.println("Query 2 : " + query1); Statement st1 = null;
	 * Statement st2 = null; ResultSet rs1 = null; ResultSet rs2 = null; try {
	 * 
	 * st1 = con.createStatement(); rs1 = st1.executeQuery(query);
	 * 
	 * st2 = con.createStatement(); rs2 = st2.executeQuery(query1); while
	 * (rs1.next()) { ScheduledEmpDto dto = new ScheduledEmpDto();
	 * dto.setScheduleId(rs1.getString("id")); dto.setFrom_date("" +
	 * rs1.getDate("from_date")); dto.setTo_date("" + rs1.getDate("to_date"));
	 * dto.setLoginTime(rs1.getString("login_time"));
	 * dto.setLogoutTime(rs1.getString("logout_time")); //
	 * dto.setStatus(rs1.getString("status")); //
	 * dto.setCancelDate(""+rs1.getDate("cancel_date")); if
	 * (rs1.getDate("cancel_date") != null) { dto.setTo_date("" +
	 * rs1.getDate("cancel_date")); } scheduledList.add(dto); } while
	 * (rs2.next()) { ScheduledEmpDto dto = new ScheduledEmpDto();
	 * dto.setScheduleId(rs2.getString("scheduleId")); dto.setFrom_date("" +
	 * rs2.getDate("date")); dto.setLoginTime(rs2.getString("login_time"));
	 * dto.setLogoutTime(rs2.getString("logout_time"));
	 * dto.setStatus(rs2.getString("status")); alteredList.add(dto);
	 * 
	 * } ScheduledEmpDto dto = null; boolean flag=false; int index=0; for
	 * (ScheduledEmpDto empDto : scheduledList) { flag=false; ScheduledEmpDto
	 * empDto2 = alteredList[index]; if
	 * (empDto.getScheduleId().equals(empDto2.getScheduleId())) { flag=true; if
	 * (empDto2.getFrom_date() .equals(empDto.getFrom_date())) {
	 * System.out.println
	 * (empDto2.getFrom_date()+"First "+empDto.getFrom_date()); dto = new
	 * ScheduledEmpDto(); dto.setScheduleId(empDto.getScheduleId());
	 * dto.setFrom_date(empDto.getFrom_date());
	 * dto.setTo_date(empDto2.getFrom_date());
	 * dto.setLoginTime(empDto2.getLoginTime());
	 * dto.setLogoutTime(empDto2.getLogoutTime());
	 * dto.setStatus(empDto2.getStatus()); finalList.add(dto); break; } else if
	 * (empDto.getTo_date().equals( empDto2.getFrom_date())) { dto = new
	 * ScheduledEmpDto(); dto.setScheduleId(empDto.getScheduleId());
	 * dto.setFrom_date(empDto.getTo_date());
	 * dto.setTo_date(empDto.getTo_date());
	 * dto.setLoginTime(empDto2.getLoginTime());
	 * dto.setLogoutTime(empDto2.getLogoutTime());
	 * dto.setStatus(empDto2.getStatus()); finalList.add(dto); } else {
	 * dto.setScheduleId(empDto.getScheduleId());
	 * dto.setFrom_date(empDto.getFrom_date());
	 * dto.setTo_date(OtherFunctions.getPreviousDay(empDto2.getFrom_date()));
	 * dto.setLoginTime(empDto.getLoginTime());
	 * dto.setLogoutTime(empDto.getLogoutTime()); finalList.add(dto); dto = new
	 * ScheduledEmpDto(); dto.setScheduleId(empDto.getScheduleId());
	 * dto.setFrom_date(empDto2.getFrom_date());
	 * dto.setTo_date(empDto2.getFrom_date());
	 * dto.setLoginTime(empDto2.getLoginTime());
	 * dto.setLogoutTime(empDto2.getLogoutTime());
	 * dto.setStatus(empDto2.getStatus()); finalList.add(dto); }
	 * 
	 * }
	 * 
	 * } if(flag=false) { dto = new ScheduledEmpDto();
	 * dto.setScheduleId(empDto.getScheduleId());
	 * dto.setFrom_date(empDto.getFrom_date());
	 * dto.setLoginTime(empDto.getLoginTime());
	 * dto.setLogoutTime(empDto.getLogoutTime());
	 * dto.setTo_date(empDto.getTo_date()); finalList.add(dto); } } /*
	 * System.out.println("size"+finalList.size()); for (ScheduledEmpDto empDto
	 * : finalList) { System.out.println("From date"+empDto.getFrom_date());
	 * System.out.println("TO date"+empDto.getTo_date());
	 * System.out.println("Login Time"+empDto.getLoginTime());
	 * System.out.println("Logout Time"+empDto.getLogoutTime()); }
	 */
	// }
	/*
	 * catch (Exception e) { System.out.println("ERRRO" + e);
	 * e.printStackTrace(); }
	 * 
	 * return finalList; }
	 */

	public List<ScheduledEmpDto> getScheduledHistory(String subscriptionID,
			String fromDate1, String toDate1)

	{
		System.out.println("Herereeeeeeeeeeee");
		List<ScheduledEmpDto> dtoList = new ArrayList<ScheduledEmpDto>();		
		DbConnect ob = DbConnect.getInstance();
		Connection con = ob.connectDB();
		String query = " SELECT id, subscription_id, employee_id, case when from_date<'"
				+ fromDate1
				+ "' then '"
				+ fromDate1
				+ "' else from_date end from_date,  case when to_date>'"
				+ toDate1
				+ "' then '"
				+ toDate1
				+ "' else to_date end to_date,  login_time,logout_time,status, cancel_date from employee_schedule where subscription_id="
				+ subscriptionID
				+ " and from_date<='"
				+ toDate1
				+ "' and to_date>='" + fromDate1 + "' order by from_date";
		String query1 = "  SELECT a.id,a.scheduleId, a.date, ifnull(a.login_time,'') login_time, ifnull(a.logout_time,'') logout_time, a.status from employee_schedule_alter a join employee_schedule s on a.scheduleId=s.id where s.subscription_id="
				+ subscriptionID
				+ " and a.date>='"
				+ fromDate1
				+ "' and a.date<='" + toDate1 + "' order by date ";
		fromDate1 = OtherFunctions.changeDateFromat(fromDate1);
		toDate1 = OtherFunctions.changeDateFromat(toDate1);
		String m_status;
		try {
System.out.println("query:  "+query);
System.out.println("query1:  "+query1);
			int scheduleId2 = -10;
			boolean isNext = false;
			boolean itIsFirstTime = true;
			Statement st1 = con.createStatement();
			ResultSet rs1 = st1.executeQuery(query);
			Statement st2 = con.createStatement();
			ResultSet rs2 = st2.executeQuery(query1);
			while (rs1.next()) {
				Date fromDate = new Date();
				Date toDate = new Date();
				try {
					fromDate = rs1.getDate("from_date");
					toDate = rs1.getDate("to_date");
				} catch (Exception e) {
					String fromDateString = rs1.getString("from_date");
					String toDateString = rs1.getString("to_date");
					fromDate = OtherFunctions.stringToDate(fromDateString);
					toDate = OtherFunctions.stringToDate(toDateString);
				}
				Date a_fromDate = new Date(fromDate.getYear(),
						fromDate.getMonth(), fromDate.getDate());
				Date a_toDate = new Date(toDate.getYear(), toDate.getMonth(),
						toDate.getDate());
				m_status = rs1.getString("status");
				int scheduleId1 = rs1.getInt("id");
				String scheduleid = ""+scheduleId1;
				Date alterDate;
				if (itIsFirstTime) {
					isNext = rs2.next();
					if (isNext) {
						scheduleId2 = rs2.getInt("scheduleId");
						itIsFirstTime = false;
					}
				}
				if (m_status.equals("c")) {

					toDate = rs1.getDate("cancel_date");
				}
				if (scheduleId1 != scheduleId2) {
					ScheduledEmpDto dto = new ScheduledEmpDto();
					dto.setFrom_date("" + fromDate);
					dto.setLoginTime(rs1.getString("login_time"));
					dto.setLogoutTime(rs1.getString("logout_time"));
					dto.setStatus("Scheduled");
					dto.setTo_date("" + toDate);
					dto.setScheduleId(scheduleid);
					dtoList.add(dto);
				}
				while (scheduleId1 == scheduleId2 && isNext) {
					ScheduledEmpDto dtoAlter = new ScheduledEmpDto();
					alterDate = rs2.getDate("date");
					String alterStatus = rs2.getString("status"); // -------------------
					if (a_fromDate.compareTo(alterDate) < 0
							&& toDate.compareTo(alterDate) >= 0) {
						String scheduleida = rs1.getString("id");
						a_toDate = new Date(alterDate.getYear(),
								alterDate.getMonth(), alterDate.getDate());
						a_toDate.setDate(a_toDate.getDate() - 1);
						ScheduledEmpDto dto1 = new ScheduledEmpDto();
						dto1.setFrom_date(""
								+ OtherFunctions.changeDateFromat(a_fromDate));
						dto1.setTo_date(""
								+ OtherFunctions.changeDateFromat(a_toDate));
						dto1.setLoginTime(rs1.getString("login_time"));
						dto1.setLogoutTime(rs1.getString("logout_time"));
						dto1.setStatus("Scheduled");
						dto1.setScheduleId(scheduleida);
						dtoList.add(dto1);
					} // -------------------
					if (alterStatus.equals("c")) {
						dtoAlter.setLoginTime("");
						dtoAlter.setLogoutTime("");
						dtoAlter.setStatus("Cancelled");
					} else {
						dtoAlter.setLoginTime(rs2.getString("login_time"));
						dtoAlter.setLogoutTime(rs2.getString("logout_time"));
						int rId = rs2.getInt("id");
						String rId2 = rId + "";
						dtoAlter.setScheduleAlterId(rId2);
						dtoAlter.setStatus("Edited");
					}
					dtoAlter.setFrom_date("" + alterDate);
					dtoAlter.setTo_date("");
					dtoList.add(dtoAlter);
					a_fromDate.setDate(alterDate.getDate() + 1);
					isNext = rs2.next();
					if (isNext) {
						scheduleId2 = rs2.getInt("scheduleId");
					}
				} // end of first while
				if (toDate.compareTo(a_toDate) > 0
						&& a_fromDate.compareTo(toDate) <= 0) {
					ScheduledEmpDto dto1 = new ScheduledEmpDto();
					dto1.setScheduleId(scheduleid);
					dto1.setFrom_date(""
							+ OtherFunctions.changeDateFromat(a_fromDate));
					dto1.setTo_date(""
							+ OtherFunctions.changeDateFromat(toDate));
					dto1.setLoginTime(rs1.getString("login_time"));
					dto1.setLogoutTime(rs1.getString("logout_time"));
					dto1.setStatus("Scheduled");
					dtoList.add(dto1);
				}
			} // end of second while
			DbConnect.closeResultSet(rs1, rs2);
			DbConnect.closeStatement(st1, st2);
			DbConnect.closeConnection(con);
			// ----------here searching is taken
			// place----------------------------- /*
			Date date_fromDate1 = OtherFunctions.stringToDate(fromDate1);
			Date date_toDate1 = OtherFunctions.stringToDate(toDate1);
			int i = 0;
			for (ScheduledEmpDto cDto : dtoList) {
				boolean isWithinDate = false;
				if (date_fromDate1.compareTo(OtherFunctions.stringToDate(cDto
						.getFrom_date())) < 0
						&& date_toDate1.compareTo(OtherFunctions
								.stringToDate(cDto.getFrom_date())) >= 0) {
					isWithinDate = true;
				}
				if (date_toDate1.compareTo(OtherFunctions.stringToDate(cDto
						.getTo_date())) > 0
						&& date_fromDate1.compareTo(OtherFunctions
								.stringToDate(cDto.getTo_date())) <= 0) {
					isWithinDate = true;
				}
				if (date_fromDate1.compareTo(OtherFunctions.stringToDate(cDto
						.getFrom_date())) > 0
						&& date_fromDate1.compareTo(OtherFunctions
								.stringToDate(cDto.getTo_date())) <= 0) {
					cDto.setFrom_date(OtherFunctions
							.changeDateFromat(date_fromDate1));
					isWithinDate = true;
				}
				if (date_toDate1.compareTo(OtherFunctions.stringToDate(cDto
						.getTo_date())) < 0
						&& date_toDate1.compareTo(OtherFunctions
								.stringToDate(cDto.getFrom_date())) >= 0) {
					cDto.setTo_date(OtherFunctions
							.changeDateFromat(date_toDate1));
					isWithinDate = true;
				}
				if (!isWithinDate) {
					dtoList.remove(i);
				}
				i++;
			}
			// -------------------------------------------
		} catch (Exception e) {
			DbConnect.closeConnection(con);
			System.out.println("Error : " + e);
		}
		return dtoList;
	}

	public boolean isScheduled(String subscriptionID) {
		boolean flag = false;
		DbConnect ob = DbConnect.getInstance();
		Connection con = ob.connectDB();
		try {
			Statement st = con.createStatement();
			String query = "select * from employee_schedule where subscription_id="
					+ subscriptionID;
			ResultSet rs = st.executeQuery(query);
			if (rs.next()) {
				flag = true;
			}
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);
		} catch (Exception e) {
			System.out.println("Error in isSchedued " + e);
		} finally {
			DbConnect.closeConnection(con);
		}
		return flag;
	}

	public boolean isWithinSchedules(String subscriptionID, String fromDate,
			String toDate) {
		boolean flag = false;
		DbConnect ob = DbConnect.getInstance();
		Connection con = ob.connectDB();
		Statement st = null;
		ResultSet rs = null;
		try {
			st = con.createStatement();
			String query = "select * from employee_schedule where (('"
					+ fromDate
					+ "' between from_date and ifnull(cancel_date, to_date)) or ('"
					+ toDate
					+ "' between from_date and ifnull(cancel_date, to_date)) or ('"
					+ fromDate
					+ "'<=from_date and '"
					+ toDate
					+ "'>=ifnull(cancel_date, to_date) ) ) and employee_schedule.status='a' and subscription_id="
					+ subscriptionID;
			System.out.println("Query : " + query);
			rs = st.executeQuery(query);
			if (rs.next()) {
				flag = true;
			}
		} catch (Exception e) {
			System.out.println("Error in isSchedued " + e);
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);
		}
		return flag;
	}

	public long[] getScheduledEmpByEmployee(long empId) {
		DbConnect ob = DbConnect.getInstance();
		Connection con = ob.connectDB();
		long[] scheduleIds = null;
		ResultSet rs = null;
		Statement st = null;
		try {
			st = con.createStatement();
			String query = "select id ,(select count(*) from employee_schedule where employee_id="
					+ empId
					+ " and to_date>curdate()) as count from employee_schedule where employee_id="
					+ empId + " and  to_date>curdate()";
			System.out.println("Query : " + query);
			rs = st.executeQuery(query);
			scheduleIds = null;
			int i = 0;
			if (rs.next()) {
				scheduleIds = new long[rs.getInt("count")];
				do {
					scheduleIds[i] = rs.getLong("id");
					i++;
				} while (rs.next());
			}
		} catch (Exception e) {
			System.out.println("error in scheduled Dao" + e);
			// Logger.getLogger(ScheduledEmpDao.class.getName()).log(Level.SEVERE,
			// null, e);
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);

		}
		return scheduleIds;
	}

	/*
	 * created by Noufal CC on oct 22 2012
	 */
	/*
	 * public List<ScheduledEmpDto> getScheduledHistory1(String subscriptionID)
	 * { List<ScheduledEmpDto> dtoList = new ArrayList<ScheduledEmpDto>();
	 * 
	 * DbConnect ob = DbConnect.getInstance(); Connection con= ob.connectDB();
	 * 
	 * int returnInt=0;
	 * 
	 * try{
	 * 
	 * ResultSet rs; CallableStatement cs =
	 * con.prepareCall("{call getScheduledHistory(?)}"); cs.setInt(1,
	 * Integer.parseInt(subscriptionID));
	 * 
	 * boolean flag= cs.execute();
	 * 
	 * 
	 * 
	 * rs= cs.executeQuery(); System.out.println("Here .." + rs);
	 * while(rs.next()) {
	 * 
	 * ScheduledEmpDto dto = new ScheduledEmpDto(); // toDate , loginTime ,
	 * logoutTime , type
	 * 
	 * dto.setFrom_date(rs.getString("fromDate"));
	 * dto.setTo_date(rs.getString("toDate"));
	 * dto.setLoginTime(rs.getString("loginTime"));
	 * dto.setLogoutTime(rs.getString("logoutTime"));
	 * dto.setStatus(rs.getString("type"));
	 * 
	 * System.out.println("Getting ..."); System.out.println("From Date " +
	 * rs.getString("fromdate") ); dtoList.add(dto); }
	 * 
	 * 
	 * 
	 * System.out.println(" Return value : " + returnInt); }catch(Exception e) {
	 * DbConnect.closeConnection(con); System.out.println("Error : " + e);
	 * 
	 * }
	 * 
	 * 
	 * 
	 * return dtoList;
	 * 
	 * }
	 */
	// created by noufal ends..on oct 22 2012
	
	

}
