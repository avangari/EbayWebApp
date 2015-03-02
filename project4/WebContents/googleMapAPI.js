	
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
  				    }
  				  });
  			}  	