package com.agiledge.atom.commons;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.TreeMap;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.UriBuilder;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFDataFormat;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.agiledge.atom.dao.DistanceListDao;
import com.agiledge.atom.dao.SchedulingDao;
import com.agiledge.atom.dbConnect.DbConnect;
import com.agiledge.atom.dto.EmployeeDto;
import com.agiledge.atom.dto.ScheduleAlterDto;
import com.agiledge.atom.dto.SchedulingDto;
import com.agiledge.atom.service.SettingsService;
import com.agiledge.atom.settings.dto.DeviceConfigurationSettingsConstants;
import com.agiledge.atom.task.dao.GetEmps;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;

/*
 import org.apache.commons.lang.RandomStringUtils;
 import org.apache.http.HttpResponse;
 import org.apache.http.client.Cl
 ientProtocolException;
 import org.apache.http.client.HttpClient;
 import org.apache.http.client.methods.HttpGet;
 import org.apache.http.client.methods.HttpPut;
 import org.apache.http.impl.client.DefaultHttpClient;
 import org.json.JSONException;
 import org.json.JSONObject;
 */

public class Main {
	
	
	
	
	
	
	
	
public static void  getMapDistances() {
	System.out.println("test");
		DefaultHttpClient client1 = new DefaultHttpClient();					
	    try{ 
		String url="https://maps.googleapis.com/maps/api/directions/json?origin=12.8771367810329,77.6024436950683&destination=12.89155934,77.63820559&waypoints=12.89318039,77.60744333%7C12.8812576,77.6254034%7C12.8992147738759,77.6267150044441%7C0,0&client=gme-agiledsolutionspvt&signature=5lclDUsN6yE3dG-HJhJViUOnyic=";
		System.out.println(url);
		HttpGet request = new HttpGet(url);
		System.out.println(request.getURI());
	    HttpResponse response1 = 
	    		 client1.execute(request);	      
	     BufferedReader rd = new BufferedReader (new InputStreamReader(response1.getEntity().getContent()));
	     String line = "";
	     String nLine="";
	     while ((line = rd.readLine()) != null) {
	    	 nLine+=line;
	        }
	     JSONObject obj = new JSONObject(nLine);	 
	  int legs = ((JSONObject) obj.getJSONArray("routes").get(0)).getJSONArray("legs").length();
	  for(int i = 0 ; i < legs ; i ++) {
		  JSONObject leg = ((JSONObject) obj.getJSONArray("routes").get(0)).getJSONArray("legs").getJSONObject(i);
		  
		  System.out.println(leg.toString());
		
	  }
		}catch(Exception e) {
			System.out.println("Error in getMapDistance : "+ e);		
		}
				
	}

	
	
	
	
	
	
	
	
	
	
	public static void test() {

		SettingsService stService = new SettingsService();
		System.out.println(OtherFunctions.getBoolean( stService.getSiteSetting(3, DeviceConfigurationSettingsConstants.DOUBLE_AUTHENTICATION_FOR_EMPLOYEE_PICKUP)));
		//new LoginService().autoMapDeviceAndVehicle("dv2", "010010101001001");
		System.out.println("...........................");
	//	new TripSheetService(). getTripSheet("000000000000000", "2014-08-11",
	//			"00:00", "IN");
	//	new DeviceDao(). mapDeviceVehicleAuto("358870059126353" , "dv2");

		//System.out.println("..........................." + OtherFunctions.TimeFormat24Hr("2:5:00 am"));
		showSchedule();
	 
	}
	public static void showNonScheduledEmp() {
		String fromDate ="10/08/2014";
		String toDate = "20/08/2014";
		ArrayList<SchedulingDto> dtoList = new SchedulingDao().getEmployeesHavingNoSchedule (fromDate,toDate , "1");
		//ArrayList<SchedulingDto> dtoList = new SchedulingDao().getScheduleOfEmpForDownload(fromDate,toDate , "1");
		// dtoList =getAllInBetweenInAlterList(fromDate, toDate, dtoList);
		//ArrayList<SchedulingDto> dtoListNoSchedule = new SchedulingDao().getEmployeesHavingNoSchedule (fromDate,toDate , "1");
		 //dtoList.addAll(dtoListNoSchedule);
		System.out.println("Liszt Size  : "+ dtoList.size());
		 for(SchedulingDto dto : dtoList)
		{
			 
			System.out.print(   dto.getEmployeeId() +"\t" + dto.getEmployeeName() +"\t" );
			System.out.print( " "+ dto.getAlterList().size() + " is the size ");
			for(ScheduleAlterDto adto : dto.getAlterList()) {
				System.out.print(OtherFunctions.changeDateFromat( "  "+ adto.getDate()) +"\t" + adto.getLoginTime() +"\t" + adto.getLogoutTime()  +"\t");
				/*if(adto==null) {
					System.out.println("null");
				}
				System.out.println(adto.toString());*/
			}
			System.out.println();
		} 
		
	}
	public static ArrayList<SchedulingDto> getAllInBetweenInAlterList(String fromDate, String toDate, LinkedHashMap<String, ArrayList<SchedulingDto>> dtoMap) {
		
		Calendar cal = Calendar.getInstance();
//		Calendar calT = Calendar.getInstance();
		Calendar calT1 = Calendar.getInstance();
		cal.setTime(OtherFunctions.changeDateFromat_ddmmyyy_to_UtilDate(fromDate));
		Calendar calEnd = Calendar.getInstance();
		Calendar calShEnd = Calendar.getInstance();
		 //calEnd.setTime( OtherFunctions.changeDateFromat_ddmmyyy_to_UtilDate(OtherFunctions.changeDateFromatToddmmyyyy(dto.getSchedulingToDate())));
		 calEnd.setTime(OtherFunctions.changeDateFromat_ddmmyyy_to_UtilDate(toDate));
		 ArrayList<SchedulingDto> dtoList=null; 
		System.out.println("Cal at start : "+ OtherFunctions.changeDateFromat(  cal.getTime())  );
		//SchedulingDto dto = dtoList.get(0);
		ArrayList<SchedulingDto> tempDtoList = new ArrayList<SchedulingDto>();
		Set<String> keySet = dtoMap.keySet();
		System.out.println(keySet.size() + " key size ");
		for(String key : keySet) {
			dtoList = dtoMap.get(key);
		
			cal.setTime(OtherFunctions.changeDateFromat_ddmmyyy_to_UtilDate(fromDate));
		 
			System.out.println("Size : "+ dtoList.size());
			
			SchedulingDto preShDto = null;
			SchedulingDto dtoTemp = null;
			for(SchedulingDto dto : dtoList) 
				{
				
				
				 Calendar calSubStart = Calendar.getInstance();
					Calendar calSubEnd = Calendar.getInstance();
					 
					calSubStart.setTime(OtherFunctions.changeDateFromat_ddmmyyy_to_UtilDate(OtherFunctions.changeDateFromatToddmmyyyy( dto.getSubscriptionDate() )));
					calSubEnd.setTime(OtherFunctions.changeDateFromat_ddmmyyy_to_UtilDate(OtherFunctions.changeDateFromatToddmmyyyy(  dto.getSubscriptionToDate() )));
				calT1.setTime( OtherFunctions.changeDateFromat_ddmmyyy_to_UtilDate(OtherFunctions.changeDateFromat( dto.getSchedulingFromDate()  ) ) );
				calShEnd.setTime( OtherFunctions.changeDateFromat_ddmmyyy_to_UtilDate(OtherFunctions.changeDateFromat( dto.getSchedulingToDate()  ) ) );
				
				if( (preShDto!=null && preShDto.getProject().equals(dto.getProject())) ==false) {
					
					if(dtoTemp!=null ) {
						System.out.print(   dtoTemp.getEmployeeId() +"\t" + dtoTemp.getEmployeeName() +"\t" );
						for(ScheduleAlterDto adto : dtoTemp.getAlterList()) {
							System.out.print(OtherFunctions.changeDateFromat( "  "+ adto.getDate()) +"\t" + adto.getLoginTime() +"\t" + adto.getLogoutTime()  +"\t");
						}
						cal.setTime(OtherFunctions.changeDateFromat_ddmmyyy_to_UtilDate(fromDate)); 	
						tempDtoList.add(dtoTemp);
						System.out.println(" tempAddded");
					}
					System.out.println(dtoTemp+ " dtoTemp initiated");
					dtoTemp = new SchedulingDto();
					dtoTemp.setEmployeeId(dto.getEmployeeId());
					dtoTemp.setEmployeeName(dto.getEmployeeName());
					dtoTemp.setProject(dto.getProject());
					dtoTemp.setScheduledFromDate(fromDate);
					dtoTemp.setScheduledToDate(toDate);
					 
					dtoTemp.setSubscriptionDate(dto.getSubscriptionDate());
					 
					dtoTemp.setSubscriptionToDate(dto.getSubscriptionToDate());
					
					
				}
				
				if(cal .compareTo(calT1)<0 && cal.compareTo(calEnd)<=0) {
					
				//System.out.println(" inside interm.");
					while(cal .compareTo(calT1)<0 && cal.compareTo(calEnd)<=0) {
//				System.out.println(cal.getTime());		

						 ScheduleAlterDto alterDto = new ScheduleAlterDto();
						 alterDto .setDate( OtherFunctions.changeDateFromat(  cal.getTime() ));
							if(cal.compareTo(calSubStart)<0  )
							{
								alterDto.setLoginTime("INVALID");
								alterDto.setLogoutTime("INVALID");
							} else if(cal.compareTo(calSubStart)>=0 && cal.compareTo(calSubEnd) <=0  )
							{
								alterDto .setLoginTime("off");
								 alterDto .setLogoutTime("off");
							} else if(cal.compareTo(calSubEnd)>0 )
							{
								alterDto.setLoginTime("INVALID");
								alterDto.setLogoutTime("INVALID");
							}
						 
							dtoTemp.getAlterList() .add(alterDto);
						 cal.add(Calendar.DATE, 1);
					}
				//	tempDtoList.add(dtoTemp);
				}
				
				
			//		System.out.print(   dto.getEmployeeId() +"\t" + dto.getEmployeeName() +"\t" );
					//System.out.print( dto.getSchedulingFromDate());
				//	cal .setTime(  OtherFunctions.changeDateFromat_ddmmyyy_to_UtilDate(OtherFunctions.changeDateFromatToddmmyyyy(dto.getSchedulingFromDate())));
				
					//System.out.println("...cal1 : ." + cal);
					 Calendar cal1 = Calendar.getInstance();
					 ArrayList<ScheduleAlterDto> raList = new ArrayList<ScheduleAlterDto>();
//					 System.out.println(" alter ...");
					for(ScheduleAlterDto adto : dto.getAlterList()) {
					 
						 cal1.setTime(OtherFunctions.changeDateFromat_ddmmyyy_to_UtilDate(OtherFunctions.changeDateFromatToddmmyyyy(  adto.getDate())));
					 	 
//						  System.out.println("cal time : " + OtherFunctions.changeDateFromat(  cal.getTime()) +" cal1 : " + OtherFunctions.changeDateFromat(  cal1.getTime() ) + " compare : " + cal.compareTo(cal1));
						 while(cal.compareTo(cal1)<0 && cal.compareTo(calShEnd)<=0 && cal.compareTo(calEnd)<=0 ) {
//						 	 System.out.print(OtherFunctions.changeDateFromat(  OtherFunctions.changeDateFromat(  cal.getTime() ) ) +"\t" + adto.getLoginTime() +"\t" + adto.getLogoutTime()  +"\t");
							 
							 ScheduleAlterDto alterDto = new ScheduleAlterDto();
							 alterDto .setDate( OtherFunctions.changeDateFromat(  cal.getTime() ));
								if(cal.compareTo(calSubStart)<0  )
								{
									alterDto.setLoginTime("INVALID");
									alterDto.setLogoutTime("INVALID");
								} else if(cal.compareTo(calSubStart)>=0 && cal.compareTo(calSubEnd) <=0  )
								{
									alterDto .setLoginTime(adto.getLoginTime());
									 alterDto .setLogoutTime(adto.getLogoutTime());
								} else if(cal.compareTo(calSubEnd)>0 )
								{
									alterDto.setLoginTime("INVALID");
									alterDto.setLogoutTime("INVALID");
								}
							 
							 //raList.add(alterDto);
								dtoTemp.getAlterList().add(alterDto);
							 cal.add(Calendar.DATE, 1);
							 
						 }
						 
						 if(cal.compareTo(calShEnd)<=0) {
//					 		 System.out.print(OtherFunctions.changeDateFromat( "Alt "+ adto.getDate()) +"\t" + adto.getLoginTime() +"\t" + adto.getLogoutTime()  +"\t");
							 ScheduleAlterDto alterDto = new ScheduleAlterDto();
							 alterDto .setDate( OtherFunctions.changeDateFromat(  cal.getTime() ));
								if(cal.compareTo(calSubStart)<0  )
								{
									alterDto.setLoginTime("INVALID");
									alterDto.setLogoutTime("INVALID");
								} else if(cal.compareTo(calSubStart)>=0 && cal.compareTo(calSubEnd) <=0  )
								{
									alterDto .setLoginTime(adto.getLoginTime());
									 alterDto .setLogoutTime(adto.getLogoutTime());
								} else if(cal.compareTo(calSubEnd)>0   && cal.compareTo(calEnd) <=0 )
								{
									alterDto.setLoginTime("INVALID");
									alterDto.setLogoutTime("INVALID");
								}
							 
							 
							 //raList.add(alterDto);
							dtoTemp.getAlterList().add(alterDto);
							 cal.add(Calendar.DATE, 1);
							 
						 }
				
						
					}
					 
				 	  
					while(cal.compareTo(cal1)>0 && cal.compareTo(calShEnd)<=0   && cal.compareTo(calEnd) <=0 ) {
						
//					 	 System.out.print(OtherFunctions.changeDateFromat(  OtherFunctions.changeDateFromat(  cal.getTime() ) ) +"\t" +  dto.getLoginTime() +"\t" +  dto.getLogoutTime()  +"\t");
						 ScheduleAlterDto alterDto = new ScheduleAlterDto();
						 alterDto .setDate( OtherFunctions.changeDateFromat(  cal.getTime() ));
						
							if(cal.compareTo(calSubStart)<0  )
							{
								alterDto.setLoginTime("INVALID");
								alterDto.setLogoutTime("INVALID");
							} else if(cal.compareTo(calSubStart)>=0 && cal.compareTo(calSubEnd) <=0  )
							{
								 alterDto .setLoginTime(dto.getLoginTime());
								 alterDto .setLogoutTime(dto.getLogoutTime());
							} else if(cal.compareTo(calSubEnd)>0  && cal.compareTo(calEnd) <=0  )
							{
								//System.out.println("Last __>" + calSubEnd.getTime());
								alterDto.setLoginTime("INVALID");
								alterDto.setLogoutTime("INVALID");
							}
					
					
						 
						 //raList.add(alterDto);
							dtoTemp.getAlterList() .add(alterDto);
//						  System.out.println("\ncal time : " + OtherFunctions.changeDateFromat(  cal.getTime()) + "cal1 time : " + OtherFunctions.changeDateFromat(  cal1.getTime()) + " calend : " + OtherFunctions.changeDateFromat(  calEnd.getTime() ) + " compare2 : " + cal.compareTo(calEnd) + " compare 1 : " + cal.compareTo(cal1));
						 cal.add(Calendar.DATE, 1);
					}
					//dto.setAlterList(raList);
				//	System.out.println();
				
				preShDto = dto;
				}
			
			if(dtoTemp!=null ) {
				System.out.print(   dtoTemp.getEmployeeId() +"\t" + dtoTemp.getEmployeeName() +"\t" );
				for(ScheduleAlterDto adto : dtoTemp.getAlterList()) {
					System.out.print(OtherFunctions.changeDateFromat( "  "+ adto.getDate()) +"\t" + adto.getLoginTime() +"\t" + adto.getLogoutTime()  +"\t");
				}
				
				tempDtoList.add(dtoTemp);
				System.out.println(" tempAddded");
			}
			
		}
		return tempDtoList; 
	}
	
