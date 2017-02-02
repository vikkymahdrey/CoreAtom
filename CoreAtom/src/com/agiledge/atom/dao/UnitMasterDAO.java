package com.agiledge.atom.dao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import com.agiledge.atom.dbConnect.DbConnect;
import com.agiledge.atom.dto.UnitMasterDTO;


public class UnitMasterDAO {
	
	public ArrayList<UnitMasterDTO> getallUnits()
	{
		DbConnect ob = DbConnect.getInstance();
		Connection con = null;
		Statement st=null;
		ResultSet rs=null;
		ArrayList<UnitMasterDTO> list = new ArrayList<UnitMasterDTO>();
		String query="SELECT * FROM atsconnect where projecttype='unit'";
		try {
			con = ob.connectDB();
			st=con.createStatement();
			rs=st.executeQuery(query);
			while(rs.next())
			{
				UnitMasterDTO dto=new UnitMasterDTO();
				dto.setUnitid(rs.getInt("id"));
				dto.setUnitcode(rs.getString("project"));
				dto.setUnitname(rs.getString("description"));
				dto.setStatus(rs.getString("status"));
				dto.setLocation_id(rs.getString("location_id"));
				list.add(dto);
			}
			
			
		}catch (Exception e) {
			System.out.println("Error"+e);					
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);
        }
        return list;
	}
	
	
	public int updateUnitData(UnitMasterDTO dto)
	{
		DbConnect ob = DbConnect.getInstance();
		Connection con = null;
		PreparedStatement st=null;
		int returnInt=0;
		try {
			con = ob.connectDB();
			st=con.prepareStatement("UPDATE atsconnect SET project=?,description=?,LOCATION_ID=? WHERE ID=?");
		    st.setString(1, dto.getUnitcode());
		    st.setString(2, dto.getUnitname());
		    st.setString(3, dto.getLocation_id());
		    st.setInt(4, dto.getUnitid());
		    returnInt=st.executeUpdate();
		}catch (Exception e) {
			System.out.println("Error"+e);					
		} finally {
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);
        } 
		    
		return returnInt;
		
	}
	
	public int updateStatus(int unitid,String status)
	{
		DbConnect ob = DbConnect.getInstance();
		Connection con = null;
		PreparedStatement st=null;
		int returnInt=0;
		try {
			con = ob.connectDB();
			st=con.prepareStatement("UPDATE atsconnect SET STATUS=? WHERE ID=?");
			st.setString(1, status);
			st.setInt(2, unitid);
			returnInt=st.executeUpdate();
		}catch (Exception e) {
			System.out.println("Error"+e);					
		} finally {
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);
        } 
		    
		return returnInt;
		
	}

	
	public int insertUnitData(UnitMasterDTO dto)
	{
		DbConnect ob = DbConnect.getInstance();
		Connection con = null;
		PreparedStatement st=null;
		int returnInt=0;
		try {
			con = ob.connectDB();
			st=con.prepareStatement("INSERT INTO atsconnect(project,description,STATUS,LOCATION_ID,projecttype) VALUES(?,?,?,?,?)");
		    st.setString(1, dto.getUnitcode());
		    st.setString(2, dto.getUnitname());
		    st.setString(3, dto.getStatus());
		    st.setString(4, dto.getLocation_id());
		    st.setString(5, "unit");
		    returnInt=st.executeUpdate();
		}catch (Exception e) {
			System.out.println("Error"+e);					
		} finally {
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);
        } 
		    
		return returnInt;
		
	}
	
	
	public int insertunitfromexcel(ArrayList<UnitMasterDTO> list)
	{
		DbConnect ob = DbConnect.getInstance();
		Connection con = null;
		PreparedStatement st = null;
		Statement st1=null;
		ResultSet rs=null;
		int returnInt = 0,tempint=0;
		try {
			con = ob.connectDB();
			st1=con.createStatement();
			String query="";
			for(UnitMasterDTO dto:list)
			{
				rs=null;
				if(dto.getLocation_id()==null)
				{
					dto.setLocation_id("");
				}
				rs=st1.executeQuery("SELECT ID FROM atsconnect WHERE project='"+dto.getUnitcode()+"' and projecttype='unit'");
				if(rs.next())
				{
					query="UPDATE atsconnect SET project=?,description=?, STATUS=?,LOCATION_ID=? WHERE project=? and projecttype='unit'";
					st = con.prepareStatement(query);
					st.setString(1, dto.getUnitcode());
					st.setString(2, dto.getUnitname());
					st.setString(3, dto.getStatus());
					st.setString(4, dto.getLocation_id());
					st.setString(5, dto.getUnitcode());
					tempint = st.executeUpdate();
					returnInt=tempint+returnInt;
					
				}
				else
				{
				query ="INSERT INTO atsconnect(project,description,STATUS,LOCATION_ID,projecttype) VALUES(?,?,?,?,?)";
				st = con.prepareStatement(query);
				st.setString(1, dto.getUnitcode());
			    st.setString(2, dto.getUnitname());
			    st.setString(3, dto.getStatus().toUpperCase());
			    st.setString(4, dto.getLocation_id());
			    st.setString(5, "unit");
			    tempint = st.executeUpdate();
			    returnInt=tempint+returnInt;
				}
			}
	}catch (Exception e) {
		System.out.println("Error"+e);					
	} finally {
		DbConnect.closeResultSet(rs);
		DbConnect.closeStatement(st);
		DbConnect.closeConnection(con);
    } 
	    
	return returnInt;
}
	
	
	/*
	public ArrayList<UnitMasterDTO> getUnitbyName(String unitname)
	{
		DbConnect ob = DbConnect.getInstance();
		Connection con = null;
		Statement st = null;
		ResultSet rs=null; 
		ArrayList<UnitMasterDTO> list=new ArrayList<UnitMasterDTO>();
		String query ="SELECT * FROM UNIT_MASTER WHERE UNIT_NAME LIKE '%"+unitname+"%' AND STATUS='A'";
		try {
			con = ob.connectDB();
			st=con.createStatement();
			rs=st.executeQuery(query);
			while(rs.next())
			{
				UnitMasterDTO dto=new UnitMasterDTO();
				dto.setUnitid(rs.getInt("id"));
				dto.setUnitcode(rs.getString("unit_code"));
				dto.setUnitname(rs.getString("unit_name"));
				dto.setStatus(rs.getString("status"));
				dto.setLocation_id(rs.getString("location_id"));
				list.add(dto);
			}
		}catch (Exception e) {
			System.out.println("Error"+e);					
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);
	    } 
		return list;
		
	}
	
	
	
	public ArrayList<UnitMasterDTO> getUnitbyCode(String unitcode)
	{
		DbConnect ob = DbConnect.getInstance();
		Connection con = null;
		Statement st = null;
		ResultSet rs=null; 
		ArrayList<UnitMasterDTO> list=new ArrayList<UnitMasterDTO>();
		String query ="SELECT * FROM UNIT_MASTER WHERE UNIT_CODE LIKE '%"+unitcode+"%' AND STATUS='A'";
		try {
			con = ob.connectDB();
			st=con.createStatement();
			rs=st.executeQuery(query);
			while(rs.next())
			{
				UnitMasterDTO dto=new UnitMasterDTO();
				dto.setUnitid(rs.getInt("id"));
				dto.setUnitcode(rs.getString("unit_code"));
				dto.setUnitname(rs.getString("unit_name"));
				dto.setStatus(rs.getString("status"));
				dto.setLocation_id(rs.getString("location_id"));
				list.add(dto);
			}
		}catch (Exception e) {
			System.out.println("Error"+e);					
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);
	    } 
		return list;
		
	}
	*/
	
	public String getUnitNamebyId(int id)
	{
		DbConnect ob = DbConnect.getInstance();
		Connection con = null;
		Statement st = null;
		ResultSet rs=null;
		String query ="SELECT project FROM atsconnect WHERE ID="+id;
		String returnString="";
		try {
			con = ob.connectDB();
			st=con.createStatement();
			rs=st.executeQuery(query);
			while(rs.next())
			{
				returnString=rs.getString("project");
			}
		}catch (Exception e) {
			System.out.println("Error"+e);					
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);
	    } 
		return returnString;
	}
}
