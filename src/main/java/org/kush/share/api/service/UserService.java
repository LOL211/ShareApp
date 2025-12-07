package org.kush.share.api.service;

import lombok.RequiredArgsConstructor;
import org.kush.share.api.client.AuthOAuthFeignClient;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UserService {
    //TODO: Add a cache invalidation endpoint so the auth server can update the cache here

    private final AuthOAuthFeignClient authClientFeignClient;
    private final CacheManager cacheManager;

    public Map<UUID, String> getUsernamesForUuids(List<UUID> userIds) {
        Cache cache = cacheManager.getCache("user-cache");
        Map<UUID, String> result = new HashMap<>();
        List<UUID> missing = new ArrayList<>();


        for (UUID uuid : userIds) {
            String cached = cache.get(uuid, String.class);
            if (cached != null) {
                result.put(uuid, cached);
            } else {
                missing.add(uuid);
            }
        }


        if (missing.isEmpty()) {
            return result;
        }

        Map<UUID, String> fetched = authClientFeignClient.getUsersInfo(missing);

        for (Map.Entry<UUID, String> entry : fetched.entrySet()) {
            cache.put(entry.getKey(), entry.getValue());
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }

    public String getUsernameForUuid(UUID userId) {
        return getUsernamesForUuids(List.of(userId)).get(userId);
    }
}
