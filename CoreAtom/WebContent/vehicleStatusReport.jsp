
<%@page import="com.agiledge.atom.dao.VendorBaseVehicleStatusDao"%>
<%@page import="com.agiledge.atom.dao.OtherDao"%>
<%@page import="com.agiledge.atom.dto.TripDetailsChildDto"%>
<%@page import="com.agiledge.atom.dto.TripDetailsDto"%>
<%@page import="com.agiledge.atom.dao.VendorBaseVehicleStatusDao"%>

<%@page import="com.agiledge.atom.service.VendorService"%>
<%@page import="com.agiledge.atom.dto.VendorDto"%>

<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.Calendar"%>
<%@page import="com.agiledge.atom.dao.TripDetailsDao"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="css/style.css" rel="stylesheet" type="text/css" />
<link href="css/menu.css" rel="stylesheet" type="text/css" />
<script src="http://code.jquery.com/jquery-1.9.1.js"></script>
<script type="text/javascript" src="js/jquery-latest.js"></script>
<script type="text/javascript" src="js/validate.js"></script>

<script type="text/javascript" src="js/dateValidation.js"></script>
<script src="js/JavaScriptUtil.js"></script>
<style type="text/css">
@import "css/jquery.datepick.css";
</style>
<script type="text/javascript" src="js/jquery.datepick.js"></script>
<title>Vehicle Status Report</title>
<link href="css/xlstablefilter.css" rel="stylesheet" type="text/css">
<script src="js/JavaScriptUtil.js"></script>
<script type="text/javascript">
function attachFilter(table, filterRow)
{
	table.filterRow = filterRow;
	// Check if the table has any rows. If not, do nothing
	if(table.rows.length > 0)
	{
		// Insert the filterrow and add cells whith drowdowns.
		var filterRow = table.insertRow(table.filterRow);
		for(var i = 0; i < table.rows[table.filterRow + 1].cells.length; i++)
		{

			var c = document.createElement("TH");
			table.rows[table.filterRow].appendChild(c);
			
			var opt = document.createElement("select");
			opt.className = "mystyle";
			opt.onchange = filter;
			if(i==2||i==3||i==4||i==5||i==6)
			{
			c.appendChild(opt);
//			$('.mystyle').select2();
			$('.mystyle').css('width', '150');

			}
		}
		// Set the functions
		table.fillFilters = fillFilters;
		table.inFilter = inFilter;
		table.buildFilter = buildFilter;
		table.showAll = showAll;
		table.detachFilter = detachFilter;
		table.filterElements = new Array();
		
		// Fill the filters
		table.fillFilters();
		table.filterEnabled = true;
	}
}

function detachFilter()
{
	if(this.filterEnabled)
	{
		// Remove the filter
		this.showAll();
		this.deleteRow(this.filterRow);
		this.filterEnabled = false;
	}
}

// Checks if a column is filtered
function inFilter(col)
{
	for(var i = 0; i < this.filterElements.length; i++)
	{
		if(this.filterElements[i].index == col)
			return true;
	}
	return false;
}

// Fills the filters for columns which are not fiiltered
function fillFilters()
{
	for(var col = 0; col < this.rows[this.filterRow].cells.length; col++)
	{
		if(!this.inFilter(col))
		{
			if(col==3||col==4||col==2||col==5||col==6)
				{
			this.buildFilter(col, "(ALL)");
				}
		}
	}
}

