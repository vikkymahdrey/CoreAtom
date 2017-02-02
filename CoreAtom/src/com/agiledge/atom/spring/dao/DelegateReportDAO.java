package com.agiledge.atom.spring.dao;

import java.util.List;

import com.agiledge.atom.reports.dto.DelegationReportDto;
import com.agiledge.atom.reports.dto.EmpSubscription;

public interface DelegateReportDAO {	
	
	public List<DelegationReportDto> getDelegationReport(String fromDate,String toDate);

}
