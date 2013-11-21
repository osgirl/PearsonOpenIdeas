<%--

  Simple component returning cq5 running mode.

--%><%
%><%@include file="/apps/plc/prkc/uk/open-ideas/global.jsp"%><%
%><%@page session="false"%>
<%@ taglib prefix="blx" uri="http://openideas-pearson/tacit/tags" %>

<blx:ComponentReturningRunModeTag/>

<div class="simpleComponent">
    RUN MODE:
    <div class="runMode">
        ${contentComponent.runMode}
    </div>
</div>