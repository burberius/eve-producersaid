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

public class BlueprintSkill {
    private int typeId;
    private int level;

    public BlueprintSkill(int typeId, int level) {
	super();
	this.typeId = typeId;
	this.level = level;
    }

    public int getTypeId() {
	return typeId;
    }

    public void setTypeId(int typeId) {
	this.typeId = typeId;
    }

    public int getLevel() {
	return level;
    }

    public void setLevel(int level) {
	this.level = level;
    }

    @Override
    public String toString() {
	return "ProductSkill [typeId=" + typeId + ", level=" + level + "]";
    }
}
