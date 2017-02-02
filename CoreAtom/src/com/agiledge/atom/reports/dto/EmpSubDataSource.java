package com.agiledge.atom.reports.dto;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

public class EmpSubDataSource implements JRDataSource {
	
	
	

	@Override
	public Object getFieldValue(JRField jrField) throws JRException {
		String fieldName= jrField.getName();
		
		return null;
	}

	@Override
	public boolean next() throws JRException {
		// TODO Auto-generated method stub
		return false;
	}

}
