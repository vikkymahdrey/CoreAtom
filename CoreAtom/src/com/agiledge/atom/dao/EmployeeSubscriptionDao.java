/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.agiledge.atom.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.RowId;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import com.agiledge.atom.commons.OtherFunctions;
import com.agiledge.atom.constants.AuditLogConstants;
import com.agiledge.atom.constants.SettingsConstant;
import com.agiledge.atom.dbConnect.DbConnect;
import com.agiledge.atom.dto.APLDto;
import com.agiledge.atom.dto.AuditLogDTO;
import com.agiledge.atom.dto.EmployeeDto;
import com.agiledge.atom.dto.EmployeeSubscriptionDto;
import com.agiledge.atom.mail.SendMail;
import com.agiledge.atom.mail.SendMailFactory;





/**
 * 
 * @author 123
 */
/**
 * @author noufalcc
 *
 */
public class EmployeeSubscriptionDao {

	ArrayList<Object> employeeSubscritionList = null;

	public EmployeeSubscriptionDao() {

	}

	// employee requests for subscription

	public int subscribeRequest(EmployeeSubscriptionDto dto) {

		DbConnect ob = DbConnect.getInstance();
		Statement st = null;
		Statement st1 = null;
		ResultSet rs1 = null;
		ResultSet autogenrs = null;
		//String role = "emp";
		int returnValue = 0;
		int autoincNumber = 0;
		Connection con = ob.connectDB();
		// PreparedStatement pst = null;
		try {
			// Connection pooling implementation

			// replace ob.connectDB() with Connection con=ob.connectDB();
			// replace ob.con with con
			String query1 = "select subscriptionID from employee_subscription where empID="
					+ dto.getEmployeeID()
					+ " and ( subscriptionStatus='"
					+ EmployeeSubscriptionDto.status.SUBSCRIBED
					+ "' or subscriptionStatus='"
					+ EmployeeSubscriptionDto.status.PENDING + "' )";
			String query = "insert into employee_subscription ( empID, supervisor, spoc, landMark, subscriptionDate, fromDate, subscriptionStatus) values ("
					+ dto.getEmployeeID()
					+ ", "
					+ dto.getSupervisor1()
					+ ", "
					+ dto.getSupervisor2()
					+ ", "
					+ dto.getLandMark()
					+ ",curdate(), '"
					+ OtherFunctions.changeDateFromatToIso(dto
							.getSubscriptionFromDate())
					+ "', '"
					+ EmployeeSubscriptionDto.status.PENDING + "');";
			String query2 = "";
			query2 = "update employee set site='"
					+ dto.getSite()
					+ "', contactNumber1='"
					+ dto.getContactNo()
					+ "', lineManager=" +dto.getSupervisor1() 
					+ " ,addressChangeDate=curdate()" +
					"   where id="
					+ dto.getEmployeeID();
			st = con.createStatement();
			st1 = con.createStatement();
			rs1 = st1.executeQuery(query1);
			if (!rs1.next()) {
				returnValue = st.executeUpdate(query,
						Statement.RETURN_GENERATED_KEYS);
				autogenrs = st.getGeneratedKeys();
				while (autogenrs.next()) {
					autoincNumber = autogenrs.getInt(1);
				}
				if (returnValue > 0) {
					System.out.println("query2 in SUb" + query2);
					st.executeUpdate(query2);
				}
			} else {
				returnValue = -1;
			}
			int changedBy = Integer.parseInt(dto.getDoneBy());
			auditLogEntry(autoincNumber,
					AuditLogConstants.SUBCRIPTION_EMP_MODULE, changedBy,
					AuditLogConstants.WORKFLOW_STATE_EMPTY,
					AuditLogConstants.WORKFLOW_STATE_SUB_PENDING,
					AuditLogConstants.AUDIT_LOG_CREATE_ACTION);
		} catch (Exception e) {

			System.out.println("Error in DAO-> SubscribeEmployee  subscribe : "
					+ e);

		} finally {
			DbConnect.closeResultSet(rs1);
			DbConnect.closeStatement(st1);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);
		}
		return returnValue;
	}

	private void auditLogEntry(int autoincNumber, String subcriptionEmpModule,
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

	public int modifySubscription(EmployeeSubscriptionDto dto) {
		DbConnect ob = DbConnect.getInstance();
		Statement st = null;
		int autoincNumber = 0;
		int returnValue = 0;
		String currentLandmark = "";
		Connection con = ob.connectDB();
		String currentstatus = null;
		String previousstatus = null;
		ResultSet auditrs = null;
		// PreparedStatement pst = null;
		System.out
				.println("select landMark from employee_subscription where subscriptionID="
						+ dto.getSubscriptionID());
		try {
			st = con.createStatement();
			ResultSet rs = st
					.executeQuery("select landMark,subscriptiondate,subscriptionstatus from employee_subscription where subscriptionID="
							+ dto.getSubscriptionID());
			if (rs.next()) {
				currentLandmark = rs.getString(1);
				dto.setSubscriptionDate(rs.getString(2));
				dto.setSubscriptionStatus(rs.getString(3));
			}
			// Connection pooling implementation

			// replace ob.connectDB() with Connection con=ob.connectDB();
			// replace ob.con with con
			
			
			String insertquery = "INSERT INTO employee_subscription_modify (subscriptionID,empid,supervisor,spoc,landmark,fromdate,effectivedate,subscriptiondate,subscriptionstatus,contactno,site,changedby) VALUES("+dto.getSubscriptionID()+","+dto.getEmployeeID()+","+dto.getSupervisor1()+","+dto.getSupervisor2()+","+dto.getLandMark()+",'"+OtherFunctions.changeDateFromatToIso(dto
					.getSubscriptionFromDate())+"','"+OtherFunctions.changeDateFromatToIso(dto.getEffectiveFrom())+"','"+dto.getSubscriptionDate()+"','"+dto.getSubscriptionStatus()+"','"+dto.getContactNo()+"',"+dto.getSite()+",'"+dto.getDoneBy()+"')";

			/*String query = "update  employee_subscription set supervisor="
					+ dto.getSupervisor1()
					+ ", spoc="
					+ dto.getSupervisor2()
					+ ", landMark="
					+ dto.getLandMark()
					+ ",  fromDate='"
					+ OtherFunctions.changeDateFromatToIso(dto
							.getSubscriptionFromDate())
					+ "' where subscriptionID=" + dto.getSubscriptionID() + " ";
			String query1 = "";
			if (dto.getLandMark().equals(currentLandmark)) {
				query1 = "update employee set  contactNumber1='"
						+ dto.getContactNo() + "',site=" + dto.getSite()
						+ ", lineManager=" + dto.getSupervisor1() + "   where id=" + dto.getEmployeeID();
			} else {
				query1 = "update employee set  contactNumber1='"
						+ dto.getContactNo()
						+ "', addressChangeDate=curdate() ,site="
						+ dto.getSite() + ", addresschangestatus=0, lineManager=" + dto.getSupervisor1() + " where id="
						+ dto.getEmployeeID();
			}*/
			auditrs = st
					.executeQuery("select subscriptionStatus from employee_subscription  where empID="
							+ dto.getEmployeeID()
							+ " order by subscriptionDate DESC ");
			while (auditrs.next()) {
				currentstatus = auditrs.getString(1);
			}
			autoincNumber = Integer.parseInt(dto.getSubscriptionID());

			if (currentstatus.equals("subscribed")) {
				previousstatus = AuditLogConstants.WORKFLOW_STATE_SUB_SUBSCRIBED;
				currentstatus = AuditLogConstants.WORKFLOW_STATE_SUB_SUBSCRIBED;
			} else if (currentstatus.equals("pending")) {
				previousstatus = AuditLogConstants.WORKFLOW_STATE_SUB_PENDING;
				currentstatus = AuditLogConstants.WORKFLOW_STATE_SUB_PENDING;
			}
			returnValue = st.executeUpdate(insertquery);
			//st.executeUpdate(query1);
			int changedBy = Integer.parseInt(dto.getDoneBy());
			auditLogEntryforModifysubscription(autoincNumber,
					AuditLogConstants.SUBCRIPTION_EMP_MODULE, changedBy,
					previousstatus, currentstatus,
					AuditLogConstants.AUDIT_LOG_EDIT_ACTION);
		/*	DateFormat formatter;
			
			formatter = new SimpleDateFormat("yyyy-MM-dd");
			String curDate = formatter.format(new Date());
				
			modifySubscriptionEffective(curDate);*/

		} catch (Exception e) {

			System.out
					.println("Error in DAO-> ModifySubscription  subscribe : "
							+ e);
		} finally {
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);
		}
		return returnValue;

	}
	public int modifySubscriptionEffective(String curdate) {
		DbConnect ob = DbConnect.getInstance();
		Statement st = null;
		Statement st1 = null;
		int autoincNumber = 0;
		int returnValue = 0;
		String currentLandmark = "";
		Connection con = ob.connectDB();
		String currentstatus = null;
		String previousstatus = null;
		ResultSet auditrs = null;
		// PreparedStatement pst = null;
		EmployeeSubscriptionDto dto = new EmployeeSubscriptionDto();
		
		
		try {

			String selectquery = "SELECT * FROM employee_subscription_modify WHERE effectivedate = '"+ curdate +"'ORDER BY id ";
			System.out.println("query" + selectquery );
			
			st = con.createStatement();
			st1 = con.createStatement();
			ResultSet resultset = st.executeQuery(selectquery);
			while(resultset.next())
			{
				System.out.println(resultset.getString("Subscriptionid"));
				dto.setSubscriptionID(resultset.getString("Subscriptionid"));
				dto.setSupervisor1(resultset.getString("supervisor"));
				dto.setSupervisor2(resultset.getString("spoc"));
				dto.setSubscriptionFromDate(resultset.getString("fromdate"));
				dto.setEmployeeID(resultset.getString("empid"));
				dto.setContactNo(resultset.getString("ContactNo"));
				dto.setSite(resultset.getString("site"));
				dto.setLandMark(resultset.getString("landmark"));
			
				
							

			ResultSet rs = st1
					.executeQuery("select landMark from employee_subscription where subscriptionID="+ dto.getSubscriptionID());
						
			if (rs.next()) {
				currentLandmark = rs.getString(1);
			}
			
			
			// Connection pooling implementation

			// replace ob.connectDB() with Connection con=ob.connectDB();
			// replace ob.con with con

			String query = "update  employee_subscription set supervisor="
					+ dto.getSupervisor1()
					+ ", spoc="
					+ dto.getSupervisor2()
					+ ", landMark="
					+ dto.getLandMark()
					+ ",  fromDate='"
					+ OtherFunctions.changeDateFromatToIso(dto
							.getSubscriptionFromDate())
					+ "' where subscriptionID=" + dto.getSubscriptionID() + " ";
			String query1 = "";
			if (dto.getLandMark().equals(currentLandmark)) {
				query1 = "update employee set  contactNumber1='"
						+ dto.getContactNo() + "',site=" + dto.getSite()
						+ ", lineManager=" + dto.getSupervisor1() + "   where id=" + dto.getEmployeeID();
			} else {
				query1 = "update employee set  contactNumber1='"
						+ dto.getContactNo()
						+ "', addressChangeDate=curdate() ,site="
						+ dto.getSite() + ", addresschangestatus=0, lineManager=" + dto.getSupervisor1() + " where id="
						+ dto.getEmployeeID();
			}
			auditrs = st1
					.executeQuery("select subscriptionStatus from employee_subscription  where empID="
							+ dto.getEmployeeID()
							+ " order by subscriptionDate DESC ");
			while (auditrs.next()) {
				currentstatus = auditrs.getString(1);
			}
			autoincNumber = Integer.parseInt(dto.getSubscriptionID());
			

			if (currentstatus.equals("subscribed")) {
				previousstatus = AuditLogConstants.WORKFLOW_STATE_SUB_SUBSCRIBED;
				currentstatus = AuditLogConstants.WORKFLOW_STATE_SUB_SUBSCRIBED;
			} else if (currentstatus.equals("pending")) {
				previousstatus = AuditLogConstants.WORKFLOW_STATE_SUB_PENDING;
				currentstatus = AuditLogConstants.WORKFLOW_STATE_SUB_PENDING;
			}
			
			returnValue = st1.executeUpdate(query);
			st1.executeUpdate(query1);
			
			int changedBy = 0;
			
			
			if(returnValue>0)
			{
				
			
				if(currentLandmark!=null&&!currentLandmark.equals("")&&!currentLandmark.equals(dto.getLandMark()))
				{
					new MailDao().sendMailForAplChange(dto.getEmployeeID());
				}
				
				auditLogEntryforModifysubscription(autoincNumber,
						AuditLogConstants.SUBCRIPTION_EMP_MODULE, changedBy,
						previousstatus, currentstatus,
						AuditLogConstants.AUDIT_LOG_EDIT_ACTION);
			}
			}
		} catch (Exception e) {

			System.out
					.println("Error in DAO->-> ModifySubscriptioneffective subscribe  : "
							+ e);
		} finally {
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);
		}
		return returnValue;

	}
	
	public int modifySubscribeRequest(EmployeeSubscriptionDto dto) {
		DbConnect ob = DbConnect.getInstance();
		Statement st = null;
		int autoincNumber = 0;
		int returnValue = 0;
		String currentLandmark = "";
		Connection con = ob.connectDB();
		String currentstatus = null;
		String previousstatus = null;
		ResultSet auditrs = null;
		// PreparedStatement pst = null;
		System.out
				.println("select landMark from employee_subscription where subscriptionID="
						+ dto.getSubscriptionID());
		try {
			st = con.createStatement();
			ResultSet rs = st
					.executeQuery("select landMark from employee_subscription where subscriptionID="
							+ dto.getSubscriptionID());
			if (rs.next()) {
				currentLandmark = rs.getString(1);
			}
			// Connection pooling implementation

			// replace ob.connectDB() with Connection con=ob.connectDB();
			// replace ob.con with con

			String query = "update  employee_subscription set supervisor="
					+ dto.getSupervisor1()
					+ ", spoc="
					+ dto.getSupervisor2()
					+ ", landMark="
					+ dto.getLandMark()
					+ ",  fromDate='"
					+ OtherFunctions.changeDateFromatToIso(dto
							.getSubscriptionFromDate())
					+ "' where subscriptionID=" + dto.getSubscriptionID() + " ";
			String query1 = "";
			if (dto.getLandMark().equals(currentLandmark)) {
				query1 = "update employee set  contactNumber1='"
						+ dto.getContactNo() + "',site=" + dto.getSite()
						+ ", lineManager=" + dto.getSupervisor1() + "   where id=" + dto.getEmployeeID();
			} else {
				query1 = "update employee set  contactNumber1='"
						+ dto.getContactNo()
						+ "', addressChangeDate=curdate() ,site="
						+ dto.getSite() + ", addresschangestatus=0, lineManager=" + dto.getSupervisor1() + " where id="
						+ dto.getEmployeeID();
			}
			auditrs = st
					.executeQuery("select subscriptionStatus from employee_subscription  where empID="
							+ dto.getEmployeeID()
							+ " order by subscriptionDate DESC ");
			while (auditrs.next()) {
				currentstatus = auditrs.getString(1);
			}
			autoincNumber = Integer.parseInt(dto.getSubscriptionID());

			if (currentstatus.equals("subscribed")) {
				previousstatus = AuditLogConstants.WORKFLOW_STATE_SUB_SUBSCRIBED;
				currentstatus = AuditLogConstants.WORKFLOW_STATE_SUB_SUBSCRIBED;
			} else if (currentstatus.equals("pending")) {
				previousstatus = AuditLogConstants.WORKFLOW_STATE_SUB_PENDING;
				currentstatus = AuditLogConstants.WORKFLOW_STATE_SUB_PENDING;
			}
			returnValue = st.executeUpdate(query);
			st.executeUpdate(query1);
			int changedBy = Integer.parseInt(dto.getDoneBy());
			auditLogEntryforModifysubscription(autoincNumber,
					AuditLogConstants.SUBCRIPTION_EMP_MODULE, changedBy,
					previousstatus, currentstatus,
					AuditLogConstants.AUDIT_LOG_EDIT_ACTION);

		} catch (Exception e) {

			System.out
					.println("Error in DAO-> ModifySubscribeRequest  subscribe : "
							+ e);
		} finally {
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);
		}
		return returnValue;

	}

	private void auditLogEntryforModifysubscription(int autoincNumber,
			String subcriptionEmpModule, int changedBy, String previousstate,
			String currentstate, String auditLogEditAction) {
		try{
		AuditLogDTO auditLog = new AuditLogDTO();

		auditLog.setRelatedNodeId(autoincNumber);
		auditLog.setModuleName(subcriptionEmpModule);
		auditLog.setChangedBy(changedBy);
		auditLog.setDateChanged(new Date());
		auditLog.setPreviousState(previousstate);
		auditLog.setCurrentState(currentstate);
		auditLog.setAction(auditLogEditAction);

		AuditLogDAO auditDAO = new AuditLogDAO();
		auditDAO.addAuditLogEntry(auditLog);
		}
		catch(Exception e)
		{
			System.out.println("error in modify subscription auditlog entey" + e);
		}
	}

	// utility subscribes all subscription request that are pending for current
	// date
	public int subscribe(String date) {
		DbConnect ob = DbConnect.getInstance();
		Statement st = null;
		ResultSet autogenrs = null;
		int returnValue = 0;
		int autoincNumber = 0;
		Connection con = ob.connectDB();
		// PreparedStatement pst = null;
		try {
			// Connection pooling implementation

			// replace ob.connectDB() with Connection con=ob.connectDB();
			// replace ob.con with con
			String queryaudit = "select subscriptionID from employee_subscription where subscriptionStatus='"
					+ EmployeeSubscriptionDto.status.PENDING
					+ "' and fromDate<='"
					+ date
					+ "' and subscriptionID not in (select subscriptionID from employee_unsubscription )";
			String query = "update employee_subscription set subscriptionStatus='"
					+ EmployeeSubscriptionDto.status.SUBSCRIBED
					+ "' where subscriptionStatus='"
					+ EmployeeSubscriptionDto.status.PENDING
					+ "' and fromDate<='"
					+ date
					+ "' and subscriptionID not in (select subscriptionID from employee_unsubscription )";
			st = con.createStatement();
			// System.out.println(query);
			autogenrs = st.executeQuery(queryaudit);
			int changedBy = AuditLogConstants.CHANGED_BY_SYSTEM;
			while (autogenrs.next()) {
				autoincNumber = autogenrs.getInt(1);
				auditLogEntryforsubscriptionsuccess(autoincNumber,
						AuditLogConstants.SUBCRIPTION_EMP_MODULE, changedBy,
						AuditLogConstants.WORKFLOW_STATE_SUB_PENDING,
						AuditLogConstants.WORKFLOW_STATE_SUB_SUBSCRIBED,
						AuditLogConstants.AUDIT_LOG_EDIT_ACTION);
			}

			returnValue = st.executeUpdate(query);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);
		} catch (Exception e) {
			DbConnect.closeConnection(con);
			System.out.println("Error in DAO-> SubscribeEmployee  subscribe : "
					+ e);

		}
		return returnValue;
	}

	private void auditLogEntryforsubscriptionsuccess(int autoincNumber,
			String subcriptionEmpModule, int changedBy,
			String workflowStateEmpty, String workflowStateSubPending,
			String auditLogCreateAction) {
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

	public boolean isSubscriptionRequestMade(String empid) {
		DbConnect ob = DbConnect.getInstance();
		Statement st = null;

		boolean returnValue = false;

		Connection con = ob.connectDB();
		// PreparedStatement pst = null;
		try {
			// Connection pooling implementation

			// replace ob.connectDB() with Connection con=ob.connectDB();
			// replace ob.con with co)n
			String query = "select empID,subscriptionStatus from employee_subscription where empID='"
					+ empid
					+ "' and  "
					+ "subscriptionStatus='"
					+ EmployeeSubscriptionDto.status.PENDING
					+ "'  and "
					+ "subscriptionID not in (select subscriptionID from employee_unsubscription )  ";
			st = con.createStatement();
			ResultSet rs = st.executeQuery(query);
			if (rs.next()) {
				returnValue = true;
			}
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);
			// System.out.println(" " + returnValue);

		} catch (Exception e) {
			DbConnect.closeConnection(con);
			System.out
					.println("Error in isSubcribed in EmployeeSubscriptionDao : "
							+ e);
		}

		return returnValue;

	}

	public boolean isSubscribed(String empid) {
		DbConnect ob = DbConnect.getInstance();
		Statement st = null;

		boolean returnValue = false;

		Connection con = ob.connectDB();
		// PreparedStatement pst = null;
		try {
			// Connection pooling implementation

			// replace ob.connectDB() with Connection con=ob.connectDB();
			// replace ob.con with con
			String query = "select empID,subscriptionStatus from employee_subscription where empID='"
					+ empid
					+ "' and ( subscriptionStatus='"
					+ EmployeeSubscriptionDto.status.SUBSCRIBED + "' ) ";
			st = con.createStatement();
			ResultSet rs = st.executeQuery(query);
			if (rs.next()) {
				returnValue = true;
			}
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);
			// System.out.println(" " + returnValue);

		} catch (Exception e) {
			DbConnect.closeConnection(con);
			System.out
					.println("Error in isSubcribed in EmployeeSubscriptionDao : "
							+ e);
		}

		return returnValue;

	}

	public boolean isUnsubscribed(String empid) {
		DbConnect ob = DbConnect.getInstance();
		Statement st = null;

		boolean returnValue = false;

		Connection con = ob.connectDB();
		// PreparedStatement pst = null;
		try {
			// Connection pooling implementation

			// replace ob.connectDB() with Connection con=ob.connectDB();
			// replace ob.con with con
			String query = "select empID,subscriptionStatus from employee_subscription where empID='"
					+ empid
					+ "' and ( subscriptionStatus='"
					+ EmployeeSubscriptionDto.status.UNSUBSCRIBED + "' ) ";
			st = con.createStatement();
			// System.out.println(query);
			ResultSet rs = st.executeQuery(query);
			if (rs.next()) {
				returnValue = true;
			}
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);
			// System.out.println(" " + returnValue);

		} catch (Exception e) {
			DbConnect.closeConnection(con);
			System.out
					.println("Error in isSubcribed in EmployeeSubscriptionDao : "
							+ e);
		}

		return returnValue;

	}

	public int unsubscribeRequest(EmployeeSubscriptionDto dto) {
		DbConnect ob = DbConnect.getInstance();
		Statement st = null;
		int autoincNumber = 0;
		int returnValue = 0;

		Connection con = ob.connectDB();
		// PreparedStatement pst = null;
		try {
			// Connection pooling implementation

			// replace ob.connectDB() with Connection con=ob.connectDB();
			// replace ob.con with con

			String query1 = " select subscriptionID from employee_subscription where empid="
					+ dto.getEmployeeID()
					+ "  and subscriptionStatus='"
					+ EmployeeSubscriptionDto.status.SUBSCRIBED
					+ "' and subscriptionID not in (select subscriptionID from employee_unsubscription ) ";
			// System.out.println("query 1 : " + query1);
			Statement stmt = con.createStatement();
			ResultSet rs1 = stmt.executeQuery(query1);
			String subscriptionID = "";
			if (rs1.next()) {
				subscriptionID = rs1.getString("subscriptionID");

				String query = "insert into employee_unsubscription ( subscriptionID, unsubscriptionDate, fromDate ) values ("
						+ subscriptionID
						+ ", curdate(), '"
						+ OtherFunctions.changeDateFromatToIso(dto
								.getUnsubscriptionFromDate()) + "' );";
				// System.out.println("Query : " + query);
				st = con.createStatement();
				// System.out.println(query);
				returnValue = st.executeUpdate(query);
				if (returnValue == 1) {
					autoincNumber = Integer.parseInt(subscriptionID);
					int changedBy = Integer.parseInt(dto.getEmployeeID());
					if (dto.getSource().equals("TransportAdmin"))
						changedBy = dto.getTransAdminId();
					auditLogEntryforunsubscription(autoincNumber,
							AuditLogConstants.UNSUBSCRIPTION_EMP_MODULE,
							changedBy,
							AuditLogConstants.WORKFLOW_STATE_SUB_SUBSCRIBED,
							AuditLogConstants.WORKFLOW_STATE_UNSUB_PENDING,
							AuditLogConstants.AUDIT_LOG_CREATE_ACTION);
				}
			}
			DbConnect.closeResultSet(rs1);

			DbConnect.closeStatement(stmt, st);
			DbConnect.closeConnection(con);
		} catch (Exception e) {
			DbConnect.closeConnection(con);
			System.out
					.println("Error in DAO-> SubscribeEmployee  unsubscribeRequest : "
							+ e);

		}
		return returnValue;
	}

	private void auditLogEntryforunsubscription(int autoincNumber,
			String subcriptionEmpModule, int changedBy,
			String workflowStateSubscribed, String workflowStateSubPending,
			String auditLogCreateAction) {
		AuditLogDTO auditLog = new AuditLogDTO();

		auditLog.setRelatedNodeId(autoincNumber);
		auditLog.setModuleName(subcriptionEmpModule);
		auditLog.setChangedBy(changedBy);
		auditLog.setDateChanged(new Date());
		auditLog.setPreviousState(workflowStateSubscribed);
		auditLog.setCurrentState(workflowStateSubPending);
		auditLog.setAction(auditLogCreateAction);

		AuditLogDAO auditDAO = new AuditLogDAO();
		auditDAO.addAuditLogEntry(auditLog);

	}

	public int unsubscribe(String from_date) {
		DbConnect ob = DbConnect.getInstance();
		Statement st = null;
		ResultSet autogenrs = null;
		int autoincNumber = 0;
		int returnValue = 0;

		Connection con = ob.connectDB();
		// PreparedStatement pst = null;
		try {
			// Connection pooling implementation

			// replace ob.connectDB() with Connection con=ob.connectDB();
			// replace ob.con with con
			String auditquery = "select subscriptionID from employee_unsubscription where fromDate='"
					+ from_date
					+ "' and subscriptionID in (select subscriptionID from employee_subscription where subscriptionStatus='"
					+ EmployeeSubscriptionDto.status.SUBSCRIBED + "' )";
			String query = "update employee_subscription set subscriptionStatus='"
					+ EmployeeSubscriptionDto.status.UNSUBSCRIBED
					+ "' where subscriptionID in (select subscriptionID from employee_unsubscription where fromDate<='"
					+ from_date + "')";

			st = con.createStatement();
			autogenrs = st.executeQuery(auditquery);
			Statement st1 = con.createStatement();
			returnValue = st1.executeUpdate(query);
			if (returnValue == 1) {
				int changedBy = AuditLogConstants.CHANGED_BY_SYSTEM;
				try{
				while (autogenrs.next()) {
					autoincNumber = autogenrs.getInt(1);
					auditLogEntryforunsubscriptionsuccess(autoincNumber,
							AuditLogConstants.UNSUBSCRIPTION_EMP_MODULE,
							changedBy,
							AuditLogConstants.WORKFLOW_STATE_UNSUB_PENDING,
							AuditLogConstants.WORKFLOW_STATE_SUB_UNSUBSCRIBED,
							AuditLogConstants.AUDIT_LOG_EDIT_ACTION);
				}
				}
				catch(Exception e)
				{
					System.out.println("error"+e);
				}
			}
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);
		} catch (Exception e) {
			DbConnect.closeConnection(con);
			System.out
					.println("Error in DAO-> SubscribeEmployee  unsubscribe : "
							+ e);

		}
		return returnValue;
	}

	private void auditLogEntryforunsubscriptionsuccess(int autoincNumber,
			String unsubcriptionEmpModule, int changedBy,
			String workflowStateUnsubPending, String workflowStateUnsub,
			String auditLogEditAction) {
		AuditLogDTO auditLog = new AuditLogDTO();

		auditLog.setRelatedNodeId(autoincNumber);
		auditLog.setModuleName(unsubcriptionEmpModule);
		auditLog.setChangedBy(changedBy);
		auditLog.setDateChanged(new Date());
		auditLog.setPreviousState(workflowStateUnsubPending);
		auditLog.setCurrentState(workflowStateUnsub);
		auditLog.setAction(auditLogEditAction);

		AuditLogDAO auditDAO = new AuditLogDAO();
		auditDAO.addAuditLogEntry(auditLog);

	}

	public boolean isUnsubscriptionRequestMade(String empid) {
		DbConnect ob = DbConnect.getInstance();
		Statement st = null;

		boolean returnValue = false;

		Connection con = ob.connectDB();
		// PreparedStatement pst = null;
		try {
			// Connection pooling implementation

			// replace ob.connectDB() with Connection con=ob.connectDB();
			// replace ob.con with con
			String query = "select empID,subscriptionStatus from employee_subscription s join employee_unsubscription u on s.subscriptionID=u.subscriptionID where empID='"
					+ empid
					+ "' and ( subscriptionStatus='"
					+ EmployeeSubscriptionDto.status.SUBSCRIBED + "' ) ";
			st = con.createStatement();
			// System.out.println(query);
			ResultSet rs = st.executeQuery(query);
			if (rs.next()) {
				returnValue = true;
			}
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);
			// System.out.println(" " + returnValue);

		} catch (Exception e) {
			DbConnect.closeConnection(con);
			System.out
					.println("Error in isSubcribed in EmployeeSubscriptionDao : "
							+ e);
		}

		return returnValue;

	}

	public EmployeeSubscriptionDto getUnsubscriptionRequestDetails(String empid) {
		DbConnect ob = DbConnect.getInstance();
		Statement st = null;
		Connection con = ob.connectDB();
		EmployeeSubscriptionDto dto = new EmployeeSubscriptionDto();
		// PreparedStatement pst = null;
		try {
			// Connection pooling implementation

			// replace ob.connectDB() with Connection con=ob.connectDB();
			// replace ob.con with con
			String query = "select es.subscriptionID,es.subscriptionStatus,eu.fromDate,eu.unsubscriptionDate from employee_subscription es left outer join employee_unsubscription eu on eu.subscriptionID=es.subscriptionID where es.empID="
					+ empid + " order by es.fromDate desc limit 1";
			st = con.createStatement();
			// System.out.println(query);
			ResultSet rs = st.executeQuery(query);
			if (rs.next()) {
				dto.setSubscriptionID(rs.getString("subscriptionID"));
				dto.setSubscriptionStatus(rs.getString("subscriptionStatus"));
				dto.setUnsubscriptionDate(rs.getDate("unsubscriptionDate")
						.toString());
				dto.setUnsubscriptionFromDate(rs.getDate("fromDate").toString());
			}
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);
			// System.out.println(" " + returnValue);

		} catch (Exception e) {
			DbConnect.closeConnection(con);
			System.out
					.println("Error in isSubcribed in EmployeeSubscriptionDao : "
							+ e);
		}

		return dto;

	}

	public EmployeeSubscriptionDto getEmployeeSubscriptionDetails(String empid) {

		DbConnect ob = DbConnect.getInstance();
		Statement st = null;
		EmployeeSubscriptionDto dto = null;
		ResultSet rs = null;
		Connection con = ob.connectDB();
		// PreparedStatement pst = null;
		// System.out.println("Emp id : " + empid);
		try {
			// Connection pooling implementation
			// replace ob.connectDB() with Connection con=ob.connectDB();
			// replace ob.con with con
			// String query =
			// "select s.subscriptionID, s.empID, s.supervisor, s.spoc, s.landMark,s.subscriptionDate,s.fromDate as fromDate  , s.subscriptionStatus,  e.site,e.contactNumber1,e.isContractEmployee,  (select top 1 case es.status when 'a' then 'Scheduled' when 'c' then 'Cancelled' else 'Not scheduled' end  from employee_schedule es where es.subscription_id=s.subscriptionID order by es.statusdate)   as scheduleStaus  from employee_subscription  s join employee e on  s.empID=e.id   where s.empID="
			// + empid + "    order by s.fromDate desc ";
			String query = "select s.subscriptionID, s.empID, e.displayname as employeeName, s.supervisor, sup.displayname as supervisorName, s.spoc, spoc.displayname as spocName, s.landMark, apl.landmarkName, apl.placeId, apl.placeName, apl.areaId, apl.areaName,s.subscriptionDate,s.fromDate as fromDate  , s.subscriptionStatus,  e.site,e.contactNumber1,e.isContractEmployee,  (select case es.status when 'a' then 'Scheduled' when 'c' then 'Cancelled' else 'Not scheduled' end  from employee_schedule es where es.subscription_id=s.subscriptionID order by es.statusdate limit 1)   as scheduleStaus,e.addressChangeDate as addressChangeDate  from employee_subscription  s join employee e on  s.empID=e.id join employee sup on s.supervisor=sup.id join employee spoc on s.spoc=spoc.id join (select landmark.id landmarkId, landmark.landmark landmarkName, placeId, placeName, areaName, areaId from landmark join (select place.id placeId, place.place placeName, area.id areaId, area.area areaName from place join area on area.id=place.area) ap on landmark.place=ap.placeId) apl on s.landMark=apl.landmarkId where s.empID="
					+ empid + " and s.subscriptionStatus in ('subscribed','pending')  order by s.fromDate desc ";
			st = con.createStatement();
			System.out.println(query);
			rs = st.executeQuery(query);
			if (rs.next()) {
				dto = new EmployeeSubscriptionDto();
				dto.setSubscriptionID(rs.getString("subscriptionID"));
				dto.setEmployeeID(rs.getString("empID"));
				EmployeeDto emp = new EmployeeDto();
				emp.setEmployeeID(rs.getString("empID"));
				emp.setEmployeeFirstName(rs.getString("employeeName"));
				emp.setAddressChangeDate(rs.getString("addressChangeDate"));
				dto.setEmployee(emp);
				dto.setSupervisor1(rs.getString("supervisor"));
				EmployeeDto supervisor = new EmployeeDto();
				supervisor.setEmployeeID(rs.getString("supervisor"));
				supervisor.setEmployeeFirstName(rs.getString("supervisorName"));
				dto.setSupervisor(supervisor);
				EmployeeDto spoc = new EmployeeDto();
				spoc.setEmployeeID(rs.getString("spoc"));
				spoc.setEmployeeFirstName(rs.getString("spocName"));
				dto.setSpoc(spoc);
				dto.setSupervisor2(rs.getString("spoc"));
				APLDto apl = new APLDto();
				apl.setLandMarkID(rs.getString("landMark"));
				apl.setLandMark(rs.getString("landmarkName"));
				apl.setPlaceID(rs.getString("placeId"));
				apl.setPlace(rs.getString("placeName"));
				apl.setAreaID(rs.getString("areaId"));
				apl.setArea(rs.getString("areaName"));
				dto.setApl(apl);
				dto.setLandMark(rs.getString("landMark"));
				dto.setSubscriptionFromDate("" + rs.getDate("fromDate"));
				dto.setSubscriptionDate("" + rs.getDate("subscriptionDate"));
				dto.setSubscriptionStatus(rs.getString("subscriptionStatus"));
				dto.setSite(rs.getString("site"));
				dto.setScheduleStatus(rs.getString("scheduleStaus"));
				dto.setContactNo(rs.getString("contactNumber1"));
				dto.setEmpType(rs.getString("isContractEmployee"));

			}

		} catch (Exception e) {

			System.out
					.println("Error in isSubcribed in EmployeeSubscriptionDao : "
							+ e);
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);
		}
		return dto;
	}

	// get the list of subscription details by single employee

	public List<EmployeeSubscriptionDto> getEmployeeSubscriptionsList(
			String empid) {

		DbConnect ob = DbConnect.getInstance();
		Statement st = null;
		List<EmployeeSubscriptionDto> dtoList = new ArrayList<EmployeeSubscriptionDto>();
		Connection con = ob.connectDB();
		// PreparedStatement pst = null;
		try {
			// Connection pooling implementation
			// replace ob.connectDB() with Connection con=ob.connectDB();
			// replace ob.con with con
			String query = " select sd.*,eus.fromDate as unsubscriptionDate  from (select s.subscriptionID, s.empID,emp.displayName as employeeName,  s.supervisor supervisorID,sup.displayName as supervisorName,  s.spoc, spoc.displayName as spocName ,s.landMark, landMark.landMark landmarkName, landMark.place landmarkPlace, landMark.area landmarkArea , s.subscriptionDate as subscriptionDate, s.fromDate as fromDate  ,  s.subscriptionStatus from employee_subscription s   join employee emp on s.empID=emp.id  join employee sup on sup.id=s.supervisor  join employee spoc on spoc.id=s.spoc join  ( select landMark.id id, landMark.landMark landMark, place.place place, area.area area from landMark  join place on landMark.place=place.id join area on place.area=area.id   ) as landMark on  landMark.id=s.landMark  ) as sd left join employee_unsubscription eus on sd.subscriptionID=eus.subscriptionID where sd.empID="
					+ empid;
			st = con.createStatement();
			// System.out.println(query);
			ResultSet rs = st.executeQuery(query);
			while (rs.next()) {
				EmployeeSubscriptionDto dto = new EmployeeSubscriptionDto();
				dto.setEmployeeID(rs.getString("empID"));
				EmployeeDto employee = new EmployeeDto();
				EmployeeDto supervisor = new EmployeeDto();
				EmployeeDto spoc = new EmployeeDto();
				APLDto apl = new APLDto();

				employee.setEmployeeID(dto.getEmployeeID());
				employee.setDisplayName(rs.getString("employeename"));
				supervisor.setEmployeeID(rs.getString("supervisorID"));
				supervisor.setDisplayName(rs.getString("supervisorName"));
				apl.setArea(rs.getString("landmarkArea"));
				apl.setPlace(rs.getString("landmarkPlace"));
				apl.setLandMark(rs.getString("landmarkName"));
				spoc.setEmployeeID(rs.getString("spoc"));
				spoc.setDisplayName(rs.getString("spocName"));
				dto.setUnsubscriptionDate("" + rs.getDate("unsubscriptionDate"));
				dto.setLandMark(rs.getString("landMark"));
				dto.setLandMarkName(rs.getString("landMarkName"));
				dto.setEmployee(employee);
				dto.setSupervisor(supervisor);
				dto.setSpoc(spoc);
				dto.setSubscriptionID(rs.getString("subscriptionID"));
				dto.setSupervisor1(rs.getString("supervisorID"));
				dto.setSupervisor2(rs.getString("spoc"));
				dto.setLandMark(rs.getString("landMark"));
				dto.setSubscriptionFromDate("" + rs.getDate("fromDate"));
				dto.setSubscriptionDate("" + rs.getDate("subscriptionDate"));
				dto.setSubscriptionStatus(rs.getString("subscriptionStatus"));
				dto.setApl(apl);
				dtoList.add(dto);
			}
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);
		} catch (Exception e) {
			DbConnect.closeConnection(con);
			System.out
					.println("Error in getEmployeeSubscriptionsList in EmployeeSubscriptionDao : "
							+ e);
		}
		return dtoList;
	}

	// update employee_subscription set aplChangeCounter= ( select
	// empAplChangeCounter sss from settings ) where subscriptionID in (select
	// subscriptionID from employee_subscription where
	// subscriptionStatus='subscribed' ) and empID in (select id from employee
	// where isContractEmployee='contract') and DATEADD(mm,-1,getdate())=
	// lastUpatedDate

	// to set address privilege for contract employee in each month
	/*
	 * public int setAddressChangePrivilegeCounterForContractEmployee(String
	 * curDate ) { DbConnect ob = DbConnect.getInstance(); Statement st = null;
	 * 
	 * int returnValue=0;
	 * 
	 * Connection con= ob.connectDB(); // PreparedStatement pst = null; try {
	 * //Connection pooling implementation
	 * 
	 * // replace ob.connectDB() with Connection con=ob.connectDB(); // replace
	 * ob.con with con
	 * System.out.println("in set address privilege counter of contract employee"
	 * ); String query=
	 * " update  employee_subscription set aplChangeCounter= ( select empAplChangeCounter sss from settings )    where subscriptionID in (select subscriptionID from employee_subscription where subscriptionStatus='"
	 * + EmployeeSubscriptionDto.status.SUBSCRIBED +
	 * "'  )    and empID in (select id from employee where isContractEmployee='contract') and DATEADD(mm,-1,'"
	 * + curDate + "')= lastUpatedDate ";
	 * 
	 * st= con.createStatement(); System.out.println("Query :" + query);
	 * returnValue=st.executeUpdate(query); }catch(Exception e) {
	 * System.out.println
	 * ("Error setAddressChangePrivillegeCounterfor contract employee: " + e);
	 * 
	 * } finally{ DbConnect.closeStatement(st); DbConnect.closeConnection(con);
	 * System.out.println("finally of set aplchangecounter"); } return
	 * returnValue;
	 * 
	 * 
	 * 
	 * }
	 * 
	 * 
	 * // to set number of days in which employee can modify his address public
	 * int setAddressChangePrivilegeCounter( ) { DbConnect ob =
	 * DbConnect.getInstance(); Statement st = null;
	 * 
	 * int returnValue=0;
	 * 
	 * Connection con= ob.connectDB(); // PreparedStatement pst = null; try {
	 * //Connection pooling implementation
	 * 
	 * // replace ob.connectDB() with Connection con=ob.connectDB(); // replace
	 * ob.con with con System.out.println("in set address privilege counter");
	 * String query=
	 * " update  employee_subscription set aplChangeCounter= ( select empAplChangeCounter sss from settings )    where subscriptionID in (select subscriptionID from employee_subscription where subscriptionStatus='"
	 * + EmployeeSubscriptionDto.status.SUBSCRIBED +
	 * "'  )    and empID in (select id from employee where addresschangestatus=1)"
	 * ;
	 * 
	 * st= con.createStatement(); System.out.println("Query :" + query);
	 * returnValue=st.executeUpdate(query); }catch(Exception e) {
	 * System.out.println("Error setAddressChangePrivillegeCounter: " + e);
	 * 
	 * } finally{ DbConnect.closeStatement(st); DbConnect.closeConnection(con);
	 * System.out.println("finally of set aplchangecounter"); } return
	 * returnValue;
	 * 
	 * 
	 * 
	 * } public Object countDownAplChangePrivilegeCounter() { // DbConnect ob =
	 * DbConnect.getInstance(); Statement st = null;
	 * 
	 * int returnValue=0;
	 * 
	 * Connection con= ob.connectDB(); // PreparedStatement pst = null; try {
	 * //Connection pooling implementation
	 * 
	 * // replace ob.connectDB() with Connection con=ob.connectDB(); // replace
	 * ob.con with con String query=
	 * " update employee_subscription set aplChangeCounter= aplChangeCounter-1 where aplChangeCounter >0 and subscriptionStatus='"
	 * + EmployeeSubscriptionDto.status.SUBSCRIBED + "'";
	 * 
	 * st= con.createStatement(); System.out.println("Query :" + query);
	 * returnValue=st.executeUpdate(query); }catch(Exception e) {
	 * System.out.println("Error countDownAplChangePrivilegeCounter() : " + e);
	 * 
	 * } finally{ DbConnect.closeStatement(st); DbConnect.closeConnection(con);
	 * 
	 * } return returnValue;
	 * 
	 * } public boolean hasPrivilegeToChangeAddress(String empid) {
	 * 
	 * boolean privilege = false; String query =
	 * "select * from employee_subscription where aplChangeCounter>0 ";
	 * 
	 * DbConnect ob = DbConnect.getInstance(); Statement st = null; ResultSet rs
	 * = null;
	 * 
	 * Connection con = ob.connectDB(); // PreparedStatement pst = null; try {
	 * //Connection pooling implementation
	 * 
	 * // replace ob.connectDB() with Connection con=ob.connectDB(); // replace
	 * ob.con with con
	 * 
	 * st = con.createStatement(); rs = st.executeQuery(query); if (rs.next()) {
	 * privilege = true; } } catch (Exception ex) {
	 * System.out.println("Error in EmployeeDao getEmployeeID() "); } finally{
	 * DbConnect.closeResultSet(rs); DbConnect.closeStatement(st);
	 * DbConnect.closeConnection(con);
	 * 
	 * } return privilege;
	 * 
	 * } public EmployeeSubscriptionDto getUnsubscriptionDetails(String empid) {
	 * 
	 * DbConnect ob = DbConnect.getInstance(); Statement st = null;
	 * EmployeeSubscriptionDto dto =null; Connection con= ob.connectDB(); //
	 * PreparedStatement pst = null; System.out.println("Emp id : " + empid);
	 * try { //Connection pooling implementation // replace ob.connectDB() with
	 * Connection con=ob.connectDB(); // replace ob.con with con String query=
	 * "select s.subscriptionID, s.empID, s.supervisor, s.spoc, s.landMark,s.subscriptionDate, convert(VARCHAR(10), s.fromDate,101) as fromDate, convert(VARCHAR(10), us.fromDate,101) as unsubscriptionFromDate   , s.subscriptionStatus,  e.site,e.contactNumber1,e.isContractEmployee   from employee_subscription  s join employee e on  s.empID=e.id join employee_unsubscription us on s.subscriptionID=us.subscriptionID  where s.empID="
	 * + empid + " and s.subscriptionStatus='" +
	 * EmployeeSubscriptionDto.status.UNSUBSCRIBED +
	 * "'    order by s.fromDate desc  "; st = con.createStatement();
	 * System.out.println(query); ResultSet rs= st.executeQuery(query);
	 * if(rs.next()) { dto= new EmployeeSubscriptionDto();
	 * dto.setEmployeeID(rs.getString("subscriptionID"));
	 * dto.setEmployeeID(rs.getString("empID"));
	 * dto.setSupervisor1(rs.getString("supervisor"));
	 * dto.setSupervisor2(rs.getString("spoc"));
	 * dto.setLandMark(rs.getString("landMark")); dto.setSubscriptionFromDate(""
	 * + rs.getString("fromDate"));
	 * dto.setUnsubscriptionFromDate(rs.getString("unsubscriptionFromDate"));
	 * dto.setSubscriptionDate("" + rs.getDate("subscriptionDate"));
	 * dto.setSubscriptionStatus(rs.getString("subscriptionStatus"));
	 * dto.setSite(rs.getString("site"));
	 * 
	 * dto.setContactNo(rs.getString("contactNumber1"));
	 * dto.setEmpType(rs.getString("isContractEmployee"));
	 * 
	 * } DbConnect.closeResultSet(rs); DbConnect.closeStatement(st);
	 * DbConnect.closeConnection(con); } catch(Exception e) {
	 * DbConnect.closeConnection(con);
	 * System.out.println("Error in isSubcribed in EmployeeSubscriptionDao : " +
	 * e); } return dto;
	 * 
	 * 
	 * }
	 * 
	 * // update employee_subscription set aplChangeCounter= ( select
	 * empAplChangeCounter sss from settings ) where subscriptionID in (select
	 * subscriptionID from employee_subscription where
	 * subscriptionStatus='subscribed' ) and empID in (select id from employee
	 * where isContractEmployee='contract') and DATEADD(mm,-1,getdate())=
	 * lastUpatedDate
	 * 
	 * // to set address privilege for contract employee in each month public
	 * int setAddressChangePrivilegeCounterForContractEmployee(String curDate )
	 * { DbConnect ob = DbConnect.getInstance(); Statement st = null;
	 * 
	 * int returnValue=0;
	 * 
	 * Connection con= ob.connectDB(); // PreparedStatement pst = null; try {
	 * //Connection pooling implementation
	 * 
	 * // replace ob.connectDB() with Connection con=ob.connectDB(); // replace
	 * ob.con with con
	 * System.out.println("in set address privilege counter of contract employee"
	 * ); String query=
	 * " update  employee_subscription set aplChangeCounter= ( select empAplChangeCounter sss from settings )    where subscriptionID in (select subscriptionID from employee_subscription where subscriptionStatus='"
	 * + EmployeeSubscriptionDto.status.SUBSCRIBED +
	 * "'  )    and empID in (select id from employee where isContractEmployee='contract') and DATEADD(mm,-1,'"
	 * + curDate + "')= lastUpatedDate ";
	 * 
	 * st= con.createStatement(); System.out.println("Query :" + query);
	 * returnValue=st.executeUpdate(query); }catch(Exception e) {
	 * System.out.println
	 * ("Error setAddressChangePrivillegeCounterfor contract employee: " + e);
	 * 
	 * } finally{ DbConnect.closeStatement(st); DbConnect.closeConnection(con);
	 * System.out.println("finally of set aplchangecounter"); } return
	 * returnValue;
	 * 
	 * 
	 * 
	 * }
	 * 
	 * 
	 * // to set number of days in which employee can modify his address public
	 * int setAddressChangePrivilegeCounter( ) { DbConnect ob =
	 * DbConnect.getInstance(); Statement st = null;
	 * 
	 * int returnValue=0;
	 * 
	 * Connection con= ob.connectDB(); // PreparedStatement pst = null; try {
	 * //Connection pooling implementation
	 * 
	 * // replace ob.connectDB() with Connection con=ob.connectDB(); // replace
	 * ob.con with con System.out.println("in set address privilege counter");
	 * String query=
	 * " update  employee_subscription set aplChangeCounter= ( select empAplChangeCounter sss from settings )    where subscriptionID in (select subscriptionID from employee_subscription where subscriptionStatus='"
	 * + EmployeeSubscriptionDto.status.SUBSCRIBED +
	 * "'  )    and empID in (select id from employee where addresschangestatus=1)"
	 * ;
	 * 
	 * st= con.createStatement(); System.out.println("Query :" + query);
	 * returnValue=st.executeUpdate(query); }catch(Exception e) {
	 * System.out.println("Error setAddressChangePrivillegeCounter: " + e);
	 * 
	 * } finally{ DbConnect.closeStatement(st); DbConnect.closeConnection(con);
	 * System.out.println("finally of set aplchangecounter"); } return
	 * returnValue;
	 * 
	 * 
	 * 
	 * } public Object countDownAplChangePrivilegeCounter() { // DbConnect ob =
	 * DbConnect.getInstance(); Statement st = null;
	 * 
	 * int returnValue=0;
	 * 
	 * Connection con= ob.connectDB(); // PreparedStatement pst = null; try {
	 * //Connection pooling implementation
	 * 
	 * // replace ob.connectDB() with Connection con=ob.connectDB(); // replace
	 * ob.con with con String query=
	 * " update employee_subscription set aplChangeCounter= aplChangeCounter-1 where aplChangeCounter >0 and subscriptionStatus='"
	 * + EmployeeSubscriptionDto.status.SUBSCRIBED + "'";
	 * 
	 * st= con.createStatement(); System.out.println("Query :" + query);
	 * returnValue=st.executeUpdate(query); }catch(Exception e) {
	 * System.out.println("Error countDownAplChangePrivilegeCounter() : " + e);
	 * 
	 * } finally{ DbConnect.closeStatement(st); DbConnect.closeConnection(con);
	 * 
	 * } return returnValue;
	 * 
	 * } public boolean hasPrivilegeToChangeAddress(String empid) {
	 * 
	 * boolean privilege = false; String query =
	 * "select * from employee_subscription where aplChangeCounter>0 ";
	 * 
	 * DbConnect ob = DbConnect.getInstance(); Statement st = null; ResultSet rs
	 * = null;
	 * 
	 * Connection con = ob.connectDB(); // PreparedStatement pst = null; try {
	 * //Connection pooling implementation
	 * 
	 * // replace ob.connectDB() with Connection con=ob.connectDB(); // replace
	 * ob.con with con
	 * 
	 * st = con.createStatement(); rs = st.executeQuery(query); if (rs.next()) {
	 * privilege = true; } } catch (Exception ex) {
	 * System.out.println("Error in EmployeeDao getEmployeeID() "); } finally{
	 * DbConnect.closeResultSet(rs); DbConnect.closeStatement(st);
	 * DbConnect.closeConnection(con);
	 * 
	 * } return privilege;
	 * 
	 * }
	 */
	public EmployeeSubscriptionDto getUnsubscriptionDetails(String empid) {

		DbConnect ob = DbConnect.getInstance();
		Statement st = null;
		ResultSet rs = null;
		EmployeeSubscriptionDto dto = null;
		Connection con = ob.connectDB();
		// PreparedStatement pst = null;
		// System.out.println("Emp id : " + empid);
		try {
			// Connection pooling implementation
			// replace ob.connectDB() with Connection con=ob.connectDB();
			// replace ob.con with con
			String query = "select s.subscriptionID, s.empID, s.supervisor, s.spoc, s.landMark,s.subscriptionDate, date_format(s.fromDate,'%d/%m/%Y') as fromDate, date_format(us.fromDate,'%d/%m/%Y') as unsubscriptionFromDate   , s.subscriptionStatus,  e.site,e.contactNumber1,e.isContractEmployee   from employee_subscription  s join employee e on  s.empID=e.id join employee_unsubscription us on s.subscriptionID=us.subscriptionID  where s.empID="
					+ empid
					+ " and s.subscriptionStatus='"
					+ EmployeeSubscriptionDto.status.UNSUBSCRIBED
					+ "'    order by s.fromDate desc  ";
			st = con.createStatement();
			// System.out.println(query);
			rs = st.executeQuery(query);
			if (rs.next()) {
				dto = new EmployeeSubscriptionDto();
				dto.setEmployeeID(rs.getString("subscriptionID"));
				dto.setEmployeeID(rs.getString("empID"));
				dto.setSupervisor1(rs.getString("supervisor"));
				dto.setSupervisor2(rs.getString("spoc"));
				dto.setLandMark(rs.getString("landMark"));
				dto.setSubscriptionFromDate("" + rs.getString("fromDate"));
				dto.setUnsubscriptionFromDate(rs
						.getString("unsubscriptionFromDate"));
				dto.setSubscriptionDate("" + rs.getDate("subscriptionDate"));
				dto.setSubscriptionStatus(rs.getString("subscriptionStatus"));
				dto.setSite(rs.getString("site"));

				dto.setContactNo(rs.getString("contactNumber1"));
				dto.setEmpType(rs.getString("isContractEmployee"));

			}

		} catch (Exception e) {
			DbConnect.closeConnection(con);
			System.out
					.println("Error in isSubcribed in EmployeeSubscriptionDao : "
							+ e);
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);
		}
		return dto;

	}

	public List<EmployeeSubscriptionDto> getSubscriptionDetailsOfEmployeesForSubscription(
			String onDate, int daysBefore) {
		List<EmployeeSubscriptionDto> list = new ArrayList<EmployeeSubscriptionDto>();
		DbConnect ob = DbConnect.getInstance();
		Statement st = null;
		ResultSet rs = null;
		Connection con = ob.connectDB();
		// PreparedStatement pst = null;

		try {
			// Connection pooling implementation
			// replace ob.connectDB() with Connection con=ob.connectDB();
			// replace ob.con with con
			String query = "select s.subscriptionID, s.empID, s.supervisor, s.spoc, s.landMark,s.subscriptionDate, date(s.fromDate) as fromDate,   s.subscriptionStatus,  e.site,e.contactNumber1,e.isContractEmployee   from employee_subscription  s join employee e on  s.empID=e.id     where  s.fromDate= date_add('"
					+ onDate
					+ "',interval "+daysBefore +" day)"
					+"   order by s.fromDate desc  ";
			st = con.createStatement();
			System.out.println(query);
			rs = st.executeQuery(query);

			while (rs.next()) {
				EmployeeSubscriptionDto dto = new EmployeeSubscriptionDto();

				dto = new EmployeeSubscriptionDto();
				dto.setEmployeeID(rs.getString("subscriptionID"));
				dto.setEmployeeID(rs.getString("empID"));
				dto.setSupervisor1(rs.getString("supervisor"));
				dto.setSupervisor2(rs.getString("spoc"));
				dto.setLandMark(rs.getString("landMark"));
				dto.setSubscriptionFromDate("" + rs.getString("fromDate"));

				dto.setSubscriptionDate("" + rs.getDate("subscriptionDate"));
				dto.setSubscriptionStatus(rs.getString("subscriptionStatus"));
				dto.setSite(rs.getString("site"));

				dto.setContactNo(rs.getString("contactNumber1"));
				dto.setEmpType(rs.getString("isContractEmployee"));
				list.add(dto);

			}

		} catch (Exception e) {

			System.out
					.println("Error in isSubcribed in EmployeeSubscriptionDao : "
							+ e);
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);
		}
		return list;

	}

	public List<EmployeeSubscriptionDto> getSubscriptionDetailsOfEmployeesForUnsubscription(
			String onDate, int daysBefore) {
		List<EmployeeSubscriptionDto> list = new ArrayList<EmployeeSubscriptionDto>();
		DbConnect ob = DbConnect.getInstance();
		Statement st = null;
		ResultSet rs = null;
		Connection con = ob.connectDB();
		// PreparedStatement pst = null;

		try {
			// Connection pooling implementation
			// replace ob.connectDB() with Connection con=ob.connectDB();
			// replace ob.con with con
			String query = "select s.subscriptionID, s.empID, s.supervisor, s.spoc, s.landMark,s.subscriptionDate, date_format(s.fromDate,'%d/%m/%Y') as fromDate, date_format(us.fromDate,'%d/%m/%Y') as unsubscriptionFromDate   , s.subscriptionStatus,  e.site,e.contactNumber1,e.isContractEmployee   from employee_subscription  s join employee e on  s.empID=e.id join employee_unsubscription us on s.subscriptionID=us.subscriptionID   where  us.fromDate=" +
					"date(date_add('"
					+ onDate
					+ "',interval "
					+ daysBefore +" day"										
					+ "))   order by s.fromDate desc  ";
			st = con.createStatement();
			rs = st.executeQuery(query);
			while (rs.next()) {
				EmployeeSubscriptionDto dto = new EmployeeSubscriptionDto();

				dto = new EmployeeSubscriptionDto();
				dto.setEmployeeID(rs.getString("subscriptionID"));
				dto.setEmployeeID(rs.getString("empID"));
				dto.setSupervisor1(rs.getString("supervisor"));
				dto.setSupervisor2(rs.getString("spoc"));
				dto.setLandMark(rs.getString("landMark"));
				dto.setSubscriptionFromDate("" + rs.getString("fromDate"));
				dto.setUnsubscriptionFromDate(rs
						.getString("unsubscriptionFromDate"));
				dto.setSubscriptionDate("" + rs.getDate("subscriptionDate"));
				dto.setSubscriptionStatus(rs.getString("subscriptionStatus"));
				dto.setSite(rs.getString("site"));

				dto.setContactNo(rs.getString("contactNumber1"));
				dto.setEmpType(rs.getString("isContractEmployee"));
				list.add(dto);

			}

		} catch (Exception e) {

			System.out
					.println("Error in isSubcribed in EmployeeSubscriptionDao : "
							+ e);
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);
		}
		return list;

	}

	public String getSubscriptionIdFromScheduleId(String schID) {
		DbConnect ob = DbConnect.getInstance();
		Statement st = null;
		ResultSet rs = null;
		String subID = null;
		Connection con = ob.connectDB();

		try {

			String query = "select subscriptionID from employee_subscription join employee_schedule on subscription_id=subscriptionID where id="
					+ schID;
			st = con.createStatement();
			// System.out.println(query);
			rs = st.executeQuery(query);
			if (rs.next()) {
				subID = rs.getString("subscriptionID");

			}

		} catch (Exception e) {
			DbConnect.closeConnection(con);
			System.out
					.println("Error in isSubcribed in EmployeeSubscriptionDao : "
							+ e);
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);
		}
		return subID;

	}

	public boolean isPendingOrSubscribed(String empid) {

		DbConnect ob = DbConnect.getInstance();
		Statement st = null;

		boolean returnValue = false;

		Connection con = ob.connectDB();
		// PreparedStatement pst = null;
		try {
			// Connection pooling implementation

			// replace ob.connectDB() with Connection con=ob.connectDB();
			// replace ob.con with con
			String query = "select empID,subscriptionStatus from employee_subscription where empID='"
					+ empid
					+ "' and ( subscriptionStatus='"
					+ EmployeeSubscriptionDto.status.SUBSCRIBED
					+ "' or subscriptionStatus='"
					+ EmployeeSubscriptionDto.status.PENDING + "'  ) ";
			st = con.createStatement();
			// System.out.println(query);
			ResultSet rs = st.executeQuery(query);
			if (rs.next()) {
				returnValue = true;
			}
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);
			// System.out.println(" " + returnValue);

		} catch (Exception e) {
			DbConnect.closeConnection(con);
			System.out
					.println("Error in isPendingOrSubcribed in EmployeeSubscriptionDao : "
							+ e);
		}

		return returnValue;

	}

	public String getLandmark(String employeeId) {
		String landmarkId = "";
		DbConnect ob = DbConnect.getInstance();
		Statement st = null;

		Connection con = ob.connectDB();
		// PreparedStatement pst = null;
		try {
			// Connection pooling implementation

			// replace ob.connectDB() with Connection con=ob.connectDB();
			// replace ob.con with con
			String query = "select landmark from employee_subscription where empID='"
					+ employeeId
					+ "' and ( subscriptionStatus='"
					+ EmployeeSubscriptionDto.status.SUBSCRIBED
					+ "' or subscriptionStatus='"
					+ EmployeeSubscriptionDto.status.PENDING + "'  ) ";
			st = con.createStatement();
			// System.out.println(query);
			ResultSet rs = st.executeQuery(query);
			if (rs.next()) {
				landmarkId = rs.getString(1);
			}
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);
			// System.out.println(" " + returnValue);

		} catch (Exception e) {
			DbConnect.closeConnection(con);
			System.out
					.println("Error in getLandmark Id in EmployeeSubscriptionDao : "
							+ e);
		}

		return landmarkId;
	}
	
	/*
	 * Upload subscription from excel file
	 * 1. Prevent update if manager / spoc is wrong (any employee can be choosen as spoc/manager)
	 * 2. Subscription can be done for past date and future dates
	 * 3. There is no validation b/w requested date and effective date. Requested date will be current date.
	 * 3. If there is an active subscription ( on date where the current subscription is to be active):
	 *      3.1. Unsubscribe active subscription, inticate forcefull. Unsubscription requested date and effective date should be the same
	 *      3.1.1 Unsubscription failes if there is any schedule for the subscription after the unsubscription date.
	 *       
	 */
	public int uploadSubscription(ArrayList<EmployeeSubscriptionDto> subList, String site, String changedBy) {
		int returnInt=0;
		Connection con = null;
		PreparedStatement checkSpocSt = null;
		PreparedStatement checkManagerSt = null;
		PreparedStatement checkSubscriptionSt = null;
		PreparedStatement forceUnSubscriptionSt = null;
		PreparedStatement forceUnsubscriptionSt1 =  null;
		PreparedStatement checkScheduleAfterSubscriptionSt = null;
		PreparedStatement insertSubscriptionSt = null;
		PreparedStatement employeeCheckSt = null;
		PreparedStatement aplCheckSt = null;
		
		PreparedStatement updateEmployeeSt = null;
		ResultSet rsManager = null;
		ResultSet rsSpoc = null;
		ResultSet rsSubscription = null;
		ResultSet rsSchedule = null;
		ResultSet spocRs = null;
		ResultSet managerRs = null;
		ResultSet auditRs =null;
		ResultSet auditRs1 =null;
		ResultSet employeeRs = null;
		ResultSet aplRs = null;
				
		int rowCount=2;
		boolean error=false;
		try {
			AuditLogDAO auditDao = new AuditLogDAO();
			messageList.clear();
			con = DbConnect.getInstance().connectDB();
			con.setAutoCommit(false);
			String aplCheckQuery = "select * from area a, place p, landmark l where l.place=p.id and p.area=a.id and " +
			" a.area=? and p.place=? and l.landmark=?";
			String updateEmployeeQuery = "update employee  set site=?, lineManager=? where  personnelNo=? ";
			String checkSpocQuery = "select * from employee where personnelNo=?";
			String checkManagerQuery = "select * from employee where personnelNo=?";
			String checkSubscriptionQuery ="select es.subscriptionID from employee_subscription es left join employee_unsubscription us on es.subscriptionId=us.subscriptionId join employee e on es.empid=e.id where es.fromDate <=? and (us.fromDate >? or us.fromDate is null )  and e.personnelNo=? order by es.fromDate desc";
			String checkScheduleAfterSubscriptionQuery="select sh.id from employee_schedule sh where sh.subscription_id=? and sh.to_date >=?;";
			String forceUnSubscriptionQuery="insert into employee_unsubscription (subscriptionId, unsubscriptionDate, fromDate ) values (?, ?, ?)";
			String forceUnsubscriptionQuery1 = "update employee_subscription set subscriptionStatus='unsubscribed'  where subscriptionId in (select subscriptionid from employee_unsubscription ) and (subscriptionStatus= 'pending' or  subscriptionStatus='subscribed'  )";
			String insertSubscriptionQuery = "insert into employee_subscription ( empID, supervisor, spoc, landMark, subscriptionDate, fromDate, subscriptionStatus, aplChangeCounter, lastUpdatedDate, latitude, longitude ) "+
											"select e.id, sup.id, mgr.id, l.id, ? , ?, ?, 0, now(), l.latitude, l.longitude from employee e, employee sup, employee mgr, area a, place p, landmark l " +
											"where e.personnelNo=? and mgr.personnelNo=? and sup.personnelNo=? and l.place = p.id and p.area = a.id and a.area = ? and p.place=? and l.landmark=? ";
			if(subList!=null && subList.size()>0) {
				
//				Statement instantiation
				updateEmployeeSt = con.prepareStatement(updateEmployeeQuery);
				forceUnsubscriptionSt1 = con.prepareStatement(forceUnsubscriptionQuery1);
				for(EmployeeSubscriptionDto esubDto : subList) {
					
					try {
//					check employee
					employeeCheckSt = con.prepareStatement(checkManagerQuery);
					employeeCheckSt.setString(1, esubDto.getEmployeeID());
					employeeRs = employeeCheckSt.executeQuery();
					if(employeeRs.next()==false) {
						getMessageList().add("Line :" + rowCount + " Error : Invalid Employee " + esubDto.getEmployeeID());
						rowCount++;
						error =true;
						continue;
					}
//					check apl
					aplCheckSt = con.prepareStatement(aplCheckQuery);
					aplCheckSt.setString(1, esubDto.getApl().getArea());
					aplCheckSt.setString(2, esubDto.getApl().getPlace());
					aplCheckSt.setString(3, esubDto.getApl().getLandMark());
					aplRs = aplCheckSt.executeQuery();
					if(aplRs.next()==false) {
						getMessageList().add("Line :" + rowCount + " Error : Invalid Apl ( " + esubDto.getApl().getArea() + ", "+ esubDto
								.getApl().getPlace() + ", "+ esubDto.getApl().getLandMark() + " ) ");
						rowCount++;
						error =true;
						continue;
					}
//					check invalid manager
					
					checkManagerSt =  con.prepareStatement(checkManagerQuery);
					checkManagerSt.setString(1, esubDto.getSupervisor1() );
					rsManager= checkManagerSt.executeQuery();
					 
					if(rsManager.next()==false) {
						getMessageList().add("Line :" + rowCount + " Error : Invalid Manager " + esubDto.getSupervisor1());
						rowCount++;
						error =true;
						continue;
					}
					 
//					check invalid spoc 
					checkSpocSt =  con.prepareStatement(checkSpocQuery);
					checkSpocSt.setString(1, esubDto.getSupervisor2() );
					rsSpoc= checkSpocSt.executeQuery();
					if(rsSpoc.next()==false) {
						getMessageList().add("Line :" + rowCount + " Error : Invalid Spoc " + esubDto.getSupervisor2());
						rowCount++;
						error =true;
						continue;
					}
					 
//					 check any active subscription exists
					checkSubscriptionSt = con.prepareStatement(checkSubscriptionQuery);
					checkSubscriptionSt.setString(1, esubDto.getSubscriptionFromDate());
					checkSubscriptionSt.setString(2, esubDto.getSubscriptionFromDate()); 
					checkSubscriptionSt.setString(3, esubDto.getEmployeeID());	// personnelNo
					rsSubscription = checkSubscriptionSt.executeQuery();
					if(rsSubscription.next()) {
						// active subscription is there
						checkScheduleAfterSubscriptionSt = con.prepareStatement(checkScheduleAfterSubscriptionQuery);
						System.out.println(String.format(checkScheduleAfterSubscriptionQuery.replace("?", "'%s'")
								, rsSubscription.getString("subscriptionID"),esubDto.getSubscriptionFromDate()
								));
						checkScheduleAfterSubscriptionSt.setString(1, rsSubscription.getString("subscriptionID"));
						checkScheduleAfterSubscriptionSt.setString(2, esubDto.getSubscriptionFromDate() );
						rsSchedule = checkScheduleAfterSubscriptionSt.executeQuery();
						if(rsSchedule.next()) {
							 
							getMessageList().add("Line : " + rowCount + " Error : active schedule exists in previouse subscriptions" );
							rowCount++;
							error=true;
							continue;
						} else {
							forceUnSubscriptionSt = con.prepareStatement(forceUnSubscriptionQuery, Statement.RETURN_GENERATED_KEYS);
							forceUnSubscriptionSt.setString(1, rsSubscription.getString("subscriptionID"));
							forceUnSubscriptionSt.setString(2, esubDto.getSubscriptionDate());
							forceUnSubscriptionSt.setString(3, esubDto.getSubscriptionFromDate());
							int  usInt = forceUnSubscriptionSt.executeUpdate();
							if(usInt<=0) {
								getMessageList().add("Line : " + rowCount + " Error : Forcefull unsubscription failed." );
								rowCount++;
								error=true;
								continue;
							} else {
								auditRs = forceUnSubscriptionSt.getGeneratedKeys();
								if(auditRs.next() ) {
									 
									auditDao.auditLogEntry(auditRs.getLong(1), AuditLogConstants.SUBCRIPTION_ADMIN_MODULE, Integer.parseInt(changedBy),null , AuditLogConstants.WORKFLOW_STATE_UNSUB_FORCE_UPATE, AuditLogConstants.AUDIT_LOG_UPLOAD_ACTION, con);
								}
								DbConnect.closeResultSet(auditRs);
								
								
							}
						}
					
						
						
					}
					 
//					subscription insert code
					//'subdate' , 'fromDate', 'subscribed'
					//  e.id=100 and mgr.id=101 and sup.id=101
					//  a.area = 'Hormavu' and p.place='10th Main' and l.landmark='Srinivasam Nandanam Colony'
					insertSubscriptionSt = con.prepareStatement(insertSubscriptionQuery, Statement.RETURN_GENERATED_KEYS);
					insertSubscriptionSt.setString(1, esubDto.getSubscriptionDate());
					insertSubscriptionSt.setString(2, esubDto.getSubscriptionFromDate());
					insertSubscriptionSt.setString(3, EmployeeSubscriptionDto.status.SUBSCRIBED.toString());
					insertSubscriptionSt.setString(4, esubDto.getEmployeeID());
					insertSubscriptionSt.setString(5, esubDto.getSupervisor1());
					insertSubscriptionSt.setString(6, esubDto.getSupervisor2());
					insertSubscriptionSt.setString(7, esubDto.getApl().getArea().trim());
					insertSubscriptionSt.setString(8, esubDto.getApl().getPlace().trim());
					insertSubscriptionSt.setString(9, esubDto.getApl().getLandMark().trim());
					 
					String text = "select e.id, sup.id, mgr.id, l.id, '%s' , '%s', '%s', 0, now(), l.latitude, l.longitude from employee e, employee sup, employee mgr, area a, place p, landmark l " +
											"where e.personnelNo='%s' and mgr.personnelNo='%s' and sup.personnelNo='%s' and l.place = p.id and p.area = a.id and a.area ='%s' and p.place='%s' and l.landmark='%s' ";
					text = String.format(text, esubDto.getSubscriptionDate(), esubDto.getSubscriptionFromDate(),  "subscribed"
							, esubDto.getEmployeeID(), esubDto.getSupervisor1(), esubDto.getSupervisor2() 
							, esubDto.getApl().getArea().trim(), esubDto.getApl().getPlace().trim(), esubDto.getApl().getLandMark().trim());
					System.out.println(text);
					
					int val = insertSubscriptionSt.executeUpdate();
					returnInt = returnInt + val;
					if(val<=0) {
						System.out.println("insertion failed");
						getMessageList().add("Line :" + rowCount + " Error : Subscription insertion of employee ("+esubDto.getEmployeeID()+")failed :"  );
						rowCount++;
						error = true;
						continue;
					} else {
						System.out.println("insertion success");
						auditRs1 = insertSubscriptionSt.getGeneratedKeys();
						if(auditRs1.next()==true) { 
							System.out.println("Inserted id:"+ auditRs1.getLong(1));
						auditDao.auditLogEntry(auditRs1.getLong(1), AuditLogConstants.SUBCRIPTION_ADMIN_MODULE, Integer.parseInt(changedBy),null , AuditLogConstants.WORKFLOW_STATE_SUB_SUBSCRIBED, AuditLogConstants.AUDIT_LOG_UPLOAD_ACTION, con);
						System.out.println("*********");
//						Employee Site update
						updateEmployeeSt.setString(1, site);
						updateEmployeeSt.setString(2, esubDto.getSupervisor1());
						updateEmployeeSt.setString(3, esubDto.getEmployeeID());
						updateEmployeeSt.executeUpdate();
						 
						}
						DbConnect.closeResultSet(auditRs1);
					}
					System.out.println("Track 05");
					
					}catch(Exception e) {
						getMessageList().add("Line :" + rowCount + " Error : " + e);
						error = true;
					}
					
					rowCount ++;
					
					
				}
				forceUnsubscriptionSt1.executeUpdate();
				System.out.println("Error : " + error);
				if(error==false) {
					con.commit();
				}else {
					con.rollback();
				}
				
				
			} else {
				setMessage("No data");
				error = true;
			}
			
			 
		} catch(Exception e) {
			System.out.println("Exception in uploadSubscription :" + e);
		}
		finally {
			 
			
			DbConnect.closeResultSet(spocRs, managerRs, auditRs1, auditRs ,rsSchedule ,rsSubscription, 
					rsSpoc, rsManager, employeeRs, aplRs);
			DbConnect.closeStatement(checkSpocSt, checkManagerSt,insertSubscriptionSt, checkScheduleAfterSubscriptionSt ,
					forceUnSubscriptionSt, forceUnSubscriptionSt, checkSubscriptionSt, checkSubscriptionSt,checkSubscriptionSt, 
					checkSpocSt, checkManagerSt, employeeCheckSt, aplCheckSt );
			DbConnect.closeConnection(con);
			
		}
		if(error) {
			returnInt =0;
		}
		return returnInt;
	}

	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public ArrayList<String> getMessageList() {
		return messageList;
	}

	public void setMessageList(ArrayList<String> messageList) {
		this.messageList = messageList;
	}

	private String message;
	private ArrayList<String> messageList = new ArrayList<String>();

	//Only for keonics
	public EmployeeDto getShuttleEmpSubscriptionDetails(String empid) {
		DbConnect ob = DbConnect.getInstance();
		Connection con=null;
		Statement st = null;
		ResultSet rs = null;
		EmployeeDto dto= new EmployeeDto();
		try{
			con= ob.connectDB();
			String selectquery="select id,EmployeeFirstName,employeeLastName,PersonnelNo,contactNumber1,EmailAddress,emp_lat,emp_long,address,Gender,state,zip,city from employee where id="+empid;
			System.out.println(selectquery);
			st=con.createStatement();
			rs=st.executeQuery(selectquery);
			if(rs.next()){
				dto.setEmployeeID(rs.getString("id"));
				dto.setLattitude(rs.getString("emp_lat")== null ? "" :rs.getString("emp_lat"));
				dto.setLongitude(rs.getString("emp_long")== null ? "" :rs.getString("emp_long"));
				dto.setEmployeeFirstName(rs.getString("EmployeeFirstName")== null ? "" :rs.getString("EmployeeFirstName"));
				dto.setEmployeeLastName(rs.getString("employeeLastName")== null ? "" :rs.getString("employeeLastName"));
				dto.setPersonnelNo(rs.getString("PersonnelNo")== null ? "" :rs.getString("PersonnelNo"));
				dto.setEmailAddress(rs.getString("EmailAddress")== null ? "" :rs.getString("EmailAddress"));
				dto.setContactNo(rs.getString("contactNumber1")== null ? "" :rs.getString("contactNumber1"));
				dto.setAddress(rs.getString("address")== null ? "" :rs.getString("address"));
				dto.setGender(rs.getString("Gender")== null ? "" :rs.getString("Gender"));
				dto.setState(rs.getString("state")== null ? "" :rs.getString("state"));
				dto.setZip(rs.getString("zip")== null ? "" :rs.getString("zip"));
				dto.setCity(rs.getString("city")== null ? "" :rs.getString("city"));
			}
			
		}catch(Exception e){
			System.out.println("Error in EmployeeSubscriptionDao@getShuttleEmpSubscriptionDetails"+e);
		}finally{
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);
		}
		return dto;
	}

	public int setShuttleEmpSubscriptionDetails(EmployeeDto dto) {
		DbConnect ob = DbConnect.getInstance();
		Connection con=null;
		PreparedStatement pst = null;
		int res=0;
		try{
			String insertqry="UPDATE employee SET EmployeeFirstName=?, employeeLastName=?, loginId=?, PersonnelNo=?, EmailAddress=?,contactNumber1=?, displayname=?, emp_lat=?, emp_long=?, Gender=?,address=?,state=?,zip=?,city=? WHERE id=?";
			con=ob.connectDB();
			pst=con.prepareStatement(insertqry);
			pst.setString(1, dto.getEmployeeFirstName());
			pst.setString(2,dto.getEmployeeLastName());
			pst.setString(3,dto.getEmailAddress());
			pst.setString(4,dto.getPersonnelNo());
			pst.setString(5,dto.getEmailAddress());
			pst.setString(6,dto.getContactNo());
			pst.setString(7,dto.getEmployeeFirstName()+" "+dto.getEmployeeLastName());
			pst.setString(8,dto.getLattitude());
			pst.setString(9,dto.getLongitude());
			pst.setString(15,dto.getEmployeeID());
			pst.setString(10, dto.getGender());
			pst.setString(11, dto.getAddress());
			pst.setString(12, dto.getState());
			pst.setString(13, dto.getZip());
			pst.setString(14, dto.getCity());
			res=pst.executeUpdate();
			
		}catch(Exception e){
			System.out.println("Error in EmployeeSubscriptionDao@setShuttleEmpSubscriptionDetails"+e);
		}finally{
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);
		}
		return res;
	}
	public EmployeeDto getEmpRegisterDetails(String empid) {
		DbConnect ob = DbConnect.getInstance();
		Connection con=null;
		Statement st = null;
		ResultSet rs = null;
		EmployeeDto dto= new EmployeeDto();
		try{
			con= ob.connectDB();
			String selectquery="select e.id,e.EmployeeFirstName,e.employeeLastName,e.PersonnelNo,e.contactNumber1,e.EmailAddress,e.emp_lat,e.emp_long,e.address,e.Gender,e.state,e.city,e.project,e.projectUnit,em.id as managerid,em.displayname as managername from employee e  left outer join employee em on em.id=e.linemanager    where e.id="+empid;
			System.out.println(selectquery);
			st=con.createStatement();
			rs=st.executeQuery(selectquery);
			if(rs.next()){
				dto.setEmployeeID(rs.getString("id"));
			
				dto.setLattitude(rs.getString("emp_lat")== null ? "" :rs.getString("emp_lat"));
				dto.setLongitude(rs.getString("emp_long")== null ? "" :rs.getString("emp_long"));
				
				dto.setEmployeeFirstName(rs.getString("EmployeeFirstName")== null ? "" :rs.getString("EmployeeFirstName"));
				dto.setEmployeeLastName(rs.getString("employeeLastName")== null ? "" :rs.getString("employeeLastName"));
				dto.setPersonnelNo(rs.getString("PersonnelNo")== null ? "" :rs.getString("PersonnelNo"));
				dto.setEmailAddress(rs.getString("EmailAddress")== null ? "" :rs.getString("EmailAddress"));
				dto.setContactNo(rs.getString("contactNumber1")== null ? "" :rs.getString("contactNumber1"));
				dto.setAddress(rs.getString("address")== null ? "" :rs.getString("address"));
				dto.setGender(rs.getString("Gender")== null ? "" :rs.getString("Gender"));
				dto.setState(rs.getString("state")== null ? "" :rs.getString("state"));
				dto.setCity(rs.getString("city")== null ? "" :rs.getString("city"));
			
				dto.setProject(rs.getString("project")== null ? "" :rs.getString("project"));
				dto.setProjectUnit(rs.getString("projectUnit")== null ? "" :rs.getString("projectUnit"));
				
				dto.setLineManager(rs.getString("managerid")== null ? "" :rs.getString("managerid"));
				dto.setManagerName(rs.getString("managername")== null ? "" :rs.getString("managername"));
			}
			
		}catch(Exception e){
			System.out.println("Error in EmployeeSubscriptionDao :"+e);
		}finally{
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);
		}
		return dto;
	}

	public int setEmpRegisterDetails(EmployeeDto dto) {
		DbConnect ob = DbConnect.getInstance();
		Connection con=null;
		PreparedStatement pst = null;
		int res=0;
		try{
			// getting the employee past registration details
			EmployeeDto oldempdto=  getEmpRegisterDetails(dto.getEmployeeID());
			
			
			String insertqry="UPDATE employee SET EmployeeFirstName=?, employeeLastName=?,  PersonnelNo=?, EmailAddress=?,contactNumber1=?, displayname=?, emp_lat=?, emp_long=?, Gender=?,address=?,state=?,city=?,project=? ,projectUnit=?, linemanager = ?,registerStatus='a' , Dateofjoining= if(Dateofjoining is null ,curdate(),Dateofjoining) WHERE id=?";
			con=ob.connectDB();
			pst=con.prepareStatement(insertqry);
			pst.setString(1, dto.getEmployeeFirstName());
			pst.setString(2,dto.getEmployeeLastName());
			pst.setString(3,dto.getPersonnelNo());
			pst.setString(4,dto.getEmailAddress());
			pst.setString(5,dto.getContactNo());
			pst.setString(6,dto.getEmployeeFirstName()+" "+dto.getEmployeeLastName());
			pst.setString(7,dto.getLattitude());
			pst.setString(8,dto.getLongitude());
			pst.setString(9, dto.getGender());
			pst.setString(10, dto.getAddress());
			pst.setString(11, dto.getState());
			pst.setString(12, dto.getCity());
			pst.setString(13, dto.getProject());
			pst.setString(14, dto.getProjectUnit());
			pst.setString(15,dto.getLineManager());
		    pst.setString(16,dto.getEmployeeID());
			res=pst.executeUpdate();
if(SettingsConstant.comp.equalsIgnoreCase("visa"))
{
			
			String mailstring="Dear Transport Team , <br/><br/>"
					+ "Employee "+dto.getEmployeeFirstName()+" "+dto.getEmployeeLastName() + " : " +dto.getPersonnelNo() +" has updated his address from "+oldempdto.getAddress() +" to " +dto.getAddress()+" in registration page.<br/>"
					+"This is system generated mail.Please dont reply.";
					
			 SendMail mail=SendMailFactory.getMailInstance();
				
			 mail.send ("ckorru@visa.com", "EMPLOYEE REGISTRATION NOTIFICATION", mailstring);
			 mail.send ("sunirame@visa.com", "EMPLOYEE REGISTRATION NOTIFICATION", mailstring);
		
			System.out.println(mailstring);

			
			
}			
		}catch(Exception e){
			System.out.println("Error in EmployeeSubscriptionDao :"+e);
		}finally{
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);
		}
		return res;
	}
}
