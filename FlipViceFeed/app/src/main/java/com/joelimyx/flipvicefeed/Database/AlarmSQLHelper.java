package com.joelimyx.flipvicefeed.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.joelimyx.flipvicefeed.notifications.TimeObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by KorbBookProReturns on 11/30/16.
 */

public class AlarmSQLHelper extends SQLiteOpenHelper {
    private static final String TAG = AlarmSQLHelper.class.getCanonicalName();

    private static final int DATABASE_VERSION = 1;

    public static final String DATABASE_NAME = "notifications.db";

    public static final String ALARMS_TABLE = "ALARMS_TABLE";

    public static final String COL_ID = "ID";
    public static final String COL_DAY = "DAY";
    public static final String COL_HOUR = "HOUR";
    public static final String COL_MIN = "MINUTE";

    public static final String[] ALL_COLUMNS = {COL_ID,COL_DAY,COL_HOUR,COL_MIN};
    public static final String[] JUST_THE_HOUR_COLUMN = {COL_HOUR};

    private static final String CREATE_TABLE =
            "CREATE TABLE " + ALARMS_TABLE + "(" +
                    COL_ID + " INTEGER NOT NULL PRIMARY KEY, " +
                    COL_DAY + " INTEGER, " +
                    COL_HOUR + " INTEGER, " +
                    COL_MIN + " INTEGER)";

    private static AlarmSQLHelper mInstance;

    public static AlarmSQLHelper getInstance(Context context){
        if(mInstance == null){
            mInstance = new AlarmSQLHelper(context.getApplicationContext());
        }
        return mInstance;
    }

    private AlarmSQLHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + ALARMS_TABLE);
        this.onCreate(db);
    }


    //---------------------------------------------
    //  Method for getting which boxes were checked
    //---------------------------------------------

    public List<TimeObject> getAllForCheckboxes() {
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(ALARMS_TABLE,
                ALL_COLUMNS,
                null,null,null,null,null);

        List<TimeObject> checkList = new ArrayList<>();

        if(cursor.moveToFirst()){
            while(!cursor.isAfterLast()){
                Integer hour = cursor.getInt(cursor.getColumnIndex(COL_HOUR));
                Integer minute = cursor.getInt(cursor.getColumnIndex(COL_MIN));
                Integer day = cursor.getInt(cursor.getColumnIndex(COL_DAY));
                checkList.add(new TimeObject(hour,minute,day));
                cursor.moveToNext();
            }
        }
        cursor.close();
        return checkList;
    }


    //---------------------------------------------
    //  Method for updating days and time
    //---------------------------------------------

    public void updateDaysAndTime(int hour, int minute, int day){
        ContentValues values = new ContentValues();
        values.put(COL_HOUR,hour);
        values.put(COL_MIN,minute);

        SQLiteDatabase db = getWritableDatabase();
        db.update(ALARMS_TABLE,values,COL_DAY+" = ?",new String[]{Integer.toString(day)});
        db.close();
    }

}
