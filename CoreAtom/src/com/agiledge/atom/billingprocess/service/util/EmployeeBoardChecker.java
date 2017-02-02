package com.agiledge.atom.billingprocess.service.util;

import com.agiledge.atom.commons.collectionUtils.Checker;
import com.agiledge.atom.dto.TripDetailsChildDto;


public class EmployeeBoardChecker implements Checker<TripDetailsChildDto > {
	public String boardStatus;
	@Override
	public boolean check(TripDetailsChildDto  obj) {
		// TODO Auto-generated method stub
		if(obj.getShowStatus()!=null && obj.getShowStatus().equals(boardStatus)) {
			return true;
		}
		else {
		return false;
		}
	}
	
}

