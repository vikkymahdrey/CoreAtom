package com.agiledge.atom.servlets.employee;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import com.agiledge.atom.commons.OtherFunctions;
import com.agiledge.atom.service.EmployeeService;

/**
 * Servlet implementation class CheckLoginId
 */

public class CheckLoginId extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CheckLoginId() {
        super();
        // TODO Auto-generated constructor stub
    }
    protected void processRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
    	try{
    		System.out.println("Insde CheckLoginId");
    String loginid=request.getParameter("loginid");
    String personnelNo=request.getParameter("personnelNo");
    String result="here";
    boolean flag;
    if(OtherFunctions.isEmpty(loginid)==false) {
    flag=new EmployeeService().checkLoginId(loginid);
    if(flag==true)
    {
    	result="true";
    }
    else
    {
    	result="false";
    }
    } else if(OtherFunctions.isEmpty(personnelNo)==false) {
	        flag=new EmployeeService().checkPersonnelNo(personnelNo);
	        if(flag==true)
	        {
	        	result="true";
	        }
	        else
	        {
	        	result="false";
	        }
    }
    System.out.println("result"+result);
    JSONObject data = new JSONObject();
    data.put("result", result);
    response.setContentType("application/json");
	PrintWriter out = response.getWriter();
	out.write(data.toString());
	out.flush();
    }catch(Exception e){
    	System.out.println("error"+e);
    	
    }
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
      processRequest(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
      processRequest(request, response);
	}

}
