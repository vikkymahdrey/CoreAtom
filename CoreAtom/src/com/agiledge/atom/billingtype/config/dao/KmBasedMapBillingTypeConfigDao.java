package com.agiledge.atom.billingtype.config.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import com.agiledge.atom.billingtype.config.dto.AcConstraintDto;
import com.agiledge.atom.billingtype.config.dto.BillingTypeConfigConstants;
import com.agiledge.atom.billingtype.config.dto.KmBasedBillingConfigConstants;
import com.agiledge.atom.billingtype.config.dto.KmBasedMapTripBillingConfigDto;
import com.agiledge.atom.constants.AuditLogConstants;
import com.agiledge.atom.dao.AuditLogDAO;
import com.agiledge.atom.dbConnect.DbConnect;
import com.agiledge.atom.dto.VehicleTypeDto;

public class KmBasedMapBillingTypeConfigDao {
	
	 
	public int addKmBasedMapBillingConfig(KmBasedMapTripBillingConfigDto dto) {
		// TODO Auto-generated method stub
		Connection con=null;
		PreparedStatement pst1=null;
		PreparedStatement acpst=null;
		ResultSet rrs= null;
		int avalue = 0;
		try {
			 
			 
			 String aQuery = "insert into km_based_map_trip_config ( km_billing_id, vehicle_type, distance_type, calculation_type, rate, trip_rate, escort_billing_type, escort_billing_rate, ac_constraint ) values (?, ?, ?, ?, ?, ?, ?, ?, ?)";
			 String bQuery = "insert into map_billing_config_ac_constraint (bc_refId, fromTime, toTime, rate) values (?, ?, ?, ?)";
			 
			DbConnect db = DbConnect.getInstance();
			
			con = db.connectDB();
			con.setAutoCommit(false);
			
			pst1 = con.prepareStatement(aQuery, Statement.RETURN_GENERATED_KEYS);
			int step=0;
			pst1.setString(++step, dto.getKmBillingType() );
			pst1.setString(++step, dto.getVehicleTypeId());
			pst1.setString(++step, dto.getDistanceType() );
			pst1.setString(++step, dto.getCalculationType() );
			pst1.setString(++step, dto.getRate());
			pst1.setString(++step, dto.getTripRate());
			pst1.setString(++step, dto.getEscortRateType());
			pst1.setString(++step, dto.getEscortRate());
			pst1.setString(++step, dto.getAcYes());
			 avalue = pst1.executeUpdate( );
			if(avalue > 0) {
				rrs = pst1.getGeneratedKeys();
				if(rrs.next()) {
					int id = rrs.getInt(1);
					if(dto.getAcYes().equals( BillingTypeConfigConstants.AC_YES )) {
						 
						acpst = con.prepareStatement(bQuery);
						 
						for(AcConstraintDto acDto : dto.getAcList()) {
							int bvalue=0;
							  
								acpst.setLong(1, id);
								acpst.setString(2, acDto.getFromTime());
								acpst.setString(3, acDto.getToTime());
								acpst.setString(4, acDto.getRate());
								bvalue = acpst.executeUpdate();
							 
							if(bvalue<=0) {
								 avalue=0;
								con.rollback();
								break;
							}
						}
					}
			
			
					new AuditLogDAO().auditLogEntry( id, AuditLogConstants.TRIP_BASED_BILLING_MODULE, Integer.parseInt(dto.getDoneBy()), AuditLogConstants.WORKFLOW_STATE_EMPTY , AuditLogConstants.KM_BASED_MAP_BILLING_CONFIGURATION_CREATED, AuditLogConstants.AUDIT_LOG_CREATE_ACTION	);
					con.commit();
					
				}
			}
			
		} catch(Exception e) {
			 System.out.println("Error in addTripBasedBillingConfig : 11111111" + e);
			 avalue=0;
		}
		
		finally {
		 
			DbConnect.closeResultSet(rrs);
			DbConnect.closeStatement(pst1,acpst );
			DbConnect.closeConnection(con);
		}
		return avalue;
	}

