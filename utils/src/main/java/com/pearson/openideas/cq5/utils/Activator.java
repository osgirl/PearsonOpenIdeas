package com.pearson.openideas.cq5.utils;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Bundle Activator for the Component Libraries Bundle.
 *
 * @author todd.guerra
 */
public class Activator implements BundleActivator {

    /**
     * The logger.
     */
    private static final Logger log = LoggerFactory.getLogger(Activator.class);

    @Override
    public void start(BundleContext context) throws Exception {
        log.info(context.getBundle().getSymbolicName() + " started");
    }

    @Override
    public void stop(BundleContext context) throws Exception {
        log.info(context.getBundle().getSymbolicName() + " stopped");
    }

}
