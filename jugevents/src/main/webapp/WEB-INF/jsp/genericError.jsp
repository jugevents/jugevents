<%@ include file="common.jspf" %>
<h2>
    <spring:message code="attention" text="?attention?"/>
</h2>

<div id="errore">
<h3>
    <spring:message code="error" text="?error?"/>
</h3>

<p>
    <spring:message code="${requestScope.messageCode}" text="${requestScope.messageCode}"/>
</p>

<c:if test="${requestScope.messageCode != 'error.generic'}">
    <p>
        <spring:message code="error.generic" text="?error.generic?"/>
    </p>
</c:if>

<% if (!BaseConf.isProduction()) {%>
<!--
<%
     Exception e = (Exception) request.getAttribute("exception");
     if (e != null) {
         e.printStackTrace(new java.io.PrintWriter(out));
     }
%>
-->
<% }%>
</div>