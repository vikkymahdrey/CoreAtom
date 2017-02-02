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
import com.agiledge.atom.dto.VehicleDto;
import com.agiledge.atom.dto.VendorContractDto;
import com.agiledge.atom.dto.VendorDto;

public class VendorDao {
	
	public ArrayList<VendorDto> getVendorList() {
		ArrayList<VendorDto> dtoList = new ArrayList<VendorDto>();

		String query = "";
		DbConnect ob = DbConnect.getInstance();

		query = "select v.id, v.name, v.loginId, v.password, v.address, v.contactNumber, v.email, ifnull(vc.description,'') description, ifnull(rate,0) rate,v.vendorMaster,vm.company from vendormaster vm, vendor v left join vendor_contract vc on v.id=vc.vendor where v.status='active' and vm.id=v.vendorMaster";

		PreparedStatement stmt = null;
		ResultSet rs = null;

		Connection con = ob.connectDB();
		try {
			stmt = con.prepareStatement(query);
			rs = stmt.executeQuery();
			while (rs.next()) {
				VendorDto dto = new VendorDto();
				VendorContractDto contract = new VendorContractDto();
				contract.setVendor(rs.getString("id"));
				contract.setDescription(rs.getString("description"));
				contract.setRate(rs.getString("rate"));
				dto.setContract(contract);
				dto.setId(rs.getString("id"));
				dto.setName(rs.getString("name"));
				dto.setLoginId(rs.getString("loginId"));
				dto.setPassword(rs.getString("password"));
				dto.setAddress(rs.getString("address"));
				dto.setContactNumber(rs.getString("contactNumber"));
				dto.setEmail(rs.getString("email"));
				dto.setCompanyId(rs.getString("vendorMaster"));
				dto.setCompany(rs.getString("company"));

				dtoList.add(dto);

			}

		} catch (Exception e) {
			System.out.println("Excepion (getVendorList) : " + e);
		} finally {

			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(stmt);
			DbConnect.closeConnection(con);

		}
		return dtoList;
	}

	public ArrayList<VendorDto> getVendorNotInSite(String siteId) {
		ArrayList<VendorDto> dtoList = new ArrayList<VendorDto>();

		String query = "";
		DbConnect ob = DbConnect.getInstance();

		query = "select id, name, loginId, password, address, contactNumber, email, ifnull( description,'') description, ifnull( rate,'') rate from vendor left join vendor_contract on vendor.id=vendor_contract.vendor where status='active' and id not in (select vendor from vendor_site where  site=?) ";

		PreparedStatement stmt = null;
		ResultSet rs = null;

		Connection con = ob.connectDB();
		try {
			stmt = con.prepareStatement(query);
			stmt.setString(1, siteId);
			rs = stmt.executeQuery();
			while (rs.next()) {
				VendorDto dto = new VendorDto();
				VendorContractDto contract = new VendorContractDto();
				contract.setVendor(rs.getString("id"));
				contract.setDescription(rs.getString("description"));
				contract.setRate(rs.getString("rate"));
				dto.setContract(contract);
				dto.setId(rs.getString("id"));
				dto.setName(rs.getString("name"));
				dto.setLoginId(rs.getString("loginId"));
				dto.setPassword(rs.getString("password"));
				dto.setAddress(rs.getString("address"));
				dto.setContactNumber(rs.getString("contactNumber"));
				dto.setEmail(rs.getString("email"));

				dtoList.add(dto);

			}

		} catch (Exception e) {
			System.out.println("Excepion (getVendorListNot in site) : " + e);
		} finally {

			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(stmt);
			DbConnect.closeConnection(con);

		}
		return dtoList;
	}

