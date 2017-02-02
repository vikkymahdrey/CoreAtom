/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.agiledge.atom.task.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.agiledge.atom.commons.PropertyClass;
import com.agiledge.atom.dto.EmployeeDto;
import com.agiledge.atom.tasks.dbconnect.ConnectDB;



/**
 * 
 * @author Administrator
 */
public class GetEmpsDB extends GetEmps {
	Statement st = null;
	ResultSet rs = null;
	ArrayList<EmployeeDto> emplist = null;
	EmployeeDto empobj = null;
	final String COUNTRYCODE = "IN";
	final String ACTIVE = "1";
	public static String DBusername;
	public static String DBpassword;
	public static String location;
	public static String driver;

	public ArrayList<EmployeeDto> getEmps() {
		// DBusername = "Octopus";
		// DBpassword = "octopus";
		location="jdbc:mysql://localhost:3306/atomcoreSiemens";
		DBusername="root";
		DBpassword="root";
		
		//url = "jdbc:sqlserver://localhost:1433; Instance=SQLEXPRESS;"
			//	+ "databaseName=atomcore;";
		//userName = "atomdba";
		//password = "atomdba";
		driver = "com.mysql.jdbc.Driver";
		String address = "";

		PropertyClass prop = new PropertyClass();
		if (prop.readDBProperty("centraldb.property")) {
			driver = prop.getDriver();
			DBusername = prop.getUserName();
			DBpassword = prop.getPassword();
			location = prop.getUrl();
		}

		try {
			emplist = null;
			rs = null;
			st = null;
			emplist = new ArrayList<EmployeeDto>();

			if (ConnectDB.con1 == null) {
				new ConnectDB(DBusername, DBpassword, location, driver);
			}
			st = ConnectDB.con1.createStatement();
			// System.out.println("MY GET IS WORKING" + new Date().toString());
			rs = st.executeQuery("select ev.*, ifnull(av.AddressLine1,'') + ' ' +  ifnull(av.AddressLine2,'') + ' ' +  ifnull(av.AddressLine3,'') + ' ' +  ifnull(av.City,'') + ' ' +  ifnull(av.postalCode,'') address,av.City ,ed.employeeclass as emptype,case when ed.displayname ='' then (ev.employeelastname+' '+ev.employeefirstname) COLLATE DATABASE_DEFAULT  else ed.displayname end as  displayName from vw_Octopus_EmployeeDetails ev join vw_ibsemployeedata ed on ed.Loginid COLLATE DATABASE_DEFAULT=ev.LoginId COLLATE DATABASE_DEFAULT left outer join  vw_Octopus_EmployeeAddress av on ev.PersonnelNo=av.EmployeeNumber where ev.HomeCountry='"
					+ COUNTRYCODE + "' and ev.Active='" + ACTIVE + "'");
			while (rs.next()) {
				empobj = new EmployeeDto();
				empobj.setEmployeeFirstName(rs.getString("EmployeeFirstName"));
				empobj.setEmployeeMiddleName(rs.getString("EmployeeMiddleName"));
				empobj.setEmployeeLastName(rs.getString("EmployeeLastName"));
				empobj.setLoginId(rs.getString("LoginId"));
				empobj.setGender(rs.getString("Gender"));
				empobj.setPersonnelNo(rs.getString("PersonnelNo"));
				empobj.setEmailAddress(rs.getString("EmailAddress"));
				empobj.setHomeCountry(rs.getString("HomeCountry"));
				empobj.setLineManager(rs.getString("LineManager"));
				empobj.setStaffManager(rs.getString("StaffManager"));
				empobj.setClientServiceManager(rs
						.getString("ClientServiceManager"));
				empobj.setCareerLevel(rs.getString("CareerLevel"));
				empobj.setCareerPathwayDesc(rs.getString("CareerPathwayDesc"));
				empobj.setBusinessUnitCode(rs.getString("BusinessUnitCode"));
				empobj.setBusinessUnitDescription(""
						+ rs.getString("BusinessUnitDescription"));
				empobj.setDeptno(rs.getString("Deptno"));
				empobj.setDeptName(rs.getString("DeptName"));
				empobj.setOperationCode(rs.getString("OperationCode"));
				empobj.setOperationDescription(rs
						.getString("OperationDescription"));
				empobj.setPathways(rs.getString("Pathways"));
				empobj.setDateOfJoining(rs.getString("Dateofjoining"));
				empobj.setActive(rs.getString("Active"));
				address = rs.getString("address");
				empobj.setAddress(address);
				empobj.setCity(rs.getString("City"));
				empobj.setEmpType(rs.getString("emptype"));
				empobj.setDisplayName(rs.getString("displayName"));
				// System.out.println("Emp Name:"+empobj.getEmployeeFirstName());
				emplist.add(empobj);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("ERRRoin getemps" + e);
		}
		finally
		{
			try {
				ConnectDB.con1.close();				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
		}
		return emplist;
	}
}
