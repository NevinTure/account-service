package com.service.account.annotations;


import com.service.account.validations.PaymentListConstraintValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

@Constraint(validatedBy = PaymentListConstraintValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({PARAMETER, METHOD, FIELD})
public @interface PaymentListConstraint {
    String message() default "The payment list is not valid";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
