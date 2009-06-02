<%@ include file="../../common.jspf" %>
<div id="res${param.id}" style="border: 1px solid gray; padding: 5px; margin: 2px; float: left; width: 31%; display: ${param.display};}">
    <a href="${param.url}"><c:out value="${param.abbreviatedUrl}" escapeXml="true"/></a><br/>
    <c:out value="${param.description}" escapeXml="true"/>
    <div style="text-align: right;" class="smallText">
        <security:authorize ifAnyGranted="ROLE_ADMIN,ROLE_JUGGER">
            <c:if test="${param.canUserManageTheEvent == 'true'}">                
                <spring:message code='confirmDeleteEventResource' var="confirmDeleteEventResourceMessage" text="?confirmDeleteEventResourceMessage?"/>
                <a href="#" onclick="modifyEventResource(${param.id}); return false;"><spring:message code="modify" text="?modify?"/></a>
                <a href="javascript: deleteEventResource(${param.id})" onclick="return confirm('${confirmDeleteEventResourceMessage}')"><spring:message code="delete"/></a>
            </c:if>
        </security:authorize>
    </div>
</div>
