package com.pixelogicmedia.delivery.utils.crypto;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.crypto.KeyGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
public class StringCryptoTests {

    @Test
    void generateAesKey() throws NoSuchAlgorithmException {
        final var keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(256);
        final var key = keyGenerator.generateKey();

        final var encodedKey = Base64.getEncoder().encodeToString(key.getEncoded());

        assertFalse(encodedKey.isEmpty());
        System.out.println(encodedKey);
    }

    @Test
    void canEncryptAndDecrypt() {
        final var plain = UUID.randomUUID().toString();
        final var encrypted = StringCrypto.encrypt(plain);
        final var recovered = StringCrypto.decrypt(encrypted);

        assertEquals(plain, recovered);
    }
}
