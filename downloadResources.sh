#!/bin/bash

test -f downloadResources.sh || ( echo "Please run in root directory of the project!"; exit 1 )

cd src/main/resources

rm invTypes* dgmTypeAttributes* blueprint*

# Get Blueprints
wget -q https://www.fuzzwork.co.uk/dump/latest/blueprints.yaml.bz2
bunzip2 blueprints.yaml.bz2

# Get invTypes
wget -q https://www.fuzzwork.co.uk/dump/latest/invTypes.xls.bz2
bunzip2 invTypes.xls.bz2
LANG="en_US.UTF-8" ssconvert invTypes.xls invTypes.csv
rm invTypes.xls

# Get dgmTypeAttributes
wget -q https://www.fuzzwork.co.uk/dump/latest/dgmTypeAttributes.csv.bz2
bunzip2 dgmTypeAttributes.csv.bz2
echo "TYPEID,TECH" > dgmTypeAttributes.csv.new
sed -n -e 's#^\([0-9]*\),422,\([0-9]*\),\([0-9]*\)#\1,\2\3#p' dgmTypeAttributes.csv >> dgmTypeAttributes.csv.new
mv dgmTypeAttributes.csv.new dgmTypeAttributes.csv

cd -
