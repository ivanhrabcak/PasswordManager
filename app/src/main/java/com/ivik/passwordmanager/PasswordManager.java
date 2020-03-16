package com.ivik.passwordmanager;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
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
        char[] shortKey = key.toCharArray();
        char[] longKey = new char[32];
        for (int i = 0; i < 32; i++) {
            if (i < shortKey.length) {
                longKey[i] = shortKey[i];
            }
            else {
                longKey[i] = '0';
            }
        }

        key = String.valueOf(longKey);
        System.out.println(key);
        try {
            Cipher c = Cipher.getInstance("AES");
            SecretKeySpec k = new SecretKeySpec(key.getBytes(), "AES");
            c.init(Cipher.ENCRYPT_MODE, k);

            byte[] encryptedData = c.doFinal(input.getBytes());

            return encryptedData.toString();
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String decryptString(String input, String key) {
        char[] shortKey = key.toCharArray();
        char[] longKey = new char[32];
        for (int i = 0; i < 32; i++) {
            if (i < shortKey.length) {
                longKey[i] = shortKey[i];
            }
            else {
                longKey[i] = '0';
            }
        }

        key = String.valueOf(longKey);
        System.out.println(key);
        try {
            Cipher c = Cipher.getInstance("AES");
            SecretKeySpec k = new SecretKeySpec(key.getBytes(), "AES");
            c.init(Cipher.DECRYPT_MODE, k);

            byte[] decrypted = c.doFinal(input.getBytes());

            return decrypted.toString();
        }
        catch (Exception e) {
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

    public List<Account> getPasswords(String userKey) { // TODO: fix D:
        List<Account> accounts = new ArrayList<>();
        for (Iterator<String> it = passwords.keys(); it.hasNext();) {
            String encryptedUsername = it.next();
            String encryptedPassword;

            String decryptedPassword;
            String decryptedUsername;
            try {
                encryptedPassword = passwords.getString(encryptedUsername);

                decryptedPassword = decryptString(encryptedPassword, userKey);
                decryptedUsername = decryptString(encryptedPassword, userKey);
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
            catch (Exception e) {
                e.printStackTrace();
                return null;
            }

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
