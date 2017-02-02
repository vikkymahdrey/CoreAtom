package com.agiledge.atom.dao;

import java.lang.reflect.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.agiledge.atom.constants.SettingsConstant;
import com.agiledge.atom.dbConnect.DbConnect;
import com.agiledge.atom.dto.EmployeeDto;
import com.agiledge.atom.dto.GeoTagDto;
import com.agiledge.atom.dto.TripDetailsChildDto;

public class ShuttleSocketDao {
	public int setEmployeeInOut(String site, String[] empid, String inTime,
			String outTime, String route, String landmark, String routeOut, String landmarkOut) {
		DbConnect ob = DbConnect.getInstance();
		Connection con = null;
		Statement st = null, st1 = null, st2 = null;
		ResultSet rs = null, rs1 = null;
		int result = 0, k = 0;
		try {
			String inclouse = "", outclouse = "", inup = "", outup = "";
			String inval = "", outval = "";
			if (!inTime.equalsIgnoreCase("NO")) {
				inclouse = " log_in,";
				inval = ",'" + inTime + "'";
				inup = ",log_in='" + inTime + "'";
			}
			if (!outTime.equalsIgnoreCase("NO")) {
				outclouse = " log_out,";
				outval = ",'" + outTime + "'";
				outup = ",log_out='" + outTime + "'";
			}
			con = ob.connectDB();
			st = con.createStatement();
			st1 = con.createStatement();
			st2 = con.createStatement();
			String exEmpid[] = new String[empid.length];
			String sub1 = "", sub2 = "";
			for (int i = 0; i < empid.length; i++) {
				sub1 += empid[i];
				if (i < (empid.length - 1)) {
					sub1 += ",";
				}
			}
			String selectqry = "select empid from employee_tripping where empid in ("
					+ sub1 + ")";
			rs = st1.executeQuery(selectqry);
			while (rs.next()) {
				exEmpid[k] = rs.getString("empid");
				k++;
				sub2 += rs.getString("empid");
				if (!rs.isLast()) {
					sub2 += ",";
				}
			}
			String updateqry = "update employee_tripping set routeIn='" + route + "'" + inup + outup
					+ ",ePick='" + landmark
					+ "', routeOut='" + routeOut + "'"
					+", eDrop='" + landmarkOut
					+ "' where empid in (" + sub2 + ")";
			System.out.println(updateqry);
			if(sub2!=""){
			result=st2.executeUpdate(updateqry);
			}
			String subqry = "";
			String insertqry = "INSERT INTO employee_tripping (siteId,empId,"
					+ inclouse + outclouse + " routeIn,ePick,routeOut,eDrop) VALUES";
			for (int i = 0; i < empid.length; i++) {
				int m=0;
				for (int j = 0; j < exEmpid.length; j++) {
					if(empid[i].equalsIgnoreCase(exEmpid[j])){
					m++;}}
					if(m==0){
						subqry += "(" + site + ",'" + empid[i] + "'" + inval
								+ outval + ", '" + route + "','" + landmark
								+ "','"+routeOut+"','"+landmarkOut+"'),";
					}
			}
			if(subqry!=""){
				subqry=subqry.substring(0, subqry.length()-1);
				System.out.println(insertqry + subqry);
			result+= st.executeUpdate(insertqry + subqry);
			}
		} catch (Exception e) {
			System.out.println("Error ShuttleSocketDao@setEmployeeInOut" + e);
		} finally {
			DbConnect.closeResultSet(rs, rs1);
			DbConnect.closeStatement(st, st1, st2);
			DbConnect.closeConnection(con);
		}
		return result;
	}

