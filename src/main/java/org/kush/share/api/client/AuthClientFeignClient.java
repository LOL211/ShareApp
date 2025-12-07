package org.kush.share.api.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(
        name = "Auth",
        configuration = AuthFeignClientConfig.class,
        url = "${vaulty.auth.url}"
)
public interface AuthClientFeignClient {

    @RequestMapping(method = RequestMethod.GET, value = "/api/public")
    String getPublicKey();
}