// Fills the columns dropdown box. 
// setValue is the value which the dropdownbox should have one filled. 
// If the value is not suplied, the first item is selected
function buildFilter(col, setValue)
{
	// Get a reference to the dropdownbox.
	var opt = this.rows[this.filterRow].cells[col].firstChild;
	
	// remove all existing items
	while(opt.length > 0)
		opt.remove(0);
	
	var values = new Array();
		
	// put all relevant strings in the values array.
	for(var i = this.filterRow + 1; i < this.rows.length; i++)
	{
		var row = this.rows[i];
		if(row.style.display != "none" && row.className != "noFilter")
		{
			values.push(row.cells[col].innerHTML);
		}
	}
	values.sort();
	
	//add each unique string to the dopdownbox
	var value;
	for(var i = 0; i < values.length; i++)
	{
		if(values[i].toLowerCase() != value)
		{
			value = values[i].toLowerCase();
			opt.options.add(new Option(values[i], value));
		}
	}
	opt.options.add(new Option("(ALL)", "(ALL)"), 0);


	if(setValue != undefined)
		opt.value = setValue;
	else
		opt.options[0].selected = true;
}

// This function is called when a dropdown box changes
function filter()
{
	var table = this; // 'this' is a reference to the dropdownbox which changed
	while(table.tagName.toUpperCase() != "TABLE")
		table = table.parentNode;

	var filterIndex = this.parentNode.cellIndex; // The column number of the column which should be filtered
	var filterText = table.rows[table.filterRow].cells[filterIndex].firstChild.value;
	
	// First check if the column is allready in the filter.
	var bFound = false;
	
	for(var i = 0; i < table.filterElements.length; i++)
	{
		if(table.filterElements[i].index == filterIndex)
		{
			bFound = true;
			// If the new value is '(all') this column is removed from the filter.
			if(filterText == "(ALL)")
			{
				location.reload();
			}
			else
			{

				table.filterElements[i].filter = filterText;

			}
			break;
		}
	}
	if(!bFound)
	{
		// the column is added to the filter
		var obj = new Object();
		obj.filter = filterText;
		obj.index = filterIndex;
		table.filterElements.push(obj);
	}
	
	// first set all rows to be displayed
	table.showAll();
	
	// the filter ou the right rows.
	for(var i = 0; i < table.filterElements.length; i++)
	{
		// First fill the dropdown box for this column
		table.buildFilter(table.filterElements[i].index, table.filterElements[i].filter);
		// Apply the filter
		for(var j = table.filterRow + 1; j < table.rows.length; j++)
		{
			var row = table.rows[j];
			
			if(table.style.display != "none" && row.className != "noFilter")
			{
				if(table.filterElements[i].filter != row.cells[table.filterElements[i].index].innerHTML.toLowerCase())
				{
					row.style.display = "none";
				}
			}
		}
	}
	// Fill the dropdownboxes for the remaining columns.
	table.fillFilters();
}

function showAll()
{

	for(var i = this.filterRow + 1; i < this.rows.length; i++)
	{
		this.rows[i].style.display = "";
	}
}

</script>

<script type="text/javascript">
	$(document).ready(function() {
		$("#tripDate").datepick();		
		$(".hideclass").hide();

$(".showhideclass").click(function () {
	var id=$(this).attr("id");
	if ($(".empDetails"+id).is(":hidden")) {
    	  $(".empDetails"+id).show();
        $(this).attr("src","images/minus.png");
       var p = $(".empDetails"+id+":last");
        var position = p.position();
        
        showMap("tripId"+id,position.top+30);
    } else {
    	$(".empDetails"+id).hide();
      $(".empDetails"+id).slideUp("slow");
        $(this).attr("src","images/plus.png");
        document.getElementById("map").style.display = "none";
    }
});

	});
</script>


<link
	href="http://code.google.com/apis/maps/documentation/javascript/examples/default.css"
	rel="stylesheet" type="text/css" />
 <script type="text/javascript" src = "https://maps.googleapis.com/maps/api/js?sensor=true&client=gme-leptonsoftwareexport&signature=1t2jNPl7sIPdevsQdfKNrx25bko=&"></script> 
<!-- <script type="text/javascript" src = "http://maps.googleapis.com/maps/api/js?sensor=true&callback=initialize"></script>
 -->
<script type="text/javascript">	
var geocoder;
var map;
 var marker;
 var cityLat;
 var cityLon;
 var markersArray = [];
 var tripId;
 var security;
 var startLatLng;
