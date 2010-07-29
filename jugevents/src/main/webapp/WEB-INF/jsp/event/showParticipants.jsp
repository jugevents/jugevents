<%@ include file="/WEB-INF/jsp/common.jspf" %>
<div style="margin: 0; padding: 0;">
    <div style="float:left; width: 550px;">
        <h1><spring:message code='Participants'/></h1>
        <div class="secondaryMenu">
            <a href="${cp}/event/${event.id}"><spring:message code="BackToTheEvent"/></a>
        </div>
        <%@ include file="show_brief.jspf" %>
        <div id="participants">
        </div>
    </div>
    <div style="float:right; width: 200px; padding-left: 10px; font-size: 11px">
        <%@ include file="registration_button.jspf" %>
        <%@ include file="jug_box.jspf" %>
        <%@ include file="event_data.jspf" %>
        <%@ include file="speakers.jspf" %>
    </div>
</div>


