package com.agiledge.atom.reports.dashboard;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import com.agiledge.atom.dbConnect.DbConnect;


public class ProjectwiseNoShow {
		public int getProjectwise(int month,int year,int projectid)
		{
			DbConnect ob = DbConnect.getInstance();
			Connection con = null;
			Statement st=null;
			ResultSet rs=null;
			int returnInt=0;
			String query="select count(EmployeeId) from (select e.PersonnelNo as EmployeeID, e.displayname as EmployeeName,  td.trip_log, td.trip_time, convert(varchar,td.trip_date,103)    trip_date, proj.description  as ProjectName,  scheduledByName, ifnull( vts.noShowReason,'') noShowReason from    vendor_trip_sheet vts,employee e,trip_details td,    employee_subscription esub,   employee_schedule es,atsconnect proj,      ( select id scheduledById, displayName scheduledByName from employee ) scheduledBy    where vts.showStatus='No Show' and vts.employeeId= e.id    and es.scheduledBy=scheduledBy.scheduledById     and vts.tripId=td.id  and  esub.empID=vts.employeeId     and  es.subscription_id=esub.subscriptionID  and  td.trip_date between es.from_date and ifnull(es.cancel_date,es.to_date) and proj.id=es.project  and   DATEPART(MONTH,td.trip_date)="+month+" and DATEPART(year,td.trip_date)="+year+" and proj.id="+projectid+")s";
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
