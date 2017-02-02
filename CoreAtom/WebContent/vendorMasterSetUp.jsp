<%-- 
    Document   : newjsp
    Created on : Oct 22, 2012, 1:00:11 PM
    Author     : muhammad
--%>

<%@page import="com.sun.xml.internal.bind.v2.schemagen.xmlschema.Import"%>
<%@page import="com.agiledge.atom.dto.VendorDto"%>
<%@page import="com.agiledge.atom.service.VendorService"%>
<%@page import="com.agiledge.atom.dto.APLDto"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.agiledge.atom.service.APLService"%>
<%@page import="com.agiledge.atom.dao.OtherDao"%>
<%@page import="com.agiledge.atom.constants.AuditLogConstants"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@ taglib uri="http://www.nfl.com" prefix="disp" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="css/style.css" rel="stylesheet" type="text/css" />
<link href="css/menu.css" rel="stylesheet" type="text/css" />
<link href="css/displaytag.css" rel="stylesheet" type="text/css" />
 <link rel="stylesheet" href="http://code.jquery.com/ui/1.10.4/themes/smoothness/jquery-ui.css">
<script type="text/javascript" src="js/jquery-latest.js"></script> 
<script src="//ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js"></script>
<script src="http://code.jquery.com/ui/1.10.4/jquery-ui.js"></script>

<script type="text/javascript" src="js/dateValidation.js"></script>