	public static void showSchedule() {
		String fromDate ="03/09/2014";
		String toDate = "07/09/2014";
		System.out.println(" .................. ");
		LinkedHashMap<String, ArrayList<SchedulingDto>> dtoMap = new SchedulingDao().getScheduleOfEmpForDownload(fromDate,toDate , "1");
		//System.out.println(" Dto List : " + dtoList.size());
		ArrayList<SchedulingDto> dtoList =getAllInBetweenInAlterList(fromDate, toDate, dtoMap);
		 /*SchedulingDto dtoTemp = dtoList.get(dtoList.size() -1);
		for(ScheduleAlterDto dtoAlterDto : dtoTemp.getAlterList() ) {
			dtoAlterDto.setLoginTime("INVALID");
			dtoAlterDto.setLogoutTime("INVALID");
		} 
		 */ 
		
		ArrayList<SchedulingDto> dtoListNoSchedule = new SchedulingDao().getEmployeesHavingNoSchedule (fromDate,toDate , "1");
		dtoList.addAll(dtoListNoSchedule);
		//System.out.println(" nosh dto List : " + dtoListNoSchedule.size());
	 	
		//SchedulingDto dto =  dtoList.get(0);
		int i =0;
		 
		System.out.println("____________<<___");
		 writeSchedulesToExcel(dtoList);
		 System.out.println("___________>>____");
		System.out.println("over...");
	}
		
	/*	ArrayList<SchedulingDto> dtoList = new SchedulingDao().getScheduleOfEmpForDownload(fromDate,toDate , "1");
		Calendar cal = Calendar.getInstance();
		  
		cal.setTime(OtherFunctions.changeDateFromat_ddmmyyy_to_UtilDate(fromDate));
		Calendar calEnd = Calendar.getInstance();
		 //calEnd.setTime( OtherFunctions.changeDateFromat_ddmmyyy_to_UtilDate(OtherFunctions.changeDateFromatToddmmyyyy(dto.getSchedulingToDate())));
		 calEnd.setTime(OtherFunctions.changeDateFromat_ddmmyyy_to_UtilDate(toDate));
		 
		System.out.println("Cal at start : "+ OtherFunctions.changeDateFromat(  cal.getTime())  );
		//SchedulingDto dto = dtoList.get(0);
		 
		
		for(SchedulingDto dto : dtoList) 
		{
			System.out.print(   dto.getEmployeeId() +"\t" + dto.getEmployeeName() +"\t" );
			//System.out.print( dto.getSchedulingFromDate());
		//	cal .setTime(  OtherFunctions.changeDateFromat_ddmmyyy_to_UtilDate(OtherFunctions.changeDateFromatToddmmyyyy(dto.getSchedulingFromDate())));
			cal.setTime(OtherFunctions.changeDateFromat_ddmmyyy_to_UtilDate(fromDate));
			//System.out.println("...cal1 : ." + cal);
			 Calendar cal1 = Calendar.getInstance();
			for(ScheduleAlterDto adto : dto.getAlterList()) {
				
				
				 * 
				 
				 
				
	
				 cal1.setTime(OtherFunctions.changeDateFromat_ddmmyyy_to_UtilDate(OtherFunctions.changeDateFromatToddmmyyyy(  adto.getDate())));
				 
				 
				 //System.out.println("cal time : " + OtherFunctions.changeDateFromat(  cal.getTime()) +" cal1 : " + OtherFunctions.changeDateFromat(  cal1.getTime() ) + " compare : " + cal.compareTo(cal1));
				 while(cal.compareTo(cal1)<0 && cal.compareTo(calEnd)<=0) {
					 System.out.print(OtherFunctions.changeDateFromat(  OtherFunctions.changeDateFromat(  cal.getTime() ) ) +"\t" + adto.getLoginTime() +"\t" + adto.getLogoutTime()  +"\t");
					 cal.add(Calendar.DATE, 1);
				 }
				 if(cal.compareTo(calEnd)<=0) {
					 System.out.print(OtherFunctions.changeDateFromat( "Alt "+ adto.getDate()) +"\t" + adto.getLoginTime() +"\t" + adto.getLogoutTime()  +"\t");
					 cal.add(Calendar.DATE, 1);
				 }
		
				
			}
			 
			  
			while(cal.compareTo(cal1)>0 && cal.compareTo(calEnd)<=0) {
				
				 System.out.print(OtherFunctions.changeDateFromat(  OtherFunctions.changeDateFromat(  cal.getTime() ) ) +"\t" +  dto.getLoginTime() +"\t" +  dto.getLogoutTime()  +"\t");
				// System.out.println("\ncal time : " + OtherFunctions.changeDateFromat(  cal.getTime()) + "cal1 time : " + OtherFunctions.changeDateFromat(  cal1.getTime()) + " calend : " + OtherFunctions.changeDateFromat(  calEnd.getTime() ) + " compare2 : " + cal.compareTo(calEnd) + " compare 1 : " + cal.compareTo(cal1));
				 cal.add(Calendar.DATE, 1);
			}
	
			System.out.println();
		}
		System.out.println("__________OVER _________");
}*/	

