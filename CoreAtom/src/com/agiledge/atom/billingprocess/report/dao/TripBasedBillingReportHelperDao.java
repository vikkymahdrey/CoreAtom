package com.agiledge.atom.billingprocess.report.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import com.agiledge.atom.billingprocess.report.dto.TripBasedBillingReportHelperDto;
import com.agiledge.atom.billingtype.config.dto.BillingTypeConfigConstants;
import com.agiledge.atom.commons.OtherFunctions;
import com.agiledge.atom.constants.SettingsConstant;
import com.agiledge.atom.dbConnect.DbConnect;
import com.agiledge.atom.dto.TripDetailsDto;
import com.agiledge.atom.reports.dto.TripUtilisationReportDto;

public class TripBasedBillingReportHelperDao {

	
	public ArrayList<TripBasedBillingReportHelperDto> getTripBasedBillingTripCostReport_monthly(String site, String vendorId, String vehicleType, String fromDate, String toDate, String month , String year, String escort, String constraint  ) {
		

	 	DbConnect dbConn = DbConnect.getInstance();
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		fromDate=OtherFunctions.changeDateFromatToIso(fromDate);
		toDate = OtherFunctions.changeDateFromatToIso(toDate);
		vehicleType=vehicleType==null?"":vehicleType;
		vendorId=vendorId==null?"":vendorId;					
		fromDate=fromDate==null?"":fromDate;
		toDate=toDate==null?"":toDate;
	//	option=option==null?"":option;
		site=site==null?"":site;
		ArrayList<TripBasedBillingReportHelperDto> dtoList = new ArrayList<TripBasedBillingReportHelperDto>();
		
			
		try {
			if(!site.equals("")&&!vendorId.equals("")) {
			 
			String vehicleQuery="";
			if(vehicleType.equals("")==false&&vehicleType.trim().equalsIgnoreCase("all")==false) {
				vehicleQuery= "  and td.vehicle_type="+ vehicleType;
			}
			String dateQuery = "";
			if( !fromDate.equals("") ) {
				dateQuery = " and td.trip_date >= '" + OtherFunctions.changeDateFromatToIso(fromDate)+"'";
			}
			if( !toDate.equals("") ) {
				dateQuery = dateQuery +  " and td.trip_date <= '" +
						 OtherFunctions.changeDateFromatToIso(toDate)+"'";
			}
		
			String yearQuery = "";
			if(!OtherFunctions.isEmpty( year)) {
				yearQuery = " and year(td.trip_date)=" + year;
			}
			String monthQuery = "";
			if(!OtherFunctions.isEmpty( month)) {
				monthQuery = " and monthname(td.trip_date)='" + month + "' ";
			}
			
			String constraintQuery = " and ( tbc.ac_constraint='"+BillingTypeConfigConstants.AC_YES+"' OR tbc.distance_constraint ='"+BillingTypeConfigConstants.DC_YES+"' )";
			if(constraint.equals(BillingTypeConfigConstants.AC_LABEL)) {
				constraintQuery = " and  tbc.ac_constraint='"+BillingTypeConfigConstants.AC_YES+"' ";
			} else if(constraint.equals(BillingTypeConfigConstants.DC_LABEL)) {
				constraintQuery = " and  tbc.distance_constraint ='"+BillingTypeConfigConstants.DC_YES+"' ";
			} else if (constraint.equals(BillingTypeConfigConstants.NC_LABEL)) {
				constraintQuery =  " and ( tbc.ac_constraint='"+BillingTypeConfigConstants.AC_NO+"' AND tbc.distance_constraint ='"+BillingTypeConfigConstants.DC_NO+"' )";
			}
			
			String escortQuery = "";
			if(escort.equals(BillingTypeConfigConstants.WITH_ESCORT)) {
				escortQuery = " and   tEscort='"+BillingTypeConfigConstants.ESCORT_YES+"'";
			} else if(escort.equals(BillingTypeConfigConstants.WITHOUT_ESCORT)) {
				escortQuery = " and   tEscort='"+BillingTypeConfigConstants.ESCORT_NO+"'";
			} 
		 

		 
	//String billingApprovalStatus= "Sent For TM Approval";
		 
		 
		/*	String query1 = "select   year(td.trip_date) tripYear, monthname(td.trip_date) tripMonth, td.vehicleTypeName, dc.fromKm, dc.toKm, count(*) totTrips, sum(targ.tripTripRate) tripTripRate, concat(dc.fromKm,'-',dc.toKm ) distanceRange, sum(targ.tripEscortRate) tripEscortRate from  td_billing_args targ join   " +
					"(select vtp.escort tEscort, ifnull(vtp.approvalStatus,'Open') approvalStatus, tva.vendorId, td.*, vt.type vehicleTypeName from trip_details td join vehicle_type vt on td.vehicle_type=vt.id join tripvendorassign tva on td.id= tva.tripid  left join vendor_trip_sheet_parent vtp on td.id = vtp.tripId where approvalStatus = '" + billingApprovalStatus + "' )   td " +
					"on targ.tripId = td.id join  	(select tbc.id, tbc.ac_constraint, tbc.distance_constraint, tbc.trip_rate from trip_based_config tbc  where 1=1 "+constraintQuery+"  ) tbc on tbc.id= targ.tripTripBillingId  left join  (  select tbc.status, tbc.id, tbc.ac_constraint,  tbcd.fromKm, tbcd.toKm, ifnull( tbcd.rate, tbc.trip_rate) tripRate from trip_based_config tbc left join billing_config_distance_constraint tbcd on tbcd.bc_refId=tbc.id where tbc.status= 'a' ) dc on targ.tripTripBillingId=dc.id and td.distance between dc.fromKm and dc.toKm    "+ 
							" where td.siteId="+site+" and td.vendorId="+ vendorId + vehicleQuery + dateQuery+ yearQuery + monthQuery + escortQuery +
							" group by year(td.trip_date), monthname(td.trip_date) , td.vehicleTypeName, dc.fromKm, dc.toKm , concat(dc.fromKm,'-',dc.toKm )    ";*/
			String query1 = "select   year(td.trip_date) tripYear, monthname(td.trip_date) tripMonth, td.vehicleTypeName, dc.fromKm, dc.toKm, count(*) totTrips, sum(targ.tripTripRate) tripTripRate, concat(dc.fromKm,'-',dc.toKm ) distanceRange, sum(targ.tripEscortRate) tripEscortRate from  td_billing_args targ join   " +
					"(select vtp.escort tEscort, ifnull(vtp.approvalStatus,'Open') approvalStatus, tva.vendorId, td.*, vt.type vehicleTypeName from trip_details td join vehicle_type vt on td.vehicle_type=vt.id join vehicles v on v.id=td.vehicle join tripvendorassign tva on td.id= tva.tripid  left join vendor_trip_sheet_parent vtp on td.id = vtp.tripId where approvalStatus = '" + SettingsConstant.APPROVAL_TYPE + "' )   td " +
					"on targ.tripId = td.id join  	(select tbc.id, tbc.ac_constraint, tbc.distance_constraint, tbc.trip_rate from trip_based_config tbc  where 1=1 "+constraintQuery+"  ) tbc on tbc.id= targ.tripTripBillingId  left join  (  select tbc.status, tbc.id, tbc.ac_constraint,  tbcd.fromKm, tbcd.toKm, ifnull( tbcd.rate, tbc.trip_rate) tripRate from trip_based_config tbc left join billing_config_distance_constraint tbcd on tbcd.bc_refId=tbc.id where tbc.status= 'a' ) dc on targ.tripTripBillingId=dc.id and td.distance between dc.fromKm and dc.toKm    "+ 
							" where td.siteId="+site+" and td.vendorId="+ vendorId + vehicleQuery + dateQuery+ yearQuery + monthQuery + escortQuery +
							" group by year(td.trip_date), monthname(td.trip_date) , td.vehicleTypeName, dc.fromKm, dc.toKm , concat(dc.fromKm,'-',dc.toKm )    ";
			con = dbConn.connectDB();
			stmt = con.createStatement();
			System.out.println(query1);
		 
			 				rs = stmt.executeQuery(query1);

			while (rs.next()) {
				TripBasedBillingReportHelperDto dto = new TripBasedBillingReportHelperDto();
				dto.setMonthName(rs.getString("tripMonth"));
				dto.setYear(rs.getString("tripYear"));
				dto.setVehicleType(rs.getString("vehicleTypeName"));
				dto.setYear(rs.getString("tripYear"));
				dto.setEscortRate(rs.getDouble("tripEscortRate")); 
				dto.setFromKm(""+rs.getString("fromKm"));
				dto.setToKm(""+rs.getString("toKm"));
				dto.setDistanceRange(rs.getString("distanceRange"));
				dto.setTrips(getTripBasedBillingTripCostReport_tripWise(site, vendorId, dto.getVehicleType(), fromDate, toDate, dto.getMonthName(), dto.getYear(), dto.getFromKm(), dto.getToKm(), escort, constraint));
				 
				dto.setTotalTrips(rs.getInt("totTrips"));
				dto.setRate(rs.getDouble("tripTripRate"));
				
			 	 	 dtoList.add(dto);
			}
			return dtoList;
			}else  {
				System.out.println("validation error : ");
			}

		} catch (Exception e) {
			System.out.println("Error in getbyVehicleTypeReport "+ e);
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(stmt);
			DbConnect.closeConnection(con);
		}

		return null;		

		
	}
	
