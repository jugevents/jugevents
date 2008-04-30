<%@ include file="../common.jspf" %>
<script src="${cp}/dwr/interface/AjaxMethodsJS.js" type="text/javascript"></script>
<h1><spring:message code='event.resources'/></h1>
<div class="secondaryMenu">
    <a href="${cp}/event/show.html?id=${event.id}"><spring:message code="BackToTheEvent"/></a>
</div>
<%@ include file="show_brief.jspf" %>
<div class="displaytag">
    <c:forEach items="${event.eventResources}" var="resource">
        <div id="res${resource.id}" style="border: 1px solid gray; padding: 5px; margin: 2px; float: left; width: 31%;">
            <a href="${resource.url}">${resource.url}</a><br/>
            ${resource.description}
            <div style="text-align: right;">
            <authz:authorize ifAnyGranted="ROLE_ADMIN,ROLE_JUGGER">
                <%
            if (blos.getServicesBo().canCurrentUserManageEvent((it.jugpadova.po.Event) request.getAttribute(
                    "event"))) {
                %>
                <spring:message code='confirmDeleteEventResource' var="confirmDeleteEventResourceMessage" text="?confirmDeleteEventResourceMessage?"/>
                <a href="javascript: AjaxMethodsJS.deleteEventResource(${resource.id}, function (data) { if (data) $('res${resource.id}').remove();});" onclick="return confirm('${confirmDeleteEventResourceMessage}')"><spring:message code="delete"/></a>
                <%            }
                %>
            </authz:authorize>
            </div>
        </div>
    </c:forEach>
</div>
