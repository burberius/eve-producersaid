package net.troja.eve.producersaid;

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
