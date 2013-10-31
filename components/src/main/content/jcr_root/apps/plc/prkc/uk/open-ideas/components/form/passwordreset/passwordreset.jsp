<%@include file="/apps/plc/prkc/uk/open-ideas/global.jsp"%><%
%><%@ page import="com.day.cq.wcm.foundation.forms.FormsHelper,
                   com.day.cq.wcm.foundation.forms.LayoutHelper,
                   com.day.cq.security.AccountManager,
                   com.day.cq.security.AccountManagerFactory,
                   com.day.cq.security.User,
                   org.apache.sling.jcr.api.SlingRepository" %><%
                   
    final String name = FormsHelper.getParameterName(resource);
    final String id = FormsHelper.getFieldId(slingRequest, resource);
    final String confirmId = FormsHelper.getFieldId(slingRequest, resource) + "_confirm";
    final boolean required = FormsHelper.isRequired(resource);
    final boolean hideTitle = properties.get("hideTitle", false);
    final String title = FormsHelper.getTitle(resource, "Password");
    final String confirmTile = properties.get("confirmationTitle", "Confirm Password");
    final String width = properties.get("width", "");
    final String cols = properties.get("cols", "35");

    final String key = request.getParameter("ky") == null ? null : slingRequest.getRequestParameter("ky").getString();
    final String uid = request.getParameter("uid") == null ? null : slingRequest.getRequestParameter("uid").getString();
    String userName = null;

    // todo deny anonymous, admin

    if (key == null && uid == null) {
        User user = resourceResolver.adaptTo(User.class);
        if (user == null) {
            log.error("Could not resolve logged in user");
            // todo handle error
            // response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
        else {
            userName = user.getName();
        }

    } else if (key != null && uid != null) {
        SlingRepository repository = sling.getService(SlingRepository.class);
        Session session = repository.loginAdministrative(null);
        try {
            AccountManagerFactory factory = sling.getService(AccountManagerFactory.class);
            AccountManager accountManager = factory.createAccountManager(session);
            User user = accountManager.findAccount(uid);
            if (user == null) {
                log.debug("No user matching uid {}", uid);
                // todo handle error
                // response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            }
            else {
                userName = user.getName();
            }
        }
        finally {
        	if (session != null) {
            	session.logout();
        	}
        }

    } else {
        // todo handle error
        // response.sendError(HttpServletResponse.SC_BAD_REQUEST);
    }

    %><div class="form_row">
        <% LayoutHelper.printTitle(id, title, required, hideTitle, out); %>
        <div class="form_rightcol"><div class="form_rightcol_wrapper"><input class="geo textinput" id="<%= id %>" name="<%= name %>" value="" type="password" size="<%= cols %>" <%= (width.length() > 0 ? "style='width:" + width + "px;'" : "") %>></div></div>
    </div>
    <div class="form_row">
      <% LayoutHelper.printTitle(confirmId, confirmTile, required, hideTitle, out); %>
      <div class="form_rightcol"><div class="form_rightcol_wrapper"><input class="geo textinput" id="<%= confirmId %>" name="<%= name %>_confirm" value="" type="password" size="<%= cols %>" <%= (width.length()>0 ? "style='width:" + width + "px;'" : "") %>></div></div>
    </div><%
if (uid != null) { %>
  <input type="hidden" name="uid" value="<%=uid%>">
<% }
if (key != null) {%>
  <input type="hidden" name="ky" value="<%=key%>">
<% } %>
<%
    LayoutHelper.printDescription(FormsHelper.getDescription(resource, ""), out);
    LayoutHelper.printErrors(slingRequest, name, out);
%>