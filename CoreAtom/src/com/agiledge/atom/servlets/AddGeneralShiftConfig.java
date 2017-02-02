package com.agiledge.atom.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.agiledge.atom.dto.GeneralShiftDTO;
import com.agiledge.atom.service.GeneralShiftService;


public class AddGeneralShiftConfig extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddGeneralShiftConfig() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		HttpSession session=request.getSession();
		GeneralShiftDTO dto=new GeneralShiftDTO();
		dto.setSite_id(request.getParameter("siteselect"));
		dto.setLogtype(request.getParameter("typeselect"));
		String[] shiftin=request.getParameterValues("selectin");
		String[] shiftout=request.getParameterValues("selectout");
		dto.setFrom_date(request.getParameter("from_date"));
		dto.setApproval_req(request.getParameter("approvalselect"));
		dto.setApproved_by(request.getParameter("roleselect"));
		System.out.println("Approver"+dto.getApproved_by());
		dto.setCutoffdays(request.getParameter("cutoffdays"));
		dto.setCancelcutoff(request.getParameter("cutoffcancel"));
		dto.setDeduction(request.getParameter("deductionselect"));
		dto.setDeductionid(request.getParameter("deductiontype"));
		dto.setDeduction_amt(request.getParameter("deductionamt"));
		dto.setWaitlist_reconf(request.getParameter("waitselect"));
		dto.setWaitlist_cutoffdays(request.getParameter("waitlist"));
		if(request.getParameter("approvalselect").equals("n"))
		{
			dto.setApproved_by(null);
		}
		if(request.getParameter("deductionselect").equals("n"))
		{
			dto.setDeductionid(null);
			dto.setDeduction_amt(null);
		}
		if(request.getParameter("waitselect").equals("n"))
		{
			dto.setWaitlist_cutoffdays(null);
		}
		String validation=new GeneralShiftService().insertValidations(dto,shiftin,shiftout);
		if(validation.equals("true"))
		{
		int result=new GeneralShiftService().insertConfigData(dto,shiftin,shiftout);
		if(result>1)
		{
			session.setAttribute("status",
					"<div class='success'>Configuration Added Successfully</div");
		} else {
			session.setAttribute("status",
					"<div class='failure'>Operation Failed</div");
		}
		}
		else
		{
			session.setAttribute("status",
					"<div class='failure'>Operation Failed Due To "+validation+"</div");
		}
		response.sendRedirect("viewallconfig.jsp");
		
		}

}
