<%@ page import="java.text.DateFormat,
                     com.adobe.cq.social.commons.Comment,
                     com.adobe.cq.social.commons.CommentSystem,
                     com.adobe.cq.social.commons.CollabUser,
                     com.day.cq.commons.date.DateUtil,
                     java.util.Locale,
                     com.adobe.cq.social.commons.CollabUtil,
                     com.day.cq.wcm.api.WCMMode,
					org.apache.sling.jcr.api.SlingRepository,
	 				org.apache.jackrabbit.api.JackrabbitSession,
	                org.apache.jackrabbit.api.security.user.User,
	                org.apache.jackrabbit.api.security.user.UserManager,
					 javax.jcr.Session" %><%
%><%@include file="/apps/plc/prkc/uk/open-ideas/components/commons/commons.jsp"%><%


	SlingRepository repository = sling.getService(SlingRepository.class);
	Session adminSession = repository.loginAdministrative(null);

    final JackrabbitSession jrSession = JackrabbitSession.class.cast(adminSession);
    final UserManager um = jrSession.getUserManager();

    // Get user
    User user = null;
    Node userNode = null;
    if (resourceAuthorID != null) {

        // Get user from user id supplier
        user = User.class.cast(um.getAuthorizable(resourceAuthorID));

        // Get user properties
        if (user != null) {
            userNode = jrSession.getNode(user.getPath() + "/profile"); 
        }
    }

	//resolve name
    String authorName = resourceAuthorName;
    String authorImg = resourceAuthorAvatar;

	if (userNode != null) {
		String givenName = "";
        String familyName = "";
        int nameCounter = 0;
        if (userNode.hasProperty("givenName")) {
	        givenName = userNode.getProperty("givenName").getValue().getString();
	        nameCounter++;
        }
        if (userNode.hasProperty("familyName")) {
	        familyName = userNode.getProperty("familyName").getValue().getString();
	        nameCounter++;
        }
        if(nameCounter >= 1) {
	        authorName = givenName + " " + familyName;
        }
    }

    final Comment comment = resource.adaptTo(Comment.class);
    final CommentSystem cs = comment.getCommentSystem();

    String message = comment.getMessage();
    // #26898 - line breaks in comments
    // xss protection will remove line breaks before we can format them
    // for the output, so it is done here a a workaround:
    // first remove the CR, then replace the LF with <BR>
    message = message.replaceAll("\\r", "");
    message = message.replaceAll("\\n", "<br>");
    message = "<div class=\"message\">" + message + "</div>";

    String id = comment.getId();

	//resolve date
    DateFormat dateFormat = DateUtil.getDateFormat(cs.getProperty("dateFormat", String.class), DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT, pageLocale), pageLocale);
    String date = dateFormat.format(comment.getDate());

    %>
        
    <div class="info-column">
        <div class="text-heading">
            <span><span class="username"><%= xssAPI.encodeForHTML(authorName) %></span>
            <span><%= xssAPI.encodeForHTML(date) %></span><%    
        
            %><cq:include script="modtag-template"/>
            <div id="<%=xssAPI.encodeForHTMLAttr(comment.getId())%>" class="comment-body"><%
                %><%= xssAPI.filterHTML(message) %>
                <sling:include resourceType="plc/prkc/uk/open-ideas/components/commons/comments/comment" path="." replaceSelectors="attachments"/>
                <%
            %><cq:include script="toolbar-template.jsp"/></div><%
        
            if (!cs.isClosed()) {
                %><cq:include script="replies.jsp"/><%
            }
        %></div>
    </div><%

    if (WCMMode.fromRequest(request) == WCMMode.EDIT) {
        %><script type="text/javascript">
            CQ.WCM.onEditableReady("<%= comment.getPath() %>",
                function(editable) {
                    editable.refreshCommentSystem = function() {
	                    CQ.wcm.EditBase.refreshPage();
                    };
                }
            );
        </script><%
    }
%>
