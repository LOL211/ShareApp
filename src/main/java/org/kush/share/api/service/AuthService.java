package org.kush.share.api.service;

import lombok.RequiredArgsConstructor;
import org.bouncycastle.util.encoders.Base64;
import org.kush.share.api.client.AuthFeignClient;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthFeignClient authFeignClient;

    @Cacheable(cacheNames = "public-key")
    public RSAPublicKey getPublicKey() throws NoSuchAlgorithmException, InvalidKeySpecException {
        String publicKey = authFeignClient.getPublicKey();
        byte[] rawKey = Base64.decode(publicKey);

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(rawKey);
        var key = keyFactory.generatePublic(keySpec);
        return (RSAPublicKey) key;
    }
}
