
<html>
<head>
<title>JSP Page</title>
<link
	href="http://code.google.com/apis/maps/documentation/javascript/examples/default.css"
	rel="stylesheet" type="text/css" />
<script type="text/javascript"
	src="https://maps.googleapis.com/maps/api/js?sensor=false"></script>
<script type="text/javascript">
	var i = 0;
	var iteratedretData;
	function getSourceDest() {	
		
		xmlHttp = GetXmlHttpObject()
		var url = "GetSrcDestLandmark";
		xmlHttp.onreadystatechange = getSourceDestList;
		xmlHttp.open("GET", url, true);
		xmlHttp.send();		
	}
	function GetXmlHttpObject() {
		var xmlHttp = null;
		if (window.XMLHttpRequest) {
			xmlHttp = new XMLHttpRequest();
		} else if (window.ActiveXObject) {
			xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");
		}
		return xmlHttp;
	}
	
	
	function getSourceDestList() {
		
		if (xmlHttp.readyState == 4 || xmlHttp.readyState == "complete") {			
			var retData = xmlHttp.responseText;			
			iteratedretData = retData.split("$");	
			if(iteratedretData.length<2)
				{
				window.close();
				}
			else
				{
			splitData();
				}
		}
	}
	function splitData() {		
		try{
		if (i < iteratedretData.length) {
			var sorceDestList = iteratedretData[i].split(":");			
			findAndStoreDistance(sorceDestList[1], sorceDestList[2], sorceDestList[3],sorceDestList[4], sorceDestList[5], sorceDestList[6]);
		}
		else
			{
			window.location.reload();
			}
		}catch(e)
		{
		//alert(e);	
		}
	}

	function findAndStoreDistance(srcId, srcLat, srcLon, destId, destLat, destLon) {		
		var directionsService;
		var distfrmsrc;
		var request;
		var src;
		var dest;
		var url = "GetSrcDestLandmark";
		
		try {			
			directionsService = new google.maps.DirectionsService();
			src = new google.maps.LatLng(srcLat, srcLon);
			dest = new google.maps.LatLng(destLat, destLon);
			request = {
				origin : src,
				destination : dest,
				optimizeWaypoints : true,
				avoidHighways : true,
				travelMode : google.maps.DirectionsTravelMode.DRIVING
			};
			directionsService.route(request, function(response, status) {
				if (status == google.maps.DirectionsStatus.OK) {
					var route = response.routes[0];
					distfrmsrc = (route.legs[0].distance.value) / 1000;							
					url = url + "?dist=" + distfrmsrc + "&srcid=" + srcId+ "&destid=" + destId;
					xmlHttp.onreadystatechange = takeNextData;
					xmlHttp.open("GET", url, true)
					xmlHttp.send();
				} else {
					window.location.reload();
					//alert(iteratedretData.length+"error"+i);
				//	i++;
				//var startTime = new Date().getTime(); // get the current time
				//while (new Date().getTime() < startTime + 1000); // hog cp
				//i++;
					splitData();
				}
			});
		} catch (e) {
			alert(e);
			var startTime = new Date().getTime(); // get the current time
			while (new Date().getTime() < startTime + 1000); // hog cp							
			splitData();
		}
	}

	function takeNextData() {

		if (xmlHttp.readyState == 4 || xmlHttp.readyState == "complete") {
			i++;
			splitData();
		}

	}
</script>
</head>
<body>
	<%@include file="Header.jsp"%>
	
	<div id="body">
		<div id="content">
<table>
<tr>
<td>landmark Largest Id</td>
<td>
<input type="text" name="maxId" id="maxId"/>
</td>
</tr>
<tr>
<td>
Landmark Upto Id
</td>
<td>
<input type="text" name="uptoId" id="uptoId"/>
</td>
</tr>
<tr><td></td><td><input type="buton" value="Execute" onclick="getSourceDest()"/></td></tr>
</table>
	<%@include file="Footer.jsp"%>
		</div>

	</div>
</body>
</html>
