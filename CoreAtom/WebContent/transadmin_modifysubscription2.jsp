<%--
    Document   : emp_subscription
    Created on : Aug 28, 2012, 12:51:01 PM
    Author     : 123
--%>

<%@page import="com.agiledge.atom.constants.SettingsConstant"%>
<%@page import="com.agiledge.atom.service.SiteService"%>
<%@page import="com.agiledge.atom.dto.SiteDto"%>
<%@page import="java.util.Date"%>
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
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>TransAdmin-Subscription information modification for
	employees</title>

<link href="css/style.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" type="text/css" href="css/coin-slider.css" />
<link href="css/menu.css" rel="stylesheet" type="text/css" />

<link href="css/validate.css" rel="stylesheet" type="text/css" />

<script type="text/javascript" src="js/jquery-latest.js"></script>

<script type="text/javascript" src="js/jquery.validate.js"></script>
<script type="text/javascript" src="js/jquery.datepick.js"></script>
<style type="text/css">
@import "css/jquery.datepick.css";
</style>

<script src="js/dateValidation.js"></script>

<!-- Beginning of compulsory code below -->
<link href="css/dropdown/dropdown.css" media="screen" rel="stylesheet"
	type="text/css" />

<link href="css/dropdown/dropdown.vertical.css" media="screen"
	rel="stylesheet" type="text/css" />
<link href="css/dropdown/themes/default/default.advanced.css"
	media="screen" rel="stylesheet" type="text/css" />


<!--[if lte IE 7]>
        <script type="text/javascript" src="js/jquery/jquery.js"></script>
        <script type="text/javascript" src="js/jquery/jquery.dropdown.js"></script>
        <![endif]-->


<script>   
    $(document).ready(function(){
    		 
    	setActiveMenuItem();
    	$("#sameToBelow").click(sameToBelowClicked);
    });
    
    // function to be same Manager as spoc
    function sameToBelowClicked()
    {
    	if($("#sameToBelow").is(":checked"))
    		{
    			$("input[name=supervisorID2]").val($("input[name=supervisorID1]").val());
    			$("input[name=supervisorName2]").val($("input[name=supervisorName1]").val());
    		}
    	else{
	    		$("input[name=supervisorID2]").val("");
				$("input[name=supervisorName2]").val("");
    		}
    }
    
    function setActiveMenuItem()
    {
    	var url=window.location.pathname;
    	var filename=url.substring(url.lastIndexOf('/') + 1);
   //  $("li[class=active']").removeAttr("active");
  		 
  		
  		 
  		 
   		
    	 $("a[href='"+ filename + "']").parent().attr("class", "active");
    		 $("a[href='"+ filename + "']").parent().parent().parent('li').attr("class", "active") ;
    		 
    	 
    }
    </script>
<script type="text/javascript">
        function showPopup(url) {

        var params="toolbars=no,menubar=no,location=no,scrollbars=yes,resizable=yes";
        size="height=450,width=520,top=200,left=300,"+params;        
        
        if(url=="LandMarkSearch.jsp")
            {
                size="height=450,width=600,top=200,left=300," + params;
            }
        else if(url=="SupervisorSearch1.jsp" || url=="SupervisorSearch2.jsp"  )
               {
                   size="height=450,width=700,top=200,left=300,"+params;
               }
        else if(url=="termsAndConditions.html")
            {
                   size="height=450,width=520,top=200,left=300,"+params;
            }
        
        var site=document.getElementById("site").value;
		if (url == "LandMarkSearch.jsp") {
			if(site.length<1)
			{
			alert("Choose Site");
			return false;
			}
			url+="?site="+site;
		}
        newwindow=window.open(url,'name',size);
        

         if (window.focus) {newwindow.focus();}
        }
</script>
<script type="text/javascript">
            $(function() {
                $('#fromdatedata').datepick();


            });
        </script>
       
<script type="text/javascript">




