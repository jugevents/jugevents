<%@ include file="common.jspf" %>
<div id="content_menu">
    <span class="menuLevel0"><a href="${cp}/"><spring:message code="News" text="?News?"/></a></span>
    <span class="menuLevel0"><a href="${cp}/event/search.form"><spring:message code="Search"/></a></span>
    <security:authorize ifAnyGranted="ROLE_ADMIN,ROLE_JUGGER">   			
        <span class="menuLevel0"><a href="${cp}/event/edit.form"><spring:message code="NewEvent"/></a></span>
    </security:authorize>
    <span class="menuLevel0"><a href="${cp}/service/services.html"><spring:message code="Services"/></a></span>
    <security:authorize ifAnyGranted="ROLE_ADMIN">   			
        <span class="menuLevel0"><a href="${cp}/admin/index.html"><spring:message code="menu_administration"/></a></span>
    </security:authorize>         
    <security:authorize ifAnyGranted="ROLE_ADMIN">   			
        <span class="menuLevel0"><a href="${cp}/adminjugger/juggersearch.form"><spring:message code="JuggerList"/></a></span>
    </security:authorize>
</div>
