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

import net.troja.eve.producersaid.data.Blueprint;
import net.troja.eve.producersaid.data.BlueprintMaterial;

public class MaterialResearchCalculator {
    public Blueprint optimize(Blueprint blueprint) {
	List<BlueprintMaterial> materials = blueprint.getManufacturing().getMaterials();
	List<BlueprintMaterial> perfect = calculateEfficiency(materials, 10);
	blueprint.getManufacturing().setMaterials(perfect);
	for(int count = 9; count > 0; count--) {
	    List<BlueprintMaterial> work = calculateEfficiency(materials, count);
	    if(!work.toString().equals(perfect.toString())) {
		blueprint.setMaterialEfficiency(count + 1);
		break;
	    }
	}
	return blueprint;
    }
    
    private List<BlueprintMaterial> calculateEfficiency(List<BlueprintMaterial> materials, int me) {
	List<BlueprintMaterial> materialsResult = new ArrayList<BlueprintMaterial>();
	for(BlueprintMaterial material : materials) {
	    int quantity = material.getQuantity();
	    quantity = (int) Math.ceil((100 - me)/100d * quantity);
	    materialsResult.add(new BlueprintMaterial(material.getTypeId(), material.getName(), quantity));
	}
	return materialsResult;
    }
}
