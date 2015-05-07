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

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.troja.eve.crest.CrestHandler;
import net.troja.eve.crest.beans.MarketPrice;
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
    private static final int PRODUCT_QUANTITY = 100;
    private static final float PRODUCT_PRICE = 1234f;
    private static final int PRODUCT_ID = 178;
    private static final int MATERIAL_ID1 = 34;
    private static final int MATERIAL_ID2 = 35;
    private static final float MATERIAL_PRICE_BUY1 = 12f;
    private static final float MATERIAL_PRICE_BUY2 = 24f;
    private static final float MATERIAL_PRICE_SELL1 = 15f;
    private static final float MATERIAL_PRICE_SELL2 = 30f;

    private ProductionCalculator objectToTest;

    @Mock
    private EveCentral eveCentral;

    @Mock
    private CrestHandler crestHandler;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        objectToTest = new ProductionCalculator(eveCentral, crestHandler);
    }

    @Test
    public void testCaculation() {
        final BlueprintProduction result = preparation(MATERIAL_PRICE_BUY2, MATERIAL_PRICE_SELL1);

        assertThat(result.getProductPrice(), is(equalTo((double) (PRODUCT_PRICE * PRODUCT_QUANTITY))));
        final double buyPrice = (MATERIAL_PRICE_BUY1 * 50) + (MATERIAL_PRICE_BUY2 * 25);
        assertThat(result.getMaterialPriceBuy(), is(equalTo(buyPrice)));
        assertThat(result.getMaterialPriceSell(), is(equalTo((double) ((MATERIAL_PRICE_SELL1 * 50) + (MATERIAL_PRICE_SELL2 * 25)))));
        assertThat(result.getProductionBasePrice(), is(equalTo(buyPrice)));
    }

    @Test
    public void testCaculationWithMissingPrice() {
        final BlueprintProduction result = preparation(0f, 0f);

        assertThat(result.getMaterialPriceBuy(), is(equalTo(0d)));
        assertThat(result.getMaterialPriceSell(), is(equalTo(0d)));
    }

    private BlueprintProduction preparation(final float priceBuy2, final float priceSell1) {
        final EveCentralPrice productPrice = new EveCentralPrice();
        productPrice.setSell5Percent(PRODUCT_PRICE);
        when(eveCentral.getPrice(PRODUCT_ID)).thenReturn(productPrice);
        final EveCentralPrice materialPrice1 = new EveCentralPrice();
        materialPrice1.setBuy5Percent(MATERIAL_PRICE_BUY1);
        materialPrice1.setSell5Percent(priceSell1);
        final EveCentralPrice materialPrice2 = new EveCentralPrice();
        materialPrice2.setBuy5Percent(priceBuy2);
        materialPrice2.setSell5Percent(MATERIAL_PRICE_SELL2);
        final Map<Integer, EveCentralPrice> map = new HashMap<>();
        map.put(MATERIAL_ID1, materialPrice1);
        map.put(MATERIAL_ID2, materialPrice2);
        final MarketPrice marketPrice1 = new MarketPrice(MATERIAL_ID1, MATERIAL_PRICE_BUY1, 1);
        final MarketPrice marketPrice2 = new MarketPrice(MATERIAL_ID2, MATERIAL_PRICE_BUY2, 1);
        when(eveCentral.getPrices(Arrays.asList(MATERIAL_ID1, MATERIAL_ID2))).thenReturn(map);
        when(crestHandler.getMarketPrice(MATERIAL_ID1)).thenReturn(marketPrice1);
        when(crestHandler.getMarketPrice(MATERIAL_ID2)).thenReturn(marketPrice2);

        final Blueprint blueprint = createBlueprint();
        final BlueprintProduction result = objectToTest.calc(blueprint);
        return result;
    }

    private Blueprint createBlueprint() {
        final Blueprint blueprint = new Blueprint();
        final BlueprintActivity manufacturing = new BlueprintActivity();
        manufacturing.setProducts(Arrays.asList(new BlueprintProduct(PRODUCT_ID, "Product", PRODUCT_QUANTITY, 0)));
        final List<BlueprintMaterial> materials = new ArrayList<>();
        materials.add(new BlueprintMaterial(MATERIAL_ID1, "Material 1", 50));
        materials.add(new BlueprintMaterial(MATERIAL_ID2, "Material 2", 25));
        manufacturing.setMaterials(materials);
        blueprint.setManufacturing(manufacturing);
        return blueprint;
    }
}
