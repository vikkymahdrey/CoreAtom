package com.agiledge.atom.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.agiledge.atom.dao.ShuttleSocketDao;
import com.agiledge.atom.dto.EmployeeDto;
import com.agiledge.atom.dto.TripDetailsChildDto;
import com.agiledge.atom.servlets.employee.GetSubscribedEmployeeData;

public class GetSearchedEmployee extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7976366914337195554L;

	protected void processRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException,
			SQLException {
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		try {
			EmployeeDto dto = new EmployeeDto();
			dto.setEmployeeID(request.getParameter("employeeID").toString());
			dto.setEmployeeFirstName(request.getParameter("firstName")
					.toString());
			dto.setEmployeeLastName(request.getParameter("lastName").toString());
			ArrayList<TripDetailsChildDto> dtoList = new ShuttleSocketDao()
					.searchEmployee(dto);
			String html = "<table border='1'>" + " <thead>" + " <tr>"
					+ "  <th>Employee Name </th>"
					+ "  <th></th>" + "   </tr>" + "</thead>" + " <tbody>";
			String data = "";
			for (TripDetailsChildDto tripDetailsChildDto : dtoList) {
				data = data + "<tr><td id='empdata"
						+ tripDetailsChildDto.getEmployeeId() + "'>"
						+ tripDetailsChildDto.getEmployeeName()
						+ "</td><td> <a href=# onClick=selectRow('"
						+ tripDetailsChildDto.getEmployeeId()
						+ "')> Select </a> " + "</td></tr>";
			}
			String lastPart = "        </tbody>" + "   </table>";

			html = html + data + lastPart;
			out.write(html);

		} finally {
			out.close();
		}
	}
	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		try {
			processRequest(request, response);
		} catch (SQLException ex) {
			Logger.getLogger(GetSubscribedEmployeeData.class.getName()).log(
					Level.SEVERE, null, ex);
		}
	}
	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		try {
			processRequest(request, response);
		} catch (SQLException ex) {
			Logger.getLogger(GetSubscribedEmployeeData.class.getName()).log(
					Level.SEVERE, null, ex);
		}
	}

	@Override
	public String getServletInfo() {
		return "Short description";
	}// </editor-fold>


}
