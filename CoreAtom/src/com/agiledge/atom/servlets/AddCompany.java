/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.agiledge.atom.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.agiledge.atom.dto.CompanyDto;


/**
 * 
 * @author Noufal C C
 */
public class AddCompany extends HttpServlet {

	/**
	 * 
	 */

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
		HttpSession session = request.getSession(true);
		CompanyDto dto = new com.agiledge.atom.dto.CompanyDto();
		String doneBy=session.getAttribute("user").toString();

		try {
			// System.out.println("in addcompany servlet");
			String id=request.getParameter("id");
			dto.setCompanyID(id);
			dto.setDoneBy(doneBy);
			dto.setCompanyName(request.getParameter("name"));
			dto.setAddress(request.getParameter("address"));
			dto.setWebsite(request.getParameter("website"));
			dto.setContactPersonName(request.getParameter("contactPersonName"));
			dto.setContactPersonNumber(request.getParameter("contactPersonNumber"));
			if(id==null)
			{
			if (new com.agiledge.atom.service.CompanyBranchService().addCompany(dto) > 0) {
				session.setAttribute("status",
						"<div class=\"success\" > Company Registration success </div>");
			} else {
				session.setAttribute("status",
						"<div class=\"failure\" > Company Registration failed !</div>");
			}
			}
			else
			{
				if (new com.agiledge.atom.service.CompanyBranchService().modifyCompany(dto) > 0) {
					session.setAttribute("status",
							"<div class=\"success\" > Company Modification success </div>");
				} else {
					session.setAttribute("status",
							"<div class=\"failure\" > Company Modification failed !</div>");
				}	
				
			}

		} catch (Exception ex) {
			session.setAttribute("status",
					"<div class=\"failure\" > Updation failed!</div>");
			System.out.println("Erro" + ex);
		} finally {
			response.sendRedirect("addCompany.jsp");
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
