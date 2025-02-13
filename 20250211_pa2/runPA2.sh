#!/bin/bash

# Display welcome message
echo -e "Enlik - Programming Assignment 02"

# Part A: Create a Spatial Index in MySQL
echo "Part A: Create a Spatial Index in MySQL"
echo "Dropping existing table..."
mysql < dropSpatialIndex.sql
echo "Tables dropped successfully."
printf "\n"

echo "Creating new table..."
mysql < createSpatialIndex.sql
echo "Tables created successfully."
printf "\n"

# Part B: Create a Lucene Index
echo "Part B: Create a Lucene Index"
# Compile and run Indexer.java to create Lucene index
echo "Compile java file..."
javac Indexer.java
echo "Java file compiled successfully."
printf "\n"

# Run the Java program Indexer
echo "Running Indexer.java"
java Indexer
