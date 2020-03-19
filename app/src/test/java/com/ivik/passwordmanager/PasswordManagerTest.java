package com.ivik.passwordmanager;

import org.junit.Test;

import static org.junit.Assert.*;
import com.ivik.passwordmanager.PasswordManager;
import com.tozny.crypto.android.AesCbcWithIntegrity;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;

public class PasswordManagerTest {
    @Test
    public void encryptAndDecrypt_test() throws GeneralSecurityException, UnsupportedEncodingException {
        String key = "1234";
        String secretString = "12345";
        String salt = AesCbcWithIntegrity.saltString(AesCbcWithIntegrity.generateSalt());
        AesCbcWithIntegrity.SecretKeys realKey = AesCbcWithIntegrity.generateKeyFromPassword(key, salt.getBytes());

        AesCbcWithIntegrity.CipherTextIvMac cipherTextIvMac = AesCbcWithIntegrity.encrypt(secretString, realKey);
        String encrypted = cipherTextIvMac.toString();

        AesCbcWithIntegrity.CipherTextIvMac cipherTextIvMac1 = new AesCbcWithIntegrity.CipherTextIvMac(encrypted);
        String decrypted = AesCbcWithIntegrity.decryptString(cipherTextIvMac1, realKey);
        assertEquals(secretString, decrypted);
    }
}
