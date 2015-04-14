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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.troja.eve.producersaid.data.Blueprint;
import net.troja.eve.producersaid.data.BlueprintMaterial;
import net.troja.eve.producersaid.data.BlueprintProduct;
import net.troja.eve.producersaid.data.BlueprintProduction;
import net.troja.eve.producersaid.data.EveCentralPrice;

public class ProductionCalculator {
    private EveCentral eveCentral;

    public ProductionCalculator(EveCentral eveCentral) {
	this.eveCentral = eveCentral;
    }

    public BlueprintProduction calc(Blueprint blueprint) {
	BlueprintProduction production = new BlueprintProduction();
	production.setProductPrice(calcProductPrice(blueprint));
	production.setMaterialPriceBuy(calcMaterialPriceBuy(blueprint));
	production.setMaterialPriceSell(calcMaterialPriceSell(blueprint));
	return production;
    }

    private double calcMaterialPriceBuy(Blueprint blueprint) {
	double result = 0d;
	List<BlueprintMaterial> materials = blueprint.getManufacturing().getMaterials();
	List<Integer> typeIds = new ArrayList<>();
	for (BlueprintMaterial material : materials) {
	    typeIds.add(material.getTypeId());
	}
	Map<Integer, EveCentralPrice> prices = eveCentral.getPrices(typeIds);
	for (BlueprintMaterial material : materials) {
	    EveCentralPrice price = prices.get(material.getTypeId());
	    if (price == null || price.getBuy5Percent() == 0) {
		return 0d;
	    }
	    result += price.getBuy5Percent() * material.getQuantity();
	}
	return result;
    }
    
    private double calcMaterialPriceSell(Blueprint blueprint) {
	double result = 0d;
	List<BlueprintMaterial> materials = blueprint.getManufacturing().getMaterials();
	List<Integer> typeIds = new ArrayList<>();
	for (BlueprintMaterial material : materials) {
	    typeIds.add(material.getTypeId());
	}
	Map<Integer, EveCentralPrice> prices = eveCentral.getPrices(typeIds);
	for (BlueprintMaterial material : materials) {
	    EveCentralPrice price = prices.get(material.getTypeId());
	    if (price == null || price.getSell5Percent() == 0) {
		return 0d;
	    }
	    result += price.getSell5Percent() * material.getQuantity();
	}
	return result;
    }

    private double calcProductPrice(Blueprint blueprint) {
	double result = 0d;
	List<BlueprintProduct> products = blueprint.getManufacturing().getProducts();
	for (BlueprintProduct product : products) {
	    EveCentralPrice price = eveCentral.getPrice(product.getTypeId());
	    if (price != null) {
		double currentPrice = price.getSell5Percent() * product.getQuantity();
		if (currentPrice > result)
		    result = currentPrice;
	    }
	}
	return result;
    }
}
