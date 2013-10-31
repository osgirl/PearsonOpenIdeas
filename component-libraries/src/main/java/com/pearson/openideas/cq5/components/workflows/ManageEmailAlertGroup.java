/**
 * Created: June 19, 2013 5:53:25 PM
 *
 * Copyright (c) 2000 - 2011, Crown Partners.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of
 * Crown Partners. ("Confidential Information").  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Crown Partners.
 */
package com.pearson.openideas.cq5.components.workflows;

import com.day.cq.workflow.WorkflowException;
import com.day.cq.workflow.WorkflowSession;
import com.day.cq.workflow.exec.WorkItem;
import com.day.cq.workflow.exec.WorkflowData;
import com.day.cq.workflow.exec.WorkflowProcess;
import com.day.cq.workflow.metadata.MetaDataMap;
import org.apache.commons.lang.StringUtils;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.jackrabbit.api.JackrabbitSession;
import org.apache.jackrabbit.api.security.user.Group;
import org.apache.jackrabbit.api.security.user.User;
import org.apache.jackrabbit.api.security.user.UserManager;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.jcr.api.SlingRepository;
import org.osgi.framework.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;

/**
 * Manage Email Alert Group Process.
 * 
 * @author Todd M Guerra
 * 
 * @version 2.0
 */
@Component
@Service
@Properties({
        @Property(name = Constants.SERVICE_DESCRIPTION, value = "Manage email alert group process implementation."),
        @Property(name = Constants.SERVICE_VENDOR, value = "Pearson Technology"),
        @Property(name = "process.label", value = "Manage Email Alert Group Process") })
public class ManageEmailAlertGroup implements WorkflowProcess {

    @Reference
    private ResourceResolverFactory resolverFactory;

    protected SlingRepository repository;

    private static final Logger LOG = LoggerFactory.getLogger(AddUserToUserGroupProcess.class);

    public void execute(final WorkItem item, final WorkflowSession session, final MetaDataMap args)
            throws WorkflowException {

        // Reading arguments from Process Component
        final String[] principals = args.get("principals", new String[] { "not set" });

        // Get workflow data
        final WorkflowData data = item.getWorkflowData();

        // Get payload data path
        final String payloadNodePath = (String) data.getPayload();
        LOG.debug("what is this? " + payloadNodePath);

        Node payloadNode = null;

        String property = "";
        boolean wantsEmail = false;

        try {
            ResourceResolver resourceResolver = resolverFactory.getAdministrativeResourceResolver(null);
            Resource resource = resourceResolver.getResource(payloadNodePath);
            Resource profileResource = resource.getChild("profile");
            ValueMap propMap = profileResource.adaptTo(ValueMap.class);
            property = propMap.get("permissionToEmail", "");
            if (StringUtils.isNotBlank(property)) {
                if (!property.equals("{empty}")) {
                    wantsEmail = true;
                }
                LOG.debug("property is: " + property);
            }
            LOG.debug("Do they want the emails? " + wantsEmail);
        } catch (LoginException e) {
            LOG.error("Error getting resource resolver", e);
        }

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

        try {
            // Cast WorkflowSession to JackrabbitSession
            adminSession = JackrabbitSession.class.cast(session.getSession());

            // Get work item node id which should be the user id
            final String userNodeName = payloadNode.getName();

            // Get user manager
            final UserManager um = adminSession.getUserManager();

            // Create user
            final User user = User.class.cast(um.getAuthorizable(userNodeName));

            // Loop user group and add them to the user
            for (int i = 0; i < principals.length; i++) {
                // Find group
                final Group group = Group.class.cast(um.getAuthorizable(principals[i]));

                if (wantsEmail && !group.isMember(user)) {
                    // Add user to group
                    group.addMember(user);
                    // Write log
                    LOG.info("Add user: " + user.getID() + " to user group:" + group.getID());
                } else if (group.isMember(user) && !wantsEmail) {
                    // remove the user
                    group.removeMember(user);
                    // Write log
                    LOG.info("Removing user: " + user.getID() + " from user group:" + group.getID());
                } else if (group.isMember(user) && wantsEmail) {
                    // Write log
                    LOG.info("User: " + user.getID() + " is and will remain a member of:" + group.getID());
                } else {
                    LOG.info("User isn't in the group and doesn't want to be.");
                }

            }

            adminSession.save();

        } catch (Exception e) {
            LOG.error(e.getMessage());
        } finally {
            // if (adminSession != null) {
            // adminSession.logout();
            // }
        }

    }
}