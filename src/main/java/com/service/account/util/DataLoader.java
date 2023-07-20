package com.service.account.util;

import com.service.account.models.Group;
import com.service.account.repositories.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DataLoader {

    private final GroupRepository groupRepository;

    @Autowired
    public DataLoader(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
        createRoles();
    }

    private void createRoles() {
        try {
            groupRepository.save(new Group("ROLE_ADMINISTRATOR"));
            groupRepository.save(new Group("ROLE_USER"));
            groupRepository.save(new Group("ROLE_ACCOUNTANT"));
            groupRepository.save(new Group("ROLE_AUDITOR"));
        } catch (Exception e) {
        }
    }
}
