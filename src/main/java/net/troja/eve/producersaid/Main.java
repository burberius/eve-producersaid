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

import java.util.List;
import java.util.Map;

import net.troja.eve.crest.industry.systems.IndustryActivitys;
import net.troja.eve.crest.industry.systems.IndustrySystemsParser;
import net.troja.eve.crest.market.prices.MarketPricesParser;
import net.troja.eve.producersaid.data.Blueprint;
import net.troja.eve.producersaid.data.BlueprintProduction;
import net.troja.eve.producersaid.data.InvType;

public class Main {

    public Main() {
	EveCentral eveCentral = new EveCentral();
	
	MarketPricesParser marketPricesParser = new MarketPricesParser();
	Map<Integer, Double> prices = marketPricesParser.getDataAsMap();
	
	IndustrySystemsParser industrySystemsParser = new IndustrySystemsParser();
	Map<String, Map<Integer, Double>> systemsMap = industrySystemsParser.getDataAsMap();
	double costIndex = systemsMap.get("Ajanen").get(IndustryActivitys.Manufacturing);
	System.out.println("Cost Index: " + costIndex);
	
	InvTypesReader invTypesReader = new InvTypesReader();
	Map<Integer, InvType> invTypes = invTypesReader.getInvTypes();
	
	ProductionCalculator prodCalc = new ProductionCalculator(eveCentral, prices, costIndex);
	MaterialResearchCalculator matsCalculator = new MaterialResearchCalculator();
	BlueprintsReader reader = new BlueprintsReader(invTypes);
	List<Blueprint> blueprints = reader.getBlueprints();
	for (int count = 0; count < 20; count++) {
	    Blueprint blueprint = blueprints.get(count);
	    BlueprintProduction prod = prodCalc.calc(blueprint);
	    System.out.println(prod.toString());
	    prod = prodCalc.calc(matsCalculator.optimize(blueprint));
	    System.out.println(prod.toString());

	}
    }

    public static void main(String[] args) {
	new Main();
    }
}
