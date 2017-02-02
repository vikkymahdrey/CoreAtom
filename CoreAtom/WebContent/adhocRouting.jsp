<%-- 
    Document   : routeSample
    Created on : Oct 25, 2012, 1:05:50 PM
    Author     : muhammad
--%>


<%@page import="com.agiledge.atom.dto.SiteDto"%>
<%@page import="com.agiledge.atom.dao.SiteDao"%>
<%@page import="java.util.ArrayList"%>

<%@page import="java.util.List"%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>Adhoc Routing</title>

<script type="text/javascript" src="js/jquery-latest.js"></script>
<script type="text/javascript" src="js/jquery.validate.js"></script>
<script type="text/javascript" src="js/jquery.datepick.js"></script>
<style type="text/css">
@import "css/jquery.datepick.css";
</style>

<script type="text/javascript">        
            $(document).ready(function()
            {                                                                        
                $("#date").datepick();  
                getLogTime();       
            });   
            
            // submit routing 
            function submitForm(data) {
            	var submitOkey=false;
            	var msg="";
            	 try{ 
            	if(data.result==false) {
            		alert(" Error Occured ");
            	} else {
            		alert(data.saved + " " + data.routed);
            		if(data.saved=="true") {
            		 
            			msg=msg +" Total saved Trips :" + data.savedNo;
            			
            		}
            		if(data.routed=="true") {
            			msg=msg +" Total Unsaved Trips :" + data.routedNo;
            			
            			alert(msg+" routed ");
            			if(confirm(""+msg +"\n Do you want to overwrite unsaved trips" )) {
            				submitOkey=true;
            				document.getElementById("adhocRoutForm").submit();
            				
            			}
            		}
            		if(data.saved=="false" && data.routed=="false") {
            			 
            			submitOkey=true;
            			document.getElementById("adhocRoutForm").submit();
            		 
            		}
            		if(data.saved=="false" && data.routed=="false") {
            			 alert(msg);
            			submitOkey=true;
            			document.getElementById("adhocRoutForm").submit();
            		}
            		 
            	}
            	 }catch(e) {alert(e);}
            	return submitOkey;
            	 //
            }
            //AdhocTripExists
            function validateSubmit() {
            	 
            	var date = $("#date").val();
            	if(date!="") {
            		ajaxPost("AdhocTripExists.do?tripDate=" + date,"",submitForm);
            	}else {
            	 	alert(" Invalid date ");
            	}
            	return false;
            }
            function ajaxPost( urlParam, lookup, retFunction) {
		//alert(urlParam   );
		$.ajax(
				{
					type:"GET",
					url: urlParam,
					data:lookup,
					async: false,
					 
					success: function (data) {
					 	 
						 retFunction( data);
					},
				 error:function (xhr, ajaxOptions, thrownError){
					 
			            alert("in error" + xhr);
			            alert(thrownError);
			        }
					
				}		
			);
	}
   
            
        </script>
<script type="text/javascript">
            function validate()
            {
            	//var siteId=document.getElementById("siteId").value;
                var date=document.getElementById("date").value;   
                if(date.length<1)
                {
                    alert("Choose Date");                  
                    return false;
                }
            
            
        </script>


</head>
<body>
	<%@include file="Header.jsp"%>
	<div id="body">
		<div class="content">
			<%
				List<SiteDto> siteDtoList = new SiteDao().getSites();
			%>
			<%
				long empid = 0;
				String employeeId = OtherFunctions.checkUser(session);
			%>
			<h3>Automatic Routing</h3>
			<hr />
			<form id="adhocRoutForm" name="form1" action="AdhocRouting" method="post" onsubmit="return validateSubmit()">
				<table>
					<tr>
						<td align="right">Choose Site</td>
						<td><select name="siteId" id="siteId">
								<%
									for (SiteDto siteDto : siteDtoList) {
							
								%>

								<option value="<%=siteDto.getId()%>"><%=siteDto.getName()%></option>
								<%
									}
								%>
						</select></td>

						

						<td align="right">Date</td>
						<td><input name="date" id="date" type="text" size="6"
							class="{showOtherMonths: true, firstDay: 1, dateFormat: 'yyyy-mm-dd',
                                                 minDate: new Date(2008, 12 - 1, 25)}" /></td>

						

						<td></td>
						<td><input type="submit" class="formbutton" value="Allocate"
							/></td>
					</tr>

				
				</table>
			</form>
			<%@include file="Footer.jsp"%>
		</div>
	</div>
</body>
</html>
