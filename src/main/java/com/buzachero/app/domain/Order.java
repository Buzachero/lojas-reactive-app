package com.buzachero.app.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.buzachero.app.enumeration.OrderStatus;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotEmpty;
import java.util.Date;
import java.util.List;

@Document
public class Order {
    @Id
    private String id;
    private Address address;
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    private Date confirmationDate;
    private OrderStatus status;
    private Store store;
    @NotEmpty(message = "list of orderItems is empty")
    private List<OrderItem> orderItemList;
    private Payment payment;

    public Order(Address address, Date confirmationDate, Store store, List<OrderItem> orderItemList, Payment payment) {
        this.address = address;
        this.confirmationDate = confirmationDate;
        this.status = OrderStatus.PENDING; // assuming that whenever a order is created, its initial status is pending (waiting for payment confirmation)
        this.store = store;
        this.orderItemList = orderItemList;
        this.payment = payment;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Date getConfirmationDate() {
        return confirmationDate;
    }

    public void setConfirmationDate(Date confirmationDate) {
        this.confirmationDate = confirmationDate;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public List<OrderItem> getOrderItemList() {
        return orderItemList;
    }

    public void setOrderItemList(List<OrderItem> orderItemList) {
        this.orderItemList = orderItemList;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }
}
