<%@ include file="../../common.jspf" %>
<h4><spring:message code='passwordInsert'/></h4>

<form:form  commandName="enablejugger"  method="post" action="${cp}/jugger/enable.form">
    <table>                     
        <tr>
            <td>Password</td>
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
    <input type="hidden" name="code" value="${jugger.confirmationCode}"/>
    <input type="hidden" name="username" value="${jugger.user.username}"/>
</form:form>
