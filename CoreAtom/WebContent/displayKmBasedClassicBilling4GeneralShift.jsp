


 <%@page import="com.agiledge.atom.billingprocess.report.dao.KmBasedClassicBillingReportHelperDao"%>
<%@page import="com.agiledge.atom.dao.VehicleTypeDao"%>
<%@page import="com.agiledge.atom.dto.VehicleTypeDto"%>
<%@page import="com.agiledge.atom.dao.SiteDao"%>
<%@page import="com.agiledge.atom.dto.SiteDto"%>
<%@page import="java.util.List"%>
<%@page import="com.agiledge.atom.transporttype.dto.TransportTypeConfig"%>
<%@page import="com.agiledge.atom.service.VendorService"%>
<%@page import="com.agiledge.atom.dto.VendorDto"%>
<%@page import="com.agiledge.atom.billingprocess.report.dao.SlabBasedBillingReportHelperDao"%>
<%@page import="com.agiledge.atom.billingprocess.report.dao.KmBasedTemplateBillingReportHelperDao"%>
<%@page import="com.agiledge.atom.billingprocess.report.dao.KmBasedTemplateBillingReportHelperDao"%>
<%@page import="com.agiledge.atom.billingtype.config.dto.BillingTypeConfigConstants"%>
<%@page import="java.awt.Menu"%>
<%@page import="java.util.Set"%>
<%@page import="java.util.Map"%>
 
<%@page import="com.agiledge.atom.billingtype.dto.BillingTypeConstant"%>
<%@page import="com.agiledge.atom.billingprocess.service.BillingProcessUtil"%>
<%@page import="java.util.Calendar"%>
<%@page import="java.util.Date"%>
<%@page import="com.agiledge.atom.billingprocess.report.dto.TripBasedBillingReportHelperDto"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.agiledge.atom.billingprocess.report.dao.TripBasedBillingReportHelperDao"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
 
<%@ taglib uri="http://www.nfl.com" prefix="disp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
<title>Km Based Template Billing Report - General Shift</title>
<script type="text/javascript" src="js/jquery-latest.js"></script>
<style type="text/css">
@import "css/jquery.datepick.css";
</style>
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
<% request.setAttribute("contextPath", request.getContextPath()); %>
<link rel="stylesheet" type="text/css" href="${contextPath}/css/displaytag.css" />
<link href="css/second_menu.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="js/jquery.datepick.js"></script>
<script type="text/javascript" src="js/dispx.js"></script>

<script type="text/javascript">
 function showReport(url,month, year)
{ 
	 alert('go');
	var urlP=url + "&month=" + month + "&year=" + year+ "&escort="+ $("select[name=escort]").val(); 
	//window.location=urlP;
	window.open(urlP);
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
	
	var url="displayKmBasedClassicBilling4GeneralShiftDaily.jsp?siteId=" + siteId+"&fromDate="+date+"&toDate="+date+"&vendor="+vendor;
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
//var selectedVehicleType;
            $(document).ready(function()
            {                                                                        
                $("#fromDate").datepick();
                $("#toDate").datepick();
                $(".scd").toggle();
                $("#sCY").click(function () {
                	$(".scd").toggle();
                	$(".scy").toggle();
                	$("select[name=year]").val("");
                	$("select[name=month]").val("");
                	$("input[name=fromDate]").val("");
                	$("input[name=toDate]").val("");
                	
                });
                
   
                getVehicles($("select[name=siteId]").val());
                
            });     
        </script>
<script type="text/javascript">
            function validate()
            {
             /*      
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
              */
              return true;
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
        					//	alert(fullvehicle);
        						fullvehicle = fullvehicle.split("$");
        						//document.getElementById("vehicleTypelistd").innerHTML = "<select name='vehicleType' id='vehicleType' multiple=''>"+fullvehicle[0]+"</select>";
        						var selectedVehicleType = $("select[name=chosenVehicleType]").val();
                				
        						document.getElementById("vehicleTypeAdded").innerHTML = "<select name='chosenVehicleType' id='chosenVehicleType'  ><option value=''>All</option>"+fullvehicle[1]+"</select>";
        						$("select[name=chosenVehicleType]").val(selectedVehicleType);
        					}
        				}
        				xmlhttp.open("POST",
        						"GetVehicleNotInSite?siteId=" + chosenSite, true);
        				xmlhttp.send();
        			} catch (e) {

        			//	alert(e);
        			}
        		}

        	}


        </script>

