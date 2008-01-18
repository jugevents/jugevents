<%@ include file="../../common.jspf" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <%@ include file="../../head.jspf" %>
    </head>
    <body>
        <div id="nonFooter">            
            <jsp:include page="../../header.jsp"/>
            <div id="content"> 
                <div id="content_main">
                    <c:choose>
                        <c:when test="${requestScope.lang eq 'it'}">
                            Grazie per esserti registrato all'evento &quot;${participant.event.title}&quot;.<br/>
                            <br/>
                            Per completare la registrazione, 
                            <h3 style="text-align: center;">devi confermarla!</h3>
                            Ti &egrave; stata spedita una mail all'indirizzo <b>${participant.email}</b> con
                            le istruzioni per confermare la registrazione.
                        </c:when>
                        <c:when test="${requestScope.lang eq 'fr'}">
                            Merci de vous être enregistré à l'évènement &quot;${participant.event.title}&quot;.<br/>
                            <br/>
                            Pour compléter votre participation, 
                            <h3 style="text-align: center;">vous avez besoin de la confirmer.</h3>
                            Un e-mail vous a été envoyé à l'adresse <b>${participant.email}</b> avec
                            les instructions pour confirmer votre participation.
                        </c:when>
                        <c:otherwise>
                            Thank you for registering to the event &quot;${participant.event.title}&quot;.<br/>
                            <br/>
                            For completing your registration, 
                            <h3 style="text-align: center;">You need to confirm it.</h3>
                            An e-mail has been sent to your address <b>${participant.email}</b> with the
                            instructions for confirming your registration.
                        </c:otherwise>
                    </c:choose>
                </div>
                <jsp:include page="../../menu.jsp"/>
            </div>
        </div>
        <jsp:include page="../../footer.jsp"/>        
    </body>
</html>