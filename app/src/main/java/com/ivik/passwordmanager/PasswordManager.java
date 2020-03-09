package com.ivik.passwordmanager;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
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

    public static String encryptString(String input, String key) throws Exception {
        Cipher c = Cipher.getInstance("AES");
        SecretKeySpec k = new SecretKeySpec(key.getBytes(), "AES");
        c.init(Cipher.ENCRYPT_MODE, k);

        byte[] encryptedData = c.doFinal(input.getBytes());

        return encryptedData.toString();
    }

    public static String decryptString(String input, String key) throws Exception {
        Cipher c = Cipher.getInstance("AES");
        SecretKeySpec k = new SecretKeySpec(key.getBytes(), "AES");
        c.init(Cipher.DECRYPT_MODE, k);

        byte[] decrypted = c.doFinal(input.getBytes());

        return decrypted.toString();
    }

    public static String hashString(String input) // Copied
    {
        if (input == null || input.equals("")) {
            return " ";
        }
        try {
            // getInstance() method is called with algorithm SHA-512
            MessageDigest md = MessageDigest.getInstance("SHA-512");

            // digest() method is called
            // to calculate message digest of the input string
            // returned as array of byte
            byte[] messageDigest = md.digest(input.getBytes());

            // Convert byte array into signum representation
            BigInteger no = new BigInteger(1, messageDigest);

            // Convert message digest into hex value
            String hashtext = no.toString(16);

            // Add preceding 0s to make it 32 bit
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }

            // return the HashText
            return hashtext;
        }

        // For specifying wrong message digest algorithms
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Account> getPasswords(String userKey) {
        List<Account> accounts = new ArrayList<>();
        for (Iterator<String> it = passwords.keys(); it.hasNext();) {
            String encryptedUsername = it.next();
            String encryptedPassword = null;
            try {
                encryptedPassword = passwords.getString(encryptedUsername);
            } catch (JSONException e) {
                return null;
            }
            String decryptedPassword = null;
            String decryptedUsername = null;
            try {
                decryptedPassword = decryptString(encryptedPassword, userKey);
                decryptedUsername = decryptString(encryptedPassword, userKey);
            }
            catch (Exception e) {
                return null;
            }
            accounts.add(new Account(decryptedPassword, decryptedUsername));
        }
        return accounts;
    }
    
    public boolean addAccount(Account account) {
        return addAccount(account.getUsername, account.getPassword());
    }
    

    public boolean addAccount(String username, String password) {
        String encryptedUsername = null;
        String encryptedPassword = null;
        try {
            encryptedUsername = encryptString(username, userKey);
            encryptedPassword = encryptString(password, userKey);
        } catch (Exception e) {
            return false;
        }
        try {
            passwords.put(encryptedUsername, encryptedPassword);
        } catch (JSONException e) {
            return false;
        }
        return true;
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
                    return new Account(decryptString(passwords.get(encryptedUsername), userKey), decryptedUsername);
                } catch (JSONException e) {
                    return null;
                }
            }
        }
        return null;
    }

    public String getJsonData() {
        return passwords.toString();
    }
}
