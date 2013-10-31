<%--

  Article Video component.

  Video Component for Articles

--%><%
%><%@include file="/apps/plc/prkc/uk/open-ideas/global.jsp"%><%
%><%@page session="false" import="com.pearson.openideas.cq5.components.content.ArticleVideo" %><%
%>

<%
        ArticleVideo articleVideo = new ArticleVideo(pageContext);
        pageContext.setAttribute("articleVideo", articleVideo);
%>
<div class="clear clearfix"></div><!-- just incase a floated element exists above -->
<c:choose>
      <c:when test="${empty articleVideo.youtubeVideoURL}">
            <c:choose>
                <c:when test="${empty articleVideo.errorText}">
			        Double Click Here to add YouTube Video URL
			    </c:when>
			    <c:otherwise>
			        <c:if test="${articleVideo.authorMode}">
			            <h1>${articleVideo.errorText}</h2>
			        </c:if>
			    </c:otherwise>

			</c:choose>
      </c:when>

      <c:otherwise>
      <c:if test="${articleVideo.authorMode}">
      <p>Click here to edit YouTube URL (this will disappear outside of edit mode)</p>
      </c:if>
      ${articleVideo.youtubeVideoURL}
        <c:if test="${not empty articleVideo.transcript}">
        <a href="#" id="transcript-show" class="showLink"
        onclick="showHide('transcript');return false;">Show Transcript</a>
        <div class="articlebody">
        <div id="transcript" class="more">
             <div class="video-transcript">
                  <c:forEach var="line" items="${articleVideo.transcript}">
                  		  ${line}<br />
                  </c:forEach>
             </div>
             <a href="#" id="transcript-hide" class="hideLink"
                onclick="showHide('transcript');return false;">Hide Transcript</a>
        </div>
        </div>
        </c:if>
      </c:otherwise>
</c:choose>
<div class="clear clearfix"></div><!-- just in case a floated element exists above -->