	/*
	 * public static void aaa() { { "routes" : [ { "bounds" : { "northeast" : {
	 * "lat" : 12.9354307, "lng" : 77.67085879999999 }, "southwest" : { "lat" :
	 * 12.8453944, "lng" : 77.5344549 } }, "copyrights" :
	 * "Map data ©2014 Google", "legs" : [ { "distance" : { "text" : "1 m",
	 * "value" : 0 }, "duration" : { "text" : "1 min", "value" : 0 },
	 * "end_address" :
	 * "52, 6th Cross Road, Dwaraka Nagar, Banashankari, Bangalore, Karnataka 560085, India"
	 * , "end_location" : { "lat" : 12.9339629, "lng" : 77.5347367 },
	 * "start_address" :
	 * "52, 6th Cross Road, Dwaraka Nagar, Banashankari, Bangalore, Karnataka 560085, India"
	 * , "start_location" : { "lat" : 12.9339629, "lng" : 77.5347367 }, "steps"
	 * : [ { "distance" : { "text" : "1 m", "value" : 0 }, "duration" : { "text"
	 * : "1 min", "value" : 0 }, "end_location" : { "lat" : 12.9339629, "lng" :
	 * 77.5347367 }, "html_instructions" :
	 * "Head \u003cb\u003eeast\u003c/b\u003e", "polyline" : { "points" :
	 * "gd}mAcofxM" }, "start_location" : { "lat" : 12.9339629, "lng" :
	 * 77.5347367 }, "travel_mode" : "DRIVING" } ], "via_waypoint" : [] }, {
	 * "distance" : { "text" : "1 m", "value" : 0 }, "duration" : { "text" :
	 * "1 min", "value" : 0 }, "end_address" :
	 * "52, 6th Cross Road, Dwaraka Nagar, Banashankari, Bangalore, Karnataka 560085, India"
	 * , "end_location" : { "lat" : 12.9339629, "lng" : 77.5347367 },
	 * "start_address" :
	 * "52, 6th Cross Road, Dwaraka Nagar, Banashankari, Bangalore, Karnataka 560085, India"
	 * , "start_location" : { "lat" : 12.9339629, "lng" : 77.5347367 }, "steps"
	 * : [ { "distance" : { "text" : "1 m", "value" : 0 }, "duration" : { "text"
	 * : "1 min", "value" : 0 }, "end_location" : { "lat" : 12.9339629, "lng" :
	 * 77.5347367 }, "html_instructions" :
	 * "Head \u003cb\u003eeast\u003c/b\u003e", "polyline" : { "points" :
	 * "gd}mAcofxM" }, "start_location" : { "lat" : 12.9339629, "lng" :
	 * 77.5347367 }, "travel_mode" : "DRIVING" } ], "via_waypoint" : [] }, {
	 * "distance" : { "text" : "1 m", "value" : 0 }, "duration" : { "text" :
	 * "1 min", "value" : 0 }, "end_address" :
	 * "52, 6th Cross Road, Dwaraka Nagar, Banashankari, Bangalore, Karnataka 560085, India"
	 * , "end_location" : { "lat" : 12.9339629, "lng" : 77.5347367 },
	 * "start_address" :
	 * "52, 6th Cross Road, Dwaraka Nagar, Banashankari, Bangalore, Karnataka 560085, India"
	 * , "start_location" : { "lat" : 12.9339629, "lng" : 77.5347367 }, "steps"
	 * : [ { "distance" : { "text" : "1 m", "value" : 0 }, "duration" : { "text"
	 * : "1 min", "value" : 0 }, "end_location" : { "lat" : 12.9339629, "lng" :
	 * 77.5347367 }, "html_instructions" :
	 * "Head \u003cb\u003eeast\u003c/b\u003e", "polyline" : { "points" :
	 * "gd}mAcofxM" }, "start_location" : { "lat" : 12.9339629, "lng" :
	 * 77.5347367 }, "travel_mode" : "DRIVING" } ], "via_waypoint" : [] }, {
	 * "distance" : { "text" : "1 m", "value" : 0 }, "duration" : { "text" :
	 * "1 min", "value" : 0 }, "end_address" :
	 * "52, 6th Cross Road, Dwaraka Nagar, Banashankari, Bangalore, Karnataka 560085, India"
	 * , "end_location" : { "lat" : 12.9339629, "lng" : 77.5347367 },
	 * "start_address" :
	 * "52, 6th Cross Road, Dwaraka Nagar, Banashankari, Bangalore, Karnataka 560085, India"
	 * , "start_location" : { "lat" : 12.9339629, "lng" : 77.5347367 }, "steps"
	 * : [ { "distance" : { "text" : "1 m", "value" : 0 }, "duration" : { "text"
	 * : "1 min", "value" : 0 }, "end_location" : { "lat" : 12.9339629, "lng" :
	 * 77.5347367 }, "html_instructions" :
	 * "Head \u003cb\u003eeast\u003c/b\u003e", "polyline" : { "points" :
	 * "gd}mAcofxM" }, "start_location" : { "lat" : 12.9339629, "lng" :
	 * 77.5347367 }, "travel_mode" : "DRIVING" } ], "via_waypoint" : [] }, {
	 * "distance" : { "text" : "2.0 km", "value" : 1960 }, "duration" : { "text"
	 * : "7 mins", "value" : 438 }, "end_address" :
	 * "132-134, Ittamadu Main Road, Ittamadu, Banashankari, Bangalore, Karnataka 560085, India"
	 * , "end_location" : { "lat" : 12.9269356, "lng" : 77.54484309999999 },
	 * "start_address" :
	 * "52, 6th Cross Road, Dwaraka Nagar, Banashankari, Bangalore, Karnataka 560085, India"
	 * , "start_location" : { "lat" : 12.9339629, "lng" : 77.5347367 }, "steps"
	 * : [ { "distance" : { "text" : "0.1 km", "value" : 132 }, "duration" : {
	 * "text" : "1 min", "value" : 32 }, "end_location" : { "lat" : 12.9350605,
	 * "lng" : 77.53447 }, "html_instructions" :
	 * "Head \u003cb\u003enorth\u003c/b\u003e", "polyline" : { "points" :
	 * "gd}mAcofxMQCSCO?OBSHg@XOJMFKBO@OAOA" }, "start_location" : { "lat" :
	 * 12.9339629, "lng" : 77.5347367 }, "travel_mode" : "DRIVING" }, {
	 * "distance" : { "text" : "0.2 km", "value" : 231 }, "duration" : { "text"
	 * : "3 mins", "value" : 180 }, "end_location" : { "lat" : 12.9354307, "lng"
	 * : 77.53646259999999 }, "html_instructions" :
	 * "Turn \u003cb\u003eright\u003c/b\u003e towards \u003cb\u003eOuter Ring Rd\u003c/b\u003e"
	 * , "maneuver" : "turn-right", "polyline" : { "points" :
	 * "ck}mAmmfxM@uAOkFCSAGEKMGQQOI" }, "start_location" : { "lat" :
	 * 12.9350605, "lng" : 77.53447 }, "travel_mode" : "DRIVING" }, { "distance"
	 * : { "text" : "91 m", "value" : 91 }, "duration" : { "text" : "1 min",
	 * "value" : 14 }, "end_location" : { "lat" : 12.9350157, "lng" :
	 * 77.53718169999999 }, "html_instructions" :
	 * "Turn \u003cb\u003eright\u003c/b\u003e towards \u003cb\u003eOuter Ring Rd\u003c/b\u003e"
	 * , "maneuver" : "turn-right", "polyline" : { "points" :
	 * "mm}mA{yfxMr@{ALUN]" }, "start_location" : { "lat" : 12.9354307, "lng" :
	 * 77.53646259999999 }, "travel_mode" : "DRIVING" }, { "distance" : { "text"
	 * : "10 m", "value" : 10 }, "duration" : { "text" : "1 min", "value" : 4 },
	 * "end_location" : { "lat" : 12.934935, "lng" : 77.5371438 },
	 * "html_instructions" :
	 * "Turn \u003cb\u003eright\u003c/b\u003e towards \u003cb\u003eOuter Ring Rd\u003c/b\u003e"
	 * , "maneuver" : "turn-right", "polyline" : { "points" : "{j}mAk~fxMNF" },
	 * "start_location" : { "lat" : 12.9350157, "lng" : 77.53718169999999 },
	 * "travel_mode" : "DRIVING" }, { "distance" : { "text" : "1.2 km", "value"
	 * : 1247 }, "duration" : { "text" : "3 mins", "value" : 170 },
	 * "end_location" : { "lat" : 12.9281426, "lng" : 77.5462478 },
	 * "html_instructions" :
	 * "Turn \u003cb\u003eleft\u003c/b\u003e onto \u003cb\u003eOuter Ring Rd\u003c/b\u003e\u003cdiv style=\"font-size:0.9em\"\u003ePass by Dev's Paradise Deluxe Lodge (on the left in 400&nbsp;m)\u003c/div\u003e"
	 * , "maneuver" : "turn-left", "polyline" : { "points" :
	 * "kj}mAc~fxMr@oA@C`A}BFMnAqC~@gBf@{@~@{A`AyAh@w@BClAmBT[n@aAp@aALQLOLQFGDIDEDGFGFKNQX[|@aAJKjJyKDIFGDGDGrAgB"
	 * }, "start_location" : { "lat" : 12.934935, "lng" : 77.5371438 },
	 * "travel_mode" : "DRIVING" }, { "distance" : { "text" : "0.2 km", "value"
	 * : 194 }, "duration" : { "text" : "1 min", "value" : 32 }, "end_location"
	 * : { "lat" : 12.9273902, "lng" : 77.54503810000001 }, "html_instructions"
	 * :
	 * "Turn \u003cb\u003eright\u003c/b\u003e onto \u003cb\u003eIttamadu Main Rd\u003c/b\u003e\u003cdiv style=\"font-size:0.9em\"\u003ePass by S G Hospital (on the right)\u003c/div\u003e"
	 * , "maneuver" : "turn-right", "polyline" : { "points" :
	 * "{_|mAawhxMNJj@V\\RTJz@\\FFDF?H?FAFG^YdA" }, "start_location" : { "lat" :
	 * 12.9281426, "lng" : 77.5462478 }, "travel_mode" : "DRIVING" }, {
	 * "distance" : { "text" : "55 m", "value" : 55 }, "duration" : { "text" :
	 * "1 min", "value" : 6 }, "end_location" : { "lat" : 12.9269356, "lng" :
	 * 77.54484309999999 }, "html_instructions" :
	 * "Turn \u003cb\u003eleft\u003c/b\u003e to stay on \u003cb\u003eIttamadu Main Rd\u003c/b\u003e\u003cdiv style=\"font-size:0.9em\"\u003eDestination will be on the right\u003c/div\u003e"
	 * , "maneuver" : "turn-left", "polyline" : { "points" : "e{{mAoohxMxAf@" },
	 * "start_location" : { "lat" : 12.9273902, "lng" : 77.54503810000001 },
	 * "travel_mode" : "DRIVING" } ], "via_waypoint" : [] }, { "distance" : {
	 * "text" : "1 m", "value" : 0 }, "duration" : { "text" : "1 min", "value" :
	 * 0 }, "end_address" :
	 * "132-134, Ittamadu Main Road, Ittamadu, Banashankari, Bangalore, Karnataka 560085, India"
	 * , "end_location" : { "lat" : 12.9269356, "lng" : 77.54484309999999 },
	 * "start_address" :
	 * "132-134, Ittamadu Main Road, Ittamadu, Banashankari, Bangalore, Karnataka 560085, India"
	 * , "start_location" : { "lat" : 12.9269356, "lng" : 77.54484309999999 },
	 * "steps" : [ { "distance" : { "text" : "1 m", "value" : 0 }, "duration" :
	 * { "text" : "1 min", "value" : 0 }, "end_location" : { "lat" : 12.9269356,
	 * "lng" : 77.54484309999999 }, "html_instructions" :
	 * "Head \u003cb\u003eeast\u003c/b\u003e on \u003cb\u003eIttamadu Main Rd\u003c/b\u003e"
	 * , "polyline" : { "points" : "kx{mAgnhxM" }, "start_location" : { "lat" :
	 * 12.9269356, "lng" : 77.54484309999999 }, "travel_mode" : "DRIVING" } ],
	 * "via_waypoint" : [] }, { "distance" : { "text" : "20.0 km", "value" :
	 * 20012 }, "duration" : { "text" : "29 mins", "value" : 1769 },
	 * "end_address" :
	 * "Infosys Drive, Electronics City Phase 1, Electronics City, Bangalore, Karnataka 560100, India"
	 * , "end_location" : { "lat" : 12.8460243, "lng" : 77.6615425 },
	 * "start_address" :
	 * "132-134, Ittamadu Main Road, Ittamadu, Banashankari, Bangalore, Karnataka 560085, India"
	 * , "start_location" : { "lat" : 12.9269356, "lng" : 77.54484309999999 },
	 * "steps" : [ { "distance" : { "text" : "55 m", "value" : 55 }, "duration"
	 * : { "text" : "1 min", "value" : 5 }, "end_location" : { "lat" :
	 * 12.9273902, "lng" : 77.54503810000001 }, "html_instructions" :
	 * "Head \u003cb\u003enorth-east\u003c/b\u003e on \u003cb\u003eIttamadu Main Rd\u003c/b\u003e"
	 * , "polyline" : { "points" : "kx{mAgnhxMyAg@" }, "start_location" : {
	 * "lat" : 12.9269356, "lng" : 77.54484309999999 }, "travel_mode" :
	 * "DRIVING" }, { "distance" : { "text" : "0.2 km", "value" : 194 },
	 * "duration" : { "text" : "1 min", "value" : 36 }, "end_location" : { "lat"
	 * : 12.9281426, "lng" : 77.5462478 }, "html_instructions" :
	 * "Turn \u003cb\u003eright\u003c/b\u003e to stay on \u003cb\u003eIttamadu Main Rd\u003c/b\u003e\u003cdiv style=\"font-size:0.9em\"\u003ePass by S G Hospital (on the left)\u003c/div\u003e"
	 * , "maneuver" : "turn-right", "polyline" : { "points" :
	 * "e{{mAoohxMXeAF_@@G?G?IEGGG{@]UK]Sk@WOK" }, "start_location" : { "lat" :
	 * 12.9273902, "lng" : 77.54503810000001 }, "travel_mode" : "DRIVING" }, {
	 * "distance" : { "text" : "2.7 km", "value" : 2654 }, "duration" : { "text"
	 * : "5 mins", "value" : 315 }, "end_location" : { "lat" : 12.9155312, "lng"
	 * : 77.5653634 }, "html_instructions" :
	 * "Turn \u003cb\u003eright\u003c/b\u003e onto \u003cb\u003eOuter Ring Rd\u003c/b\u003e\u003cdiv style=\"font-size:0.9em\"\u003ePass by M S P Moto World (on the left)\u003c/div\u003e"
	 * , "maneuver" : "turn-right", "polyline" : { "points" :
	 * "{_|mAawhxMxAoBbB_C^g@^i@X]d@m@Xa@FIDGFI^k@FMFKFKFMRc@b@y@t@}AFMZw@Vk@x@mBBEt@oBHYlAeFNm@DSXoA@EJc@h@aCR}@P{@R{@P{@RaAXuAxBiJZ}AXgB@QJw@R_BBURqBPcBBQ@IBOFKHIDEDCpA[rCeALGFEFINSvAsCHMHONOXYf@[DElBiAVOnBi@ZKlBm@DA`Bi@p@Q|C_ARIJC"
	 * }, "start_location" : { "lat" : 12.9281426, "lng" : 77.5462478 },
	 * "travel_mode" : "DRIVING" }, { "distance" : { "text" : "0.2 km", "value"
	 * : 172 }, "duration" : { "text" : "1 min", "value" : 16 }, "end_location"
	 * : { "lat" : 12.9141129, "lng" : 77.565983 }, "html_instructions" :
	 * "Take the exit towards \u003cb\u003eSubramanya Pura Rd\u003c/b\u003e",
	 * "maneuver" : "ramp-left", "polyline" : { "points" :
	 * "aqymAonlxMPQHCJExA]zAg@BAp@UFA" }, "start_location" : { "lat" :
	 * 12.9155312, "lng" : 77.5653634 }, "travel_mode" : "DRIVING" }, {
	 * "distance" : { "text" : "0.9 km", "value" : 948 }, "duration" : { "text"
	 * : "2 mins", "value" : 116 }, "end_location" : { "lat" : 12.9169309, "lng"
	 * : 77.5734904 }, "html_instructions" :
	 * "Turn \u003cb\u003eleft\u003c/b\u003e onto \u003cb\u003eSubramanya Pura Rd\u003c/b\u003e"
	 * , "maneuver" : "turn-left", "polyline" : { "points" :
	 * "ehymAkrlxMH]@GHUHW@ETw@@U@e@@]?C?C?GAEIOa@q@Y_@W[IIW]YYk@q@]a@IIACi@o@KQKSGQCQG_@GUCSAIAECKCGAECCGGKEMGYK[KWKKGKEGGCEEGCIIa@CKESMy@EWAQ?Q?U@g@@_@?U?K?A?Q?SC_@Em@Eu@AGu@sB"
	 * }, "start_location" : { "lat" : 12.9141129, "lng" : 77.565983 },
	 * "travel_mode" : "DRIVING" }, { "distance" : { "text" : "82 m", "value" :
	 * 82 }, "duration" : { "text" : "1 min", "value" : 17 }, "end_location" : {
	 * "lat" : 12.9176648, "lng" : 77.57347799999999 }, "html_instructions" :
	 * "Turn \u003cb\u003eleft\u003c/b\u003e onto \u003cb\u003eKanakapura Rd\u003c/b\u003e"
	 * , "maneuver" : "turn-left", "polyline" : { "points" : "yyymAianxM_CBQA"
	 * }, "start_location" : { "lat" : 12.9169309, "lng" : 77.5734904 },
	 * "travel_mode" : "DRIVING" }, { "distance" : { "text" : "2.6 km", "value"
	 * : 2614 }, "duration" : { "text" : "5 mins", "value" : 321 },
	 * "end_location" : { "lat" : 12.9168103, "lng" : 77.59744429999999 },
	 * "html_instructions" :
	 * "Turn \u003cb\u003eright\u003c/b\u003e onto \u003cb\u003eMarenahalli Rd\u003c/b\u003e\u003cdiv style=\"font-size:0.9em\"\u003ePass by Raghavendra Swamy Mutt (on the left in 1.3&nbsp;km)\u003c/div\u003e"
	 * , "maneuver" : "turn-right", "polyline" : { "points" :
	 * "k~ymAganxMAUCiAC_AAqA?_GJ_FFuAFu@@E@EBCJIZ[HKFIBMBM@QAoG?{A?S?qA@cBB}B?GJmI?QD_B@gB?SAeE?S@cB@sBDgDHsIDeGDkJ@}@?iA?cA?S?y@?_A@{@@iA?O?oAB}@@aABy@@oA@S?K@w@@e@"
	 * }, "start_location" : { "lat" : 12.9176648, "lng" : 77.57347799999999 },
	 * "travel_mode" : "DRIVING" }, { "distance" : { "text" : "0.6 km", "value"
	 * : 640 }, "duration" : { "text" : "1 min", "value" : 53 }, "end_location"
	 * : { "lat" : 12.9167093, "lng" : 77.6033494 }, "html_instructions" :
	 * "Continue onto \u003cb\u003eMarenahalli Flyover\u003c/b\u003e\u003cdiv style=\"font-size:0.9em\"\u003ePass by Chungs Chinese Restaurant (on the left)\u003c/div\u003e"
	 * , "polyline" : { "points" : "ayymA_wrxMDgC?S?w@DuE@cA?cA@_@?eDAcGBuA@kA"
	 * }, "start_location" : { "lat" : 12.9168103, "lng" : 77.59744429999999 },
	 * "travel_mode" : "DRIVING" }, { "distance" : { "text" : "2.1 km", "value"
	 * : 2133 }, "duration" : { "text" : "5 mins", "value" : 292 },
	 * "end_location" : { "lat" : 12.9173589, "lng" : 77.62284729999999 },
	 * "html_instructions" :
	 * "Continue onto \u003cb\u003eMarenahalli Rd/Outer Ring Rd\u003c/b\u003e\u003cdiv style=\"font-size:0.9em\"\u003eContinue to follow Outer Ring Rd\u003c/div\u003e\u003cdiv style=\"font-size:0.9em\"\u003ePass by Indian Oil Petrol Bunk (on the right)\u003c/div\u003e"
	 * , "polyline" : { "points" :
	 * "mxymA}{sxM@oCDgF?M@m@Ek@@q@?sA?m@@cA?w@@}@?sFBwF@S?E@w@@a@F_I?e@FkJ@a@@o@LeB?I@KNgEHqCC}IAY?s@CaAAa@E]G_@Oq@s@kDi@}BYoA_@yAQ_A"
	 * }, "start_location" : { "lat" : 12.9167093, "lng" : 77.6033494 },
	 * "travel_mode" : "DRIVING" }, { "distance" : { "text" : "0.3 km", "value"
	 * : 318 }, "duration" : { "text" : "1 min", "value" : 37 }, "end_location"
	 * : { "lat" : 12.9148609, "lng" : 77.62425809999999 }, "html_instructions"
	 * :
	 * "Turn \u003cb\u003eright\u003c/b\u003e onto \u003cb\u003eSilk Board Signal Right Rd/AH 43/AH 45/NH 7\u003c/b\u003e\u003cdiv style=\"font-size:0.9em\"\u003eContinue to follow AH 43/AH 45/NH 7\u003c/div\u003e"
	 * , "maneuver" : "turn-right", "polyline" : { "points" :
	 * "o|ymAyuwxMPId@a@bD_BnGgCFE" }, "start_location" : { "lat" : 12.9173589,
	 * "lng" : 77.62284729999999 }, "travel_mode" : "DRIVING" }, { "distance" :
	 * { "text" : "0.6 km", "value" : 625 }, "duration" : { "text" : "1 min",
	 * "value" : 45 }, "end_location" : { "lat" : 12.9100073, "lng" : 77.6271321
	 * }, "html_instructions" :
	 * "Slight \u003cb\u003eright\u003c/b\u003e to stay on \u003cb\u003eAH 43/AH 45/NH 7\u003c/b\u003e\u003cdiv style=\"font-size:0.9em\"\u003ePass by Krimson Square (on the left)\u003c/div\u003e"
	 * , "maneuver" : "turn-slight-right", "polyline" : { "points" :
	 * "{lymAs~wxMvAYv@_@xXcO" }, "start_location" : { "lat" : 12.9148609, "lng"
	 * : 77.62425809999999 }, "travel_mode" : "DRIVING" }, { "distance" : {
	 * "text" : "9.0 km", "value" : 9044 }, "duration" : { "text" : "7 mins",
	 * "value" : 439 }, "end_location" : { "lat" : 12.8453944, "lng" :
	 * 77.66590789999999 }, "html_instructions" :
	 * "Slight \u003cb\u003eright\u003c/b\u003e onto \u003cb\u003eElectronic City Flyover/Hosur Rd\u003c/b\u003e\u003cdiv style=\"font-size:0.9em\"\u003eToll road\u003c/div\u003e"
	 * , "maneuver" : "turn-slight-right", "polyline" : { "points" :
	 * "qnxmAqpxxMVG|Au@~F{C|D{BRK@ABADCz@c@HGLGLI|@c@NIHEBA@?RKpFuCdCqAzBkAnBeAh@Yv@a@~A{@rAq@bAk@NGDCFC@Ad@WxC}AtAs@vAu@vBiAz@e@z@c@z@e@^SHCFEFEFCd@W^SpAq@zAw@xAy@~Ay@`B}@HELG@ALG\\QBAHELIrAq@~A{@l@[t@a@rAq@bAg@x@c@xBkA|BmAh@WRMTK~Ay@dAk@LGNIJGBAd@WDCNGLIfAk@bB}@rAs@bB{@zBmA`Ag@|A{@xBkAbDcBfCsA~Ay@hE{BvFwCdKmF`B{@VMNIBCJEh@Yt@a@FCDEFCDCFEFEHCFEHGHEFEHGFEHEFEHGJGJIFEFEFGHEFGHGNKVSVUPMPONQHGHIJGHIVUVUJKLIl@i@XWVSfFqEXUHGFGHGHGHGFGHGHGFEFGFEFEHGHGnP}LRMNKJIHGjBuA|PgM~PiMjDgClCmB`CgB`As@xCyBzC{BnEcDHGTO@A@A@?FG@A@?`@[XSZWb@]d@_@vEkDHGFERONK|@q@hBsApB{AlA}@bDaCrDoCfDcCdBqApByAzC{BjFwDbBmAJEJC^K@A@?@?DAF?D?D?F?D?@?B?D@D@D@B?@@D@@?@@@@@?BBDBDBDBBDBBBBDDBDBBBD@DBDBDBD@DhAvCxAtDr@`BDLj@fBBJ@JH^DTBR@L@JH`E?X"
	 * }, "start_location" : { "lat" : 12.9100073, "lng" : 77.6271321 },
	 * "travel_mode" : "DRIVING" }, { "distance" : { "text" : "0.5 km", "value"
	 * : 475 }, "duration" : { "text" : "1 min", "value" : 60 }, "end_location"
	 * : { "lat" : 12.8455042, "lng" : 77.66153439999999 }, "html_instructions"
	 * :
	 * "Continue onto \u003cb\u003eInfosys Avenue\u003c/b\u003e\u003cdiv style=\"font-size:0.9em\"\u003ePass by Tata Power Company Limited (on the right)\u003c/div\u003e"
	 * , "polyline" : { "points" : "uzkmA}b`yMCLELAVC`@AlC?LArF?FA|J" },
	 * "start_location" : { "lat" : 12.8453944, "lng" : 77.66590789999999 },
	 * "travel_mode" : "DRIVING" }, { "distance" : { "text" : "58 m", "value" :
	 * 58 }, "duration" : { "text" : "1 min", "value" : 17 }, "end_location" : {
	 * "lat" : 12.8460243, "lng" : 77.6615425 }, "html_instructions" :
	 * "Turn \u003cb\u003eright\u003c/b\u003e after International Institute of Information Technology Bangalore (on the left)\u003cdiv style=\"font-size:0.9em\"\u003eDestination will be on the right\u003c/div\u003e"
	 * , "maneuver" : "turn-right", "polyline" : { "points" : "k{kmAqg_yMgBA" },
	 * "start_location" : { "lat" : 12.8455042, "lng" : 77.66153439999999 },
	 * "travel_mode" : "DRIVING" } ], "via_waypoint" : [] } ],
	 * "overview_polyline" : { "points" :
	 * "gd}mAcofxMe@G_@ByAv@[D_@C@uAOkFE[SSa@[`AqBN]NFt@sAhAkCnAqC~@gBfBwC|DcGdCqDdAuArB{B~JsLxAoBz@b@r@^z@\\FFDPANa@dBxAf@yAg@`@eB@OEQGG{@]s@_@{@c@|FaI`BwBt@kAj@iAxAwCb@eApAyCx@uBlBaIpA{FjAqFl@wCtCgMz@qGl@}FDYPUJIpA[rCeATMV]`BaDX_@`Au@rBoAVOnBi@hCy@fBk@nEqA^MZUdBc@~Ai@x@WJe@j@kBD}A?KKU{@qAa@e@{BkCaAoASe@Kq@Oy@M]SMg@SkAe@KMIQa@{BGi@?g@BiBCgAKcBw@{BqC@E_BEqC?_GJ_FNkCBKt@u@JWD_@AqMPwPDqB@{BAyEBwEN{NJqR@_F?yBBeCD_FFiDHyFFuK@eEAcGBuAB{EDuF@m@Ek@@eCBgFBkN@YJ_MHmKNuCZoJEwJCuBG_AWqA}AiHy@iDQ_APId@a@bD_BvGmCvAYv@_@xXcOVG|IqEvEkCxAw@~BkAxZcPnXuNrRcKbs@__@bO_If\\{P`G}CjCaBnA_AbCsBlNyLz{@ko@p`@oYdDgCfPwLlQsM~Q_NbBmAJEj@ODAh@AZF\\P^`@LVhDxIx@nBn@rBTtABXHzEIZEx@CvKA|JgBA"
	 * }, "summary" : "Electronic City Flyover/Hosur Rd", "warnings" : [],
	 * "waypoint_order" : [ 0, 1, 2, 3, 4, 5 ] } ], "status" : "OK" } }
	 */
	/*
	 * public static void main(String[] args) throws Exception { =======
	 */

