<%@ include file="../common.jspf" %>
<jsp:useBean id="today" class="java.util.Date"/>
<div class="secondaryMenu">
    <authz:authorize ifAnyGranted="ROLE_ADMIN,ROLE_JUGGER">
        <c:if test="${canCurrentUserManageEvent}">
            <a href="${cp}/event/edit.form?id=${event.id}"><spring:message code="edit"/></a>
        </c:if>
    </authz:authorize>
    <authz:authorize ifAnyGranted="ROLE_ADMIN,ROLE_JUGGER">
        <c:if test="${canCurrentUserManageEvent}">
            <spring:message code='confirmDeleteEvent' var="confirmDeleteEventMessage"/>
            <a href="${cp}/event/delete.html?id=${event.id}" onclick="return confirm('${confirmDeleteEventMessage}')"><spring:message code="delete"/></a>
        </c:if>
    </authz:authorize>
    <authz:authorize ifAnyGranted="ROLE_ADMIN,ROLE_JUGGER">
        <c:if test="${canCurrentUserManageEvent}">
            <a href="${cp}/event/participants.html?id=${event.id}"><spring:message code="participants"/></a>
        </c:if>
    </authz:authorize>
    <authz:authorize ifAnyGranted="ROLE_ADMIN,ROLE_JUGGER">
        <c:if test="${canCurrentUserManageEvent}">
            <a href="${cp}/event/winners.html?id=${event.id}"><spring:message code="winners"/></a>
        </c:if>
    </authz:authorize>
</div>

<div class="eventTitle">${event.title}</div>

<c:if test="${event.registrationOpen}">
    <a href="${cp}/event/registration.form?event.id=${event.id}"><div class="je_button"><spring:message code="register"/></div></a>
    <c:if test="${(!empty event.registration) && event.registration.registrationRulesAreApplied}">
        <c:if test="${!empty event.registration.maxParticipants}">
            <div style="text-align: center;">
                <spring:message code="event.registration.message.availableSeats" text="?event.registration.message.availableSeats?" arguments="${event.registration.maxParticipants - event.numberOfParticipants}"/>
            </div>
        </c:if>
        <c:if test="${!empty event.registration.endRegistration}">
            <div style="text-align: center;">
                <fmt:formatDate value='${event.registration.endRegistration}' type='both' dateStyle='short' timeStyle="short" var="endRegistrationDate"/>
                <spring:message code="event.registration.message.endRegistrationAt" text="?event.registration.message.endRegistrationAt?" arguments="${endRegistrationDate}"/>
            </div>
        </c:if>
        <c:if test="${empty event.registration.endRegistration}">
            <div style="text-align: center;">
                <fmt:formatDate value='${event.startDate}' type='date' dateStyle='short' var="endRegistrationDate"/>
                <spring:message code="event.registration.message.endRegistrationAt" text="?event.registration.message.endRegistrationAt?" arguments="${endRegistrationDate}"/>
            </div>
        </c:if>
    </c:if>
</c:if>

<c:if test="${!empty event.owner}">
    <div class="jugLogoBox">
        <a href="${event.owner.jug.webSiteUrl}"><img src="${cp}/bin/jugLogo.bin?id=${event.owner.jug.id}" alt="JUG Logo" width="100"/></a>
        <a href="${event.owner.jug.webSiteUrl}">${event.owner.jug.name}</a>
    </div>
</c:if>

<%@ include file="show.jspf" %>

<c:if test="${event.registrationOpen}">
    <a href="${cp}/event/registration.form?event.id=${event.id}"><div class="je_button"><spring:message code="register"/></div></a>
</c:if>
