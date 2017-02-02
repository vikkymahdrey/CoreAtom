package com.agiledge.atom.transporttype.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.agiledge.atom.commons.OtherFunctions;
import com.agiledge.atom.transporttype.dto.TransportTypeDto;
import com.agiledge.atom.transporttype.service.TransportTypeService;




/**
 * Servlet implementation class MapTransportType
 */
 
public class MapTransportType extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MapTransportType() {
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
			TransportTypeDto dto = new TransportTypeDto();
			dto.setId(Integer.parseInt(request.getParameter("type")));
			dto.setSite(String.valueOf(Integer.parseInt(request.getParameter("site"))));
			dto.setVendor(String.valueOf(Integer.parseInt(request.getParameter("vendor"))));
			String fromDate  = request.getParameter("fromDate");
			/*
			Calendar cal = Calendar.getInstance();
 			cal.setTime(  new Date() );
 			cal.add(Calendar.DATE, 1);
 			dto.setFromDate(cal.getTime());*/
			dto.setFromDate(OtherFunctions.changeDateFromat_ddmmyyy_to_UtilDate(fromDate)); 
 			 
			  
			
			 
			TransportTypeService service = new TransportTypeService();
			int value =service.mapTransportType(dto);
			if(value>0) {
				request.getSession().setAttribute("status",
						"<div class=\"success\" >Transportaion Type Mapping Successful.</div>");
			}else {
				request.getSession().setAttribute("status",
						"<div class=\"failure\" >"+service.getMessage()+"</div>");
			}
			
		}catch(Exception e) {
			request.getSession().setAttribute("status",
					"<div class=\"failure\" >Validation Failure:" +e +"  !</div>");
			
		}
		response.sendRedirect("transporttype_mapping.jsp");		
	}
	
	private String ifNullSetEmpty(String param) {
		return param==null?"":param;
	}


}
