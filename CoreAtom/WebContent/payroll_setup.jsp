<%@page import="com.agiledge.atom.dao.SiteDao"%>
<%@page import="com.agiledge.atom.dto.SiteDto"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link href="css/style.css" rel="stylesheet" type="text/css" />
<link href="css/menu.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="js/jquery-latest.js"></script>
<script type="text/javascript" src="js/dateValidation.js"></script>
<style type="text/css">
@import "css/jquery.datepick.css";
</style>
<script type="text/javascript" src="js/jquery.datepick.js"></script>
<script src="js/dateValidation.js"></script>
<script type="text/javascript">
function openWindow(url) {
	window.open(url, 'Ratting',
			'width=400,height=350,left=150,top=200,toolbar=1,status=1,');

}
$(document).ready(function()
        {                                                                        
            $("#fromDate").datepick();
            $("#toDate").datepick();
        });
   function hideProject()
   {
	   var chkBox = document.getElementById("chkBox");
		if (chkBox.checked){
			$("#projectdesc").hide();
		    $("#projbutton").hide();
		    document.getElementById("condition").setAttribute("value","All");
		} else {
			 $("#projectdesc").show();
			 $("#projbutton").show(); 
			 document.getElementById("condition").setAttribute("value","Selected");
		}
	}
   function confirmValidate() {
		var flag = true;
		var siteid=$("select[name=siteId]").val();
		var fromDate = $("input[name=fromDate]").val();
		var toDate = $("input[name=toDate]").val();
		var amount=$("input[name=amount]").val();
		var property=$("select[name=property]").val();
		var projectdesc=$("input[name=projectdesc]").val();
		var chkBox = document.getElementById("chkBox");
		var projectid="0";
		if(!chkBox.checked)
			{
			projectid=$("input[name=project]").val();
			}
		if(!chkBox.checked && projectdesc=="")
			{
			alert("Please Specify Project Option");
			flag = false;
			}
		else if(property == "")
			{
			alert("Please Select Settings For");
			flag = false;
			}
		else if (fromDate == "") {
			alert("Please Specify Effective Date");
			flag = false;
		} else if (toDate == "") {
			alert("Please Specify To Date");
			flag = false;
		}  else if (CompareTwodiffDates(toDate,fromDate)) {
			alert("Effective Date Should Be Before To Date!");
			flag = false;	
		} 
		else if(amount==""){
			alert("Please Specify Amount");
			flag=false;
		}
		var jsonData="";
		var param="?fromDate="+fromDate+"&toDate="+toDate+"&projectid="+projectid+"&site="+siteid;
		try{
		 jsonData = $.ajax({
	          url: "CheckData"+param,
	          dataType:"json",
	          async: false
	          }).responseText;
		}catch (e)
		{
			 alert(" error " + e.message);
		}
		var ans = jQuery.parseJSON(jsonData);
		if(ans.result=="true")
			{
			flag=false;
			alert("Selected Date Range Already Exists,Select Another Dates!");
			}
		return flag;
		
		

	}
</script>
<title>Settings</title>
</head>
<body>
<%
		long empid = 0;
		String employeeId = OtherFunctions.checkUser(session);
		if (employeeId == null || employeeId.equals("null")) {
			String param = request.getServletPath().substring(1) + "___"
					+ request.getQueryString();
			response.sendRedirect("index.jsp?page=" + param);
		} else {
			empid = Long.parseLong(employeeId);
	%>
	<%@include file="Header.jsp"%>
	<%
		}
	%>
	<div id="body">
		<div class="content">
<h3 align="center">Settings</h3>
<div>
<form name="AddSettings" action="AddSettings" method="post" onsubmit="return confirmValidate()" >
<table>
<tr>
<th align="center">Choose Site</th>
<th align="center">Select Project</th>
<th align="center">Settings For</th>
<th align="center">Effective Date</th>
<th align="center">To Date</th>
<th align="center">Amount(Rupees)</th>
</tr>
<tr>
<td align="center"><select name="siteId" id="siteId">
									<%

									String siteID = request.getParameter("siteId");
									String fromDate = request.getParameter("fromDate");
									String toDate = request.getParameter("toDate");
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
<td align="center">All Projects                    <input type="checkbox" id="chkBox" onClick="hideProject()"/><div></div><input readonly type="text" value=""   name="projectdesc"
							id="projectdesc" />
							<input type="button" class="formbutton" id="projbutton" value="..." width="6" onclick="openWindow('getproject.jsp')" />
							 <input type="hidden" id="project" value="" name="project" />
							 <input type="hidden" id="condition"  name="condition" value="Selected"/>
						</td>
<td align="center"><select name="property" id="property">
<option value="">---Select---</option>
<option value="payroll"> Payroll </option>
</select>
</td>
<td align="center"><input name="fromDate"
								id="fromDate" type="text" size="6" readonly="readonly"
								class="{showOtherMonths: true, firstDay: 1, dateFormat: 'yyyy-mm-dd',
	                                                 minDate: new Date(2008, 12 - 1, 25)}"
								value="<%=fromDate!=null&&fromDate.trim().equals("")==false?fromDate:"" %>" /></td>
<td align="center"><input name="toDate" id="toDate" type="text" size="6"
								readonly="readonly"
								class="{showOtherMonths: true, firstDay: 1, dateFormat: 'yyyy-mm-dd',
                                                 minDate: new Date(2008, 12 - 1, 25)}"
								value="<%=toDate!=null&&toDate.trim().equals("")==false?toDate:"" %>" />
</td>
<td align="center"><input id="amount" size="10" name="amount"/></td>
</tr>
</table>
<table>
<td align="center">
<input type="submit" class="formbutton"
			value="Save"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="reset" class="formbutton" value="Reset"/>
			</td>
</table>
</form>
<%@include file='Footer.jsp'%>
		</div>
		</div>
		</div>

</body>
</html>