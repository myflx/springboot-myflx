package com.myflx.validation.payload.dto;

import com.myflx.validation.annotation.FrenchZipCode;
import com.myflx.validation.annotation.FrenchZipCode2;

public class Reader {
    @FrenchZipCode
    private String zipCode;

    @FrenchZipCode2
    private String zipCode2;

    public String getZipCode2() {
        return zipCode2;
    }

    public void setZipCode2(String zipCode2) {
        this.zipCode2 = zipCode2;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }
}
