package com.agiledge.atom.usermanagement.controller;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.agiledge.atom.usermanagement.dto.PageDto;
import com.agiledge.atom.usermanagement.service.PageService;

/**
 * Servlet implementation class GroupUrls
 */
 
public class GroupUrls extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GroupUrls() {
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
			 	
			 	String mainUrl = request.getParameter("mainPageDrp");
			 	String assignedUrl[] = request .getParameter ("assignedPageDrp").replace("\"","").replace("[", "").replace("]", "" ).split(",");
			 	 
			 System.out.println("in side groupurls length " + assignedUrl   );	 
			 	 
			 	  
			 	  
			 	 
			 	ArrayList<PageDto> assignedList = new ArrayList<PageDto > ();
			 	for(int i=0; i<assignedUrl.length ; i++) {
			 		PageDto dto = new PageDto();
			 		dto .setId(assignedUrl[i]);
			 		dto.setParentPageId(mainUrl);
			 		System.out.println(" inside Group Url id:"+ dto.getId() + " parentPageId : " + dto.getParentPageId() );
			 		assignedList.add(dto);
			 		
			 	}
			 	
			  
			  
			 			
			 	
			 	PageService service = new PageService();
			 	int returnInt =0;
			 	  returnInt = service.groupAssignedUrls(assignedList);
			 	if(returnInt>0) {
			 		json.put("result", "true");
			 		json.put("message",  "Grouping successful.");
			 	} else {
			 		json.put("result", "false");
			 		json.put("message",  "Grouping failed.");
			 	}
			  
			 
		 } catch(Exception e) {
			 try {
		 			json.put("result", "false");
		 		 
					json.put("message",  "Error :"+ e);
				 
			 } catch(org.json.JSONException jsonException) {
				 response.getWriter().write("{result:\"true\", message:\"Error :" + jsonException + "\" }");
			 }
		 }
		 
		 response.getWriter().write(json.toString());
	}

}
