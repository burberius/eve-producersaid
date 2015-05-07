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
import java.util.List;
import java.util.Map;

import net.troja.eve.crest.CrestHandler;
import net.troja.eve.producersaid.data.Blueprint;
import net.troja.eve.producersaid.data.BlueprintMaterial;
import net.troja.eve.producersaid.data.BlueprintProduct;
import net.troja.eve.producersaid.data.BlueprintProduction;
import net.troja.eve.producersaid.data.EveCentralPrice;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ProductionCalculator {
    private static final Logger LOGGER = LogManager.getLogger(ProductionCalculator.class);
    private final EveCentral eveCentral;
    private final CrestHandler crestHandler;

    public ProductionCalculator(final EveCentral eveCentral, final CrestHandler crestHandler) {
        this.eveCentral = eveCentral;
        this.crestHandler = crestHandler;
    }

    public BlueprintProduction calc(final Blueprint blueprint) {
        LOGGER.info("Calculating " + blueprint.getName());
        final BlueprintProduction production = new BlueprintProduction();
        production.setProductPrice(calcProductPrice(blueprint));
        production.setMaterialPriceBuy(calcMaterialPriceBuy(blueprint));
        production.setMaterialPriceSell(calcMaterialPriceSell(blueprint));
        production.setProductionBasePrice(calcProductionBasePrice(blueprint));
        production.setUpdateTime(System.currentTimeMillis());
        production.addBlueprintData(blueprint);
        return production;
    }

    private double calcProductionBasePrice(final Blueprint blueprint) {
        double result = 0d;
        final List<BlueprintMaterial> materials = blueprint.getManufacturing().getMaterials();
        for (final BlueprintMaterial material : materials) {
            final Double price = crestHandler.getMarketPrice(material.getTypeId()).getAdjustedPrice();
            if (price == null) {
                return 0d;
            }
            result += material.getQuantity() * price;
        }
        return result;
    }

    private double calcMaterialPriceBuy(final Blueprint blueprint) {
        double result = 0d;
        final List<BlueprintMaterial> materials = blueprint.getManufacturing().getMaterials();
        final List<Integer> typeIds = new ArrayList<>();
        for (final BlueprintMaterial material : materials) {
            typeIds.add(material.getTypeId());
        }
        final Map<Integer, EveCentralPrice> prices = eveCentral.getPrices(typeIds);
        for (final BlueprintMaterial material : materials) {
            final EveCentralPrice price = prices.get(material.getTypeId());
            if ((price == null) || (price.getBuy5Percent() == 0)) {
                return 0d;
            }
            result += price.getBuy5Percent() * material.getQuantity();
        }
        return result;
    }

    private double calcMaterialPriceSell(final Blueprint blueprint) {
        double result = 0d;
        final List<BlueprintMaterial> materials = blueprint.getManufacturing().getMaterials();
        final List<Integer> typeIds = new ArrayList<>();
        for (final BlueprintMaterial material : materials) {
            typeIds.add(material.getTypeId());
        }
        final Map<Integer, EveCentralPrice> prices = eveCentral.getPrices(typeIds);
        for (final BlueprintMaterial material : materials) {
            final EveCentralPrice price = prices.get(material.getTypeId());
            if ((price == null) || (price.getSell5Percent() == 0)) {
                return 0d;
            }
            result += price.getSell5Percent() * material.getQuantity();
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
}
