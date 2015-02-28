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
	<script type="text/javascript"> 
	
		var map;
  		function initialize() 
  		{ 
  			
  			try
  			{
  			 	var lat_value = Number( document.getElementById('latitude').textContent );
  			 	var lon_value = Number( document.getElementById('longitude').textContent );
  			 	if( (lat_value > 90) || (lat_value < -90) || (lon_value > 180) || (lon_value <180) ){
  			 		
  			 		no_coordinates();
  			 		return;
  			 	}
  			 	
  				var latlng = new google.maps.LatLng(lat_value,lon_value); 
    			var myOptions = { 
      				zoom: 10, // default is 8  
      				center: latlng, 
      				mapTypeId: google.maps.MapTypeId.ROADMAP 
   		 		}; 
    		 	 map = new google.maps.Map(document.getElementById("map-canvas"), 
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
  			catch(err){
  				no_coordinates();
  			}
  		}

  			//if no latitude or longitude specified
  			function no_coordinates()
  			{
  				  var geocoder = new google.maps.Geocoder();
  				  var latlng = new google.maps.LatLng(0, 0);
  				  var mapOptions = {
  				    zoom: 1,
  				    center: latlng
  				  }
  				   map = new google.maps.Map(document.getElementById('map-canvas'), mapOptions);
  				  
  				var location = document.getElementById('location').textContent;
  				var country = document.getElementById('country').textContent;
  				var address = location+" "+country ;
  				  geocoder.geocode( { 'address': address}, function(results, status) {
  				    if (status == google.maps.GeocoderStatus.OK) {
  				      map.setCenter(results[0].geometry.location);
  				      map.setZoom(14);
  				      var marker = new google.maps.Marker({
  				          map: map,
  				          position: results[0].geometry.location
  				      });
  				    } else {
  				      alert('Geocode was not successful for the following reason: ' + status);
  				    }
  				  });
  				  
  				  
  				google.maps.event.addListener(map, 'mouseover', function() {
  	       		   
    				document.getElementById("map-canvas").style.width = '100%';
    			
    				google.maps.event.trigger(map, "resize");
    			
    		 	 });
    		

    			google.maps.event.addListener(map, 'mouseout', function() {
      		   
    				document.getElementById("map-canvas").style.width = '25%';
    			
    				google.maps.event.trigger(map, "resize");
    			
    		  	});
  				
  			
	</script> 
</head>
<body onload="initialize()">
	<h1>Search for new item</h1>
	<form action="/eBay/item" method="GET">
		ItemID: <input type="text" name="itemId" id="itemId"/> <br />
		<input type="submit"/>
	</form>
	<% Item item = (Item)request.getAttribute("item"); %>
	<div id="rightContent">
		<% if(item.getBidLength() > 0) {%>
	    	<p><strong>Bids:</strong></p>   
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
						  	<td><%= b.getLocation()%></td>
						  	<td><%= b.getCountry()%></td>
					</tr>
				<%}%>
				</table> <br/><br/>
			<%}%>
			<div id = "map-canvas" > </div>
	</div>
	<div id="leftContent">
		<p><strong>Item: </strong><%= item.getName() %></p>
		<%if(item.getBuy_price() >= 0.0){%>
			<p><strong>Buy Price: </strong>$<%= item.getBuy_price() %></p>
		<%}else{%>
			<p><strong>Buy Price: </strong><i>unavailable</i></p>
			<%}%>
		<p><strong>Current Bid: </strong>$<%= item.getCurrent_bid() %></p>
		<p><strong>First Bid: </strong>$<%= item.getFirst_bid() %></p>
		<p><strong>Number of Bids: </strong><%= item.getNo_of_bids() %></p>
		<%if(item.getLocation() != ""){%>
		  	<p><strong>Location: </strong><%= item.getLocation()%></p>
		<%}%>
		<p><strong>Country: </strong><%= item.getCountry() %></p>	

		<%if(item.getLatitude() != ""){%>
		  	<strong>Latitude: </strong><p id="lat"><%= item.getLatitude()%></p>
		<%}%>	
		
		<%if(item.getLongitude() != ""){%>
		  	<strong>Longitude: </strong><p id="log"><%= item.getLongitude()%></p>
		<%}%>	
		<p><strong>Start Date: </strong><%= item.getStarted() %></p>
		<p><strong>End Date: </strong><%= item.getEnds() %></p>
		<p><strong>Seller ID: </strong><%= item.getSeller_id() %></p>
		<p><strong>Seller Rating: </strong><%= item.getSeller_rating() %></p>
		<p><strong>Item Description: </strong><%= item.getDecription() %></p><br/>
	</div>
</body>
</html>
