/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.agiledge.atom.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;

import com.agiledge.atom.commons.OtherFunctions;
import com.agiledge.atom.constants.AuditLogConstants;
import com.agiledge.atom.dbConnect.DbConnect;
import com.agiledge.atom.dto.AuditLogDTO;
import com.agiledge.atom.dto.EmployeeDto;
import com.agiledge.atom.dto.ScheduleAlterDto;
import com.agiledge.atom.dto.SchedulingDto;
import com.agiledge.atom.service.LogTimeService;
import com.agiledge.atom.service.SchedulingAlterService;




/**
 * 
 * @author Administrator
 */

public class SchedulingDao {
	private ArrayList<String> messageList= new ArrayList<String> ();
	private String message ="";
	public ArrayList<SchedulingDto> getSubscribedEmployeeDetailsByManger(
			long managerId, long delegaterId, String site, String employeeFilter) {
		DbConnect ob = DbConnect.getInstance();
		Statement st = null;
		ResultSet rs = null;
		ArrayList<SchedulingDto> employeeSubscritionList = null;
		SchedulingDto dto = null;// new SchedulingDto();
		dto = new SchedulingDto();
		Connection con = ob.connectDB();
		LogTimeDao lostimeDao=new LogTimeDao();		
		try {
			
			String subQuery = "";
			// if (site != null && !site.equals("")) {
			subQuery = " and e.site=" + site + " ";
			// }
			String empFilterQuery = "";
			
			if(employeeFilter.trim().equalsIgnoreCase("--Self--")) {
				empFilterQuery= "  and (  e.id="+managerId+" or s.supervisor="
						+ managerId
						+ " or s.spoc="
						+ managerId
						+ " or s.spoc="
						+ delegaterId
						+ " or s.supervisor="
						+ delegaterId
						+ "  or e.linemanager="
						+ managerId
						+ " or e.linemanager="
						+ delegaterId
						+ " )";
			}
			else if (employeeFilter.trim().equalsIgnoreCase("--ALL--")) {
				empFilterQuery="  and (  e.id="+managerId+" or s.supervisor="
						+ managerId
						+ " or s.spoc="
						+ managerId
						+ " or s.spoc="
						+ delegaterId
						+ " or s.supervisor="
						+ delegaterId
						+ "  or e.linemanager="
						+ managerId
						+ " or e.linemanager="
						+ delegaterId
						+ " or e.linemanager in (select id from employee where linemanager="
						+ managerId + ") )  ";
			} else {
				empFilterQuery= "  and (    s.supervisor="
						+ employeeFilter
						+ " or s.spoc="
						+ employeeFilter
						+ " or s.supervisor="
						+ employeeFilter
						+ "  or e.linemanager="
						+ employeeFilter
						 
						+ " )";
				//delegated employee id is not given  here. May require future changes.
			}
			
			String query = "select  s.subscriptionID, s.subscriptionDate, e.PersonnelNo,(e.displayname) as EmployeeFirstName,s.fromDate ,(select es.to_date from  employee_schedule es where es.status='a' and  es.subscription_id=s.subscriptionID order by es.to_date desc limit 1) as scheduledTo,(select es.from_date from  employee_schedule es where es.status='a' and  es.subscription_id=s.subscriptionID order by es.to_date desc limit 1) as scheduledFrom,"
					+ "ifnull((select pr.id from  atsconnect pr where pr.project=e.project limit 1),'1' ) as  projectid, "
					+ " e.project as projectdesc, "
					+ "ifnull((select es.statusDate from employee_schedule es where es.status='a' and  es.subscription_id=s.subscriptionID order by es.to_date desc  limit 1 ),'' ) as statusDate,"
					+ "ifnull((select e1.displayname  from employee_schedule es, employee e1 where e1.id=es.scheduledBy and  es.status='a' and  es.subscription_id=s.subscriptionID order by es.to_date desc limit 1),'' ) as scheduledBy"
					+ " from employee e,employee_subscription s where s.empID=e.id "
					+ subQuery
					+ " and    (s.subscriptionStatus='subscribed' or s.subscriptionStatus='pending')" + empFilterQuery;
			System.out.println("aaa"+query);
			st = con.createStatement();
			rs = st.executeQuery(query);
			employeeSubscritionList = new ArrayList<SchedulingDto>();
			while (rs.next()) {
				dto = new SchedulingDto();
				dto.setSubscriptionId(rs.getString("subscriptionID"));
				dto.setEmployeeId(rs.getString("PersonnelNo"));
				dto.setEmployeeName(rs.getString("EmployeeFirstName"));
				dto.setSubscriptionDate("" + rs.getDate("fromDate"));
				dto.setSchedulingToDate("" + rs.getDate("scheduledTo"));
				dto.setSchedulingFromDate("" + rs.getDate("scheduledFrom"));
				dto.setProject(rs.getString("projectid"));
				dto.setDescription(rs.getString("projectdesc"));
				dto.setSubscriptionQuestDate(""
						+ rs.getDate("subscriptionDate"));
				dto.setStatusDate(rs.getString("statusDate"));
				dto.setScheduledBy(rs.getString("scheduledBy"));
				dto.setLoginTimeDto(lostimeDao
						.getAllTimeWithProjectSpecificFromDao(con,
								dto.getProject(), "IN", site));
				dto.setLogoutTimeDto(lostimeDao
						.getAllTimeWithProjectSpecificFromDao(con,
								dto.getProject(), "OUT", site));
				employeeSubscritionList.add(dto);
				// employeeSubscritionList.add(dto);
				// employeeSubscritionList.add(dto);
			}
		} catch (Exception e) {
			System.out
					.println("Error in isSubcribed in EmployeeSubscriptionDao : "
							+ e);
		} finally {
			try {
				DbConnect.closeResultSet(rs);
			} catch (Exception e) {
				System.out.println("Error in ReslutSet close");
			}
			try {
				DbConnect.closeStatement(st);
			} catch (Exception e) {
				System.out.println("Error in Statement close");
			}
			try {
				DbConnect.closeConnection(con);
			} catch (Exception e) {
				System.out.println("Error in Connection close");
			}
		}
		return employeeSubscritionList;
	}
	public ArrayList<SchedulingDto> getSubscribedEmployeeDetailsByspoc(
			long spocid , String site) {
		DbConnect ob = DbConnect.getInstance();
		Statement st = null;
		ResultSet rs = null;
		ArrayList<SchedulingDto> employeeSubscritionList = null;
		SchedulingDto dto = null;// new SchedulingDto();
		dto = new SchedulingDto();
		Connection con = ob.connectDB();
		LogTimeDao lostimeDao=new LogTimeDao();		
		try {
			String empFilterQuery = "";
			String subQuery = "";
			// if (site != null && !site.equals("")) {
			subQuery = " and e.site=" + site + " ";
			// }
			/*String empFilterQuery = "";
			
			if(employeeFilter.trim().equalsIgnoreCase("--Self--")) {
				empFilterQuery= "  and (  e.id="+managerId+" or s.supervisor="
						+ managerId
						+ " or s.spoc="
						+ managerId
						+ " or s.spoc="
						+ delegaterId
						+ " or s.supervisor="
						+ delegaterId
						+ "  or e.linemanager="
						+ managerId
						+ " or e.linemanager="
						+ delegaterId
						+ " )";
			}
			else if (employeeFilter.trim().equalsIgnoreCase("--ALL--")) {
				empFilterQuery="  and (  e.id="+managerId+" or s.supervisor="
						+ managerId
						+ " or s.spoc="
						+ managerId
						+ " or s.spoc="
						+ delegaterId
						+ " or s.supervisor="
						+ delegaterId
						+ "  or e.linemanager="
						+ managerId
						+ " or e.linemanager="
						+ delegaterId
						+ " or e.linemanager in (select id from employee where linemanager="
						+ managerId + ") )  ";
			} else {
				empFilterQuery= "  and (    s.supervisor="
						+ employeeFilter
						+ " or s.spoc="
						+ employeeFilter
						+ " or s.supervisor="
						+ employeeFilter
						+ "  or e.linemanager="
						+ employeeFilter
						 
						+ " )";
				//delegated employee id is not given  here. May require future changes.
			}*/
			empFilterQuery="  and (  e.id="+spocid+" or s.supervisor="
					+ spocid
					+ " or s.spoc="
					+ spocid
					+ " or s.spoc="
					+ spocid
					+ " or s.supervisor="
					+ spocid
					+ "  or e.linemanager="
					+ spocid
					+ " or e.linemanager="
					+ spocid
					+ " or e.linemanager in (select id from employee where linemanager="
					+ spocid + ") )  ";
			/*String query = "select  s.subscriptionID, e.id,s.spoc,s.subscriptionDate, e.PersonnelNo,(e.displayname) as EmployeeFirstName,s.fromDate ,(select es.to_date from  employee_schedule es where es.status='a' and  es.subscription_id=s.subscriptionID order by es.to_date desc limit 1) as scheduledTo,(select es.from_date from  employee_schedule es where es.status='a' and  es.subscription_id=s.subscriptionID order by es.to_date desc limit 1) as scheduledFrom,"
					+ "nullif((select pr.id from  atsconnect pr where pr.project=e.project limit 1),'1' ) as  projectid, "
					+ " e.project as projectdesc, "
					+ "ifnull((select es.statusDate from employee_schedule es where es.status='a' and  es.subscription_id=s.subscriptionID order by es.to_date desc  limit 1 ),'' ) as statusDate,"
					+ "ifnull((select e1.displayname  from employee_schedule es, employee e1 where e1.id=es.scheduledBy and  es.status='a' and  es.subscription_id=s.subscriptionID order by es.to_date desc limit 1),'' ) as scheduledBy"
					+ " from employee e,employee_subscription s where s.empID=e.id "
					+ subQuery
					+ " and    (s.subscriptionStatus='subscribed' or s.subscriptionStatus='pending') AND s.spoc = "+spocid+" ORDER BY projectdesc";*/
			String query = "select  s.subscriptionID, e.id,s.spoc,s.subscriptionDate, e.PersonnelNo,(e.displayname) as EmployeeFirstName,s.fromDate ,(select es.to_date from  employee_schedule es where es.status='a' and  es.subscription_id=s.subscriptionID order by es.to_date desc limit 1) as scheduledTo,(select es.from_date from  employee_schedule es where es.status='a' and  es.subscription_id=s.subscriptionID order by es.to_date desc limit 1) as scheduledFrom,"
					+ "nullif((select pr.id from  atsconnect pr where pr.project=e.project limit 1),'1' ) as  projectid, "
					+ " e.project as projectdesc, "
					+ "ifnull((select es.statusDate from employee_schedule es where es.status='a' and  es.subscription_id=s.subscriptionID order by es.to_date desc  limit 1 ),'' ) as statusDate,"
					+ "ifnull((select e1.displayname  from employee_schedule es, employee e1 where e1.id=es.scheduledBy and  es.status='a' and  es.subscription_id=s.subscriptionID order by es.to_date desc limit 1),'' ) as scheduledBy"
					+ " from employee e,employee_subscription s where s.empID=e.id "
					+ subQuery
					+ " and    (s.subscriptionStatus='subscribed' or s.subscriptionStatus='pending') " +  empFilterQuery + "ORDER BY projectdesc";
			System.out.println(query);
			st = con.createStatement();
			rs = st.executeQuery(query);
			employeeSubscritionList = new ArrayList<SchedulingDto>();
			while (rs.next()) {
				dto = new SchedulingDto();
				dto.setSubscriptionId(rs.getString("subscriptionID"));
				dto.setPersonnelNo(rs.getString("PersonnelNo"));
				dto.setEmployeeId(rs.getString("id"));
			
				dto.setEmployeeName(rs.getString("EmployeeFirstName"));
				dto.setSubscriptionDate("" + rs.getDate("fromDate"));
				dto.setSchedulingToDate("" + rs.getDate("scheduledTo"));
				dto.setSchedulingFromDate("" + rs.getDate("scheduledFrom"));
				dto.setProject(rs.getString("projectid"));
				dto.setDescription(rs.getString("projectdesc"));
				dto.setSubscriptionQuestDate(""
						+ rs.getDate("subscriptionDate"));
				dto.setStatusDate(rs.getString("statusDate"));
				dto.setScheduledBy(rs.getString("scheduledBy"));
				dto.setLoginTimeDto(lostimeDao
						.getAllTimeWithProjectSpecificFromDao(con,
								dto.getProject(), "IN", site));
				dto.setLogoutTimeDto(lostimeDao
						.getAllTimeWithProjectSpecificFromDao(con,
								dto.getProject(), "OUT", site));
				employeeSubscritionList.add(dto);
				// employeeSubscritionList.add(dto);
				// employeeSubscritionList.add(dto);
			}
		} catch (Exception e) {
			System.out
					.println("Error in isSubcribed in EmployeeSubscriptionDao : "
							+ e);
		} finally {
			try {
				DbConnect.closeResultSet(rs);
			} catch (Exception e) {
				System.out.println("Error in ReslutSet close");
			}
			try {
				DbConnect.closeStatement(st);
			} catch (Exception e) {
				System.out.println("Error in Statement close");
			}
			try {
				DbConnect.closeConnection(con);
			} catch (Exception e) {
				System.out.println("Error in Connection close");
			}
		}
		return employeeSubscritionList;
	}
	public ArrayList<SchedulingDto> getAllSubscribedEmployeeDetailsByManger(
			long managerId) {
		DbConnect ob = DbConnect.getInstance();
		Statement st = null;
		ResultSet rs = null;
		ArrayList<SchedulingDto> employeeSubscritionList = null;
		SchedulingDto dto = null;// new SchedulingDto();
		dto = new SchedulingDto();
		Connection con = ob.connectDB();
		try {
			String query = "select  esub.subscriptionID, esub.subscriptionDate, e.PersonnelNo,e.displayname as EmployeeFirstName,esub.fromDate,"
					+

					"(select es.to_date from  employee_schedule es where es.status='a' and  es.subscription_id=esub.subscriptionID order by es.to_date desc limit 1) as scheduledTo,(select es.from_date from  employee_schedule es where es.status='a' and  es.subscription_id=esub.subscriptionID order by es.to_date desc limit 1) as scheduledFrom,"
					+ "ifnull((select es.project from  employee_schedule es where es.status='a' and es.subscription_id=esub.subscriptionID order by es.to_date desc limit 1),'' ) as  projectid,ifnull((select p.description from  atsconnect p where  p.id=(select es.project from  employee_schedule es where es.status='a' and es.subscription_id=esub.subscriptionID order by es.to_date desc limit 1 ) limit 1),'') as projectdesc,ifnull((select es.statusDate from employee_schedule es where es.status='a' and  es.subscription_id=esub.subscriptionID order by es.to_date desc limit 1),'' ) as statusDate,ifnull((select (e1.displayname)  from employee_schedule es, employee e1 where e1.id=es.scheduledBy and  es.status='a' and  es.subscription_id=esub.subscriptionID order by es.to_date desc limit 1),'' ) as scheduledBy "
					+ " from employee_subscription esub,employee e where esub.empID=e.id "
					+ " and    (esub.subscriptionStatus='subscribed' or esub.subscriptionStatus='pending') and "
					+ " (s.supervisor="
					+ managerId
					+ " or s.spoc="
					+ managerId
					+ ") ";
			st = con.createStatement();
			// System.out.println(query);
			rs = st.executeQuery(query);
			employeeSubscritionList = new ArrayList<SchedulingDto>();
			while (rs.next()) {
				dto = new SchedulingDto();
				dto.setSubscriptionId(rs.getString("subscriptionID"));
				dto.setEmployeeId(rs.getString("PersonnelNo"));
				dto.setEmployeeName(rs.getString("EmployeeFirstName"));
				dto.setSubscriptionDate("" + rs.getDate("fromDate"));
				dto.setSchedulingToDate("" + rs.getString("scheduledTo"));
				dto.setSubscriptionQuestDate(""
						+ rs.getDate("subscriptionDate"));
				dto.setStatusDate(rs.getString("statusDate"));
				employeeSubscritionList.add(dto);
			}
		} catch (Exception e) {

			System.out
					.println("Error in isSubcribed in EmployeeSubscriptionDao : "
							+ e);
		} finally {
			try {
				DbConnect.closeResultSet(rs);
			} catch (Exception e) {
				System.out.println("Error in ReslutSet close");
			}
			try {
				DbConnect.closeStatement(st);
			} catch (Exception e) {
				System.out.println("Error in Statement close");
			}
			try {
				DbConnect.closeConnection(con);
			} catch (Exception e) {
				System.out.println("Error in Connection close");
			}
		}
		return employeeSubscritionList;
	}

