<%--
  Copyright 1997-2008 Day Management AG
  Barfuesserplatz 6, 4001 Basel, Switzerland
  All Rights Reserved.

  This software is the confidential and proprietary information of
  Day Management AG, ("Confidential Information"). You shall not
  disclose such Confidential Information and shall use it only in
  accordance with the terms of the license agreement you entered into
  with Day.

  ==============================================================================

  Default body script.

  Draws an empty HTML body.

  ==============================================================================

--%><%@include file="/apps/plc/prkc/uk/open-ideas/global.jsp" %>
<%@page session="false"%>
<cq:include script="bodyStartTag.jsp"/>
    <cq:include script="header.jsp"/>
    
<div class="main-container">
    <div class="main wrapper clearfix">
        <div class="homepage-content-sidebar-wrapper clearfix">
            <cq:include script="logo.jsp"/>
            <article class="article homepage shadow">
                <section class="section">
                    <cq:include script="content.jsp"/>
                    <div class="stories">
                        <cq:include path="par2" resourceType="foundation/components/parsys" />
                    </div>
                </section>
            </article>
            <cq:include script="sidebar.jsp"/>
       </div><!-- end of content-sidebar-wrapper -->
       <cq:include script="footer.jsp"/>
    </div>
</div>  
    		
</body>