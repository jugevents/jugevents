<%@ include file="../common.jspf" %>
<p><spring:message code='passwordRecoveryMessage'/></p>
<form:form commandName="passwordRecovery" method="POST" action="${cp}/passwordRecovery.form">
    <form:errors path="*" cssClass="errorBox"/>
    
    <fieldset>
        <legend><spring:message code='passwordRecovery'/></legend>
        <dl>
            <dt>e-mail</dt>
            <dd><form:input path="email"/></dd>
        </dl>
    </fieldset>
    
    <dl>
        <dt>&nbsp;</dt><dd><input type="submit" value="<spring:message code='Recover'/>"/><br/><br/></dd>
        <dd>&nbsp;</dd>
    </dl>
</form:form>
