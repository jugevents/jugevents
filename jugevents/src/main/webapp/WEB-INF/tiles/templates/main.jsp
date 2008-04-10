<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ include file="/WEB-INF/jsp/common.jspf" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

    <head>
        <%@ include file="/WEB-INF/jsp/head.jspf" %>
    </head>
    <body>
        <div id="nonFooter">
            <tiles:insertAttribute name="header"/>
            <tiles:insertAttribute name="menu"/>
            <div id="content">
                <div id="content_main_twoColumns">
                    <tiles:insertAttribute name="main"/>
                </div>
                <div id="content_right_column">
                    <tiles:insertAttribute name="sidebar"/>
                </div>
            </div>
            <div style="height: 30px;">&nbsp;</div>
        </div>
        <tiles:insertAttribute name="footer"/>
    </body>
</html>