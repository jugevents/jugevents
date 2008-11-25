<%@ include file="../common.jspf" %>
<jwr:script src="${cp}/dwr/interface/AjaxMethodsJS.js" />
<h1><spring:message code='Participants'/></h1>
<div class="secondaryMenu">
    <a href="${cp}/event/show.html?id=${event.id}"><spring:message code="BackToTheEvent"/></a>
</div>
<%@ include file="show_brief.jspf" %>

<h2><spring:message code='ParticipantList'/></h2>
<div class="secondaryMenu">
    <a href="javascript:void(0)" onclick="AjaxMethodsJS.sendCertificateToAllParticipants(${event.id}, '${conf.jugeventsBaseUrl}'); return false;"><spring:message code="SendAllCertificates"/></a>
    <a href="javascript:void(0)" onclick="$('addNewParticipantDiv').show(); $('participant.firstName').focus(); return false;"><spring:message code="AddParticipant"/></a>
</div>
<div id="sentCertificatesMessage"></div>
<div id="addNewParticipantDiv" <c:if test="${showAddNewPartecipantDiv != 'true'}">style="display: none;"</c:if>><div>
    <form:form commandName="registration" method="post" action="${cp}/event/addParticipant.form">
        <fieldset>
            <legend><spring:message code="AddParticipant"/></legend>
            <form:errors path="*" cssClass="errorBox"/>
            <form:hidden path="event.id"/>
            <dl>
                <dt><form:label path="participant.firstName"><spring:message code="first_name"/>:</form:label></dt>
                <dd><form:input path="participant.firstName"/></dd>
                <dt><form:label path="participant.lastName"><spring:message code="last_name"/>:</form:label></dt>
                <dd><form:input path="participant.lastName"/></dd>
                <dt><form:label path="participant.email"><spring:message code="Email"/>:</form:label></dt>
                <dd><form:input path="participant.email"/></dd>
                <dt><form:label path="participant.note"><spring:message code="Note"/>:</form:label></dt>
                <dd><form:textarea path="participant.note" rows="6" cols="25"/></dd>
                <dt>&nbsp;</dt>
                <dd><input type="submit" value="<spring:message code="AddParticipant"/>"/><br/><br/></dd>
            </dl>
        </fieldset>
    </form:form>
