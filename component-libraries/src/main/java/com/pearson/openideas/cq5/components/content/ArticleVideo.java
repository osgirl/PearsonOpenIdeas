/**
 * Created: Apr 29, 2013 12:14:00 AM
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

import com.crownpartners.cq.quickstart.core.component.AbstractComponent;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.jsp.PageContext;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * The article video component.
 * 
 * @version 2.0
 * 
 * @author Mandeep Singh
 */
public class ArticleVideo extends AbstractComponent {

    // the logger
    private static final Logger log = LoggerFactory.getLogger(ArticleVideo.class);

    private static final String PROPERTY_VIDEO_URL = "videoURL";
    private static final int URL_OFFSET = 5;
    private String youtubeVideoURL;
    private List<String> transcript;
    private String errorText;

    /**
     * Constructor.
     * 
     * @param pageContext
     *            The page context
     */
    public ArticleVideo(PageContext pageContext) {
        super(pageContext);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void init() {
        setYoutubeVideoURL(getProperties().get(PROPERTY_VIDEO_URL, ""));

        transcript = new ArrayList<String>();

        try {
            Resource resource = getCurrentResource().getChild("file");
            log.debug("file associated with this: " + resource.getName() + ", " + resource.getResourceType());
            if (StringUtils.equalsIgnoreCase(resource.getResourceType(), "nt:file")) {
                InputStream inputStream = resource.adaptTo(InputStream.class);
                InputStreamReader is = new InputStreamReader(inputStream);
                BufferedReader br = new BufferedReader(is);
                String read = br.readLine();

                while (read != null) {
                    transcript.add(read);
                    read = br.readLine();
                }

            }
        } catch (Exception e) {
            log.warn("There is no file associated with this component");
        }

        log.debug("transcript lines: " + transcript.size());

    }

    /**
     * Get the youtube video url.
     * 
     * @return the youtube video url
     */
    public String getYoutubeVideoURL() {
        return youtubeVideoURL;
    }

    /**
     * Set the youtube video url.
     * 
     * @param embedCode
     *            The embedcode code provided by youtube
     */
    public void setYoutubeVideoURL(String embedCode) {

        if (StringUtils.isNotBlank(embedCode)) {
            // get src attribute from URL

            if (embedCode.startsWith("<iframe")) {
            int startPosition;
            int endPosition;
            startPosition = embedCode.indexOf("src") + URL_OFFSET;
            endPosition = embedCode.indexOf("\"", startPosition);
            String srcAttribute = embedCode.substring(startPosition, endPosition);
            // append html5=1 to the src attribute
            srcAttribute += "?html5=1";
            // replace src attribute
            embedCode = embedCode.substring(0, startPosition) + srcAttribute
                    + embedCode.substring(endPosition, embedCode.length());
            // add wrapper for responsive video size
            embedCode = "<div class='video-container'>" + embedCode + "</div>";
            log.debug("START POSITION: " + startPosition);
            log.debug("END POSITION: " + endPosition);
            log.debug("SRCATTRIBUTE: " + srcAttribute);
            log.debug("FINALSTRING: " + embedCode);
            this.youtubeVideoURL = embedCode;
            } else {
                errorText = "ERROR! You must use the embed YouTube link, that starts with an iframe tag.";
            }
        }
    }

    /**
     * Gets transcript.
     * 
     * @return Value of transcript.
     */
    public List<String> getTranscript() {
        return transcript;
    }

    /**
     * Sets new transcript.
     * 
     * @param transcript
     *            New value of transcript.
     */
    public void setTranscript(List<String> transcript) {
        this.transcript = transcript;
    }


    /**
     * Gets errorText.
     *
     * @return Value of errorText.
     */
    public String getErrorText() {
        return errorText;
    }

    /**
     * Sets new errorText.
     *
     * @param errorText New value of errorText.
     */
    public void setErrorText(String errorText) {
        this.errorText = errorText;
    }
}
