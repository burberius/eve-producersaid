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
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;

import net.troja.eve.producersaid.data.Blueprint;
import net.troja.eve.producersaid.data.BlueprintActivity;
import net.troja.eve.producersaid.data.InvType;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class BlueprintsReaderTest {
    @Mock
    private Map<Integer, InvType> invTypes;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testReadOfBlueprints() {
        final BlueprintsReader reader = new BlueprintsReader(invTypes);
        reader.setBlueprintsFile("testBlueprints.yaml");
        when(invTypes.containsKey(any(Integer.class))).thenReturn(true);
        when(invTypes.get(any(Integer.class))).thenReturn(new InvType());

        final List<Blueprint> blueprints = reader.getBlueprints();
        assertThat(blueprints, not(is(nullValue())));
        assertThat(blueprints.size(), is(equalTo(1)));
        final Blueprint blueprint = blueprints.get(0);
        assertThat(blueprint.getId(), is(equalTo(688)));

        final BlueprintActivity manufacturing = blueprint.getManufacturing();
        assertThat(manufacturing.getTime(), is(equalTo(18000)));
        assertThat(manufacturing.getMaterials().size(), is(equalTo(7)));
        assertThat(manufacturing.getProducts().size(), is(equalTo(1)));
        assertThat(manufacturing.getProducts().get(0).getTypeId(), is(equalTo(638)));
        assertThat(manufacturing.getSkills().size(), is(equalTo(1)));
    }
}
