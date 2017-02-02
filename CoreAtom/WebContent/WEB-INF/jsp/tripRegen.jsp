<%--
    Document   : emp_subscription
    Created on : Aug 28, 2012, 12:51:01 PM
    Author     : 123
--%>

<%@page import="com.agiledge.atom.service.APLService"%>
<%@page import="com.agiledge.atom.service.SiteService"%>
<%@page import="com.agiledge.atom.dto.SiteDto"%>
<%@page import="com.agiledge.atom.service.EmployeeSubscriptionService"%>
<%@page import="com.agiledge.atom.dto.EmployeeSubscriptionDto"%>
<%@page import="com.agiledge.atom.dao.OtherDao"%>
<%@page import="com.agiledge.atom.commons.OtherFunctions"%>
<%@page import="java.lang.Exception"%>
<%@page import="com.agiledge.atom.dto.APLDto"%>
<%@page import="com.agiledge.atom.dao.APLDao"%>
<%@page import="java.util.List"%>
<%@page import="com.agiledge.atom.dao.EmployeeDao"%>
<%@page import="com.agiledge.atom.dto.EmployeeDto"%>
<%@page contentType="text/html" pageEncoding="UTF-8"
	errorPage="error.jsp"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
 <%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="http://www.nfl.com" prefix="disp" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
<link href="css/style.css" rel="stylesheet" type="text/css" />
<link href="css/menu.css" rel="stylesheet" type="text/css" />
 <link href="css/jquery.datepick.css" rel="stylesheet" type="text/css" />   
     
  	<script type="text/javascript" src="js/jquery-latest.js"></script>
	<script type="text/javascript" src="js/jquery.datepick.js"></script>

  	<link rel="stylesheet" href="//code.jquery.com/ui/1.11.2/themes/smoothness/jquery-ui.css">
  
  <script src="//code.jquery.com/ui/1.11.2/jquery-ui.js"></script>
	
 
<script src="js/dateValidation.js"></script>
<style>




	#formDiv {
			 
	  		border: thin;  
			border-style: outset;
			margin-left: 10px;
			display: inline-block;
			 
			padding: 3px;
	}
	
	#formDiv1 {
			 
	  		border: thin;  
			border-style: outset;
			margin-left: 10px;
			display: inline-block;
			margin-top:10px;
			padding: 3px;
			 
	}
	
	#formDiv1 > span {
		margin-left: 5px;
		 
		float: left;
		 
		 
	}
	 
	
	 
	
	span > input, span > select{
		width: 100px;
	}
	#getForm > span {
		margin-left: 3px;
		padding:3px;
		float: left;
		width:120px;
		 
	}
	#getForm > span > label {
		width: 100%;
		hight:8px;
	   
	}
	
	#getForm > span > button{
		margin-top: 20px;
		
	}
	
	.tripDiv{
		border: 1;
		border-bottom-color: black;
		   
		  
	}
	
	
	
	 
	
	#oprArea {
		padding:5px;
		 
	
	}
	
	.tripDiv{
		border-bottom-style: outset;
		width: 100%;
		background-color: #EBE4E8;
	}
	
	 .tripChildContainer {
		width:100%; 
		 
		
	}
	 .tripChildContainer > div{
	 
		 width: 100%;
		 border-top-style:outset;
		 padding-top: 5px;
		 padding-bottom:5px;
		 margin-top: 2px;
	 	 
	 }
	 
	 #oprDiv {
	 
	 	width:50%;
	 	height:500px;
	 	padding-right:4px;
	 	padding-bottom:4px;
	 	float: left;
	   
	  	
	 }
	  
	   
	 #employeePool{
	 	width:45%;
	 	 
	 	height: 390px;
	 	 float : left;
	 	margin-left: 50%;
	 	background-color : #EDE1E2;
		position:fixed;
		 overflow: scroll;
		 margin-bottom: 25px;
		  
	 	
	 }
	 
	 #employeePool> div{
	 
		 width: 100%;
		 border-top-style:outset;
		 padding-top: 5px;
		 padding-bottom:5px;
		 margin-top: 2px;
		 
	 	 
	 }
	
	    
	 
</style>

