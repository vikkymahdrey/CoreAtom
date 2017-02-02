package com.agiledge.atom.servlets;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class RoutingCheck
 */
 
public class RoutingCheck extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RoutingCheck() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		try {
//			writeFile("********* 1");
//			writeFile ("routeStatus " + request.getSession().getAttribute("routeStatus") );
//			writeFile( "routeStatus to String : " + request.getSession().getAttribute("routeStatus").toString() );
			if( request.getSession().getAttribute("routeStatus")!=null && request.getSession().getAttribute("routeStatus").toString().equalsIgnoreCase("started") ) {
//				writeFile("********* 2");
				response.getWriter().print("started");
				System.out.println("_____________________________________________________________");
//				writeFile("started");
			} else if( request.getSession().getAttribute("routeStatus")!=null && request.getSession().getAttribute("routeStatus").toString().equalsIgnoreCase("finished") ) {  
//				writeFile("********* 3");
				System.out.println("___________________________finished__________________________________");
				response.getWriter().print(request.getSession().getAttribute("routingData"));
//				writeFile("finished");
			} else {
//				writeFile("********* 4");
				response.getWriter().print("No Routing Done");
//				writeFile("no routing done");
			}
			
		} catch(Exception e) {
//			writeFile("********* 5");
			System.out.println("Errror .......................... " +e);
			response.getWriter().print("error " +e );
		
		}
	}
	
	private void writeFile(String data) {
		   String fileName = System.getProperty("user.home") + "/log.txt"; 
		   try(PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(fileName, true)))) {
			    
			    out.println(  data+ "\r\n");
			}catch (IOException e) {
			    //exception handling left as an exercise for the reader
			}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}
	
	

}
