		var states = [];
		function sendAjaxRequest(input)
		{
			var request = "suggest?q="+encodeURI(input);
			xmlHttp.open("GET",request);
			xmlHttp.onreadystatechange = requestSuggestions();
			xmlHttp.send(null);
		}
		
		function showSuggestion()
		{
			 if ( (xmlHttp.readyState == 4) && (xmlHttp.status === 200) )
				 {
				// get the CompleteSuggestion elements from the response
				    states = [];
				    var s = xmlHttp.responseXML.getElementsByTagName('CompleteSuggestion');
				 	
				    // construct a bullet list from the suggestions
				    //var htmlCode = "<ul>";
				    for (i = 0; i < s.length; i++) {
				       var text = s[i].childNodes[0].getAttribute("data");
				       states.push(text);
				       //htmlCode += "<li><b>" + text + "</b></li>";
				    }
				    //htmlCode += "</ul>";

				    // display the list on the page
				    //document.getElementById("suggestion").innerHTML = htmlCode;
				 }
		}
		/**
		 * An autosuggest textbox control.
		 * @class
		 * @scope public
		 */
		function AutoSuggestControl(oTextbox /*:HTMLInputElement*/, 
		                            oProvider /*:SuggestionProvider*/) {
		    
		    /**
		     * The currently selected suggestions.
		     * @scope private
		     */   
		    this.cur /*:int*/ = -1;

		    /**
		     * The dropdown list layer.
		     * @scope private
		     */
		    this.layer = null;
		    
		    /**
		     * Suggestion provider for the autosuggest feature.
		     * @scope private.
		     */
		    this.provider /*:SuggestionProvider*/ = oProvider;
		    
		    /**
		     * The textbox to capture.
		     * @scope private
		     */
		    this.textbox /*:HTMLInputElement*/ = oTextbox;
		    
		    //initialize the control
		    this.init();
		    
		}

		/**
		 * Autosuggests one or more suggestions for what the user has typed.
		 * If no suggestions are passed in, then no autosuggest occurs.
		 * @scope private
		 * @param aSuggestions An array of suggestion strings.
		 * @param bTypeAhead If the control should provide a type ahead suggestion.
		 */
		AutoSuggestControl.prototype.autosuggest = function (aSuggestions /*:Array*/,
		                                                     bTypeAhead /*:boolean*/) {
		    
		    //make sure there's at least one suggestion
		    if (aSuggestions.length > 0) {
		        if (bTypeAhead) {
		           this.typeAhead(aSuggestions[0]);
		        }
		        
		        this.showSuggestions(aSuggestions);
		    } else {
		        this.hideSuggestions();
		    }
		};

		/**
		 * Creates the dropdown layer to display multiple suggestions.
		 * @scope private
		 */
		AutoSuggestControl.prototype.createDropDown = function () {

		    var oThis = this;

		    //create the layer and assign styles
		    this.layer = document.createElement("div");
		    this.layer.className = "suggestions";
		    this.layer.style.visibility = "hidden";
		    this.layer.style.width = "400px";
		    
		    //when the user clicks on the a suggestion, get the text (innerHTML)
		    //and place it into a textbox
		    this.layer.onmousedown = 
		    this.layer.onmouseup = 
		    this.layer.onmouseover = function (oEvent) {
		        oEvent = oEvent || window.event;
		        oTarget = oEvent.target || oEvent.srcElement;

		        if (oEvent.type == "mousedown") {
		            oThis.textbox.value = oTarget.firstChild.nodeValue;
		            oThis.hideSuggestions();
		        } else if (oEvent.type == "mouseover") {
		            oThis.highlightSuggestion(oTarget);
		        } else {
		            oThis.textbox.focus();
		        }
		    };
		    
		    
		    document.body.appendChild(this.layer);
		};

		/**
		 * Gets the left coordinate of the textbox.
		 * @scope private
		 * @return The left coordinate of the textbox in pixels.
		 */
		AutoSuggestControl.prototype.getLeft = function () /*:int*/ {

		    var oNode = this.textbox;
		    var iLeft = 0;
		    
		    while(oNode.tagName != "BODY") {
		        iLeft += oNode.offsetLeft;
		        oNode = oNode.offsetParent;        
		    }
		    
		    return iLeft;
		};

		/**
		 * Gets the top coordinate of the textbox.
		 * @scope private
		 * @return The top coordinate of the textbox in pixels.
		 */
		AutoSuggestControl.prototype.getTop = function () /*:int*/ {

		    var oNode = this.textbox;
		    var iTop = 0;
		    
		    while(oNode.tagName != "BODY") {
		        iTop += oNode.offsetTop;
		        oNode = oNode.offsetParent;
		    }
		    
		    return iTop;
		};

		/**
		 * Handles three keydown events.
		 * @scope private
		 * @param oEvent The event object for the keydown event.
		 */
		AutoSuggestControl.prototype.handleKeyDown = function (oEvent /*:Event*/) {

		    switch(oEvent.keyCode) {
		        case 38: //up arrow
		            this.previousSuggestion();
		            break;
		        case 40: //down arrow 
		            this.nextSuggestion();
		            break;
		        case 13: //enter
		            this.hideSuggestions();
		            break;
		    }

		};

		/**
		 * Handles keyup events.
		 * @scope private
		 * @param oEvent The event object for the keyup event.
		 */
		AutoSuggestControl.prototype.handleKeyUp = function (oEvent /*:Event*/) {

		    var iKeyCode = oEvent.keyCode;

		    //for backspace (8) and delete (46), shows suggestions without typeahead
		    if (iKeyCode == 8 || iKeyCode == 46) {
		        this.provider.requestSuggestions(this, false);
		        
		    //make sure not to interfere with non-character keys
		    } else if (iKeyCode < 32 || (iKeyCode >= 33 && iKeyCode < 46) || (iKeyCode >= 112 && iKeyCode <= 123)) {
		        //ignore
		    } else {
		        //request suggestions from the suggestion provider with typeahead
		        this.provider.requestSuggestions(this,false);
		    }
		};

		/**
		 * Hides the suggestion dropdown.
		 * @scope private
		 */
		AutoSuggestControl.prototype.hideSuggestions = function () {
		    this.layer.style.visibility = "hidden";
		};

		/**
		 * Highlights the given node in the suggestions dropdown.
		 * @scope private
		 * @param oSuggestionNode The node representing a suggestion in the dropdown.
		 */
		AutoSuggestControl.prototype.highlightSuggestion = function (oSuggestionNode) {
		    
		    for (var i=0; i < this.layer.childNodes.length; i++) {
		        var oNode = this.layer.childNodes[i];
		        if (oNode == oSuggestionNode) {
		            oNode.className = "current"
		        } else if (oNode.className == "current") {
		            oNode.className = "";
		        }
		    }
		};

		/**
		 * Initializes the textbox with event handlers for
		 * auto suggest functionality.
		 * @scope private
		 */
		AutoSuggestControl.prototype.init = function () {

		    //save a reference to this object
		    var oThis = this;
		    
		    //assign the onkeyup event handler
		    this.textbox.onkeyup = function (oEvent) {
		    	//alert("keyup");
		    	//sendAjaxRequest(this.value);
		        //check for the proper location of the event object
		        
		        if (!oEvent) {
		            oEvent = window.event;
		        }    
		        
		        //call the handleKeyUp() method with the event object
		        oThis.handleKeyUp(oEvent);
		    };
		    
		    //assign onkeydown event handler
		    this.textbox.onkeydown = function (oEvent) {
		    
		        //check for the proper location of the event object
		        if (!oEvent) {
		            oEvent = window.event;
		        }    
		        
		        //call the handleKeyDown() method with the event object
		        oThis.handleKeyDown(oEvent);
		    };
		    
		    //assign onblur event handler (hides suggestions)    
		    this.textbox.onblur = function () {
		        oThis.hideSuggestions();
		    };
		    
		    //create the suggestions dropdown
		    this.createDropDown();
		};

		/**
		 * Highlights the next suggestion in the dropdown and
		 * places the suggestion into the textbox.
		 * @scope private
		 */
		AutoSuggestControl.prototype.nextSuggestion = function () {
		    var cSuggestionNodes = this.layer.childNodes;

		    if (cSuggestionNodes.length > 0 && this.cur < cSuggestionNodes.length-1) {
		        var oNode = cSuggestionNodes[++this.cur];
		        this.highlightSuggestion(oNode);
		        this.textbox.value = oNode.firstChild.nodeValue; 
		    }
		};

		/**
		 * Highlights the previous suggestion in the dropdown and
		 * places the suggestion into the textbox.
		 * @scope private
		 */
		AutoSuggestControl.prototype.previousSuggestion = function () {
		    var cSuggestionNodes = this.layer.childNodes;

		    if (cSuggestionNodes.length > 0 && this.cur > 0) {
		        var oNode = cSuggestionNodes[--this.cur];
		        this.highlightSuggestion(oNode);
		        this.textbox.value = oNode.firstChild.nodeValue;   
		    }
		};

		/**
		 * Selects a range of text in the textbox.
		 * @scope public
		 * @param iStart The start index (base 0) of the selection.
		 * @param iLength The number of characters to select.
		 */
		AutoSuggestControl.prototype.selectRange = function (iStart /*:int*/, iLength /*:int*/) {

		    //use text ranges for Internet Explorer
		    if (this.textbox.createTextRange) {
		        var oRange = this.textbox.createTextRange(); 
		        oRange.moveStart("character", iStart); 
		        oRange.moveEnd("character", iLength - this.textbox.value.length);      
		        oRange.select();
		        
		    //use setSelectionRange() for Mozilla
		    } else if (this.textbox.setSelectionRange) {
		        this.textbox.setSelectionRange(iStart, iLength);
		    }     

		    //set focus back to the textbox
		    this.textbox.focus();      
		}; 

		/**
		 * Builds the suggestion layer contents, moves it into position,
		 * and displays the layer.
		 * @scope private
		 * @param aSuggestions An array of suggestions for the control.
		 */
		AutoSuggestControl.prototype.showSuggestions = function (aSuggestions /*:Array*/) {
		    
		    var oDiv = null;
		    this.layer.innerHTML = "";  //clear contents of the layer
		    
		    for (var i=0; i < aSuggestions.length; i++) {
		        oDiv = document.createElement("div");
		        oDiv.appendChild(document.createTextNode(aSuggestions[i]));
		        this.layer.appendChild(oDiv);
		    }
		    
		    this.layer.style.left = this.getLeft() + "px";
		    this.layer.style.top = (this.getTop()+this.textbox.offsetHeight) + "px";
		    this.layer.style.visibility = "visible";

		};

		/**
		 * Inserts a suggestion into the textbox, highlighting the 
		 * suggested part of the text.
		 * @scope private
		 * @param sSuggestion The suggestion for the textbox.
		 */
		AutoSuggestControl.prototype.typeAhead = function (sSuggestion /*:String*/) {

		    //check for support of typeahead functionality
		    if (this.textbox.createTextRange || this.textbox.setSelectionRange){
		        var iLen = this.textbox.value.length; 
		        this.textbox.value = sSuggestion; 
		        this.selectRange(iLen, sSuggestion.length);
		    }
		};

				/**
		 * Provides suggestions for state names (USA).
		 * @class
		 * @scope public
		 */
		function StateSuggestions() {
		    // this.states = [
		    //     "Alabama", "Alaska", "Arizona", "Arkansas",
		    //     "California", "Colorado", "Connecticut",
		    //     "Delaware", "Florida", "Georgia", "Hawaii",
		    //     "Idaho", "Illinois", "Indiana", "Iowa",
		    //     "Kansas", "Kentucky", "Louisiana",
		    //     "Maine", "Maryland", "Massachusetts", "Michigan", "Minnesota", 
		    //     "Mississippi", "Missouri", "Montana",
		    //     "Nebraska", "Nevada", "New Hampshire", "New Mexico", "New York",
		    //     "North Carolina", "North Dakota", "Ohio", "Oklahoma", "Oregon", 
		    //     "Pennsylvania", "Rhode Island", "South Carolina", "South Dakota",
		    //     "Tennessee", "Texas", "Utah", "Vermont", "Virginia", 
		    //     "Washington", "West Virginia", "Wisconsin", "Wyoming"  
		    // ];
		}

		/**
		 * Request suggestions for the given autosuggest control. 
		 * @scope protected
		 * @param oAutoSuggestControl The autosuggest control to provide suggestions for.
		 */
		StateSuggestions.prototype.requestSuggestions = function (oAutoSuggestControl /*:AutoSuggestControl*/,
		                                                          bTypeAhead /*:boolean*/) {
		    var aSuggestions = [];
		    var sTextboxValue = oAutoSuggestControl.textbox.value;
		    
		    if (sTextboxValue.length > 0){
		    	var xmlHttp = new XMLHttpRequest();
			    var request = "suggest?q="+encodeURI(sTextboxValue);
				xmlHttp.open("GET",request);
				//xmlHttp.onreadystatechange = requestSuggestions();
				xmlHttp.send(null);

				xmlHttp.onreadystatechange=function()
				{
					if ( (xmlHttp.readyState == 4) && (xmlHttp.status === 200) )
					 {
					// get the CompleteSuggestion elements from the response
					    states = [];
					    var s = xmlHttp.responseXML.getElementsByTagName('CompleteSuggestion');
					 	
					    // construct a bullet list from the suggestions
					    //var htmlCode = "<ul>";
					    for (i = 0; i < s.length; i++) {
					       var text = s[i].childNodes[0].getAttribute("data");
					       states.push(text);
					       //htmlCode += "<li><b>" + text + "</b></li>";
					    }
					    //htmlCode += "</ul>";
					    console.log(states.length);
					    console.log(states);
					    // display the list on the page
					    //document.getElementById("suggestion").innerHTML = htmlCode;
					 	oAutoSuggestControl.autosuggest(states, bTypeAhead);
					 }
				}
		        //search for matching states
		        // alert(states.length);
		        // for (var i=0; i < states.length; i++) { 
		        //     if (states[i].indexOf(sTextboxValue) == 0) {
		        //         aSuggestions.push(states[i]);
		        //     } 
		        // }
		        //console.log(aSuggestions.length);
		    	
		    }
		    else{
					states = [];
					oAutoSuggestControl.autosuggest(states, bTypeAhead);
				}
		    //provide suggestions to the control
		    
		};
		function start() {
			//alert("onload");
        var oTextbox = new AutoSuggestControl(document.getElementById("q"), new StateSuggestions());     
        }   