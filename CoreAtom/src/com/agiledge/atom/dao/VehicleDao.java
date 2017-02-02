package com.agiledge.atom.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import com.agiledge.atom.commons.OtherFunctions;
import com.agiledge.atom.dbConnect.DbConnect;
import com.agiledge.atom.dto.DriverDto;
import com.agiledge.atom.dto.DriverVehicleDto;
import com.agiledge.atom.dto.VehicleDto;

public class VehicleDao {
	public int addVehicle(VehicleDto vehicleDto) {
		DbConnect ob = DbConnect.getInstance();
		PreparedStatement pst = null;
		Connection con = ob.connectDB();
		int retVal = 0;
		try {
			pst = con
					.prepareStatement("insert into vehicles(regNo,vehicletype,vendor) values (?,?,?)");
			pst.setString(1, vehicleDto.getVehicleNo());
			pst.setString(2, vehicleDto.getVehicleType());
			pst.setString(3, vehicleDto.getVendor());
			retVal = pst.executeUpdate();

		} catch (Exception e) {

		} finally {
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);
		}
		return retVal;
	}

	public ArrayList<VehicleDto> getAllVehicle() {
		System.out.println("here");
		DbConnect ob = DbConnect.getInstance();
		PreparedStatement pst = null;
		ResultSet rs = null;
		Connection con = ob.connectDB();
		ArrayList<VehicleDto> vehicleDtos = new ArrayList<VehicleDto>();
		VehicleDto vehicleDto = null;
		try {
			pst = con
					.prepareStatement("select v.id,v.regno,vt.type,v.status from vehicles v join vehicle_type vt on vt.id=v.vehicletype");
			rs = pst.executeQuery();
			while (rs.next()) {
				vehicleDto = new VehicleDto();
				vehicleDto.setId(rs.getString("id"));
				vehicleDto.setVehicleNo(rs.getString("regNo"));
				vehicleDto.setVehicleType(rs.getString("type"));
				vehicleDto.setStatus(rs.getString("status"));
				vehicleDtos.add(vehicleDto);
			}

		} catch (Exception e) {

		} finally {
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);
		}
		return vehicleDtos;

	}

	public ArrayList<VehicleDto> getAllAvailableVehicle() {
		DbConnect ob = DbConnect.getInstance();
		PreparedStatement pst = null;
		ResultSet rs = null;
		Connection con = ob.connectDB();
		ArrayList<VehicleDto> vehicleDtos = new ArrayList<VehicleDto>();
		VehicleDto vehicleDto = null;
		try {
			
			pst = con
					.prepareStatement("select v.*,vt.type,vt.seat,vt.sit_cap from vehicles v,vehicle_type vt where v.vehicletype=vt.id and v.id in (select vehicleId from vehicle_status where status='in') order by vt.sit_cap");
			rs = pst.executeQuery();
			if (rs.next()) {
				System.out.println("IN IFFFs");
				do {
					vehicleDto = new VehicleDto();
					vehicleDto.setId(rs.getString("id"));
					vehicleDto.setVehicleNo(rs.getString("regNo"));
					vehicleDto.setVehicleTypeId(rs.getString("vehicletype"));
					vehicleDto.setVehicleType(rs.getString("type"));
					vehicleDto.setSeat(rs.getInt("seat"));
					vehicleDto.setSit_cap(rs.getInt("sit_cap"));
					vehicleDtos.add(vehicleDto);
				} while (rs.next());
			} else {
				System.out.println("IN else");
				pst = con
						.prepareStatement("select v.*,vt.type,vt.seat,vt.sit_cap from vehicles v,vehicle_type vt where v.vehicletype=vt.id and v.id  order by vt.sit_cap");
				rs = pst.executeQuery();
				if (rs.next()) {
					vehicleDto = new VehicleDto();
					vehicleDto.setId(rs.getString("id"));
					vehicleDto.setVehicleNo(rs.getString("regNo"));
					vehicleDto.setVehicleTypeId(rs.getString("vehicletype"));
					vehicleDto.setVehicleType(rs.getString("type"));
					vehicleDto.setSeat(rs.getInt("seat"));
					vehicleDto.setSit_cap(rs.getInt("sit_cap"));
					vehicleDtos.add(vehicleDto);
				}
			}

		} catch (Exception e) {
			System.out.println("errro in getting vehicle" + e);
		} finally {
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);
		}
		return vehicleDtos;
	}

	public void updateAllVehicleStatus() {
		DbConnect ob = DbConnect.getInstance();
		PreparedStatement pst = null;
		Connection con = ob.connectDB();
		System.out.println("IN Update");
		try {
			pst = con
					.prepareStatement("update vehicle_status set status='out'");

			pst.executeUpdate();
			pst = con
					.prepareStatement("insert into vehicle_status (vehicleId,loggedIn,status) select vehicles.id,now(),'in' from vehicles");
			pst.executeUpdate();

		} catch (Exception e) {

		} finally {
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);
		}
	}

	public ArrayList<VehicleDto> getTripVehicle(String tripDate) {
		DbConnect ob = DbConnect.getInstance();
		PreparedStatement pst = null;
		ResultSet rs = null;
		Connection con = ob.connectDB();
		ArrayList<VehicleDto> vehicleDtos = new ArrayList<VehicleDto>();
		VehicleDto vehicleDto = null;
		try {
			pst = con
					.prepareStatement("select * from vehicles where id in (select td.vehicle from trip_details td,vendor_trip_sheet_parent vtp where td.trip_date=? and  td.id=vtp.tripid)");
			System.out.println("select * from vehicles where id in (select td.vehicle from trip_details td,vendor_trip_sheet_parent vtp where td.trip_date=? and  td.id=vtp.tripid)");
			pst.setString(1, OtherFunctions.changeDateFromatToIso(tripDate));
			rs = pst.executeQuery();
			while (rs.next()) {
				vehicleDto = new VehicleDto();
				vehicleDto.setId(rs.getString("id"));
				vehicleDto.setVehicleNo(rs.getString("regNo"));
				vehicleDto.setVehicleType(rs.getString("vehicleType"));
				vehicleDtos.add(vehicleDto);
			}

		} catch (Exception e) {
			System.out.println("error" + e);
		} finally {
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);
		}
		return vehicleDtos;

	}

	public ArrayList<VehicleDto> vehicleTrack(String tripId) {
		DbConnect ob = DbConnect.getInstance();
		PreparedStatement pst = null;
		ResultSet rs = null;
		VehicleDto vehicleDto = null;
		Connection con = ob.connectDB();
		ArrayList<VehicleDto> vehicleDtos = new ArrayList<VehicleDto>();
		try {
			pst = con
					.prepareStatement("select vp.*,v.regNo from vehicles v,vehicle_position vp where vp.tripId="
							+ tripId
							+ " and vp.vehicleId=v.id order by vp.date_time");
			rs = pst.executeQuery();
			while (rs.next()) {
				vehicleDto = new VehicleDto();
				vehicleDto.setId(rs.getString("vehicleid"));
				vehicleDto.setVehicleNo(rs.getString("regNo"));
				vehicleDto.setLattitude(rs.getString("lattitude"));
				vehicleDto.setLongitude(rs.getString("longitude"));
				vehicleDto.setStatus(rs.getString("logstatus"));
				vehicleDtos.add(vehicleDto);

			}
		} catch (Exception e) {
			System.out.println("Error" + e);
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);
		}
		return vehicleDtos;
	}

	public VehicleDto getPanicAlarmVehicle(String imei) {
		DbConnect ob = null;
		Connection con = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		VehicleDto vehicleDto = new VehicleDto();
		try {

			ob = DbConnect.getInstance();
			con = ob.connectDB();
			System.out
					.println(" select vp.vehicleId,v.regNo,vp.tripId from vehicle_position vp,vehicles v where vp.vehicleId=(select vehicleId from driver_logins dl where dl.imei_number='"
							+ imei
							+ "' and dl.status='logged') and vp.vehicleId=v.id order by date_time desc limit 1");
			pst = con
					.prepareStatement("select vp.vehicleId,v.regNo,vp.tripId from vehicle_position vp,vehicles v where vp.vehicleId=(select vehicleId from driver_logins dl where dl.imei_number='"
							+ imei
							+ "' and dl.status='logged') and vp.vehicleId=v.id order by date_time desc limit 1");
			rs = pst.executeQuery();
			if (rs.next()) {
				vehicleDto.setId(rs.getString("vehicleId"));
				System.out.println("query veh" + rs.getString("regNo"));
				vehicleDto.setVehicleNo(rs.getString("regNo"));
				vehicleDto.setTripId(rs.getString("tripId"));
			}

			System.out.println("Dto Veh" + vehicleDto.getVehicleNo());
		} catch (Exception e) {
			System.out.println("ërror" + e);
		} finally {
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);
		}
		return vehicleDto;

	}

	public ArrayList<DriverVehicleDto> getVehicle(String vehicleId) {
		DbConnect ob = DbConnect.getInstance();
		PreparedStatement pst = null;
		ResultSet rs = null;
		DriverVehicleDto driverVehicleDto = null;
		Connection con = ob.connectDB();
		ArrayList<DriverVehicleDto> drivervehicleDtos = new ArrayList<DriverVehicleDto>();
		VehicleDto vehicleDto = null;
		DriverDto driverDto = null;
		try {
			pst = con
					.prepareStatement("select d.id,d.name,d.address,d.contact,v.regNo,vt.type,vm.company from driver d,vehicles v,driver_vehicle dv,vehicle_type vt,vendormaster vm where v.id="
							+ vehicleId
							+ " and d.id=dv.driverId and v.id=dv.vehicleid and v.vehicletype=vt.id and d.vendorId=vm.id ");
			/*System.out
					.println("select d.id,d.name,d.address,d.contact,v.regNo,vt.type,vm.company from driver d,vehicles v,driver_vehicle dv,vehicle_type vt,vendormaster vm where v.id="
							+ vehicleId
							+ " and d.id=dv.driverId and v.id=dv.vehicleid and v.vehicletype=vt.id and d.vendorId=vm.id");
							*/
			rs = pst.executeQuery();
			while (rs.next()) {

				driverVehicleDto = new DriverVehicleDto();
				driverDto = new DriverDto();
				vehicleDto = new VehicleDto();
				driverDto.setDriverId(rs.getInt("id"));
				driverDto.setDriverName(rs.getString("name"));
				driverDto.setAddress1(rs.getString("address"));
				driverDto.setVendor(rs.getString("company"));
				driverDto.setContactNo(rs.getString("contact"));
				vehicleDto.setVehicleNo(rs.getString("regNo"));
				vehicleDto.setVehicleType(rs.getString("type"));
				driverVehicleDto.setVehicleDto(vehicleDto);
				driverVehicleDto.setDriverDto(driverDto);
				drivervehicleDtos.add(driverVehicleDto);
			}
		} catch (Exception e) {
			System.out.println("Error" + e);
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);
		}
		return drivervehicleDtos;
	}

	public int addDriverVehicle(String vehicle, String[] vehicleDrivers) {
		DbConnect ob = DbConnect.getInstance();
		PreparedStatement pst = null;
		Connection con = ob.connectDB();
		int retVal=0;
		
		try {
			pst = con
					.prepareStatement("delete from driver_vehicle where vehicleId="+vehicle+"");
			pst.executeUpdate();
			pst = con
					.prepareStatement("insert into driver_vehicle(driverId,vehicleId) values (?,?)");
			System.out.println("insert into driver_vehicle(driverId,vehicleId) values (?,?)");
			for (String driverId : vehicleDrivers) {
					pst.setString(1, driverId);
					pst.setString(2, vehicle);
					System.out.println(vehicle+""+driverId);

					retVal+=pst.executeUpdate();
			}

		} catch (Exception e) {
			System.out.println("error"+e);
		} finally {
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);
		}
		return retVal;
	}

	public ArrayList<VehicleDto> getVehicleTrackInInterval(String tripId) {
		DbConnect ob = DbConnect.getInstance();
		PreparedStatement pst = null;
		ResultSet rs = null;
		VehicleDto vehicleDto = null;
		Connection con = ob.connectDB();
		ArrayList<VehicleDto> vehicleDtos = new ArrayList<VehicleDto>();
		try {
			pst = con
					.prepareStatement("SELECT *,DATE_FORMAT(date_time,'%d-%m-%Y %H:%i') as dttime FROM vehicle_position WHERE  tripid="+tripId+" group by dttime");
			rs = pst.executeQuery();
			while (rs.next()) {
				vehicleDto = new VehicleDto();
				vehicleDto.setId(rs.getString("vehicleid"));
				vehicleDto.setDateTime(rs.getString("dttime"));
				vehicleDto.setLattitude(rs.getString("lattitude"));
				vehicleDto.setLongitude(rs.getString("longitude"));
				vehicleDtos.add(vehicleDto);

			}
		} catch (Exception e) {
			System.out.println("Error" + e);
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);
		}
		return vehicleDtos;
	}
	
