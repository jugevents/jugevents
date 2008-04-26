<%@ include file="common.jspf"%>
<div id="footer">
    <div id="validators">
        <a href="http://validator.w3.org/check?uri=referer"><img
                src="http://www.w3.org/Icons/valid-xhtml10-blue"
            alt="Valid XHTML 1.0 Strict" height="31" width="88" style="border: 0"/></a>
    </div>
    <div id="copyright">&#169; 2007, <a href="http://code.google.com/p/jugevents/wiki/JUGEventsContributors" rel="external">JUG Events Team</a> <br/><a href="mailto:info@jugevents.org">info@jugevents.org</a></div>
    <div id="references"><a href="http://www.parancoe.org" rel="external"><img src="${cp}/images/powered_parancoe.png" alt="Parancoe powered" style="border: 0"/></a></div>
</div>

<% if (!BaseConf.isProduction()) {%>
<jsp:include page="debug.jsp" />
<% }%>

<% if (BaseConf.isProduction()) {%>
<%@ include file="analytics.jspf"%>
<% }%>