	public ArrayList<SchedulingDto> getAllSubscribedEmployeeDetails(String site) {
		DbConnect ob = DbConnect.getInstance();
		Statement st = null;
		ResultSet rs = null;
		ArrayList<SchedulingDto> employeeSubscritionList = null;
		SchedulingDto dto = null;// new SchedulingDto();
		dto = new SchedulingDto();
		Connection con = ob.connectDB();
		LogTimeDao lostimeDao=new LogTimeDao();
		try {
			String subQuery = "";
			if (site != null && !site.equals("")) {
				subQuery = " and e.site=" + site + " ";
			}
			String query = "select  esub.subscriptionID, esub.subscriptionDate, e.loginid personnelno,e.displayname as EmployeeFirstName,esub.fromDate,"
+"ifnull(max(es.from_date),'') as scheduledFrom,ifnull(max(es.to_date),'') as scheduledto,ifnull(e.project,'') as projectdesc,"
+"ifnull(p.id ,'1') as projectid,ifnull(max(es.statusDate),'') as statusDate,e1.displayname as scheduledBy"
 +" from employee e left join atsconnect p on e.project=p.project,employee_subscription esub left join employee_schedule es" 
 +" on esub.subscriptionId=es.subscription_id"
+" left join employee e1 on es.scheduledBy=e1.id"
 +" where esub.empID=e.id   and    (esub.subscriptionStatus='subscribed' or esub.subscriptionStatus='pending')"+ subQuery 
 +" group by esub.subscriptionID, esub.subscriptionDate, e.PersonnelNo,e.displayname ,esub.fromDate,"
+" e.project";
			st = con.createStatement();
			 System.out.println(query);
			rs = st.executeQuery(query);
			employeeSubscritionList = new ArrayList<SchedulingDto>();
			while (rs.next()) {
				dto = new SchedulingDto();
				dto.setSubscriptionId(rs.getString("subscriptionID"));
				dto.setEmployeeId(rs.getString("PersonnelNo"));
				dto.setEmployeeName(rs.getString("EmployeeFirstName"));
				dto.setSubscriptionDate("" + rs.getDate("fromDate"));
				dto.setSchedulingToDate("" + rs.getString("scheduledTo"));
				dto.setProject(rs.getString("projectid"));
				dto.setDescription(rs.getString("projectdesc"));
				dto.setSubscriptionQuestDate(""
						+ rs.getDate("subscriptionDate"));
				dto.setStatusDate(rs.getString("statusDate"));
				dto.setScheduledBy(rs.getString("scheduledBy"));
				dto.setLoginTimeDto(lostimeDao
						.getAllTimeWithProjectSpecificFromDao(con,
								dto.getProject(), "IN", site));
				dto.setLogoutTimeDto(lostimeDao
						.getAllTimeWithProjectSpecificFromDao(con,
								dto.getProject(), "OUT", site));
				employeeSubscritionList.add(dto);
			}
		} catch (Exception e) {
			DbConnect.closeConnection(con);
			System.out
					.println("Error in isSubcribed in EmployeeSubscriptionDao : "
							+ e);
		} finally {
			try {
				DbConnect.closeResultSet(rs);
			} catch (Exception e) {
				System.out.println("Error in ReslutSet close");
			}
			try {
				DbConnect.closeStatement(st);
			} catch (Exception e) {
				System.out.println("Error in Statement close");
			}
			try {
				DbConnect.closeConnection(con);
			} catch (Exception e) {
				System.out.println("Error in Connection close");
			}
		}
		return employeeSubscritionList;
	}

