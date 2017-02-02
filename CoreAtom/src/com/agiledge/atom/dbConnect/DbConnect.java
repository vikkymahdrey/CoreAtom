/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.agiledge.atom.dbConnect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import javax.sql.DataSource;

import org.apache.commons.dbcp.ConnectionFactory;
import org.apache.commons.dbcp.DriverManagerConnectionFactory;
import org.apache.commons.dbcp.PoolableConnectionFactory;
import org.apache.commons.dbcp.PoolingDataSource;
import org.apache.commons.pool.impl.GenericObjectPool;

import com.agiledge.atom.commons.PropertyClass;
import com.agiledge.atom.constants.SettingsConstant;
import com.agiledge.atom.dto.DriverDto;


public class DbConnect {
	private static Timer  timer =null;
	private String url = "";
	private String userName = "";
	private String password = "";
	private String driver = "";
	private DataSource dataSource;
	private GenericObjectPool  connectionPool; 
	private static DbConnect instance = null;
	private static Set<Integer> ConnectHashCodeSets=new HashSet<>();
	
	static
	{
	//	System.out.println("Static Block of CommonDao");
		SettingsConstant.setCompany();
		 timer = new Timer();
		 timer.scheduleAtFixedRate(new TimerTask(){
					 		public void run()
					 		{
				  
					 					 
					 						System.out.println("Hureyyy its time event");
					 						try{
					 							 DbConnect db= DbConnect.getInstance();
					 							Connection con= db.connectDB();;
					 							
					 							
 
					 							if (con != null) {
					 								try {
					 									testTransaction();
					 									testTransaction();
					 									con.close();
					 								} catch (SQLException ex) {
					 									System.out.println("Error while closing connection in Timer " + ex);
					 								}
					 							}

					 							 
					 							DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
					 							Date date = new Date();
					 							System.out.println(dateFormat.format(date));
					 						}
					 						
					 						catch (Exception e) {
												// TODO: handle exception
					 							System.out
														.println(" Exception in timer while closing connection " + e);
					 							
					 							 
											}
					 							 
					 						 
			     
			  
			  
			  
			  
					 			}},0,1000*60*60*10);
					 			 
			  
	}
	

	private void loadDriverProperties() {


		//url="jdbc:mysql://localhost:3306/atomcoreamat";		
		//url="jdbc:mysql://180.179.227.35:3306/atomcorecdo";		

//		url="jdbc:mysql://localhost:3306/atomcoreamat";		


		url=SettingsConstant.url;		

		userName="root";
		password="root";
		System.out.println(" URL =" + url );
		//url = "jdbc:sqlserver://localhost:1433; Instance=SQLEXPRESS;"
			//	+ "databaseName=atomcore;";
		//userName = "atomdba";
		//password = "atomdba";
		driver = "com.mysql.jdbc.Driver";
		PropertyClass prop = new PropertyClass();
 
  
		 if (prop.readDBProperty(SettingsConstant.propertyFile)) {
 //if (prop.readDBProperty("cddbsiemens.property")) {

//		if (prop.readDBProperty("cddb1.property")) {
// if (prop.readDBProperty("siemensdb__1.property")) {


/*		if (prop.readDBProperty("siemensdba.property")) {
 */
 
			url = prop.getUrl();
			userName = prop.getUserName();
			password = prop.getPassword();
			driver = prop.getDriver();

		}
		 
System.out.println("URL : " + url);
	}

	private DbConnect() {

		loadDriverProperties();
		dataSource = getDataSource();
	}
	private void resetPool(){
		loadDriverProperties();
		dataSource = getDataSource();
	}

	public static DbConnect getInstance() {
		if (instance == null) {
			instance = new DbConnect();

		}
		return instance;
	}

	private DataSource getDataSource() {

		// loading driver

		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			System.out.println("Error while loading driver : " + e);
		}

		// set up pooling datasource

	DataSource dataSource1 = setupDataSource(url, userName, password);
	
		return dataSource1;
	}

	// ........................

	private DataSource setupDataSource(String host, String userName,
			String password) {
		  connectionPool = new GenericObjectPool(null);
		connectionPool.setMaxActive(10);
connectionPool.setMaxWait(10000);
 
		ConnectionFactory connectionFactory = new DriverManagerConnectionFactory(
				host, userName, password);

		PoolableConnectionFactory poolableConnectionFactory = new PoolableConnectionFactory(
				connectionFactory, connectionPool, null, null, false, true);

		PoolingDataSource dataSource1 = new PoolingDataSource(connectionPool);

		return dataSource1;

	}

	// ......................

	public Connection connectDB() {
		Connection con = null;
		try {

			// System.out.println("connectDB");
			con = dataSource.getConnection();
			ConnectHashCodeSets.add(con.hashCode());
			
			
			// System.out.println("Connection Size: " + ConnectHashCodeSets.size()+"  Active COn count    "+connectionPool.getNumActive()+"     "+new Date());

		} catch(SQLException ex){
			try{
				connectionPool.clear();
			connectionPool.close();
			
			}
			catch (Exception e) {
				// TODO: handle exception
				System.out.println(" unable to close pool :"+e);
			}
			 resetPool();
		}
		catch (Exception e) {
			System.out.println("Error :" + e);
			//resetPool();
		}
		return con;

	}

	public static void closeConnection(Connection con) {
		if (con != null) {
			try {
				con.close();
			} catch (SQLException ex) {
				System.out.println("Error while closing connection " + ex);
			}
		}
	}

	public static void closeStatement(Statement... st) {
		for (Statement st1 : st) {
			if (st1 != null) {
				try {
					st1.close();
				} catch (SQLException ex) {
					System.out.println("Error while closing statement " + ex);
				}
			}

		}
	}

	public static void closeResultSet(ResultSet... rs) {
		for (ResultSet rs1 : rs) {
			if (rs1 != null) {
				try {
					rs1.close();
				} catch (SQLException ex) {
					System.out.println("Error while closing ResultSet " + ex);
				}
			}

		}

	}
	
	private static void testTransaction() {

		DbConnect ob = DbConnect.getInstance();
		PreparedStatement pst = null;
		ResultSet rs = null;
		Connection con = ob.connectDB();
		 
		DriverDto driverDto = null;

		try {
			String query = "select * from settings ";
			
			 
			pst = con.prepareStatement(query);
			 
			rs = pst.executeQuery();
			if(rs.next()) {
 		
				 ;
			}

		} catch (Exception e) {
			System.out.println("ERROR" + e);
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);
		}

		

	}
	
	@Override
	protected void finalize() throws Throwable {
		// TODO Auto-generated method stub
		timer.cancel();
		super.finalize();
	}
	
}