var	directionsDisplay ;	
var directionsService = new google.maps.DirectionsService();
function showMap(curTripId,position) {
	document.getElementById("map").style.display = "none";
	document.getElementById("map").style.display = "block";
	document.getElementById("map").style.top= position+"px";
	document.getElementById("map").style.position= "absolute";
   	try
   	{
   		  
	cityLat = document.getElementById("cityLat").value;
	  
	cityLon = document.getElementById("cityLon").value;
	  
		directionsDisplay = new google.maps.DirectionsRenderer();
		var myLatlng = new google.maps.LatLng(cityLat, cityLon);

	 
    var myOptions = {
        zoom: 12,
        center: myLatlng,
        mapTypeId: google.maps.MapTypeId.ROADMAP
    };
    map = new google.maps.Map(document.getElementById("map"), myOptions);    
   	directionsDisplay.setMap(map);
   	 
   	tripId=document.getElementById(curTripId).value
   		displayVehicles(); 
}catch(e)
{
	alert(e)
	}
}

	function GetXmlHttpObject()
	{
		var xmlHttp=null;
	if (window.XMLHttpRequest) {
			xmlHttp=new XMLHttpRequest();
		}
		//catch (e)
	else if (window.ActiveXObject) {
		 		xmlHttp=new ActiveXObject("Microsoft.XMLHTTP");
	 		}
	return xmlHttp;
	}	
function displayVehicles()
{
	
	
    xmlHttp=GetXmlHttpObject()
    if (xmlHttp==null)
	{
        alert ("Browser does not support HTTP Request")
        return
    }
	var url="GetVehiclePosition?tripId="+tripId;
	xmlHttp.onreadystatechange=showVehiclePosition;
	xmlHttp.open("POST",url,true);
	xmlHttp.send(null);    
}

function showVehiclePosition()
{
    if (xmlHttp.readyState==4 || xmlHttp.readyState=="complete")
    {    	
    	var retString=xmlHttp.responseText;
    	vehiclepositions=retString.split("|");
    	//showActualPath();
    	showEmployeeGetIn();   
    	
    	
    }
}
function  showActualPath(){
   // document.getElementById("routedet").style.display = 'block';    
    xmlHttp=GetXmlHttpObject()
    var url="GetRoute?tripIdInTrace="+tripId;
    xmlHttp.onreadystatechange=displayPath;
    xmlHttp.open("POST",url,true)
    xmlHttp.send()
}


function displayPath()
{
	if (xmlHttp.readyState==4 || xmlHttp.readyState=="complete")
    	    {
    	    	
    	    	try{
    	        var fullnodes = xmlHttp.responseText;    
    	        
    	        var nodes=fullnodes.split("$");
    	        var noofnodes=(nodes.length)-1;          
    	        var startnode=nodes[0].split(":");       
    	      //  alert(startnode[2]+"     "+startnode[3]);
    	        var startnodelatlng=new google.maps.LatLng(startnode[2],startnode[3]);            
    	        var endnode=nodes[noofnodes-1].split(":");
    	     //   alert(endnode[2]+"     "+endnode[3]);
    	        var endnodelatlng=new google.maps.LatLng(endnode[2],endnode[3]);              
    	        var waypts = [];                
    	        
    	        for(var i=1;i<noofnodes-1;i++)
    	        {            
    	            intnode=nodes[i].split(":");
    	         //   alert("lat and long "+intnode[2]+"  "+intnode[3]);
    	            var intnodeltln=new google.maps.LatLng(intnode[2],intnode[3]);                        
    	            waypts.push({location:intnodeltln,stopover:true});
    	        }       
    	        
    	     
    	        var request = {
    	            origin: startnodelatlng,
    	            destination: endnodelatlng,
    	            waypoints: waypts,
    	            optimizeWaypoints: true,
    	            avoidHighways:false,
    	            travelMode: google.maps.DirectionsTravelMode.DRIVING
    	        };
    	        var addressRoute="";
    	        directionsService.route(request, function(response, status) {
    	            if (status == google.maps.DirectionsStatus.OK) {
    	                directionsDisplay.setDirections(response);
//    	                          var summaryPanel = document.getElementById("routedet");
    	                var datasting ='<table border="1" width="460"><thead style="color:#fff;background:#FF6633"><tr><th>SI</th><th>Emp ID</th><th>Emp Name</th></tr></thead>';
    	                datasting +='<tbody></tr>';
    	                datasting +='<tr><td>'+1+'</td><td>'+startnode[0]+'</td><td>'+startnode[1]+'</td></tr>';
    	                for (var i = 1; i < noofnodes-1; i++) {
    	                    var intnode=nodes[i].split(":");
    	                    datasting +='<tr><td>'+(i+1)+'</td><td>'+intnode[0]+'</td><td>'+intnode[1]+'</td></tr>';                                                                                                   
    	                }
    	                datasting +='<tr><td>'+(i+1)+'</td><td>'+endnode[0]+'</td><td>'+endnode[1]+'</td></tr>';
    	                datasting +='</tbody></table>';
  //  	                summaryPanel.innerHTML=datasting;
    	    
    				showEmployeeGetIn();                
    	            }
    	            else
    	            {
    	                alert(status);}
    	        });
    	            
    	    	}catch(e)
    	    	{
    	    	alert("error"+e);	
    	    	}
    	                            
    	    }
    	    
    	}
