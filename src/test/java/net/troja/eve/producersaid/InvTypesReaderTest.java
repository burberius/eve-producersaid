package net.troja.eve.producersaid;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

import java.util.Map;

import org.junit.Test;

public class InvTypesReaderTest {
    @Test
    public void testRead() {
	InvTypesReader reader = new InvTypesReader();
	reader.setDataFile("testInvTypes.csv");
	Map<Integer, InvType> invTypes = reader.getInvTypes();
	assertNotNull(invTypes);
	assertTrue(invTypes.size() > 0);
	InvType trit = invTypes.get(34);
	assertThat(trit.getVolume(), is(0.01d));
    }
}
