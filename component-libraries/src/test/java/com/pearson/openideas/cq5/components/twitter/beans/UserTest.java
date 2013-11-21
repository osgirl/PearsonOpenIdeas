package com.pearson.openideas.cq5.components.twitter.beans;

import com.pearson.openideas.cq5.components.content.twitter.beans.User;
import com.tacitknowledge.noexcuses.MethodTester;
import org.junit.Test;

/**
* @author John Spyronis (jspyronis@tacitknowledge.com)
*/
public class UserTest
{
    @Test
    public void testAllMethodsWithGetPrefix() throws Exception {
        User object = new User();
        MethodTester methodTester = new MethodTester("get");
        methodTester.performTest(object);
    }

    @Test
    public void testAllMethodsWithSetPrefix() throws Exception {
        User object = new User();
        MethodTester methodTester = new MethodTester("set");
        methodTester.performTest(object);
    }
}
