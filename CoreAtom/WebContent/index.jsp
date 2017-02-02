<%@page import="com.agiledge.atom.constants.SettingsConstant"%>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    
<script type="text/javascript" src="js/jquery-latest.js"></script>
<script  type="text/javascript">
function browserIdentity()
{
	if (/MSIE (\d+\.\d+);/.test(navigator.userAgent)){ //test for MSIE x.x;
		 var ieversion=new Number(RegExp.$1) // capture x.x portion and store as a number
		  
		   if (ieversion<=7)
		 	 alert("You are using older version of Internet Explorer. Please upgrade your browser.");
		 
}
}
function validate() {
	var uname = document.getElementById("uname").value;
	var password = document.getElementById("pass").value;
	document.getElementById("namevalid").innerHTML = "";
	document.getElementById("passvalid").innerHTML = "";
	if (uname.length < 1) {
		document.getElementById("namevalid").innerHTML = "UserName can't be blank !";
		return false;
	}else if (password.length < 1) {
		document.getElementById("passvalid").innerHTML = "Password can't be blank !";
		return false;
	}  else{
		return true;
	}
}
</script>
    <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
    <title>Login</title>

    <!-- Bootstrap -->
    <link href="css/bootstrap.min.css" rel="stylesheet">
    <link href="css/custom_siemens.css" rel="stylesheet">
<link rel="icon" href="images/agile.png" type="image/x-icon" />
    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
      <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
      <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->
  </head>
  <body class="login-bg">
<div class="wrapper">
	
	<div class="login-triangle"></div>
	
	<div class="header-wrap-login">
	<div class="container-fluid">
		<div class="col-sm-6 col-xs-8">
		<% if(SettingsConstant.comp.equalsIgnoreCase("cd")||SettingsConstant.comp.equalsIgnoreCase("cd1")) {%>
		
	
	<% } else{%>
	       <img src="images/siemens_logo.png" />
	<% }%>
		</div>
	</div>
	</div>
	
	<div class="container">
	<%
response.setHeader("Cache-Control","no-cache, no-store, must-revalidate");

response.setHeader("Pragma", "co-cache");
response.setDateHeader("Expires", 0);
String prePageUrl="";
try{
   
 
  String type=request.getParameter("prePage");
  
  System.out.println(" type : " +type);
 	prePageUrl=type.replaceFirst("___", "?");
 	prePageUrl=prePageUrl.replaceAll("___", "&");
  
        System.out.println(" prepage : " +prePageUrl);
        
       


}catch(Exception e)
{
System.out.println("Error"+e);	
}
%>
				
				
				
				
					<%
					String message="";
				try{
					message=request.getParameter("message");
					if(message!=null&&!message.equals(""))
					{
					
					}
					else
					{						
						message = "";
							message = session.getAttribute("status").toString();
						session.setAttribute("status", "");
					}
				
				}catch(Exception e)
				{
					;
				}
					
				%>
		<div class="login-input-wrap">
			<div class="login-input-border-wrap">
			<form action="UserValidation" name="user_validation_form"
					id="user_validation_form" method="post"
					onsubmit="return validate()">		
				<div class="row">
					<div class="col-sm-12">
						<h6 class="text-regular text-uppercase" style="text-decoration:blink;"><i>Sign in to ATOm</i></h6>
						<input type="hidden" name="prePage" value="<%=prePageUrl%>" />
						<input type="text" name="uname" id="uname" placeholder="Login ID" class="form-control login-fields username mar-top-20" /><label id="namevalid" style="color: red;"></label>
						<input type="password" name="pass" id="pass" placeholder="Password"  class="form-control login-fields password mar-top-10" /><label id="passvalid" style="color: red;"></label>
						<label id="passvalid" style="color: red;"><%=message %></label>
						
					</div>
				</div>
				
				<div class="row">
					<div class="col-sm-6 mar-top-25">
						<a href="forgotPassword.jsp" class="text-grey link-underline">Forgot Password?</a>
					</div>
					<div class="col-sm-6 mar-top-20 text-right">
						<input type="submit" value="Login" class="btn btn-blue"/>
					</div>
				</div>
				</form>
			</div>
		</div>
	</div>
	
	<div class="login-footer" >
		<div class="container-fluid">
			<div class="row">
				<div class="col-sm-9 text-12 mar-top-10">
					<p>Note: Best viewed in Google Chrome 48.0 and above at resolution 1024 X 768</p>
					<p class="">© ATOm: Agiledge transport optimization manager (TM) All Rights Reserved: 2016 Agiledge Process Solutions Private Limited.</p>
				</div>
				<div class="col-sm-3 text-right mar-top-10">
					<img src="images/atom_logo.png" alt="" />
				</div>
			</div>
		</div>
	</div>	
</div>

    <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
    <!-- Include all compiled plugins (below), or include individual files as needed -->
    <script src="js/bootstrap.min.js"></script>
	<script src="http://maps.googleapis.com/maps/api/js"></script>
	<script>
	
	var docHeight = $(window).height();
	//alert(docHeight);
	var footerHeight = $(".login-footer").outerHeight();
	var headerHeight = $(".header-wrap-login").outerHeight() + 50;
	var contentheight = $(".login-bg .container").outerHeight();
	
	
	
	$(window).on('resize', function () {	
		$(".wrapper").css({
			"min-height" : $(window).height(),
			"padding-bottom" : (footerHeight + 30)
		});				
		var marginValue = ($(window).height() - footerHeight - headerHeight - contentheight)/2;		
		function loginMargin() {
			if (marginValue > 0) {
				$(".login-bg .container").css({
					"margin-top": marginValue
				});
			}
		}
		loginMargin();
	});			
	$( document ).ready(function() {		
		$(window).trigger('resize');		
	});	
</script>
  </body>
</html>