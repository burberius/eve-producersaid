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
import net.troja.eve.producersaid.data.BlueprintMaterial;
import net.troja.eve.producersaid.data.BlueprintProduct;
import net.troja.eve.producersaid.data.BlueprintProduction;
import net.troja.eve.producersaid.data.EveCentralPrice;
import net.troja.eve.producersaid.utils.BlueprintsReader;
import net.troja.eve.producersaid.utils.EveCentral;
import net.troja.eve.producersaid.utils.InvTypesReader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProductionCalculator {
    private static final Logger LOGGER = LogManager.getLogger(ProductionCalculator.class);
    private final EveCentral eveCentral;
    private final CrestHandler crestHandler;
    private final Map<Integer, Blueprint> blueprints = new HashMap<>();
    private final Map<Integer, Integer> productId2BlueprintId = new HashMap<>();

    @Autowired
    private BlueprintProductionRepository productionRepository;

    public ProductionCalculator() {
        LOGGER.info("Starting ProductionCalculator");
        eveCentral = new EveCentral();
        crestHandler = new CrestHandler();
        crestHandler.enableDataPrefetching(DataType.INDUSTRY_SYSTEM, DataType.MARKET_PRICE);
        crestHandler.init();
        initBlueprints();
    }

    public ProductionCalculator(final EveCentral eveCentral, final CrestHandler crestHandler) {
        this.eveCentral = eveCentral;
        this.crestHandler = crestHandler;
        initBlueprints();
    }

    private void initBlueprints() {
        final InvTypesReader invTypesReader = new InvTypesReader();
        final BlueprintsReader reader = new BlueprintsReader(invTypesReader.getInvTypes());
        final List<Blueprint> originalBlueprints = reader.getBlueprints();
        final MaterialResearchCalculator matsCalculator = new MaterialResearchCalculator();
        for (final Blueprint blueprint : originalBlueprints) {
            if (blueprint.getManufacturing() == null) {
                continue;
            }
            blueprints.put(blueprint.getId(), matsCalculator.optimize(blueprint));
            for (final BlueprintProduct product : blueprint.getManufacturing().getProducts()) {
                productId2BlueprintId.put(product.getTypeId(), blueprint.getId());
            }
        }
    }

    public BlueprintProduction getBlueprintProduction(final Integer blueprintTypeId) {
        BlueprintProduction blueprintProduction = productionRepository.get(blueprintTypeId);
        if (blueprintProduction == null) {
            blueprintProduction = calc(blueprintTypeId);
            productionRepository.put(blueprintProduction);
        }
        return blueprintProduction;
    }

    public BlueprintProduction calc(final int blueprintTypeId) {
        final Blueprint blueprint = blueprints.get(blueprintTypeId);
        BlueprintProduction result = null;
        if (blueprint != null) {
            LOGGER.info("Work on " + blueprint.getId() + " " + blueprint.getName());
            result = calc(blueprint);
        }
        return result;
    }

    public BlueprintProduction calc(final Blueprint blueprint) {
        LOGGER.info("Calculating " + blueprint.getName());
        final BlueprintProduction production = new BlueprintProduction();
        production.setProductPrice(calcProductPrice(blueprint));
        production.setMaterialPriceBuy(calcMaterialPriceBuy(blueprint, false));
        production.setMaterialPriceBuyWithSubs(calcMaterialPriceBuy(blueprint, true));
        production.setMaterialPriceSell(calcMaterialPriceSell(blueprint, false));
        production.setMaterialPriceSellWithSubs(calcMaterialPriceSell(blueprint, true));
        production.setProductionBasePrice(calcProductionBasePrice(blueprint, false));
        production.setProductionBasePriceWithSubs(calcProductionBasePrice(blueprint, true));
        production.setUpdateTime(System.currentTimeMillis());
        production.addBlueprintData(blueprint);
        return production;
    }

    private double calcProductionBasePrice(final Blueprint blueprint, final boolean withSub) {
        LOGGER.info(" calcProductionBasePrice " + blueprint.getName() + " " + withSub);
        double result = 0d;
        final List<BlueprintMaterial> materials = blueprint.getManufacturing().getMaterials();
        for (final BlueprintMaterial material : materials) {
            LOGGER.info("  Working on material " + material.getName());
            Double price = null;
            if (withSub && productId2BlueprintId.containsKey(material.getTypeId())) {
                final Integer blueprintTypeId = productId2BlueprintId.get(material.getTypeId());
                LOGGER.info("  BlueprintId: " + blueprintTypeId);
                final BlueprintProduction production = getBlueprintProduction(blueprintTypeId);
                if (production != null) {
                    price = production.getProductionBasePriceWithSubs();
                }
                if ((price == null) || (price == 0d)) {
                    price = production.getProductionBasePrice();
                }
            }
            if ((price == null) || (price == 0d)) {
                price = crestHandler.getMarketPrice(material.getTypeId()).getAdjustedPrice();
            }
            if (price == null) {
                return 0d;
            }
            result += material.getQuantity() * price;
        }
        return result;
    }

    private double calcMaterialPriceBuy(final Blueprint blueprint, final boolean withSub) {
        LOGGER.info(" calcMaterialPriceBuy " + blueprint.getName());
        double result = 0d;
        final List<BlueprintMaterial> materials = blueprint.getManufacturing().getMaterials();
        final List<Integer> typeIds = new ArrayList<>();
        for (final BlueprintMaterial material : materials) {
            typeIds.add(material.getTypeId());
        }
        final Map<Integer, EveCentralPrice> prices = eveCentral.getPrices(typeIds);
        for (final BlueprintMaterial material : materials) {
            LOGGER.info("  Working on material " + material.getName());
            Double price = null;
            if (withSub && productId2BlueprintId.containsKey(material.getTypeId())) {
                final Integer blueprintTypeId = productId2BlueprintId.get(material.getTypeId());
                final BlueprintProduction production = getBlueprintProduction(blueprintTypeId);
                if (production != null) {
                    price = production.getMaterialPriceBuyWithSubs();
                    if ((price == null) || (price == 0d)) {
                        price = production.getMaterialPriceBuy();
                    }
                }
            } else {
                final EveCentralPrice evePrice = prices.get(material.getTypeId());
                if ((evePrice != null) && (evePrice.getBuy5Percent() > 0)) {
                    price = (double) evePrice.getBuy5Percent();
                }
            }
            if ((price == null) || (price == 0d)) {
                return 0d;
            }
            result += price * material.getQuantity();
        }
        return result;
    }

    private double calcMaterialPriceSell(final Blueprint blueprint, final boolean withSub) {
        LOGGER.info(" calcMaterialPriceSell " + blueprint.getName());
        double result = 0d;
        final List<BlueprintMaterial> materials = blueprint.getManufacturing().getMaterials();
        final List<Integer> typeIds = new ArrayList<>();
        for (final BlueprintMaterial material : materials) {
            typeIds.add(material.getTypeId());
        }
        final Map<Integer, EveCentralPrice> prices = eveCentral.getPrices(typeIds);
        for (final BlueprintMaterial material : materials) {
            LOGGER.info("  Working on material " + material.getName());
            Double price = null;
            if (withSub && productId2BlueprintId.containsKey(material.getTypeId())) {
                final Integer blueprintTypeId = productId2BlueprintId.get(material.getTypeId());
                final BlueprintProduction production = getBlueprintProduction(blueprintTypeId);
                if (production != null) {
                    price = production.getMaterialPriceSellWithSubs();
                    if ((price == null) || (price == 0d)) {
                        price = production.getMaterialPriceSell();
                    }
                }
            } else {
                final EveCentralPrice evePrice = prices.get(material.getTypeId());
                if ((evePrice != null) && (evePrice.getSell5Percent() > 0)) {
                    price = (double) evePrice.getSell5Percent();
                }
            }
            if ((price == null) || (price == 0d)) {
                return 0d;
            }
            result += price * material.getQuantity();
        }
        return result;
    }

    private double calcProductPrice(final Blueprint blueprint) {
        double result = 0d;
        final List<BlueprintProduct> products = blueprint.getManufacturing().getProducts();
        for (final BlueprintProduct product : products) {
            final EveCentralPrice price = eveCentral.getPrice(product.getTypeId());
            if (price != null) {
                final double currentPrice = price.getSell5Percent() * product.getQuantity();
                if (currentPrice > result) {
                    result = currentPrice;
                }
            }
        }
        return result;
    }

    public IndustrySystem getIndustrySystem(final String systemName) {
        return crestHandler.getIndustrySystem(systemName);
    }
}
