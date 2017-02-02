package com.agiledge.atom.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.agiledge.atom.constants.SettingsConstant;
import com.agiledge.atom.dbConnect.DbConnect;
import com.agiledge.atom.service.ShuttleServcie;
import com.agiledge.atom.service.SocketDeviceService;
import com.agiledge.atom.sms.SMSService;
import com.agiledge.atom.sms.SendSMS;
import com.agiledge.atom.sms.SendSMSFactory;

public class SocketDeviceDao {

	public void insertGPSData(String deviceNo, String latitude, String longitude) {
		String vid = "";
		String tripId = "";
		String logStatus = "run";
		DbConnect ob = DbConnect.getInstance();
		Connection con = null;
		Statement st = null, st1 = null, st2 = null;
		ResultSet rs = null, rs1 = null;
		try {
			System.out.println("GOT DATA");
			con = ob.connectDB();
			st = con.createStatement();
			st1 = con.createStatement();
			st2 = con.createStatement();
			if (SettingsConstant.deviceMap.size() > 0) {
				//tripId = SettingsConstant.deviceMap.get(deviceNo);
				//logStatus = SettingsConstant.tripMap.get(tripId);
			}
			// when server restarts
			if (tripId == "" || tripId == null) {
				String selectquery = "select vtps.tripid,vtps.vehicleNo from vendor_trip_sheet_parent vtps,vehicles v  where vtps.vehicleNo=v.id and vtps.status='started' and v.device_num='"
						+ deviceNo + "'";
				rs = st.executeQuery(selectquery);
				while (rs.next()) {
					tripId = rs.getString("tripid");
					vid = rs.getString("vehicleNo");
					SettingsConstant.deviceMap.put(deviceNo, tripId);
					String getLog = "select logstatus from vehicle_position where vehicleId="
							+ vid
							+ " and tripId='"
							+ tripId
							+ "' order by id desc limit 1";
					rs1 = st2.executeQuery(getLog);
					if (rs1.next()) {
						logStatus = rs1.getString("logstatus");
						SettingsConstant.tripMap.put(tripId, logStatus);
					}
				}
			}
			if (tripId == "" || tripId == null) {
				tripId = "0";
			}
			System.out.println("cameout");
			String query = "INSERT INTO VEHICLE_POSITION(vehicleid,date_time,lattitude,longitude,logstatus,tripid,status) VALUES((select id from vehicles where device_num='"
					+ deviceNo
					+ "'),now(),'"
					+ latitude
					+ "','"
					+ longitude
					+ "','" + logStatus + "','" + tripId + "','checkedin') ";
			st1.executeUpdate(query);
			System.out.println("DATA INSERTED" + query);
		} catch (Exception e) {
			System.out.println("Error in SocketDeviceDao@insertGPSData" + e);
		} finally {
			DbConnect.closeResultSet(rs, rs1);
			DbConnect.closeStatement(st, st1, st2);
			DbConnect.closeConnection(con);
		}
	}

