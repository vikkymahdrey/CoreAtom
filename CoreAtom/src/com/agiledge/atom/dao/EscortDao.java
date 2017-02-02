package com.agiledge.atom.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import org.apache.commons.collections.set.CompositeSet.SetMutator;

import com.agiledge.atom.commons.RandomString;
import com.agiledge.atom.constants.AuditLogConstants;
import com.agiledge.atom.constants.SettingsConstant;
import com.agiledge.atom.dbConnect.DbConnect;
import com.agiledge.atom.dto.EscortDto;
import com.agiledge.atom.sms.SMSService;
import com.sun.corba.se.spi.orbutil.fsm.State;



public class EscortDao {

	private String message;

	public ArrayList<EscortDto> getAllEscorts() {
		Statement st1 = null;
		ResultSet rs1 = null;
		ArrayList<EscortDto> escorts = null;
		Connection con = null;
		try {
			rs1 = null;
			st1 = null;
		DbConnect ob = DbConnect.getInstance();
		con=ob.connectDB();
		
			 escorts = new ArrayList<EscortDto>();
			st1 = con.createStatement();
			rs1 = st1.executeQuery("select e.*, s.site_name from escort e join site s on e.site=s.id order by s.id");
			while (rs1.next()) {
				EscortDto escort = new EscortDto();
				escort = new EscortDto();
				escort.setId(rs1.getString("id"));
				escort.setEscortClock(""
						+ rs1.getString("escortClock"));
				escort.setName(""
						+ rs1.getString("name"));
				escort.setAddress(""
						+ rs1.getString("Address"));
				escort.setPhone(rs1.getString("phone"));
				escort.setEmail(rs1.getString("email"));
				escort.setPassword(rs1.getString("password"));
				escort.setSite(rs1.getString("site"));
				escort.setSiteName(rs1.getString("site_name"));
				escorts.add(escort);
			}
		} catch (Exception e) {
			System.out.println("errro" + e);
		}
		finally{
			
			DbConnect.closeResultSet(rs1);
			DbConnect.closeStatement(st1);
			DbConnect.closeConnection(con);
			
		}
		return escorts;
	}
	
	public  EscortDto getEscortByTripId(String tripId) {
		Statement st1 = null;
		ResultSet rs1 = null;
		EscortDto escort = null;
		Connection con = null;
		try {
			rs1 = null;
			st1 = null;
		DbConnect ob = DbConnect.getInstance();
		con=ob.connectDB();
		
			  
			st1 = con.createStatement();
			rs1 = st1.executeQuery("select e.*, s.site_name from escort e join site s on e.site=s.id join trip_details td on td.escortId=e.id where td.id=" + tripId + " order by s.id");
			if(rs1.next()) {
				 escort = new EscortDto();
				escort = new EscortDto();
				escort.setId(rs1.getString("id"));
				escort.setEscortClock(""
						+ rs1.getString("escortClock"));
				escort.setName(""
						+ rs1.getString("name"));
				escort.setAddress(""
						+ rs1.getString("Address"));
				escort.setPhone(rs1.getString("phone"));
				escort.setEmail(rs1.getString("email"));
				escort.setPassword(rs1.getString("password"));
				escort.setSite(rs1.getString("site"));
				escort.setSiteName(rs1.getString("site_name"));
				 
			}
		} catch (Exception e) {
			System.out.println("errro" + e);
		}
		finally{
			
			DbConnect.closeResultSet(rs1);
			DbConnect.closeStatement(st1);
			DbConnect.closeConnection(con);
			
		}
		return escort;
	}
	

	public  EscortDto getEscortById(String  id) {
		Statement st1 = null;
		ResultSet rs1 = null;
		EscortDto escort = null;
		Connection con = null;
		try {
			rs1 = null;
			st1 = null;
		DbConnect ob = DbConnect.getInstance();
		con=ob.connectDB();
		
			  
			st1 = con.createStatement();
			rs1 = st1.executeQuery("select e.*, s.site_name from escort e join site s on e.site=s.id where e.id=" + id + " order by s.id");
			if(rs1.next()) {
				escort = new EscortDto();
				escort.setId(rs1.getString("id"));
				escort.setEscortClock(""
						+ rs1.getString("escortClock"));
				escort.setName(""
						+ rs1.getString("name"));
				escort.setAddress(""
						+ rs1.getString("Address"));
				escort.setPhone(rs1.getString("phone"));
				escort.setEmail(rs1.getString("email"));
				escort.setPassword(rs1.getString("password"));
				escort.setSite(rs1.getString("site"));
				escort.setSiteName(rs1.getString("site_name"));
				 
			}
		} catch (Exception e) {
			System.out.println("errro" + e);
		}
		finally{
			
			DbConnect.closeResultSet(rs1);
			DbConnect.closeStatement(st1);
			DbConnect.closeConnection(con);
			
		}
		return escort;
	}
	

	
	
