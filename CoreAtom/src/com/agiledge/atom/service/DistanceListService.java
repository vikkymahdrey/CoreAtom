package com.agiledge.atom.service;

import java.util.ArrayList;

import com.agiledge.atom.dao.DistanceListDao;
import com.agiledge.atom.dto.APLDto;
import com.agiledge.atom.dto.DistanceChartDto;

public class DistanceListService {
	static ArrayList<APLDto> apls=null;
	static int aplPointer=0;
	static int currentPos=0;
	
	public ArrayList<DistanceChartDto> getsourceAndDestination(int srcId) {
		ArrayList<DistanceChartDto> lst=new DistanceListDao().getsourceAndDestination(srcId,currentPos);
		return null;
	}	
}
