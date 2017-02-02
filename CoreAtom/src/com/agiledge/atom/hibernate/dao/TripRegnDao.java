package com.agiledge.atom.hibernate.dao;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import com.agiledge.atom.commons.OtherFunctions;
import com.agiledge.atom.constants.SettingsConstant;
import com.agiledge.atom.dao.APLDao;
import com.agiledge.atom.dbConnect.DbConnect;
import com.agiledge.atom.dto.APLDto;
import com.agiledge.atom.dto.RoutingDto;
import com.agiledge.atom.dto.TripDetailsChildDto;
import com.agiledge.atom.dto.TripDetailsDto;
import com.agiledge.atom.trip.dto.AplDistanceDto;

public class TripRegnDao {

	int uniqueNo = 1;

	public ArrayList<RoutingDto> getAllroutingDtos(String siteid,
			String tripDate, String tripLog, String tripTime) {
		DbConnect ob = DbConnect.getInstance();
		Connection con = ob.connectDB();
		PreparedStatement pst = null;
		ResultSet rs = null;
		ArrayList<RoutingDto> routingDtos = new ArrayList<RoutingDto>();
		RoutingDto dto = null;
		// TODO Auto-generated method stub
		// String
		// query="(select distinct table1.login_time as time,'IN' as logtype from (select login_time from employee_schedule where from_date<=? and to_date>=?  union select login_time from employee_schedule_alter where date=?) as table1 where table1.login_time not in (select trip_details.trip_time from trip_details where trip_date=? and trip_log='IN'))";
		// String
		// query1="(select distinct table2.logout_time as time,'OUT' as logtype from (select logout_time from employee_schedule where from_date<=? and to_date>=?  union select logout_time from employee_schedule_alter where date=?) as table2 where table2.logout_time not in (select trip_details.trip_time from trip_details where trip_date=? and trip_log='OUT'))";
		String query = "(select distinct table1.login_time as time,'IN' as logtype,table1.bookType from (select login_time,'shift' as  bookType from employee_schedule where from_date<=? and to_date>=? and status='a'  union select login_time,'shift' as  bookType from employee_schedule_alter where date=?  and status='a' union select startTime login_time,'adhoc' as  bookType from adhocBooking where adhocType='"
				+ SettingsConstant.SHIFT_EXTENSTION
				+ "' and siteId="
				+ siteid
				+ " and travelDate=? and pickDrop='IN') as table1 where table1.login_time not in (select trip_details.trip_time from trip_details where trip_date=?  and trip_log='IN' and siteId="
				+ siteid + ") order by table1.login_time )";

		String query1 = "(select distinct table2.logout_time as time,'OUT' as logtype,table2.bookType from (select logout_time,'shift' as  bookType from employee_schedule where from_date<=? and to_date>=? and status='a' union select logout_time,'shift' as  bookType from employee_schedule_alter where date=? and status='a' union select startTime as login_time,'adhoc' as  bookType from adhocBooking where adhocType='"
				+ SettingsConstant.SHIFT_EXTENSTION
				+ "' and siteId="
				+ siteid
				+ " and travelDate=? and pickDrop='OUT') as table2 where table2.logout_time not in (select trip_details.trip_time from trip_details where trip_date=? and trip_log='OUT' and siteId="
				+ siteid + ") order by table2.logout_time )";
		String queryLastpart = " as td where time!='' and time!='null' and time!='none' and time!='' and time!='weekly off' and time is not null "
				+ " and time+logtype not in (select trip_time+trip_log from trip_details where siteid="
				+ siteid
				+ " and trip_date='"
				+ tripDate
				+ "')"
				+ "group by logtype,time order by time,logtype ";
		String queryFrist = "select * from  ";

		try {
		//	System.out.println(" eththunnu...........tripTime:" + tripTime);
			if (!(tripTime.equalsIgnoreCase("all"))) {
				dto = new RoutingDto();
				dto.setDate(tripDate);
				dto.setTravelMode(tripLog);
				dto.setTime(tripTime);
				routingDtos.add(dto);
			} else {
				if (tripLog.equalsIgnoreCase("All")) {
					pst = con.prepareStatement(queryFrist + "(" + query
							+ " union " + query1 + " ) " + queryLastpart);
				//	System.out.println(queryFrist + "(" + query + " union "
					//		+ query1 + " ) " + queryLastpart);
					pst.setString(1, tripDate);
					pst.setString(2, tripDate);
					pst.setString(3, tripDate);
					pst.setString(4, tripDate);
					pst.setString(5, tripDate);
					pst.setString(6, tripDate);
					pst.setString(7, tripDate);
					pst.setString(8, tripDate);
					pst.setString(9, tripDate);
					pst.setString(10, tripDate);
				} else if (tripLog.equalsIgnoreCase("IN")
						&& tripTime.equalsIgnoreCase("all")) {
			//		System.out.println(queryFrist + query + queryLastpart);
					pst = con.prepareStatement(queryFrist + query
							+ queryLastpart);
					pst.setString(1, tripDate);
					pst.setString(2, tripDate);
					pst.setString(3, tripDate);
					pst.setString(4, tripDate);
					pst.setString(5, tripDate);
				} else if (tripLog.equalsIgnoreCase("OUT")
						&& tripTime.equalsIgnoreCase("all")) {
			//		System.out.println(queryFrist + query1 + queryLastpart);
					pst = con.prepareStatement(queryFrist + query1
							+ queryLastpart);
					pst.setString(1, tripDate);
					pst.setString(2, tripDate);
					pst.setString(3, tripDate);
					pst.setString(4, tripDate);
					pst.setString(5, tripDate);
				}
				rs = pst.executeQuery();
				while (rs.next()) {
					dto = new RoutingDto();
					dto.setDate(tripDate);
					dto.setTravelMode(rs.getString("logtype"));
					dto.setTime(rs.getString("time"));
					dto.setTransportType(rs.getString("bookType"));
				//	System.out.println("Time :" + dto.getTravelMode() + "-"
				//			+ dto.getTime());
					routingDtos.add(dto);
				}
			}

		} catch (Exception e)

		{
			System.out.println(routingDtos.size()
					+ "ERROR in route dtolist dao" + e);
		} finally {
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);

		}

