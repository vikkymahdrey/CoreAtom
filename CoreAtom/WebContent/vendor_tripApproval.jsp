
<%@page import="com.agiledge.atom.transporttype.dto.TransportTypeConfig"%>
<%@page import="com.agiledge.atom.billingtype.dto.BillingTypeConstant"%>
<%@page import="com.agiledge.atom.billingtype.service.BillingTypeService"%>
<%@page import="com.agiledge.atom.dto.TripCommentDto"%>
<%@page import="com.agiledge.atom.service.TripDetailsService"%>
<%@page import="com.itextpdf.text.log.SysoLogger"%>
<%@page import="com.agiledge.atom.dto.TripDetailsChildDto"%>
<%@page import="com.agiledge.atom.dao.TripDetailsDao"%>
<%@page import="com.agiledge.atom.dto.TripDetailsDto"%>
<%@page import="java.sql.*"%>
<%@page import="com.agiledge.atom.dao.OtherDao"%>
<%@page import="java.util.*"%>
<%@page import="java.util.Date"%>
<%@page import="com.agiledge.atom.commons.OtherFunctions"%>
<%@page import="java.text.*"%>
<%@page import="com.agiledge.atom.constants.AuditLogConstants"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Trip Sheet</title>
<script type="text/javascript" src="js/jquery-latest.js"></script>
<script type="text/javascript" src="js/validate.js"></script>

<script src="js/JavaScriptUtil.js"></script>
<script src="js/Parsers.js"></script>
<script src="js/InputMask.js"></script>


