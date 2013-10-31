<%@page session="false" %><%
%><%@page import="java.util.*,
                  org.apache.sling.api.resource.Resource,
                  org.apache.sling.api.request.RequestParameter,
                  org.apache.sling.api.request.RequestParameterMap,
                  com.day.cq.wcm.foundation.forms.FormsHelper,
                  org.apache.sling.jcr.api.SlingRepository,
                  javax.jcr.Session,
                  javax.jcr.Node,
                  javax.jcr.PropertyIterator,
                  javax.jcr.nodetype.ConstraintViolationException,
                  com.day.cq.security.profile.Profile,
                  com.day.cq.security.AccountManager,
                  com.day.cq.security.AccountManagerFactory,
                  com.day.cq.security.User,
                  java.io.InputStream,
                  java.io.IOException,
                  java.io.UnsupportedEncodingException,
                  com.pearson.openideas.cq5.components.services.WorkflowService,
                  com.pearson.openideas.cq5.components.utils.CookieUtils"
                  %><%
%><%@taglib prefix="sling" uri="http://sling.apache.org/taglibs/sling/1.0" %><%
%><sling:defineObjects/>
<%!

    final String PWD = "password";
    final String PWD_CONFIRM = PWD +"_confirm";
    final String SUBMIT = "submit";

    private Map<String, RequestParameter[]> filterParameter(Iterator<Resource> itr, RequestParameterMap paras) {
        Map<String, RequestParameter[]> prefs = new HashMap<String, RequestParameter[]>();
        while(itr.hasNext()) {
            String name = FormsHelper.getParameterName(itr.next());
            if (name.startsWith(SUBMIT)
                    || name.startsWith("_")
                    || name.startsWith(PWD)
                    || name.startsWith(PWD_CONFIRM))
                  {
                continue; // filter all rep, anc cq properties but save the email in the profile
            }
            if (!paras.containsKey(name)) {
                prefs.put(name, new RequestParameter[] {new EmptyRequestParameter()} );
            } else {
                prefs.put(name, paras.getValues(name));      
            }
        }
        return prefs;
    }
    
    private final class EmptyRequestParameter implements RequestParameter {

        private final String emptyString;

        private EmptyRequestParameter() {
            this.emptyString = "{empty}";
        }

        public boolean isFormField() {
            return true;
        }

        public String getContentType() {
            return null;
        }

        public long getSize() {
            return emptyString.length();
        }

        public byte[] get() {
            return emptyString.getBytes();
        }

        public InputStream getInputStream() throws IOException {
            return null;
        }

        public String getFileName() {
            return null;
        }

        public String getString() {
            return emptyString;
        }

        public String getString(String s) throws UnsupportedEncodingException {
            return new String(emptyString.getBytes(s));
        }
    }
    
%>

<%

    // Get sling respository service
    final SlingRepository repos = sling.getService(SlingRepository.class);
    
    // Get admin session
    Session session = null;
    session = repos.loginAdministrative(null);
   
    // Get current logged in profile
    final Profile currentProfile = slingRequest.adaptTo(Profile.class);
    
    final AccountManagerFactory af = sling.getService(AccountManagerFactory.class);
    final AccountManager am = af.createAccountManager(session);
    final User user = am.findAccount(currentProfile.getAuthorizable().getID());
    
    
    // Change any properties that are either single for multiple
    Map<String, RequestParameter[]> profileProps = filterParameter(FormsHelper.getFormElements(resource), slingRequest.getRequestParameterMap());   

    for (String key : profileProps.keySet()) {
        out.write(key + "<br>");
    }

    am.updateAccount(user, profileProps);

    // Get user node
    Node userNode = session.getNode(currentProfile.getAuthorizable().getHomePath());

    // Set USERACTION on user node to show user has been modified
    userNode.setProperty("USERACTION", "MODIFY");

    // Save session
    session.save();

    // Reverse replicate user to authors
    WorkflowService workflowService = sling.getService(WorkflowService.class);
	workflowService.startWorkflow("publish", "/etc/workflow/models/reverse_replication/jcr:content/model", userNode.getPath());

    // Session logout
    session.logout();             
     
    // Remove user cookie so any change of name will be shown in userinfo component
    CookieUtils cookieUtils = new CookieUtils();
    cookieUtils.removeCookie("SessionPersistence-publish", request, response);

    response.setStatus(200);
    response.sendRedirect(slingRequest.getResourceResolver().map(slingRequest, "/content/plc/prkc/uk/open-ideas/en/profile.html?message=profileUpdated"));

     
%>