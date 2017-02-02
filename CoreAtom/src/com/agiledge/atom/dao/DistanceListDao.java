package com.agiledge.atom.dao;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import com.agiledge.atom.commons.OtherFunctions;
import com.agiledge.atom.dbConnect.DbConnect;
import com.agiledge.atom.dto.APLDto;
import com.agiledge.atom.dto.DistanceChartDto;
import com.agiledge.atom.dto.RoutingDto;
import com.agiledge.atom.trip.dto.AplDistanceDto;

public class DistanceListDao {
	public static int lastId=346;
	public static int uptoId=346;
	private ArrayList<String> errorMessageList = new ArrayList<String>();

	public ArrayList<DistanceChartDto> getsourceAndDestination(int srcId,
			int destId) {
		ArrayList<DistanceChartDto> sourceDest = new ArrayList<DistanceChartDto>();
		DbConnect ob = DbConnect.getInstance();
		Statement st = null;
		ResultSet rs = null;
		DistanceChartDto distanceChartDto = null;
		Connection con = ob.connectDB();
		try {
			st = con.createStatement();
			String query = "";
			while (srcId>=uptoId) {
				/*query = "select  l.id as srcId,l.latitude as srcLat,l.longitude as srcLon,l1.id as destId,l1.latitude as destLat ,l1.longitude as destLon from landmark l,landmark l1 where l.id<l1.id and l.id="
						+ srcId
						+ " and l1.id>"
						+ destId
						+ " order by l.id,l1.id limit 8";
						*/
				query="select  l.id as srcId,l.latitude as srcLat,l.longitude as srcLon,l1.id as destId,l1.latitude as destLat ,l1.longitude as destLon from landmark l,landmark l1 where l.id>l1.id and l.id="+srcId+" and l1.id>"+destId+" and l.latitude!=0 and l1.latitude!=0 order by l.id,l1.id limit 8";
				//System.out.println("query" + query);
				rs = st.executeQuery(query);
				if (rs.next()) {
					do {
						distanceChartDto = new DistanceChartDto();
						distanceChartDto.setSourceLandmarkId(rs
								.getString("srcId"));
						distanceChartDto.setSourceLattitude(rs
								.getString("srcLat"));
						distanceChartDto.setSourceLongitude(rs
								.getString("srcLon"));
						distanceChartDto.setDestinationLandmarkId(rs
								.getString("destId"));
						distanceChartDto.setDestinationLattitude(rs
								.getString("destLat"));
						distanceChartDto.setDestinationLongitude(rs
								.getString("destLon"));
						sourceDest.add(distanceChartDto);
					} while (rs.next());					
					break;
				} else {
					srcId--;
					destId = 0;
				}
			}
		
			insertIntoDB(sourceDest);
		} catch (Exception e) {
			System.out.println("error" + e);
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);
		}
		return sourceDest;

	}

	public void storeDistance(String srcid, String destid, String dist) {
		DbConnect ob = DbConnect.getInstance();
		Statement st = null;
		Connection con = ob.connectDB();
		try {
			float distance = Float.parseFloat(dist);
			st = con.createStatement();
			String query = "update distchart set distance="+distance+" where  srcId='"
					+ srcid + "' and destId='" + destid + "'";
			st.executeUpdate(query);
		} catch (Exception e) {
			System.out.println("error" + e);
		} finally {
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);
		}

	}
	
	 
	public void insertDistanceFromDistanceList(ArrayList<AplDistanceDto> distanceList) {
		
		DbConnect ob = DbConnect.getInstance();
		PreparedStatement pst = null;
		PreparedStatement checkPst = null;
		PreparedStatement updatePst = null;
		ResultSet rsCheck = null;
		Connection con = ob.connectDB();
		int returnInt = 0;
		try {
			con.setAutoCommit(false);
			String checkQuery = "select * from distchart where (srcId=? and destId=?) or (srcId=? and destId=?) ";
			String query = "insert into distchart(srcId,destId, distance) values (?,?, ?)";
			String updatePstQuery = "update distchart set distance=? where srcId=? and destId=?";
				checkPst = con.prepareStatement(checkQuery);
				updatePst = con.prepareStatement(updatePstQuery);
				 
				pst = con.prepareStatement(query);
				for(AplDistanceDto dto: distanceList)
				{
					if(dto.getSourceId().equals(dto.getTargetId())) {
						returnInt = returnInt + 1;
					} else {
						checkPst.setString(1, dto.getSourceId());
						checkPst.setString(2, dto.getTargetId());
						checkPst.setString(3, dto.getTargetId());
						checkPst.setString(4, dto.getSourceId());
						rsCheck = checkPst.executeQuery();
						if(rsCheck.next()) {
							updatePst.setDouble(1, dto.getDistance());
							updatePst.setString(2, dto.getSourceId());
							updatePst.setString(3, dto.getTargetId());
							updatePst.executeUpdate();
							returnInt= returnInt + 1;
						} else {
							String errorMsg="select * from distchart where (srcId=? and destId=?) or (srcId=? and destId=?)" ;
							errorMsg = errorMsg.replace("?", "'%s'");
							errorMsg = String.format(errorMsg,dto.getSourceId(), dto.getTargetId(), dto.getTargetId(), dto.getSourceId() );
							getErrorMessageList().add(errorMsg);
							
							pst.setString(1, dto.getSourceId());
							pst.setString(2, dto.getTargetId() ); 
							pst.setDouble(3, dto.getDistance() ); 
							int val = pst.executeUpdate();
							if(val <=0) {
								throw new Exception("Insertion failed");
								
							} 
							returnInt = returnInt + val;
						}
						
					}
					con.commit();
					  

				}
		 
		} catch (Exception e) {
			System.out.println("error" + e);
			try {
				con.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} finally {
			DbConnect.closeResultSet(rsCheck);
			DbConnect.closeStatement(pst, updatePst, checkPst);
			DbConnect.closeConnection(con);
		}


		
	}

	public void insertIntoDB(ArrayList<DistanceChartDto> distanceChartDtos) {
		DbConnect ob = DbConnect.getInstance();
		PreparedStatement pst = null;
		Connection con = ob.connectDB();
		try {
			
			String query = "insert into distchart(srcId,destId) values (?,?)";
			pst = con.prepareStatement(query);
			for(DistanceChartDto dto: distanceChartDtos)
			{
			pst.setString(1, dto.getSourceLandmarkId());
			pst.setString(2, dto.getDestinationLandmarkId());
			pst.executeUpdate();
		}
		} catch (Exception e) {
			System.out.println("error" + e);
		} finally {
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);
		}

		
	}
	public Connection getConnection()
	{
		DbConnect ob = DbConnect.getInstance();
		Connection con = ob.connectDB();
		return con;
	}
	public float getDistance(String srcid, String destid,Object connectionObject) {
		Statement st = null;
		ResultSet rs=null;
		float distance=(float) 0.1;
		try {
			
			st = ((Connection) connectionObject).createStatement();
			String query = "select distance from distchart where (srcId="+srcid+" and destId="+destid+") or (srcId="+destid+" and destId="+srcid+")";			
			rs=st.executeQuery(query);
			if(rs.next())
			{
			distance=rs.getFloat(1);	
			}
			else
			{
			distance=getGooglMapDistance(srcid,destid);	
			if(distance!=0.1||distance!=0.0)
			{
			String query1 = "insert into distchart set srcId="+srcid+",destId="+destid+", distance="+distance;			
			st.executeUpdate(query1);
			}
			}
		} catch (Exception e) {
			System.out.println("error" + e);
		} finally {
			DbConnect.closeStatement(st);			
		}
		return distance;

	}
	public void closeConnection(Object con)
	{
		
		DbConnect.closeConnection((Connection) con);
	}
	

	public ArrayList<String> getErrorMessageList() {
		return errorMessageList;
	}

	public void setErrorMessageList(ArrayList<String> errorMessageList) {
		this.errorMessageList = errorMessageList;
	}
	
	
	

	public float getGooglMapDistance(String srcId,String destId) {
		float distance = (float)0.0;
		try{
		
	
		/* get distances from map api */
		DefaultHttpClient client1 = new DefaultHttpClient();					
	     String sourceLatLng=getLandmarkLatitudeLongitude(srcId).getLocation();
	     String destinationLatLng=getLandmarkLatitudeLongitude(destId).getLocation();																																			 
	     String url="http://maps.googleapis.com/maps/api/distancematrix/json?origins="+sourceLatLng+"&destinations="+destinationLatLng;
	     String keyedURL=OtherFunctions.encryptTheMapKey(url);
		HttpGet request = new HttpGet(keyedURL);
	    HttpResponse response1 = 
	    		 client1.execute(request);
	      
	     BufferedReader rd = new BufferedReader (new InputStreamReader(response1.getEntity().getContent()));
	     String line = "";
	     String nLine="";
	
	     while ((line = rd.readLine()) != null) {
	    	 nLine+=line;
	        }
	    // System.out.println(nLine);
	     JSONObject obj = new JSONObject(nLine);
	     
	     obj=(JSONObject) (obj.getJSONArray("rows")).get(0);
			obj =(JSONObject) (obj.getJSONArray("elements")).get(0);
			obj=(JSONObject) obj.get("distance");
			int distanceM =(int) obj.get("value");	
			distance=(float)distanceM/1000;
		
		}catch(Exception e) {
			System.out.println(srcId+"  dest"+destId+"  Error in getMapDistance : "+ e);

		}
		
		return distance;
	}
	public APLDto getLandmarkLatitudeLongitude(String landMarkID) {
		APLDto dto = new APLDto();
		DbConnect ob = DbConnect.getInstance();
		Statement st = null;
		ResultSet rs = null;

		Connection con = ob.connectDB();
		String query = "select id, latitude ,longitude  from landMark  where id="
				+ landMarkID;
		// System.out.println("query : " + query);
		// PreparedStatement pst = null;
		try {
			// Connection pooling implementation

			// replace ob.connectDB() with Connection con=ob.connectDB();
			// replace ob.con with con

			st = con.createStatement();
			rs = st.executeQuery(query);
			while (rs.next()) {
				dto.setLandMarkID(rs.getString(1));
				dto.setLattitude(rs.getString(2));
				dto.setLocation(rs.getString(2)+","+rs.getString(3));	
			}


		} catch (Exception e) {
			System.out.println("Error in DAO-> APL getLandMarkAccurate : " + e);

		}
		finally
		{
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);	
		}		
		return dto;
	}
	public double getDistanceWithoutMap(double sourcelat, double sourcelong, double destlat, double destlong) {

	    double earthRadius = 6371; // in km, change to 3958.75 for miles

	    double dLat = Math.toRadians(destlat-sourcelat);
	    double dLng = Math.toRadians(destlong-sourcelong);

	    double sindLat = Math.sin(dLat / 2);
	    double sindLng = Math.sin(dLng / 2);

	    double a = Math.pow(sindLat, 2) + Math.pow(sindLng, 2)
	        * Math.cos(Math.toRadians(sourcelat)) * Math.cos(Math.toRadians(destlat));

	    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));

	    double dist = earthRadius * c;

	    return dist;
	}
}
