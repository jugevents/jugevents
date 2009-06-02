<%@ include file="common.jspf" %>
<div id="topRuler">&nbsp;</div>
<div id="topRulerShadow">&nbsp;</div>
<div id="header">
    <div id="logo">
        <a href="${cp}/"><img src="${cp}/images/applicationLogo.jpg" alt="${artifactId} Logo" style="border: 0;" /></a>
    </div>
    <div id="login_menu">
        <security:authorize ifNotGranted="ROLE_ADMIN,ROLE_PARANCOE,ROLE_JUGGER">
            <span class="smallText"><a href="${cp}/login.secure"><spring:message code="Login" text="?Login?"/></a> <spring:message code="or" text="?or?"/> <a href="${cp}/jugger/registration.form"><spring:message code="registerAccount" text="?registerAccount?"/></a></span>
        </security:authorize>
        <security:authorize ifAnyGranted="ROLE_ADMIN,ROLE_PARANCOE,ROLE_JUGGER">
            <span class="smallText"><spring:message code="Welcome" text="?Welcome?"/> <span style="font-size: 110%; font-weight: bold;"><security:authentication property="principal.username"/></span></span><br/>
            <span class="smallText"><a href="${cp}/logout.secure"><spring:message code="Logout" text="?Logout?"/></a></span>
        </security:authorize>
        <security:authorize ifAnyGranted="ROLE_JUGGER">    
            <span class="smallText"><a href="${cp}/jugger/edit.form?jugger.user.username=<security:authentication property="principal.username"/>"><spring:message code="Edit-Jugger" text="?Edit-Jugger?"/></a></span>
        </security:authorize>
    </div>
    <!--div id="motto"><spring:message code="slogan" text="?slogan?"/></div-->
    <div id="languages"><jsp:include page="language.jsp"/></div>
</div>
