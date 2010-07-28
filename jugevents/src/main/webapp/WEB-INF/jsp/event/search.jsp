<%@ include file="../common.jspf" %>
<%@ taglib uri="http://jawr.net/tags" prefix="jwr" %>
<jwr:script src="${cp}/dwr/interface/juggerBo.js" />
<jwr:script src="${cp}/dwr/interface/eventBo.js" />
<h1>
    <spring:message code="SearchEvents"/>
    <a href="${cp}/event/rss.html?continent=${eventSearch.continent}&amp;country=${eventSearch.country}&amp;jugName=${eventSearch.jugName}&amp;pastEvents=${eventSearch.pastEvents}&amp;order=${eventSearch.orderByDate}">
        <img style="vertical-align: middle; border: none;" src="${cp}/images/feed-icon-14x14.png" alt="Feed icon"/>
    </a>
    <span class="smallText">
        <a href="${cp}/event/rss.html?continent=${eventSearch.continent}&amp;country=${eventSearch.country}&amp;jugName=${eventSearch.jugName}&amp;pastEvents=${eventSearch.pastEvents}&amp;order=${eventSearch.orderByDate}">
            <spring:message code="SearchFeed" text="?SearchFeed?"/>
        </a>
    </span>
    <a href="${cp}/event/ics.html?continent=${eventSearch.continent}&amp;country=${eventSearch.country}&amp;jugName=${eventSearch.jugName}&amp;pastEvents=${eventSearch.pastEvents}&amp;order=${eventSearch.orderByDate}">
        <img style="vertical-align: middle; border: none;" src="${cp}/images/ics_icon.gif" alt="ICS icon"/>
    </a>
    <span class="smallText">
        <a href="${cp}/event/ics.html?continent=${eventSearch.continent}&amp;country=${eventSearch.country}&amp;jugName=${eventSearch.jugName}&amp;pastEvents=${eventSearch.pastEvents}&amp;order=${eventSearch.orderByDate}">
            <spring:message code="SearchCalendar" text="?SearchCalendar?"/>
        </a>
    </span>
    <a href="${cp}/event/json.html?continent=${eventSearch.continent}&amp;country=${eventSearch.country}&amp;jugName=${eventSearch.jugName}&amp;pastEvents=${eventSearch.pastEvents}&amp;order=${eventSearch.orderByDate}">
        <img style="vertical-align: middle; border: none;" src="${cp}/images/json_icon.png" alt="JSON icon"/>
    </a>
    <span class="smallText">
        <a href="${cp}/event/json.html?continent=${eventSearch.continent}&amp;country=${eventSearch.country}&amp;jugName=${eventSearch.jugName}&amp;pastEvents=${eventSearch.pastEvents}&amp;order=${eventSearch.orderByDate}">
            <spring:message code="SearchJson" text="?SearchJson?"/>
        </a>
    </span>
</h1>
<a href="#" onclick="updateBadge(); $('webBadge').show(); new Effect.ScrollTo('webBadge', {offset: -24}); return false;"><spring:message code="GetBadgeLink"/></a>
<a name="searchForm"></a>
<form:form commandName="eventSearch" method="get" action="${cp}/event/search.form">
    <fieldset>
        <legend><spring:message code='Search'/></legend>
        <dl>
            <dt><form:label path="continent"><spring:message code="search.continent"/>:</form:label></dt>
            <dd><form:input path="continent"/><div id="continentList" class="auto_complete"></div></dd>
            <dt><form:label path="country"><spring:message code="search.country"/>:</form:label></dt>
            <dd><form:input path="country"/><div id="countryList" class="auto_complete"></div></dd>
            <dt><form:label path="jugName"><spring:message code="search.jugName"/>:</form:label></dt>
            <dd><form:input path="jugName"/><div id="jugNameList" class="auto_complete"></div></dd>
            <dt><form:label path="pastEvents"><spring:message code="search.pastEvents"/>:</form:label></dt>
            <dd><form:checkbox path="pastEvents" id="pastEvents"/></dd>
            <dt><form:label path="orderByDate"><spring:message code="search.orderByDate"/>:</form:label></dt>
            <dd><form:radiobutton path="orderByDate" value="asc"/><spring:message code="Ascending"/>&nbsp;<form:radiobutton path="orderByDate" value="desc"/><spring:message code="Descending"/></dd>
            <dt>&nbsp;</dt><dd><input type="submit" value="<spring:message code='Search'/>"/></dd>
        </dl>
    </fieldset>
