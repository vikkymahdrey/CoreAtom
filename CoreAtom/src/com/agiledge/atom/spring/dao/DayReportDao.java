package com.agiledge.atom.spring.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;

import com.agiledge.atom.commons.OtherFunctions;
import com.agiledge.atom.dto.DayReportDto;
import com.agiledge.atom.dto.ScheduledEmpDto;

public class DayReportDao {
	private JdbcTemplate jdbcTemplate;  
	  
	public void setDataSource(DataSource dataSource) {  
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	

	public DayReportDto getDayReport(String site, String date) {
		DayReportDto dayReport=new DayReportDto();
		String sqlDate=OtherFunctions.changeDateFromatToIso(date);	
		List<ScheduledEmpDto> scheduleDtoList=jdbcTemplate.query("select e.displayname ,e.personnelno,ifnull(esa.login_time,es.login_time) as loginTime ,ifnull(esa.logout_time,es.logout_time) logoutTime from employee e,employee_subscription esub left join employee_schedule es on  esub.subscriptionid=es.subscription_id and es.from_date <='"+sqlDate+"' and es.to_date>='"+sqlDate+"' left join employee_schedule_alter esa on es.id=esa.scheduleid and esa.date='"+sqlDate+"' where e.site="+site+" and e.id=esub.empid  and esub.fromDate<'"+sqlDate+"' and esub.Subscriptionstatus in ('subscribed','pending') ",new ResultSetExtractor<List<ScheduledEmpDto>>(){  
	    @Override  
	     public List<ScheduledEmpDto> extractData(ResultSet rs) throws SQLException,  
	            DataAccessException {  
	      
	        List<ScheduledEmpDto> scheduleDtoList=new ArrayList<ScheduledEmpDto>();  
	        while(rs.next()){  
	        	ScheduledEmpDto dto=new ScheduledEmpDto();  
	        	dto.setEmployeeName(rs.getString("displayname"));
				dto.setEmployeePersonnelNo(rs.getString("personnelno"));
				dto.setLoginTime(rs.getString("loginTime"));
				dto.setLogoutTime(rs.getString("logoutTime"));
				scheduleDtoList.add(dto);  	          
	        }  
	        return scheduleDtoList;  
	        }  
	    });  
		dayReport.setSite(site);
		dayReport.setDate(date);
		dayReport.setEmpScheduleList((ArrayList<ScheduledEmpDto>) scheduleDtoList);
		return dayReport;
}
	
	/*
public DayReportDto getDayReport(String site, String date) {
		
		String query="";
	DbConnect dbcon=	DbConnect.getInstance();
	Connection con=dbcon.connectDB();
	PreparedStatement pst=null;
	ResultSet rs=null;
	DayReportDto dayReport=new DayReportDto();
		try{
		String sqlDate=OtherFunctions.changeDateFromatToIso(date);	
			query="select e.displayname,e.personnelno,ifnull(esa.login_time,es.login_time) as loginTime ,ifnull(esa.logout_time,es.logout_time) logoutTime from employee e,employee_subscription esub left join employee_schedule es on  esub.subscriptionid=es.subscription_id and es.from_date <='"+sqlDate+"' and es.to_date>='"+sqlDate+"' left join employee_schedule_alter esa on es.id=esa.scheduleid and esa.date='"+sqlDate+"' where e.site="+site+" and e.id=esub.empid  and esub.fromDate<'"+sqlDate+"' and esub.Subscriptionstatus in ('subscribed','pending') ";
			System.out.println(query);
			pst=con.prepareStatement(query);
			rs=pst.executeQuery();
			
			ArrayList<ScheduledEmpDto> scheduleDtoLost=new ArrayList<ScheduledEmpDto>();
			
			while(rs.next())
			{
			ScheduledEmpDto dto=new ScheduledEmpDto();
			dto.setEmployeeName(rs.getString("displayname"));
			dto.setEmployeePersonnelNo(rs.getString("personnelno"));
			dto.setLoginTime(rs.getString("loginTime"));
			dto.setLogoutTime(rs.getString("logoutTime"));
			scheduleDtoLost.add(dto);
			}
			dayReport.setSite(site);
			dayReport.setDate(date);
			dayReport.setEmpScheduleList(scheduleDtoLost);
		}catch(Exception e)
		{
		System.out.println("Error day report Dao "+e);	
		}
		finally
		{
		DbConnect.closeResultSet(rs);
		DbConnect.closeStatement(pst);
		DbConnect.closeConnection(con);
		}
		return dayReport;
	}
	*/

}
