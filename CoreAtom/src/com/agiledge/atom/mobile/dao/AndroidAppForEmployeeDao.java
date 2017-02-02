package com.agiledge.atom.mobile.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.agiledge.atom.commons.OtherFunctions;
import com.agiledge.atom.constants.SettingsConstant;
import com.agiledge.atom.dao.DistanceListDao;
import com.agiledge.atom.dao.PanicDao;
import com.agiledge.atom.dao.ScheduleAlterDao;
import com.agiledge.atom.dao.SettingsDoa;
import com.agiledge.atom.dao.TripDetailsDao;
import com.agiledge.atom.dbConnect.DbConnect;
import com.agiledge.atom.dto.AdhocDto;
import com.agiledge.atom.dto.EmployeeDto;
import com.agiledge.atom.dto.EmployeeICEDTO;
import com.agiledge.atom.dto.EmployeeSubscriptionDto.status;
import com.agiledge.atom.dto.ScheduleAlterDto;
import com.agiledge.atom.dto.ScheduledEmpDto;
import com.agiledge.atom.dto.PanicDto;
import com.agiledge.atom.dto.TripDetailsChildDto;
import com.agiledge.atom.dto.TripDetailsDto;
import com.agiledge.atom.mail.SendMail;
import com.agiledge.atom.mail.SendMailFactory;
import com.agiledge.atom.service.AdhocService;
import com.agiledge.atom.service.MailService;
import com.agiledge.atom.servlets.ScheduleEmployee;
import com.agiledge.atom.sms.SMSService;
import com.agiledge.atom.sms.SendSMS;
import com.agiledge.atom.sms.SendSMSFactory;

public class AndroidAppForEmployeeDao {

