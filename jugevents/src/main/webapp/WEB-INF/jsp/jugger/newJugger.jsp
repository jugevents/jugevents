<%@ include file="../common.jspf" %>
<jwr:script src="${cp}/dwr/interface/juggerBo.js" />


<h2><spring:message code="juggerRegistrationTitle"/></h2>

<div class="warning"><spring:message code="newAccountRegistrationWarning" text="?newAccountRegistrationWarning?"/></div>

<form:form commandName="jugger" method="post" action="${cp}/jugger/registration.form" onsubmit="enableJugFields(); return true;" enctype="multipart/form-data">
    <form:errors path="*" cssClass="errorBox"/>
    
    
    <fieldset>
        <legend>Jugger</legend>                        
        <form:hidden path="jugger.id"/>
        <dl>
            <dt><form:label path="jugger.firstName"><spring:message code="juggerRegistrationFirstName"/> (*)</form:label></dt>
            <dd><form:input path="jugger.firstName"/></dd>
            <dt><form:label path="jugger.lastName"><spring:message code="juggerRegistrationLastName"/> (*)</form:label></dt>
            <dd><form:input path="jugger.lastName"/></dd>
            <dt><form:label path="jugger.email"><spring:message code="Email"/> (*)</form:label></dt>
            <dd><form:input path="jugger.email"/></dd>
            <dt><form:label path="jugger.user.username"><spring:message code="username"/> (*)</form:label></dt>
            <dd><form:input path="jugger.user.username"/></dd>
        </dl>
    </fieldset>
    
    <fieldset><legend><spring:message
            code="Reliability" /></legend>
        <dl>
            <dt><form:label path="requireReliability.requireReliability">
                    <spring:message code="requireReliability" />
            </form:label></dt>
            <dd><form:checkbox path="requireReliability.requireReliability" value='false'
                               onclick="javascript:require();" />&nbsp;<img id="tip_reliability" src="${cp}/images/question16x16.png" alt="Help Tip"/></dd>
        </dl>
        <div id="hcomment" style="display: none;">
            <dl>
                <dt><spring:message code="commentReliability" /></dt>
                <dd><form:textarea path="requireReliability.comment" cols="35" rows="5" /></dd>
            </dl>
        </div>
    </fieldset>
    
    <fieldset>
        <legend>JUG</legend>                        
        <dl>
            
            <dt><form:label path="jugger.jug.name"><spring:message code="juggerRegistrationJUGName"/> (*)</form:label></dt>                            
            <dd><form:input path="jugger.jug.name" onblur="javascript:disableJugFields();"/><div id="jugList" class="auto_complete"></div></dd>
            <dt><form:label path="jugger.jug.internalFriendlyName"><spring:message code="juggerRegistrationJUGFriendlyName" text="?juggerRegistrationJUGFriendlyName?" /></form:label></dt>
            <dd><form:input path="jugger.jug.internalFriendlyName" /></dd>
            <dt><form:label path="jugger.jug.country.englishName"><spring:message code="juggerRegistrationCountry"/></form:label></dt>                            
            <dd><form:input path="jugger.jug.country.englishName"  /><div id="countryList" class="auto_complete"></div></dd>
            <dt><form:label path="jugger.jug.webSite"><spring:message code="juggerRegistrationWebSite"/></form:label></dt>                            
            <dd><form:input path="jugger.jug.webSite"    /></dd>
            <dt><form:label path="jugger.jug.logo">
                <img id="tip_jugLogo" src="${cp}/images/question16x16.png" alt="Help Tip"/>&nbsp;<spring:message code="juggerRegistrationLogo" />
            </form:label></dt>
            <dd><input type="file" name="jugger.jug.logo" id="jugger.jug.logo"/></dd>
            <dt><form:label path="jugger.jug.longitude"><img id="tip_jugCoordinates" src="${cp}/images/question16x16.png" alt="Help Tip"/>&nbsp;<spring:message code="juggerRegistrationLongitude"/></form:label></dt>                            
            <dd>
                <form:input path="jugger.jug.longitude"  /><br/>
                <a href="http://www.travelgis.com/geocode/" rel="external" class="smallText"><spring:message code="FindYourLocation" text="Find Your Location"/></a>
            </dd>
            <dt><form:label path="jugger.jug.latitude"><spring:message code="juggerRegistrationLatitude"/></form:label></dt>                            
            <dd>
                <form:input path="jugger.jug.latitude" />
            </dd>
            <dt>
                <form:label path="jugger.jug.infos"><spring:message code="juggerRegistrationJUGInfos"/></form:label>
            </dt>
            <dd><form:textarea path="jugger.jug.infos"  cols="35" rows="5" /></dd>                               
        </dl>
    </fieldset>
    <dl>
        <dt><form:label path="captchaResponse"><spring:message code="InsertCharactersInTheImage"/></form:label></dt>
        <dd style="margin-left: 210px;"><form:input path="captchaResponse"/><br/><img src="${cp}/jcaptcha/image.html" alt="Captcha Image"/></dd>
        <dt>&nbsp;</dt>
        <dd><input type="submit" value="Submit"/><br/><br/></dd>
        <dt><spring:message code="juggerRegistrationRequired"/> (*)</dt>
        <dd>&nbsp;</dd>
    </dl>
