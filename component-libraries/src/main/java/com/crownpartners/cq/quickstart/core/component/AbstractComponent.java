/**
 * Created: Feb 3, 2012 3:05:26 PM
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

package com.crownpartners.cq.quickstart.core.component;

import javax.jcr.Node;
import javax.servlet.jsp.PageContext;

import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.scripting.SlingScriptHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.crownpartners.cq.quickstart.core.annotations.parser.ComponentAnnotationParser;
import com.day.cq.commons.inherit.HierarchyNodeInheritanceValueMap;
import com.day.cq.commons.inherit.InheritanceValueMap;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.day.cq.wcm.api.WCMMode;
import com.day.cq.wcm.api.components.EditConfig;
import com.day.cq.wcm.api.components.EditContext;
import com.day.cq.wcm.api.components.EditLayout;
import com.day.cq.wcm.api.components.Toolbar;
import com.day.cq.wcm.api.designer.Design;
import com.day.cq.wcm.api.designer.Style;
import com.day.cq.wcm.foundation.ELEvaluator;

/**
 * Super class for all components.
 * 
 * @version 2.0
 * @author SamLabib
 */
public abstract class AbstractComponent {

    /** current logger instance. */
    private static final Logger log = LoggerFactory.getLogger(AbstractComponent.class);

    /** The author mode. */
    private final boolean authorMode;

    /** the current WCMMode. */
    private final WCMMode wcmMode;

    /** The current design. */
    private final Design currentDesign;

    /** The current node. */
    private final Node currentNode;

    /** The current page. */
    private final Page currentPage;

    /** The current style. */
    private final Style currentStyle;

    private final EditContext editContext;

    /** The page context. */
    private final PageContext pageContext;

    /** The page manager. */
    private final PageManager pageManager;

    /** The properties. */
    private final ValueMap properties;

    /** the page properties. */
    private final InheritanceValueMap pageProperties;

    /**
     * the inherited properties, not final because they are retrieved on demand only if needed.
     */
    private InheritanceValueMap inheritedProperties;

    /** The resource. */
    private final Resource resource;

    /** The resource design. */
    private final Design resourceDesign;

    private final ResourceResolver resourceResolver;

    /** The sling request. */
    private final SlingHttpServletRequest slingRequest;

    /** the sling response. */
    private final SlingHttpServletResponse slingResponse;

    /** The sling script helper. */
    private final SlingScriptHelper slingScriptHelper;

    private final String[] selectors;

    /**
     * Get all important objects from pageContext.
     * 
     * @param pageContext
     *            the page context.
     */
    public AbstractComponent(final PageContext pageContext) {
        this(pageContext, (Resource) pageContext.getAttribute("resource"));
    }

    /**
     * Construct the component model from the given resource instead of the resource found in the page context. Page
     * context is still use for retrieving other values such as request & response objects.
     * 
     * @param pageContext
     *            the page context
     * @param resource
     *            the resource
     */
    public AbstractComponent(final PageContext pageContext, final Resource resource) {
        if (pageContext == null) {
            throw new IllegalArgumentException("pageContext must not be null.");
        }

        this.pageContext = pageContext;

        this.slingRequest = (SlingHttpServletRequest) pageContext.getAttribute("slingRequest");
        this.slingResponse = (SlingHttpServletResponse) pageContext.getAttribute("slingResponse");
        this.slingScriptHelper = (SlingScriptHelper) pageContext.getAttribute("sling");
        this.pageManager = (PageManager) pageContext.getAttribute("pageManager");

        this.currentNode = (Node) pageContext.getAttribute("currentNode");
        this.currentDesign = (Design) pageContext.getAttribute("currentDesign");
        this.resourceDesign = (Design) pageContext.getAttribute("resourceDesign");
        this.currentStyle = (Style) pageContext.getAttribute("currentStyle");
        this.editContext = (EditContext) pageContext.getAttribute("editContext");

        this.wcmMode = WCMMode.fromRequest(slingRequest);
        this.authorMode = wcmMode == WCMMode.EDIT || wcmMode == WCMMode.DESIGN;
        this.selectors = slingRequest.getRequestPathInfo().getSelectors();

        this.resource = resource;
        this.resourceResolver = resource.getResourceResolver();
        this.properties = resource.adaptTo(ValueMap.class);

        this.currentPage = pageManager.getContainingPage(resource);
        if (currentPage != null) {
            this.pageProperties = new HierarchyNodeInheritanceValueMap(currentPage.getContentResource());
        } else {
            this.pageProperties = null;
        }

        log.debug("Initializing component {}", this.getClass().getName());

        final ComponentAnnotationParser parser = new ComponentAnnotationParser();
        parser.parse(this);

        init();
    }

    /**
     * Get the current design. Note: The current design can be null
     * 
     * @return the current design.
     */
    public Design getCurrentDesign() {
        return this.currentDesign;
    }

    /**
     * Get the current node.
     * 
     * @return the current node
     */
    public Node getCurrentNode() {
        return this.currentNode;
    }

    /**
     * Get the current page.
     * 
     * @return the current page
     */
    public Page getCurrentPage() {
        return this.currentPage;
    }

