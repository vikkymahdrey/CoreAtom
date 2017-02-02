package com.agiledge.atom.task.dao;

import java.util.ArrayList;

import com.agiledge.atom.constants.SettingsConstant;
import com.agiledge.atom.dto.EmployeeDto;

public abstract class GetEmps {
	public static String sourceFlag = SettingsConstant.getEmpSource;

	public static GetEmps getSource() {
		if (sourceFlag.equalsIgnoreCase("cd"))
			return (GetEmps)new GetEmpsJSON();
		if (sourceFlag.equals("DB"))
			return  (GetEmps) new GetEmpsDB();
		return null;
	}
	public abstract ArrayList<EmployeeDto> getEmps();
}
