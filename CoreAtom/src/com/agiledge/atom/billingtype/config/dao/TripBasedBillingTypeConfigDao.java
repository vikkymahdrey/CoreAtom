package com.agiledge.atom.billingtype.config.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.agiledge.atom.billingtype.config.dto.AcConstraintDto;
import com.agiledge.atom.billingtype.config.dto.BillingTypeConfigConstants;
import com.agiledge.atom.billingtype.config.dto.DistanceConstraintDto;
import com.agiledge.atom.billingtype.config.dto.TripBasedBillingConfigDto;
import com.agiledge.atom.commons.OtherFunctions;
import com.agiledge.atom.constants.AuditLogConstants;
import com.agiledge.atom.dao.AuditLogDAO;
import com.agiledge.atom.dbConnect.DbConnect;
import com.agiledge.atom.dto.VehicleTypeDto;

public class TripBasedBillingTypeConfigDao {

	public int addTripBasedBillingConfig(TripBasedBillingConfigDto dto) {
		// TODO Auto-generated method stub
		Connection con=null;
		PreparedStatement pst1=null;
		PreparedStatement acpst=null;
		PreparedStatement dcpst1=null;
		PreparedStatement dcpst2=null;
		ResultSet rrs= null;
		int avalue = 0;
		try {
			String aQuery = "insert into trip_based_config (billing_type_refId, vehicle_type, distance_constraint, ac_constraint, escort_billing_type, escort_billing_rate, trip_rate) values (?, ?, ?, ?, ?, ?, ?)";
			String bQuery = "insert into billing_config_ac_constraint (bc_refId, fromTime, toTime, rate) values (?, ?, ?, ?)";
			//String b2Query = "insert into billing_config_ac_constraint (bc_refId, fromTime, rate) values (?, ?, ?)";
			String cQuery1 = "insert into billing_config_distance_constraint ( bc_refId, fromKm, toKm, rate, dcAcRate ) values (?, ?, ?, ?, ?)";
			String cQuery2 = "insert into billing_config_distance_constraint ( bc_refId, fromKm, rate, dcAcRate ) values (?, ?, ?, ?)";
			DbConnect db = DbConnect.getInstance();
			
			con = db.connectDB();
			con.setAutoCommit(false);
			
			pst1 = con.prepareStatement(aQuery, Statement.RETURN_GENERATED_KEYS);
			int step=0;
			pst1.setString(++step, dto.getBillingRefId());
			pst1.setString(++step, dto.getVehicleTypeId());
			pst1.setString(++step, dto.getDcYes());
			pst1.setString(++step, dto.getAcYes());
			pst1.setString(++step, dto.getEscortRateType());
			pst1.setString(++step, dto.getEscortRate());
			pst1.setString(++step, dto.getFlatTripRate() );
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
					if(dto.getDcYes().equals( BillingTypeConfigConstants.DC_YES )) {
						 
						dcpst1 = con.prepareStatement(cQuery1);
						dcpst2 = con.prepareStatement( cQuery2);
						for(DistanceConstraintDto dcDto : dto.getDcList()) {
							int bvalue=0;
							if(OtherFunctions.isEmpty(dcDto.getToKm()) ) {
								dcpst2.setLong(1, id);
								dcpst2.setString(2, dcDto.getFromKm() );
								dcpst2.setString(3, dcDto.getRate());
								dcpst2.setString(4, dcDto.getDcAcRate());
								bvalue = dcpst2.executeUpdate();
								 
							} else {
								dcpst1.setLong(1, id);
								dcpst1.setString(2, dcDto.getFromKm());
								dcpst1.setString(3, dcDto.getToKm());
								dcpst1.setString(4, dcDto.getRate());
								dcpst1.setString(5, dcDto.getDcAcRate());
								bvalue = dcpst1.executeUpdate();
							}
							if(bvalue<=0) {
								
								con.rollback();
								break;
							}
						}
					}
					
					new AuditLogDAO().auditLogEntry( id, AuditLogConstants.TRIP_BASED_BILLING_MODULE, Integer.parseInt(dto.getDoneBy()), AuditLogConstants.WORKFLOW_STATE_EMPTY , AuditLogConstants.TRIP_BASED_BILLING_CONFIGURATION_CREATED, AuditLogConstants.AUDIT_LOG_CREATE_ACTION	);
					con.commit();
				
				
				}
			}
			
		} catch(Exception e) {
			 System.out.println("Error in addTripBasedBillingConfig : " + e);
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

	
	
	public ArrayList<TripBasedBillingConfigDto> getTripBasedBillingConfig(String  refId) {
		// TODO Auto-generated method stub
		Connection con=null;
		PreparedStatement stmt=null;
		  
		 
		 
		ResultSet rs= null;
		 
		 
		ArrayList<TripBasedBillingConfigDto> dtoList = new ArrayList<TripBasedBillingConfigDto>(); 
		try {
			String query = "select tbc.id, ifnull(tbc.trip_rate,'0') trip_rate ,  billing_type_refId, vehicle_type, distance_constraint, ac_constraint, escort_billing_type, escort_billing_rate, vt.type vehicleTypeName from trip_based_config tbc join vehicle_type vt on tbc.vehicle_type= vt.id where status='" + BillingTypeConfigConstants.ACTIVE + "' and billing_type_refId=?";
			  
			  
			DbConnect db = DbConnect.getInstance();
			
			con = db.connectDB();
			 
			
			stmt = con.prepareStatement(query );
			 
			stmt.setString(1, refId);
		 	
			rs = stmt.executeQuery();
			while(rs.next()) {
				 
					 
					TripBasedBillingConfigDto dto = new TripBasedBillingConfigDto();
					dto.setBillingRefId(refId);
					dto.setId(rs.getString("id"));
					dto.setBillingRefId( rs.getString("billing_type_refId"));
					dto.setVehicleTypeId(rs.getString("vehicle_type"));
					dto.setDcYes(rs.getString("distance_constraint"));
					dto.setAcYes(rs.getString("ac_constraint")); 
					dto.setEscortRateType(rs.getString("escort_billing_type"));
					dto.setEscortRate(rs.getString("escort_billing_rate"));
					dto.setFlatTripRate(rs.getString("trip_rate"));
					 
					VehicleTypeDto vdto = new VehicleTypeDto();
					vdto .setId(rs.getInt("vehicle_type"));
					vdto.setType(rs.getString("vehicleTypeName"));
					dto.setVehicleTypeDto(vdto);
					
					
					if(dto.getAcYes().equals( BillingTypeConfigConstants.AC_YES )) {
						dto.setAcList(getTripBasedBillingConfigAcConstraints(dto.getId()));
					}
					if(dto.getDcYes().equals( BillingTypeConfigConstants.DC_YES )) {
						dto.setDcList(getTripBasedBillingConfigDistanceConstraints(dto.getId()));
					}
					 			
				dtoList.add(dto);	
			}
			
		} catch(Exception e) {
			 System.out.println("Error in addTripBasedBillingConfig : " + e);
			  
		}
		
		finally {
		 
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(stmt);
			DbConnect.closeConnection(con);
		}
		return dtoList;
	}
	
	public  TripBasedBillingConfigDto  getTripBasedBillingConfig(String  refId, String vehicleId) {
		// TODO Auto-generated method stub
		Connection con=null;
		PreparedStatement stmt=null;
		  
		 
		 
		ResultSet rs= null;
		 
		TripBasedBillingConfigDto  dto = null;  
		try {
			String query = "select tbc.id, ifnull(tbc.trip_rate,'0') trip_rate ,  billing_type_refId, vehicle_type, distance_constraint, ac_constraint, escort_billing_type, escort_billing_rate, vt.type vehicleTypeName from trip_based_config tbc join vehicle_type vt on tbc.vehicle_type= vt.id where status='" + BillingTypeConfigConstants.ACTIVE + "' and billing_type_refId=? and vt.id=? ";
			 System.out.println("query trip based : select tbc.id, ifnull(tbc.trip_rate,'0') trip_rate ,  billing_type_refId, vehicle_type, distance_constraint, ac_constraint, escort_billing_type, escort_billing_rate, vt.type vehicleTypeName from trip_based_config tbc join vehicle_type vt on tbc.vehicle_type= vt.id where status='" + BillingTypeConfigConstants.ACTIVE + "' and billing_type_refId="+refId+" and vt.id=   "+vehicleId+" ");
			 /*
			    "+refId+"
			    '"+billType+"'
			    "+vehicleTypeId+"
			   */
			DbConnect db = DbConnect.getInstance();
			
			con = db.connectDB();
			 
			
			stmt = con.prepareStatement(query );
			 
			stmt.setString(1, refId);
		 	stmt.setString(2, vehicleId);
			rs = stmt.executeQuery();
			if(rs.next()) {
				 
					 
					 dto = new TripBasedBillingConfigDto();
					dto.setBillingRefId(refId);
					dto.setId(rs.getString("id"));
					dto.setBillingRefId( rs.getString("billing_type_refId"));
					dto.setVehicleTypeId(rs.getString("vehicle_type"));
					dto.setDcYes(rs.getString("distance_constraint"));
					dto.setAcYes(rs.getString("ac_constraint")); 
					dto.setEscortRateType(rs.getString("escort_billing_type"));
					dto.setEscortRate(rs.getString("escort_billing_rate"));
					dto.setFlatTripRate(rs.getString("trip_rate"));
					 
					VehicleTypeDto vdto = new VehicleTypeDto();
					vdto .setId(rs.getInt("vehicle_type"));
					vdto.setType(rs.getString("vehicleTypeName"));
					dto.setVehicleTypeDto(vdto);
					
					
					if(dto.getAcYes().equals( BillingTypeConfigConstants.AC_YES )) {
						dto.setAcList(getTripBasedBillingConfigAcConstraints(dto.getId()));
					}
					if(dto.getDcYes().equals( BillingTypeConfigConstants.DC_YES )) {
						dto.setDcList(getTripBasedBillingConfigDistanceConstraints(dto.getId()));
					}
					 			
				 	
			}
			
		} catch(Exception e) {
			 System.out.println("Error in addTripBasedBillingConfig : " + e);
			  
		}
		
		finally {
		 
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(stmt);
			DbConnect.closeConnection(con);
		}
		return dto;
	}
	
	

	public ArrayList<AcConstraintDto> getTripBasedBillingConfigAcConstraints(String  refId) {
		// TODO Auto-generated method stub
		Connection con=null;
		 
		PreparedStatement stmt=null;
		 
		 
		 
		ResultSet rs= null; 
		 
		ArrayList<AcConstraintDto> dtoList = new ArrayList<AcConstraintDto>(); 
		try {
			 
			String query= "select id, bc_refId, fromTime, toTime, rate from billing_config_ac_constraint where status='" + BillingTypeConfigConstants.ACTIVE + "' and  bc_refId=?";
		 
			 
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
			 System.out.println("Error in addTripBasedBillingConfig : " + e);
			  
		}
		
		finally {
		 
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(stmt);
			DbConnect.closeConnection(con);
		}
		return dtoList;
	}
	

	public ArrayList<DistanceConstraintDto> getTripBasedBillingConfigDistanceConstraints(String  refId) {
		// TODO Auto-generated method stub
		Connection con=null;
		 
		PreparedStatement stmt=null;
		 
		 
		 
		ResultSet rs= null; 
		 
		ArrayList<DistanceConstraintDto> dtoList = new ArrayList<DistanceConstraintDto>(); 
		try {
			 
			String query= "select id, bc_refId, fromKm, toKm, rate, dcAcRate from billing_config_distance_constraint where status='" + BillingTypeConfigConstants.ACTIVE + "' and  bc_refId=?";
 			DbConnect db = DbConnect.getInstance();
			
			con = db.connectDB();
			 
			
			stmt= con.prepareStatement(query);
			 
			stmt.setString(1, refId);
		 	
			rs = stmt.executeQuery();
			while(rs.next()) {
				 
					 
					DistanceConstraintDto dto = new DistanceConstraintDto();
					dto.setParentId(refId);
					dto.setFromKm(rs.getString("fromKm"));
					dto.setToKm(rs.getString("toKm"));
					dto.setRate(rs.getString("rate"));
					dto.setDcAcRate(rs.getString("dcAcRate"));
					dto.setId(rs.getString("id"));
					dtoList.add(dto);
					 		 			
			}
			
		} catch(Exception e) {
			 System.out.println("Error in addTripBasedBillingConfig : " + e);
			  
		}
		
		finally {
		 
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(stmt);
			DbConnect.closeConnection(con);
		}
		return dtoList;
	}



	public int updateTripBasedConfig(TripBasedBillingConfigDto dto) {
		  
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
			System.out.println("update billing_config_ac_constraint set status='c' where status='a' and  bc_refId=" + dto.getId());
			System.out.println("update billing_config_distance_constraint set status='c' where status='a' and  bc_refId="+  dto.getId());
			System.out.println("update trip_based_config set vehicle_type="+ dto.getVehicleTypeId()+", distance_constraint='"+dto.getDcYes()+"', ac_constraint='"+dto.getAcYes()+"', escort_billing_type="+dto.getEscortRateType()+", escort_billing_rate="+ dto.getEscortRate() +", trip_rate=" + dto.getFlatTripRate() + " where status='a' and id=" + dto.getId());
			String deleteAcQuery = "update billing_config_ac_constraint set status='c' where status='a' and  bc_refId=? ";
			String deleteDcQuery = "update billing_config_distance_constraint set status='c' where status='a' and  bc_refId=? ";
			String aQuery = "update trip_based_config set vehicle_type=?, distance_constraint=?, ac_constraint=?, escort_billing_type=?, escort_billing_rate=?, trip_rate=? where status='a' and id=?";
			String bQuery = "insert into billing_config_ac_constraint (bc_refId, fromTime, toTime, rate) values (?, ?, ?, ?)";
			//String b2Query = "insert into billing_config_ac_constraint (bc_refId, fromTime, rate) values (?, ?, ?)";
			String cQuery1 = "insert into billing_config_distance_constraint ( bc_refId, fromKm, toKm, rate, dcAcRate ) values (?, ?, ?, ?, ?)";
			String cQuery2 = "insert into billing_config_distance_constraint ( bc_refId, fromKm, rate, dcAcRate ) values (?, ?, ?, ?)";
			DbConnect db = DbConnect.getInstance();
			
			con = db.connectDB();
			con.setAutoCommit(false);
			deleteAcPst = con.prepareStatement(deleteAcQuery);
			deleteAcPst.setString(1, dto.getId());
			deleteAcPst.executeUpdate();
			System.out.println(" ........  1");
			deleteDcPst = con.prepareStatement(deleteDcQuery);
			deleteDcPst.setString(1, dto.getId());
			deleteDcPst.executeUpdate();
			System.out.println(" ........  2");
			
			pst1 = con.prepareStatement(aQuery, Statement.RETURN_GENERATED_KEYS);
			
			int step=0;
			
			pst1.setString(++step, dto.getVehicleTypeId());
			pst1.setString(++step, dto.getDcYes());
			pst1.setString(++step, dto.getAcYes());
			pst1.setString(++step, dto.getEscortRateType());
			pst1.setString(++step, dto.getEscortRate());
			pst1.setString(++step, dto.getFlatTripRate() );
			pst1.setString(++step, dto.getId());
			System.out.println(" ........  3");
			avalue = pst1.executeUpdate( );
			System.out.println(" ........  4");
			if(avalue > 0) {
				rrs = pst1.getGeneratedKeys();
				 
					   
					
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
					if(dto.getDcYes().equals( BillingTypeConfigConstants.DC_YES )) {
						 
						dcpst1 = con.prepareStatement(cQuery1);
						dcpst2 = con.prepareStatement( cQuery2);
						for(DistanceConstraintDto dcDto : dto.getDcList()) {
							int bvalue=0;
							if(OtherFunctions.isEmpty(dcDto.getToKm()) ) {
						 
								dcpst2.setLong(1, Long.parseLong(  dto.getId()));
								dcpst2.setString(2, dcDto.getFromKm() );
								dcpst2.setString(3, dcDto.getRate());
								dcpst2.setString(4, dcDto.getDcAcRate());
								bvalue = dcpst2.executeUpdate();
								System.out.println(" ........  6");
								 
							} else {
								 
								dcpst1.setLong(1, Long.parseLong( dto.getId()));
								dcpst1.setString(2, dcDto.getFromKm());
								dcpst1.setString(3, dcDto.getToKm());
								dcpst1.setString(4, dcDto.getRate());
								dcpst1.setString(5, dcDto.getDcAcRate());
								bvalue = dcpst1.executeUpdate();
								System.out.println(" ........  7");
							}
							if(bvalue<=0) {
								
								con.rollback();
								break;
							}
						}
					}
					
					new AuditLogDAO().auditLogEntry(Long.parseLong(  dto.getId()), AuditLogConstants.TRIP_BASED_BILLING_MODULE, Integer.parseInt(dto.getDoneBy()), AuditLogConstants.WORKFLOW_STATE_EMPTY , AuditLogConstants.TRIP_BASED_BILLING_CONFIGURATION_CREATED, AuditLogConstants.AUDIT_LOG_CREATE_ACTION	);
					con.commit();
				
					System.out.println(" ........  8");
				 
			}
			
		} catch(Exception e) {
			 System.out.println("Error in addTripBasedBillingConfig : " + e);
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
	


	public int deleteTripBasedConfig(TripBasedBillingConfigDto dto) {
		  
		Connection con=null;
		PreparedStatement deleteAcPst =  null;
		PreparedStatement deleteDcPst =  null;
		PreparedStatement pst1=null;
		PreparedStatement acpst=null;
		PreparedStatement dcpst1=null;
		PreparedStatement dcpst2=null;
		ResultSet rrs= null;
		int avalue = 0;
		int retVal=0;
		try {
			String deleteAcQuery = "update billing_config_ac_constraint set status='c' where status='a' and  bc_refId=? ";
			String deleteDcQuery = "update billing_config_distance_constraint set status='c' where status='a' and  bc_refId=? ";
			String aQuery = "update trip_based_config set status='c' where status='a' and id=" + dto.getId();
			 
			//String b2Query = "insert into billing_config_ac_constraint (bc_refId, fromTime, rate) values (?, ?, ?)";
		 
		 
			DbConnect db = DbConnect.getInstance();
			
			con = db.connectDB();
			con.setAutoCommit(false);
			deleteAcPst = con.prepareStatement(deleteAcQuery);
			deleteAcPst.setString(1, dto.getId());
			System.out.println("001");
			deleteAcPst.executeUpdate();
			deleteDcPst = con.prepareStatement(deleteDcQuery);
			deleteDcPst.setString(1, dto.getId());
			deleteDcPst.executeUpdate();
			System.out.println("002");
			pst1 = con.prepareStatement(aQuery );
			//int step=0;
			
			 System.out.println("Delte query : update trip_based_config set status='c' where status='a' and id="+ dto.getId());
			//pst1.setString(++step, dto.getId());
			 pst1.executeUpdate( );
			 System.out.println(avalue);
			System.out.println("003");
	 				long id =  Long.parseLong( dto.getId());
					System.out.println("004");
 					new AuditLogDAO().auditLogEntry( id, AuditLogConstants.TRIP_BASED_BILLING_MODULE, Integer.parseInt(dto.getDoneBy()), AuditLogConstants.WORKFLOW_STATE_EMPTY , AuditLogConstants.TRIP_BASED_BILLING_CONFIGURATION_DELETED, AuditLogConstants.AUDIT_LOG_CREATE_ACTION, con	);
  					retVal=1;
					System.out.println("005");
			 
			con.commit();
			System.out.println("006");
		} catch(Exception e) {
			 System.out.println("Error in deleteTripBasedBillingConfig : " + e);
			 try {
				  
				 retVal=0;
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
		return retVal;

	}
	


}
