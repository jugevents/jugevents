<%@ include file="../common.jspf" %>
<h1><spring:message code="RegisterTo" text="?Register to?"/> &quot;${event.title}&quot;</h1>

<a href="${cp}/event/${event.id}"><spring:message code="BackToTheEvent"/></a>

<c:if test="${!empty event.owner}">
    <div class="jugLogoBox" style="float: right;">
        <a href="${event.owner.jug.webSiteUrl}"><img src="${cp}/bin/jugLogo.bin?id=${event.owner.jug.id}" alt="JUG Logo" width="100"/></a>
        <a href="${event.owner.jug.webSiteUrl}">${event.owner.jug.name}</a>
    </div>
</c:if>

<form:form commandName="registration" method="post" action="${cp}/event/registration.form">
    <div>
        <form:hidden path="event.id"/>
        <form:errors path="*" cssClass="errorBox"/>                        
        <dl>
            <dt><form:label path="participant.firstName"><spring:message code="first_name"/>:</form:label></dt>
            <dd><form:input path="participant.firstName"/></dd>
            <dt><form:label path="participant.lastName"><spring:message code="last_name"/>:</form:label></dt>
            <dd><form:input path="participant.lastName"/></dd>
            <dt><form:label path="participant.showFullLastName"><spring:message code="show_full_last_name" text="?show_full_last_name?"/>:</form:label></dt>
            <dd><form:checkbox path="participant.showFullLastName"/> <img id="tip_showFullLastName" src="${cp}/images/question16x16.png" alt="Help Tip"/></dd>
            <dt><form:label path="participant.email"><spring:message code="Email"/>:</form:label></dt>
            <dd><form:input path="participant.email"/></dd>
            <dt><form:label path="participant.note"><spring:message code="Note"/>:</form:label></dt>
            <dd><form:textarea path="participant.note" rows="6" cols="25"/></dd>
            <%-- //TODO fix this problem, try to find the way to evaluate constants in the jstl. I would prefer to use NumOfDaysReminder.NOT_ACTIVE instead   --%>
            <c:if test="${event.reminderDate != null}">
                <dt><form:label path="participant.reminderEnabled"><spring:message code="participant.enableReminder" text="?participant.enableReminder?"/>:</form:label></dt>
                <dd><form:checkbox id="participant.reminderEnabled" path="participant.reminderEnabled"></form:checkbox>
                    <img id="tip_enableReminder" src="${cp}/images/question16x16.png" alt="Help Tip"/></dd>
                <script type="text/javascript">
                    new Tip($('tip_enableReminder'), '<spring:message code="tip.enableReminder"/>', {title: '<spring:message code="tip.enableReminder.title"/>', effect: 'appear'});
                </script>
            </c:if>
            <dt><spring:message code="InsertCharactersInTheImage"/></dt>
            <dd><form:input path="captchaResponse"/><br/><img src="${cp}/jcaptcha/image.html" alt="Captcha Image"/></dd>
            <dt>&nbsp;</dt>
            <dd><input type="submit" value="<spring:message code='RegisterYou'/>"/><br/><br/></dd>
        </dl>
    </div>
</form:form>

<script type="text/javascript">
    new Tip($('tip_showFullLastName'), '<spring:message code="tip.showFullLastName" text="?tip.showFullLastName?"/>', {title: '<spring:message code="tip.showFullLastName.title" text="?tip.showFullLastName.title?"/>', effect: 'appear'});
</script>
