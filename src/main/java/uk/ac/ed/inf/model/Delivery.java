package uk.ac.ed.inf.model;

import uk.ac.ed.inf.ilp.constant.OrderStatus;
import uk.ac.ed.inf.ilp.constant.OrderValidationCode;

public class Delivery {
    private String orderNo;
    private OrderStatus orderStatus;
    private OrderValidationCode orderValidationCode;
    private int priceTotalInPence;

    public Delivery(String orderNo, OrderStatus orderStatus, OrderValidationCode orderValidationCode, int priceTotalInPence) {
        this.orderNo = orderNo;
        this.orderStatus = orderStatus;
        this.orderValidationCode = orderValidationCode;
        this.priceTotalInPence = priceTotalInPence;
    }

    public Delivery() {
        this.orderNo = "";
        this.orderStatus = null;
        this.orderValidationCode = null;
        this.priceTotalInPence = 0;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public OrderValidationCode getOrderValidationCode() {
        return orderValidationCode;
    }

    public int getPriceTotalInPence() {
        return priceTotalInPence;
    }

}
