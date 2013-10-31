/**
 * Created: Jun 5, 2013 12:45:00 AM
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
package com.pearson.openideas.cq5.components.services.solr.page;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Service;
import org.osgi.framework.Constants;

import com.pearson.openideas.cq5.components.beans.solr.Page;
import com.pearson.openideas.cq5.components.services.solr.AbstractSolrSearchService;

/**
 * A Service interface which provides for index and retrieval of Page data operating against a SOLR search server.
 * 
 * @version 2.0
 * 
 * @author Sam Labib
 * 
 */
@Component(label = "Pearson Open Ideas Site Solr Page Search Service", immediate = true, metatype = true)
@Property(name = Constants.SERVICE_DESCRIPTION, value = "Open Ideas Site Solr Page Search Service")
@Service(SolrPageSearchService.class)
public class SolrPageSearchServiceImpl extends AbstractSolrSearchService<Page> implements SolrPageSearchService {

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    protected Class<Page> getEntityClass() {
        return Page.class;
    }

}
