package com.agiledge.atom.commons;

import java.util.ArrayList;
import java.util.Random;

import com.agiledge.atom.mobile.dao.MobileTripSheetDao;

public class RandomString
{

  private static final char[] symbols = new char[10];

  static {
    for (int idx = 0; idx < 10; ++idx) {
      symbols[idx] = (char) ('0' + idx);
      System.out.println(symbols[idx]);
    }
  
  }

  private final Random random = new Random();

  private final char[] buf;

  public RandomString(int length)
  {
    if (length < 1)
      throw new IllegalArgumentException("length < 1: " + length);
    buf = new char[length];
  }

  public String nextString()
  {
    for (int idx = 0; idx < buf.length; ++idx) 
      buf[idx] = symbols[random.nextInt(symbols.length)];
    return new String(buf);
  }
  public String getDifferentPassword(ArrayList<String> pswds) {
	  String returnString="0000";
	  long largest = 0;
	  try {
	  for(String pswd : pswds) {
		  try {
			  long curPswd = Long.parseLong(pswd);
			  if(largest<=curPswd) {
				  largest = curPswd;
			  }
		  }catch(Exception e) {
			  ;
		  }
	  }
	  largest = largest + 1;
	  returnString = String.valueOf(largest);
	  }catch(Exception e) {
		  ;
	  }
	  
	  return returnString;
  }
  
  public String nextDriverString(String tripId)
  {
	  MobileTripSheetDao dao=new MobileTripSheetDao();
	  ArrayList<String> pswds= dao.getDriverPasswords(tripId,-15, +15);
	   
	  String returnPswd="";
	   
	  boolean contains=true;
	 for(int i = 0; contains&&i < 2; i++) { 
    for (int idx = 0;   idx < buf.length; ++idx) {
    	int randomInt= random.nextInt(symbols.length);
//    	System.out.println("Random : "+ randomInt);
      buf[idx] = symbols[randomInt];
     
    }
    returnPswd=new String(buf); 
	 
    System.out.println("Generated @"+ i+" :"+returnPswd);
    	if(pswds.contains(returnPswd)) {
    		contains=true;
    		System.out.println("contains :true at "+ i );
    	} else {
    		contains=false;
    		System.out.println("contains :false at "+ i );
    	}
	 }
	  System.out.println(contains);
	 if(contains) {
		 returnPswd = getDifferentPassword(pswds);
		 System.out.println("return Pswd in fn : "+ returnPswd);
		 if(returnPswd.equalsIgnoreCase("0000")) {
			 returnPswd = returnPswd + tripId;
		 }
		 
	 }
    return new String(returnPswd);
  }


  public String nextEscortString(String tripId)
  {
	  MobileTripSheetDao dao=new MobileTripSheetDao();
	  ArrayList<String> pswds= dao.getEscortPasswords(tripId,-15, +15);
	  String returnPswd="";
	  boolean contains=true;
	 for(int i = 0; contains&&i < 10; i++) { 
    for (int idx = 0;   idx < buf.length; ++idx) {
    	int randomInt= random.nextInt(symbols.length);
    	System.out.println("Random : "+ randomInt);
      buf[idx] = symbols[randomInt];
     
    }
    returnPswd=new String(buf);
    System.out.println("Generated @"+ i+" :"+returnPswd);
    	if(pswds.contains(returnPswd)) {
    		contains=true;
    	} else {
    		contains=false;
    	}
	 }
	  
	 if(contains) {
		 returnPswd=tripId;
		 for(int ctr=tripId.length()-1;ctr<buf.length;ctr++) {
			 returnPswd+="0";
		 }
		 
	 }
    return new String(returnPswd);
  }



}
