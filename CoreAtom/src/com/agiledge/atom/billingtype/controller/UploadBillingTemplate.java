package com.agiledge.atom.billingtype.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.agiledge.atom.billingtype.config.dto.KmBasedBillingConfigDto;
import com.agiledge.atom.billingtype.config.dto.KmTemplateDto;
import com.agiledge.atom.billingtype.config.service.KmBasedBillingTypeConfigService;
import com.agiledge.atom.billingtype.config.service.KmBasedTemplateBillingService;


/**
 * Servlet implementation class UploadBillingTemplate
 */
 
public class UploadBillingTemplate extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UploadBillingTemplate() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		processRequest(request, response);
	}

	private void processRequest(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		// TODO Auto-generated method stub
		KmTemplateDto kmTemplate = new KmTemplateDto();
		String refId = null;
		String billingId="";
		String templateName="";
		System.out.println(" Refid : " + refId);

		System.out.println("..................");
		FileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(factory);
		 InputStream in = null;
		 
		try {
			List<FileItem> list =null;// upload.parseRequest(request);
			 for(FileItem item : list) {
				 String fieldName=item.getFieldName();
				 System.out.println("Filed name : "+ fieldName);
				 
				 if(item.isFormField()) {
					 
					 
					 String fieldValue=item.getString();
					 System.out.println("value "+ fieldValue);
					 if(fieldName.equals("refId")) {
						 refId=fieldValue;
					 }
					 else if (fieldName.equals("billingId" )) {
						 billingId = fieldValue;
					 } else if (fieldName.equals("templateName")) {
						 templateName=fieldValue;
					 }
				 }else {
					 //java.io.InputStream in = item.getInputStream();
					 long length =  item.getSize();
					 byte b[] =item.get();
					 String content = new String(b);
					 String fieldValue=content;
					 System.out.println("value "+ fieldValue);
					 if( fieldName.equals("templateUrl")) {
						 in =item.getInputStream();
					 }


					  
					 
				 }
				 
			 }
			 
			 KmBasedBillingConfigDto bdto = new KmBasedBillingTypeConfigService().getKmBasedBillingConfig(refId);
				kmTemplate.setSite(bdto.getSite());
				kmTemplate.setDoneBy(request.getSession().getAttribute("user").toString());
				kmTemplate.setBillingRefId(billingId);
				kmTemplate.setTemplateName(templateName);
				
			 System.out.println(" IN >>>>");
			 if(in==null) {
				 System.out.println(" IN is null");
			 }
			 
	 	 if(new KmBasedTemplateBillingService().processTemplate(kmTemplate, in)>0) {
			 request.getSession().setAttribute("status",
						"<div class=\"success\" width=\"100%\" > Template uploaded successfully</div>"); 
		 }else {
			 request.getSession().setAttribute("status",
						"<div class=\"failure\" width=\"100%\" > Template uploading failed : Please check format and apl validity</div>"); 
		 }
		}catch(Exception e) {
			System.out.println("Error in UploadBilling Template "+e);
			 request.getSession().setAttribute("status",
						"<div class=\"failure\" width=\"100%\" > Template uploading failed </div>");
		}
		   response.sendRedirect("kmBasedTripBillingConfig.jsp?refId="+refId);
 
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
				
		/*	Map<String,String> map = request.getParameterMap();
			Set<String > keys = map.keySet();
			for(String s : keys) {
				System.out.println("value[" + s + "] : " + map.get(s));
			}*/
		 
		String contentType = request.getContentType();
		System.out.println("COntent type : " + contentType);
 	  	processRequest(request, response);
		 
	}


}
