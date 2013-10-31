package com.pearson.openideas.cq5.components.servlets;

import com.pearson.openideas.cq5.components.exceptions.UserNotFoundException;
import com.pearson.openideas.cq5.components.utils.Constants;
import com.pearson.openideas.cq5.components.utils.UserUtils;
import org.apache.felix.scr.annotations.*;
import org.apache.felix.scr.annotations.sling.SlingServlet;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.jcr.api.SlingRepository;
import org.json.JSONWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Session;
import javax.servlet.ServletException;
import java.io.IOException;

@SlingServlet(methods = { "GET" }, paths = { "/bin/open_ideas_lookup_user_id" }, extensions = { "json" })
@Properties({
        @Property(name = "service.pid", value = "com.pearson.openideas.cq5.components.servlets.LookupUserIdServlet", propertyPrivate = false),
        @Property(name = "service.description", value = "Pearson Open Ideas lookup user id servlet", propertyPrivate = false),
        @Property(name = "service.vendor", value = "Pearson Technology - Dan Chapman (dan.chapman@pearson.com)", propertyPrivate = false) })
public class LookupUserIdServlet extends SlingSafeMethodsServlet {
    private static final long serialVersionUID = 1L;

    @Reference(policy = ReferencePolicy.DYNAMIC, cardinality = ReferenceCardinality.OPTIONAL_UNARY)
    protected SlingRepository repository;

    private static final Logger LOG = LoggerFactory.getLogger(LookupUserIdServlet.class);

    @Override
    protected void doGet(final SlingHttpServletRequest request, final SlingHttpServletResponse response)
            throws ServletException, IOException {
        // Check request referer and deny access if not from login page for security
        final String referer = request.getHeader("REFERER") == null ? "" : request.getHeader("REFERER");
        final String serverName = request.getServerName() == null ? "" : request.getServerName();

        // Deny if referer does not match server the servlet is hosted on
        if (referer.indexOf("://" + serverName) == -1) {
            LOG.error("Lookup failed, invalid referer (" + referer + ", " + serverName + ")");
            return; // Exit method
        }

        Session adminSession = null;

        try {

            adminSession = repository.loginAdministrative(null);

            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");

            final JSONWriter w = new JSONWriter(response.getWriter());
            w.object();

            try {

                final String email = request.getParameter("email");

                // Need to think about how we are going to remove user path
                final UserUtils userUtils = new UserUtils();
                final String userId = userUtils.findUserId(adminSession, Constants.USERS_PATH, email);

                w.key("userId").value(userId);

            } catch (UserNotFoundException e1) {
                LOG.error(e1.getMessage());
            } catch (Exception e) {

                // log error
                LOG.error(e.getMessage());
            }

            w.endObject();

        } catch (Exception e) {
            // log error
            LOG.error(e.getMessage());
        } finally {
            if (adminSession != null) {
                adminSession.logout();
            }
        }
    }

}