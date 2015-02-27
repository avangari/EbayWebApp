<html>
<head>
	<title> Item data </title>
 	 <%@ page import="edu.ucla.cs.cs144.Item" %>
	 <%@ page import="edu.ucla.cs.cs144.Bid" %>
	 <link rel="stylesheet" type="text/css" href="styles.css">
</head>
<body>
	<h1>Search for new item</h1>
	<form action="/eBay/item" method="GET">
		ItemID: <input type="text" name="itemId" id="itemId"/> <br />
		<input type="submit"/>
	</form>
	<% Item item = (Item)request.getAttribute("item"); %>
	<p><strong>Item: </strong><%= item.getName() %></p>
	<p><strong>Current Bid: </strong>$<%= item.getCurrent_bid() %></p>
	<p><strong>First Bid: </strong>$<%= item.getFirst_bid() %></p>
	<p><strong>Number of Bids: </strong><%= item.getNo_of_bids() %></p>
	<%if(item.getLocation() != ""){%>
	  	<p><strong>Location: </strong><%= item.getLocation()%></p>
	<%}%>
	<p><strong>Country: </strong><%= item.getCountry() %></p>	

	<%if(item.getLatitude() != ""){%>
	  	<p><strong>Latitude: </strong><%= item.getLatitude()%></p>
	<%}%>	
	
	<%if(item.getLongitude() != ""){%>
	  	<p><strong>Longitude: </strong><%= item.getLongitude()%></p>
	<%}%>	

	<p><strong>Start Date: </strong><%= item.getStarted() %></p>
	<p><strong>End Date: </strong><%= item.getEnds() %></p>
	<p><strong>Seller ID: </strong><%= item.getSeller_id() %></p>
	<p><strong>Seller Rating: </strong><%= item.getSeller_rating() %></p>
	<p><strong>Item Description: </strong><%= item.getDecription() %></p><br/>
	<p><strong>Bids:</strong></p>
    <% if(item.getBidLength() > 0) {%>   
	    <table class="bidTable">
		    <tr>
		        <th>Bidder ID</th>
		        <th>Bidder Rating</th>
		        <th>Bid Amount</th>
		       	<th>Bidder Location</th>
		        <th>Bidder Country</th>
		    </tr>
			<% for(Bid b : item.getBids()) {%>
				<tr>
					<td><%= b.getBidder_id()%></td>
					<td><%= b.getBidder_id()%></td>
					<td>$<%= b.getAmount()%></td>
					<%if(item.getLocation() != ""){%>
					  	<td><%= b.getLocation()%></td>
					<%}else{%>
					  <td><i>unavailable</i></td> 
					<%}%>	
					
					<%if(item.getCountry() != ""){%>
					  	<td><%= b.getCountry()%></td>
					<%}else{%>
					  <td><i>unavailable</i></td> 
					<%}%>
				</tr>
			<%}%>
		</table>
	<%}%>
</body>
</html>
