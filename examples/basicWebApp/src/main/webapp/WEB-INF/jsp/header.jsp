<%@ include file="common.jspf" %>
<%@ taglib prefix="authz" uri="http://acegisecurity.org/authz" %>
<div id="header">
  <span id="menu">
      <a href="${cp}">Home</a> |
      <a href="${cp}/admin/index.html">Admin</a> |
      <a href="${cp}/people/list.html">People</a> |       
      		<authz:authorize ifAnyGranted="ROLE_ADMIN,ROLE_PARANCOE">   			
				<a href="${cp}/j_acegi_logout">Logout</a>
		</authz:authorize>     
  </span>
  <jsp:include page="language.jsp"/>
</div>
