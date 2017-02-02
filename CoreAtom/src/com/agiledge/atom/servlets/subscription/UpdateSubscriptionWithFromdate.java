/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.agiledge.atom.servlets.subscription;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.agiledge.atom.dto.EmployeeDto;
import com.agiledge.atom.dto.EmployeeSubscriptionDto;
import com.agiledge.atom.service.EmployeeService;
import com.agiledge.atom.service.EmployeeSubscriptionService;
import com.agiledge.atom.service.MailService;


/**
 * 
 * @author 123
 */
public class UpdateSubscriptionWithFromdate extends HttpServlet {

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
			HttpSession session1 = request.getSession(true);
			String doneBy = session1.getAttribute("user").toString();
			EmployeeSubscriptionDto dto = new EmployeeSubscriptionDto();
			String employeeID = request.getParameter("employeeID");
			dto.setSubscriptionID(request.getParameter("subscriptionId"));
			dto.setSite(request.getParameter("site"));
			dto.setSupervisor1(request.getParameter("supervisorID1"));
			dto.setSupervisor2(request.getParameter("supervisorID2"));
			dto.setSubscriptionFromDate(request.getParameter("fromDate"));
			dto.setLandMark(request.getParameter("landMarkID"));
			dto.setContactNo(request.getParameter("contactNo"));
			dto.setEmpAddress(request.getParameter("addressn"));
			dto.setEffectiveFrom(request.getParameter("fromdatedata"));
			System.out.println("Alter subscribe from"+request.getParameter("fromdatedata"));
			EmployeeDto mngrDto=new EmployeeService().getEmployeeAccurate(dto.getSupervisor1());
			EmployeeDto spocDto=new EmployeeService().getEmployeeAccurate(dto.getSupervisor2());
			System.out.println("manger " + mngrDto.getDisplayName()+" " + mngrDto.getUserType());
			System.out.println("spoc " + spocDto.getDisplayName()+" " + spocDto.getUserType());

			
			dto.setDoneBy(doneBy);
			HttpSession session = request.getSession();
			
			
			dto.setEmployeeID(session.getAttribute("user").toString());
			if (employeeID == null) {
				dto.setEmployeeID(session.getAttribute("user").toString());
			} else {
				source = "TransportAdmin";
				dto.setEmployeeID(employeeID);
			}
			String date = new Date().toString();
			Calendar cal = Calendar.getInstance();
			date = " " + (cal.get(cal.MONTH) + 1) + "/" + cal.get(cal.DATE)
					+ "/" + cal.get(cal.YEAR);
			
			EmployeeSubscriptionService service = new EmployeeSubscriptionService();
			if (validateForm(dto)) {
				// System.out.println("<**************>"+dto.getSubscriptionID());
				//int value = service.modifySubscribeRequest(dto);
				int value = service.modifySubscription(dto);
				if (value > 0)
				{
					
					String userType1=request.getParameter("userType1")==null?"":request.getParameter("userType1");
					String userType2=request.getParameter("userType2")==null?"":request.getParameter("userType2");
					if((spocDto.getUserType().equals("hrm")|| spocDto.getUserType().equals("ta")||spocDto.getUserType().equals("tm"))==false
							|| (mngrDto.getUserType().equals("hrm")|| mngrDto.getUserType().equals("ta")||mngrDto.getUserType().equals("tm"))==false )
						{
						new MailService().sendMailToTmDueToInappropriateSelectionOfManager(dto,spocDto.getUserType(),mngrDto.getUserType());
					}
					 
					session.setAttribute("status",
							"<div class=\"success\" > Subscription modification successful</div>");
				}
				else
					session.setAttribute("status",
							"<div class=\"failure\" > Subscription modification failed !</div>");
			} else {
				session.setAttribute("status",
						"<div class=\"failure\" > Subscription modification failed !</div>");
			}
		} catch (Exception e) {
			System.out.println("Exception in UpdateSubscription Servlet :" + e);
		}

		if(source.equals("Employee"))
		{
			response.sendRedirect("SubscriptionSelector");
		}else
		{
			response.sendRedirect("transadmin_modifysubscription.jsp");
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

	private boolean validateForm(EmployeeSubscriptionDto dto) {
		boolean flag = true;
		try {
			// System.out.println("CCC"+dto.getEmployeeID()+dto.getLandMark()+dto.getSite());
			if (dto.getEmployeeID().equals("")) {
				flag = false;

			} else if (dto.getSupervisor1().equals("")) {
				flag = false;
			} else if (dto.getLandMark().equals("")) {
				flag = false;
			}
			// System.out.println(" Flag " + flag);
		} catch (Exception e) {
			System.out.println("Error" + e);
		}
		return flag;
	}

}
