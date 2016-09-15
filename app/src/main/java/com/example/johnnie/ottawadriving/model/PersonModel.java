package com.example.johnnie.ottawadriving.model;

import java.io.Serializable;

/**
 * Created by Johnnie on 2016-09-14.
 */
public class PersonModel implements Serializable{

    private long Id;
    private String name;
    private String address;
    private String information;
    private String phoneNumber;
    private String email;



    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }
    public long getId() {
        return Id;
    }

    public void setId(long id) {
        Id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    private String imageUri;
}
