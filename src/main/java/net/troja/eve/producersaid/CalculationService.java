package net.troja.eve.producersaid;

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
    
    private EveCentral eveCentral = new EveCentral();
    private MarketPricesParser marketPricesParser = new MarketPricesParser();
    private IndustrySystemsParser industrySystemsParser = new IndustrySystemsParser();
    private List<Blueprint> blueprints = new ArrayList<Blueprint>();
    private Map<Integer, BlueprintProduction> productionMap = new HashMap<Integer, BlueprintProduction>();
    
    public CalculationService() {
	LOGGER.info("Starting CalculationService");
	InvTypesReader invTypesReader = new InvTypesReader();
	BlueprintsReader reader = new BlueprintsReader(invTypesReader.getInvTypes());
	MaterialResearchCalculator matsCalculator = new MaterialResearchCalculator();
	List<Blueprint> originalBlueprints = reader.getBlueprints();
	for(Blueprint blueprint : originalBlueprints) {
	    if(blueprint.getManufacturing() == null) {
		continue;
	    }
	    blueprints.add(matsCalculator.optimize(blueprint));
	}
	
	updateList();
    }
    
    private void updateList() {
	LOGGER.info("Updating list...");
	Map<Integer, Double> prices = marketPricesParser.getDataAsMap();
	Map<String, Map<Integer, Double>> systemsMap = industrySystemsParser.getDataAsMap();
	double costIndex = systemsMap.get("Ajanen").get(IndustryActivitys.Manufacturing);
	
	ProductionCalculator prodCalc = new ProductionCalculator(eveCentral, prices, costIndex);
	
	for (int count = 0; count < 20; count++) {
	    Blueprint blueprint = blueprints.get(count);
	    productionMap.put(blueprint.getManufacturing().getProducts().get(0).getTypeId(), prodCalc.calc(blueprint));
	}
	LOGGER.info("Updating list done");

    }

    public BlueprintProduction getBlueprintProduction() {
	return productionMap.entrySet().iterator().next().getValue();
    }
}
