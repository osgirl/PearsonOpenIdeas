/**
 * Created: June 03, 2013 8:40:00 PM
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

package com.pearson.openideas.cq5.components.form;

import com.crownpartners.cq.quickstart.core.component.AbstractComponent;
import com.day.cq.mailer.MessageGateway;
import com.day.cq.mailer.MessageGatewayService;
import com.day.cq.wcm.foundation.forms.FormsConstants;
import com.pearson.openideas.cq5.components.services.PropertiesService;
import com.pearson.openideas.cq5.components.utils.UrlUtils;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.apache.felix.scr.annotations.Reference;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.jsp.PageContext;

/**
 * This class handles the form action for sending contribute contact us email.
 * 
 * @author Todd Guerra/Mandeep Singh
 * 
 * @version 2.0
 */
public class SendContributorEmail extends AbstractComponent {

    private static final Logger log = LoggerFactory.getLogger(SendContributorEmail.class);

    @Reference
    private MessageGatewayService messageGatewayService;

    private static final String NEW_LINE_CHAR = "<br/>";

    private String redirectSuccessPath;
    private String redirectErrorPath;

    private String headerLogoUrl;
    private String footerLogoLeftUrl;
    private String footerLogoRightUrl;

    private String name;
    private String userEmail;
    private String subject;
    private String comments;

    private String toEmail;
    private String emailSubject;

    private MessageGateway<SimpleEmail> mailMessageGateway;

    /**
     * Constructor. Sets the page context.
     * 
     * @param pageContext
     *            the page context to set.
     */
    public SendContributorEmail(final PageContext pageContext) {
        super(pageContext);
    }

    /**
     * {@inheritDoc}
     */
    public void init() {
        log.debug("Creating Contact Us Email");

        // get the redirect and error path for after we send the email
        redirectSuccessPath = getSlingRequest().getParameter(":redirect");
        redirectErrorPath = UrlUtils.getPageUrl(getCurrentPage(), getSlingScriptHelper(), getSlingRequest());

        // get the form resource so we can pull the email properties
        String formStartResourcePath = getSlingRequest().getParameter(FormsConstants.REQUEST_PROPERTY_FORM_START);
        Resource formStartResource = getResourceResolver().getResource(formStartResourcePath);
        ValueMap formStartProps = formStartResource.adaptTo(ValueMap.class);

        // get the aforementioned email properties
        toEmail = formStartProps.get("toEmail", "");
        log.debug("email: " + toEmail);
        emailSubject = formStartProps.get("emailSubject", "Open Ideas Reader Comment");

        // get what the user entered
        name = getSlingRequest().getParameter("name");
        userEmail = getSlingRequest().getParameter("emailaddress");
        log.debug("user email: " + userEmail);
        subject = getSlingRequest().getParameter("subject");
        comments = getSlingRequest().getParameter("comments");

        PropertiesService propertiesService = getSlingScriptHelper().getService(PropertiesService.class);
        //set up our image urls for both emails
        headerLogoUrl = propertiesService.getPublishUrl() + "/etc/designs/plc/prkc/uk/open-ideas/clientlibs/img/openideas_logo.png";
        footerLogoLeftUrl = propertiesService.getPublishUrl() + "/etc/designs/plc/prkc/uk/open-ideas/clientlibs/img/alwayslearning.png";
        footerLogoRightUrl = propertiesService.getPublishUrl() + "/etc/designs/plc/prkc/uk/open-ideas/clientlibs/img/pearson.png";

        log.debug("header logo: " + headerLogoUrl);


        send();
    }

