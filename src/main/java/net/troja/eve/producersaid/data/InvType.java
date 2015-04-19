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
package net.troja.eve.producersaid.data;

public class InvType {
    private int id;
    private String name;
    private String description;
    private int groupId;
    private int marketGroupId;
    private double mass;
    private double volume;
    private int techLevel;

    public int getId() {
	return id;
    }

    public void setId(int id) {
	this.id = id;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public int getMarketGroupId() {
        return marketGroupId;
    }

    public void setMarketGroupId(int marketGroupId) {
        this.marketGroupId = marketGroupId;
    }

    public double getMass() {
        return mass;
    }

    public void setMass(double d) {
        this.mass = d;
    }

    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }

    public int getTechLevel() {
        return techLevel;
    }

    public void setTechLevel(int techLevel) {
        this.techLevel = techLevel;
    }

    @Override
    public String toString() {
	return "InvType [id=" + id + ", name=" + name + ", description=" + description + ", groupId=" + groupId + ", marketGroupId=" + marketGroupId
		+ ", mass=" + mass + ", volume=" + volume + ", techLevel=" + techLevel + "]";
    }
}
