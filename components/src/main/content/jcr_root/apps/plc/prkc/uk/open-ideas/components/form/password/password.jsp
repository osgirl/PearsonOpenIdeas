<%@include file="/apps/plc/prkc/uk/open-ideas/global.jsp"%><%
%><%@ page import="com.day.cq.wcm.foundation.forms.FormsHelper,
                   com.day.cq.wcm.foundation.forms.LayoutHelper" %><%
    final String name = FormsHelper.getParameterName(resource);
    final String id = FormsHelper.getFieldId(slingRequest, resource);
    final String confirmId = FormsHelper.getFieldId(slingRequest, resource) + "_confirm";
    final boolean required = FormsHelper.isRequired(resource);
    final boolean hideTitle = properties.get("hideTitle", false);
    final String title = FormsHelper.getTitle(resource, "Password");
    final String confirmTile = properties.get("confirmationTitle", "Confirm Password");
    final String width = properties.get("width", "");
    final String cols = properties.get("cols", "35");

    %><div class="form_row">
        <% LayoutHelper.printTitle(id, title, required, hideTitle, out); %>
        <div class="form_rightcol"><div class="form_rightcol_wrapper"><input class="geo textinput" id="<%= id %>" name="<%= name %>" value="" type="password" size="<%= cols %>" <%= (width.length() > 0 ? "style='width:" + width + "px;'" : "") %>></div></div>
    </div>
    <div class="form_row">
      <% LayoutHelper.printTitle(confirmId, confirmTile, required, hideTitle, out); %>
      <div class="form_rightcol"><div class="form_rightcol_wrapper"><input class="geo textinput" id="<%= confirmId %>" name="<%= name %>_confirm" value="" type="password" size="<%= cols %>" <%= (width.length()>0 ? "style='width:" + width + "px;'" : "") %>></div></div>
    </div><%

    LayoutHelper.printDescription(FormsHelper.getDescription(resource, ""), out);
    LayoutHelper.printErrors(slingRequest, name, out);
%>