//  validate to prompt user about the payment
function confirmValidate()
{
	var fromdate=document.getElementById("fromdatedata").value;
    var flag=true;
     var currentDate=new Date();
     var currentDatevar = currentDate.getDate() + "/"
		+ currentDate.getMonth() + "/" + currentDate.getFullYear();
     var fromDate =$("input[name=fromDate]").val();
     var site=$("select[name=site]").val();
     if(fromdate.length<1)
     {
         alert("Choose From Date");
       //  date.focus();
         return false;
             
     }
     else if($("input[name=employeeName]").val()=="")
    {
        alert("Please specify Employee");
        flag=false;
    }
      else if($("input[name=supervisorName1]").val()=="")
            {
                alert("Please specify TeamLead");
                flag=false;
            }
      else if ($("input[name=supervisorName2]").val()==""){
                alert("Please specify SPOC");
                flag=false;
            }
      else if($("input[name=landMark]").val()=="")
          {
              alert("Please specify Landmark");
              flag=false;
          }
      else if(site=="")
          {
              alert("Please choose your site");
              flag=false;
          }
      else if (isNaN($("input[name=contactNo]").val())||($("input[name=contactNo]").val()).length!=10){
          alert("Please specify 10 digit contact number");
          flag=false;
      }
    

       

           if(flag==true)
               {
                   if(!confirm(" Please verify the subscription details before submitting\n Click OK to proceed Cancel to review"))
                       {
                           flag=false;
                       }
               }

        return flag;


}


                            </script>

<script type="text/javascript">
function displayaddress()
{
	document.getElementById("addresstd").innerHTML=document.getElementById("address").innerHTML;
	}
