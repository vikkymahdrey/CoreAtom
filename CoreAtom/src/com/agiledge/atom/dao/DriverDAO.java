package com.agiledge.atom.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.agiledge.atom.constants.AuditLogConstants;
import com.agiledge.atom.dbConnect.DbConnect;
import com.agiledge.atom.dto.DriverDto;

public class DriverDAO {
	private String message;

	public int addDriver(DriverDto driverDto, String changedBy) {
		DbConnect ob = DbConnect.getInstance();
		PreparedStatement pst = null;
		PreparedStatement pstCheck = null;
		ResultSet rs = null;
		ResultSet rsAuto = null;
		Connection con = ob.connectDB();
		int retVal = 0;
		try {
			con.setAutoCommit(false);
			pstCheck = con.prepareStatement(" select * from driver where username=? and status='a'");
			System.out.println(" select * from driver where username='"+driverDto.getUsername()+"' and status='a'" );
			pstCheck.setString(1,  driverDto.getUsername());
			rs = pstCheck.executeQuery();
			if(rs.next()) {
				message = "User name already exists";
				retVal=0;
			} else {
				System.out.println(" user name not exist");
			pst = con
					.prepareStatement("insert into driver(name,address,username,password,vendorId,contact, status) values (?,?,?,?,?,?, ?)", Statement.RETURN_GENERATED_KEYS);
			pst.setString(1, driverDto.getDriverName());
			pst.setString(2,
					driverDto.getAddress1());
			pst.setString(3, driverDto.getUsername());
			pst.setString(4, driverDto.getPassword());
			pst.setString(5, driverDto.getVendor());
			pst.setString(6, driverDto.getContactNo());
			pst.setString(7, "a");
			retVal = pst.executeUpdate();
			if(retVal>0) {
				rsAuto = pst.getGeneratedKeys();
				rsAuto.next();
				long autoInt = rsAuto.getLong(1);
				new AuditLogDAO().auditLogEntry(autoInt, AuditLogConstants.DRIVER_MODULE, Integer.parseInt(changedBy) , "",AuditLogConstants.WORKFLOW_STATE_DRIVER_ADDED  , AuditLogConstants.AUDIT_LOG_CREATE_ACTION, con);
				con.commit();
				message= "Driver insertion success";
				con.commit();
				
			} else {
				message= "Driver insertion failed";
				retVal=0;
				con.rollback();
			}
			}
		} catch (Exception e) {
			System.out.println("Error in inserting" + e);
			message = "Error : " +e;
			try {
				con.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} finally {
			DbConnect.closeResultSet(rs, rsAuto);
			DbConnect.closeStatement(pst, pstCheck);
			DbConnect.closeConnection(con);
		}
		return retVal;
	}
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int UpdateDriver(DriverDto driverDto) {
		DbConnect ob = DbConnect.getInstance();
		PreparedStatement pst = null;
		Connection con = ob.connectDB();
		int retVal = 0;
		try {
			System.out.println(driverDto.getDriverId());
			pst = con
					.prepareStatement("update driver set name=?,address=?,username=?,password=?,vendorId=?,contact=? where id=? ");
			
			
			pst.setString(1, driverDto.getDriverName());
			pst.setString(2, 
					driverDto.getAddress1());
			pst.setString(3, driverDto.getUsername());
			pst.setString(4, driverDto.getPassword());
			pst.setString(5, driverDto.getVendor());
			pst.setString(6, driverDto.getContactNo());
			pst.setInt(7, driverDto.getDriverId());
			retVal = pst.executeUpdate();
		} catch (Exception e) {
			System.out.println("Error in UpdateDriver in DriverDao" + e);
		} finally {
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);
		}
		System.out.println("RETVAL:"+retVal);
		return retVal;
	}



