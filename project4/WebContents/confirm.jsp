<!DOCTYPE html>
<html>
<head>
	<title> Item data </title>
 	 <%@ page import="edu.ucla.cs.cs144.Item" %>
</head>

<body>

<% if ( (Boolean)request.getAttribute("error") == true ) { %>
<h1> there was some error</h1>
<h3><a href="/eBay"> click here </a> to go to home page </h3>
<% } else { %>
<% HttpSession this_session = request.getSession(true); %>
<% Item item = (Item)this_session.getAttribute("item"); %>
<p><strong>ID: </strong><%= item.getItem_ID() %></p>
<p><strong>Item: </strong><%= item.getName() %></p>
<strong>Buy_price:</strong>$<p id="buy_price"><%= item.getBuy_price()%></p>

<p><Strong>Credit card number: </Strong><%= request.getAttribute("credit_card") %> </p>
<p><Strong> Time purchased : </Strong><%= request.getAttribute("buy_time") %></p>
<% } %>
</body>
</html>
