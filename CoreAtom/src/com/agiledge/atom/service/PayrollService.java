/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.agiledge.atom.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import com.agiledge.atom.commons.OtherFunctions;
import com.agiledge.atom.dao.PayrollDao;
import com.agiledge.atom.dbConnect.DbConnect;
import com.agiledge.atom.dto.EmployeeDto;
import com.agiledge.atom.dto.PayrollDto;
import com.agiledge.atom.dto.PayrollReportDto;


//import org.apache.commons.

/**
 * 
 * @author 123
 */
public class PayrollService {

	PayrollDao dao = new PayrollDao();

	public List<PayrollReportDto> getPayrollDetails(int month, int year, int site) {
System.out.println("cccccccccccc");
		List<PayrollReportDto> finalPayrollList = new ArrayList<PayrollReportDto>();
		EmployeeDto preEmployee = null;
		double amount = 0;
		PayrollDto preDto = null;
		 List<PayrollDto> dtoList = null;
		 System.out.println("reached ..");
		dtoList  = dao.getPayrollDetails(month, year,site);
		System.out.println("NULvvvvvvvvvvvvv              "+dtoList==null);
		if(dtoList==null||dtoList.size()==0) {
			dtoList = dao.getLivePayrollDetails(month, year,site);
		}
				int i = 0, j = 0;
		;
		boolean flag = false;
		PayrollReportDto prd = null;
		String currentEmp = null;
		EmployeeDto empDto = null;
		for (PayrollDto dto : dtoList) {

			empDto = dto.getEmployee();
			// System.out.println("Current " + currentEmp);
			// System.out.println("empid " + empDto.getEmployeeID());
			if (currentEmp == null) {
				currentEmp = empDto.getEmployeeID();
			} else if (!currentEmp.equals(empDto.getEmployeeID())) {

				// System.out.println(prd.getDetails());
				finalPayrollList.add(prd);
				flag = false;
				currentEmp = empDto.getEmployeeID();
			}

			if (!flag) {

				prd = new PayrollReportDto();
				prd.setId(dto.getId());
				prd.setEmployeeID(empDto.getEmployeeID());
				prd.setEmployeeName(empDto.getEmployeeFirstName() + ","
						+ empDto.getEmployeeLastName());
				prd.setRoll(empDto.getEmpType());
				ArrayList<PayrollReportDto.DetailClass > details = new ArrayList<PayrollReportDto.DetailClass >();
				PayrollReportDto.DetailClass detailClass= prd.new DetailClass();
				detailClass.setAmount(dto.getAmount());
				detailClass.setId(dto.getId());
				detailClass.setMonth(dto.getMonth());
				detailClass.setStatus(dto.getStatus());
				detailClass.setUpdatedBy(dto.getUpdatedBy());
				detailClass.setUpdateDate(dto.getUpdatedDate());
				detailClass.setReason(dto.getReason());
				 
				//details.put( dto.getMonth(), detailClass);
				details.add(detailClass);
				 
				 

				prd.setDetails(details);
				prd.setTotalAmount(dto.getAmount());
				flag = true;

			} else {

				ArrayList<PayrollReportDto.DetailClass >details  =  prd.getDetails();
				PayrollReportDto.DetailClass detailClass= prd.new DetailClass();
				detailClass.setAmount(dto.getAmount());
				detailClass.setId(dto.getId());
				detailClass.setMonth(dto.getMonth());
				detailClass.setStatus(dto.getStatus());
				detailClass.setUpdatedBy(dto.getUpdatedBy());
				detailClass.setUpdateDate(dto.getUpdatedDate());
				detailClass.setReason(dto.getReason());
				
				details.add(  detailClass);
				 
				prd.setTotalAmount(prd.getTotalAmount() + dto.getAmount());

			}

		}

		finalPayrollList.add(prd);

		// System.out.println("payroll List size" + finalPayrollList.size());
		

		return finalPayrollList;

	}
	
