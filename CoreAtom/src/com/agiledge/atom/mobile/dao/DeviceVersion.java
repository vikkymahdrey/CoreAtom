package com.agiledge.atom.mobile.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.agiledge.atom.dbConnect.DbConnect;


public class DeviceVersion {

	public void Version( String imei, String version) {
		
			DbConnect ob = null;
			Connection con = null;
			ob = DbConnect.getInstance();
			con = ob.connectDB();
			PreparedStatement pst=null ,pst1=null,pst2= null;
			ResultSet rs=null;
			String ver=null;
			try {
			pst = con.prepareStatement("select * from deviceinfo where imei = ? ");
			pst.setString(1, imei);
			rs = pst.executeQuery();
			if(rs.next())
			 {
				ver=rs.getString("version");
				
				if(!(ver.equals(version)))
				{
					pst1 = con.prepareStatement("update  deviceinfo set version = ? where imei = ? ");
					pst1.setString(1, version);
					pst1.setString(2, imei);
					pst1.executeUpdate();
					System.out.println("UPDATED THE DEVICE");
				}
				 
			 }
			else{
				
				pst2 = con.prepareStatement("insert into deviceinfo values(?,?)");
				
				pst2.setString(1, imei);
				pst2.setString(2, version);
				pst2.executeUpdate();
			    System.out.println("INSERTED NEW DEVICE");
			
			}
		}
		catch (Exception e) {
		System.out.println("Device Version" + e);
		}
		finally{
			

			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(pst,pst1,pst2);
			DbConnect.closeConnection(con);
		}
		
 
	}

}
