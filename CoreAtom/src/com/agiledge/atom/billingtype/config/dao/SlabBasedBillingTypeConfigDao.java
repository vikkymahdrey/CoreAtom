package com.agiledge.atom.billingtype.config.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.agiledge.atom.billingtype.config.dto.BillingSlabDto;
import com.agiledge.atom.billingtype.config.dto.BillingTypeConfigConstants;
import com.agiledge.atom.billingtype.config.dto.SlabBasedBillingConfigDto;
import com.agiledge.atom.constants.AuditLogConstants;
import com.agiledge.atom.dao.AuditLogDAO;
import com.agiledge.atom.dbConnect.DbConnect;
import com.agiledge.atom.dto.VehicleTypeDto;

public class SlabBasedBillingTypeConfigDao {

	public int addSlabBasedBillingConfig(SlabBasedBillingConfigDto dto) {
		// TODO Auto-generated method stub
		Connection con=null;
		PreparedStatement pst1=null;
		PreparedStatement acpst=null;
		PreparedStatement dcpst1=null;
		PreparedStatement dcpst2=null;
		ResultSet rrs= null;
		int avalue = 0;
		try {
			String aQuery = "insert into slab_based_billing_config (billing_type_refId, vehicle_type, escort_billing_type, escort_billing_rate, trip_rate, status) values (?,?,?,?,?,?) ";
			String bQuery = "insert into  slab_based_billing_slabs ( slab_billing_id, slabId, rate, status ) values (?, ?, ?, ?)";
			//String b2Query = "insert into billing_config_ac_constraint (bc_refId, fromTime, rate) values (?, ?, ?)";
			 
			DbConnect db = DbConnect.getInstance();
			
			con = db.connectDB();
			con.setAutoCommit(false);
			
			pst1 = con.prepareStatement(aQuery, Statement.RETURN_GENERATED_KEYS);
	 
			int step=0;
			pst1.setString(++step, dto.getRefId() );
			pst1.setString(++step, dto.getVehicleType());
			pst1.setString(++step, dto.getEscortRateType()); 
			pst1.setString(++step, dto.getEscortRate());
			pst1.setString(++step, dto.getTripRate());
			pst1.setString(++step, BillingTypeConfigConstants.ACTIVE);
			 
			avalue = pst1.executeUpdate( );
			if(avalue > 0) {
				rrs = pst1.getGeneratedKeys();
				if(rrs.next()) {
					int id = rrs.getInt(1);
					dto.setId(String.valueOf(id));
				 
					 
						acpst = con.prepareStatement(bQuery);
						 
						for(BillingSlabDto acDto : dto.getSlabList()) {
							int bvalue=0;
							  
								acpst.setLong(1, id);
								acpst.setString(2, acDto.getSlabId());
								acpst.setString(3, acDto.getRate());
								acpst.setString(4,  BillingTypeConfigConstants.ACTIVE );
								bvalue = acpst.executeUpdate();
							 
							if(bvalue<=0) {
								 avalue=0;
								con.rollback();
								break;
							}
						}
					}
					 
					
					new AuditLogDAO().auditLogEntry(Long.parseLong( dto.getId()), AuditLogConstants.SLAB_BASED_BILLING_MODULE, Integer.parseInt(dto.getDoneBy()), AuditLogConstants.WORKFLOW_STATE_EMPTY , AuditLogConstants.SLAB_BASED_BILLING_CREATED, AuditLogConstants.AUDIT_LOG_CREATE_ACTION	);
					con.commit();
			}
				
				 
			
		} catch(Exception e) {
			 System.out.println("Error in addSlabBasedBillingConfig : " + e);
			 try {
				 avalue=0;
				con.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				;
			}
		}
		
		finally {
		 
			DbConnect.closeResultSet(rrs);
			DbConnect.closeStatement(pst1,acpst,dcpst1, dcpst2);
			DbConnect.closeConnection(con);
		}
		return avalue;
	}

	
	
