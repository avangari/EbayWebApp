<html>
<head>
	<title> Item data </title>
 	 <%@ page import="edu.ucla.cs.cs144.Item" %>
	 <%@ page import="edu.ucla.cs.cs144.Bid" %>
</head>
<body>
    	<h1>Search for new item</h1>
    	<form action="/eBay/item" method="GET">
    		ItemID: <input type="text" name="itemId" id="itemId"/> <br />
    		<input type="submit"/>
    	</form>
    

<% Item item = (Item)request.getAttribute("item"); %>
<p><%= item.printAll() %> </p>
</body>
</html>
