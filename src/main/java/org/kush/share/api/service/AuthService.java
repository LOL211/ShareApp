package org.kush.share.api.service;

import lombok.RequiredArgsConstructor;
import org.bouncycastle.util.encoders.Base64;
import org.kush.share.api.client.AuthClientFeignClient;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Service;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthClientFeignClient authClientFeignClient;

    @Cacheable(cacheNames = "jwt-decoder")
    public JwtDecoder getJwtDecoder() throws NoSuchAlgorithmException, InvalidKeySpecException {
        String publicKey = authClientFeignClient.getPublicKey();
        byte[] rawKey = Base64.decode(publicKey);

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(rawKey);
        var key = keyFactory.generatePublic(keySpec);

        return NimbusJwtDecoder.withPublicKey((RSAPublicKey) key).build();
    }
}
