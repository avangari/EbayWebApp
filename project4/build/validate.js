function validateForm() 
			{
   				 var x = document.forms["myForm"]["itemId"].value;
   				 if (x == null || x == "") 
   				 {
       				alert("fill in the itemId");
        			return false;
        		 }

        		 var num = parseInt(x);
        		 if(isNaN(x))
        		 {
        		 	alert("please enter a valid item ID. The entered number is not a number");
        		 	return false;
        		 }

        		 
    		}