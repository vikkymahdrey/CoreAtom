package com.agiledge.atom.reports;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.agiledge.atom.commons.OtherFunctions;
import com.agiledge.atom.dbConnect.DbConnect;
import com.agiledge.atom.reports.dto.TripBucketDto;



public class TripBucketReportHelper {

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
	
	
	public List<TripBucketDto> getTripBucket(String fromDate,
			String toDate, String siteID,  String shiftTime, String shiftLog ) {

		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		fromDate=fromDate==null?"":fromDate;
		toDate=toDate==null?"":toDate;
		siteID=siteID==null?"":siteID;
		  
		  
		shiftTime=shiftTime==null?"":shiftTime;
		shiftLog=shiftLog==null?"":shiftLog;
		 
		
		try{
			if(siteID.trim().equals("")==false)
			{
		 
		fromDate= OtherFunctions.changeDateFromatToIso(fromDate);
		toDate=OtherFunctions.changeDateFromatToIso(toDate);

		System.out.println("from Date " + fromDate);
		System.out.println("to Date " + toDate);
		System.out.println("siteID " + siteID);
		 
	 
		DbConnect dbConn = DbConnect.getInstance();
		
		List<TripBucketDto> scheduleList = new ArrayList<TripBucketDto>();
	  
		String query="";
	String queryNill = "select distinct 'ALL'trip_log, 'ALL'trip_time,  td.trip_date  , ifnull(opn.openCount,0)openCount, ifnull(rtc.rtcCount,0)rtcCount ,ifnull(stca.stcaCount,0)stcaCount , ifnull(rtm.rtmCount,0)rtmCount ,ifnull(stma.stmaCount,0) stmaCount, ifnull(atm.atmCount,0)atmCount from (  select * from trip_details td where  (td.status='saved' or td.status='saveedit' or td.status='addsave') )td left join ( select opn .trip_date,  openCount, siteId   from ( select td.trip_date, count(td.id) openCount  , td.siteId  from trip_details td left join vendor_trip_sheet_parent vtp on td.id=vtp.tripId join vehicles v on v.id=td.vehicle where  (td.status='saved' or td.status='saveedit' or td.status='addsave') and (approvalStatus is null  or approvalStatus='Open')    group by td.trip_date   ,  td.siteId    ) as opn) opn on opn.trip_date=td.trip_date     and opn.siteId=td.siteId    left join ( select  trip_date,  rtcCount,  siteId   from ( select td.trip_date, count(td.id) rtcCount   , td.siteId   from trip_details td   join vendor_trip_sheet_parent vtp on td.id=vtp.tripId  where  ( td.status='saved' or  td.status='saveedit' or  td.status='addsave')  and  approvalStatus='Rejected by Transport Co-ordinator'   group by td.trip_date , td.siteId   ) rtc )rtc on td.trip_date = rtc.trip_date  and  rtc.siteId=td.siteId  left join  (  select trip_date,  stcaCount, siteId    from (  select  td.trip_date, count(td.id) stcaCount  , td.siteId   from trip_details td   join vendor_trip_sheet_parent vtp on td.id=vtp.tripId  where  ( td.status='saved' or  td.status='saveedit' or  td.status='addsave')  and approvalStatus='Sent for TC approval'   group by td.trip_date  ,  td.siteId   ) stca)  stca on td.trip_date=stca.trip_date   and stca.siteId=td.siteId  left join  (  select trip_date,  rtmCount,  siteId   from ( select  td.trip_date, count(td.id) rtmCount   , td.siteId   from trip_details td   join vendor_trip_sheet_parent vtp on td.id=vtp.tripId  where  ( td.status='saved' or  td.status='saveedit' or  td.status='addsave') and    approvalStatus='Rejected by Transport Manager'   group by td.trip_date  , td.siteId  ) rtm ) rtm on td.trip_date=rtm.trip_date  and rtm.siteId=td.siteId  left join ( select trip_date,  stmaCount, siteId   from ( select  td.trip_date, count(td.id) stmaCount  ,  td.siteId from trip_details td   join vendor_trip_sheet_parent vtp on td.id=vtp.tripId  where  ( td.status='saved' or  td.status='saveedit' or  td.status='addsave') and       approvalStatus='Sent for TM approval'   group by td.trip_date   ,  td.siteId   ) stma ) stma on td.trip_date = stma.trip_date    and stma.siteId=td.siteId left join (  select  trip_date, atmCount , siteId  from  ( select  td.trip_date, count(td.id) atmCount , td.siteId  from trip_details td   join vendor_trip_sheet_parent vtp on td.id=vtp.tripId  where  ( td.status='saved' or  td.status='saveedit' or  td.status='addsave') and      approvalStatus='Approved by Transport Manager'    group by td.trip_date    , td.siteId  ) atm )atm on td.trip_date= atm.trip_date and   atm.siteId=td.siteId where  td.siteId="+siteID+"  and td.trip_date between '"+fromDate+"' and '"+toDate+"' ";
     String queryWithLogTimeAndLogType="select distinct td.trip_date, td.trip_time, td.trip_log , ifnull(opn.openCount,0)openCount, ifnull(rtc.rtcCount,0)rtcCount ,ifnull(stca.stcaCount,0)stcaCount , ifnull(rtm.rtmCount,0)rtmCount ,ifnull(stma.stmaCount,0) stmaCount, ifnull(atm.atmCount,0)atmCount from (  select * from trip_details td where  (td.status='saved' or td.status='saveedit' or td.status='addsave') )td left join ( select opn .trip_date,  openCount,trip_time,trip_log,siteId   from ( select td.trip_date, count(td.id) openCount , td.trip_time,td.trip_log, td.siteId  from trip_details td left join vendor_trip_sheet_parent vtp on td.id=vtp.tripId join vehicles v on v.id=td.vehicle where  (td.status='saved' or td.status='saveedit' or td.status='addsave') and (approvalStatus is null  or approvalStatus='Open')    group by td.trip_date , td.trip_time,td.trip_log, td.siteId    ) as opn) opn on opn.trip_date=td.trip_date   and td.trip_time=opn.trip_time and td.trip_log=opn.trip_log   and opn.siteId=td.siteId    left join ( select  trip_date,  rtcCount,trip_time,trip_log,siteId   from ( select td.trip_date, count(td.id) rtcCount, td.trip_time,td.trip_log , td.siteId   from trip_details td   join vendor_trip_sheet_parent vtp on td.id=vtp.tripId  where  (td.status='saved' or td.status='saveedit' or td.status='addsave')  and  approvalStatus='Rejected by Transport Co-ordinator'   group by td.trip_date, td.trip_time,td.trip_log, td.siteId   ) rtc )rtc on td.trip_date = rtc.trip_date  and td.trip_time=rtc.trip_time and td.trip_log=rtc.trip_log  and rtc.siteId=td.siteId  left join  (  select trip_date,  stcaCount,trip_time,trip_log,siteId    from (  select  td.trip_date, count(td.id) stcaCount , td.trip_time,td.trip_log, td.siteId   from trip_details td   join vendor_trip_sheet_parent vtp on td.id=vtp.tripId  where  (td.status='saved' or td.status='saveedit' or td.status='addsave')  and approvalStatus='Sent for TC approval'   group by td.trip_date, td.trip_time,td.trip_log, td.siteId   ) stca)  stca on td.trip_date=stca.trip_date  and td.trip_time=stca.trip_time and td.trip_log=stca.trip_log and stca.siteId=td.siteId  left join  (  select trip_date,  rtmCount,trip_time,trip_log,siteId   from ( select  td.trip_date, count(td.id) rtmCount , td.trip_time,td.trip_log , td.siteId   from trip_details td   join vendor_trip_sheet_parent vtp on td.id=vtp.tripId  where  (td.status='saved' or td.status='saveedit' or td.status='addsave') and    approvalStatus='Rejected by Transport Manager'   group by td.trip_date, td.trip_time,td.trip_log, td.siteId  ) rtm ) rtm on td.trip_date=rtm.trip_date  and td.trip_time=rtm.trip_time and td.trip_log=rtm.trip_log and rtm.siteId=td.siteId  left join ( select trip_date,  stmaCount,trip_time,trip_log,siteId   from ( select  td.trip_date, count(td.id) stmaCount , td.trip_time,td.trip_log, td.siteId from trip_details td   join vendor_trip_sheet_parent vtp on td.id=vtp.tripId  where  (td.status='saved' or td.status='saveedit' or td.status='addsave') and       approvalStatus='Sent for TM approval'   group by td.trip_date, td.trip_time,td.trip_log, td.siteId   ) stma ) stma on td.trip_date = stma.trip_date  and td.trip_time=stma.trip_time and stma.trip_log=td.trip_log  and stma.siteId=td.siteId left join (  select  trip_date, atmCount,trip_time,trip_log,siteId  from  ( select  td.trip_date, count(td.id) atmCount, td.trip_time,td.trip_log , td.siteId  from trip_details td   join vendor_trip_sheet_parent vtp on td.id=vtp.tripId  where  (td.status='saved' or td.status='saveedit' or td.status='addsave') and      approvalStatus='Approved by Transport Manager'    group by td.trip_date , td.trip_time,td.trip_log, td.siteId  ) atm )atm on td.trip_date= atm.trip_date and td.trip_time=atm.trip_time and atm.trip_log=td.trip_log  and atm.siteId=td.siteId   where td.siteId="+siteID+" and td.trip_time='"+shiftTime+"' and td.trip_log='"+shiftLog+"' "+"  and td.trip_date between '"+fromDate+"' and '"+toDate+"' ";
	 String queryWithLog="select distinct  'ALL'trip_time,  td.trip_date,  td.trip_log, td.siteId ,  ifnull(opn.openCount,0)openCount, ifnull(rtc.rtcCount,0)rtcCount ,ifnull(stca.stcaCount,0)stcaCount , ifnull(rtm.rtmCount,0)rtmCount ,ifnull(stma.stmaCount,0) stmaCount, ifnull(atm.atmCount,0)atmCount from ( select * from trip_details td where  (td.status='saved' or td.status='saveedit' or td.status='addsave') )td left join ( select opn .trip_date,  openCount ,trip_log, siteId   from ( select td.trip_date, count(td.id) openCount  ,td.trip_log,td.siteId from trip_details td left join vendor_trip_sheet_parent vtp on td.id=vtp.tripId join vehicles v on v.id=td.vehicle where  (td.status='saved' or td.status='saveedit' or td.status='addsave') and (approvalStatus is null  or approvalStatus='Open')    group by td.trip_date , td.trip_time,td.trip_log,td.siteId  ) as opn) opn on opn.trip_date=td.trip_date     and td.trip_log=opn.trip_log and opn.siteId=td.siteId    left join ( select  trip_date,  rtcCount, trip_log , siteId  from (  select td.trip_date, count(td.id) rtcCount, td.trip_log,td.siteId   from trip_details td   join vendor_trip_sheet_parent vtp on td.id=vtp.tripId where  (td.status='saved' or td.status='saveedit' or td.status='addsave')  and  approvalStatus='Rejected by Transport Co-ordinator'   group by td.trip_date, td.trip_time,td.trip_log,td.siteId  ) rtc )rtc on td.trip_date = rtc.trip_date    and td.trip_log=rtc.trip_log and rtc.siteId=td.siteId  left join   (  select trip_date,  stcaCount,  trip_log , siteId  from (  select  td.trip_date, count(td.id) stcaCount ,  td.trip_log,td.siteId  from trip_details td   join vendor_trip_sheet_parent vtp on td.id=vtp.tripId  where  (td.status='saved' or td.status='saveedit' or td.status='addsave')  and approvalStatus='Sent for TC approval'   group by td.trip_date, td.trip_time,td.trip_log,td.siteId  ) stca)  stca on td.trip_date=stca.trip_date  and td.trip_log=stca.trip_log and stca.siteId=td.siteId  left join  (  select trip_date,  rtmCount, trip_log  , siteId from (  select  td.trip_date, count(td.id) rtmCount  ,td.trip_log,td.siteId   from trip_details td   join vendor_trip_sheet_parent vtp on td.id=vtp.tripId  where  (td.status='saved' or td.status='saveedit' or td.status='addsave') and    approvalStatus='Rejected by Transport Manager'   group by td.trip_date, td.trip_time,td.trip_log, td.siteId ) rtm ) rtm on td.trip_date=rtm.trip_date   and td.trip_log=rtm.trip_log and  rtm.siteId=td.siteId  left join ( select trip_date,  stmaCount, trip_log, siteId   from ( select  td.trip_date, count(td.id) stmaCount ,  td.trip_log,td.siteId from trip_details td   join vendor_trip_sheet_parent vtp on td.id=vtp.tripId  where  (td.status='saved' or td.status='saveedit' or td.status='addsave') and       approvalStatus='Sent for TM approval'   group by td.trip_date, td.trip_time,td.trip_log, td.siteId  ) stma ) stma on td.trip_date = stma.trip_date    and stma.trip_log=td.trip_log and stma.siteId=td.siteId left join (  select  trip_date, atmCount,trip_time,trip_log , siteId from  ( select  td.trip_date, count(td.id) atmCount, td.trip_time,td.trip_log,td.siteId   from trip_details td   join vendor_trip_sheet_parent vtp on td.id=vtp.tripId  where  (td.status='saved' or td.status='saveedit' or td.status='addsave') and      approvalStatus='Approved by Transport Manager'    group by td.trip_date , td.trip_time,td.trip_log, td.siteId ) atm )atm on td.trip_date= atm.trip_date and td.trip_time=atm.trip_time and atm.trip_log=td.trip_log and atm.siteId=td.siteId   where td.siteId="+siteID+" and   td.trip_log='"+shiftLog+"' "+"  and td.trip_date between '"+fromDate+"' and '"+toDate+"' ";
	 System.out.println(" Shift Log :" + shiftLog + " , time " + shiftTime);
	 if(shiftLog.equalsIgnoreCase("ALL")&&shiftTime.equalsIgnoreCase("ALL"))
	 {
		 query=queryNill;
	 }else if(shiftTime.equalsIgnoreCase("ALL"))
	 {
		 query=queryWithLog;
	 }else
	 {
		 query=queryWithLogTimeAndLogType;
	 }
			 
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
System.out.println(query);
			rs = stmt.executeQuery(query);

			while (rs.next()) {

				TripBucketDto dto= new TripBucketDto();
				dto.setSiteId(siteID);
				dto.setTripDate(rs.getDate("trip_date"));
				dto.setTripLog(rs.getString("trip_log"));
				dto.setTripTime(rs.getString("trip_time"));
				dto.setOpenCount(rs.getInt("openCount"));
				dto.setRtcCount(rs.getInt("rtcCount"));
				dto.setRtmCount(rs.getInt("rtmCount"));
				dto.setAtmCount(rs.getInt("atmCount"));
				dto.setStcaCount(rs.getInt("stcaCount"));
				dto.setStmaCount(rs.getInt("stmaCount"));
				
System.out.println("Sent for tc cvount"+dto.getStcaCount());

				scheduleList.add(dto);
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
