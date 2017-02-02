<%@page import="com.agiledge.atom.dao.EmployeeDao"%>
<%@page import="com.itextpdf.text.log.SysoLogger"%>
<%@page import="com.agiledge.atom.dao.EmployeeDao"%>
<%@page import="com.agiledge.atom.dto.SiteDto"%>
<%@page import="com.agiledge.atom.dao.SiteDao"%>
<%@page import="com.agiledge.atom.dto.EmployeeDto"%>
<%@page import="com.agiledge.atom.service.EmployeeService"%>
<%@page import="org.displaytag.decorator.TotalTableDecorator"%>
<%@page import="java.util.Date"%>
<%@page import="com.agiledge.atom.commons.OtherFunctions"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>


<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>


<%@ taglib uri="http://www.nfl.com" prefix="disp"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<%
	request.setAttribute("contextPath", request.getContextPath());
%>

<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
<title>NoShowReport</title>
<link rel="stylesheet" type="text/css"
	href="${contextPath}/css/displaytag.css" />
	<link href="css/style.css" rel="stylesheet" type="text/css" />
<link href="css/menu.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="js/dispx.js"></script>
<script type="text/javascript" src="js/jquery-latest.js"></script>
<style type="text/css">
@import "css/jquery.datepick.css";
</style>
<script type="text/javascript" src="js/jquery.datepick.js"></script>
<script type="text/javascript">

function resetFields()
{
	$("input[type=hidden]").val("");
	$("input[type='text']").val("");
}
</script>

<script type="text/javascript">        
            $(document).ready(function()
            {                                                                        
                $("#fromDate").datepick();
                $("#toDate").datepick();
            });     
        </script>
<script type="text/javascript">
            function validate()
            {
                  
                var fromdate=document.getElementById("fromDate").value;
                var todate=document.getElementById("toDate").value;
                if(fromdate.length<1)
                {
                    alert("Choose From Date");
                  //  date.focus();
                    return false;
                        
                }
                if(todate.length<1)
                {
                    alert("Choose To Date");
                  //  date.focus();
                    return false;
                        
                }
                 else
                {
                    return true;                            
                }
               
            }
        </script>


</head>
<body>
	
			<%
			String fname1 = ("TransadminNoshowReport :").concat(new Date().toString()).concat(".csv");
         	String fname2 = ("TransadminNoshowReport :").concat(new Date().toString()).concat(".xls");
	        String fname3 = ("TransadminNoshowReport:").concat(new Date().toString()).concat(".xml");

	        String fromDate = request.getParameter("fromDate");
			String toDate = request.getParameter("toDate");
			String siteID = request.getParameter("siteId");
		 
			long empid = 0;
			String employeeId = OtherFunctions.checkUser(session);
			empid = Long.parseLong(employeeId);
			%>
			  <%@include file="Header.jsp"%>
<div id="body">
		<div class="content">
			<div>
				<form name="form1" action="transadminNoshowReport.jsp"
					method="POST" onsubmit="return validate()">
					<table>
						<tr>
							<td>Choose Site</td>
							<td><select name="siteId" id="siteId">
									<%
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
							</select></td>



							<td align="right">From Date</td>
							<td align="left" colspan="2"><input name="fromDate"
								id="fromDate" type="text" size="6" readonly="readonly"
								class="{showOtherMonths: true, firstDay: 1, dateFormat: 'yyyy-mm-dd',
	                                                 minDate: new Date(2008, 12 - 1, 25)}"
								value="<%=fromDate!=null&&fromDate.trim().equals("")==false?fromDate:"" %>" />

							</td>
							<td align="right">To Date</td>
							<td align="left"><input name="toDate" id="toDate"
								type="text" size="6" readonly="readonly"
								class="{showOtherMonths: true, firstDay: 1, dateFormat: 'yyyy-mm-dd',
                                                 minDate: new Date(2008, 12 - 1, 25)}"
								value="<%=toDate!=null&&toDate.trim().equals("")==false?toDate:"" %>" />


							</td>
<td>&nbsp;</td>
							<td><input type="submit" class="formbutton"
								value="Generate Report" /> <input type="button"
								class="formbutton" value="Reset" onclick="resetFields();" /></td>
</tr>
					</table>
				</form>
			</div>


			<br />


		

			<h2 align="center" style="color:teal;">NoShow Employee Report </h2>
			<hr />
			<%
			EmployeeDto dto=new EmployeeDto();
	
			
			EmployeeService reportHelper = new EmployeeService();
			
			ArrayList<EmployeeDto> dtoList = null;
			
			 
				dtoList=reportHelper.NoshowReport(fromDate, toDate);
		    	
			if(dtoList!=null&&dtoList.size()>0) {
				TotalTableDecorator grandTotals = new TotalTableDecorator();
				pageContext.getRequest().setAttribute("tableDecor", grandTotals);
				 
			
		%> 
		<display:table class="alternateColorInSub"    name="<%=dtoList%>" id="row"  style=" text-align:center" 
			export="true" defaultsort="1" defaultorder="descending" pagesize="50" >
			
				<display:column property="personnelNo" title="Personnel No"
				sortable="true" headerClass="sortable" />
				<display:column property="displayName" title="DisplayName"
				sortable="true" headerClass="sortable" /> 
				<display:column property="emailAddress" title="EmailAddress"
				sortable="true" headerClass="sortable" />
				<display:column property="noshowcount" title="NoShowCount"
				sortable="true" headerClass="sortable" />
	
			<display:setProperty name="export.csv.filename" value="<%=fname1%>" />
			<display:setProperty name="export.excel.filename" value="<%=fname2%>" />
			<display:setProperty name="export.xml.filename" value="<%=fname3%>" />
		</display:table>
<%} else { 
		out.println(" No results found to display");
		
				
}
		%>	</div>
			
			 <%@include file="Footer.jsp"%> 
		</div>
	

</body>
</html>
