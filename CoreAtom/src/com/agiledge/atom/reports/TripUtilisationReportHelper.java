package com.agiledge.atom.reports;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import com.agiledge.atom.commons.OtherFunctions;
import com.agiledge.atom.dao.TripDetailsDao;
import com.agiledge.atom.dbConnect.DbConnect;
import com.agiledge.atom.dto.TripDetailsChildDto;
import com.agiledge.atom.dto.TripDetailsDto;
import com.agiledge.atom.reports.dto.TripUtilisationReportDto;

public class TripUtilisationReportHelper {
	
	public ArrayList<TripUtilisationReportDto> getTripUtilisationReport(String  site, String fromDate, String toDate, String tripMode, String tripTimes[])
	
	{
		ArrayList<TripUtilisationReportDto> dtoList=null;
		Connection con=null;
		PreparedStatement st=null;
		ResultSet rs=null;
	
 
		
		try {
		
			fromDate = OtherFunctions.changeDateFromatToIso(fromDate);
			toDate = OtherFunctions.changeDateFromatToIso(toDate);
			DbConnect db = DbConnect.getInstance();
			String siteIdQuery=" siteId=" + site;
			con = db.connectDB();
			String query="";
			 if(tripTimes!=null) {
				 System.out.println("tripTimes length " +tripTimes);
			 }else
			 {
				 System.out.println(" Trip time is null");
			 }
					 
			 String timeQuery="";
			 String logQuery="";
			 String siteQuery="";
			 if(!site.equalsIgnoreCase("ALL"))
			 siteQuery=" and td.siteId="+site;
 
		
			if(tripTimes[0].equalsIgnoreCase("All")&&tripMode.equalsIgnoreCase("All")) {				
				System.out.println("params in function :"+ site+" "+fromDate+" "+toDate+" "+tripMode );
			} else if (tripTimes[0].equalsIgnoreCase("all")) {
				logQuery=" and td.trip_log='"+tripMode+"' ";
				 
			} else {
				 logQuery = " and td.trip_log='"+ tripMode + "' ";
				timeQuery=" and td.trip_time in (";
				int i=0;
				for(String time: tripTimes)
				{
					if(i++>0)
					{
						timeQuery+=",";
					}
					timeQuery+="'"+time+"'";
				}
				 		
				if(i<1) {
					timeQuery="";
				} else {
					timeQuery+=") ";
					
				}
			}
			 query=" /* total trips starts*/ select total.trip_date, totalCount, ifnull(downloaded.downloadedCount,0) downloadedCount,ifnull(initial.initialCount,0) initialCount,ifnull(started.startedCount,0) startedCount, ifnull(stopped.stoppedCount,0) stoppedCount  from ( select  trip_date, count(*) totalCount from trip_details td where   td.status in ('saved','addsave','saveedit')  and  trip_date between '"+
			fromDate+"' and '"+toDate+"' "+timeQuery+logQuery+siteQuery+"  group by td.trip_date ) total /* totalTrips ends*/ left join  ( /* downloaded trips starts */ select trip_date, downloadedCount from ( select  trip_date, count(*) downloadedCount  from trip_details td join vendor_trip_sheet_parent vtp on td.id=vtp.tripId where vtp.downloadStatus='downloaded'  "+timeQuery+logQuery+siteQuery+"  group by trip_date   ) downloaded ) downloaded /* downloaded trips ends */ on total.trip_date=downloaded.trip_date left join  (   /* initial trips starts */ select trip_date, initialCount from ( select  trip_date, count(*) initialCount  from trip_details td join vendor_trip_sheet_parent vtp on td.id=vtp.tripId where vtp.downloadStatus='downloaded' and vtp.status='initial'  "+timeQuery+logQuery+siteQuery+"  group by trip_date   ) initial /* started trips ends */  ) initial on total.trip_date=initial.trip_date left join  (   /* started trips starts */ select trip_date, startedCount from ( select  trip_date, count(*) startedCount  from trip_details td join vendor_trip_sheet_parent vtp on td.id=vtp.tripId where vtp.downloadStatus='downloaded' and  vtp.status='started'  "+timeQuery+logQuery+siteQuery+"  group by trip_date   ) started /* started trips ends */  ) started on total.trip_date=started.trip_date left join ( /* stopped trips starts */ select trip_date, stoppedCount from ( select  trip_date, count(*) stoppedCount  from trip_details td join vendor_trip_sheet_parent vtp on td.id=vtp.tripId where vtp.downloadStatus='downloaded' and  vtp.status='stopped'   "+timeQuery+logQuery+siteQuery+"   group by trip_date   ) downloaded /* stopped trips ends */  ) stopped on total.trip_date= stopped.trip_date  ";
 
			System.out.println(query);
			st = con.prepareStatement(query);
			 
			rs = st.executeQuery();
			TripDetailsDao tripDao = new TripDetailsDao();
			while(rs.next())
			{
				if(rs.isFirst())
				{
					dtoList = new  ArrayList<TripUtilisationReportDto>();
				}
				TripUtilisationReportDto dto = new TripUtilisationReportDto();
				
				dto.setDate(rs.getDate("trip_date"));
				dto.setTotalCount(rs.getInt("totalCount"));
				dto.setDownloadedCount(rs.getInt("downloadedCount"));
				dto.setInitialCount(rs.getInt("initialCount"));
				dto.setStartedCount(rs.getInt("startedCount"));
				dto.setStoppedCount(rs.getInt("stoppedCount"));
				dto.setPendingCount(dto.getTotalCount()-dto.getDownloadedCount());
				if(dto.getDownloadedCount()!=0 && dto.getTotalCount()!=0)
				{
				float sum=(float)dto.getDownloadedCount()/dto.getTotalCount();
				System.out.println("sum"+sum);
				float per=sum*100;
				System.out.println("perc"+per);
				dto.setUsage((int)per);
				}
				
				
				dto.setTotalTrips(tripDao.totalTripStatus(OtherFunctions.changeDateFromat(dto.getDate()), tripTimes, tripMode, "total" ));
				dto.setNotDownloaded(tripDao.totalTripStatus(OtherFunctions.changeDateFromat(dto.getDate()), tripTimes, tripMode, "not downloaded" ));
				 
				 
				dto.setStartedTrips(tripDao.liveTripStatusByTrips_triptimesAndStatus
						(OtherFunctions.changeDateFromat(dto.getDate()), tripTimes, tripMode, "started"));
				dto.setDownloadedTrips(tripDao.liveTripStatusByTrips_triptimesAndStatus
						(OtherFunctions.changeDateFromat(dto.getDate()), tripTimes, tripMode, "downloaded"));
				dto.setInitialTrips(tripDao.liveTripStatusByTrips_triptimesAndStatus
						(OtherFunctions.changeDateFromat(dto.getDate()), tripTimes, tripMode, "initial"));
				
				dto.setStoppedTrips(tripDao.liveTripStatusByTrips_triptimesAndStatus
						(OtherFunctions.changeDateFromat(dto.getDate()), tripTimes, tripMode, "stopped"));
				dtoList.add(dto);
				
			}
			
		}catch(Exception e)
		{
			System.out.println("Error in ProjectUtilisationReport "+ e);
		}
		finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);
			
		}
		return dtoList;
	}
	
	public ArrayList<TripDetailsDto> getTripsWithVehicle(String branch,String shift,String regNo) {
		DbConnect ob = DbConnect.getInstance();
		PreparedStatement pst = null;
		PreparedStatement pst1 = null;
		ResultSet rs = null;
		ResultSet rs1 = null;
		TripDetailsDto tripDto = null;
		TripDetailsChildDto childDto = null;
		Connection con = ob.connectDB();
		ArrayList<TripDetailsDto> tripDtailsDtos = new ArrayList<TripDetailsDto>();
		ArrayList<TripDetailsChildDto> childDtos = null;
		try {
			String filterquery="";
			if(!shift.equalsIgnoreCase("ALL")&&regNo.equalsIgnoreCase("ALL"))
			{
				System.out.println("here shift");
				filterquery=" and td.trip_log='"+shift+"'";
			}
			else if(shift.equalsIgnoreCase("ALL")&&!regNo.equalsIgnoreCase("ALL"))
			{
				System.out.println("here regNo");
				filterquery=" and v.regNo='"+regNo+"'";
			}
			else if(!shift.equalsIgnoreCase("ALL")&&!regNo.equalsIgnoreCase("ALL"))
			{
				System.out.println("here regNo");
				filterquery=" and v.regNo='"+regNo+"' and td.trip_log='"+shift+"'";
			}
			
			String query = "select vp.vehicleId,vp.date_time,vp.lattitude,vp.longitude,case when timediff(now(),vp.date_time)>'00:10:00' and vp.logstatus!='danger'  then 'outofreach' else vp.logstatus  end as logstatus,vp.tripId,v.regNo,td.trip_code,td.trip_log,vtsp.escort,td.siteid,td.trip_code,(select count(*)  from vendor_trip_sheet vts,employee e where vts.tripId=td.id and  e.id=vts.employeeId and e.gender='F' and vts.curStatus='IN') as  ladyInCount,(select count(*)  from vendor_trip_sheet  vts where  vts.tripId=td.id) as empCount,(select count(*)  from vendor_trip_sheet  vts where  vts.tripId=td.id and vts.curStatus='IN') as empInCount, ifnull(pa.acknowledgeBy,'notstop') as acknowledgeBy from vehicles v,vehicle_position vp, (select vp.vehicleId,max(vp.date_time) as date_time from vehicle_position vp group by tripid,vehicleId) as  tg,site s,trip_details td left  outer join  panicaction pa on td.id=pa.tripId join vendor_trip_sheet_parent vtsp on td.id=vtsp.tripid where td.trip_date between  DATE_ADD(CURDATE(),INTERVAL -1 DAY) and curdate() and tg.vehicleId=vp.vehicleId and td.id=vp.tripId and td.siteid=s.id and s.branch="
					+ branch
					+ " and tg.date_time=vp.date_time and  vp.vehicleId=v.id and vp.status='checkedin' and vp.tripid in (select tripid from vendor_trip_sheet_parent where status='started')"+filterquery+"  group by v.id order by vp.date_time desc";
		/*	String query = "select   vp.vehicleId,vp.date_time,vp.lattitude,vp.longitude,case when (timediff(now(),vp.date_time)>'00:10:00' and vp.logstatus!='danger') then 'outofreach' else vp.logstatus  end as logstatus,vp.tripId,v.regNo,td.trip_code,td.trip_log,vtsp.escort,td.siteid,td.trip_code,(select count(*)  from vendor_trip_sheet vts,employee e where vts.tripId=td.id and  e.id=vts.employeeId and e.gender='F' and vts.curStatus='IN') as  ladyInCount,(select count(*)  from vendor_trip_sheet  vts where  vts.tripId=td.id and vts.curStatus='IN') as empInCount,(select count(*)  from vendor_trip_sheet  vts where  vts.tripId=td.id) as empCount,  ifnull(pa.acknowledgeBy,'notstop') as acknowledgeBy from vehicles v,vehicle_position vp, (select dv.vehicleId, max( fvp.date_time ) date_time from full_vehicle_position  fvp join device_vehicle dv on fvp.dvId=dv.id group by dv.vehicleId) rp,   (select vp.vehicleId,max(vp.date_time) as date_time from vehicle_position vp group by tripid,vehicleId) as  tg,site s,trip_details td left  outer join  panicaction pa on td.id=pa.tripId join vendor_trip_sheet_parent vtsp on td.id=vtsp.tripid where  rp.vehicleId=tg.vehicleId and vp.date_time >=rp.date_time and tg.vehicleId=vp.vehicleId and td.id=vp.tripId and td.siteid=s.id" +
					" and s.branch=" + branch +
					" and tg.date_time=vp.date_time and  vp.vehicleId=v.id and vp.status='checkedin' and vp.tripid in (select tripid from vendor_trip_sheet_parent where status='started')  group by v.id order by vp.date_time desc";
					*/
			pst = con
					.prepareStatement( query);
			 System.out.println(query);
			
			rs=pst.executeQuery();
			while (rs.next()) {
				
				tripDto = new TripDetailsDto();
			 
				tripDto.setVehicle(rs.getString("vehicleid"));
			 tripDto.setTrip_date(rs.getString("date_time"));
				tripDto.setId(rs.getString("tripId"));
				tripDto.setTrip_code(rs.getString("trip_code"));				
				tripDto.setVehicleNo(rs.getString("regNo"));			 
				tripDto.setLatitude(rs.getString("lattitude"));			 
				tripDto.setLongitude(rs.getString("longitude"));			 
				tripDto.setStatus(rs.getString("logstatus"));			 
				tripDto.setVehicleNo(rs.getString("regNo"));				 
				tripDto.setTrip_code(rs.getString("trip_code"));				 
				tripDto.setTrip_log(rs.getString("trip_log"));				 
				tripDto.setIsSecurity(rs.getString("escort"));
				tripDto.setEmpCount(rs.getInt("empCount"));
				tripDto.setLadyInCount(rs.getInt("ladyInCount"));
				tripDto.setEmpInCount(rs.getInt("empInCount"));
				 
				tripDto.setPanicAck(rs.getString("acknowledgeBy"));
			
				String actq="";
				String nuquery = " select l.latitude,l.longitude,tdc.routedOrder as pos from landmark l,trip_details_child tdc where tdc.tripId="
						+ tripDto.getId() + " and  tdc.landmarkid=l.id";
				if(tripDto.getTrip_log().equals("IN"))
				
				{
				actq="  select l.latitude,l.longitude,100 as pos from landmark l,site s where  s.id="+rs.getString("siteid")+" and s.landmark=l.id"; 
				}
				else 
				{
					actq="  select l.latitude,l.longitude,0 as pos from landmark l,site s where  s.id="+rs.getString("siteid")+" and s.landmark=l.id";	
				}				
//				System.out.println("select * from ("+nuquery+" union "+actq+") as tble order by pos");
				pst1 = con.prepareStatement("select * from ("+nuquery+" union "+actq+") as tble order by pos");
				rs1 = pst1.executeQuery();
				childDtos = new ArrayList<TripDetailsChildDto>();
				while (rs1.next()) {
					 
					childDto = new TripDetailsChildDto();
					childDto.setLatitude(rs1.getString("latitude"));
					childDto.setLongitude(rs1.getString("longitude"));
					childDtos.add(childDto);

				}
				tripDto.setTripDetailsChildDtoList(childDtos);

				tripDtailsDtos.add(tripDto);
			}

		} catch (Exception e) {
			System.out.println("Error" + e);
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeResultSet(rs1);
			DbConnect.closeStatement(pst);
			DbConnect.closeStatement(pst1);
			DbConnect.closeConnection(con);
		}
		return tripDtailsDtos;
	}

}
