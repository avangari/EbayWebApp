<!DOCTYPE html>
<html>
<head>
	<title> Item data </title>
 	 <%@ page import="edu.ucla.cs.cs144.Item" %>
 	 <link rel="stylesheet" type="text/css" href="styles.css">
</head>

<body>
<% HttpSession this_session = request.getSession(true); %>
<% Item item = (Item)this_session.getAttribute("item"); %>
<p><strong>ID: </strong><%= item.getItem_ID() %></p>
<p><strong>Item: </strong><%= item.getName() %></p>
<strong>Buy_price:</strong>$<p id="buy_price"><%= item.getBuy_price()%></p>

<form action="/eBay/confirm" method="GET" name="buyForm" >
	<input type="text" id="credit" name="credit" placeholder="enter credit card info">
	<input type="submit" value="pay now" > </input>
</form>






</body>
</html>