/*

 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.agiledge.atom.commons;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.xml.bind.DatatypeConverter;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FilenameUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.agiledge.atom.constants.SettingsConstant;
import com.agiledge.atom.dto.EmployeeDto;
import com.agiledge.atom.usermanagement.dao.ViewManagementDao;
import com.agiledge.atom.usermanagement.dto.ViewManagementDto;

/**
 * 
 * @author Administrator
 */
public class OtherFunctions {
public static String encryptTheMapKey(String urlString)
{
	String keyString="API_KEY";
	String client="AIzaSyB5VBCKJW8niA-qH66rABzhEYvI5WHsP-I";
		
	  byte[] key=null;
	  URL url =null;
	  try {
		url = new URL(urlString+"&avoid=tolls&client="+client);		
		keyString = keyString.replace('-', '+');
		keyString = keyString.replace('_', '/');
		key=DatatypeConverter.parseBase64Binary(keyString);
		String resource = url.getPath() + '?' + url.getQuery();		   
	    // Get an HMAC-SHA1 signing key from the raw key bytes
	    SecretKeySpec sha1Key = new SecretKeySpec(key, "HmacSHA1");

	    // Get an HMAC-SHA1 Mac instance and initialize it with the HMAC-SHA1 key
	    Mac mac = Mac.getInstance("HmacSHA1");
	    mac.init(sha1Key);

	    // compute the binary signature for the request
	    byte[] sigBytes = mac.doFinal(resource.getBytes());

	    // base 64 encode the binary signature
	    String signature = DatatypeConverter.printBase64Binary(sigBytes);
	    
	    // convert the signature to 'web safe' base 64
	    signature = signature.replace('+', '-');
	    signature = signature.replace('/', '_');
	  //  System.out.println(url.getProtocol() + "://" + url.getHost() + resource + "&signature=" + signature);
	    return url.getProtocol() + "://" + url.getHost() + resource + "&signature=" + signature;
		
		
		
		
		
		
		
		
		
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	return urlString;
	
}
	public static String getRoleNamebyId(int roleId) {
		String roleName;
		ViewManagementDao dao = new ViewManagementDao();
		roleName = dao.getRoleNamebyid(roleId);
		return roleName;

	}

	public static boolean isTimePast(String date, String cutOffTime,
			String startTime) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		String todaysDate;
		DateFormat formatter;
		DateFormat formatter1;
		try {
			formatter = new SimpleDateFormat("yyyy-MM-dd");
			formatter1 = new SimpleDateFormat("HH:mm");
			date=formatter.format(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date));			
			cal.add(Calendar.HOUR_OF_DAY,
					Integer.parseInt(cutOffTime.split(":")[0]));
			cal.add(Calendar.MINUTE, Integer.parseInt(cutOffTime.split(":")[1]));	
			cutOffTime=formatter1.format(cal.getTime());
			todaysDate = formatter.format(new Date());
			if (todaysDate.compareTo(date) == 0
					&& startTime.compareTo(cutOffTime) >= 0) {
				System.out.println("In IF" + todaysDate + date + cutOffTime
						+ startTime);
				return true;
			} else if (date.compareTo(todaysDate) > 0)

			{
				System.out.println("In ELSE IFF" + todaysDate + date
						+ cutOffTime + startTime);
				return true;

			} else {
				System.out.println("In ELSEE" + todaysDate + date + cutOffTime
						+ startTime);
				return false;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}

	}

