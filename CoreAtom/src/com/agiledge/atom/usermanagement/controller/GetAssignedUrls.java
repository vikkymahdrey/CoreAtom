package com.agiledge.atom.usermanagement.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import com.agiledge.atom.usermanagement.dto.PageDto;
import com.agiledge.atom.usermanagement.dto.PageManagementStatus;
import com.agiledge.atom.usermanagement.service.PageService;

/**
 * Servlet implementation class GetAssignedUrls
 */
 
public class GetAssignedUrls extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetAssignedUrls() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		JSONObject json = new JSONObject();
		try {
			 
				String id = request.getParameter("id");
				System.out.println(" IN Sid GetAssignedUrl : " + id);
				PageService service  = new PageService();
				PageDto inDto = new PageDto ();
				inDto.setId(id);
				inDto.setUrlType(PageManagementStatus.SUB);
				org.json.JSONArray array = service . getAssignedPagesJson(inDto);
				if(array.length()>0) {
					json.put("result","true");
					//System.out.println(" success : butt array : " + array.toString());
					json.put("item",array);
					
				}
			
		}catch(Exception e) {
			 try {
				json.put("result", "false");
				json.put("message", "Error :"+e);
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			 
		}
		System.out.println("JSON :"+ json.toString());
		response.getWriter().write(json.toString());
	}

}
