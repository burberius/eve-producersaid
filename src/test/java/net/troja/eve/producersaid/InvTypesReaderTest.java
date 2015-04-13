/*******************************************************************************
 * Copyright (c) 2015 Jens Oberender <j.obi@troja.net>
 *
 * This file is part of Producer's Aid.
 *
 * Producer's Aid is free software: you can redistribute it and/or 
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

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.Map;

import net.troja.eve.producersaid.data.InvType;

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
