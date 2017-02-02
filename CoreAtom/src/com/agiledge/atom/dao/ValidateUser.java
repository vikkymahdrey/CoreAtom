/*
 * To change this template, choose Tools || Templates
 * and open the template in the editor.
 */
package com.agiledge.atom.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.ws.rs.core.MediaType;

import org.json.JSONObject;

import com.agiledge.atom.constants.SettingsConstant;
import com.agiledge.atom.dbConnect.DbConnect;
import com.agiledge.atom.dto.EmployeeDto;
import com.agiledge.atom.task.dao.GetEmps;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;

/**
 * 
 * @author Shahid
 */
public class ValidateUser {

	String username = "";
	String password = "";
	public String uid = "";
	public String role = "";
	public String roleId = "";
	public String delegatedUId = "";
	public String delegatedUrole = "";
	public String delegatedUroleId = "";
	public String site = "";
	public String vendorId = "";
	DbConnect ob = DbConnect.getInstance();
	// JSON for API authentication LDAP for LDAP authentication LoginId validate
	// loginId External for external Employee authentication
	


	static String validationType = SettingsConstant.validationFlag;

//"UAT";
	public void SetUserNamePassword(String username, String password) {
		this.username = username;
		this.password = password;
	}

	public boolean checkUser() {
		/*
		 * Check LDAP
		 */

		PreparedStatement st = null;
		ResultSet rs = null;
		Connection con = ob.connectDB();
		System.out.println("Inside check User connection established");
		Statement st1 = null;
		ResultSet rs1 = null;
		try {
			/*
			 * if (!authenticateUser()) { st = con.prepareStatement(
			 * "select * from etms_user where username=? and password=?");
			 * st.setString(1, username); st.setString(2, password); rs =
			 * st.executeQuery(); if (rs.next()) { uid =
			 * rs.getString("employee_id"); role = rs.getString("role");
			 * System.out.println("uid "+ uid);
			 * 
			 * return true; } return false; }
			 */
			System.out.println(authenticateUser());
			if (checkIsLocalAuthentication()==1||authenticateUser()) {
				System.out.println("authentication success");
				String query = "";
				query = "select e.id,NULLIF(e.site,'') site,e.usertype,e.role_id,rd.employeeId delegatedEmployeeId,rd.delegatedType,rd.delegatedRoleId from employee e left outer join ( select rd.employeeId, rd.delegatedEmployeeId as delegatedEmployeeId,e1.usertype as delegatedType,e1.role_id as delegatedRoleId from   roleDelegation rd left outer join employee e1 on e1.id=rd.employeeId  where rd.from_date<=curdate() and rd.to_date>=curdate() and rd.status='a'  ) as rd on rd.delegatedEmployeeId=e.id where e.loginId=?";
				String queryTest = query.replace("?", "'%s'");
				queryTest = String.format(queryTest,  uid);
				if (uid.equalsIgnoreCase("")) {
				//	query = "select e.id,NULLIF(e.site,'') site,e.usertype,e.role_id,rd.delegatedEmployeeId,rd.delegatedType,rd.delegatedRoleId from employee e left outer join (select rd.employeeId, rd.delegatedEmployeeId as delegatedEmployeeId,e1.usertype as delegatedType,e1.role_id as delegatedRoleId from   roleDelegation rd left outer join employee e1 on e1.id=rd.delegatedEmployeeId where rd.from_date>=curdate() and rd.to_date>=curdate() and rd.status='a' ) as rd on rd.employeeId=e.id where e.loginId=?";
				//	query = "select e.id,NULLIF(e.site,'') site,e.usertype,e.role_id,rd.employeeId delegatedEmployeeId,rd.delegatedType,rd.delegatedRoleId from employee e left outer join ( select rd.employeeId, rd.delegatedEmployeeId as delegatedEmployeeId,e1.usertype as delegatedType,e1.role_id as delegatedRoleId from   roleDelegation rd left outer join employee e1 on e1.id=rd.employeeId  where rd.from_date>=curdate() and rd.to_date>=curdate() and rd.status='a'  ) as rd on rd.delegatedEmployeeId=e.id where e.loginId=?";
					System.out.println("uid is blank then :"+queryTest);
					st = con.prepareStatement(query);
					st.setString(1, username);
				} else {
					//query = "select e.id,NULLIF(e.site,'') site,e.usertype,e.role_id,rd.delegatedEmployeeId,rd.delegatedType,rd.delegatedRoleId from employee e left outer join (select rd.employeeId, rd.delegatedEmployeeId as delegatedEmployeeId,e1.usertype as delegatedType,e1.role_id as delegatedRoleId from   roleDelegation rd left outer join employee e1 on e1.id=rd.delegatedEmployeeId where rd.from_date>=curdate() and rd.to_date>=curdate() and rd.status='a' ) as rd on rd.employeeId=e.id where e.personnelNo=?";
				//	query = "select e.id,NULLIF(e.site,'') site,e.usertype,e.role_id,rd.employeeId delegatedEmployeeId,rd.delegatedType,rd.delegatedRoleId from employee e left outer join ( select rd.employeeId, rd.delegatedEmployeeId as delegatedEmployeeId,e1.usertype as delegatedType,e1.role_id as delegatedRoleId from   roleDelegation rd left outer join employee e1 on e1.id=rd.employeeId  where rd.from_date>=curdate() and rd.to_date>=curdate() and rd.status='a'  ) as rd on rd.delegatedEmployeeId=e.id where e.loginId=?";
					System.out.println("uid is not blank then :" +queryTest);
					st = con.prepareStatement(query);
					st.setString(1, uid);
				}

				st1 = con.createStatement();
				rs = st.executeQuery();
				System.out.println("After RS...");
				if (rs.next()) {
					System.out.println(" .................");
					uid = rs.getString("id");
					role = rs.getString("usertype");
					roleId = rs.getString("role_id");
					delegatedUId = rs.getString("delegatedEmployeeId");
					delegatedUrole = rs.getString("delegatedType");
					delegatedUroleId = rs.getString("delegatedRoleId");
					site = rs.getString("site");
					if (role.equalsIgnoreCase("v")) {
						rs1 = st1
								.executeQuery("select vendorMaster from vendor where empLinkId="
										+ uid + "");
						if (rs1.next()) {
							vendorId = rs1.getString("vendorMaster");
						}
						else
						{
							rs1 = st1
									.executeQuery("select id from vendorMaster order by id ");
							if (rs1.next()) {
								vendorId = rs1.getString("id");
							}	
						}
					}
					return true;	
				}
				return false;
			}
		} catch (Exception e) {
			System.out.println("error : " + e);
		} finally {

			DbConnect.closeResultSet(rs, rs1);
			DbConnect.closeStatement(st, st1);
			DbConnect.closeConnection(con);
		}
		return false;
	}

	
	
	
	
