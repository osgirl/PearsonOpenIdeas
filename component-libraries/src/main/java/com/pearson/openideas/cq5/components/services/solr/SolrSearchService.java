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
package com.pearson.openideas.cq5.components.services.solr;

import java.util.List;

import com.pearson.openideas.cq5.components.beans.solr.SolrResults;
import com.pearson.openideas.cq5.components.beans.solr.SolrSearchParameters;

/**
 * A Service Interface which when implemented provides methods for indexing and retrieving documents against a SOLR
 * Search Server.
 * 
 * @version 2.0
 * 
 * @author Sam Labib
 * 
 * @param <E>
 *            the entity type handled by the implementing SolrSearchService
 */
public interface SolrSearchService<E> {

    /**
     * Retrieve a list of documents from the SOLR Server using the parameters to filter the results.
     * 
     * @param parameters
     *            the parameters used to filter the results
     * @return the list of documents retrieved
     * @throws Exception
     *             if an error occurs while retrieving documents
     */
    SolrResults<E> retrieve(SolrSearchParameters parameters) throws Exception;

    /**
     * Add the document to the SOLR Search Index.
     * 
     * @param bean
     *            the document to index
     * @throws Exception
     *             if an error occurs while retrieving document
     */
    void index(E bean) throws Exception;

    /**
     * Add the list of documents to the SOLR Search Index.
     * 
     * @param beans
     *            the list of documents to index
     * @throws Exception
     *             if an error occurs while retrieving documents
     */
    void index(List<E> beans) throws Exception;

    /**
     * Remove a given document from the SOLR Search Index. Calling this method has the same affect as calling
     * unIndexEntries() with a list of one.
     * 
     * @param uniqueId
     *            the id of the document to be removed
     * @throws Exception
     *             if an error occurs while retrieving documents
     */
    void unIndex(String uniqueId) throws Exception;

    /**
     * Remove a list of documents from the SOLR Search Index.
     * 
     * @param ids
     *            the ids of the documents to be removed
     * @throws Exception
     *             if an error occurs while retrieving documents
     */
    void unIndex(List<String> ids) throws Exception;
}
