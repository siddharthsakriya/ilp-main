package uk.ac.ed.inf.model;

public class Delivery {
    private final String orderNo;
    private final String orderStatus;
    private final String orderValidationCode;
    private final int priceTotalInPence;

    public Delivery(String orderNo, String orderStatus, String orderValidationCode, int priceTotalInPence) {
        this.orderNo = orderNo;
        this.orderStatus = orderStatus;
        this.orderValidationCode = orderValidationCode;
        this.priceTotalInPence = priceTotalInPence;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public String getOrderValidationCode() {
        return orderValidationCode;
    }

    public int getPriceTotalInPence() {
        return priceTotalInPence;
    }

}
