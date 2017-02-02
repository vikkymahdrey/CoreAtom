<%@page import="com.agiledge.atom.service.VendorService"%>
<%@page import="com.agiledge.atom.dto.VendorDto"%>
<%@page import="com.agiledge.atom.dto.DriverDto"%>
<%@page import="com.agiledge.atom.dao.DriverDAO"%>
<%@page import="com.agiledge.atom.dao.VehicleDao"%>
<%@page import="com.agiledge.atom.dao.SiteDao"%>
<%@page import="com.agiledge.atom.dto.SiteDto"%>
<%@page import="com.agiledge.atom.dto.VehicleDto"%>
<%@page import="com.agiledge.atom.dao.VehicleTypeDao"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="com.agiledge.atom.dto.VehicleTypeDto"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.nfl.com" prefix="disp" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="css/style.css" rel="stylesheet" type="text/css" />
<link href="css/menu.css" rel="stylesheet" type="text/css" />
<title>Driver Setup</title>
<% request.setAttribute("contextPath", request.getContextPath()); %>
<link rel="stylesheet" type="text/css" href="${contextPath}/css/displaytag.css" />
<script type="text/javascript" src="js/dispx.js"></script>
<script type="text/javascript" src="js/validate.js"></script>
<script type="text/javascript" src="js/dateValidation.js"></script>
<script src="js/JavaScriptUtil.js"></script>
<script type="text/javascript" src="js/jquery-latest.js"></script>
<style type="text/css">
@import "css/jquery.datepick.css";
</style>
<script type="text/javascript" src="js/jquery.datepick.js"></script>
<script type="text/javascript">
	$(document).ready(function() {
		$("#lisenceexpdt").datepick();
	
	});
</script>
<script type="text/javascript">


	function validate() {
		   var currentDateparted =$("input[id=curdate]").val().split(" ");
		var name = $("input[id=name]").val();
		var address1 = $("input[id=address1]").val();
		var address2 = $("input[id=address2]").val();
		var contactNo = $("input[id=contactNo]").val();
		var username = $("input[id=username]").val();
		var vendor = $("input[id=vendor]").val();
		var nameRegx = /^[A-Za-z][A-Za-z0-9 ]*$/;
		var vehicleNo = $("input[id=vehicleNo]").val();
		var lisence = $("input[id=lisence]").val();
		var badge = $("input[id=badge]").val();
		var lisenceexpdt = $("input[id=lisenceexpdt]").val();
		var fileName = $("input[id=photo]").val();
		var input = document.getElementById('photo');
        var file = input.files[0];
        var inputdbc=document.getElementById('dbcer');
        var file1 = inputdbc.files[0];
        var fileName1 = $("input[id=dbcer]").val();
       
     
        var points = document.getElementsByName('dbstatus');
		var point;
		var fileName2="a";
		for (var i = 0; i < points.length; i++) {
			if (points[i].checked) {
				point = points[i].value;
			}
		}
		if(fileName1!=""){
			fileName2=fileName1;
		}
		try {  if (name == "") {
				alert("Name should not be blank !  ");
				return false;
			}
			 else if( nameRegx.test(name)==false) {
				alert("Name is invalid !");
				return false;
			}
			 else if (address1 == "") {
				alert("Address Should not be blank !");
				return false;
			} 
	
			 else 	if (contactNo == "") {
				alert("Contact No  Should not be blank !");
				return false;
			} 
			
			var phoneRegx = /^\+?\d?\d?\d{10}$/;
		
			 if (phoneRegx.test(contactNo )==false) {
				alert("Contact No  is invalid!");
				return false;
			} 
			else if (lisence == "") {
					alert("Please Provide DriverLisence Number");
					return false;
				}
			 else if (lisenceexpdt == "") {
					alert("Please Choose DriverLisence Expiry Date");
					return false;
				}
			/*  else if( lisenceexpdt<currentDateparted  ){
		         	alert("Driver Lisence is  expired");
		         	return false;
		         } */
			 else if (badge == "") {
					alert("Please Provide DriverBadge Number");
					return false;
				}
			
			 else if (username == "") {
				alert("User Name Should not be blank !");
				return false;
			}  
			
			else if(point=='Yes' && fileName1==""){
				alert("Please upload driver background certificate");
				return false;
			}else if (fileName == "") {
		            alert("Plese upload Image File");
		            return false;
		        
		}
		
			 else if(true){
				 if ((fileName.split(".")[1].toUpperCase() == "PNG") ||(fileName.split(".")[1].toUpperCase() == "GIF") ||(fileName.split(".")[1].toUpperCase() == "BMP") ||(fileName.split(".")[1].toUpperCase() == "JPEG") || (fileName.split(".")[1].toUpperCase() == "JPG"))
		          {
		          return true;
		        }else if(file.size>1048576){
        	    	alert("File size should be less than 1MB");
        	    	return false;
        	    }else{  alert("Please upload Image File Only"); return false;}
			 }
				else if(point=='Yes')
				{
			 if(file1.size>1048576){
	    	alert("File size should be less than 1MB");
	    	return false;
		}
			 else if(!((fileName2.split(".")[1].toUpperCase() == "PNG") ||(fileName2.split(".")[1].toUpperCase() == "GIF") ||(fileName2.split(".")[1].toUpperCase() == "BMP") ||(fileName2.split(".")[1].toUpperCase() == "JPEG") || (fileName2.split(".")[1].toUpperCase() == "JPG"))){
	     	    	alert("Plese upload Image File");
		            return false;
	    	    }}
			else {
				return false;
				 
			}
		} catch (e) {
			alert(e);
			return false;
		}
	}
	
	function fillFields(id) {
		 
		
		$("#name").val($("#name-"+id).val());
		$("#address1").val($("#address1-"+id).val());
		$("#username").val($("#username-"+id).val());
		$("#contactNo").val($("#contactNo-"+id).val());
		$("#lisence").val($("#lisence-"+id).val());
		$("#lisenceexpdt").val($("#lisenceexpdt-"+id).val());
		$("#vendor").val($("#vendor-"+id).val());
		$("#remarks").val($("#remarks-"+id).val());
		$("#badge").val($("#badge-"+id).val());
		$("#driverId").val($("#driverId-"+id).val());
		$("form[name='driiveradd']").attr("action","UpdateDriver");
		$("input[name='submitbtn']").val("Update");
	}
	

	function clearFields() {
		 		
		$("#name").val("");
		$("#address1").val("");
		$("#username").val("");
		$("#contactNo").val("");
		$("#lisence").val("");
		$("#lisenceexpdt").val("");
		$("#vendor").val("");
		$("#password").val("");
		$("#remarks").val("");
		$("#driverId").val("");
		$("#badge").val("");
		$("#photo").val("");	
		$("form[name='driiveradd']").attr("action","AddDriver");
		$("input[name='submitbtn']").val("Submit");
	}
	
	function showupload(status)
	{
		
		if(status=='yes'){
		$('#dbcer').show();
		}else{
			$('#dbcer').hide();	
		}
	}
	
