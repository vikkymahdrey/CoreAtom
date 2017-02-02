 
<%@page import="com.agiledge.atom.billingprocess.report.dto.TripBasedBillingReportHelperDto"%>
<%@page import="com.agiledge.atom.billingprocess.report.dao.TripBasedBillingReportHelperDao"%>
<%@page import="com.agiledge.atom.dao.VehicleTypeDao"%>
<%@page import="com.agiledge.atom.dto.VehicleTypeDto"%>
<%@page import="com.agiledge.atom.dao.SiteDao"%>
<%@page import="com.agiledge.atom.dto.SiteDto"%>
<%@page import="com.agiledge.atom.service.VendorService"%>
<%@page import="com.agiledge.atom.dto.VendorDto"%>
<%@page import="com.agiledge.atom.dao.VehicleTypeDao"%>
<%@page import="com.agiledge.atom.dto.VehicleTypeDto"%>
<%@page import="com.agiledge.atom.dao.SiteDao"%>
<%@page import="com.agiledge.atom.dto.SiteDto"%>
<%@page import="java.util.List"%>
<%@page import="com.agiledge.atom.service.VendorService"%>
<%@page import="com.agiledge.atom.dto.VendorDto"%>
<%@page import="java.util.Date"%>
<%@page import="com.agiledge.atom.billingprocess.report.dto.TripBasedBillingReportHelperDto"%>
<%@page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
 
	pageEncoding="ISO-8859-1"%>
 
<%@ taglib uri="http://www.nfl.com" prefix="disp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
<title>Trip By Vehicle Type Report</title>
<script type="text/javascript" src="js/jquery-latest.js"></script>
<style type="text/css">
@import "css/jquery.datepick.css";
</style>
<% request.setAttribute("contextPath", request.getContextPath()); %>
<link rel="stylesheet" type="text/css" href="${contextPath}/css/displaytag.css" />
<script type="text/javascript" src="js/jquery.datepick.js"></script>
<script type="text/javascript" src="js/dispx.js"></script>

<script type="text/javascript">
function showReport(type,site, tripdate)
{
	var d=tripdate.split("-");
	tripdate=d[2]+"/"+d[1]+"/"+d[0];
	var params ="fromDate="+tripdate+"&toDate="+ tripdate + "&siteId=" + site+ "&vehicleTypeId=" + type;
	var url="display_ByVehicleTypeReport.jsp?" + params;
	window.location=url;
}

function resetFields()
{
	$("input[type=hidden]").val("");
}


function getSummaryReport(siteId,date,option, vendor)
{
	try{ 
	
	 
	
	var stddate=date.split('-');
	var date=stddate[2]+"/"+stddate[1]+"/"+stddate[0];
	
	var url="display_vehicleStatusReport.jsp?siteId=" + siteId+"&fromDate="+date+"&toDate="+date+"&option="+option+"&vendor="+vendor;
	var size = "height=600,width=1200,top=100,left=60,toolbars=no,menubar=no,location=no,scrollbars=yes,resizable=yes";
	var newwindow=window.open(url,'name',size);

	if (window.focus) {
		newwindow.focus();
	}
	}catch(e) {
		alert(e);
	}
	}
 </script>

<script type="text/javascript">        
            $(document).ready(function()
            {                                                                        
                $("#fromDate").datepick();
                $("#toDate").datepick();
            });     
        </script>
<script type="text/javascript">
            function validate()
            {
                  
                var fromdate=document.getElementById("fromDate").value;
                var todate=document.getElementById("toDate").value;
                if(fromdate.length<1)
                {
                    alert("Choose From Date");
                  //  date.focus();
                    return false;
                        
                }
                if(todate.length<1)
                {
                    alert("Choose To Date");
                  //  date.focus();
                    return false;
                        
                }
                 else
                {
                    return true;                            
                }
               
            }
            
            // get vehicle when site changes
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
        						//alert(fullvehicle);
        						fullvehicle = fullvehicle.split("$");
        						//document.getElementById("vehicleTypelistd").innerHTML = "<select name='vehicleType' id='vehicleType' multiple=''>"+fullvehicle[0]+"</select>";
        						document.getElementById("vehicleTypeAdded").innerHTML = "<select name='chosenVehicleType' id='chosenVehicleType'  ><option value=''>All</option>"+fullvehicle[1]+"</select>";
        					}
        				}
        				xmlhttp.open("POST",
        						"GetVehicleNotInSite?siteId=" + chosenSite, true);
        				xmlhttp.send();
        			} catch (e) {

        				alert(e);
        			}
        		}

        	}


        </script>

