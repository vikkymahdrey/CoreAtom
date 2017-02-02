<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
 <%@taglib uri="http://www.nfl.com"  prefix="disp"%>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<link href="css/style.css" rel="stylesheet" type="text/css" />
</head>
<body>

  	<disp:dipxTable id="table" list="${dtoList }">
  		<disp:dipxColumn property="property" title="Property" ></disp:dipxColumn>
  		<disp:dipxColumn property="value"  title="Property" ></disp:dipxColumn>
  		<disp:dipxColumn property="site"  title="site" ></disp:dipxColumn>
  		<disp:dipxColumn property="effectivedate"  title="Effective" ></disp:dipxColumn>
  		<disp:dipxColumn property="todate"  title="To Date" ></disp:dipxColumn>
  	</disp:dipxTable>


</body>
</html>