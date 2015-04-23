package net.troja.eve.producersaid;

/*
 * ========================================================================
 * Eve Producer's Aid
 * ------------------------------------------------------------------------
 * Copyright (C) 2014 - 2015 Jens Oberender <j.obi@troja.net>
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

import net.troja.eve.crest.industry.systems.IndustryActivitys;
import net.troja.eve.crest.industry.systems.IndustrySystemsParser;
import net.troja.eve.crest.market.prices.MarketPricesParser;
import net.troja.eve.producersaid.data.Blueprint;
import net.troja.eve.producersaid.data.BlueprintProduction;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
public class CalculationService {
    private static final Logger LOGGER = LogManager.getLogger(CalculationService.class);

    private final EveCentral eveCentral = new EveCentral();
    private final MarketPricesParser marketPricesParser = new MarketPricesParser();
    private final IndustrySystemsParser industrySystemsParser = new IndustrySystemsParser();
    private final List<Blueprint> blueprints = new ArrayList<Blueprint>();
    private final Map<Integer, BlueprintProduction> productionMap = new HashMap<Integer, BlueprintProduction>();

    public CalculationService() {
        LOGGER.info("Starting CalculationService");
        final InvTypesReader invTypesReader = new InvTypesReader();
        final BlueprintsReader reader = new BlueprintsReader(invTypesReader.getInvTypes());
        final MaterialResearchCalculator matsCalculator = new MaterialResearchCalculator();
        final List<Blueprint> originalBlueprints = reader.getBlueprints();
        for (final Blueprint blueprint : originalBlueprints) {
            if (blueprint.getManufacturing() == null) {
                continue;
            }
            blueprints.add(matsCalculator.optimize(blueprint));
        }

        updateList();
    }

    private void updateList() {
        LOGGER.info("Updating list...");
        final Map<Integer, Double> prices = marketPricesParser.getDataAsMap();
        final Map<String, Map<Integer, Double>> systemsMap = industrySystemsParser.getDataAsMap();
        final double costIndex = systemsMap.get("Ajanen").get(IndustryActivitys.Manufacturing);

        final ProductionCalculator prodCalc = new ProductionCalculator(eveCentral, prices, costIndex);

        for (int count = 0; count < 20; count++) {
            final Blueprint blueprint = blueprints.get(count);
            productionMap.put(blueprint.getManufacturing().getProducts().get(0).getTypeId(), prodCalc.calc(blueprint));
        }
        LOGGER.info("Updating list done");

    }

    public BlueprintProduction getBlueprintProduction() {
        return productionMap.entrySet().iterator().next().getValue();
    }
}
