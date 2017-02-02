package com.agiledge.atom.dao;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.hibernate.sql.Insert;
import org.json.JSONObject;

import com.agiledge.atom.commons.OtherFunctions;
import com.agiledge.atom.constants.SettingsConstant;
import com.agiledge.atom.dbConnect.DbConnect;
import com.agiledge.atom.dto.APLDto;
import com.agiledge.atom.dto.AdhocDto;
import com.agiledge.atom.dto.EmployeeDto;
import com.agiledge.atom.dto.EmployeeSubscriptionDto;
import com.agiledge.atom.dto.GeneralShiftDTO;
import com.agiledge.atom.dto.RouteDto;
import com.agiledge.atom.reports.dto.SubscriptionReportDto;

public class ShuttleDao {
	public boolean isSubscriptionRequestMade(String empid) {
		DbConnect ob = DbConnect.getInstance();
		Statement st = null;

		boolean returnValue = false;

		Connection con = ob.connectDB();
		// PreparedStatement pst = null;
		try {
			// Connection pooling implementation

			// replace ob.connectDB() with Connection con=ob.connectDB();
			// replace ob.con with con
			String query = "select * from shuttleBooking where employeeId='"
					+ empid + "' and status in ('"
					+ SettingsConstant.ACTIVE_STATUS + "', '"
					+ SettingsConstant.WAITING_LIST + "','"
					+ SettingsConstant.CANCEL_REQUESTED + "')";
			st = con.createStatement();
			ResultSet rs = st.executeQuery(query);
			if (rs.next()) {
				returnValue = true;
			}
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);
			// System.out.println(" " + returnValue);

		} catch (Exception e) {
			DbConnect.closeConnection(con);
			System.out
					.println("Error in isshuttleSubcribed in SubscriptionDao : "
							+ e);
		}

