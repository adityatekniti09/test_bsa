<%@page session="false" import="javax.jcr.*,
        org.apache.sling.api.resource.Resource,
        org.apache.sling.api.resource.ValueMap,
        com.day.cq.commons.inherit.InheritanceValueMap,
        com.day.cq.wcm.commons.WCMUtils,
        com.day.cq.wcm.api.Page,
        com.day.cq.wcm.api.NameConstants,
        com.day.cq.wcm.api.PageManager,
        com.day.cq.wcm.api.designer.Designer,
        com.day.cq.wcm.api.designer.Design,
        com.day.cq.wcm.api.designer.Style,
        com.day.cq.wcm.api.components.ComponentContext,
        com.day.cq.wcm.api.components.EditContext"
%><%@page trimDirectiveWhitespaces="true"
%><%@taglib prefix="sling" uri="http://sling.apache.org/taglibs/sling/1.0" %><%
%><%@taglib prefix="cq" uri="http://www.day.com/taglibs/cq/1.0" %><%
%><%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %><%
%><%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %><%
%><%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%
%><cq:defineObjects/><%
%><sling:defineObjects/><%
%><cq:setContentBundle source="auto"/><%
%>
<%@page import="com.day.cq.wcm.api.WCMMode"%>
<c:set var="isAuthorMode" value="<%=WCMMode.fromRequest(request) == WCMMode.EDIT%>"/>
<c:set var="isPreviewMode" value="<%=WCMMode.fromRequest(request) == WCMMode.PREVIEW%>"/>
<c:set var="isDesignMode" value="<%=WCMMode.fromRequest(request) == WCMMode.DESIGN%>"/>
<c:set var="isPublishMode" value="<%=WCMMode.fromRequest(request) == WCMMode.DISABLED%>"/>
