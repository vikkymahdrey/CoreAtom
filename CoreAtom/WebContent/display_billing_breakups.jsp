<%@page import="com.agiledge.atom.dto.TripDetailsDto"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.itextpdf.text.log.SysoLogger"%>
<%@page import="com.agiledge.atom.reports.TripBillingBreakUpsReportHelper"%>
<%@page import="com.agiledge.atom.dao.SiteDao"%>
<%@page import="com.agiledge.atom.dto.SiteDto"%>
<%@page import="java.util.Calendar"%>
<%@page import="java.util.List"%>
<%@page import="com.agiledge.atom.dto.PayrollDto"%>
<%@page import="com.agiledge.atom.service.PayrollService"%>
<%@page import="com.agiledge.atom.commons.OtherFunctions"%>
<%@page import="com.agiledge.atom.dao.OtherDao"%>
<%@ page contentType="text/html; charset=utf-8" language="java"
	import="java.sql.*" errorPage="error.jsp"%>
	<%@ taglib uri="http://www.nfl.com" prefix="disp"%>
<%@taglib uri="http://displaytag.sf.net" prefix="display"%>	
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Billing Break Ups</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<script type="text/javascript" src="js/jquery-latest.js"></script>

<link href="css/style.css" rel="stylesheet" type="text/css" />
<link href="css/menu.css" rel="stylesheet" type="text/css" />

<script type="text/javascript">
	$(document).ready( function() {
		var i=0;
		$("#row1").children("tbody").children().each(function(){
			if(i%2==0) {
				$(this).attr("class","even");
				
			} else 
			{
				$(this).attr("class","odd");
			}
			
			i++;
		});
	});
function closeMyParent(id) {
	$("#"+id).parent().remove();
}
</script>

<!-- Beginning of compulsory code below -->
 
