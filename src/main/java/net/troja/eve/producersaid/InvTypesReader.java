package net.troja.eve.producersaid;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

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
    
    public Map<Integer, InvType> getInvTypes() {
	Map<Integer, InvType> result = new HashMap<Integer, InvType>();
	try {
	    URL resource = getClass().getResource("/" + dataFile);
	    if(resource == null) {
		LOGGER.error("Could not find invTypes data file: " + dataFile);
		return result;
	    }
	    InputStreamReader reader = new InputStreamReader(resource.openStream());
	    for (CSVRecord record : CSVFormat.EXCEL.withHeader().parse(reader)) {
		if(!"1".equals(record.get(COLUMN_PUBLISHED))) {
		    continue;
		}
		InvType invType = new InvType();
		invType.setId(Integer.parseInt(record.get(COLUMN_ID)));
		invType.setName(record.get(COLUMN_NAME));
		invType.setDescription(record.get(COLUMN_DESCRIPTION));
		invType.setGroupId(Integer.parseInt(record.get(COLUMN_GROUPID)));
		invType.setMarketGroupId(Integer.parseInt(record.get(COLUMN_MARKETGROUPID)));
		invType.setMass(Integer.parseInt(record.get(COLUMN_MASS)));
		try {
		    invType.setVolume(Double.parseDouble(record.get(COLUMN_VOLUME)));
		} catch(NumberFormatException e) {
		    LOGGER.error("Wrong number format, the csv is probably not converted with en_US locale!");
		}
		result.put(invType.getId(), invType);
	    }
	    reader.close();
	} catch (IOException e) {
	    LOGGER.error("Could not read CSV file of InvTypes", e);
	}

	return result;
    }

    public void setDataFile(String dataFile) {
        this.dataFile = dataFile;
    }
}