public void cleanVehiclePosition()	
{
	DbConnect ob = DbConnect.getInstance();
	PreparedStatement pst = null;
	ResultSet rs = null;
	Connection con = ob.connectDB();
	try {
		pst = con.prepareStatement("truncate vehicle_position_temp");
		pst.executeUpdate();
		pst = con
				.prepareStatement("insert into vehicle_position_temp  (vehicleId,date_time,lattitude,longitude,logstatus,tripId,status)  select vehicleId,date_time,lattitude,longitude,logstatus,tripId,status  from    vehicle_position");
		pst.executeUpdate();
		pst = con.prepareStatement("truncate vehicle_position");
		pst.executeUpdate();
		try{
		pst = con.prepareStatement("insert into vehicle_position_old (vehicleId,date_time,lattitude,longitude,logstatus,tripId,status)  select vehicleId,date_time,lattitude,longitude,logstatus,tripId,status  from  vehicle_position_temp where date_time < date_add(curdate(),interval -10 day)");
		pst.executeUpdate();
		}catch(Exception e1)
		{
			System.out.println("error in putting to vehicle posiition old"+e1);
		}
		pst = con.prepareStatement(" delete from  vehicle_position_temp where date_time < date_add(curdate(),interval -10 day)");
		pst.executeUpdate();
		pst = con.prepareStatement("insert into vehicle_position (vehicleId,date_time,lattitude,longitude,logstatus,tripId,status)  select vehicleId,date_time,lattitude,longitude,logstatus,tripId,status  from  vehicle_position_temp");
		pst.executeUpdate();
		
	} catch (Exception e) {
		System.out.println("Error" + e);
	} finally {
		DbConnect.closeResultSet(rs);
		DbConnect.closeStatement(pst);
		DbConnect.closeConnection(con);
	}
}
	

