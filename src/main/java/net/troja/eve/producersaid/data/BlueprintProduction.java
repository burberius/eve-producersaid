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


public class BlueprintProduction {
    private double productPrice;
    private double materialPriceBuy;
    private double materialPriceBuyWithSubs;
    private double materialPriceSell;
    private double materialPriceSellWithSubs;
    private double productionCost;
    private double productionCostWithSubs;
    private double productionBasePrice;
    private double productionBasePriceWithSubs;
    private long updateTime;
    private int blueprintTypeId;
    private String blueprintName;
    private int productTypeId;
    private String productName;
    private int productQuantity;
    private int perfectME;
    private int techLevel;

    public BlueprintProduction() {
        super();
    }

    public BlueprintProduction(final BlueprintProduction original, final double costIndex) {
        super();
        productPrice = original.getProductPrice();
        materialPriceBuy = original.getMaterialPriceBuy();
        materialPriceBuyWithSubs = original.getMaterialPriceBuyWithSubs();
        materialPriceSell = original.getMaterialPriceSell();
        materialPriceSellWithSubs = original.getMaterialPriceSellWithSubs();
        updateTime = original.getUpdateTime();
        productionBasePrice = original.getProductionBasePrice();
        productionBasePriceWithSubs = original.getProductionBasePriceWithSubs();
        productionCost = original.getProductionBasePrice() * costIndex * 1.1;
        productionCostWithSubs = original.getProductionBasePriceWithSubs() * costIndex * 1.1;
        blueprintTypeId = original.getBlueprintTypeId();
        blueprintName = original.getBlueprintName();
        perfectME = original.getPerfectME();
        productTypeId = original.getProductTypeId();
        productName = original.getProductName();
        productQuantity = original.getProductQuantity();
        techLevel = original.getTechLevel();
    }

    public void addBlueprintData(final Blueprint blueprint) {
        blueprintTypeId = blueprint.getId();
        blueprintName = blueprint.getName();
        perfectME = blueprint.getMaterialEfficiency();
        final BlueprintProduct blueprintProduct = blueprint.getManufacturing().getProducts().get(0);
        productTypeId = blueprintProduct.getTypeId();
        productName = blueprintProduct.getName();
        productQuantity = blueprintProduct.getQuantity();
        techLevel = blueprintProduct.getTechLevel();
    }

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

    public double getProductionCost() {
        return productionCost;
    }

    public void setProductionCost(final double productionCost) {
        this.productionCost = productionCost;
    }

    public double getProductionBasePrice() {
        return productionBasePrice;
    }

    public void setProductionBasePrice(final double productionBasePrice) {
        this.productionBasePrice = productionBasePrice;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(final long updateTime) {
        this.updateTime = updateTime;
    }

    public int getBlueprintTypeId() {
        return blueprintTypeId;
    }

    public void setBlueprintTypeId(final int blueprintTypeId) {
        this.blueprintTypeId = blueprintTypeId;
    }

    public String getBlueprintName() {
        return blueprintName;
    }

    public void setBlueprintName(final String blueprintName) {
        this.blueprintName = blueprintName;
    }

    public int getProductTypeId() {
        return productTypeId;
    }

    public void setProductTypeId(final int productTypeId) {
        this.productTypeId = productTypeId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(final String productName) {
        this.productName = productName;
    }

    public int getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(final int productQuantity) {
        this.productQuantity = productQuantity;
    }

    public int getPerfectME() {
        return perfectME;
    }

    public void setPerfectME(final int perfectME) {
        this.perfectME = perfectME;
    }

    public int getTechLevel() {
        return techLevel;
    }

    public void setTechLevel(final int techLevel) {
        this.techLevel = techLevel;
    }

    public double getMaterialPriceBuyWithSubs() {
        return materialPriceBuyWithSubs;
    }

    public void setMaterialPriceBuyWithSubs(final double materialPriceBuyWithSubs) {
        this.materialPriceBuyWithSubs = materialPriceBuyWithSubs;
    }

    public double getMaterialPriceSellWithSubs() {
        return materialPriceSellWithSubs;
    }

    public void setMaterialPriceSellWithSubs(final double materialPriceSellWithSubs) {
        this.materialPriceSellWithSubs = materialPriceSellWithSubs;
    }

    public double getProductionCostWithSubs() {
        return productionCostWithSubs;
    }

    public void setProductionCostWithSubs(final double productionCostWithSubs) {
        this.productionCostWithSubs = productionCostWithSubs;
    }

    public double getProductionBasePriceWithSubs() {
        return productionBasePriceWithSubs;
    }

    public void setProductionBasePriceWithSubs(final double productionBasePriceWithSubs) {
        this.productionBasePriceWithSubs = productionBasePriceWithSubs;
    }

    @Override
    public String toString() {
        return "BlueprintProduction [productPrice=" + productPrice + ", materialPriceBuy=" + materialPriceBuy + ", materialPriceBuyWithSubs="
                + materialPriceBuyWithSubs + ", materialPriceSell=" + materialPriceSell + ", materialPriceSellWithSubs=" + materialPriceSellWithSubs
                + ", productionCost=" + productionCost + ", productionCostWithSubs=" + productionCostWithSubs + ", productionBasePrice="
                + productionBasePrice + ", productionBasePriceWithSubs=" + productionBasePriceWithSubs + ", updateTime=" + updateTime
                + ", blueprintTypeId=" + blueprintTypeId + ", blueprintName=" + blueprintName + ", productTypeId=" + productTypeId + ", productName="
                + productName + ", productQuantity=" + productQuantity + ", perfectME=" + perfectME + ", techLevel=" + techLevel + "]";
    }
}
