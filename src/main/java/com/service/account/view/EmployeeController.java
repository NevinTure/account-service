package com.service.account.view;

import com.service.account.dto.PaymentDTO;
import com.service.account.models.Payment;
import com.service.account.models.User;
import com.service.account.services.PaymentService;
import com.service.account.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/empl")
@Validated
public class EmployeeController {

    private final UserService userService;
    private final PaymentService paymentService;
    private final ModelMapper mapper;

    @Autowired
    public EmployeeController(UserService userService, PaymentService paymentService, ModelMapper mapper) {
        this.userService = userService;
        this.paymentService = paymentService;
        this.mapper = mapper;
    }


    @GetMapping("/payment")
    @ResponseBody
    public ResponseEntity<Object> getEmployeePayments(@RequestParam(value = "period", required = false) String periodS) {
        UserDetails details = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.getUserByEmail(details.getUsername()).get();
        List<PaymentDTO> paymentDTOList = paymentsToPaymentDTOs(user);
        if (periodS == null) {
            Collections.sort(paymentDTOList);
            return new ResponseEntity<>(paymentDTOList, HttpStatus.OK);

        }
        LocalDate date = LocalDate.parse("01-"+periodS, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        Payment payment = paymentService.getPaymentIfExist(user, date);
        if(payment == null) {
            throw new DateTimeParseException("No such period!", "", 0);
        }
        PaymentDTO paymentDTO = mapper.map(payment, PaymentDTO.class);
        paymentDTO.setUserEmail(null);
        paymentDTO.setUserName(user.getName());
        paymentDTO.setUserLastname(user.getLastname());
        return new ResponseEntity<>(paymentDTO, HttpStatus.OK);
    }

    private List<PaymentDTO> paymentsToPaymentDTOs(User user) {
        List<Payment> payments = user.getPayments();
        List<PaymentDTO> paymentDTOList = new ArrayList<>();
        for(Payment payment : payments) {
            PaymentDTO paymentDTO = mapper.map(payment, PaymentDTO.class);
            paymentDTO.setUserEmail(null);
            paymentDTO.setUserName(user.getName());
            paymentDTO.setUserLastname(user.getLastname());
            paymentDTOList.add(paymentDTO);
        }
        return paymentDTOList;
    }
}
