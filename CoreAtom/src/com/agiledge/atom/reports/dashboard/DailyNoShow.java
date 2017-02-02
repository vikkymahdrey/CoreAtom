package com.agiledge.atom.reports.dashboard;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import com.agiledge.atom.dbConnect.DbConnect;


public class DailyNoShow {
	public int getNoShow(String date)
	{
		DbConnect ob = DbConnect.getInstance();
		Connection con = null;
		Statement st=null;
		ResultSet rs=null;
		int returnInt=0;
		String query="select sum(count) from (select  datename(month, td.trip_date) + ' ' + convert(varchar,datepart(year,td.trip_date)) month,  proj.description ProjectName, proj.id projectId,   count(*) count  from vendor_trip_sheet vts,employee e,employee spoc,employee sup,trip_details td,employee_subscription esub,   employee_schedule es,atsconnect proj, site  where vts.showStatus='No Show' and vts.employeeId= e.id and vts.tripId=td.id  and esub.empID=vts.employeeId and esub.spoc=spoc.id and esub.supervisor=sup.id  and es.subscription_id=esub.subscriptionID and td.trip_date between es.from_date and ifnull(es.cancel_date,es.to_date) and proj.id=es.project and site.id=e.site   and td.trip_date ="+date+" and siteId=1 group by datename(month, td.trip_date) + ' ' + convert(varchar,datepart(year,td.trip_date)),  proj.id, proj.description)s";
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
