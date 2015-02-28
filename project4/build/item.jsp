<html>
<head>
	<title> Item data </title>
 	<%@ page import="edu.ucla.cs.cs144.Item" %>
	<%@ page import="edu.ucla.cs.cs144.Bid" %>
	<link rel="stylesheet" type="text/css" href="styles.css">
	<script type="text/javascript" src="http://maps.google.com/maps/api/js?sensor=false"> </script> 
	<script type="text/javascript"> 
	  var lat = 0;
	  var log = 0;
	  function initialize() { 
	  	//lat = document.getElementById("lat").innerHTML;
	  	//log = document.getElementById("log").innerHTML;
	  	alert("beforeCall");
	    getLatLngFromAddress("Los Angeles", "USA");
	    // alert(lat+" "log);
	    // var latlng = new google.maps.LatLng(lat,log); 
	    // var myOptions = { 
	    //   zoom: 14, // default is 8  
	    //   center: latlng, 
	    //   mapTypeId: google.maps.MapTypeId.ROADMAP 
	    // }; 
	    // var map = new google.maps.Map(document.getElementById("map_canvas"), 
	    //     myOptions); 
	  }
	  function getLatLngFromAddress(city, country)
	  {
		  var address = city +", "+ country;
		  var geocoder = new google.maps.Geocoder();
		  geocoder.geocode( { 'address': address}, function(results, status) 
		  {
		    if (status == google.maps.GeocoderStatus.OK) 
		    {
		      alert(val(results[0].geometry.location.Pa));
		      //log = val(results[0].geometry.location.Qa);
		      //alert(lat+" "+log);
		    } 
		    else 
		    {
		      alert("Geocode was not successful for the following reason: " + status);
		    }
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
			</table> <br/><br/>
			<%}%>
			<div id="map_canvas" style="width:100%; height:50%"></div> 
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
