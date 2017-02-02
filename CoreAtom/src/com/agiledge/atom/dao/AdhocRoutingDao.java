package com.agiledge.atom.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.agiledge.atom.commons.OtherFunctions;
import com.agiledge.atom.constants.SettingsConstant;
import com.agiledge.atom.dbConnect.DbConnect;
import com.agiledge.atom.dbConnect.hibernate.HibernateUtil;
import com.agiledge.atom.dto.AdhocDto;
import com.agiledge.atom.dto.TripDetailsChildDto;
import com.agiledge.atom.dto.TripDetailsDto;
import com.agiledge.atom.dto.VendorDto;
import com.agiledge.atom.hibernate.dto.AdhocTripHBMDto;

public class AdhocRoutingDao {
	public static int adhocHorA = 0;
	public static int adhocatD = 0;
	
	private boolean bookingAlreadyExists(String bookingId, ArrayList<TripDetailsDto> trips) {
		boolean retVal = false;
		try {
			for(TripDetailsDto dto : trips) {
				if(dto.getScheduleId().trim().equalsIgnoreCase(bookingId)) {
					retVal = true;
					break;
				}
			}
		}catch(Exception ignore){}
		
		return retVal;
	}

	public int insertToTrip(ArrayList<AdhocDto> bookingList) {
		DbConnect ob = DbConnect.getInstance();
		Connection con = ob.connectDB();

		int retVal = 0;
		String tripCode = "";

		 
		PreparedStatement pst = null;
		String query = " INSERT INTO adhocTrips (adhoctype,tripCode,siteId,travelDate,startTime,endTime,startPlace,endPlace,vehicleType,employeeId,status,isEscort, bookingId)"
				+ " VALUES (?,?,?,?,?,?,?,?,?,?,?,?, ?)";
		 
		try {
			con.setAutoCommit(false);
			pst = con.prepareStatement(query);
			System.out.println(bookingList.size());
			if(bookingList!=null&&bookingList.size()>0) {
				 
				
				 for (AdhocDto dto : bookingList) {
					 hardDeleteAdhocTrips(dto, con);
				 
					tripCode = "";
					if (dto.getAdhocType().equalsIgnoreCase(
							SettingsConstant.HOTEL_OR_AIRPORT)) {
						tripCode = "" + adhocHorA;
						adhocHorA++;
					} else if (dto.getAdhocType().equalsIgnoreCase(
							SettingsConstant.AT_DISPOSAL)) {
						tripCode = "" + adhocatD;
						adhocatD++;
					}
					try{
					pst.setString(1, dto.getAdhocType());
					pst.setString(2, tripCode);
					pst.setString(3, dto.getSiteId());
					pst.setString(4, dto.getTravelDate());
					pst.setString(5, dto.getStartTime());
					pst.setString(6, dto.getEndTime());
					pst.setString(7, dto.getOrgination());
					pst.setString(8, dto.getDestination());
					pst.setString(9, dto.getVehicleType());
					pst.setString(10, dto.getBookedFor());
					pst.setString(11, "routed");
					pst.setString(12, dto.getEscort());
					pst.setLong(13, dto.getId());
					retVal += pst.executeUpdate();
					}catch(SQLException e) {
						System.out.println(" "+ e);
					}
				 
				}
				 con.commit();
			}
		} catch (Exception e) {
			System.out.println("Error in inserting to adhoc trip " + e);
			try {
				con.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
			System.out.println("Transaction rolled back due to " + e1);
			}
		} finally {
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);

		}
		return retVal;
	}

	public ArrayList<TripDetailsDto> getTripDetails(String siteId, String date) {
		DbConnect ob = DbConnect.getInstance();
		Connection con = ob.connectDB();
		ArrayList<TripDetailsDto> arrayList = new ArrayList<TripDetailsDto>();
		PreparedStatement pst = null;
		ResultSet rs = null;
		String query = " select at.*,e.displayname,vt.type from adhoctrips at,employee e,vehicle_type vt where at.employeeId=e.id and vt.id=at.vehicleType and  siteId="
				+ siteId + " and travelDate='" + date + "'";
		System.out.println(query);
		TripDetailsDto dto = null;
		try {
			pst = con.prepareStatement(query);
			rs = pst.executeQuery();
			while (rs.next()) {
				dto = new TripDetailsDto();
				dto.setId(rs.getString("id"));
				//dto.setTripBasedDistance(rs.getString("enteredDistance"));
				
				dto.setVehicleNo(rs.getString("vehicleNo" ));
				
				dto.setTripType(rs.getString("adhocType"));
				dto.setTrip_date(rs.getString("travelDate"));
				dto.setTrip_code(rs.getString("tripCode"));
				dto.setSiteId(rs.getString("siteId"));
				dto.setVehicle_type(rs.getString("type"));
				dto.setVehicleTypeId(rs.getString("vehicleType"));
				dto.setDistance(rs.getString("enteredDistance"));
				dto.setDistanceCovered(rs.getFloat("actualDistance"));
				dto.setTravelTime(rs.getString("travelTime"));
				dto.setStartTime(rs.getString("startTime"));
				dto.setTrip_time(dto.getStartTime());// for bill
				dto.setTrip_log("IN");//FOR BILL				
				dto.setStopTime(rs.getString("endTime"));
				dto.setStartPlace(rs.getString("startPlace"));
				dto.setEndPlace(rs.getString("endPlace"));
				dto.setStatus(rs.getString("status"));
				dto.setIsSecurity(rs.getString("isEscort"));
				dto.setEscort(rs.getString("escortApprove"));
				ArrayList<TripDetailsChildDto> subList = new ArrayList<TripDetailsChildDto>();
				TripDetailsChildDto subdto = new TripDetailsChildDto();
				subdto.setEmployeeId(rs.getString("employeeId"));
				subdto.setEmployeeName(rs.getString("displayname"));
				subList.add(subdto);
				dto.setTripDetailsChildDtoList(subList);
				arrayList.add(dto);
			}

		} catch (Exception e) {
			System.out.println("Error in geting adhoc trip " + e);
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);

		}
		return arrayList;
	}

	

	public ArrayList<TripDetailsDto> getSavedTripDetails( TripDetailsDto pdto, String approvalStatus[]) {
		DbConnect ob = DbConnect.getInstance();
		Connection con = ob.connectDB();
		ArrayList<TripDetailsDto> arrayList = new ArrayList<TripDetailsDto>();
		PreparedStatement pst = null;
		ResultSet rs = null;

		
		TripDetailsDto dto = null;
		try {
			String query = " select at.*,e.displayname,vt.type from adhoctrips at,employee e,vehicle_type vt where at.employeeId=e.id and vt.id=at.vehicleType and status='saved' and  siteId="
					+ pdto.getSiteId() + " and travelDate='" + pdto.getTrip_date() + "'";
			if(approvalStatus!=null && approvalStatus.length >0) {
				query = query +  " and approvalStatus in (";
				for(String status : approvalStatus) {
					query = query + "'" + status + "', ";
				}
				query = query.substring(0, query.length()-2) + ") ";
				
			}
			 
			System.out.println(query);
			pst = con.prepareStatement(query);
			rs = pst.executeQuery();
			while (rs.next()) {
				dto = new TripDetailsDto();
				dto.setId(rs.getString("id"));
				//dto.setTripBasedDistance(rs.getString("enteredDistance"));
				
				dto.setVehicleNo(rs.getString("vehicleNo" ));
				
				dto.setTripType(rs.getString("adhocType"));
				dto.setTrip_date(rs.getString("travelDate"));
				dto.setTrip_code(rs.getString("tripCode"));
				dto.setSiteId(rs.getString("siteId"));
				dto.setVehicle_type(rs.getString("type"));
				dto.setVehicleTypeId(rs.getString("vehicleType"));
				dto.setDistance(rs.getString("enteredDistance"));
				dto.setDistanceCovered(rs.getFloat("actualDistance"));
				dto.setTravelTime(rs.getString("travelTime"));
				dto.setStartTime(rs.getString("startTime"));
				dto.setTrip_time(dto.getStartTime());// for bill
				dto.setTrip_log("IN");//FOR BILL				
				dto.setStopTime(rs.getString("endTime"));
				dto.setStartPlace(rs.getString("startPlace"));
				dto.setEndPlace(rs.getString("endPlace"));
				dto.setStatus(rs.getString("status"));
				dto.setIsSecurity(rs.getString("isEscort"));
				dto.setEscort(rs.getString("escortApprove"));
				dto.setApprovalStatus(rs.getString("approvalStatus"));
				ArrayList<TripDetailsChildDto> subList = new ArrayList<TripDetailsChildDto>();
				TripDetailsChildDto subdto = new TripDetailsChildDto();
				subdto.setEmployeeId(rs.getString("employeeId"));
				subdto.setEmployeeName(rs.getString("displayname"));
				subList.add(subdto);
				dto.setTripDetailsChildDtoList(subList);
				arrayList.add(dto);
			}

		} catch (Exception e) {
			System.out.println("Error in geting adhoc trip " + e);
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);

		}
		return arrayList;
	}


	public int SaveTrip(String[] tripids) {
		DbConnect ob = DbConnect.getInstance();
		Connection con = ob.connectDB();
		PreparedStatement pst = null;
		int retVal = 0;
		String query = "update adhoctrips set status='saved' where id =?";
		try {
			pst = con.prepareStatement(query);
			for (String tripid : tripids) {
				pst.setString(1, tripid);
				retVal += pst.executeUpdate();
			}
		} catch (Exception e) {
			System.out.println("Error in saving adhoc trip " + e);
		} finally {
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);

		}
		return retVal;
	}

	public int approveTrip(ArrayList<TripDetailsDto> tripList) {
		DbConnect ob = DbConnect.getInstance();
		Connection con = ob.connectDB();
		PreparedStatement pst = null;
		int retVal = 0;
		String query = "update adhoctrips set enteredDistance=?,vehicleNo=?,escortApprove=?,escortId=?,status='approved' where id =?";
		try {
			pst = con.prepareStatement(query);
			for (TripDetailsDto tripDto : tripList) {	
				pst.setString(1,tripDto.getTripBasedDistance());
				pst.setString(2, tripDto.getVehicleNo());
				pst.setString(3, tripDto.getEscort());
				pst.setString(4, tripDto.getEscortId());				
				pst.setString(5, tripDto.getId());								
				retVal += pst.executeUpdate();
			}
		} catch (Exception e) {
			System.out.println("Error in saving adhoc trip " + e);
		} finally {
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);

		}
		return retVal;
	}
	public int rejectTrip(String[] tripids) {
		DbConnect ob = DbConnect.getInstance();
		Connection con = ob.connectDB();
		PreparedStatement pst = null;
		int retVal = 0;
		String query = "update adhoctrips set status='rejected' where id =?";
		try {
			pst = con.prepareStatement(query);
			for (String tripid : tripids) {
				pst.setString(1, tripid);
				retVal += pst.executeUpdate();
			}
		} catch (Exception e) {
			System.out.println("Error in saving adhoc trip " + e);
		} finally {
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);

		}
		return retVal;
	}
	//getOtherAdhocRoutingTripForVendor
	
	public ArrayList<TripDetailsDto> getOtherAdhocRoutingTripForVendor(TripDetailsDto paramDto) {
		// TODO Auto-generated method stub
		DbConnect ob = DbConnect.getInstance();
		Connection con = ob.connectDB();
 
		PreparedStatement pst = null;
		ResultSet rs = null;
		String query = " select at.*,e.displayname,vt.type, at.vendorId from adhoctrips at,employee e,vehicle_type vt where at.employeeId=e.id and vt.id=at.vehicleType ";
		String tripDateQuery="";
		String bookingIdQuery="";
		String statusQuery="";
		String vendorQuery="";
		ArrayList<TripDetailsDto> dtoList= new ArrayList<TripDetailsDto>();
		
		
		try {
			
			if(OtherFunctions.isEmpty( paramDto.getTrip_date())==false) {
				tripDateQuery = " and at.travelDate='" + OtherFunctions.changeDateFromatToIso(paramDto.getTrip_date()) + "'";
			}
			if(OtherFunctions.isEmpty( paramDto.getScheduleId())==false) {
				bookingIdQuery = " and at.bookingId='" + paramDto.getScheduleId() + "'";
			}
			if(OtherFunctions.isEmpty( paramDto.getStatus())==false) {
				statusQuery = " and at.status='" + paramDto.getStatus() + "'";
			}
			if(OtherFunctions.isEmpty( paramDto.getVendorId())==false) {
				statusQuery = " and at.vendorId!='" + paramDto.getVendorId() + "'";
			}
			
			if(OtherFunctions.isEmpty( paramDto.getSiteId())==false) {
				statusQuery = " and at.siteId='" + paramDto.getSiteId() + "'";
			}
			
			query = query + tripDateQuery + bookingIdQuery + statusQuery + vendorQuery;
			System.out.println(query);
			pst= con.prepareStatement(query);
			rs = pst.executeQuery();
			while (rs.next()) {
				TripDetailsDto dto = null;
				dto = new TripDetailsDto();
				dto.setId(rs.getString("id"));
				//dto.setTripBasedDistance(rs.getString("enteredDistance"));
				dto.setVehicleNo(rs.getString("vehicleNo" ));
				dto.setVendorId(  rs.getString("vendorId" ));
				dto.setTripType(rs.getString("adhocType"));
				dto.setTrip_date(rs.getString("travelDate"));
				dto.setTrip_code(rs.getString("tripCode"));
				dto.setSiteId(rs.getString("siteId"));
				dto.setVehicle_type(rs.getString("type"));
				dto.setVehicleTypeId(rs.getString("vehicleType"));
				dto.setDistance(rs.getString("enteredDistance"));
				dto.setDistanceCovered(rs.getFloat("actualDistance"));
				dto.setTravelTime(rs.getString("travelTime"));
				dto.setStartTime(rs.getString("startTime"));
				dto.setTrip_time(dto.getStartTime());// for bill
				dto.setTrip_log("IN");//FOR BILL				
				dto.setStopTime(rs.getString("endTime"));
				dto.setStartPlace(rs.getString("startPlace"));
				dto.setEndPlace(rs.getString("endPlace"));
				dto.setStatus(rs.getString("status"));
				dto.setIsSecurity(rs.getString("isEscort"));
				dto.setEscort(rs.getString("escortApprove"));
				dto.setScheduleId(rs.getString("bookingId"));
				dto.setLabel(dto.getId() + " " + dto.getStartPlace() + " "+ dto.getEndPlace());
				ArrayList<TripDetailsChildDto> subList = new ArrayList<TripDetailsChildDto>();
				TripDetailsChildDto subdto = new TripDetailsChildDto();
				subdto.setEmployeeId(rs.getString("employeeId"));
				subdto.setEmployeeName(rs.getString("displayname"));
				subList.add(subdto);
				dto.setTripDetailsChildDtoList(subList);
				
				dtoList.add(dto);
				 
			}

		} catch (Exception e) {
			System.out.println("Error in geting adhoc trip " + e);
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);

		}
		return dtoList;
 	}


	
	public TripDetailsDto getTripDetailsTrip(String tripId) {
		// TODO Auto-generated method stub
		DbConnect ob = DbConnect.getInstance();
		Connection con = ob.connectDB();
 
		PreparedStatement pst = null;
		ResultSet rs = null;
		String query = " select at.*,e.displayname,vt.type from adhoctrips at,employee e,vehicle_type vt where at.employeeId=e.id and vt.id=at.vehicleType and  at.id=" + tripId;
		System.out.println(query);
		TripDetailsDto dto = null;
		try {
			pst = con.prepareStatement(query);
			rs = pst.executeQuery();
			if (rs.next()) {
				dto = new TripDetailsDto();
				dto.setId(rs.getString("id"));
				//dto.setTripBasedDistance(rs.getString("enteredDistance"));
				dto.setVehicleNo(rs.getString("vehicleNo" ));
				dto.setScheduleId(rs.getString("bookingId"));
				dto.setTripType(rs.getString("adhocType"));
				dto.setTrip_date(rs.getString("travelDate"));
				dto.setTrip_code(rs.getString("tripCode"));
				dto.setSiteId(rs.getString("siteId"));
				dto.setVehicle_type(rs.getString("type"));
				dto.setVehicleTypeId(rs.getString("vehicleType"));
				dto.setDistance(rs.getString("enteredDistance"));
				dto.setDistanceCovered(rs.getFloat("actualDistance"));
				dto.setTravelTime(rs.getString("travelTime"));
				dto.setStartTime(rs.getString("startTime"));
				dto.setTrip_time(dto.getStartTime());// for bill
				dto.setTrip_log("IN");//FOR BILL				
				dto.setStopTime(rs.getString("endTime"));
				dto.setStartPlace(rs.getString("startPlace"));
				dto.setEndPlace(rs.getString("endPlace"));
				dto.setStatus(rs.getString("status"));
				dto.setIsSecurity(rs.getString("isEscort"));
				dto.setEscort(rs.getString("escortApprove"));
				ArrayList<TripDetailsChildDto> subList = new ArrayList<TripDetailsChildDto>();
				TripDetailsChildDto subdto = new TripDetailsChildDto();
				subdto.setEmployeeId(rs.getString("employeeId"));
				subdto.setEmployeeName(rs.getString("displayname"));
				subList.add(subdto);
				dto.setTripDetailsChildDtoList(subList);
				 
			}

		} catch (Exception e) {
			System.out.println("Error in geting adhoc trip " + e);
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);

		}
		return dto;
 	}
	
	private int hardDeleteAdhocTrips(AdhocDto paramDto, Connection con) {
		int val =0;
		
		PreparedStatement pst = null;
		 
		try {
			 System.out.println("delete from adhoctrips where bookingId="+paramDto.getId()+" and status='routed'");
			 pst = con.prepareStatement("delete from adhoctrips where bookingId=? and status='routed'");
			 pst.setLong(1, paramDto.getId());
			 val = pst.executeUpdate();
			 
		}catch(Exception e) {
			try {
				con.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		finally{
			DbConnect.closeStatement(pst);
		}
		return val;
	}
	
	public ArrayList<TripDetailsDto> getTripDetailsTrip(TripDetailsDto paramDto) {
		// TODO Auto-generated method stub
		DbConnect ob = DbConnect.getInstance();
		Connection con = ob.connectDB();
 
		PreparedStatement pst = null;
		ResultSet rs = null;
		String query = " select at.*,e.displayname,vt.type, at.vendorId from adhoctrips at,employee e,vehicle_type vt where at.employeeId=e.id and vt.id=at.vehicleType ";
		String tripDateQuery="";
		String bookingIdQuery="";
		String statusQuery="";
		String vendorQuery="";
		ArrayList<TripDetailsDto> dtoList= new ArrayList<TripDetailsDto>();
		
		
		try {
			
			if(OtherFunctions.isEmpty( paramDto.getTrip_date())==false) {
				tripDateQuery = " and at.travelDate='" + OtherFunctions.changeDateFromatToIso(paramDto.getTrip_date()) + "'";
			}
			if(OtherFunctions.isEmpty( paramDto.getScheduleId())==false) {
				bookingIdQuery = " and at.bookingId='" + paramDto.getScheduleId() + "'";
			}
			if(OtherFunctions.isEmpty( paramDto.getStatus())==false) {
				statusQuery = " and at.status='" + paramDto.getStatus() + "'";
			}
			if(OtherFunctions.isEmpty( paramDto.getVendorId())==false) {
				statusQuery = " and at.vendorId='" + paramDto.getVendorId() + "'";
			}
			
			if(OtherFunctions.isEmpty( paramDto.getSiteId())==false) {
				statusQuery = " and at.siteId='" + paramDto.getSiteId() + "'";
			}
			
			query = query + tripDateQuery + bookingIdQuery + statusQuery + vendorQuery;
			System.out.println(query);
			pst= con.prepareStatement(query);
			rs = pst.executeQuery();
			while (rs.next()) {
				TripDetailsDto dto = null;
				dto = new TripDetailsDto();
				dto.setId(rs.getString("id"));
				//dto.setTripBasedDistance(rs.getString("enteredDistance"));
				dto.setVehicleNo(rs.getString("vehicleNo" ));
				dto.setVendorId(  rs.getString("vendorId" ));
				dto.setTripType(rs.getString("adhocType"));
				dto.setTrip_date(rs.getString("travelDate"));
				dto.setTrip_code(rs.getString("tripCode"));
				dto.setSiteId(rs.getString("siteId"));
				dto.setVehicle_type(rs.getString("type"));
				dto.setVehicleTypeId(rs.getString("vehicleType"));
				dto.setDistance(rs.getString("enteredDistance"));
				dto.setDistanceCovered(rs.getFloat("actualDistance"));
				dto.setTravelTime(rs.getString("travelTime"));
				dto.setStartTime(rs.getString("startTime"));
				dto.setTrip_time(dto.getStartTime());// for bill
				dto.setTrip_log("IN");//FOR BILL				
				dto.setStopTime(rs.getString("endTime"));
				dto.setStartPlace(rs.getString("startPlace"));
				dto.setEndPlace(rs.getString("endPlace"));
				dto.setStatus(rs.getString("status"));
				dto.setIsSecurity(rs.getString("isEscort"));
				dto.setEscort(rs.getString("escortApprove"));
				dto.setScheduleId(rs.getString("bookingId"));
				dto.setLabel(dto.getId() + " " + dto.getStartPlace() + " "+ dto.getEndPlace());
				ArrayList<TripDetailsChildDto> subList = new ArrayList<TripDetailsChildDto>();
				TripDetailsChildDto subdto = new TripDetailsChildDto();
				subdto.setEmployeeId(rs.getString("employeeId"));
				subdto.setEmployeeName(rs.getString("displayname"));
				subList.add(subdto);
				dto.setTripDetailsChildDtoList(subList);
				
				dtoList.add(dto);
				 
			}

		} catch (Exception e) {
			System.out.println("Error in geting adhoc trip " + e);
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);

		}
		return dtoList;
 	}

 
	public ArrayList<VendorDto> getMasterVendorList(ArrayList<TripDetailsDto> trips) {


		ArrayList<VendorDto> dtoList = new ArrayList<VendorDto>();
		String query = "";
		DbConnect ob = DbConnect.getInstance();
		String tripIds="-1";
		if(trips!=null && trips.size()>0) {
			for(TripDetailsDto tripDto: trips) {
				tripIds+=", " + tripDto.getVendorId();
			}
		}
		query = "select vm.id, vm.company, vm.address, ifnull(vm.contact,'') contact, ifnull(vm.contact1,'') contact1, ifnull(vm.email,'') email from vendormaster vm where vm.status='active' and vm.id not in("+tripIds+")";

		PreparedStatement stmt = null;
		ResultSet rs = null;

		Connection con = ob.connectDB();
		try {
			stmt = con.prepareStatement(query);
			rs = stmt.executeQuery();
			while (rs.next()) {
				VendorDto dto = new VendorDto();
				dto.setCompanyId(rs.getString("id"));
				dto.setCompany(rs.getString("company"));
				dto.setCompanyAddress(rs.getString("address"));
				dto.setCompanycontact(rs.getString("contact"));
				dto.setCompanycontact1(rs.getString("contact1"));
				dto.setEmail(rs.getString("email"));
				dtoList.add(dto);
			}

		} catch (Exception e) {
			System.out.println("Excepion (getVendorList) : " + e);
		} finally {

			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(stmt);
			DbConnect.closeConnection(con);

		}
		return dtoList;

	}


	public ArrayList<VendorDto> getAssignedMasterVendorList(ArrayList<TripDetailsDto> trips) {


		ArrayList<VendorDto> dtoList = new ArrayList<VendorDto>();
		String query = "";
		DbConnect ob = DbConnect.getInstance();
		String tripIds="-1";
		if(trips!=null && trips.size()>0) {
			for(TripDetailsDto tripDto: trips) {
				tripIds+=", " + tripDto.getVendorId();
			}
		}
		query = "select vm.id, vm.company, vm.address, ifnull(vm.contact,'') contact, ifnull(vm.contact1,'') contact1, ifnull(vm.email,'') email from vendormaster vm where vm.status='active' and vm.id in("+tripIds+")";

		PreparedStatement stmt = null;
		ResultSet rs = null;

		Connection con = ob.connectDB();
		try {
			stmt = con.prepareStatement(query);
			rs = stmt.executeQuery();
			while (rs.next()) {
				VendorDto dto = new VendorDto();
				dto.setCompanyId(rs.getString("id"));
				dto.setCompany(rs.getString("company"));
				dto.setCompanyAddress(rs.getString("address"));
				dto.setCompanycontact(rs.getString("contact"));
				dto.setCompanycontact1(rs.getString("contact1"));
				dto.setEmail(rs.getString("email"));
				dtoList.add(dto);
			}

		} catch (Exception e) {
			System.out.println("Excepion (getVendorList) : " + e);
		} finally {

			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(stmt);
			DbConnect.closeConnection(con);

		}
		return dtoList;

	}

	public int assignVendor(ArrayList<AdhocTripHBMDto> hdtoList) {
		// TODO Auto-generated method stub
		int returnInt=0;
		  SessionFactory sessionFactory= HibernateUtil.getSessionFactory();
			 
			Session session = sessionFactory.openSession();
			 
			session.getTransaction().begin();
			try{
				for(AdhocTripHBMDto dto : hdtoList) {
					session.update(dto);
					returnInt++;
					System.out.println(" . "+returnInt);
				}
				session.getTransaction().commit();
				
			}catch(Exception e) {
				session.getTransaction().rollback();
				System.out.println("Error in assignVendor (adhoc) " + e);
				returnInt=0;
			}
			
			finally {
				session.close();
			}
			
		return returnInt;
	}


}
