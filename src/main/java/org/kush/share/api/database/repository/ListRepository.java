package org.kush.share.api.database.repository;

import org.kush.share.api.database.models.User;
import org.kush.share.api.database.models.UserList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ListRepository extends JpaRepository<UserList, String>
{
    @Query("select ul from UserList ul where :user member of ul.sharedWith or ul.createdBy = :user")
    List<UserList> findAllListsOfAUser(@Param("user") User user);

    @Query("select ul from UserList ul " +
            "join ListItem li on li.userList = ul " +
            "where :user member of ul.sharedWith or ul.createdBy = :user")
    List<UserList> findAllListsOfAUserWithItems(@Param("user") User user);
}
