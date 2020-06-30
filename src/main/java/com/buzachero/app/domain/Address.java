package com.buzachero.app.domain;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

public class Address implements Serializable {
    @NotEmpty(message = "address location is empty")
    private String location;
    @NotEmpty(message = "address city is empty")
    private String city;
    @NotEmpty(message = "address state is empty")
    private String state;
    @NotEmpty(message = "address country is empty")
    private String country;

    public Address(String location, String city, String state, String country) {
        this.location = location;
        this.city = city;
        this.state = state;
        this.country = country;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
