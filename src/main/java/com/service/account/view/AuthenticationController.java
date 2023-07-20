package com.service.account.view;



import com.service.account.dto.UserDTO;
import com.service.account.exceptions.EmailAlreadyUsedException;
import com.service.account.models.User;
import com.service.account.services.LogService;
import com.service.account.services.UserService;
import com.service.account.util.StatusMessage;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@Validated
public class AuthenticationController {

    private final UserService userService;
    private final LogService logService;
    private final ModelMapper mapper;

    @Autowired
    public AuthenticationController(UserService userService, LogService logService, ModelMapper mapper) {
        this.userService = userService;
        this.logService = logService;
        this.mapper = mapper;
    }

    @PostMapping("/signup")
    @ResponseBody
    public ResponseEntity<UserDTO> signUp(@Valid @RequestBody UserDTO receivedUser) {
        User user = mapper.map(receivedUser, User.class);
        if(userService.getUserByEmail(user.getEmail()).isPresent()) {
            throw new EmailAlreadyUsedException("User exist!");
        }
        userService.checkNewPassword("", user.getPassword());
        userService.save(user);
        logService.createUser(user);
        UserDTO userDTO = mapper.map(user, UserDTO.class);
        userDTO.setPassword(null);
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    @PostMapping("/changepass")
    @ResponseBody
    public ResponseEntity<StatusMessage> changePass(@RequestBody Map<String, String> newPassword) {
        UserDetails details = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        userService.changePassword(details.getUsername(), newPassword.get("new_password"));
        StatusMessage statusMessage = new StatusMessage("The password has been updated successfully");
        statusMessage.setUserEmail(details.getUsername());
        logService.changePass(details.getUsername());
        return new ResponseEntity<>(statusMessage, HttpStatus.OK);
    }

}