	public static boolean checkTime(String datesql, String time, String time1) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		String dateSqlDate;
		String curTime;
		DateFormat formatter;
		DateFormat formatter1;
		System.out.println(time1);
		try {
			cal.add(Calendar.HOUR_OF_DAY, Integer.parseInt(time1.split(":")[0]));
			cal.add(Calendar.MINUTE, Integer.parseInt(time1.split(":")[1]));
			formatter = new SimpleDateFormat("yyyy-MM-dd");

			formatter1 = new SimpleDateFormat("HH:mm");

			dateSqlDate = formatter.format(new Date());
			curTime = formatter1.format(cal.getTime());
			System.out.println(dateSqlDate+"11        222"+datesql+"is equal  "+dateSqlDate.equals(datesql)+"  "+time+"  "+curTime+"   "+time.compareTo(curTime));			
			if (dateSqlDate.equals(datesql) && time.compareTo(curTime) > 0)
				return true;
			return false;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

	public static void resetViews(int roleId, ServletContext application) {
		String menuName = String.valueOf(roleId).concat("-menu");
		application.removeAttribute(menuName);
		getLinks(roleId, application);
	}

	public static String getPreviousDay(String sqlFormat) {
		// System.out.println("Got Here");
		try {

			String naturalFormat = "";
			Calendar cal = Calendar.getInstance();
			java.util.Date dateSqlDate;
			DateFormat formatter;
			formatter = new SimpleDateFormat("yyyy-MM-dd");
			dateSqlDate = (java.util.Date) formatter.parse(sqlFormat);
			cal.setTime(dateSqlDate);
			cal.add(Calendar.DATE, -1);
			naturalFormat = formatter.format(cal.getTime());

			// System.out.println("Date" + naturalFormat);
			return naturalFormat;
		} catch (Exception e) {
			// System.out.println("Error" + e);
			return sqlFormat;
		}
	}

	public static String changeDateFromatToSqlFormat(Date anyDate) {
		// System.out.println("Got Here");
		try {
			String naturalFormat = "";
			DateFormat formatter;
			formatter = new SimpleDateFormat("yyyy-MM-dd");
			naturalFormat = formatter.format(anyDate);

			// System.out.println("Date" + naturalFormat);
			return naturalFormat;
		} catch (Exception e) {
			// System.out.println("Error" + e);
			return null;
		}
	}

	public static String formatDateToOrdinaryFormat(Date anyDate) {
		// System.out.println("Got Here");
		try {
			String naturalFormat = "";
			DateFormat formatter;
			formatter = new SimpleDateFormat("dd/MM/yyyy");
			naturalFormat = formatter.format(anyDate);

			//System.out.println("Date 111111111" + naturalFormat);
			return naturalFormat;
		} catch (Exception e) {
			// System.out.println("Error" + e);
			return null;
		}
	}

	public static String getafterthirtydayDateInSqlFormat() {
		// System.out.println("Got Here");
		Calendar anyDate = Calendar.getInstance();
		anyDate.setTime(new Date());
		// System.out.println(anyDate.getTime());
		anyDate.add(Calendar.MONTH, 1);

		try {
			String naturalFormat = "";
			DateFormat formatter;
			formatter = new SimpleDateFormat("yyyy-MM-dd");
			naturalFormat = formatter.format(anyDate.getTime());

			// System.out.println("Date" + naturalFormat);
			return naturalFormat;
		} catch (Exception e) {
			System.out.println("Error" + e);
			return null;
		}
	}

	public static String changeDateFromat(Date sqlFormat) {
		// System.out.println("Got Here");
		try {
			String naturalFormat = "";
			DateFormat formatter;
			formatter = new SimpleDateFormat("yyyy-MM-dd");
			naturalFormat = formatter.format(sqlFormat);

			// System.out.println("Date" + naturalFormat);
			return naturalFormat;
		} catch (Exception e) {
			// System.out.println("Error" + e);
			return sqlFormat.toString();
		}
	}	

	public static String changeDateFromat(String sqlFormat) {
		// System.out.println("Got Here");
		try {
			String naturalFormat = "";
			java.util.Date dateSqlDate;
			DateFormat formatter;
			formatter = new SimpleDateFormat("yyyy-MM-dd");
			dateSqlDate = (java.util.Date) formatter.parse(sqlFormat);
			formatter = new SimpleDateFormat("dd/MM/yyyy");
			naturalFormat = formatter.format(dateSqlDate);

			// System.out.println("Date" + naturalFormat);
			return naturalFormat;
		} catch (Exception e) {
			// System.out.println("Error" + e);
			return sqlFormat;
		}
	}

	public static String changeDateFromatddmmyy(String sqlFormat) {
		// System.out.println("Got Here");
		try {
			String naturalFormat = "";
			java.util.Date dateSqlDate;
			DateFormat formatter;
			formatter = new SimpleDateFormat("yyyy-MM-dd");
			dateSqlDate = (java.util.Date) formatter.parse(sqlFormat);
			formatter = new SimpleDateFormat("dd/MM/yyyy");
			naturalFormat = formatter.format(dateSqlDate);

			// System.out.println("Date" + naturalFormat);
			return naturalFormat;
		} catch (Exception e) {
			// System.out.println("Error" + e);
			return sqlFormat;
		}
	}

	public static String changeDateFromatWithTime(String sqlFormat) {
		// System.out.println("Got Here");
		try {
			String naturalFormat = "";
			java.util.Date dateSqlDate;
			DateFormat formatter;
			formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm");
			dateSqlDate = (java.util.Date) formatter.parse(sqlFormat);
			formatter = new SimpleDateFormat("MM/dd/yyyy hh:mm");
			naturalFormat = formatter.format(dateSqlDate);

			// System.out.println("Date" + naturalFormat);
			return naturalFormat;
		} catch (Exception e) {
			// System.out.println("Error" + e);
			return sqlFormat;
		}
	}

	public static String changeDateFromatToNormalWithTime(Date sqlFormat) {
		// System.out.println("Got Here");
		try {
			String naturalFormat = "";
			java.util.Date dateSqlDate = sqlFormat;
			DateFormat formatter;

			formatter = new SimpleDateFormat("MM/dd/yyyy hh:mm");
			naturalFormat = formatter.format(dateSqlDate);

			// System.out.println("Date" + naturalFormat);
			return naturalFormat;
		} catch (Exception e) {
			// System.out.println("Error" + e);
			return "";
		}
	}

	public static int getDay(String sqlFormat) {
		// System.out.println("Got Here");
		try {
			java.util.Date dateSqlDate;
			int retDay = 0;
			DateFormat formatter;
			formatter = new SimpleDateFormat("yyyy-MM-dd");
			dateSqlDate = (java.util.Date) formatter.parse(sqlFormat);
			Calendar cal = Calendar.getInstance();
			cal.setTime(dateSqlDate);
			retDay = cal.get(Calendar.DAY_OF_WEEK);
			// System.out.println("Date" + naturalFormat);
			return retDay;
		} catch (Exception e) {
			// System.out.println("Error" + e);
			return 0;
		}
	}

	public static String getWeekDay(String sqlFormat) {
		// System.out.println("Got Here");
		try {
			java.util.Date dateSqlDate;
			int retDay = 0;
			DateFormat formatter;
			formatter = new SimpleDateFormat("yyyy-MM-dd");
			dateSqlDate = (java.util.Date) formatter.parse(sqlFormat);
			Calendar cal = Calendar.getInstance();
			cal.setTime(dateSqlDate);
			retDay = cal.get(Calendar.DAY_OF_WEEK);
			String[] strDays = new String[] { "Sunday", "Monday", "Tuesday",
					"Wednesday", "Thursday", "Friday", "Saturday" };
			// System.out.println("Date" + naturalFormat);
			return strDays[retDay - 1];
		} catch (Exception e) {
			System.out.println("Error" + e);
			return "";
		}
	}

	public static String changeDateFromatToddmmyyyy(String sqlFormat) {
		// System.out.println("Got Here");
		try {
			if (sqlFormat.equals("1900-01-01")) {
				return "";
			}
			String naturalFormat = "";
			java.util.Date dateSqlDate;
			DateFormat formatter;
			formatter = new SimpleDateFormat("yyyy-MM-dd");
			dateSqlDate = (java.util.Date) formatter.parse(sqlFormat);
			formatter = new SimpleDateFormat("dd/MM/yyyy");
			naturalFormat = formatter.format(dateSqlDate);

			// System.out.println("Date" + naturalFormat);
			return naturalFormat;
		} catch (Exception e) {
			// System.out.println("Error" + e);
			return sqlFormat;
		}
	}

	public static String changeDateFromatToddmmyyyymmss(Date date) {
		// System.out.println("Got Here");
		String naturalFormat = "";
		try {
			/*
			 * if(sqlFormat.equals("1900-01-01")) { return ""; }
			 */

			// java.util.Date dateSqlDate;
			DateFormat formatter;
			// System.out.println("given" + sqlFormat);
			formatter = new SimpleDateFormat("dd/MM/yyyy");
			// dateSqlDate = (java.util.Date) formatter.parse(sqlFormat);
			// formatter = new SimpleDateFormat("dd/MM/yyyy");
			naturalFormat = formatter.format(date);

			// System.out.println("Date" + sqlFormat);
			return naturalFormat;
		} catch (Exception e) {
			// System.out.println("Error" + e);
			return naturalFormat;
		}
	}

	public static boolean checkedDateForAdmin(String date)
			throws ParseException {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());

		// int is6pmPassed = cal.get(Calendar.HOUR_OF_DAY);
		// cal.add(Calendar.DATE, 1);
		java.util.Date curDate;
		String givenDate;
		String currentDate;
		DateFormat formatter;

		formatter = new SimpleDateFormat("yyyy-MM-dd");

		curDate = (java.util.Date) formatter.parse(date);
		formatter = new SimpleDateFormat("yyyy-MM-dd");
		givenDate = formatter.format(cal.getTime());
		currentDate = formatter.format(curDate);
		givenDate =  changeTimeFormat( SettingsConstant.getCurdate(),"dd/MM/yyyy", "yyyy-MM-dd" );
		if (currentDate.compareTo(givenDate) > 0
				|| (currentDate.compareTo(givenDate) == 0)) {
			return true;
		} else {
			return false;
		}
	}

