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

import net.troja.eve.crest.beans.IndustrySystem;
import net.troja.eve.producersaid.data.BlueprintProduction;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CalculationService {
    private static final Logger LOGGER = LogManager.getLogger(CalculationService.class);

    @Autowired
    private ProductionCalculator prodCalc;

    public List<BlueprintProduction> getBlueprintProductions(final List<Integer> blueprintTypeIds, final String systemName) {
        final IndustrySystem industrySystem = prodCalc.getIndustrySystem(systemName);
        List<BlueprintProduction> result = null;
        if (industrySystem != null) {
            result = new ArrayList<BlueprintProduction>();
            final double costIndex = industrySystem.getManufacturingCostIndex();
            LOGGER.info("Cost Index: " + costIndex);
            for (final int typeId : blueprintTypeIds) {
                final BlueprintProduction blueprintProduction = prodCalc.getBlueprintProduction(typeId);
                if (blueprintProduction == null) {
                    continue;
                }
                result.add(new BlueprintProduction(blueprintProduction, costIndex));
            }
        }
        return result;
    }
}
