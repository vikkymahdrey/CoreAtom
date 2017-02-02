<%-- 
    Document   : newjsp
    Created on : Oct 22, 2012, 1:00:11 PM
    Author     : muhammad
--%>

<%@page import="com.agiledge.atom.dto.BranchDto"%>
<%@page import="com.agiledge.atom.dao.BranchDao"%>
<%@page import="com.agiledge.atom.dto.APLDto"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.agiledge.atom.service.APLService"%>
<%@page import="com.agiledge.atom.dao.OtherDao"%>
<%@page import="com.agiledge.atom.constants.AuditLogConstants"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="css/style.css" rel="stylesheet" type="text/css" />
<link href="css/menu.css" rel="stylesheet" type="text/css" />
 <link rel="stylesheet" href="http://code.jquery.com/ui/1.10.4/themes/smoothness/jquery-ui.css">
<script type="text/javascript" src="js/jquery-latest.js"></script> 
<script src="http://code.jquery.com/ui/1.10.4/jquery-ui.js"></script>

<script type="text/javascript" src="js/dateValidation.js"></script>

<script type="text/javascript">
          $(document).ready(function(){
        	 $("form[name=area]").hide();
        	 $("form[name=editArea]").hide();
        	$("#showAddAreaDiv_a").click(showAddArea);
        	$("#closeAddArea_img").click(closeAddArea);
        	 $(".editButton").click(showEditArea);
        	 $("form[name=area]").draggable();
          });
          
          function showAddArea()
          {
        	  $("#area").val("");
        	  $("form[name=editArea]").attr("action","AddArea");
        	  $("#windowTitle").text("Add Area");
        	  $("#showAddAreaDiv").hide();
        	  $("form[name=area]").show();
        	  $("#submitbtn").val("Add");
          }
          function closeAddArea()
          {
        	  $("#showAddAreaDiv").show();
        	  $("form[name=area]").hide();
        	  $("form[name=area]").attr("action","#");
          }

          function showEditArea()
          {
        	 try{ 
        	  $("form[name=area]").show();
        	  $("form[name=area]").attr("action","UpdateArea");
        	  $("#windowTitle").text("Edit Area");
        	  $("#submitbtn").val("Update");
        	  
        	  $("input[name=areaId]").val($(this).parent().parent().children().children(".areaId").val());
        	  $("#area").val($(this).parent().parent().children().children(".area").val());
        	 
        	 }catch(e)
        	 {
        		 alert(e);
        	 }
          }
          
          //-----validation
          
          function validateArea()
          {
        	  var flag=true;
        	  try{
        	if($("input[name=area]").val().trim()=="")
        	{
        		alert("Area should not be blank")
        		flag=false;
        	}
        	}catch (e) {
				
				alert(e.message);
				flag=false;
			}
        	return flag;   
          }
          function showAuditLog(relatedId,moduleName){
      		var params = "toolbars=no,menubar=no,location=no,scrollbars=yes,resizable=yes";
      		var size = "height=450,width=900,top=200,left=300," + params;
      		var url="ShowAuditLog.jsp?relatedNodeId="+relatedId+"&moduleName="+moduleName+"&current=AREA";	
      	    newwindow = window.open(url, 'AuditLog', size);

      		if (window.focus) {
      			newwindow.focus();
      		}
      	}
          function submitForm()
          {
        	 document.getElementById("branchForm").submit(); 
          }
          </script>
<title>Area</title>

</head>
<body>
	<%@include file="Header.jsp"%>
	<div class="content">
		<div class="content_resize">
			<%
				long empid = 0;
				String employeeId = OtherFunctions.checkUser(session);
			%>
			<br />

			<%
				String location = request.getParameter("branchId");
				ArrayList<BranchDto> branchDtos = new BranchDao().getLocations();
			%>
			<form action="areaShuttle.jsp" name="branchForm" id="branchForm">
				<table>
					<tr>
						<td>Location</td>
						<td><select name="branchId" onchange="submitForm()">
								<option>Select</option>
								<%
									for (BranchDto dto : branchDtos) {
										if (dto.getId().equals(location)) {
								%>
								<option value="<%=dto.getId()%>" selected="selected"><%=dto.getLocation()%></option>
								<%
									} else {
								%>
								<option value="<%=dto.getId()%>"><%=dto.getLocation()%></option>
								<%
									}
									}
								%>
						</select></td>
					</tr>
				</table>
			</form>
			<%
				if (location != null) {
					APLService APLServiceObj = new APLService();
					ArrayList<APLDto> APLDtoList = APLServiceObj.getAreas(location);
			%>
			<h3>Area List</h3>
			<hr />
			<p align="center">
				<a href="downloadshuttleapl.jsp?location=<%=location%>">Download All APL</a>
			</p>
			<table>
				<tr>
					<td style="width: 70%; vertical-align: top;">



						<table>

							<thead>

								<tr>
									<th align="center">Id</th>
									<th align="center">Area</th>
									<th width="1%"></th>
									<th align="center">Audit Log</th>
								</tr>
							</thead>
							<%
								for (APLDto aplDto : APLDtoList) {
							%>
							<tr>
								<td align="center"><%=aplDto.getAreaID()%></td>
								<td align="center"><a
									href="placeShuttle.jsp?areaId=<%=aplDto.getAreaID()%>"><%=aplDto.getArea()%></a>
									<input type="hidden" value="<%=aplDto.getAreaID()%>"
									class="areaId" /> <input type="hidden"
									value="<%=aplDto.getArea()%>" class="area" /></td>
								<td><img src="images/edit.png" class="editButton"
									title="Edit" /></td>
								<td align="center"><input type="button" align="middle"
									class="formbutton"
									onclick="showAuditLog(<%=aplDto.getAreaID()%>,'<%=AuditLogConstants.APL_MODULE%>');"
									value="Audit Log" /></td>
							</tr>
							<%
								}
							%>
						</table>


					</td>
					<td style="width: 30%; vertical-align: top;">											
					</td>
				</tr>
			</table>
			<%
				}
			%>
		</div>
	</div>
</body>
</html>
