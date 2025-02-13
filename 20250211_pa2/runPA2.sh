#!/bin/bash

# Display welcome message
echo -e "Enlik - Programming Assignment 02"

# Part A: Create a Spatial Index in MySQL
echo "Dropping existing table..."
mysql < dropSpatialIndex.sql
echo "Tables dropped successfully."
printf "\n"

echo "Creating new table..."
mysql < createSpatialIndex.sql
echo "Tables created successfully."
printf "\n"