package com.agiledge.atom.reports.dashboard;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import com.agiledge.atom.dbConnect.DbConnect;


public class DailyCapacityUtilization {
	public int getPlanned(String date)
	{
		DbConnect ob = DbConnect.getInstance();
		Connection con = null;
		Statement st=null;
		ResultSet rs=null;
		int returnInt=0;
		String query="select sum(s.plannedCapacity) from (select site site, site_name, trip_date, vehicle_type, vehicleTypeName, sum(availableCapacity) availableCapacity, sum(plannedCapacity) plannedCapacity, sum(actualCapacity) actualCapacity  from  ( select a.site, site.site_name, a.trip_date, a.trip_time, a.trip_log, a.vehicle_type, vt.type vehicleTypeName, a.vehicleCount, a.availableCapacity,p.plannedCapacity, ifnull( p1.actualCapacity,0) actualCapacity  from ( select td.site,  td.trip_date, td.trip_time, td.trip_log, td.vehicle_type , count(*) vehicleCount,(select sit_cap from vehicle_type where id=td.vehicle_type) * count(*) availableCapacity from ( select distinct td.*, tc.site from trip_details td join  ( select tc.*,e.site  from trip_details_child tc join employee e on tc.employeeId=e.id)   tc on td.id=tc.tripId   ) td where ( td.status='saved' or td.status='addsave' or td.status='saveedit'   )  group by td.site, td.trip_date, td.trip_time, td.trip_log, td.vehicle_type   ) a join ( select  tc.site,  td.trip_date, td.trip_time, td.trip_log, td.vehicle_type  , count(*) plannedCapacity from trip_details td join   ( select tc.*,e.site  from trip_details_child tc join employee e on tc.employeeId=e.id)  tc on td.id=tc.tripId where ( td.status='saved' or td.status='addsave' or td.status='saveedit'   ) group by  tc.site,  td.trip_date, td.trip_time, td.trip_log, td.vehicle_type  ) p on a.site=p.site and  a.trip_date=p.trip_date and a.trip_time=p.trip_time and a.trip_log=p.trip_log and a.vehicle_type=p.vehicle_type   join vehicle_type vt on p.vehicle_type=vt.id join site  on p.site=site.id  left join (select  tc.site,  td.trip_date, td.trip_time, td.trip_log, td.vehicle_type, count(*) actualCapacity from (select * from vendor_trip_sheet_parent vtd join trip_details td on vtd.tripId=td.id  )td join ( select tc.*,e.site  from vendor_trip_sheet tc join employee e on tc.employeeId=e.id)    tc on td.id=tc.tripId where ( td.status='saved' or td.status='addsave' or td.status='saveedit'   ) and tc.showStatus='show' and td.approvalStatus='Sent for approval' group by  tc.site,  td.trip_date, td.trip_time, td.trip_log, td.vehicle_type  )   p1 on a.site=p1.site and a.trip_date=p1.trip_date and a.trip_time=p1.trip_time and a.trip_log=p1.trip_log and a.vehicle_type=p1.vehicle_type     where a.trip_date='"+date+"'  )summary group by site, site_name, trip_date, vehicle_type, vehicleTypeName)s";
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
	public int getActual(String date)
	{
		DbConnect ob = DbConnect.getInstance();
		Connection con = null;
		Statement st=null;
		ResultSet rs=null;
		int returnInt=0;
		String query="select sum(s.actualCapacity) from (select site site, site_name, trip_date, vehicle_type, vehicleTypeName, sum(availableCapacity) availableCapacity, sum(plannedCapacity) plannedCapacity, sum(actualCapacity) actualCapacity  from  ( select a.site, site.site_name, a.trip_date, a.trip_time, a.trip_log, a.vehicle_type, vt.type vehicleTypeName, a.vehicleCount, a.availableCapacity,p.plannedCapacity, ifnull( p1.actualCapacity,0) actualCapacity  from ( select td.site,  td.trip_date, td.trip_time, td.trip_log, td.vehicle_type , count(*) vehicleCount,(select sit_cap from vehicle_type where id=td.vehicle_type) * count(*) availableCapacity from ( select distinct td.*, tc.site from trip_details td join  ( select tc.*,e.site  from trip_details_child tc join employee e on tc.employeeId=e.id)   tc on td.id=tc.tripId   ) td where ( td.status='saved' or td.status='addsave' or td.status='saveedit'   )  group by td.site, td.trip_date, td.trip_time, td.trip_log, td.vehicle_type   ) a join ( select  tc.site,  td.trip_date, td.trip_time, td.trip_log, td.vehicle_type  , count(*) plannedCapacity from trip_details td join   ( select tc.*,e.site  from trip_details_child tc join employee e on tc.employeeId=e.id)  tc on td.id=tc.tripId where ( td.status='saved' or td.status='addsave' or td.status='saveedit'   ) group by  tc.site,  td.trip_date, td.trip_time, td.trip_log, td.vehicle_type  ) p on a.site=p.site and  a.trip_date=p.trip_date and a.trip_time=p.trip_time and a.trip_log=p.trip_log and a.vehicle_type=p.vehicle_type   join vehicle_type vt on p.vehicle_type=vt.id join site  on p.site=site.id  left join (select  tc.site,  td.trip_date, td.trip_time, td.trip_log, td.vehicle_type, count(*) actualCapacity from (select * from vendor_trip_sheet_parent vtd join trip_details td on vtd.tripId=td.id  )td join ( select tc.*,e.site  from vendor_trip_sheet tc join employee e on tc.employeeId=e.id)    tc on td.id=tc.tripId where ( td.status='saved' or td.status='addsave' or td.status='saveedit'   ) and tc.showStatus='show' and td.approvalStatus='Sent for approval' group by  tc.site,  td.trip_date, td.trip_time, td.trip_log, td.vehicle_type  )   p1 on a.site=p1.site and a.trip_date=p1.trip_date and a.trip_time=p1.trip_time and a.trip_log=p1.trip_log and a.vehicle_type=p1.vehicle_type     where a.trip_date='"+date+"'  )summary group by site, site_name, trip_date, vehicle_type, vehicleTypeName)s";
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
