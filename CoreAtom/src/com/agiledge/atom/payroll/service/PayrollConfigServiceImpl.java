package com.agiledge.atom.payroll.service;

import java.util.ArrayList;

import com.agiledge.atom.payroll.dao.PayrollConfigDaoImpl;
import com.agiledge.atom.payrollsettings.dto.PayrollConfigDto;

public class PayrollConfigServiceImpl implements PayrollConfigService {

	PayrollConfigDaoImpl dao = new PayrollConfigDaoImpl();
	@Override
	public int updatePayrollConfig(PayrollConfigDto dto) {
		//  
		dao.updatePayrollConfig(dto);
		return 0;
	}
	
	 public ArrayList<PayrollConfigDto> getPayrollConfigs() {
		return dao.getPayrollConfigs();
		
	}

}
