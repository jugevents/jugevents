<%@ include file="common.jspf" %>
<h2><spring:message code="attention" text="?attention?"/></h2>

<div class="error">
    <h3>HTTP: 404</h3>
    <p><spring:message code="message.404" text="?message.404?"/><br>
    <a href="${cp}/"><spring:message code="back_to_home" text="?back_to_home?"/></a></p>
</div>
