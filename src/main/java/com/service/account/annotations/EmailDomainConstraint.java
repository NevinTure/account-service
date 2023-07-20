package com.service.account.annotations;



import com.service.account.validations.EmailDomainValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Constraint(validatedBy = EmailDomainValidator.class)
@Target({ ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface EmailDomainConstraint {
    String message() default "Invalid email domain";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
