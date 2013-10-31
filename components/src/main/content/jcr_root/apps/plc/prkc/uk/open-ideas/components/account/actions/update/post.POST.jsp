<%@page session="false" %><%
%><%@page import="java.io.UnsupportedEncodingException"%>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.UUID" %>
<%@ page import="javax.jcr.RepositoryException" %>
<%@ page import="javax.jcr.Session" %>
<%@ page import="javax.jcr.SimpleCredentials" %>
<%@ page import="javax.jcr.Node" %>
<%@ page import="org.apache.commons.codec.binary.Base64" %>
<%@ page import="org.apache.sling.api.request.RequestParameter" %>
<%@ page import="org.apache.sling.api.request.RequestParameterMap" %>
<%@ page import="org.apache.sling.api.resource.Resource" %>
<%@ page import="org.apache.sling.api.resource.ResourceUtil" %>
<%@ page import="org.apache.sling.api.resource.ValueMap" %>
<%@ page import="org.apache.sling.jcr.api.SlingRepository" %>
<%@ page import="com.day.cq.security.AccountManager" %>
<%@ page import="com.day.cq.security.AccountManagerFactory" %>
<%@ page import="com.day.cq.security.User" %>
<%@ page import="com.day.cq.wcm.foundation.forms.FormsHelper" %>
<%@ page import="java.io.InputStream" %>
<%@ page import="java.io.IOException" %>
<%@ page import="com.pearson.openideas.cq5.components.services.WorkflowService" %>
<%@ page import="com.pearson.openideas.cq5.components.utils.CookieUtils" %>
<%@ page import="com.pearson.openideas.cq5.components.utils.UserUtils" %>
<%@taglib prefix="sling" uri="http://sling.apache.org/taglibs/sling/1.0" %><%!

    // Constants added - Dan Chapman - 22/11/2012
    final String EMAIL = "email";

    //final String LOGIN = "rep:userId";
    final String PWD = "password";
    final String PWD_CONFIRM = PWD +"_confirm";
    final String CREATE = "cq:create";
    final String SUBMIT = "submit";
    
    final String PF_REP = "rep:";
    final String PF_CQ = "cq:";
    //final String EMAIL = PF_REP + "e-mail";
    final String MEMBER_OF = PF_REP + "memberOf";
    

    private boolean authenticate(SlingRepository repos, String auth, String pwd) {
        Session session = null;
        try {
            SimpleCredentials creds = new SimpleCredentials(auth, pwd.toCharArray());
            session = repos.login(creds);
            return true;
        } catch (RepositoryException e) {
            return false;
        } finally {
            if (session!=null) {
                session.logout();
            }
        }
    }

    private Map<String, RequestParameter[]> filterParameter(Iterator<Resource> itr, RequestParameterMap paras) {
        Map<String, RequestParameter[]> prefs = new HashMap<String, RequestParameter[]>();
        while(itr.hasNext()) {
            String name = FormsHelper.getParameterName(itr.next());
            if (!paras.containsKey(name)
                    || name.startsWith(SUBMIT)
                    || name.startsWith("_")
                    || name.startsWith(PWD)
                    || name.startsWith(PWD_CONFIRM))
                  {
                continue; // filter all rep, anc cq properties but save the email in the profile
            }
            prefs.put(name, paras.getValues(name));
        }
        return prefs;
    }

    private final class IntermediatePathParam implements RequestParameter {

        private final String intermediatePath;

        private IntermediatePathParam(String intermediatePath) {
            this.intermediatePath = intermediatePath;
        }

        public boolean isFormField() {
            return true;
        }

        public String getContentType() {
            return null;
        }

        public long getSize() {
            return intermediatePath.length();
        }

        public byte[] get() {
            return intermediatePath.getBytes();
        }

        public InputStream getInputStream() throws IOException {
            return null;
        }

        public String getFileName() {
            return null;
        }

        public String getString() {
            return intermediatePath;
        }

        public String getString(String s) throws UnsupportedEncodingException {
            return new String(intermediatePath.getBytes(s));
        }
    }

