
 <%@page import="com.agiledge.atom.service.VendorService"%>
<%@page import="com.agiledge.atom.dao.VehicleTypeDao"%>
<%@page import="com.agiledge.atom.dto.VehicleDto"%>
<%@page import="com.agiledge.atom.dto.TripDetailsDto"%>
<%@page import="com.agiledge.atom.dao.VehicleDao"%>
<%@page import="com.agiledge.atom.dto.VehicleTypeDto"%>
<%@page import="com.agiledge.atom.dto.VendorDto"%>
<%@page import="com.agiledge.atom.dto.SiteDto"%>
<%@page import="com.agiledge.atom.dao.SiteDao"%>
<%@page import="com.agiledge.atom.service.LogTimeService" %>
<%@page import="com.agiledge.atom.dto.LogTimeDto"%>
<%@page import="org.displaytag.decorator.TotalTableDecorator"%>
<%@page import="com.agiledge.atom.billingprocess.report.dao.KmBasedClassicBillingReportHelperDao"%>
<%@page import="com.agiledge.atom.billingtype.config.dto.BillingTypeConfigConstants"%>
<%@page import="java.util.Calendar"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Date"%>
<%@page import="com.agiledge.atom.billingprocess.report.dto.TripBasedBillingReportHelperDto"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.agiledge.atom.billingprocess.report.dao.TripBasedBillingReportHelperDao"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib uri="http://www.nfl.com" prefix="disp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
<title>TRIP Based Billing Report - General Shift</title>
<script type="text/javascript" src="js/jquery-latest.js"></script>
<style type="text/css">
@import "css/jquery.datepick.css";
</style>
<% request.setAttribute("contextPath", request.getContextPath()); %>
<link rel="stylesheet" type="text/css" href="${contextPath}/css/displaytag.css" />
<style type="text/css">
	div#filterContainer {
		height: auto;
		background-colore : red;
		width: 100%;
		height: 50px;
	}
	div#filterContainer > div {
		display: inline;
		 float: left;
		 padding-left:12px;
	}
		div#filterContainer >div > div:FIRST-CHILD {
		display: inline;
		 float: left;
		 margin-bottom: 5px; 
		 margin-top: 10px;
	}
	
	div#reportDiv {
		margin-top:25px;
	}
</style>

<script type="text/javascript" src="js/jquery.datepick.js"></script>
<script type="text/javascript" src="js/dispx.js"></script>

<script type="text/javascript">
	$(document).ready(function() {
        
        $("#fromDate").datepick();
        $("#toDate").datepick();

        getVehicles($("select[name=siteId]").val());
	});
</script>

<script type="text/javascript">
function resetFields()
{
	$("input[type=hidden]").val("");
}
 
function validate()
{    
    var fromdate=document.getElementById("fromDate").value;
    var todate=document.getElementById("toDate").value;
    var logtime=document.forms["form1"]["logtimes"].selectedIndex;
    
    if(fromdate.length<1)
    {
        alert("Choose From Date");
        return false;
            
    }
    if(todate.length<1)
    {
        alert("Choose To Date");
        return false;
            
    }
    if (logtime< 0) {
    	alert("Choose Shift Time");
        return false;
    	}
     else
    {
        return true;                            
    }
}

 
            function getVehicles(chosenSite) {
            	
        		if (chosenSite.length > 0) {
        			try {
        				var xmlhttp;
        				if (window.XMLHttpRequest) {// code for IE7+, Firefox, Chrome, Opera, Safari
        					xmlhttp = new XMLHttpRequest();
        				} else {// code for IE6, IE5
        					xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
        				}
        				xmlhttp.onreadystatechange = function() {
        					if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
        						var fullvehicle = xmlhttp.responseText;
        					//	alert(fullvehicle);
        						//fullvehicle = fullvehicle.split("$");
        						//document.getElementById("vehicleTypelistd").innerHTML = "<select name='vehicleType' id='vehicleType' multiple=''>"+fullvehicle[0]+"</select>";
        						var selectedVehicleType = $("select[name=chosenVehicleType]").val();
                				
        						document.getElementById("vehicleTypeAdded").innerHTML = "<select name='chosenVehicleType' id='chosenVehicleType' onChange='refreshy();' ><option value='all'>All</option>"+fullvehicle+"</select>";
        						$("select[name=chosenVehicleType]").val(selectedVehicleType);
        					}
        				}
        				xmlhttp.open("POST",
        						"GetVehicleType?siteId=" + chosenSite, true);
        				xmlhttp.send();
        			} catch (e) {

        			//	alert(e);
        			}
        		}
            }
            
