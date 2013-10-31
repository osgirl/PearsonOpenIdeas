<%--
  Copyright 1997-2010 Day Management AG
  Barfuesserplatz 6, 4001 Basel, Switzerland
  All Rights Reserved.

  This software is the confidential and proprietary information of
  Day Management AG, ("Confidential Information"). You shall not
  disclose such Confidential Information and shall use it only in
  accordance with the terms of the license agreement you entered into
  with Day.

  ==============================================================================

  Newsletter body script.

  ==============================================================================
--%>
<%@page import="com.day.cq.widget.HtmlLibraryManager" %>
<%@page import="org.apache.sling.api.scripting.SlingScriptHelper" %>
<%@page import="com.pearson.openideas.cq5.components.services.PropertiesService" %>
<%@include file="/apps/plc/prkc/uk/open-ideas/global.jsp" %>
<%
    SlingScriptHelper slingScriptHelper = (SlingScriptHelper) pageContext.getAttribute("sling");
    PropertiesService propertiesService = slingScriptHelper.getService(PropertiesService.class);
    String pubUrl = propertiesService.getPublishUrl();
%>

<body bgcolor="#fcf5ea" marginwidth="0" marginheight="0" topmargin="0" leftmargin="0">
    <div id="wrapper">
        <table cellpadding="0" cellspacing="0" width="100%" bgcolor="#fcf5ea" border="0"><tbody>
            <tr><td height="30"><cq:include script="actionstoolbar.jsp"/></td></tr>
            <tr>
                <td valign="top">
                    <table cellpadding="0" cellspacing="0" width="560" bgcolor="#fcf5ea" border="0"
                            style="margin: 0 auto;"><tbody><tr>
                        <td>
                            <table cellpadding="0" cellspacing="0" width="530" border="0" style="margin: 0 auto;">
                                <tbody>
                                <tr><td height="15"></td></tr>
                                <tr><td>
                                        <img src='<%= pubUrl %>/etc/designs/plc/prkc/uk/open-ideas/clientlibs/img/openideas_logo.png' style='width:165px;height:80px;margin: 8px 8px 20px 8px;' />
                                </td></tr>
                                <tr><td>
								<div style='background-image: linear-gradient(top, rgb(68,68,68) 36%, rgb(51,51,51) 68%, rgb(51,51,51) 84%);background-image: -o-linear-gradient(top, rgb(68,68,68) 36%, rgb(51,51,51) 68%, rgb(51,51,51) 84%);background-image: -moz-linear-gradient(top, rgb(68,68,68) 36%, rgb(51,51,51) 68%, rgb(51,51,51) 84%);background-image: -webkit-linear-gradient(top, rgb(68,68,68) 36%, rgb(51,51,51) 68%, rgb(51,51,51) 84%);background-image: -ms-linear-gradient(top, rgb(68,68,68) 36%, rgb(51,51,51) 68%, rgb(51,51,51) 84%);background-image: -webkit-gradient(linear,left top,left bottom,color-stop(0.36, rgb(68,68,68)),color-stop(0.68, rgb(51,51,51)),color-stop(0.84, rgb(51,51,51)));padding: 4px 0 10px 8px;height:16px;width:100%; margin: 0 0 40px 0;'></div>
                                    </td></tr>
                                <tr><td style="
                                        font-family:Arial,sans-serif;
                                        font-size: 14px;
                                        color:#222222;
                                    "><cq:include path="par" resourceType="mcm/components/newsletter/parsys"/>
                                </td></tr>
                                <tr><td>
                                        <div style='background:#393a3a; width:100%; padding:10px 10px 10px 10px; height:26px;margin: 20px 0 0;'><img src='<%= pubUrl %>/etc/designs/plc/prkc/uk/open-ideas/clientlibs/img/alwayslearning.png' style='float:left;width:140px; height:15px'><img src='<%= pubUrl %>/etc/designs/plc/prkc/uk/open-ideas/clientlibs/img/pearson.png' style='float:right; width:105px;height:25px;'></div>

                                </td></tr>
                            </tbody></table>
                        </td>
                    </tr></tbody></table>
                </td>
            </tr>
            <tr><td height="20"></td></tr>
            <tr><td align="center" style="
                font-family:Arial,sans-serif;
                font-size:10px;
                color:#ffffff;
                text-align:center;
            ">
            </td></tr>
            <tr><td height="10"></td></tr>
        </tbody></table>
    </div>
<cq:include path="cloudservices" resourceType="cq/cloudserviceconfigs/components/servicecomponents"/>
</body>