	public boolean authenticateUserWIthSSO(String userId) {
		/*
		 * Check LDAP
		 */

		PreparedStatement st = null;
		ResultSet rs = null;
		Connection con = ob.connectDB();
		Statement st1 = null;
		ResultSet rs1 = null;
		try {	
				String query = "";
				query = "select e.id,NULLIF(e.site,'') site,e.usertype,e.role_id,rd.employeeId delegatedEmployeeId,rd.delegatedType,rd.delegatedRoleId from employee e left outer join ( select rd.employeeId, rd.delegatedEmployeeId as delegatedEmployeeId,e1.usertype as delegatedType,e1.role_id as delegatedRoleId from   roleDelegation rd left outer join employee e1 on e1.id=rd.employeeId  where rd.from_date<=curdate() and rd.to_date>=curdate() and rd.status='a'  ) as rd on rd.delegatedEmployeeId=e.id where e.loginId=?";
				String queryTest = query.replace("?", "'%s'");
				queryTest = String.format(queryTest,  uid);				
				st = con.prepareStatement(query);
				st.setString(1, userId);
				rs = st.executeQuery();
				if (rs.next()) {
					uid = rs.getString("id");
					role = rs.getString("usertype");
					roleId = rs.getString("role_id");
					delegatedUId = rs.getString("delegatedEmployeeId");
					delegatedUrole = rs.getString("delegatedType");
					delegatedUroleId = rs.getString("delegatedRoleId");
					site = rs.getString("site");
					if (role.equalsIgnoreCase("v")) {
						rs1 = st1
								.executeQuery("select vendorMaster from vendor where empLinkId="
										+ uid + "");
						if (rs1.next()) {
							vendorId = rs1.getString("vendorMaster");
						}
						else
						{
							rs1 = st1
									.executeQuery("select id from vendorMaster order by id ");
							if (rs1.next()) {
								vendorId = rs1.getString("id");
							}	
						}
					}
					return true;	
				}
				return false;			
		} catch (Exception e) {
			System.out.println("error : " + e);
		} finally {

			DbConnect.closeResultSet(rs, rs1);
			DbConnect.closeStatement(st, st1);
			DbConnect.closeConnection(con);
		}
		return false;
	}
	
	
	
	
	