</div></div>
<spring:message code='confirmDeleteParticipant' var="confirmDeleteParticipantMessage" text="Are you sure?"/>
<div class="displaytag">
    <display:table name="participants" id="participantList" sort="list" pagesize="20" defaultsort="6" defaultorder="ascending" requestURI="participants.html" export="true">
        <display:column title="#">${participantList_rowNum}</display:column>
        <display:column media="html" sortProperty="firstName" titleKey="first_name" sortable="true" headerClass="sortable">
            <span id="firstName_v_${participantList.id}" onclick="$('firstName_e_${participantList.id}').toggle(); $('firstName_v_${participantList.id}').toggle();$('firstName_f_${participantList.id}').focus()">${participantList.firstName}&nbsp;<img src="${cp}/images/editableMarker.gif" alt="<spring:message code='InlineEdit'/>"/></span>
            <span id="firstName_e_${participantList.id}" style="display: none;"><input type="text" onchange="AjaxMethodsJS.updateParticipantFieldValue(${participantList.id}, 'firstName', this.value)" id="firstName_f_${participantList.id}" size="15" onblur="$('firstName_e_${participantList.id}').toggle(); $('firstName_v_${participantList.id}').toggle()" value="${participantList.firstName}"/></span>
        </display:column>
        <display:column media="csv xml excel pdf" property="firstName" titleKey="first_name" sortable="true" headerClass="sortable"/>
        <display:column media="html" sortProperty="lastName" titleKey="last_name" sortable="true" headerClass="sortable">
            <span id="lastName_v_${participantList.id}" onclick="$('lastName_e_${participantList.id}').toggle(); $('lastName_v_${participantList.id}').toggle();$('lastName_f_${participantList.id}').focus()">${participantList.lastName}&nbsp;<img src="${cp}/images/editableMarker.gif" alt="<spring:message code='InlineEdit'/>"/></span>
            <span id="lastName_e_${participantList.id}" style="display: none;"><input type="text" onchange="AjaxMethodsJS.updateParticipantFieldValue(${participantList.id}, 'lastName', this.value)" id="lastName_f_${participantList.id}" size="15" onblur="$('lastName_e_${participantList.id}').toggle(); $('lastName_v_${participantList.id}').toggle()" value="${participantList.lastName}"/></span>
        </display:column>
        <display:column media="csv xml excel pdf" property="lastName" titleKey="last_name" sortable="true" headerClass="sortable"/>
        <display:column media="html" sortProperty="email" titleKey="Email" sortable="true" headerClass="sortable">
            <span id="email_v_${participantList.id}" onclick="$('email_e_${participantList.id}').toggle(); $('email_v_${participantList.id}').toggle();$('email_f_${participantList.id}').focus()">${participantList.email}&nbsp;<img src="${cp}/images/editableMarker.gif" alt="<spring:message code='InlineEdit'/>"/></span>
            <span id="email_e_${participantList.id}" style="display: none;"><input type="text" onchange="AjaxMethodsJS.updateParticipantFieldValue(${participantList.id}, 'email', this.value)" id="email_f_${participantList.id}" size="15" onblur="$('email_e_${participantList.id}').toggle(); $('email_v_${participantList.id}').toggle()" value="${participantList.email}"/></span>
        </display:column>
        <display:column media="csv xml excel pdf" property="email" titleKey="Email" sortable="true" headerClass="sortable"/>
        <display:column media="html" sortProperty="note" titleKey="Note" sortable="true" headerClass="sortable">
            <span id="note_v_${participantList.id}" onclick="$('note_e_${participantList.id}').toggle(); $('note_v_${participantList.id}').toggle();$('note_f_${participantList.id}').focus()">${participantList.note}&nbsp;<img src="${cp}/images/editableMarker.gif" alt="<spring:message code='InlineEdit'/>"/></span>
            <span id="note_e_${participantList.id}" style="display: none;"><textarea onchange="AjaxMethodsJS.updateParticipantFieldValue(${participantList.id}, 'note', this.value)" id="note_f_${participantList.id}" rows="3" cols="10" onblur="$('note_e_${participantList.id}').toggle(); $('note_v_${participantList.id}').toggle()">${participantList.note}</textarea></span>
        </display:column>
        <display:column media="csv xml excel pdf" property="note" titleKey="Note" sortable="true" headerClass="sortable"/>
        <display:column property="creationDate" titleKey="JoinedAt" sortable="true" headerClass="sortable"/>
        <display:column property="confirmationDate" titleKey="ConfirmedAt" sortable="true" headerClass="sortable"/>
        <display:column media="html" titleKey="Attended" sortable="true" headerClass="sortable" style="text-align: center;">
            <input onclick="AjaxMethodsJS.setAttended(${participantList.id}, this.checked)" type="checkbox" <c:if test="${participantList.attended}">checked="checked"</c:if> />
               </display:column>
               <display:column media="html" titleKey="Winner" sortable="true" headerClass="sortable" style="text-align: center;">
                   <input onclick="AjaxMethodsJS.setWinner(${participantList.id}, this.checked)" type="checkbox" <c:if test="${participantList.winner}">checked="checked"</c:if> />
               </display:column>
               <display:column media="csv xml excel pdf" property="attended" titleKey="Attended" sortable="true" headerClass="sortable" style="text-align: center;" />
               <display:column media="html" title="" sortable="false" style="text-align: center;">
                   <a href="#" onclick="AjaxMethodsJS.sendCertificateToParticipant(${participantList.id}, '${conf.jugeventsBaseUrl}'); return false"><spring:message code="SendCertificate"/></a>
            <div id="certificateMsg${participantList.id}"><fmt:formatDate value="${participantList.lastCertificateSentDate}" type="date" dateStyle="short" /></div>
            <a href="${cp}/bin/participantCertificate.bin?id=${participantList.id}"><spring:message code="DownloadCertificate" text="?DownloadCertificate?"/></a><br/>
            <a href="${cp}/event/deleteParticipant.html?id=${participantList.id}" onclick="return confirm('${confirmDeleteParticipantMessage}')"><spring:message code="delete" text="?delete?"/></a>
        </display:column>
        <display:column media="csv xml excel pdf" property="lastCertificateSentDate" titleKey="Certificate" sortable="false" style="text-align: center;" />
    </display:table>
