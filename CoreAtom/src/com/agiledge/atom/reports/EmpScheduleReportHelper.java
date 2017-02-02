package com.agiledge.atom.reports;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.agiledge.atom.commons.OtherFunctions;
import com.agiledge.atom.dbConnect.DbConnect;
import com.agiledge.atom.reports.dto.EmpSchedule;



public class EmpScheduleReportHelper {

	/*public List<EmpSchedule> getActiveScheduleDetails(String fromDate,
			String toDate, String siteID,  String spoc,
			String project,String shiftInTime, String shiftOutTime,String empId) {

		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		fromDate=fromDate==null?"":fromDate;
		toDate=toDate==null?"":toDate;
		siteID=siteID==null?"":siteID;
		 
		spoc=spoc==null?"":spoc;
		project=project==null?"":project;
		shiftInTime=shiftInTime==null?"":shiftInTime;
		shiftOutTime=shiftOutTime==null?"":shiftOutTime;
		empId=empId==null?"":empId;
		
		try{
			if(siteID.trim().equals("")==false)
			{
		 
		fromDate= OtherFunctions.changeDateFromatToIso(fromDate);
		toDate=OtherFunctions.changeDateFromatToIso(toDate);

		System.out.println("from Date " + fromDate);
		System.out.println("to Date " + toDate);
		System.out.println("siteID " + siteID);
		 
		System.out.println("spoc " + spoc);
		System.out.println("project" + project);
		DbConnect dbConn = DbConnect.getInstance();
		
		List<EmpSchedule> scheduleList = new ArrayList<EmpSchedule>();
	  

			String query = "select e.PersonnelNo as EmployeeID, e.displayName as EmployeeName,proj.project, proj.description, es.from_date,es.to_date,es.login_time,es.logout_time,sup.displayName as Manager,spoc.displayName as SPOC"
					+ " from employee_schedule es,employee_subscription esub,employee e ,employee sup,employee spoc, atsconnect proj where es.status='a' and es.employee_id=e.id "
					+ "and esub.spoc=sup.id and esub.supervisor=spoc.id and es.project=proj.id and es.subscription_id=esub.subscriptionID and "
					+ "((es.from_date<='"
					+ fromDate
					+ "' and  ifnull(es.cancel_date,es.to_date)>='"
					+ toDate
					+ "' ) or " +
					" (es.from_date>='"+fromDate+"' and  ifnull(es.cancel_date,es.to_date)<='"+toDate+"' ) or " +
					" (es.to_date>='"+fromDate+"' and  ifnull(es.cancel_date,es.to_date)<='"+toDate+"' ) or  " +
					" (es.from_date>='"+fromDate+"' and  es.from_date<='"+toDate+"' ) " +
					")";
 
			if (spoc != null && !spoc.equals(""))
				query = query + " and ( esub.spoc=" + spoc + " or  esub.supervisor=" + spoc+" ) ";
			if (project != null && !project.equals(""))
				query = query + " and es.project=" + project;
			if (shiftInTime != null && !shiftInTime.equals(""))
				query = query + "  and es.login_time='" + shiftInTime+ "' ";
			if (shiftOutTime != null && !shiftOutTime.equals(""))
				query = query + "  and es.logout_time='" + shiftOutTime+ "' ";
			if (empId != null && !empId.equals(""))
				query = query + "  and e.id=" + empId+ " ";

			System.out.println(query);

			// "SELECT e.id,e.PersonnelNo,e.EmployeeFirstName,e.EmployeeLastName,e.Gender,a.area,p.place,l.id as landmarkId ,l.landmark  FROM employee e,employee_subscription es,area a,place p,landmark l where e.id=es.empID and (es.subscriptionStatus='subscribed' or es.subscriptionStatus='pending') and es.fromDate >=?  and es.fromDate<= ? and es.landMark=l.id and l.place=p.id and p.area=a.id";

			con = dbConn.connectDB();
			stmt = con.createStatement();
			// // stmt.setString(1, fromDate);
			// // stmt.setString(2, toDate);
			// stmt.setDate(1, new
			// java.sql.Date(sdf.parse(fromDate).getTime()));
			// stmt.setDate(2, new java.sql.Date(sdf.parse(toDate).getTime()));
			// stmt.setDate(3, new
			// java.sql.Date(sdf.parse(fromDate).getTime()));
			// stmt.setDate(4, new java.sql.Date(sdf.parse(toDate).getTime()));

			rs = stmt.executeQuery(query);

			while (rs.next()) {

				EmpSchedule emp = new EmpSchedule();
				emp.setEmpID(rs.getString("EmployeeID"));
				emp.setEmpName(rs.getString("EmployeeName"));
				emp.setProjectID(rs.getString("project"));
				emp.setProjectName(rs.getString("description"));
				emp.setFromDate(new java.util.Date(rs.getDate("from_date")
						.getTime()));
				emp.setLoginTime(rs.getString("login_time"));
				emp.setLogoutTime(rs.getString("logout_time"));
				emp.setToDate(new java.util.Date(rs.getDate("to_Date")
						.getTime()));
				emp.setManager(rs.getString("Manager"));
				emp.setSpoc(rs.getString("SPOC"));

				scheduleList.add(emp);
			}
			return scheduleList;
			}else
				return null;

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(stmt);
			DbConnect.closeConnection(con);
		}

		return null;

	}*/
	
	
	public List<EmpSchedule> getActiveScheduleDetails(String fromDate,
			String toDate, String siteID,  String spoc,
			String project,String shiftInTime, String shiftOutTime,String empId) {

		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		fromDate=fromDate==null?"":fromDate;
		toDate=toDate==null?"":toDate;
		siteID=siteID==null?"":siteID;
		 
		spoc=spoc==null?"":spoc;
		project=project==null?"":project;
		shiftInTime=shiftInTime==null?"":shiftInTime;
		shiftOutTime=shiftOutTime==null?"":shiftOutTime;
		empId=empId==null?"":empId;
		
		try{
			if(siteID.trim().equals("")==false)
			{
		 
		fromDate= OtherFunctions.changeDateFromatToIso(fromDate);
		toDate=OtherFunctions.changeDateFromatToIso(toDate);

		System.out.println("from Date " + fromDate);
		System.out.println("to Date " + toDate);
		System.out.println("siteID " + siteID);
		 
		System.out.println("spoc " + spoc);
		System.out.println("project" + project);
		DbConnect dbConn = DbConnect.getInstance();
		
		List<EmpSchedule> scheduleList = new ArrayList<EmpSchedule>();
	  

			String query = "select distinct   s.date, e.site,  e.displayName EmployeeName, e.PersonnelNo EmployeeID,s.projectId, s.projectName,s.description, s.SPOC, s.spocId,s.managerId, s.Manager,  s.description, ifnull(a.login_time,s.login_time) login_time, ifnull(a.logout_time,s.logout_time) logout_time from employee e,  (select aa.DateVal date, sh.*,spoc.id spocId, spoc.displayname SPOC, manager.id managerId, manager.displayname Manager, proj.id projectId, proj.project projectName, proj.description description from (select DATE_FORMAT(d2.m1, '%Y-%m-%d') as DateVal from (select ('"+fromDate+"' - INTERVAL DAYOFMONTH('"+fromDate+"')-1 DAY) +INTERVAL m day as m1 from (select @rownum:=@rownum+1 as m from (select 1 union select 2 union select 3 union select 4) t1,(select 1 union select 2 union select 3 union select 4) t2,(select 1 union select 2 union select 3 union select 4) t3,(select 1 union select 2 union select 3 union select 4) t4,(select @rownum:=-1) t0) d1) d2 where m1<='"+toDate+"' and m1>='"+fromDate+"' order by m1) as aa, employee_schedule sh join  ( select s.*, u.fromDate uFromDate from employee_subscription s left join employee_unsubscription u on s.subscriptionID=u.subscriptionID )   es on sh.subscription_id=es.subscriptionID join employee spoc on spoc.id=es.spoc join employee manager on manager.id=es.supervisor join atsconnect proj on sh.project= proj.id where    (aa.DateVal between sh.from_date and ifnull(sh.cancel_date,sh.to_date) and (es.uFromDate is null or  es.uFromDate>aa.DateVal) )     )  s   left join( select * from employee_schedule_alter ) a on a.scheduleId=s.id    and s.date=a.date where e.id=s.employee_id   and ((ifnull(a.login_time,s.login_time)!='weekly off' and ifnull(a.logout_time, s.logout_time)!='weekly off') or ( (ifnull(a.login_time,s.login_time)!=''and  ifnull(a.logout_time, s.logout_time)='')) )  and e.site="+siteID; 
			if (spoc != null && !spoc.equals(""))
				query = query + " and (  s.spocId=" + spoc + " or  s.managerId=" + spoc+" ) ";
			if (project != null && !project.equals(""))
				query = query + " and s.projectId=" + project;
			if (shiftInTime != null && !shiftInTime.equals(""))
				query = query + "  and ifnull(a.login_time, s.login_time)='" + shiftInTime+ "' ";
			if (shiftOutTime != null && !shiftOutTime.equals(""))
				query = query + "  and ifnull(a.logout_time,s.logout_time)='" + shiftOutTime+ "' ";
			if (empId != null && !empId.equals(""))
				query = query + "  and e.id=" + empId+ " ";
			query+=" order by date ";
			System.out.println(query);

			// "SELECT e.id,e.PersonnelNo,e.EmployeeFirstName,e.EmployeeLastName,e.Gender,a.area,p.place,l.id as landmarkId ,l.landmark  FROM employee e,employee_subscription es,area a,place p,landmark l where e.id=es.empID and (es.subscriptionStatus='subscribed' or es.subscriptionStatus='pending') and es.fromDate >=?  and es.fromDate<= ? and es.landMark=l.id and l.place=p.id and p.area=a.id";

			con = dbConn.connectDB();
			stmt = con.createStatement();
			// // stmt.setString(1, fromDate);
			// // stmt.setString(2, toDate);
			// stmt.setDate(1, new
			// java.sql.Date(sdf.parse(fromDate).getTime()));
			// stmt.setDate(2, new java.sql.Date(sdf.parse(toDate).getTime()));
			// stmt.setDate(3, new
			// java.sql.Date(sdf.parse(fromDate).getTime()));
			// stmt.setDate(4, new java.sql.Date(sdf.parse(toDate).getTime()));

			rs = stmt.executeQuery(query);

			while (rs.next()) {

				EmpSchedule emp = new EmpSchedule();
				emp.setEmpID(rs.getString("EmployeeID"));
				emp.setEmpName(rs.getString("EmployeeName"));
				emp.setProjectID(rs.getString("projectName"));
				emp.setProjectName(rs.getString("description"));
				emp.setDate(new java.util.Date(rs.getDate("date")
						.getTime()));
				emp.setLoginTime(rs.getString("login_time"));
				emp.setLogoutTime(rs.getString("logout_time"));
				
						 
				emp.setManager(rs.getString("Manager"));
				emp.setSpoc(rs.getString("SPOC"));

				scheduleList.add(emp);
			}
			return scheduleList;
			}else
				return null;

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(stmt);
			DbConnect.closeConnection(con);
		}

		return null;

	}

}