	public ArrayList<SlabBasedBillingConfigDto> getSlabBasedBillingConfig(String  refId) {
		// TODO Auto-generated method stub
		Connection con=null;
		PreparedStatement stmt=null;
		  
		 
		 
		ResultSet rs= null;
		 
		 
		ArrayList<SlabBasedBillingConfigDto> dtoList = new ArrayList<SlabBasedBillingConfigDto>(); 
		try {
			String query = "select sbbc.id, sbbc.billing_type_refId, sbbc.vehicle_type, sbbc.escort_billing_type, sbbc.escort_billing_rate, sbbc.trip_rate,sbbc.status, v.type vehicle_type_name from slab_based_billing_config sbbc join vehicle_type v on sbbc.vehicle_type=v.id where sbbc.status='" + BillingTypeConfigConstants.ACTIVE + "' and sbbc.billing_type_refId=?";
			  
			  
			DbConnect db = DbConnect.getInstance();
			
			con = db.connectDB();
			 
			
			stmt = con.prepareStatement(query );
			 
			stmt.setString(1, refId);
		 	
			rs = stmt.executeQuery();
			while(rs.next()) {
				 
				  			 
					SlabBasedBillingConfigDto dto = new SlabBasedBillingConfigDto();
					dto.setId(rs.getString("id"));
					dto.setRefId(rs.getString("billing_type_refId"));
					dto.setVehicleType(rs.getString("vehicle_type"));
					dto.setEscortRate(rs.getString("escort_billing_rate"));
					dto.setEscortRateType(rs.getString("escort_billing_type"));
					dto.setTripRate(rs.getString("trip_rate"));
					dto.setStatus(rs.getString("status"));
					  
					VehicleTypeDto vdto = new VehicleTypeDto();
					vdto .setId(rs.getInt("vehicle_type"));
					vdto.setType(rs.getString("vehicle_type_name"));
					dto.setVehicleTypeDto(vdto);
					
					
				  
						dto.setSlabList(getSlabRates(dto.getId()));
				 
					 			
				dtoList.add(dto);	
			}
			
		} catch(Exception e) {
			 System.out.println("Error in addSlabBasedBillingConfig : " + e);
			  
		}
		
		finally {
		 
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(stmt);
			DbConnect.closeConnection(con);
		}
		return dtoList;
	}
	
	 	

	public  SlabBasedBillingConfigDto getSlabBasedBillingConfig(String  refId, String vehicleTypeId) {
		// TODO Auto-generated method stub
		Connection con=null;
		PreparedStatement stmt=null;
		  
		 
		 
		ResultSet rs= null;
		SlabBasedBillingConfigDto dto = null;
		 
		  
		try {
			String query = "select sbbc.id, sbbc.billing_type_refId, sbbc.vehicle_type, sbbc.escort_billing_type, sbbc.escort_billing_rate, sbbc.trip_rate,sbbc.status, v.type vehicle_type_name from slab_based_billing_config sbbc join vehicle_type v on sbbc.vehicle_type=v.id where sbbc.status='" + BillingTypeConfigConstants.ACTIVE + "' and sbbc.billing_type_refId=? and vehicle_type=?";
			 System.out.println(" Query slab based : select sbbc.id, sbbc.billing_type_refId, sbbc.vehicle_type, sbbc.escort_billing_type, sbbc.escort_billing_rate, sbbc.trip_rate,sbbc.status, v.type vehicle_type_name from slab_based_billing_config sbbc join vehicle_type v on sbbc.vehicle_type=v.id where sbbc.status='" + BillingTypeConfigConstants.ACTIVE + "' and sbbc.billing_type_refId="+refId+" and vehicle_type="+vehicleTypeId+" "); 
			/*
		    "+refId+"
		    '"+billType+"'
		    "+vehicleTypeId+"
		   */
			DbConnect db = DbConnect.getInstance();
			
			con = db.connectDB();
			 
			
			stmt = con.prepareStatement(query );
			 
			stmt.setString(1, refId);
		 	stmt.setString(2, vehicleTypeId);
			rs = stmt.executeQuery();
			if(rs.next()) {
				  
				  			 
					dto = new SlabBasedBillingConfigDto();
					dto.setId(rs.getString("id"));
					dto.setRefId(rs.getString("billing_type_refId"));
					dto.setVehicleType(rs.getString("vehicle_type"));
					dto.setEscortRate(rs.getString("escort_billing_rate") );
					dto.setEscortRateType(rs.getString("escort_billing_type"));
					dto.setTripRate(rs.getString("trip_rate"));
					dto.setStatus(rs.getString("status"));
					  
					VehicleTypeDto vdto = new VehicleTypeDto();
					vdto .setId(rs.getInt("vehicle_type"));
					vdto.setType(rs.getString("vehicle_type_name"));
					dto.setVehicleTypeDto(vdto);
					
					
				   dto.setSlabList(getSlabRates(dto.getId()));
				   
				   
				   System.out.println(" \n..........\n");
				 
					 			
				 	
			}
			
		} catch(Exception e) {
			 System.out.println("Error in getSlabBasedBillingConfig : " + e);
			  
		}
		
		finally {
		 
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(stmt);
			DbConnect.closeConnection(con);
		}
		return dto;
	}
	
	 	

	
	public ArrayList<BillingSlabDto> getSlabRates(String  refId) {
		// TODO Auto-generated method stub
		Connection con=null;
		 
		PreparedStatement stmt=null;
		 
		 
		 
		ResultSet rs= null; 
		 
		ArrayList<BillingSlabDto> dtoList = new ArrayList<BillingSlabDto>(); 
		try {
			 
			String query= "select sbbs.id sbbsId, slab_billing_id, slabId, rate, status, startTime, endTime, traffic, speedpkm  from slab_based_billing_slabs sbbs join timesloat ts on sbbs.slabId = ts.id where status='" + BillingTypeConfigConstants.ACTIVE + "' and  slab_billing_id=?";
		 
			 
			DbConnect db = DbConnect.getInstance();
			
			con = db.connectDB();
			 
			
			stmt= con.prepareStatement(query);
			 
			stmt.setString(1, refId);
		 	
			rs = stmt.executeQuery();
			while(rs.next()) {
				 
					 
					BillingSlabDto dto = new BillingSlabDto();
					//dto.setId(rs.getString("id"));
					dto.setSlabBillingId(rs.getString("slab_billing_id"));
					dto.setSlabId(rs.getString("slabId"));
					dto.setStatus(rs.getString("status"));
					
					dto.setStartTime(rs.getString("startTime"));
					dto.setEndTime(rs.getString("endTime"));
					dto.setRate(rs.getString("rate"));
					dto.setId(rs.getString("sbbsId"));
					dto.setTrafic(rs.getString("traffic"));
					
					dtoList.add(dto);
					 		 			
			}
			
		} catch(Exception e) {
			 System.out.println("Error in getSlabRates : " + e);
			  
		}
		
		finally {
		 
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(stmt);
			DbConnect.closeConnection(con);
		}
		return dtoList;
	}
	

