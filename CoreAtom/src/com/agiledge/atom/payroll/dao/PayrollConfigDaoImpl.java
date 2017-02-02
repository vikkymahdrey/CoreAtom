package com.agiledge.atom.payroll.dao;

import java.util.ArrayList;

import javax.management.Query;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.agiledge.atom.dbConnect.hibernate.HibernateUtil;
import com.agiledge.atom.payrollsettings.dto.PayrollConfigDto;

public class PayrollConfigDaoImpl implements PayrollConfigDao {

	@Override
	public int updatePayrollConfig(PayrollConfigDto dto) {
		try {
		  SessionFactory sessionFactory= HibernateUtil.getSessionFactory();
		 
		Session session = sessionFactory.openSession();
		 
		session.getTransaction().begin();
		
		 System.out.println(" before save");
		 String selectQuery = " from payroll_config where site= :site and transportType= :transportType ";
		 //Quer
		 org.hibernate.Query query = session.createQuery(selectQuery);
		  query.setParameter("site", dto.getSite());
		  query.setParameter("transportType", dto.getTransportType());
		  
		  if(query.list().size()>0) {
			  PayrollConfigDto newDto= (PayrollConfigDto) query.list().get(0);
			  dto.setFromDate(newDto.getFromDate());
			  dto.setId(newDto.getId());
			  newDto.setFlatRate(dto.getFlatRate());
			  newDto.setPayrollType(dto.getPayrollType());
			  newDto.setToDate(dto.getToDate());
			  newDto.setOneWayDivider(dto.getOneWayDivider());
			  newDto.setOneWayDivRate(dto.getOneWayDivRate());
			  session.update(newDto);
			  System.out.println("Updated");
		  } else {
			  session.save(dto);
		  }
	  System.out.println(" after save");
	        
		
		session.getTransaction().commit();
		session.close();
		
		return 1;
		}catch(Exception e) {
			//System.out.println("Error in updatePayrollConfig " + e);
			e.printStackTrace();
			return 0;
		}
	}

	public ArrayList<PayrollConfigDto> getPayrollConfigs() {
		// TODO Auto-generated method stub
		Session session = HibernateUtil.getSessionFactory().openSession();
		String selectQuery = " from payroll_config "; 
		org.hibernate.Query query = session.createQuery(selectQuery);
		 ArrayList<PayrollConfigDto> list = (ArrayList<PayrollConfigDto>) session.createQuery(selectQuery).list(); 
		 
		return list;
	}
	
	
	public  PayrollConfigDto getPayrollConfigs(int site, int transportType ) {
		// TODO Auto-generated method stub
		Session session = HibernateUtil.getSessionFactory().openSession();
		PayrollConfigDto dto =null;
		try {
		String selectQuery = " from payroll_config where site= :ste and transportType= :transportType";
		org.hibernate.Query query = session.createQuery(selectQuery);
query.setParameter("ste",  String.valueOf( site));
query.setParameter("transportType", String.valueOf( transportType));
		
		 ArrayList<PayrollConfigDto> list = (ArrayList<PayrollConfigDto>)  query.list(); 
		 
		 try {
			 dto = list.get(0);
			 System.out.println(dto.toString());
		 }catch(Exception ignor) {dto=null;}
		} catch(Exception e) { 
			System.out.println("Exception in getPayrollConfigs site, transporttype " + e);
		}
		session.close();
		return dto;
	}


}
