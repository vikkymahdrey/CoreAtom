package com.agiledge.atom.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.agiledge.atom.dao.DelegateRoleDao;
import com.agiledge.atom.dto.DelegateRoleDto;


/**
 * Servlet implementation class DelegateRole
 */
public class ModifyDelegateRole extends HttpServlet {

	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException 
			{		
		HttpSession session = request.getSession(true);
		
		String doneBy = session.getAttribute("user").toString();
		int Id = Integer.parseInt(request.getParameter("Id"));
		String source = request.getParameter("cancel");
		try {
		if(source==null)
		{
		String toDate = request.getParameter("toDate");
		
		DelegateRoleDto delegateRoleDto = new DelegateRoleDto();
		
			delegateRoleDto.setId(Id);
			delegateRoleDto.setToDate(toDate);
			delegateRoleDto.setDoneBy(doneBy);

			int value = new DelegateRoleDao().modifyDelegateRole(delegateRoleDto);
			if (value > 0) {
				request.getSession()
						.setAttribute("status",
								"<div class=\"success\" width=\"100%\" >Modify Delegate Role Success</div>");

			} else {
				request.getSession()
						.setAttribute("status",
								"<div class=\"failure\" width=\"100%\" >Modify Delegate Role failure</div>");

			}


		}
		else
		{
			
			int value = new DelegateRoleDao().cancelDelegateRole(Id);
			if (value > 0) {
				request.getSession()
						.setAttribute("status",
								"<div class=\"success\" width=\"100%\" >Cancel Delegate Role Success</div>");

			} else {
				request.getSession()
						.setAttribute("status",
								"<div class=\"failure\" width=\"100%\" >Cancel Delegate Role failure</div>");

			}


		}
		} catch (Exception e) {
			request.getSession()
					.setAttribute("status",
							"<div class=\"failure\" width=\"100%\" >Change Role Delegate  Failure </div>");

			System.out.println("Exception in delegate role servlet " + e);
		}
	response.sendRedirect("viewDelegatedRole.jsp");	
	}

}
