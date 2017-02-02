package com.agiledge.atom.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;

import com.agiledge.atom.constants.AuditLogConstants;
import com.agiledge.atom.dbConnect.DbConnect;
import com.agiledge.atom.dto.AuditLogDTO;
import com.agiledge.atom.dto.BranchDto;


public class BranchDao {
	public class addEmployee {
	}

	public ArrayList<BranchDto> getBranches(String companyId)
			throws SQLException {
		DbConnect ob = DbConnect.getInstance();
		Connection con = ob.connectDB();
		BranchDto dto = null;
		ResultSet rs = null;
		Statement st = null;
		ArrayList<BranchDto> branchDtos = new ArrayList<BranchDto>();
		try {
			st = con.createStatement();
			rs = st.executeQuery("select  b.*,c.name as companyname  from branch b ,company c where c.id=b.companyid");
			while (rs.next()) {
				dto = new BranchDto();
				dto.setId("" + rs.getInt("id"));
				dto.setCompanyId("" + rs.getInt("companyid"));
				dto.setLocation(rs.getString("location"));
				dto.setCompanyName(rs.getString("companyname"));
				branchDtos.add(dto);
			}
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);

		}
		return branchDtos;
	}

	public int modifyBranch(BranchDto dto) {
		int returnInt = 0;
		DbConnect ob = DbConnect.getInstance();
		int changedBy = 0, autoincNumber;

		Connection con = ob.connectDB();
		PreparedStatement pst = null;
		try {
			String query = "update branch set location=? where id=?";
			pst = con.prepareStatement(query);
			pst.setString(1, dto.getLocation());
			pst.setString(2, dto.getId());
			changedBy = Integer.parseInt(dto.getDoneBy());
			autoincNumber = Integer.parseInt(dto.getId());
			returnInt = pst.executeUpdate();
			if (returnInt == 1) {
				auditLogEntryformodifybranch(autoincNumber,
						AuditLogConstants.COMPANY_BRANCH_MODULE, changedBy,
						AuditLogConstants.WORKFLOW_STATE_BRANCH_ADDED,
						AuditLogConstants.WORKFLOW_STATE_BRANCH_MODIFIED,
						AuditLogConstants.AUDIT_LOG_EDIT_ACTION);
			}

		} catch (Exception e) {
			System.out.println("Error in SettingDao getTransportationCost : e "
					+ e);
		}

		finally {
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);
		}

		return returnInt;
	}

	private void auditLogEntryformodifybranch(int autoincNumber, String Module,
			int changedBy, String preworkflowState, String curworkflowState,
			String auditLogAction) {
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

	public int addBranch(BranchDto dto) {
		int returnInt = 0;
		DbConnect ob = DbConnect.getInstance();
		ResultSet autogenrs = null;
		int changedBy = 0, autoincNumber;

		Connection con = ob.connectDB();
		PreparedStatement pst = null;
		try {
			Statement st = con.createStatement();
			changedBy = Integer.parseInt(dto.getDoneBy());
			String query = "insert into branch(companyid,location) values (?,?)";
			pst = con.prepareStatement(query);
			pst.setString(1, dto.getCompanyId());
			pst.setString(2, dto.getLocation());
			returnInt = pst.executeUpdate();
			if (returnInt == 1) {
				autogenrs = st
						.executeQuery("Select id from branch where companyid="
								+ dto.getCompanyId() + " and location='"
								+ dto.getLocation() + "'");
				while (autogenrs.next()) {
					autoincNumber = autogenrs.getInt(1);
					auditLogEntryforaddbranch(autoincNumber,
							AuditLogConstants.COMPANY_BRANCH_MODULE, changedBy,
							AuditLogConstants.WORKFLOW_STATE_EMPTY,
							AuditLogConstants.WORKFLOW_STATE_BRANCH_ADDED,
							AuditLogConstants.AUDIT_LOG_CREATE_ACTION);
				}
			}

		} catch (Exception e) {
			System.out.println("Error in Branch insert  dao " + e);
		}

		finally {
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);
		}

		return returnInt;

	}

	private void auditLogEntryforaddbranch(int autoincNumber, String Module,
			int changedBy, String preworkflowState, String curworkflowState,
			String auditLogAction) {
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

	public String getBranchLocation(String branchID) {
		DbConnect ob = DbConnect.getInstance();
		Connection con = ob.connectDB();
		BranchDto dto = null;
		ResultSet rs = null;
		Statement st = null;
		dto = new BranchDto();
		try {
			st = con.createStatement();
			rs = st.executeQuery("select b.id, b.companyid, b.location, c.name as companyName from branch b join company c on b.companyid=c.id where b.id="
					+ branchID);
			if (rs.next()) {

				dto.setId("" + rs.getInt("id"));
				dto.setCompanyId("" + rs.getInt("companyid"));
				dto.setLocation(rs.getString("location"));
				dto.setCompanyName(rs.getString("companyname"));
			}
		} catch (Exception e) {
			System.out.println("error" + e);
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);

		}
		return dto.getLocation();
	}

	public ArrayList<BranchDto> getLocations() {
		DbConnect ob = DbConnect.getInstance();
		Connection con = ob.connectDB();
		BranchDto dto = null;
		ResultSet rs = null;
		Statement st = null;
		ArrayList<BranchDto> locations=new ArrayList<BranchDto>();		
		try {
			st = con.createStatement();
			rs = st.executeQuery("select b.id, b.companyid, b.location from branch b ");
			while (rs.next()) {
				dto = new BranchDto();
				dto.setId("" + rs.getInt("id"));
				dto.setCompanyId("" + rs.getInt("companyid"));
				dto.setLocation(rs.getString("location"));
				locations.add(dto);
			}
		} catch (Exception e) {
			System.out.println("error" + e);
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);

		}
		return locations;
	}
}