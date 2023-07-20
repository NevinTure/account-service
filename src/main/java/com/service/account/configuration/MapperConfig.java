package com.service.account.configuration;

import com.service.account.dto.PaymentDTO;
import com.service.account.models.Payment;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Configuration
public class MapperConfig {

    @Bean
    public ModelMapper mapper() {
        ModelMapper mapper = new ModelMapper();
        TypeMap<PaymentDTO, Payment> fromPaymentDTO = mapper.createTypeMap(PaymentDTO.class, Payment.class);
        TypeMap<Payment, PaymentDTO> toPaymentDTO = mapper.createTypeMap(Payment.class, PaymentDTO.class);
        Converter<Long, BigDecimal> longToDecimal = c -> new BigDecimal(c.getSource()).divide(new BigDecimal(100), 4, RoundingMode.HALF_UP);
        Converter<BigDecimal, Long> decimalToLong = c -> c.getSource().multiply(new BigDecimal(100)).longValue();
        fromPaymentDTO.addMappings(innerMapper -> innerMapper.using(longToDecimal).map(PaymentDTO::getSalary, Payment::setSalary));
        toPaymentDTO.addMappings(innerMapper -> innerMapper.using(decimalToLong).map(Payment::getSalary, PaymentDTO::setSalary));

        return mapper;
    }
}
