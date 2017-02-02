package com.agiledge.atom.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.mail.Session;
import javax.servlet.GenericServlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.agiledge.atom.dao.EmergencyAPLFetcherDAO;

public class EmergencyAPLFetcher extends HttpServlet {
		
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public EmergencyAPLFetcher() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest req,
			HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
	}

	
		protected void doPost(HttpServletRequest req, HttpServletResponse resp)
				throws ServletException, IOException {
			// TODO Auto-generated method stub
			System.out.println("i m in sev,ettttttttttttttttttttttttttttttttttttt");
			PrintWriter out = resp.getWriter();
			int employeeID = Integer.parseInt(req.getParameter("eid"));
			EmergencyAPLFetcherDAO eme = new EmergencyAPLFetcherDAO();
			String[] apl=eme.getAPL(employeeID);
			String aplgot = apl[0]+"$"+apl[1]+"$"+apl[2]+"$"+apl[3]+"$"+apl[4];
			 System.out.println("DATA"+ aplgot);
			 out.print(aplgot);
		

		
	}

}
