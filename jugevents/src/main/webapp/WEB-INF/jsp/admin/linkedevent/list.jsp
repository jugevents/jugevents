<%@ include file="/WEB-INF/jsp/common.jspf" %>
<h1><spring:message code='LinkedEvents' text='?LinkedEvents?'/></h1>

<div class="secondaryMenu">
    <a href="create.html"><spring:message code="NewEvent" text="?NewEvent?"/></a>
</div>

<div class="displaytag">
    <display:table name="events" id="event" sort="list" pagesize="20" defaultsort="1" defaultorder="descending">
        <display:column property="title" titleKey="Title"  sortable="true" headerClass="sortable"/>
        <display:column property="url" titleKey="URL"  sortable="true" headerClass="sortable"/>
        <display:column property="urlLabel" titleKey="URLLabel"  sortable="true" headerClass="sortable"/>
        <display:column property="startDate" titleKey="event.startDate"  sortable="true" headerClass="sortable"/>
        <display:column property="endDate" titleKey="event.endDate"  sortable="true" headerClass="sortable"/>
        <display:column property="expositionStartDate" titleKey="exposition.startDate"  sortable="true" headerClass="sortable"/>
        <display:column property="expositionEndDate" titleKey="exposition.endDate"  sortable="true" headerClass="sortable"/>
        <display:column>
            <a href="edit.html?id=${event.id}"><spring:message code="edit" text="?edit?"/></a>
            <spring:message code='confirmDeleteEvent' var="confirmDeleteEventMessage" text="?confirmDeleteEventMessage?"/>
            <a href="delete.html?id=${event.id}"  onclick="return confirm('${confirmDeleteEventMessage}')"><spring:message code="delete"  text="?delete?"/></a>
        </display:column>
    </display:table>
</div>