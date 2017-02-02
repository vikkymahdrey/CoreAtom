<%-- 
    Document   : routeSample
    Created on : Oct 25, 2012, 1:05:50 PM
    Author     : muhammad
--%>

<%@page import="com.agiledge.atom.dto.LogTimeDto"%>
<%@page import="com.agiledge.atom.dao.LogTimeDao"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.agiledge.atom.dto.SiteDto"%>
<%@page import="com.agiledge.atom.dao.SiteDao"%>
<%@page import="com.agiledge.atom.dao.OtherDao"%>
<%@page import="java.util.List"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Routing</title>
<script type="text/javascript" src="js/jquery-latest.js"></script>
<style type="text/css">
@import "css/jquery.datepick.css";
</style>
<script type="text/javascript" src="js/jquery.datepick.js"></script>
<script type="text/javascript">

function resetFields()
{
	$("input[type=hidden]").val("");
}
        function showPopup(url) {

        var params="toolbars=no,menubar=no,location=no,scrollbars=yes,resizable=yes";
        size="height=450,width=520,top=200,left=300,"+params;
        if(url=="LandMarkSearch.jsp")
            {
                size="height=450,width=600,top=200,left=300," + params;
            }
        else if(url=="SupervisorSearch1.jsp" || url=="SupervisorSearch2.jsp"  )
               {
                   size="height=450,width=700,top=200,left=300,"+params;
               }
        else if(url=="termsAndConditions.html")
            {
                   size="height=450,width=520,top=200,left=300,"+params;
            }

        newwindow=window.open(url,'name',size);
        

         if (window.focus) {newwindow.focus();}
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
		long empid = 0;
		String employeeId = OtherFunctions.checkUser(session);		
			empid = Long.parseLong(employeeId);
	%>
	<%@include file="Header.jsp"%>
	<%		
		OtherDao ob = null;
		ob = OtherDao.getInstance();
		
		 List<SiteDto> siteDtoList = new SiteDao().getSites();       
	%>



	<hr />
	<div id="body">
		<div class="content">
			<div>
				<h3>Subscription Report</h3>
				<form name="form1" action="display_employeeSubscriptionReport.jsp"
					method="POST" onsubmit="return validate()">
					<table style="margin-left: 24%; width: 25%;">
						<tr>
							<td>Choose Site</td>
							<td><select name="siteId" id="siteId">
									<%for (SiteDto siteDto : siteDtoList) {
									String site=(request.getSession().getAttribute("site")==null||request.getSession().getAttribute("site").toString().trim().equals(""))?"" :request.getSession().getAttribute("site").toString().trim();
									String siteSelect="";
									if(site.equals(siteDto.getId()))
									{
										siteSelect="selected";
									}
											
								%>

									<option <%=siteSelect %> value="<%=siteDto.getId()%>"><%=siteDto.getName()%></option>
									<%  }%>
							</select></td>

						</tr>
						<tr>
							<td align="right">From</td>
							<td align="left" colspan="2"><input name="fromDate"
								id="fromDate" type="text" size="6"
								class="{showOtherMonths: true, firstDay: 1, dateFormat: 'yyyy-mm-dd',
	                                                 minDate: new Date(2008, 12 - 1, 25)}" />



								To<input name="toDate" id="toDate" type="text" size="6"
								class="{showOtherMonths: true, firstDay: 1, dateFormat: 'yyyy-mm-dd',
                                                 minDate: new Date(2008, 12 - 1, 25)}" />


							</td>




						</tr>


						<tr>
							<td align="right">Supervisor</td>
							<td><input type="hidden" name="supervisorID1"
								id="supervisorID1" /> <input type="text" readonly
								name="supervisorName1" id="supervisorName1"
								onclick="showPopup('SupervisorSearch1.jsp')" /> <label
								for="supervisorID1" class="requiredLabel"></label> <input
								class="formbutton" type="button" value="..."
								onclick="showPopup('SupervisorSearch1.jsp')" /></td>

						</tr>


						<tr>
							<td align="right">SPOC</td>
							<td><input type="hidden" name="supervisorID2"
								id="supervisorID2" /> <input type="text" readonly
								name="supervisorName2" id="supervisorName2"
								onclick="showPopup('SupervisorSearch2.jsp')" /> <label
								for="supervisorID2" class="requiredLabel"></label> <input
								class="formbutton" type="button" value="..."
								onclick="showPopup('SupervisorSearch2.jsp')" /></td>

						</tr>

						<tr>
							<td>&nbsp;</td>
							<td><input type="submit" class="formbutton"
								value="Generate Report" /> <input type="reset"
								class="formbutton" value="Reset" onclick="resetFields();" /></td>
						</tr>
					</table>
				</form>
			</div>
			<%@include file="Footer.jsp"%>

		</div>
	</div>
</body>
</html>
