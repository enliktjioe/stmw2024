-- 1. Find the number of users in the database.
select count(*) as totalUsers from Bidder;

-- 2. Find the number of items in ”New York”, i.e., items whose location is exactly the string
-- ”New York”. Pay special attention to case sensitivity, i.e. you should e.g. not match items in ”new york”.
select count(*) as totalItemsInNewYork from Items where Location = "New York";

-- 3. Find the number of auctions belonging to exactly four categories. Be careful to remove
-- duplicates, if you store them.
select count(*) as totalItemWithFourCategories from (
    select itemID, count(*) as numOfCategory from ItemCategory group by ItemID
) SQ where SQ.numOfCategory = 4;

-- 4. Find the ID(s) of current (unsold) auction(s) with the highest bid. Remember that
-- the data was captured at December 20th, 2001, one second after midnight. Pay special
-- attention to the current auctions without any bid.

-- 5. Find the number of sellers whose rating is higher than 1000.
select count(*) as NumOfSellersWithRatingHigherThan1K from Seller where rating > 1000;

-- 6. Find the number of users who are both sellers and bidders.
select count(*) as NumOfUsersWhoAreSellerAndBidder from Bidder a, Seller b where a.userID = b.userID;

-- 7. Find the number of categories that include at least one item with a bid of more than $100.
select count(*) as NumOfUsersWhoAreSellerAndBidder from ItemCategory a, Seller b where a.userID = b.userID;
