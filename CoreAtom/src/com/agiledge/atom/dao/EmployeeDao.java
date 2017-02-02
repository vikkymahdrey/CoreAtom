/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.agiledge.atom.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.agiledge.atom.commons.OtherFunctions;
import com.agiledge.atom.commons.PasswordGenerator;
import com.agiledge.atom.constants.AuditLogConstants;
import com.agiledge.atom.constants.SettingsConstant;
import com.agiledge.atom.dbConnect.DbConnect;
import com.agiledge.atom.dto.AuditLogDTO;
import com.agiledge.atom.dto.EmployeeDto;
import com.agiledge.atom.dto.EmployeeSubscriptionDto;
import com.agiledge.atom.mail.SendMail;
import com.agiledge.atom.mail.SendMailFactory;
import com.agiledge.atom.service.EmployeeService;
import com.agiledge.atom.service.EmployeeSubscriptionService;
import com.agiledge.atom.service.SettingsService;
import com.agiledge.atom.task.dao.GetEmps;
import com.agiledge.atom.usermanagement.dao.UserManagementDao;
import com.agiledge.atom.usermanagement.dto.UserManagementDto;

/**
 * 
 * @author 123
 */
public class EmployeeDao {

	private String message = "";
	static final int EMPIDINDEX = 5;
	final String COUNTRYCODE = "IN";
	final String ACTIVE = "1";
	ArrayList<EmployeeDto> employeeActual;
	EmployeeDto employeedtobj = null;
	String allEmployeeLoginIdString = "";
	String allEmployeeAddressString = "";
	DbConnect ob = null;
	Connection con = null;
	ArrayList<EmployeeDto> employeeExist = null;

	public void UpdateEmpsDBConnet() throws Exception {
		DbConnect ob = DbConnect.getInstance();
		con = ob.connectDB();
	}

	public void setEmpLists(ArrayList<EmployeeDto> employeeActual,
			ArrayList<EmployeeDto> employeeExist) throws Exception {
		this.employeeActual = employeeActual;
		this.employeeExist = employeeExist;
		System.out.println("WORKING THE UTLITY 1");
		System.out.println("EMps Size" + employeeActual.size());
		try {
			if (employeeActual != null && employeeActual.size() > 0) {
				setActiveFalse();
				System.out.println("WORKING THE UTLITY 2");
				updateEmpoyee();
				System.out.println("WORKING THE UTLITY 3 ");
				updateExitedEmployee();
				System.out.println("WORKING THE UTLITY 4");
				updateLineManager();
			}

			// setCommit();
			setAutoCommit(true);
			System.out.println("WORKING THE UTLITY 5");
			autoChangeRole();
			// closeConnection();

		} catch (Exception e) {
			System.out.println("Error In Update");
			e.printStackTrace();
			setRollBack();
			setAutoCommit(true);
			// closeConnection();
			throw (e);
		}

	}

	public void setAutoCommit(boolean val) throws SQLException {
		con.setAutoCommit(val);
	}

	public void setRollBack() throws SQLException {
		con.rollback();
	}

	public void setCommit() throws SQLException {
		con.commit();
	}

	public void closeConnection() throws SQLException {
		DbConnect.closeConnection(con);
	}

	public void setActiveFalse() throws SQLException {

		PreparedStatement pst = con
				.prepareStatement("Update employee set active=? where externaluser is null or externaluser !='yes'");
		pst.setInt(1, 0);
		pst.executeUpdate();
		con.commit();
	}

	public void updateEmployeeDatilsAddressChangeStatus(EmployeeDto dto,
			String LoginId) throws Exception {
		PreparedStatement pst = con
				.prepareStatement("UPDATE employee SET EmployeeFirstName = ?,EmployeeMiddleName =?,"
						+ "employeeLastName =?,Gender=?,EmailAddress=?,HomeCountry=?,LineManager=?,StaffManager=?,"
						+ "ClientServiceManager=?,CareerLevel=?,CareerPathwayDesc=?,BusinessUnitCode=?,"
						+ "BusinessUnitDescription=?,Deptno=?,DeptName=?,OperationCode=?,OperationDescription=?,"
						+ "Pathways=?,Dateofjoining=?,Active=?,address=?,addresschangestatus=?,city=?,isContractEmployee=?,displayname=?,project=?,projectUnit=?,externalUser=?,authtype=?,designationName=? WHERE loginId=?");
		pst.setString(1, dto.getEmployeeFirstName());
		pst.setString(2, dto.getEmployeeMiddleName());
		pst.setString(3, dto.getEmployeeLastName());
		pst.setString(4, dto.getGender());
		pst.setString(5, dto.getEmailAddress());
		pst.setString(6, dto.getHomeCountry());
		pst.setString(7, dto.getLineManager());
		pst.setString(8, dto.getStaffManager());
		pst.setString(9, dto.getClientServiceManager());
		pst.setString(10, dto.getCareerLevel());
		pst.setString(11, dto.getCareerPathwayDesc());
		pst.setString(12, dto.getBusinessUnitCode());
		pst.setString(13, dto.getBusinessUnitDescription());
		pst.setString(14, dto.getDeptno());
		pst.setString(15, dto.getDeptName());
		pst.setString(16, dto.getOperationCode());
		pst.setString(17, dto.getOperationDescription());
		pst.setString(18, dto.getPathways());
		pst.setString(19, dto.getDateOfJoining());
		pst.setString(20, ACTIVE);
		pst.setString(21, dto.getAddress());
		pst.setString(22, ACTIVE);
		pst.setString(23, dto.getCity());
		pst.setString(24, "on roll");
		pst.setString(25, dto.getDisplayName());
		pst.setString(26, dto.getProjectid());
		pst.setString(27, dto.getProjectUnit());
		pst.setString(28, "No");
		pst.setString(29, "w");	
		pst.setString(30, dto.getDesignationName());	
		pst.setString(31, LoginId);
		pst.executeUpdate();
		con.commit();
		/*
		 * try {
		 * 
		 * EmployeeDto empDto = getEmployeeAccurate(getEmployeeID(LoginId));
		 * String message = "Hi " + empDto.getEmployeeFirstName() + " " +
		 * empDto.getEmployeeLastName() +
		 * "since your address has been changed. Please update your address in "
		 * + "atom by clicking the link below  withinn 24 hours. <br/> " +
		 * "<a href='#' > Click Here </a>"; SendMail mail = new SendMail();
		 * mail.send(LoginId, ACTIVE, ACTIVE);
		 * 
		 * } catch (Exception e) { System.out.println("Error " + e); }
		 * pst.executeUpdate();
		 */
	}

	public void updateEmployeeDatils(EmployeeDto dto, String LoginId)
			throws Exception {
		PreparedStatement pst = con
				.prepareStatement("UPDATE employee SET EmployeeFirstName = ?,EmployeeMiddleName =?,"
						+ "employeeLastName =?,Gender=?,EmailAddress=?,HomeCountry=?,LineManager=?,StaffManager=?,"
						+ "ClientServiceManager=?,CareerLevel=?,CareerPathwayDesc=?,BusinessUnitCode=?,"
						+ "BusinessUnitDescription=?,Deptno=?,DeptName=?,OperationCode=?,OperationDescription=?,"
						+ "Pathways=?,Dateofjoining=?,Active=?,city=?,isContractEmployee=?,displayname=?,project=?,projectUnit=?,externalUser=?,authtype=?,designationName=? WHERE loginId=?");
		pst.setString(1, dto.getEmployeeFirstName());
		pst.setString(2, dto.getEmployeeMiddleName());
		pst.setString(3, dto.getEmployeeLastName());
		pst.setString(4, dto.getGender());
		pst.setString(5, dto.getEmailAddress());
		pst.setString(6, dto.getHomeCountry());
		pst.setString(7, dto.getLineManager());
		pst.setString(8, dto.getStaffManager());
		pst.setString(9, dto.getClientServiceManager());
		pst.setString(10, dto.getCareerLevel());
		pst.setString(11, dto.getCareerPathwayDesc());
		pst.setString(12, dto.getBusinessUnitCode());
		pst.setString(13, dto.getBusinessUnitDescription());
		pst.setString(14, dto.getDeptno());
		pst.setString(15, dto.getDeptName());
		pst.setString(16, dto.getOperationCode());
		pst.setString(17, dto.getOperationDescription());
		pst.setString(18, dto.getPathways());
		pst.setString(19, dto.getDateOfJoining());
		pst.setString(20, ACTIVE);
		pst.setString(21, dto.getCity());
		pst.setString(22, "on roll");
		pst.setString(23, dto.getDisplayName());
		pst.setString(24, dto.getProjectid());
		pst.setString(25, dto.getProjectUnit());
		pst.setString(26, "No");
		pst.setString(27, "w");
		pst.setString(28, dto.getDesignationName());
		pst.setString(29, LoginId);
		pst.executeUpdate();
		con.commit();
	}

	public void insertEmpoyee(EmployeeDto emplyeedto) {
		// System.out.println("INSERTInnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnn");
		// System.out.println("");
		try {
			PreparedStatement pst = con
					.prepareStatement("insert into employee(EmployeeFirstName,EmployeeMiddleName,EmployeeLastName,LoginId,Gender,PersonnelNo,EmailAddress,contactNumber1,"
							+ "HomeCountry,LineManager,StaffManager,ClientServiceManager,CareerLevel,CareerPathwayDesc,BusinessUnitCode,BusinessUnitDescription,"
							+ "Deptno,DeptName,OperationCode,OperationDescription,Pathways,Dateofjoining,Active,address,city,isContractEmployee,displayname,projectUnit,designationName,state,site,project) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
			pst.setString(1, emplyeedto.getEmployeeFirstName());
			pst.setString(2, emplyeedto.getEmployeeMiddleName());
			pst.setString(3, emplyeedto.getEmployeeLastName());
			pst.setString(4, emplyeedto.getLoginId());
			pst.setString(5, emplyeedto.getGender());
			pst.setString(6, emplyeedto.getPersonnelNo());
			pst.setString(7, emplyeedto.getEmailAddress());
			pst.setString(8, emplyeedto.getContactNo());
			pst.setString(9, emplyeedto.getHomeCountry());
			pst.setString(10, emplyeedto.getLineManager());
			pst.setString(11, emplyeedto.getStaffManager());
			pst.setString(12, emplyeedto.getClientServiceManager());
			pst.setString(13, emplyeedto.getCareerLevel());
			pst.setString(14, emplyeedto.getCareerPathwayDesc());
			pst.setString(15, emplyeedto.getBusinessUnitCode());
			pst.setString(16, emplyeedto.getBusinessUnitDescription());
			pst.setString(17, emplyeedto.getDeptno());
			pst.setString(18, emplyeedto.getDeptName());
			pst.setString(19, emplyeedto.getOperationCode());
			pst.setString(20, emplyeedto.getOperationDescription());
			pst.setString(21, emplyeedto.getPathways());
			pst.setString(22, emplyeedto.getDateOfJoining());
			pst.setString(23, ACTIVE);
			pst.setString(24, emplyeedto.getAddress());
			pst.setString(25, emplyeedto.getCity());
			pst.setString(26, "on Roll");
			pst.setString(27, emplyeedto.getDisplayName());
			pst.setString(28, emplyeedto.getProjectUnit());
			pst.setString(29, emplyeedto.getDesignationName());
			pst.setString(30, emplyeedto.getState());
			pst.setString(31, "1");
			pst.setString(32, emplyeedto.getProjectid());
			pst.executeUpdate();
			con.commit();
		} catch (Exception e) {
			System.out.println("error" + emplyeedto.getPersonnelNo() + e);
		}
	}

	/**
	 * @throws SQLException
	 */
	/**
	 * @throws SQLException
	 */
	public void updateEmpoyee() throws Exception {
		boolean flag = false;
		// System.out.println("In Update Employee");
		// System.out.println("actual emp size" + employeeActual.size());
		// System.out.println("Existing Emp size" + employeeExist.size());

		for (EmployeeDto employeeActualDto : employeeActual) {
			// System.out.println("Actual Login Id"+employeeActualDto.getLoginId());
			flag = false;
			for (EmployeeDto employeeExistDto : employeeExist) {
				// System.out.println(employeeActualDto.getPersonnelNo()+" :ID:  "+employeeExistDto.getPersonnelNo());
				if (employeeActualDto.getPersonnelNo().equals(
						employeeExistDto.getPersonnelNo())) {
					// System.out.println("Existing Login Id"+employeeExistDto.getPersonnelNo()+"\nExisted EMP Address"+employeeExistDto.getAddress()+"Actuals"+employeeActualDto.getPersonnelNo());
					if (employeeActualDto.getAddress().equals(
							employeeExistDto.getAddress())) {
						updateEmployeeDatils(employeeActualDto,
								employeeExistDto.getLoginId());
					} else {
						updateEmployeeDatilsAddressChangeStatus(
								employeeActualDto,
								employeeExistDto.getLoginId());
						// ====>

						sendMailToAddressChanged(employeeExistDto);

					}
					flag = true;
					break;
				}

			}
			if (!flag) {
				// System.out.println("Inserting login ID"+employeeActualDto.getLoginId());
				insertEmpoyee(employeeActualDto);
			}
		}
	}

	public int sendMailToAddressChanged(EmployeeDto emailEmployee) {
		int returnInt = 0;

		if (emailEmployee != null
				&& new EmployeeSubscriptionService()
						.isPendingOrSubscribed(emailEmployee.getEmployeeID())) {

			String message = SettingsService
					.getAddressChangedEmployeesMessage(emailEmployee);
			String title = SettingsService
					.getAddressChangedEmployeesMessageTitle();
			SendMail mail = SendMailFactory.getMailInstance();
			try {
				ArrayList<EmployeeDto> tms = new EmployeeDao()
						.getEmployeesByRole("ta");
				String bccs[] = new String[0];
				bccs = OtherFunctions.appendArray(bccs,
						new EmployeeService().getEmails(tms));
				for (EmployeeDto tm : tms) {
					bccs = OtherFunctions
							.appendArray(bccs, new EmployeeService()
									.getEmails(new EmployeeDao()
											.getDelegatedEmployees(tm
													.getEmployeeID())));
				}
				mail.setBccs(bccs);
				mail.send(emailEmployee.getEmailAddress(), title, message);
				returnInt = 1;
			} catch (Exception e) {
				System.out.println("Error In mail sending : " + e);
			}

		}

		return returnInt;

	}

	public ArrayList<EmployeeDto> getAllEmps() {
		System.out.println("get all emps started");
		Statement st1 = null;
		ResultSet rs1 = null;
		ArrayList<EmployeeDto> existingEmployee = null;
		try {
			rs1 = null;
			st1 = null;
			existingEmployee = new ArrayList<EmployeeDto>();
			st1 = con.createStatement();
			rs1 = st1
					.executeQuery("select * from employee ");
			while (rs1.next()) {
				employeedtobj = new EmployeeDto();
				employeedtobj.setEmployeeID(rs1.getString("id"));
				employeedtobj.setEmployeeFirstName(""
						+ rs1.getString("EmployeeFirstName"));
				employeedtobj.setEmployeeMiddleName(""
						+ rs1.getString("EmployeeMiddleName"));
				employeedtobj.setEmployeeLastName(""
						+ rs1.getString("EmployeeLastName"));
				employeedtobj.setLoginId(rs1.getString("LoginId"));
				employeedtobj.setGender(rs1.getString("Gender"));
				employeedtobj.setPersonnelNo(rs1.getString("PersonnelNo"));
				employeedtobj.setEmailAddress(rs1.getString("EmailAddress"));
				employeedtobj.setHomeCountry(rs1.getString("HomeCountry"));
				employeedtobj.setLineManager(rs1.getString("LineManager"));
				employeedtobj.setStaffManager(rs1.getString("StaffManager"));
				employeedtobj.setClientServiceManager(""
						+ rs1.getString("ClientServiceManager"));
				employeedtobj.setCareerLevel(rs1.getString("CareerLevel"));
				employeedtobj.setCareerPathwayDesc(""
						+ rs1.getString("CareerPathwayDesc"));
				employeedtobj.setBusinessUnitCode(""
						+ rs1.getString("BusinessUnitCode"));
				employeedtobj.setBusinessUnitDescription(""
						+ rs1.getString("BusinessUnitDescription"));
				employeedtobj.setDeptno(rs1.getString("Deptno"));
				employeedtobj.setDeptName(rs1.getString("DeptName"));
				employeedtobj.setOperationCode(""
						+ rs1.getString("OperationCode"));
				employeedtobj.setOperationDescription(""
						+ rs1.getString("OperationDescription"));
				employeedtobj.setPathways(rs1.getString("Pathways"));
				employeedtobj.setDateOfJoining(""
						+ rs1.getString("Dateofjoining"));
				employeedtobj.setActive(rs1.getString("Active"));
				//System.out.println("In dao" + rs1.getString("Active"));
				employeedtobj.setAddress(rs1.getString("address"));
				employeedtobj.setCity(rs1.getString("City"));
				employeedtobj.setContactNo(rs1.getString("contactNumber1"));
				employeedtobj.setAddressChangeDate(rs1
						.getString("addressChangeDate"));
				employeedtobj.setEmpType(rs1.getString("isContractEmployee"));
				employeedtobj.setDisplayName(rs1.getString("displayname"));
				existingEmployee.add(employeedtobj);
			}
		} catch (Exception e) {
			System.out.println("errro" + e);
		}
		System.out.println("get all emps ended");
		return existingEmployee;
	}

	public void updateExitedEmployee() throws Exception {
		PreparedStatement pst = null;
		PreparedStatement pst1 = null;
		PreparedStatement pst2 = null;
		ResultSet rs = null;
		Statement st = null;
		long empId;
		try {
			st = con.createStatement();
			String query = "insert into employee_unsubscription(subscriptionID,unsubscriptionDate,fromDate) select subscriptionID, now(),curdate() from employee_subscription where empID=?  and (subscriptionStatus='subscribed' or subscriptionStatus='pending')";
			pst = con.prepareStatement(query);
			rs = st.executeQuery("select * from employee where Active=0");
			while (rs.next()) {
				empId = rs.getLong(1);
				pst.setLong(1, empId);
				pst.executeUpdate();
			}
		} catch (Exception e) {
			System.out.println("errro in exited emp isssue" + e);
		} finally {
			DbConnect.closeStatement(pst, pst1, pst2);
		}
	}

	public void updateLineManager() {
		PreparedStatement pst = null;
		PreparedStatement pst1 = null;
		PreparedStatement pst2 = null;
		ResultSet rs=null;
		String query = "update employee e,employee e1 set e.linemanager=e1.id where e.lineManager=e1.personnelno";
		try {
			pst = con.prepareStatement(query);
			pst.executeUpdate();
			con.commit();
			pst=con.prepareStatement("Select employee.id from employee_subscription,employee where employee_subscription.empid=employee.id and employee_subscription.supervisor!=employee.linemanager and  employee.linemanager is not null and employee.linemanager!=''");
			rs=pst.executeQuery();
			while(rs.next())
			{		
			query = "update   employee_subscription,employee set employee_subscription.supervisor =employee.linemanager  where employee_subscription.empid=employee.id and employee_subscription.subscriptionstatus in ('pending','subscribed') and  employee.id="+rs.getInt(1);
			pst1 = con.prepareStatement(query);
			int retVal=pst1.executeUpdate();
			if(retVal>0)
			{
				query = "update   employee_subscription,employee set employee_subscription.spoc =employee.linemanager  where employee_subscription.empid=employee.id and employee_subscription.subscriptionstatus in ('pending','subscribed') and  employee.id="+rs.getInt(1);
				pst1 = con.prepareStatement(query);
				pst1.executeUpdate();	
			}
		}
			con.commit();
		} catch (Exception e) {
			System.out.println("error in exited emp isssue" + e);
		} finally {

			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(pst, pst1, pst2);
		}
	}

	public String getEmployeeID(String loginID) {
		String employeeID = "";
		String query = "select id from employee where employee.loginId='"
				+ loginID + "' ";

		DbConnect ob = DbConnect.getInstance();
		Statement st = null;
		ResultSet rs = null;

		Connection con = ob.connectDB();
		// PreparedStatement pst = null;
		try {
			// Connection pooling implementation

			// replace ob.connectDB() with Connection con=ob.connectDB();
			// replace ob.con with con

			st = con.createStatement();
			rs = st.executeQuery(query);
			if (rs.next()) {
				employeeID = rs.getString("id");
			}
		} catch (Exception ex) {
			DbConnect.closeConnection(con);
			System.out.println("Error in EmployeeDao getEmployeeID() ");
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);
		}
		return employeeID;
	}

