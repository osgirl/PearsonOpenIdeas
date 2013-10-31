package com.pearson.openideas.cq5.components.servlets;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import org.apache.felix.scr.annotations.Property;
 
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
 
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
 
import javax.jcr.Session;
 
@Component(metatype=false)
@Service
public class AuthcheckerServlet extends SlingSafeMethodsServlet {
     
	private static final long serialVersionUID = 1L;

	@Property(value="/bin/permissioncheckstudents/html")
	private static final String SERVLET_PATH="sling.servlet.paths";
     
	private static final Logger LOG = LoggerFactory.getLogger(AuthcheckerServlet.class);
     
    public void doHead(final SlingHttpServletRequest request, final SlingHttpServletResponse response) {

    	Session session = null;
    	
    	try{
            //retrieve the requested URL
    		final String uri = request.getParameter("uri");
            //obtain the session from the request
            session = request.getResourceResolver().adaptTo(Session.class);
            //perform the permissions check
            try {
                session.checkPermission(uri, Session.ACTION_READ);
                LOG.info("authchecker says OK");
                response.setStatus(SlingHttpServletResponse.SC_OK);
            } catch(Exception e) {
            	LOG.info("authchecker says READ access DENIED!");
                response.setStatus(SlingHttpServletResponse.SC_FORBIDDEN);
            }
        }catch(Exception e){
        	LOG.error("authchecker servlet exception: " + e.getMessage());
        } finally {
        	if (session != null) {
        		session.logout();
        	}
        }
    }
}