<%@ include file="../common.jspf" %>
<jsp:useBean id="today" class="java.util.Date"/>
<div class="secondaryMenu">
    <security:authorize ifAnyGranted="ROLE_ADMIN,ROLE_JUGGER">
        <c:if test="${canCurrentUserManageEvent}">
            <a href="${cp}/event/edit.form?id=${event.id}"><spring:message code="edit" text="?edit?"/></a>
        </c:if>
    </security:authorize>
    <security:authorize ifAnyGranted="ROLE_ADMIN,ROLE_JUGGER">
        <c:if test="${canCurrentUserManageEvent}">
            <a href="${cp}/event/edit.form?copyId=${event.id}"><spring:message code="newLikeThis" text="?newLikeThis?"/></a>
        </c:if>
    </security:authorize>
    <security:authorize ifAnyGranted="ROLE_ADMIN,ROLE_JUGGER">
        <c:if test="${canCurrentUserManageEvent}">
            <a href="${cp}/event/resources.html?id=${event.id}"><spring:message code="resources" text="?resources?"/></a>
        </c:if>
    </security:authorize>
    <security:authorize ifAnyGranted="ROLE_ADMIN,ROLE_JUGGER">
        <c:if test="${canCurrentUserManageEvent}">
            <spring:message code='confirmDeleteEvent' var="confirmDeleteEventMessage" text="?confirmDeleteEvent?"/>
            <a href="${cp}/event/delete.html?id=${event.id}" onclick="return confirm('${confirmDeleteEventMessage}')"><spring:message code="delete" text="?delete?"/></a>
        </c:if>
    </security:authorize>
    <security:authorize ifAnyGranted="ROLE_ADMIN,ROLE_JUGGER">
        <c:if test="${canCurrentUserManageEvent}">
            <a href="${cp}/event/participants.html?id=${event.id}"><spring:message code="participants" text="?participants?"/></a>
        </c:if>
    </security:authorize>
    <security:authorize ifAnyGranted="ROLE_ADMIN,ROLE_JUGGER">
        <c:if test="${canCurrentUserManageEvent}">
            <a href="${cp}/event/winners.html?id=${event.id}"><spring:message code="winners" text="?winners?"/></a>
        </c:if>
    </security:authorize>
</div>

<%@include file="show.jspf" %>