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

<script type='text/JavaScript' src='scw.js'></script>
<script type="text/javascript">
    $(document).ready(function() {
      $("#vehicle_regn_form").validate({
        rules: {
          vendor: {
		  		 required : true
				 },
		  owner: {
		  		 required : true,
				 maxlength : 40
				 },
		  state_code: {
		  		 required : true
				 },
		  state_number: {
		  		 required : true
				 },
		  regn_code: {
		  		 required : true
				 },
		  regn_number: {
		  		 required : true
				 },
		  regn_date: {
		  		 required : true
				 },
		  permitfrom: {
		  		 required : true
				 },
		  permitto: {
		  		 required : true
				 },
		  insurancefrom: {
		  		 required : true
				 },
		  insuranceto: {
		  		 required : true
				 },
		  taxfrom: {
		  		 required : true
				 },
		  taxto: {
		  		 required : true
				 },
		  pollutionfrom: {
		  		 required : true
				 },
		  pollutionto: {
		  		 required : true
				 },
		  contract_type: {
		  		 required : true
				 },
		  type: {
		  		 required : true
				 }
         },
		 messages: {
		  vendor : {
				required: "Please select a vendor"
			},
		  owner: {
				required: "Please enter owner name",
				maxlength: "Maximum 40 characters"
			},
		  state_code : {
				required: "Please select a state code"
			},
		  state_number : {
				required: "Please select a district code"
			},
		  regn_code : {
				required: "Please select a regn code"
			},
		  regn_number : {
				required: "Please select a regn number"
			},
		  regn_date : {
				required: "Registration date required"
			},
		  rc : {
				required: "Registration certificate required"
			},
		  permitfrom : {
				required: "Permit start date required"
			},
		  permitto : {
				required: "Permit end date required"
			},
		  permit_cert : {
				required: "Permit certificate required"
			},
		  insurancefrom : {
				required: "Insurance start date required"
			},
		  insuranceto : {
				required: "Insurance end date required"
			},
		  insurance_cert : {
				required: "Insurance certificate required"
			},
		  taxfrom : {
				required: "Tax start date required"
			},
		  taxto : {
				required: "Tax end date required"
			},
		  tax_cert : {
				required: "Tax certificate required"
			},
		  pollutionfrom : {
				required: "Pollution start date required"
			},
		  pollutionto : {
				required: "Pollution end date required"
			},
		  pollution_cert : {
				required: "Pollution certificate required"
			},
		  contract_type : {
				required: "Contract type required"
			},
		  rate : {
				required: "Rate required"
			},
		  type : {
				required: "Vehicle type required"
			}
		}
      });
    });
  </script>
    <script type="text/javascript">
            function checkDate()
            {
				var regn_date = new Date(document.getElementById('regn_date').value)
				var permitfrom = new Date(document.getElementById('permitfrom').value)
				var permitto = new Date(document.getElementById('permitto').value)
				var insurancefrom = new Date(document.getElementById('insurancefrom').value)
				var insuranceto = new Date(document.getElementById('insuranceto').value)
				var taxfrom = new Date(document.getElementById('taxfrom').value)
				var taxto = new Date(document.getElementById('taxto').value)
				var pollutionfrom = new Date(document.getElementById('pollutionfrom').value)
				var pollutionto = new Date(document.getElementById('pollutionto').value)
				var cur = new Date();
				
				if(Date.parse(cur) < Date.parse(regn_date))
				{
					alert('Invalid Registration Date!')
					return false;
				}
				if(Date.parse(cur) < Date.parse(permitfrom) || Date.parse(regn_date) > Date.parse(permitfrom))
				{
					alert('Permit \'from\' date cannot be after current date and before \'registration\' date!')
					return false;
				}
				if(Date.parse(cur) > Date.parse(permitto))
				{
					alert('Permit expired!')
					return false;
				}
				if(Date.parse(cur) < Date.parse(insurancefrom) || Date.parse(regn_date) > Date.parse(insurancefrom))
				{
					alert('Insurance \'from\' date cannot be after current date and before \'registration\' date!')
					return false;
				}
				if(Date.parse(cur) > Date.parse(insuranceto))
				{
					alert('Insurance expired!')
					return false;
				}
				if(Date.parse(cur) < Date.parse(taxfrom) || Date.parse(regn_date) > Date.parse(taxfrom))
				{
					alert('Tax \'from\' date cannot be after current date and before \'registration\' date!')
					return false;
				}
				if(Date.parse(cur) > Date.parse(taxto))
				{
					alert('Tax expired!')
					return false;
				}
				if(Date.parse(cur) < Date.parse(pollutionfrom) || Date.parse(regn_date) > Date.parse(pollutionfrom))
				{
					alert('Pollution \'from\' date cannot be after current date and before \'registration\' date!')
					return false;
				}
				if(Date.parse(cur) > Date.parse(pollutionto))
				{
					alert('Pollution expired!')
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
        function showseater(vehicle_type)
        {
            if(document.getElementById("type").value!="")
            {
                xmlHttp=GetXmlHttpObject()
                if (xmlHttp==null)
                {
                    alert ("Browser does not support HTTP Request")
                    return
                }
                var url="getseater.jsp";
                url=url+"?type="+vehicle_type;		
                xmlHttp.onreadystatechange=stateChanged1
		
                xmlHttp.open("GET",url,true)		
                xmlHttp.send(null)
            }
            else
            {
                alert("Please Select vehicle_type-- ");
            }
        }
			
        function stateChanged1() 
        { 	
            if (xmlHttp.readyState==4 || xmlHttp.readyState=="complete")
            { 
            	document.getElementById("seat").value=xmlHttp.responseText 
            }
        }
		
		function checkvehicle()
        {
            if(document.getElementById("state_code").value!="" && document.getElementById("state_number").value!="" && document.getElementById("regn_code").value!="" && document.getElementById("regn_number").value!="")
            {
				var state_code = document.getElementById("state_code").value
				var state_number = document.getElementById("state_number").value
				var regn_code = document.getElementById("regn_code").value
				var regn_number = document.getElementById("regn_number").value
				
                xmlHttp=GetXmlHttpObject()
                if (xmlHttp==null)
                {
                    alert ("Browser does not support HTTP Request")
                    return
                }
                var url="getcheckvehicle.jsp";
                url=url+"?state_code="+state_code+"&&state_number="+state_number+"&&regn_code="+regn_code+"&&regn_number="+regn_number+"&&id=null";		
                xmlHttp.onreadystatechange=stateChanged2
		
                xmlHttp.open("GET",url,true)		
                xmlHttp.send(null)
            }
            else
            {
                
            }
        }
			
        function stateChanged2() 
        { 	
            if (xmlHttp.readyState==4 || xmlHttp.readyState=="complete")
            { 
            	document.getElementById("regn_error").innerHTML=xmlHttp.responseText 
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


</head>

<body>
<div id='body'>
		<div class='content'>
		<%@include file="Header.jsp"%>
		<p><h1>Add Vehicle</h1></p>
<form action="VehicleRegn" name="vehicle_regn_form" id="vehicle_regn_form"  method="post" enctype="multipart/form-data" onsubmit="return checkDate()">
            <table style="width: 50%" border="0">
                <%
                  String success = "" + session.getAttribute("success");
                  if (success.equals("null")) {
                      success = "";
                  }
              %>
              <tr>
                  <td></td>
                  <td width="400"><h2><%=success%><%session.setAttribute("success", "");%></h2></td>
              </tr>
                <tr>
                    <td width="133">&nbsp;</td>
                    <td width="218">&nbsp;</td>
                    <td width="235">&nbsp;</td>
                </tr>
                <tr>
                    <td>&nbsp;</td>
                    <td>&nbsp;</td>
                    <td>&nbsp;</td>
                </tr>
                <tr>
                    <td><strong>Vendor</strong></td>
                    <td><select name="vendor" id="vendor">
                            <option value="">Select</option>
                              
                        </select></td>
                    <td>&nbsp;</td>
                </tr>
                <tr>
                    <td><strong>Owner</strong></td>
                    <td><label>
                            <input type="text" name="owner" id="owner" />
                        </label></td>
                    <td>&nbsp;</td>
                </tr>
                    
                <tr>
                    <td><strong>Reg No.</strong></td>
                    <td><select name="state_code" id="state_code" onchange="checkvehicle()">
                            <option value="" >Select</option>
							
                        </select>&nbsp;
                        <select name="state_number" id="state_number" onchange="checkvehicle()">
                            <option value="" >Select</option>
							
                        </select>&nbsp;
                        <select name="regn_code" id="regn_code" onchange="checkvehicle()">
                            <option value="" >Select</option>
							
                        </select>&nbsp;
                    </td>
                    <td>
                    	<select name="regn_number" id="regn_number" onchange="checkvehicle()">
                            <option value="" >Select</option>
							<%
                                for (int i = 1; i < 10000; i++) {
                            %>
                            <option value="<%=i%>"><%=i%></option>
                            <%
                                }
                            %>
                        </select>
                    </td>
                </tr>
                <tr>
                	<td></td>
                    <td><h3><p id="regn_error" name="regn_error"></p></h3></td>
                </tr>
                <tr>
                    <td><strong>Date Of Regn</strong></td>
                    <td><label>
                            <input type="text" name="regn_date" id="regn_date" onkeyup="scwShow(this,event);" onclick="scwShow(this,event);" />
                        </label></td>
                    <td>&nbsp;</td>
                </tr>
                <tr>
                    <td><strong>Year Of Manufacture</strong></td>
                    <td><select name="year_of_manufacture" id="year_of_manufacture">
                            
                        </select>&nbsp;
                    </td>
                    <td>&nbsp;</td>
                </tr> 
                <tr>
                    <td><strong>R C</strong></td>
                    <td><input type="file" name="rc" id="rc" /></td>
                    <td>&nbsp;</td>
                </tr>
                <tr>
                    <td><strong>Permit</strong></td>
                    <td align="left">From<input type="text" name="permitfrom" id="permitfrom" onkeyup="scwShow(this,event);" onclick="scwShow(this,event);" /></td>
                    <td  align="right">To<input type="text" name="permitto" id="permitto" onkeyup="scwShow(this,event);" onclick="scwShow(this,event);" /></td>
                </tr>
                <tr>
                    <td></td>  
                    <td><input type="file" name="permit_cert" id="permit_cert" /></td>
                    <td></td>
                </tr>
                <tr>
                    <td><strong>Insurance</strong></td>
                    <td  align="left">From<input type="text" name="insurancefrom" id="insurancefrom" onkeyup="scwShow(this,event);" onclick="scwShow(this,event);" /></td>
                    <td  align="right">To<input type="text" name="insuranceto" id="insuranceto" onkeyup="scwShow(this,event);" onclick="scwShow(this,event);" /></td>
                </tr>
                <tr>
                    <td></td>  
                    <td><input type="file" name="insurance_cert" id="insurance_cert" /></td>
                    <td></td>  
                </tr>
                <tr>
                    <td><strong>Tax</strong></td>
                    <td  align="left">From<input type="text" name="taxfrom" id="taxfrom" onkeyup="scwShow(this,event);" onclick="scwShow(this,event);" /></td>
                    <td  align="right">To<input type="text" name="taxto" id="taxto" onkeyup="scwShow(this,event);" onclick="scwShow(this,event);" /></td>
                </tr>
                <tr>
                    <td></td>   
                    <td><input type="file" name="tax_cert" id="tax_cert" /></td>
                    <td></td>  
                </tr>
                <tr>
                    <td><strong>Pollution Certificate</strong></td>
                    <td  align="left">From<input type="text" name="pollutionfrom" id="pollutionfrom" onkeyup="scwShow(this,event);" onclick="scwShow(this,event);" /></td>
                    <td  align="right">To<input type="text" name="pollutionto" id="pollutionto" onkeyup="scwShow(this,event);" onclick="scwShow(this,event);" /></td>
                </tr>
                <tr>
                    <td></td>  
                    <td><input type="file" name="pollution_cert" id="pollution_cert" /></td>
                    <td></td>  
                </tr>
                <tr>
                    <td><strong>Contract Type</strong></td>
                    <td><select name="contract_type" id="contract_type">
                            <option value="">Select</option>
                            
                        </select>
                    </td>
                </tr>
                <tr>
                    <td><strong>Type</strong></td>
                    <td><select name="type" id="type" onchange="showseater(this.value)">
                            <option value="">Select</option>
					
                        </select></td>
                    <td>&nbsp;</td>
                </tr>
                <tr>
                    <td><strong>Seat</strong></td>
                    <td><input type="text" name="seat" id="seat" value="0" readonly/>Seater</td>
                    <td>&nbsp;</td>
                </tr>
                <tr>
                    <td><strong>Fitness Certificate</strong></td>
                    <td><select name="fc" id="fc">
                            <option value="yes">Yes</option>
                            <option value="no">No</option>
                        </select></td>
                    <td>&nbsp;</td>
                </tr>
                <tr>
                    <td><strong>First Aid</strong></td>
                    <td><select name="firstaid" id="firstaid">
                            <option value="yes">Yes</option>
                            <option value="no">No</option>
                        </select></td>
                    <td>&nbsp;</td>
                </tr>
                <tr>
                    <td><strong>Fire Extinguisher</strong></td>
                    <td><select name="fireexit" id="fireexit">
                            <option value="yes">Yes</option>
                            <option value="no">No</option>
                        </select></td>
                    <td>&nbsp;</td>
                </tr>
                <tr>
                    <td><strong>Tool kit</strong></td>
                    <td><select name="toolkit" id="toolkit">
                            <option value="yes">Yes</option>
                            <option value="no">No</option>
                        </select></td>
                    <td>&nbsp;</td>
                </tr>
                <tr>
                    <td><strong>Seat Belt</strong></td>
                    <td><select name="seatbelt" id="seatbelt">
                            <option value="yes">Yes</option>
                            <option value="no">No</option>
                        </select></td>
                    <td>&nbsp;</td>
                </tr>
                <tr>
                    <td><strong>Mobile</strong></td>
                    <td><select name="mobile" id="mobile"> <option value="yes">Yes</option>
                            <option value="no">No</option>
                        </select></td>
                    <td id="ifmob"></td>
                </tr>
                <tr>
                    <td><strong>Hands Free</strong></td>
                    <td><select name="handsfree" id="handsfree">
                            <option value="yes">Yes</option>
                            <option value="no">No</option>
                        </select></td>
                    <td>&nbsp;</td>
                </tr>
                <tr>
                    <td><strong>Cab Condition</strong></td>
                    <td><select name="cabcondition" id="cabcondition">
                            <option value="yes">Yes</option>
                            <option value="no">No</option>
                        </select></td>
                    <td>&nbsp;</td>
                </tr>
                <tr>
                    <td><strong>Head Light</strong></td>
                    <td><select name="headlight" id="headlight">
                            <option value="yes">Yes</option>
                            <option value="no">No</option>
                        </select></td>
                    <td>&nbsp;</td>
                </tr>
                <tr>
                    <td><strong>Break Light</strong></td>
                    <td><select name="breaklight" id="breaklight">
                            <option value="yes">Yes</option>
                            <option value="no">No</option>
                        </select></td>
                    <td>&nbsp;</td>
                </tr>
                <tr>
                    <td><strong>Indicator</strong></td>
                    <td><select name="indicator" id="indicator">
                            <option value="yes">Yes</option>
                            <option value="no">No</option>
                        </select></td>
                    <td>&nbsp;</td>
                </tr>
                <tr>
                    <td><strong>Parking Light</strong></td>
                    <td><select name="parkinglight" id="parkinglight">
                            <option value="yes">Yes</option>
                            <option value="no">No</option>
                        </select></td>
                    <td>&nbsp;</td>
                </tr>
                <tr>
                    <td><strong>Right Mirror</strong></td>
                    <td><select name="rmirror" id="rmirror">
                            <option value="yes">Yes</option>
                            <option value="no">No</option>
                        </select></td>
                    <td>&nbsp;</td>
                </tr>
                <tr>
                    <td><strong>Sticker</strong></td>
                    <td><select name="sticker" id="sticker">
                            <option value="yes">Yes</option>
                            <option value="no">No</option>
                        </select></td>
                    <td>&nbsp;</td>
                </tr>
                <tr>
                    <td><strong>A/C</strong></td>
                    <td><select name="ac" id="ac">
                            <option value="yes">Yes</option>
                            <option value="no">No</option>
                        </select></td>
                    <td>&nbsp;</td>
                </tr>
                <tr>
                    <td><strong>Spare Tyre</strong></td>
                    <td><select name="sparetyre" id="sparetyre">
                            <option value="yes">Yes</option>
                            <option value="no">No</option>
                        </select></td>
                    <td>&nbsp;</td>
                </tr>
                <tr>
                    <td><strong>Sun Reflection Glass</strong></td>
                    <td><select name="reflectionglass" id="reflectionglass">
                            <option value="yes">Yes</option>
                            <option value="no">No</option>
                        </select>  </td>
                    <td>&nbsp;</td>
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
