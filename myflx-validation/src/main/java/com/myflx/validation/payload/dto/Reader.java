package com.myflx.validation.payload.dto;

import com.myflx.validation.annotation.FrenchZipCode;
import com.myflx.validation.annotation.FrenchZipCode2;

public class Reader {

    /*@FrenchZipCode.List({
            @FrenchZipCode(countryCode = "us",message = "11"),
            @FrenchZipCode(countryCode = "fr",message = "22")
    })
    @FrenchZipCode(countryCode = "fr",message = "226")*/
    private String zipCode;

    @FrenchZipCode2
    private String zipCode2;

    /*@FrenchZipCode.List({
            @FrenchZipCode(message = "11"),
            @FrenchZipCode(message = "56556")
    })*/
    @FrenchZipCode(message = "56556")
    private String zipCode3;

    public String getZipCode3() {
        return zipCode3;
    }

    public void setZipCode3(String zipCode3) {
        this.zipCode3 = zipCode3;
    }

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
