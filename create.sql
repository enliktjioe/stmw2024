CREATE DATABASE IF NOT EXISTS ad;
USE ad;

CREATE TABLE Items (
    ItemID VARCHAR(255) PRIMARY KEY,
    Name TEXT,
    Currently DECIMAL(8, 2),
    Buy_Price DECIMAL(8, 2),
    First_Bid DECIMAL(8, 2),
    Number_of_Bids INT,
    Location TEXT,
    Country TEXT,
    Started DATETIME,
    Ends DATETIME,
    Description VARCHAR(4000)
);

CREATE TABLE ItemCategory (
    ItemID VARCHAR(255),
    Category VARCHAR(255),
    PRIMARY KEY (ItemID, Category),
    FOREIGN KEY (ItemID) REFERENCES Items(ItemID)
);

CREATE TABLE Bidder (
    UserID VARCHAR(255) PRIMARY KEY,
    Rating INT,
    Location TEXT,
    Country TEXT
);

CREATE TABLE Bids (
    BidderID VARCHAR(255),
    Time DATETIME,
    Amount DECIMAL(8, 2),
    PRIMARY KEY (BidderID, Time),
    FOREIGN KEY (BidderID) REFERENCES Bidder(UserID)
);

CREATE TABLE Seller (
    UserID VARCHAR(255) PRIMARY KEY,
    Rating INT
);