	public void updateemployeeLogin(String rfid, String deviceNo,
			String[] geocodes) {
		DbConnect ob = DbConnect.getInstance();
		Connection con = null;
		Statement st = null, st1 = null, st2 = null, st3 = null, st4 = null;
		PreparedStatement pst = null;
		ResultSet rs = null, rs1 = null, rs2 = null, rs3 = null;
		try {

			System.out.println("LOGINNNNNNNNNN");
			String tripId = "";
			con = ob.connectDB();
			st = con.createStatement();
			st1 = con.createStatement();
			st3 = con.createStatement();
			st4 = con.createStatement();
			// getting current log
			String logType = "";
			String logTime = "";
			String selectlog = "(select logtime,logtype from logtime where status='active' and logtype='OUT' and logtime between date_format((NOW()-INTERVAL 40 minute) ,'%H:%i') and date_format((NOW()+INTERVAL 10 minute) ,'%H:%i') order by logtime desc limit 1) union (select logtime,logtype from logtime where status='active' and logtype='IN' and CONCAT(logtime,':00') > CONCAT(curtime(),'')  order by logtime limit 1)";
			rs2 = st3.executeQuery(selectlog);
			if (rs2.next()) {
				logTime = rs2.getString("logtime");
				logType = rs2.getString("logtype");
			}
			String getTripId = "select t.id from trip_details t,vendor_trip_sheet_parent vt,vehicles v where t.id=vt.tripid  and t.trip_time='"
					+ logTime
					+ "' and t.vehicle=v.id and v.device_num= '"
					+ deviceNo + "' and vt.status='started'";
			rs3 = st4.executeQuery(getTripId);
			if (rs3.next()) {
				tripId = rs3.getString("id");
			} else {
				tripId = new ShuttleServcie().createShuttle(deviceNo, logType,
						logTime);
			}
			String selectquery = "SELECT VTS.EMPLOYEEID,VTS.SHOWSTATUS FROM VENDOR_TRIP_SHEET VTS JOIN EMPLOYEE E ON E.ID=VTS.EMPLOYEEID WHERE VTS.TRIPID="
					+ tripId + " AND  E.RFID_CODE='" + rfid + "'";
			System.out.println(selectquery);
			rs = st.executeQuery(selectquery);
			if (rs.next()) {
				if (rs.getString("showstatus").equalsIgnoreCase("No Show")) {
					String updatequery = "UPDATE VENDOR_TRIP_SHEET SET SHOWSTATUS='Show',LATITUDE='"
							+ geocodes[0]
							+ "',LONGITUDE='"
							+ geocodes[1]
							+ "',intime=curtime() WHERE TRIPID='"
							+ tripId
							+ "' AND EMPLOYEEID='" + rs.getString("employeeid")+"'";
					System.out.println("updated..........." + updatequery);
				}

			} else {
				int count = 1;
				st2 = con.createStatement();
				String countquery = "SELECT INSERTEDORDER FROM VENDOR_TRIP_SHEET WHERE TRIPID="
						+ tripId + " ORDER BY INSERTEDORDER DESC";
				rs1 = st2.executeQuery(countquery);
				if (rs1.next()) {
					count = rs1.getInt("INSERTEDORDER") + 1;
				}
				String distance =Float.toString(new ShuttleDao().getDistance(count, tripId, logType,rfid));
				String time = Double.toString(new ShuttleDao().getTime(distance));

				pst = con
						.prepareStatement("INSERT INTO trip_details_child (tripId, employeeId, routedOrder, landmarkId, time, dist, transportType) VALUES (?, (SELECT ID FROM EMPLOYEE WHERE RFID_CODE=?), ?,(select landMark from employee where rfid_code=?), ?, ?, 'shift')");
				pst.setString(1, tripId);
				pst.setString(2, rfid);
				pst.setInt(3, count);
				pst.setString(4, rfid);
				pst.setString(5, time);
				pst.setString(6, distance);
				pst.executeUpdate();

				String insertVTSquery = "INSERT INTO VENDOR_TRIP_SHEET(TRIPID,EMPLOYEEID,SHOWSTATUS,LATITUDE,LONGITUDE,INSERTEDORDER,INTIME) VALUES("
						+ tripId
						+ ",(SELECT ID FROM EMPLOYEE WHERE RFID_CODE='"
						+ rfid
						+ "'),'Show','"
						+ geocodes[0]
						+ "','"
						+ geocodes[1]
						+ "'," + count + ",curtime())";
				st1.executeUpdate(insertVTSquery);
				System.out.println("inserted..........." + insertVTSquery);

			}
			System.out.println("LOGINN cmplt");
		} catch (Exception e) {
			System.out.println("Error SocketDeviceDao@updateemployeeLogin" + e);
		} finally {
			DbConnect.closeResultSet(rs, rs1, rs2, rs3);
			DbConnect.closeStatement(st, st1, st2, st3, st4);
			DbConnect.closeConnection(con);
		}
	}

/*	public String getDisanceAndTimeFromLastEmployee(int routedOrder,
			String tripId, String rfid) {
		DbConnect ob = DbConnect.getInstance();
		Connection con = null;
		Statement st = null, st1 = null;
		ResultSet rs = null, rs1 = null;
		String src = null, dest = null;
		float distance = 0, time = 0;
		String distTime = null;
		try {
			con = ob.connectDB();
			st = con.createStatement();
			st1 = con.createStatement();
			if (routedOrder == 1) {
				src = "(select landmark from site where id=1)";
			} else {
				src = "(select landmarkId from trip_details_child where tripId='"
						+ tripId + "' and routedOrder='" + routedOrder + "')";
			}
			dest = "(select landMark from employee where rfid_code='" + rfid
					+ "')";
			String qry = "select distance from distchart where (srcId=" + src
					+ " and destId=" + dest + ") or (destId=" + src
					+ " and srcId=" + dest + ")";
			System.out.println(qry);
			rs = st.executeQuery(qry);
			if (rs.next()) {
				distance = rs.getFloat("distance"); 
			}
			String gettingTime = "select ROUND( "
					+ distance
					+ "/speedpkm*60,0)as time from timeSloat where CONCAT(startTime,':00') <= CONCAT(curtime(),'') and CONCAT(endTime,':00') > CONCAT(curtime(),'')";
			rs1 = st1.executeQuery(gettingTime);
			if (rs1.next()) {
				time = rs1.getFloat("time");
			}
			distTime = distance + "#" + time;
		} catch (Exception e) {
			System.out
					.println("Error SocketDeviceDao@getDisanceFromLastEmployee"
							+ e);
		} finally {
			DbConnect.closeResultSet(rs, rs1);
			DbConnect.closeStatement(st, st1);
			DbConnect.closeConnection(con);
		}

		return distTime;
	}
*/
	public void panicactivated(String deviceNo, String latitude,
			String longitude) {

		String tripId = "";
		SettingsConstant.tripMap.put(tripId, "danger");
		DbConnect ob = DbConnect.getInstance();
		Connection con = null;
		Statement st = null, st1 = null, st2 = null;
		ResultSet rs = null;
		try {
			con = ob.connectDB();
			st = con.createStatement();
			st1 = con.createStatement();
			st2 = con.createStatement();
			String insertquery = "INSERT INTO VEHICLE_POSITION(vehicleid,date_time,lattitude,longitude,logstatus,tripid,status) VALUES((select id from vehicles where device_num='"
					+ deviceNo
					+ "'),now(),'"
					+ latitude
					+ "','"
					+ longitude
					+ "','danger','" + tripId + "','checkedin') ";
			String getVehicle = "select regNo from vehicles where device_num='"
					+ deviceNo+"'";
			String insertPanication = "INSERT INTO panicaction (tripId, curStatus) VALUES ('"+ tripId +"', 'open')";
			String regNo = "";
			
			st.executeUpdate(insertquery);
			rs = st1.executeQuery(getVehicle);
			if (rs.next()) {
				regNo = rs.getString("regNo");
			}
			// for panic notification
			st2.executeUpdate(insertPanication);
			String time = "";
			Calendar cal = Calendar.getInstance();
			cal.setTime(new Date());
			int hour = cal.get(Calendar.HOUR);
			int minute = cal.get(Calendar.MINUTE);
			time = "" + (hour < 10 ? "0" + hour : hour);
			time = time + ":" + (minute < 10 ? "0" + minute : minute);
			int returnVal = new SMSService().sendSMSOnPanicForShuttle(regNo,
					time, deviceNo);
			if (returnVal > 0) {
				System.out.println("Sent messages");
			}

		} catch (Exception e) {
			System.out.println("Error SocketDeviceDao@panicactivated" + e);
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st, st1, st2);
			DbConnect.closeConnection(con);
		}
	}

	public void stopTrip(String deviceNo, String lat, String lng) {
		DbConnect ob = DbConnect.getInstance();
		Connection con = null;
		Statement st = null, st1 = null,st2=null;
		ResultSet rs = null, rs1 = null,rs2=null;
		String site="1";
		try {
			con = ob.connectDB();
			st = con.createStatement();
			String getlogquery = "select logtime from logtime where status='active' and logtime between date_format(NOW() ,'%H:%i') and date_format((NOW()+INTERVAL 10 minute) ,'%H:%i') and logtype='IN'";
			rs = st.executeQuery(getlogquery);
			if (rs.next()) {
				String latlngqry1="select l.latitude,l.longitude from site s,landmark l where l.id=s.landmark and s.id='"+site+"'";
				st2=con.createStatement();
				rs2=st2.executeQuery(latlngqry1);
				if(rs2.next()){
					double distance1 = new DistanceListDao()
					.getDistanceWithoutMap(Double.parseDouble(lat),
							Double.parseDouble(lng), Double
									.parseDouble(rs2
											.getString("latitude")),
							Double.parseDouble(rs2
									.getString("longitude")));
					if(distance1 < 0.2){
						new ShuttleServcie().stopShuttle(deviceNo);
					}
				}
				
			} else {
				String getLastLandmk = "select latitude,longitude from landmark where id=(select r.landMarkId from routechild r, vehicle_shift_map vs,vehicles v where r.routeId=vs.routeId and vs.vehicleId=v.id and  v.device_num='"
						+ deviceNo + "' order by r.position desc limit 1)";
				st1 = con.createStatement();
				rs1 = st1.executeQuery(getLastLandmk);
				while (rs1.next()) {
					double distance = new DistanceListDao()
							.getDistanceWithoutMap(Double.parseDouble(lat),
									Double.parseDouble(lng), Double
											.parseDouble(rs1
													.getString("latitude")),
									Double.parseDouble(rs1
											.getString("longitude")));
					if (distance < 0.5) {
						new ShuttleServcie().stopShuttle(deviceNo);
					}
				}
			}
		} catch (SQLException e) {
			System.out.println("Error SocketDeviceDao@stopTrip" + e);
		} finally {
			DbConnect.closeResultSet(rs, rs1,rs2);
			DbConnect.closeStatement(st, st1,st2);
			DbConnect.closeConnection(con);
		}

	}

	public int vehicleAlertSms(String deviceNo, String lat, String lng) {
		DbConnect ob = DbConnect.getInstance();
		Connection con = null;
		Statement st = null, st1 = null;
		ResultSet rs = null, rs1 = null;
		try {
			if(SettingsConstant.deviceMap.get(deviceNo)!=null){
			con = ob.connectDB();
			st = con.createStatement();
			ArrayList<String> empid = new ArrayList<String>();
			ArrayList<String> contact = new ArrayList<String>();
			ArrayList<String> empName = new ArrayList<String>();
			ArrayList<String> vehicleNo = new ArrayList<String>();
			double lat1 = Double.parseDouble(lat) + 0.000000001;
			double long1 = Double.parseDouble(lng) + 0.000000001;
			String selectqry = "select e.id,e.displayname,e.contactNumber1,e.smsflag,(select regNo from vehicles where device_num='"
					+ deviceNo
					+ "')as regNo from employee e, employee_tripping et where e.id=et.empid and et.ePick in (select id from landmark where latitude between '"
					+ lat
					+ "' and '"
					+ lat1
					+ "' and longitude between '"
					+ lng + "' and '" + long1 + "')";
			rs = st.executeQuery(selectqry);
			while (rs.next()) {
				if (rs.getString("smsflag") == null
						|| (rs.getString("smsflag") != SettingsConstant.tripMap
								.get(deviceNo))) {
					empid.add(rs.getString("id"));
					contact.add(rs.getString("contactNumber1"));
					empName.add(rs.getString("displayname"));
					vehicleNo.add(rs.getString("regNo"));
				}
			}
			String sub = "";
			if (!empid.isEmpty()) {
				for (int i = 0; i < empid.size(); i++) {
					sub += empid.get(i);
					if (!((i + 1) == empid.size())) {
						sub += ",";
					}
				}
				String insertqry = "UPDATE employee SET smsFlag=(select td.id from trip_details td, vendor_trip_sheet_parent vtsp,vehicles v where td.id=vtsp.tripid and vtsp.vehicleNo=v.id and v.device_num='"
						+ deviceNo
						+ "' and vtsp.status='started' order by td.id desc limit 1) WHERE id=("
						+ sub + ")";
				st1 = con.createStatement();
				int updt = st1.executeUpdate(insertqry);
				String[] MobileNo = new String[updt];
				String[] message = new String[updt];
				Date d = new Date();
				Calendar cal = Calendar.getInstance();
				cal.setTime(d);
				cal.add(Calendar.MINUTE, 10);
				String[] datee = cal.getTime().toString().split(" ");
				for (int i = 0; i < updt; i++) {
					MobileNo[i] = contact.get(i);
					message[i] = "Dear Member, Your cab (N0:-"
							+ vehicleNo.get(i)
							+ ") will reach your pick-up point approximatly @ "
							+ (datee[3].split(":"))[0] + ":"
							+ (datee[3].split(":"))[0];
				}
				SendSMS sendSMS = SendSMSFactory.getSMSInstance();
				sendSMS.send(MobileNo, message);
			}
			}
		} catch (SQLException e) {
			System.out.println("Error SocketDeviceDao@vehicleAlertSms" + e);
		} finally {
			DbConnect.closeResultSet(rs, rs1);
			DbConnect.closeStatement(st, st1);
			DbConnect.closeConnection(con);
		}
		return 0;
	}

	public void insertdata(String data) {
		DbConnect ob = DbConnect.getInstance();
		Connection con = null;
		Statement st = null;
		try {
			con = ob.connectDB();
			st = con.createStatement();
			String query = "INSERT INTO DATA_RECIEVED(REC_DATE,REC_TIME,DATA) VALUES(CURDATE(),CURTIME(),'"
					+ data + "') ";
			st.executeUpdate(query);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);
		}
	}

	public ArrayList<String> getPicupTimes(String LogType) {
		DbConnect ob = DbConnect.getInstance();
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		ArrayList<String> list = new ArrayList<String>();
		try {
			con = ob.connectDB();
			st = con.createStatement();
			String query = "select startTime from logtime where logtype='"
					+ LogType + "' and status='active'";
			rs = st.executeQuery(query);
			while (rs.next()) {
				list.add(rs.getString("startTime"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);
			DbConnect.closeResultSet(rs);
		}
		return list;
	}

	public void StartTrip(String lattitude, String longitude, String deviceNo) {

		DbConnect ob = DbConnect.getInstance();
		Connection con = null;
		Statement st = null, st1 = null,st2=null,st3=null;
		ResultSet rs = null, rs1 = null,rs2=null,rs3=null;
		String site="1";
		try {
			con = ob.connectDB();
			boolean flag = true;
			ArrayList<String> picLog = null;
			ArrayList<String> out = null;
			if (SettingsConstant.inpicLogs != null
					&& SettingsConstant.inpicLogs.size() > 0) {
				picLog = SettingsConstant.inpicLogs;
			} else {
				picLog = new SocketDeviceDao().getPicupTimes("IN");
				SettingsConstant.inpicLogs.addAll(picLog);
			}
			if (SettingsConstant.outLogs != null
					&& SettingsConstant.outLogs.size() > 0) {
				out = SettingsConstant.outLogs;
			} else {
				out = new SocketDeviceDao().getPicupTimes("OUT");
				SettingsConstant.outLogs.addAll(out);
			}

			System.out.println(picLog.size()+"--"+out.size());
			String string1 = new SimpleDateFormat("HH:mm").format(new Date());
			Date time1 = new SimpleDateFormat("HH:mm").parse(string1);
			Calendar calendar1 = Calendar.getInstance();
			calendar1.setTime(time1);
			calendar1.add(Calendar.MINUTE, -10);
			
			Date time2 = new SimpleDateFormat("HH:mm").parse(string1);
			Calendar calendar2 = Calendar.getInstance();
			calendar2.setTime(time2);
			calendar2.add(Calendar.MINUTE, 15);

			for (String pic : picLog) {
				Date d = new SimpleDateFormat("HH:mm").parse(pic);
				Calendar calendar3 = Calendar.getInstance();
				calendar3.setTime(d);
				Date x = calendar3.getTime();
				if (x.after(calendar1.getTime())
						&& x.before(calendar2.getTime())) {
					flag = false;
					st = con.createStatement();
					String qry = "select logtime from logtime where startTime='"
							+ pic + "' and logtype='IN'";
					rs = st.executeQuery(qry);
					String logtime = "";
					if (rs.next()) {
						logtime = rs.getString("logtime");
					}
					//String latlngqry="select l.latitude,l.longitude from landmark l,vehicle_shift_map vs,vehicles v,routechild r,logtime lt where l.id=r.landmarkId and r.routeid=vs.routeId and vs.vehicleId=v.id and vs.shiftId=lt.id and v.device_num='"+deviceNo+"' and lt.logtime='"+logtime+"' and lt.logtype='IN' and lt.status='active' order by r.position desc limit 1";
					st2=con.createStatement();
					//rs2=st2.executeQuery(latlngqry);
					//if(rs2.next()){
					//	double dist=new DistanceListDao().getDistanceWithoutMap(Double.parseDouble(lattitude),Double.parseDouble(longitude),Double.parseDouble(rs2.getString("latitude")),Double.parseDouble(rs2.getString("longitude")));
						//if(dist<0.5){
					String getTripId = "select t.id from trip_details t,vendor_trip_sheet_parent vt,vehicles v where t.id=vt.tripid  and t.trip_time='"
							+ logtime
							+ "' and t.vehicle=v.id and v.device_num= '"
							+ deviceNo + "' and vt.status='started'";
					rs2 = st2.executeQuery(getTripId);
					if (rs2.next()) {
					} else {
						new ShuttleServcie().createShuttle(deviceNo, "IN", logtime);
					}
							
							//}
					//}
					
				}
			}
			if (flag){
				for (String pic : out) {
					Date d = new SimpleDateFormat("HH:mm").parse(pic);
					Calendar calendar3 = Calendar.getInstance();
					calendar3.setTime(d);
					Date x = calendar3.getTime();
					if (x.after(calendar1.getTime())
							&& x.before(calendar2.getTime())) {
						st1 = con.createStatement();
						String logtime=pic;
						//String latlngqry1="select l.latitude,l.longitude from site s,landmark l where l.id=s.landmark and s.id="+site;
						st3=con.createStatement();
						//rs3=st3.executeQuery(latlngqry1);
						//if(rs3.next()){
							//double dist=new DistanceListDao().getDistanceWithoutMap(Double.parseDouble(lattitude),Double.parseDouble(longitude),Double.parseDouble(rs3.getString("latitude")),Double.parseDouble(rs3.getString("longitude")));
							//if(dist<0.5){
						String getTripId1 = "select t.id from trip_details t,vendor_trip_sheet_parent vt,vehicles v where t.id=vt.tripid  and t.trip_time='"
								+ logtime
								+ "' and t.vehicle=v.id and v.device_num= '"
								+ deviceNo + "' and vt.status='started'";
						rs3 = st3.executeQuery(getTripId1);
						if (rs3.next()) {
						} else {
							new ShuttleServcie().createShuttle(deviceNo, "OUT", logtime);
						}
								
						//	}
						//}
					}
				}
			}
		} catch (ParseException e) {
			System.out.println("Error in SocketDeviceDao@StartTrip" + e);
			e.printStackTrace();
		} catch (SQLException e) {
			System.out.println("Error in SocketDeviceDao@StartTrip" + e);
		}
		 finally {
				DbConnect.closeStatement(st,st1,st2,st3);
				DbConnect.closeConnection(con);
				DbConnect.closeResultSet(rs,rs1,rs2,rs3);
			}

	}
}
