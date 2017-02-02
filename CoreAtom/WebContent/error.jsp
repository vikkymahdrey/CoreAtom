<%@page isErrorPage="true"%>
<html>
<head>
<title>Error Page.</title>
</head>
<body bgcolor="blue">
	<% long empid=0;
                //	session.setAttribute("status", "Test Test Test Test Test Test");
                %>

	<%@include file="Header.jsp"%>
	<div id="body">
		<div class="content">
			<font size="16" color="white">Your page generated an error:"<br />
				Exception:<br /></font>


			<%@include file="Footer.jsp"%>
		</div>
	</div>
</body>
</html>