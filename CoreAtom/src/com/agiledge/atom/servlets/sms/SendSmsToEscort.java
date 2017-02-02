package com.agiledge.atom.servlets.sms;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.agiledge.atom.sms.SMSService;

/**
 * Servlet implementation class SendSmsToEscort
 */
public class SendSmsToEscort extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SendSmsToEscort() {
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
		// TODO Auto-generated method stub
		try{
			System.out.println("......................");
			System.out.println("data " + request.getParameter("trip"));
			SMSService sms= new SMSService();
			
			 
			response.getWriter().write(  sms.sendPasswordToEscortGetAck ( request.getParameter("trip"))); 
		}catch(Exception e) {
			response.getWriter().write("Error : " + e);
		}

	}

}
