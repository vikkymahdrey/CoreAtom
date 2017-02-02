<%@page import="com.agiledge.atom.constants.SettingsConstant"%>
<div id="footer">

<%
	if(SettingsConstant.comp.equalsIgnoreCase("cd" )||SettingsConstant.comp.equalsIgnoreCase("cd1" )) {
		
		 %>

	<div class="footer-bottom">
	   <p>
	   For queries write to transport@cross-domain.com 
	   </p>
		<p>
			&copy; ATOm: Agiledge Transport Optimization manager (TM) All Rights
			Reserved: 2014 Agiledge Process Solutions Private Limited.
		</p>
	</div>
<%
	} else	if(SettingsConstant.comp.equalsIgnoreCase("gss" )||SettingsConstant.comp.equalsIgnoreCase("gss1" )) {
		
		 %>

	
	<div class="footer-bottom">
	    
		<p>
			&copy; ATOm: Agiledge Transport Optimization manager (TM) All Rights
			Reserved: 2014 Agiledge Process Solutions Private Limited.
		</p>
	</div>
	<%
	} else	{
		
		 %>
		<div class="footer-bottom">
	    
		<p>
			&copy; ATOm: Agiledge Transport Optimization manager (TM) All Rights
			Reserved: 2014 Agiledge Process Solutions Private Limited.
		</p>
	</div>
	<%} %>
	
</div>	