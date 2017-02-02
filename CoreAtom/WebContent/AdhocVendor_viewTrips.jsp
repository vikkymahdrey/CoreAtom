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
<title>Adhoc View Trips</title>
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
                var date=document.getElementById("tripDate").value;  
                if(date.length<1)
                {
                    alert("Please Choose Date!");
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
            List<SiteDto> siteDtoList = new SiteDao().getSites();

        %>
	<%
            long empid = 0;
            String employeeId = OtherFunctions.checkUser(session);
                empid = Long.parseLong(employeeId);
                %>
	<%@include file="Header.jsp"%>



	<hr />
	<div id="body">
		<div class="content">
			<center><h3>View Adhoc Trips</h3></center>
			<form name="form1" action="adhoctripsheet.jsp" method="POST"
				onsubmit="return validate()">
				<table>
					<tr>
						<td align="right">Choose Site</td>
						<td><select  name="siteId" id="siteId"  onchange="getTripTime()">
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


						<td align="right">Date</td>
						<td>
							<%
							String tripDateSession ="";
							try {
							 tripDateSession = request.getSession().getAttribute("date").toString();
							} catch(Exception e) {
								tripDateSession="";
							}
							
								String tripDate=request.getParameter("tripDate");
							 	tripDate = (tripDate==null || tripDate.trim().equals("")) ?tripDateSession:tripDate;
							 	request.getSession().setAttribute("date", tripDate);
							%>
						<input name="tripDate" id="tripDate" type="text" size="6" value="<%=tripDate %>"
							class="{showOtherMonths: true, firstDay: 1, dateFormat: 'yyyy-mm-dd',
                                                 minDate: new Date(2008, 12 - 1, 25)}" /></td>

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
