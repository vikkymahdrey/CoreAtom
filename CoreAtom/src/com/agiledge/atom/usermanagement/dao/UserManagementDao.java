package com.agiledge.atom.usermanagement.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import javax.servlet.http.HttpSession;

import com.agiledge.atom.constants.AuditLogConstants;
import com.agiledge.atom.dao.AuditLogDAO;
import com.agiledge.atom.dbConnect.DbConnect;
import com.agiledge.atom.usermanagement.dto.UserManagementDto;

public class UserManagementDao {

	public static String SECONDARY_TYPE="SECONDARY";
	public static String PRIMARY_TYPE="PRIMARY";
	public ArrayList<UserManagementDto> getSystemUsers(String roleid) {
		Statement st1 = null;
		ResultSet rs = null;
		ArrayList<UserManagementDto> userTypes = null;
		Connection con = null;
		try {
			rs = null;
			st1 = null;
		DbConnect ob = DbConnect.getInstance();
		con=ob.connectDB();
		
		String  query="select id, name, description, usertype, type from roles where id not in(2,5)";	
		
		if(roleid.equalsIgnoreCase("2")||roleid.equalsIgnoreCase("5"))	
		{ 
			 query="select id, name, description, usertype, type from roles";
		}
		
		
		
		
		
		
		userTypes = new ArrayList<UserManagementDto>();
			st1 = con.createStatement();
			
			
			rs = st1.executeQuery(query);
		
			while (rs.next()) {
				
				UserManagementDto dto = new UserManagementDto();
				dto.setId(rs.getInt("id"));
				dto.setName(rs.getString("name"));
				dto.setDescription(rs.getString("description"));
				dto.setType(rs.getString("type"));
				dto.setUserType(rs.getString("usertype"));
				
				
		  System.out.println(" user type : " + dto.getUserType());
				userTypes.add(dto);
			}
		} catch (Exception e) {
			System.out.println("errro" + e);
		}
		finally{
			
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st1);
			DbConnect.closeConnection(con);
			
		}
		System.out.println(" list length : " + userTypes.size());
		return userTypes;
	}
	public ArrayList<UserManagementDto> getSystemUsers() {
		Statement st1 = null;
		ResultSet rs = null;
		ArrayList<UserManagementDto> userTypes = null;
		Connection con = null;
		try {
			rs = null;
			st1 = null;
		DbConnect ob = DbConnect.getInstance();
		con=ob.connectDB();
		
		
			userTypes = new ArrayList<UserManagementDto>();
			st1 = con.createStatement();
			rs = st1.executeQuery("select id, name, description, usertype, type from roles");
		
			while (rs.next()) {
				
				UserManagementDto dto = new UserManagementDto();
				dto.setId(rs.getInt("id"));
				dto.setName(rs.getString("name"));
				dto.setDescription(rs.getString("description"));
				dto.setType(rs.getString("type"));
				dto.setUserType(rs.getString("usertype"));
				
				
		  System.out.println(" user type : " + dto.getUserType());
				userTypes.add(dto);
			}
		} catch (Exception e) {
			System.out.println("errro" + e);
		}
		finally{
			
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st1);
			DbConnect.closeConnection(con);
			
		}
		System.out.println(" list length : " + userTypes.size());
		return userTypes;
	}

	public int addUserRole(UserManagementDto dto) {
		 
		PreparedStatement st1 = null;
		ResultSet autogenrs = null;
		int val=-1;
		Connection con = null;
		try {
			 
			 
		DbConnect ob = DbConnect.getInstance();
		con=ob.connectDB();
		
			 
			 String query = "insert into roles (name, description, type, usertype) values (?,?,?,?)";
			 
			st1 = con.prepareStatement(query,Statement.RETURN_GENERATED_KEYS);
			st1.setString(1, dto.getName());
			st1.setString(2, dto.getDescription());
			st1.setString(3, dto.getType());
			st1.setString(4, dto.getUserType());
			 val = st1.executeUpdate();
			if(val>0) {
				autogenrs = st1.getGeneratedKeys();
				int autoincNumber = autogenrs.getInt(1);
				 new AuditLogDAO(). 
				auditLogEntry(autoincNumber,
						 AuditLogConstants.USER_ROLE_MANAGEMENT, Integer.parseInt(dto.getUpdatedBy()),
						 AuditLogConstants.WORKFLOW_STATE_EMPTY,
						 AuditLogConstants.WORKFLOW_USER_ROLE,
						 AuditLogConstants.AUDIT_LOG_CREATE_ACTION);
			}
			} catch (Exception e) {
			System.out.println("errro" + e);
		}
		finally{
			
			DbConnect.closeResultSet(autogenrs);
			DbConnect.closeStatement(st1);
			DbConnect.closeConnection(con);
			
		}
		return val;
	}
	
	public int updateUserRole(UserManagementDto dto) {
		 
		PreparedStatement st1 = null;
		 
		 int val = 0;
		Connection con = null;
		try {
			 
			 
		DbConnect ob = DbConnect.getInstance();
		con=ob.connectDB();
		
			 
			 String query = "update roles set name=?, description=? where id=? and usertype=?";
			 System.out.println(" query :  update roles set name='"+dto.getName()+"', description='"+dto.getDescription()+"' where id='"+dto.getId()+"' and usertype='"+dto.getUserType()+"' and type!='"+UserManagementDao.PRIMARY_TYPE+"'");
			st1 = con.prepareStatement(query );
			st1.setString(1, dto.getName());
			st1.setString(2, dto.getDescription());						 
			st1.setInt(3 , dto.getId());
			st1.setString(4, dto.getUserType());
			//st1.setString(5, UserManagementDao.PRIMARY_TYPE);
			  val = st1.executeUpdate();
			if(val>0) {
			 
				int autoincNumber = dto.getId();
				 new AuditLogDAO(). 
				auditLogEntry(autoincNumber,
						AuditLogConstants.USER_ROLE_MANAGEMENT, Integer.parseInt(dto.getUpdatedBy()),
						AuditLogConstants.AUDIT_LOG_CREATE_ACTION,
						AuditLogConstants.WORKFLOW_USER_ROLE,
						AuditLogConstants.AUDIT_LOG_EDIT_ACTION);
			}
			} catch (Exception e) {
			System.out.println("errro" + e);
		}
		finally{
			
		 
			DbConnect.closeStatement(st1);
			DbConnect.closeConnection(con);
			
		}
		return val;
	}
	
	public boolean checkUserTypeExists(UserManagementDto dto) {
		
		Statement st1 = null;
		ResultSet rs = null;
		boolean flag=false;
		Connection con = null;
		try {
			rs = null;
			st1 = null;
		DbConnect ob = DbConnect.getInstance();
		con=ob.connectDB();
		
			 
			st1 = con.createStatement();
			rs = st1.executeQuery("select id, name, description, usertype, type from roles where   usertype='"+dto.getUserType()+"'");
			 
			if(rs.next()) {
			  		
				flag = true;
		 
			} 
		} catch (Exception e) {
			System.out.println("errro" + e);
		}
		finally{
			
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st1);
			DbConnect.closeConnection(con);
			
		}
		return flag;
		
	}

	public boolean checkUserNameExists(UserManagementDto dto) {
		Statement st1 = null;
		ResultSet rs = null;
		boolean flag=false;
		Connection con = null;
		try {
			rs = null;
			st1 = null;
		DbConnect ob = DbConnect.getInstance();
		con=ob.connectDB();
		
			 
			st1 = con.createStatement();
			rs = st1.executeQuery("select id, name, description, usertype, type from roles where name='"+dto.getName()+"'  ");
			 
			if(rs.next()) {
			  		
				flag = true;
		 
			} 
		} catch (Exception e) {
			System.out.println("errro" + e);
		}
		finally{
			
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st1);
			DbConnect.closeConnection(con);
			
		}
		return flag;
		
	
	}
}
