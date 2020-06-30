package com.buzachero.app.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.buzachero.app.enumeration.PaymentStatus;
import java.util.Date;

public class Payment {
    private PaymentStatus status;
    private String creditCardNumber; // we should add some functionality to mask this number (security purpose)
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    private Date paymentDate;

    public Payment(PaymentStatus status, String creditCardNumber, Date paymentDate) {
        this.status = status;
        this.creditCardNumber = creditCardNumber;
        this.paymentDate = paymentDate;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }

    public String getCreditCardNumber() {
        return creditCardNumber;
    }

    public void setCreditCardNumber(String creditCardNumber) {
        this.creditCardNumber = creditCardNumber;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }
}

