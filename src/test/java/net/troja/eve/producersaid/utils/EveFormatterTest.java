package net.troja.eve.producersaid.utils;

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
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class EveFormatterTest {
    private static final double WITH_DECIMAL = 1234.5678d;
    private static final String RESULT_WITH_DECIMAL = "1.234,57";
    private static final String RESULT_WITH_DECIMAL_WITHOUT_THEM = "1.235";
    private static final double WITHOUT_DECIMAL = 12345678d;
    private static final String RESULT_WITHOUT_DECIMAL = "12.345.678";

    @Test
    public void testWithDecimal() {
        assertThat(EveFormatter.formatIsk(WITH_DECIMAL), equalTo(RESULT_WITH_DECIMAL));
        assertThat(EveFormatter.formatIskWithoutDecimals(WITH_DECIMAL), equalTo(RESULT_WITH_DECIMAL_WITHOUT_THEM));
    }

    @Test
    public void testWithoutDecimal() {
        assertThat(EveFormatter.formatIskWithoutDecimals(WITHOUT_DECIMAL), equalTo(RESULT_WITHOUT_DECIMAL));
    }
}
