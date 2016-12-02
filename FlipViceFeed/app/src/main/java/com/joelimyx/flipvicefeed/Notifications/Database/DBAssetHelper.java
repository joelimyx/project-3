package com.joelimyx.flipvicefeed.Notifications.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

/**
 * Created by KorbBookProReturns on 11/30/16.
 */

public class DBAssetHelper extends SQLiteAssetHelper {
    private static final String DATABASE_NAME = "notifications.db";
    private static final int DATABASE_VERSION = 1;

    public DBAssetHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);


    }
}
