function validate_card() 
			{
   				 var x = document.forms["buyForm"]["credit"].value;
   				 if (x == null || x == "") 
   				 {
       				alert("fill in the credit card information");
        			return false;
        		 }

        		 var num = parseInt(x);
        		 if(isNaN(x))
        		 {
        		 	alert("please enter a valid credit card number. The entered string is not a number");
        		 	return false;
        		 }

             var re16digit = /^\d{16}$/;
             if (!re16digit.test(x)) {
             alert("Please enter your 16 digit credit card numbers");
             return false;
             }
        		 
    		}