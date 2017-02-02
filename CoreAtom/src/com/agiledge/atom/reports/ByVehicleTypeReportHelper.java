package com.agiledge.atom.reports;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import com.agiledge.atom.commons.OtherFunctions;
import com.agiledge.atom.dbConnect.DbConnect;
import com.agiledge.atom.dto.TripDetailsDto;
import com.agiledge.atom.reports.dto.ByVehicleTypeReportDto;

public class ByVehicleTypeReportHelper {
	
	public List<ByVehicleTypeReportDto> getByVehicleTypeReport(String fromDate, String toDate, String option, String site, String vendorId, String vehicleType)
	{
		 System.out.println("vendor in method : "+ vehicleType);
		 
		 
		 	//vendorId="1";
 
			DbConnect dbConn = DbConnect.getInstance();
			Connection con = null;
			Statement stmt = null;
			ResultSet rs = null;
			
			fromDate=OtherFunctions.changeDateFromatToIso(fromDate);
			toDate = OtherFunctions.changeDateFromatToIso(toDate);
			vehicleType=vehicleType==null?"":vehicleType;
			vendorId=vendorId==null?"":vendorId;					
			fromDate=fromDate==null?"":fromDate;
			toDate=toDate==null?"":toDate;
			option=option==null?"":option;
			site=site==null?"":site;
			List<ByVehicleTypeReportDto> dtoList = new ArrayList<ByVehicleTypeReportDto>();
			
				
			try {
				if(!fromDate.equals("")&&!toDate.equals("")&&!site.equals("")) {
				String query="";
				String  optionTitle="";
				String vehicleQuery="";
				if(vehicleType.equals("")==false&&vehicleType.trim().equalsIgnoreCase("all")==false) {
					vehicleQuery= " and vt.id="+ vehicleType;
				}
				String vendorQuery = " where vendorId=" + vendorId;
				String optionlessQuery="select  vtp.siteId,vtp.trip_date, vt. type vehicle_type, '' optionLabel,   vtp.count, vtp.totalDistance, vtp.averageTime, vtp.totalOnTimeCount, td.minCount, td.maxCount  from (   select trip_date, siteId, vehicle_type, count(*) count,  sum(vtp.distanceCovered)  totalDistance, avg(timeElapsed) averageTime, sum(onTimeCount )   totalOnTimeCount  	from (  select vtp.tripId, td.siteId, td.vehicle_type, td.trip_date,   ifnull(vtp.distanceCovered,0) distanceCovered, ifnull(vtp.timeElapsed,0) timeElapsed,  ifnull(onTimeCount,0)onTimeCount  	from  vendor_trip_sheet_parent vtp  join     (   select td.* from trip_details td join tripvendorassign tva on td.id=tva.tripId   where vendorId=1 )  td     on vtp.tripId=td.id  	left join ( select tripId, count(*) onTimeCount from vendor_trip_sheet_parent where onTimeStatus='On Time' group by tripId ) vts on vtp.tripId=vts.tripId  )  vtp   group by vtp.vehicle_type, trip_date, siteId ) vtp join  ( select   siteId,vehicle_type, trip_date, max(count) maxCount, min(count) minCount from ( select td.siteId, td.vehicle,   td.vehicle_type, td.trip_date,  count(*) count  from   (   select td.* from trip_details td join tripvendorassign tva on td.id=tva.tripId  "+vendorQuery+" )  td where td.vehicle is   not null group by td.vehicle,td.vehicle_type, trip_date, siteId ) td group by vehicle_type, trip_date  )   td on vtp.vehicle_type=td.vehicle_type and td.trip_date=vtp.trip_date and vtp.siteId=td.siteId join vehicle_type vt on vtp.vehicle_type=vt.id   where  vtp.trip_date between '"+fromDate+"' and '"+toDate+"'  and vtp.siteId="+site+vehicleQuery;
				String timeSlotQuery="select  vtp.siteId,vtp.trip_date, vt. type vehicle_type, concat(startTime,concat( ' - ',  endTime)) optionLabel,   vtp.count, vtp.totalDistance, vtp.averageTime, vtp.totalOnTimeCount, td.minCount, td.maxCount from ( select trip_date, siteId, vehicle_type, startTime, endTime, count(*) count,  sum(vtp.distanceCovered)  totalDistance, avg(timeElapsed) averageTime, sum(onTimeCount )   totalOnTimeCount  from (  select vtp.tripId, td.siteId, td.vehicle_type, td.trip_date,   td.startTime,td.endTime ,  ifnull(vtp.distanceCovered,0) distanceCovered, ifnull  (vtp.timeElapsed,0) timeElapsed,  ifnull(onTimeCount,0)onTimeCount  from  vendor_trip_sheet_parent vtp  join   ( SELECT td.*,   ts.startTime,ts.endTime FROM  timesloat ts join  (   select td.* from trip_details td join tripvendorassign tva on td.id=tva.tripId  "+vendorQuery+" )  td on td.trip_time between  ts.startTime and ts.endTime)   td on vtp.tripId=td.id  left join ( select tripId, count(*) onTimeCount from vendor_trip_sheet_parent where onTimeStatus='On Time' group by tripId ) vts on vtp.tripId=vts.tripId  )  vtp   group by vtp.vehicle_type, trip_date, siteId, startTime, endTime ) vtp join  ( select   siteId,vehicle_type, trip_date, max(count) maxCount, min(count) minCount from ( select td.siteId, td.vehicle,   td.vehicle_type, td.trip_date,  count(*) count  from   (   select td.* from trip_details td join tripvendorassign tva on td.id=tva.tripId "+vendorQuery+"  )  td where td.vehicle is   not null group by td.vehicle,td.vehicle_type, trip_date, siteId ) td group by vehicle_type, trip_date  )   td on vtp.vehicle_type=td.vehicle_type and td.trip_date=vtp.trip_date and vtp.siteId=td.siteId join vehicle_type vt on vtp.vehicle_type=vt.id   "+
						"where vtp.trip_date between '"+fromDate+"' and '"+toDate+"' and vtp.siteId="+ site+vehicleQuery;
				String distanceGridQuery="select  vtp.siteId, vtp.trip_date, vtp.vehicle_type, startKm, endKm, concat(cast(startKm as char),concat( ' - ', cast(  endKm as char))) optionLabel,  vtp.count, vtp.totalDistance, vtp.averageTime, vtp.totalOnTimeCount, td.minCount, td.maxCount from ( select trip_date, siteId, vehicle_type, startKm, endKm, count(*) count, sum(vtp.distanceCovered)    totalDistance, avg(timeElapsed) averageTime, sum(onTimeCount ) totalOnTimeCount  from (   select vtp.tripId, td.siteId, td.vehicle_type, td.trip_date, td.startKm,td.endKm ,  ifnull    (vtp.distanceCovered,0) distanceCovered, ifnull(vtp.timeElapsed,0) timeElapsed,  ifnull  (onTimeCount,0)onTimeCount  from vendor_trip_sheet_parent vtp  join    ( SELECT td.*,  dg.startKm, dg.endKm FROM  distance_grid dg join    (  select td.*,    vtp.distanceCovered from trip_details td join vendor_trip_sheet_parent vtp on     td.id=vtp.tripId  )  td on td.distanceCovered between dg.startKm and dg.endKm  )    td on vtp.tripId=td.id  left join ( select tripId, count(*) onTimeCount from   vendor_trip_sheet_parent where onTimeStatus='On Time' group by tripId    ) vts  on       vtp.tripId=vts.tripId         ) vtp  group by  vtp.vehicle_type, trip_date, siteId, startKm, endKm ) vtp join  ( select siteId, vehicle_type, trip_date, max(count) maxCount, min(count) minCount from (   select td.siteId, td.vehicle, td.vehicle_type,    td.trip_date, count(*) count  from    (   select td.* from trip_details td join tripvendorassign tva on td.id=tva.tripId  "+vendorQuery+" )  td where td.vehicle is not null group by td.vehicle,td.vehicle_type, trip_date, td.siteId ) td group by vehicle_type, trip_date, siteId  ) td on    vtp.vehicle_type=td.vehicle_type and td.trip_date=vtp.trip_date and td.siteId= vtp.siteId  join vehicle_type vt on vtp.vehicle_type=vt.id       "+
						"where vtp.trip_date between '"+fromDate+"' and '"+toDate+"' and vtp.siteId="+ site+vehicleQuery;
				String timeOfTheDayQuery="select vtp.siteId, vtp.trip_date, vt.type vehicle_type, vtp.label optionLabel,  vtp.count, vtp.totalDistance, vtp.averageTime, vtp.totalOnTimeCount, td.minCount, td.maxCount from ( select siteId, trip_date, vehicle_type, label, count(*) count, sum(vtp.distanceCovered)   totalDistance,  avg(timeElapsed) averageTime, sum(onTimeCount ) totalOnTimeCount  from ( select td.siteId, vtp.tripId, td.vehicle_type, td.trip_date, td.label,  ifnull(vtp.distanceCovered,0)  distanceCovered, ifnull (vtp.timeElapsed,0) timeElapsed,  ifnull(onTimeCount,0)onTimeCount   from  vendor_trip_sheet_parent vtp  join   ( SELECT td.*, ts.label FROM  time_of_day ts join (   select td.* from trip_details td join tripvendorassign tva on td.id=tva.tripId  "+vendorQuery+" )   td on td.trip_time between  ts.startTime and ts.endTime)  td on vtp.tripId=td.id left join (select tripId, count(*) onTimeCount from vendor_trip_sheet_parent where onTimeStatus='On Time' group by  tripId) vts on vtp.tripId=vts.tripId  )   vtp   group by  vtp.vehicle_type, trip_date, siteId,label ) vtp  join  ( select siteId, vehicle_type, trip_date, max(count)   maxCount, min(count) minCount from ( select td.siteId,  td.vehicle,  td.vehicle_type, td.trip_date,   count(*) count  from    (   select td.* from trip_details td join tripvendorassign tva on td.id=tva.tripId  "+vendorQuery+" )  td where td.vehicle is   not null group by  td.vehicle,td.vehicle_type, trip_date ) td group by vehicle_type, siteId, trip_date ) td on  vtp.vehicle_type=td.vehicle_type and td.trip_date=vtp.trip_date and td.siteId=vtp.siteId join vehicle_type vt on vtp.vehicle_type=vt.id   "+
						"where vtp.trip_date between '"+fromDate+"' and '"+toDate+"' and vtp.siteId="+ site+vehicleQuery;
 				con = dbConn.connectDB();
				stmt = con.createStatement();
				
				if(option.equals("Time slot"))
				{
					query=timeSlotQuery;
					optionTitle="Time Slot";
				} else if(option.equals("Time of the day"))
				{
					query=timeOfTheDayQuery;
					optionTitle="Time Of The Day";
				} else if(option.equals("Distance grid"))
				{
					query=distanceGridQuery;
					optionTitle="Distance Grid";
				} else {
					query=optionlessQuery;
				}
				System.out.println(" query : " + query);
				 				rs = stmt.executeQuery(query);

				while (rs.next()) {
				 	ByVehicleTypeReportDto dto = new ByVehicleTypeReportDto();
				 	dto.setVendorId(vendorId);
				 	dto.setDate(rs.getDate("trip_date"));
				 	dto.setVehicleType(rs.getString("vehicle_type"));
				 	dto.setOption(rs.getString("optionLabel"));
				 	dto.setOptionTitle(optionTitle);
				 	dto.setCount(rs.getInt("count"));
				 	dto.setTotalDistance(rs.getDouble("totalDistance"));
				 	Calendar cal = Calendar.getInstance();
				 	//dto.setAverageTime(rs.getLong("averageTime"));
				 	cal.setTimeZone(TimeZone.getTimeZone("GMT"));
				 	cal.setTimeInMillis(rs.getLong("averageTime"));
				 	dto.setAverageTime(rs.getLong("averageTime"));
				 	//dto.setAverageTime(cal.get(Calendar.HOUR_OF_DAY) + ":" + cal.get(Calendar.MINUTE) + ":" + cal.get(Calendar.SECOND));
				 	dto.setTotalOnTimeCount(rs.getInt("totalOnTimeCount"));
				 	dto.setMinCount(rs.getInt("minCount"));
				 	dto.setMaxCount(rs.getInt("maxCount"));
				 	System.out.println(" Trip Date :" + dto.getDate());
					 dtoList.add(dto);
				}
				return dtoList;
				}else  {
					System.out.println("validation error : ");
				}

			} catch (Exception e) {
				System.out.println("Error in getbyVehicleTypeReport "+ e);
			} finally {
				DbConnect.closeResultSet(rs);
				DbConnect.closeStatement(stmt);
				DbConnect.closeConnection(con);
			}

			return null;		
	}
	
