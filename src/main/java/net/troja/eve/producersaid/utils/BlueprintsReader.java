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
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.troja.eve.producersaid.data.Blueprint;
import net.troja.eve.producersaid.data.BlueprintActivity;
import net.troja.eve.producersaid.data.BlueprintMaterial;
import net.troja.eve.producersaid.data.BlueprintProduct;
import net.troja.eve.producersaid.data.BlueprintSkill;
import net.troja.eve.producersaid.data.InvType;

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
    private final ObjectMapper mapper;
    private final Map<Integer, InvType> invTypes;

    public BlueprintsReader(final Map<Integer, InvType> invTypes) {
        mapper = new ObjectMapper(new YAMLFactory());
        this.invTypes = invTypes;
    }

    public void setBlueprintsFile(final String blueprintsFile) {
        this.blueprintsFile = blueprintsFile;
    }

    public List<Blueprint> getBlueprints() {
        final List<Blueprint> blueprints = new ArrayList<Blueprint>();
        final InputStream resourceStream = getClass().getResourceAsStream("/" + blueprintsFile);
        try {
            final JsonNode root = mapper.readTree(resourceStream);
            final Iterator<JsonNode> elements = root.elements();
            while (elements.hasNext()) {
                final JsonNode current = elements.next();
                final Blueprint blueprint = processBlueprint(current);
                if (blueprint != null) {
                    blueprints.add(blueprint);
                }
            }
        } catch (final JsonProcessingException e) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("Could not parse blueprints file!", e);
            }
        } catch (final IOException e) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("Could not read blueprints file: " + blueprintsFile, e);
            }
        } finally {
            if (resourceStream != null) {
                try {
                    resourceStream.close();
                } catch (final IOException e) {
                    if (LOGGER.isErrorEnabled()) {
                        LOGGER.error("Could not close stream", e);
                    }
                }
            }
        }
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Loaded " + blueprints.size() + " blueprints");
        }
        return blueprints;
    }

    private Blueprint processBlueprint(final JsonNode node) {
        final int typeId = node.path(NODE_BLUEPRINTTYPEID).asInt();
        if (!invTypes.containsKey(typeId)) {
            return null;
        }
        final Blueprint blueprint = new Blueprint();
        blueprint.setId(typeId);
        blueprint.setName(getName(blueprint.getId()));
        blueprint.setMaxProductionLimit(node.path(NODE_MAXPRODUCTIONLIMIT).asInt());

        final Iterator<Entry<String, JsonNode>> activities = node.path(NODE_ACTIVITIES).fields();
        while (activities.hasNext()) {
            final Entry<String, JsonNode> work = activities.next();
            final BlueprintActivity activity = processActivity(work.getValue());
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

    private BlueprintActivity processActivity(final JsonNode node) {
        final BlueprintActivity activity = new BlueprintActivity();
        activity.setTime(node.path(NODE_TIME).asInt());

        final JsonNode materials = node.path(NODE_MATERIALS);
        if ((materials != null) && !materials.isMissingNode()) {
            final Iterator<JsonNode> elements = materials.elements();
            while (elements.hasNext()) {
                activity.addMaterial(processMaterial(elements.next()));
            }
        }

        final JsonNode products = node.path(NODE_PRODUCTS);
        if ((products != null) && !products.isMissingNode()) {
            final Iterator<JsonNode> elements = products.elements();
            while (elements.hasNext()) {
                activity.addProduct(processProduct(elements.next()));
            }
        }

        final JsonNode skills = node.path(NODE_SKILLS);
        if ((skills != null) && !skills.isMissingNode()) {
            final Iterator<JsonNode> elements = skills.elements();
            while (elements.hasNext()) {
                activity.addSkill(parseSkill(elements.next()));
            }
        }
        return activity;
    }

    private BlueprintMaterial processMaterial(final JsonNode element) {
        final int typeId = element.get(NODE_TYPEID).asInt();
        return new BlueprintMaterial(typeId, getName(typeId), element.get(NODE_QUANTITY).asInt());
    }

    private BlueprintProduct processProduct(final JsonNode element) {
        final JsonNode probability = element.get(NODE_PROBABILITY);
        float prob = 0f;
        if (probability != null) {
            prob = probability.floatValue();
        }
        final int typeId = element.get(NODE_TYPEID).asInt();
        return new BlueprintProduct(typeId, getName(typeId), element.get(NODE_QUANTITY).asInt(), prob, getTechLevel(typeId));
    }

    private BlueprintSkill parseSkill(final JsonNode element) {
        final int typeId = element.get(NODE_TYPEID).asInt();
        return new BlueprintSkill(typeId, getName(typeId), element.get(NODE_LEVEL).asInt());
    }

    private String getName(final int typeId) {
        String name = "unknown";
        final InvType invType = invTypes.get(typeId);
        if (invType != null) {
            name = invType.getName();
        }
        return name;
    }

    private int getTechLevel(final int typeId) {
        int result = 0;
        final InvType invType = invTypes.get(typeId);
        if (invType != null) {
            result = invType.getTechLevel();
        }
        return result;
    }
}
