package com.agiledge.atom.billingtype.config.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.agiledge.atom.billingtype.config.dto.AcConstraintDto;
import com.agiledge.atom.billingtype.config.dto.BillingTypeConfigConstants;
import com.agiledge.atom.billingtype.config.dto.KmBasedBillingConfigConstants;
import com.agiledge.atom.billingtype.config.dto.KmBasedTemplateTripBillingConfigDto;
import com.agiledge.atom.billingtype.config.dto.KmTemplateDto;
import com.agiledge.atom.billingtype.config.dto.KmTemplateDto.KmTemplateChildDto;
import com.agiledge.atom.constants.AuditLogConstants;
import com.agiledge.atom.dao.AuditLogDAO;
import com.agiledge.atom.dbConnect.DbConnect;
import com.agiledge.atom.dto.APLDto;
import com.agiledge.atom.dto.VehicleTypeDto;

public class KmBasedTemplateBillingTypeConfigDao {
	  

	public int addKmBasedTemplateBillingConfig(KmBasedTemplateTripBillingConfigDto dto) {
		
		// TODO Auto-generated method stub
		Connection con=null;
		PreparedStatement pst1=null;
		PreparedStatement acpst=null;
		PreparedStatement dcpst1=null;
		PreparedStatement dcpst2=null;
		ResultSet rrs= null;
		int avalue = 0;
		try {
			String aQuery = "INSERT INTO km_based_template_trip_config (km_billing_id, vehicle_type, templateId, swingRateType, swingRate, escort_billing_type, escort_billing_rate, ac_constraint, trip_rate, status ) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			String bQuery = "insert into template_billing_config_ac_constraint (bc_refId, fromTime, toTime, rate) values (?, ?, ?, ?)";
		 
			DbConnect db = DbConnect.getInstance();
			
			con = db.connectDB();
			con.setAutoCommit(false);
			
			pst1 = con.prepareStatement(aQuery, Statement.RETURN_GENERATED_KEYS);
			int step=0; 
			 
			 System.out.println(">>> " + dto.getBillingRefId());

			pst1.setString(++step, dto.getKmBillingType() );
			pst1.setString(++step, dto.getVehicleTypeId());
			pst1.setInt(++step, dto.getTemplateId());
			pst1.setString(++step, dto.getSwingRateType() );
			pst1.setString(++step, dto.getSwingRate());
			pst1.setString(++step, dto.getEscortRateType());
			pst1.setDouble(++step, dto.getEscortRate());
			pst1.setString(++step, dto.getAcYes());
			pst1.setString(++step, dto.getTripRate());
			pst1.setString(++step, BillingTypeConfigConstants.ACTIVE);
			 
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
					 
					
					new AuditLogDAO().auditLogEntry( id, AuditLogConstants.TRIP_BASED_BILLING_MODULE, Integer.parseInt(dto.getDoneBy()), AuditLogConstants.WORKFLOW_STATE_EMPTY , AuditLogConstants.TRIP_BASED_BILLING_CONFIGURATION_CREATED, AuditLogConstants.AUDIT_LOG_CREATE_ACTION	);
					con.commit();
				
				
				}
			}
			
		} catch(Exception e) {
			 System.out.println("Error in KmBasedTemplateBillingConfig : " + e);
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

	public int insertKmBasedTemplate(KmTemplateDto template) {
		// TODO Auto-generated method stub
		Connection con=null;

		PreparedStatement pst1=null;
		PreparedStatement pst2=null;
		PreparedStatement pstU=null;
		PreparedStatement pstUC=null; 
		 
		ResultSet rrs= null;
		int returnInt=0;
		int avalue  = 0;
		try {
 			String updateTemplateQuery = "update km_based_template set status='c' where km_billing_id=?";
 			String updateTemplateChildQuery = "update km_based_template_child set status='c' where template_id in ( select id from km_based_template where km_billing_id=? )";
			String insertTemplateQuery = "insert into km_based_template ( site, km_billing_id, name ) values (?, ?, ? )";
			String insertTemplateChildQuery = " insert into km_based_template_child (landmark, distance,  template_id )  select l.id, ?, ? from landmark l, area a, place p where l.place=p.id and p.area=a.id and a.area=? and p.place=? and l.landmark=? ";
			//String cQuery1 = "insert into km_based_map_trip_config ( km_billing_id, vehicle_type, distance_type, calculation_type, rate) values (?, ?, ?, ?, ?)";
			 
			DbConnect db = DbConnect.getInstance();
			
			con = db.connectDB();
			 con.setAutoCommit(false);
			pstU = con.prepareStatement(updateTemplateQuery);
			pstUC = con.prepareStatement(updateTemplateChildQuery);
			pst2 = con.prepareStatement(insertTemplateChildQuery);
			pst1 = con.prepareStatement(insertTemplateQuery , Statement.RETURN_GENERATED_KEYS);
			pstU.setString(1, template.getBillingRefId());
			pstU.executeUpdate();
			pstUC.setString(1, template.getBillingRefId());
			pstUC.executeUpdate();
			
			int step=0;
			pst1.setString(++step, template.getSite());
			pst1.setString(++step, template.getBillingRefId());
			pst1.setString(++step, template.getTemplateName());
			 
			  avalue = pst1.executeUpdate( );
			if(avalue > 0) {
				rrs = pst1.getGeneratedKeys();
				if(rrs.next()) {
					
					long id = rrs.getLong(1);
					for (KmTemplateChildDto cdto : template.getChildList()) {
						step = 0;
						pst2.setFloat(++step, cdto.getDistance()  );
						pst2.setLong(++step, id);
						pst2.setString(++step, cdto.getAplDto().getArea());
						pst2.setString(++step, cdto.getAplDto().getPlace());
						pst2.setString(++step, cdto.getAplDto().getLandMark());
						returnInt +=pst2.executeUpdate();
					}
					
					
//					pst2.set
					new AuditLogDAO().auditLogEntry( id, AuditLogConstants.TRIP_BASED_BILLING_MODULE, Integer.parseInt(template.getDoneBy()), AuditLogConstants.WORKFLOW_STATE_EMPTY , AuditLogConstants.KM_BASED_TEMPLATE_BILLING_CONFIGURATION_CREATED, AuditLogConstants.AUDIT_LOG_CREATE_ACTION	);
					
					con.commit();
				
				} else {
					con.rollback();
					returnInt=0;
				}
			}
			
		} catch(Exception e) {
			try {
				con.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
			;
			}
			returnInt=0;
			 System.out.println("Error in addTripBasedBillingConfig : " + e);
			 avalue=0;
		}
		
		finally {
		 
			DbConnect.closeResultSet(rrs);
			DbConnect.closeStatement(pst1, pst2 );
			DbConnect.closeConnection(con);
		}
		return avalue;
	}

	public KmTemplateDto getKmTemplateDto(String kmBillingId) {
		// TODO Auto-generated method stub
		
		String parentQuery = "select  tpt.id, ifnull(tpt.name,'') name, tpt.site, tpt.status, tpt.km_billing_id from  km_based_template tpt  where tpt.status='a'  and tpt.km_billing_id=?";
		String childQuery = "select t.site, ktc.distance, ktc.status, ktc.template_id, l.id landmarkId, l.landmark lanmarkName, p.id placeId, p.place placeName, a.id areaId, a.area areaName from km_based_template_child ktc, km_based_template t, landmark l, place p, area a where ktc.landmark=l.id and l.place=p.id and p.area=a.id and ktc.template_id=t.id and t.id=?";
		System.out.println( " parentQuery  " + parentQuery + " "+kmBillingId);
		PreparedStatement pst = null;
		PreparedStatement pst1 = null;
				ResultSet rs = null;
				ResultSet rs1 = null;
			Connection con = null;
			KmTemplateDto tDto =null;
		try {
			con = DbConnect.getInstance().connectDB();
			pst = con.prepareStatement(parentQuery);
			pst1 = con.prepareStatement(childQuery);
			pst.setString(1, kmBillingId);
			rs = pst.executeQuery();
			if(rs.next()) {
				tDto = new KmTemplateDto();
				pst1.setString(1, rs.getString("id"));
				System.out.println( "childQuery " + childQuery + " "+rs.getString("id"));
				tDto.setTemplateId(rs.getLong("id"));
				tDto.setSite(rs.getString("site"));
				tDto.setTemplateName(rs.getString("name"));
				
				rs1 = pst1.executeQuery();
				while (rs1.next()) {
					
					KmTemplateChildDto tcDto = tDto.new KmTemplateChildDto();
					//tcDto.setId(rs1.getLong("id"));
					tcDto.setDistance(rs1.getFloat("distance"));
					APLDto apl = new APLDto();
					apl.setLandMarkID(rs1.getString("landmarkId"));
					apl.setLandMark(rs1.getString("lanmarkName"));
					apl.setPlaceID(rs1.getString("placeId"));
					apl.setPlace(rs1.getString("placeName"));
					apl.setAreaID(rs1.getString("areaId"));
					apl.setArea(rs1.getString("areaName"));
					tcDto.setAplDto(apl);
					//System.out.println("..."+ apl.getArea() + " " + apl.getPlace() + "  " + apl.getLandMark());
					tDto.getChildList().add(tcDto);
				}
				
				
			}
		}catch(Exception e) {
			System.out.println("Error in getKmTEmplateDto in KmBasedTemplateBillingDao " +e);
		}
		
		finally {
			DbConnect.closeResultSet(rs1, rs);
			DbConnect.closeStatement(pst1, pst);
			DbConnect.closeConnection(con);
			
		}
		return tDto;
	}

	
	public ArrayList<KmBasedTemplateTripBillingConfigDto> getKmBasedTemplateBillingConfig(
			String refId) {
		Connection con=null;
		PreparedStatement stmt=null;
		String billType= KmBasedBillingConfigConstants.TEMPLATE;
		 
		 
		ResultSet rs= null;
		 
		 
		ArrayList<KmBasedTemplateTripBillingConfigDto> dtoList = new ArrayList<KmBasedTemplateTripBillingConfigDto>(); 
		try {
			String query = "select  kt.billing_type_id, kmc.id, kmc.km_billing_id, kmc.vehicle_type, kmc.templateId, kmc.swingRateType, kmc.swingRate, vt.type vehicleTypeName, ifnull( escort_billing_type,'') escort_billing_type,ifnull( escort_billing_rate,'') escort_billing_rate, ifnull( ac_constraint,'') ac_constraint, ifnull( trip_rate,'') trip_rate   from km_based_template_trip_config kmc join km_based_trip_config kt on kmc.km_billing_id=kt.id join vehicle_type vt on kmc.vehicle_type=vt.id where kmc.status='a' and kt.status='a' and kt.billing_type_id=?  and kt.km_billing_type=?";
			  
			  System.out.println("query " + query + "" + billType + " , " + refId);
			DbConnect db = DbConnect.getInstance();
			
			con = db.connectDB();
			 
			
			stmt = con.prepareStatement(query );
			
			stmt.setString(1, refId);
			stmt.setString(2, billType);
		 	
			rs = stmt.executeQuery();
			while(rs.next()) {
				 
System.out.println("......... ");					 
					KmBasedTemplateTripBillingConfigDto dto = new KmBasedTemplateTripBillingConfigDto();
					dto.setBillingRefId(refId);
					dto.setId(rs.getString("id"));
					dto.setKmBillingRefId(rs.getString("km_billing_id"));
		 
					 dto.setVehicleTypeId(rs.getString("vehicle_type"));
					 dto.setTemplateId(rs.getInt("templateId"));
					 dto.setSwingRateType(rs.getString("swingRateType"));
					 dto.setSwingRate(rs.getString("swingRate"));
 
			 		 
					VehicleTypeDto vdto = new VehicleTypeDto();
					vdto .setId(rs.getInt("vehicle_type"));
					vdto.setType(rs.getString("vehicleTypeName"));
					dto.setVehicleTypeDto(vdto);
					dto.setEscortRateType(rs.getString("escort_billing_type"));
					dto.setEscortRate(rs.getDouble("escort_billing_rate"));
					
					dto.setTripRate(rs.getString("trip_rate"));
					dto.setAcYes(rs.getString("ac_constraint"));
					dto.setEscortRate(rs.getDouble("escort_billing_rate"));
					dto.setEscortRateType(rs.getString("escort_billing_type"));
					if(dto.getAcYes().equalsIgnoreCase(KmBasedBillingConfigConstants.AC_YES)) {
						System.out.println("......ac yes .......");
						dto.setAcList(getTemplateBasedBillingConfigAcConstraints(dto.getId()));
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
	
	public  KmBasedTemplateTripBillingConfigDto  getKmBasedTemplateBillingConfig(
			String refId, String vehicleTypeId) {
		Connection con=null;
		PreparedStatement stmt=null;
		String billType= KmBasedBillingConfigConstants.TEMPLATE;
		KmBasedTemplateTripBillingConfigDto dto = null;
		 
		ResultSet rs= null;
		 
		 
		ArrayList<KmBasedTemplateTripBillingConfigDto> dtoList = new ArrayList<KmBasedTemplateTripBillingConfigDto>(); 
		try {
			String query = "select  kt.billing_type_id, kmc.id, kmc.km_billing_id, kmc.vehicle_type, kmc.templateId, kmc.swingRateType, kmc.swingRate, vt.type vehicleTypeName, ifnull( escort_billing_type,'') escort_billing_type,ifnull( escort_billing_rate,'') escort_billing_rate, ifnull( ac_constraint,'') ac_constraint, ifnull( trip_rate,'') trip_rate   from km_based_template_trip_config kmc join km_based_trip_config kt on kmc.km_billing_id=kt.id join vehicle_type vt on kmc.vehicle_type=vt.id where kmc.status='a' and kt.status='a' and kt.billing_type_id=?  and kt.km_billing_type=? and kmc.vehicle_type=?";
			 
			  System.out.println("query template select  kt.billing_type_id, kmc.id, kmc.km_billing_id, kmc.vehicle_type, kmc.templateId, kmc.swingRateType, kmc.swingRate, vt.type vehicleTypeName, ifnull( escort_billing_type,'') escort_billing_type,ifnull( escort_billing_rate,'') escort_billing_rate, ifnull( ac_constraint,'') ac_constraint, ifnull( trip_rate,'') trip_rate   from km_based_template_trip_config kmc join km_based_trip_config kt on kmc.km_billing_id=kt.id join vehicle_type vt on kmc.vehicle_type=vt.id where kmc.status='a' and kt.status='a' and kt.billing_type_id="+refId+"  and kt.km_billing_type='"+billType+"' and kmc.vehicle_type="+vehicleTypeId+" ");
			DbConnect db = DbConnect.getInstance();
			
			con = db.connectDB();
			 
			
			stmt = con.prepareStatement(query );
			
			stmt.setString(1, refId);
			stmt.setString(2, billType);
			stmt.setString(3, vehicleTypeId);
		 	
			rs = stmt.executeQuery();
			if(rs.next()) {
				 
System.out.println("......... ");					 
				 dto = new KmBasedTemplateTripBillingConfigDto();
					dto.setBillingRefId(refId);
					dto.setId(rs.getString("id"));
					dto.setKmBillingRefId(rs.getString("km_billing_id"));
		 
					 dto.setVehicleTypeId(rs.getString("vehicle_type"));
					 dto.setTemplateId(rs.getInt("templateId"));
					 dto.setSwingRateType(rs.getString("swingRateType"));
					 dto.setSwingRate(rs.getString("swingRate"));
 
			 		 
					VehicleTypeDto vdto = new VehicleTypeDto();
					vdto .setId(rs.getInt("vehicle_type"));
					vdto.setType(rs.getString("vehicleTypeName"));
					dto.setVehicleTypeDto(vdto);
					dto.setEscortRateType(rs.getString("escort_billing_type"));
					dto.setEscortRate(rs.getDouble("escort_billing_rate"));
					
					dto.setTripRate(rs.getString("trip_rate"));
					dto.setAcYes(rs.getString("ac_constraint"));
					dto.setEscortRate(rs.getDouble("escort_billing_rate"));
					dto.setEscortRateType(rs.getString("escort_billing_type"));
					if(dto.getAcYes().equalsIgnoreCase(KmBasedBillingConfigConstants.AC_YES)) {
						System.out.println("......ac yes .......");
						dto.setAcList(getTemplateBasedBillingConfigAcConstraints(dto.getId()));
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

	
	public ArrayList<AcConstraintDto> getTemplateBasedBillingConfigAcConstraints(String  refId) {
		// TODO Auto-generated method stub
		Connection con=null;
		 
		PreparedStatement stmt=null;
		 
		 
		 
		ResultSet rs= null; 
		 
		ArrayList<AcConstraintDto> dtoList = new ArrayList<AcConstraintDto>(); 
		try {
			 
			String query= "select id, bc_refId, fromTime, toTime, rate from template_billing_config_ac_constraint where status='" + BillingTypeConfigConstants.ACTIVE + "' and  bc_refId=?";
		 
			 
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
	


	public boolean checkDuplication(KmBasedTemplateTripBillingConfigDto dto) {
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

	public int deleteKmBasedTemplateBillingConfig(KmBasedTemplateTripBillingConfigDto dto) {
		//  
		
		// TODO Auto-generated method stub
				Connection con=null;
				PreparedStatement pst1=null;
				 
				ResultSet rrs= null;
				int avalue = 0;
				try {
					 
					 
					 String aQuery = "update km_based_template_trip_config set status='"+BillingTypeConfigConstants.INACTIVE+"' where  id=?";
					 
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
							new AuditLogDAO().auditLogEntry( id, AuditLogConstants.TRIP_BASED_BILLING_MODULE, Integer.parseInt(dto.getDoneBy()), AuditLogConstants.WORKFLOW_STATE_EMPTY , AuditLogConstants.KM_BASED_MAP_BILLING_CONFIGURATION_DELETED, AuditLogConstants.AUDIT_LOG_DELETE_ACTION	);
					 
						}
					}
					
				} catch(Exception e) {
					 System.out.println("Error in deleteKmBasedTemplateBillingConfig : " + e);
					 avalue=0;
				}
				
				finally {
				 
					DbConnect.closeResultSet(rrs);
					DbConnect.closeStatement(pst1 );
					DbConnect.closeConnection(con);
				}
				return avalue;
	}

	public int updateKmBasedTemplateConfig(
			KmBasedTemplateTripBillingConfigDto dto) {
		// TODO Auto-generated method stub
		// update km_based_template_trip_config set swingRateType=?, swingRate=?, escort_billing_type=?, escort_billing_rate=?, ac_constraint=? where id=?
		
		// TODO Auto-generated method stub
		Connection con=null;
		PreparedStatement pst1=null;
		PreparedStatement deleteAcPst=null;
		PreparedStatement acpst=null;
		ResultSet rrs= null;
		int avalue = 0;
		try {
			 
			String deleteAcQuery = "update template_billing_config_ac_constraint set status='"+BillingTypeConfigConstants.INACTIVE+"' where status='"+BillingTypeConfigConstants.ACTIVE+"' and  bc_refId=? ";
			 String aQuery = "update km_based_template_trip_config set swingRateType=?, swingRate=?, escort_billing_type=?, escort_billing_rate=?, ac_constraint=?, trip_rate=? where status='"+BillingTypeConfigConstants.ACTIVE+"' and  id=?";
			 String bQuery = "insert into template_billing_config_ac_constraint (bc_refId, fromTime, toTime, rate) values (?, ?, ?, ?)";
			DbConnect db = DbConnect.getInstance();
			
			con = db.connectDB();
			con.setAutoCommit(false);
			deleteAcPst = con.prepareStatement(deleteAcQuery);
			deleteAcPst.setString(1, dto.getId());
			deleteAcPst.executeUpdate();
			
			pst1 = con.prepareStatement(aQuery, Statement.RETURN_GENERATED_KEYS);
			int step=0;
			 
			pst1.setString(++step, dto.getSwingRateType() );
			pst1.setString(++step, dto.getSwingRate() );
			pst1.setString(++step, dto.getEscortRateType());
		
			pst1.setDouble(++step, dto.getEscortRate());
			  
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
					new AuditLogDAO().auditLogEntry( id, AuditLogConstants.KM_BASED_BILLING_MODULE, Integer.parseInt(dto.getDoneBy()), AuditLogConstants.WORKFLOW_STATE_EMPTY , AuditLogConstants.KM_BASED_MAP_BILLING_CONFIGURATION_CREATED, AuditLogConstants.AUDIT_LOG_EDIT_ACTION	);
					con.commit();
				}
			}
			
		} catch(Exception e) {
			 System.out.println("Error in updateKmBasedTemplateBillingConfig : " + e);
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
