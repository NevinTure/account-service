package com.service.account.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@Entity
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
    @Column(name = "user_id")
    private long id;

    @Column(name = "name")
    @Setter
    @Getter
    private String name;

    @Column(name = "lastname")
    @Setter
    @Getter
    private String lastname;

    @Column(name = "email", unique = true)
    @Setter
    @Getter
    private String email;

    @Column(name = "password")
    @Setter
    @Getter
    private String password;

    @Getter
    @Setter
    @OneToMany(mappedBy = "owner")
    private List<Payment> payments;

    @Getter
    @Setter
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_groups",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "group_id")
    )
    private Set<Group> userGroups;

    @Getter
    @Setter
    @Column(name = "user_non_locked")
    private boolean accountNonLocked;

    @Getter
    @Setter
    @Column(name = "failed_attempts")
    private int failedAttempts;

    @Getter
    @Setter
    @Transient
    private boolean accountNonExpired;

    @Getter
    @Setter
    @Transient
    private boolean credentialsNonExpired;

    @Getter
    @Setter
    @Transient
    private boolean enabled;

    public User() {
        accountNonExpired = true;
        accountNonLocked = true;
        credentialsNonExpired = true;
        enabled = true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return userGroups.stream().map(v -> new SimpleGrantedAuthority(v.getName().toUpperCase())).toList();
    }

    @Override
    public String getUsername() {
        return email.toLowerCase();
    }

}
