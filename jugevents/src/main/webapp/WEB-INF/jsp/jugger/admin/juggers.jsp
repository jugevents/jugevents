<%@ include file="../../common.jspf"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@page import="java.util.EnumSet"%>
<%@page import="it.jugpadova.util.RRStatus"%>
<html xmlns="http://www.w3.org/1999/xhtml">

   
<head>
<%@ include file="../../head.jspf"%>
<script src="${cp}/dwr/interface/juggerBo.js" type="text/javascript"></script>        
</head>
<body>
<div id="nonFooter"><jsp:include page="../header.jsp" />
<div id="content">
<div id="content_main">

<h1><spring:message code="JuggerList"/></h1>

<form:form commandName="juggerSearch" method="POST" action="${cp}/adminjugger/juggersearch.form">
                        <fieldset>
                            <legend><spring:message code='Search'/></legend>
                            <dl>
                                <dt><form:label path="username">username</form:label></dt>
                                <dd><form:input path="username"/></dd>
                                <dt><form:label path="JUGName">JUGName</form:label></dt>
                                <dd><form:input path="JUGName"/><div id="jugNameList" class="auto_complete"></div></dd>   
                                 <dt><form:label path="RRStatus"><spring:message code="RR.status"/></form:label></dt>
	                                 <dd><form:select path="RRStatus" >
	                                  <form:option value="-1" label="---" />	
	                                 <% EnumSet<RRStatus> es = EnumSet.allOf(RRStatus.class);	                                    
	                                    for(RRStatus rrs:es)
	                                    {	                                    	
	                                    	%>
	                                    		<form:option value="<%= rrs.value %>" label="<%= rrs.description %>" />
	                                    	<%
	                                    }
	                                 %>			
	                                		          
							        </form:select></dd> 
							        <dt>&nbsp;</dt><dd><input type="submit" value="<spring:message code='Search'/>"/></dd>
                            </dl>
                        </fieldset>
                    </form:form>



    
	<div class="displaytag">
               <display:table  id="jg"  name="juggers"   sort="list" pagesize="20" defaultsort="5" defaultorder="ascending" requestURI="juggersearch.form;" >
					<!--  name="sessionScope.juggers" -->
					<display:column titleKey="username" sortable="true">
						<a	href="${cp}/adminjugger/viewJugger.html?username=${jg.user.username}">${jg.user.username}</a>				
					</display:column>
					<display:column titleKey="juggerRegistrationJUGName" sortable="true">
						<c:out value="${jg.jug.name}" />				
					</display:column>
					<display:column titleKey="ReliabilityRequest" sortable="true">						
					<c:choose>
					<c:when test="${jg.reliabilityRequest!=null}">					
						${jg.reliabilityRequest.description}
						(<a href="${cp}/adminjugger/reliability.form?jugger.user.username=${jg.user.username}"><spring:message code="edit"/></a>)
						
					</c:when>
					<c:otherwise>NOT REQUIRED</c:otherwise>					  
					</c:choose>
					
					</display:column>
					<display:column titleKey="actions" >
						<a
							href="${cp}/jugger/edit.form?jugger.user.username=${jg.user.username}"><spring:message code="edit"/></a>
						<c:choose>
							<c:when test="${jg.user.enabled}">
								<a
									href="${cp}/adminjugger/disableJugger.html?username=${jg.user.username}"><spring:message code="disable"/></a>
							</c:when>
							<c:otherwise>
								<a
									href="${cp}/adminjugger/enableJugger.html?username=${jg.user.username}"><spring:message code="enable"/></a>
							</c:otherwise>														
						</c:choose>						
						 <spring:message code='confirmDeleteJugger' var="confirmDeleteJuggerMessage"/>
                         <a href="${cp}/adminjugger/delete.html?username=${jg.user.username}" onclick="return 		 confirm('${confirmDeleteJuggerMessage}')"><spring:message code="delete"/></a>				
					</display:column>
					
			    </display:table>
            </div>


</div>
<jsp:include page="../../menu.jsp" /></div>
</div>
<jsp:include page="../../footer.jsp" />

<script type="text/javascript">

            dwr.util.setEscapeHtml(false);           
            new Autocompleter.DWR('JUGName', 'jugNameList', updateJUGNameList, { valueSelector: singleValueSelector, partialChars: 0, fullSearch: true });

            function updateJUGNameList(autocompleter, token) {
            juggerBo.findPartialJugName(token, function(data) {
            autocompleter.setChoices(data)
            });
            }

            function singleValueSelector(tag) {
            return tag;
            }        
            
            

        </script>
</body>
</html>
