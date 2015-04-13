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

import java.util.ArrayList;
import java.util.List;

public class BlueprintActivity {
    private int time;
    private List<BlueprintMaterial> materials = new ArrayList<>();
    private List<BlueprintProduct> products = new ArrayList<>();
    private List<BlueprintSkill> skills = new ArrayList<>();

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public void addMaterial(BlueprintMaterial blueprintMaterials) {
	materials.add(blueprintMaterials);
    }

    public List<BlueprintMaterial> getMaterials() {
        return materials;
    }

    public void setMaterials(List<BlueprintMaterial> materials) {
        this.materials = materials;
    }
    
    public void addProduct(BlueprintProduct blueprintProduct) {
	products.add(blueprintProduct);
    }
    
    public List<BlueprintProduct> getProducts() {
        return products;
    }

    public void setProducts(List<BlueprintProduct> products) {
        this.products = products;
    }

    public void addSkill(BlueprintSkill blueprintSkill) {
	skills.add(blueprintSkill);	
    }
    
    public List<BlueprintSkill> getSkills() {
        return skills;
    }

    public void setSkills(List<BlueprintSkill> skills) {
        this.skills = skills;
    }

    @Override
    public String toString() {
	return "BlueprintActivity [time=" + time + ", materials=" + materials + ", products=" + products + ", skills=" + skills + "]";
    }
}