function  showEmployeeGetIn(){
	xmlHttp=GetXmlHttpObject()
		var url="GetRoute?tripIdForGetIn="+tripId;
	//alert(url);
		xmlHttp.onreadystatechange=displayEmployeeGetIn;
		xmlHttp.open("POST",url,true);
		xmlHttp.send()
	}
	
function displayEmployeeGetIn()
{
		if (xmlHttp.readyState==4 || xmlHttp.readyState=="complete")
			{
			var fullnodes = xmlHttp.responseText;
			var nodes=fullnodes.split("$");
			var noofnodes=nodes.length;
			var ltln;
			var centerLatlng;
			var center=(noofnodes-2)/2;
			for(var i=1;i<noofnodes-1;i++)
			{
			ltln=nodes[i].split(":");
			var latlng = new google.maps.LatLng(ltln[2],ltln[3]);
			if(i==noofnodes-3)
				{
				centerLatlng=new google.maps.LatLng(ltln[2],ltln[3]);
				}
						var message="Emp Code:"+ltln[0]+"   Name:"+ltln[1];
						placeMarker(ltln[4],latlng,message);
						//displayEmployeeGetInMarkers(marker,latlng,message);
			}
			showVehiclesPath(map,vehiclepositions);
			//setMapCenter(centerLatlng);
			
			}
}



function showVehiclesPath(map, vehiclepositions) {    

                       	var flightPlanCoordinates=new Array();
             var lastlat;
             var lastlon;
        for(var i=1;i<vehiclepositions.length-1;i++)
			{        	
        	var locations=vehiclepositions[i].split("~");
        	lastlat=locations[1]
        	lastlon=locations[2];
        	flightPlanCoordinates[i-1]=new google.maps.LatLng(locations[1], locations[2]);
			}
        startLatLng=flightPlanCoordinates[0];
    //    alert(security)
        if(security=="YES")
        	{
        placeMarker("S",startLatLng,"Security");
        	}
        var flightPath = new google.maps.Polyline({
       	    path: flightPlanCoordinates,
       	    strokeColor: '#FF0000',
       	    strokeOpacity: 1.0,
       	    strokeWeight: 2
       	  });
        flightPath.setMap(map);
        markVehicle(lastlat,lastlon);
  }
function markVehicle(lastlat,lastlon)
{
	try
	{
	var myLatLng=new google.maps.LatLng(lastlat, lastlon);
	var title="";
	image = 'icons/greencar.png';
 marker = new google.maps.Marker({
	position : myLatLng,
	map : map,
	icon : image,
	title : title
});
}catch(e){alert(e)}
}

