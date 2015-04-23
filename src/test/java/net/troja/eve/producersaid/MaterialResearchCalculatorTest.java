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

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;

import net.troja.eve.producersaid.data.Blueprint;
import net.troja.eve.producersaid.data.BlueprintActivity;
import net.troja.eve.producersaid.data.BlueprintMaterial;

import org.junit.Test;

public class MaterialResearchCalculatorTest {
    private final MaterialResearchCalculator calculator = new MaterialResearchCalculator();

    @Test
    public void testOptimizationME8() {
        final int[] materials = { 7, 16, 14 };
        final int[] optimizedMaterials = { 7, 15, 13 };
        final int me = 8;
        testOptimization(materials, optimizedMaterials, me);
    }

    @Test
    public void testOptimizationME0() {
        final int[] materials = { 1, 3, 3 };
        final int[] optimizedMaterials = { 1, 3, 3 };
        final int me = 0;
        testOptimization(materials, optimizedMaterials, me);
    }

    @Test
    public void testOptimizationME10() {
        final int[] materials = { 167, 389, 361 };
        final int[] optimizedMaterials = { 151, 351, 325 };
        final int me = 10;
        testOptimization(materials, optimizedMaterials, me);
    }

    private void testOptimization(final int[] quantities, final int[] optimizedQuantities, final int me) {
        final Blueprint blueprint = new Blueprint();
        final BlueprintActivity manufacturing = new BlueprintActivity();
        final List<BlueprintMaterial> materials = new ArrayList<>();
        for (final int num : quantities) {
            materials.add(new BlueprintMaterial(0, "", num));
        }
        manufacturing.setMaterials(materials);
        blueprint.setManufacturing(manufacturing);

        final Blueprint optimized = calculator.optimize(blueprint);
        assertThat(optimized.getMaterialEfficiency(), is(equalTo(me)));
        final List<BlueprintMaterial> optimizedMaterials = optimized.getManufacturing().getMaterials();
        assertThat(optimizedMaterials.size(), is(equalTo(optimizedQuantities.length)));
        for (int pos = 0; pos < optimizedMaterials.size(); pos++) {
            assertThat(optimizedMaterials.get(pos).getQuantity(), is(equalTo(optimizedQuantities[pos])));
        }
    }
}