	public ArrayList<VendorDto> getVendorInSite(String siteId) {

		ArrayList<VendorDto> dtoList = new ArrayList<VendorDto>();

		String query = "";
		DbConnect ob = DbConnect.getInstance();

		query = "select v.id, v.name, v.loginId, v.password, v.address, v.contactNumber, v.email, ifnull( vendor_contract.description,'') description, ifnull( vendor_contract.rate,'') rate from vendor v left join vendor_contract on v.id=vendor_contract.vendor join vendor_site vs on v.id=vs.vendor where v.status='active' and vs.site=?";
		System.out.println("query : " + query);
		PreparedStatement stmt = null;
		ResultSet rs = null;

		Connection con = ob.connectDB();
		try {
			stmt = con.prepareStatement(query);
			stmt.setString(1, siteId);
			rs = stmt.executeQuery();
			while (rs.next()) {
				VendorDto dto = new VendorDto();
				VendorContractDto contract = new VendorContractDto();
				contract.setVendor(rs.getString("id"));
				contract.setDescription(rs.getString("description"));
				contract.setRate(rs.getString("rate"));
				dto.setContract(contract);
				dto.setId(rs.getString("id"));
				dto.setName(rs.getString("name"));
				dto.setLoginId(rs.getString("loginId"));
				dto.setPassword(rs.getString("password"));
				dto.setAddress(rs.getString("address"));
				dto.setContactNumber(rs.getString("contactNumber"));
				dto.setEmail(rs.getString("email"));

				dtoList.add(dto);

			}

		} catch (Exception e) {
			System.out.println("Excepion (getVendorList) : " + e);
		} finally {

			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(stmt);
			DbConnect.closeConnection(con);

		}
		return dtoList;
	}

	public int addVendor(VendorDto dto) {
		int returnInt = 0;
		DbConnect ob = DbConnect.getInstance();
		ResultSet autogenrs = null;
		int changedBy = 0, autoincNumber = 0;

		Connection con = ob.connectDB();
		PreparedStatement pst = null;
		try {
			changedBy = Integer.parseInt(dto.getDoneBy());
			String query = "insert into vendor ( name,loginId, empLinkId, password, address, contactNumber, email, vendorMaster) select ?,e.loginid, e.id,?,?,?,?,? from employee e where e.loginid=?";
			pst = con.prepareStatement(query);
			int step = 0;
			pst.setString(++step, dto.getName());
			
			pst.setString(++step, dto.getPassword());
			pst.setString(++step, dto.getAddress());
			pst.setString(++step, dto.getContactNumber());
			pst.setString(++step, dto.getEmail());
			pst.setString(++step, dto.getCompanyId() );
			pst.setString(++step, dto.getLoginId());
			
			 
			returnInt = pst.executeUpdate();
			 
			if (returnInt == 1) {
				Statement st = con.createStatement();
				autogenrs = st
						.executeQuery("Select id from vendor where name='"
								+ dto.getName() + "' and loginId='"
								+ dto.getLoginId() + "' and email='"
								+ dto.getEmail() + "'");
				while (autogenrs.next()) {
					autoincNumber = autogenrs.getInt(1);
				}
				auditLogEntryforaddvendor(autoincNumber,
						AuditLogConstants.VENDOR_MODULE, changedBy,
						AuditLogConstants.WORKFLOW_STATE_EMPTY,
						AuditLogConstants.WORKFLOW_STATE_VENDOR_ADDED,
						AuditLogConstants.AUDIT_LOG_CREATE_ACTION);
			}

		} catch (Exception e) {
			System.out.println("Error in Vendor insert dao" + e);
		}

		finally {
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);
		}

