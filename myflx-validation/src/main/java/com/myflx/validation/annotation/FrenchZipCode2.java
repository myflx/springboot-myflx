package com.myflx.validation.annotation;

import javax.validation.Constraint;
import javax.validation.OverridesAttribute;
import javax.validation.Payload;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Pattern(regexp="[0-9]*")
@Size
@Constraint(validatedBy = FrenchZipCodeValidator2.class)
@Documented
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
@Retention(RUNTIME)
public @interface FrenchZipCode2 {
    String message() default "Wrong zipCode";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    @OverridesAttribute.List( {
            @OverridesAttribute(constraint=Size.class, name="min"),
            @OverridesAttribute(constraint=Size.class, name="max") } )
    int size() default 5;
    @OverridesAttribute(constraint=Size.class, name="message")
    String sizeMessage() default "{com.acme.constraint.FrenchZipcode.zipcode.size}";
    @OverridesAttribute(constraint=Pattern.class, name="message")
    String numberMessage() default "{com.acme.constraint.FrenchZipcode.number.size}";

    @Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
    @Retention(RUNTIME)
    @Documented
    @interface List {
        FrenchZipCode2[] value();
    }
}
