<%@ include file="../common.jspf" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <%@ include file="../head.jspf" %>
    </head>
    <body>
        <div id="nonFooter">            
            <jsp:include page="../header.jsp"/>
            <div id="content"> 
                <div id="content_main">
                    <h1><spring:message code="Services"/></h1>                    
                    <c:choose>
                        <c:when test="${requestScope.lang eq 'it'}">
                            <p>JUG Events non &egrave; solo per gestire gli eventi
                            del tuo JUG. Pu&ograve; fornire anche alcuni servizi
                            utili al tuo JUG e alla comunit&agrave; internazionale
                            dei JUG.</p>
                            <p>Se hai in mente qualche servizio, e pensi che JUG Events
                            potrebbe fornirlo, <a href="mailto:info@jugevents.org">suggeriscilo</a>
                            e cercheremo di realizzarlo.</p>
                        </c:when>
                        <c:when test="${requestScope.lang eq 'fr'}">
                            <p>JUG Events n'est pas uniquement fait pour g�rer les �v�nements. Il peut
                            vous fournir d'autres services tr�s utiles pour votre JUG et pour la
                            communaut� internationale des JUGs.</p>
                            <p>Si vous pensez � un service que JUG Events pourrait
                            vous fournir, <a href="mailto:info@jugevents.org">sugg�rez le nous</a>,
                            et nous essaierons de le r�aliser.</p>
                        </c:when>
                        <c:otherwise>
                            <p>JUG Events isn't just for event management. It can
                            provide some useful services to your JUG and to the
                            international JUG community.</p>
                            <p>If You are thinking to a service JUG Events could
                            provide, <a href="mailto:info@jugevents.org">suggest it</a>,
                            and we'll try to realize it.</p>
                        </c:otherwise>
                    </c:choose>
                    <!-- JUG KML Map -->
                    <c:choose>
                        <c:when test="${requestScope.lang eq 'it'}">
                            <h2>KML per una mappa di JUG</h2>
                            <p>Ogni JUG pu&ograve; gestire le proprie informazioni
                            in JUG Events e tenere quindi aggiornato questo file KML.</p>
                            <p style="text-align: center;"><a href="${cp}/service/kml.html">Scarica il file KML</a></p>
                            <p>Questo file KML &egrave; prodotto usando i dati presenti nel database di JUG Events. I dati
                            originali sono stati estratti dalla
                            <a href="http://wiki.java.net/bin/view/JUGs/JUG-MAP">Java User Groups International Map</a>.</p>
                        </c:when>
                        <c:when test="${requestScope.lang eq 'fr'}">
                            <h2>KML pour le JUG map</h2>
                            <p>Chaque JUG g�re ses informations das JUG Events et
                            maintien � jour son fichier KML.</p>
                            <p style="text-align: center;"><a href="${cp}/service/kml.html">T�l�chargez le fichier KML</a></p>
                            <p>Le fichier KML est produit � partir des donn�es de JUG Events. Les donn�es d'origine ont �t�
                            import� � partir de
                            <a href="http://wiki.java.net/bin/view/JUGs/JUG-MAP">Java User Groups International Map</a>.</p>
                        </c:when>
                        <c:otherwise>
                            <h2>KML for a JUG map</h2>
                            <p>Every JUG can manage its information in JUG Events and
                            mantain updated this KML file.</p>
                            <p style="text-align: center;"><a href="${cp}/service/kml.html">Download the KML file</a></p>
                            <p>This KML file is produced from the data of the JUG Events database. The original data has been
                            imported from the
                            <a href="http://wiki.java.net/bin/view/JUGs/JUG-MAP">Java User Groups International Map</a>.</p>
                        </c:otherwise>
                    </c:choose>                    
                </div>
                <jsp:include page="../menu.jsp"/>
            </div>
        </div>
        <jsp:include page="../footer.jsp"/>        
    </body>
</html>