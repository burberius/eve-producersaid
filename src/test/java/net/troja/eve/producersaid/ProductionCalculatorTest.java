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
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import net.troja.eve.producersaid.data.Blueprint;
import net.troja.eve.producersaid.data.BlueprintActivity;
import net.troja.eve.producersaid.data.BlueprintMaterial;
import net.troja.eve.producersaid.data.BlueprintProduct;
import net.troja.eve.producersaid.data.BlueprintProduction;
import net.troja.eve.producersaid.data.EveCentralPrice;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class ProductionCalculatorTest {
    private static final float PRODUCT_PRICE = 1234f;
    private static final int PRODUCT_ID = 178;
    private static final int MATERIAL_ID1 = 34;
    private static final int MATERIAL_ID2 = 35;
    private static final float MATERIAL_PRICE_BUY1 = 12f;
    private static final float MATERIAL_PRICE_BUY2 = 24f;
    private static final float MATERIAL_PRICE_SELL1 = 15f;
    private static final float MATERIAL_PRICE_SELL2 = 30f;
    
    @Mock
    private EveCentral eveCentral;
    
    @Before
    public void setup() {
	MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void testCaculation() {
	ProductionCalculator calculator = new ProductionCalculator(eveCentral);
	EveCentralPrice productPrice = new EveCentralPrice();
	productPrice.setSell5Percent(PRODUCT_PRICE);
	when(eveCentral.getPrice(PRODUCT_ID)).thenReturn(productPrice);
	EveCentralPrice materialPrice1 = new EveCentralPrice();
	materialPrice1.setBuy5Percent(MATERIAL_PRICE_BUY1);
	materialPrice1.setSell5Percent(MATERIAL_PRICE_SELL1);
	EveCentralPrice materialPrice2 = new EveCentralPrice();
	materialPrice2.setBuy5Percent(MATERIAL_PRICE_BUY2);
	materialPrice2.setSell5Percent(MATERIAL_PRICE_SELL2);
	HashMap<Integer, EveCentralPrice> map = new HashMap<Integer, EveCentralPrice>();
	map.put(MATERIAL_ID1, materialPrice1);
	map.put(MATERIAL_ID2, materialPrice2);
	when(eveCentral.getPrices(Arrays.asList(MATERIAL_ID1, MATERIAL_ID2))).thenReturn(map);
	
	Blueprint blueprint = createBlueprint();
	BlueprintProduction result = calculator.calc(blueprint);
	
	assertThat(result.getProductPrice(), is(equalTo((double)(PRODUCT_PRICE * 100))));
	assertThat(result.getMaterialPriceBuy(), is(equalTo((double)((MATERIAL_PRICE_BUY1 * 50) + (MATERIAL_PRICE_BUY2 * 25)))));
	assertThat(result.getMaterialPriceSell(), is(equalTo((double)((MATERIAL_PRICE_SELL1 * 50) + (MATERIAL_PRICE_SELL2 * 25)))));
    }

    private Blueprint createBlueprint() {
	Blueprint blueprint = new Blueprint();
	BlueprintActivity manufacturing = new BlueprintActivity();
	manufacturing.setProducts(Arrays.asList(new BlueprintProduct(PRODUCT_ID, 100, 0)));
	List<BlueprintMaterial> materials = new ArrayList<>();
	materials.add(new BlueprintMaterial(MATERIAL_ID1, 50));
	materials.add(new BlueprintMaterial(MATERIAL_ID2, 25));
	manufacturing.setMaterials(materials);
	blueprint.setManufacturing(manufacturing);
	return blueprint;
    }
}
