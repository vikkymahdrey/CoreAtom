/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.agiledge.atom.servlets.subscription;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.agiledge.atom.accessRight.dto.AccessRightConstants;

import com.agiledge.atom.accessRight.service.AccessRightService;
import com.agiledge.atom.dto.EmployeeDto;
import com.agiledge.atom.dto.EmployeeSubscriptionDto;
import com.agiledge.atom.service.EmployeeService;
import com.agiledge.atom.service.EmployeeSubscriptionService;
import com.agiledge.atom.service.MailService;


/**
 * 
 * @author 123
 */
public class Subscribe extends HttpServlet {

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
		String source = "Employee";
		try {

			// System.out.println("In subscribe Servlet");
			HttpSession session1 = request.getSession();
			String doneBy=session1.getAttribute("user").toString();
			EmployeeSubscriptionDto dto = new EmployeeSubscriptionDto();
			String employeeID = request.getParameter("employeeID");
			dto.setSite(request.getParameter("site"));
			dto.setContactNo(request.getParameter("contactNo"));
			dto.setEmpAddress(request.getParameter("addressn"));
			// System.out.println("IsContr ......." +
			// dto.getIsContractEmployee());
			 
			dto.setSupervisor1(request.getParameter("supervisorID1"));
			dto.setSupervisor2(request.getParameter("supervisorID2"));
			dto.setSubscriptionFromDate(request.getParameter("fromDate"));
			dto.setLandMark(request.getParameter("landMarkID"));
			dto.setDoneBy(doneBy);
			HttpSession session = request.getSession();
			EmployeeDto mngrDto=new EmployeeService().getEmployeeAccurate(dto.getSupervisor1());
			EmployeeDto spocDto=new EmployeeService().getEmployeeAccurate(dto.getSupervisor2());
		
			dto.setEmployeeID(session.getAttribute("user").toString());
			// System.out.println(" date  " + date);
			if (employeeID == null) {
				dto.setEmployeeID(session.getAttribute("user").toString());
			} else {
				source = "TransportAdmin";
				dto.setEmployeeID(employeeID);
			}
			EmployeeSubscriptionService service = new EmployeeSubscriptionService();
		 
			if (service.validateSubscriptionForm(dto, source) ) {
			 
				int value = service.subscribeRequest(dto);
				
				if (value > 0)
				{

					
					if(source.equals("Employee"))
					{
						AccessRightService accessService = new AccessRightService();
						System.out.println("Employee : "+ spocDto.getUserType());
						if(( accessService.hasRight(AccessRightConstants.HRM, spocDto.getUserType()) ||  accessService.hasRight(AccessRightConstants.TA, spocDto.getUserType())|| accessService.hasRight(AccessRightConstants.TM, spocDto.getUserType()))==false
							|| ( accessService.hasRight(AccessRightConstants.HRM, mngrDto.getUserType())|| accessService.hasRight(AccessRightConstants.TA, mngrDto.getUserType())||accessService.hasRight(AccessRightConstants.TM, mngrDto.getUserType()))==false )
						{
							new MailService().sendMailToTmDueToInappropriateSelectionOfManager(dto,spocDto.getUserType(),mngrDto.getUserType());
						}
					}
					
					session.setAttribute("status",
							"<div class=\"success\" width=\"100%\" > Subscription successful</div>");
					
				}
				
				else if (value == -1) {
					session.setAttribute("status",
							"<div class=\"failure\" > Already registered!</div>");
				} else {
					session.setAttribute("status",
							"<div class=\"failure\" > Subscription failed !</div>");
				}
			} else {
				session.setAttribute("status",
						"<div class=\"failure\" >Validation Failure:" +service.getErrorMessage() +"  !</div>");
			}
		} catch (Exception e) {
			System.out.println("Error in Servliet subscribe : " + e);
			request.getSession().setAttribute("status",
					"<div class=\"failure\" >Validation Failure:" +e +"  !</div>");
		}
		if (source.equalsIgnoreCase("Employee")) {
			response.sendRedirect("SubscriptionSelector");
		} else {
			response.sendRedirect("transadmin_subscriptionforemp.jsp");
		}
	}

	private boolean validateForm(EmployeeSubscriptionDto dto) {
		boolean flag = true;
		if (dto.getEmployeeID().equals("")) {
			flag = false;

		} else if (dto.getSupervisor1().equals("")) {
			flag = false;
		} else if (dto.getSupervisor2().equals("")) {
			flag = false;
		} else if (dto.getLandMark().equals("")) {
			flag = false;
		} else if (dto.getSite().equals("")) {
			flag = false;
		}
		return flag;
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
