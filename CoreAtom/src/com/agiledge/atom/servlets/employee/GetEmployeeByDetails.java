package com.agiledge.atom.servlets.employee;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.agiledge.atom.dao.EmployeeDao;
import com.agiledge.atom.dto.EmployeeDto;
import com.agiledge.atom.service.EmployeeService;

public class GetEmployeeByDetails extends HttpServlet{

	protected void processRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		try {
			/*
			 * TODO output your page here out.println("<html>");
			 * out.println("<head>");
			 * out.println("<title>Servlet GetEmployeeDetails</title>");
			 * out.println("</head>"); out.println("<body>");
			 * out.println("<h1>Servlet GetEmployeeDetails at " +
			 * request.getContextPath () + "</h1>"); out.println("</body>");
			 * out.println("</html>");
			 */

			EmployeeDao dao = new EmployeeDao();
String isSub=request.getParameter("isSub");
			String data="";
			
			if(isSub!=null)
			{
				System.out.println("reached");
				String empID = request.getParameter("empID");
				EmployeeDto dto=new EmployeeService().getEmployeeToSubscribe(empID);
				if(dto!=null)
				{
					data+=dto.getSite()+"|";
					data+= dto.getManagerName()+"|";
					data+=dto.getLineManager() + "|";					
					data+=dto.getSpocName() + "|";	
					data+=dto.getSpoc_id() + "|";
					data+=dto.getContactNo();		
					out.write(data);
				}
			}
			else
			{
			String empName = request.getParameter("empid");
			// System.out.println(" Text " + empName);
			// System.out.println(" Attr " +
			// request.getAttribute("supervisor1Text").toString());
			EmployeeDto dto = dao.getEmployeeAccurate(request
					.getParameter("empid"));
			String employeePipe = "";

			employeePipe = dto.getPersonnelNo() + ":" + dto.getDeptName();

			// System.out.println("List : " + employeePipe);
			out.write(employeePipe);
			}
		} finally {
			//out.close();
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
