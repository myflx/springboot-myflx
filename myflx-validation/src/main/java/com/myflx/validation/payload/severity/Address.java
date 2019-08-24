package com.myflx.validation.payload.severity;

import javax.validation.constraints.NotNull;


/**
 * The payload information can be retrieved from error reports via the ConstraintDescriptor either accessed
 * through the ConstraintViolation objects (see Section 4.2) or through the metadata API (see Section 5.5).
 */
public class Address {
    @NotNull(message="would be nice if we had one", payload=Severity.Info.class)
    public String getZipCode() {
        return null;
    }
    @NotNull(message="the city is mandatory", payload=Severity.Error.class)
    public String getCity() {
        return null;
    }
}