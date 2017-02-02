package com.agiledge.atom.mobile.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sf.json.JSON;
import net.sf.json.JSONArray;

import org.json.JSONException;
import org.json.JSONObject;

import com.agiledge.atom.dao.LogTimeDao;
import com.agiledge.atom.dao.PanicDao;
import com.agiledge.atom.dto.AdhocDto;
import com.agiledge.atom.dto.EmployeeDto;
import com.agiledge.atom.dto.EmployeeICEDTO;
import com.agiledge.atom.dto.LogTimeDto;
import com.agiledge.atom.dto.ScheduleAlterDto;
import com.agiledge.atom.dto.TripDetailsDto;
import com.agiledge.atom.mobile.dao.AndroidAppForEmployeeDao;
import com.agiledge.atom.service.AdhocService;
import com.agiledge.atom.service.EmployeeService;
import com.agiledge.atom.servlets.AdhocTripUpdate;

public class AndroidAppForEmployeeService {

	public TripDetailsDto getTripDetails(String imei,String auth)
	{
		return new AndroidAppForEmployeeDao().getTripDetails(imei,auth);
	}
	
	public JSONObject getVehiclePosition(String tripid)
	{
		return new AndroidAppForEmployeeDao().getVehiclePosition(tripid);
	}
	public JSONObject getVehiclePosition1()
	{
		return new AndroidAppForEmployeeDao().getVehiclePosition1();
	}
	
	public JSONObject getEmpsBoarded(String tripid)
	{
		return new AndroidAppForEmployeeDao().getEmpsBoarded(tripid);
	}
	public int insertICEdetails(EmployeeICEDTO dto)
	{
		return new AndroidAppForEmployeeDao().insertICEdetails(dto);
	}
	public EmployeeICEDTO getICEdetailsbyID(String empid)
	{
		return new AndroidAppForEmployeeDao().getICEdetailsbyID(empid);
	}
	public int panicactivated(String imei,String empid,String tripid,String Lat,String Long,String time)
	{
		
		new AndroidAppForEmployeeDao().sendsmstoICE(empid,time);
		return new AndroidAppForEmployeeDao().panicactivated(imei, empid,tripid, Lat, Long,time);
		
	}
	public int panicdeactivated(String imei,String empid,String tripid)
	{
		return new AndroidAppForEmployeeDao().panicdeactivated(imei, empid,tripid);
	}
	public JSONObject getLiveTrips()
	{
		return new AndroidAppForEmployeeDao().getLiveTrips();
	}
	
	public List<Object> scheduleList(String imei) {
		return new AndroidAppForEmployeeDao().scheduleList(imei);
	}

	public String requestScheduleAlter(String imei, String date,
			String logIn, String logOut,String type) {
		return new AndroidAppForEmployeeDao().requestScheduleAlter(imei, date,logIn,logOut,type);
	}

	public JSONObject adhocRequest(String imei, AdhocDto dto) {
		
		return new AndroidAppForEmployeeDao().adhocRequest(imei,dto);
	}

	public JSONArray getActivelog(String log) {

		ArrayList<LogTimeDto> logdtos=new LogTimeDao().getAllLogtime(log);
		JSONArray logs= new JSONArray();
		for(LogTimeDto dto : logdtos){
			logs.add(dto.getLogTime());
		}
		return logs;
	}

	public String setfeedback(String imei, String date,String logType, String vehiclCond,
			String driverBehav, String travelTime, String overAll,String other) {
		String[] remark = {vehiclCond,driverBehav,travelTime,overAll};
		for(int i =0; i<remark.length;i++){
			switch(remark[i]){
			case "1"	:remark[i]="BAD";
						break;
			case "2"	:remark[i]="AVERAGE";
						break;
			case "3"	: remark[i]="GOOD";
						break;
			case "4"	: remark[i]="VERY GOOD";
						break;
			}
		}
		return new AndroidAppForEmployeeDao().setfeedback(imei,date,logType,remark[0],remark[1],remark[2],remark[3],other);
	}