	public int setScheduleEmployees(ArrayList<SchedulingDto> scheduleEmpList) {
		DbConnect ob = DbConnect.getInstance();

		Connection con = ob.connectDB();
		long returnScheduleId = 0;
		CallableStatement cst = null;
		int ChangedBy = 0;
		int retVal = 0;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {

			pst = con
					.prepareStatement("select count(id) from employee_schedule where (	(? between from_date and ifnull(cancel_date,to_date)) or	(? between from_date and ifnull(cancel_date,to_date)) or 	(? <=from_date and ?>=ifnull(cancel_date,to_date)) ) and employee_schedule.status='a' and employee_schedule.subscription_id=? having count(id)>1");
			// con.prepareStatement("insert into employee_schdule(subscripton_id,employee_id,from_date,to_date,login_time,logout_time,log_status,status) values (?,?,?,?,?,?,?,?)");
			cst = con.prepareCall("{CALL scheduleinsert(?,?,?,?,?,?,?,?,?,?)}");
			for (SchedulingDto schedulingEmpDto : scheduleEmpList) {
				// System.out.println("S"+Long.parseLong(schedulingEmpDto.getSubscriptionId())+"P"+schedulingEmpDto.getProject()+"Fd"+schedulingEmpDto.getSchedulingFromDate()+"TD"+schedulingEmpDto.getSchedulingToDate()+"LIN"+schedulingEmpDto.getLoginTime()+"LOUT"+schedulingEmpDto.getLogoutTime()+"weekly offffffffff"+schedulingEmpDto.getWeeklyOff());
				// System.out.println("S"+Long.parseLong(schedulingEmpDto.getSubscriptionId())+"P"+schedulingEmpDto.getProject()+"Fd"+schedulingEmpDto.getSchedulingFromDate()+"TD"+schedulingEmpDto.getSchedulingToDate()+"LIN"+schedulingEmpDto.getLoginTime()+"LOUT"+schedulingEmpDto.getLogoutTime());
				pst.setString(1, OtherFunctions
						.changeDateFromatToIso(schedulingEmpDto
								.getSchedulingFromDate()));
				pst.setString(2, OtherFunctions
						.changeDateFromatToIso(schedulingEmpDto
								.getSchedulingToDate()));
				pst.setString(3, OtherFunctions
						.changeDateFromatToIso(schedulingEmpDto
								.getSchedulingFromDate()));
				pst.setString(4, OtherFunctions
						.changeDateFromatToIso(schedulingEmpDto
								.getSchedulingToDate()));
				pst.setString(5, schedulingEmpDto.getSubscriptionId());
				rs = pst.executeQuery();
				if (!rs.next()) {
					cst.setLong(1, Long.parseLong(schedulingEmpDto
							.getSubscriptionId()));
					cst.setString(2, schedulingEmpDto.getProject());
					cst.setString(3, OtherFunctions
							.changeDateFromatToIso(schedulingEmpDto
									.getSchedulingFromDate()));
					cst.setString(4, OtherFunctions
							.changeDateFromatToIso(schedulingEmpDto
									.getSchedulingToDate()));
					cst.setString(5, schedulingEmpDto.getLoginTime());
					cst.setString(6, schedulingEmpDto.getLogoutTime());					
					cst.setString(7, schedulingEmpDto.getScheduledBy());
					cst.setString(8, "both");
					cst.setString(9, "a");
					cst.registerOutParameter(10, Types.BIGINT);
					ChangedBy = Integer.parseInt(schedulingEmpDto
							.getScheduledBy());
					cst.execute();
					returnScheduleId = cst.getLong(10);
					int returnschid = cst.getInt(10);
					auditLogEntryforcreateschedule(returnschid,
							AuditLogConstants.SCHEDULE_EMP, ChangedBy,
							AuditLogConstants.WORKFLOW_STATE_EMPTY,
							AuditLogConstants.WORKFLOW_STATE_SCHEDULED,
							AuditLogConstants.AUDIT_LOG_CREATE_ACTION);
					if (returnScheduleId != -1) {
						schedulingEmpDto.setIsUpdated(true);
						schedulingEmpDto.setScheduleId(returnScheduleId);
						retVal++;
					} else {
						schedulingEmpDto.setIsUpdated(false);
					}
					// System.out.println(" Updated :"
					// +schedulingEmpDto.isIsUpdated());
				} else {
					System.out.println("Duplication :");
				}
				DbConnect.closeResultSet(rs);
			}
		}

		catch (Exception e) {
			System.out.println("Error1111" + e);
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(cst, pst);
			DbConnect.closeConnection(con);
		}

		return retVal;
	}

	private void auditLogEntryforcreateschedule(int autoincNumber,
			String subcriptionEmpModule, int changedBy,
			String workflowStateEmpty, String workflowStateSubPending,
			String auditLogCreateAction) {
		AuditLogDTO auditLog = new AuditLogDTO();

		auditLog.setRelatedNodeId(autoincNumber);
		auditLog.setModuleName(subcriptionEmpModule);
		auditLog.setChangedBy(changedBy);
		auditLog.setDateChanged(new java.util.Date());
		auditLog.setPreviousState(workflowStateEmpty);
		auditLog.setCurrentState(workflowStateSubPending);
		auditLog.setAction(auditLogCreateAction);

		AuditLogDAO auditDAO = new AuditLogDAO();
		auditDAO.addAuditLogEntry(auditLog);

	}