	/*
	 * public static String changeDateFromatFromutilToddmmyyyy(String sqlFormat)
	 * { // System.out.println("Got Here"); try {
	 * 
	 * 
	 * String naturalFormat = ""; java.util.Date dateSqlDate; DateFormat
	 * formatter; formatter = new SimpleDateFormat("yyyy-MM-dd"); dateSqlDate =
	 * (java.util.Date) formatter.parse(sqlFormat); formatter = new
	 * SimpleDateFormat("dd/MM/yyyy"); naturalFormat =
	 * formatter.format(dateSqlDate);
	 * 
	 * // System.out.println("Date" + naturalFormat); return naturalFormat; }
	 * catch (Exception e) { // System.out.println("Error" + e); return
	 * sqlFormat; } }
	 */
	public static Date stringToDate(String sqlFormat) {
		// System.out.println("Got Here");
		java.util.Date dateSqlDate = null;
		try {
			DateFormat formatter;
			formatter = new SimpleDateFormat("MM/dd/yyyy");
			dateSqlDate = (java.util.Date) formatter.parse(sqlFormat);

			// System.out.println("Date" + naturalFormat);
			// System.out.println("Input Date " + sqlFormat +
			// " Returning Date : "
			// + dateSqlDate);
			return dateSqlDate;
		} catch (Exception e) {
			System.out.println("Error" + e);
			return dateSqlDate;
		}
	}

	public static Date sqlFormatToDate(String sqlFormat) {
		Date dateSqlDate = null;
		try {
			DateFormat formatter;
			formatter = new SimpleDateFormat("yyyy-MM-dd");
			dateSqlDate = formatter.parse(sqlFormat);

			return dateSqlDate;
		} catch (Exception e) {
			System.out.println("Error here" + e);
			return dateSqlDate;
		}
	}

	public static String checkUser(HttpSession session) {
		String empid = "" + session.getAttribute("user");
		// System.out.println("Role" + role);
		return empid;
	}

