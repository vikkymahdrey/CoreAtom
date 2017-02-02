/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.agiledge.atom.servlets.employee;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.agiledge.atom.dto.EmployeeDto;
import com.agiledge.atom.service.EmployeeService;
import com.agiledge.atom.usermanagement.dto.UserManagementDto;
import com.agiledge.atom.usermanagement.service.UserManagementService;


//import javax.servlet.annotation.WebServlet;

/**
 * 
 * @author 123
 */
public class GetEmployeeView extends HttpServlet {

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

			// System.out.println("id : " + dto.getEmployeeID());
			// System.out.println("name : " + dto.getEmployeeFirstName());

			List<EmployeeDto> dtoList = new EmployeeService()
					.searchEmployee(dto);
			// System.out.println("Size of list " + dtoList.size());
			String html = "<table border='0'>" + " <thead>" + " <tr>"
					+ "<th></th>  " + "   <th>Personnel No</th>"
					+ "<th>First Name</th>" + "<th>Last Name</th>"
					+ "     <th>Department</th>" + "     <th>Role</th>"
					+ "   </tr>" + "</thead>" + " <tbody>";
			String data = "";
			int i = 0;
			// System.out.println("dtolist " + dtoList.size());
			if (dtoList.size() == 0) {
				data = "<td colspan='5' > No Data Found ! </td>";
			}
			for (EmployeeDto emp : dtoList) {
				i++;
				// System.out.println("trac-*****---------- " + i );
				data = data
						+ "<tr> "
						+ "<td> <input type=\"checkbox\" name=\"employeeIdCheckBox\" value=\""
						+ emp.getEmployeeID() + "\" />"
						+ "</td><td id=personnelNoCell-" + i + ">"
						+ emp.getPersonnelNo() + " </td>"
						+ "<td id=employeeNameCell-" + i + "> "
						+ emp.getEmployeeFirstName() + "</td>" + "<td>"
						+ emp.getEmployeeLastName() +

						"</td> <td>" + emp.getDeptName() + "</td> "
						+ "</td> <td>" + emp.getUserType() + "</td> " + "</tr>";
			}
			ArrayList<UserManagementDto> userList = new UserManagementService().getSystemUsers();
			String options="";
			if(userList!=null&& userList.size()>0) {
				for(UserManagementDto userDto : userList) {
					options += "<option value='" + userDto.getId() + "' >" + userDto.getName() + "</option>";
				}
			}
			
			String lastPart = "    "
					+ "<tr> <td colspan=\"5\" align='center'  >"
					+ "<select name='role' id='role' >"
					 
					+ options 
					+ "<input type='submit' class='formbutton'  value='Update' /> "
					+ "</td>" + "</tr> " + "</td> " + "    </tbody>"
					+ "   </table>";

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
