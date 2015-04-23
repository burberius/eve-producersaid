#*******************************************************************************
# Copyright (c) 2015 Jens Oberender <j.obi@troja.net>
#
# This file is part of Eve Producer's Aid.
#
# Eve Producer's Aid is free software: you can redistribute it and/or 
# modify it under the terms of the GNU General Public License as
# published by the Free Software Foundation, either version 3 of the
# License, or (at your option) any later version.
#
# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU General Public License for more details.
#
# You should have received a copy of the GNU General Public
# License along with this program.  If not, see
# <http://www.gnu.org/licenses/gpl-3.0.html>.
#*******************************************************************************
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