    /**
     * Get the current resource.
     * 
     * @return the resource of the request.
     */
    public Resource getCurrentResource() {
        return this.resource;
    }

    /**
     * Get the current style. Note: The current style can be null.
     * 
     * @return the current style.
     */
    public Style getCurrentStyle() {
        return this.currentStyle;
    }

    /**
     * Get the edit context.
     * 
     * @return the edit context
     */
    public EditContext getEditContext() {
        return this.editContext;
    }

    /**
     * Get the page context.
     * 
     * @return the page context
     */
    public PageContext getPageContext() {
        return this.pageContext;
    }

    /**
     * Get the page manager.
     * 
     * @return the page manager
     */
    public PageManager getPageManager() {
        return this.pageManager;
    }

    /**
     * Get the properties.
     * 
     * @return the properties
     */
    public ValueMap getProperties() {
        return this.properties;
    }

    /**
     * Get the inherited properties.
     * 
     * @return the inherited properties
     */
    public InheritanceValueMap getInheritedProperties() {
        if (inheritedProperties == null) {
            inheritedProperties = new HierarchyNodeInheritanceValueMap(getCurrentResource());
        }
        return inheritedProperties;
    }

    /**
     * get the page properties.
     * 
     * @return the page properties
     */
    public InheritanceValueMap getPageProperties() {
        return pageProperties;
    }

    /**
     * Get the resource design.
     * 
     * @return the resource design
     */
    public Design getResourceDesign() {
        return this.resourceDesign;
    }

    /**
     * Get the resource resolver.
     * 
     * @return the resource resolver
     */
    public ResourceResolver getResourceResolver() {
        return this.resourceResolver;
    }

    /**
     * Get the sling request.
     * 
     * @return the sling request
     */
    public SlingHttpServletRequest getSlingRequest() {
        return this.slingRequest;
    }

    /**
     * Get the sling response.
     * 
     * @return he sling response
     */
    public SlingHttpServletResponse getSlingResponse() {
        return this.slingResponse;
    }

    /**
     * Get the sling script helper.
     * 
     * @return the sling script helper
     */
    public SlingScriptHelper getSlingScriptHelper() {
        return this.slingScriptHelper;
    }

    /**
     * Get the selectors for this request.
     * 
     * @return the selectors
     */
    public String[] getSelectors() {
        return selectors;
    }

    /**
     * In every concrete component, use this method to calculate your attributes.<br />
     * Use this method to fetch node-properties and to assign them to your attributes.<br />
     * Avoid to perform these calculations in the constructor. <br />
     * <br />
     * Please note that the init-method will be called by the constructor of the AbstractComponent class. No child class
     * should call it itself in order to avoid multiple initializations.
     */
    public abstract void init();

    /**
     * get the current WCMMode.
     * 
     * @return the WCMMode
     */
    public WCMMode getWcmMode() {
        return wcmMode;
    }

    /**
     * Check if in edit or author mode.
     * 
     * @return true, if is the editor or author mode, otherwise return false
     */
    public boolean isAuthorMode() {
        return this.authorMode;
    }

    /**
     * Set the method for a editbar text.
     * 
     * @param key
     *            the string of the key from the language file
     */
    protected void setEditbarLabel(final String key) {
        if (getEditContext() != null && WCMMode.fromRequest(getPageContext().getRequest()) == WCMMode.EDIT) {
            final EditConfig editConfig = getEditContext().getEditConfig();
            if (editConfig.getLayout() == EditLayout.EDITBAR) {
                editConfig.getToolbar().add(new Toolbar.Label(key));
            }
        }
    }

    /**
     * set a response header so that the current page cannot be cached by the dispatcher see
     * http://dev.day.com/content/kb/home/Dispatcher/faq-s/DispatcherNoCache .html for details.
     */
    protected void setResponseNonCacheable() {
        getSlingResponse().setHeader("Dispatcher", "no-cache");
    }

    /**
     * retrieve a parameter from the request.
     * 
     * @param parameterName
     *            the name of the parameter to retrieve
     * @return the parameter value, or null if it isn't found
     */
    protected String getRequestParameter(final String parameterName) {
        return getRequestParameter(parameterName, null);
    }

    /**
     * retrieve a parameter from the request, falling back to a default if the parameter isn't found.
     * 
     * @param parameterName
     *            the name of the parameter to retrieve
     * @param defaultVal
     *            the default value
     * @return the parameter value if it exists in the requests, otherwise the default value
     */
    protected String getRequestParameter(final String parameterName, final String defaultVal) {
        String val = getSlingRequest().getParameter(parameterName);
        if (StringUtils.isBlank(val)) {
            val = defaultVal;
        }
        return val;
    }

    /**
     * get a property, but Expression Language (EL) evaluate the result before returning.
     * 
     * @param propertyName
     *            the name of the property to retrieve
     * @param defaultValue
     *            the default value, if it's not found in the properties ValueMap
     * @return the EL evaluated property value
     */
    protected String getELEvaluatedProperty(final String propertyName, final String defaultValue) {
        final String val = getProperties().get(propertyName, defaultValue);
        return ELEvaluator.evaluate(val, getSlingRequest(), getPageContext());
    }
}
