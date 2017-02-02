package com.agiledge.atom.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Random;

import com.agiledge.atom.commons.OtherFunctions;
import com.agiledge.atom.dbConnect.DbConnect;
import com.agiledge.atom.dto.EmergencyDto;
import com.agiledge.atom.dto.VehicleDto;
import com.agiledge.atom.mobile.dao.MobileTripSheetDao;
import com.agiledge.atom.mobile.dao.TripServiceDao;
import com.agiledge.atom.sms.SMSService;
import com.itextpdf.text.pdf.PdfStructTreeController.returnType;
import com.sun.istack.internal.FinalArrayList;

public class EmergencyDaoForCD {
	private static final char[] symbols = new char[10];

	  static {
	    for (int idx = 0; idx < 10; ++idx) {
	      symbols[idx] = (char) ('0' + idx);
	      System.out.println(symbols[idx]);
	    }
	  }
	public int insertEmergencyDetails(EmergencyDto dto)
    {
	
		int i=0;
		DbConnect ob = DbConnect.getInstance();
		Connection con = ob.connectDB();
		ResultSet  autogenrs=null,autogenrs1=null;
		Statement pst = null;
		PreparedStatement pst1=null,pst2=null,pst3=null,pst4=null,pst5=null,pst6=null;
		String tripid="";
		dto.setTravelDate(OtherFunctions.changeDateFromatToIso(dto.getTravelDate()));
	
		try {
			   
		//int site_id =Integer.parseInt(dto.getSiteId()); 	
		String escort="NO",escortquery1="",escortquery2="",tripescortquery="",tripescortval="",escortpswd=""; 
		 if(dto.getEscortId()!=null&&!dto.getEscortId().equalsIgnoreCase(""))
		 { 
			 escort="YES";
			 tripescortquery=",escortid,escortpswd";
			 tripescortval=",?,?";
			 escortquery1="escort,escortId,";
			 escortpswd=new EscortDao().TripEscortPassword(dto.getEscortId());
			 escortquery2="'"+escort+"','"+dto.getEscortId()+"',";
		}
		 else
		 {
			 escort="NO";
			 escortquery1="escort,";
			 escortquery2="'"+escort+"',";
		 }
		
		 		  String query="insert into transportation_in_emergency(siteId,bookingFor,travelDate,startTime,bookingBy,"+escortquery1+"type,vehicle,area,place,landmark,landmarkId,reason,booked_date_status,booked_time_status,driverId) values("+dto.getSiteId()+",'"+dto.getBookingFor()+"', '"+dto.getTravelDate()+"' ,'"+dto.getStartTime()+"','"+dto.getBookingBy()+"' ,"+escortquery2+"'"+dto.getVehicleType()+"','"+dto.getVehicle()+"','"+dto.getArea()+"','"+dto.getPlace()+"','"+dto.getLandmark()+"','"+dto.getLandmarkid()+"','"+dto.getReason()+"',curdate(),curtime(),"+dto.getDriverId()+")";	
		 		  pst = con.createStatement();
		 		  String em_id = "0";
		 		  i = pst.executeUpdate(query,Statement.RETURN_GENERATED_KEYS);
		          autogenrs1=pst.getGeneratedKeys();
		          while(autogenrs1.next()){
		        	 em_id=autogenrs1.getString(1); 
		          }
		          if(i==1)
		          {
		        	  String tripdetailsquery="INSERT INTO TRIP_DETAILS(trip_code,siteid,trip_date,trip_time,trip_log,vehicle_type,status,security_status,vehicle,routingtype,driverid,destinationLandmark"+tripescortquery+") values(?,?,?,?,?,?,?,?,?,?,?,?"+tripescortval+")";
		        	  pst1=con.prepareStatement(tripdetailsquery,Statement.RETURN_GENERATED_KEYS);
		        	  String tId = createUniqueID(dto.getTravelDate(),dto.getSiteId());
		        	  pst1.setString(1,tId);
		        	  pst1.setInt(2, Integer.parseInt(dto.getSiteId()));
		        	  pst1.setString(3,dto.getTravelDate());
		        	  pst1.setString(4, dto.getStartTime());
		        	  pst1.setString(5, "OUT");
		        	  pst1.setInt(6,Integer.parseInt(dto.getVehicleType()));
		        	  pst1.setString(7, "saved");
		        	  pst1.setString(8, escort);
		        	  pst1.setInt(9, Integer.parseInt(dto.getVehicle()));
		        	  pst1.setString(10, "o");
		        	  pst1.setString(11, dto.getDriverId());
		        	  pst1.setInt(12,Integer.parseInt(dto.getLandmarkid()));
		        	  if(escort.equalsIgnoreCase("yes"))
		        	  {
		        	  pst1.setInt(13, Integer.parseInt(dto.getEscortId()));     	  
		        	  pst1.setString(14, escortpswd);
		        	  }
		        	  int returnint = pst1.executeUpdate();
					   autogenrs = pst1.getGeneratedKeys();
						while (autogenrs.next()) {
							tripid = autogenrs.getString(1);
						}
						if(returnint==1)
						{ 
						  String updatetransport="UPDATE TRANSPORTATION_IN_EMERGENCY SET TRIPID='"+tripid+"' WHERE ID="+em_id;
						  pst6=con.prepareStatement(updatetransport);
						  pst6.executeUpdate();
						  String vendorassign="INSERT INTO TRIPVENDORASSIGN(TRIPID,VENDORID,STATUS) VALUES("+tripid +", (select vendor from vehicles where id="+dto.getVehicle()+"), 'a' )";
						  pst5=con.prepareStatement(vendorassign);
						 /* pst5.setString(1, tripid);
						  pst5.setString(2, "1");
						  pst5.setString(3, "a");*/
						  pst5.executeUpdate();
						  String updateQuery = "update trip_details set driverPswd=ifnull(driverPswd,?) where id=? ";
						  pst2=con.prepareStatement(updateQuery);
					      String password= nextDriverString( tripid);
						  pst2.setString(1,password );
						  pst2.setString(2,tripid);
						  int t=pst2.executeUpdate();
								  
						  String tripchilddetailsquery="INSERT INTO TRIP_DETAILS_CHILD(tripId,employeeid,routedOrder,landmarkId,transportType) values(?,?,?,?,?)";
						  pst3=con.prepareStatement(tripchilddetailsquery);
						  pst3.setInt(1,Integer.parseInt(tripid));
			        	  pst3.setInt(2, Integer.parseInt(dto.getBookingFor()));
			        	  pst3.setInt(3,1);
			        	  pst3.setInt(4,Integer.parseInt(dto.getLandmarkid()));
			        	  pst3.setString(5, "adhoc");
			        	  int y =pst3.executeUpdate();							
			        	  int  ts = new TripServiceDao().setTripReadyForTracking(tripid, dto.getVehicle());
			        	  String empPin=getEmppin(tripid);
			        	  if(escort.equalsIgnoreCase("YES"))
			        	  {	  
			        	  pst4=con.prepareStatement("Insert into vendor_trip_sheet_escort (tripId,escortId,showStatus) values(?,?,?) ");
			        	  pst4.setString(1, tripid);
			        	  pst4.setString(2, dto.getEscortId());
			        	  pst4.setString(3,"No Show");
			        	  pst4.executeUpdate();
			        	  }
			        	  new SMSService().SendEmergencyTransportSMS(dto,password,empPin);
			     		}
		        }
		          
		}
		 
	 catch (Exception e) {
			System.out.println("Error in Emergency transportation services..."+e); 
	} 
		
		finally {
		
		DbConnect.closeStatement(pst);
		DbConnect.closeConnection(con);
	}
		return i;	 
	}
	
