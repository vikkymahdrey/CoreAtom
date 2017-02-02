package com.agiledge.atom.mobile.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.agiledge.atom.dbConnect.DbConnect;
import com.agiledge.atom.dto.DriverDto;


public class ValidateDriverDao {



	public boolean checkUser(String userName,String password,String imei) {
		DbConnect ob = DbConnect.getInstance();
		PreparedStatement pst = null;
		ResultSet rs = null;
		Connection con = ob.connectDB();
		int uid=0;
		int vhicleId=0; 
		String tripid="";
		 
		try {		
			// old authentication query : select d.*,dv.vehicleId as vehicleId from driver d,driver_vehicle  dv where dv.driverId=d.id and d.username=? and d.password=?
			
			pst = con.prepareStatement("select d.*,td.vehicle as vehicleId,td.id as tripid from driver d,trip_details td where d.username=? and   trip_date between date_add(curdate(), interval -3 DAY) AND date_add(curdate(), interval 3 DAY) and d.Id=td.driverId and td.driverPswd=?");
			
			pst.setString(1, userName);
			pst.setString(2, password);			
			System.out.println("select d.*,td.vehicle as vehicleId from driver d,trip_details td where d.username='"+userName+"' and   trip_date between date_add(curdate(), interval -3 DAY) AND date_add(curdate(), interval 3 DAY) and d.Id=td.driverId and td.driverPswd='"+password+"'");
			rs = pst.executeQuery();
			if (rs.next()) {
				
				uid = rs.getInt("id");				 
				vhicleId = rs.getInt("vehicleId");
				tripid= rs.getString("tripid");
				System.out.println(uid+"NEXT >>>>>>>Next"+vhicleId);
				pst = con.prepareStatement("insert into driver_logins(driverId,imei_number,vehicleId,status, datetime,tripid) values(?,?,?,?,curdate(),?)");
				pst.setInt(1,uid);
				pst.setInt(3,vhicleId);
				pst.setString(2,imei);
				pst.setString(4, "logged");
				pst.setString(5, tripid);
				pst.executeUpdate();
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			System.out.println("error : " + e);
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);
		}
		return false;		
	}
	
	public DriverDto checkUserOTP( String password,String imei) {
		System.out.println("Inside checkUserOTP");
		DbConnect ob = DbConnect.getInstance();
		PreparedStatement pst = null;
		DriverDto driverDto =null;
		ResultSet rs = null;
		Connection con = ob.connectDB();
		int uid=0;
		int vhicleId=0; 
		String tripid="";
		String username=""; 
		try {		
			// old authentication query : select d.*,dv.vehicleId as vehicleId from driver d,driver_vehicle  dv where dv.driverId=d.id and d.username=? and d.password=?
			
			pst = con.prepareStatement("select d.*,td.vehicle as vehicleId,td.id as tripid from driver d,trip_details td where  trip_date between date_add(curdate(), interval -3 DAY) AND date_add(curdate(), interval 3 DAY) and d.Id=td.driverId and td.driverPswd=?");
			
			
			pst.setString(1, password);			
			 
			rs = pst.executeQuery();
			if (rs.next()) {
				 System.out.println("TRAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAaaaaK - 1");	
				uid = rs.getInt("id");				 
				vhicleId = rs.getInt("vehicleId");
				tripid= rs.getString("tripid");
				username = rs.getString("username");
				 System.out.println("TRAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAaaaaK - 2");
				System.out.println(uid+"NEXT >>>>>>>Next"+vhicleId);
				pst = con.prepareStatement("insert into driver_logins(driverId,imei_number,vehicleId,status, datetime,tripid) values(?,?,?,?,curdate(),?)");
				pst.setInt(1,uid);
				pst.setInt(3,vhicleId);
				pst.setString(2,imei);
				pst.setString(4, "logged");
				pst.setString(5, tripid);
				pst.executeUpdate();
				driverDto = new DriverDto();
				driverDto .setUsername(username);
				driverDto.setPassword(password);
				driverDto.setTripId(tripid);
				driverDto.setVehicleId(vhicleId);
			} 
		} catch (Exception e) {
			System.out.println("error : " + e);
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);
		}
		return driverDto;		
	}


	public boolean checkLogged(String imei) {
		DbConnect ob = DbConnect.getInstance();
		PreparedStatement pst = null;
		ResultSet rs = null;
		Connection con = ob.connectDB();
		try {		
			//System.out.println("select * from driver_logins where imei_number='"+imei+"' and status='logged'");
		pst = con.prepareStatement("select * from driver_logins where imei_number=? and status='logged'");
		pst.setString(1, imei);
		rs=pst.executeQuery();
		if(rs.next())
		{
		return true;	
		}
		else
		{
		return false;	
		}	
	} catch (Exception e) {
		System.out.println("error : " + e);
	} finally {
		DbConnect.closeResultSet(rs);
		DbConnect.closeStatement(pst);
		DbConnect.closeConnection(con);
	}

				
		return false;
	}

	public int logout(String imei) {
		DbConnect ob = DbConnect.getInstance();
		PreparedStatement pst = null;
		int retval=0;
		Connection con = ob.connectDB();
		try {		
		pst = con.prepareStatement("update driver_logins set status='logout' where imei_number=?");
		pst.setString(1, imei);	
		retval=pst.executeUpdate();
		}
		catch(Exception e)
		{
		System.out.println("error in logout() " +e);	
		}
	 finally {

		DbConnect.closeStatement(pst);
		DbConnect.closeConnection(con);
	}
		return retval;
		}

	public int logout(String imei,String userName) {
		DbConnect ob = DbConnect.getInstance();
		PreparedStatement pst = null;
		int retval=0;
		Connection con = ob.connectDB();
		try {		
		pst = con.prepareStatement("update driver_logins set status='logout' where imei_number=? or driverId=(select id from driver where  username=? limit 1)");
		pst.setString(1, imei);	
		pst.setString(2, userName);	
		retval=pst.executeUpdate();
		}
		catch(Exception e)
		{
		System.out.println("error" +e);	
		}
	 finally {

		DbConnect.closeStatement(pst);
		DbConnect.closeConnection(con);
	}
		return retval;
		}
	
	/* Logout Driver using their imei number
	 * 
	 */

	public int logoutImei(String imei) {
		//  
		DbConnect ob = DbConnect.getInstance();
		PreparedStatement pst = null;
		int retval=0;
		Connection con = ob.connectDB();
		try {		
		pst = con.prepareStatement("update driver_logins set status='logout' where imei_number=?  ");
		pst.setString(1, imei);	
		 	
		retval=pst.executeUpdate();
		}
		catch(Exception e)
		{
		System.out.println("error" +e);	
		}
	 finally {

		DbConnect.closeStatement(pst);
		DbConnect.closeConnection(con);
	}
		return retval;
	}

}
