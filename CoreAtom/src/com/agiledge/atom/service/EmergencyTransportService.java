package com.agiledge.atom.service;

import com.agiledge.atom.dao.EmergencyDaoForCD;
import com.agiledge.atom.dto.EmergencyDto;

public class EmergencyTransportService {
	
	public int insertEmergencyDetails(EmergencyDto dto)
    {
		return new EmergencyDaoForCD().insertEmergencyDetails(dto);
    }

}
