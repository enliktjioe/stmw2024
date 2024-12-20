#!/bin/bash
# echo "test"

# Run the drop.sql batch file to drop existing tables.
# Inside the drop.sql, you should check whether the table exists. Drop them ONLY if they exist.
echo "Dropping existing tables..."
mysql < drop.sql
echo "Tables dropped successfully."
printf "\n"

# Run the create.sql batch file to create the database (if it does not exist) and the tables.
echo "Creating new tables..."
mysql < create.sql
echo "Tables created successfully."
printf "\n"

# Create new directory for storing output files from conversion XML to CSV
echo "Create new directory for storing output files from conversion XML to CSV..."
mkdir "ebay-data-csv"
echo "New Directory 'ebay-data-csv' created successfully."
printf "\n"

# Compile and run the convertor
echo "Compile and run the convertor..."
javac XMLToCSV.java
java XMLToCSV
echo "Java XMLToCSV ran successfully."
printf "\n"

# Run the load.sql batch file to load the data
echo "Loading data into tables..."
mysql --local-infile=1 ad < load.sql
echo "Data loaded successfully."
printf "\n"

# Remove all temporary files
echo "Remove all temporary files..."
rm -r ebay-data-csv
echo "All temporary files removed successfully..."
printf "\n"

echo "Database setup and data loading completed successfully."