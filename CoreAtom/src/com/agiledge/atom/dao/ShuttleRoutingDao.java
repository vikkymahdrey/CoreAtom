package com.agiledge.atom.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import com.agiledge.atom.dbConnect.DbConnect;
import com.agiledge.atom.dto.EmployeeDto;
import com.agiledge.atom.dto.GeneralShiftDTO;
import com.agiledge.atom.dto.TripDetailsChildDto;

public class ShuttleRoutingDao {

	public int createTrip(String siteId, String date, String inOut,
			String inOutTime) {
		DbConnect ob = DbConnect.getInstance();
		Connection con = ob.connectDB();
		PreparedStatement pst = null;
		ResultSet rs = null;
		int retVal = 0;
		try {
			ArrayList<GeneralShiftDTO> list = new ArrayList<GeneralShiftDTO>();
			GeneralShiftDTO dto = null;
			String subquery = "";
			String subquery1 = "";
			if (inOut.equalsIgnoreCase("all")) {
				subquery1 = "  and effectiveDate<='" + date + "'";
			} else if (inOutTime.equalsIgnoreCase("all")) {
				subquery = " and  sr.siteid=" + siteId + " and sr.in_out='"
						+ inOut + "'";
				subquery1 += " and effectiveDate<='" + date + "' and in_out='"
						+ inOut + "'";
			} else {
				subquery += " and  sr.siteid=" + siteId + " and sr.in_out='"
						+ inOut + "' and sv.inOutTime='" + inOutTime + "'";
				subquery1 += " and effectiveDate<='" + date + "' and in_out='"
						+ inOut + "' and inOutTime='" + inOutTime + "'";
			}
			String query = "select sv.routeid,sr.in_out,vt.id,vt.type,vt.sit_cap,sv.inOutTime,sv.count from shuttlevehicle sv,vehicle_type vt,Routeshuttle sr  where sr.id=sv.routeid and sv.vehicletype=vt.id "
					+ subquery
					+ " order by sv.routeid,sv.inOutTime,vt.sit_cap,sv.id ";
			pst = con.prepareStatement(query);
			rs = pst.executeQuery();
			while (rs.next()) {
				for (int i = 0; i < rs.getInt("count"); i++) {
					dto = new GeneralShiftDTO();
					dto.setRouteId(rs.getString("routeid"));
					dto.setVehicleTypeId(rs.getString("id"));
					dto.setVehicleType(rs.getString("type"));
					dto.setDate(date);
					dto.setSite_id(siteId);
					dto.setVehicleSeat(rs.getString("sit_cap"));
					dto.setLogtime(rs.getString("inOutTime"));
					dto.setLogtype(rs.getString("in_out"));
					dto.setCount(rs.getInt("count"));
					dto.setDate(date);
					list.add(dto);
				}
			}
			/*
			 * for (GeneralShiftDTO dto1 : list) {
			 * System.out.println("routeid :"+ dto1.getRouteId()+" inOut :"+
			 * dto1.getLogtype()+"  logTime:"+dto1.getLogtime()+"  seat:"+dto.
			 * getVehicleSeat()+"  type:"+dto1.getVehicleTypeId()); }
			 */

			query = "select shuttlebooking.* from shuttlebooking where  status in('a','cr') "
					+ subquery1 + " order by routeid, inOutTime";
			pst = con.prepareStatement(query);
			rs = pst.executeQuery();
			ArrayList<TripDetailsChildDto> emplist = new ArrayList<TripDetailsChildDto>();

			while (rs.next()) {
				TripDetailsChildDto empDto = new TripDetailsChildDto();
				empDto.setRouteId(rs.getString("routeId"));
				empDto.setInOutTime(rs.getString("inOutTime"));
				empDto.setEmployeeId(rs.getString("employeeid"));
				empDto.setLandmark(rs.getString("landmark"));
				empDto.setTripId(rs.getString("routeid"));
				emplist.add(empDto);
			}

			for (GeneralShiftDTO gendto : list) {
				System.out.println(" RouteId:" + gendto.getRouteId()
						+ "  LogTime:" + gendto.getLogtime());
				ArrayList<TripDetailsChildDto> selectedEmp = new ArrayList<TripDetailsChildDto>();
				gendto.setEmpDtoList(selectedEmp);
				for (int i = 0; i < emplist.size()
						&& i <= Integer.parseInt(gendto.getVehicleSeat()); i++) {
					System.out.println(" Emp RouteId:"
							+ emplist.get(i).getRouteId() + "  Emp LogTime:"
							+ emplist.get(i).getInOutTime() + "  empId"
							+ emplist.get(i).getEmployeeId());
					if (gendto.getRouteId().equals(emplist.get(i).getRouteId())
							&& gendto.getLogtime().equals(
									emplist.get(i).getInOutTime())) {
						System.out.println(emplist.get(i).getEmployeeId());
						selectedEmp.add(emplist.get(i));
						emplist.remove(i);
						i--;
					} else {
						break;
					}

				}
			}

			retVal = insertTrip(list);

		} catch (Exception e) {
			System.out.println("Error in shuttle routing 1" + e);
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);
		}
		return retVal;
	}

	private int insertTrip(ArrayList<GeneralShiftDTO> list) {
		DbConnect ob = DbConnect.getInstance();
		Connection con = ob.connectDB();
		PreparedStatement pst = null;
		ResultSet rs = null;
		int retVal = 0;
		try {

			for (GeneralShiftDTO dto : list) {
				System.out.println("routeId: " + dto.getRouteId()
						+ "    Log Type: " + dto.getLogtype() + "   log time"
						+ dto.getLogtime() + "    vehicleid"
						+ dto.getVehicleTypeId());
				for (TripDetailsChildDto empDto : dto.getEmpDtoList()) {
					System.out.println("empId " + empDto.getEmployeeId()
							+ " landmark " + empDto.getLandmark());
				}
			}

			String query = "insert into shuttletrip (siteId,date,routeid,in_out,inOutTime,vehicletype,status) values (?,?,?,?,?,?,?) ";
			pst = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			for (GeneralShiftDTO dto : list) {
				pst.setString(1, dto.getSite_id());
				pst.setString(2, dto.getDate());
				pst.setString(3, dto.getRouteId());
				pst.setString(4, dto.getLogtype());
				pst.setString(5, dto.getLogtime());
				pst.setString(6, dto.getVehicleTypeId());
				pst.setString(7, "routed");
				pst.executeUpdate();
				ResultSet rs1 = pst.getGeneratedKeys();
				if (rs1.next()) {
					String tripChildQuery = "insert into shuttletripemployee(shuttletripid,employeeid,landmarkid,status) values (?,?,?,?)";
					PreparedStatement pst1 = con
							.prepareStatement(tripChildQuery);
					for (TripDetailsChildDto empDto : dto.getEmpDtoList()) {
						pst1.setString(1, rs1.getString(1));
						pst1.setString(2, empDto.getEmployeeId());
						pst1.setString(3, empDto.getLandmark());
						pst1.setString(4, "routed");
						retVal += pst1.executeUpdate();
					}
				}

			}

		} catch (Exception e) {
			System.out.println("Error" + e);
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);
		}
		return retVal;
	}

	public ArrayList<GeneralShiftDTO> getShuttleTrip(String siteid,
			String date, String inOut, String inOutTime) {
		DbConnect ob = DbConnect.getInstance();
		Connection con = ob.connectDB();
		PreparedStatement pst = null;
		ResultSet rs = null;
		ArrayList<GeneralShiftDTO> list = new ArrayList<GeneralShiftDTO>();
		try {
			String subquery1 = "";
			if (inOut.equalsIgnoreCase("all")) {
				subquery1 = "siteid=" + siteid + " and   date='" + date + "'";
			} else if (inOutTime.equalsIgnoreCase("all")) {

				subquery1 += " siteid=" + siteid + " and  date='" + date
						+ "' and in_out='" + inOut + "'";
			} else {
				subquery1 += " siteid=" + siteid + " and  date='" + date
						+ "' and in_out='" + inOut + "' and inOutTime='"
						+ inOutTime + "'";
			}
			String query = "select date,st.id,routeid,in_out,inOutTime,vt.type,status from shuttletrip st,vehicle_type vt where st.vehicletype=vt.id  and "
					+ subquery1;
			pst = con.prepareStatement(query);
			rs = pst.executeQuery();
			while (rs.next()) {
				GeneralShiftDTO dto = new GeneralShiftDTO();
				dto.setRouteId(rs.getString("routeid"));
				dto.setLogtype(rs.getString("in_out"));
				dto.setLogtime(rs.getString("inOutTime"));
				dto.setVehicleType(rs.getString("type"));
				String tripChildQuery = "select  e.displayname,e.personnelno,a.area,p.place,l.landmark from employee e, shuttletripemployee se,area a,placeShuttle p,landmarkShuttle l where se.shuttletripid="
						+ rs.getString("id")
						+ " and se.employeeid=e.id and se.landmarkid=l.id and l.place=p.id and p.area=a.id";
				PreparedStatement pst1 = con.prepareStatement(tripChildQuery);
				ResultSet rs1 = pst1.executeQuery();
				ArrayList<TripDetailsChildDto> emplist = new ArrayList<TripDetailsChildDto>();
				boolean flag = false;
				while (rs1.next()) {
					flag = true;
					TripDetailsChildDto empDto = new TripDetailsChildDto();
					empDto.setEmployeeId(rs1.getString("personnelno"));
					empDto.setEmployeeName(rs1.getString("displayname"));
					empDto.setArea(rs1.getString("area"));
					empDto.setPlace(rs1.getString("place"));
					empDto.setLandmark(rs1.getString("landmark"));
					emplist.add(empDto);
				}
				dto.setEmpDtoList(emplist);
				if (flag) {
					list.add(dto);
				}
			}
		} catch (Exception e) {
			System.out.println("Error" + e);
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);
		}
		return list;
	}

	public int checkTripExist(String siteid, String date, String inOut,
			String inOutTime) {
		DbConnect ob = DbConnect.getInstance();
		Connection con = ob.connectDB();
		PreparedStatement pst = null;
		ResultSet rs = null;
		int retVal = 0;
		try {
			String subquery1 = "";
			if (inOut.equalsIgnoreCase("all")) {
				subquery1 = "siteid=" + siteid + " and   date='" + date + "'";
			} else if (inOutTime.equalsIgnoreCase("all")) {

				subquery1 += " siteid=" + siteid + " and  date='" + date
						+ "' and in_out='" + inOut + "'";
			} else {
				subquery1 += " siteid=" + siteid + " and  date='" + date
						+ "' and in_out='" + inOut + "' and inOutTime='"
						+ inOutTime + "'";
			}
			String query = "select date,st.id,routeid,in_out,inOutTime,vt.type,status from shuttletrip st,vehicle_type vt where st.vehicletype=vt.id  and "
					+ subquery1;
			pst = con.prepareStatement(query);
			rs = pst.executeQuery();
			while (rs.next()) {
				retVal = 1;
			}
		} catch (Exception e)

		{
			System.out.println("Error" + e);
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);
		}
		return retVal;
	}

}
