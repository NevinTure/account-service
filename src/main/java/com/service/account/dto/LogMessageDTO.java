package com.service.account.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
public class LogMessageDTO {

    @Getter
    @Setter
    @NotEmpty
    private LocalDate date;

    @Getter
    @Setter
    @NotEmpty
    @JsonProperty("action")
    private String eventName;

    @Getter
    @Setter
    @NotEmpty
    private String subject;

    @Getter
    @Setter
    @NotEmpty
    private String object;

    @Getter
    @Setter
    @NotEmpty
    private String path;
}