	public List<SchedulingDto> getSchedulingDetailsWhoAreAboutToExpiry(
			String curDate) {

		DbConnect ob = DbConnect.getInstance();
		Statement st = null;
		ResultSet rs = null;
		List<SchedulingDto> dtoList = new ArrayList<SchedulingDto>();

		Connection con = ob.connectDB();
		try {
			String query = "select s.subscriptionID, s.subscriptionDate, e.PersonnelNo,(e.displayname) as EmployeeFirstName,s.fromDate ,es.to_date as scheduledTo from employee e,employee_subscription s left join employee_schedule es on s.subscriptionID=es.subscription_id and  es.to_date>curDate() and es.status='a'  where s.empID=e.id and (s.subscriptionStatus='subscribed' or s.subscriptionStatus='pending')";
			st = con.createStatement();
			// System.out.println(query);
			rs = st.executeQuery(query);
			while (rs.next()) {
				SchedulingDto dto = new SchedulingDto();
				dto = new SchedulingDto();
				dto.setSubscriptionId(rs.getString("subscriptionID"));
				dto.setEmployeeId(rs.getString("PersonnelNo"));
				dto.setEmployeeName(rs.getString("EmployeeFirstName"));
				dto.setSubscriptionDate("" + rs.getDate("fromDate"));
				dto.setSchedulingToDate("" + rs.getString("scheduledTo"));
				dto.setSubscriptionQuestDate(""
						+ rs.getDate("subscriptionDate"));

				dtoList.add(dto);
			}
		} catch (Exception e) {

			System.out
					.println("Error in isSubcribed in EmployeeSubscriptionDao : "
							+ e);
		} finally {

			DbConnect.closeResultSet(rs);

			DbConnect.closeStatement(st);

			DbConnect.closeConnection(con);

		}
		return dtoList;

	}

