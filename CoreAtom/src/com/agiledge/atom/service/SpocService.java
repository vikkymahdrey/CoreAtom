package com.agiledge.atom.service;

import java.util.ArrayList;

import com.agiledge.atom.dao.SpocSetup;
import com.agiledge.atom.dto.EmployeeDto;
import com.agiledge.atom.dto.SpocDto;

public class SpocService {

	public ArrayList<EmployeeDto> getemployeesbymanager(long id)
	{
		return new SpocSetup().getemployeesbymanager(id);
	}
	
	public int insertSpoc(String[] id,String man_id,String from_date,String to_date)
	{
		return new SpocSetup().insertSpoc(id, man_id,from_date,to_date);
	}
	
	public ArrayList<SpocDto> getspocsbymanagerid(long man_id)
	{
		return new SpocSetup().getspocsbymanagerid(man_id);
	}
	
	public int updateStatus(String spocid,String status) {
		return new SpocSetup().updateStatus(spocid, status);
	}
	
	public ArrayList<EmployeeDto> getemployeesbyspoc(long spoc_id )
	{
		return new SpocSetup().getemployeesbyspoc(spoc_id);
	}
	
	public int removeEmployee(String id) {
		return new SpocSetup().removeEmployee(id);
	}
	
	public int insertEmployee(String[] employee_id,String spoc_id)
	{
		return new SpocSetup().insertEmployee(employee_id,spoc_id);
	}
	
	public ArrayList<EmployeeDto> getemployeesbymanagerspocid(long id)
	{
		return new SpocSetup().getemployeesbymanagerspocid(id);
	}
	
	public int checkSpoc(long empid,String from_date,String to_date)
	{
		return new SpocSetup().checkSpoc(empid, from_date, to_date);
	}
	
	public ArrayList<EmployeeDto> getemployeesbymanagerforspoc(long id)
	{
		return new SpocSetup().getemployeesbymanagerforspoc(id);
	}
	
	public int checkAssignEmployee(long empid)
	{
		return new SpocSetup().checkAssignEmployee(empid);
	}
}
