<!DOCTYPE html>
<html>
<head>
    <title>Search Results</title>
    <%@ page import="edu.ucla.cs.cs144.SearchResult" %>
    <script src="ebayFunctionality.js" type="text/javascript"></script>
    <script type="text/javascript">
        function validate()
        {
            var x = document.forms["myForm"]["q"].value;
            if (x == null || x == "") 
                 {
                    alert("fill in the query box");
                    return false;
                 }
        }
    </script>
    <link rel="stylesheet" type="text/css" href="autosuggest.css" />  
    <link rel="stylesheet" type="text/css" href="styles.css" />
    <link rel="stylesheet" type="text/css" href="searchStyle.css" /> 
</head>
<body onload="start()" id="resultBody">
    <form id="searchbox" action="/eBay/search" method="GET" name="myForm" onsubmit="return validate()">
            <input type="text" name="q" id="q" autocomplete="off" placeholder="Search Keyword..."/>
            <input type="hidden" name="numResultsToSkip" value="0"/>
            <input type="hidden" name="numResultsToReturn" value="20"/>
            <input type="submit" id="submit" value='Search'/>
    </form>
    <%if((Boolean)request.getAttribute("missing_query") == true){%>
        <h1>Missing query, don't mess with the url!</h1>
    <%}else{%>

    <% if ( ( (Boolean)request.getAttribute("url_changed") == true) ||  ( (Boolean)request.getAttribute("number_exception") == true) ) { %>
        <h3> please do not mess with the URL!! </h3>
    
    <% } else { %>
            <% if ( (Boolean)request.getAttribute("no_result") == true) { %>
            <h3> search again with different keywords </h3>
            <% } else {%>

            
             <div id="listDiv">
        		<% int num = Integer.parseInt(request.getParameter("numResultsToSkip")); 
        			SearchResult[] sr = (SearchResult[])request.getAttribute("sr");
        		for (int i=0;i<sr.length;i++) { %>
            	<li><a href=<%= "\"/eBay/item?itemId=" + sr[i].getItemId() + "\"" %>><%= sr[i].getItemId()+":  "+sr[i].getName() %></a></li>
        		<% } %>
    		</div> 
            <%if(Integer.parseInt(request.getParameter("numResultsToSkip")) != 0){%>
                <div id="prev">
                <form action="/eBay/search" id="prevForm">
                    <% int curr = Integer.parseInt(request.getParameter("numResultsToSkip"))-20;
                       String qVal = request.getParameter("q");
                    %>
                    <input type="hidden" name="numResultsToSkip" value="<%=curr%>"/>
                    <input type="hidden" name="numResultsToReturn" value="20"/>
                    <input type="hidden" name="q" value="<%=qVal%>"/>
                    <input class="styled-button-10" type="submit" value="Prev">
                </form>
                </div>
            <%}%> 
            
            <%if(request.getParameter("q") != "" && (Boolean)request.getAttribute("toskip") == true){%>
                <div id="next">
                <form action="/eBay/search" id="nextForm">
                    <% int curr = Integer.parseInt(request.getParameter("numResultsToSkip"))+20;
                       String qVal = request.getParameter("q");
                    %>
                    <input type="hidden" name="numResultsToSkip" value="<%=curr%>"/>
                    <input type="hidden" name="numResultsToReturn" value="20"/>
                    <input type="hidden" name="q" value="<%=qVal%>"/>
                    <input class="styled-button-10" type="submit" value="Next">
                </form>
                </div>
            <%}%> 
            <% } %>
    <% } %>
    <%}%>
</body>
</html>