	/*
	 * 
	 * 
	 * //import com.agiledge.atom.sms.SendSMS;
	 * 
	 * 
	 * public class Main {
	 * 
	 * 
	 * 
	 * public static void main(String[] args) {
	 * 
	 * System.out.println(Cell.CELL_TYPE_BLANK);
	 * System.out.println(Cell.CELL_TYPE_BOOLEAN);
	 * System.out.println(Cell.CELL_TYPE_ERROR);
	 * System.out.println(Cell.CELL_TYPE_FORMULA);
	 * System.out.println(Cell.CELL_TYPE_NUMERIC);
	 * System.out.println(Cell.CELL_TYPE_STRING);
	 * 
	 * 
	 * }  
	 * 
	 * HttpClient client = new DefaultHttpClient(); //String name =
	 * RandomStringUtils.randomAlphabetic( 8 ); HttpGet request=new
	 * HttpGet("http://www.example.com/customers/12345"); //HttpPut request=new
	 * HttpPut("https://www.peopleworks.ind.in/api/LogOn/Put); JSONObject js=new
	 * JSONObject(); js.put("ClientCodeSlashName", "ps1/Cd16");
	 * js.put("Password", "Passw0rd!"); HttpEntity entity = new
	 * StringEntity(js.toString()); // request.setEntity(entity); HttpResponse
	 * response = client.execute(request); BufferedReader rd = new
	 * BufferedReader (new
	 * InputStreamReader(response.getEntity().getContent())); String line = "";
	 * while ((line = rd.readLine()) != null) { System.out.println(line); } }
	 */
	public static void main (String[] args) {		
		

		
	/*	 
		SettingsConstant .setCompany();
		String se=SettingsConstant.getCurdate();
		String currentDate = OtherFunctions. changeTimeFormat( SettingsConstant.getCurdate(),"dd/MM/yyyy", "yyyy-MM-dd" );
		
		System.out.println(currentDate);
			

			}
		
			//System.out.println(OtherFunctions.addTime("10:40", "40.5"));
			
			
		*/	
				
			 //sm.send("muhammad@agiledgesolutions.com","Test Mail","This is a testmail");
			// permutation("1234567890");
			 //getdistnce();
			 try{
				 
				//System.out.println(OtherFunctions.checkedDate("2015-01-21"));
				 /*
				 byte[] ba=new byte[4];
				 ba[0]=0b1111;
				 ba[1]=0b1111;
				 ba[2]=0b1110;
				 ba[3]=0b1101;
				 String stringVal="";

				 
				 for(int i=0;i<4;i++)
				 {
					stringVal +=Integer.toBinaryString(ba[i]);
				 }
				 int decimalNumber = Integer.parseInt(stringVal,2);
				   */
			//	 System.out.println(decimalNumber);
				 
				// System.out.println(OtherFunctions.checkedDate("2014-01-05"));
				
			ClientConfig config = new DefaultClientConfig();
			Client client = Client.create(config);
		/*
			WebResource webResource = client
					.resource("http://192.168.5.110/api/Logon");
			WebResource webResource =
			client.resource("https://www.peopleworks.ind.in/PWWEbApi/api/LogOn/put");
			JSONObject js = new JSONObject();

			String username = "cd\\TR01";
			String password = "Atom@user123";

			js.put("ClientCodeSlashName", username);
			js.put("Password", password);
			ClientResponse response = webResource.type(
					MediaType.APPLICATION_JSON).put(ClientResponse.class,
					js.toString());
			System.out.println("111"+response.toString());
			System.out.println(response.getHeaders());
			JSONObject jso = new JSONObject(response.getEntity(String.class));
			System.out.println(jso.toString());
			JSONArray getArray = jso.getJSONArray("ModuleDetails");
			String sessionId = (String) jso.get("sessionID");
			System.out.println("session id"+sessionId);
			*/		
			DefaultHttpClient client1 = new DefaultHttpClient();			
			/*HttpGet request = new HttpGet(
					"https://www.peopleworks.ind.in/PWWEbApi/(X(1)S("+sessionId+"))/Api/EmloyeeDatas/GetEmployeeDetails/"+ sessionId);*/
		
			String reqString="https://www.peopleworks.ind.in/pwwebapi/api/validation/validateTravelUser?id=dHIwMX4yMjUyMDE1IH5DaHJvbWU=&userId=TR01";
						
			//String reqString="https://www.peopleworks.ind.in/pwwebapi/(X(1)S("+sessionId+"))/api/validation/validateTravelUser?id="+sessionId+"&userId="+username;
		//	String reqString="http://192.168.5.110/api/Validation/validateTravelUser?id="+sessionId+"&userId=PR102";
		//	String reqString="http://192.168.5.110/api/EmloyeeDatas/GetEmployeeDetails/"+ sessionId;
		//	String reqString="http://192.168.5.110/api/EmloyeeDatas/GetEmployeeDetails/e3x32umrp1gmjmtbrx2r0b3i";			
			System.out.println(reqString);
			HttpGet request = new HttpGet(reqString);
			
			
			HttpResponse response1 = client1.execute(request);
			System.out.println(response1.toString());
			BufferedReader rd = new BufferedReader(new InputStreamReader(
					response1.getEntity().getContent()));
			String line = "";
			String nLine = "";
			while ((line = rd.readLine()) != null) {
				nLine += line;
			}
			System.out.println(nLine);
			
		} catch (Exception e) {
			System.out.println("Error" + e);
		}	

	}

