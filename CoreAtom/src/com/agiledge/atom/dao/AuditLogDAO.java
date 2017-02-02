package com.agiledge.atom.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.agiledge.atom.commons.OtherFunctions;
import com.agiledge.atom.constants.AuditLogConstants;
import com.agiledge.atom.dbConnect.DbConnect;
import com.agiledge.atom.dto.AuditLogDTO;

public class AuditLogDAO {
	
	public String getPreviouseState(AuditLogDTO dto) {
		String previouseState = AuditLogConstants.WORKFLOW_STATE_EMPTY;
		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
				DbConnect  db = DbConnect.getInstance();
				con = db.connectDB();
				String query = "select * from audit_log where reference_id=? and module_name=? and id=(select max(id) from audit_log where reference_id=? and module_name=?)";
				st = con.prepareStatement(query);
				st.setLong(1, dto.getRelatedNodeId());
				st.setString(2, dto.getModuleName()	);
				st.setLong(3, dto.getRelatedNodeId());
				st.setString(4, dto.getModuleName()	);
				rs = st.executeQuery();
				if(rs.next()) {
					previouseState = rs.getString("module_name");
				}  
		} catch( Exception e ) {
			System.out.println(" Error in getPreviouseState " + e);
			
		}
		finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);
		}
		return previouseState;
	}
	
	public int addAuditLogEntry(AuditLogDTO auditLog) {
		DbConnect ob = DbConnect.getInstance();
		Connection con = null;
		PreparedStatement st = null;
		int returnValue=-1;
		try {
			con = ob.connectDB();
			String query="INSERT INTO AUDIT_LOG(REFERENCE_ID,MODULE_NAME,DATE_CHANGED,CHANGED_BY,PREVIOUS_STATE,CURRENT_STATE,ACTION) VALUES(?,?,now(),?,?,?,?)";
			st = con.prepareStatement(query);

			st.setLong(1, auditLog.getRelatedNodeId());
			st.setString(2, auditLog.getModuleName());			
			st.setInt(3, auditLog.getChangedBy());
			st.setString(4, auditLog.getPreviousState());
			st.setString(5, auditLog.getCurrentState());
			st.setString(6, auditLog.getAction());
			returnValue=st.executeUpdate();
		} catch (Exception e) {
			System.out.println("Error"+e);					
		} finally {
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);

		}
		return returnValue;

	}
	
	public int addAuditLogEntry(AuditLogDTO auditLog, Connection con) {
		DbConnect ob = DbConnect.getInstance();
		 
		PreparedStatement st = null;
		int returnValue=-1;
		try {
			con = ob.connectDB();
			String query="INSERT INTO AUDIT_LOG(REFERENCE_ID,MODULE_NAME,DATE_CHANGED,CHANGED_BY,PREVIOUS_STATE,CURRENT_STATE,ACTION) VALUES(?,?,now(),?,?,?,?)";
			st = con.prepareStatement(query);

			st.setLong(1, auditLog.getRelatedNodeId());
			st.setString(2, auditLog.getModuleName());			
			st.setInt(3, auditLog.getChangedBy());
			st.setString(4, auditLog.getPreviousState());
			st.setString(5, auditLog.getCurrentState());
			st.setString(6, auditLog.getAction());
			returnValue=st.executeUpdate();
		} catch (Exception e) {
			System.out.println("Error"+e);					
		} finally {
			DbConnect.closeStatement(st);
		 DbConnect.closeConnection(con);

		}
		return returnValue;

	}



	public List<AuditLogDTO> getAllAuditLogEntries(int releatedNodeId,
			String moduleName,String current) {
		DbConnect ob = DbConnect.getInstance();
		Connection con = null;
		PreparedStatement st = null,st1=null,st2=null;
		ResultSet rs = null,rs1=null,rs2=null;
		try {
			con = ob.connectDB();
			st = con.prepareStatement("SELECT *, cast( concat(  date_format(date_changed,'%d/%m/%Y')  , ':',   time(date_changed)  ) as char ) displaydate  FROM AUDIT_LOG WHERE REFERENCE_ID=? AND MODULE_NAME=?");
			System.out.println(" Inside getAllAuditLogEntires");
			st.setInt(1, releatedNodeId);
			st.setString(2, moduleName);
			String tQuery1=" FROM AUDIT_LOG WHERE REFERENCE_ID='%s' AND MODULE_NAME='%s'";
			tQuery1 = "SELECT *, cast( concat(  date_format(date_changed,'%d/%m/%Y')  , ':',   time(date_changed)  ) as char ) displaydate " + String.format(tQuery1,releatedNodeId, moduleName );
			System.out.println(" query 1:  " + tQuery1);
			
			ArrayList<AuditLogDTO> auditEntires = new ArrayList<AuditLogDTO>();
			if(moduleName.equals(AuditLogConstants.SCHEDULE_EMP))
			{
				if(current==null)
				{
				st1 = con.prepareStatement("SELECT *,cast( concat(  date_format(date_changed,'%d/%m/%Y')  , ':',   time(date_changed)  ) as char ) displaydate FROM AUDIT_LOG WHERE MODULE_NAME=? AND REFERENCE_ID=? AND CURRENT_STATE IN (?,?)");
			    String tQuery2 = " FROM AUDIT_LOG WHERE MODULE_NAME=? AND REFERENCE_ID='%s' AND CURRENT_STATE IN ('%s','%s')"; 
				st1.setString(1,AuditLogConstants.SCHEDULE_EMP );
				st1.setInt(2, releatedNodeId);
				st1.setString(3, AuditLogConstants.WORKFLOW_STATE_SCHEDULED);
				st1.setString(4, AuditLogConstants.WORKFLOW_STATE_MODIFY_SCHEDULE);
				tQuery2 = "SELECT *,cast( concat(  date_format(date_changed,'%d/%m/%Y')  , ':',   time(date_changed)  ) as char )  displaydate " + String.format(tQuery2, AuditLogConstants.SCHEDULE_EMP  
				 , releatedNodeId 
				 , AuditLogConstants.WORKFLOW_STATE_SCHEDULED,
				 AuditLogConstants.WORKFLOW_STATE_MODIFY_SCHEDULE);
	
				System.out.println(" query 2:  " + tQuery2);
				rs1=st1.executeQuery();
				while (rs1.next()) {
					
					AuditLogDTO audit = new AuditLogDTO();
					audit.setId(rs1.getInt("id"));
					audit.setRelatedNodeId(rs1.getInt("reference_id"));
					audit.setModuleName(rs1.getString("module_name"));
					audit.setDisplayDate(rs1.getString("displaydate"));
					audit.setDateChanged(rs1.getDate("date_changed"));
					audit.setChangedBy(rs1.getInt("changed_by"));
					audit.setPreviousState(rs1.getString("previous_state"));
					audit.setCurrentState(rs1.getString("current_state"));
					audit.setAction(rs1.getString("action"));
					auditEntires.add(audit);
				}
				st2=con.prepareStatement("SELECT *, cast( concat(  date_format(date_changed,'%d/%m/%Y')  , ':',   time(date_changed)  ) as char )  displaydate     FROM AUDIT_LOG WHERE MODULE_NAME=? AND REFERENCE_ID IN (SELECT ID FROM EMPLOYEE_SCHEDULE_ALTER WHERE SCHEDULEID=?) AND PREVIOUS_STATE=?");
				st2.setString(1,AuditLogConstants.SCHEDULE_EMP );
				st2.setInt(2, releatedNodeId);
				st2.setString(3, AuditLogConstants.WORKFLOW_STATE_SCHEDULED);
				rs2=st2.executeQuery();
				String tQuery3 = "   FROM AUDIT_LOG WHERE MODULE_NAME='%s' AND REFERENCE_ID IN (SELECT ID FROM EMPLOYEE_SCHEDULE_ALTER WHERE SCHEDULEID='%s') AND PREVIOUS_STATE='%s'";
				tQuery3 = "SELECT *, cast( concat(  date_format(date_changed,'%d/%m/%Y')  , ':',   time(date_changed)  ) as char )  displaydate " + String.format(tQuery3, AuditLogConstants.SCHEDULE_EMP ,  releatedNodeId , AuditLogConstants.WORKFLOW_STATE_SCHEDULED);
				System.out.println("Query 3 :  " + tQuery3);
				
				while (rs2.next()) {
					AuditLogDTO audit1 = new AuditLogDTO();
					audit1.setId(rs2.getInt("id"));
					audit1.setRelatedNodeId(rs2.getInt("reference_id"));
					audit1.setModuleName(rs2.getString("module_name"));
					audit1.setDisplayDate(rs2.getString("displaydate"));
					audit1.setDateChanged(rs2.getDate("date_changed"));
					audit1.setChangedBy(rs2.getInt("changed_by"));
					audit1.setPreviousState(rs2.getString("previous_state"));
					audit1.setCurrentState(rs2.getString("current_state"));
					audit1.setAction(rs2.getString("action"));
					auditEntires.add(audit1);
				}
				}
				else
				{
					st1 = con.prepareStatement("SELECT *,cast( concat(  date_format(date_changed,'%d/%m/%Y')  , ':',   time(date_changed)  ) as char ) displaydate   FROM AUDIT_LOG WHERE REFERENCE_ID=? AND MODULE_NAME=? AND CURRENT_STATE='"+current+"'");
					st1.setString(2,AuditLogConstants.SCHEDULE_EMP );
					st1.setInt(1, releatedNodeId);
					rs1=st1.executeQuery();
					while (rs1.next()) {
						AuditLogDTO audit = new AuditLogDTO();
						audit.setId(rs1.getInt("id"));
						audit.setRelatedNodeId(rs1.getInt("reference_id"));
						audit.setModuleName(rs1.getString("module_name"));
						audit.setDisplayDate(rs1.getString("displaydate"));
						audit.setDateChanged(rs1.getDate("date_changed"));
						audit.setChangedBy(rs1.getInt("changed_by"));
						audit.setPreviousState(rs1.getString("previous_state"));
						audit.setCurrentState(rs1.getString("current_state"));
						audit.setAction(rs1.getString("action"));
						auditEntires.add(audit);
					}
				
				}
			}
			else if(current!=null)
			{
				String query="SELECT *, cast( concat(  date_format(date_changed,'%d/%m/%Y')  , ':',   time(date_changed)  ) as char ) displaydate   FROM AUDIT_LOG WHERE MODULE_NAME=? AND REFERENCE_ID=? AND (CURRENT_STATE LIKE '%"+current+"%')";
				st2 = con.prepareStatement(query);
				st2.setString(1,moduleName);
				st2.setInt(2, releatedNodeId);
				rs2=st2.executeQuery();
				while (rs2.next()) {
					AuditLogDTO audit = new AuditLogDTO();
					audit.setId(rs2.getInt("id"));
					audit.setRelatedNodeId(rs2.getInt("reference_id"));
					audit.setModuleName(rs2.getString("module_name"));
					audit.setDisplayDate(rs2.getString("displaydate"));
					audit.setDateChanged(rs2.getDate("date_changed"));
					audit.setChangedBy(rs2.getInt("changed_by"));
					audit.setPreviousState(rs2.getString("previous_state"));
					audit.setCurrentState(rs2.getString("current_state"));
					audit.setAction(rs2.getString("action"));
					auditEntires.add(audit);
				}
			}
			else
			{
			rs = st.executeQuery();

			while (rs.next()) {

				System.out.println("Adding audit entries");

				AuditLogDTO audit = new AuditLogDTO();
				audit.setId(rs.getInt("id"));
				audit.setRelatedNodeId(rs.getInt("reference_id"));
				audit.setModuleName(rs.getString("module_name"));
				audit.setDisplayDate(rs.getString("displaydate"));
				audit.setDateChanged(rs.getDate("date_changed"));
				audit.setChangedBy(rs.getInt("changed_by"));
				audit.setPreviousState(rs.getString("previous_state"));
				audit.setCurrentState(rs.getString("current_state"));
				audit.setAction(rs.getString("action"));
				auditEntires.add(audit);
			}
			}
			return auditEntires;

		} catch (Exception e) {
			System.out.println("Error in auditlog :" +e);
			
		} finally {
			DbConnect.closeResultSet(rs,rs1,rs2);			
			DbConnect.closeStatement(st,st1);
			DbConnect.closeConnection(con);

		}

		return null;
	}
	public String getemployeename(int empid)
	{
		DbConnect ob = DbConnect.getInstance();
		Connection con = null;
		ResultSet rs = null;
		PreparedStatement st = null;
		String fullname=null,firstname=null,lastname=null;
		 try
		{
			con = ob.connectDB();
			st=con.prepareStatement("Select EmployeeFirstName,employeeLastName from employee where id=?");
			st.setInt(1,empid);
			rs=st.executeQuery();
			while(rs.next())
			{
			firstname=rs.getString("EmployeeFirstName");
			lastname=rs.getString("employeeLastName");
			}
			fullname=firstname+" "+lastname;
		}catch (Exception e) 
		{
			
		}finally
		{
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);
		}
		return fullname;
	}
	public ArrayList<AuditLogDTO> getTripSheetAuditLog(String site,String fromDate,String toDate)
	{
		DbConnect ob = DbConnect.getInstance();
		Connection con = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		ArrayList<AuditLogDTO> auditLogDTOs=new ArrayList<AuditLogDTO>();
		AuditLogDTO dto=null;
		try {
			con = ob.connectDB();
			String queryString="select t.id,t.trip_code, t.trip_date trip_date,t.trip_log,t.trip_time, cast( concat(  date_format(al.date_changed,'%d/%m/%Y')  , ':',   time(al.date_changed)  ) as char )   changedTime, e.displayname changedby,al.previous_state,al.current_state,al.action from audit_log al, employee e,trip_details t where t.siteId=? and  al.reference_id=t.id and  al.changed_by=e.id and   al.date_changed between ? and ? and al.module_name='"+AuditLogConstants.TRIPSHEET_MODULE+"' order by t.id";			
			pst=con.prepareStatement(queryString);
			pst.setString(1, site);
			pst.setString(2, OtherFunctions.changeDateFromatToIso(fromDate));
			pst.setString(3, OtherFunctions.changeDateFromatToIso(toDate));
			rs=pst.executeQuery();
			while(rs.next())
			{
				dto=new AuditLogDTO();
				dto.setRelatedNodeId(rs.getInt("id"));
				dto.setTripCode(rs.getString("trip_code"));
				dto.setTripDate(rs.getString("trip_date"));
				dto.setTripMode(rs.getString("trip_log"));
				dto.setTripTime(rs.getString("trip_time"));
				dto.setDisplayDate(rs.getString("changedTime"));
				dto.setChangedByName(rs.getString("changedBy"));
				dto.setPreviousState(rs.getString("previous_state"));
				dto.setCurrentState(rs.getString("current_state"));
				dto.setAction(rs.getString("action"));
				auditLogDTOs.add(dto);
			}
			

		} catch (Exception e) {
			System.out.println("Error"+e);
		} finally {
			DbConnect.closeResultSet(rs);			
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);

		}

		return auditLogDTOs;		
	}
	
	public int auditLogEntry(long autoincNumber, String Module,
			int changedBy, String preworkflowState,
			String curworkflowState, String auditLogAction) {
		AuditLogDTO auditLog = new AuditLogDTO();

		auditLog.setRelatedNodeId(autoincNumber);
		auditLog.setModuleName(Module);
		auditLog.setChangedBy(changedBy);
		auditLog.setDateChanged(new Date());
		if(preworkflowState==null || preworkflowState.equals("")) {
			auditLog.setPreviousState(this.getPreviouseState(auditLog));
		} else {
			auditLog.setPreviousState(preworkflowState);
		}
		auditLog.setCurrentState(curworkflowState);
		auditLog.setAction(auditLogAction);

		 
		 return addAuditLogEntry(auditLog);

	}
	
	public int auditLogEntry(long autoincNumber, String Module,
			int changedBy, String preworkflowState,
			String curworkflowState, String auditLogAction, Connection con) {
		AuditLogDTO auditLog = new AuditLogDTO();

		auditLog.setRelatedNodeId(autoincNumber);
		auditLog.setModuleName(Module);
		auditLog.setChangedBy(changedBy);
		auditLog.setDateChanged(new Date());
		if(preworkflowState==null || preworkflowState.equals("")) {
			auditLog.setPreviousState(this.getPreviouseState(auditLog));
		} else {
			auditLog.setPreviousState(preworkflowState);
		}
		auditLog.setCurrentState(curworkflowState);
		auditLog.setAction(auditLogAction);

		 
		 return addAuditLogEntry(auditLog,con);

	}


	
}

