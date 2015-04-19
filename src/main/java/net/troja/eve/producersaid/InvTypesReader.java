/*******************************************************************************
 * Copyright (c) 2015 Jens Oberender <j.obi@troja.net>
 *
 * This file is part of Eve Producer's Aid.
 *
 * Eve Producer's Aid is free software: you can redistribute it and/or 
 * modify it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 *******************************************************************************/
package net.troja.eve.producersaid;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import net.troja.eve.producersaid.data.InvType;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class InvTypesReader {
    private static final Logger LOGGER = LogManager.getLogger(InvTypesReader.class);
    private static final String COLUMN_PUBLISHED = "PUBLISHED";
    private static final String COLUMN_ID = "TYPEID";
    private static final String COLUMN_NAME = "TYPENAME";
    private static final String COLUMN_DESCRIPTION = "DESCRIPTION";
    private static final String COLUMN_GROUPID = "GROUPID";
    private static final String COLUMN_MARKETGROUPID = "MARKETGROUPID";
    private static final String COLUMN_MASS = "MASS";
    private static final String COLUMN_VOLUME = "VOLUME";

    private String dataFile = "invTypes.csv";
    private String techFile = "dgmTypeAttributes.csv";
    private Map<Integer, InvType> invTypes = null;

    public Map<Integer, InvType> getInvTypes() {
	if (invTypes == null) {
	    loadInvTypes();
	    loadTechInformation();
	}

	return invTypes;
    }

    private void loadInvTypes() {
	invTypes = new HashMap<Integer, InvType>();
	try {
	    InputStreamReader reader = new InputStreamReader(getClass().getResourceAsStream("/" + dataFile));
	    for (CSVRecord record : CSVFormat.EXCEL.withHeader().parse(reader)) {
		if (!"1".equals(record.get(COLUMN_PUBLISHED))) {
		    continue;
		}
		InvType invType = new InvType();
		invType.setId(Integer.parseInt(record.get(COLUMN_ID)));
		invType.setName(record.get(COLUMN_NAME));
		invType.setDescription(record.get(COLUMN_DESCRIPTION));
		invType.setGroupId(Integer.parseInt(record.get(COLUMN_GROUPID)));
		String marketGroupId = record.get(COLUMN_MARKETGROUPID);
		if (marketGroupId != null && marketGroupId.trim().length() > 0) {
		    invType.setMarketGroupId(Integer.parseInt(marketGroupId));
		}
		invType.setMass(Double.parseDouble(record.get(COLUMN_MASS)));
		try {
		    invType.setVolume(Double.parseDouble(record.get(COLUMN_VOLUME)));
		} catch (NumberFormatException e) {
		    LOGGER.error("Wrong number format, the csv is probably not converted with en_US locale!");
		}
		invTypes.put(invType.getId(), invType);
	    }
	    reader.close();
	} catch (IOException e) {
	    LOGGER.error("Could not read CSV file of InvTypes", e);
	}
    }

    private void loadTechInformation() {
	try {
	    InputStreamReader reader = new InputStreamReader(getClass().getResourceAsStream("/" + techFile));
	    for (CSVRecord record : CSVFormat.EXCEL.withHeader().parse(reader)) {
		int typeid = Integer.parseInt(record.get("TYPEID"));
		InvType invType = invTypes.get(typeid);
		if(invType != null) {
		    invType.setTechLevel(Integer.parseInt(record.get("TECH")));
		}
	    }
	} catch (IOException e) {
	    LOGGER.error("Could not read CSV file of InvTypes", e);
	}
    }

    public void setDataFile(String dataFile) {
	this.dataFile = dataFile;
    }

    public void setTechFile(String techFile) {
	this.techFile = techFile;
    }
}
