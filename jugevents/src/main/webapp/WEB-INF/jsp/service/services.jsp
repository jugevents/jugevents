<%@ page pageEncoding="UTF-8" %>
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
                            <p>JUG Events n'est pas uniquement fait pour gérer les évènements. Il peut
                                vous fournir d'autres services très utiles pour votre JUG et pour la
                            communauté internationale des JUGs.</p>
                            <p>Si vous pensez à un service que JUG Events pourrait
                                vous fournir, <a href="mailto:info@jugevents.org">suggérez le nous</a>,
                            et nous essaierons de le réaliser.</p>
                        </c:when>
                        <c:when test="${requestScope.requestContext.locale eq 'pt_BR'}">
                            <p>JUG Events não serve apenas para o gerenciamento de eventos.
                                O projeto também provê serviços úteis ao seu JUG e à comunidade
                            internacional de JUGs.</p>
                            <p>Se você imagina algum serviço que o JUG Events deveria
                                fornecer, <a href="mailto:info@jugevents.org">envie sua sugestão</a>,
                            e nós analisaremos e tentaremos implementá-la.</p>
                        </c:when>
                        <c:when test="${requestScope.requestContext.locale eq 'pt_PT'}">
                            <p>JUG Events não serve apenas para gestão de eventos.
                                O projecto também fornece serviços úteis ao seu JUG e à comunidade
                            internacional de JUGs.</p>
                            <p>Se você imagina algum serviço que o JUG Events deveria
                                fornecer, <a href="mailto:info@jugevents.org">envie a sua sugestão</a>,
                            e nós analisaremos e tentaremos implementá-la.</p>
                        </c:when>
                        <c:when test="${requestScope.lang eq 'pl'}">
                            <p>JUG Events to nie tylko zarządzanie wydarzeniami.
                                Udostępnia także usługi dla Twojego JUGa
                                oraz dla międzynarodowej społeczności JUGów.
                            <p>Jeśli masz pomysł na nową usługę
                                to ją <a href="mailto:info@jugevents.org">opisz</a>,
                            a my spróbujemy ją zrealizować.</p>
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
                            <p>Questo file KML &egrave; prodotto usando i dati presenti nel database di JUG Events.
                            &Egrave; un sottoinsieme della pi&ugrave; estesa
                            <a href="http://wiki.java.net/bin/view/JUGs/JUG-MAP">Java User Groups International Map</a>
                            mantenuta in java.net.</p>
                            <p style="text-align: center;"><a href="http://tinyurl.com/ynktb2" target="jugmap">Guarda la java.net JUGs Map</a></p>
                        </c:when>
                        <c:when test="${requestScope.lang eq 'fr'}">
                            <h2>KML pour le JUG map</h2>
                            <p>Chaque JUG gère ses informations das JUG Events et
                            maintien à jour son fichier KML.</p>
                            <p style="text-align: center;"><a href="${cp}/service/kml.html">Téléchargez le fichier KML</a></p>
                            <p>Le fichier KML est produit à partir des données de JUG Events. Les données d'origine ont été
                                importé à partir de
                            <a href="http://wiki.java.net/bin/view/JUGs/JUG-MAP">Java User Groups International Map</a>.</p>
                        </c:when>
                        <c:when test="${requestScope.requestContext.locale eq 'pt_BR'}">
                            <h2>KML para um mapa de JUG</h2>
                            <p>Qualquer JUG Every pode gerenciar suas informações através do JUG Events e
                            atualizar o seu arquivo KML.</p>
                            <p style="text-align: center;"><a href="${cp}/service/kml.html">Faça o download do arquivo KML</a></p>
                            <p>Este arquivo KML é produzido a partir do banco de dados do JUG Events. Os dados originais foram importados
                                do
                            <a href="http://wiki.java.net/bin/view/JUGs/JUG-MAP">Mapa Internacional de Java User Groups</a>.</p>
                        </c:when>
                        <c:when test="${requestScope.requestContext.locale eq 'pt_PT'}">
                            <h2>KML para um mapa de JUG</h2>
                            <p>Qualquer JUG pode gerir as suas informações através do JUG Events e
                            actualizar o seu ficheiro KML.</p>
                            <p style="text-align: center;"><a href="${cp}/service/kml.html">Faça o download do ficheiro KML</a></p>
                            <p>Este arquivo KML é produzido a partir da base de dados do JUG Events. Os dados originais foram importados
                                do
                            <a href="http://wiki.java.net/bin/view/JUGs/JUG-MAP">Mapa Internacional de Java User Groups</a>.</p>
                        </c:when>
                        <c:when test="${requestScope.lang eq 'pl'}">
                            <h2>Plik KML dla mapy JUGów</h2>
                            <p>Każdy JUG może zarządzać swoimi danymi w JUG Events oraz
                            w pliku KLM.</p>
                            <p style="text-align: center;"><a href="${cp}/service/kml.html">Ściągnij plik KML</a></p>
                            <p>Plik KML jest stworzony z danych z bazy JUG Events.
                            Orginalne dane zostały zaimportowane z
                            <a href="http://wiki.java.net/bin/view/JUGs/JUG-MAP">Java User Groups International Map</a>.</p>
                        </c:when>
                        <c:otherwise>
                            <h2>KML for a JUG map</h2>
                            <p>Every JUG can manage its information in JUG Events which updates this KML file.</p>
                            <p style="text-align: center;"><a href="${cp}/service/kml.html">Download the KML file</a></p>
                            <p>This KML file is produced from the data of the JUG Events database. It is a subset of
                            this more comprehensive
                            <a href="http://wiki.java.net/bin/view/JUGs/JUG-MAP">Java User Groups International Map</a>
                            mantained at java.net.</p>
                            <p style="text-align: center;"><a href="http://tinyurl.com/ynktb2" target="jugmap">View java.net JUGs Map</a></p>
                        </c:otherwise>
                    </c:choose>
                </div>
                <jsp:include page="../menu.jsp"/>
            </div>
        </div>
        <jsp:include page="../footer.jsp"/>
    </body>
</html>