package com.service.account.dto;

import com.service.account.util.DateDeserializer;
import com.service.account.util.DateSerializer;
import com.service.account.util.MoneySerializer;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

public class PaymentDTO implements Comparable<PaymentDTO>{

    @Getter
    @Setter
    @JsonProperty("employee")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @NotEmpty
    private String userEmail;

    @Getter
    @Setter
    @JsonProperty("name")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String userName;

    @Getter
    @Setter
    @JsonProperty("lastname")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String userLastname;

    @Getter
    @Setter
    @JsonProperty("period")
    @JsonDeserialize(using = DateDeserializer.class)
    @JsonSerialize(using = DateSerializer.class)
    @NotNull
    private LocalDate period;

    @Getter
    @Setter
    @JsonProperty("salary")
    @JsonSerialize(using = MoneySerializer.class)
    @NotNull
    @Min(value = 0, message = "Salary must be non negative!")
    private Long salary;

    @Override
    public int compareTo(PaymentDTO o) {
        return o.getPeriod().compareTo(getPeriod());
    }
}
