<%@page import="com.agiledge.atom.commons.Main"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Validation Test</title>
<link rel="stylesheet" href="http://cdn.leafletjs.com/leaflet-0.6.4/leaflet.css" />
<!--[if lte IE 8]>
    <link rel="stylesheet" href="http://cdn.leafletjs.com/leaflet-0.6.4/leaflet.ie.css" />
<![endif]-->

    <script src="http://www.mapquestapi.com/sdk/js/v7.0.s/mqa.toolkit.js?key=Fmjtd%7Cluur210t2q%2Crw%3Do5-90y0u6"></script>

<script type="text/javascript">

/*An example of using the MQA.EventUtil to hook into the window load event and execute defined function
passed in as the last parameter. You could alternatively create a plain function here and have it
executed whenever you like (e.g. <body onload="yourfunction">).*/
var map;
var MQA;
function l11oadonwindow() {

  /*Create an object for options*/
  var options={
    elt:document.getElementById('map'),        /*ID of element on the page where you want the map added*/
    zoom:12,                                   /*initial zoom level of map*/
    latLng:{lat:12.999205, lng:77.5600433},   /*center of map in latitude/longitude*/
    mtype:'map'                                /*map type (map)*/
  };

  /*Construct an instance of MQA.TileMap with the options object*/
  map = new MQA.TileMap(options);

  MQA.withModule('largezoom','traffictoggle','viewoptions','geolocationcontrol','insetmapcontrol','mousewheel', function() {

    map.addControl(
      new MQA.LargeZoom(),
      new MQA.MapCornerPlacement(MQA.MapCorner.TOP_LEFT, new MQA.Size(5,5))
    );

    map.addControl(new MQA.TrafficToggle());

    map.addControl(new MQA.ViewOptions());

    map.addControl(
      new MQA.GeolocationControl(),
      new MQA.MapCornerPlacement(MQA.MapCorner.TOP_RIGHT, new MQA.Size(10,50))
    );

    /*Inset Map Control options */
    var options={
      size:{width:150, height:125},
      zoom:3,
      mapType:'map',
      minimized:true
    };

    map.addControl(
      new MQA.InsetMapControl(options),
      new MQA.MapCornerPlacement(MQA.MapCorner.BOTTOM_RIGHT)
    );

    map.enableMouseWheelZoom();
  });
      
  showExistingLandmarks();
  MQA.EventManager.addListener(map, 'click', eventRaised);
}

function eventRaised(evt){
	var evtll=evt.ll;	
   alert(evtll.getLatitude()+":"+evtll.getLongitude());
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

function  showExistingLandmarks(){
	xmlHttp=GetXmlHttpObject()
		var url="GetLandmarks";
		xmlHttp.onreadystatechange=displayExistingLandmarks;
		xmlHttp.open("POST",url,true);
		xmlHttp.send()
	}
function displayExistingLandmarks()
{
		if (xmlHttp.readyState==4 || xmlHttp.readyState=="complete")
			{
			
			var fullnodes = xmlHttp.responseText;
			var nodes=fullnodes.split("$");
			var noofnodes=nodes.length;
			var ltln;
			for(var i=1;i<noofnodes;i++)
			{
			ltln=nodes[i].split(":");			
						placeMarker(ltln[4],ltln[5],ltln[0]+"<br>"+ltln[1]+"<br>"+ltln[3]+"<br>"+'');
						//var message=ltln[0]+"<br>"+ltln[1]+"<br>"+ltln[3]+"<br>"+'';
						//displayExistingLandmarkMarkers(marker,latlng,message);
			}
			}
}
function placeMarker(latn,lonn,message)
{
var basic=new MQA.Poi( {lat:latn, lng:lonn} );
var info=new MQA.Poi( {lat:latn, lng:lonn} );
info.setInfoContentHTML(message);
/*This will add the POI to the map in the map's default shape collection.*/
map.addShape(basic);
map.addShape(info);
}
</script>


<body>
<%
Main m=new Main();
m.getdistnce();
%>
</body>
</html>
