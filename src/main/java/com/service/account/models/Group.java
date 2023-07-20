package com.service.account.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "groups")
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "group_id")
    @Setter
    @Getter
    private long id;

    @Column(name = "name", unique = true)
    @Getter
    @Setter
    private String name;

    @ManyToMany(mappedBy = "userGroups")
    @Setter
    @Getter
    private List<User> users;

    public Group(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
