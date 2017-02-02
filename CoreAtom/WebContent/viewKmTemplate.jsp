 
<%@page import="com.agiledge.atom.billingtype.config.dto.KmTemplateDto"%>
<%@page import="com.agiledge.atom.billingtype.config.service.KmBasedTemplateBillingService"%>
<%@page import="com.agiledge.atom.dao.OtherDao"%>
<%@page import="com.agiledge.atom.billingtype.config.dto.KmTemplateDto.KmTemplateChildDto"%>

<%@page import="com.agiledge.atom.billingtype.config.dto.KmTemplateDto"%>
<%@page import="com.agiledge.atom.billingtype.config.service.KmBasedTemplateBillingService"%>
<%@page import="java.util.Date"%>

<%@page import="com.agiledge.atom.dao.OtherDao"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.nfl.com" prefix="disp"%>

<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Tracked Trip Sheet</title>
 <%
   
String date=request.getParameter("date");

        String mimeType = "application/vnd.ms-excel";        
        response.setContentType(mimeType);        
        String fname = new Date().toString();
        response.setHeader("Content-Disposition", "inline; filename = " + fname + ".xls");        
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        response.setHeader("Cache-Control", "post-check=0, pre-check=0");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Pragma", "public");
        response.setHeader("Expires", "Mon, 1 Jan 1995 05:00:00 GMT"); 
	 OtherDao ob1= OtherDao.getInstance();
 %>
 
 
	 
	  
</head>
<body>
		<%
		String id = request.getParameter("id");
		KmBasedTemplateBillingService kbs = new KmBasedTemplateBillingService();
     	KmTemplateDto kt=kbs.getKmTemplateDto(id);
     	KmTemplateChildDto dto = null;
     	 
     	if(kt!=null) { 
     		
		%>
		 
		<disp:dipxTable id="row"  list="<%=kt.getChildList() %>"  >
			<disp:dipxColumn title="Area" >${row.aplDto.area }</disp:dipxColumn>
			<disp:dipxColumn title="Place"  >${row.aplDto.place }</disp:dipxColumn>
			<disp:dipxColumn title="Landmark" >${row.aplDto.landMark }</disp:dipxColumn>
			<disp:dipxColumn title="Distance" property="distance" />
		</disp:dipxTable>
		
 
	<%} %>
 </body>
</html>

