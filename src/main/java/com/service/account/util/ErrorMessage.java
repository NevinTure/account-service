package com.service.account.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
public class ErrorMessage {

    @Getter
    @Setter
    @JsonProperty("timestamp")
    private LocalDate localDate;

    @Getter
    @Setter
    @JsonProperty("status")
    private int status;

    @Getter
    @Setter
    @JsonProperty("error")
    private String error;

    @Getter
    @Setter
    @JsonProperty("message")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String message;

    @Getter
    @Setter
    @JsonProperty("path")
    private String path;

    public ErrorMessage(LocalDate localDate, int status, String error, String path) {
        this.localDate = localDate;
        this.status = status;
        this.error = error;
        this.path = path;
    }
}
