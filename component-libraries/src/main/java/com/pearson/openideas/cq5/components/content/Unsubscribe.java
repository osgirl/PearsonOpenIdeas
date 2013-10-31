/**
 * Created: May 20, 2013 11:02:11 AM
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
package com.pearson.openideas.cq5.components.content;

import org.apache.sling.jcr.api.SlingRepository;
import org.apache.jackrabbit.api.JackrabbitSession;
import org.apache.jackrabbit.api.security.user.User;
import org.apache.jackrabbit.api.security.user.UserManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.crownpartners.cq.quickstart.core.component.AbstractComponent;
import com.day.cq.security.AccountManager;
import com.day.cq.security.AccountManagerFactory;
import com.day.cq.security.Authorizable;

import com.pearson.openideas.cq5.components.utils.UserUtils;
import com.pearson.openideas.cq5.components.utils.Constants;

import javax.servlet.jsp.PageContext;
import javax.jcr.Session;
import javax.jcr.Node;
import javax.jcr.PropertyIterator;
import javax.jcr.NodeIterator;
import javax.jcr.Property;
import javax.jcr.Value;
import java.util.Iterator;
import javax.jcr.Node;

import com.day.cq.security.profile.Profile;

import com.day.cq.security.Authorizable;

/**
 * Model class for the Unsubscribe component.
 * 
 * @author mandeep.singh
 * 
 */


public class Unsubscribe extends AbstractComponent{
	private static final Logger log = LoggerFactory.getLogger(Unsubscribe.class);
	private final String EMAIL = "email";
    private String email;

    Session session = null;
    String error;
	
    /**
     * Constructor.
     * 
     * @param pageContext
     *            the pageContext to set.
     */
	public Unsubscribe(PageContext pageContext){
		super(pageContext);
	}
	
    /**
     * {@inheritDoc}
     */
    @Override
	public void init(){
    	email = getSlingRequest().getParameter(EMAIL);
		log.debug("email:" + email);
		JackrabbitSession adminSession = null;
		
		Node userNode = null;
	    User user = null;
	    Authorizable authorizable = null;
	    final SlingRepository repos = getSlingScriptHelper().getService(SlingRepository.class);
	    final AccountManagerFactory af = getSlingScriptHelper().getService(AccountManagerFactory.class);
	    
		try{
			session = repos.loginAdministrative(null);
			adminSession = (JackrabbitSession)session;
			final AccountManager am = af.createAccountManager(adminSession);
			final UserUtils userUtils = new UserUtils();
            final String userid = userUtils.findUserId(adminSession, Constants.USERS_PATH, email);
            log.debug("USERID:" + userid);
			authorizable = am.findAccount(userid);
		    final UserManager userManager = adminSession.getUserManager();
		        // Get user from user id supplier
	        //user = User.class.cast(userManager.getAuthorizable(userid));
		    
		}catch(Exception e){
			log.error("couldn't get usermanager setup using adminSession for unsubscribe", e);
		}


		try{
			// Get user properties
	        userNode = adminSession.getNode(authorizable.getHomePath() + "/profile");
	        log.debug("HomePath" + authorizable.getHomePath());
	        userNode.setProperty("permissionToEmail", "{empty}");
	       /* final Profile currentProfile = getSlingRequest().adaptTo(Profile.class);
	        userNode = adminSession.getNode(currentProfile.getAuthorizable().getHomePath());
	        userNode.setProperty("permissionToEmail", "null");*/
	        // Save session
	        adminSession.save();
	        session.save();
			
		}catch(Exception e){
			log.debug("Couldn't set permissionToEmail to null.");
		}
		
	    if (adminSession!= null) {
            adminSession.logout();
            adminSession = null;
        }
	    
	    if (session!=null) {
            session.logout();
            session = null;
        }
    }
}