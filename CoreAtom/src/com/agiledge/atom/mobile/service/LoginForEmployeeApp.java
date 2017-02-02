package com.agiledge.atom.mobile.service;

import java.util.Random;

import com.agiledge.atom.dto.EmployeeDto;
import com.agiledge.atom.mobile.dao.AndroidAppForEmployeeDao;
public class LoginForEmployeeApp {

	static Random rnd = new Random();

	public String mobileNocheck(String mobileno)
	{
	return new AndroidAppForEmployeeDao().mobileNocheck(mobileno);
	}
	
	public String checkImei(String Imei)
	{
		return new AndroidAppForEmployeeDao().checkImei(Imei);
	}

	public String passwordgenerator()
	{
		StringBuilder sb = new StringBuilder();
		int n = 4; 
		String set ="0123456789"; 

		for (int i= 0; i < n; i++) {
		    sb.append(set.charAt(rnd.nextInt(set.length())));
		}
		String result = sb.toString();
		return result;
	}
	
	public String insertOTP(String mobileNo,String Imei,String otp)
	{
		return new AndroidAppForEmployeeDao().insertOTP(mobileNo, Imei, otp);
	}
	
	public EmployeeDto getEmployeeDetails(String imei)
	{
		return new AndroidAppForEmployeeDao().getEmployeeDetails(imei);
	}
}
