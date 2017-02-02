<%@page import="com.agiledge.atom.dao.AdhocBookedEmployeeReportDao"%>
<%@page import="com.agiledge.atom.dao.SiteDao"%>
<%@page import="com.agiledge.atom.dto.SiteDto"%>
<%@page import="com.agiledge.atom.dto.AdhocBookedEmployeeReportDto"%>
<%@page import="org.displaytag.decorator.TotalTableDecorator"%>
<%@page import="org.displaytag.decorator.MultilevelTotalTableDecorator"%>
<%@page import="com.itextpdf.text.log.SysoLogger"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.*"%>
<%@page import="java.util.Date"%>
<%@page import="com.agiledge.atom.dto.LogTimeDto"%>
<%@page import="com.agiledge.atom.dao.LogTimeDao"%>
<%@page import="com.agiledge.atom.dao.OtherDao"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Calendar"%>

<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script type="text/javascript" src="js/jquery-latest.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Adhoc Booking Report</title>
<link rel="stylesheet" type="text/css"
	href="${contextPath}/css/displaytag.css" />
<script type="text/javascript" src="js/jquery-latest.js"></script>
<style type="text/css">
@import "css/jquery.datepick.css";
</style>

<script type="text/javascript" src="js/jquery.datepick.js"></script>
<script type="text/javascript">        
            $(document).ready(function()
            {         
            	getTripTime();
                $("#fromDate").datepick();
                $("#toDate").datepick();
            });     
        </script>