function refreshy()
   {
	var sel=$("select[name=chosenVehicleType]").val();
	var site=$("select[name=siteId]").val();
	window.location.href ="display_cabwisenew.jsp?chosenVehicleType="+sel+"&siteId="+site;
	}
	
function move(tbFrom, tbTo) 
{
 var arrFrom = new Array(); var arrTo = new Array(); 
 var arrLU = new Array();
 var i;
 for (i = 0; i < tbTo.options.length; i++) 
 {
  arrLU[tbTo.options[i].text] = tbTo.options[i].value;
  arrTo[i] = tbTo.options[i].text;
 }
 var fLength = 0;
 var tLength = arrTo.length;
 for(i = 0; i < tbFrom.options.length; i++) 
 {
  arrLU[tbFrom.options[i].text] = tbFrom.options[i].value;
  if (tbFrom.options[i].selected && tbFrom.options[i].value != "") 
  {
   arrTo[tLength] = tbFrom.options[i].text;
   tLength++;
  }
  else 
  {
   arrFrom[fLength] = tbFrom.options[i].text;
   fLength++;
  }
}

tbFrom.length = 0;
tbTo.length = 0;
var ii;

for(ii = 0; ii < arrFrom.length; ii++) 
{
  var no = new Option();
  no.value = arrLU[arrFrom[ii]];
  no.text = arrFrom[ii];
  tbFrom[ii] = no;
}

for(ii = 0; ii < arrTo.length; ii++) 
{
 var no = new Option();
 no.value = arrLU[arrTo[ii]];
 no.text = arrTo[ii];
 tbTo[ii] = no;
}
var elements = document.getElementById("logtimes").options;
for(var i = 0; i < elements.length; i++){
     elements[i].selected = true;
   }
}

        </script>