<script type="text/javascript">
	$(document).ready(function() {

		$("input[name=approve]").click(approved_checked);
		 
		$(".checkTrip").change(function() {
			validateCheckBox($(this));
			//validateCheckBox($(this));
		});
		$(".escort").change(function(){
			changeEscort($(this));
		});
		$(".escortClock").change(

				{
					method : required,
					errorMessage : "Should not be blank"
				}, validateElement);
		$(".vehicleNo").change(

		{
			method : required,
			errorMessage : "Should not be blank"
		}, validateElement);
		
		$(".driverContact").change(

				{
					method : required,
					errorMessage : "Should not be blank"
		}, validateElement);
		maskVechicleNo();

		 
	});

	function maskVechicleNo() {

		$(".vehicleNo").each(
				function() {
					var name = $(this).attr("name");
					new InputMask([ fieldBuilder.upperLetters(1, 4),
							fieldBuilder.literal(" "),
							fieldBuilder.inputNumbers(1, 2),							
							fieldBuilder.literal(" "),
							fieldBuilder.literal(""),
							fieldBuilder.upperLetters(1, 3),
 
							fieldBuilder.literal(" "),
							fieldBuilder.inputNumbers(1, 4)], name);
				});

	}

	function approved_checked() {

		if ($(this).is(":checked")) {
			var val = $(this).val();

			$("#disapprove-" + val).removeAttr("checked");
			$("#" + $(this).attr("class")).attr("checked", ":checked");
			$("#" + $(this).attr("class")).addClass("validate");
			$("#vehiclNo-" + $(this).attr("class")).addClass("required");
			$("#vehiclNo-" + $(this).attr("class")).addClass("validate");
			//$("#reason-"+ $(this).attr("class")).val("");
			$(this).parent().next().children(".reason").hide();

		} else {
			var val = $(this).val();
			$("#disapprove-" + val).attr("checked", ":checked");


			$(this).parent().next().children(".reason").show();
		}

		$("#" + $(this).attr("class")).trigger('change');
	}

	function searchForm() {
		//alert('hai ');
		document.vendorTripSheetForm.action = "vendor_tripApproval.jsp";
		document.vendorTripSheetForm.submit();
		return false;
	}
	
	function download()
	{
		
		document.vendorTripSheetForm.action = "trackedTripDownloads.jsp";
		document.vendorTripSheetForm.target="_blank";
		document.vendorTripSheetForm.submit();
		return false;
	}
	
	function loadForm()
	{
		document.vendorTripSheetForm.action = "vendor_tripApproval.jsp";
		document.vendorTripSheetForm.submit();
		return false;
	}

	function goToApproval() {
		flag = false;
		if (Validate()) {
			if (confirm("Once you move the trips to billing you will not be allowed to track those trips once  transport admin approves the trips.\n\t Press OK to Proceed")) {
				document.getElementById("isApproval").value = "true";
				flag = true;
			}
		}
		//document.vendorTripSheetForm.action="ApproveTrips";
		return flag;

	}

	// validation 
	//-------------------
	function formValidate() {
		try {

			$(".validate").each(function() {

				$(this).trigger('blur');
				$(this).trigger('change');
			});

		} catch (e) {
			alert(e);
		}

	}

	function Validate() {

		var flag = true;
		formValidate();
		 
		try {

			if ($(".validate").hasClass("valid")) {
				if ($(".validate").hasClass("error")) {
					alert("Validation Error");
					flag = false;
				} else {
					flag = true;
				}

			} else if ($(".validate").hasClass("error")) {
				alert("Validation Error");
				flag = false;
			}

		} catch (e) {
			alert(e);
		}

		return flag;
		//return false;

	}

	function changeEscort(me)
	{
		//whether the trip is selected or not
		var id=$(me).attr("id").split("-")[1];
		
		if($("#"+id).is(":checked"))
			{
		if($("#escort-"+id).val()=="YES")
		{
		$("#escortRow-"+id).show();
		
		$("#escortClock-"+id ).addClass("required");
		$("#escortClock-"+id ).addClass("validate");
		}
	else{
		$("#escortClock-"+id ).removeClass("required");
		$("#escortClock-"+id).removeClass("validate");
		$("#escortClock-"+id).removeClass("error");
		$("#escortClock-"+id+"_errorImage").hide();
		$("#escortRow-"+id).hide();
	}
			}
	}
	
	function validateCheckBox(me) {
		if ($(me).is(":checked") == true) {
			$($(me)).addClass("validate");
			$("#vehiclNo-" + $(me).attr("id")).addClass("required");
			$("#vehiclNo-" + $(me).attr("id")).addClass("validate");
			 
			if($("#escort-"+$(me).attr("id") ).val()=="YES")
				{
				$("#escortClock-"+$(me).attr("id") ).addClass("required");
				$("#escortClock-"+$(me).attr("id") ).addClass("validate");
				$("#escortClock-"+$(me).attr("id") ).parent().parent().show();
				
				}
			else{
				$("#escortClock-"+$(me).attr("id") ).removeClass("required");
				$("#escortClock-"+$(me).attr("id") ).removeClass("validate");
				$("#escortClock-"+$(me).attr("id") ).removeClass("error");
				$("#escortClock-"+$(me).attr("id") ).parent().parent().hide();
			}

			var flag = false;
			if ($(me).hasClass("validate")) {
				var id = $(me).attr("id");
				var element = "#" + id + "_errorImage";
				//alert("" + id);
				if ($(me).is(":checked") == false) {
					$(element).attr("title", " Please check");
					$(me).removeClass("valid");
					$(me).addClass("error");
					$("#" + id + "_errorImage").show();
				} else {
					$(me).removeClass("error");
					$(me).addClass("valid");
					$("#" + id + "_errorImage").hide();
					flag = true;

				}
			}
		} else {
			$(me).removeClass("validate");
			$(me).removeClass("error");
			$($(me).attr("id") + "_errorImage").hide();

			$("#vehiclNo-" + $(me).attr("id")).removeClass("required");
			$("#vehiclNo-" + $(me).attr("id")).removeClass("validate");
			$("#vehiclNo-" + $(me).attr("id")).removeClass("error");
			$("#vehiclNo-" + $(me).attr("id") + "_errorImage").hide();
			$("#escortClock-"+$(me).attr("id") ).removeClass("required");
			$("#escortClock-"+$(me).attr("id") ).removeClass("validate");
			$("#escortClock-"+$(me).attr("id") ).removeClass("error");
		}
		return flag;
	}
	function showAuditLog(relatedId,moduleName){
		var params = "toolbars=no,menubar=no,location=no,scrollbars=yes,resizable=yes";
		var size = "height=450,width=900,top=200,left=300," + params;
		var url="ShowAuditLog.jsp?relatedNodeId="+relatedId+"&moduleName="+moduleName;	
	    newwindow = window.open(url, 'AuditLog', size);

		if (window.focus) {
			newwindow.focus();
		}
	}


	//----------------
	
	function showComment(  tripId)
	{
		
		$("#commentPanel-"+tripId).show();
	}
	function closeCommentPanel( )
	{
		
		try{
		$(".commentPanel").hide();
		 
		}catch (e) {
			alert("message "+e);
		}
	}
	
	function msgfunction()
	{
		$("input[name=msgfield]").val("true");
		var status=$("input[name=msgfield]").val();
		if(status=="true")
			{
			$("form[name=vendorTripSheetForm]").submit();
			}
	}