</form:form>

<c:choose>
    <c:when test="${not empty events}">
        <div class="displaytag">
            <display:table name="events" id="event" sort="list" pagesize="20" requestURI="search.form" export="true">
                <display:column media="html" sortProperty="hostingOrganizationName" title="JUG" sortable="true" headerClass="sortable">
                    <c:choose>
                        <c:when test="${!empty event.hostingOrganizationUrl}">
                            <a href="${event.hostingOrganizationUrl}" rel="external">${event.hostingOrganizationName}</a>
                        </c:when>
                        <c:otherwise>
                        ${event.hostingOrganizationName}
                        </c:otherwise>
                    </c:choose>
                </display:column>
                <display:column media="csv xml excel pdf" property="hostingOrganizationName" title="JUG" sortable="true" headerClass="sortable"/>
                <display:column media="html" sortProperty="title" titleKey="Event" sortable="true" headerClass="sortable">
                    <a href="${cp}/event/${event.id}">${event.title}</a>
                </display:column>
                <display:column media="csv xml excel pdf" property="title" titleKey="Event" sortable="true" headerClass="sortable"/>
                <display:column sortProperty="startDate" titleKey="Date" sortable="true" headerClass="sortable">
                    <fmt:formatDate value="${event.startDate}" dateStyle="SHORT" />
                </display:column>
                <display:column property="numberOfParticipants" title="#" sortable="true" headerClass="sortable"/>
                <display:column media="html" title="" sortable="false" headerClass="actionColumn" class="actionColumn">
                    <security:authorize ifAnyGranted="ROLE_ADMIN,ROLE_JUGGER">
                        <%
            if (servicesBo.canCurrentUserManageEvent((it.jugpadova.po.Event) pageContext.getAttribute("event"))) {
                        %>
                        <a href="edit.form?id=${event.id}"><spring:message code="edit"/></a>
                        <%            }
                        %>
                    </security:authorize>
                    <security:authorize ifAnyGranted="ROLE_ADMIN,ROLE_JUGGER">
                        <%
            if (servicesBo.canCurrentUserManageEvent((it.jugpadova.po.Event) pageContext.getAttribute("event"))) {
                        %>
                        <spring:message code='confirmDeleteEvent' var="confirmDeleteEventMessage"/>
                        <a href="delete.html?id=${event.id}" onclick="return confirm('${confirmDeleteEventMessage}')"><spring:message code="delete"/></a>
                        <%            }
                        %>
                    </security:authorize>
                    <c:if test="${event.registrationOpen}">
                        <a href="registration.form?event.id=${event.id}"><spring:message code="register"/></a>
                    </c:if>
                    <security:authorize ifAnyGranted="ROLE_ADMIN,ROLE_JUGGER">
                        <%
            if (servicesBo.canCurrentUserManageEvent((it.jugpadova.po.Event) pageContext.getAttribute("event"))) {
                        %>
                        <a href="participants.html?id=${event.id}"><spring:message code="participants"/></a>
                        <%            }
                        %>
                    </security:authorize>
                </display:column>
            </display:table>
        </div>
    </c:when>
    <c:otherwise>
        <c:if test="${not empty requestScope.showNoResultsMessage}">
            <spring:message code="NoResults"/>
        </c:if>
    </c:otherwise>
