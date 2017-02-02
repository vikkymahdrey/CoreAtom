
package com.agiledge.atom.mobile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.json.JSONObject;

import com.agiledge.atom.dao.LogTimeDao;
import com.agiledge.atom.dto.AdhocDto;
import com.agiledge.atom.dto.EmployeeDto;
import com.agiledge.atom.dto.EmployeeICEDTO;
import com.agiledge.atom.dto.LogTimeDto;
import com.agiledge.atom.dto.ScheduleAlterDto;
import com.agiledge.atom.dto.SchedulingDto;
import com.agiledge.atom.dto.TripDetailsChildDto;
import com.agiledge.atom.dto.TripDetailsDto;
import com.agiledge.atom.mobile.dao.TripServiceDao;
import com.agiledge.atom.mobile.service.AndroidAppForEmployeeService;
import com.agiledge.atom.mobile.service.LoginForEmployeeApp;
import com.agiledge.atom.mobile.service.TripService;

/**
 * Servlet implementation class AndroidAppForEmployee
 */
public class AndroidAppForEmployee extends HttpServlet {

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AndroidAppForEmployee() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		process(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		process(request, response);
	}

	private void process(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		int no = 0;
		try {
			
			// response.getWriter().write("Testing response from server ");
			BufferedReader reader = request.getReader();
			PrintWriter out = response.getWriter();
			String line;
			String lines = "";
			String result = "fail";
			String otp = null;

			while ((line = reader.readLine()) != null) {
				lines = lines + line + "\n";
			}
			reader.close();
			JSONObject obj = new JSONObject(lines);
			JSONObject rObj = null;
			rObj = obj;
			if (obj.getString("ACTION").equals("INTERNET_CHECK")) {
				no = 1;
				rObj = obj;
				rObj.put("result", "true");

			} else if (obj.getString("ACTION").equals("IMEI_CHECK")) {
				no = 2;
				System.out.println("here");
				result = new LoginForEmployeeApp().checkImei(obj
						.getString("IMEI_NUMBER"));

				rObj.put("result", result);
				if (result.equalsIgnoreCase("true")) {
					System.out.println("here in true");
					TripDetailsDto dto = new AndroidAppForEmployeeService()
							.getTripDetails(obj.getString("IMEI_NUMBER"), "No");
					EmployeeDto edto = new LoginForEmployeeApp()
							.getEmployeeDetails(obj.getString("IMEI_NUMBER"));
					
					if (dto.getId() != null
							&& !dto.getId().equalsIgnoreCase("")) {
						System.out.println("here in id");
						rObj.put("result", result);
						rObj.put("user_type", edto.getUserType()); // for admin app
						rObj.put("EMP_NAME", edto.getDisplayName());
						rObj.put("EMP_PERSONNELNO", edto.getPersonnelNo());
						rObj.put("EMP_GENDER", edto.getGender());
						rObj.put("EMP_EMAIL", edto.getEmailAddress());
						rObj.put("EMP_SITE", edto.getSite());
						rObj.put("EMP_ID", edto.getEmployeeID());
						rObj.put("TRIP_ID", dto.getId());
						rObj.put("TRIP_CODE", dto.getTrip_code());
						rObj.put("TRIP_TIME", dto.getTrip_time());
						rObj.put("TRIP_DATE", dto.getTrip_date());
						rObj.put("TRIP_LOG", dto.getTrip_log());
						rObj.put("SECURITY", dto.getIsSecurity());
						rObj.put("REG_NO", dto.getVehicleNo());
						rObj.put("DRIVER_NAME", dto.getDriverName());
						rObj.put("DRIVER_CONTACT", dto.getDriverContact());
						rObj.put("ESCORT_NAME", dto.getEscortName());
						rObj.put("ESCORT_CONTACT", dto.getEscortContact());
						rObj.put("EMPS_COUNT", dto.getEmpCount());

						if (dto.getEmpCount() > 0) {
							int i = 1;
							for (TripDetailsChildDto tripDetailsChildDto : dto
									.getTripDetailsChildDtoList()) {
								rObj.put("EMP_ID" + i,
										tripDetailsChildDto.getEmployeeId());
								rObj.put("PERSONNEL_NO" + i,
										tripDetailsChildDto.getPersonnelNo());
								rObj.put("EMP_NAME" + i,
										tripDetailsChildDto.getEmployeeName());
								rObj.put("GENDER" + i,
										tripDetailsChildDto.getGender());
								rObj.put("EMP_CONTACT" + i,
										tripDetailsChildDto.getContactNumber());
								i = i + 1;
							}
						}
					} else {

						System.out.println("here in else");
						rObj.put("EMP_ID", "");
						rObj.put("TRIP_ID", "");
						rObj.put("TRIP_CODE", "");
						rObj.put("TRIP_TIME", "");
						rObj.put("TRIP_DATE", "");
						rObj.put("TRIP_LOG", "");
						rObj.put("SECURITY", "");
						rObj.put("REG_NO", "");
						rObj.put("DRIVER_NAME", "");
						rObj.put("DRIVER_CONTACT", "");
						rObj.put("ESCORT_NAME", "");
						rObj.put("ESCORT_CONTACT", "");
						rObj.put("EMPS_COUNT", "0");
						rObj.put("EMP_NAME", edto.getDisplayName());
						rObj.put("EMP_PERSONNELNO", edto.getPersonnelNo());
						rObj.put("EMP_GENDER", edto.getGender());
						rObj.put("EMP_EMAIL", edto.getEmailAddress());
						rObj.put("EMP_SITE", edto.getSite());
						rObj.put("EMP_ID", edto.getEmployeeID());
						rObj.put("user_type", edto.getUserType()); // for admin
																	// app
					}

								}

			}

			else if (obj.getString("ACTION").equals("INITIAL_LOGIN")) {
				no = 3;
				result = new LoginForEmployeeApp().mobileNocheck(obj
						.getString("MOBILE_NUMBER"));
				if (result.equalsIgnoreCase("true")) {
					otp = new LoginForEmployeeApp().passwordgenerator();
					otp = new LoginForEmployeeApp().insertOTP(
							obj.getString("MOBILE_NUMBER"),
							obj.getString("IMEI_NUMBER"), otp);
					if (otp != null && !otp.equalsIgnoreCase("")) {
						EmployeeDto dto = new LoginForEmployeeApp()
								.getEmployeeDetails(obj
										.getString("IMEI_NUMBER"));
						rObj.put("result", result);
						rObj.put("EMP_NAME", dto.getDisplayName());
						rObj.put("EMP_PERSONNELNO", dto.getPersonnelNo());
						rObj.put("EMP_GENDER", dto.getGender());
						rObj.put("EMP_EMAIL", dto.getEmailAddress());
						rObj.put("EMP_SITE", dto.getSite());
						rObj.put("EMP_ID", dto.getEmployeeID());
						rObj.put("otp", otp);
						rObj.put("user_type", dto.getUserType()); // for admin
																	// app
					} else {
						rObj.put("result", "false");
					}
				} else {
					rObj.put("result", result);
				}

			} else if (obj.getString("ACTION").equalsIgnoreCase("TRIP_DETAILS")) {
				no = 4;
				rObj.put("result", "true");
				TripDetailsDto dto = new AndroidAppForEmployeeService()
						.getTripDetails(obj.getString("IMEI_NUMBER"), "Yes");
				EmployeeDto edto = new LoginForEmployeeApp()
						.getEmployeeDetails(obj.getString("IMEI_NUMBER"));
			

				if (dto.getId() != null && !dto.getId().equalsIgnoreCase("")) {
					rObj.put("TRIP_ID", dto.getId());
					rObj.put("EMP_ID", dto.getDoneBy());
					rObj.put("TRIP_CODE", dto.getTrip_code());
					rObj.put("TRIP_TIME", dto.getTrip_time());
					rObj.put("TRIP_DATE", dto.getTrip_date());
					rObj.put("TRIP_LOG", dto.getTrip_log());
					rObj.put("SECURITY", dto.getIsSecurity());
					rObj.put("REG_NO", dto.getVehicleNo());
					rObj.put("DRIVER_NAME", dto.getDriverName());
					rObj.put("DRIVER_CONTACT", dto.getDriverContact());
					rObj.put("ESCORT_NAME", dto.getEscortName());
					rObj.put("ESCORT_CONTACT", dto.getEscortContact());
					rObj.put("EMPS_COUNT", dto.getEmpCount());
					rObj.put("user_type", edto.getUserType()); // for admin app
					rObj.put("EMP_NAME", edto.getDisplayName());
					rObj.put("EMP_GENDER", edto.getGender());
					if (dto.getEmpCount() > 0) {
						int i = 1;
						for (TripDetailsChildDto tripDetailsChildDto : dto
								.getTripDetailsChildDtoList()) {
							rObj.put("EMP_ID" + i,
									tripDetailsChildDto.getEmployeeId());
							rObj.put("PERSONNEL_NO" + i,
									tripDetailsChildDto.getPersonnelNo());
							rObj.put("EMP_NAME" + i,
									tripDetailsChildDto.getEmployeeName());
							rObj.put("GENDER" + i,
									tripDetailsChildDto.getGender());
							rObj.put("EMP_CONTACT" + i,
									tripDetailsChildDto.getContactNumber());
							i = i + 1;
						}
					}
				} else {
					System.out.println("here in else");
					rObj.put("EMP_ID", "");
					rObj.put("TRIP_ID", "");
					rObj.put("TRIP_CODE", "");
					rObj.put("TRIP_TIME", "");
					rObj.put("TRIP_DATE", "");
					rObj.put("TRIP_LOG", "");
					rObj.put("SECURITY", "");
					rObj.put("REG_NO", "");
					rObj.put("DRIVER_NAME", "");
					rObj.put("DRIVER_CONTACT", "");
					rObj.put("ESCORT_NAME", "");
					rObj.put("ESCORT_CONTACT", "");
					rObj.put("EMPS_COUNT", "0");
					rObj.put("EMP_NAME", "");
					rObj.put("EMP_PERSONNELNO", "");
					rObj.put("EMP_GENDER", "");
					rObj.put("EMP_EMAIL", "");
					rObj.put("EMP_SITE", "");
					rObj.put("EMP_ID", "");
					rObj.put("EMP_NAME", "");
					rObj.put("EMP_GENDER", "");
					rObj.put("user_type", edto.getUserType()); // for admin app

				}


			} else if (obj.getString("ACTION").equalsIgnoreCase(
					"VEHICLE_LOCATION")) {
				no = 5;
				System.out.println("here in veh loc");
				rObj = new AndroidAppForEmployeeService()
						.getVehiclePosition(obj.getString("TRIP_ID"));
				rObj.put("result", "true");

			} else if (obj.getString("ACTION").equalsIgnoreCase(
					"VEHICLE_LOCATION1")) {
				no = 6;
				System.out.println("here in veh loc");
				rObj = new AndroidAppForEmployeeService().getVehiclePosition1();
				rObj.put("result", "true");

			} 
			else if (obj.getString("ACTION").equalsIgnoreCase("EMPS_BOARDED")) {
				no = 7;
				rObj = new AndroidAppForEmployeeService().getEmpsBoarded(obj
						.getString("TRIP_ID"));
				rObj.put("result", "true");

			} else if (obj.getString("ACTION").equalsIgnoreCase("ICE_REGISTER")) {
				no = 8;
				EmployeeICEDTO dto = new EmployeeICEDTO();
				dto.setEmpid(obj.getString("EMP_ID"));
				dto.setContact_name1(obj.getString("CONTACT_NAME1"));
				dto.setContact_number1(obj.getString("CONTACT_NUMBER1"));
				dto.setContact_email1(obj.getString("CONTACT_EMAIL1"));
				dto.setContact_name2(obj.getString("CONTACT_NAME2"));
				dto.setContact_number2(obj.getString("CONTACT_NUMBER2"));
				dto.setContact_email2(obj.getString("CONTACT_EMAIL2"));
				int returnint = new AndroidAppForEmployeeService()
						.insertICEdetails(dto);
				if (returnint == 1) {
					rObj.put("result", "true");
				} else {
					rObj.put("result", "false");
				}

			} else if (obj.getString("ACTION").equalsIgnoreCase("ICE_DATA")) {
				no = 9;
				EmployeeICEDTO dto = new AndroidAppForEmployeeService()
						.getICEdetailsbyID(obj.getString("EMP_ID"));

				if (dto != null && dto.getContact_name1() != null
						&& !dto.getContact_name1().equalsIgnoreCase("")) {
					rObj.put("result", "true");
					rObj.put("CONTACT_NAME1", dto.getContact_name1());
					rObj.put("CONTACT_NUMBER1", dto.getContact_number1());
					rObj.put("CONTACT_EMAIL1", dto.getContact_email1());
					rObj.put("CONTACT_NAME2", dto.getContact_name2());
					rObj.put("CONTACT_NUMBER2", dto.getContact_number2());
					rObj.put("CONTACT_EMAIL2", dto.getContact_email2());
				} else {
					rObj.put("result", "false");
				}

			} else if (obj.getString("ACTION").equalsIgnoreCase(
					"PANIC_ACTIVATED")) {
				no = 10;
				String time = "";
				Calendar cal = Calendar.getInstance();
				cal.setTime(new Date());
				int hour = cal.get(Calendar.HOUR);
				int minute = cal.get(Calendar.MINUTE);
				time = "" + (hour < 10 ? "0" + hour : hour);
				time = time + ":" + (minute < 10 ? "0" + minute : minute);
				int returnint=new AndroidAppForEmployeeService().panicactivated(obj.getString("IMEI_NUMBER"),obj.getString("EMP_ID") ,obj.getString("TRIP_ID"),obj.getString("LAT"),obj.getString("LONG"),time);
				if(returnint==1)
				{ 
					
					rObj.put("result", "true");
					rObj.put("PANIC_STATUS", "ACTIVATED");

				} else {
					rObj.put("result", "false");
					rObj.put("PANIC_STATUS", "NOT ACTIVATED");

				}
			} else if (obj.getString("ACTION").equalsIgnoreCase(
					"PANIC_DEACTIVATED")) {
				no = 11;
				int returnint = new AndroidAppForEmployeeService()
						.panicdeactivated(obj.getString("IMEI_NUMBER"),
								obj.getString("EMP_ID"),
								obj.getString("TRIP_ID"));
				if (returnint == 1) {
					rObj.put("result", "true");
					rObj.put("PANIC_STATUS", "DEACTIVATED");

				} else {
					rObj.put("result", "false");
					rObj.put("PANIC_STATUS", "NOT DEACTIVATED");

				}
			}

			else if (obj.getString("ACTION").equalsIgnoreCase("admin_track")) {
				no = 12; // for admin app

				rObj = new AndroidAppForEmployeeService().getLiveTrips();
				rObj.put("result", "true");
			}  else if (obj.getString("ACTION").equalsIgnoreCase(
					"SCHEDULE_ALTER")) {
				no = 14;
				String status = new AndroidAppForEmployeeService()
						.requestScheduleAlter(obj.getString("IMEI"),
								obj.getString("DATE"), obj.getString("LOG_IN"),
								obj.getString("LOG_OUT"), "Alter");
				rObj.put("STATUS", status);
				if(status.equalsIgnoreCase("Altered Successfully") || status.equalsIgnoreCase("Cancelled Successfully")){
					rObj.put("RESULT", "true");
				}else{
					rObj.put("RESULT", "false");
				}
			} else if (obj.getString("ACTION").equalsIgnoreCase(
					"SCHEDULE_CANCEL")) {
				no = 15;
				System.out.println("IN CANCEL");
				String status = new AndroidAppForEmployeeService()
						.requestScheduleAlter(obj.getString("IMEI"),
								obj.getString("DATE"), obj.getString("LOG_IN"),
								obj.getString("LOG_OUT"), "Cancel");
				rObj.put("STATUS", status);
			} else if (obj.getString("ACTION").equalsIgnoreCase("ADHOC_BOOK")) {
				no = 16;
				
				AdhocDto dto = new AdhocDto();
				dto.setAdhocType(obj.getString("ADHOC_TYPE"));
				dto.setTravelDate(obj.getString("DATE"));
				dto.setReason(obj.getString("REASON"));
				dto.setPickupDrop(obj.getString("LOGTYPE"));
				dto.setShiftTime(obj.getString("LOGTIME"));
				rObj = new AndroidAppForEmployeeService()
						.adhocRequest(obj.getString("IMEI_NUMBER"), dto);
				
			} else if (obj.getString("ACTION").equalsIgnoreCase("EMP_FEEDBACK")) {
				no = 17;
				String status = new AndroidAppForEmployeeService().setfeedback(
						obj.getString("IMEI"), obj.getString("DATE"),
						obj.getString("LOGTYPE"), obj.getString("VC"),
						obj.getString("DRIVER"), obj.getString("TRAVEL"),
						obj.getString("OVERALL"), obj.getString("OTHER"));
				if(status.equalsIgnoreCase("Thank you for your feedback")){
					rObj.put("RESULT", "true");
				}else{
					rObj.put("RESULT", "false");
				}
				rObj.put("STATUS", status);
			} else if (obj.getString("ACTION").equalsIgnoreCase("TRIP_HISTORY")) {
				no = 18;
				JSONObject tripDetails = new AndroidAppForEmployeeService()
						.getTripHistory(obj.getString("IMEI"));
				rObj=tripDetails;
			}else if(obj.getString("ACTION").equalsIgnoreCase("MANAGEBOOKING")){
				no=19;
				System.out.println("hi");
				List<Object> list = new AndroidAppForEmployeeService().scheduleList(obj.getString("IMEI_NUMBER"));
				JSONArray adhocType = new AndroidAppForEmployeeService().getAdhocTypes();
				JSONArray inLog = new AndroidAppForEmployeeService().getActivelog("IN");
				JSONArray outLog = new AndroidAppForEmployeeService().getActivelog("OUT");
				if (list.size() > 0) {
					ArrayList<ScheduleAlterDto> schedule = (ArrayList<ScheduleAlterDto>) list.get(0);

					for (ScheduleAlterDto scheduleAlterDto : schedule) {
						rObj.put("FROM_DATE",
								scheduleAlterDto.getFromDate());
						rObj.put("TO_DATE", scheduleAlterDto.getToDate());
						rObj.put("LOG_IN", scheduleAlterDto.getLoginTime());
						rObj.put("LOG_OUT",
								scheduleAlterDto.getLogoutTime());
					}
					rObj.put("ALTERDATE", (org.json.JSONArray) list.get(1));
					rObj.put("AL_LOG_IN", (org.json.JSONArray) list.get(2));
					rObj.put("AL_LOG_OUT", (org.json.JSONArray) list.get(3));
				} else {
					// if no schedule
					JSONArray dummy = new JSONArray();
					rObj.put("FROM_DATE", "2012-12-21");
					rObj.put("TO_DATE", "2012-12-21");
					rObj.put("LOG_IN", "00:00");
					rObj.put("LOG_OUT", "00:00");
					rObj.put("ALTERDATE", dummy);
					rObj.put("AL_LOG_IN", dummy);
					rObj.put("AL_LOG_OUT", dummy);
				}

				rObj.put("IN_LOGS", inLog);
				rObj.put("OUT_LOGS", outLog);
				rObj.put("ADHOCTYPE", adhocType);
			

			}
			
			else if (obj.getString("ACTION").equalsIgnoreCase("manager_track")) {
				no = 19; // for manager view

				rObj = new AndroidAppForEmployeeService().getManagerTrips(obj.getString("EMP_ID"));
				rObj.put("result", "true");
			}
			else if (obj.getString("ACTION").equalsIgnoreCase("EMP_LOGS")) {
				no = 20; 
			
				rObj = new AndroidAppForEmployeeService().getemplogs(obj.getString("EMP_ID"));
				//rObj.put("result", "true");
			}
			
			else if(obj.getString("ACTION").equalsIgnoreCase("EMP_SEARCH")){
				rObj = new AndroidAppForEmployeeService().searchEmp(obj.getString("PERSONAL_NUMBER"),obj.getString("FNAME"),obj.getString("LNAME"));
			
			}else if(obj.getString("ACTION").equalsIgnoreCase("ADMIN_BOOKING")){
				
				rObj = new AndroidAppForEmployeeService().scheduleEmp(obj.getString("EMP_ID"),obj.getString("IMEI_NUMBER"),obj.getString("FROM_DATE"),obj.getString("TO_DATE"),obj.getString("LOG_IN"),obj.getString("LOG_OUT"),"1");
			
			}else if(obj.getString("ACTION").equalsIgnoreCase("CHECK_EMP_SCHEDULE")){
				rObj = new AndroidAppForEmployeeService().checkEmpSchedule(obj.getString("EMP_ID"),obj.getString("IMEI_NUMBER"));
			
			}else if(obj.getString("ACTION").equalsIgnoreCase("ADMIN_BOOK_ALTER")){
				rObj = new AndroidAppForEmployeeService().adminScheduleAlter(obj.getString("EMP_ID"),obj.getString("IMEI_NUMBER"),obj.getString("DATE"),obj.getString("LOG_IN"),obj.getString("LOG_OUT"));

			
			}else if(obj.getString("ACTION").equalsIgnoreCase("BOOKING")){
				rObj = new AndroidAppForEmployeeService().scheduleEmp(obj.getString("BOOKING_FOR"),obj.getString("IMEI_NUMBER"),obj.getString("FROM_DATE"),obj.getString("TO_DATE"),obj.getString("LOG_IN"),obj.getString("LOG_OUT"),obj.getString("SITE_ID"));
			
			}else if(obj.getString("ACTION").equalsIgnoreCase("SEARCH_EMPLOYEE")){
				rObj = new AndroidAppForEmployeeService().getEmployeeResult(obj.getString("SEARCHKEY"));
			}
		else if(obj.getString("ACTION").equalsIgnoreCase("GET_SCHEDULES")){
				rObj = new AndroidAppForEmployeeService().getSchedules(obj.getString("IMEI"));
			}
			else if(obj.getString("ACTION").equalsIgnoreCase("MODIFY_CHECK")){
				rObj=new AndroidAppForEmployeeService().getTimesForDate(obj.getString("DATE"),obj.getString("IMEI"));
			}
			else if(obj.getString("ACTION").equalsIgnoreCase("ADHOCK_CHECK")){
				JSONArray adhocType = new AndroidAppForEmployeeService().getAdhocTypes();
				ArrayList<LogTimeDto> dtos = new LogTimeDao().getAdhocShift("OUT");
				JSONArray outlogs=new JSONArray();
				for(LogTimeDto dto : dtos){
					outlogs.add(dto.getLogTime());
				}
				rObj.put("RESULT", "TRUE");
				rObj.put("ADHOCTYPE", adhocType);
				rObj.put("LOG_OUT", outlogs);
			}
	
			else if (obj.getString("ACTION").equalsIgnoreCase("HELPDESK")) {
				rObj = new AndroidAppForEmployeeService().getHelpDeskDetails();
			} 
			else if (obj.getString("ACTION").equalsIgnoreCase("PANICTRIPDETAILS")) {

				rObj = new AndroidAppForEmployeeService().getPanicTripDetails( obj.getString("TRIPID"));


			}
			else if (obj.getString("ACTION").equalsIgnoreCase("PANICSTOP")) {

				int returnint = new AndroidAppForEmployeeService().stopPanic(
						obj.getString("EMPID"), obj.getString("TRIPID")
								, obj.getString("ALARMCAUSE"),
						obj.getString("PRIMARYACTION"));

				if (returnint > 0) {
					rObj.put("RESULT", "true");
					rObj.put("STATUS", "PANIC STOPPED SUCCESSFULLY");
				} else {
					rObj.put("RESULT", "false");
					rObj.put("STATUS", "FAILED TO STOP PANIC");
				}

			}

			out.print(rObj.toString());
			out.flush();
			out.close();
		} catch (Exception e) {
			System.out.println(no + "Error in AndroidAppforemp" + e);
		}
	}

	
}
