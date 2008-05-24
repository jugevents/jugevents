<%@ page pageEncoding="UTF-8" %>
<%@ include file="../../common.jspf" %>
<div id="res${param.id}" style="border: 1px solid gray; padding: 5px; margin: 2px; float: left; width: 93%; display: ${param.display};}">
    <!-- Start of Flickr Badge -->
    <style type="text/css">
        .flickr_badge_source_txt {padding:0; font: 11px Arial, Helvetica, Sans serif; color:#666666;}
        #flickr_badge_icon {display:block !important; margin:0 !important; border: 1px solid rgb(0, 0, 0) !important;}
        #flickr_icon_td {padding:0 5px 0 0 !important;}
        .flickr_badge_image {text-align:center !important;}
        .flickr_badge_image img {border: 1px solid black !important;}
        .flickr_www {display:block; text-align:left; padding:0 10px 0 10px !important; font: 11px Arial, Helvetica, Sans serif !important; color:#3993ff !important;}
        .flickr_badge_uber_wrapper a:hover,
        .flickr_badge_uber_wrapper a:link,
        .flickr_badge_uber_wrapper a:active,
        .flickr_badge_uber_wrapper a:visited {text-decoration:none !important; background:inherit !important;color:#3993ff;}
        .flickr_badge_wrapper {background-color:#ffffff;border: solid 1px #000000}
        .flickr_badge_source {padding:0 !important; font: 11px Arial, Helvetica, Sans serif !important; color:#666666 !important;}
    </style>
    <table class="flickr_badge_uber_wrapper" cellpadding="0" cellspacing="10" border="0"><tr><td><a href="http://www.flickr.com" class="flickr_www">www.<strong style="color:#3993ff">flick<span style="color:#ff1c92">r</span></strong>.com</a><table cellpadding="0" cellspacing="10" border="0" class="flickr_badge_wrapper">
                    <tr>
                        <script type="text/javascript" src="http://www.flickr.com/badge_code_v2.gne?show_name=1&count=7&display=latest&size=s&layout=h&source=all_tag&tag=${param.tag}"></script>
                        <td class="flickr_badge_source" valign="center" align="center">
                            <table cellpadding="0" cellspacing="0" border="0"><tr>
                                    <td class="flickr_badge_source_txt">Altre <a href="http://www.flickr.com/photos/tags/${param.tag}/"> foto o video provvisti di tag ${param.tag}</a>su Flickr</td>
                            </tr></table>
                        </td>
                    </tr>
                </table>
    </td></tr></table>
    <!-- End of Flickr Badge -->
    <c:out value="${param.description}" escapeXml="true"/>
    <div style="text-align: right;" class="smallText">
        <authz:authorize ifAnyGranted="ROLE_ADMIN,ROLE_JUGGER">
            <c:if test="${param.canUserManageTheEvent == 'true'}">                
                <spring:message code='confirmDeleteEventResource' var="confirmDeleteEventResourceMessage" text="?confirmDeleteEventResourceMessage?"/>
                <a href="#" onclick="modifyEventResource(${param.id}); $('resourceType').disable(); return false;"><spring:message code="modify" text="?modify?"/></a>
                <a href="javascript: deleteEventResource(${param.id})" onclick="return confirm('${confirmDeleteEventResourceMessage}')"><spring:message code="delete"/></a>
            </c:if>
        </authz:authorize>
    </div>
</div>