	/*
	 * to get all the escort based on site
	 */
	public ArrayList<EscortDto> getAllEscorts(String site) {
		Statement st1 = null;
		ResultSet rs1 = null;
		ArrayList<EscortDto> escorts = null;
		Connection con = null;
		try {
			rs1 = null;
			st1 = null;
		DbConnect ob = DbConnect.getInstance();
		con=ob.connectDB();
		
			 escorts = new ArrayList<EscortDto>();
			st1 = con.createStatement();
			rs1 = st1.executeQuery("select e.*, s.site_name from escort e join site s on e.site=s.id where s.id="+site+" order by s.id ");
			while (rs1.next()) {
				EscortDto escort = new EscortDto();
				escort = new EscortDto();
				escort.setId(rs1.getString("id"));
				escort.setEscortClock(""
						+ rs1.getString("escortClock"));
				escort.setName(""
						+ rs1.getString("name"));
				escort.setAddress(""
						+ rs1.getString("Address"));
				escort.setPhone(rs1.getString("phone"));
				escort.setEmail(rs1.getString("email"));
				escort.setPassword(rs1.getString("password"));
				escort.setSite(rs1.getString("site"));
				escort.setSiteName(rs1.getString("site_name"));
				escorts.add(escort);
			}
		} catch (Exception e) {
			System.out.println("errro" + e);
		}
		finally{
			
			DbConnect.closeResultSet(rs1);
			DbConnect.closeStatement(st1);
			DbConnect.closeConnection(con);
			
		}
		return escorts;
	}
	
	
	
	public boolean validateEscort(EscortDto dto )
	{
		PreparedStatement st1 = null;
		boolean flag=false;
		ResultSet rs1 = null;
		ArrayList<EscortDto> escorts = null;
		Connection con = null;
		try {
			rs1 = null;
			st1 = null;
		DbConnect ob = DbConnect.getInstance();
		con=ob.connectDB();
		
			 escorts = new ArrayList<EscortDto>();
			 /*
			  * for crossdomain
			  */
			//st1= con.prepareStatement("select * from escort e join trip_details td on e.id=td.escortId where e.escortClock=? and e.password =?");
			 /*
			  * for siemens
			  */
			 st1= con.prepareStatement("select * from escort e join trip_details td on e.id=td.escortId where e.escortClock=? and td.escortPswd=? and td.id=?");
			st1.setString(1, dto.getEscortClock());
			st1.setString(2, dto.getPassword());
			st1.setString(3, dto.getTripId());
			rs1 = st1.executeQuery();
			if (rs1.next()) {
				flag=true;
			}
			}catch(Exception e)
			{
				System.out.println("Error in validateEscort in EscortDao :"+ e);
			}
		finally{
			DbConnect.closeResultSet(rs1);
			DbConnect.closeStatement(st1);
			DbConnect.closeConnection(con);
			
		}
		return flag;
	}


