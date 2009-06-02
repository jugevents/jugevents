<%@ include file="common.jspf" %>
<%@ page import="org.springframework.security.ui.AbstractProcessingFilter" %>
<%@ page import="org.springframework.security.ui.webapp.AuthenticationProcessingFilter" %>
<%@ page import="org.springframework.security.AuthenticationException" %>
<%-- this form-login-page form is also used as the form-error-page to ask for a login again. --%>
<div id="loginBox">
    <div id="loginTitle"><spring:message code="loginTitle"/></div>
    <div id="loginMessageBox">
        <c:if test="${not empty param.login_error}">
            <font color="red">
                <spring:message code="login.error"/>
            </font>
        </c:if>
    </div>
    <form id="loginForm" action="<c:url value='securityCheck.secure'/>" method="post">
        <dl>
            <dt><label class="loginLabel" for="username"><spring:message code="username"/></label></dt>
            <dd><input id="username" class="loginField" type='text' name='j_username' tabindex="1" <c:if test="${not empty param.login_error}">value='<%= session.getAttribute(AuthenticationProcessingFilter.SPRING_SECURITY_LAST_USERNAME_KEY)%>'</c:if>/></dd>
            <dt><label class="loginLabel" for="password"><spring:message code="password"/></label></dt>
            <dd><input id="password" class="loginField" type='password' name='j_password' tabindex="2"/></dd>
            <dt><label class="loginLabel" for="remember">&nbsp;</label></dt>
            <dd><input id="remember" type="checkbox" name="_spring_security_remember_me" tabindex="3"/> <spring:message code="remember_me"/></dd>
            <dt><label class="loginLabel" for="submit">&nbsp;</label></dt>
            <dd><input id="submit" name="submit" type="submit" value="<spring:message code='sign_in'/>" tabindex="4"/></dd>
        </dl>
    </form>
    <div id="registerMessage">
        <spring:message code="dontHaveAccount"/> <a href="${cp}/jugger/registration.form"><spring:message code="registerYou"/></a>
    </div>
    <br/>
    <br/>
    <div id="forgotPassword">
        <spring:message code="forgotPassword"/> <a href="${cp}/passwordRecovery.form"><spring:message code="Recover"/></a>
    </div>
</div>
