package com.agiledge.atom.spring.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.ResultSetExtractor;

import com.agiledge.atom.reports.dto.DelegationReportDto;

public class ResultExtractData implements ResultSetExtractor {
	
	@Override
	public Object extractData(ResultSet rs) throws SQLException
	{
	List<DelegationReportDto> drList=new ArrayList<DelegationReportDto>();
	
	while(rs.next())
	{
		DelegationReportDto dto=new DelegationReportDto();
		dto.setId(rs.getInt("id"));
		dto.setDisplayname(rs.getString("displayname"));
		dto.setPersonnelno(rs.getString("personnelnO"));
		dto.setDelegatedEmployeeId(rs.getInt("delegatedEmployeeId"));
		dto.setDelegate_empID(rs.getInt("delegate_empID"));
		dto.setDelegatedEmpDisplayname(rs.getString("delegatedEmpDisplayname"));
		dto.setDelegatedPersonnelno(rs.getString("delegatedPersonnelno"));
		dto.setFrom_date(rs.getDate("from_date"));
		dto.setTo_date(rs.getDate("to_date"));
		
	/*	dto.setId(rs.getInt(1));
		dto.setDisplayname(rs.getString(2));
		dto.setPersonnelno(rs.getString(3));
		dto.setDelegatedEmployeeId(rs.getInt(4));
		dto.setDelegate_empID(rs.getInt(5));
		dto.setDelegatedEmpDisplayname(rs.getString(6));
		dto.setDelegatedPersonnelno(rs.getString(7));
		dto.setFrom_date(rs.getDate(7));
		dto.setTo_date(rs.getDate(8));*/
	drList.add(dto);
	}
	return drList;
	}

}	



