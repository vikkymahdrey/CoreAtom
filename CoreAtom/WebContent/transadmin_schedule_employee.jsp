<%@page import="com.agiledge.atom.constants.SettingsConstant"%>
<%@page import="com.agiledge.atom.service.PaginationService"%>
<%@page import="com.agiledge.atom.service.LogTimeService"%>
<%@page import="com.agiledge.atom.service.SiteService"%>
<%@page import="com.agiledge.atom.dto.SiteDto"%>
<%@page import="java.util.List"%>
<%@page import="com.agiledge.atom.servlets.subscription.SubscriptionSelector"%>
<%@page import="com.agiledge.atom.dao.LogTimeDao"%>
<%@page import="com.agiledge.atom.dto.LogTimeDto"%>
<%@page import="com.agiledge.atom.dto.SchedulingDto"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.agiledge.atom.dao.SchedulingDao"%>
<%@page import="com.agiledge.atom.commons.OtherFunctions"%>
<%@page import="com.agiledge.atom.dao.OtherDao"%>
<%@ page contentType="text/html; charset=utf-8" language="java"
	import="java.sql.*" errorPage="error.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Schedule Employee</title>
<link href="css/style.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="js/jquery-latest.js"></script>
<style type="text/css">
@import "css/jquery.datepick.css";
</style>
<script type="text/javascript" src="js/jquery.datepick.js"></script>
<script src="js/dateValidation.js"></script>
<!-- Beginning of compulsory code below -->
<script type="text/javascript">
	var employeeCount;
	$(window).load(function() {
		$("#checkAllEmployees").click(checkOrUncheckEmployees);
		$("#checkAllLoginTime").click(setAllLoginTimeSame);
		$("#checkAllLogoutTime").click(setAllLogoutTimeSame);
		$("#checkAllToDate").click(setAllToDate);
		$("#checkAllFromDate").click(setAllFromDate);
		//$("#checkAllProject").click(setAllProject);
		$("#checkAllWeeklyOff").click(setAllWeeklyOff);
		employeeCount = document.getElementById("employeeCount").value;
		var from_date;//="from_date";
		var to_date;//="to_date";*/
		for ( var i = 1; i <= employeeCount; i++) {
			from_date = "from_date" + i;
			to_date = "to_date" + i;
			$("#" + from_date).datepick();
			$("#" + to_date).datepick();
		}

		// on time validation functions calling

		/*
		var element = $( ".checkEmployee:checked").parent().parent().children().children("input");
		 $(element).click(validateRowOnTime);
		 */
		$(".errorImage").hide();
		$("input[type=text]").change(instentValidation);

		$("select").change(instentValidation);

		$(".checkEmployee").change(makeElementsInTheRowToBeValidated);

	});

	//---search using site
	function searchForm() {
		//alert('hai ');
		document.siteSearchForm.action = "transadmin_schedule_employee.jsp";
		document.siteSearchForm.submit();
		return false;
	}

	function setAllWeeklyOff() {
		if ($(this).is(":checked")) {
			var element = $(".checkEmployee:checked").parent().parent()
					.children().children(".weeklyoff");
			$(element).attr("checked", ":checked");

		} else {
			var element = $(".checkEmployee:checked").parent().parent()
					.children().children(".weeklyoff");
			$(element).removeAttr("checked");
		}
	}
	//-------  when the check employee is clicked
	function makeElementsInTheRowToBeValidated() {
		try {
			if ($(this).is(":checked")) {

			//	$(this).parent().parent().children().children(".projectdesc")
			//			.addClass("validate");
				$(this).parent().parent().children().children(".toDate")
						.addClass("validate");
				$(this).parent().parent().children().children(".fromDate")
						.addClass("validate");
				$(this).parent().parent().children().children(".loginTime")
						.addClass("validate");
				$(this).parent().parent().children().children(".logoutTime")
						.addClass("validate");

			} else {

				//$(this).parent().parent().children().children(".projectdesc")
				//		.removeClass("validate");
				$(this).parent().parent().children().children(".toDate")
						.removeClass("validate");
				$(this).parent().parent().children().children(".fromDate")
						.removeClass("validate");
				$(this).parent().parent().children().children(".loginTime")
						.removeClass("validate");
				$(this).parent().parent().children().children(".logoutTime")
						.removeClass("validate");
				$(this).parent().parent().children().children(".logoutTime")
						.removeClass("validate");
				$(this).parent().parent().children().children(".errorImage")
						.hide();
				$(this).parent().parent().children().children(
						"input[type=text]").val("");
				$(this).parent().parent().children().children("select").val("");

			}
		} catch (e) {
			alert(e);
		}
	}

	//--------------on  4 11 2012 ------------------ starts---

	function setAllFromDate() {
		try {

			if ($("#checkAllFromDate").is(":checked")) {

				var value = $(".checkEmployee:checked").parent().parent()
						.children().children(".fromDate").first().val();

				var element = $(".checkEmployee:checked").parent().parent()
						.children().children(".fromDate");
				element.val(value);

			} else {
				var oldValue = $(".checkEmployee:checked").parent().parent()
						.children().children(".fromDate").first().val();
				var element = $(".checkEmployee:checked").parent().parent()
						.children().children(".fromDate");
				element.val("");
				$(".checkEmployee:checked").parent().parent().children()
						.children(".fromDate").first().val(oldValue);

			}
		} catch (e) {
			alert(e.message);
		}
	}

	function setAllToDate() {
		try {

			if ($("#checkAllToDate").is(":checked")) {

				var value = $(".checkEmployee:checked").parent().parent()
						.children().children(".toDate").first().val();
				$(".checkEmployee:checked").parent().parent().children()
						.children(".toDate").first().val();
				var element = $(".checkEmployee:checked").parent().parent()
						.children().children(".toDate");
				element.val(value);

			} else {

				var oldValue = $(".checkEmployee:checked").parent().parent()
						.children().children(".toDate").first().val();
				var element = $(".checkEmployee:checked").parent().parent()
						.children().children(".toDate");
				element.val("");
				$(".checkEmployee:checked").parent().parent().children()
						.children(".toDate").first().val(oldValue);
			}
		} catch (e) {
			alert(e.message);
		}
	}

	function setAllProject() {

		try {

			if ($("#checkAllProject").is(":checked")) {

				// $(".project").parent().parent().children().children(".checkEmployee:checked").parent().parent().children().children(".project").val(valueId);
				// $(".projectdesc").parent().parent().children().children(".checkEmployee:checked").parent().parent().children().children(".projectdesc").val(valueDesc);

				var valueDesc = $(".checkEmployee:checked").parent().parent()
						.children().children(".projectdesc").first().val();

				var valueId = $(".checkEmployee:checked").parent().parent()
						.children().children(".project").first().val();

				// alert(value1);
				var element1 = $(".checkEmployee:checked").parent().parent()
						.children().children(".project");
				var element2 = $(".checkEmployee:checked").parent().parent()
						.children().children(".projectdesc");
				element1.val(valueId);
				element2.val(valueDesc);

				$(element1).each(function() {
					copyTimesSpecificToProject(this);
				});
			} else {
				var oldProject = $(".checkEmployee:checked").parent().parent()
						.children().children(".project").first().val();

				var oldProjectDesc = $(".checkEmployee:checked").parent()
						.parent().children().children(".projectdesc").first()
						.val();

				var element1 = $(".checkEmployee:checked").parent().parent()
						.children().children(".project");
				var element2 = $(".checkEmployee:checked").parent().parent()
						.children().children(".projectdesc");
				element1.val("");
				element2.val("");
				$(".checkEmployee:checked").parent().parent().children()
						.children(".project").first().val(oldProject);
				$(".checkEmployee:checked").parent().parent().children()
						.children(".projectdesc").first().val(oldProjectDesc);

			}
		} catch (e) {
			alert(e.message);
		}
	}

 function copyTimesSpecificToProject(element) {
		try {

			if ($("#checkAllProject").is(":checked")) {

				// $(".project").parent().parent().children().children(".checkEmployee:checked").parent().parent().children().children(".project").val(valueId);
				// $(".projectdesc").parent().parent().children().children(".checkEmployee:checked").parent().parent().children().children(".projectdesc").val(valueDesc);

				var firstProjectElement = $(".checkEmployee:checked").parent()
						.parent().children().children(".project");
				if ($(element).val() == $(firstProjectElement).val()) {

					var firstLoginElement = $(".checkEmployee:checked")
							.parent().parent().children()
							.children(".loginTime").first();

					var firstLogoutElement = $(".checkEmployee:checked")
							.parent().parent().children().children(
									".logoutTime").first();
					var loginVal = $(firstLoginElement).val();
					var logoutVal = $(firstLogoutElement).val();

					var currentLoginElement = $(".checkEmployee:checked")
							.parent().parent().children()
							.children(".loginTime");

					var currentLogoutElement = $(".checkEmployee:checked")
							.parent().parent().children().children(
									".logoutTime");

					// alert(value1);
					$(currentLoginElement).html($(firstLoginElement).html());
					$(currentLogoutElement).html($(firstLogoutElement).html());
					$(firstLoginElement).val(loginVal);
					$(firstLogoutElement).val(logoutVal);
					

				}
			}

		} catch (e) {
			alert(e.message);
		}
	}


	function setAllLoginTimeSame() {
		setAllLoginTimeSame_sub();
	}

	function setAllLogoutTimeSame() {
		setAllLogoutTimeSame_sub();
	}

	function setAllLogoutTimeSame_sub() {
		try {

			if ($("#checkAllLogoutTime").is(":checked")) {
				var value = $(".checkEmployee:checked").parent().parent()
						.children().children(".logoutTime ").first().val();
				//var value = $("#logouttime1 option:selected").val();
				var element = $(".checkEmployee:checked").parent().parent()
						.children().children(".logoutTime");
				element.removeAttr("selected");
				element.val(value);

				var projectValue = $(".checkEmployee:checked").parent()
						.parent().children().children(".project ").first()
						.val();
				if (value != null && value != "") {
					element.each(function() {
						//alert($(this).val());
						if ($(this).val() == null || $(this).val() == "") {
							//alert('tick..');
							var thisProjectValue = $(this).parent().parent()
									.children().children(".project").val();
							//	alert(" 1->" +thisProjectValue + " 2->" +projectValue);
							if (thisProjectValue == projectValue) {
								$(this).append(
										"<option selected value='"+value+"'>"
												+ value + "</option>")
							}
						}
					});
				}

			} else {
				var oldValue = $(".checkEmployee:checked").parent().parent()
						.children().children(".logoutTime ").first().val();
				var element = $(".checkEmployee:checked").parent().parent()
						.children().children(".logoutTime");
				element.removeAttr("selected");
				element.val("");
				$(".checkEmployee:checked").parent().parent().children()
						.children(".logoutTime ").first().val(oldValue);

			}
		} catch (e) {
			alert(e.message);
		}
	}

	function setAllLoginTimeSame_sub() {
		try {

			if ($("#checkAllLoginTime").is(":checked")) {

				var value = $(".checkEmployee:checked").parent().parent()
						.children().children(".loginTime ").first().val();

				//$(".loginTime option").removeAttr("selected");
				//$(".loginTime option[value='" + value + "']").attr("selected",
				//		"selected");
				var element = $(".checkEmployee:checked").parent().parent()
						.children().children(".loginTime ");
				element.removeAttr("selected");
				element.val(value);

				var projectValue = $(".checkEmployee:checked").parent()
						.parent().children().children(".project ").first()
						.val();
				if (value != null && value != "") {
					element.each(function() {
						//alert($(this).val());
						if ($(this).val() == null || $(this).val() == "") {
							//alert('tick..');
							var thisProjectValue = $(this).parent().parent()
									.children().children(".project").val();
							//	alert(" 1->" +thisProjectValue + " 2->" +projectValue);
							if (thisProjectValue == projectValue) {
								$(this).append(
										"<option selected value='"+value+"'>"
												+ value + "</option>")
							}
						}
					});
				}

			} else {
				var oldValue = $(".checkEmployee:checked").parent().parent()
						.children().children(".loginTime ").first().val();
				var element = $(".checkEmployee:checked").parent().parent()
						.children().children(".loginTime ");
				element.removeAttr("selected");
				element.val("");
				$(".checkEmployee:checked").parent().parent().children()
						.children(".loginTime ").first().val(oldValue);

			}
		} catch (e) {
			alert(e.message);
		}
	}

	/*
	function setAllLoginTimeSame() {
		try {
			 
			 
			if ($("#checkAllLoginTime").is(":checked")) {

				var value =  $( ".checkEmployee:checked").parent().parent().children().children(".loginTime ").first().val();
				//$(".loginTime option").removeAttr("selected");
				//$(".loginTime option[value='" + value + "']").attr("selected",
				//		"selected");
				 var element=$( ".checkEmployee:checked").parent().parent().children().children(".loginTime ");
				 element.removeAttr("selected");
				element.val(value);

			}else
				{
				var oldValue=$( ".checkEmployee:checked").parent().parent().children().children(".loginTime ").first().val();
				var element=$( ".checkEmployee:checked").parent().parent().children().children(".loginTime ");
				 element.removeAttr("selected");
				element.val("");
				$( ".checkEmployee:checked").parent().parent().children().children(".loginTime ").first().val(oldValue);
				
				}
		} catch (e) {
			alert(e.message);
		}
	}

	function setAllLogoutTimeSame() {
		try {

			if ($("#checkAllLogoutTime").is(":checked")) {
				var value =  $( ".checkEmployee:checked").parent().parent().children().children(".logoutTime ").first().val();
				//var value = $("#logouttime1 option:selected").val();
				var element=$( ".checkEmployee:checked").parent().parent().children().children(".logoutTime");
				 element.removeAttr("selected");
				element.val(value);

			}else
				{
				var oldValue= $( ".checkEmployee:checked").parent().parent().children().children(".logoutTime ").first().val();
				var element=$( ".checkEmployee:checked").parent().parent().children().children(".logoutTime");
				 element.removeAttr("selected");
				element.val("");
				 $( ".checkEmployee:checked").parent().parent().children().children(".logoutTime ").first().val(oldValue);
				
				}
		} catch (e) {
			alert(e.message);
		}
	}

	 */

	function checkOrUncheckEmployees() {
		try {
			if ($("#checkAllEmployees").is(":checked")) {
				$(".checkEmployee").attr("checked", "checked");

			} else {
				$(".checkEmployee").removeAttr("checked");

			}

			$(".checkEmployee").trigger('change');

		} catch (e) {
			alert(e.message);
		}
	}

	//-------------------

	//------------------

	function validateWithSubscriptionEffectiveDates1(row) {
		var flag = true;

		try {
			var fromDate = $(row).parent("td").parent("tr").children("td")
					.children(".fromDate").val();
			var effectiveDate = $(row).parent("td").parent("tr").children(
					".td-EffectiveDate").children("input[type=hidden]").val();
			//alert("fromdate: "+ fromDate + " effective date : "+ effectiveDate);
			//alert(CompareTwoDatesyyyymmddddmmyyyy(fromDate,effectiveDate));
			if (CompareTwoDatesyyyymmddddmmyyyy(fromDate, effectiveDate)) {
				flag = false;
			}
		} catch (e) {
			flag = false;
			alert(e.message);

		}
		return flag;
	}

	// ----------on 26 11 2012-----------validate row on time

	/*	
	 function validateAllFieldsInTheRow(row)
	 {
	 var flag=true;
	 var cur_date = new Date();
	
	 try {
	
	 $(".errorImage").hide();
	 var projectDesc= $(row).parent("td").parent("tr").children("td").children(".checkEmployee").parent().parent().children().children(".projectdesc");
	 var fromDate= $(row).parent("td").parent("tr").children("td").children(".checkEmployee").parent().parent().children().children(".fromDate");
	 var toDate= $(row).parent("td").parent("tr").children("td").children(".checkEmployee").parent().parent().children().children(".toDate");
	 var loginTime= $(row).parent("td").parent("tr").children("td").children(".checkEmployee").parent().parent().children().children(".loginTime");
	 var logoutTime= $(row).parent("td").parent("tr").children("td").children(".checkEmployee").parent().parent().children().children(".logoutTime");
	 var scheduledTo=$(row).parent("td").parent("tr").children("td").children(".checkEmployee").parent().parent().children().children(".scheduledTo");
	
	 //alert(projectDesc.val() + " " + fromDate.val() + " "+ fromDate.val() + " "+toDate.val() + " " + loginTime.val() + " " + logoutTime.val());
	
	 // error images
	
	
	
	
	 return flag;
	
	
	 } catch (e) {
	 alert("Eror" + e);
	 return false;
	 }
	 }
	 */

	function formValidate() {
		try {
			$(".validate").each(function() {
				$(this).trigger('change');
			});
			/* $("input[class='validate']").each(function(){
			$(this).trigger('change');
			});
			alert("TTT"); */
		} catch (e) {
			alert(e);
		}

	}

	// validation on time
	function instentValidation() {
		// alert("val : "+ $(this).val());
		var flag = true;

		try {
			if ($(this).hasClass("validate")) {
				if ($(this).hasClass("projectdesc")) {

					flag = validate_projectDesc($(this));

				}

				if ($(this).hasClass("fromDate")) {
					var toDate = $(this).parent("td").parent("tr").children(
							"td").children(".toDate");

					flag = validate_fromDate($(this), $(toDate));
					if (flag) {
						var scheduledTo = $(this).parent("td").parent("tr")
								.children("td").children(".scheduledTo");
						var scheduledFrom = $(this).parent("td").parent("tr")
								.children("td").children(".scheduledFrom");
						flag = validate_fromDateAndtToDate($(this),
								$(scheduledTo), $(toDate), $(scheduledFrom));
					}
				}
				if ($(this).hasClass("toDate")) {
					flag = validate_toDate($(this));
					if (flag) {
						var scheduledTo = $(this).parent("td").parent("tr")
								.children("td").children(".scheduledTo");
						var scheduledFrom = $(this).parent("td").parent("tr")
								.children("td").children(".scheduledFrom");
						var fromDate = $(this).parent("td").parent("tr")
								.children("td").children(".checkEmployee")
								.parent().parent().children().children(
										".fromDate");
						flag = validate_fromDateAndtToDate($(fromDate),
								$(scheduledTo), $(this), $(scheduledFrom));
					}
				}
				if ($(this).hasClass("loginTime")) {
					 
					var logoutTimep = $(this).parent("td").parent("tr")
					.children("td").children(".logoutTime");
					flag = validate_loginTime($(this), logoutTimep);

				}
				if ($(this).hasClass("logoutTime")) {
					var loginTimep = $(this).parent("td").parent("tr")
					.children("td").children(".loginTime");
					flag = validate_logoutTime($(this), loginTimep);
					 
				}
			}

		} catch (e) {
			flag = false;
			alert(e);
		}

	}

	// validate intividuals

	function validate_projectDesc(projectDesc) {

		var flag = true;
		var projectDesc_img = $(projectDesc).parent("td").children("img");

		if ($(projectDesc).val() == "") {
			$(projectDesc_img).attr("title", "Please Enter Project");

			$(projectDesc_img).show();
			$(projectDesc).removeClass("valid");
			$(projectDesc).addClass("error");

			flag = false;

		} else {
			$(projectDesc_img).hide();
			$(projectDesc).removeClass("error");
			$(projectDesc).addClass("valid");
		}
		return flag;
	}

	function validate_fromDate(fromDate, toDate) {
		var flag = true;
		try {
			var fromDate_img = $(fromDate).parent("td").children("img");
			if ($(fromDate).val() == "") {
				$(fromDate_img).attr("title", "Please Enter from date");
				$(fromDate_img).show();
				$(fromDate).addClass("error");
				$(fromDate).removeClass("valid");
				flag = false;
			} else {
				$(fromDate_img).hide();
				$(fromDate).addClass("valid");
				$(fromDate).removeClass("error");
			}
		} catch (e) {
			alert("exception " + e);
		}
		return flag;
	}

	function validate_fromDateAndtToDate(fromDate, scheduledTo, toDate,
			scheduledFrom) {

		var flag = true;
		try {
			flag = validate_fromDate($(fromDate), $(toDate));
			if (flag == true) {
				var cur_date = new Date();
				var today = new Date();
			//	var todayevar = today.getDate() + "/" + today.getMonth() + "/"
			//			+ today.getFullYear();
				var todayevar = $("#curDate").val();
			
				var current_hour = cur_date.getHours();
				var next_day = new Date(cur_date
						.setDate(cur_date.getDate() + 1));
				var next_dayvar = next_day.getDate() + "/"
						+ next_day.getMonth() + "/" + next_day.getFullYear();
				var advFifteen_day = new Date(cur_date.setDate(cur_date
						.getDate() + 15));
				var advFifteen_dayvar = advFifteen_day.getDate() + "/"
						+ advFifteen_day.getMonth() + "/"
						+ advFifteen_day.getFullYear();
				var fromDate_img = $(fromDate).parent("td").children("img");
				var toDate_img = $(toDate).parent("td").children("img");
				var from_date = $(fromDate).val();
				var to_date = $(toDate).val();

				var mon1 = parseInt(from_date.substring(3, 5), 10) - 1;
				var dt1 = parseInt(from_date.substring(0, 2), 10);
				var yr1 = parseInt(from_date.substring(6, 10), 10);
				var date1 = new Date(yr1, mon1, dt1);

				var advThirt_day = new Date(date1.setDate(date1.getDate() + 30));

				var advThirt_dayvar = advThirt_day.getDate() + "/"
						+ advThirt_day.getMonth() + "/"
						+ advThirt_day.getFullYear();
				from_date = $(fromDate).val();
				var scheduled_To = $(scheduledTo).val();
				var scheduled_From = $(scheduledFrom).val();
				var subscriptionID = $(fromDate).parent().parent().children()
						.children(".checkEmployee").val();
				 
				
				
				//alert(from_date+"from date"+scheduled_To);				

				if (flag == true) {
					if (CompareTwodiffDates(to_date, from_date)) {
						$(fromDate_img)
								.attr("title",
										"Invalid Date Range!\nFrom Date cannot be after To Date!");
						$(fromDate_img).show();
						flag = false;
						$(fromDate).addClass("error");
						$(fromDate).removeClass("valid");
					} else if (validateWithSubscriptionEffectiveDates1($(fromDate)) == true) {
						$(fromDate_img)
								.attr("title",
										"Scheduling  subscription effective date");
						flag = false;
						$(fromDate_img).show();
						$(fromDate).addClass("error");
						$(fromDate).removeClass("valid");
					} else if (CompareTwoDatesddmmyyyy_1(from_date, todayevar)) {
						$(fromDate_img)
								.attr("title",
										"Invalid Date Range!\n From date Should  be after " + todayevar + "!");
						$(fromDate_img).show();
						flag = false;
						$(fromDate).addClass("error");
						$(fromDate).removeClass("valid");
					}
					/*	else if (CompareTwoDatesddmmyyyy(from_date, next_dayvar)
							&& current_hour >= 23) {
						$(fromDate_img)
								.attr("title",
										"Invalid date. Cut off time is 6 pm to schedule for next day.");

						$(fromDate_img).show();
						flag = false;
						$(fromDate).addClass("error");
						$(fromDate).removeClass("valid");
					} else  if (CompareTwoDatesddmmyyyy(from_date,advFifteen_dayvar)==false) {
																						$(fromDate_img).attr("title","Invalid Date Range!\n From Date cannot be after 15 Days!");
																						 
																						$(fromDate_img).show();
																						flag = false;
																						$(fromDate).addClass("error");
																						$(fromDate).removeClass("valid");
																						
																		New requirement  to avoid this
																						
																		
																					}*/else if ((to_date != "")
							&& (CompareTwoDatesddmmyyyy(to_date,
									advThirt_dayvar) == false)) {
						$(toDate_img)
								.attr("title",
										"Invalid Date Range!\n Date range should be 30 Days!");
						$(toDate_img).show();
						flag = false;
						$(toDate).addClass("error");
						$(toDate).removeClass("valid");
					} else {
						checkDatesAreOutOfExistingSchedules(subscriptionID,
								from_date, to_date, fromDate, fromDate_img);

					}
				}

			}
		} catch (e) {
			alert("Error in validate_fromDateToDate " + e);
		}
		return flag;
	}

	function checkDatesAreOutOfExistingSchedules(subscriptionID, fromDateParam,
			toDateParam, fromDate, fromDate_img) {

		var flag = true;

		try {
			if (subscriptionID != "" && fromDateParam != ""
					&& toDateParam != "") {

				var param = "?subscriptionID=" + subscriptionID + "&fromDate="
						+ fromDateParam + "&toDate=" + toDateParam;
				//alert(param);
				$
						.ajax({
							type : 'POST',
							url : 'IsWithinSchedules' + param,
							success : function(data) {
								var sss = data.trim();
								//	alert(sss);
								if (sss == "true") {
									//	alert('schedule exists'+flag);	
									$(fromDate_img)
											.attr("title",
													"Duplicate! Schedule exists on the date range");
									$(fromDate_img).show();
									flag = false;
									$(fromDate).addClass("error");
									$(fromDate).removeClass("valid");
									flag = false;

								} else {
									$(fromDate_img).hide();
									$(fromDate).addClass("valid");
									$(fromDate).removeClass("error");
								}

							},
							error : function(data) {
								var data = $.parseJSON(data);
								$.each(data.errors, function(index, value) {
									alert(value);
								});
								//	 flag=true;
								alert('error');
							}
						//dataType: 'json'

						});
			}
		} catch (e) {
			alert(e);
		}

		return flag;
	}

	function validate_toDate(toDate) {

		var flag = true;
		try {
			var toDate_img = $(toDate).parent("td").children("img");
			if ($(toDate).val() == "") {
				$(toDate_img).attr("title", "Please Enter to date");

				$(toDate_img).show();
				flag = false;

				$(toDate).addClass("error");
				$(toDate).removeClass("valid");

			} else {
				$(toDate_img).hide();

				$(toDate).addClass("valid");
				$(toDate).removeClass("error");
			}
		} catch (e) {
			alert(e);
		}
		return flag;
	}
	
	function validate_loginTime(loginTime, logoutTime) {

		var flag = true;
		try {
			var loginTime_img = $(loginTime).parent("td").children(
					".errorImage");

			if ($(loginTime).val() == "") {
				$(loginTime_img).attr("title", "Please Select Login Time");

				$(loginTime_img).show();

				flag = false;

				$(loginTime).addClass("error");
				$(loginTime).removeClass("valid");

			} else if($(loginTime).val()!="None" && ($(logoutTime).val()==$(loginTime).val())) {
				
				$(loginTime_img).attr("title", "Login & logout are same");

				$(loginTime_img).show();

				flag = false;

				$(loginTime).addClass("error");
				$(loginTime).removeClass("valid");

				
			} else {
				
				$(loginTime_img).hide();

				$(loginTime).addClass("valid");
				$(loginTime).removeClass("error");
			}

		} catch (e) {
			alert(e);
		}

		return flag;
	}

	function validate_logoutTime(logoutTime, loginTime) {
		var flag = true;
		try {
			var logoutTime_img = $(logoutTime).parent("td").children(
					".errorImage");
			//$(logoutTime_img).show();
			if ($(logoutTime).val() == "") {
				$(logoutTime_img).attr("title", "Please Select logout");

				$(logoutTime_img).show();
				flag = false;

				$(logoutTime).addClass("error");
				$(logoutTime).removeClass("valid");
			} if($(logoutTime).val()!="None" && ($(logoutTime).val()==$(loginTime).val())) {
				
				$(logoutTime_img).attr("title", "Login & logout are same");

				$(logoutTime_img).show();

				flag = false;

				$(logoutTime).addClass("error");
				$(logoutTime).removeClass("valid");

				
			} else {
				$(logoutTime_img).hide();

				$(logoutTime).addClass("valid");
				$(logoutTime).removeClass("error");
			}
		} catch (e) {
			alert(e);
		}
		return flag;
	}


	// validate entirform
	function Validate1() {

		var flag = true;
		formValidate();
		try {
			if ($(".validate").hasClass("valid")) {
				if ($(".validate").hasClass("error")) {
					alert("Validation Error.Move mouse pointer to error mark");
					flag = false;
				} else {
					flag = true;
				}

			} else {
				if (!$(".checkEmployee").is(":checked")) {
					alert("Please select atleast one row ");
				} else if ($(".validate").hasClass("error")) {
					alert("Validation Error.Move mouse pointer to error mark");

				}
				flag = false;
			}
		} catch (e) {
			alert(e);
		}
		return flag;

	}
	function showAll() {
		var site = document.getElementById("site").value;
		var listSize = document.getElementById("listSize").value;
		var startPos = 0;
		var endPos = listSize;
		window.location = "transadmin_schedule_employee.jsp?site=" + site + "&startPos="
				+ startPos + "&endPos=" + endPos;
	}

	function next() {
		var site = document.getElementById("site").value;
		var startPos = document.getElementById("startPos").value;
		var endPos = document.getElementById("endPos").value;
		var listSize = document.getElementById("listSize").value;
		startPos = parseInt(startPos)
				+ parseInt(document.getElementById("recordsize").value);
		endPos = parseInt(startPos)
				+ parseInt(document.getElementById("recordsize").value);

		if (startPos > listSize) {
			startPos = parseInt(startPos)
					- parseInt(document.getElementById("recordsize").value);
			endPos = parseInt(startPos)
					+ parseInt(document.getElementById("recordsize").value);
		} else {
			if (endPos > listSize) {
				endPos = listSize;
			}
			window.location = "transadmin_schedule_employee.jsp?site=" + site
					+ "&startPos=" + startPos + "&endPos=" + endPos;
		}

	}
	function pervious() {

		var site = document.getElementById("site").value;
		var startPos = document.getElementById("startPos").value;
		var endPos = document.getElementById("endPos").value;
		//var listSize = document.getElementById("listSize").value;
		endPos = startPos;
		startPos = parseInt(startPos)
				- parseInt(document.getElementById("recordsize").value);

		if (startPos < 0) {
			startPos = 0;
			endPos = parseInt(startPos)
					+ parseInt(document.getElementById("recordsize").value);
		} else {

			window.location = "transadmin_schedule_employee.jsp?site=" + site
					+ "&startPos=" + startPos + "&endPos=" + endPos;
		}
	}
