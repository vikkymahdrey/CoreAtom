/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.agiledge.atom.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.agiledge.atom.commons.OtherFunctions;
import com.agiledge.atom.constants.AuditLogConstants;
import com.agiledge.atom.dbConnect.DbConnect;
import com.agiledge.atom.dto.AuditLogDTO;
import com.agiledge.atom.dto.EmployeeDto;
import com.agiledge.atom.dto.PayrollDto;
import com.agiledge.atom.dto.SiteDto;
import com.agiledge.atom.payroll.dao.PayrollConfigDao;
import com.agiledge.atom.payroll.dao.PayrollConfigDaoImpl;
import com.agiledge.atom.payrollsettings.dto.PayrollConfigDto;
import com.agiledge.atom.transporttype.dto.TransportTypeConfig;



/**
 * 
 * @author 123
 */
public class PayrollDao {

	PayrollConfigDao payrollConfigDao = new PayrollConfigDaoImpl();
	public int makePayroll(String date) {
		DbConnect ob = DbConnect.getInstance();
		Connection con = ob.connectDB();

		int returnInt = 0;
		CallableStatement cs = null;
		try {
			// System.out.println("In makePayroll in PayrollDao date : " +
			// date);
			List<SiteDto> sites = new SiteDao().getSites();
			for(SiteDto siteDto : sites) {
				
				String storedProcedure ="makeOneWayPayrollEntry";
				PayrollConfigDto payrollConfigDto =  payrollConfigDao.getPayrollConfigs(Integer.parseInt(siteDto.getId()), TransportTypeConfig .GENERAL_SHIFT );
				System.out.println("payroll"+payrollConfigDto.toString());
				if(payrollConfigDto!=null)
				{
				System.out.println(" date :"+date+"   siteId"+ siteDto.getId()+"  type"+payrollConfigDto.getPayrollType());
				if(payrollConfigDto.getPayrollType().equals("prorata")) {
					System.out.println("here in prorata");
					storedProcedure="makeProRataPayrollEntry";
				} else if(payrollConfigDto.getPayrollType().equals("flatrate")) { 
					storedProcedure = "makeFlatRatePayrollEntry";
					System.out.println("here in prorata");
				}
			cs = con
					.prepareCall("{call "+storedProcedure+"(?,?,?)}");
			cs.setString(1, date);
			cs.setString(2, siteDto.getId());
			cs.registerOutParameter(3, Types.VARCHAR);
			cs.execute();

			returnInt = Integer.parseInt(cs.getString(3));
				}
			DbConnect.closeStatement(cs);
			}
			
			DbConnect.closeConnection(con);
		} catch (Exception e) {
			DbConnect.closeConnection(con);			
			System.out.println("Error : hererre " + e);
		}

		return returnInt;
	}

