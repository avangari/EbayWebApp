<!DOCTYPE html>
<html>
<head>
	<title> Item data </title>
 	 <%@ page import="edu.ucla.cs.cs144.Item" %>
 	 <link rel="stylesheet" type="text/css" href="styles.css">
 	 <script src="validate_card.js" type="text/javascript">
 	 </script>

</head>

<body>
<% HttpSession this_session = request.getSession(true); %>
<% Item item = (Item)this_session.getAttribute("item"); %>
<p><strong>ID: </strong><%= item.getItem_ID() %></p>
<p><strong>Item: </strong><%= item.getName() %></p>
<strong>Buy_price:</strong>$<p id="buy_price"><%= item.getBuy_price()%></p>
<p><strong>Seller ID: </strong><%= item.getSeller_id() %></p>
<p><strong>Seller Rating: </strong><%= item.getSeller_rating() %></p>

<form action="/eBay/confirm" method="GET" name="buyForm" onsubmit = "return validate_card()">
	<input type="text" id="credit" name="credit" placeholder="enter credit card info">
	<input type="submit" value="pay now" > </input>
</form>


</body>
</html>