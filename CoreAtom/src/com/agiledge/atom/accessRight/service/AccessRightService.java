package com.agiledge.atom.accessRight.service;

import com.agiledge.atom.accessRight.dao.AccessRightDao;
import com.agiledge.atom.accessRight.dto.AccessRightDto;


public class AccessRightService {

	AccessRightDao dao = new AccessRightDao();
	 public  boolean hasRight(String accessRightName, String userType) {
		AccessRightDto dto = new AccessRightDto();
		dto.setAccessRightName(accessRightName);
		dto.setUserType(userType);
		return dao.isAccessForUserType(dto);		
	}
	 
	 public String getAccessRight(String role, String module) {
		 return dao.getAccessRight(role, module);
	 }
}
