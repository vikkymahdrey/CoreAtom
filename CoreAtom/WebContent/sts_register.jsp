<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
    <title>Sts-Registeration</title>

    <!-- Bootstrap -->
    <link href="css/bootstrap.min.css" rel="stylesheet">
    <link href="css/custom_siemens.css" rel="stylesheet">

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
			<img src="images/siemens_logo.png" />
		</div>
	</div>
	</div>
	
	<div class="container">
		<div class="login-input-wrap">
			<div >	
			<form action="StsRegister" onsubmit="return validate();" method="post">	
				<div class="row">
					<div class="col-sm-10">
						<h6 class="text-regular"><Strong>REGISTER TO ATOm</Strong></h6>
						
						<input type="text" id="fname" name="fname" placeholder="First Name" class="form-control login-fields username mar-top-20" />
						<input type="text" id="lname" name="lname" placeholder="Last Name"  class="form-control login-fields password mar-top-10" />
						<input type="text" id="personnelno" name="personnelno" placeholder="Employee Id"  class="form-control login-fields password mar-top-10" />
						Male&nbsp;&nbsp;<input type="radio" name="gender" id="gender"
							value="M" checked="checked" />&nbsp;&nbsp;&nbsp;&nbsp;Female&nbsp;&nbsp;<input
							type="radio" name="gender" id="gender" value="F" />
						<input type="text" id="mob" name="mob" placeholder="Contact Number"  class="form-control login-fields password mar-top-10" />
						<input type="text" id="email" name="email" placeholder="Email Address"  class="form-control login-fields password mar-top-10" />
						<input type="password" id="pwd" name="pwd" placeholder="Password"  class="form-control login-fields password mar-top-10" />
						<input type="password" id="pwdc" name="pwdc" placeholder="Confirm Password"  class="form-control login-fields password mar-top-10" />
					</div>
				</div>
				
				<div class="row">
					
					<div class="text-center left mar-top-10">
						<input type="submit" value="Register" class="btn btn-blue"/> &nbsp;&nbsp;&nbsp;
						<input type="reset" value="Reset" class="btn btn-blue"/> 
					</div>
				</div>
				</form>
			</div>
		</div>
	</div>
	
	<div class="login-footer">
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
<script type="text/javascript">
	function validate(){
		var fname=document.getElementById("fname").value;
		var lname=document.getElementById("lname").value;
		var personnelno=document.getElementById("personnelno").value;
		var email=document.getElementById("email").value;
		var pwd=document.getElementById("pwd").value;
		var pwdc=document.getElementById("pwdc").value;
		var mob=document.getElementById("mob").value;
		var atpos = email.lastIndexOf("@");
	    var dotpos = email.lastIndexOf(".");
		if(fname=="" || /^\s+$/.test(fname)){
			alert("Please enter Your Name");
			return false;
		}else if(lname=="" || /^\s+$/.test(lname)){
			alert("Please enter Your Last Name");
			return false;
		}else if(personnelno=="" || /^\s+$/.test(personnelno)){
			alert("Please enter Your ID");
			return false;
		}else if(mob=="" ||!(/^[1-9]{1}[0-9]{9}$/.test(mob))){
			alert("Enter valid mobile number");
			return false;
		}else if(atpos<1 || dotpos<atpos+2 || dotpos+2>=email.length || /^\s+$/.test(email)){
			alert("Enter valid Email Address");
			return false;
		}else if(pwd=="" || /^\s+$/.test(pwd)){
			alert("Please enter password");
			return false;
		}else if(pwd!=pwdc){
			alert("Password didn't match");
			return false;
		}
		return true;
	}
</script>
  </body>
</html>