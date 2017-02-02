package com.agiledge.atom.service;

import java.util.ArrayList;

import com.agiledge.atom.dao.TripCostDao;
import com.agiledge.atom.dto.TripDetailsDto;

public class TripCostService {
	public ArrayList<TripDetailsDto> gettripCostInRange(String fromDate,
			String toDate, String project) {
		ArrayList<TripDetailsDto> tripCostList=new TripCostDao().gettripCostInRange(fromDate, toDate, project);
		return tripCostList;
	}
}
