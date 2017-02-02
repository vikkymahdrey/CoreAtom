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
 * Servlet implementation class GetEmployeeForManagerData
 */
   
public class GetEmployeeForManagerData extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetEmployeeForManagerData() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		processRequest(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		processRequest(request, response);
	}
	
	protected void processRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {		
		PrintWriter out = response.getWriter();
		try {
			  System.out.println(" inside GetEmployee Data..........");
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

			List<EmployeeDto> dtoList = new EmployeeService()
					.searchEmployee(dto);
			String html = "<table border='1'>" + " <thead>" + " <tr>"
					+ "  <th>Employee Name</th>" + "   <th>Personnel No</th>"
					+ "     <th>Department</th>" + "      <th></th>"
					+ "   </tr>" + "</thead>" + " <tbody>";
			String data = "";
			String i = "";
			if (dtoList.size() == 0) {
				data = "<td colspan='5' > No Data Found ! </td>";
			}
			for (EmployeeDto emp : dtoList) {
				i = emp.getEmployeeID();
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
						+ emp.getAddress() + "' /> " +
								"<input type='hidden' name='userType-" + i +"' id='userType-" + i +"' value='"+emp.getUserType()+"' /> " +
								"" + "</td></tr>";

			}
			String lastPart = "        </tbody>" + "   </table>";

			html = html + data + lastPart;
			// System.out.println("Value " + html);
			out.write(html);

		} finally {
			out.close();
		}
	}



}
