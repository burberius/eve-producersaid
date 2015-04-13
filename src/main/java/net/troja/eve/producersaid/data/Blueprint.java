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
package net.troja.eve.producersaid.data;

public class Blueprint {
    private int id;
    private int maxProductionLimit;
    private BlueprintActivity copying;
    private BlueprintActivity invention;
    private BlueprintActivity manufacturing;
    private BlueprintActivity researchMaterial;
    private BlueprintActivity researchTime;

    public int getId() {
	return id;
    }

    public void setId(int id) {
	this.id = id;
    }

    public int getMaxProductionLimit() {
	return maxProductionLimit;
    }

    public void setMaxProductionLimit(int maxProductionLimit) {
	this.maxProductionLimit = maxProductionLimit;
    }

    public BlueprintActivity getCopying() {
        return copying;
    }

    public void setCopying(BlueprintActivity copying) {
        this.copying = copying;
    }

    public BlueprintActivity getInvention() {
        return invention;
    }

    public void setInvention(BlueprintActivity invention) {
        this.invention = invention;
    }

    public BlueprintActivity getManufacturing() {
        return manufacturing;
    }

    public void setManufacturing(BlueprintActivity manufacturing) {
        this.manufacturing = manufacturing;
    }

    public BlueprintActivity getResearchMaterial() {
        return researchMaterial;
    }

    public void setResearchMaterial(BlueprintActivity researchMaterial) {
        this.researchMaterial = researchMaterial;
    }

    public BlueprintActivity getResearchTime() {
        return researchTime;
    }

    public void setResearchTime(BlueprintActivity researchTime) {
        this.researchTime = researchTime;
    }

    @Override
    public String toString() {
	return "Blueprint [id=" + id + ", maxProductionLimit=" + maxProductionLimit + ", copying=" + copying + ", invention=" + invention
		+ ", manufacturing=" + manufacturing + ", researchMaterial=" + researchMaterial + ", researchTime=" + researchTime + "]";
    }
}
