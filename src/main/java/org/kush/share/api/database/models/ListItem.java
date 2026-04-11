package org.kush.share.api.database.models;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(
        uniqueConstraints = @UniqueConstraint(columnNames = {"description, list_id"})
)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListItem
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "item_id")
    protected UUID id;

    @Column
    private String link;

    @Column
    private String description;

    @ManyToOne
    @JoinColumn(name = "list_id")
    private UserList userList;
}
