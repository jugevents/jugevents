<%@ include file="../common.jspf" %>
<jwr:script src="${cp}/dwr/interface/eventBo.js" />
<jwr:script src="${cp}/dwr/interface/filterBo.js" />
<c:choose>
    <c:when test="${empty event.id}">
        <h1><spring:message code="NewEvent"/></h1>
    </c:when>
    <c:otherwise>
        <h1><spring:message code="EditEvent"/></h1>                            
    </c:otherwise>
</c:choose>
<form:form commandName="event" method="post" action="${cp}/event/edit.form">
    <div>
        <form:errors path="*" cssClass="errorBox"/>
        <form:hidden path="id"/>
        <dl>
            <dt><form:label path="title"><spring:message code="event.title"/></form:label></dt>
            <dd><form:input path="title"/></dd>
            <dt><form:label path="location"><spring:message code="event.location"/></form:label></dt>
            <dd>
                <form:input path="location"/><div id="locationList" class="auto_complete"></div>                                
            </dd>
            <dt>
                <form:label path="directions"><spring:message code="event.directions"/></form:label><br/>
                (<a href="http://hobix.com/textile/" rel="external">Textile</a>)
            </dt>
            <dd><form:textarea path="directions" cols="38" rows="5"/></dd>
            <dt>&nbsp;</dt>
            <dd><div id="directionsPreview" class="preview">${requestScope.event.filteredDirections}&nbsp;</div></dd>
            <dt><form:label path="startDate"><spring:message code="event.startDate"/></form:label></dt>
            <dd><form:input path="startDate" maxlength="10" size="10"/>&nbsp;<img id="startDate.calendar" src="${cp}/images/calendar.gif" alt="Calendar icon"/>&nbsp;<span class="smallText">(dd/MM/yyyy)</span></dd>
            <dt><form:label path="startTime"><spring:message code="event.startTime"/></form:label></dt>
            <dd>
                <form:select path="startTime">
                    <form:option value="00:00 AM" label="00:00 AM"/>
                    <form:option value="00:30 AM" label="00:30 AM"/>
                    <form:option value="01:00 AM" label="01:00 AM"/>
                    <form:option value="01:30 AM" label="01:30 AM"/>
                    <form:option value="02:00 AM" label="02:00 AM"/>
                    <form:option value="02:30 AM" label="02:30 AM"/>
                    <form:option value="03:00 AM" label="03:00 AM"/>
                    <form:option value="03:30 AM" label="03:30 AM"/>
                    <form:option value="04:00 AM" label="04:00 AM"/>
                    <form:option value="04:30 AM" label="04:30 AM"/>
                    <form:option value="05:00 AM" label="05:00 AM"/>
                    <form:option value="05:30 AM" label="05:30 AM"/>
                    <form:option value="06:00 AM" label="06:00 AM"/>
                    <form:option value="06:30 AM" label="06:30 AM"/>
                    <form:option value="07:00 AM" label="07:00 AM"/>
                    <form:option value="07:30 AM" label="07:30 AM"/>
                    <form:option value="08:00 AM" label="08:00 AM"/>
                    <form:option value="08:30 AM" label="08:30 AM"/>
                    <form:option value="09:00 AM" label="09:00 AM"/>
                    <form:option value="09:30 AM" label="09:30 AM"/>
                    <form:option value="10:00 AM" label="10:00 AM"/>
                    <form:option value="10:30 AM" label="10:30 AM"/>
                    <form:option value="11:00 AM" label="11:00 AM"/>
                    <form:option value="11:30 AM" label="11:30 AM"/>
                    <form:option value="12:00 AM" label="12:00 AM"/>
                    <form:option value="12:30 AM" label="12:30 AM"/>
                    <form:option value="01:00 PM" label="01:00 PM"/>
                    <form:option value="01:30 PM" label="01:30 PM"/>
                    <form:option value="02:00 PM" label="02:00 PM"/>
                    <form:option value="02:30 PM" label="02:30 PM"/>
                    <form:option value="03:00 PM" label="03:00 PM"/>
                    <form:option value="03:30 PM" label="03:30 PM"/>
                    <form:option value="04:00 PM" label="04:00 PM"/>
                    <form:option value="04:30 PM" label="04:30 PM"/>
                    <form:option value="05:00 PM" label="05:00 PM"/>
                    <form:option value="05:30 PM" label="05:30 PM"/>
                    <form:option value="06:00 PM" label="06:00 PM"/>
                    <form:option value="06:30 PM" label="06:30 PM"/>
                    <form:option value="07:00 PM" label="07:00 PM"/>
                    <form:option value="07:30 PM" label="07:30 PM"/>
                    <form:option value="08:00 PM" label="08:00 PM"/>
                    <form:option value="08:30 PM" label="08:30 PM"/>
                    <form:option value="09:00 PM" label="09:00 PM"/>
                    <form:option value="09:30 PM" label="09:30 PM"/>
                    <form:option value="10:00 PM" label="10:00 PM"/>
                    <form:option value="10:30 PM" label="10:30 PM"/>
                    <form:option value="11:00 PM" label="11:00 PM"/>
                    <form:option value="11:30 PM" label="11:30 PM"/>
                </form:select>
            </dd>
            <dt><form:label path="endDate"><spring:message code="event.endDate"/></form:label></dt>
            <dd><form:input path="endDate" maxlength="10" size="10"/>&nbsp;<img id="endDate.calendar" src="${cp}/images/calendar.gif" alt="Calendar icon"/>&nbsp;<span class="smallText">(dd/MM/yyyy)</span></dd>
            <dt><form:label path="endTime"><spring:message code="event.endTime"/></form:label></dt>
            <dd>
                <form:select path="endTime">
                    <form:option value="00:00 AM" label="00:00 AM"/>
                    <form:option value="00:30 AM" label="00:30 AM"/>
                    <form:option value="01:00 AM" label="01:00 AM"/>
                    <form:option value="01:30 AM" label="01:30 AM"/>
                    <form:option value="02:00 AM" label="02:00 AM"/>
                    <form:option value="02:30 AM" label="02:30 AM"/>
                    <form:option value="03:00 AM" label="03:00 AM"/>
                    <form:option value="03:30 AM" label="03:30 AM"/>
                    <form:option value="04:00 AM" label="04:00 AM"/>
                    <form:option value="04:30 AM" label="04:30 AM"/>
                    <form:option value="05:00 AM" label="05:00 AM"/>
                    <form:option value="05:30 AM" label="05:30 AM"/>
                    <form:option value="06:00 AM" label="06:00 AM"/>
                    <form:option value="06:30 AM" label="06:30 AM"/>
                    <form:option value="07:00 AM" label="07:00 AM"/>
                    <form:option value="07:30 AM" label="07:30 AM"/>
                    <form:option value="08:00 AM" label="08:00 AM"/>
                    <form:option value="08:30 AM" label="08:30 AM"/>
                    <form:option value="09:00 AM" label="09:00 AM"/>
                    <form:option value="09:30 AM" label="09:30 AM"/>
                    <form:option value="10:00 AM" label="10:00 AM"/>
                    <form:option value="10:30 AM" label="10:30 AM"/>
                    <form:option value="11:00 AM" label="11:00 AM"/>
                    <form:option value="11:30 AM" label="11:30 AM"/>
                    <form:option value="12:00 AM" label="12:00 AM"/>
                    <form:option value="12:30 AM" label="12:30 AM"/>
                    <form:option value="01:00 PM" label="01:00 PM"/>
                    <form:option value="01:30 PM" label="01:30 PM"/>
                    <form:option value="02:00 PM" label="02:00 PM"/>
                    <form:option value="02:30 PM" label="02:30 PM"/>
                    <form:option value="03:00 PM" label="03:00 PM"/>
                    <form:option value="03:30 PM" label="03:30 PM"/>
                    <form:option value="04:00 PM" label="04:00 PM"/>
                    <form:option value="04:30 PM" label="04:30 PM"/>
                    <form:option value="05:00 PM" label="05:00 PM"/>
                    <form:option value="05:30 PM" label="05:30 PM"/>
                    <form:option value="06:00 PM" label="06:00 PM"/>
                    <form:option value="06:30 PM" label="06:30 PM"/>
                    <form:option value="07:00 PM" label="07:00 PM"/>
                    <form:option value="07:30 PM" label="07:30 PM"/>
                    <form:option value="08:00 PM" label="08:00 PM"/>
                    <form:option value="08:30 PM" label="08:30 PM"/>
                    <form:option value="09:00 PM" label="09:00 PM"/>
                    <form:option value="09:30 PM" label="09:30 PM"/>
                    <form:option value="10:00 PM" label="10:00 PM"/>
                    <form:option value="10:30 PM" label="10:30 PM"/>
                    <form:option value="11:00 PM" label="11:00 PM"/>
                    <form:option value="11:30 PM" label="11:30 PM"/>
                </form:select>
            </dd>
            <dt>
                <form:label path="description"><spring:message code="event.description"/></form:label><br/>
                (<a href="http://hobix.com/textile/" rel="external">Textile</a>)
            </dt>
            <dd><form:textarea path="description" cols="38" rows="8" /></dd>
            <dt>&nbsp;</dt>
            <dd><div id="descriptionPreview" class="preview">${requestScope.event.filteredDescription}&nbsp;</div></dd>
            <dt><form:label path="registration.enabled"><spring:message code="event.registration.enabled" text="?event.registration.enabled?"/></form:label></dt>
            <dd><form:checkbox id="registration.enabled" path="registration.enabled"/></dd>
        </dl>
        <div id="registrationFieldsDiv" style="margin-bottom: 1em;<c:if test='${!event.registration.enabled}'> display: none;</c:if>">
            <fieldset>
                <legend><spring:message code="RegistrationToTheEvent" text="?RegistrationToTheEvent?"/></legend>
                <dl>
                    <dt><form:label path="registration.startRegistration"><spring:message code="event.registration.startRegistration" text="?event.registration.startRegistration?"/></form:label></dt>
                    <dd><form:input path="registration.startRegistration" maxlength="16" size="16"/>&nbsp;<img id="registration.startRegistration.calendar" src="${cp}/images/calendar.gif" alt="Calendar icon" />&nbsp;<span class="smallText">(dd/MM/yyyy HH:mm)</span></dd>
                    <dt><form:label path="registration.endRegistration"><spring:message code="event.registration.endRegistration" text="?event.registration.endRegistration?"/></form:label></dt>
                    <dd><form:input path="registration.endRegistration" maxlength="16" size="16"/>&nbsp;<img id="registration.endRegistration.calendar" src="${cp}/images/calendar.gif" alt="Calendar icon" />&nbsp;<span class="smallText">(dd/MM/yyyy HH:mm)</span></dd>
                    <dt><form:label path="registration.maxParticipants"><spring:message code="event.registration.maxParticipants" text="?event.registration.maxParticipants?"/></form:label></dt>
                    <dd><form:input path="registration.maxParticipants" maxlength="7" size="7"/></dd>
                    <dt><form:label path="registration.manualActivation"><spring:message code="event.registration.manualActivation" text="?event.registration.manualActivation?"/>:</form:label></dt>
                    <dd><form:radiobutton path="registration.manualActivation" value="" onclick="enableRegistrationRulesFields(true)"/><spring:message code="No" text="?No?"/>&nbsp;<form:radiobutton path="registration.manualActivation" value="true" onclick="enableRegistrationRulesFields(false)"/><spring:message code="Enabled" text="?Enabled?"/>&nbsp;<form:radiobutton path="registration.manualActivation" value="false" onclick="enableRegistrationRulesFields(false)"/><spring:message code="Disabled" text="?Disabled?"/></dd>
                </dl>
            </fieldset>
        </div>
        <dl>
            <dt>&nbsp;</dt>
            <dd><input type="submit" value="<spring:message code='Submit'/>"/><br/><br/></dd>
        </dl>
    </div>
