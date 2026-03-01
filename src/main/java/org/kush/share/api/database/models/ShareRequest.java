package org.kush.share.api.database.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
@Table
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShareRequest
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "share_id")
    private UUID shareId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="shared_list")
    private UserList sharedList;

    @Column(name = "request_status")
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private ShareRequestStatus requestStatus = ShareRequestStatus.VALID;

    @Column(name = "expires_at")
    private ZonedDateTime expiresAt;
}
