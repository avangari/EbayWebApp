use CS144;

select count(*) from Users;

select count(*) from Items where Location = "New York";

select count(*) from (Select count(*) from Categories group by Item_ID having count(Item_ID) = 4) as i;

SELECT i.Item_ID FROM Bids b, Items i WHERE b.Item_ID = i.Item_ID AND Amount = (SELECT MAX(Amount) FROM Bids) AND Ends > '2001-12-20 00:00:01';

select count(*) from Users where Seller_Rating >1000;

select count(*) from Users where Seller_rating is not null and Bidder_Rating is not null;

select count(*) from (select distinct Category from Categories c , Bids b where b.Item_ID = c.Item_ID and Amount > 100) as c;


