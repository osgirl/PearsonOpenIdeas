<%--

  Landing Page Component

--%><%
%><%@include file="/apps/plc/prkc/uk/open-ideas/global.jsp"%><%
%><%@page session="false" import="com.day.cq.wcm.foundation.Image, com.pearson.openideas.cq5.components.filters.LandingPageList"%><%
    LandingPageList landing = new LandingPageList(pageContext);
    pageContext.setAttribute("landing",landing);
%>
<div class="separator">
	<h1 class="landing-title">${landing.landingTitle}</h1>
	<div class="filterby">
        <a class="filterby-open">Filter by</a>
	    <div class="filterby-window clearfix">
	        <form id="myform">
                <input class="pearson-button-dark reset-filter-button" type="submit" value="Clear All Filters" />
                <!--Choose if we are using theme or category here -->
                <c:choose>
                <c:when test="${landing.useThemeFilter}">
                    <h2 class="themes-title">Sector</h2>
                    <ul class="landing-sectors-list">
                        <c:forEach var="entry" items="${landing.categoryTags}">
                             <li class="gc" onclick=""><input class="radio-item" type="radio" name="category" value="${entry.key.title}" ${entry.key.title eq landing.selectedCategoryFilter ? 'checked' : ''} /><span class="aside-list-item-title">${entry.key.title}</span><img class="asidelitag" src="/etc/designs/plc/prkc/uk/open-ideas/clientlibs/img/tag.png"/><span class="number">${entry.value}</span></li>
                        </c:forEach>
                    </ul>
                </c:when>
                <c:otherwise>
                    <h2 class="themes-title">Themes</h2>
                      <ul class="landing-themes-list">
                          <c:forEach var="entry" items="${landing.themeTags}">
                              <li class="gc" onclick=""><input class="radio-item" type="radio" name="theme" value="${entry.key.title}" ${entry.key.title eq landing.selectedThemeFilter ? 'checked' : ''} /><span class="aside-list-item-title">${entry.key.title}</span><img class="asidelitag" src="/etc/designs/plc/prkc/uk/open-ideas/clientlibs/img/tag.png"/><span class="number">${entry.value}</span></li>
                          </c:forEach>
                      </ul>
                </c:otherwise>
                </c:choose>
                <!-- choose if we are using region or category here -->
                <c:choose>
                <c:when test="${landing.useRegionFilter}">
                   <div class="country-level-control">
                      <label>Sector</label><br/>
                      <select class="country-select" name="category">
                          <option value="All">All</option>
                          <c:forEach var="entry" items="${landing.categoryTags}">
                              <option value="${entry.key.title}" ${entry.key.title eq landing.selectedCategoryFilter ? 'selected' : ''}>${entry.key.title}</option>
                          </c:forEach>
                      </select>
                   </div>
                </c:when>
                <c:otherwise>
                    <div class="country-level-control">
                       <label>Region</label><br/>
                       <select class="country-select" name="region">
                           <option value="All">World Wide</option>
                           <c:forEach var="region" items="${landing.regionTagList}">
                               <option value="${region.name}" ${region.name eq landing.selectedRegionFilter ? 'selected' : ''}>${region.title}</option>
                           </c:forEach>
                       </select>
                    </div>
                </c:otherwise>
                </c:choose>
                <div class="country-level-control">
                  <label>Country</label><br/>
                  <select class="country-select" name="country">
                      <option value="All">World Wide</option>
                      <c:forEach var="country" items="${landing.countryTagList}">
                          <option value="${country.name}" ${country.name eq landing.selectedCountryFilter ? 'selected' : ''}>${country.title}</option>
                      </c:forEach>
                  </select>
                </div>
                <input class="pearson-button-dark go-button" type="submit" value="GO" />
	        </form>
        </div><!-- /.asidetheme2 -->
    </div><!-- /.filtermenu -->
</div><!-- /.separator -->

<c:choose>
<c:when test="${empty landing.articles}">
   <h1>Sorry, no results found.</h1>
</c:when>
<c:otherwise>
    <c:forEach var="article" items="${landing.articles}">
    <div class="item clearfix">
           <div class="reviewhead"><p class="inline-text">${article.category}</p> </div>
           <div class="filtername2"><p class="inline-text landing-theme">${article.theme}</p> <img src="/etc/designs/plc/prkc/uk/open-ideas/clientlibs/img/tag.png"/></div>
           <div class="sectionfilter">
               <c:set var="articleImage" value="${article.thumbnail}"/>
               <div class="section-left clearfix">
               <a href="${article.url}.html">
               <%
                    Image image = (Image) pageContext.getAttribute("articleImage");
                    if(image != null) {
                        image.draw(out);
                    }
                %>
                </a></div>
               <div class="sectionbody clearfix">
                   <div class="publishsection"><p class="inline-text">${article.pubDate} <c:if test="${not empty article.author}">- Lead Contributor: ${article.author}</c:if></div>
                   <div class="summary">
                       <a class="title-link" href="${article.url}.html"><h2 class="landing-article-title">${article.articleTitle}</h2></a>
                       <p>${article.shortSummary}<br><a class="landing-read-full" href="${article.url}.html">READ FULL ARTICLE</a></p>
                    </div>
               </div>
           </div>
    </div>
    </c:forEach>
</c:otherwise>
</c:choose>

<c:if test="${landing.totalArticles gt landing.pagesize}">
    <c:set var="pageNumber" value="${landing.pageNumber}" />
    <c:set var="pageCount" value="${landing.pageCount}" />
    <c:set var="pageString" value="${landing.params}" />
    <ul id="pagination-clean">
        <c:if test="${pageNumber != 1}">
            <li class="previous"><a href="${pageString}${pageNumber-1}">&lt;&lt;Previous</a></li>
        </c:if>
        
        <c:if test="${pageNumber > 1}">
            <li><a href="${pageString}${pageNumber-1}">${pageNumber-1}</a></li>
        </c:if>
        
        <li class="active">${pageNumber}</li>
        
        <c:forEach begin="${pageNumber+1}" end="${pageNumber+4}" varStatus="nPage">
            <c:if test="${pageCount >= nPage.index}">
                <li><a href="${pageString}${nPage.index}">${nPage.index}</a></li>
            </c:if>
        </c:forEach>
        <c:if test="${pageCount > 1 && pageNumber < pageCount}">
            <li class="next"><a href="${pageString}${pageNumber+1}">Next>></a></li>
        </c:if>
    </ul>
</c:if>

<script type="text/javascript">

var Pearson = Pearson || {};
Pearson.relationshipJsonData = ${landing.relationshipJSON};  //the EL expression to pull this value from the java class

</script>
