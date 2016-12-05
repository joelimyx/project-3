package com.joelimyx.flipvicefeed.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.joelimyx.flipvicefeed.classes.TimeObject;
import com.joelimyx.flipvicefeed.classes.TopicObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by KorbBookProReturns on 11/30/16.
 */

public class AlarmSQLHelper extends SQLiteOpenHelper {
    private static final String TAG = AlarmSQLHelper.class.getCanonicalName();

    private static final int DATABASE_VERSION = 3;
    public static final String DATABASE_NAME = "notifications.db";

    public static final String ALARMS_TABLE = "ALARMS_TABLE";

    public static final String COL_ID = "ID";
    public static final String COL_DAY = "DAY";
    public static final String COL_HOUR = "HOUR";
    public static final String COL_MIN = "MINUTE";

    public static final String[] ALL_COLUMNS = {COL_ID,COL_DAY,COL_HOUR,COL_MIN};

    private static final String CREATE_TABLE =
            "CREATE TABLE " + ALARMS_TABLE + "(" +
                    COL_ID + " INTEGER NOT NULL PRIMARY KEY, " +
                    COL_DAY + " INTEGER, " +
                    COL_HOUR + " INTEGER, " +
                    COL_MIN + " INTEGER)";

    public static final String TOPIC_FILTER_TABLE = "TOPIC_LIST_TABLE";
    public static final String COL_TOPIC_NAME = "topic";
    public static final String COL_TOPIC_SELECTED = "selected";

    public static final String[] TOPIC_COLUMNS = {COL_TOPIC_NAME,COL_TOPIC_SELECTED};

    public static final String CREATE_TOPIC_TABLE =
            "CREATE TABLE "+TOPIC_FILTER_TABLE+"("+
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, "+
                    COL_TOPIC_NAME+" TEXT, "+
                    COL_TOPIC_SELECTED+" INTEGER )";

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
        db.execSQL(CREATE_TOPIC_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + ALARMS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + TOPIC_FILTER_TABLE);
        this.onCreate(db);
    }

    /*---------------------------------------------------------------------------------
    // TOPIC FILTER PART
    ---------------------------------------------------------------------------------*/
    public List<TopicObject> getAllTopic(){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(
                TOPIC_FILTER_TABLE,
                TOPIC_COLUMNS,
                null,
                null,
                null,
                null,
                null,
                null);
        List<TopicObject> topicList = new ArrayList<>();
        if (cursor.moveToFirst()){
            while (!cursor.isAfterLast()){
                topicList.add(new TopicObject(
                        cursor.getString(cursor.getColumnIndex(COL_TOPIC_NAME)),
                        cursor.getInt(cursor.getColumnIndex(COL_TOPIC_SELECTED))
                ));
                cursor.moveToNext();
            }
        }
        cursor.close();
        return topicList;
    }

    public List<TopicObject> getFavoriteTopics(){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(
                TOPIC_FILTER_TABLE,
                TOPIC_COLUMNS,
                null,null,null,null,null,null);
        List<TopicObject> favoriteTopicList = new ArrayList<>();
        if(cursor.moveToFirst()){
            while(!cursor.isAfterLast()){
                int isFavorite = cursor.getInt(cursor.getColumnIndex(COL_TOPIC_SELECTED));

                if(isFavorite == 1) {
                    favoriteTopicList.add(new TopicObject(
                            cursor.getString(cursor.getColumnIndex(COL_TOPIC_NAME)),
                            cursor.getInt(cursor.getColumnIndex(COL_TOPIC_SELECTED))
                    ));
                }
                cursor.moveToNext();
            }
        }
        cursor.close();
        return favoriteTopicList;
    }

    public void updateSelectedTopic(String topic, boolean changedState){
        ContentValues values = new ContentValues();
        int state = changedState ? 1:0;
        values.put(COL_TOPIC_SELECTED,state);

        SQLiteDatabase db = getWritableDatabase();
        db.update(TOPIC_FILTER_TABLE,
                values,
                COL_TOPIC_NAME+" = ?",
                new String[]{topic});
        db.close();
    }
    /*---------------------------------------------------------------------------------
    // ALARM NOTIFICATION PART
    ---------------------------------------------------------------------------------*/

    //---------------------------------------------
    //  Method for getting which boxes were checked
    //---------------------------------------------

    public List<TimeObject> getAllDaysAndTime() {
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