		return returnValue;

	}

	public String subscribe(EmployeeSubscriptionDto dto, String inOut) {
		DbConnect ob = DbConnect.getInstance();
		Statement st = null;
		ResultSet rs = null;

		Connection con = ob.connectDB();
		PreparedStatement pst = null;
		String status = "";
		String returnStatus = "";
		try {
			dto.setSubscriptionFromDate(OtherFunctions
					.changeDateFromatToIso(dto.getSubscriptionFromDate()));

			// Connection pooling implementation

			// replace ob.connectDB() with Connection con=ob.connectDB();
			// replace ob.con with con
			String routeId = "";
			String time = "";
			if (inOut.equalsIgnoreCase("IN")) {
				routeId = dto.getInRoute();
				time = dto.getShiftIn();
			} else {
				routeId = dto.getOutRoute();
				time = dto.getShiftOut();
			}
			String query1 = "select sv.id,ifnull(sum(vt.sit_cap*sv.count),0)-(select count(*) from shuttleBooking where routeId="
					+ routeId
					+ " and siteid="
					+ dto.getSite()
					+ " and status in ('"
					+ SettingsConstant.ACTIVE_STATUS
					+ "','"
					+ SettingsConstant.WAITING_LIST
					+ "')) as seats ,(select count(*) from shuttlebooking where status in('"
					+ SettingsConstant.ACTIVE_STATUS
					+ "','"
					+ SettingsConstant.WAITING_LIST
					+ "') and employeeId="
					+ dto.getEmployeeID()
					+ " and in_out='"
					+ inOut
					+ "') as previousBooking from vehicle_type vt,shuttleVehicle sv where sv.vehicleType=vt.id and routeId="
					+ routeId + " and sv.inOutTime='" + time + "'";
			String query = "insert into shuttleBooking ( siteId,employeeId,   effectiveDate, bookedDate, routeId, landmark,status,waitingList,in_out,approvalStatus,inOutTime) values (?,?,?,now(),?,?,?,?,?,?,?)";
			st = con.createStatement();
			rs = st.executeQuery(query1);
			pst = con.prepareStatement(query);
			if (rs.next()) {
				if (rs.getInt("previousBooking") == 0) {
					pst.setString(1, dto.getSite());
					pst.setString(2, dto.getEmployeeID());
					pst.setString(3, dto.getSubscriptionFromDate());
					pst.setString(4, routeId);
					pst.setString(5, dto.getLandMark());
					if (rs.getInt("seats") <= 0) {
						status = SettingsConstant.WAITING_LIST;
						pst.setString(6, SettingsConstant.WAITING_LIST);
						returnStatus = "On Waiting List " + (-1)
								* (rs.getInt("seats") - 1);
					} else {
						status = SettingsConstant.ACTIVE_STATUS;
						pst.setString(6, SettingsConstant.ACTIVE_STATUS);
						returnStatus = "Success";
					}
					pst.setInt(7, rs.getInt("seats") - 1);
					pst.setString(8, inOut);
					pst.setString(9, dto.getApprovalStatus());
					pst.setString(10, time);
					pst.executeUpdate();
					/*
					 * if(status.equals(SettingsConstant.ACTIVE_STATUS)&&dto.
					 * getApprovalStatus
					 * ().equalsIgnoreCase(SettingsConstant.APPROVED)) {
					 * pst=con.prepareStatement(
					 * "insert into shuttlevehicleemployee(shuttlevehicleid,employeeid,landmarkid,status) values (?,?,?,?)"
					 * ); pst.setString(1, rs.getString("id")); pst.setString(2,
					 * dto.getEmployeeID()); pst.setString(3,
					 * dto.getLandMark()); pst.setString(4,
					 * SettingsConstant.ACTIVE_STATUS); pst.executeUpdate();
					 * pst=con.prepareStatement(
					 * "update shuttlevehicle set availableseat=availableseat-1 where id="
					 * +rs.getString("id")); pst.executeUpdate(); }
					 */
				} else {
					returnStatus = "Booking Exist";
				}
			}

		} catch (Exception e) {

			System.out.println("Error in DAO-> SubscribeEmployee  subscribe : "
					+ e);

		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st, pst);
			DbConnect.closeConnection(con);
		}
		System.out.println("HEREEEEEEEEEEEEEEEE" + status);
		return returnStatus;
	}

	public ArrayList<RouteDto> getShuttleVehicles(String routeId,
			String shiftTime) {
		DbConnect ob = DbConnect.getInstance();
		Statement st = null;

		boolean returnValue = false;

		Connection con = ob.connectDB();
		ResultSet rs = null;
		ArrayList<RouteDto> shuttleVehicles = new ArrayList<RouteDto>();
		try {
			String query = "SELECT vt.sit_cap,vt.type as type, sv.* FROM vehicle_type vt left outer join shuttleVehicle sv  on sv.vehicleType=vt.id  where routeId='"
					+ routeId + "' and inOutTime ='" + shiftTime + "'";
			System.out.println(query);
			st = con.createStatement();
			rs = st.executeQuery(query);
			while (rs.next()) {
				RouteDto dto = new RouteDto();
				dto.setRouteId(rs.getInt("routeId"));
				dto.setVehicleType(rs.getString("type"));
				dto.setVehicleTypeId(rs.getString("vehicleType"));
				dto.setInOut(rs.getString("inOutTime"));
				dto.setVehicleCount(rs.getString("count"));
				dto.setSeats(rs.getString("sit_cap"));
				shuttleVehicles.add(dto);
			}
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);
			// System.out.println(" " + returnValue);

		} catch (Exception e) {
			System.out.println("Error in Get Shuttle  Route Vehicle : " + e);
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);
		}
		return shuttleVehicles;

	}

	public ArrayList<EmployeeSubscriptionDto> getShuttleBookingDatiles(
			String empId) {
		DbConnect ob = DbConnect.getInstance();
		Statement st = null;

		Connection con = ob.connectDB();
		ArrayList<EmployeeSubscriptionDto> shuttlebooking = new ArrayList<EmployeeSubscriptionDto>();
		ResultSet rs = null;
		try {
			String query = "SELECT sb.*,sr.routeName,e.displayname,a.area,p.place,l.landmark as landmarkName FROM shuttlebooking sb,routeShuttle sr,employee e,area a,placeshuttle p,landmarkShuttle l where  e.id=sb.employeeId and sb.employeeId="
					+ empId
					+ " and sb.status in ('"
					+ SettingsConstant.WAITING_LIST
					+ "','"
					+ SettingsConstant.ACTIVE_STATUS
					+ "','"
					+ SettingsConstant.CANCEL_REQUESTED
					+ "') and sb.routeId=sr.id and sb.landmark=l.id and l.place=p.id and p.area=a.id";

			st = con.createStatement();
			rs = st.executeQuery(query);
			while (rs.next()) {
				EmployeeSubscriptionDto dto = new EmployeeSubscriptionDto();
				dto.setSubscriptionID(rs.getString("id"));
				dto.setEmployeeID(rs.getString("employeeid"));
				dto.setSubscriptionFromDate(rs.getString("effectiveDate"));
				dto.setRouteName(rs.getString("routeName"));
				EmployeeDto empDto = new EmployeeDto();
				empDto.setDisplayName(rs.getString("displayname"));
				dto.setEmployee(empDto);
				dto.setSubscriptionStatus(rs.getString("status"));
				dto.setWaitingList(rs.getInt("waitingList"));
				dto.setApprovalStatus(rs.getString("approvalStatus"));
				dto.setIN_OUT(rs.getString("in_out"));
				dto.setShiftIn(rs.getString("inOutTime"));
				APLDto aplDto = new APLDto();
				aplDto.setArea(rs.getString("area"));
				aplDto.setPlace(rs.getString("place"));
				aplDto.setLandMark(rs.getString("landmarkName"));
				dto.setApl(aplDto);
				dto.setReConfirmDate(rs.getString("reconfirmDate"));
				shuttlebooking.add(dto);
			}
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);
		} catch (Exception e) {
			System.out.println("Error in Get Shuttle  Route Vehicle : " + e);
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);
		}

		return shuttlebooking;

	}

	public int cancelRequest(String bookingId, String date) {
		DbConnect ob = DbConnect.getInstance();
		PreparedStatement pst = null;
		int retVal = 0;
		Connection con = ob.connectDB();
		ResultSet rs = null;
		try {
			String query = "insert into shuttlecancel (bookingId,requestedDate,effectiveDate,status) values (?,?,curdate(),?)";
			pst = con.prepareStatement(query);
			pst.setString(1, bookingId);
			pst.setString(2, date);
			pst.setString(3, "r");
			retVal = pst.executeUpdate();
			query = "update shuttleBooking set status='"
					+ SettingsConstant.CANCEL_REQUESTED + "' where id="
					+ bookingId;
			pst = con.prepareStatement(query);
			retVal += pst.executeUpdate();
		} catch (Exception e) {
			System.out.println("error" + e);
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);
		}

		return retVal;
	}

	public int cancelBooking(String bookingId, String date) {
		DbConnect ob = DbConnect.getInstance();
		PreparedStatement pst = null;
		int retVal = 0;
		Connection con = ob.connectDB();
		ResultSet rs = null;
		try {
			con.setAutoCommit(false);
			String query = "select waitinglist,routeId,inOutTime,(select cutoff_waitlist from general_Shift_Config where status='a' and waitlist_reconf='y') as waitinglistDays from shuttleBooking where id="
					+ bookingId;
			pst = con.prepareStatement(query);
			rs = pst.executeQuery();
			EmployeeSubscriptionDto dto = new EmployeeSubscriptionDto();
			if (rs.next()) {
				dto.setWaitingList(rs.getInt("waitinglist"));
				dto.setInRoute(rs.getString("routeId"));
				dto.setShiftIn(rs.getString("inOutTime"));
				dto.setWaitingListDays(rs.getInt("waitinglistDays"));
			}

			query = "update shuttleBooking set status='"
					+ SettingsConstant.CANCELLED_STATUS + "' where id="
					+ bookingId + "";
			pst = con.prepareStatement(query);
			retVal = pst.executeUpdate();
			System.out.println(query);
			System.out.println(retVal);
			if (retVal > 0) {
				if (dto.getWaitingListDays() != 0) {

					query = "update shuttleBooking set waitinglist=waitinglist+1,reconfirmDate='"
							+ OtherFunctions.addDate(new Date(),
									dto.getWaitingListDays())
							+ "' where routeId="
							+ dto.getInRoute()
							+ " and inOutTime='"
							+ dto.getShiftIn()
							+ "' and waitinglist<" + dto.getWaitingList() + " ";
				} else {
					query = "update shuttleBooking set waitinglist=waitinglist+1 where routeId="
							+ dto.getInRoute()
							+ " and inOutTime='"
							+ dto.getShiftIn()
							+ "' and waitinglist<"
							+ dto.getWaitingList() + " ";
					reConfirmBooking(bookingId);
				}
				pst = con.prepareStatement(query);
				retVal = pst.executeUpdate();

			}
			con.commit();
		} catch (Exception e) {
			System.out.println("Error" + e);
		} finally {
			try {
				con.setAutoCommit(true);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);
		}
		return retVal;

	}

	public int cancelBookingRequest(String bookingId, String date) {
		DbConnect ob = DbConnect.getInstance();
		PreparedStatement pst = null;
		int retVal = 0;

		Connection con = ob.connectDB();
		ResultSet rs = null;
		try {
			String query = "insert into shuttleCancel(bookingId,requestedDate,effectiveDate) values (?,now(),?)"
					+ bookingId;
			pst = con.prepareStatement(query);
			pst.setString(1, bookingId);
			pst.setString(2, OtherFunctions.changeDateFromatToIso(date));
			retVal = pst.executeUpdate();
			query = "updateShuttleBooking set status='"
					+ SettingsConstant.CANCEL_REQUESTED + "' where id="
					+ bookingId + "";
			pst = con.prepareStatement(query);
			retVal += pst.executeUpdate();

			if (getWaitingListDays() == 0) {
				cancelBooking(bookingId, new Date().toString());
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

	public int cancelNotConfirmed(String date) {
		DbConnect ob = DbConnect.getInstance();
		PreparedStatement pst = null;
		int retVal = 0;
		Connection con = ob.connectDB();
		ResultSet rs = null;
		ArrayList<EmployeeSubscriptionDto> list = new ArrayList<EmployeeSubscriptionDto>();
		String idStrings = "";
		try {
			con.setAutoCommit(false);
			String query = "select * from shuttleBooking where reconfirmDate is not null and reconfirmdate<'"
					+ date + "'";
			pst = con.prepareStatement(query);
			rs = pst.executeQuery();
			while (rs.next()) {
				EmployeeSubscriptionDto dto = new EmployeeSubscriptionDto();
				dto.setSubscriptionID(rs.getString("id"));
				dto.setInRoute(rs.getString("routeid"));
				dto.setSubscriptionFromDate(rs.getString("effectiveDate"));
				dto.setShiftIn(rs.getString("inOutTime"));
				dto.setIN_OUT("in_out");
				dto.setWaitingList(rs.getInt("waitinglist"));
				list.add(dto);
			}
			query = "update shuttleBooking set status='"
					+ SettingsConstant.CANCELLED_STATUS
					+ "' where reconfirmDate is not null and reconfirmdate<'"
					+ date + "'";
			pst = con.prepareStatement(query);
			pst.executeUpdate();
			int waitingListDays = getWaitingListDays();
			if (waitingListDays != 0) {
				query = "update shuttleBooking set waitinglist=waitinglist+1,reconfirmDate='"
						+ OtherFunctions.addDate(new Date(), waitingListDays)
						+ "' where routeId=? and inOutTime=? and waitinglist<?";
			} else {
				query = "update shuttleBooking set waitinglist=waitinglist+1 "
						+ "' where routeId=? and inOutTime=? and waitinglist<?";
			}
			pst = con.prepareStatement(query);
			for (EmployeeSubscriptionDto dto : list) {
				idStrings += "," + dto.getSubscriptionID();
				pst.setString(1, dto.getInRoute());
				pst.setString(2, dto.getShiftIn());
				pst.setInt(3, dto.getWaitingList());
				retVal += pst.executeUpdate();
			}
			if (waitingListDays != 0) {
				idStrings = idStrings.substring(1);

				query = "update shuttleBooking set status='"
						+ SettingsConstant.ACTIVE_STATUS + "' where status='"
						+ SettingsConstant.WAITING_LIST
						+ "' and waitinglist>=0 and id in (" + idStrings + ")";
				pst = con.prepareStatement(query);
				retVal += pst.executeUpdate();

			}
			con.commit();
		} catch (Exception e) {
			System.out.println("Error" + e);
		} finally {
			try {
				con.setAutoCommit(true);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);
		}
		return retVal;
	}

	public int reConfirmBooking(String bookingId) {
		DbConnect ob = DbConnect.getInstance();
		PreparedStatement pst = null;
		int retVal = 0;

		Connection con = ob.connectDB();
		ResultSet rs = null;
		try {
			String query = "update shuttleBooking set status='"
					+ SettingsConstant.ACTIVE_STATUS + "' where status='"
					+ SettingsConstant.WAITING_LIST
					+ "' and waitinglist>=0 and id=" + bookingId;
			if (!bookingId.equals("0")) {
				query += " and id=" + bookingId;
			}
			pst = con.prepareStatement(query);
			retVal = pst.executeUpdate();
		} catch (Exception e) {
			System.out.println("Error" + e);
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);
		}
		return retVal;

	}

	public static int getWaitingListDays() {
		DbConnect ob = DbConnect.getInstance();
		PreparedStatement pst = null;
		int retVal = 0;
		ResultSet rs = null;
		Connection con = ob.connectDB();
		String query = "select cutoff_waitlist from general_Shift_Config where status='a' and waitlist_reconf='y'";
		try {

			pst = con.prepareStatement(query);
			rs = pst.executeQuery();
			if (rs.next()) {
				retVal = rs.getInt(1);
			}

		} catch (Exception e) {
			System.out.println("Exception " + e);

		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);
		}
		return retVal;
	}

	public int updateBookingWaitingList(RouteDto dto, int totalSeats) {
		DbConnect ob = DbConnect.getInstance();
		PreparedStatement pst = null;
		PreparedStatement pst1 = null;
		int retVal = 0;
		Connection con = ob.connectDB();
		ResultSet rs = null;
		String query = "", query1 = "";
		int waitingListDays = getWaitingListDays();
		try {
			if (waitingListDays != 0) {
				query = "update shuttleBooking set waitinglist=waitinglist+"
						+ totalSeats + ",reconfirmDate='"
						+ OtherFunctions.addDate(new Date(), waitingListDays)
						+ "' where routeId=? and inOutTime=? ";
			} else {
				query = "update shuttleBooking set waitinglist=waitinglist+"
						+ totalSeats + ""
						+ "' where routeId=? and inOutTime=? ";
				query1 = "update shuttleBooking set status='"
						+ SettingsConstant.ACTIVE_STATUS + "' where status='"
						+ SettingsConstant.WAITING_LIST
						+ "' and waitinglist>=0 and routeId=? and inOutTime=?";
			}
			pst = con.prepareStatement(query);
			pst.setInt(1, dto.getRouteId());
			pst.setString(2, dto.getInOut());
			retVal += pst.executeUpdate();
			if (waitingListDays == 0) {
				pst1 = con
						.prepareStatement("select * from shuttleBooking where status='"
								+ SettingsConstant.WAITING_LIST
								+ "' and waitinglist>=0 and routeId=? and inOutTime=?");
				pst1.setInt(1, dto.getRouteId());
				pst1.setString(2, dto.getInOut());
				rs = pst1.executeQuery();
				pst1 = con.prepareStatement(query1);
				pst1.setInt(1, dto.getRouteId());
				pst1.setString(2, dto.getInOut());
				retVal += pst1.executeUpdate();

			}
		} catch (Exception e) {
			System.out.println("Exception " + e);

		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);
		}
		return retVal;
	}

	public int getTotalbookings(String routeId, String inOutTime) {

		DbConnect ob = DbConnect.getInstance();
		PreparedStatement pst = null;
		int retVal = 0;
		ResultSet rs = null;
		Connection con = ob.connectDB();
		String query = "select count(*) from shuttleBooking where routeId="
				+ routeId + " and inOutTime='" + inOutTime
				+ "' and status in ('" + SettingsConstant.WAITING_LIST + "' ,'"
				+ SettingsConstant.CANCEL_REQUESTED + "','"
				+ SettingsConstant.ACTIVE_STATUS + "')";
		try {

			pst = con.prepareStatement(query);
			rs = pst.executeQuery();
			if (rs.next()) {
				retVal = rs.getInt(1);
			}

		} catch (Exception e) {
			System.out.println("Exception " + e);

		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);
		}
		return retVal;
	}

	public ArrayList<GeneralShiftDTO> getBookingDetailsForManager(
			String managerId) {
		DbConnect ob = DbConnect.getInstance();
		ResultSet rs = null;
		Connection con = null;
		GeneralShiftDTO dto = null;
		ArrayList<GeneralShiftDTO> list = new ArrayList<GeneralShiftDTO>();

		PreparedStatement pst = null;
		try {
			con = ob.connectDB();
			String query = "select sb.*,e.site,e.projectUnit,e.displayname,e.PersonnelNo from shuttleBooking sb,employee e where sb.employeeId=e.id  and (sb.employeeId in (select id from employee where LineManager="
					+ managerId
					+ ") or sb.employeeId in (select sc.employee_id from spoc_parent sp,spoc_child sc where sc.spoc_id=sp.id and sp.employee_id="
					+ managerId
					+ ")) and sb.status in ('"
					+ SettingsConstant.ACTIVE_STATUS
					+ "','"
					+ SettingsConstant.WAITING_LIST
					+ "','"
					+ SettingsConstant.CANCEL_REQUESTED
					+ "') order by sb.bookedDate desc";
			pst = con.prepareStatement(query);

			rs = pst.executeQuery();
			while (rs.next()) {
				dto = new GeneralShiftDTO();
				dto.setBookingId(rs.getString("id"));
				dto.setFrom_date(rs.getString("effectiveDate"));
				dto.setApproval_req(rs.getString("approvalStatus"));
				dto.setLogtype(rs.getString("in_out"));
				dto.setLogtime(rs.getString("inOutTime"));
				dto.setStatus(rs.getString("status"));
				dto.setEmployeeId(rs.getString("PersonnelNo"));
				dto.setEmployeeName(rs.getString("displayName"));
				list.add(dto);
			}

		} catch (Exception e) {
			System.out.println("Error in selecting adhoc types for emp 3 " + e);
		}

		finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);

		}
		return list;
		// TODO Auto-generated method stub
	}

	public int approveBooking(String bookingId) {
		DbConnect ob = DbConnect.getInstance();
		Connection con = null;
		int retVal = 0;
		PreparedStatement pst = null;
		try {
			con = ob.connectDB();
			String query = "update shuttleBooking set approvalstatus='"
					+ SettingsConstant.APPROVED + "' where id=" + bookingId;
			pst = con.prepareStatement(query);
			retVal = pst.executeUpdate();

		} catch (Exception e) {
			System.out.println("Error in selecting adhoc types for emp 3 " + e);
		}

		finally {
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);

		}

		// TODO Auto-generated method stub

		return retVal;
	}

	public String createShuttle(String deviceNo, String logType, String logTime) {

		DbConnect ob = DbConnect.getInstance();
		Connection con = null;
		ResultSet rs = null, rs1 = null;
		Statement st = null, st1 = null, st2 = null, st3 = null;
		PreparedStatement pst = null, pst1 = null, pst2 = null;
		con = ob.connectDB();
		int tripId = 0;
		String tid = "";
		String siteId = "1";
		String vendor = "1";
		try {
			st = con.createStatement();
			st1 = con.createStatement();
			String tripCode = "";
			// stopping Last trip
			stopShuttle(deviceNo);
			// get tripCode
			Date date = new Date();
			String currDate = OtherFunctions.changeDateFromatToSqlFormat(date);
			TripDetailsDao dao = new TripDetailsDao();
			dao.createUniqueID(currDate, logType, siteId);
			dao.getIncremenetedUnique();
			tripCode = dao.uniqueId;

			// creating Trip
			String createTDQuery = "INSERT INTO trip_details (trip_code, siteId, trip_date, trip_time, trip_log, routeId, vehicle_type, status, security_status, vehicle, routingType) VALUES ('"
					+ tripCode
					+ "', "
					+ siteId
					+ ", current_date(), '"
					+ logTime
					+ "', '"
					+ logType
					+ "', (SELECT vs.routeId FROM vehicle_shift_map vs,logtime l,vehicles v where vs.shiftId=l.id and l.logtime='"
					+ logTime
					+ "' and l.logtype='"
					+ logType
					+ "' and l.status='active' and vs.vehicleId=v.id and v.device_num='"
					+ deviceNo
					+ "' order by vs.id desc limit 1), (select vehicletype from vehicles where device_num='"
					+ deviceNo
					+ "'), 'routed', 'NO', (select id from vehicles where device_num='"
					+ deviceNo + "'), 'o')";
			st1.executeUpdate(createTDQuery, Statement.RETURN_GENERATED_KEYS);
			rs = st1.getGeneratedKeys();
			if (rs.next()) {
				tripId = rs.getInt(1);
			}

			// Assigning to Vendor
			String assignVendor = "INSERT INTO tripvendorassign (tripId, vendorId, status) VALUES (?, ?, ?);";
			pst = con.prepareStatement(assignVendor);
			pst.setInt(1, tripId);
			pst.setString(2, vendor);
			pst.setString(3, "a");
			pst.executeUpdate();

			// Creating vendorTripSheet
			String createVTPSQuery = "INSERT INTO vendor_trip_sheet_parent (tripId, logTime,vehicleNo, status, startTime) VALUES ('"
					+ tripId
					+ "', curtime(),((select id from vehicles where device_num='"
					+ deviceNo + "')), 'started', curtime())";
			st2 = con.createStatement();
			st2.executeUpdate(createVTPSQuery);
			tid = "" + tripId;
			SettingsConstant.deviceMap.put(deviceNo, tid);
			SettingsConstant.tripMap.put(tid, "run");

			// Inserting into trip child
			int inserOrder = 1;
			String getEmployees = null;
			if (logType.equalsIgnoreCase("IN")) {
				getEmployees = "select e.id,et.ePick as lid,e.rfid_code from employee e, employee_tripping et,trip_details td,routechild rc where e.id=et.empid and et.routeIn=td.routeId and rc.routeId=td.routeId and td.id='"
						+ tripId + "' order by rc.position";
			} else {
				getEmployees = "select e.id,et.eDrop as lid,e.rfid_code from employee e, employee_tripping et,trip_details td,routechild rc where e.id=et.empid and et.routeOut=td.routeId and rc.routeId=td.routeId and td.id='"
						+ tripId + "' order by rc.position";
			}
			st3 = con.createStatement();
			rs1 = st3.executeQuery(getEmployees);
			while (rs1.next()) {

				String distance = Float.toString(getDistance(inserOrder, tid,
						logType, rs1.getString("rfid_code")));
				String time = Double.toString(getTime(distance));
				pst1 = con
						.prepareStatement("INSERT INTO trip_details_child (tripId, employeeId, routedOrder, landmarkId, time, dist, transportType) VALUES (?, ?, ?,?, ?, ?, 'shift')");
				pst1.setString(1, tid);
				pst1.setString(2, rs1.getString("id"));
				pst1.setInt(3, inserOrder);
				pst1.setString(4, rs1.getString("lid"));
				pst1.setString(5, time);
				pst1.setString(6, distance);
				pst1.executeUpdate();

				pst2 = con
						.prepareStatement("INSERT INTO vendor_trip_sheet (tripId, employeeId, showStatus, insertedOrder) VALUES (?, ?, ?, ?)");
				pst2.setString(2, tid);
				pst2.setString(2, rs1.getString("id"));
				pst2.setString(3, "No Show");
				pst2.setInt(4, inserOrder);

				inserOrder++;
			}
			// inserting into vendor child

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("Error in ShuttleDao@createShuttle" + e);
		} finally {
			DbConnect.closeResultSet(rs, rs1);
			DbConnect.closeStatement(st, st1, st2, st3, pst, pst1, pst2);
			DbConnect.closeConnection(con);

		}
		return tid;
	}

	private double getDistanceBasedOnLatLOng(String tripId) {

		DbConnect ob = DbConnect.getInstance();
		Connection con = null;
		ResultSet rs = null;
		Statement st = null;
		int i = 0;
		double distance = 0;
		double sourceLat = 0, sourceLong = 0, destLat = 0, destLong = 0;
		String getLatLong = "(select lattitude,longitude,id from vehicle_position where tripId='"
				+ tripId
				+ "' order by id limit 1) union (select lattitude,longitude,id from vehicle_position where tripId='"
				+ tripId + "' order by id desc limit 1)";
		try {
			con = ob.connectDB();
			st = con.createStatement();
			rs = st.executeQuery(getLatLong);
			while (rs.next()) {
				if (i == 0) {
					sourceLat = Double.parseDouble(rs.getString("lattitude"));
					sourceLong = Double.parseDouble(rs.getString("longitude"));
					i++;
				} else {
					destLat = Double.parseDouble(rs.getString("lattitude"));
					destLong = Double.parseDouble(rs.getString("longitude"));
				}
			}
			// getDistanceWithoutMap
			distance = new DistanceListDao().getDistanceWithoutMap(sourceLat,
					sourceLong, destLat, destLong);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("Error in ShuttleDao@getDistanceBasedOnLatLOng"
					+ e);
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);

		}
		return distance;
	}

	public int stopShuttle(String deviceNo) {
		DbConnect ob = DbConnect.getInstance();
		Connection con = null;
		ResultSet rs = null, rs1 = null;
		Statement st = null, st1 = null;
		int result = 0;
		try {

			con = ob.connectDB();
			st = con.createStatement();
			String lastTrip = "";
			if (lastTrip == null) {
				String getTrip = "select vtsp.tripId from vendor_trip_sheet_parent vtsp,vehicles v where vtsp.vehicleNo=v.id and vtsp.status='started' and v.device_num='"
						+ deviceNo + "' ";
				st1 = con.createStatement();
				rs1 = st1.executeQuery(getTrip);
				if (rs1.next()) {
					lastTrip = rs1.getString("tripId");
				}
			}
			if (lastTrip != null) {
				double distance = getDistanceBasedOnLatLOng(lastTrip);
				String stopOldTrip = "UPDATE vendor_trip_sheet_parent SET status='stopped',stopTime=curtime(), distanceCovered='"
						+ distance
						+ "' where vehicleNo=(select id from vehicles where device_num='"
						+ deviceNo
						+ "') and status='started' and tripId='"
						+ lastTrip + "'";
				result = st.executeUpdate(stopOldTrip);
				SettingsConstant.tripMap.remove(SettingsConstant.deviceMap
						.get(deviceNo));
				SettingsConstant.deviceMap.remove(deviceNo);
			}
		} catch (SQLException e) {
			System.out.println("Error in ShuttleDao@stopShuttle" + e);
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);
		}

		return result;
	}

	public float getDistance(int routedOrder, String tripId, String rfid,
			String logType) {
		float distance = 0;
		DbConnect ob = DbConnect.getInstance();
		Connection con = null;
		ResultSet rs = null;
		Statement st = null;
		con = ob.connectDB();
		String src = null, dest = null;
		try {
			if (logType.equalsIgnoreCase("IN")) {
				if (routedOrder > 1) {
					src = "(select landmarkId from trip_details_child where tripId='"
							+ tripId
							+ "' and routedOrder='"
							+ (routedOrder - 1) + "')";
					dest = "select et.ePick from employee_tripping et,employee e where et.empid=e.id and e.rfid_code='"
							+ rfid + "'";
				}
			} else {
				src = "(select landmark from site where id=1)";
				dest = "select et.eDrop from employee_tripping et,employee e where et.empid=e.id and e.rfid_code='"
						+ rfid + "'";
			}
			String getInDist = "select distance from distchart where (srcId="
					+ src + " and destId=" + dest + ") or (destId=" + src
					+ " and srcId=" + dest + ")";

			st = con.createStatement();
			rs = st.executeQuery(getInDist);
			if (rs.next()) {
				distance = rs.getFloat("distance");
			}

		} catch (SQLException e) {
			System.out.println("Error in ShuttleDao@getDistance" + e);
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);
		}

		return distance;
	}

	public float getTime(String distance) {

		DbConnect ob = DbConnect.getInstance();
		Connection con = null;
		ResultSet rs = null;
		Statement st = null;
		float time = 0;
		con = ob.connectDB();
		try {
			st = con.createStatement();
			String gettingTime = "select ROUND( "
					+ distance
					+ "/speedpkm*60,0)as time from timeSloat where CONCAT(startTime,':00') <= CONCAT(curtime(),'') and CONCAT(endTime,':00') > CONCAT(curtime(),'')";
			rs = st.executeQuery(gettingTime);
			if (rs.next()) {
				time = rs.getFloat("time");
			}

		} catch (Exception e) {
			System.out.println("Error in ShuttleDao@getTime" + e);
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);
		}
		return time;
	}
	/*
	 * public int updateShuttleVehicleEmployee() { DbConnect ob =
	 * DbConnect.getInstance(); PreparedStatement pst = null; int retVal = 0;
	 * 
	 * Connection con = ob.connectDB(); ResultSet rs = null; try {
	 * pst=con.prepareStatement(
	 * "insert into shuttlevehicleemployee(shuttlevehicleid,employeeid,landmarkid,status) values (?,?,?,?)"
	 * ); pst.setString(1, rs.getString("id")); pst.setString(2,
	 * dto.getEmployeeID()); pst.setString(3, dto.getLandMark());
	 * pst.setString(4, SettingsConstant.ACTIVE_STATUS); pst.executeUpdate();
	 * 
	 * }catch(Exception e) { System.out.println("Error in shtleemps"+e); }
	 * return 0; }
	 */
}
