<%@ include file="common.jspf" %>
<h2><spring:message code="attention"/></h2>

<div class="error">
    <h3><spring:message code="conversationException"/></h3>
    <p><spring:message code="conversationException_message"/><br>
    <a href="${cp}/"><spring:message code="back_to_home"/></a></p>
</div>
