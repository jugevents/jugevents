<%@ include file="../../common.jspf" %>
<%@ taglib uri="http://jawr.net/tags" prefix="jwr" %>
<jwr:script src="${cp}/dwr/interface/speakerBo.js" />
<jwr:script src="${cp}/dwr/interface/filterBo.js" />
<c:choose>
    <c:when test="${empty speaker.indexSpeaker}">
        <h1><spring:message code="speaker.newspeaker" text="?speaker.newspeaker?"/></h1>
    </c:when>
    <c:otherwise>
        <h1><spring:message code="speaker.editspeaker" text="?speaker.editspeaker?"/></h1>                            
    </c:otherwise>
</c:choose>

<c:if test="${empty speaker.indexSpeaker}">
<div id="linkSearchSpeaker">
	<a href="javascript:showSearchSpeaker();"><spring:message code='SearchExistingSpeaker'  text='?SearchExistingSpeaker?'/></a>
</div>
	<div id="searchSpeaker" style="margin-bottom: 1em; display: none;">
		<fieldset><legend><spring:message
			code="SearchSpeaker" text="?SearchSpeaker?" /></legend> <a
			href="javascript:hideSearchSpeaker();"><spring:message
			code='CloseSearchSpeaker' text='?CloseSearchSpeaker?' /></a>
				<form id="fullTextSearch" action="javascript:doNothing();" method="get">
				<div id="content_textSearch">
				<p class="boxTitle"><spring:message code="Search" text="?Search?" />&nbsp;<span
					class="smallText"></span></p>
				<input id="fullTextQuery" name="fullTextQuery" type="text"
					value="${param['fullTextQuery']}" /><br />
				<div id="content_textSearch_result"></div>
				</div>
				</form>
		</fieldset>		
	</div>
</c:if>




<fieldset>
<legend><spring:message code="speaker" text="?speaker?"/>
</legend>

 
<span id="speakerImage"><img  src="${cp}/bin/pictureSpeakerInSession.bin?indexSpeaker=${speaker.indexSpeaker}" alt="Speaker Image" width="100" align="right"/></span>

<form:form commandName="speaker" method="post" action="${cp}/event/speakerevent.form" enctype="multipart/form-data">
   
    <div>
        <form:errors path="*" cssClass="errorBox"/>
	<form:hidden path="indexSpeaker"/>
      
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
            <dt><form:label path="resume"><spring:message code="speaker.resume" text="?speaker.resume?"/></form:label><br/>
            (<a href="http://redcloth.org/hobix.com/textile/" rel="external">Textile</a>)
            </dt>
            <dd><form:textarea path="resume" cols="50" rows="10"/></dd>  
             <dt>&nbsp;</dt>
            <dd><div id="resumePreview" class="preview">${requestScope.speaker.filteredPreview}&nbsp;</div></dd> 
        </dl>       
        <dl>
            <dt>&nbsp;</dt>
            <dd>
           		 <input type="submit" value="<spring:message code='Submit'  text='?Submit?'/>"/>
                 <input value="<spring:message code='Cancel'  text='?Cancel?'/>" type="button" onclick="location.href='${cp}/event/backtoevent.form?indexSpeaker=${speaker.indexSpeaker}'"/><br/><br/>
            </dd>
        </dl>
</div>
</form:form>
</fieldset>



<script type="text/javascript">
dwr.util.setEscapeHtml(false);
new Form.Element.Observer('resume', 2,
		function(el, value) {
		    filterBo.populatePreview(value, 'Textile', 'resumePreview');
});

<c:if test="${empty speaker.indexSpeaker}">
	function showSearchSpeaker() {
		Effect.toggle('searchSpeaker', 'slide'); 		
		//$('searchSpeaker').show();
		$('linkSearchSpeaker').hide();
	}
	function hideSearchSpeaker() {		 
		$('searchSpeaker').hide();
		$('linkSearchSpeaker').show();
	}
	
	function populateSpeakerFields(id) {
		speakerBo.populateSpeakerFields(id);
	}
		speakerBo.fullTextSearch($F('fullTextQuery'), 10);
		new Form.Observer('fullTextSearch', 1, function(el, value) {
			formValues = value.parseQuery();
			speakerBo.fullTextSearch(formValues.fullTextQuery, 10);
		});	
	function doNothing()
	{
		//do nothing
		//alert('"<spring:message code='aaa'  text='?aaa?'/>"');
	}
</c:if>
	
</script>



