package com.service.account.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
public class StatusMessage {

    @Getter
    @Setter
    private String status;

    @Getter
    @Setter
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("email")
    private String userEmail;

    @Getter
    @Setter
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("user")
    private String username;

    public StatusMessage(String status) {
        this.status = status;
    }
}
