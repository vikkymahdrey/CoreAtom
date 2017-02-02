/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.agiledge.atom.servlets.subscription;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.agiledge.atom.dto.EmployeeDto;
import com.agiledge.atom.service.EmployeeService;


//import javax.servlet.annotation.WebServlet;

/**
 * 
 * @author 123
 */
public class GetEmployeeDataFromSubscription extends HttpServlet {

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

			// System.out.println(" inside GetEmployee Data..........");
			/*
			 * TODO output your page here out.println("<html>");
			 * out.println("<head>");
			 * out.println("<title>Servlet GetEmployeeData</title>");
			 * out.println("</head>"); out.println("<body>");
			 * out.println("<h1>Servlet GetEmployeeData at " +
			 * request.getContextPath () + "</h1>"); out.println("</body>");
			 * out.println("</html>");
			 */

			EmployeeDto dto = new EmployeeDto();

			dto.setEmployeeID(request.getParameter("employeeID").toString());
			dto.setEmployeeFirstName(request.getParameter("firstName")
					.toString());
			dto.setEmployeeLastName(request.getParameter("lastName").toString());
			if (request.getParameter("lastName") == null
					|| request.getParameter("lastName").toString().equals("")) {
				dto.setEmployeeLastName("");
			}
			if (request.getParameter("firstName") == null
					|| request.getParameter("firstName").toString().equals("")) {
				dto.setEmployeeFirstName("");
			}

			//
			// System.out.println("id : " + dto.getEmployeeID());
			// System.out.println("name : " + dto.getEmployeeFirstName());
			// System.out.println("name : " + dto.getEmployeeLastName());

			List<EmployeeDto> dtoList = new EmployeeService()
					.searchEmployeeFromSubscription(dto);
			// System.out.println("Size of list " + dtoList.size());
			String html = "<table border='1'>" + " <thead>" + " <tr>"
					+ "  <th>Employee Name</th>" + "   <th>Personnel No</th>"
					+ "     <th>Department</th>" + "      <th></th>"
					+ "   </tr>" + "</thead>" + " <tbody>";
			String data = "";
			String i = "";
			// System.out.println("dtolist " + dtoList.size());
			if (dtoList.size() == 0) {
				data = "<td colspan='5' > No Data Found ! </td>";
			}
			for (EmployeeDto emp : dtoList) {
				i = emp.getEmployeeID();
				// System.out.println("trac-*****---------- " + i );
				data = data + "<tr> " + "" + "<td id=employeeNameCell-" + i
						+ "> " + emp.getEmployeeFirstName() + " "
						+ emp.getEmployeeLastName()
						+ "</td><td id=personnelNoCell-" + i + ">"
						+ emp.getPersonnelNo() + "</td><td>"
						+ emp.getDeptName()
						+ "</td><td> <a href=# onClick=selectRow('"
						+ emp.getEmployeeID() + "') > Select </a> "
						+ "<input type='hidden' id='name-" + i + "' "
						+ "name='name-" + i + "'  value='"
						+ emp.getEmployeeFirstName() + " "
						+ emp.getEmployeeLastName() + "' /> "
						+ "<input type='hidden' id='address-" + i + "' "
						+ "name='address-" + i + "'  value='"
						+ emp.getAddress() + "' /> " + "</td></tr>";

			}
			String lastPart = "        </tbody>" + "   </table>";

			html = html + data + lastPart;
			// System.out.println("Value " + html);
			out.write(html);

		} finally {
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
