package com.agiledge.atom.task.dao;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import com.agiledge.atom.commons.OtherFunctions;
import com.agiledge.atom.dto.EmployeeDto;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;


public class GetEmpsJSON extends GetEmps {
	ArrayList<EmployeeDto> emplist = null;
	EmployeeDto empobj = null;
	final String COUNTRYCODE = "IN";
	final String ACTIVE = "1";
	public static String DBusername;
	public static String DBpassword;
	public static String location;
	public static String driver;
	public ArrayList<EmployeeDto> getEmps() {
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
			NewCookie cookies = response.getCookies().get(0);
			JSONObject jso = new JSONObject(response.getEntity(String.class));
			String sessionId = (String) jso.get("sessionID");
			DefaultHttpClient client1 = new DefaultHttpClient();
			/*
			HttpGet request = new HttpGet(
					"https://www.peopleworks.ind.in/PWWEbApi/Api/EmloyeeDatas/GetEmployeeDetails/"
							+ sessionId);
			
			*/

			HttpGet request = new HttpGet("https://www.peopleworks.ind.in/PWWEbApi/(X(1)S("+sessionId+"))/Api/EmloyeeDatas/GetEmployeeDetails/"+ sessionId);
			request.addHeader("Cookie", cookies.toString());
			
			HttpResponse response1 = client1.execute(request);
			BufferedReader rd = new BufferedReader(new InputStreamReader(
					response1.getEntity().getContent()));
			String line = "";
			String nLine = "";
			while ((line = rd.readLine()) != null) {
				nLine += line;
			}
			// System.out.println("aaa"+nLine);
			JSONArray jsa = new JSONArray(nLine);
/*		try{
		 ClientConfig config = new DefaultClientConfig();
		        Client client = Client.create(config);
			         WebResource webResource = client.resource("https://www.peopleworks.ind.in/pwwebApi/api/Logon");
			        JSONObject js=new JSONObject();
			 		js.put("ClientCodeSlashName", "ps1\\Cd16");
			 		js.put("Password", "Passw0rd!");
			       ClientResponse response = webResource.type(MediaType.APPLICATION_JSON).put(ClientResponse.class, js.toString());
			       JSONObject empArray=new JSONObject(response.getEntity(String.class));
			         System.out.println();
			 }catch(Exception e)
			 {
				System.out.println("Error"+e); 
			 }

		
 	
		try {
*/
			emplist = null;
			emplist = new ArrayList<EmployeeDto>();
			for (int i = 0; i < jsa.length(); i++) {
				JSONObject emp = jsa.getJSONObject(i);
if(emp.getString("IsResigned").equalsIgnoreCase("false"))
		{
				empobj = new EmployeeDto();
				empobj.setEmployeeFirstName(emp.get("FirstName").toString());
				empobj.setEmployeeMiddleName(emp.get("MidName").toString());
				empobj.setEmployeeLastName(emp.get("LastName").toString());
				empobj.setLoginId(emp.get("EmployeeID").toString());
				empobj.setGender((emp.getString("Gender").toString()));
				empobj.setPersonnelNo(emp.get("EmployeeID").toString());
				empobj.setEmailAddress(emp.get("OfficeEmail").toString());
				empobj.setHomeCountry("IN");
				empobj.setLineManager(emp.get("ROEmployeeID").toString());
				empobj.setStaffManager(emp.get("REVEmployeeID").toString());
				// empobj.setClientServiceManager(rs.getString("ClientServiceManager"));
				empobj.setCareerLevel("1");
				// empobj.setCareerPathwayDesc(emp.get("CareerPathwayDesc"));
				empobj.setBusinessUnitDescription(emp.get("ProcessName")
						.toString());
				empobj.setContactNo(emp.get("ContactNumber").toString());
				empobj.setDeptno(emp.get("DepartmentName").toString());
				empobj.setDeptName(emp.get("DepartmentName").toString());
				empobj.setOperationCode(emp.get("DivisionName").toString());
				empobj.setOperationDescription(emp.get("DivisionName")
						.toString());
				// empobj.setPathways(emp.get("Pathways"));
				empobj.setDateOfJoining(OtherFunctions
						.changeDateFromatToIso(emp.get("DOJ").toString()));
				empobj.setActive("1");
				String address = emp.get("PresentAddress").toString();
				empobj.setAddress(address);
				empobj.setCity(emp.get("PresentCity").toString());
				empobj.setDisplayName(emp.get("FirstName").toString()+" "+emp.get("MidName").toString()+" "+emp.get("LastName").toString());
				empobj.setProjectUnit(emp.getString("DepartmentName"));
				empobj.setDesignationName(emp.getString("DesignationName"));
				empobj.setState(emp.getString("PresentState"));
				empobj.setProjectid(emp.getString("ProcessName"));
				System.out.println("Emp Name:"+empobj.getEmployeeFirstName());
				emplist.add(empobj);	
		}
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("ERRRoin getemps" + e);
		} finally {
		}		 
		return emplist;
	}
 
}
