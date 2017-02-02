package com.agiledge.atom.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import com.agiledge.atom.commons.OtherFunctions;
import com.agiledge.atom.dbConnect.DbConnect;
import com.agiledge.atom.dto.EmployeeDto;
import com.agiledge.atom.dto.SpocDto;

public class SpocSetup {
	
	public ArrayList<EmployeeDto> getemployeesbymanager(long id)
	{
		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		ArrayList<EmployeeDto> list=new ArrayList<EmployeeDto>();
		try {
			DbConnect  db = DbConnect.getInstance();
			con = db.connectDB();
			String query="SELECT * FROM EMPLOYEE WHERE LINEMANAGER=?";
			st = con.prepareStatement(query);
			st.setLong(1, id);
			rs = st.executeQuery();
			while(rs.next())
			{
				EmployeeDto dto=new EmployeeDto();
				dto.setEmployeeID(rs.getString("id"));
				dto.setEmployeeFirstName(rs.getString("EmployeeFirstName"));
				dto.setEmployeeLastName(rs.getString("employeeLastName"));
				dto.setPersonnelNo(rs.getString("PersonnelNo"));
				dto.setUserType(rs.getString("usertype"));
				dto.setLoginId(rs.getString("loginId"));
				dto.setGender(rs.getString("Gender"));
				dto.setEmailAddress(rs.getString("EmailAddress"));
				list.add(dto);
			}
			
	}catch (Exception e) {
		
	} finally {
		DbConnect.closeResultSet(rs);
		DbConnect.closeStatement(st);
		DbConnect.closeConnection(con);
	}
		return list;
	}

	
	public int insertSpoc(String[] id,String man_id,String from_date,String to_date)
	{
		Connection con = null;
		PreparedStatement st = null;
		int returnInt=0;
		String query="";
		try {
			DbConnect  db = DbConnect.getInstance();
			con = db.connectDB();
			if(from_date==null||from_date.equals("")||to_date==null||to_date.equals(""))
			{
			query="INSERT INTO SPOC_PARENT(MANAGER_ID,EMPLOYEE_ID,STATUS) VALUES(?,?,?)";
			st = con.prepareStatement(query);
			for (String emp : id) {
				st.setLong(1, Long.parseLong(man_id));
				st.setLong(2, Long.parseLong(emp));
				st.setString(3, "a");
				returnInt += st.executeUpdate();
			}
			}
			else
			{
				query="INSERT INTO SPOC_PARENT(MANAGER_ID,EMPLOYEE_ID,FROM_DATE,TO_DATE,STATUS) VALUES(?,?,?,?,?)";
				st = con.prepareStatement(query);
				for (String emp : id) {
					st.setLong(1, Long.parseLong(man_id));
					st.setLong(2, Long.parseLong(emp));
					st.setString(3, OtherFunctions.changeDateFromatToIso(from_date));
					st.setString(4, OtherFunctions.changeDateFromatToIso(to_date));
					st.setString(5, "a");
					returnInt += st.executeUpdate();
				}
			}
			query="update employee set usertype='spoc' ,role_id=(select id from roles where usertype='spoc') where id=?";
			st = con.prepareStatement(query);
			for (String emp : id) {
				st.setString(1, emp);
				st.executeUpdate();
			}
			
				
		}catch (Exception e) {
			System.out.println("ERRORR in assign:  "+e);
		} finally {
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);
		}
		return returnInt;
	}
	
	public ArrayList<SpocDto> getspocsbymanagerid(long man_id)
	{
		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		ArrayList<SpocDto> list=new ArrayList<SpocDto>();
		try {
			String query="select spoc_parent.id,employee_id,displayName  as Name,personnelNo as PersonnalNo,date_format( from_date, '%d/%m/%Y') from_date, date_format( to_date , '%d/%m/%Y') to_date ,status from spoc_parent, employee where spoc_parent.employee_id=employee.id and manager_id=? and spoc_parent.status='a' order by from_date ";
			System.out.println("manager id  : "+ man_id);
			DbConnect  db = DbConnect.getInstance();
			con = db.connectDB();
			st = con.prepareStatement(query);
			st.setLong(1, man_id);
			rs = st.executeQuery();
			while (rs.next()) {
				SpocDto dto=new SpocDto();
				dto.setSpoc_id(rs.getString("id"));
				dto.setEmployee_id(rs.getString("employee_id"));
				dto.setSpocName(rs.getString("Name"));
				dto.setPers_no(rs.getString("PersonnalNo"));
				dto.setFrom_date(rs.getString("from_date"));
				dto.setTo_date(rs.getString("to_date"));
				dto.setStatus(rs.getString("status"));
				list.add(dto);
			}	
		}catch (Exception e) {
			System.out.println("Error in getspocby managerid : "+ e);
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);
		}
		return list;
	}
	
	public int updateStatus(String spocid,String status) {
		DbConnect ob = DbConnect.getInstance();
		Connection con = null;
		Statement st = null,st1=null;
		int result=0,cresult=0;
		try {
			if(status.equals("a"))
				status="c";
			else
				status="a";
			String query="UPDATE SPOC_PARENT SET STATUS='"+status+"' WHERE ID="+Long.parseLong(spocid);
			String childQuery="UPDATE SPOC_CHILD SET STATUS='"+status+"' WHERE SPOC_ID="+Long.parseLong(spocid);
			System.out.println(query);
			con=ob.connectDB();
			st=con.createStatement();
			result=st.executeUpdate(query);
			if(result>0)
			{
				st1=con.createStatement();
				cresult=st1.executeUpdate(childQuery);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DbConnect.closeStatement(st,st1);
			DbConnect.closeConnection(con);

		}
		return result;
	}
	
	public ArrayList<EmployeeDto> getemployeesbyspoc(long spoc_id )
	{
		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		ArrayList<EmployeeDto> list=new ArrayList<EmployeeDto>();
		try {
			DbConnect  db = DbConnect.getInstance();
			con = db.connectDB();
			String query="SELECT EMPLOYEE.ID as id,EmployeeFirstName,employeeLastName,PersonnelNo,Gender,EmailAddress,spoc_child.id as spoc_id FROM SPOC_CHILD,EMPLOYEE WHERE EMPLOYEE.ID=SPOC_CHILD.employee_id AND SPOC_ID=? AND SPOC_CHILD.STATUS='a'";
			st = con.prepareStatement(query);
			st.setLong(1, spoc_id);
			rs = st.executeQuery();
			while(rs.next())
			{
				EmployeeDto dto=new EmployeeDto();
				dto.setEmployeeID(rs.getString("id"));
				dto.setEmployeeFirstName(rs.getString("EmployeeFirstName"));
				dto.setEmployeeLastName(rs.getString("employeeLastName"));
				dto.setPersonnelNo(rs.getString("PersonnelNo"));
				dto.setGender(rs.getString("Gender"));
				dto.setEmailAddress(rs.getString("EmailAddress"));
				dto.setSpoc_id(rs.getString("spoc_id"));
				list.add(dto);
			}
			
	}catch (Exception e) {
		
	} finally {
		DbConnect.closeResultSet(rs);
		DbConnect.closeStatement(st);
		DbConnect.closeConnection(con);
	}
		return list;
	}
	
	public int removeEmployee(String id) {
		DbConnect ob = DbConnect.getInstance();
		Connection con = null;
		PreparedStatement st = null;
		int result=0;
		try {
			String query="UPDATE SPOC_CHILD SET STATUS='c' WHERE ID=?";
			System.out.println(query);
			con=ob.connectDB();
			st=con.prepareStatement(query);
			st.setLong(1, Long.parseLong(id));
			result=st.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);

		}
		return result;
	}
	
	public int insertEmployee(String[] employee_id,String spoc_id)
	{
		Connection con = null;
		PreparedStatement st = null,cst=null;
		ResultSet rs=null;
		int returnInt=0;
		String query="insert into spoc_child(spoc_id,employee_id,status) values(?,?,'a')";
		String checkquery="select * from spoc_child where spoc_id=? and employee_id=? and status='a'";
		try {
			DbConnect  db = DbConnect.getInstance();
			con = db.connectDB();
			cst=con.prepareStatement(checkquery);
			st = con.prepareStatement(query);
			for (String emp : employee_id) {
				cst.setLong(1, Long.parseLong(spoc_id));
				cst.setLong(2, Long.parseLong(emp));
				rs=cst.executeQuery();
				if(!rs.next())
				{
				st.setLong(1, Long.parseLong(spoc_id));
				st.setLong(2, Long.parseLong(emp));
				returnInt += st.executeUpdate();
				}
			}	
		}catch (Exception e) {
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(cst);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);
		}
		return returnInt;
	}
	
	public ArrayList<EmployeeDto> getemployeesbymanagerspocid(long id)
	{
		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		ArrayList<EmployeeDto> list=new ArrayList<EmployeeDto>();
		try {
			DbConnect  db = DbConnect.getInstance();
			con = db.connectDB();
			String query="SELECT e.*,r.name as usertypename FROM EMPLOYEE e,roles r WHERE e.role_id=r.id and  e.LINEMANAGER=? AND e.ID NOT IN (SELECT EMPLOYEE_ID FROM SPOC_CHILD WHERE STATUS='a')";
			st = con.prepareStatement(query);
			st.setLong(1, id);
			rs = st.executeQuery();
			while(rs.next())
			{
				EmployeeDto dto=new EmployeeDto();
				dto.setEmployeeID(rs.getString("id"));
				dto.setEmployeeFirstName(rs.getString("EmployeeFirstName"));
				dto.setEmployeeLastName(rs.getString("employeeLastName"));
				dto.setPersonnelNo(rs.getString("PersonnelNo"));
				dto.setUserType(rs.getString("usertype"));
				dto.setLoginId(rs.getString("loginId"));
				dto.setGender(rs.getString("Gender"));
				dto.setEmailAddress(rs.getString("EmailAddress"));
				dto.setUserType(rs.getString("usertypename"));
				list.add(dto);
			}
			
	}catch (Exception e) {
		System.out.println("Errorrrrrrr in get emps under manager "+e);
	} finally {
		DbConnect.closeResultSet(rs);
		DbConnect.closeStatement(st);
		DbConnect.closeConnection(con);
	}
		return list;
	}
	
	public int utilitychangeStatus()
	{
		Connection con = null;
		Statement st = null,st1=null;
		int result=0;
		String query="update spoc_parent set status='c' where to_date<=now()";
		String childquery="update spoc_child set status='c' where spoc_id in (select id from spoc_parent where to_date<=now())";
		try {
			DbConnect  ob = DbConnect.getInstance();
			con=ob.connectDB();
			st1=con.createStatement();
			int cresult=st1.executeUpdate(childquery);
			st=con.createStatement();
			result=st.executeUpdate(query);
		}
			catch (Exception e) {
				e.printStackTrace();
			} finally {
				DbConnect.closeStatement(st);
				DbConnect.closeConnection(con);
			}
	return result;	
}
	
	
	public int checkSpoc(long empid,String from_date,String to_date)
	{
		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		int result=0;
		String query="";
		System.out.println(empid);
		System.out.println(from_date);
		System.out.println(to_date);
		try {
			DbConnect  db = DbConnect.getInstance();
			con = db.connectDB();
			if(from_date==""||from_date==null||to_date==""||to_date==null)
			{
				query="SELECT * FROM SPOC_PARENT WHERE EMPLOYEE_ID=? AND FROM_DATE IS NULL AND STATUS='a'";
				st = con.prepareStatement(query);
				st.setLong(1,empid);
			}
			else
			{
				query="SELECT * FROM SPOC_PARENT WHERE EMPLOYEE_ID=? AND (FROM_DATE IS NULL OR (FROM_DATE BETWEEN ? AND ? OR TO_DATE BETWEEN ? AND ?)) AND STATUS='a'";	
				st=con.prepareStatement(query);
				st.setLong(1, empid);
				st.setString(2, from_date);
				st.setString(3, to_date);
				st.setString(4, from_date);
				st.setString(5, to_date);
			}
			rs=st.executeQuery();
			if(rs.next())
				result=1;
		}catch (Exception e) {
			} finally {
				DbConnect.closeResultSet(rs);
				DbConnect.closeStatement(st);
				DbConnect.closeConnection(con);
			}
			return result;
		}
	
	public ArrayList<EmployeeDto> getemployeesbymanagerforspoc(long id)
	{
		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		ArrayList<EmployeeDto> list=new ArrayList<EmployeeDto>();
		try {
			DbConnect  db = DbConnect.getInstance();
			con = db.connectDB();
			String query="SELECT * FROM EMPLOYEE WHERE LINEMANAGER=? AND ID NOT IN(SELECT EMPLOYEE_ID FROM SPOC_PARENT WHERE FROM_DATE IS NULL AND STATUS='a')";
			st = con.prepareStatement(query);
			st.setLong(1, id);
			rs = st.executeQuery();
			while(rs.next())
			{
				EmployeeDto dto=new EmployeeDto();
				dto.setEmployeeID(rs.getString("id"));
				dto.setEmployeeFirstName(rs.getString("EmployeeFirstName"));
				dto.setEmployeeLastName(rs.getString("employeeLastName"));
				dto.setPersonnelNo(rs.getString("PersonnelNo"));
				dto.setUserType(rs.getString("usertype"));
				dto.setLoginId(rs.getString("loginId"));
				dto.setGender(rs.getString("Gender"));
				dto.setEmailAddress(rs.getString("EmailAddress"));
				list.add(dto);
			}
			
	}catch (Exception e) {
		
	} finally {
		DbConnect.closeResultSet(rs);
		DbConnect.closeStatement(st);
		DbConnect.closeConnection(con);
	}
		return list;
	}
	
	
	public int checkAssignEmployee(long empid)
	{
		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		int result=0;
		String query="SELECT * FROM SPOC_CHILD WHERE EMPLOYEE_ID=? AND STATUS='a'";
		try {
			DbConnect  db = DbConnect.getInstance();
			con = db.connectDB();
			st = con.prepareStatement(query);
			st.setLong(1,empid);
			rs=st.executeQuery();
			if(rs.next())
				result=1;
		}catch (Exception e) {
			} finally {
				DbConnect.closeResultSet(rs);
				DbConnect.closeStatement(st);
				DbConnect.closeConnection(con);
			}
			return result;
	}
	}