	public ArrayList<KmBasedMapTripBillingConfigDto> getKmBasedMapBillingConfig(
			String refId) {
		Connection con=null;
		PreparedStatement stmt=null;
		String billType= KmBasedBillingConfigConstants.MAP;
		 
		 
		ResultSet rs= null;
		 
		 
		ArrayList<KmBasedMapTripBillingConfigDto> dtoList = new ArrayList<KmBasedMapTripBillingConfigDto>(); 
		try {
			String query = "select  kt.billing_type_id, kmc.id, kmc.km_billing_id, kmc.vehicle_type, kmc.distance_type, kmc.calculation_type, kmc.rate, vt.type vehicleTypeName, ifnull( escort_billing_type,'') escort_billing_type,ifnull( escort_billing_rate,'') escort_billing_rate, ifnull( ac_constraint,'') ac_constraint, ifnull( trip_rate ,0)trip_rate   from km_based_map_trip_config kmc join km_based_trip_config kt on kmc.km_billing_id=kt.id join vehicle_type vt on kmc.vehicle_type=vt.id where kmc.status='a' and kt.status='a' and kt.billing_type_id=?  and kt.km_billing_type=?";
			  
			  System.out.println("query " + query + "" + billType + " , " + refId);
			DbConnect db = DbConnect.getInstance();
			
			con = db.connectDB();
			 
			
			stmt = con.prepareStatement(query );
			
			stmt.setString(1, refId);
			stmt.setString(2, billType);
		 	
			rs = stmt.executeQuery();
			while(rs.next()) {
				 
					 
					KmBasedMapTripBillingConfigDto dto = new KmBasedMapTripBillingConfigDto();
					dto.setBillingRefId(refId);
					dto.setId(rs.getString("id"));
					dto.setKmBillingRefId(rs.getString("km_billing_id"));
					 
					dto.setRate(rs.getString("rate"));
			 
					dto.setVehicleTypeId(rs.getString("vehicle_type"));
			 		 
					VehicleTypeDto vdto = new VehicleTypeDto();
					vdto .setId(rs.getInt("vehicle_type"));
					vdto.setType(rs.getString("vehicleTypeName"));
					dto.setVehicleTypeDto(vdto);
					dto.setDistanceType(rs.getString("distance_type"));
					dto.setCalculationType(rs.getString("calculation_type"));
					dto.setTripRate(rs.getString("trip_rate"));
					dto.setAcYes(rs.getString("ac_constraint"));
					dto.setEscortRate(rs.getString("escort_billing_rate"));
					dto.setEscortRateType(rs.getString("escort_billing_type"));
					dto.setTripRate(rs.getString("trip_rate"));
					if(dto.getAcYes().equalsIgnoreCase(KmBasedBillingConfigConstants.AC_YES)) {
						
						dto.setAcList(getMapBasedBillingConfigAcConstraints(dto.getId()));
					}
					 			
				dtoList.add(dto);	
			}
			
		} catch(Exception e) {
			 System.out.println("Error in addTripBasedBillingConfig : 22222222" + e);
			  
		}
		
		finally {
		 
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(stmt);
			DbConnect.closeConnection(con);
		}
		return dtoList;
	}
	
