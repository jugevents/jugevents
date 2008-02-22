<%@ include file="../common.jspf" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <%@ include file="../head.jspf" %>
    <script src="${cp}/dwr/interface/participantBo.js" type="text/javascript"></script>
</head>
<body>
<div id="nonFooter">
    <jsp:include page="../header.jsp"/>
    <div id="content">
        <div id="content_main">
            
            <h1><spring:message code='Participants'/></h1>
            <a href="${cp}/event/show.html?id=${event.id}"><spring:message code="BackToTheEvent"/></a>
            <%@ include file="show_brief.jspf" %>
            
            <h2><spring:message code='ParticipantList'/></h2>
            <a href="javascript:void(0)" onclick="participantBo.sendCertificateToAllParticipants(${event.id}, 'http://www.jugevents.org'); return false;"><spring:message code="SendAllCertificates"/></a>
            <a href="javascript:void(0)" onclick="$('addNewParticipantDiv').show(); $('participant.firstName').focus(); return false;"><spring:message code="AddParticipant"/></a>
            <div id="sentCertificatesMessage"></div>
            <div id="addNewParticipantDiv" <c:if test="${showAddNewPartecipantDiv != 'true'}">style="display: none;"</c:if>><div>
                <fieldset>
                    <legend><spring:message code="AddParticipant"/></legend>
                    <form:form commandName="registration" method="POST" action="${cp}/event/addParticipant.form">
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
                            <dd><form:textarea path="participant.note" rows="6"/></dd>
                            <dt>&nbsp;</dt>
                            <dd><input type="submit" value="<spring:message code="AddParticipant"/>"/><br/><br/></dd>
                        </dl>
                    </form:form>
                </fieldset>
        </div></div>
        <div class="displaytag">
            <display:table name="participants" id="participantList" sort="list" pagesize="20" defaultsort="6" defaultorder="ascending" requestURI="participants.html" export="true">
                <display:column title="#">${participantList_rowNum}</display:column>
                <display:column sortProperty="firstName" titleKey="first_name" sortable="true" headerClass="sortable">
                    <span id="firstName_v_${participantList.id}" onclick="$('firstName_e_${participantList.id}').toggle(); $('firstName_v_${participantList.id}').toggle();$('firstName_f_${participantList.id}').focus()">${participantList.firstName}&nbsp;<img src="${cp}/images/editableMarker.gif" alt="<spring:message code='InlineEdit'/>"/></span>
                    <span id="firstName_e_${participantList.id}" style="display: none;"><input type="text" onchange="participantBo.updateParticipantFieldValue(${participantList.id}, 'firstName', this.value)" id="firstName_f_${participantList.id}" size="15" onblur="$('firstName_e_${participantList.id}').toggle(); $('firstName_v_${participantList.id}').toggle()" value="${participantList.firstName}"/></span>
                </display:column>
                <display:column sortProperty="lastName" titleKey="last_name" sortable="true" headerClass="sortable">
                    <span id="lastName_v_${participantList.id}" onclick="$('lastName_e_${participantList.id}').toggle(); $('lastName_v_${participantList.id}').toggle();$('lastName_f_${participantList.id}').focus()">${participantList.lastName}&nbsp;<img src="${cp}/images/editableMarker.gif" alt="<spring:message code='InlineEdit'/>"/></span>
                    <span id="lastName_e_${participantList.id}" style="display: none;"><input type="text" onchange="participantBo.updateParticipantFieldValue(${participantList.id}, 'lastName', this.value)" id="lastName_f_${participantList.id}" size="15" onblur="$('lastName_e_${participantList.id}').toggle(); $('lastName_v_${participantList.id}').toggle()" value="${participantList.lastName}"/></span>
                </display:column>
                <display:column sortProperty="email" titleKey="Email" sortable="true" headerClass="sortable">
                    <span id="email_v_${participantList.id}" onclick="$('email_e_${participantList.id}').toggle(); $('email_v_${participantList.id}').toggle();$('email_f_${participantList.id}').focus()">${participantList.email}&nbsp;<img src="${cp}/images/editableMarker.gif" alt="<spring:message code='InlineEdit'/>"/></span>
                    <span id="email_e_${participantList.id}" style="display: none;"><input type="text" onchange="participantBo.updateParticipantFieldValue(${participantList.id}, 'email', this.value)" id="email_f_${participantList.id}" size="15" onblur="$('email_e_${participantList.id}').toggle(); $('email_v_${participantList.id}').toggle()" value="${participantList.email}"/></span>
                </display:column>
                <display:column sortProperty="note" titleKey="Note" sortable="true" headerClass="sortable">
                    <span id="note_v_${participantList.id}" onclick="$('note_e_${participantList.id}').toggle(); $('note_v_${participantList.id}').toggle();$('note_f_${participantList.id}').focus()">${participantList.note}&nbsp;<img src="${cp}/images/editableMarker.gif" alt="<spring:message code='InlineEdit'/>"/></span>
                    <span id="note_e_${participantList.id}" style="display: none;"><textarea onchange="participantBo.updateParticipantFieldValue(${participantList.id}, 'note', this.value)" id="note_f_${participantList.id}" rows="3" cols="10" onblur="$('note_e_${participantList.id}').toggle(); $('note_v_${participantList.id}').toggle()">${participantList.note}</textarea></span>
                </display:column>
                <display:column property="creationDate" titleKey="JoinedAt" sortable="true" headerClass="sortable"/>
                <display:column property="confirmationDate" titleKey="ConfirmedAt" sortable="true" headerClass="sortable"/>
                <display:column media="html" titleKey="Attended" sortable="true" headerClass="sortable" style="text-align: center;">
                    <input onclick="participantBo.setAttended(${participantList.id}, this.checked)" type="checkbox" <c:if test="${participantList.attended}">checked="${participantList.attended}"</c:if> />
                       </display:column>
                       <display:column media="html" titleKey="Winner" sortable="true" headerClass="sortable" style="text-align: center;">
                           <input onclick="participantBo.setWinner(${participantList.id}, this.checked)" type="checkbox" <c:if test="${participantList.winner}">checked="${participantList.winner}"</c:if> />
                       </display:column>
                       <display:column media="csv xml excel pdf" property="attended" titleKey="Attended" sortable="true" headerClass="sortable" style="text-align: center;" />
                       <display:column media="html" titleKey="Certificate" sortable="false" style="text-align: center;">
                           <a href="#" onclick="participantBo.sendCertificateToParticipant(${participantList.id}, 'http://www.jugevents.org'); return false"><spring:message code="SendCertificate"/></a>
                    <div id="certificateMsg${participantList.id}"><fmt:formatDate value="${participantList.lastCertificateSentDate}" type="date" dateStyle="short" /></div>
                </display:column>
                <display:column media="csv xml excel pdf" property="lastCertificateSentDate" titleKey="Certificate" sortable="false" style="text-align: center;" />
            </display:table>
        </div>
        <h2><spring:message code='NotConfirmedParticipantList'/></h2>
        <div class="displaytag">
            <display:table name="participantsNotConfirmed" id="participantNotConfirmedList" sort="list" pagesize="20" defaultsort="6" defaultorder="ascending" requestURI="participants.html" export="true">
                <display:column title="#">${participantNotConfirmedList_rowNum}</display:column>
                <display:column sortProperty="firstName" titleKey="first_name" sortable="true" headerClass="sortable">
                    <span id="firstName_v_${participantNotConfirmedList.id}" onclick="$('firstName_e_${participantNotConfirmedList.id}').toggle(); $('firstName_v_${participantNotConfirmedList.id}').toggle();$('firstName_f_${participantNotConfirmedList.id}').focus()">${participantNotConfirmedList.firstName}&nbsp;<img src="${cp}/images/editableMarker.gif" alt="<spring:message code='InlineEdit'/>"/></span>
                    <span id="firstName_e_${participantNotConfirmedList.id}" style="display: none;"><input type="text" onchange="participantBo.updateParticipantFieldValue(${participantNotConfirmedList.id}, 'firstName', this.value)" id="firstName_f_${participantNotConfirmedList.id}" size="15" onblur="$('firstName_e_${participantNotConfirmedList.id}').toggle(); $('firstName_v_${participantNotConfirmedList.id}').toggle()" value="${participantNotConfirmedList.firstName}"/></span>
                </display:column>
                <display:column sortProperty="lastName" titleKey="last_name" sortable="true" headerClass="sortable">
                    <span id="lastName_v_${participantNotConfirmedList.id}" onclick="$('lastName_e_${participantNotConfirmedList.id}').toggle(); $('lastName_v_${participantNotConfirmedList.id}').toggle();$('lastName_f_${participantNotConfirmedList.id}').focus()">${participantNotConfirmedList.lastName}&nbsp;<img src="${cp}/images/editableMarker.gif" alt="<spring:message code='InlineEdit'/>"/></span>
                    <span id="lastName_e_${participantNotConfirmedList.id}" style="display: none;"><input type="text" onchange="participantBo.updateParticipantFieldValue(${participantNotConfirmedList.id}, 'lastName', this.value)" id="lastName_f_${participantNotConfirmedList.id}" size="15" onblur="$('lastName_e_${participantNotConfirmedList.id}').toggle(); $('lastName_v_${participantNotConfirmedList.id}').toggle()" value="${participantNotConfirmedList.lastName}"/></span>
                </display:column>
                <display:column sortProperty="email" titleKey="Email" sortable="true" headerClass="sortable">
                    <span id="email_v_${participantNotConfirmedList.id}" onclick="$('email_e_${participantNotConfirmedList.id}').toggle(); $('email_v_${participantNotConfirmedList.id}').toggle();$('email_f_${participantNotConfirmedList.id}').focus()">${participantNotConfirmedList.email}&nbsp;<img src="${cp}/images/editableMarker.gif" alt="<spring:message code='InlineEdit'/>"/></span>
                    <span id="email_e_${participantNotConfirmedList.id}" style="display: none;"><input type="text" onchange="participantBo.updateParticipantFieldValue(${participantNotConfirmedList.id}, 'email', this.value)" id="email_f_${participantNotConfirmedList.id}" size="15" onblur="$('email_e_${participantNotConfirmedList.id}').toggle(); $('email_v_${participantNotConfirmedList.id}').toggle()" value="${participantNotConfirmedList.email}"/></span>
                </display:column>
                <display:column sortProperty="note" titleKey="Note" sortable="true" headerClass="sortable">
                    <span id="note_v_${participantNotConfirmedList.id}" onclick="$('note_e_${participantNotConfirmedList.id}').toggle(); $('note_v_${participantNotConfirmedList.id}').toggle();$('note_f_${participantNotConfirmedList.id}').focus()">${participantNotConfirmedList.note}&nbsp;<img src="${cp}/images/editableMarker.gif" alt="<spring:message code='InlineEdit'/>"/></span>
                    <span id="note_e_${participantNotConfirmedList.id}" style="display: none;"><textarea onchange="participantBo.updateParticipantFieldValue(${participantNotConfirmedList.id}, 'note', this.value)" id="note_f_${participantNotConfirmedList.id}" rows="3" cols="10" onblur="$('note_e_${participantNotConfirmedList.id}').toggle(); $('note_v_${participantNotConfirmedList.id}').toggle()">${participantNotConfirmedList.note}</textarea></span>
                </display:column>
                <display:column property="creationDate" titleKey="JoinedAt" sortable="true" headerClass="sortable"/>
                <display:column media="html" titleKey="Attended" sortable="true" headerClass="sortable" style="text-align: center;">
                    <input onclick="participantBo.confirmParticipantOnAttendance(${participantNotConfirmedList.id}, this.checked)" type="checkbox" <c:if test="${participantNotConfirmedList.attended}">checked="${participantNotConfirmedList.attended}"</c:if> />
                       </display:column>
                       <display:column media="html" titleKey="Winner" sortable="true" headerClass="sortable" style="text-align: center;">
                           <input onclick="participantBo.setWinner(${participantList.id}, this.checked)" type="checkbox" <c:if test="${participantList.winner}">checked="${participantList.winner}"</c:if> />
                       </display:column>
                       <display:column media="csv xml excel pdf" property="attended" titleKey="Attended" sortable="true" headerClass="sortable" style="text-align: center;" />
                   </display:table>
                   </div>
            <br/>
        </div>
        <jsp:include page="../menu.jsp"/>
    </div>
</div>
<jsp:include page="../footer.jsp"/>
<script language="javascript">
    
    function pausecomp(millis)
    {
        var date = new Date();
        var curDate = null;
        
        do { curDate = new Date(); }
        while(curDate-date < millis);
    }
    
    </script>
</body>
</html>