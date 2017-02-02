package com.agiledge.atom.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.agiledge.atom.commons.OtherFunctions;
import com.agiledge.atom.dbConnect.DbConnect;
import com.agiledge.atom.dto.EmergencyDto;
import com.agiledge.atom.dto.EmployeeDto;
import com.agiledge.atom.dto.TripDetailsDto;
import com.agiledge.atom.mail.SendMail;
import com.agiledge.atom.mail.SendMailFactory;

public class StsDao {

	public int addStsEmployee(EmployeeDto dto) {
		// TODO Auto-generated method stub
		int result=0;
		DbConnect ob = DbConnect.getInstance();
		Connection con = null;
		PreparedStatement pst=null;
		
		con=ob.connectDB();
		try {
			String insertqry="INSERT INTO employee(`EmployeeFirstName`, `employeeLastName`, `gender`, `emailaddress`, `contactnumber1`, `personnelNo`, `password`,Active,displayname,registerStatus,pwdChangedDate,loginId,authType,usertype,role_id) VALUES (?, ?, ?,?, ?, ?, ?,?,?,?,curdate(),?,'l','ste',14)";
			pst=con.prepareStatement(insertqry);
			pst.setString(1, dto.getEmployeeFirstName());
			pst.setString(2, dto.getEmployeeLastName());
			pst.setString(3, dto.getGender());
			pst.setString(4, dto.getEmailAddress());
			pst.setString(5, dto.getContactNo());
			pst.setString(6, dto.getPersonnelNo());
			pst.setString(7, dto.getPassword());
			pst.setString(8, "1");
			pst.setString(9, dto.getEmployeeFirstName()+" "+dto.getEmployeeLastName());
			pst.setString(10, "a");
			pst.setString(11, dto.getPersonnelNo());
			result=pst.executeUpdate();
			if(result>0){
				String msg="Dear "+dto.getEmployeeFirstName()+", <br/> <br/>        You have successfully  registered in ATOm <br/><br/>"
						+ "Your username is "+dto.getPersonnelNo()+" <br/>"
						
						+ "Regards,<br/><a href='"
						+  new SettingsDoa().getDomainName()
						+ "'  >" + "Transport Team" + " </a>"
						+ "<br/>---------------<br/> <i><u>Note:</u> This is a system generated email. Please do not reply.</i>";
				SendMail mail= SendMailFactory.getMailInstance();
				String  mailaddress=dto.getEmailAddress();
				mail.send( mailaddress,"Special Transportation Service", msg );
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("Error StsDao@addStsEmployee"+e);
		}finally{
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);
		}
		return result;
	}
	public int insertSpecialTransportDetails(EmergencyDto dto){
		 int i=0;
			
		 DbConnect ob = DbConnect.getInstance();
			Connection con = ob.connectDB();
			java.sql.PreparedStatement pst = null;
			Statement st=null;
			ResultSet rs=null;
			try {
				String approval="0";
				
				DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
				Date date =  df.parse(dto.getTravelDate());
				
				String travelDate=OtherFunctions.changeDateFromatToSqlFormat(date);
				
				String qry="INSERT INTO sts_trip_details (site, bookedby, date, time, reason, vehicletype, total_empl, pickup,droppoint,approved) VALUES (?,?, ?, ?, ?, ?, ?, ?,?,?)";
			
				pst=con.prepareStatement(qry);
				pst.setString(1, dto.getSiteId());
				pst.setString(2, dto.getBookingBy());
				pst.setString(3, travelDate);
				pst.setString(4, dto.getStartTime());
				pst.setString(5, dto.getReason());
				pst.setString(6, dto.getVehicleType());
				pst.setString(7, dto.getEmpCount());
				pst.setString(8, dto.getLandmark());
				pst.setString(9, dto.getDrop());
				pst.setString(10, approval);
				i=pst.executeUpdate();
				if(i>0){
					String submsg="<table><th><td>Name</td><td>Contact Number</td><td> From </td><td> To </td><td> Total Traveller </td><td> Travel Date </td><td> Travel Time </td></th>";
					String qry1="select displayname,personnelno,contactNumber1,emailaddress,(select IFNULL ((select emailaddress from employee where id in (select linemanager from employee where  id="+dto.getBookingBy()+")),emailaddress)) as linemanager from employee e where id="+dto.getBookingBy();
					st=con.createStatement();
					rs=st.executeQuery(qry1);
					if(rs.next()){
						submsg+="<tr><td>"+rs.getString("personnelno")+"</td><td>"+rs.getString("displayname")+"</td><td>"+rs.getString("contactNumber1")+"</td><td>"+dto.getLandmark()+"</td><td>"+dto.getDrop()+"</td><td>"+dto.getEmpCount()+"</td><td>"+dto.getTravelDate()+"</td><td>"+dto.getStartTime()+"</td></tr></table>";
					String msg="Dear "+rs.getString("displayname")+"( "+rs.getString("personnelno")+" ), <br/> <br/>         Request successful for Special transport. Drtails are shown below <br/><br/>"
							+submsg
							+ " <br/>"
							+ "Regards,<br/><a href='"
							+  new SettingsDoa().getDomainName()
							+ "'  >" + "Transport Team" + " </a>"
							+ "<br/>---------------<br/> <i><u>Note:</u> This is a system generated email. Please do not reply.</i>";
					
					SendMail mail= SendMailFactory.getMailInstance();
					String  mailaddress=rs.getString("emailaddress");
					String cc[]={rs.getString("linemanager"),"HARIHARAN.RAMAKRISHNAN@SIEMENS.COM","anand.s.ext@siemens.com","blr2transport.sips.in@siemens.com"};
					String message=msg;
					mail.setCcs(cc);
					mail.send( mailaddress,"Special Transportation Service", message );
					}
				}
			} catch (SQLException | ParseException e) {
				// TODO Auto-generated catch block
				System.out.println("Error in Emergencydao@insertSpecialTransportDetails"+e);;
			}finally{
				DbConnect.closeResultSet(rs);
				DbConnect.closeStatement(pst,st);
				DbConnect.closeConnection(con);
			}
		return i; 
	 }
	public List<EmergencyDto> getStsEmployeesForApproval() {
		// TODO Auto-generated method stub

		 DbConnect ob = DbConnect.getInstance();
			Connection con = ob.connectDB();
			Statement st=null;
			ResultSet rs=null;
			List<EmergencyDto> list= new ArrayList<EmergencyDto>();
			try{
				st=con.createStatement();
				String qry="SELECT s.id,e.displayname as bookedby,s.logtype,s.date,s.time,s.reason,vt.type as vehicletype,s.total_empl,s.pickup,s.droppoint FROM sts_trip_details s,vehicle_type vt,employee e where e.id=s.bookedby and vt.id=s.vehicletype and s.approved='0' and s.vehicleno is null";
				rs=st.executeQuery(qry);
				while(rs.next()){
					EmergencyDto dto = new EmergencyDto();
					dto.setTripcode(rs.getString("id"));
					dto.setBookingFor(rs.getString("bookedby"));
					dto.setLogtype(rs.getString("logtype"));
					dto.setTravelDate(OtherFunctions.changeDateFromat(rs.getString("date")));
					dto.setStartTime(rs.getString("time"));
					dto.setReason(rs.getString("reason"));
					dto.setVehicleType(rs.getString("vehicletype"));
					dto.setEmpCount(rs.getString("total_empl"));
					dto.setArea(rs.getString("pickup"));
					dto.setDrop(rs.getString("droppoint"));
					list.add(dto);
				}
			
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				System.out.println("Error in Emergencydao@getStsEmployeesForApproval"+e);;
			}finally{
				DbConnect.closeResultSet(rs);
				DbConnect.closeStatement(st);
				DbConnect.closeConnection(con);
			}
		return list;
	}

