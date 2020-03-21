package com.ivik.passwordmanager;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class AccountsContract {
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private AccountsContract() {}

    /* Inner class that defines the table contents */
    public static class FeedEntry implements BaseColumns {
        public static final String TABLE_NAME = "DEBILNICEK";
        public static final String PASSWORD_COLUMN_NAME_TITLE = "PASSWORD";
        public static final String USERNAME_COLUMN_NAME_TITLE = "USERNAME";

        public static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + FeedEntry.TABLE_NAME + " (" +
                        FeedEntry._ID + " INTEGER PRIMARY KEY," +
                        FeedEntry.PASSWORD_COLUMN_NAME_TITLE + " TEXT," +
                        FeedEntry.USERNAME_COLUMN_NAME_TITLE + " TEXT)";

        public static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + FeedEntry.TABLE_NAME;


    }
}

