/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.agiledge.atom.servlets;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 
 * @author Shahid
 */
public class NewClass {

	public static void main(String args[]) {

		/*
		 * String from_date=""+request.getParameter("from_date"); String
		 * to_date=""+request.getParameter("to_date");
		 */
		String from_date = "08/10/2012";
		String to_date = "08/11/2012";
		DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		java.sql.Date startDate = null;
		java.sql.Date endDate = null;
		try {
			startDate = new java.sql.Date(formatter.parse(from_date).getTime());
			endDate = new java.sql.Date(formatter.parse(to_date).getTime());
		} catch (ParseException ex) {
			Logger.getLogger(NewClass.class.getName()).log(Level.SEVERE, null,
					ex);
		}

		Calendar startCalendar = Calendar.getInstance();
		startCalendar.setTime(startDate);
		Calendar endCalender = Calendar.getInstance();
		endCalender.setTime(endDate);
		String dates = "";
		String outputparam = "$";
		while (startCalendar.getTimeInMillis() <= endCalender.getTimeInMillis()) {
			dates = formatter.format(startCalendar.getTime());
			startCalendar.add(Calendar.DATE, 1);
			outputparam += dates + "$";
		}
		// System.out.println(outputparam);

	}
}
