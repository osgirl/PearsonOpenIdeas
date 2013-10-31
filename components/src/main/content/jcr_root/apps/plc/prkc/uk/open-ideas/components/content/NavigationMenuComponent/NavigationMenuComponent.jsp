<%--

  Navigation Menu Component component.

  The navigation menu that appears at the top of the page

--%><%
%>
<%@include file="/apps/plc/prkc/uk/open-ideas/global.jsp"%><%
%><%@page session="false" import="com.pearson.openideas.cq5.components.content.OpenIdeasNavigationMenu, com.day.cq.wcm.api.WCMMode" %>
<%
		OpenIdeasNavigationMenu navmenu = new OpenIdeasNavigationMenu(pageContext);
	    pageContext.setAttribute("navmenu", navmenu);
%>
<c:set var="seeAllSize" value="10" />
<div class="header-container">
   <header class="header-menu">
      <nav>
          <ul class="main-navigation">
              <li class="home-li">
                  <a href="/content/plc/prkc/uk/open-ideas/en.html"><img src="/etc/designs/plc/prkc/uk/open-ideas/clientlibs/img/home.png"/></a>
              </li>
              <li class="research-li down-arrow">
                  <a class="nav-research" href="#"><img class="event-calendar" src="/etc/designs/plc/prkc/uk/open-ideas/clientlibs/img/sections.png"/>Explore</a>
                  <div id="research-dropdown" class="drop close">
                  <div id="container3">
				    <div id="container2">
				        <div id="container1">
				            <div id="col1">
								<h3 class="drop-title">Themes</h3>
		                          <ul class="rightboxlist">
		                             <c:forEach var="theme" items="${navmenu.themeTags}" varStatus="count">
		                             <c:choose>
			                              	<c:when test="${count.index < seeAllSize}">
		                         			<li><a href="/content/plc/prkc/uk/open-ideas/en/explore/theme.${theme.name}.html">${theme.title}</a></li>
		                         			 </c:when>
			                                <c:otherwise>
			                                <li class="hide tag"><a href="/content/plc/prkc/uk/open-ideas/en/explore/theme.${theme.name}.html">${theme.title}</a></li>
			                                 </c:otherwise>
			                            </c:choose> 
		                             </c:forEach>
		                          </ul>
		                          <c:if test="${fn:length(navmenu.themeTags) gt seeAllSize}">
			                          <ul class="see-all see-all-themes">
			                              <li>
			                                  <a href="#">See all</a>
			                              </li>
			                          </ul>
		                          </c:if>
				            </div>
				            <div id="col2">
				            	<h3 class="drop-title">Sectors</h3>
		                          <ul class="leftboxlist">
		                              <c:forEach var="category" items="${navmenu.categoryTags}" varStatus="count">
		                              	<c:choose>
			                              	<c:when test="${count.index < seeAllSize}">
			                                   <li><a href="/content/plc/prkc/uk/open-ideas/en/explore/sector.${category.name}.html">${category.title}</a></li>
			                                </c:when>
			                                <c:otherwise>
			                                  <li class="hide tag"><a href="/content/plc/prkc/uk/open-ideas/en/explore/sector.${category.name}.html">${category.title}</a></li>
			                                </c:otherwise>
			                            </c:choose>   
		                             </c:forEach>
		                          </ul>
		                          <c:if test="${fn:length(navmenu.categoryTags) gt seeAllSize}">
			                          <ul class="see-all see-all-sectors">
			                              <li>
			                                  <a href="#">See all</a>
			                              </li>
			                          </ul>
			                      </c:if>
				            	  
				            </div>
				            <div id="col3">
				                  <h3 class="drop-title">Regions</h3>
		                          <ul class="far-rightboxlist">
		                             <c:forEach var="region" items="${navmenu.regionTags}" varStatus="count">
		                             <c:choose>
			                              	<c:when test="${count.index < seeAllSize}">
		                                    <li><a href="/content/plc/prkc/uk/open-ideas/en/explore/region.${region.name}.html">${region.title}</a></li>
		                                    </c:when>
			                                <c:otherwise>
			                                <li class="hide tag"><a href="/content/plc/prkc/uk/open-ideas/en/explore/theme.${region.name}.html">${region.title}</a></li>
			                                </c:otherwise>
			                            </c:choose> 
		                             </c:forEach>
		                          </ul>
		                          <c:if test="${fn:length(navmenu.regionTags) gt seeAllSize}">
			                          <ul class="see-all see-all-regions">
			                              <li>
			                                  <a href="#">See all</a>
			                              </li>
			                          </ul>
		                          </c:if>
				            </div>
				        </div>
				    </div>
				</div>
                  </div>
              </li>
              <li class="search-li down-arrow"><a href="#" class="nav-search"><img class="event-calendar" src="/etc/designs/plc/prkc/uk/open-ideas/clientlibs/img/search.png">Search</a>
                  <div class="drop close" id="search-dropdown">
                    <form method="GET" action="/content/plc/prkc/uk/open-ideas/en/search.html">
                      <input id="searchbox" class="searchbox" type="text" name="searchbox" value="Enter search term" placeholder="Enter search term" />
                      <input class="searchbutton pearson-button" type="submit" name="searchterm" value="Search"><br />
				    </form>
                  </div>
              </li>
              <li class="about-li">
                  <a class="events-dropdown" href="/content/plc/prkc/uk/open-ideas/en/events.html"><img class="event-calendar" src="/etc/designs/plc/prkc/uk/open-ideas/clientlibs/img/calander.png">Events</a>
              </li>
     

      
              <li class="contribute-li"><a href="/content/plc/prkc/uk/open-ideas/en/contribute.html" class="nav-contribute"><img class="event-calendar" src="/etc/designs/plc/prkc/uk/open-ideas/clientlibs/img/contribute.png">Contribute</a></li>

              <li class="signup-li hidden"><a class="nav-alert" href="/content/plc/prkc/uk/open-ideas/en/signup.html"><img class="event-calendar" src="/etc/designs/plc/prkc/uk/open-ideas/clientlibs/img/email.png">Sign Up</a></li>

            <li class="logged-in-li hidden"><a href="#"><div class="logged-in-name"></div></a>
                 <div class="drop close" id="login-dropdown">

                    <a class="logged-in-link" href="/content/plc/prkc/uk/open-ideas/en/profile.html">My Profile</a>
                    <a class="logged-in-link" href="${navmenu.logoutUrl}"> Log Out </a>

                  </div>
              </li>

               <li class="login-li hidden"><a href="#">Log In</a>
                  <div class="drop close" id="login-dropdown">

                   <form id="header-login-form" action="/bin/open-ideas_security_check" method="post">
                        <input type="hidden" name="resource" value="&#x2f;content&#x2f;plc&#x2f;prkc&#x2f;uk&#x2f;open-ideas&#x2f;en&#x2f;login&#x2f;jcr&#x3a;content&#x2f;form-content&#x2f;login">
                        <input type="hidden" name="redirectTo" value="${navmenu.currentUrl} }">
                        <input type="hidden" name="errorPage" value="/content/plc/prkc/uk/open-ideas/en/login.html">
                        <input type="hidden" name="_charset_" value="UTF-8"/>
                        <input type="hidden" name="returnToPage" value="<%= request.getParameter("returnToPage") != null ? request.getParameter("returnToPage") : "" %>" />
                        Email: <input class="enter-email" type="email" name="email" /><br>
                        <span class="password-text">Password:</span> <input class="enter-password" type="password" name="password"><br>
                        <div class="links clearfix">
                          <a class="forgot-password" href="/content/plc/prkc/uk/open-ideas/en/forgot-password.html">Forgot password?</a>
                          <input class="pearson-button sign-in-button" type="submit" name="sign-in-button" value="Sign In">
                          <div class="clearfix"></div><!-- clear floating elements -->
                        </div>
                    </form>

                  </div>
               </li>
              <li class="share-li"><a href="#" class=" nav-share"><img class="event-calendar" src="/etc/designs/plc/prkc/uk/open-ideas/clientlibs/img/share.png">Share</a>
                  <div class="drop close" id="share-dropdown">
					<span class='st_facebook_large share-item' displayText='Facebook'><span class="share-button-text">Facebook</span></span>
					<span class='st_email_large share-item border-left-gray' displayText='Email'><span class="share-button-text">Email</span></span>
					<span class='st_twitter_large share-item' displayText='Tweet'><span class="share-button-text">Tweet</span></span>
					<span class='st_linkedin_large share-item border-left-gray' displayText='LinkedIn'><span class="share-button-text">LinkedIn</span></span>
					<span class='st_googleplus_large share-item' displayText='Google +'><span class="share-button-text">Google +</span></span>
					<span class='st_pinterest_large share-item border-left-gray' displayText='Pinterest'><span class="share-button-text">Pinterest</span></span>
				  </div>
              </li>
          </ul>
      </nav>
   </header>
</div>