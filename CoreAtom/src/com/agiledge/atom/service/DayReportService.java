package com.agiledge.atom.service;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.agiledge.atom.constants.SettingsConstant;
import com.agiledge.atom.dto.DayReportDto;
import com.agiledge.atom.spring.dao.DayReportDao;


public class DayReportService {
public DayReportDto getDayReport(DayReportDto dto)
{
	ApplicationContext ctx=new ClassPathXmlApplicationContext("applicationContext.xml");
	DayReportDao dao=null;	
	dao=(DayReportDao)ctx.getBean(SettingsConstant.dayreportBean);	
    return dao.getDayReport(dto.getSite(), dto.getDate());
}
}