    /**
     * This method sends the contact us email.
     */
    public void send() {

        // get the MailService from the CQ server and set up the mail object
        messageGatewayService = getSlingScriptHelper().getService(MessageGatewayService.class);
        mailMessageGateway = messageGatewayService.getGateway(SimpleEmail.class);

        SimpleEmail email = new SimpleEmail();

        // build the properties of the email
        try {
            email.addTo(toEmail);
            email.setFrom(toEmail);
        } catch (EmailException e) {
            log.error("Trouble adding email address " + toEmail, e);
        }
        email.setSubject(emailSubject);

        StringBuilder template = new StringBuilder();
        template.append("<div style='background:#fcf5ea'>");
        template.append(
                "<img src='" + headerLogoUrl +"' style='width:165px;height:80px;margin: 8px 8px 20px 8px;' />")
                .append(NEW_LINE_CHAR);
        template.append("<div style='background-image: linear-gradient(top, rgb(68,68,68) 36%, rgb(51,51,51) 68%, rgb(51,51,51) 84%);background-image: -o-linear-gradient(top, rgb(68,68,68) 36%, rgb(51,51,51) 68%, rgb(51,51,51) 84%);background-image: -moz-linear-gradient(top, rgb(68,68,68) 36%, rgb(51,51,51) 68%, rgb(51,51,51) 84%);background-image: -webkit-linear-gradient(top, rgb(68,68,68) 36%, rgb(51,51,51) 68%, rgb(51,51,51) 84%);background-image: -ms-linear-gradient(top, rgb(68,68,68) 36%, rgb(51,51,51) 68%, rgb(51,51,51) 84%);background-image: -webkit-gradient(linear,left top,left bottom,color-stop(0.36, rgb(68,68,68)),color-stop(0.68, rgb(51,51,51)),color-stop(0.84, rgb(51,51,51)));padding: 4px 0 10px 8px;height:16px;width:100%; margin: 0 0 20px 0;'></div>");
        template.append("<div style='margin:0 0 0 10px'>");
        template.append("Name: ").append(name).append(NEW_LINE_CHAR);
        template.append("Email: ").append(userEmail).append(NEW_LINE_CHAR);
        template.append("Subject: ").append(subject).append(NEW_LINE_CHAR);
        template.append("Comments: ").append(comments).append(NEW_LINE_CHAR);
        template.append("</div>");
        template.append("<a href='http://uk.pearson.com/legal-notice.html'>Terms and Conditions</a><br/>");	
        template.append("<div style='background:#393a3a; width:100%; padding:10px 10px 10px 10px; height:45px;margin: 20px 0 0;'><img src='" + footerLogoLeftUrl + "' style='float:left;width:140px; height:15px; margin-top: 15px'><img src='" + footerLogoRightUrl + "' style='float:right; width:105px;height:25px;margin:7px 17px 0 0;'></div>");
        template.append("</div>");
        email.setContent(template.toString(), "text/html");

        if (mailMessageGateway != null) {
            mailMessageGateway.send(email);
        } else {
            log.error("email message gateway was null, no email sent.");
        }

        sendUserEmail(userEmail);
    }

    /**
     * This method sends an email saying thank you for contacting us.
     */
    public void sendUserEmail(String userEmailAddress) {

        SimpleEmail userEmail = new SimpleEmail();

        // build the properties of the email
        try {
            userEmail.addTo(userEmailAddress);
            userEmail.setFrom(toEmail);
        } catch (EmailException e) {
            log.error("Trouble adding email address", e);
        }
        userEmail.setSubject("Open Ideas - Thank you for getting in touch");

        // get parameters from
        StringBuilder template = new StringBuilder();
        template.append("<table style='background:#fcf5ea; width:100%;'><tr><td>");
        template.append("<img src='" + headerLogoUrl +"' style='width:165px;height:80px;margin: 8px 8px 20px 8px;' /></td></tr>");
        
        template.append("<tr><td height='30' style='bgcolor:#393a3a;background:#393a3a;background-image:linear-gradient(top, rgb(68,68,68) 36%, rgb(51,51,51) 68%, rgb(51,51,51) 84%);background-image: -o-linear-gradient(top, rgb(68,68,68) 36%, rgb(51,51,51) 68%, rgb(51,51,51) 84%);background-image: -moz-linear-gradient(top, rgb(68,68,68) 36%, rgb(51,51,51) 68%, rgb(51,51,51) 84%);background-image: -webkit-linear-gradient(top, rgb(68,68,68) 36%, rgb(51,51,51) 68%, rgb(51,51,51) 84%);background-image: -ms-linear-gradient(top, rgb(68,68,68) 36%, rgb(51,51,51) 68%, rgb(51,51,51) 84%);background-image: -webkit-gradient(linear,left top,left bottom,color-stop(0.36, rgb(68,68,68)),color-stop(0.68, rgb(51,51,51)),color-stop(0.84, rgb(51,51,51))); width:100%;'></td></tr>");
        template.append("<tr><td><br>Hello ").append(name).append(",<br><br></td></tr>");
        template.append("<tr><td>Thank you for getting in touch.").append("<br><br></td></tr>");
        template.append("<tr><td>We will do our best to get back to you as soon as possible.").append("</td></tr>");
        template.append("<tr><td><br>Best wishes,").append("</td></tr>");
        template.append("<tr><td>The Open Ideas Team</td></tr>");
        template.append("<tr><td><a style='margin:10px 0 0 0;' href='http://research.pearson.com'>http://research.pearson.com</a><br><br></td></tr></table>");
        
        template.append("<table style='background:#fcf5ea; width:100%; border-collapse:collapse'>");
        template.append("<tr style='background:#393a3a; width:100%; padding:10px 10px 10px 10px; height:45px;margin: 20px 0 0;'><td style='width:50%;background:#393a3a;'><img src='" + footerLogoLeftUrl + "' style='float:left;width:140px; height:15px; margin:15px 0 15px 10px;'></td><td style='background:#393a3a; width:50%; text-align:right;'><img src='" + footerLogoRightUrl + "' style='float:right;  width:105px;height:25px;margin:10px 10px;'></td></tr>");
        template.append("</table>");
        userEmail.setContent(template.toString(), "text/html");

        if (mailMessageGateway != null) {
            mailMessageGateway.send(userEmail);
        } else {
            log.error("email message gateway was null, no user email sent.");
        }
    }
}