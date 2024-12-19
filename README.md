# stmw2024
Study repository for Uni Bremen course "Search Technology for Media &amp; Web (Winter Semester 2024/2025)"



## Design Relational Schema

### Step 1: List Your Relations
**Items**:
- `ItemID` (Primary Key)
- `Name`
- `Currently`
- `Buy_Price`
- `First_Bid`
- `Number_of_Bids`
- `Location`
- `Country`
- `Started`
- `Ends`
- `Description`

**ItemCategory**:
- `ItemID` (Foreign Key)
- `Category` (Composite Primary Key with `ItemID`)

**Bids**:
- `ItemID` (Foreign Key)
- `BidderID` (Composite Primary Key with `ItemID` and `Time`)
- `Time`
- `Amount`

**Bidder**:
- `UserID` (Primary Key)
- `Rating`
- `Location`
- `Country`

**Seller**:
- `UserID` (Primary Key)
- `Rating`

### Step 2: List Functional Dependencies
Let's list the non-trivial functional dependencies for each relation:

- **Items**:
  - `ItemID → Name, Currently, Buy_Price, First_Bid, Number_of_Bids, Location, Country, Started, Ends, Description`
  
- **ItemCategory**:
  - `ItemID, Category → (Item is identified by the combination of ItemID and Category)`

- **Bids**:
  - `ItemID, BidderID, Time → Amount`
  
- **Bidder**:
  - `UserID → Rating, Location, Country`
  
- **Seller**:
  - `UserID → Rating`
  

These dependencies capture the relationships and constraints in your data.

### Step 3: Boyce-Codd Normal Form (BCNF)
Checking if the relations are in BCNF:

- **Items**: Already in BCNF, as all non-trivial FDs have a superkey as their determinant.
- **ItemCategory**: In BCNF, as the combination of ItemID and Category forms a composite key.
- **Bids**: In BCNF, as all non-trivial FDs have a superkey as their determinant.
- **Bidder**: In BCNF, as all non-trivial FDs have a superkey as their determinant.
- **Seller**: In BCNF, as all non-trivial FDs have a superkey as their determinant.

### Step 4: Fourth Normal Form (4NF)
Checking if the relations are in 4NF:

- **Items**: Already in 4NF, as there are no multi-valued dependencies.
- **ItemCategory**: Already in 4NF, as there are no multi-valued dependencies.
- **Bids**: Already in 4NF, as there are no multi-valued dependencies.
- **Bidder**: Already in 4NF, as there are no multi-valued dependencies.
- **Seller**: Already in 4NF, as there are no multi-valued dependencies.



### Example Schema:

Example of the relational schema in SQL, reflecting the previous relational schema design:

```sql
CREATE TABLE Items (
    ItemID VARCHAR(255) PRIMARY KEY,
    Name TEXT,
    Currently DECIMAL(10, 2),
    Buy_Price DECIMAL(10, 2),
    First_Bid DECIMAL(10, 2),
    Number_of_Bids INT,
    Location TEXT,
    Country TEXT,
    Started DATETIME,
    Ends DATETIME,
    Description TEXT
);

CREATE TABLE ItemCategory (
    ItemID VARCHAR(255),
    Category TEXT,
    PRIMARY KEY (ItemID, Category),
    FOREIGN KEY (ItemID) REFERENCES Items(ItemID)
);

CREATE TABLE Bids (
    ItemID VARCHAR(255),
    BidderID VARCHAR(255),
    Time DATETIME,
    Amount DECIMAL(10, 2),
    PRIMARY KEY (ItemID, BidderID, Time),
    FOREIGN KEY (ItemID) REFERENCES Items(ItemID),
    FOREIGN KEY (BidderID) REFERENCES Bidder(UserID)
);

CREATE TABLE Bidder (
    UserID VARCHAR(255) PRIMARY KEY,
    Rating INT,
    Location TEXT,
    Country TEXT
);

CREATE TABLE Seller (
    UserID VARCHAR(255) PRIMARY KEY,
    Rating INT
);
```

Let's break down the key components:

1. **Items** table:
    - Contains all essential attributes of an item.
    - `ItemID` as the primary key.

2. **ItemCategory** table:
    - Handles the relationship between items and categories.
    - Composite primary key consisting of `ItemID` and `Category`.

3. **Bids** table:
    - Records bid information.
    - Composite primary key of `ItemID`, `BidderID`, and `Time`.

4. **Bidder** table:
    - Details about each bidder.
    - `UserID` as the primary key.

5. **Seller** table:
    - Details about each seller.
    - `UserID` as the primary key.







## References

- How to use `dos2unix` in Windows
  https://unix.stackexchange.com/questions/721844/linux-bash-shell-script-error-cannot-execute-required-file-not-found

  - Convert using this command:
    ```shell
    dos2unix ./<shell_file_name>.sh
    ```

  - Example usage:
    ![image-20241219205315931](img/README/image-20241219205315931.png)

- 

