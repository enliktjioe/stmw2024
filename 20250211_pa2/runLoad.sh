#!/bin/bash

# only for debug / development purpose
echo "Dropping existing table..."
mysql < dropSpatialIndex.sql
echo "Tables dropped successfully."
printf "\n"

# Display welcome message
echo -e "Enlik - Programming Assignment 02"

# Part A: Create a Spatial Index in MySQL
echo "Part A: Create a Spatial Index in MySQL"
echo "Creating new table..."
mysql < createSpatialIndex.sql
echo "Tables created successfully."
printf "\n"

# Part B: Create a Lucene Index
echo "Part B: Create a Lucene Index"

# Compile indexer
javac Indexer.java
printf "\n"

# Run indexer (permitting restricted native access features and enable optimization via vectorization (optional parameters))
java --enable-native-access=ALL-UNNAMED --add-modules jdk.incubator.vector Indexer

# # Compile searcher
# javac Searcher.java

# # Run searcher (permitting restricted native access features and enable optimization via vectorization (optional parameters))
# java --enable-native-access=ALL-UNNAMED --add-modules jdk.incubator.vector Searcher "the keeper" 10


# PART C