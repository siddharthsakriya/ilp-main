package uk.ac.ed.inf.model;

public class Move {
    private String orderNo;
    private Double fromLongitude;
    private Double fromLatitude;
    private double angle;
    private Double toLongitude;
    private Double toLatitude;

    public Move(){
        this.orderNo = "";
        this.fromLongitude = null;
        this.fromLatitude = null;
        this.angle = 0.0;
        this.toLongitude = null;
        this.toLatitude = null;
    }

    public Move(String orderNo, Double fromLongitude, Double fromLatitude, Double toLongitude, Double toLatitude, double angle){
        this.orderNo = orderNo;
        this.fromLongitude = fromLongitude;
        this.fromLatitude = fromLatitude;
        this.angle = angle;
        this.toLongitude = toLongitude;
        this.toLatitude = toLatitude;
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

    public double getAngle() {
        return angle;
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

    public void setAngle(double angle) {
        this.angle = angle;
    }

}
