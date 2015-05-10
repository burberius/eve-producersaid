package net.troja.eve.producersaid;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.troja.eve.crest.CrestDataHandler.DataType;
import net.troja.eve.crest.CrestHandler;
import net.troja.eve.crest.beans.IndustrySystem;
import net.troja.eve.producersaid.data.Blueprint;
import net.troja.eve.producersaid.data.BlueprintProduction;
import net.troja.eve.producersaid.utils.BlueprintsReader;
import net.troja.eve.producersaid.utils.EveCentral;
import net.troja.eve.producersaid.utils.InvTypesReader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
public class CalculationService {
    private static final Logger LOGGER = LogManager.getLogger(CalculationService.class);
    private static final int HOUR = 1000 * 60 * 60;

    private final CrestHandler crestHandler = new CrestHandler();
    private final ProductionCalculator prodCalc;
    private final EveCentral eveCentral = new EveCentral();
    private final Map<Integer, Blueprint> blueprints = new HashMap<>();
    private final Map<Integer, Integer> product2Blueprint = new HashMap<>();
    private final Map<Integer, BlueprintProduction> productionMap = new HashMap<>();

    public CalculationService() {
        LOGGER.info("Starting CalculationService");
        crestHandler.enableDataPrefetching(DataType.INDUSTRY_SYSTEM, DataType.MARKET_PRICE);
        crestHandler.init();
        prodCalc = new ProductionCalculator(eveCentral, crestHandler);

        final InvTypesReader invTypesReader = new InvTypesReader();
        final BlueprintsReader reader = new BlueprintsReader(invTypesReader.getInvTypes());
        final MaterialResearchCalculator matsCalculator = new MaterialResearchCalculator();
        final List<Blueprint> originalBlueprints = reader.getBlueprints();
        for (final Blueprint blueprint : originalBlueprints) {
            if (blueprint.getManufacturing() == null) {
                continue;
            }
            blueprints.put(blueprint.getId(), matsCalculator.optimize(blueprint));
            product2Blueprint.put(blueprint.getManufacturing().getProducts().get(0).getTypeId(), blueprint.getId());
        }
    }

    public List<BlueprintProduction> getBlueprintProductions(final List<Integer> blueprintTypeIds, final String systemName) {
        final IndustrySystem industrySystem = crestHandler.getIndustrySystem(systemName);
        List<BlueprintProduction> result = null;
        if (industrySystem != null) {
            result = new ArrayList<BlueprintProduction>();
            final double costIndex = industrySystem.getManufacturingCostIndex();
            LOGGER.info("Cost Index: " + costIndex);
            for (final int typeId : blueprintTypeIds) {
                final BlueprintProduction blueprintProduction = getBlueprintProduction(typeId);
                if (blueprintProduction == null) {
                    continue;
                }
                result.add(new BlueprintProduction(blueprintProduction, costIndex));
            }
        }
        return result;
    }

    private BlueprintProduction getBlueprintProduction(final Integer blueprintTypeId) {
        BlueprintProduction blueprintProduction = productionMap.get(blueprintTypeId);
        if ((blueprintProduction == null) || (blueprintProduction.getUpdateTime() < (System.currentTimeMillis() - HOUR))) {
            final Blueprint blueprint = blueprints.get(blueprintTypeId);
            if (blueprint != null) {
                LOGGER.info("Work on " + blueprint.getId() + " " + blueprint.getName());
                blueprintProduction = prodCalc.calc(blueprint);
                productionMap.put(blueprintTypeId, blueprintProduction);
            }
        }
        return blueprintProduction;
    }
}
