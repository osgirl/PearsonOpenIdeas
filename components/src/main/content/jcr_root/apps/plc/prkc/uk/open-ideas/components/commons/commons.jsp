<%@page session="false" import="com.day.cq.i18n.I18n,
                    com.day.cq.commons.Externalizer,
                    com.day.cq.commons.date.RelativeTimeFormat,
                    com.day.cq.commons.jcr.JcrUtil,
                    com.day.cq.wcm.api.WCMMode,
                    com.adobe.cq.social.commons.CollabUtil,
                    com.adobe.cq.social.commons.CollabUser,
                    com.adobe.granite.xss.XSSFilter,
                    org.apache.sling.api.request.RequestParameter,
                    java.text.SimpleDateFormat,
                    java.util.Locale,
                    java.util.ResourceBundle,
                    org.apache.commons.lang3.StringEscapeUtils,
                    org.apache.commons.lang3.StringUtils,
                    org.apache.commons.lang3.ArrayUtils,
                    org.apache.sling.jcr.base.AbstractSlingRepository,
                    com.adobe.cq.social.commons.Comment,
                    com.adobe.cq.social.commons.CommentSystem,
                    com.adobe.granite.security.user.UserProperties"%><%
%><%@include file="/apps/plc/prkc/uk/open-ideas/global.jsp"%><%
    if(currentPage.getPath().contains(CommentSystem.PATH_UGC)) {
        final Comment comment = resource.adaptTo(Comment.class);
        if(null != comment) {
            currentPage = pageManager.getContainingPage(resourceResolver.getResource(comment.getCommentSystem().getPath()));
        }
    }

    final Locale pageLocale = currentPage.getLanguage(false);
    final ResourceBundle resourceBundle = slingRequest.getResourceBundle(pageLocale);
    final I18n i18n = new I18n(resourceBundle);
    final SimpleDateFormat localizedDateFormatter = new SimpleDateFormat(i18n.get("EEE MMM dd hh:mm:ss z yyyy"), pageLocale);
    final Externalizer externalizer = sling.getService(Externalizer.class);
    final String defaultAvatar = externalizer.absoluteLink(slingRequest,
                                                           slingRequest.getScheme(),
                                                           CollabUtil.DEFAULT_AVATAR);
    final RelativeTimeFormat fmt = new RelativeTimeFormat("r", resourceBundle);
    final WCMMode wcmMode = WCMMode.fromRequest(slingRequest);
    final String absoluteDefaultAvatar = externalizer.absoluteLink(slingRequest, slingRequest.getScheme(), CollabUtil.DEFAULT_AVATAR);
    final RequestParameter fromParam = slingRequest.getRequestParameter("from");
    final RequestParameter countParam = slingRequest.getRequestParameter("count");
    final Boolean isPageRequest = (null != fromParam);
    final String localURL = externalizer.externalLink(slingRequest.getResourceResolver(), (wcmMode == WCMMode.DISABLED)?Externalizer.PUBLISH:Externalizer.AUTHOR,"");
    Boolean isCORS = false;
    if(!(localURL.equals(request.getHeader("Origin")))){
        isCORS = true;
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
        response.setHeader("Access-Control-Allow-Headers", "CONTENT-TYPE, LOCATION, *");
    }

    if (wcmMode == WCMMode.EDIT) {
        %><cq:includeClientLib categories="cq.social.author" /><%
    }

    String resourceAuthorName = null;
    String resourceAuthorID = null;
    String resourceAuthorAvatar = null;
    String resourceAuthorPath = null;
    boolean resourceAuthorIsAnonymous = true;
    if (resource.adaptTo(Comment.class) != null) {
        final Comment comment = resource.adaptTo(Comment.class);
        
        final CollabUser author = resource.adaptTo(Comment.class).getAuthor();
        
        

        final ValueMap map = resource.adaptTo(ValueMap.class);
        
        resourceAuthorID = map.get("userIdentifier", String.class);

        final UserProperties userProperties = CollabUtil.getUserProperties(resourceResolver,
            resourceAuthorID);
        if(userProperties != null){
            resourceAuthorName = userProperties.getDisplayName();

            if(StringUtils.isBlank(resourceAuthorName)){
                resourceAuthorName = resourceAuthorID;
            }
            resourceAuthorPath = userProperties.getNode().getPath();
            //TODO: use a centralized place to determine anonymous, to avoid hardcoded "anonymous",
            //cannot use it yet due to soco jspc config issues with snapshot dependencies :(
            //resourceAuthorIsAnonymous = UserPropertiesUtil.isAnonymous(userProperties);
            resourceAuthorIsAnonymous = resourceAuthorID.equals("anonymous");
        }
        else{
            resourceAuthorName = author.getName();
        }
        resourceAuthorAvatar = CollabUtil.getAvatar(userProperties, author.getEmail(), absoluteDefaultAvatar);
    }
    final UserProperties loggedInUserProperties = slingRequest.adaptTo(UserProperties.class);
    final String loggedInUserID = (loggedInUserProperties == null) ? null : loggedInUserProperties.getAuthorizableID();
	final String loggedInUserName = (loggedInUserProperties == null) ? null : (loggedInUserProperties.getDisplayName() == null)? loggedInUserID : loggedInUserProperties.getDisplayName();
	final String loggedInUserEmail = (loggedInUserProperties == null) ? "" : (loggedInUserProperties.getProperty("email") == null)? "" : loggedInUserProperties.getProperty("email");
	final Boolean isAnonymous = loggedInUserName.equals(AbstractSlingRepository.DEFAULT_ANONYMOUS_USER);
	final Boolean isAdmin = loggedInUserID.equals(AbstractSlingRepository.DEFAULT_ADMIN_USER);
	final String socialProfilePage = (String)request.getAttribute(CollabUtil.REQ_ATTR_SOCIAL_PROFILE_PAGE);
	final String socialProfileUrl = socialProfilePage != null && resourceAuthorPath != null && !resourceAuthorIsAnonymous ? resourceAuthorPath  + ".form.html" + socialProfilePage : "##";
%>