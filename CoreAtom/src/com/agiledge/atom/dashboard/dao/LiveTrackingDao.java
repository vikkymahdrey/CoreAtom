package com.agiledge.atom.dashboard.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.agiledge.atom.dashboard.dto.LiveTrackingDto;
import com.agiledge.atom.dbConnect.DbConnect;
import com.agiledge.atom.dto.LogTimeDto;
import com.agiledge.atom.dto.TripDetailsChildDto;

public class LiveTrackingDao {

	public ArrayList<LogTimeDto> getLast3ShiftTimes()
	{
		DbConnect ob = DbConnect.getInstance();
		ArrayList<LogTimeDto> list=new ArrayList<LogTimeDto>();
		String query = "SELECT LOGTIME,LOGTYPE FROM LOGTIME WHERE STATUS='active' AND concat(LOGTIME,':00')<=concat(curtime(),'') and logtime in (select trip_time from trip_details where trip_date = curdate() ) order by logtime desc limit 3";
		Connection con = ob.connectDB();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = con.prepareStatement(query);
			rs = stmt.executeQuery();
			while (rs.next()) {
				LogTimeDto dto= new LogTimeDto();
				dto.setLogTime(rs.getString("LOGTIME"));
				dto.setLogtype(rs.getString("LOGTYPE"));
				list.add(dto);
				
			}
		} catch (Exception e) {
			System.out.println("Exception in livedao 1 " + e);

		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(stmt);
			DbConnect.closeConnection(con);
		}

