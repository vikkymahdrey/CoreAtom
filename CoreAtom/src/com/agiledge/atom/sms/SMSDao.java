package com.agiledge.atom.sms;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import com.agiledge.atom.dbConnect.DbConnect;
import com.agiledge.atom.dto.EmployeeDto;


public class SMSDao {
	/*
	 * public String[] getmobileNO(String imei)
	 * 
	 * }
	 */
	public static boolean checkClosed(String tripId) {
		DbConnect ob = DbConnect.getInstance();
		PreparedStatement pst = null;
		ResultSet rs = null;
		Connection con = ob.connectDB();
		boolean flag = false;
		try {
			pst = con
					.prepareStatement("select * from panicaction where tripid="
							+ tripId + " and curStatus='closed'");
			rs = pst.executeQuery();
			if (rs.next()) {
				flag = true;
			}
		} catch (Exception e) {
			System.out.println("error" + e);
		} finally {
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);
		}
		return flag;
	}
	public ArrayList<EmployeeDto> getEmpployeePinAndMob(String tripId) {
		DbConnect ob = DbConnect.getInstance();
		PreparedStatement pst = null;
		ResultSet rs = null;
		Connection con = ob.connectDB();
		ArrayList<EmployeeDto> empdtos=new ArrayList<EmployeeDto>();
		EmployeeDto dto=null;
		
		try {
			pst = con
					.prepareStatement("select e.displayname,e.contactNumber1,vts.keypin from employee e,vendor_trip_sheet vts where vts.tripid="+ tripId + " and vts.employeeId=e.id");
			rs = pst.executeQuery();
			while (rs.next()) {
				dto=new EmployeeDto();
				dto.setDisplayName("displayname");
				dto.setContactNo(rs.getString("contactNumber1"));
				dto.setKeyPin(rs.getString("keypin"));
				empdtos.add(dto);	
			}
		} catch (Exception e) {
			System.out.println("error" + e);
		} finally {
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);
		}
		return empdtos;
	}
	
	
	public String[] getMobileNosbyIds(String driverid,String empid)
	{
		DbConnect ob = DbConnect.getInstance();
		PreparedStatement pst = null;
		ResultSet rs = null;
		Connection con = ob.connectDB();
		String[] mobileNo=new String[2];
		try {
			pst = con
					.prepareStatement("select d.contact as drivercontact,e.contactnumber1 as employeecontact from driver d left join employee e on e.id="+empid+" where d.id="+driverid );
			rs = pst.executeQuery();
			if(rs.next())
				mobileNo[0]=rs.getString("drivercontact");
			    mobileNo[1]=rs.getString("employeecontact");
			  
				
	}catch(Exception e){e.printStackTrace();}
	finally{
		DbConnect.closeResultSet(rs);
		DbConnect.closeStatement(pst);
		DbConnect.closeConnection(con);
	}
		return mobileNo;
	}
	public static boolean panicEmpcheckClosed(String imei,String empid,String tripId) {
		DbConnect ob = DbConnect.getInstance();
		PreparedStatement pst = null;
		ResultSet rs = null;
		Connection con = ob.connectDB();
		boolean flag = false;
		try {
			pst = con
					.prepareStatement("SELECT STATUS FROM EMPLOYEE_PANIC WHERE EMPID="+empid+" AND IMEI="+imei+" AND TRIPID="+tripId+" AND STATUS='deactivated'");
			rs = pst.executeQuery();
			if (rs.next()) {
				flag = true;
			}
		} catch (Exception e) {
			System.out.println("error" + e);
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);
		}
		return flag;
	}
	
	public String getEmployeePanicMsg(String empid,String tripid)
	{
		DbConnect ob = DbConnect.getInstance();
		String message="";
		PreparedStatement pst = null;
		ResultSet rs = null;
		Connection con = ob.connectDB();
		try {
			pst = con.prepareStatement("SELECT V.REGNO,e.displayname FROM TRIP_DETAILS TD JOIN VEHICLES V ON V.ID=TD.VEHICLE left join employee e on e.id="+empid+" WHERE TD.ID="+tripid);
			rs = pst.executeQuery();
			if(rs.next())
			{
				message="Panic Alarm has been pressed by the employee "+rs.getString("displayname")+"in vehicle "
						+rs.getString("regno")+ " Please take immediate action";
			}
		}catch(Exception e){e.printStackTrace();}
		finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);
		}
		return message;
	}
	public boolean sendTripStartSMS(String tripId)
	{
		DbConnect ob = DbConnect.getInstance();
		PreparedStatement pst = null;
		ResultSet rs = null;
		Connection con = ob.connectDB();
		String msg=null;
		String mobile = null;
		boolean flag= true;
		try {
			
			pst = con
					.prepareStatement("select e.displayname,e.contactnumber1,now() as time ,t.trip_log  from trip_details_child tc join employee e on tc.employeeid=e.id join trip_details t on t.id=tc.tripid  where tc.tripId="
							+ tripId + "");
			rs = pst.executeQuery();
			
			
			
			while (rs.next())

			{
				if(rs.getString("trip_log").equalsIgnoreCase("IN"))
				{
				mobile = rs.getString("contactnumber1");
				msg= "Hi "
						+ rs.getString("displayname")
						+ ", your trip has been started at "
						+ rs.getString("time").substring(11,16)
						+ ". please reach your pickup point 10min prior to your scheduled time." 
						+" From : Transport Dept.";

				SendSMS sendSMS = SendSMSFactory.getSMSInstance();
				sendSMS.send(mobile, msg);
				
				}

			}

				
	}catch(Exception e){e.printStackTrace();
	flag= false;}
	finally{
		DbConnect.closeResultSet(rs);
		DbConnect.closeStatement(pst);
		DbConnect.closeConnection(con);
	}
		return flag;
	}
	
}