	public String createUniqueID(String date,String siteid) {
		String uniqueId = "";
		String uniqueCode = "";
		int uniqueNo = 1;
		DbConnect ob = null;
		Connection con = null;
		ResultSet rs = null;
		Statement st = null;
		try {
			ob = DbConnect.getInstance();
			con = ob.connectDB();
			uniqueCode = OtherFunctions.getOrdinaryDateFormat(date);
			uniqueCode = uniqueCode.replaceAll("/", "");
			
				uniqueCode += "ET";

			st = con.createStatement();
			String query = "select CAST(SUBSTRING(trip_code,9,3) as UNSIGNED) as serial from trip_details where siteId="
					+ siteid
					+ " and trip_date='"
					+ date
					+ "' and  SUBSTRING(trip_code,7,2)='ET' and status in ('saved','addsave','saveedit') order by  serial desc limit 1";
			// System.out.println("query" + query);
			rs = st.executeQuery(query);
			if (rs.next()) {
				uniqueNo = rs.getInt(1) + 1;
			}
			if(uniqueNo<10)
			uniqueCode+="00"+uniqueNo;
			else
				uniqueCode+="0"+uniqueNo;
		} catch (SQLException ex) {

			System.out.println("Error in save" + ex);
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);
		}
		return uniqueCode;
	}
