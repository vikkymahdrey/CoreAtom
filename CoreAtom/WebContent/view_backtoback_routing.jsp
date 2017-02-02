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
            $(document).ready(function()
            {                                                                        
                $("#tripDate").datepick();                                                            
            });     
        </script>
<script type="text/javascript">
            function validate()
            {
                var mod=document.getElementById("tripMode").value;   
                var time=document.getElementById("tripTime").value;  
                var date=document.getElementById("tripDate").value;  
                if(date.length<1)
                {
                    alert("Choose Date");
                  //  date.focus();
                    return false;
                        
                }
                else if(mod=="ALL"&&time!="ALL")
                {
                    alert("Cannot choose time for both Shift");
                    //time.options[0].selected=true;   
                    return false;
                }
                else
                {
                    return true;                            
                }
               
            }
        </script>
<script language="javascript">
            function getTripTime()
            {                    
                var logtype=document.getElementById("tripMode").value;
                if(logtype=="ALL")
                	{
                	var tripTimeId=document.getElementById("tripTimeId");
                	tripTimeId.innerHTML='<select name="tripTime" id="tripTime"> <option value="ALL" >ALL</option></select>';
                	return;
                	}
                
                var url="GetLogTime?logtype="+logtype;                                    
                xmlHttp=GetXmlHttpObject()
                if (xmlHttp==null)
                {
                    alert ("Browser does not support HTTP Request");
                    return
                }                    
                xmlHttp.onreadystatechange=setLogTime	
                xmlHttp.open("GET",url,true)                
                xmlHttp.send(null)
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
                    var tripTimeId=document.getElementById("tripTimeId");
                    tripTimeId.innerHTML='<select name="tripTime" id="tripTime"> <option value="ALL" >ALL</option>'+returnText+'</select>';                                             
                }
            }
            
        </script>

</head>
<body>
	<%
            List<SiteDto> siteDtoList = new SiteDao().getSites();
            //ArrayList<LogTimeDto> logtimeDtoList = new LogTimeDao().getAllGeneralLogtime();

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



	<hr />
	<div id="body">
		<div class="content">
			<h3>View Trips</h3>
			<form name="form1" action="trip_details_backtoback.jsp" method="POST"
				onsubmit="return validate()">
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
						<td><input name="tripDate" id="tripDate" type="text" size="6"
							class="{showOtherMonths: true, firstDay: 1, dateFormat: 'yyyy-mm-dd',
                                                 minDate: new Date(2008, 12 - 1, 25)}" /></td>

					</tr>
					<tr>
						<td>Log In/Out</td>
						<td><select name="tripMode" id="tripMode"
							onchange="getTripTime()">
								<option value="ALL">ALL</option>
								<option value="IN">IN</option>
								<option value="OUT">OUT</option>
						</select></td>
					</tr>
					<tr>
						<td>Shift</td>
						<td id="tripTimeId"><select name="tripTime" id="tripTime">
								<option value="ALL">ALL</option>
						</select></td>
					</tr>
					<tr>
						<td>&nbsp;</td>
						<td><input type="submit" class="formbutton" value="Show" /></td>
					</tr>
				</table>
			</form>
			<%@include file="Footer.jsp"%>
		</div>
	</div>
</body>
</html>
