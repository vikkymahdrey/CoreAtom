package com.agiledge.atom.usermanagement.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import com.agiledge.atom.commons.OtherFunctions;
import com.agiledge.atom.dto.EmployeeDto;
import com.agiledge.atom.usermanagement.dto.PageDto;
import com.agiledge.atom.usermanagement.dto.ViewManagementStatus;
import com.agiledge.atom.usermanagement.service.PageService;

/**
 * Servlet implementation class AddPage
 */
 
public class AddPage extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddPage() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		 doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		 JSONObject json = new JSONObject();
		try {
				String page = request.getParameter("page");
				String pageType = request.getParameter("pageType");
				if(page == null || page.trim().equals("") ) {
					json.put("result", "false");
					json.put("message", "URL is blank" );
				} else {
					PageDto dto = new PageDto();
					 dto .setUrl(page);
					  
					 dto.setUpdatedBy(request.getSession().getAttribute("user").toString());
					 if(pageType!=null&&pageType.equalsIgnoreCase("PAGE")) {
						 dto.setUrlType(ViewManagementStatus.PAGE);
					 } else {
						 dto.setUrlType(ViewManagementStatus.SERVLET);
					 }
					PageDto rDto = new PageService().addPage(dto);
					if(rDto!=null&&Integer.parseInt(rDto.getId())>0) {
						json.put("result", "true");
						json.put("message", "URL inserted successfuly" );
						json.put("id", rDto.getId());
						EmployeeDto employeeDto = (EmployeeDto) request.getSession()
								.getAttribute("userDto");
						OtherFunctions.resetViews(employeeDto.getRoleId(), request
								.getSession().getServletContext());
					} else {
						json.put("result", "false");
						json.put("message", "URL insertion failed" );
					}
				}
		}catch(Exception e) {
			
			try {
				json.put("result", "false");
				json.put("message", "Error : "+ e );
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
		response.getWriter().write(json.toString());
	}

}
