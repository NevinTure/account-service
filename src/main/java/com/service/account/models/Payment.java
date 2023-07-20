package com.service.account.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;


import java.math.BigDecimal;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @Getter
    @Setter
    private Long id;

    @Column(name = "period")
    @Getter
    @Setter
    private LocalDate period;

    @Column(name = "salary", precision = 19, scale = 4)
    @Getter
    @Setter
    @NotNull
    @Min(value = 0, message = "Salary must be non negative!")
    private BigDecimal salary;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "employee_email", referencedColumnName = "email")
    private User owner;


}
