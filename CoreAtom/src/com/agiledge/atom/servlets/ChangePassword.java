package com.agiledge.atom.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.agiledge.atom.dao.EmployeeDao;

/**
 * Servlet implementation class CheckPassword
 */
public class ChangePassword extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ChangePassword() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stubtry{
		try{
		HttpSession session =request.getSession();
		int loginid=Integer.parseInt(session.getAttribute("user").toString());
		String password=request.getParameter("pwd");
        int returnInt=new EmployeeDao().changePassword(loginid, password);
        if (returnInt==1) {
			session.setAttribute("status",
					"<div class='success'>Password Updated Successfully</div");
		} else {
			session.setAttribute("status",
					"<div class='failure'>Password Updation Failed</div");
		}
        response.sendRedirect("index.jsp");
	    }catch(Exception e){
	    	System.out.println("error"+e);
	    	
	    }
		
	}

}
