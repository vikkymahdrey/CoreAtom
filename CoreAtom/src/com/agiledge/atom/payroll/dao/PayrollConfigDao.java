package com.agiledge.atom.payroll.dao;

import com.agiledge.atom.payrollsettings.dto.PayrollConfigDto;

public interface PayrollConfigDao {
	
	public int updatePayrollConfig(PayrollConfigDto dto);
	public  PayrollConfigDto getPayrollConfigs(int site, int transportType );

}
