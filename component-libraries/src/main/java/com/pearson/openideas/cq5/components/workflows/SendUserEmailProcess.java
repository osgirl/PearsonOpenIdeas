package com.pearson.openideas.cq5.components.workflows;

import com.day.cq.commons.mail.MailTemplate;
import com.day.cq.mailer.MailingException;
import com.day.cq.mailer.MessageGateway;
import com.day.cq.mailer.MessageGatewayService;
import com.day.cq.workflow.WorkflowException;
import com.day.cq.workflow.WorkflowSession;
import com.day.cq.workflow.exec.WorkItem;
import com.day.cq.workflow.exec.WorkflowData;
import com.day.cq.workflow.exec.WorkflowProcess;
import com.day.cq.workflow.metadata.MetaDataMap;
import org.apache.commons.lang.text.StrLookup;
import org.apache.commons.mail.HtmlEmail;
import org.apache.felix.scr.annotations.*;
import org.apache.jackrabbit.api.JackrabbitSession;
import org.apache.jackrabbit.api.security.user.User;
import org.apache.jackrabbit.api.security.user.UserManager;
import org.osgi.framework.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.PropertyIterator;
import javax.jcr.RepositoryException;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Send a user an email
 */
@Component
@Service
@Properties({ @Property(name = Constants.SERVICE_DESCRIPTION, value = "Send user and email"),
        @Property(name = Constants.SERVICE_VENDOR, value = "Pearson Technology"),
        @Property(name = "process.label", value = "Send User Email") })
public class SendUserEmailProcess implements WorkflowProcess {

    @Reference
    private MessageGatewayService messageGatewayService;

    private static final Logger LOG = LoggerFactory.getLogger(SendUserEmailProcess.class);

    public void execute(final WorkItem item, final WorkflowSession session, final MetaDataMap args)
            throws WorkflowException {

        // Get template path workflow argument
        final String templatePath = args.get("templatePath", "");

        // Get template workflow argument
        final String template = args.get("template", "");

        // Get workflow data
        final WorkflowData data = item.getWorkflowData();

        // Get payload data path
        final String payloadNodePath = (String) data.getPayload();

        Node payloadNode = null;

        // Get payload node path (should be user path)
        try {
            payloadNode = (Node) session.getSession().getItem(payloadNodePath);
        } catch (PathNotFoundException e1) {
            LOG.error("Path not found", e1);
            return;
        } catch (RepositoryException e1) {
            LOG.error("Repository Exception", e1);
            return;
        }

        JackrabbitSession adminSession = null;

        try {
            // Cast WorkflowSession to JackrabbitSession
            adminSession = JackrabbitSession.class.cast(session.getSession());

            String templateSourceText = "";

            if (templatePath.equals("") == false) {

                // Get email template based on template path passed as a argument
                // ***** MUST BE A TEXT FILE *****
                final Node templateNode = adminSession.getNode(templatePath + "/jcr:content");

                // Get source text from text file
                templateSourceText = templateNode.getProperty("jcr:data").getString();

            } else if (template.equals("") == false) {

                // Set template source text to the email template text passed as an argument
                templateSourceText = template;

            }

            // Get work item node id which should be the user id
            final String userNodeName = payloadNode.getName();

            // Get user manager
            final UserManager userManager = adminSession.getUserManager();

            // Get user
            final User user = User.class.cast(userManager.getAuthorizable(userNodeName));

            // Get full path to user node
            final String userPath = user.getPath();

            // Get user node
            final Node userNode = adminSession.getNode(userPath);

            // Get user profile node
            final Node userProfileNode = adminSession.getNode(userPath + "/profile");

            // Get all properties from user profile
            final PropertyIterator userProfileProps = userProfileNode.getProperties();

            final Map<String, String> propertiesMap = new HashMap<String, String>();

            // Replace property names in the template with values from the user profile
            // e.g. {familyName} would replace this with Smith
            while (userProfileProps.hasNext()) {

                // Get content item element
                final javax.jcr.Property prop = (javax.jcr.Property) userProfileProps.next();

                // Can only deal with single values, not multiple
                if (prop.isMultiple() == false) {

                    templateSourceText = templateSourceText.replace("${" + prop.getName() + "}", prop.getString());

                    propertiesMap.put(prop.getName(), prop.getString());

                }

            }

            // Replace changePasswordUrl with real change password url on user node
            try {

                // Set change password url
                String changePasswordUrl = userNode.getProperty("RESETPASSWORDURL").getString();
                changePasswordUrl += "?uid=" + userNodeName;
                changePasswordUrl += "&ky=" + userNode.getProperty("RESETPASSWORDKEY").getString();

                // Replace ${changePasswordUrl} in email template
                templateSourceText = templateSourceText.replace("${changePasswordUrl}", changePasswordUrl);

            } catch (Exception e) {
                LOG.error("error setting change password URL", e);
            }

            // Convert email template text (String) to input stream for email template
            final InputStream templateStream = new ByteArrayInputStream(templateSourceText.getBytes("UTF-8"));

            // Prepare mail template
            final MailTemplate mailTemplate = new MailTemplate(templateStream, "UTF-8");

            // Prepare html email
            final HtmlEmail email = mailTemplate.getEmail(StrLookup.mapLookup(propertiesMap), HtmlEmail.class);
            email.setCharset("UTF-8");

            try {

                // Send email
                final MessageGateway<HtmlEmail> messageGateway = messageGatewayService.getGateway(HtmlEmail.class);
                messageGateway.send(email);

            } catch (MailingException e1) {
                // Log error if email is not sent
                LOG.error("mailing exception has occurred here", e1);
            } catch (Exception e2) {
                // Log error
                LOG.error("General error has occurred here", e2);
            }

        } catch (Exception e) {
            LOG.error(e.getMessage());
        } finally {
            // if (adminSession != null) {
             //adminSession.logout();
             //}
            LOG.debug("Ending the send user email workflow");
        }

    }

}