<script type="text/javascript">
            function validate()
            {
                  
                var fromdate=document.getElementById("fromDate").value;
                var todate=document.getElementById("toDate").value;
                var mod=document.getElementById("tripMode").value;   
                var time=document.getElementById("tripTime").value;
		
                if(fromdate.length<1)
                {
                    alert("Choose From Date");
                  //  date.focus();
                    return false;
                        
                }
                else if(todate.length<1)
                {
                    alert("Choose To Date");
                  //  date.focus();
                    return false;
                        
                }
		else if(todate<fromdate)
		{
			alert("From date should be less than toDate");
			 //  date.focus();
			return false;
		}
                else if(mod==="ALL")
                {
                    alert("Please select Log IN/OUT");
                    //time.options[0].selected=true;   
                    return false;
                }
                 else
                {
                    return true;                            
                }
               
            }
            
            function getTripTime()
            {     
            	try{
            	
                var logtype=document.getElementById("tripMode").value;
                if(logtype=="ALL")
                	{
                	var tripTimeId=document.getElementById("tripTimeId");
                	tripTimeId.innerHTML='<select name="tripTime" id="tripTime"> <option value="ALL" >ALL</option></select>';
                	return;
                	}
                var site=document.getElementById("siteId").value;
                var url="GetLogTime?logtype="+logtype+"&site="+site;                                    
                xmlHttp=GetXmlHttpObject()
                if (xmlHttp==null)
                {
                    alert ("Browser does not support HTTP Request");
                    return
                }                    
                xmlHttp.onreadystatechange=setLogTime	
                xmlHttp.open("POST",url,true)                
                xmlHttp.send(null)
            }catch(e){alert(e);}
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
<script language="javascript">
$(document).ready(function(){
	$("table#row tr:even").css("background-color","#43f3");
	$("table#row  tr:odd").css("background-color","#0032");
	 
	
});


	


</script>
</head>
<body>
	<%
	String fname1=("AdhocBookedReport :").concat(new Date().toString()).concat(".csv");
	String fname2=("AdhocBookedReport :").concat(new Date().toString()).concat(".xls");
	String fname3=("AdhocBookedReport :").concat(new Date().toString()).concat(".xml");
	
	String siteId=request.getParameter("siteId");
	String fromDate = request.getParameter("fromDate");
	String toDate = request.getParameter("toDate");
	String mod = request.getParameter("tripMode");
	String time = request.getParameter("tripTime");
	AdhocBookedEmployeeReportDao report = new AdhocBookedEmployeeReportDao();
	 
	ArrayList<AdhocBookedEmployeeReportDto> dtolist = new ArrayList<AdhocBookedEmployeeReportDto>();
	
	if(fromDate!=null&&toDate!=null)
	{
		 dtolist = report.getAdhocBookedEmp(siteId, fromDate, toDate, mod, time);
		 
	}
	long empid = 0;
	String employeeId = OtherFunctions.checkUser(session);
		empid = Long.parseLong(employeeId);
	
	%>
	<%@include file="Header.jsp"%>
	<div id="body">
		<div class="content">
			<h2 align="center">Adhoc Booking Report</h2>
			<div>

				<form action="display_adhoc_booked_employees.jsp" method="POST"
					onsubmit="return validate()">

					<table width="20%">

						<tr>
							<td align="right">Choose Site</td>
							<td><select name="siteId" id="siteId"
								onchange="getTripTime()">
									<%
									String site=(request.getSession().getAttribute("site")==null||request.getSession().getAttribute("site").toString().trim().equals(""))?"" :request.getSession().getAttribute("site").toString().trim();
									 List<SiteDto> siteDtoList = new SiteDao().getSites();  
									 
									 site=request.getParameter("siteId")!=null&&request.getParameter("siteId").trim().equals("")==false?request.getParameter("siteId"):site;
									
										
									 
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
							<td align="right">From</td>
							<td align="left" colspan="2"><input name="fromDate"
								id="fromDate" type="text" size="6" readonly="readonly"
								class="{showOtherMonths: true, firstDay: 1, dateFormat: 'yyyy-mm-dd',
	                                                 minDate: new Date(2008, 12 - 1, 25)}"
								value="<%=fromDate!=null&&fromDate.trim().equals("")==false?fromDate:"" %>" />

							</td>
							<td align="right">To</td>
							<td align="left"><input name="toDate" id="toDate"
								type="text" size="6" readonly="readonly"
								class="{showOtherMonths: true, firstDay: 1, dateFormat: 'yyyy-mm-dd',
                                                 minDate: new Date(2008, 12 - 1, 25)}"
								value="<%=toDate!=null&&toDate.trim().equals("")==false?toDate:"" %>" />


							</td>
							<td align="right">Log In/Out</td>
							<td><select name="tripMode" id="tripMode"
								onchange="getTripTime()">

									<%
							String shiftLogLabel[] = {"ALL", "IN", "OUT"};
							String shiftLogValue[] = {"ALL", "IN", "OUT" };
							String slParamSession="";
							String slParam="";
							try {
								slParam=request.getParameter("shiftLog");
								slParam=slParam==null?"":slParam;
								slParamSession=request.getSession().getAttribute("shiftLog").toString();
							    slParam = slParam.equals("")?slParamSession:slParam;
							}catch(Exception e) {
								slParamSession="";
							}
							for(int slCntr=0;slCntr<shiftLogLabel.length; slCntr++) {
								String slSelect="";
								if(slParam.equals(shiftLogValue[slCntr])) {
									slSelect="selected";
								}
						%>
									<option <%=slSelect %> value="<%=shiftLogLabel[slCntr] %>"><%=shiftLogValue[slCntr] %></option>
									<%
							}	
								%>


							</select></td>
							<td align="right">Shift</td>
							<td id="tripTimeId"><select name="tripTime" id="tripTime">
									<option value="ALL">ALL</option>
							</select></td>

							<td>&nbsp;</td>


							<td><input type="submit" class="formbutton" value="Generate" />
							</td>


						</tr>
					</table>
				</form>
			</div>

		</div>


		<hr />

		<%
			 TotalTableDecorator grandTotals=new TotalTableDecorator();
		
			pageContext.getRequest().setAttribute("tableDecor", grandTotals);
			
			%>



		<display:table class="alternateColor" name="<%=dtolist%>" id="row"
			export="true" defaultsort="1" defaultorder="descending" pagesize="50">
			
			<display:column property="adhocType" title="Adhoc Type"
				sortable="true" headerClass="sortable" />
				<display:column property="bookedFor" title="Booked For"
				sortable="true" headerClass="sortable" />
				<display:column property="personnelNo" title="PersonnelNo"
				sortable="true" headerClass="sortable" />
				<display:column property="pickupDrop" title="LogType"
				sortable="true" headerClass="sortable" />
				<display:column property="startTime" title="Time" sortable="true"
				headerClass="sortable" />
			<display:column property="travelDate" title="Travel Date"
				sortable="true" headerClass="sortable" />			
			<display:column property="bookedBy" title="Booked By" sortable="true"
				headerClass="sortable" />			
			
			<display:column property="approvedBy" title="ApprovedBy"
				sortable="true" headerClass="sortable" />
			<display:column property="status" title="Status" sortable="true"
				headerClass="sortable" />
				<display:column property="bookedDate" title="Booked Date"
				sortable="true" headerClass="sortable" />


			<display:setProperty name="export.csv.filename" value="<%=fname1%>" />
			<display:setProperty name="export.excel.filename" value="<%=fname2%>" />
			<display:setProperty name="export.xml.filename" value="<%=fname3%>" />
		</display:table>




		<%@include file="Footer.jsp"%>
	</div>


</body>
</html>