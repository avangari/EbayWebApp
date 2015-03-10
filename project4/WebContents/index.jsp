<!DOCTYPE html>
<html>
    <head><title>eBay Search</title></head>
    <body>
    	<h1>eBay Search Web Site</h1>
    	<br />
    	<% if (request.isSecure()){ %>
    	<% response.sendRedirect("http://localhost:1448/eBay"); }%>
    	<h3> <a href="keywordSearch.html" > click here </a> to search for an item by keyword <h3>
    	<br />
    	<h3> <a href= "getItem.html" > clicke here </a> to search for an item by item ID </h3>
    </body>
</html>