	/*
	 * public static String checkUser(HttpSession session, String roleParam) {
	 * String empid = "" + session.getAttribute("user"); role = "" +
	 * session.getAttribute("role"); System.out.println("Role" + role);
	 * System.out.println("Role Param : " + roleParam); if
	 * (roleParam.equals("hrm")) { if (role.equals("hrm") || role.equals("hrme")
	 * || role.equals("tmhr")) { return empid; } else { return null; } } else if
	 * (roleParam.equals("tm")) { if (role.equals("tm") || role.equals("tmhr")
	 * || role.equals("tme")) { return empid; } else { return null; } } else if
	 * (roleParam.equals("emp")) { if (role.equals("emp") || role.equals("tme")
	 * || role.equals("hrme")) { return empid;
	 * 
	 * } else { return null; } } else { return null; }
	 * 
	 * }
	 */

	private static String getLinksFromDb(int roleId) {
		// System.out.println("role : " + role);
		StringBuffer Links = new StringBuffer();
		StringBuffer returnLinks = new StringBuffer();

		StringBuffer finalMenu = new StringBuffer();
		ViewManagementDao daoobj = new ViewManagementDao();
		ArrayList<ViewManagementDto> viewList = daoobj.getViewsbyRole(roleId);
		for (ViewManagementDto dto : viewList) {
			ArrayList<ViewManagementDto> subviewList = daoobj
					.getSubviewsbyView(dto.getViewId());
			// System.out.println("su");
			Links.append("<a href='").append(dto.getViewURL())
					.append("'><span>");
			Links.append(dto.getViewName()).append("</span></a>");
			if (subviewList.isEmpty()) {
				finalMenu.append("<li>").append(Links.toString())
						.append("</li> ");
				returnLinks.append(finalMenu.toString());
				Links = new StringBuffer();
				finalMenu = new StringBuffer();

			} else {
				finalMenu.append("<li class='has-sub'>")
						.append(Links.toString()).append("<ul>");
				// System.out.println("subviewList size()" +
				// subviewList.size());
				for (ViewManagementDto dto1 : subviewList) {
					finalMenu.append("<li > <a href='")
							.append(dto1.getSubViewURL()).append("'>")
							.append(dto1.getSubViewName()).append("</a></li>");

				}

				returnLinks.append(finalMenu.toString()).append("</ul>")
						.append("</li>");
				Links = new StringBuffer();
				finalMenu = new StringBuffer();

			}
		}

		return returnLinks.toString();

	}

	public static String getLinks(int roleId, ServletContext application) {
		System.out.println("IN GetLINKS");
		String menuName = String.valueOf(roleId).concat("-menu");

		String menu = null;
		try {
			menu = application.getAttribute(menuName).toString();

			// System.out.print("Menu from context "+menu);
		} catch (Exception e) {
			System.out.println("Menu empty");
			menu = "";
		}
		if (menu.equals("")) {
			menu = OtherFunctions.getLinksFromDb(roleId);
			// System.out.print("Menu from db "+menu);
			application.setAttribute(menuName, menu);

		}
		return menu;
	}

	public static boolean checkDate(String dateold) throws ParseException {
		DateFormat formatter;
		formatter = new SimpleDateFormat("yyyy-MM-dd");

		Date currentDate = new Date();
		String passDate = formatter.format(currentDate);
		// System.out.println("passDate" + passDate + "curDtae" + currentDate);
		if (passDate.compareTo(dateold) < 0) {
			return false;
		} else {
			return true;
		}
	}

	// -----------------
	public static String toISODate(String usDate) {
		// System.out.println("Got Here");

		String d[] = usDate.split("/");

		// System.out.println("Date" + naturalFormat);
		return d[1] + "-" + d[0] + "-" + d[2] + " 12:00:00 AM";

	}

	public static Date resetTime(Date toDate) {

		if (toDate == null) {
			return null;
		} else {
			Calendar updatedatetime = Calendar.getInstance();
			updatedatetime.setTime(toDate);
			updatedatetime.set(Calendar.HOUR, 0);
			updatedatetime.set(Calendar.MINUTE, 00);
			updatedatetime.set(Calendar.SECOND, 00);
			updatedatetime.set(Calendar.AM_PM, Calendar.AM);
			// System.out.println("Inreset"+updatedatetime.getTime());
			return updatedatetime.getTime();
		}
	}

	// ----------
	public static String getMonthInString(int month) {
		String monthString[] = { "January", "February", "March", "April",
				"May", "June", "July", "August", "September", "October",
				"November", "December" };
		return monthString[month - 1];

	}

	public static String getRoleName(String role) {
		String roleName;
		ViewManagementDao dao = new ViewManagementDao();
		roleName = dao.getRoleNamebyUserType(role);
		return roleName;

	}
	public static String getRoleNameById(String id) {
		String roleName;
		ViewManagementDao dao = new ViewManagementDao();
		roleName = dao.getRoleNameById(id);
		return roleName;

	}

	public static String getOrdinaryDateFormat(String date) {
		String returnDate = null;
		java.util.Date dateDate = null;
		try {
			DateFormat formatter;
			formatter = new SimpleDateFormat("yyyy-MM-dd");
			dateDate = (java.util.Date) formatter.parse(date);

			// System.out.println("Input Date " + date + " Returning Date : "
			// + dateDate);
			formatter = new SimpleDateFormat("dd/MM/yy");
			returnDate = formatter.format(dateDate);
			return returnDate;
		} catch (Exception e) {
			System.out.println("Error" + e);
			return date;
		}
	}