	public ArrayList<EmployeeDto> getEmployeeTrippingDetails(String site) {
		DbConnect ob = DbConnect.getInstance();
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		ArrayList<EmployeeDto> emplist = null;
		try {
			emplist = new ArrayList<EmployeeDto>();
			con = ob.connectDB();
			st = con.createStatement();
			String selectquery = "select e.id,e.displayname,e.PersonnelNo,et.log_in,et.log_out,r.routeName as 'inRoute',r1.routeName as 'outRoute' from employee_tripping et,employee e,route r,route r1 where et.empId=e.id and r.id=et.routeIn and r1.id=et.routeOut and e.site="
					+ site;
			System.out.println(selectquery);
			rs = st.executeQuery(selectquery);
			while (rs.next()) {
				EmployeeDto dto = new EmployeeDto();
				dto.setEmployeeID(rs.getString("id"));
				dto.setDisplayName(rs.getString("displayname"));
				dto.setPersonnelNo(rs.getString("PersonnelNo"));
				dto.setLogin(rs.getString("log_in"));
				dto.setLogout(rs.getString("log_out"));
				dto.setinroute(rs.getString("inRoute"));
				dto.setOutroute(rs.getString("outRoute"));
				emplist.add(dto);
			}
		} catch (Exception e) {
			System.out
					.println("Error ShuttleSocketDao@getEmployeeTrippingDetails"
							+ e);
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);
		}
		return emplist;
	}

	public ArrayList<EmployeeDto> getEmployeeDetails(String[] empids) {
		DbConnect ob = DbConnect.getInstance();
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		ArrayList<EmployeeDto> emplist = null;
		try {
			emplist = new ArrayList<EmployeeDto>();
			con = ob.connectDB();
			st = con.createStatement();
			String emp = "";
			for (int i = 0; i < empids.length; i++) {
				emp += empids[i];
				if (i < empids.length - 1) {
					emp += ",";
				}
			}
			String selectquery = "select e.id,e.displayname,e.PersonnelNo,et.log_in,et.log_out,r.id as routeName, a.area,p.place,l.landMark,et.ePick as lid, r1.id as rName1, a1.area as area1,p1.place as place1,l1.landMark as landMark1,et.eDrop as lid1 from employee_tripping et,employee e,route r,area a,place p,landmark l,route r1,area a1,place p1,landmark l1 where et.empId=e.id and r.id=et.routeIn and a.id=p.area and p.id= l.place and l.id=et.ePick and r1.id=et.routeIn and a1.id=p1.area and p1.id= l1.place and l1.id=et.eDrop and e.id in ("
					+ emp + ")";
			rs = st.executeQuery(selectquery);
			while (rs.next()) {
				EmployeeDto dto = new EmployeeDto();
				dto.setEmployeeID(rs.getString("id"));
				dto.setDisplayName(rs.getString("displayname"));
				dto.setPersonnelNo(rs.getString("PersonnelNo"));
				dto.setLogin(rs.getString("log_in"));
				dto.setLogout(rs.getString("log_out"));
				dto.setinroute(rs.getString("routeName"));
				dto.setOutroute(rs.getString("rName1"));
				dto.setPickup(rs.getString("lid"));
				dto.setDrop(rs.getString("lid1"));
				dto.setAPL(rs.getString("area") + "->" + rs.getString("place")
						+ "->" + rs.getString("landMark"));
				dto.setAddress(rs.getString("area1") + "->" + rs.getString("place1")
						+ "->" + rs.getString("landMark1"));
				emplist.add(dto);
			}
		} catch (Exception e) {
			System.out.println("Error ShuttleSocketDao@getEmployeeDetails" + e);
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);
		}
		return emplist;
	}
	public ArrayList<TripDetailsChildDto> searchEmployee(EmployeeDto employeeDto) {
		ArrayList<TripDetailsChildDto> tripSheetChild = new ArrayList<TripDetailsChildDto>();

		DbConnect ob = DbConnect.getInstance();
		boolean nameFlag = false;
		boolean emailFlag = false;
		boolean idflag = false;
		String query = "select * from employee";
		String subQuery = "";

		if (employeeDto.getEmployeeFirstName() != null
				&& !employeeDto.getEmployeeFirstName().equals("")) {
			subQuery = subQuery + "EmployeeFirstName like ? ";
			nameFlag = true;
		}

		if (employeeDto.getEmployeeID() != null
				&& !employeeDto.getEmployeeID().equals("")) {
			if (!subQuery.equals("")) {
				subQuery = subQuery + " and ";
			}
			subQuery = subQuery + " PersonnelNo =? ";

			idflag = true;

		}

		if (employeeDto.getEmployeeLastName() != null
				&& !employeeDto.getEmployeeLastName().equals("")) {
			if (!subQuery.equals("")) {
				subQuery = subQuery + " and ";
			}
			subQuery = subQuery + " employeeLastName like ? ";

			emailFlag = true;
		}

		if (!subQuery.equals("")) {
			query = query + " where " + subQuery;
		}

		// System.out.println("In getEmployeeDao");
		 System.out.println("Query :" + query);
		Connection con = ob.connectDB();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = con.prepareStatement(query);
			// Statement stmt = con.createStatement();
			int i = 1;
			if (nameFlag) {
				stmt.setString(i, "%" + employeeDto.getEmployeeFirstName()
						+ "%");
				i++;
			}
			if (idflag) {
				stmt.setString(i, employeeDto.getEmployeeID());
				i++;
			}
			if (emailFlag) {
				stmt.setString(i, "%" + employeeDto.getEmployeeLastName() + "%");
				i++;
			}

			// stmt.setString(1, employeeName );
			// stmt.setString(2, employeeName );
			// ResultSet rs = stmt.executeQuery(query);
			rs = stmt.executeQuery();

			while (rs.next()) {
				TripDetailsChildDto tripDetailsChildDtoObj = new TripDetailsChildDto();
				tripDetailsChildDtoObj.setEmployeeId(rs.getString("id"));
				tripDetailsChildDtoObj.setEmployeeName(rs
						.getString("displayname"));
				tripSheetChild.add(tripDetailsChildDtoObj);
			}

		} catch (Exception e) {

			System.out.println("Error ShuttleSocketDao@searchEmployee " + e);

		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(stmt);
			DbConnect.closeConnection(con);
		}
		return tripSheetChild;
	}

	public int setShuttlePickUpDrop(EmployeeDto dto) {
		DbConnect ob = DbConnect.getInstance();
		Connection con = null;
		Statement st=null,st1=null;
		PreparedStatement pst=null;
		ResultSet rs=null;
		int result=0;
		try{
			String plat="";
			String dlat="";
			String plong="";
			String dlong="";

			String routein="";
			String routeout="";
			con=ob.connectDB();
			
			if(SettingsConstant.comp.equalsIgnoreCase("keo"))
			{
				routein=dto.getinroute();
				routeout=dto.getOutroute();
				
			String platlong = dto.getPickup();
			platlong = platlong.substring(1, platlong.length() - 1);

			plat=platlong.split(",")[0];
			plong=platlong.split(", ")[1];
			
			String dlatlong = dto.getDrop();
			dlatlong = dlatlong.substring(1, dlatlong.length() - 1);

			dlat=dlatlong.split(",")[0];
			dlong=dlatlong.split(", ")[1];
			}
			String selectqry="select empid from employee_tripping where empid="+dto.getEmployeeID();
			st=con.createStatement();
			rs=st.executeQuery(selectqry);
			if(rs.next()){
				String updateqry="UPDATE employee_tripping SET log_in='"+dto.getLogin()+"', log_out='"+dto.getLogout()+"', routeIn='"+routein+"', routeOut='"+routeout+"', plat='"+plat+"',plong='"+plong+"', dlat='"+dlat+"', dlong='"+dlong+"' WHERE empid='"+dto.getEmployeeID()+"';";
				st1=con.createStatement();
				result=st1.executeUpdate(updateqry);
			}else{
			String qry="INSERT INTO employee_tripping (siteId, empId, log_in, log_out, routeIn, routeOut, plat, plong, dlat, dlong) VALUES ( '1', ?, ?, ?, ?, ?, ?, ?, ?,?)";
			pst=con.prepareStatement(qry);
			pst.setString(1, dto.getEmployeeID());
			pst.setString(2, dto.getLogin());
			pst.setString(3, dto.getLogout());
			pst.setString(4,routein);
			pst.setString(5,routeout);
			pst.setString(6, plat);
			pst.setString(7, plong);
			pst.setString(8, dlat);
			pst.setString(9, dlong);
			result=pst.executeUpdate();
			}
			
		} catch (Exception e) {

			System.out.println("Error ShuttleSocketDao@setShuttlePickUpDrop " + e);

		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(pst,st,st1);
			DbConnect.closeConnection(con);
		}
		return result;
	}

	public EmployeeDto getShuttlePickUpDrop(String empid) {
		DbConnect ob = DbConnect.getInstance();
		Connection con = null;
		Statement st=null;
		ResultSet rs=null;
		EmployeeDto dto = new EmployeeDto();
		try{
			con=ob.connectDB();
			dto.setEmployeeID(empid);
			dto.setLogin("");
			dto.setLogout("");
			dto.setinroute("");
			dto.setOutroute("");
			dto.setPickup("");
			dto.setDrop("");
			
			
			
			
			
			String qry="select * from employee_tripping where empid="+empid;
			st=con.createStatement();
			rs=st.executeQuery(qry);
			if(rs.next()){
				//siteId, empId, log_in, log_out, routeIn, routeOut, plat, plong, dlat, dlong
				if(rs.getString("log_in")==null){
					dto.setLogin("");
				}
				else{
				dto.setLogin(rs.getString("log_in"));
				}
				if(rs.getString("log_out")==null){
					dto.setLogout("");
				}
				else{
				dto.setLogout(rs.getString("log_out"));
				}
				if(rs.getString("routeIn")==null){
					dto.setinroute("");
				}
				else{
				dto.setinroute(rs.getString("routeIn"));
				}
				if(rs.getString("routeOut")==null){
					dto.setOutroute("");
				}
				else{
					dto.setOutroute(rs.getString("routeOut"));
				}
				if(rs.getString("plat")==null||rs.getString("plong")==null){
					dto.setPickup("");
				}
				else{
					dto.setPickup(rs.getString("plat")+"#"+rs.getString("plong"));
				}
				if(rs.getString("dlat")==null||rs.getString("dlong")==null){
					dto.setDrop("");
				}
				else{
					dto.setDrop(rs.getString("dlat")+"#"+rs.getString("dlong"));
				}
				
			}
		} catch (Exception e) {

			System.out.println("Error ShuttleSocketDao@getShuttlePickUpDrop " + e);

		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);
		}
		return dto;
	}

	public int removeShuttlePickUpDrop(EmployeeDto dto) {
		// TODO Auto-generated method stub
		DbConnect ob = DbConnect.getInstance();
		Connection con = null;
		Statement st=null;
		int i=0;
		try{
			con=ob.connectDB();
			String deleteqry="delete from employee_tripping where empid="+dto.getEmployeeID();
			st=con.createStatement();
			i=st.executeUpdate(deleteqry);
		} catch (Exception e) {

			System.out.println("Error ShuttleSocketDao@removeShuttlePickUpDrop " + e);

		} finally {
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);
		}
		return i;
	}

	public List<GeoTagDto> getemployeeGeoTagDetails() {
		DbConnect ob = DbConnect.getInstance();
		Connection con = null;
		Statement st=null,st1=null,st2=null,st3=null;
		ResultSet rs=null,rs1=null,rs2=null,rs3=null;
		List<GeoTagDto> list = new ArrayList<GeoTagDto>();
		try{
			con=ob.connectDB();
			st=con.createStatement();
			DistanceListDao distdao= new DistanceListDao();
			DecimalFormat df = new DecimalFormat("###.##");
			String qry="select e.id,e.displayName,e.gender,e.EmailAddress,e.address,e.personnelno,et.log_in,et.log_out,e.emp_lat,e.emp_long,et.plat,et.plong,et.dlat,et.dlong,r.routename as inroute,r1.routename as outroute,et.routein as riid ,et.routeout as roid from employee e,employee_tripping et,route r,route r1 where et.empid=e.id and r.id=et.routein and r1.id=et.routeout and e.active='1' and e.emp_lat is not null";
			String qry5="select id,displayname,gender,EmailAddress,address,personnelno,emp_lat,emp_long from employee where emp_lat is not null and id not in (select empid from employee_tripping )";
			rs=st.executeQuery(qry);
			while(rs.next()){
				GeoTagDto dto = new GeoTagDto();
				dto.setEmpid(rs.getString("id"));
				dto.setEmpName(rs.getString("displayName"));
				dto.setAddress(rs.getString("address"));
				dto.setPersonnelNo(rs.getString("personnelno"));
				dto.setGender(rs.getString("gender"));
				dto.setEmailid(rs.getString("EmailAddress"));
				dto.setHomelat(rs.getString("emp_lat"));
				dto.setHomelong(rs.getString("emp_long"));
				dto.setPicklat(rs.getString("plat"));
				dto.setPicklong(rs.getString("plong"));
				dto.setDroplat(rs.getString("dlat"));
				dto.setDreoplong(rs.getString("dlong"));
				dto.setLogin(rs.getString("log_in"));
				dto.setLogout(rs.getString("log_out"));
				dto.setInroute(rs.getString("inroute"));
				dto.setOutroute(rs.getString("outroute"));
				dto.setHometopickdistance( Double.valueOf(df.format(distdao.getDistanceWithoutMap(Double.parseDouble(rs.getString("emp_lat")),Double.parseDouble( rs.getString("emp_long")), Double.parseDouble(rs.getString("plat")), Double.parseDouble(rs.getString("plong"))))));
				dto.setHometodropdistance(Double.valueOf(df.format(distdao.getDistanceWithoutMap(Double.parseDouble(rs.getString("emp_lat")),Double.parseDouble( rs.getString("emp_long")), Double.parseDouble(rs.getString("dlat")), Double.parseDouble(rs.getString("dlong"))))));
				dto.setPickuptodropdistance(Double.valueOf(df.format(distdao.getDistanceWithoutMap(Double.parseDouble(rs.getString("plat")),Double.parseDouble( rs.getString("plong")), Double.parseDouble(rs.getString("dlat")), Double.parseDouble(rs.getString("dlong"))))));
				dto.setDistanceperadmin(0);
				dto.setDistanceperadminout(0);
				String lat="",lon="",lat1="",lon1="";
				lat=rs.getString("plat");
				lon=rs.getString("plong");
				lat1=rs.getString("dlat");
				lon1=rs.getString("dlong");
				String qry1="SELECT *,(((acos(sin(("+lat+"*pi()/180)) * sin((dest.latitude*pi()/180))+cos(("+lat+"*pi()/180))*cos((dest.latitude*pi()/180))*cos((("+lon+"-dest.longitude)*pi()/180))))*180/pi())*60*1.1515*1609.344) as distance FROM landmark AS dest  where id in(select landmarkId from routechild where routeid="+rs.getString("riid")+")  ORDER BY distance limit 1";
				String qry2="SELECT *,(((acos(sin(("+lat1+"*pi()/180)) * sin((dest.latitude*pi()/180))+cos(("+lat1+"*pi()/180))*cos((dest.latitude*pi()/180))*cos((("+lon1+"-dest.longitude)*pi()/180))))*180/pi())*60*1.1515*1609.344) as distance FROM landmark AS dest  where id in(select landmarkId from routechild where routeid="+rs.getString("roid")+")  ORDER BY distance limit 1";
				st1=con.createStatement();
				rs1=st1.executeQuery(qry1);
				if(rs1.next()){
					dto.setDistanceperadmin(Double.valueOf(df.format(rs1.getDouble("distance"))));
				}
				st2=con.createStatement();
				rs2=st2.executeQuery(qry2);
				if(rs2.next()){
					dto.setDistanceperadminout(Double.valueOf(df.format(rs2.getDouble("distance"))));
				}
				
				list.add(dto);
			}
			st3=con.createStatement();
			rs3=st3.executeQuery(qry5);
			while(rs3.next()){
				GeoTagDto dto = new GeoTagDto();
				dto.setEmpid(rs3.getString("id"));
				dto.setEmpName(rs3.getString("displayName"));
				dto.setAddress(rs3.getString("address"));
				dto.setGender(rs3.getString("gender"));
				dto.setEmailid(rs3.getString("EmailAddress"));
				dto.setPersonnelNo(rs3.getString("personnelno"));
				dto.setHomelat(rs3.getString("emp_lat"));
				dto.setHomelong(rs3.getString("emp_long"));
				dto.setPicklat("");
				dto.setPicklong("");
				dto.setDroplat("");
				dto.setDreoplong("");
				dto.setLogin("");
				dto.setLogout("");
				dto.setInroute("");
				dto.setOutroute("");
				dto.setDistanceperadmin(0);
				dto.setDistanceperadminout(0);
				list.add(dto);
			}
		} catch (Exception e) {
			System.out.println("Error ShuttleSocketDao@getemployeeGeoTagDetails " + e);

		} finally {
			DbConnect.closeResultSet(rs,rs1,rs2,rs3);
			DbConnect.closeStatement(st,st1,st2,st3);
			DbConnect.closeConnection(con);
		}
		return list;
	}
	public List<GeoTagDto> getemployeeLocationDetails() {
		DbConnect ob = DbConnect.getInstance();
		Connection con = null;
		Statement st=null,st3=null;
		ResultSet rs=null,rs3=null;
		List<GeoTagDto> list = new ArrayList<GeoTagDto>();
		try{
			con=ob.connectDB();
			st=con.createStatement();
			String qry="select e.id,e.displayName,e.gender,e.EmailAddress,e.address,e.personnelno,et.log_in,et.log_out,e.emp_lat,e.emp_long,et.plat,et.plong,et.dlat,et.dlong,r.routename as inroute,r1.routename as outroute,et.routein as riid ,et.routeout as roid from employee e,employee_tripping et,route r,route r1 where et.empid=e.id and r.id=et.routein and r1.id=et.routeout and e.active='1' and e.emp_lat is not null";
			String qry5="select id,displayname,gender,EmailAddress,address,personnelno,emp_lat,emp_long from employee where emp_lat is not null and id not in (select empid from employee_tripping )";
			rs=st.executeQuery(qry);
			while(rs.next()){
				GeoTagDto dto = new GeoTagDto();
				dto.setEmpid(rs.getString("id"));
				dto.setEmpName(rs.getString("displayName"));
				dto.setAddress(rs.getString("address"));
				dto.setPersonnelNo(rs.getString("personnelno"));
				dto.setGender(rs.getString("gender"));
				dto.setEmailid(rs.getString("EmailAddress"));
				dto.setHomelat(rs.getString("emp_lat"));
				dto.setHomelong(rs.getString("emp_long"));
				dto.setPicklat(rs.getString("plat"));
				dto.setPicklong(rs.getString("plong"));
				dto.setDroplat(rs.getString("dlat"));
				dto.setDreoplong(rs.getString("dlong"));
				
				list.add(dto);
			}
			st3=con.createStatement();
			rs3=st3.executeQuery(qry5);
			while(rs3.next()){
				GeoTagDto dto = new GeoTagDto();
				dto.setEmpid(rs3.getString("id"));
				dto.setEmpName(rs3.getString("displayName"));
				dto.setAddress(rs3.getString("address"));
				dto.setGender(rs3.getString("gender"));
				dto.setEmailid(rs3.getString("EmailAddress"));
				dto.setPersonnelNo(rs3.getString("personnelno"));
				dto.setHomelat(rs3.getString("emp_lat"));
				dto.setHomelong(rs3.getString("emp_long"));
				dto.setPicklat("");
				dto.setPicklong("");
				dto.setDroplat("");
				dto.setDreoplong("");
	
				list.add(dto);
			}
		} catch (Exception e) {
			System.out.println("Error ShuttleSocketDao@getemployeeGeoTagDetails " + e);

		} finally {
			DbConnect.closeResultSet(rs,rs3);
			DbConnect.closeStatement(st,st3);
			DbConnect.closeConnection(con);
		}
		return list;
	}

	public List<GeoTagDto> getemployeeGeoTagDetails1(int routeId,
			String logtype, String time ) {
		DbConnect ob = DbConnect.getInstance();
		Connection con = null;
		Statement st=null;
		ResultSet rs=null;
		List<GeoTagDto> list = new ArrayList<GeoTagDto>();
		try{
			con=ob.connectDB();
			st=con.createStatement();
			DistanceListDao distdao= new DistanceListDao();
			DecimalFormat df = new DecimalFormat("###.##");
			String subqry="";
			if(logtype.equalsIgnoreCase("IN")){
				subqry="et.log_in='"+time+"' and et.routeIn="+routeId+" and";
			}else{
				subqry="et.log_out='"+time+"' and et.routeOut="+routeId+" and";
			}
			String qry="select e.id,e.displayName,e.address,e.personnelno,e.emp_lat,e.emp_long,et.plat,et.plong,et.dlat,et.dlong from employee e,employee_tripping et where et.empid=e.id and "+subqry+" e.active='1' and e.emp_lat is not null";
			rs=st.executeQuery(qry);
			while(rs.next()){
				GeoTagDto dto = new GeoTagDto();
				dto.setEmpid(rs.getString("id"));
				dto.setEmpName(rs.getString("displayName"));
				dto.setAddress(rs.getString("address"));
				dto.setPersonnelNo(rs.getString("personnelno"));
				dto.setHomelat(rs.getString("emp_lat"));
				dto.setHomelong(rs.getString("emp_long"));
				dto.setPicklat(rs.getString("plat"));
				dto.setPicklong(rs.getString("plong"));
				dto.setDroplat(rs.getString("dlat"));
				dto.setDreoplong(rs.getString("dlong"));
				dto.setHometopickdistance( Double.valueOf(df.format(distdao.getDistanceWithoutMap(Double.parseDouble(rs.getString("emp_lat")),Double.parseDouble( rs.getString("emp_long")), Double.parseDouble(rs.getString("plat")), Double.parseDouble(rs.getString("plong"))))));
				dto.setHometodropdistance(Double.valueOf(df.format(distdao.getDistanceWithoutMap(Double.parseDouble(rs.getString("emp_lat")),Double.parseDouble( rs.getString("emp_long")), Double.parseDouble(rs.getString("dlat")), Double.parseDouble(rs.getString("dlong"))))));
				dto.setPickuptodropdistance(Double.valueOf(df.format(distdao.getDistanceWithoutMap(Double.parseDouble(rs.getString("plat")),Double.parseDouble( rs.getString("plong")), Double.parseDouble(rs.getString("dlat")), Double.parseDouble(rs.getString("dlong"))))));
				list.add(dto);
			}
		} catch (Exception e) {
			System.out.println("Error ShuttleSocketDao@getemployeeDetails1 " + e);

		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);
		}
		return list;
	}

	public List<GeoTagDto> employeeValueMatchWithNoodle(int routeId,
			String logtype, String time,String compareBy) {
		DbConnect ob = DbConnect.getInstance();
		Connection con = null;
		Statement st=null,st1=null;
		ResultSet rs=null,rs1=null;
		List<GeoTagDto> list = new ArrayList<GeoTagDto>();
		try{
			con=ob.connectDB();
			st=con.createStatement();
			DistanceListDao distdao= new DistanceListDao();
			DecimalFormat df = new DecimalFormat("###.##");
			String subqry="";
			if(logtype.equalsIgnoreCase("IN")){
				subqry="et.log_in='"+time+"' and et.routeIn="+routeId+" and";
			}else{
				subqry="et.log_out='"+time+"' and et.routeOut="+routeId+" and";
			}
			String getemp="select e.id,e.displayName,e.address,e.personnelno,e.emp_lat,e.emp_long,et.plat,et.plong,et.dlat,et.dlong from employee e,employee_tripping et where et.empid=e.id and "+subqry+" e.active='1' and e.emp_lat is not null";
			rs=st.executeQuery(getemp);
			while(rs.next()){
				String lat="",lon="";
				if(compareBy.equalsIgnoreCase("HOME")){
					lat=rs.getString("emp_lat");
					lon=rs.getString("emp_long");
				}else if(logtype.equalsIgnoreCase("IN")){
					lat=rs.getString("plat");
					lon=rs.getString("plong");
				}else{
					lat=rs.getString("dlat");
					lon=rs.getString("dlong");
				}
				String distqry="SELECT *,(((acos(sin(("+lat+"*pi()/180)) * sin((dest.latitude*pi()/180))+cos(("+lat+"*pi()/180))*cos((dest.latitude*pi()/180))*cos((("+lon+"-dest.longitude)*pi()/180))))*180/pi())*60*1.1515*1609.344) as distance FROM landmark AS dest HAVING distance < 200 and id in(select landmarkId from routechild where routeid="+routeId+")  ORDER BY distance ";
				st1=con.createStatement();
				rs1=st1.executeQuery(distqry);
				if(rs1.next()){
					GeoTagDto dto = new GeoTagDto();
					dto.setEmpid(rs.getString("id"));
					dto.setEmpName(rs.getString("displayName"));
					dto.setAddress(rs.getString("address"));
					dto.setPersonnelNo(rs.getString("personnelno"));
					dto.setHomelat(rs.getString("emp_lat"));
					dto.setHomelong(rs.getString("emp_long"));
					dto.setPicklat(rs.getString("plat"));
					dto.setPicklong(rs.getString("plong"));
					dto.setDroplat(rs.getString("dlat"));
					dto.setDreoplong(rs.getString("dlong"));
					dto.setHometopickdistance( Double.valueOf(df.format(distdao.getDistanceWithoutMap(Double.parseDouble(rs.getString("emp_lat")),Double.parseDouble( rs.getString("emp_long")), Double.parseDouble(rs.getString("plat")), Double.parseDouble(rs.getString("plong"))))));
					dto.setHometodropdistance(Double.valueOf(df.format(distdao.getDistanceWithoutMap(Double.parseDouble(rs.getString("emp_lat")),Double.parseDouble( rs.getString("emp_long")), Double.parseDouble(rs.getString("dlat")), Double.parseDouble(rs.getString("dlong"))))));
					dto.setPickuptodropdistance(Double.valueOf(df.format(distdao.getDistanceWithoutMap(Double.parseDouble(rs.getString("plat")),Double.parseDouble( rs.getString("plong")), Double.parseDouble(rs.getString("dlat")), Double.parseDouble(rs.getString("dlong"))))));
					list.add(dto);
				}
			}
		} catch (Exception e) {
			System.out.println("Error ShuttleSocketDao@employeeValueMatchWithNoodle " + e);

		} finally {
			DbConnect.closeResultSet(rs,rs1);
			DbConnect.closeStatement(st,st1);
			DbConnect.closeConnection(con);
		}
		return list;

	}

	public List<GeoTagDto> employeeValueDifrrWthNoodle(int routeId,
			String logtype, String time, String compareBy) {
		DbConnect ob = DbConnect.getInstance();
		Connection con = null;
		Statement st=null,st1=null;
		ResultSet rs=null,rs1=null;
		List<GeoTagDto> list = new ArrayList<GeoTagDto>();
		try{
			con=ob.connectDB();
			st=con.createStatement();
			DistanceListDao distdao= new DistanceListDao();
			DecimalFormat df = new DecimalFormat("###.##");
			String subqry="";
			if(logtype.equalsIgnoreCase("IN")){
				subqry="et.log_in='"+time+"' and et.routeIn="+routeId+" and";
			}else{
				subqry="et.log_out='"+time+"' and et.routeOut="+routeId+" and";
			}
			String getemp="select e.id,e.displayName,e.address,e.personnelno,e.emp_lat,e.emp_long,et.plat,et.plong,et.dlat,et.dlong from employee e,employee_tripping et where et.empid=e.id and "+subqry+" e.active='1' and e.emp_lat is not null";
			rs=st.executeQuery(getemp);
			while(rs.next()){
				String lat="",lon="";
				if(compareBy.equalsIgnoreCase("HOME")){
					lat=rs.getString("emp_lat");
					lon=rs.getString("emp_long");
				}else if(logtype.equalsIgnoreCase("IN")){
					lat=rs.getString("plat");
					lon=rs.getString("plong");
				}else{
					lat=rs.getString("dlat");
					lon=rs.getString("dlong");
				}
				String distqry="SELECT *,(((acos(sin(("+lat+"*pi()/180)) * sin((dest.latitude*pi()/180))+cos(("+lat+"*pi()/180))*cos((dest.latitude*pi()/180))*cos((("+lon+"-dest.longitude)*pi()/180))))*180/pi())*60*1.1515*1609.344) as distance FROM landmark AS dest  where id in(select landmarkId from routechild where routeid="+routeId+")  ORDER BY distance";
				st1=con.createStatement();
				rs1=st1.executeQuery(distqry);
				if(rs1.next()){
					GeoTagDto dto = new GeoTagDto();
					dto.setEmpid(rs.getString("id"));
					dto.setEmpName(rs.getString("displayName"));
					dto.setAddress(rs.getString("address"));
					dto.setPersonnelNo(rs.getString("personnelno"));
					dto.setHomelat(rs.getString("emp_lat"));
					dto.setHomelong(rs.getString("emp_long"));
					dto.setPicklat(rs.getString("plat"));
					dto.setPicklong(rs.getString("plong"));
					dto.setDroplat(rs.getString("dlat"));
					dto.setDreoplong(rs.getString("dlong"));
					dto.setHometopickdistance( Double.valueOf(df.format(distdao.getDistanceWithoutMap(Double.parseDouble(rs.getString("emp_lat")),Double.parseDouble( rs.getString("emp_long")), Double.parseDouble(rs.getString("plat")), Double.parseDouble(rs.getString("plong"))))));
					dto.setHometodropdistance(Double.valueOf(df.format(distdao.getDistanceWithoutMap(Double.parseDouble(rs.getString("emp_lat")),Double.parseDouble( rs.getString("emp_long")), Double.parseDouble(rs.getString("dlat")), Double.parseDouble(rs.getString("dlong"))))));
					dto.setPickuptodropdistance(Double.valueOf(df.format(distdao.getDistanceWithoutMap(Double.parseDouble(rs.getString("plat")),Double.parseDouble( rs.getString("plong")), Double.parseDouble(rs.getString("dlat")), Double.parseDouble(rs.getString("dlong"))))));
					dto.setDistanceperadmin(rs1.getDouble("distance"));
					list.add(dto);
				}
			}
		} catch (Exception e) {
			System.out.println("Error ShuttleSocketDao@employeeValueDifrrWthNoodle " + e);

		} finally {
			DbConnect.closeResultSet(rs,rs1);
			DbConnect.closeStatement(st,st1);
			DbConnect.closeConnection(con);
		}
		return list;
	}

	public ArrayList<GeoTagDto> getEmployeeNearNoodlePoints(String[] points,String distanceConst,String[] route) {
		DbConnect ob = DbConnect.getInstance();
		Connection con = null;
		Statement st=null;
		ResultSet rs=null;
		ArrayList<GeoTagDto> list = new ArrayList<GeoTagDto>();
		ArrayList<GeoTagDto> list1 = new ArrayList<GeoTagDto>();
		double distance= Double.parseDouble(distanceConst);
		DecimalFormat df = new DecimalFormat("###.##");
		try{
			con=ob.connectDB();
			st=con.createStatement();
			String sub="";
			for(int i=0;i<route.length;i++){
				sub+="'"+route[i]+"'";
				if(i!=route.length-1){
					sub+=",";
				}
			}
			DistanceListDao distdao= new DistanceListDao();
			String qry="select id,personnelno,displayname,emp_lat,emp_long from employee where emp_lat is not null and adminroute in ("+sub+")";
			System.out.println(qry+"  Disttttt");
			rs=st.executeQuery(qry);
			while(rs.next()){
				double dist1=5.0;
				double distfromCompany1=0.0;
				for(int i=0; i < points.length ;i++){
					String str= new String(points[i]);
					str=str.replace('*', '/');
					String lat=str.split("/")[0];
					String lng=str.split("/")[1];
					double dist=Double.valueOf(df.format(distdao.getDistanceWithoutMap(Double.parseDouble(rs.getString("emp_lat")),Double.parseDouble( rs.getString("emp_long")), Double.parseDouble(lat), Double.parseDouble(lng))));
					double distfromCompany=Double.valueOf(df.format(distdao.getDistanceWithoutMap(Double.parseDouble(rs.getString("emp_lat")),Double.parseDouble( rs.getString("emp_long")), Double.parseDouble("12.8451923"), Double.parseDouble("77.6594844"))));
					if(dist<=5){
						if(dist<dist1){
							dist1=dist;
							distfromCompany1=distfromCompany;
		
						}
					}
				}
				if(dist1<5){
						GeoTagDto dto = new GeoTagDto();
						dto.setEmpid(rs.getString("id"));
						dto.setEmpName(rs.getString("displayname"));
						dto.setPersonnelNo(rs.getString("personnelno"));
						dto.setDistanceperadmin(dist1);
						dto.setDistanceperadminout(distfromCompany1);
						dto.setHomelat(rs.getString("emp_lat"));
						dto.setHomelong(rs.getString("emp_long"));
						list.add(dto);
			}
			}
			Double[] array= new Double[list.size()];
			int i=0;
			for (GeoTagDto dt : list) {
			
				array[i]=dt.getDistanceperadminout();
				i++;
			}
			Arrays.sort(array);
			for(int k=array.length-1;k>=0;k--){
				for (GeoTagDto dt : list) {
					if(array[k].toString().equalsIgnoreCase(Double.toString(dt.getDistanceperadminout()))){
						GeoTagDto dto = new GeoTagDto();
						dto.setEmpid(dt.getEmpid());
						dto.setEmpName(dt.getEmpName());
						dto.setPersonnelNo(dt.getPersonnelNo());
						dto.setDistanceperadmin(dt.getDistanceperadmin());
						dto.setDistanceperadminout(dt.getDistanceperadminout());
						dto.setHomelat(dt.getHomelat());
						dto.setHomelong(dt.getHomelong());
						list1.add(dto);
					}
				}
			}
			
		} catch (Exception e) {
			System.out.println("Error ShuttleSocketDao@getEmployeeNearNoodlePoints " + e);
		//	e.printStackTrace();

		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);
		}
		return list1;
	}

	public List<GeoTagDto> getemployeeWithAdminRoute(String[] route) {
		// TODO Auto-generated method stub
		DbConnect ob = DbConnect.getInstance();
		Connection con = null;
		Statement st=null,st3=null;
		ResultSet rs=null,rs3=null;
		List<GeoTagDto> list = new ArrayList<GeoTagDto>();

		try{
			con=ob.connectDB();
			st=con.createStatement();
			String qry="",qry1="";;
			if(route[0].equalsIgnoreCase("NO")){
				qry="select e.id,e.displayName,e.gender,e.EmailAddress,e.address,e.personnelno,et.log_in,et.log_out,e.emp_lat,e.emp_long,et.plat,et.plong,et.dlat,et.dlong,r.routename as inroute,r1.routename as outroute,et.routein as riid ,et.routeout as roid from employee e,employee_tripping et,route r,route r1 where et.empid=e.id and r.id=et.routein and r1.id=et.routeout and e.active='1' and e.adminRoute=0 and  e.emp_lat is not null";
				qry1="select id,displayname,gender,EmailAddress,address,personnelno,emp_lat,emp_long from employee where emp_lat is not null and adminRoute=0 and id not in (select empid from employee_tripping )";
			}else{
				String sub="0,";
				for(int i=0;i<route.length;i++){
					sub+=route[i]+"";
					if(i!=route.length-1){
						sub+=",";
					}
				}
				qry="select e.id,e.displayName,e.gender,e.EmailAddress,e.address,e.personnelno,et.log_in,et.log_out,e.emp_lat,e.emp_long,et.plat,et.plong,et.dlat,et.dlong,r.routename as inroute,r1.routename as outroute,et.routein as riid ,et.routeout as roid from employee e,employee_tripping et,route r,route r1 where et.empid=e.id and r.id=et.routein and r1.id=et.routeout and e.active='1' and e.adminRoute in("+sub+") and  e.emp_lat is not null";
				qry1="select id,displayname,gender,EmailAddress,address,personnelno,emp_lat,emp_long from employee where emp_lat is not null and adminRoute in ("+sub+") and id not in (select empid from employee_tripping )";
			}
			rs=st.executeQuery(qry);
			while(rs.next()){
				GeoTagDto dto = new GeoTagDto();
				dto.setEmpid(rs.getString("id"));
				dto.setEmpName(rs.getString("displayName"));
				dto.setAddress(rs.getString("address"));
				dto.setPersonnelNo(rs.getString("personnelno"));
				dto.setGender(rs.getString("gender"));
				dto.setEmailid(rs.getString("EmailAddress"));
				dto.setHomelat(rs.getString("emp_lat"));
				dto.setHomelong(rs.getString("emp_long"));
				dto.setPicklat(rs.getString("plat"));
				dto.setPicklong(rs.getString("plong"));
				dto.setDroplat(rs.getString("dlat"));
				dto.setDreoplong(rs.getString("dlong"));
				list.add(dto);
			}
			st3=con.createStatement();
			rs3=st3.executeQuery(qry1);
			while(rs3.next()){
				GeoTagDto dto = new GeoTagDto();
				dto.setEmpid(rs3.getString("id"));
				dto.setEmpName(rs3.getString("displayName"));
				dto.setAddress(rs3.getString("address"));
				dto.setGender(rs3.getString("gender"));
				dto.setEmailid(rs3.getString("EmailAddress"));
				dto.setPersonnelNo(rs3.getString("personnelno"));
				dto.setHomelat(rs3.getString("emp_lat"));
				dto.setHomelong(rs3.getString("emp_long"));
				dto.setPicklat("");
				dto.setPicklong("");
				dto.setDroplat("");
				dto.setDreoplong("");
	
				list.add(dto);
			}
	
		} catch (Exception e) {
			System.out.println("Error ShuttleSocketDao@getemployeeWithoutAdminRoute" + e);

		} finally {
			DbConnect.closeResultSet(rs,rs3);
			DbConnect.closeStatement(st,st3);
			DbConnect.closeConnection(con);
		}
		return list;
	}

	public List<GeoTagDto> getemployeeWithAdminRouteAndLog(String logtype,
			String logtime,String[] route) {
		// TODO Auto-generated method stub
		DbConnect ob = DbConnect.getInstance();
		Connection con = null;
		Statement st=null;
		ResultSet rs=null;
		List<GeoTagDto> list = new ArrayList<GeoTagDto>();

		try{
			con=ob.connectDB();
			st=con.createStatement();
			String qry="";
			String subqry="";
			if(logtype.equalsIgnoreCase("IN")){
				if(!logtime.equalsIgnoreCase("all")){
				subqry=" et.log_in='"+logtime+"' and ";
				}
			}else{
				if(!logtime.equalsIgnoreCase("all")){
					subqry=" et.log_out='"+logtime+"' and ";
					}
			}
			
			if(route[0].equalsIgnoreCase("NO")){
				qry="select e.id,e.displayName,e.gender,e.EmailAddress,e.address,e.personnelno,et.log_in,et.log_out,e.emp_lat,e.emp_long,et.plat,et.plong,et.dlat,et.dlong from employee e,employee_tripping et where et.empid=e.id and e.active='1' and e.adminRoute=0 and "+subqry+" e.emp_lat is not null";
			}else{
				String sub="0,";
				for(int i=0;i<route.length;i++){
					sub+=route[i]+"";
					if(i!=route.length-1){
						sub+=",";
					}
				}
				qry="select e.id,e.displayName,e.gender,e.EmailAddress,e.address,e.personnelno,et.log_in,et.log_out,e.emp_lat,e.emp_long,et.plat,et.plong,et.dlat,et.dlong from employee e,employee_tripping et where et.empid=e.id and e.active='1' and e.adminRoute in("+sub+") and "+subqry+" e.emp_lat is not null";
			}
			System.out.println(qry+" qry");
			rs=st.executeQuery(qry);
			while(rs.next()){
				GeoTagDto dto = new GeoTagDto();
				dto.setEmpid(rs.getString("id"));
				dto.setEmpName(rs.getString("displayName"));
				dto.setAddress(rs.getString("address"));
				dto.setPersonnelNo(rs.getString("personnelno"));
				dto.setGender(rs.getString("gender"));
				dto.setEmailid(rs.getString("EmailAddress"));
				dto.setHomelat(rs.getString("emp_lat"));
				dto.setHomelong(rs.getString("emp_long"));
				dto.setPicklat(rs.getString("plat"));
				dto.setPicklong(rs.getString("plong"));
				dto.setDroplat(rs.getString("dlat"));
				dto.setDreoplong(rs.getString("dlong"));
				list.add(dto);
			}
	

		} catch (Exception e) {
			System.out.println("Error ShuttleSocketDao@getemployeeWithAdminRouteAndLog" + e);

		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);
		}
		return list;
	}

	public ArrayList<GeoTagDto> getEmployeeNearNoodlePointsWithLog(String[] points,
			String distanceConst, String[] route, String logtype, String time) {
		DbConnect ob = DbConnect.getInstance();
		Connection con = null;
		Statement st=null;
		ResultSet rs=null;
		ArrayList<GeoTagDto> list = new ArrayList<GeoTagDto>();
		ArrayList<GeoTagDto> list1 = new ArrayList<GeoTagDto>();
		double distance= Double.parseDouble(distanceConst);
		DecimalFormat df = new DecimalFormat("###.##");
		try{
			con=ob.connectDB();
			st=con.createStatement();
			String sub="",sub23="";
			for(int i=0;i<route.length;i++){
				sub+="'"+route[i]+"'";
				if(i!=route.length-1){
					sub+=",";
				}
			}
			if(logtype.equalsIgnoreCase("IN")){
				if(!time.equalsIgnoreCase("ALL")){
					sub23="and et.log_in='"+time+"'";
				}
			}else{
				if(!time.equalsIgnoreCase("ALL")){
					sub23="and et.log_out='"+time+"'";
				}
			}
			DistanceListDao distdao= new DistanceListDao();
			String qry="select e.id,e.personnelno,e.displayname,e.emp_lat,e.emp_long from employee e,employee_tripping et where et.empid=e.id "+sub23+" and e.emp_lat is not null and e.adminroute in ("+sub+")";
			System.out.println(qry+"  Disttttt");
			rs=st.executeQuery(qry);
			while(rs.next()){
				double dist1=5.0;
				double distfromCompany1=0.0;
				for(int i=0; i < points.length ;i++){
					String str= new String(points[i]);
					str=str.replace('*', '/');
					String lat=str.split("/")[0];
					String lng=str.split("/")[1];
					double dist=Double.valueOf(df.format(distdao.getDistanceWithoutMap(Double.parseDouble(rs.getString("emp_lat")),Double.parseDouble( rs.getString("emp_long")), Double.parseDouble(lat), Double.parseDouble(lng))));
					double distfromCompany=Double.valueOf(df.format(distdao.getDistanceWithoutMap(Double.parseDouble(rs.getString("emp_lat")),Double.parseDouble( rs.getString("emp_long")), Double.parseDouble("12.6810066"), Double.parseDouble("77.4353909"))));
					System.out.println(dist);
					if(dist<=5){
						if(dist<dist1){
							dist1=dist;
							distfromCompany1=distfromCompany;
		
						}
					}
				}
				if(dist1<5){
						GeoTagDto dto = new GeoTagDto();
						dto.setEmpid(rs.getString("id"));
						dto.setEmpName(rs.getString("displayname"));
						dto.setPersonnelNo(rs.getString("personnelno"));
						dto.setDistanceperadmin(dist1);
						dto.setDistanceperadminout(distfromCompany1);
						dto.setHomelat(rs.getString("emp_lat"));
						dto.setHomelong(rs.getString("emp_long"));
						list.add(dto);
			}
			}
			Double[] array= new Double[list.size()];
			int i=0;
			for (GeoTagDto dt : list) {
			
				array[i]=dt.getDistanceperadminout();
				i++;
			}
			Arrays.sort(array);
			for(int k=array.length-1;k>=0;k--){
				for (GeoTagDto dt : list) {
					if(array[k].toString().equalsIgnoreCase(Double.toString(dt.getDistanceperadminout()))){
						int cnt=0;
						for(GeoTagDto dto1 :list1){
							if(dto1.getEmpid().equalsIgnoreCase(dt.getEmpid())){
								cnt++;
							}
						}
						if(cnt==0){
						GeoTagDto dto = new GeoTagDto();
						dto.setEmpid(dt.getEmpid());
						dto.setEmpName(dt.getEmpName());
						dto.setPersonnelNo(dt.getPersonnelNo());
						dto.setDistanceperadmin(dt.getDistanceperadmin());
						dto.setDistanceperadminout(dt.getDistanceperadminout());
						dto.setHomelat(dt.getHomelat());
						dto.setHomelong(dt.getHomelong());
						list1.add(dto);
						}
					}
				}
			}
		} catch (Exception e) {
			System.out.println("Error ShuttleSocketDao@getEmployeeNearNoodlePointsWithLog " + e);
		//	e.printStackTrace();

		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);
		}
		return list1;

	}


	public int insertData(String data){
		DbConnect ob = DbConnect.getInstance();
		Connection con = null;
		Statement st = null;
		String query="INSERT INTO DATA_RECEIVED(DATA) VALUES('"+data+"')";
		int returnint=0;
		try{
			con = ob.connectDB();
			st = con.createStatement();
			returnint=st.executeUpdate(query);
		}catch(Exception e){e.printStackTrace();}
		return returnint;
	}
	
	
	//AutoRouteCreate
	public List<GeoTagDto> getshuttleEmployeeRoute(String logtype,
			String logtime, String[] routes) {
		DbConnect ob = DbConnect.getInstance();
		Connection con = null;
		Statement st=null;
		ResultSet rs=null;
		List<GeoTagDto> list = new ArrayList<GeoTagDto>();

		try{
			con=ob.connectDB();
			st=con.createStatement();
			String subqry="";
			String logqry="",sub3="";
			if(!logtime.equalsIgnoreCase("ALL")){
				if(logtype.equalsIgnoreCase("IN")){
					logqry="log_in='"+logtime+"' and ";
				}else{
					logqry="log_out='"+logtime+"' and ";
				}
			}
			for(int i=0;i<routes.length;i++){
				subqry+=routes[i];
				if(i!=routes.length-1){
					subqry+=",";
				}
			}
			if(logtype.equalsIgnoreCase("IN")){
				sub3=" routein "; 
			}else{
				sub3=" routeOut ";
			}
			String qry="select e.id,e.displayName,e.gender,e.EmailAddress,e.address,e.personnelno,et.log_in,et.log_out,e.emp_lat,e.emp_long,et.plat,et.plong,et.dlat,et.dlong from employee e,employee_tripping et where et.empid=e.id and e.emp_lat is not null and et.empid in(select empid from employee_tripping where "+logqry+sub3+" in ("+subqry+")  )";
			System.out.println(qry+" qry");
			rs=st.executeQuery(qry);
			while(rs.next()){
				GeoTagDto dto = new GeoTagDto();
				dto.setEmpid(rs.getString("id"));
				dto.setEmpName(rs.getString("displayName"));
				dto.setAddress(rs.getString("address"));
				dto.setPersonnelNo(rs.getString("personnelno"));
				dto.setGender(rs.getString("gender"));
				dto.setEmailid(rs.getString("EmailAddress"));
				dto.setHomelat(rs.getString("emp_lat"));
				dto.setHomelong(rs.getString("emp_long"));
				dto.setPicklat(rs.getString("plat"));
				dto.setPicklong(rs.getString("plong"));
				dto.setDroplat(rs.getString("dlat"));
				dto.setDreoplong(rs.getString("dlong"));
				list.add(dto);
			}
		} catch (Exception e) {
			System.out.println("Error ShuttleSocketDao@getshuttleEmployeeRoute" + e);

		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);
		}
		return list;

	}

	public ArrayList<GeoTagDto> getEmployeeNearNoodlePointsForShuttle(
			String[] points, String distanceConst, String[] route,
			String logtype, String time) {
		// TODO Auto-generated method stub
		DbConnect ob = DbConnect.getInstance();
		Connection con = null;
		Statement st=null;
		ResultSet rs=null;
		ArrayList<GeoTagDto> list = new ArrayList<GeoTagDto>();
		ArrayList<GeoTagDto> list1 = new ArrayList<GeoTagDto>();
		double distance= Double.parseDouble(distanceConst);
		DecimalFormat df = new DecimalFormat("###.##");
		try{
			con=ob.connectDB();
			st=con.createStatement();
			String sub="",sub23="",routeqry1="";
			for(int i=0;i<route.length;i++){
				sub+="'"+route[i]+"'";
				if(i!=route.length-1){
					sub+=",";
				}
			}
			if(logtype.equalsIgnoreCase("IN")){
				if(!time.equalsIgnoreCase("ALL")){
					sub23="and et.log_in='"+time+"'";
				}
				routeqry1=" et.routeIn ";
			}else{
				if(!time.equalsIgnoreCase("ALL")){
					sub23="and et.log_out='"+time+"'";
				}
				routeqry1=" et.routeOut ";
			}
			DistanceListDao distdao= new DistanceListDao();
			String qry="select e.id,e.personnelno,e.displayname,e.emp_lat,e.emp_long from employee e,employee_tripping et where et.empid=e.id "+sub23+" and e.emp_lat is not null and "+routeqry1+" in ("+sub+")";
			System.out.println(qry+"  Disttttt");
			rs=st.executeQuery(qry);
			while(rs.next()){
				double dist1=5.0;
				double distfromCompany1=0.0;
				for(int i=0; i < points.length ;i++){
					String str= new String(points[i]);
					str=str.replace('*', '/');
					String lat=str.split("/")[0];
					String lng=str.split("/")[1];
					double dist=Double.valueOf(df.format(distdao.getDistanceWithoutMap(Double.parseDouble(rs.getString("emp_lat")),Double.parseDouble( rs.getString("emp_long")), Double.parseDouble(lat), Double.parseDouble(lng))));
					double distfromCompany=Double.valueOf(df.format(distdao.getDistanceWithoutMap(Double.parseDouble(rs.getString("emp_lat")),Double.parseDouble( rs.getString("emp_long")), Double.parseDouble("12.6810066"), Double.parseDouble("77.4353909"))));
					System.out.println(dist);
					if(dist<=5){
						if(dist<dist1){
							dist1=dist;
							distfromCompany1=distfromCompany;
		
						}
					}
				}
				if(dist1<5){
						GeoTagDto dto = new GeoTagDto();
						dto.setEmpid(rs.getString("id"));
						dto.setEmpName(rs.getString("displayname"));
						dto.setPersonnelNo(rs.getString("personnelno"));
						dto.setDistanceperadmin(dist1);
						dto.setDistanceperadminout(distfromCompany1);
						dto.setHomelat(rs.getString("emp_lat"));
						dto.setHomelong(rs.getString("emp_long"));
						list.add(dto);
			}
			}
			Double[] array= new Double[list.size()];
			int i=0;
			for (GeoTagDto dt : list) {
			
				array[i]=dt.getDistanceperadminout();
				i++;
			}
			Arrays.sort(array);
			for(int k=array.length-1;k>=0;k--){
				for (GeoTagDto dt : list) {
					if(array[k].toString().equalsIgnoreCase(Double.toString(dt.getDistanceperadminout()))){
						int cnt=0;
						for(GeoTagDto dto1 :list1){
							if(dto1.getEmpid().equalsIgnoreCase(dt.getEmpid())){
								cnt++;
							}
						}
						if(cnt==0){
						GeoTagDto dto = new GeoTagDto();
						dto.setEmpid(dt.getEmpid());
						dto.setEmpName(dt.getEmpName());
						dto.setPersonnelNo(dt.getPersonnelNo());
						dto.setDistanceperadmin(dt.getDistanceperadmin());
						dto.setDistanceperadminout(dt.getDistanceperadminout());
						dto.setHomelat(dt.getHomelat());
						dto.setHomelong(dt.getHomelong());
						list1.add(dto);
						}
					}
				}
			}
		} catch (Exception e) {
			System.out.println("Error ShuttleSocketDao@getEmployeeNearNoodlePointsForShuttle " + e);
		//	e.printStackTrace();

		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);
		}
		return list1;

	}
	public List<GeoTagDto> getshuttleEmployeeRoute1(String logtype,
			String logtime, String[] route,String filter,String siteid) {
		DbConnect ob = DbConnect.getInstance();
		Connection con = null;
		Statement st=null;
		ResultSet rs=null;
		List<GeoTagDto> list = new ArrayList<GeoTagDto>();

		try{
			con=ob.connectDB();
			st=con.createStatement();
			String qry="";
			String subqry="";
			String rtqry="",rtqry1="";
			if(logtype.equalsIgnoreCase("IN")){
				if(!logtime.equalsIgnoreCase("all")){
				subqry=" et.log_in='"+logtime+"' and ";
				}
				rtqry="and e.adminRoute1=0 ";
				rtqry1=" e.adminRoute1 ";
			}else{
				if(!logtime.equalsIgnoreCase("all")){
					subqry=" et.log_out='"+logtime+"' and ";
					}
				rtqry="and e.adminoutRoute=0 ";
				rtqry1=" e.adminoutRoute ";
				
			}
			
			if(route[0].equalsIgnoreCase("NO")){
				qry="select e.id,e.displayName,e.gender,e.EmailAddress,e.address,e.personnelno,et.log_in,et.log_out,e.emp_lat,e.emp_long,et.plat,et.plong,et.dlat,et.dlong from employee e,employee_tripping et where et.empid=e.id and e.site='"+siteid+"' "+rtqry+" and e.active='1'  and "+subqry+" e.emp_lat is not null";
			}else{
				String sub="0,";
				for(int i=0;i<route.length;i++){
					sub+=route[i]+"";
					if(i!=route.length-1){
						sub+=",";
					}
				}
				qry="select e.id,e.displayName,e.gender,e.EmailAddress,e.address,e.personnelno,et.log_in,et.log_out,e.emp_lat,e.emp_long,et.plat,et.plong,et.dlat,et.dlong from employee e,employee_tripping et where et.empid=e.id and e.site='"+siteid+"' and e.active='1' and "+rtqry1+" in("+sub+") and "+subqry+" e.emp_lat is not null";
			}
			System.out.println(qry+" qry");
			rs=st.executeQuery(qry);
			while(rs.next()){
				GeoTagDto dto = new GeoTagDto();
				dto.setEmpid(rs.getString("id"));
				dto.setEmpName(rs.getString("displayName"));
				dto.setAddress(rs.getString("address"));
				dto.setPersonnelNo(rs.getString("personnelno"));
				dto.setGender(rs.getString("gender"));
				dto.setEmailid(rs.getString("EmailAddress"));
				dto.setHomelat(rs.getString("emp_lat"));
				dto.setHomelong(rs.getString("emp_long"));
				dto.setPicklat(rs.getString("plat"));
				dto.setPicklong(rs.getString("plong"));
				dto.setDroplat(rs.getString("dlat"));
				dto.setDreoplong(rs.getString("dlong"));
				list.add(dto);
			}
	

		} catch (Exception e) {
			System.out.println("Error ShuttleSocketDao@getshuttleEmployeeRoute1" + e);

		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);
		}
		return list;

	}

	public ArrayList<GeoTagDto> getEmployeeNearNoodlePointsWithLog2(
			String[] points, String distanceConst, String[] route,
			String logtype, String time,String filter,String siteid) {
		// TODO Auto-generated method stub
		DbConnect ob = DbConnect.getInstance();
		Connection con = null;
		Statement st=null;
		ResultSet rs=null;
		OtherDao otherdao = OtherDao.getInstance();
		String[] city = otherdao.getCity(siteid);
		ArrayList<GeoTagDto> list = new ArrayList<GeoTagDto>();
		ArrayList<GeoTagDto> list1 = new ArrayList<GeoTagDto>();
		DecimalFormat df = new DecimalFormat("###.##");
		try{
			con=ob.connectDB();
			st=con.createStatement();
			String sub="",sub23="";
			String rtqry="";
			for(int i=0;i<route.length;i++){
				sub+="'"+route[i]+"'";
				if(i!=route.length-1){
					sub+=",";
				}
			}
			if(logtype.equalsIgnoreCase("IN")){
				if(!time.equalsIgnoreCase("ALL")){
					sub23="and et.log_in='"+time+"'";
					rtqry=" e.adminRoute1 ";
				}
			}else{
				if(!time.equalsIgnoreCase("ALL")){
					sub23="and et.log_out='"+time+"'";
				}
				rtqry=" e.adminoutRoute ";
			}
			String qry="";
			DistanceListDao distdao= new DistanceListDao();
			if(filter.equalsIgnoreCase("home")){
				qry="select e.id,e.personnelno,e.displayname,e.emp_lat,e.emp_long from employee e,employee_tripping et where et.empid=e.id "+sub23+" and site="+siteid+" and e.emp_lat is not null and "+rtqry+" in ("+sub+")";
			}else if(filter.equalsIgnoreCase("pickup")){
				qry="select e.id,e.personnelno,e.displayname,et.plat as emp_lat,et.plong as emp_long from employee e,employee_tripping et where et.empid=e.id "+sub23+" and site="+siteid+" and e.emp_lat is not null and "+rtqry+" in ("+sub+")";
			}else{
				qry="select e.id,e.personnelno,e.displayname,et.dlat as emp_lat,et.dlong as emp_long from employee e,employee_tripping et where et.empid=e.id "+sub23+" and site="+siteid+" and e.emp_lat is not null and "+rtqry+" in ("+sub+")";
			}
			
			System.out.println(qry+"  Disttttt");
			rs=st.executeQuery(qry);
			while(rs.next()){
				double dist1=5.0;
				double distfromCompany1=0.0;
				for(int i=0; i < points.length ;i++){
					String str= new String(points[i]);
					str=str.replace('*', '/');
					String lat=str.split("/")[0];
					String lng=str.split("/")[1];
					double dist=Double.valueOf(df.format(distdao.getDistanceWithoutMap(Double.parseDouble(rs.getString("emp_lat")),Double.parseDouble( rs.getString("emp_long")), Double.parseDouble(lat), Double.parseDouble(lng))));
					double distfromCompany=Double.valueOf(df.format(distdao.getDistanceWithoutMap(Double.parseDouble(rs.getString("emp_lat")),Double.parseDouble( rs.getString("emp_long")), Double.parseDouble(city[0]), Double.parseDouble(city[1]))));
					System.out.println(dist);
					if(dist<=5){
						if(dist<dist1){
							dist1=dist;
							distfromCompany1=distfromCompany;
		
						}
					}
				}
				if(dist1<5){
						GeoTagDto dto = new GeoTagDto();
						dto.setEmpid(rs.getString("id"));
						dto.setEmpName(rs.getString("displayname"));
						dto.setPersonnelNo(rs.getString("personnelno"));
						dto.setDistanceperadmin(dist1);
						dto.setDistanceperadminout(distfromCompany1);
						dto.setHomelat(rs.getString("emp_lat"));
						dto.setHomelong(rs.getString("emp_long"));
						list.add(dto);
			}
			}
			Double[] array= new Double[list.size()];
			int i=0;
			for (GeoTagDto dt : list) {
			
				array[i]=dt.getDistanceperadminout();
				i++;
			}
			Arrays.sort(array);
			for(int k=array.length-1;k>=0;k--){
				for (GeoTagDto dt : list) {
					if(array[k].toString().equalsIgnoreCase(Double.toString(dt.getDistanceperadminout()))){
						int cnt=0;
						for(GeoTagDto dto1 :list1){
							if(dto1.getEmpid().equalsIgnoreCase(dt.getEmpid())){
								cnt++;
							}
						}
						if(cnt==0){
						GeoTagDto dto = new GeoTagDto();
						dto.setEmpid(dt.getEmpid());
						dto.setEmpName(dt.getEmpName());
						dto.setPersonnelNo(dt.getPersonnelNo());
						dto.setDistanceperadmin(dt.getDistanceperadmin());
						dto.setDistanceperadminout(dt.getDistanceperadminout());
						dto.setHomelat(dt.getHomelat());
						dto.setHomelong(dt.getHomelong());
						list1.add(dto);
						}
					}
				}
			}
		} catch (Exception e) {
			System.out.println("Error ShuttleSocketDao@getEmployeeNearNoodlePointsWithLog2 " + e);
		//	e.printStackTrace();

		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);
		}
		return list1;


	}

	public int setShuttlePickUpDrop1(EmployeeDto dto,String Interest) {
		DbConnect ob = DbConnect.getInstance();
		Connection con = null;
		Statement st=null,st1=null;
		PreparedStatement pst=null;
		ResultSet rs=null;
		int result=0;
		try{
			
			con=ob.connectDB();
			
		
			String selectqry="select empid from employee_tripping where empid="+dto.getEmployeeID();
			st=con.createStatement();
			rs=st.executeQuery(selectqry);
			if(rs.next()){
				String updateqry="UPDATE employee_tripping SET log_in='"+dto.getLogin()+"', log_out='"+dto.getLogout()+"' WHERE empid='"+dto.getEmployeeID()+"';";
				st1=con.createStatement();
				result=st1.executeUpdate(updateqry);
			}else{
			String qry="INSERT INTO employee_tripping (empId, log_in, log_out,siteId,interest) VALUES ( ?, ?, ?, ?, ?)";
			pst=con.prepareStatement(qry);
			pst.setString(1, dto.getEmployeeID());
			pst.setString(2, dto.getLogin());
			pst.setString(3, dto.getLogout());
			pst.setString(4, dto.getSite());
			pst.setString(5, Interest);
			result=pst.executeUpdate();
			}
			
		} catch (Exception e) {

			System.out.println("Error ShuttleSocketDao@setShuttlePickUpDrop1 " + e);

		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(pst,st,st1);
			DbConnect.closeConnection(con);
		}
		return result;
	}

}
