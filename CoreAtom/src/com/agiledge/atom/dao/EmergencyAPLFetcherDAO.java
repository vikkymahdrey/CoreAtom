package com.agiledge.atom.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import com.agiledge.atom.dbConnect.DbConnect;

public class EmergencyAPLFetcherDAO {
	
	public String[] getAPL(int id)
	{
		System.out.println("m started");

		String APLset[] = new String[5];
		DbConnect ob = DbConnect.getInstance();
		Statement st = null;
		ResultSet rs = null;
		Connection con = ob.connectDB();
		try {
			st = con.createStatement();
		
		String query = "select a.area,p.place,l.landmark,l.id, o.project from area a,place p,landmark l ,employee_subscription e,employee o where a.id=p.area && p.id=l.place && l.id=e.landMark and e.empID = o.id && o.id="+id+"";		
		System.err.println("VVVVVVVVVVVVVVVVVVVVVVVVVVVVV"+query);
		rs =st.executeQuery(query);
		System.out.println("completed");
		if( rs.next() )
		{
			APLset[0] = rs.getString("area");
			APLset[1] = rs.getString("place");
			APLset[2] = rs.getString("landmark");
			APLset[4] = rs.getString("id");
			APLset[3] = rs.getString("project");
		}
		System.out.println("completed");
		
		
		return APLset;

		} catch (SQLException e) {
			System.out.println("Error in EmergencyAPL fetching");
			return APLset;
		}
		 
				
	}
	
}