	public String mobileNocheck(String mobileno) {

		String result = "false";
		DbConnect ob = DbConnect.getInstance();
		Connection con = ob.connectDB();
		ResultSet rs = null;
		PreparedStatement pst = null;
		try {
			String query = "SELECT * FROM EMPLOYEE WHERE (CONTACTNUMBER1=? OR CONTACTNUMBER2=?) AND  ACTIVE=1";
			pst = con.prepareStatement(query);
			pst.setString(1, mobileno);
			pst.setString(2, mobileno);
			rs = pst.executeQuery();
			if (rs.next()) {
				result = "true";
			}
		} catch (Exception e) {
			e.printStackTrace();

			System.err.println("error 0");
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);
		}
		return result;

	}

	public String checkImei(String Imei) {

		String flag = "false";
		DbConnect ob = DbConnect.getInstance();
		Connection con = ob.connectDB();
		PreparedStatement pst = null;
		ResultSet rs = null;

		try {
			String query = "SELECT * FROM EMPLOYEE_IMEI WHERE IMEI='"+Imei+"' AND STATUS='a' AND AUTHENTICATE='YES'";
			pst = con.prepareStatement(query);
			
			rs = pst.executeQuery();
			if (rs.next()) {
				flag = "true";

			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);
		}

		return flag;
	}

	public String insertOTP(String mobileNO, String Imei, String otp) {

		int returnInt = 0;
		DbConnect ob = DbConnect.getInstance();
		Connection con = ob.connectDB();
		PreparedStatement pst = null, pst1 = null, pst2 = null;
		ResultSet rs = null, rs1 = null;
		try {
			String selectQuery = "SELECT * FROM EMPLOYEE_IMEI WHERE IMEI='"+Imei+"'";
			pst1 = con.prepareStatement(selectQuery);
			rs = pst1.executeQuery();
			if (rs.next()) {

				otp = rs.getString("otp");
				returnInt = 1;

				// some cenearios we need to updatethe data
			} else {
				String query = "INSERT INTO EMPLOYEE_IMEI(EMPID,IMEI,OTP,STATUS) VALUES((SELECT ID FROM EMPLOYEE WHERE (CONTACTNUMBER1=? OR CONTACTNUMBER2=?) AND ACTIVE=1),?,?,?)";
				pst = con.prepareStatement(query);
				pst.setString(1, mobileNO);
				pst.setString(2, mobileNO);
				pst.setString(3, Imei);
				pst.setString(4, otp);
				pst.setString(5, "a");
				returnInt = pst.executeUpdate();
			}
			if (returnInt > 0) {
				// sending sms
				new SMSService().sendOTPEmployeeMessage(mobileNO, otp);

				// sending Mail
				String query2 = "SELECT emailaddress,displayname FROM EMPLOYEE WHERE (CONTACTNUMBER1=? OR CONTACTNUMBER2=?)";
				pst2 = con.prepareStatement(query2);
				pst2.setString(1, mobileNO);
				pst2.setString(2, mobileNO);
				rs1 = pst2.executeQuery();
				if (rs1.next()) {
					String msg = "Dear "
							+ rs1.getString("displayname")
							+ ", <br/> <br/> "
							+ "Your One Time Password(OTP) is "
							+ otp
							+ ". Please use this OTP to login to ATOm safetravel application."
							+ "<br/><br/>"
							+ "Regards,<br/>"
							+ "Transport Team."
							+ " <br/><br/>"
							+ "<br/> <i><u>Note:</u> This is a system generated email. Please do not reply.</i>";

					SendMail mail = SendMailFactory.getMailInstance();

					mail.send(rs1.getString("emailaddress"),
							"Mobile Registration", msg);

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			
		} finally {
			DbConnect.closeResultSet(rs, rs1);
			DbConnect.closeStatement(pst, pst1, pst2);
			DbConnect.closeConnection(con);
		}
		return otp;
	}

	public EmployeeDto getEmployeeDetails(String imei) {
		EmployeeDto dto = new EmployeeDto();
		DbConnect ob = DbConnect.getInstance();
		String usertype = "emp";
		Connection con = ob.connectDB();
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			String query = "select e.id,e.personnelNo,e.displayname,e.gender,e.emailaddress,e.role_id, s.site_name from employee e join site s on s.id=e.site join employee_imei ei on ei.empid=e.id where status='a' and ei.imei='"+imei+"'";
			pst = con.prepareStatement(query);
			
			rs = pst.executeQuery();
			while (rs.next()) {
				dto.setEmployeeID(rs.getString("id"));
				dto.setPersonnelNo(rs.getString("personnelNo"));
				dto.setDisplayName(rs.getString("displayname"));
				dto.setGender(rs.getString("gender"));
				dto.setEmailAddress(rs.getString("emailaddress"));
				dto.setSite(rs.getString("site_name"));
				if (rs.getString("role_id").equalsIgnoreCase("2")
						|| rs.getString("role_id").equalsIgnoreCase("5")) // for
																			// admin
																			// app
				{
					usertype = "admin";
				}
				if (rs.getString("role_id").equalsIgnoreCase("4")
						|| rs.getString("role_id").equalsIgnoreCase("7")) {
					usertype = "manager";
				}
				dto.setUserType(usertype);

			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);
		}
		return dto;
	}

	public TripDetailsDto getTripDetails(String imei, String auth) {
		TripDetailsDto dto = new TripDetailsDto();
		DbConnect ob = DbConnect.getInstance();
		Connection con = ob.connectDB();
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			
			if (auth != null && auth.equalsIgnoreCase("yes")) {
			

					updateAuthenticate(imei);
					}
					String query = "select ei.empid as empid,td.id,td.trip_code,DATE_FORMAT(td.trip_date, \"%d-%m-%Y\") as trip_date,td.trip_time,td.trip_log,td.security_status,vtp.escort as escortstatus,vtp.status,v.regno,d.name as drivername,d.contact as drivercontact,e.name as escortname,e.phone as escortcontact,(select count(employeeid) from trip_details_child where tripid=td.id) as totalemps from trip_details td join trip_details_child tc on tc.tripid=td.id join employee_imei ei on ei.status='a' and ei.empid=tc.employeeid and ei.imei=? join vendor_trip_sheet_parent vtp on vtp.status!='initial' and vtp.tripid=td.id join vehicles v on v.id=td.vehicle left join escort e on e.id=td.escortid join driver d on d.id=td.driverid and td.trip_date between  DATE_ADD(CURDATE(),INTERVAL -1 DAY) and curdate() order by td.id desc  ";
					pst = con.prepareStatement(query);
					pst.setString(1, imei);
					rs = pst.executeQuery();
					System.out.println(query);
					if (rs.next()) {

						if (rs.getString("status").equalsIgnoreCase("started")) {

							dto.setId(rs.getString("id"));
							dto.setTrip_code(rs.getString("trip_code"));
							dto.setTrip_time(rs.getString("trip_time"));
							dto.setTrip_date(rs.getString("trip_date"));
							dto.setTrip_log(rs.getString("trip_log"));
							dto.setVehicleNo(rs.getString("regno"));
							dto.setDriverName(rs.getString("drivername"));
							dto.setDriverContact(rs.getString("drivercontact"));
						  //dto.setIsSecurity(rs.getString("security_status"));
							if(rs.getString("security_status").equalsIgnoreCase(rs.getString("escortstatus")))
							{
							dto.setIsSecurity("NO");
							}
							else{
								dto.setIsSecurity("YES");
							}
							dto.setEscortName(rs.getString("escortname"));
							dto.setEscortContact(rs.getString("escortcontact"));
							dto.setEmpCount(Integer.parseInt(rs.getString("totalemps")));
							dto.setDoneBy(rs.getString("empid"));
							dto.setTripDetailsChildDtoList(new AndroidAppForEmployeeDao()
									.getTripEmployees(dto.getId()));
						}
					}
		}
catch (Exception e) {
			e.printStackTrace();
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);
		}
		return dto;
	}

	public ArrayList<TripDetailsChildDto> getTripEmployees(String tripid) {

		ArrayList<TripDetailsChildDto> list = new ArrayList<TripDetailsChildDto>();
		DbConnect ob = DbConnect.getInstance();
		Connection con = ob.connectDB();
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {

			String query = "select e.id,e.personnelNo,e.displayname,e.gender,e.contactnumber1,e.contactnumber2 from trip_details_child tdc join employee e on e.id=tdc.employeeid where tripid=?";
			pst = con.prepareStatement(query);
			pst.setString(1, tripid);
			rs = pst.executeQuery();
			while (rs.next()) {
				TripDetailsChildDto dto = new TripDetailsChildDto();
				dto.setEmployeeId(rs.getString("id"));
				dto.setPersonnelNo(rs.getString("personnelNo"));
				dto.setEmployeeName(rs.getString("displayname"));
				dto.setContactNumber(rs.getString("contactnumber1"));
				if (dto.getContactNumber() == null
						|| dto.getContactNumber().equalsIgnoreCase(""))
					dto.setContactNumber(rs.getString("contactnumber2"));
				dto.setGender(rs.getString("gender"));
				list.add(dto);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);
		}
		return list;
	}

	public JSONObject getVehiclePosition(String tripid) {
		JSONObject json = new JSONObject();
		DbConnect ob = DbConnect.getInstance();
		Connection con = ob.connectDB();
		PreparedStatement pst = null, pst1 = null, pst2 = null;
		ResultSet rs = null, rs1 = null, rs2 = null;
		try {
			String query = "SELECT date_time,lattitude,longitude FROM VEHICLE_POSITION WHERE TRIPID=? order by date_time desc";
			pst = con.prepareStatement(query);
			pst.setString(1, tripid);
			if (tripid != null && !tripid.equalsIgnoreCase("")) {
				rs = pst.executeQuery();
				if (rs.next()) {
				
					json.put("DATE_TIME", rs.getString("date_time"));
					json.put("LAT", rs.getString("lattitude"));
					json.put("LONG", rs.getString("longitude"));
				
					
					
				
				} else {
					json.put("DATE_TIME", "");
					json.put("LAT", "");
					json.put("LONG", "");
				}
			} else {
				json.put("DATE_TIME", "");
				json.put("LAT", "");
				json.put("LONG", "");
			}
			String empquery = "select e.id from trip_details_child tdc join employee e on e.id=tdc.employeeid join vendor_trip_sheet vt on vt.tripid=tdc.tripid and vt.employeeid=e.id and vt.showstatus='Show' where tdc.tripid=?";
			pst1 = con.prepareStatement(empquery);
			pst1.setString(1, tripid);
			rs1 = pst1.executeQuery();
			int i = 0;
			while (rs1.next()) {

				i = i + 1;
				json.put("BOARD_ID" + i, rs1.getString("id"));
			}
			json.put("COUNT", i);
			pst2 = con
					.prepareStatement("SELECT SHOWSTATUS FROM VENDOR_TRIP_SHEET_ESCORT WHERE TRIPID=?");
			pst2.setString(1, tripid);
			rs2 = pst2.executeQuery();
			if (rs2.next()) {
				json.put("ESCORT_STATUS", rs2.getString("showstatus"));
			} else {
				json.put("ESCORT_STATUS", "NIL");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DbConnect.closeResultSet(rs, rs1, rs2);
			DbConnect.closeStatement(pst, pst1, pst2);
			DbConnect.closeConnection(con);
		}
		return json;
	}

	/*
	 * public JSONObject getVehiclePosition1() { DbConnect ob =
	 * DbConnect.getInstance(); Connection con = ob.connectDB();
	 * PreparedStatement pst = null; ResultSet rs = null; JSONObject obj2 = new
	 * JSONObject(); JSONArray jarray = new JSONArray();
	 * 
	 * try { String query=
	 * "SELECT vp.lattitude,vp.longitude,v.regNo,vp.tripid FROM VEHICLE_POSITION vp JOIN vehicles v on vp.vehicleId=v.id JOIN vendor_trip_sheet_parent vtsp on vtsp.tripid=vp.tripid and vtsp.status='started'  WHERE v.status='a' "
	 * ; //String query=
	 * "SELECT vp.lattitude,vp.longitude,v.regNo FROM VEHICLE_POSITION vp JOIN vehicles v on vp.vehicleId=v.id WHERE v.status='a' "
	 * ; //String query=
	 * "SELECT vp.lattitude,vp.longitude,v.regNo FROM VEHICLE_POSITION vp JOIN vehicles v on vp.vehicleId=v.id"
	 * ;
	 * 
	 * System.out.println("VEHICLE POSITION QUERY"+query); pst =
	 * con.prepareStatement(query); List<String> lati = new ArrayList<String>();
	 * List<String> logi = new ArrayList<String>(); List<String> vehicle = new
	 * ArrayList<String>();
	 * 
	 * 
	 * rs=pst.executeQuery(); while(rs.next()) {
	 * lati.add(rs.getString("lattitude")); logi.add(rs.getString("longitude"));
	 * vehicle.add(rs.getString("regNo")); }
	 * 
	 * 
	 * try { JSONObject obj1 = new JSONObject(); for (int i = 0; i <
	 * vehicle.size(); i++) { obj1.put("LAT", lati.get(i)); obj1.put("LONG",
	 * logi.get(i)); obj1.put("REG_NO", vehicle.get(i)); jarray.put(obj1); }
	 * obj2.put("JSONARRAY", jarray);
	 * 
	 * 
	 * }catch(Exception e) { e.printStackTrace(); } }catch(Exception e) {
	 * e.printStackTrace(); } try { String query=
	 * "SELECT vp.lattitude,vp.longitude,v.regNo FROM VEHICLE_POSITION vp JOIN vehicles v on vp.vehicleId=v.id WHERE v.status='a'"
	 * ; pst = con.prepareStatement(query); rs=pst.executeQuery();
	 * while(rs.next()) {
	 * 
	 * json.put("LAT", rs.getString("lattitude")); json.put("LONG",
	 * rs.getString("longitude")); json.put("regNo", rs.getString("regNo")); }
	 * 
	 * } catch(Exception e) { e.printStackTrace(); }
	 * 
	 * 
	 * finally { DbConnect.closeResultSet(rs); DbConnect.closeStatement(pst);
	 * DbConnect.closeConnection(con); }
	 * 
	 * return obj2; }
	 */

	public JSONObject getVehiclePosition1() {
		DbConnect ob = DbConnect.getInstance();
		Connection con = ob.connectDB();
		Statement st = null, st1 = null, st2 = null;
		ResultSet rs = null, rs1 = null, rs2 = null;
		JSONObject json = new JSONObject();

		String count = null;

		try {
			st = con.createStatement();
			String query = "SELECT vp.lattitude,vp.longitude,v.regNo,vp.tripid FROM VEHICLE_POSITION vp JOIN vehicles v on vp.vehicleId=v.id JOIN vendor_trip_sheet_parent vtsp on vtsp.tripid=vp.tripid and vtsp.status='started' group  by tripid";
			rs = st.executeQuery(query);
			if (rs.next()) {
				json.put("LAT", rs.getString("lattitude"));
				json.put("LONG", rs.getString("longitude"));
				json.put("REGNO", rs.getString("regNo"));
				json.put("TRIPID", rs.getString("tripid"));
				String query1 = "select  count(employeeid)  from employee e ,trip_details_child tc where e.id =tc.employeeId and tc.tripid ="
						+ rs.getString("tripid");
				st1 = con.createStatement();
				rs1 = st1.executeQuery(query1);
				if (rs1.next())
					count = rs1.getString("count(employeeid)");
				json.put("EMPS_COUNT", count);

				String query2 = "select e.displayname,e.gender   from employee e ,trip_details_child tc where e.id =tc.employeeId and tc.tripid ="
						+ rs.getString("tripid");
				st2 = con.createStatement();
				rs2 = st2.executeQuery(query2);
				int i = 1;
				while (rs2.next()) {
					json.put("EMP_NAME" + i, rs2.getString("displayname"));
					json.put("GENDER" + i, rs2.getString("gender"));
					i++;
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DbConnect.closeResultSet(rs, rs1);
			DbConnect.closeStatement(st, st1);
			DbConnect.closeConnection(con);
		}

		return json;
	}

	public JSONObject getEmpsBoarded(String tripid) {
		JSONObject obj = new JSONObject();
		DbConnect ob = DbConnect.getInstance();
		Connection con = ob.connectDB();
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {

			int i = 0;
			String query = "select e.id from trip_details_child tdc join employee e on e.id=tdc.employeeid join vendor_trip_sheet vt on vt.tripid=tdc.tripid and vt.employeeid=e.id and vt.showstatus='Show' where tdc.tripid=?";
			pst = con.prepareStatement(query);
			pst.setString(1, tripid);
			rs = pst.executeQuery();
			while (rs.next()) {

				i = i + 1;
				obj.put("BOARD_ID" + i, rs.getString("id"));
			}
			obj.put("COUNT", i);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);
		}
		return obj;
	}

	public int updateAuthenticate(String Imei) {

		int returnInt = 0;
		DbConnect ob = DbConnect.getInstance();
		Connection con = ob.connectDB();
		PreparedStatement pst = null;
		try {

			String query = "UPDATE EMPLOYEE_IMEI SET AUTHENTICATE='YES' WHERE IMEI=?";
			pst = con.prepareStatement(query);
			pst.setString(1, Imei);
			returnInt = pst.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);
		}
		return returnInt;
	}

	public int insertICEdetails(EmployeeICEDTO dto) {
		int returnint = 0;
		DbConnect ob = DbConnect.getInstance();
		Connection con = ob.connectDB();
		PreparedStatement pst = null, pst1 = null;
		ResultSet rs = null;
		try {

			String selectQuery = "SELECT ID FROM EMPLOYEE_ICE WHERE STATUS='A' AND EMPID = "
					+ dto.getEmpid();
			pst = con.prepareStatement(selectQuery);
			rs = pst.executeQuery();
			if (rs.next()) {
				String updatequery = "UPDATE EMPLOYEE_ICE SET CONTACT_NAME1=?,CONTACT_NUMBER1=?,CONTACT_EMAIL1=?,CONTACT_NAME2=?,CONTACT_NUMBER2=?,CONTACT_EMAIL2=? WHERE ID="
						+ rs.getInt("id");
				pst1 = con.prepareStatement(updatequery);
				pst1.setString(1, dto.getContact_name1());
				pst1.setString(2, dto.getContact_number1());
				pst1.setString(3, dto.getContact_email1());
				pst1.setString(4, dto.getContact_name2());
				pst1.setString(5, dto.getContact_number2());
				pst1.setString(6, dto.getContact_email2());
				returnint = pst1.executeUpdate();
			} else {
				String insertquery = "INSERT INTO EMPLOYEE_ICE(EMPID,CONTACT_NAME1,CONTACT_NUMBER1,CONTACT_EMAIL1,CONTACT_NAME2,CONTACT_NUMBER2,CONTACT_EMAIL2,STATUS) VALUES(?,?,?,?,?,?,?,'a')";
				pst1 = con.prepareStatement(insertquery);
				pst1.setString(1, dto.getEmpid());
				pst1.setString(2, dto.getContact_name1());
				pst1.setString(3, dto.getContact_number1());
				pst1.setString(4, dto.getContact_email1());
				pst1.setString(5, dto.getContact_name2());
				pst1.setString(6, dto.getContact_number2());
				pst1.setString(7, dto.getContact_email2());
				returnint = pst1.executeUpdate();
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(pst, pst1);
			DbConnect.closeConnection(con);
		}
		return returnint;
	}

	public EmployeeICEDTO getICEdetailsbyID(String empid) {

		EmployeeICEDTO dto = new EmployeeICEDTO();
		DbConnect ob = DbConnect.getInstance();
		Connection con = ob.connectDB();
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {

			String query = "SELECT * FROM EMPLOYEE_ICE WHERE STATUS='A' AND EMPID="
					+ empid;
			pst = con.prepareStatement(query);
			rs = pst.executeQuery();
			while (rs.next()) {
				dto.setContact_name1(rs.getString("contact_name1"));
				dto.setContact_number1(rs.getString("contact_number1"));
				dto.setContact_email1(rs.getString("contact_email1"));
				dto.setContact_name2(rs.getString("contact_name2"));
				dto.setContact_number2(rs.getString("contact_number2"));
				dto.setContact_email2(rs.getString("contact_email2"));

			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);
		}
		return dto;

	}

	public int panicactivated(String imei, String empid, String tripid,
			String Lat, String Long, String time) {
		int returnint = 0;
		DbConnect ob = DbConnect.getInstance();
		Connection con = ob.connectDB();
		int i=0;
		

		PreparedStatement pst = null, pst1 = null;
		ResultSet rs = null;
		try {
			
			String panicquery = "INSERT INTO EMPLOYEE_PANIC(EMPID,IMEI,TRIPID,LATITUDE,LONGITUDE,PANICTIME,STATUS,PANICDATE) VALUES(?,?,?,?,?,?,?,CURDATE())";

			pst = con.prepareStatement(panicquery);
			pst.setString(1, empid);
			pst.setString(2, imei);
			pst.setString(3, tripid);
			pst.setString(4, Lat);
			pst.setString(5, Long);
			pst.setString(6, time);
			pst.setString(7, "activated");
			returnint = pst.executeUpdate();

			String driverquery = "select imei_number from driver_logins dl join trip_details td on dl.vehicleid=td.vehicle and td.id= '"
					+ tripid + "'  order by  dl.datetime  desc limit 1";
			pst1 = con.prepareStatement(driverquery);
			rs = pst1.executeQuery();
			String driverImei = "";
			if (rs.next()) {
				driverImei = rs.getString("imei_number");
			}

			i =new TripServiceDao().panicAlarm(driverImei, empid);

			if(i>0)
			{
				new SMSService().sendSMSOnPanicEmployee(empid, tripid, imei);

			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);
		}
		return returnint;
	}

	public int panicdeactivated(String imei, String empid, String tripid) {
		int returnint = 0;

		DbConnect ob = DbConnect.getInstance();
		Connection con = ob.connectDB();
		PreparedStatement pst = null;
		try {
			String panicquery = "UPDATE EMPLOYEE_PANIC SET STATUS=? WHERE EMPID=? AND IMEI=? AND TRIPID=?";
			pst = con.prepareStatement(panicquery);
			pst.setString(1, "deactivated");
			pst.setString(2, empid);
			pst.setString(3, imei);
			pst.setString(4, tripid);
			returnint = pst.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);
		}
		return returnint;
	}

	public void sendsmstoICE(String empid, String time) {
		DbConnect ob = DbConnect.getInstance();
		Connection con = ob.connectDB();
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			String query = "select ei.*,e.displayname from employee_ice ei join employee e on e.id=ei.empid where status='a' and empid="
					+ empid;
			String message = "";
			pst = con.prepareStatement(query);
			rs = pst.executeQuery();
			if (rs.next()) {
				message = "Panic alarm has been pressed by "
						+ rs.getString("displayname") + " at " + time
						+ ".Please take immediate action.";
				SendSMS sendSMS = SendSMSFactory.getSMSInstance();
				sendSMS.send(rs.getString("contact_number1"), message);
				sendSMS = SendSMSFactory.getSMSInstance();
				sendSMS.send(rs.getString("contact_number2"), message);
				SendMail sendmail = SendMailFactory.getMailInstance();
				sendmail.send(rs.getString("contact_email1"),
						"Panic Alarm Pressed", message);
				sendmail = SendMailFactory.getMailInstance();
				sendmail.send(rs.getString("contact_email2"),
						"Panic Alarm Pressed", message);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);
		}
	}

	public JSONObject getLiveTrips() {
		DbConnect ob = DbConnect.getInstance();
		Connection con = ob.connectDB();
		Statement st = null, st1 = null;
		ResultSet rs = null, rs1 = null;
		JSONObject json = new JSONObject();
		JSONArray LAT = new JSONArray();
		JSONArray LONG = new JSONArray();
		JSONArray REGNO = new JSONArray();
		JSONArray TRIPCODE = new JSONArray();
		JSONArray TRIPLOG = new JSONArray();
		JSONArray LOGSTATUS = new JSONArray();
		JSONArray ESCORT = new JSONArray();
		JSONArray LADYCOUNT = new JSONArray();
		JSONArray EMPINCOUNT = new JSONArray();
		JSONArray TRIPID = new JSONArray();
		
		try {
			st = con.createStatement();

			String query = "select vp.tripid,vp.lattitude,vp.longitude,vp.logstatus,td.trip_code,v.regNo,td.trip_log,vtsp.escort,(select count(*)  from vendor_trip_sheet vts,employee e where vts.tripId=td.id and  e.id=vts.employeeId and e.gender='F' and vts.curStatus='IN') as  ladyInCount,(select count(*)  from vendor_trip_sheet  vts where  vts.tripId=td.id) as empCount,(select count(*)  from vendor_trip_sheet  vts where  vts.tripId=td.id and vts.curStatus='IN') as empInCount from vehicles v,vehicle_position vp, (select vp.vehicleId,max(vp.date_time) as date_time from vehicle_position vp group by tripid,vehicleId) as  tg,site s,trip_details td  join vendor_trip_sheet_parent vtsp on td.id=vtsp.tripid where td.trip_date between  DATE_ADD(CURDATE(),INTERVAL -1 DAY) and curdate() and tg.vehicleId=vp.vehicleId and td.id=vp.tripId and td.siteid=s.id and s.branch=1 and tg.date_time=vp.date_time and  vp.vehicleId=v.id and vp.status='checkedin' and vp.tripid in (select tripid from vendor_trip_sheet_parent where status='started')  group by v.id order by vp.date_time desc";

			rs = st.executeQuery(query);

			while (rs.next()) {

				TRIPID.put(rs.getString("tripid"));
				LAT.put(rs.getString("lattitude"));
				LONG.put(rs.getString("longitude"));
				LOGSTATUS.put(rs.getString("logstatus"));
				TRIPLOG.put(rs.getString("trip_log"));
				TRIPCODE.put(rs.getString("trip_code"));
				REGNO.put(rs.getString("regNo"));
				ESCORT.put(rs.getString("escort"));
				LADYCOUNT.put(rs.getString("ladyInCount"));
				EMPINCOUNT.put(rs.getString("empInCount"));

			}

			
			json.put("TRIPID", TRIPID);
			json.put("LAT", LAT);
			json.put("LONG", LONG);
			json.put("REGNO", REGNO);
			json.put("TRIPCODE", TRIPCODE);
			json.put("TRIPLOG", TRIPLOG);
			json.put("LOGSTATUS", LOGSTATUS);
			json.put("LADYCOUNT", LADYCOUNT);
			json.put("EMPINCOUNT", EMPINCOUNT);
			json.put("ESCORT", ESCORT);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DbConnect.closeResultSet(rs, rs1);
			DbConnect.closeStatement(st, st1);
			DbConnect.closeConnection(con);
		}
		return json;
	}

	public List<Object> scheduleList(String imei) {
		DbConnect ob = DbConnect.getInstance();
		Connection con = ob.connectDB();
		Statement st = null, st1 = null;
		ResultSet rs = null, rs1 = null;
		List<Object> list = new ArrayList<Object>();
		ArrayList<ScheduleAlterDto> schedule = new ArrayList<ScheduleAlterDto>();
		JSONArray scheduledate = new JSONArray();
		JSONArray login = new JSONArray();
		JSONArray logout = new JSONArray();
		int id = 0;
		try {

			String getschheduleqry = "select id,from_date,to_date,login_time,logout_time from employee_schedule where to_date >=current_date()  and employee_id=(select empid from employee_imei where imei='"
					+ imei + "')";
			
			st = con.createStatement();
			rs = st.executeQuery(getschheduleqry);
			if (rs.next()) {
				ScheduleAlterDto dto = new ScheduleAlterDto();
				dto.setFromDate(rs.getDate("from_date"));
				dto.setToDate(rs.getDate("to_date"));
				dto.setLoginTime(rs.getString("login_time"));
				dto.setLogoutTime(rs.getString("logout_time"));
				id = rs.getInt("id");
				schedule.add(dto);

				String getAlter = "select date,login_time,logout_time from employee_schedule_alter where date>=curdate() and scheduleid="
						+ id;
				st1 = con.createStatement();
				rs1 = st1.executeQuery(getAlter);
				System.out.println("qry2" + getAlter);
				while (rs1.next()) {
					scheduledate.put(rs1.getString("date"));
					login.put(rs1.getString("login_time"));
					logout.put(rs1.getString("logout_time"));
				}
				list.add(schedule);

				list.add(scheduledate);
				list.add(login);
				list.add(logout);
			}
		} catch (SQLException e) {
			System.out.println("Error in AndroidAppForEmployeeDao@scheduleList"
					+ e);
		} finally {
			DbConnect.closeResultSet(rs, rs1);
			DbConnect.closeStatement(st, st1);
			DbConnect.closeConnection(con);
		}
		return list;
	}

	public String requestScheduleAlter(String imei, String date, String logIn,
			String logOut, String type) {
		DbConnect ob = DbConnect.getInstance();
		Connection con = ob.connectDB();
		Statement st = null, st1 = null, st2 = null, st3 = null;
		ResultSet rs = null, rs1 = null, rs2 = null;
		PreparedStatement pst = null, pst2 = null;
		int result = 0;
		String status = "Failed!";
		try {

			// convert date to sql
			DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
			Date d1 = format.parse(date);
			date = OtherFunctions.changeDateFromatToSqlFormat(d1);
			boolean flag = dateValidator(date,logIn,logOut);
			if (flag) {
				st = con.createStatement();
				String access_query = "select has_accessl from schedule_access_right where user_type=(select role_id from employee where id=(select empid from employee_imei where imei='"
						+ imei + "'  order by id desc limit 1))";
				rs = st.executeQuery(access_query);
				String access = "", empid = "";
				int schId = 0;
				while (rs.next()) {
					access = rs.getString("has_accessl");
				}
				String getquery = "select id,employee_id from employee_schedule where employee_id=(select empid from employee_imei where imei='"
						+ imei
						+ "'  order by id desc limit 1) and from_date<='"
						+ date
						+ "' and to_date>='"
						+ date
						+ "' order by id desc limit 1";
				st1 = con.createStatement();
				rs1 = st1.executeQuery(getquery);
				if (rs1.next()) {
					schId = rs1.getInt("id");
					empid = rs1.getString("employee_id");
				}
				if (access.equalsIgnoreCase("YES")) {
					String check = "select id from employee_schedule_alter where scheduleId="
							+ schId + " and date='" + date + "'";
					st2 = con.createStatement();
					rs2 = st2.executeQuery(check);
					if (rs2.next()) {
						String update = "UPDATE employee_schedule_alter SET login_time='"
								+ logIn
								+ "', logout_time='"
								+ logOut
								+ "' WHERE id='" + rs2.getString("id") + "'";
						st3 = con.createStatement();
						result = st3.executeUpdate(update);
					} else {
						String insertqry = "INSERT INTO employee_schedule_alter (scheduleId,date, login_time, logout_time, status, statusDate, updatedBy) VALUES (?,?, ?, ?, 'a', curdate(), ?)";
						pst2 = con.prepareStatement(insertqry);
						pst2.setInt(1, schId);
						pst2.setString(2, date);
						pst2.setString(3, logIn);
						pst2.setString(4, logOut);
						pst2.setString(5, empid);
						result = pst2.executeUpdate();
					}
					if (result > 0) {
						if (type.equalsIgnoreCase("Alter")) {
							status = "Altered Successfully";
						} else {
							status = "Cancelled Successfully";
						}
						ScheduleAlterDto dto = new ScheduleAlterDto();
						dto.setScheduleId(schId + "");
						ArrayList<ScheduleAlterDto> list = new ArrayList<ScheduleAlterDto>();
						list.add(dto);
						new MailService().sendAlteredScheduleMail(list, empid);
					}
				} else {
					String insertrequest = "INSERT INTO schedulealterrequest (scheduleId,empId, date, logIn, logOut) VALUES (?,?,?, ?,?)";
					pst = con.prepareStatement(insertrequest);
					pst.setInt(1, schId);
					pst.setString(2, empid);
					pst.setString(3, date);
					pst.setString(4, logIn);
					pst.setString(5, logOut);
					int i = pst.executeUpdate();
					if (i > 0) {
						status = "Sent for Approvance";
					}
				}
			} else {
				status = "Failed! Alter Restricted";
			}
		} catch (SQLException e) {
			System.out
					.println("Error in AndroidAppForEmployeeDao@requestSchedulAlter"
							+ e);
		} catch (ParseException e) {
			System.out
					.println("Error in AndroidAppForEmployeeDao@requestSchedulAlter"
							+ e);
		} finally {
			DbConnect.closeResultSet(rs, rs1, rs2);
			DbConnect.closeStatement(st, st1, pst2, pst, st2);
			DbConnect.closeConnection(con);
		}

		return status;
	}

	private boolean dateValidator(String date, String logIn, String logOut) {
		boolean flag = true;
		try {
			if (SettingsConstant.comp.equalsIgnoreCase("gss") || SettingsConstant.comp.equalsIgnoreCase("gss1")) {

				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
				Date date1 = formatter.parse(date);
				Calendar cal = Calendar.getInstance();
				Calendar cal2 = Calendar.getInstance();
				cal.setTime(date1);
				int alteringWeeek = cal.get(Calendar.WEEK_OF_YEAR);
				int currentWeek = cal2.get(Calendar.WEEK_OF_YEAR);
				if (alteringWeeek == currentWeek) {
					flag = false;
				} else if (!(cal2.get(Calendar.DAY_OF_WEEK) == 4 || cal2.get(Calendar.DAY_OF_WEEK) == 5
						|| cal2.get(Calendar.DAY_OF_WEEK) == 3)) {
					flag = false;
				}
			} else if (SettingsConstant.scheduleSingleDayCutOff.equalsIgnoreCase("YES")) {

				ScheduleAlterDto scheduleCuttOff = getScheduleCutOff();
				int inCutOff = Integer.parseInt(scheduleCuttOff.getLoginTime());
				int outCutOff = Integer.parseInt(scheduleCuttOff.getLogoutTime());
				
				Calendar cal = Calendar.getInstance();
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				String curdate = format.format(cal.getTime());
				
				Calendar cal2 = Calendar.getInstance();
				cal2.add(Calendar.DAY_OF_MONTH, 1);
				String nxtDate = format.format(cal2.getTime());
				
				SimpleDateFormat format1 = new SimpleDateFormat("HH");
				int curTime = Integer.parseInt(format1.format(cal.getTime()));
				
				SimpleDateFormat format2 = new SimpleDateFormat("mm");
				int curMin =Integer.parseInt( format2.format(cal.getTime()));
				
				if(curdate.equalsIgnoreCase(date)){
					if(!(logIn.equalsIgnoreCase("weekly off") ||logIn.equalsIgnoreCase("cancel"))){
					if((Integer.parseInt(logIn.split(":")[0])) - curTime <inCutOff){
						flag=false;
					}else if((Integer.parseInt(logIn.split(":")[0])) - curTime ==inCutOff && (Integer.parseInt(logIn.split(":")[1]))-curMin <inCutOff){
						flag=false;
					}
				}else if(!(logOut.equalsIgnoreCase("weekly off") ||logOut.equalsIgnoreCase("cancel"))){
					
					if((Integer.parseInt(logOut.split(":")[0])) - curTime <outCutOff){
						flag=false;
					}else if((Integer.parseInt(logOut.split(":")[0])) - curTime ==outCutOff && (Integer.parseInt(logOut.split(":")[1]))-curMin <outCutOff){
						flag=false;
					}
				}
				}else if(nxtDate.equalsIgnoreCase(date)){
					if(!(logIn.equalsIgnoreCase("weekly off") ||logIn.equalsIgnoreCase("cancel"))){
						if((Integer.parseInt(logIn.split(":")[0])+24) - curTime <inCutOff){
							flag=false;
						}else if((Integer.parseInt(logIn.split(":")[0])+24) - curTime ==inCutOff && (Integer.parseInt(logIn.split(":")[1]))-curMin <inCutOff){
							flag=false;
						}
					}else if(!(logOut.equalsIgnoreCase("weekly off") ||logOut.equalsIgnoreCase("cancel"))){
						
						if((Integer.parseInt(logOut.split(":")[0])+24) - curTime <outCutOff){
							flag=false;
						}else if((Integer.parseInt(logOut.split(":")[0])+24) - curTime ==outCutOff && (Integer.parseInt(logOut.split(":")[1]))-curMin <outCutOff){
							flag=false;
						}
					}
				}
				
			}
		} catch (Exception e) {
			System.out.println("Error in AndroidAppForEmployee@dateValidator" + e);
			flag = false;
		}

		return flag;
	}

	public JSONObject adhocRequest(String imei, AdhocDto dto) {
		DbConnect ob = DbConnect.getInstance();
		Connection con = ob.connectDB();
		Statement st = null;
		ResultSet rs = null;
		String Status = "Failed!";
		String result="FALSE";
		JSONObject obj=new JSONObject();
		try {
			String getempl = "select e.id,e.site,e.projectUnit from employee e,employee_imei ei where e.id=ei.empid and imei='"
					+ imei + "'";
			st = con.createStatement();
			rs = st.executeQuery(getempl);
			if (rs.next()) {
				boolean flag=checkAdhocDate(dto.getTravelDate(),dto.getShiftTime(),dto.getAdhocType(),rs.getString("site"),rs.getString("projectUnit"));
				if(flag){
				dto.setSiteId(rs.getString("site"));
				dto.setEmployeeId(rs.getString("id"));
				dto.setBookedFor(rs.getString("id"));
				dto.setBookedBy(rs.getString("id"));
				dto.setProjectUnit(rs.getString("projectUnit"));
				Status = new AdhocService().bookAdhocTravel(dto);
				Status = Status.split(">")[1];
				Status = Status.split("<")[0];
				result="TRUE";
				}else{
					Status="Booking for That time has closed";
				}
				
			} else {
				Status = "Failed!No Active Schedule";
			}

		} catch (Exception e) {
			System.out.println("Error in AndroidAppForEmployeeDao@adhocRequest"
					+ e);
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);
		}
		try {
			obj.put("RESULT", result);
			obj.put("STATUS", Status);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return obj;
	}

	public String setfeedback(String imei, String date, String logType,
			String vehiclCond, String driverBehav, String travelTime,
			String overAll, String other) {

		DbConnect ob = DbConnect.getInstance();
		Connection con = ob.connectDB();
		Statement st = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		String Status = "Failed!";
		int result = 0;
		String tripid = "";
		try {
			SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
			Date dat = format.parse(date);
			date = OtherFunctions.changeDateFromatToSqlFormat(dat);
			String getTripId = "select td.id from trip_details td,vendor_trip_sheet vts,employee_imei em where td.trip_log='"+logType+"' and td.trip_date='"
					+ date
					+ "' and td.id=vts.tripid  and vts.showStatus='Show' and vts.employeeId=em.empid and imei='"
					+ imei + "'";
			System.out.println(getTripId);
			st = con.createStatement();
			rs = st.executeQuery(getTripId);
			if (rs.next()) {
				tripid = rs.getString("id");

				String setfeedback = "INSERT INTO employee_feedback (empId, tripId, vehicleCondition, DriverBehaviour, TravelTime, OverAll, Other) VALUES ((select empid from employee_imei where imei=?), ?, ?, ?, ?, ?, ?)";
				pst = con.prepareStatement(setfeedback);
				pst.setString(1, imei);
				pst.setString(2, tripid);
				pst.setString(3, vehiclCond);
				pst.setString(4, driverBehav);
				pst.setString(5, travelTime);
				pst.setString(6, overAll);
				;
				pst.setString(7, other);
				result = pst.executeUpdate();
				if (result > 0) {
					Status = "Thank you for your feedback";
				}
			} else {
				Status = "Failed! Your are not traveled on this date";
			}
		} catch (Exception e) {
			System.out.println("Error in AndroidAppForEmployeeDao@setfeedback"
					+ e);
		} finally {
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);
		}
		return Status;
	}

		public JSONObject getManagerTrips(String empid) {
		DbConnect ob = DbConnect.getInstance();
		Connection con = ob.connectDB();
		Statement st = null, st1 = null;
		ResultSet rs = null, rs1 = null;
		JSONObject json = new JSONObject();
		JSONArray LAT = new JSONArray();
		JSONArray LONG = new JSONArray();
		JSONArray REGNO = new JSONArray();
		JSONArray TRIPCODE = new JSONArray();
		JSONArray TRIPLOG = new JSONArray();
		JSONArray LOGSTATUS = new JSONArray();
		JSONArray ESCORT = new JSONArray();
		JSONArray LADYCOUNT = new JSONArray();
		JSONArray EMPINCOUNT = new JSONArray();

		try {
			st = con.createStatement();

			// String query =
			// "select vp.lattitude,vp.longitude,vp.logstatus,td.trip_code,v.regNo,td.trip_log,vtsp.escort,(select count(*)  from vendor_trip_sheet vts,employee e where vts.tripId=td.id and  e.id=vts.employeeId and e.gender='F' and vts.curStatus='IN') as  ladyInCount,(select count(*)  from vendor_trip_sheet  vts where  vts.tripId=td.id) as empCount,(select count(*)  from vendor_trip_sheet  vts where  vts.tripId=td.id and vts.curStatus='IN') as empInCount from vehicles v,vehicle_position vp, (select vp.vehicleId,max(vp.date_time) as date_time from vehicle_position vp group by tripid,vehicleId) as  tg,site s,trip_details td  join vendor_trip_sheet_parent vtsp on td.id=vtsp.tripid where td.trip_date between  DATE_ADD(CURDATE(),INTERVAL -1 DAY) and curdate() and tg.vehicleId=vp.vehicleId and td.id=vp.tripId and td.siteid=s.id and s.branch=1 and tg.date_time=vp.date_time and  vp.vehicleId=v.id and vp.status='checkedin' and vp.tripid in (select tripid from vendor_trip_sheet_parent where status='started') and (select tripid from trip_details td join trip_details_child tc  on tc.tripid =td.id and  td.trip_date between  DATE_ADD(CURDATE(),INTERVAL -1 DAY) and curdate()    join employee_subscription es on es.empid=tc.employeeid where (es.spoc =1783||es.supervisor=1783)) group by v.id order by vp.date_time desc";
			String query = "select vp.lattitude,vp.longitude,vp.logstatus,td.trip_code,v.regNo,td.trip_log,vtsp.escort,(select count(*)  from vendor_trip_sheet vts,employee e where vts.tripId=td.id and  e.id=vts.employeeId and e.gender='F' and vts.curStatus='IN') as  ladyInCount,(select count(*)  from vendor_trip_sheet  vts where  vts.tripId=td.id) as empCount,(select count(*)  from vendor_trip_sheet  vts where  vts.tripId=td.id and vts.curStatus='IN') as empInCount from vehicles v,vehicle_position vp,(select vp.vehicleId,max(vp.date_time) as date_time from vehicle_position vp group by tripid,vehicleId) as  tg,site s,  ( select t.id,t.trip_code,t.trip_log,t.siteid from ( select * from trip_details where trip_date between  DATE_ADD(CURDATE(),INTERVAL -1 DAY) and curdate() ) t join trip_details_child tc  on tc.tripid =t.id    join employee_subscription es on es.empid=tc.employeeid where (es.spoc ='"
					+ empid
					+ "'  ||es.supervisor='"
					+ empid
					+ "' ) ) td  join vendor_trip_sheet_parent vtsp on td.id=vtsp.tripid  and vtsp.status ='started' where tg.vehicleId=vp.vehicleId and td.id=vp.tripId and td.siteid=s.id and s.branch=1 and tg.date_time=vp.date_time and  vp.vehicleId=v.id and vp.status='checkedin' group by v.id order by vp.date_time desc";
			rs = st.executeQuery(query);

			while (rs.next()) {

				LAT.put(rs.getString("lattitude"));
				LONG.put(rs.getString("longitude"));
				LOGSTATUS.put(rs.getString("trip_log"));
				TRIPLOG.put(rs.getString("logstatus"));
				TRIPCODE.put(rs.getString("trip_code"));
				REGNO.put(rs.getString("regNo"));
				ESCORT.put(rs.getString("escort"));
				LADYCOUNT.put(rs.getString("ladyInCount"));
				EMPINCOUNT.put(rs.getString("empInCount"));

			}

			json.put("LAT", LAT);
			json.put("LONG", LONG);
			json.put("REGNO", REGNO);
			json.put("TRIPCODE", TRIPCODE);
			json.put("TRIPLOG", TRIPLOG);
			json.put("LOGSTATUS", LOGSTATUS);
			json.put("LADYCOUNT", LADYCOUNT);
			json.put("EMPINCOUNT", EMPINCOUNT);
			json.put("ESCORT", ESCORT);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DbConnect.closeResultSet(rs, rs1);
			DbConnect.closeStatement(st, st1);
			DbConnect.closeConnection(con);
		}
		return json;
	}

	public JSONObject getemplogs ( String empid) 
	{
		DbConnect ob = DbConnect.getInstance();
		Connection con = ob.connectDB();

		Statement st = null, st1 = null;
		ResultSet rs = null, rs1 = null;

		JSONArray IN_LOGS = new JSONArray();
		JSONArray OUT_LOGS = new JSONArray();
		JSONObject json = new JSONObject();

		try {
			st = con.createStatement();
			String query = "";
			String projectid = null;

			
			String checkquery = "select project from employee where id = " + empid;
			rs = st.executeQuery(checkquery);
			while (rs.next()) {
			
				projectid=rs.getString("project");
			}
			
			if(SettingsConstant.comp.equalsIgnoreCase("visa") && projectid!=null)
			{
				query="select logtime,logtype from  atsconnect at join project_shifttime pt on pt.project=at.project join logtime lt on lt.id=pt.shifttime where  at.project='"+projectid+"' and lt.status='active' ";
			}
				
			else{
				query = "select  logtime,logtype from site_shift ss,logtime lt where lt.id=ss.shiftid and lt.status='active'";
			}
			if( SettingsConstant.comp.equalsIgnoreCase("visa"))
			{
				if(!projectid.equalsIgnoreCase("6") )//for O&I project we are not sending logs as they cant book.
				{
					st1 = con.createStatement();
				
				rs1 = st1.executeQuery(query);
				while (rs1.next()) {

					if (rs1.getString("logtype").equalsIgnoreCase("IN")) {
						IN_LOGS.put(rs1.getString("logtime"));
					} else {

						OUT_LOGS.put(rs1.getString("logtime"));
					}

				}
				}
			}
			else {
				
				st1 = con.createStatement();
				rs1 = st1.executeQuery(query);
				while (rs1.next()) {

					if (rs1.getString("logtype").equalsIgnoreCase("IN")) {
						IN_LOGS.put(rs1.getString("logtime"));
					} else {

						OUT_LOGS.put(rs1.getString("logtime"));
					}

				}
			}
			
			
			json.put("RESULT", "true");
			json.put("IN_LOGS", IN_LOGS);
			json.put("OUT_LOGS", OUT_LOGS);

		} catch (Exception e) {

			try {
				json.put("RESULT", "false");
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			e.printStackTrace();
		} finally {
			DbConnect.closeResultSet(rs, rs1);
			DbConnect.closeStatement(st, st1);
			DbConnect.closeConnection(con);
		}
		return json;
	}

	public boolean checkAccess(String imei) {
		DbConnect ob = DbConnect.getInstance();
		Connection con = ob.connectDB();
		Statement st = null;
		ResultSet rs=null;
		boolean result=false;
		try{
			st=con.createStatement();
			String check="select sr.has_accessl FROM employee e,employee_imei ei,schedule_access_right sr where sr.user_type=e.role_id and e.id=ei.empid and ei.imei='"+imei+"'";
			System.out.println(check);
			rs=st.executeQuery(check);
			if(rs.next()){
				if(rs.getString("has_accessl").equalsIgnoreCase("yes")){
					result=true;
				}
			}
		}catch(Exception e){
			System.out.println("Error in AndroidAppFroEmployeeDao@CheckAccess"+e);
		}finally{
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);
		}
		
		return result;
	}

	public JSONObject scheduleListofEmp(String empid) {
		// TODO Auto-generated method stub
		DbConnect ob = DbConnect.getInstance();
		Connection con = ob.connectDB();
		Statement st = null,st1=null;
		ResultSet rs=null,rs1=null;
		JSONObject jObj=new JSONObject();
		JSONArray logIn= new JSONArray();
		JSONArray logOut= new JSONArray();
		JSONArray FromDate= new JSONArray();
		JSONArray ToDate= new JSONArray();
		JSONArray alterLogin = new JSONArray();
		JSONArray alterLogout = new JSONArray();
		JSONArray alterDate = new JSONArray();
		try{
			String getSchedule="select id,from_date,to_date,login_time,logout_time from employee_schedule where to_date >=current_date()  and employee_id="+empid;
			st=con.createStatement();
			rs=st.executeQuery(getSchedule);
			while(rs.next()){
				FromDate.put(OtherFunctions.changeDateFromat(rs.getString("from_date")));
				ToDate.put(OtherFunctions.changeDateFromat(rs.getString("to_date")));
				logIn.put(rs.getString("login_time"));
				logOut.put(rs.getString("logout_time"));
				String getAlter = "select date,login_time,logout_time from employee_schedule_alter where date>=curdate() and scheduleid="+rs.getString("id");
				st1=con.createStatement();
				rs1=st1.executeQuery(getAlter);
				while(rs1.next()){
					alterDate.put(rs1.getString("date"));
					alterLogin.put(rs1.getString("login_time"));
					alterLogout.put(rs1.getString("logout_time"));
				}
			}
			jObj.put("FROM_DATE", FromDate);
			jObj.put("TO_DATE", ToDate);
			jObj.put("LOG_IN", logIn);
			jObj.put("LOG_OUT", logOut);
			jObj.put("ALTERDATE",alterDate);
			jObj.put("AL_LOG_IN", alterLogin);
			jObj.put("AL_LOG_OUT", alterLogout);
			jObj.put("RESULT", "TRUE");
		}catch(Exception e){
			System.out.println("Error in AndroidAppFroEmployeeDao@scheduleListofEmp"+e);
		}finally{
			DbConnect.closeResultSet(rs,rs1);
			DbConnect.closeStatement(st,st1);
			DbConnect.closeConnection(con);
		}
		
		return jObj;
	}

	public JSONObject scheduleEmp(String empid, String bookingByImei,
			String fromDate, String toDate, String logIn, String logOut,String site) {

		DbConnect ob = DbConnect.getInstance();
		Connection con = ob.connectDB();
		Statement st = null,st1=null,st3=null;
		PreparedStatement st2=null;
		ResultSet rs=null,rs1=null,rs2=null;
		String result="false";
		String msg="Booking Failed";
		JSONObject obj = new JSONObject();
		try{
			String fromdate12=fromDate;
			String todate12=toDate;
			DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
			Date dat = format.parse(fromDate);
			String date = OtherFunctions.changeDateFromatToSqlFormat(dat);
			boolean flag = dateValidator(date,logIn,logOut);
			String bookingForName="";
			String bookingById="";
			String bookingMail="";
			String bookingByName="";
			if(flag){
				String getempqry="select ei.empid,e.displayname from employee_imei ei,employee e where e.id=ei.empid and ei.imei='"+bookingByImei+"'";
				System.out.println(getempqry);
				st3=con.createStatement();
				rs2=st3.executeQuery(getempqry);
				if(rs2.next()){
					bookingById=rs2.getString("empid");
					bookingByName=rs2.getString("displayname");
				}
			if(empid.equalsIgnoreCase("SELF")){
				
				empid=bookingById;
				}
			
			
			
			
			String getsubscrition="select e.displayname,e.EmailAddress,es.subscriptionID,es.subscriptionStatus,a.id as project from employee_subscription es,employee e,atsconnect a where a.project=e.project and  e.id=es.empid and empid="+empid+" order by subscriptionID desc limit 1";
			System.out.println(getsubscrition);
			st1=con.createStatement();
			rs1=st1.executeQuery(getsubscrition);
			
			Date dat1 = format.parse(toDate);
			fromDate = OtherFunctions.changeDateFromatToSqlFormat(dat);
			toDate= OtherFunctions.changeDateFromatToSqlFormat(dat1);
			if(rs1.next()){
				
			String checkQry="select count(id) from employee_schedule where (	('"+fromDate+"' between from_date and ifnull(cancel_date,to_date)) or	('"+toDate+"' between from_date and ifnull(cancel_date,to_date)) or 	('"+fromDate+"' <=from_date and '"+toDate+"'>=ifnull(cancel_date,to_date)) ) and employee_schedule.status='a' and employee_schedule.subscription_id="+rs1.getString("subscriptionID")+" having count(id)>0";
		System.out.println(checkQry);
			st=con.createStatement();
			rs=st.executeQuery(checkQry);
			if(rs.next()){
				result="false";
				msg="Schedule for this date exists, try modify";
			}else{
				
					if(rs1.getString("subscriptionStatus").equalsIgnoreCase("subscribed")){
						bookingForName=rs1.getString("displayname");
						bookingMail=rs1.getString("EmailAddress");
						
						String insrtQry="INSERT INTO employee_schedule (`subscription_id`, `employee_id`, `project`, `from_date`, `to_date`, `login_time`, `logout_time`, `scheduledBy`, `log_status`, `status`, `type`) VALUES (?, ?, ?, ?, ?, ?, ?, (select empid from employee_imei where imei=?), 'both', 'a', 'project')";
						st2=con.prepareStatement(insrtQry);
						st2.setString(1, rs1.getString("subscriptionID"));
						st2.setString(2, empid);
						st2.setString(3, rs1.getString("project"));
						st2.setString(4, fromDate);
						st2.setString(5, toDate);
						st2.setString(6, logIn);
						st2.setString(7, logOut);
						st2.setString(8, bookingByImei);
						int res=st2.executeUpdate();
						if(res>0){
							result="true";
							msg="Booking Successful";
							SendMail mail = SendMailFactory.getMailInstance();
							String mailBody="Dear "
									+ bookingForName
									+ ", <br/> <br/> "
									+ "Your Scheduled has been created from "
									+ fromdate12
									+ " to "
									+todate12
									+" by  "
									+bookingByName
									+" ."
									+ "<br/><br/>"
									+ "Regards,<br/>"
									+ "Transport Team."
									+ " <br/><br/>"
									+ "<br/> <i><u>Note:</u> This is a system generated email. Please do not reply.</i>";
							
							mail.send(bookingMail,
									"Schedule Notification", mailBody);
						}
					}
			}
			}else{
				msg="You are not subcribed";
			}
			}else{
				msg = "Failed! Booking Restricted";
			}
			obj.put("RESULT", result);
			obj.put("STATUS", msg);
		}catch(Exception e){
			System.out.println("Error in AndroidAppFroEmployeeDao@scheduleEmp"+e);
		}finally{
			DbConnect.closeResultSet(rs,rs1);
			DbConnect.closeStatement(st,st1,st2,st3);
			DbConnect.closeConnection(con);
		}
		
		return obj;
	}

	public JSONObject adminScheduleAlter(String bookingFor,
			String bookingByImei, String date, String logIn, String logOut) {
		// TODO Auto-generated method stub
		DbConnect ob = DbConnect.getInstance();
		Connection con = ob.connectDB();
		Statement st = null,st1=null,st2=null,st4=null;
		PreparedStatement st3=null;
		ResultSet rs=null,rs1=null,rs2=null;
		int result=0;
		JSONObject jObj= new JSONObject();
		try{
			
			
if(bookingFor.equalsIgnoreCase("SELF")){
				
				String getempqry="select empid from employee_imei where imei='"+bookingByImei+"'";
				System.out.println(getempqry);
				st4=con.createStatement();
				rs2=st4.executeQuery(getempqry);
				if(rs2.next()){
					bookingFor=rs2.getString("empid");
				}
				
				
				}
			
			
			
			SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
			Date dat = format.parse(date);
			date = OtherFunctions.changeDateFromatToSqlFormat(dat);
			String checkQry="select id  from employee_schedule where from_date <="+date+" and to_date >= "+date+" and employee_id="+bookingFor;
			st=con.createStatement();
			rs=st.executeQuery(checkQry);
			if(rs.next()){
				String checkAlter="select id from employee_schedule_alter where scheduleId="+rs.getString("id")+" and  date='"+date+"'";
				st1=con.createStatement();
				rs1=st1.executeQuery(checkAlter);
				if(rs1.next()){
					String updateqry="UPDATE employee_schedule_alter SET login_time='"+logIn+"', logout_time='"+logOut+"', updatedBy=(select empid from employee_imei where imei='"+bookingByImei+"'),statusDate=now() WHERE id="+rs1.getString("id");
					st2=con.createStatement();
					result=st2.executeUpdate(updateqry);
				}else{
					String insetqry="INSERT INTO employee_schedule_alter (`scheduleId`, `date`, `login_time`, `logout_time`, `status`, `updatedBy`) VALUES (?, ?, ?, ?, 'a', (select empid from employee_imei where imei=?))";
					st3=con.prepareStatement(insetqry);
					st3.setString(1, rs.getString("id"));
					st3.setString(2, date);
					st3.setString(3, logIn);
					st3.setString(4, logOut);
					st3.setString(4, bookingByImei);
					result=st3.executeUpdate(insetqry);
				}
			}
			if(result>0){
				jObj.put("RESULT", "true");
				jObj.put("STATUS", "Booking altered successfully");
			}else{
				jObj.put("RESULT", "false");
				jObj.put("STATUS", "Failed");
			}
		}catch(Exception e){
			System.out.println("Error in AndroidAppFroEmployeeDao@adminScheduleAlter"+e);
		}finally{
			DbConnect.closeResultSet(rs,rs1);
			DbConnect.closeStatement(st,st1,st2,st3);
			DbConnect.closeConnection(con);
		}
		return jObj;
	}
	public JSONObject getEmployeeResult(String searchKey){
		
		DbConnect ob = DbConnect.getInstance();
		Connection con = ob.connectDB();
		Statement st = null;
		ResultSet rs=null;
		boolean flag=false;
		JSONObject jObj= new JSONObject();
		JSONArray idjarr=new JSONArray();
		JSONArray namearr=new JSONArray();
		try {
			st=con.createStatement();
			String getqry="select id,displayname from employee where displayname like '%"+searchKey+"%' or PersonnelNo like '%"+searchKey+"%' limit 5";
			rs=st.executeQuery(getqry);
			while(rs.next()){
				flag=true;
				idjarr.put(rs.getString("id"));
				namearr.put(rs.getString("displayname"));
			}
			
			if(flag){
				jObj.put("RESULT","TRUE");
				jObj.put("NAMES",namearr);
				jObj.put("IDS", idjarr);
			}else{
				jObj.put("RESULT","FALSE");
				jObj.put("MESSAGE","NO RESULTS FOUND!");
			}
			
		}catch(Exception e){
			System.out.println("Error in AndroidAppFroEmployeeDao@getEmployeeResult"+e);
		}finally{
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);
		}
		return jObj;
	}

	public JSONObject getTripHistory(String imei) {
		DbConnect ob = DbConnect.getInstance();
		Connection con = ob.connectDB();
		PreparedStatement pst = null;
		Statement st=null;
		ResultSet rs = null,rs1=null;
		JSONObject json = new JSONObject();
		JSONArray tripDate = new JSONArray();
		JSONArray distance = new JSONArray();
		JSONArray area = new JSONArray();
		JSONArray log= new JSONArray();
		int branch=1;
		String sitelandmrk="";
		int cutOff = Integer
				.parseInt(SettingsConstant.andrioidTripDetailsCount);
		try {
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DAY_OF_MONTH, -1);
			cal.getTime();
			System.out.println(cal.getTime());
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			String curdate = format.format(cal.getTime());
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.DAY_OF_MONTH, -cutOff);
			String previousdate = format.format(calendar.getTime());
			String getDetails = "select DATE_FORMAT(td.trip_date,'%d-%m-%Y')as tripDate,td.trip_log,tdc.dist,tdc.landmarkId,l.landmark,a.area from trip_details td,trip_details_child tdc,employee_imei im,landmark l,place p,area a  where td.id=tdc.tripId and tdc.employeeId=im.empid and a.id=p.area and p.id=l.place and l.id=tdc.landmarkId and td.trip_date between '"
					+ previousdate
					+ "'and '"
					+ curdate
					+ "' and im.imei='"
					+ imei + "' order by td.id";
			String getsitelandmak="select landmark from site where branch="+branch;
			st=con.createStatement();
			rs1=st.executeQuery(getsitelandmak);
			if(rs1.next()){
				sitelandmrk=rs1.getString("landmark");
			}
			System.out.println(getDetails);
			pst = con.prepareStatement(getDetails);
			rs = pst.executeQuery();
			int i=0;
			while (rs.next()) {
				i++;
				tripDate.put(rs.getString("tripDate"));
				log.put(rs.getString("trip_log"));
				if(Float.parseFloat(rs.getString("dist"))>0){
					distance.put(rs.getString("dist"));
				}else{
					float dist = new DistanceListDao().getGooglMapDistance(sitelandmrk,rs.getString("landmarkId"));
					distance.put(Float.toString(dist));
				}
				
				area.put(rs.getString("area"));
				}
			if(i==0){
				json.put("RESULT", "false");
			}else{
				json.put("RESULT", "true");
			}
			json.put("tripDate", tripDate);
			json.put("distance", distance);
			json.put("tripshift", log);
			json.put("area", area);
			
		} catch (Exception e) {
			System.out
					.println("Error in AndroidAppForEmployeeDao@getTripHistory"
							+ e);
		} finally {
			DbConnect.closeResultSet(rs,rs1);
			DbConnect.closeStatement(pst,st);
			DbConnect.closeConnection(con);
		}
		return json;
	}
	public JSONObject getSchedules(String imei){
		DbConnect ob = DbConnect.getInstance();
		Connection con = ob.connectDB();
		Statement st=null,st1=null;
		ResultSet rs = null,rs1=null;
		JSONObject json=new JSONObject();
		boolean isScheduled=false;
		try{		
		JSONArray dates=new JSONArray();
		JSONArray intimes=new JSONArray();
		JSONArray outtimes=new JSONArray();
		Date dt=new Date();
		String todaydate=new SimpleDateFormat("yyyy-MM-dd").format(dt);
		String query ="select *,es.id as schid from employee_imei ei join employee e on e.id =ei.empid join employee_schedule es on es.employee_id=e.id where ei.imei='"+imei+"' and es.status='a' and  (from_date>='"+todaydate+"' or to_date>='"+todaydate+"')";
		st=con.createStatement();
		rs=st.executeQuery(query);
		List<ScheduledEmpDto> eslist=new ArrayList<ScheduledEmpDto>();
		while(rs.next()){
		isScheduled=true;
		ScheduledEmpDto dto=new ScheduledEmpDto();
		dto.setScheduleId(rs.getString("schid"));
		dto.setFrom_date(rs.getString("from_date"));
		dto.setTo_date(rs.getString("to_date"));
		dto.setLoginTime(rs.getString("login_time"));
		dto.setLogoutTime(rs.getString("logout_time"));
		eslist.add(dto);
		}
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-M-d");	
		SimpleDateFormat dbsdf=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String schedulealterquery="";
			for(ScheduledEmpDto es:eslist){
			long timediff=dbsdf.parse(es.getTo_date()).getTime()-dbsdf.parse(es.getFrom_date()).getTime();
			int daysdiff=Integer.parseInt(""+TimeUnit.DAYS.convert(timediff, TimeUnit.MILLISECONDS));
			schedulealterquery="select * from employee_schedule_alter where status='a' and date>curdate() and scheduleid="+es.getScheduleId();
			System.out.println(schedulealterquery);
			List<ScheduleAlterDto> esalter=new ArrayList<ScheduleAlterDto>();
			st1=con.createStatement();
			rs1=st1.executeQuery(schedulealterquery);
			while(rs1.next()){
				ScheduleAlterDto schalter=new ScheduleAlterDto();
				schalter.setDate(rs1.getString("date"));
				schalter.setLoginTime(rs1.getString("login_time"));
				schalter.setLogoutTime(rs1.getString("logout_time"));
				esalter.add(schalter);
			}
			for(int i=0;i<=daysdiff;i++){
				Date d=new Date(dbsdf.parse(es.getFrom_date()).getTime()+ TimeUnit.DAYS.toMillis( i ));
				System.out.println("gggg"+esalter.size());
				if(esalter.size()>0){
				for(ScheduleAlterDto esal:esalter){
					System.out.println("alter date"+esal.getDate());
					if(dbsdf.parse(esal.getDate()).getDate()==d.getDate()){
							dates.put(sdf.format(sdf.parse(esal.getDate())));
							outtimes.put(esal.getLogoutTime());
							intimes.put(esal.getLoginTime());
					}
					else{
						dates.put(sdf.format(d).toString());
						outtimes.put(es.getLogoutTime());
						intimes.put(es.getLoginTime());
					}
				}
				}else
				{
					dates.put(sdf.format(d));
					outtimes.put(es.getLogoutTime());
					intimes.put(es.getLoginTime());
				}
			}
			}
			if(isScheduled){
		json.put("DATES",dates);
		json.put("RESULT", "TRUE");
		json.put("IN_TIMES",intimes);
		json.put("OUT_TIMES", outtimes);
			}
			else{
				json.put("RESULT", "FALSE");
				json.put("MESSAGE", "NO SCHEDULES FOUND!");
			}
		
		}catch(Exception e){
			e.printStackTrace();
			try{
			json.put("RESULT", "FALSE");
			json.put("MESSAGE", e);
			}catch(Exception e1){e1.printStackTrace();}
		}
		finally{
			DbConnect.closeResultSet(rs,rs1);
			DbConnect.closeStatement(st,st1);
			DbConnect.closeConnection(con);
		}
		return json;
	}
	
	public JSONObject getTimesForDate(String date,String imei){
		JSONObject retJson=new JSONObject();
		JSONArray intimes=new JSONArray();
		JSONArray outtimes=new JSONArray();
		DbConnect ob = DbConnect.getInstance();
		Connection con = ob.connectDB();
		String logintime="",logouttime="",scheduleid="";
		Statement st=null,st1=null,st2=null,st3=null;
		ResultSet rs = null,rs1=null,rs2=null,rs3=null;
		boolean isSchedule=false;
		try{
			SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy");
			SimpleDateFormat dbsdf=new SimpleDateFormat("yyyy-MM-dd");
			date=dbsdf.format(sdf.parse(date));
			String query="SELECT ES.ID AS schid,ES.LOGIN_TIME,LOGOUT_TIME FROM EMPLOYEE_SCHEDULE ES JOIN EMPLOYEE E ON E.ID=ES.EMPLOYEE_ID JOIN EMPLOYEE_IMEI EI ON EI.EMPID=E.ID WHERE EI.IMEI='"+imei+"' AND ES. STATUS='a' AND ES.from_date<='"+date+"' and ES.to_date>='"+date+"'";

		//String query="SELECT ES.ID AS schid,ES.LOGIN_TIME,LOGOUT_TIME FROM EMPLOYEE_SCHEDULE ES JOIN EMPLOYEE E ON E.ID=ES.EMPLOYEE_ID JOIN EMPLOYEE_IMEI EI ON EI.EMPID=E.ID WHERE EI.IMEI='"+imei+"' AND ES. STATUS='a' AND ((from_date between '"+date+"' and '"+date+"') or( to_date between '"+date+"' and '"+date+"'))";
			st=con.createStatement();
			rs=st.executeQuery(query);
			if(rs.next()){
				isSchedule=true;
				logintime=rs.getString("login_time");
				logouttime=rs.getString("logout_time");
				scheduleid=rs.getString("schid");
				
			}
			if(!scheduleid.equalsIgnoreCase("")){
			String alterquery="SELECT ID,LOGIN_TIME,LOGOUT_TIME FROM EMPLOYEE_SCHEDULE_ALTER WHERE STATUS='a' and SCHEDULEID="+scheduleid+" AND DATE='"+date+"'";
			System.out.println("query"+alterquery);
			st1=con.createStatement();
			rs1=st1.executeQuery(alterquery);
			if(rs1.next()){
				logintime=rs1.getString("login_time");
				logouttime=rs1.getString("logout_time");
			}
			}
			st2 = con.createStatement();
			String squery = "";
			String projectid = null;

			
			String checkquery = "select project from employee where id in (select empid from employee_imei where imei='" + imei+"')";
			rs2 = st2.executeQuery(checkquery);
			while (rs2.next()) {
			
				projectid=rs2.getString("project");
			}
			
			if(SettingsConstant.comp.equalsIgnoreCase("visa") && projectid!=null)
			{
				squery="select logtime,logtype from  atsconnect at join project_shifttime pt on pt.project=at.project join logtime lt on lt.id=pt.shifttime where  at.project='"+projectid+"' and lt.status='active' ";
			}
				
			else{
				squery = "select  logtime,logtype from site_shift ss,logtime lt where lt.id=ss.shiftid and lt.status='active'";
			}
			
			if(SettingsConstant.comp.equalsIgnoreCase("visa"))
			{
				if(!projectid.equalsIgnoreCase("6") )//for O&I project we are not sending logs as they cant book.
				{
				st3 = con.createStatement();
				rs3 = st3.executeQuery(squery);
				while (rs3.next()) {

					if (rs3.getString("logtype").equalsIgnoreCase("IN")) {
						intimes.put(rs3.getString("logtime"));
					} else {

						outtimes.put(rs3.getString("logtime"));
					}

				}
				}
			}	
			else{
				
				st3 = con.createStatement();
				rs3 = st3.executeQuery(squery);
				while (rs3.next()) {

					if (rs3.getString("logtype").equalsIgnoreCase("IN")) {
						intimes.put(rs3.getString("logtime"));
					} else {

						outtimes.put(rs3.getString("logtime"));
					}

				}
			}
					
			
			outtimes.put("cancel");
			intimes.put("cancel");
			outtimes.put("weekly off");
			intimes.put("weekly off");
			
			retJson.put("RESULT", "TRUE");
			retJson.put("LOGINTIME", logintime);
			retJson.put("LOGOUTTIME", logouttime);
			retJson.put("OUT_LOGS",outtimes);
			retJson.put("IN_LOGS", intimes);
			retJson.put("SCHEDULEID", scheduleid);
		}catch(Exception e){e.printStackTrace();
		try{
		retJson.put("RESULT", "FALSE");
		}catch(Exception e1){e1.printStackTrace();}
		}
		return retJson;
	}
	public JSONObject getHelpDeskDetails(){
		
		DbConnect ob = null;
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		
		JSONObject rObj=new JSONObject();
		try
		{
			
			ob = DbConnect.getInstance();
			con = ob.connectDB();
			st=con.createStatement();
		rs=st.executeQuery("select * from helpcontact limit 2 ");
		String Result="FALSE";
		int i=1;
		while (rs.next())
		{	
		
		rObj.put("CONTACT_NAME"+i,rs.getString("name"));
		rObj.put("CONTACT_NUMBER"+i,rs.getString("number1") );
		rObj.put("CONTACT_EMAIL"+i, rs.getString("emailAddress"));
		i++;
		}

       if(i>1){
    	   Result="TRUE";
          }
       rObj.put("RESULT",Result);
       
		
		}
		catch(Exception e){
			
			e.printStackTrace();
			try {
				rObj.put("RESULT", "false");
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		}
		finally{
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);
			
		}
		
		return rObj;

		
	}

	public int stopPanic(String empid, String tripid, String alarmcause,
			String primaryaction) {
		
	
		
		TripDetailsDto  tdto = new PanicDao().getPanicTripDetails(tripid);
		PanicDto panicDto = new PanicDto();
		panicDto.setAlarmCause(alarmcause);
		panicDto.setTripId(tripid);
		panicDto.setPrimaryActiontakenBy(empid);
		panicDto.setPrimaryAction(primaryaction);
		
		
		
		
		
		if(new PanicDao().checkprimaryaction(tripid)<1)
		{
		int status = new PanicDao().TakePanicAction(panicDto); //taking primary action
		}
		int returnval = new PanicDao().PanicStopAction(tdto.getPanicdto().getId(),tripid,empid);	//closing panic
		
		return returnval;
	}

	public JSONObject getPanicTripDetails(String tripid) {
		JSONObject robj=null;
		try{
			
		robj=new JSONObject();
		JSONArray EMPNAME = new JSONArray();
		JSONArray EMPID = new JSONArray();
		JSONArray EMPGENDER = new JSONArray();
		JSONArray EMPCONTACT = new JSONArray();
		
		
		TripDetailsDto  tdto = new PanicDao().getPanicTripDetails(tripid);
		
		
		
		if(tdto!=null)
			
		{
			
		robj.put("RESULT","TRUE");
		robj.put("STATUS","PANICTRIP DETAILS");
		robj.put("TRIPID", tdto.getId());
		robj.put("DRIVERNAME",tdto.getDriverName());
		robj.put("DRIVERNUMBER",tdto.getDriverContact());
		robj.put("VEHICLENUMBER",tdto.getVehicleNo());
		robj.put("VENDORNAME", tdto.getVendorName());
		robj.put("VENDORCONTACT", tdto.getVendorContact());
		robj.put("ESCORTNAME",tdto.getEscortName());
		robj.put("ESCORTNUMBER",tdto.getEscortContact());
		robj.put("ACTIVATEDTIME",tdto.getTrip_time());
		
		
		
		
		
	
		
		
		for (TripDetailsChildDto childDto : tdto.getTripDetailsChildDtoList() ) {
		
			
			
			EMPNAME.put(childDto.getEmployeeName());
			EMPID.put(childDto.getEmployeeId());
			EMPGENDER.put(childDto.getGender());
			EMPCONTACT.put(childDto.getContactNumber());
			
			
		}
		
		robj.put("EMPNAME", EMPNAME);
		robj.put("EMPID", EMPID);
		robj.put("EMPGENDER", EMPGENDER);
		robj.put("EMPCONTACT", EMPCONTACT);
		
		robj.put("PRIMARYACTION", tdto.getPanicdto().getPrimaryAction());
		robj.put("ALARMCAUSE",tdto.getPanicdto().getAlarmCause());
		robj.put("PRIMARYACTIONTAKENBY",tdto.getPanicdto().getPrimaryActiontakenByName());
		
		}
		
		else{
			robj.put("RESULT","FALSE");
			robj.put("STATUS","UNABLE TO FETCH PANICTRIP DETAILS");
			
			
		}
		
		
		System.out.println(robj);
		
		
		}
		catch(Exception e){
			e.printStackTrace();
			System.out.println(e);
		}
		
		return robj;
	}

	/*public JSONObject getTripHistory(String imei) {
		DbConnect ob = DbConnect.getInstance();
		Connection con = ob.connectDB();
		PreparedStatement pst = null;
		Statement st=null;
		ResultSet rs = null,rs1=null;
		JSONObject json = new JSONObject();
		JSONArray tripDate = new JSONArray();
		JSONArray distance = new JSONArray();
		JSONArray area = new JSONArray();
		JSONArray log= new JSONArray();
		int branch=1;
		String sitelandmrk="";
		int cutOff = Integer
				.parseInt(SettingsConstant.andrioidTripDetailsCount);
		try {
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DAY_OF_MONTH, -1);
			cal.getTime();
			System.out.println(cal.getTime());
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			String curdate = format.format(cal.getTime());
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.DAY_OF_MONTH, -cutOff);
			String previousdate = format.format(calendar.getTime());
			String getDetails = "select DATE_FORMAT(td.trip_date,'%d-%m-%Y')as tripDate,td.trip_log,tdc.dist,tdc.landmarkId,l.landmark,a.area from trip_details td,trip_details_child tdc,employee_imei im,landmark l,place p,area a  where td.id=tdc.tripId and tdc.employeeId=im.empid and a.id=p.area and p.id=l.place and l.id=tdc.landmarkId and td.trip_date between '"
					+ previousdate
					+ "'and '"
					+ curdate
					+ "' and im.imei='"
					+ imei + "' order by td.id";
			String getsitelandmak="select landmark from site where branch="+branch;
			st=con.createStatement();
			rs1=st.executeQuery(getsitelandmak);
			if(rs1.next()){
				sitelandmrk=rs1.getString("landmark");
			}
			System.out.println(getDetails);
			pst = con.prepareStatement(getDetails);
			rs = pst.executeQuery();
			int i=0;
			while (rs.next()) {
				i++;
				tripDate.put(rs.getString("tripDate"));
				log.put(rs.getString("trip_log"));
				if(Float.parseFloat(rs.getString("dist"))>0){
					distance.put(rs.getString("dist"));
				}else{
					float dist = new DistanceListDao().getGooglMapDistance(sitelandmrk,rs.getString("landmarkId"));
					distance.put(Float.toString(dist));
				}
				
				area.put(rs.getString("area"));
				}
			if(i==0){
				json.put("RESULT", "false");
			}else{
				json.put("RESULT", "true");
			}
			json.put("tripDate", tripDate);
			json.put("distance", distance);
			json.put("tripshift", log);
			json.put("area", area);
			
		} catch (Exception e) {
			System.out
					.println("Error in AndroidAppForEmployeeDao@getTripHistory"
							+ e);
		} finally {
			DbConnect.closeResultSet(rs,rs1);
			DbConnect.closeStatement(pst,st);
			DbConnect.closeConnection(con);
		}
		return json;
	}*/
	/*public JSONObject getSchedules(String imei){
		DbConnect ob = DbConnect.getInstance();
		Connection con = ob.connectDB();
		Statement st=null,st1=null;
		ResultSet rs = null,rs1=null;
		JSONObject json=new JSONObject();
		boolean isScheduled=false;
		try{		
		JSONArray dates=new JSONArray();
		JSONArray intimes=new JSONArray();
		JSONArray outtimes=new JSONArray();
		Date dt=new Date();
		String todaydate=new SimpleDateFormat("yyyy-MM-dd").format(dt);
		String query ="select *,es.id as schid from employee_imei ei join employee e on e.id =ei.empid join employee_schedule es on es.employee_id=e.id where ei.imei='"+imei+"' and es.status='a' and  (from_date>='"+todaydate+"' or to_date>='"+todaydate+"')";
		st=con.createStatement();
		rs=st.executeQuery(query);
		List<ScheduledEmpDto> eslist=new ArrayList<ScheduledEmpDto>();
		while(rs.next()){
		isScheduled=true;
		ScheduledEmpDto dto=new ScheduledEmpDto();
		dto.setScheduleId(rs.getString("schid"));
		dto.setFrom_date(rs.getString("from_date"));
		dto.setTo_date(rs.getString("to_date"));
		dto.setLoginTime(rs.getString("login_time"));
		dto.setLogoutTime(rs.getString("logout_time"));
		eslist.add(dto);
		}
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-M-d");	
		SimpleDateFormat dbsdf=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String schedulealterquery="";
			for(ScheduledEmpDto es:eslist){
			long timediff=dbsdf.parse(es.getTo_date()).getTime()-dbsdf.parse(es.getFrom_date()).getTime();
			int daysdiff=Integer.parseInt(""+TimeUnit.DAYS.convert(timediff, TimeUnit.MILLISECONDS));
			schedulealterquery="select * from employee_schedule_alter where status='a' and scheduleid="+es.getScheduleId();
			System.out.println(schedulealterquery);
			List<ScheduleAlterDto> esalter=new ArrayList<ScheduleAlterDto>();
			st1=con.createStatement();
			rs1=st1.executeQuery(schedulealterquery);
			while(rs1.next()){
				ScheduleAlterDto schalter=new ScheduleAlterDto();
				schalter.setDate(rs1.getString("date"));
				schalter.setLoginTime(rs1.getString("login_time"));
				schalter.setLogoutTime(rs1.getString("logout_time"));
				esalter.add(schalter);
			}
			for(int i=0;i<=daysdiff;i++){
				Date d=new Date(dbsdf.parse(es.getFrom_date()).getTime()+ TimeUnit.DAYS.toMillis( i ));
				System.out.println("gggg"+esalter.size());
				if(esalter.size()>0){
				for(ScheduleAlterDto esal:esalter){
					System.out.println("alter date"+esal.getDate());
					if(dbsdf.parse(esal.getDate()).getDate()==d.getDate()){
							dates.put(sdf.format(sdf.parse(esal.getDate())));
							outtimes.put(esal.getLogoutTime());
							intimes.put(esal.getLoginTime());
					}
					else{
						dates.put(sdf.format(d).toString());
						outtimes.put(es.getLogoutTime());
						intimes.put(es.getLoginTime());
					}
				}
				}else
				{
					dates.put(sdf.format(d));
					outtimes.put(es.getLogoutTime());
					intimes.put(es.getLoginTime());
				}
			}
			}
			if(isScheduled){
		json.put("DATES",dates);
		json.put("RESULT", "TRUE");
		json.put("IN_TIMES",intimes);
		json.put("OUT_TIMES", outtimes);
			}
			else{
				json.put("RESULT", "FALSE");
				json.put("MESSAGE", "NO SCHEDULES FOUND!");
			}
		
		}catch(Exception e){
			e.printStackTrace();
			try{
			json.put("RESULT", "FALSE");
			json.put("MESSAGE", e);
			}catch(Exception e1){e1.printStackTrace();}
		}
		finally{
			DbConnect.closeResultSet(rs,rs1);
			DbConnect.closeStatement(st,st1);
			DbConnect.closeConnection(con);
		}
		return json;
	}
*/
	public Map panicactivatedIVR( String empid, String tripid,
			String Lat, String Long, String time) {
		int returnint = 0;
		DbConnect ob = DbConnect.getInstance();
		Connection con = ob.connectDB();

		PreparedStatement pst = null, pst1 = null;
		ResultSet rs = null;
		String[] contacts = null;
		try {
			contacts = new SMSService().sendSMSOnPanicEmployeeIVR(empid, tripid);

			/*	String panicquery = "INSERT INTO EMPLOYEE_PANIC(EMPID,IMEI,TRIPID,LATITUDE,LONGITUDE,PANICTIME,STATUS,PANICDATE) VALUES(?,'NULL',?,?,?,?,?,CURDATE())";

			pst = con.prepareStatement(panicquery);
			pst.setString(1, empid);
		//	pst.setString(2, imei);
			pst.setString(2, tripid);
			pst.setString(3, Lat);
			pst.setString(4, Long);
			pst.setString(5, time);
			pst.setString(6, "activated");
			returnint = pst.executeUpdate();

			String driverquery = "select imei_number from driver_logins dl join trip_details td on dl.vehicleid=td.vehicle and td.id= '"
					+ tripid + "'  order by  dl.datetime  desc limit 1";
			pst1 = con.prepareStatement(driverquery);
			rs = pst1.executeQuery();
			String driverImei = "";
			if (rs.next()) {
				driverImei = rs.getString("imei_number");
			}

		new TripServiceDao().panicAlarm(driverImei, empid);
*/
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);
		}
		
		
		Map mp = new HashMap();
		mp.put("result_int", returnint);
		mp.put("contactstring", contacts);
		return mp;
	}

	public JSONObject getTripDetailsIVR(String phonenumber, String auth) {
		TripDetailsDto dto = new TripDetailsDto();
		DbConnect ob = DbConnect.getInstance();
		Connection con = ob.connectDB();
		PreparedStatement pst = null;
		PreparedStatement pst2 = null;
		ResultSet rs = null;
		ResultSet rs2 = null;
		JSONObject jobj=new JSONObject();
		try {
			if (auth != null && auth.equalsIgnoreCase("yes")) {
				updateAuthenticate(phonenumber);
			}
			//String query = "select ei.id as tripid,td.id,td.trip_code,DATE_FORMAT(td.trip_date, \"%d-%m-%Y\") as trip_date,td.trip_time,td.trip_log,td.security_status,v.regno,d.name as drivername,d.contact as drivercontact,e.name as escortname,e.phone as escortcontact,vtp.status from trip_details td join trip_details_child tc on tc.tripid=td.id join employee ei on  (ei.contactnumber1=? OR ei.contactnumber2=?) join vendor_trip_sheet_parent vtp on vtp.status!='initial' and vtp.tripid=td.id join vehicles v on v.id=td.vehicle left join escort e on e.id=td.escortid join driver d on d.id=td.driverid and td.trip_date between  DATE_ADD(CURDATE(),INTERVAL -1 DAY) and curdate() order by td.id desc  ";
			String query ="select vts.curStatus as emplsurstatus,ei.EmployeeFirstName as empname,ei.id as empid,td.id as tripid,td.trip_code,DATE_FORMAT(td.trip_date,\"%d-%m-%Y\") as trip_date,td.trip_time,td.trip_log,td.security_status,v.regno,d.name as drivername,d.contact as drivercontact,e.name as escortname,e.phone as escortcontact,vtp.status from trip_details td join trip_details_child tc on tc.tripid=td.id join employee ei on ei.id=tc.employeeid join vendor_trip_sheet_parent vtp on vtp.tripid=td.id and vtp.status!='stopped'  join vendor_trip_sheet vts on vts.tripId=td.id and vts.employeeId=ei.id  join vehicles v on v.id=td.vehicle left join escort e on e.id=td.escortid join driver d on d.id=td.driverid where (ei.contactnumber1=? or ei.contactnumber2=?) and CAST(CONCAT(CAST(td.trip_date as date),' ',td.trip_time,':00') as datetime) > DATE_ADD(NOW(), INTERVAL -3 HOUR) order by td.id desc";
			pst = con.prepareStatement(query);
			pst.setString(1, phonenumber);
			pst.setString(2, phonenumber);
			rs = pst.executeQuery();
			
			String query1 = "select  l.latitude,l.longitude from (select id from employee where contactnumber1=? OR contactnumber2=?) e join employee_subscription es on es.empid=e.id join landmark l on l.id =es.landmark  ";
			pst2 = con.prepareStatement(query1);
			pst2.setString(1, phonenumber);
			pst2.setString(2, phonenumber);
			rs2=pst2.executeQuery();
			
			
			
			//System.out.println("the value is "+o+"heee"+t);
			if (rs.next() &&rs2.next()) {

				
				if (rs.getString("status").equalsIgnoreCase("started")) {

					String emplsurstatus= rs.getString("emplsurstatus")==null ? "":rs.getString("emplsurstatus");
					
					/*dto.setId(rs.getString("tripid"));
					dto.setTrip_code(rs.getString("trip_code"));
					dto.setTrip_time(rs.getString("trip_time"));
					dto.setTrip_date(rs.getString("trip_date"));
					dto.setTrip_log(rs.getString("trip_log"));
					dto.setIsSecurity(rs.getString("security_status"));
					dto.setVehicleNo(rs.getString("regno"));
					dto.setDriverName(rs.getString("drivername"));
					dto.setDriverContact(rs.getString("drivercontact"));
					dto.setEscortName(rs.getString("escortname"));
					dto.setEscortContact(rs.getString("escortcontact"));
					dto.setDoneBy(rs.getString("empid"));
					dto.setTripDetailsChildDtoList(new AndroidAppForEmployeeDao().getTripEmployees(dto.getId()));*/
				    jobj= getVehiclePosition(rs.getString("tripid")) ;
					jobj.put("value_status", "found");
					jobj.put("cab_number", rs.getString("regno"));
					jobj.put("driver_name", rs.getString("drivername"));
					jobj.put("driver_contact", rs.getString("drivercontact"));
					jobj.put("trip_time", rs.getString("trip_time"));
					jobj.put("trip_date", rs.getString("trip_date"));
					jobj.put("empllat", rs2.getString("latitude"));
                    jobj.put("emplng", rs2.getString("longitude"));
                    jobj.put("tripid", rs.getString("tripid"));
                    jobj.put("empid", rs.getString("empid"));
                    jobj.put("empname", rs.getString("empname"));
                    jobj.put("emplsurstatus",emplsurstatus);
                    //jobj.put("", value)
				}else{
					
					jobj.put("value_status", "found_not_started");
				    jobj.put("cab_number", rs.getString("regno"));
					jobj.put("driver_name", rs.getString("drivername"));
					jobj.put("driver_contact", rs.getString("drivercontact"));
					jobj.put("trip_time", rs.getString("trip_time"));
					jobj.put("trip_date", rs.getString("trip_date"));
					
					
					
				}
			}else{
				
				jobj.put("value_status", "not_found");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);
		}
		return jobj;
	}
	public boolean checkAdhocDate(String travelDate,String TravelTime,String AdhocType,String site,String projectUnnit){
		Boolean flag=true;
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		String curdate = format.format(cal.getTime());
		Calendar cal2 = Calendar.getInstance();
		cal2.add(Calendar.DAY_OF_MONTH, 1);
		String nxtDate = format.format(cal2.getTime());
		ArrayList<AdhocDto> SetupDetails=new AdhocService().getSetupDetails(AdhocType,site,projectUnnit);
		int cuttoff=0;
		for(AdhocDto adto : SetupDetails){
		if(adto.getAdhocType().equalsIgnoreCase(AdhocType) && adto.getPickupDrop().equalsIgnoreCase("drop")){
			cuttoff=Integer.parseInt(adto.getRequestCutoff().split(":")[0]);
		}
		}
		int cutoffMin=Integer.parseInt(SetupDetails.get(0).getRequestCutoff().split(":")[1]);
		SimpleDateFormat format1 = new SimpleDateFormat("HH");
		String curTime = format1.format(cal.getTime());
		SimpleDateFormat format2 = new SimpleDateFormat("mm");
		int curMin =Integer.parseInt( format2.format(cal.getTime()));
		if(curdate.equalsIgnoreCase(travelDate)){
			if(Integer.parseInt(TravelTime.split(":")[0])-Integer.parseInt(curTime)<cuttoff){
				flag=false;
			}else if(Integer.parseInt(TravelTime.split(":")[0])-Integer.parseInt(curTime)==cuttoff && Integer.parseInt(TravelTime.split(":")[1])-curMin <cutoffMin){
				flag=false;
			}
		}else if (nxtDate.equalsIgnoreCase(travelDate)){
			if(((Integer.parseInt(TravelTime.split(":")[0]))+24)-(Integer.parseInt(curTime))<cuttoff){
				flag=false;
			}else if(((Integer.parseInt(TravelTime.split(":")[0]))+24)-(Integer.parseInt(curTime))==cuttoff && Integer.parseInt(TravelTime.split(":")[1])-curMin <cutoffMin){
				flag=false;
			}
		}
		return flag;
	}
	public ScheduleAlterDto getScheduleCutOff(){
		ScheduleAlterDto dto = new ScheduleAlterDto();
		
		
			DbConnect ob = DbConnect.getInstance();		
			Connection con = ob.connectDB();
			Statement st = null;
			ResultSet rs=null;
			try {
				if(SettingsConstant.scheduleSingleDayCutOff.equalsIgnoreCase("YES")){
				String qry="select login_cutoff,logout_cutoff from schedule_cutoff limit 1";
				st = con.createStatement();
				rs=st.executeQuery(qry);
				if(rs.next()){
					dto.setLoginTime(rs.getString("login_cutoff"));
					dto.setLogoutTime(rs.getString("logout_cutoff"));
				}
				
				}else{
					dto.setLoginTime("0");
					dto.setLogoutTime("0");
				}
			} catch (SQLException e) {
				System.out.println("Error in getScheduleCutOff"+e);
			}finally{
				DbConnect.closeResultSet(rs);
				DbConnect.closeStatement(st);
				DbConnect.closeConnection(con);
			}
		
		return dto;
	}
}