	// to get ui part for payroll output report
	public String getPayrolUI(int month, int year, int site)
	{
		String out= "<div width='100%' style='text-align:center;'  ><div alighn='center'  style='width:80%; margin-left:8%; '  border=1  >  <table alighn='center'  class='alternateColor'   width=\"50%\"  > "
				+ "	<tr> "
				+ "	<th align=\"center\"   nowrap=\"nowrap\">Staff Id</th>"
				+ " <th align=\"center\" width=\"15%px\"  >Staff Name</th>"
				+ "<th align=\"center\" nowrap=\"nowrap\">Status</th> "
				+ "<th align=\"center\" width=\"140px\"  >Month Charged For </th> "
				+ "<th align=\"center\" width=\"70px\"  nowrap=\"nowrap\">Amount</th> "
				+ "<th align=\"center\"  nowrap=\"nowrap\">Updated By</th> "
				+ "<th align=\"center\"  nowrap=\"nowrap\">Updated On</th> "
				+ "<th align=\"center\"  nowrap=\"nowrap\">Reason</th> "
				+ "<th align=\"center\"   nowrap=\"nowrap\"> Total Amount </th> "

				+ "  </tr> ";

		 		PayrollService service = new PayrollService();
		List<PayrollReportDto> dtoList = service.getPayrollDetails(month,
				year,site);
		Collections.sort(dtoList, new Comparator<PayrollReportDto>(){

			@Override
			public int compare(PayrollReportDto o1, PayrollReportDto o2) {
 
				System.out.println(" amount 1 " + o1.getTotalAmount() + " amount 2 " + o2.getTotalAmount());
				if(o1.getTotalAmount()<o2.getTotalAmount()) {
					return 1;
				} else if(o1.getTotalAmount()>o2.getTotalAmount()) {
					return -1;
				} else {
					return 0;
				}
			}
			
		});

		int i = 0;
		if (dtoList.size() > 0) {
			for (PayrollReportDto dto : dtoList) {

				String color = "#FAF3EB";
				if (i++ % 2 == 0) {

					color = "white";

				}

				out+="    	<tr  style=' background-color:" + color
						+ ";' > " + "<td align=\"center\"> ";
				out+=dto.getEmployeeID()
						+ "</td><td align=\"center\"> ";

				out+=dto.getEmployeeName();
				out+="</td> ";

				out+="<td align=\"center\"> ";

				out+=dto.getRoll();
				out+="</td> ";

				out+="<td colspan=5 >";

				out+="<table   >";

				ArrayList<PayrollReportDto.DetailClass >details  =    dto.getDetails();
				// System.out.println("Map size is: " + details.size());
				for ( PayrollReportDto.DetailClass detailClass  :details) {
					 
				 
					out+="<tr><td  width=\"140px\"  align=\"center\" >";
					out+= detailClass.getMonth() +   "</td>";
					if(detailClass.getStatus().equalsIgnoreCase("fixed"))
					{
						out+="<td  width=\"70px\"  align=\"center\"><a id=\""+detailClass.getId()+"\" title=\"Click here to edit\" onclick=\"showEditAmount("+detailClass.getId()+")\"    >" + detailClass.getAmount()
							+ "</a></td  ><td  align=\"center\" ></td ><td  align=\"center\" ></td><td  align=\"center\" ></td>";
					} else {
						out+="<td align=\"center\"><a style=\"color:#000;\" id=\""+detailClass.getId()+"\"   >" + detailClass.getAmount()
								+ "</a></td>";
						out+="<td align=\"center\">" + detailClass.getUpdatedBy()
								+ "</td>";
						out+="<td align=\"center\">" + OtherFunctions.changeDateFromatToNormalWithTime( detailClass.getUpdateDate())
								+ "</td>";
						out+="<td align=\"center\">" + detailClass.getReason()
								+ "</td>";
					}
						out+="</tr>";
					// / System.out.println(key);
				}
				out+="</table>";

				out+="</td>";

				out+="<td id=\""+dto.getId()+"-total\" align=\"center\"> ";
				out+=dto.getTotalAmount();

				out+="</td></tr>";
			}
		} else {
			out+="            <tr><td align=\"center\"> No transaction found ! "
					+ "</td> " + "</tr> ";

		}

		return out+=" </table> </div></div> ";

	}

	public int makePayroll() {

		int i=0;
		try{
		SettingsService settingService = new SettingsService();
		String accountingDate = settingService.getCurrentAccountingDate();
		System.out.println(accountingDate);
		//Calendar cal = Calendar.getInstance();
		String currentDate="";
		//String dat="";
		Date myDate = new Date();
		currentDate =new SimpleDateFormat("yyyy-M-dd").format(myDate);
		/*if(cal.get(Calendar.DATE)<10)
		{
			dat="0"+cal.get(Calendar.DATE);
		}
		else
			{
			dat=Integer.toString(cal.get(Calendar.DATE));
			}*/
		
		/*String currentDate = cal.get(Calendar.YEAR) + "-"
				+ (cal.get(Calendar.MONTH) + 1) + "-" + dat;*/
		System.out.println(currentDate);
		// System.out.println("\nCurrent Date " + currentDate
		// + "\n AccountingDate : " + accountingDate);
		// Date toDate = OtherFunctions.convertWindowsDateToDate(currentDate);
		// Date acDate =
		// OtherFunctions.convertWindowsDateToDate(accountingDate);
		// toDate = OtherFunctions.resetTime(toDate);
		// acDate = OtherFunctions.resetTime(acDate);
		if (accountingDate.compareTo(currentDate) <= 0) {
			// System.out.println("Payroll Running....");
			System.out.println("i m in payroll");
			i= dao.makePayroll(accountingDate);
		} else {
			i=0;
		}
		}catch(Exception e){
			System.out.println("error in payroll service"+e);
			
		}
		return i;

	}

