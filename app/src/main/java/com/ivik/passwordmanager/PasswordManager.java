package com.ivik.passwordmanager;

import android.os.Build;

import androidx.annotation.RequiresApi;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Iterator;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class PasswordManager {
    private JSONObject passwords;
    private String userKey;

    public PasswordManager(String userKey) {
        passwords = new JSONObject();
        this.userKey = userKey;
    }

    public JSONObject getJSONObject() {
        return passwords;
    }

        public static String encryptString(String input, String key) {
        try {

            Cipher c = Cipher.getInstance("AES");
            SecretKeySpec k = new SecretKeySpec(key.getBytes(), "AES");
            c.init(ENCRYPT_MODE, k);

            byte[] encryptedData = c.doFinal(input.getBytes());
            String encodedData = Base64.getEncoder().encodeToString(encryptedData);
            return encodedData;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String decryptString(String input, String key) {
        try {
            Cipher c = Cipher.getInstance("AES");
            SecretKeySpec k = new SecretKeySpec(key.getBytes(), "AES");
            c.init(Cipher.DECRYPT_MODE, k);
            byte[] encryptedData = Base64.getDecoder().decode(input);
            byte[] data = c.doFinal(encryptedData);
            return data.toString();
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
