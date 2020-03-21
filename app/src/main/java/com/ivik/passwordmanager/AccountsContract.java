package com.ivik.passwordmanager;

import android.provider.BaseColumns;

public final class AccountsContract {
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private AccountsContract() {}

    /* Inner class that defines the table contents */
    public static class FeedEntry implements BaseColumns {
        public static final String TABLE_NAME = "DEBILNICEK";
        public static final String PASSWORD_COLUMN_NAME = "PASSWORD";
        public static final String USERNAME_COLUMN_NAME = "USERNAME";
        public static final String ID_COLUMN_NAME = "ID";

        public static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + FeedEntry.TABLE_NAME + " (" +
                        FeedEntry._ID + " INTEGER PRIMARY KEY," +
                        FeedEntry.PASSWORD_COLUMN_NAME + " TEXT," +
                        FeedEntry.USERNAME_COLUMN_NAME + " TEXT," +
                        FeedEntry.ID_COLUMN_NAME + " int)";

        public static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + FeedEntry.TABLE_NAME;


    }
}

