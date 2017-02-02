package com.agiledge.atom.reports.dashboard;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import com.agiledge.atom.dbConnect.DbConnect;


public class SiteCostSummary {
	public int getCostbySite(int month,int year,int site)
	{
		DbConnect ob = DbConnect.getInstance();
		Connection con = null;
		Statement st=null;
		ResultSet rs=null;
		int returnInt=0;
		String query=" declare @site int  declare @month varchar(10)  declare @year varchar(10)  declare @fromDate datetime declare @toDate datetime    set @month='"+month+"'   set @year='"+year+"'   set @site="+site+"             set @fromDate=    CONVERT(VARCHAR(25),DATEADD(dd,-(DAY( @year+'-'+@month+'-01' )-1),  @year+'-'+@month+'-01'),121)   set @toDate= CONVERT(VARCHAR(25),DATEADD(dd,-(DAY(DATEADD(mm,1,  @year+'-'+@month+'-01'))),DATEADD(mm,1, @year+'-'+@month+'-01')),101)                                     select sum(S.PROJECTCOST) FROM (  						   select alldata.cost/alldata.totalEmployee PERINSTANCE,  alldata.cost/alldata.totalEmployee*2 twoway,   (PROJECT_Emp.empCount*(alldata.cost/alldata.totalEmployee))  AS PROJECTCOST, PROJECT_Emp.id projectId,  PROJECT_Emp.empCount,PROJECT_Emp.trip_date,  PROJECT_Emp.projectCode,  project_emp.projectdescription, alldata.trip_log, alldata.trip_time  from    ( select sum(totals.totalNoOfTrips) as trips,   sum(totals.total) cost, totalEmployee.emps as totalEmployee,  totals.trip_time,  totals.trip_log from  (select  count(tdc.employeeId) as emps, td.trip_time, td.trip_log from trip_details_child tdc, trip_details td where td.id=tdc.tripId and  td.trip_date between @fromDate and @toDate  and td.siteId=@site group by td.trip_time, td.trip_log) as totalEmployee, ( select  count(*) totalNoOfTrips, td.trip_date, td.trip_time, td.trip_log,  ( (select top 1 ratePerTrip from site_vehicle_rate_calander where site=td.siteId and vehicle_type=td.vehicle_type and td.trip_date >=fromDate and td.trip_date<ifnull(toDate,curdate())) *(case when td.security_status='YES' then  2 else 1 end))*count(*) as total, vehicle_type,security_status, (select top 1 ratePerTrip from site_vehicle_rate_calander where site=td.siteId and vehicle_type=td.vehicle_type and td.trip_date >=fromDate and td.trip_date<ifnull(toDate,curdate()))  cost  from trip_details td, vehicle_type vht where vht.id=td.vehicle_type and td.trip_date between @fromDate and @toDate and td.siteId=@site  group by siteid, td.trip_date,vehicle_type,security_status,   td.trip_time,td.trip_log ) as totals  where totalEmployee.trip_log=totals.trip_log and totalEmployee.trip_time=totals.trip_time group by totalEmployee.emps, totals.trip_time,  totals.trip_log  ) as alldata,  (  select count( *) empCount, sch.project,td.trip_date,pr.project as projectCode, pr.description as projectdescription, pr.id, td.trip_time, td.trip_log from trip_details_child tdc ,trip_details td,   employee_schedule sch   ,atsconnect pr where td.id=tdc.tripId and  pr.id=sch.project and tdc.scheduleId=sch.id and     (td.trip_date between @fromDate and @toDate) and td.siteId=@site    group by sch.project,td.trip_date,pr.project,pr.description, pr.id, td.trip_time, td.trip_log ) as project_emp  where   alldata.trip_time=project_emp.trip_time and alldata.trip_log=project_emp.trip_log    						   ) s       ";
 

		try{
			con=ob.connectDB();
			st=con.createStatement();
			rs=st.executeQuery(query);
			while(rs.next())
			{
				returnInt=rs.getInt(1);
			}
		}catch (Exception e) {
			try {
				con.rollback();
			} catch (Exception ignore) {

			}
			e.printStackTrace();
		} finally {
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);

		}
		return returnInt;
	}
	

}