	public Date getFirstSchedulingFromDate(String subid) {
		Date date = null;
		DbConnect ob = DbConnect.getInstance();
		Connection con = ob.connectDB();
		PreparedStatement st = null;
		ResultSet rs = null;

		try {
			String query = "select min(sch.from_date ) date from employee_schedule sch where sch.subscription_id=?";

			st = con.prepareStatement(query);
			st.setString(1, subid);

			rs = st.executeQuery();
			if (rs.next()) {
				date = rs.getDate(1);
			}

		} catch (Exception e) {
			date = null;
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);
		}
		return date;

	}

	public Date getLastSchedulingToDate(String subid) {
		Date date = null;
		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		DbConnect db = DbConnect.getInstance();
		con = db.connectDB();

		try {
			String query = "select max(sch.to_date ) from employee_schedule sch where sch.subscription_id=?";
			st = con.prepareStatement(query);
			st.setString(1, subid);

			rs = st.executeQuery();
			if (rs.next()) {
				date = rs.getDate(1);
			}

		} catch (Exception e) {
			date = null;
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);
		}
		return date;

	}

	public ArrayList<ScheduleAlterDto> getEmployeesWeeklyOff(
			ArrayList<SchedulingDto> scheduleEmpList) {

		return null;
	}

	public boolean hasNoSchedule(String subscriptionId) {
		boolean flag = false;
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		DbConnect db = DbConnect.getInstance();
		con = db.connectDB();

		try {
			String query = "select count(*) from employee_schedule sch where sch.subscription_id="
					+ subscriptionId;
			st = con.createStatement();
			rs = st.executeQuery(query);
			if (rs.next() && rs.getInt(1) > 1) {
				flag = false;
			} else {
				flag = true;
			}

		} catch (Exception e) {
			System.out.println("error"+e);
			flag = false;
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);
		}
		System.out.println("In the FN"+flag);
		return flag;
	}

	public int uploadSchedule(ArrayList<SchedulingDto> dtoList, String changedBy)   {
		// TODO Auto-generated method stub
		int val=0;
		Connection con =null;
		PreparedStatement insertQueryPst = null;
		PreparedStatement checkQueryAlter1Pst = null;
		PreparedStatement updateQuery1Pst = null;
		PreparedStatement updateQuery2Pst = null;
		PreparedStatement insertScheduleAlterQueryPst = null;
		PreparedStatement deleteScheduleQueryPst = null;
		PreparedStatement selectFullOverlapQueryPst = null;
		PreparedStatement scheduleUpdateQueryPst = null;
		PreparedStatement selectAlterOverlapQueryPst = null;
		boolean error=false;
		
		/* check pst*/
		PreparedStatement checkEmpPst = null;
		PreparedStatement checkSubPst = null;
		PreparedStatement checkProjectPst = null;
		PreparedStatement empProjectUpdatePst = null;
		
		/* Result sets for check*/
		ResultSet checkEmpRs = null;
		ResultSet checkSubRs = null;
		ResultSet checkProjectRs = null;
		/* query */
		String checkEmpQuery = "select * from employee where personnelNo = ? ";
		String checkSubQuery = "select * from employee e join employee_subscription es on e.id=es.empid where personnelNo = ? and ( es.subscriptionStatus='subscribed' or es.subscriptionStatus='pending' )  ";
		String checkProjectQuery = "select * from atsconnect where status='a' and project=?";
		
		 
		ArrayList<SchedulingDto> newDtoList = null;
		ResultSet rsSchedule = null;
		ResultSet rsScheduleAlter=null;
		ResultSet rsScheduleAlter1=null;
		ResultSet rsOverLapp=null;
		int rowCount =2;
		try {
			 con = DbConnect.getInstance().connectDB();
			con.setAutoCommit(false);
			String empProjectUpdatequery = "update  employee e, atsconnect prj set e.project=prj.project, e.projectUnit=prj.projectUnit where e.personnelNo=? and prj.project=?";
			String selectFullOverlapQuery = "select employee.personnelNo, p.project projectName, date_add(?, interval 1 day) next_date, employee_schedule.* from employee_schedule join employee on employee_schedule.employee_id=employee.id join atsconnect p on employee_schedule.project=p.id  where employee_schedule.from_date<? and employee_schedule.to_date >?  and employee.personnelNo=? and employee_schedule.status!='c'  ";
			String selectAlterOverlapQuery = "select employee_schedule_alter.* from employee_schedule_alter where scheduleId=?";
			String scheduleUpdateQuery="update employee_schedule set to_date=date_add(?, interval -1 day) where id=? ";
			
			String insertQuery = " insert into employee_schedule (subscription_id, employee_id, project, from_date, to_date, login_time, logout_time, scheduledBy, status, statusDate) select s.subscriptionId,e.id, p.id, ?, ?, ?, ?, ?, 'a', curdate() from employee_subscription s join employee e on s.empid=e.id and (s.subscriptionStatus='pending' or s.subscriptionStatus='subscribed'), atsconnect p where e.personnelNo=? and p.project=?";
			// from_date, to_date, login_time, logout_time, scheduledBy, personneNo, project  
			 
			String checkQueryAlter1 = "delete sha from employee_schedule_alter sha join employee_schedule sh on sha.scheduleId=sh.id join employee e on sh.employee_Id=e.id where (sha.date>=? and sha.date <=? or sha.date>ifnull(sh.to_date,sh.cancel_date) ) and e.personnelNo=? ";
			// 1. from_date
			// 2. to_date
			String updateQuery1 = "update employee_schedule join employee on employee_schedule.employee_id=employee.id set to_date=date_add(?, interval -1 day) where employee_schedule.from_date<? and employee_schedule.to_date >=?   and employee.personnelNo=?  and employee_schedule.status!='c'";
			// 1. fromdate
			// 2. fromdate
			// 3. fromdate
			// 4. todate
			String updateQuery2 = "update employee_schedule join employee on employee_schedule.employee_id=employee.id set from_date=date_add(?, interval 1 day) where employee_schedule.from_date>=? and employee_schedule.from_date<=? and employee_schedule.to_date >? and employee.personnelNo=?  and employee_schedule.status!='c'";
			// 1. todate
			// 2. fromdate
			// 3. todate
			// 4. todate
			String deleteScheduleQuery = "delete es from employee_schedule es join employee e on es.employee_id=e.id where es.from_date>=? and es.to_date<=? and e.personnelNo=? ";
			// 1. fromdate
			// 2. todate
			
			String insertScheduleAlterQuery= "insert into employee_schedule_alter(scheduleId, date, login_time, logout_time, statusDate, updatedBy, status) values(?,?,?,?,curdate(),?, ?)";
			
			/*validation pst intantiation*/
			checkEmpPst = con.prepareStatement(checkEmpQuery);
			checkSubPst = con.prepareStatement(checkSubQuery);
			checkProjectPst = con.prepareStatement(checkProjectQuery);
			
			
			insertQueryPst= con.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS);
			checkQueryAlter1Pst = con.prepareStatement(checkQueryAlter1, Statement.RETURN_GENERATED_KEYS);
			updateQuery1Pst = con.prepareStatement(updateQuery1);
			updateQuery2Pst = con.prepareStatement(updateQuery2);
			selectFullOverlapQueryPst = con.prepareStatement(selectFullOverlapQuery);
			scheduleUpdateQueryPst = con.prepareStatement(scheduleUpdateQuery);
			selectAlterOverlapQueryPst = con.prepareStatement(selectAlterOverlapQuery);
			insertScheduleAlterQueryPst = con.prepareStatement(insertScheduleAlterQuery, Statement.RETURN_GENERATED_KEYS);
			deleteScheduleQueryPst = con.prepareStatement(deleteScheduleQuery);
			
			
			boolean hasNew = true;
			newDtoList =new ArrayList<SchedulingDto>();
			while(hasNew) {
				int idx=0;
			for(SchedulingDto sdto : dtoList) {
				EmployeeDto empPrjDto = null;
				System.out.println("List Size : " + dtoList.size() + " , index : " + ++idx);
				String message="";
				/* validation operation */
				if(OtherFunctions.isEmpty(sdto.getSchedulingToDate())) {
					if(!OtherFunctions.isEmpty(sdto.getScheduledToDate())) {
						sdto.setSchedulingToDate(sdto.getScheduledToDate());
					}
				}
				checkEmpPst.setString(1, sdto.getEmployeeId());
				checkSubPst.setString(1,  sdto.getEmployeeId());
				checkProjectPst.setString(1,  sdto.getProject());
				
				checkEmpRs = checkEmpPst.executeQuery();
				checkSubRs = checkSubPst.executeQuery();
				checkProjectRs = checkProjectPst.executeQuery();
				
				if(checkEmpRs.next()==false) {
					error=true;
					
					 message = "Invalid employee("+sdto.getEmployeeId()+"). ";
					 System.out.println(message);
			 
				}
				if(checkSubRs.next()==false) {
					error=true;
					 message += "No subscription for("+sdto.getEmployeeId()+"). ";
					 System.out.println(message);
			 
				}
				if(checkProjectRs.next() ){
					empPrjDto= new EmployeeDto();
					empPrjDto.setPersonnelNo(sdto.getEmployeeId());
					empPrjDto.setProjectCode(checkProjectRs.getString("project"));
					empPrjDto.setProject(checkProjectRs.getString("description"));
					empPrjDto.setProjectUnit(checkProjectRs.getString("projectUnit"));
					
				} else {
					error=true;
					 message += "Invalid project ("+sdto.getProject() +"). ";
					 System.out.println(message);
			 
				}
				
				if(message.equals("")==false) {
					getMessageList().add("Line:"+ rowCount + ". "+message);
				}
				if(error==false) {
				
					 
					System.out.println(" x ");
					selectFullOverlapQueryPst.setString(1, OtherFunctions.changeDateFromatToIso( sdto.getSchedulingToDate()));
					selectFullOverlapQueryPst.setString(2, OtherFunctions.changeDateFromatToIso( sdto.getSchedulingFromDate()));
					selectFullOverlapQueryPst.setString(3, OtherFunctions.changeDateFromatToIso( sdto.getSchedulingToDate()));
					selectFullOverlapQueryPst.setString(4, sdto.getEmployeeId());
				System.out.println("WQuery :" +	String.format( selectFullOverlapQuery.replace("?", "'%s'"), OtherFunctions.changeDateFromatToIso( sdto.getSchedulingToDate()),
					 OtherFunctions.changeDateFromatToIso( sdto.getSchedulingFromDate()), 
					  OtherFunctions.changeDateFromatToIso( sdto.getSchedulingToDate()), 
					 sdto.getEmployeeId() 
							
							) );
					rsOverLapp = selectFullOverlapQueryPst.executeQuery();
					if(rsOverLapp.next()) {
						SchedulingDto newSchDto = new SchedulingDto();
						newSchDto.setScheduleId( rsOverLapp.getLong("id"));
						newSchDto.setEmployeeId(rsOverLapp.getString("personnelNo"));
						newSchDto.setSubscriptionId(rsOverLapp.getString("subscription_Id"));
						newSchDto.setProject(rsOverLapp.getString("projectName"));
						newSchDto.setSchedulingFromDate(rsOverLapp.getString("next_date"));
						newSchDto.setSchedulingToDate(rsOverLapp.getString("to_date"));
						newSchDto.setStatus(rsOverLapp.getString("status"));
						newSchDto.setLoginTime(rsOverLapp.getString("login_time"));
						newSchDto.setLogoutTime(rsOverLapp.getString("logout_time"));
						newSchDto.setScheduledBy(rsOverLapp.getString("scheduledBy"));
						 
						
						scheduleUpdateQueryPst.setString(1,  OtherFunctions.changeDateFromatToIso(  sdto.getSchedulingFromDate()));
						scheduleUpdateQueryPst.setLong(2, newSchDto.getScheduleId());
						
						scheduleUpdateQueryPst.executeUpdate();
						  
						 AuditLogDAO auditDao = new AuditLogDAO();
						 auditDao.auditLogEntry(newSchDto.getScheduleId(), AuditLogConstants.SCHEDULE_EMP, Integer.parseInt( changedBy) , null, AuditLogConstants.WORKFLOW_STATE_EXCEL_SCHEDULE_UPDATE , AuditLogConstants.AUDIT_LOG_UPLOAD_ACTION, con);
						//............
						 selectAlterOverlapQueryPst.setLong(1, newSchDto.getScheduleId());
						 rsScheduleAlter1 = selectAlterOverlapQueryPst.executeQuery();
						 while(rsScheduleAlter1.next()) {
							 ScheduleAlterDto schaDto = new ScheduleAlterDto(); 
							 schaDto.setDate(rsScheduleAlter1.getString("date"));
							 schaDto.setLoginTime(rsScheduleAlter1.getString("login_time"));
							 schaDto.setLogoutTime(rsScheduleAlter1.getString("logout_time"));
							 schaDto.setStatus(rsScheduleAlter1.getString("status"));
							  
							 newSchDto.getAlterList().add(schaDto);
						 }
						 
						 newDtoList.add(newSchDto);
						    
						
						 
					}
					checkQueryAlter1Pst.setString(1, OtherFunctions.changeDateFromatToIso( sdto.getSchedulingFromDate()));
					checkQueryAlter1Pst.setString(2, OtherFunctions.changeDateFromatToIso( sdto.getSchedulingToDate()));
					checkQueryAlter1Pst.setString(3, sdto.getEmployeeId());
					checkQueryAlter1Pst.executeUpdate();
					
					updateQuery1Pst.setString(1,  OtherFunctions.changeDateFromatToIso( sdto.getSchedulingFromDate()));
					updateQuery1Pst.setString(2,  OtherFunctions.changeDateFromatToIso( sdto.getSchedulingFromDate()));
					updateQuery1Pst.setString(3,  OtherFunctions.changeDateFromatToIso( sdto.getSchedulingFromDate()));
					//updateQuery1Pst.setString(4,  OtherFunctions.changeDateFromatToIso( sdto.getSchedulingToDate()));
					updateQuery1Pst.setString(4, sdto.getEmployeeId());  
					updateQuery1Pst.executeUpdate();
					
					updateQuery2Pst.setString(1,  OtherFunctions.changeDateFromatToIso( sdto.getSchedulingToDate()));
					updateQuery2Pst.setString(2,  OtherFunctions.changeDateFromatToIso( sdto.getSchedulingFromDate()));
					updateQuery2Pst.setString(3,  OtherFunctions.changeDateFromatToIso( sdto.getSchedulingToDate()));
					updateQuery2Pst.setString(4,  OtherFunctions.changeDateFromatToIso( sdto.getSchedulingToDate()));
					updateQuery2Pst.setString(5, sdto.getEmployeeId());
					updateQuery2Pst.executeUpdate();
					
					deleteScheduleQueryPst.setString(1,  OtherFunctions.changeDateFromatToIso( sdto.getSchedulingFromDate()));
					deleteScheduleQueryPst.setString(2,  OtherFunctions.changeDateFromatToIso( sdto.getSchedulingToDate()));
					deleteScheduleQueryPst.setString(3,  sdto.getEmployeeId());
					deleteScheduleQueryPst.executeUpdate();
					
					 
					insertQueryPst.setString(1,   OtherFunctions.changeDateFromatToIso( sdto.getSchedulingFromDate()));
					insertQueryPst.setString(2,   OtherFunctions.changeDateFromatToIso( sdto.getSchedulingToDate()));
					insertQueryPst.setString(3,   sdto.getLoginTime());
					insertQueryPst.setString(4,   sdto.getLogoutTime());
					insertQueryPst.setString(5,   changedBy);
					insertQueryPst.setString(6,   sdto.getEmployeeId());
					insertQueryPst.setString(7,   sdto.getProject());
					String ss=insertQuery;
					 
					ss= ss.replace("?", "'%s'");
					ss=String.format(ss,   OtherFunctions.changeDateFromatToIso( sdto.getSchedulingFromDate())  ,   OtherFunctions.changeDateFromatToIso( sdto.getSchedulingToDate())  ,  sdto.getLoginTime(),   sdto.getLogoutTime()
							,   changedBy,     sdto.getEmployeeId(),   sdto.getProject()
							);
					System.out.println(ss);
					
					//
					System.out.println("empid :" + sdto.getEmployeeId());
					System.out.println("project :" + sdto.getProject());
				 int schVal=	insertQueryPst.executeUpdate();
				 if(schVal<=0) {
					 
					 val = val + schVal;
					 getMessageList().add("Line:"+ rowCount + ": Insertion failed");
					 error=true;
					 System.out.println("errorrrrr");
				 } else {
					 
					 /* update project in employee
					  * 
					  */
					 if(empPrjDto!=null) {
						  empProjectUpdatePst = con.prepareStatement(empProjectUpdatequery);
						  empProjectUpdatePst.setString(1, empPrjDto.getPersonnelNo());
						  empProjectUpdatePst.setString(2,  empPrjDto.getProjectCode());
						  empProjectUpdatePst.executeUpdate();
					 }
					 
					 //-----------------
					 rsSchedule = insertQueryPst.getGeneratedKeys();
					 if(rsSchedule.next()) {
						 long scheduleId = rsSchedule.getLong(1);
						 AuditLogDAO auditDao = new AuditLogDAO();
						 auditDao.auditLogEntry(scheduleId, AuditLogConstants.SCHEDULE_EMP, Integer.parseInt( changedBy) , AuditLogConstants.WORKFLOW_STATE_EMPTY, AuditLogConstants.WORKFLOW_STATE_EXCEL_SCHEDULE_UPDATE , AuditLogConstants.AUDIT_LOG_UPLOAD_ACTION, con);
						  
						 if(sdto.getAlterList() !=null && sdto.getAlterList().size()>0) {
							 for(ScheduleAlterDto aldto : sdto.getAlterList()) {
								 System.out.println("alter..");
								 insertScheduleAlterQueryPst.setLong(1, scheduleId);
								 insertScheduleAlterQueryPst.setString(2, OtherFunctions.changeDateFromatToIso(  aldto.getDate()));
								 if(aldto.getLoginTime().equalsIgnoreCase("none")) {
									 aldto.setLoginTime("");
								 }
								 if(aldto.getLogoutTime().equalsIgnoreCase("none")) {
									 aldto.setLogoutTime("");
								 }
								 insertScheduleAlterQueryPst.setString(3,aldto.getLoginTime());
								 insertScheduleAlterQueryPst.setString(4,aldto.getLogoutTime());
								 insertScheduleAlterQueryPst.setString(5,changedBy);
								 insertScheduleAlterQueryPst.setString(6,"a" );
								int alterInsert= insertScheduleAlterQueryPst.executeUpdate();
								if(alterInsert>0) {
								 rsScheduleAlter = insertScheduleAlterQueryPst.getGeneratedKeys();
								 if(rsScheduleAlter.next()) {
									 long scheduleAlterId = rsScheduleAlter.getLong(1);
									 auditDao.auditLogEntry(scheduleAlterId , AuditLogConstants .SCHEDULE_EMP, Integer.parseInt( changedBy), AuditLogConstants.WORKFLOW_STATE_EMPTY, AuditLogConstants.WORKFLOW_STATE_ALTER_EXCEL_UPLOAD_SCHEDULE , AuditLogConstants.AUDIT_LOG_UPLOAD_ACTION,con);
									 
								 }
								}else {
									 getMessageList().add("Line:"+ rowCount + ": Insertion failed");
									 error=true;
									 System.out.println("errrrrrrr");
								}
							 }
							  
						 }
					 }
					 
				 }
			
			}
			
			rowCount ++;
			}
			System.out.println("finished.. Error : " + error);
			System.out.println("new Dto list : "+ newDtoList);
			if(  newDtoList.size()>0) {
				dtoList= newDtoList;
				newDtoList =new ArrayList<SchedulingDto>();
				
			}else {
				hasNew=false;
			}
			
			}
			
			if(error) {
				con.rollback();
				setMessage("Upload failed.");
				val=0;
			} else {
				con.commit();
				setMessage("Upload successfull .");
				getMessageList().add("Uploaded succefully.");
				val=1;
			}
		
			
			
		} catch(Exception e) {
			 messageList.add("DB Operation error : " + e);
			 val=0;
			try {
				con.rollback();
			} catch (SQLException e1) {
			 
			  ;
			}
			System.out.println("Error :"+ e);
			
		}
		finally{
			DbConnect.closeResultSet(rsSchedule, rsScheduleAlter, rsOverLapp, checkEmpRs, checkSubRs, checkProjectRs);
			DbConnect.closeStatement(
					 insertQueryPst 
					, checkQueryAlter1Pst 
					, updateQuery1Pst 
					, updateQuery2Pst 
					, insertScheduleAlterQueryPst 
					, deleteScheduleQueryPst 
					, selectFullOverlapQueryPst 
					, scheduleUpdateQueryPst 
					, checkEmpPst
					, checkSubPst
					, checkProjectPst
					);
			
			DbConnect.closeConnection(con);
			
		}
		return val;
	}

	public ArrayList<String> getMessageList() {
		return messageList;
	}

	public void setMessageList(ArrayList<String> messageList) {
		this.messageList = messageList;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	public SchedulingDto getDetailsForSchedule(String employeeId) {
		DbConnect ob = DbConnect.getInstance();
		Connection con = ob.connectDB();
		Statement st = null;
		ResultSet rs = null;
		LogTimeDao lostimeDao = new LogTimeDao();
		SchedulingDto dto = null;// new SchedulingDto();
		String query = "select  esub.subscriptionID, esub.subscriptionDate,e.site, e.PersonnelNo,e.displayname as EmployeeFirstName,esub.fromDate,(select es.to_date from  employee_schedule es where es.status='a' and  es.subscription_id=esub.subscriptionID order by es.to_date desc limit 1) as scheduledTo, (select es.from_date from  employee_schedule es where es.status='a' and  es.subscription_id=esub.subscriptionID order by es.to_date desc limit 1) as scheduledFrom, ifnull((select pr.id from  atsconnect pr where  pr.project=e.project limit 1),'' ) as  projectid,e.project as projectdesc,ifnull((select es.statusDate from employee_schedule es where es.status='a' and  es.subscription_id=esub.subscriptionID order by es.to_date desc limit 1),'' ) as statusDate, ifnull((select (e1.displayname)  from employee_schedule es, employee e1 where e1.id=es.scheduledBy and  es.status='a' and es.subscription_id=esub.subscriptionID order by es.to_date desc limit 1),'' ) as scheduledBy  from employee_subscription esub,employee e where esub.empID=e.id  and e.id="+employeeId+"  and    (esub.subscriptionStatus='subscribed' or esub.subscriptionStatus='pending')";
		//System.out.println(query);
		try {
			st = con.createStatement();
			rs = st.executeQuery(query);
			
			if (rs.next()) {
				dto = new SchedulingDto();
				dto.setSubscriptionId(rs.getString("subscriptionID"));
				dto.setEmployeeId(rs.getString("PersonnelNo"));
				dto.setEmployeeName(rs.getString("EmployeeFirstName"));
				dto.setSubscriptionDate(rs.getString("fromDate"));
				dto.setScheduledToDate(rs.getString("scheduledTo"));
				dto.setScheduledFromDate(rs.getString("scheduledFrom"));
				dto.setSchedulingFromDate(OtherFunctions.formatDateToOrdinaryFormat(rs.getDate("scheduledFrom")));
				dto.setSchedulingToDate(OtherFunctions.formatDateToOrdinaryFormat(rs.getDate("scheduledTo")));
				dto.setProject(rs.getString("projectid"));
				dto.setDescription(rs.getString("projectdesc"));
				dto.setSubscriptionQuestDate(rs.getString("subscriptionDate"));
				dto.setStatusDate(rs.getString("statusDate"));
				dto.setScheduledBy(rs.getString("scheduledBy"));
				dto.setLoginTimeDto(lostimeDao
						.getAllTimeWithProjectSpecificFromDao(con,
								dto.getProject(), "IN", rs.getString("site")));
				dto.setLogoutTimeDto(lostimeDao
						.getAllTimeWithProjectSpecificFromDao(con,
								dto.getProject(), "OUT", rs.getString("site")));
			}
		} catch (Exception e) {
			System.out.println("ERROR in getDetailsForSchedule " + e);
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);
		}
		return dto;
	}

	public long scheduleSelf(SchedulingDto schedulingEmpDto) {
		System.out.println("Self Schedule");
		DbConnect ob = DbConnect.getInstance();

		Connection con = ob.connectDB();
		long returnScheduleId = 0;
		CallableStatement cst = null;
		int ChangedBy = 0;
		int retVal = 0;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {

			pst = con
					.prepareStatement("select count(id) from employee_schedule where (	(? between from_date and ifnull(cancel_date,to_date)) or	(? between from_date and ifnull(cancel_date,to_date)) or 	(? <=from_date and ?>=ifnull(cancel_date,to_date)) ) and employee_schedule.status='a' and employee_schedule.subscription_id=? having count(id)>1");
			// con.prepareStatement("insert into employee_schdule(subscripton_id,employee_id,from_date,to_date,login_time,logout_time,log_status,status) values (?,?,?,?,?,?,?,?)");
			cst = con.prepareCall("{CALL scheduleinsert(?,?,?,?,?,?,?,?,?,?)}");
				// System.out.println("S"+Long.parseLong(schedulingEmpDto.getSubscriptionId())+"P"+schedulingEmpDto.getProject()+"Fd"+schedulingEmpDto.getSchedulingFromDate()+"TD"+schedulingEmpDto.getSchedulingToDate()+"LIN"+schedulingEmpDto.getLoginTime()+"LOUT"+schedulingEmpDto.getLogoutTime()+"weekly offffffffff"+schedulingEmpDto.getWeeklyOff());
				// System.out.println("S"+Long.parseLong(schedulingEmpDto.getSubscriptionId())+"P"+schedulingEmpDto.getProject()+"Fd"+schedulingEmpDto.getSchedulingFromDate()+"TD"+schedulingEmpDto.getSchedulingToDate()+"LIN"+schedulingEmpDto.getLoginTime()+"LOUT"+schedulingEmpDto.getLogoutTime());			
				pst.setString(1, OtherFunctions
						.changeDateFromatToIso(schedulingEmpDto
								.getSchedulingFromDate()));
				pst.setString(2, OtherFunctions
						.changeDateFromatToIso(schedulingEmpDto
								.getSchedulingToDate()));
				pst.setString(3, OtherFunctions
						.changeDateFromatToIso(schedulingEmpDto
								.getSchedulingFromDate()));
				pst.setString(4, OtherFunctions
						.changeDateFromatToIso(schedulingEmpDto
								.getSchedulingToDate()));
				pst.setString(5, schedulingEmpDto.getSubscriptionId());
				rs = pst.executeQuery();
				if (!rs.next()) {
					cst.setLong(1, Long.parseLong(schedulingEmpDto
							.getSubscriptionId()));
					cst.setString(2, schedulingEmpDto.getProject());
					cst.setString(3, OtherFunctions
							.changeDateFromatToIso(schedulingEmpDto
									.getSchedulingFromDate()));
					cst.setString(4, OtherFunctions
							.changeDateFromatToIso(schedulingEmpDto
									.getSchedulingToDate()));
					cst.setString(5, schedulingEmpDto.getLoginTime());
					cst.setString(6, schedulingEmpDto.getLogoutTime());
					cst.setString(7, schedulingEmpDto.getScheduledBy());
					cst.setString(8, "both");
					cst.setString(9, "a");
					cst.registerOutParameter(10, Types.BIGINT);
					ChangedBy = Integer.parseInt(schedulingEmpDto
							.getScheduledBy());
					cst.execute();
					returnScheduleId = cst.getLong(10);
					int returnschid = cst.getInt(10);					
					auditLogEntryforcreateschedule(returnschid,
							AuditLogConstants.SCHEDULE_EMP, ChangedBy,
							AuditLogConstants.WORKFLOW_STATE_EMPTY,
							AuditLogConstants.WORKFLOW_STATE_SCHEDULED,
							AuditLogConstants.AUDIT_LOG_CREATE_ACTION);
					if (returnScheduleId != -1) {
						schedulingEmpDto.setIsUpdated(true);
						schedulingEmpDto.setScheduleId(returnScheduleId);
						retVal++;
					} else {
						schedulingEmpDto.setIsUpdated(false);
					}
					// System.out.println(" Updated :"
					// +schedulingEmpDto.isIsUpdated());
				} else {
					System.out.println("Duplication :");
				}
				DbConnect.closeResultSet(rs);
				
		}
		
		catch (Exception e) {
			System.out.println("Error1111" + e);
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(cst, pst);
			DbConnect.closeConnection(con);
		}
		System.out.println("Self Schedule End"+returnScheduleId);
		return returnScheduleId;
	}
	
	
	// scheduling details within date d1 and d2 of site s1
	
	public LinkedHashMap<String, ArrayList<SchedulingDto>> getScheduleOfEmpForDownload(String fromDate, String toDate, String site) {
		DbConnect ob = DbConnect.getInstance();
		System.out.println("(((((((((((((");
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		System.out.println(")))))))))))))");
		SchedulingDto dto = null;// new SchedulingDto();
		ScheduleAlterDto adto = null;
		ArrayList<SchedulingDto> dtoList = null;
		LinkedHashMap<String, ArrayList<SchedulingDto>> dtoMap = new LinkedHashMap<String, ArrayList<SchedulingDto>>();
		 
		//String query = "select esh.id scheduleId, ifnull(prj.project,'') projectName, esub.subscriptionId, esh.employee_id, e.personnelNo, e.displayName, e.site, esh.id, esha.date, ifnull(ifnull(esha.login_time, esh.login_time),'none') login_time, ifnull(ifnull(esha.logout_time, esh.logout_time),'none') logout_time, esh.from_date , ifnull(esh.cancel_date, esh.to_Date) to_date, ifnull(esh.login_time,'none') login_time1, ifnull(esh.logout_time,'none') logout_time1, esub.fromDate subFromDate,  ifnull(date_add(unsub.fromDate, interval -1 day), STR_TO_DATE('" + OtherFunctions.changeDateFromatToIso(toDate) + "' , '%Y-%m-%d' ) ) subToDate from employee_schedule esh join employee_subscription esub on esh.subscription_Id=esub.subscriptionId join employee e on esh.employee_id=e.id left join atsconnect prj on esh.project=prj.id left join employee_schedule_alter esha on (esh.id=esha.scheduleId and   esha.date between  '" + OtherFunctions.changeDateFromatToIso(fromDate) + "' and '" + OtherFunctions.changeDateFromatToIso(toDate) + "'   )  left join employee_unsubscription unsub on esub.subscriptionId=unsub.subscriptionId where (unsub.fromDate>'" + OtherFunctions.changeDateFromatToIso(fromDate) + "'  or unsub.fromDate is null ) and e.site=" + site + " and ifnull(esh.cancel_date, esh.to_Date)  >=esh.from_date and ( esh.from_date between '" + OtherFunctions.changeDateFromatToIso(fromDate) + "' and '" + OtherFunctions.changeDateFromatToIso(toDate) + "'   or ifnull(esh.cancel_date, esh.to_Date)  between '" + OtherFunctions.changeDateFromatToIso(fromDate) + "' and '" + OtherFunctions.changeDateFromatToIso(toDate) + "'  )   order by   esh.employee_id, esh.from_date, esha.date, esub.fromDate   ";
		String query = "select esh.id scheduleId, ifnull(prj.project,'') projectName, esub.subscriptionId, esh.employee_id, e.personnelNo, e.displayName, e.site, esh.id, esha.date, ifnull(ifnull(esha.login_time, esh.login_time),'none') login_time, ifnull(ifnull(esha.logout_time, esh.logout_time),'none') logout_time, esh.from_date , ifnull(esh.cancel_date, esh.to_Date) to_date, ifnull(esh.login_time,'none') login_time1, ifnull(esh.logout_time,'none') logout_time1, esub.fromDate subFromDate,  ifnull(date_add(unsub.fromDate, interval -1 day), STR_TO_DATE( '" + OtherFunctions.changeDateFromatToIso(toDate) + "' , '%Y-%m-%d' ) ) subToDate from employee_schedule esh  join employee_subscription esub on esh.subscription_Id=esub.subscriptionId  join employee e on esh.employee_id=e.id left join atsconnect prj on esh.project=prj.id  left join employee_schedule_alter esha on (esh.id=esha.scheduleId and   esha.date>='" + OtherFunctions.changeDateFromatToIso(fromDate)+ "' and esha.date<='" + OtherFunctions.changeDateFromatToIso(toDate) + "'  )   left join employee_unsubscription unsub on esub.subscriptionId=unsub.subscriptionId  where (unsub.fromDate>'" + OtherFunctions.changeDateFromatToIso(toDate) + "'  or unsub.fromDate is null ) and e.site=" + site + " and ifnull(esh.cancel_date, esh.to_Date)  >=esh.from_date and (  (esh.from_date >='" + OtherFunctions.changeDateFromatToIso(fromDate)+ "' and esh.from_date <='" + OtherFunctions.changeDateFromatToIso(toDate) + "' )  or (ifnull(esh.cancel_date, esh.to_Date) >='" + OtherFunctions.changeDateFromatToIso(fromDate)+ "' and ifnull(esh.cancel_date, esh.to_Date) <='" + OtherFunctions.changeDateFromatToIso(toDate) + "' ) or ( esh.from_date <='" + OtherFunctions.changeDateFromatToIso(fromDate)+ "' and ifnull(esh.cancel_date, esh.to_Date) >='" + OtherFunctions.changeDateFromatToIso(toDate) + "' )  )   order by   esh.employee_id, esh.from_date, esha.date, esub.fromDate ";
	System.out.println(query);
		try {
			con = ob.connectDB();
			st = con.createStatement();
			rs = st.executeQuery(query);
			boolean newEmp=true;
			String preSubId = "";
			String preShId = "";
			long i=0;
			while (rs.next()) {
				adto = new ScheduleAlterDto();
				String curShId = rs.getString("scheduleId");
				String curSubId = rs.getString("subscriptionId");
//				System.out.println("Pre sub id"+preSubId + " , Cur sub id " + curSubId  + " ");
				if(preSubId.equals(curSubId)==false) {
//					System.out.println(" New Entry list ");
					dtoList = new ArrayList<SchedulingDto> ();
					dtoMap.put(String.valueOf(i++), dtoList);
				}
				if(preShId.equals(curShId)==false) {
				/*	if(newEmp) {
						System.out.println(">>>>>>>>>>>>>>");
						System.out.println(rs.getString("from_date"));
						System.out.println(rs.getString("to_date"));
						
					}
				*/	newEmp=false;
					dto = new SchedulingDto();
					dto.setScheduleId(rs.getLong("scheduleId"));
					dtoList.add(dto);
					dto.setEmployeeId(rs.getString("personnelNo"));
					dto.setEmployeeName (rs.getString("displayName"));
					dto.setLoginTime(rs.getString("login_time1"));
					dto.setLogoutTime(rs.getString("logout_time1"));					
					dto.setSchedulingFromDate(rs.getString("from_date"));
					dto.setSchedulingToDate(rs.getString("to_date"));
					dto.setSubscriptionDate(rs.getString("subFromDate"));
					dto.setSubscriptionToDate(rs.getString("subToDate"));
					dto.setProject(rs.getString("projectName"));
					
				}
					if(rs.getDate("date")!=null) {
						
						adto.setDate(rs.getString("date"));
						
						adto.setLoginTime(rs.getString("login_time"));
						adto.setLogoutTime(rs.getString("logout_time"));
						
						
					
					} else {
						adto.setDate(rs.getString("from_date"));
						adto.setLoginTime(rs.getString("login_time1"));
						adto.setLogoutTime(rs.getString("logout_time1"));
						
					}
					if(OtherFunctions.isEmpty(adto.getLoginTime()))
					{
						adto.setLoginTime("off");
					}
					if(OtherFunctions.isEmpty(adto.getLogoutTime()))
					{
						adto.setLogoutTime("off");
					}
					dto.getAlterList().add(adto);
				 
				preSubId = curSubId;
				preShId = curShId;
				}
				
				
			  
		 
		} catch (Exception e) {
			System.out.println("ERROR in getDetailsForSchedule " + e);
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);
			//System.out.println(" Connection closed..");
		}
		return dtoMap;
	}
	
	
	public ArrayList<SchedulingDto> getEmployeesHavingNoSchedule(String fromDate, String toDate, String site)  {
		
		DbConnect ob = DbConnect.getInstance();
		System.out.println("(((((((((((((");
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		System.out.println(")))))))))))))");
		SchedulingDto dto = null;// new SchedulingDto();
		 
		ArrayList<SchedulingDto> dtoList = new ArrayList<SchedulingDto> ();
		//String query = "select esh.employee_id, e.personnelNo, e.displayName, e.site, esh.id, esha.date, ifnull(esh.login_time, esha.login_time) login_time, ifnull(esh.logout_time, esha.logout_time) logout_time, esh.from_date , ifnull(esh.cancel_date, esh.to_Date) to_date, esh.login_time login_time1, esh.logout_time logout_time1  from employee_schedule esh join employee e on esh.employee_id=e.id left join employee_schedule_alter esha on esh.id=esha.scheduleId where e.site=" + site + " and ifnull(esh.cancel_date, esh.to_Date)  >=esh.from_date and ( esh.from_date between '" + OtherFunctions.changeDateFromatToIso(fromDate) + "' and '" + OtherFunctions.changeDateFromatToIso(toDate) + "'   or ifnull(esh.cancel_date, esh.to_Date)  between '" + OtherFunctions.changeDateFromatToIso(fromDate) + "' and '" + OtherFunctions.changeDateFromatToIso(toDate) + "'  ) order by esh.employee_id, esh.from_date, esha.date  ";
		String query = "select e.project, e.projectUnit, esh.id, esub.subscriptionId,  esub.empid employee_id , e.personnelNo, e.displayName, e.site, esub.fromDate subFromDate, ifnull(date_add(unsub.fromDate, interval -1 day), STR_TO_DATE('" + OtherFunctions.changeDateFromatToIso(toDate) + "' , '%Y-%m-%d' ) ) subToDate   from employee_subscription esub join employee e on esub.empid=e.id and e.site=" + site + " and  esub.fromDate<='" + OtherFunctions.changeDateFromatToIso(toDate) + "' left join employee_unsubscription unsub on esub.subscriptionId=unsub.subscriptionId left join employee_schedule esh on esub.subscriptionId = esh.subscription_Id and " +
				" ifnull(esh.cancel_date, esh.to_Date)  >=esh.from_date" +
				" and (  ( esh.from_date >='" + OtherFunctions.changeDateFromatToIso(fromDate) + "' and esh.from_date<='" + OtherFunctions.changeDateFromatToIso(toDate) + "' )" +
						"   or ( ifnull(esh.cancel_date, esh.to_Date)  >='" + OtherFunctions.changeDateFromatToIso(fromDate) + "' and ifnull(esh.cancel_date, esh.to_Date)  <='" + OtherFunctions.changeDateFromatToIso(toDate) + "' ) " +
								" or ( esh.from_date <='" + OtherFunctions.changeDateFromatToIso(fromDate) + "' and ifnull(esh.cancel_date, esh.to_Date)>='" + OtherFunctions.changeDateFromatToIso(toDate) + "'  ) " +
								" )  where (unsub.fromDate>'" + OtherFunctions.changeDateFromatToIso(fromDate) + "'  or unsub.fromDate is null ) and esh.id is null  ";
	System.out.println(query);
		try {
			con = ob.connectDB();
			st = con.createStatement();
			rs = st.executeQuery(query);
			 
			 
			while (rs.next()) {
					dto = new SchedulingDto();
					 
					dto.setEmployeeId(rs.getString("personnelNo"));
					dto.setEmployeeName(rs.getString("displayName"));
					dto.setSubscriptionDate(rs.getString("subFromDate"));
					dto.setSubscriptionToDate(rs.getString("subToDate"));
					dto.setProject(rs.getString("project"));
					 
					 
				//	System.out.print (dto.getEmployeeId() + "\t "+ dto.getEmployeeName() );
					Calendar cal = Calendar.getInstance();
					
					Calendar calEnd = Calendar.getInstance();
					
					cal.setTime(OtherFunctions.changeDateFromat_ddmmyyy_to_UtilDate(fromDate));
					calEnd.setTime(OtherFunctions.changeDateFromat_ddmmyyy_to_UtilDate(toDate));
					Calendar calSubStart = Calendar.getInstance();
					Calendar calSubEnd = Calendar.getInstance();
					calSubStart.setTime(OtherFunctions.changeDateFromat_ddmmyyy_to_UtilDate(OtherFunctions.changeDateFromatToddmmyyyy(  rs.getString("subFromDate") )));
					calSubEnd.setTime(OtherFunctions.changeDateFromat_ddmmyyy_to_UtilDate(OtherFunctions.changeDateFromatToddmmyyyy(  rs.getString("subToDate") )));
					 
					 while(  cal.compareTo(calEnd)<=0) {
							 
			//			 System.out.print("\t" + OtherFunctions.changeDateFromat(OtherFunctions.changeDateFromat(cal.getTime())) );
						 ScheduleAlterDto alterDto = new ScheduleAlterDto();
						 alterDto .setDate( OtherFunctions.changeDateFromat(  cal.getTime() ));
						if(cal.compareTo(calSubStart)<0  )
						{
							alterDto.setLoginTime("INVALID");
							alterDto.setLogoutTime("INVALID");
						} else if(cal.compareTo(calSubStart)>=0 && cal.compareTo(calSubEnd) <=0  )
						{
							alterDto.setLoginTime("none");
							alterDto.setLogoutTime("none");
						} else if(cal.compareTo(calSubEnd)>0 )
						{
							alterDto.setLoginTime("INVALID");
							alterDto.setLogoutTime("INVALID");
						}
					 
						 cal.add(Calendar.DATE, 1);
						dto.getAlterList().add(alterDto);
					} 
				//	 System.out.println();
				 dtoList.add(dto);
				 
				}
				
				
			  
		 
		} catch (Exception e) {
			System.out.println("ERROR in getDetailsForSchedule " + e);
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);
			  
		}
		return dtoList;
		
	}
	
}