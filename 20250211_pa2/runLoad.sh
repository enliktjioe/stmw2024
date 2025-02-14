#!/bin/bash

# # only for debug / development purpose
# echo "Dropping existing table..."
# mysql < dropSpatialIndex.sql
# echo "Tables dropped successfully."
# printf "\n"

# # Display welcome message
# echo -e "Enlik - Programming Assignment 02"

# # Part A: Create a Spatial Index in MySQL
# echo "Part A: Create a Spatial Index in MySQL"
# echo "Creating new table..."
# mysql < createSpatialIndex.sql
# echo "Tables created successfully."
# printf "\n"

# # Part B: Create a Lucene Index
# echo "Part B: Create a Lucene Index"

# # Compile indexer
# javac Indexer.java
# printf "\n"

# # Run indexer (permitting restricted native access features and enable optimization via vectorization (optional parameters))
# java --enable-native-access=ALL-UNNAMED --add-modules jdk.incubator.vector Indexer
# printf "\n"

# Part C: Implement the Search Function

# Compile searcher
javac Searcher.java
printf "\n"

# Run searcher (permitting restricted native access features and enable optimization via vectorization (optional parameters))
# java --enable-native-access=ALL-UNNAMED --add-modules jdk.incubator.vector Searcher "Marvel" 5
# java --enable-native-access=ALL-UNNAMED --add-modules jdk.incubator.vector Searcher "money" 5

# Lat and Lon using itemId = 1047970072
java --enable-native-access=ALL-UNNAMED --add-modules jdk.incubator.vector Searcher "Marvel" 5 -x 40.84987900 -y -73.97501000 -w 100000
java --enable-native-access=ALL-UNNAMED --add-modules jdk.incubator.vector Searcher "Marvel" 5 -x 20.84987900 -y -53.97501000 -w 100000
# java --enable-native-access=ALL-UNNAMED --add-modules jdk.incubator.vector Searcher "money" 5 -x 40.84987900 -y -73.97501000 -w 100
printf "\n"