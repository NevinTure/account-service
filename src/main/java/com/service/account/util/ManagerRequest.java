package com.service.account.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
public class ManagerRequest {

    @NotEmpty
    @NotNull
    @JsonProperty("user")
    @Getter
    @Setter
    private String email;

    @NotNull
    @NotEmpty
    @Getter
    @Setter
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String role;

    @NotNull
    @NotEmpty
    @Getter
    @Setter
    private String operation;
}
