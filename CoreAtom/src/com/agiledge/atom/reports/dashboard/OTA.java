package com.agiledge.atom.reports.dashboard;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import com.agiledge.atom.dbConnect.DbConnect;


public class OTA {
		public float getOTA(int month,int year)
		{
			DbConnect ob = DbConnect.getInstance();
			Connection con = null;
			Statement st=null;
			ResultSet rs=null;
			float returnInt=0,total=0,ontime=0;
			String query="select onTimeCount,total from (select onTime.monthNo, onTime.month, ontime.site,onTime.logType, ifnull(onTime.onTimeCount ,0)  onTimeCount, ifnull( offTime.offTimeCount,0) offTimeCount, ifnull(onTimeCount,0)+ ifnull(offTimeCount,0) total from ( select datepart(month, td.trip_date) monthNo, datename(month, td.trip_date) + ' ' + convert(varchar,datepart(year,td.trip_date)) month, site.site_name site,   count(vtp.tripId) onTimeCount, td.trip_log logType  from vendor_trip_sheet_parent vtp,   trip_details td, site  where  vtp.tripId=td.id and onTimeStatus='On Time' and site.id=td.siteId   	group by datepart(month, td.trip_date), datename(month, td.trip_date) + ' ' + convert(varchar,datepart(year,td.trip_date))  ,site.site_name, td.trip_log 	) as onTime 	left join ( select  datename(month, td.trip_date) + ' ' + convert(varchar,datepart(year,td.trip_date)) month, site.site_name site, count(vtp.tripId) offTimeCount, td.trip_log logType    from vendor_trip_sheet_parent vtp,   trip_details td, site    where  vtp.tripId=td.id   and  onTimeStatus='Not On Time'  and site.id=td.siteId     	group by datename(month, td.trip_date) + ' ' + convert(varchar,datepart(year,td.trip_date))  ,site.site_name, td.trip_log 	) as offTime on onTime.month=offTime.month and onTime.logType=offTime.logType and onTime.site=offTime.site  where monthNo="+month+" and onTime.month like '%"+year+"' and onTime.logType='IN')s";
			try{
				con=ob.connectDB();
				st=con.createStatement();
				rs=st.executeQuery(query);
				while(rs.next())
				{
					total=rs.getInt(2);
					ontime=rs.getInt(1);
				}
				if(total!=0)
				{
			    float ans=(ontime/total);
				returnInt=ontime/total*100;
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



