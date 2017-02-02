package com.agiledge.atom.billingtype.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.agiledge.atom.billingtype.dto.BillingTypeDto;
import com.agiledge.atom.billingtype.service.BillingTypeService;
import com.agiledge.atom.commons.OtherFunctions;

/**
 * Servlet implementation class MapBillingType
 */
 
public class MapBillingType extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MapBillingType() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		 
		doProcess(request,response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doProcess(request,response);
	}

	private void doProcess(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		
		try {
			BillingTypeDto dto = new BillingTypeDto();
	      
			dto.setId(String.valueOf(Integer.parseInt(request.getParameter("type"))));
		 
			dto.setTransportType(String.valueOf(Integer.parseInt(request.getParameter("trans_type"))));
			 
			dto.setSite(String.valueOf(Integer.parseInt(request.getParameter("site"))));
			 
			dto.setVendor(String.valueOf(Integer.parseInt(request.getParameter("vendor"))));
			String fromDate  = request.getParameter("fromDate");
			dto.setDoneBy(request.getSession().getAttribute("user").toString());
			 
			/*
			Calendar cal = Calendar.getInstance();
 			cal.setTime(  new Date() );
 			cal.add(Calendar.DATE, 1);
 			dto.setFromDate(cal.getTime());*/
			dto.setFromDate(OtherFunctions.changeDateFromat_ddmmyyy_to_UtilDate(fromDate)); 
 			 
			  
			
			 
			BillingTypeService service = new BillingTypeService();
			System.out.println("DTO : "+ dto.toString());
			int value =service.mapBillingType(dto);
			if(value>0) {
				request.getSession().setAttribute("status",
						"<div class=\"success\" >Billing Type Mapping Successful.</div>");
			}else {
				request.getSession().setAttribute("status",
						"<div class=\"failure\" >"+service.getMessage()+"</div>");
			}
			
		}catch(Exception e) {
			request.getSession().setAttribute("status",
					"<div class=\"failure\" >Validation Failure:" +e +"  !</div>");
			
		}
		response.sendRedirect("billingtype_mapping.jsp");		
	}
	
	private String ifNullSetEmpty(String param) {
		return param==null?"":param;
	}


}
