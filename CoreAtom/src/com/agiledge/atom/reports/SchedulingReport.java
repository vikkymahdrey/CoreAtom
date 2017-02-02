package com.agiledge.atom.reports;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.agiledge.atom.commons.OtherFunctions;
import com.agiledge.atom.dbConnect.DbConnect;
import com.agiledge.atom.reports.dto.ExceptionReportDto;



public class SchedulingReport {
 
	
	 
	public List<ExceptionReportDto> getExceptionReport(String dateParam, String siteId,String log) {

	 
		String date=OtherFunctions.changeDateFromatToIso( dateParam) ;
		DbConnect dbConn = DbConnect.getInstance();
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		List<ExceptionReportDto> scheduleList = new ArrayList<ExceptionReportDto>();
		siteId=siteId==null?"":siteId;
		try {
		if(siteId.trim().equals("")==false)
		{
		
			 
			
			//String query="select *, site.site_name from (  select distinct id staffId ,displayname,PersonnelNo,'schedule cancelled' as currentStatus from employee, (select es.employee_id from employee_schedule es, (select ea.scheduleId from employee_schedule_alter ea where  ea.date='" + date + "' and  (ea.status='c' or ea.login_time='' or ea.login_time='none')) shId	 where  ( es.cancel_date is  null or es.cancel_date>'" + date + "' ) and  es.id = shId.scheduleId  ) empId1	  where id = empId1.employee_id union select distinct id  staffId, displayname, PersonnelNo,'weekly off' from employee, ((select es.employee_id from employee_schedule es where  ( es.cancel_date is  null or es.cancel_date>'" + date + "' ) and  es.id in  (select ea.scheduleId from employee_schedule_alter ea where  ea.date='" + date + "' and  (ea.login_time='weekly off')))) empId2 where id = empId2.employee_id union	 select distinct id  staffId, displayname, PersonnelNo,'No Schdule For The Day' from employee,  ((select employee_subscription.empID from employee_subscription, ( select subscriptionID from employee_unsubscription u where u.fromDate<='" + date + "') unsubo1, (select es1.employee_id from employee_schedule es1 where  ( es1.cancel_date is  null or es1.cancel_date>'" + date + "' ) and es1.from_date<='" + date + "' and es1.to_date>='" + date + "') esho1 where fromDate<='" + date + "' and  employee_subscription.subscriptionID !=unsubo1.subscriptionID  and  empID != esho1.employee_id )) empId3 where id = empId3.empID  union  select distinct id  staffId, displayname, PersonnelNo,'No Schdule For The Day' from employee, (select sub1.empID from employee_subscription sub1,( select subscriptionID from employee_unsubscription u where u.fromDate<='" + date + "')  us, (select es1.employee_id from employee_schedule es1, ( select scheduleId from employee_schedule_alter where date='" + date + "' ) eshh where  ( es1.cancel_date is  null or es1.cancel_date>'" + date + "') and (login_time='none' or login_time='') and ( logout_time='none' or logout_time='' ) and  es1.id != eshh.scheduleId  and es1.from_date<='" + date + "' and es1.to_date>='" + date + "' ) esh1 where fromDate<='" + date + "'  and   sub1.subscriptionID !=us.subscriptionID and sub1.empID = esh1.employee_id   ) empId4  where id= empId4.empID union  select distinct id  staffId, displayname, PersonnelNo,'Landmark Not In Route' from employee , ((select employee_subscription.empID, employee_subscription.subscriptionID, rt1.siteId from employee_subscription, ( select subscriptionID from employee_unsubscription u where u.fromDate<='" + date + "') sub1, (select routeChild.landmarkId , siteId from routeChild,route   where  routeChild.routeId=route.id ) rt1 where fromDate<='" + date + "' and employee_subscription. subscriptionID != sub1.subscriptionID and landmark  != rt1.landmarkId)) empId5 where id =empId5.empID and empId5.siteId=employee.site ) ex, employee e, site  where ex.staffId=e.id and e.site=site.id and site.id="+ siteId;
	 String query="select *, site.site_name from ("
+" (select distinct e.id staffId ,e.displayname,e.PersonnelNo,'Schedule Cancelled For "+log+" '  as currentStatus from employee e, employee_schedule es ,employee_schedule_alter ea where e.id=es.employee_id and (es.cancel_date is  null or es.cancel_date>'"+ date +"') and es.id=ea.scheduleId and ea.date='"+ date +"' and (ea.status='c' or (('"+log+"'='IN' and  (ea.login_time='' or ea.login_time='none')) or ('"+log+"'='OUT' and (ea.logout_time='' or ea.logout_time='none')) ) ) )" 
+" union"
+" (select distinct e.id staffId ,e.displayname,e.PersonnelNo,'Weekly OFF'  as currentStatus from  employee e, employee_schedule es ,employee_schedule_alter ea where e.id=es.employee_id and (es.cancel_date is  null or es.cancel_date>'"+ date +"') and es.id=ea.scheduleId and ea.date='"+ date +"' and (('"+log+"'='IN' and  (ea.login_time='weekly off')) or ('"+log+"'='OUT' and (ea.logout_time='weekly off'))))" 
+" union "
+" (select distinct e.id  staffId, e.displayname, e.PersonnelNo,'No Schedule For The Day' from employee e, employee_subscription esub where e.id=esub.empid and  esub.fromDate<='"+ date +"' and esub.empID not  in(select es1.employee_id from employee_schedule es1 where  ( es1.cancel_date is  null or es1.cancel_date>'"+ date +"' ) and es1.from_date<='"+ date +"'  and es1.to_date>='"+ date +"') and esub.subscriptionId not in (select subscriptionid from employee_unsubscription where fromDate<='"+ date +"' ))"
+" union"
+" (select distinct e.id  staffId, e.displayname, e.PersonnelNo,'No Schedule For "+log+"' from employee e,employee_subscription esub,employee_schedule es1 where e.id=esub.empid and  esub.fromDate<='"+ date +"' and esub.subscriptionid=es1.subscription_id and subscriptionID not in  ( select subscriptionID from employee_unsubscription u where u.fromDate<='"+ date +"') and (es1.cancel_date is  null or es1.cancel_date>'"+ date +"') and  (((login_time='none' or login_time='') and  '"+log+"'='IN') or ( (logout_time='none' or logout_time='') and '"+log+"'='OUT') ) and  es1.id not in ( select scheduleId from employee_schedule_alter where date='"+ date +"' )  and es1.from_date<='"+ date +"' and es1.to_date>='"+ date +"')" 
+" union "
+" (select distinct e.id  staffId, e.displayname, e.PersonnelNo,'Landmark Not In Any Primary Route' from employee e,employee_subscription esub  where e.id=esub.empId and  esub.fromDate<='"+ date +"' and   esub.subscriptionID not in ( select subscriptionID from employee_unsubscription u where u.fromDate<='"+ date +"')  and esub.landmark  not in(select landmarkId from routeChild rc ,route r where rc.routeId=r.id and r.siteId=e.site and r.siteId="+siteId+" and r.type='p'))" 
// +" union "
// +" (select distinct e.id  staffId, e.displayname, e.PersonnelNo,'Landmark Not In Primary Route' from employee e,employee_subscription esub  where e.id=esub.empId and  esub.fromDate<='"+ date +"' and   esub.subscriptionID not in ( select subscriptionID from employee_unsubscription u where u.fromDate<='"+ date +"') and esub.landmark  not in(select landmarkId from routeChild rc ,route r where rc.routeId=r.id and r.siteId=e.site and r.type='p'))"
+" ) ex, employee e, site where ex.staffId=e.id and  e.site=site.id and site.id="+siteId;
		 
			System.out.println(query);

			// "SELECT e.id,e.PersonnelNo,e.EmployeeFirstName,e.EmployeeLastName,e.Gender,a.area,p.place,l.id as landmarkId ,l.landmark  FROM employee e,employee_subscription es,area a,place p,landmark l where e.id=es.empID and (es.subscriptionStatus='subscribed' or es.subscriptionStatus='pending') and es.fromDate >=?  and es.fromDate<= ? and es.landMark=l.id and l.place=p.id and p.area=a.id";

			con = dbConn.connectDB();
			stmt = con.createStatement();
			 

			rs = stmt.executeQuery(query);

			while (rs.next()) {

				ExceptionReportDto ex = new ExceptionReportDto();
				ex.setStaff(rs.getString("displayName"));
				ex.setStaffId(rs.getString("staffId"));
				ex.setStatus(rs.getString("currentStatus"));
				ex.setPersonnelNo(rs.getString("personnelNo"));
				ex.setDate( OtherFunctions.changeDateFromatToddmmyyyy( date));

				scheduleList.add(ex);
			}
			return scheduleList;
		}else
		{
			return null;
		}

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