</script>


</head>

<body>
	<%
		long empid = 0;
		String employeeId = OtherFunctions.checkUser(session);
		String siteID = request.getParameter("siteId");

		if (employeeId == null || employeeId.equals("null")) {
			String param = request.getServletPath().substring(1) + "___"
					+ request.getQueryString();
			response.sendRedirect("index.jsp?page=" + param);
		} else {
			empid = Long.parseLong(employeeId);
	%><%@include file="Header.jsp"%>
	<%
		}
		ArrayList<DriverDto> driverDtos= new DriverDAO().getAllDriver();
	%>

	<div id="body">
		<div class="content">

			<h3>Add Driver</h3>
			<hr />
			<form name="driiveradd"  method="post" action="AddDriver"  enctype="multipart/form-data"
				onsubmit="return validate()" >

				<table >
					<tr>	
					<td align="right">Choose Site</td>
							<td><select name="siteId" id="siteId" onchange="getLogTime()">
							<%
							String site=(request.getSession().getAttribute("site")==null||request.getSession().getAttribute("site").toString().trim().equals(""))?"" :request.getSession().getAttribute("site").toString().trim();
																if(site==null||site.equals("0"))
									{%>
									<option>Select</option>	
									<%}								
									
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
							</select><input type="hidden" id="curdate"
					value="<%=OtherFunctions.getTodaysDate()%>" /></td>
					</tr>
					<tr>
						<td align="right">Vendor Company</td>
						<td><select name="vendor" id="vendor"  required>
						<%
							ArrayList<VendorDto> vendorDtos = new VendorService().getMasterVendorlist();
						%>
								<option></option>
								
								<%
								if(vendorDtos!=null&&vendorDtos.size()>0)
								{
								for(VendorDto vendorDto:vendorDtos) {%>
								<option  value="<%=vendorDto.getCompanyId()%>" ><%=vendorDto.getCompany()%>  </option>
								<%} 
								
								}%>
						</select></td>
					</tr>
					<tr>
						<td align="right">Driver Name</td>
						<td><input type="text" name="name" id="name" />
						<input type="hidden" name="driverId" id="driverId" required/>
						
						</td>
					</tr>
					<tr>
						<td align="right">Residence Address</td>
						<td><input type="text" name="address1" id="address1" required/></td>
					</tr>
					<tr>
						<td align="right">Contact Number</td>
						<td><input type="text" maxlength="10" name="contactNo" id="contactNo" required/></td>
					</tr>
					<tr>
						<td align="right">Lisence Number</td>
						<td><input type="text" name="lisence" id="lisence" />
			     		 	<input type="file" name="photo" id="photo" size="50"    required/></td> 
			     		 
					</tr>
					
					<tr>
						<td align="right">Lisence ExpiryDate</td>
						<td><input type="text" name="lisenceexpdt" id="lisenceexpdt" required/></td>
					</tr>
			
					<tr>
						<td align="right">Driver Badge</td>
						<td><input type="text" maxlength="10" name="badge" id="badge" required/></td>
					</tr>
					<tr>
						<td align="right">User Name</td>
						<td><input type="text" name="username" id="username" required/></td>
					</tr>
					<tr>
						<td align="right">Password</td>
						<td><input type="password"  name="password" id="password"  required/></td>
					</tr>					
					<tr>
						<td align="right">Remarks</td>
						<td><input type="text"  name="remarks" id="remarks" /></td>
					</tr>	
					<tr>
						<td align="right">Background Check</td>
						<td>&nbsp;&nbsp;&nbsp;<input type="radio" name="dbstatus" id="dbstatus"
							value="Yes" onclick="showupload('yes');"/>&nbsp;Yes <input type="radio" name="dbstatus" id="dbstatus"
							value="No" onclick="showupload('no');" />&nbsp;No &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="file" name="dbcer" id="dbcer" size="50"   /> </td>
					</tr>	
									
					
					<tr>
						<td></td>
						<td><input type="submit" class="formbutton" name="submitbtn"
							value="Submit" /> <input type="button" class="formbutton"
							onclick="javascript:history.go(-1);" value="Back" />
							<input type="button" class="formbutton"
							onclick="clearFields()" value="Clear" />
							</td>
					</tr>
				</table>
			</form>
			 <h3 align="center">Driver Details </h3> 
			 <p align="right">
			 <input type="button" class="formbutton" value="Excel Download" onclick="location.href = 'downloadDriverDetails.jsp'" />&nbsp;&nbsp;&nbsp;&nbsp;
			</p>
					
					
					
					
			<disp:dipxTable id="row" list="<%=driverDtos %>" >
 			
				<disp:dipxColumn  title="Name" sortable="true" property="driverName"  >
				<input type="hidden" id="driverId-${rowCount+1}" value="${row.driverId}"/>
				<input type="hidden" id="address1-${rowCount+1}" value="${row.address1 }"/>
				<input type="hidden" id="name-${rowCount+1}" value="${row.driverName }"/>
				<input type="hidden" id="contactNo-${rowCount+1}" value="${row.contactNo }"/>
				<input type="hidden" id="username-${rowCount+1}" value="${row.userId }"/>
				<input type="hidden" id="vendor-${rowCount+1}" value="${row.vendor }"/>
				<input type="hidden" id="lisence-${rowCount+1}" value="${row.lisence }"/>
     			<input type="hidden" id="badge-${rowCount+1}" value="${row.badge }"/>
				<input type="hidden" id="remarks-${rowCount+1}" value="${row.remarks }"/>
				
				</disp:dipxColumn>
				<disp:dipxColumn  title="Contact No" sortable="true" property="userId"  ></disp:dipxColumn>
				<disp:dipxColumn  title="Lisence Number" sortable="true" property="lisence"  ></disp:dipxColumn>
				<disp:dipxColumn  title="Expiry Date" sortable="true" property="lisenceExpiryDt"  ></disp:dipxColumn>
				<disp:dipxColumn  title="Address" sortable="true" property="address1"  ></disp:dipxColumn>
				 <disp:dipxColumn  title="Badge" sortable="true" property="badge" ></disp:dipxColumn>
				<disp:dipxColumn  title="Remarks" sortable="true" property="remarks" ></disp:dipxColumn>
				 
				<disp:dipxColumn title="Action"   ><a onclick="fillFields('${rowCount+1}')">Edit</a></disp:dipxColumn>
			</disp:dipxTable>
		
 <p align="right">
			 <input type="button" class="formbutton" value="Excel Download" onclick="location.href = 'downloadDriverDetails.jsp'" />&nbsp;&nbsp;&nbsp;&nbsp;
			</p>		 
			<%@include file="Footer.jsp"%>
		</div>
	</div>
</body>

</html>
