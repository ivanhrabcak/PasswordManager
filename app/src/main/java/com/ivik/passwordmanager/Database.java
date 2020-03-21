package com.ivik.passwordmanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

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
        contentValues.put(AccountsContract.FeedEntry.USERNAME_COLUMN_NAME_TITLE, account.getUsername());
        contentValues.put(AccountsContract.FeedEntry.PASSWORD_COLUMN_NAME_TITLE, account.getPassword());
    }

    public void removeAccount(Account account) {
        contentValues.remove(account.getUsername());
    }

    public List<Account> accountsCursorToList(Cursor cursor, String userKey) {
        if (cursor.getCount() == 0) {
            return new ArrayList<>();
        }
        List<Account> accounts = new ArrayList<>();

        while (cursor.moveToNext()) {
            String encryptedUsername = cursor.getString(cursor.getColumnIndex(AccountsContract.FeedEntry.USERNAME_COLUMN_NAME_TITLE));
            String encryptedPassword = cursor.getString(cursor.getColumnIndex(AccountsContract.FeedEntry.PASSWORD_COLUMN_NAME_TITLE));

            String decryptedUsername = PasswordManager.decryptString(encryptedUsername, userKey);
            String decryptedPassword = PasswordManager.decryptString(encryptedPassword, userKey);

            Account account = new Account(decryptedPassword, decryptedUsername);
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
