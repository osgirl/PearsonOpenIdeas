<%@page import="com.pearson.openideas.cq5.components.utils.UrlUtils"%>
<%
%><%@ page import="com.day.cq.i18n.I18n,
                   com.day.cq.wcm.api.WCMMode,
                   com.day.cq.wcm.foundation.forms.FormsHelper,
                   com.day.text.Text,
                   org.apache.sling.scripting.jsp.util.JspSlingHttpServletResponseWrapper,
                   com.day.cq.commons.Externalizer,
                   com.pearson.openideas.cq5.components.utils.CookieUtils,
                   com.pearson.openideas.cq5.components.utils.UrlUtils" %><%
%><%@include file="/apps/plc/prkc/uk/open-ideas/global.jsp"%><%

   
    String id = Text.getName(resource.getPath());
    I18n i18n = new I18n(slingRequest);

    String action = "/bin/open-ideas_security_check";

    String validationFunctionName = "cq5forms_validate_" + id;

    String emailLabel = properties.get("./emailLabel", String.class);
    if (emailLabel == null) {
        emailLabel = i18n.get("Email Address");
    } else {
        emailLabel = i18n.getVar(emailLabel);
    }
    String passwordLabel = properties.get("./passwordLabel", String.class);
    if (passwordLabel == null) {
        passwordLabel = i18n.get("Password");
    } else {
        passwordLabel = i18n.getVar(passwordLabel);
    }

    String referer = request.getHeader("Referer");
    String currentUrl = UrlUtils.getPageUrl(currentPage, sling, slingRequest);
    String defaultRedirect = currentUrl;
    if( referer != null ) {
        defaultRedirect = referer;
    }

    // managed URIs should respect sling mapping
    String redirectTo = slingRequest.getResourceResolver().map(request, properties.get("./redirectTo",defaultRedirect));
    if( !redirectTo.endsWith(".html")) {
        redirectTo += ".html";
    }

    // If return to page is true, get return to page path from cookie
    if (request.getParameter("returnToPage") != null) {
        CookieUtils cookieUtils = new CookieUtils();
        if (cookieUtils.getCookieValue(request, "returnToPagePath") != null) {
            redirectTo  = cookieUtils.getCookieValue(request, "returnToPagePath");
            if( !redirectTo.endsWith(".html")) {
                redirectTo += ".html";
            }            
        }
    }
    
    boolean isDisabled = WCMMode.fromRequest(request).equals(WCMMode.DISABLED);
%>
<script type="text/javascript">
    function <%=validationFunctionName%>() {
        if (CQ_Analytics) {
            var e = document.forms['<%=id%>']['email'].value;
            
            var url = CQ_Analytics.Utils.externalize("/content/ped/elt/global/english-dot-com-students/en/login/lookup_user_id.json", true);
            url = CQ_Analytics.Utils.addParameter(url, "email", e);
            var json = CQ.shared.HTTP.eval(url);
            
            var u = json["userId"];

            if (!u) {
                jQuery('.form_error').html('<%=properties.get("userNotFoundErrorMessage", "Login failed.")%>');
                return false;
            }
            
            if (CQ_Analytics.Sitecatalyst) {
                CQ_Analytics.record({ event: "loginAttempt", values: {
                    username:u,
                    loginPage:"${currentUrl}",
                    destinationPage:"<%= xssAPI.encodeForJSString(redirectTo) %>"
                }, componentPath: '<%=resource.getResourceType()%>'});
                if (CQ_Analytics.ClickstreamcloudUI && CQ_Analytics.ClickstreamcloudUI.isVisible()) {
                    return false;
                }
            }
            <% if ( !isDisabled ) { %>
                if (CQ_Analytics.ProfileDataMgr) {
                    if (u) {
                        /*
                         * AdobePatentID="B1393"
                         */
                        var loaded = CQ_Analytics.ProfileDataMgr.loadProfile(u);
                        if (loaded) {
                            var url = CQ.shared.HTTP.noCaching("<%= xssAPI.encodeForJSString(redirectTo) %>");
                            CQ.shared.Util.load(url);
                        } else {
                            jQuery('.form_error').html('<%=properties.get("defaultErrorMessage", "Login failed.")%>');
                        }
                        return false;
                    }
                }
                return true;
            <% } else { %>
                if (CQ_Analytics.ProfileDataMgr) {
                    CQ_Analytics.ProfileDataMgr.clear();
                }
                return true;
            <% } %>
        }
    }
</script>

<form method="POST"
      action="<%= xssAPI.getValidHref(action) %>"
      id="<%= xssAPI.encodeForHTMLAttr(id) %>"
      name="<%= xssAPI.encodeForHTMLAttr(id) %>"
      enctype="multipart/form-data"
      onsubmit="return <%=validationFunctionName%>();">
    <input type="hidden" name="resource" value="<%= xssAPI.encodeForHTMLAttr(resource.getPath()) %>">
    <input type="hidden" name="redirectTo" value="<%= xssAPI.encodeForHTMLAttr(redirectTo) %>">
    <input type="hidden" name="_charset_" value="UTF-8"/>
    <input type="hidden" name="returnToPage" value="<%=(request.getParameter("returnToPage") != null ? request.getParameter("returnToPage") : "") %>" />
    <input type="hidden" name="errorPage" value="<%= xssAPI.encodeForHTMLAttr(currentUrl) %>">
    <div class="form">
        <div class="text section">
            <div class="form_row">
                <div class="form_leftcol"><label for="<%= xssAPI.encodeForHTMLAttr(id + "_email")%>"><strong style="color:red;">* </strong><%= xssAPI.encodeForHTML(emailLabel) %></label></div>
                <div class="form_rightcol"><div class="form_rightcol_wrapper"><input type="email" id="<%= xssAPI.encodeForHTMLAttr(id + "_email")%>"
                       class="<%= FormsHelper.getCss(properties, "form_field form_field_text form_" + id + "_email") %>"
                       name="email"/></div>
                <%
				    String jReason = request.getParameter("j_reason");
				
				    if (null != jReason) {
				        %><div class="form_error"><%=xssAPI.encodeForHTML(i18n.getVar(jReason))%></div><%    
				    } else {
				        %><div class="form_error"></div><%
				    }
				    
				    String forgotPassword = properties.get("forgotPassword", "");
				    
				%>       
                </div>
            </div>
            <div class="form_row_description"></div>
        </div>
        <div class="password section">
            <div class="form_row">
                <div class="form_leftcol"><label for="<%= xssAPI.encodeForHTMLAttr(id + "_password")%>"><strong style="color:red;">* </strong><%= xssAPI.encodeForHTML(passwordLabel) %></label></div>
                <div class="form_rightcol"><div class="form_rightcol_wrapper"><input id="<%= xssAPI.encodeForHTMLAttr(id + "_password")%>"
                           class="<%= FormsHelper.getCss(properties, "form_field form_field_text form_" + id + "_password") %>"
                   type="password" name="password"/></div></div>
            </div>            
            <div class="form_row_description">
                <% if (forgotPassword.equals("") == false) { %>
                    <a href="<%= forgotPassword%>.html">Forgot password?</a>
                <% } %>
             </div>
         </div>
         <div class="submit section">
            <div class="form_row">
                <div class="form_leftcol"></div>
                <div class="form_rightcol">
                    <button class="form_button_submit" type="submit">Log in <b class="icon"></b></button>
                </div>
            </div>
            <div class="form_row_description"></div>
        </div>
    </div>
</form>