public int UpdateVehicleStatus(String id,String status) {
	DbConnect ob = DbConnect.getInstance();
	PreparedStatement pst = null;
	Connection con = ob.connectDB();
	int retVal = 0;
	try {
		if(status.equalsIgnoreCase("a"))
		{
			status="c";
		}
		else
		{
			
			status="a";
		}
		pst = con
				.prepareStatement("UPDATE VEHICLES SET STATUS=? WHERE ID=?");
		pst.setString(1, status);
		pst.setString(2, id);
		retVal = pst.executeUpdate();

	} catch (Exception e) {

	} finally {
		DbConnect.closeStatement(pst);
		DbConnect.closeConnection(con);
	}
	return retVal;
}

public ArrayList<VehicleDto> getvehiclebyType(String type) {
	DbConnect ob = null;
	Connection con = null;
	PreparedStatement pst = null;
	ResultSet rs = null;
	 ArrayList<VehicleDto> list= new  ArrayList<VehicleDto>();
	try {

		ob = DbConnect.getInstance();
		con = ob.connectDB();
		String query="SELECT * FROM VEHICLES  WHERE VEHICLETYPE="+type;
		if(type==null||type.equalsIgnoreCase("All")||type.equalsIgnoreCase(""))
		{
			query="SELECT * FROM VEHICLES";
		}
		pst = con
				.prepareStatement(query);
		rs = pst.executeQuery();
		System.out.println(query);
		while (rs.next()) {

			VehicleDto vehicleDto = new VehicleDto();
			vehicleDto.setId(rs.getString("Id"));
			vehicleDto.setVehicleNo(rs.getString("regNo"));
			list.add(vehicleDto);
		}

	} catch (Exception e) {
		System.out.println("ërror" + e);
	} finally {
		DbConnect.closeStatement(pst);
		DbConnect.closeConnection(con);
	}
	return list;

}
public ArrayList<VehicleDto> getvehiclebyVendorType(String type,String vendor) {
	DbConnect ob = null;
	Connection con = null;
	PreparedStatement pst = null;
	ResultSet rs = null;
	 ArrayList<VehicleDto> list= new  ArrayList<VehicleDto>();
	try {

		ob = DbConnect.getInstance();
		con = ob.connectDB();
		String query="SELECT * FROM VEHICLES  WHERE VEHICLETYPE="+type+" AND VENDOR = "+vendor;
		/*if(type==null||type.equalsIgnoreCase("All")||type.equalsIgnoreCase(""))
		{
			query="SELECT * FROM VEHICLES";
		}*/
	

		pst = con.prepareStatement(query);
		rs = pst.executeQuery();
		System.out.println(query);
		while (rs.next()) {

			VehicleDto vehicleDto = new VehicleDto();
			vehicleDto.setId(rs.getString("Id"));
			vehicleDto.setVehicleNo(rs.getString("regNo"));
			list.add(vehicleDto);
		}

	} catch (Exception e) {
		System.out.println("error" + e);
	} finally {
		DbConnect.closeStatement(pst);
		DbConnect.closeConnection(con);
	}
	return list;

}

