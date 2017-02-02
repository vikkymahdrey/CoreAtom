package com.agiledge.atom.reports;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.agiledge.atom.commons.OtherFunctions;
import com.agiledge.atom.dbConnect.DbConnect;
import com.agiledge.atom.dto.DayReportDto;
import com.agiledge.atom.dto.ScheduledEmpDto;
import com.agiledge.atom.reports.dto.EmpSubscription;
import com.agiledge.atom.reports.dto.SubscriptionReportDto;



public class EmpSubscriptionReportHelper {
	
	
	
	//String query="select * from (select  st.subscriptionDate, st.fromDate, e.PersonnelNo staff#, e.id, e.displayname as Staff,st.contact#, st.isContractEmployee, st.Manager, st.managerId, st.SPOC,st.spocId, st.apl, st.landmarkId,st.projectName project, st.projectId,st.project code ,st.login_time, st.logout_time,  st.subscriptionStatus,site.site_name , e.site siteId,  e.Gender from employee e  join( select s.subscriptionDate, e.id,s.subscriptionID, s.fromDate,  e.PersonnelNo staff#, e.isContractEmployee, e.displayname as Staff, e.contactNumber1 as contact#,e.isContractEmployee as Roll, spoc.displayname as SPOC, spoc.id as spocId, manager.displayname as Manager, manager.id as managerId, apl.apl, s.landMark as landmarkId, p.project, p.description projectName, p.id as projectId, h.login_time,h.logout_time, s.subscriptionStatus  from employee_subscription s   join employee_schedule h on s.subscriptionID=h.subscription_id join atsconnect p on h.project=p.id join (select landmark.id landmarkId, (landmark.landmark + ' -> ' + place.place + ' -> ' +  area.area ) apl from landmark join place on landmark.place=place.id join area on place.area=area.id) apl on s.landMark=apl.landmarkId join employee e on s.empID=e.id join employee spoc on s.spoc=spoc.id join employee manager on s.supervisor=manager.id  where h.from_date=(select max(h1.from_date) from_date from employee_schedule h1 where h1.subscription_id=h.subscription_id )  union  select s.subscriptionDate, e.id,s.subscriptionID,s.fromDate,  e.PersonnelNo staff#, e.isContractEmployee,  e.displayname as Staff, e.contactNumber1 as contact#, e.isContractEmployee as Roll, spoc.displayname as SPOC, spoc.id as spocId, manager.displayname as Manager, manager.id as mangerId,  apl.apl, s.landMark as landmarkId, '' project,  ''  projectName, '-1'  projectId,  '' login_time,'' logout_time,s.subscriptionStatus   from employee_subscription s      join (select landmark.id landmarkId, (landmark.landmark + ' -> ' + place.place + ' -> ' +  area.area ) apl from landmark join place on landmark.place=place.id join area on place.area=area.id) apl on s.landMark=apl.landmarkId join employee e on s.empID=e.id join employee spoc on s.spoc=spoc.id join employee manager on s.supervisor=manager.id where s.subscriptionID not in  ( select subscription_id from employee_schedule) ) st  on  e.id = st.id, site where site.id=e.site ) rpt  order by rpt.subscriptionDate, rpt.fromDate ";

	public List<EmpSubscription> getAllSubscriptionDetails(String fromDate,
			String toDate, String manager, String spoc) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		SimpleDateFormat sdf1 = new SimpleDateFormat("MM/dd/yyyy");

