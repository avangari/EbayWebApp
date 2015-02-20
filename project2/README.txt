Akshay Vangari
Naren Nagarajappa

Items(Item_ID , Name, Started, Ends, Currently, First_Bid, Buy_Price, No_of_Bids, Seller_ID, Location, Latitude, Longitude, Country, Description)-----------------> PrimaryKey(Item_ID).
User(User_ID(PK), Seller_Rating, Bidder_Rating, Location, Country)--------------> PrimaryKey(User_ID)
ItemCategory(Item_ID(PK), Category(PK))------------->PrimaryKey(Item_ID,Category)
Bid(Item_ID (PK), User_ID (PK), Time (PK), Amount)----------> PrimaryKey(Item_ID, User_ID, Time)

--- we started off building the schema with the ER diagram and a result of that, we had only one Functional Dependancy which violated the BCNF and we eliminted that. As a result of that, we never had any 4NF violations. --- 
