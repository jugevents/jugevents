<%@ include file="../../common.jspf" %>
<jwr:script src="${cp}/dwr/interface/eventBo.js" />
<jwr:script src="${cp}/dwr/interface/filterBo.js" />
<c:choose>
    <c:when test="${empty id}">
        <h1><spring:message code="speaker.newspeaker" text="?speaker.newspeaker?"/></h1>
    </c:when>
    <c:otherwise>
        <h1><spring:message code="speaker.editspeaker" text="?speaker.editspeaker?"/></h1>                            
    </c:otherwise>
</c:choose>
<form:form commandName="speaker" method="post" action="${cp}/event/speakerevent.form">
    <div>
        <form:errors path="*" cssClass="errorBox"/>
        <form:hidden path="id"/>
        <dl>
            <dt><form:label path="speakerCoreAttributes.firstName"><spring:message code="first_name" text="?first_name?"/></form:label></dt>
            <dd><form:input path="speakerCoreAttributes.firstName" size="35"/></dd>
            <dt><form:label path="speakerCoreAttributes.lastName"><spring:message code="last_name" text="?last_name?"/></form:label></dt>
            <dd><form:input path="speakerCoreAttributes.lastName" size="35"/></dd>
            <dt><form:label path="speakerCoreAttributes.email"><spring:message code="Email" text="?Email?"/></form:label></dt>
            <dd><form:input path="speakerCoreAttributes.email" size="35"/></dd>
            <dt><form:label path="speakerCoreAttributes.url"><spring:message code="WebSite" text="?WebSite?"/></form:label></dt>
            <dd><form:input path="speakerCoreAttributes.url" size="35"/></dd>
            <dt><form:label path="speakerCoreAttributes.skypeId"><spring:message code="SkypeId" text="?SkypeId?"/></form:label></dt>
            <dd><form:input path="speakerCoreAttributes.skypeId" size="35"/></dd>
            <dt><form:label path="picture"><spring:message code="speaker.picture" text="?speaker.picture?"/></form:label></dt>
            <dd><input type="file" name="picture"></dd>
            <dt><form:label path="resume"><spring:message code="speaker.resume" text="?speaker.resume?"/></form:label></dt>
            <dd><form:textarea path="resume" cols="40" rows="8"/></dd>  


        </dl>       
        <dl>
            <dt>&nbsp;</dt>
            <dd><input type="submit" value="<spring:message code='Submit'  text='?Submit?'/>"/><br/><br/></dd>
        </dl>
</div>
</form:form>

