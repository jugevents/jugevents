<%@ include file="/WEB-INF/jsp/common.jspf" %>

<c:choose>
    <c:when test="${empty linkedEvent.id}">
        <h1><spring:message code="NewLinkedEvent" text="?NewLinkedEvent?"/></h1>
    </c:when>
    <c:otherwise>
        <h1><spring:message code="EditLinkedEvent" text="?EditLinkedEvent?"/></h1>
    </c:otherwise>
</c:choose>
<form:form commandName="linkedEvent" method="post" action="save.html" enctype="multipart/form-data">
    <div>
        <form:errors path="*" cssClass="errorBox"/>
        <c:if test="${!empty linkedEvent.id}">
            <span id="linkedEventBackground"><img style="float: right;" src="${cp}/bin/linkedEventBackground.bin?id=${linkedEvent.id}" alt="Linked event background" width="100"/></span>
        </c:if>
        <dl>
            <dt><form:label path="title"><spring:message code="Title" text="?Title?"/></form:label></dt>
            <dd><form:input path="title"/></dd>
            <dt><form:label path="url"><spring:message code="URL" text="?URL?"/></form:label></dt>
            <dd><form:input path="url"/></dd>
            <dt><form:label path="urlLabel"><spring:message code="URLLabel" text="?URLLabel?"/></form:label></dt>
            <dd><form:input path="urlLabel"/></dd>
            <dt><form:label path="startDate"><spring:message code="event.startDate"/></form:label></dt>
            <dd><form:input path="startDate" maxlength="10" size="10"/>&nbsp;<img id="startDate.calendar" src="${cp}/images/calendar.gif" alt="Calendar icon"/>&nbsp;<span class="smallText">(dd/MM/yyyy)</span></dd>
            <dt><form:label path="endDate"><spring:message code="event.endDate"/></form:label></dt>
            <dd><form:input path="endDate" maxlength="10" size="10"/>&nbsp;<img id="endDate.calendar" src="${cp}/images/calendar.gif" alt="Calendar icon"/>&nbsp;<span class="smallText">(dd/MM/yyyy)</span></dd>
            <dt><form:label path="expositionStartDate"><spring:message code="exposition.startDate" text="?exposition.startDate?"/></form:label></dt>
            <dd><form:input path="expositionStartDate" maxlength="10" size="10"/>&nbsp;<img id="expositionStartDate.calendar" src="${cp}/images/calendar.gif" alt="Calendar icon"/>&nbsp;<span class="smallText">(dd/MM/yyyy)</span></dd>
            <dt><form:label path="expositionEndDate"><spring:message code="exposition.endDate" text="?exposition.endDate?"/></form:label></dt>
            <dd><form:input path="expositionEndDate" maxlength="10" size="10"/>&nbsp;<img id="expositionEndDate.calendar" src="${cp}/images/calendar.gif" alt="Calendar icon"/>&nbsp;<span class="smallText">(dd/MM/yyyy)</span></dd>
            <dt><form:label path="background"><spring:message code="Background" text="?Background?"/></form:label></dt>
            <dd><input type="file" name="background"/></dd>
        </dl>
        <dl>
            <dt>&nbsp;</dt>
            <dd><input type="submit" value="<spring:message code='Submit'/>"/><br/><br/></dd>
        </dl>
    </div>
</form:form>

<script type="text/javascript">
    document.observe('dom:loaded' , function() {
        attachCalendar('startDate.calendar', 'startDate', false);
        attachCalendar('endDate.calendar', 'endDate', false);
        attachCalendar('expositionStartDate.calendar', 'expositionStartDate', false);
        attachCalendar('expositionEndDate.calendar', 'expositionEndDate', false);
    });
</script>