</head>
<body  >
	<%@include file="Header.jsp"%>
	
	<div id="body">
		<div class="content">
			<%

			String fname1=("TripReport :").concat(new Date().toString()).concat(".csv");
			String fname2=("TripReport :").concat(new Date().toString()).concat(".xls");
			String fname3=("TripReport :").concat(new Date().toString()).concat(".xml");
		String fromDate = request.getParameter("fromDate");
		String toDate = request.getParameter("toDate");
		String siteID = request.getParameter("siteId");
		String option = request.getParameter("option");
		String vehicleType= request.getParameter("chosenVehicleType");
		String vehicle=request.getParameter("chosenregNo");
		String escort = request.getParameter("escort");
		String logtimes[] = request.getParameterValues("logtimes");

		
		String vendor="1";
		try{
			System.out.println(")))))))))))))))))))))");
		vendor= request.getParameter("vendor");
		vendor=vendor==null?"":vendor;
		 } catch(Exception e ) {
			 System.out.print("vendor : "+vendor);
			System.out.println("Error in jsp : " + e );
		}
		String  url="display_cabwisenew.jsp?siteId=" + siteID+"&fromDate="+fromDate+"&toDate="+toDate+"&vendor="+vendor+"&logtimes="+logtimes;
		long empid = 0;
		String employeeId = OtherFunctions.checkUser(session);
		
			empid = Long.parseLong(employeeId);
			 
			ArrayList<VendorDto> masterVendorList = new VendorService().getMasterVendorlist();
			 
			
			
	%>

 
			<div>
				<form name="form1" id="form1" action="display_cabwisenew.jsp"
					method="GET" onsubmit="return validate()" >
					 
					<div id="filterContainer">
						 
							<div><div>Choose Site</div>
							<div>
							
							<select name="siteId" id="siteId" onchange="getVehicles(this.value)" >
								<option value="ALL">ALL</option>
									<%
									
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
							</select></div></div>
							<div>
							<div>Vendor
							</div>
							<div><select id="vendor" name="vendor">
									<%
						 
									for (VendorDto vendorDto : masterVendorList) {
											String vendorSelect="";
											System.out.println("dto :"+vendorDto.getCompanyId());
											if(vendor!=null&&vendorDto.getCompanyId().equals(vendor)) {
												vendorSelect="selected";
											}
									%>

									<option <%=vendorSelect %> value="<%=vendorDto.getCompanyId()%>"><%=vendorDto.getCompany()%></option>
									<%
										}
						 
									%>
							</select>
							</div></div>
							<div>
							<div>Vehicle Type
							</div>
							<div id="vehicleTypeAdded"><select  
							name="chosenVehicleType" id="chosenVehicleType" onchange='refreshy();'>
								<option value='all'>All</option>
							<%
							
							ArrayList<VehicleTypeDto> vehicleTypeInSiteDtos = null;
							try{
							vehicleTypeInSiteDtos = new VehicleTypeDao()
							.getAllVehicleType(request.getParameter("siteId"));
							if(vehicleTypeInSiteDtos!=null&&vehicleTypeInSiteDtos.size()>0) {
								for (VehicleTypeDto dto : vehicleTypeInSiteDtos) {
									String vehicleSelect="";
									try {
									
									if(request.getParameter("chosenVehicleType").equals(String.valueOf(dto.getId()))) {
										vehicleSelect="selected";
										System.out.println("td :"+dto.getId()+" Vehicle type ; "+ request.getParameter("chosenVehicleType"));
									} } catch(Exception e) {
										vehicleSelect="";
									}
									 
								
							%>
									<option <%=vehicleSelect %> value='<%=dto.getId() %>'><%=dto.getType() %></option>
							<%
								}
							}
							}catch(Exception e) {} %>
							</select>
 							</div>
 							</div>
 														<div>
							<div>Register No
							</div>
							<div id="regNo"><select  
							name="chosenregNo" id="chosenregNo" >
								<option value='all'>All</option>
							<%
							
						ArrayList<VehicleDto> vehicleInSiteDtos = null;
							try{
							vehicleInSiteDtos = new VehicleDao().getvehiclebyType(request.getParameter("chosenVehicleType"));
							if(vehicleInSiteDtos!=null&&vehicleInSiteDtos.size()>0) {
								for (VehicleDto vdto : vehicleInSiteDtos) {
									String vehicleSelect="";
									try {
									
									if(request.getParameter("chosenregNo").equals(String.valueOf(vdto.getId()))) {
										vehicleSelect="selected";
														} }catch(Exception e) {
										vehicleSelect="";
									}
									 
								
							%>
								<option <%=vehicleSelect %> value='<%=vdto.getId() %>'><%=vdto.getVehicleNo()%></option>
							<%
								}
							}
							}catch(Exception e) {} %>
							</select>
 							</div>
 							</div>
 							<div>
	 							<div>
	 								<label> From</label>
	 							</div>
	 							<div>
	 								<label class="scd" >
									<input name="fromDate" id="fromDate" type="text" size="6" readonly="readonly" class="{showOtherMonths: true, firstDay: 1, dateFormat: 'yyyy-mm-dd', minDate: new Date(2008, 12 - 1, 25)}" value="<%=fromDate!=null&&fromDate.trim().equals("")==false?fromDate:"" %>" />
									</label>
								 		
	 								
	 							</div>
 							</div>
							<div  > 
							<div >		  
  									
									
									<label>To</label>
		 								
  							</div>
 							<div>
 																	 		
		 							<label class="scd" ><input name="toDate" id="toDate" type="text" size="6"
										readonly="readonly"
										class="{showOtherMonths: true, firstDay: 1, dateFormat: 'yyyy-mm-dd',
		                                                 minDate: new Date(2008, 12 - 1, 25)}"
										value="<%=toDate!=null&&toDate.trim().equals("")==false?toDate:"" %>" />
									</label>									
 								</div>
 		
						</div>
							 
						<div>
								<div>
									 Escort <%
							  
							List<String> escorts = new ArrayList<String>();
							escorts.add("ALL" );
							escorts.add(BillingTypeConfigConstants.WITH_ESCORT);
							escorts.add(BillingTypeConfigConstants.WITHOUT_ESCORT );
							
							%>
								</div>
								
								<div>	
									 <disp:select name="escort"   selectedValue="<%=escort %>" list="<%=escorts %>" >
									 	 
									</disp:select>
								</div>
									 
							</div>
						 
							<div>
								<div>
									 Shift Time <%

							ArrayList<LogTimeDto> llist=new LogTimeService().getAllTime();
									 
							
							%>
								</div>
								
								<div>	
									<select multiple="multiple" name="alllogtimes" id="alllogtimes">
									<%for(LogTimeDto ldto:llist)
									{
									%>
									<option value='<%=ldto.getLogTime() %>'><%=ldto.getLogTime() %></option>
									<%} %>
									</select>								</div>
									 
							</div><div><br/><br/><input type="button" class="formbutton" id="movebtn" value="=>" onClick="move(this.form.alllogtimes,this.form.logtimes)"/><br/><br/><input type="button" class="formbutton" id="movebtn1" value="<=" onClick="move(this.form.logtimes,this.form.alllogtimes)"/> </div><div></div>
							<div>
							
								<div>Selected</div><div>
									<select multiple="multiple" name="logtimes" id="logtimes">
									
									</select>	</div></div>
					 
 
						<div style="margin-top:29px;">
							 
							 
							 	
							 		<input type="submit" class="formbutton" value="Generate Report" />
							 		<input type="reset" class="formbutton" value="Reset" onclick="resetFields();" /> 
							 	
							</div>
							 
						 
					</div>
					
							

				</form>
			</div> 

			<div id="reportDiv" >
			
			<br />
			<br />
			<br />
			<br />
			<h2 align="center">Trip Based Billing Report - General Shift</h2>
			<hr />
			<%
			ArrayList<TripDetailsDto> dtoList =null;
			try{
				dtoList = new TripBasedBillingReportHelperDao ().getTripBasedBillingTripCostReportmonthlywithlog(siteID, vendor, vehicleType, fromDate, toDate, escort, logtimes,vehicle);		 
				
			}catch(Exception e) {
				System.out.println(" Error in jsp "+ e);
			} 
			TripDetailsDto dto = new TripDetailsDto();
		 	 
		if(dtoList!=null&&dtoList.size()>0) {
			TotalTableDecorator grandTotals = new TotalTableDecorator();
			pageContext.getRequest().setAttribute("tableDecor", grandTotals);
			double triptotal=0.0,escorttotal=0.0;
			for(TripDetailsDto tdto :dtoList)
			{
			triptotal+=tdto.getTripRate();	
			escorttotal+=tdto.getEscortRate();	
			}
			 
		%>
		<table>
		<tr><td><h3>Cost Summary</h3></td><td></td></tr><tr><td>Total Trip Cost</td><td><%=triptotal %></td></tr>
		<tr><td>Escort Total</td><td><%=escorttotal %></td></tr>
		</table>
		 			<display:table class="alternateColorInSub" id="row"  style="width:60%; margin-left:20%" name="<%=dtoList %>" export="true" defaultsort="1" defaultorder="descending" decorator="tableDecor" pagesize="100" summary="myTotalValue" >
		 			<display:column property="siteId" sortable="true" title="Site" />
				    <display:column property="vehicleNo" sortable="true" title="Register Number" />
				 	<display:column property="trip_code" sortable="true" title="Trip Code" />
				 	<display:column property="tripDate"  sortable="true" title="Trip Date" />
				 	<display:column property="trip_time" sortable="true" title="Time" />
				 	<display:column property="trip_log"  sortable="true" title="Shift" />
				 	<display:column property="tripRate"  sortable="true" title="Trip Rate"  total="true" format="{0,number,0.00}"  />
				 	<display:column property="escortRate" sortable="true" title="Escort Cost" total="true"  format="{0,number,0.00}"/>
				 <display:setProperty name="export.csv.filename" value="<%=fname1%>" />
				<display:setProperty name="export.excel.filename"
					value="<%=fname2%>" />
				<display:setProperty name="export.xml.filename" value="<%=fname3%>" />
				 </display:table>	
				
				 
		<%} else { 
			System.out.println(" dto list is null ");
		}
		%>	</div>
		<%@include file="Footer.jsp"%>
		</div>
	</div>

</body>
</html>
