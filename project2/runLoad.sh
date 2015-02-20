#!/bin/bash

mysql CS144 < drop.sql

mysql CS144 < create.sql

ant -f build.xml run-all

sort -u categories.csv > categories.dat
sort -u items.csv > items.dat
sort -u users.csv > users.dat
sort -u bids.csv > bids.dat

mysql CS144 < load.sql

rm *.csv 
rm *.dat
rm -r bin
rm -r lib