public int mapVehicleWithShift(String siteId, String vehicle, String[] inT,
		String[] outT, String routeIn, String routeOut) {
	DbConnect ob = null;
	Connection con = null;
	Statement st = null,st1 = null;
	int result=0;
	try {
		ob = DbConnect.getInstance();
		con = ob.connectDB();
		String insertqry1="INSERT INTO vehicle_shift_map (vehicleId,shiftId,routeId) VALUES ";
		String sub="",sub1="";
		if(inT!=null){
		st=con.createStatement();
		for (int i = 0; i < inT.length; i++) {
			sub+="('"+vehicle+"','"+inT[i]+"','"+routeIn+"')";
			if(!((i+1)==inT.length)){
				sub+=",";
			}
		}
		result=st.executeUpdate(insertqry1+sub);
		}
		if(outT!=null){
		st1=con.createStatement();
		for (int j = 0; j < outT.length; j++) {
			sub1+="('"+vehicle+"','"+outT[j]+"','"+routeOut+"')";
			if(!((j+1)==outT.length)){
				sub1+=",";
			}
		}
		result+=st1.executeUpdate(insertqry1+sub1);
		}
	} catch (Exception e) {
		System.out.println("Error in VehicleDao@mapVehicleWithShift" + e);
	} finally {
		DbConnect.closeStatement(st,st1);
		DbConnect.closeConnection(con);
	}

	return result;
}