</head>
<body>
	<div id="body">
		<div class="content">
			<%
		String fromDate = request.getParameter("fromDate");
		String toDate = request.getParameter("toDate");
		String siteID = request.getParameter("siteId");
		String option = request.getParameter("option");
		String vehicleType= request.getParameter("chosenVehicleType");
		 
		
		String vendor="1";
		try{
			System.out.println(")))))))))))))))))))))");
		vendor= request.getParameter("vendor");
		vendor=vendor==null?"":vendor;
		 } catch(Exception e ) {
			 System.out.print("vendor : "+vendor);
			System.out.println("Error in jsp : " + e );
		}  
		//vendor="1";
	/* 	String logTimeIn = request.getParameter("shiftInTime");
		String logTimeOut = request.getParameter("shiftOutTime");		 		
	 */ 
		
		
		//System.out.println("List size : "+ dtolist.size());
		long empid = 0;
		String employeeId = OtherFunctions.checkUser(session);
		
			empid = Long.parseLong(employeeId);
			 
			ArrayList<VendorDto> masterVendorList = new VendorService().getMasterVendorlist();
			 
			
			
	%>

<%@include file="Header.jsp"%>
			<div>
				<form name="form1" action="displayTripBasedBilling4GeneralShift.jsp"
					method="POST" onsubmit="return validate()">
					<table>
						<tr>
							<td>Choose Site</td>
							<td>
							
							<select name="siteId" id="siteId" onchange="getVehicles(this.value)" >
								<option value="0">Select Site</option>
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
							</select></td>
							<td>Vendor
							</td>
							<td><select id="vendor" name="vendor">
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
							</td>
							<td>Vehicle Type
							</td>
							<td id="vehicleTypeAdded"><select  
							name="chosenVehicleType" id="chosenVehicleType" height="4">
								<option value='all'>All</option>
							<%
							
							ArrayList<VehicleTypeDto> vehicleTypeInSiteDtos = null;
							try{
							vehicleTypeInSiteDtos = new VehicleTypeDao()
							.getSiteVehicleType(request.getParameter("siteId"));
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
							</td>

							<td align="right">From</td>
							<td align="left" colspan="2"><input name="fromDate"
								id="fromDate" type="text" size="6" readonly="readonly"
								class="{showOtherMonths: true, firstDay: 1, dateFormat: 'yyyy-mm-dd',
	                                                 minDate: new Date(2008, 12 - 1, 25)}"
								value="<%=fromDate!=null&&fromDate.trim().equals("")==false?fromDate:"" %>" />



								To<input name="toDate" id="toDate" type="text" size="6"
								readonly="readonly"
								class="{showOtherMonths: true, firstDay: 1, dateFormat: 'yyyy-mm-dd',
                                                 minDate: new Date(2008, 12 - 1, 25)}"
								value="<%=toDate!=null&&toDate.trim().equals("")==false?toDate:"" %>" />


							</td>





							 
							<td>Option</td> 

							<td><select name="option" id="option">
									<%
									
									 String [] options={"All","Time slot","Time of the day","Distance grid"};  
									 
									 
									for (String opt: options) {
									
									String optionSelect="";
									if(opt.equals(option))
									{
										optionSelect="selected";
									}
								 
								%>

									<option <%=optionSelect %> value="<%=opt%>"><%=opt%></option>
									<%  }%>
							</select></td>
							<td><input type="submit" class="formbutton"
								value="Generate Report" /> <input type="reset"
								class="formbutton" value="Reset" onclick="resetFields();" /></td>
						</tr>
					</table>
				</form>
			</div>

			<hr />
			<hr />
			<br />
			<h2 align="center">Trip By Vehicle Type Report</h2>
			<hr />
			<%
			ArrayList<TripBasedBillingReportHelperDto> dtoList =null;
			try{
				dtoList = new TripBasedBillingReportHelperDao().getTripBasedBillingTripCostReport_monthly(siteID, vendor, vehicleType, fromDate, toDate,"","","","");
						 
				
			}catch(Exception e) {
				System.out.println(" Error in jsp "+ e);
			} 
	 	TripBasedBillingReportHelperDto dto = new TripBasedBillingReportHelperDto();
		 	 
		if(dtoList!=null&&dtoList.size()>0) {
			System.out.println("SIze "+ dtoList.size());
		 
			pageContext.setAttribute("siteID", siteID);
			pageContext.setAttribute("selvendor", vendor);
			pageContext.setAttribute("vehtype", vehicleType);
			pageContext.setAttribute("option", option);
			 
		%>
		
		
			<disp:dipxTable id="outer"  style="width:60%; margin-left:20%" list="<%=dtoList %>" >
				<disp:dipxColumn style="text-align:center;" property="year" title="Year"/>
				 <disp:dipxColumn style="text-align:center;" property="monthName" title="Month"
				  href="displayTripBasedBilling4GeneralShiftDaily.jsp?siteId=${siteID}&vendor=${selvendor}&chosenVehicleType=${vehtype}&fromDate=&year=${row.year }&toDate=&month=${row.monthName}&escort=All&constraint=All" />
				 <disp:dipxColumn style="text-align:center;" sortable="true"  property="vehicleType" title="Cab"/>
				 <disp:dipxColumn style="text-align:center;" sortable="true"  property="totalTrips"  title="#.Trips"/>
				 <disp:dipxColumn style="text-align:center;" sortable="true" property="rate" title="Trip Cost"  />
				  
			</disp:dipxTable> 	  
		
		<%} else { 
			System.out.println(" dto list is null ");
		}
		%>	<%@include file="Footer.jsp"%>
		</div>
	</div>

</body>
</html>