	public JSONArray getAdhocTypes() {
		JSONArray adhocType= new JSONArray();
		ArrayList<AdhocDto> adhoc=new AdhocService().getAdhocTypes();
		for(AdhocDto dto : adhoc){
			adhocType.add(dto.getAdhocType());
		}
		return adhocType;
	}
	public JSONObject getTripHistory(String imei){
		return new AndroidAppForEmployeeDao().getTripHistory(imei);
	}
	public JSONObject getManagerTrips(String empid)
	{
		return new AndroidAppForEmployeeDao().getManagerTrips(empid);
	}
	
	public JSONObject searchEmp(String personnelNo,String fname,String lname){
		
		EmployeeDto dto=new EmployeeDto();
		dto.setEmployeeID(personnelNo);
		dto.setEmployeeFirstName(fname);
		dto.setEmployeeLastName(lname);
		List<EmployeeDto> dtoList = new EmployeeService().searchEmployee(dto);
		JSONObject jObj=new JSONObject();
		JSONArray ename= new JSONArray();
		JSONArray pNo =new JSONArray();
		JSONArray eId= new JSONArray();
		
		for(EmployeeDto empdto : dtoList){
			eId.add(empdto.getEmployeeID());
			ename.add(empdto.getDisplayName());
			pNo.add(empdto.getPersonnelNo());
		}
		try {
			jObj.put("RESULT", "TRUE");
			jObj.put("EMP_ID", eId);
			jObj.put("EMP_NAME",ename);
			jObj.put("EMP_NO",pNo);
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return jObj;
	}
	public JSONObject checkEmpSchedule(String empid,String imei){
		JSONObject jObj=new JSONObject();
		String status="Failed";
		String result="false";
		boolean flag=checkAccess(imei);
		if(flag){
			jObj = new AndroidAppForEmployeeService().scheduleListofEmp(empid);
			
		}else{
			try {
				jObj.put("RESULT", "false");
				jObj.put("STATUS", "You Dont have acceess");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return jObj;
	}
	public boolean checkAccess(String imei){
		return new AndroidAppForEmployeeDao().checkAccess(imei);
	}
	
	public JSONObject scheduleListofEmp(String empid) {
		return new AndroidAppForEmployeeDao().scheduleListofEmp(empid);
	}


	public JSONObject getemplogs(String empid)
	{
		return new AndroidAppForEmployeeDao().getemplogs(empid);
	}

	public JSONObject scheduleEmp(String empid, String bookingByImei,
			String fromDate, String toDate, String logIn, String logOut,String site) {
		// TODO Auto-generated method stub
		
		JSONObject obj = new JSONObject();
		boolean flag=checkAccess(bookingByImei);
		if(flag){
			obj=new AndroidAppForEmployeeDao().scheduleEmp(empid,bookingByImei,fromDate,toDate,logIn,logOut,site);
			
		}else{
			try {
			obj.put("RESULT", "false");
			obj.put("STATUS", "You Dont have access");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
			
		
		
		return obj;
	}

	public JSONObject adminScheduleAlter(String bookingFor, String bookingByImei,
			String date, String logIn, String logOut) {
		// TODO Auto-generated method stub
		return new AndroidAppForEmployeeDao().adminScheduleAlter(bookingFor,bookingByImei,date,logIn,logOut);
	}
	
	public JSONObject  getEmployeeResult(String searchkey) {
		return new AndroidAppForEmployeeDao().getEmployeeResult(searchkey);
	}
	public JSONObject  getSchedules(String imei) {
		return new AndroidAppForEmployeeDao().getSchedules(imei);
	}
	
	public JSONObject getTimesForDate(String date,String imei){
		return new AndroidAppForEmployeeDao().getTimesForDate(date, imei);
	}
	public JSONObject getHelpDeskDetails()
	{
		return new AndroidAppForEmployeeDao().getHelpDeskDetails();
	}
	public Map panicactivatedIVR(String empid,String tripid,String Lat,String Long,String time)
	{
		
		//new AndroidAppForEmployeeDao().sendsmstoICE(empid,time);
		return new AndroidAppForEmployeeDao().panicactivatedIVR(empid,tripid, Lat, Long,time);
		
	}

	public int stopPanic(String empid, String tripid, String alarmcause,
			String primaryaction) {
		
		return new AndroidAppForEmployeeDao().stopPanic(empid, tripid, alarmcause, primaryaction);
		
		
		
	}

	public JSONObject getPanicTripDetails(String tripid) {
		return  new AndroidAppForEmployeeDao().getPanicTripDetails( tripid);
	}
}
