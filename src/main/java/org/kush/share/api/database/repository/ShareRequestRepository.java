package org.kush.share.api.database.repository;

import org.kush.share.api.database.models.ShareRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ShareRequestRepository extends JpaRepository<ShareRequest, UUID>
{
    @Query("select sr.shareId " +
            "from ShareRequest sr " +
            "where sr.requestStatus = org.kush.share.api.database.models.ShareRequestStatus.USED and " +
            "sr.expiresAt < CURRENT_TIMESTAMP")
    List<UUID> findAllInvalidShareRequests();

}
