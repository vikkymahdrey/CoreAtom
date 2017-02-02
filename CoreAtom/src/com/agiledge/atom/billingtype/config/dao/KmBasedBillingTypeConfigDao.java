package com.agiledge.atom.billingtype.config.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import com.agiledge.atom.billingtype.config.dto.KmBasedBillingConfigDto;
import com.agiledge.atom.constants.AuditLogConstants;
import com.agiledge.atom.dao.AuditLogDAO;
import com.agiledge.atom.dbConnect.DbConnect;

public class KmBasedBillingTypeConfigDao {
	
	public int addKmBasedBillingConfig(KmBasedBillingConfigDto dto) {
		// TODO Auto-generated method stub
		Connection con=null;
		PreparedStatement pst1=null;
		PreparedStatement selectPst = null;
		 
		 ResultSet rrs= null;
		 ResultSet crs= null;
		  
		int avalue = 0;
		try {
			String aQuery = "insert into km_based_trip_config (billing_type_id, km_billing_type ) values (?, ? )";
			String checkQuery = "select id, billing_type_id from km_based_trip_config where status='a' and billing_type_id=?";
			String updateQuery="update km_based_trip_config set km_billing_type=? where status='a' and billing_type_id=?";
		 	DbConnect db = DbConnect.getInstance();
			
			con = db.connectDB();
			 System.out.println("Check query : " + checkQuery + "("+ dto.getBillingRefId() +")");
			selectPst = con.prepareStatement(checkQuery);
			selectPst.setString(1, dto.getBillingRefId());
			crs = selectPst.executeQuery();
			if(crs.next()) {
				int id = crs.getInt("id");
				pst1 = con.prepareStatement(updateQuery );
				int step=0;
				 
				pst1.setString(++step, dto.getKmBillingType() );
				pst1.setString(++step, dto.getBillingRefId());
				 avalue = pst1.executeUpdate( );
				if(avalue > 0) {
					System.out.println("value updated ");
					 
					 
				 
						new AuditLogDAO().auditLogEntry( id, AuditLogConstants.KM_BASED_BILLING_MODULE, Integer.parseInt(dto.getDoneBy()),  "" , AuditLogConstants.KM_BASED_BILLING_CONFIGURATION_UPDATED, AuditLogConstants.AUDIT_LOG_CREATE_ACTION	);
				  	
					 
				}
				
			} else {
			
			pst1 = con.prepareStatement(aQuery, Statement.RETURN_GENERATED_KEYS);
			int step=0;
			
			
			pst1.setString(++step, dto.getBillingRefId());
			pst1.setString(++step, dto.getKmBillingType() );
			
			 avalue = pst1.executeUpdate( );
			if(avalue > 0) {
				rrs = pst1.getGeneratedKeys();
				if(rrs.next()) {
					int id = rrs.getInt(1);
					new AuditLogDAO().auditLogEntry( id, AuditLogConstants.KM_BASED_BILLING_MODULE, Integer.parseInt(dto.getDoneBy()), AuditLogConstants.WORKFLOW_STATE_EMPTY , AuditLogConstants.KM_BASED_BILLING_CONFIGURATION_CREATED, AuditLogConstants.AUDIT_LOG_CREATE_ACTION	);
			  	
				}
			}
			}
			
		} catch(Exception e) {
			 System.out.println("Error in addKmBasedBillingConfig : " + e);
			 avalue=0;
		}
		finally {
			 
			DbConnect.closeResultSet(rrs, crs);
			DbConnect.closeStatement(pst1, selectPst );
			DbConnect.closeConnection(con);
		} 
		
		return avalue;
	}
	
	
	public  KmBasedBillingConfigDto  getKmBasedBillingConfig(String  refId) {
		// TODO Auto-generated method stub
		Connection con=null;
		PreparedStatement stmt=null;
		KmBasedBillingConfigDto dto = null;
		  
		 
		 
		ResultSet rs= null;
		 
		 
		  
		try {
			String query = "select kbt.id, kbt.billing_type_id, kbt.km_billing_type, kbt.status, btm.site, btm.fromDate, btm.vendor from billing_type_mapping btm join km_based_trip_config  kbt on kbt.billing_type_id=btm.id where kbt.status='a' and kbt.billing_type_id=?";
			  System.out.println("km based config :" + query + " " + refId  );
			  
			DbConnect db = DbConnect.getInstance();
			
			con = db.connectDB();
			 
			
			stmt = con.prepareStatement(query );
			 
			stmt.setString(1, refId);
		 	
			rs = stmt.executeQuery();
			if(rs.next()) {
				 
					 
					dto = new KmBasedBillingConfigDto();
					dto.setBillingRefId(refId);
					dto.setId(rs.getString("id"));
					dto.setBillingRefId(rs.getString("billing_type_id"));
					dto.setKmBillingType(rs.getString("km_billing_type") );
					dto.setSite(rs.getString("site"));
					System.out.println(dto.getKmBillingType());
				  	 
			}
			
		} catch(Exception e) {
			 System.out.println("Error in getKmBasedBillingConfig : " + e);
			  
		}
		
		finally {
		 
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(stmt);
			DbConnect.closeConnection(con);
		}
		return dto;
	}
	
	
 	

  
}
