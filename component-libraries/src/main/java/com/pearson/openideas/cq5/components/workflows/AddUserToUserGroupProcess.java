package com.pearson.openideas.cq5.components.workflows;

import com.day.cq.workflow.WorkflowException;
import com.day.cq.workflow.WorkflowSession;
import com.day.cq.workflow.exec.WorkItem;
import com.day.cq.workflow.exec.WorkflowData;
import com.day.cq.workflow.exec.WorkflowProcess;
import com.day.cq.workflow.metadata.MetaDataMap;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Service;
import org.apache.jackrabbit.api.JackrabbitSession;
import org.apache.jackrabbit.api.security.user.Group;
import org.apache.jackrabbit.api.security.user.User;
import org.apache.jackrabbit.api.security.user.UserManager;
import org.apache.sling.jcr.api.SlingRepository;
import org.osgi.framework.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;

/**
 * Add user to user group workflow process
 */
@Component
@Service
@Properties({
        @Property(name = Constants.SERVICE_DESCRIPTION, value = "Add User to user group process implementation."),
        @Property(name = Constants.SERVICE_VENDOR, value = "Pearson Technology"),
        @Property(name = "process.label", value = "Add User to User Group Process")})
public class AddUserToUserGroupProcess implements WorkflowProcess {
 
	protected SlingRepository repository;
	
	private static final Logger LOG = LoggerFactory.getLogger(AddUserToUserGroupProcess.class);
 
    public void execute(final WorkItem item, final WorkflowSession session, final MetaDataMap args) throws WorkflowException {
    	
    	// Reading arguments from Process Component
    	final String[] principals = args.get("principals", new String[]{"not set"});
    	
    	// Get workflow data
    	final WorkflowData data = item.getWorkflowData();

    	// Get payload data path
    	final String payloadNodePath = (String)data.getPayload();
    	
    	Node payloadNode = null;
    	
    	// Get payload node path (should be user path)
    	try {
			payloadNode = (Node) session.getSession().getItem(payloadNodePath);
		} catch (PathNotFoundException e1) {
			// Write log
			return;
		} catch (RepositoryException e1) {
			// Write log
			return;
		}
    	
    	JackrabbitSession adminSession = null;   	
    	   
    	try 
    	{
    		// Cast WorkflowSession to JackrabbitSession 
    		adminSession = JackrabbitSession.class.cast(session.getSession());
    		
    		// Get work item node id which should be the user id
    		final String userNodeName = payloadNode.getName();
    		
	        // Get user manager
	        final UserManager um = adminSession.getUserManager();
	        
	    	// Create user
	        final User user = User.class.cast(um.getAuthorizable(userNodeName));

	    	
	    	// Loop user group and add them to the user
	        for (int i = 0; i < principals.length; i++)
	        {
	            // Find group
	        	final Group group = Group.class.cast(um.getAuthorizable(principals[i]));
	        	
	        	// Add user to group
	        	group.addMember(user);
	        	
	        	// Write log
	        	LOG.info("Add user: " + user.getID() + " to user group:" + group.getID());

	        }
	        
     	   adminSession.save();
	        
       } catch (Exception e) {
       	 LOG.error(e.getMessage());
       } finally {
    	  //if (adminSession != null) {
    		//  adminSession.logout();
    	  //}
       }

    }
}