<script type="text/javascript">
          $(document).ready(function(){
        	 $("form[name=vendorForm]").hide();
        	 $("form[name=editVendor]").hide();
        	 
        	$("#showAddVendorDiv_a").click(showAddVendor);
        	$("#closeAddVendor_img").click(closeAddVendor);
        	 $(".editButton").click(showEditArea);
        	 $("#changePassword").click(showChangePassword);
        	 $(".deleteButton").click(deleteVendor);
        	 $("#closeVendorContract_img").click(closeVendorContract);
        	  
        	 try{
        		 $("form[name=vendorForm]").draggable();
        	 }catch (e) {
				;
			}
        	
          });
          
          function deleteVendor()
          {
        	  var imageId= $(this).attr("id");
      		 var id= imageId.split("-")[1];
     		 $("form[name=deleteVendorFrom]").attr("action","DeleteVendorMaster");
     		 $("input[name=deleteid]").val(id);
     		 if(confirm("Do you really want to delete ?"))
     			 { 
     				$("form[name=deleteVendorFrom]").submit();
     				
     			 }
        	  
        	  
          }
          function showChangePassword()
          {
        	  if( $("#changePassword").is(":checked"))
        		  {
        		  $("#row-password").show();
        		  
        		  }
        	  else {
        		  $("#row-password").hide();
        		  $("#password").val("");
        	  }
        	  
          }
          
          function showAddVendor()
          {
        	   
        	//  $("form[name=editVendor]").attr("action","AddVendorMaster");
        	  $("#windowTitle").text("New");
        	  $("#showAddVendorDiv").hide();
        	  $("form[name=vendorForm]").show();
        	  $("#submitbtn").val("Add");
        	  $("#row-changePassword").hide();
        	  $("#row-password").show();
          }
          
          function clearVendorForm()
          {
        	  $("#id").val("");
        	  $("#name").val("");
        	  $("#address").val("");
        	  $("#contactNumber").val("");
        	  $("#email").val("");
        	  $("#loginId").val("");
        	  $("#password").val("");
        	  $("#changePassword").removeAttr("checked");
        	  
        	  
          }
          function closeAddVendor()
          {
        	  clearVendorForm();
        	  $("#showAddVendorDiv").show();
        	  $("form[name=vendorForm]").hide();
        	  $("form[name=vendorForm]").attr("action","#");
          }
          
         
          function closeVendorContract()
          {
        	  clearVendorContractForm();
        	  
        	  $("form[name=vendorContractForm]").hide();
        	 // $("form[name=vendorContractForm]").attr("action","#");
        	  
          }
			function clearVendorContractForm()
			{
				$("#contractDescription").val("");
				$("#contractRate").val("");
				
			}
          function showEditArea()
          {
        	 try{ 
        		 
        		 var imageId= $(this).attr("id");
        		 var id= imageId.split("-")[1];
        		 
        		 
        	  $("form[name=vendorForm]").show();
        	  $("form[name=vendorForm]").attr("action","UpdateVendorMaster");
        	  $("#windowTitle").text("Edit");
        	  $("#submitbtn").val("Update");
        	  
        	  $("input[name=Id]").val($(this).parent().parent().children().children(".Id").val());
        	   
        
        	  $("#id").val(  $("#id-" + id).val());
        	  $("#name").val($("#name-" + id).val());
        	  $("#address").val($("#address-" + id).val( ));
        	  $("#contactNumber").val( $("#contactNumber-" + id).val( ));
        	  $("#email").val( $("#email-" + id).val( ));
        	    $("#id").val(id);
        	 
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
        		  
        	if($("#name").val().trim()=="")
        	{
        		alert("Name should not be blank");
        		flag=false;
        	}else if($("#address").val().trim()=="")
        	{
        		alert("Address should not be blank");
        		flag=false;
        	}
        	else if($("#contactNumber").val().trim()=="")
    		{
    		alert("Contact Number should not be blank");
    		flag=false;
    		}
        	else if($("#email").val().trim()=="")
    		{
        		alert("Email address should not be blank");
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
  			var url="ShowAuditLog.jsp?relatedNodeId="+relatedId+"&moduleName="+moduleName;	
  		    newwindow = window.open(url, 'AuditLog', size);

  			if (window.focus) {
  			newwindow.focus();
  		}
  		}
  		
          </script>
<title>Vendor Company Setup</title>

</head>
<body>
	<div class="content">
		<div class="content_resize">
			<%
          long empid=0;
        String employeeId = OtherFunctions.checkUser(session);
            empid = Long.parseLong(employeeId);
            %>
			<%@include file="Header.jsp"%>
			<br />

			<%
VendorService service=new VendorService();
      ArrayList<VendorDto> dtoList=service.getMasterVendorlist();

%>
			<h3>Vendor Setup</h3>
			<hr />
			<table>
				<tr>
					<td style="width: 30%; vertical-align: top;">
  
							<%         
				pageContext.setAttribute("vendorModule", AuditLogConstants.VENDOR_MODULE);
%>
						 
						
												<div>
	  	 	 
								 		<disp:dipxTable styleClass="displaytag"     id="row" list="<%=dtoList %>" style="width:100%;" >
												<disp:dipxColumn   property="company" title="Name" >
													 
								 					   	<input type="hidden" class="name" value="${row.company}" id="name-${row.companyId}" />
								  						<input type="hidden" class="id" value="${row.companyId}" id="id-${row.companyId}" />
												</disp:dipxColumn>
												<disp:dipxColumn  property="companyAddress" title="Address" />
														<input type="hidden" class="address" value="${row.companyAddress}" id="address-${row.companyId}" />
												<disp:dipxColumn  property="email" title="Email" />
														<input type="hidden" class="email" value="${row.email}" id="email-${row.companyId}" />
												<disp:dipxColumn  property="companycontact" title="Contact No" />
														<input type="hidden" class="contactNumber" value="${row.companycontact}" id="contactNumber-${row.companyId}" />
												<disp:dipxColumn title="">
													<table>
														<tr>
															<td style="border-bottom: 0px solid #cE5; padding: 0px;">
																<img src="images/edit.png" class="editButton"
																id="editImg-${row.companyId }" title="Edit" />
															</td>
															<td style="border-bottom: 0px solid #cE5; padding: 0px;">
																<img src="images/delete.png" class="deleteButton"
																id="deleteImg-${row.companyId }" title="Delete" />
	
															</td>
															 
															<td align="center"><input type="button"
																class="formbutton"
																onclick="showAuditLog(${row.companyId},'${vendorModule }');"
															value="Audit Log" />
															</td>
	
														</tr>
													</table>
									
													
												 </disp:dipxColumn>
												 
												 
										</disp:dipxTable>
			
											
									</div>
						
						
						


					</td>
					<td style="width: 30%; vertical-align: top;">
						<div id="showAddVendorDiv" style="padding-top: 3px;">
							<input type="button" class="formbutton" id="showAddVendorDiv_a"
								title="sdfsdf" value="Add Vendor Company" />

						</div>
						<form name="deleteVendorFrom" action="DeleteVendorMaster" method="POST">
							<input type="hidden" name="deleteid" />
						</form>
 
		
						<form name="vendorForm"  style="position: fixed;"  action="AddVendorMaster" method="POST"
							onsubmit="return validateArea();">
							<table style="border-style: outset; width: 20%; background-color: #ddd;">
								<thead>
									<tr>
										<th colspan="2"><label id="windowTitle">Add
												Vendor</label>
											<div style="float: right;" id="closeAddVendor">
												<img id="closeAddVendor_img" style="float: right;"
													id="closeAddVendor" src="images/close.png" title="Close" />
											</div></th>
									</tr>
								</thead>
								<tr>
									<td align="center">Name</td>
									<td align="center"><input type="text" name="name"
										id="name" /> <input type="hidden" id="id" name="id" /></td>
								</tr>
								<tr>
									<td align="center">Address</td>
									<td align="center"><input type="text" name="address"
										id="address" /></td>
								</tr>
								<tr>
									<td align="center">Contact Number</td>
									<td align="center"><input type="text" name="contactNumber"
										id="contactNumber" /></td>
								</tr>
								<tr>
									<td align="center">Email</td>
									<td align="center"><input type="text" name="email"
										id="email" /></td>
								</tr>
								<tr>
									<td align="center">&nbsp;</td>
									<td align="center"><input type="submit" class="formbutton"
										value="Add" name="submitbtn" id="submitbtn" /></td>
								</tr>
								<tr>
									<td align="center"></td>
								</tr>
							</table>
						</form>
					</td>
				</tr>
			</table>

		</div>
	</div>
</body>
</html>