</script>

<link href="css/style.css" rel="stylesheet" type="text/css" />
<link href="css/menu.css" rel="stylesheet" type="text/css" />
<script>
	$(document).ready(function() {

		setActiveMenuItem();
	});

	function setActiveMenuItem() {
		var url = window.location.pathname;
		var filename = url.substring(url.lastIndexOf('/') + 1);
		//  $("li[class=active']").removeAttr("active");

		$("a[href='" + filename + "']").parent().attr("class", "active");
		$("a[href='" + filename + "']").parent().parent().parent('li').attr(
				"class", "active");

	}

	function openWinodw(url) {
		window.open(url, 'Ratting',
				'width=400,height=350,left=150,top=200,toolbar=1,status=1,');

	}
</script>
</head>
<body>


	<%
		long empid = 0;

		String employeeId = OtherFunctions.checkUser(session);
		empid = Long.parseLong(employeeId);
	%>
	<%@include file="Header.jsp"%>
	<%
	long actEmpId=empid;
		if (session.getAttribute("delegatedId") != null) {
			employeeId = session.getAttribute("delegatedId").toString();
			empid = Long.parseLong(employeeId);
		}
		OtherDao ob = null;
		ob = OtherDao.getInstance();
		SchedulingDao SchedulingDaoObj = new SchedulingDao();
		ArrayList<SchedulingDto> schedulingEmpList;
		ArrayList<LogTimeDto> loginTimeList = null;
		ArrayList<LogTimeDto> logoutTimeList = null;
		ArrayList<LogTimeDto> masterLoginTimeList = null;
		ArrayList<LogTimeDto> masterLogoutTimeList = null;
		LogTimeService logTimeDaoObj = new LogTimeService();
		String site = "";
		site = request.getParameter("site");

		/*starting special section for pagination middle */
		int RECORD_SIZE = 50;
		String startPos = request.getParameter("startPos");
		String endPos = request.getParameter("endPos");
		if (startPos == null && endPos == null) {
			startPos = "0";
			endPos = "" + RECORD_SIZE;
		}

		
	%>
	<div id="body">
		<div class="content">
			<h3>Schedule for Employees</h3>

			<hr />
			<br />

			<div>
				<form name="siteSearchForm">
					<table style="width: 20%; border: 0px none;">
						<tr>
							<td>Site</td>
							<td>
							<input type="hidden" id="curDate" value="<%=SettingsConstant.getCurdate() %>" />								
							<%
									List<SiteDto> siteList = new SiteService().getSites();
								%> <select name="site" id="site" onchange="return searchForm();">
									<option value="">--select--</option>
									<%
										for (SiteDto dto : siteList) {
											String siteSelect = "";
											if (dto.getId().equals(site)) {
												siteSelect = "selected";
											}
									%>




									<option <%=siteSelect%> value="<%=dto.getId()%>">
										<%=dto.getName()%>
									</option>


									<%
										}
									%>
							</select>
							</td>
						</tr>
					</table>
				</form>
			</div>

			<br />
			<hr />
			<%
			if(site!=null && !site.equals("") && !site.equals("null"))
			{
			schedulingEmpList = SchedulingDaoObj
			.getAllSubscribedEmployeeDetails(site);
	List<SchedulingDto> schedulingEmpListIterated = new PaginationService()
			.getNext(schedulingEmpList, startPos, endPos);
	masterLoginTimeList = logTimeDaoObj
			.getAllGeneralLogtime("IN",site);
	masterLogoutTimeList = logTimeDaoObj
			.getAllGeneralLogtime("OUT",site);
	if (RECORD_SIZE > schedulingEmpList.size()) {
		endPos = "" + schedulingEmpList.size();
	}
	int serialNo = 0;
				if (schedulingEmpList != null && schedulingEmpList.size() > 0) {
			%>
			<table style="width: 40%">
				<tr>
					<td><a href="#" onclick="pervious()">Previous</a></td>

					<td id="pageInfo"><%=Integer.parseInt(startPos) + 1%> to <%=endPos%>
						of</td>
					<td><%=schedulingEmpList.size()%> is showing</td>
					<td><a href="#" onclick="next()">Next</a></td>
					<td><a href="#" onclick="showAll()">Show All</a></td>
				</tr>
			</table>

			<form name="schedule_emp" method="post" action="ScheduleEmployee"
				onsubmit="return Validate1()">
				<table class="dataTable">
					<tr>
						<td></td>
						
						<td></td>
						<td></td>
						<td></td>
						<td align="left" width="27%"></td>



						<td align="left" width="15%"><input type="checkbox"
							id="checkAllFromDate" /></td>
						<td align="left" width="15%"><input style="margin-left: 4%;"
							type="checkbox" id="checkAllToDate" /></td>
						<td align="left"><input type="checkbox"
							id="checkAllWeeklyOff" /></td>
						<td align="left" width="15%"><input type="checkbox"
							id="checkAllLoginTime" /></td>
						<td align="left" width="15%"><input type="checkbox"
							id="checkAllLogoutTime" /></td>
						<td></td>
						<td></td>
						<td></td>



					</tr>
					<thead>
						<tr>
							<td><input type="checkbox" id="checkAllEmployees" /></td>
							<th>Emp ID</th>
							<th>Name</th>
							<th>Subscription Effective Date(dd/mm/yyyy)</th>
							<th width="250"><%=SettingsConstant.PROJECT_TERM%></th>
							<th>From Date</th>
							<th>To Date</th>
							<th>Weekly Off</th>
							<th>Login</th>
							<th>Logout</th>
							<th>Status</th>
							<th>Scheduled Upto (dd/mm/yyyy)</th>
							<th>Scheduled By</th>
							<th>Updated on</th>


						</tr>
					</thead>					
					<%
						for (SchedulingDto schdulingEmployeeDto : schedulingEmpListIterated) {
								serialNo++;

								if (schdulingEmployeeDto.getProject() != null
										&& !schdulingEmployeeDto.getProject().equals("")) {

									loginTimeList = schdulingEmployeeDto.getLoginTimeDto();
									logoutTimeList = schdulingEmployeeDto.getLogoutTimeDto();
								} else {
									loginTimeList = masterLoginTimeList;
									logoutTimeList = masterLogoutTimeList;
								}
					%>
					<tr id="<%=serialNo%>">
						<td><input class="checkEmployee" size="1" type="checkbox"
							id="employeecheck<%=serialNo%>" name="subscriptionId"
							value="<%=schdulingEmployeeDto.getSubscriptionId()%>" /></td>
						<td><%=schdulingEmployeeDto.getEmployeeId()%></td>

						<td><%=schdulingEmployeeDto.getEmployeeName()%></td>

						<td class="td-EffectiveDate"><%=OtherFunctions
							.changeDateFromatToddmmyyyy(schdulingEmployeeDto
									.getSubscriptionDate())%> <input type="hidden"
							id="subscriptionEffectiveDate-<%=serialNo%>"
							value="<%=schdulingEmployeeDto.getSubscriptionDate()%>" /></td>


						<td><%=schdulingEmployeeDto.getDescription()%>
						<input type="hidden" class="project"
							name="project<%=schdulingEmployeeDto.getSubscriptionId()%>"
							id="project<%=serialNo%>"
							value="<%=schdulingEmployeeDto.getProject()%>" /></td>
						<td class="td-schedulingFromDate"><input readonly="readonly"
							name="from_date<%=schdulingEmployeeDto.getSubscriptionId()%>"
							id="from_date<%=serialNo%>" type="text" size="7"
							class="fromDate {showOtherMonths: true, firstDay: 1, dateFormat: 'yyyy-mm-dd',
                                        minDate: new Date(2008, 12 - 1, 25)} " />
							<img src="images/error.png" class="errorImage"
							id="from_date<%=serialNo%>_img" /></td>

						<td><input class="toDate" readonly="readonly"
							name="to_date<%=schdulingEmployeeDto.getSubscriptionId()%>"
							id="to_date<%=serialNo%>" type="text" size="7"
							class="{showOtherMonths: true, firstDay: 1, dateFormat: 'dd/mm/yyyy',
                                        minDate: new Date(2008, 12 - 1, 25)}" />
							<img src="images/error.png" class="errorImage"
							id="to_date<%=serialNo%>_img" /></td>
						<td><input type="checkbox"
							name="weeklyoff<%=schdulingEmployeeDto.getSubscriptionId()%>"
							class="weeklyoff" /></td>
						<td id="td-login<%=serialNo%>"><select class="loginTime"
							name="logintime<%=schdulingEmployeeDto.getSubscriptionId()%>"
							id="logintime<%=serialNo%>">

								<option value="">Select</option>
								<option value="none">None</option>
								<%
									for (LogTimeDto logTimeDto : loginTimeList) {
								%>
								<option value="<%=logTimeDto.getLogTime()%>"><%=logTimeDto.getLogTime()%></option>
								<%
									}
								%>
						</select> <img src="images/error.png" class="errorImage"
							id="logintime<%=serialNo%>_img" /></td>
						<td id="td-logout<%=serialNo%>"><select class="logoutTime"
							name="logouttime<%=schdulingEmployeeDto.getSubscriptionId()%>"
							id="logouttime<%=serialNo%>">

								<option value="">Select</option>
								<option value="none">None</option>
								<%
									for (LogTimeDto logTimeDto : logoutTimeList) {
								%>
								<option value="<%=logTimeDto.getLogTime()%>"><%=logTimeDto.getLogTime()%></option>
								<%
									}
								%>
						</select> <img src="images/error.png" class="errorImage"
							id="logouttime<%=serialNo%>_img" /></td>

						<td><input type="hidden" class="scheduledTo"
							id="scheduledTo<%=serialNo%>"
							value="<%=schdulingEmployeeDto.getSchedulingToDate()%>" /> <input
							type="hidden" class="scheduledFrom"
							id="scheduledFrom<%=serialNo%>"
							value="<%=schdulingEmployeeDto.getSchedulingFromDate()%>" /> <%
 	//System.out.println("scheduled to"+schdulingEmployeeDto.getSchedulingToDate());

 			if (!(("" + schdulingEmployeeDto.getSchedulingToDate())
 					.equals("null"))) {
 				out.print("Active</td><td>"
 						+ OtherFunctions
 								.changeDateFromatToddmmyyyy(schdulingEmployeeDto
 										.getSchedulingToDate()));
 			}

 			else {
 				out.print("Not Scheduled</td><td>&nbsp;");
 			}
 %></td>
						<td><%=schdulingEmployeeDto.getScheduledBy()%></td>
						<td><%=OtherFunctions
							.changeDateFromatToddmmyyyy(schdulingEmployeeDto
									.getStatusDate())%></td>



					</tr>
					<%
						}
					%>
					<tr>
						<td colspan="11">&nbsp;</td>
					</tr>
					<tr>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
						<td colspan="5" align="center"><input type="submit"
							class="formbutton" name="submit" value="Schedule" style="width: 70px" />&nbsp;
							<input type="button" class="formbutton" value="Back"
							onclick="javascript:history.go(-1);" /> <input type="hidden"
							id="employeeCount" name="employeeCount" value="<%=serialNo%>" />
						</td>
					</tr>
					<tr>
						<td colspan="11"><input type="hidden" name="source" value="admin"/></td>
					</tr>
				</table>

			</form>
			<table style="width: 40%">
				<tr>
					<td><a href="#" onclick="pervious()">Previous</a></td>

					<td id="pageInfo"><%=Integer.parseInt(startPos) + 1%> to <%=endPos%>
						of</td>
					<td><%=schedulingEmpList.size()%> is showing</td>
					<td><a href="#" onclick="next()">Next</a></td>
					<td><a href="#" onclick="showAll()">Show All</a></td>
				</tr>
				<tr>
					<td colspan="4"><input type="hidden" value="<%=RECORD_SIZE%>"
						id="recordsize" /> <input type="hidden" value="<%=endPos%>"
						id="endPos" /> <input type="hidden" value="<%=startPos%>"
						id="startPos" /> <input type="hidden"
						value="<%=schedulingEmpList.size()%>" id="listSize" /></td>
				</tr>
			</table>

			<%
				} else {
			%>
			<p style="height: 400px" class="status">NO Scheduling is pending
				for Employees</p>

			<%
				}}
			%>
			<%@include file="Footer.jsp"%>
		</div>
	</div>

</body>
</html>
