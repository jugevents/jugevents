<%@ include file="../common.jspf" %>
<p><spring:message code='passwordChange'/></p>

<form:form  commandName="enablejugger"  method="post" action="${cp}/jugger/changePassword.form">
    <table>                     
        <tr>
            <td><spring:message code='newPassword'/></td>
            <td><form:password   path="password"/></td>                    
        </tr>
        <tr>
            <td><spring:message code='confirmPassword'/></td>
            <td><form:password  path="confirmPassword"/></td>                    
        </tr>
        <tr>
            <td>&nbsp;</td>
            <td><input type="submit" value="Submit"/><br/><br/>
            </td>
        </tr>       
    </table>
    <form:errors path="*" cssClass="errorBox"/>
    <input type="hidden" name="code" value="${jugger.changePasswordCode}"/>
    <input type="hidden" name="username" value="${jugger.user.username}"/>
</form:form>
