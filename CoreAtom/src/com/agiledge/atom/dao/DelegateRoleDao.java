package com.agiledge.atom.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;

import com.agiledge.atom.commons.OtherFunctions;
import com.agiledge.atom.constants.AuditLogConstants;
import com.agiledge.atom.dbConnect.DbConnect;
import com.agiledge.atom.dto.AuditLogDTO;
import com.agiledge.atom.dto.DelegateRoleDto;




public class DelegateRoleDao {

	public ArrayList<DelegateRoleDto> getDelegatedRoles() throws SQLException {
		ArrayList<DelegateRoleDto> delegateRoleDtos = new ArrayList<DelegateRoleDto>();
		DbConnect ob = DbConnect.getInstance();
		Statement st = null;
		Connection con = ob.connectDB();
		st = con.createStatement();
		ResultSet rs = null;
		try {
			String query = "";
			query = "select rd.id,rd.employeeId,e.displayName as EmployeeName,e.PersonnelNo as EmployeePersonnelNo,rd.delegatedEmployeeId,e1.displayName as DelegatedEmployeeName,e1.PersonnelNo as DelegatedEmployeePersonnelNo,date_format(rd.from_date, '%d-%m-%Y') as from_date,date_format(rd.to_date, '%d-%m-%Y') as to_date from roleDelegation rd,employee e,employee e1 where rd.employeeId=e.id and rd.delegatedEmployeeId=e1.id and rd.status='a' order by rd.id desc";
			rs = st.executeQuery(query);
			System.out.println("dq"+query);
			DelegateRoleDto delegateRoleDto = null;
			while (rs.next()) {

				delegateRoleDto = new DelegateRoleDto();
				delegateRoleDto.setId(rs.getInt("id"));
				delegateRoleDto.setActualEmpId(rs.getString("employeeId"));
				delegateRoleDto.setEmployeeName(rs.getString("EmployeeName"));
				delegateRoleDto.setEmployeePersonnelNo(rs
						.getString("EmployeePersonnelNo"));
				delegateRoleDto.setDelegatedEmpId(rs
						.getString("delegatedEmployeeId"));
				delegateRoleDto.setDelegatedemployeeName(rs
						.getString("DelegatedEmployeeName"));
				delegateRoleDto.setDelegatedemployeePersonnelNo(rs
						.getString("DelegatedEmployeePersonnelNo"));
				delegateRoleDto.setFromDate(rs.getString("from_date"));
				delegateRoleDto.setToDate(rs.getString("to_date"));
				delegateRoleDtos.add(delegateRoleDto);
			}
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);
		}
		return delegateRoleDtos;
	}
	
	public ArrayList<DelegateRoleDto> getDelegatedRoles(long empId) throws SQLException {
		ArrayList<DelegateRoleDto> delegateRoleDtos = new ArrayList<DelegateRoleDto>();
		DbConnect ob = DbConnect.getInstance();
		Statement st = null;
		Connection con = ob.connectDB();
		st = con.createStatement();
		ResultSet rs = null;
		try {
			String query = "";
			query = "select rd.id,rd.employeeId,e.displayName as EmployeeName,e.PersonnelNo as EmployeePersonnelNo,rd.delegatedEmployeeId,e1.displayName as DelegatedEmployeeName,e1.PersonnelNo as DelegatedEmployeePersonnelNo,date_format(rd.from_date,'%d-%m-%Y') as from_date,date_format(to_date,'%d-%m-%Y') as to_date from roleDelegation rd,employee e,employee e1 where rd.employeeId=e.id and rd.delegatedEmployeeId=e1.id and e.id="+empId+" and rd.status='a' order by rd.id desc";
			rs = st.executeQuery(query);
			DelegateRoleDto delegateRoleDto = null;
			while (rs.next()) {

				delegateRoleDto = new DelegateRoleDto();
                delegateRoleDto.setId(rs.getInt("id"));
				delegateRoleDto.setActualEmpId(rs.getString("employeeId"));
				delegateRoleDto.setEmployeeName(rs.getString("EmployeeName"));
				delegateRoleDto.setEmployeePersonnelNo(rs
						.getString("EmployeePersonnelNo"));
				delegateRoleDto.setDelegatedEmpId(rs
						.getString("delegatedEmployeeId"));
				delegateRoleDto.setDelegatedemployeeName(rs
						.getString("DelegatedEmployeeName"));
				delegateRoleDto.setDelegatedemployeePersonnelNo(rs
						.getString("DelegatedEmployeePersonnelNo"));
				delegateRoleDto.setFromDate(rs.getString("from_date"));
				delegateRoleDto.setToDate(rs.getString("to_date"));
				delegateRoleDtos.add(delegateRoleDto);
			}
		} 
		catch(Exception e)
		{
			System.out.println("erro  "+e);
		}
		finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);
		}
		return delegateRoleDtos;
	}

	public DelegateRoleDto getDelegatedRolesById(int id){
		
		DelegateRoleDto delegateRoleDto = null;
		DbConnect ob = DbConnect.getInstance();
		Statement st = null;
		Connection con = ob.connectDB();
		System.out.println("Reached here");
		ResultSet rs = null;
		try {
			st = con.createStatement();
			String query = "";
			query = "select rd.id,rd.employeeId,e.displayName as EmployeeName,e.PersonnelNo as EmployeePersonnelNo,rd.delegatedEmployeeId,e1.displayName as DelegatedEmployeeName,e1.PersonnelNo as DelegatedEmployeePersonnelNo,date_format(rd.from_date,'%d/%m/%Y') as from_date,date_format(rd.to_date,'%d/%m/%Y')as to_date from roleDelegation rd,employee e,employee e1 where rd.employeeId=e.id and rd.delegatedEmployeeId=e1.id and rd.id="+id+" ";
			rs = st.executeQuery(query);
			
			if (rs.next()) {

				delegateRoleDto = new DelegateRoleDto();
                delegateRoleDto.setId(rs.getInt("id"));
				delegateRoleDto.setActualEmpId(rs.getString("employeeId"));
				delegateRoleDto.setEmployeeName(rs.getString("EmployeeName"));
				delegateRoleDto.setEmployeePersonnelNo(rs
						.getString("EmployeePersonnelNo"));
				delegateRoleDto.setDelegatedEmpId(rs
						.getString("delegatedEmployeeId"));
				delegateRoleDto.setDelegatedemployeeName(rs
						.getString("DelegatedEmployeeName"));
				delegateRoleDto.setDelegatedemployeePersonnelNo(rs
						.getString("DelegatedEmployeePersonnelNo"));
				delegateRoleDto.setFromDate(rs.getString("from_date"));
				delegateRoleDto.setToDate(rs.getString("to_date"));				
			}
		}catch(Exception e)
		
		{
		System.out.println("Error in role delegation dao"+e);	
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);
		}
		return delegateRoleDto;
	}


	public int insertDelegateRole(DelegateRoleDto delegateRoleDto)
			throws SQLException

	{
		DbConnect ob = DbConnect.getInstance();
		PreparedStatement pst = null;
		ResultSet autogenrs=null;
		int changedBy=0,autoincNumber=0;
		Connection con = ob.connectDB();
		String query = "";
		int retVal = 0;
		CallableStatement cst = null;
		try {
			 changedBy = Integer.parseInt(delegateRoleDto.getDoneBy());

			cst = con.prepareCall("{CALL checkroleDelegation(?,?,?,?,?)}");

			cst.setString(1, delegateRoleDto.getActualEmpId());
			cst.setString(2, delegateRoleDto.getDelegatedEmpId());
			cst.setString(3, OtherFunctions
					.changeDateFromatToIso(delegateRoleDto.getFromDate()));
			cst.setString(4, OtherFunctions
					.changeDateFromatToIso(delegateRoleDto.getToDate()));
			cst.registerOutParameter(5, Types.INTEGER);
			cst.execute();
			retVal = cst.getInt(5);
			if (retVal > -1) {
				query = "insert into roleDelegation(from_date,to_date,employeeId,delegatedEmployeeId,status) values (?,?,?,?,?)";
				pst = con.prepareStatement(query,Statement.RETURN_GENERATED_KEYS);
				pst.setString(1, OtherFunctions
						.changeDateFromatToIso(delegateRoleDto.getFromDate()));
				pst.setString(2, OtherFunctions
						.changeDateFromatToIso(delegateRoleDto.getToDate()));
				pst.setString(3, delegateRoleDto.getActualEmpId());
				pst.setString(4, delegateRoleDto.getDelegatedEmpId());
				pst.setString(5, "a");
				retVal = pst.executeUpdate();
			    autogenrs = pst.getGeneratedKeys();
				while (autogenrs.next()) {
					autoincNumber = autogenrs.getInt(1);
					System.out.println(autoincNumber);
				}
				if(retVal==1)
				{
					auditLogEntryfordelegaterole(autoincNumber,AuditLogConstants.DELEGATE_MODULE
							, changedBy,
							AuditLogConstants.WORKFLOW_STATE_EMPTY,
							AuditLogConstants.WORKFLOW_STATE_DELEGATED,
							AuditLogConstants.AUDIT_LOG_CREATE_ACTION);
				}
			}
		} finally {
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);
		}
		return retVal;
	}
	private void auditLogEntryfordelegaterole(int autoincNumber, String subcriptionEmpModule,
			int changedBy, String workflowStateEmpty,
			String workflowStateSubPending, String auditLogCreateAction) {
		AuditLogDTO auditLog = new AuditLogDTO();

		auditLog.setRelatedNodeId(autoincNumber);
		auditLog.setModuleName(subcriptionEmpModule);
		auditLog.setChangedBy(changedBy);
		auditLog.setDateChanged(new Date());
		auditLog.setPreviousState(workflowStateEmpty);
		auditLog.setCurrentState(workflowStateSubPending);
		auditLog.setAction(auditLogCreateAction);

		AuditLogDAO auditDAO = new AuditLogDAO();
		auditDAO.addAuditLogEntry(auditLog);

	}

	public int modifyDelegateRole(DelegateRoleDto delegateRoleDto) {
		String query = "update roleDelegation set to_date='"+OtherFunctions.changeDateFromatToIso(delegateRoleDto.getToDate())+"' where id="+delegateRoleDto.getId();
		
		DbConnect ob = DbConnect.getInstance();
		Connection con = ob.connectDB();
		Statement st = null;
		int retVal=0;
		try
		{
			st=con.createStatement();
			retVal=st.executeUpdate(query);
		}
		catch(Exception e)
		{
			System.out.println("Error "+e);
		}
		finally
		{
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);	
		}
		return retVal;
	}
	
	public int cancelDelegateRole(int Id) {
		String query = "update roleDelegation set status='c' where id="+Id;
		
		DbConnect ob = DbConnect.getInstance();
		Connection con = ob.connectDB();
		Statement st = null;
		int retVal=0;
		try
		{
			st=con.createStatement();
			retVal=st.executeUpdate(query);
		}
		catch(Exception e)
		{
			System.out.println("Error "+e);
		}
		finally
		{
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);	
		}
		return retVal;
	}
}
