<%@ page contentType="text/html; charset=utf-8" language="java" import="java.sql.*" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml"><!-- InstanceBegin template="/Templates/etms_temp.dwt.jsp" codeOutsideHTMLIsLocked="false" -->
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<!-- InstanceBeginEditable name="doctitle" -->
<title>Driver Registration</title>


<!-- InstanceEndEditable -->
<%@page import="java.util.*"%>
<%@page import="java.util.Date"%>
<%@page import="java.text.*"%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link href="css/style.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" type="text/css" href="css/coin-slider.css" />
<script src="jquery-1.7.1.min.js" type="text/javascript"></script>
<script src="menu-collapsed.js" type="text/javascript"></script>
<script type="text/javascript" src="js/cufon-yui.js"></script>
<script type="text/javascript" src="js/cufon-titillium-250.js"></script>
<script type="text/javascript" src="js/jquery-1.4.2.min.js"></script>
<script type="text/javascript" src="js/script.js"></script>
<script type="text/javascript" src="jquery-latest.js"></script>
<script type="text/javascript" src="jquery.validate.js"></script>
<script type="text/javascript" src="js/coin-slider.min.js"></script>

<!-- Beginning of compulsory code below -->
<link href="css/dropdown/dropdown.css" media="screen" rel="stylesheet" type="text/css" />

<link href="css/dropdown/dropdown.vertical.css" media="screen" rel="stylesheet" type="text/css" />
<link href="css/dropdown/themes/default/default.advanced.css" media="screen" rel="stylesheet" type="text/css" />


<!--[if lte IE 7]>
<script type="text/javascript" src="js/jquery/jquery.js"></script>
<script type="text/javascript" src="js/jquery/jquery.dropdown.js"></script>
<![endif]-->
     <script type="text/javascript">
        function showPopup(url) {
newwindow=window.open(url,'name','height=190,width=250,top=200,left=300,resizable=no,toolbars=no,menubar=no,location=no');
if (window.focus) {newwindow.focus()}
}
</script>
<!-- InstanceBeginEditable name="head" -->
<script type='text/JavaScript' src='scw.js'></script>  
<script src="/jquery.js" type="text/javascript"></script>
<script type="text/javascript">
    $(document).ready(function() {
      $("#driver_regn_form").validate({
        rules: {
          driver_name: {
		  		 required : true,
				 maxlength : 30
				 },
		  dob: {
		  		 required : true
				 },
		  father_name: {
		  		 required : true,
				 maxlength : 30
				 },
		  curr_addr: {
		  		 required : true,
				 maxlength : 100
				 },
		  per_addr: {
		  		 required : true,
				 maxlength : 100
				 },
		  curr_area: {
		  		 required : true
				 },
		  per_area: {
		  		 required : true
				 },
		  curr_place: {
		  		 required : true
				 },
		  per_place: {
		  		 required : true
				 },
		  curr_landmark: {
		  		 required : true
				 },
		  per_landmark: {
		  		 required : true
				 },
		  curr_pincode: {
		  		 required : true,
				 minlength : 6,
				 maxlength : 6,
				 number : true
				 },
		  per_pincode: {
		  		 required : true,
				 minlength : 6,
				 maxlength : 6,
				 number : true
				 },
		  licence_number: {
		  		 required : true,
				 maxlength : 30
				 },
		  rto: {
		  		 required : true,
				 maxlength : 40
				 },
		  date_of_issue: {
		  		 required : true
				 },
		  licence_start: {
		  		 required : true
				 },
		  licence_end: {
		  		 required : true
				 },
		  vendor: {
		  		 required : true
				 },
		  vehicle_selected: {
		  		 required : true,
				 maxlength : 3
				 },
		  employed_from: {
		  		 required : true
				 },
		  },
		 messages: {
		  driver_name : {
				required: "Please enter driver name",
				maxlength: "Maximum 30 characters"
			},
		  dob: {
				required: "Date of birth required"
			},
		  father_name : {
				required: "Please enter father name",
				maxlength: "Maximum 30 characters"
			},
		  curr_addr : {
				required: "Please enter current address",
				maxlength: "Maximum 100 characters"
			},
		  per_addr : {
				required: "Please enter Permanant address",
				maxlength: "Maximum 100 characters"
			},
		  curr_area : {
				required: "Current area required"
			},
		  per_area : {
				required: "Permanant area required"
			},
		  curr_place : {
				required: "Current place required"
			},
		  per_place : {
				required: "Permanant place required"
			},
		  curr_landmark : {
				required: "Current landmark required"
			},
		  per_landmark : {
				required: "Permanant landmark required"
			},
		  curr_pincode : {
				required: "Current pincode required",
				maxlength: "Pincode is 6 characters",
				maxlength: "Pincode is 6 characters"
			},
		  per_pincode : {
				required: "Permanant pincode required",
				minlength: "Pincode is 6 characters",
				maxlength: "Pincode is 6 characters"
			},
		  addr_proof : {
				required: "Address proof required"
			},
		  id_proof: {
				required: "Id proof required"
			},
	      back_proof: {
				required: "Background proof required"
			},
	   	  photo: {
				required: "Photo required"
			},
		  licence_proof: {
				required: "Licence proof required"
			},
		  licence_number : {
				required: "Please enter licence number",
				maxlength: "Maximum 30 characters",
				number: "Please enter numbers only"
			},
		  rto : {
				required: "Please enter RTO",
				maxlength: "Maximum 40 characters"
			},
		  date_of_issue: {
				required: "Please enter licence issued date"
			},
		  licence_start: {
				required: "Licence start date required"
			},
		  licence_end: {
				required: "Licence end date required"
			},
		  vendor: {
				required: "Please select vendor"
			},
		  vehicle_selected: {
				required: "Select vehicle for driver",
				maxlength: "Maximum 3 vehicle allowed"
			},
		  employed_from: {
				required: "Employed date required"
			}
         }
      });
    });
  </script>
  <style type="text/css">