	public boolean hasPrivilegeToChangeAddress(String empid) {
		boolean privilege = false;
		String query = "select id from employee where id=" + empid
				+ " and DATE_ADD(addressChangeDate,interval 3 days)<curdate()";

		DbConnect ob = DbConnect.getInstance();
		Statement st = null;
		ResultSet rs = null;

		Connection con = ob.connectDB();
		// PreparedStatement pst = null;
		try {
			// Connection pooling implementation

			// replace ob.connectDB() with Connection con=ob.connectDB();
			// replace ob.con with con

			st = con.createStatement();
			rs = st.executeQuery(query);
			if (rs.next()) {
				privilege = true;
			}
		} catch (Exception ex) {
			System.out.println("Error in EmployeeDao getEmployeeID() ");
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);
		}
		return privilege;
	}

	// get all employees

	/*
	 * public List<EmployeeDto> getEmployeddddes() { List<EmployeeDto>
	 * employeeList = new ArrayList<EmployeeDto>(); DbConnect ob =
	 * DbConnect.getInstance(); Statement st = null; ResultSet rs = null;
	 * 
	 * Connection con= ob.connectDB(); // PreparedStatement pst = null; try {
	 * //Connection pooling implementation
	 * 
	 * // replace ob.connectDB() with Connection con=ob.connectDB(); // replace
	 * ob.con with con
	 * 
	 * st = con.createStatement(); String query = "select * from employee"; rs =
	 * st.executeQuery(query); while(rs.next()) { EmployeeDto dto = new
	 * EmployeeDto(); dto.setEmployeeID(rs.getString("id"));
	 * dto.setEmployeeFirstName(rs.getString("employee_name"));
	 * //dto.setLastName(rs.getString("employeeLastName"));
	 * dto.setEmployeeLastName(rs.getString(3)); employeeList.add(dto);
	 * 
	 * employeeList.add(dto); }
	 * 
	 * DbConnect.closeResultSet(rs); DbConnect.closeStatement(st);
	 * DbConnect.closeConnection(con); }catch(Exception e) {
	 * DbConnect.closeConnection(con);
	 * System.out.println("Error in DAO-> APL getLandMarks  : " + e);
	 * 
	 * } return employeeList; }
	 */
	public List<EmployeeDto> getEmployee(String employeeName) {
		List<EmployeeDto> empList = new ArrayList<EmployeeDto>();
		DbConnect ob = DbConnect.getInstance();

		String query = "select employee.id,EmployeeFirstName,personnelNo,deptName, EmployeeLastName from employee where employeeFirstName like ?  or employeeLastName like ? or personnelNo like ?";

		Connection con = ob.connectDB();
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			stmt = con.prepareStatement(query);

			stmt.setString(1, "%" + employeeName + "%");
			stmt.setString(2, "%" + employeeName + "%");
			stmt.setString(3, "%" + employeeName + "%");

			rs = stmt.executeQuery();

			while (rs.next()) {
				EmployeeDto dto = new EmployeeDto();
				dto.setEmployeeID(rs.getString("id"));
				dto.setEmployeeFirstName(rs.getString("EmployeeFirstname"));
				dto.setEmployeeLastName(rs.getString("EmployeeLastName"));
				dto.setPersonnelNo(rs.getString("PersonnelNo"));
				dto.setDeptName(rs.getString("DeptName"));

				empList.add(dto);

				// System.out.println("ID : " + dto.getEmployeeFirstName());
			}
		} catch (Exception e) {
			System.out.println("Exception in EmployeeDao getEmployee " + e);

		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(stmt);
			DbConnect.closeConnection(con);
		}

		return empList;
	}

	public EmployeeDto getEmployeeAccurate(String empid) {
		EmployeeDto dto = new EmployeeDto();
		DbConnect ob = DbConnect.getInstance();

		String query = "select e1.id as managerid,e1.displayname as managerName,e2.id as spocId,e2.displayname as spocName, e.contactNumber1,e.Gender, e.id, e.usertype, r.id roleId, e.displayName, e.EmployeeFirstName, e.personnelNo, e.loginId, e.deptName, e.employeeLastName, e.EmailAddress,e.password,e.site,e.projectUnit,e.registerStatus,e.designationName from roles r, employee e left join spoc_child sc on e.id=sc.employee_id left join spoc_parent sp on  sc.spoc_id=sp.id left join employee e2 on sp.employee_id=e2.id left join employee e1 on e.linemanager=e1.id  where e.usertype=r.usertype  and  e.id=? ";
		// System.out.println(query+empid);
		Connection con = ob.connectDB();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = con.prepareStatement(query);
			stmt.setString(1, empid);
			rs = stmt.executeQuery();
			if (rs.next()) {
				dto.setEmployeeID(rs.getString("id"));
				dto.setDisplayName(rs.getString("displayName"));
				dto.setEmployeeFirstName(rs.getString("employeeFirstname"));
				dto.setEmployeeLastName(rs.getString("employeeLastName"));
				dto.setGender(rs.getString("Gender"));
				dto.setPersonnelNo(rs.getString("personnelNo"));
				dto.setDeptName(rs.getString("deptName"));
				dto.setEmailAddress(rs.getString("EmailAddress"));
				dto.setLoginId(rs.getString("loginId"));
				dto.setUserType(rs.getString("usertype"));
				dto.setPassword(rs.getString("password"));
				dto.setSite(rs.getString("site"));
				dto.setRoleId(rs.getInt("roleId"));
				dto.setProjectUnit(rs.getString("projectUnit"));
				dto.setRegisterStatus(rs.getString("registerStatus"));
				dto.setLineManager(rs.getString("managerid"));
				dto.setManagerName(rs.getString("managerName"));
				dto.setSpoc_id(rs.getString("spocId"));
				dto.setSpocName(rs.getString("spocName"));
				dto.setDesignationName(rs.getString("designationName"));
				dto.setContactNo(rs.getString("contactNumber1"));
				System.out.println("CONTACT NUMBER 1 : " + dto.getContactNo());
			}
		} catch (Exception e) {
			System.out.println("Exception in EmployeeDao getEmployee " + e);

		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(stmt);
			DbConnect.closeConnection(con);
		}
		System.out.println("Is registered" + dto.getRegisterStatus());
		return dto;
	}

	public List<EmployeeDto> getAllUnSubscribedEmployees(String currentDate) {
		List<EmployeeDto> empList = new ArrayList<EmployeeDto>();
		DbConnect ob = DbConnect.getInstance();

		String query = "select e.id, e.employeeFirstName + ' ' + e.employeeLastName employeeName ,e.EmailAddress, s.subscriptionStatus, us.fromDate from employee e join employee_subscription s  on e.id=s.empid join employee_unsubscription us on s.subscriptionID=us.subscriptionID where s.subscriptionStatus=? and us.fromDate=?";

		Connection con = ob.connectDB();
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			stmt = con.prepareStatement(query);
			stmt.setString(1,
					EmployeeSubscriptionDto.status.UNSUBSCRIBED.toString());
			stmt.setString(2, currentDate);

			rs = stmt.executeQuery();

			while (rs.next()) {
				EmployeeDto dto = new EmployeeDto();
				dto.setEmployeeID(rs.getString("id"));
				dto.setEmailAddress(rs.getString("EmailAddress"));
				dto.setEmployeeFirstName(rs.getString("employeeName"));

				empList.add(dto);

				// System.out.println("ID : " + dto.getEmailAddress());
			}

		} catch (Exception e) {
			System.out.println("Exception in EmployeeDao getEmployee " + e);

		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(stmt);
			DbConnect.closeConnection(con);
		}

		return empList;
	}

	public List<EmployeeDto> getAllSubscribedEmployees(String currentDate) {
		List<EmployeeDto> empList = new ArrayList<EmployeeDto>();
		DbConnect ob = DbConnect.getInstance();

		String query = "select e.id,e.EmailAddress, e.EmployeeFirstName, e.EmployeeLastName, subscriptionStatus, fromDate from employee e join employee_subscription s on e.id=s.empid where s.fromDate=? and  s.subscriptionStatus=?  ";

		Connection con = ob.connectDB();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = con.prepareStatement(query);
			// Statement stmt = con.createStatement();
			stmt.setString(1, currentDate);
			stmt.setString(2,
					EmployeeSubscriptionDto.status.SUBSCRIBED.toString());

			rs = stmt.executeQuery();

			while (rs.next()) {
				EmployeeDto dto = new EmployeeDto();
				dto.setEmployeeID(rs.getString("id"));
				dto.setEmailAddress(rs.getString("EmailAddress"));
				dto.setEmployeeFirstName(rs.getString("EmployeeFirstName"));
				dto.setEmployeeLastName(rs.getString("EmployeeLastName"));

				empList.add(dto);

				// System.out.println("ID : " + dto.getEmailAddress());
			}

		} catch (Exception e) {
			System.out.println("Exception in EmployeeDao getEmployee " + e);

		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(stmt);
			DbConnect.closeConnection(con);
		}

		return empList;
	}

	public List<EmployeeDto> searchEmployeeVendor(EmployeeDto searchEmployee) {
		List<EmployeeDto> dtoList = new ArrayList<EmployeeDto>();

		DbConnect ob = DbConnect.getInstance();
		String query = "select id, Name,address,email from vendor where ";
		String subQuery = "";

		if (searchEmployee.getEmployeeFirstName() != null
				&& !searchEmployee.getEmployeeFirstName().equals("")) {
			subQuery = subQuery + " name like ? ";
		}
		query += subQuery;
		Connection con = ob.connectDB();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = con.prepareStatement(query);
			stmt.setString(1, "%" + searchEmployee.getEmployeeFirstName() + "%");
			rs = stmt.executeQuery();
			while (rs.next()) {
				EmployeeDto dto = new EmployeeDto();
				dto.setEmployeeID(rs.getString("id"));
				dto.setPersonnelNo("");
				dto.setDisplayName(rs.getString("name"));
				dto.setEmployeeFirstName(rs.getString("name"));
				dto.setEmailAddress(rs.getString("email"));
				dtoList.add(dto);
			}

		} catch (Exception e) {
			System.out.println("Exception in EmployeeDao SearchEmployee " + e);

		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(stmt);
			DbConnect.closeConnection(con);
		}

		return dtoList;
	}

	public List<EmployeeDto> searchEmployeeSecurity(EmployeeDto searchEmployee) {
		List<EmployeeDto> dtoList = new ArrayList<EmployeeDto>();

		DbConnect ob = DbConnect.getInstance();
		String query = "select id, Name,escortClock,email from escort where ";
		String subQuery = "";
		boolean nameFlag = false;
		boolean idFlag = false;
		if (searchEmployee.getEmployeeFirstName() != null
				&& !searchEmployee.getEmployeeFirstName().equals("")) {
			subQuery = subQuery + " name like ? ";
			nameFlag = true;
		}
		if (searchEmployee.getEmployeeID() != null
				&& !searchEmployee.getEmployeeID().equals("")) {
			if (nameFlag)
				subQuery += " and ";
			subQuery = subQuery + " escortClock = ? ";
			idFlag = true;
		}
		Connection con = ob.connectDB();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		query += subQuery;
		System.out.println("query " + query + " "
				+ searchEmployee.getEmployeeID());
		try {
			stmt = con.prepareStatement(query);
			if (nameFlag && idFlag) {
				stmt.setString(1, searchEmployee.getEmployeeID());
				stmt.setString(2, "%" + searchEmployee.getEmployeeFirstName()
						+ "%");
			} else if (nameFlag) {
				stmt.setString(1, "%" + searchEmployee.getEmployeeFirstName()
						+ "%");
			} else if (idFlag) {
				stmt.setString(1, searchEmployee.getEmployeeID());
			}

			rs = stmt.executeQuery();
			while (rs.next()) {
				EmployeeDto dto = new EmployeeDto();
				dto.setEmployeeID(rs.getString("id"));
				dto.setPersonnelNo(rs.getString("escortClock"));
				dto.setEmployeeFirstName(rs.getString("name"));
				dto.setDisplayName(rs.getString("name"));
				dto.setEmailAddress(rs.getString("email"));
				dtoList.add(dto);
			}

		} catch (Exception e) {
			System.out.println("Exception in EmployeeDao SearchEmployee " + e);

		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(stmt);
			DbConnect.closeConnection(con);
		}

		return dtoList;
	}

	public List<EmployeeDto> searchEmployee(EmployeeDto searchEmployee) {
		List<EmployeeDto> dtoList = new ArrayList<EmployeeDto>();

		DbConnect ob = DbConnect.getInstance();
		boolean nameFlag = false;
		boolean emailFlag = false;
		boolean idflag = false;
		String query = "select distinct employee.id, EmployeeFirstName, EmployeeLastName,personnelNo, address, EmailAddress, DeptName , r.name as usertype,displayname   from roles r join  employee on employee.role_id=r.id ";
		if (searchEmployee.getLineManager() != null
				&& !searchEmployee.getLineManager().equals("")) {
			query += " left join employee_subscription es on es.empid=employee.id and (es.subscriptionStatus='subscribed' or  es.subscriptionStatus='pending')   left join roledelegation rd on rd.delegatedEmployeeId="+searchEmployee.getLineManager()+" and rd.from_Date <= curdate() and rd.status='a'  where   (linemanager="+searchEmployee.getLineManager()+" or rd.EmployeeId=linemanager or rd.employeeid=es.spoc or es.spoc="+searchEmployee.getLineManager()+" )  and   personnelNo is not null ";
		} else {
			query += " where personnelNo is not null ";
		}
		String subQuery = "";

		if (searchEmployee.getEmployeeFirstName() != null
				&& !searchEmployee.getEmployeeFirstName().equals("")) {
			subQuery = subQuery + " and EmployeeFirstName like ? ";
			nameFlag = true;
		}

		if (searchEmployee.getEmployeeID() != null
				&& !searchEmployee.getEmployeeID().equals("")) {
			subQuery = subQuery + " and  PersonnelNo =? ";
			idflag = true;
		}

		if (searchEmployee.getEmployeeLastName() != null
				&& !searchEmployee.getEmployeeLastName().equals("")) {
			subQuery = subQuery + " and ";
			subQuery = subQuery + " employeeLastName like ? ";
			emailFlag = true;
		}

		if (!subQuery.equals("")) {
			query = query + " " + subQuery;
		}

		Connection con = ob.connectDB();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			System.out.println(query);
			stmt = con.prepareStatement(query);
			int i = 1;
			if (nameFlag) {
				stmt.setString(i, "%" + searchEmployee.getEmployeeFirstName()
						+ "%");
				i++;
			}
			if (idflag) {
				stmt.setString(i, searchEmployee.getEmployeeID());
				i++;
			}
			if (emailFlag) {
				stmt.setString(i, "%" + searchEmployee.getEmployeeLastName()
						+ "%");
				i++;
			}

			rs = stmt.executeQuery();

			while (rs.next()) {
				EmployeeDto dto = new EmployeeDto();
				dto.setEmployeeFirstName(rs.getString("EmployeeFirstName"));
				dto.setEmployeeLastName(rs.getString("EmployeeLastName"));
				dto.setPersonnelNo(rs.getString("personnelNo"));
				dto.setEmailAddress(rs.getString("emailAddress"));
				dto.setDeptName(rs.getString("deptName"));
				dto.setEmployeeID(rs.getString("id"));
				dto.setUserType(rs.getString("usertype"));
				dto.setAddress(rs.getString("address"));
				dto.setDisplayName(rs.getString("displayname"));
				dtoList.add(dto);

			}

		} catch (Exception e) {

			System.out.println("Exception in EmployeeDao SearchEmployee " + e);

		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(stmt);
			DbConnect.closeConnection(con);
		}

		return dtoList;

	}

	public List<EmployeeDto> searchEmployeeUnderManager(
			EmployeeDto searchEmployee) {
		List<EmployeeDto> dtoList = new ArrayList<EmployeeDto>();

		DbConnect ob = DbConnect.getInstance();
		boolean nameFlag = false;
		boolean emailFlag = false;
		boolean idflag = false;
		String query = "select id, EmployeeFirstName, EmployeeLastName,personnelNo, address, EmailAddress, DeptName , case usertype when 'hrm' then 'Manager/SPOC' when 'tm' then 'Transport Co-ordinator'  when 'ta' then 'Transport Manager'  when 'v' then 'Vendor SuperVisor'  else    'Employee'  end as usertype from employee where lineManager="
				+ searchEmployee.getLineManager()
				+ " and personnelNo is not null   ";
		String subQuery = "";

		if (searchEmployee.getEmployeeFirstName() != null
				&& !searchEmployee.getEmployeeFirstName().equals("")) {
			subQuery = subQuery + " and EmployeeFirstName like ? ";
			nameFlag = true;
		}

		if (searchEmployee.getEmployeeID() != null
				&& !searchEmployee.getEmployeeID().equals("")) {
			subQuery = subQuery + " and  PersonnelNo =? ";
			idflag = true;
		}

		if (searchEmployee.getEmployeeLastName() != null
				&& !searchEmployee.getEmployeeLastName().equals("")) {
			subQuery = subQuery + " and ";
			subQuery = subQuery + " employeeLastName like ? ";
			emailFlag = true;
		}

		if (!subQuery.equals("")) {
			query = query + " " + subQuery;
		}

		Connection con = ob.connectDB();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = con.prepareStatement(query);
			int i = 1;
			if (nameFlag) {
				stmt.setString(i, "%" + searchEmployee.getEmployeeFirstName()
						+ "%");
				i++;
			}
			if (idflag) {
				stmt.setString(i, searchEmployee.getEmployeeID());
				i++;
			}
			if (emailFlag) {
				stmt.setString(i, "%" + searchEmployee.getEmployeeLastName()
						+ "%");
				i++;
			}

			rs = stmt.executeQuery();

			while (rs.next()) {
				EmployeeDto dto = new EmployeeDto();
				dto.setEmployeeFirstName(rs.getString("EmployeeFirstName"));
				dto.setEmployeeLastName(rs.getString("EmployeeLastName"));
				dto.setPersonnelNo(rs.getString("personnelNo"));
				dto.setEmailAddress(rs.getString("emailAddress"));
				dto.setDeptName(rs.getString("deptName"));
				dto.setEmployeeID(rs.getString("id"));
				dto.setUserType(rs.getString("usertype"));
				dto.setAddress(rs.getString("address"));

				dtoList.add(dto);

			}

		} catch (Exception e) {

			System.out.println("Exception in EmployeeDao SearchEmployee " + e);

		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(stmt);
			DbConnect.closeConnection(con);
		}

		return dtoList;

	}

	public List<EmployeeDto> searchEmployeeForSpoc(EmployeeDto searchEmployee) {
		List<EmployeeDto> dtoList = new ArrayList<EmployeeDto>();

		DbConnect ob = DbConnect.getInstance();
		boolean nameFlag = false;
		boolean emailFlag = false;
		boolean idflag = false;
		String query = "select id, EmployeeFirstName, EmployeeLastName,personnelNo, address, EmailAddress, DeptName , case usertype when 'hrm' then 'Manager/SPOC' when 'tm' then 'Transport Co-ordinator'  when 'ta' then 'Transport Manager'  when 'v' then 'Vendor SuperVisor'  else    'Employee'  end as usertype from employee where personnelNo not like '99%' ";
		String subQuery = "";

		if (searchEmployee.getEmployeeFirstName() != null
				&& !searchEmployee.getEmployeeFirstName().equals("")) {
			subQuery = subQuery + " and EmployeeFirstName like ? ";
			nameFlag = true;
		}

		if (searchEmployee.getEmployeeID() != null
				&& !searchEmployee.getEmployeeID().equals("")) {
			subQuery = subQuery + " and  PersonnelNo =? ";
			idflag = true;
		}

		if (searchEmployee.getEmployeeLastName() != null
				&& !searchEmployee.getEmployeeLastName().equals("")) {
			subQuery = subQuery + " and ";
			subQuery = subQuery + " employeeLastName like ? ";
			emailFlag = true;
		}

		if (!subQuery.equals("")) {
			query = query + " " + subQuery;
		}

		Connection con = ob.connectDB();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = con.prepareStatement(query);
			int i = 1;
			if (nameFlag) {
				stmt.setString(i, "%" + searchEmployee.getEmployeeFirstName()
						+ "%");
				i++;
			}
			if (idflag) {
				stmt.setString(i, searchEmployee.getEmployeeID());
				i++;
			}
			if (emailFlag) {
				stmt.setString(i, "%" + searchEmployee.getEmployeeLastName()
						+ "%");
				i++;
			}

			rs = stmt.executeQuery();

			while (rs.next()) {
				EmployeeDto dto = new EmployeeDto();
				dto.setEmployeeFirstName(rs.getString("EmployeeFirstName"));
				dto.setEmployeeLastName(rs.getString("EmployeeLastName"));
				dto.setPersonnelNo(rs.getString("personnelNo"));
				dto.setEmailAddress(rs.getString("emailAddress"));
				dto.setDeptName(rs.getString("deptName"));
				dto.setEmployeeID(rs.getString("id"));
				dto.setUserType(rs.getString("usertype"));
				dto.setAddress(rs.getString("address"));

				dtoList.add(dto);

			}

		} catch (Exception e) {

			System.out.println("Exception in EmployeeDao SearchEmployee " + e);

		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(stmt);
			DbConnect.closeConnection(con);
		}

		return dtoList;

	}

	public int updateEmployeesRole(String id[], String role, String doneBy) {
		int returnInt = 0;
		String query = "update employee set usertype=(select usertype from roles where id=?),role_id=?, autoRoleUpdate='false' where id=?";
		System.out.println(query);

		DbConnect ob = DbConnect.getInstance();
		// Statement st = null;
		ResultSet rs = null;
		int changedBy = 0;
		Connection con = ob.connectDB();
		PreparedStatement pst = null;
		int autoincNumber;
		try {
			// Connection pooling implementation

			// replace ob.connectDB() with Connection con=ob.connectDB();
			// replace ob.con with con
			changedBy = Integer.parseInt(doneBy);
			pst = con.prepareStatement(query);
			for (String emp : id) {
				System.out.println("empid :" + id);
				System.out.println("Role :" + role);
				pst.setString(1, role);
				pst.setString(2, role);
				pst.setLong(3, Long.parseLong(emp));
				System.out.println(role + "      id" + emp);
				returnInt += pst.executeUpdate();
				if (returnInt >= 1) {
					autoincNumber = Integer.parseInt(emp);
					auditLogEntryforchangerole(autoincNumber,
							AuditLogConstants.CHANGEROLE_EMP_MODULE, changedBy,
							AuditLogConstants.WORKFLOW_STATE_EMPTY,
							AuditLogConstants.WORK_FLOW_STATE_EMP_ROLE_UPDATED,
							AuditLogConstants.AUDIT_LOG_EDIT_ACTION);
				}
			}
		} catch (Exception ex) {
			System.out.println("Error in EmployeeDao UpdateEmployeesRole() "
					+ ex);
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);
		}

		return returnInt;
	}

	private void auditLogEntryforchangerole(int autoincNumber, String Module,
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

	// get email address based on employee'subscription id
	public EmployeeDto getEmployeeDetailsBasedOnSubscriptionID(
			String subscriptionID) {
		EmployeeDto dto = new EmployeeDto();
		DbConnect ob = DbConnect.getInstance();

		String query = "select e.id, e.EmployeeFirstName, e.EmployeeLastName,e.displayName,e.EmailAddress, s.subscriptionStatus,e.site  from employee e join employee_subscription s  on e.id=s.empid   where s.subscriptionID=? ";
		// String query=
		// "select id, employee_name, employeeLastName from employee where employee_name like '%"
		// + employeeName + "%' or employeeLastName like '%" + employeeName +
		// "%' ";
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Connection con = ob.connectDB();
		try {
			stmt = con.prepareStatement(query);
			// Statement stmt = con.createStatement();

			stmt.setString(1, subscriptionID);

			// stmt.setString(1, employeeName );
			// stmt.setString(2, employeeName );
			// ResultSet rs = stmt.executeQuery(query);
			rs = stmt.executeQuery();
			// System.out.println("Track... " + query);

			if (rs.next()) {

				dto.setEmployeeID(rs.getString("id"));
				dto.setEmailAddress(rs.getString("EmailAddress"));
				dto.setEmployeeFirstName(rs.getString("EmployeeFirstName"));
				dto.setEmployeeLastName(rs.getString("EmployeeLastName"));
				dto.setDisplayName(rs.getString("displayName"));
				dto.setSite(rs.getString("site"));

				// System.out.println("ID : " + dto.getEmailAddress());
			}

		} catch (Exception e) {

			System.out.println("Exception in EmployeeDao getEmployee " + e);

		}

		finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(stmt);
			DbConnect.closeConnection(con);
		}

		return dto;

	}

	// get email address based on employee'subscription id
	public EmployeeDto getEmployeeDetailsBasedOnScheduleID(String subscriptionID) {
		EmployeeDto dto = null;
		DbConnect ob = DbConnect.getInstance();

		String query = "select e.id,e.EmailAddress,e.displayname EmployeeName from  employee_subscription s join employee e  on e.id=s.empid join employee_schedule sh on s.subscriptionID=sh.subscription_id   where sh.id=? ";
		// String query=
		// "select id, employee_name, employeeLastName from employee where employee_name like '%"
		// + employeeName + "%' or employeeLastName like '%" + employeeName +
		// "%' ";
		// System.out.println("In getEmployeeDao");
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Connection con = ob.connectDB();
		try {
			stmt = con.prepareStatement(query);
			// Statement stmt = con.createStatement();

			stmt.setString(1, subscriptionID);

			// stmt.setString(1, employeeName );
			// stmt.setString(2, employeeName );
			// ResultSet rs = stmt.executeQuery(query);
			rs = stmt.executeQuery();
			// System.out.println("Track... " + query);

			while (rs.next()) {
				dto = new EmployeeDto();
				dto.setEmployeeID(rs.getString("id"));
				dto.setEmailAddress(rs.getString("EmailAddress"));
				dto.setEmployeeFirstName(rs.getString("EmployeeName"));
				// System.out.println("ID : " + dto.getEmailAddress());
			}

		} catch (Exception e) {

			System.out.println("Exception in EmployeeDao getEmployee " + e);

		}

		finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(stmt);
			DbConnect.closeConnection(con);
		}

		return dto;

	}

	// ---------
	// get scheduled employee details based on supervisor
	public List<EmployeeSubscriptionDto> getSupervisorBasedScheduleEmployeesAndWhoAreAboutToExpirty(
			String curDate, int day) {

		List<EmployeeSubscriptionDto> dtoList = new ArrayList<EmployeeSubscriptionDto>();
		DbConnect ob = DbConnect.getInstance();

		// String query =
		// " select sup.id supID,sup.EmailAddress spocEmail, sch.id empID,supFirstName,supLastName, sch.EmailAddress empEmail, sch.scheduleID, sch.to_date,empFirstName,empLastName from   (   select  e.id,e.EmailAddress,e.EmployeeFirstName supFirstName, e.employeeLastName supLastName from  employee_subscription s join employee e  on e.id=s.spoc join employee_schedule sh on s.subscriptionID=sh.subscription_id    union select   e.id,e.EmailAddress,e.EmployeeFirstName supFirstName, e.employeeLastName supLastName from  employee_subscription s join employee e  on e.id=s.supervisor join employee_schedule sh on s.subscriptionID=sh.subscription_id    ) sup      join    (   select  s.subscriptionID, sh.id scheduleID, e.id,e.EmailAddress,s.spoc, e.EmployeeFirstName empFirstName, e.EmployeeLastName empLastName, sh.to_date from  employee_subscription s join employee e  on e.id=s.empID  join employee_schedule sh on s.subscriptionID=sh.subscription_id       union     select  s.subscriptionID, sh.id scheduleID, e.id,e.EmailAddress,s.supervisor spoc, e.EmployeeFirstName, e.employeeLastName, sh.to_date from  employee_subscription s join employee e  on e.id=s.empID  join employee_schedule sh on s.subscriptionID=sh.subscription_id         ) sch on sup.id=sch.spoc     where to_date=dateadd(dd,"
		// + day + ",'" + curDate + "')     order by sup.id  ";
		String query = "select sup.id supID,sup.EmailAddress spocEmail, sch.id empID,supFirstName,supLastName, supDisplayName, sch.EmailAddress empEmail, sch.scheduleID, sch.to_date,empFirstName,empLastName from   (   select  e.id,e.EmailAddress,e.EmployeeFirstName supFirstName, e.employeeLastName supLastName, e.displayName supDisplayName  from  employee_subscription s join employee e  on e.id=s.spoc join   (    select es.* from employee_schedule es , (select employee_id, max(to_date) to_date from   employee_schedule    group by  employee_id ) ss where es.employee_id=ss.employee_id and es.to_date=ss.to_date  )   sh on s.subscriptionID=sh.subscription_id    union select   e.id,e.EmailAddress,e.EmployeeFirstName supFirstName, e.employeeLastName supLastName, e.displayName supDisplayName from  employee_subscription s join employee e  on e.id=s.supervisor join (   select es.* from employee_schedule es , ( select employee_id, max(to_date) to_date from   employee_schedule    group by  employee_id ) ss where es.employee_id=ss.employee_id and es.to_date=ss.to_date  )  sh on s.subscriptionID=sh.subscription_id    ) sup      join     (   select  s.subscriptionID, sh.id scheduleID, e.id,e.EmailAddress,s.spoc, e.EmployeeFirstName empFirstName,   e.EmployeeLastName empLastName, sh.to_date    from  employee_subscription s join employee e  on e.id=s.empID     join    (    select es.* from employee_schedule es , (select employee_id, max(to_date) to_date from   employee_schedule    group by  employee_id ) ss where es.employee_id=ss.employee_id and es.to_date=ss.to_date  )     sh on s.subscriptionID=sh.subscription_id          union        select  s.subscriptionID, sh.id scheduleID, e.id,e.EmailAddress,s.supervisor spoc, e.EmployeeFirstName, e.employeeLastName, sh.to_date    from  employee_subscription s join employee e  on e.id=s.empID  join 	(    select es.* from employee_schedule es , ( select employee_id, max(to_date) to_date from   employee_schedule    group by  employee_id  ) ss where es.employee_id=ss.employee_id and es.to_date=ss.to_date   ) 	 sh on s.subscriptionID=sh.subscription_id     ) sch on sup.id=sch.spoc where to_date=DATE_ADD('"
				+ curDate + "',INTERVAL " + day + " DAY) and sch.subscriptionID not in  (select subscriptionid from employee_unsubscription where fromDate<=DATE_ADD('" + curDate + "',INTERVAL " + day + " DAY)  )     order by sup.id  ";
		System.out.println("Query : " + query);

		Statement stmt = null;
		ResultSet rs = null;
		Connection con = ob.connectDB();
		try {
			stmt = con.createStatement();
			// Statement stmt = con.createStatement();

			// stmt.setString(1, employeeName );
			// stmt.setString(2, employeeName );
			// ResultSet rs = stmt.executeQuery(query);
			rs = stmt.executeQuery(query);

			while (rs.next()) {
				EmployeeSubscriptionDto dto = new EmployeeSubscriptionDto();

				EmployeeDto empDto = new EmployeeDto();
				EmployeeDto supDto = new EmployeeDto();
				empDto.setEmployeeID(rs.getString("empID"));
				empDto.setEmployeeFirstName(rs.getString("empFirstName"));
				empDto.setEmployeeLastName(rs.getString("empLastName"));
				empDto.setEmailAddress(rs.getString("empEmail"));
				supDto.setDisplayName(rs.getString("supDisplayName"));
				supDto.setEmployeeFirstName(rs.getString("supFirstName"));
				supDto.setEmployeeLastName(rs.getString("supLastName"));

				supDto.setEmployeeID(rs.getString("supID"));
				supDto.setEmailAddress(rs.getString("spocEmail"));

				dto.setEmployee(empDto);
				dto.setSupervisor(supDto);
				dto.setUnsubscriptionDate(rs.getDate("to_date").toString());
				// System.out.println(" To date : " +
				// dto.getUnsubscriptionDate());
				dtoList.add(dto);
			}

		} catch (Exception e) {

			System.out
					.println("Exception in getSupervisorBasedScheduleEmployeesAndWhoAreAboutToExpirty   "
							+ e);

		}

		finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(stmt);
			DbConnect.closeConnection(con);
		}

		return dtoList;

	}

	public List<EmployeeSubscriptionDto> getEmployeesWhoAreAboutScheduleExpiry(
			String curDate, int day) {
		List<EmployeeSubscriptionDto> dtoList = new ArrayList<EmployeeSubscriptionDto>();
		DbConnect ob = DbConnect.getInstance();

		// String query =
		// "    select  sh.id scheduleID, e.id, e.EmployeeFirstName + ' ' + e.employeeLastName employeeName , e.EmailAddress,  sh.to_date from  employee_subscription s join employee e  on e.id=s.empID  join employee_schedule sh on s.subscriptionID=sh.subscription_id           where to_date=dateadd(dd,"
		// + day + ",'" + curDate + "')   and status='a'    ";
		String query = "select  sh.id scheduleID, e.id, e.EmployeeFirstName + ' ' + e.employeeLastName employeeName , e.EmailAddress,  sh.to_date from  employee_subscription s join employee e  on e.id=s.empID  join employee_schedule sh on s.subscriptionID=sh.subscription_id           where to_date=dateadd(dd,"
				+ day
				+ ",'"
				+ curDate
				+ "') and   status='a'    and sh.subscription_id not in ( select  subscription_id from   employee_schedule where subscription_id=sh.subscription_id and from_date>dateadd(dd,"
				+ day + ",'" + curDate + "')) ";

		// System.out.println("Query : " + query);

		Statement stmt = null;
		ResultSet rs = null;
		Connection con = ob.connectDB();
		try {
			stmt = con.createStatement();
			// Statement stmt = con.createStatement();

			// stmt.setString(1, employeeName );
			// stmt.setString(2, employeeName );
			// ResultSet rs = stmt.executeQuery(query);
			rs = stmt.executeQuery(query);

			while (rs.next()) {
				EmployeeSubscriptionDto dto = new EmployeeSubscriptionDto();

				EmployeeDto empDto = new EmployeeDto();
				// EmployeeDto supDto = new EmployeeDto();
				empDto.setEmployeeID(rs.getString("id"));
				empDto.setEmployeeFirstName(rs.getString("employeeName"));

				empDto.setEmailAddress(rs.getString("EmailAddress"));

				dto.setEmployee(empDto);
				dto.setUnsubscriptionDate(rs.getDate("to_date").toString());
				// System.out.println(" To date : " +
				// dto.getUnsubscriptionDate());
				dtoList.add(dto);
			}

		} catch (Exception e) {

			System.out.println("Exception in  ...   " + e);

		}

		finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(stmt);
			DbConnect.closeConnection(con);
		}

		return dtoList;

	}

	// -----
	public List<EmployeeDto> searchEmployeeFromSubscription(
			EmployeeDto searchEmployee) {
		List<EmployeeDto> dtoList = new ArrayList<EmployeeDto>();

		DbConnect ob = DbConnect.getInstance();
		boolean nameFlag = false;
		boolean emailFlag = false;
		boolean idflag = false;
		String query = "select id, EmployeeFirstName, EmployeeLastName,personnelNo, address, EmailAddress, DeptName , case usertype when 'hrm' then 'Manager' when 'tm' then 'Transport Manager' else    'Employee' end as usertype,case routingType when 'o' then 'Shared Vehicle' when 'p' then 'Project Vehicle' else    'Individual Vehicle' end as routingType from employee";
		String subQuery = "";
		if (searchEmployee.getEmployeeFirstName() != null
				&& !searchEmployee.getEmployeeFirstName().equals("")) {

			subQuery = subQuery + "EmployeeFirstName like ? ";
			nameFlag = true;
		}

		if (searchEmployee.getEmployeeID() != null
				&& !searchEmployee.getEmployeeID().equals("")) {

			if (!subQuery.equals("")) {
				subQuery = subQuery + " and ";
			}
			subQuery = subQuery + " PersonnelNo =? ";

			idflag = true;

		}

		if (searchEmployee.getEmployeeLastName() != null
				&& !searchEmployee.getEmployeeLastName().equals("")) {

			if (!subQuery.equals("")) {
				subQuery = subQuery + " and ";
			}
			subQuery = subQuery + " employeeLastName like ? ";

			emailFlag = true;
		}
		query = query
				+ " where id in (select empID id from employee_subscription where subscriptionStatus='"
				+ EmployeeSubscriptionDto.status.PENDING
				+ "' or subscriptionStatus='"
				+ EmployeeSubscriptionDto.status.SUBSCRIBED + "' )";
		if (!subQuery.equals("")) {
			query = query + "and ( " + subQuery + " )";
		}
		Connection con = ob.connectDB();
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			stmt = con.prepareStatement(query);
			System.out.println("query" + query);
			// Statement stmt = con.createStatement();
			int i = 1;
			if (nameFlag) {
				stmt.setString(i, "%" + searchEmployee.getEmployeeFirstName()
						+ "%");
				i++;
			}
			if (idflag) {
				stmt.setString(i, searchEmployee.getEmployeeID());
				i++;
			}
			if (emailFlag) {
				stmt.setString(i, "%" + searchEmployee.getEmployeeLastName()
						+ "%");
				i++;
			}

			rs = stmt.executeQuery();
			while (rs.next()) {
				EmployeeDto dto = new EmployeeDto();
				dto.setEmployeeFirstName(rs.getString("EmployeeFirstName"));
				dto.setEmployeeLastName(rs.getString("EmployeeLastName"));
				dto.setPersonnelNo(rs.getString("personnelNo"));
				dto.setEmailAddress(rs.getString("emailAddress"));
				dto.setDeptName(rs.getString("deptName"));
				dto.setEmployeeID(rs.getString("id"));
				dto.setUserType(rs.getString("usertype"));
				dto.setAddress(rs.getString("address"));
				dto.setRoutingType(rs.getString("routingType"));
				// System.out.println(dto.getUserType());
				// dto.setEmailAddress(rs.getString("EmailAddress"));

				dtoList.add(dto);

			}

		} catch (Exception e) {

			System.out.println("Exception in EmployeeDao SearchEmployee " + e);

		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(stmt);
			DbConnect.closeConnection(con);

		}

		return dtoList;

	}

	public List<EmployeeDto> searchEmployeeFromSubscription(String site) {
		List<EmployeeDto> dtoList = new ArrayList<EmployeeDto>();

		DbConnect ob = DbConnect.getInstance();
		String query = "select id, EmployeeFirstName, EmployeeLastName,personnelNo, address, EmailAddress, DeptName , case usertype when 'hrm' then 'Manager' when 'tm' then 'Transport Manager' else    'Employee' end as usertype,case routingType when 'o' then 'Shared Vehicle' when 'p' then 'Project Vehicle' else    'Individual Vehicle' end as routingType from employee where site="
				+ site
				+ " and "
				+ " id in (select empID id from employee_subscription where subscriptionStatus='"
				+ EmployeeSubscriptionDto.status.PENDING
				+ "' or subscriptionStatus='"
				+ EmployeeSubscriptionDto.status.SUBSCRIBED + "' )";
		Connection con = ob.connectDB();
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			stmt = con.prepareStatement(query);
			System.out.println("query" + query);
			rs = stmt.executeQuery();
			while (rs.next()) {
				EmployeeDto dto = new EmployeeDto();
				dto.setEmployeeFirstName(rs.getString("EmployeeFirstName"));
				dto.setEmployeeLastName(rs.getString("EmployeeLastName"));
				dto.setPersonnelNo(rs.getString("personnelNo"));
				dto.setEmailAddress(rs.getString("emailAddress"));
				dto.setDeptName(rs.getString("deptName"));
				dto.setEmployeeID(rs.getString("id"));
				dto.setUserType(rs.getString("usertype"));
				dto.setAddress(rs.getString("address"));
				dto.setRoutingType(rs.getString("routingType"));
				dtoList.add(dto);
			}

		} catch (Exception e) {

			System.out.println("Exception in EmployeeDao SearchEmployee " + e);

		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(stmt);
			DbConnect.closeConnection(con);
		}
		return dtoList;
	}

	public EmployeeDto getSpoc(String empid) {
		EmployeeDto dto = new EmployeeDto();
		DbConnect ob = DbConnect.getInstance();

		String query = "select employee.id, EmployeeFirstName, personnelNo, loginId, deptName, employeeLastName, EmailAddress from employee where employee.id in "
				+ " (select top 1 s.spoc from employee_subscription s where s.empID=? order by s.fromDate)";

		Connection con = null;
		con = ob.connectDB();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = con.prepareStatement(query);
			stmt.setString(1, empid);
			rs = stmt.executeQuery();
			if (rs.next()) {

				dto.setEmployeeID(rs.getString("id"));

				dto.setEmployeeFirstName(rs.getString("employeeFirstname"));
				dto.setEmployeeLastName(rs.getString("employeeLastName"));
				dto.setPersonnelNo(rs.getString("personnelNo"));
				dto.setDeptName(rs.getString("deptName"));
				dto.setEmailAddress(rs.getString("EmailAddress"));
				dto.setLoginId(rs.getString("loginId"));
			}

		} catch (Exception e) {
			System.out.println("Exception in EmployeeDao getSpoc " + e);

		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(stmt);
			DbConnect.closeConnection(con);
		}

		return dto;
	}

	public EmployeeDto getManager(String empid) {
		EmployeeDto dto = new EmployeeDto();
		DbConnect ob = DbConnect.getInstance();

		String query = "select employee.id, EmployeeFirstName, personnelNo, loginId, deptName, employeeLastName, EmailAddress from employee where employee.id in "
				+ " (select top 1 s.supervisor from employee_subscription s where s.empID=? order by s.fromDate)";
		Connection con = ob.connectDB();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = con.prepareStatement(query);
			stmt.setString(1, empid);
			rs = stmt.executeQuery();

			if (rs.next()) {

				dto.setEmployeeID(rs.getString("id"));

				dto.setEmployeeFirstName(rs.getString("employeeFirstname"));
				dto.setEmployeeLastName(rs.getString("employeeLastName"));
				dto.setPersonnelNo(rs.getString("personnelNo"));
				dto.setDeptName(rs.getString("deptName"));
				dto.setEmailAddress(rs.getString("EmailAddress"));
				dto.setLoginId(rs.getString("loginId"));

				// System.out.println("login id : "+ dto.getLoginId());

			}

		} catch (Exception e) {
			System.out.println("Exception in EmployeeDao getSpoc " + e);

		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(stmt);
			DbConnect.closeConnection(con);
		}

		return dto;
	}

	public EmployeeDto getOne_Employee(String role) {
		EmployeeDto dto = new EmployeeDto();
		DbConnect ob = DbConnect.getInstance();
		String query = "select employee.id, employee.displayName, EmployeeFirstName, personnelNo, loginId, deptName, employeeLastName, EmailAddress from employee where  userType=? ";
		Connection con = ob.connectDB();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = con.prepareStatement(query);
			stmt.setString(1, role);
			rs = stmt.executeQuery();

			if (rs.next()) {

				dto.setEmployeeID(rs.getString("id"));
				dto.setDisplayName(rs.getString("displayName"));
				dto.setEmployeeFirstName(rs.getString("employeeFirstname"));
				dto.setEmployeeLastName(rs.getString("employeeLastName"));
				dto.setPersonnelNo(rs.getString("personnelNo"));
				dto.setDeptName(rs.getString("deptName"));
				dto.setEmailAddress(rs.getString("EmailAddress"));
				dto.setLoginId(rs.getString("loginId"));

				// System.out.println("login id : "+ dto.getLoginId());

			}

		} catch (Exception e) {
			System.out.println("Exception in EmployeeDao getEmployee " + e);

		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(stmt);
			DbConnect.closeConnection(con);
		}
		return dto;
	}

	public ArrayList<EmployeeDto> getEmployeesByRole(String role) {
		ArrayList<EmployeeDto> dtoList = new ArrayList<EmployeeDto>();
		DbConnect ob = DbConnect.getInstance();
		String query = "select employee.id, employee.displayName, EmployeeFirstName, personnelNo, loginId, deptName, employeeLastName, EmailAddress from employee where  userType=? ";
		Connection con = ob.connectDB();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = con.prepareStatement(query);
			stmt.setString(1, role);
			rs = stmt.executeQuery();

			while (rs.next()) {
				EmployeeDto dto = new EmployeeDto();
				dto.setEmployeeID(rs.getString("id"));
				dto.setDisplayName(rs.getString("displayName"));
				dto.setEmployeeFirstName(rs.getString("employeeFirstname"));
				dto.setEmployeeLastName(rs.getString("employeeLastName"));
				dto.setPersonnelNo(rs.getString("personnelNo"));
				dto.setDeptName(rs.getString("deptName"));
				dto.setEmailAddress(rs.getString("EmailAddress"));
				dto.setLoginId(rs.getString("loginId"));
				dtoList.add(dto);
				// System.out.println("login id : "+ dto.getLoginId());

			}

		} catch (Exception e) {
			System.out.println("Exception in EmployeeDao getEmployee " + e);

		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(stmt);
			DbConnect.closeConnection(con);
		}
		return dtoList;
	}

	public ArrayList<EmployeeDto> getDelegatedEmployees(String empid) {

		ArrayList<EmployeeDto> deles = new ArrayList<EmployeeDto>();
		String query = "select e.* from employee e, roleDelegation r where e.id=r.delegatedEmployeeId and r.employeeId=? and curdate() between from_date and to_date ";
		// System.out.println(" trackk...");
		DbConnect ob = DbConnect.getInstance();
		Connection con = ob.connectDB();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = con.prepareStatement(query);

			stmt.setString(1, empid);
			rs = stmt.executeQuery();

			if (rs.next()) {
				EmployeeDto dto = new EmployeeDto();
				dto.setEmployeeID(rs.getString("id"));
				dto.setDisplayName(rs.getString("displayName"));
				dto.setEmployeeFirstName(rs.getString("employeeFirstname"));
				dto.setEmployeeLastName(rs.getString("employeeLastName"));
				dto.setPersonnelNo(rs.getString("personnelNo"));
				dto.setDeptName(rs.getString("deptName"));
				dto.setEmailAddress(rs.getString("EmailAddress"));
				dto.setLoginId(rs.getString("loginId"));

				deles.add(dto);

			}

		} catch (Exception e) {
			System.out.println("Exception in EmployeeDao getEmployee " + e);

		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(stmt);
			DbConnect.closeConnection(con);
		}
		return deles;
	}

	public List<EmployeeDto> getProjectEmployees(EmployeeDto dto) {

		List<EmployeeDto> resultDto = new ArrayList<EmployeeDto>();
		String empfirstname = dto.getEmployeeFirstName(), emplastname = dto
				.getEmployeeLastName();
		// int empid = 0
		int projectid = 0;
		String query = "";
		DbConnect ob = DbConnect.getInstance();
		Connection con = ob.connectDB();
		PreparedStatement pst = null;
		ResultSet rs = null;
		String subQuery = "";
		String projectQuery = "";

		try {
			if (empfirstname == null || empfirstname.equals("")) {
				empfirstname = "";
			}
			if (emplastname == null || emplastname.equals("")) {
				emplastname = "";
			}

			if (dto.getEmployeeID() != null && !dto.getEmployeeID().equals("")) {
				subQuery = " and PersonnelNo=?";
			}

			if (dto.getProjectid() != null && !dto.getProjectid().equals("")) {
				projectid = Integer.parseInt(dto.getProjectid());
				projectQuery += "  id in  (select employee_id from employee_schedule where project="
						+ projectid + ") and ";
			}

			query = "select id,EmployeeFirstName,EmployeeLastName,personnelNo,emailAddress,deptName,usertype,address,case routingType when 'o' then 'Shared Vehicle' when 'p' then 'Project Vehicle' else    'Individual Vehicle' end as routingType from employee where "
					+ projectQuery
					+ " employeeFirstName like ? and employeeLastName like ? "
					+ subQuery;
			System.out.println("query " + query);
			pst = con.prepareStatement(query);
			pst.setString(1, "%" + empfirstname + "%");
			pst.setString(2, "%" + emplastname + "%");
			if (!subQuery.equals("")) {
				pst.setString(3, dto.getEmployeeID());
			}
			rs = pst.executeQuery();
			while (rs.next()) {
				EmployeeDto dto1 = new EmployeeDto();
				dto1.setEmployeeFirstName(rs.getString("EmployeeFirstName"));
				dto1.setEmployeeLastName(rs.getString("EmployeeLastName"));
				dto1.setPersonnelNo(rs.getString("personnelNo"));
				dto1.setEmailAddress(rs.getString("emailAddress"));
				dto1.setDeptName(rs.getString("deptName"));
				dto1.setEmployeeID(rs.getString("id"));
				dto1.setUserType(rs.getString("usertype"));
				dto1.setAddress(rs.getString("address"));
				dto1.setRoutingType(rs.getString("routingType"));
				resultDto.add(dto1);
			}
			

		} catch (Exception e) {			
			System.out.println("Exception in EmployeeDao getproject " + e);

		}
		finally
		{
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);	
		}
		return resultDto;
	}

	public int UpdateSetSpecial(String id[], String setattrib) {
		int returnInt = 0;
		String query = "update employee set routingType=? where id=?";

		DbConnect ob = DbConnect.getInstance();
		ResultSet rs = null;
		Connection con = ob.connectDB();
		PreparedStatement pst = null;
		try {
			pst = con.prepareStatement(query);
			for (String emp : id) {
				pst.setString(1, setattrib);
				pst.setLong(2, Long.parseLong(emp));
				returnInt += pst.executeUpdate();
			}

		} catch (Exception ex) {
			System.out.println("Error in EmployeeDao UpdateSetSpecial() ");
		}
		finally
		{
		DbConnect.closeResultSet(rs);
		DbConnect.closeStatement(pst);
		DbConnect.closeConnection(con);
		}

		return returnInt;
	}

	public boolean checkPersonnelNo(String loginid) {
		DbConnect ob = DbConnect.getInstance();
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		boolean flag = false;
		try {
			String query = "select * from employee where personnelNo='"
					+ loginid + "'";
			con = ob.connectDB();
			st = con.createStatement();
			rs = st.executeQuery(query);
			if (rs.next()) {
				flag = true;
			}
		} catch (Exception e) {
		
		e.printStackTrace();
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);

		}
		return flag;
	}

	public boolean checkLoginId(String loginid) {
		DbConnect ob = DbConnect.getInstance();
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		boolean flag = false;
		try {
			String query = "select * from employee where loginId='" + loginid
					+ "'";
			con = ob.connectDB();
			st = con.createStatement();
			rs = st.executeQuery(query);
			if (rs.next()) {
				flag = true;
			}
		} catch (Exception e) {
			try {
				con.rollback();
			} catch (Exception ignore) {

			}
			e.printStackTrace();
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);

		}
		return flag;
	}

	public int insertExternalEmployee(EmployeeDto dto) {
		DbConnect ob = DbConnect.getInstance();
		Connection con = null;
		PreparedStatement st = null;
		int returnInt = 0;
		try {
			con = ob.connectDB();
			String query = "INSERT INTO EMPLOYEE (EmployeeFirstName,EmployeeMiddleName,employeeLastName,loginId,Gender,PersonnelNo,EmailAddress,Dateofjoining,address,usertype,isContractEmployee,contactNumber1,displayname,authType,externalUser,password,role_id,active,routingtype,project,linemanager,projectunit) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,(select id from roles where usertype=?),1,?,(select project from atsconnect where id=? limit 1),?,(select projectunit from atsconnect where id=? limit 1))";
			st = con.prepareStatement(query);
			st.setString(1, dto.getEmployeeFirstName());
			st.setString(2, dto.getEmployeeMiddleName());
			st.setString(3, dto.getEmployeeLastName());
			st.setString(4, dto.getLoginId());
			st.setString(5, dto.getGender());
			st.setString(6, dto.getPersonnelNo());
			st.setString(7, dto.getEmailAddress());
			st.setString(8, OtherFunctions.changeDateFromatToIso(dto
					.getDateOfJoining()));
			st.setString(9, dto.getAddress());
			st.setString(10, dto.getUserType());
			st.setString(11, dto.getContract());
			st.setString(12, dto.getContactNo());
			st.setString(13, dto.getDisplayName());
			st.setString(14, dto.getAuthtype());
			st.setString(15, "Yes");
			st.setString(16, dto.getPassword());
			st.setString(17, dto.getUserType());
			st.setString(18, "o");
			st.setString(19, dto.getProjectid());
			st.setString(20, dto.getLineManager());
			st.setString(21,dto.getProjectid());
			if (dto.getUserType().equalsIgnoreCase("v"))
				;
			{

			}
			returnInt = st.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);

		}
		return returnInt;
	}

	public EmployeeDto getEmployeeByLogin(String EmpId) {
		System.out.println("get employee by login started" + EmpId);
		PreparedStatement st1 = null;
		ResultSet rs1 = null;

		try {
			rs1 = null;
			st1 = null;
			con = DbConnect.getInstance().connectDB();
			st1 = con
					.prepareStatement("select * from employee where Loginid=?");
			System.out.println("select * from employee where Loginid='" + EmpId
					+ "'");
			st1.setString(1, EmpId);
			employeedtobj = new EmployeeDto();
			rs1 = st1.executeQuery();
			if (rs1.next()) {
				employeedtobj.setEmployeeID(rs1.getString("id"));
				employeedtobj.setEmployeeFirstName(""
						+ rs1.getString("EmployeeFirstName"));
				employeedtobj.setEmployeeMiddleName(""
						+ rs1.getString("EmployeeMiddleName"));
				employeedtobj.setEmployeeLastName(""
						+ rs1.getString("EmployeeLastName"));
				employeedtobj.setLoginId(rs1.getString("LoginId"));
				employeedtobj.setGender(rs1.getString("Gender"));
				employeedtobj.setPersonnelNo(rs1.getString("PersonnelNo"));
				employeedtobj.setEmailAddress(rs1.getString("EmailAddress"));
				employeedtobj.setHomeCountry(rs1.getString("HomeCountry"));
				employeedtobj.setLineManager("" + rs1.getString("LineManager"));
				employeedtobj.setStaffManager(""
						+ rs1.getString("StaffManager"));
				employeedtobj.setClientServiceManager(""
						+ rs1.getString("ClientServiceManager"));
				employeedtobj.setCareerLevel("" + rs1.getString("CareerLevel"));
				employeedtobj.setCareerPathwayDesc(""
						+ rs1.getString("CareerPathwayDesc"));
				employeedtobj.setBusinessUnitCode(""
						+ rs1.getString("BusinessUnitCode"));
				employeedtobj.setBusinessUnitDescription(""
						+ rs1.getString("BusinessUnitDescription"));
				employeedtobj.setDeptno("" + rs1.getString("Deptno"));
				employeedtobj.setDeptName("" + rs1.getString("DeptName"));
				employeedtobj.setOperationCode(""
						+ rs1.getString("OperationCode"));
				employeedtobj.setOperationDescription(""
						+ rs1.getString("OperationDescription"));
				employeedtobj.setPathways("" + rs1.getString("Pathways"));
				employeedtobj.setDateOfJoining(""
						+ rs1.getString("Dateofjoining"));
				employeedtobj.setActive(rs1.getString("Active"));
				System.out.println("ACTVE : " + rs1.getString("Active") );
				employeedtobj.setAddress("" + rs1.getString("address"));
				employeedtobj.setCity("" + rs1.getString("City"));
				employeedtobj.setContactNo(rs1.getString("contactNumber1"));
				employeedtobj.setAddressChangeDate(rs1
						.getString("addressChangeDate"));
				employeedtobj.setEmpType(rs1.getString("isContractEmployee"));
				employeedtobj.setDisplayName(rs1.getString("displayname"));
				employeedtobj.setPassword(rs1.getString("password"));
				employeedtobj.setAuthtype(rs1.getString("authType"));
				employeedtobj.setPasswordchangedate(rs1
						.getString("pwdChangedDate"));
				employeedtobj.setExternaluser(rs1.getString("externalUser"));
				employeedtobj.setExternaluser(rs1.getString("userType"));
				return employeedtobj;

			} else {
				return null;
			}
		} catch (Exception e) {
			// System.out.println("errro" + e);
			e.printStackTrace();
		} finally {
			DbConnect.closeResultSet(rs1);
			DbConnect.closeStatement(st1);
			DbConnect.closeConnection(con);
		}

		System.out.println("get employee with loginid ended");
		return null;

	}

	public EmployeeDto getEmployeeById(String EmpId) {
		System.out.println("get employee by login started" + EmpId);
		PreparedStatement st1 = null;
		ResultSet rs1 = null;

		try {
			rs1 = null;
			st1 = null;
			con = DbConnect.getInstance().connectDB();
			st1 = con.prepareStatement("select * from employee where id=?");
			st1.setString(1, EmpId);
			employeedtobj = new EmployeeDto();
			rs1 = st1.executeQuery();
			if (rs1.next()) {
				employeedtobj.setEmployeeID(rs1.getString("id"));
				employeedtobj.setEmployeeFirstName(""
						+ rs1.getString("EmployeeFirstName"));
				employeedtobj.setEmployeeMiddleName(""
						+ rs1.getString("EmployeeMiddleName"));
				employeedtobj.setEmployeeLastName(""
						+ rs1.getString("EmployeeLastName"));
				employeedtobj.setLoginId(rs1.getString("LoginId"));
				employeedtobj.setGender(rs1.getString("Gender"));
				employeedtobj.setPersonnelNo(rs1.getString("PersonnelNo"));
				employeedtobj.setEmailAddress(rs1.getString("EmailAddress"));
				employeedtobj.setHomeCountry(rs1.getString("HomeCountry"));
				employeedtobj.setLineManager("" + rs1.getString("LineManager"));
				employeedtobj.setStaffManager(""
						+ rs1.getString("StaffManager"));
				employeedtobj.setClientServiceManager(""
						+ rs1.getString("ClientServiceManager"));
				employeedtobj.setCareerLevel("" + rs1.getString("CareerLevel"));
				employeedtobj.setCareerPathwayDesc(""
						+ rs1.getString("CareerPathwayDesc"));
				employeedtobj.setBusinessUnitCode(""
						+ rs1.getString("BusinessUnitCode"));
				employeedtobj.setBusinessUnitDescription(""
						+ rs1.getString("BusinessUnitDescription"));
				employeedtobj.setDeptno("" + rs1.getString("Deptno"));
				employeedtobj.setDeptName("" + rs1.getString("DeptName"));
				employeedtobj.setOperationCode(""
						+ rs1.getString("OperationCode"));
				employeedtobj.setOperationDescription(""
						+ rs1.getString("OperationDescription"));
				employeedtobj.setPathways("" + rs1.getString("Pathways"));
				employeedtobj.setDateOfJoining(""
						+ rs1.getString("Dateofjoining"));
				employeedtobj.setActive(rs1.getString("Active"));
				employeedtobj.setAddress("" + rs1.getString("address"));
				employeedtobj.setCity("" + rs1.getString("City"));
				employeedtobj.setContactNo(rs1.getString("contactNumber1"));
				employeedtobj.setAddressChangeDate(rs1
						.getString("addressChangeDate"));
				employeedtobj.setEmpType(rs1.getString("isContractEmployee"));
				employeedtobj.setDisplayName(rs1.getString("displayname"));
				employeedtobj.setPassword(rs1.getString("password"));
				employeedtobj.setAuthtype(rs1.getString("authType"));
				employeedtobj.setPasswordchangedate(rs1
						.getString("pwdChangedDate"));
				employeedtobj.setExternaluser(rs1.getString("externalUser"));
				;
				return employeedtobj;

			} else {
				return null;
			}
		} catch (Exception e) {
			// System.out.println("errro" + e);
			e.printStackTrace();
		} finally {
			DbConnect.closeResultSet(rs1);
			DbConnect.closeStatement(st1);
			DbConnect.closeConnection(con);
		}

		System.out.println("get employee with loginid ended");
		return null;

	}

	public int changePassword(int userid, String password) {
		DbConnect ob = DbConnect.getInstance();
		Connection con = null;
		PreparedStatement st = null;
		int returnInt = 0;
		try {
			con = ob.connectDB();
			st = con.prepareStatement("UPDATE EMPLOYEE SET PASSWORD=?,PWDCHANGEDDATE=curdate() WHERE ID=?");
			st.setString(1, password);
			st.setInt(2, userid);
			returnInt = st.executeUpdate();

		} catch (Exception e) {
			try {
				con.rollback();
			} catch (Exception ignore) {

			}
			e.printStackTrace();
		} finally {
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);

		}
		return returnInt;

	}

	public ArrayList<EmployeeDto> getAllExternalemployees() {
		DbConnect ob = DbConnect.getInstance();
		Connection con = null;

		Statement st = null;
		ResultSet rs = null;
		ArrayList<EmployeeDto> list = new ArrayList<EmployeeDto>();
		EmployeeDto dto = null;
		try {
			con = ob.connectDB();
			st = con.createStatement();
			rs = st.executeQuery("SELECT e.*,m.displayname AS managername,at.id as projectid  FROM EMPLOYEE e LEFT JOIN employee m ON m.id = e.LineManager LEFT JOIN ATSCONNECT AT ON AT.PROJECT=E.PROJECT WHERE e.EXTERNALUSER = 'Yes' ORDER BY personnelNo");

			while (rs.next()) {

				dto = new EmployeeDto();
				dto.setEmployeeID(rs.getString("id"));
				dto.setPersonnelNo(rs.getString("PersonnelNo"));
				dto.setEmployeeFirstName(rs.getString("EmployeeFirstName"));
				dto.setEmployeeMiddleName(rs.getString("EmployeeMiddleName"));
				dto.setEmployeeLastName(rs.getString("employeeLastName"));
				dto.setDisplayName(rs.getString("displayname"));
				dto.setGender(rs.getString("gender"));
				dto.setContactNo(rs.getString("contactNumber1"));
				dto.setEmailAddress(rs.getString("EmailAddress"));
				dto.setAuthtype(rs.getString("authType"));
				dto.setActive(rs.getString("Active"));
				dto.setAddress(rs.getString("address"));
				dto.setUserType(rs.getString("usertype"));
				dto.setProject(rs.getString("project"));
				dto.setLineManager(rs.getString("LineManager"));
				dto.setManagerName(rs.getString("managername"));
				dto.setProjectid(rs.getString("projectid"));
				list.add(dto);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);

		}
		return list;
	}

	public ArrayList<EmployeeDto> getemployeeProjectInfo() {
		DbConnect ob = DbConnect.getInstance();
		Connection con = null;

		Statement st = null;
		ResultSet rs = null;
		ArrayList<EmployeeDto> list = new ArrayList<EmployeeDto>();
		EmployeeDto dto = null;
		try {
			con = ob.connectDB();
			st = con.createStatement();
			rs = st.executeQuery("SELECT e.*,at.description as projectName  FROM EMPLOYEE e  LEFT JOIN ATSCONNECT AT ON AT.PROJECT=E.PROJECT ORDER BY personnelNo");

			while (rs.next()) {

				dto = new EmployeeDto();
				dto.setPersonnelNo(rs.getString("PersonnelNo"));
				dto.setDisplayName(rs.getString("displayname"));
				dto.setProjectid(rs.getString("project"));
				dto.setProjectUnit(rs.getString("projectUnit"));
				dto.setProject(rs.getString("projectName"));
				list.add(dto);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);

		}
		return list;
	}

	public EmployeeDto getEmployeeAccurate(String empid, String role) {
		EmployeeDto dto = new EmployeeDto();
		DbConnect ob = DbConnect.getInstance();

		Connection con = ob.connectDB();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			if (role.equalsIgnoreCase("v")) {
				String query = "select * from vendor where id=? ";
				stmt = con.prepareStatement(query);
				stmt.setString(1, empid);
				rs = stmt.executeQuery();
				if (rs.next()) {
					dto.setEmployeeID(rs.getString("id"));
					dto.setDisplayName(rs.getString("name"));
					dto.setEmailAddress(rs.getString("email"));
					dto.setLoginId(rs.getString("loginId"));
					dto.setRoleId(rs.getInt("role_id"));
					dto.setUserType("v");
				}
			} else {
				String query = "select employee.id, employee.usertype, employee.displayName, EmployeeFirstName, personnelNo, loginId, deptName, employeeLastName, EmailAddress,role_id from employee where employee.id=? ";
				stmt = con.prepareStatement(query);
				stmt.setString(1, empid);
				rs = stmt.executeQuery();

				if (rs.next()) {

					dto.setEmployeeID(rs.getString("id"));
					dto.setDisplayName(rs.getString("displayName"));
					dto.setEmployeeFirstName(rs.getString("employeeFirstname"));
					dto.setEmployeeLastName(rs.getString("employeeLastName"));
					dto.setPersonnelNo(rs.getString("personnelNo"));
					dto.setDeptName(rs.getString("deptName"));
					dto.setEmailAddress(rs.getString("EmailAddress"));
					dto.setLoginId(rs.getString("loginId"));
					dto.setUserType(rs.getString("usertype"));
					dto.setRoleId(rs.getInt("role_id"));
				}
			}
		} catch (Exception e) {
			System.out.println("Exception in EmployeeDao getEmployee " + e);

		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(stmt);
			DbConnect.closeConnection(con);
		}

		return dto;
	}

	public int insertExternalEmployeefromexcel(ArrayList<EmployeeDto> list,
			String site, String overwrite, String sendMail) {
		EmployeeDto lastEmployee = null;
		DbConnect ob = DbConnect.getInstance();
		Connection con = null;
		PreparedStatement st = null;
		PreparedStatement checkEmpSt = null;
		PreparedStatement updateEmpSt = null;
		PreparedStatement checkEmpSt1 = null;
		PreparedStatement updateLineManagerSt = null;
		PreparedStatement checkProjectSt = null;
		PreparedStatement insertProjectSt = null;
		ResultSet checkEmpRs = null;
		ResultSet checkEmpRs1 = null;
		ResultSet checkProjectRs = null;
		int mailflag = 0;
		int returnInt = 0;
		int lineManagerUpdateCount = 0;
		boolean exception = false;
		boolean proceed = true;
		boolean proceedLmUpdate = true;
		String messageForLineManagerUpdate = "";
		String subject = "You have been added to ATOm application";
		System.out.println("Size of the list is " + list.size());
		ArrayList<EmployeeDto> lineManagerUpdateList = new ArrayList<EmployeeDto>();

		try {
			con = ob.connectDB();
			String query = "INSERT INTO EMPLOYEE (EmployeeFirstName,EmployeeMiddleName,employeeLastName,loginId,Gender,PersonnelNo,EmailAddress,Dateofjoining,address,usertype,isContractEmployee,contactNumber1,displayname,authType,externalUser,password,Active,role_id, routingType, site, project, projectUnit ) select ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,  id, ?, ?, ?, ? from roles where usertype=? ";
			String updateQuery = "UPDATE EMPLOYEE e, ROLES r SET e.EmployeeFirstName=?,e.EmployeeMiddleName=?,e.employeeLastName=?,e.loginId=?,e.Gender=?,e.PersonnelNo=?,e.EmailAddress=?,e.Dateofjoining=?,e.address=?,e.usertype=?,e.isContractEmployee=?,e.contactNumber1=?,e.displayname=?,e.authType=?,e.externalUser=?, e.Active=?,e.role_id=r.id, routingType=?,e.site=?, e.project=ifnull(?,e.project), e.projectUnit=ifnull(?,e.projectUnit) where r.usertype=? and e.personnelNo=? ";
			String checkEmpQuery = "SELECT * FROM employee where personnelNo=?";
			String updateLineManagerQuery = "update employee e1, employee e2 set e1.lineManager=e2.id where e1.personnelNo=? and e2.personnelNo=? ";
			String projectInsertQuery = "insert into atsconnect (project, description, status, projectUnit ) values (?, ?, 'a', ? ) ";
			String projectCheckQuery = "select * from atsconnect where project=?";
			checkProjectSt = con.prepareStatement(projectCheckQuery);
			insertProjectSt = con.prepareStatement(projectInsertQuery);

			ArrayList<EmployeeDto> projectList = new ArrayList<EmployeeDto>();
			ArrayList<UserManagementDto> userManagementDtoList = new UserManagementDao()
					.getSystemUsers();
			HashMap<String, Boolean> userMap = new HashMap<String, Boolean>();
			for (UserManagementDto userManagmentDto : userManagementDtoList) {
				userMap.put(userManagmentDto.getUserType(), true);

			}

			st = con.prepareStatement(query);
			checkEmpSt = con.prepareStatement(checkEmpQuery);
			updateEmpSt = con.prepareStatement(updateQuery);
			
			for (EmployeeDto dto : list) {
				
				if (proceed) {
					System.out.println("Inserting emp record for "
							+ dto.getLoginId());

					mailflag = 0;
					/* Usertype check */
					if (userMap.get(dto.getUserType()) == null
							|| userMap.get(dto.getUserType()) != true) {
						proceed = false;
						setMessage("Usertype (" + dto.getUserType()
								+ ") is invalid.");

					}
					/* Employee check */

					

					checkEmpSt.setString(1, dto.getPersonnelNo());
					checkEmpRs = checkEmpSt.executeQuery();
					System.out.println(" .. 01");
					if (checkEmpRs.next() && proceed) {
						System.out.println(" .. 01.01");
						if (overwrite.trim().equals("true") && proceed) {
							System.out.println(" .. 01.01.01");
							// update code
							
							int updateStep=0;
							updateEmpSt
									.setString(++updateStep, dto.getEmployeeFirstName());
							updateEmpSt.setString(++updateStep,
									dto.getEmployeeMiddleName());
							updateEmpSt.setString(++updateStep, dto.getEmployeeLastName());
							updateEmpSt.setString(++updateStep, dto.getLoginId());
							updateEmpSt.setString(++updateStep, dto.getGender());
							updateEmpSt.setString(++updateStep, dto.getPersonnelNo());
							updateEmpSt.setString(++updateStep, dto.getEmailAddress());
							updateEmpSt.setString(++updateStep, dto.getDateOfJoining());
							updateEmpSt.setString(++updateStep, dto.getAddress());
							updateEmpSt.setString(++updateStep, dto.getUserType());
							updateEmpSt.setString(++updateStep, dto.getContract());
							updateEmpSt.setString(++updateStep, dto.getContactNo());
							updateEmpSt.setString(++updateStep, dto.getDisplayName());
							updateEmpSt.setString(++updateStep, dto.getAuthtype());
							updateEmpSt.setString(++updateStep, "Yes");
						 
						 
							updateEmpSt.setInt(++updateStep, 1);
							updateEmpSt.setString(++updateStep, dto.getRoutingType());
							updateEmpSt.setString(++updateStep, site);
							
							updateEmpSt.setString(++updateStep, dto.getProjectCode());
							updateEmpSt.setString(++updateStep, dto.getProjectUnit());
							updateEmpSt.setString(++updateStep, dto.getUserType()
									.toLowerCase());
							updateEmpSt.setString(++updateStep, dto.getPersonnelNo());
							mailflag = updateEmpSt.executeUpdate();
							returnInt = mailflag + returnInt;
							if (mailflag == 1) {
								lastEmployee = dto;
								lineManagerUpdateList.add(dto);
								if(sendMail.equalsIgnoreCase("on")) {
									String message = SettingsService
											.getAddExternalEmployeeMessage(dto);
									SendMail mail = SendMailFactory
											.getMailInstance();
									String bcc[] = { "manjula@agiledgesolutions.com" };
									mail.setBccs(bcc);
									mail.send(dto.getEmailAddress(), subject,
										message);
								}
							} else {
								proceed = false;
								setMessage("Error on employee ("
										+ dto.getPersonnelNo() + " ) ");
							}

						} else {
							proceed = false;
							setMessage("|Employee (" + dto.getPersonnelNo()
									+ ") duplication !");
						}
					} else if (proceed) {
						System.out.println(" .. 02");
						
						int insertStep =0;
						st.setString(++insertStep, dto.getEmployeeFirstName());
						st.setString(++insertStep, dto.getEmployeeMiddleName());
						st.setString(++insertStep, dto.getEmployeeLastName());
						st.setString(++insertStep, dto.getLoginId());
						st.setString(++insertStep, dto.getGender());
						st.setString(++insertStep, dto.getPersonnelNo());
						st.setString(++insertStep, dto.getEmailAddress());
						st.setString(++insertStep, dto.getDateOfJoining());
						st.setString(++insertStep, dto.getAddress());
						st.setString(++insertStep, dto.getUserType());
						st.setString(++insertStep, dto.getContract());
						st.setString(++insertStep, dto.getContactNo());
						st.setString(++insertStep, dto.getDisplayName());
						System.out.println("03");
						st.setString(++insertStep, dto.getAuthtype().toLowerCase());

						st.setString(++insertStep, "Yes");
						String password = new PasswordGenerator()
								.randomString(10);
						dto.setPassword(password);
						st.setString(++insertStep, password);
						st.setInt(++insertStep, 1);
						System.out.println("04");
						st.setString(++insertStep, dto.getRoutingType());
						st.setString(++insertStep, site);
						System.out.println(" .. 05");
						st.setString(++insertStep, dto.getProjectCode());
						st.setString(++insertStep, dto.getProjectUnit());
						st.setString(++insertStep, dto.getUserType().toLowerCase());
						System.out.println(" .. 06");
						mailflag = st.executeUpdate();
						returnInt = mailflag + returnInt;
						if (mailflag == 1) {
							
							lastEmployee = dto;
							lineManagerUpdateList.add(dto);
							if(sendMail.equalsIgnoreCase("on")) {
								String message = SettingsService
										.getAddExternalEmployeeMessage(dto);
								SendMail mail = SendMailFactory.getMailInstance();
								String bcc[] = { "noufal@agiledgesolutions.com" };
								mail.setBccs(bcc);
								mail.send(dto.getEmailAddress(), subject, message);
							}
						} else {
							proceed = false;
							setMessage("Error on employee ("
									+ dto.getPersonnelNo() + " ) ");
						}
					}
					
					if(OtherFunctions.isEmpty( dto.getProject()) == false ) {
						System.out.println(" project added .");
						projectList.add(dto);
					}
					
				} else {
					break;
				}
				
				
			}

			// Line Manager Update
			if (returnInt == list.size()) {
				System.out.println("Employee insertion success...");
			} else {
				System.out.println("Employee insertion skipped from line :  "
						+ returnInt);
			}
			try {
				checkEmpSt1 = con.prepareStatement(checkEmpQuery);
				for (EmployeeDto empDto : lineManagerUpdateList) {
					if (proceedLmUpdate) {
						
						checkEmpSt1.setString(1, empDto.getLineManager());
						checkEmpRs1 = checkEmpSt1.executeQuery();
						if (checkEmpRs1.next()) {
							updateLineManagerSt = con
									.prepareStatement(updateLineManagerQuery);
							updateLineManagerSt.setString(1,
									empDto.getPersonnelNo());
							updateLineManagerSt.setString(2,
									empDto.getLineManager());
							int val = updateLineManagerSt.executeUpdate();
							lineManagerUpdateCount = lineManagerUpdateCount++;
							if (val == 0) {
								proceedLmUpdate = false;
								messageForLineManagerUpdate = "|Error on updating lineManager("
										+ empDto.getLineManager()
										+ ") for Employee ("
										+ empDto.getPersonnelNo() + ") ";

							}
						} else {
							lineManagerUpdateCount++;
						}
					} else {
						break;
					}
				}
			} catch (Exception e) {
				messageForLineManagerUpdate = "Error in LineManager Update :"
						+ e;
				proceedLmUpdate = false;

			}
			if (lineManagerUpdateCount == returnInt) {
				System.out
						.println("All Inserted employees area updated with line manager");
			} else {
				System.out.println("Skipped line manager update from line "
						+ lineManagerUpdateCount);
			}
			
			if(projectList.size() >0 ) {
				for(EmployeeDto dto : projectList) {
					checkProjectSt.setString(1,  dto.getProjectCode());
					checkProjectRs = checkProjectSt.executeQuery();
					if(checkProjectRs.next()==false) {
						insertProjectSt.setString(1,  dto.getProjectCode());
						insertProjectSt.setString(2,  dto.getProject());
						insertProjectSt.setString(3, dto.getProjectUnit());
						insertProjectSt.executeUpdate();
					}
				}
			}

		} catch (Exception e) {
			exception = true;
			try {
				setMessage(e.getMessage());
				con.rollback();
			} catch (Exception ignore) {

			}
			e.printStackTrace();
		} finally {
			DbConnect.closeResultSet(checkEmpRs, checkEmpRs1);
			DbConnect.closeStatement(st, checkEmpSt, updateEmpSt, checkEmpSt1,
					updateLineManagerSt);
			DbConnect.closeConnection(con);

		}
		if (returnInt == list.size()) {
			setMessage("Employee insertion successful.");
		} else if (returnInt < list.size() && returnInt > 0 && proceed == false) {
			setMessage("Insertion is partially successful.| Last Employee inserted is (Employee#: "
					+ lastEmployee.getPersonnelNo()
					+ ") | Error :|"
					+ getMessage());

		}
		if (lineManagerUpdateCount == returnInt) {
			setMessage(getMessage()
					+ "Line managers for all inserted employees are updated succefully");
		} else if (lineManagerUpdateCount < returnInt
				&& proceedLmUpdate == false) {
			setMessage(getMessage() + "|" + messageForLineManagerUpdate);
		}

		return returnInt;
	}

	public int updateExternalEmployeefromexcel(ArrayList<EmployeeDto> list,
			String site, String overwrite) {
	
		DbConnect ob = DbConnect.getInstance();
		Connection con = null;
		PreparedStatement updateEmp = null;
		PreparedStatement checkProjectSt = null;
		PreparedStatement insertProjectSt = null;
		ResultSet checkProjectRs = null;
		int returnInt = 0;
		
		System.out.println("Size of the list is " + list.size());
	
		try {
			con = ob.connectDB();
			
			
			
			String empProjectUpdate="UPDATE employee SET project=?, projectUnit=? WHERE PersonnelNo=? ";
			String projectInsertQuery = "insert into atsconnect (project, description, status, projectUnit ) values (?, ?, 'a', ? ) ";
			String projectCheckQuery = "select * from atsconnect where project=?";
			
			
			
         	updateEmp=con.prepareStatement(empProjectUpdate);
			checkProjectSt = con.prepareStatement(projectCheckQuery);
			insertProjectSt = con.prepareStatement(projectInsertQuery);

			

			
			for (EmployeeDto dto : list) {
				if( dto.getProjectCode().length()>0 && dto.getProjectCode()!=null && dto.getProject()!=null && dto.getProjectUnit()!=null)
				{	
				checkProjectSt.setString(1,  dto.getProjectCode());
				checkProjectRs = checkProjectSt.executeQuery();
				if(checkProjectRs.next()==false )  {
					insertProjectSt.setString(1,  dto.getProjectCode());
					insertProjectSt.setString(2,  dto.getProject());
					insertProjectSt.setString(3, dto.getProjectUnit());
					insertProjectSt.executeUpdate();
				}
				
				updateEmp.setString(1, dto.getProjectCode());
				updateEmp.setString(2, dto.getProjectUnit());
				updateEmp.setString(3, dto.getPersonnelNo());
				
				returnInt+=updateEmp.executeUpdate();
				
			}else{
				returnInt++;	}
			}
								

		} catch (Exception e) {	setMessage(e.getMessage());
		
		
					} finally {
			DbConnect.closeResultSet(checkProjectRs);
			DbConnect.closeStatement(insertProjectSt, updateEmp, checkProjectSt);
			DbConnect.closeConnection(con);

		}
		return returnInt;
	}

	public int updateStatus(String empid, int active) {
		DbConnect ob = DbConnect.getInstance();
		Connection con = null;
		Statement st = null;
		int result = 0;
		try {
			if (active == 0)
				active = 1;
			else
				active = 0;
			String query = "UPDATE EMPLOYEE SET ACTIVE=" + active
					+ " WHERE ID=" + Integer.parseInt(empid);
			System.out.println(query);
			con = ob.connectDB();
			st = con.createStatement();
			result = st.executeUpdate(query);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);

		}
		return result;
	}

	public int UpdatePassword(int empid, String password) {
		DbConnect ob = DbConnect.getInstance();
		Connection con = null;
		PreparedStatement pst = null;
		int returnint = 0;
		try {
			con = ob.connectDB();
			String query = "UPDATE EMPLOYEE SET PASSWORD=?,PWDCHANGEDDATE=NULL WHERE ID=?";
			pst = con.prepareStatement(query);
			pst.setString(1, password);
			pst.setInt(2, empid);
			returnint = pst.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);
		}
		return returnint;
	}

	public int UpdateEmployee(EmployeeDto dto) {
		DbConnect ob = DbConnect.getInstance();
		Connection con = null;
		PreparedStatement pst = null;
		int returnint = 0;
		try {
			con = ob.connectDB();

			String query = "UPDATE EMPLOYEE SET EMPLOYEEFIRSTNAME=?,EMPLOYEEMIDDLENAME=?,EMPLOYEELASTNAME=?,DISPLAYNAME=?,CONTACTNUMBER1=?,EMAILADDRESS=?,ADDRESS=?,GENDER=?,AUTHTYPE=?,USERTYPE=?,PROJECT=(select project from atsconnect where id=?),LINEMANAGER=?,PROJECTUNIT=(select projectunit from atsconnect where id=?),ROLE_ID =(select id from roles where usertype=?) WHERE personnelNo=?";
			String displayQuery = query.replace("?", "'%s'");
			/*
			 * displayQuery =
			 * String.format(displayQuery,dto.getEmployeeFirstName(),
			 * dto.getEmployeeMiddleName(), dto.getEmployeeLastName(),
			 * dto.getDisplayName(), dto.getContactNo(), dto.getEmailAddress(),
			 * dto.getAddress(), dto.getGender(), dto.getAuthtype(),
			 * dto.getUserType(),dto.getEmployeeID());
			 * System.out.println("DIsplay query : " + displayQuery);
			 */
			System.out.println("pro"+dto.getProject());
			System.out.println("lm"+dto.getLineManager());
			pst = con.prepareStatement(query);
			pst.setString(1, dto.getEmployeeFirstName());
			pst.setString(2, dto.getEmployeeMiddleName());
			pst.setString(3, dto.getEmployeeLastName());
			pst.setString(4, dto.getDisplayName());
			pst.setString(5, dto.getContactNo());
			pst.setString(6, dto.getEmailAddress());
			pst.setString(7, dto.getAddress());
			pst.setString(8, dto.getGender());
			pst.setString(9, dto.getAuthtype());
			pst.setString(10, dto.getUserType());
			pst.setString(11, dto.getProject());
			pst.setString(12, dto.getLineManager());
			pst.setString(13, dto.getProject());
			pst.setString(14, dto.getUserType());
			
			System.out.println(dto.getPersonnelNo() + "jhghg");
			pst.setString(15, dto.getPersonnelNo());
			returnint = pst.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);
		}
		System.out.println(returnint);
		return returnint;
	}

	public ArrayList<EmployeeDto> getEnabledExternalemployees() {
		DbConnect ob = DbConnect.getInstance();
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		ArrayList<EmployeeDto> list = new ArrayList<EmployeeDto>();
		try {
			con = ob.connectDB();
			st = con.createStatement();

			rs = st.executeQuery("SELECT e.*,m.displayname AS managername,at.id as projectid  FROM EMPLOYEE e LEFT JOIN employee m ON m.id = e.LineManager LEFT JOIN ATSCONNECT AT ON AT.PROJECT=E.PROJECT  WHERE e.EXTERNALUSER = 'Yes' AND e.active = 1 ORDER BY personnelNo");

			while (rs.next()) {
				EmployeeDto dto = new EmployeeDto();
				dto.setEmployeeID(rs.getString("id"));
				dto.setPersonnelNo(rs.getString("PersonnelNo"));
				dto.setEmployeeFirstName(rs.getString("EmployeeFirstName"));
				dto.setEmployeeMiddleName(rs.getString("EmployeeMiddleName"));
				dto.setEmployeeLastName(rs.getString("employeeLastName"));
				dto.setDisplayName(rs.getString("displayname"));
				dto.setLoginId(rs.getString("loginId"));
				dto.setContactNo(rs.getString("contactNumber1"));
				dto.setGender(rs.getString("gender"));
				dto.setEmailAddress(rs.getString("EmailAddress"));
				dto.setAuthtype(rs.getString("authType"));
				dto.setActive(rs.getString("Active"));
				dto.setAddress(rs.getString("address"));
				dto.setUserType(rs.getString("usertype"));
				dto.setProject(rs.getString("project"));
				dto.setLineManager(rs.getString("LineManager"));
				dto.setManagerName(rs.getString("managername"));
				dto.setProjectid(rs.getString("projectid"));
				list.add(dto);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);

		}
		return list;

	}

	public List<EmployeeDto> searchspoc(EmployeeDto searchEmployee, Long man_id) {
		List<EmployeeDto> dtoList = new ArrayList<EmployeeDto>();

		DbConnect ob = DbConnect.getInstance();
		boolean nameFlag = false;
		boolean emailFlag = false;
		boolean idflag = false;
		String query = "select id, EmployeeFirstName, EmployeeLastName,personnelNo, address, EmailAddress, DeptName , case usertype when 'hrm' then 'Manager/SPOC' when 'tm' then 'Transport Co-ordinator'  when 'ta' then 'Transport Manager'  when 'v' then 'Vendor SuperVisor'  else    'Employee'  end as usertype from employee where LineManager="
				+ man_id;
		String subQuery = "";

		if (searchEmployee.getEmployeeFirstName() != null
				&& !searchEmployee.getEmployeeFirstName().equals("")) {
			subQuery = subQuery + " and EmployeeFirstName like ? ";
			nameFlag = true;
		}

		if (searchEmployee.getEmployeeID() != null
				&& !searchEmployee.getEmployeeID().equals("")) {
			subQuery = subQuery + " and  PersonnelNo =? ";
			idflag = true;
		}

		if (searchEmployee.getEmployeeLastName() != null
				&& !searchEmployee.getEmployeeLastName().equals("")) {
			subQuery = subQuery + " and ";
			subQuery = subQuery + " employeeLastName like ? ";
			emailFlag = true;
		}

		if (!subQuery.equals("")) {
			query = query + " " + subQuery;
		}
		System.out.println(query);
		Connection con = ob.connectDB();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = con.prepareStatement(query);
			int i = 1;
			if (nameFlag) {
				stmt.setString(i, "%" + searchEmployee.getEmployeeFirstName()
						+ "%");
				i++;
			}
			if (idflag) {
				stmt.setString(i, searchEmployee.getEmployeeID());
				i++;
			}
			if (emailFlag) {
				stmt.setString(i, "%" + searchEmployee.getEmployeeLastName()
						+ "%");
				i++;
			}

			rs = stmt.executeQuery();

			while (rs.next()) {
				EmployeeDto dto = new EmployeeDto();
				dto.setEmployeeFirstName(rs.getString("EmployeeFirstName"));
				dto.setEmployeeLastName(rs.getString("EmployeeLastName"));
				dto.setPersonnelNo(rs.getString("personnelNo"));
				dto.setEmailAddress(rs.getString("emailAddress"));
				dto.setDeptName(rs.getString("deptName"));
				dto.setEmployeeID(rs.getString("id"));
				dto.setUserType(rs.getString("usertype"));
				dto.setAddress(rs.getString("address"));

				dtoList.add(dto);

			}

		} catch (Exception e) {

			System.out.println("Exception in EmployeeDao Searchspoc " + e);

		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(stmt);
			DbConnect.closeConnection(con);
		}

		return dtoList;

	}

	public int registerEmployee(String empId) {
		DbConnect ob = DbConnect.getInstance();
		Connection con = null;
		PreparedStatement pst = null;
		int returnint = 0;
		String query = "Update employee set registerStatus='"
				+ SettingsConstant.ACTIVE_STATUS + "' where id='" + empId + "'";
		try {
			con = ob.connectDB();
			pst = con.prepareStatement(query);

			returnint = pst.executeUpdate();
		} catch (Exception e) {
			System.out.println("Error" + e);
		} finally {
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);
		}

		return returnint;
	}

	/*
	 * 
	 * public int registerEmployee(ArrayList<TransportTypeDto> list) { DbConnect
	 * ob = DbConnect.getInstance(); Connection con = null; PreparedStatement
	 * pst = null; int returnint=0; String
	 * query="Update employee set registerStatus='"
	 * +SettingsConstant.ACTIVE_STATUS
	 * +"' where id="+list.get(0).getEmployeeId(); String query1=
	 * "insert into registerTransport(employeeId,transportType,reguestedDate,landmark,status) values (?,?,now(),?,?)"
	 * ; try{ con=ob.connectDB(); pst=con.prepareStatement(query);
	 * pst.executeUpdate(); pst=con.prepareStatement(query1);
	 * for(TransportTypeDto dto:list) {
	 * 
	 * pst.setString(1, dto.getEmployeeId()); pst.setString(2,
	 * dto.getTransportType());
	 * System.out.println("landmark "+dto.getLandmarkId());
	 * if(dto.getLandmarkId()==null) { System.out.println("landmark null");
	 * pst.setString(3,null); } else { pst.setString(3, dto.getLandmarkId()); }
	 * pst.setString(4, SettingsConstant.ACTIVE_STATUS);
	 * returnint+=pst.executeUpdate(); }
	 * 
	 * 
	 * }catch(Exception e) { System.out.println("Error"+e); } finally {
	 * DbConnect.closeStatement(pst); DbConnect.closeConnection(con); }
	 * 
	 * return returnint; }
	 */
	public EmployeeDto getEmployeeFullInfoByEmpId(String empId) {
		PreparedStatement st1 = null;
		ResultSet rs1 = null;

		try {
			rs1 = null;
			st1 = null;
			con = DbConnect.getInstance().connectDB();
			System.out.println(empId);
			EmployeeDto dtobj = new EmployeeDto();
			st1 = con
					.prepareStatement("select e.*,s.site_name,e1.displayname as manager,e.project as projectCode,e.projectUnit proUnit  from employee e left join site s on e.site=s.id left join employee e1 on e.lineManager=e1.id  where  e.Id='"
							+ empId + "' ");
			rs1 = st1.executeQuery();
			if (rs1.next()) {

				dtobj.setEmployeeID(rs1.getString("id"));
				dtobj.setEmployeeFirstName(rs1.getString("EmployeeFirstName"));
				dtobj.setEmployeeLastName(rs1.getString("EmployeeLastName"));
				dtobj.setLoginId(rs1.getString("LoginId"));
				dtobj.setGender(rs1.getString("Gender"));
				dtobj.setPersonnelNo(rs1.getString("PersonnelNo"));
				dtobj.setEmailAddress(rs1.getString("EmailAddress"));
				dtobj.setHomeCountry(rs1.getString("HomeCountry"));
				dtobj.setLineManager(rs1.getString("manager"));
				dtobj.setDeptno(rs1.getString("Deptno"));
				dtobj.setDeptName(rs1.getString("DeptName"));
				dtobj.setOperationCode(rs1.getString("OperationCode"));
				dtobj.setOperationDescription(rs1
						.getString("OperationDescription"));
				dtobj.setPathways(rs1.getString("Pathways"));
				dtobj.setDateOfJoining(rs1.getString("Dateofjoining"));
				dtobj.setActive(rs1.getString("Active"));
				dtobj.setAddress(rs1.getString("address"));
				dtobj.setCity(rs1.getString("City"));
				dtobj.setContactNo(rs1.getString("contactNumber1"));
				dtobj.setEmpType(rs1.getString("isContractEmployee"));
				dtobj.setDisplayName(rs1.getString("displayname"));
				dtobj.setSite(rs1.getString("site_name"));
				dtobj.setProjectid(rs1.getString("projectCode"));
				dtobj.setProjectUnit(rs1.getString("proUnit"));
			}
			return dtobj;
		} catch (Exception e) {
			System.out.println("errro" + e);
			// e.printStackTrace();
		} finally {
			DbConnect.closeResultSet(rs1);
			DbConnect.closeStatement(st1);
			DbConnect.closeConnection(con);
		}

		System.out.println("get employee with loginid ended");
		return null;

	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void autoChangeRole() {

		if (GetEmps.sourceFlag.equalsIgnoreCase("cd")) {

			DbConnect ob = DbConnect.getInstance();

			String query1 = " update employee e, roles r, roleDesignationMapping rm set e.usertype=r.usertype, e.role_id=r.id where e.designationName =rm.designation and rm.usertype=r.usertype and ( e.autoRoleUpdate is null or e.autoRoleUpdate !='false' ) and ( externalUser is null or externalUser!='false' ) ";
			// String query2 =
			// " update employee e, roles r set e.usertype=r.usertype, e.role_id=r.id where e.designationName like 'PROCESS MANAGER%' AND r.usertype='PM'  and ( e.autoRoleUpdate is null or e.autoRoleUpdate !='false' ) ";

			Connection con = null;
			con = ob.connectDB();
			PreparedStatement stmt1 = null;
			// PreparedStatement stmt2 = null;
			ResultSet rs = null;
			try {
				stmt1 = con.prepareStatement(query1);
				// stmt2 = con.prepareStatement(query2);

				stmt1.executeUpdate();
				// stmt2.executeUpdate();

			} catch (Exception e) {
				System.out.println("Exception in autochange role getSpoc " + e);

			} finally {
				DbConnect.closeResultSet(rs);
				DbConnect.closeStatement(stmt1);
				DbConnect.closeConnection(con);
			}

		}

	}

	public ArrayList<EmployeeDto> getAllDirectSubordinate(String empId) {
		// TODO Auto-generated method stub
		ArrayList<EmployeeDto> employeeList = new ArrayList<EmployeeDto>();
		PreparedStatement st1 = null;
		ResultSet rs1 = null;

		try {
			rs1 = null;
			st1 = null;
			con = DbConnect.getInstance().connectDB();
			System.out.println(empId);

			st1 = con
					.prepareStatement("select e.*,s.site_name,e1.displayname as manager,e.project as projectCode,e.projectUnit proUnit  from employee e left join site s on e.site=s.id left join employee e1 on e.lineManager=e1.id  where  e.lineManager='"
							+ empId + "' order by e.displayname ");
			System.out
					.println("Query : select e.*,s.site_name,e1.displayname as manager,e.project as projectCode,e.projectUnit proUnit  from employee e left join site s on e.site=s.id left join employee e1 on e.lineManager=e1.id  where  e.lineManager='"
							+ empId + "' ");
			rs1 = st1.executeQuery();
			while (rs1.next()) {
				EmployeeDto dtobj = new EmployeeDto();
				dtobj.setEmployeeID(rs1.getString("id"));
				dtobj.setEmployeeFirstName(rs1.getString("EmployeeFirstName"));
				dtobj.setEmployeeLastName(rs1.getString("EmployeeLastName"));
				dtobj.setLoginId(rs1.getString("LoginId"));
				dtobj.setGender(rs1.getString("Gender"));
				dtobj.setPersonnelNo(rs1.getString("PersonnelNo"));
				dtobj.setEmailAddress(rs1.getString("EmailAddress"));
				dtobj.setHomeCountry(rs1.getString("HomeCountry"));
				dtobj.setLineManager(rs1.getString("manager"));
				dtobj.setDeptno(rs1.getString("Deptno"));
				dtobj.setDeptName(rs1.getString("DeptName"));
				dtobj.setOperationCode(rs1.getString("OperationCode"));
				dtobj.setOperationDescription(rs1
						.getString("OperationDescription"));
				dtobj.setPathways(rs1.getString("Pathways"));
				dtobj.setDateOfJoining(rs1.getString("Dateofjoining"));
				dtobj.setActive(rs1.getString("Active"));
				dtobj.setAddress(rs1.getString("address"));
				dtobj.setCity(rs1.getString("City"));
				dtobj.setContactNo(rs1.getString("contactNumber1"));
				dtobj.setEmpType(rs1.getString("isContractEmployee"));
				dtobj.setDisplayName(rs1.getString("displayname"));
				dtobj.setSite(rs1.getString("site_name"));
				dtobj.setProjectid(rs1.getString("projectCode"));
				dtobj.setProjectUnit(rs1.getString("proUnit"));
				employeeList.add(dtobj);
			}
			return employeeList;
		} catch (Exception e) {
			System.out.println("Error in employee getAllDirectSubordinate : "
					+ e);
			// e.printStackTrace();
		} finally {
			DbConnect.closeResultSet(rs1);
			DbConnect.closeStatement(st1);
			DbConnect.closeConnection(con);
		}

		return employeeList;
	}

	public EmployeeDto getEmployeeToSubscribe(String empid) {
		EmployeeDto dto = new EmployeeDto();
		DbConnect ob = DbConnect.getInstance();

		String query = "select e1.id as managerid,e1.displayname as managerName,ifnull(e2.id,'') as spocId,ifnull(e2.displayname,'') as spocName, e.contactNumber1, e.id, e.usertype, r.id roleId, e.displayName, e.EmployeeFirstName, e.personnelNo, e.loginId, e.deptName, e.employeeLastName, e.EmailAddress,e.password,e.site,e.projectUnit,e.registerStatus,e.designationName from roles r, employee e left join spoc_child sc on e.id=sc.employee_id left join spoc_parent sp on  sc.spoc_id=sp.id left join employee e2 on sp.employee_id=e2.id left join employee e1 on e.linemanager=e1.id  where e.usertype=r.usertype  and  e.id=? and e.id not in (select empid from employee_subscription where subscriptionstatus in ('pending','subscribed')) ";
		// System.out.println(query+"  "+empid);
		Connection con = ob.connectDB();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = con.prepareStatement(query);
			stmt.setString(1, empid);
			rs = stmt.executeQuery();
			if (rs.next()) {
				dto.setEmployeeID(rs.getString("id"));
				dto.setDisplayName(rs.getString("displayName"));
				dto.setEmployeeFirstName(rs.getString("employeeFirstname"));
				dto.setEmployeeLastName(rs.getString("employeeLastName"));
				dto.setPersonnelNo(rs.getString("personnelNo"));
				dto.setDeptName(rs.getString("deptName"));
				dto.setEmailAddress(rs.getString("EmailAddress"));
				dto.setLoginId(rs.getString("loginId"));
				dto.setUserType(rs.getString("usertype"));
				dto.setPassword(rs.getString("password"));
				dto.setSite(rs.getString("site"));
				dto.setRoleId(rs.getInt("roleId"));
				dto.setProjectUnit(rs.getString("projectUnit"));
				dto.setRegisterStatus(rs.getString("registerStatus"));
				dto.setLineManager(rs.getString("managerid"));
				dto.setManagerName(rs.getString("managerName"));
				dto.setSpoc_id(rs.getString("spocId"));
				dto.setSpocName(rs.getString("spocName"));
				dto.setDesignationName(rs.getString("designationName"));
				dto.setContactNo(rs.getString("contactNumber1"));
				System.out.println("CONTACT NUMBER 1 : " + dto.getContactNo());
			}
		} catch (Exception e) {
			System.out.println("Exception in EmployeeDao getEmployee " + e);

		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(stmt);
			DbConnect.closeConnection(con);
		}
		System.out.println("Is registered" + dto.getRegisterStatus());
		return dto;
	}

	public int UpdatePassword(String email, String password) {
		DbConnect ob = DbConnect.getInstance();
		Connection con = null;
		PreparedStatement pst = null;
		int returnint = 0;
		try {
			con = ob.connectDB();
			String query = "UPDATE EMPLOYEE SET PASSWORD=?,PWDCHANGEDDATE=NULL WHERE EmailAddress=?";
			pst = con.prepareStatement(query);
			pst.setString(1, password);
			pst.setString(2, email);
			returnint = pst.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);
		}
		return returnint;
	}

	public EmployeeDto getEmployeeByEmail(String email) {
		EmployeeDto dto = new EmployeeDto();
		DbConnect ob = DbConnect.getInstance();

		String query = "select e1.id as managerid,e1.displayname as managerName,e2.id as spocId,e2.displayname as spocName, e.contactNumber1, e.id, e.usertype, r.id roleId, e.displayName, e.EmployeeFirstName, e.personnelNo, e.loginId, e.deptName, e.employeeLastName, e.EmailAddress,e.password,e.site,e.projectUnit,e.registerStatus,e.designationName from roles r, employee e left join spoc_child sc on e.id=sc.employee_id left join spoc_parent sp on  sc.spoc_id=sp.id left join employee e2 on sp.employee_id=e2.id left join employee e1 on e.linemanager=e1.id  where e.usertype=r.usertype  and  e.EmailAddress=? ";
		// System.out.println(query+empid);
		Connection con = ob.connectDB();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = con.prepareStatement(query);
			stmt.setString(1, email);
			rs = stmt.executeQuery();
			if (rs.next()) {
				dto.setEmployeeID(rs.getString("id"));
				dto.setDisplayName(rs.getString("displayName"));
				dto.setEmployeeFirstName(rs.getString("employeeFirstname"));
				dto.setEmployeeLastName(rs.getString("employeeLastName"));
				dto.setPersonnelNo(rs.getString("personnelNo"));
				dto.setDeptName(rs.getString("deptName"));
				dto.setEmailAddress(rs.getString("EmailAddress"));
				dto.setLoginId(rs.getString("loginId"));
				dto.setUserType(rs.getString("usertype"));
				dto.setPassword(rs.getString("password"));
				dto.setSite(rs.getString("site"));
				dto.setRoleId(rs.getInt("roleId"));
				dto.setProjectUnit(rs.getString("projectUnit"));
				dto.setRegisterStatus(rs.getString("registerStatus"));
				dto.setLineManager(rs.getString("managerid"));
				dto.setManagerName(rs.getString("managerName"));
				dto.setSpoc_id(rs.getString("spocId"));
				dto.setSpocName(rs.getString("spocName"));
				dto.setDesignationName(rs.getString("designationName"));
				dto.setContactNo(rs.getString("contactNumber1"));
				System.out.println("CONTACT NUMBER 1 : " + dto.getContactNo());
			}
		} catch (Exception e) {
			System.out.println("Exception in EmployeeDao getEmployee " + e);

		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(stmt);
			DbConnect.closeConnection(con);
		}

		return dto;

	}
	
	public String getDisplayNamebyId(String id) {
		DbConnect ob = DbConnect.getInstance();
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		String result="";
		try {
			con = ob.connectDB();
			st = con.createStatement();
			rs = st.executeQuery("SELECT * FROM EMPLOYEE WHERE ID='"+id+"'");
			while (rs.next()) {
				result=rs.getString("displayname");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);

		}
		return result;
	}
	
	public int UpdateNoShowConfig(String type, String count,String days) {
		DbConnect ob = DbConnect.getInstance();
		Connection con = null;
		PreparedStatement pst = null;
		int returnint = 0;
		try {
			con = ob.connectDB();
			String query = "UPDATE NOSHOW_CONFIG SET COUNT=?,DAYS=? WHERE TYPE=?";
			pst = con.prepareStatement(query);
			pst.setString(1, count);
			pst.setString(2, days);
			pst.setString(3, type);
			returnint = pst.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);
		}
		return returnint;
	}
	public EmployeeDto getNoShowConfig(String type) {
		DbConnect ob = DbConnect.getInstance();
		Connection con = null;
		PreparedStatement pst = null;
		ResultSet rs=null;
		EmployeeDto dto = new EmployeeDto();
		try {
			con = ob.connectDB();
			String query = "SELECT * FROM NOSHOW_CONFIG WHERE TYPE=?";
			pst = con.prepareStatement(query);
			pst.setString(1, type);
			rs = pst.executeQuery();
			if(rs.next())
			{
				dto.setNoshowcount(rs.getString("count"));
				dto.setNoshowdays(rs.getString("days"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);
		}
		return dto;
	}
	
	public ArrayList<EmployeeDto> NoshowEmps()
	{
		DbConnect ob = null;
		EmployeeDto setdto=getNoShowConfig("email");
		ArrayList<EmployeeDto> list =new ArrayList<EmployeeDto>();
		String query = "select e.id,e.personnelno,e.displayname,e.emailaddress,count(e.id) Noshowcount,e1.emailaddress as spocmail from employee e join vendor_trip_sheet vts on vts.employeeid=e.id join vendor_trip_sheet_parent vtp on vts.tripid=vtp.tripid and vtp.status in ('started','stopped') join trip_details td on td.id=vtp.tripid and td.trip_date between DATE_SUB(CURDATE(), INTERVAL "+setdto.getNoshowdays()+" DAY) and CURDATE()   and td.status in ('saved','saveedit','addsave') join employee_subscription es on es.empid=e.id and es.subscriptionstatus='subscribed' join employee e1 on e1.id=es.spoc where vts.showstatus='No Show'   group by e.id having Noshowcount >= "+setdto.getNoshowcount()+" order by Noshowcount desc";
		System.out.println("no show mail query ********************** "+query);
		Connection con =null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			ob=DbConnect.getInstance();
		    con= ob.connectDB();
			stmt = con.createStatement();
			rs = stmt.executeQuery(query);
			while(rs.next())
			{
				
				EmployeeDto dto = new EmployeeDto();
				dto.setEmployeeID(rs.getString("id"));
				dto.setPersonnelNo(rs.getString("personnelno"));
				dto.setDisplayName(rs.getString("displayname"));
				dto.setEmailAddress(rs.getString("emailaddress"));
				dto.setNoshowcount(rs.getString("noshowcount"));
				dto.setNoshowdays(setdto.getNoshowdays());
				dto.setSpocName(rs.getString("spocmail"));
				list.add(dto);
				
			}
		}catch(Exception e){ System.out.println("error in now show mail "+e);}
		return list;
	}
	public int UpdateProject(String empid, String project) {
		DbConnect ob = DbConnect.getInstance();
		Connection con = null;
		PreparedStatement pst = null;
		int returnint = 0;
		try {
			con = ob.connectDB();
			String query = "UPDATE EMPLOYEE SET PROJECTUNIT=(SELECT PROJECTUNIT FROM ATSCONNECT WHERE ID=?),PROJECT=(SELECT PROJECT FROM ATSCONNECT WHERE ID=?) WHERE ID=?";
			pst = con.prepareStatement(query);
			pst.setString(1, project);
			pst.setString(2, project);
			pst.setString(3, empid);
			returnint = pst.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);
		}
		return returnint;
	}
	
	public String[] getEmpLatLong(String empid)
	{
		DbConnect ob = DbConnect.getInstance();
		Connection con = null;
		PreparedStatement pst = null;
		String latlongs[]=new String[2];
		ResultSet rs=null;
		try {
			con = ob.connectDB();
			String query = "SELECT EMP_LAT,EMP_LONG FROM EMPLOYEE WHERE ID=?";
			pst = con.prepareStatement(query);
			pst.setString(1, empid);
			rs = pst.executeQuery();
			if(rs.next()&&rs.getString("EMP_LAT")!=null&&!rs.getString("EMP_LAT").equalsIgnoreCase("")&&rs.getString("EMP_LONG")!=null&&!rs.getString("EMP_LONG").equalsIgnoreCase(""))
			{
				latlongs[0]=rs.getString("EMP_LAT");
				latlongs[1]=rs.getString("EMP_LONG");
			}
			else
			{
				latlongs[0]="0";
				latlongs[1]="0";
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);
		}
		return latlongs;
	}
	
	public int UpdateEmpLatLong(String empid, String latitude,String longitude) {
		DbConnect ob = DbConnect.getInstance();
		Connection con = null;
		PreparedStatement pst = null;
		int returnint = 0;
		try {
			con = ob.connectDB();
			String query = "UPDATE EMPLOYEE SET EMP_LAT=?,EMP_LONG=? WHERE ID=?";
			pst = con.prepareStatement(query);
			pst.setString(1, latitude);
			pst.setString(2, longitude);
			pst.setString(3, empid);
			returnint = pst.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);
		}
		return returnint;
	}
	public ArrayList<EmployeeDto> getEmployeesForReminderMail() {
		DbConnect ob = DbConnect.getInstance();
		Connection con = null;

		Statement st = null;
		ResultSet rs = null;
		ArrayList<EmployeeDto> list = new ArrayList<EmployeeDto>();
		EmployeeDto dto = null;
		try {
			con = ob.connectDB();
			st = con.createStatement();
			rs = st.executeQuery(" select id,displayname,EmailAddress from employee where role_id not in ('1','3') ");

				
			while (rs.next()) {

				dto = new EmployeeDto();
				dto.setEmployeeID(rs.getString("id"));
				dto.setDisplayName(rs.getString("displayname"));
				dto.setEmailAddress(rs.getString("EmailAddress"));
				list.add(dto);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);

		}
		return list;
	}
	public String[] getApl(long uid)
	{
		DbConnect ob = null;
		

		String query = ("select a.area,p.place,l.landmark from employee_subscription es join landmark l on  l.id =es.landmark and es.empid = "+ uid+" join place p on p.id=l.place join area a on a.id=p.area");
		String [] result=new String[3];
		Connection con =null;
		Statement stmt = null;
		ResultSet rs = null;
		
		try {
			ob=DbConnect.getInstance();
		    con= ob.connectDB();
			stmt = con.createStatement();
			rs = stmt.executeQuery(query);
			while(rs.next())
			{String area = rs.getString("area");
			String place = rs.getString("place");
			String landmark = rs.getString("landmark");
				result[0]=area==null?" ":area;
				result[1]=place==null?" ":place;
				result[2]=landmark==null?" ":landmark;

				
				
			}
		}catch(Exception e){System.out.println("error in Noshow report dao"+e);}
		return result;
	}
	
	public ArrayList<EmployeeDto> getSafeTravelAppUsers() {
		DbConnect ob = DbConnect.getInstance();
		Connection con = null;

		Statement st = null;
		ResultSet rs = null;
		ArrayList<EmployeeDto> list = new ArrayList<EmployeeDto>();
		EmployeeDto dto = null;
		try {
			con = ob.connectDB();
			st = con.createStatement();
			rs = st.executeQuery(" select e.displayname,e.personnelno,e.contactnumber1,e.emailaddress,ei.timestamp from employee e join  (select * from employee_imei order by timestamp desc) ei on e.id=ei.empid group by empid order by  timestamp desc ");

			String date;	
			while (rs.next()) {

				dto = new EmployeeDto();
				dto.setDisplayName(rs.getString("displayname"));
	            dto.setPersonnelNo(rs.getString("personnelno"));
				dto.setContactNo(rs.getString("contactnumber1"));
				dto.setEmailAddress(rs.getString("emailaddress"));
				date=rs.getString("timestamp");
			    date= date == null ? "" : date;
                dto.setDate(date.split(" ")[0]);
				list.add(dto);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);

		}
		return list;
	}
	public ArrayList<EmployeeDto> NoshowReport(String fromdate,String todate)
	{
		DbConnect ob = null;
		ArrayList<EmployeeDto> list =new ArrayList<EmployeeDto>();

		fromdate=OtherFunctions.changeDateFromatToIso(fromdate);
		todate=OtherFunctions.changeDateFromatToIso(todate);
		String query = "select e.id,e.personnelno,e.displayname,e.emailaddress,count(e.id) Noshowcount from employee e join vendor_trip_sheet vts on vts.employeeid=e.id join vendor_trip_sheet_parent vtp on vts.tripid=vtp.tripid and vtp.status in ('started','stopped') join trip_details td on td.id=vtp.tripid and td.trip_date between '"+fromdate+"' and '"+todate+"'  and td.status in ('saved','saveedit','addsave')  where vts.showstatus='No Show'   group by e.id having Noshowcount >= 1 order by Noshowcount desc";
		System.out.println("transadmin noshow query ********************** "+query);
		Connection con =null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			ob=DbConnect.getInstance();
		    con= ob.connectDB();
			stmt = con.createStatement();
			rs = stmt.executeQuery(query);
			while(rs.next())
			{
				
				EmployeeDto dto = new EmployeeDto();
				dto.setEmployeeID(rs.getString("id"));
				dto.setPersonnelNo(rs.getString("personnelno"));
				dto.setDisplayName(rs.getString("displayname"));
				dto.setEmailAddress(rs.getString("emailaddress"));
				dto.setNoshowcount(rs.getString("noshowcount"));
				list.add(dto);
				
			}
			System.out.println("***************size"+list.size());
		}catch(Exception e){System.out.println("error in Noshow report dao"+e);}
		return list;
	}
	public ArrayList<EmployeeDto> getSubscriptionRequestUsers() {
		DbConnect ob = DbConnect.getInstance();
		Connection con = null;

		Statement st = null;
		ResultSet rs = null;
		ArrayList<EmployeeDto> list = new ArrayList<EmployeeDto>();
		EmployeeDto dto = null;
	
		
		try {
			con = ob.connectDB();
			st = con.createStatement();
			rs = st.executeQuery(" select displayname,personnelNo,emailAddress,contactnumber1,dateofjoining from employee where id not in (select e.id from employee e join employee_subscription ei on ei.empid=e.id) and emp_lat is not null ");
	
			while (rs.next()) {

				dto = new EmployeeDto();
				
				String date=rs.getString("dateofjoining");
				date=date==null?"":date.split(" ")[0];
				
				dto.setContactNo(rs.getString("contactnumber1"));
				dto.setEmailAddress(rs.getString("emailAddress"));
				dto.setDisplayName(rs.getString("displayname"));
	            dto.setPersonnelNo(rs.getString("personnelNo"));
	            dto.setDateOfJoining(date);
	           
	    		
	            list.add(dto);
		        
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);

		}
		return list;
	}	

	

}
