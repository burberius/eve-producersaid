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

public class EveCentralPrice {
    private int typeId;
    private long time;
    private float buyMax;
    private float buy5Percent;
    private long buyVolume;
    private float sellMin;
    private float sell5Percent;
    private long sellVolume;

    public EveCentralPrice() {
	super();
    }

    public EveCentralPrice(int typeId, long time) {
	super();
	this.typeId = typeId;
	this.time = time;
    }

    public int getTypeId() {
	return typeId;
    }

    public void setTypeId(int typeId) {
	this.typeId = typeId;
    }

    public long getTime() {
	return time;
    }

    public void setTime(long time) {
	this.time = time;
    }

    public float getBuyMax() {
	return buyMax;
    }

    public void setBuyMax(float buyMax) {
	this.buyMax = buyMax;
    }

    public long getBuyVolume() {
	return buyVolume;
    }

    public void setBuyVolume(long buyVolume) {
	this.buyVolume = buyVolume;
    }

    public long getSellVolume() {
	return sellVolume;
    }

    public void setSellVolume(long sellVolume) {
	this.sellVolume = sellVolume;
    }

    public float getBuy5Percent() {
	return buy5Percent;
    }

    public void setBuy5Percent(float buyPercentile) {
	this.buy5Percent = buyPercentile;
    }

    public float getSellMin() {
	return sellMin;
    }

    public void setSellMin(float sellMin) {
	this.sellMin = sellMin;
    }

    public float getSell5Percent() {
	return sell5Percent;
    }

    public void setSell5Percent(float sellPercentile) {
	this.sell5Percent = sellPercentile;
    }

    @Override
    public String toString() {
	return "EveCentralPrice [typeId=" + typeId + ", time=" + time + ", buyMax=" + buyMax + ", buy5Percent=" + buy5Percent + ", buyVolume="
		+ buyVolume + ", sellMin=" + sellMin + ", sell5Percent=" + sell5Percent + ", sellVolume=" + sellVolume + "]";
    }
}
