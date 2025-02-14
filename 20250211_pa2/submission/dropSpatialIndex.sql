USE ad;

-- Drop the spatial index
ALTER TABLE GeoCoordinates DROP INDEX idx_location;

-- Drop the table
DROP TABLE IF EXISTS GeoCoordinates;