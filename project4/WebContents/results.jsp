<html>
<head>
    <title>Search Results</title>
    <%@ page import="edu.ucla.cs.cs144.SearchResult" %>
</head>
<body>
	<h1> search again with different keywords </h1>
	<form action="/eBay/search" method="GET">
    		Keyword Search: <input type="text" name="q" id="q"/> <br />
    		<input type="hidden" name="numResultsToSkip" value="0"/>
			<input type="hidden" name="numResultsToReturn" value="10"/><br/>
    		<input type="submit"/>
    </form>
	
    <h1> The results for the query are: </h1>
    <% int num = Integer.parseInt(request.getParameter("numResultsToSkip")); 
    SearchResult[] sr = (SearchResult[])request.getAttribute("sr");
	for (int i=0;i<sr.length;i++) { %>
   	<p><%= i+num+1 %>) <label>ID: </label><a href = <%= "\"/eBay/item?itemId=" + sr[i].getItemId() + "\"" %>> <%= sr[i].getItemId() %></a>&nbsp; &nbsp;<label>Name:  </label><%= sr[i].getName() %></p>
    <% } %> 
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
</body>
</html>
