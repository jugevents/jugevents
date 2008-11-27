<%@ include file="common.jspf" %>
<h2><spring:message code="attention" text="?attention?"/></h2>

<div class="error">
    <h3>HTTP: 500</h3>

    <p><spring:message code="message.500" text="?message.500?"/><br/>
        <a href="${cp}/"><spring:message code="back_to_home" text="?back_to_home?"/></a>
    </p>
</div>
