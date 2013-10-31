package com.pearson.openideas.cq5.components.workflows;

import com.day.cq.commons.date.DateUtil;

import com.day.cq.workflow.WorkflowException;
import com.day.cq.workflow.WorkflowSession;
import com.day.cq.workflow.exec.WorkItem;
import com.day.cq.workflow.exec.WorkflowData;
import com.day.cq.workflow.exec.WorkflowProcess;
import com.day.cq.workflow.metadata.MetaDataMap;

import com.day.cq.replication.ReplicationActionType;
import com.day.cq.replication.ReplicationOptions;
import com.day.cq.replication.Replicator;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.ReferenceCardinality;
import org.apache.felix.scr.annotations.ReferencePolicy;
import org.apache.felix.scr.annotations.Service;

import org.apache.sling.jcr.api.SlingRepository;

import org.osgi.framework.Constants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
 
import javax.jcr.Node;
import javax.jcr.PropertyIterator;
import javax.jcr.Session;
import javax.jcr.nodetype.ConstraintViolationException;

import java.util.UUID;

/**
 * Workflow process that creates a user message to replicate to publish.
 */
@Component
@Service
@Properties({
        @Property(name = Constants.SERVICE_DESCRIPTION, value = "Create user message process implementation."),
        @Property(name = Constants.SERVICE_VENDOR, value = "Pearson Technology - Dan Chapman (dan.chapman@pearson.com)"),
        @Property(name = "process.label", value = "Create User Message")})
public class CreateUserMessageProcess implements WorkflowProcess {
 
   private static final Logger LOG = LoggerFactory.getLogger(CreateUserMessageProcess.class);
 
	@Reference (policy = ReferencePolicy.DYNAMIC, cardinality= ReferenceCardinality.OPTIONAL_UNARY)
	protected SlingRepository repository;	
   
	@Reference (policy = ReferencePolicy.DYNAMIC, cardinality= ReferenceCardinality.OPTIONAL_UNARY)
	protected Replicator replicator;	
	
	private static final String TYPE_JCR_PATH = "JCR_PATH";
   
    public void execute(final WorkItem item, final WorkflowSession session, final MetaDataMap args) throws WorkflowException {

        LOG.debug("In execute class");
        
    	final DateUtil dateUtil = new DateUtil();
    	
    	// Get workflow data
    	final WorkflowData workflowData = item.getWorkflowData();

        LOG.debug("payload type: " + workflowData.getPayloadType());
        if (workflowData.getPayloadType().equals(TYPE_JCR_PATH)) {


        	Session adminSession = null;
        	
        	try {
        	
	        	// Get admin session
	        	adminSession = repository.loginAdministrative(null);
	        	
	        	// Get payload path (this should be a user)
	        	final String payloadPath = workflowData.getPayload().toString();
                LOG.debug("payload path: " + payloadPath);
	        	
	        	// Get user node
	        	final Node userNode = (Node) adminSession.getItem(payloadPath);
                LOG.debug("We have userNode: " + userNode.getPath());
	        	//replicator.replicate(session.getSession(), ReplicationActionType.ACTIVATE, userNode.getPath());

	        	// Get message outbox node
	        	final Node messages = (Node) adminSession.getItem(com.pearson.openideas.cq5.components.utils.Constants.WORKFLOW_USER_MESSAGES_PATH);


	            LOG.debug("we've got the messages node");
	        	// Generate message id
	        	final UUID messageId = UUID.randomUUID();

                LOG.debug("creating the user message node");
	        	// Create user message node
	        	final Node messageNode =  messages.addNode(messageId.toString(), "nt:unstructured");
	        	
	        	// Get user id from user node
	        	final String userId = userNode.getName();

                LOG.debug("updating the user message node!");
	        	// Update create user message node
	        	messageNode.setProperty("userId", userId);
	        	messageNode.setProperty("MESSAGEACTION", "CREATED");
	        	messageNode.setProperty("messageCreated", dateUtil.getNow());
	        	
	        	// Get password from user node
                LOG.debug("Getting the user password for some reason?");
	        	String password = null;
	        	try {
		        	password = userNode.getProperty("password").getString();
		        	messageNode.setProperty("password", password);
	        	} catch (Exception e) {
	        		LOG.error("Error getting user password", e);
	        	}	        	
	        	
	        	try {
	        		if (userNode.hasProperty("RESETPASSWORDKEY")) {
			        	messageNode.setProperty("RESETPASSWORDKEY", userNode.getProperty("RESETPASSWORDKEY").getValue());
			        	messageNode.setProperty("RESETPASSWORDURL", userNode.getProperty("RESETPASSWORDURL").getValue());
			        	messageNode.setProperty("RESETPASSWORDATETIME", userNode.getProperty("RESETPASSWORDATETIME").getValue());
	        		}
	        	} catch (Exception e) {
	        		LOG.error("Error setting some properties", e);
	        	}	 	        	
	        	
	        	// Get user profile node
                LOG.debug("Getting the profile node");
	        	Node userProfileNode;
				userProfileNode = userNode.getNode("profile");
				
				// Get user profile properties
				final PropertyIterator userProfileProperties = userProfileNode.getProperties();
	        	
	        	// Create message outbox profile node
	        	Node messageProfileNode;
	        	messageProfileNode = messageNode.addNode("profile", "nt:unstructured");
	        	
		        while (userProfileProperties.hasNext()) {
		            
		        	final javax.jcr.Property prop = (javax.jcr.Property) userProfileProperties.next();
                    LOG.debug("Getting ready to set some profile node properties");
	
		        	// Update create user profile (message outbox) node
		        	try {
                        if (!prop.getName().equalsIgnoreCase("jcr:primaryType")) {
                            if (prop.isMultiple()) {
			        		messageProfileNode.setProperty(prop.getName(), prop.getValues());
			        	    } else {
			        		messageProfileNode.setProperty(prop.getName(), prop.getValue());
			        	    }
                        }
		        	} catch (ConstraintViolationException e) {
		        		LOG.error("error setting profile nodes", e);
		        	}
	
		        }     
		        
		        // Save session (commit changes to JCR)
		        adminSession.save();		        
	        
		        // Replicate message (to publish)
		        final ReplicationOptions opts = new ReplicationOptions();
                LOG.debug("We're now replicating the user back out to the publish instances!!!! ");
                LOG.debug("Specifically: " + messageProfileNode.getPath());

		        replicator.replicate(session.getSession(), ReplicationActionType.ACTIVATE, messageProfileNode.getPath(), opts); // Must activate profile node first
		        LOG.debug("And now: " + messageNode.getPath());
                replicator.replicate(session.getSession(), ReplicationActionType.ACTIVATE, messageNode.getPath(), opts);


				       		        		        
		        messageNode.setProperty("MESSAGEACTION", "SENT");
 
		        // Remove password property from message node 
        		if (password != null) {
        			final javax.jcr.Value nullValue = null;
        			
        			// Remove plain text password from message node
        			messageNode.setProperty("password", nullValue);
        			
        			// Remove plain text password from user node
        			userNode.setProperty("password", nullValue);
        		}	        
        		
		        // Delete message
		        messageNode.remove();        		
		        
		        // Save session (commit changes to JCR)
		        adminSession.save();
		        
        	} catch(Exception e) {
        		LOG.error("Error during something, look at the stack trace", e);
        	} finally {
        		if (adminSession != null) {
        			adminSession.logout();
        		}
        	}
        	
        }
    	
    }
}
