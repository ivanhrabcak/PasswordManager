package com.ivik.passwordmanager;

import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.tozny.crypto.android.AesCbcWithIntegrity;

import static com.tozny.crypto.android.AesCbcWithIntegrity.*;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Iterator;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import static javax.crypto.Cipher.ENCRYPT_MODE;

public class PasswordManager {
    private JSONObject passwords;
    private String userKey;
    public static final String SALT = "1EVAXlM3HxNDD3m4l1ojcIYtqtA8jEhTRD40m4/2YLK5ak8lLElSYyBKbD7QJ2RdBGicw37I7/8PHD4rm6a1eb0Z0I4oZVANEB03cLFE3vUKP1NqDOnBgUgq62Gwt9InzHNrnBRddSooorfynzIpQiTyRYObx83oxcvHAloVfPM=";

    public PasswordManager(String userKey) {
        passwords = new JSONObject();
        this.userKey = userKey;
    }

    public JSONObject getJSONObject() {
        return passwords;
    }


    public static String encryptString(String input, String key) {
        try {
            String salt = SALT;
            SecretKeys k = generateKeyFromPassword(key, salt.getBytes());

            CipherTextIvMac cipherTextIvMac = encrypt(input, k);
            String encrypted = cipherTextIvMac.toString();
            return encrypted;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String decryptString(String input, String key) {
        try {
            String salt = SALT;
            SecretKeys k = generateKeyFromPassword(key, salt.getBytes());

            CipherTextIvMac cipherTextIvMac1 = new CipherTextIvMac(input);
            String decrypted = AesCbcWithIntegrity.decryptString(cipherTextIvMac1, k);
            return decrypted;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String hashString(String input) {
        if (input == null || input.equals("")) {
            return " ";
        }
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            byte[] messageDigest = md.digest(input.getBytes());

            BigInteger no = new BigInteger(1, messageDigest);
            String hashtext = no.toString(16);

            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }

            return hashtext;
        }
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Account> getPasswords(String userKey) throws JSONException { // TODO: fix D:
        List<Account> accounts = new ArrayList<>();
        for (Iterator<String> it = passwords.keys(); it.hasNext();) {
            String encryptedUsername = it.next();
            String encryptedPassword = passwords.getString(encryptedUsername);

            String decryptedPassword = decryptString(encryptedPassword, userKey);
            String decryptedUsername = decryptString(encryptedPassword, userKey);
            System.out.println("a");
            accounts.add(new Account(decryptedPassword, decryptedUsername));
        }
        return accounts;
    }

    public void addAccount(Account account) {
        try {
            addAccount(account.getUsername(), account.getPassword());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void addAccount(String username, String password) throws Exception {
        String encryptedUsername = encryptString(username, userKey);
        String encryptedPassword  = encryptString(password, userKey);
        System.out.println(encryptedPassword);
        System.out.println(encryptedUsername);
        passwords.put(encryptedUsername, encryptedPassword);
        System.out.println(passwords.toString());
    }

    public Account getAccountByUsername(String username, String userKey) {
        for (Iterator<String> it = passwords.keys(); it.hasNext();) {
            String encryptedUsername = it.next();
            String decryptedUsername = null;
            try {
                decryptedUsername = decryptString(encryptedUsername, userKey);
            } catch (Exception e) {
                return null;
            }
            if (decryptedUsername.equals(username)) {
                try {
                    return new Account(decryptString(passwords.getString(encryptedUsername), userKey), decryptedUsername);
                } catch (JSONException e) {
                    return null;
                }
            }
        }
        return null;
    }

    public void parseAccountsFromJsonData(String data) throws JSONException {
        System.out.println(data);
        passwords = new JSONObject(data);
    }

    public String getJsonData() {
        return passwords.toString();
    }
}
