<%@ include file="../common.jspf" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <%@ include file="../head.jspf" %>
    </head>
    <body>
        <div id="nonFooter">    
            <jsp:include page="../header.jsp"/>
            <div id="content"> 
                <div id="content_main">
                    
                    <h1><spring:message code="RegisterToTheEvent"/></h1>
                    
                    <a href="${cp}/event/show.html?id=${event.id}"><spring:message code="BackToTheEvent"/></a>
                    
                    <c:if test="${!empty event.owner}">
                        <div class="jugLogoBox">
                            <a href="${event.owner.jug.webSite}"><img src="${cp}/bin/jugLogo.html?id=${event.owner.jug.id}" alt="JUG Logo" width="100"/></a>
                            <a href="${event.owner.jug.webSite}">${event.owner.jug.name}</a>
                        </div>
                    </c:if>
                    
                    <form:form commandName="registration" method="POST" action="${cp}/event/registration.form">
                        <form:hidden path="event.id"/>
                        <dl>
                            <dt><form:label path="participant.firstName"><spring:message code="first_name"/>:</form:label></dt>
                            <dd><form:input path="participant.firstName"/></dd>
                            <dt><form:label path="participant.lastName"><spring:message code="last_name"/>:</form:label></dt>
                            <dd><form:input path="participant.lastName"/></dd>
                            <dt><form:label path="participant.email"><spring:message code="Email"/>:</form:label></dt>
                            <dd><form:input path="participant.email"/></dd>
                            <dt><spring:message code="InsertCharactersInTheImage"/></dt>
                            <dd><form:input path="captchaResponse"/><br/><img src="${cp}/jcaptcha/image.html" alt="Captcha Image"/></dd>
                            <dt>&nbsp;</dt>
                            <dd><input type="submit" value="<spring:message code='RegisterYou'/>"/><br/><br/></dd>
                        </dl>
                        <form:errors path="*" cssClass="errorBox"/>
                    </form:form>
                    <%@ include file="show.jspf"%>
                </div>
                <jsp:include page="../menu.jsp"/>
            </div>
        </div>
        <jsp:include page="../footer.jsp"/>        
    </body>
</html>