	public int addEscort(EscortDto dto, String changedBy) {
		// TODO Auto-generated method stub
		int val=-1;
		DbConnect db = DbConnect.getInstance();
		Connection con = null;
		PreparedStatement stmt = null;
		PreparedStatement existPst = null;
		ResultSet autogenrs= null;
		ResultSet rs = null;
		try {
			
			con=db.connectDB();
			con.setAutoCommit(false);
			System.out.println("getEscortClock " + dto.getEscortClock() +"getName" + dto.getName() +"getAddress " + dto.getAddress() 
					+"getPhone " + dto.getPhone() +"getEmail " + dto.getEmail() +"getPassword " + dto.getPassword() 
					 +"getSite " + dto.getSite());
			existPst = con.prepareStatement("select * from escort where escortClock=?");
			existPst.setString(1, dto.getEscortClock());
			System.out.println("______1_______");
			rs = existPst.executeQuery();
			System.out.println("_____2________");
			if(rs.next()==false) {
				System.out.println("_____3_______");
				stmt = con.prepareStatement("insert into escort (escortClock, name, Address, phone, email, password, site ) values ( ?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
				
				stmt.setString(1, dto.getEscortClock());
				stmt.setString(2, dto.getName());
				stmt.setString(3, dto.getAddress());
				stmt.setString(4, dto.getPhone());
				stmt.setString(5, dto.getEmail());
				stmt.setString(6, dto.getPassword());
				stmt.setString(7, dto.getSite());
				val=stmt.executeUpdate();
				System.out.println("Valu e : "+ val );
				if (val== 1) {
					 
					   autogenrs = stmt.getGeneratedKeys();
					if(autogenrs.next()) {
						long autoincNumber = autogenrs.getInt(1);
					
						new AuditLogDAO().auditLogEntry(autoincNumber, AuditLogConstants.ESCORT_MODULE,   Integer.parseInt(changedBy), "", AuditLogConstants.WORKFLOW_STATE_ESCORT_ADDED, AuditLogConstants.AUDIT_LOG_CREATE_ACTION, con);
						con.commit();
						message="Escort inserted succefully..";
					} else {
						val=0;
						message="Escort insertion failed (audit log error )..";
					}
					
				} else {
					message="Escort insertion failed..";
				}
				 
			} else {
				message = "Escort with " + dto.getEscortClock() + " already exists.";
				
			}
			
		} catch(Exception e) {
			message ="Error : " + e;
			System.out.println("Error in  addEscort in EscortDao : " +e);
		}
		finally {
			DbConnect.closeResultSet(rs,autogenrs);
			DbConnect.closeStatement(stmt, existPst );
			DbConnect.closeConnection(con);
		}
		return val;
	}


	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int updateEscort(EscortDto dto) {
		// TODO Auto-generated method stub
		int val=-1;
		DbConnect db = DbConnect.getInstance();
		Connection con = null;
		PreparedStatement stmt = null;
		 
		
		try {
			con=db.connectDB();
			stmt = con.prepareStatement("update escort set escortClock=?, name=?, Address=?, phone=?, email=?, password=?, site=? where id=? ");
			stmt.setString(1, dto.getEscortClock());
			stmt.setString(2, dto.getName());
			stmt.setString(3, dto.getAddress());
			stmt.setString(4, dto.getPhone());
			stmt.setString(5, dto.getEmail());
			stmt.setString(6, dto.getPassword());
			stmt.setString(7, dto.getSite());
			stmt.setString(8, dto.getId());
			val=stmt.executeUpdate();
			
		} catch(Exception e) {
			System.out.println("Error in  addEscort in EscortDao : " +e);		
	} finally {
		DbConnect.closeStatement(stmt);		 
		DbConnect.closeConnection(con);
	}
		return val;
		 
	}

	public int assignTripEscort(ArrayList<EscortDto> escortList) {
		// TODO Auto-generated method stub
		System.out.println("IN assign tripEscort");
		DbConnect ob = null;
		Connection con = null;
		PreparedStatement pst1 = null;
		PreparedStatement pst2 = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		int retVal = -1;
		try {
			RandomString rd= new RandomString(4);
			ob = DbConnect.getInstance();
			con = ob.connectDB();
			String updateQuery = "update trip_details set  escortId=?, escortPswd=ifnull(escortPswd,?) where id=? ";
			System.out.println(updateQuery);
			pst = con.prepareStatement(updateQuery);
			for (EscortDto dto : escortList) {
				
				 if(dto.getId()!=null)
				 {
					// System.out.println("Escort Id : "+ dto.getId());
					 EscortDto escortDto = this.getEscortById(dto.getId());
				 String password= rd.nextEscortString(  dto.getTripId());//passwordGenerator.nextString();
				// for crossdomain
				//	  String password = escortDto.getPassword();
				//System.out.println("@@ Escort password : "+password);
				 if(SettingsConstant.escortpwd.equalsIgnoreCase("mobileNo"))
				 {
					 password=new EscortDao().TripEscortPassword(dto.getId());
				 }
				 System.out.println("dao escort Id"+dto.getId());
				pst.setString(1, dto.getId());
				pst.setString(2, password); 				 
				pst.setString(3, dto.getTripId());
				//System.out.println("update trip_details set  escortId='"+dto.getId()+"', escortPswd=ifnull(escortPswd,'"+password+"') where id='"+dto.getTripId()+"' ");
				int result=pst.executeUpdate();
				retVal += result;
				pst2 = con.prepareStatement("select * from vendor_trip_sheet_escort where tripId=? ");
				pst2.setString(1, dto.getTripId());
				rs = pst2.executeQuery();
				if(rs.next()==false) {
				System.out.println("nothing ... ");					
				pst1 = con.prepareStatement("insert into vendor_trip_sheet_escort (tripId, escortId, showStatus) values (?, ?, ?)");
				pst1.setString(1, dto.getTripId());
				pst1.setString(2, dto.getId());
				pst1.setString(3, "No Show");
				pst1.executeUpdate();
				}  			if(result>0) {
					SMSService sms= new SMSService();
					sms.sendPasswordToEscort (dto.getTripId(),password);
				}
				 }
 
			}

		} catch (Exception e) {
			System.out.println("Error in get tirp code DAOOOOOOO" + e);
		} finally {
			DbConnect.closeStatement(pst1, pst2);
			 
			DbConnect.closeConnection(con);
		}
		return retVal;
	}
	
	public String TripEscortPassword(String escortid) {
		DbConnect ob = null;
		Connection con = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		String password="";
		try {
			ob = DbConnect.getInstance();
			con = ob.connectDB();
			String Query = "SELECT RIGHT(PHONE,10) AS PHONE FROM ESCORT WHERE ID=? ";
			pst = con.prepareStatement(Query);
				pst.setString(1, escortid);
				rs = pst.executeQuery();
				while(rs.next())
				{
					password=rs.getString("PHONE");
				}  						

		} catch (Exception e) {
			System.out.println("Error in get tirp code DAOOOOOOO" + e);
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(pst);			 
			DbConnect.closeConnection(con);
		}
		return password;


	}
	public int alterEscortStatus(String tripId,String status)
	{
		String change;
		if(status.equalsIgnoreCase("Add Escort"))
		{
			change="YES";
		}
		else
		{
			change="NO";
		}
		

	String q ="Update trip_details  SET  security_status ='"+change+"' WHERE id="+tripId;
	DbConnect ob = DbConnect.getInstance();
	Statement st = null;
	int i=0;
	Connection con = ob.connectDB();
	
		try {
			st = con.createStatement();
			i=st.executeUpdate(q);
		    System.out.println("Query to alter escort status"+q);
		
       return i;			
					
		} catch (Exception e) {
			// TODO Auto-generated catch block
	       e.printStackTrace();
		} finally {
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);
		}
		return i;


	}
	
public int escortGetInbyPassword(String tripid,String password,double latitude,double longitude)
{
	int result=0;
	DbConnect ob = null;
	Connection con = null;
	PreparedStatement pst = null,pst1=null,pst2=null,pst3=null,pst4=null;
	ResultSet rs = null,rs1=null;
	try{
		ob = DbConnect.getInstance();
		con = ob.connectDB();
		String Query = "SELECT id,escortclock FROM ESCORT WHERE RIGHT(PHONE,10)=?";
		pst = con.prepareStatement(Query);
		pst.setString(1, password);
		rs = pst.executeQuery();
		if(rs.next())
		{
			pst1 = con
					.prepareStatement("select * from  vendor_trip_sheet_escort where showstatus='Show' and tripId='"
							+ tripid + "' ");
			rs1 = pst1.executeQuery();
			if (rs1.next()) {
				result=-1;
				setMessage("Security is already authenticated");
			} else {
				String updatequery="UPDATE TRIP_DETAILS SET ESCORTID="+rs.getString("id")+" where id="+tripid;
				pst3=con.prepareStatement(updatequery);
				pst3.executeUpdate();
				String updateQuery1 = " update vendor_trip_sheet_parent set escort='YES', escortClock= '"
						+ rs.getString("escortclock")
						+ "' where tripId="
						+ tripid;
		
				pst2 = con
						.prepareStatement(updateQuery1);
				int retVal = pst2.executeUpdate();
				String escortGetInQuery =" update vendor_trip_sheet_escort set escortid="+rs.getString("id")+",showStatus='Show', curStatus='IN', inLatitude= '"
						+ latitude
						+ "', inLongitude='" + longitude + "', inTime=curtime() where tripId="
						+ tripid;
				System.out.println("escortGetInQuery "+escortGetInQuery);
				pst4 = con
						.prepareStatement(escortGetInQuery);
				retVal = pst4.executeUpdate();
				if(retVal==1)
				result=1;
		} 
		
		}
	}catch(Exception e){e.printStackTrace();}
	finally {
		DbConnect.closeResultSet(rs,rs1);
		DbConnect.closeStatement(pst,pst1,pst2);
		DbConnect.closeConnection(con);
	}
	return result;
}

	

	
}
