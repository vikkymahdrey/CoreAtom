package com.agiledge.atom.harddelete.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.agiledge.atom.dbConnect.DbConnect;
import com.agiledge.atom.harddelete.dao.HardDeleteDao;

public class HardDeleteService {

	HardDeleteDao dao = new HardDeleteDao();

	public boolean hardDeleteTripRelatedTables(String site) {
		
		return dao.hardDeleteTripRelatedTables(site);
	}
	
	public boolean hardDeleteSchedule(String site) {
		
		return dao.hardDeleteSchedule(site);
	}


public boolean hardDeleteSubscription(String site) {
		
		return dao.hardDeleteSubscription(site);
	}

	

 
}
