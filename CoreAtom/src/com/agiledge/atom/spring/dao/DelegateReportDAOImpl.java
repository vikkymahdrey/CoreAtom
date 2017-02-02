package com.agiledge.atom.spring.dao;


import java.text.SimpleDateFormat;
import java.util.List;
import java.text.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.agiledge.atom.commons.OtherFunctions;
import com.agiledge.atom.reports.dto.DelegationReportDto;
@Repository
public class DelegateReportDAOImpl implements DelegateReportDAO {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;  
	

	public List<DelegationReportDto> getDelegatedEmployeeDetails()
	{
		String sqlQuery="select e.id,e.displayname,e.personnelno,r.delegatedEmployeeId, e1.id as delegate_empID,e1.displayname as delegatedEmpDisplayname,e1.personnelno as delegatedPersonnelno,r.from_date, r.to_date  from employee e, employee e1, roledelegation r where  e.id=r.employeeId and e1.id=r.delegatedEmployeeId";
		System.out.println("sqlQueryExecuted");
		return (List<DelegationReportDto>)jdbcTemplate.query(sqlQuery, new ResultExtractData());
	}
	
	public List<DelegationReportDto> getDelegationReport(String fromDate,String toDate) {
			fromDate=OtherFunctions.changeDateFromatToIso(fromDate);
			toDate=OtherFunctions.changeDateFromatToIso(toDate);
				
		String sqlQuery="select e.id,e.displayname,e.personnelno,r.delegatedEmployeeId, e1.id as delegate_empID,e1.displayname as delegatedEmpDisplayname,e1.personnelno as delegatedPersonnelno,r.from_date, r.to_date  from employee e, employee e1, roledelegation r where  e.id=r.employeeId and e1.id=r.delegatedEmployeeId and(( r.from_date>='"+fromDate+"' and r.from_date<='"+toDate+"') or ( r.to_date>='"+fromDate+"' and r.to_date<='"+toDate+"') or  ( r.from_date>='"+fromDate+"' and r.to_date<='"+toDate+"') or  ( r.from_date<='"+fromDate+"' and r.to_date>='"+toDate+"'))";
		
		System.out.println("sqlQuery2Executed  "+ sqlQuery);
		
		return (List<DelegationReportDto>)jdbcTemplate.query(sqlQuery, new ResultExtractData());
	}
}

			