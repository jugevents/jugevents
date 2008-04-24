<%@ include file="common.jspf"%>
<div id="footer">
    <div id="validators">&nbsp;</div>
    <div id="copyright">&#169; 2007, <a href="http://code.google.com/p/jugevents/wiki/JUGEventsContributors" target="project">JUG Events Team</a> <br/><a href="mailto:info@jugevents.org">info@jugevents.org</a></div>
    <div id="references"><a href="http://www.parancoe.org" target="parancoe"><img src="${cp}/images/powered_parancoe.png" alt="Parancoe powered" border="0"/></a></div>
</div>

<% if (!BaseConf.isProduction()) { %>
<jsp:include page="debug.jsp" />
<% } %>

<% if (BaseConf.isProduction()) { %>
<%@ include file="analytics.jspf"%>
<% } %>
