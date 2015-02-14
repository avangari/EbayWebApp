USE CS144;
set character_set_client=utf8; 
set character_set_connection=utf8;
set character_set_results=utf8;
SET NAMES utf8;

CREATE TABLE Users 
(
	User_ID varchar(100) NOT NULL,
	Seller_Rating int(11) DEFAULT NULL,
	Bidder_Rating int(11) DEFAULT NULL,
	Location varchar(100) DEFAULT NULL,
	Country varchar(100)  DEFAULT NULL,
	PRIMARY KEY (User_ID)
) ;

CREATE TABLE Items
(
	Item_ID int(11) NOT NULL,
	Name varchar(100)  NOT NULL,
	Started timestamp NOT NULL ,
	Ends timestamp NOT NULL,
	Currently decimal(8,2) NOT NULL,
	First_Bid decimal(8,2) NOT NULL,
	Buy_Price decimal(8,2) NOT NULL, 
	No_of_Bids int(11) NOT NULL,
    Seller_ID varchar(100) NOT NULL,
    Location varchar(100)  NOT NULL,
    Latitude decimal(10,6) DEFAULT NULL,
    Longitude decimal(10,6) DEFAULT NULL,
    Country varchar(50) DEFAULT NULL,
    Description varchar(4000) DEFAULT NULL,
    PRIMARY KEY(Item_ID),
    CONSTRAINT Items_FK FOREIGN KEY (Seller_ID) REFERENCES Users (User_ID) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE Categories
(
	Item_ID int(11) NOT NULL,
	Category varchar(100) NOT NULL,
	PRIMARY KEY(Item_ID, Category),
	CONSTRAINT Categories_FK FOREIGN KEY (Item_ID) REFERENCES Items (Item_ID) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE Bids
(
	Item_ID int(11) NOT NULL,
	User_ID varchar(100) NOT NULL,
	Time timestamp NOT NULL ,
	Amount decimal(8,2) DEFAULT NULL,
	PRIMARY KEY(Item_ID, User_ID, Time),
	CONSTRAINT Bids_FK FOREIGN KEY (Item_ID) REFERENCES Items(Item_ID) ON DELETE CASCADE ON UPDATE CASCADE,
	CONSTRAINT Bids_FK2 FOREIGN KEY (USER_ID) REFERENCES Users(User_ID) ON DELETE CASCADE ON UPDATE CASCADE
);