	public ArrayList<BillingSlabDto> getTimeSlabs( ) {
		// TODO Auto-generated method stub
		Connection con=null;
		 
		PreparedStatement stmt=null;
		 
		 
		 
		ResultSet rs= null; 
		 
		ArrayList<BillingSlabDto> dtoList = new ArrayList<BillingSlabDto>(); 
		try {
			 
			String query= "select id, startTime, endTime, traffic, speedpkm  from timesloat";
		 
			 
			DbConnect db = DbConnect.getInstance();
			
			con = db.connectDB();
			 
			
			stmt= con.prepareStatement(query);
			 
			 
		 	
			rs = stmt.executeQuery();
			while(rs.next()) {
				 
					 
					BillingSlabDto dto = new BillingSlabDto();
					//dto.setId(rs.getString("id"));
			 
			 
			 
					
					dto.setStartTime(rs.getString("startTime"));
					dto.setEndTime(rs.getString("endTime"));
					 
					dto.setId(rs.getString("id"));
					dto.setTrafic(rs.getString("traffic"));
					dto.setSpeedPerKm(rs.getString("speedpkm") );
					
					dtoList.add(dto);
					 		 			
			}
			
		} catch(Exception e) {
			 System.out.println("Error in getTimeSlabs : " + e);
			  
		}
		
		finally { 
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(stmt);
			DbConnect.closeConnection(con);
		}
		return dtoList;
	}
	

	 
	public int updateSlabBasedConfig(SlabBasedBillingConfigDto dto) {
		  
		Connection con=null;
		PreparedStatement deleteAcPst =  null;
	 
		PreparedStatement pst1=null;
		PreparedStatement acpst=null;
		PreparedStatement dcpst1=null;
		PreparedStatement dcpst2=null;
		ResultSet rrs= null;
		int avalue = 0;
		try {
			 	String deleteAcQuery = "update slab_based_billing_slabs set status='c' where status='a' and slab_billing_id=? ";
			 
			String aQuery = " update slab_based_billing_config set billing_type_refId=?, vehicle_type=?, escort_billing_type=?, escort_billing_rate=?, trip_rate=?, status='"+BillingTypeConfigConstants.ACTIVE+"' where id=?";
			String bQuery = "insert into slab_based_billing_slabs  ( slab_billing_id, slabId, rate, status ) values (?,?,?,'"+BillingTypeConfigConstants.ACTIVE+"')";
			// 
			 
			 
			DbConnect db = DbConnect.getInstance();
			
			con = db.connectDB();
			con.setAutoCommit(false);
			deleteAcPst = con.prepareStatement(deleteAcQuery);
			deleteAcPst.setString(1, dto.getId());
			deleteAcPst.executeUpdate();
			  
			
			pst1 = con.prepareStatement(aQuery, Statement.RETURN_GENERATED_KEYS);
			
			int step=0;
 					
			pst1.setString(++step, dto.getRefId());
			pst1.setString(++step, dto.getVehicleType() );
			pst1.setString(++step, dto.getEscortRateType());
			pst1.setString(++step, dto.getEscortRate());
			pst1.setString(++step, dto.getTripRate());
 
			pst1.setString(++step, dto.getId());
 
			avalue = pst1.executeUpdate( );
 
			if(avalue > 0) {
				rrs = pst1.getGeneratedKeys();
				 
					   
					
 						acpst = con.prepareStatement(bQuery);
						  
						for(BillingSlabDto acDto : dto.getSlabList()) {
							int bvalue=0;
								 
								acpst.setLong(1, Long.parseLong( dto.getId()));
								  
								acpst.setString(2, acDto.getSlabId());
								acpst.setString(3, acDto.getRate());
							 
								bvalue = acpst.executeUpdate();
							 
							if(bvalue<=0) {
								 avalue=0;
								con.rollback();
								break;
							}
						}
						
						con.commit();
					}
			 
			
		} catch(Exception e) {
			 System.out.println("Error in updateSlabBasedConfig : " + e);
			 try {
				 avalue=0;
				con.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				;
			}
		}
		
		finally {
		 
			DbConnect.closeResultSet(rrs);
			DbConnect.closeStatement(pst1,acpst,dcpst1, dcpst2);
			DbConnect.closeConnection(con);
		}
		return avalue;

	}
	


