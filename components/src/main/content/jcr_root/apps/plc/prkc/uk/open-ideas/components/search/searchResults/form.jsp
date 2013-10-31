<%@include file="/apps/plc/prkc/uk/open-ideas/global.jsp"%>
<form method="GET" action="/content/plc/prkc/uk/open-ideas/en/search.html">
    <input id="searchbox" class="no-search-results-input text" name="searchbox" type="text" value="${fn:escapeXml(searchResults.searchText)}" placeholder="${searchResults.searchText}" />
    <select id="no-search-results-dropdown" name="searchType">
        <option ${"All words" eq searchResults.searchType ? 'selected' : ''}>All words</option>
        <option ${"Any words" eq searchResults.searchType ? 'selected' : ''}>Any words</option>
        <option ${"Exact phrase" eq searchResults.searchType ? 'selected' : ''}>Exact phrase</option>
    </select>
    <input class="no-search-results-button pearson-button-dark" name="searchterm" type="submit" value="Submit" />
</form>