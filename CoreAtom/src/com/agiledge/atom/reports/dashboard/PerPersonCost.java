package com.agiledge.atom.reports.dashboard;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import com.agiledge.atom.dbConnect.DbConnect;


public class PerPersonCost {
	public int getPerPersonCost(int month,int year)
	{
		DbConnect ob = DbConnect.getInstance();
		Connection con = null;
		Statement st=null;
		ResultSet rs=null;
		int returnInt=0;
		String query="select sum(s.PERINSTANCE)from (select alldata.cost/alldata.totalEmployee PERINSTANCE,  alldata.cost/alldata.totalEmployee*2 twoway,   (PROJECT_Emp.empCount*(alldata.cost/alldata.totalEmployee))  AS PROJECTCOST, PROJECT_Emp.id projectId,  PROJECT_Emp.empCount,PROJECT_Emp.trip_date,  PROJECT_Emp.projectCode,  project_emp.projectdescription, alldata.trip_log, alldata.trip_time  from    ( select sum(totals.totalNoOfTrips) as trips,   sum(totals.total) cost, totalEmployee.emps as totalEmployee,  totals.trip_time,  totals.trip_log from  (select  count(tdc.employeeId) as emps, td.trip_time, td.trip_log from trip_details_child tdc, trip_details td where td.id=tdc.tripId and  DATEPART(MONTH, td.trip_date)="+month+" AND DATEPART(YEAR, td.trip_date)="+year+"  group by td.trip_time, td.trip_log) as totalEmployee, ( select  count(*) totalNoOfTrips, td.trip_date, td.trip_time, td.trip_log,  (vht.ratePerTrip*(case when td.security_status='YES' then  2 else 1 end))*count(*) as total, vehicle_type,security_status,vht.ratePerTrip cost  from trip_details td, vehicle_type vht where vht.id=td.vehicle_type and  DATEPART(MONTH, td.trip_date)="+month+" AND DATEPART(YEAR, td.trip_date)="+year+"  group by siteid, td.trip_date,vehicle_type,security_status,vht.ratePerTrip,td.trip_time,td.trip_log ) as totals  where totalEmployee.trip_log=totals.trip_log and totalEmployee.trip_time=totals.trip_time group by totalEmployee.emps, totals.trip_time,  totals.trip_log  ) as alldata,  (  select count( *) empCount, sch.project,td.trip_date,pr.project as projectCode, pr.description as projectdescription, pr.id, td.trip_time, td.trip_log from trip_details_child tdc ,trip_details td,   employee_schedule sch   ,atsconnect pr where td.id=tdc.tripId and  pr.id=sch.project and tdc.scheduleId=sch.id and     ( DATEPART(MONTH, td.trip_date)="+month+" AND DATEPART(YEAR, td.trip_date)="+year+"  )    group by sch.project,td.trip_date,pr.project,pr.description, pr.id, td.trip_time, td.trip_log ) as project_emp  where   alldata.trip_time=project_emp.trip_time and alldata.trip_log=project_emp.trip_log) s";


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
