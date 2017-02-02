package com.agiledge.atom.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import com.agiledge.atom.constants.AuditLogConstants;
import com.agiledge.atom.dbConnect.DbConnect;
import com.agiledge.atom.dto.AuditLogDTO;
import com.agiledge.atom.dto.CompanyDto;


public class CompanyDao {

	public class addEmployee {

	}

	public int addCompany(CompanyDto dto) {
		int returnInt = 0;
		DbConnect ob = DbConnect.getInstance();
		ResultSet autogenrs=null;
		int changedBy=0,autoincNumber;
		Connection con = ob.connectDB();
		PreparedStatement  pst = null;
		try {
			Statement st=con.createStatement();
			String query = "insert into company(name,address,website,contactpersonName,contactPersonNumber) values (?,?,?,?,?)";
			pst = con.prepareStatement(query);
			pst.setString(1, dto.getCompanyName());			
			pst.setString(2, dto.getAddress());
			pst.setString(3, dto.getWebsite());
			pst.setString(4, dto.getContactPersonName());
			pst.setString(5, dto.getContactPersonNumber());
			changedBy=Integer.parseInt(dto.getDoneBy());
			returnInt = pst.executeUpdate();
			if (returnInt==1)
			{
				autogenrs=st.executeQuery("Select id from company where name='"+dto.getCompanyName()+"' and address='"+dto.getAddress()+"' and website='"+dto.getWebsite()+"'");
				while(autogenrs.next())
				{
					autoincNumber=autogenrs.getInt(1);
					auditLogEntryforaddcompany(autoincNumber,AuditLogConstants.COMPANY_SETUP_MODULE
							, changedBy,
							AuditLogConstants.WORKFLOW_STATE_EMPTY,
							AuditLogConstants.WORKFLOW_STATE_COMPANY_ADDED,
							AuditLogConstants.AUDIT_LOG_CREATE_ACTION);
				}
			}

		} catch (Exception e) {
			System.out.println("Error in comapny insert dao"
					+ e);
		}

		finally {
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);
		}

		return returnInt;

	}
	private void auditLogEntryforaddcompany(int autoincNumber, String Module,
			int changedBy, String preworkflowState,
			String curworkflowState, String auditLogAction) {
		AuditLogDTO auditLog = new AuditLogDTO();

		auditLog.setRelatedNodeId(autoincNumber);
		auditLog.setModuleName(Module);
		auditLog.setChangedBy(changedBy);
		auditLog.setDateChanged(new Date());
		auditLog.setPreviousState(preworkflowState);
		auditLog.setCurrentState(curworkflowState);
		auditLog.setAction(auditLogAction);

		AuditLogDAO auditDAO = new AuditLogDAO();
		auditDAO.addAuditLogEntry(auditLog);

	}
	public CompanyDto getCompany() throws SQLException {
		DbConnect ob = DbConnect.getInstance();
		Connection con = ob.connectDB();
		CompanyDto dto=null;
		ResultSet rs=null;
		Statement st=null;
		try
		{
		st=con.createStatement();
		rs=st.executeQuery("select  * from company");
		if(rs.next())		
		{
			dto=new CompanyDto();
			dto.setCompanyID(""+rs.getInt("id"));
			dto.setCompanyName(rs.getString("name"));
			dto.setAddress(rs.getString("address"));
			dto.setWebsite(rs.getString("website"));
			dto.setContactPersonName(rs.getString("contactpersonName"));
			dto.setContactPersonNumber(rs.getString("contactPersonNumber"));
		}
	}
		finally{
		DbConnect.closeResultSet(rs);
		DbConnect.closeStatement(st);
		DbConnect.closeConnection(con);
		
			
		}
		return dto;	
}

	public int modifyCompany(CompanyDto dto) {		
		int returnInt = 0;
		DbConnect ob = DbConnect.getInstance();
		int changedBy=0,autoincNumber;
		Connection con = ob.connectDB();
		PreparedStatement  pst = null;
		try {
			String query = "update company set name=?,address=?,website=?,contactpersonName=?,contactPersonNumber=? where id=?";
			pst = con.prepareStatement(query);
			pst.setString(1, dto.getCompanyName());			
			pst.setString(2, dto.getAddress());
			pst.setString(3, dto.getWebsite());
			pst.setString(4, dto.getContactPersonName());
			pst.setString(5, dto.getContactPersonNumber());
			pst.setString(6, dto.getCompanyID());
			changedBy=Integer.parseInt(dto.getDoneBy());
			autoincNumber=Integer.parseInt(dto.getCompanyID());
			returnInt = pst.executeUpdate();
			if(returnInt==1)
			{
				auditLogEntryformodifycompany(autoincNumber,AuditLogConstants.COMPANY_SETUP_MODULE
						, changedBy,
						AuditLogConstants.WORKFLOW_STATE_COMPANY_ADDED,
						AuditLogConstants.WORKFLOW_STATE_COMPANY_MODIFIED,
						AuditLogConstants.AUDIT_LOG_EDIT_ACTION);
			}

		} catch (Exception e) {
			System.out.println("Error in company update dao"
					+ e);
		}

		finally {
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);
		}

		return returnInt;
	}
	private void auditLogEntryformodifycompany(int autoincNumber, String Module,
			int changedBy, String preworkflowState,
			String curworkflowState, String auditLogAction) {
		AuditLogDTO auditLog = new AuditLogDTO();

		auditLog.setRelatedNodeId(autoincNumber);
		auditLog.setModuleName(Module);
		auditLog.setChangedBy(changedBy);
		auditLog.setDateChanged(new Date());
		auditLog.setPreviousState(preworkflowState);
		auditLog.setCurrentState(curworkflowState);
		auditLog.setAction(auditLogAction);

		AuditLogDAO auditDAO = new AuditLogDAO();
		auditDAO.addAuditLogEntry(auditLog);

	}



}