<%--

 ADOBE CONFIDENTIAL
 __________________

  Copyright 2012 Adobe Systems Incorporated
  All Rights Reserved.

 NOTICE:  All information contained herein is, and remains
 the property of Adobe Systems Incorporated and its suppliers,
 if any.  The intellectual and technical concepts contained
 herein are proprietary to Adobe Systems Incorporated and its
 suppliers and are protected by trade secret or copyright law.
 Dissemination of this information or reproduction of this material
 is strictly forbidden unless prior written permission is obtained
 from Adobe Systems Incorporated.

   ==============================================================================

  Comments component sub-script

  Draws a form for new comments.

--%><%@ page session="false" import="com.adobe.cq.social.commons.CollabUser,
                     com.adobe.cq.social.commons.Comment,
                     com.adobe.cq.social.commons.CommentSystem,
                     com.day.cq.i18n.I18n,
                     com.day.cq.wcm.api.WCMMode,
                     com.day.cq.wcm.foundation.forms.FormsHelper,
                     java.util.List,
                     java.util.Locale,
                     java.util.ResourceBundle,
                     org.apache.sling.api.resource.ResourceUtil" %><%
%><%@taglib uri="http://www.day.com/taglibs/cq/personalization/1.0" prefix="personalization" %><%
%><%@include file="/apps/plc/prkc/uk/open-ideas/components/commons/commons.jsp"%><%
    
    final String targetResourceType = resourceResolver.resolve(resource.getPath()).getResourceType();
    CommentSystem cs = resource.adaptTo(CommentSystem.class);
    
    boolean isModerated = cs.getProperty("moderateComments", false);
    String defaultMessage = cs.getProperty("defaultMessage", i18n.get("Type your comment here."));
    String id = cs.getId();

    Resource commentResource = resource;
    if (cs == null) {
        // form called directly
        final List<Resource> editResources = FormsHelper.getFormEditResources(slingRequest);
        if (null != editResources && editResources.size() > 0) {
            cs = editResources.get(0).adaptTo(CommentSystem.class);
            commentResource = (null != cs) ? cs.getResource() : commentResource;
        } else {
            cs = resource.adaptTo(CommentSystem.class);
        }
    }
    if (cs == null) {
        log.error("failed to retrieve comment system for " + resource.getPath());
        return;
    }

    

    if (request.getParameter("idPrefix") != null) {
        id = xssAPI.getValidJSToken(request.getParameter("idPrefix"), "XSS");
    }

// In nested replies, when there are muliple composer from the same comment system, the taglib:
// contextProfileHtmlInput fails to select the right thing because it only takes in an ID so,
// the page must give us how many composers are already on the page to make the ID unique.
    String composerCount = request.getParameter("composerCount");
    if (composerCount != null) {
        id += "_" + composerCount;
    }

//    String loginId = id + "-login";
    String formId = id + "-form";
    String textId = id +  "-text";
    String nameId = id + "-" + CollabUser.PROP_NAME;
	String userName = id + "-username";
	String mailId = id +"-" + CollabUser.PROP_EMAIL;
    String formAction = commentResource.getPath() + ".social.createcomment.html";

/*     if(isRenderByForm){
        formAction = (String) request.getAttribute("commentsRoot") + ".createcomment.html";
    } */

    //ValueMap props = commentResource.adaptTo(ValueMap.class);
    //boolean requireLogin = props.get("requireLogin", Boolean.FALSE);
    boolean requireLogin = false;

//    boolean isDisabled = WCMMode.fromRequest(request).equals(WCMMode.DISABLED);
	String authorizableId = loggedInUserID;
	boolean anonymous = isAnonymous;

    String commentBlockClass = "comment-block-author";
    if(anonymous){
        commentBlockClass = "comment-block-publish";
    }
    %>

<script type="text/javascript">
    function validateSubmitWrapper<%= formId.replaceAll("[^a-zA-Z0-9]","") %>(id) {
        var validationResult = CQ_collab_comments_validateSubmit(id);
        if (validationResult && CQ_Analytics.Sitecatalyst){
            CQ_Analytics.record({event: 'postComment',
                                values: {commenterName: document.getElementById("<%= nameId %>").value},
                                componentPath: '<%=resource.getResourceType()%>'
                              });
        }
        return <% if (WCMMode.fromRequest(request).equals(WCMMode.DISABLED)) { %>validationResult<% } else { %>(CQ_Analytics.ClickstreamcloudUI.isVisible() ? false : validationResult)<% } %>;
    }
    function recordPostCommentEvent(name, user, component, category) {
        CQ_Analytics.record({event: 'postComment',
                                values: {commenterName: user,
                                         topic: name,
                                         category: category
                                        },
                                componentPath: component
                              });
    }