</c:choose>
<br/>
<div id="webBadge" style="display: none;">
    <fieldset>
        <legend><spring:message code='WebBadge'/></legend>
        <a href="#" onclick="$('webBadge').hide(); return false;"><spring:message code="CloseBadgePanel"/></a>
        <dl>
            <dt><label><spring:message code="IncludeJUGName"/>:</label></dt>
            <dd><input type="checkbox" name="badgeIncludeJUGName" id="badgeIncludeJUGName" onchange="updateBadge()"/></dd>
            <dt><label><spring:message code="IncludeCountry"/>:</label></dt>
            <dd><input type="checkbox" name="badgeIncludeCountry" id="badgeIncludeCountry" onchange="updateBadge()"/></dd>
            <dt><label><spring:message code="IncludeTheDescription"/>:</label></dt>
            <dd><input type="checkbox" name="badgeIncludeTheDescription" id="badgeIncludeTheDescription" onchange="updateBadge()"/></dd>
            <dt><label><spring:message code="IncludeTheParticipants"/>:</label></dt>
            <dd><input type="checkbox" name="badgeIncludeTheParticipants" id="badgeIncludeTheParticipants" onchange="updateBadge()"/></dd>
            <dt><label><spring:message code="MaxNumberOfResults"/>:</label></dt>
            <dd><input type="text" name="maxResults" id="maxResults" onchange="updateBadge()" size="2"/></dd>
            <dt><label><spring:message code="Style"/>:</label></dt>
            <dd><input type="radio" name="badgeStyle" value="none" checked="checked" onchange="updateBadge()"/><spring:message code='None'/>&nbsp;<input type="radio" name="badgeStyle" value="simple" onchange="updateBadge()"/><spring:message code='Simple'/></dd>
            <dt><label><spring:message code="badgeCopyThisCode"/></label></dt>
            <dd><textarea name="badgeCode" cols="35" rows="4" readonly="readonly">${badgeCode}</textarea></dd>
            <dt><label><spring:message code="BadgePreview"/>:</label></dt>
            <dd><div id="badgePreview" style="margin-left: 10px; width: 200px; border: 1px solid gray; padding: 4px;">${badgePreview}</div></dd>
        </dl>
    </fieldset>
</div>
<script type="text/javascript">

dwr.util.setEscapeHtml(false);

new Autocompleter.DWR('continent', 'continentList', updateContinentList, { valueSelector: singleValueSelector, partialChars: 0, fullSearch: true });
new Autocompleter.DWR('country', 'countryList', updateCountryList, { valueSelector: singleValueSelector, partialChars: 0, fullSearch: true });
new Autocompleter.DWR('jugName', 'jugNameList', updateJugNameList, { valueSelector: singleValueSelector, partialChars: 0, fullSearch: true });

function updateContinentList(autocompleter, token) {
juggerBo.findPartialContinent(token, function(data) {
autocompleter.setChoices(data)
});
}

function updateCountryList(autocompleter, token) {
juggerBo.findPartialCountryWithContinent(token, $('continent').value, function(data) {
autocompleter.setChoices(data)
});
}

function updateJugNameList(autocompleter, token) {
juggerBo.findPartialJugNameWithCountryAndContinent(token, $('country').value, $('continent').value, function(data) {
autocompleter.setChoices(data)
});
}

function singleValueSelector(tag) {
return tag;
}

function updateBadge() {
eventBo.updateBadgePanel($('continent').value, $('country').value, $('jugName').value, $('pastEvents').checked, $$('input[type=radio][name=orderByDate]').find(function(el) { return el.checked }).value, $('badgeIncludeJUGName').checked, $('badgeIncludeCountry').checked, $('badgeIncludeTheDescription').checked, $('badgeIncludeTheParticipants').checked, $$('input[type=radio][name=badgeStyle]').find(function(el) { return el.checked }).value, '${requestScope.lang}', $('maxResults').value);
}

</script>