	public List<ByVehicleTypeReportDto> getVehicleStatusReport(String fromDate, String toDate, String option, String site, String vendorId)
	{
			
 
			DbConnect dbConn = DbConnect.getInstance();
			Connection con = null;
			Statement stmt = null;
			ResultSet rs = null;
			
			fromDate=OtherFunctions.changeDateFromatToIso(fromDate);
			toDate = OtherFunctions.changeDateFromatToIso(toDate);
			
			vendorId=vendorId==null?"":vendorId;
			fromDate=fromDate==null?"":fromDate;
			toDate=toDate==null?"":toDate;
			option=option==null?"":option;
			site=site==null?"":site;
			List<ByVehicleTypeReportDto> dtoList = new ArrayList<ByVehicleTypeReportDto>();
			
				
			try {
				if(!fromDate.equals("")&&!toDate.equals("")&&!site.equals("")&&!option.equals("")) {
					String siteQuery="";
					if(!site.equalsIgnoreCase("ALL"))
					{
						siteQuery="and td.siteId="+site;
					}
					String vendorQuery = " where vendorId="+vendorId;	
					String query=" select  regNo, sum(tripCount) tripCount, max(tripCount) maxTripCount, min(tripCount) minTripCount, sum(totalDistance) totalDistance, sum(totalTime) totalTime, avg(avgDistance) avgDistance, avg(avgTime) avgTime from ( select v.regNo, trip_date, count(td.id) tripCount, sum( ifnull(vtp.distanceCovered,0)) totalDistance, sum(ifnull(timeElapsed,0)) totalTime, avg( ifnull(vtp.distanceCovered,0)) avgDistance, avg(ifnull(timeElapsed,0)) avgTime   from    (   select td.* from trip_details td join tripvendorassign tva on td.id=tva.tripId  "+vendorQuery+" )  td   join vendor_trip_sheet_parent vtp on td.id=vtp.tripId join vehicles v on td.vehicle=v.id where td.vehicle is not null   and   trip_date between '"+fromDate+"' and '"+toDate+"' "+siteQuery+" group by td.trip_date, td.vehicle) tv group by regNo";
					String  optionTitle="";
					String optionLessQuery="select regNo, '' optionLabel, sum(tripCount) tripCount, max(tripCount) maxTripCount, min(tripCount) minTripCount, sum(totalDistance) totalDistance, sum(totalTime) totalTime, avg(avgDistance) avgDistance, avg(avgTime) avgTime  from ( select v.regNo,  trip_date, count(td.id) tripCount, sum( ifnull(vtp.distanceCovered,0)) totalDistance, sum(ifnull(timeElapsed,0)) totalTime, avg( ifnull(vtp.distanceCovered,0)) avgDistance, avg(ifnull(timeElapsed,0)) avgTime  		from   (   select td.* from trip_details td join tripvendorassign tva on td.id=tva.tripId  "+vendorQuery+" )  td  join vendor_trip_sheet_parent vtp on td.id=vtp.tripId join vehicles v on td.vehicle=v.id where td.vehicle is not null and trip_date  between '"+fromDate+"' and '"+toDate+"' "+siteQuery+"  group by td.trip_date, td.vehicle 	) tv   group by regNo   ";
					String timeSlotQuery="/* vehicle status report with time slot filtering  */ select regNo, concat(startTime,concat( ' - ',  endTime)) optionLabel, sum(tripCount) tripCount, max(tripCount) maxTripCount, min(tripCount) minTripCount, sum(totalDistance) totalDistance, sum(totalTime) totalTime, avg(avgDistance) avgDistance, avg(avgTime) avgTime , startTime, endTime   from ( select v.regNo,  trip_date, count(td.id) tripCount, sum( ifnull(vtp.distanceCovered,0)) totalDistance, sum(ifnull(timeElapsed,0)) totalTime, avg( ifnull(vtp.distanceCovered,0)) avgDistance, avg(ifnull(timeElapsed,0)) avgTime , ts.startTime, ts.endTime from   (   select td.* from trip_details td join tripvendorassign tva on td.id=tva.tripId  "+vendorQuery+" )  td join timesloat ts on td.trip_time between ts.startTime and ts.endTime   join vendor_trip_sheet_parent vtp on td.id=vtp.tripId join vehicles v on td.vehicle=v.id where td.vehicle is not null and trip_date  between '"+fromDate+"' and '"+toDate+"' "+ siteQuery + "  group by td.trip_date, td.vehicle , ts.startTime, ts.endTime  ) tv   group by regNo,   startTime, endTime";
					String distanceGridQuery=" /* vehicle status report with distance grid filter  */ select regNo, concat(cast(startKm as char),concat( ' - ', cast(  endKm as char))) optionLabel, sum(tripCount) tripCount, max(tripCount) maxTripCount, min(tripCount) minTripCount, sum(totalDistance) totalDistance, sum(totalTime) totalTime, avg(avgDistance) avgDistance, avg(avgTime) avgTime , startKm, endKm  from (   select v.regNo,  trip_date, count(td.id) tripCount, sum( ifnull(vtp.distanceCovered,0)) totalDistance, sum(ifnull(timeElapsed,0)) totalTime, avg( ifnull(vtp.distanceCovered,0)) avgDistance, avg(ifnull(timeElapsed,0)) avgTime , dg.startKm, dg.endKm  from   (   select td.* from trip_details td join tripvendorassign tva on td.id=tva.tripId  "+vendorQuery+" )  td join vendor_trip_sheet_parent vtp on td.id=vtp.tripId  join   distance_grid dg on vtp.distanceCovered between dg.startKm and dg.endKm  join vehicles v on td.vehicle=v.id   where td.vehicle is not null and td.trip_date   between '"+fromDate+"' and '"+toDate+"' "+ siteQuery + " group by v.regNo, trip_date, dg.startKm, dg.endKm ) tv  group by regNo, startKm, endKm; ";
					String timeOfTheDayQuery=" /* vehicle status report with time of the day filter  */ select  regNo, label optionLabel, sum(tripCount) tripCount, max(tripCount) maxTripCount, min(tripCount) minTripCount, sum(totalDistance) totalDistance, sum(totalTime) totalTime, avg(avgDistance) avgDistance, avg(avgTime) avgTime    from ( select v.regNo,  trip_date, count(td.id) tripCount, sum( ifnull(vtp.distanceCovered,0)) totalDistance, sum(ifnull(timeElapsed,0)) totalTime, avg( ifnull(vtp.distanceCovered,0)) avgDistance, avg(ifnull(timeElapsed,0)) avgTime , ts.label from   (   select td.* from trip_details td join tripvendorassign tva on td.id=tva.tripId  "+vendorQuery+" )  td join time_of_day ts on td.trip_time between ts.startTime and ts.endTime   join vendor_trip_sheet_parent vtp on td.id=vtp.tripId   join vehicles v on td.vehicle=v.id   where td.vehicle is not null and td.trip_date   group by td.trip_date, v.regNo , ts.label ) tv where trip_date group by regNo, label ;  ";
				
 				con = dbConn.connectDB();
				stmt = con.createStatement();
				if(option.equals("Time slot"))
				{
					query=timeSlotQuery;
					optionTitle="Time Slot";
				} else if(option.equals("Time of the day"))
				{
					query=timeOfTheDayQuery;
					optionTitle="Time Of The Day";
				} else if(option.equals("Distance grid"))
				{
					query=distanceGridQuery;
					optionTitle="Distance Grid";
				} else {
					query=optionLessQuery;
					optionTitle="";
				}
				System.out.println(" query : " + query);
				 				rs = stmt.executeQuery(query);

				while (rs.next()) {
				 	ByVehicleTypeReportDto dto = new ByVehicleTypeReportDto();
				 	 
				 	dto.setVehicleType(rs.getString("regNo"));
				 	dto.setOption(rs.getString("optionLabel"));
				 	dto.setOptionTitle(optionTitle);
				 	dto.setCount(rs.getInt("tripCount"));
				 	dto.setTotalDistance(rs.getDouble("totalDistance"));
				 	Calendar cal = Calendar.getInstance();
				 	//dto.setAverageTime(rs.getLong("avgTime"));
				 	cal.setTimeInMillis(rs.getLong("avgTime"));
					dto.setAverageTime(rs.getLong("avgTime"));
				 //	dto.setAverageTime(cal.get(Calendar.HOUR) + ":" + cal.get(Calendar.MINUTE));
				 	dto.setAverageDistance(rs.getDouble("avgDistance"));
				 	dto.setMinCount(rs.getInt("minTripCount"));
				 	dto.setMaxCount(rs.getInt("maxTripCount"));
				 	 
					 dtoList.add(dto);
				}
				return dtoList;
				}

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				DbConnect.closeResultSet(rs);
				DbConnect.closeStatement(stmt);
				DbConnect.closeConnection(con);
			}

			return null;

 
		
	}
	public List<TripDetailsDto> getVehicleKilometerReport(String fromDate, String toDate, String site, String vendorId)
	{
			
            System.out.println("In the servlet");
			DbConnect dbConn = DbConnect.getInstance();
			Connection con = null;
			Statement stmt = null;
			ResultSet rs = null;
			
			fromDate=OtherFunctions.changeDateFromatToIso(fromDate);
			toDate = OtherFunctions.changeDateFromatToIso(toDate);
			
			vendorId=vendorId==null?"":vendorId;
			fromDate=fromDate==null?"":fromDate;
			toDate=toDate==null?"":toDate;
			
			site=site==null?"":site;
			List<TripDetailsDto> dtoList = new ArrayList<TripDetailsDto>();
			
				
			try {
				if(!fromDate.equals("")&&!toDate.equals("")&&!site.equals("")) {
				
 				con = dbConn.connectDB();
				stmt = con.createStatement();
				String query = "SELECT vtsp.tripid,td.id,td.trip_code,td.trip_date,td.trip_time,td.distance AS estimateddistance,td.traveltime AS estimatedtraveltime, vtsp.vehicleno , vtsp.distancecovered , vtsp.timeelapsed FROM trip_details AS td,vendor_trip_sheet_parent AS vtsp, tripvendorassign AS tva,vehicles AS v WHERE td.trip_date BETWEEN '"+fromDate+"' AND '"+toDate+"' AND (td.status = 'saved' or td.status = 'saveedit') AND td.id = vtsp.tripid AND vtsp.tripid = tva.tripid AND vtsp.vehicleno = v.regno AND tva.vendorid = "+vendorId+" AND (vtsp.startTime != NULL OR vtsp.startTime !='') AND  td.siteid = "+site;
				System.out.println(" query : " + query);
				 				rs = stmt.executeQuery(query);

				while (rs.next()) {
				 	TripDetailsDto dto = new TripDetailsDto();
				 	 
				 	dto.setTrip_code(rs.getString("trip_code"));
				 	System.out.println("trip code " + rs.getString("trip_code") );
				 
                    dto.setTrip_date( rs.getString("trip_date") );
				 	
				 	dto.setTrip_time(rs.getString("trip_time"));
				 	dto.setVehicleNo(rs.getString("vehicleno"));
				 	dto.setDistanceDouble(rs.getDouble("estimateddistance"));
				 	dto.setTravelTime(rs.getString("estimatedtraveltime"));
				 	dto.setDistanceCovered(rs.getDouble("distancecovered"));
				 	dto.setTimeElapsed(rs.getInt("timeelapsed"));
				 	
				 	 
					 dtoList.add(dto);
				}
				return dtoList;
				}

			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("error in ByVehicleTypereporthelper" + e);
			} finally {
				DbConnect.closeResultSet(rs);
				DbConnect.closeStatement(stmt);
				DbConnect.closeConnection(con);
			}

			return null;

 
		
	}



}
