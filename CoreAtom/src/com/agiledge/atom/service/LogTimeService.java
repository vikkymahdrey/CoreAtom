package com.agiledge.atom.service;

import java.util.ArrayList;

import com.agiledge.atom.dao.LogTimeDao;
import com.agiledge.atom.dto.LogTimeDto;


public class LogTimeService {
	LogTimeDao dao = new LogTimeDao();

	public int deleteLogTime(String id,String status,String doneBy) {

		return dao.deleteLogTime(id,status,doneBy);
	}
	public ArrayList <LogTimeDto> getAllTimeWithProjectSpecificForEmployee(String employeeId,String logType)
	{
		return new LogTimeDao().getAllTimeWithProjectSpecificForEmployee(employeeId, logType);
	}
	public String getAllTimeWithProjectSpecific_String(String projectId,String siteId) {
		
		ArrayList <LogTimeDto>timeIn = new ArrayList<LogTimeDto>();
		ArrayList <LogTimeDto>timeOut = new ArrayList<LogTimeDto>();
		timeIn=new LogTimeDao().getAllTimeWithProjectSpecific(projectId,"IN",siteId);
		timeOut=new LogTimeDao().getAllTimeWithProjectSpecific(projectId, "OUT",siteId);
		String times1="";
		String times2="";
		for(LogTimeDto dto: timeIn)
		{
			times1+= "&" +dto.getLogTime()  ;
		}
		for(LogTimeDto dto: timeOut)
		{
			System.out.println(".....");
			times2+="&" +dto.getLogTime() ;
		}
	//	times1=times1.replaceFirst("&","");
	//	times2=times2.replaceFirst("&","");
		return times1 + "_" + times2;
	}
	
	public ArrayList <LogTimeDto>  getAllTimeWithProjectSpecific(String projectId, String type,String siteId)
	{
		
		return new LogTimeDao().getAllTimeWithProjectSpecific(projectId,type,siteId);
	}
	public ArrayList <LogTimeDto>  getProjectSpecificTime(String projectId, String type)
	{
		ArrayList <LogTimeDto>time = new LogTimeDao().getProjectSpecificTime(projectId,type);
		return time;
	}
	
	public ArrayList <LogTimeDto> getAllGeneralLogtime(String log,String site)
	{
		return dao.getAllGeneralLogtime(log,site);
	}
	public ArrayList<LogTimeDto> getAllLogtime(String log) {
		
		return dao.getAllLogtime(log);
	}
public ArrayList<LogTimeDto> getAllLogtime(String log,String site) {
		
		return dao.getAllLogtime(log,site);
	}
	
public ArrayList<LogTimeDto> getAllTime()
{
	return dao.getAllTime();
}

}
