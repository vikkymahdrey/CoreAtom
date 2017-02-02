package com.agiledge.atom.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.agiledge.atom.dto.BranchDto;


/**
 * Servlet implementation class Branch
 */
public class Branch extends HttpServlet {

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		String doneBy=session.getAttribute("user").toString();
		String id = request.getParameter("id");
		String companyId = request.getParameter("companyId");
		String branchLocation = request.getParameter("branchLocation");
		BranchDto dto = new BranchDto();
		try {
			dto.setId(id);
			dto.setCompanyId(companyId);		
			dto.setLocation(branchLocation);
			dto.setDoneBy(doneBy);
			if (id == null) {
				if (new com.agiledge.atom.service.CompanyBranchService().addBranch(dto) > 0) {
					session.setAttribute("status",
							"<div class=\"success\" > Branch Registration success </div>");
				} else {
					session.setAttribute("status",
							"<div class=\"failure\" > Branch Registration failed !</div>");
				}
			} else {
				if (new com.agiledge.atom.service.CompanyBranchService().modifyBranch(dto) > 0) {
					session.setAttribute("status",
							"<div class=\"success\" > Branch Modification success </div>");
				} else {
					session.setAttribute("status",
							"<div class=\"failure\" > Branch Modification failed !</div>");
				}

			}

		} catch (Exception ex) {
			session.setAttribute("status",
					"<div class=\"failure\" > Updation failed!</div>");
			System.out.println("Erro" + ex);
		} finally {
			response.sendRedirect("branch.jsp?companyId="+companyId);
		}

	}

}