function GetXmlHttpObject()
{
    var xmlHttp=null;
    if (window.XMLHttpRequest) {
        xmlHttp=new XMLHttpRequest();
    }
    //catch (e)
    else if (window.ActiveXObject) {
        xmlHttp=new ActiveXObject("Microsoft.XMLHTTP");
    }

    return xmlHttp;
}

function placeMarker(gender,location,title) {
    var image='images/male.png';
    if(gender=="M")
	{
	image = 'images/male.png';
	}
else if(gender=="F")
{
	image = 'images/female.png';	
}
else if(gender=="S")
{
	image = 'images/security.gif';	
}	    
         marker = new google.maps.Marker({
            position: location,
            map: map,
            icon:image,
            title: title
        });
	
}
function setMapCenter(centerLatLng)
{
    map.setCenter(centerLatLng);
}

function displayEmployeeGetInMarkers(marker,latlng,message) {	  
	  var infowindow = new google.maps.InfoWindow(
	      { content: message,
	        size: new google.maps.Size(50,50)
	      });
	     google.maps.event.addListener(marker, 'click', function() {
	  infowindow.open(map,marker);
	  });

	}

</script>










</head>

<body onload="attachFilter(document.getElementById('disptable'), 1)">
	<%
		long empid = 0;
		String employeeId = OtherFunctions.checkUser(session);

		if (employeeId == null || employeeId.equals("null")) {
			String param = request.getServletPath().substring(1) + "___"
					+ request.getQueryString();
			response.sendRedirect("index.jsp?page=" + param);
		} else {
			empid = Long.parseLong(employeeId);
	%><%@include file="Header.jsp"%>
	<%
}		OtherDao ob = null;
		ob = OtherDao.getInstance();
		String site =""; 
	     try {
	    	 site = session.getAttribute("site").toString();	 
	     }catch(Exception ignor){}
	    
		String[] city = ob.getCity(site);
	%>

			<%
			VendorBaseVehicleStatusDao dao= new VendorBaseVehicleStatusDao();
			String userType=dao.getUserType(employeeId);
			String tripDate = request.getParameter("tripDate") == null ? OtherFunctions
						.formatDateToOrdinaryFormat(new Date()) : request
						.getParameter("tripDate");
				int filter = request.getParameter("filter") == null ? 0 : Integer
						.parseInt(request.getParameter("filter"));
				String masterVendor= request.getParameter("vendorId");
				String selected = "";
				if (filter == 0) {
					selected = "selected";
				}

				ArrayList<TripDetailsDto> detailsDtos = new TripDetailsDao()
						.tripStatus(tripDate,masterVendor,filter);
				System.out.println("here date"+tripDate);
			%>
				<div id="body">
		<div class="content">
			<h3>Vehicle Status Report</h3>			
			<form>
				<table>
					<tr>
								<input type="hidden" value="<%=city[0]%>" id="cityLat"> <input
				type="hidden" value="<%=city[1]%>" id="cityLon">
						<td>Date&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input name="tripDate" id="tripDate" type="text" size="6"
							value="<%=tripDate%>" /></td>
<<<<<<< vehicleStatusReport.jsp
							<td>
							<% 
							if(userType.equalsIgnoreCase("v"))
							{	
								String master= dao.getvendorCompany(employeeId);
								%>
								<input type="hidden" value="<%=master%>" id="vendorId">
								<%
							}else{
							%>
							Select Vendor
			<select name="vendorId" id="vendorId">
			<option value="0"> ALL </option>
				<%
				ArrayList<VendorDto> vendorDtos = null;
				vendorDtos= new VendorService().getMasterVendorlist();
			
			
					for(VendorDto vendorDto : vendorDtos){
						%>
							<option value="<%=vendorDto.getCompanyId()%>"><%=vendorDto.getCompany()%></option>
						<% 
					}
				%>
				</select>
			<%} %>
							</td>
