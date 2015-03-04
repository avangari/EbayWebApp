CS144 Project 4
Team: Akshay and Naren

Members:
Akshay Vangari (004435621)
Naren Nagarajappa (004414529)

In this part of the project, we have implemented a web application to help the user navigate through the bidding data. We use the soap services we built from project 3 to do this part. 

The first service provided is that a user can enter a query and matching items are returned, using a server side pagination. The user can then click on any of the ads and then the second service service is called, to display the details about the item . 

We also have a service to enter the item ID directly. We have used google maps java script API to display the position of the item in the item details page. If no valid position can be obtained, we display the map of the entire world (a generic map). First we try to map the latitude and longiude because they are more specific and if theey are unavailable, we use the location and country. 

We also used the google auto-suggest to suggest queries when people want to navigate through the data. This is again implemented using the javascript. 

We have tried to make the service as effecient as possible and not a make a request to the server when there are invalid entries. We have also used some CSS to make the interface pretty. 


PS : For efficiency, we have made validation functions in javascript on client side. For example, when the user enters characters which are not numbers in the Item search page, then the request won't be forwarded to the server. Also if the query box is left empty in any of the search query boxes, the user is alerted but the request is not sent to the server. 

PPS : We have handled most of the common errors and let the user know of the error in those cases. In case there is an unexpected error which we haven't handled, there is a default error page, which will have links to the homepage of our service. 