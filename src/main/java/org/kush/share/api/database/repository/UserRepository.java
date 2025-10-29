package org.kush.share.api.database.repository;

import org.kush.share.api.database.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String>
{
    Optional<User> findByEmail(String email);
}
