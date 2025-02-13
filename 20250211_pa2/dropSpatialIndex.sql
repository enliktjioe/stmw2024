USE ad;

-- Drop the spatial index
DROP INDEX IF EXISTS idx_location ON GeoCoordinates;

-- Drop the table
DROP TABLE IF EXISTS GeoCoordinates;