	/* The method to update payroll amount
	 * 
	 */
	public int updatePayrollAmount(PayrollDto dto) {
		// TODO Auto-generated method stub
		
		return dao.updatePayrollAmount(dto);
	}
	
	/* get payroll details for excel sheet
	 */
	// to get ui part for payroll output report
	public String getPayrollForExcel(int month, int year, int site)
	{
		String out= "<div width='100%' style='text-align:center;'  ><div alighn='center'  style='width:80%; margin-left:8%; '  border=1  >  <table alighn='center'  style='background-color:#F0E4E4;'   width=\"50%\"  > "
				+ "	<tr> "
				+ "	<th align=\"center\"   nowrap=\"nowrap\">Staff Id</th>"
				+ " <th align=\"center\" width=\"15%px\"  >Staff Name</th>"
				+ "<th align=\"center\" nowrap=\"nowrap\">Status</th> "
				+ "<th align=\"center\" width=\"140px\"  >Month Charged For </th> "
				+ "<th align=\"center\" width=\"70px\"  nowrap=\"nowrap\">Amount</th> "
				+ "<th align=\"center\"  nowrap=\"nowrap\">Updated By</th> "
				+ "<th align=\"center\"  nowrap=\"nowrap\">Updated On</th> "
				+ "<th align=\"center\"  nowrap=\"nowrap\">Reason</th> "
				+ "<th align=\"center\"   nowrap=\"nowrap\"> Total Amount </th> "

				+ "  </tr> ";

		 		PayrollService service = new PayrollService();
		List<PayrollReportDto> dtoList = service.getPayrollDetails(month,
				year,site);
		int i = 0;
		if (dtoList.size() > 0) {
			for (PayrollReportDto dto : dtoList) {

				String color = "#FAF3EB";
//				if (i++ % 2 == 0) {

					color = "white";

	//			}

				out+="    	<tr  style=' background-color:" + color
						+ ";' > " + "<td align=\"center\"> ";
				out+=dto.getEmployeeID()
						+ "</td><td align=\"center\"> ";

				out+=dto.getEmployeeName();
				out+="</td> ";

				out+="<td align=\"center\"> ";

				out+=dto.getRoll();
				out+="</td> ";

				out+="<td colspan=5 >";

				out+="<table   >";

				ArrayList<PayrollReportDto.DetailClass >details  =    dto.getDetails();
				// System.out.println("Map size is: " + details.size());
				for ( PayrollReportDto.DetailClass detailClass  :details) {
					 
				 
					out+="<tr><td  width=\"140px\"  align=\"center\" >";
					out+= detailClass.getMonth() +   "</td>";
					if(detailClass.getStatus().equalsIgnoreCase("fixed"))
					{
						out+="<td  width=\"70px\"  align=\"center\"><a id=\""+detailClass.getId()+"\" title=\"Click here to edit\" onclick=\"showEditAmount("+detailClass.getId()+")\"    >" + detailClass.getAmount()
							+ "</a></td  ><td  align=\"center\" ></td ><td  align=\"center\" ></td><td  align=\"center\" ></td>";
					} else {
						out+="<td align=\"center\"><a style=\"color:#000;\" id=\""+detailClass.getId()+"\"   >" + detailClass.getAmount()
								+ "</a></td>";
						out+="<td align=\"center\">" + detailClass.getUpdatedBy()
								+ "</td>";
						out+="<td align=\"center\">" + OtherFunctions.changeDateFromatToNormalWithTime( detailClass.getUpdateDate())
								+ "</td>";
						out+="<td align=\"center\">" + detailClass.getReason()
								+ "</td>";
					}
						out+="</tr>";
					// / System.out.println(key);
				}
				out+="</table>";

				out+="</td>";

				out+="<td id=\""+dto.getId()+"-total\" align=\"center\"> ";
				out+=dto.getTotalAmount();

				out+="</td></tr>";
			}
		} else {
			out+="            <tr><td align=\"center\"> No transaction found ! "
					+ "</td> " + "</tr> ";

		}

		return out+=" </table> </div></div> ";

	}

}
