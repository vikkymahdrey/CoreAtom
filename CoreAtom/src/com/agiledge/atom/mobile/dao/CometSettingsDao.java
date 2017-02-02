package com.agiledge.atom.mobile.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.agiledge.atom.dbConnect.hibernate.HibernateUtil;
import com.agiledge.atom.mobile.dto.CometSettingsDto;

public class CometSettingsDao {

	public CometSettingsDto getCometSettings() {
		// TODO Auto-generated method stub
		CometSettingsDto dto = null;
		try {
			System.out.println("............");
		SessionFactory sf =  HibernateUtil.getSessionFactory();
		Session session = sf.openSession();
		session.getTransaction().begin();
		String selectQuery = "from  cs ";
		System.out.println(".......IIII.");
		org.hibernate.Query query = session.createQuery(selectQuery);
		System.out.println(".......II***II.");
		System.out.println("............");
		if(query.list().size()>0) {
			
			dto = (CometSettingsDto) query.list().get(0);
			System.out.println(dto.toString());
		}

		session.getTransaction().commit();
		session.close();
		
		 
		} catch(Exception e) {
			System.out.println("Error in getCometSettigs :" + e);
		}
		return dto;
	}
}
