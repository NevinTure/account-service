package com.service.account.dto;

import com.service.account.annotations.EmailDomainConstraint;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.TreeSet;


@NoArgsConstructor
@AllArgsConstructor
public class UserDTO implements Comparable<UserDTO> {

    @JsonProperty("id")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Getter
    @Setter
    private Long id;

    @NotNull
    @NotEmpty
    @JsonProperty("name")
    @Getter
    @Setter
    private String name;
    @NotNull
    @NotEmpty
    @JsonProperty("lastname")
    @Getter
    @Setter
    private String lastName;
    @NotNull
    @NotEmpty
    @JsonProperty("email")
    @Email
    @EmailDomainConstraint
    @Getter
    @Setter
    private String email;
    @NotEmpty
    @JsonProperty("password")
    @Getter
    @Setter
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String password;

    @JsonProperty("roles")
    @Getter
    @Setter
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private TreeSet<String> userGroups;

    @Override
    public int compareTo(UserDTO o) {
        return this.getId().compareTo(o.getId());
    }
}
