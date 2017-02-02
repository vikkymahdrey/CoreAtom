package com.agiledge.atom.servlets.employee;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.agiledge.atom.commons.PasswordGenerator;
import com.agiledge.atom.dto.EmployeeDto;
import com.agiledge.atom.mail.SendMail;
import com.agiledge.atom.mail.SendMailFactory;
import com.agiledge.atom.service.EmployeeService;
import com.agiledge.atom.service.SettingsService;


/**
 * Servlet implementation class AddExternalEmployee
 */
public class AddExternalEmployee extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddExternalEmployee() {
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
		String subject="You have been added to ATOm application";
		EmployeeDto dto=new EmployeeDto();
		String personnelno=request.getParameter("personnelno");
		String firstname=request.getParameter("firstname");
		String middlename=request.getParameter("middlename");
		String lastname=request.getParameter("lastname");
		String displayname=request.getParameter("displayname");
		String gender=request.getParameter("gender");
		String phonenumber=request.getParameter("phoneno");
		String emailid=request.getParameter("email");
		String doj=request.getParameter("dateofjoin");
		String address=request.getParameter("address");
		String loginid=request.getParameter("loginid");
		String usertype=request.getParameter("usertype");
		String authtype=request.getParameter("authtype");
		String contract=request.getParameter("contract");
		String project=request.getParameter("project");
		String lineManager=request.getParameter("supervisorID1");		
		String password=new PasswordGenerator().randomString(10);
		System.out.println("Password"+password);
		dto.setPassword(password);
		dto.setPersonnelNo(personnelno);
		dto.setEmployeeFirstName(firstname);
		dto.setEmployeeMiddleName(middlename);
		dto.setEmployeeLastName(lastname);
		dto.setDisplayName(displayname);
		dto.setGender(gender);
		dto.setContactNo(phonenumber);
		dto.setEmailAddress(emailid);
		dto.setDateOfJoining(doj);
		dto.setAddress(address);
		dto.setLoginId(loginid);
		dto.setUserType(usertype);
		dto.setAuthtype(authtype);
		dto.setContract(contract);
		dto.setProjectid(project);
		dto.setLineManager(lineManager);
		int result=new EmployeeService().insertExternalEmployee(dto);
		if (result==1) {
        String message=SettingsService.getAddExternalEmployeeMessage(dto);
        SendMail mail = SendMailFactory.getMailInstance();
        String bcc[]={"manjula@agiledgesolutions.com"};
        mail.setBccs(bcc);
        mail.send(dto.getEmailAddress(),subject, message);
			session.setAttribute("status",
					"<div class='success'>Employee Added Successfully</div");
		} else {
			session.setAttribute("status",
					"<div class='failure'>Operation Failed</div");
		}
		response.sendRedirect("viewallExternal.jsp");
		
		
		
		
		
				
	}

}
