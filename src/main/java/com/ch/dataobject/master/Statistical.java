package com.ch.dataobject.master;

public class Statistical {
    private String xdate;
    private Double garbageWeight;
    private Integer quantity;

    public Statistical() {
    }


    public String getXdate() {
        return xdate;
    }

    public void setXdate(String xdate) {
        this.xdate = xdate;
    }

    public Double getGarbageWeight() {
        return garbageWeight;
    }

    public void setGarbageWeight(Double garbageWeight) {
        this.garbageWeight = garbageWeight;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "Statistical{" +
                "xdate='" + xdate + '\'' +
                ", garbageWeight=" + garbageWeight +
                ", quantity=" + quantity +
                '}';
    }
}
