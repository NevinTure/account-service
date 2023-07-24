package com.service.account.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
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
