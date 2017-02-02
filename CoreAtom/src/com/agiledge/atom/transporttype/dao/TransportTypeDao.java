package com.agiledge.atom.transporttype.dao;
 
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import com.agiledge.atom.commons.OtherFunctions;
import com.agiledge.atom.constants.AuditLogConstants;
import com.agiledge.atom.dao.AuditLogDAO;
import com.agiledge.atom.dbConnect.DbConnect;
import com.agiledge.atom.transporttype.dto.TransportTypeDto;

public class TransportTypeDao {
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

	public ArrayList<TransportTypeDto> getTransportTypes() {
		ArrayList<TransportTypeDto> dtoList = new ArrayList<TransportTypeDto>();

		String query = "";

		query = "select id, name, ifnull(description,'') description  from  transport_type";

		PreparedStatement stmt = null;
		ResultSet rs = null;
		Connection con;

		try {
			con = getConnection();
			stmt = con.prepareStatement(query);
			rs = stmt.executeQuery();
			while (rs.next()) {
				TransportTypeDto dto = new TransportTypeDto();

				dto.setId(rs.getInt("id"));
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

	public int mapTransportType(TransportTypeDto dto) throws Exception {
		  
			int returnInt = 0;
			DbConnect ob = DbConnect.getInstance();
			int changedBy = 0, autoincNumber = 0;
			ResultSet ars =null;

			Connection con =getConnection();
			
			PreparedStatement pst = null;
			PreparedStatement updatePst = null;
			 
			try {
			 
				
				String query = "insert into transport_type_mapping (site, vendor, tran_type, fromDate) values (?,?,?,?)";
				String updateQuery = "update transport_type_mapping set toDate=dateadd(day,-1, ?) where fromDate<? and toDate is null and site=? and vendor=? and tran_type=? ";
				updatePst = con.prepareStatement(updateQuery);
				updatePst.setString(1, OtherFunctions.changeDateFromatToIso( dto.getFromDate()));
				updatePst.setString(2,  OtherFunctions.changeDateFromatToIso(dto.getFromDate()));
				updatePst.setString(3, dto.getSite());
				updatePst.setString(4, dto.getVendor());
				updatePst.setInt(5, dto.getId());
				pst = con.prepareStatement(query,java.sql.Statement.RETURN_GENERATED_KEYS);
				pst.setString(1, dto.getSite());
				pst.setString(2, dto.getVendor());
				pst.setInt(3, dto.getId());
				pst.setString(4, OtherFunctions.changeDateFromatToIso( dto.getFromDate()));
				returnInt =pst.executeUpdate();
				
				updatePst.executeUpdate();
				if(returnInt >0) {
					ars = pst.getGeneratedKeys();
					if(ars.next()) {
					dto.setDoneBy(ars.getInt(1));
					  
			 
 				 
 				
			 				 
			 					AuditLogDAO adao = new AuditLogDAO();
			 					 
			 					//adto.set
			 					adao.auditLogEntry(dto.getDoneBy(), AuditLogConstants.TRANSPORT_TYPE_MODULE, dto.getDoneBy(), AuditLogConstants.WORKFLOW_STATE_EMPTY, AuditLogConstants.WORK_FLOW_STATE_TRANSPORT_TYPE_MAPPED, AuditLogConstants.AUDIT_LOG_CREATE_ACTION);
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
	
	public boolean checkAnyFutureEntry(TransportTypeDto dto) {
		boolean flag=false;
		 String selectQuery = " select * from transport_type_mapping where fromDate > '"+OtherFunctions.changeDateFromat(dto.getFromDate())+"' and toDate is null and site=? and vendor=? and tran_type=? ";
		 
			PreparedStatement stmt = null;
			ResultSet rs = null;
			Connection con;

			try {
				con = getConnection();
				stmt = con.prepareStatement(selectQuery);
				 
				stmt.setString(1, dto.getSite());
				stmt.setString(2,  dto.getVendor());
				stmt.setInt(3, dto.getId());
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
	
	
	public ArrayList<TransportTypeDto> getTransportTypeMappings(String param) {
		
		Connection con=null;
		Statement st = null;
		ResultSet rs=null;
		ArrayList<TransportTypeDto> dtoList = null;
		 try {
			 String query = "select tm.id, tm.fromDate, tm.toDate, s.site_name, vm.Company vendor_name," +
			 		"  tt.name transport_type_name, s.id site_id, vm.id vendor_id, tt.id transport_type_id from transport_type_mapping tm join site s on tm.site=s.id join vendormaster vm on tm.vendor=vm.id join transport_type tt on tm.tran_type=tt.id";
			 if(param.equals("future")) {
				 query = query + " where fromDate>now()   ";
			 } else if (param.equals("present")) {
				 query = query + " where fromDate <=now() and ( toDate>=now() or toDate is null ) ";
			 } else if(param.equals("past")){
				 query = query + "  where  toDate < now()  ";
			 }
			 String groupByQuery = " order by s.id, vm.id, tt.id, tm.fromDate";
			  query=  query + groupByQuery; 
				System.out.println("query : " + query);	 
					 /*  where toDate < now() */
					/* where fromDate>now()   */
					/* where fromDate <=now() and ( toDate>=now() or toDate is null ) */
			 con = getConnection();
			 st = con.createStatement();
			 rs = st.executeQuery(query);
			 dtoList= new ArrayList<TransportTypeDto>();
			 while(rs.next()) {
				 TransportTypeDto dto = new TransportTypeDto();
				 dto.setId(rs.getInt("id"));
				 dto.setFromDate(rs.getDate("fromDate"));
				 dto.setToDate(rs.getDate("toDate"));
				 dto.setSite(rs.getString("site_name"));
				 dto.setSiteId(rs.getString("site_id"));
				 dto.setVendor(rs.getString("vendor_name"));
				 dto.setVendorId(rs.getString("vendor_id"));
				 dto.setTransportTypeId(rs.getString("transport_type_id"));
				 dto.setTransportType(rs.getString("transport_type_name"));
				 dtoList.add(dto);
			 }

			 
		 }catch(Exception e) {
			 System.out.println("Error in getAcctiveTransportTypeMapping :"+ e);
		 }
		 finally{
			 DbConnect.closeResultSet(rs);
			 DbConnect.closeStatement(st);
			 closeConnection();
		 }
		 return dtoList;
				 
	}

	public  TransportTypeDto getTransportTypeMappingById(String id) {
		
		Connection con=null;
		Statement st = null;
		ResultSet rs=null;
		 
		TransportTypeDto dto = null;
		 try {
			 String query = "select tm.id, tm.fromDate, tm.toDate, s.site_name, vm.Company vendor_name," +
			 		"  tt.name transport_type_name, s.id site_id, vm.id vendor_id, tt.id transport_type_id from transport_type_mapping tm join site s on tm.site=s.id join vendormaster vm on tm.vendor=vm.id join transport_type tt on tm.tran_type=tt.id where tm.id=" + id;
					 
					 /*  where toDate < now() */
					/* where fromDate>now()   */
					/* where fromDate <=now() and ( toDate>=now() or toDate is null ) */
			 con = getConnection();
			 st = con.createStatement();
			 rs = st.executeQuery(query);
			 
			 if(rs.next()) {
				 dto = new TransportTypeDto();
				 dto.setId(rs.getInt("id"));
				 dto.setFromDate(rs.getDate("fromDate"));
				 dto.setToDate(rs.getDate("toDate"));
				 dto.setSite(rs.getString("site_name"));
				 dto.setSiteId(rs.getString("site_id"));
				 dto.setVendor(rs.getString("vendor_name"));
				 dto.setVendorId(rs.getString("vendor_id"));
				 dto.setTransportTypeId(rs.getString("transport_type_id"));
				 dto.setTransportType(rs.getString("transport_type_name"));
				  
			 }

			 
		 }catch(Exception e) {
			 System.out.println("Error in getAcctiveTransportTypeMapping :"+ e);
		 }
		 finally{
			 DbConnect.closeResultSet(rs);
			 DbConnect.closeStatement(st);
			 closeConnection();
		 }
		 return dto;
				 
	}


	public int deleteTransportTypeMapping(TransportTypeDto dto) {
			
		int returnInt = 0;
		DbConnect ob = DbConnect.getInstance();
		  
		Connection con =getConnection();

		CallableStatement deletePst = null;
		 
		 
		try {
		 
			deletePst = con.prepareCall("{CALL DeleteTransportTypeMapping(?, ?)}");
			System.out.println("((( " + dto.getId());
			deletePst.setInt(1, dto.getId());
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

	public ArrayList<TransportTypeDto> getTransportTypeMappings(
			TransportTypeDto dtoParam) {
		

		Connection con=null;
		Statement st = null;
		ResultSet rs=null;
		ArrayList<TransportTypeDto> dtoList = null;
		 try {
			 String query = "select tm.id, tm.fromDate, tm.toDate, s.site_name, vm.Company vendor_name," +
			 		"  tt.name transport_type_name, s.id site_id, vm.id vendor_id, tt.id transport_type_id from transport_type_mapping tm , site s  , vendormaster vm, transport_type tt  where  tm.site=s.id and  tm.tran_type=tt.id and tm.vendor=vm.id    ";
			 if(dtoParam.getStatus().equals("future")) {
				 query = query + " and fromDate>now()   ";
			 } else if (dtoParam.getStatus().equals("present")) {
				 query = query + " and fromDate <=now() and ( toDate>=now() or toDate is null ) ";
			 } else if(dtoParam.getStatus().equals("past")){
				 query = query + "  and toDate < now()  ";
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
					 /*  where toDate < now() */
					/* where fromDate>now()   */
					/* where fromDate <=now() and ( toDate>=now() or toDate is null ) */
			 con = getConnection();
			 st = con.createStatement();
			 rs = st.executeQuery(query);
			 dtoList= new ArrayList<TransportTypeDto>();
			 while(rs.next()) {
				 TransportTypeDto dto = new TransportTypeDto();
				 dto.setId(rs.getInt("id"));
				 dto.setFromDate(rs.getDate("fromDate"));
				 dto.setToDate(rs.getDate("toDate"));
				 dto.setSite(rs.getString("site_name"));
				 dto.setSiteId(rs.getString("site_id"));
				 dto.setVendor(rs.getString("vendor_name"));
				 dto.setVendorId(rs.getString("vendor_id"));
				 dto.setTransportTypeId(rs.getString("transport_type_id"));
				 dto.setTransportType(rs.getString("transport_type_name"));
				 dtoList.add(dto);
			 }

			 
		 }catch(Exception e) {
			 System.out.println("Error in getTransportTypeMappings . :"+ e);
		 }
		 finally{
			 DbConnect.closeResultSet(rs);
			 DbConnect.closeStatement(st);
			 closeConnection();
		 }
		 return dtoList;
				 

		
	}

	 

}