<!--
 label.error { width: 250px; display: block; float: right; color: red; padding-left: 10px; }
-->
</style>
<!-- InstanceEndEditable -->
<style type="text/css">
<!--
.style1 {
	font-family: "Book Antiqua";
	font-size: 24px;
	font-weight: bold;
	color: #333333;
	font-style: italic;
}
.style7 {color: #FFFFFF}
.style8 {
	font-size: 24px;
	font-style: italic;
	color: #FF0000;
}
.style12 {
	color: #FFFFFF
}
.style16 {
	font-family: "Book Antiqua";
	font-size: 24px;
	color: #FFFFFF;
	font-weight: bold;
	font-style: italic;
}
.style19 {font-size: 26px}
.style21 {
	color: #FFFFFF;
	font-size: 26px;
}
.style31 {
	font-family: "Book Antiqua";
	font-size: 24px;
	color: #000000;
	font-weight: bold;
}
.style32 {
	font-family: "Book Antiqua";
	font-size: 24px;
	color: #FF0000;
	font-weight: bold;
	font-style: italic;
}
.style37 {font-family: Arial; font-size: 12px; font-weight: bold; color: #000000; }
.style38 {color: #000000}
.style39 {font-size: 12px; font-weight: bold; font-family: Arial;}
-->
</style>

        <script type="text/javascript">
            function checkDate()
            {
				var dob = new Date(document.getElementById('dob').value)
				var date_of_issue = new Date(document.getElementById('date_of_issue').value)
				var licence_start = new Date(document.getElementById('licence_start').value)
				var licence_end = new Date(document.getElementById('licence_end').value)
				var employed_from = new Date(document.getElementById('employed_from').value)
				var cur = new Date();
				if(Date.parse(cur) < Date.parse(dob))
				{
					alert('Invalid Birth Date!')
					return false;
				}
				if(Date.parse(cur) < Date.parse(date_of_issue))
				{
					alert('Invalid Licence Issuing Date!')
					return false;
				}
				if(Date.parse(licence_start) > Date.parse(licence_end))
				{
					alert('Licence start date cannot be after end date!')
					return false;
				}
				if(Date.parse(cur) < Date.parse(licence_start))
				{
					alert('Licence start date cannot be after current date!')
					return false;
				}
				if(Date.parse(cur) > Date.parse(licence_end))
				{
					alert('Licence expired!')
					return false;
				}
				if(Date.parse(employed_from) > Date.parse(cur))
				{
					alert('Employed from date is Invalid!')
					return false;
				}
                return true;

            }
		</script>	
		<script type="text/javascript">
            $().ready(function() {
                $('#add').click(function() {
                    return !$('#vehicle option:selected').remove().appendTo('#vehicle_selected');
                });
                $('#remove').click(function() {
                    return !$('#vehicle_selected option:selected').remove().appendTo('#vehicle');
                });
            });
            $('form').submit(function() {
                $('#vehicle_selected option').each(function(i) {
                    $(this).attr("selected", "selected");
                });
            });
        </script>
      
      <script type="text/javascript">

        function showcurrplace(curr_area)
        {
            if(document.getElementById("curr_area").value!="")
            {
                xmlHttp=GetXmlHttpObject()
                if (xmlHttp==null)
                {
                    alert ("Browser does not support HTTP Request")
                    return
                }
                var url="get_curr_place.jsp";
                url=url+"?curr_area="+curr_area;		
                xmlHttp.onreadystatechange=stateChanged_curr_area
				
                xmlHttp.open("GET",url,true)		
                xmlHttp.send(null)
            }
            else
            {
                alert("Please Select current area-- ");
            }
        }
        
        function stateChanged_curr_area() 
        { 	
            if (xmlHttp.readyState==4 || xmlHttp.readyState=="complete")
            { 
                document.getElementById("curr_place").innerHTML=xmlHttp.responseText 
            }
        }
        
        function showcurrlandmark(curr_place)
        {
            if(document.getElementById("curr_place").value!="")
            {
                xmlHttp=GetXmlHttpObject()
                if (xmlHttp==null)
                {
                    alert ("Browser does not support HTTP Request")
                    return
                }
                var url="get_curr_landmark.jsp";
                url=url+"?curr_place="+curr_place;		
                xmlHttp.onreadystatechange=stateChanged_curr_place
				
                xmlHttp.open("GET",url,true)		
                xmlHttp.send(null)
            }
            else
            {
                alert("Please Select current place-- ");
            }
        }
        
        function stateChanged_curr_place() 
        { 	
            if (xmlHttp.readyState==4 || xmlHttp.readyState=="complete")
            { 
                document.getElementById("curr_landmark").innerHTML=xmlHttp.responseText 
            }
        }
		
        function showperplace(per_area)
        {
            if(document.getElementById("per_area").value!="")
            {
                xmlHttp=GetXmlHttpObject()
                if (xmlHttp==null)
                {
                    alert ("Browser does not support HTTP Request")
                    return
                }
                var url="get_per_place.jsp";
                url=url+"?per_area="+per_area;		
                xmlHttp.onreadystatechange=stateChanged_per_area
				
                xmlHttp.open("GET",url,true)		
                xmlHttp.send(null)
            }
            else
            {
                alert("Please Select Permanant area-- ");
            }
        }
        
        function stateChanged_per_area() 
        { 	
            if (xmlHttp.readyState==4 || xmlHttp.readyState=="complete")
            { 
                document.getElementById("per_place").innerHTML=xmlHttp.responseText 
            }
        }
        
        function showperlandmark(per_place)
        {
            if(document.getElementById("per_place").value!="")
            {
                xmlHttp=GetXmlHttpObject()
                if (xmlHttp==null)
                {
                    alert ("Browser does not support HTTP Request")
                    return
                }
                var url="get_per_landmark.jsp";
                url=url+"?per_place="+per_place;		
                xmlHttp.onreadystatechange=stateChanged_per_place
				
                xmlHttp.open("GET",url,true)		
                xmlHttp.send(null)
            }
            else
            {
                alert("Please Select Permanant place-- ");
            }
        }
        
        function stateChanged_per_place() 
        { 	
            if (xmlHttp.readyState==4 || xmlHttp.readyState=="complete")
            { 
                document.getElementById("per_landmark").innerHTML=xmlHttp.responseText 
            }
        }

		
        

        function showvehicles(vendor)
        {
            if(document.getElementById("vendor").value!="")
            {
                xmlHttp=GetXmlHttpObject()
                if (xmlHttp==null)
                {
                    alert ("Browser does not support HTTP Request")
                    return
                }
                var url="getvehicle.jsp";
                url=url+"?vendor="+vendor;		
                xmlHttp.onreadystatechange=stateChanged
				
                xmlHttp.open("GET",url,true)		
                xmlHttp.send(null)
            }
            else
            {
                alert("Please Select vendor-- ");
            }
        }
        
        function stateChanged() 
        { 	
            if (xmlHttp.readyState==4 || xmlHttp.readyState=="complete")
            { 
                document.getElementById("vehicle").innerHTML=xmlHttp.responseText 
            }
        }
        
        function GetXmlHttpObject()
        {
            var xmlHttp=null;
            if (window.XMLHttpRequest) 
            {
                xmlHttp=new XMLHttpRequest();
            }
            //catch (e)
            else if (window.ActiveXObject) 
            { 
                xmlHttp=new ActiveXObject("Microsoft.XMLHTTP");
            }

            return xmlHttp;
        }
    </script>          


</head>

<body>
<div id='body'>
		<div class='content'>
		<%@include file="Header.jsp"%>
		<p><h1>Add Driver</h1></p>
		<form action="DriverRegn" name="driver_regn_form" id="driver_regn_form"  method="post" enctype="multipart/form-data" onsubmit="return checkDate()">
            <table  border="0">
                <%
                  String success = "" + session.getAttribute("success");
                  if (success.equals("null")) {
                      success = "";
                  }
              %>
              <tr>
                  <td></td>
                  <td width="255"><h2><%=success%><%session.setAttribute("success", "");%></h2></td>
              </tr>
                <tr>
                    <td width="146">&nbsp;</td>
                  <td width="255">&nbsp;</td>
                  <td width="302">&nbsp;</td>
              </tr>
                    
                <tr>
                    <td><strong>Driver Name</strong></td>
                    <td><input type="text" name="driver_name" id="driver_name"/></td>
                    <td></td>
                </tr>
                    
                <tr>
                    <td><strong>DOB</strong></td>
                    <td><input type="text" name="dob" id="dob"onkeyup="scwShow(this,event);" onclick="scwShow(this,event);"/></td>
                    <td>&nbsp;</td>
                </tr>
                    
                <tr>
                    <td><strong>Father Name</strong></td>
                    <td><input type="text" name="father_name" id="father_name" onclick="checkBirthDate()"/></td>
                    <td>&nbsp;</td>
                </tr>
                    
                <tr>
                    <td></td>
                    <td><strong>Current</strong></td>
                    <td><strong>Permanant</strong></td>
                </tr>
                    
                <tr>
                    <td><strong>Address</strong></td>
                    <td><input type="text" name="curr_addr" id="curr_addr" /></td>
                    <td><input type="text" name="per_addr" id="per_addr" /></td>
                </tr>
                    
                <tr>
                    <td><strong>Area</strong></td>
                    <td>
                        <select name="curr_area" id="curr_area" onchange="showcurrplace(this.value)" >
                            <option value="" >Select</option>
                                                       
                        </select>                    </td>
                    <td>
                        <input type="text" name="per_area" id="per_area"/>                    </td>
                </tr>
                    
                <tr>
                    <td><strong>Place</strong></td>
                    <td>
                        <div id="curr_place"><select name="curr_place">
                            <option value="" >Select</option>
                        </select>
                        </div>                    </td>
                    <td>
                        <input type="text" name="per_place" id="per_place">                    </td>
                </tr>
                    
                <tr>
                    <td><strong>Landmark</strong></td>
                    <td>
                        <select name="curr_landmark" id="curr_landmark">
                            <option value="" >Select</option>
                        </select>                    </td>
                    <td>
                        <input type="text" name="per_landmark" id="per_landmark">                    </td>
                </tr>
                    
                <tr>
                    <td><strong>Pincode</strong></td>
                    <td><input type="text" name="curr_pincode" id="curr_pincode" /></td>
                    <td><input type="text" name="per_pincode" id="per_pincode" /></td>
                </tr>
                </tr>
                    
                <tr>
                    <td><strong>Address Proof</strong></td>
                    <td><input type="file" name="addr_proof" id="addr_proof" /></td>
                    <td><select name="addr_proof_id" id="addr_proof_id" >
                            
                        </select>                    </td>
                </tr>
                    
                <tr>
                    <td><strong>Identity Proof</strong></td>
                    <td><input type="file" name="id_proof" id="id_proof"/></td>
                </tr>
                    
                <tr>
                    <td><strong>Back Ground Check Proof</strong></td>
                    <td><input type="file" name="back_proof" id="back_proof"/></td>
                </tr>
                    
                <tr>
                    <td><strong>Photo</strong></td>
                    <td><input type="file" name="photo" id="photo"/></td>
                </tr>
                    
                <tr>
                    <td></td>
                    <td><h3>Licence Details</h3></td>
                </tr>
                <tr>
                    <td><strong>Proof</strong></td>
                    <td><input type="file" size="20" name="licence_proof" id="licence_proof"/></td>
                    <td><strong>Number&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</strong><input type="text" name="licence_number" id="licence_number" /></td>
                </tr>
                    
                <tr>
                    <td><strong>RTO</strong></td>
                    <td><input type="text" name="rto" id="rto" /></td>
                    <td><strong>Date Of Issue</strong><input type="text" name="date_of_issue" id="date_of_issue" onkeyup="scwShow(this,event);" onclick="scwShow(this,event);"/></td>
                </tr>
                    
                <tr>
                    <td><strong>Start Date</strong></td>
                    <td><input type="text" name="licence_start" id="licence_start" onkeyup="scwShow(this,event);" onclick="scwShow(this,event);"/></td>
                    <td><strong>End Date </strong>
                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="text" name="licence_end" id="licence_end" onkeyup="scwShow(this,event);" onclick="scwShow(this,event);"/></td>
              </tr>
                    
                <tr>
                    <td></td>
                    <td><h3>Vendor Details</h3></td>
                </tr>
                    
                <tr>
                    <td><strong>Vendor</strong></td>
                    <td><select name="vendor" id="vendor" onchange="showvehicles(this.value)" >
                            <option value="" >Select</option>
                            
                        </select>                    </td>
                </tr>
                    
                <tr>
                    <td><strong>Vehicle</strong></td>
                    <td><select multiple="multiple" name="vehicle" id="vehicle" >
                        </select>                    </td>
                    <td>
                        <a href="#" id="add">add >></a>&nbsp;&nbsp;&nbsp;
                        <a href="#" id="remove"><< remove</a>                    </td>
                    <td width="39"><select multiple name="vehicle_selected" id="vehicle_selected">
                        </select>                    </td>
              <tr>
                <tr>
                    <td><strong>Employed From</strong></td>
                    <td><input type="text" name="employed_from" id="employed_from" onkeyup="scwShow(this,event);" onclick="scwShow(this,event);" /></td>
                </tr>
                <tr>
                    <td>&nbsp;</td>
                    <td><input type="submit" value="Add" name="addbutton" /> &nbsp; <input type="reset" value="Reset" name="resetbuttobn" /></td>
                    <td></td>
                </tr>
            </table>
    </form>


<%@include file="Footer.jsp"%>
	</div>
	</div>
</body>
</html>
