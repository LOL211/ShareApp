package org.kush.share.api.database.models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "user_list")
@Data
public class UserList
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "list_id")
    protected UUID id;

    @Column(name = "created_by")
    private UUID createdBy;

    @ElementCollection
    @CollectionTable(name = "list_shared_with", joinColumns = @JoinColumn(name = "list_id"))
    @Column(name = "shared_with_user")
    private Set<UUID> sharedWith;

    @Column(name = "list_name")
    private String listName;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "userList")
    private List<ListItem> items;
}
