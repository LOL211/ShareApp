package org.kush.share.api.database.models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Entity
@Table(name = "user_list")
@Data
public class UserList
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "list_id")
    protected long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User createdBy;

    @ManyToMany(fetch = FetchType.LAZY)
    private Set<User> sharedWith;

    @Column(name = "list_name")
    private String listName;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "userList")
    private List<ListItem> items;
}
