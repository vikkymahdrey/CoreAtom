/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.agiledge.atom.servlets.subscription;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.agiledge.atom.dto.EmployeeDto;
import com.agiledge.atom.dto.EmployeeSubscriptionDto;
import com.agiledge.atom.service.EmployeeService;
import com.agiledge.atom.service.EmployeeSubscriptionService;


//import javax.servlet.annotation.WebServlet;

/**
 * 
 * @author 123
 */
public class GetLastSubscriptionDetails_Ajax extends HttpServlet {

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
	protected void processRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		try {
			String data="";
			String empID=request.getParameter("employeeID");
			
			
				EmployeeSubscriptionDto dto=new EmployeeSubscriptionService().getEmployeeSubscriptionDetails(empID);
		
			if(dto!=null)
			{
				data= dto.getSubscriptionFromDate() + "|";
				data+=dto.getSupervisor().getEmployeeFirstName() + "|";
				data+=dto.getSupervisor().getEmployeeID() + "|";
				data+=dto.getSpoc().getEmployeeFirstName() + "|";			
				data+=dto.getSpoc().getEmployeeID() + "|";
				data+=dto.getApl().getArea() + "|";
				data+=dto.getApl().getPlace() + "|";
				data+=dto.getApl().getLandMark() + "|";
				data+=dto.getApl().getLandMarkID() + "|";
				data+=dto.getSite() + "|";
				data+=dto.getContactNo() + "|";
				data+=dto.getSubscriptionID();								
			}
						
			System.out.println(data);
			out.write(data);
		}catch(Exception e)
		{
			System.out.println("Errror "+ e);
		}
		finally {
			out.close();
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

}
