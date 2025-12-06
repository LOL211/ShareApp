package org.kush.share.api.client;

import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.context.SecurityContextHolder;

@Import(FeignClientsConfiguration.class)
public class AuthFeignClientConfig {

    @Value("${vaulty.client.id}")
    private String clientId;

    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            requestTemplate.header("client_id", clientId);

            if (!requestTemplate.url().endsWith("/public"))
            {
                String delegateToken = (String) SecurityContextHolder.getContext().getAuthentication().getCredentials();
                requestTemplate.header("Authorization", "Bearer " + delegateToken);
            }
        };
    }

}