</head>
<body>
	<%@include file="Header.jsp"%>
	<div id="body">
		<div class="content">
		
				<%
		String fromDate = request.getParameter("fromDate");
		String toDate = request.getParameter("toDate");
		String siteID = request.getParameter("siteId");
		String option = request.getParameter("option");
		String vehicleType= request.getParameter("chosenVehicleType");
		String year= request.getParameter("year");
		String month= request.getParameter("month");
		String escort = request.getParameter("escort");
		 
		
		year = year == null ? "":year;
		month = month == null ? "":month;

		
		String vendor="1";
		try{
			System.out.println(")))))))))))))))))))))");
		vendor= request.getParameter("vendor");
		vendor=vendor==null?"":vendor;
		 } catch(Exception e ) {
			 System.out.print("vendor : "+vendor);
			System.out.println("Error in jsp : " + e );
		}
		String  url="displayKmBasedClassicBilling4GeneralShift.jsp?siteId=" + siteID+"&fromDate="+fromDate+"&toDate="+toDate+"&vendor="+vendor;
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



 	 <div class="div-menu-outer"  >

 	<%
 		
 	try {
 		String mySite=(request.getSession().getAttribute("site")==null||request.getSession().getAttribute("site").toString().trim().equals(""))?"" :request.getSession().getAttribute("site").toString().trim();
 		Map <String, String> menuMap = BillingProcessUtil.getBillingPages(mySite, TransportTypeConfig .GENERAL_SHIFT);
 		Set<String> keySet = menuMap.keySet();
 		for(String key : keySet) {
 			String cur="";
 			  
 			if(menuMap.get(key).equalsIgnoreCase( request.getServletPath().substring(1) )) {
 				cur="cur";
 			}
 	%>  
 	 	<div class="div-menu-inner <%=cur %>" >
 	 	<a href="<%= menuMap.get(key)%>"><%=key %></a>
 	 </div>
 	 <%
 	 }
 	}catch(Exception e) {
 		System.out.println("exception : " + e);
 	}
 	 %>
 	  
  	
 	 
 	  </div>
	 <hr/>
			<div>
				<form name="form1" action="displayKmBasedClassicBilling4GeneralShift.jsp"
					method="POST" onsubmit="return validate()">
					<div id="filterContainer">
						 
							<div><div>Choose Site</div>
							<div>
							
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
							name="chosenVehicleType" id="chosenVehicleType" height="4">
								<option value=''>All</option>
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
 							</div>
 							</div>
 							<div>
	 							<div>
	 								<label class="scd"   > From</label>
	 								<label class="scy"  > Year</label>
	 							</div>
	 							<div>
	 								<label class="scd" >
									<input name="fromDate" id="fromDate" type="text" size="6" readonly="readonly" class="{showOtherMonths: true, firstDay: 1, dateFormat: 'yyyy-mm-dd', minDate: new Date(2008, 12 - 1, 25)}" value="<%=fromDate!=null&&fromDate.trim().equals("")==false?fromDate:"" %>" />
									</label>
									<label class="scy" >
											<%
												ArrayList<String> years = new ArrayList<String> ();
												Calendar cal = Calendar.getInstance();
												cal.setTimeInMillis(new Date( ).getTime());
												int yearCount = new Date().getYear()-20 +1900;
												 
												 int selectedYearIndex=20;
												for(int i=0;i<=50;i++) {
													
													if(year.equals(String.valueOf(yearCount))) {
														 selectedYearIndex=i;
													 }
													years.add(String.valueOf(yearCount++));
													
													 System.out.println("Year : "+ yearCount);
													 
												}
												String monthsp[]={"January","February", "March", "April", "May", "June", "July", "Auguest", "September", "October", "November", "December" };
												ArrayList<String> months = new ArrayList<String>();
												int selectedMonthIndex = cal.get(Calendar.MONTH);
												for(int i=0;i<monthsp.length; i++) {
													 months.add(monthsp[i] );
													 if(month.equals(monthsp[i])) {
														 selectedMonthIndex=i;
													 }
												}
											%>
	 						 			<disp:select id="year"   name="year" list="<%=years %>" selectedIndex="<%= String.valueOf(selectedYearIndex) %>"  >
 												 <option value="" > Select </option>
 											</disp:select>
  
								 		</label>
								 		
	 								
	 							</div>
 							</div>
							<div  > 
								<div >		  
  									
									
									<label class="scd"  >To</label>
		 								<label class="scy"  >Month</label>
		 								<a  name="scy" id="sCY" >Change</a>
		 								
  								</div>
 								<div>
 																	 		
		 							<label class="scd" ><input name="toDate" id="toDate" type="text" size="6"
										readonly="readonly"
										class="{showOtherMonths: true, firstDay: 1, dateFormat: 'yyyy-mm-dd',
		                                                 minDate: new Date(2008, 12 - 1, 25)}"
										value="<%=toDate!=null&&toDate.trim().equals("")==false?toDate:"" %>" />
									</label>
										
									<label class="scy" > 
		  									 <disp:select id="month"   name="month" list="<%=months %>" selectedIndex="<%= String.valueOf(selectedMonthIndex) %>"  >
 												 <option value="" > Select </option>
 											</disp:select>
   
										
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
						  					 

						<div style="margin-top:29px;">
							 
							 
							 	
							 		<input type="submit" class="formbutton" value="Generate Report" />
							 		<input type="reset" class="formbutton" value="Reset" onclick="resetFields();" /> 
							 	
							</div>
							 
						 
					</div>
		  	</form>
			</div>
			<div id="reportDiv">
			<hr />
			<hr />
			<br />
			<h2 align="center">Km Based Classic Billing Report - General Shift</h2>
			<hr />
			<%
			ArrayList<TripBasedBillingReportHelperDto> dtoList =null;
			try{
				dtoList = new KmBasedClassicBillingReportHelperDao().getKmBasedClassicBillingTripCostReport_monthly(siteID, vendor, vehicleType, fromDate, toDate,month,year,escort," " );
						 
				
			}catch(Exception e) {
				System.out.println(" Error in jsp "+ e);
			} 
	 	TripBasedBillingReportHelperDto dto = new TripBasedBillingReportHelperDto();
		 	 
		if(dtoList!=null&&dtoList.size()>0) {
			System.out.println("SIze "+ dtoList.size());
		 pageContext.setAttribute("pageUrl",url);
		//	pageContext.setAttribute("siteID", siteID);
			 
		%>
		 
		
			<disp:dipxTable id="outer"  style="width:60%; margin-left:20%" list="<%=dtoList %>"  >
				 <disp:dipxColumn sortable="true" href="javascript:showReport('${pageUrl }', '${row.monthName }', '${row.year}')" group="1" style="text-align:center;" property="monthName" title="Month"/>
				 <disp:dipxColumn group="2" style="text-align:center;" sortable="true"  property="distanceRange" title="Distance"/>
				 <disp:dipxColumn   style="text-align:center;" sortable="true"  property="vehicleType" title="Cab"/>
				 <disp:dipxColumn style="text-align:center;" sortable="true"  property="totalTrips" expandable="true"  title="#.Trips"/>
				 <disp:dipxColumn style="text-align:center;" sortable="true" property="rate" title="Trip Cost"  />
				 <disp:dipxColumn style="text-align:center;" sortable="true"  format="{0,number,0.00}"  property="escortRate" title="Escort Cost" /> 
				 <div>
				 <disp:dipxTable id="inner" type="inner" property="trips" parentProperty="totalTrips" >
				 	<disp:dipxColumn property="trip_code" title="Trip Code" />
				 	<disp:dipxColumn property="tripDate"  title="Trip Date" />
				 	<disp:dipxColumn property="trip_time"  title="Time" />
				 	<disp:dipxColumn property="trip_log"  title="Shift" />
				 	<disp:dipxColumn property="tripRate"  title="Trip Rate" />
				 	<disp:dipxColumn property="escortRate"  title="Escort Cost" />
				 	
				 </disp:dipxTable>
				 </div>
				  <disp:DispxGroupSummaryRow groupProperty="monthName" id="grp">
				  
				 	<td></td>
				 	<td></td>
				 	 <td></td>
				 	 <disp:dipxColumn style="text-align:center; font-weight:bold;"  property="totalTrips" />
				 	 <disp:dipxColumn style="text-align:center; font-weight:bold;"  property="rate"  />
				 	 <disp:dipxColumn style="text-align:center; font-weight:bold;"  property="escortRate" format="{0,number,0.00}" />
				 </disp:DispxGroupSummaryRow>
				 <disp:DispxSummaryRow style="background-color:#fff;" >
				  
				 	<td colspan="3"><center>Grand Total </center></td>
				 	  
				 	 <disp:dipxColumn style="text-align:center; font-weight:bold;"  property="totalTrips" />
				 	 <disp:dipxColumn style="text-align:center; font-weight:bold;"  property="rate"  />
				 	 <disp:dipxColumn style="text-align:center; font-weight:bold;"  property="escortRate"  format="{0,number,0.00}"  />
				 	  
				 </disp:DispxSummaryRow>
				  
			</disp:dipxTable>
			
			 	  
		
		<%} else { 
			System.out.println(" dto list is null ");
		}
		%>	
		</div>
				<%@include file="Footer.jsp"%>
		</div>
	</div>

</body>
</html>
