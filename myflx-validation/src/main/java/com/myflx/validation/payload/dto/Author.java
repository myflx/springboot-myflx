package com.myflx.validation.payload.dto;

import com.myflx.validation.annotation.NotEmpty;

import javax.validation.constraints.Size;

public class Author {
    private String firstName;
    @NotEmpty(message = "lastname must not be null")
    private String lastName;
    @Size(max = 30)
    private String company;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }
}
