package com.agiledge.atom.settings.dto;

public interface SettingStatus {

	/* AUTHERISATION_INSERTION_MODE IS USED TO ADD automatically the ALL REQUESTed urls 
	 * to the roles who are currently accessing the page 
	 * vlaues : ON/ OFF. WORKS IF ONLY ON
	 */
	public String AUTHERISATION_INSERTION_MODE="AUTHERISATION_INSERTION_MODE";
	public String LOAD_PAGE_ROLE_DATA="LOAD_PAGE_ROLE_DATA";
	public String LOAD_PAGE_ROLE_DATA_ON="ON";
	public String LOAD_PAGE_ROLE_DATA_OFF="OFF";
	public String AUTHERISATION_INSERTION_MODE_VALUE_ON="ON";
	
	public String AUTHERISATION_INSERTION_MODE_VALUE_OFF="OFF";
	
	/* this property is to enable or disable servlet and page searching by app
	 * in order to add to table page.
	 * values 'YES', 'NO'
	 */
	public static String PROPERTY_AUTOMATIC_PAGE_ADD="PROPERTY_AUTOMATIC_PAGE_ADD";
}
