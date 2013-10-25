-- Find the number of users in the database.
select count(*) from User;
-- 13422 as expected

-- Find the number of sellers from "New York", (i.e., sellers whose location is exactly the string "New York"). Pay special attention to case sensitivity. You should match the sellers from "New York" but not from "new york".
select count(distinct User.id) 
from User, ItemSeller 
where User.id like ItemSeller.uid 
and binary User.location like "New York";
-- 80 as expected

-- Find the number of auctions belonging to exactly four categories.
-- Example GROUPBY: http://www.w3schools.com/sql/trysql.asp?filename=trysql_select_groupby
-- Example Subqueries: http://dev.mysql.com/tech-resources/articles/subqueries_part_1.html
select count(*) 
from 
	(select Item.id, count(ItemCategory.category) as NumberOfCategories 
	from Item, ItemCategory 
	where Item.id=ItemCategory.iid 
	group by Item.id) as CategoryCount 
where CategoryCount.NumberOfCategories=4;
-- 8363 instead of 8365

-- Find the ID(s) of current (unsold) auction(s) with the highest bid. Remember that the data was captured at the point in time December 20th, 2001, one second after midnight, so you can use this time point to decide which auction(s) are current. Pay special attention to the current auctions without any bid.

-- Find the number of sellers whose rating is higher than 1000.
select count(distinct User.id) from User, ItemSeller where User.id like ItemSeller.uid and User.rating > 1000;

-- Find the number of users who are both sellers and bidders.

-- Find the number of categories that include at least one item with a bid of more than $100.