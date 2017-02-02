package com.agiledge.atom.servlets;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.agiledge.atom.commons.OtherFunctions;
import com.agiledge.atom.dao.SettingsDoa;
import com.agiledge.atom.dbConnect.DbConnect;
import com.agiledge.atom.dto.EmergencyDto;
import com.agiledge.atom.mail.SendMail;
import com.agiledge.atom.mail.SendMailFactory;

public class EmergencyDao {
 public int insertEmergencyDetails(String siteId, String bookingFor,String travelingDate,String travelingtoDate,String startTime,String bookingBy,String type, String vehicle,String area,String place,String landmark,String landmarkId, String reason)
 {
	
	 int i=0;
	
	 DbConnect ob = DbConnect.getInstance();
		Connection con = ob.connectDB();
		Statement pst = null;
	
		String travelFromDate=OtherFunctions.changeDateFromatToIso(travelingDate);
		String traveltoDate=OtherFunctions.changeDateFromatToIso(travelingtoDate);
		
		try {
			
		
	    
		String query="insert into transportation_in_emergency(siteId,bookingFor,travelFromDate,travelToDate,startTime,bookingBy,type,vehicle,area,place,landmark,landmarkId,reason,booked_date_status,booked_time_status) values("+siteId+",'"+bookingFor+"', '"+travelFromDate+"','"+traveltoDate+"' ,'"+startTime+"','"+bookingBy+"' ,'"+type+"','"+vehicle+"','"+area+"','"+place+"','"+landmark+"','"+landmarkId+"','"+reason+"',curdate(),curtime())";	
		pst = con.createStatement();
		System.err.println(siteId+"   "+bookingFor+"   "+travelFromDate+"   "+traveltoDate+"   "+startTime+"   "+bookingBy+"   "+type+"   "+vehicle+"   "+area+"   "+place+"   "+landmark+"   "+landmarkId+"   "+reason);
		System.out.println("Emergency Insert Query:"+query);	
		  i = pst.executeUpdate(query);
		     con.commit(); 
		     }
		 
	 catch (Exception e) {
			System.out.println("Error in Emergency transportation services..."); 
	} 
		
		finally {
		
		DbConnect.closeStatement(pst);
		DbConnect.closeConnection(con);
	}
		return i;	 
	} 
 
 public int insertSpecialTransportDetails(EmergencyDto dto){
	 int i=0;
		
	 DbConnect ob = DbConnect.getInstance();
		Connection con = ob.connectDB();
		java.sql.PreparedStatement pst = null;
		Statement st=null;
		ResultSet rs=null;
		try {
			String travelDate=OtherFunctions.changeDateFromatToIso(dto.getTravelDate());
			String qry="INSERT INTO special_transport (`site`, `bookedby`, `logtype`, `date`, `time`, `reason`, `vehicletype`, `total_empl`, `location`) VALUES (?,?, ?, ?, ?, ?, ?, ?, ?)";
		
			pst=con.prepareStatement(qry);
			pst.setString(1, dto.getSiteId());
			pst.setString(2, dto.getBookingBy());
			pst.setString(3, dto.getLogtype());
			pst.setString(4, travelDate);
			pst.setString(5, dto.getStartTime());
			pst.setString(6, dto.getReason());
			pst.setString(7, dto.getVehicleType());
			pst.setString(8, dto.getEmpCount());
			pst.setString(9, dto.getLandmark());
			i=pst.executeUpdate();
			if(i>0){
				String qry1="select displayname,emailaddress from employee where id="+dto.getBookingBy();
				st=con.createStatement();
				rs=st.executeQuery(qry1);
				if(rs.next()){
				String msg="Dear "+rs.getString("displayname")+", <br/> <br/>        You have successfully  booked Special transport to "+dto.getLandmark()+" for "+dto.getLogtype()+" request along with "+dto.getEmpCount()+" employee(s) on "+dto.getTravelDate()+" at "+dto.getStartTime()+" <br/><br/>"
						+ " <br/>"
						
						+ "Regards,<br/><a href='"
						+  new SettingsDoa().getDomainName()
						+ "'  >" + "Transport Team" + " </a>"
						+ "<br/>---------------<br/> <i><u>Note:</u> This is a system generated email. Please do not reply.</i>";
				SendMail mail= SendMailFactory.getMailInstance();
				String[]  mailaddress={rs.getString("emailaddress")};
				String cc[]={"lalith@AGILEDGESOLUTIONS.COM"};
				String[] message={msg};
				mail.send( mailaddress,"Special Transportation Service", message,cc );
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("Error in Emergencydao@insertSpecialTransportDetails"+e);;
		}
	return i; 
 }
 
}