package com.service.account.view;

import com.service.account.annotations.PaymentListConstraint;
import com.service.account.dto.PaymentDTO;
import com.service.account.exceptions.PaymentUpdateException;
import com.service.account.models.Payment;
import com.service.account.models.User;
import com.service.account.services.PaymentService;
import com.service.account.services.UserService;
import com.service.account.util.StatusMessage;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/acct")
@Validated
public class AccountController {

    private final UserService userService;
    private final PaymentService paymentService;

    @Autowired
    public AccountController(UserService userService, PaymentService paymentService) {
        this.userService = userService;
        this.paymentService = paymentService;
    }

    @PutMapping("/payments")
    @ResponseBody
    public ResponseEntity<StatusMessage> updatePayment(@Valid @RequestBody PaymentDTO paymentDTO) {
        Optional<User> user = userService.getUserByEmail(paymentDTO.getUserEmail());
        if (user.isEmpty()) {
            throw new PaymentUpdateException("No such employee!");
        }
        if (paymentDTO.getSalary() < 0) {
            throw new PaymentUpdateException("Salary must be non negative!");
        }
        Payment payment = paymentService.getPaymentIfExist(user.get(), paymentDTO.getPeriod());
        if (payment != null) {
            payment.setSalary(new BigDecimal(paymentDTO.getSalary()).divide(new BigDecimal(100), 4, RoundingMode.HALF_UP));
            paymentService.save(payment);
            return new ResponseEntity<>(new StatusMessage("Updated successfully!"), HttpStatus.OK);

        }
        throw new PaymentUpdateException("No such period!");
    }


    @PostMapping(value = "/payments", consumes = "application/json")
    @ResponseBody
    public ResponseEntity<StatusMessage> addPayments(
            @RequestBody
            @PaymentListConstraint
            List<@Valid PaymentDTO> paymentsDTO, BindingResult result) {
        if (result.hasErrors()) {
            throw new PaymentUpdateException(result.getAllErrors().toString());
        }
        paymentService.saveAllWithConversion(paymentsDTO);

        return new ResponseEntity<>(new StatusMessage("Added successfully!"), HttpStatus.OK);
    }
}