		System.out.println("from Date " + fromDate);
		System.out.println("to Date " + toDate);
		DbConnect dbConn = DbConnect.getInstance();
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		List<EmpSubscription> subscriptionList = new ArrayList<EmpSubscription>();
		try {
			Date dbFromDate = sdf.parse(fromDate);
			Date dbToDate = sdf.parse(toDate);
			String query = "SELECT e.id,e.PersonnelNo,e.EmployeeFirstName,e.EmployeeLastName,em.EmployeeFirstName+em.employeeLastName as Manager,esp.EmployeeFirstName+esp.employeeLastName as SPOC, es.subscriptionDate,es.fromDate,a.area,p.place,l.landmark"
					+ " FROM employee e,employee em,employee esp,employee_subscription es,area a,place p,landmark l"
					+ " where e.id=es.empID and em.id=es.supervisor and esp.id=es.spoc and (es.subscriptionStatus='subscribed' or es.subscriptionStatus='pending')"
					+ " and es.landMark=l.id and l.place=p.id and p.area=a.id "
					+ "and es.fromDate >='"
					+ sdf1.format(dbFromDate)
					+ "' and es.fromDate<='" + sdf1.format(dbToDate) + "' ";
			
			if (manager != null && !manager.equals(""))
				query = query + " and esub.supervisor=" + manager;
			if (spoc != null && !spoc.equals(""))
				query = query + " and esub.spoc=" + spoc;

			System.out.println("Query: " + query);
			// "SELECT e.id,e.PersonnelNo,e.EmployeeFirstName,e.EmployeeLastName,e.Gender,a.area,p.place,l.id as landmarkId ,l.landmark  FROM employee e,employee_subscription es,area a,place p,landmark l where e.id=es.empID and (es.subscriptionStatus='subscribed' or es.subscriptionStatus='pending') and es.fromDate >=?  and es.fromDate<= ? and es.landMark=l.id and l.place=p.id and p.area=a.id";

			con = dbConn.connectDB();
			stmt = con.createStatement();
			// stmt.setString(1, fromDate);
			// stmt.setString(2, toDate);
			// stmt.setDate(1, new
			// java.sql.Date(sdf.parse(fromDate).getTime()));
			// stmt.setDate(2, new java.sql.Date(sdf.parse(toDate).getTime()));

			rs = stmt.executeQuery(query);

			while (rs.next()) {
				System.out.println(rs.getString("PersonnelNo"));
				EmpSubscription empSub = new EmpSubscription();
				empSub.setEmpID(rs.getString("PersonnelNo"));
				empSub.setEmpName(rs.getString("EmployeeFirstName") + " "
						+ rs.getString("EmployeeLastName"));
				empSub.setSubscriptionDate(new java.util.Date(rs.getDate(
						"subscriptionDate").getTime()));
				empSub.setEffectiveDate(new java.util.Date(rs.getDate(
						"fromDate").getTime()));
				empSub.setManager(rs.getString("Manager"));
				empSub.setSpoc(rs.getString("SPOC"));
				empSub.setArea(rs.getString("area"));
				empSub.setPlace(rs.getString("place"));
				empSub.setLandmark(rs.getString("landmark"));
				subscriptionList.add(empSub);
			}
			return subscriptionList;

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(stmt);
			DbConnect.closeConnection(con);
		}

