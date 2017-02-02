package com.agiledge.atom.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.agiledge.atom.dao.EscalationMatrixDao;
import com.agiledge.atom.dto.EmployeeDto;
import com.agiledge.atom.dto.EscalationMatrixDto;
import com.agiledge.atom.service.EmployeeService;


/**
 * Servlet implementation class EscalationMatrix
 */

public class EscalationMatrix extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public EscalationMatrix() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		if (request.getParameter("param") != null) {
			PrintWriter out = response.getWriter();
			String id = request.getParameter("employeeID");
			String name = request.getParameter("name");
			String param = request.getParameter("param");
			System.out.println(param);
			char[] charArray = param.toCharArray();
			EmployeeDto dto = new EmployeeDto();

			dto.setEmployeeID(id);
			dto.setEmployeeFirstName(name);

			//
			// System.out.println("id : " + dto.getEmployeeID());
			// System.out.println("name : " + dto.getEmployeeFirstName());
			// System.out.println("name : " + dto.getEmployeeLastName());

			List<EmployeeDto> dtoList = new EmployeeService().searchEmployee(
					dto, charArray[6]);
			String html = "<table border='1'>" + " <thead>" + " <tr>"
					+ "  <th>Employee Name</th>" + "   <th>Code</th>" + ""
					+ "      <th></th>" + "   </tr>" + "</thead>" + " <tbody>";
			String data = "";
			String i = "";
			if (dtoList.size() == 0) {
				data = "<td colspan='5' > No Data Found ! </td>";
			}
			for (EmployeeDto emp : dtoList) {
				i = emp.getEmployeeID();
				data = data + "<tr> " + "" + "<td id=employeeNameCell-" + i
						+ "> " + emp.getDisplayName()
						+ "</td><td id=personnelNoCell-" + i + ">"
						+ emp.getPersonnelNo()
						+ "</td><td> <a href=# onClick=selectRow('"
						+ emp.getEmployeeID() + "') > Select </a> "
						+ "<input type='hidden' id='name-" + i + "' "
						+ "name='name-" + i + "'  value='"
						+ emp.getEmployeeFirstName() + "' /> " + "</td></tr>";

			}
			String lastPart = "        </tbody>" + "   </table>";

			html = html + data + lastPart;
			// System.out.println("Value " + html);
			out.write(html);

		} else {
			int status=0;
			HttpSession session = request.getSession();
			System.out.println();
			ArrayList<EscalationMatrixDto> escalationDto = new ArrayList<EscalationMatrixDto>();
			EscalationMatrixDto dto = null;
			try{
			for (int i = 1; i <= 15; i++) {
				dto = new EscalationMatrixDto();
				dto.setLevel("Level" + i);
				dto.setGroup("Vendor");
				dto.setPersonId(request.getParameter("Level" + i + "VID"));
				dto.setTimeSlot(request.getParameter("Level" + i + "Vtime"));
				escalationDto.add(dto);

				dto = new EscalationMatrixDto();
				dto.setLevel("Level" + i);
				dto.setGroup("Admin");
				dto.setPersonId(request.getParameter("Level" + i + "AID"));
				dto.setTimeSlot(request.getParameter("Level" + i + "Atime"));
				escalationDto.add(dto);
				
			}
			}catch(Exception e)
			{System.out.println("error"+e);}
			status = new EscalationMatrixDao()
					.setEscalationMatrix(escalationDto);
			
			if (status > 0) {
				session.setAttribute("status",
						"<div class='success'>Escalation matrix created Susscessfully</div");
			} else {
				session.setAttribute("status",
						"<div class='failure'>Escalation matrix creation Failed!</div");
			}
			response.sendRedirect("escalationEdit.jsp");

		}

	}

}