<script type="text/javascript">

	function draggingStart 
		(event, ui) {
			 try {
			 
			 $(this).parent("#employeePool").css("overflow","visible");
			 }catch (e) {
				alert(e) ;
			 }
		  
	}
	
	function draggingStop(event, ui) {
		draggingStop1(this);
	 }
	
	function draggingStop1(ui) {
		 try {
			 
			 $(ui).parent("#employeePool").css("overflow","scroll");
			 }catch (e) {
				 ;
			 }
	}
	
	function changeEscort() {
		var myId = $(this).attr("id").split("_")[1];
		 
		
			if($(this).val()=="YES") {
				var flag = validateTotalMember($("#tripDivContainer_" + myId).children()[0], 0);
				 ;
				if(flag==false) {
					alert("Remove extra employees to allocated escort");
					$(this).val("NO");
				}	
			}
	}
	
	function changeNthIndexOfElement(element, rank, index, properties) {
		property = properties.split(" ");

		 for(var ctr = 0 ; ctr < property.length; ctr ++) {
			 var prop = property[ctr];
			 //alert(prop);
			 try {
					if($(element).attr(prop)!=undefined &&$(element).attr(prop).trim()!="" ) {   
					 	var id = $(element).attr(prop);
							  
						var position = rank;
						 var value = index;
						 var s = id;
						 var nth = 0;
						 s = s.replace(/\d+/g, function (match, i, original) {
						     nth++;
						     return (nth === position) ? value : match;
						 });
					//	 alert(s);
						 $(element).attr(prop, s);
					}
				}catch(e) {
		//		 alert(" error n replace " + e);
			 }
		 }

	}
	
	function changeNthIndexOfAllChildren(parent, rank,index, properties) {
		property = properties.split(" ");
		$(parent).children().each(function() {
			
			 for(var ctr = 0 ; ctr < property.length; ctr ++) {
				 var prop = property[ctr];
				 //alert(prop);
				 try {
						if($(this).attr(prop)!=undefined &&$(this).attr(prop).trim()!="" ) {   
						 	var id = $(this).attr(prop);
								  
							 position = rank;
							  value = index;
							 var s = id;
							 var nth = 0;
							 s = s.replace(/\d+/g, function (match, i, original) {
							     nth++;
							     return (nth === position) ? value : match;
							 });
						//	 alert(s);
							 $(this).attr(prop, s);
						}
					}catch(e) {
			//		 alert(" error n replace " + e);
				 }
			 }
				
		});
	}
	 
	
	function createNewTrip() {
		 
		try { 
		if($(".tripDiv")!=undefined)  {
			var xx = $(".tripChildContainer").length;
			var divElement=$(".tripDiv")[0];
			 var newDivElement = $(divElement).clone();
			 var lastElement = $(".tripDiv")[xx-1];
			$(newDivElement).insertAfter(lastElement);
			

			//alert("length : " + xx);
			//xx=xx-1;
			$(newDivElement).children(".tripChildContainer").children().not(":first").remove();
		// alert("ID : " + $($(newDivElement).children()[0]).attr("id"));
		  
			changeNthIndexOfAllChildren(newDivElement, 1, xx, "name for id");
		 
			changeNthIndexOfAllChildren($(newDivElement).children()[0], 1, xx, "name for id");
		 
			changeNthIndexOfAllChildren($(newDivElement).children()[1], 1, xx, "name for id");
			
			 var item = $(newDivElement).children(".tripChildContainer").children(":first");
			 
			makeEmpty( item);
			 
			changeNthIndexOfAllChildren(item, 1, xx, "name for id");
			   
				$($("#tripHeaderDiv_" +xx).children("h4")).html("Trip " + xx);
			 $('html,body').animate({
			        scrollTop: $(newDivElement).offset().top},
			        'slow');	 
			 
			//mainFn();
			$("#tripVehicleId_" + xx).val("");
			$("#tripVehicleId_" + xx).change(changeVehicle);
		 	$("#isSecurity_" + xx).change(changeEscort);
			 
			 try {
					$("#tripDivContainer_" + xx).children(".aplItem").draggable({ cursor: "move", revert:true, 
						 start: draggingStart,
						 stop: draggingStop,
						  scroll: false,
						    
					});
					
					
					
					$("#tripDivContainer_" + xx).children(".divEmpty").draggable({ cursor: "move", revert:true, 
						 start: draggingStart,
						 stop: draggingStop,
						  scroll: false,
						    
					});
			 }catch(e) {
				 alert(e);
			 }
				try { 
					$("#tripDivContainer_" + xx).droppable({
						addClasses: false,
						drop: drop_item_toTemplate  
							
				  	});
		  	}catch(e) {
		  		alert(e);
		  	}

		}
		}catch(e) {
			alert("Error in create new trip :" + e);
		}
	}
	
	function mainFn() {
		try {
			$("#createNew").click(createNewTrip);	
			$("#fromDate").datepick();
			$("#toDate").datepick();
			 
			$(".vehicleSelect").change(changeVehicle);
			$(".isSecurity").change(changeEscort);
			 
			 try {
					$(".aplItem").draggable({ cursor: "move", revert:true, 
						 start: draggingStart,
						 stop: draggingStop,
						  scroll: false,
						    
					});
					
					
					
					$(".divEmpty").draggable({ cursor: "move", revert:true, 
						 start: draggingStart,
						 stop: draggingStop,
						  scroll: false,
						    
					});
			 }catch(e) {
				 alert(e);
			 }
			 try { 
				  	$("#employeePool").droppable({
						addClasses: false,
						drop: drop_item 
					});
			 }catch(e) {
				 alert(e);
			 }  
		  	try { 
				  	$(".tripChildContainer").droppable({
						addClasses: false,
						drop: drop_item_toTemplate  
							
				  	});
		  	}catch(e) {
		  		alert(e);
		  	}

			}catch(e) {
				alert(e);
			}
				}
	
	$(document).ready(mainFn );
	
	function delete_ids(ui) {
		  for(var i=0; i< $(ui).children().length; i++) {
			$((ui).children()[i]).removeAttr("id");
			$((ui).children()[i]).removeAttr("name");
			 
		  }
		  return ui;
	}
	
	function getClone(ui) {
		var newElement = $(ui).clone();
	 	newElement = delete_ids(newElement);
	 	 
	 	
	 	return newElement;
	}
	
	 
	
	function drop_item(event, ui) {
		
		 
		 
		try {
			if($(ui.draggable).hasClass("divEmpty")==false) {	
				//$(this).children(".tripChildContainer").append($(ui.draggable));
				$(ui.draggable).attr("style","position:relative; ");
				$(ui.draggable).removeAttr("id");
				 
				//---- $(ui.draggable).draggable();
				var receiver = document.elementFromPoint(event.clientX,event.clientY);
				// var elementText = $(ui.draggable).children(".displayText").text();
				 if(receiver!=null && $(ui.draggable)[0]!=$(receiver)[0]  ) {
	//				 alert($(receiver).text() + " " + $(receiver).hasClass("aplItem")+ " " + $(receiver).hasClass(".aplItem"));
					    if($(ui.draggable).parent().hasClass("tripChildContainer")) {
					 	//$("<div class='divEmpty' > EMPTY <div>").insertBefore(ui.draggable);
					 	
					 	var newElement = getClone( $(ui.draggable));
					 	 
					 		/* $(" <div class=\"aplItem\"  > "+
								 "<input type=\"hidden\"  class=\"landmarkId\ value=\""+$(ui.draggable).children(".landmarkId").val().trim()+"\"  /> " +
								 " 	<input type=\"hidden\" class=\"employeeId\" value=\""+$(ui.draggable).children(".employeeId").val()+"\" />  "+
								 "	 <span class=\"displayText\">"+elementText+"</span>  </div>");
					 	*/
					 	  $(newElement).draggable({ 
					 		  	 cursor: "move", 
								 start: draggingStart,
								 stop: draggingStop,  
								  revert:true 
								  } );
					 	
					 	$(ui.draggable).removeClass("aplItem");
					 	 
					 	$(ui.draggable).addClass("divEmpty");
					 	$(ui.draggable).children(".status").val("EMPTY"); 
					 	$(ui.draggable).children(".displayText").text("EMPTY-*-");
					 //	$(ui.draggable).draggable("disable");
					 	
					 	makeTripModified(ui.draggable);
					 	
					} else {
					     newElement =$(ui.draggable);
					}
					    
					   
					 if($(receiver).hasClass("aplItem")) {
						 
						 $(newElement).insertBefore( $(receiver) );
						  
						  
					 }else {
						 $(this).append($(newElement));
						  
					 }
					 
				 }
			}
		}catch(e) {
			alert(e);
		}
		
 
	}
	
	function copyItem(receiver, giver) {
		$(receiver).addClass("aplItem");
		 $(receiver).removeClass("divEmpty");		
		    
		/* $(receiver).
		 	 children(".displayText").text($(giver).children(".displayText").text());
		 $(receiver).
	 	 	children(".landmarkId").text($(giver).children(".landmarkId").text());
		  
		 $(receiver).
	 	 	children(".employeeId").text($(giver).children(".employeeId").text());
		  */
		  //alert(" copy...");
		  try {
		  for(var i=0; i< $( $(receiver).children() ).length; i++) {
			 // alert($(giver).children()[i].val());
			  var receiverClass =$( $(receiver).children()[i]).attr("class");
			$(  $(receiver).children("."+receiverClass) ).val($( $(giver).children("." + receiverClass) ).val());
			  $( $(receiver).children("."+receiverClass) ).html($( $(giver).children("." + receiverClass) ).html());
			 // $($(receiver).children()[i]).val($($(giver).children()[i]).val());
			  //if()
			 // $($(receiver).children()[i]).html($($(giver).children()[i]).html());
		  }
		  $ ( $(receiver).children(".status") ).val("");
		  }catch(e) {alert(" erro in copy item : " + e);}
		 //
		 //$(receiver).draggable();
		 //$(receiver).draggable("enable");
	}
	
	function seriesCopyForward(uidraggable, receiver) {
		var done=false;
	 	 
		if($(receiver).nextAll(".divEmpty")!=undefined && $(receiver).nextAll(".divEmpty").length>0) {
			 
			 $(receiver).addClass("receiverClass");
			   
		 
			   
			  var newElementTemp1 = getClone(uidraggable);
				  /* $(" <div class=\"aplItem\"  > "+
						 "<input type=\"hidden\"  class=\"landmarkId\ value=\""+$(uidraggable).children(".landmarkId").val().trim()+"\"  /> " +
						 " 	<input type=\"hidden\" class=\"employeeId\" value=\""+$(uidraggable).children(".employeeId").val()+"\" />  "+
						 "	 <span class=\"displayText\">" + $(uidraggable).children(".displayText").text() +"</span>  </div>");
				  */
			  var newElementTemp2 = getClone(uidraggable);
				 /* $(" <div class=\"aplItem\"  > "+
						 "<input type=\"hidden\"  class=\"landmarkId\ value=\""+$(uidraggable).children(".landmarkId").val().trim()+"\"  /> " +
						 " 	<input type=\"hidden\" class=\"employeeId\" value=\""+$(uidraggable).children(".employeeId").val()+"\" />  "+
						 "	 <span class=\"displayText\">" + $(uidraggable).children(".displayText").text() +"</span>  </div>");
			  */
			  copyItem(newElementTemp1,  receiver);
			  copyItem(receiver,  uidraggable );
			  var nextElement =null;
			for(var elementVar= $(receiver).next() ; $(elementVar).hasClass("divEmpty")==false && $(elementVar).attr("class")!=undefined; elementVar=$(elementVar).next() ) {
				 nextElement = elementVar;
				 
				 if(nextElement!=undefined) {
					 copyItem(newElementTemp2, nextElement);
				 	copyItem(nextElement, newElementTemp1);
				 	copyItem(newElementTemp1, newElementTemp2);
				 }
			//	 alert($(elementVar).children(".displayText").text() + " : " + $(elementVar).attr("class"));
				   
			 
			//	textOfAll=textOfAll+ " \n  " + $(elementVar).children(".displayText").text();  
			}	 
		  
			if( $(elementVar)!=null && $(elementVar) !=undefined && $(elementVar).hasClass("divEmpty")) {
			 
				copyItem(elementVar, newElementTemp1);
				done=true;
			} 
			$(receiver).removeClass("receiverClass");	
		 
		 }else {
			 done=false;
		 }
		
		//$(uidraggable).insertBefore( $(receiver) );

		return done;
	}
	
	
	function seriesCopyBackward(uidraggable, receiver) {
		var done=false;
	 	 
		if($(receiver).prevAll(".divEmpty")!=undefined && $(receiver).prevAll(".divEmpty").length>0) {
			  
			 $(receiver).addClass("receiverClass");
			   
		 
			   
			  var newElementTemp1 = getClone(uidraggable);
				  /* $(" <div class=\"aplItem\"  > "+
						 "<input type=\"hidden\"  class=\"landmarkId\ value=\""+$(uidraggable).children(".landmarkId").val().trim()+"\"  /> " +
						 " 	<input type=\"hidden\" class=\"employeeId\" value=\""+$(uidraggable).children(".employeeId").val()+"\" />  "+
						 "	 <span class=\"displayText\">" + $(uidraggable).children(".displayText").text() +"</span>  </div>");
			  */
			  var newElementTemp2 = getClone(uidraggable); 
				  /* $(" <div class=\"aplItem\"  > "+
						 "<input type=\"hidden\"  class=\"landmarkId\ value=\""+$(uidraggable).children(".landmarkId").val().trim()+"\"  /> " +
						 " 	<input type=\"hidden\" class=\"employeeId\" value=\""+$(uidraggable).children(".employeeId").val()+"\" />  "+
						 "	 <span class=\"displayText\">" + $(uidraggable).children(".displayText").text() +"</span>  </div>");
			   */
			  copyItem(newElementTemp1,  receiver);
			  copyItem(receiver,  uidraggable );
			  var nextElement =null;
			for(var elementVar= $(receiver).prev() ; $(elementVar).hasClass("divEmpty")==false && $(elementVar).attr("class")!=undefined; elementVar=$(elementVar).prev() ) {
				 nextElement = elementVar;
			 
				 if(nextElement!=undefined) {
					 copyItem(newElementTemp2, nextElement);
				 	copyItem(nextElement, newElementTemp1);
				 	copyItem(newElementTemp1, newElementTemp2);
				 }
			//	 alert($(elementVar).children(".displayText").text() + " : " + $(elementVar).attr("class"));
				   
			 
			//	textOfAll=textOfAll+ " \n  " + $(elementVar).children(".displayText").text();  
			}	 
		  
			if( $(elementVar)!=null && $(elementVar) !=undefined && $(elementVar).hasClass("divEmpty")) {
			 
				copyItem(elementVar, newElementTemp1);
				done=true;
			} 
			$(receiver).removeClass("receiverClass");	
		 
		 }else {
			 done=false;
		 }
		
		//$(uidraggable).insertBefore( $(receiver) );

		return done;
	}
	
	// set tripmodifed based on item (aplItem or emptyDiv)