	public List<EmergencyDto> getStsApprovedEmployees() {
		// TODO Auto-generated method stub

		 DbConnect ob = DbConnect.getInstance();
			Connection con = null;
			Statement st=null;
			ResultSet rs=null;
			List<EmergencyDto> list= new ArrayList<EmergencyDto>();
			try{
				con=ob.connectDB();
				st=con.createStatement();
				String qry="SELECT s.id,e.displayname as bookedby,s.date,s.time,s.reason,vt.type as vehicletype,s.total_empl,s.pickup,s.droppoint FROM sts_trip_details s,vehicle_type vt ,employee e where e.id=s.bookedby and vt.id=s.vehicletype and s.approved='approved' and s.vehicleno is null";
				rs=st.executeQuery(qry);
				while(rs.next()){
					EmergencyDto dto = new EmergencyDto();
					dto.setTripcode(rs.getString("id"));
					dto.setBookingFor(rs.getString("bookedby"));
					dto.setTravelDate(rs.getString("date"));
					dto.setStartTime(rs.getString("time"));
					dto.setReason(rs.getString("reason"));
					dto.setVehicleType(rs.getString("vehicletype"));
					dto.setEmpCount(rs.getString("total_empl"));
					dto.setArea(rs.getString("pickup"));
					dto.setDrop(rs.getString("droppoint"));
					list.add(dto);
				}
			
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				System.out.println("Error in Emergencydao@getStsApprovedEmployees"+e);;
			}finally{
				DbConnect.closeResultSet(rs);
				DbConnect.closeStatement(st);
				DbConnect.closeConnection(con);
			}
		return list;
	}
	public int approveStsRequest(String id,String status,String approvedby) {
		DbConnect ob = DbConnect.getInstance();
		Connection con = null;
		Statement st=null,st1=null;
		ResultSet rs=null;
		int result=0;
		try {
			con=ob.connectDB();
			st=con.createStatement();
			String updateqry="update sts_trip_details set approved='"+status+"',approvedby='"+approvedby+"' where id="+id;
			System.out.println(updateqry);
			result= st.executeUpdate(updateqry);
			if(result>0){
				st1=con.createStatement();
				String qry1="select e.displayname as emp,e.emailaddress as empadd,e1.displayname as approve,e1.emailaddress as approveby from sts_trip_details s,employee e,employee e1 where e.id=s.bookedby and e1.id=s.bookedby and s.id="+id;
				st1=con.createStatement();
				rs=st1.executeQuery(qry1);
				if(rs.next()){
					
					String msg="Dear "+rs.getString("emp")+", <br/> <br/>         Request for Special transport is approved by "+rs.getString("approve")+"<br/><br/>"
							+ " <br/>"
							
							+ "Regards,<br/><a href='"
							+  new SettingsDoa().getDomainName()
							+ "'  >" + "Transport Team" + " </a>"
							+ "<br/>---------------<br/> <i><u>Note:</u> This is a system generated email. Please do not reply.</i>";
					
					SendMail mail= SendMailFactory.getMailInstance();
					String[]  mailaddress={rs.getString("empadd")};
					String cc[]={rs.getString("approveby"),"HARIHARAN.RAMAKRISHNAN@SIEMENS.COM","anand.s.ext@siemens.com"};
					String[] message={msg};
					mail.send( mailaddress,"Special Transportation Service", message,cc );
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("Error in StsDao@approveStsRequest"+e);
		}finally{
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);
		}
		return result;
	}
	public int stsTripDriverAssaign(TripDetailsDto dto){
		DbConnect ob = DbConnect.getInstance();
		Connection con = null;
		PreparedStatement pst=null;
		Statement st=null;
		ResultSet rs=null;
		int result=0;
		try{
			con=ob.connectDB();
			String qry="update sts_trip_details set vehicletype_allocated=?,vehicleno=?,drivername=?,drivernumber=?,approvedby=?,approved=?,vendor=? where id=?";
			pst=con.prepareStatement(qry);
			pst.setString(1, dto.getVehicle_type());
			pst.setString(2, dto.getVehicleNo());
			pst.setString(3, dto.getDriverName());
			pst.setString(4, dto.getDriverContact());
			pst.setString(8, dto.getId());
			pst.setString(5, dto.getDoneBy());
			pst.setString(6, "approved");
			pst.setString(7, "Sri Ayyappa Tourist");
			result=pst.executeUpdate();
			if(result>0){
				st=con.createStatement();
				String qry1="select e.displayname as emp,e.emailaddress as empadd from sts_trip_details s,employee e where e.id=s.bookedby and s.id="+dto.getId();
				st=con.createStatement();
				rs=st.executeQuery(qry1);
				if(rs.next()){
					
					String msg="Dear "+rs.getString("emp")+", <br/> <br/>         Cab is allocated  for your Special Transportation <br/><br/>"
							+ " Driver Name : "+dto.getDriverName()+"   Mobile No : "+dto.getDriverContact()+"<br/>"
							+ " Cab Number : "+dto.getVehicleNo()+" ( "+dto.getVehicle_type()+" )<br/><br/>"
							+ "Regards,<br/><a href='"
							+  new SettingsDoa().getDomainName()
							+ "'  >" + "Transport Team" + " </a>"
							+ "<br/>---------------<br/> <i><u>Note:</u> This is a system generated email. Please do not reply.</i>";
					
					SendMail mail= SendMailFactory.getMailInstance();
					String[]  mailaddress={rs.getString("empadd")};
					String cc[]={"HARIHARAN.RAMAKRISHNAN@SIEMENS.COM","anand.s.ext@siemens.com","tpt.helpdesk.in@siemens.com "};
					String[] message={msg};
					mail.send( mailaddress,"Special Transportation Service", message,cc );
				}
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("Error in StsDao@stsTripDriverAssaign"+e);
		}finally{
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);
		}
		return result;
	}
	public EmergencyDto getStsApprovedTrip(String id) {
		 DbConnect ob = DbConnect.getInstance();
			Connection con = null;
			Statement st=null;
			ResultSet rs=null;
			EmergencyDto dto = new EmergencyDto();
			try{
				con=ob.connectDB();
				st=con.createStatement();
				String qry="SELECT s.id,e.displayname as bookedby,s.logtype,s.date,s.time,s.reason,vt.type as vehicletype,s.total_empl,s.pickup,s.droppoint FROM sts_trip_details s,vehicle_type vt, employee e where e.id=s.bookedby and vt.id=s.vehicletype and s.id="+id;
				rs=st.executeQuery(qry);
				if(rs.next()){
					dto.setTripcode(rs.getString("id"));
					dto.setBookingFor(rs.getString("bookedby"));
					dto.setLogtype(rs.getString("logtype"));
					dto.setTravelDate(rs.getString("date"));
					dto.setStartTime(rs.getString("time"));
					dto.setReason(rs.getString("reason"));
					dto.setVehicleType(rs.getString("vehicletype"));
					dto.setEmpCount(rs.getString("total_empl"));
					dto.setArea(rs.getString("pickup"));
					dto.setDrop(rs.getString("droppoint"));
				}
			
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				System.out.println("Error in Emergencydao@getStsApprovedTrips"+e);;
			}finally{
				DbConnect.closeResultSet(rs);
				DbConnect.closeStatement(st);
				DbConnect.closeConnection(con);
			}
		return dto;
	}
	public List<EmergencyDto> getStsEmployeesForApprovalUnderTL(String empid) {
		// TODO Auto-generated method stub
		 DbConnect ob = DbConnect.getInstance();
			Connection con = ob.connectDB();
			Statement st=null;
			ResultSet rs=null;
			List<EmergencyDto> list= new ArrayList<EmergencyDto>();
			try{
				st=con.createStatement();
				String qry="SELECT s.id,e.displayname as bookedby,s.logtype,s.date,s.time,s.reason,vt.type as vehicletype,s.total_empl,s.pickup,s.droppoint FROM sts_trip_details s,vehicle_type vt,employee e where e.id=s.bookedby and vt.id=s.vehicletype and s.approved='0' and s.bookedby in (select id from employee where linemanager="+empid+") and s.vehicleno is null";
				rs=st.executeQuery(qry);
				while(rs.next()){
					EmergencyDto dto = new EmergencyDto();
					dto.setTripcode(rs.getString("id"));
					dto.setBookingFor(rs.getString("bookedby"));
					dto.setLogtype(rs.getString("logtype"));
					dto.setTravelDate(OtherFunctions.changeDateFromat(rs.getString("date")));
					dto.setStartTime(rs.getString("time"));
					dto.setReason(rs.getString("reason"));
					dto.setVehicleType(rs.getString("vehicletype"));
					dto.setEmpCount(rs.getString("total_empl"));
					dto.setArea(rs.getString("pickup"));
					dto.setDrop(rs.getString("droppoint"));
					list.add(dto);
				}
			
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				System.out.println("Error in Emergencydao@getStsEmployeesForApprovalUnderTL"+e);
			}finally{
				DbConnect.closeResultSet(rs);
				DbConnect.closeStatement(st);
				DbConnect.closeConnection(con);
			}
		return list;
	}
	public int thirdPartyAssaign(String tripid, String approvedby,String thirdParty) {
		DbConnect ob = DbConnect.getInstance();
		Connection con = ob.connectDB();
		PreparedStatement pst=null;
		Statement st=null;
		ResultSet rs=null;
		int result=0;
		try {
			String qry="update sts_trip_details set approvedby=?,approved=?,vendor=? where id=?";
			pst=con.prepareStatement(qry);
			pst.setString(1, approvedby);
			pst.setString(2, "approved");
			pst.setString(3, thirdParty);
			pst.setString(4, tripid);
			result=pst.executeUpdate();
			if(result>0){
				st=con.createStatement();
				String qry1="select e.displayname as emp,e.emailaddress as empadd from sts_trip_details s,employee e where e.id=s.bookedby and s.id="+tripid;
				st=con.createStatement();
				rs=st.executeQuery(qry1);
				if(rs.next()){
					
					String msg="Dear "+rs.getString("emp")+", <br/> <br/>         Cab is allocated  for your Special Transportation through "+thirdParty+". <br/><br/>"
							+ " Soon you will recieve cab and driver details.<br/><br/>"
							+ "Regards,<br/><a href='"
							+  new SettingsDoa().getDomainName()
							+ "'  >" + "Transport Team" + " </a>"
							+ "<br/>---------------<br/> <i><u>Note:</u> This is a system generated email. Please do not reply.</i>";
					
					SendMail mail= SendMailFactory.getMailInstance();
					String[]  mailaddress={rs.getString("empadd")};
					String cc[]={"HARIHARAN.RAMAKRISHNAN@SIEMENS.COM","anand.s.ext@siemens.com"};
					String[] message={msg};
					mail.send( mailaddress,"Special Transportation Service", message,cc );
				}
			}
		} catch (SQLException e) {
			System.out.println("Error in Emergencydao@thirdPartyAssaign"+e);;
		}finally{
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(pst,st);
			DbConnect.closeConnection(con);
		}
		return result;
	}

}
