#!/bin/bash

echo $PROJECT_DB_PASSWORD
mysql -v --auto-rehash --host="$PROJECT_DB_HOST" --password="$PROJECT_DB_PASSWORD" --user web <<EOF

# Select diacritics database
USE diacritics;

# Drop all tables
DROP TABLE IF EXISTS Unigrams;
DROP TABLE IF EXISTS Bigrams;
DROP TABLE IF EXISTS Trigrams;
DROP TABLE IF EXISTS Dictionary;
DROP TABLE IF EXISTS FlatDictionary;

# Create each table in the appropriate order
CREATE TABLE FlatDictionary (
  id              INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
  clean_form      VARCHAR(250) NOT NULL UNIQUE
);

CREATE TABLE Dictionary (
  id              INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
  word            VARCHAR(250) NOT NULL UNIQUE,
  clean_id        INT UNSIGNED NOT NULL REFERENCES FlatDictionary (id)
);

CREATE TABLE Unigrams (
  id              INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
  word_id         INT UNSIGNED NOT NULL UNIQUE REFERENCES Dictionary (id),
  frequency       INT UNSIGNED NOT NULL DEFAULT 0
);

CREATE TABLE Bigrams (
  id              INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
  first_word_id   INT UNSIGNED NOT NULL REFERENCES Dictionary (id),
  second_word_id  INT UNSIGNED NOT NULL REFERENCES Dictionary (id)
);

CREATE TABLE Trigrams (
  id              INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
  first_word_id   INT UNSIGNED NOT NULL REFERENCES Dictionary (id),
  second_word_id  INT UNSIGNED NOT NULL REFERENCES Dictionary (id),
  third_word_id   INT UNSIGNED NOT NULL REFERENCES Dictionary (id)
);

EOF