		return returnInt;
	}

	private void auditLogEntryforaddvendor(int autoincNumber, String Module,
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

	public int updateVendor(VendorDto dto) {
		int returnInt = 0;
		DbConnect ob = DbConnect.getInstance();
		int changedBy = 0, autoincNumber = 0;

		// System.out.println("");
		
		Connection con = ob.connectDB();
		PreparedStatement pst = null;
		try {
			String passwordQuery = "";
			if (dto.isNoPasswordUpdate() == false) {
				passwordQuery = " , vendor.password=? ";
			}
			
			String query = "update vendor, employee  set vendor.name=?,vendor.loginId=?,  vendor.address=?, vendor.contactNumber=?, vendor.email=?,vendorMaster=?, vendor.empLinkId=employee.id "
					+ passwordQuery + "  where vendor.id=? and employee.loginId=?";
			System.out.println(" query :  update vendor, employee   set name='"
					+ dto.getName() + "',loginId='" + dto.getLoginId()
					+ "',  address='" + dto.getAddress() + "', contactNumber='"
					+ dto.getContactNumber() + "', email='" + dto.getEmail()
					+ "', empLinkId=employee.id    where id=" + dto.getId() + " and employee.loginId='" + dto.getLoginId() + "' " );
			
			pst = con.prepareStatement(query);
			changedBy = Integer.parseInt(dto.getDoneBy());
			autoincNumber = Integer.parseInt(dto.getId());
			pst.setString(1, dto.getName());
			pst.setString(2, dto.getLoginId());

			pst.setString(3, dto.getAddress());
			pst.setString(4, dto.getContactNumber());
			pst.setString(5, dto.getEmail());
			pst.setString(6, dto.getCompanyId());
			int step = 6;
			if (dto.isNoPasswordUpdate() == false) {
				pst.setString(++step, dto.getPassword());
			}

			pst.setString(++step, dto.getId());
			pst.setString(++step, dto.getLoginId());
			returnInt = pst.executeUpdate();
			if (returnInt == 1) {
				auditLogEntryforupdatevendor(autoincNumber,
						AuditLogConstants.VENDOR_MODULE, changedBy,
						AuditLogConstants.WORKFLOW_STATE_VENDOR_ADDED,
						AuditLogConstants.WORKFLOW_STATE_VENDOR_MODIFIED,
						AuditLogConstants.AUDIT_LOG_EDIT_ACTION);

			}

		} catch (Exception e) {
			System.out.println("Error in Vendor Update dao" + e);
		}

		finally {
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);
		}

		return returnInt;
	}

	private void auditLogEntryforupdatevendor(int autoincNumber, String Module,
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

	public int deleteVendor(VendorDto dto) {

		int returnInt = 0;
		DbConnect ob = DbConnect.getInstance();
		int changedBy = 0, autoincNumber = 0;

		Connection con = ob.connectDB();
		PreparedStatement pst = null;
		Statement st = null;
		ResultSet rs = null;
		try {
			String query = "update vendor set status=?    where id=?";
			changedBy = Integer.parseInt(dto.getDoneBy());
			autoincNumber = Integer.parseInt(dto.getId());
			st = con.createStatement();
			rs = st.executeQuery("select * from vendor_trip_sheet_parent where insertedBy="
					+ dto.getId() + " or updatedBy=" + dto.getId() + "");
			if (!rs.next()) {
				pst = con.prepareStatement(query);

				pst.setString(1, "inactive");
				pst.setString(2, dto.getId());

				returnInt = pst.executeUpdate();

			}
			if (returnInt == 1) {
				auditLogEntryfordeletetevendor(autoincNumber,
						AuditLogConstants.VENDOR_MODULE, changedBy,
						AuditLogConstants.WORKFLOW_STATE_VENDOR_ADDED,
						AuditLogConstants.WORKFLOW_STATE_VENDOR_DELETED,
						AuditLogConstants.AUDIT_LOG_DELETE_ACTION);

			}

		} catch (Exception e) {
			System.out.println("Error in Vendor Update dao" + e);
		}

		finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(pst, st);
			DbConnect.closeConnection(con);
		}

		return returnInt;

	}

	private void auditLogEntryfordeletetevendor(int autoincNumber,
			String Module, int changedBy, String preworkflowState,
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

	public int doVendorContract(VendorContractDto dto) {

		int returnInt = 0;
		DbConnect ob = DbConnect.getInstance();
		int changedBy = 0, autoincNumber = 0;

		Connection con = ob.connectDB();
		PreparedStatement pst = null;

		Statement st = null;
		ResultSet rs = null;
		try {
			String checkQuery = "select * from vendor_contract where vendor="
					+ dto.getVendor();
			String updateQuery = "update vendor_contract set  description=?, rate=?    where vendor=?";
			String insertQuery = "insert into vendor_contract (vendor, description, rate) values (?,?,?)";
			st = con.createStatement();
			rs = st.executeQuery(checkQuery);
			changedBy = Integer.parseInt(dto.getDoneBy());
			autoincNumber = Integer.parseInt(dto.getVendor());
			if (rs.next()) {
				pst = con.prepareStatement(updateQuery);

				pst.setString(1, dto.getDescription());
				pst.setString(2, dto.getRate());
				pst.setString(3, dto.getVendor());

				returnInt = pst.executeUpdate();
				if (returnInt == 1) {
					auditLogEntryforcontract(autoincNumber,
							AuditLogConstants.VENDOR_MODULE, changedBy,
							AuditLogConstants.WORKFLOW_STATE_CONTRACT_ADDED,
							AuditLogConstants.WORFLOW_STATE_CONTRACT_UPDATED,
							AuditLogConstants.AUDIT_LOG_EDIT_ACTION);

				}
			} else {
				pst = con.prepareStatement(insertQuery);

				pst.setString(1, dto.getVendor());
				pst.setString(2, dto.getDescription());
				pst.setString(3, dto.getRate());

				returnInt = pst.executeUpdate();
				if (returnInt == 1) {
					auditLogEntryforcontract(autoincNumber,
							AuditLogConstants.VENDOR_MODULE, changedBy,
							AuditLogConstants.WORKFLOW_STATE_EMPTY,
							AuditLogConstants.WORKFLOW_STATE_CONTRACT_ADDED,
							AuditLogConstants.AUDIT_LOG_CREATE_ACTION);

				}
			}

		} catch (Exception e) {
			System.out.println("Error in doVendorContract Update dao" + e);
		}

		finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(pst, st);
			DbConnect.closeConnection(con);
		}

		return returnInt;

	}

	private void auditLogEntryforcontract(int autoincNumber, String Module,
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

	public ArrayList<VendorDto> getMasterVendorList() {


		ArrayList<VendorDto> dtoList = new ArrayList<VendorDto>();
		String query = "";
		DbConnect ob = DbConnect.getInstance();

		query = "select id, company, address, ifnull(contact,'') contact, ifnull(contact1,'') contact1, ifnull(email,'') email from vendormaster where status='active'";

		PreparedStatement stmt = null;
		ResultSet rs = null;

		Connection con = ob.connectDB();
		try {
			stmt = con.prepareStatement(query);
			rs = stmt.executeQuery();
			while (rs.next()) {
				VendorDto dto = new VendorDto();
				dto.setCompanyId(rs.getString("id"));
				dto.setCompany(rs.getString("company"));
				dto.setCompanyAddress(rs.getString("address"));
				dto.setCompanycontact(rs.getString("contact"));
				dto.setCompanycontact1(rs.getString("contact1"));
				dto.setEmail(rs.getString("email"));
				dtoList.add(dto);
			}

		} catch (Exception e) {
			System.out.println("Excepion (getVendorList) : " + e);
		} finally {

			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(stmt);
			DbConnect.closeConnection(con);

		}
		return dtoList;

	}

	public int assignVendorTrip(String vendorCompanyIdId, String[] trips) {
		int returnInt = 0;
		DbConnect ob = DbConnect.getInstance();

		Connection con = ob.connectDB();
		PreparedStatement pst = null;
		try {
			String insertQuery = "insert into tripvendorassign (tripId,vendorId, status) values (?,?, 'a')";
			pst = con.prepareStatement(insertQuery);
			for (String trip : trips) {
				pst.setString(1, trip);
				pst.setString(2, vendorCompanyIdId);
				returnInt += pst.executeUpdate();
			}
		} catch (Exception e) {
			System.out.println("error in assaign vendor dao" + e);
		} finally {
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);
		}

		return returnInt;
	}

	public ArrayList<VehicleDto> getVendorVehicles(String vendorId) {
		DbConnect ob = DbConnect.getInstance();
		ResultSet rs = null;

		Connection con = ob.connectDB();
		PreparedStatement pst = null;
		ArrayList<VehicleDto> vehicleDtos = new ArrayList<VehicleDto>();
		VehicleDto dto = null;
		try {
			String query = "select * from vehicles where status='a' and vendor=" + vendorId
					+ " order by vehicletype,CONVERT(SUBSTRING_INDEX(regNO ,' ',-1), UNSIGNED INTEGER)";
			System.out.println("vehicle Query : " + query);
			pst = con.prepareStatement(query);
			rs = pst.executeQuery();
			while (rs.next()) {
				dto = new VehicleDto();
				dto.setId(rs.getString("id"));
				dto.setVehicleNo(rs.getString("regNo"));
				dto.setVehicleType(rs.getString("vehicletype"));
				dto.setVendor(rs.getString("vendor"));
				vehicleDtos.add(dto);
			}

		} catch (Exception e) {
			System.out.println("Error in Vendor insert dao" + e);
		}

		finally {
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);
		}

		return vehicleDtos;
	}

	public VehicleDto getvendorCompany(String vendorId) {
		DbConnect ob = DbConnect.getInstance();
		ResultSet rs = null;

		Connection con = ob.connectDB();
		PreparedStatement pst = null;
		VehicleDto dto = null;
		try {
			String query = "select * from vendor where id=" + vendorId + "";
			pst = con.prepareStatement(query);
			rs = pst.executeQuery();
			if (rs.next()) {
				dto = new VehicleDto();
				dto.setId(rs.getString("id"));
				dto.setVendor(rs.getString("vendorMaster"));
			}

		} catch (Exception e) {
			System.out.println("Error in Vendor insert dao" + e);
		}

		finally {
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);
		}

		return dto;

	}
	

	public int addVendorMaster(VendorDto dto) {
		int returnInt = 0;
		DbConnect ob = DbConnect.getInstance();
		ResultSet autogenrs = null;
		int changedBy = 0, autoincNumber = 0;

		Connection con = ob.connectDB();

		PreparedStatement pst = null;
		try {
			con.setAutoCommit(false);
			changedBy = Integer.parseInt(dto.getDoneBy());
			String query = "insert into vendormaster ( Company, address, contact, email,status) values (?,?,?,?,'active')";
			pst = con.prepareStatement(query);
			int step = 0;
			pst.setString(++step, dto.getName());
			pst.setString(++step, dto.getAddress());
			pst.setString(++step, dto.getContactNumber());
			pst.setString(++step, dto.getEmail());
			returnInt = pst.executeUpdate();
			if (returnInt == 1) {
				Statement st = con.createStatement();
				autogenrs = st
						.executeQuery("Select id from vendormaster  where Company='"
								+ dto.getName()
								+ "'  and email='"
								+ dto.getEmail() + "'");
				while (autogenrs.next()) {
					autoincNumber = autogenrs.getInt(1);
				}
				auditLogEntryforaddvendor(autoincNumber,
						AuditLogConstants.VENDOR_MODULE, changedBy,
						AuditLogConstants.WORKFLOW_STATE_EMPTY,
						AuditLogConstants.WORKFLOW_STATE_VENDOR_MASTER_ADDED,
						AuditLogConstants.AUDIT_LOG_CREATE_ACTION);
			}
			con.commit();

		} catch (Exception e) {
			System.out.println("Error in Vendor insert dao" + e);
			try {
				con.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				;
			}
		}

		finally {
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);
		}

		return returnInt;

	}

	public int updateVendorMaster(VendorDto dto) {
		int returnInt = 0;
		DbConnect ob = DbConnect.getInstance();
		int changedBy = 0, autoincNumber = 0;

		Connection con = ob.connectDB();
		PreparedStatement pst = null;
		try {

			String query = "update vendormaster set Company=?, address=?, contact=?, email=?  where id=?";

			pst = con.prepareStatement(query);
			changedBy = Integer.parseInt(dto.getDoneBy());
			autoincNumber = Integer.parseInt(dto.getId());
			int step = 0;
			pst.setString(++step, dto.getName());
			pst.setString(++step, dto.getAddress());
			pst.setString(++step, dto.getContactNumber());
			pst.setString(++step, dto.getEmail());
			pst.setString(++step, dto.getId());

			returnInt = pst.executeUpdate();
			if (returnInt == 1) {
				auditLogEntryforupdatevendor(autoincNumber,
						AuditLogConstants.VENDOR_MODULE, changedBy,
						AuditLogConstants.WORKFLOW_STATE_VENDOR_ADDED,
						AuditLogConstants.WORKFLOW_STATE_VENDOR_MODIFIED,
						AuditLogConstants.AUDIT_LOG_EDIT_ACTION);

			}

		} catch (Exception e) {
			System.out.println("Error in Vendor Update dao" + e);
		}

		finally {
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);
		}

		return returnInt;
	}

	public int deleteVendorMaster(VendorDto dto) {
		//

		int returnInt = 0;
		DbConnect ob = DbConnect.getInstance();
		int changedBy = 0, autoincNumber = 0;

		Connection con = ob.connectDB();
		PreparedStatement pst = null;
		Statement st = null;

		try {
			String query = "update vendormaster set status=?    where id=?";
			changedBy = Integer.parseInt(dto.getDoneBy());
			autoincNumber = Integer.parseInt(dto.getId());
			st = con.createStatement();

			pst = con.prepareStatement(query);

			pst.setString(1, "inactive");
			pst.setString(2, dto.getId());

			returnInt = pst.executeUpdate();

			if (returnInt == 1) {
				AuditLogDAO adao = new AuditLogDAO();
				adao.auditLogEntry(autoincNumber,
						AuditLogConstants.VENDOR_MODULE, changedBy, "",
						AuditLogConstants.WORKFLOW_STATE_VENDOR_DELETED,
						AuditLogConstants.AUDIT_LOG_DELETE_ACTION);

			}

		} catch (Exception e) {
			System.out.println("Error in Soft Delete Vendor Master dao" + e);
		}

		finally {

			DbConnect.closeStatement(pst, st);
			DbConnect.closeConnection(con);
		}

		return returnInt;

	}
	
	public ArrayList<VendorDto> getVendorMasterList()
	{
		ArrayList<VendorDto> dtoList = new ArrayList<VendorDto> ();
		
		String query="";
		DbConnect ob = DbConnect.getInstance();

	  query = "select id, Company, address, contact, contact1, email, status from vendormaster where status='Active' ";
	 
		PreparedStatement stmt =null;
		ResultSet rs=null;
				
		Connection con = ob.connectDB();
		try {
			  stmt = con.prepareStatement(query);
			  rs= stmt.executeQuery();
			  while(rs.next())
			  {
				  VendorDto dto = new VendorDto();
				  dto.setId(rs.getString("id"));
				  dto.setCompany(rs.getString("company"));
				  dto.setAddress(rs.getString("address"));
				  dto.setContactNumber(rs.getString("contact"));
				  dto.setEmail(rs.getString("email"));
				  
			 
				  dtoList.add(dto);
				  
			  }
			
		}catch(Exception e)
		{
			System.out.println("Excepion (getVendormasterList) : " + e);
		}
		finally{
			
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(stmt);
			DbConnect.closeConnection(con);
		
		}
		return dtoList;
	}


}
