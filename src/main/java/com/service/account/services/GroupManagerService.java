package com.service.account.services;

import com.service.account.exceptions.RoleManageException;
import com.service.account.exceptions.RoleNotFoundException;
import com.service.account.models.Group;
import com.service.account.models.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class GroupManagerService {

    private final UserService userService;
    private final GroupService groupService;

    private final LogService logService;

    public GroupManagerService(UserService userService, GroupService groupService, LogService logService) {
        this.userService = userService;
        this.groupService = groupService;
        this.logService = logService;
    }

    public void grantRole(User user, String role) {
        Group adminRole = groupService.getByName("ROLE_ADMINISTRATOR");
        Group group = groupService.getByName(role);
        if(group != null) {
            if (group.getName().equals("ROLE_ADMINISTRATOR") || user.getUserGroups().contains(adminRole)) {
                throw new RoleManageException("The user cannot combine administrative and business roles!");
            }
            user.getUserGroups().add(group);
            logService.grantRole(user, role);
            userService.update(user);
        } else {
            throw new RoleNotFoundException("Role not found!");
        }
    }

    public void removeRole(User user, String role) {
        Group group = groupService.getByName(role);
        if(group != null) {
            if (!user.getUserGroups().contains(group)) {
                throw new RoleManageException("The user does not have a role!");
            }
            if (group.getName().equals("ROLE_ADMINISTRATOR")) {
                throw new RoleManageException("Can't remove ADMINISTRATOR role!");
            }
            if (user.getUserGroups().size() == 1) {
                throw new RoleManageException("The user must have at least one role!");
            }
            user.getUserGroups().remove(group);
            logService.removeRole(user, role);
            userService.update(user);
        } else {
            throw new RoleNotFoundException("Role not found!");
        }
    }
}