</script>
</head>
<body>


	<%
          long empid=0;
        String employeeId = OtherFunctions.checkUser(session);
            empid = Long.parseLong(employeeId);
            %>
	<%@include file="Header.jsp"%>



	<div id="body">
		<div class="content">



			<form action="UpdateSubscriptionWithFromdate" name="subscriptionForm"
				onsubmit="return confirmValidate();">


				<%
                        // code to show employee information in page
                        String code="";
                        try{
                         code= OtherDao.getInstance().getEmployeeDet(empid);
                        }catch(Exception e)
                                {
                                System.out.println("Exception  " + e);
                            }



                        %>
				<%///=code%>
				<hr />
				<h3>Modify Subscription</h3>





				<table align="center">
					<tr>
						<td align="right">Employee</td>
						<td><input type="hidden" name="subscriptionId"
							id="subscriptionId" /> <input type="hidden" name="employeeID"
							id="employeeID" /> <input type="text" readonly
							name="employeeName" id="employeeName"
							onclick="showPopup('SearchEmployeeAndSetSubscriptionDetails.jsp')" />
							<label for="employeeID" class="requiredLabel">*</label> <input
							class="formbutton" type="button" value="..."
							onclick="showPopup('SearchEmployeeAndSetSubscriptionDetails.jsp')" /></td>

					</tr>
					<tr>

						<td align="right">Effective Date</td>
						<td><input type="text" name="fromDate" id="fromDate"
							class="required" readOnly
							class="{showOtherMonths: true, firstDay: 1, dateFormat: 'yyyy-mm-dd', minDate: new Date(2012,  1, 25)}" />
							<label for="fromDate" class="requiredLabel">*</label></td>
							

					</tr>
					<tr>							
							<td align="right">From</td>
							<td align="left" colspan="2"><input name="fromdatedata"
								id="fromdatedata" type="text" size="6" readonly="readonly"
								class="{showOtherMonths: true, firstDay: 1, dateFormat: 'yyyy-mm-dd',
	                                                 minDate: new Date(2008, 12 - 1, 25)}"
								/><label for="fromDate1" class="requiredLabel">*</label>

							</td>
					</tr>	
					
											
					<tr>
						<td align="right">Site</td>
						<td>
							<%
                             List<SiteDto> siteList = new SiteService().getSites();
                             %> <select name="site" id="site">
								<option value="">---------- Select -----------</option>
								<%
                             for(SiteDto dto: siteList)
                                 {
                                    %>


								<option value="<%=dto.getId()%>">
									<%=dto.getName()%>
								</option>


								<%
                                }
                             %>
						</select> <label for="site" class="requiredLabel">*</label>


						</td>

					</tr>
					<tr>
						<td align="right"><%=SettingsConstant.hrm %></td>
						<td><input type="hidden" name="supervisorID1"
							id="supervisorID1" /> <input type="text" readonly
							name="supervisorName1" id="supervisorName1"
							onclick="showPopup('SupervisorSearch1.jsp')" /> <label
							for="supervisorID1" class="requiredLabel">#</label> <input
							class="formbutton" type="button" value="..."
							onclick="showPopup('SupervisorSearch1.jsp')" /></td>

					</tr>


					<tr>
						<td align="right">SPOC</td>
						<td><input type="hidden" name="supervisorID2"
							id="supervisorID2" /> <input type="text" readonly
							name="supervisorName2" id="supervisorName2"
							onclick="showPopup('SupervisorSearch2.jsp')" /> <label
							for="supervisorID2" class="requiredLabel">#</label> <input
							class="formbutton" type="button" value="..."
							onclick="showPopup('SupervisorSearch2.jsp')" /> <input
							type="checkbox" id="sameToBelow" />Same as <%=SettingsConstant.hrm %></td>

					</tr>







					<tr>


						<td colspan="2"><input style="margin-left: 36%;"
							type="button" value="Select APL" class="formbutton"
							onclick="showPopup('LandMarkSearch.jsp') " /></td>

					</tr>
					<tr>
						<td align="right">Area</td>
						<td><input type="text" value=""
							onclick="showPopup('LandMarkSearch.jsp') " readonly name="area"
							id="area" /> <input type="hidden" id="landMarkID"
							name="landMarkID" /> <label for="area" class="requiredLabel">*</label>

						</td>

					</tr>
					<tr>
						<td align="right">Place</td>
						<td><input type="text" value=""
							onclick="showPopup('LandMarkSearch.jsp') " readonly name="place"
							id="place" /> <label for="place" class="requiredLabel">*</label>
						</td>

					</tr>
					<tr>
						<td align="right">Landmark</td>
						<td><input type="text"
							onclick="showPopup('LandMarkSearch.jsp') " readonly
							name="landMark" id="landmark" /> <label for="landMark"
							class="requiredLabel">*</label></td>

					</tr>




					<tr>
						<td align="right">Contact No</td>
						<td><input type="text" name="contactNo" id="contactNo"
							maxlength="10" /> <label for="contactNo" class="requiredLabel">*</label>



						</td>

					</tr>
					<tr>
						<td align="right" rowspan="4" valign="middle">Address</td>
						<td rowspan="4" id='addresstd'></td>

					</tr>
					<tr></tr>
					<tr></tr>
					<tr></tr>

					<tr>
						<td></td>
						<td><input class="formbutton" type="submit" name="subscribe"
							value="Submit" /> <input type="button" class="formbutton"
							onclick="javascript:history.go(-1);" value="Back" /></td>



					</tr>
					<tr>


						<td colspan="2"><label style="margin-left: 20%" for="site"
							class="requiredLabel">*</label> Indicates mandatory field</td>
					</tr>
					<tr>
						<td colspan="2"><label style="margin-left: 20%" for="site"
							class="requiredLabel">#</label> <%=SettingsConstant.hrm %> is your transport
							approving authority.
					</tr>
					<tr>
						<td colspan="2"><label style="margin-left: 20%" for="site"
							class="requiredLabel">#</label> Spoc is your transport scheduling
							authority.
					</tr>
				</table>
			</form>



			<%@include file="Footer.jsp"%>
		</div>
	</div>

</body>
</html>
