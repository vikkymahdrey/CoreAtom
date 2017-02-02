/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.agiledge.atom.servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.agiledge.atom.commons.OtherFunctions;
import com.agiledge.atom.constants.SettingsConstant;
import com.agiledge.atom.dao.DistanceListDao;
import com.agiledge.atom.dao.EmployeeDao;
import com.agiledge.atom.dao.ValidateUser;
import com.agiledge.atom.dto.EmployeeDto;
import com.agiledge.atom.service.EmployeeService;

/**
 * 
 * @author Shahid
 */
public  class UserValidation extends HttpServlet {
	/*
	 * public static String DBusername; public static String DBpassword; public
	 * static String instance; public static String location; public static
	 * String DBname;
	 * 
	 * @Override public void init(ServletConfig config) throws ServletException
	 * { super.init(config); DBusername = config.getInitParameter("DBuserName");
	 * DBpassword = config.getInitParameter("DBpassword"); instance =
	 * config.getInitParameter("instance"); location =
	 * config.getInitParameter("location"); DBname =
	 * config.getInitParameter("DBname"); }
	 */

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
	 * methods.
	 * 
	 * @param request
	 *            servlet request
	 * @param response
	 *            servlet response
	 * @throws ServletException
	 *             if a servlet-specific error occurs
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	protected  void processRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(true);
		boolean needToChangePwd = false;
		String username = request.getParameter("uname") == null ? "" : request
				.getParameter("uname");
		String password = request.getParameter("pass") == null ? "" : request
				.getParameter("pass");
		String sessionId = request.getParameter("id");
		String userId = request.getParameter("Userid");
		String clientCode=request.getParameter("clientCode");
		String prePage = "" + request.getParameter("prePage");
		boolean status=false;
		ValidateUser ob = new ValidateUser();
		if(sessionId!=null && userId!=null && clientCode!=null)
		{
			System.out.println("dsdssddsds"+userId);
			System.out.println("sessionid"+sessionId);
			System.out.println("clientcode"+clientCode);
			/*System.out.println(userId.split("\\\\")[1]);
			userId=userId.split("\\\\")[1];
			System.out.println("1111111");*/
			status=validateUserWithAPI(sessionId, userId,clientCode);
			if(!status)
			{
			response.sendRedirect("index.jsp?message=Invalid User Name/Password !");
			return;
			}
			else 
			{
				status = ob.authenticateUserWIthSSO(userId);
			}	
		}
		else if (username.equals("") || password.equals("")) {
			response.sendRedirect("index.jsp?message=Invalid User Name/Password !");
			return;
		} else {			
			ob.SetUserNamePassword(username, password);
			status = ob.checkUser();
		}
			if (status) {
				String role = ob.role;
				String roleId = ob.roleId;
				session.setAttribute("user", ob.uid);
				EmployeeDto empDto = new EmployeeService()
						.getEmployeeAccurate(session.getAttribute("user")
								.toString());
				session.setAttribute("userDto", empDto);

				EmployeeDto emp = new EmployeeDao()
						.getEmployeeById(ob.uid);				
				if (emp!=null && emp.getExternaluser()!=null && emp.getExternaluser().equalsIgnoreCase("yes")) {
					if (emp.getPasswordchangedate() == null
							|| emp.getPasswordchangedate().equals("")) {
						needToChangePwd = true;
					} else if (new OtherFunctions().checkDate90(emp
							.getPasswordchangedate())) {
						needToChangePwd = true;
					}
				}
				if (ob.delegatedUId != null
						&& (!ob.delegatedUId.equals("null"))
						&& (!ob.delegatedUId.equals(""))) {
					session.setAttribute("delegatedId", ob.delegatedUId);
					role = ob.delegatedUrole;
					roleId = ob.delegatedUroleId;
				}
				try {
				empDto.setUserType(role);
				empDto.setRoleId(Integer.parseInt( roleId));
				}catch(Exception e) {
					System.out.println("Error in setting roles and roleid :"+ e);
				}

				System.out.println(" role: " + role);

				session.setAttribute("role", role);
				session.setAttribute("roleId", roleId);
				if (ob.site != null && !ob.site.trim().equals("")) {
					session.setAttribute("site", ob.site);
				}
				String indexPage = "";
				try {
					System.out.println("prepage : "+ prePage);
					indexPage = prePage.substring(0, prePage.indexOf("?"));
				} catch (Exception e) {
					 System.out.println("prepage ? : "+ prePage);
				}
				System.out.println("Status        "+empDto.getRegisterStatus());
				if (needToChangePwd) {
					System.out.println("IN Need to chANGE");
					session.setAttribute("username", username);
					System.out.println("IN Need to chANGE"
							+ session.getAttribute("user"));
					session.setAttribute("password", password);
					System.out.println("IN Need to chANGE"
							+ session.getAttribute("role"));
					response.sendRedirect("changePassword.jsp");
					
				} else if (empDto.getRegisterStatus()==null||!empDto.getRegisterStatus().equals(
						SettingsConstant.ACTIVE_STATUS)) {
					request.getRequestDispatcher("register_employee.jsp?loginId="+username+"").forward(request,response);
				} else if (prePage == null || prePage.equals("null")
						|| prePage.equals("") || indexPage.equals("index.jsp")) {					
					/*
					if (role.equalsIgnoreCase("emp")) {
						response.sendRedirect("employee_home.jsp");
					}
					if (role.equalsIgnoreCase("hrm")) {
						response.sendRedirect("manager_home.jsp");
					}
					if (role.equalsIgnoreCase("tm")) {
						response.sendRedirect("transportmanager_home.jsp");
						// request.getRequestDispatcher("WEB-INF/check.jsp").forward(request,
						// response);
					}
					if (role.equalsIgnoreCase("ta")) {
						response.sendRedirect("transportmanager_home.jsp");
						// request.getRequestDispatcher("WEB-INF/check.jsp").forward(request,
						// response);
					}
					if (role.equalsIgnoreCase("hq")) {
						response.sendRedirect("admin_home.jsp");
					}
					*/
					if (role.equalsIgnoreCase("v")) {
						System.out.println("INNNNNN" + role + ob.vendorId);
						session.setAttribute("vendorCompany", "1");
						response.sendRedirect("vendors_home.jsp");
					}
					else
					{
					response.sendRedirect("employee_home.jsp");
					}
				} else {
					response.sendRedirect(prePage);
				}

			} else {

				response.sendRedirect("index.jsp?message=Invalid User Name/Password !");
				return;
			}
		

	}

	// <editor-fold defaultstate="collapsed"
	// desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
	/**
	 * Handles the HTTP <code>GET</code> method.
	 * 
	 * @param request
	 *            servlet request
	 * @param response
	 *            servlet response
	 * @throws ServletException
	 *             if a servlet-specific error occurs
	 * @throws IOException
	 *             if an I/O error occurs
	 */

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		processRequest(request, response);

	}

	/**
	 * Handles the HTTP <code>POST</code> method.
	 * 
	 * @param request
	 *            servlet request
	 * @param response
	 *            servlet response
	 * @throws ServletException
	 *             if a servlet-specific error occurs
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
		//float dsit=new DistanceListDao().getGooglMapDistance("7","16");
		//System.out.println(dsit);
	}

	/**
	 * Returns a short description of the servlet.
	 * 
	 * @return a String containing servlet description
	 */
	@Override
	public String getServletInfo() {
		return "Short description";
	}// </editor-fold>
	
	
	public boolean validateUserWithAPI(String sessionId,String userId,String clientCode)
	{
		boolean result=false;
try{	
	String reqString="https://www.peopleworks.ind.in/pwwebapi/api/Validation/validateTravelUser?id="+URLEncoder.encode(sessionId, "UTF-8")+"&userId="+userId+"&clientCode="+clientCode;
	DefaultHttpClient client1 = new DefaultHttpClient();	
	//String reqString="https://www.peopleworks.ind.in/pwwebapi/(X(1)S("+sessionId+"))/api/validation/validateTravelUser?id="+sessionId+"&userId="+username;
//	String reqString="http://192.168.5.110/api/Validation/validateTravelUser?id="+sessionId+"&userId=PR102";
//	String reqString="http://192.168.5.110/api/EmloyeeDatas/GetEmployeeDetails/"+ sessionId;
//	String reqString="http://192.168.5.110/api/EmloyeeDatas/GetEmployeeDetails/e3x32umrp1gmjmtbrx2r0b3i";			
	//System.out.println(request);
	//reqString="https://www.peopleworks.ind.in/pwwebapi/api/validation/validateTravelUser?id="+URLEncoder.encode(sessionId, "UTF-8")+"&userId="+userId;
	//reqString="http://192.168.5.19/pwwebapi/api/Validation/validateTravelUser/?id="+URLEncoder.encode(sessionId, "UTF-8")+"&userId="+userId;
	//reqString="https://180.179.182.8/pwwebapi/api/Validation/validateTravelUser/?id="+URLEncoder.encode(sessionId, "UTF-8")+"&userId="+userId+"&clientCode="+clientCode;
	HttpGet request = new HttpGet(reqString);
	System.out.println("link"+reqString);
	HttpResponse response1 = client1.execute(request);
	System.out.println(response1.getStatusLine());
	BufferedReader rd = new BufferedReader(new InputStreamReader(
			response1.getEntity().getContent()));
	String line = "";
	String nLine = "";
	while ((line = rd.readLine()) != null) {
		nLine += line;
	}
	System.out.println(nLine);
	if(nLine.equalsIgnoreCase("true"))
	{
	result= true;	
	}
	
	
} catch (Exception e) {
	System.out.println("Error" + e);
}	
return result;
}
}
