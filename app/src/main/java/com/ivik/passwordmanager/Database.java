package com.ivik.passwordmanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
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

    public void removeAccount(Account account) { // ????????????
        //System.out.println(readableDatabase.rawQuery("select * from " + AccountsContract.FeedEntry.TABLE_NAME + " where " + AccountsContract.FeedEntry.USERNAME_COLUMN_NAME + " = '" + account.getUsername() + "'" + " and " + AccountsContract.FeedEntry.PASSWORD_COLUMN_NAME + " = '" + account.getPassword() + "'", null));
//        writableDatabase.rawQuery("delete from " + AccountsContract.FeedEntry.TABLE_NAME + " where " + AccountsContract.FeedEntry.USERNAME_COLUMN_NAME + " = '" + account.getUsername() + "' and " + AccountsContract.FeedEntry.PASSWORD_COLUMN_NAME + " = '" + account.getPassword() + "'", null);
//        writableDatabase.rawQuery("delete from " + AccountsContract.FeedEntry.TABLE_NAME + " where " + AccountsContract.FeedEntry.PASSWORD_COLUMN_NAME + " = '" + account.getPassword() + "' and " + AccountsContract.FeedEntry.USERNAME_COLUMN_NAME + " = '" + account.getUsername() + "'", null);

        String selectionPassword = AccountsContract.FeedEntry.PASSWORD_COLUMN_NAME + " = '" + account.getPassword() + "' and "
                + AccountsContract.FeedEntry.USERNAME_COLUMN_NAME + " = '" + account.getUsername() + "' and "
                + AccountsContract.FeedEntry.WEBPAGE_COLUMN_NAME + " = '" + account.getWebpage() + "' and "
                + AccountsContract.FeedEntry.APPLICATION_COLUMN_NAME + " = '" + account.getApp() + "'";

        String selectionUsername = AccountsContract.FeedEntry.USERNAME_COLUMN_NAME + " = '" + account.getUsername() + "' and "
                + AccountsContract.FeedEntry.PASSWORD_COLUMN_NAME + " = '" + account.getPassword() + "' and "
                + AccountsContract.FeedEntry.WEBPAGE_COLUMN_NAME + " = '" + account.getWebpage() + "' and "
                + AccountsContract.FeedEntry.APPLICATION_COLUMN_NAME + " = '" + account.getApp() + "'";

        String selectionWebpage = AccountsContract.FeedEntry.WEBPAGE_COLUMN_NAME + " = '" + account.getWebpage() + "' and "
                + AccountsContract.FeedEntry.PASSWORD_COLUMN_NAME + " = '" + account.getPassword() + "' and "
                + AccountsContract.FeedEntry.USERNAME_COLUMN_NAME + " = '" + account.getUsername() + "' and "
                + AccountsContract.FeedEntry.APPLICATION_COLUMN_NAME + " = '" + account.getApp() + "'";

        String selectionApplication = AccountsContract.FeedEntry.APPLICATION_COLUMN_NAME + "= '" + account.getApp() + "' and"
                + AccountsContract.FeedEntry.PASSWORD_COLUMN_NAME + " = '" + account.getPassword() + "' and "
                + AccountsContract.FeedEntry.USERNAME_COLUMN_NAME + " = '" + account.getUsername() + "' and "
                + AccountsContract.FeedEntry.WEBPAGE_COLUMN_NAME + " = '" + account.getWebpage() + "'";

        int result1 = writableDatabase.delete(AccountsContract.FeedEntry.TABLE_NAME, selectionPassword, null); // returns number of rows deleted
        int result2 = writableDatabase.delete(AccountsContract.FeedEntry.TABLE_NAME, selectionUsername, null);
        int result3 = writableDatabase.delete(AccountsContract.FeedEntry.TABLE_NAME, selectionWebpage, null);
        int result4 = writableDatabase.delete(AccountsContract.FeedEntry.TABLE_NAME, selectionApplication, null);

        System.out.println(result1 + result2 + result3 + result4); // 0?
        refreshDatabase();
    }


    public List<Account> accountsCursorToList(Cursor cursor, String userKey) {
        if (cursor.getCount() == 0) {
            return new ArrayList<>();
        }
        List<Account> accounts = new ArrayList<>();

        while (cursor.moveToNext()) {
            String encryptedUsername = cursor.getString(cursor.getColumnIndex(AccountsContract.FeedEntry.USERNAME_COLUMN_NAME));
            String encryptedPassword = cursor.getString(cursor.getColumnIndex(AccountsContract.FeedEntry.PASSWORD_COLUMN_NAME));
            String encryptedWebpage = cursor.getString(cursor.getColumnIndex(AccountsContract.FeedEntry.WEBPAGE_COLUMN_NAME));
            String encryptedApplication = cursor.getString(cursor.getColumnIndex(AccountsContract.FeedEntry.APPLICATION_COLUMN_NAME));

            String decryptedUsername = PasswordManager.decryptString(encryptedUsername, userKey);
            String decryptedPassword = PasswordManager.decryptString(encryptedPassword, userKey);
            String decryptedWebpage = PasswordManager.decryptString(encryptedWebpage, userKey);
            String decryptedApplication = PasswordManager.decryptString(encryptedApplication, userKey);

            Account account = new Account(decryptedPassword, decryptedUsername, decryptedWebpage, decryptedApplication);
            accounts.add(account);
        }

        return accounts;
    }

    public List<Account> getAccounts(String userKey) {
        refreshDatabase();

        Cursor cursor = readableDatabase.rawQuery("select * from " + AccountsContract.FeedEntry.TABLE_NAME, null);
        return accountsCursorToList(cursor, userKey);
    }

    public void saveToDatabase() {
        writableDatabase.insert(AccountsContract.FeedEntry.TABLE_NAME, null, contentValues);
    }
}
