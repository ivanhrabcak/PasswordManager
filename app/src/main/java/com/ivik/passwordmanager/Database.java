package com.ivik.passwordmanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ivik.passwordmanager.Account;
import com.ivik.passwordmanager.PasswordManager;
import com.ivik.passwordmanager.AccountsContract;
import com.ivik.passwordmanager.AccountsDb;

import java.util.ArrayList;
import java.util.List;

public class Database {
    private AccountsDb accountsDb;
    private SQLiteDatabase readableDatabase;
    private SQLiteDatabase writableDatabase;
    private ContentValues contentValues;

    public Database(Context context) {
        accountsDb = new AccountsDb(context);
        refreshDatabase();
        contentValues = new ContentValues();
    }

    public void refreshDatabase() {
        readableDatabase = accountsDb.getReadableDatabase();
        writableDatabase = accountsDb.getWritableDatabase();
    }

    public void insertAccount(Account account) {
        contentValues.put(AccountsContract.FeedEntry.USERNAME_COLUMN_NAME, account.getUsername());
        contentValues.put(AccountsContract.FeedEntry.PASSWORD_COLUMN_NAME, account.getPassword());
        contentValues.put(AccountsContract.FeedEntry.WEBPAGE_COLUMN_NAME, account.getWebpage());
        contentValues.put(AccountsContract.FeedEntry.APPLICATION_COLUMN_NAME, account.getApp());
        saveToDatabase();
    }

    public void removeAccount(Account account) {
        int result1 = writableDatabase.delete(AccountsContract.FeedEntry.TABLE_NAME, AccountsContract.FeedEntry._ID + " = ?", new String[] {String.valueOf(account.getId())});
        System.out.println(result1); // 0?
        refreshDatabase();
    }


    public List<Account> accountsCursorToList(Cursor cursor, String userKey) {
        if (cursor.getCount() == 0) {
            return new ArrayList<>();
        }
        List<Account> accounts = new ArrayList<>();

        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex(AccountsContract.FeedEntry._ID));
            String encryptedUsername = cursor.getString(cursor.getColumnIndex(AccountsContract.FeedEntry.USERNAME_COLUMN_NAME));
            String encryptedPassword = cursor.getString(cursor.getColumnIndex(AccountsContract.FeedEntry.PASSWORD_COLUMN_NAME));
            String encryptedWebpage = cursor.getString(cursor.getColumnIndex(AccountsContract.FeedEntry.WEBPAGE_COLUMN_NAME));
            String encryptedApplication = cursor.getString(cursor.getColumnIndex(AccountsContract.FeedEntry.APPLICATION_COLUMN_NAME));

            String decryptedUsername = PasswordManager.decryptString(encryptedUsername, userKey);
            String decryptedPassword = PasswordManager.decryptString(encryptedPassword, userKey);
            String decryptedWebpage = PasswordManager.decryptString(encryptedWebpage, userKey);
            String decryptedApplication = PasswordManager.decryptString(encryptedApplication, userKey);

            Account account = new Account(id, decryptedPassword, decryptedUsername, decryptedWebpage, decryptedApplication);
            accounts.add(account);
        }

        return accounts;
    }

    public List<Account> getAccounts(String userKey) {
        refreshDatabase();

//        Cursor cursor = readableDatabase.rawQuery("select * from " + AccountsContract.FeedEntry.TABLE_NAME, null);
        Cursor cursor = readableDatabase.query(AccountsContract.FeedEntry.TABLE_NAME, null, null, null, null, null, null);
        return accountsCursorToList(cursor, userKey);
    }

    public void saveToDatabase() {
        writableDatabase.insert(AccountsContract.FeedEntry.TABLE_NAME, null, contentValues);
    }
}
