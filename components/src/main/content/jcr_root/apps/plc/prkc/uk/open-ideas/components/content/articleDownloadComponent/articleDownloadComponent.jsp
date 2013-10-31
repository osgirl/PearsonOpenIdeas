<%--

  Article Downloads Component component.

  Article Downloads Component for attaching files such as, pdfs.

--%><%@ page import="com.day.cq.wcm.api.WCMMode,
                     com.day.cq.wcm.api.components.DropTarget,
                     com.day.cq.wcm.api.WCMMode,
                     com.day.cq.wcm.foundation.Download,
                     com.day.cq.xss.ProtectionContext,
                     com.day.cq.xss.XSSProtectionService,
                     com.day.cq.xss.XSSProtectionException,
                     java.util.Map,
                     com.pearson.openideas.cq5.components.content.ArticleDownloadComponent,
                     org.apache.commons.lang3.StringEscapeUtils" %><%
%><%@include file="/apps/plc/prkc/uk/open-ideas/global.jsp"%><%
    //drop target css class = dd prefix + name of the drop target in the edit config
    String ddClassName = DropTarget.CSS_CLASS_PREFIX + "file";

    Download dld = new Download(resource);
    if (dld.hasContent()) {
        dld.addCssClass(ddClassName);
        String title = dld.getTitle(true);
        //String href = Text.escape(dld.getHref(), '%', true);
        String href =dld.getHref();
        String displayText = dld.getInnerHtml() == null ? dld.getFileName() : dld.getInnerHtml().toString();
        String description = dld.getDescription();
        XSSProtectionService xss = sling.getService(XSSProtectionService.class);
        if (xss != null) {
            try {
                displayText = xss.protectForContext(ProtectionContext.PLAIN_HTML_CONTENT, displayText);
            } catch (XSSProtectionException e) {
                log.warn("Unable to protect link display text from XSS: {}", displayText);
            }
            try {
                description = xss.protectForContext(ProtectionContext.PLAIN_HTML_CONTENT, description);
            } catch (XSSProtectionException e) {
                log.warn("Unable to protect link description from XSS: {}", description);
            }
        }
        
        // Crown Code
        ArticleDownloadComponent download = new ArticleDownloadComponent(pageContext);
        pageContext.setAttribute("download", download);
        %><div>
            <c:choose>
	            <c:when test="${download.printImage}">
	              <span class="customImage"><% download.getImage().draw(out);%></span>
	        	</c:when>
	        	<c:otherwise>
	        		<span class="icon type_<%= dld.getIconType() %>"><img src="/etc/designs/default/0.gif" alt="*"></span>	
	        	</c:otherwise>
        	</c:choose>
			<small><%= description %></small>
            <a class="article-download-link" href="<%= href%>" target="_blank" title="<%=StringEscapeUtils.escapeHtml4(title)%>"
               onclick="CQ_Analytics.record({event: 'startDownload', values: { downloadLink: '<%=href%>' }, collect:  false, options: { obj: this, defaultLinkType: 'd' }, componentPath: '<%=resource.getResourceType()%>'})"<%
                Map<String,String> attrs = dld.getAttributes();
            if ( attrs!= null) {
                for (Map.Entry e : attrs.entrySet()) {
                    out.print(StringEscapeUtils.escapeHtml4(e.getKey().toString()));
                    out.print("=\"");
                    out.print(StringEscapeUtils.escapeHtml4(e.getValue().toString()));
                    out.print("\"");
                }
            }%>
        >Download</a><br>
        </div>
        <%
        
    } else if (WCMMode.fromRequest(request) == WCMMode.EDIT) {
        %><img src="/libs/cq/ui/resources/0.gif" class="cq-file-placeholder <%= ddClassName %>" alt="">
        <p>Double-click here to add a download</p>
        <div class="clear clearfix"></div><%
    }
%>