	private static void readExcelFromFile() throws IOException, InvalidFormatException {
		  //File excel =  new File ("C:/Users/Leah-Dina/Desktop/LogFile.xlsx");
		  FileInputStream fis = new FileInputStream(new File("D:\\CrossDomain\\ScheduleTemplateGeneated.xlsx"));
		//FileInputStream fis = new FileInputStream(new File("D:\\CrossDomain\\testExcel.xlsx"));
	       // FileInputStream fis = new FileInputStream(excel);
		
	        XSSFWorkbook wb = new XSSFWorkbook(fis);
		  //org.apache.poi.ss.usermodel.Workbook wb = WorkbookFactory.create(fis);
		  
	        //XSSFSheet ws = wb.getSheet(0);
	        XSSFSheet ws =  wb.getSheetAt(0);
		  //org.apache.poi.ss.usermodel.Sheet ws = wb.getSheetAt(0);
	        
	        int rowNum = ws.getLastRowNum() + 1;
	        System.out.println(".................");
	        int colNum = ws.getRow(0).getLastCellNum();
	        
	        String [][] data = new String [rowNum] [colNum];
	        
	        ArrayList<String> dates= new ArrayList<String>();
	        ArrayList<SchedulingDto> dtoList = new ArrayList<SchedulingDto>();
	        SchedulingDto dto = null;
	        ScheduleAlterDto adto = null;
	        for(int i = 0; i <rowNum; i++){
	        	System.out.println(" row num : " + rowNum);
	            XSSFRow row = (XSSFRow) ws.getRow(i);
	        	//Row row = (XSSFRow) ws.getRow(i);
	             System.out.println();
	             boolean invalidFlow=true;
	             boolean invalid=false;
	             boolean firstLogoutTime=false;
	             boolean sameLogTime =false;
	             //boolean get
	                for (int j = 0; (j < colNum&&i<=1) || (j<( 5+ dates.size()*2)&&i>1); j++){
	                    XSSFCell cell = row.getCell(j);
	              //  	org.apache.poi.ss.usermodel.Cell cell =  row.getCell(j);
	                    
	                    String value = cell.toString();
	                    data[i][j] = value;
	                    if(j>4 && i==0) {
	                  //  System.out.print ("the value is("+i+", "+j+") " + value +"\t");
	                    	if(i==0  && j%2==1) {
	                    		
	                    		System.out.println(" inside date");
	    						String date="";
	    						if(cell.getCellType()==Cell.CELL_TYPE_NUMERIC  && DateUtil.isCellDateFormatted(cell)) {
	    							
	    							 
    								 date =OtherFunctions
 	    									.changeDateFromatToSqlFormat(  cell.getDateCellValue() );
    								
    							} else if(cell.getCellType()==Cell.CELL_TYPE_STRING){
	    						try{
	    							Calendar cal = Calendar.getInstance();
	    							cal.set(1900, 0, 0, 0, 0);
	    				
	    						    cal.add(Calendar.DATE,  Integer.parseInt( cell.getStringCellValue())-1);
	    							 date =OtherFunctions
	    									.changeDateFromatToSqlFormat( new Date(cal.getTimeInMillis()) );
	    							}catch(NumberFormatException ne) {
	    								date =cell.getStringCellValue();
	    							}
	    						//System.out.println(	" To date :" + dto.getSchedulingToDate());
	    							if(OtherFunctions.checkDateFormat(date, "dd/MM/yyyy")==false){
	    								 if(OtherFunctions.checkDateFormat(date, "yyyy-MM-dd")) {
	    									 date =OtherFunctions.changeDateFromat(date);
	    								 } else if (OtherFunctions.checkDateFormat(date, "yyyy-MMM-dd")) {
	    									 date = OtherFunctions.changeTimeFormat(date, "yyyy-MMM-dd", "yyyy-MM-dd");
	    								 } else if (OtherFunctions.checkDateFormat(date, "dd-MMM-yyyy")) {
	    									 date = OtherFunctions.changeTimeFormat(date, "dd-MMM-yyyy", "yyyy-MM-dd");
	    								 } else {
//	    									 message=message + "|" + "To Date :incorrect format (row:" + rowIndex+" )";
//	    									 error=true;
	    									 System.out.println("date validation error");
	    									 
	    								 }
	    							 }
	    							
    							}
	    						dates.add(date);	
    							} else if (i>1) {
    								System.out.print(String.format("(%2d, %2d ) : %9s",i,j,value));
    							}	

	                    	
	                    	
	                    } else if(i>1) {
	                    	if(j==0) {
	                    		System.out.println("Creating scheduling dto ");
	                    		dto = new SchedulingDto();
	                    		dtoList.add(dto);
	                    		if(cell.getCellType()==Cell.CELL_TYPE_STRING) {
	                    		dto.setEmployeeId(cell.getStringCellValue());
	                    		
	                    		} else {
	                    			dto.setEmployeeId("Error");
	                    		}
	                    	} else if(j==1) {
	                    		 
	                    		 
	                    		if(cell.getCellType()==Cell.CELL_TYPE_STRING) {
	                    		dto.setEmployeeName(cell.getStringCellValue());
	                    		
	                    		} else {
	                    			dto.setEmployeeName("Error");
	                    		}
	                    	} else  if(j==2) {
	                    		 
	                    		if(cell.getCellType()==Cell.CELL_TYPE_STRING) {
	                    		dto.setProject(cell.getStringCellValue());
	                    			
	                    		} else {
	                    			dto.setProject("Error");
	                    		}
	                    	} else if (j>4) {
	                    		 
	                    		if(j%2==1) {
	                    			System.out.println(" in login ... (" + i + " , " + j + " )");
	                    			String loginTime= getLoginTime(cell);
	                    			if(loginTime.equalsIgnoreCase("invalid") ) {
	                    				 invalid=true;
	                    			}else {
	                    				

	                    				 invalid=false;
		                    			 
		                    					
	                    				 if(invalidFlow == true) {
	                    					 System.out.println(" whatt happen to " + dto );
	                    					 
			                    				firstLogoutTime=true; 
			                    					dto.setSchedulingFromDate( dates.get((j-5)/2));
			                    				 
			                    				dto.setLoginTime(loginTime);
			                    				invalidFlow = false;
			                    			} else {
			                    				String loginTime1=getLoginTime(cell);
			                    				if(loginTime.equals(dto.getLoginTime())) {
			                    					sameLogTime=true;
			                    				} else {
			                    					sameLogTime = false;
					                    			adto =new ScheduleAlterDto();
					                    			adto.setLoginTime( loginTime1  );
					                    			System.out.println(" Date Index :" + ( (j-5)/2));
					                    			adto.setDate( dates.get((j-5)/2));
					                    			dto.getAlterList().add(adto);
					                    			
			                    				}
			                    				dto.setScheduledToDate( dates.get((j-5)/2));
				                    			
			                    			}
				                    			
		                    			 
		                    			
		                    			}			
	                    			
	                    		} else if (j%2==0) {
	                    			System.out.println(" in logout ... ");
	                    			if(invalid == false) {
	                    				if(firstLogoutTime) {
	                    					dto.setLogoutTime(getLogoutTime(cell));
	                    					firstLogoutTime = false;
	                    				} else {
	                    					 String logoutTime1 = getLogoutTime(cell);
	                    					 if(sameLogTime  ) {
	                    						 if(dto.getLogoutTime().equals(logoutTime1)==false) {
				                    					sameLogTime = false;
						                    			adto =new ScheduleAlterDto();
						                    			adto.setLoginTime( dto.getLoginTime()  );
						                    			adto.setLogoutTime(logoutTime1);
						                    			adto.setDate( dates.get((j-5)/2));
						                    			dto.getAlterList().add(adto);
						                    			System.out.println("IN : " + adto.getLoginTime()+" OUT :" + adto.getLogoutTime());
	                    						 }
	                    						 
	                    					 } else {
	                    					adto.setLogoutTime(logoutTime1);
	                    					System.out.println("IN : " + adto.getLoginTime()+" OUT :" + adto.getLogoutTime());
	                    					 }
	                    					
	                    				}
	                    			} else {
	                    				System.out.println("INVALID...");
	                    			}
	                    			
	                    		}
	                    		
	                    		
	                    		
	                    	}
	                    	
	                    }
	                }
	        }
	        System.out.println();
	        System.out.println(" ___________________");
	        for(String date : dates) {
	        	System.out.println(" " + date);
	        }
	        System.out.println(" ___________________");
	        for (SchedulingDto sDto : dtoList) {
	        	System.out.println("Emp Name : "+ sDto.getEmployeeId() + "\t EmpName : "+ sDto.getEmployeeName() + "\t Login : " + sDto.getLoginTime() + "\t Logout : " + sDto.getLogoutTime() + "\t From Date :" + sDto.getSchedulingFromDate() + "\t To Date :" + sDto.getScheduledToDate());
	        	System.out.println("Alter list size : "+ sDto.getAlterList().size());
	        	for(ScheduleAlterDto sAdto : sDto.getAlterList()) {
	        		System.out.println("Date : "+ sAdto.getDate() + "\tLogin : "+ sAdto.getLoginTime() + "Logout : "+ sAdto.getLogoutTime() );
	        		 
	        	}
	        }

	    }
	