</script>
</head>
<body>

	<%try{ 
		 
		String tripDate = OtherFunctions.changeDateFromatToIso(request
				.getParameter("tripDate"));
		String siteId = request.getParameter("siteId");
		String tripMode = "" + request.getParameter("tripMode");
	 
		String employeePersonnelNo = request.getParameter("personnelNo");
		String vendor = request.getParameter("vendor");
		String [] tripTimes= request.getParameterValues("tripTime");
		if(tripTimes== null)
		{
			String[] array = (String[]) request.getAttribute("tripTimes");
			tripTimes=array;

		}
		
		String approvalStatusParam=request.getParameter("approvalStatusParam");
		
		approvalStatusParam=approvalStatusParam==null||approvalStatusParam.equals("")?"All":approvalStatusParam;
		
		if(tripTimes!=null&&tripTimes.length>0) {
			for(String triptime: tripTimes)
			{
			System.out.println("Trip time :"+ triptime);
			}
		}
		
		//ArrayList<TripDetailsDto> tripSheetListActual = new TripDetailsDao().getTripSheetActual(tripDate, tripMode, siteId, tripTime);
		/*  ArrayList<TripDetailsDto> tripSheetList = null;
		 ArrayList<TripDetailsDto> tripSheetSaved =null;
		 tripSheetList = new TripDetailsDao().getTripSheetModified(tripDate, tripMode, siteId, tripTime);
		 tripSheetSaved = new TripDetailsDao().getTripSheetSaved(tripDate, tripMode, siteId, tripTime);
		 if((vendor!=null&&!vendor.equals(""))|| (employeePersonnelNo!=null&&!employeePersonnelNo.equals("")))
		 {
		 	
		 	if(tripSheetList!=null&&tripSheetList.size()>0)
		 	{
		 		 
		 		tripSheetList = new TripDetailsDao().getTripSheetModified_Vendor(tripDate, tripMode, siteId, tripTime,  employeePersonnelNo, vendor);
		 	}
		 	if(tripSheetSaved!=null&&tripSheetSaved.size()>0)
		 	{
		 		 
		 	    	tripSheetSaved = new TripDetailsDao().getTripSheetSaved_Vendor(tripDate, tripMode, siteId, tripTime,  employeePersonnelNo, vendor);
		 	}
		 } */
		
	%>
	<%
	
	boolean flag = false;
		long empid = 0;
		String employeeId = OtherFunctions.checkUser(session);
					empid = Long.parseLong(employeeId);
	%>
	<%@include file="Header.jsp"%>

	<div id="body">
		<div class="content">
			<hr />


			<form method="POST" name="vendorTripSheetForm"
				action="TrackTripSheet" onsubmit="return Validate()">
				<div>
					 <table style="width: 50%; border: 0px none;">
					 
					 	<tr>
							<td>Status</td>
							<td>
								<select name="approvalStatusParam" onchange="return loadForm();" >
									
									<% 
									String statusSelect="";
									String statusLabels[]={"All","Open","Rejected by Transport Co-ordinator","Sent for TC approval","Rejected by Transport Manager","Sent for TM approval","Approved by Transport Manager"};
									String statusVals[]={"All","Open","Rejected by Transport Co-ordinator","Sent for TC approval","Rejected by Transport Manager","Sent for TM approval","Approved by Transport Manager"};
									try{
										int i=0;
										for(String value:statusVals)
										{
											if(approvalStatusParam.equals(value))
											{
												statusSelect="selected";
											}else
											{
												statusSelect="";
											}
											
									%>
										<option  <%=statusSelect%> value="<%=value %>" ><%=statusLabels[i]%></option>
									<%
										i++;
										}
									}catch(Exception e)
									{ 
										System.out.println("Erro "+ e);
									}
										/* ArrayList<TripDetailsDto> tripSheetList = new TripDetailsService()
										.getEmployeeTripSheetForVendorTracking(tripDate, tripMode,
												siteId, tripTimes, employeePersonnelNo, vendor); */
										String [] status= new String[1];
												status[0]=approvalStatusParam;
												System.out.println("Status : "+approvalStatusParam);
												if(approvalStatusParam.trim().equals("All"))
												{
													status= new String[6]; 
												    status[0]="";
													status[1]="Rejected by Transport Co-ordinator";
													status[2]="Sent for TC approval";
													status[3]= "Rejected by Transport Manager";
													status[4]="Sent for TM approval";
													status[5]="Approved by Transport Manager";
												}
										ArrayList<TripDetailsDto> tripSheetList = new TripDetailsService().getTrackedTripSheet(tripDate, tripMode, siteId, tripTimes,
												employeePersonnelNo, vendor, status);
										 System.out.println("Size : "+tripSheetList.size());
								ArrayList<TripDetailsDto> tripSheetSaved = tripSheetList;
									%>
								</select>
								
							</td>
							<td>
								
								<label type="button"  onclick="return download();">
								<a >Download</a>
								</label>
							</td>
						</tr>
					 	
						<!--<tr>
							<td>Employee ID</td>
							<td><input type="text" name="personnelNo" /></td>
						</tr>

						<tr>
							<td></td>
							<td><input type="button" value="Search" 
								onclick="return searchForm();" class="formbutton" />
								 <input type="Button"
								name="add" class="formbutton" value="Back"  style="margin-left: 2%"
								onclick="javascript:history.go(-1);" />
								</td>

						</tr>-->
					</table> 

				</div>

				<input type="hidden" name="tripDate" id="tripDate"
					value="<%=tripDate%>" /> <input type="hidden" name="siteId"
					id="siteId" value="<%=siteId%>" /> <input type="hidden"
					name="tripMode" id="tripMode" value="<%=tripMode%>" />
					
					<% for(String tripTime: tripTimes) 
					{%> <input
					type="hidden" name="tripTime"   value="<%=tripTime%>" />
				<%
					}
					String disable = "";
					boolean totalDisable = true;
					if (tripSheetSaved != null && tripSheetSaved.size() > 0) {

						flag = true;
				%>
				<!-- From here modified trip sheet starts -->

				<h3>Trip Sheet</h3>

				<%
					for (TripDetailsDto tripDetailsDtoObj : tripSheetSaved) {
						
				%>
				<table border="1" style="width: 90%;">
					<tr>
					</tr>
					<tr>
						<td colspan="8">
						<%
						
						if(tripDetailsDtoObj.getComments()!=null&&tripDetailsDtoObj.getComments().size()>0){ %>
						<div class="commentPanel" id="commentPanel-<%=tripDetailsDtoObj.getId() %>"  >
						<div class="commentPanelTitle" >
							<p style="float:left; margin-top: 0px; margin-left: 30%;" >Comments</p><label style="float:right;"> <a  onclick="closeCommentPanel()" >Close</a>  </label>
						</div>

						<% 
						for(TripCommentDto tripCommentDto: tripDetailsDtoObj.getComments())
						{
							
						%>
						<div class="commentPanelContent" >
						 
					     <div class="commentPanelContentCommentTitle"  >
						
						<label style=" position:relative; float:left; font: bold; "> <%=tripCommentDto.getCommentedByName() %></label>
						<label style="position:relative; float: right; margin-right: 2px; font: italic; " ><%=tripCommentDto.getCommentedDate() %></label>
						</div>
						
						
						<%=tripCommentDto.getComment() %>
						<br/>
						</div>
						<%} %>
						
						</div>
						<%} %>
							<table style="background-color: #E8E8E6;">
								<tr>
									<td width="20%">
										<%
											String tripSelect = " ";

													if (tripDetailsDtoObj.getActualLogTime() != null
															&& !tripDetailsDtoObj.getActualLogTime().equals("")&&
															!tripDetailsDtoObj.getApprovalStatus()
															.equals("Rejected by Transport Co-ordinator")) {
														tripSelect = "checked";

													}
													if (tripDetailsDtoObj.getApprovalStatus() == null
															|| (tripDetailsDtoObj.getApprovalStatus() != null && (tripDetailsDtoObj
																	.getApprovalStatus().equals("")
																	|| tripDetailsDtoObj.getApprovalStatus()
																			.equals("Tracked")
																	|| tripDetailsDtoObj.getApprovalStatus()
																			.equals("Open") || tripDetailsDtoObj
																	.getApprovalStatus()
																	.equals("Rejected by Transport Co-ordinator"))))

													{
														disable = "";
													} else {
														disable = "disabled";
													}
										%> <label><input class="checkTrip "
											name="selectedTrip"   value="<%=tripDetailsDtoObj.getId()%>"
											<%=disable%> type="checkbox" <%=tripSelect%>
											id="<%=tripDetailsDtoObj.getId()%>" />&nbsp;&nbsp;Select
											Trip</label> <img id="<%=tripDetailsDtoObj.getId()%>_errorImage"
										src="images/error.png" style="display: none;" />
									</td>
									<td width="20%">Vehicle type</td>
									<td width="20%"><%=tripDetailsDtoObj.getVehicle_type()%></td>
									<td width="20%">Trip ID</td>
									<td width="20%"><%=tripDetailsDtoObj.getTrip_code()%></td>
									
								</tr>



								<tr>
									<td>&nbsp;</td>
									<td>IN/OUT Time</td>
									<td><%=tripDetailsDtoObj.getTrip_log() + "  "
							+ tripDetailsDtoObj.getTrip_time()%></td>
									<td>Actual IN/OUT time</td>
									<td>
										<%
											String actualLogTime = "";

													if (tripDetailsDtoObj.getActualLogTime() != null
															&& !tripDetailsDtoObj.getActualLogTime().equals("")) {
														actualLogTime = tripDetailsDtoObj.getActualLogTime();

													} else {
														actualLogTime = tripDetailsDtoObj.getTrip_time();
													}
													String vehicleNo = "";
													if (tripDetailsDtoObj.getVehicleNo() != null
															&& !tripDetailsDtoObj.getVehicleNo().equals("")) {
														vehicleNo = tripDetailsDtoObj.getVehicleNo();

													}
													disable = "disabled";
													String textdisable = "readOnly";

													if (tripDetailsDtoObj.getApprovalStatus() == null
															|| (tripDetailsDtoObj.getApprovalStatus() != null && (tripDetailsDtoObj
																	.getApprovalStatus().equals("")
																	|| tripDetailsDtoObj.getApprovalStatus()
																			.equals("Tracked")
																	|| tripDetailsDtoObj.getApprovalStatus()
																			.equals("Open") || tripDetailsDtoObj
																	.getApprovalStatus()
																	.equals("Rejected by Transport Co-ordinator"))))

													{

														tripDetailsDtoObj.setActualLogTime("Tracked");
														disable = "";
														textdisable = "";
														totalDisable = false;
													}
													int hour = 0;
										%> <select class="hour" <%=disable%>
										name="hour-<%=tripDetailsDtoObj.getId()%>">
											<%
												String h = "";
														String m = "";
														boolean tHourFlag = false;
														boolean tMinuteFlag = false;
														boolean dHourFlag = false;
														boolean dMinuteFlag = false;
														System.out.println(":::_________ "+tripDetailsDtoObj.getTrip_time());
														String defaultTimeHour = tripDetailsDtoObj.getTrip_time()
																.trim().split(":")[0];
														String defaultTimeMinute = tripDetailsDtoObj.getTrip_time()
																.trim().split(":")[1];
														String defaultTimeOption = "";
														System.out.println(":::_________ ");
														try {
															h = actualLogTime.split(":")[0];
															m = actualLogTime.split(":")[1];

														} catch (ArrayIndexOutOfBoundsException e) {
															;
														}
														
														String optionSelect = "";
														for (; hour <= 23; hour++) {
															
															String hourString = (hour < 10 ? "0" + hour : "" + hour);
															optionSelect = "";
															if (h.trim().equals(hourString) && tHourFlag == false) {

																optionSelect = "selected";
																tHourFlag = true;

															}
															/* if(tHourFlag=false&&dHourFlag==flag&&defaultTimeHour.equals(hourString))
															{
																defaultTimeOption="selected"
															} */
	%>
											<option value="<%=hourString%>" <%=optionSelect%>><%=hourString%></option>

											<%
												}
											%>
									</select> <select class="minute" <%=disable%>
										name="minute-<%=tripDetailsDtoObj.getId()%>">
											<%
												optionSelect = "";
														for (hour = 0; hour <= 59; hour++) {
															String hourString = (hour < 10 ? "0" + hour : "" + hour);
															optionSelect = "";
															if (m.trim().equals(hourString)) {
																optionSelect = "selected";
															}
											%>
											<option value="<%=hourString%>" <%=optionSelect%>><%=hourString%></option>

											<%
												}
											%>
									</select> <input type="hidden" class="actualLogTime" <%=textdisable%>
										value="<%=tripDetailsDtoObj.getTrip_time()%>"
										name="actualLogTime-<%=tripDetailsDtoObj.getId()%>" /> <input
										type="hidden"
										name="actualTripLog-<%=tripDetailsDtoObj.getId()%>"
										value="<%=tripDetailsDtoObj.getTrip_log()%>" />
										<%if(tripDetailsDtoObj.getTrackingStatus().equalsIgnoreCase("initial")) { %>
										
										<br/><div  style="padding:5px; color: red; height: 70%; display: none;" ><label  ><input type="checkbox" name="forceStop-<%=tripDetailsDtoObj.getId() %>" />&nbsp;Force Stop</label></div>  
										<% } %>				

									</td>
								</tr>
								<tr>
								  <td></td>
								  <td></td>
								 	<td></td>
									<td>Escort Trip :</td>
									<td><select class="escort"
										id="escort-<%=tripDetailsDtoObj.getId()%>" <%=disable%>
										name="escort-<%=tripDetailsDtoObj.getId()%>">
											<%
									
									String escort[]={"YES","NO"};
									String escortSelect="";
									String escortValue=null;
									if( tripDetailsDtoObj .getEscort()!=null&&tripDetailsDtoObj .getEscort().equals("")!=true)
									{
										
										escortValue=tripDetailsDtoObj.getEscort();
									}else if(tripDetailsDtoObj .getIsSecurity()!=null&&tripDetailsDtoObj .getIsSecurity().equals("")!=true)
									{
										escortValue=tripDetailsDtoObj .getIsSecurity();
										
									}
									
									for(String es: escort)
									{
										if(escortValue!=null)
										{
											escortSelect="";
										
											if(es.equals(escortValue))
											{
												escortSelect="selected";
											}
											 
												
											 
										}
										
										%>
											<option <%=escortSelect %> value="<%=es %>"><%=es %></option>
											<%
									}
									%>


									</select></td>
								</tr>
								<%
								String escortRowStyle="  "; 
								if(tripDetailsDtoObj.getEscort()==null||tripDetailsDtoObj.getEscort().equals("NO"))
								{
									
									
									escortRowStyle=" style='display:none;'";
								}
								%>
								<tr id="escortRow-<%=tripDetailsDtoObj.getId() %>"
									<%=escortRowStyle %>>
									 
									 <td></td>
									 <td></td>
									<td></td>
									<td>Escort Clock :</td>
									<td>
										<%
										if(tripDetailsDtoObj.getEscortNo() ==null)
										{
											tripDetailsDtoObj.setEscortNo(""); 
										}
									%> <input type="text" class="escortClock" <%=textdisable%>
										name="escortClock-<%=tripDetailsDtoObj.getId()%>"
										id="escortClock-<%=tripDetailsDtoObj.getId()%>"
										value="<%=tripDetailsDtoObj.getEscortNo() %>" /> <img
										id="escortClock-<%=tripDetailsDtoObj.getId()%>_errorImage"
										src="images/error.png" style="display: none;" />
									</td>
								</tr>

								<tr>
									 <% String []copyStatus=Arrays.copyOf(status, status.length); 
									 	Arrays.sort(status);
									 	int colorIndex=Arrays.binarySearch(status,tripDetailsDtoObj.getApprovalStatus());
									 	String colors[]={"","#1be050","#c6e01b","#e0bc1b","#e07e1b","#e01e1b","black"};
									  String validationClass="";
									  if(tripSelect.equalsIgnoreCase("checked")) {
										  validationClass="validate";
									  }
									 %>
									
									 <td   colspan="2"  align="left">Approval Status :<i ><%=tripDetailsDtoObj.getApprovalStatus()%></i></td>
									
									<td>Tracking Status :<i><%=tripDetailsDtoObj.getTrackingStatus().toUpperCase() %></i></td>
									<td width="20%">Vehicle No</td>
									<td width="20%"><input type="text"
										class="vehicleNo" <%=textdisable%>
										value="<%=vehicleNo%>"
										id="vehiclNo-<%=tripDetailsDtoObj.getId()%>"
										name="vehiclNo-<%=tripDetailsDtoObj.getId()%>" /> <img
										id="vehiclNo-<%=tripDetailsDtoObj.getId()%>_errorImage"
										src="images/error.png" style="display: none;" />
										</td>
										</tr> 
										<tr>
										<td   colspan="2"  align="left">Approval Status :<i ><%=tripDetailsDtoObj.getApprovalStatus()%></i></td>
										<td></td>
 										<td width="20%"  >Driver Contact</td>
 										<td width="20%"><input type="text" class="vehicleNo" maxlength="10" id="driverContact-<%=tripDetailsDtoObj.getId()%>" name="driverContact-<%=tripDetailsDtoObj.getId()%>" value="<%=tripDetailsDtoObj.getDriverContact() %>" />
								</tr>
							
								<tr> 	<td colspan="2"  >Map Distance
										</td>
										<td><%=tripDetailsDtoObj.getDistance() %></td>
											<%
								ArrayList<String > str = new BillingTypeService().getCurrentBillingTypeMappingsKeys("1", "1", String.valueOf(TransportTypeConfig.GENERAL_SHIFT ));
								if(str!=null&& str.size()>0 && str.contains(BillingTypeConstant.KM_BASED_CLASSIC_BILLING_TYPESTRING)) {
								%>
										<td>Distance </td>
										<td> <input type="text" name="tripBasedDistance-<%=tripDetailsDtoObj.getId() %>" value="<%=tripDetailsDtoObj.getTripBasedDistance() %>" /> 
										</td>
										<%} else { %>
										<td>Distance </td>
										<td> <input type="text" disabled="disabled" name="tripBasedDistance-<%=tripDetailsDtoObj.getId() %>" value = "<%=tripDetailsDtoObj.getDistance() %>" /> 
										</td>										
										<%} %>
								</tr>
										<%
								 
 	String comment = "";
 			if (tripDetailsDtoObj.getComment() != null
 					&& !tripDetailsDtoObj.getComment().trim()
 							.equals("")) {
 				comment = tripDetailsDtoObj.getComment();

 			}
 %> <input type="hidden" name="comment" value="<%=comment%>" />


 
 </td>
 </tr>

					</table>
						</td>
						<td align="center">
							 <%if(tripDetailsDtoObj.getComments()!=null){ %>
									<a class="showComment" style="font:  large;" onclick="showComment(<%=tripDetailsDtoObj.getId() %>)" >Comments</a>
										<% } %>
						</td>
					</tr>
					<tr>
						<th width="2%">#</th>
						<th width="14%">Name</th>
						<th width="6%">Staff#</th>
						<th width="10%">Area</th>
						<th width="15%">Place</th>
						<th width="20%">landmark</th>
						<th width="8%">Contact No</th>
						<th width="5%">Present</th>
						<th width="15%">Reason For No-Show</th>
					</tr>
					<%
						int i = 1;
								for (TripDetailsChildDto tripDetailsChildDto : tripDetailsDtoObj
										.getTripDetailsChildDtoList()) {
					%>
					<tr>
						<td><%=i%></td>
						<td><%=tripDetailsChildDto.getEmployeeName()%></td>
						<td><%=tripDetailsChildDto.getPersonnelNo()%></td>
						<td><%=tripDetailsChildDto.getArea()%></td>
						<td><%=tripDetailsChildDto.getPlace()%></td>
						<td><%=tripDetailsChildDto.getLandmark()%></td>
						<td><%=tripDetailsChildDto.getContactNumber()%></td>
						<td>
							<%
								String checked = "";
											String disapproved = "checked";
											if (tripDetailsChildDto.getShowStatus() != null
													&& tripDetailsChildDto.getShowStatus().equals(
															"Show")) {
												checked = "checked";
												disapproved = "";
											}
											String type = "";
											if (tripDetailsDtoObj.getTrip_code().contains("P")) {
												type = "P";
											} else {
												type = "D";
											}
							%> <input type="hidden"
							name="employeeId-<%=tripDetailsChildDto.getEmployeeId() + "-"
								+ type%>"
							value="<%=tripDetailsChildDto.getEmployeeId()%>" /> <input
							type="checkbox" <%=disable%> <%=checked%> name="approve"
							value="<%=tripDetailsChildDto.getEmployeeId() + "-"
								+ type%>"
							class="<%=tripDetailsDtoObj.getId()%>" /> <input type="hidden"
							id="type" name="type" value="<%=type%>" /> <input type="hidden"
							name="tripId-<%=tripDetailsChildDto.getEmployeeId() + "-"
								+ type%>"
							value="<%=tripDetailsDtoObj.getId()%>" /> <input type="checkbox"
							<%=disable%> <%=disapproved%> style="display: none;"
							id="disapprove-<%=tripDetailsChildDto.getEmployeeId() + "-"
								+ type%>"
							name="disapprove"
							value="<%=tripDetailsChildDto.getEmployeeId() + "-"
								+ type%>" />
						</td>
						<td>
							<%
								String reasonDisplay = "";
											String reasonSelect = "";
											if (checked == "checked") {
												reasonDisplay = "style='display:none;'";
											}
											String reasons[] = {  "No Response",
													"Not Coming", "Vacation", "Delay Pick-Up","Traffic Delay", "Weekly Off", "Unwell", "Mobile Not Reachable" };
							%> <select
							name="reason-<%=tripDetailsChildDto.getEmployeeId() + "-"
								+ type%>"
							<%=reasonDisplay%> <%=disable%> class="reason">

								<%
									for (String item : reasons) {
													if (tripDetailsChildDto.getReason() != null
															&& tripDetailsChildDto.getReason().equals(
																	item)) {
														reasonSelect = "selected";
													} else {
														reasonSelect = "";
													}
								%>
								<option value="<%=item%>" <%=reasonSelect%>><%=item%></option>
								<%
									}
								%>




						</select>
						</td>



					</tr>
					<%
						i++;
								}
					%>


					</tbody>
				</table>
				<br /> <br /> <br />
				<%
					}
					}
				%>

				<%
					if (!flag) {
				%>
				<p>No trips to display</p>


				<%
					}
				%>
				<br /> <input type="hidden" name="isApproval" id="isApproval"
					value="false" />
				<%
					if (totalDisable == false) {
				%>
				<input style="margin-left: 63%" class="formbutton" type="submit"
					value="Update" />
				<%
					if ((vendor == null || vendor.equals(""))
								&& (employeePersonnelNo == null || employeePersonnelNo
										.equals(""))) {
				%>
				<input style="margin-left: 2%" class="formbutton" type="submit"
					onclick="return goToApproval();" value="Send For Approval" />
					<input style="margin-left: 1%; " type="submit" class="formbutton" value="Update & Send SMS" id="msgbutton" name="msgbutton" onClick="msgfunction()"/>
					<input type="hidden" name="msgfield" value="false"/>
					 <input type="Button"  style="margin-left: 1%"
								name="add" class="formbutton" value="Back"
								onclick="javascript:history.go(-1);" />
				<%
					}
					}
				%>
				&nbsp; <br /> <br />
				<hr />

			</form>
			<%}catch(Exception e)
			{System.out.println("Err55555555555555555555555555555or"+e);}
			%>
			<%@include file="Footer.jsp"%>
		</div>
	</div>
</body>
</html>

