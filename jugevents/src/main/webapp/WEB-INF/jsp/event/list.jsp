<%@ include file="../common.jspf" %>
<c:if test="${!empty news}">
    <h1><spring:message code="NewsAndUpcomings"/></h1>
    <%@ include file="news.jspf" %>
</c:if>