		return routingDtos;
	}

	/*
	 * public ArrayList<TripDetailsDto> getTripForRegen(String siteId,String
	 * tripDate,String tripTime,String tripLog) { try { SessionFactory
	 * sessionFactory= HibernateUtil.getSessionFactory();
	 * 
	 * Session session = sessionFactory.openSession();
	 * 
	 * session.getTransaction().begin(); String subQuery = ""; String SelQuery =
	 * " FROM TripDetailsDto t inner join VehicleTypeDto vton.t.vehicle_ype=vt.id where t.id='22307'"
	 * ;// t.siteId=:siteId and (t.status='saved' or t.status='addsave' or
	 * t.status='saveedit') "; if (tripLog.equals("ALL")) { subQuery =
	 * " and t.trip_dateDate=:tripDate";
	 * 
	 * } else if (tripTime.equals("ALL")) { subQuery =
	 * " and t.trip_dateDate<='"+tripDate+"'" + " and t.trip_log='" + tripLog +
	 * "' "; } else { subQuery = " and t.trip_date='"+tripDate+"'" +
	 * " and t.trip_log='" + tripLog + "' and t.trip_time='" + tripTime + "'"; }
	 * String lastQuery = " order by t.id ";
	 * 
	 * //Quer System.out.println("AAAAAAAAAAAAAAAAAAA"); Query
	 * query1=session.createQuery(SelQuery); // query1.setParameter("siteId",
	 * siteId);
	 * 
	 * System.out.println("AAAAAAAAAAAAAAAAAAA"); ArrayList<TripDetailsDto>
	 * dtos=(ArrayList<TripDetailsDto>) query1.list();
	 * System.out.println("BBBBBBB"); // org.hibernate.Query query =
	 * session.createQuery(SelQuery); // query.setParameter("siteId", siteId);
	 * // ArrayList<TripDetailsDto> dtos=(ArrayList<TripDetailsDto>)
	 * query.list(); System.out.println(dtos.size());
	 * System.out.println(dtos.get
	 * (0).getId()+"    "+dtos.get(0).getTravelTime()+
	 * " "+dtos.get(0).getTrip_date
	 * ()+" "+dtos.get(0).getTrip_log()+" - "+dtos.get
	 * (0).getTrip_time()+" "+dtos
	 * .get(0).getSiteId()+"  "+dtos.get(0).getVehicle_type());
	 * 
	 * } catch(Exception e) { System.out.println("Exception "+e); } return null;
	 * }
	*/	

	public ArrayList<TripDetailsChildDto> getRemainingEmpList(String tripDate,
			String remEMpString) {

		DbConnect ob = DbConnect.getInstance();
		Connection con = ob.connectDB();
		ResultSet rs = null;
		PreparedStatement pst = null;
		ArrayList<TripDetailsChildDto> dtoList = new ArrayList<TripDetailsChildDto>();
		try {
			String query = "select e.personnelNo, e.id,e.displayname,es.landmark as landmarkid,a.area,p.place,l.landmark,esh.id as scheduleId from employee e, employee_subscription es ,employee_schedule esh,area a,place p,landmark l where e.id in ("+remEMpString+") and es.subscriptionid=esh.subscription_id and esh.from_date<='"+tripDate+"' and esh.to_date>='"+tripDate+"' and e.id=es.empid and es.subscriptionstatus in ('subscribed','pending') and es.landmark=l.id and l.place=p.id and p.area=a.id   ";
		//	System.out.println(query);
			pst=con.prepareStatement(query);
			rs = pst.executeQuery();
			while (rs.next()) {
				TripDetailsChildDto dto = new TripDetailsChildDto();
				dto.setEmployeeId(rs.getString("id"));
				dto.setEmployeeName(rs.getString("displayname"));
				dto.setLandmarkId(rs.getString("landmarkid"));
				dto.setPersonnelNo(rs.getString("personnelNo"));
				dto.setArea(rs.getString("area"));
				dto.setPlace(rs.getString("place"));
				dto.setLandmark(rs.getString("landmark"));
				dto.setScheduleId(rs.getString("scheduleId"));
				dto.setDistance((float) 0.0);
				System.out.println(dto.getDistance());
				dto.setTime("");
				dtoList.add(dto);
			}

		} catch (Exception e) {
			System.out.println("Exception " + e);
		} finally {

			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);

		}
		return dtoList;

	}

	public int insertTrip(ArrayList<TripDetailsDto> tripDetailsDtoList) {
		String uniqueId = "" + uniqueNo;
		uniqueNo++;
		DbConnect ob = DbConnect.getInstance();
		Connection con = ob.connectDB();
		ResultSet rs = null,rs2=null,rs3=null;
		PreparedStatement pst = null,pst2=null;
		PreparedStatement pst1 = null;
		Statement st=null,st1=null;
		String insertedTripId = null;		
		int retVal = 0;
				
		try {
			
			for (TripDetailsDto tripDetailsDtoObj : tripDetailsDtoList) {
				System.out.println("date  "+tripDetailsDtoObj.getSiteId()+"   "+tripDetailsDtoObj.getTrip_date()+"  "+tripDetailsDtoObj.getTrip_log()+"  "+tripDetailsDtoObj.getTrip_time());
				st=con.createStatement();
				st.execute("delete from trip_details_child where tripid in (select id from trip_details where siteId="+tripDetailsDtoObj.getSiteId()+" and trip_date='"+tripDetailsDtoObj.getTrip_date()+"' and trip_log='"+tripDetailsDtoObj.getTrip_log()+"' and trip_time='"+tripDetailsDtoObj.getTrip_time()+"')");
		
				st1 = con.createStatement();
					st1.execute	("delete from trip_details where siteId="+tripDetailsDtoObj.getSiteId()+" and trip_date='"+tripDetailsDtoObj.getTrip_date()+"' and trip_log='"+tripDetailsDtoObj.getTrip_log()+"' and trip_time='"+tripDetailsDtoObj.getTrip_time()+"'");
			
				
				}
			
			pst=null;
			pst = con.prepareStatement("insert into trip_details(trip_code,siteId,trip_date,trip_time,trip_log,routeId,vehicle_type,status,security_status,routingType,distance,travelTime) values(?,?,?,?,?,?,?,?,?,?,?,?)",Statement.RETURN_GENERATED_KEYS);
			for (TripDetailsDto tripDetailsDtoObj : tripDetailsDtoList) {
				pst.setString(1, uniqueId);
				pst.setString(2, tripDetailsDtoObj.getSiteId());
				pst.setString(3, tripDetailsDtoObj.getTrip_date());
				pst.setString(4, tripDetailsDtoObj.getTrip_time());
				pst.setString(5, tripDetailsDtoObj.getTrip_log());
				pst.setString(6, tripDetailsDtoObj.getRouteId());
				pst.setString(7, tripDetailsDtoObj.getVehicleTypeId());
				pst.setString(8, "routed");
				pst.setString(9, tripDetailsDtoObj.getIsSecurity());
				pst.setString(10, "o");
				pst.setString(11, tripDetailsDtoObj.getDistance());
				pst.setString(12, tripDetailsDtoObj.getTravelTime());
				System.out.println(" inserted status :" +		  		pst.executeUpdate());
		 		rs = pst.getGeneratedKeys();
				if (rs.next()) {
					insertedTripId = rs.getString(1);
				}
				int routedOrder=1;
				pst1 = con.prepareStatement("insert into trip_details_child(tripId,employeeId,routedOrder,landmarkId,scheduleId,time,dist,transportType) values(?,?,?,?,?,?,?,?)");
				for (TripDetailsChildDto tripDetailsChildDtoObj : tripDetailsDtoObj
						.getTripDetailsChildDtoList()) {
					try{
					tripDetailsChildDtoObj.setRoutedOrder(Integer.toString(routedOrder));
					pst1.setString(1, insertedTripId);
					pst1.setString(2, tripDetailsChildDtoObj.getEmployeeId());
					pst1.setString(3, tripDetailsChildDtoObj.getRoutedOrder());
					pst1.setString(4, tripDetailsChildDtoObj.getLandmarkId());
					pst1.setString(5, tripDetailsChildDtoObj.getScheduleId());
					pst1.setString(6, tripDetailsChildDtoObj.getTime());
					pst1.setFloat(7, tripDetailsChildDtoObj.getDistance());
					pst1.setString(8, tripDetailsChildDtoObj.getTransportType());
					retVal += pst1.executeUpdate();
					routedOrder++;
					}catch(Exception e)
					{
						System.out.println("Error in trip child saving for trip mirror"+e);						
					}
				}
			}
		}

		catch (Exception e) {
			System.out.println("Exception " + e);
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(pst,pst1,st1,st);
			DbConnect.closeConnection(con);

		}
		return retVal;
	}

	public String getSitelandmark(String siteId,Connection con)

	{

		PreparedStatement pst = null;
		String siteLandmarkId = null;
		try {
			pst = con.prepareStatement("select landmark from site where id="
					+ siteId);
			ResultSet rs = pst.executeQuery();
			if (rs.next()) {
				siteLandmarkId = rs.getString(1);
			}

		} catch (Exception e) {
			System.out.println("error in update" + e);
		} finally {
			DbConnect.closeStatement(pst);

		}
		return siteLandmarkId;
	}
