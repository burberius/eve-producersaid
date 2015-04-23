package net.troja.eve.producersaid.data;

/*
 * ========================================================================
 * Eve Producer's Aid
 * ------------------------------------------------------------------------
 * Copyright (C) 2014 - 2015 Jens Oberender <j.obi@troja.net>
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

public class Blueprint {
    private int id;
    private String name;
    private int maxProductionLimit;
    private BlueprintActivity copying;
    private BlueprintActivity invention;
    private BlueprintActivity manufacturing;
    private BlueprintActivity researchMaterial;
    private BlueprintActivity researchTime;
    private int materialEfficiency = 0;
    private int timeEfficiency = 0;

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public int getMaxProductionLimit() {
        return maxProductionLimit;
    }

    public void setMaxProductionLimit(final int maxProductionLimit) {
        this.maxProductionLimit = maxProductionLimit;
    }

    public BlueprintActivity getCopying() {
        return copying;
    }

    public void setCopying(final BlueprintActivity copying) {
        this.copying = copying;
    }

    public BlueprintActivity getInvention() {
        return invention;
    }

    public void setInvention(final BlueprintActivity invention) {
        this.invention = invention;
    }

    public BlueprintActivity getManufacturing() {
        return manufacturing;
    }

    public void setManufacturing(final BlueprintActivity manufacturing) {
        this.manufacturing = manufacturing;
    }

    public BlueprintActivity getResearchMaterial() {
        return researchMaterial;
    }

    public void setResearchMaterial(final BlueprintActivity researchMaterial) {
        this.researchMaterial = researchMaterial;
    }

    public BlueprintActivity getResearchTime() {
        return researchTime;
    }

    public void setResearchTime(final BlueprintActivity researchTime) {
        this.researchTime = researchTime;
    }

    public int getMaterialEfficiency() {
        return materialEfficiency;
    }

    public void setMaterialEfficiency(final int materialEfficiency) {
        this.materialEfficiency = materialEfficiency;
    }

    public int getTimeEfficiency() {
        return timeEfficiency;
    }

    public void setTimeEfficiency(final int timeEfficiency) {
        this.timeEfficiency = timeEfficiency;
    }

    @Override
    public String toString() {
        return "Blueprint [id=" + id + ", name=" + name + ", maxProductionLimit=" + maxProductionLimit + ", copying=" + copying + ", invention="
                + invention + ", manufacturing=" + manufacturing + ", researchMaterial=" + researchMaterial + ", researchTime=" + researchTime
                + ", materialEfficiency=" + materialEfficiency + ", timeEfficiency=" + timeEfficiency + "]";
    }
}