	public ArrayList<KmBasedMapTripBillingConfigDto> getKmBasedMapBillingConfig(
			String refId, String vehicleTypeId) {
		Connection con=null;
		PreparedStatement stmt=null;
		String billType= KmBasedBillingConfigConstants.MAP;
		 
		 
		ResultSet rs= null;
		 
		 
		ArrayList<KmBasedMapTripBillingConfigDto> dtoList = new ArrayList<KmBasedMapTripBillingConfigDto>(); 
		try {
			String query = "select  kt.billing_type_id, kmc.id, kmc.km_billing_id, kmc.vehicle_type, kmc.distance_type, kmc.calculation_type, kmc.rate, vt.type vehicleTypeName, ifnull( escort_billing_type,'') escort_billing_type,ifnull( escort_billing_rate,'') escort_billing_rate, ifnull( ac_constraint,'') ac_constraint, ifnull( trip_rate ,0)trip_rate   from km_based_map_trip_config kmc join km_based_trip_config kt on kmc.km_billing_id=kt.id join vehicle_type vt on kmc.vehicle_type=vt.id where kmc.status='a' and kt.status='a' and kt.billing_type_id=?  and kt.km_billing_type=? and kmc.vehicle_type=? ";
			
			  
			  System.out.println("query MAP based : select  kt.billing_type_id, kmc.id, kmc.km_billing_id, kmc.vehicle_type, kmc.distance_type, kmc.calculation_type, kmc.rate, vt.type vehicleTypeName, ifnull( escort_billing_type,'') escort_billing_type,ifnull( escort_billing_rate,'') escort_billing_rate, ifnull( ac_constraint,'') ac_constraint, ifnull( trip_rate ,0)trip_rate   from km_based_map_trip_config kmc join km_based_trip_config kt on kmc.km_billing_id=kt.id join vehicle_type vt on kmc.vehicle_type=vt.id where kmc.status='a' and kt.status='a' and kt.billing_type_id="+refId+"  and kt.km_billing_type='"+billType+"' and kmc.vehicle_type="+vehicleTypeId+" ");
			  /*
			    "+refId+"
			    '"+billType+"'
			    "+vehicleTypeId+"
			   */
			DbConnect db = DbConnect.getInstance();
			
			con = db.connectDB();
			 
			
			stmt = con.prepareStatement(query );
			
			stmt.setString(1, refId);
			stmt.setString(2, billType);
			stmt.setString(3, vehicleTypeId);
		 	
			rs = stmt.executeQuery();
			while(rs.next()) {
				 
					 
					KmBasedMapTripBillingConfigDto dto = new KmBasedMapTripBillingConfigDto();
					dto.setBillingRefId(refId);
					dto.setId(rs.getString("id"));
					dto.setKmBillingRefId(rs.getString("km_billing_id"));
					 
					dto.setRate(rs.getString("rate"));
			 
					dto.setVehicleTypeId(rs.getString("vehicle_type"));
			 		 
					VehicleTypeDto vdto = new VehicleTypeDto();
					vdto .setId(rs.getInt("vehicle_type"));
					vdto.setType(rs.getString("vehicleTypeName"));
					dto.setVehicleTypeDto(vdto);
					dto.setDistanceType(rs.getString("distance_type"));
					dto.setCalculationType(rs.getString("calculation_type"));
					dto.setTripRate(rs.getString("trip_rate"));
					dto.setAcYes(rs.getString("ac_constraint"));
					dto.setEscortRate(rs.getString("escort_billing_rate"));
					dto.setEscortRateType(rs.getString("escort_billing_type"));
					dto.setTripRate(rs.getString("trip_rate"));
					if(dto.getAcYes().equalsIgnoreCase(KmBasedBillingConfigConstants.AC_YES)) {
						
						dto.setAcList(getMapBasedBillingConfigAcConstraints(dto.getId()));
					}
					 			
				dtoList.add(dto);	
			}
			
		} catch(Exception e) {
			 System.out.println("Error in addTripBasedBillingConfig : 33333333" + e);
			  
		}
		
		finally {
		 
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(stmt);
			DbConnect.closeConnection(con);
		}
		return dtoList;
	}
	
	
	public ArrayList<AcConstraintDto> getMapBasedBillingConfigAcConstraints(String  refId) {
		// TODO Auto-generated method stub
		Connection con=null;
		 
		PreparedStatement stmt=null;
		 
		 
		 
		ResultSet rs= null; 
		 
		ArrayList<AcConstraintDto> dtoList = new ArrayList<AcConstraintDto>(); 
		try {
			 
			String query= "select id, bc_refId, fromTime, toTime, rate from map_billing_config_ac_constraint where status='" + BillingTypeConfigConstants.ACTIVE + "' and  bc_refId=?";
		 
			 
			DbConnect db = DbConnect.getInstance();
			
			con = db.connectDB();
			 
			
			stmt= con.prepareStatement(query);
			 
			stmt.setString(1, refId);
		 	
			rs = stmt.executeQuery();
			while(rs.next()) {
				 
					 
					AcConstraintDto dto = new AcConstraintDto();
					dto.setParentId(refId);
					dto.setFromTime(rs.getString("fromTime"));
					dto.setToTime(rs.getString("toTime"));
					dto.setRate(rs.getString("rate"));
					dto.setId(rs.getString("id"));
					dtoList.add(dto);
					 		 			
			}
			
		} catch(Exception e) {
			 System.out.println("Error in addTripBasedBillingConfig : 444444" + e);
			  
		}
		
		finally {
		 
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(stmt);
			DbConnect.closeConnection(con);
		}
		return dtoList;
	}
	


	public boolean checkDuplication(KmBasedMapTripBillingConfigDto dto) {
		//  

		 
		boolean flag = false;
		Connection con=null;
		PreparedStatement stmt=null;
		 
		 
		 
		ResultSet rs= null;
		 
		 
		  
		try {
			String query = "select * from km_based_map_trip_config where status='a' and km_billing_id=? and  vehicle_type=?";
			   
			DbConnect db = DbConnect.getInstance();
			
			con = db.connectDB();
		 	stmt = con.prepareStatement(query );
			stmt.setString(1, dto.getKmBillingType());
			stmt.setString(2, dto.getVehicleTypeId());
		 	
			rs = stmt.executeQuery();
			if(rs.next()) {
				flag=true;
			 }
			
		} catch(Exception e) {
			 System.out.println("Error in checkDuplication : " + e);
			  
		}
		
		finally {
		 
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(stmt);
			DbConnect.closeConnection(con);
		}
		return flag;

	}

