package com.agiledge.atom.service;

import java.util.ArrayList;
import java.util.List;

public class PaginationService {

	public List subList(ArrayList<?> fullList, int start, int end)

	{
		List subList = null;
		if (fullList.size() <= end) {
			subList = fullList.subList(start, fullList.size());
		} else {
			subList = fullList.subList(start, end);
		}
		return subList;

	}

	public List getNext(ArrayList<?> fullList, String startPosition,String endPosition) {
		List subList = null;
		int startPos = 0;
		int endPos = 0;
		try {			
			startPos = Integer.parseInt(startPosition);
			endPos = Integer.parseInt(endPosition);
			System.out.println("start"+startPos+"endpos"+endPos);
			subList = subList(fullList, startPos, endPos);			
			
		} catch (Exception e) {
			System.out.println("Error" + e);
		}
		return subList;
	}
}
