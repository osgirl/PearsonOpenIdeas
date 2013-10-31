package com.pearson.openideas.cq5.components.workflows;

import com.day.cq.workflow.WorkflowException;
import com.day.cq.workflow.WorkflowSession;
import com.day.cq.workflow.exec.WorkItem;
import com.day.cq.workflow.exec.WorkflowData;
import com.day.cq.workflow.exec.WorkflowProcess;
import com.day.cq.workflow.metadata.MetaDataMap;
import com.pearson.openideas.cq5.components.utils.UserUtils;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.ReferenceCardinality;
import org.apache.felix.scr.annotations.ReferencePolicy;
import org.apache.felix.scr.annotations.Service;
import org.apache.jackrabbit.api.JackrabbitSession;
import org.apache.jackrabbit.api.security.user.Authorizable;
import org.apache.jackrabbit.api.security.user.Group;
import org.apache.jackrabbit.api.security.user.User;
import org.apache.jackrabbit.api.security.user.UserManager;
import org.apache.sling.jcr.api.SlingRepository;
import org.osgi.framework.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Node;
import javax.jcr.PropertyIterator;
import javax.jcr.Session;
import javax.jcr.nodetype.ConstraintViolationException;
import java.security.Principal;

/**
 * Workflow process that processes a user message received from author
 */
@Component
@Service
@Properties({
        @Property(name = Constants.SERVICE_DESCRIPTION, value = "Process user message implementation."),
        @Property(name = Constants.SERVICE_VENDOR, value = "Pearson Technology - Dan Chapman (dan.chapman@pearson.com)"),
        @Property(name = "process.label", value = "Process User Message") })
public class ProcessUserMessageProcess implements WorkflowProcess {

    private static final Logger LOG = LoggerFactory.getLogger(ProcessUserMessageProcess.class);

    @Reference(policy = ReferencePolicy.DYNAMIC, cardinality = ReferenceCardinality.OPTIONAL_UNARY)
    protected SlingRepository repository;

    private static final String TYPE_JCR_PATH = "JCR_PATH";

    public void execute(final WorkItem item, final WorkflowSession session, final MetaDataMap args)
            throws WorkflowException {

        final UserUtils userUtils = new UserUtils();

        // Get workflow data
        final WorkflowData workflowData = item.getWorkflowData();

        Session adminSession = null;

        if (workflowData.getPayloadType().equals(TYPE_JCR_PATH)) {

            try {

                // Get payload path (this should be a user)
                final String payloadPath = workflowData.getPayload().toString();

                // Get admin session
                adminSession = repository.loginAdministrative(null);

                // Get message node
                final Node messageNode = adminSession.getNode(payloadPath);

                // Get message user profile properties
                final PropertyIterator messageUserProfileNode = messageNode.getNode("profile").getProperties();

                // / Get user id from message node
                final String userId = messageNode.getProperty("userId").getString();

                // Get password (encrypted) from message node
                String password = null;
                try {
                    password = messageNode.getProperty("password").getString();
                } catch (Exception e) {
                    LOG.error(e.getMessage());
                }

                // Get user manager
                final UserManager userManager = JackrabbitSession.class.cast(adminSession).getUserManager();

                // Find user (authorizable)
                final Authorizable authorizable = userManager.getAuthorizable(userId);

                Node userNode = null;
                boolean newUser = false;
                String userAction = "";
                if (authorizable == null) { // User does not exist
                    newUser = true;

                    // Intermediate path
                    String intermediatePath = com.pearson.openideas.cq5.components.utils.Constants.ENGLISH_DOT_COM_INTERMEDIATE_PATH;
                    for (int i = 0; i < 5; i++) { // i indexes each element successively.
                        intermediatePath += "/" + userId.substring(i, i + 1);
                    }

                    final Principal principal = new Principal() {
                        public String getName() {
                            return userId;
                        }
                    };

                    // Create user
                    final User user = userManager.createUser(userId, userUtils.decodePassword(password), principal,
                            intermediatePath);

                    // Add user to group
                    final Group group = Group.class
                            .cast(userManager
                                    .getAuthorizable(com.pearson.openideas.cq5.components.utils.Constants.ENGLISH_DOT_COM_USERS_GROUP));
                    if (group.isGroup()) {
                        group.addMember(user);
                    }

                    // Get user node
                    userNode = adminSession.getNode(user.getPath());

                    // Create profile node (if one does not exist)
                    if (userNode.hasNode("profile") == false) {
                        userNode.addNode("profile");
                    }

                    // Save session
                    adminSession.save();

                } else { // User exists

                    newUser = false;

                    // Get user
                    final User user = User.class.cast(authorizable);

                    // Get user node
                    userNode = adminSession.getNode(user.getPath());

                    if (userNode.hasProperty("USERACTION")) {
                        userAction = userNode.getProperty("USERACTION").getString();
                    }

                    // Create profile node (if one does not exist)
                    if (userNode.hasNode("profile") == false) {
                        userNode.addNode("profile");
                    }

                    if (userAction.equals("DONE")) {

                        // If password has changed, update password
                        if (password != null) {
                            user.changePassword(userUtils.decodePassword(password));
                        }

                        // If password has been requested to be reset, add reset properties to user node
                        try {
                            if (messageNode.hasProperty("RESETPASSWORDKEY")) {
                                userNode.setProperty("RESETPASSWORDKEY", messageNode.getProperty("RESETPASSWORDKEY")
                                        .getValue());
                                userNode.setProperty("RESETPASSWORDURL", messageNode.getProperty("RESETPASSWORDURL")
                                        .getValue());
                                userNode.setProperty("RESETPASSWORDATETIME",
                                        messageNode.getProperty("RESETPASSWORDATETIME").getValue());
                            } else {
                                final javax.jcr.Value nullValue = null;
                                userNode.setProperty("RESETPASSWORDKEY", nullValue);
                                userNode.setProperty("RESETPASSWORDURL", nullValue);
                                userNode.setProperty("RESETPASSWORDATETIME", nullValue);
                            }
                        } catch (Exception e) {
                            LOG.error(e.getMessage());
                        }

                    }

                }

                // Remove password property from message node
                if (password != null) {
                    final javax.jcr.Value nullValue = null;

                    // Remove plain text password from message node
                    messageNode.setProperty("password", nullValue);

                    // Remove plain text password from user node
                    userNode.setProperty("password", nullValue);
                }

                // Get user profile node
                final Node userProfileNode = userNode.getNode("profile");

                // Populate user properties for new user or publish user node that didn't instigate the update
                if (newUser || userAction.equals("DONE")) {
                    while (messageUserProfileNode.hasNext()) {

                        final javax.jcr.Property prop = (javax.jcr.Property) messageUserProfileNode.next();

                        try {
                            // Update user node from message profile node
                            if (prop.isMultiple()) {
                                userProfileNode.setProperty(prop.getName(), prop.getValues());
                            } else {
                                userProfileNode.setProperty(prop.getName(), prop.getValue());
                            }
                        } catch (ConstraintViolationException e) {
                            LOG.error(e.getMessage());
                        }

                    }
                }

                // Update user node to done when its a new user or for the user who has been modified
                if (newUser || userAction.equals("DONE") == false) {
                    userNode.setProperty("USERACTION", "DONE");
                }

                // Update message as processed
                messageNode.setProperty("MESSAGEACTION", "DONE");

                // Save session (commit changes to JCR)
                adminSession.save();

            } catch (Exception e) {
                LOG.error(e.getMessage());
                throw new WorkflowException(e.getMessage(), e);
            } finally {
                if (adminSession != null) {
                    adminSession.logout();
                }
            }

        }

    }

}