=======
							<td>
							<% 
							if(userType.equalsIgnoreCase("v"))
							{	
								String master= dao.getvendorCompany(employeeId);
								%>
								<input type="hidden" value="<%=master%>" id="vendorId">
								<%
							}else{
							%>
							Select Vendor
			<select name="vendorId" id="vendorId">
			<option value="">-SELECT-</option>
				<%
				ArrayList<VendorDto> vendorDtos = null;
				vendorDtos= new VendorService().getMasterVendorlist();
			
			
					for(VendorDto vendorDto : vendorDtos){
						%>
							<option value="<%=vendorDto.getCompanyId()%>"><%=vendorDto.getCompany()%></option>
						<% 
					}
				%>
				</select>
			<%} %>
							</td>
>>>>>>> 1.16
						<td>Show&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<select name="filter"><option value="1">All</option>
								<option value="0" <%=selected%>>With Female</option>
						</select></td>
						<td></td>
						<td><input type="submit" value="submit" class="formbutton"></td>
					</tr>

				</table>
			</form>		
	<div id="map" style="width: 100%; height: 500px; float: left;display:none "></div>
			<table id='disptable'>
					<tr>
						<th>trip Code</th>
						<th>Date</th>
						<th>Time</th>
						<th>Vehicle No</th>
						<th width="8%">Escort</th>
						<th>start Time</th>
						<th>stop Time</th>
						<th>#Emps</th>
						<th>#EmpsBoarded</th>
						<th>#Reached</th>
						<th>#Female</th>						
						<th>#Reached</th>
						<th>Status</th>
						<th></th>
					</tr>									
					<%					
					int serailNo=0;
						for (TripDetailsDto dto : detailsDtos) {
							serailNo++;
					%>

					<tr>
						<td><input type="hidden" id="tripId<%=serailNo%>" value="<%=dto.getId()%>"/>
												
						<a href="replayTrack.do?tripid=<%=dto.getId()%>" ><%=dto.getTrip_code()%></a></td>
						<td><%=dto.getTrip_date()%></td>
						<td><%=dto.getTrip_time() + " " + dto.getTrip_log()%></td>
						<td><%=dto.getVehicleNo()%></td>
						<td><%=dto.getIsSecurity()%></td>
						<td><%=dto.getStartTime()%></td>
						<td><%=dto.getStopTime()%></td>
						<td><%=dto.getEmpCount()%></td>
						<td><%=dto.getEmpInCount()%></td>
						<td><%=dto.getReachedempCount()%></td>
						<td><%=dto.getLadyInCount()%></td>						
						<td><%=dto.getReachedLadyCount()%></td>
						<td></td>
						<td><img src="images/plus.png" class="showhideclass" id="<%=serailNo%>"></td>
					</tr>
						<tr class='empDetails<%=serailNo%> hideclass'>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
							<td><b>Code</b></td>
							<td><b>Name</b></td>
							<td><b>Gender</b></td>
							<td><b>In time</b></td>
							<td><b>Out Time</b></td>
							<td><b>Correct Drop</b></td>
							<td></td>
							<td></td>		
						</tr>
						<%
							for (TripDetailsChildDto childDto : dto.getTripDetailsChildDtoList()) {
						%>
						<tr class='empDetails<%=serailNo%> hideclass'>
						<td></td>
							<td></td>
							<td></td>
							<td></td>
							<td><%=childDto.getEmployeeId() %></td>
							<td><%=childDto.getEmployeeName() %></td>
							<td><%=childDto.getGender() %></td>
							<td><%=childDto.getInTime() %></td>
							<td><%=childDto.getOutTime() %></td>
							<td><%=childDto.getIsCorrectPos() %></td>
							<td></td>
							<td></td>
						</tr>
						<%
							}
						}
					%>	
					</table>
</div>
</div>
<div style="height: 750px"></div>
	<%@include file="Footer.jsp"%>	
</body>

</html>
