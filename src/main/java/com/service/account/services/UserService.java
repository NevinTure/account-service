package com.service.account.services;

import com.service.account.handlers.AuthenticationEventHandler;
import com.service.account.exceptions.PasswordChangeException;
import com.service.account.models.User;
import com.service.account.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
public class UserService implements UserDetailsService {

    private final UserRepository repository;
    private final GroupService groupService;
    private final LogService logService;
    private final PasswordEncoder encoder;
    private final List<String> branchedPasswords;


    @Autowired
    public UserService(UserRepository repository, GroupService groupService, LogService logService, PasswordEncoder encoder) {
        this.repository = repository;
        this.groupService = groupService;
        this.logService = logService;
        this.encoder = encoder;
        branchedPasswords = List.of("PasswordForJanuary", "PasswordForFebruary", "PasswordForMarch", "PasswordForApril",
                "PasswordForMay", "PasswordForJune", "PasswordForJuly", "PasswordForAugust",
                "PasswordForSeptember", "PasswordForOctober", "PasswordForNovember", "PasswordForDecember");
    }

    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = repository.findByEmail(username.toLowerCase());

        if(user.isPresent()) {
            return user.get();
        } else {
            throw new UsernameNotFoundException(
                    "User with username: %s not found".formatted(username));
        }
    }


    public Optional<User> getUserByEmail(String email) {
        return repository.findByEmail(email.toLowerCase());
    }

    public void save(User user) {
        if(!repository.existsById(1L)) {
            user.setUserGroups(Set.of(groupService.getByName("ROLE_ADMINISTRATOR")));
        } else {
            user.setUserGroups(Set.of(groupService.getByName("ROLE_USER")));
        }
        user.setEmail(user.getEmail().toLowerCase());
        user.setPassword(encoder.encode(user.getPassword()));
        repository.save(user);
    }

    public void update(User user) {
        repository.save(user);
    }
    public void changePassword(String email, String newPassword) {
        User user = getUserByEmail(email).get();
        if (checkNewPassword(user.getPassword(), newPassword)) {
            user.setPassword(encoder.encode(newPassword));
            update(user);
        }
    }

    public List<User> getAll() {
        return repository.findAll();
    }

    //TODO Сделать проверку через аннотацию Min(value, message)
    public boolean checkNewPassword(String oldPassword ,String newPassword) {
        if (newPassword.length() < 12) {
            throw new PasswordChangeException("Password length must be 12 chars minimum!");
        } else if (branchedPasswords.contains(newPassword)) {
            throw new PasswordChangeException("The password is in the hacker's database!");
        } else if (encoder.matches(newPassword, oldPassword)) {
            throw new PasswordChangeException("The passwords must be different!");
        }
        return true;
    }

    public void deleteUser(User user) {
        logService.deleteUser(user.getUsername());
        repository.delete(user);
    }

    public void resetUserAttempts(String userEmail) {
        User user = repository.findByEmail(userEmail).get();
        user.setFailedAttempts(0);
        update(user);
    }

    public void increaseFailedAttemptsByUser(User user) {
        logService.authenticationFailed(user.getEmail());
        user.setFailedAttempts(user.getFailedAttempts() + 1);
        update(user);
    }

    public void lockUser(User user) {
        // user with id == 1 - admin
        if (user.getId() == 1) {
            throw new LockedException("Can't lock the ADMINISTRATOR!");
        }
        if (user.getFailedAttempts() >= AuthenticationEventHandler.MAX_FAILED_ATTEMPTS) {
            logService.bruteForce(user);
        }
        user.setAccountNonLocked(false);
        user.setFailedAttempts(0);
        logService.lockUser(user);
        update(user);
    }

    public void unlockUser(User user) {
        user.setAccountNonLocked(true);
        user.setFailedAttempts(0);
        logService.unlockUser(user);
        update(user);
    }
}