</form:form>

<script type="text/javascript">

    new Tip($('tip_reliability'), '<spring:message code="tip.reliability"/>', {title: '<spring:message code="tip.reliability.title"/>', effect: 'appear'});
    new Tip($('tip_jugLogo'), '<spring:message code="tip.jugLogo"/>', {title: '<spring:message code="tip.jugLogo.title"/>', effect: 'appear'});
    new Tip($('tip_jugCoordinates'), '<spring:message code="tip.jugCoordinates"/>', {title: '<spring:message code="tip.jugCoordinates.title"/>', effect: 'appear'});

    dwr.util.setEscapeHtml(false);

    disableJugFields();

    new Autocompleter.DWR('jugger.jug.country.englishName', 'countryList', updateCountryList, { valueSelector: singleValueSelector, partialChars: 0, fullSearch: true });
    new Autocompleter.DWR('jugger.jug.name', 'jugList', updateJUGNameList, { valueSelector: singleValueSelector, partialChars: 0, fullSearch: true, afterUpdateElement: populateJugFields });

    function updateCountryList(autocompleter, token) {
        juggerBo.findPartialCountry(token, function(data) {
            autocompleter.setChoices(data)
        });
    }

    function updateJUGNameList(autocompleter, token) {
        juggerBo.findPartialJugNameWithCountry(token,  $('jugger.jug.country.englishName').value, function(data)  {
            autocompleter.setChoices(data)
        });
    }

    function populateJugFields(jugName, selectedElement) {
        juggerBo.populateJugFields(jugName.value);
    }

    function disableJugFields() {
        var s = document.getElementById('jugger.jug.name');    
        juggerBo.readOnlyJugFields(s.value, false);
    }

    function enableJugFields() {
        parancoe.util.fullEnableFormElement('jugger.jug.country.englishName');
        parancoe.util.fullEnableFormElement('jugger.jug.internalFriendlyName');
        parancoe.util.fullEnableFormElement('jugger.jug.webSite');
        parancoe.util.fullEnableFormElement('jugger.jug.logo');
        parancoe.util.fullEnableFormElement('jugger.jug.longitude');
        parancoe.util.fullEnableFormElement('jugger.jug.latitude');
        parancoe.util.fullEnableFormElement('jugger.jug.infos');
    }

    function singleValueSelector(tag) {
        return tag;
    }

    function require()
    {     
        if($('requireReliability.requireReliability1').checked)
        {
            $('hcomment').show(); return false;
        }
        else
        {
            $('hcomment').hide(); return false;
        }   
    }  

</script>
