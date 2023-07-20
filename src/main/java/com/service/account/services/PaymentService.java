package com.service.account.services;

import com.service.account.dto.PaymentDTO;
import com.service.account.models.Payment;
import com.service.account.models.User;
import com.service.account.repositories.PaymentRepository;
import com.service.account.repositories.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;



    private final ModelMapper mapper;


    public PaymentService(PaymentRepository paymentRepository, UserRepository userRepository, ModelMapper mapper) {
        this.paymentRepository = paymentRepository;
        this.userRepository = userRepository;
        this.mapper = mapper;
    }

    public void save(Payment payment) {
        paymentRepository.save(payment);
    }

    public void saveAll(List<Payment> paymentList) {
        paymentRepository.saveAll(paymentList);
    }

    public void saveAllWithConversion(List<PaymentDTO> paymentDTOList) {
        List<Payment> payments = new ArrayList<>();
        for (PaymentDTO paymentDTO : paymentDTOList) {
            User user = userRepository.findByEmail(paymentDTO.getUserEmail()).get();
            Payment payment = mapper.map(paymentDTO, Payment.class);
            payment.setOwner(user);
            payments.add(payment);
        }
        saveAll(payments);
    }

    public Payment getPaymentIfExist(User user, LocalDate date) {
        for(Payment payment : user.getPayments()) {
            if(payment.getPeriod().equals(date)) {
                return payment;
            }
        }
        return null;
    }
}