public String nextDriverString(String tripId)
	  {
		Random random = new Random();
		char[] buf =new char[4];

		MobileTripSheetDao mdao=new MobileTripSheetDao();
		  ArrayList<String> pswds= mdao.getDriverPasswords(tripId,-15, +15);
		   
		  String returnPswd="";
		   
		  boolean contains=true;
		 for(int i = 0; contains&&i < 2; i++) { 
	    for (int idx = 0;   idx < buf.length; ++idx) {
	    	int randomInt= random.nextInt(symbols.length);
	    	System.out.println("Random : "+ randomInt);
	      buf[idx] = symbols[randomInt];
	     
	    }
	    returnPswd=new String(buf); 
		 
	    System.out.println("Generated @"+ i+" :"+returnPswd);
	    	if(pswds.contains(returnPswd)) {
	    		contains=true;
	    		System.out.println("contains :true at "+ i );
	    	} else {
	    		contains=false;
	    		System.out.println("contains :false at "+ i );
	    	}
		 }
		  System.out.println(contains);
		 if(contains) {
			 returnPswd = getDifferentPassword(pswds);
			 System.out.println("return Pswd in fn : "+ returnPswd);
			 if(returnPswd.equalsIgnoreCase("0000")) {
				 returnPswd = returnPswd + tripId;
			 }
			 
		 }
		 System.out.println("password"+new String(returnPswd));
	    return new String(returnPswd);
	  }
	public String getDifferentPassword(ArrayList<String> pswds) {
		  String returnString="0000";
		  long largest = 0;
		  try {
		  for(String pswd : pswds) {
			  try {
				  long curPswd = Long.parseLong(pswd);
				  if(largest<=curPswd) {
					  largest = curPswd;
				  }
			  }catch(Exception e) {
				  ;
			  }
		  }
		  largest = largest + 1;
		  returnString = String.valueOf(largest);
		  }catch(Exception e) {
			  ;
		  }
		  
		  return returnString;
	  }
	
	public String getEmppin(String tripid)
	{
		DbConnect ob = null;
		Connection con = null;
		ResultSet rs = null;
		Statement st = null;
		String epin="";
		try {
			ob = DbConnect.getInstance();
			con = ob.connectDB();
			st=con.createStatement();
			String q="select keypin from  vendor_trip_sheet where tripId = "+tripid ;
	         rs=st.executeQuery(q);
	         while(rs.next())
	         {
	        	 epin=rs.getString("keypin");
	         }
	 		return epin;
		}

		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);
		}
		return epin;
	
	
	}
		
		
		
		
}
 