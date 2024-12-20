#!/bin/bash

# Set the input directory and output directory 
INPUT_DIR="../ebay-data" 
OUTPUT_DIR="ebay-data-csv" 
EXTENSION="xml"

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
mkdir -p "$OUTPUT_DIR"
echo "New Directory '$OUTPUT_DIR' created successfully."
printf "\n"

# Compile and run the convertor
echo "Compile java file..."
javac XMLToCSV.java
# java XMLToCSV ../ebay-data/items-0.xml ebay-data-csv/
echo "Java XMLToCSV compiled successfully."
printf "\n"

# Iterate over each file with the specified extension in the input directory
echo "Iterate over each file with the specified extension in the input directory..."
for INPUT_FILE in "$INPUT_DIR"/*."$EXTENSION"; do
    echo "Processing $INPUT_FILE..."

    # Run the Java program to convert XML to CSV
    java -cp . XMLToCSV "$INPUT_FILE" "$OUTPUT_DIR"
    
    if [ $? -eq 0 ]; then
        echo "Successfully processed $INPUT_FILE"
        printf "\n"
    else
        echo "Failed to process $INPUT_FILE"
        printf "\n"
    fi
done
echo "All files processed. Starting database setup..."

# Run the load.sql batch file to load the data
echo "Loading data into tables..."
mysql --local-infile=1 ad < load.sql
echo "Data loaded successfully."
printf "\n"

# Remove all temporary files
echo "Remove all temporary files..."
rm -r "$OUTPUT_DIR"
echo "All temporary files removed successfully..."
printf "\n"

echo "Database setup and data loading completed successfully."