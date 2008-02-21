<%@ include file="common.jspf" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <%@ include file="head.jspf" %>
    </head>
    <body>
        <div id="nonFooter">            
            <jsp:include page="header.jsp"/>
            <div id="content"> 
                <div id="content_main">
                    <spring:message code="${messageCode}" arguments="${messageArguments}" text='<p>Unknown message...maybe a bug...or are you hackering this application?</p><p>If you think it\'s a bug, please <a href="http://code.google.com/p/jugevents/issues/list">enter a new issue</a>. Before writing a new issue, search for an already existent issue.</p><p>Thank you!</p>'/>
                </div>
                <jsp:include page="menu.jsp"/>
            </div>
        </div>
        <jsp:include page="footer.jsp"/>        
    </body>
</html>