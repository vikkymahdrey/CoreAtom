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
 function replaceTest() {
	 alert();
	  var val = "jesper1.tttt";
	  var mystr = 'a_a_a_a_a';

	 try { 
	   alert( mystr.match(/a/g));
	 }catch(e) {
		 alert(e);
	 }
	  position = 2;
	  value = "1";
	 var s = "HELLO, 9WOR5LD!";
	 var nth = 0;
	 s = s.replace(/\d/g, function (match, i, original) {
	     nth++;
	     return (nth === position) ? value : match;
	 });
	 alert(s);

 }
</script>


<body onload="loadonwindow()">
 
<div id="sr">
<button onclick="replaceTest()" >OKEY</button>
</div>
</body>
</html>
