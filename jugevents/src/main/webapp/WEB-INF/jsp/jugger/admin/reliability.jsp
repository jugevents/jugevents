<%@ include file="../../common.jspf" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@page import="java.util.HashMap"%>
<%@page import="java.util.EnumSet"%>
<%@page import="it.jugpadova.util.RRStatus"%>
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <%@ include file="../../head.jspf" %>   
        <script type="text/javascript">
        //simple javascript function to modify the value of reliability
        //according to the value selected for the status            
			function changeReliability() {  					
				var sts = document.getElementById('reliabilityRequest.status');
				var rel = document.getElementById('reliability');				  		
				if((sts.value==3)||(sts.value==4))
					rel.value=0;
				if(sts.value==2)
				    rel.value=1;				  
			   }
		</script>
        <style type="text/css">
    	</style>        
    </head>
    <body>
        <div id="nonFooter">
            <jsp:include page="../../header.jsp"/>
            <div id="content">
            <div id="content_main">
                                        
                    <h1><spring:message code="JuggerReliabilityAdministration"/></h1>                   
                    <form:form commandName="jugger" method="POST" action="${cp}/adminjugger/reliability.form" >
                        <form:errors path="*" cssClass="errorBox"/> 
                        <input name="jugger.user.username" type="hidden" value="${jugger.user.username}"/>               
                        
                       
                        <fieldset>
                            <legend>Jugger</legend>
                            <dl>
                                <dt><form:label path="firstName"><spring:message code="juggerRegistrationFirstName"/></form:label></dt>
                                <dd><form:input path="firstName" readonly="true"/></dd>
                                <dt><form:label path="lastName"><spring:message code="juggerRegistrationLastName"/></form:label></dt>
                                <dd><form:input path="lastName" readonly="true"/></dd>
                                <dt><form:label path="user.username"><spring:message code="username"/></form:label></dt>
                                <dd><form:input path="user.username" readonly="true"/></dd>
                                <dt><form:label path="email"><spring:message code="Email"/></form:label></dt>
                                <dd><form:input path="email" readonly="true" size="35"/></dd>                                
                            </dl>
                        </fieldset>
                        
                        <fieldset><legend>JUG</legend>
                            <span id="jugLogo"><img style="float: right;" src="${cp}/bin/jugLogo.bin?id=${jugger.jug.id}" alt="JUG Logo" width="100"/></span>
                            <dl>
                                <dt><form:label path="jug.name">
                                <spring:message code="juggerRegistrationJUGName" />
                                </form:label></dt>
                                <dd><form:input path="jug.name" readonly="true"/></dd>
                        	</dl>
                        </fieldset>
                        
                        <fieldset>
                            	<legend><spring:message code="ReliabilityRequest"/></legend>     
                            	 <dt><form:label path="reliability"><spring:message code="Reliability"/></form:label></dt>
                                 <dd><form:input path="reliability" /></dd>  
                                 <c:if test="${jugger.reliabilityRequest!=null}">                                 
	                                
	                                 
	                                 <dt><form:label path="reliabilityRequest.status"><spring:message code="RR.status"/></form:label></dt>
	                                 <dd><form:select path="reliabilityRequest.status" onchange="javascript:changeReliability();">
	                                 <% EnumSet<RRStatus> es = (EnumSet<RRStatus>)request.getAttribute("statusList");
	                                    boolean disabledOption=false;
	                                    for(RRStatus rrs:es)
	                                    {
	                                    	if((rrs.equals(RRStatus.NOT_REQUIRED))||(rrs.equals(RRStatus.RELIABILITY_REQUIRED))) 
	                                    		disabledOption=true; 
	                                    	else 
	                                    		disabledOption=false;
	                                    	%>
	                                    		<form:option value="<%= rrs.value %>" label="<%= rrs.description %>" disabled="<%= String.valueOf(disabledOption) %>"/>
	                                    	<%
	                                    }
	                                 %>						          
							        </form:select></dd> 
	                                 <dt><form:label path="reliabilityRequest.motivation"><spring:message code="RR.motivation"/></form:label></dt>
	                                 <dd><form:textarea path="reliabilityRequest.motivation"  disabled="true" /></dd> 
	                                 <dt><form:label path="reliabilityRequest.adminResponse"><spring:message code="RR.adminResponse"/></form:label></dt>
	                                 <dd><form:textarea path="reliabilityRequest.adminResponse"   /></dd> 
	                                 <dt><form:label path="reliabilityRequest.dateRequest"><spring:message code="RR.dateRequest"/></form:label></dt>
	                                 <dd><form:input path="reliabilityRequest.dateRequest"  disabled="true" /></dd> 
	                                 <dt><form:label path="reliabilityRequest.dateAdminResponse"><spring:message code="RR.dateAdminResponse"/></form:label></dt>
	                                 <dd><form:input path="reliabilityRequest.dateAdminResponse"  disabled="true" /></dd>                               
                                 </c:if>									
                            </fieldset>   
                        
                        
                        
                        <dl>
                            <dt>&nbsp;</dt>
                            <dd><input type="submit" value="<spring:message code='Update'/>" /><br/><br/></dd>                            
                            
                        </dl>
                        
                    </form:form>
                </div>
                <jsp:include page="../../menu.jsp"/>
            </div>
        </div>
        <jsp:include page="../../footer.jsp"/>
        
       
       
        
    </body>
</html>