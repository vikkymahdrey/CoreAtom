package com.agiledge.atom.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.agiledge.atom.dto.EmployeeDto;
import com.agiledge.atom.service.EmployeeService;


/**
 * Servlet implementation class GetEmployeeSpecial
 */
public class GetEmployeeSpecial extends HttpServlet {
       
	protected void processRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		try {
			EmployeeDto dto = new EmployeeDto();

			dto.setEmployeeID(request.getParameter("employeeID"));
			dto.setEmployeeFirstName(request.getParameter("firstName"));
			dto.setEmployeeLastName(request.getParameter("lastName"));
			dto.setProjectid(request.getParameter("projectid"));
			List<EmployeeDto> dtoList;
			if(dto.getProjectid()==null||dto.getProjectid()=="")
			{
				
				dtoList = new EmployeeService()
				.searchEmployeeFromSubscription(dto);
			}
			else
			{
				dtoList = new EmployeeService().getProjectEmployees(dto);
			}
			String html = "<table border='0'>" + " <thead>" + " <tr>"
					+ "<th></th>  " + "   <th>Personnel No</th>"
					+ "<th>First Name</th>" + "<th>Last Name</th>"
					+ "     <th>Department</th>" + "     <th>Routing</th>"
					+ "   </tr>" + "</thead>" + " <tbody>";
			String data = "";
			int i = 0;
			if (dtoList.size() == 0) {
				data = "<td colspan='5' > No Result Found ! </td>";
			}
			for (EmployeeDto emp : dtoList) {
				i++;
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
						+ "</td> <td>" + emp.getRoutingType() + "</td> " + "</tr>";
			}
			String lastPart = "    "
					+ "<tr> <td colspan=\"5\" align='center'  >"
					+ "<select name='setattrib' id='setattrib' >"
					+ "  <option value='i'> Set Individual Vehicle</option>"
					+ "  <option value='p'> Set Project Vehicle</option>"
					+ "  <option value='o'> Set Shared Vehicle</option>"
					+ "</select>"
					+ " "
					+ "<input type='submit' class='formbutton'  value='Update' /> "
					+ "</td>" + "</tr> " + "</td> " + "    </tbody>"
					+ "   </table>";

			html = html + data + lastPart;
			out.write(html);

		} finally {
			out.close();
		}
	}
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
