package com.agiledge.atom.device.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.httpclient.UsernamePasswordCredentials;

import com.agiledge.atom.commons.OtherFunctions;
import com.agiledge.atom.dbConnect.DbConnect;
import com.agiledge.atom.device.dto.DeviceDto;
import com.agiledge.atom.dto.DriverDto;
import com.agiledge.atom.dto.VehicleDto;
import com.agiledge.atom.trip.dto.AplDistanceDto;

public class DeviceDao {

	private String message;
	public int addUpdateDevice(DeviceDto dto) {
		System.out.println("TTTTTTTTTTTTTTTTTTTTT");
		DbConnect ob = DbConnect.getInstance();
		PreparedStatement pst = null;
		PreparedStatement checkPst = null;
		PreparedStatement updatePst = null;
		ResultSet rsCheck = null;
		Connection con = ob.connectDB();
		int returnInt = 0;
		try {
			con.setAutoCommit(false);
			System.out.println("TTTTTTTTTTTTTTTTTTTTT");
			String checkQuery = "select * from device where imei=? ";
			String query = "insert into device ( model, company, imei, status ) values (?, ?, ?, 'active')";
			String updatePstQuery = "update device set model=?, company=?, status='active' where imei=? and status='active'";
				checkPst = con.prepareStatement(checkQuery);
				updatePst = con.prepareStatement(updatePstQuery);
				 
				pst = con.prepareStatement(query);
				 
					 
						checkPst.setString(1, dto.getImei());
						 rsCheck = checkPst.executeQuery();
						if(rsCheck.next()) {
							updatePst.setString(1, dto.getModel());
							updatePst.setString(2, dto.getCompany());
							updatePst.setString(3, dto.getImei());
							System.out.println("update device set model='"+dto.getModel()+"', company='"+dto.getCompany()+"', status='active' where imei='"+dto.getImei()+"' and status='ative'");
							updatePst.executeUpdate();
							System.out.println("............");
							returnInt= returnInt + 1;
						} else {
							 
							 
							pst.setString(1, dto.getModel());
							pst.setString(2, dto.getCompany()); 
							pst.setString(3, dto.getImei() );
							 
							int val = pst.executeUpdate();
							if(val <=0) {
								throw new Exception("Insertion failed");
								
							} 
							returnInt = returnInt + val;
						}
						
					 
					con.commit();
					  

				 
		 
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



		return returnInt;
		
	}
	
/*	String updatePstQuery = "insert into device_vehicle (vehicleId, deviceId, fromDate,   status) select ?, d.id, now(), 'a',  from device d where d.imei=?  ";
	  
	pst = con.prepareStatement(updatePstQuery);
	 
		  
				pst.setString(1, vehicleDto.getId());
				  
				pst.setString(3, deviceDto.getImei() );
				 
				int val = pst.executeUpdate();
*/
	public int mapDeviceVehicle(DeviceDto deviceDto, VehicleDto vehicleDto ) {
 

			DbConnect ob = DbConnect.getInstance();
			PreparedStatement pst = null;
			PreparedStatement checkPst = null;
			PreparedStatement updatePst = null;
			ResultSet rsCheck = null;
			Connection con = ob.connectDB();
			int returnInt = 0;
			try {
				con.setAutoCommit(false);
				String checkQuery = "select * from device_vehicle dv, device d  where d.id=dv.deviceId and dv.status='active' and d.imei=? ";
				String query = "insert into device_vehicle (vehicleId, deviceId, fromDate,   status) select ?, d.id, now(), 'active',  from device d where d.imei=? ";
				String updatePstQuery = "update device_vehicle dv, device d set dv.deviceId=d.id, vehicleId=?  where d.imei=? and dv.status='active'";
					checkPst = con.prepareStatement(checkQuery);
					updatePst = con.prepareStatement(updatePstQuery);
					 
					pst = con.prepareStatement(query);
					 
						 
							checkPst.setString(1, deviceDto.getImei());
							 rsCheck = checkPst.executeQuery();
							if(rsCheck.next()) {
								updatePst.setString(1, vehicleDto.getId());
								updatePst.setString(2, deviceDto.getImei());
								 
								updatePst.executeUpdate();
								returnInt= returnInt + 1;
							} else {
								 
								 
								pst.setString(1, vehicleDto.getId());
								pst.setString(2, deviceDto.getImei()); 
								 
								 
								int val = pst.executeUpdate();
								if(val <=0) {
									throw new Exception("Insertion failed");
									
								} 
								returnInt = returnInt + val;
							}
							
						 
						con.commit();
						  

					 
			 
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



			return returnInt;
		
	}
	

	public int mapDeviceVehicleAuto(String imei, String driverUserName ) {
		 
		System.out.println("inside mapDevice invokation");
		DbConnect ob = DbConnect.getInstance();
		PreparedStatement pst = null;
		PreparedStatement checkPst = null;
		PreparedStatement updatePst = null;
		PreparedStatement vehiclePst = null; 
		ResultSet rsCheck = null;
		ResultSet vehicleRs = null;
		Connection con = ob.connectDB();
		int returnInt = 0;
		try {
			 
			con.setAutoCommit(false);
			String checkQuery = "select * from device_vehicle dv, device d  where d.id=dv.deviceId and dv.status='active' and d.imei=? ";
			String query = "insert into device_vehicle (vehicleId, deviceId, fromDate,   status) select ?, d.id, now(), 'active'  from device d where d.imei=? ";
			String queryVehicle= "select * from driver_vehicle dv, driver d where dv.driverid=d.id and d.username=? and d.status='a' ;";
			String updatePstQuery = "update device_vehicle dv, device d set dv.deviceId=d.id, vehicleId=?  where dv.deviceId=d.id and d.imei=? and dv.status='active'";
				checkPst = con.prepareStatement(checkQuery);
				updatePst = con.prepareStatement(updatePstQuery);
				vehiclePst = con.prepareStatement(queryVehicle);
			//	System.out.println(queryVehicle+" username : " + driverUserName);
				pst = con.prepareStatement(query);
				 vehiclePst.setString(1, driverUserName);
				 vehicleRs = vehiclePst.executeQuery();
				 System.out.println("sprign...");
				 if(vehicleRs.next()) {
					  			 
					 String vehicleId = vehicleRs.getString("vehicleId");
					 System.out.println("has vehicle vehicleId ; "+ vehicleId );
						checkPst.setString(1, imei);
						 rsCheck = checkPst.executeQuery();
						if(rsCheck.next()) {
							System.out.println("  has rupees  "  );		
							updatePst.setString(1, vehicleId);
							updatePst.setString(2, imei);
							 
							updatePst.executeUpdate();
							returnInt= returnInt + 1;
						} else {
							 
							 
							pst.setString(1, vehicleId);
							pst.setString(2, imei); 
							 
							 
							int val = pst.executeUpdate();
							if(val <=0) {
								throw new Exception("Insertion failed");
								
							} 
							returnInt = returnInt + val;
						}
						
					 
					con.commit();
					  

				 }
		 
		} catch (Exception e) {
			returnInt =0;
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



		return returnInt;
	
}


	
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}

}
