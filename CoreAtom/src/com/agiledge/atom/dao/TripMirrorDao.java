package com.agiledge.atom.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import com.agiledge.atom.commons.RandomString;
import com.agiledge.atom.dbConnect.DbConnect;
import com.agiledge.atom.dto.RouteDto;

public class TripMirrorDao {


	public Boolean checkTripWithDate(String dateinsql, String tripLog,String tripTime) {
		DbConnect ob = null;
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		boolean flag=false;
		try{
			ob=DbConnect.getInstance();
			con=ob.connectDB();
			st=con.createStatement();
			String selectqry="";
			if(tripLog.equalsIgnoreCase("ALL") && tripTime.equalsIgnoreCase("ALL")){
				selectqry="select id from trip_details where trip_date='"+dateinsql+"'";
			}else{
				selectqry="select id from trip_details where trip_date='"+dateinsql+"' and trip_log='"+tripLog+"' and trip_time='"+tripTime+"'";
			}
			rs=st.executeQuery(selectqry);
			if(rs.next()){
				flag=true;
			}
		} catch (Exception e) {
			System.out.println("Error in TripMirrorDao@checkTripWithDate");
		}finally{
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);
		}
		return flag;
	}

	public int generatefixedRouteTrips(String travelDate, String tripLog,
			String tripTime,String siteid) {
		System.out.println("logggggggggg"+tripLog);
		DbConnect ob = null;
		Connection con = null;
		Statement st = null,st1=null,st2=null,st3=null;
		ResultSet rs = null,rs1=null,rs2=null,rs3=null;
		PreparedStatement st4=null;
		int result=0;
		try{
			ob=DbConnect.getInstance();
			con=ob.connectDB();
			ArrayList<String> empids= new ArrayList<String>();
			String subqry="",subqy2="";
			if(tripLog.equalsIgnoreCase("IN")){
				subqry="log_in='"+tripTime+"'";
			}else{
				subqry="log_out='"+tripTime+"'";
			}
			String tdqry="select distinct(routein) as empid from employee_tripping where  routein!=0  and "+subqry;
			System.out.println("empsss"+tdqry);
			st=con.createStatement();
			rs=st.executeQuery(tdqry);
			while(rs.next()){
			
			String getRouteDetails="select * from  route where id ="+rs.getString("empid")+"";
			st1=con.createStatement();
			System.out.println("details "+getRouteDetails);
			rs1=st1.executeQuery(getRouteDetails);
			RouteDto routetdto = new RouteDto();
			while(rs1.next()){
				routetdto.setRouteId(rs1.getInt("id"));
				
				
				String vType="";
				if(rs1.getString("vehicle").equalsIgnoreCase("Indica")){
					vType="3";
				}else if(rs1.getString("vehicle").equalsIgnoreCase("Sumo")){
					vType="2";
				}else if(rs1.getString("vehicle").equalsIgnoreCase("Tempo")){
					vType="4";
				}else if(rs1.getString("vehicle").equalsIgnoreCase("Mazda")){
					vType="5";
				}else if(rs1.getString("vehicle").equalsIgnoreCase("Mini Bus")){
					vType="6";
				}else if(rs1.getString("vehicle").equalsIgnoreCase("Bus")){
					vType="7";
				}
				
				
				
				routetdto.setVehicleTypeId(vType);
				
			}
				TripDetailsDao tddao = new TripDetailsDao();
				tddao.createUniqueID(travelDate, tripLog, siteid);
				tddao.getIncremenetedUnique();
				String tripCode = tddao.uniqueId;
				String insertqry="INSERT INTO trip_details (trip_code, siteId, trip_date, trip_time, trip_log, vehicle_type, status, security_status,routingType,routeId) VALUES ('"+tripCode+"', '"+siteid+"', '"+travelDate+"', '"+tripTime+"', '"+tripLog+"', '"+routetdto.getVehicleTypeId()+"', 'routed', 'NO','o','"+routetdto.getRouteId()+"')";
				System.out.println("create trip "+insertqry);
				st2=con.createStatement();
				st2.executeUpdate(insertqry, Statement.RETURN_GENERATED_KEYS);
				rs2 = st2.getGeneratedKeys();
				int tripId=0;
				if (rs2.next()) {
					tripId = rs2.getInt(1);
					result++;
				}
				//getting Childs
				System.out.println("tdid"+tripId);
				st3=con.createStatement();
				String getchild="select * from emp_route_map where routeid="+routetdto.getRouteId()+" order by position ";
				System.out.println("getEmps"+getchild);
				rs3=st3.executeQuery(getchild);
				int routedorder=1;
				while(rs3.next()){
					float timereq=getTime(rs3.getString("compdist"));
					System.out.println(rs3.getString("empid")+"  DISSSS"+rs3.getString("compdist")+"->"+timereq);
					String insertchild="INSERT INTO trip_details_child (tripId, employeeId, routedOrder, landmarkId, time, dist, scheduleId) VALUES (?, ?, ?, '326', ?, ?, '0')";
					st4=con.prepareStatement(insertchild);
					st4.setInt(1, tripId);
					st4.setString(2, rs3.getString("empid"));
					st4.setInt(3, routedorder);
					st4.setFloat(4, timereq);
					st4.setString(5, rs3.getString("compdist"));
					st4.executeUpdate();
					routedorder++;
				}
			
			}
		} catch (Exception e) {
			System.out.println("Error in TripMirrorDao@generatefixedRouteTrips");
		}finally{
			DbConnect.closeResultSet(rs,rs1,rs2,rs3);
			DbConnect.closeStatement(st,st1,st2,st3,st4);
			DbConnect.closeConnection(con);
		}
		
		return result;
	}
	public float getTime(String distance) {

		DbConnect ob = DbConnect.getInstance();
		Connection con = null;
		ResultSet rs = null;
		Statement st = null;
		float time = 0;
		con = ob.connectDB();
		try {
			st = con.createStatement();
			String gettingTime = "select ROUND( "
					+ distance
					+ "/speedpkm*60,0)as time from timeSloat where CONCAT(startTime,':00') <= CONCAT(curtime(),'') and CONCAT(endTime,':00') > CONCAT(curtime(),'')";
			rs = st.executeQuery(gettingTime);
			if (rs.next()) {
				time = rs.getFloat("time");
			}

		} catch (Exception e) {
			System.out.println("Error in TripMirrorDao@getTime" + e);
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);
		}
		return time;
	}

	public Boolean checkTripWithDateRange(String fromdateinsql, String todateinsql) {
		DbConnect ob = null;
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		boolean flag=false;
		try{
			ob=DbConnect.getInstance();
			con=ob.connectDB();
			st=con.createStatement();
			String selectqry="select id from trip_details where trip_date  between '"+fromdateinsql+"' and '"+todateinsql+"'";
			
			rs=st.executeQuery(selectqry);
			if(rs.next()){
				flag=true;
			}
		} catch (Exception e) {
			System.out.println("Error in TripMirrorDao@checkTripWithDate");
		}finally{
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);
		}
		return flag;
	}

	public int mirrorFixedTrips(String siteId, String sourcedate,
			String fromdate, String todate) {
		DbConnect ob = null;
		Connection con = null;
		Statement st = null,st1=null,st2=null;
		ResultSet rs = null,rs1=null,rs2=null;
		PreparedStatement pst=null,pst1=null,pst2=null,pst3=null,pst4=null;
		int result=0;
		try{
			ob=DbConnect.getInstance();
			con=ob.connectDB();
			st=con.createStatement();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Calendar c = Calendar.getInstance();
			c.setTime(sdf.parse(fromdate));
			Calendar c1 = Calendar.getInstance();
			c1.setTime(sdf.parse(todate));
			c1.add(Calendar.DATE, 1);
			todate = sdf.format(c1.getTime());
			while(!fromdate.equalsIgnoreCase(todate)){
				String getTripDetails="select * from trip_details where siteid="+siteId+" and trip_date='"+sourcedate+"'";
				rs=st.executeQuery(getTripDetails);
				while(rs.next()){
					TripDetailsDao tddao = new TripDetailsDao();
					tddao.createUniqueID(fromdate, rs.getString("trip_log"), siteId);
					tddao.getIncremenetedUnique();
					String tripCode = tddao.uniqueId;
					
					String insertqry="INSERT INTO trip_details (trip_code, siteId, trip_date, trip_time, trip_log, vehicle_type, status, security_status,routingtype,vehicle,driverid,driverPswd,routeId) VALUES ('"+tripCode+"', '"+siteId+"', '"+fromdate+"', '"+rs.getString("trip_time")+"', '"+rs.getString("trip_log")+"', '"+rs.getString("vehicle_type")+"', 'saved', '"+rs.getString("security_status")+"' ,'o','"+rs.getString("vehicle")+"','"+rs.getString("driverid")+"','1234','"+rs.getString("routeId")+"')";
					
					st1=con.createStatement();
					st1.executeUpdate(insertqry, Statement.RETURN_GENERATED_KEYS);
					rs1 = st1.getGeneratedKeys();
					int tripId=0;
					if (rs1.next()) {
						tripId = rs1.getInt(1);
						result++;
						RandomString rd = new RandomString(4);
						 String password= rd.nextDriverString( tripId+"");
						 String driverpswd = "update trip_details set driverPswd="+password+" where id= "+tripId;
						 System.out.println(driverpswd);
						 pst4=con.prepareStatement(driverpswd);
						 pst4.executeUpdate(); 
					      
						
						
						
						
						
						
						
						String vendorassign="INSERT INTO tripvendorassign (tripId, vendorid, status) VALUES (?, '1', 'a')";
						pst1=con.prepareStatement(vendorassign);
						pst1.setInt(1, tripId);
						pst1.executeUpdate();
							
						
					
						
					
						
						String vendortripparent="INSERT INTO vendor_trip_sheet_parent (tripId, vehicleNO, escort,status,downloadStatus) VALUES (?,?,?,?,?)";
						pst2=con.prepareStatement(vendortripparent);
						pst2.setInt(1, tripId);
						pst2.setString(2, rs.getString("vehicle"));
						pst2.setString(3, "NO");
						pst2.setString(4, "initial");
						pst2.setString(5, "ready");
						
						pst2.executeUpdate();
						
					
					
					String getchild="select * from trip_details_child where tripid="+rs.getString("id");
					st2=con.createStatement();
					rs2=st2.executeQuery(getchild);
					while(rs2.next()){
						String insertchild="INSERT INTO trip_details_child (tripId, employeeId, routedOrder, landmarkId, time, dist, scheduleId) VALUES (?, ?, ?, '3367', ?, ?, '0')";
						pst=con.prepareStatement(insertchild);
						pst.setInt(1, tripId);
						pst.setString(2, rs2.getString("employeeId"));
						pst.setInt(3, rs2.getInt("routedOrder"));
						pst.setFloat(4, rs2.getFloat("time"));
						pst.setString(5, rs2.getString("dist"));
						pst.executeUpdate();
						
						
						
						String vendortripchild="INSERT INTO vendor_trip_sheet (tripId, employeeId, insertedorder, showstatus) VALUES (?, ?, ?, 'No Show')";
						pst3=con.prepareStatement(vendortripchild);
						pst3.setInt(1, tripId);
						pst3.setString(2, rs2.getString("employeeId"));
						pst3.setInt(3, rs2.getInt("routedOrder"));
						pst3.executeUpdate();
						
				
					}
						
						
					}
				}
				
				c.add(Calendar.DATE, 1);  // number of days to add
				fromdate = sdf.format(c.getTime());
				System.out.println(fromdate);
			}
			
		} catch (Exception e) {
			System.out.println("Error in TripMirrorDao@mirrorFixedTrips");
			e.printStackTrace();
		}finally{
			DbConnect.closeResultSet(rs,rs1,rs2);
			DbConnect.closeStatement(st,st1,st2,pst3,pst2,pst1,pst);
			DbConnect.closeConnection(con);
		}
		return result;
	}


}
