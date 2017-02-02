<%-- 
    Document   : get_site_vehicle
    Created on : Oct 30, 2012, 4:48:12 PM
    Author     : muhammad
--%>

<%@page import="com.agiledge.atom.dto.SiteDto"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.agiledge.atom.service.SiteVehicleService"%>
<%@page import="com.agiledge.atom.constants.AuditLogConstants"%>
<%
	String tableRowStart="<tr>" + 
		" <td> Site Name:Logica </td> " +
			" <td> " +
			" <td> </td>";
			
	%>
<table>
	<tr>
		<th width='20%'>Vehicle</th>		
	</tr>
	<tr>		
		<td></td>
	</tr>
	<%  int siteId = Integer.parseInt(request.getParameter("siteId"));
    ArrayList<SiteDto> siteDtoList = new ArrayList<SiteDto>();
    siteDtoList = new SiteVehicleService().getSiteVehicle(siteId);
    for (SiteDto siteDtoObj : siteDtoList) {
        siteDtoObj.getId();
        siteDtoObj.getName();
        siteDtoObj.getVehicleType();
        siteDtoObj.getVehicleTypeName();
        //outValue+="<tr><td>"+siteDtoObj.getName()+"</td><td>"+siteDtoObj.getVehicleTypeName()+"</td></tr>";%>
	<tr>
		<td><%=siteDtoObj.getVehicleTypeName() %></td>
		<td></td>
		<td><input type="button" class="formbutton"
			onclick="showAuditLog(<%=siteDtoObj.getVehicleType()%>,'<%=AuditLogConstants.SITE_MODULE%>');"
			value="Audit Log" /></td>
	</tr>
	<%
   System.out.println("vehicle"+siteDtoObj.getVehicleType());
   }%>
</table>



