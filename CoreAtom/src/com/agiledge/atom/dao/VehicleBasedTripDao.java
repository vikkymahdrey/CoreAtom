package com.agiledge.atom.dao;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.agiledge.atom.commons.OtherFunctions;
import com.agiledge.atom.dbConnect.DbConnect;
import com.agiledge.atom.dto.VehicleBasedTripDto;
public class VehicleBasedTripDao {

	public ArrayList<VehicleBasedTripDto> getTripdetails(String siteId,String fromDate, String toDate, String vehicle){
		
		DbConnect dbConn = DbConnect.getInstance();
		Connection con = null;
		Statement stmt=null,st = null;
		ResultSet rs=null,rs1 = null;
		String travelFromDate=OtherFunctions.changeDateFromatToIso(fromDate);
		String traveltoDate=OtherFunctions.changeDateFromatToIso(toDate);
		String Starttime;
		String Stoptime;
		String download ;
		String order ="1";
		String apl="";
		ArrayList<VehicleBasedTripDto> dtoList = new ArrayList<VehicleBasedTripDto>();
		con = dbConn.connectDB();
		String query = "select td.id,td.trip_code,date_format(td.trip_date,'%d/%m/%Y')  trip_date ,td.trip_log,td.trip_time,vtsp.status,vtsp.startTime,vtsp.stopTime,vtsp.downloadStatus,count(e.id) as actemp,count(temp.id) as inemp,vt.type from trip_details td join vehicle_type vt on vt.id=td.vehicle_type join vendor_trip_sheet_parent vtsp on td.id=vtsp.tripId join vendor_trip_sheet vts	on vtsp.tripId=vts.tripId join employee e on vts.employeeId=e.id left outer join (select * from employee) temp  on vts.employeeId=temp.id and vts.showstatus='Show' where (td.trip_date between '"+travelFromDate+"' and '"+traveltoDate+"') and td.vehicle= '"+vehicle+"' group by td.id ";
		System.out.println("Query "+query);
		try {			
			stmt = con.createStatement();
			rs = stmt.executeQuery(query);
			while(rs.next())
			{
				Starttime=rs.getString("startTime");
				Stoptime=rs.getString("stopTime");
			    download =rs.getString("downloadStatus");
			    order="1";
			    if( rs.getString("trip_log").equalsIgnoreCase("OUT"))
			    {
				   order=rs.getString("actemp");
			    }
			  
			   st = con.createStatement();
			    String aplqry="select a.area,p.place,l.landmark from landmark l,area a,place p where a.id=p.area and p.id=l.place and l.id =(select landmarkId from trip_details_child where tripid ="+rs.getString("id")+"  and routedorder ="+order+")";
				rs1=st.executeQuery(aplqry);
				if(rs1.next()){
					apl=rs1.getString("area")+" - ";
					apl=apl+rs1.getString("place")+" - ";
					apl=apl+rs1.getString("landmark");
				}
				if ((Starttime == null)|| Starttime.equalsIgnoreCase("null"))
				{
					Starttime = "";
				}
				if ((Stoptime == null)|| Stoptime.equalsIgnoreCase("null"))
				{
					Stoptime = "";
				}
				if ( download.equalsIgnoreCase("ready"))
				{
					download = "No";
				}
				else
				{
					download = "Yes";
				}
				
				VehicleBasedTripDto dto = new VehicleBasedTripDto();
				dto.setSiteId(siteId);
				dto.setTripId(rs.getString("id"));
				dto.setTripCode(rs.getString("trip_code"));
				dto.setTripDate(rs.getString("trip_date"));
				dto.setTripLog(rs.getString("trip_log"));
				dto.setTripTime(rs.getString("trip_time"));
				dto.setStarttime(Starttime);
				dto.setStopTime(Stoptime);
				dto.setStatus(rs.getString("status"));
				dto.setActemp(rs.getString("actemp"));
				dto.setInemp(rs.getString("inemp"));
				dto.setType(rs.getString("type"));
				dto.setDownload(download);
				dto.setTripAPL(apl);
			 	 
				dtoList.add(dto);
			}
			return dtoList;
		} catch (SQLException e) {
			System.err.println("Report:error in VehicleBasedTripDao"+e);
		}
		finally{
			DbConnect.closeResultSet(rs,rs1);
		DbConnect.closeStatement(stmt,st);
		DbConnect.closeConnection(con);
	}

		
		return dtoList;
	}
	public ArrayList<VehicleBasedTripDto> getEmpDetails(int tripId)
    {
    	
    	DbConnect dbConn = DbConnect.getInstance();
	    Connection con = null;
	    Statement stmt = null;
	    ResultSet rs = null;
	    ArrayList<VehicleBasedTripDto> dtoList = new ArrayList<VehicleBasedTripDto>();
	    String query  = " select e.id , e.displayname ,e.PersonnelNo ,vt.showstatus as estat from employee e join vendor_trip_sheet vt on e.id = vt.employeeId and vt.tripId="+tripId;
		
			con = dbConn.connectDB();
			try {			
			stmt = con.createStatement();
			rs = stmt.executeQuery(query);
			while(rs.next())
			{
				VehicleBasedTripDto dto = new VehicleBasedTripDto();
				
				dto.setEmpname(rs.getString("displayname"));
				dto.setPersonalno(rs.getString("PersonnelNo"));
				dto.setEmpid(rs.getString("id"));
				dto.setEstat(rs.getString("estat"));
				
				dtoList.add(dto);
			}
			return dtoList;
		} catch (SQLException e) {
			System.err.println("Report:error in VehicleEmployeeDao"+e);
		}
			finally{
					DbConnect.closeResultSet(rs);
				DbConnect.closeStatement(stmt);
				DbConnect.closeConnection(con);
			}
		return dtoList;
	
	
    }
	
	
public ArrayList<VehicleBasedTripDto> getTripStatus(String siteId,String fromDate, String toDate, String logtype ,String tripTime,String status){
		
		DbConnect dbConn = DbConnect.getInstance();
		Connection con = null;
		Statement stmt=null,st=null;
		ResultSet rs=null,rs1=null;
		String fromdate=OtherFunctions.changeDateFromatToIso(fromDate);
		String todate=OtherFunctions.changeDateFromatToIso(toDate);
		ArrayList<VehicleBasedTripDto> dtoList = new ArrayList<VehicleBasedTripDto>();
		con = dbConn.connectDB();
		String temp;
		String order;
		String apl="";
		String logtypequery="";
		if(!logtype.equalsIgnoreCase("ALL"))
		{
			logtypequery = " and t.trip_log = '"+logtype+"'";
		}
		String logtimequery="";
		if(!tripTime.equalsIgnoreCase("ALL"))
		{
			logtimequery = " and t.trip_time = '"+tripTime+"'";
		}
		String statusquery="";
		if(!status.equalsIgnoreCase("ALL"))
		{
			statusquery = " and v.approvalStatus = '"+status+"'";
		}
		

		String query = "select t.id,t.trip_code,date_format(t.trip_date,'%d/%m/%Y')  trip_date,t.trip_time,t.trip_log,vt.type,v.vehicleNo,v.approvalStatus,tb.tripTripRate,tb.tripEscortRate,count(tc.employeeId) count  from trip_details t join trip_details_child tc on t.id=tc.tripId join  vendor_trip_sheet_parent v on t.id=v.tripId join vehicle_type vt  on t.vehicle_type=vt.id left outer join  td_billing_args tb    on tb.tripId=t.id  where  (t.trip_date between '"+fromdate+"' and '"+todate+"')"+logtypequery+logtimequery+statusquery+" group by t.id";
		
		System.out.println("Query "+query);
		try {			
			stmt = con.createStatement();
			rs = stmt.executeQuery(query);
			while(rs.next())
			{
				 order="1";
				if( rs.getString("trip_log").equalsIgnoreCase("OUT"))
				  {
					   order=rs.getString("count");
				  }
				 st = con.createStatement();
				    String aplqry="select a.area,p.place,l.landmark from landmark l,area a,place p where a.id=p.area and p.id=l.place and l.id =(select landmarkId from trip_details_child where tripid ="+rs.getString("id")+"  and routedorder ="+order+")";
					rs1=st.executeQuery(aplqry);
					if(rs1.next()){
						apl=rs1.getString("area")+" - ";
						apl=apl+rs1.getString("place")+" - ";
						apl=apl+rs1.getString("landmark");
					}
				
				
				VehicleBasedTripDto dto = new VehicleBasedTripDto();
				dto.setSiteId(siteId);
				dto.setTripCode(rs.getString("trip_code"));
				dto.setTripDate(rs.getString("trip_date"));
				dto.setTripLog(rs.getString("trip_log"));
				dto.setTripTime(rs.getString("trip_time"));
				dto.setType(rs.getString("type"));
				dto.setVehicle(rs.getString("vehicleNo"));
				dto.setApprovalStatus(rs.getString("approvalStatus"));
				dto.setTripAPL(apl);
				temp =rs.getString("tripTripRate");
				temp= temp == null ? "" : temp;
				dto.setTripRate(temp);
				dto.setEscortRate(rs.getString("tripEscortRate"));
				
				dtoList.add(dto);
		       
				
			
			
			
			
			
			
			
			}
			return dtoList;
		} catch (SQLException e) {
			System.err.println("Report:error in VehicleBasedTripDao"+e);
		}
		finally{
		DbConnect.closeResultSet(rs,rs1);
		DbConnect.closeStatement(stmt,st);
		DbConnect.closeConnection(con);
	}

		
		return dtoList;
	}
	
	
	
public ArrayList<VehicleBasedTripDto> getShuttleTripdetails(String siteId,String fromDate, String toDate, String vehicle){
		
		DbConnect dbConn = DbConnect.getInstance();
		Connection con = null;
		Statement stmt=null,st = null;
		ResultSet rs=null,rs1 = null;
		String travelFromDate=OtherFunctions.changeDateFromatToIso(fromDate);
		String traveltoDate=OtherFunctions.changeDateFromatToIso(toDate);
		String Starttime;
		String Stoptime;

		
		ArrayList<VehicleBasedTripDto> dtoList = new ArrayList<VehicleBasedTripDto>();
		con = dbConn.connectDB();
		
	    String	query = "select td.id,td.trip_code,date_format(td.trip_date,'%d/%m/%Y')  trip_date ,td.trip_log,td.trip_time,vtsp.startTime,vtsp.stopTime,v.regno , r.routeName , count(e.id) as actemp,count(temp.id) as inemp from trip_details td join vehicle_type vt on vt.id=td.vehicle_type join vendor_trip_sheet_parent vtsp on td.id=vtsp.tripId join vendor_trip_sheet vts	on vtsp.tripId=vts.tripId join vehicles v on v.id=td.vehicle join route r on td.routeid=r.id join  employee e on vts.employeeId=e.id left outer join (select * from employee) temp  on vts.employeeId=temp.id and vts.showstatus='Show' where (td.trip_date between '"+travelFromDate+"' and '"+traveltoDate+"')  group by td.id ";
		
		System.out.println("Query "+query);
		try {			
			stmt = con.createStatement();
			rs = stmt.executeQuery(query);
			while(rs.next())
			{
				Starttime=rs.getString("startTime");
				Stoptime=rs.getString("stopTime");
			   
			   
			   
				if ((Starttime == null)|| Starttime.equalsIgnoreCase("null"))
				{
					Starttime = "";
				}
				if ((Stoptime == null)|| Stoptime.equalsIgnoreCase("null"))
				{
					Stoptime = "";
				}
				
				
				VehicleBasedTripDto dto = new VehicleBasedTripDto();
				
				dto.setVehicle(rs.getString("regno"));	
				dto.setRoute(rs.getString("routeName"));	
				dto.setTripId(rs.getString("id"));
				dto.setTripCode(rs.getString("trip_code"));
				dto.setTripDate(rs.getString("trip_date"));
				dto.setTripLog(rs.getString("trip_log"));
				dto.setTripTime(rs.getString("trip_time"));
				dto.setStarttime(Starttime);
				dto.setStopTime(Stoptime);
				dto.setActemp(rs.getString("actemp"));
				
			 	 
				dtoList.add(dto);
			}
		} catch (SQLException e) {
			System.err.println("Report:error in VehicleBasedTripDao"+e);
		}
		finally{
			DbConnect.closeResultSet(rs,rs1);
		DbConnect.closeStatement(stmt,st);
		DbConnect.closeConnection(con);
	}

		
		return dtoList;
	}
	
	
}
	

	
	

