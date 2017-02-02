package com.agiledge.atom.billingtype.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;

import com.agiledge.atom.billingtype.dto.BillingTypeDto;
import com.agiledge.atom.commons.OtherFunctions;
import com.agiledge.atom.constants.AuditLogConstants;
import com.agiledge.atom.dao.AuditLogDAO;
import com.agiledge.atom.dbConnect.DbConnect;
import com.agiledge.atom.dto.VehicleTypeDto;

public class BillingTypeDao {

	Connection con;
	
	boolean localCon = false;
	public void Connect() {
		  getConnection();
	}
	
	public void setAutoCommit(boolean flag) throws SQLException {
		con.setAutoCommit(flag);
	}
	
	public void commit() throws SQLException {
		con.commit();
	}
	public void rollback() throws SQLException {
		con.rollback();
	}

	private Connection getConnection() {

		try {
			if (con == null || con.isClosed()) {
				DbConnect ob = DbConnect.getInstance();
				con = ob.connectDB();
				localCon = true;
			} else {
				localCon = false;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			DbConnect ob = DbConnect.getInstance();
			con = ob.connectDB();
			localCon = true;
		}
		return con;
	}

	private void closeConnection() {
		if (localCon == true) {
			DbConnect.closeConnection(con);
		}
	}

	public ArrayList<BillingTypeDto> getBillingTypes() {
		ArrayList<BillingTypeDto> dtoList = new ArrayList<BillingTypeDto>();

		String query = "";

		query = "select id, name, ifnull(description,'') description  from  Billing_type";

		PreparedStatement stmt = null;
		ResultSet rs = null;
		Connection con;

		try {
			con = getConnection();
			stmt = con.prepareStatement(query);
			rs = stmt.executeQuery();
			while (rs.next()) {
				BillingTypeDto dto = new BillingTypeDto();

				dto.setId(rs.getString("id"));
				dto.setName(rs.getString("name"));
				dto.setDescription(rs.getString("description"));

				dtoList.add(dto);

			}

		} catch (Exception e) {
			System.out.println("Excepion (getVendorList) : " + e);
		} finally {

			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(stmt);

			closeConnection();

		}
		return dtoList;
	}

	@Override
	protected void finalize() throws Throwable {
		DbConnect.closeConnection(con);
		localCon = false;
	}

	public int mapBillingType(BillingTypeDto dto) throws Exception {
		  
			int returnInt = 0;
			DbConnect ob = DbConnect.getInstance();
			int changedBy = 0, autoincNumber = 0;
			ResultSet ars =null;

			Connection con =getConnection();
			
			PreparedStatement pst = null;
			PreparedStatement updatePst = null;
			 
			try {
			 
				
				String query = "insert into Billing_type_mapping (site, vendor, tran_type, fromDate, billing_type) values (?,?,?,?,?)";
				String updateQuery = "update Billing_type_mapping set toDate=date_add( ?, interval -1 day) where fromDate<? and toDate is null and site=? and vendor=? and tran_type=? and billing_type=? ";
				updatePst = con.prepareStatement(updateQuery);
				updatePst.setString(1, OtherFunctions.changeDateFromatToIso( dto.getFromDate()));
				updatePst.setString(2,  OtherFunctions.changeDateFromatToIso(dto.getFromDate()));
				updatePst.setString(3, dto.getSite());
				updatePst.setString(4, dto.getVendor());
				updatePst.setString(5, dto.getTransportType());
				updatePst.setString(6, dto.getId());
				pst = con.prepareStatement(query,java.sql.Statement.RETURN_GENERATED_KEYS);
				pst.setString(1, dto.getSite());
				pst.setString(2, dto.getVendor());
				pst.setString(3, dto.getTransportType());
				pst.setString(4, OtherFunctions.changeDateFromatToIso( dto.getFromDate()));
				pst.setString(5, dto.getId());
				returnInt =pst.executeUpdate();
				
				updatePst.executeUpdate();
				if(returnInt >0) {
					ars = pst.getGeneratedKeys();
					if(ars.next()) {
					dto.setId(ars.getString(1));
					  
			 
 				 
 				
			 				 
			 					AuditLogDAO adao = new AuditLogDAO();
			 					 
			 					//adto.set
			 					adao.auditLogEntry( Long.parseLong(dto.getId()), AuditLogConstants.BILLING_TYPE_MODULE, Integer.parseInt( dto.getDoneBy()), AuditLogConstants.WORKFLOW_STATE_EMPTY, AuditLogConstants.WORK_FLOW_STATE_BILLING_TYPE_MAPPED, AuditLogConstants.AUDIT_LOG_CREATE_ACTION);
			 		} 
					 
					 

				}

			} catch (Exception e) {
				System.out.println("Error in Vendor Update dao" + e);
				throw e;
			}

			finally {
				DbConnect.closeStatement(pst);
				 closeConnection();
			}

			return returnInt;
		
	}
	
	public boolean checkAnyFutureEntry(BillingTypeDto dto) {
		boolean flag=false;
		 String selectQuery = " select * from Billing_type_mapping where fromDate > '"+OtherFunctions.changeDateFromat(dto.getFromDate())+"' and toDate is null and site=? and vendor=? and tran_type=? and billing_type=? ";
		 System.out.println(selectQuery);
			PreparedStatement stmt = null;
			ResultSet rs = null;
			Connection con;

			try {
				con = getConnection();
				stmt = con.prepareStatement(selectQuery);
				 
				stmt.setString(1, dto.getSite());
				stmt.setString(2,  dto.getVendor());
				stmt.setString(3, dto.getTransportType());
				stmt.setString(4, dto.getId());
				rs = stmt.executeQuery();
				if (rs.next()) {
					flag =true;
				}

			} catch (Exception e) {
				System.out.println("Excepion (getVendorList) : " + e);
			} finally {

				DbConnect.closeResultSet(rs);
				DbConnect.closeStatement(stmt);

				closeConnection();

			}

		 return flag;
	}
	
	
	public ArrayList<BillingTypeDto> getBillingTypeMappings(String param) {
		
		Connection con=null;
		Statement st = null;
		ResultSet rs=null;
		ArrayList<BillingTypeDto> dtoList = null;
		 try {
			 String query =  "select tm.id, tm.fromDate, tm.toDate, s.site_name, vm.Company vendor_name," +
				 		"  tt.name Billing_type_name, s.id site_id, vm.id vendor_id, tt.id Billing_type_id, ttp.id transport_type_id, ttp.name transport_type_name, ifnull(vkr.id,-1)  conditionId from Billing_type_mapping tm left join vehicle_km_rate vkr on tm.id=vkr.refId , site s  , vendormaster vm, Billing_type tt, transport_type ttp  where  tm.site=s.id and  tm.billing_type=tt.id and tm.vendor=vm.id  and tm.tran_type=ttp.id  ";
			 if(param.equals("future")) {
				 query = query + " and fromDate>curdate()   ";
			 } else if (param.equals("present")) {
				 query = query + " and fromDate <=curdate() and ( toDate>=curdate() or toDate is null ) ";
			 } else if(param.equals("past")){
				 query = query + "  and  toDate < curdate()  ";
			 }
			 String groupByQuery = " order by s.id, vm.id, tt.id, tm.fromDate";
			  query=  query + groupByQuery; 
				System.out.println("query : " + query);	 
					 /*  where toDate < curdate() */
					/* where fromDate>curdate()   */
					/* where fromDate <=curdate() and ( toDate>=curdate() or toDate is null ) */
			 con = getConnection();
			 System.out.println("here");
			 st = con.createStatement();
			 rs = st.executeQuery(query);
			 System.out.println("Query : " + query);
			 dtoList= new ArrayList<BillingTypeDto>();
			 while(rs.next()) {
				 BillingTypeDto dto = new BillingTypeDto();
				 dto.setId(rs.getString("id"));
				 dto.setFromDate(rs.getDate("fromDate"));
				 dto.setToDate(rs.getDate("toDate"));
				 dto.setSite(rs.getString("site_name"));
				 dto.setSiteId(rs.getString("site_id"));
				 dto.setVendor(rs.getString("vendor_name"));
				 dto.setVendorId(rs.getString("vendor_id"));
				 dto.setBillingTypeId(rs.getString("Billing_type_id"));
				 dto.setBillingType(rs.getString("Billing_type_name"));
				 dto.setTransportType(rs.getString("transport_type_name"));
				 dto.setTransportTypeId(rs.getString("transport_type_id"));
				  
				 dto.setConditionId(rs.getString("conditionId"));
				 dtoList.add(dto);
			 }

			 
		 }catch(Exception e) {
			 System.out.println("Error in getAcctiveBillingTypeMapping :"+ e);
		 }
		 finally{
			 DbConnect.closeResultSet(rs);
			 DbConnect.closeStatement(st);
			 closeConnection();
		 }
		 return dtoList;
				 
	}

	public  BillingTypeDto getBillingTypeMappingById(String id) {
		
		Connection con=null;
		Statement st = null;
		ResultSet rs=null;
		 
		BillingTypeDto dto = null;
		 try {
			 String query = "select tm.id, tm.fromDate, tm.toDate, s.site_name, vm.Company vendor_name," +
				 		"  tt.name Billing_type_name, s.id site_id, vm.id vendor_id, tt.id Billing_type_id, ttp.id tranport_type_id, ttp.name transport_type_name, ttp.id transport_type_id from Billing_type_mapping tm , site s  , vendormaster vm, Billing_type tt, transport_type ttp  where  tm.site=s.id and  tm.billing_type=tt.id and tm.vendor=vm.id  and tm.tran_type=ttp.id  " 
					 +" and tm.id=" + id;
				 	 
					 /*  where toDate < curdate() */
					/* where fromDate>curdate()   */
					/* where fromDate <=curdate() and ( toDate>=curdate() or toDate is null ) */
			 con = getConnection();
			 st = con.createStatement();
			 rs = st.executeQuery(query);
			 
			 if(rs.next()) {
				 dto = new BillingTypeDto();
				 dto.setId(rs.getString("id"));
				 dto.setFromDate(rs.getDate("fromDate"));
				 dto.setToDate(rs.getDate("toDate"));
				 dto.setSite(rs.getString("site_name"));
				 dto.setSiteId(rs.getString("site_id"));
				 dto.setVendor(rs.getString("vendor_name"));
				 dto.setVendorId(rs.getString("vendor_id"));
				 dto.setBillingTypeId(rs.getString("Billing_type_id"));
				 dto.setBillingType(rs.getString("Billing_type_name"));
				 dto.setTransportType(rs.getString("transport_type_name"));
				 dto.setTransportTypeId(rs.getString("transport_type_id"));
				  
			 }

			 
		 }catch(Exception e) {
			 System.out.println("Error in getAcctiveBillingTypeMapping :"+ e);
		 }
		 finally{
			 DbConnect.closeResultSet(rs);
			 DbConnect.closeStatement(st);
			 closeConnection();
		 }
		 return dto;
				 
	}


	public int deleteBillingTypeMapping(BillingTypeDto dto) {
			
		int returnInt = 0;
		DbConnect ob = DbConnect.getInstance();
		  
		Connection con =getConnection();

		CallableStatement deletePst = null;
		 
		 
		try {
		 
			deletePst = con.prepareCall("{CALL DeleteBillingTypeMapping(?, ?)}");
			System.out.println("((( " + dto.getId());
			deletePst.setString(1, dto.getId());
			deletePst.registerOutParameter(2, Types.INTEGER);   
			 deletePst.execute();
			   returnInt = deletePst.getInt(2);
			  
		} catch (Exception e) {
			System.out.println("Error in Vendor Update dao" + e);
			//throw e;
		}

		finally {
			DbConnect.closeStatement(deletePst);
			 closeConnection();
		}

		return returnInt;
	

 
	}

	public ArrayList<BillingTypeDto> getBillingTypeMappings(
			BillingTypeDto dtoParam) {
		

		Connection con=null;
		Statement st = null;
		ResultSet rs=null;
		ArrayList<BillingTypeDto> dtoList = null;
		 try {
			 String query =  "select tm.id, tm.fromDate, tm.toDate, s.site_name, vm.Company vendor_name," +
				 		"  tt.name Billing_type_name, s.id site_id, vm.id vendor_id, tt.id Billing_type_id, ttp.id tranport_type_id, ttp.name transport_type_name from Billing_type_mapping tm , site s  , vendormaster vm, Billing_type tt, transport_type ttp  where  tm.site=s.id and  tm.billing_type=tt.id and tm.vendor=vm.id  and tm.tran_type=ttp.id  ";
			 if(dtoParam.getStatus().equals("future")) {
				 query = query + " and fromDate>curdate()   ";
			 } else if (dtoParam.getStatus().equals("present")) {
				 query = query + " and fromDate <=curdate() and ( toDate>=curdate() or toDate is null ) ";
			 } else if(dtoParam.getStatus().equals("past")){
				  query = query + "  and toDate < curdate()  ";
			 }
			 
			 // other params
			 if(dtoParam.getSiteId()!=null&&dtoParam.getSiteId().equals("")==false) {
				 query = query + "  and s.id=" + dtoParam.getSiteId();
			 }
			 if(dtoParam.getVendorId()!=null && dtoParam.getVendorId().equals("")==false) {
				 query = query + "  and vm.id=" + dtoParam.getVendorId();
			 }
			 
			 //
			 String groupByQuery = " order by s.id, vm.id, tt.id, tm.fromDate";
			  query=  query + groupByQuery; 
				System.out.println("query : " + query);	 
					 /*  where toDate < curdate() */
					/* where fromDate>curdate()   */
					/* where fromDate <=curdate() and ( toDate>=curdate() or toDate is null ) */
			 con = getConnection();
			 st = con.createStatement();
			 rs = st.executeQuery(query);
			 dtoList= new ArrayList<BillingTypeDto>();
			 while(rs.next()) {
				 BillingTypeDto dto = new BillingTypeDto();
				 dto.setId(rs.getString("id"));
				 dto.setFromDate(rs.getDate("fromDate"));
				 dto.setToDate(rs.getDate("toDate"));
				 dto.setSite(rs.getString("site_name"));
				 dto.setSiteId(rs.getString("site_id"));
				 dto.setVendor(rs.getString("vendor_name"));
				 dto.setVendorId(rs.getString("vendor_id"));
				 dto.setBillingTypeId(rs.getString("Billing_type_id"));
				 dto.setBillingType(rs.getString("Billing_type_name"));
				 dto.setTransportType(rs.getString("transport_type_name"));
				 dto.setTransportTypeId(rs.getString("transport_type_id"));
				 dtoList.add(dto);
			 }

			 
		 }catch(Exception e) {
			 System.out.println("Error in getBillingTypeMappings . :"+ e);
		 }
		 finally{
			 DbConnect.closeResultSet(rs);
			 DbConnect.closeStatement(st);
			 closeConnection();
		 }
		 return dtoList;
				 

		
	}

	public ArrayList<VehicleTypeDto> getVehicleRates(String refId) {
		// TODO Auto-generated method stub

		Connection con=null;
		Statement st = null;
		ResultSet rs=null;
		 
		VehicleTypeDto dto = null;
		ArrayList<VehicleTypeDto> dtoList = null;
		 try {
			 String query = "select vkr.id, vkr.vehicle_type vehicleTypeId, vt.type  vehicleTypeName, vkr.ratePerKm ratePerKm  from vehicle_km_rate vkr, vehicle_type vt   where vkr.vehicle_type=vt.id and vkr.refId="+ refId;
					 
					 /*  where toDate < curdate() */
					/* where fromDate>curdate()   */
					/* where fromDate <=curdate() and ( toDate>=curdate() or toDate is null ) */
			 con = getConnection();
			 st = con.createStatement();
			 rs = st.executeQuery(query);
			 dtoList= new ArrayList<VehicleTypeDto>(); 
			 while(rs.next()) {
				 dto = new VehicleTypeDto();
				 dto.setConditionId(rs.getString("id"));
				 dto.setId(rs.getInt("vehicleTypeId"));
				 dto.setType(rs.getString("vehicleTypeName"));
				 dto.setRatePerKm(rs.getDouble("ratePerKm"));
				 dto.setRefId(refId);
				  dtoList.add(dto);
			 }

			 
		 }catch(Exception e) {
			 System.out.println("Error in getVehicles with rate in BillingTypeDao :"+ e);
		 }
		 finally{
			 DbConnect.closeResultSet(rs);
			 DbConnect.closeStatement(st);
			 closeConnection();
		 }
		 return dtoList;

		
	}
	
	

	public int addVehicleRate(VehicleTypeDto dto) throws Exception {
		 

		int returnInt = 0;
		DbConnect ob = DbConnect.getInstance();
		int  autoincNumber = 0;
		ResultSet ars =null;

		Connection con =getConnection();
		
		PreparedStatement pst = null;
		 
		 
		try {
		 
			
			String query = "insert into vehicle_km_rate (refId, vehicle_type, ratePerKm) values (?, ?, ?)";
			 
		 
			pst = con.prepareStatement(query,java.sql.Statement.RETURN_GENERATED_KEYS);
			pst.setString(1, dto.getConditionId());
			pst.setInt(2, dto.getId());
			pst.setDouble(3, dto.getRatePerKm());
			 
			returnInt =pst.executeUpdate();
			
			 
			if(returnInt >0) {
				ars = pst.getGeneratedKeys();
				if(ars.next()) {
							autoincNumber= ars.getInt(1);
 		 					AuditLogDAO adao = new AuditLogDAO();
 		 					adao.auditLogEntry(autoincNumber, AuditLogConstants.BILLING_TYPE_MODULE, Integer.parseInt(dto.getDoneBy()), AuditLogConstants.WORKFLOW_STATE_EMPTY, AuditLogConstants.WORK_FLOW_STATE_KM_RATE_FOR_VEHICLE_ADDED, AuditLogConstants.AUDIT_LOG_CREATE_ACTION);
		 					 
		 		} 
				 
				 

			}

		} catch (Exception e) {
			System.out.println("Error in addVehicleRate in BillingTypeDao" + e);
			throw e;
		}

		finally {
			DbConnect.closeStatement(pst);
			 closeConnection();
		}

		return returnInt;
	

	}

	public boolean checkDupliationinVehicleRate(VehicleTypeDto dto) {
		boolean flag=false;
		 String selectQuery = " select * from vehicle_km_rate where refId=? and vehicle_type=?  "; 
		 
			PreparedStatement stmt = null;
			ResultSet rs = null;
			Connection con;

			try {
				con = getConnection();
				stmt = con.prepareStatement(selectQuery);
				 
				stmt.setString(1, dto.getConditionId());
				stmt.setInt(2, dto.getId());
				rs = stmt.executeQuery();
				if (rs.next()) {
					flag =true;
				}

			} catch (Exception e) {
				System.out.println("Excepion (check dupliation) : " + e);
			} finally {

				DbConnect.closeResultSet(rs);
				DbConnect.closeStatement(stmt);

				closeConnection();

			}

		 return flag;

	}

	 public int deleteVehicleKmRate(VehicleTypeDto dto) {
		//  
		 int returnInt = 0;
			DbConnect ob = DbConnect.getInstance();
			  
			Connection con =getConnection();

			PreparedStatement  pst = null;
			 
			 
			try {
				String query = "delete from vehicle_km_rate where id=?";
			 System.out.println("query : " +query+"\b"+ dto.getConditionId());
				pst = con.prepareStatement(query);
				
				 
				pst.setString(1, dto.getConditionId().trim());
				 
				   returnInt = pst.executeUpdate();
				  
			} catch (Exception e) {
				System.out.println("Error in delete vehicle km rate billing type dao" + e);
				//throw e;
			}

			finally {
				DbConnect.closeStatement(pst);
				 closeConnection();
			}

			return returnInt;
		
 
	}

	public ArrayList<BillingTypeDto> getCurrentBillingTypeMappings(
			String siteId, String vendorId, String transportType) {
 			
			Connection con=null;
			Statement st = null;
			ResultSet rs=null;
			ArrayList<BillingTypeDto> dtoList = null;
			 try {
				 String param="present";
				 String query =  "select tm.id, tm.fromDate, tm.toDate, s.site_name, vm.Company vendor_name," +
					 		"  tt.name Billing_type_name,tt.`key`  Billing_type_key , s.id site_id, vm.id vendor_id, tt.id Billing_type_id, ttp.id transport_type_id, ttp.name transport_type_name, ifnull(vkr.id,'-1')  conditionId from Billing_type_mapping tm left join vehicle_km_rate vkr on tm.id=vkr.refId , site s  , vendormaster vm, Billing_type tt, transport_type ttp  where  tm.site=s.id and  tm.billing_type=tt.id and tm.vendor=vm.id  and tm.tran_type=ttp.id and tm.site=" + siteId + " and tm.vendor=" + vendorId + " and tm.tran_type=" + transportType + " ";
				 if(param.equals("future")) {
					 query = query + " and fromDate>curdate()   ";
				 } else if (param.equals("present")) {
					 query = query + " and fromDate <=curdate() and ( toDate>=curdate() or toDate is null ) ";
				 } else if(param.equals("past")){
					 query = query + "  and  toDate < curdate()  ";
				 }
				 String groupByQuery = " order by s.id, vm.id, tt.id, tm.fromDate";
				  query=  query + groupByQuery; 
					System.out.println("query : " + query);	 
						 /*  where toDate < curdate() */
						/* where fromDate>curdate()   */
						/* where fromDate <=curdate() and ( toDate>=curdate() or toDate is null ) */
				 con = getConnection();
				 st = con.createStatement();
				 rs = st.executeQuery(query);
				 dtoList= new ArrayList<BillingTypeDto>();
				 while(rs.next()) {
					 BillingTypeDto dto = new BillingTypeDto();
					 dto.setId(rs.getString("id"));
					 dto.setFromDate(rs.getDate("fromDate"));
					 dto.setToDate(rs.getDate("toDate"));
					 dto.setSite(rs.getString("site_name"));
					 dto.setSiteId(rs.getString("site_id"));
					 dto.setVendor(rs.getString("vendor_name"));
					 dto.setVendorId(rs.getString("vendor_id"));
					 dto.setBillingTypeId(rs.getString("Billing_type_id"));
					 dto.setKey(rs.getString("Billing_type_key"));
					 dto.setBillingType(rs.getString("Billing_type_name"));
					 dto.setTransportType(rs.getString("transport_type_name"));
					 dto.setTransportTypeId(rs.getString("transport_type_id"));
					  
					 dto.setConditionId(rs.getString("conditionId"));
					 dtoList.add(dto);
					  
				 }

				 
			 }catch(Exception e) {
				 System.out.println("Error in getAcctiveBillingTypeMapping :"+ e);
			 }
			 finally{
				 DbConnect.closeResultSet(rs);
				 DbConnect.closeStatement(st);
				 closeConnection();
			 }
			 return dtoList;
					 
		}
	 

}
