package com.pearson.openideas.cq5.components.tags;

import com.pearson.openideas.cq5.components.content.ComponentReturningRunMode;

import javax.servlet.jsp.tagext.TagSupport;

/**
 * Example custom tag to return run mode..
 * @author John S. (jspyronis@tacitknowledge.com)
 *         Date: 21/11/2013
 *         Time: 09:26
 */
public class ComponentReturningRunModeTag extends TagSupport
{
    public int doStartTag()
    {
        ComponentReturningRunMode contentComponent = new ComponentReturningRunMode(pageContext);

        pageContext.setAttribute("contentComponent", contentComponent);

        return SKIP_BODY;
    }


}