/*******************************************************************************
 * Copyright (c) 2015 Jens Oberender <j.obi@troja.net>
 *
 * This file is part of Producer's Aid.
 *
 * Producer's Aid is free software: you can redistribute it and/or 
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
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import net.troja.eve.producersaid.data.Blueprint;
import net.troja.eve.producersaid.data.BlueprintActivity;
import net.troja.eve.producersaid.data.BlueprintMaterial;
import net.troja.eve.producersaid.data.BlueprintProduct;
import net.troja.eve.producersaid.data.BlueprintSkill;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

public class BlueprintsReader {
    private static final Logger LOGGER = LogManager.getLogger(BlueprintsReader.class);
    private static final String NODE_BLUEPRINTTYPEID = "blueprintTypeID";
    private static final String NODE_MAXPRODUCTIONLIMIT = "maxProductionLimit";
    private static final String NODE_ACTIVITIES = "activities";
    private static final String NODE_TIME = "time";
    private static final String NODE_MATERIALS = "materials";
    private static final String NODE_QUANTITY = "quantity";
    private static final String NODE_TYPEID = "typeID";
    private static final String NODE_PRODUCTS = "products";
    private static final String NODE_PROBABILITY = "probability";
    private static final String NODE_SKILLS = "skills";
    private static final String NODE_LEVEL = "level";
    
    private String blueprintsFile = "blueprints.yaml";
    private ObjectMapper mapper;
    
    public BlueprintsReader() {
	mapper = new ObjectMapper(new YAMLFactory());
    }
    
    public void setBlueprintsFile(String blueprintsFile) {
        this.blueprintsFile = blueprintsFile;
    }

    public List<Blueprint> getBlueprints() {
	List<Blueprint> blueprints = new ArrayList<Blueprint>();
	InputStream resourceStream = getClass().getResourceAsStream("/" + blueprintsFile);
	try {
	    JsonNode root = mapper.readTree(resourceStream);
	    Iterator<JsonNode> elements = root.elements();
	    while(elements.hasNext()) {
		JsonNode current = elements.next();
		blueprints.add(processBlueprint(current));
	    }
	} catch (JsonProcessingException e) {
	    LOGGER.error("Could not parse blueprints file!");
	} catch (IOException e) {
	    LOGGER.error("Could not read blueprints file: " + blueprintsFile);
	}
	return blueprints;
    }

    private Blueprint processBlueprint(JsonNode node) {
	Blueprint blueprint = new Blueprint();
	blueprint.setId(node.path(NODE_BLUEPRINTTYPEID).asInt());
	blueprint.setMaxProductionLimit(node.path(NODE_MAXPRODUCTIONLIMIT).asInt());
	
	Iterator<Entry<String, JsonNode>> activities = node.path(NODE_ACTIVITIES).fields();
	while(activities.hasNext()) {
	    Entry<String, JsonNode> work = activities.next();
	    LOGGER.info("Name " + work.getKey());
	    BlueprintActivity activity = processActivity(work.getValue());
	    switch (work.getKey()) {
	    case "copying":
		blueprint.setCopying(activity);
		break;
	    case "invention":
		blueprint.setInvention(activity);
		break;
	    case "manufacturing":
		blueprint.setManufacturing(activity);
		break;
	    case "research_material":
		blueprint.setResearchMaterial(activity);
		break;
	    case "research_time":
		blueprint.setResearchTime(activity);
		break;
	    default:
		break;
	    }
	}
	
	return blueprint;
    }

    private BlueprintActivity processActivity(JsonNode node) {
	BlueprintActivity activity = new BlueprintActivity();
	activity.setTime(node.path(NODE_TIME).asInt());
	
	JsonNode materials = node.path(NODE_MATERIALS);
	if(materials != null && !materials.isMissingNode()) {
	    Iterator<JsonNode> elements = materials.elements();
	    while(elements.hasNext()) {
		JsonNode element = elements.next();
		activity.addMaterial(new BlueprintMaterial(element.get(NODE_TYPEID).asInt(), element.get(NODE_QUANTITY).asInt()));
	    }
	}
	
	JsonNode products = node.path(NODE_PRODUCTS);
	if(products != null && !products.isMissingNode()) {
	    Iterator<JsonNode> elements = products.elements();
	    while(elements.hasNext()) {
		JsonNode element = elements.next();
		JsonNode probability = element.get(NODE_PROBABILITY);
		float prob = probability != null ? probability.floatValue() : 0f;
		activity.addProduct(new BlueprintProduct(element.get(NODE_TYPEID).asInt(), element.get(NODE_QUANTITY).asInt(), prob));
	    }
	}
	
	JsonNode skills = node.path(NODE_SKILLS);
	if(skills != null && !skills.isMissingNode()) {
	    Iterator<JsonNode> elements = skills.elements();
	    while(elements.hasNext()) {
		JsonNode element = elements.next();
		activity.addSkill(new BlueprintSkill(element.get(NODE_TYPEID).asInt(), element.get(NODE_LEVEL).asInt()));
	    }
	}
	return activity;
    }
    
    
}
