<%@page import="com.agiledge.atom.accessRight.dto.AccessRightConstants"%>
<%@page import="com.agiledge.atom.accessRight.service.AccessRightService"%>
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
	/*	$(".vehicleNo").change(

		{
			method : required,
			errorMessage : "Should not be blank"
		}, validateElement);*/
		//maskVechicleNo();

	});

	function maskVechicleNo() {

		$(".vehicleNo").each(
				function() {
					var name = $(this).attr("name");
					new InputMask([ fieldBuilder.upperLetters(1, 4),
							fieldBuilder.literal(" "),
							fieldBuilder.inputNumbers(1, 2),

							fieldBuilder.literal(" "),
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

	function download()
	{
		
		document.vendorTripSheetForm.action = "trackedTripDownloads.jsp";
		document.vendorTripSheetForm.target="_blank";
		document.vendorTripSheetForm.submit();
		return false;
	}
	function loadForm()
	{
		document.vendorTripSheetForm.action = "transadmin_trackedTrips.jsp";
		document.vendorTripSheetForm.submit();
		return false;
	}
	function searchForm() {
		//alert('hai ');
		document.vendorTripSheetForm.action = "transadmin_trackedTrips.jsp";
		document.vendorTripSheetForm.submit();
		return false;
	}
	
	

	function goToApproval() {
		flag = false;
		if ($('.checkTrip:checked').length>0) {
		if (Validate()) {
             
     			if (confirm("Are You Sure To Approve Selected Trips?")) {
     				document.getElementById("isApproval").value = "true";
     				approvalUpdate();
     				document.getElementById("vendorTripSheetForm").submit();
     				flag = true;
             }
		}
		}	else
     				{
     				alert("Select Atleast One Trip!!!");
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

		if($("input:focus").attr("name")=="personnelNo")
		{	
			
			return searchForm();
		}
		else
			{
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
		
			}
		 

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
	function disapprove() {
		//	alert('.UU.');
		var user = "Vendor";
		if ($("#accessRight").val() == $("#level2A").val()) {
			user = "Transport Co-ordinator";
		}
		else if ($("#accessRight").val() == $("#level3A").val()) {
			user = "Transport Manager";
		}

		$("#approvalStatus").val("Rejected by " + user);
		//	alert($("#approvalStatus").val());
		return true;
	}

	function approvalUpdate() {
		//alert('.AA.');
		var flag=false;
		try {
			flag = false;
			if (Validate()) {
				
				var user = "Vendor";
				var statusMessage="Sent for TC approval";
				if ($("#accessRight").val() == $("#level2A").val()) {
					user = "Transport Co-ordinator";
					var statusMessage="Sent for TM approval";
				}
				else if ($("#accessRight").val() == $("#level3A").val()) {
					user = "Transport Manager";
					statusMessage="Approved by "+user;
				}								
 
			$("#approvalStatus").val(statusMessage);
			 
			flag=true;
			}
		 
		} catch (e) {
			alert(e);
		}
		 
		return flag;

	}
	function approveOne(id)
	{
		$(".checkTrip").removeAttr("checked");
		
		$("#"+id).attr("checked","checked");
		$("#"+id).trigger("change");
		return approvalUpdate();
	}
	function disapproveOne(id)
	{
		$(".checkTrip").removeAttr("checked");
		$("#"+id).attr("checked","checked");
		 
		return disapprove();
	}
	
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
	
	function showdedtr(tripid)
	{
		var name='dedtr'+tripid;
		if($('#'+name).is(':visible'))
			{
			$('#'+name).hide();
			}
		else
			{
			$('#'+name).show();
			}
	}
	
	function variationcalc(tripid,oldrate)
	{
		var actualamtid='actualcost'+tripid;
		var variationid='variation'+tripid;
		var opid='op'+tripid;
		var actualamt=$('#'+actualamtid).val();
		var variationamt=$('#'+variationid).val();
		var op=$('#'+opid).val();
		var calamt;
		if (variationamt==null||variationamt==0)
			{
			alert("Please Specify Variation Amount!");
			return;
			}
		else
			{
		if(op=='add')
			{
			calamt=parseFloat(actualamt) + parseFloat(variationamt);
			$('#'+actualamtid).val(calamt);
			$('#updatetripid').val(tripid);
			$('#oldrate').val(oldrate);
			$('#updatevariation').val(variationamt);
			$('#updateoperation').val("add");
			if(confirm("Are You Sure To Update The Amount?"))
				{
			$('#costupdate').submit();
				}
			}
		else
			{
			calamt=parseFloat(actualamt) - parseFloat(variationamt);
			if(calamt<1)
				{
				alert("The Actual Cost Can't Be Less Than One!");
				}
			else
				{
			$('#'+actualamtid).val(calamt);
			$('#'+actualamtid).val(calamt);
			$('#updatetripid').val(tripid);
			$('#oldrate').val(oldrate);
			$('#updatevariation').val(variationamt);
			$('#updateoperation').val("sub");
			if(confirm("Are You Sure To Update The Amount?"))
			{
			$('#costupdate').submit();
			}
			}
			}
			}
	}
	
	function escortcancel(tripid)
	{
		actlval=$("#escrtsts-"+tripid).val();
		if ($("#accessRight").val() == $("#level2A").val()) {
			if($("#escort-"+tripid).val()=="YES" && actlval=="NO")
				{
				$("#escort-"+tripid).val(actlval);
				alert("Operation Restricted!!");
				}
		}
		else
			{
		return;
			}
	}
	
	function selectAll()
	{
		$('.checkTrip').attr("checked","checked");
	}
</script>
</head>
<body>

	<%
		String tripDate = OtherFunctions.changeDateFromatToIso(request
				.getParameter("tripDate"));
		String siteId = request.getParameter("siteId");
		String tripMode = "" + request.getParameter("tripMode");
		String tripTime = "" + request.getParameter("tripTime");
		String multiapprove="No";
		String employeePersonnelNo = request.getParameter("personnelNo");
		String vendor = request.getParameter("vendor");
		String approvalStatusParam=request.getParameter("approvalStatusParam");
		approvalStatusParam=approvalStatusParam==null?"":approvalStatusParam;
		/* String status[] = null;
		if(approvalStatusParam.equals("")){
	 	if (request.getSession().getAttribute("role").equals("ta")) {
			status = new String[2];
			status[0] = "Approved by Transport Manager";
			status[1] = "Sent for TM approval";
		} else if (request.getSession().getAttribute("role").equals("tm")) {
			status = new String[4];
			status[0] = "Sent for TC approval";			 
			status[1] = "Rejected by Transport Manager";
			status[2] = "Sent for TM approval";
			status[3] = "Approved by Transport Manager";
		} } else
		{
			status=new String[1];
			status[0]=approvalStatusParam;
		}
		*/
		
		String []tripTimes=new String[1];
		tripTimes[0]=tripTime;
		/*
		ArrayList<TripDetailsDto> tripSheetList = new TripDetailsService()
				.getTrackedTripSheet(tripDate, tripMode, siteId, tripTimes,
						employeePersonnelNo, vendor, status);
		ArrayList<TripDetailsDto> tripSheetSaved = tripSheetList; */

		boolean involvedByCo_ordinator = false;
	%>
	<%
	boolean flag = false;
		long empid = 0;

		
		String employeeId = OtherFunctions.checkUser(session);
 
			empid = Long.parseLong(employeeId);
			
			
 
	%>
	<%@include file="Header.jsp"%>

	<div id="body"  >
		<div class="content">
			<hr />
			<form method="POST" name="vendorTripSheetForm"
				action="ApproveTrips" onsubmit="return Validate()" id="vendorTripSheetForm">
				 <input type="hidden" name="isApproval" id="isApproval"
					value="false" />
				 
				 
			 
				<input type="hidden" name="approvalStatus" id="approvalStatus"
					value="Approved by Transport Admin" />
				 
				<div>
<!-- <table style="width: 20%; border: 0px none;">
						<tr>
							<td>Employee ID</td>
							<td><input type="text" name="personnelNo" /></td>
						</tr>

						<tr>
							<td></td>
							<td><input type="submit" value="Search" 
								 class="formbutton" />
								 <input type="Button"
								name="add" class="formbutton" value="Back"  style="margin-left: 2%"
								onclick="javascript:history.go(-1);" />
								</td>

						</tr>
					</table>
 -->	
 
			<p id="top"></p>
				<a href="#top" style="position: fixed; right:50px">Top</a> &nbsp;
				
			
				<a href="#bottom" style="position: fixed; right:50px; bottom:50px ">Bottom</a> &nbsp;
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
												if(approvalStatusParam.trim().equals("All"))
												{
													status= new String[6]; 
												    status[0]="Open";
													status[1]="Rejected by Transport Co-ordinator";
													status[2]="Sent for TC approval";
													status[3]= "Rejected by Transport Manager";
													status[4]="Sent for TM approval";
													status[5]="Approved by Transport Manager";
												}
										ArrayList<TripDetailsDto> tripSheetList = new TripDetailsService().getTrackedTripSheet(tripDate, tripMode, siteId, tripTimes,
												employeePersonnelNo, vendor, status);
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
					 
				
				<div>
				
				<input type="hidden" id="userRole" value="<%=request.getSession().getAttribute("role").toString() %>"/>
				<input type="hidden" id="level1A" value="<%=AccessRightConstants.LEVEL1_APPROVER %>"/>
				<input type="hidden" id="level2A" value="<%=AccessRightConstants.LEVEL2_APPROVER %>"/>
				<input type="hidden" id="level3A" value="<%=AccessRightConstants.LEVEL3_APPROVER %>"/>
				<input type="hidden" id="accessRight" value="<%=new AccessRightService().getAccessRight(request.getSession().getAttribute("role").toString(), AccessRightConstants.TRACKING_APPROVAL_MODULE)  %>"/>	

				</div>

				<input type="hidden" name="tripDate" id="tripDate"
					value="<%=tripDate%>" /> <input type="hidden" name="siteId"
					id="siteId" value="<%=siteId%>" /> <input type="hidden"
					name="tripMode" id="tripMode" value="<%=tripMode%>" />
					
					 <input
					type="hidden" name="tripTime"   value="<%=tripTime%>" />
				<%
					 
					String disable = "";
					boolean totalDisable = true;
					if (tripSheetSaved != null && tripSheetSaved.size() > 0) {

						flag = true;
						
				%>
				<!-- From here modified trip sheet starts -->

				<h3>Trip Sheet</h3>

				<%
				System.out.println("size"+tripSheetSaved.size());
					for (TripDetailsDto tripDetailsDtoObj : tripSheetSaved) {
				%>
				<%
											 

												 String commentEditable=" display:none;";
													disable = "disabled";
													String textdisable = "readOnly";
													System.out.println(request.getSession().getAttribute("role").toString());
													if( new  AccessRightService().hasRight(AccessRightConstants.LEVEL1_APPROVER, request.getSession().getAttribute("role").toString())) 
													{
															System.out.println("TM:::"+tripDetailsDtoObj.getApprovalStatus()+tripDetailsDtoObj.getApprovalStatus()+tripDetailsDtoObj.getApprovalStatus());
															if (  (tripDetailsDtoObj.getApprovalStatus() != null && ( 
																	  tripDetailsDtoObj.getApprovalStatus()
																				.equals("Open") || tripDetailsDtoObj
																		.getApprovalStatus()
																		.equals("Rejected by Transport Co-ordinator"))))

														

														{

														//	tripDetailsDtoObj.setActualLogTime("Tracked");
															disable = "";
															textdisable = "";
															totalDisable = false;
															commentEditable="";
														}

													}
													else if( new  AccessRightService().hasRight(AccessRightConstants.LEVEL2_APPROVER, request.getSession().getAttribute("role").toString())) 
												{
														System.out.println("TM:::"+tripDetailsDtoObj.getApprovalStatus()+tripDetailsDtoObj.getApprovalStatus()+tripDetailsDtoObj.getApprovalStatus());
														if (  (tripDetailsDtoObj.getApprovalStatus() != null && ( 
																  tripDetailsDtoObj.getApprovalStatus()
																			.equals("Sent for TC approval") || tripDetailsDtoObj
																	.getApprovalStatus()
																	.equals("Rejected by Transport Manager"))))

													

													{

													//	tripDetailsDtoObj.setActualLogTime("Tracked");
														disable = "";
														textdisable = "";
														totalDisable = false;
														commentEditable="";
													}
													}else if(new  AccessRightService().hasRight(AccessRightConstants.LEVEL3_APPROVER, request.getSession().getAttribute("role").toString()))
													{
														if (  (tripDetailsDtoObj.getApprovalStatus() != null && ( 
																  tripDetailsDtoObj.getApprovalStatus()
																			.equals("Sent for TM approval")  )))

													

													{
															disable = "";
															commentEditable="";
													}
													}
										%>
				<table border="1" style="width: 85%;">
					<tr>
					</tr>
					<tr>
						<td colspan="8">
										
						<div class="commentPanel" id="commentPanel-<%=tripDetailsDtoObj.getId() %>"  >
						<div class="commentPanelTitle" >
						
							<p style="float:left; margin-top: 0px; margin-left: 30%;" >Comments</p><label style="float:right;"> <a  onclick="closeCommentPanel()" >Close</a>  </label>
						</div>

		<%
						
						if(tripDetailsDtoObj.getComments()!=null&&tripDetailsDtoObj.getComments().size()>0){ %>
						<% 
						for(TripCommentDto tripCommentDto: tripDetailsDtoObj.getComments())
						{
						%>
						
						<div class="commentPanelContent" >
						 
					     <div class="commentPanelContentCommentTitle" >
						
						<label style=" position:relative; float:left; font: bold; "> <%=tripCommentDto.getCommentedByName() %></label>
						<label style="position:relative; float: right; margin-right: 2px; font: italic; " ><%=tripCommentDto.getCommentedDate() %></label>
						</div>
						
						
						<%=tripCommentDto.getComment() %>
						<br/>
						</div>
						<%} %>
						<%} %>
						<div class="commentPanelContent" style="<%=commentEditable%>" >
						 
					     <div class="commentPanelContentCommentTitle" >
						
						<label style=" position:relative; float:left; font: bold; "> Type Your Comment</label>
						<label style="position:relative; float: right; margin-right: 2px; font: italic; " ></label>
						</div>
						
						
						<textarea style="   float:left; margin-left=3%; margin-top=3%; width:97%; " cols="1" rows="6" name="comment-<%=tripDetailsDtoObj.getId() %>" ></textarea>
						<br/>
						</div>
						
						</div>
						
							<table style="background-color: #E8E8E6;">
								<tr>
									<td width="20%">
										 <label ><input class="checkTrip" 
											name="selectedTrip" value="<%=tripDetailsDtoObj.getId()%>"
											 type="checkbox"  
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

														String defaultTimeHour = tripDetailsDtoObj.getTrip_time()
																.trim().split(":")[0];
														String defaultTimeMinute = tripDetailsDtoObj.getTrip_time()
																.trim().split(":")[1];
														String defaultTimeOption = "";
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
															 /*if(tHourFlag=false && dHourFlag==flag&&defaultTimeHour.equals(hourString))
															{
																defaultTimeOption="selected";
															}*/ 
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


									</td>
								</tr>
								<tr>
									<td></td>
									<td></td>
									<td></td>
									<td>Escort Trip :</td>
									<td><select class="escort"
										id="escort-<%=tripDetailsDtoObj.getId()%>" <%=disable%>
										name="escort-<%=tripDetailsDtoObj.getId()%>" onChange='escortcancel(<%=tripDetailsDtoObj.getId()%>);'>
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
									
										<input type="hidden" id="escrtsts-<%=tripDetailsDtoObj.getId()%>" name="escrtsts-<%=tripDetailsDtoObj.getId()%>" value=<%=escortValue%> />
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
									<td width="20%" colspan="2" align="left">Approval Status: <i><%=tripDetailsDtoObj.getApprovalStatus()%></i></td>
										
									<td width="20%">
											
									
										
									</td>
									<td width="20%">Vehicle No</td>
									<td width="20%"><%=vehicleNo%>&nbsp;&nbsp;&nbsp;
									<input type="hidden"
										class="vehicleNo required "<%=textdisable%>
										value="<%=vehicleNo%>"
										id="vehiclNo-<%=tripDetailsDtoObj.getId()%>"
										name="vehiclNo-<%=tripDetailsDtoObj.getId()%>" />  <%
 	String comment = "";
 			if (tripDetailsDtoObj.getComment() != null
 					&& !tripDetailsDtoObj.getComment().trim()
 							.equals("")) {
 				comment = tripDetailsDtoObj.getComment();

 			}
 %> <input type="hidden" name="comment" value="<%=comment%>" />
 <a class="showComment" style="font:  large;" onclick="showComment(<%=tripDetailsDtoObj.getId() %>)" >Comments</a>
 </td>
								</tr>
							<%if(tripDetailsDtoObj.getApprovalStatus().equalsIgnoreCase("Sent For TM approval"))
								{
								TripDetailsDto tdto = new TripDetailsService().getTripRate(tripDetailsDtoObj.getId());
								double actualcost=0;
								if(tdto.getEscortCost()!=null)
								actualcost=tdto.getTripRate() + Double.parseDouble(tdto.getEscortCost());
								%>
							 <tr><td width="20"></td><td width="20"></td><td width="20"></td>
							<td width="20%"> Actual Trip Cost</td>
									<td width="20%"><input type="text" value="<%=actualcost %>" id="actualcost<%=tripDetailsDtoObj.getId()%>"  readonly/>&nbsp;<input type="checkbox" name="Deduction" onclick='showdedtr(<%=tripDetailsDtoObj.getId()%>)' >Variation</td></tr>
									<tr id="dedtr<%=tripDetailsDtoObj.getId()%>" style="display:none;"><td width="20%"></td><td width="20%"></td><td width="20%"></td>
									<td width="20%"><select id="op<%=tripDetailsDtoObj.getId()%>"><option value="add">Addition</option><option value="minus">Deduction</option></select></td>
									<td width="20%"><input type="text" id="variation<%=tripDetailsDtoObj.getId()%>"/>&nbsp;&nbsp;<input type="button" class="formbutton" value="Apply" onclick='variationcalc(<%=tripDetailsDtoObj.getId()%>,<%=tdto.getTripRate()%>)'/></td></tr>
									<% }  %>
							</table>
						</td>
					</tr>
					<tr>
						<th width="4%">#</th>
						<th width="20%">Name</th>
						<th width="20%">Area</th>
						<th width="20%">Place</th>
						<th width="20%">landmark</th>
						<th width="10%">Contact No</th>
						<th width="6%">Present</th>
						<th width="6%">Reason For No-Show</th>

					</tr>
					<%
						int i = 1;
								for (TripDetailsChildDto tripDetailsChildDto : tripDetailsDtoObj
										.getTripDetailsChildDtoList()) {
					%>
					<tr>
						<td><%=i%></td>
						<td><%=tripDetailsChildDto.getEmployeeName()%></td>
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
						<%
					boolean approvalRow=false;
						if(  new  AccessRightService().hasRight(AccessRightConstants.LEVEL1_APPROVER, request.getSession().getAttribute("role").toString()) 	)
						{
							System.out.println("role is tm : status :"+tripDetailsDtoObj.getApprovalStatus());
							if (  (tripDetailsDtoObj.getApprovalStatus() != null && ( 
									  tripDetailsDtoObj.getApprovalStatus()
												.equals("Open") || tripDetailsDtoObj
										.getApprovalStatus()
										.equals("Rejected by Transport Co-ordinator"))))

						

						{
								approvalRow=true;
						}
						}
							else if(  new  AccessRightService().hasRight(AccessRightConstants.LEVEL2_APPROVER, request.getSession().getAttribute("role").toString()) 	)
					{
						System.out.println("role is tm : status :"+tripDetailsDtoObj.getApprovalStatus());
						if (  (tripDetailsDtoObj.getApprovalStatus() != null && ( 
								  tripDetailsDtoObj.getApprovalStatus()
											.equals("Sent for TC approval") || tripDetailsDtoObj
									.getApprovalStatus()
									.equals("Rejected by Transport Manager"))))

					

					{
							approvalRow=true;
					}
					}else if(new  AccessRightService().hasRight(AccessRightConstants.LEVEL3_APPROVER, request.getSession().getAttribute("role").toString()))
					{
						if (  (tripDetailsDtoObj.getApprovalStatus() != null && ( 
								  tripDetailsDtoObj.getApprovalStatus()
											.equals("Sent for TM approval") )))

					

					{
							approvalRow=true;
					}
					}
					if(approvalRow)
					{
						multiapprove="Yes";
%>				<tr style="background-color: #E8E8E6;">
						<td colspan="5"></td>
						<td colspan="3"><input class="formbutton" type="submit"
							value="Approve"
							onclick="return approveOne(<%=tripDetailsDtoObj.getId()%>);" />

							<input type="submit" value="Reject" class="formbutton"
							onclick="return disapproveOne(<%=tripDetailsDtoObj.getId()%>);">
						</td>
					</tr>
					<%} %>


					</tbody>
					
				
				</table>
				<br /> <br /> <br />
				
					
				<%
					}
					}
				%>

				<% if(multiapprove.equalsIgnoreCase("Yes")&&!request.getParameter("approvalStatusParam").equalsIgnoreCase("All"))
				{ %>
						
 <br/>
 <p align="center"><input type="button" value="Select All" onClick="selectAll()" class="formbutton"/></p>
 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		<p align="center"><input type="Button"  class="formbutton" value="Approve Selected" onclick="return goToApproval();"/></p>
			<%} %>
			
				
					<%if (!flag) {
				%>
				<p>No trips to display</p>


				<%
					}
				%>
				
				<br />
				
				<br/>
				<br/>
				
					 <input type="Button"  style="margin-left: 2%"
								name="add" class="formbutton" value="Back"
								onclick="javascript: window.location='display_TrackedTripBucket.jsp'" />
				 
				&nbsp; <br /> <br />
				
				<hr />

				
			</form>
			<form action="UpdateTripRate" method="post"  id="costupdate" >
			<input type="hidden" id="updatetripid" name="updatetripid"/>
			<input type="hidden" id="oldrate" name="oldrate"/>
			<input type="hidden" id="updatevariation" name="updatevariation"/>
			<input type="hidden" id="updateoperation" name="updateoperation"/>
			<% 
					String str=request.getRequestURL()+"?"+request.getQueryString();
					session.setAttribute("url", str);
			%>
			</form>
			
	<p id="bottom"></p>
			<%@include file="Footer.jsp"%>
				
		</div>
	</div>
</body>
</html>