	public ArrayList<DriverDto> getAllDriver() {

		DbConnect ob = DbConnect.getInstance();
		PreparedStatement pst = null;
		ResultSet rs = null;
		Connection con = ob.connectDB();
		ArrayList<DriverDto> driverDtos = new ArrayList<DriverDto>();
		DriverDto driverDto = null;

		try {
			pst = con
					.prepareStatement("select d.*,v.regNo from driver d left outer join driver_vehicle dv  on d.id=dv.driverId left outer join vehicles v on dv.vehicleId=v.id");
			rs = pst.executeQuery();
			while (rs.next()) {
				driverDto = new DriverDto();
				driverDto.setDriverId(rs.getInt("id"));
				driverDto.setDriverName(rs.getString("name"));
				driverDto.setAddress1(rs.getString("address"));
				driverDto.setVehicle(rs.getString("regNo"));
				driverDto.setContactNo(rs.getString("contact"));
				driverDto.setUserId(rs.getString("username"));
				driverDto.setVendor(rs.getString("vendorId"));
			
				System.out.println(driverDto.getUsername());
				
				driverDtos.add(driverDto);
			}

		} catch (Exception e) {

		} finally {
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);
		}
		return driverDtos;

	}

	public ArrayList<DriverDto> getDrivers(String vehicleId) {

		DbConnect ob = DbConnect.getInstance();
		PreparedStatement pst = null;
		ResultSet rs = null;
		Connection con = ob.connectDB();
		ArrayList<DriverDto> driverDtos = new ArrayList<DriverDto>();
		DriverDto driverDto = null;

		try {
			String query = "select d.id,d.name,vm.company from driver d,vendormaster vm where d.vendorId=vm.id and d.id not in (select driverId from driver_vehicle where vehicleid='"
					+ vehicleId + "')and d.vendorid =(select vendor from vehicles where id='"
					+ vehicleId + "')";
			System.out.println(query);
			pst = con.prepareStatement(query);
			rs = pst.executeQuery();
			while (rs.next()) {
				driverDto = new DriverDto();
				driverDto.setDriverId(rs.getInt("id"));
				driverDto.setDriverName(rs.getString("name"));
				driverDto.setVendor(rs.getString("company"));
				driverDtos.add(driverDto);
			}

		} catch (Exception e) {
			System.out.println("ERROR" + e);
		} finally {
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);
		}

		return driverDtos;
	}

	public int DeleteDriver(DriverDto driverDto) {
		// delete driver using driverId
		DbConnect ob = DbConnect.getInstance();
		PreparedStatement pst = null;
		Connection con = ob.connectDB();
		int retVal = 0;
		try {
			System.out.println(driverDto.getDriverId());
			pst = con
					.prepareStatement("Delete driver  where id=? ");
			
			 
			pst.setInt(1, driverDto.getDriverId());
			retVal = pst.executeUpdate();
		} catch (Exception e) {
			System.out.println("Error in DeleteDriver in DriverDao" + e);
		} finally {
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);
		}
		System.out.println("RETVAL:"+retVal);
		return retVal;

	}
	
	public  DriverDto  getDriver(String driverId) {

		DbConnect ob = DbConnect.getInstance();
		PreparedStatement pst = null;
		ResultSet rs = null;
		Connection con = ob.connectDB();
		 
		DriverDto driverDto = null;

		try {
			String query = "select id, name, address, username, vendorId, contact from driver where id=? ";
			
			System.out.println(query);
			pst = con.prepareStatement(query);
			pst.setString(1, driverId);
			rs = pst.executeQuery();
			if(rs.next()) {
				driverDto = new DriverDto();
				driverDto.setDriverId(rs.getInt("id"));
				driverDto.setDriverName(rs.getString("name"));
				driverDto.setVendor(rs.getString("vendorId"));
				driverDto.setContactNo(rs.getString("contact"));
				if(rs.getString("contact")!=null) {
					System.out.println("CONTACT : " + driverDto.getContactNo());
				}
				driverDto.setUsername(rs.getString("username"));
				driverDto.setAddress1(rs.getString("address"));
				
				 
			}

		} catch (Exception e) {
			System.out.println("ERROR" + e);
		} finally {
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);
		}

		return driverDto;
	}

	public String getDriverPassword(String id)
	{
		String query="select RIGHT(contact,10) as password from driver where id=?";
		DbConnect ob = DbConnect.getInstance();
		PreparedStatement stmt =null;
		ResultSet rs=null;
		Connection con = ob.connectDB();
		String password="";
		try {
			  stmt = con.prepareStatement(query);
			  stmt.setString(1, id);
			  rs= stmt.executeQuery();
			  while(rs.next())
			  {
				  password=rs.getString("password");
			  }
			
		}catch(Exception e)
		{
			System.out.println("Excepion (password) : " + e);
		}
		finally{
			
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(stmt);
			DbConnect.closeConnection(con);
		
		}
		return password;
	}
}
