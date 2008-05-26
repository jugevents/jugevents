<%@ page pageEncoding="UTF-8" %>
<%@ include file="../../common.jspf" %>
<div id="res${param.id}" style="border: 1px solid gray; padding: 5px; margin: 2px; float: left; width: 320px; display: ${param.display};}">
    <embed src="http://www.archive.org/flow/FlowPlayerLight.swf?config=%7Bembedded%3Atrue%2CshowFullScreenButton%3Atrue%2CshowMuteVolumeButton%3Atrue%2CshowMenu%3Atrue%2CautoBuffering%3Atrue%2CautoPlay%3Afalse%2CinitialScale%3A%27fit%27%2CmenuItems%3A%5Bfalse%2Cfalse%2Cfalse%2Cfalse%2Ctrue%2Ctrue%2Cfalse%5D%2CusePlayOverlay%3Afalse%2CshowPlayListButtons%3Atrue%2CplayList%3A%5B%7Burl%3A%27${param.flashVideoUrl}%27%7D%5D%2CcontrolBarGloss%3A%27high%27%2CshowVolumeSlider%3Atrue%2CbaseURL%3A%27http%3A%2F%2Fwww%2Earchive%2Eorg%2Fdownload%2F%27%2Cloop%3Afalse%2CcontrolBarBackgroundColor%3A%270x000000%27%7D" width="320" height="268" scale="noscale" bgcolor="111111" type="application/x-shockwave-flash" allowFullScreen="true" allowScriptAccess="always" allowNetworking="all" pluginspage="http://www.macromedia.com/go/getflashplayer"></embed>
    <div><a href="${param.detailsUrl}"><spring:message code="DetailsAndOtherFormats" text="?DetailsAndOtherFormats?"/></a></div>
    <c:out value="${param.description}" escapeXml="true"/>
    <div style="text-align: right;" class="smallText">
        <c:if test="${param.canUserManageTheEvent == 'true'}">                
            <spring:message code='confirmDeleteEventResource' var="confirmDeleteEventResourceMessage" text="?confirmDeleteEventResourceMessage?"/>
            <a href="#" onclick="modifyEventResource(${param.id}); $('resourceType').disable(); return false;"><spring:message code="modify" text="?modify?"/></a>
            <a href="javascript: deleteEventResource(${param.id})" onclick="return confirm('${confirmDeleteEventResourceMessage}')"><spring:message code="delete"/></a>
        </c:if>
    </div>
</div>
