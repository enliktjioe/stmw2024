USE ad;

-- 1. Create a new table that associates geo-coordinates (as POINTs) to item ids, i.e., a table with two columns:
-- One column for item ids and another column for geo geographic coordinates. Geo-coordinates are points, represented using the POINT data type of MySQL. 
-- Recall that the point-column must be declared as NOT NULL and that the table must be created using ENGINE=InnoDB, i.e., the statement has this structure 
-- ```sql CREATE TABLE IF NOT EXISTS xxx (...) ENGINE = InnoDB; ```
CREATE TABLE IF NOT EXISTS GeoCoordinates (
    itemId INT NOT NULL,
    location POINT NOT NULL,
    PRIMARY KEY (itemId)
) ENGINE=InnoDB;

-- 2. Fill this table with all items that have latitude and longitude information, i.e., write a
-- SQL INSERT INTO statement with a SQL query that selects each item id together with
-- its latitude and longitude information (converted into a POINT).
INSERT INTO GeoCoordinates (itemId, location)
    SELECT itemId, ST_GeomFromText(CONCAT('POINT(', latitude, ' ', longitude, ')'))
    FROM ItemLatLon
WHERE latitude IS NOT NULL AND longitude IS NOT NULL;

-- 3. Create a spatial index for the point column of your table from the previous step.
CREATE SPATIAL INDEX idx_location ON GeoCoordinates (location);