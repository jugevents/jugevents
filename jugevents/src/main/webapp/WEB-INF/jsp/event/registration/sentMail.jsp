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
                            Merci de vous �tre enregistr� � l'�v�nement &quot;${participant.event.title}&quot;.<br/>
                            <br/>
                            Pour compl�ter votre participation,
                            <h3 style="text-align: center;">vous avez besoin de la confirmer.</h3>
                            Un e-mail vous a �t� envoy� � l'adresse <b>${participant.email}</b> avec
                            les instructions pour confirmer votre participation.
                        </c:when>
                        <c:when test="${requestScope.requestContext.locale eq 'pt_BR'}">
                            Obrigado pelo seu pedido de cadastro no evento &quot;${participant.event.title}&quot;.<br/>
                            <br/>
                            Para completar o seu cadastro,
                            <h3 style="text-align: center;">Voc� precisa confirmar seu cadastro.</h3>
                            Um email foi enviado ao seu endere�o <b>${participant.email}</b> com as instru��es
                            sobre como confirmar o seu cadastro.
                        </c:when>
                        <c:when test="${requestScope.requestContext.locale eq 'pt_PT'}">
                            Obrigado pelo seu pedido de registo no evento &quot;${participant.event.title}&quot;.<br/>
                            <br/>
                            Para completar o seu registo,
                            <h3 style="text-align: center;">Precisa confirmar seu registo.</h3>
                            Foi enviado um e-mail para o seu endere�o <b>${participant.email}</b> com as instru��es
                            sobre como confirmar o seu registo.
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