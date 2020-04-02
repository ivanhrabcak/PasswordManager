package com.ivik.passwordmanager;

import android.content.Context;

import com.ivik.passwordmanager.database.Database;
import com.tozny.crypto.android.AesCbcWithIntegrity;

import static com.tozny.crypto.android.AesCbcWithIntegrity.*;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public class PasswordManager {
    private Database database;
    private String userKey;
    public static final String SALT = "1EVAXlM3HxNDD3m4l1ojcIYtqtA8jEhTRD40m4/2YLK5ak8lLElSYyBKbD7QJ2RdBGicw37I7/8PHD4rm6a1eb0Z0I4oZVANEB03cLFE3vUKP1NqDOnBgUgq62Gwt9InzHNrnBRddSooorfynzIpQiTyRYObx83oxcvHAloVfPM=";

    public PasswordManager(String userKey, Context context) {
        this.userKey = userKey;
        database = new Database(context);
    }

    public Database getDatabase() {
        return database;
    }

    public void removeAccount(Account account) {
        String encryptedUsername = PasswordManager.encryptString(account.getUsername(), userKey);
        String encryptedPassword = PasswordManager.encryptString(account.getPassword(), userKey);
        String encryptedWebpage = PasswordManager.encryptString(account.getWebpage(), userKey);
        String encryptedApplication = PasswordManager.encryptString(account.getApp(), userKey);
        Account encryptedAccount = new Account(account.getId(), encryptedPassword, encryptedUsername, encryptedWebpage, encryptedApplication);
        database.removeAccount(encryptedAccount);
        }


    public static String encryptString(String input, String key) {
        try {
            SecretKeys k = generateKeyFromPassword(key, SALT.getBytes());

            CipherTextIvMac cipherTextIvMac = encrypt(input, k);
            String encrypted = cipherTextIvMac.toString();
            System.out.println("ClearText: " + input);
            System.out.println("CipherText: " + encrypted);
            return encrypted;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String decryptString(String input, String key) {
        try {
            SecretKeys k = generateKeyFromPassword(key, SALT.getBytes());

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

    public List<Account> getPasswords(String userKey) {
        return database.getAccounts(userKey);
    }

    public Account getDataForApplication(String packageName, String userKey) {
        List<Account> accounts = getPasswords(userKey);
        for (Account account : accounts) {
            String appId = account.getApp();
            if (appId != null) {
                if (appId.equals(packageName)) {
                    return account;
                }
            }
        }
        return null;
    }

    public Account getDataForWebpage(String webpage, String userKey) {
        List<Account> accounts = getPasswords(userKey);
        for (Account account : accounts) {
            if (account.getWebpage().contains(webpage)) {
                return account;
            }
        }
        return null;
    }

    public void addAccount(Account account) {
        try {
            addAccount(account.getUsername(), account.getPassword(), account.getWebpage(), account.getApp());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void addAccount(String username, String password, String webpage, String application) throws Exception {
        String encryptedUsername = encryptString(username, userKey);
        String encryptedPassword  = encryptString(password, userKey);
        String encryptedWebpage = encryptString(webpage, userKey);
        String encryptedApplication = encryptString(application, userKey);
        database.insertAccount(new Account(encryptedPassword, encryptedUsername, encryptedWebpage, encryptedApplication));
    }

    public void apply() {
        database.saveToDatabase();
    }
}
