package net.troja.eve.producersaid.utils;

/*
 * ========================================================================
 * Eve Producer's Aid
 * ------------------------------------------------------------------------
 * Copyright (C) 2015 Jens Oberender <j.obi@troja.net>
 * ------------------------------------------------------------------------
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
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
 * ========================================================================
 */

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import net.troja.eve.producersaid.data.InvType;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang.StringUtils;
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
            final InputStreamReader reader = new InputStreamReader(getClass().getResourceAsStream("/" + dataFile));
            for (final CSVRecord record : CSVFormat.EXCEL.withHeader().parse(reader)) {
                if (!"0".equals(record.get(COLUMN_PUBLISHED))) {
                    LOGGER.info("Found published: " + record.get(COLUMN_NAME));
                }
                final InvType invType = new InvType();
                invType.setId(Integer.parseInt(record.get(COLUMN_ID)));
                invType.setName(record.get(COLUMN_NAME));
                invType.setDescription(record.get(COLUMN_DESCRIPTION));
                invType.setGroupId(Integer.parseInt(record.get(COLUMN_GROUPID)));
                final String marketGroupId = record.get(COLUMN_MARKETGROUPID);
                if ((marketGroupId != null) && (marketGroupId.trim().length() > 0)) {
                    invType.setMarketGroupId(Integer.parseInt(marketGroupId));
                }
                final String mass = record.get(COLUMN_MASS);
                if (!StringUtils.isEmpty(mass)) {
                    invType.setMass(Double.parseDouble(mass));
                }
                final String volume = record.get(COLUMN_VOLUME);
                if (!StringUtils.isEmpty(volume)) {
                    try {
                        invType.setVolume(Double.parseDouble(volume));
                    } catch (final NumberFormatException e) {
                        LOGGER.error("Wrong number format, the csv is probably not converted with en_US locale!");
                    }
                }
                invTypes.put(invType.getId(), invType);
            }
            reader.close();
        } catch (final IOException e) {
            LOGGER.error("Could not read CSV file of InvTypes", e);
        }
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Loaded " + invTypes.size() + " invTypes");
        }
    }

    private void loadTechInformation() {
        try {
            final InputStreamReader reader = new InputStreamReader(getClass().getResourceAsStream("/" + techFile));
            for (final CSVRecord record : CSVFormat.EXCEL.withHeader().parse(reader)) {
                final int typeid = Integer.parseInt(record.get("TYPEID"));
                final InvType invType = invTypes.get(typeid);
                if (invType != null) {
                    invType.setTechLevel(Integer.parseInt(record.get("TECH")));
                }
            }
        } catch (final IOException e) {
            LOGGER.error("Could not read CSV file of InvTypes", e);
        }
    }

    public void setDataFile(final String dataFile) {
        this.dataFile = dataFile;
    }

    public void setTechFile(final String techFile) {
        this.techFile = techFile;
    }
}
