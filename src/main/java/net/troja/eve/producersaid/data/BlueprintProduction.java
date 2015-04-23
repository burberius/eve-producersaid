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

import net.troja.eve.producersaid.utils.EveFormatter;

public class BlueprintProduction {
    private double productPrice;
    private double materialPriceBuy;
    private double materialPriceSell;
    private double productionCost;
    private Blueprint blueprint;

    public void setProductPrice(final double productPrice) {
        this.productPrice = productPrice;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public double getMaterialPriceBuy() {
        return materialPriceBuy;
    }

    public void setMaterialPriceBuy(final double materialPriceBuy) {
        this.materialPriceBuy = materialPriceBuy;
    }

    public double getMaterialPriceSell() {
        return materialPriceSell;
    }

    public void setMaterialPriceSell(final double materialPriceSell) {
        this.materialPriceSell = materialPriceSell;
    }

    public Blueprint getBlueprint() {
        return blueprint;
    }

    public void setBlueprint(final Blueprint blueprint) {
        this.blueprint = blueprint;
    }

    public double getProductionCost() {
        return productionCost;
    }

    public void setProductionCost(final double productionCost) {
        this.productionCost = productionCost;
    }

    @Override
    public String toString() {
        return "BlueprintProduction [productPrice=" + EveFormatter.formatIsk(productPrice) + ", materialPriceBuy="
                + EveFormatter.formatIsk(materialPriceBuy) + ", materialPriceSell=" + EveFormatter.formatIsk(materialPriceSell) + ", productionCost="
                + EveFormatter.formatIsk(productionCost) + ", blueprint=" + blueprint + "]";
    }
}