function	makeTripModified(itm) {
		try {
		 
		if($(itm).parent().hasClass("tripChildContainer")) {
			var myId = $(itm).parent().attr("id").split("_")[1];
			$("#status_" + myId).val("modified");
		}
		}catch(e) {
			
			alert(e);
		}
	}

	
	// check element1 and element2 has same parent
	function hasSameParent(element1, element2) {
		return ($(element1).parent().attr("id")==$(element2).parent().attr("id"));
	}
	
	function validateTotalMember(ui, extra) {
		var flag = true;
		try {
			if($(ui).parent().hasClass("tripChildContainer"))
				{
					 
					var myId = $(ui).parent(".tripChildContainer").attr("id").split("_")[1];
					 
					var vid = $("#tripVehicleId_" + myId).val();
					var vehicle_seats = $("#vehicle_" + vid).val();
					var securityCount =0;
					if($("#isSecurity_" + myId).val()=="YES" ) {
						securityCount = 1;
					}
					 
					var allocatedSeats = $("#tripDivContainer_" + myId).children(".aplItem").length;
					if( (allocatedSeats + securityCount + extra)> vehicle_seats) {
						flag = false;
					}else {
						flag = true;
					}
					
					 
					
				}else {
					flag=true;
				} 
			
		}catch(e) {
			flag =false;
			alert("Total member validation error :" + e);
		}
		return flag;
	}
	
	function drop_item_toTemplate(event, ui) {
		
		 
		 
		try { 
				if($(ui.draggable).hasClass("divEmpty")==false ) {
				//$(this).children(".tripChildContainer").append($(ui.draggable));
				var preStyle = $(ui.draggable).attr("style"); 
				 $(ui.draggable).attr("style","position:relative; ");
				$(ui.draggable).removeAttr("id");
				 
				//---- $(ui.draggable).draggable({ cursor: "move", revert:true });
				var receiver = document.elementFromPoint(event.clientX,event.clientY);
				//alert(" receiver : " + $(receiver).attr("class") );
				if($(receiver).hasClass("aplItem")==false) { 
					// error.......
					if($(receiver).parents(".aplItem").length>0) {
				 		receiver = $(receiver).parent(".aplItem");
					} 
				}
			//	alert(" receiver : " + $(receiver).html() );
			 
				 if(receiver!=null &&  $(ui.draggable)[0]!=$(receiver)[0] && validateTotalMember(receiver, 1) ) {
					    
	//				 alert($(receiver).text() + " " + $(receiver).hasClass("aplItem")+ " " + $(receiver).hasClass(".aplItem"));
	 
					 if($(receiver).hasClass("aplItem")) {
						 
						  var giverTemp = getClone( $(ui.draggable));
						   
							  /* $(" <div class=\"aplItem\"  > "+
									 "<input type=\"hidden\"  class=\"landmarkId\ value=\""+$(ui.draggable).children(".landmarkId").val().trim()+"\"  /> " +
									 " 	<input type=\"hidden\" class=\"employeeId\" value=\""+$(ui.draggable).children(".employeeId").val()+"\" />  "+
									 "	 <span class=\"displayText\">" + $(ui.draggable).children(".displayText").text() +"</span>  </div>"); */
						  copyItem(giverTemp, ui.draggable);
						  if( $(ui.draggable).parent().hasClass("tripChildContainer") ) {					  
								$(ui.draggable).removeClass("aplItem"); 
							 	$(ui.draggable).addClass("divEmpty");
							 	$(ui.draggable).children(".status").val("EMPTY"); 
							 	$(ui.draggable).children(".displayText").text("EMPTY-*-");
							 	 
						//	 	$(ui.draggable).draggable("disable");	
						 }
						   
						 var done = seriesCopyBackward(giverTemp, receiver);
						 if(done==false) {
							 done = seriesCopyForward(giverTemp, receiver);
						 }
						 if(done) {							 
							 makeTripModified(ui.draggable);
							 makeTripModified(receiver);
							 if( $(ui.draggable).parent().hasClass("tripChildContainer")==false ) {
								 
								 
								 $(ui.draggable).remove();
								  
							 }
						 }else {
							  
							 $(ui.draggable).attr("style", preStyle);
							// alert("Unable to insert");
							 if( $(ui.draggable).parent().hasClass("tripChildContainer") ) {
								  draggingStop1(ui.draggable);
								 copyItem(ui.draggable, giverTemp);
							 }
							  
							 
						 }
					 }else if($(receiver).hasClass("divEmpty")) {
						 var parentId=$(ui.draggable).parent().attr("id");
						 makeTripModified(ui.draggable);
						 makeTripModified(receiver);
						  
						 if(parentId=="employeePool") { 
						 
							  
							  
							 $(receiver).addClass("aplItem");
							 $(receiver).removeClass("divEmpty");
							 
							 $(receiver).children(".status").val("");
							  
							/*  $(receiver).
							 	 children(".displayText").text($(ui.draggable).children(".displayText").text());
							 $(receiver).
						 	 	children(".landmarkId").text($(ui.draggable).children(".landmarkId").text());
							 $(receiver).
						 	 	children(".employeeId").text($(ui.draggable).children(".employeeId").text());
							 */
//							 $(receiver).draggable("enable");
							 copyItem(receiver, ui.draggable);
							 $(receiver).children(".status").val("");
							 draggingStop1(ui.draggable);	
							 $(ui.draggable).remove();
						  
						 } else if($(ui.draggable).parent().hasClass("tripChildContainer") ){
							 
							 copyItem(receiver, ui.draggable);
							makeEmpty(ui.draggable);
							 /*$(receiver).addClass("aplItem");
							 $(receiver).removeClass("divEmpty");
							 
							 $(receiver).children(".status").val("");
							  
							 $(receiver).
							 	 children(".displayText").text($(ui.draggable).children(".displayText").text());
							 $(receiver).
						 	 	children(".landmarkId").text($(ui.draggable).children(".landmarkId").text());
							 $(receiver).
						 	 	children(".employeeId").text($(ui.draggable).children(".employeeId").text());
							 //$(receiver).draggable("enable");
							 
								$(ui.draggable).removeClass("aplItem"); 
							 	$(ui.draggable).addClass("divEmpty");
							 	$(ui.draggable).children(".status").val("EMPTY"); 
							 	$(ui.draggable).children(".displayText").text("EMPTY-*-");
							// 	$(ui.draggable).draggable("disable");
							 */
							 
						 }
						  
						 
					 } 
					 
				 }else {
					 draggingStop1(ui.draggable);
				 }
				  
			}
			
		}catch(e) {
			alert(e);
		}
		
 
	}
	
	function changeVehicle() {
		 
		try {
		 
				vid = $(this).val();
				vehicle_seats = $("#vehicle_" + vid).val();
				  
				myId = $(this).attr("id").split("_")[1];
				// trip_modified status set
				
				
				// alert("#status_" + myId);
				$("#status_" + myId).val("modified");
				 
				
				var tripSeats =    $("#tripDivContainer_" + myId).children().length;
				//alert("vehicle seats : "+ vehicle_seats + " tripSeats : " + tripSeats);
				var securityCount =0;
						if($("#isSecurity_" + myId).val()=="YES" ) {
							securityCount = 1;
						}
				 if(vehicle_seats > tripSeats) {
					for(var idx = tripSeats; idx <vehicle_seats;idx++ ) {
						 
					var newElement = $("#tripDivContainer_" + myId).children().last();
					var itemId = $("#tripDivContainer_" + myId).children().length;
					
	 				   
	 				 newElement =  newElement.clone();
	 				changeNthIndexOfElement(newElement, 2, itemId, "id name");
	 				changeNthIndexOfAllChildren(newElement, 2, itemId, "id name for");
	 				makeEmpty(newElement);
	 					 /*$("<div class=\"divEmpty\" id=item_" + myId + "_" + idx  + "  > <input type=\"hidden\" value=\"EMPTY\" class=\"status\" name=\"tripList[" + myId + "].tripDetailsChildDtoList[" + idx + "].status\"/> " +
			  				 	" <input type=\"hidden\"  class=\"employeeId\" name=\"tripList[" + myId + "].tripDetailsChildDtoList[" + idx + "].employeeId\"/> " + 
						 		" <input type=\"hidden\"  class=\"landmarkId\" name=\"tripList[" + myId + "].tripDetailsChildDtoList[" + idx + "].landmarkId\"/> " + 
			  				 "	<span class=\"displayText\">EMPTY-*-</span>  </div> ");
	 				 */
					 $("#tripDivContainer_" + myId).append(newElement);
	 				 
					 $(newElement).draggable({ cursor: "move", 
						 start: draggingStart,
						 stop: draggingStop,  
						  revert:true 
						  });
	 				 
					}
				//	alert(vehicle_seats + " " + tripSeats);
				 }else if( (vehicle_seats - securityCount ) < tripSeats ) {
					 var emptyElements = $("#tripDivContainer_" + myId).children(".divEmpty").length;
					 
					// alert( "Empty elelemnts : " + emptyElements);
					 if(emptyElements < (tripSeats  - (vehicle_seats- securityCount) )) {
						 var removeNo = (tripSeats  - (vehicle_seats- securityCount)) - emptyElements; 
						 alert( "Remove at least " + removeNo + " employees");
						 $(this).val("");
					 } else {
						 moveEmptyToLast($("#tripDivContainer_" + myId));
						 
						 for(var ix=vehicle_seats;ix<tripSeats; ix++ ) {
							 $("#tripDivContainer_" + myId).children().last().remove();
						 }
					 }
					 
				}
							 	 
					
					 
				 
	 			
		 }catch(e) {
			 alert(e);
		 }

	}
	
	function moveEmptyToLast(  container ) {
		 
		var emptyIndex=-1;
		var retVal =false;
		try {
			var childLength = $(container).children("div").length;
			var index = 0;
			// alert(childLength);
		 	for(var elementItem=$(container).children("div")[index]; index<childLength; elementItem=$(container).children("div")[index] ) {
		 		
		 		if($(elementItem).hasClass("divEmpty") && emptyIndex==-1 ) {
		 			emptyIndex=index;
		 		} else if( $(elementItem).hasClass("divEmpty") ==false && emptyIndex>0 && emptyIndex!=index) {
		  		//	alert("current index :" + index + " empty index : "  + emptyIndex );
		 			copyItem($(container).children("div")[emptyIndex], elementItem);
		 			
		 			makeEmpty(elementItem);
		 			emptyIndex=index;
		 		}
				//alert(elementItem);
				index++;
			}
		 	retVal=true;
		 
		}catch(e) {
			alert("Error in moveEmptyToLast " +e);
		}
		return retVal;
		
	}
	
	function makeEmpty( uidraggable) {
		$(uidraggable).removeClass("aplItem"); 
	 	$(uidraggable).addClass("divEmpty");
	 	$(uidraggable).children(".status").val("EMPTY"); 
	 	$(uidraggable).children(".displayText").text("EMPTY-*-");
	 	 
	}
	
	// setting toLog and toTime in javascript
	 function getTripTime()
            {     
            	try{
            	
                var logtype=document.getElementById("fromLog").value;
                if(logtype=="ALL")
                	{
                	var tripTimeId=document.getElementById("tripTimeId");
                	tripTimeId.innerHTML='<select name="fromTime" id="fromTime"> <option value="" >--   Select    --</option></select>';
                	return;
                	}
                var site=document.getElementById("siteId").value;
                var url="GetLogTime?logtype="+logtype+"&site="+site;                                    
                xmlHttp=GetXmlHttpObject();
                if (xmlHttp==null)
                {
                    alert ("Browser does not support HTTP Request");
                    return
                }                    
                xmlHttp.onreadystatechange=setLogTime;	
                xmlHttp.open("POST",url,true);                
                xmlHttp.send(null);
            }catch(e){alert(e);}
            }
	
     function setLogTime() 
     {                      
         if (xmlHttp.readyState==4 || xmlHttp.readyState=="complete")
         { 
             var returnText=xmlHttp.responseText;
             var tripTimeId=document.getElementById("tripTimeId");
             tripTimeId.innerHTML='<select name="fromTime" id="tripTime"> <option value="" >--  Select  -- </option>'+returnText+'</select>';                                             
         }
     }

     //---second trip time change
     	 function getTripTime1()
            {     
            	try{
            	
                var logtype=document.getElementById("toLog").value;
                if(logtype=="ALL")
                	{
                	var tripTimeId1=document.getElementById("tripTimeId1");
                	tripTimeId1.innerHTML='<select name="toTime" id="toTime"> <option value="" >--   Select    --</option></select>';
                	return;
                	}
                var site=document.getElementById("siteId").value;
                var url="GetLogTime?logtype="+logtype+"&site="+site;                                    
                xmlHttp=GetXmlHttpObject();
                if (xmlHttp==null)
                {
                    alert ("Browser does not support HTTP Request");
                    return
                }                    
                xmlHttp.onreadystatechange=setLogTime1;	
                xmlHttp.open("POST",url,true);                
                xmlHttp.send(null);
            }catch(e){alert(e);}
            }
	
     function setLogTime1() 
     {                      
         if (xmlHttp.readyState==4 || xmlHttp.readyState=="complete")
         { 
             var returnText=xmlHttp.responseText;
             var tripTimeId1=document.getElementById("tripTimeId1");
             tripTimeId1.innerHTML='<select name="toTime" id="toTime"> <option value="" >--  Select  -- </option>'+returnText+'</select>';                                             
         }
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
            
            // validation 
            function validateForm1() {
            	var flag = true;
            	if($("#siteId").val().trim() == "") {
            		alert("Site is not selected");
            		flag=false;
            	} else if($("#fromDate").val().trim() == "") {
            		alert("From date is not selected");
            		flag=false;
            	} else if($("#fromLog").val().trim() == "") {
            		alert("From Log is not selected");
            		flag=false;
            	} else if($("#fromTime").val().trim() == "") {
            		alert("From Time is not selected");
            		flag=false;
            	} else if($("#toDate").val().trim() == "") {
            		alert("To date is not selected");
            		flag=false;
            	} else if($("#toLog").val().trim() == "") {
            		alert("To log is not selected");
            		flag=false;
            	} else if($("#toTime").val().trim() == "") {
            		alert("To time is not selected");
            		flag=false;
            	} 
            	
            	return flag;
            }
            
            function validateForm2() {
            	var flag = true;
            	$('.vehicleSelect').each(function() {
            	if($(this).val().trim()=="") {
            		alert("Some vehicles are not selected ");
            		flag = false;
            	}            	
            	});
            	return flag;
            }
       



</script>

<head> 
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Trip Sheet Mirroring</title>


 </head>
<body onload="displayaddress()">

	<%
		try {
			long empid = 0;
			String employeeId = OtherFunctions.checkUser(session);
			empid = Long.parseLong(employeeId);
	%>
	<%@include file="../../Header.jsp"%>
	<%
		OtherDao ob = null;
			ob = OtherDao.getInstance();
			 
	%>
	<div id="body">
		<div class="content">
		<div >
  <div>		
  		<div id="formDiv" > 	 
 			<form:form id="getForm" method="post"    
   modelAttribute="tripRegen" action="gettripgenaccept.do" onsubmit="return validateForm1()">
   	 
   	 		<span>
	   			 <form:label path="siteId">Site  </form:label>
   	 			<form:select items="${sites }" var="site" path="siteId" onchange="getTripTime()" > 	 </form:select>
	   		</span>
   			 <span>
	   			 <form:label path="fromDate">From Date</form:label> 
	   			 <form:input path="fromDate"/>
	   			    
   			 </span> 
   			 <span>
	   			 <form:label path="fromLog" >From Log </form:label>
	   			 <form:select path="fromLog" onchange="getTripTime()" >
	   			 	<form:option value="">--- Select ---</form:option>
	   			 	<form:option value="IN">--- IN ---</form:option>
	   			 	<form:option value="OUT">--- OUT ---</form:option>
	   			 </form:select>
   			 </span>
   			 <span>
	   			 <form:label path="fromTime" >From Time</form:label>
	   			 <span id="tripTimeId" >
		   			 <form:select  path="fromTime" items="${fromTimeMap}" >
		   			 	<form:option value="">--- Select ---</form:option>
		   			 </form:select>
	   			 </span>
   			 </span>
   			  <span>
	   			 <form:label path="toDate">To Date</form:label> 
	   			 <form:input path="toDate"/>
   			 </span> 
   			 <span>
	   			 <form:label path="toLog" >To Log</form:label>
	   			 <form:select path="toLog" onchange="getTripTime1()" >
	   			 	<form:option value="">-- Select --</form:option>
	   			 	<form:option value="IN">-- IN --</form:option>
	   			 	<form:option value="OUT">-- OUT --</form:option>
	   			 </form:select>
	   			 
   			 </span>
   			 <span>
	   			 <form:label path="toTime" >To Time</form:label>
	   			 <span id = "tripTimeId1">
	   			 	<form:select path="toTime"  items="${toTimeMap}" >
	   			 		<form:option value="">---Select---</form:option>
	   			 	</form:select>
	   			 </span>
	   			 
   			 </span>
   
   			 <span>
   			  
   			 	<form:button>Generate</form:button>
   			 </span>
   
   
     
      </form:form>
      </div>
      <form:form id="tripForm"  modelAttribute="tripForm" action="tripgensubmit.do" onsubmit="return validateForm2()" >
		
		 	 
 	 
			 
	   			   
	   				 <form:hidden   id="site_id" path="siteId" />
	   				 <form:hidden   id="trip_date" path="trip_date" />
	   				 <form:hidden  id="trip_log"  path="trip_log" />
	   				 <form:hidden  id="trip_time"  path="trip_time" />
	   			 	
	   			   
   		<c:if test="${ tripForm.tripList!=null && fn:length(tripForm.tripList) gt 0}">
   		<div id="formDiv1" >	    
<span>
   			  
   			 	<form:button class="frombutton">Save</form:button>
   			 </span>
   			 <span>
   			  
   			 	<input type="button" id="createNew" value="New" class="frombutton"> 
   			 </span>
   		</div>
   		</c:if>
   
     
    
      
         
      
      <div id="oprArea">
	      	<div id="oprDiv">
	      	<c:forEach items="${vehicleTypeList}" var="vehicleType" >
	      		<input type="hidden" id="vehicle_${vehicleType.id }" value="${vehicleType.sittingCopacity}" />
	      	</c:forEach>
		      	<c:forEach items="${tripForm.tripList}" var="item" varStatus="itemStatus" >
		      		
			      		  <div class="tripDiv">
			      		  		<div id="tripHeaderDiv_${itemStatus.index }">
			      		  		<form:hidden path="tripList[${itemStatus.index }].trip_time"/>
			      		  		<form:hidden path="tripList[${itemStatus.index }].trip_date"/>
			      		  		<form:hidden path="tripList[${itemStatus.index }].trip_log"/>
			      		  		<form:hidden path="tripList[${itemStatus.index }].siteId"/>
			      		  		<form:hidden path="tripList[${itemStatus.index }].distance"/>
			      		  		<form:hidden path="tripList[${itemStatus.index }].travelTime"/>
			      		  		<form:hidden id="status_${itemStatus.index }" path="tripList[${itemStatus.index }].status" />
			      		  		 
			      		  		<h4> Trip ${itemStatus.index }</h4>
			      		  			<form:label path="tripList[${itemStatus.index }].vehicleTypeId">Vehicle</form:label>
			      		  			<form:select class="vehicleSelect"  items="${vehicleTypeMap }" id="tripVehicleId_${itemStatus.index }" path="tripList[${itemStatus.index }].vehicleTypeId" >
			      		  				 
			      		  			</form:select>
			      		  			<form:label path="tripList[${itemStatus.index }].isSecurity">Escort</form:label>
			      		  			<form:select class="isSecurity"  items="${securityLabels }" id="isSecurity_${itemStatus.index }" path="tripList[${itemStatus.index }].isSecurity" >
			      		  				 
			      		  			</form:select>
			      		  			
			      		  		</div>
			      		  		<div id="tripDivContainer_${itemStatus.index }" class="tripChildContainer">
			      		  			
			      		  			<c:forEach items="${item.tripDetailsChildDtoList}" var="childItem" varStatus="childItemStatus"  >
			      		  				 <div class="${childItem.status=='EMPTY'?'divEmpty':'aplItem'}" id="item_${itemStatus.index}-${childItemStatus.index}"  >
			      		  				 	<form:hidden class="status" path="tripList[${itemStatus.index }].tripDetailsChildDtoList[${childItemStatus.index}].status"/>
			      		  				 	<form:hidden class="employeeId" path="tripList[${itemStatus.index }].tripDetailsChildDtoList[${childItemStatus.index}].employeeId"/>
			      		  				 	<form:hidden class="landmarkId" path="tripList[${itemStatus.index }].tripDetailsChildDtoList[${childItemStatus.index}].landmarkId"/>
			      		  				 	<form:hidden class="scheduleId" path="tripList[${itemStatus.index }].tripDetailsChildDtoList[${childItemStatus.index}].scheduleId"/>
			      		  				 	<form:hidden class="tcdistance" path="tripList[${itemStatus.index }].tripDetailsChildDtoList[${childItemStatus.index}].distance"/>
			      		  				 	<form:hidden class="tctime" path="tripList[${itemStatus.index }].tripDetailsChildDtoList[${childItemStatus.index}].time"/>			      		  				 	
			      		  				 	<span class="displayText">${childItem.employeeName} (${childItem.personnelNo}). ${childItem.place}->${childItem.landmark}</span>
			      		  				 </div>
			      		  				 
			      		  			</c:forEach>
			      		  		</div>
			      	
		      		</div>
		      	</c:forEach>
		      	
	      	</div>
      		 
      		
	      	<div id="employeePool">
	      	
	      		 
			      		  
			      		  		 
			      		  		 
			      		  		 
			      	  		 <c:forEach items="${employeePool}"  var="empItem"  >
			      		  		 		 <div class="aplItem"  >
			      		  				 	<input type="hidden" class="status"  /> 
			      		  				 	<input type="hidden" class="landmarkId"   value="${empItem.landmarkId}" />
			      		  				 	<input type="hidden" class="employeeId"  value="${empItem.employeeId}" />
			      		  				 	<input type="hidden" class="scheduleId"  value="${empItem.scheduleId}" />
			      		  				 	<input type="hidden" class="tcdistance"  value="${empItem.distance}" />
			      		  				 	<input type="hidden" class="tctime"  value="${empItem.time}" />  
			      		  				 	 <span class="displayText">${empItem.employeeName} (${empItem.personnelNo}).  ${empItem.place}->${empItem.landmark}</span>
			      		  				 	 
			      		  				 	 
			      		  				 </div>
			      		  		 
			      		  		 </c:forEach>
			     
			      		  		  
	      		 	      		
	      	</div>
	      	 
      	</div>
      	
      </form:form>
        	 
 	</div>	 
			</div>
			
			<%
				} catch (Exception e) {
					System.out.println("Error" + e);
				}
			%>
		</div>
	</div>
 
</body>
</html>