	private boolean authenticateUser() {
		System.out.println("validationType"+validationType);
		/*
		 * EmployeeDao empDAO = new EmployeeDao(); EmployeeDto emp =
		 * empDAO.getEmployeeByLogin(username); if
		 * (emp.getExternaluser().equalsIgnoreCase("yes")) { <<<<<<<
		 * ValidateUser.java if (Integer.parseInt(emp.getActive()) == 1 &&
		 * emp.getLoginId().equals(username) &&
		 * emp.getPassword().equals(password)) return true; else return false;
		 * ======= System.out.println("Externale user"); if
		 * (emp.getAuthtype().equalsIgnoreCase("l") &&
		 * Integer.parseInt(emp.getActive()) == 1) {
		 * System.out.println("External local user"); if
		 * (emp.getLoginId().equals(username) &&
		 * emp.getPassword().equals(password)) return true; else return false;
		 * 
		 * } else if (emp.getAuthtype().equalsIgnoreCase("w")) {
		 * System.out.println("External Windows AD User"); // return
		 * LDAPAuthentication(); return true; } >>>>>>> 1.13
		 * 
		 * } else {
		 */
		//if(GetEmps.sourceFlag.equalsIgnoreCase("JSON")) {
		//	validationType = "API";
		//}
		if (validationType.equalsIgnoreCase("UAT"))
			return true;		
		if (validationType.equalsIgnoreCase("LDAP"))		
			return LDAPAuthentication();	
		else if (validationType.equalsIgnoreCase("API"))
			return APIAuthentication();
		return false;
	}

	public boolean APIAuthentication() {
		try {
			// permutation("1234567890");

			ClientConfig config = new DefaultClientConfig();
			Client client = Client.create(config);
			WebResource webResource = client
					.resource("https://www.peopleworks.ind.in/pwwebApi/api/Logon");
			JSONObject js = new JSONObject();
			js.put("ClientCodeSlashName", username);
			js.put("Password", password);
			ClientResponse response = webResource.type(
					MediaType.APPLICATION_JSON).put(ClientResponse.class,
					js.toString());
			System.out.println(response.getStatus());
			if (response.getStatus() == 200) {
				JSONObject jso = new JSONObject(
						response.getEntity(String.class));
				uid = (String) jso.get("EmployeeID");
				return true;
			}
		} catch (Exception e) {
			System.out.println("Error" + e);
		}
		return false;
	}

	private boolean LDAPAuthentication() {

		try {
			Hashtable env = new Hashtable();
			env.put(Context.INITIAL_CONTEXT_FACTORY,
					"com.sun.jndi.ldap.LdapCtxFactory");
			env.put(Context.PROVIDER_URL, "ldap://groupinfra.com:389");
			env.put(Context.SECURITY_AUTHENTICATION, "simple");
			env.put(Context.SECURITY_PRINCIPAL, "groupinfra\\" + username);
			env.put(Context.SECURITY_CREDENTIALS, password);
			DirContext ctx = new InitialDirContext(env);
			return true;
		} catch (Exception e) {
			return false;
		}

	}

	public int checkIsLocalAuthentication() {
		EmployeeDao empDAO = new EmployeeDao();
		EmployeeDto emp = empDAO.getEmployeeByLogin(username);
		try
		{
		if (emp!=null && (emp.getAuthtype() .equalsIgnoreCase("l") || emp.getExternaluser().equalsIgnoreCase("yes")) ) {
			System.out.println(".. use name : " + username  + " password : " + password   );	
			if (emp.getLoginId().equals(username)
					&& emp.getPassword().equals(password)) {
				if (Integer.parseInt(emp.getActive()) == 1) {
					System.out.println("External success");
					return 1;
				} else {
					System.out.println("External failure " + emp);
					return -1;
				}
			} else {
				System.out.println("External failure 0 " + emp);
			}
		}
	}catch(Exception e)
	{
	System.out.println("error in extrenal employee check"+e);	
	}
		return 0;

	}
}