	public static String changeDateFromatToIso(String ordinaryFormat) {
		// System.out.println("Got Here ordinary format"+ordinaryFormat);
		try {
			Date naturalFormat;
			DateFormat formatter;
			String isoFormat = "";
			formatter = new SimpleDateFormat("dd/MM/yyyy");
			naturalFormat = formatter.parse(ordinaryFormat);
			formatter = new SimpleDateFormat("yyyy-MM-dd");
			isoFormat = formatter.format(naturalFormat);
			// System.out.println("Date " + isoFormat);
			return isoFormat;
		} catch (Exception e) {
			// System.out.println("Errorin             ...................Iso  "+e);
			// + e);
			return ordinaryFormat;
		}
	}

	public static String changeDateFromatToIso(Date ordinaryFormat) {
		// System.out.println("Got Here ordinary format"+ordinaryFormat);
		try {
			// Date naturalFormat = null;
			DateFormat formatter;
			String isoFormat = "";
			formatter = new SimpleDateFormat("yyyy-MM-dd");
			isoFormat = formatter.format(ordinaryFormat);
			// System.out.println("Date" + isoFormat);
			return isoFormat;
		} catch (Exception e) {

			System.out.println("Errorin >>>>>>>>>.Iso  " + e);
			return ordinaryFormat.toString();
		}
	}

	public static boolean checkedDate(String date) throws ParseException {
		// System.out.println("Date wen"+date);
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());

		int is6pmPassed = cal.get(Calendar.HOUR_OF_DAY);
		cal.add(Calendar.DATE, 1);
		java.util.Date curDate;
		String givenDate;
		String nextDate;
		String currentDate;
		DateFormat formatter;

