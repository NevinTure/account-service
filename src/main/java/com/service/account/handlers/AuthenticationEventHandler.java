package com.service.account.handlers;

import com.service.account.models.User;
import com.service.account.services.LogService;
import com.service.account.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AuthenticationEventHandler {

    public static final int MAX_FAILED_ATTEMPTS = 5;
    private final LogService logService;
    private final UserService userService;

    @Autowired
    public AuthenticationEventHandler(LogService logService, UserService userService) {
        this.logService = logService;
        this.userService = userService;
    }

    @EventListener
    public void onSuccess(AuthenticationSuccessEvent success) {
        UserDetails details = (UserDetails) success.getAuthentication().getPrincipal();
        userService.resetUserAttempts(details.getUsername());
    }

    @EventListener
    public void onFailure(AbstractAuthenticationFailureEvent failures) {
        String email = (String) failures.getAuthentication().getPrincipal();
        Optional<User> userOptional = userService.getUserByEmail(email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (!user.isAccountNonLocked()) {
                return;
            }
            userService.increaseFailedAttemptsByUser(user);
            if (user.getFailedAttempts() >= MAX_FAILED_ATTEMPTS) {
                userService.lockUser(user);
            }
        } else {
            logService.authenticationFailed(email);
        }
    }

}

