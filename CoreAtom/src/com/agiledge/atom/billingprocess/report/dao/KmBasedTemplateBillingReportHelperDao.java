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

public class KmBasedTemplateBillingReportHelperDao {

	 
		public ArrayList<TripBasedBillingReportHelperDto> getKmBasedTemplateBillingTripCostReport_monthly(String site, String vendorId, String vehicleType, String fromDate, String toDate, String month , String year, String escort, String constraint  ) {
			

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
					yearQuery = " and YEAR(td.trip_date)=" + year;
				}
				String monthQuery = "";
				if(!OtherFunctions.isEmpty( month)) {
					monthQuery = " and MONTH(td.trip_date)='" + month + "' ";
				}
				
				String constraintQuery = "";
				if(constraint.equals(BillingTypeConfigConstants.AC_LABEL)) {
					constraintQuery = " and tbcc.ac_constraint='"+BillingTypeConfigConstants.AC_YES+"' ";
				}
			 
				String escortQuery = "";
				if(escort.equals(BillingTypeConfigConstants.WITH_ESCORT)) {
					escortQuery = " and   tEscort='"+BillingTypeConfigConstants.ESCORT_YES+"'";
				} else if(escort.equals(BillingTypeConfigConstants.WITHOUT_ESCORT)) {
					escortQuery = " and   tEscort='"+BillingTypeConfigConstants.ESCORT_NO+"'";
				}  
			 

			 
		
			 
				 
				String query1 = " select YEAR( td.trip_date) tripYear,  MONTHNAME(td.trip_date) tripMonth, td.vehicleTypeName,  sum(targ.templateTripDistance) templateTripDistance, count(*) totTrips, sum(targ.templateTripRate) templateTripRate, sum(targ.templateEscortRate) templateEscortRate   from  td_billing_args targ join " +
						"(select vtp.manualDistance, vtp.escort tEscort, ifnull(vtp.approvalStatus,'Open') approvalStatus, tva.vendorId, td.*, vt.type vehicleTypeName from trip_details td join vehicle_type vt on td.vehicle_type=vt.id join tripvendorassign tva on td.id= tva.tripid  left join vendor_trip_sheet_parent vtp on td.id = vtp.tripId where approvalStatus = '"+SettingsConstant.APPROVAL_TYPE+"' ) td on targ.tripId = td.id join "+
								" (select tbcc.id, tbcc.ac_constraint, tbcc.trip_rate from km_based_trip_config tbc join km_based_template_trip_config  tbcc on tbcc.km_billing_id=tbc.id " + constraintQuery + " ) tbc on tbc.id= targ.templateTripBillingId " +
								" where td.siteId="+site+" and td.vendorId="+ vendorId + vehicleQuery + dateQuery+ yearQuery + monthQuery + escortQuery +
								" group by YEAR( td.trip_date), MONTHNAME(td.trip_date), td.vehicleTypeName ";
				con = dbConn.connectDB();
				stmt = con.createStatement();
				System.out.println("jjjj"+query1);
			 
				 				rs = stmt.executeQuery(query1);

