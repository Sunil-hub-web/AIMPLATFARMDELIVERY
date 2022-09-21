package com.aimplatfarm.aimplatfarmdelivery.Models.requestDto;

import java.io.Serializable;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum implements Serializable {

    @SerializedName("paymentDetails")
    @Expose
    private PaymentDetails paymentDetails;
    @SerializedName("trackingDetails")
    @Expose
    private TrackingDetails trackingDetails;
    @SerializedName("shippingDetails")
    @Expose
    private ShippingDetails shippingDetails;
    @SerializedName("expectedDelivery")
    @Expose
    private String expectedDelivery;
    @SerializedName("discountPrice")
    @Expose
    private double discountPrice;
    @SerializedName("createdAt")
    @Expose
    private String createdAt;
    @SerializedName("updatedAt")
    @Expose
    private String updatedAt;
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("oderedBy")
    @Expose
    private String oderedBy;
    @SerializedName("cart_id")
    @Expose
    private String cartId;
    @SerializedName("products")
    @Expose
    private List<Product> products = null;
    @SerializedName("orderImg")
    @Expose
    private String orderImg;
    @SerializedName("shippingCharge")
    @Expose
    private double shippingCharge;
    @SerializedName("totalAmount")
    @Expose
    private double totalAmount;
    @SerializedName("seller")
    @Expose
    private String seller;
    @SerializedName("orderStatus")
    @Expose
    private String orderStatus;
    @SerializedName("__v")
    @Expose
    private double v;
    @SerializedName("deliveryAgent")
    @Expose
    private String deliveryAgent;
    @SerializedName("DriverDettails")
    @Expose
    private List<DriverDettails> driverDettails = null;

    @SerializedName("warehouse_id")
    @Expose
    private WarehouseId warehouseId;

//    private String warehouse_id;
//
//    public String getWarehouse_id() {
//        return warehouse_id;
//    }
//
//    public void setWarehouse_id(String warehouse_id) {
//        this.warehouse_id = warehouse_id;
//    }


    public WarehouseId getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(WarehouseId warehouseId) {
        this.warehouseId = warehouseId;
    }

    public PaymentDetails getPaymentDetails() {
        return paymentDetails;
    }

    public void setPaymentDetails(PaymentDetails paymentDetails) {
        this.paymentDetails = paymentDetails;
    }

    public TrackingDetails getTrackingDetails() {
        return trackingDetails;
    }

    public void setTrackingDetails(TrackingDetails trackingDetails) {
        this.trackingDetails = trackingDetails;
    }

    public ShippingDetails getShippingDetails() {
        return shippingDetails;
    }

    public void setShippingDetails(ShippingDetails shippingDetails) {
        this.shippingDetails = shippingDetails;
    }

    public String getExpectedDelivery() {
        return expectedDelivery;
    }

    public void setExpectedDelivery(String expectedDelivery) {
        this.expectedDelivery = expectedDelivery;
    }

    public double getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(double discountPrice) {
        this.discountPrice = discountPrice;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOderedBy() {
        return oderedBy;
    }

    public void setOderedBy(String oderedBy) {
        this.oderedBy = oderedBy;
    }

    public String getCartId() {
        return cartId;
    }

    public void setCartId(String cartId) {
        this.cartId = cartId;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public String getOrderImg() {
        return orderImg;
    }

    public void setOrderImg(String orderImg) {
        this.orderImg = orderImg;
    }

    public double getShippingCharge() {
        return shippingCharge;
    }

    public void setShippingCharge(double shippingCharge) {
        this.shippingCharge = shippingCharge;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public double getV() {
        return v;
    }

    public void setV(double v) {
        this.v = v;
    }

    public String getDeliveryAgent() {
        return deliveryAgent;
    }

    public void setDeliveryAgent(String deliveryAgent) {
        this.deliveryAgent = deliveryAgent;
    }

    public List<DriverDettails> getDriverDettails() {
        return driverDettails;
    }

    public void setDriverDettails(List<DriverDettails> driverDettails) {
        this.driverDettails = driverDettails;
    }

}