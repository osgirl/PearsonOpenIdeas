package com.pearson.openideas.cq5.components.services.test.impl;

import com.pearson.openideas.cq5.components.services.test.TestService;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author John S. (jspyronis@tacitknowledge.com)
 *         Date: 22/11/2013
 *         Time: 15:23
 */

@Component(
        immediate = true,
        metatype = true,
        label = "Pearson Test Service",
        description = "test service for Pearson open ideas project"
)
@Service(value = TestService.class)
@org.apache.felix.scr.annotations.Properties({
        @org.apache.felix.scr.annotations.Property(name = "service.description", value = "Pearson Test Service"),
        @org.apache.felix.scr.annotations.Property(name = "service.vendor", value = "Pearson")
})
public class TestServiceImpl implements TestService
{
    private static final Logger LOGGER = LoggerFactory.getLogger(TestService.class);

    @Override
    public void runTestService()
    {
        LOGGER.debug("Running Test Service is ok........");
    }

    protected void activate(ComponentContext context) throws Exception
    {
        LOGGER.info("Service Test activating");
    }
}
