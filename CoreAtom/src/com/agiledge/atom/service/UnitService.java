package com.agiledge.atom.service;
import java.util.ArrayList;

import com.agiledge.atom.dao.UnitMasterDAO;
import com.agiledge.atom.dto.UnitMasterDTO;

public class UnitService {
	
	public ArrayList<UnitMasterDTO> getallUnits()
	{
		return new UnitMasterDAO().getallUnits();
	}
	
	public int updateUnitData(UnitMasterDTO dto)
	{
		return new UnitMasterDAO().updateUnitData(dto);
	}

	
	public int updateStatus(int unitid,String status)
	{
		return new UnitMasterDAO().updateStatus(unitid, status);
	}
	
	public int insertUnitData(UnitMasterDTO dto)
	{
			return new  UnitMasterDAO().insertUnitData(dto);
	}
	
	public int insertunitfromexcel(ArrayList<UnitMasterDTO> list)
	{
		return new  UnitMasterDAO().insertunitfromexcel(list);
	}
}
