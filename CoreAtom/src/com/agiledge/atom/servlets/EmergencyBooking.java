package com.agiledge.atom.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.agiledge.atom.dbConnect.DbConnect;

public class EmergencyBooking extends HttpServlet{
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public EmergencyBooking() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		response.setContentType("text/html");  
		PrintWriter out = response.getWriter(); 
		
		
		DbConnect ob = DbConnect.getInstance();
		Connection con = ob.connectDB();
		PreparedStatement pst = null;
		try {
			   
			    String siteId=request.getParameter("siteId");
				String type = request.getParameter("chosenVehicleType");
				String vehicle = request.getParameter("selectVehicle");
				String bookingFor = request.getParameter("employeeName");
				String reason = request.getParameter("reason");
				
		System.out.println("Hiiiiiiiiiiiiiisiteid"+siteId);
		System.out.println("bookingFor"+bookingFor);
		System.out.println("type"+type);
		System.out.println("vehicle"+vehicle);
		System.out.println("reason"+reason);
		
		String query="insert into medical_emergency(siteId,bookingFor,type,vehicle,reason) values(?,?,?,?,?)";	
		pst = con.prepareStatement(query);
				 
				System.out.println("hi insert query"+query);	
		          pst.setString(1,siteId);        
		          pst.setString(2,bookingFor);
		          pst.setString(3,type);
		          pst.setString(4,vehicle);
		          pst.setString(5,reason);
		         
		    System.out.println();    
		          int i = pst.executeUpdate();
		          if(i>0)  
		        	  out.print("You are successfully Booked Transportation Services...");  
		          con.commit(); 
		       
		}
	 catch (Exception e) {
		System.out.println(" Error in Emergency transportation services");
		 out.print("Error in Emergency transportation services..."); 
	} finally {
		
		DbConnect.closeStatement(pst);
		DbConnect.closeConnection(con);
	}

	}

	
		
		

			
	
}
