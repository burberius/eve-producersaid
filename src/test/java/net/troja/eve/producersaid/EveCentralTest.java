package net.troja.eve.producersaid;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Map;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class EveCentralTest {
    private EveCentral eveCentral = new EveCentral();
    
    @Before
    public void setup() {
	eveCentral.setFilename("src/test/resources/evecentraldata.json");
    }
    
    @Test
    public void testOffline() {
	Map<Integer, EveCentralPrice> prices = eveCentral.getPrices(Arrays.asList(34,35));
	assertNotNull(prices);
	assertTrue(prices.size() == 2);
	EveCentralPrice price = prices.get(34);
	assertNotNull(price.getTypeId());
	assertTrue(price.getTypeId() == 34);
	assertTrue(price.getBuy5Percent() == 5.68f);
    }
    
    @Test
    @Ignore
    public void testLive() {
	eveCentral.setFilename(null);
	Map<Integer, EveCentralPrice> prices = eveCentral.getPrices(Arrays.asList(34,35));
	assertNotNull(prices);
	assertTrue(prices.size() == 2);
	EveCentralPrice price = prices.get(34);
	assertNotNull(price.getTypeId());
	assertTrue(price.getTypeId() == 34);
    }
}
