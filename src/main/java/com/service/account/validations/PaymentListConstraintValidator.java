package com.service.account.validations;

import com.service.account.annotations.PaymentListConstraint;
import com.service.account.dto.PaymentDTO;
import com.service.account.repositories.UserRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class PaymentListConstraintValidator implements ConstraintValidator<PaymentListConstraint, List<PaymentDTO>> {

    @Autowired
    private UserRepository repository;

    @Override
    public void initialize(PaymentListConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(List<PaymentDTO> value, ConstraintValidatorContext context) {
        Map<String, Set<LocalDate>> periods = new HashMap<>();
        for(PaymentDTO payment : value) {
            if (payment.getSalary() < 0) {
                return false;
            }
            if (repository.findByEmail(payment.getUserEmail()).isEmpty()) {
                return false;
            }
            if (!periods.containsKey(payment.getUserEmail())) {
                periods.put(payment.getUserEmail(), new HashSet<>());
            }
            long between;
            for(LocalDate period : periods.get(payment.getUserEmail())) {
                between = ChronoUnit.YEARS.between(
                        YearMonth.from(period),
                        YearMonth.from(payment.getPeriod())
                );
                System.out.println(between);
                if (between == 0) {
                    between = ChronoUnit.MONTHS.between(
                            YearMonth.from(period),
                            YearMonth.from(payment.getPeriod())
                    );
                    System.out.println(between);
                    if (between == 0) {
                        return false;
                    }
                }
            }
            periods.get(payment.getUserEmail()).add(payment.getPeriod());
        }
        return true;
    }
}
