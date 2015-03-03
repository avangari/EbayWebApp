body {
	background: #333;
	margin: 30px auto;
  text-align: center
}

.search {
	background: #444;
	border-radius: 3px;
  margin: 20px;
	display: inline-block;
	padding: 7px
}

input[type="q"]::-webkit-search-cancel-button {
  appearance: none
}

.search input {
	border: 0;
	margin: 0;
  font: 14px/20px 'Nobile', Helvetica, Arial, sans-serif;
	outline: none;
  vertical-align: middle;
  appearance: none
}

.search input[type="q"],
.search input[type="submit"] { border-radius: 3px }
.search input[type="submit"] { cursor: pointer }
.search input[type="q"] {
	background: #fff;
	color: #444;
	min-width: 200px;
  appearance: none;
	padding: 6px 8px
}

.search input[type="submit"] {
	color: #fff;
	margin-left: 7px;
	padding: 6px 10px;
  appearance: none;
  box-shadow: inset 0 5px rgba(255,255,255,0.2), 
            inset 0 -15px 30px rgba(0,0,0,0.4);
  transition: background .3s ease .300ms
}

.search input[type="search"]::-webkit-input-placeholder { 
  color: #444 
}

.search input[type="submit"]:active {
  position: relative; top: 1px
}

/* COLORS */
.blue input[type="submit"] { background: #347fd0  }
.blue input[type="submit"]:hover { background: #0975b6 }
.blue input[type='q'] { border: 1px solid #347fd0 }