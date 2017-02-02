package com.agiledge.atom.servlets;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.agiledge.atom.commons.OtherFunctions;
import com.agiledge.atom.dto.EmployeeDto;
import com.agiledge.atom.service.EmployeeService;


/**
 * Servlet implementation class UploadExcel
 */
public class UploadExcel extends HttpServlet {

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public UploadExcel() {
		super();
		// TODO Auto-generated constructor stub
	}

	
	protected void processRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		String contentType = request.getContentType();
		HttpSession session=request.getSession();

		if ((contentType != null)
				&& (contentType.indexOf("multipart/form-data") >= 0)) {
			 
			 
			 
			DataInputStream in = new DataInputStream(request.getInputStream());

			int formDataLength = request.getContentLength();
			
			byte dataBytes[] = new byte[formDataLength];

			int byteRead = 0;
		//	String site =OtherFunctions.getRequestedFields( request,"site");
			String site="1";
			boolean valid =true;
			String validationMessage ="";
			System.out.println(" Site :" + site);
			if(site==null||site.equals("")) {
				valid = false;
				validationMessage="Site is not selected";
				System.out.println(validationMessage);
			}
		
		
			int totalBytesRead = 0;

			// this loop converting the uploaded file into byte code
			while (totalBytesRead < formDataLength) {

				byteRead = in.read(dataBytes, totalBytesRead, formDataLength);
				totalBytesRead += byteRead;

			}
			String file = new String(dataBytes);

			// for saving the file name
			String saveFile = file.substring(file.indexOf("filename=\"") + 10);

			saveFile = saveFile.substring(0, saveFile.indexOf("\n"));

			saveFile = saveFile.substring(saveFile.lastIndexOf("\\") + 1,
					saveFile.indexOf("\""));
			
			// retreving site 
			String site1=  file.substring(file.indexOf("name=\"site\"") + "name=\"site\"".length() );
			 
			System.out.println("......."+ site1.substring(0,7));
			site1 = site1.substring(site1.indexOf("\r\n")+2);
			site1 = site1.substring(site1.indexOf("\r\n")+2   );
			site1 = site1.substring(0, site1.indexOf("\r\n") );
			System.out.println("actual site :" + site1);
			site =site1;

			 String sendMail=  file.substring(file.indexOf("name=\"sendMail\"") + "name=\"sendMail\"".length() );
			 try {
			sendMail = sendMail.substring(sendMail.indexOf("\r\n")+2);
			sendMail = sendMail.substring(  sendMail.indexOf("\r\n")+2);
			sendMail = sendMail.substring(0, sendMail.indexOf("\r\n") );

			 }catch(ArrayIndexOutOfBoundsException e ) {
				 sendMail="off";
			 }
			 if(OtherFunctions.isEmpty(sendMail)) {
				 sendMail = "off";
			 }
				System.out.println("actual sendMail :" + sendMail);
			/*String firstString[] = file.split("sendMail");
			
System.out.println("sendMail back " + firstString[0].substring(firstString[0].length()-10) );
System.out.println("sendMail front " + firstString[1] );*/

			int lastIndex = contentType.lastIndexOf("=");

			String boundary = contentType.substring(lastIndex + 1,
					contentType.length());
			
			int pos;
try{
			// extracting the index of file
			pos = file.indexOf("filename=\"");

			pos = file.indexOf("\n", pos) + 1;

			pos = file.indexOf("\n", pos) + 1;

			pos = file.indexOf("\n", pos) + 1;

			int boundaryLocation = file.indexOf(boundary, pos) - 4;

			int startPos = ((file.substring(0, pos)).getBytes()).length;

			int endPos = ((file.substring(0, boundaryLocation)).getBytes()).length;

			// creating a new file with the same name and writing the

			System.out.println("Save File Name: " + saveFile);
			FileOutputStream fileOut = new FileOutputStream(saveFile);

			fileOut.write(dataBytes, startPos, (endPos - startPos));

			fileOut.flush();
			fileOut.close();
}catch(FileNotFoundException fnf) {
	System.out.println("No file");
	valid=false;
	validationMessage="No file choosen";
}
			EmployeeService employeeService = new EmployeeService();
			int returnint=0;
			String overrite="true";

			if(valid) {
				returnint=employeeService.processXLFile(saveFile, site, overrite, sendMail);
			}
			String html="<html><script type=\"text/javascript\"> function doaction() { window.close(); opener.location.reload();	} </script><link href=\"css/style.css\" rel=\"stylesheet\" type=\"text/css\" /><body><center><h1>%s!</h1></center><div></div><center><input type=\"button\" class=\"formbutton\" onClick=\"doaction()\" value=\"Close\"/></center></body></html> ";
			PrintWriter out = response.getWriter();
			
			if(returnint>0)
			{
				 
				session.setAttribute("status",
						"<div class='success'>" + employeeService.getMessage() + "!</div");
				out.write(String.format(html, employeeService.getMessage()));
				out.flush();
				
				  
			}else 
			{
				if(valid) {
				session.setAttribute("status",
						"<div class='failure'>" + employeeService.getMessage() + "</div");
				 
				} else {
					session.setAttribute("status",
							"<div class='failure'>Validation Failure :" + validationMessage + "!</div");
				}
				out.write(String.format(html, employeeService.getMessage()));
				out.flush();
				  
			}
		}

	}

	/* get requested fields */
	

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		processRequest(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		processRequest(request, response);
	}

}