</div>
<h2><spring:message code='NotConfirmedParticipantList'/></h2>
<div class="displaytag">
    <display:table name="participantsNotConfirmed" id="participantNotConfirmedList" sort="list" pagesize="20" defaultsort="6" defaultorder="ascending" requestURI="participants.html" export="true">
        <display:column title="#">${participantNotConfirmedList_rowNum}</display:column>
        <display:column media="html" sortProperty="firstName" titleKey="first_name" sortable="true" headerClass="sortable">
            <span id="firstName_v_${participantNotConfirmedList.id}" onclick="$('firstName_e_${participantNotConfirmedList.id}').toggle(); $('firstName_v_${participantNotConfirmedList.id}').toggle();$('firstName_f_${participantNotConfirmedList.id}').focus()">${participantNotConfirmedList.firstName}&nbsp;<img src="${cp}/images/editableMarker.gif" alt="<spring:message code='InlineEdit'/>"/></span>
            <span id="firstName_e_${participantNotConfirmedList.id}" style="display: none;"><input type="text" onchange="AjaxMethodsJS.updateParticipantFieldValue(${participantNotConfirmedList.id}, 'firstName', this.value)" id="firstName_f_${participantNotConfirmedList.id}" size="15" onblur="$('firstName_e_${participantNotConfirmedList.id}').toggle(); $('firstName_v_${participantNotConfirmedList.id}').toggle()" value="${participantNotConfirmedList.firstName}"/></span>
        </display:column>
        <display:column media="csv xml excel pdf" property="firstName" titleKey="first_name" sortable="true" headerClass="sortable"/>
        <display:column media="html" sortProperty="lastName" titleKey="last_name" sortable="true" headerClass="sortable">
            <span id="lastName_v_${participantNotConfirmedList.id}" onclick="$('lastName_e_${participantNotConfirmedList.id}').toggle(); $('lastName_v_${participantNotConfirmedList.id}').toggle();$('lastName_f_${participantNotConfirmedList.id}').focus()">${participantNotConfirmedList.lastName}&nbsp;<img src="${cp}/images/editableMarker.gif" alt="<spring:message code='InlineEdit'/>"/></span>
            <span id="lastName_e_${participantNotConfirmedList.id}" style="display: none;"><input type="text" onchange="AjaxMethodsJS.updateParticipantFieldValue(${participantNotConfirmedList.id}, 'lastName', this.value)" id="lastName_f_${participantNotConfirmedList.id}" size="15" onblur="$('lastName_e_${participantNotConfirmedList.id}').toggle(); $('lastName_v_${participantNotConfirmedList.id}').toggle()" value="${participantNotConfirmedList.lastName}"/></span>
        </display:column>
        <display:column media="csv xml excel pdf" property="lastName" titleKey="last_name" sortable="true" headerClass="sortable"/>
        <display:column media="html" sortProperty="email" titleKey="Email" sortable="true" headerClass="sortable">
            <span id="email_v_${participantNotConfirmedList.id}" onclick="$('email_e_${participantNotConfirmedList.id}').toggle(); $('email_v_${participantNotConfirmedList.id}').toggle();$('email_f_${participantNotConfirmedList.id}').focus()">${participantNotConfirmedList.email}&nbsp;<img src="${cp}/images/editableMarker.gif" alt="<spring:message code='InlineEdit'/>"/></span>
            <span id="email_e_${participantNotConfirmedList.id}" style="display: none;"><input type="text" onchange="AjaxMethodsJS.updateParticipantFieldValue(${participantNotConfirmedList.id}, 'email', this.value)" id="email_f_${participantNotConfirmedList.id}" size="15" onblur="$('email_e_${participantNotConfirmedList.id}').toggle(); $('email_v_${participantNotConfirmedList.id}').toggle()" value="${participantNotConfirmedList.email}"/></span>
        </display:column>
        <display:column media="csv xml excel pdf" property="email" titleKey="Email" sortable="true" headerClass="sortable"/>
        <display:column media="html" sortProperty="note" titleKey="Note" sortable="true" headerClass="sortable">
            <span id="note_v_${participantNotConfirmedList.id}" onclick="$('note_e_${participantNotConfirmedList.id}').toggle(); $('note_v_${participantNotConfirmedList.id}').toggle();$('note_f_${participantNotConfirmedList.id}').focus()">${participantNotConfirmedList.note}&nbsp;<img src="${cp}/images/editableMarker.gif" alt="<spring:message code='InlineEdit'/>"/></span>
            <span id="note_e_${participantNotConfirmedList.id}" style="display: none;"><textarea onchange="AjaxMethodsJS.updateParticipantFieldValue(${participantNotConfirmedList.id}, 'note', this.value)" id="note_f_${participantNotConfirmedList.id}" rows="3" cols="10" onblur="$('note_e_${participantNotConfirmedList.id}').toggle(); $('note_v_${participantNotConfirmedList.id}').toggle()">${participantNotConfirmedList.note}</textarea></span>
        </display:column>
        <display:column media="csv xml excel pdf" property="note" titleKey="Note" sortable="true" headerClass="sortable"/>
        <display:column property="creationDate" titleKey="JoinedAt" sortable="true" headerClass="sortable"/>
        <display:column media="html" titleKey="Attended" sortable="true" headerClass="sortable" style="text-align: center;">
            <input onclick="AjaxMethodsJS.confirmParticipantOnAttendance(${participantNotConfirmedList.id}, this.checked)" type="checkbox" <c:if test="${participantNotConfirmedList.attended}">checked="checked"</c:if> />
               </display:column>
               <display:column media="html" titleKey="Winner" sortable="true" headerClass="sortable" style="text-align: center;">
                   <input onclick="AjaxMethodsJS.setWinner(${participantList.id}, this.checked)" type="checkbox" <c:if test="${participantList.winner}">checked="checked"</c:if> />
               </display:column>
               <display:column media="csv xml excel pdf" property="attended" titleKey="Attended" sortable="true" headerClass="sortable" style="text-align: center;" />
               <display:column media="html" title="" sortable="false" style="text-align: center;">
            <a href="${cp}/event/deleteParticipant.html?id=${participantNotConfirmedList.id}" onclick="return confirm('${confirmDeleteParticipantMessage}')"><spring:message code="delete" text="?delete?"/></a>
        </display:column>           
    </display:table>
</div>
