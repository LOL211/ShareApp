package org.kush.share.api.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@FeignClient(
        name = "Auth",
        configuration = AuthFeignClientConfig.class,
        url = "${vaulty.auth.url}"
)
public interface AuthFeignClient {

    @RequestMapping(method = RequestMethod.GET, value = "/api/public")
    String getPublicKey();

    @RequestMapping(method = RequestMethod.POST, value = "/api/user")
    Map<UUID, String> getUsersInfo(List<UUID> ids);
}
