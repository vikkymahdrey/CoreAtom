package com.agiledge.atom.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.agiledge.atom.constants.SettingsConstant;
import com.agiledge.atom.dto.AdhocDto;
import com.agiledge.atom.service.AdhocService;

/**
 * Servlet implementation class AdhocSetup
 */
public class AdhocSetup extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AdhocSetup() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		//System.out.println("herrrrrrrrrrrrrrrrrrrrr");
		String siteId = request.getParameter("siteId");
		String projectUnit = request.getParameter("projectUnit");
		String adhocType = request.getParameter("adhoctype");
		
		int status = 0;
		String adhocTypeId = request.getParameter("adhocTypeId");
		//String pickDrop = request.getParameter("pickDrop");
		//System.out.println("pick/drop" + pickDrop);
		String approval = request.getParameter("approval");
		String requestCutoff = request.getParameter("bookCutoff");
		String cancelCutoff = request.getParameter("cancelCutoff");
		//String requester = request.getParameter("requester");
		String requesters[] = request.getParameterValues("requesters");
		String maxPendingRequest = request.getParameter("maxpendingrequest");
		//String approval = request.getParameter("approval");
		String requestCutoffdrop = request.getParameter("bookCutoffdrop");
		String cancelCutoffdrop = request.getParameter("cancelCutoffdrop");
		//String requester = request.getParameter("requester");
		//String requesters[] = request.getParameterValues("requesters");
		String maxPendingRequestdrop = request.getParameter("maxpendingrequestdrop");
		String approvers[];
		String maxRequest;
		String cancelMode;
		if (adhocType.equalsIgnoreCase(SettingsConstant.SHIFT_EXTENSTION))
		{
			AdhocDto dto = new AdhocDto();
			dto.setSiteId(siteId);
			dto.setPickupDrop("pick up");
			dto.setProjectUnit(projectUnit);
			dto.setAdhocType(adhocType);
			dto.setMaxPendingRequest(maxPendingRequest);
			dto.setApproval(approval);
			dto.setRequestCutoff(requestCutoff);
			dto.setCancelCutoff(cancelCutoff);
			//dto.setRequester(requester);
			dto.setRequesters(requesters);
			//System.out.println(dto.toString());
			if (approval.equalsIgnoreCase("yes")) {
				approvers = request.getParameterValues("approvers");
				dto.setApprovers(approvers);
			}
			if (adhocType.equalsIgnoreCase(SettingsConstant.SHIFT_EXTENSTION)) {
				maxRequest = request.getParameter("maxrequest");
				cancelMode = request.getParameter("cancelMode");
				dto.setMaxRequest(maxRequest);
				dto.setCancelMode(cancelMode);
				dto.setExistingCancelTime(request.getParameter("existingCancel"));
			}
			
			
			AdhocDto dto2 = new AdhocDto();
			dto2.setSiteId(siteId);
			dto2.setPickupDrop("drop");
			dto2.setProjectUnit(projectUnit);
			dto2.setAdhocType(adhocType);
			dto2.setMaxPendingRequest(maxPendingRequestdrop);
			dto2.setApproval(approval);
			dto2.setRequestCutoff(requestCutoffdrop);
			dto2.setCancelCutoff(cancelCutoffdrop);
			//dto.setRequester(requester);
			dto2.setRequesters(requesters);
			//System.out.println(dto.toString());
			if (approval.equalsIgnoreCase("yes")) {
				approvers = request.getParameterValues("approvers");
				dto2.setApprovers(approvers);
			}
			if (adhocType.equalsIgnoreCase(SettingsConstant.SHIFT_EXTENSTION)) {
				String maxRequestdrop = request.getParameter("maxrequestdrop");
				String cancelModedrop = request.getParameter("cancelModedrop");
				dto2.setMaxRequest(maxRequestdrop);
				dto2.setCancelMode(cancelModedrop);
				dto2.setExistingCancelTime(request.getParameter("existingCanceldrop"));
			}
			status = new AdhocService().insertSetup(dto);
			status = new AdhocService().insertSetup(dto2);
			if (adhocTypeId != null || adhocType.equals("")) {			
				dto.setId(Long.parseLong(adhocTypeId));
				status = new AdhocService().updateSetup(dto);
				status = new AdhocService().updateSetup(dto2);
				if (status > 0) {
					session.setAttribute("status",
							"<div class=\"success\" > Adhoc Type Updation Successfull</div>");
				} else {
					session.setAttribute("status",
							"<div class=\"failure\" > Adhoc type Updation failed !</div>");
				}
				response.sendRedirect("AdhocSetupEdit.jsp");
			} else {
				status = new AdhocService().insertSetup(dto);
				status = new AdhocService().insertSetup(dto2);
				if (status > 0) {
					session.setAttribute("status",
							"<div class=\"success\" > Adhoc Type registered Successfully</div>");
				} else {
					session.setAttribute("status",
							"<div class=\"failure\" > Adhoc type registeration failed !</div>");
				}
				response.sendRedirect("adhocSetup.jsp");	
			}
			
		}
		else{
		
		AdhocDto dto = new AdhocDto();
		//dto.setPickupDrop("pick up");
		dto.setSiteId(siteId);
		dto.setProjectUnit(projectUnit);
		dto.setAdhocType(adhocType);
		dto.setMaxPendingRequest(maxPendingRequest);
		dto.setApproval(approval);
		dto.setRequestCutoff(requestCutoff);
		dto.setCancelCutoff(cancelCutoff);
		//dto.setRequester(requester);
		dto.setRequesters(requesters);
		//System.out.println(dto.toString());
		if (approval.equalsIgnoreCase("yes")) {
			approvers = request.getParameterValues("approvers");
			dto.setApprovers(approvers);
		}
		if (adhocType.equalsIgnoreCase(SettingsConstant.SHIFT_EXTENSTION)) {
			maxRequest = request.getParameter("maxrequest");
			cancelMode = request.getParameter("cancelMode");
			dto.setMaxRequest(maxRequest);
			dto.setCancelMode(cancelMode);
			dto.setExistingCancelTime(request.getParameter("existingCancel"));
		}
		
		
		
		/*if (status > 0) {
			session.setAttribute("status",
					"<div class=\"success\" > Adhoc Type registered Successfully</div>");
		} else {
			session.setAttribute("status",
					"<div class=\"failure\" > Adhoc type registeration failed !</div>");
		}
		response.sendRedirect("adhocSetup.jsp");*/
		if (adhocTypeId != null || adhocType.equals("")) {			
			dto.setId(Long.parseLong(adhocTypeId));
			status = new AdhocService().updateSetup(dto);
			if (status > 0) {
				session.setAttribute("status",
						"<div class=\"success\" > Adhoc Type Updation Successfull</div>");
			} else {
				session.setAttribute("status",
						"<div class=\"failure\" > Adhoc type Updation failed !</div>");
			}
			response.sendRedirect("AdhocSetupEdit.jsp");
		} else {
			status = new AdhocService().insertSetup(dto);
			if (status > 0) {
				session.setAttribute("status",
						"<div class=\"success\" > Adhoc Type registered Successfully</div>");
			} else {
				session.setAttribute("status",
						"<div class=\"failure\" > Adhoc type registeration failed !</div>");
			}
			response.sendRedirect("adhocSetup.jsp");	
		}
		}
	

	}
}