</head>
<body>
	<%
        long empid = 0;
        String employeeId = OtherFunctions.checkUser(session);        
            empid = Long.parseLong(employeeId);
            %>
	<%@include file="Header.jsp"%>



	<div id="body">
		<div class="content">

			<h2 align="center">Billing Break Ups</h2>

			<form id="billingBreaUpsDetails" name="billingBreaUpsDetails"
				method="post" action="display_billing_breakups.jsp">
				<table width="529">
					<tr>
						<td>Site</td>
						<td width="20%">Month</td>
						<td>Year</td>

					</tr>

					<tr>
						<td>
							<select name="siteId" id="siteId">
									<%
									String siteID=request.getParameter("siteId");
									siteID=siteID==null?"":siteID;
									String site=(request.getSession().getAttribute("site")==null||request.getSession().getAttribute("site").toString().trim().equals(""))?"" :request.getSession().getAttribute("site").toString().trim();
									 List<SiteDto> siteDtoList = new SiteDao().getSites();  
									 
									if(siteID!=null&&siteID.trim().equals("")==false)
									{
										site=siteID;
									} 
									for (SiteDto siteDto : siteDtoList) {
									
									String siteSelect="";
									if(site.equals(siteDto.getId()))
									{
										siteSelect="selected";
									}
							 
								%>

									<option <%=siteSelect %> value="<%=siteDto.getId()%>"><%=siteDto.getName()%></option>
									<%  }%>
							</select>
						</td>
						<td width="20%"><select name="month" id="month">
								<%
							
								 
							 
							 
									String monthParam=request.getParameter("month")==null?"":request.getParameter("month");
									String yearParam=request.getParameter("year")==null?"":request.getParameter("year");
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
							
												selected = "selected";
											} else {
												selected = "";
											}
											out.print("<option " + selected + " value=" + i + ">"
													+ monthLabel[i - 1] + "</option>");
										}
									%>
						</select></td>
						<td align="left"><select name="year" id="year">
								<%
								
                                        int i = year - 5;
										boolean yearSet=false;
                                        for (; i < year; i++) {
                                        	String yearSelect="";
                                        	
                                        	if(yearParam.equals(String.valueOf(i)))
                                        	{
                                        		yearSelect= "selected";
                                        		yearSet=true;
                                        	}

                                            out.print("<option  "+yearSelect+"  value=" + i + ">" + i + "</option>");
                                        }
                                        if(yearSet) {
                                        	   out.print("<option     value=" + i + ">" + i + "</option>");
                                        	
                                        } else {
                                        	   out.print("<option  selected  value=" + i + ">" + i + "</option>");
                                        }
                                     
                                    %>

						</select></td>

					</tr>
					<tr>

						<td width="20%" align="right">
							 
						</td>
						<td><input type="submit" class="formbutton" name="Submit"
							id="Search" value="Submit" /> <input type="button"
							class="formbutton" onclick="javascript:history.go(-1);"
							value="Back" /></td>

					</tr>
				</table>
			</form>
			<div id="ReportDiv">
			
			<%
			try {
				TripBillingBreakUpsReportHelper helper = new TripBillingBreakUpsReportHelper();
				System.out.println("month : "+ monthParam +  "\n Year : "+ yearParam + " site : " + siteID);
		ArrayList<TripDetailsDto> dtoList=	helper.getBillBreakUpsReport(Integer.parseInt(monthParam), Integer.parseInt( yearParam), Integer.parseInt( siteID));
		System.out.println("size :  " + dtoList.size());
			%>
			
		 
 				
				<h>///////////////////</h>
				<br/>
				<disp:dipxTable id="row1" styleClass="alternateColor"  list="<%=dtoList %>">
						<disp:dipxColumn title="Date" 
					  sortable="true" headerClass="sortable"  property="trip_date"  />
					  					  <disp:dipxColumn title="Trip#" 
					  sortable="true" headerClass="sortable"  property="trip_code"  />
					  <disp:dipxColumn title="Vehicle" 
					  sortable="true" headerClass="sortable"  property="vehicle_type"  />
					  <disp:dipxColumn title="Vehicle#" 
					  sortable="true" headerClass="sortable"  property="vehicleNo"  />
							  <disp:dipxColumn title="Escort#" 
					  sortable="true" headerClass="sortable"  property="escortNo"  />
					    <disp:dipxColumn title="Escort" 
					  sortable="true" headerClass="sortable"  property="escort"  />
					    <disp:dipxColumn title="Shift" 
					  sortable="true" headerClass="sortable"  property="trip_time"  />
					    <disp:dipxColumn title="Reporting Time" 
					  sortable="true" headerClass="sortable"  property="actualLogTime"  />
					    <disp:dipxColumn title="Reporting Time" 
					  sortable="true" headerClass="sortable"  property="actualLogTime"  />
					   <disp:dipxColumn title="Status" 
					  sortable="true" headerClass="sortable"  property="onTimeStatus"  />
					  <disp:dipxColumn title="Approval Status" 
					  sortable="true" headerClass="sortable"  property="approvalStatus"  />
					  
					   <disp:dipxColumn title="Seats" 
					  sortable="true" headerClass="sortable"  property="sittingCapasity"  />
					   <disp:dipxColumn title="Boarded" 
					  sortable="true" headerClass="sortable"  property="showCount"  />
				 <disp:dipxColumn title="No Shows" 
					  sortable="true" headerClass="sortable"  property="noShowCount"  />
					  		 <disp:dipxColumn title="No Shows" 
					  sortable="true" headerClass="sortable" expandable="true" property="comment"  />
					  
					  	<disp:dipxTable styleClass="inner displaytag" style="margin-left:20%; border=1; width:60%; background: white;" id="inner-downloaded"
						property="comments" type="inner"
						parentProperty="comment">
							<disp:dipxColumn title="Date" 
						  sortable="true" headerClass="sortable"  property="commentedDate"  />
								<disp:dipxColumn title="Commented By" 
						  sortable="true" headerClass="sortable"  property="commentedByName"  />
						  	<disp:dipxColumn title="Comment" 
						  sortable="true" headerClass="sortable"  property="comment"  />
						  
						  
						</disp:dipxTable>
					  
				</disp:dipxTable>
					Export Options : <a href="download_display_billing_breakups.jsp?month=<%=monthParam %>&year=<%=yearParam %>&siteId=<%=siteID %>" >Excel</a>
			<%
			}catch(Exception e) {
				System.out.print("error in display_billing_breakups :" +e);
			}
			 %>
		
			</div>







			<table style="height: 250px">
				<tr>
					<td width="310"></td>
					<td width="250"></td>
				</tr>

			</table>
			<%@include file="Footer.jsp"%>
		</div>
	</div>
</body>
</html>
