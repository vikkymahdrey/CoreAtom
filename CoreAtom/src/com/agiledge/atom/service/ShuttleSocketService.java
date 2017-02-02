package com.agiledge.atom.service;

import java.util.ArrayList;
import java.util.List;

import com.agiledge.atom.dao.ShuttleSocketDao;
import com.agiledge.atom.dto.EmployeeDto;
import com.agiledge.atom.dto.GeoTagDto;

public class ShuttleSocketService {
	public int setEmployeeInOut(String site,String[] empid, String inTime, String outTime,String route,String landmark, String routeOut, String landmarkOut){
		return new ShuttleSocketDao().setEmployeeInOut(site,empid,inTime,outTime,route,landmark,routeOut,landmarkOut);
	}
	public ArrayList<EmployeeDto> getEmployeeTrippingDetails(String site){
		return new ShuttleSocketDao().getEmployeeTrippingDetails(site);
	}
	public ArrayList<EmployeeDto> getEmployeeDetails(String[] empids){
		return new ShuttleSocketDao().getEmployeeDetails(empids);
	}
	public int setShuttlePickUpDrop(EmployeeDto dto) {
		return new ShuttleSocketDao().setShuttlePickUpDrop(dto);
	}
	public EmployeeDto getShuttlePickUpDrop(String empid) {
		return new ShuttleSocketDao().getShuttlePickUpDrop(empid);
	}
	public int removeShuttlePickUpDrop(EmployeeDto dto) {
		// TODO Auto-generated method stub
		return new ShuttleSocketDao().removeShuttlePickUpDrop(dto);
	}
	public List<GeoTagDto> getemployeeGeoTagDetails(){
		return new ShuttleSocketDao().getemployeeGeoTagDetails();
	}
	public List<GeoTagDto> getemployeeGeoTagDetails1(int routeId,String logtype,String time){
		return new ShuttleSocketDao().getemployeeGeoTagDetails1(routeId,logtype,time);
	}
	public List<GeoTagDto> employeeValueMatchWithNoodle(int routeId,String logtype,String time,String compareBy){
		return new ShuttleSocketDao().employeeValueMatchWithNoodle(routeId,logtype,time,compareBy);
	}
	public List<GeoTagDto> employeeValueDifrrWthNoodle(int routeId,String logtype,String time,String compareBy){
		return new ShuttleSocketDao().employeeValueDifrrWthNoodle(routeId,logtype,time,compareBy);
	}
	public ArrayList<GeoTagDto> getEmployeeNearNoodlePoints(String[] points,String distanceConst,String[] route) {
		// TODO Auto-generated method stub
		return new ShuttleSocketDao().getEmployeeNearNoodlePoints(points,distanceConst,route);
	}
	public ArrayList<GeoTagDto> getEmployeeNearNoodlePointsWithLog(
			String[] points, String distanceConst, String[] routes, String logtype, String time) {
		// TODO Auto-generated method stub
		return new ShuttleSocketDao().getEmployeeNearNoodlePointsWithLog(points,distanceConst,routes,logtype,time);
	}
	public ArrayList<GeoTagDto> getEmployeeNearNoodlePointsForShuttle(
			String[] points, String distanceConst, String[] routes,
			String logtype, String time) {
		// TODO Auto-generated method stub
		return new ShuttleSocketDao().getEmployeeNearNoodlePointsForShuttle(points,distanceConst,routes,logtype,time);
	}
	public ArrayList<GeoTagDto> getEmployeeNearNoodlePointsWithLog2(
			String[] points, String distanceConst, String[] routes,
			String logtype, String time, String filter,String siteid) {
		// TODO Auto-generated method stub
		return new ShuttleSocketDao().getEmployeeNearNoodlePointsWithLog2(points,distanceConst,routes,logtype,time,filter,siteid);
	}
}
