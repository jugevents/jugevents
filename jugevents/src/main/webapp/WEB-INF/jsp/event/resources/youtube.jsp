<%@ page pageEncoding="UTF-8" %>
<%@ include file="../../common.jspf" %>
<div id="res${param.id}" style="border: 1px solid gray; padding: 5px; margin: 2px; float: left; width: 425px; display: ${param.display};}">
    <object width="425" height="355"><param name="movie" value="http://www.youtube.com/v/${param.videoId}"></param><param name="wmode" value="transparent"></param><embed src="http://www.youtube.com/v/${param.videoId}" type="application/x-shockwave-flash" wmode="transparent" width="425" height="355"></embed></object>
    <c:out value="${param.description}" escapeXml="true"/>
    <div style="text-align: right;" class="smallText">
        <c:if test="${param.canUserManageTheEvent == 'true'}">                
            <spring:message code='confirmDeleteEventResource' var="confirmDeleteEventResourceMessage" text="?confirmDeleteEventResourceMessage?"/>
            <a href="#" onclick="modifyEventResource(${param.id}); $('resourceType').disable(); return false;"><spring:message code="modify" text="?modify?"/></a>
            <a href="javascript: deleteEventResource(${param.id})" onclick="return confirm('${confirmDeleteEventResourceMessage}')"><spring:message code="delete"/></a>
        </c:if>
    </div>
</div>
