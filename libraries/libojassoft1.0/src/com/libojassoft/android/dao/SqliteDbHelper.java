package com.libojassoft.android.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SqliteDbHelper extends SQLiteOpenHelper {

    // Table Name
    public static final String TABLE_NOTES = "NOTES";
    public static final String TABLE_REMINDER = "REMINDER";
    public static final String TABLE_NOTIFICATION = "NOTIFICATION";

    // Table Notes columns
    public static final String ID = "id";
    public static final String MONTH = "month";
    public static final String DAY = "day";
    public static final String YEAR = "year";
    public static final String CATEGORY_ID = "categoryId";
    public static final String TAGS = "tags";
    public static final String NOTES = "notes";
    public static final String TIMESTAMP = "timestamp";

    // Table Remindar columns
    public static final String HOUR = "hour";
    public static final String MINUTE = "minute";
    public static final String DESCRIPTION = "description";
    public static final String ISREPEAT_EVERYMONTH = "isRepeatEveryMonth";
    public static final String NOTE_ID = "noteId";

    // Table Noticication columns
    public static final String MESSAGE = "message";
    public static final String TITLE = "title";
    public static final String LINK = "link";
    public static final String NT_ID = "ntId";
    public static final String EXTRA = "extra";
    public static final String IMG_URL = "imgUrl";
    public static final String BLOG_ID = "blogId";
    public static final String NOTIFICATION_TYPE = "notificationType";

    // Database Information
    static final String DB_NAME = "CALENDAR.DB";

    // database version
    static final int DB_VERSION = 2;

    // Creating NOTES table query
    private static final String CREATE_TABLE_NOTES = "CREATE TABLE IF NOT EXISTS " + TABLE_NOTES + "("
            + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + MONTH + " INTEGER, "
            + DAY + " INTEGER, "
            + YEAR + " INTEGER, "
            + CATEGORY_ID + " INTEGER, "
            + TAGS + " TEXT, "
            + NOTES + " TEXT, "
            + TIMESTAMP + " TEXT);";

    // Creating REMINDER table query
    private static final String CREATE_TABLE_REMINDER = "CREATE TABLE IF NOT EXISTS " + TABLE_REMINDER + "("
            + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + YEAR + " INTEGER, "
            + MONTH + " INTEGER, "
            + DAY + " INTEGER, "
            + HOUR + " INTEGER, "
            + MINUTE + " INTEGER, "
            + ISREPEAT_EVERYMONTH + " INTEGER, "
            + DESCRIPTION + " TEXT, "
            + NOTE_ID + " INTEGER, "
            + TIMESTAMP + " TEXT);";

    // Creating NOTES table query
    private static final String CREATE_TABLE_NOTIFICATION = "CREATE TABLE IF NOT EXISTS " + TABLE_NOTIFICATION + "("
            + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + MESSAGE + " TEXT, "
            + TITLE + " TEXT, "
            + LINK + " TEXT, "
            + NT_ID + " TEXT, "
            + EXTRA + " TEXT, "
            + IMG_URL + " TEXT, "
            + BLOG_ID + " TEXT, "
            + NOTIFICATION_TYPE + " INTEGER, "
            + TIMESTAMP + " TEXT);";


    public SqliteDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(CREATE_TABLE_NOTES);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            db.execSQL(CREATE_TABLE_REMINDER);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            db.execSQL(CREATE_TABLE_NOTIFICATION);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTES);
        //db.execSQL("DROP TABLE IF EXISTS " + TABLE_REMINDER);
        onCreate(db);
    }
}
