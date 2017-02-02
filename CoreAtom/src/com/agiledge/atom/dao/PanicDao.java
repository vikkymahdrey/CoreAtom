package com.agiledge.atom.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.agiledge.atom.commons.OtherFunctions;
import com.agiledge.atom.constants.SettingsConstant;
import com.agiledge.atom.dbConnect.DbConnect;
import com.agiledge.atom.dto.PanicDto;
import com.agiledge.atom.dto.TripDetailsChildDto;
import com.agiledge.atom.dto.TripDetailsDto;
import com.agiledge.atom.dto.VehicleBasedTripDto;

public class PanicDao {
	public int TakePanicAction(PanicDto panicDto) {
		DbConnect ob = DbConnect.getInstance();
		Connection con = ob.connectDB();
		PreparedStatement pst = null;
		int retVal = 0;
		try {
			Statement st=con.createStatement();
			ResultSet rs=st.executeQuery("select * from panicaction where tripid="+ panicDto.getTripId());
			if(!rs.next())
			{
			pst = con
					.prepareStatement("insert into panicaction(tripId,alarmCause,primaryAction,primaryActiontakenBy,curStatus,activatedBy) values (?,?,?,?,?,?) ");

			pst.setString(1, panicDto.getTripId());
			pst.setString(2, panicDto.getAlarmCause());
			pst.setString(3, panicDto.getPrimaryAction());
			pst.setString(4, panicDto.getPrimaryActiontakenBy());
			pst.setString(5, "open");
			pst.setString(6, panicDto.getActivatedby());

			retVal = pst.executeUpdate();
			}
			else
			{
				pst = con
						.prepareStatement("update panicaction set alarmCause=?,primaryAction=?,primaryActiontakenBy=?,curStatus=? where tripid=?");
				
				pst.setString(1, panicDto.getAlarmCause());
				pst.setString(2, panicDto.getPrimaryAction());
				pst.setString(3, panicDto.getPrimaryActiontakenBy());
				pst.setString(4, "open");
				pst.setString(5, panicDto.getTripId());
				retVal = pst.executeUpdate();	
			}
		} catch (Exception e) {
			System.out.println("ERROR" + e);
		} finally {
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);
		}
		return retVal;
	}

	public int PanicStopAction(String panicId, String tripId, String empId) {
		DbConnect ob = DbConnect.getInstance();
		Connection con = ob.connectDB();
		PreparedStatement pst = null;
		int retVal = 0;
		try {
			
				pst = con
					.prepareStatement("update panicaction set curStatus='closed',approvedBy="
							+ empId + " , stopTime = now() where id=" + panicId + "");
			pst.executeUpdate();
			retVal += pst.executeUpdate();

			pst = con
					.prepareStatement("update vehicle_position set logstatus='run' where tripId="
							+ tripId + " order by  id desc limit 1");			
			retVal += pst.executeUpdate();
			if(SettingsConstant.comp.equalsIgnoreCase("keo")){
				SettingsConstant.deviceMap.put(tripId, "run");
			}
		} catch (Exception e) {
			System.out.println("ERROR" + e);
		} finally {
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);
		}
		return retVal;
	}

	public int StopSound(String tripId, String empId) {
		System.out.println("reached here");
		DbConnect ob = DbConnect.getInstance();
		Connection con = ob.connectDB();
		PreparedStatement pst = null;
		int retVal = 0;
		try {
			Statement st=con.createStatement();
			ResultSet rs=st.executeQuery("select * from panicaction where tripid="+tripId);
			if(!rs.next())
			{
			pst = con
					.prepareStatement("insert into panicaction(tripId,acknowledgeOn,acknowledgeBy,curStatus) values (?,concat(curdate(),'  ',curtime()),?,?)");
			pst.setString(1, tripId);
			pst.setString(2, empId);
			pst.setString(3, "open");
			pst.executeUpdate();
			}
		} catch (Exception e) {
			System.out.println("ERROR" + e);
		} finally {
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);
		}
		return retVal;
	}
	public ArrayList<PanicDto> GetPanicTrips(String site,String fromDate,String toDate) {
		DbConnect ob = DbConnect.getInstance();
		Connection con =null;
		Statement st=null;
		PreparedStatement pst = null,pst1 = null;
		ResultSet rs=null,rs1=null,rs2=null;
		ArrayList<PanicDto> list=null;
		try {
			
			list=new ArrayList<PanicDto>();
			con= ob.connectDB();;
			st=con.createStatement();
			String dateQuery="";
			
			
			if((fromDate!="")&&(toDate!="")){
				
				fromDate= OtherFunctions.changeDateFromatToIso(fromDate);
				toDate=OtherFunctions.changeDateFromatToIso(toDate);

				fromDate=fromDate +" 00:00:00";
				toDate=toDate+" 23:59:59";
				dateQuery= " where pa.actiontime between '"+fromDate+"' and '"+toDate+"'";
			}
		
			String PanicQuery=" select pa.tripid,pa.activatedBy,date_format(pa.actiontime,'%d/%m/%Y %k:%i:%s') as actiontime,pa.curstatus,e.displayName as primaryActiontakenByName,pa.alarmCause,pa.primaryAction,e1.displayName as approvedBy,pa.stopTime from panicAction pa left outer join employee e on pa.primaryActiontakenBy=e.id   left outer join employee e1 on  pa.approvedBy=e1.id    "+dateQuery;
		
			System.out.println("PanicQuery"+PanicQuery);
			rs=st.executeQuery(PanicQuery );
		    
			
			while (rs.next()) {
				PanicDto dto=new PanicDto();
				String activatedBy=rs.getString("activatedBy");
				
				if(activatedBy.equalsIgnoreCase("DRIVER APP"))
				{ 
					
					//Activated in driver app  i.e	activatedBy="DRIVER APP"
				}
				else
				{	
					//Activated in emp app ,getting emp name
				
					String EmpQuery="select displayname from employee where id = '"+activatedBy+"'";
					pst1=con.prepareStatement(EmpQuery);
					
					rs2=pst1.executeQuery();
					 if(rs2.next())
					 {
						 activatedBy=rs2.getString("displayname");
						
					 }
		
					
				}
				
				String alarmCause=rs.getString("alarmCause") == null ? " ":rs.getString("alarmCause") ;
				String	primaryActiontakenByName=rs.getString("primaryActiontakenByName")  == null ? " ":rs.getString("primaryActiontakenByName") ;
				String	primaryAction=rs.getString("primaryAction")  == null ? " ":rs.getString("primaryAction") ;
				String approvedBy=rs.getString("approvedBy")  == null ? " ":rs.getString("approvedBy") ;
				
				
				
				dto.setTripId(rs.getString("tripid"));
				dto.setActivatedby(activatedBy);
				dto.setActiontime(rs.getString("actiontime"));
				dto.setCurStatus(rs.getString("curstatus"));
				dto.setAlarmCause(alarmCause);
				dto.setPrimaryActiontakenByName(primaryActiontakenByName);	
				dto.setPrimaryAction(primaryAction);	
				dto.setApprovedBy(approvedBy);
				pst=con.prepareStatement("select v.regno from vehicles v,trip_details t where v.id=t.vehicle and t.id= "+rs.getString("tripid"));
				 
				 rs1=pst.executeQuery();
				 if(rs1.next())
				 {
					 dto.setVehicle(rs1.getString("regno"));
				 }
			
				 
			    list.add(dto);
			}
			
			
		} catch (Exception e) {
			System.out.println("ERROR" + e);
			e.printStackTrace();
		} finally {
			DbConnect.closeStatement(pst,st);
			DbConnect.closeConnection(con);
			DbConnect.closeResultSet(rs,rs1);
		}
		return list;
	
	}
	public ArrayList<TripDetailsDto> getPanicTripDetails() {
		DbConnect ob = DbConnect.getInstance();
		PreparedStatement pst = null;
		PreparedStatement pst1 = null;
		ResultSet rs = null;
		ResultSet rs1 = null;
		ArrayList<TripDetailsDto> tripDetailsDtos = new ArrayList<TripDetailsDto>();
		TripDetailsDto tripDto = new TripDetailsDto();
		TripDetailsChildDto childDto = null;
		Connection con = ob.connectDB();
		ArrayList<TripDetailsChildDto> childDtos = null;
		try {
			pst = con
					.prepareStatement("select td.id tripId,d.name driverName,d.contact driverContact,vm.Company vendorName,vm.contact as "
							+ "  vendorContact,veh.regNo,e.name "
							+ " escortName,e.escortClock,e.phone escortContact, (select date_format(date_time,'%d/%m/%Y %k:%i:%s') as date_time from vehicle_position vp where "
							+ " vp.tripId=td.id and vp.logstatus='danger' order by date_time desc limit 1) as date_time  "
							+ " from trip_details td,driver d,vendormaster vm,vehicles veh, "
							+ " vendor_trip_sheet_parent vtsp "
							+ " left outer join escort e on vtsp.escortClock  where td.id in (select tripId from panicaction where curStatus='open')"
							+ " and "
							+ " td.id=vtsp.tripid and td.vehicle=veh.id and td.driverId=d.id and veh.vendor=vm.id and vm.id=d.vendorId ");
			rs = pst.executeQuery();
			while (rs.next()) {
				tripDto = new TripDetailsDto();
				tripDto.setId(rs.getString("tripId"));
				tripDto.setDriverName(rs.getString("driverName"));
				tripDto.setDriverContact(rs.getString("driverContact"));
				tripDto.setVendorName(rs.getString("vendorName"));
				tripDto.setVendorContact(rs.getString("vendorContact"));
				tripDto.setVehicleNo(rs.getString("regNo"));
				tripDto.setEscortName(rs.getString("escortName"));
				tripDto.setEscortclock(rs.getString("escortClock"));
				tripDto.setEscortContact(rs.getString("escortContact"));
				tripDto.setTrip_time(rs.getString("date_time"));

				pst1 = con
						.prepareStatement("select e.id,e.PersonnelNo,e.gender,e.employeeFirstName,e.employeeLastName,e.displayName,e.contactNumber1,e.contactNumber2 from vendor_trip_sheet vts,employee e where vts.tripId ="
								+ tripDto.getId()
								+ " and curStatus='IN' and vts.employeeId=e.id");
				rs1 = pst1.executeQuery();
				childDtos = new ArrayList<TripDetailsChildDto>();
				while (rs1.next()) {
					childDto = new TripDetailsChildDto();
					childDto.setEmployeeName(rs1.getString("displayName"));
					childDto.setEmployeeId(rs1.getString("PersonnelNo"));
					childDto.setContactNumber(rs1.getString("contactNumber1"));
					childDto.setGender(rs1.getString("gender"));
					childDtos.add(childDto);
				}
				tripDto.setTripDetailsChildDtoList(childDtos);
				pst1 = con
						.prepareStatement("select pa.id,e.displayName,pa.actionTime,pa.alarmCause,pa.primaryAction from panicAction pa   left outer join employee e on pa.primaryActiontakenBy=e.id where  pa.tripId="
								+ tripDto.getId() + "");
				// System.out.println("select pa.id,e.displayName,pa.actionTime,pa.alarmCause,pa.primaryAction from panicAction pa,employee e where pa.primaryActiontakenBy=e.id AND pa.tripId="
				// + tripDto.getId() + "");
				rs1 = pst1.executeQuery();
				PanicDto dto = null;
				
				if (rs1.next()) {
					dto = new PanicDto();
					String displayName=rs1.getString("displayName")==null ? "":rs1.getString("displayName");
					String alarmCause=rs1.getString("alarmCause")==null ? "":rs1.getString("alarmCause");
					String primaryAction=rs1.getString("primaryAction")==null ? "NO ACTION TAKEN":rs1.getString("primaryAction");
					
					
					dto.setId(rs1.getString("id"));
					dto.setPrimaryActiontakenTime(rs1.getString("actionTime"));
					dto.setAlarmCause(alarmCause);
					dto.setPrimaryAction(primaryAction);
					dto.setPrimaryActiontakenByName(displayName);
					
				}
				tripDto.setPanicdto(dto);
				tripDetailsDtos.add(tripDto);
			}

		} catch (Exception e) {
			System.out.println("Error here" + e);
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeResultSet(rs1);
			DbConnect.closeStatement(pst);
			DbConnect.closeStatement(pst1);
			DbConnect.closeConnection(con);
		}
		return tripDetailsDtos;

	}
	
	public TripDetailsDto getPanicTripDetails(String tripid) {
		DbConnect ob = DbConnect.getInstance();
		PreparedStatement pst = null;
		PreparedStatement pst1 = null;
		ResultSet rs = null;
		ResultSet rs1 = null;
		TripDetailsDto tripDto = new TripDetailsDto();
		TripDetailsChildDto childDto = null;
		Connection con = ob.connectDB();
		ArrayList<TripDetailsChildDto> childDtos = null;
		try {
			pst = con
					.prepareStatement("select td.id tripId,d.name driverName,d.contact driverContact,vm.Company vendorName,vm.contact as "
							+ "  vendorContact,veh.regNo,e.name "
							+ " escortName,e.escortClock,e.phone escortContact, (select date_format(date_time,'%d/%m/%Y %k:%i:%s') as date_time from vehicle_position vp where "
							+ " vp.tripId=td.id and vp.logstatus='danger' order by date_time desc limit 1) as date_time  "
							+ " from trip_details td,driver d,vendormaster vm,vehicles veh, "
							+ " vendor_trip_sheet_parent vtsp "
							+ " left outer join escort e on vtsp.escortClock  where td.id ="+tripid
							+ " and "
							+ " td.id=vtsp.tripid and td.vehicle=veh.id and td.driverId=d.id and veh.vendor=vm.id and vm.id=d.vendorId ");
			rs = pst.executeQuery();
		
			
			while (rs.next()) {
				String escortname=rs.getString("escortName")==null ? "":rs.getString("escortName");
				String escortClock=rs.getString("escortClock")==null ? "":rs.getString("escortClock");
				String escortContact=rs.getString("escortContact")==null ? "":rs.getString("escortContact");
				tripDto = new TripDetailsDto();
				tripDto.setId(rs.getString("tripId"));
				tripDto.setDriverName(rs.getString("driverName"));
				tripDto.setDriverContact(rs.getString("driverContact"));
				tripDto.setVendorName(rs.getString("vendorName"));
				tripDto.setVendorContact(rs.getString("vendorContact"));
				tripDto.setVehicleNo(rs.getString("regNo"));
				tripDto.setEscortName(escortname);
				tripDto.setEscortclock(escortClock);
				tripDto.setEscortContact(escortContact);
				tripDto.setTrip_time(rs.getString("date_time"));

				pst1 = con
						.prepareStatement("select e.id,e.PersonnelNo,e.gender,e.employeeFirstName,e.employeeLastName,e.displayName,e.contactNumber1,e.contactNumber2 from vendor_trip_sheet vts,employee e where vts.tripId ="
								+ tripDto.getId()
								+ " and curStatus='IN' and vts.employeeId=e.id");
				rs1 = pst1.executeQuery();
				childDtos = new ArrayList<TripDetailsChildDto>();
				while (rs1.next()) {
					childDto = new TripDetailsChildDto();
					childDto.setEmployeeName(rs1.getString("displayName"));
					childDto.setEmployeeId(rs1.getString("PersonnelNo"));
					childDto.setContactNumber(rs1.getString("contactNumber1"));
					childDto.setGender(rs1.getString("gender"));
					childDtos.add(childDto);
				}
				tripDto.setTripDetailsChildDtoList(childDtos);
				pst1 = con
						.prepareStatement("select pa.id,e.displayName,pa.actionTime,pa.alarmCause,pa.primaryAction from panicAction pa   left outer join employee e on pa.primaryActiontakenBy=e.id where  pa.tripId="
								+ tripDto.getId() + "");
				// System.out.println("select pa.id,e.displayName,pa.actionTime,pa.alarmCause,pa.primaryAction from panicAction pa,employee e where pa.primaryActiontakenBy=e.id AND pa.tripId="
				// + tripDto.getId() + "");
				rs1 = pst1.executeQuery();
				PanicDto dto = null;
				
				if (rs1.next()) {
					dto = new PanicDto();
					String displayName=rs1.getString("displayName")==null ? "":rs1.getString("displayName");
					String alarmCause=rs1.getString("alarmCause")==null ? "":rs1.getString("alarmCause");
					String primaryAction=rs1.getString("primaryAction")==null ? "NO ACTION TAKEN":rs1.getString("primaryAction");
					
					
					dto.setId(rs1.getString("id"));
					dto.setPrimaryActiontakenTime(rs1.getString("actionTime"));
					dto.setAlarmCause(alarmCause);
					dto.setPrimaryAction(primaryAction);
					dto.setPrimaryActiontakenByName(displayName);
					
				}
				tripDto.setPanicdto(dto);
				
			}

		} catch (Exception e) {
			System.out.println("Error here" + e);
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeResultSet(rs1);
			DbConnect.closeStatement(pst);
			DbConnect.closeStatement(pst1);
			DbConnect.closeConnection(con);
		}
		return tripDto;

	}
	public int checkprimaryaction(String tripId) {
		
		DbConnect ob = DbConnect.getInstance();
		Connection con = ob.connectDB();
		PreparedStatement pst = null;
		int retVal = 0;
		try {
			Statement st=con.createStatement();
			ResultSet rs=st.executeQuery("select * from panicaction where tripid="+tripId+" and length(primaryActiontakenBy)<1");
			if(!rs.next())
			{
				retVal=1;
			}
		} catch (Exception e) {
			System.out.println("ERROR" + e);
		} finally {
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);
		}
		return retVal;
	}
	
	
	
	
	
	

}