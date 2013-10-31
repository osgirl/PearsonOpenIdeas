<%--

  Editors Choice Component component.

  This component pulls the most recent editors choice article for display

--%><%
%><%@include file="/apps/plc/prkc/uk/open-ideas/global.jsp"%>
<%@page session="false" import="com.pearson.openideas.cq5.components.content.EditorsChoice" %>
<%
	EditorsChoice editorsChoice = new EditorsChoice(pageContext);
	pageContext.setAttribute("editorsChoice", editorsChoice);
%>
<div class="editorschoice">
<a href="${editorsChoice.url}.html">
        <div class="index-inner">
            <div class="index-innerback">EDITORS CHOICE <img src="/etc/designs/plc/prkc/uk/open-ideas/clientlibs/img/star.png" alt="star"/></div>
        </div>

    <c:if test="${not empty editorsChoice.image}">
       <% editorsChoice.getImage().draw(out); %>
    </c:if>

        <div class="index-inner2">
            <div class="tag">
                <p class="taghead">${editorsChoice.category}</p>
                <p class="tagcontent">${editorsChoice.theme} <img src="/etc/designs/plc/prkc/uk/open-ideas/clientlibs/img/tag.png" alt="tag"/></p>
            </div>
            <div class="mainsum">
              <c:if test="${not empty editorsChoice.title}">
              ${editorsChoice.title}
              </c:if>
            </div>
        </div>
        </a>
</div><!--end of editorschoice -->