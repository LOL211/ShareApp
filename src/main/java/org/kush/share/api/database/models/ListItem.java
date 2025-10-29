package org.kush.share.api.database.models;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table
@Data
public class ListItem
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "item_id")
    protected long id;

    @Column
    private String link;

    @Column
    private String description;

    @ManyToOne
    @JoinColumn(name = "list_id")
    private UserList userList;
}
