<!DOCTYPE html>
<html>
<head>
	<title> Item data </title>
 	 <%@ page import="edu.ucla.cs.cs144.Item" %>
	 <%@ page import="edu.ucla.cs.cs144.Bid" %>
	<link rel="stylesheet" type="text/css" href="styles.css">
    <meta name="viewport" content="initial-scale=1.0, user-scalable=no" /> 
     
	<script type="text/javascript" 
 	   src="http://maps.google.com/maps/api/js?sensor=false"> 
	</script> 
	<script type="text/javascript"> 
  		function initialize() { 
  			var lat = Number( document.getElementById('latitude').textContent );
  			var lon = Number( document.getElementById('longitude').textContent );
    		var latlng = new google.maps.LatLng(lat,lon); 
    		var myOptions = { 
      			zoom: 14, // default is 8  
      			center: latlng, 
      			mapTypeId: google.maps.MapTypeId.ROADMAP 
   		 	}; 
    		var map = new google.maps.Map(document.getElementById("map-canvas"), 
        				myOptions); 
    		
    		var marker = new google.maps.Marker({
    		    position: map.getCenter(),
    		    map: map,
    		    title: 'Click to zoom'
    		  });
    		
    		google.maps.event.addListener(map, 'mouseover', function() {
      		   
    			document.getElementById("map-canvas").style.width = '100%';
    			
    			google.maps.event.trigger(map, "resize");
    			
    		  });
    		

    		google.maps.event.addListener(map, 'mouseout', function() {
      		   
    			document.getElementById("map-canvas").style.width = '25%';
    			
    			google.maps.event.trigger(map, "resize");
    			
    		  });
    		
    		
  		} 
  		
  		
  		
	</script> 
    
    
    
</head>
<body onload="initialize()">
	<h1>Search for new item</h1>
	<form action="/eBay/item" method="GET">
		ItemID: <input type="text" name="itemId" id="itemId"/> <br />
		<input type="submit"/>
	</form>
	<% Item item = (Item)request.getAttribute("item"); %>
	<p><strong>ID: </strong><%= item.getItem_ID() %></p>
	<p><strong>Item: </strong><%= item.getName() %></p>
	<p><strong>Current Bid: </strong>$<%= item.getCurrent_bid() %></p>
	<p><strong>First Bid: </strong>$<%= item.getFirst_bid() %></p>
	<p><strong>Number of Bids: </strong><%= item.getNo_of_bids() %></p>
	<%if(item.getLocation() != ""){%>
	  	<p><strong>Location: </strong><%= item.getLocation()%></p>
	<%}%>
	<p><strong>Country: </strong><%= item.getCountry() %></p>	

	<%if(item.getLatitude() != ""){%>
	  	<strong>Latitude: </strong><p id="latitude"><%= item.getLatitude()%></p>
	<%}%>	
	
	<%if(item.getLongitude() != ""){%>
	  	<strong>Longitude: </strong><p id="longitude"><%= item.getLongitude()%></p>
	<%}%>	
	<div id = "map-canvas" > </div>

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
