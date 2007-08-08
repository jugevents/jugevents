<%@ include file="../common.jspf" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <%@ include file="../head.jspf" %>
    </head>
    <body>
        <div id="nonFooter">    
            <jsp:include page="../header.jsp"/>
             <div id="content_main">
            <h1>Jugger</h1>
            
            <h2>Edit/Insert Jugger</h2>
            
            <form:form commandName="jugger" method="POST" action="${cp}/jugger/edit.form">
                <form:hidden path="id"/>
                
                <table>
                    <tr>
                        <td>firstName</td>
                        <td><form:input path="firstName"/></td>
                        <td><form:errors path="firstName" cssClass="errorBox"/></td>
                    </tr>
                    <tr>
                        <td>lastName</td>
                        <td><form:input path="lastName"/></td>
                        <td><form:errors path="lastName" cssClass="errorBox"/></td>
                    </tr>
                    <tr>
                        <td>email</td>
                        <td><form:input path="email"/></td>
                        <td><form:errors path="email" cssClass="errorBox"/></td>
                    </tr>
                    
                    <tr>
                        <td>username</td>
                        <td><form:input path="username"/></td>
                        <td><form:errors path="username" cssClass="errorBox"/></td>
                    </tr>
                    
                    <tr>
                        <td>country</td>
                        <td>
                        <form:select path="country">
                        <form:option value="prova">aa</form:option>
                        </form:select>
                        </td>
                        <td><form:errors path="country" cssClass="errorBox"/></td>
                    </tr>
                      <tr>
                        <td>jugName</td>
                        <td><form:input path="jugName"/></td>
                        <td><form:errors path="jugName" cssClass="errorBox"/></td>
                    </tr>
                    
                    <tr>
                        <td>&nbsp;</td>
                        <td><input type="submit" value="Submit"/><br/><br/>
                        </td>
                    </tr>       
                    <tr>
                        <td>(*): required</td>
                        <td>(**): format dd/MM/yyyy</td>
                    </tr>       
                    
                </table>
                <!-- <form:errors path="*" cssClass="errorBox"/> -->
            </form:form>
        </div>
        </div>
        <jsp:include page="../footer.jsp"/>
        
    </body>
</html>