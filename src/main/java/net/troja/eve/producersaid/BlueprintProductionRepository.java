package net.troja.eve.producersaid;

import java.util.HashMap;
import java.util.Map;

import net.troja.eve.producersaid.data.BlueprintProduction;

import org.springframework.stereotype.Repository;

@Repository
public class BlueprintProductionRepository {
    private static final int HOUR = 1000 * 60 * 60;

    private final Map<Integer, BlueprintProduction> productionMap = new HashMap<>();

    public BlueprintProduction get(final Integer blueprintTypeId) {
        BlueprintProduction blueprintProduction = productionMap.get(blueprintTypeId);
        if ((blueprintProduction != null) && (blueprintProduction.getUpdateTime() < (System.currentTimeMillis() - HOUR))) {
            blueprintProduction = null;
        }
        return blueprintProduction;
    }

    public void put(final BlueprintProduction blueprintProduction) {
        productionMap.put(blueprintProduction.getBlueprintTypeId(), blueprintProduction);
    }
}