	private static String getLoginTime( Cell cell) {
		String loginTime=null;
		if(cell.getCellType()==Cell.CELL_TYPE_NUMERIC && DateUtil.isCellDateFormatted(cell)) {
			  System.out.println("login  time : " +cell.getDateCellValue());
				  Date date = cell.getDateCellValue();

			        SimpleDateFormat formatTime = new SimpleDateFormat("HH:mm");
			        String dateStamp = formatTime.format(date);
			        loginTime= dateStamp;

			  //System.out.println(" login time : " + dto.getLoginTime());
			  System.out.println("type : "+ cell.getCellType());
			  try {
				  String split[] = loginTime.split(":");
				  if(split[0].length()<2) {
					  split[0]="0"+split[0];
				  }
				  if(split[1].length()<2) {
					  split[1]="0"+split[0];
				  }
				  loginTime = split[0]+":" + split[1];
			  }catch(Exception e) {
				  if(loginTime ==null||loginTime.trim().equalsIgnoreCase("")||loginTime.trim().equalsIgnoreCase("none")||loginTime.trim().equalsIgnoreCase("nill")||loginTime.trim().equalsIgnoreCase("null")) {
					  loginTime="none";
					} else {
						if(loginTime.equalsIgnoreCase("invalid"))
						{
							
						} else {
						  String time = OtherFunctions.TimeFormat24Hr(loginTime );
						  if(time!=null) {
							  loginTime = time;
						  } else {
		/*					  message=message + "|" + "Login Time :incorrect format (row:" + rowIndex+" )";
							  error=true;
							  */
							  System.out.println(" Error in login format");
							  loginTime="Error";
						  }
						}
					   
					}
			  }
			  } else  {

				  if(cell.getCellType()==Cell.CELL_TYPE_STRING) {   
					    System.out.println("cell type : " + cell.getCellType());
					    loginTime = cell.getStringCellValue();
					  
					  
					  if(loginTime ==null||loginTime.trim().equalsIgnoreCase("")||loginTime.trim().equalsIgnoreCase("none")||loginTime.trim().equalsIgnoreCase("nill")||loginTime .trim().equalsIgnoreCase("null")||loginTime .trim().equalsIgnoreCase("cancel")|| loginTime .trim().equalsIgnoreCase("off" ) ) {
						  loginTime = "none";
						}  
						
						 else {
								if(loginTime.equalsIgnoreCase("invalid"))
								{
									
								} else {
									  String time = OtherFunctions.TimeFormat24Hr(loginTime );
									  if(time!=null) {
										  loginTime  = time;
									  } else {
										  /*message=message + "|" + "Login Time :incorrect format (row:" + rowIndex+" )";
										  error=true;*/
										  System.out.println("Error in login format");
										  loginTime = "error";
									  }
								}
						}
				  } else if(cell.getCellType()==Cell.CELL_TYPE_BLANK) {
					  loginTime  = "none";
				  }
				   
			  }
		return loginTime;
		   

		
	}
	
	//-------------
	
	private static String getLogoutTime( Cell cell) {
		String logoutTime=null;
		if(cell.getCellType()==Cell.CELL_TYPE_NUMERIC && DateUtil.isCellDateFormatted(cell)) {
			  System.out.println("login  time : " +cell.getDateCellValue());
				  Date date = cell.getDateCellValue();

			        SimpleDateFormat formatTime = new SimpleDateFormat("HH:mm");
			        String dateStamp = formatTime.format(date);
			        logoutTime= dateStamp;

			  //System.out.println(" login time : " + dto.getLogoutTime());
			  System.out.println("type : "+ cell.getCellType());
			  try {
				  String split[] = logoutTime.split(":");
				  if(split[0].length()<2) {
					  split[0]="0"+split[0];
				  }
				  if(split[1].length()<2) {
					  split[1]="0"+split[0];
				  }
				  logoutTime = split[0]+":" + split[1];
			  }catch(Exception e) {
				  if(logoutTime ==null||logoutTime.trim().equalsIgnoreCase("")||logoutTime.trim().equalsIgnoreCase("none")||logoutTime.trim().equalsIgnoreCase("nill")||logoutTime.trim().equalsIgnoreCase("null")||logoutTime.trim().equalsIgnoreCase("invalid")) {
					  logoutTime="none";
					} else {
						if(logoutTime.equalsIgnoreCase("invalid"))
						{
							
						} else {
								  String time = OtherFunctions.TimeFormat24Hr(logoutTime );
								  if(time!=null) {
									  logoutTime = time;
								  } else {
				/*					  message=message + "|" + "Login Time :incorrect format (row:" + rowIndex+" )";
									  error=true;
									  */
									  System.out.println(" Error in login format");
									  logoutTime="Error";
								  }
						}	   
					}
			  }
			  } else  {

				  if(cell.getCellType()==Cell.CELL_TYPE_STRING) {   
					    System.out.println("cell type : " + cell.getCellType());
					    logoutTime = cell.getStringCellValue();
					  
					  
					  if(logoutTime ==null||logoutTime.trim().equalsIgnoreCase("")||logoutTime.trim().equalsIgnoreCase("none")||logoutTime.trim().equalsIgnoreCase("nill")||logoutTime .trim().equalsIgnoreCase("null")||logoutTime .trim().equalsIgnoreCase("cancel")|| logoutTime .trim().equalsIgnoreCase("off")||logoutTime .equalsIgnoreCase("invalid") ) {
						  logoutTime = "none";
						}  
						
						 else {
								if(logoutTime.equalsIgnoreCase("invalid"))
								{
									
								} else {
									  String time = OtherFunctions.TimeFormat24Hr(logoutTime );
									  if(time!=null) {
										  logoutTime  = time;
									  } else {
										  /*message=message + "|" + "Login Time :incorrect format (row:" + rowIndex+" )";
										  error=true;*/
										  System.out.println("Error in login format");
										  logoutTime = "error";
									  }
								}
						}
				  } else if(cell.getCellType()==Cell.CELL_TYPE_BLANK) {
					  logoutTime  = "none";
				  }
				   
			  }
		return logoutTime;
		   

		
	}

	//---------
	
