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
import java.util.List;
import java.util.Map;

import net.troja.eve.producersaid.data.Blueprint;
import net.troja.eve.producersaid.data.BlueprintMaterial;
import net.troja.eve.producersaid.data.BlueprintProduct;
import net.troja.eve.producersaid.data.BlueprintProduction;
import net.troja.eve.producersaid.data.EveCentralPrice;

public class ProductionCalculator {
    private final EveCentral eveCentral;
    private final Map<Integer, Double> basePrices;
    private final double costIndex;

    public ProductionCalculator(final EveCentral eveCentral, final Map<Integer, Double> basePrices, final double costIndex) {
        this.eveCentral = eveCentral;
        this.basePrices = basePrices;
        this.costIndex = costIndex;
    }

    public BlueprintProduction calc(final Blueprint blueprint) {
        final BlueprintProduction production = new BlueprintProduction();
        production.setProductPrice(calcProductPrice(blueprint));
        production.setMaterialPriceBuy(calcMaterialPriceBuy(blueprint));
        production.setMaterialPriceSell(calcMaterialPriceSell(blueprint));
        production.setProductionCost(calcProductionCosts(blueprint));
        production.setBlueprint(blueprint);
        return production;
    }

    private double calcProductionCosts(final Blueprint blueprint) {
        double result = 0d;
        final List<BlueprintMaterial> materials = blueprint.getManufacturing().getMaterials();
        for (final BlueprintMaterial material : materials) {
            final Double price = basePrices.get(material.getTypeId());
            if (price == null) {
                return 0d;
            }
            result += material.getQuantity() * price;
        }
        result = result * costIndex * 1.1;
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