	public int deleteSlabBasedConfig(SlabBasedBillingConfigDto dto) {
		  
		Connection con=null;
		PreparedStatement deleteAcPst =  null;
		PreparedStatement deleteDcPst =  null;
		PreparedStatement pst1=null;
		PreparedStatement acpst=null;
		PreparedStatement dcpst1=null;
		PreparedStatement dcpst2=null;
		ResultSet rrs= null;
		int avalue = 0;
		try {
			String deleteAcQuery = "update slab_based_billing_slabs set status='c' where status='a' and  slab_billing_id=? ";
		 	String aQuery = "update slab_based_billing_config set status='c' where status='a' and id=?";
			 
			//String b2Query = "insert into billing_config_ac_constraint (bc_refId, fromTime, rate) values (?, ?, ?)";
		 
		 
			DbConnect db = DbConnect.getInstance();
			
			con = db.connectDB();
			con.setAutoCommit(false);
			deleteAcPst = con.prepareStatement(deleteAcQuery);
			deleteAcPst.setString(1, dto.getId());
			deleteAcPst.executeUpdate();
			 
			pst1 = con.prepareStatement(aQuery, Statement.RETURN_GENERATED_KEYS);
			int step=0;
			 
			pst1.setString(++step, dto.getId());
			avalue = pst1.executeUpdate( );
			if(avalue > 0) {
				rrs = pst1.getGeneratedKeys();
				if(rrs.next()) {
					int id = rrs.getInt(1);
					
					 
					
					new AuditLogDAO().auditLogEntry( id, AuditLogConstants.SLAB_BASED_BILLING_MODULE, Integer.parseInt(dto.getDoneBy()), AuditLogConstants.WORKFLOW_STATE_EMPTY , AuditLogConstants.SLAB_BASED_BILLING_DELETED, AuditLogConstants.AUDIT_LOG_CREATE_ACTION	);
					con.commit();
				
				
				}
			}
			
		} catch(Exception e) {
			 System.out.println("Error deleteSlabBasedBillingConfig : " + e);
			 try {
				 avalue=0;
				con.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				;
			}
		}
		
		finally {
		 
			DbConnect.closeResultSet(rrs);
			DbConnect.closeStatement(pst1,acpst,dcpst1, dcpst2);
			DbConnect.closeConnection(con);
		}
		return avalue;

	}
	


}
