<%@ include file="common.jspf" %>
<h2><spring:message code="attention"/></h2>

<div class="error">
    <h3><spring:message code="access_denied"/></h3>
    <p><spring:message code="access_denied_message"/><br>
    <a href="${cp}/"><spring:message code="back_to_home"/></a></p>
</div>