</script>
<form
    class="comment"
    action="<%= formAction %>"
    onsubmit="return CQ.soco.comments.validateCommentForm(this)"
    method="POST"
    name="comment"
    enctype="multipart/form-data"
    id="<%= formId %>">
    <script type="text/javascript" charset="utf-8">
        CQ_collab_comments_defaultMessage = "<%= defaultMessage.replaceAll("\\\"", "\\\\\"") %>";
        CQ_collab_comments_requireLogin = <%= requireLogin %>;
        CQ_collab_comments_enterComment = "<%= i18n.get("Please enter a comment") %>";
        CQ_collab_comments_commentActivated = "<%= i18n.get("Comment activated") %>";
        CQ_collab_comments_unableToActivate = "<%= i18n.get("Unable to activate comment") %>";
    </script>
    <div class="comment-error" id="<%= id %>-error"></div>
    <label for="<%= textId %>" class="comment-text-label"><%= i18n.get("Comment") %></label>
    <textarea
            name="<%= Comment.PROP_MESSAGE %>"
            id="<%= textId %>"
            class="comment-text"
            onfocus="CQ.soco.commons.handleOnFocus(this, '<%= defaultMessage%>');"
            onblur="CQ.soco.commons.handleOnBlur(this, '<%= defaultMessage%>');"
            ><%= defaultMessage %></textarea>
    <div class="comment-info">
        <div>
            <div class="<%= commentBlockClass %>" id="<%= nameId %>-comment-block" >
                <span class="comment-error" id="<%= nameId %>-displayName-error"></span>
                <label for="<%= nameId %>" class="comment-text-label"><%= i18n.get("Name", "Label for commenter's name") %></label>
                <% 
                String tooltip = "<strong>Post as anonymous</strong><br />Simply 'Sign Up' or 'Log In' to leave your comment with your identity. ";
                if(!isAnonymous){
                	tooltip = "<strong>Post as " + loggedInUserName + "</strong><br />Simply 'Log Out' to leave your comment anonymously.";
                }
                %>
                <input id="<%= nameId %>" class="comment-text" type="hidden" name="<%= CollabUser.PROP_NAME %>" value="<%= loggedInUserID %>" propertyName="authorizableId" />
                <input id="<%= userName %>" class="comment-text" type="hidden" value="<%= loggedInUserName %>" />
                <a href="#" class="tooltip">Post as <%= loggedInUserName %><span><img class="callout" src="/etc/designs/plc/prkc/uk/open-ideas/clientlibs/img/callout.gif" /><%= tooltip %></span></a>
            </div>
			<div class="<%= commentBlockClass %>" id="<%= mailId %>-comment-block" >
                <span class="comment-error" id="<%= mailId %>-email-error"></span>
                <label for="<%= mailId %>" class="comment-text-label"><%= i18n.get("Email", "Label for commenter's email") %></label>
                <input id="<%= mailId %>" class="comment-text" type="hidden" name="<%= CollabUser.PROP_EMAIL %>" value="<%= loggedInUserEmail %>" propertyName="email"/>
            </div>
            <div class="submit-block">
                <input type="hidden" name="_charset_" value="<%= response.getCharacterEncoding() %>"/>
                <input class="pearson-button-dark" type="submit" name="submit" id="<%= id %>-submit" value="<%= i18n.get("Post Comment", "Form submit action button") %>"
                onclick="<% if(isModerated) {%>alert('Please be aware that all comments are moderated prior\nto being published on this website. Your comment has been\nsubmitted for approval.'); <%}%>recordPostCommentEvent('<%= commentResource.getPath() %>','<%= authorizableId %>','social/commons/components/composer' , 'forum')"/>
	        </div>
        </div>
    </div>

</form>

<script type="text/javascript">
    $CQ(function(){
        CQ.soco.commons.fillInputFromClientContext($CQ("<%=formId%> #XSS-userIdentifier"), "<%= CollabUser.PROP_NAME %>");
    });
</script>