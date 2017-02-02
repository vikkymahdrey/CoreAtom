/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.agiledge.atom.tasks.dbconnect;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * 
 * @author Shahid
 */
public class ConnectDB {

	public static Connection con1 = null;
	public String DBusername;
	public String DBpassword;
	public String driver;
	public String location;

	public ConnectDB(String DBusername, String DBpassword, String location,
			String driver) {
		this.DBusername = DBusername;
		this.DBpassword = DBpassword;
		this.driver = driver;
		this.location = location;

		try {
			Class.forName(driver);
			String urll = location + ";user=" + DBusername + ";password="
					+ DBpassword + ";";
			con1 = DriverManager.getConnection(urll);

		} catch (Exception e) {
			System.out.println("Exception On Connection : " + e);
			// return false;
		}
	}

}
