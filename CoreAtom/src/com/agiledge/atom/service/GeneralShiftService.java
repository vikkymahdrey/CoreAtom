package com.agiledge.atom.service;

import java.util.ArrayList;

import com.agiledge.atom.dao.GeneralShiftDAO;
import com.agiledge.atom.dto.GeneralShiftDTO;

public class GeneralShiftService {
	public ArrayList<GeneralShiftDTO> getDeductionTypes()
	{
		return new GeneralShiftDAO().getDeductionTypes();
	}
    
	
	public String insertValidations(GeneralShiftDTO dto,String[] shiftin,String[] shiftout)
	{
		return new GeneralShiftDAO().insertValidations(dto,shiftin,shiftout);
	}
	
	
	public int insertConfigData(GeneralShiftDTO dto,String[] shiftin,String[] shiftout)
	{
		return new GeneralShiftDAO().insertConfigData(dto,shiftin,shiftout);
	}
	
	public ArrayList<GeneralShiftDTO> getallConfigurations()
	{
		return new GeneralShiftDAO().getallConfigurations();
	}
	
	public ArrayList<GeneralShiftDTO> getAssignedLogData(String id)
	{
		return new GeneralShiftDAO().getAssignedLogData(id);
	}
	
	public int deleteConfig(String id)
	{
		return new GeneralShiftDAO().deleteConfig(id);
	}
}
