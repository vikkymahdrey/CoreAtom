package com.agiledge.atom.accessRight.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.agiledge.atom.accessRight.dto.AccessRightDto;
import com.agiledge.atom.dbConnect.DbConnect;

public class AccessRightDao {
	
	public boolean isAccessForUserType(AccessRightDto inDto) {
		Connection con=null;
		boolean flag = false;		
		PreparedStatement pst = null;
		ResultSet rs = null;
		System.out.println(" ........... ");
		try {
			DbConnect db = DbConnect.getInstance();
			con = db.connectDB();			
			pst= con.prepareStatement("select * from  access_right_role rhr  where rhr.access_right=?  and rhr.role=?");
			pst.setString(1, inDto.getAccessRightName());
			pst.setString(2, inDto.getUserType());
			rs = pst.executeQuery();
			if(rs.next()) {
				flag = true;
			} else {
				flag = false;
			}
			
			
		} catch (Exception e) {
			System.out.println(" Errror in isAccessFrorUsereType in AccessRigthDao : " + e);									
		}
		finally
		{
		DbConnect.closeResultSet(rs);
		DbConnect.closeStatement(pst);
		DbConnect.closeConnection(con);
		}
		return flag;
	}
	
	
	public String getAccessRight(String role , String module) {
		Connection con=null;
		String accessRight = null;
		boolean flag = false;		
		PreparedStatement pst = null;
		ResultSet rs = null;
		System.out.println(" ........... ");
		try {
			DbConnect db = DbConnect.getInstance();
			con = db.connectDB();			
			pst= con.prepareStatement("select * from  access_right_role where role=? and module=?");
			pst.setString(1, role);
			pst.setString(2, module);
			rs = pst.executeQuery();
			if(rs.next()) {
				accessRight = rs.getString("access_right");
				flag = true;
			} else {
				flag = false;
			}
			
			
		} catch (Exception e) {
			System.out.println(" Errror in getAccessRight in AccessRigthDao : " + e);									
		}
		finally
		{
		DbConnect.closeResultSet(rs);
		DbConnect.closeStatement(pst);
		DbConnect.closeConnection(con);
		}
		return accessRight;
	}


}
