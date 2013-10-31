package com.pearson.openideas.cq5.components.servlets;

import com.day.cq.workflow.WorkflowException;
import com.pearson.openideas.cq5.components.exceptions.DuplicateUserException;
import com.pearson.openideas.cq5.components.exceptions.UserNotFoundException;
import com.pearson.openideas.cq5.components.services.WorkflowService;
import com.pearson.openideas.cq5.components.utils.ApacheCookieUtils;
import com.pearson.openideas.cq5.components.utils.Constants;
import com.pearson.openideas.cq5.components.utils.UserUtils;
import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.felix.scr.annotations.*;
import org.apache.felix.scr.annotations.sling.SlingServlet;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ResourceUtil;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.jcr.api.SlingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.Calendar;

@SlingServlet(methods = { "POST" }, paths = { "/bin/open-ideas_security_check" }, // Must
// also
// be
// pt_security_check
extensions = { "html" })
@Properties({
        @Property(name = "service.pid", value = "com.pearson.openideas.cq5.components.servlets.SecurityCheckServlet", propertyPrivate = false),
        @Property(name = "service.description", value = "Pearson Open Ideas security check servlet", propertyPrivate = false),
        @Property(name = "service.vendor", value = "Pearson Technology - Dan Chapman (dan.chapman@pearson.com)", propertyPrivate = false) })
public class SecurityCheckServlet extends SlingAllMethodsServlet {
    private static final long serialVersionUID = 1L;

    private static final Logger LOG = LoggerFactory.getLogger(SecurityCheckServlet.class);

    private final boolean LOGIN_USE_SECURE_COOKIE = Constants.LOGIN_USE_SECURE_COOKIE;
    private final long MAX_LOGIN_ATTEMPTS = 5;

    @Reference(policy = ReferencePolicy.DYNAMIC, cardinality = ReferenceCardinality.OPTIONAL_UNARY)
    protected SlingRepository repository;

    @Reference
    private WorkflowService workflowService;

