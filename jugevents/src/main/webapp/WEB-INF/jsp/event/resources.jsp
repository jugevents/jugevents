<%@ include file="../common.jspf" %>
<script src="${cp}/javascripts/builder.js" type="text/javascript"></script>
<script src="${cp}/dwr/interface/AjaxMethodsJS.js" type="text/javascript"></script>
<script src="${cp}/dwr/interface/eventResourceBo.js" type="text/javascript"></script>
<script src="${cp}/javascripts/jugevents/eventResources.js" type="text/javascript"></script>
<h1><spring:message code='event.resources'/></h1>
<div class="secondaryMenu">
    <a href="${cp}/event/show.html?id=${event.id}"><spring:message code="BackToTheEvent"/></a>
    <a id="newResourceLink" href="#"><spring:message code="AddResource" text="?AddResource?"/></a>
</div>
<%@ include file="show_brief.jspf" %>
<div id="resources">
    <c:forEach items="${event.eventResources}" var="resource">
        <jsp:include page="resources/link.jsp">
            <jsp:param name="id" value="${resource.id}"/>
            <jsp:param name="url" value="${resource.url}"/>
            <jsp:param name="abbreviatedUrl" value="${resource.abbreviatedUrl}"/>
            <jsp:param name="description" value="${resource.description}"/>
            <jsp:param name="canUserManageTheEvent" value='<%= blos.getServicesBo().canCurrentUserManageEvent((it.jugpadova.po.Event) request.getAttribute("event")) %>'/>
            <jsp:param name="display" value="block"/>
        </jsp:include>
    </c:forEach>
</div>
<br style="clear: both;"/>
<div id="resourceFormDiv" style="display: none;">
    <form id="resourceForm" method="post">
        <input id="resourceId" name="resourceId" type="hidden" value=""/>
        <input id="eventId" name="eventId" type="hidden" value="${event.id}"/>
        <div id="linkFields">
            <dl>
                <dt><spring:message code="URL" text="?URL?"/>:</dt>
                <dd><input id="linkUrl" name="linkUrl" type="text" size="50"/></dd>
                <dt><spring:message code="Description" text="?Description?"/>:</dt>
                <dd><input id="linkDescription" name="linkDescription" type="text" size="50"/></dd>
            </dl>
        </div>
        <dl>
            <dt>&nbsp;</dt>
            <dd>
                <input id="addResourceButton" type="submit" value="<spring:message code='AddResource' text="?AddResource?"/>">
                <input id="updateResourceButton" type="submit" value="<spring:message code='UpdateResource' text="?UpdateResource?"/>" style="display: none;">
            </dd>
        </dl>        
    </form>
</div>