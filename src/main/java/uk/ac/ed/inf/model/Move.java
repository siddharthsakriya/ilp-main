package uk.ac.ed.inf.model;

public class Move {
    private String orderNo;
    private String fromLongitude;
    private String fromLatitude;
    private String toLongitude;
    private String toLatitude;

    public Move(String orderNo, String fromLongitude, String fromLatitude, String toLongitude, String toLatitude) {
        this.orderNo = orderNo;
        this.fromLongitude = fromLongitude;
        this.fromLatitude = fromLatitude;
        this.toLongitude = toLongitude;
        this.toLatitude = toLatitude;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public String getFromLongitude() {
        return fromLongitude;
    }

    public String getFromLatitude() {
        return fromLatitude;
    }

    public String getToLongitude() {
        return toLongitude;
    }

    public String getToLatitude() {
        return toLatitude;
    }

    public String setOrderNo(String orderNo) {
        this.orderNo = orderNo;
        return orderNo;
    }

    public String setFromLongitude(String fromLongitude) {
        this.fromLongitude = fromLongitude;
        return fromLongitude;
    }

    public String setFromLatitude(String fromLatitude) {
        this.fromLatitude = fromLatitude;
        return fromLatitude;
    }

    public String setToLongitude(String toLongitude) {
        this.toLongitude = toLongitude;
        return toLongitude;
    }

    public String setToLatitude(String toLatitude) {
        this.toLatitude = toLatitude;
        return toLatitude;
    }
}
