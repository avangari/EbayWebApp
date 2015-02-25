<html>
<head>
	<title> Item data </title>
<!--  	 <%@ page import="edu.ucla.cs.cs144.Item" %>
	 <%@ page import="edu.ucla.cs.cs144.Bid" %> -->
</head>
<body>
<h1> Item Results </h1>
<p><%= request.getAttribute("Name") %></p>
<p><%= request.getAttribute("Started") %></p>
<p><%= request.getAttribute("Currently") %></p>
<p><%= request.getAttribute("First_Bid") %></p>
</body>
</html>
