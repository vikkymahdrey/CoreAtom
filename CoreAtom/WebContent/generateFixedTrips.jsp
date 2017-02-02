<%@page import="com.agiledge.atom.dto.SiteDto"%>
<%@page import="com.agiledge.atom.dao.SiteDao"%>
<%@page import="java.util.List"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
<title>Generate Trip</title>

<!-- Bootstrap -->
<link href="css/bootstrap.min.css" rel="stylesheet">
<link href="css/custom_siemens.css" rel="stylesheet">
<link rel="stylesheet" href="css/jquery-ui.css">
<link href="css/toastr.min.css" rel="stylesheet" />
<!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
<!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
<!--[if lt IE 9]>
      <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
      <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->
 <script type="text/javascript" src="js/jquery-2.2.0.js"></script>   
<script type="text/javascript" src="js/jquery.validate.js"></script>
<script type="text/javascript" src="js/jquery.datepick.js"></script>

  <script src="js/jquery-ui.js"></script>
<script src="js/toastr.min.js"></script>

<script type="text/javascript">        
            $(document).ready(function()
            {                                                                        
                $("#tripDate").datepicker({ minDate: 0,dateFormat: 'dd/mm/yy' }); 
 
                      
            }); 
	
    function getlogtime()
    {                    
        var logtype=document.getElementById("tripLog").value;
        var site=document.getElementById("siteId").value;
        if(logtype=="All")
        	{
        	
        	tripTimeId.innerHTML='<select name="logTime" id="logTime"> <option value="All" >ALL</option></select>';
        	return;
        	}
        else
        	{
        var url="GetLogTime?logtype="+logtype+"&site="+site;                                    
        xmlHttp=GetXmlHttpObject()
        if (xmlHttp==null)
        {
            alert ("Browser does not support HTTP Request");
            return
        }                    
        xmlHttp.onreadystatechange=setLogTime;	
        xmlHttp.open("GET",url,true);                
        xmlHttp.send(null);
        
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

    function setLogTime() 
    {                      
        if (xmlHttp.readyState==4 || xmlHttp.readyState=="complete")
        { 
            var returnText=xmlHttp.responseText;
            var logTimeListTd=document.getElementById("tripTime");
            logTimeListTd.innerHTML='<select  name="logTime" id="logTime"><Option value="All">Select</Option>'+returnText+'</select>';                                             
        }
    }
function validate(){
		var site=document.getElementById("siteId").value;
		var date=document.getElementById("tripDate").value;
		var logTime=document.getElementById("tripTime").value;
		var logType=document.getElementById("tripLog").value;
		if(site=="All" ){
			$('.san').show();
			document.getElementById("errortag").innerHTML ="Please select site";
			$('.validation-required').removeClass( "validation-required" ).addClass( "form-control" );
			$('#siteId').removeClass( "form-control" ).addClass( "validation-required" );
			document.getElementById("siteId").focus();
			return false;
		}else if(date==""){
			$('.san').show();
			document.getElementById("errortag").innerHTML ="Please select date";
			$('.validation-required').removeClass( "validation-required" ).addClass( "form-control" );
			$('#tripDate').removeClass( "form-control" ).addClass( "validation-required" );
			document.getElementById("tripDate").focus();
			return false;
		}else if(logType=="All"){
			$('.san').show();
			document.getElementById("errortag").innerHTML ="Please select log type";
			$('.validation-required').removeClass( "validation-required" ).addClass( "form-control" );
			$('#tripLog').removeClass( "form-control" ).addClass( "validation-required" );
			document.getElementById("tripLog").focus();
			return false;
		}else if(logTime=="All"){
			$('.san').show();
			document.getElementById("errortag").innerHTML ="Please select Log Time";
			$('.validation-required').removeClass( "validation-required" ).addClass( "form-control" );
			$('#tripTime').removeClass( "form-control" ).addClass( "validation-required" );
			document.getElementById("tripTime").focus();
			return false;
		}else{
			return true;
			toastr.info('Please wait...');
		}
	}
</script>
</head>
<body>
<%
				List<SiteDto> siteDtoList = new SiteDao().getSites();
			%>
	<div class="wrapper">
		

		<div class="main-page-container">
		<%@include file="Header.jsp"%>
			<div class="container">
				<div class="row">
					<div class="col-sm-12">

						

						<div class="content-wrap">

							<%if(session.getAttribute("status")!=null){ %>
							<div class="row mar-top-40">
								<div class="col-sm-12">
									<div class="alert alert-success"><%= session.getAttribute("status")%></div>
								</div>
							</div>
							<%} %>
							<div class="row">
								<div class="col-sm-8 page-heading mar-top-20">
									<i class="page-heading-icon"><img
										src="images/lv_tack_cab_icon.png" /></i>
									<h5 class="text-blue text-semi-bold">Generate Trips</h5>
								</div>

							</div>

							<div class="row mar-top-20">
						<div class="col-sm-12">
							<div class="alert alert-danger san" hidden="hidden" style="color: red" ><p id="errortag"></p></div>
						</div>
					</div>
							<div class="push-15">
							<form action="GenerateFixedRoute" method="post" onsubmit="return validate();">
								<div class="row">
									<div class="col-md-1 col-xs-6 mar-top-15 text-lightgrey">Site</div>
									<div class="col-md-2  col-xs-6 mar-top-15"><select id="siteId" name="siteId" class="form-control" ><option value="All">Select</option>
									
										<%
									for (SiteDto siteDto : siteDtoList) {
										%>

										<option  value="<%=siteDto.getId()%>"><%=siteDto.getName()%></option>
										<%
									}
									%>
									
									
									
									</select></div>
									
									<div class="col-md-1 col-xs-6 mar-top-15 text-lightgrey">Date</div>
									<div class="col-md-2  col-xs-6 mar-top-15"><input type="text" id="tripDate" name="tripDate" class="form-control"/></div>
						
									<div class="col-md-1  col-xs-6 mar-top-15 text-lightgrey">Log Type</div>
									<div class="col-md-2  col-xs-6 mar-top-15"><select id="tripLog" name="tripLog" class="form-control" onchange="getlogtime();"><option value="All">Select</option><option value="IN">IN</option><option value="OUT">OUT</option></select></div>
									<div class="col-md-1  col-xs-6 mar-top-15 text-lightgrey">Time</div>
									<div class="col-md-2  col-xs-6 mar-top-15"><select id="tripTime" name="tripTime" class="form-control"><option value="All">Select</option></select></div>
									
								</div>
								<div class="row">
									<div class="col-md-15  mar-top-15 text-lightgrey" align="center"><input type="submit" class="btn btn-blue save-btn" value="Generate" /></div>
								</div>
								</form>
							</div>











						</div>

						<div class="footer-wrap">
							<div class="row">
								<div class="col-sm-12 text-center">
									<p class="text-12">
										The information stored on this website is maintained in
										accordance with the organization's Data Privacy Policy. </span><br />Copyright
										© 2016 agiledge
								</div>
							</div>

						</div>


					</div>
				</div>

			</div>
		</div>
	</div>

	<!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
	<!-- Include all compiled plugins (below), or include individual files as needed -->
	<script src="js/bootstrap.min.js"></script>

</body>
</html>