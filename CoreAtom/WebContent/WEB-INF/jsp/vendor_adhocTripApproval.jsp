<%-- 
    Document   : routeSample
    Created on : Oct 25, 2012, 1:05:50 PM
    Author     : muhammad
--%>

<%@page import="com.itextpdf.text.log.SysoLogger"%>
<%@page import="com.agiledge.atom.constants.SettingsConstant"%>
<%@page import="com.agiledge.atom.dto.TripDetailsChildDto"%>
<%@page import="com.agiledge.atom.dto.TripDetailsDto"%>
<%@page import="com.agiledge.atom.service.AdhocRoutingService"%>
<%@page import="com.agiledge.atom.dto.AdhocDto"%>
<%@page import="com.agiledge.atom.dto.LogTimeDto"%>
<%@page import="com.agiledge.atom.dao.LogTimeDao"%>
<%@page import="com.agiledge.atom.dao.VehicleTypeDao"%>
<%@page import="com.agiledge.atom.dto.VehicleTypeDto"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.agiledge.atom.dto.SiteDto"%>
<%@page import="com.agiledge.atom.dao.SiteDao"%>
<%@page import="java.util.List"%>
<%@page import="com.agiledge.atom.dao.OtherDao"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.nfl.com" prefix="disp" %>
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
        </script>
<script type="text/javascript">
			function assignVendor() {
				try { 
				var siteId= $("#siteId").val();
				var date = $("#date").val();
				 
				var goUrl=encodeURI(  "adhocVendorAssign.do?siteId="+siteId+"&tripDate="+ date);
			 
			location.href=goUrl;
				}catch (e) {
					// TODO: handle exception
					alert(e);
				}
				 
				 
				 
			}
            function validate()
            {
            	//var siteId=document.getElementById("siteId").value;
                var date=document.getElementById("date").value;   
                if(date.length<1)
                {
                    alert("Choose Date");                  
                    return false;
                }
            }
            
        </script>


</head>
<body>
	<%@include file="/../../Header.jsp"%>
	<div id="body">
		<div class="content">
			 
			 
			<h3>Adhoc Trip Sheet</h3>
			<hr />
			<form:form action="vendorAdhocTripApproval.do" method="get" commandName="tripDetailsDto">
				<table>
					<tr>
						<td align="right">Choose Site</td>
						<td>
						 	<form:select path="siteId"  id="siteId" itemValue="id" itemLabel="name" items="${sites }" >
									<form:option value="">--Select the Site</form:option>
									
								</form:select> 
						</td>



						<td align="right">Date</td>
						<td>                     
                            <form:input path="trip_date" id="date" type="text" size="6"
                            cssClass="{showOtherMonths: true, firstDay: 1, dateFormat: 'yyyy-mm-dd',
                                                 minDate: new Date(2008, 12 - 1, 25)}" />                     
                         </td>



						<td><input type="submit" class="formbutton" value="Submit" /></td>
					</tr>


				</table>
			</form:form>
			<hr />
			<br/>

	 
	 
			<c:if test="${tripForm.tripList ne null and fn:length( tripForm.tripList ) gt 0 }">
	  
	 
			<c:set var="notSavedCount" value="0"/>
			<c:set var="savedCount" value="0"/>
			<form:form   action="vendorAdhocTripApproval.do" method="post" commandName="tripForm" >
				<table border="1" width="80%">
  
			<form:hidden path="siteId"/>
			<form:hidden path="trip_date"/>
			<c:forEach var ="item"  items="${tripForm.tripList}" varStatus="itemStatus" >
					<tr>
	 					<th>&nbsp;
						<c:out value="${itemStatus.index }"/>
						</th>
						<th>Vehicle type</th>
						<th><c:out value="${item.vehicle_type }"/>
					 
						<input type="hidden" value="${item.vehicleTypeId }" name="vehicleType${tripForm.tripList[itemStatus.index].id }"/></th>
						<c:out value="${ tripForm.tripList[itemStatus.index].selected}"/>
						 
						<th>Date</th>
						<th> 
							<c:out value="${item.trip_date }"/>
						</th>						
							<th>Escort Trip</th>
						<th colspan="3"> 
							<c:out value="${item.isSecurity }"/>
						</th>
						<th>
					 
						<c:if test="${ fn:toLowerCase(item.approvalStatus) eq fn:toLowerCase('Open') }">
					 				
							<c:set value="${notSavedCount + 1 }" var="notSavedCount"></c:set>
							 	
							<%--  <input type="checkbox" name="tripids"
								id="tripidcheck" value="${item.id} }"/> --%> 
							<form:checkbox path="tripList[${itemStatus.index}].selected" id="tripidcheck" />
						  
							
					 	</c:if>
							</th>
							
					</tr>

					<tr>
						<th>&nbsp;</th>
						<th>Vehicle #</th>
						<th>&nbsp;</th>
						<th>Source</th>
						<th> 
							<c:out value="${item.startPlace }" />
						</th>
						<th>Destination</th>
						<th>	<c:out value="${item.endPlace }" />
						</th>
						
					</tr>
					
					<tr>
						<th>&nbsp;</th>
						<th>Status</th>
						<th><c:out value="${item.status }" />
						</th>
						<th>Start Time</th>
						<th><c:out value="${item.startTime }" />
						</th>
						<th>End Time</th>
						<th><c:out value="${item.stopTime }" /> 
						</th>						
					</tr>
				
					<tr>
						<th>#</th>
						<th>Name</th>
						<th colspan="5"></th>

					</tr>
				<%-- 	<%
						int i = 1;
					TripDetailsDto tripDetailsDtoObj =  (TripDetailsDto )pageContext.getAttribute("item");
								for (TripDetailsChildDto tripDetailsChildDto : tripDetailsDtoObj
										.getTripDetailsChildDtoList()) {
					%> --%>
					<c:forEach var="tripChild" items="${item.tripDetailsChildDtoList}" varStatus="childLoop" >
					<tr>
						<th><c:out value="${childLoop.index + 1}"/> </th>
						<td> <c:out value="${tripChild.employeeName}" /> 
						</td>
						<td colspan="5"></td>						
					</tr>
					</c:forEach>
<%-- 					<%
						i++;
								}
					%>

					 --%>
	 
	 
			</c:forEach>
 					</tbody>
				</table>
				 
				<c:if test="${notSavedCount > 0 }" >		
				<p>
					<input type="submit" class="formbutton" value="Approve" />
				</p>
				
				</c:if> 
				 
			</form:form>
		</c:if>
	 		<%@include file="/../../Footer.jsp"%>
		</div>
	</div>
</body>
</html>
