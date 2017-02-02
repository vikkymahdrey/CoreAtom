package com.agiledge.atom.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.agiledge.atom.dbConnect.DbConnect;
import com.agiledge.atom.dto.DriverDto;
import com.agiledge.atom.dto.VehicleDto;

public class NotificationDao {

	public List<VehicleDto> notificationForvehicles(String notificationFor) {
		DbConnect ob = DbConnect.getInstance();
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		List<VehicleDto> list=new ArrayList<VehicleDto>();
		String condition="";
		try {
			Calendar cal = Calendar.getInstance();
			cal.getTime();
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			String curdate = format.format(cal.getTime());
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.DAY_OF_MONTH, 15);
			String nextdate = format.format(calendar.getTime());
			con = ob.connectDB();
			st = con.createStatement();
			if(notificationFor.equalsIgnoreCase("tax")){
				condition=" and v.taxend < '"+nextdate+"'";
			}else if(notificationFor.equalsIgnoreCase("insurance")){
				condition=" and v.insuranceupto < '"+nextdate+"'";
			}else if(notificationFor.equalsIgnoreCase("pollution")){
				condition=" and v.pollutionupto < '"+nextdate+"'";
			}
			String qry="select v.id,v.regNo,v.taxend,v.insuranceupto,v.pollutionupto,p.type,d.company from vehicles v,vehicle_type p,vendormaster d where v.vehicletype=p.id and v.vendor=d.id"+condition;
			System.out.println(qry);
			rs=st.executeQuery(qry);
			while(rs.next()){
				VehicleDto dto = new VehicleDto();
				dto.setId(rs.getString("id"));
				dto.setVehicleNo(rs.getString("regNo"));
				dto.setVehicleType(rs.getString("type"));
				dto.setVendor(rs.getString("company"));
				dto.setTaxtodate(rs.getString("taxend"));
				dto.setInsuranceupto(rs.getString("v.insuranceupto"));
				dto.setPollutionUpto(rs.getString("pollutionupto"));
				list.add(dto);
			}
		} catch (SQLException e) {
			System.out.println("Error in NotificationDao@notificationForvehicles"+e);
		}finally{
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);
		}
		return list;
	}

	public List<VehicleDto> notificationForInsuranceEnd(String date) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<VehicleDto> notificationForPUC(String date) {
		// TODO Auto-generated method stub
		return null;
	}
	public byte[] getphoto(){
		DbConnect ob = DbConnect.getInstance();
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		byte[] imag=null;
		try{
			con=ob.connectDB();
			st=con.createStatement();
			String qry="select taximage from vehicles where id=29";
			rs=st.executeQuery(qry);
			if(rs.next()){
				imag=rs.getBytes("taximage");
			}
		} catch (SQLException e) {
			System.out.println("Error in NotificationDao@photo"+e);
		}finally{
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);
		}
		return imag;
	}

	public List<DriverDto> notificationForDriverLisence() {
		// TODO Auto-generated method stub
		DbConnect ob = DbConnect.getInstance();
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		List<DriverDto> list=new ArrayList<DriverDto>();
		try{
			Calendar cal = Calendar.getInstance();
			cal.getTime();
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			String curdate = format.format(cal.getTime());
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.DAY_OF_MONTH, 15);
			String nextdate = format.format(calendar.getTime());
			con=ob.connectDB();
			st=con.createStatement();
			String qry="select d.name,d.contact,d.lisence,d.lisenceexpdt,v.company from driver d,vendormaster v where v.id=d.vendorid and d.lisenceexpdt < '"+nextdate+"'";
			rs=st.executeQuery(qry);
			while(rs.next()){
				DriverDto dto= new DriverDto();
				dto.setDriverName(rs.getString("name"));
				dto.setVendor(rs.getString("company"));
				dto.setContactNo(rs.getString("contact"));
				dto.setLisence(rs.getString("lisence"));
				dto.setLisenceExpiryDt(rs.getString("lisenceexpdt"));
				list.add(dto);
			}
		} catch (SQLException e) {
			System.out.println("Error in NotificationDao@notificationForDriverLisence"+e);
		}finally{
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);
		}
		return list;
	}
}
