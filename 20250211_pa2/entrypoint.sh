#!/bin/bash

# Display welcome message
echo -e "Welcome to Programming Assignment 2!\n\n"

# Start MySQL service and configure it
echo "Starting MySQL service..."
service mysql start || { echo "Failed to start MySQL"; exit 1; }
mysql -u root -e "SET GLOBAL local_infile = 1;"
mysql -u root -e "ALTER USER 'root'@'localhost' IDENTIFIED WITH mysql_native_password BY '';
FLUSH PRIVILEGES;"  # Allow password authentication (required for JDBC)
mysql -u root -e "GRANT ALL PRIVILEGES ON ad.* TO 'root'@'localhost';
FLUSH PRIVILEGES;"  # Grant all privileges to root (required for JDBC)

# Check if the 'ad' database exists. If not, create it.
if ! mysql -u root -e "SHOW DATABASES LIKE 'ad';" | grep -q "ad"; then
  echo "Database 'ad' does not exist. Creating database..."
  mysql -u root -e "CREATE DATABASE ad;"
else
  echo "Database 'ad' already exists. Skipping creation."
fi

# Check if the database is already initialized (you can use a table that is guaranteed to exist)
if ! mysql -u root -e "USE ad; SHOW TABLES;" | grep -q "Items"; then
  echo "Database is not initialized. Importing data..."
  mysql ad < ebay-data/ad.sql  # Populate database
else
  echo "Database is already initialized. Skipping data import."
fi

# Prevent the container from exiting immediately by starting a bash shell
exec bash