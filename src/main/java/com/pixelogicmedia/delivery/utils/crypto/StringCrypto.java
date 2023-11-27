package com.pixelogicmedia.delivery.utils.crypto;

import com.pixelogicmedia.delivery.utils.SpringContext;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class StringCrypto {

    private static final String ALGORITHM = "AES";

    private StringCrypto() {
    }

    public static String encrypt(final String str) {
        try {
            final var cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, getKey());
            final byte[] cipherText = cipher.doFinal(str.getBytes());
            return Base64.getEncoder()
                    .encodeToString(cipherText);
        } catch (final NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException |
                       BadPaddingException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }

    public static String decrypt(final String str) {
        try {
            final var cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, getKey());
            final byte[] text = cipher.doFinal(Base64.getDecoder().decode(str));
            return new String(text);
        } catch (final NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException |
                       BadPaddingException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }

    private static Key getKey() {
        final var encodedKey = SpringContext.getConfigurationValue("crypto.key");
        final var bytes = Base64.getDecoder().decode(encodedKey);
        return new SecretKeySpec(bytes, ALGORITHM);
    }

}