		return null;

	}
	
	
	public List<SubscriptionReportDto> getSubscripionReport(String fromDate,
			String toDate,  String spoc, String site,String empId) throws ParseException {
		fromDate=fromDate==null?"":fromDate;
		toDate=toDate==null?"":toDate;
		 
		spoc=spoc==null?"":spoc;
		site=site==null?"":site;
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		try{
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");		
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");		
		sdf1.format(sdf.parse(fromDate));
		sdf1.format(sdf.parse(toDate));
		
		fromDate=OtherFunctions.changeDateFromatToIso(fromDate);
		toDate=OtherFunctions.changeDateFromatToIso(toDate);
		System.out.println("from Date " + fromDate);
		System.out.println("to Date " + toDate);
		DbConnect dbConn = DbConnect.getInstance();
		
		List<SubscriptionReportDto> subscriptionList = new ArrayList<SubscriptionReportDto>();
		
			 
			 

			String query = "select distinct site_name, date_format(fromDate,'%d/%m/%y') fromDate,date_format(subscriptionDate,'%d/%m/%y') subscriptionDate,staffNo,staffName,case when gender='M' then 'Male' else 'Female' end  gender,contactNo,role,managerName,spocName, landmarkName, placeName,areaName,project, projectCode,esub.subscriptionStatus,ifnull(date_format(esub.scheduledUpTo,'%d/%m/%y'),'') scheduledUpTo from (	select *,(select to_date from employee_schedule where employee_id=sub.empID order by to_date desc limit 1) scheduledUpTo  from employee_subscription sub,(select displayName spocName,id spocId from employee ) spoc,(select displayName managerName,id managerId from employee ) manager,(select employee.id staffId, displayname staffName, PersonnelNo staffNo,isContractEmployee role, gender,contactNumber1 contactNo,site.id siteId, site_name from employee, site where site.id=employee.site) e,(select landmark.id landmarkId,area.area areaName,place.place placeName,landmark.landmark landmarkName from landmark,place, area where landmark.place=place.id and place.area=area.id)apl where sub.spoc=spoc.spocId and sub.supervisor=manager.managerId and apl.landmarkId=sub.landMark and e.staffId=sub.empID) esub left outer join (select es.subscription_id, es.id scheduleId,es.project projectId,p.project projectCode, p.description project,es.login_time, es.logout_time	 from employee_schedule es, atsconnect p where(es.from_date<='"
					+ fromDate
					+ "' and ifnull(es.cancel_date,es.to_date)>='"
					+ fromDate
					+ "'	or		es.from_date<='"
					+ toDate
					+ "' and ifnull(es.cancel_date, es.to_date)>='"
					+ toDate
					+ "'		or		'" + fromDate + "' < es.from_date and ifnull(es.cancel_date, es.to_date)<='"
					+ toDate
					+ "') and es.from_date<=ifnull(es.cancel_date,es.to_date) and p.id=es.project) as es on es.subscription_id=esub.subscriptionID where esub.fromDate<='"+toDate+"' and esub.subscriptionID not in (select subscriptionID from employee_unsubscription where fromDate<='" + fromDate + "') ";
			 
			if (spoc != null && !spoc.equals(""))
				query = query + " and ( spocId=" + spoc + " or managerId="
						+ spoc + " ) ";
			if (site != null && !site.equals(""))
				query = query + " and esub.siteId=" + site;
			if (empId != null && !empId.equals(""))
				query = query + " and esub.empID=" + empId;
					
			
			

		 

			System.out.println("Query: " + query);
			// "SELECT e.id,e.PersonnelNo,e.EmployeeFirstName,e.EmployeeLastName,e.Gender,a.area,p.place,l.id as landmarkId ,l.landmark  FROM employee e,employee_subscription es,area a,place p,landmark l where e.id=es.empID and (es.subscriptionStatus='subscribed' or es.subscriptionStatus='pending') and es.fromDate >=?  and es.fromDate<= ? and es.landMark=l.id and l.place=p.id and p.area=a.id";

			con = dbConn.connectDB();
			stmt = con.createStatement();
			// stmt.setString(1, fromDate);
			// stmt.setString(2, toDate);
			// stmt.setDate(1, new
			// java.sql.Date(sdf.parse(fromDate).getTime()));
			// stmt.setDate(2, new java.sql.Date(sdf.parse(toDate).getTime()));

			rs = stmt.executeQuery(query);

			while (rs.next()) {
				// System.out.println(rs.getString("PersonnelNo"));
				SubscriptionReportDto empSub = new SubscriptionReportDto();
		 
				 empSub.setSiteName(rs.getString("site_name"));
				 empSub.setEffectiveDate(rs.getString("fromDate"));
				 empSub.setSubscriptionDate (rs.getString("subscriptionDate"));
				 empSub.setStaffCode(rs.getString("staffNo"));
				 empSub.setStaff(rs.getString("staffName"));
				 empSub.setGender(rs.getString("gender"));
				 empSub.setContactNo(rs.getString("contactNo"));
				 empSub.setRoll(rs.getString("role"));
				 empSub.setSpoc(rs.getString("spocName"));
				 empSub.setManager(rs.getString("managerName"));
				 empSub.setArea(rs.getString("areaName"));
				 empSub.setPlace(rs.getString("placeName"));
				 empSub.setLandmark(rs.getString("landmarkName"));
				 empSub.setProject(rs.getString("project"));
				 empSub.setProjectCode(rs.getString("projectCode"));
 
				 empSub.setSubscriptionStatus(rs.getString("subscriptionStatus"));
				 empSub.setScheduledUpTo(rs.getString("scheduledUpTo"));
				 
				 
				  
				
				
				
				 
				subscriptionList.add(empSub);
			}
			return subscriptionList;

		} catch (Exception e) {
			System.out.println(" Exception : " + e);
			e.printStackTrace();
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(stmt);
			DbConnect.closeConnection(con);
		}

		return null;

	}


	}
