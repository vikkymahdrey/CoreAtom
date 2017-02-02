/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.agiledge.atom.task.dao;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import com.agiledge.atom.commons.OtherFunctions;
import com.agiledge.atom.commons.PropertyClass;
import com.agiledge.atom.dto.EmployeeDto;
import com.agiledge.atom.dto.ProjectDto;
import com.agiledge.atom.tasks.dbconnect.ConnectDB;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;



/**
 * 
 * @author Administrator
 */
public class GetProject {
	/*
	 * To change this template, choose Tools | Templates and open the template
	 * in the editor.
	 */

	/**
	 * 
	 * @author Administrator
	 */
	public static String DBusername;
	public static String DBpassword;

	public static String location;
	public static String driver;
	ArrayList<ProjectDto> projectlist = null;
	public HashMap<String,ProjectDto> getProjectJSON() {
	
		
		HashMap<String,ProjectDto> projects = new HashMap<String, ProjectDto>();
		try {
		ClientConfig config = new DefaultClientConfig();
		Client client = Client.create(config);
		WebResource webResource = client
				.resource("https://www.peopleworks.ind.in/pwwebApi/api/Logon");
		// WebResource webResource =
		// client.resource("https://www.peopleworks.ind.in/PWWEbApi/api/LogOn/put");
		JSONObject js = new JSONObject();
		// String username = "CD\\transport";
		// String password = "transportATOM!23";
		//String username = "ps1\\Cd16";
		//String password = "Passw0rd!";
		String username="cd\\TR01";
        String password="Atom@user123";
		js.put("ClientCodeSlashName", username);
		js.put("Password", password);
		ClientResponse response = webResource.type(
				MediaType.APPLICATION_JSON).put(ClientResponse.class,
				js.toString());	
		System.out.println("Status" + response.getStatus());
		JSONObject jso = new JSONObject(response.getEntity(String.class));
		String sessionId = (String) jso.get("sessionID");
		DefaultHttpClient client1 = new DefaultHttpClient();
		HttpGet request = new HttpGet("https://www.peopleworks.ind.in/PWWEbApi/(X(1)S("+sessionId+"))/Api/EmloyeeDatas/GetEmployeeDetails/"+ sessionId);

		HttpResponse response1 = client1.execute(request);
		// System.out.println("nnnnnnnnnnnnn"+response1.getEntity().getContent());
		BufferedReader rd = new BufferedReader(new InputStreamReader(
				response1.getEntity().getContent()));
		String line = "";
		String nLine = "";
		while ((line = rd.readLine()) != null) {
			nLine += line;
		}
		String emps="";
		// System.out.println("aaa"+nLine);
		JSONArray jsa = new JSONArray(nLine);		
		for (int i = 0; i < jsa.length(); i++) {
			JSONObject emp = jsa.getJSONObject(i);
			try {
				ProjectDto dto = new ProjectDto();
				dto.setProject(emp.getString("ProcessName"));
				dto.setDescription(emp.getString("ProcessName"));
				dto.setProjectUnit(emp.getString("DepartmentName"));
				projects.put(emp.getString("ProcessName"), dto);
			} catch (Exception ee) {
				System.out.println("aaaa" + ee);
			}			
		}
	} catch (Exception e) {
		e.printStackTrace();
		System.out.println("ERRRoin getProject" + e);
	} finally {
	}
	return projects;
}
	public ArrayList<ProjectDto> getProject() {
		DBusername="root";
		DBpassword="Atom@dbserver159$$";
		location="jdbc:mysql://localhost:3306/Project";
		driver = "com.mysql.jdbc.Driver";

		PropertyClass prop = new PropertyClass();
		if (prop.readDBProperty("aggressodb.property")) {
			driver = prop.getDriver();
			DBusername = prop.getUserName();
			DBpassword = prop.getPassword();
			location = prop.getUrl();
			// System.out.println("Reading from file");
		}

		ProjectDto projectDtoObj = null;
		Statement st;
		ResultSet rs = null;
		rs = null;
		st = null;
		try {
			projectlist = new ArrayList<ProjectDto>();
			// ConnectDB.con1=null;
			if (ConnectDB.con1 == null) {
				new ConnectDB(DBusername, DBpassword, location, driver);
			}
			st = ConnectDB.con1.createStatement();
			rs = st.executeQuery("select  distinct atsproject.project,atsproject.description from atsproject");
			while (rs.next()) {
				projectDtoObj = new ProjectDto();
				projectDtoObj.setProject("" + rs.getString("project"));
				projectDtoObj.setDescription("" + rs.getString("description"));
				projectlist.add(projectDtoObj);
			}
			rs.close();
			st.close();
			ConnectDB.con1.close();
			ConnectDB.con1 = null;
		} catch (Exception e) {
			System.out.println("ERRRoin Project" + e);

			try {
				ConnectDB.con1.close();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		return projectlist;
	}

}
