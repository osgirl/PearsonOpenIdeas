<%--

  Unsubscribe component.

  This component allows the user to visit the page this component is placed on to unsubscribe from receiving more emails.

--%><%
%><%@include file="/apps/plc/prkc/uk/open-ideas/global.jsp"%><%
%><%@page session="false" import="com.pearson.openideas.cq5.components.content.CreateUnsubscribeLink"%><%
%><%
	CreateUnsubscribeLink unsubscribeLink = new CreateUnsubscribeLink(pageContext);
	pageContext.setAttribute("unsubscribeLink", unsubscribeLink);
%>
<cq:text property="text"/>

        <cq:includeClientLib categories="cq.personalization"/>
        <script type="text/javascript">
            CQ.personalization.variables.Variables.applyToEditComponent("<%=resource.getPath()%>");
        </script>
Click the link below if you wish to unsubscribe:<br/>
${unsubscribeLink.unsubscribeUrl}

<br />
<a style="color:blue;" href="http://uk.pearson.com/legal-notice.html" target="_blank">Terms and Conditions</a>