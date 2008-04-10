<%@ include file="../common.jspf" %>
<link href="${cp}/event/rss.html?continent=${eventSearch.continent}&country=${eventSearch.country}&jugName=${eventSearch.jugName}&pastEvents=${eventSearch.pastEvents}&order=${eventSearch.orderByDate}" rel="alternate" title="RSS" type="application/rss+xml" />
<c:if test="${!empty news}">
    <h1><spring:message code="NewsAndUpcomings"/></h1>
    <%@ include file="news.jspf" %>
</c:if>
