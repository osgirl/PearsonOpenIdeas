<%--

  Twitter Panel component.

  This is Pearson twitter panel component.

--%><%
%><%@include file="/apps/plc/prkc/uk/open-ideas/global.jsp"%>
<%@page session="false" %><%
%>
<%

String maxNumberTweets = properties.get("maxNumberTweets", String.class);
if (maxNumberTweets == null){
    // If maxNumberTweets not set, then set to default to be 5.
    maxNumberTweets = "5";
}


// Get Twitter feeds
String[] accounts= properties.get("accounts", String[].class);

if (accounts!= null) {
    String twitterAccounts= "";
    int count = 0;
    for (String account: accounts) { 
        if(account != null && account.length() > 0){      
            twitterAccounts += (count == 0) ? account.trim() : "," + account.trim();
            count++;
        }
    }
%>
<div class="panel" id="twitter-feed-container" data-scroll="true" data-twitter-accounts='<%=twitterAccounts%>' data-tweet-count='<%=maxNumberTweets%>'>
    <p class="loading-twitter-pannel">Loading twitter feeds for accounts. Refresh page to see results</p>
</div>
<%        
}
else{
    out.write("<div>[Add Twitter accounts here]</div>");
}
%>

