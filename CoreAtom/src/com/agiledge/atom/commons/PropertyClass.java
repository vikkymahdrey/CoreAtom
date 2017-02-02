package com.agiledge.atom.commons;

import java.io.FileInputStream;
import java.util.Properties;

public class PropertyClass {

	private String url;
	private String userName;
	private String password;
	private String driver;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getDriver() {
		return driver;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}

	public boolean readDBProperty() {
		boolean returnflag = false;
		try {
			Properties prop = new Properties();
			prop.load(new FileInputStream(System.getProperty("user.home")
					+ "/mydb.property"));

			String host = prop.getProperty("url").toString();
			String username = prop.getProperty("username").toString();
			String password = prop.getProperty("password").toString();
			String driver = prop.getProperty("driver").toString();

			setDriver(driver);
			setUrl(host);
			setUserName(username);
			setPassword(password);

			returnflag = true;
			// System.out.println("File Includes :" + host + "\n " + username +
			// "\n " + driver + "\n Password: "+ password);
			// System.out.println("property file read");
		} catch (Exception e) {
			returnflag = false;
		}

		return returnflag;

	}

	public boolean readDBProperty(String fileName) {
		boolean returnflag = false;
		try {
			Properties prop = new Properties();
			prop.load(new FileInputStream(System.getProperty("user.home") + "/"
					+ fileName));

			String host = prop.getProperty("url").toString();
			String username = prop.getProperty("username").toString();
			String password = prop.getProperty("password").toString();
			String driver = prop.getProperty("driver").toString();

			setDriver(driver);
			setUrl(host);
			setUserName(username);
			setPassword(password);

			returnflag = true;
			// System.out.println("File Includes :" + host + "\n " + username +
			// "\n " + driver + "\n Password: "+ password);
			// System.out.println("Property file read");
		} catch (Exception e) {
			returnflag = false;
		}

		return returnflag;

	}

}
