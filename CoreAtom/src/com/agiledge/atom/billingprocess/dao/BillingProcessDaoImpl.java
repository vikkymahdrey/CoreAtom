package com.agiledge.atom.billingprocess.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Map;
import java.util.Set;

import com.agiledge.atom.dbConnect.DbConnect;
import com.agiledge.atom.dto.TripDetailsDto;

public class BillingProcessDaoImpl implements BillingProcessDao {

	@Override
	public int save(TripDetailsDto tripDto , Map<String, String> params, String table) {
		//  
		  
		int returnInt = -1;
		DbConnect ob = DbConnect.getInstance();
		PreparedStatement pst = null;
		PreparedStatement cpst = null;
		
		ResultSet rs = null;
		 
		Connection con = ob.connectDB();
		
		String checkQuery = " select * from "+table+" where tripId=" + tripDto.getId();
		String updateQuery = "update  "+table+" set ";
		String insertQuery = "insert into "+table+" ( ";
		  
		try {
			con.setAutoCommit(true);	
				String insertLabelQuery =" tripId";
				String insertValueQuery = tripDto.getId();
				String updateLabelVaueQuery= " tripId=" + tripDto.getId();
				Set<String> keys = params.keySet();
				for(String key : keys) {
					insertLabelQuery  +=  ", " +key;
					insertValueQuery +=  ", " +params.get(key);
					updateLabelVaueQuery += ", " + key + "=" + params.get(key);
				}
			/*for(int idx = 0; idx < label.length; idx++ ) {
				insertLabelQuery  +=  ", " +label[idx];
				insertValueQuery +=  ", " +value[idx];
				updateLabelVaueQuery = ", " + label[idx] + "=" + value[idx]; 
				
			}*/
		     
			updateQuery +=updateLabelVaueQuery + " where tripId=" + tripDto.getId() + "";
			insertQuery += insertLabelQuery + " ) values (" + insertValueQuery + " )";
				
		
		   
			cpst = con.prepareStatement(checkQuery);
			rs =cpst.executeQuery();
			System.out.println("UPDATE QUERY : " + updateQuery);
			System.out.println("INSERT QUERY : " + insertQuery);
			if(rs.next()==false) {

				
				pst = con.prepareStatement(insertQuery);
			} else {
				
				pst = con.prepareStatement(updateQuery);
			}
			   returnInt= pst.executeUpdate();
			   System.out.println(" UPATED ..");
			 			 
		} catch (Exception e) {
		
			System.out.println("Error in DAO-> Addd TripBill Params : " + e);
		} finally {			
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);
		}
		
		return returnInt;
	}
	

}
