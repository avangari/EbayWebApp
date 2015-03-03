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
</head>
<body onload="start()">
    <h1> search  </h1>
    <form action="/eBay/search" method="GET" name="myForm" onsubmit="return validate()">
            Keyword Search: <input type="text" name="q" id="q" autocomplete="off"/> <br />
            <input type="hidden" name="numResultsToSkip" value="0"/>
            <input type="hidden" name="numResultsToReturn" value="20"/><br/>
            <input type="submit"/>
    </form>
    <% if ( ( (Boolean)request.getAttribute("url_changed") == true) ||  ( (Boolean)request.getAttribute("number_exception") == true) ) { %>
    <h3> please do not mess with the URL!! </h3>
    <% } else { %>


            <% if ( (Boolean)request.getAttribute("no_result") == true) { %>
            <h3> search again with different keywords </h3>
            <% } else {%>

            
             <div id="listDiv">
    			<h2> The results for the query are: </h2>
        		<% int num = Integer.parseInt(request.getParameter("numResultsToSkip")); 
        			SearchResult[] sr = (SearchResult[])request.getAttribute("sr");
        		for (int i=0;i<sr.length;i++) { %>
            	<li><a href=<%= "\"/eBay/item?itemId=" + sr[i].getItemId() + "\"" %>><%= sr[i].getItemId()+" "+sr[i].getName() %></a></li>
        		<% } %>
    		</div> 
            <%if(Integer.parseInt(request.getParameter("numResultsToSkip")) != 0){%>
                <form action="/eBay/search">
                    <% int curr = Integer.parseInt(request.getParameter("numResultsToSkip"))-20;
                       String qVal = request.getParameter("q");
                    %>
                    <input type="hidden" name="numResultsToSkip" value="<%=curr%>"/>
                    <input type="hidden" name="numResultsToReturn" value="20"/>
                    <input type="hidden" name="q" value="<%=qVal%>"/>
                    <input type="submit" value="Prev">
                </form>
            <%}%> 
            
            <%if(request.getParameter("q") != "" && (Boolean)request.getAttribute("toskip") == true){%>
                <form action="/eBay/search">
                    <% int curr = Integer.parseInt(request.getParameter("numResultsToSkip"))+20;
                       String qVal = request.getParameter("q");
                    %>
                    <input type="hidden" name="numResultsToSkip" value="<%=curr%>"/>
                    <input type="hidden" name="numResultsToReturn" value="20"/>
                    <input type="hidden" name="q" value="<%=qVal%>"/>
                    <input type="submit" value="Next">
                </form>
            <%}%> 
            <% } %>
    <% } %>
</body>
</html>
