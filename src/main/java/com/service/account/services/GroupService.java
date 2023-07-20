package com.service.account.services;

import com.service.account.models.Group;
import com.service.account.repositories.GroupRepository;
import org.springframework.stereotype.Service;

@Service
public class GroupService {

    private final GroupRepository repository;

    public GroupService(GroupRepository repository) {
        this.repository = repository;
    }

    public Group getByName(String name) {
        return repository.getGroupByName(name.toUpperCase()).orElse(null);
    }
}
