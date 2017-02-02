<%@page import="com.agiledge.atom.dao.OtherDao"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>

        <script type="text/javascript" src = "https://maps.googleapis.com/maps/api/js?sensor=true&client=gme-leptonsoftwareexport&signature=1t2jNPl7sIPdevsQdfKNrx25bko="></script>
<script type="text/javascript">
var directionDisplay;
var directionsService = new google.maps.DirectionsService();
var map;
var cityLat;
var cityLon;
function showRoute() {
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
    var routeId=document.getElementById("routeId").value;
    getPath(routeId);	
}

function  getPath(routeId){
   // document.getElementById("routedet").style.display = 'block';    
    xmlHttp=GetXmlHttpObject()
    var url="GetRoute?routeId="+routeId;
    xmlHttp.onreadystatechange=displayPath;
    xmlHttp.open("POST",url,true)
    xmlHttp.send()


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

function displayPath()
{
    if (xmlHttp.readyState==4 || xmlHttp.readyState=="complete")
    {
    	try{
        var fullnodes = xmlHttp.responseText;       
        var nodes=fullnodes.split("$");                           
        var noofnodes=(nodes.length)-1;          
        var startnode=nodes[0].split(":");          
        var startnodelatlng=new google.maps.LatLng(startnode[5],startnode[6]);            
        var endnode=nodes[noofnodes-1].split(":");                    
        var endnodelatlng=new google.maps.LatLng(endnode[5],endnode[6]);              
        var waypts = [];                
        
        var nodesplit=Math.ceil((noofnodes-1)/26);
        var intnode;        
        for(var i=1;i<noofnodes-1;i=i+nodesplit)
        {            
            intnode=nodes[i].split(":");
            var intnodeltln=new google.maps.LatLng(intnode[5],intnode[6]);                        
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
        directionsService.route(request, function(response, status) {
            if (status == google.maps.DirectionsStatus.OK) {
                directionsDisplay.setDirections(response);             
       //         var summaryPanel = document.getElementById("routedet");
                var datasting ='<table border="0" width="460"><thead ><th>SI</th><th>Area</th><th>Place</th><th>Landmark</th></thead>';
                datasting +='<tbody ><tr><td>0</td><td>'+startnode[5]+'</td><td>'+startnode[6]+'</td><td>'+startnode[4]+'</td><td>&nbsp;</td><td>&nbsp;</td></tr>';                
                for (var i = 1; i < noofnodes-2; i++) {
                    var intnode=nodes[i+1].split(":");
                    datasting +='<tr><td>'+(i)+'</td><td>'+intnode[5]+'</td><td>'+intnode[6]+'</td><td>'+intnode[4]+'</td></tr>';
                }
                datasting +='<tr><td>'+(i)+'</td><td>'+endnode[5]+'</td><td>'+endnode[6]+'</td><td>'+endnode[4]+'</td></tr></tbody></table>';
             //   summaryPanel.innerHTML=datasting;
            }
            else
            {
                alert(status);}
        });
        /*
              for(i=1;i<noofnodes-1;i++)
        {

            var intnode=nodes[i].split(":");
            var intnodeltln=new google.maps.LatLng(intnode[5],intnode[6]);
            placeMarker(intnodeltln);

            var message=intnode[1]+"<br>"+intnode[2]+"<br>"+intnode[4]+"<br>";
            attachdetails(marker,message);

        }
        */
        
    	}catch(e)
    	{
    	alert("error"+e);	
    	}
                            
    }
    
}
function placeMarker(location) {
	marker = new google.maps.Marker({
		position : location,
		map : map
	});
}
function attachdetails(marker,message) {	
	try{
	  var infowindow = new google.maps.InfoWindow(
	      { content: message,
	        size: new google.maps.Size(50,50)
	      });
	     google.maps.event.addListener(marker, 'click', function() {
	  infowindow.open(map,marker);
	  });
	}
	catch(e)
	{alert(e);}
	}
</script>
</head>
<% OtherDao ob=OtherDao.getInstance();
String site =""; 
try {
	 site = session.getAttribute("site").toString();	 
}catch(Exception ignor){}

String[] city=ob.getCity(site);

%>
<input type="hidden" value="<%=city[0]%>" id="cityLat">
<input type="hidden" value="<%=city[1]%>" id="cityLon">
<body onload="showRoute()">
	<%
		String routeId = request.getParameter("routeId");
	%>
	<P>
		<input type="hidden" name="routeId" id="routeId" value="<%=routeId%>" />
	</P>
	<div id="map" style="width: 100%; height: 500px; float: right;"></div>
	<!-- 
	<div id="routedet" style="width: 90%; height: 90%; float: right;display: none;"></div>
	-->
</body>


</html>