		return list;
		
		
	}
	
	
	public String[] getEmployeeValues(String triptime,String log)
	{
		DbConnect ob = DbConnect.getInstance();
		String values[]=new String[3];
		String query = "SELECT COUNT(EMPLOYEEID) FROM TRIP_DETAILS_CHILD WHERE TRIPID IN (SELECT ID FROM TRIP_DETAILS WHERE STATUS IN ('SAVED','SAVEEDIT','ADDSAVE') AND TRIP_TIME='"+triptime+"' AND TRIP_LOG='"+log+"' AND TRIP_DATE=CURDATE()) UNION SELECT COUNT(EMPLOYEEID) FROM VENDOR_TRIP_SHEET WHERE SHOWSTATUS='SHOW' AND TRIPID IN (SELECT ID FROM TRIP_DETAILS WHERE STATUS IN ('SAVED','SAVEEDIT','ADDSAVE') AND TRIP_TIME='"+triptime+"' AND TRIP_LOG='"+log+"' AND TRIP_DATE=CURDATE()) UNION SELECT COUNT(EMPLOYEEID) FROM VENDOR_TRIP_SHEET WHERE SHOWSTATUS='SHOW' AND CURSTATUS='OUT' AND TRIPID IN (SELECT ID FROM TRIP_DETAILS WHERE STATUS IN ('SAVED','SAVEEDIT','ADDSAVE') AND TRIP_TIME='"+triptime+"' AND TRIP_LOG='"+log+"' AND TRIP_DATE=CURDATE())";
		System.out.println(query);
		Connection con = ob.connectDB();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = con.prepareStatement(query);
			rs = stmt.executeQuery();
			int i=0;
			while (rs.next()) {
				values[i]=rs.getString(1);
				i++;	
			}
			if(i==2){
				values[2]=values[1];
			}
		} catch (Exception e) {
			System.out.println("Exception in livedao 2" + e);

		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(stmt);
			DbConnect.closeConnection(con);
		}

		return values;
		
		
	}
	
	
	public String[] getTripReport(String shift,String log)
	{
		DbConnect ob = DbConnect.getInstance();
		String values[]=new String[3];
		String query = "SELECT COUNT(ID),1 FROM TRIP_DETAILS WHERE STATUS IN ('SAVED','SAVEEDIT','ADDSAVE') AND TRIP_TIME='"+shift+"' AND TRIP_LOG='"+log+"' AND TRIP_DATE=CURDATE() AND VEHICLE is not NULL UNION SELECT COUNT(TRIPID),2 FROM VENDOR_TRIP_SHEET_PARENT WHERE STATUS='started' AND TRIPID IN (SELECT ID FROM TRIP_DETAILS WHERE STATUS IN ('SAVED','SAVEEDIT','ADDSAVE') AND TRIP_TIME='"+shift+"' AND TRIP_LOG='"+log+"' AND TRIP_DATE=CURDATE()) UNION SELECT COUNT(TRIPID),3 FROM VENDOR_TRIP_SHEET_PARENT WHERE STATUS='stopped' AND TRIPID IN (SELECT ID FROM TRIP_DETAILS WHERE STATUS IN ('SAVED','SAVEEDIT','ADDSAVE') AND TRIP_TIME='"+shift+"' AND TRIP_LOG='"+log+"' AND TRIP_DATE=CURDATE())";
		System.out.println(query);
		Connection con = ob.connectDB();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = con.prepareStatement(query);
			rs = stmt.executeQuery();
			int i=0;
			while (rs.next()) {
				values[i]=rs.getString(1);
				i++;		
			}
			
		} catch (Exception e) {
			System.out.println("Exception in livedao 3" + e);

		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(stmt);
			DbConnect.closeConnection(con);
		}

		return values;
		
	}
	public ArrayList<LiveTrackingDto> getNext3ShiftTimes()
	{
		DbConnect ob = DbConnect.getInstance();
		ArrayList<LiveTrackingDto> list=new ArrayList<LiveTrackingDto>();
		String query = "SELECT LOGTIME,LOGTYPE, curDate() as date FROM LOGTIME WHERE STATUS='active' AND concat(LOGTIME,':00')>=concat(curtime(),'') and logtime in (select trip_time from trip_details where trip_date=curdate()) order by logtime  limit 3";
		System.out.println(query);
		Connection con = ob.connectDB();
		PreparedStatement stmt = null, stmt1 = null;
		ResultSet rs = null,rs1 = null;
		try {
			stmt = con.prepareStatement(query);
			rs = stmt.executeQuery();
			int i=0;
			while (rs.next()) {
				i++;
				LiveTrackingDto dto= new LiveTrackingDto();
				dto.setLogtime(rs.getString("LOGTIME"));
				dto.setLogtype(rs.getString("LOGTYPE"));
				dto.setDate(rs.getString("date"));
				list.add(dto);
			}
			int j=0;
			if (i<3)
			{  
				j=3-i;
				String query1 = "SELECT LOGTIME,LOGTYPE, (CURDATE() + INTERVAL 1 day) as date FROM LOGTIME WHERE STATUS='active'  and logtime in (select trip_time from trip_details where trip_date=(CURDATE() + INTERVAL 1 day)) order by logtime  limit "+j;
				System.out.println(query1);
				stmt1 = con.prepareStatement(query1);
				rs1 = stmt1.executeQuery();
				while (rs1.next()) {
					LiveTrackingDto dto= new LiveTrackingDto();
				dto.setLogtime(rs1.getString("LOGTIME"));
				dto.setLogtype(rs1.getString("LOGTYPE"));
				dto.setDate(rs1.getString("date"));
				
				list.add(dto);
				}
			}
			
			
		} catch (Exception e) {
			System.out.println("Exception in livedao getNext3ShiftTimes" + e);

		} finally {
			DbConnect.closeResultSet(rs,rs1);
			DbConnect.closeStatement(stmt,stmt1);
			DbConnect.closeConnection(con);
		}

		return list;
		
		
	}
	public LiveTrackingDto getAdhocDetails(String triptime,String log,String date)
	{
		DbConnect ob = DbConnect.getInstance();
		String values[]=new String[4];
		String query = "select count(empid),1 from adhocbooking   where travelDate= curdate() and status in('approved','pending') union select count(empid),2  from adhocbooking   where travelDate= curdate() and status ='approved' union select count(empid),3  from adhocbooking   where travelDate= curdate()  and status ='pending' union select count(empid),4  from adhocbooking where travelDate= '"+date+"' and pickDrop = '"+log+"' and startTime ='"+triptime+"' and status ='pending' ";
		System.out.println(query);
		LiveTrackingDto dto =new LiveTrackingDto();
		Connection con = ob.connectDB();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = con.prepareStatement(query);
			rs = stmt.executeQuery();
			int i=0;
			while (rs.next()) {
				values[i]=rs.getString(1);
				values[i]=values[i]==null ? "0":values[i];
				i++;	
			}
			
			dto.setAdhocrequest(values[0]);
			dto.setAdhocapproved(values[1]);
			dto.setAdhocpending(values[2]);
			dto.setNextadhocpending(values[3]);
			
			
		} catch (Exception e) {
			System.out.println("Exception in livedao getAdhocDetails " + e);

		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(stmt);
			DbConnect.closeConnection(con);
		}

		return dto;
		
		
	}


	public LiveTrackingDto currentShiftDetails()
	{
		DbConnect ob = DbConnect.getInstance();
		String logtime="",logtype ="",date="";
		
		
	
		String query = "SELECT LOGTIME,LOGTYPE,curDate() as date FROM LOGTIME WHERE STATUS='active' AND concat(LOGTIME,':00')<=concat(curtime(),'') and logtime in (select trip_time from trip_details where trip_date = curdate() ) order by logtime desc limit 1";
		System.out.println(query);
		
		Connection con = ob.connectDB();
		PreparedStatement stmt = null,stmt1 = null,stmt2 = null;
		ResultSet rs = null, rs1 = null, rs2 = null;
		LiveTrackingDto dto= new LiveTrackingDto();
		try {
			stmt = con.prepareStatement(query);
			rs = stmt.executeQuery();
			int i=0;
			
			while (rs.next()) {
				
				logtime=rs.getString("logtime");
				logtype=rs.getString("logtype");
				date=rs.getString("date");
				i++;	
			}
			
			if(i==0)
			{
				String query1 = "SELECT LOGTIME,LOGTYPE,(CURDATE() - INTERVAL 1 day) as date FROM LOGTIME WHERE STATUS='active' and logtime in (select trip_time from trip_details where trip_date = (CURDATE() - INTERVAL 1 day) ) order by logtime desc limit 1";	
				System.out.println(query1);
				stmt1 = con.prepareStatement(query1);
				rs1 = stmt1.executeQuery();
				while (rs1.next()) {
					logtime=rs1.getString("logtime");
					logtype=rs1.getString("logtype");
					date=rs1.getString("date");
					}
			}
			
			
			String query2=" /* total trips starts*/ select  totalCount, ifnull(downloaded.downloadedCount,0) downloadedCount,ifnull(initial.initialCount,0) initialCount,ifnull(started.startedCount,0) startedCount, ifnull(stopped.stoppedCount,0) stoppedCount  from ( select  trip_date, count(*) totalCount from trip_details td where   td.status in ('saved','addsave','saveedit')  and "
					+ " trip_date ='"+date+"'  and td.trip_time in ('"+logtime+"')  and td.trip_log='"+logtype+"'    group by td.trip_date ) total /* totalTrips ends*/ left join  ( /* downloaded trips starts */ select trip_date, downloadedCount from ( select  trip_date, count(*) downloadedCount  from trip_details td join vendor_trip_sheet_parent vtp on td.id=vtp.tripId where vtp.downloadStatus='downloaded'   "
					+ "and td.trip_time in ('"+logtime+"')  and td.trip_log='"+logtype+"'    group by trip_date   ) downloaded ) downloaded /* downloaded trips ends */ on total.trip_date=downloaded.trip_date left join  (   /* initial trips starts */ select trip_date, initialCount from ( select  trip_date, count(*) initialCount  from trip_details td join vendor_trip_sheet_parent vtp on td.id=vtp.tripId where vtp.downloadStatus='downloaded' and vtp.status='initial'  "
					+ " and td.trip_time in ('"+logtime+"')  and td.trip_log='"+logtype+"'    group by trip_date   ) initial /* started trips ends */  ) initial on total.trip_date=initial.trip_date left join  (   /* started trips starts */ select trip_date, startedCount from ( select  trip_date, count(*) startedCount  from trip_details td join vendor_trip_sheet_parent vtp on td.id=vtp.tripId where vtp.downloadStatus='downloaded' and  vtp.status='started'  "
							+ " and td.trip_time in ('"+logtime+"')  and td.trip_log='"+logtype+"'   group by trip_date   ) started /* started trips ends */  ) started on total.trip_date=started.trip_date left join ( /* stopped trips starts */ select trip_date, stoppedCount from ( select  trip_date, count(*) stoppedCount  from trip_details td join vendor_trip_sheet_parent vtp on td.id=vtp.tripId where vtp.downloadStatus='downloaded' and  vtp.status='stopped' "
					+ "   and td.trip_time in ('"+logtime+"')  and td.trip_log='"+logtype+"'     group by trip_date   ) downloaded /* stopped trips ends */  ) stopped on total.trip_date= stopped.trip_date";  
			System.out.println(query2);
			
			stmt2 = con.prepareStatement(query2);
			rs2 = stmt2.executeQuery();
			while (rs2.next()) {
			
				dto.setLogtime(logtime);
				dto.setLogtype(logtype);
				dto.setDate(date);
				dto.setTotal(rs2.getString("totalCount"));
				dto.setDownloaded(rs2.getString("downloadedCount"));
				dto.setStarted(rs2.getString("startedCount"));
				dto.setNotstarted(rs2.getString("initialCount"));
				dto.setCompleted(rs2.getString("stoppedCount"));
				int temp=Integer.parseInt(rs2.getString("totalCount"))-Integer.parseInt(rs2.getString("downloadedCount"));
				dto.setNotdownloaded(temp+"");
			}
			
			
			
		} catch (Exception e) {
			System.out.println("Exception in livedao getLastShiftDetails " + e);

		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(stmt);
			DbConnect.closeConnection(con);
		}

		return dto;
		
		
	}

	public ArrayList<LiveTrackingDto> getLastInandOut()
	{
		DbConnect ob = DbConnect.getInstance();
	
		String query = "(SELECT LOGTIME,logtype FROM LOGTIME WHERE STATUS='active' and LOGTYPE='in' AND concat(LOGTIME,':00')<=concat(curtime(),'') and logtime in (select trip_time from trip_details where trip_date = curdate() ) order by logtime desc limit 1) union (SELECT LOGTIME,logtype FROM LOGTIME WHERE STATUS='active' and LOGTYPE='out' AND concat(LOGTIME,':00')<=concat(curtime(),'') and logtime in (select trip_time from trip_details where trip_date = curdate() ) order by logtime desc limit 1) ";
		System.out.println(query);
		
	
		Connection con = ob.connectDB();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		ArrayList<LiveTrackingDto> list=new ArrayList<LiveTrackingDto>();
			
		try {
			stmt = con.prepareStatement(query);
			rs = stmt.executeQuery();
			while (rs.next()) {
				LiveTrackingDto dto= new LiveTrackingDto();
				dto.setLogtime(rs.getString("LOGTIME"));
				dto.setLogtype(rs.getString("LOGTYPE"));
				list.add(dto);
			}
			
			
			
			
			
			
		} catch (Exception e) {
			System.out.println("Exception in livedao getNextshiftPendingDetails" + e);

		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(stmt);
			DbConnect.closeConnection(con);
		}

		return list;
		
		
	}
	
		public LiveTrackingDto getLastInOutDetails(String log)
	    {
		DbConnect ob = DbConnect.getInstance();
		String values[]=new String[9];
		LiveTrackingDto dto= new LiveTrackingDto();

		Connection con = ob.connectDB();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		PreparedStatement pst = null;
		ResultSet rs1 = null;
		String triptime="";
	
		
		
		String query1 = " SELECT LOGTIME,logtype FROM LOGTIME WHERE STATUS='active' and LOGTYPE='"+log+"' AND concat(LOGTIME,':00')<=concat(curtime(),'') and logtime in (select trip_time from trip_details where trip_date = curdate() ) order by logtime desc limit 1  ";

		try {
			pst = con.prepareStatement(query1);
			rs1 = pst.executeQuery();
			if (rs1.next()) {
			
				triptime=rs1.getString("LOGTIME");
				log=rs1.getString("LOGTYPE");
			}
			dto.setLogtime(triptime);
			dto.setLogtype(log);
		
		
		
		
		String query = 
				"SELECT COUNT(EMPLOYEEID),1 FROM TRIP_DETAILS_CHILD WHERE TRIPID IN (SELECT ID FROM TRIP_DETAILS WHERE STATUS IN ('SAVED','SAVEEDIT','ADDSAVE') AND TRIP_TIME='"+triptime+"' AND TRIP_LOG='"+log+"' AND TRIP_DATE= curdate())"
	   + " UNION SELECT COUNT(EMPLOYEEID),2 FROM VENDOR_TRIP_SHEET WHERE SHOWSTATUS='SHOW' AND TRIPID IN (SELECT ID FROM TRIP_DETAILS WHERE STATUS IN ('SAVED','SAVEEDIT','ADDSAVE') AND TRIP_TIME='"+triptime+"' AND TRIP_LOG='"+log+"' AND TRIP_DATE= curdate()) "
		+ "UNION SELECT COUNT(EMPLOYEEID),3 FROM VENDOR_TRIP_SHEET WHERE SHOWSTATUS='SHOW' AND CURSTATUS='OUT' AND TRIPID IN (SELECT ID FROM TRIP_DETAILS WHERE STATUS IN ('SAVED','SAVEEDIT','ADDSAVE') AND TRIP_TIME='"+triptime+"' AND TRIP_LOG='"+log+"' AND TRIP_DATE= curdate())"
		+ " UNION SELECT COUNT(EMPLOYEEID),4 FROM TRIP_DETAILS_CHILD WHERE  employeeid in (select id from employee where gender ='F') and TRIPID IN (SELECT ID FROM TRIP_DETAILS WHERE STATUS IN ('SAVED','SAVEEDIT','ADDSAVE') AND TRIP_TIME='"+triptime+"' AND TRIP_LOG='"+log+"' AND TRIP_DATE= curdate())"
		 + " UNION SELECT COUNT(EMPLOYEEID),5 FROM VENDOR_TRIP_SHEET WHERE employeeid in (select id from employee where gender ='F') and SHOWSTATUS='SHOW'  AND TRIPID IN (SELECT ID FROM TRIP_DETAILS WHERE STATUS IN ('SAVED','SAVEEDIT','ADDSAVE') AND TRIP_TIME='"+triptime+"' AND TRIP_LOG='"+log+"' AND TRIP_DATE= curdate()) "
			+ "UNION SELECT COUNT(EMPLOYEEID),6 FROM VENDOR_TRIP_SHEET WHERE employeeid in (select id from employee where gender ='F') and SHOWSTATUS='SHOW' AND CURSTATUS='OUT' AND TRIPID IN (SELECT ID FROM TRIP_DETAILS WHERE STATUS IN ('SAVED','SAVEEDIT','ADDSAVE') AND TRIP_TIME='"+triptime+"' AND TRIP_LOG='"+log+"' AND TRIP_DATE= curdate())"
			+ "UNION SELECT COUNT(ID),7 FROM TRIP_DETAILS WHERE STATUS IN ('SAVED','SAVEEDIT','ADDSAVE') AND TRIP_TIME='"+triptime+"' AND TRIP_LOG='"+log+"' AND TRIP_DATE=curdate() AND VEHICLE is not NULL "
					+ "UNION SELECT COUNT(TRIPID),8 FROM VENDOR_TRIP_SHEET_PARENT WHERE STATUS='started' AND TRIPID IN (SELECT ID FROM TRIP_DETAILS WHERE STATUS IN ('SAVED','SAVEEDIT','ADDSAVE') AND TRIP_TIME='"+triptime+"' AND TRIP_LOG='"+log+"' AND TRIP_DATE=curdate())"
							+ " UNION  SELECT COUNT(TRIPID),9 FROM VENDOR_TRIP_SHEET_PARENT WHERE STATUS='stopped' AND TRIPID IN (SELECT ID FROM TRIP_DETAILS WHERE STATUS IN ('SAVED','SAVEEDIT','ADDSAVE') AND TRIP_TIME='"+triptime+"' AND TRIP_LOG='"+log+"' AND TRIP_DATE=curdate())" ;
		
		
		System.out.println(query);

		
			stmt = con.prepareStatement(query);
			rs = stmt.executeQuery();
			int i=0;
			while (rs.next()) {
				values[i]=rs.getString(1);
				i++;	
			}
			
			dto.setTotalemp(values[0]);
			dto.setBoardemp(values[1]);
			dto.setRechemp(values[2]);
			dto.setTotalfemale(values[3]);
			dto.setBoardfemale(values[4]);
			dto.setRechfemale(values[5]);
			dto.setTotal(values[6]);
			dto.setStarted(values[7]);
			dto.setCompleted(values[8]);
			
			
			
		} catch (Exception e) {
			System.out.println("Exception in livedao getEmpDetails " + e);

		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(stmt);
			DbConnect.closeConnection(con);
		}

		return dto;
		}
	
	
	public LiveTrackingDto getVehicleStatus(String triptime,String log,String date)
	{
		DbConnect ob = DbConnect.getInstance();
		String values[]=new String[2];
		LiveTrackingDto dto =new LiveTrackingDto();
		String query ="select count(id),1 from trip_details where trip_time ='"+triptime+"' and trip_log ='"+log+"' and trip_date ='"+date+"' and vehicle is not null union select count(id),2 from trip_details where trip_time ='"+triptime+"' and trip_log ='"+log+"' and trip_date ='"+date+"' and vehicle is null ";


        // vehicles deployed ,vehicles allocated,vehicles pending		
		
		
		System.out.println(query);
		Connection con = ob.connectDB();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = con.prepareStatement(query);
			rs = stmt.executeQuery();
			int i=0;
			while (rs.next()) {
				values[i]=rs.getString(1);
				i++;	
			}
			
			dto.setVehallocated(values[0]);
			dto.setVehpending(values[1]);
			
			
			
		} catch (Exception e) {
			System.out.println("Exception in livedao getVehicleDetails " + e);

		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(stmt);
			DbConnect.closeConnection(con);
		}

		return dto;
		
	}

	public ArrayList<LiveTrackingDto> getEmployeeStatus(String filter,String triptime,String triplog)
	{
		DbConnect ob = DbConnect.getInstance();
		ArrayList<LiveTrackingDto> list=new ArrayList<LiveTrackingDto>();
	
		
		String subquery="";
		if(filter.equalsIgnoreCase("Women"))
		{
			subquery=" and e.gender='f' "	;
		}	
		
		
		String query = "select e.displayname,e.personnelno,e.gender,t.id,v.regNo,d.name,d.contact,vtp.curStatus from trip_details t ,driver d,vehicles v,vendor_trip_sheet vtp,employee e where t.id=vtp.tripid and t.trip_date=curdate() and d.id=t.driverId and v.id=t.vehicle  and e.id=vtp.employeeid and t.trip_time= '"+triptime+"' and t.trip_log='"+triplog+"' and vtp.curStatus in ('IN','OUT')" +subquery +" order by t.id desc";
		
		Connection con = ob.connectDB();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			
			String gender="";
			String status="";
			stmt = con.prepareStatement(query);
			rs = stmt.executeQuery();
			while (rs.next()) {
				
				gender=rs.getString("gender");
				if (gender.equalsIgnoreCase("F"))
				{
					gender="Female";
				}else{
					gender="Male";
				}
				status=rs.getString("curStatus");
				if (status.equalsIgnoreCase("OUT"))
				{
					status="Reached";
				}
				else{
					status="Boarded";
				}
				LiveTrackingDto dto= new LiveTrackingDto();
				dto.setEmpname(rs.getString("displayname"));
				dto.setEmppersonnelno(rs.getString("personnelno"));
				dto.setGender(gender);
				dto.setTripid(rs.getString("id"));
				dto.setVehicle(rs.getString("regNo"));
				dto.setDriverName(rs.getString("name"));
				dto.setDriverContact(rs.getString("contact"));
				dto.setStatus(status);
				list.add(dto);
				
			}
		} catch (Exception e) {
			System.out.println("Exception in getCurTripDetails " + e);

		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(stmt);
			DbConnect.closeConnection(con);
		}

		return list;
		
		
	}
	public ArrayList<LiveTrackingDto> getTripStatus(String date,String triptime,String triplog)
	{

		DbConnect ob = DbConnect.getInstance();
		ArrayList<LiveTrackingDto> list=new ArrayList<LiveTrackingDto>();
		String query = "select t.id,v.regNo,d.name,d.contact,vtp.status,vtp.downloadStatus,r.routename from trip_details t left outer join vendor_trip_sheet_parent vtp  on  t.id=vtp.tripid left outer join driver d on  d.id=t.driverId left outer join vehicles v on v.id=t.vehicle left outer join route r on r.id=t.routeId where t.trip_date='"+date+"' and t.trip_time='"+triptime+"' and t.trip_log='" +triplog+"'   order by t.id desc";
		System.out.println(query);
		
		Connection con = ob.connectDB();
		PreparedStatement stmt = null;
		Statement st =null;
		ResultSet rs = null,rs1=null;
		try {
			
			String status="";
			String downloaded="";
			String driver="";
			String drivercontact="";
			String vehicle="";
			String route="";
			stmt = con.prepareStatement(query);
			rs = stmt.executeQuery();
			while (rs.next()) {
				 st=con.createStatement();
				 String getqry="select count(employeeid)as empcount from vendor_trip_sheet where showStatus='show' and tripid="+rs.getString("id");
				 rs1=st.executeQuery(getqry);
				 String count="0";
				 if(rs1.next()){
					 count=rs1.getString("empcount");
				 }
				 status=rs.getString("status");
				 driver=rs.getString("name");
				 drivercontact=rs.getString("contact");
				 vehicle=rs.getString("regNo");
				 downloaded=rs.getString("downloadStatus");
				 route=rs.getString("routename");
				
				driver=driver==null ? "****":driver;
				drivercontact=drivercontact==null ? "****":drivercontact;
				vehicle=vehicle==null ? "Not Assigned":vehicle;
				status=status==null ? " ":status;
				downloaded=downloaded==null ? " ":downloaded;
				route=route==null?" ":route;
				
			    if(status.equalsIgnoreCase("started")){
					status="Running";
				}
				else if(status.equalsIgnoreCase("stopped")){
					status="Completed";
				}
				else{
					status="Not Started";
					
				}
				if(downloaded.equalsIgnoreCase("Downloaded")){
					downloaded="Downloaded";
				}
				else {
					downloaded="Not Downloaded";
				}
				
				
				LiveTrackingDto dto= new LiveTrackingDto();
				dto.setTripid(rs.getString("id"));
				dto.setVehicle(vehicle);
				dto.setDriverName(driver);
				dto.setDriverContact(drivercontact);
				dto.setDownloaded(downloaded);
				dto.setStatus(status);
				dto.setRoute(route);
				dto.setTotalemp(count);
				list.add(dto);
		
			}
		} catch (Exception e) {
			System.out.println("Exception in getCurTripDetails " + e);

		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(stmt);
			DbConnect.closeConnection(con);
		}

		return list;
		
		
	}
	/*public LiveTrackingDto getDriverDetails(String triptime,String log,String date)
	{
		DbConnect ob = DbConnect.getInstance();
		String values[]=new String[4];
		String query = "select count(empid),1 from adhocbooking   where travelDate= curdate() and status in('approved','pending') union select count(empid),2  from adhocbooking   where travelDate= curdate() and status ='approved' union select count(empid),3  from adhocbooking   where travelDate= curdate()  and status ='pending' union select count(empid),4  from adhocbooking where travelDate= '"+date+"' and pickDrop = '"+log+"' and startTime ='"+triptime+"' and status ='pending' ";
		System.out.println(query);
		LiveTrackingDto dto =new LiveTrackingDto();
		Connection con = ob.connectDB();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = con.prepareStatement(query);
			rs = stmt.executeQuery();
			int i=0;
			while (rs.next()) {
				values[i]=rs.getString(1);
				values[i]=values[i]==null ? "0":values[i];
				i++;	
			}
			
			dto.setAdhocrequest(values[0]);
			dto.setAdhocapproved(values[1]);
			dto.setAdhocpending(values[2]);
			dto.setNextadhocpending(values[3]);
			
			
		} catch (Exception e) {
			System.out.println("Exception in livedao driver details " + e);

<<<<<<< LiveTrackingDao.java
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(stmt);
			DbConnect.closeConnection(con);
		}

		return dto;
		
		
	}
*/

	public List<TripDetailsChildDto> getEmployeeInTrip(String tripid) {
		DbConnect ob = DbConnect.getInstance();
		Connection con = ob.connectDB();
		PreparedStatement stmt = null;
		Statement st =null;
		ResultSet rs = null;
		List<TripDetailsChildDto> list=new ArrayList<TripDetailsChildDto>();
		try{
			String qry="select e.personnelNo,e.displayName,e.gender,vts.inTime,vts.outTime,vts.isCorrectPos from vendor_trip_sheet vts,employee e where vts.tripId="
							+ tripid
							+ " and e.id=vts.employeeId order by vts.inTime";
			st=con.createStatement();;
			rs=st.executeQuery(qry);
			while(rs.next()){
				TripDetailsChildDto childDto = new TripDetailsChildDto();
					childDto.setEmployeeId(rs.getString("personnelNo"));
					childDto.setEmployeeName(rs.getString("displayName"));
					childDto.setGender(rs.getString("gender"));
					childDto.setInTime(rs.getString("inTime"));
					childDto.setOutTime(rs.getString("outTime"));
					childDto.setIsCorrectPos(rs.getString("isCorrectPos"));
					list.add(childDto);
				
			}
			
		} catch (Exception e) {
			System.out.println("Exception in LiveTrackingDao@getEmployeeInTrip " + e);

		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(stmt);
			DbConnect.closeConnection(con);
		}

		return list;
	}

	
}