    private void eraseAuthenticationCookies(final SlingHttpServletRequest req, final SlingHttpServletResponse resp) {
        final javax.servlet.http.Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (int i = 0; i < cookies.length; i++) {
                if (cookies[i].getName().equals("login-token")) {
                    cookies[i].setValue("");
                    cookies[i].setMaxAge(0);
                }
                resp.addCookie(cookies[i]);
            }
        }
    }

    @Override
    protected void doPost(final SlingHttpServletRequest request, final SlingHttpServletResponse response)
            throws ServletException, IOException {

        // Clear all cookies
        eraseAuthenticationCookies(request, response);

        if (request.getAttribute("userid") == null) {

            // User is trying to authenticate though login form
            // Get form parameters
            final String email = request.getParameter("email");
            final String password = request.getParameter("password");
            final String resource = request.getParameter("resource");
            final String redirectTo = request.getParameter("redirectTo");
            final String errorPage = request.getParameter("errorPage").toString();
            final boolean returnToPage = request.getParameter("returnToPage").equals("true") ? true : false;

            // Get referer
            final String referer = request.getHeader("referer");

            // Get login component properties
            final ValueMap properties = ResourceUtil.getValueMap(request.getResourceResolver().getResource(resource));

            final String usersPath = properties.get("usersPath", "/home/users");
            final String defaultErrorMsg = properties.get("defaultErrorMessage", Constants.DEFAULT_LOGIN_ERROR_MESSAGE);
            final String duplicateUserErrorMsg = properties.get("duplicateUserErrorMessage", defaultErrorMsg);
            final String userNotFoundErrorMsg = properties.get("userNotFoundErrorMessage", defaultErrorMsg);
            final String authFailedErrorMsg = properties.get("authenticationFailedErrorMessage", defaultErrorMsg);

            String errorMessage = null;


            // Lookup user id
            Session session = null;
            String userId = null;
            String userName = "";
            Node profileNode = null;
            Node userNode = null;
            long failedAttempts = 0;

            try {
                session = repository.loginAdministrative(null);
                final UserUtils userUtils = new UserUtils();
                userId = userUtils.findUserId(session, usersPath, email);
                userName = userUtils.findUserName(session, usersPath, email);
                profileNode = userUtils.findUserProfileNode(session, usersPath, email);
                userNode = profileNode.getParent();
            } catch (DuplicateUserException e) {
                // Log duplicate user error
                errorMessage = duplicateUserErrorMsg;
            } catch (UserNotFoundException e) {
                // Log user not found error
                errorMessage = userNotFoundErrorMsg;
            } catch (Exception e) {
                // Log general error
                errorMessage = defaultErrorMsg;
            }

            try {
                // if we've got failed attempts we'll see if it happened in the last day or not
                if (profileNode.hasProperty("failedAttempts")) {
                    // pull the last failed attempt property
                    Calendar lastFailedAttemptDate = profileNode.getProperty("lastFailedAttempt").getDate();
                    LOG.debug("Last failed login: " + lastFailedAttemptDate.getTime());
                    // make an object for RIGHT NOW right now
                    Calendar today = Calendar.getInstance();
                    // add 24 hours to the last failed attempt to check if today is past that
                    lastFailedAttemptDate.add(Calendar.DAY_OF_MONTH, 1);
                    // if today is more than 24 hours past the last failed login date, we'll reset the failed attempts
                    // to zero
                    if (today.after(lastFailedAttemptDate)) {
                        LOG.debug("Clearing the failed attempts since it's been 24 hours since we last failed");
                        profileNode.setProperty("failedAttempts", 0);
                        userNode.setProperty("USERACTION", "MODIFY");
                        session.save();
                        //replicate the user node back to author so it's in sync, baby
                        workflowService.startWorkflow("publish", "/etc/workflow/models/reverse_replication/jcr:content/model", userNode.getPath());
                    }
                    failedAttempts = profileNode.getProperty("failedAttempts").getLong();
                }
            } catch (RepositoryException e) {
                LOG.error("Error getting failed attempts property", e);
            } catch (WorkflowException e) {
                LOG.error("Error starting workflow for user modified", e);
            }

            // check to see if we've got some yahoo trying to log in too many times
            if (failedAttempts > MAX_LOGIN_ATTEMPTS) {
                errorMessage = "You have exceeded login attempts allowed for the day. Either reset your password or wait one day";
            } else {
                // if they haven't failed login more than 5 times, let them try to log in.
                if (userId != null) {

                    try {
                        // Authenticate user
                        final HttpState state = authenticateUser(request, Constants.LOGIN_J_SECURITY_CHECK_PATH,
                                userId, password);
                        // Add cookies from result of HTTP post to response
                        final Cookie[] responseCookies = state.getCookies();
                        for (Cookie cookie : responseCookies) {
                            if (cookie.getName().equals("login-token")) {
                                LOG.debug("Setting the login token cookie");
                                cookie.setSecure(LOGIN_USE_SECURE_COOKIE);
                                cookie.setDomain(request.getServerName());

                                Cookie loginNameCookie = new Cookie();
                                loginNameCookie.setDomain(request.getServerName());
                                loginNameCookie.setSecure(LOGIN_USE_SECURE_COOKIE);
                                loginNameCookie.setName("login-name");
                                loginNameCookie.setValue(userName);
                                loginNameCookie.setExpiryDate(null);
                                loginNameCookie.setPath("/");

                                // now add both cookies
                                response.addCookie(ApacheCookieUtils.servletCookieFromApacheCookie(cookie));
                                response.addCookie(ApacheCookieUtils.servletCookieFromApacheCookie(loginNameCookie));
                                if (profileNode.hasProperty("failedAttempts")) {
                                    profileNode.setProperty("failedAttempts", 0);
                                    session.save();
                                }
                            }
                        }

                    } catch (Exception e) {
                        // Log authentication failed message
                        errorMessage = authFailedErrorMsg;
                        // Log error
                        LOG.error(e.getMessage());
                    }
                }
            }

            // Set path to redirect to, either the login page or specified redirect url
            String path = null;

            if (redirectTo != null && errorMessage == null) {
                path = redirectTo;
            } else {
                try {
                    // if we failed, we'll add to the failed attempts property. they get 5 AND THAT'S IT
                    LOG.debug("There have been " + failedAttempts + " failed attempts for this user before this one");
                    // no need to keep updating past the max login, as they login wasn't actually attempted
                    if (failedAttempts < MAX_LOGIN_ATTEMPTS + 1) {
                        profileNode.setProperty("failedAttempts", failedAttempts + 1);
                        profileNode.setProperty("lastFailedAttempt", Calendar.getInstance());
                        userNode.setProperty("USERACTION", "MODIFY");
                        session.save();
                    }

                    //and finally we'll reverse replicate the user so it's the same on all pub instances
                    workflowService.startWorkflow("publish", "/etc/workflow/models/reverse_replication/jcr:content/model", userNode.getPath());

                } catch (RepositoryException e) {
                    LOG.error("Error adding failed attempts to profile node", e);
                } catch (WorkflowException e) {
                    LOG.error("Error running the workflow", e);
                }
                if (errorPage == null) {
                    // Redirect back to login page if no redirect has been specified or there is an error
                    path = referer;
                } else {
                    path = errorPage;
                }
            }
            if (session != null) {
                session.logout();
            }

            // Remove existing query string from path
            if (path.indexOf("?") > -1) {
                path = path.substring(0, path.indexOf("?"));
            }

            // Add .html to end of path
            if (!path.endsWith(".html")) {
                path += ".html";
            }

            // Add error message and returntoPage to query string
            if (errorMessage != null) {
                path += "?returnToPage=" + (returnToPage == true ? "true" : "false");
                path += "&j_reason=" + java.net.URLEncoder.encode(errorMessage, "UTF-8");
            }

            // Redirect browser
            response.setStatus(200);
            response.sendRedirect(path);

        } else {
            // User has come from user registration form and need to automatically login
            final String userid = request.getAttribute("userid").toString();
            LOG.debug("user ID: " + userid);
            final String password = request.getAttribute("password").toString();
            String userName = request.getParameter("givenName").toString();
            LOG.debug("do we have a user name? " + userName);
            LOG.debug("password: " + password);
            String redirectTo = request.getAttribute("redirectTo").toString();
            LOG.debug("redirectTo: " + redirectTo);

            try {

                // Authenticate user
                final HttpState state = authenticateUser(request, Constants.LOGIN_J_SECURITY_CHECK_PATH, userid,
                        password);

                LOG.debug("state: " + state.toString());
                // Add cookies from result of HTTP post to response
                final Cookie[] responseCookies = state.getCookies();
                for (Cookie cookie : responseCookies) {
                    LOG.debug("Cookie info: " + cookie.getName() + ": " + cookie.getValue());
                    if (cookie.getName().equals("login-token")) {
                        LOG.debug("Cookie info: " + cookie.getName() + ": " + cookie.getValue());
                        cookie.setSecure(LOGIN_USE_SECURE_COOKIE);
                        cookie.setDomain(request.getServerName());
                        LOG.debug("domain? " + request.getServerName());
                        Cookie loginNameCookie = new Cookie();

                        // put the login name cookie, since we're assigning the login token
                        loginNameCookie.setDomain(request.getServerName());
                        loginNameCookie.setSecure(LOGIN_USE_SECURE_COOKIE);
                        loginNameCookie.setName("login-name");
                        loginNameCookie.setValue(userName);
                        loginNameCookie.setExpiryDate(null);
                        loginNameCookie.setPath("/");
                        response.addCookie(ApacheCookieUtils.servletCookieFromApacheCookie(cookie));
                        response.addCookie(ApacheCookieUtils.servletCookieFromApacheCookie(loginNameCookie));
                    }
                }

            } catch (Exception e) {
                // Log error
                LOG.error(e.getMessage());
            }

            if (!redirectTo.endsWith(".html")) {
                redirectTo += ".html";
            }

            // Redirect browser
            response.setStatus(200);
            response.sendRedirect(redirectTo);

        }

    }

    private HttpState authenticateUser(final SlingHttpServletRequest request, final String postUrl,
            final String userId, final String password) throws HttpException, IOException, Exception {

        // Create instance on HttpState
        final HttpState state = new HttpState();

        final HostConfiguration hostConfiguration = new HostConfiguration();
        hostConfiguration.setHost(request.getLocalAddr(), request.getLocalPort());
        LOG.debug("host config: " + hostConfiguration.toString());

        // Get HTTP client instance
        final HttpClient httpClient = new HttpClient();
        httpClient.setHostConfiguration(hostConfiguration);
        httpClient.setState(state);

        // Create HTTP Post method and execute it
        final PostMethod postMethod = new PostMethod(postUrl);
        postMethod.addParameter("j_username", userId);
        postMethod.addParameter("j_password", password);
        LOG.debug("post method: " + postMethod.getParameters());
        final int status = httpClient.executeMethod(postMethod);
        postMethod.releaseConnection();

        LOG.debug("status: " + status);
        if (status != 302) {
            LOG.error("Error logging in user: " + userId);
            throw new Exception("Authentication failed for user: " + userId);
        }

        return state;

    }

}
