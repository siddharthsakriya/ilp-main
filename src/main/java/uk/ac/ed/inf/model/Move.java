package uk.ac.ed.inf.model;

public class Move {
    private String orderNo;
    private Double fromLongitude;
    private Double fromLatitude;
    private Double toLongitude;
    private Double toLatitude;


    public Move(){
        this.orderNo = "";
        this.fromLongitude = null;
        this.fromLatitude = null;
        this.toLongitude = null;
        this.toLatitude = null;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public Double getFromLongitude() {
        return fromLongitude;
    }

    public Double getFromLatitude() {
        return fromLatitude;
    }

    public Double getToLongitude() {
        return toLongitude;
    }

    public Double getToLatitude() {
        return toLatitude;
    }

    public String setOrderNo(String orderNo) {
        this.orderNo = orderNo;
        return orderNo;
    }

    public Double setFromLongitude(Double fromLongitude) {
        this.fromLongitude = fromLongitude;
        return fromLongitude;
    }

    public Double setFromLatitude(Double fromLatitude) {
        this.fromLatitude = fromLatitude;
        return fromLatitude;
    }

    public Double setToLongitude(Double toLongitude) {
        this.toLongitude = toLongitude;
        return toLongitude;
    }

    public Double setToLatitude(Double toLatitude) {
        this.toLatitude = toLatitude;
        return toLatitude;
    }
}
