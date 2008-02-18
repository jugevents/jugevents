<%@ include file="../../common.jspf"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">

   
<head>
<%@ include file="../../head.jspf"%>
</head>
<body>
<div id="nonFooter"><jsp:include page="../../header.jsp" />
<div id="content">
<div id="content_main">

<h1><spring:message code="JuggerList"/></h1>



    
	<div class="displaytag">
               <display:table  id="jg"  name="juggers"  sort="list" pagesize="20" defaultsort="5" defaultorder="ascending" requestURI="list.html" >
					
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
</body>
</html>
