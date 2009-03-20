<%@ include file="../../common.jspf" %>
<jwr:script src="${cp}/dwr/interface/eventBo.js" />
<jwr:script src="${cp}/dwr/interface/filterBo.js" />
<c:choose>
    <c:when test="${empty speakerId}">
        <h1><spring:message code="speaker.newspeaker" text="?speaker.newspeaker?"/></h1>
    </c:when>
    <c:otherwise>
        <h1><spring:message code="speaker.editspeaker" text="?speaker.editspeaker?"/></h1>                            
    </c:otherwise>
</c:choose>

 
<img  src="${cp}/bin/pictureSpeakerInSession.bin?speakerId=${speakerId}" alt="Speaker Image" width="100" align="right"/>  

<form:form commandName="speaker" method="post" action="${cp}/event/speakerevent.form?speakerId=${speakerId}" enctype="multipart/form-data">
    <div>
        <form:errors path="*" cssClass="errorBox"/>
      
        <dl>
            <dt><form:label path="firstName"><spring:message code="first_name" text="?first_name?"/></form:label></dt>
            <dd><form:input path="firstName" size="35"/></dd>
            <dt><form:label path="lastName"><spring:message code="last_name" text="?last_name?"/></form:label></dt>
            <dd><form:input path="lastName" size="35"/></dd>
            <dt><form:label path="email"><spring:message code="Email" text="?Email?"/></form:label></dt>
            <dd><form:input path="email" size="35"/></dd>
            <dt><form:label path="url"><spring:message code="WebSite" text="?WebSite?"/></form:label></dt>
            <dd><form:input path="url" size="35"/></dd>
            <dt><form:label path="skypeId"><spring:message code="SkypeId" text="?SkypeId?"/></form:label></dt>
            <dd><form:input path="skypeId" size="35"/></dd>
            <dt><form:label path="picture"><spring:message code="speaker.picture" text="?speaker.picture?"/></form:label></dt>
            <dd><input type="file" name="picture"></dd>
            <dt><form:label path="resume"><spring:message code="speaker.resume" text="?speaker.resume?"/></form:label></dt>
            <dd><form:textarea path="resume" cols="40" rows="8"/></dd>  


        </dl>       
        <dl>
            <dt>&nbsp;</dt>
            <dd>
           		 <input type="submit" value="<spring:message code='Submit'  text='?Submit?'/>"/>
                 <input value="<spring:message code='Cancel'  text='?Cancel?'/>" type="button" onclick="location.href='${cp}/event/backtoevent.form'"/><br/><br/>
            </dd>
        </dl>
</div>
</form:form>
<script type="text/javascript">

</script>