	public ArrayList<TripBasedBillingReportHelperDto> getTripBasedBillingTripCostReport_daily(String site, String vendorId, String vehicleType, String fromDate, String toDate, String month, String year, String escort, String constraint) {
		

	 	//vendorId="1";
		 
		


		DbConnect dbConn = DbConnect.getInstance();
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		 
		fromDate=OtherFunctions.changeDateFromatToIso(fromDate);
		toDate = OtherFunctions.changeDateFromatToIso(toDate);
		vehicleType=vehicleType==null?"":vehicleType;
		vendorId=vendorId==null?"":vendorId;					
		fromDate=fromDate==null?"":fromDate;
		toDate=toDate==null?"":toDate;
	//	option=option==null?"":option;
		site=site==null?"":site;
		ArrayList<TripBasedBillingReportHelperDto> dtoList = new ArrayList<TripBasedBillingReportHelperDto>();
		
			
		try {
			if(!site.equals("")&&!vendorId.equals("")) {
			 
			String vehicleQuery="";
			if(vehicleType.equals("")==false&&vehicleType.trim().equalsIgnoreCase("all")==false) {
				vehicleQuery= "  and td.vehicle_type="+ vehicleType;
			}
			String dateQuery = "";
			if( !fromDate.equals("") ) {
				dateQuery = " and td.trip_date >= '" + OtherFunctions.changeDateFromatToIso(fromDate)+"'";
			}
			if( !toDate.equals("") ) {
				dateQuery = dateQuery +  " and td.trip_date <= '" +
						 OtherFunctions.changeDateFromatToIso(toDate)+"'";
			}
			String yearQuery = "";
			if(!OtherFunctions.isEmpty( year)) {
				yearQuery = " and year(td.trip_date)=" + year;
			}
			String monthQuery = "";
			if(!OtherFunctions.isEmpty( month)) {
				monthQuery = " and monthname(td.trip_date)='" + month + "' ";
			}
			String constraintQuery = " and ( tbc.ac_constraint='"+BillingTypeConfigConstants.AC_YES+"' OR tbc.distance_constraint ='"+BillingTypeConfigConstants.DC_YES+"' )";
			if(constraint.equals(BillingTypeConfigConstants.AC_LABEL)) {
				constraintQuery = " and  tbc.ac_constraint='"+BillingTypeConfigConstants.AC_YES+"' ";
			} else if(constraint.equals(BillingTypeConfigConstants.DC_LABEL)) {
				constraintQuery = " and  tbc.distance_constraint ='"+BillingTypeConfigConstants.DC_YES+"' ";
			} else if (constraint.equals(BillingTypeConfigConstants.NC_LABEL)) {
				constraintQuery =  " and ( tbc.ac_constraint='"+BillingTypeConfigConstants.AC_NO+"' AND tbc.distance_constraint ='"+BillingTypeConfigConstants.DC_NO+"' )";
			}
			
			String escortQuery = "";
			if(escort.equals(BillingTypeConfigConstants.WITH_ESCORT)) {
				escortQuery = " and   tEscort='"+BillingTypeConfigConstants.ESCORT_YES+"'";
			} else if(escort.equals(BillingTypeConfigConstants.WITHOUT_ESCORT)) {
				escortQuery = " and   tEscort='"+BillingTypeConfigConstants. ESCORT_NO+"'";
			}  
		 
			//String billingApprovalStatus= "Sent For TM Approval";
		
			String query1 = "select  td.trip_date, td.vehicleTypeName, dc.fromKm, dc.toKm, count(*) totTrips, sum(targ.tripTripRate) tripTripRate, sum(targ.tripEscortRate )  tripEscortRate, ifnull(concat(dc.fromKm,'-',dc.toKm ),'') distanceRange   from  td_billing_args targ join  " +
					" (select vtp.escort tEscort, ifnull(vtp.approvalStatus,'Open') approvalStatus, tva.vendorId, td.*, vt.type vehicleTypeName from trip_details td join vehicle_type vt on td.vehicle_type=vt.id join tripvendorassign tva on td.id= tva.tripid  left join vendor_trip_sheet_parent vtp on td.id = vtp.tripId where approvalStatus = '" + SettingsConstant.APPROVAL_TYPE + "' ) " +
					" td on targ.tripId = td.id join  	(select tbc.id, tbc.ac_constraint, tbc.distance_constraint, tbc.trip_rate from trip_based_config tbc  where 1=1 "+constraintQuery+"  ) tbc on tbc.id= targ.tripTripBillingId  left join  (  select tbc.status, tbc.id, tbc.ac_constraint,  tbcd.fromKm, tbcd.toKm, ifnull( tbcd.rate, tbc.trip_rate) tripRate from trip_based_config tbc left join billing_config_distance_constraint tbcd on tbcd.bc_refId=tbc.id where tbc.status= 'a'  ) dc on targ.tripTripBillingId=dc.id and td.distance between dc.fromKm and dc.toKm   "+ 
							" where td.siteId="+site+" and td.vendorId="+ vendorId + vehicleQuery + dateQuery+ yearQuery + monthQuery + escortQuery +
							" group by td.trip_date, td.vehicleTypeName, dc.fromKm, dc.toKm,  concat(dc.fromKm,'-',dc.toKm ) ";
			System.out.println("DAIDLY QURY 1 : " +query1);
			con = dbConn.connectDB();
			stmt = con.createStatement();
		 	 				rs = stmt.executeQuery(query1);

			while (rs.next()) {
				TripBasedBillingReportHelperDto dto = new TripBasedBillingReportHelperDto();
				dto.setDate(rs.getDate("trip_date"));
				dto.setEscortRate(rs.getDouble("tripEscortRate"));
				dto.setVehicleType(rs.getString("vehicleTypeName"));
				 
				dto.setFromKm(""+rs.getString("fromKm"));
				dto.setToKm(""+rs.getString("toKm"));
				 dto.setDistanceRange(rs.getString("distanceRange"));
				dto.setTotalTrips(rs.getInt("totTrips"));
				dto.setRate(rs.getDouble("tripTripRate"));
				dto.setTrips(getTripBasedBillingTripCostReport_tripWise(site, vendorId, dto.getVehicleType(), OtherFunctions.changeDateFromatToIso( dto.getDate()),OtherFunctions.changeDateFromatToIso( dto.getDate()), dto.getMonthName(), dto.getYear(), dto.getFromKm(), dto.getToKm(), escort, constraint));
				
			 	 	 dtoList.add(dto);
			}
			return dtoList;
			}else  {
				System.out.println("validation error : ");
			}

		} catch (Exception e) {
			System.out.println("Error in getbyVehicleTypeReport "+ e);
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(stmt);
			DbConnect.closeConnection(con);
		}

		return null;		

		
	}
	
	
	
public ArrayList<TripDetailsDto> getTripBasedBillingTripCostReport_tripWise(String site, String vendorId, String vehicleType, String fromDate, String toDate, String month, String year, String fromKm, String toKm, String escort, String constraint) {
		

	 	//vendorId="1"; 

		DbConnect dbConn = DbConnect.getInstance();
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		fromDate=OtherFunctions.changeDateFromatToIso(fromDate);
		toDate = OtherFunctions.changeDateFromatToIso(toDate);
		vehicleType=vehicleType==null?"":vehicleType;
		vendorId=vendorId==null?"":vendorId;					
		fromDate=fromDate==null?"":fromDate;
		toDate=toDate==null?"":toDate;
	//	option=option==null?"":option;
		site=site==null?"":site;
		ArrayList<TripDetailsDto> dtoList = new ArrayList<TripDetailsDto>();
		
			
		try {
			if(!site.equals("")&&!vendorId.equals("")) {
			 
			String vehicleQuery="";
			if(vehicleType.equals("")==false&&vehicleType.trim().equalsIgnoreCase("all")==false) {
				vehicleQuery= "  and td.vehicleTypeName='"+ vehicleType + "' ";
			}
			String dateQuery = "";
			if( !fromDate.equals("") ) {
				dateQuery = " and td.trip_date >= '" + OtherFunctions.changeDateFromatToIso(fromDate)+"'";
			}
			if( !toDate.equals("") ) {
				dateQuery = dateQuery +  " and td.trip_date <= '" +
						 OtherFunctions.changeDateFromatToIso(toDate)+"'";
			}
			String yearQuery = "";
			if(!OtherFunctions.isEmpty( year)) {
				yearQuery = " and year(td.trip_date)=" + year;
			}
			String monthQuery = "";
			if(!OtherFunctions.isEmpty( month)) {
				monthQuery = " and monthname(td.trip_date)='" + month + "' ";
			}
			String fromKmQuery = "";
			if(!OtherFunctions.isEmpty( fromKm)) {
				fromKmQuery = " and dc.fromKm='" + fromKm + "' ";
			}
			String toKmQuery = "";
			if(!OtherFunctions.isEmpty( toKm)) {
				fromKmQuery = " and dc.toKm='" + toKm + "' ";
			}
			String constraintQuery = " and ( tbc.ac_constraint='"+BillingTypeConfigConstants.AC_YES+"' OR tbc.distance_constraint ='"+BillingTypeConfigConstants.DC_YES+"' )";
			if(constraint.equals(BillingTypeConfigConstants.AC_LABEL)) {
				constraintQuery = " and  tbc.ac_constraint='"+BillingTypeConfigConstants.AC_YES+"' ";
			} else if(constraint.equals(BillingTypeConfigConstants.DC_LABEL)) {
				constraintQuery = " and  tbc.distance_constraint ='"+BillingTypeConfigConstants.DC_YES+"' ";
			} else if (constraint.equals(BillingTypeConfigConstants.NC_LABEL)) {
				constraintQuery =  " and ( tbc.ac_constraint='"+BillingTypeConfigConstants.AC_NO+"' AND tbc.distance_constraint ='"+BillingTypeConfigConstants.DC_NO+"' )";
			}
			
			String escortQuery = "";
			if(escort.equals(BillingTypeConfigConstants.WITH_ESCORT)) {
				escortQuery = " and   tEscort='"+BillingTypeConfigConstants.ESCORT_YES +"'";
			} else if(escort.equals(BillingTypeConfigConstants.WITHOUT_ESCORT)) {
				escortQuery = " and   tEscort='"+BillingTypeConfigConstants.ESCORT_NO+"'";
			}  
		 
		 
			
			String billingApprovalStatus= "Approved by Transport Manager";
		
			String query1 = " select v.regno,td.trip_code, td.trip_date, td.vehicleTypeName, td.trip_time, td.trip_log, td.security_status, targ.tripId, targ.tripTripBillingId, targ.tripTripRate, tbc.ac_constraint, tbc.distance_constraint, targ.tripEscortRate,  dc.fromKm, dc.toKm, ifnull(dc.tripRate, tbc.trip_rate) tripRate, td.distance,  concat(dc.fromKm,'-', dc.toKm ) distanceRange, targ.tripEscortRate  from  td_billing_args targ join  " +
					"( select vtp.escort tEscort, ifnull(vtp.approvalStatus,'Open') approvalStatus, tva.vendorId, td.*, vt.type vehicleTypeName from trip_details td join vehicle_type vt on td.vehicle_type=vt.id join tripvendorassign tva on td.id= tva.tripid  left join vendor_trip_sheet_parent vtp on td.id = vtp.tripId where approvalStatus = '" + billingApprovalStatus + "'    )    td " +
					" on targ.tripId = td.id join vehicles v on v.id=td.vehicle join  (select tbc.id, tbc.ac_constraint, tbc.distance_constraint, tbc.trip_rate from trip_based_config tbc where 1=1 "+constraintQuery+" ) tbc on tbc.id= targ.tripTripBillingId left join ( select tbc.status, tbc.id, tbc.ac_constraint,  tbcd.fromKm, tbcd.toKm, ifnull( tbcd.rate, tbc.trip_rate) tripRate from trip_based_config tbc left join billing_config_distance_constraint tbcd on tbcd.bc_refId=tbc.id where tbc.status= 'a'  ) dc on targ.tripTripBillingId=dc.id and td.distance between dc.fromKm and dc.toKm "+ 
							" where td.siteId="+site+" and td.vendorId="+ vendorId + vehicleQuery + dateQuery+ yearQuery + monthQuery + fromKmQuery + toKmQuery + escortQuery;
						 
			System.out.println("DAIDLY QURY : " +query1);
			System.out.println(toKm);
			con = dbConn.connectDB();
			stmt = con.createStatement();
			
			/*if(option.equals("Time slot"))
			{
				query=timeSlotQuery;
				optionTitle="Time Slot";
			} else if(option.equals("Time of the day"))
			{
				query=timeOfTheDayQuery;
				optionTitle="Time Of The Day";
			} else if(option.equals("Distance grid"))
			{
				query=distanceGridQuery;
				optionTitle="Distance Grid";
			} else {
				query=optionlessQuery;
			}*/
		 
			 				rs = stmt.executeQuery(query1);

			while (rs.next()) {
				TripDetailsDto dto = new TripDetailsDto();
				dto.setTrip_code(rs.getString("trip_code"));
				dto.setTripDate(rs.getDate("trip_date"));
				dto.setTrip_time(rs.getString("trip_time"));
				dto.setTrip_log(rs.getString("trip_log"));
				dto.setEscortRate(rs.getDouble("tripEscortRate"));
				dto.setVehicle_type(rs.getString("vehicleTypeName"));
				dto.setVehicleNo(rs.getString("regno")); 
				dto.setFromKm(""+rs.getString("fromKm"));
				dto.setToKm(""+rs.getString("toKm"));
				 dto.setDistanceRange(rs.getString("distanceRange")); 
				dto.setTripRate (rs.getDouble("tripTripRate"));
				
			 	 	 dtoList.add(dto);
			}
			return dtoList;
			}else  {
				System.out.println("validation error : ");
			}

		} catch (Exception e) {
			System.out.println("Error in getbyVehicleTypeReport "+ e);
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(stmt);
			DbConnect.closeConnection(con);
		}

		return null;		

		
	}

public ArrayList<TripDetailsDto> getTripBasedBillingTripCostReportmonthly(String site, String vendorId, String vehicleType, String fromDate, String toDate, String escort, String constraint,String vehicle) {
	DbConnect dbConn = DbConnect.getInstance();
	Connection con = null;
	Statement stmt = null;
	ResultSet rs = null;	
	fromDate=OtherFunctions.changeDateFromatToIso(fromDate);
	toDate = OtherFunctions.changeDateFromatToIso(toDate);
	vehicleType=vehicleType==null?"":vehicleType;
	vendorId=vendorId==null?"":vendorId;					
	fromDate=fromDate==null?"":fromDate;
	toDate=toDate==null?"":toDate;
	vehicle=vehicle==null?"":vehicle;
	site=site==null?"":site;
	ArrayList<TripDetailsDto> dtoList = new ArrayList<TripDetailsDto>();		
	try {
		if(!site.equals("")&&!vendorId.equals("")) {		 
		String vehicleQuery="";
		String vehquery="";
		if(vehicleType.equals("")==false&&vehicleType.trim().equalsIgnoreCase("all")==false) {
			vehicleQuery= "  and td.vehicle_type="+ vehicleType + " ";
		}
		String dateQuery = "";
		if( !fromDate.equals("") ) {
			dateQuery = " and td.trip_date >= '" + OtherFunctions.changeDateFromatToIso(fromDate)+"'";
		}
		if( !toDate.equals("") ) {
			dateQuery = dateQuery +  " and td.trip_date <= '" +
					 OtherFunctions.changeDateFromatToIso(toDate)+"'";
		}
		if(vehicle.equals("")==false&&vehicle.trim().equalsIgnoreCase("all")==false) {
			vehquery= "  and v.id="+ vehicle + " ";
		}
		String constraintQuery =" ";
	//	String constraintQuery = " and ( tbc.ac_constraint='"+BillingTypeConfigConstants.AC_YES+"' OR tbc.distance_constraint ='"+BillingTypeConfigConstants.DC_YES+"' )";
		if(constraint.equals(BillingTypeConfigConstants.AC_LABEL)) {
			constraintQuery = " and  tbc.ac_constraint='"+BillingTypeConfigConstants.AC_YES+"' ";
		} else if(constraint.equals(BillingTypeConfigConstants.DC_LABEL)) {
			constraintQuery = " and  tbc.distance_constraint ='"+BillingTypeConfigConstants.DC_YES+"' ";
		} else if (constraint.equals(BillingTypeConfigConstants.NC_LABEL)) {
			constraintQuery =  " and ( tbc.ac_constraint='"+BillingTypeConfigConstants.AC_NO+"' AND tbc.distance_constraint ='"+BillingTypeConfigConstants.DC_NO+"' )";
		}
		
		String escortQuery = "";
		if(escort.equals(BillingTypeConfigConstants.WITH_ESCORT)) {
			escortQuery = " and   tEscort='"+BillingTypeConfigConstants.ESCORT_YES +"'";
		} else if(escort.equals(BillingTypeConfigConstants.WITHOUT_ESCORT)) {
			escortQuery = " and   tEscort='"+BillingTypeConfigConstants.ESCORT_NO+"'";
		}  
	 
	 
		
		String billingApprovalStatus= "Approved by Transport Manager";
	
		String query1 = " select v.regno,td.trip_code, td.trip_date, td.vehicleTypeName, td.trip_time, td.trip_log, td.security_status, targ.tripId, targ.tripTripBillingId, targ.tripTripRate, tbc.ac_constraint, tbc.distance_constraint, targ.tripEscortRate,  dc.fromKm, dc.toKm, ifnull(dc.tripRate, tbc.trip_rate) tripRate, td.distance,  concat(dc.fromKm,'-', dc.toKm ) distanceRange, targ.tripEscortRate  from  td_billing_args targ join  " +
				"( select vtp.escort tEscort, ifnull(vtp.approvalStatus,'Open') approvalStatus, tva.vendorId, td.*, vt.type vehicleTypeName from trip_details td join vehicle_type vt on td.vehicle_type=vt.id join tripvendorassign tva on td.id= tva.tripid  left join vendor_trip_sheet_parent vtp on td.id = vtp.tripId where approvalStatus = '" + billingApprovalStatus + "'    )    td " +
				" on targ.tripId = td.id join vehicles v on v.id=td.vehicle join  (select tbc.id, tbc.ac_constraint, tbc.distance_constraint, tbc.trip_rate from trip_based_config tbc where 1=1 "+constraintQuery+" ) tbc on tbc.id= targ.tripTripBillingId left join ( select tbc.status, tbc.id, tbc.ac_constraint,  tbcd.fromKm, tbcd.toKm, ifnull( tbcd.rate, tbc.trip_rate) tripRate from trip_based_config tbc left join billing_config_distance_constraint tbcd on tbcd.bc_refId=tbc.id where tbc.status= 'a' and tbcd.status='a'  ) dc on targ.tripTripBillingId=dc.id and td.distance between dc.fromKm and dc.toKm "+ 
						" where td.siteId="+site+" and td.vendorId="+ vendorId + vehicleQuery + dateQuery+vehquery+ escortQuery+constraintQuery;
					 
		System.out.println("QUERY: " +query1);
		con = dbConn.connectDB();
		stmt = con.createStatement();
		rs = stmt.executeQuery(query1);

		while (rs.next()) {
			TripDetailsDto dto = new TripDetailsDto();
			dto.setTrip_code(rs.getString("trip_code"));
			dto.setTripDate(rs.getDate("trip_date"));
			dto.setTrip_time(rs.getString("trip_time"));
			dto.setTrip_log(rs.getString("trip_log"));
			dto.setEscortRate(rs.getDouble("tripEscortRate"));
			dto.setVehicle_type(rs.getString("vehicleTypeName"));
			dto.setVehicleNo(rs.getString("regno")); 
			dto.setFromKm(""+rs.getString("fromKm"));
			dto.setToKm(""+rs.getString("toKm"));
			 dto.setDistanceRange(rs.getString("distanceRange")); 
			dto.setTripRate (rs.getDouble("tripTripRate"));
			
		 	 	 dtoList.add(dto);
		}
		return dtoList;
		}else  {
			System.out.println("validation error : ");
		}

	} catch (Exception e) {
		System.out.println("Error in getbyVehicleTypeReport "+ e);
	} finally {
		DbConnect.closeResultSet(rs);
		DbConnect.closeStatement(stmt);
		DbConnect.closeConnection(con);
	}

	return null;		

	
}

public ArrayList<TripUtilisationReportDto> getTripsMonthly(String site, String fromDate, String toDate) {
	DbConnect dbConn = DbConnect.getInstance();
	Connection con = null;
	Statement stmt = null;
	ResultSet rs = null;	
	fromDate=OtherFunctions.changeDateFromatToIso(fromDate);
	toDate = OtherFunctions.changeDateFromatToIso(toDate);				
	fromDate=fromDate==null?"":fromDate;
	toDate=toDate==null?"":toDate;
	site=site==null?"":site;
	ArrayList<TripUtilisationReportDto> dtoList = new ArrayList<TripUtilisationReportDto>();		
	try {
		String query="select DATE_FORMAT(td.trip_date, '%d/%m/%Y') as trip_date,count(td.id) as totaltrips, SUM(CASE WHEN td.trip_log='IN' THEN 1 ELSE 0 END) as incount,SUM(CASE WHEN td.security_status='Yes' THEN 1 ELSE 0 END) as escort from trip_details td where td.trip_date between '"+fromDate+"'  and '"+toDate+"' and td.siteid="+site+" and td.status in ('saved','saveedit','addsave') and td.vehicle is not null group by td.trip_date ";
		System.out.println("QUERY: " +query);
		con = dbConn.connectDB();
		stmt = con.createStatement();
		rs = stmt.executeQuery(query);
		while (rs.next()) {
			TripUtilisationReportDto dto = new TripUtilisationReportDto();
			dto.setTrip_date(rs.getString("trip_date"));
			dto.setTotalcount(rs.getInt("totaltrips"));
			dto.setIncount(rs.getInt("incount"));
			dto.setEscortcount(rs.getInt("escort"));
			dto.setOutcount(dto.getTotalcount()-dto.getIncount());
			dto.setNonescortcount(dto.getTotalcount()-dto.getEscortcount());
		 	 	 dtoList.add(dto);
		}
		return dtoList;
		
	} catch (Exception e) {
		System.out.println("Error in trip summary report"+ e);
	} finally {
		DbConnect.closeResultSet(rs);
		DbConnect.closeStatement(stmt);
		DbConnect.closeConnection(con);
	}

	return null;		

	
}

public ArrayList<TripDetailsDto> getTripsDaily(String site,String Date) {
	DbConnect dbConn = DbConnect.getInstance();
	Connection con = null;
	Statement stmt = null;
	ResultSet rs = null;	
	Date=OtherFunctions.changeDateFromatToIso(Date);		
	Date=Date==null?"":Date;
	site=site==null?"":site;
	ArrayList<TripDetailsDto> dtoList = new ArrayList<TripDetailsDto>();		
	try {
		String query="select DATE_FORMAT(td.trip_date, '%d/%m/%Y') as trip_date,td.trip_code,vt.type,v.regno,td.trip_time,td.trip_log,td.security_status,SUM(CASE WHEN vts.tripid=td.id THEN 1 ELSE 0 END) as actualcount,SUM(CASE WHEN vts.tripid=td.id and vts.showstatus='Show' THEN 1 ELSE 0 END) as incount from trip_details td join vehicles v on v.id=td.vehicle join vehicle_type  vt on vt.id=td.vehicle_type  join vendor_trip_sheet vts on vts.tripid=td.id  where td.trip_date='"+Date+"' and td.status in ('saved','saveedit','addsave') and td.siteid="+site+" group by td.id";
		System.out.println("QUERY: " +query);
		con = dbConn.connectDB();
		stmt = con.createStatement();
		rs = stmt.executeQuery(query);
		while (rs.next()) {
			TripDetailsDto dto = new TripDetailsDto();
			dto.setTrip_date(rs.getString("trip_date"));
			dto.setTrip_code(rs.getString("trip_code"));
			dto.setVehicle_type(rs.getString("type"));
			dto.setVehicleNo(rs.getString("regno"));
			dto.setTrip_time(rs.getString("trip_time"));
			dto.setTrip_log(rs.getString("trip_log"));
			dto.setIsSecurity(rs.getString("security_status"));
			dto.setEmpCount(rs.getInt("actualcount"));
			dto.setEmpInCount(rs.getInt("incount"));
		 	dtoList.add(dto);
		}
		return dtoList;
		
	} catch (Exception e) {
		System.out.println("Error in trip summary report"+ e);
	} finally {
		DbConnect.closeResultSet(rs);
		DbConnect.closeStatement(stmt);
		DbConnect.closeConnection(con);
	}

	return null;		

	
}

public ArrayList<TripDetailsDto> getTripsDistancewise(String site, String fromDate, String toDate) {
	DbConnect dbConn = DbConnect.getInstance();
	Connection con = null;
	Statement stmt = null;
	ResultSet rs = null;	
	fromDate=OtherFunctions.changeDateFromatToIso(fromDate);
	toDate = OtherFunctions.changeDateFromatToIso(toDate);				
	fromDate=fromDate==null?"":fromDate;
	toDate=toDate==null?"":toDate;
	site=site==null?"":site;
	ArrayList<TripDetailsDto> dtoList = new ArrayList<TripDetailsDto>();		
	try {
		String query="select td.trip_code,DATE_FORMAT(td.trip_date, '%d/%m/%Y') as trip_date,td.trip_time,td.trip_log,td.security_status,td.distance,vt.type from trip_details td join vehicle_type vt on vt.id=td.vehicle_type  where trip_date between '"+fromDate+"' and '"+toDate+"' and siteid="+site;
		System.out.println("QUERY: " +query);
		con = dbConn.connectDB();
		stmt = con.createStatement();
		rs = stmt.executeQuery(query);
		while (rs.next()) {
			TripDetailsDto dto = new TripDetailsDto();
			dto.setTrip_code(rs.getString("trip_code"));
			dto.setTrip_date(rs.getString("trip_date"));
			dto.setTrip_time(rs.getString("trip_time"));
			dto.setTrip_log(rs.getString("trip_log"));
			dto.setIsSecurity(rs.getString("security_status"));
			dto.setDistance(rs.getString("distance"));
			dto.setVehicle_type(rs.getString("type"));
		 	 	 dtoList.add(dto);
		}
		return dtoList;
		
	} catch (Exception e) {
		System.out.println("Error in trip summary report"+ e);
	} finally {
		DbConnect.closeResultSet(rs);
		DbConnect.closeStatement(stmt);
		DbConnect.closeConnection(con);
	}

	return null;		

	
}

public ArrayList<TripDetailsDto> getTripBasedBillingTripCostReportmonthlywithlog(String site, String vendorId, String vehicleType, String fromDate, String toDate, String escort, String logtimes[],String vehicle) {
	DbConnect dbConn = DbConnect.getInstance();
	Connection con = null;
	Statement stmt = null;
	ResultSet rs = null;	
	fromDate=OtherFunctions.changeDateFromatToIso(fromDate);
	toDate = OtherFunctions.changeDateFromatToIso(toDate);
	vehicleType=vehicleType==null?"":vehicleType;
	vendorId=vendorId==null?"":vendorId;					
	fromDate=fromDate==null?"":fromDate;
	toDate=toDate==null?"":toDate;
	vehicle=vehicle==null?"":vehicle;
	site=site==null?"":site;
	ArrayList<TripDetailsDto> dtoList = new ArrayList<TripDetailsDto>();		
	try {
		if(!site.equals("")&&!vendorId.equals("")) {		 
		String vehicleQuery="";
		String vehquery="";
		if(vehicleType.equals("")==false&&vehicleType.trim().equalsIgnoreCase("all")==false) {
			vehicleQuery= "  and td.vehicle_type="+ vehicleType + " ";
		}
		String dateQuery = "";
		if( !fromDate.equals("") ) {
			dateQuery = " and td.trip_date >= '" + OtherFunctions.changeDateFromatToIso(fromDate)+"'";
		}
		if( !toDate.equals("") ) {
			dateQuery = dateQuery +  " and td.trip_date <= '" +
					 OtherFunctions.changeDateFromatToIso(toDate)+"'";
		}
		if(vehicle.equals("")==false&&vehicle.trim().equalsIgnoreCase("all")==false) {
			vehquery= "  and v.id="+ vehicle + " ";
		}
	
		
		String escortQuery = "";
		if(escort.equals(BillingTypeConfigConstants.WITH_ESCORT)) {
			escortQuery = " and   tEscort='"+BillingTypeConfigConstants.ESCORT_YES +"'";
		} else if(escort.equals(BillingTypeConfigConstants.WITHOUT_ESCORT)) {
			escortQuery = " and   tEscort='"+BillingTypeConfigConstants.ESCORT_NO+"'";
		}  
	 String sitequery="";
if(!site.equalsIgnoreCase("All"))	
	sitequery="td.siteId="+site+" and";
		String LogQuery="",times="";
		if(logtimes.length>0)
		{
			for(int i=0;i<=logtimes.length-1;i++)
			{
				times+="'"+logtimes[i]+"'";
				if(i!=logtimes.length-1)
					times+=",";
			}
			LogQuery=" and concat(td.trip_time,'-',td.trip_log) in ("+times+")";
		}
			
		String billingApprovalStatus= "Approved by Transport Manager";
	
		String query1 = " select site.site_name,v.regno,td.trip_code, td.trip_date, td.vehicleTypeName, td.trip_time, td.trip_log, td.security_status, targ.tripId, targ.tripTripBillingId, targ.tripTripRate, tbc.ac_constraint, tbc.distance_constraint, targ.tripEscortRate,  dc.fromKm, dc.toKm, ifnull(dc.tripRate, tbc.trip_rate) tripRate, td.distance,  concat(dc.fromKm,'-', dc.toKm ) distanceRange, targ.tripEscortRate  from  td_billing_args targ join  " +
				"( select vtp.escort tEscort, ifnull(vtp.approvalStatus,'Open') approvalStatus, tva.vendorId, td.*, vt.type vehicleTypeName from trip_details td join vehicle_type vt on td.vehicle_type=vt.id join tripvendorassign tva on td.id= tva.tripid  left join vendor_trip_sheet_parent vtp on td.id = vtp.tripId where approvalStatus = '" + billingApprovalStatus + "'    )    td " +
				" on targ.tripId = td.id join vehicles v on v.id=td.vehicle join  (select tbc.id, tbc.ac_constraint, tbc.distance_constraint, tbc.trip_rate from trip_based_config tbc where 1=1 ) tbc on tbc.id= targ.tripTripBillingId left join ( select tbc.status, tbc.id, tbc.ac_constraint,  tbcd.fromKm, tbcd.toKm, ifnull( tbcd.rate, tbc.trip_rate) tripRate from trip_based_config tbc left join billing_config_distance_constraint tbcd on tbcd.bc_refId=tbc.id where tbc.status= 'a' and tbcd.status='a'  ) dc on targ.tripTripBillingId=dc.id and td.distance between dc.fromKm and dc.toKm join site site on site.id=td.siteid"+ 
						" where "+sitequery+" td.vendorId="+ vendorId + vehicleQuery + dateQuery+vehquery+ escortQuery+LogQuery;
					 
		System.out.println("QUERY: " +query1);
		con = dbConn.connectDB();
		stmt = con.createStatement();
		rs = stmt.executeQuery(query1);

		while (rs.next()) {
			TripDetailsDto dto = new TripDetailsDto();
			dto.setSiteId(rs.getString("site_name"));
			dto.setTrip_code(rs.getString("trip_code"));
			dto.setTripDate(rs.getDate("trip_date"));
			dto.setTrip_time(rs.getString("trip_time"));
			dto.setTrip_log(rs.getString("trip_log"));
			dto.setEscortRate(rs.getDouble("tripEscortRate"));
			dto.setVehicle_type(rs.getString("vehicleTypeName"));
			dto.setVehicleNo(rs.getString("regno")); 
			dto.setFromKm(""+rs.getString("fromKm"));
			dto.setToKm(""+rs.getString("toKm"));
			 dto.setDistanceRange(rs.getString("distanceRange")); 
			dto.setTripRate (rs.getDouble("tripTripRate"));
			
		 	 	 dtoList.add(dto);
		}
		return dtoList;
		}else  {
			System.out.println("validation error : ");
		}

	} catch (Exception e) {
		System.out.println("Error in getbyVehicleTypeReport "+ e);
	} finally {
		DbConnect.closeResultSet(rs);
		DbConnect.closeStatement(stmt);
		DbConnect.closeConnection(con);
	}

	return null;		

	
}

}
