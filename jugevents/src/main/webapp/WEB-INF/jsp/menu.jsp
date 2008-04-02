<%@ include file="common.jspf" %>
<div id="content_menu">
    <!--p class="menuTitle"><spring:message code="label_menu"/></p-->
    <span class="menuLevel0"><a href="${cp}/event/search.form"><spring:message code="Search"/></a></span>
    <authz:authorize ifAnyGranted="ROLE_ADMIN,ROLE_JUGGER">   			
        <span class="menuLevel0"><a href="${cp}/event/edit.form"><spring:message code="NewEvent"/></a></span>
    </authz:authorize>
    <span class="menuLevel0"><a href="${cp}/service/services.html"><spring:message code="Services"/></a></span>
    <authz:authorize ifAnyGranted="ROLE_ADMIN">   			
        <span class="menuLevel0"><a href="${cp}/admin/index.html"><spring:message code="menu_administration"/></a></span>
    </authz:authorize>         
    <authz:authorize ifAnyGranted="ROLE_ADMIN">   			
        <span class="menuLevel0"><a href="${cp}/adminjugger/juggersearch.form"><spring:message code="JuggerList"/></a></span>
    </authz:authorize>
</div>
