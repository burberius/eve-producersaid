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

import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Map;

import net.troja.eve.producersaid.data.EveCentralPrice;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class EveCentralTest {
    private final EveCentral eveCentral = new EveCentral();

    @Before
    public void setup() {
        eveCentral.setFilename("src/test/resources/evecentraldata.json");
    }

    @Test
    public void testOfflineSeveralPrices() {
        final Map<Integer, EveCentralPrice> prices = eveCentral.getPrices(Arrays.asList(34, 35));
        assertNotNull(prices);
        assertTrue(prices.size() == 2);
        final EveCentralPrice price = prices.get(34);
        assertNotNull(price.getTypeId());
        assertTrue(price.getTypeId() == 34);
        assertTrue(price.getBuy5Percent() == 5.68f);
    }

    @Test
    public void testOfflineSinglePrice() {
        final EveCentralPrice price = eveCentral.getPrice(34);
        assertNotNull(price);
        assertNotNull(price.getTypeId());
        assertTrue(price.getTypeId() == 34);
        assertTrue(price.getBuy5Percent() == 5.68f);
    }

    @Test
    public void testRefreshOfPrice() {
        EveCentralPrice price = eveCentral.getPrice(34);
        price.setTime(1L);
        price = eveCentral.getPrice(34);
        assertThat(price.getTime(), greaterThan(1L));
    }

    @Test
    @Ignore
    public void testLive() {
        eveCentral.setFilename(null);
        final Map<Integer, EveCentralPrice> prices = eveCentral.getPrices(Arrays.asList(34, 35));
        assertNotNull(prices);
        assertTrue(prices.size() == 2);
        final EveCentralPrice price = prices.get(34);
        assertNotNull(price.getTypeId());
        assertTrue(price.getTypeId() == 34);
    }
}