		formatter = new SimpleDateFormat("yyyy-MM-dd");
		try {
			curDate = (java.util.Date) formatter.parse(date);
			formatter = new SimpleDateFormat("yyyy-MM-dd");

			nextDate = formatter.format(cal.getTime());
			currentDate = formatter.format(curDate);
			
			givenDate =  changeTimeFormat( SettingsConstant.getCurdate(),"dd/MM/yyyy", "yyyy-MM-dd" );
			if(nextDate.compareTo(givenDate)>0)
			{
				givenDate=nextDate;	
			}
			if (currentDate.compareTo(givenDate) > 0
					|| ((currentDate.compareTo(givenDate) == 0 && is6pmPassed < 23))) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			System.out.println("ERROR In parse" + e);
			return false;
		}
	}

	public static String addDate(String date) {
		// TODO Auto-generated method stub
		try {
			Calendar cal = Calendar.getInstance();
			Date correctDate;

			DateFormat formatter;
			formatter = new SimpleDateFormat("yyyy-MM-dd");
			correctDate = formatter.parse(date);
			cal.setTime(correctDate);
			cal.add(Calendar.DATE, -1);
			return formatter.format(cal.getTime());
		} catch (Exception e) {
			System.out.println("Error" + e);
			return date;
		}
	}

	public static String addDate(Date date, int days) {
		// TODO Auto-generated method stub
		try {
			Calendar cal = Calendar.getInstance();

			cal.setTime(date);
			cal.add(Calendar.DATE, days);
			DateFormat formatter;
			formatter = new SimpleDateFormat("yyyy-MM-dd");
			return formatter.format(cal.getTime());
		} catch (Exception e) {
			System.out.println("Error" + e);
			return date.toString();
		}
	}

	public static boolean condains(String string, String[] strings) {
		boolean flag = false;
		for (String str : strings) {
			if (str.equals(string)) {
				flag = true;
				break;
			}
		}

		return flag;
	}

	public static String getEmptyIfNull(String val) {
		if (val == null) {
			return "";
		} else
			return val;
	}

	// the below function is to put employee email address to the hash map is it
	// is existing already
	// it will deleted and will not be inserted again

	public static void insertIfNotExistsElseDelete(HashMap<String, String> map,
			String email, HashMap<String, String> chak) {
		try {
			if (chak.containsKey(email) == false) {

				map.put(email, email);
				chak.put(email, email);
			}
		} catch (Exception e) {
			System.out.println("Error in insertIfNotExistsElseDelte " + e);
		}
	}

	public static String[] mapToStringArray(HashMap<String, String> map) {
		String value[] = new String[map.size()];
		int i = 0;
		for (Iterator<?> it = map.entrySet().iterator(); it.hasNext();) {
			@SuppressWarnings("unchecked")
			Entry<String, String> var = (Entry<String, String>) it.next();
			value[i++] = var.getValue();

		}
		return value;
	}

	public static String setNullIfEmpty(String value) {
		return value == null ? "" : value;
	}

	@SuppressWarnings("static-access")
	public static String getAccurateAccountingDate(int day) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.set(cal.DAY_OF_MONTH, day);

		if (cal.get(cal.DAY_OF_WEEK) == 1) {
			cal.set(cal.DAY_OF_MONTH, cal.get(cal.DAY_OF_MONTH) - 2);
		} else if (cal.get(cal.DAY_OF_WEEK) == 7) {
			cal.set(cal.DAY_OF_MONTH, cal.get(cal.DAY_OF_MONTH) - 1);
		}
		return changeDateFromatToSqlFormat(cal.getTime());
	}

	public static String[] appendArray(String[] bcc, String[] cc)
			throws StringIndexOutOfBoundsException {

		String returnValue[] = new String[bcc.length + cc.length];
		int i = 0;
		while (i < bcc.length) {
			returnValue[i] = bcc[i];
			i++;
		}
		int j = 0;
		while (j < cc.length) {
			returnValue[i] = cc[j];
			j++;
			i++;
		}
		return returnValue;
	}

	public static Calendar getLastDateOfMonth(int year, int month) {
		Calendar cal = new GregorianCalendar(year, month, Calendar.DAY_OF_MONTH);
		cal.set(Calendar.DAY_OF_MONTH,
				cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		return cal;

	}

	public static boolean isISODate(String dateParam) {
		boolean flag = false;
		try {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			df.parse(dateParam);
			flag = true;

		} catch (ParseException e) {
			flag = false;
		}
		return flag;
	}

	public static boolean isNormalDate(String dateParam) {
		boolean flag = false;
		try {
			SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
			df.parse(dateParam);
			flag = true;

		} catch (ParseException e) {
			flag = false;
		}
		return flag;
	}

	public static String getNewWeekOffString(String logTime) {
		if (logTime != null && logTime.equalsIgnoreCase("weekly off")) {
			return "Saturday & Sunday OFF";
		} else {
			return logTime;
		}

	}

	public static void changeEscapeCharecter(
			ArrayList<EmployeeDto> externalempList) {

	}

	public static String FullminutesOptions() {
		String retVal = "";
		for (int i = 0; i <= 60; i++) {
			if (i < 10) {
				retVal += "<option value='0" + i + "'>0" + i + "</option>";
			} else {
				retVal += "<option value='" + i + "'>" + i + "</option>";
			}
		}
		return retVal;
	}

	public static String FullminutesOptions(String logTime, String selectedTime) {
		String retVal = "";
		String value = "";
		if (selectedTime == null || selectedTime.equals("null")
				|| selectedTime.equals("")) {
			selectedTime = logTime;
		}
		
		for (int i = 0; i <= 60; i++) {
			if (i < 10) {
				value = "0" + i;

			} else {
				value = "" + i;
			}
			if (value.equals(selectedTime)) {
				retVal += "<option selected value='" + value + "'>" + value
						+ "</option>";
			} else {
				retVal += "<option value='" + value + "'>" + value
						+ "</option>";
			}
		}
		System.out.println();
		return retVal;
	}

	public static String getTimePartFromDate(Date date) {
		String time = "";
		try {
			SimpleDateFormat df = new SimpleDateFormat("HH:mm");
			time = df.format(date);

		} catch (Exception e) {
			System.out.println("Error" + e);
		}
		return time;
	}

	public boolean checkDate90(String date) {
		boolean flag = false;
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Calendar cal = Calendar.getInstance();
			Calendar cal1 = Calendar.getInstance();
			cal.setTime(new Date());
			cal.add(Calendar.DATE, -90);
			Date dbDate = dateFormat.parse(date);
			cal1.setTime(dbDate);
			if (cal.getTime().after(cal1.getTime())) {
				flag = true;
			} else {
				flag = false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;

	}

	/*
	 * Time: time in string. for eg. "20:30" timeOption: return value eg.
	 * minute, second, hour
	 */
	public static long getTimeDifference(Date tripDate, String time,
			String timeOption) {
		String tripTime = time;
		Calendar tripTimeCal = Calendar.getInstance();
		tripTimeCal.setTime(tripDate);

		tripTimeCal.set(Calendar.HOUR_OF_DAY,
				Integer.parseInt(tripTime.split(":")[0]));
		tripTimeCal.set(Calendar.MINUTE,
				Integer.parseInt(tripTime.split(":")[1]));
		// System.out.println("Get "+tripTimeCal.get(Calendar.HOUR_OF_DAY)+":"+tripTimeCal.get(Calendar.MINUTE)+
		// "  in millisecond :"+ tripTimeCal.getTimeInMillis());

		Calendar curTimeCal = Calendar.getInstance();
		curTimeCal.setTime(new Date());

		// System.out.println("Get curtime "+curTimeCal.get(Calendar.HOUR_OF_DAY)+":"+curTimeCal.get(Calendar.MINUTE)+" in millisecond :"+
		// curTimeCal.getTimeInMillis());

		long millisecond = curTimeCal.getTimeInMillis()
				- tripTimeCal.getTimeInMillis();
		long value = 0;
		if (timeOption.equalsIgnoreCase("second")) {
			value = millisecond / (1000);
		} else if (timeOption.equalsIgnoreCase("minute")) {
			value = millisecond / (1000 * 60);
		} else if (timeOption.equalsIgnoreCase("hour")) {
			value = millisecond / (1000 * 60 * 60);
		}

		return value;

	}

	/*
	 * this function read data from request and convert to org.json
	 */
	public static JSONObject getJSONObject(HttpServletRequest request)
			throws IOException, JSONException {
		BufferedReader reader = request.getReader();
		String line;
		String lines = "";

		while ((line = reader.readLine()) != null) {
			System.out.println("Line :" + line);
			lines = lines + line + "\n";
		}
		reader.close();
		JSONObject obj = new JSONObject(lines);
		return obj;
	}

	public static Date changeDateFromat_ddmmyyy_to_UtilDate(String ddmmyyyyDate) {
		try {
			String naturalFormat = "";
			java.util.Date dateSqlDate;
			DateFormat formatter;
			formatter = new SimpleDateFormat("dd/MM/yyyy");
			dateSqlDate = (java.util.Date) formatter.parse(ddmmyyyyDate);

			// System.out.println("Date" + naturalFormat);
			return dateSqlDate;
		} catch (Exception e) {
			// System.out.println("Error" + e);
			return null;
		}

	}

	public static String FullhoursOptions() {
		String retVal = "";
		for (int i = 0; i <= 23; i++) {
			if (i < 10) {
				retVal += "<option value='0" + i + "'>0" + i + "</option>";
			} else {
				retVal += "<option value='" + i + "'>" + i + "</option>";
			}
		}
		return retVal;
	}

	public static String FullhoursOptions(String logTime, String selectedTime) {
		String retVal = "";
		String value = "";
		if (selectedTime == null || selectedTime.equals("null")
				|| selectedTime.equals("")) {
			selectedTime = logTime;
		}
		String selected = selectedTime.split(":")[0];
		for (int i = 0; i <= 23; i++) {
			if (i < 10) {
				value = "0" + i;

			} else {
				value = "" + i;
			}
			if (value.equals(selected)) {
				retVal += "<option selected value='" + value + "'>" + value
						+ "</option>";
			} else {
				retVal += "<option value='" + value + "'>" + value
						+ "</option>";
			}
		}
		return retVal;
	}

	public static String FullTimeInIntervalOptions() {
		String retVal = "";
		String valPart = "";
		for (int i = 0; i <= 23; i++) {
			for (int j = 0; j < 60; j = j + 15) {
				if (i < 10) {
					if (j < 10) {
						valPart = "0" + i + ":0" + j + "";
					} else {
						valPart = "0" + i + ":" + j + "";
					}
				} else {
					if (j < 10) {
						valPart = i + ":0" + j;
					} else {
						valPart = i + ":" + j;
					}

				}
				retVal += "<option value='" + valPart + "'>" + valPart
						+ "</option>";
			}
		}
		return retVal;
	}

	public static String FullTimeInIntervalOptionsWithSelect(String value) {
		String retVal = "";
		String valPart = "";
		for (int i = 0; i <= 23; i++) {
			for (int j = 0; j < 60; j = j + 15) {
				if (i < 10) {
					if (j < 10) {
						valPart = "0" + i + ":0" + j + "";
					} else {
						valPart = "0" + i + ":" + j + "";
					}
				} else {
					if (j < 10) {
						valPart = i + ":0" + j;
					} else {
						valPart = i + ":" + j;
					}

				}
				if (valPart.equals(value)) {
					retVal += "<option selected='selected' value='" + valPart
							+ "'>" + valPart + "</option>";
				} else {
					retVal += "<option value='" + valPart + "'>" + valPart
							+ "</option>";
				}
			}
		}
		return retVal;
	}

	public static String siteWiseSMSContent(String site) {
		String retString = "";
		if (site.equals("1")) {
			retString = "Regards, CGI Technopolis TPT Team 9980933662";
		} else if (site.equals("2")) {
			retString = "Regards, CGI Bagmane TPT Team 9900063484";
		} else if (site.equals("3")) {
			retString = "Regards, CGI DLF Chennai TPT team";
		}
		return retString;
	}

	public static boolean isEmpty(String value) {
		if (value == null || value.trim().equals("")||value.equals("null")) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isDouble(String s) {
		boolean flag = true;
		try {
			Double.parseDouble(s);
		} catch (Exception e) {
			flag = false;
		}
		return flag;
	}

	public static boolean isInteger(String s) {
		boolean flag = true;
		try {
			Integer.parseInt(s);
		} catch (Exception e) {
			flag = false;
		}
		return flag;
	}

	public static boolean isEitherJspOrServlet(String page) {
		boolean val = false;
		if (page.contains(".")) {
			if (page.indexOf("jsp") == (page.length() - 3)) {
				val = true;
			} else if (page.indexOf("do") == (page.length() - 2)) {
				val = true;
			}
		} else {
			val = true;
		}
		return val;
	}

	public static boolean getBoolean(String bool) {
		boolean value = false;
		try {
if(bool!=null&&bool.equalsIgnoreCase("yes"))
{
	bool="true";	
}
			value = Boolean.parseBoolean(bool);
		} catch (Exception e) {
		}
		System.out.println("Boolean  : " + value);
		return value;
	}

	public static boolean checkDateFormat(String date, String dateFormat) {
		boolean value = true;
		SimpleDateFormat format = new SimpleDateFormat(dateFormat);
		try {
			format.parse(date);

		} catch (Exception e) {
			value = false;
		}

		return value;

	}

	public static String getRequestedFields(HttpServletRequest request,
			String field) {
		String value = null;
		try {
			List<FileItem> items = new ServletFileUpload(
					new DiskFileItemFactory()).parseRequest(request);
			for (FileItem item : items) {
				if (item.isFormField()) {
					// Process regular form field (input
					// type="text|radio|checkbox|etc", select, etc).
					String fieldname = item.getFieldName();
					String fieldvalue = item.getString();
					if (field.equals(fieldname)) {
						value = fieldvalue;
						break;
					}
					// ... (do your job here)
				} else {
					// Process form file field (input type="file").
					String fieldname = item.getFieldName();
					String filename = FilenameUtils.getName(item.getName());

					InputStream filecontent = item.getInputStream();
					if (field.equals(fieldname)) {
						value = item.getString();
						break;
					}
					// ... (do your job here)
				}
			}
		} catch (Exception e) {
			System.out.println(" Error in getRequstedFieds " + e);
		}
		return value;
	}
	
	public static String TimeFormat24Hr(String time ) {
		String val=null;
		try {
			String s[] =time.split(":");
			int hh = Integer.parseInt(s[0]);
			int mm =0;
			try {
				mm = Integer.parseInt(s[1]);
				System.out.println("mm = "+ mm);
				// check 12:30:00 PM
				System.out.println("splitted length : "+ s.length);
				System.out.println(" hh:mm "+ hh + ": "+ mm);
				if(s.length>2) {
					try {
						 
						String smm[] = s[2].split(" ");
						
						int mmil = Integer.parseInt(smm[0]);
						if(mmil > 59) {
							throw new Exception("Milli second > 59");
						}
						if(smm[1].equalsIgnoreCase("PM")) {
							hh = hh + 12;
							if(hh==24) {
								hh=0;
							}
							
						}
					} catch (Exception ne) {
						throw ne;
					}
				}
			}catch(NumberFormatException ne) {
				// check 12:30 PM fromat
				String sm[] = s[1].split(" ");
				try {
					mm = Integer.parseInt(sm[0]);
				}catch(Exception e) {
					throw new Exception("Exception in setting second in 12:30 PM");
				}
				if(sm[1].equalsIgnoreCase("PM")) {
					hh = hh + 12;
					if(hh==24) {
						hh=0;
					}
				}
			}
			if(hh>23 || hh<0) {
				throw new Exception("hh > 23 or hh <0");
			} else if (mm>59 || mm<0) {
				throw new Exception("mm > 23 or mm <0");
			}
			String hString = hh <= 9 ? "0" + hh : ""+hh;
			String mString = mm <= 9 ? "0" + mm : "" + mm;
			val = hString + ":" + mString;
			
		}catch(Exception e) {
			
			System.out.println("Error in 24timeformat : "+ e);
		}
		return val;
	}
	
	
	public static String changeTimeFormat(String time, String sourceFormat, String destinationFormat) {
		  SimpleDateFormat displayFormat = new SimpleDateFormat(destinationFormat);
		   
		try {
			
		       SimpleDateFormat parseFormat = new SimpleDateFormat(sourceFormat);
		       Date date = parseFormat.parse(time);
		       //System.out.println(parseFormat.format(date) + " = " + displayFormat.format(date));
		       time =displayFormat.format(date); 
	       
		}catch(Exception e) {
			;
		}
	       return time; 
	}
	

	public static String changeDateFormat(Date date,   String destinationFormat) {
		  SimpleDateFormat displayFormat = new SimpleDateFormat(destinationFormat);
		   String time="";
		try {
			
		        
		       //System.out.println(parseFormat.format(date) + " = " + displayFormat.format(date));
		       time =displayFormat.format(date); 
	       
		}catch(Exception e) {
			;
		}
	       return time; 
	}
	
	public static int getDayIndexFromStringWeekOfDay(String day)  
	 {  
	  if(day.equalsIgnoreCase("Monday"))  
	   return 2;  
	  if(day.equalsIgnoreCase("Tuesday"))  
	   return 3;  
	  if(day.equalsIgnoreCase("Wednesday"))  
	   return 4;  
	  if(day.equalsIgnoreCase("Thursday"))  
	   return 5;  
	  if(day.equalsIgnoreCase("Friday"))  
	   return 6;  
	  if(day.equalsIgnoreCase("Saturday"))  
	   return 7;  
	  if(day.equalsIgnoreCase("Sunday"))  
	   return 1;  
	  return 0;   
	 }

	public static String addTime(String trip_time,
			String firstPointTime) {
		String tripTims[]=trip_time.split(":");
		int travleTimeSec=(int) (Float.parseFloat(firstPointTime)*60);
		SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a");
		Calendar calendar=Calendar.getInstance();		
		try {
			
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.AM_PM, Calendar.AM);
			calendar.set(Calendar.HOUR, Integer.parseInt(tripTims[0]));
			calendar.set(Calendar.MINUTE, Integer.parseInt(tripTims[1]));
			System.out.println(calendar.getTime());
			calendar.add(Calendar.SECOND, -(travleTimeSec+600));
			System.out.println(calendar.getTime());
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		
					
		String date3 = timeFormat.format(calendar.getTime());
		return date3;
	}  
	
	public static String getTodaysDate()
	{
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	    return simpleDateFormat.format(new Date());	
	}
	public static String getNextDay(String sqlFormat) {
		// System.out.println("Got Here");
		try {

			String naturalFormat = "";
			Calendar cal = Calendar.getInstance();
			java.util.Date dateSqlDate;
			DateFormat formatter;
			formatter = new SimpleDateFormat("yyyy-MM-dd");
			dateSqlDate = (java.util.Date) formatter.parse(sqlFormat);
			cal.setTime(dateSqlDate);
			cal.add(Calendar.DATE, 1);
			naturalFormat = formatter.format(cal.getTime());

			// System.out.println("Date" + naturalFormat);
			return naturalFormat;
		} catch (Exception e) {
			// System.out.println("Error" + e);
			return sqlFormat;
		}
	}
	 
}