	public int updateKmBasedMapBillingConfig(KmBasedMapTripBillingConfigDto dto) {
		// TODO Auto-generated method stub
		Connection con=null;
		PreparedStatement pst1=null;
		PreparedStatement deleteAcPst=null;
		PreparedStatement acpst=null;
		ResultSet rrs= null;
		int avalue = 0;
		try {
			 
			String deleteAcQuery = "update map_billing_config_ac_constraint set status='c' where status='a' and  bc_refId=? ";
			 String aQuery = "update km_based_map_trip_config set  distance_type=?, calculation_type=?, rate=?, escort_billing_type=?, escort_billing_rate=?, ac_constraint=?, trip_rate=? where status='a' and id=?";
			 String bQuery = "insert into map_billing_config_ac_constraint (bc_refId, fromTime, toTime, rate) values (?, ?, ?, ?)";
			DbConnect db = DbConnect.getInstance();
			
			con = db.connectDB();
			con.setAutoCommit(false);
			deleteAcPst = con.prepareStatement(deleteAcQuery);
			deleteAcPst.setString(1, dto.getId());
			deleteAcPst.executeUpdate();
			
			pst1 = con.prepareStatement(aQuery, Statement.RETURN_GENERATED_KEYS);
			int step=0;
			
			pst1.setString(++step, dto.getDistanceType() );
			pst1.setString(++step, dto.getCalculationType() );
			pst1.setString(++step, dto.getRate());
		
			pst1.setString(++step, dto.getEscortRateType());
			pst1.setString(++step, dto.getEscortRate());
			pst1.setString(++step, dto.getAcYes());
			pst1.setString(++step, dto.getTripRate() );
			 
			pst1.setString(++step, dto.getId());
			 avalue = pst1.executeUpdate( );
			if(avalue > 0) {
				rrs = pst1.getGeneratedKeys();
				if(rrs.next()) {
					int id = rrs.getInt(1);

					if(dto.getAcYes().equals( BillingTypeConfigConstants.AC_YES )) {
					 
						acpst = con.prepareStatement(bQuery);
						  
						for(AcConstraintDto acDto : dto.getAcList()) {
							int bvalue=0;
								System.out.println("insert into billing_config_ac_constraint (bc_refId, fromTime, toTime, rate) values (" + dto.getId() + ", '" + acDto.getFromTime() +"', '" + acDto.getToTime() +"', "+ acDto.getRate()+")");
								acpst.setLong(1, Long.parseLong( dto.getId()));
								acpst.setString(2, acDto.getFromTime());
								acpst.setString(3, acDto.getToTime());
								acpst.setString(4, acDto.getRate());
								bvalue = acpst.executeUpdate();
								System.out.println(" ........  5");
							if(bvalue<=0) {
								 avalue=0;
								con.rollback();
								break;
							}
						}
					}
					new AuditLogDAO().auditLogEntry( id, AuditLogConstants.TRIP_BASED_BILLING_MODULE, Integer.parseInt(dto.getDoneBy()), AuditLogConstants.WORKFLOW_STATE_EMPTY , AuditLogConstants.KM_BASED_MAP_BILLING_CONFIGURATION_CREATED, AuditLogConstants.AUDIT_LOG_CREATE_ACTION	);
					con.commit();
				}
			}
			
		} catch(Exception e) {
			 System.out.println("Error in updateKmBasedMapBillingConfig : " + e);
			 avalue=0;
		}
		
		finally {
		 
			DbConnect.closeResultSet(rrs);
			DbConnect.closeStatement(pst1 );
			DbConnect.closeConnection(con);
		}
		return avalue;
	}
	
	public int deleteKmBasedMapBillingConfig(KmBasedMapTripBillingConfigDto dto) {
		// TODO Auto-generated method stub
		Connection con=null;
		PreparedStatement pst1=null;
		 
		ResultSet rrs= null;
		int avalue = 0;
		try {
			 
			 
			 String aQuery = "update km_based_map_trip_config set status='c' where  id=?";
			 
			DbConnect db = DbConnect.getInstance();
			
			con = db.connectDB();
			 
			
			pst1 = con.prepareStatement(aQuery, Statement.RETURN_GENERATED_KEYS);
			int step=0;
			pst1.setString(++step, dto.getId());
			 avalue = pst1.executeUpdate( );
			if(avalue > 0) {
				rrs = pst1.getGeneratedKeys();
				if(rrs.next()) {
					int id = rrs.getInt(1);
					new AuditLogDAO().auditLogEntry( id, AuditLogConstants.TRIP_BASED_BILLING_MODULE, Integer.parseInt(dto.getDoneBy()), AuditLogConstants.WORKFLOW_STATE_EMPTY , AuditLogConstants.KM_BASED_MAP_BILLING_CONFIGURATION_DELETED, AuditLogConstants.AUDIT_LOG_CREATE_ACTION	);
			 
				}
			}
			
		} catch(Exception e) {
			 System.out.println("Error in updateKmBasedMapBillingConfig : " + e);
			 avalue=0;
		}
		
		finally {
		 
			DbConnect.closeResultSet(rrs);
			DbConnect.closeStatement(pst1 );
			DbConnect.closeConnection(con);
		}
		return avalue;
	}
	
	
  
}