</form:form>
<script type="text/javascript">
    dwr.util.setEscapeHtml(false);
            
    new Autocompleter.DWR('location', 'locationList', updateLocationList, { partialChars: 0, fullSearch: true, updateElement: populateDirections });

    function updateLocationList(autocompleter, token) {
        eventBo.findPartialLocation(token, '<authz:authentication operation="username"/>', function(data) {
        autocompleter.setChoices(data)
    });
}

function populateDirections(selectedElement) {
    eventBo.copyDirectionsFromEvent(selectedElement.childNodes[3].childNodes[0].nodeValue);
}

new Form.Element.Observer('description', 2,
function(el, value) {
    filterBo.populatePreview(value, 'Textile', 'descriptionPreview');
});

new Form.Element.Observer('directions', 2,
function(el, value) {
    filterBo.populatePreview(value, 'Textile', 'directionsPreview');
});

new Form.Element.Observer('registration.enabled', 0.8,
function(el, value) {
    Effect.toggle($('registrationFieldsDiv'), 'blind', {duration: 0.4, queue: {scope: 'registrationFieldsDiv', position: 'end'}});
});

attachCalendar('startDate.calendar', 'startDate', false);            
attachCalendar('endDate.calendar', 'endDate', false);            
attachCalendar('registration.startRegistration.calendar', 'registration.startRegistration', true);
attachCalendar('registration.endRegistration.calendar', 'registration.endRegistration', true);

new Form.Element.Observer('startDate', 1,
function(el, value) {
    $('endDate').value = $('startDate').value;
    $('registration.endRegistration').value = $('startDate').value+' 12:00';
});

function enableRegistrationRulesFields(enabled) {
    if (enabled) {
        $('registration.startRegistration').enable();
        $('registration.startRegistration.calendar').show();
        $('registration.endRegistration').enable();
        $('registration.endRegistration.calendar').show();
        $('registration.maxParticipants').enable();
    } else {
        $('registration.startRegistration').disable();        
        $('registration.startRegistration.calendar').hide();
        $('registration.endRegistration').disable();
        $('registration.endRegistration.calendar').hide();
        $('registration.maxParticipants').disable();
    }
}

if ( '<c:if test="${empty event.registration.manualActivation}">null</c:if>' == 'null') {
$('registration.manualActivation1').checked = true;
} else {
enableRegistrationRulesFields(false);
}

</script>
