<%@ include file="../../common.jspf"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@page import="it.jugpadova.po.Jugger"%>
<html xmlns="http://www.w3.org/1999/xhtml">

   
<head>
<%@ include file="../../head.jspf"%>
</head>
<body>
<div id="nonFooter"><jsp:include page="../../header.jsp" />
<div id="content">
<div id="content_main">

<h1>Juggers List</h1>



    
	<div class="displaytag">
               <display:table  id="jg"  name="juggers"  sort="list" pagesize="20" defaultsort="5" defaultorder="ascending" requestURI="list.html" >
					
					<display:column title="username" sortable="true">
						<a	href="${cp}/adminjugger/viewJugger.html?username=${jg.user.username}">${jg.user.username}</a>				
					</display:column>
					<display:column title="JUG Name" sortable="true">
						<c:out value="${jg.jug.name}" />				
					</display:column>
					<display:column title="Reliability Request" sortable="true">						
					  <% 
						if(((Jugger)jg).getReliabilityRequest()!= null) {	%>
							<%= ((Jugger)jg).getReliabilityRequest().statusDescription() %>
							<%
							} else {
							%>
							NOT REQUIRED
							<% } %>
					</display:column>
					<display:column title="actions" >
						<a
							href="${cp}/jugger/edit.form?jugger.user.username=${jg.user.username}">edit</a>
						<c:choose>
							<c:when test="${jg.user.enabled}">
								<a
									href="${cp}/adminjugger/disableJugger.html?username=${jg.user.username}">disable</a>
							</c:when>
							<c:otherwise>
								<a
									href="${cp}/adminjugger/enableJugger.html?username=${jg.user.username}">enable</a>
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