/*
	public boolean isDistanceInDb() {
		return distanceInDb;
	}

	public void setDistanceInDb(boolean distanceInDb) {
		this.distanceInDb = distanceInDb;
	}

	public ArrayList<AplDistanceDto> getLandmarkDistanceList() {
		return landmarkDistanceList;
	}

	public void setLandmarkDistanceList(ArrayList<AplDistanceDto> landmarkDistanceList) {
		this.landmarkDistanceList = landmarkDistanceList;
	}
*/

	public void updateDsitances(TripDetailsDto tripDto) {
		PreparedStatement pst = null;
		ResultSet rs = null;
		DbConnect ob=DbConnect.getInstance();
		Connection con=ob.connectDB();
		
		String query = "select distance from distchart where (srcId=? and destId=?) or (destId=? and srcId=?)";
		try {
			String siteLandmarkId =getSitelandmark(tripDto.getSiteId(),con);
			pst = con.prepareStatement(query);			
				ArrayList< AplDistanceDto> aplDistanceList = new ArrayList< AplDistanceDto> (); 
				ArrayList< AplDistanceDto> landmarkDistanceList = new ArrayList< AplDistanceDto> ();	
			float totalDistance = 0;
			float distanceTemp =0f;
			boolean setDistanceInDb=true;
			String srcId;
			String destId = null;
			if(tripDto.getTrip_log().equalsIgnoreCase("OUT" )) {
				for (int i =0 ; i <tripDto.getTripDetailsChildDtoList().size(); i++) {				
					if(i==0) {
						srcId=siteLandmarkId;
						AplDistanceDto aplDto1 = new AplDistanceDto();
						aplDto1.setSourceId(String.valueOf(srcId)); 
						aplDto1.setDistance(0); 
						landmarkDistanceList.add(aplDto1);
					} else {
						srcId=tripDto.getTripDetailsChildDtoList().get(i-1).getLandmarkId();
						//destId = landmarks[i-1];
					}
						destId = tripDto.getTripDetailsChildDtoList().get(i).getLandmarkId();
 
						
						pst.setString(1, srcId);
						pst.setString(2, destId);
						pst.setString(3, srcId);
						pst.setString(4, destId);
						
						rs = pst.executeQuery();
						if (rs.next()) {
							tripDto.getTripDetailsChildDtoList().get(i).setDistance(rs.getFloat(1));
							totalDistance += rs.getFloat(1);
						} else {
							 
							if(srcId!=destId) {  
								setDistanceInDb=false;
							}
						}
						AplDistanceDto aplDto1 = new AplDistanceDto();
						aplDto1.setSourceId(String.valueOf(destId)); 
						aplDto1.setDistance(tripDto.getTripDetailsChildDtoList().get(i).getDistance()); 
						landmarkDistanceList.add(aplDto1);						
						
							/* src, dest setup for db insert */
							AplDistanceDto aplDDto = new AplDistanceDto();
							aplDDto.setSourceId( String.valueOf(srcId) );
							aplDDto.setTargetId(String.valueOf(destId));
							aplDDto.setDistance(tripDto.getTripDetailsChildDtoList().get(i).getDistance());
							aplDistanceList.add(aplDDto);
							
						 
						 
				}
							
			
			} else if(tripDto.getTrip_log().equalsIgnoreCase("IN" )) {
				for (int i = 0; i<=tripDto.getTripDetailsChildDtoList().size(); i++) {					
					if(i==0) {														
						tripDto.getTripDetailsChildDtoList().get(i).setDistance(0);
						AplDistanceDto aplDto1 = new AplDistanceDto();
						aplDto1.setSourceId(tripDto.getTripDetailsChildDtoList().get(i).getLandmarkId());
						aplDto1.setDistance(0); 
						landmarkDistanceList.add(aplDto1);												
					}
					else
					{
					srcId = tripDto.getTripDetailsChildDtoList().get(i-1).getLandmarkId();
					if(i==tripDto.getTripDetailsChildDtoList().size())
					{
							
						destId=siteLandmarkId;
					}
					else
					{
					destId=tripDto.getTripDetailsChildDtoList().get(i).getLandmarkId();
					}					
						pst.setString(1, srcId);
						pst.setString(2, destId);
						pst.setString(3, srcId);
						pst.setString(4, destId);						
						rs = pst.executeQuery();
						distanceTemp =0f;
						if (rs.next()) {									
							distanceTemp = rs.getFloat(1);
							totalDistance += distanceTemp;
						System.out.println("Source :"+srcId+"  destId"+destId+" distance:"+distanceTemp+" total distance "+totalDistance);
							if(i!=tripDto.getTripDetailsChildDtoList().size())
							{
							tripDto.getTripDetailsChildDtoList().get(i).setDistance(distanceTemp);
							}							
						} else {
							 
							if(srcId!=destId) {  
								setDistanceInDb=false;
								distanceTemp=0;
							}
						}					
						AplDistanceDto aplDto2 = new AplDistanceDto();
						aplDto2.setSourceId(String.valueOf(destId));
						aplDto2.setDistance(distanceTemp); 
						landmarkDistanceList.add(aplDto2);
						
						/* src, dest setup for db insert */
						AplDistanceDto aplDDto = new AplDistanceDto();
						aplDDto.setSourceId( String.valueOf(srcId) );
						aplDDto.setTargetId(String.valueOf(destId));
						aplDDto.setDistance(distanceTemp);
						aplDistanceList.add(aplDDto);
					}
				}
			}
						
						
						
						
						
					
				
			
				
			
			if(setDistanceInDb==false) {
				System.out.println(landmarkDistanceList.size());				
				getMapDistances(tripDto,aplDistanceList,landmarkDistanceList);				
			}
			else
			{
			tripDto.setDistance(Float.toString(totalDistance));	
			}
		}catch(Exception e)
		
		{
		System.out.println("error in "+e);
		}
		finally
		{
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);
		}
		}
	

