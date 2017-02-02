package com.agiledge.atom.payroll.service;

import java.util.ArrayList;

import com.agiledge.atom.payrollsettings.dto.PayrollConfigDto;

public interface PayrollConfigService {
	
	public int updatePayrollConfig(PayrollConfigDto dto );
	
	public ArrayList<PayrollConfigDto> getPayrollConfigs(); 

}
