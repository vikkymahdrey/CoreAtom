<%-- 
    Document   : site_vehicle_list
    Created on : Oct 30, 2012, 3:02:12 PM
    Author     : muhammad
--%>

<%@page import="com.agiledge.atom.dao.SiteDao"%>
<%@page import="java.util.List"%>
<%@page import="com.agiledge.atom.dto.SiteDto"%>
<%@page import="com.agiledge.atom.dao.OtherDao"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="css/style.css" rel="stylesheet" type="text/css" />
<link href="css/menu.css" rel="stylesheet" type="text/css" />

<title>Vehicle List</title>
<script>   
    $(document).ready(function(){
    		 
    	 setActiveMenuItem();
   });
    
    function setActiveMenuItem()
    {
    	var url=window.location.pathname;
    	var filename=url.substring(url.lastIndexOf('/') + 1);
   //  $("li[class=active']").removeAttr("active");
  		 
  		
  		 
  		 
   		
    	 $("a[href='"+ filename + "']").parent().attr("class", "active");
    		 $("a[href='"+ filename + "']").parent().parent().parent('li').attr("class", "active") ;
    		 alert(filename);
    	 
    }
    </script>
<script type="text/javascript">
            function showAllVehicle()
            {
                var siteId=document.getElementById("siteId").value;
                if(document.getElementById("siteId").value!="")
                {  xmlHttp=GetXmlHttpObject()
                    if (xmlHttp==null)
                    {
                        alert ("Browser does not support HTTP Request")
                        return
                    }
                    var url="get_site_vehicle.jsp";
                    url=url+"?siteId="+siteId;	
                                
                                
                    xmlHttp.onreadystatechange=showSiteVehicle
				
                    xmlHttp.open("GET",url,true);		
                    xmlHttp.send(null)    
                }
                else
                {
                    alert("Please Select Site-- ");
                }
            }
           
                
            function GetXmlHttpObject()
            {
                var xmlHttp=null;
                if (window.XMLHttpRequest) 
                {
                    xmlHttp=new XMLHttpRequest();
                }                
                else if (window.ActiveXObject) 
                { 
                    xmlHttp=new ActiveXObject("Microsoft.XMLHTTP");
                }
                return xmlHttp;
            }
        
            function showSiteVehicle() 
            {                                     	
                if (xmlHttp.readyState==4 || xmlHttp.readyState=="complete")
                { 
                    document.getElementById("sietvehicle").innerHTML=xmlHttp.responseText 
                }
            }
            function showAuditLog(relatedId,moduleName){
            	var params = "toolbars=no,menubar=no,location=no,scrollbars=yes,resizable=yes";
            	var size = "height=450,width=900,top=200,left=300," + params;
            	var url="ShowAuditLog.jsp?relatedNodeId="+relatedId+"&moduleName="+moduleName+"&current=VEHICLE";	
                newwindow = window.open(url, 'AuditLog', size);

            	if (window.focus) {
            	newwindow.focus();
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

	<div id="body">
		<div class="content">

			<h3>Vehicles</h3>
			<%
            List<SiteDto> siteDtoList = new SiteDao().getSites();
        %>
			<table>
				<tr>
					<td width="10%">Choose Site</td>
					<td><select name="siteId" id="siteId">
							<%for (SiteDto siteDto : siteDtoList) {%>
							<option value="<%=siteDto.getId()%>"><%=siteDto.getName()%></option>
							<%  }%>
					</select></td>
					<td><input type="button" class="formbutton" value="Show"
						onclick="showAllVehicle()" /></td>
				</tr>

			</table>
			<div id="sietvehicle"></div>
			<%@include file="Footer.jsp"%>
		</div>
	</div>
</body>




</html>