				while (rs.next()) {
					TripBasedBillingReportHelperDto dto = new TripBasedBillingReportHelperDto();
					dto.setMonthName(rs.getString("tripMonth"));
					dto.setYear(rs.getString("tripYear"));
					dto.setVehicleType(rs.getString("vehicleTypeName"));
					 
					dto.setEscortRate(rs.getDouble("templateEscortRate")); 
				 	dto.setDistanceRange(rs.getString("templateTripDistance"));
					dto.setTrips(getKmBasedTemplateBillingTripCostReport_tripWise(site, vendorId, dto.getVehicleType(), fromDate, toDate, dto.getMonthName(), dto.getYear(), escort, constraint));
					 
					dto.setTotalTrips(rs.getInt("totTrips"));
					dto.setRate(rs.getDouble("templateTripRate"));
					
				 	 	 dtoList.add(dto);
				}
				return dtoList;
				}else  {
					System.out.println("validation error : ");
				}

			} catch (Exception e) {
				System.out.println("Error in getKmBasedTemplateBillingTripCostReport_monthly "+ e);
			} finally {
				DbConnect.closeResultSet(rs);
				DbConnect.closeStatement(stmt);
				DbConnect.closeConnection(con);
			}

			return null;		

			
		}
		
		public ArrayList<TripBasedBillingReportHelperDto> getKmBasedTemplateBillingTripCostReport_daily(String site, String vendorId, String vehicleType, String fromDate, String toDate, String month, String year, String escort, String constraint) {
			

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
				String constraintQuery = "";
				if(constraint.equals(BillingTypeConfigConstants.AC_LABEL)) {
					constraintQuery = " and tbcc.ac_constraint='"+BillingTypeConfigConstants.AC_YES+"' ";
				}
			 
			 
			
				String query1 = "  select   td.trip_date, td.vehicleTypeName,  sum( targ.templateTripDistance) templateTripDistance , count(*) totTrips, sum(targ.templateTripRate) templateTripRate, sum(targ.templateEscortRate) templateEscortRate  from  td_billing_args targ join " +
						"(select vtp.manualDistance, vtp.escort tEscort, ifnull(vtp.approvalStatus,'Open') approvalStatus, tva.vendorId, td.*, vt.type vehicleTypeName from trip_details td join vehicle_type vt on td.vehicle_type=vt.id join tripvendorassign tva on td.id= tva.tripid  left join vendor_trip_sheet_parent vtp on td.id = vtp.tripId where approvalStatus = 'Approved by Transport Manager' )   td on targ.tripId = td.id join  "+
								"(select tbcc.id, tbcc.ac_constraint, tbcc.trip_rate from km_based_trip_config tbc join km_based_template_trip_config  tbcc on tbcc.km_billing_id=tbc.id "+constraintQuery+" ) tbc on tbc.id= targ.templateTripBillingId" +
								" where td.siteId="+site+" and td.vendorId="+ vendorId + vehicleQuery + dateQuery+ yearQuery + monthQuery + escortQuery +
								" group by td.trip_date, td.vehicleTypeName ";
				System.out.println("DAIDLY QURY : " +query1);
				con = dbConn.connectDB();
				stmt = con.createStatement();
			 	 				rs = stmt.executeQuery(query1);

				while (rs.next()) {
					TripBasedBillingReportHelperDto dto = new TripBasedBillingReportHelperDto();
					dto.setDate(rs.getDate("trip_date"));
					dto.setEscortRate(rs.getDouble("templateEscortRate"));
					dto.setVehicleType(rs.getString("vehicleTypeName"));
				 	 dto.setDistanceRange(rs.getString("templateTripDistance"));
				 	 
					dto.setTotalTrips(rs.getInt("totTrips"));
					dto.setRate(rs.getDouble("templateTripRate"));
					dto.setTrips(getKmBasedTemplateBillingTripCostReport_tripWise(site, vendorId, dto.getVehicleType(), OtherFunctions.changeDateFromatToIso( dto.getDate()),OtherFunctions.changeDateFromatToIso( dto.getDate()), dto.getMonthName(), dto.getYear(), escort, constraint));
					
				 	 	 dtoList.add(dto);
				}
				return dtoList;
				}else  {
					System.out.println("validation error : ");
				}

			} catch (Exception e) {
				System.out.println("Error in getKmBasedTemplateBillingTripCostReport_daily "+ e);
			} finally {
				DbConnect.closeResultSet(rs);
				DbConnect.closeStatement(stmt);
				DbConnect.closeConnection(con);
			}

			return null;		

			
		}
		
		
		
	public ArrayList<TripDetailsDto> getKmBasedTemplateBillingTripCostReport_tripWise(String site, String vendorId, String vehicleType, String fromDate, String toDate, String month, String year, String escort, String constraint) {
			

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
				if(constraint.equals(BillingTypeConfigConstants.AC_LABEL)) {
					constraintQuery = " and tbcc.ac_constraint='"+BillingTypeConfigConstants.AC_YES+"' ";
				} 
				String escortQuery = "";
				if(escort.equals(BillingTypeConfigConstants.WITH_ESCORT)) {
					escortQuery = " and   tEscort='"+BillingTypeConfigConstants.ESCORT_YES +"'";
				} else if(escort.equals(BillingTypeConfigConstants.WITHOUT_ESCORT)) {
					escortQuery = " and   tEscort='"+BillingTypeConfigConstants.ESCORT_NO+"'";
				}  
			 
			 
				
			 
			
				String query1 = " select td.trip_code, td.trip_date, td.vehicleTypeName, td.trip_time, td.trip_log, td.security_status, targ.tripId, targ.classicTripBillingId, targ.templateTripRate, tbc.ac_constraint, targ.templateEscortRate, targ.templateTripDistance from  td_billing_args targ join  " +
						"(select vtp.manualDistance, vtp.escort tEscort, ifnull(vtp.approvalStatus,'Open') approvalStatus, tva.vendorId, td.*, vt.type vehicleTypeName from trip_details td join vehicle_type vt on td.vehicle_type=vt.id join tripvendorassign tva on td.id= tva.tripid  left join vendor_trip_sheet_parent vtp on td.id = vtp.tripId where approvalStatus = 'Approved by Transport Manager' ) td on targ.tripId = td.id" +
						"   join (select tbcc.id, tbcc.ac_constraint, tbcc.trip_rate from km_based_trip_config tbc join km_based_template_trip_config  tbcc on tbcc.km_billing_id=tbc.id where 1=1 "+constraintQuery+"  ) tbc on tbc.id= targ.templateTripBillingId  "+ 
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
					dto.setEscortRate(rs.getDouble("templateEscortRate"));
					dto.setVehicle_type(rs.getString("vehicleTypeName"));
					 
					 
 					 dto.setDistanceRange(rs.getString("templateTripDistance")); 
					dto.setTripRate (rs.getDouble("templateTripRate"));
					
				 	 	 dtoList.add(dto);
				}
				return dtoList;
				}else  {
					System.out.println("validation error : ");
				}

			} catch (Exception e) {
				System.out.println("Error in getKmBasedTemplateBillingTripCostReport_tripWise "+ e);
			} finally {
				DbConnect.closeResultSet(rs);
				DbConnect.closeStatement(stmt);
				DbConnect.closeConnection(con);
			}

			return null;		

			
		}


	 
}
