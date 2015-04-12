package net.troja.eve.producersaid;

public class InvType {
    private int id;
    private String name;
    private String description;
    private int groupId;
    private int marketGroupId;
    private int mass;
    private double volume;

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

    public int getMass() {
        return mass;
    }

    public void setMass(int mass) {
        this.mass = mass;
    }

    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }

    @Override
    public String toString() {
	return "InvType [id=" + id + ", name=" + name + ", description=" + description + ", groupId=" + groupId + ", marketGroupId=" + marketGroupId
		+ ", mass=" + mass + ", volume=" + volume + "]";
    }
}