public int addVehicleDetails(VehicleDto vehicleDto,ArrayList images) {
	DbConnect ob = DbConnect.getInstance();
	PreparedStatement pst = null,pst2=null;
	Statement st= null;
	ResultSet rs= null;
	Connection con = ob.connectDB();
	int retVal = 0;
	String sub1=null;
	if(images.size()==2){
		byte[] bt= new byte[1];
		images.add(bt);		}
	if(!vehicleDto.getPollutionUpto().equalsIgnoreCase("0000-00-00")){
		sub1=", pollutiondate='"+vehicleDto.getPollutionstart()+"', pollutionupto='"+vehicleDto.getPollutionUpto()+"' ";
	}
	try {
		st=con.createStatement();
		String selectqry="select id from vehicles where regNo='"+vehicleDto.getVehicleNo()+"'";
		rs=st.executeQuery(selectqry);
		if(rs.next()){
			String updateqry="UPDATE vehicles SET vehicletype=?,vendor=?,site=?, fcnumber=?, taxstart=?, taxend=?,insurancefrom=?, insuranceupto=?, fireexit=?, firstaid=?, cabcondition=?, remark=?,taximage=?,insuimage=?,pucimage=?,model=? WHERE id=?";
			pst2=con.prepareStatement(updateqry);
			pst2.setString(1, vehicleDto.getVehicleType());
			pst2.setString(2, vehicleDto.getVendor());
			pst2.setString(3, vehicleDto.getSite());
			pst2.setString(4, vehicleDto.getFcnumber());
			pst2.setString(5, vehicleDto.getTaxfromdate());
			pst2.setString(6, vehicleDto.getTaxtodate());
			pst2.setString(7, vehicleDto.getInsuranceStart());
			pst2.setString(8, vehicleDto.getInsuranceupto());
			
			pst2.setString(9, vehicleDto.getFireexit());
			pst2.setString(10, vehicleDto.getFirstaid());
			pst2.setString(11, vehicleDto.getCabcondition());
			pst2.setString(12, vehicleDto.getRemark());
			pst2.setBytes(13, (byte[]) images.get(0));
			pst2.setBytes(14, (byte[]) images.get(1));
			pst2.setBytes(15, (byte[]) images.get(2));
			pst2.setString(16, vehicleDto.getModel());
			pst2.setString(17,rs.getString("id"));
			retVal=pst2.executeUpdate();
		}else{
		pst = con
				.prepareStatement("INSERT INTO vehicles (regNo, vehicletype,vendor, site, fcnumber, taxstart, taxend, insurancefrom, insuranceupto, pollutiondate, pollutionupto, fireexit, firstaid, cabcondition, remark,taximage,insuimage,pucimage,model) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?,?,?)");
		pst.setString(1, vehicleDto.getVehicleNo());
		pst.setString(2, vehicleDto.getVehicleType());
		pst.setString(3, vehicleDto.getVendor());
		pst.setString(4, vehicleDto.getSite());
		pst.setString(5, vehicleDto.getFcnumber());
		pst.setString(6, vehicleDto.getTaxfromdate());
		pst.setString(7, vehicleDto.getTaxtodate());
		pst.setString(8, vehicleDto.getInsuranceStart());
		pst.setString(9, vehicleDto.getInsuranceupto());
		pst.setString(10, vehicleDto.getPollutionstart());
		pst.setString(11, vehicleDto.getPollutionUpto());
		pst.setString(12, vehicleDto.getFireexit());
		pst.setString(13, vehicleDto.getFirstaid());
		pst.setString(14, vehicleDto.getCabcondition());
		pst.setString(15, vehicleDto.getRemark());
		pst.setBytes(16, (byte[]) images.get(0));
		pst.setBytes(17, (byte[]) images.get(1));
		pst.setBytes(18, (byte[]) images.get(2));
		pst.setString(19, vehicleDto.getModel());
		retVal = pst.executeUpdate();
		}
	} catch (Exception e) {
		System.out.println("Error in VehicleDao@addVehicleDetails"+e);
	} finally {
		DbConnect.closeResultSet(rs);
		DbConnect.closeStatement(pst,pst2,st);
		DbConnect.closeConnection(con);
	}
	return retVal;
}
public ArrayList<VehicleDto> getAllVehicleDetails() {
	DbConnect ob = DbConnect.getInstance();
	PreparedStatement pst = null;
	ResultSet rs = null;
	Connection con = ob.connectDB();
	ArrayList<VehicleDto> vehicleDtos = new ArrayList<VehicleDto>();
	VehicleDto vehicleDto = null;
	try {
		pst = con
				.prepareStatement("select v.id,v.regNo,v.status,vt.type,v.fcnumber,v.site,v.taxstart,v.taxend,v.insurancefrom,v.insuranceupto,v.pollutiondate,v.pollutionupto,v.fireexit,v.firstaid,v.cabcondition,v.remark,v.model,vm.company as vendor from vehicles v,vehicle_type vt,vendormaster vm where v.vehicletype=vt.id and v.vendor=vm.id");
		rs = pst.executeQuery();
		while (rs.next()) {
			vehicleDto = new VehicleDto();
			vehicleDto.setId(rs.getString("id"));
			vehicleDto.setVehicleNo(rs.getString("regNo"));
			System.out.println(rs.getString("regNo"));
			vehicleDto.setVehicleType(rs.getString("type"));
			vehicleDto.setStatus(rs.getString("status"));
			String site=rs.getString("site")== null ? "" :rs.getString("site");
			String fcnumber=rs.getString("fcnumber")== null ? "" :rs.getString("fcnumber");
			String taxstart=rs.getString("taxstart")== null ? "" :rs.getString("taxstart");
			String taxend=rs.getString("taxend")== null ? "" :rs.getString("taxend");
			String insurancefrom=rs.getString("insurancefrom")== null ? "" :rs.getString("insurancefrom");
			String insuranceupto=rs.getString("insuranceupto")== null ? "" :rs.getString("insuranceupto");
			String pollutiondate=rs.getString("pollutiondate")== null ? "" :rs.getString("pollutiondate");
			String pollutionupto=rs.getString("pollutionupto")== null ? "" :rs.getString("pollutionupto");
			String fireexit=rs.getString("fireexit")== null ? "" :rs.getString("fireexit");
			String firstaid=rs.getString("firstaid")== null ? "" :rs.getString("firstaid");
			String cabcondition=rs.getString("cabcondition")== null ? "" :rs.getString("cabcondition");
			String remark=rs.getString("remark")== null ? "" :rs.getString("remark");
			String model=rs.getString("model")== null ? "" :rs.getString("model");
			vehicleDto.setSite(site);
			vehicleDto.setFcnumber(fcnumber);
			vehicleDto.setTaxfromdate(OtherFunctions.changeDateFromatToddmmyyyy(taxstart));
			vehicleDto.setTaxtodate(OtherFunctions.changeDateFromatToddmmyyyy(taxend));
			vehicleDto.setInsuranceStart(OtherFunctions.changeDateFromatToddmmyyyy(insurancefrom));
			vehicleDto.setInsuranceupto(OtherFunctions.changeDateFromatToddmmyyyy(insuranceupto));
			if(!pollutiondate.equalsIgnoreCase("0000-00-00")){
			vehicleDto.setPollutionstart(OtherFunctions.changeDateFromatToddmmyyyy(pollutiondate));
			vehicleDto.setPollutionUpto(OtherFunctions.changeDateFromatToddmmyyyy(pollutionupto));
			}else{
				vehicleDto.setPollutionstart("");
				vehicleDto.setPollutionUpto("");
			}
			vehicleDto.setFireexit(fireexit);
			vehicleDto.setFirstaid(firstaid);
			vehicleDto.setCabcondition(cabcondition);
			vehicleDto.setRemark(remark);
			vehicleDto.setModel(model);
			vehicleDtos.add(vehicleDto);
		}

	} catch (Exception e) {

	} finally {
		DbConnect.closeStatement(pst);
		DbConnect.closeConnection(con);
	}
	return vehicleDtos;

}
public VehicleDto getVehicleDetails(String id) {
	DbConnect ob = DbConnect.getInstance();
	PreparedStatement pst = null;
	ResultSet rs = null;
	Connection con = ob.connectDB();
	ArrayList<VehicleDto> vehicleDtos = new ArrayList<VehicleDto>();
	VehicleDto vehicleDto = null;
	try {
		pst = con
				.prepareStatement("select v.id,v.regNo,v.status,vt.type,v.fcnumber,v.site,v.taxstart,v.taxend,v.insurancefrom,v.insuranceupto,v.pollutiondate,v.pollutionupto,v.fireexit,v.firstaid,v.cabcondition,v.remark,v.vendor,v.model from vehicles v,vehicle_type vt where v.vehicletype=vt.id and v.id="+id);
		rs = pst.executeQuery();
		while (rs.next()) {
			vehicleDto = new VehicleDto();
			vehicleDto.setId(rs.getString("id"));
			vehicleDto.setVehicleNo(rs.getString("regNo"));
			vehicleDto.setVehicleType(rs.getString("type"));
			vehicleDto.setStatus(rs.getString("status"));
			vehicleDto.setVendor(rs.getString("vendor"));
			String site=rs.getString("site")== null ? "" :rs.getString("site");
			String fcnumber=rs.getString("fcnumber")== null ? "" :rs.getString("fcnumber");
			String taxstart=rs.getString("taxstart")== null ? "" :rs.getString("taxstart");
			String taxend=rs.getString("taxend")== null ? "" :rs.getString("taxend");
			String insurancefrom=rs.getString("insurancefrom")== null ? "" :rs.getString("insurancefrom");
			String insuranceupto=rs.getString("insuranceupto")== null ? "" :rs.getString("insuranceupto");
			String pollutiondate=rs.getString("pollutiondate")== null ? "" :rs.getString("pollutiondate");
			String pollutionupto=rs.getString("pollutionupto")== null ? "" :rs.getString("pollutionupto");
			String fireexit=rs.getString("fireexit")== null ? "" :rs.getString("fireexit");
			String firstaid=rs.getString("firstaid")== null ? "" :rs.getString("firstaid");
			String cabcondition=rs.getString("cabcondition")== null ? "" :rs.getString("cabcondition");
			String remark=rs.getString("remark")== null ? "" :rs.getString("remark");
			String model=rs.getString("model")== null ? "" :rs.getString("model");
			vehicleDto.setSite(site);
			vehicleDto.setFcnumber(fcnumber);
			vehicleDto.setTaxfromdate(taxstart);
			vehicleDto.setTaxtodate(taxend);
			vehicleDto.setInsuranceStart(insurancefrom);
			vehicleDto.setInsuranceupto(insuranceupto);
			vehicleDto.setPollutionstart(pollutiondate);
			vehicleDto.setPollutionUpto(pollutionupto);
			vehicleDto.setFireexit(fireexit);
			vehicleDto.setFirstaid(firstaid);
			vehicleDto.setCabcondition(cabcondition);
			vehicleDto.setRemark(remark);
			vehicleDto.setModel(model);
		}

	} catch (Exception e) {
			System.out.println("Error in vehicleDao@getVehicleDetails"+e);
	} finally {
		DbConnect.closeResultSet(rs);
		DbConnect.closeStatement(pst);
		DbConnect.closeConnection(con);
	}
	return vehicleDto;

}

}