	public List<PayrollDto> getPayrollDetails(int month, int year, int site) {
		List<PayrollDto> dtoList = new ArrayList<PayrollDto>();

		int month1 = month;
		int year1 = year;
		if (month == 12) {
			month1 = 1;
			year1 = year1 + 1;

		} else {
			month1++;
		}

		String query1 = " select p.id, e.displayname as  employeename, e.personnelNo, e.isContractEmployee empType, p.payrolldate,p.month, month(p.month) monthPart , p.amount, p.site, p.status, p.updatedBy, p.updatedDate, p.reason from employee e join ( select  p.id, p.empid empid,  p.payrolldate,p.month, p.amount, p.site, p.status, ifnull( upemp.displayName,'') updatedBy,  p.updatedDate , ifnull(p.reason,'') reason  from   payroll p left join Employee upemp on p.updatedBy=upemp.id   ) as p on e.id=p.empid "
				+ " where p.site="+ site + " and p.payrolldate >= '"
				+ year
				+ "-"
				+ month
				+ "-01' and  p.payrolldate < '"
				+ year1
				+ "-"
				+ month1
				+ "-01'"
				+ "  order by empType, e.PersonnelNo, p.payrollDate, p.id  ";
		System.out.println("query :"+ query1);
		DbConnect ob = DbConnect.getInstance();
		Statement st = null;
		ResultSet rs = null;

		 //System.out.println("Query in getPayrollDetails : " + query1);

		Connection con = ob.connectDB();
		// PreparedStatement pst = null;
		try {

			st = con.createStatement();
			rs = st.executeQuery(query1);
			
			while (rs.next()) {
				System.out.println("cccccccccccount");
				PayrollDto dto = new PayrollDto();
				EmployeeDto empdto = new EmployeeDto();
				empdto.setEmployeeFirstName(rs.getString("employeename"));
				empdto.setEmployeeID(rs.getString("personnelNo"));
				empdto.setEmpType(rs.getString("empType"));
				dto.setId(rs.getString("id"));
				dto.setEmployee(empdto);
				dto.setAmount(rs.getDouble("amount"));
				dto.setPayrollDate(rs.getString("payrolldate"));
				dto.setMonth(OtherFunctions.getMonthInString(rs
						.getInt("monthpart")));
				dto.setStatus(rs.getString("status"));
				dto.setUpdatedBy(rs.getString("updatedBy"));
				
				dto.setUpdatedDate(rs.getDate("updatedDate"));
				dto.setReason(rs.getString("reason"));
				dtoList.add(dto);
			}

		} catch (Exception e) {
			System.out.println("get payroll details : " + e);
		}
		finally
		{
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);	
		}
		System.out.println("List size"+dtoList.size());
		return dtoList;

	}
	

	public List<PayrollDto> getLivePayrollDetails(int month, int year, int site) {
		List<PayrollDto> dtoList = new ArrayList<PayrollDto>();

		int month1 = month;
		int year1 = year;
		if (month == 12) {
			month1 = 1;
			year1 = year1 + 1;

		} else {
			month1++;
		}
		String payrollDate =year+"-" + month+ "" + new Date().getDate(); 
System.out.println("PRocedure call ");
		 
		DbConnect ob = DbConnect.getInstance();
		CallableStatement st = null;
		ResultSet rs = null;

		 //System.out.println("Query in getPayrollDetails : " + query1);

		Connection con = ob.connectDB();
		// PreparedStatement pst = null;
		try {
			String storedProcedure ="getOneWayPayrollEntry";
			PayrollConfigDto payrollConfigDto =  payrollConfigDao.getPayrollConfigs(site, TransportTypeConfig .GENERAL_SHIFT );
			if(payrollConfigDto.getPayrollType().equals("prorata")) {
				storedProcedure="getProRataPayrollEntry";
			} else if(payrollConfigDto.getPayrollType().equals("flatrate")) { 
				storedProcedure = "getFlatRatePayrollEntry";
			}
System.out.println("{call "+storedProcedure+"(?,?,?)}");
			st = con.prepareCall("{call "+storedProcedure+"(?,?,?)}");
			st.setString(1, "2014-04-22");
			st.setInt(2, site);
			int val=0;
			st.registerOutParameter(3, Types.INTEGER);
			 
			 st.execute();
			 val = st.getInt(3);
			 System.out.println("Value : "+ val);
			rs = st.getResultSet();			
			
			while (rs.next()) {
				System.out.println("* ");
				PayrollDto dto = new PayrollDto();
				EmployeeDto empdto = new EmployeeDto();
				empdto.setEmployeeFirstName(rs.getString("employeename"));
				empdto.setEmployeeID(rs.getString("personnelNo"));
				empdto.setEmpType(rs.getString("empType"));
				 
				dto.setId("");
				dto.setEmployee(empdto);
				dto.setAmount(rs.getDouble("amount"));
				dto.setPayrollDate(rs.getString("payrolldate"));
				dto.setMonth(OtherFunctions.getMonthInString(rs
						.getInt("monthpart")));
				dto.setStatus(rs.getString("status"));
				dto.setUpdatedBy( "");
				
				 
				dto.setReason( "Live on " + payrollDate);
				dtoList.add(dto);
			}
		} catch (Exception e) {
			System.out.println("get payroll details : " + e);
		}
		finally
		{
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);	
		}
		return dtoList;

	}
	
	
	
	 



	/* update amount in the payroll table
	 * 
	 */
	public int updatePayrollAmount(PayrollDto dto) {
		// TODO Auto-generated method stub
		DbConnect ob = DbConnect.getInstance();
		Connection con = ob.connectDB();
		ResultSet rs=null;
		ResultSet ars=null;
		int returnInt = 0;
		PreparedStatement ps1=null;
		PreparedStatement ps2 = null;
		try {
			// System.out.println("In makePayroll in PayrollDao date : " +
			// date);
			System.out.println("Query:  select p.*, e.displayname, e.PersonnelNo from payroll p join employee e on p.updatedBy=e.id where p.id=" + dto.getId());
			ps1=con.prepareStatement(" select p.*, e.displayname, e.PersonnelNo from payroll p join employee e on p.empid=e.id where p.id=?");
			ps1.setString(1, dto.getId());
			rs=ps1.executeQuery();
			if(dto.getAmount()<0)
			{
				dto.setStatus("Substracted");
			}else{
				dto.setStatus("Added");
			}
			if(rs.next()) {
				 dto.setEmployeeId(rs.getString("empid"));
				 dto.setSubscriptionId(rs.getString("subscriptionID"));
				 dto.setMonth(rs.getString("month"));
				 dto.setPayrollDate(rs.getString("payrollDate"));
				 dto.setSite(rs.getString("site"));
				 dto.setStatus("later");
				 EmployeeDto empDto = new EmployeeDto();
				 empDto.setDisplayName(rs.getString("displayName"));
				 empDto.setPersonnelNo(rs.getString("PersonnelNo"));
				 dto.setEmployee(empDto);
			
			ps2= con.prepareStatement("insert into payroll (subscriptionID, empid, payrollDate, month, amount, status, site, updatedBy, reason) values (?,?,?,?,?,?,?,?,?) ",Statement.RETURN_GENERATED_KEYS);
					
					
			ps2.setString(1, dto.getSubscriptionId());
			ps2.setString(2, dto.getEmployeeId());
			ps2.setString(3, dto.getPayrollDate());
			ps2.setString(4, dto.getMonth());
			ps2.setDouble(5, dto.getAmount());
			ps2.setString(6, dto.getStatus());
			ps2.setString(7, dto.getSite());
			ps2.setString(8, dto.getUpdatedBy());
			ps2.setString(9, dto.getReason());
			
			returnInt = ps2.executeUpdate();
			if(returnInt>0) {
				ars= ps2.getGeneratedKeys();
				if(ars.next())
				{
					long id=ars.getLong(1);
					dto.setId(String.valueOf(id));
					if(dto.getAmount()<0)
					{
						auditLogEntryforinsertlandmark(id, AuditLogConstants.PAYROLL_MODULE, Integer.parseInt( dto.getUpdatedBy()), AuditLogConstants.WORK_FLOW_STATE_SUBSTRACT_PAYROLL, AuditLogConstants.WORK_FLOW_STATE_SUBSTRACT_PAYROLL, AuditLogConstants.WORK_FLOW_STATE_SUBSTRACT_PAYROLL);
					} else {
						auditLogEntryforinsertlandmark(id, AuditLogConstants.PAYROLL_MODULE,  Integer.parseInt( dto.getUpdatedBy()), AuditLogConstants.WORK_FLOW_STATE_ADD_PAYROLL, AuditLogConstants.WORK_FLOW_STATE_ADD_PAYROLL, AuditLogConstants.WORK_FLOW_STATE_ADD_PAYROLL);
					}
					
				}
			}
			} 

			 
		} catch (Exception e) {
			 			
			System.out.println("Error : " + e);
		} finally {
			DbConnect.closeStatement(ps1,ps2);
			DbConnect.closeResultSet(rs,ars);
			DbConnect.closeConnection(con);
		}
		

		return returnInt;

	}
	
	/*
	 * for audit log entries
	 */
	private void auditLogEntryforinsertlandmark(long autoincNumber, String Module,
			int changedBy, String preworkflowState,
			String curworkflowState, String auditLogAction) {
		AuditLogDTO auditLog = new AuditLogDTO();

		auditLog.setRelatedNodeId((int)autoincNumber);
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