	public static void connectionTest() {
		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			
					con = DbConnect.getInstance().connectDB();
				//	System.out.println("........ "+con);
					st =  con.prepareStatement("select e.personnelno,e.displayname, a.area,p.place,l.landmark,es.landmark as landmarkid,count(es.landmark) as empcount from area a,place p,landmark l, employee_subscription es,employee e where a.id=p.area and p.id=l.place and l.id=es.landmark and e.id=es.empid and es.subscriptionstatus in ('subscribed','pending') group by es.empid" );
					System.out.println("PrepareStatement is done ..");
				rs =	st.executeQuery();
				DistanceListDao dao =new DistanceListDao();
				while(rs.next())
				{
				float dist=dao.getGooglMapDistance("2128", rs.getString("landmarkid"));
				System.out.println(rs.getString("personnelno")+"\t"+rs.getString("displayname")+"\t"+rs.getString("area")+"\t"+rs.getString("place")+"\t"+rs.getString("landmark")+"\t"+dist);
				}
				
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);
			
		} catch(Exception e ) { 
			System.out.println("Error in test con: " + e);
		}
	}
	 
	/* public static void main(String[] args) {
		 try{
			 System.out.println("Time : " + OtherFunctions. changeTimeFormat("10:30-PM",  "hh:mm-a", "HH:mm"));
			 //connectionTest();
	 //readExcelFromFile();
			 /*FileInputStream in = new FileInputStream(new File("D:\\CrossDomain\\ScheduleTemplateGeneated.xlsx"));
	    	 	SchedulingService.uploadScheduleHorizoantalFile(in);*/
			 //showNonScheduledEmp();
			// writeExcel();
			 //getdistnce();
			  //test();
			 //new RouteService().orderTheRoute("1");	
			 //System.out.println("aaaaaaaaaaaaaaaaa");
			// new SendMailCrossDomain().send("muhammad@agiledgesolutions.com","Test Mail","This is a testmail");
			// permutation("1234567890");
		//	 getdistnce();
	/*
		 ClientConfig config = new DefaultClientConfig();
	        Client client = Client.create(config);
		         WebResource webResource = client.resource("https://www.peopleworks.ind.in/pwwebApi/api/Logon");
		         //WebResource webResource = client.resource("https://www.peopleworks.ind.in/PWWEbApi/api/LogOn/put");
		        JSONObject js=new JSONObject();
		        //String username="cd\\transport";
		       String username="cd\\tr01";
		       String password="06Yry0dH";
//		       String password="transportATOM!23";
		      //  String username="ps1\\Cd16";
		   //     String password="Passw0rd!";
		 		js.put("ClientCodeSlashName", username);
		 		js.put("Password", password);
		       ClientResponse response = webResource.type(MediaType.APPLICATION_JSON).put(ClientResponse.class, js.toString());
		       NewCookie cookies= response.getCookies().get(0);		       
					JSONObject jso = new JSONObject(response.getEntity(String.class));
					System.out.println(jso.toString());
					JSONArray getArray = jso.getJSONArray("ModuleDetails");		
				//	System.out.println(getArray.toString());
					String sessionId=(String)jso.get("sessionID");
					System.out.println(sessionId);
				//	getDetails(cookies,sessionId);
					
					
					DefaultHttpClient client1 = new DefaultHttpClient();					
				     HttpGet request = new HttpGet("https://www.peopleworks.ind.in/PWWEbApi/Api/EmloyeeDatas/GetEmployeeDetails/"+sessionId);
				     request.addHeader("Cookie", cookies.toString());
				     
				     HttpResponse response1 = client1.execute(request);
				     //System.out.println(response1.getEntity());
				     BufferedReader rd = new BufferedReader (new InputStreamReader(response1.getEntity().getContent()));
				     String line = "";
				     String nLine="";
				     System.out.println(response1.getStatusLine());
				     while ((line = rd.readLine()) != null) {
				    	 nLine+=line;
				        }
				     //System.out.println(nLine);
				    JSONArray jsa=new JSONArray(nLine);
				     System.out.println(jsa.get(1).toString());
				     
				     
				     
				     
				     
					/*
					
					ClientConfig config1 = new DefaultClientConfig();
			        Client client1 = Client.create(config);
				         WebResource webResource1 = client1.resource("https://www.peopleworks.ind.in/pwwebapi/api/logon/GETapi/EmloyeeDatas/GetEmployeeDetails/");
				         //WebResource webResource = client.resource("https://www.peopleworks.ind.in/PWWEbApi/api/LogOn/put");
				        JSONObject js1=new JSONObject();
				 		js.put("id", seesionId);
				       ClientResponse response1 = webResource1.type(MediaType.APPLICATION_JSON).put(ClientResponse.class, js1.toString());
				       //JSONObject jso=new JSONObject(response.getEntity(String.class));
				       System.out.println(response);
						
							System.out.println(response1.getEntity(String.class));
							*/
							//System.out.println(jso1.toString());
					
				  
		//	 new ValidateUser().APIAuthentication();
	/*	 }catch(Exception e)
		 {
			System.out.println("Error"+e); 
		 }
	
}
*/

	public static void getDetails(NewCookie cookies, String sessionId)
			throws ClientHandlerException, UniformInterfaceException,
			JSONException {

		ClientConfig config = new DefaultClientConfig();
		Client client = Client.create(config);
		WebResource webResource = client
				.resource("https://www.peopleworks.ind.in/PWWEbApi/Api/EmloyeeDatas/GetEmployeeDetails/"
						+ sessionId);
		webResource.cookie(cookies);
		// WebResource webResource =
		// client.resource("https://www.peopleworks.ind.in/PWWEbApi/api/LogOn/put");
		ClientResponse response = webResource
				.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
		System.out.println(response.toString());
		JSONObject jso = new JSONObject(response.getEntity(String.class));
		System.out.println(jso.toString());
		// System.out.println(getArray.toString());

	}
	
	public static void cdEmpCheck () {
		
		GetEmps getEmpsObj = GetEmps.getSource();
		System.out.println("g etting emps ");
		ArrayList<EmployeeDto> ActualEmplist = getEmpsObj.getEmps();
		 int i=0;
		 EmployeeDto emplyeedto = null;
		 
		for(EmployeeDto edto : ActualEmplist) {
			/*System.out.print("\t" + edto.getPersonnelNo());
			if((++i)%15==0) {
				System.out.println();
				i=0;
			}*/
			if(edto.getPersonnelNo().trim().equals("4069")) {
				System.out.println("YES ! HE/SHE is here :" + edto.toString());
				emplyeedto = edto;
				break;
			}
		}
		
		String query = "insert into employee(EmployeeFirstName,EmployeeMiddleName,EmployeeLastName,LoginId,Gender,PersonnelNo,EmailAddress,contactNumber1,"
				+ "HomeCountry,LineManager,StaffManager,ClientServiceManager,CareerLevel,CareerPathwayDesc,BusinessUnitCode,BusinessUnitDescription,"
				+ "Deptno,DeptName,OperationCode,OperationDescription,Pathways,Dateofjoining,Active,address,city,isContractEmployee,displayname,projectUnit,designationName,state,site,project) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		query = query.replace("?", "'%s'");
query = String.format(query 		
, emplyeedto.getEmployeeFirstName()
, emplyeedto.getEmployeeMiddleName()
, emplyeedto.getEmployeeLastName()
, emplyeedto.getLoginId()
, emplyeedto.getGender()
, emplyeedto.getPersonnelNo()
, emplyeedto.getEmailAddress()
, emplyeedto.getContactNo()
, emplyeedto.getHomeCountry()
, emplyeedto.getLineManager()
, emplyeedto.getStaffManager()
, emplyeedto.getClientServiceManager()
, emplyeedto.getCareerLevel()
, emplyeedto.getCareerPathwayDesc()
, emplyeedto.getBusinessUnitCode()
, emplyeedto.getBusinessUnitDescription()
, emplyeedto.getDeptno()
, emplyeedto.getDeptName()
, emplyeedto.getOperationCode()
, emplyeedto.getOperationDescription()
, emplyeedto.getPathways()
, emplyeedto.getDateOfJoining()
, "1"
, emplyeedto.getAddress()
, emplyeedto.getCity()
, "on Roll"
, emplyeedto.getDisplayName()
, emplyeedto.getProjectUnit()
, emplyeedto.getDesignationName()
, emplyeedto.getState()
, "1" 
, emplyeedto.getProjectid());
System.out.println("Query : "+ query);
	}

	public static void type3Method(String sessionId) {
		URL url;
		try {
			System.out.println(sessionId);
			url = new URL(
					"https://www.peopleworks.ind.in/PWWEbApi/Api/EmloyeeDatas/GetEmployeeDetails/"
							+ sessionId);

			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : "
						+ conn.getResponseCode());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void type2Method(String sessionId) {
		try {
			ClientConfig config2 = new DefaultClientConfig();
			Client client2 = Client.create(config2);
			WebResource service2 = client2.resource(UriBuilder.fromUri(
					"https://www.peopleworks.ind.in/PWWEbApi/Api/EmloyeeDatas/GetEmployeeDetails/"
							+ sessionId).build());
			// getting XML data
			System.out.println(service2.accept(MediaType.APPLICATION_JSON).get(
					String.class));
			// getting JSON data
			System.out.println(service2.accept(MediaType.APPLICATION_XML).get(
					String.class));
		} catch (Exception e) {
			System.out.println("Error" + e);
		}
	}

	public static void APIAuthentication() {
		try {
			ClientConfig config = new DefaultClientConfig();
			Client client = Client.create(config);
			WebResource webResource = client
					.resource("https://www.peopleworks.ind.in/pwwebApi/api/Logon");
			JSONObject js = new JSONObject();
			String username = "cd\\tr01";
			String password = "06Yry0dH";
			js.put("ClientCodeSlashName", username);
			js.put("Password", password);
			ClientResponse response = webResource.type(
					MediaType.APPLICATION_JSON).put(ClientResponse.class,
					js.toString());
			System.out.println("AAAAAAAAAAAA"+response.toString());
			if (response.getStatus() == 200) {
				JSONObject jso = new JSONObject(
						response.getEntity(String.class));
				String uid = (String) jso.get("EmployeeID");
				System.out.println("True"+uid);
			}
		} catch (Exception e) {
			System.out.println("Error" + e);
		}


			
			
			
			
			
			
			
			
			
			
			
			

	}

	public static void permutation(String str) {
		permutation("", str);
	}


	/*
	 * 
	 * public static void main(String[] args) throws Exception { ClientConfig
	 * config = new DefaultClientConfig(); Client client =
	 * Client.create(config); WebResource service =
	 * client.resource(UriBuilder.fromUri
	 * ("http://www.example.com/customers/12345").build()); // getting XML data
	 * System
	 * .out.println(service.type(MediaType.APPLICATION_JSON).accept(MediaType
	 * .APPLICATION_JSON).get(String.class)); // getting JSON data
	 * //System.out.println(service.
	 * path("restPath").path("resourcePath").accept
	 * (MediaType.APPLICATION_XML).get(String.class)); }
	 */
	/*
	public static void getdistnce() throws ClientProtocolException,
			IOException, JSONException {
		DefaultHttpClient client1 = new DefaultHttpClient();
		// HttpGet request = new
		// HttpGet("http://maps.googleapis.com/maps/api/directions/json?origin=12.94918897,77.71295071&destination=12.9537582,77.61893928");
		HttpGet request = new HttpGet(
				//"http://maps.googleapis.com/maps/api/distancematrix/json?origin=Vancouver+BC&destination=San+Francisco");
				"http://maps.googleapis.com/maps/api/distancematrix/json?origins=13.1096574695688,77.5795269012451&destinations=13.05765667,77.5929594"); // &waypoints=12.9199732126374,77.5924015045166" + URLEncoder.encode("|", "UTF-8") + "12.982807943166,77.5625002384186" + URLEncoder.encode("|", "UTF-8") + "12.95475149,77.68829584");
		
		HttpResponse response1 = client1.execute(request);
		// System.out.println(response1.getEntity());
		BufferedReader rd = new BufferedReader(new InputStreamReader(response1
				.getEntity().getContent()));
		String line = "";
		String nLine = "";
		System.out.println(response1.getStatusLine());
		while ((line = rd.readLine()) != null) {
			nLine += line;
		}
		System.out.println(nLine);
		JSONObject obj = new JSONObject(nLine);
		obj=(JSONObject) (obj.getJSONArray("rows")).get(0);
		obj =(JSONObject) (obj.getJSONArray("elements")).get(0);
		obj=(JSONObject) obj.get("distance");
		int distanceM =(int) obj.get("value");	
		float dist=(float)distanceM/1000;
		System.out.println(dist);
		
		/*
		 * routes : [0].legs[0].distance.value = meter routes :
		 * [0].legs[0].duration.value= 1611 second routes :
		 * [0].legs[0].start_location.lat= 12.9963345 routes :
		 * [0].legs[0].start_location.lng= 77.5646637 routes :
		 * [0].legs[0].end_location.lat= 12.9201671 routes :
		 * [0].legs[0].end_location.lng= 77.5646637
		 */
		//JSONArray jsa = new JSONArray(nLine);
		//System.out.println(jsa.get(3));
		/*
		String distance = ((JSONObject) ((JSONObject) ((JSONObject) obj
				.getJSONArray("routes").get(0)).getJSONArray("legs").get(0))
				.get("distance")).get("value").toString();
		System.out.println("Distance : " + distance);
		int legs = ((JSONObject) obj.getJSONArray("routes").get(0))
				.getJSONArray("legs").length();
		for (int i = 0; i < legs; i++) {
			JSONObject leg = ((JSONObject) obj.getJSONArray("routes").get(0))
					.getJSONArray("legs").getJSONObject(0);
			System.out.println("Distance : "
					+ leg.getJSONObject("distance").getDouble("value"));
			System.out.println("Duration : "
					+ leg.getJSONObject("duration").getDouble("value"));
			System.out.print("Start Location lat/lng : "
					+ leg.getJSONObject("start_location").getDouble("lat"));
			System.out.println(","
					+ leg.getJSONObject("start_location").getDouble("lng"));
			System.out.println("End Location lat/lng : "
					+ leg.getJSONObject("end_location").getDouble("lat"));
			System.out.println(","
					+ leg.getJSONObject("end_location").getDouble("lng"));
					
		}

	}*/

			private static void permutation(String prefix, String str) {
			    int n = str.length();
			    if (n == 0) System.out.println(prefix);
			    else {
			        for (int i = 0; i < n; i++)
			            permutation(prefix + str.charAt(i), str.substring(0, i) + str.substring(i+1, n));
			    }
			}	 
			
/*
	
	public static void main(String[] args) throws Exception {
        ClientConfig config = new DefaultClientConfig();
        Client client = Client.create(config);
        WebResource service = client.resource(UriBuilder.fromUri("http://www.example.com/customers/12345").build());
        // getting XML data
        System.out.println(service.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).get(String.class));
        // getting JSON data
        //System.out.println(service. path("restPath").path("resourcePath").accept(MediaType.APPLICATION_XML).get(String.class));
    }
*/    
			public static void getdistnce() throws ClientProtocolException, IOException, JSONException
			{
				DefaultHttpClient client1 = new DefaultHttpClient();					
			     //HttpGet request = new HttpGet("http://maps.googleapis.com/maps/api/directions/json?origin=12.94918897,77.71295071&destination=12.9537582,77.61893928");
				String req="http://maps.googleapis.com/maps/api/directions/json?origin=12.8959675171397,77.6353114843368&destination=12.89155934,77.63820559";
				String enckey=OtherFunctions.encryptTheMapKey(req);
				HttpGet request = new HttpGet(enckey);
			     HttpResponse response1 = 
			    		 client1.execute(request);
			     //System.out.println(response1.getEntity());
			     BufferedReader rd = new BufferedReader (new InputStreamReader(response1.getEntity().getContent()));
			     String line = "";
			     String nLine="";
			     System.out.println(response1.getStatusLine());
			     while ((line = rd.readLine()) != null) {
			    	 nLine+=line;
			        }
			     System.out.println(nLine);
			     JSONObject obj = new JSONObject(nLine);
			 /*    		 routes : [0].legs[0].distance.value = meter
			    		 routes : [0].legs[0].duration.value= 1611 second
			    		 routes : [0].legs[0].start_location.lat= 12.9963345
			    		 routes : [0].legs[0].start_location.lng= 77.5646637
			    		 routes : [0].legs[0].end_location.lat= 12.9201671
			    		 routes : [0].legs[0].end_location.lng= 77.5646637
			 */      
			  String distance = ((JSONObject) ((JSONObject)  ((JSONObject) obj.getJSONArray("routes").get(0)).getJSONArray("legs").get(0)).get("distance")).get("value").toString();
			  System.out.println("Distance : "+ distance);
			  int legs = ((JSONObject) obj.getJSONArray("routes").get(0)).getJSONArray("legs").length();
			  for(int i = 0 ; i < legs ; i ++) {
				  JSONObject leg = ((JSONObject) obj.getJSONArray("routes").get(0)).getJSONArray("legs").getJSONObject(0);
				  System.out.println( "Distance : " + leg.getJSONObject("distance").getDouble("value"));
				  System.out.println( "Duration : " + leg.getJSONObject("duration").getDouble("value"));
				  System.out.print( "Start Location lat/lng : " + leg.getJSONObject("start_location").getDouble("lat"));
				  System.out.println( "," + leg.getJSONObject("start_location").getDouble("lng"));
				  System.out.println( "End Location lat/lng : " + leg.getJSONObject("end_location").getDouble("lat"));
				  System.out.println( "," + leg.getJSONObject("end_location").getDouble("lng"));
			  }
			     
			    	
			}
			
			
			/**
			 * @param dtoList
			 */
			public static void writeSchedulesToExcel (ArrayList<SchedulingDto> dtoList) {
				
				//HSSFWorkbook workbook = new HSSFWorkbook();
				XSSFWorkbook workbook = new XSSFWorkbook();
				//HSSFSheet sheet = workbook.createSheet();
				XSSFSheet sheet = workbook.createSheet();
				
				
				int rownum=0;
				if(dtoList!=null && dtoList.size() >0) {
					SchedulingDto dto = dtoList.get(rownum);
					int colnum =0;
					//HSSFRow headerRow = sheet.createRow(rownum);
					XSSFRow headerRow = sheet.createRow(rownum);
					 
					headerRow.setRowStyle(workbook.createCellStyle() );
					
					//headerRow.getRowStyle().setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
					//headerRow.getRowStyle().setFillBackgroundColor(HSSFColor.YELLOW.index);
					headerRow.getRowStyle().setAlignment(HSSFCellStyle.ALIGN_CENTER);
//			 		HSSFFont font = workbook.createFont();
			 		XSSFFont font = workbook.createFont();
					font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
					headerRow.getRowStyle().setFont(font); 
					
//					sheet.addMergedRegion(new CellRangeAddress(rownum,rownum+1,colnum,colnum));
					sheet.addMergedRegion( new org.apache.poi.ss.util.CellRangeAddress(rownum,rownum+1,colnum,colnum));
//					HSSFCell cell1  = headerRow.createCell(colnum++);
					XSSFCell cell1  = headerRow.createCell(colnum++);
					
					cell1.setCellStyle(headerRow.getRowStyle() );
					cell1.setCellValue ("Emp#");
//					sheet.addMergedRegion(new CellRangeAddress(rownum,rownum+1,colnum,colnum));
					sheet.addMergedRegion( new org.apache.poi.ss.util.CellRangeAddress(rownum,rownum+1,colnum,colnum));
//					HSSFCell cell2  = headerRow.createCell(colnum++);
					XSSFCell cell2  = headerRow.createCell(colnum++);
					
					cell2.setCellValue ("DisplayName");
					cell2.setCellStyle(headerRow.getRowStyle() );
//					sheet.addMergedRegion(new CellRangeAddress(rownum,rownum+1,colnum,colnum));
					sheet.addMergedRegion(new org.apache.poi.ss.util.CellRangeAddress(rownum,rownum+1,colnum,colnum ));
					
//					HSSFCell cell3  = headerRow.createCell(colnum++);
					XSSFCell cell3  = headerRow.createCell(colnum++);
					
					cell3.setCellStyle(headerRow.getRowStyle() );
					cell3.setCellValue ("Project");
//					sheet.addMergedRegion(new CellRangeAddress(rownum,rownum+1,colnum,colnum));
					sheet.addMergedRegion( new org.apache.poi.ss.util.CellRangeAddress(rownum,rownum+1,colnum,colnum));
					
//					HSSFCell cell4  = headerRow.createCell(colnum++);
					XSSFCell cell4 = headerRow.createCell(colnum++);
					
					cell4.setCellStyle(headerRow.getRowStyle() );
					cell4.setCellValue ("Subscription Effective Date");
					
//					sheet.addMergedRegion(new CellRangeAddress(rownum,rownum+1,colnum,colnum));
					sheet.addMergedRegion(new org.apache.poi.ss.util.CellRangeAddress(rownum,rownum+1,colnum,colnum ));
//					HSSFCell cell5  = headerRow.createCell(colnum++);
					XSSFCell cell5  = headerRow.createCell(colnum++);
					 
					cell5.setCellStyle(headerRow.getRowStyle() );
					cell5.setCellValue ("End Date");
					 
			//System.out.println(dto.getAlterList().size() + " : size of first alter list ");
					
					for(ScheduleAlterDto adto : dto.getAlterList()) {
						
						
//						HSSFDataFormat dataFormat = workbook.createDataFormat();
						XSSFDataFormat dataFormat = workbook.createDataFormat();
//						HSSFCell cellDate  = headerRow.createCell(colnum++);
						XSSFCell cellDate  = headerRow.createCell(colnum++);
//						HSSFCellStyle cellStyle= workbook.createCellStyle();
						XSSFCellStyle cellStyle= workbook.createCellStyle();
						
						cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
						cellStyle.setDataFormat(dataFormat.getFormat("yyyy-mm-dd"));
						cellStyle.setFont(font);
						//cellStyle.setFillBackgroundColor(HSSFColor.YELLOW.index);
					//	cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
						
						cellDate.setCellValue ( OtherFunctions.changeDateFromat_ddmmyyy_to_UtilDate( OtherFunctions.changeDateFromat( adto.getDate())));
						 
						//sheet.addMergedRegion(new CellRangeAddress(rownum,rownum,colnum-1,colnum));
						sheet.addMergedRegion(new org.apache.poi.ss.util.CellRangeAddress(rownum,rownum,colnum-1,colnum));
						colnum++;
						//cellDate.setCellStyle(headerRow.getRowStyle() );
						cellDate.setCellStyle(cellStyle);
						cellDate.setCellType(HSSFCell.CELL_TYPE_NUMERIC );
						
					}
					 
					rownum++;
					colnum=5;
//					 HSSFRow headerRow1 = sheet.createRow(rownum);
					XSSFRow headerRow1 = sheet.createRow(rownum);
					 headerRow1.setRowStyle( headerRow.getRowStyle());
					 for(ScheduleAlterDto adto : dto.getAlterList()) {
					
//						HSSFCell cell11  = headerRow1.createCell(colnum++);
						 XSSFCell cell11  = headerRow1.createCell(colnum++);
	  					cell11.setCellStyle(headerRow1.getRowStyle() );
						cell11.setCellValue ("Login");
						
//						HSSFCell cell12 = headerRow1.createCell(colnum++);
						XSSFCell cell12 = headerRow1.createCell(colnum++);
						cell12.setCellStyle(headerRow1.getRowStyle() );
						cell12.setCellValue ("Logout");
					
					}
					 
					 for(SchedulingDto sdto : dtoList) {
						 rownum++;
//						 HSSFRow dataRow = sheet.createRow(rownum);
						 XSSFRow dataRow = sheet.createRow(rownum);
						 colnum=0;
//						 HSSFCell dataCellEmployeeId = dataRow.createCell(colnum++);
						 XSSFCell dataCellEmployeeId = dataRow.createCell(colnum++);
						 dataCellEmployeeId.setCellValue(sdto.getEmployeeId());
						 
//						 HSSFCell dataCellEmployeeName = dataRow.createCell(colnum++);
						 XSSFCell dataCellEmployeeName = dataRow.createCell(colnum++);
						 dataCellEmployeeName.setCellValue(sdto.getEmployeeName() );
						 
//						 HSSFCell dataCellProject = dataRow.createCell(colnum++);
						 XSSFCell dataCellProject = dataRow.createCell(colnum++);
						 dataCellProject.setCellValue(sdto.getProject() );
						 
//						 HSSFCell dataCellSubscriptionEffectiveDate = dataRow.createCell(colnum++);
						 XSSFCell dataCellSubscriptionEffectiveDate = dataRow.createCell(colnum++);
						 dataCellSubscriptionEffectiveDate.setCellValue(sdto.getSubscriptionDate() );
						 
//						 HSSFCell dataCellSubscriptionToDate = dataRow.createCell(colnum++);
						 XSSFCell dataCellSubscriptionToDate = dataRow.createCell(colnum++);
						 dataCellSubscriptionToDate.setCellValue(sdto.getSubscriptionToDate() );
						 
				//		 System.out.print(   dto.getEmployeeId() +"\t" + dto.getEmployeeName() +"\t" );
						 for(ScheduleAlterDto adto:  sdto.getAlterList()) {
				//			 System.out.print(OtherFunctions.changeDateFromat( "  "+ adto.getDate()) +"\t" + adto.getLogoutTime() +"\t" + adto.getLogoutTime()  +"\t");
//							 HSSFDataFormat dataFormat = workbook.createDataFormat();
							 XSSFDataFormat dataFormat = workbook.createDataFormat();
//							 HSSFCellStyle cellStyle= workbook.createCellStyle();
							 XSSFCellStyle cellStyle= workbook.createCellStyle();
							 cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
							 cellStyle.setDataFormat(dataFormat.getFormat("[h]:mm:ss;@"));
							  
							 
//							 HSSFCell dataCell = dataRow.createCell(colnum++);
							 XSSFCell dataCell = dataRow.createCell(colnum++);
							 dataCell.setCellValue(adto.getLogoutTime());
					//		 System.out.println( sdto.getEmployeeName() + " (" + rownum + ":" + colnum + ")......AAA.........." + adto.getLogoutTime() + " " + adto.getLogoutTime()); 
							  if(adto.getLogoutTime().equalsIgnoreCase("INVALID") ) {
								  
								 //CellRangeAddressList addressList = new CellRangeAddressList(rownum, rownum, colnum-1, colnum-1);
								   org.apache.poi.ss.util.CellRangeAddressList addressList = new org.apache.poi.ss.util.CellRangeAddressList(rownum, rownum, colnum-1, colnum-1); 
								// DVConstraint dvConstraint =  DVConstraint.createExplicitListConstraint( new String[] {""});
								   //CTDataValidation = CTDataValidationImpl
								 CellReference cr =  new  CellReference(dataCell.getRowIndex(), dataCell.getColumnIndex());
								 //System.out.println(cr.toString());
								 
								 //DVConstraint dvConstraint =  DVConstraint.createCustomFormulaConstraint("(" + cr.formatAsString()  +"='l'");
								 
//								 HSSFDataValidation dv = new HSSFDataValidation( addressList, dvConstraint);
								 //XSSFDataValidation dv = new XSSFDataValidation( addressList, dvConstraint  );
								// sheet.addValidationData(dv);
							 }
							   
					 		  
//							 HSSFCell dataCel2 = dataRow.createCell(colnum++);
							  XSSFCell dataCel2 = dataRow.createCell(colnum++);
							 dataCel2.setCellValue(adto.getLogoutTime());
							 

					 		 if(adto.getLogoutTime().equalsIgnoreCase("INVALID") ) {
								  
								/* CellRangeAddressList addressList = new CellRangeAddressList(rownum, rownum, colnum-1, colnum-1);
								 DVConstraint dvConstraint =  DVConstraint.createExplicitListConstraint( new String[] {""});
								 HSSFDataValidation dv = new HSSFDataValidation( addressList, dvConstraint);
								 sheet.addValidationData(dv);*/
							 }
					 	 }
					//	 System.err.println();
					 }
					 
					try   
			        {  
			            FileOutputStream out =  
			            new FileOutputStream  
			                (new File("D:\\CrossDomain\\ScheduleTemplateGeneated.xlsx"));  
			            workbook.write(out);  
			            out.close();  
			            System.out.println  
			                ("Excel written successfully..");
			            
			            System.out.println("Reading begins..");
			            FileInputStream in = new FileInputStream(new File("D:\\CrossDomain\\ScheduleTemplateGeneated.xlsx"));
			    	// 	SchedulingService.uploadScheduleHorizoantalFile(in);
			        }   
			        catch (FileNotFoundException e)   
			        {  
			            e.printStackTrace();  
			        }   
			        catch (IOException e)   
			        {  
			            e.printStackTrace();  
			        }  
			       

				
					 	
					
				}
				
			}

			public static void writeExcel() {
				    //     HSSFWorkbook workbook = new HSSFWorkbook();
				XSSFWorkbook workbook = new XSSFWorkbook();
				
//				        HSSFSheet sheet = workbook.createSheet("Sample sheet");
				XSSFSheet sheet = workbook.createSheet("sample sheet");
				        TreeMap<String, Object[]> data =   
				            new TreeMap<String, Object[]>();  
				        data.put("1", new Object[]   
				            {"Group.", "Name", "Salary"});
				        
				        data.put("2", new Object[]   
				            {1d, "shalabh", 1500000.00d});
				        data.put("3", new Object[]   
					            {1d, "shalabh", 1500000.00d});
				        data.put("4", new Object[]   
				            {2d, "yutika", 800000.00d});
				        data.put("5", new Object[]   
					            {2d, "yutika", 800000.00d});
				        data.put("6", new Object[]   
				            {3d, "john", 700000.09d});
				        data.put("7", new Object[]   
					            {3d, "john", 700000.09d});
				        data.put("8", new Object[]  
				            {4d, "harry", 788884d});
				        data.put("9", new Object[]  
					            {4d, "harry", 788884d});
				        Set<String> keyset = data.keySet();  
				        int rownum = 0;
				       
				        for (String key : keyset)   
				        {   
				         //   HSSFRow row = sheet.createRow(rownum++);
				        	   XSSFRow row = sheet.createRow(rownum++);
				            Object [] objArr = data.get(key);  
				            int cellnum = 0; 
				            int even =0;
				            for (Object obj : objArr)   
				            {  
				            	 
				            	if((rownum)%2==0 && cellnum==0 & rownum > 1) {
				            		// merge first cell with next row
				            		//HSSFCell cell = row.createCell(cellnum++);
				            		XSSFCell cell = row.createCell(cellnum++);
				            		 
				            		if(obj instanceof Date)  
						                cell.setCellValue((Date)obj);  
						                else if(obj instanceof Boolean)  
						                cell.setCellValue((Boolean)obj);  
						                else if(obj instanceof String)  
						                cell.setCellValue((String)obj);  
						                else if(obj instanceof Double)  
						                cell.setCellValue((Double)obj);
				            		// sheet.addMergedRegion(new CellRangeAddress(rownum-1, rownum, cellnum-1, cellnum-1));
				            		sheet.addMergedRegion( new org.apache.poi.ss.util.CellRangeAddress( rownum-1, rownum, cellnum-1, cellnum-1) );
				            		  
				            		
				            	} else {
				               // HSSFCell cell = row.createCell(cellnum++);
				                XSSFCell cell  = row.createCell(cellnum);
				 
				                if(obj instanceof Date)  
				                cell.setCellValue((Date)obj);  
				                else if(obj instanceof Boolean)  
				                cell.setCellValue((Boolean)obj);  
				                else if(obj instanceof String)  
				                cell.setCellValue((String)obj);  
				                else if(obj instanceof Double)  
				                cell.setCellValue((Double)obj);
				                
				            	}
				            }  
				        }  
				        try   
				        {  
				            FileOutputStream out =  
				            new FileOutputStream  
				                (new File("D:\\CrossDomain\\First.xls"));  
				            workbook.write(out);  
				            out.close();  
				            System.out.println  
				                ("Excel written successfully..");  
				        }   
				        catch (FileNotFoundException e)   
				        {  
				            e.printStackTrace();  
				        }   
				        catch (IOException e)   
				        {  
				            e.printStackTrace();  
				        }  
				       
				
			}

			public static void acccessCheck()
{
				try{
	ClientConfig config = new DefaultClientConfig();
	Client client = Client.create(config);
	WebResource webResource = client
			.resource("https://www.peopleworks.ind.in/pwwebApi/api/Logon");
	// WebResource webResource =
	// client.resource("https://www.peopleworks.ind.in/PWWEbApi/api/LogOn/put");
	JSONObject js = new JSONObject();
	// String username="cd\\transport";
	String username = "cd\\tr01";
	String password = "06Yry0dH";
	// String password="transportATOM!23";
	// String username="ps1\\Cd16";
	// String password="Passw0rd!";
	js.put("ClientCodeSlashName", username);
	js.put("Password", password);
	ClientResponse response = webResource.type(
			MediaType.APPLICATION_JSON).put(ClientResponse.class,
			js.toString());
	System.out.println(response.toString());
	NewCookie cookies = response.getCookies().get(0);
	JSONObject jso = new JSONObject(response.getEntity(String.class));
	System.out.println(jso.toString());
	JSONArray getArray = jso.getJSONArray("ModuleDetails");
	// System.out.println(getArray.toString());
	String sessionId = (String) jso.get("sessionID");
	System.out.println(sessionId);
				}catch(Exception e)
				{
				System.out.println("erro in ac"+e);	
				}

}
			 
}