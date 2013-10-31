<%--

  Article Contributor Information component.

  The Article Contributor Information displayed at the bottom of the article

--%><%
%><%@include file="/apps/plc/prkc/uk/open-ideas/global.jsp"%><%
%><%@page session="false" import="com.pearson.openideas.cq5.components.content.ArticleContributorInfo" %><%
%><%
    ArticleContributorInfo articleContributorInfo = new ArticleContributorInfo(pageContext);
    pageContext.setAttribute("articleContributorInfo",articleContributorInfo);
%>
<c:choose>
    <c:when test="${articleContributorInfo.renderForPublish}">
        <div class="articlefooter">
             <div class="contributor">
                 <p class="conhead"><c:if test="${not empty articleContributorInfo.leadContributor}">LEAD</c:if> CONTRIBUTOR <img src="/etc/designs/plc/prkc/uk/open-ideas/clientlibs/img/contribute.png"/></p>
                      <div class="conimage"><img src="${articleContributorInfo.imageUrl}" /></div>
              </div>
              <div class="contributeinfo">
                   <div class="bio">
                        <p class="name">${articleContributorInfo.contributorName}</p>
                        <c:if test="${empty articleContributorInfo.contributorBio}" >
                            <h1> DOUBLE CLICK HERE TO ENTER REQUIRED CONTRIBUTOR DETAILS</h1>
                        </c:if>
                        <p class="contributor-bio">${articleContributorInfo.contributorBio}</p>
                        <a class="contributor-link" href="${articleContributorInfo.contributorUrl}" target="_blank">${articleContributorInfo.contributorUrl}</a>
                   </div>
              </div>
        </div>
    </c:when>
    <c:otherwise>
       <c:if test="${articleContributorInfo.authorMode}">
          <h2>Click here to add optional contributor info (will not render outside of author mode)</h2>
       </c:if>
    </c:otherwise>
</c:choose>