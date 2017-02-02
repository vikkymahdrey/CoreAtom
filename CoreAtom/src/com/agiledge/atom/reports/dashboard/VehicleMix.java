package com.agiledge.atom.reports.dashboard;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import com.agiledge.atom.dbConnect.DbConnect;


public class VehicleMix {
	public int getVehicleMix(int month,int year,int vehicleid)
	{
		DbConnect ob = DbConnect.getInstance();
		Connection con = null;
		Statement st=null;
		ResultSet rs=null;
		int returnInt=0;
		String query="select avg(percentage) from (select vehicle_type, count(vehicle_type) totalVehicle, trip_date,(select count( k.vehicle_type) from trip_details k where td.trip_date=k.trip_date and (status='addsave' or  status='saveedit' or status='saved')) total,100* count(vehicle_type) /(select count( k.vehicle_type) from trip_details k where td.trip_date=k.trip_date  and (status='addsave' or  status='saveedit' or status='saved')) percentage , td.status from trip_details td  where    (td.status='addsave' or  td.status='saveedit' or td.status='saved' and DATEPART(MONTH,td.trip_date)="+month+" and DATEPART(YEAR,td.trip_date)="+year+") group by vehicle_type, trip_date, td.status)s where vehicle_type="+vehicleid;
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
	
	public int getDailyNoShow(String date,int vehicleid)
	{
		DbConnect ob = DbConnect.getInstance();
		Connection con = null;
		Statement st=null;
		ResultSet rs=null;
		int returnInt=0;
		String query="select avg(percentage) from (select vehicle_type, count(vehicle_type) totalVehicle, trip_date,(select count( k.vehicle_type) from trip_details k where td.trip_date=k.trip_date and (status='addsave' or  status='saveedit' or status='saved')) total,100* count(vehicle_type) /(select count( k.vehicle_type) from trip_details k where td.trip_date=k.trip_date  and (status='addsave' or  status='saveedit' or status='saved')) percentage , td.status from trip_details td  where    (td.status='addsave' or  td.status='saveedit' or td.status='saved' and td.trip_date='"+date+"') group by vehicle_type, trip_date, td.status)s where vehicle_type="+vehicleid;
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
