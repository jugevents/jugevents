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
        <c:if test="${resource.class.name == 'it.jugpadova.po.EventLink'}">
            <jsp:include page="resources/link.jsp">
                <jsp:param name="id" value="${resource.id}"/>
                <jsp:param name="url" value="${resource.url}"/>
                <jsp:param name="abbreviatedUrl" value="${resource.abbreviatedUrl}"/>
                <jsp:param name="description" value="${resource.description}"/>
                <jsp:param name="canUserManageTheEvent" value='<%= blos.getServicesBo().canCurrentUserManageEvent((it.jugpadova.po.Event) request.getAttribute("event")) %>'/>
                <jsp:param name="display" value="block"/>
            </jsp:include>
        </c:if>
        <c:if test="${resource.class.name == 'it.jugpadova.po.SlideShareResource'}">
            <jsp:include page="resources/slideshare.jsp">
                <jsp:param name="id" value="${resource.id}"/>
                <jsp:param name="embedCode" value="${resource.embedCode}"/>
                <jsp:param name="url" value="${resource.url}"/>
                <jsp:param name="abbreviatedUrl" value="${resource.abbreviatedUrl}"/>
                <jsp:param name="description" value="${resource.description}"/>
                <jsp:param name="canUserManageTheEvent" value='<%= blos.getServicesBo().canCurrentUserManageEvent((it.jugpadova.po.Event) request.getAttribute("event")) %>'/>
                <jsp:param name="display" value="block"/>
            </jsp:include>
        </c:if>
    </c:forEach>
</div>
<br style="clear: both;"/>
<div id="resourceFormDiv" style="display: none;">
    <form id="resourceForm" method="post">
        <input id="resourceId" name="resourceId" type="hidden" value=""/>
        <input id="eventId" name="eventId" type="hidden" value="${event.id}"/>
        <dl>
            <dt><spring:message code="ResourceType" text="?ResourceType?"/>:</dt>
            <dd>
                <select id="resourceType" name="resourceType">
                    <option value="link">Web Link</option>
                    <option value="slideshare">SlideShare</option>
                </select>
                <span id="linkLink" class="smallText">a simple Web link</span>
                <span id="slideshareLink" class="smallText" style="display: none;"><a href="http://www.slideshare.net">www.slideshare.net</a></span>
            </dd>
        </dl>
        <div id="linkFields">
            <dl>
                <dt><spring:message code="URL" text="?URL?"/>:</dt>
                <dd><input id="linkUrl" name="linkUrl" type="text" size="50"/></dd>
                <dt><spring:message code="Description" text="?Description?"/>:</dt>
                <dd><input id="linkDescription" name="linkDescription" type="text" size="50"/></dd>
            </dl>
        </div>
        <div id="slideshareFields" style="display: none;">
            <dl>
                <dt><spring:message code="SlideShareId" text="?SlideShareId?"/>:</dt>
                <dd><input id="slideshareId" name="slideshareId" type="text" size="10"/>&nbsp;<img id="tip_slideshareId" src="${cp}/images/question16x16.png" alt="Help Tip"/></dd>
                <dt><spring:message code="Description" text="?Description?"/>:</dt>
                <dd><input id="slideshareDescription" name="slideshareDescription" type="text" size="50"/></dd>
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

<script type="text/javascript">
    new Tip($('tip_slideshareId'), '<spring:message code="tip.slideshareId" text="?tip.slideshareId?"/>', {title: '<spring:message code="tip.slideshareId.title" text="?tip.slideshareId.title?"/>', effect: 'appear'});
</script>