package net.troja.eve.producersaid.data;

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

public class BlueprintProduct extends BlueprintMaterial {
    private float probability;
    private int techLevel;

    public BlueprintProduct(final int typeId, final String name, final int quantity, final float probability, final int techLevel) {
        super(typeId, name, quantity);
        this.probability = probability;
        this.techLevel = techLevel;
    }

    public float getProbability() {
        return probability;
    }

    public void setProbability(final float probability) {
        this.probability = probability;
    }

    public int getTechLevel() {
        return techLevel;
    }

    public void setTechLevel(final int techLevel) {
        this.techLevel = techLevel;
    }

    @Override
    public String toString() {
        return "BlueprintProduct [probability=" + probability + ", techLevel=" + techLevel + "]";
    }
}
