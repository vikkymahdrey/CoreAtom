/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.agiledge.atom.servlets.employee;

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

import com.agiledge.atom.dao.TripDetailsDao;
import com.agiledge.atom.dto.EmployeeDto;
import com.agiledge.atom.dto.TripDetailsChildDto;


/**
 * 
 * @author 123
 */
public class GetSubscribedEmployeeData extends HttpServlet {

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
			HttpServletResponse response) throws ServletException, IOException,
			SQLException {
		response.setContentType("text/html;charset=UTF-8");
		// System.out.println("IN GetSUbscribed");
		PrintWriter out = response.getWriter();
		try {
			EmployeeDto dto = new EmployeeDto();
			dto.setEmployeeID(request.getParameter("employeeID").toString());
			dto.setEmployeeFirstName(request.getParameter("firstName")
					.toString());
			dto.setEmployeeLastName(request.getParameter("lastName").toString());
			ArrayList<TripDetailsChildDto> dtoList = new TripDetailsDao()
					.searchEmployee(dto);
			// System.out.println("Size of list " + dtoList.size());
			String html = "<table border='1'>" + " <thead>" + " <tr>"
					+ "  <th>Employee Name Area Place Landmark</th>"
					+ "  <th></th>" + "   </tr>" + "</thead>" + " <tbody>";
			String data = "";
			// System.out.println("dtolist " + dtoList.size());
			for (TripDetailsChildDto tripDetailsChildDto : dtoList) {
				data = data + "<tr><td id='empdata"
						+ tripDetailsChildDto.getEmployeeId() + "'>"
						+ tripDetailsChildDto.getEmployeeName() + "--"
						+ tripDetailsChildDto.getArea() + "--"
						+ tripDetailsChildDto.getPlace() + "--"
						+ tripDetailsChildDto.getLandmark()
						+ "</td><td> <a href=# onClick=selectRow('"
						+ tripDetailsChildDto.getEmployeeId() + ":"
						+ tripDetailsChildDto.getLandmarkId()
						+ "')> Select </a> " + "</td></tr>";
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
		try {
			processRequest(request, response);
		} catch (SQLException ex) {
			Logger.getLogger(GetSubscribedEmployeeData.class.getName()).log(
					Level.SEVERE, null, ex);
		}
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
		try {
			processRequest(request, response);
		} catch (SQLException ex) {
			Logger.getLogger(GetSubscribedEmployeeData.class.getName()).log(
					Level.SEVERE, null, ex);
		}
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
