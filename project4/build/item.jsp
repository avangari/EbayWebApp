<html>
<head>
	<title> Item data </title>
 	 <%@ page import="edu.ucla.cs.cs144.Item" %>
	 <%@ page import="edu.ucla.cs.cs144.Bid" %>
</head>
<body>

<% Item item = (Item)request.getAttribute("item"); %>
<p><%= item.printAll() %> </p>
</body>
</html>
