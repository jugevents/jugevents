<%@ include file="../common.jspf" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <%@ include file="../head.jspf" %>
    </head>
    <body>
        <div id="nonFooter">    
            <jsp:include page="../header.jsp"/>
            <div id="content"> 
                <div id="content_main">
                    
                    <h1><spring:message code='Event'/></h1>
                    <jsp:useBean id="today" class="java.util.Date"/>
                    
                    <authz:authorize ifAnyGranted="ROLE_ADMIN,ROLE_JUGGER">
                        <c:if test="${event.owner.user.username == authentication.name || authentication.authorities[0] == 'ROLE_ADMIN'}">
                            <a href="${cp}/event/edit.form?id=${event.id}"><spring:message code="edit"/></a>
                        </c:if>
                    </authz:authorize>
                    <authz:authorize ifAnyGranted="ROLE_ADMIN,ROLE_JUGGER">
                        <c:if test="${event.owner.user.username == authentication.name || authentication.authorities[0] == 'ROLE_ADMIN'}">
                            <spring:message code='confirmDeleteEvent' var="confirmDeleteEventMessage"/>
                            <a href="${cp}/event/delete.html?id=${event.id}" onclick="return confirm('${confirmDeleteEventMessage}')"><spring:message code="delete"/></a>
                        </c:if>
                    </authz:authorize>
                    <c:if test="${today lt event.startDate}">
                        <a href="${cp}/event/registration.form?event.id=${event.id}"><spring:message code="register"/></a>
                    </c:if>
                    <authz:authorize ifAnyGranted="ROLE_ADMIN,ROLE_JUGGER">
                        <c:if test="${event.owner.user.username == authentication.name || authentication.authorities[0] == 'ROLE_ADMIN'}">
                            <a href="${cp}/event/participants.html?id=${event.id}"><spring:message code="participants"/></a>
                        </c:if>
                    </authz:authorize>
                    <authz:authorize ifAnyGranted="ROLE_ADMIN,ROLE_JUGGER">
                        <c:if test="${event.owner.user.username == authentication.name || authentication.authorities[0] == 'ROLE_ADMIN'}">
                            <a href="${cp}/event/winners.html?id=${event.id}"><spring:message code="winners"/></a>
                        </c:if>
                    </authz:authorize>
                    
                    <%@ include file="show.jspf" %>
                </div>
                <jsp:include page="../menu.jsp"/>
            </div>
        </div>
        <jsp:include page="../footer.jsp"/>        
    </body>
</html>