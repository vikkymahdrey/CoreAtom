/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.agiledge.atom.commons;

import java.util.Calendar;

/**
 * 
 * @author 123
 */
public class Common {

	public static int getLastDay(int month, int year) {

		Calendar calendar = Calendar.getInstance();
		calendar.set(year, month, 1);

		int maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		return maxDay;

	}

}
