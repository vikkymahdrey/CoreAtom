package com.agiledge.atom.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.agiledge.atom.dbConnect.DbConnect;
import com.agiledge.atom.dto.VehicleDto;

public class VendorBaseVehicleStatusDao {
	public String getUserType(String employeeId)
	{
		DbConnect ob = DbConnect.getInstance();
		Statement st = null;
		ResultSet rs = null;
		String userType= null;
		Connection con = ob.connectDB();

			try {
				st = con.createStatement();
				rs = st.executeQuery("select usertype from employee where id="+employeeId);
				while(rs.next())
				{
					userType= rs.getString("usertype");
				}
				return userType;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				System.out.println("Error in VendorstatusDao" + e);
			}
			finally {
				DbConnect.closeStatement(st);
				DbConnect.closeConnection(con);
			}

			
		return userType;
	}
	public String getvendorCompany(String vendorId) {
		DbConnect ob = DbConnect.getInstance();
		ResultSet rs = null;
		String master= "";
		Connection con = ob.connectDB();
		PreparedStatement pst = null;
		VehicleDto dto = null;
		try {
			String query = "select vendorMaster from vendor where empLinkId="+vendorId;
			pst = con.prepareStatement(query);
			rs = pst.executeQuery();
			if (rs.next()) {
				master =rs.getString("vendorMaster");
			}

			return master;
		} catch (Exception e) {
			System.out.println("Error in VendorstatusDao" + e);
		}

		finally {
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);
		}

		return master;

	}

}
