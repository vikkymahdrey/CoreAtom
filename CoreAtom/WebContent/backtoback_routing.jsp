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
<%@page import="java.util.List"%>
<%@page import="com.agiledge.atom.dao.OtherDao"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>Back To Back Routing</title>

<script type="text/javascript" src="js/jquery-latest.js"></script>
<script type="text/javascript" src="js/jquery.validate.js"></script>
<script type="text/javascript" src="js/jquery.datepick.js"></script>
<style type="text/css">
@import "css/jquery.datepick.css";
</style>

<script type="text/javascript">        
            $(document).ready(function()
            {                                                                        
                $("#for_date").datepick();                                                            
            });     
        </script>
<script type="text/javascript">
            function validate()
            {
            	var siteId=document.getElementById("siteId").value;
                var date=document.getElementById("for_date").value;   
                var time=document.getElementById("logTime").value;
                var log=document.getElementById("log").value;                
                if(date.length<1)
                {
                    alert("Choose Date");                  
                    //return false;
                }
                else if(time.length<1)
                {
                    alert(" choose Shift time");                    
                    //return false;
                }
                else
                {                
                	
                	var url="BackToBackRouting?check=c&siteId="+siteId+"&for_date="+date+"&log="+log+"&logTime="+time+"";                                    
                   xmlHttp=GetXmlHttpObject();
                    if (xmlHttp==null)
                    {
                        alert("Browser does not support HTTP Request");
                        return
                    }                    
                    xmlHttp.onreadystatechange=submitForm;
                    xmlHttp.open("GET",url,true);
                    xmlHttp.send(null);
                	                                           
                }
               
            }
            function submitForm()
            {
            try{	
           if (xmlHttp.readyState==4 || xmlHttp.readyState=="complete")
                 { 
        	   var returnText=xmlHttp.responseText;        	   
        	   if(returnText=="true")
        		   {
        		   if(confirm("Trips on selected day/shift already exists, OK to modify?"))
        		   {document.getElementById("isOverwrite").value="yes";
        		   }
        		   else
        			   {
        			   document.getElementById("isOverwrite").value="no";
        			   }
        		   }
                 document.form1.submit();
                 }
           }catch(e)
           {
        	 alert(e);  
           }
            }    
            function getLogTime()
            {                    
                var logtype=document.getElementById("log").value;
                if(logtype=="All")
                	{
                	
                	var tripTimeId=document.getElementById("logTimeListTd");
                	//alert(tripTimeId);
                	tripTimeId.innerHTML='<select name="logTime" id="logTime"> <option value="ALL" >ALL</option></select>';
                	return;
                	}
                else
                	{
                var url="GetLogTime?logtype="+logtype;                                    
                xmlHttp=GetXmlHttpObject()
                if (xmlHttp==null)
                {
                    alert ("Browser does not support HTTP Request")
                    return
                }                    
                xmlHttp.onreadystatechange=setLogTime	
                xmlHttp.open("GET",url,true)                
                xmlHttp.send(null)
                
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
        
            function setLogTime() 
            {                      
                if (xmlHttp.readyState==4 || xmlHttp.readyState=="complete")
                { 
                    var returnText=xmlHttp.responseText;
                    var logTimeListTd=document.getElementById("logTimeListTd");
                    logTimeListTd.innerHTML='<select  name="logTime" id="logTime"><Option value="All">All</Option>'+returnText+'</select>';                                             
                }
            }
            
        </script>


</head>
<body>
	<div id="body">
		<div class="content">
			<%
            List<SiteDto> siteDtoList = new SiteDao().getSites();            
        %>
			<%
            long empid = 0;
            String employeeId = OtherFunctions.checkUser(session);
            if (employeeId == null||employeeId.equals("null") ) {
                String param = request.getServletPath().substring(1) + "___"+ request.getQueryString(); 	response.sendRedirect("index.jsp?page=" + param);
            } else {
                empid = Long.parseLong(employeeId);
                %>
			<%@include file="Header.jsp"%>
			<%
            }
            OtherDao ob = null;
            ob = OtherDao.getInstance();
        %>

			<h3>Automatic Routing</h3>
			<hr />
			<form name="form1" action="BackToBackRouting">
				<table>
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
						<td>Date</td>
						<td><input name="for_date" id="for_date" type="text" size="6"
							class="{showOtherMonths: true, firstDay: 1, dateFormat: 'yyyy-mm-dd',
                                                 minDate: new Date(2008, 12 - 1, 25)}" /></td>
					</tr>
					<tr>
						<td>Log In/Out</td>
						<td><select name="log" id="log" onchange="getLogTime()">
								<option value="All">IN & Out</option>
								<option value="IN">IN</option>
								<option value="OUT">OUT</option>
						</select></td>
					</tr>
					<tr>
						<td>Time</td>
						<td id="logTimeListTd"><select name="logTime" id="logTime">
								<option value="All">All</option>
						</select></td>
					</tr>
					<tr>
						<td>&nbsp;<input type="hidden" name="isOverwrite"
							id="isOverwrite" /></td>
						<td><input type="button" class="formbutton" value="Allocate"
							onclick="return validate()" /></td>
					</tr>
				</table>
			</form>
			<%@include file="Footer.jsp"%>
		</div>
	</div>
</body>
</html>
