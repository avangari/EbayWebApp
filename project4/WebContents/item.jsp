<!DOCTYPE html>
<html>
<head>
	<title> Item data </title>
 	 <%@ page import="edu.ucla.cs.cs144.Item" %>
	 <%@ page import="edu.ucla.cs.cs144.Bid" %>
	<link rel="stylesheet" type="text/css" href="styles.css">
    <meta name="viewport" content="initial-scale=1.0, user-scalable=no" /> 
	<script type="text/javascript" 
 	   src="http://maps.google.com/maps/api/js?sensor=false&key=AIzaSyCizZ44XO3l_pPhWxQ3tw7kpR0GqIW6GCI"> 
	</script> 
    <script src="googleMapAPI.js"></script>
    <script src="validate.js" type="text/javascript" ></script>
    <link rel="stylesheet" type="text/css" href="searchStyle.css" /> 
</head>
<body onload="initialize()" id="itemBody">
  <form id="searchbox" action="/eBay/item" method="GET" onsubmit="return validateForm()" name="myForm">
    <input type="text" name="itemId" id="itemId" placeholder="Search Item ID..."/>
    <input id="submit" type="submit" value='Search'/>
  </form>
  <div class="leftContent">
        <% if ( (Boolean)request.getAttribute("no_item") == true) { %>
        <h3> no item found for that id </h3>
        <% } else { %> 
      	<% Item item = (Item)request.getAttribute("item"); %>
      	<p><strong>ID: </strong><%= item.getItem_ID() %></p>
      	<p><strong>Item: </strong><%= item.getName() %></p>
      	<% if(item.getBuy_price() != -1){%>
      	    <strong>Buy_price:</strong>$<p id="buy_price"><%= item.getBuy_price()%></p>
      	<%} else {%>
      	<strong>Buy_price:</strong><p id="buy_price"><i>buy price not available</i></p>
      	<% } %>
      	<p><strong>Current Bid: </strong>$<%= item.getCurrent_bid() %></p>
      	<p><strong>First Bid: </strong>$<%= item.getFirst_bid() %></p>

      	<p><strong>Number of Bids: </strong><%= item.getNo_of_bids() %></p>
      	
      	<strong>Location: </strong><p id="location"><%= item.getLocation()%></p><br/><br/>
      	
      	
      	<strong>Country: </strong><p id = "country"><%= item.getCountry() %></p><br/><br/>	
      	
      	<%if(item.getLatitude() != ""){%>
      	    <strong>Latitude: </strong><p id="latitude"><%= item.getLatitude()%></p><br/><br/>
      	<%}%>	
      	
      	<%if(item.getLongitude() != ""){%>
      	  	<strong>Longitude: </strong><p id="longitude"><%= item.getLongitude()%></p><br/>
      	<%}%>	

      	<p><strong>Start Date: </strong><%= item.getStarted() %></p>
      	<p><strong>End Date: </strong><%= item.getEnds() %></p>
      	<p><strong>Seller ID: </strong><%= item.getSeller_id() %></p>
      	<p><strong>Seller Rating: </strong><%= item.getSeller_rating() %></p>
        <% if(item.getCategoryLength() > 0) {%> 
          <p><strong>Categories: </strong>
            <%for(String s: item.getCategories()){%>
              <%=s+ " | "%>
            <%}%>
          </p>
        <%}%> 
      	<p><strong>Item Description: </strong><%= item.getDecription() %></p><br/>
      </div>
      <% if(item.getBidLength() > 0) {%> 
    	<div class="rightContent">
        <p><strong>Bids:</strong></p>
          
    	    <table class="bidTable">
    		    <tr>
    		        <th>Bidder ID</th>
    		        <th>Bidder Rating</th>
    		        <th>Bid Amount</th>
    		        <th>Date and time</th>
    		       	<th>Bidder Location</th>
    		        <th>Bidder Country</th>
    		    </tr>
    			<% for(Bid b : item.getBids()) {%>
    				<tr>
    					<td><%= b.getBidder_id()%></td>
    					<td><%= b.getBidder_rating()%></td>
    					<td>$<%= b.getAmount()%></td>
    					<td><%= b.getBid_date()%></td>

    					  	<td><%= b.getLocation()%></td>
    					
    					
    					
    					  	<td><%= b.getCountry()%></td>
    					
    				</tr>
    	<%}%>
    		</table><br/><br/>
    	<%}%>
      </div>
      <div class="rightContent" id = "map-canvas" > </div>
      <% } %>

      
</body>
</html>
