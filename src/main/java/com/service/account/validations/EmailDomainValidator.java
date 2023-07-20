package com.service.account.validations;


import com.service.account.annotations.EmailDomainConstraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EmailDomainValidator implements ConstraintValidator<EmailDomainConstraint, String> {

    @Override
    public void initialize(EmailDomainConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }
        return value.endsWith("@acme.com");
    }
}