%><sling:defineObjects/><%
    final ValueMap properties = ResourceUtil.getValueMap(resource);
    final SlingRepository repos = sling.getService(SlingRepository.class);
    final AccountManagerFactory af = sling.getService(AccountManagerFactory.class);
    boolean create = false; 
    boolean login = false;
    Session session = null;
    String error = null;
    String auth = null;
    String pwd = null;
    try {
        session = repos.loginAdministrative(null);
      
        // Properties added - Dan Chapman - 22/11/2012
        // This property is not required now to make sure user id is unique as we are now
        // using UUID as user id instead of email address
        final String userIdPrefix = properties.get("userIdPrefix","");
        log.debug("userIdPrefix: " + userIdPrefix);
        
        final String group = properties.get("memberOf", "");
        log.debug("memberOf: " + group);
        String intermediatePath = properties.get("intermediatePath", null);
        log.debug("intermediatePath: " + intermediatePath);        
        final AccountManager am = af.createAccountManager(session);
        final String email = request.getParameter(EMAIL)==null? null : slingRequest.getRequestParameter(EMAIL).getString();
        log.debug("email: " + email);

        // Creating a random UUID (Universally unique identifier) for user id
        UUID uuid = UUID.randomUUID();
        auth = userIdPrefix + uuid.toString(); 
        log.debug("UUID: " + uuid); 

        pwd = request.getParameter(PWD)==null? null : slingRequest.getRequestParameter(PWD).getString();
        final String createPara = request.getParameter(CREATE)==null? null : slingRequest.getRequestParameter(CREATE).getString();
        final String pwdConfirm = request.getParameter(PWD_CONFIRM)==null? null : slingRequest.getRequestParameter(PWD_CONFIRM).getString();

        final boolean hasAuth = auth!=null && auth.length()>0;
        final boolean hasPwd = pwd!=null && pwd.length()>0;
        final boolean isCreate = createPara!=null && Boolean.valueOf(createPara);
        login = hasAuth && hasPwd;
        create = hasAuth &&((pwdConfirm!=null && pwdConfirm.length()>0) || (isCreate && hasPwd));
                
                  
        if (!(login || create)) {
            error = "Request incomplete no user-id or no password";
            log.debug(error);
        } else if (create) {
            if (!hasPwd) {
                pwd = pwdConfirm;
            }
        } else if (login) {
            if (!authenticate(repos, auth, pwd)) {
                error = "Error credentials do not authenticate";
            } 
        }

        // Add sub folders to intermediatePath to better filter users to prevent to many users being ceated
        // under a single node        
        if (intermediatePath != null && auth != null) {
            if (auth.length() >= 1) {
                intermediatePath  = intermediatePath + "/" + auth.substring(0,1);
            }
            if (auth.length() >= 2) {
                intermediatePath  = intermediatePath + "/" + auth.substring(1,2);
            }
            if (auth.length() >= 3) {
                intermediatePath  = intermediatePath + "/" + auth.substring(2,3);
            }
            if (auth.length() >= 4) {
                intermediatePath  = intermediatePath + "/" + auth.substring(3,4);
            }   
            if (auth.length() >= 5) {
                intermediatePath  = intermediatePath + "/" + auth.substring(4,5);
            }                   
        }    
     
        if (error==null) {
            Map<String, RequestParameter[]> userProps = filterParameter(FormsHelper.getFormElements(resource), slingRequest.getRequestParameterMap());
            // pass the intermediate path action as additional parameter (see bug #38146)
            if (intermediatePath != null) {
                userProps.put("rep:intermediatePath", new RequestParameter[] {new IntermediatePathParam(intermediatePath)});
            }
            try {
            
                // may fail when email cannot be send
                am.getOrCreateAccount(auth, pwd, group, userProps);
                
                // Add USERACTION to user node
                String userNodePath = "/home/users/" + intermediatePath + "/" + auth;  
                Node userNode = session.getNode(userNodePath);
                userNode.setProperty("USERACTION", "CREATE" );
                
                UserUtils userUtils = new UserUtils();
                userNode.setProperty("password", userUtils.encodePassword(pwd));
                                
                // Save session
                session.save();
                
                // Reverse replicate user
                WorkflowService workflowService = sling.getService(WorkflowService.class);
                workflowService.startWorkflow("publish", "/etc/workflow/models/reverse_replication/jcr:content/model", userNode.getPath());
                           
                           
            } catch (Exception e) {
                log.warn("error while creating account: " + e.getMessage());
            }
        }
    } catch (Exception e) {
        error = e.getMessage();
    } finally {
        if (session!=null) {
            session.logout();
        }
    }


    if (error!=null) {
        log.error(error);
    }

    if (error == null) {
    
        // Log user in automatically
        request.setAttribute("userid", auth);  
        request.setAttribute("password", pwd); 
        
        CookieUtils cookieUtils = new CookieUtils();  
        
        // Redirect to thank you page (set in form properties) or page specified as the return page
        if (request.getParameter("returnToPage") != null && cookieUtils.getCookieValue(request, "returnToPagePath") != null) {
            request.setAttribute("redirectTo", cookieUtils.getCookieValue(request, "returnToPagePath"));
        } else {
             request.setAttribute("redirectTo", properties.get("thankyouPage", ""));
        }
        slingRequest.getRequestDispatcher("/bin/open-ideas_security_check", null).forward(request, response);

           
    } else {

        // Get referer
        String path = request.getHeader("referer");
       
        // Remove existing query string from path
        if (path.indexOf("?") > -1) {
            path = path.substring(0, path.indexOf("?"));
        }   
        
        response.sendRedirect(path + "?message=registrationFailed");  
       
       
    }
     
    
%>