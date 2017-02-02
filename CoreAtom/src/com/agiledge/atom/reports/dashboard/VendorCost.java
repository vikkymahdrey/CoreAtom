package com.agiledge.atom.reports.dashboard;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import com.agiledge.atom.dbConnect.DbConnect;


public class VendorCost {
	public int getVendorCost(int month,int year)
	{
		DbConnect ob = DbConnect.getInstance();
		Connection con = null;
		Statement st=null;
		ResultSet rs=null;
		int returnInt=0;
		String query="select sum(ifnull(TotalCost,0)) from(select * from (    select  datepart(month,td.trip_date) monthNo1, datename(month,td.trip_date) + ' ' +convert(varchar, DATEPART  (year, td.trip_date)) as month1, count (*) actualTrips,vt.type type1, 'With Escort' escortType, site.site_name site_name1, site.id siteId, vt.ratePerTrip*2 perTripCost1  from trip_details td, vehicle_type vt, site  where  vehicle_type=vt.id and site.id=td.siteId   and DATEPART(year,td.trip_date)="+year+"  group by datepart(month,trip_date),  datename(month,trip_date) + ' ' +convert(varchar,   DATEPART(year,trip_date)), site.site_name, site.id   , vt.type, vt.ratePerTrip   ) as actualTrip   left join   ( select  datepart(month,td.trip_date) monthNo, datename(month,td.trip_date) + ' ' +convert(varchar, DATEPART(year,td.trip_date)) as   month, count(*) submittedTrips, vt.type type2, site.site_name site_name2 from trip_details td,   vendor_trip_sheet_parent vtp, vehicle_type vt, site where   td.id=vtp.tripId and   vehicle_type=vt.id  and site.id=td.siteId      and DATEPART(year,td.trip_date)="+year+"    group by datepart(month,trip_date),   datename(month,td.trip_date) + ' ' +convert(varchar, DATEPART(year,td.trip_date)), site.site_name    , vt.type   ) submittedTrip   on actualTrip.month1=submittedTrip.month and    actualTrip.type1=submittedTrip.type2 and actualTrip.site_name1=submittedTrip.site_name2    left join  (    select   datepart(month,td.trip_date) monthNo2, datename(month,td.trip_date) + ' ' +   convert(varchar, DATEPART(year,td.trip_date)) as month2 , count(*) approvedTrips, vt.type,   2*count(*)* vt.ratePerTrip as TotalCost, vt.ratePerTrip*2 perTripCost, site.site_name from   trip_details td, vendor_trip_sheet_parent vtp, vehicle_type vt, site where vtp.escort='YES' and  (vtp.approvalStatus='Approved by Transport Admin' or   vtp.approvalStatus='Approved by Transport Manager' )  and td.id=vtp.tripId and vehicle_type=vt.id   and site.id=td.siteId     and DATEPART(year,td.trip_date)="+year+"   group by datepart(month,trip_date), datename(month,  td.trip_date) + ' ' +convert(varchar, DATEPART(year,td.trip_date)) , site.site_name , vt.type,  vt.ratePerTrip ) approvedTrip    on    actualTrip.month1=approvedTrip.month2 and   actualTrip.type1=approvedTrip.type  and actualTrip.site_name1=approvedTrip.site_name   UNION    select * from (    select  datepart(month,td.trip_date) monthNo, datename(month,td.trip_date) + ' ' +convert(varchar, DATEPART  (year, td.trip_date)) as month1,count (*) actualTrips,vt.type type1, 'Without Escort' escortType , site.site_name site_name1, site.id siteId, vt.ratePerTrip perTripCost1  from trip_details td, vehicle_type vt, site  where  vehicle_type=vt.id and site.id=td.siteId    and DATEPART(year,td.trip_date)="+year+"   group by datepart(month,trip_date), datename(month,trip_date) + ' ' +convert(varchar,   DATEPART(year,trip_date)), site.site_name, site.id   , vt.type , vt.ratePerTrip  ) as actualTrip   left join     ( select  datepart(month,td.trip_date) monthNo, datename(month,td.trip_date) + ' ' +convert(varchar, DATEPART(year,td.trip_date)) as   month, count(*) submittedTrips, vt.type, site.site_name site_name2 from trip_details td,   vendor_trip_sheet_parent vtp, vehicle_type vt, site where   td.id=vtp.tripId and   vehicle_type=vt.id  and site.id=td.siteId      and DATEPART(year,td.trip_date)="+year+"   group by  datepart(month,trip_date),  datename(month,td.trip_date) + ' ' +convert(varchar, DATEPART(year,td.trip_date)), site.site_name    , vt.type   ) submittedTrip on actualTrip.month1=submittedTrip.month and   actualTrip.type1=submittedTrip.type and actualTrip.site_name1=submittedTrip.site_name2    	 left join  (    select  datepart(month,td.trip_date) monthNo2, datename(month,td.trip_date) + ' ' +   convert(varchar, DATEPART(year,td.trip_date)) as month2 , count(*) approvedTrips, vt.type,  count(*)* vt.ratePerTrip as TotalCost, vt.ratePerTrip perTripCost, site.site_name from   trip_details td, vendor_trip_sheet_parent vtp, vehicle_type vt, site where vtp.escort='NO' and  (vtp.approvalStatus='Approved by Transport Admin' or   vtp.approvalStatus='Approved by Transport Manager' )  and td.id=vtp.tripId and vehicle_type=vt.id   and site.id=td.siteId     and DATEPART(year,td.trip_date)="+year+"      group by  datepart(month,trip_date), datename(month,  td.trip_date) + ' ' +convert(varchar, DATEPART(year,td.trip_date)) , site.site_name , vt.type,  vt.ratePerTrip   ) approvedTrip    on    actualTrip.month1=approvedTrip.month2 and   actualTrip.type1=approvedTrip.type  and actualTrip.site_name1=approvedTrip.site_name )s  where monthNo1="+month+" group by s.month1";


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
