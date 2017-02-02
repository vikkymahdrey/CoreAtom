<%@page import="com.agiledge.atom.constants.SettingsConstant"%>
<%@page import="java.util.Date"%>
<%@page import="org.displaytag.decorator.TotalTableDecorator"%>
<%@page import="com.itextpdf.text.log.SysoLogger"%>
<%@page import="java.util.List"%>
<%@page import="com.agiledge.atom.spring.dao.DelegateReportDAOImpl"%>
<%@page import="com.agiledge.atom.spring.controller.DelegateController"%>

<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
<title>Employee Subscription Report</title>
<script type="text/javascript" src="js/jquery-latest.js"></script>
<style type="text/css">
@import "css/jquery.datepick.css";
</style>
<script type="text/javascript" src="js/jquery.datepick.js"></script>

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
	

				<%String fname1=("DelegationReport :").concat(new Date().toString()).concat(".csv");
				  String fname2=("DelegationReport :").concat(new Date().toString()).concat(".xls");
				  String fname3=("DelegationReport :").concat(new Date().toString()).concat(".xml");
					String fromDate = request.getParameter("fromDate");
					String toDate = request.getParameter("toDate");
					String siteID = request.getParameter("siteId");
					
					long empid = 0;
					String employeeId = OtherFunctions.checkUser(session);
						empid = Long.parseLong(employeeId);
					
				  %>
				  	<%@include file="../../Header.jsp"%>
	<div id="body">
		<div class="content">
				<div>
				  	<form name="form1" method="post" action="display_delegationReport2.do"
					 onsubmit="return validate()">
					<table>
						<tr>							
							<td align="right">From</td>
							<td align="left" colspan="2"><input name="fromDate"
								id="fromDate" type="text" size="6" readonly="readonly"
								class="{showOtherMonths: true, firstDay: 1, dateFormat: 'yyyy-mm-dd',
	                                                 minDate: new Date(2008, 12 - 1, 25)}"
								value="<%=fromDate!=null&&fromDate.trim().equals("")==false?fromDate:"" %>" />

							</td>
							<td align="right">To</td>
							<td align="left"><input name="toDate" id="toDate"
								type="text" size="6" readonly="readonly"
								class="{showOtherMonths: true, firstDay: 1, dateFormat: 'yyyy-mm-dd',
                                                 minDate: new Date(2008, 12 - 1, 25)}"
								value="<%=toDate!=null&&toDate.trim().equals("")==false?toDate:"" %>" />


							</td>					

						

							<td>&nbsp;</td>
							<td><input type="submit" class="formbutton"
								value="Generate DelegateReport" />
						</tr>
					</table>
				</form>
				</div>
				<br />
			<h2 align="center">Employee Subscription Report</h2>
			<hr />
				<display:table requestURI="/display_delegationReport2.do"  class="alternateColorInSub" name="empDelegateList"
				id="row" export="true" defaultsort="1" defaultorder="descending"
				pagesize="50">

				<display:column property="id" title="ID" 
					sortable="true" headerClass="sortable" />
				<display:column property="displayname" title="displayname" 
					sortable="true" headerClass="sortable" />
				<display:column property="personnelno" title="personnelno" sortable="true"
					headerClass="sortable" />
				<display:column property="delegatedEmployeeId" title="delegatedEmployeeId" sortable="true"
					headerClass="sortable" />
				<display:column property="delegate_empID" title="delegate_empID" sortable="true"
					headerClass="sortable" />
				<display:column property="delegatedEmpDisplayname" title="delegatedEmpDisplayname"
					sortable="true" headerClass="sortable" />
				<display:column property="delegatedPersonnelno" title="delegatedPersonnelno" sortable="true"
					headerClass="sortable" />
				<display:column property="from_date" title="from_date" sortable="true"
					headerClass="sortable" />
				<display:column property="to_date" title="to_date" sortable="true"
					headerClass="sortable" />	 
				<display:setProperty name="export.csv.filename" value="<%=fname1%>" />
				<display:setProperty name="export.excel.filename"
					value="<%=fname2%>" />
				<display:setProperty name="export.xml.filename" value="<%=fname3%>" />


			</display:table>
			
	<%@include file="../../Footer.jsp"%>
						
             			
		</div>
	</div>

	</body>
</html>
