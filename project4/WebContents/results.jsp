<html>
<head>
    <title>Search Results</title>
    <%@ page import="edu.ucla.cs.cs144.SearchResult" %>
</head>
<body>
    <h1> The results for the query are: </h1>
    <%  SearchResult[] sr = (SearchResult[])request.getAttribute("sr");
	for (int i=0;i<sr.length;i++) { %>
   	<p><%= i+1 %>---------><label>id:</label><%= sr[i].getItemId() %>&nbsp; &nbsp;<label>Name:</label><%= sr[i].getName() %></p>
    <% } %> 
    
    
</body>
</html>
