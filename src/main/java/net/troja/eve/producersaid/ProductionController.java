package net.troja.eve.producersaid;

import java.util.ArrayList;
import java.util.List;

import net.troja.eve.producersaid.data.BlueprintProduction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductionController {
    @Autowired
    private CalculationService calcService;
    
    @RequestMapping("/query")
    public List<BlueprintProduction> query(@RequestParam(value="ids", defaultValue="35") String ids, @RequestParam(value="system", defaultValue="Jita") String system) {
	List<BlueprintProduction> result = new ArrayList<BlueprintProduction>();
	result.add(calcService.getBlueprintProduction());
	return result;
    }
}
