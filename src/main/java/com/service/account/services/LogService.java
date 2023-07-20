package com.service.account.services;

import com.service.account.models.LogMessage;
import com.service.account.models.User;
import com.service.account.repositories.LogMessageRepository;
import com.service.account.util.EventName;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class LogService {

    private final HttpServletRequest request;
    private final LogMessageRepository logMessageRepository;

    public LogService(HttpServletRequest request, LogMessageRepository logMessageRepository) {
        this.request = request;
        this.logMessageRepository = logMessageRepository;
    }

    public void authenticationFailed(String email) {
        LogMessage logMessage = new LogMessage(request.getRequestURI());
        logMessage.setEventName(EventName.LOGIN_FAILED);
        logMessage.setSubject(email);
        logMessage.setObject(request.getRequestURI());
        save(logMessage);
    }

    public void createUser(User user) {
        LogMessage logMessage = new LogMessage(request.getRequestURI());
        logMessage.setEventName(EventName.CREATE_USER);
        logMessage.setSubject("Anonymous");
        logMessage.setObject(user.getEmail());
        save(logMessage);
    }

    public void changePass(String email) {
        LogMessage logMessage = new LogMessage(request.getRequestURI());
        logMessage.setEventName(EventName.CHANGE_PASSWORD);
        logMessage.setSubject(email);
        logMessage.setObject(email);
        save(logMessage);
    }

    public void accessDenied() {
        UserDetails details = getUserDetails();
        LogMessage logMessage = new LogMessage(request.getRequestURI());
        logMessage.setEventName(EventName.ACCESS_DENIED);
        logMessage.setSubject(details.getUsername());
        logMessage.setObject(request.getRequestURI());
        save(logMessage);
    }

    public void grantRole(User managedUser, String role) {
        role = role.replace("ROLE_", "");
        UserDetails details = getUserDetails();
        LogMessage logMessage = new LogMessage(request.getRequestURI());
        logMessage.setEventName(EventName.GRANT_ROLE);
        logMessage.setSubject(details.getUsername());
        logMessage.setObject(String.format("Grant role %s to %s", role, managedUser.getEmail()));
        save(logMessage);
    }

    public void removeRole(User managedUser, String role) {
        role = role.replace("ROLE_", "");
        UserDetails details = getUserDetails();
        LogMessage logMessage = new LogMessage(request.getRequestURI());
        logMessage.setEventName(EventName.REMOVE_ROLE);
        logMessage.setSubject(details.getUsername());
        logMessage.setObject(String.format("Remove role %s from %s", role, managedUser.getEmail()));
        save(logMessage);
    }

    public void lockUser(User user) {
        LogMessage logMessage = new LogMessage(request.getRequestURI());
        logMessage.setEventName(EventName.LOCK_USER);
        logMessage.setSubject(user.getEmail().toLowerCase());
        logMessage.setObject(String.format("Lock user %s", user.getEmail()));
        save(logMessage);
    }

    public void unlockUser(User user) {
        UserDetails details = getUserDetails();
        LogMessage logMessage = new LogMessage(request.getRequestURI());
        logMessage.setEventName(EventName.UNLOCK_USER);
        logMessage.setSubject(details.getUsername());
        logMessage.setObject(String.format("Unlock user %s", user.getEmail()));
        save(logMessage);
    }

    public void deleteUser(String managedUsername) {
        UserDetails details = getUserDetails();
        LogMessage logMessage = new LogMessage(request.getRequestURI());
        logMessage.setEventName(EventName.DELETE_USER);
        logMessage.setSubject(details.getUsername());
        logMessage.setObject(managedUsername);
        save(logMessage);
    }

    public void bruteForce(User user) {
        LogMessage logMessage = new LogMessage(request.getRequestURI());
        logMessage.setEventName(EventName.BRUTE_FORCE);
        logMessage.setSubject(user.getEmail());
        logMessage.setObject(request.getRequestURI());
        save(logMessage);
    }

    public void save(LogMessage logMessage) {
        logMessageRepository.save(logMessage);
    }

    public List<LogMessage> getAll() {
        return logMessageRepository.findAll();
    }

    private UserDetails getUserDetails() {
        return (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
