package org.kush.share.api.database.repository;

import org.kush.share.api.database.models.ListItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemsRepository extends JpaRepository<ListItem, String>
{
}
