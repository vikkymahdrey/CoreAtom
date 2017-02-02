



<%@page import="com.agiledge.atom.reports.dto.AgileBillDto"%>
<%@page import="java.util.Date"%>
<%@page import="com.agiledge.atom.reports.dto.OtaOtdDto"%>
<%@page import="com.agiledge.atom.reports.dto.OnTimeTripCountDto"%>
<%@page import="com.itextpdf.text.log.SysoLogger"%>
<%@page import="com.agiledge.atom.reports.dto.TripSheetNoShowCountReportDto"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Calendar"%>
<%@page import="com.agiledge.atom.reports.TripSheetNoShowCountReportHelper"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
<title>Agiledge Billing Report</title>

<script type="text/javascript" src="js/jquery-latest.js"></script>
<style type="text/css">
@import "css/jquery.datepick.css";
</style>
<script type="text/javascript" src="js/jquery.datepick.js"></script>
 
 


</head>
<body>
	<%
	String fname1=("Agile Billing Report :").concat(new Date().toString()).concat(".csv");
	String fname2=("Agile Billing Report :").concat(new Date().toString()).concat(".xls");
	String fname3=("Agile Billing Report :").concat(new Date().toString()).concat(".xml");
		String monthParam = request.getParameter("month");
		String yearParam= request.getParameter("year");
		 
		monthParam=monthParam==null?"":monthParam;
		yearParam=yearParam==null?"":yearParam;
		 		 List<AgileBillDto> dtoList = null;
		int monthValue=-1;
		int yearValue=2013;
		try{
			yearValue=Integer.parseInt(yearParam);
			monthValue=Integer.parseInt(monthParam);
		}catch(NumberFormatException e)
		{
			;
		}
		 	if(yearParam.equals("")==false)	 
			// dtoList= new TripSheetNoShowCountReportHelper().getAgileBill(monthValue, yearValue);
		 	{}
				
			 

	 
		long empid = 0;
		String employeeId = OtherFunctions.checkUser(session);		
			empid = Long.parseLong(employeeId);
	%>
	<%@include file="Header.jsp"%>
	<div id="body">
		<div class="content">


			 <div>
				<form action="display_AgileBillingReport.jsp">

					<table width="20%">
						<tr>
							  
							<td>Month</td>
							<td>Year</td> 
							<td></td>
						</tr>
						<tr>
							 
							<td><select name="month" id="month">
									<%
							String selectAll="";
								System.out.println("Month: " +request.getParameter("month"));
							if(request.getParameter("month")==null||request.getParameter("month").equals(""))
							{
								selectAll="selected";
							}
							%>
									<option <%=selectAll %> value="">All</option>
									<%
										int month = 0;
										Calendar cal = Calendar.getInstance();
										if (monthParam != null && !monthParam.equals("")) {
											month = Integer.parseInt(monthParam);
										} else {

											month = cal.get(cal.MONTH)+1;
										}
										int curYear = cal.get(cal.YEAR);
										int year = cal.get(cal.YEAR);
										if (yearParam != null && !yearParam.equals("")) {
											year = Integer.parseInt(yearParam);
										} else {
											year = cal.get(cal.YEAR);
										}
										String selected = "";
										String[] monthLabel = { "January", "February", "March", "April",
												"May", "June", "July", "August", "September", "October",
												"November", "December" };
										for (int i = 1; i <= 12; i++) {
											if (i == month) {
												if(selectAll.equals("selected")==false)
												selected = "selected";
											} else {
												selected = "";
											}
											out.print("<option " + selected + " value=" + i + ">"
													+ monthLabel[i - 1] + "</option>");
										}
									%>
							</select></td>
							<td><select name="year" id="year">
									<%
                                        int i = cal.get(cal.YEAR) - 5;
                                        for (; i <= cal.get(cal.YEAR); i++) {
                                        	String yearSelect="";
                                        	if(i==year)
                                        	{
                                        		yearSelect="selected";
                                        	}
                                            out.print("<option  "+yearSelect+"  value=" + i + ">" + i + "</option>");
                                        }
                                        
                                        String groupByLabel[]={"Project", "SPOC","Manager"};
                                        
                                    %>

							</select></td>
							 
							<td><input type="submit" class="formbutton" value="Generate" />
							</td>


						</tr>
					</table>
				</form>
			</div>

			<h2 align="center">Agiledge Billing Report</h2>
			<hr />

			<display:table style="margin-left:5%; width:90%;" class="alternateColor" name="<%=dtoList%>" id="row"
				export="true" defaultsort="1" defaultorder="descending"
				pagesize="25">
				  
				<display:column  style="width:10%;" property="year" title="Year" sortable="true"
					headerClass="sortable" group="1" />
				<display:column  style="width:20%;" property="monthName" title="Month"
					sortable="true" headerClass="sortable"  group="2" />
				
				<display:column  style="width:10%;" property="firstCount" title="Count on 1st" sortable="true"
					 headerClass="sortable" />
				<display:column style="width:10%;" property="middleCount" title="Count on 15th" sortable="true"
					 headerClass="sortable" />
				<display:column style="width:15%;" property="lastCount" title="Count on Last" sortable="true"
					headerClass="sortable" />				
				<display:column style="width:15%;" property="avgCount" title="Average" sortable="true"
					headerClass="sortable" />
				<display:column style="width:10%; text-align:right;   "  property="pricePerSubscription" title="Price Per Subscription" sortable="true"
				format="&#8377; {0,number,0.00}"		headerClass="sortable" />	
				<display:column style="width:10%; text-align:right;" property="totalAmount" title="Total
				" sortable="true"
				 format="&#8377; {0,number,0.00}"	headerClass="sortable" />			
 			</display:table>
			<br />
			<p>
				 
			</p>
			<%@include file="Footer.jsp"%>
		</div>
	</div>

</body>
</html>
