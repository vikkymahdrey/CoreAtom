package com.agiledge.atom.dbConnect.hibernate;


import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

import com.agiledge.atom.constants.SettingsConstant;



public class HibernateUtil {

 
	public static SessionFactory sessionFactory;
	
	static {
		  
		setUpDB();
	}
	
	private static void setUpDB() {
		System.out.println("hibernate file : " + SettingsConstant.hibernateFile);
		Configuration configuration = new Configuration().configure(SettingsConstant.hibernateFile);
				//.addFile(SettingsConstant.hibernateFile).configure();
		StandardServiceRegistryBuilder registryBuilder = new StandardServiceRegistryBuilder()
		.applySettings(configuration.getProperties());
		sessionFactory= configuration.buildSessionFactory(registryBuilder.build());		  
	}
	
	public static SessionFactory getSessionFactory() {
		if(sessionFactory == null ) {
			setUpDB();
		}
		return sessionFactory;
	}
	
	 /*
	public static SessionFactory getAnnotatedSessionFactory() {
		StandardServiceRegistryBuilder registryBuilder = new StandardServiceRegistryBuilder();
	sessionFactory = new AnnotationConfiguration().configure().buildSessionFactory(registryBuilder.build());
	return sessionFactory;
		
	}*/

}
