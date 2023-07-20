package com.service.account.view;

import com.service.account.dto.UserDTO;
import com.service.account.exceptions.AdministratorActionException;
import com.service.account.models.User;
import com.service.account.services.GroupManagerService;
import com.service.account.services.UserService;
import com.service.account.util.ManagerRequest;
import com.service.account.util.StatusMessage;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin")
@Validated
public class AdminController {

    private final UserService userService;
    private final GroupManagerService groupManagerService;
    private final ModelMapper mapper;


    @Autowired
    public AdminController(UserService userService, GroupManagerService groupManagerService, ModelMapper mapper) {
        this.userService = userService;
        this.groupManagerService = groupManagerService;
        this.mapper = mapper;
    }

    @GetMapping("/user/")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> userDTOs = new ArrayList<>();
        for (User user : userService.getAll()) {
            UserDTO userDTO = mapper.map(user, UserDTO.class);
            userDTO.setPassword(null);
            userDTOs.add(userDTO);
        }
        return new ResponseEntity<>(userDTOs, HttpStatus.OK);
    }

    @DeleteMapping(value = {"/user/{user}", "/user/*", "/user"})
    public ResponseEntity<StatusMessage> deleteUser(@PathVariable(name = "user", required = false) String userEmail) {
        if (userEmail == null) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        Optional<User> userToDelete = userService.getUserByEmail(userEmail);
        System.out.println(userToDelete);
        if (userToDelete.isPresent()) {
            UserDetails userAdmin = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (userAdmin.getUsername().equals(userEmail)) {
                throw new AdministratorActionException("Can't remove ADMINISTRATOR role!");
            }
            userService.deleteUser(userToDelete.get());
            StatusMessage statusMessage = new StatusMessage("Deleted successfully!");
            statusMessage.setUsername(userEmail);
            return new ResponseEntity<>(statusMessage, HttpStatus.OK);
        } else {
            throw new UsernameNotFoundException("User not found!");
        }
    }

    @PutMapping("/user/role")
    public ResponseEntity<UserDTO> manageRole(@RequestBody ManagerRequest managerRequest) {
        Optional<User> user = userService.getUserByEmail(managerRequest.getEmail());
        if (user.isPresent()) {
            String role = "ROLE_" + managerRequest.getRole();
            switch (managerRequest.getOperation()) {
                case "GRANT" -> groupManagerService.grantRole(user.get(), role);
                case "REMOVE" -> groupManagerService.removeRole(user.get(), role);
            }
        } else {
            throw new UsernameNotFoundException("User not found!");
        }
        UserDTO userDTO = mapper.map(user, UserDTO.class);
        userDTO.setPassword(null);
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    @PutMapping("/user/access")
    public ResponseEntity<StatusMessage> manageLock(@RequestBody ManagerRequest managerRequest) {
        Optional<User> user = userService.getUserByEmail(managerRequest.getEmail());
        if (user.isPresent()) {
            switch (managerRequest.getOperation()) {
                case "LOCK" -> userService.lockUser(user.get());
                case "UNLOCK" -> userService.unlockUser(user.get());
            }
        } else {
            throw new UsernameNotFoundException("User not found!");
        }
        return new ResponseEntity<>(new StatusMessage(
                String.format("User %s %s!",
                        managerRequest.getEmail().toLowerCase(),
                        managerRequest.getOperation().toLowerCase() + "ed")
        ), HttpStatus.OK);
    }

}
