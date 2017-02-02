package com.agiledge.atom.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.agiledge.atom.dto.VendorContractDto;
import com.agiledge.atom.service.VendorService;


/**
 * Servlet implementation class VendorContract
 */

public class VendorContract extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public VendorContract() {
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
		HttpSession session = request.getSession(true);
		String doneBy = session.getAttribute("user").toString();
		
		try{
			VendorContractDto dto=new VendorContractDto();
			 
			 
			dto.setDescription(request.getParameter("contractDescription"));
			dto.setRate(request.getParameter("contractRate"));
			dto.setVendor(request.getParameter("vendor"));
			dto.setDoneBy(doneBy);
			VendorService service= new VendorService();
			boolean flag=service.validateVendorContract(dto);
			if(flag==false)
			{
				request.getSession().setAttribute("status",
						"<div class=\"failure\" > "+ service.getErroMessage()+ "</div>");
			}else
			{
				int value=service.doVendorContract(dto);
				if(value>0)
				{
					request.getSession().setAttribute("status",
							"<div class=\"success\" > Contract updated successfully</div>");
				}else
				{
					request.getSession().setAttribute("status",
							"<div class=\"failure\" > Contract update failure</div>");
				}
			}
			
			
		}catch(Exception e)
		{
			request.getSession().setAttribute("status",
					"<div class=\"failure\" > Contract update failure</div>");
			System.out.println(" Exception in VendorContract " + e);
		}
		
		response.sendRedirect("transadmin_vendorSetUp.jsp");
	}
	
	

}
