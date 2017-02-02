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

public class SlabBasedBillingReportHelperDao {

	 
		public ArrayList<TripBasedBillingReportHelperDto> getSlabBasedBillingTripCostReport_monthly(String site, String vendorId, String vehicleType, String fromDate, String toDate, String month , String year, String escort   ) {
			

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
				
				String constraintQuery = "";
		 
				String escortQuery = "";
				if(escort.equals(BillingTypeConfigConstants.WITH_ESCORT)) {
					escortQuery = " and   tEscort='"+BillingTypeConfigConstants.ESCORT_YES+"'";
				} else if(escort.equals(BillingTypeConfigConstants.WITHOUT_ESCORT)) {
					escortQuery = " and   tEscort='"+BillingTypeConfigConstants.ESCORT_NO+"'";
				}  
			 

			 
		
			 
				 
				String query1 = " select   year(td.trip_date) tripYear, monthname(td.trip_date) tripMonth, td.vehicleTypeName, sum( td.distance) distance, count(*) totTrips, sum(targ.slabTripRate) slabTripRate , sum(targ.slabEscortRate) slabEscortRate from  td_billing_args targ join (select vtp.manualDistance, vtp.escort tEscort, ifnull(vtp.approvalStatus,'Open') approvalStatus, tva.vendorId, td.*, vt.type vehicleTypeName from trip_details td join vehicle_type vt on td.vehicle_type=vt.id join tripvendorassign tva on td.id= tva.tripid  left join vendor_trip_sheet_parent vtp on td.id = vtp.tripId where approvalStatus = '"+SettingsConstant.APPROVAL_TYPE+"' ) td on targ.tripId = td.id join (select tbc.id, tbc.trip_rate from  slab_based_billing_config tbc ) tbc on tbc.id= targ.slabTripBillingId " +
								" where td.siteId="+site+" and td.vendorId="+ vendorId + vehicleQuery + dateQuery+ yearQuery + monthQuery + escortQuery +
								" group by year(td.trip_date), monthname(td.trip_date) , td.vehicleTypeName ";
				con = dbConn.connectDB();
				stmt = con.createStatement();
				System.out.println(query1);
			 
				 				rs = stmt.executeQuery(query1);

				while (rs.next()) {
					TripBasedBillingReportHelperDto dto = new TripBasedBillingReportHelperDto();
					dto.setMonthName(rs.getString("tripMonth"));
					dto.setYear(rs.getString("tripYear"));
					dto.setVehicleType(rs.getString("vehicleTypeName"));
					 
					dto.setEscortRate(rs.getDouble("slabEscortRate")); 
				 	dto.setDistanceRange(rs.getString("distance"));
					dto.setTrips(getSlabBasedBillingTripCostReport_tripWise(site, vendorId, dto.getVehicleType(), fromDate, toDate, dto.getMonthName(), dto.getYear(), escort  ));
					 
					dto.setTotalTrips(rs.getInt("totTrips"));
					dto.setRate(rs.getDouble("slabTripRate"));
					
				 	 	 dtoList.add(dto);
				}
				return dtoList;
				}else  {
					System.out.println("validation error : ");
				}

			} catch (Exception e) {
				System.out.println("Error in getSlabBasedBillingTripCostReport_monthly "+ e);
			} finally {
				DbConnect.closeResultSet(rs);
				DbConnect.closeStatement(stmt);
				DbConnect.closeConnection(con);
			}

			return null;		

			
		}
		
		public ArrayList<TripBasedBillingReportHelperDto> getSlabBasedBillingTripCostReport_daily(String site, String vendorId, String vehicleType, String fromDate, String toDate, String month, String year, String escort ) {
			

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
				 
				
				String escortQuery = "";
				if(escort.equals(BillingTypeConfigConstants.WITH_ESCORT)) {
					escortQuery = " and   tEscort='"+BillingTypeConfigConstants.ESCORT_YES+"'";
				} else if(escort.equals(BillingTypeConfigConstants.WITHOUT_ESCORT)) {
					escortQuery = " and   tEscort='"+BillingTypeConfigConstants. ESCORT_NO+"'";
				}
			 
				 
			 
			
				String query1 = " select   td.trip_date, td.vehicleTypeName, sum(td.distance) distance, count(*) totTrips, sum(targ.slabTripRate) slabTripRate, sum(targ.slabEscortRate) slabEscortRate from  td_billing_args targ join (select vtp.manualDistance, vtp.escort tEscort, ifnull(vtp.approvalStatus,'Open') approvalStatus, tva.vendorId, td.*, vt.type vehicleTypeName from trip_details td join vehicle_type vt on td.vehicle_type=vt.id join tripvendorassign tva on td.id= tva.tripid  left join vendor_trip_sheet_parent vtp on td.id = vtp.tripId where approvalStatus = '"+SettingsConstant.APPROVAL_TYPE+"'  ) td on targ.tripId = td.id join (select tbc.id, tbc.trip_rate from  slab_based_billing_config tbc ) tbc on tbc.id= targ.slabTripBillingId " +
								" where td.siteId="+site+" and td.vendorId="+ vendorId + vehicleQuery + dateQuery+ yearQuery + monthQuery + escortQuery +
								" group by td.trip_date, td.vehicleTypeName";
				  
				System.out.println("DAIDLY QURY : " +query1);
				con = dbConn.connectDB();
				stmt = con.createStatement();
			 	 				rs = stmt.executeQuery(query1);

				while (rs.next()) {
					TripBasedBillingReportHelperDto dto = new TripBasedBillingReportHelperDto();
					dto.setDate(rs.getDate("trip_date"));
					dto.setEscortRate(rs.getDouble("slabEscortRate"));
					dto.setVehicleType(rs.getString("vehicleTypeName"));
				 	 dto.setDistanceRange(rs.getString("distance"));
				 	 
					dto.setTotalTrips(rs.getInt("totTrips"));
					dto.setRate(rs.getDouble("slabTripRate"));
					dto.setTrips(getSlabBasedBillingTripCostReport_tripWise(site, vendorId, dto.getVehicleType(), OtherFunctions.changeDateFromatToIso( dto.getDate()),OtherFunctions.changeDateFromatToIso( dto.getDate()), dto.getMonthName(), dto.getYear(), escort ));
					
				 	 	 dtoList.add(dto);
				}
				return dtoList;
				}else  {
					System.out.println("validation error : ");
				}

			} catch (Exception e) {
				System.out.println("Error in getSlabBasedBillingTripCostReport_daily "+ e);
			} finally {
				DbConnect.closeResultSet(rs);
				DbConnect.closeStatement(stmt);
				DbConnect.closeConnection(con);
			}

			return null;		

			
		}
		
		
		
	public ArrayList<TripDetailsDto> getSlabBasedBillingTripCostReport_tripWise(String site, String vendorId, String vehicleType, String fromDate, String toDate, String month, String year, String escort ) {
			

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
				 
				String constraintQuery = "";
			 	String escortQuery = "";
				if(escort.equals(BillingTypeConfigConstants.WITH_ESCORT)) {
					escortQuery = " and   tEscort='"+BillingTypeConfigConstants.ESCORT_YES +"'";
				} else if(escort.equals(BillingTypeConfigConstants.WITHOUT_ESCORT)) {
					escortQuery = " and   tEscort='"+BillingTypeConfigConstants.ESCORT_NO+"'";
				}  
			 
			 
				
			 
			
				String query1 = "  select td.trip_code, td.trip_date, td.vehicleTypeName, td.trip_time, td.trip_log, td.security_status, targ.tripId, targ.slabTripBillingId, targ.slabTripRate, targ.slabEscortRate,  td.distance from  td_billing_args targ join  (select vtp.manualDistance, vtp.escort tEscort, ifnull(vtp.approvalStatus,'Open') approvalStatus, tva.vendorId, td.*, vt.type vehicleTypeName from trip_details td join vehicle_type vt on td.vehicle_type=vt.id join tripvendorassign tva on td.id= tva.tripid  left join vendor_trip_sheet_parent vtp on td.id = vtp.tripId where approvalStatus = 'Approved by Transport Manager' )   td on targ.tripId = td.id join (select tbc.id, tbc.trip_rate from  slab_based_billing_config tbc ) tbc on tbc.id= targ.slabTripBillingId  "+ 
								" where td.siteId="+site+" and td.vendorId="+ vendorId + vehicleQuery + dateQuery+ yearQuery + monthQuery + escortQuery;
							 
				System.out.println("DAIDLY QURY : " +query1);
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
					dto.setEscortRate(rs.getDouble("slabEscortRate"));
					dto.setVehicle_type(rs.getString("vehicleTypeName"));
					 
					 
				 	 dto.setDistanceRange(rs.getString("distance")); 
					dto.setTripRate (rs.getDouble("slabTripRate"));
					
				 	 	 dtoList.add(dto);
				}
				return dtoList;
				}else  {
					System.out.println("validation error : ");
				}

			} catch (Exception e) {
				System.out.println("Error in getSlabBasedBillingTripCostReport_tripWise "+ e);
			} finally {
				DbConnect.closeResultSet(rs);
				DbConnect.closeStatement(stmt);
				DbConnect.closeConnection(con);
			}

			return null;		

			
		}




}
