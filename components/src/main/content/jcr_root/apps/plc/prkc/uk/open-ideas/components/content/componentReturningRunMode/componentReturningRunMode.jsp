<%--

  Simple component returning cq5 running mode.

  This component appears as a sidebar on the right side Article Template

--%><%
%><%@include file="/apps/plc/prkc/uk/open-ideas/global.jsp"%><%
%><%@page session="false"%>
<%@ page import="com.pearson.openideas.cq5.components.content.ComponentReturningRunMode" %>
<%
    ComponentReturningRunMode componentReturningRunMode = new ComponentReturningRunMode(pageContext);
    pageContext.setAttribute("componentReturningRunMode", componentReturningRunMode);
%>

<div class="simpleComponent">
    RUN MODE:
    <div class="runMode">
        ${componentReturningRunMode.runMode}
    </div>
</div>