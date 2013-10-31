package com.pearson.openideas.cq5.components.utils;

import com.pearson.openideas.cq5.components.exceptions.DuplicateUserException;
import com.pearson.openideas.cq5.components.exceptions.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.query.Query;
import javax.jcr.query.QueryManager;
import javax.jcr.query.QueryResult;

public class UserUtils {

    private final static String SECRET_KEY = "033c7819-d20d-4e17-b166-4269fc6411b0"; // The key for 'encrypting' and
                                                                                     // 'decrypting'.

    // the logger
    private static final Logger log = LoggerFactory.getLogger(UserUtils.class);

    public String encodeString(final String str) {

        final StringBuffer stringBuffer = new StringBuffer(str);

        final int lenStr = str.length();
        final int lenKey = SECRET_KEY.length();

        //
        // For each character in our string, encrypt it...
        for (int i = 0, j = 0; i < lenStr; i++, j++) {
            if (j >= lenKey) {
                j = 0;
            } // Wrap 'round to beginning of key string.

            //
            // XOR the chars together. Must cast back to char to avoid compile error.
            //
            stringBuffer.setCharAt(i, (char) (str.charAt(i) ^ SECRET_KEY.charAt(j)));
        }

        return stringBuffer.toString();

    }

    public String decodeString(final String str) {

        // To 'decrypt' the string, simply apply the same technique.
        return encodeString(str);

    }

    public String encodePassword(final String str) {
        return encodeString(str);
    }

    public String decodePassword(final String str) {
        return decodeString(str);
    }

    public String findUserId(final Session adminSession, final String usersPath, final String email)
            throws DuplicateUserException, UserNotFoundException, RepositoryException {

        log.debug("Trying to find user ID for: " + email);
        String userId = null;

        final QueryManager queryManager = adminSession.getWorkspace().getQueryManager();

        // Create XPath query
        final String xPathQuery = "/jcr:root" + usersPath + "//element(*, rep:User) [profile/@email = '" + email
                + "'] order by @jcr:score";

        // Create query
        @SuppressWarnings("deprecation")
        final Query query = queryManager.createQuery(xPathQuery, Query.XPATH);

        // Set return limit - only need 2 to know if 1 or 2 accounts exists with same
        query.setLimit(2);

        // Execute query
        final QueryResult result = query.execute();

        // Get nodes
        final NodeIterator iter = result.getNodes();

        // Get found count
        final long count = iter.getSize();

        // Check result count and handle
        if (count == 1) {
            // User found (only 1 user as well)
            final javax.jcr.Node userNode = iter.nextNode();
            userId = userNode.getName();

        } else if (count > 1) {
            throw new DuplicateUserException("Duplicate user" + email);
        } else if (count < 1) {
            throw new UserNotFoundException("User not found " + email);
        }

        return userId; // Return found user id or exception is thrown

    }

    public String findUserName(final Session adminSession, final String usersPath, final String email)
            throws DuplicateUserException, UserNotFoundException, RepositoryException {

        log.debug("Trying to find user name for: " + email);
        String userId = null;
        String userName = "";

        final QueryManager queryManager = adminSession.getWorkspace().getQueryManager();

        // Create XPath query
        final String xPathQuery = "/jcr:root" + usersPath + "//element(*, rep:User) [profile/@email = '" + email
                + "'] order by @jcr:score";

        // Create query
        @SuppressWarnings("deprecation")
        final Query query = queryManager.createQuery(xPathQuery, Query.XPATH);

        // Set return limit - only need 2 to know if 1 or 2 accounts exists with same
        query.setLimit(2);

        // Execute query
        final QueryResult result = query.execute();

        // Get nodes
        final NodeIterator iter = result.getNodes();

        // Get found count
        final long count = iter.getSize();

        // Check result count and handle
        if (count == 1) {
            // User found (only 1 user as well)
            final javax.jcr.Node userNode = iter.nextNode();
            userId = userNode.getName();
            final javax.jcr.Node profileNode = userNode.getNode("profile");
            log.debug("do we have the right node? " + profileNode.getName());
            userName = NodeUtils.getSingleProperty(profileNode, "givenName");
            log.debug("The user name is " + userName);

        } else if (count > 1) {
            throw new DuplicateUserException("Duplicate user" + email);
        } else if (count < 1) {
            throw new UserNotFoundException("User not found " + email);
        }

        return userName; // Return found user id or exception is thrown

    }

    /**
     * This method will find the user profile node we need to update/pull data from for login locking.
     * 
     * @param adminSession
     *            the session
     * @param usersPath
     *            the user path we're looking for
     * @param email
     *            the email of the user
     * @return the user node needed
     * 
     * @throws RepositoryException
     *             repository exception thrown if there are any problems interacting with the repository
     */
    public Node findUserProfileNode(final Session adminSession, final String usersPath, final String email)
            throws RepositoryException {
        log.debug("Trying to find user name for: " + email);

        Node profileNode = null;

        String userId = null;
        String userName = "";

        final QueryManager queryManager = adminSession.getWorkspace().getQueryManager();

        // Create XPath query
        final String xPathQuery = "/jcr:root" + usersPath + "//element(*, rep:User) [profile/@email = '" + email
                + "'] order by @jcr:score";

        // Create query
        @SuppressWarnings("deprecation")
        final Query query = queryManager.createQuery(xPathQuery, Query.XPATH);

        // Set return limit - only need 2 to know if 1 or 2 accounts exists with same
        query.setLimit(2);

        // Execute query
        final QueryResult result = query.execute();

        // Get nodes
        final NodeIterator iter = result.getNodes();

        // Get found count
        final long count = iter.getSize();

        // Check result count and handle
        if (count == 1) {
            // User found (only 1 user as well)
            Node userNode = iter.nextNode();

            profileNode = userNode.getNode("profile");
        } else if (count > 1) {
            log.error("ERROR: duplicate user");
        } else if (count < 1) {
            log.error("ERROR: no user found");
        }

        return profileNode;
    }

    public boolean checkUserExists(final Session adminSession, final String searchUsersPath, final String searchEmail)
            throws RepositoryException {
        return checkUserExists(adminSession, "", searchUsersPath, searchEmail);
    }

    public boolean checkUserExists(final Session adminSession, final String loggedInUserId,
            final String searchUsersPath, final String searchEmail) throws RepositoryException {

        boolean userExists = false;

        final QueryManager queryManager = adminSession.getWorkspace().getQueryManager();

        // Create XPath query
        final String xPathQuery = "/jcr:root" + searchUsersPath + "//element(*, rep:User) [profile/@email = '"
                + searchEmail + "' and @rep:principalName != '" + loggedInUserId + "'] order by @jcr:score";

        // Create query
        @SuppressWarnings("deprecation")
        final Query query = queryManager.createQuery(xPathQuery, Query.XPATH);

        // Set return limit - only need 2 to know if 1 or 2 accounts exists with same
        query.setLimit(1);

        // Execute query
        final QueryResult result = query.execute();

        // Get nodes
        final NodeIterator iter = result.getNodes();

        // Get found count
        final long count = iter.getSize();

        // Check result count and handle
        if (count == 1) {
            userExists = true; // User exists
        }

        return userExists;
    }

}