public void getMapDistances(TripDetailsDto tripDto, ArrayList<AplDistanceDto> aplDistanceList, ArrayList<AplDistanceDto> landmarkDistanceList) {
		
		float totalDistance = 0.0f;
		HashMap<String, APLDto> aplMap = new HashMap<String, APLDto>();
		int landmarkDupe[] = new int[landmarkDistanceList.size()];
		try {
			int ctx=0;
		 	for(AplDistanceDto addto : landmarkDistanceList ) {
		 		landmarkDupe[ctx] =Integer.parseInt( addto.getSourceId());
		 		ctx++;
		 	}
		 	 
		aplMap = new APLDao().getAllAPL(landmarkDupe);
		int totalPoints = landmarkDistanceList.size();		  
		String sourceLatLng= aplMap.get(landmarkDistanceList.get(0).getSourceId()).getLattitude() + ","+ aplMap.get(landmarkDistanceList.get(0).getSourceId()).getLongitude();
		String destinationLatLng=aplMap.get(landmarkDistanceList.get(totalPoints-1).getSourceId()).getLattitude() + ","+ aplMap.get(landmarkDistanceList.get(totalPoints-1).getSourceId()).getLongitude();		 
		String wayPointLatLngs = "";		
		for(int i = 1; i< landmarkDistanceList.size()-1; i++) {
			if(i!=1&& i!=landmarkDistanceList.size()-1) {
				wayPointLatLngs+=URLEncoder.encode("|");
			}
			wayPointLatLngs+=  aplMap.get(landmarkDistanceList.get(i).getSourceId()).getLattitude() + ","+ aplMap.get(landmarkDistanceList.get(i).getSourceId()).getLongitude();
		}
		
		if(wayPointLatLngs.trim().equals("")==false) {
			wayPointLatLngs="&waypoints="+wayPointLatLngs;
		}
		DefaultHttpClient client1 = new DefaultHttpClient();					
	     
		String url="https://maps.googleapis.com/maps/api/directions/json?origin="+sourceLatLng+"&destination="+destinationLatLng+wayPointLatLngs;
String keyedURL=OtherFunctions.encryptTheMapKey(url);
System.out.println(keyedURL);
		HttpGet request = new HttpGet(keyedURL);
	    HttpResponse response1 = 
	    		 client1.execute(request);
	      
	     BufferedReader rd = new BufferedReader (new InputStreamReader(response1.getEntity().getContent()));
	     String line = "";
	     String nLine="";
	     while ((line = rd.readLine()) != null) {
	    	 nLine+=line;
	        }
	     JSONObject obj = new JSONObject(nLine);
	   
	   
	  int legs = ((JSONObject) obj.getJSONArray("routes").get(0)).getJSONArray("legs").length();
	  for(int i = 0 ; i < legs ; i ++) {
		  JSONObject leg = ((JSONObject) obj.getJSONArray("routes").get(0)).getJSONArray("legs").getJSONObject(i);
		  double distanceM =leg.getJSONObject("distance").getDouble("value");
		  distanceM = distanceM / 1000;
		  System.out.println("i="+i+" distance"+distanceM);
		  totalDistance +=distanceM;
		   
			  aplDistanceList.get(i).setDistance(distanceM);
			 
		   
		  if(tripDto.getTrip_log().equalsIgnoreCase("IN")) {
			  if(i==0) {
				  tripDto.getTripDetailsChildDtoList().get(0).setDistance(0);
			  } 
			  if(i<legs-1) {
				  tripDto.getTripDetailsChildDtoList().get(i+1).setDistance((float) distanceM);
			  }
		  } else if(tripDto.getTrip_log().equalsIgnoreCase("OUT")) {
			  
			  if(i<legs) {
				  tripDto.getTripDetailsChildDtoList().get(i).setDistance((float) distanceM);
			  }
		  }		   		  		 
		}
	  System.out.println("Total distance from map"+totalDistance);
			tripDto.setDistance(Float.toString(totalDistance));
	   
		}catch(Exception e) {
			System.out.println("Error in getMapDistance : "+ e);
		}	
		
	}



	public void updateTime(TripDetailsDto tripDto) {
		ResultSet rs = null;
		DbConnect ob=DbConnect.getInstance();
		Connection con=ob.connectDB();
		PreparedStatement pst = null;
		float totalMinutes = (float) 0.0;
		float currentDistance = (float) 0.0;
		String query = "select ROUND( ?/speedpkm*60,0)as time from timeSloat where startTime<=? and endTime>?";
		try {
			pst = con.prepareStatement(query);
			if (tripDto.getTrip_log().equalsIgnoreCase("IN")) {
				
				for (int i = 0; i < tripDto.getTripDetailsChildDtoList().size(); i++) {
					if(i==0)
					{
					currentDistance=Float.parseFloat(tripDto.getDistance());
					}
					currentDistance -= tripDto.getTripDetailsChildDtoList().get(i).getDistance();
					pst.setFloat(1, currentDistance);
					pst.setString(2, tripDto.getTrip_time());
					pst.setString(3,  tripDto.getTrip_time());
					rs = pst.executeQuery();
					if (rs.next()) {
						tripDto.getTripDetailsChildDtoList().get(i).setTime(Float.toString(rs.getFloat("time")));
						if(i == 0)
						totalMinutes = Float.parseFloat(tripDto.getTripDetailsChildDtoList().get(i).getTime());
					}
				}
		
			} else {
				for (int i = 0; i < tripDto.getTripDetailsChildDtoList().size(); i++) {
					pst.setFloat(1, tripDto.getTripDetailsChildDtoList().get(i).getDistance());
					pst.setString(2, tripDto.getTrip_time());
					pst.setString(3,  tripDto.getTrip_time());
					rs = pst.executeQuery();
					if (rs.next()) {
						tripDto.getTripDetailsChildDtoList().get(i).setTime(rs.getString("time"));
						totalMinutes = Float.parseFloat(tripDto.getTripDetailsChildDtoList().get(i).getTime());
					}
				}
			}
			tripDto.setTravelTime(Float.toString(totalMinutes));
		} catch (Exception e) {
			System.out.println("Error in takingtime"+e);			
		}	
		finally
		{
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);
		}
	}

	public String empsTotravelStatus="";  
	public HashMap<String,RoutingDto> getEmployeesForTravel(int siteid,String tripDate,String tripTime,String InOut)
			 {
		empsTotravelStatus="";
		// uniqueNo = 1;
	//	System.out.println("reached here");
		ResultSet rs = null;
		DbConnect	ob = DbConnect.getInstance();
	Connection	con = ob.connectDB();
		CallableStatement cst = null;
		rs = null;
		HashMap<String ,RoutingDto> routingDtoList =new LinkedHashMap<String,RoutingDto>();
	//	System.out.println(siteid+tripDate+tripTime+InOut);
		try {
			cst = con.prepareCall("{CALL getEmployeeToTravel(?,?,?,?,?,?)}");		
		cst.setInt(1, siteid);
		cst.setString(2,tripDate);
		cst.setString(3, tripTime);
		cst.setString(4, InOut);
		cst.setString(5, "o");
		cst.registerOutParameter(6, Types.INTEGER);
		cst.execute();
		int returnInt = cst.getInt(6);		
		if (returnInt == 0) {
			rs = cst.executeQuery();
			if (rs.next()) {
				
				do {
					RoutingDto rDto=new RoutingDto();
					rDto.setEmployeeId(rs.getString("employeeId"));
					rDto.setEmployeeLandmark(rs.getString("landmarkId"));
					routingDtoList.put(rDto.getEmployeeId(),rDto);
				} while (rs.next());
			}
			else
			{
				empsTotravelStatus="No employee To Travel";	
			}
		}
		else
		{
			empsTotravelStatus="Routing Done";	
		}
		} catch (Exception e) {
			System.out.println("ERRor in dao"+e);
			
		}
		finally
		{
			DbConnect.closeStatement(cst);
			DbConnect.closeConnection(con);		
		}
		return routingDtoList;
	}
}
