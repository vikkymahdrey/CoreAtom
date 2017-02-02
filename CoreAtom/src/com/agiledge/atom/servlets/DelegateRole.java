package com.agiledge.atom.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.agiledge.atom.commons.OtherFunctions;
import com.agiledge.atom.dao.DelegateRoleDao;
import com.agiledge.atom.dto.DelegateRoleDto;
import com.agiledge.atom.service.MailService;


/**
 * Servlet implementation class DelegateRole
 */
public class DelegateRole extends HttpServlet {

	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException 
			{
		String source="";
		String employeeId = request.getParameter("actualEmpId");
		if(employeeId==null)
		{
		employeeId = request.getSession().getAttribute("user").toString();
		source="manager";
		}

		HttpSession session = request.getSession(true);
		String doneBy = session.getAttribute("user").toString();
		String delegatedEmployeeId = request.getParameter("delegatedEmpId");
		String fromDate = request.getParameter("fromDate");
		String toDate = request.getParameter("toDate");
		DelegateRoleDto delegateRoleDto = new DelegateRoleDto();
		try {
				delegateRoleDto.setActualEmpId(employeeId);
			delegateRoleDto.setDelegatedEmpId(delegatedEmployeeId);
			delegateRoleDto.setFromDate(fromDate);
			delegateRoleDto.setToDate(toDate);
			delegateRoleDto.setDoneBy(doneBy);

			int value = new DelegateRoleDao()
					.insertDelegateRole(delegateRoleDto);
			if (value > 0) {
				
				new MailService().sendRoleDelegationMail(delegateRoleDto,session.getAttribute("user").toString());
				request.getSession()
						.setAttribute("status",
								"<div class=\"success\" width=\"100%\" > Delegate Role Success</div>");

			} else if(value==-1) {
				request.getSession()
						.setAttribute("status",
								"<div class=\"failure\" width=\"100%\" > Delegate Role failure!Delegation Exist</div>");
			
		} else if(value==-2) {
			request.getSession()
					.setAttribute("status",
							"<div class=\"failure\" width=\"100%\" > Delegate Role failure!Two Delegation Has Done</div>");

		}

		} catch (Exception e) {
			request.getSession()
					.setAttribute("status",
							"<div class=\"failure\" width=\"100%\" > Delegate Role Failure </div>");

			System.out.println("Exception in delegate role servlet " + e);
		}
		if(source.equals("manager"))
		{
		response.sendRedirect("viewDelegatedRole.jsp");
		}
		else
		{
			response.sendRedirect("transadminviewDelegatedRole.jsp");	
		}
	}

}
